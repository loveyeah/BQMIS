Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var nowdate = new Date();
	var date1 = nowdate.toLocaleDateString();
	var sxdate;
	var exdate;
	if (date1.substring(6, 7) == '月') {
		sxdate = date1.substring(0, 4) + "-01";
		exdate = date1.substring(0, 4) + '-0' + date1.substring(5, 6);
	} else {
		sxdate = date1.substring(0, 4) + "-" + date1.substring(5, 7);
		exdate = date1.substring(0, 4) + "-" + date1.substring(5, 7);
	}
	var fromdate = sxdate;
	var enddate = exdate;
	var fromDate = new Ext.form.TextField({
				name : 'startDate',
				value : sxdate,
				id : 'fromDate',
				fieldLabel : "执行时间",
				style : 'cursor:pointer',
				cls : 'Wdate',
				anchor : '100%',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M',
									dateFmt : 'yyyy-MM',
									alwaysUseStartDate : true
								});
					}
				}
			});
	var toDate = new Ext.form.TextField({
				name : 'endDate',
				value : exdate,
				id : 'toDate',
				fieldLabel : "至",
				style : 'cursor:pointer',
				cls : 'Wdate',
				anchor : '80%',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M',
									dateFmt : 'yyyy-MM',
									alwaysUseStartDate : true
								});
					}
				}
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
	var cliendId = new Ext.form.ComboBox({
				fieldLabel : "供应商",
				id : 'cliendId',
				name : 'cliendId',
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'con.cliendId',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				// allowBlank : false,
				// emptyText : '请选择',
				anchor : '100%'
			});
	var operateBox = new Ext.form.ComboBox({
				fieldLabel : '经办人',
				// store : [['999999', '测试员'], ['000000', 'xx']],
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
			});
	var btnQuery = new Ext.Button({
		id : 'btnQuery',
		text : '查  询',
		minWidth : 70,
		handler : function() {
			var _thisTitle = '<center><font color="blue" size="3">大唐灞桥电厂采购合同付款执行情况统计</font><br/><br/>'
					+ fromDate.getValue()
					+ "  至 "
					+ toDate.getValue()
					+ "</center>";
			Grid.setTitle(_thisTitle);
			var sd = Ext.get('fromDate').dom.value;
			var ed = Ext.get('toDate').dom.value;
			if (sd > ed) {
				Ext.Msg.alert("提示", "选择后一日期应比前一日期大！");
				return false;
			}
			con_ds.load({
						params : {
							startdate : Ext.get("fromDate").dom.value,
							enddate : Ext.get("toDate").dom.value,
							start : 0,
							limit : 18,
							conNo : Ext.get('conttreesNo').dom.value,
							conName : Ext.get('contractName').dom.value,
							clientName : Ext.get('cliendId').dom.value,
							operaterBy : hidoperateBox.getValue(),
							conTypeId : 1
						}
					});
		}
	});
	var btnClear = new Ext.Button({
				id : 'btnClear',
				text : '清除条件',
				minWidth : 70,
				handler : function() {
					form.getForm().reset();
				}
			});
	var btnInfo = new Ext.Button({
		id : 'btnInfo',
		text : '合同信息',
		minWidth : 70,
		handler : function() {
			var selrows = Grid.getSelectionModel().getSelections();
			if (selrows.length > 0) {
				var record = Grid.getSelectionModel().getSelected();
				var url;
				var conId = record.data.conId;
				if ((conId != null) && (conId != "")) {
					url = "../../../../manage/contract/business/conBaseInfo/conBaseInfo.jsp?id="
							+ conId;
				}
				var o = window.showModalDialog(url, '',
						'dialogWidth=850px;dialogHeight=700px;status=no');
			} else {
				Ext.Msg.alert('提示', '请从列表中选择一条记录!');
			}
		}
	});
	var btnPrint = new Ext.Button({
				id : 'btnPrint',
				text : '会签表 ',
				minWidth : 70,
				iconCls : 'pdfview',
				handler : function() {
					CheckRptPreview()
				}
			});

	// 会签票面浏览
	function CheckRptPreview() {
		var selected = Grid.getSelectionModel().getSelections();
		var appId;
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要浏览的记录！");
		} else {
			var menber = selected[0];
			appId = menber.get('balanceName');
			var url = "/powerrpt/report/webfile/bqmis/CGContractBalance.jsp?appId="
					+ appId;
			window.open(url);

		}
	};

	var btnExport = new Ext.Button({
				id : 'btnExport',
				text : '导  出',
				minWidth : 70,
				handler : function() {
					myexport();
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

	function myexport() {
		Ext.Ajax.request({
			url : 'managecontract/findConPayDetailsList.action?conTypeId=' + 1,
			success : function(response) {
				var json = eval('(' + response.responseText.trim() + ')');
				var records = json.root;
				// alert(records.length);
				var html = ['<table border=1><tr><th>合同编号</th><th>合同名称</th><th>供应商</th><th>总金额</th><th>币别</th><th>已结算金额</th><th>未结算金额</th><th>经办人</th></tr>'];
				for (var i = 0; i < records.length; i += 1) {
					var rc = records[i];
					html.push('<tr><td>' + rc.conttreesNo + '</td><td>'
							+ rc.contractName + '</td><td>' + rc.clientName
							+ '</td><td>' + rc.actAmount + '</td><td>'
							+ rc.currencyName + '</td><td>' + rc.payedAmount
							+ '</td><td>' + rc.unliquidate + '</td><td>'
							+ rc.operateName + '</td></tr>');
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
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										border : false,
										columnWidth : 0.3,
										layout : 'form',
										items : [cliendId]
									}, {
										border : false,
										columnWidth : 0.4,
										layout : 'form',
										items : [operateBox, hidoperateBox]
									}, {
										border : false,
										columnWidth : 0.15,
										layout : 'form',
										hidden : true,
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
				name : 'payedAmount'
			}, {
				name : 'unliquidate'
			}, {
				name : 'operateName'
			}, {
				name : 'currencyName'
			}, {
				name : 'balanceName'
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
				header : '合同编号',
				dataIndex : 'conttreesNo',
				align : 'center'
			}, {
				header : '合同名称',
				dataIndex : 'contractName',
				align : 'center'
			}, {
				header : '供应商',
				dataIndex : 'clientName',
				align : 'center'
			}, {
				header : '总金额',
				dataIndex : 'actAmount',
				align : 'center'
			}, {
				header : '币别',
				dataIndex : 'currencyName',
				align : 'center'
			}, {
				header : '已结算金额',
				dataIndex : 'payedAmount',
				width : 120,
				align : 'center'
			}, {
				header : '未结算金额',
				dataIndex : 'unliquidate',
				width : 120,
				align : 'center'
			}, {
				header : '经办人',
				dataIndex : 'operateName',
				align : 'center'
			}]);
	con_item_cm.defaultSortable = true;
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'managecontract/findConPaymentList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'total',
							root : 'root'
						}, con_item)
			});

	con_ds.load({
				params : {
					startdate : sxdate,
					enddate : exdate,
					start : 0,
					limit : 18,
					conTypeId : 1
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
	var _title = '<center><font color="blue" size="3">大唐灞桥电厂采购合同付款执行情况统计</font><br/><br/>'
			+ fromdate + "  至 " + enddate + "</center>";
	var Grid = new Ext.grid.GridPanel({
				title : _title,
				ds : con_ds,
				cm : con_item_cm,
				sm : con_sm,
				width : Ext.get('div_lay').getWidth(),
				split : true,
				autoScroll : true,
				bbar : gridbbar,
				// tbar : gridtbar,
				border : false,
				viewConfig : {
					forceFit : false
				}
			});
	// 显示柱状图
	function displaygraphcolumn() {
		//
		var so = new SWFObject("comm/amchart/amcolumn/amcolumn.swf", "amline",
				"100%", "100%", "8", "#FFFFFF");
		so.addVariable("path", "comm/amchart/amcolumn/");
		so.addVariable("settings_file",
				escape("comm/amchart/amcolumn/amcolumn_settings.xml"));
		so.addVariable("data_file",
				escape("system/getElectricColumnOnUnit.action"));
		so.addVariable("preloader_color", "#999999");
		so.write("flashColumn");
	}
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
							id : 'centerPanel',
							layout : 'fit',
							// title : _title ,
							border : false,
							collapsible : true,
							split : true,
							margins : '0 0 0 0',
							// 注入表格
							items : [Grid]
						}]
			});
})