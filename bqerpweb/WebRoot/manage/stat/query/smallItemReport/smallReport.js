Ext.onReady(function() { 
	var dateType;// 报表时间类型
	var grid;
	var ds;
	function getDate(v) {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		t = d.getDate();
		s += "-" + (t > 9 ? "" : "0") + t + " ";
		if (v == 1) {
			t = d.getHours();
			s += (t > 9 ? "" : "0") + t;
		}
		return s;
	}
	function getAttri(jsonData, columnName) {
		for (var attr in jsonData) {
			if (attr == columnName)
				return jsonData[attr]
		}
		return "";
	}
	
	var ctbtnOut = new Ext.Button({
				id : 'reflesh',
				text : '导出',
				disabled:true,
				iconCls : 'export',
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
	function NullToZero(tobeparse) {
		if (tobeparse == null || tobeparse == undefined) {
			return 0;
		} else {
			return tobeparse;
		}
	}

	function myExport() { 
						var ItemName = Ext.get("reportNameBox").dom.value;
						var count = (json.data != "") ? json.columModle1.length : 5;
						var html = ['<table border=1><tr ><th colspan ='+count+'><center><font size="3">'
						+ ItemName
						+ '</font></center></th></tr>'];
	                 var gridnum = json.gridnum;
				for (k = 1; k <= gridnum; k++) { 
					var myComlumModel=getAttri(json, "columModle" + k);
					html.push('<tr>');
						for (var i = 0; i < myComlumModel.length; i += 1) {
							html.push('<th>' + myComlumModel[i].header
									+ '</th>');

						} 
						html.push('</tr>'); 
						for (var i = 0; i < ds.getCount(); i += 1) {
							var rc = ds.getAt(i); 
							html.push('<tr>');
							for (var j = 0; j < myComlumModel.length; j += 1) {
								var v_name = myComlumModel[j].dataIndex; 
								html.push('<td>' + rc.get(v_name) + '</td>');
							}
							html.push('</tr>');
						}
					}
						html.push('</table>');
						html = html.join(''); // 最后生成的HTML表格 
						tableToExcel(html); 
	}
	
	function query() {
		if(reportNameBox.getValue() == "" || startTime.getValue() == "")
		{
			Ext.Msg.alert('提示','请选择查询条件！')
			return false；
		}
		Ext.Msg.wait("正在查询数据!请等待...");
		Ext.Ajax.request({
			url : 'manager/smallItemReportQuery.action',
			params : {
				reportId : reportNameBox.getValue(),
				dateType : dateType,
				date : startTime.getValue(),
				
				quarter : startQuarterBox.getValue()
			},
			method : 'post',
			success : function(result, request) {
				 json = eval('(' + result.responseText + ')');
				ds = new Ext.data.JsonStore({
					data : json.data,
					fields : json.fieldsNames
				}); 
				var gridnum = json.gridnum;  
				document.getElementById("changePanel").innerHTML = "";
				for (i = 1; i <= gridnum; i++) {
//					var el = {
//						id : 'div' + i + '',
//						tag : 'div',
//						style : 'border:0px;style="width:20"'
//					}; 
//				Ext.DomHelper.append(panel.body, tempPanel)  
					var o =document.createElement("<div id='div"+i+"' style=''border:0px;vertical-align:top;'></div>");
					document.getElementById("changePanel").appendChild(o); 
					document.getElementById("div" + i).innerHTML = "";
				    var	grid = new Ext.grid.GridPanel({
						renderTo : 'div' + i + '',
						id : 'grid' + i + '',
						split : false,
						width :(json.columModle1.length)*180+10,
						autoScroll : false,
  					    forcefit : true,
						border : true,
						cm : new Ext.grid.ColumnModel(getAttri(json, "columModle" + i)),
						ds : ds
					});
					grid.render(); 
				}
				Ext.Msg.hide();
			},
			failure : function(result, request) {
				Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
				Ext.Msg.hide();
			}
		});
		ctbtnOut.setDisabled(false);
	}
	var nowdate = new Date();
	var date1 = nowdate.toLocaleDateString();
	var exdate;
	if (date1.substring(6, 7) == '月') {
		exdate = date1.substring(0, 4) + '-0' + date1.substring(5, 6);
	} else {
		exdate = date1.substring(0, 4) + "-" + date1.substring(5, 7);
	}
	function setValue(reportType) {
		var d = new Date();
		if (reportType == 1)
			startTime.setValue(getDate(1)), startQuarterBox.hide();
		if (reportType == 3 ||reportType == 4)
			startTime.setValue(getDate(3)), startQuarterBox.hide();
			//20100913  月报表查询精确到天 bjxu
//		if (reportType == 4)
//			startTime.setValue(exdate), startQuarterBox.hide();
		if (reportType == 5)
			startTime.setValue(d.getFullYear()), startQuarterBox.show();
		if (reportType == 6)
			startTime.setValue(d.getFullYear()), startQuarterBox.hide();
	}
	// 时间类型
	var startTime = new Ext.form.TextField({
		id : 'startTime',
		allowBlank : false,
		fieldLabel : '时段：',
		// value : getDate(),
		width : 100,
		listeners : {
			focus : function() {
				var format = "";
				if (dateType == 1)
					format = "yyyy-MM-dd HH";
				if (dateType == 3 || dateType == 4)
					format = "yyyy-MM-dd";
					//20100913  月报表查询精确到天 bjxu
//				if (dateType == 4)
//					format = "yyyy-MM";
				if (dateType == 5)
					format = "yyyy";
				if (dateType == 6)
					format = "yyyy";
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : format,
					alwaysUseStartDate : true
				});
			}
		}
	});

	var startQuarterBox = new Ext.form.ComboBox({
		fieldLabel : '季度',
		store : [['1', '第一季'], ['2', '第二季'], ['3', '第三季'], ['4', '第四季']],
		id : 'startQuarterBox',
		name : 'startQuarterBoxName',
		valueField : "value",
		displayField : "text",
		mode : 'local',
		typeAhead : true,
		hidden : true,
		forceSelection : true,
		hiddenName : 'startQuarterBoxName',
		editable : false,
		triggerAction : 'all',
		width : 65,
		selectOnFocus : true,
		value : 1
	});

	var rec = Ext.data.Record.create([{
		name : 'reportId'
	}, {
		name : 'reportName'
	}, {
		name : 'dataType'
	}, {
		name : 'rowHeadName'
	}, {
		name : 'isShowTotal'
	}, {
		name : 'columnNum'
	}]);

	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'manager/findSmallItemReportList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list'
		}, rec)
	});
	ds.on('load', function(e, records) {
		dateType = records[0].data.dataType;
	});
	ds.load({
		params : {
			start : 0,
			limit : 99999999,
			typeCode:"query"
		}
	});

	var reportNameBox = new Ext.form.ComboBox({
		fieldLabel : '运行报表名称',
		store : ds,
		id : 'reportNameBox',
		name : 'reportName',
		valueField : "reportId",
		displayField : "reportName",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		editable : false,
		width : 250,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : true,
		emptyText : '请选择',
		resizable:true,
		listeners : {
			select : function(index, record) {
				dateType = record.data.dataType;
				setValue(dateType);
				ctbtnOut.setDisabled(true);
			}
		}
	});

	var btnQuery = new Ext.Button({
		text : '查询',
		iconCls : 'query',
		handler : query
	});
	var tbar = new Ext.Toolbar({
		id : 'tbar',
		height : 25,
		items : ['小指标报表名称：', reportNameBox, '-', '查询时间:', startTime, ' ',
				startQuarterBox, ' ', '-', btnQuery,'-',ctbtnOut]
	})
	var panel = new Ext.Panel({
		id : 'mainPanel',
		autoScroll : true,
		spilt : true, 
		autoHeight : false,
		autoWidth : false,
		border : false,
		layout : 'fit',
		bodyStyle : "padding: 0,0,0,0",
		tbar : tbar,
		items:[{
			html:'<div style="vertical-align:top;" id="changePanel"></div>',
			autoScroll:true
		}]
	})
	var view = new Ext.Viewport({
		layout : 'fit',
		margins : '0 0 0 0',
		collapsible : true,
		split : true,
		border : false,
		items : [panel]
	});
});
