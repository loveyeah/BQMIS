Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

function getDateDay() {
	var d, s, t;
	d = new Date();
	s = d.getFullYear().toString(10) + "-";
	t = d.getMonth() + 1;
	s += (t > 9 ? "" : "0") + t + "-";
	t = d.getDate();
	s += (t > 9 ? "" : "0") + t + " ";
	t = d.getHours();
	s += (t > 9 ? "" : "0") + t + ":";
	t = d.getMinutes();
	s += (t > 9 ? "" : "0") + t + ":";
	t = d.getSeconds();
	s += (t > 9 ? "" : "0") + t;
	return s;
}

// 系统当前时间
function getMonth() {
	var d, s, t;
	d = new Date();
	s = d.getFullYear().toString(10) + "-";
	t = d.getMonth() + 1;
	s += (t > 9 ? "" : "0") + t;
	return s;
}
function getDate() {
	var d, s, t;
	d = new Date();
	s = d.getFullYear().toString(10)
	return s;
}
Ext.onReady(function() {
	var workFlowNo;

	var changeStatus;

	// 从session取登录人编码姓名部门相关信息
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号，赋给全局变量
							Ext.get('changeBy').dom.value = result.workerCode;
							Ext.get('changeName').dom.value = result.workerName;
						}
					}
				});
	}

	var formatType;
	var yearRadio = new Ext.form.Radio({
				id : 'year',
				name : 'queryWayRadio',
				hideLabel : true,
				boxLabel : '年份',
				checked : true,
				listeners : {
					check : function() {
						var queryType = getChooseQueryType();
						switch (queryType) {
							case 'year' : {
								formatType = 1;
								time.setValue(getDate());
								break;
							}
							case 'month' : {
								time.setValue(getMonth());
								formatType = 2;
								break;
							}
						}
					}
				}
			});
	var monthRadio = new Ext.form.Radio({
				id : 'month',
				name : 'queryWayRadio',
				hideLabel : true,
				boxLabel : '月份'
			});
	var time = new Ext.form.TextField({
				id : 'time',
				allowBlank : true,
				width : 100,
				listeners : {
					focus : function() {
						var format = '';
						if (formatType == 1)
							format = 'yyyy';
						if (formatType == 2)
							format = 'yyyy-MM';
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : format,
									alwaysUseStartDate : true,
									onclearing : function() {
										planStartDate.markInvalid();
									}
								});
					}
				}
			});
	// 查询按钮
	var btnquery = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : function query() {
					queryRecord();
				}
			});

	function queryRecord() {
		var msg = "";
		if (topicComboBox.getValue() == "")
			msg = "'预算主题'";
		if (time.getValue() == "")
			msg += "'时间'";
		if (msg != "") {
			Ext.Msg.alert("提示", "请选择" + msg + "!");
			return;
		}
			
		westgrids.baseParams = {
			topicId : topicComboBox.getValue(),
			budgetTime : time.getValue(),
			formatType : formatType,
		    budgetDept:hideDept.getValue(),
		    itemName:txtItemName.getValue()
		};
		westgrids.load({
					params : {
						start : 0,
						limit : 18
					}
				});
		westgrids.rejectChanges();
	}

	// 定义主题
	var topicData = new Ext.data.JsonStore({
				root : 'list',
				url : "managebudget/getThemeList.action",
				fields : ['topicId', 'topicName']
			});
	topicData.on('load', function(e, records) {
				var record1 = new Ext.data.Record({
							// topicName : '所有',
							topicName : '',
							topicId : ''
						});
				topicData.insert(0, record1);
				topicComboBox.setValue('');
			});
	topicData.load();

	var topicComboBox = new Ext.form.ComboBox({
				id : "topicComboBox",
				store : topicData,
				displayField : "topicName",
				valueField : "topicId",
				mode : 'local',
				width : 100,
				triggerAction : 'all',
				readOnly : true
			});

	var westsm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});

	// 左边列表中的数据
	var westdatalist = new Ext.data.Record.create([{
				name : 'budgetItemId'
			}, {
				name : 'itemId'
			}, {
				name : 'itemAlias'
			}, {
				name : 'unitName'
			}, {
				name : 'adviceBudget'
			}, {
				name : 'ensureBudget'
			}, {
				name : 'topicId'
			}, {
				name : 'budgetTime'
			}, {
				name : 'changeId'
			}, {
				name : 'changeWorkFlowNo'
			}, {
				name : 'changeStatus'
			}, {
				name : 'changeDate'
			},{name:'itemCode'}]);

	var westgrids = new Ext.data.JsonStore({
				url : 'managebudget/findAllModifyItem.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : westdatalist
			});
	// westgrids.load();
			
//-------add by fyyang 20100609-----预算部门---------------------
			
	var hideDept = new Ext.form.Hidden({
				id : 'deptH',
				name : 'deptH'
			})
	var txtBudgetDept = new Ext.form.TextField({
		id : 'budgetDept',
		fieldLabel : '部门',
		readOnly : true,
		width : 160,
		listeners : {
			focus : function() {
				var args = {
					selectModel : 'single',
					rootNode : {
						id : "0",
						text : '合肥电厂'
					}
				}
				var url = "../../maint/budegtDeptSelect.jsp";
				var dept = window
						.showModalDialog(
								url,
								args,
								'dialogWidth:500px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;')
				if (typeof(dept) != "undefined") {
					hideDept.setValue(dept.depCode);
					txtBudgetDept.setValue(dept.depName);
				}
				this.blur();
			}
		}
	});
	
	var txtItemName=new Ext.form.TextField({
	fieldLabel : '指标名称',
	name:'qtyItemName',
	width : 100
	
	});
//-------------------------------------------------------			
	
	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
				// width : 200,
		       // renderTo:'mygrid',
				split : true,
				autoScroll : true,
				ds : westgrids,
//				tbar : [monthRadio, yearRadio, time, {
//							xtype : "tbseparator"
//						}, '预算部门:',txtBudgetDept,hideDept],
				columns : [westsm, new Ext.grid.RowNumberer({
									header : "行号",
									width : 31
								}), {
							header : "指标ID",
							width : 110,
							hidden : true,
							align : "center",
							sortable : true,
							dataIndex : 'itemId'
						}, {
							header : "指标名称",
							width : 100,
							align : "left",
							sortable : true,
							dataIndex : 'itemAlias',
								renderer : function(value, cellmeta, record, rowIndex, columnIndex, store){
						var level=0;
						if(record.get("itemCode")!=null||record.get("itemCode")!="")
						{
							level= (record.get("itemCode").length/3)-2;
						}
						 if(level>0)
						 {
						 var levelNo="";
						 for(var i=0;i<level;i++)
						{
							levelNo=" "+levelNo;
						}
						
						value=levelNo+value;
						 }
					return "<pre>"+value+"</pre>";
				}
						}, {
							header : "计量单位",
							width : 100,
							align : "center",
							sortable : true,
							dataIndex : 'unitName'
						}, {
							header : "建议预算",
							width : 80,
							align : "left",
							sortable : true,
							dataIndex : 'adviceBudget'
						}, {
							header : "审定预算",
							width : 80,
							align : "left",
							sortable : true,
							dataIndex : 'ensureBudget'
						}, {
							header : "changeId",
							width : 70,
							align : "center",
							sortable : true,
							hidden : true,
							dataIndex : 'changeId'
						}, {
							header : "changeWorkFlowNo",
							width : 70,
							align : "center",
							sortable : true,
							hidden : true,
							dataIndex : 'changeWorkFlowNo'
						}, {
							header : "changeStatus",
							width : 70,
							align : "center",
							sortable : true,
							hidden : true,
							dataIndex : 'changeStatus'
						}],
				sm : westsm,
				// frame : true,
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : westgrids,
							displayInfo : true,
							displayMsg : "共 {2} 条",
							emptyMsg : "没有记录"
						}),
				border : true
			});
			

	westsm.on('rowselect', function() {
		if (westsm.hasSelection()) {
			Ext.Ajax.request({
				url : 'managebudget/getBudgetModifyById.action',
				method : 'post',
				params : {
					budgetItemId : westsm.getSelected().get('budgetItemId'),
					changeId : westsm.getSelected().get('changeId')
				},
				success : function(action) {
					var result = eval("(" + action.responseText + ")");
//					 alert(action.responseText);
					if (result.centerName != null) {
						Ext.get('centerName').dom.value = result.centerName;
					}
					if (result.topicName != null) {
						Ext.get('topicName').dom.value = result.topicName;
					}
					if (result.budgetTime != null) {
						Ext.get('budgetTime').dom.value = result.budgetTime;
					}
					if (result.itemName != null) {
						Ext.get('itemName').dom.value = result.itemName;
					}
					if (westsm.getSelected().get('changeId') == null) {
						if (result.ensureBudget != null) {
							Ext.get('originBudget').dom.value = result.ensureBudget;
						} else {
							Ext.get('originBudget').dom.value = "";
						}
					} else {
						if (result.originBudget != null) {
							Ext.get('originBudget').dom.value = result.originBudget;
						} else {
							Ext.get('originBudget').dom.value = "";
						}
					}
					if (result.budgetChange != null) {
						Ext.get('budgetChange').dom.value = result.budgetChange;
					} else {
						Ext.get('budgetChange').dom.value = "";
					}
					// add by liuyi 20100601 状态为变更确认的时，预算变更的值设置为null
					 if(result.changeStatus == '2'){
					 	budgetChange.setValue(null);
					 }
					if (result.newBudget != null) {
						Ext.get('newBudget').dom.value = result.newBudget;
					} else {
						Ext.get('newBudget').dom.value = "";
					}
					if (result.changeReason != null) {
						Ext.get('changeReason').dom.value = result.changeReason;
					} else {
						Ext.get('changeReason').dom.value = "";
					}
					if (result.btnChange == Ext.get('budgetChange').dom.value) {
						ryRadio.setValue(true);
					} else {
						rnRadio.setValue(true);
					}
					if (result.changeDate != null) {
						Ext.get('changeDate').dom.value = result.changeDate;
					}
				}

			});
			check();
		}
	});

	var centerName = new Ext.form.TextField({
				id : 'centerName',
				fieldLabel : '预算部门',
				readOnly : true,
				anchor : "90%",
				name : 'centerName'
			});

	var topicName = new Ext.form.TextField({
				id : 'topicName',
				fieldLabel : '预算主题',
				readOnly : true,
				anchor : "90%",
				name : 'topicName'
			});

	var budgetTime = new Ext.form.TextField({
				id : 'budgetTime',
				fieldLabel : '预算时间',
				readOnly : true,
				anchor : "90%",
				name : 'change.budgetTime'
			});

	var itemName = new Ext.form.TextField({
				id : 'itemName',
				fieldLabel : '预算指标',
				readOnly : true,
				anchor : "90%",
				name : 'itemName'
			});

	var originBudget = new Ext.form.NumberField({
				id : 'originBudget',
				fieldLabel : '原预算值',
				readOnly : true,
				anchor : "90%",
				name : 'change.originBudget'
			});

	var newBudget = new Ext.form.NumberField({
				id : 'newBudget',
				fieldLabel : '现预算值',
				readOnly : true,
				anchor : "90%",
				name : 'change.newBudget'
			});

	var ryRadio = new Ext.form.Radio({
				id : 'ay',
				boxLabel : '追加',
				name : 'rs',
				inputValue : 'Y',
				checked : true
			});

	var rnRadio = new Ext.form.Radio({
				id : 'an',
				boxLabel : '减少',
				name : 'rs',
				inputValue : 'N'
			});

	var ifBudget = {
		id : 'ifBudget',
		layout : 'column',
		isFormField : true,
		style : 'cursor:hand',
		fieldLabel : '变更方式',
		anchor : '90%',
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

	var budgetChange = new Ext.form.NumberField({
				id : 'budgetChange',
				fieldLabel : '预算变更',
				anchor : "90%",
				name : 'change.budgetChange'
			});

	var changeReason = new Ext.form.TextArea({
				id : "changeReason",
				height : 50,
				fieldLabel : '变更事由',
				name : 'change.changeReason',
				anchor : "95%"
			});

	var changeName = new Ext.form.TextField({
				id : 'changeName',
				fieldLabel : '变更申请人',
				name : 'changeName',
				anchor : '90%',
				readOnly : true

			});
	var changeBy = new Ext.form.Hidden({
				hidden : false,
				id : "changeBy",
				name : 'change.changeBy'
			});

	var changeDate = new Ext.form.TextField({
				id : 'changeDate',
				fieldLabel : '变更时间',
				name : 'change.changeDate',
				readOnly : true,
				// value : getDateDay(),
				anchor : "90%"
			});
			
			
var headBar=new Ext.Toolbar({
	
	items:[monthRadio, yearRadio, time, {
							xtype : "tbseparator"
						}, '预算部门:',txtBudgetDept,hideDept,'-','预算主题：', topicComboBox,'-','指标名称:',txtItemName, {
							xtype : "tbseparator"
						}, btnquery]
	});
	var formtbar = new Ext.Toolbar({
				items : [{
							id : 'btnAdd',
							text : "变更",
							iconCls : 'add',
							hidden : true,
							handler : addRecord
						}, '-', {
							id : 'btnSave',
							text : "保存",
							iconCls : 'save',
							handler : saveRecord
						}, '-', {
							id : 'btnDelete',
							text : "删除",
							iconCls : 'delete',
							handler : deleteRecord
						}, '-', {
							id : 'btnReport',
							text : "变更确认",
							iconCls : 'upcommit',
							handler : isReportRecord
						}, '-', {
							id : 'btnModifyInfo',
							text : "变更",
							iconCls : 'list',
							handler : showModifyInfo
						}
//						, '-', {
//							id : 'btnQuery',
//							text : "审批查询",
//							iconCls : 'view',
//							handler : approveQuery
//						}
						]
			});

	var eastField = new Ext.Panel({
				border : true,
				labelWidth : 70,
				region : 'north',
				labelAlign : 'right',
				autoHeight : true,
				style : {
					"margin-top" : "20px",
					"margin-left" : "10px",
					"margin-right" : Ext.isIE6 ? (Ext.isStrict
							? "-10px"
							: "-13px") : "0"
				},
				items : [{
							layout : 'column',
							items : [{
										columnWidth : 0.5,
										layout : 'form',
										items : [centerName]
									}, {
										columnWidth : 0.5,
										layout : 'form',
										items : [topicName]
									}]
						}, {
							layout : 'column',
							items : [{
										columnWidth : 0.5,
										layout : 'form',
										items : [budgetTime]
									}, {
										columnWidth : 0.5,
										layout : 'form',
										items : [itemName]
									}]
						}, {
							layout : 'column',
							items : [{
										columnWidth : 0.5,
										layout : 'form',
										items : [originBudget]
									}, {
										columnWidth : 0.5,
										layout : 'form',
										items : [newBudget]
									}]
						}, {
							layout : 'column',
							items : [{
										columnWidth : 0.5,
										border : false,
										layout : "form",
										items : [ifBudget]
									}, {
										columnWidth : 0.5,
										border : false,
										layout : "form",
										items : [budgetChange]
									}]
						}, {
							layout : 'column',
							items : [{
										columnWidth : 1,
										layout : 'form',
										items : [changeReason]
									}]
						}, {
							layout : 'column',
							items : [{
										columnWidth : 0.5,
										layout : 'form',
										items : [changeName, changeBy]
									}, {
										columnWidth : 0.5,
										layout : 'form',
										items : [changeDate]
									}]
						}]
			});

	var form = new Ext.form.FormPanel({
				border : false,
				frame : true,
				layout : 'form',
				items : [eastField],
				bodyStyle : {
					'padding-top' : '5px'
				},
				defaults : {
					labelAlign : 'right'
				}
			});

	var eastForm = new Ext.form.FieldSet({
				border : true,
				width : 500,
				labelWidth : 80,
				labelAlign : 'right',
				layout : 'form',
				title : '变更信息',
				autoHeight : true,
				items : [form]
			});

	function addRecord() {
		if (!westsm.hasSelection()) {
			Ext.Msg.alert('提示信息', '请先选择左边的一条记录！');
			return;
		}
		form.getForm().reset();
		Ext.Ajax.request({
					url : 'managebudget/getBudgetModifyById.action',
					method : 'post',
					params : {
						budgetItemId : westsm.getSelected().get('budgetItemId'),
						changeId : westsm.getSelected().get('changeId')
					},
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.centerName != null) {
							Ext.get('centerName').dom.value = result.centerName;
						}
						if (result.topicName != null) {
							Ext.get('topicName').dom.value = result.topicName;
						}
						if (result.budgetTime != null) {
							Ext.get('budgetTime').dom.value = result.budgetTime;
						}
						if (result.itemName != null) {
							Ext.get('itemName').dom.value = result.itemName;
						}
						if (result.originBudget != null) {
							Ext.get('originBudget').dom.value = result.ensureBudget;
						}
						if (result.budgetChange != null) {
							Ext.get('budgetChange').dom.value = "";
						}
						if (result.changeStatus == '2') {
							if (result.newBudget != null) {
								Ext.get('newBudget').dom.value = "";
							}
						} else {
							Ext.get('newBudget').dom.value = result.newBudget;
						}
						if (result.changeReason != null) {
							Ext.get('changeReason').dom.value = "";
						}
					}
				});
		Ext.get('btnSave').dom.disabled = false;
		getWorkCode();
		Ext.get('changeDate').dom.value = getDateDay();
	}

	function deleteRecord() {
		var sm = westgrid.getSelectionModel();
		var selected = westsm.getSelections();
		var ids = [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.changeId) {
					ids.push(member.changeId);
				}
				if (member.changeId == null || member.changeId == "") {
					Ext.Msg.alert("提示", "该记录没有变更信息,不能进行删除操作！");
					return;
				}// modified by liuyi 20100601 
//				else {
//					westgrids.remove(westgrids.getAt(i));
//				}
			}
			Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(buttonobj) {
						if (buttonobj == "yes") {
							Ext.lib.Ajax.request('POST',
									'managebudget/deleteBudgetModify.action', {
										success : function(action) {
											Ext.Msg.alert("提示", "删除成功！")
											westgrids.load();
											westgrids.rejectChanges();
										},
										failure : function() {
											Ext.Msg.alert('错误', '删除时出现未知错误.');
										}
									}, 'ids=' + ids);
						}
					});
		}
	}

	function checkInput() {
		var msg = "";
		if (budgetChange.getValue() == "" || budgetChange.getValue() == null) {
			msg = "'预算变更'";
		}
		if (msg != "") {
			Ext.Msg.alert("提示", "请输入" + msg);
			return false
		} else {
			return true;
		}
	}

	function saveRecord() {
		var isItem = Ext.get("ay").dom.checked;
		var idItemValue = (isItem ? "Y" : "N");
		if (!westsm.hasSelection()) {
			Ext.Msg.alert('提示信息', '请先选择左边的一条记录！');
			return;
		}
		if (!checkInput())
			return;
		Ext.Msg.confirm("提示", "是否确定变更？", function(buttonobj) {
			if (buttonobj == "yes") {
				form.getForm().submit({
					url : 'managebudget/saveBudgetModify.action',
					method : 'POST',
					params : {
						budgetItemId : westsm.getSelected().get('budgetItemId'),
						'ifBudget' : idItemValue,
						changeStatus : westsm.getSelected().get('changeStatus'),
						changeIda : westsm.getSelected().get('changeId')
					},
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("提示", o.msg);
						westgrids.reload();
						westgrids.rejectChanges();
					},
					failure : function() {
						Ext.Msg.alert('提示', '操作失败，请联系管理员！');
					}
				})
			}
		});
	}

	function showModifyInfo() {
		var selrows = westgrid.getSelectionModel().getSelections();
		if (selrows.length > 0) {
			var record = westgrid.getSelectionModel().getSelected();
			var url;
			var budgetItemId = record.data.budgetItemId;
			var changeId = record.data.changeId;
			if (changeId == null || changeId == "") {
				Ext.Msg.alert('提示', '所选记录没有变更信息!');
				return;
			} else
			// if ((changeId != null) && (changeId != "")) {
			// url =
			// "../budgetModify/budgetModifySelect/budgetModifySelect.jsp?budgetItemId="
			// + budgetItemId + "&changeId=" + changeId;
			// }
			// update by ltong
			{
				url = "../budgetModify/budgetModifySelect/budgetModifySelectGrid.jsp?budgetItemId="
						+ budgetItemId + "&changeId=" + changeId;
			}
			var o = window.showModalDialog(url, '',
					'dialogWidth=900px;dialogHeight=500px;status=no');
		} else {
			Ext.Msg.alert('提示', '请先选择左边的一条记录!');
		}
	}

	function isReportRecord() {
		var isItem = Ext.get("ay").dom.checked;
		var idItemValue = (isItem ? "Y" : "N");
		var sm = westgrid.getSelectionModel();
		var selected = westsm.getSelections();
		var ids = [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请先选择左边的一条记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.changeId) {
					ids.push(member.changeId);
				}
				if (member.changeId == null || member.changeId == "") {
					Ext.Msg.alert("提示", "该记录没有变更信息,不能进行确认操作！");
					return;
				}// modified by liuyi 20100601
//				else {
//					westgrids.remove(westgrids.getAt(i));
//				}
			}
			Ext.Msg.confirm("提示", "是否确定变更所选的记录？", function(buttonobj) {
						if (buttonobj == "yes") {
							Ext.lib.Ajax.request('POST',
									'managebudget/affModify.action', {
										success : function(action) {
											Ext.Msg.alert("提示", "变更确认成功！")
											westgrids.reload();
											westgrids.rejectChanges();
										},
										failure : function() {
											Ext.Msg.alert('错误', '变更确认时出现未知错误.');
										}
									}, 'ids=' + ids + '&budgetItemId='+ westsm.getSelected().get('budgetItemId')
									+ '&ifBudget=' + idItemValue+ '&budgetChange=' + budgetChange.getValue());
						}
					});
		}
	}

	function reportRecord() {
		var sm = westgrid.getSelectionModel();
		var selected = westsm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请先选择左边的一条记录！");
			return;
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.changeId == null || member.changeId == "") {
					Ext.Msg.alert("提示", "该记录没有变更信息,不能进行上报操作！");
					return;
				}
			}
		}
		var record = westgrid.getSelectionModel().getSelected();
		// var data = westgrids.getAt(0);
		if (!confirm("确定要上报吗？"))
			return;
		var url = "budgetModifyReport.jsp";
		var args = new Object();
		args.entryId = record.get("changeWorkFlowNo");
		args.workflowType = "budgetModifyApprove";
		args.changeId = record.get("changeId");
		args.prjStatus = record.get("changeStatus");
		var obj = window.showModalDialog(url, args,
				'status:no;dialogWidth=750px;dialogHeight=550px');
		if (obj) {
			queryRecord();
		}
	}

	function approveQuery() {
		if (!westsm.hasSelection()) {
			Ext.Msg.alert('提示信息', '请先选择左边的一条记录！');
			return;
		}
		var record = westgrid.getSelectionModel().getSelected();
		var entryId = record.get('changeWorkFlowNo');
		if (entryId == null || entryId == "") {
			Ext.Msg.alert('提示', '流程尚未启动！');
		} else {
			var url = "../../../../workflow/manager/show/show.jsp?entryId="
					+ entryId;
			window.open(url);
		}
	}

	function check() {
		var record = westgrid.getSelectionModel().getSelected();
		var changeStatus = record.get('changeStatus');
		if (changeStatus == null) {
			Ext.get('btnSave').dom.disabled = false;
			Ext.get('btnDelete').dom.disabled = true;
			Ext.get('btnReport').dom.disabled = true;
			Ext.get('btnAdd').dom.disabled = true;
		} else if (changeStatus == '0' || changeStatus == '3') {
			Ext.get('btnSave').dom.disabled = false;
			Ext.get('btnDelete').dom.disabled = false;
			Ext.get('btnReport').dom.disabled = false;
			Ext.get('btnAdd').dom.disabled = true;
		} else if (changeStatus == '1') {
			Ext.get('btnSave').dom.disabled = false;
			Ext.get('btnDelete').dom.disabled = false;
			Ext.get('btnReport').dom.disabled = false;
			Ext.get('btnAdd').dom.disabled = true;
		} else if (changeStatus == '2') {
			Ext.get('btnSave').dom.disabled = false;
			Ext.get('btnDelete').dom.disabled = false;
			Ext.get('btnReport').dom.disabled = false;
			Ext.get('btnAdd').dom.disabled = true;
		}
	}

	
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [{
							//layout : 'fit',
							border : false,
							split : true,
							frame : false,
							region : "north",
							height : 25,
							items : [headBar]
						},{
							// bodyStyle : "padding: 2,2,2,2",
							layout : 'fit',
							border : false,
							split : true,
							frame : false,
							region : "west",
							// width : '42%',
							width : 430,
							items : [westgrid]
						}, {
							// bodyStyle : "padding: 2,2,2,2",
							region : "center",
							border : false,
							autoScroll : true,
							frame : false,
							tbar : formtbar,
							// layout : 'fit',
							items : [eastForm]
						}]
			});

	// 遍历所有的REDIO获得ID
	function getChooseQueryType() {
		var list = document.getElementsByName("queryWayRadio");
		for (var i = 0; i < list.length; i++) {
			if (list[i].checked) {
				return list[i].id;
			}
		}
	}
	getWorkCode();
	Ext.get('changeDate').dom.value = getDateDay();
});