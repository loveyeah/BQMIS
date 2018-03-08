Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var contractId;
	var conIndext;
	function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";
		// 初始化时间
		Year = DateIn.getYear();
		Month = DateIn.getMonth() + 1;
		Day = DateIn.getDate();
		CurrentDate = Year + "-";
		if (Month >= 10) {
			CurrentDate = CurrentDate + Month + "-";
		} else {
			CurrentDate = CurrentDate + "0" + Month + "-";
		}
		if (Day >= 10) {
			CurrentDate = CurrentDate + Day;
		} else {
			CurrentDate = CurrentDate + "0" + Day;
		}
		return CurrentDate;
	};
	var date = new Date();
	var startdate = date.add(Date.DAY, -60);
	var enddate = date;
	var fuzzy = "";
	var sdate = ChangeDateToString(startdate);
	var edate = ChangeDateToString(enddate);
	var fromDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : 'startDate',
		id : 'fromDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		checked : true,
		value : sdate,
		emptyText : '请选择',
		width : 100
	});
	var toDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : 'endDate',
		value : edate,
		id : 'toDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		checked : true,
		emptyText : '请选择',
		width : 100
	});
	var fuzzytext = new Ext.form.TextField({
		id : 'fuzzy',
		name : 'fuzzy',
		// addBy Liuyingwen 09/04/23 
		width : 180,
		emptyText : "合同变更编号/合同名称/合作伙伴"
	});
	var contbar = new Ext.Toolbar({
		items : ['开始执行时间从：', fromDate, '至:', toDate, '-', fuzzytext, '-', {
			id : 'btnQuery',
			text : "查询",
			width : Ext.getBody().getWidth(),
			iconCls : 'query',
			handler : function() {
				con_ds.load({
					params : {
						conTypeId : 2,
						fuzzy : fuzzytext.getValue(),
						startdate : Ext.get('fromDate').dom.value,
						enddate : Ext.get('toDate').dom.value,
						start : 0,
						limit : 18
					}
				});
			}
		}, '-', {
			id : 'applyPayment',
			text : "申请付款",
			iconCls : 'add',
			handler : function() {
				var selrows = Grid.getSelectionModel().getSelections();
				var balrows = balanceGrid.getSelectionModel().getSelections()
				if (selrows.length > 0) {
					if (balrows.length > 0) {
						contractId = selrows[0].data.conId;
						balanceId = balrows[0].data.balanceId;
						balaFlag = balrows[0].data.balaFlag;
						currencyName = balrows[0].data.currencyName;
						balanceBy = balrows[0].data.balanceBy;
						balanceName = balrows[0].data.balanceName;
//						return;
						parent.edit(contractId, balanceId,balaFlag,currencyName,balanceBy,balanceName);
						// parent.Ext.getCmp("maintab").setActiveTab(1);
						// var url =
						// "manage/contract/business/contractBalance/register/conBalance/conBalance.jsp";
						// parent.document.all.iframe2.src = url + "?conId=" +
						// contractId+"&balanceId="+balanceId;
					} else {
						contractId = selrows[0].data.conId;
						contractId = selrows[0].data.conId;
						parent.edit(contractId, "", "","","","");
						// parent.Ext.getCmp("maintab").setActiveTab(1);
						// var url =
						// "manage/contract/business/contractBalance/register/conBalance/conBalance.jsp?conId="+
						// contractId;
						// parent.document.all.iframe2.src = url;
					}
				} else {
					Ext.Msg.alert('提示', '请选择要申请付款的合同！');
				}
			}
		}, '-', {
			id : 'contractInfo',
			text : "合同信息",
			 iconCls : 'list',
			handler : function() {
				
				var selrows = Grid.getSelectionModel().getSelections();
				if (selrows.length > 0) {
					var record = Grid.getSelectionModel().getSelected();
					var url;
					var conId = record.data.conId;

					if ((conId != null) && (conId != "")) {
						url = "/power/manage/projectFact/business/conBaseInfo/conBaseInfo.jsp?id="
								+ conId;
					}
					var o = window.showModalDialog(url, '',
							'dialogWidth=700px;dialogHeight=600px;status=no');
				} else {
					Ext.Msg.alert('提示', '请从列表中选择一条记录!');
				}
			
			}
//				var record = Grid.getSelectionModel().getSelected();
//				if (record == null) {
//					Ext.Msg.alert('提示信息', '请选择合同列表中的行！');
//				} else {
//					var url = "contractInfo.jsp?conId=" + record.data.conId;
//					// window.showModalDialog(url,"dialogWidth=200px;dialogHeight=100px");
//					window.open(url);
//				}
//			}
		}]
	});
	var con_item = Ext.data.Record.create([{
		name : 'conId'
	}, {
		name : 'workflowStatus'
	}, {
		name : 'conttreesNo'
	}, {
		name : 'contractName'
	}, {
		name : 'clientName'
	}, {
		name : 'actAmount'
	}, {
		name : 'currencyType'
	}, {
		name : 'operateName'
	}, {
		name : 'operateDeptName'
	}, {
		name : 'startDate'
	}, {
		name : 'endDate'
	}, {
		name : 'workflowNo'
	},{
		name : 'currencyName'
	}]);
	var con_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			rowselect : function(sm, row, rec) {
				conIndext = row;
			}
		}
	});
	var con_item_cm = new Ext.grid.ColumnModel([con_sm,
			new Ext.grid.RowNumberer({
				header : '序号',
				width : 50,
				align : 'center'
			}), {
				header : '合同编号',
				dataIndex : 'conttreesNo',
				align : 'center'
			}, {
				header : '合同名称',
				dataIndex : 'contractName',
				align : 'center'
			}, {
				header : '合作伙伴',
				dataIndex : 'clientName',
				align : 'center'
			}, {
				header : '金额',
				dataIndex : 'actAmount',
				align : 'center'
			}, {
				header : '币别',
				dataIndex : 'currencyName',
				align : 'center'
//				,
//				renderer : function(v) {
//
//					if (v == 1) {
//						return "RMB";
//					}
//					if (v == 2) {
//						return "USD";
//					} else {
//						return "异常";
//					}
//
//				}
			}, {
				header : '经办人',
				dataIndex : 'operateName',
				align : 'center'
			}, {
				header : '经办部门',
				dataIndex : 'operateDeptName',
				align : 'center'
			}, {
				header : '履行开始时间',
				dataIndex : 'startDate',
				width : 200,
				align : 'center'
			}, {
				header : '履行结束时间',
				dataIndex : 'endDate',
				align : 'center'
			}]);
	con_item_cm.defaultSortable = true;
	var con_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managecontract/findAppConList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : 'root'
		}, con_item)
	});
	con_ds.load({
		params : {
			conTypeId : 2,
			startdate : sdate,
			enddate : edate,
			fuzzy : fuzzy,
			start : 0,
			limit : 18
		}
	});
	var gridbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : con_ds,
		// width : Ext.getBody().getWidth(),
		displayInfo : true,
		displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
		emptyMsg : "没有记录",
		beforePageText : '页',
		afterPageText : "共{0}"
	});
	var Grid = new Ext.grid.GridPanel({
		ds : con_ds,
		cm : con_item_cm,
		sm : con_sm,
		height : 300,
		split : true,
		// autoHeight : true,
		// autoWidth : true,
		autoScroll : true,
		width : Ext.getBody().getWidth(),
		bbar : gridbbar,
		tbar : contbar,
		border : false,
		viewConfig : {
			forceFit : false
		}
			// ,
			// listeners : {
			// render : function(g) {
			// g.getSelectionModel().selectRow(0);
			// },
			// delay : 10
			// }
	});
	Grid.on('rowclick', function(grid, rowIndex, e) {
		var record = Grid.getSelectionModel().getSelected();
		if (record != null) {
			if (record.data.workflowStatus == '2' || record.data.workflowStatus == '4') {
				contractId = record.data.conId;
			} else {
				Ext.Msg.alert('提示信息', '请选择执行中的合同')
				return false;
			}
		}
		balance_ds.load({
			params : {
				conId : contractId
			}
		})
	});
	var balance_item = new Ext.data.Record.create([{
		name : 'balanceId'
	}, {
		name : 'conId'
	}, {
		name : 'balaFlag'
	}, {
		name : 'contractName'
	}, {
		name : 'clientName'
	}, {
		name : 'actAccount'
	}, {
		name : 'itemName'
	}, {
		name : 'applicatPrice'
	}, {
		name : 'applicatDate'
	}, {
		name : 'operateName'
	}, {
		name : 'passPrice'
	}, {
		name : 'passDate'
	}, {
		name : 'balancePrice'
	}, {
		name : 'balaDate'
	}, {
		name : 'balanceName'
	},{
		name : 'currencyName'
	},{
		name : 'balanceBy'
	}])
	var balance_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
		,
		listeners : {
			rowselect : function(sm, row, rec) {
				con_sm.selectRow(conIndext);
				parent.toFuz(contractId, rec.data.balanceId,rec.data.balaFlag);
			}
		}
	})
	var balance_item_cm = new Ext.grid.ColumnModel([balance_sm,
			new Ext.grid.RowNumberer({
				header : '序号',
				width : 50,
				align : 'center'
			}), {
				header : '结算状态',
				dataIndex : 'balaFlag',
				align : 'center',
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
				header : '合同名称',
				dataIndex : 'contractName',
				align : 'center'
			}, {
				header : '合作伙伴',
				dataIndex : 'clientName',
				align : 'center'
			}, {
				header : '合同总额',
				dataIndex : 'actAccount',
				align : 'center'
			}, {
				header : '费用来源',
				dataIndex : 'itemName',
				hidden : true,
				align : 'center'
			}, {
				header : '申请付款',
				dataIndex : 'applicatPrice',
				align : 'center'
			}, {
				header : '申请日期',
				dataIndex : 'applicatDate',
				align : 'center'
			}, {
				header : '经办人',
				dataIndex : 'operateName',
				align : 'center'
			}, {
				header : '批准付款',
				dataIndex : 'passPrice',
				align : 'center'
			}, {
				header : '批准日期',
				dataIndex : 'applicatDate',
				align : 'center'
			}, {
				header : '财务付款',
				dataIndex : 'balancePrice',
				hidden : true,
				align : 'center'
			}, {
				header : '财务付款日期',
				hidden : true,
				dataIndex : 'balaDate',
				align : 'center'
			}, {
				header : '财务部经办人',
				hidden : true,
				dataIndex : 'balanceName',
				align : 'center'
			}]);
	balance_item_cm.defaultSortable = true;
	var balance_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managecontract/findBalanceList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'balanceTotal',
			root : 'balanceRoot'
		}, balance_item)
	});
	// balance_ds.load({
	// params : {
	// conId : contractId
	// }
	// });
	// if(contractId!=""){
	// alert();
	// parent.Ext.getCmp("maintab").setDeactiveTab(1);
	// // parent.Ext.getCmp("maintab").setDeactiveTab(2);
	// // parent.Ext.getCmp("maintab").setDeactiveTab(3);
	// }
	var blancebar  = new Ext.Toolbar({
	items : [{
			id : 'blancebar',
//			text : "查询",
			hidden : true,
			width : Ext.getBody().getWidth(),
			iconCls : 'query',
			handler : function() {
				balance_ds.load({
			params : {
				conId : 0
			}
		})
			}}]
	})
	var balanceGrid = new Ext.grid.GridPanel({
		ds : balance_ds,
		cm : balance_item_cm,
		sm : balance_sm,
		split : true,
		// autoHeigth : true,
		// height : 300,
		// autoWidth : true,
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
			title : "执行中的合同列表",
			region : 'center',
			layout : 'fit',
			border : false,
			margins : '0 0 0 0',
			split : true,
			collapsible : false,
			// autoWidth : true,
			items : [Grid]

		}, {
			title : "合同结算列表",
			region : "south",
			layout : 'fit',
			border : false,
			margins : '0 0 0 0',
			height : 300,
			// autoHeigth : true,
			// autoWidth : true,
			split : true,
			collapsible : true,
			items : [balanceGrid]
		}

		]
	});
		// var layout = new Ext.Panel({
		// autoWidth : true,
		// autoHeight : true,
		// border : false,
		// autoScroll : true,
		// split : true,
		// items : [Grid, balanceGrid]
		// });
		// layout.render(Ext.getBody());
	})