Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	Ext.QuickTips.init();

	function getParameter(psName) {
		var url = self.location;
		var result = "";
		var str = self.location.search.substring(0);
		if (str.indexOf(psName) != -1
				&& (str.substr(str.indexOf(psName) - 1, 1) == "?" || str
						.substr(str.indexOf(psName) - 1, 1) == "&")) {
			if (str.substring(str.indexOf(psName), str.length).indexOf("&") != -1) {
				var Test = str.substring(str.indexOf(psName), str.length);
				result = Test.substr(Test.indexOf(psName), Test.indexOf("&")
								- Test.indexOf(psName));
				result = result.substring(result.indexOf("=") + 1,
						result.length);
			} else {
				result = str.substring(str.indexOf(psName), str.length);
				result = result.substring(result.indexOf("=") + 1,
						result.length);
			}
		}
		return result;
	}

	var woCode = getParameter("woCode");

	var workorderStatus = getParameter("workorderStatus");

	/** 弹窗元素↓↓* */

	/** 弹窗元素↑↑* */
	/** 窗口元素↓↓* */

	// 描述
		// 弹出画面

	var memoText = new Ext.form.TextArea({
				id : "memoText",
				maxLength : 128,
				width : 180
			});

	var win = new Ext.Window({
				height : 170,
				width : 350,
				layout : 'fit',
				modal : true,
				resizable : false,
				closeAction : 'hide',
				items : [memoText],
				buttonAlign : "center",
				title : '详细信息录入窗口',
				buttons : [{
					text : '保存',
					iconCls : 'save',
					handler : function() {
						if (!memoText.isValid()) {
							return;
						}
						var record = northGrid.selModel.getSelected();
						record.set("logContent",
								Ext.get("memoText").dom.value);
						win.hide();
					}
				}, {
					text : '关闭',
					iconCls : 'cancer',
					handler : function() {
						win.hide();
					}
				}]
			});
	
	
	var planDescription = new Ext.form.TextArea({
		id : "planDescription",

		fieldLabel : '日志',
		name : 'baseInfo.planDescription',
		allowBlank : true,
//		readOnly:true,
		blankText : '描述...',
		listeners : {
			"render" : function() {
				this.el.on("dblclick", function() {
							var record = northGrid.getSelectionModel().getSelected();
							var value = record
									.get('logContent');
							memoText.setValue(value);
							win.x = undefined;
							win.y = undefined;
							win.show();
						})
			}
		},
		anchor : '99%'
	});



	/** 弹窗元素↑↑* */
	/** 窗口元素↓↓* */
	var datalist = new Ext.data.Record.create([{
				name : 'logContent'
			}, {
				name : 'id'
			}, {
				name : 'woCode'
			}]);

	var northGridsm = new Ext.grid.CheckboxSelectionModel();

	var northGrids = new Ext.data.JsonStore({
				url : 'workbill/getEquJWoLog.action?woCode=' + woCode,
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});

	northGrids.load({
				params : {
					start : 0,
					limit : 18
				}
			});

	/** northGrid的按钮组↓↓* */

	// 增加
	function addRecord() {
		var count = northGrids.getCount();
		var currentIndex = count;
		var o = new datalist({
					logContent : '',

					woCode : woCode
				});
		northGrid.stopEditing();
		northGrids.insert(currentIndex, o);
		northGridsm.selectRow(currentIndex);
		northGrid.startEditing(currentIndex, 2);
	}

	var btnAdd = new Ext.Button({
				text : "新增",
				iconCls : 'add',
				handler : addRecord
			})

	// 保存
	function saveModifies() {
		northGrid.stopEditing();
		var modifyRec = northGrid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			Ext.Msg.confirm('提示信息', '确定要保存修改数据吗?', function(button) {
				if (button == 'yes') {
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						updateData.push(modifyRec[i].data);
					}
					Ext.Ajax.request({
								url : 'workbill/saveEquJWoLog.action',
								method : 'post',
								params : {
									isUpdate : Ext.util.JSON.encode(updateData),
									isDelete : ids.join(",")
								},
								success : function(result, request) {
									var o = eval('(' + result.responseText
											+ ')');
									Ext.MessageBox.alert('提示信息', o.msg);
									northGrids.rejectChanges();
									ids = [];
									northGrids.reload();
								},
								failure : function(result, request) {
									Ext.MessageBox.alert('提示信息', '未知错误！')
								}
							})
				}
			})

		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}

	var btnSave = new Ext.Button({
				text : "保存修改",
				iconCls : 'save',
				handler : saveModifies
			})

	// 删除记录
	var ids = new Array();
	function deleteRecords() {
		northGrid.stopEditing();
		var selected = northGrid.getSelectionModel().getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("id") != null) {
					ids.push(member.get("id"));
				}
				northGrid.getStore().remove(member);
				northGrid.getStore().getModifiedRecords().remove(member);
			}
		}
	}

	var btnDelete = new Ext.Button({
				text : "删除",
				iconCls : 'delete',
				handler : deleteRecords
			})

	// 取消
	function cancel() {
		var modifyRec = northGrids.getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			Ext.Msg.confirm('提示信息', '确定要放弃修改数据吗?', function(button) {
						if (button == 'yes') {
							northGrids.reload();
							northGrids.rejectChanges();
							ids = [];
						}
					})

		} else {
			northGrids.reload();
			northGrids.rejectChanges();
			ids = [];
		}
	}

	var btnCancel = new Ext.Button({
				text : "取消",
				iconCls : 'cancer',
				handler : cancel
			})
	/** northGrid的按钮组↑↑* */
	// 列表

	var northGrid = new Ext.grid.EditorGridPanel({
		ds : northGrids,
		columns : [northGridsm, new Ext.grid.RowNumberer(), {
					width : 700,
					header : "日志内容",
					sortable : false,
					dataIndex : 'logContent',
					editor : planDescription
				}],
		sm : northGridsm,
		tbar : [btnAdd, btnDelete, btnCancel, '-', btnSave,'<font color="red">提示：在"日志内容"列单元格编辑状态中双击可弹出窗口编辑</font>'],
		frame : true,
		border : true,
		clicksToEdit:1,//单击一次编辑
		enableColumnHide : false,
		enableColumnMove : false
			// ,
			// iconCls : 'icon-grid'
		});

	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : 'border',
				border : false,
				items : [{
							title : "",
							region : 'center',
							layout : 'fit',
							// border : false,
							// margins : '0',
							// height : 200,
							// split : false,
							// collapsible : false,
							items : [northGrid]
						}]
			});

	if (woCode == '' || workorderStatus > '0') {

		btnAdd.setDisabled(true);
		btnDelete.setDisabled(true);
		btnCancel.setDisabled(true);
		btnSave.setDisabled(true);;

	}

})