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

	function getCode(psName, postUrl) {
		var result = "";
		var str = postUrl.search.substring(0);
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
	var opCode;

	/** 弹窗元素↓↓* */

	// 工序名
	var operationStepTitle = new Ext.form.TextField({
				id : "operationStepTitle",
				xtype : "textfield",
				fieldLabel : '工序名',
				name : 'baseInfo.operationStepTitle',
				allowBlank : false,
				blankText : '工序名...',
				anchor : '99%'
			});

	// 工序内容
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
						record.set("description",Ext.get("memoText").dom.value);
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

	var description = new Ext.form.TextArea({
				id : "description",
				xtype : "textarea",
				fieldLabel : '描述',
				name : 'baseInfo.description',
				allowBlank : true,
				blankText : '描述...',
				anchor : '99%',
				listeners : {
					"render" : function() {
						this.el.on("dblclick", function() {
									var record = northGrid.getSelectionModel()
											.getSelected();
									var value = record.get('description');
									memoText.setValue(value);
									win.x = undefined;
									win.y = undefined;
									win.show();
								})
					}
				}
			});

	// 排序号
	var orderby = new Ext.form.NumberField({
				id : "orderby",
				xtype : "numberfield",
				fieldLabel : '排序号',
				name : 'baseInfo.orderby',
				blankText : '排序号...',
				anchor : '50%',
				allowBlank : false,//modify by wpzhu
				allowDecimals:false
				
				
				
			});

	// id
	var hidden_id = {
		id : "id",
		xtype : "textfield",
		name : 'baseInfo.id',
		hidden : true,
		anchor : '50%'
	};

	// woCode
	var hidden_woCode = {
		id : "woCode",
		xtype : "textfield",
		name : 'baseInfo.woCode',
		hidden : true,
		anchor : '50%'
	};

	// 弹窗的表单对象
	var blockForm = new Ext.form.FormPanel({
				labelAlign : 'right',
				frame : true,
				items : [{
							border : false,
							layout : 'form',
							items : [{
										border : false,
										layout : 'form',
										columnWidth : 1,
										items : [operationStepTitle]
									}, {
										border : false,
										layout : 'form',
										columnWidth : 1,
										items : [description]
									}, {
										border : false,
										layout : 'form',
										columnWidth : .5,
										items : [orderby, hidden_id,
												hidden_woCode]
									}]
						}]
			});

	var blockAddWindow;
	function showAddWindow() {
		if (!blockAddWindow) {
			blockAddWindow = new Ext.Window({
				layout : 'fit',
				width : 350,
				height : 200,
				modal : true,
				closable : true,
				border : false,
				resizable : false,
				closeAction : 'hide',
				plain : true,
				// 面板中按钮的排列方式
				buttonAlign : 'center',
				items : [blockForm],
				buttons : [{
					text : '保存',
					handler : function() {
						if (op == "add") {
							blockForm.getForm().submit({
								waitMsg : '保存中,请稍后...',
								url : "equstandard/saveEquCStandardOrderstep.action",
								success : function(form, action) {
									blockForm.getForm().reset();
									blockAddWindow.hide();
									northGrids.reload();
								},
								failure : function(form, action) {
									var o = eval('('
											+ action.response.responseText
											+ ')');
									Ext.MessageBox.alert('错误', o.eMsg);
								}
							});
						} else if (op == "edit") {
							blockForm.getForm().submit({
								waitMsg : '保存中,请稍后...',
								url : "equstandard/updateEquCStandardOrderstep.action",
								params : {
									id : northGrid.getSelectionModel()
											.getSelected().get("id")
								},
								success : function(form, action) {
									var o = eval('('
											+ action.response.responseText
											+ ')');
									blockForm.getForm().reset();
									blockAddWindow.hide();
									northGrids.reload();
								},
								failure : function(form, action) {
									var o = eval('('
											+ action.response.responseText
											+ ')');
									Ext.MessageBox.alert('错误', o.eMsg);
								}
							});
						} else {
							Ext.MessageBox.alert('错误', '未定义的操作');
						}
					}

				}, {
					text : '取消',
					handler : function() {
						blockForm.getForm().reset();
						blockAddWindow.hide();
					}
				}]
			});
		}
		if (op == "add") {
			// 新增时，赋初始值
			blockAddWindow.setTitle("添加工序");
			blockForm.getForm().reset();
			blockAddWindow.show();
			Ext.get("baseInfo.woCode").dom.value = woCode;
		} else if (op == "edit") {
			blockAddWindow.setTitle("修改工序");
			blockForm.getForm().reset();
			blockAddWindow.show();
			var rec = northGrid.getSelectionModel().getSelected();
			blockForm.getForm().loadRecord(rec);
		} else {
		}
		blockAddWindow.show(Ext.get('getrole'));
	};

	/** 弹窗元素↑↑* */
	/** 窗口元素↓↓* */
	var datalist = new Ext.data.Record.create([{
				name : 'description'
			}, {
				name : 'id'
			}, {
				name : 'opDuration'
			}, {
				name : 'operationStep'
			}, {
				name : 'operationStepTitle'
			}, {
				name : 'orderby'
			}, {
				name : 'pointName'
			}, {
				name : 'woCode'
			}]);

	var northGridsm = new Ext.grid.CheckboxSelectionModel();

	var northGrids = new Ext.data.JsonStore({
				url : 'equstandard/getEquCStandardOrderstepList.action?woCode='
						+ woCode,
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
					operationStepTitle : '',
					description : '',
					orderby : '',
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
								url : 'equstandard/saveEquCStandardOrderstep.action',
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
			northGrids.reload();
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
				if (member.get("id") != "") {
					ids.push(member.get("id"));
				
					northGrid.getStore().remove(member);
				}
			}
				    southTabPanel.setActiveTab(0);
					iframe0.location = "about:blank";
					iframe1.location = "about:blank";
					iframe3.location = "about:blank";
					opCode='';
				
				
				northGrid.getStore().getModifiedRecords().remove(member);
				northGrid.getView().refresh();
			
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
							header : "工序名",
							sortable : false,
							dataIndex : 'operationStepTitle',
							width:260,
							editor : operationStepTitle
						}, {
							header : "备注",
							sortable : false,
							dataIndex : 'description',
							editor : description
						}, {
							header : "排序号",
							width : 100,
							sortable : false,
							dataIndex : 'orderby',
							editor : orderby
						}, {
							header : "主键",
							width : 100,
							sortable : false,
							hidden : true,
							dataIndex : 'id'
						}],
				sm : northGridsm,
				tbar : [btnAdd, btnDelete, btnSave, btnCancel],
				frame : true,
				border : true,
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});

	northGrid.on('rowclick', function(grid, rowIndex, e) {
		if (northGrid.getSelectionModel().hasSelection()) {
					opCode = northGrid.getSelectionModel().getSelected()
							.get("operationStep");
//					alert(Ext.encode(northGrid.getSelectionModel()
//							.getSelected().data))
					if (northGrid.getSelectionModel().getSelected() != null
							&& northGrid.getSelectionModel().getSelections().length < 2
							&& woCode != null && opCode != null
							&& woCode != "undefined" && opCode != "undefined") {
						southTabPanel.setActiveTab(0);
						
						iframe0.location = "spstepman.jsp?woCode=" + woCode
								+ "&opCode=" + opCode;
					} else {
						southTabPanel.setActiveTab(0);
						iframe0.location = "about:blank";
					}
				}else{
					southTabPanel.setActiveTab(0);
						iframe0.location = "about:blank";
						iframe3.location = "about:blank";
						iframe1.location = "about:blank";
						opCode = null;
				}
			});

	// tab
	var southTabPanel = new Ext.TabPanel({
		activeTab : 0,
		layoutOnTabChange : true,
		tabPosition : 'top',
		plain : true,
		defaults : {
			autoScroll : true
		},
		frame : false,
		border : false,
		items : [{
			title : '人工',
			id : 'tab0',
			listeners : {
				activate : function() {
					if (woCode != null && opCode != null&&woCode != '' && opCode != ''
							&& woCode != "undefined" && opCode != "undefined") {
						if (getCode("woCode", iframe0.location) != woCode
								|| getCode("opCode", iframe0.location) != opCode)
							iframe0.location = "spstepman.jsp?woCode=" + woCode
									+ "&opCode=" + opCode;
					}
				}
			},
			html : '<iframe id="iframe0" src="" style="width:100%;height:100%;border:0px;"></iframe>'
		}, {
			title : '服务',
			id : 'tab3',
			listeners : {
				activate : function() {
					if (woCode != null && opCode != null&&woCode != '' && opCode != ''
							&& woCode != "undefined" && opCode != "undefined") {
						if (getCode("woCode", iframe3.location) != woCode
								|| getCode("opCode", iframe3.location) != opCode)
							iframe3.location = "spstepserv.jsp?woCode="
									+ woCode + "&opCode=" + opCode;
					}
				}
			},
			html : '<iframe id="iframe3" src="" style="width:100%;height:100%;border:0px;"></iframe>'
		}, {
			title : '工具',
			id : 'tab1',
			listeners : {
				activate : function() {
					if (woCode != null && opCode != null&&woCode != '' && opCode != ''
							&& woCode != "undefined" && opCode != "undefined")
						if (getCode("woCode", iframe1.location) != woCode
								|| getCode("opCode", iframe1.location) != opCode)
							iframe1.location = "spsteptools.jsp?woCode="
									+ woCode + "&opCode=" + opCode;

				}
			},
			html : '<iframe id="iframe1" style="width:100%;height:100%;border:0px;"></iframe>'

		}, {
			title : '材料',
			//hidden : true,
			id : 'tab2',
			listeners : {
				activate : function() {
					if (woCode != null && opCode != null
							&& woCode != "undefined" && opCode != "undefined")
						if (getCode("woCode", iframe2.location) != woCode
								|| getCode("opCode", iframe2.location) != opCode)
							iframe2.location = "spstepmat.jsp?woCode=" + woCode
									+ "&opCode=" + opCode;
				}
			},
			html : '<iframe id="iframe2" src="" style="width:100%;height:100%;border:0px;"></iframe>'
		}]
	});
	//隐藏材料子页面
  //  southTabPanel.remove("tab2");
	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							title : "",
							region : 'north',
							layout : 'fit',
							border : false,
							margins : '0',
							height : 200,
							split : false,
							collapsible : false,
							items : [northGrid]
						}, {
							title : "",
							region : 'center',
							layout : 'fit',
							border : false,
							margins : '1',
							items : [southTabPanel]
						}]
			});
});