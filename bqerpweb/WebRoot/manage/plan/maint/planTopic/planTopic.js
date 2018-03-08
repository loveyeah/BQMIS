Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	/** 右边的grid * */

	var MyRecord = Ext.data.Record.create([{
				name : 'baseInfo.id.topicCode'
			}, {
				name : 'baseInfo.id.itemCode'
			}, {
				name : 'baseInfo.displayNo'
			}, {
				name : 'itemName'
			}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'manageplan/getBpCPlanTopicItem.action '
			});

	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"

			}, MyRecord);

	var store = new Ext.data.Store({

				proxy : dataProxy,

				reader : theReader

			});

	// store.load({
	// params : {
	// start : 0,
	// limit : 18
	// }
	// });

	var sm = new Ext.grid.CheckboxSelectionModel();

	number = new Ext.grid.RowNumberer({
				header : "",
				align : 'left'
			})

	// 指标选择
	function addRecord() {
		var recL = Grid.getSelectionModel().getSelected();

		if (recL) {
			if (recL.get("topicCode") != null) {
				var args = {

					topicCode : recL.get("topicCode")
				}

				var url = "selectItem.jsp";
				var rvo = window
						.showModalDialog(
								url,
								args,
								'dialogWidth:600px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

				store.load({
							params : {
								topicCode : recL.get("topicCode")
//								,
//								start : 0,
//								limit : 18
							}
						});
				// if (typeof(rvo) != "undefined") {
				//
				// var i, j, k ;
				// for (i = 0; i < rvo.length; i++) {
				// k=0;
				// for (j = 0; j < store.getModifiedRecords().length; j++) {
				// if (store.getModifiedRecords()[j]
				// .get("baseInfo.id.itemCode") == rvo[i].itemCode) {
				// k = 1;
				// break;
				// }
				// }
				// if(k==0){
				// var count = store.getCount();
				//
				// // var currentIndex = currentRecord ? rowNo : count;
				// var currentIndex = count;
				//
				// var o = new MyRecord({
				// 'baseInfo.id.topicCode' : '',
				// 'baseInfo.id.itemCode' : '',
				// 'baseInfo.displayNo' : '',
				// 'itemName' : ''
				// });
				//
				// grid.stopEditing();
				//
				// store.insert(currentIndex, o);
				// sm.selectRow(currentIndex);
				// o.set("baseInfo.id.topicCode", rvo[i].topicCode);
				// o.set("baseInfo.id.itemCode", rvo[i].itemCode);
				// o.set("baseInfo.displayNo", rvo[i].displayNo);
				// o.set("itemName", rvo[i].itemName);
				// grid.startEditing(currentIndex, 3);
				//
				// }
				//				
				// }
				// }

			} else {
				Ext.Msg.alert("提示", "请选择一条已保存过的记录！")
			}
		} else {
			Ext.Msg.alert("提示", "请选择左边的一条记录！")
		}

	}

	// 删除记录

	var ids = new Array();
	function deleteRecords() {
		var sm = grid.getSelectionModel();
		var selections = sm.getSelections();
		if (selections.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selections.length; i += 1) {
				var member = selections[i];
				var id = new Array();

				if (member.get("baseInfo.id.topicCode") != null
						&& member.get("baseInfo.id.itemCode")) {

					id.push(member.get("baseInfo.id.topicCode"));
					id.push(member.get("baseInfo.id.itemCode"));
					var idstr = '';
					idstr = id.join(",");

					ids.push(idstr);
				}
				grid.getStore().remove(member);
				grid.getStore().getModifiedRecords().remove(member);
			}
			// resetLine();
		}
	}
	// 保存
	function saveModifies() {
		grid.stopEditing();
		var modifyRec = store.getModifiedRecords();

		if (modifyRec.length > 0 || ids.length > 0) {
			if (!confirm("确定要保存修改吗?"))
				return;
			// var newData = new Array();
			var updateData = new Array();
			for (var i = 0; i < modifyRec.length; i++) {
				// if (modifyRec[i].get("id") == "") {
				// newData.push(modifyRec[i].data);
				// } else {
				updateData.push(modifyRec[i].data);
				// }
			}

			Ext.Ajax.request({
						url : 'manageplan/saveBpCPlanTopicItem.action',
						method : 'post',
						params : {
							isUpdate : Ext.util.JSON.encode(updateData),
							isDelete : ids.join(";")
						},
						success : function(result, request) {
							var o = eval('(' + result.responseText + ')');
							Ext.MessageBox.alert('提示信息', o.msg);
							store.rejectChanges();
							ids = [];
							store.reload();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('提示信息', '未知错误！')
						}
					})
		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}
	// 取消
	function cancel() {
		var modifyRec = store.getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			if (!confirm("确定要放弃修改吗"))
				return;
			store.reload();
			store.rejectChanges();
			ids = [];
		} else {
			store.reload();
			store.rejectChanges();
			ids = [];
		}
	}

	// 定义grid

	var grid = new Ext.grid.EditorGridPanel({
				// region : "west",
				store : store,
				layout : 'fit',
				// width:'0.5',
				columns : [

						sm, // 选择框
						number, {
							header : "指标编码",

							sortable : false,
							dataIndex : 'baseInfo.id.itemCode'

						}, {
							header : "指标名称",

							align : "center",
							sortable : true,
							dataIndex : 'itemName'

						}, {
							header : "显示顺序",

							align : "center",
							sortable : true,
							dataIndex : 'baseInfo.displayNo',
							editor : new Ext.form.NumberField({
										maxLength : 4
									})

						}],
				tbar : [{
							text : "指标选择",
							iconCls : 'add',

							handler : addRecord
						}

				],
				sm : sm
//				, // 选择框的选择 Shorthand for
//				// selModel（selectModel）
//				// 分页
//				bbar : new Ext.PagingToolbar({
//							pageSize : 18,
//							store : store,
//							hidden : true,
//							displayInfo : true,
//							displayMsg : "显示第{0}条到{1}条，共{2}条",
//							emptyMsg : "没有记录",
//							beforePageText : '',
//							afterPageText : ""
//						})
			});
	/** 右边的grid * */

	/** 左边的grid * */
	var con_item = Ext.data.Record.create([{
				name : 'topicCode'
			}, {
				name : 'topicName'
			}, {
				name : 'topicMemo'
			}, {
				name : 'displayNo'
			}]);

	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false

			});
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manageplan/getBpCPlanTopicList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, con_item)
			});
	con_ds.load({
				params : {

					start : 0,
					limit : 18

				}
			})

	var con_item_cm = new Ext.grid.ColumnModel([con_sm,

	{
				header : '主题编码',

				dataIndex : 'topicCode',
				align : 'center'

			}, {
				header : '主题名称',
				dataIndex : 'topicName',
				align : 'center',
				editor : new Ext.form.TextField()
			}, {
				header : '主题说明',
				dataIndex : 'topicMemo',
				align : 'center',
				editor : new Ext.form.TextArea()
			}, {
				header : '显示顺序',
				dataIndex : 'displayNo',
				align : 'center',
				editor : new Ext.form.NumberField({
							maxLength : 4
						})
			}]);
	con_item_cm.defaultSortable = true;

	var gridbbar = new Ext.PagingToolbar({
				pageSize : 18,
				store : con_ds,
				displayInfo : true,
				displayMsg : "显示第{0}条到{1}条，共{2}条",
				emptyMsg : "没有记录",
				beforePageText : '',
				afterPageText : ""
			});
	// 增加
	function addTopic() {

		var count = con_ds.getCount();

		var currentIndex = count;

		var o = new con_item({
					'topicName' : '',
					'topicMemo' : '',
					'displayNo' : ''

				});

		Grid.stopEditing();
		con_ds.insert(currentIndex, o);
		con_sm.selectRow(currentIndex);
		Grid.startEditing(currentIndex, 2);
		// resetLine();
	}

	// 删除记录
	var topicIds = new Array();
	function deleteTopic() {
		Grid.stopEditing();
		var sm = Grid.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("topicCode") != null) {
					topicIds.push(member.get("topicCode"));
				}
				Grid.getStore().remove(member);
				Grid.getStore().getModifiedRecords().remove(member);
			}
			// resetLine();
		}
	}
	// 保存
	function saveTopic() {
		Grid.stopEditing();
		var modifyRec = Grid.getStore().getModifiedRecords();

		if (modifyRec.length > 0 || topicIds.length > 0) {

			if (!confirm("确定要保存修改吗?"))
				return;
			// var newData = new Array();
			var updateData = new Array();
			for (var i = 0; i < modifyRec.length; i++) {
				if (modifyRec[i].get("topicName") == "") {
					Ext.MessageBox.alert('提示信息', '主题名称不能为空！')
					return
				}

				updateData.push(modifyRec[i].data);
				// }
			}

			Ext.Ajax.request({
						url : 'manageplan/saveBpCPlanTopic.action',
						method : 'post',
						params : {
							// isAdd : Ext.util.JSON.encode(newData),
							isUpdate : Ext.util.JSON.encode(updateData),
							isDelete : topicIds.join(",")
						},
						success : function(result, request) {
							var o = eval('(' + result.responseText + ')');
							Ext.MessageBox.alert('提示信息', o.msg);

							con_ds.rejectChanges();
							topicIds = [];
							con_ds.reload();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('提示信息', '未知错误！')
						}
					})
		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}
	// 取消
	function cancerTopic() {
		var modifyRec = con_ds.getModifiedRecords();
		if (modifyRec.length > 0 || topicIds.length > 0) {
			if (!confirm("确定要放弃修改吗"))
				return;
			con_ds.reload();
			con_ds.rejectChanges();
			topicIds = [];
		} else {
			con_ds.reload();
			con_ds.rejectChanges();
			topicIds = [];
		}
	}
	var contbar = new Ext.Toolbar({
				items : [{
							id : 'btnAdd',
							iconCls : 'add',
							text : "新增",
							handler : addTopic

						}, {
							id : 'btnDelete',
							iconCls : 'delete',
							text : "删除",
							handler : deleteTopic

						}, {
							id : 'btnCancer',
							iconCls : 'cancer',
							text : "取消",
							handler : cancerTopic

						}, '-', {
							id : 'btnSave',
							iconCls : 'save',
							text : "保存修改",
							handler : saveTopic
						}]

			});
	var Grid = new Ext.grid.EditorGridPanel({
		sm : con_sm,
		ds : con_ds,
		cm : con_item_cm,
		height : 425,
		// // title : '合同列表',
		// width : Ext.get('div_lay').getWidth(),
		// split : true,
		autoScroll : true,
		bbar : gridbbar,
		tbar : contbar,
		border : true,
		clicksToEdit : 2
			// ,
			// viewConfig : {
			// forceFit : true
			// }
		});

	Grid.on('rowclick', modifyBtn);
	function modifyBtn() {
		var recL = Grid.getSelectionModel().getSelected();

		if (recL) {
			if (recL.get("topicCode") != null) {
				store.load({
							params : {
								topicCode : recL.get("topicCode"),
								start : 0,
								limit : 18
							}
						});

			}

		} else {
			Ext.Msg.alert("提示", "请选择左边的一条记录！")
		}
	}
	/** 左边的grid * */

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [{
//							bodyStyle : "padding: 20,10,20,20",
							layout : 'fit',
							border : false,
							frame : false,
							region : "west",
							width : '45%',
							items : [Grid]
						}, {
//							bodyStyle : "padding: 20,20,20,0",
							region : "center",
							border : false,
							frame : false,
							layout : 'fit',
							// width : '50%',
							items : [grid]
						}]
			});

})