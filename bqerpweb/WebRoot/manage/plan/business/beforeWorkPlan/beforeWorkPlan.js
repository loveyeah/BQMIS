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
			var plantime="";
			//取得当前用户的工号
			function getWorkCode() {
				Ext.lib.Ajax.request('POST',
						'comm/getCurrentSessionEmployee.action', {
							success : function(action) {
								var result = eval("(" + action.responseText
										+ ")");
								if (result.workerCode) {
									// 设定默认工号
									editBy.setValue(result.workerName);
									hiddeditBy.setValue(result.workerCode);
								}
							}
						});
			}

			//內容
			var content = new Ext.form.TextArea({
				id : 'planForeword',
				xtype : 'textarea',
				fieldLabel : '计划前言',
				readOnly : false,
				name : 'baseInfo.planForeword',
				allowBlank : false,
				height : 200,
				anchor : '97%'
			});

			// 填写人
			var editBy = new Ext.form.TextField({
						id : "editBy",
						name : "workName",
						fieldLabel : '填写人',
						readOnly : true,
						anchor : "90%"
					})
			//计划时间
			var planTime = new Ext.form.Hidden({
				id : 'hiddplanTime',
				name : 'baseInfo.planTime'
			})
			//前言状态
			var hiddplanStatus = new Ext.form.Hidden({
			    id : 'hiddplanStatus',
			    name : 'baseInfo.planStatus'
			})
			//填写人编码
			var hiddeditBy = new Ext.form.Hidden({
						id : 'hiddeditBy',
						name : 'baseInfo.editBy'
					})
			// 填写日期
		    var hiddeditDate = new Ext.form.Hidden({
						id : "hiddeditDate",
						name : "baseInfo.editDate",
						mode : 'local',
						value : getDate()
					})
			var editDate = new Ext.form.TextField({
						id : "editDate",
						readOnly : true,
						fieldLabel : '填写日期',
						name : 'editDateString',
						value : "",
						anchor : "95%"
					})
			//月份
			var month = new Ext.form.TextField({
						style : 'cursor:pointer',
//						id : 'month',
						columnWidth : 0.5,
						readOnly : true,
						anchor : "70%",
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
			//前言状态		
			var status = new Ext.form.TextField({
				id : 'planstatus',
				readOnly : true,
				name : 'planstatus',
				//anchor : "30%",
				width : 50
				
			})			
			var stocktbar = new Ext.Toolbar({
					items : ["计划月份:", month, {
								id : 'btnQuery',
								text : "查询",
								iconCls : 'query',
								handler : getMain
							},'-',{
							id : 'btnAdd',
							text : '新增',
							iconCls : 'add',
							handler : function() {
								plantime = null;									
								Myform.getForm().reset();
								status.setValue("");
								editDate.setValue(getDate());
								getWorkCode();
										}
							}, '-', {
							id : 'btnSave',
							text : '保存',
							iconCls : 'save',
							handler : function() {
								var url;
								if(plantime == null || plantime == ""){
								url ='manageplan/addBeforeWorkPlan.action';
								}else{
								url ='manageplan/updateBeforeWorkPlan.action';
								}
								if ( Myform.getForm().isValid()) {
									 Myform.getForm().submit({
										method : 'post',
										url:url,
										params : {
											planTime : month.getValue()
										},
											success : function(form, action) {
											var o = eval('(' + action.response.responseText
												+ ')');
											plantime = o.plt;
											getPlanStatus(o.status)
											getAllButtonStatus(o.status);
											getWorkCode();
											editDate.setValue(getDate());
											Ext.MessageBox.alert('提示信息', '保存成功，可以对该月计划前言进行定稿操作！');
										},
											failure : function(form, action) {
												var o = eval('(' + action.response.responseText
												+ ')');
											Ext.MessageBox.alert('错误', o.msg);
										}	
									})
								}
								}
							},'-',{
					    	id : 'btnDelete',
					    	text : '删除',
					    	iconCls : 'delete',
					    	handler : DeletePlan
							},'-',{
							id : 'btnDefine',
							text : '定稿',
							iconCls: 'update',
							handler : definePlan
							},'-',{
							id : 'btnEdit',
							text : '发布',
							iconCls : 'upcommit',
							handler : editPlan
							},'-',"<font color='red'>前言状态:</font>",status]
			});
			//初始化页面查询、根据月份查询
			function getMain(){
							Ext.Ajax.request({
								url : 'manageplan/getBeforeWorkPlanInfo.action',
								params : {
											planTime : month.getValue()
										},
							method : 'post',
							waitMsg : '正在加载数据...',
							success : function(result, request) {
											var o = eval('(' + result.responseText + ')');
											if(o != null){
												content.setValue(o.baseInfo.planForeword);
												editBy.setValue(o.editName);
												editDate.setValue(o.editDateString);
												hiddeditBy.setValue(o.baseInfo.editBy);
												hiddplanStatus.setValue(o.baseInfo.planStatus);
												planTime.setValue(o.baseInfo.planTime);
												getPlanStatus(o.baseInfo.planStatus)
												getAllButtonStatus(o.baseInfo.planStatus);
												plantime = o.baseInfo.planTime;
											}else {
												Ext.MessageBox.alert('提示信息', '没有该月的数据!');
												Myform.getForm().reset();
												plantime = "";
												getAllButtonStatus(v);
												status.setValue("");
												editDate.setValue("");
												//getWorkCode();
											}
										},
							failure : function(result, request) {
									Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
										}
								});
			}
			//删除前言
			function DeletePlan(){
					if(plantime == null || plantime == ""){
						Ext.Msg.alert("提示","请选择你要删除的记录！");
						return false;
					}
					Myform.getForm().submit({
					method : 'post',
					params : {
						planTime : month.getValue()
					},
					url : 'manageplan/deleteBeforeWorkPlan.action',
					success : function(form, action) {
						Ext.MessageBox.alert('提示信息', '删除成功!');				
						Myform.getForm().reset();
						plantime = "";
						status.setValue("");
						//getWorkCode();
					},
					failure : function(form, action) {
						Ext.MessageBox.alert('错误', '删除失败!');
					}
				})
			}
			//获取前言状态
			function getPlanStatus(v){
				
					if (v == 0) {
						status.setValue("未定稿");
					}
					else if (v == 1) {
						status.setValue("未发布");
					}
					else if (v == 2) {
						status.setValue("已发布");
					}else{
						status.setValue("");
					}
			}
			//定稿
			function definePlan(){
				if(plantime == null || plantime == ""){
						Ext.Msg.alert("提示","请选择你要操作的记录！");
						return false;
				}
				Ext.Ajax.request({
									url : 'manageplan/defineBeforeWorkPlan.action',
									params : {
													planTime : month.getValue()
											},
									method : 'post',
									waitMsg : '正在加载数据...',
									success : function(result, request) {
														var o = eval('(' + result.responseText + ')');
														getPlanStatus(o.status)
														getAllButtonStatus(o.status);
														hiddplanStatus.setValue(o.status);
														Ext.MessageBox.alert('提示信息','定稿成功,可以对该月计划前言进行发布操作！');
													
													},
									failure : function(result, request) {
											Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
												}
							});
				
			}
			//发布
			function editPlan(){
				if(plantime == null || plantime == ""){
						Ext.Msg.alert("提示","请选择你要操作的记录！");
						return false;
				}
				if(hiddplanStatus.getValue() == 0){
					Ext.Msg.alert("提示","该状态记录不允许发布！");
					return false;
				}
				Ext.Ajax.request({
									url : 'manageplan/editBeforeWorkPlan.action',
									params : {
													planTime : month.getValue()
												},
									method : 'post',
									waitMsg : '正在加载数据...',
									success : function(result, request) {
														var o = eval('(' + result.responseText + ')');
														getPlanStatus(o.status);
														getAllButtonStatus(o.status);
														plantime = "";
														Ext.MessageBox.alert('提示信息',o.msg);
													
													},
									failure : function(result, request) {
											Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
												}
							});
			}
			//控制按钮是否可用
			function getAllButtonStatus(v){
				if(v == 1){
					//当前言状态为定稿时，即状态值为1时除查询、发布按钮可用，其余都不可用。
					Ext.get('btnDelete').dom.disabled = true;
					Ext.get('btnAdd').dom.disabled = true;
					Ext.get('btnDefine').dom.disabled = true;
					Ext.get('btnSave').dom.disabled = true;
				}else if(v == 2){
					//当前言状态为发布时，即状态值为2时除查询按钮可用，其余都不可用。
					Ext.get('btnDelete').dom.disabled = true;
					Ext.get('btnAdd').dom.disabled = true;
					Ext.get('btnDefine').dom.disabled = true;
					Ext.get('btnSave').dom.disabled = true;
					Ext.get('btnEdit').dom.disabled = true;
				}else {
					//当前言状态为未上报和为空时，按钮都可用。
					Ext.get('btnDelete').dom.disabled = false;
					Ext.get('btnAdd').dom.disabled = false;
					Ext.get('btnDefine').dom.disabled = false;
					Ext.get('btnSave').dom.disabled = false;
					Ext.get('btnEdit').dom.disabled = false;
				}
			}
			//mianform	
			var formContent = new Ext.form.FieldSet({
				border : false,
				layout : 'column',
				items : [{
							bodyStyle : "padding:10px 5px ",
							columnWidth : 1,
							layout : 'form',
							labelWidth : 70,
							border : false,
							items : [content,planTime]
						}, {
							bodyStyle : "padding:5px 5px ",
							border : false,
							layout : 'form',
							columnWidth : .5,
							labelWidth : 70,
							items : [hiddeditBy,editBy]
						}, {
							bodyStyle : "padding:5px 5px ",
							border : false,
							layout : 'form',
							columnWidth : .5,
							labelWidth : 70,
							items : [hiddeditDate,editDate,hiddplanStatus]
						}]
			});
			var Myform = new Ext.form.FormPanel({
				bodyStyle : "padding:5px 5px 0",
				labelAlign : 'right',
				labelWidth : 50,
				autoHeight : false,
				region : 'center',
				border : false,
				tbar : stocktbar,
				items : [formContent]
			});
		// 设定布局器及面板
			new Ext.Viewport({
				enableTabScroll : true,
				align : 'center',
				layout : 'border',
				height : 3000,
				items : [Myform]

			});	
			getMain()
			//getWorkCode()
			
});
