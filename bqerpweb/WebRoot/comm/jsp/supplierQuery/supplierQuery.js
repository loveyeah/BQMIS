//Ext.onReady(function() {
//
//			// 供应商编码/名称 TextField
//			var txtQuerySupplyCode = new Ext.form.TextField({
//						id : 'querySupplyCode',
//						width : 250,
//						emptyText : "供应商编码/名称"
//					})
//
//			// 模糊查詢 Button
//			var btnFuzzyQuery = new Ext.Button({
//						text : '模糊查询',
//						handler : fuzzyQueryHandler
//					})
//
//			// 确认 Button
//			var btnConfirm = new Ext.Button({
//						text : '确认',
//						handler : confirmHandler
//					})
//		    // 取消按钮
//		    var btnCancel = new Ext.Button({
//		    	id : 'btnCancel',
//		    	text : Constants.BTN_CANCEL,
//		    	iconCls : Constants.CLS_CANCEL,
//		    	handler : cancelHandler
//		    })
//
//			// 供应商查询DataStore
//			var dsQuery = new Ext.data.JsonStore({
//						url : 'resource/getSupplierGegerateInfo.action',
//						root : 'list',
//						fields : ['supplierId', 'supplier', 'supplyName',
//								'companyTypeDesc', 'principal',
//								'registeredCapital', 'companyTel']
//					})
//
//			// 初期化数据读入
//			dsQuery.load({
//						params : {
//							start : 0,
//							limit : Constants.PAGE_SIZE
//						}
//					});
//
//			// 设置默认排序
//			dsQuery.setDefaultSort('supplier');
//
//			// 供应商查询Grid列内容定义
//			var cmQuery = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
//								header : "行号",
//								width : 35
//							}), {
//						sortable : true,
//						header : '供应商编码',
//						dataIndex : 'supplier',
//						align : 'left'
//					}, {
//						sortable : true,
//						header : '供应商名称',
//						width : 300,
//						dataIndex : 'supplyName',
//						align : 'left'
//					}, {
//						sortable : true,
//						header : '公司负责人',
//						dataIndex : 'principal',
//						align : 'left'
//					}, {
//						sortable : true,
//						header : '公司性质',
//						dataIndex : 'companyTypeDesc',
//						align : 'left'
//					}, {
//						sortable : true,
//						header : '注册资金',
//						dataIndex : 'registeredCapital',
//						align : 'left'
//					}, {
//						sortable : true,
//						header : '公司电话',
//						dataIndex : 'companyTel',
//						align : 'left'
//					}])
//
//			// 供应商查询 ToolBar
//			var tbarQuery = new Ext.Toolbar({
//						items : ["供应商编码/名称:", txtQuerySupplyCode, "-",
//								btnFuzzyQuery, "-", btnConfirm, "-", btnCancel]
//					})
//
//			// 供应商查询 Grid
//			var gridQuery = new Ext.grid.GridPanel({
//						layout : 'fit',
//						ds : dsQuery,
//						cm : cmQuery,
//						tbar : tbarQuery,
//						sm : new Ext.grid.RowSelectionModel({
//									singleSelect : true
//								}),
//						autoScroll : true,
//						enableColumnMove : false,
//						enableColumnHide : true,
//						border : false,
//						// 分页
//						bbar : new Ext.PagingToolbar({
//									pageSize : Constants.PAGE_SIZE,
//									store : dsQuery,
//									displayInfo : true,
//									displayMsg : Constants.DISPLAY_MSG,
//									emptyMsg : Constants.EMPTY_MSG
//								}),
//						viewConfig : {
//							forceFit : true,
//							autoFill : true
//						}
//					})
//
//			// 供应商查询 Panel
//			var tabQuery = new Ext.Panel({
//						layout : 'fit',
//						border : true,
//						items : [gridQuery]
//					})
//
//			new Ext.Viewport({
//						enableTabScroll : true,
//						layout : "fit",
//						border : false,
//						items : [{
//									layout : 'fit',
//									border : false,
//									margins : '0 0 0 0',
//									region : 'center',
//									autoScroll : true,
//									items : [tabQuery]
//								}]
//					})
//
//			// 查询画面Grid双击事件
//			gridQuery.on("rowdblclick", queryDbClickHandler);
//
//			/**
//			 * 双击供应商查询TAB中Grid
//			 */
//			function queryDbClickHandler() {
//			    // 如果没有选择，弹出提示信息
//                if(!gridQuery.selModel.hasSelection()){
//                    Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
//                    return;
//                }  
//				var record = gridQuery.getSelectionModel().getSelected();
//				var object = new Object();
//				if (record != null) {
//					object.supplierId = record.data.supplierId;
//					object.supplier = record.data.supplier;
//					object.supplyName = record.data.supplyName;
//					object.companyTypeDesc = record.data.companyTypeDesc;
//					object.principal = record.data.principal;
//					object.registeredCapital = record.data.registeredCapital;
//					object.companyTel = record.data.companyTel;
//					window.returnValue = object;
//					window.close();
//				}
//			}
//
//			/**
//			 * 供应商查询TAB确认BUTTON
//			 */
//			function confirmHandler() {
//				queryDbClickHandler();
//			}
//            /**
//             * 取消选择
//             */
//			function cancelHandler(){
//				var object = new Object();			
//				// 返回值
//				object.supplierId = "";
//				object.supplier = "";
//				object.supplyName = "";
//				object.companyTypeDesc = "";
//				object.principal = "";
//				object.registeredCapital = "";
//				object.companyTel = "";
//				window.returnValue = object;
//				// 关闭画面
//				window.close();
//			}
//			/**
//			 * 模糊查询
//			 */
//			function fuzzyQueryHandler() {
//				dsQuery.load({
//							params : {
//								supplyNameCode : txtQuerySupplyCode.getValue(),
//								start : 0,
//								limit : Constants.PAGE_SIZE
//							}
//						});
//			}
//		})

Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	
	var approveFlag = "2";
	
	var fuzzyText = new Ext.form.TextField({
		id : 'fuzzyText',
		name : 'fuzzyText',
		width : 180,
		emptyText : '合作伙伴编号/名称'
	})		

	var westbtnQuery = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : function() {
					westStore.load({
								params : {
									fuzzyText : fuzzyText.getValue(),
									start : 0,
									limit : 18,
									approveFlag : approveFlag
								}
							});
				}
			});

	var westSm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});

	var westStorelist = new Ext.data.Record.create([westSm, {
				name : 'cliendId'
			}, {
				name : 'typeId'
			}, {
				name : 'characterId'
			}, {
				name : 'importanceId'
			}, {
				name : 'tradeId'
			}, {
				name : 'clientCode'
			}, {
				name : 'clientName'
			}, {
				name : 'cliendChargeby'
			}, {
				name : 'clientOverview'
			}, {
				name : 'mainProducts'
			}, {
				name : 'mainPerformance'
			}, {
				name : 'address'
			}, {
				name : 'telephone'
			}, {
				name : 'zipcode'
			}, {
				name : 'email'
			}, {
				name : 'website'
			}, {
				name : 'fax'
			}, {
				name : 'legalRepresentative'
			}, {
				name : 'taxQualification'
			}, {
				name : 'bank'
			}, {
				name : 'account'
			}, {
				name : 'taxnumber'
			}, {
				name : 'regFunds'
			}, {
				name : 'regAddress'
			}, {
				name : 'memo'
			}, {
				name : 'approveFlag'
			}, {
				name : 'lastModifiedBy'
			}, {
				name : 'lastModifiedDate'
			}]);

	var westStore = new Ext.data.JsonStore({
				url : 'clients/getClientsInfoList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : westStorelist
			});

              westStore.load({
								params : {
									fuzzyText : "",
									approveFlag : approveFlag,
									start : 0,
									limit : 18
								}
							});

	// 左侧Grid
	var grid = new Ext.grid.GridPanel({
				store : westStore,
				columns : [new Ext.grid.RowNumberer(), {
							header : "合作伙伴编码",
							width : 90,
							sortable : false,
							dataIndex : 'clientCode',
							region : 'center'
						}, {
							header : "合作伙伴名称",
							width : 90,
							sortable : false,
							dataIndex : 'clientName',
							align : 'center'
						}, {
							header : "公司负责人",
							width : 80,
							sortable : false,
							dataIndex : 'cliendChargeby',
							align : 'center'
						}, {
							header : "状态",
							width : 80,
							sortable : false,
							dataIndex : 'approveFlag',
							align : 'center',
							renderer : function returnValue(val) {
								if(val == "1") {
									return '未启用';
								} else if(val == "2"){
									return '已启用';
								}
							}
						}],
				viewConfig : {
					forceFit : true
				},
				tbar : ['模糊查询 ：', fuzzyText, westbtnQuery,
				{ text:'确定',
				  iconCls:'ok',
				  handler:selectRecord
				},
				{
				 text:'取消',
				 iconCls:'cancer',
				 handler:cancelHandler
				}
				],
				sm : westSm,
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : westStore,
							displayInfo : true,
							displayMsg : "{2}条记录",
							emptyMsg : "没有记录"
						}),
				frame : true,
				enableColumnHide : false,
				enableColumnMove : false
			});
			
	grid.on("rowdblclick", selectRecord);	
	function selectRecord()
	{
		if (grid.selModel.hasSelection()) {
		
			var records = grid.getSelectionModel().getSelected();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项！");
			} else {
				 
				var record = grid.getSelectionModel().getSelected();
		         var client=new Object();
		         client.supplierId=record.get("cliendId");
		         client.supplyName=record.get("clientName");
		         client.supplier=record.get("clientCode");
		       //  client.companyTypeDesc=
		         //公司负责人
		         client.principal=record.get("cliendChargeby");
		         client.registeredCapital =record.get("regFunds");
		         client.companyTel=record.get("telephone");
		         window.returnValue=client;
		         window.close();

			}
		} else {
			Ext.Msg.alert("提示", "请先选择合作伙伴!");
		}
	}
	
				function cancelHandler(){
				var object = new Object();	
				object.supplierId = "";
				object.supplier = "";
				object.supplyName = "";
				object.companyTypeDesc = "";
				object.principal = "";
				object.registeredCapital = "";
				object.companyTel = "";
				window.returnValue = object;
				window.close();
			}
	
			

	new Ext.Viewport({
		enableTabScroll : true,
	//	layout : "border",
		layout : "fit",
		items : [grid]
	});

	
	
	
	
	
});