Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var btnQuery = new Ext.Toolbar.Button({
				id : "btnQuery",
				text : "查询",
				iconCls : 'query',
				handler : queryRecord
			})
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
				name : 'conId'
			}, {
				name : 'conModifyId'
			}, {
				name : 'conttreesNo'
			}, {
				name : 'contractName'
			}, {
				name : 'clientName'
			}, {
				name : 'isSum'
			}, {
				name : 'actAmount'
			}, {
				name : 'currencyType'
			}, {
				name : 'payedAmount'
			}, {
				name : 'unliquidate'
			}, {
				name : 'operateName'
			}, {
				name : 'operateDeptName'
			}, {
				name : 'endDate'
			}, {
				name : 'itemId'
			}, {
				name : 'currencyName'
			}]);
	var dataProxy = new Ext.data.HttpProxy({
				url : 'managecontract/findContractTerminateList.action'
			});
	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, MyRecord);
	var store = new Ext.data.Store({
				proxy : dataProxy,
				reader : theReader
			});
	// 分页
	var fuzzy = new Ext.form.TextField({
				id : "fuzzy",
				name : "fuzzy"
			});
	var sm = new Ext.grid.CheckboxSelectionModel();
	var grid = new Ext.grid.GridPanel({
				region : "center",
				store : store,
				columns : [sm, new Ext.grid.RowNumberer(), {
							header : "ID",
							width : 100,
							sortable : true,
							dataIndex : 'conId',
							hidden : true
						}, {
							header : "变更ID",
							width : 120,
							sortable : true,
							hidden : true,
							dataIndex : 'conModifyId',
							align : 'center'
						}, {
							header : "合同编号",
							width : 140,
							sortable : true,
							dataIndex : 'conttreesNo',
							align : 'center'
						}, {
							header : "合同名称",
							sortable : true,
							dataIndex : 'contractName',
							align : 'center',
							width : 200,
							renderer : function change(val) {
								return ' <span style="white-space:normal;">'
										+ val + ' </span>';
							}
						}, {
							header : "供应商",
							width : 100,
							sortable : true,
							dataIndex : 'clientName',
							align : 'center'
						}, {
							header : "有总金额",
							width : 100,
							sortable : true,
							dataIndex : 'isSum',
							renderer : function changeIt(val) {
								if (val == "Y")
									return "是";
								if (val == "N")
									return "否";
							},
							align : 'center'
						}, {
							header : "金额",
							width : 100,
							sortable : true,
							dataIndex : 'actAmount',
							align : 'center'
						}, {
							header : "币别",
							width : 100,
							sortable : true,
							dataIndex : 'currencyName',
							align : 'center'
						}, {
							header : "已结算金额",
							width : 100,
							sortable : true,
							hidden : true,
							dataIndex : 'payedAmount',
							align : 'center'
						}, {
							header : "未结算金额",
							width : 100,
							sortable : true,
							hidden : true,
							dataIndex : 'unliquidate',
							align : 'center'
						}, {
							header : "经办人",
							width : 100,
							sortable : true,
							dataIndex : 'operateName',
							align : 'center'
						}, {
							header : "经办部门",
							width : 100,
							sortable : true,
							dataIndex : 'operateDeptName',
							align : 'center'
						}, {
							header : "履行结束时间",
							width : 100,
							sortable : true,
							dataIndex : 'endDate',
							align : 'center'
						}, {
							header : "费用来源",
							width : 100,
							sortable : true,
							dataIndex : 'itemId',
							hidden : true,
							align : 'center'
						}],
				sm : sm,
				tbar : ['<font size = "2">按合同编号、合同名称、供应商名模糊查询：</font>', fuzzy,
						btnQuery, '-', {
							text : "浏览合同",
							iconCls : 'list',
							handler : updateRecord
						}],
				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : store,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						})
			});

	// 双击事件
	grid.on('rowdblclick', updateRecord);
	function updateRecord() {

		if (grid.selModel.hasSelection()) {

			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项记录！");
			} else {
				var record = grid.getSelectionModel().getSelected();
				id = record.data.conId;
				var contractNo = record.data.conttreesNo;
				unliquidate = record.data.unliquidate == null
						? ""
						: record.data.unliquidate;
				conModifyId = record.data.conModifyId == null
						? ""
						: record.data.conModifyId;
				var currencyType = record.data.currencyType;
				var actAmount = record.data.actAmount;
				parent.Ext.getCmp("maintab").setActiveTab(1);
				var url = "manage/contract/business/contractTerminate/register/terminateBase/terbase.jsp";
				parent.document.all.iframe2.src = url + "?id=" + id
						+ "&unliquidate=" + unliquidate + "&conModifyId="
						+ conModifyId + "&contractNo=" + contractNo;
			}
		} else {
			Ext.Msg.alert("提示", "请先选择记录!");
		}
	}

	function queryRecord() {
		store.load({
					params : {
						start : 0,
						limit : 18,
						fuzzy : fuzzy.getValue(),
						conTypeId : 1
					}
				});
	}
	// ---------------------------------------

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "fit",
				items : [grid]
			});

	queryRecord();

});