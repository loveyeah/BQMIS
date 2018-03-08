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
			//取得当前用户的工号
			function getWorkCode() {
				Ext.lib.Ajax.request('POST',
						'comm/getCurrentSessionEmployee.action', {
							success : function(action) {
								var result = eval("(" + action.responseText
										+ ")");
								if (result.workerCode) {
									// 设定默认工号
									editByName.setValue(result.workerName);
									editBy.setValue(result.workerCode);
								}
							}
						});
			}
	// 工作流序号
	var workFlowNo;
	// 工作流状态
	var signStatus;
	var plantime = "";
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
				name : 'mainDepName'
			}, {
				name : 'referDepName'
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
	//指导内容
	var guideContent = new Ext.form.TextArea({
		    header : '指导内容',
			dataIndex : 'baseInfo.guideContent',
			align : 'left',
			width : 300,
			editor : new Ext.form.TextArea({
            		maxLength : 250,
					listeners:{
					    "render" : function() {
					        this.el.on("dblclick", function(){ 
					        	var record = grid.getSelectionModel().getSelected();
                              	grid.stopEditing();
                              	taShowMemo.setValue(record.get("baseInfo.guideContent"));
                              	win.show();
					        })
					    }
					}
				})
	})
	//主要落实单位
	var mainDepName = {
		    header : '主要落实单位',
			dataIndex : 'mainDepName',
			width : 150,
			align : 'left'
			//editor : mainDeptBox
	}
	//任务落实单位
	var taskDepName = {
			header : '任务落实单位',
			dataIndex : 'baseInfo.taskDepcode',
			align : 'left',
			width : 150,
			editor : new Ext.form.TextField({allowBlank : false})
	}
	//提出单位
	var referDepName = {
		    header : '提出单位',
			dataIndex : 'referDepName',
			align : 'left',
			width : 150
			//editor : referDeptBox
	}
	//计划ID
	var guideId = new Ext.form.TextField({
		dataIndex : 'baseInfo.guideId',
		hidden : true
	});
	//主要落实单位编码
	var mainDepCode = new Ext.form.TextField({
		dataIndex : 'baseInfo.mainDepcode',
		hidden : true
	});
	//提出单位编码
	var referDepCode= new Ext.form.TextField({
		dataIndex : 'baseInfo.referDepcode',
		hidden : true
	});
	
	var cm = new Ext.grid.ColumnModel([sm, rn, 
			guideContent,mainDepName,taskDepName ,referDepName,guideId,mainDepCode,referDepCode]);
			
	function choseEdit(grid, rowIndex, columnIndex, e) {
		var record = grid.getStore().getAt(rowIndex);
		var fieldName = grid.getColumnModel()
				.getDataIndex(columnIndex);

		if (fieldName == "mainDepName" || fieldName == "referDepName") {
			
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
				if (fieldName == "mainDepName") {
					var record = grid.getSelectionModel().getSelected();
					record.set("baseInfo.mainDepcode", rvo.codes);
					record.set("mainDepName", rvo.names);
				} else if (fieldName == "referDepName") {
					var record = grid.getSelectionModel().getSelected();
					record.set("baseInfo.referDepcode", rvo.codes);
					record.set("referDepName", rvo.names);
				} else {
				}
			}
		}
		if (fieldName == "clickToChose") {
			choseSpare();
		}
	};
		
	//计划月度
	var planTime = new Ext.form.TextField({
			readOnly : true,
			fieldLabel : '计划月度'
	})
	// 编辑人名称
	var editByName = new Ext.form.TextField({
				readOnly : true,
				fieldLabel : '编辑人'
			});

	// 编辑时间
	var editDate = new Ext.form.TextField({
				readOnly : true,
				name : 'editDate',
				fieldLabel : '编辑时间'
			});

	// 编辑人编码
	var editBy = new Ext.form.Hidden({
				name : 'editBy'
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
	//								items : [editByName]
	//							},
//
	//							{
	//								columnWidth : '.33',
	//								layout : 'form',
	//								border : false,
	//								labelWidth : 60,
	//								items : [editDate]
	//							},
//
	//							editBy
//
	//					]
//
	//				})

	// 重置排序号
	function resetLine() {
		for (var j = 0; j < ds.getCount(); j++) {
			var temp = ds.getAt(j);
			temp.set("displayNo", j + 1);

		}
	}
	// 增加
	function addRecords() {
		editDate.setValue(getDate());
		getWorkCode();
		planTime.setValue(month.getValue());
		var currentRecord = grid.getSelectionModel().getSelected();
		var rowNo = ds.indexOf(currentRecord);
		var count = ds.getCount();
		var o = new rec({
					//'reportCode' : '',
					'baseInfo.guideContent' : '',
					'mainDepName' : '',
					'baseInfo.taskDepcode'  : '',
					'referDepName'   : ''
				});

		grid.stopEditing();
		ds.insert(count, o);
		sm.selectRow(count);
		grid.startEditing(count, 2);
//		resetLine();
	}

	// 删除记录
	var ids = new Array();
	function deleteRecords() {
		grid.stopEditing();
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("baseInfo.guideId") != null) {
					ids.push(member.get("baseInfo.guideId"));
				}
				grid.getStore().remove(member);
				grid.getStore().getModifiedRecords().remove(member);
			}
			// resetLine();
		}
	}
	// 保存
	function save() {
		grid.stopEditing();
		var modifyRec = grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			if (!confirm("确定要保存修改吗?"))
				return;
			// var newData = new Array();
			var updateData = new Array();
			for (var i = 0; i < modifyRec.length; i++) {
				if(modifyRec[i].get('baseInfo.guideContent') == "") {
					Ext.MessageBox.alert('提示信息', '指导内容不能为空！')
					return 
				}
				if(modifyRec[i].data.mainDepName == "") {
					Ext.MessageBox.alert('提示信息', '主要落实单位不能为空！')
					return 
				}
				if(modifyRec[i].get('baseInfo.taskDepcode') == "") {
					Ext.MessageBox.alert('提示信息', '任务落实不能为空！')
					return 
				}
				if(modifyRec[i].data.referDepName == "") {
					Ext.MessageBox.alert('提示信息', '提出单位不能为空！')
					return 
				}
				
				updateData.push(modifyRec[i].data);
				
			}

			Ext.Ajax.request({
						url : 'manageplan/saveBpJPlanGuide.action',
						method : 'post',
						params : {
							editDate : editDate.getValue(),
							planTime : plantime,
							monthTime : month.getValue(),
							isUpdate : Ext.util.JSON.encode(updateData),
							isDelete : ids.join(",")
						},
						success : function(result, request) {
							var o = eval('(' + result.responseText + ')');
							Ext.MessageBox.alert('提示信息', o.msg);

							ds.rejectChanges();
							ids = [];
							//ds.load({
							//			params : {
							//						planTime : planTime.getValue(),
							//						start : 0,
							//						limit : 18
							//					}
							//				});
							getMain();
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
	function cancer() {
		var modifyRec = ds.getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			if (!confirm("确定要放弃修改吗"))
				return;
			ds.reload();
			ds.rejectChanges();
			ids = [];
		} else {
			ds.reload();
			ds.rejectChanges();
			ids = [];
		}
	}
	
	//月份
	var month = new Ext.form.TextField({
				style : 'cursor:pointer',
//				id : 'month',
//				columnWidth : 0.5,
				readOnly : true,
//				anchor : "40%",
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
	function getMain(){
		Ext.Ajax.request({
						url : 'manageplan/getBpJPlanGuideMain.action',
						params : {
									planTime : month.getValue()
								},
					method : 'post',
					waitMsg : '正在加载数据...',
					success : function(result, request) {
										signStatus = "";
										var o = eval('(' + result.responseText + ')');
										if(o != null){
											ds.rejectChanges();
								            ids = [];
											editByName.setValue(o.editByName);
											editDate.setValue(o.editDate);
											editBy.setValue(o.baseInfo.editBy);
											planTime.setValue(o.planTimeString);
											signStatus = o.baseInfo.signStatus;
											//alert(signStatus);
											workFlowNo = o.baseInfo.workFlowNo;
											plantime = (o.planTimeString == null ? "" : o.planTimeString);
											ds.load({
													params : {
														planTime : planTime.getValue(),
														start : 0,
														limit : 18
													}
												});
											if(signStatus == '0'|| signStatus == '3'){
												Ext.getCmp('btnAdd').setDisabled(false);
												Ext.getCmp('btnDelete').setDisabled(false);
												Ext.getCmp('btnCancer').setDisabled(false);
												Ext.getCmp('btnSave').setDisabled(false);
												Ext.getCmp('btnApprove').setDisabled(true);
												Ext.MessageBox.alert('提示信息', '该月计划还未上报!');
											}
											if(signStatus == '1'){
												Ext.getCmp('btnAdd').setDisabled(false);
												Ext.getCmp('btnDelete').setDisabled(false);
												Ext.getCmp('btnCancer').setDisabled(false);
												Ext.getCmp('btnSave').setDisabled(false);
												Ext.getCmp('btnApprove').setDisabled(false);
											}
											if(signStatus == '2'){
												Ext.getCmp('btnAdd').setDisabled(true);
												Ext.getCmp('btnDelete').setDisabled(true);
												Ext.getCmp('btnCancer').setDisabled(true);
												Ext.getCmp('btnSave').setDisabled(true);
												Ext.getCmp('btnApprove').setDisabled(true);
												Ext.MessageBox.alert('提示信息', '该月计划已经审批!');
											}
										}else {
										
											ds.removeAll();
											plantime = "";
											//myform.getForm().reset();
											planTime.setValue("");
										    editByName.setValue("");
										    editDate.setValue("");
										    editBy.setValue("");
											//if(signStatus == ""){
											//tbar.setDisabled(false);
											//}
											Ext.getCmp('btnAdd').setDisabled(true);
											Ext.getCmp('btnDelete').setDisabled(true);
											Ext.getCmp('btnCancer').setDisabled(true);
											Ext.getCmp('btnSave').setDisabled(true);
											Ext.getCmp('btnApprove').setDisabled(true);
											ds.rejectChanges();
							                ids = [];
											Ext.MessageBox.alert('提示信息', '该月计划还未填写!');
										}
									},
					failure : function(result, request) {
							Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
								}
						});
	}
	
	// 签字
	function approve() {
		if(signStatus == '0'){
			Ext.Msg.alert("提示","该月记录还未上报！");
		}
		var url = "../SignGuidePlan/approveGuidePlan.jsp";
		var args = new Object();
		args.prjNo = planTime.getValue();
		args.entryId = workFlowNo;
		args.workflowType = "bqGuidePlan";
		args.prjTypeId = "";
		args.prjStatus = signStatus;

		var obj = window.showModalDialog(url, args,
				'status:no;dialogWidth=750px;dialogHeight=550px');
		workFlowNo = obj.workFlowNo;
		signStatus = obj.signStatus;
		// 按钮设为不可用
		if(signStatus == '2'){
			Ext.getCmp('btnAdd').setDisabled(true);
			Ext.getCmp('btnDelete').setDisabled(true);
			Ext.getCmp('btnCancer').setDisabled(true);
			Ext.getCmp('btnSave').setDisabled(true);
			Ext.getCmp('btnApprove').setDisabled(true);
		}
		if(signStatus == '3'){
			Ext.getCmp('btnAdd').setDisabled(false);
			Ext.getCmp('btnDelete').setDisabled(false);
			Ext.getCmp('btnCancer').setDisabled(false);
			Ext.getCmp('btnSave').setDisabled(false);
			Ext.getCmp('btnApprove').setDisabled(true);
		}

	}
	// 详细信息录入
    var taShowMemo = new Ext.form.TextArea({
		id : "taShowMemo",
		maxLength : 250,
    	width : 180
    });
    
    // 弹出画面
	var win = new Ext.Window({
		height : 170,
		width : 350,
		layout : 'fit',
		resizable : false,
		modal  : true,
		closeAction : 'hide',
		items : [taShowMemo],
		buttonAlign : "center",
		title : '详细信息录入窗口',
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
				var record = grid.getSelectionModel().getSelected();
				record.set("baseInfo.guideContent", taShowMemo.getValue());
				win.hide();
			}
		}, {
			text : '取消',
			iconCls : 'cancer',
			handler : function() {
				win.hide();
			}
		}]
	});
	win.on('show', function() {
		taShowMemo.focus(true, 100);
	});
	var tbar = new Ext.Toolbar({
				items : ["计划月份：",month,'-',{
							id : 'btnQuery',
							text : "查询",
							iconCls : 'query',
							handler : function(){
								getMain();
							}
					    }, '-',{
							id : 'btnAdd',
							iconCls : 'add',
							text : "新增",
							handler : addRecords

						}, '-',{
							id : 'btnDelete',
							iconCls : 'delete',
							text : "删除",
							handler : deleteRecords

						}, '-',{
							id : 'btnCancer',
							iconCls : 'cancer',
							text : "取消",
							handler : cancer

						}, '-', {
							id : 'btnSave',
							iconCls : 'save',
							text : "保存修改",
							handler : save
						}, '-', {
							id : "btnApprove",
							text : "审批",
							iconCls : 'approve',
							handler : approve
							
						}]

			});
	var bbar = new Ext.Toolbar({
		items : ["计划月份：",planTime,"编辑人：",editByName,"编辑时间：",editDate,editBy]
	});
	/* 创建表格 */
	var grid = new Ext.grid.EditorGridPanel({
				// el : 'siteTeam',
				ds : ds,
				cm : cm,
				sm : sm,
				tbar : tbar,
				autoWidth : true,
				fitToFrame : true,
				border : true,
				frame : true,
				clicksToEdit : 2,// 双击进行编辑
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
	grid.on('celldblclick', choseEdit);
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
		getMain();
		
})