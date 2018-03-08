Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/s.gif';

Ext.onReady(function(){
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'qtip';
	// 标记被单击的申请id
	var tempId = null;
	
	
	/**
	 * 金钱格式化
	 */
	function moneyFormat(v) {
		v = (Math.round((v - 0) * 100)) / 100;
		v = (v == Math.floor(v))
				? v + ".0000"
				: ((v * 10 == Math.floor(v * 10))
						? v + "000"
						: ((v * 100 == Math.floor(v * 100)) ? v + "00" : ((v
								* 1000 == Math.floor(v * 1000)) ? v + "0" : v)));
		v = String(v);
		var ps = v.split('.');
		var whole = ps[0];
		var sub = ps[1] ? '.' + ps[1] : '.0000';
		var r = /(\d+)(\d{3})/;
		while (r.test(whole)) {
			whole = whole.replace(r, '$1' + ',' + '$2');
		}
		v = whole + sub;
		if (v.charAt(0) == '-') {
			return '-' + v.substr(1);
		}
		return v;
	}
	//主表头部数据对象
	var mainRec = new Ext.data.Record.create([
		{// 测试用 后面需删除
		name : 'conId'
	},
//	{
//		name : 'workflowStatus'
//	}, {
//		name : 'conttreesNo'
//	}, {
//		name : 'contractName'
//	}, {
//		name : 'clientName'
//	}, {
//		name : 'actAmount'
//	}, {
//		name : 'currencyType'
//	}, {
//		name : 'operateName'
//	}, {
//		name : 'operateDeptName'
//	}, {
//		name : 'startDate'
//	}, {
//		name : 'endDate'
//	}, {
//		name : 'workflowNo'
//	},{
//		name : 'currencyName'
//	}
		{
		name : 'workflowStatus'
	}
	// 数据添加开始
	,{
		// 申请id
		name : 'appId'
	},{
		// 制表人名称
		name : 'entryByName'
	},{
		// 上报部门名称
		name : 'operateDepName'
	},{
		// 制表时间
		name : 'entryDate'
	},{
		// 申请付款时间按
		name : 'applicatDate'
	},{
		// 金额单位
		name : 'moneyUnit'
	}
	// 数据添加结束
	]);
	// 头部主表行选择模式
	var mainsm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	})
	// 头部主表列模式
	var maincm = new Ext.grid.ColumnModel([mainsm,
		new Ext.grid.RowNumberer({
			header : '行号',
			width : 35,
			align : 'center'
		}),{
				header : '制表人',
				dataIndex : 'entryByName',
				align : 'center'
			}, {
				header : '制表时间',
				dataIndex : 'entryDate',
				width : 200,
				align : 'center'
			}, {
				header : '上报部门',
				dataIndex : 'operateDepName',
				align : 'center'
			}, {
				header : '金额单位',
				dataIndex : 'moneyUnit',
				align : 'center',
				renderer : function(v) {

					if (v == 1) {
						return "元";
					}
					if (v == 2) {
						return "万元";
					} else {
						return "";
					}

				}
			}, {
				header : '申请付款时间',
				dataIndex : 'applicatDate',
				align : 'center'
			}
		])
		
		// 头部列表stroe
		var mainstore = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
				url : 'managecontract/bqfindAppConList.action'
			}),
			reader : new Ext.data.JsonReader({
				totalProperty : 'totalCount',
				root : 'list'
			},mainRec)
		});
		mainstore.baseParams = {
			approved : 'N'
		}
		mainstore.load({
			params : {
				start : 0,
				limit : 18
			}	
		});
		// 头部列表分页栏
		var mainpage = new Ext.PagingToolbar({
						pageSize : 18,
						store : mainstore,
						displayInfo : true,
						displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
						emptyMsg : "没有记录",
						beforePageText : '页',
						afterPageText : "共{0}"
					})
		// 头部工具栏
		var contbar = new Ext.Toolbar({
			items : ['']
		})
		// 头部列表
		var maingrid = new Ext.grid.GridPanel({
						ds : mainstore,
						cm : maincm,
						sm : mainsm,
						height : 300,
						split : true,
						autoScroll : true,
						width : Ext.getBody().getWidth(),
						bbar : mainpage,
						tbar : contbar,
						border : false,
						viewConfig : {
							forceFit : false
						}
					})
					
		maingrid.on('rowclick', function(grid, rowIndex, e) {
						var record = maingrid.getSelectionModel().getSelected();
						if (record != null) {
								tempId = record.data.appId;
						}
						detailstore.load({
									params : {
										appId : tempId
									}
								})
	});
	// 双击事件 ，跳入信息页面
	maingrid.on('rowdblclick',function(){
		var record = maingrid.getSelectionModel().getSelected();
						if (record != null) {
							parent.flagId = record.get('appId')
//							alert(parent.flagId)
							parent.Ext.getCmp("maintab").setActiveTab(1);
							var url = "manage/contract/business/contractBalance/contractPayment/paymentTab/paymentInfo/paymentInfo.jsp";
							parent.document.all.iframe2.src = url;
							
						}
	})
	// 明细列表中的数据对象
	var detailRec = new Ext.data.Record.create([
//		{
//						name : 'balanceId'
//					}, {
//						name : 'conId'
//					}, {
//						name : 'balaFlag'
//					}, {
//						name : 'contractName'
//					}, {
//						name : 'clientName'
//					}, {
//						name : 'actAccount'
//					}, {
//						name : 'itemName'
//					}, {
//						name : 'applicatPrice'
//					}, {
//						name : 'applicatDate'
//					}, {
//						name : 'operateName'
//					}, {
//						name : 'passPrice'
//					}, {
//						name : 'passDate'
//					}, {
//						name : 'balancePrice'
//					}, {
//						name : 'balaDate'
//					}, {
//						name : 'balanceName'
//					}, {
//						name : 'currencyName'
//					}, {
//						name : 'balanceBy'
//					},
					 {
					 	// 结算状态
						name : 'balaFlag'
					}, 
					// 添加数据开始
					{
						//结算id
						name : 'balanceId'
					},{
						//合同id
						name : 'conId'
					},{
						//合同编号
						name : 'contractNo'
					},{
						//收款单位
						name : 'clientName'
					},{
						//合同名称
						name : 'contractName'
					},{
						//合同总金额
						name : 'actAccount'
					},{
						//已付款金额
						name : 'payedAccount'
					},{
						//本次申请付款金额
						name : 'applicatPrice'
					},{
						//本次批准付款金额
						name : 'passPrice'
					},{
						//余额金额
						name : 'balance'
					},{
						//备注
						name : 'memo'
					},{
						// 申请id
						name : 'appId'
					}
					// 添加数据结束
					])
		// 明细列表中的选择模式
		var detailsm = new Ext.grid.CheckboxSelectionModel({
			singleSelect : true
				})
		// 明细列表中的列模式
		var detailcm = new Ext.grid.ColumnModel([detailsm,
			new Ext.grid.RowNumberer({
				header : '序号',
				width : 35,
				align : 'center'
			}), {
				header : '结算状态',
				dataIndex : 'balaFlag',
				align : 'center',
				hidden : true,
				renderer : function(v) {
					var f;
					if (v == "0")
						f = "未结算"
					if ("1" == v)
						f = "结算中"
					if ("2" == v)
						f = "已结算"
					if ("3" == v)
						f = "退回"
					return f;
				}
			}, {
				header : '合同编号',
				dataIndex : 'contractNo',
				align : 'center'
			}, {
				header : '收款单位',
				dataIndex : 'clientName',
				align : 'center'
			},{
				header : '合同名称',
				dataIndex : 'contractName',
				align : 'center'
			}, {
				header : '合同总总额',
				dataIndex : 'actAccount',
				align : 'center',
				renderer : moneyFormat
			}, {
				header : '已付款金额',
				dataIndex : 'payedAccount',
				align : 'center',
				renderer : moneyFormat
			}, {
				header : '本次申请付款金额',
				dataIndex : 'applicatPrice',
				align : 'center',
				renderer : moneyFormat
			}, {
				header : '本次批准付款金额',
				dataIndex : 'passPrice',
				align : 'center',
				renderer : moneyFormat
			}, {
				header : '余额金额',
				dataIndex : 'balance',
				align : 'center',
				renderer : moneyFormat
			}, {
				header : '备注',
				dataIndex : 'memo',
				align : 'center'
			}]);
	// 明细列表中的store
		var detailstore = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : 'managecontract/bqfindBalanceList.action'
								}),
						reader : new Ext.data.JsonReader({
									totalProperty : 'totalCount',
									root : 'list'
								}, detailRec)
					});
	
	var blancebar = new Ext.Toolbar({
						items : [{
									id : 'blancebar',
									// text : "查询",
									hidden : true,
									width : Ext.getBody().getWidth(),
									iconCls : 'query',
									handler : function() {
										detailstore.load({
													params : {
														conId : 0
													}
												})
									}
								}]
					})
	var detailgrid = new Ext.grid.GridPanel({
		ds : detailstore,
		cm : detailcm,
		sm : detailsm,
		split : true,
		width : Ext.getBody().getWidth(),
		height : 300,
		autoScroll : true,
		border : false,
		tbar :blancebar,
		viewConfig : {
			forceFit : false
		}
	});
	new Ext.Viewport({
						enableTabScroll : true,
						layout : "border",
						items : [{
									 title : "申请列表",
									region : 'center',
									layout : 'fit',
									border : false,
									margins : '0 0 0 0',
									split : true,
									collapsible : false,
									items : [maingrid]

								}, {
									title : "合同列表",
									region : "south",
									layout : 'fit',
									border : false,
									margins : '0 0 0 0',
									height : 300,
									split : true,
									collapsible : true,
									items : [detailgrid]
								}

						]
					});
})