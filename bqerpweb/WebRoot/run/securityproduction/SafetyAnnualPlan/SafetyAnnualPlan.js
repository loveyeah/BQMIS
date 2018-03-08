Ext.onReady(function() {
	// 系统当前时间
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		return s;
	}
	function getMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		return s;
	}
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号
							fillName.setValue(result.workerName);
							hiddcommonBy.setValue(result.workerCode);
						}
					}
				});
	}
	var annualplanId;
	var Record = Ext.data.Record.create([{
				name : 'safetyAnnualplan.annualplanId'
			}, {
				name : 'safetyAnnualplan.planYear'
			}, {
				name : 'safetyAnnualplan.depCode'
			}, {
				name : 'depName'
			}, {
				name : 'safetyAnnualplan.planContent'
			}, {
				name : 'safetyAnnualplan.memo'
			}, {
				name : 'safetyAnnualplan.fillBy'
			}, {
				name : 'fillName'
			}, {
				name : 'fillTime'
			}]);
	var sm = new Ext.grid.RowSelectionModel({
				singleSelect : false
			});
	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
				header : "年度",
				dataIndex : "safetyAnnualplan.planYear",
				sortable : true,
				width : 60
			}, {
				header : "部门",
				dataIndex : "depName",
				width : 120
			}, {
				header : "",
				dataIndex : "safetyAnnualplan.depCode",
				width : 100,
				hidden : true
			}]);
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'security/getsafetyAnnualPlan.action'
						}),
				reader : new Ext.data.JsonReader({
						// totalProperty : "totalProperty",
						// root : "root"
						}, Record)
			});
	//
	// var fuzzy = new Ext.form.TextField({
	// style : 'cursor:pointer',
	// id : "fuzzy",
	// name : 'fuzzy',
	// readOnly : false,
	// emptyText : '年度',
	// anchor : "80%"
	// value : getMonth()
	// listeners : {
	// focus : function() {
	// WdatePicker({
	// startDate : '%y-%M',
	// alwaysUseStartDate : false,
	// dateFmt : 'yyyy-MM',
	// onpicked : function() {
	// },
	// onclearing : function() {
	// planStartDate.markInvalid();
	// }
	// });
	// }
	// }
	// });
	var yeardata = '';
	var myDate = new Date();
	var myMonth = myDate.getMonth() + 1;

	myMonth = (myMonth < 10 ? "0" + myMonth : myMonth);

	for (var i = 2004; i < myDate.getFullYear() + 7; i++) {
		if (i < myDate.getFullYear() + 6)
			yeardata += '[' + i + ',' + i + '],';
		else
			yeardata += '[' + i + ',' + i + ']';
	}
	var yeardata = eval('[' + yeardata + ']');

	var fuzzy = new Ext.form.ComboBox({
				xtype : "combo",
				store : new Ext.data.SimpleStore({
							fields : ['value', 'key'],
							data : yeardata
						}),
				id : 'itemType',
				name : 'itemType',
				valueField : "key",
				displayField : "value",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'statItem.itemType',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				width : 80,
				value : myDate.getFullYear()
			});
	ds.load({
				params : {
					fuzzy : fuzzy.getValue()
				}
			});

	var stocktbar = new Ext.Toolbar({
				// height : 25,
				items : ["年度:", fuzzy, {
							text : "查询",
							iconCls : 'query',
							handler : function() {
								ds.load({
											params : {
												fuzzy : fuzzy.getValue()
											}
										});
							}
						}]
			});

	var rightGrid = new Ext.grid.GridPanel({
				layout : 'fit',
				region : 'west',
				region : "center",
				anchor : "100%",
				autoScroll : true,
				style : "border-top:solid 1px",
				border : false,
				enableColumnMove : false,
				sm : sm,
				ds : ds,
				cm : cm,
				tbar : stocktbar
			});
	rightGrid.on('rowclick', dblclick);
	function dblclick() {
		var rec = rightGrid.getSelectionModel().getSelected();
		annualplanId = rec.get("safetyAnnualplan.annualplanId");
		department.setValue(rec.get("depName"));
		depCode.setValue(rec.get('safetyAnnualplan.depCode'));
		year.setValue(rec.get("safetyAnnualplan.planYear"));
		fillName.setValue(rec.get("fillName"));
		fillTime.setValue(rec.get("fillTime"));
		content.setValue(rec.get("safetyAnnualplan.planContent"));
		memo.setValue(rec.get('safetyAnnualplan.memo'));
	}
	// 部门
	function selectDept() {
		var args = {
			selectModel : 'single',
			rootNode : {
				id : '0',
				text : '合肥电厂'
			},
			onlyLeaf : false
		};
		var dept = window
				.showModalDialog(
						'../../../comm/jsp/hr/dept/dept.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(dept) != "undefined") {
			depCode.setValue(dept.codes);
			department.setValue(dept.names);
		}
	}

	var department = new Ext.form.ComboBox({
				id : 'depName',
				name : 'depName',
				fieldLabel : "部门",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				anchor : '95%',
				onTriggerClick : function() {
					selectDept();
				}
			});

	var depCode = new Ext.form.Hidden({
				id : 'safetyAnnualPlan.depCode',
				name : 'safetyAnnualPlan.depCode'
			});
	// var month = new Ext.form.TextField({
	// style : 'cursor:pointer',
	// id : "month",
	// name : 'month',
	// readOnly : true,
	// anchor : "70%",
	// fieldLabel : '部门',
	// value : getMonth()
	// listeners : {
	// focus : function() {
	// WdatePicker({
	// startDate : '%y-%M',
	// alwaysUseStartDate : false,
	// dateFmt : 'yyyy-MM',
	// onpicked : function() {
	// },
	// onclearing : function() {
	// planStartDate.markInvalid();
	// }
	// });
	// }
	// }
	// });
	// 年度
	var year = new Ext.form.ComboBox({
				fieldLabel : '年度',
				xtype : "combo",
				store : new Ext.data.SimpleStore({
							fields : ['value', 'key'],
							data : yeardata
						}),
				id : 'year',
				name : 'safetyAnnualPlan.planYear',
				valueField : "key",
				displayField : "value",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'statItem.itemType',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				readOnly : false,
				anchor : "80%"

			})
	// 填写人
	var fillName = new Ext.form.TextField({
				id : "fillName",
				fieldLabel : '填写人',
				// name : 'briefnews.commonBy',
				readOnly : true,
				anchor : "70%"
			})
	var hiddcommonBy = new Ext.form.Hidden({
				id : 'hiddcommonBy',
				name : 'safetyAnnualPlan.fillBy'
			})
	// 填写日期
	var fillTime = new Ext.form.TextField({
				id : "fillTime",
				name : 'safetyAnnualPlan.fillTime',
				readOnly : true,
				anchor : "80%",
				fieldLabel : '填写日期',
				value : getDate()
			})
	// 计划内容
	var content = new Ext.form.TextArea({
				id : "content",
				fieldLabel : '内容',
				name : 'safetyAnnualPlan.planContent',
				// readOnly : true,
				height : 100,
				anchor : "90%"
			})
	// 计划备注
	var memo = new Ext.form.TextArea({
				id : "memo",
				fieldLabel : '备注',
				name : 'safetyAnnualPlan.memo',
				// readOnly : true,
				anchor : "90%"
			})
	// 计划信息
	var safetyannualplan = new Ext.form.FieldSet({
				border : false,
				height : '100%',
				collapsible : false,
				layout : 'form',
				items : [{
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 0.5,
										border : false,
										labelWidth : 50,
										layout : 'form',
										items : [department, depCode]
									}, {
										columnWidth : 0.5,
										border : false,
										labelWidth : 55,
										layout : 'form',
										items : [year]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 1,
										border : false,
										labelWidth : 50,
										layout : "form",
										items : [content]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 1,
										border : false,
										labelWidth : 50,
										layout : "form",
										items : [memo]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 0.5,
										border : false,
										labelWidth : 50,
										layout : 'form',
										items : [fillName, hiddcommonBy]
									}, {
										columnWidth : 0.5,
										border : false,
										labelWidth : 55,
										layout : 'form',
										items : [fillTime]
									}]
						}]
			})

	var myPanel = new Ext.FormPanel({
				bodyStyle : "padding:10px 0px 0px 0px",
				region : "center",
				layout : 'form',
				autoheight : true,
				autoScroll : true,
				labelAlign : 'right',
				containerScroll : true,
				tbar : [{
							id : 'btnFirst',
							text : '增加',
							iconCls : 'add',
							handler : Add
						}, '-', {
							id : 'delete',
							text : '删除',
							iconCls : 'delete',
							handler : DeLnews

						}, '-', {
							id : 'btnAdd',
							text : '保存',
							iconCls : 'add',
							handler : Addnews
						}],
				items : [{
							layout : 'column',
							border : false,
							items : [{
										border : false,
										columnWidth : 1,
										align : 'center',
										anchor : '100%',
										layout : 'form',
										items : [safetyannualplan]
									}]
						}]
			});

	function Add() {
		myPanel.getForm().reset();
		annualplanId = "";
		getWorkCode();
	}

	function DeLnews() {
		if (annualplanId == null || annualplanId == "") {
			Ext.Msg.alert("提示", "请在左边列表中选择一条记录!");
			return false;
		} else {
//			Ext.Msg.confirm("删除", "是否确定删除安全号为'" + annualplanId + "'的记录？",
			Ext.Msg.confirm("删除", "是否确定删除该记录吗？",
					function(buttonobj) {
						if (buttonobj == "yes") {
							Ext.lib.Ajax.request('POST',
									'security/deleteSafeAnnualPlan.action', {
										success : function(action) {
											Ext.Msg.alert("提示", "删除成功！")
											myPanel.getForm().reset();
											annualplanId = "";
											getWorkCode();
											ds.reload();

										},
										failure : function() {
											Ext.Msg.alert('错误', '删除时出现未知错误.');
										}
									}, 'annualplanId=' + annualplanId);
						}
					});
		}
	}

	function Addnews() {
		var url = "";
		if (annualplanId == null || annualplanId == "") {
			url = "security/addSafetyAnnualPlan.action";
		} else {
			url = "security/updateSafetyAnnualPlan.action";
		}
		if (myPanel.getForm().isValid()) {
			myPanel.getForm().submit({
						method : 'POST',
						url : url,
						params : {
							year : year.getValue(),
							annualplanId : annualplanId
						},
						success : function(form, action) {
							var o = eval("(" + action.response.responseText
									+ ")")
							annualplanId = o.annualplanId;
							Ext.Msg.alert("注意", o.msg);
							fuzzy.setValue(year.getValue());
							ds.load({
										params : {
											fuzzy : fuzzy.getValue()
										}
									});

						},
						faliue : function() {
							Ext.Msg.alert('错误', '出现未知错误.');
						}
					});

		}
	}
	// 左边的panel
	var leftPanel = new Ext.Panel({
				title : '计划列表',
				region : 'west',
				layout : 'fit',
				width : 270,
				autoScroll : true,
				border : false,
				containerScroll : true,
				collapsible : true,
				split : false,
				items : [rightGrid]
			});

	var rightPanel = new Ext.Panel({
				title : '计划信息',
				region : "center",
				layout : 'border',
				border : true,
				items : [myPanel]
			});
	var view = new Ext.Viewport({
				enableTabScroll : true,
				autoScroll : true,
				layout : "border",
				items : [leftPanel, rightPanel]
			});
	getWorkCode();

	year.setValue(myDate.getFullYear());
})
