// 设置树的点击事件
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
			var w = screen.availWidth - 500;
			var h = screen.availHeight - 290;
			var roleId = "";
			var scrwidth = document.body.clientWidth;
			var atbar = new Ext.Toolbar( {
				items : ['联系人列表:', {
					width : 80
				}]
			});
			var eeetbar = new Ext.Toolbar( {
				items : [
						'客户公司名称:','-',
						{
							id : 'likename',
							xtype : 'textfield',
							width : 100
						},'-',{
							id : 'btnQuery',
							 iconCls : 'query',
							text : "模糊查询",
							width : 80,
							handler : function() {
								Ext.Ajax.request( {
											url : 'message/findByFuzzy.action',
											params : {
												likename : Ext.get("likename").dom.value
											},
											method : 'post',
											waitMsg : '正在查询数据...',
											success : function(result, request) {
												var gridData = eval('('
														+ result.responseText
														+ ')');
												ds.loadData(gridData);
											},
											failure : function(result, request) {
												Ext.Msg.alert('提示信息', "查询失败！");
											}
										})
							}
						},'-',
						{
							id : 'btnAdd',
							text : "联系人增加",
							iconCls : 'add',
							handler : function() {
								var sm = roleGrid.getSelectionModel();
								var selected = sm.getSelections();
								if(selected.length == 0 || selected.length < 0){
									Ext.Msg.alert("提示","请选择一个客户公司！");
								}else{
									var args = {
										selectModel : 'multiple' 
									} 
									var rvo = window.showModalDialog('../../../comm/jsp/hr/workerByDept/workerByDept2.jsp',args,'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
									var currentRole = roleGrid.getSelectionModel().getSelected();
									var companyCode = currentRole.data.zbbmtxCode;
									var workerCodes = rvo.codes; 
									Ext.Ajax.request({
										url : 'message/addContacterInCustomersList.action',
										method : 'post',
										params : {
											       'workerCodes' : workerCodes,
											        'companyCode' : companyCode
											      },
										success :function(result, request){ 
											     Ext.Msg.alert("提示","添加联系人成功!");
											     user_ds.reload();
											      
												
									   },
										failure : function(result,request){
											Ext.Msg.alert("提示","操作失败");
										}
									}); 
								}
								method = "add"; 
							}
						},'-',{
							id : 'btnDelete',
							text : "联系人删除",
							iconCls : 'delete',
							handler : function() {
								var sm = usergrid.getSelectionModel();
								var selected = sm.getSelections();
								var ids = [];
								if (selected.length == 0 || selected.length < 0) {
									Ext.Msg.alert("提示", "请选择要删除的联系人！");
								} else {
									for (var i = 0;i < selected.length; i += 1) {
										var member = selected[i].data;
										if (member.workerCode) {
											ids.push(member.workerCode);
										} else {
											store.remove(store.getAt(i));
										}
										Ext.Msg.confirm("删除","是否确定删除该联系人",
										
														function(button) {
															if (button == "yes") {
																Ext.Ajax.request( {
																			url : 'message/deleteContacter.action',
																			method : 'post',
																			params :{
																				'workerCode': ids.join(",")
																			},
																			waitMsg : '正在删除数据...',
																			success : function(result,request) {
																				Ext.Msg.alert("提示","删除联系人成功！");
																				user_ds.reload();
																			},
																			failure : function(result,request) {
																				Ext.MessageBox.alert('错误','操作失败,请联系管理员!');
																}
													});
											}

									});
								}
							}
						}
					}]
			});
			var rtbar = new Ext.Toolbar( {
				items : ['客户公司名称列表:', {
					width : 80
				}]
			});
			var ds = new Ext.data.Store( {
				proxy : new Ext.data.HttpProxy( {
					url : 'message/findByFuzzy.action',
					method : 'post'
				}),
				reader : new Ext.data.JsonReader( {
					totalProperty : 'data.total',
					root : 'data.list'
				}, [ {
					name : 'zbbmtxCode',
					hidden : true
				}, {
					name : 'zbbmtxName'
				}])
			});
			ds.load( {
				params : {
					likename : ''
				}
			});
           var User = Ext.data.Record.create([ {
					name : 'workerCode',
					hidden : true
				}, {
					name : 'zbbmtxCode',
					hidden : true
				}, {
					name : 'workerName'
				}]);
			var user_ds = new Ext.data.Store( {
				proxy : new Ext.data.HttpProxy( {
					url : 'message/findContacterByCode.action'
				}),
				reader : new Ext.data.JsonReader( {
					totalProperty : 'data.totalCount',
					root : 'data.list'
				}, User)
			});
			//另外加的
			user_ds.load();
			var sm = new Ext.grid.RowSelectionModel( {
				singleSelect : true,
				listeners : {
					rowselect : function(sm, row, rec) {
						zbbmtxCode = rec.data.zbbmtxCode;
					Ext.Ajax.request({
							url : 'message/findContacterByCode.action?zbbmtxCode='+zbbmtxCode,
							method : 'post',
							success : function(result,request){
								var gridData = eval('('+ result.responseText+ ')');
								user_ds.loadData(gridData);
							   // usergrid.getView().refresh();	
							},
							failure : function(result,request){
								Ext.Msg.alert("提示信息","显示失败");
							} 
						})
					}
				}
			});
			// 生成序列号
			var rn = new Ext.grid.RowNumberer( {
				header : "序列号",
				selectMode : 'true',
				width : 50
			});
			// 表单头
			var colModel = new Ext.grid.ColumnModel([rn, {
				header : "",
				dataIndex : "zbbmtxCode",
				hidden : true
			}, {
				header : "客户公司名称",
				dataIndex : "zbbmtxName",
				width : 100
			}]);
			colModel.defaultSortable = true;
			var rbbar = new Ext.PagingToolbar( {
				pageSize : 18,
				store : ds,
				displayInfo : true,
				displayMsg : '共 {2} 条',
				emptyMsg : "没有记录"
			});
			
			var roleGrid = new Ext.grid.GridPanel( {
				id : 'role-grid',
				el : 'role-div',
				ds : ds,
				cm : colModel,
				viewConfig : {
			        forceFit : true
		        },
				sm : sm,
				//bbar : rbbar,
				tbar : rtbar,
				border : true,
				width : 220,
				//autoScroll:true,
				//height : 400,
				fitToFrame : true,
				
				listeners : {
					render : function(g) {
						g.selModel.selectFirstRow();
					},
					delay : 10
				}
			});
			roleGrid.render();
			
			ds.on('beforeload', function() {
				Ext.apply(this.baseParams, {
					likename : Ext.get("likename").dom.value
				});
			});

			user_ds.on('beforeload', function() {
				Ext.apply(this.baseParams, {
					//userlike : Ext.get("auserlike").dom.value,
					zbbmtxCode : zbbmtxCode
				});
			});
			/* 设置每一行的选择框 */
			var user_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false,
				listeners : {
					rowselect : function(sm, row, rec) {

					}
				}
			});

			var user_cm = new Ext.grid.ColumnModel([user_sm,rn, {
				header : '',
				dataIndex : 'workerCode',
				hidden : true
			}, {
				header : '姓名',
				dataIndex : 'workerName',
				align : 'left'
			}]);

			user_cm.defaultSortable = true;
			var abbar = new Ext.PagingToolbar( {
				pageSize : 100,
				store : user_ds,
				displayInfo : true,
				displayMsg : '共 {2} 条',
				emptyMsg : "没有记录"
			})
			var usergrid = new Ext.grid.GridPanel( {
				el : 'already-role-div',
				ds : user_ds,
				cm : user_cm,
				viewConfig : {
			        forceFit : true
		        },
				sm : user_sm,
				//bbar : abbar,
				tbar : atbar,
				width : 220,
				border : true,
				fitToFrame : true
			});
			usergrid.render();
			setTimeout(function() {
				Ext.get('loading').remove();
				Ext.get('loading-mask').fadeOut( {
					remove : true
				});
			}, 250);

			var layout = new Ext.Viewport( {
				region : 'center',
				layout : "border",
				border : false,
				items : [ {
					region : 'north',
					layout : 'fit',
					height : 30,
					items : [eeetbar]
				}, {
					region : 'center',
					layout : 'fit',
					border : false,
					split : true,
					items : [usergrid]
				}, {
					region : "west",
					collapsible : true,
					// title : '角色列表',
						layout : 'fit',
						border : true,
						// margins : '0 0 0 50',
						// width : 230,
						width : scrwidth / 11 * 5,
						split : true,
						items : [roleGrid]
					}]

			})
		});
