Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	
	
	//定义审批单位负责人
     /*var approveChargeBy = {
		fieldLabel : '审批单位负责人',
		name : 'content.approveChargeBy',
		xtype : 'textfield',
		id : 'approveChargeBy',
		allowBlank : false,
		anchor : "100%"
	    };*/
//	   var label=new Ext.form.Label({
//	   	text:'审批单位负责人',
//	   	align:'left'
//	   })
	  //var reportId = getParameter("reportId");
	 var arg = window.dialogArguments;
	 var rec = arg.rec;
	 var rpid = arg.reportId;
	 
	
	  var approveChargeBy= new Ext.form.ComboBox({
						mode : 'remote',
						fieldLabel : '审批单位负责人',
//						 width:'100%',
						anchor : "86%",
						editable : false,
						onTriggerClick : function() {
							var args = {
								selectModel : 'single',
								rootNode : {
									id : "0",
									text : '灞桥热电厂'
								}
							}
							var url = "/power/comm/jsp/hr/workerByDept/workerByDept2.jsp";
							var rvo = window
									.showModalDialog(
											url,
											args,
											'dialogWidth:550px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
							if (typeof(rvo) != "undefined") {
//								record.set("chargeBy", rvo.workerCode);
//								record.set("chargeName", rvo.workerName);
								approveChargeBy.setValue(rvo.workerName);
								approveChargeByCode.setValue(rvo.workerCode);
							}
						}
					})
		var approveChargeByCode = new Ext.form.Hidden({
			id : 'approveChargeByCode',
			name : 'content.approveChargeBy'
		})
					
	  //定义审批单位批复意见
	  var approveText = new Ext.form.TextField({
	  	fieldLabel : '审批单位批复意见',
	  	name : 'content.approveText',
	  	allowBlank : false,
	  	anchor : "86%"
//	  	width:'100%'
	  })
	   //定义审批单位批复时间
	  var approveDate = new Ext.form.TextField({
        id:'approveDate',
        fieldLabel : "审批单位批复时间",
        name : 'content.approveDate',
        style : 'cursor:pointer',
        anchor : "86%",
//      width:'100%',
        //value : getDate(),
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    alwaysUseStartDate : true
                });
            }
        }
    })
    //定义施工单位负责人
    var workChargeBy = new Ext.form.TextField({
    	fieldLabel : '施工单位负责人',
		name : 'content.workChargeBy',
		anchor : "86%"
//		 width:'100%'
    })
    //定义施工单位经办人
	 var workOperateBy =  new Ext.form.TextField({
	 	fieldLabel : '施工单位经办人',
		name : 'content.workOperateBy',
		anchor : "86%"
//		 width:'100%'
	 })
    //施工单位批复时间
	var workApproveDate = new Ext.form.TextField({
        id : 'workApproveDate',
        fieldLabel : "施工单位批复时间",
        name : 'content.workApproveDate',
        style : 'cursor:pointer',
        anchor : "86%",
//       width:'100%',
        //value : getDate(),
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    alwaysUseStartDate : true
                });
            }
        }
    })
    var form = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		autoHeight : true,
		layout : 'form',
		border : false,
        tbar : 'contentbar',
        buttons : [{
					text : '保存',
					handler : function() {
						form.getForm().submit({
									url : 'manageproject/saveOpenRegister.action',
									params:{
										reportId:rpid
									},
									method : 'post',
									success : function(form, action) {
										Ext.Msg.alert('信息', '保存成功');
									},
									failure : function() {
										Ext.Msg.alert('错误', '保存失败！');
									}
									
									
								});
					}
				}, {
					text : '取消',
					handler : function() {
							 form.getForm().reset();
					}
				}],
		items : [{
					border : false,
					layout : 'form',
					items : [{
								border : false,
								layout : 'column',
								items : [
									{
											columnWidth : 0.4,
											layout : 'form',
											labelWidth : 110,
											border : false,
											items : [approveChargeBy,approveChargeByCode]
										}, 
											{
											columnWidth : 0.35,
											layout : 'form',
											labelWidth : 130,
											border : false,
											items : [approveText]
										},{
											columnWidth : 0.25,
											layout : 'form',
											labelWidth : 110,
											border : false,
											items : [approveDate]
										}]
							},{
								border : false,
								layout : 'column',
								items : [{
											columnWidth : 0.4,
											layout : 'form',
											labelWidth : 110,
											border : false,
											items : [workChargeBy]
										}, {
											columnWidth : 0.35,
											layout : 'form',
											labelWidth : 130,
											border : false,
											items : [workOperateBy]
										},{
											columnWidth : 0.25,
											layout : 'form',
											labelWidth : 110,
											border : false,
											items : [workApproveDate]
										}]
							}]

				}]

	});
	    var idss = new Array();
      	function deleteContentTheme(){
		gridContent.stopEditing();
		var sm = gridContent.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("reportContentId") != null) {
					idss.push(member.get("reportContentId"));
				}
				gridContent.getStore().remove(member);
				gridContent.getStore().getModifiedRecords().remove(member);
			}
		}
	
      	}
	   	var contentbar = new Ext.Toolbar({
				items : [{
							id : 'btnAdd',
							iconCls : 'add',
							text : "增加",
							minWidth : 70,
//							disabled : false,
							handler : addcontentTheme
						}, '-', {
							id : 'btnDelete',
							iconCls : 'delete',
							minWidth : 70,
//							disabled : true,
							text : "删除",
							handler : deleteContentTheme
						}, '-', {
							id : 'btnSave',
							iconCls : 'save',
							text : "保存",
							minWidth : 70,
							//disabled : true,
							handler : savecontentTheme
						}, '-', {
							id : 'btnCancel',
							text : "取消",
							minWidth : 70,
							iconCls : 'reflesh',
							//disabled : true,
							handler : function() {
							contentStore.reload();
							} 
						}]});
	function savecontentTheme(){
		
		gridContent.stopEditing();
		var modifyRec = gridContent.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || idss.length > 0) {
			Ext.Msg.confirm('提示信息', '确定要保存修改吗？', function(button) {
				if (button == 'yes') {
					var addData = new Array();
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
							if (modifyRec[i].get('reportContentId') != null) {
								updateData.push(modifyRec[i].data);
							} else {
								addData.push(modifyRec[i].data);
							}
						

					}
					Ext.Ajax.request({
								url : 'manageproject/saveOrUpdateContent.action',
								method : 'post',
								params : {
									reportId: rpid,
									isAdd : Ext.util.JSON.encode(addData),
									isUpdate : Ext.util.JSON.encode(updateData),
									ids : idss.join(","),
									sort:'1'

								},
								success : function(result, request) {
									var o = Ext.util.JSON
											.decode(result.responseText);
									Ext.Msg.alert("提示信息", o.msg);
									contentStore.rejectChanges();
									//ids = [];
									contentStore.reload();
								},
								failure : function(result, request) {
									Ext.Msg.alert("提示信息", "数据保存修改失败！");
									/*gridOperation.rejectChanges();
									ids = [];
									gridOperation.reload();*/
								}
							})
				}
			})

		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}		
						}
	 var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false

			});
	var contentRecord = Ext.data.Record.create([{
		name : 'reportContentId',
				mapping:0

			},
			{
				name : 'content',
				mapping:1

			},{   
				name : 'reportsId',
				mapping:2

			}]);
	var operationRecord = Ext.data.Record.create([{   
				name : 'reportContentId',
				mapping:0

			},
			{   
				name : 'operation',
				mapping:1

			},{   
				name : 'reportsId',
				mapping:2

			}]);

	var contentProxy = new Ext.data.HttpProxy({
				url : 'manageproject/queryContent.action'
			});

	var contentReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, contentRecord);

	var contentStore = new Ext.data.Store({
				proxy : contentProxy,
				reader : contentReader
			});
	var operationProxy = new Ext.data.HttpProxy({
				url : 'manageproject/queryOperation.action'
			});

	var operationReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, operationRecord);
	var operationStore = new Ext.data.Store({
				proxy : operationProxy,
				reader : operationReader
			});
	var ids = new Array();
	function deleteOperationTheme() {
		gridOperation.stopEditing();
		var sm = gridOperation.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("reportContentId") != null) {
					ids.push(member.get("reportContentId"));
				}
				gridOperation.getStore().remove(member);
				gridOperation.getStore().getModifiedRecords().remove(member);
			}
		}
	}
	function queryRecord() {
		operationStore.baseParams={
			sort:'2',
			reportId:rpid
	
		};
		operationStore.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}
	
	function queryContentRecord() {
		contentStore.baseParams={
			sort:'1',
			reportId:rpid
		};
		contentStore.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}
	queryRecord();
	queryContentRecord();
    function addoperationTheme(){
   	 var count = operationStore.getCount();
    var currentIndex = count;
	    var obj = new operationRecord({
					'operation' : ''
				});
		
		gridOperation.stopEditing();
		operationStore.insert(currentIndex, obj);
		sm.selectRow(currentIndex);
		gridOperation.startEditing(currentIndex, 1);
		gridOperation.getView().refresh();
    	
    }
	
				var operationbar = new Ext.Toolbar({
				items : [{
							id : 'btnAdd',
							iconCls : 'add',
							text : "增加",
							minWidth : 70,
							//disabled : true,
							handler : addoperationTheme
						}, '-', {
							id : 'btnDelete',
							iconCls : 'delete',
							minWidth : 70,
							//disabled : true,
							text : "删除",
							handler : deleteOperationTheme
						}, '-', {
							id : 'btnSave',
							iconCls : 'save',
							text : "保存",
							minWidth : 70,
							//disabled : true,
							handler : saveoperationTheme
						}, '-', {
							id : 'btnCancel',
							text : "取消",
							iconCls : 'reflesh',
							minWidth : 70,
							//disabled : true,
							handler : function() {
							operationStore.reload();
							} 
						}]});

       var gridOperation=  new Ext.grid.EditorGridPanel({
				layout : 'fit',
				sm : sm,
				enableColumnMove : false,
				store : operationStore,
				width:'30%',
				columns : [sm, new Ext.grid.RowNumberer({
									header : "序号",
									width : 35
								}),{
								  header:"id",
								  sortable:true,
								  dataIndex:'reportContentId',
								  hidden:true
								}, {
								  header:"开工报告号",
								  sortable:true,
								  dataIndex:'reportsId',
								  hidden:true
								},{
							header : "施工完成情况",
							sortable : true,
							dataIndex : 'operation',
							width : 825,
							editor : new Ext.form.TextField({
								style : 'cursor:pointer',
								allowDecimals :false,
								allowBlank : true
							})
//							width:150
						}],
				tbar : operationbar,
//				viewConfig : {
//					forceFit : true
//				},
				//分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : operationStore,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
		,
			//	clicksToEdit : 1,
				autoSizeColumns : true

			});
	
	function addcontentTheme() {
		var count = contentStore.getCount();
		var currentIndex = count;
		var o = new contentRecord({
						'content' : ''
					});
		gridContent.stopEditing();
		contentStore.insert(currentIndex, o);
		gridContent.startEditing(currentIndex, 1);
		gridContent.getView().refresh();
	};
	 var ids = new Array();
	function deleteOperationTheme() {
		gridOperation.stopEditing();
		var smd = gridOperation.getSelectionModel();
		var selected = smd.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("reportContentId") != null) {
					ids.push(member.get("reportContentId"));
				}
				gridOperation.getStore().remove(member);
				gridOperation.getStore().getModifiedRecords().remove(member);
			}
		}
	}
	function saveoperationTheme() {
		gridOperation.stopEditing();
		var modifyRec = gridOperation.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			Ext.Msg.confirm('提示信息', '确定要保存修改吗？', function(button) {
				if (button == 'yes') {
					var addData = new Array();
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
							if (modifyRec[i].get('reportContentId') != null) {
								updateData.push(modifyRec[i].data);
							} else {
								addData.push(modifyRec[i].data);
							}
						

					}
					Ext.Ajax.request({
								url : 'manageproject/saveOrUpdateOperation.action',
								method : 'post',
								params : {
									reportId: rpid,
									isAdd : Ext.util.JSON.encode(addData),
									isUpdate : Ext.util.JSON.encode(updateData),
									ids : ids.join(","),
									sort:'2'

								},
								success : function(result, request) {
									var o = Ext.util.JSON
											.decode(result.responseText);
									Ext.Msg.alert("提示信息", o.msg);
									operationStore.rejectChanges();
									
									//ids = [];
									operationStore.reload();
								},
								failure : function(result, request) {
									Ext.Msg.alert("提示信息", "数据保存修改失败！");
									/*gridOperation.rejectChanges();
									ids = [];
									gridOperation.reload();*/
								}
							})
				}
			})

		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}}
		var smds=new Ext.grid.CheckboxSelectionModel({
				singleSelect : false

			});
       var gridContent=new Ext.grid.EditorGridPanel({
				layout : 'fit',
				autoScrolla : true,
				height : 400,
			    sm : smds,
				enableColumnMove : false,
				store : contentStore,
				columns : [smds, 
				new Ext.grid.RowNumberer({
									header : "序号",
									width : 35
								}), {
							header : "工程主要内容及方案",
							sortable : true,
							dataIndex : 'content',
							width : 825,
							editor : new Ext.form.TextField({
								
							})
						}],
//						viewConfig : {
//							forceFit : true
//						},
				tbar : contentbar,
				//分页
				bbar : new Ext.PagingToolbar({
					pageSize : 18,
					store : contentStore,
					displayInfo : true,
					displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
					emptyMsg : "没有记录"
				}),
			//	clicksToEdit : 1,
				autoSizeColumns : true

 });
 
 	if(rec.data.approveChargeBy!=null){
	 	 approveChargeBy.setValue(rec.data.approveChargeBy);
	 }
	 if(rec.data.approveText!=null){
	 	approveText.setValue(rec.data.approveText);
	 } if(rec.data.approveDate!=null){
	 	approveDate.setValue(rec.data.approveDate.substring(0,10));
	 } if(rec.data.workChargeBy!=null){
	 	 workChargeBy.setValue(rec.data.workChargeBy);
	 } if(rec.data.workOperate_by!=null){
	 	workOperateBy.setValue(rec.data.workOperate_by);
	 } if(rec.data.workApproveDate!=null){
	 	workApproveDate.setValue(rec.data.workApproveDate.substring(0,10));
	 }
    	var rewritePanel = new Ext.Viewport({
				layout : 'fit',
				border:false,
				items : [ form, {
			region : 'east',
			height : 200,
			layout:'fit',
			items : [gridOperation]
		}, {
			region : 'west',
			height : 400,
			items : [gridContent]
		}// 初始标签页
				],
				  defaults : {
            autoScroll : true
        }
	}); 
  });