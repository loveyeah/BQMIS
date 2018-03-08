Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

			// 定义grid
			var MyRecord = Ext.data.Record.create([{
						name : 'conId'
					}, {
						name : 'conttreesNo'
					}, {
						name : 'contractName'
					}, {
						name : 'cliendId'
					}, {
						name : 'clientName'
					}, {
						name : 'actAmount'
					}, {
						name : 'payedAmount'
					}]);

			var dataProxy = new Ext.data.HttpProxy(

			{
						url : 'managecontract/bqContractSelect.action'
					}

			);
			var theReader = new Ext.data.JsonReader({
						root : "list",
						totalProperty : "totalCount"

					}, MyRecord);

			var store = new Ext.data.Store({

						proxy : dataProxy,

						reader : theReader

					});
			// 分页
			store.load({
						params : {
							// conTypeId : 2,
							start : 0,
							limit : 18
						}
					});

			var fuzzy = new Ext.form.TextField({
						id : "fuzzy",
						name : "fuzzy"
					});
			var sm = new Ext.grid.CheckboxSelectionModel();

			var grid = new Ext.grid.GridPanel({
						region : "center",
						store : store,
						columns : [sm, new Ext.grid.RowNumberer({
											header : '行号',
											width : 35,
											align : 'center'
										}), {
									header : "合同ID",
									width : 100,
									sortable : true,
									dataIndex : 'conId',
									hidden : true
								}, {

									header : "合同编号",
									width : 100,
									sortable : true,
									dataIndex : 'conttreesNo'
								},

								{
									header : "合同名称",
									width : 120,
									sortable : true,
									dataIndex : 'contractName',
									align : 'center'
								}, {
									header : "供应商Id",
									width : 100,
									sortable : true,
									hidden : true,
									dataIndex : 'cliendId',
									align : 'center'
								}, {
									header : "供应商",
									width : 100,
									sortable : true,
									dataIndex : 'clientName',
									align : 'center'
								},

								{
									header : "总金额",
									width : 100,
									sortable : true,
									dataIndex : 'actAmount',
									align : 'center'
								}, {
									header : "已付金额",
									width : 100,
									sortable : true,
									dataIndex : 'payedAmount',
									align : 'center'
								}],
						sm : sm,
						tbar : [fuzzy, {
									text : "查询",
									iconCls : 'add',
									handler : queryRecord
								}, {
									text : "确定",
									iconCls : 'update',
									handler : getRecord
								}, {
									text : "关闭",
									iconCls : 'delete',
									handler : function() {
										window.close();
									}
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

			grid.on("dblclick", getRecord);
			function queryRecord() {
				var fuzzytext = fuzzy.getValue();
				store.baseParams = {
					fuzzy : fuzzytext
				};
				store.load({
							params : {
								// conTypeId : 2,
								start : 0,
								limit : 18
							}
						});
			}

			function getRecord() {
				var records = grid.selModel.getSelections();
				var recordslen = records.length;
				if (recordslen > 0) {
					var temparr = new Array();
//					var con = new Object();
					for (var i = 0; i <= recordslen - 1; i++) {
//						// //合同id
//						con.conId = record.get("conId");
//						// 合同编号
//						con.contractNo = record.get("conttreesNo");
//						// 合同名称
//						con.contractName = record.get("contractName");
//
//						con.actAmount = record.get("actAmount");
//						con.payedAccount = record.get("payedAmount");
//
//						con.clientName = record.get("clientName");
//						con.clientId = record.get("cliendId");
						
						temparr.push(records[i].data)
					}
					window.returnValue = temparr;
					window.close()
				}

				// if (grid.selModel.hasSelection()) {
				//		
				// var records = grid.selModel.getSelections();
				// var recordslen = records.length;
				// var temparr = new Array();
				//			
				// if (recordslen > 1) {
				// Ext.Msg.alert("系统提示信息", "请选择其中一项！");
				// } else {
				// var record = grid.getSelectionModel().getSelected();
				// var con=new Object();
				// //合同id
				// con.conId=record.get("conId");
				// //合同编号
				// con.contractNo=record.get("conttreesNo");
				// //合同名称
				// con.contractName=record.get("contractName");
				//				
				// con.actAmount=record.get("actAmount");
				// con.payedAccount=record.get("payedAmount");
				//				
				// con.clientName=record.get("clientName");
				// con.clientId=record.get("cliendId");
				//				
				// window.returnValue = con;
				// window.close();
				// }
				// } else {
				// Ext.Msg.alert("提示", "请先选择行!");
				// }
			}
			// ---------------------------------------

			new Ext.Viewport({
						enableTabScroll : true,
						layout : "fit",
						items : [grid]
					});

		});