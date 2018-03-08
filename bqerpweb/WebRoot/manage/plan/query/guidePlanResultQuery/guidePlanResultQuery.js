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
			fieldLabel : '计划月度'
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
//								{
//									columnWidth : '.33',
//									layout : 'form',
//									border : false,
//									labelWidth : 60,
//									items : [status]
//								},
//
//								{
//									columnWidth : '.33',
//									layout : 'form',
//									border : false,
//									labelWidth : 60,
//									items : [releaseDate]
//								},
//								planStatus
//
//						]
//
//					})

	    //月份
	var month = new Ext.form.TextField({
				style : 'cursor:pointer',
//					id : 'month',
//					columnWidth : 0.5,
				readOnly : true,
//					anchor : "40%",
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
	var tbar = new Ext.Toolbar({
				items : ["计划月份：",month,'-',{
							id : 'btnQuery',
							text : "查询",
							iconCls : 'query',
							handler : function(){
								query();
							}
					    }, '-',{
							id : 'btnDelete',
							iconCls : 'delete',
							text : "导出",
							handler : function(){
								myExport();
							}

						}, '-', "<font color='red'>双击查看指导内容</font>"]

			});
	var memoText = new Ext.form.TextArea({
						id : "memoText",
						maxLength : 128,
						readOnly : true,
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
				title : '指导性工作计划内容',
				buttons : [{
							text : '返回',
							iconCls : 'cancer',
							handler : function() {
								win.hide();
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
				//new Ext.Toolbar({
							//pageSize : 18,
							//store : ds,
							//displayInfo : true,
							//displayMsg : "显示第{0}条到{1}条，共{2}条",
							//emptyMsg : "没有记录",
							//beforePageText : '',
							//afterPageText : ""
						//}),
				viewConfig : {
					forceFit : false
				}

			});
	grid.on("dblclick", function() {
			var record = grid.getSelectionModel().getSelected();
			var value = record.get('baseInfo.guideContent');
			memoText.setValue(value);
			win.x = undefined;
			win.y = undefined;
			win.show();
	})
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