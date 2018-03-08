Ext.onReady(function() {
	var dateType;// 报表类型
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
		s += "-01 ";// 取月初第一天
		// s += "-" + (t > 9 ? "" : "0") + t + " ";
		if (v == 1) {
			s += "00 ";//
			// t = d.getHours();
			// s += (t > 9 ? "" : "0") + t;
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
	// add by wpzhu 20100727-------
	function numberFormat(v) {
		if (v == 0 || v == null || v == "")
			return 0.00;
		v = (Math.round((v - 0) * 100)) / 100;
		v = (v == Math.floor(v)) ? v + ".00" : ((v * 10 == Math.floor(v * 10))
				? v + "0"
				: v);
		v = String(v);
		var ps = v.split('.');
		var whole = ps[0];
		var sub = ps[1] ? '.' + ps[1] : '.00';
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
	// -------------------------------------------
	function query() {
		if ((startTime.getValue() > endTime.getValue())
				|| (startTime.getValue() == endTime.getValue() && startQuarterBox
						.getValue() > endQuarterBox.getValue())) {
			Ext.Msg.alert('提示', '选择的结束时间应大于等于开始时间！');
			return false;
		}
		Ext.Msg.wait("正在查询数据!请等待...");
		Ext.Ajax.request({
			url : 'manager/findAccountItemDataList.action',
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
				var o = new Ext.data.Record({
							date : '<font color="red">合计</font>'
						})
				for (var j = 1; j < json.fieldsNames.length; j++) {
					var sum = 0;
					var count = 0;
					var max;
					var min;
					var cname = json.fieldsNames[j].name
					var method = json.fieldsNames[j].method;
					// var method='4';
					// add by wpzhu 20100727--------------------------
					var countSum = 0;
					var total = 0;
					var aa = 0;

					for (var i = 0; i < ds.getCount(); i++) {
						var rec = ds.getAt(i);

						if ((rec.get(cname) < 0)) {
							countSum++;

						}
						if (rec.get(cname) > 0) {
							aa++;
						}
						if (rec.get(cname) == "" || rec.get(cname) == null) {
							total++;
						}
					}
					if (countSum > 0) {
						max = -9999999999;
						min = 99999999999;
					} else {
						max = ds.getAt(0).get(cname);
						min = ds.getAt(0).get(cname);
					}
					if ((max == null || max == "") && countSum == 0) {
						max = 0.00;

					}
					if (min == null || min == "" && countSum == 0) {
						min = 0.00;
					}
					if ((min == null || min == "") && total > 0
							&& countSum == 0 && aa > 0) {
						min = 999999999999999999;
					}
					// --------------------------------------------------------------------------------------------
					for (var i = 0; i < ds.getCount(); i++) {// modify by
						// wpzhu
						var rec = ds.getAt(i);

						if (rec.get(cname) != "" && rec.get(cname) != 0
								&& rec.get(cname) != "0") {
							// min=rec.get(cname);
							// max=rec.get(cname);
							count++;
							sum += rec.get(cname)
							if (max < rec.get(cname))
								max = rec.get(cname);
							if (min > rec.get(cname))
								min = rec.get(cname);
						}
					}

					if (method == '1') {
						o.set(cname, count == '0' ? 0 : numberFormat(sum
										/ count))
					};// modify by wpzhu // modify by ywliu 20090911
					if (method == '2')
						o.set(cname, numberFormat(sum));
					if (method == '3')
						o.set(cname, max);
					if (method == '4')
						o.set(cname, min);

				}
				ds.add(o);
				var wd = Ext.get('gridDiv').getWidth()
				var ht = Ext.get('gridDiv').getHeight()
				grid = new Ext.grid.GridPanel({
					renderTo : 'gridDiv',
					id : 'grid',
					split : true,
					width : wd,
					height : ht,
					autoScroll : true,
					border : false,
					// cm : new Ext.grid.ColumnModel(json.columModle),
					cm : new Ext.grid.ColumnModel({
								columns : json.columModle,
								// defaultSortable : true,
								rows : json.rows
							}),
					plugins : [new Ext.ux.plugins.GroupHeaderGrid(null,
							json.rows)],
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
		if (startTime.getValue == "" || startTime.getValue == null
				|| endTime.getValue() == "" || endTime.getValue == null)
			Msg = "开始时间和结束时间不能为空！";

		if (reportType == 5) {
			if (startTime.getValue() == endTime.getValue()
					&& startQuarterBox.getValue() < endQuarterBox.getValue()) {
				Msg = "选择结束时间应大于等于开始时间！";
				if (startTime.getValue() < endTime.getValue())
					if (startQuarterBox.getValue == ""
							|| startQuarterBox.getValue == null
							|| endQuarterBox.getValue() == ""
							|| endQuarterBox.getValue == null)
						Msg = "开始时间和结束时间不能为空！";
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
				// value : getDate(),
				width : 100,
				listeners : {
					focus : function() {
						var format = "";
						if (dateType == 1)
							format = "yyyy-MM-dd HH";
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
				name : 'accountCode'
			}, {
				name : 'accountName'
			}, {
				name : 'accountType'
			}]);
	var reportReader = new Ext.data.JsonReader({
				root : "list"
			}, reportNameData);
	var reportNameStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manager/getBpCAnalyseAccountList.action'
						}),
				reader : reportReader
			});
	reportNameStore.on('load', function(e, records) {
				dateType = records[0].data.accountType;
			});

	reportNameStore.load({
				params : {
					type : 'query',
					start : 0,
					limit : 99999999
				}
			});

	var reportNameBox = new Ext.form.ComboBox({
				fieldLabel : '运行报表名称',
				store : reportNameStore,
				id : 'reportNameBox',
				name : 'reportName',
				valueField : "accountCode",
				displayField : "accountName",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : true,
				resizable : true,
				emptyText : '请选择',
				listeners : {
					select : function(index, record) {
						dateType = record.data.accountType;
						setValue(dateType);
					}
				}
			});

	var btnQuery = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : query
			});
	function getAttri(jsonData, columnName) {
		for (var attr in jsonData) {
			if (attr == columnName)
				return jsonData[attr]
		}
		return "";
	}

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
				var temp;
				if (j == 0 && dateType == '4') {
					temp = rc.get(v_name).substring(0, 4) + "­"
							+ rc.get(v_name).substring(5, 7);
				} else {
					temp = rc.get(v_name)
				}
				html.push('<td>' + temp + '</td>');
			}
			html.push('</tr>');
		}
//		alert(html);
		/*
		 * for (k = 1; k <= gridnum; k++) { var myComlumModel=getAttri(json,
		 * "columModle" + k); html.push('<tr>'); for (var i = 0; i <
		 * myComlumModel.length; i += 1) { html.push('<th>' +
		 * myComlumModel[i].header + '</th>'); } html.push('</tr>'); for
		 * (var i = 0; i < ds.getCount(); i += 1) { var rc = ds.getAt(i);
		 * html.push('<tr>'); for (var j = 0; j < myComlumModel.length; j +=
		 * 1) { var v_name = myComlumModel[j].dataIndex; html.push('<td>' +
		 * rc.get(v_name) + '</td>'); } html.push('</tr>'); } }
		 */
		html.push('</table>');
		html = html.join(''); // 最后生成的HTML表格
		tableToExcel(html);
	}
	var tbar = new Ext.Toolbar({
				id : 'tbar',
				height : 25,
				items : ['运行报表名称：', reportNameBox, '-', '时段:', startTime, ' ',
						startQuarterBox, ' ', '至:', endTime, ' ',
						endQuarterBox, '-', btnQuery, '-', btnOutput]
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
