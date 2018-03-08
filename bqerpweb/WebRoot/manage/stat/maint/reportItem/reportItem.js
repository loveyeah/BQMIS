Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	var MyRecord = Ext.data.Record.create([{
				name : 'baseInfo.id.accountCode'
			}, {
				name : 'baseInfo.id.itemCode'
			}, {
				name : 'baseInfo.displayNo'
			}, {
				name : 'itemName'
			}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'manager/getBpCAnalyseAccountItem.action '
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

	// 重置排序号
	function resetLine() {
		for (var j = 0; j < store.getCount(); j++) {
			var temp = store.getAt(j);
			temp.set("displayNo", j + 1);

		}
	}
	// 指标选择
	function addRecord() {
		var recL = Grid.getSelectionModel().getSelected();

		if (recL) {
			var args = {
				accountType : recL.get("baseInfo.accountType"),
				accountCode : recL.get("baseInfo.accountCode")
			}

			var url = "selectItem.jsp";
			var rvo = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:600px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(rvo) != "undefined") {

				var i;
				for (i = 0; i < rvo.length; i++) {

					// var currentRecord =
					// grid.getSelectionModel().getSelected();
					// var rowNo = store.indexOf(currentRecord);

					var count = store.getCount();

					// var currentIndex = currentRecord ? rowNo : count;
					var currentIndex = count;

					var o = new MyRecord({
								'baseInfo.id.accountCode' : rvo[i].accountCode,
								'baseInfo.id.itemCode' : rvo[i].itemCode,
								'baseInfo.displayNo' : rvo[i].displayNo,
								'itemName' : rvo[i].itemName
							});

					grid.stopEditing();

					store.insert(currentIndex, o);
					sm.selectRow(currentIndex);
					// grid.startEditing(currentIndex, 3);
					resetLine();
				}
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

				if (member.get("id.itemCode") != null
						&& member.get("id.formulaNo")) {

					id.push(member.get("id.itemCode"));
					id.push(member.get("id.formulaNo"));
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
		var modifyRec = grid.getStore().getModifiedRecords();
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
						url : 'manager/saveBpCAnalyseAccountItem.action',
						method : 'post',
						params : {
							isUpdate : Ext.util.JSON.encode(updateData),
							isDelete : ids.join(",")
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
							editor : new Ext.form.TextField()

						}],
				tbar : [{
							text : "指标选择",
							iconCls : 'add',

							handler : addRecord
						}

						, {
							text : "删除",
							iconCls : 'delete',
							handler : deleteRecords

						}, {
							text : "保存",
							iconCls : 'save',
							handler : saveModifies
						}, {
							text : "取消",
							iconCls : 'cancer',
							handler : cancel
						}],
				sm : sm, // 选择框的选择 Shorthand for
				// selModel（selectModel）
				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : store,
							displayInfo : true,
							displayMsg : "显示第{0}条到{1}条，共{2}条",
							emptyMsg : "没有记录",
							beforePageText : '',
							afterPageText : ""
						})
			});

	var con_item = Ext.data.Record.create([{
				name : 'baseInfo.accountCode'
			}, {
				name : 'baseInfo.accountName'
			}, {
				name : 'baseInfo.accountType'
			}]);

	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true

			});
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manager/getBpCAnalyseAccountList.action'
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

	var con_item_cm = new Ext.grid.ColumnModel([

	{
				header : '台帐编码',

				dataIndex : 'baseInfo.accountCode',
				align : 'center'

			}, {
				header : '台帐名称',
				dataIndex : 'baseInfo.accountName',
				align : 'center'
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

	var Grid = new Ext.grid.GridPanel({

		ds : con_ds,
		cm : con_item_cm,
		height : 425,
		// // title : '合同列表',
		// width : Ext.get('div_lay').getWidth(),
		split : true,
		autoScroll : true,
		bbar : gridbbar,
		// tbar : contbar,
		border : true
			// ,
			// viewConfig : {
			// forceFit : true
			// }
		});

	Grid.on('rowclick', modifyBtn);
	function modifyBtn() {
		var recL = Grid.getSelectionModel().getSelected();

		if (recL) {
			store.load({
						params : {
							accountCode : recL.get("baseInfo.accountCode"),
							start : 0,
							limit : 18
						}
					});

		} else {
			Ext.Msg.alert("提示", "请选择左边的一条记录！")
		}
	}

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [{
							bodyStyle : "padding: 20,10,20,20",
							layout : 'fit',
							border : false,
							frame : false,
							region : "west",
							width : '45%',
							items : [Grid]
						}, {
							bodyStyle : "padding: 20,20,20,0",
							region : "center",
							border : false,
							frame : false,
							layout : 'fit',
							// width : '50%',
							items : [grid]
						}]
			});

})