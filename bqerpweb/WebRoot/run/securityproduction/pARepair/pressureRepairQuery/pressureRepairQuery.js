Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	
	var queryName = new Ext.form.TextField({
		id : 'queryName'
	})
	var startTime = new Ext.form.TextField({
		id : 'startTime',
		readOnly : true,
		listeners : {
			focus : function(){
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true
				})
			}
		}
	})
	var endTime = new Ext.form.TextField({
		id : 'endTime',
		readOnly : true,
		listeners : {
			focus : function(){
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true
				})
			}
		}
	})
	
		var record = Ext.data.Record.create([{
					name : 'boilerRepairId',
					mapping : 0
				}, {
					name : 'boilerId',
					mapping : 1
				}, {
					name : 'type',
					mapping : 2
				}, {
					name : 'boilerName',
					mapping : 3
				}, {
					name : 'repairBegin',
					mapping : 4
				}, {
					name : 'repairEnd',
					mapping : 5
				}, {
					name : 'reportNo',
					mapping : 6
				}, {
					name : 'repairUnit',
					mapping : 7
				}, {
					name : 'repairResult',
					mapping : 8
				}, {
					name : 'nextTime',
					mapping : 9
				}]);

		var dataProxy = new Ext.data.HttpProxy({
					url : 'security/getSpJPressureRepairList.action'
				});

		var theReader = new Ext.data.JsonReader({
					root : "list",
					totalProperty : "totalCount"
				}, record);

		var store = new Ext.data.Store({
					proxy : dataProxy,
					reader : theReader
				});
		
		var sm = new Ext.grid.CheckboxSelectionModel();

		var grid = new Ext.grid.GridPanel({
					region : "center",
					layout : 'fit',
					store : store,
					columns : [sm, new Ext.grid.RowNumberer({
										header : '行号',
										width : 35,
										align : 'left'
									}), {
								header : "检验记录ID",
								width : 75,
								sortable : true,
								dataIndex : 'boilerRepairId',
								hidden : true
							}, {
								header : "锅炉Id",
								width : 200,
								sortable : true,
								dataIndex : 'boilerId',
								hidden : true
							}, {
								header : "设备名称",
								width : 200,
								sortable : true,
								dataIndex : 'boilerName'
							}, {
								header : "检验开始时间",
								width : 200,
								sortable : true,
								dataIndex : 'repairBegin'
							}, {
								header : "检验结束时间",
								width : 200,
								sortable : true,
								dataIndex : 'repairEnd'
							}, {
								header : "检验报告编号",
								width : 200,
								sortable : true,
								dataIndex : 'reportNo'
							}, {
								header : "检验单位",
								width : 200,
								sortable : true,
								dataIndex : 'repairUnit'
							}, {
								header : "检验结果",
								width : 200,
								sortable : true,
								dataIndex : 'repairResult',
								renderer : function(v) {
									if (v == '1') {
										return "一级";
									}
									if (v == '2') {
										return "二级";
									}
									if (v == '3') {
										return "三级";
									}
									if (v == '4') {
										return "四级";
									}
								}
							},  {
								header : "下次检验时间",
								width : 200,
								sortable : true,
								dataIndex : 'nextTime'
							}],
					sm : sm,
					autoSizeColumns : true,
					viewConfig : {
						forceFit : true
					},
					tbar : ['名称',queryName,"检修时间:",startTime,'至：',endTime , '-',{
								text : "查询",
								iconCls : 'query',
								minWidth : 60,
								handler : query
							}, '-', {
								text : "导出",
								iconCls : 'export',
								minWidth : 60,
								handler : exportRecord
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
		grid.on("rowdblclick",updateRecord);

		var boilerRepairId = new Ext.form.Hidden({
					id : "boilerRepairId",
					name : 'repair.boilerRepairId'
				});
		
		var boilerId = new Ext.form.Hidden({
					id : "boilerId",
					name : 'repair.boilerId'
				});

	  var boilerName = new Ext.form.TriggerField({
		id : 'boilerName',
		fieldLabel : '设备名称',
		allowBlank : false,
		disabled : true,
		name : 'boilerName',
		width : 150,
		onTriggerClick : function() {
			var args = {
				selectModel : 'single',
				rootNode : {
					id : "root",
					text : '锅炉设备树'
				}
			}
			var url = "/power/run/securityproduction/BoilerRepair/blockAndType.jsp";
			var rvo = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:250px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (typeof(rvo) != "undefined") {
				
                boilerId.setValue(rvo.code);
                boilerName.setValue(rvo.name);  
			}
		}
	})

	   var repairBegin = new Ext.form.TextField({
					id : "repairBegin",
					fieldLabel : '检验开始时间',
					width : 150,
					disabled : true,
					name : 'repair.repairBegin',
				    listeners : {
					 focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : true
								});
					 }
				 }
				});			
				
	   var repairEnd = new Ext.form.TextField({
					id : "repairEnd",
					fieldLabel : '检验结束时间',
					width : 150,
					name : 'repair.repairEnd',
					disabled : true,
				    listeners : {
					 focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : true
								});
					 }
				 }
				});	
				
		var reportNo = new Ext.form.TextField({
					id : "reportNo",
					fieldLabel : '检验报告编号',
					allowBlank : false,
					width : 150,
					disabled : true,
					name : 'repair.reportNo'
				});

		var repairUnit = new Ext.form.TextField({
					id : "repairUnit",
					fieldLabel : '检验单位',
					width : 150,
					disabled : true,
					name : 'repair.repairUnit'
				});		
			var repairResult = new Ext.form.ComboBox({
					fieldLabel : '检验结果',
					disabled : true,
					store : new Ext.data.SimpleStore({
								fields : ['value', 'text'],
								data : [['1', '一级'], ['2', '二级']
								       ,['3', '三级'], ['4', '四级']]
							}),
					id : 'repairResult',
					name : 'repairResult',
					valueField : "value",
					displayField : "text",
					mode : 'local',
					typeAhead : true,
		            allowBlank : false,
					forceSelection : true,
					hiddenName : 'repair.repairResult',
					editable : false,
					triggerAction : 'all',
					selectOnFocus : true,
					width : 150
				});
				
	   var nextTime = new Ext.form.TextField({
					id : "nextTime",
					fieldLabel : '下次检验时间',
		            allowBlank : false,
					width : 150,
					disabled : true,
					name : 'repair.nextTime',
				    listeners : {
					 focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : true
								});
					 }
				 }
				});	
   
		var addpanel = new Ext.FormPanel({
						frame : true,
						labelAlign : 'center',
						labelWidth : 100,
						closeAction : 'hide',
						items : [{
							layout : 'column',
							items : [{// 第一列
								columnWidth : 0.5,
								layout : 'form',
								items : [boilerRepairId,boilerId,boilerName, repairUnit,repairResult , reportNo ]
							}, {	// 第二列
										columnWidth : 0.5,
										layout : 'form',
										items : [repairBegin, repairEnd,nextTime]
									}]

						}]
					});

	function query(){
		store.baseParams = {
			queryName : queryName.getValue(),
			startTime : startTime.getValue(),
			endTime : endTime.getValue(),
			type:'1'
		}
		store.load({
					start : 0,
					limit : 18
				})
	}
					
					
		var win = new Ext.Window({
					width : 550,
					height : 200,
					buttonAlign : "center",
					items : [addpanel],
					layout : 'fit',
					closeAction : 'hide',
					resizable : false,
					modal : true
				});

		function updateRecord() {
			if (grid.selModel.hasSelection()) {
				var records = grid.selModel.getSelections();
				var recordslen = records.length;
				if (recordslen > 1) {
					;
				} else {
					win.show();
					var record = grid.getSelectionModel()
							.getSelected();
					addpanel.getForm().reset();
					addpanel.getForm().loadRecord(record)
					win.setTitle("锅炉或压力容器检修信息");
				}
			}
		}

		var copyStore = new Ext.data.Store({
					proxy : dataProxy,
					reader : theReader
				});
		copyStore.on('beforeload',function(){
			Ext.apply(this.baseParams,{
				queryName : queryName.getValue(),
			startTime : startTime.getValue(),
			endTime : endTime.getValue(),
			type : '1'
			})
		})
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
		if (copyStore.getTotalCount() == 0) {
			Ext.Msg.alert('提示', '无数据可导出！')
		} else {
			var html = ['<table border=1><tr><th>名称</th><th>检修开始时间</th><th>检修结束时间</th><th>检修报告编号</th><th>检修单位</th><th>检修结果</th><th>下次检修时间</th>'
					+ '</tr>'];
			for (var i = 0; i < copyStore.getTotalCount(); i++) {
				var rc = copyStore.getAt(i);
				var result = rc.get('repairResult') == null ? '' : rc
						.get('repairResult');
				if (result) {
					if (result == 1)
						result = '一级';
					else if (result == 2)
						result = '二级';
					else if (result == 3)
						result = '三级';
					else if (result == 4)
						result = '四级';
				}
				html.push('<tr><td>' + (rc.get('boilerName') == null ? "" : rc.get('boilerName')) + '</td>' + '<td>'
						+(rc.get('repairBegin') == null ? "" : rc.get('repairBegin')) + '</td>' + '<td>'
						+(rc.get('repairEnd') == null ? "" : rc.get('repairEnd')) + '</td>'
						+ '<td>' + (rc.get('reportNo') == null ? "" : rc.get('reportNo')) + '</td>'
						+ '<td>' +(rc.get('repairUnit') == null ? "" : rc.get('repairUnit'))  + '</td>' + '<td>'
						+result + '</td>' + '<td>'
						+ (rc.get('nextTime') == null ? "" : rc.get('nextTime')) + '</td>'
						+ '</tr>')

			}
			html.push('</table>');
			html = html.join(''); // 最后生成的HTML表格
			tableToExcel(html);
		}

	}

	function exportRecord() {
		copyStore.load();
	}
	copyStore.on('load',myExport)
		
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});
	query()
});