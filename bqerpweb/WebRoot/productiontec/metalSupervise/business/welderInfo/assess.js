Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	// 获得父页面的ID
	var weldId;
	var workerName;
	if (parent.weldId != null) {
		weldId = parent.weldId;
		workerName = parent.workerName;
	}

	// 人员姓名
	var Wname = new Ext.form.TextField({

				readOnly : true,
				anchor : '90%'
			})
	Wname.setValue(workerName);

	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false
			});
	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});

	/** 左边的grid * */
	var MyRecord = Ext.data.Record.create([{
				name : 'hgjnkhId'
			}, {
				name : 'weldId'
			}, {
				name : 'examDate'
			}, {
				name : 'checkUnit'
			}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'productionrec/findJnkhbListByweldId.action '
			});

	var theReader = new Ext.data.JsonReader({
				root : "",
				totalCount : ""

			}, MyRecord);

	var store = new Ext.data.Store({

				proxy : dataProxy,

				reader : theReader

			});
	store.load({
				params : ({
					weldId : weldId
				})
			})

	// 左边的Grid
	var grid = new Ext.grid.GridPanel({
				store : store,
				sm : sm,
				width : '1',
				columns : [sm, new Ext.grid.RowNumberer({
									header : '序号',
									width : 50
								}), {
							header : '考核时间',
							dataIndex : 'examDate',
							align : 'center',
							renderer : function(value) {
								return dateParse(value);
							}
						}, {
							header : '考核单位',
							dataIndex : 'checkUnit',
							align : 'center'
						}, {
							header : '考核单位',
							dataIndex : 'hgjnkhId',
							align : 'center'
						}],
				tbar : new Ext.Toolbar({
						
							style : "padding:10px 20px 0px 20px",
							items : ["人员姓名:", Wname]
						})
			});

	// 日期处理函数
	function dateParse(value) {

		if (value != null && value != "") {

			if (typeof(value) == 'object') {

				return value.format('Y-m-d');

			} else {

				var myYear = parseInt(value.substring(0, 4));
				var myMonth = parseInt(value.substring(5, 7));
				var myDay = parseInt(value.substring(8, 10));
				return myYear + '-' + myMonth + '-' + myDay;

			}
		} else {

			return "";
		}
	}

	// 右边表格的数据
	var con_item = Ext.data.Record.create([{
				name : 'hgjnkhxmId'
			}, {
				name : 'hgjnkhId'
			}, {
				name : 'examName'
			}, {
				name : 'material'
			}, {
				name : 'sizes'
			}, {
				name : 'method'
			}, {
				name : 'results'
			}, {
				name : 'allowWork'
			}]);
	// 右边Grid数据源
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'productionrec/findJnKhxmListByhgjnkhId.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "",
							root : ""
						}, con_item)
			});
	con_ds.load({
				params : {
					start : 0,
					limit : 18
				}
			});

	var con_item_cm = new Ext.grid.ColumnModel([con_sm,
			new Ext.grid.RowNumberer({
						header : "序号",
						width : 50
					}), {
				header : 'hgjnkhId',
				hidden : true,
				dataIndex : 'hgjnkhId',
				align : 'center'
			}, {
				header : '考核项目',

				dataIndex : 'examName',
				align : 'center',
				editor : new Ext.form.TextField()
			}, {
				header : '材质',
				dataIndex : 'material',
				align : 'center',
				editor : new Ext.form.TextField()
			}, {
				header : '规格型号',
				dataIndex : 'sizes',
				align : 'center',
				editor : new Ext.form.TextField()
			}, {
				header : '焊接方法',
				dataIndex : 'method',
				align : 'center',
				editor : new Ext.form.TextField()

			}, {
				header : '考核成绩',
				dataIndex : 'results',
				align : 'center',
				editor : new Ext.form.TextField()
			}, {
				header : '准予担当的焊接操作',
				dataIndex : 'allowWork',
				align : 'center',
				editor : new Ext.form.TextField()
			}
	]);

	con_item_cm.defaultSortable = true;

	// 增加
	function addTopic() {
		if (grid.selModel.hasSelection()
				&& grid.getSelectionModel().getSelections().length == 1) {
			var rec = grid.getSelectionModel().getSelected();
			var count = con_ds.getCount();
			var currentIndex = count;
			var o = new con_item({
						'hgjnkhId' : rec.get("hgjnkhId"),
						'examName' : '',
						'material' : '',
						'sizes' : '',
						'method' : '',
						'results' : '',
						'allowWork' : ''
					});
			Grid.stopEditing();
			con_ds.insert(currentIndex, o);
			con_sm.selectRow(currentIndex);
			Grid.startEditing(currentIndex, 2);
			// resetLine();
		} else {
			Ext.MessageBox.alert("提示", "请选择坐标的<一条>记录!");
		}
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
				if (member.get("hgjnkhxmId") != null) {
					topicIds.push(member.get("hgjnkhxmId"));
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
			var updateData = new Array();
			for (var i = 0; i < modifyRec.length; i++) {
				updateData.push(modifyRec[i].data);
			}
			Ext.Ajax.request({
						url : 'productionrec/saveJnKhxm.action',
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

	// 左边Grid的点击事件
	grid.on('click', modifyBtn);
	function modifyBtn() {
		var recL = grid.getSelectionModel().getSelected();

		if (recL) {
			if (recL.get("hgjnkhId") != null) {
				con_ds.load({
							params : {
								hgjnkhId : recL.get("hgjnkhId"),
								start : 0,
								limit : 18

							}
						})
			}
		}
	}
	// 右边的tbar
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
	// 右边的Grid
	var Grid = new Ext.grid.EditorGridPanel({
				sm : con_sm,
				ds : con_ds,
				cm : con_item_cm,
				height : 425,
				autoScroll : true,
				tbar : contbar,
				border : true,
				clicksToEdit : 2
			});

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [{
							layout : 'fit',
							border : false,
							frame : false,
							region : "west",
							width : '35%',
							items : [grid]
						}, {
							region : "center",
							border : false,
							frame : false,
							layout : 'fit',
							items : [Grid]
						}]
			});

})