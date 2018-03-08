// 劳务派遣查询
// author:lichensheng
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {

	// 主画面--------------------------------
	// 定义签字日期start
	var startDate = new Ext.form.TextField({
				id : 'startDate',
				name : 'startDate',
				style : 'cursor:pointer',
				width : 80,
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
									isShowClear : true,
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : false
								});
					}
				}
			});
	// 定义签字日期end
	var endDate = new Ext.form.TextField({
				id : 'endDate',
				name : 'endDate',
				style : 'cursor:pointer',
				width : 80,
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
									isShowClear : true,
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : false
								});
					}
				}
			});
	// 协作单位
	var drpCooperateUnit = new Ext.form.CmbHRBussiness({
				id : 'drpCooperateUnit',
				type : '协作单位',
				width : 125
			});
	// 单据状态
	var drpDcmStatus = new Ext.form.CmbHRCode({
				id : 'drpDcmStatus',
				type : '单据状态',
				width : 80
			});
	// 查询按钮
	var btnQuery = new Ext.Button({
				text : Constants.BTN_QUERY,
				iconCls : Constants.CLS_QUERY,
				handler : queryLabourHandler
			});

	// 调动类型
	var transferType = new Ext.form.ComboBox({
				width : 85,
				forceSelection : true,
				triggerAction : 'all',
				mode : 'local',
				readOnly : true,
				displayField : 'transferName',
				valueField : 'transferType',
				store : [['', ''], ['1', '外借'], ['2', '外调']]
			});

	// 面板上部
	var headTbar = new Ext.Toolbar({
				region : 'north',
				border : false,
				height : 25,
				items : ['签字日期: ', startDate, '~', endDate, '-', '协作单位: ',
						drpCooperateUnit, '-', '调动类型', transferType,
						// '单据状态: ', drpDcmStatus,
						// '-',
						btnQuery]
			});

	var gridListLabour = new Ext.data.Record.create([{
				name : 'borrowcontractid'
			}, {
				name : 'workContractNo'
			}, {
				name : 'signatureDate'
			}, {
				name : 'cooperateUnitId'
			}, {
				name : 'cooperateUnit'
			}, {
				name : 'startDate'
			}, {
				name : 'endDate'
			}, {
				name : 'contractContent'
			}, {
				name : 'fileCount'
			}, {
				name : 'dcmStatus'
			}, {
				name : 'note'
			}, {
				name : 'transferType'
			}]);
	// 定义获取数据源
	var storeLabour = new Ext.data.JsonStore({
				url : 'hr/getLabourBy.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : gridListLabour,
				listeners : {
					loadexception : function(ds, records, o) {
						var o = eval("(" + o.responseText + ")");
						var succ = o.msg;
						if (succ == Constants.SQL_FAILURE) {
							Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
						} else if (succ == Constants.DATE_FAILURE) {
							Ext.Msg.alert(Constants.ERROR, Constants.COM_E_023);
						}
					}

				}
			});
	// 排序
	storeLabour.setDefaultSort('borrowcontractid', 'ASC');
	// 分页工具栏
	var pagebar = new Ext.PagingToolbar({
				pageSize : Constants.PAGE_SIZE,
				store : storeLabour,
				displayInfo : true,
				displayMsg : Constants.DISPLAY_MSG,
				emptyMsg : Constants.EMPTY_MSG
			});
	// 合同一览中的Grid主体
	var gridLabour = new Ext.grid.GridPanel({
				store : storeLabour,
				columns : [new Ext.grid.RowNumberer({
									header : '行号',
									width : 35
								}), {
							header : "劳务派遣合同ID",
							width : 80,
							sortable : true,
							hidden : true,
							dataIndex : 'borrowcontractid'
						}, {
							header : "合同编码",
							width : 80,
							sortable : true,
							dataIndex : 'workContractNo'
						}, {
							header : "签字日期",
							width : 100,
							sortable : true,
							dataIndex : 'signatureDate'
						}, {
							header : "协作单位",
							width : 100,
							sortable : true,
							dataIndex : 'cooperateUnit'
						}, {
							header : "开始日期",
							width : 100,
							sortable : true,
							dataIndex : 'startDate'
						}, {
							header : "结束日期",
							width : 100,
							sortable : true,
							dataIndex : 'endDate'
						}, {
							header : "劳务内容",
							width : 200,
							sortable : true,
							dataIndex : 'contractContent'
						}, {
							header : "合同附件",
							width : 100,
							sortable : true,
							dataIndex : 'fileCount',
							renderer : fileCount
						}
						// , {
						// header : "单据状态",
						// width : 60,
						// sortable : true,
						// dataIndex : 'dcmStatus',
						// renderer : function(value) {
						// return getCodeName("单据状态", value);
						// }
						// }
						, {
							header : "备注",
							width : 100,
							sortable : true,
							dataIndex : 'note'
						}, {
							header : '调动类型',
							width : 100,
							sortable : true,
							dataIndex : 'transferType',
							renderer : function(v) {
								if (v == "1")
									return "外借"
								if (v == "2")
									return "外调"
							}
						}],
				viewConfig : {
					forceFit : false
				},
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : true
						}),
				bbar : pagebar,
				enableColumnHide : false,
				enableColumnMove : false
			});

	// Center 的事件
	gridLabour.on('rowclick', gridLabourSet);
	function gridLabourSet(grid, rowIndex, e) {
		if (event.ctrlKey) {
			if (gridLabour.getSelectionModel().hasSelection()) {
				storeEmp.removeAll();
				storeEmp.load({
							params : {
								lngBorrowcontractid : gridLabour.getStore()
										.getAt(rowIndex)
										.get('borrowcontractid')
							}
						});
			} else {
				storeEmp.removeAll();
			}
		} else {
			storeEmp.load({
						params : {
							lngBorrowcontractid : gridLabour.getStore()
									.getAt(rowIndex).get('borrowcontractid')
						}
					});
		}
	}
	// 详细信息录入窗口-------------------------------------
	// 注册grid的单元格双击事件
	gridLabour.on("celldblclick", cellDbClick);
	// 显示单元格内容
	var txtArea = new Ext.form.TextArea({
				disabled : true
			});
	// 显示窗口
	var winMemo = new Ext.Window({
				title : "详细信息查看窗口",
				buttonAlign : "center",
				modal : true,
				height : 170,
				width : 350,
				layout : "fit",
				closeAction : "hide",
				resizable : false,
				items : [txtArea],
				buttons : [{
							text : Constants.BTN_CLOSE,
							iconCls : Constants.CLS_CANCEL,
							handler : function closeWin() {
								winMemo.hide();
							}
						}]
			});

	// 单元格双击处理函数
	function cellDbClick(grid, rowIndex, columnIndex, e) {
		if (grid == gridLabour) {
			if (columnIndex == 10) {
				txtArea.setValue(storeLabour.getAt(rowIndex).get("note"));
				winMemo.x = undefined;
				winMemo.y = undefined;
				winMemo.show();
			}
		}
		if (grid == gridEmp) {
			if (columnIndex == 10) {
				txtArea.setValue(storeEmp.getAt(rowIndex).get("memo"));
				winMemo.x = undefined;
				winMemo.y = undefined;
				winMemo.show();
			}
		}
	}
	var gridListEmp = new Ext.data.Record.create([{
				name : 'employeeborrowid'
			}, {
				name : 'empCode'
			}, {
				name : 'chsName'
			}, {
				name : 'deptName'
			}, {
				name : 'stationName'
			}, {
				name : 'startDate'
			}, {
				name : 'endDate'
			}, {
				name : 'stopPayDate'
			}, {
				name : 'startPayDate'
			}, {
				name : 'ifBack'
			}, {
				name : 'memo'
			}]);

	// 定义获取数据源
	var storeEmp = new Ext.data.JsonStore({
				url : 'hr/getEmpBy.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : gridListEmp,
				listeners : {
					loadexception : function(ds, records, o) {
						var o = eval("(" + o.responseText + ")");
						var succ = o.msg;
						if (succ == Constants.SQL_FAILURE) {
							Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
						} else if (succ == Constants.DATE_FAILURE) {
							Ext.Msg.alert(Constants.ERROR, Constants.COM_E_023);
						}
					}

				}
			});
	// 排序
	storeEmp.setDefaultSort('employeeborrowid', 'ASC');
	// 员工一览中的Grid主体
	var gridEmp = new Ext.grid.GridPanel({
				store : storeEmp,
				columns : [new Ext.grid.RowNumberer({
									header : '行号',
									width : 35
								}), {
							header : "员工工号",
							width : 80,
							sortable : true,
							dataIndex : 'empCode'
						}, {
							header : "员工姓名",
							width : 100,
							sortable : true,
							dataIndex : 'chsName'
						}, {
							header : "所属部门",
							width : 100,
							sortable : true,
							dataIndex : 'deptName'
						}, {
							header : "所在岗位",
							width : 100,
							sortable : true,
							dataIndex : 'stationName'
						}, {
							header : "开始日期",
							width : 100,
							sortable : true,
							dataIndex : 'startDate'
						}, {
							header : "结束日期",
							width : 100,
							sortable : true,
							dataIndex : 'endDate'
						}, {
							header : "停薪日期",
							width : 100,
							sortable : true,
							dataIndex : 'stopPayDate'
						}, {
							header : "起薪日期",
							width : 100,
							sortable : true,
							dataIndex : 'startPayDate'
						}, {
							header : "是否已回",
							width : 60,
							sortable : true,
							dataIndex : 'ifBack',
							renderer : function(value) {
								return getCodeName('是否已回', value);
							}
						}, {
							header : "备注",
							width : 100,
							sortable : true,
							dataIndex : 'memo'
						}],
				viewConfig : {
					forceFit : false
				},
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : true
						}),
				enableColumnHide : false,
				enableColumnMove : false
			});
	// 注册grid的单元格双击事件
	gridEmp.on("celldblclick", cellDbClick);
	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							region : 'north',
							layout : 'fit',
							border : false,
							margins : '0 0 0 0',
							height : 25,
							items : [headTbar]
						}, {
							title : "合同一览",
							region : 'center',
							layout : 'fit',
							border : false,
							margins : '0 0 0 0',
							items : [gridLabour]

						}, {
							title : "人员一览",
							region : "south",
							layout : 'fit',
							border : false,
							margins : '0 0 0 0',
							height : 150,
							items : [gridEmp]
						}]
			});
	// 主画面------------------------------------------------------------------------

	// 函数处理----------------------------------------------------------------------
	// 查询合同一览操作
	function queryLabourHandler() {
		storeEmp.removeAll();
		storeLabour.baseParams = {
			strStartDate : startDate.getValue(),
			strEndDate : endDate.getValue(),
			strCooperateUnit : drpCooperateUnit.getValue(),
			strDcmStatus : drpDcmStatus.getValue(),
			strTransferType : transferType.getValue()
		};
		storeLabour.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});
	}
	// 没有检索到数据弹message
	storeLabour.on("load", function() {
				// 没有检索到任何信息
				if (storeLabour.getCount() <= 0) {
					Ext.Msg.alert(Constants.REMIND, Constants.COM_I_003);
				}
			});

	// 显示合同附件窗口
	showWindow = function(borrowcontractid) {
		var dialogArguments = {
			mode : 'read',
			contractId : borrowcontractid,
			fileOriger : 4
		}
		var obj = window.showModalDialog('../../../hr/common/PC002.jsp',
				dialogArguments,
				'dialogWidth=500px;dialogHeight=320px;status=no');
	}

	// 查看附件
	function fileCount(value, cellmeta, record) {
		if (value == '0') {
			return "—";
		} else {
			var borrowcontractid = record.get("borrowcontractid");
			var showWindow = 'showWindow("' + borrowcontractid
					+ '");return false;';
			return "<a href='#' onclick='" + showWindow + "'>查看附件</a>";
		}
	}

})