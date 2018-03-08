Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
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
	}
	var enddate = new Date();
	var startdate = enddate.add(Date.MONTH, -1);
	startdate = startdate.getFirstDateOfMonth();
	var sdate = ChangeDateToString(startdate);
	var edate = ChangeDateToString(enddate);
	var fromDate = new Ext.form.DateField({
				format : 'Y-m-d',
				name : 'startDate',
				id : 'fromDate',
				itemCls : 'sex-left',
				clearCls : 'allow-float',
				checked : true,
				fieldLabel : "履行时间",
				allowBlank : false,
				readOnly : true,
				value : sdate,
				emptyText : '请选择',
				anchor : '100%'
			});
	var toDate = new Ext.form.DateField({
				format : 'Y-m-d',
				name : 'endDate',
				value : edate,
				id : 'toDate',
				itemCls : 'sex-left',
				clearCls : 'allow-float',
				fieldLabel : "至",
				allowBlank : false,
				readOnly : true,
				emptyText : '请选择',
				anchor : '80%'
			});
	var conttreesNo = new Ext.form.TextField({
				id : 'conttreesNo',
				xtype : "textfield",
				fieldLabel : "合同编号",
				name : 'conttreesNo',
				anchor : '100%'
			});
	var contractName = new Ext.form.TextField({
				id : 'contractName',
				xtype : "textfield",
				fieldLabel : "合同名称",
				name : 'contractName',
				anchor : '80%'
			});

	// 合作伙伴
	var cliendId = new Ext.form.ComboBox({
		name : 'cliendId',
		id : 'cliendId',
		fieldLabel : '合作伙伴',
		mode : 'remote',
		editable : false,
		anchor : '100%',
		emptyText : '请选择',
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../manage/client/business/clientSelect/clientSelect.jsp?approveFlag=2";
			var client = window
					.showModalDialog(
							url,
							null,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(client) != "undefined") {
				cliendId.setValue(client.clientName);
				hidcliendId.setValue(client.cliendId);
			}
		}
	});
	var hidcliendId = new Ext.form.Hidden({
				id : "hidcliendBox",
				name : "con.cliendId"
			})

	var operateBox = new Ext.form.ComboBox({
				fieldLabel : '经办人',
				// store : [['999999', '测试员'], ['000000', 'xx'], [null, '全部']],
				id : 'operateName',
				// name : 'operateBy',
				// valueField : "value",
				// displayField : "text",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'operateByxx',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				emptyText : '请选择',
				anchor : '80%',
				onTriggerClick : function(e) {
					var url = "../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
					var args = {
						selectModel : 'single',
						notIn : "",
						rootNode : {
							id : '-1',
							text : '灞桥电厂'
						}
					}
					var o = window.showModalDialog(url, args,
							'dialogWidth=650px;dialogHeight=500px;status=no');
					if (typeof(o) == "object") {
						operateBox.setValue(o.workerName);
						hidoperateBox.setValue(o.workerCode);
					}
				}
			});
	var hidoperateBox = new Ext.form.Hidden({
				id : 'hidoperateBox',
				name : 'operateByxx'
			})
	var btnQuery = new Ext.Button({
		id : 'btnQuery',
		text : '查  询',
		minWidth : 80,
		handler : function() {
			var _thisTitle = '<center><font color="blue" size="3">大唐灞桥电厂项目合同付款明细统计</font><br/><br/>'
					+ fromDate.value + "  至 " + toDate.value + "</center>";
			Grid.setTitle(_thisTitle);
			con_ds.load({
						params : {
							conTypeId : 2,
							startdate : Ext.get("fromDate").dom.value,
							enddate : Ext.get("toDate").dom.value,
							start : 0,
							limit : 18,
							conNo : Ext.get('conttreesNo').dom.value,
							conName : Ext.get('contractName').dom.value,
							clientName : hidcliendId.getValue(),
							operaterBy : hidoperateBox.getValue()
						}
					});
		}
	});
	var btnClear = new Ext.Button({
				id : 'btnClear',
				text : '清除条件',
				minWidth : 80,
				handler : function() {
					form.getForm().reset();
				}
			});
	var btnInfo = new Ext.Button({
		id : 'btnInfo',
		text : '合同信息',
		minWidth : 80,
		handler : function() {
			var selrows = Grid.getSelectionModel().getSelections();
			if (selrows.length > 0) {
				var record = Grid.getSelectionModel().getSelected();
				var url;
				var conId = record.data.conId;
				if ((conId != null) && (conId != "")) {
					url = "../../../../manage/projectFact/business/conBaseInfo/conBaseInfo.jsp?id="
							+ conId;
				}
				var o = window.showModalDialog(url, '',
						'dialogWidth=850px;dialogHeight=700px;status=no');
			} else {
				Ext.Msg.alert('提示', '请从列表中选择一条记录!');
			}
		}
	});
	var btnPayInfo = new Ext.Button({
				id : 'btnPayInfo',
				text : '结算会签表',
				minWidth : 80,
				handler : function() {
					CheckRptPreview1()
				}
			});

	function CheckRptPreview1() {
		var selected = Grid.getSelectionModel().getSelections();
		var balanceId;

		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要浏览的记录！");
		} else {

			var menber = selected[0];
			balanceId = menber.get('balanceId');

			var url = "/powerrpt/report/webfile/bqmis/GCContractBalance.jsp?balanceId="
					+ balanceId;
			window.open(url);

		}
	}
	// 验收票面浏览
	function CheckRptPreview() {

		var selected = Grid.getSelectionModel().getSelections();
		var conId;

		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要浏览的记录！");
		} else {

			var menber = selected[0];
			conId = menber.get('conId');

			var url = "/powerrpt/report/webfile/bqmis/GCContractMeetSign.jsp?conId="
					+ conId;
			window.open(url);

		}
	};
	var btnPrint = new Ext.Button({
				id : 'btnPrint',
				text : '打 印 ',
				minWidth : 80,
				handler : function() {
					CheckRptPreview()
				}
			});
	var btnExport = new Ext.Button({
				id : 'btnExport',
				text : '导  出',
				minWidth : 80,
				handler : function() {
					myExport();
				}
			});
	// 导出
	function tableToExcel(tableHTML) {
		window.clipboardData.setData("Text", tableHTML);
		try {
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExApp.DisplayAlerts = false;
			ExApp.visible = true;
		} catch (e) {
			if (e.number != -2146827859)
				alert("您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}

	function myExport() {
		Ext.Ajax.request({
			url : 'managecontract/findConPayDetailsList.action',
			success : function(response) {
				var json = eval('(' + response.responseText.trim() + ')');
				var records = json.root;
				// alert(records.length);
				var html = ['<table border=1><tr><th>合同编号</th><th>合同名称</th><th>合作伙伴</th><th>合同金额</th><th>币别</th><th>费用来源</th><th>已付款</th><th>未付款</th><th>付款金额</th><th>计划付款日期</th><th>实际付款金额</th><th>实际付款日期</th><th>经办人</th><th>备注</th></tr>'];
				for (var i = 0; i < records.length; i += 1) {
					var rc = records[i];
					html.push('<tr><td>' + rc.conttreesNo + '</td><td>'
							+ rc.contractName + '</td><td>' + rc.clientName
							+ '</td><td>' + rc.actAmount + '</td><td>'
							+ rc.currencyType + '</td><td>' + rc.itemName
							+ '</td><td>' + rc.payedAmount + '</td><td>'
							+ rc.unliquidate + '</td><td>' + rc.payPrice
							+ '</td><td>' + rc.payDate + '</td><td>'
							+ rc.balancePrice + '</td><td>' + rc.balaDate
							+ '</td><td>' + rc.operateName + '</td><td>'
							+ rc.memo + '</td></tr>');
				}
				html.push('</table>');
				html = html.join(''); // 最后生成的HTML表格
				// alert(html);
				tableToExcel(html);
			},
			failure : function(response) {
				Ext.Msg.alert('信息', '失败');
			}
		});
	}
	var content = new Ext.form.FieldSet({
				title : '查询条件',
				height : '100%',
				collapsible : true,
				layout : 'form',
				items : [{
							layout : 'column',
							border : false,
							items : [{
										border : false,
										columnWidth : 0.3,
										layout : 'form',
										items : [fromDate]
									}, {
										border : false,
										columnWidth : 0.4,
										layout : 'form',
										items : [toDate]
									}, {
										border : false,
										columnWidth : 0.15,
										align : 'center',
										layout : 'form',
										items : [btnQuery]
									}, {
										border : false,
										columnWidth : 0.15,
										align : 'center',
										layout : 'form',
										items : [btnClear]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										border : false,
										columnWidth : 0.3,
										layout : 'form',
										items : [conttreesNo]
									}, {
										border : false,
										columnWidth : 0.4,
										layout : 'form',
										items : [contractName]
									}, {
										border : false,
										columnWidth : 0.15,
										layout : 'form',
										items : [btnInfo]
									}, {
										border : false,
										columnWidth : 0.15,
										layout : 'form',
										items : [btnPayInfo]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										border : false,
										columnWidth : 0.3,
										layout : 'form',
										items : [cliendId, hidcliendId]
									}, {
										border : false,
										columnWidth : 0.4,
										layout : 'form',
										items : [operateBox, hidoperateBox]
									}, {
										border : false,
										columnWidth : 0.15,
										layout : 'form',
										items : [btnExport]
									}, {
										border : false,
										columnWidth : 0.15,
										layout : 'form',
										items : [btnPrint]
									}]
						}]
			});
	var form = new Ext.form.FormPanel({
				bodyStyle : "padding:5px 5px 0",
				labelAlign : 'right',
				id : 'shift-form',
				labelWidth : 80,
				autoHeight : true,
				region : 'center',
				border : false,
				items : [content]
			});
	/*-----------------------------------------------------------------*/
	var con_item = Ext.data.Record.create([{
				name : 'conId'
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
				name : 'itemId'
			}, {
				name : 'payedAmount'
			}, {
				name : 'unliquidate'
			}, {
				name : 'payPrice'
			}, {
				name : 'payDate'
			}, {
				name : 'balancePrice'
			}, {
				name : 'balaDate'
			}, {
				name : 'operateName'
			}, {
				name : 'memo'
			}, {
				name : 'balanceId'
			}, {
				name : 'paymentId'
			}, {
				name : 'startDate'
			}, {
				name : 'currencyName'
			}, {
				name : 'paymentMoment'
			}, {
				name : 'itemName'
			}]);
	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	var con_item_cm = new Ext.grid.ColumnModel([con_sm,
			new Ext.grid.RowNumberer({
						// header : '序号',
						width : 20,
						align : 'center'
					}), {
				header : '付款阶段',
				dataIndex : 'paymentMoment',
				align : 'center'
			}, {
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
				header : '合同金额',
				dataIndex : 'actAmount',
				align : 'center'
			}, {
				header : '币别',
				dataIndex : 'currencyName',
				align : 'center'
				// ,
			// renderer : function(v) {
			//
			// if (v == 1) {
			// return "RMB";
			// }
			// if (v == 2) {
			// return "USD";
			// } else {
			// return "异常";
			// }
			//
			// }
		}	, {
				header : '费用来源',
				dataIndex : 'itemName',
				align : 'center'
				// ,
			// renderer : function(v) {
			// if(v=='lwcb'){
			// return "劳务成本";
			// } else {
			// return "制造费用"
			// }
			// }

		}	, {
				header : '已付款',
				dataIndex : 'payedAmount',
				width : 120,
				align : 'center',
				renderer : function(v) {
					if (v == null) {
						return "0";
					} else {
						return v;
					}
				}
			}, {
				header : '未付款',
				dataIndex : 'unliquidate',
				width : 120,
				align : 'center'
			}, {
				header : '计划付款',
				dataIndex : 'payPrice',
				width : 120,
				align : 'center'
			}, {
				header : '付款日期',
				dataIndex : 'payDate',
				width : 120,
				align : 'center'
			}, {
				header : '实际付款金额',
				dataIndex : 'balancePrice',
				hidden : true,
				width : 120,
				align : 'center'
			}, {
				header : '实际付款日期',
				dataIndex : 'balaDate',
				hidden : true,
				width : 120,
				align : 'center'
			}, {
				header : '经办人',
				dataIndex : 'operateName',
				align : 'center'
			}, {
				header : '备注',
				dataIndex : 'memo',
				align : 'center'
			}, {
				dataIndex : 'balanceId',
				hidden : true,
				align : 'center'
			}, {
				dataIndex : 'paymentId',
				hidden : true,
				align : 'center'
			}, {
				dataIndex : 'startDate',
				hidden : true,
				align : 'center'
			}]);
	con_item_cm.defaultSortable = true;
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'managecontract/findConPayDetailsList.action'
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
					start : 0,
					limit : 18
				}
			});
	var gridbbar = new Ext.PagingToolbar({
				pageSize : 18,
				store : con_ds,
				displayInfo : true,
				displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
				emptyMsg : "没有记录",
				beforePageText : '页',
				afterPageText : "共{0}"
			});

	var _title = '<center><font color="blue" size="3">大唐灞桥电厂项目合同付款明细统计</font><br/><br/>'
			+ sdate + "  至 " + edate + "</center>";
	var Grid = new Ext.grid.GridPanel({
				title : _title,
				ds : con_ds,
				cm : con_item_cm,
				sm : con_sm,
				width : Ext.get('div_lay').getWidth(),
				split : true,
				autoScroll : true,
				bbar : gridbbar,
				border : false,
				viewConfig : {
					forceFit : false
				}
			});

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				items : [{
							region : "north",
							layout : 'fit',
							height : 150,
							border : true,
							split : false,
							margins : '0 0 0 0',
							// 注入表格
							items : [form]
						}, {
							region : "center",
							layout : 'fit',
							border : false,
							// collapsible : true,
							split : true,
							margins : '0 0 0 0',
							// 注入表格
							items : [Grid]
						}]
			});

})