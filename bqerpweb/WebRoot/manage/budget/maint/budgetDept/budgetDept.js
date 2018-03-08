Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
				name : 'centerId'
			}, {
				name : 'depCode'
			}, {
				name : 'depName'
			}, {
				name : 'manager'
			}, {
				name : 'ifDuty'
			}, {
				name : 'manageName'
			}, {
				name : 'costCode'
			}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'managebudget/findBudgetDeptList.action'
			});

	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, MyRecord);

	var store = new Ext.data.Store({
				proxy : dataProxy,
				reader : theReader
			});

	var fuzzy = new Ext.form.TextField({
				id : "fuzzy",
				name : "fuzzy"
			});

	var sm = new Ext.grid.CheckboxSelectionModel();
	var grid = new Ext.grid.GridPanel({
				region : "center",
				layout : 'fit',
				store : store,
				columns : [sm, new Ext.grid.RowNumberer(), {
							header : "centerId",
							width : 75,
							sortable : true,
							hidden : true,
							dataIndex : 'centerId'
						}, {
							header : "部门编码",
							width : 75,
							sortable : true,
							hidden : true,
							dataIndex : 'depCode'
						}, {
							header : "部门名称",
							width : 75,
							sortable : true,
							dataIndex : 'depName'
						}, {
							header : "部门负责人编码",
							width : 75,
							sortable : true,
							hidden : true,
							dataIndex : 'manager'
						}, {
							header : "部门负责人",
							width : 75,
							sortable : true,
							dataIndex : 'manageName'
						}, {
							header : "是否责任部门",
							width : 75,
							sortable : true,
							dataIndex : 'ifDuty',
							renderer : function(v) {
								if (v == 'Y') {
									return "是";
								}
								if (v == 'N') {
									return "否";
								}
							}
						}, {
							header : "成本中心",
							width : 75,
							sortable : true,
							hidden : true,
							dataIndex : 'costCode'
						}],
				sm : sm,
				autoSizeColumns : true,
				viewConfig : {
					forceFit : true
				},
				tbar : ['部门名称：', fuzzy, {
							text : "查询",
							iconCls : 'query',
							handler : queryRecord
						}, {
							text : "新增",
							id : 'btnAdd',
							iconCls : 'add',
							handler : addRecord
						}, {
							text : "修改",
							id : 'btnUpdate',
							iconCls : 'update',
							handler : updateRecord
						}, {
							text : "删除",
							iconCls : 'delete',
							handler : deleteRecord
						}],
				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : store,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						})
			});

	grid.on("rowdblclick", updateRecord);
	// ---------------------------------------

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				items : [grid]
			});
	// -------------------

	var centerId = new Ext.form.Hidden({
				id : "centerId",
				fieldLabel : 'ID',
				anchor : '90%',
				readOnly : true,
				name : 'dept.centerId'
			});

	var depCode = new Ext.form.TextField({
				id : "depCode",
				fieldLabel : '部门编码',
				anchor : '90%',
				readOnly : true,
				name : 'dept.depCode'
			});

	var depName = new Ext.form.TextField({
		fieldLabel : "部门名称",
		name : 'dept.depName',
		emptyText : '请选择...',
		anchor : '90%',
		listeners : {
			focus : function() {
				var args = {
					selectModel : 'single',
					rootNode : {
						id : "0",
						text : '灞桥电厂'
					}
				}
				var url = "../../../../comm/jsp/hr/dept/dept.jsp";
				var rvo = window
						.showModalDialog(
								url,
								args,
								'dialogWidth:500px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;')
				if (typeof(rvo) != "undefined") {
					depCode.setValue(rvo.codes);
					depName.setValue(rvo.names);

				}
				this.blur();
			}
		},
		readOnly : true,
		allowBlank : true
	})
//	var costCode = new Ext.form.TextField({
//		fieldLabel : "成本中心",
//		name : 'dept.costCode',
//		emptyText : '请选择...',
//		anchor : '90%',
//		listeners : {
//			focus : function() {
//				var args = {
//					selectModel : 'single',
//					rootNode : {
//						id : "0",
//						text : '灞桥电厂'
//					}
//				}
//				var url = "../../../../comm/jsp/hr/cost/costCode.jsp";
//				var cost = window
//						.showModalDialog(
//								url,
//								args,
//								'dialogWidth:500px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;')
//				if (typeof(cost) != "undefined") {
//					costCode.setValue(cost.depCode);
//
//				}
//				this.blur();
//			}
//		},
//		readOnly : true
//	})

	var ryRadio = new Ext.form.Radio({
				id : 'ay',
				boxLabel : '是',
				name : 'rs',
				inputValue : 'Y',
				checked : true
			});

	var rnRadio = new Ext.form.Radio({
				id : 'an',
				boxLabel : '否',
				name : 'rs',
				inputValue : 'N'
			});

	var ifDuty = {
		id : 'ifDuty',
		layout : 'column',
		isFormField : true,
		fieldLabel : '是否责任部门',
		style : 'cursor:hand',
		name : 'dept.ifDuty',
		anchor : '95%',
		border : false,
		items : [{
					columnWidth : .4,
					border : false,
					items : [ryRadio]
				}, {
					columnWidth : .4,
					border : false,
					items : [rnRadio]
				}]
	};

	var manager = new Ext.form.ComboBox({
				id : 'manager',
				name : 'manager',
				fieldLabel : "部门负责人",
				mode : 'local',
				readOnly : true,
				typeAhead : true,
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				anchor : '90%',
				onTriggerClick : function() {
					selectPersonWin();
				}
			});
	var managerCode = new Ext.form.Hidden({
				name : "dept.manager"
			});

	var myaddpanel = new Ext.FormPanel({
				frame : true,
				labelAlign : 'center',
				labelWidth : 80,
				closeAction : 'hide',
				title : '增加/修改预算部门信息',
				items : [centerId, depCode, depName, ifDuty, manager,
						managerCode]
			});

	function checkInput() {
		var msg = "";
		if (depName.getValue() == "") {
			msg = "'部门名称'";
		}
		if (msg != "") {
			Ext.Msg.alert("提示", "请输入" + msg);
			return false
		} else {
			return true;
		}
	}

	var win = new Ext.Window({
				width : 400,
				height : 250,
				buttonAlign : "center",
				items : [myaddpanel],
				layout : 'fit',
				closeAction : 'hide',
				draggable : true,
				modal : true,
				buttons : [{
					text : '保存',
					iconCls : 'save',
					handler : function() {
						var url = '';
						var isItem = Ext.get("ay").dom.checked;
						var idItemValue = (isItem ? "Y" : "N");
						if (Ext.get("centerId").dom.value == ""
								|| Ext.get("centerId").dom.value == null) {
							url = 'managebudget/addBudgetDept.action';
						} else {
							url = 'managebudget/updateBudgetDept.action';
						}
						if (!checkInput())
							return;
						myaddpanel.getForm().submit({
							url : url,
							method : 'post',
							params : {
								'dept.ifDuty' : idItemValue
							},
							success : function(form, action) {
								var o = eval("(" + action.response.responseText
										+ ")");

								Ext.Msg.alert("注意", o.msg);
								store.reload();
								win.hide();
							},
							failure : function(form, action) {
								Ext.Msg.alert('错误', '出现未知错误.');
							}
						})
					}

				}, {
					text : '取消',
					iconCls : 'cancer',
					handler : function() {
						myaddpanel.getForm().reset();
						win.hide();
					}
				}]
			});

	// 查询
	function queryRecord() {
		// modified by liuyi 20100601
		store.baseParams = {
			fuzzytext : fuzzy.getValue()
		}
		store.load({
					params : {
						start : 0,
						limit : 18//,
//						fuzzytext : fuzzy.getValue()
					}
				});
	}

	function addRecord() {
		myaddpanel.getForm().reset();
		win.show();
		myaddpanel.setTitle("增加预算部门信息");
	}

	function updateRecord() {
		var records = grid.getSelectionModel().getSelections();
		var recordslen = records.length;
		if (recordslen > 1) {
			Ext.Msg.alert("提示信息", "请选择其中一项进行编辑！");
		} else {
			var rec = grid.getSelectionModel().getSelected();
			if (!rec) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
				return false;
			} else {
				myaddpanel.getForm().reset();
				myaddpanel.setTitle("修改预算部门信息");
				win.show();
				Ext.get("centerId").dom.value = rec.get('centerId');
				Ext.get('depCode').dom.value = rec.get('depCode') == null
						? ""
						: rec.get('depCode');
				Ext.get('dept.depName').dom.value = rec.get('depName') == null
						? ""
						: rec.get('depName');
				if (rec.get('ifDuty') == "Y") {
					ryRadio.setValue(true);
				} else {
					rnRadio.setValue(true);
				}

				Ext.getCmp('manager').setValue(rec.get("manager"));
				Ext.form.ComboBox.superclass.setValue.call(Ext
								.getCmp('manager'), rec.get("manageName"));
								managerCode.setValue(rec.get("manager"));
//				Ext.get('dept.costCode').dom.value = rec.get('costCode') == null
//						? ""
//						: rec.get('costCode');
			}
		}
	}

	function deleteRecord() {
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.centerId) {
					ids.push(member.centerId);
				} else {
					store.remove(store.getAt(i));
				}
			}
			Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(buttonobj) {
						if (buttonobj == "yes") {
							Ext.lib.Ajax.request('POST',
									'managebudget/deleteBudgetDept.action', {
										success : function(action) {
											Ext.Msg.alert("提示", "删除成功！")
											store.load();
										},
										failure : function() {
											Ext.Msg.alert('错误', '删除时出现未知错误.');
										}
									}, 'ids=' + ids);
						}
					});
		}
	}
	// modified by liuyi 20100601
//	store.reload();
	store.load({
					params : {
						start : 0,
						limit : 18
					}
				});

	// 选择人员窗口
	function selectPersonWin() {
		var args = {
			selectModel : 'signal',
			notIn : "",
			rootNode : {
				id : '-1',
				text : '灞桥电厂'
			}
		}
		var person = window
				.showModalDialog(
						'../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(person) != "undefined") {
			manager.setValue(person.workerName);
			managerCode.setValue(person.workerCode);

		}
	}
});