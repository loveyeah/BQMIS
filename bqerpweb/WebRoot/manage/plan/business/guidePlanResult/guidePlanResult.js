Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

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
	var planstatus = "";
	var rec = Ext.data.Record.create([{
				name : 'baseInfo.guideId'
			}, {
				name : 'baseInfo.guideContent'
			}, {
				name : 'baseInfo.referDepcode'
			}, {
				name : 'baseInfo.mainDepcode'
			}, {
				name : 'baseInfo.taskDepcode'
			}, {
				name : 'baseInfo.ifComplete'
			}, {
				name : 'baseInfo.completeDesc'
			}, {
				name : 'baseInfo.ifCheck'
			}, {
				name : 'baseInfo.checkStatus'
			}, {
				name : 'baseInfo.targetDepcode'
			}, {
				name : 'baseInfo.checkDesc'
			}, {
				name : 'mainDepName'
			}, {
				name : 'referDepName'
			}, {
				name : 'targetDepName'
			}, {
				name : 'ifCheckName'
			}, {
				name : 'ifCompleteName'
			}, {
				name : 'checkStatusName'
			}]);

	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manageplan/getBpJPlanGuideDetail.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'totalCount',
							root : 'list'
						}, rec)
			});


	/* 设置每一行的选择框 */
	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false,
				listeners : {
					rowselect : function(sm, row, rec) {
						// Ext.getCmp("form").getForm().loadRecord(rec);
					}
				}
			});

	var rn = new Ext.grid.RowNumberer({
	});
			
	var cm = new Ext.grid.ColumnModel([sm, rn, 
	                  {
	                  	header : '内容',
			            dataIndex : 'baseInfo.guideContent',
			            align : 'center',
			            width : 300
	                   },{
	                   	 header : '主要落实单位',
			             dataIndex : 'mainDepName',
			             width : 150,
			             align : 'center'
	                   },{
	                   	header : '任务落实单位',
			            dataIndex : 'baseInfo.taskDepcode',
			            align : 'center',
			            width : 150
	                   },{
	                   	header : '提出单位',
			            dataIndex : 'referDepName',
			            align : 'center',
			            width : 150
	                   },{
	                   	header : '完成情况',
			            dataIndex : 'baseInfo.ifComplete',
			            align : 'center',
			            width : 100,
			            renderer : function(v) {
								if (v == 0) {
									return "未完成";
								}
								if (v == 1) {
									return "进行中";
								}
								if (v == 2) {
									return "已完成";
								}
								return v
							}
	                   },{
	                   	header : '完成说明',
			            dataIndex : 'baseInfo.completeDesc',
			            align : 'center',
			            width : 150
	                   },{
	                   	header : '是否考核',
			            dataIndex : 'baseInfo.ifCheck',
			            align : 'center',
			            width : 100,
			            renderer : function(v) {
								if (v == 0) {
									return "否";
								}
								if (v == 1) {
									return "是";
								}
								return v
							}
	                   },{
	                   	header : '考核状态',
			            dataIndex : 'baseInfo.checkStatus',
			            align : 'center',
			            width :100,
			            renderer : function(v) {
								if (v == 0) {
									return "未考核";
								}
								if (v == 1) {
									return "已考核";
								}
								return v
							}
	                   },{
	                   	header : '针对单位',
			            dataIndex : 'targetDepName',
			            align : 'center',
			            width : 150
	                   },{
	                   	header : '考核说明',
			            dataIndex : 'baseInfo.checkDesc',
			            align : 'center',
			            width : 150
	                   }]);
			
		
	//计划月度
	var planTime = new Ext.form.TextField({
			readOnly : true,
			fieldLabel : '计划月份'
	})
	// 计划状态
	var status = new Ext.form.TextField({
				readOnly : true,
				fieldLabel : '计划状态'
			});

	// 发布时间
	var releaseDate = new Ext.form.TextField({
				readOnly : true,
				name : 'releaseDate',
				fieldLabel : '发布日期'
			});

	// 编辑人编码
	var planStatus = new Ext.form.Hidden({
				name : 'planStaus'
			});
			
	//var myform = new Ext.form.FormPanel({
	//					bodyStyle : "padding: 20,10,20,20",
	//					layout : 'column',
	//					items : [{
	//								columnWidth : '.33',
	//								layout : 'form',
	//								border : false,
	//								labelWidth : 60,
	//								items : [planTime]
	//							},
//
	//							{
	//								columnWidth : '.33',
	//								layout : 'form',
	//								border : false,
	//								labelWidth : 60,
	//								items : [status]
	//							},
//
	//							{
	//								columnWidth : '.33',
	//								layout : 'form',
	//								border : false,
	//								labelWidth : 60,
	//								items : [releaseDate]
	//							},
	//							planStatus
//
	//					]

	//				})

	//月份
	var month = new Ext.form.TextField({
				style : 'cursor:pointer',
//						id : 'month',
//						columnWidth : 0.5,
				readOnly : true,
//						anchor : "40%",
				fieldLabel : '计划月份',
				name : 'month',
				value : getMonth(),
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M',
									alwaysUseStartDate : false,
									dateFmt : 'yyyy-MM',
									onpicked : function() {
									},
									onclearing : function() {
										planStartDate.markInvalid();
									}
								});
							}
						}
	})
	function getPlanStatus(v){
			
				if (v == 0) {
					status.setValue("未上报");
				}else if (v == 1) {
					status.setValue("审批中");
				}else if (v == 2) {
					status.setValue("已发布");
				}else{
					status.setValue("");
				}
		}
     function query(){
		      Ext.Ajax.request({
						url : 'manageplan/getBpJPlanGuideMain.action',
						params : {
									planTime : month.getValue()
								},
					method : 'post',
					waitMsg : '正在加载数据...',
					success : function(result, request) {
										var o = eval('(' + result.responseText + ')');
										if(o != null){
										planTime.setValue(o.planTimeString);
										status.setValue(o.baseInfo.planStasus);
										releaseDate.setValue(o.releaseDate);
										getPlanStatus(o.baseInfo.planStatus);
										ifsave(o.baseInfo.planStatus);
										planstatus = o.baseInfo.planStatus;
										ds.load({
												params : {
													planTime : planTime.getValue(),
													start : 0,
													limit : 18
												}
											});
										}else {
											ds.load({
												params : {
													planTime : month.getValue(),
													start : 0,
													limit : 18
												}
											});
											//myform.getForm().reset();
											planTime.setValue("");
										    status.setValue("");
										    releaseDate.setValue("");
										Ext.MessageBox.alert('提示信息', '该月计划还未填写!');
										}
									},
					failure : function(result, request) {
							Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
								}
						});
	}
	function updateRecord(){
		if(planstatus != 2){
			Ext.Msg.alert('提示信息', '该月计划状态不允许完成情况录入!');
			return false;
		}
		var rec = grid.getSelectionModel().getSelected();
		if (rec) {
				win.show();
				myaddpanel.getForm().loadRecord(rec);
				ifCheck.setValue((rec.get("baseInfo.ifCheck") == null ? "" : rec.get("baseInfo.ifCheck")));
				ifComplete.setValue((rec.get("baseInfo.ifComplete") == null ? "" : rec.get("baseInfo.ifComplete")));
				checkStatus.setValue((rec.get("baseInfo.checkStatus") == null ? "" : rec.get("baseInfo.checkStatus")));
				checkDesc.setValue((rec.get("baseInfo.checkDesc") == null ? "" : rec.get("baseInfo.checkDesc")));
				Ext.get("completeDesc").dom.value = rec.get("baseInfo.completeDesc") == null ? "" : rec.get("baseInfo.completeDesc");
				if(ifCheck.getValue() == "" || ifCheck.getValue() == null){
					        Ext.get("targetDepcode").dom.value = "";
							targetDepName.setValue("");
							checkStatus.setDisabled(true);
							targetDepName.setDisabled(true);
							Ext.get("checkDesc").dom.readOnly = true;
				}
		} else {
			Ext.Msg.alert('提示信息', '请选择一条需要完成情况录入的记录!');
		}
	
	}
	//根据计划状态控制录入按钮
	function ifsave(v){
		if(v != 2){
			Ext.get("btnSave").dom.disabled = true;
		}
		else{
			Ext.get("btnSave").dom.disabled = false;
		}
	}
	//导出
	function myExport() {
		Ext.Ajax.request({
			url : 'manageplan/getBpJPlanGuideDetail.action?planTime='+month.getValue(),
			success : function(response) {
				var json = eval('(' + response.responseText.trim() + ')');
				var records = json.list;
				var html = ['<table border=1><tr><th>指导内容</th><th>主要落实单位</th><th>任务落实单位</th><th>提出单位</th><th>完成情况</th><th>完成说明</th><th>是否考核</th><th>考核状态</th><th>针对单位</th><th>考核说明</th></tr>'];
				for (var i = 0; i < records.length; i += 1) {
					var rc = records[i];
					html.push('<tr><td>' + rc.baseInfo.guideContent + '</td><td>'
							+ rc.mainDepName + '</td><td>' + rc.baseInfo.taskDepcode
							+ '</td><td>' + rc.referDepName + '</td><td>' + rc.ifCompleteName
							+ '</td><td>' + rc.baseInfo.completeDesc + '</td><td>' + rc.ifCheckName 
							+ '</td><td>' + rc.checkStatusName + '</td><td>' + rc.targetDepName
							+ '</td><td>' + rc.baseInfo.checkDesc + '</td></tr>');
				}
				html.push('</table>');
				html = html.join(''); // 最后生成的HTML表格
				// alert(html);
				tableToExcel(html);
			},
			failure : function(response) {
				Ext.Msg.alert('信息', '失败');
			}
		});
	}
	function tableToExcel(tableHTML) {
		window.clipboardData.setData("Text", tableHTML);
		try {
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExApp.DisplayAlerts = false;
			ExApp.visible = true;
		} catch (e) {
			if (e.number != -2146827859)
				alert("您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}
	//计划ID
	var guideId = new Ext.form.Hidden({
		        id : 'guideId',
		        name :'baseInfo.guideId'
	})
	//指导内容
	var guideContent = new Ext.form.TextArea({
		        id : "guideContent",
				// xtype : "textfield",
				fieldLabel : '指导内容',
				name : 'baseInfo.guideContent',
				readOnly : true,
				height : 40,
				width : 200
	}) 
	//主要落实单位
	var mainDepName = new Ext.form.TextField({
		       id : "mainDepName",
				// xtype : "textfield",
				fieldLabel : '主要落实单位',
				name : 'mainDepName',
				readOnly : true,
				width : 200
	})
	//主要落实单位编码
	var hiddmainDepcode = new Ext.form.Hidden({
				id :'hiddmainDepcode',
				name : 'baseInfo.mainDepcode'
	})
	//任务落实单位
	var taskDepName = new Ext.form.TextField({
		        id : "taskDepName",
				// xtype : "textfield",
				fieldLabel : '任务落实单位',
				name : 'baseInfo.taskDepcode',
				readOnly : true,
				width : 200
	})
	//提出单位
	var referDepName = new Ext.form.TextField({
			   id : "referDepName",
				// xtype : "textfield",
				fieldLabel : '提出单位',
				name : 'referDepName',
				readOnly : true,
				width : 200
	})
	//提出单位编码
	var hiddreferDepcode = new Ext.form.Hidden({
				id :'hiddreferDepcode',
				name : 'baseInfo.referDepcode'
	})
	//完成情况
	var ifComplete = new Ext.form.ComboBox({
				id : "ifComplete",
				fieldLabel : '完成情况',
				store : [['0', '未完成'], ['1', '进行中'],['2','已完成']],
				name : 'baseInfo.ifComplete',
				valueField : "value",
				displayField : "text",
				value : '',
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'baseInfo.ifComplete',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : true,
				emptyText : '请选择',
				width : 200
	})
	//完成说明
	var completeDesc = new Ext.form.TextArea({
		        id : "completeDesc",
				fieldLabel : '完成说明',
				name : 'baseInfo.completeDesc',
				readOnly : false,
				height : 40,
				width : 200
	})
	//是否考核
	var ifCheck = new Ext.form.ComboBox({
				id : 'ifCheck',
		        fieldLabel : '是否考核',
				store : [['0', '否'], ['1', '是']],
				name : 'baseInfo.ifCheck',
				valueField : "value",
				displayField : "text",
				value : '',
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'baseInfo.ifCheck',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : true,
				emptyText : '请选择',
				width : 200,
				listeners : {
					select : function check(){
						if (ifCheck.getValue() == 0){
							Ext.get("checkStatus").dom.displayField = "";
							Ext.get("checkStatus").dom.value="";
							Ext.get("checkDesc").dom.value = "";
							Ext.get("targetDepcode").dom.value = "";
							targetDepName.setValue("");
							checkStatus.setDisabled(true);
							targetDepName.setDisabled(true);
							Ext.get("checkDesc").dom.readOnly = true;
						}
						if(ifCheck.getValue() == 1){
							checkStatus.setDisabled(false);
							targetDepName.setDisabled(false);
							Ext.get("checkDesc").dom.readOnly = false;
						}
					
					}
				}
	})
	//考核状态
	var checkStatus = new Ext.form.ComboBox({
				id : 'checkStatus',
		        fieldLabel : '考核状态',
				store : [['0','未考核'], ['1','已考核']],
				name : 'baseInfo.checkStatus',
				valueField : "value",
				displayField : "text",
				value : '',
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'baseInfo.checkStatus',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : true,
				emptyText : '请选择',
				width : 200
	})
	//针对单位编码
	var hiddtargetDepcode = new Ext.form.Hidden({
		id : 'targetDepcode',
		name : 'baseInfo.targetDepcode'
	})
	//针对单位
	var targetDepName = new  Ext.form.ComboBox({
						fieldLabel : '针对单位',
						readOnly : true,	
						//anchor : "100%",
						width : 200,
						//mode : 'local',
						//triggerAction : 'all',
						//forceSelection : true,
						//selectOnFocus : true,
						editable : false,
						onTriggerClick : function() {
						if(ifCheck.getValue() == 1){
						var args = {
							selectModel : 'single',
							rootNode : {
								id : "0",
								text : '灞桥热电厂'
							}
						}
						var url = "/power/comm/jsp/hr/dept/dept.jsp";
						var rvo = window
								.showModalDialog(
										url,
										args,
										'dialogWidth:270px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
						if (typeof(rvo) != "undefined") {
							hiddtargetDepcode.setValue(rvo.codes);
							targetDepName.setValue(rvo.names);
						}
						}
					}
			})
	//考核说明
	var checkDesc = new Ext.form.TextArea({
		        id : "checkDesc",
				fieldLabel : '考核说明',
				name : 'baseInfo.checkDesc',
				readOnly : false,
				height : 40,
				width : 200
	})
	//窗口form
	var myaddpanel = new Ext.FormPanel({
				frame : true,
				labelAlign : 'center',
				title : '',
				items : [guideId,guideContent,mainDepName,taskDepName,referDepName,hiddmainDepcode,
				hiddreferDepcode,ifComplete,completeDesc,ifCheck,checkStatus,hiddtargetDepcode,targetDepName,
				checkDesc]
			});
	//弹出填写窗口
	var win = new Ext.Window({
				title : "指导性计划完成情况填写",
				width : 350,
				height : 400,
				buttonAlign : "center",
				items : [myaddpanel],
				layout : 'fit',
				resizable : false,
				modal : true,
				closeAction : 'hide',
				buttons : [{
							text : "保存",
							iconCls : 'save',
							handler : function() {
								myaddpanel.getForm().submit({
											method : 'POST',
											url : "manageplan/updateBpJPlanGuideDetail.action",
											success : function(form, action) {
												ds.load({
															params : {
																planTime : month.getValue(),
																start : 0,
																limit : 18
															}
														});
												myaddpanel.getForm().reset();
												win.hide();
												Ext.Msg.alert("注意", "保存成功!");
											},
											faliue : function() {
												Ext.Msg.alert('错误', '保存失败!');
											}
										});
										
							}
						}, {
							text : '取消',
							id : 'btnCancer',
							iconCls : 'cancer',
							handler : function() {
								myaddpanel.getForm().reset();
								win.hide();
							}
						}]

			});

	var tbar = new Ext.Toolbar({
				items : ["计划月份：",month,'-',{
							id : 'btnQuery',
							text : "查询",
							iconCls : 'query',
							handler : function(){
								query();
							}
					    },  '-',{
							id : 'btnDelete',
							iconCls : 'delete',
							text : "导出",
							handler : function(){
								myExport();
							}

						}, '-',{
							id : 'btnSave',
							iconCls : 'save',
							text : "完成情况录入",
							handler : function(){
								updateRecord()
							}

						}]

			});
	var bbar = new Ext.Toolbar({
		items : ["计划月份：",planTime,"计划状态：",status,"发布日期：",releaseDate]
	});
	/* 创建表格 */
	var grid = new Ext.grid.GridPanel({
				ds : ds,
				cm : cm,
				sm : sm,
				tbar : tbar,
				autoWidth : true,
				fitToFrame : true,
				//autoScroll : true,
				border : true,
				frame : true,
				//clicksToEdit : 1,// 单击一次编辑
				bbar : bbar,
				//new Ext.PagingToolbar({
				//			pageSize : 18,
				//			store : ds,
				//			displayInfo : true,
				//			displayMsg : "显示第{0}条到{1}条，共{2}条",
				//			emptyMsg : "没有记录",
				//			beforePageText : '',
				//			afterPageText : ""
				//		}),
				viewConfig : {
					forceFit : false
				}

			});
	grid.on("dblclick", updateRecord);
	//grid.on("rowclick",ifsave);
	// 设定布局器及面板
	var viewport = new Ext.Viewport({
				layout : 'border',
				autoWidth : true,
				autoHeight : true,
				fitToFrame : true,
				items : [{
					region : 'center',
					layout : 'fit',
					items : [grid]
				}]
			});
			query();

})