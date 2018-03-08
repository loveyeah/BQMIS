Ext.onReady(function() {
	var dateType;// 报表类型
	var m = new Map();
	var grid;
	var ds;
	function getDate(v) {
		var d, s, t;
		d = new Date();
		d.setDate(d.getDate() - 1);
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
	function getEndDate(v) {
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
	function saveData() {
		grid.stopEditing();
		var modifyRec = grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0) {
			Ext.Msg.confirm('提示', '确定要保存修改吗?', function(b) {
				if (b == 'yes' && m != null) {
					Ext.Ajax.request({
								url : 'manager/saveEntryValue.action',
								method : 'post',
								params : {
									isUpdate : Ext.util.JSON.encode(m.values()),
									timeType : dateType
								},
								success : function(result, request) {
									var json = eval('(' + result.responseText
											+ ')');
									Ext.MessageBox.alert('提示信息', json.msg);
									query();
									m = new Map();
								},
								failure : function(result, request) {
									Ext.MessageBox.alert('提示信息', '操作失败！')
								}
							})
				}
			})
		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}
	function query() {
		if (reportNameBox.getValue() == "" || startTime.getValue() == "") {
			Ext.Msg.alert('提示信息', '运行报表名称，日期必填');
			return;
		};
		if (startTime.getValue() == null || startTime.getValue() == '') {
			Ext.Msg.alert('提示', '选择日期不能为空！');
			return false;
		}
		
		Ext.Msg.wait("正在查询数据!请等待...");
		Ext.Ajax.request({
			url : 'manager/findRunReportEntryList.action',
			params : {
				reportCode : reportNameBox.getValue(),
				dateType : dateType,
				startTime : getTime(dateType, true),
				endTime : getTime(dateType, false),
				method : "query"
			},
			method : 'post',
			success : function(result, request) {
				json = eval('(' + result.responseText + ')');
				document.getElementById("gridDiv").innerHTML = "";
				ds = new Ext.data.JsonStore({
							data : json.data,
							fields : json.fieldsNames
						});
				var wd = Ext.get('gridDiv').getWidth()
				var ht = Ext.get('gridDiv').getHeight()
				grid = new Ext.grid.EditorGridPanel({
					renderTo : 'gridDiv',
					id : 'grid',
					split : true,
					width : wd,
					height : ht,
					autoScroll : true,
					border : false,
					// cm : new Ext.grid.ColumnModel(getAttri(json, "columModle"
					// + i)),
					cm : new Ext.grid.ColumnModel({
								columns : json.columModle,
								// defaultSortable : true,
								rows : json.rows
							}),
					enableColumnMove : false,
					// viewConfig : {
					// forceFit : true
					// },
					plugins : [new Ext.ux.plugins.GroupHeaderGrid()],
					ds : ds,
					clicksToEdit : 1,
					listeners : {
						'beforeedit' : function(e) {
							var type = e.record.get(e.field + 'type');
							grid
									.getColumnModel()
									.setEditor(
											e.column,
											new Ext.grid.GridEditor(new Ext.form.NumberField(
													{
														allowDecimals : true,
														decimalPrecision : type
													})));

						},
						'afteredit' : function(e) {
							var v1 = new Object();
							v1.itemCode = e.field;
							v1.date = e.record.get("date");
							v1.value = e.record.get(e.field);
							m.put(e.field + e.record.get("date"), v1);
						}

					}
				});
				grid.render();
				btnSave.show();
				Ext.Msg.hide();
			},
			failure : function(result, request) {
				Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
				Ext.Msg.hide();
			}
		});
		btnOutput.setDisabled(false);

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
			startTime.setValue(getDate(1)), startQuarterBox.hide(), endTime
					.setValue(getEndDate(1)), endQuarterBox.hide();
		if (reportType == 2)
			startTime.setValue(getDate(2)), startQuarterBox.hide(), endTime
					.setValue(getEndDate(2)), endQuarterBox.hide();
		if (reportType == 3)
			startTime.setValue(getDate(3)), startQuarterBox.hide(), endTime
					.setValue(getEndDate(3)), endQuarterBox.hide();
		if (reportType == 4)
			startTime.setValue(exdate), startQuarterBox.hide(), endTime
					.setValue(exdate), endQuarterBox.hide();
		if (reportType == 5)
			startTime.setValue(d.getFullYear()), startQuarterBox.show(), endTime
					.setValue(d.getFullYear()), endQuarterBox.show();
		if (reportType == 6)
			startTime.setValue(d.getFullYear()), startQuarterBox.hide(), endTime
					.setValue(d.getFullYear()), endQuarterBox.hide();
	}
	function validate(reportType) {
		var Msg = "";
		if (startTime.getValue() < endTime.getValue())
			Msg = "选择结束时间应大于等于开始时间！";
		if (reportType == 5) {
			if (startTime.getValue() == endTime.getValue()
					&& startQuarterBox.getValue() < endQuarterBox.getValue()) {
				Msg = "选择结束时间应大于等于开始时间！";
			}
		}
		if (reportType == 2) {
			if (startTime.getValue() != endTime.getValue()) {
				Msg = "班组指标维护时段为一天"
			}
		}

	}
	function getTime(reportType, isStart) {
		var returnTime;
		var time = startTime;
		var quarterBox = startQuarterBox;
		if (!isStart)
			time = endTime, quarterBox = endQuarterBox;
		if (reportType == 1)
			returnTime = time.getValue();
		if (reportType == 2)
			returnTime = time.getValue();
		if (reportType == 3)
			returnTime = time.getValue();
		if (reportType == 4)
			returnTime = time.getValue() + "-01";
		if (reportType == 5) {
			returnTime = time.getValue();
			var quarter = quarterBox.getValue();
			if (quarter == 1)
				returnTime += "-01-01";
			if (quarter == 2)
				returnTime += "-04-01";
			if (quarter == 3)
				returnTime += "-07-01";
			if (quarter == 4)
				returnTime += "-10-01";
		}
		if (reportType == 6)
			returnTime = time.getValue() + "-01-01";
		return returnTime;
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
						if (dateType == 2)
							format = "yyyy-MM-dd";
						if (dateType == 3)
							format = "yyyy-MM-dd";
						if (dateType == 4)
							format = "yyyy-MM";
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
	var endTime = new Ext.form.TextField({
				id : 'endTime',
				allowBlank : false,
				fieldLabel : '至：',
				hidden : true,
				// value : getDate(),
				width : 100,
				listeners : {
					focus : function() {
						var format = "";
						if (dateType == 1)
							format = "yyyy-MM-dd HH";
						if (dateType == 2)
							format = "yyyy-MM-dd";
						if (dateType == 3)
							format = "yyyy-MM-dd";
						if (dateType == 4)
							format = "yyyy-MM";
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
				width : 60,
				selectOnFocus : true,
				value : 1
			});
	var endQuarterBox = new Ext.form.ComboBox({
				fieldLabel : '季度',
				store : [['1', '第一季'], ['2', '第二季'], ['3', '第三季'], ['4', '第四季']],
				id : 'endQuarterBox',
				name : 'endQuarterBoxName',
				valueField : "value",
				displayField : "text",
				mode : 'local',
				typeAhead : true,
				hidden : true,
				forceSelection : true,
				hiddenName : 'endQuarterBoxName',
				editable : false,
				triggerAction : 'all',
				width : 60,
				selectOnFocus : true,
				value : 1
			});
	var reportNameData = Ext.data.Record.create([{
				name : 'reportName'
			}, {
				name : 'reportCode'
			}, {
				name : 'reportType'
			}, {
				name : 'timeDelay'
			}, {
				name : 'timeUnit'
			}]);
	var reportReader = new Ext.data.JsonReader({
				root : "list"
			}, reportNameData);
	var reportNameStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manager/getBpCInputReportList.action'
						}),
				reader : reportReader
			});
	reportNameStore.on('load', function(e, records) {
//				dateType = records[0].data.reportType;
		dateType = 2;
				reportNameStore.each(function(e){
					if(e.get('reportType') != 2)
						reportNameStore.remove(e);
				})
			});

	reportNameStore.load({
				params : {
					type : 'lr',
					start : 0,
					limit : 99999999
				}
			});

	var reportNameBox = new Ext.form.ComboBox({
				fieldLabel : '运行报表名称',
				store : reportNameStore,
				id : 'reportNameBox',
				name : 'reportName',
				valueField : "reportCode",
				displayField : "reportName",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : true,
				emptyText : '请选择',
				resizable : true,
				listeners : {
					select : function(index, record) {
						dateType = record.data.reportType;
						setValue(dateType);
					}
				}
			});

	var btnQuery = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : query
			});
	var btnSave = new Ext.Button({
				text : '数据保存',
				iconCls : 'save',
				hidden : true,
				handler : saveData
			});
	var btnOutput = new Ext.Button({
				id : 'export',
				text : '导出',
				disabled : true,
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
		var count = (json.data != "") ? json.columModle.length : 5;
		var html = ['<table border=1><tr ><th colspan =' + count
				+ '><center><font size="3">' + ItemName
				+ '</font></center></th></tr>'];
		var firstColmLength = json.rows[0].length;
		// /alert(json.rows[0][15].header);
		html.push('<tr>');
		for (k = 0; k < firstColmLength; k++) {
			if (json.rows[0][k] == 'undifined' || json.rows[0][k] == null) {
				html.push('<th>' + null + '</th>');
			} else {
				html.push('<th colspan=' + json.rows[0][k].colspan + '>'
						+ json.rows[0][k].header + '</th>');
			}

		}

		html.push('</tr>');
		var secondColmLength = json.columModle.length;
		html.push('<tr>');
		for (j = 0; j < secondColmLength; j++) {
			if (json.columModle[j] == 'undifined' || json.columModle[j] == null) {
				html.push('<th>')
				html.push('</th>');
			} else {
				html.push('<th>' + json.columModle[j].header + '</th>');
			}
		}
		html.push('</tr>');
		for (var i = 0; i < ds.getCount(); i += 1) {
			var rc = ds.getAt(i);
			html.push('<tr>');
			for (var j = 0; j < secondColmLength; j += 1) {
				var v_name = json.columModle[j].dataIndex;
				html.push('<td>' + rc.get(v_name) + '</td>');
			}
			html.push('</tr>');
		}
		html.push('</table>');
		html = html.join(''); // 最后生成的HTML表格
		tableToExcel(html);
	}

	var tbar = new Ext.Toolbar({
				id : 'tbar',
				height : 25,
				items : ['运行报表名称：', reportNameBox, '-', '日期:', startTime, ' ',
						startQuarterBox, ' ', endTime, ' ',
						endQuarterBox, '-', btnQuery, '-', btnSave, '-',
						btnOutput]
			})
	var view = new Ext.Viewport({
				layout : 'fit',
				margins : '0 0 0 0',
				collapsible : true,
				split : true,
				border : false,
				items : [new Ext.Panel({
							id : 'panel',
							border : false,
							tbar : tbar,
							items : [{
										html : '<div id="gridDiv"></div>'
									}]
						})]
			});
	Ext.get('gridDiv').setWidth(Ext.get('panel').getWidth());
	Ext.get('gridDiv').setHeight(Ext.get('panel').getHeight() - 25);
});
