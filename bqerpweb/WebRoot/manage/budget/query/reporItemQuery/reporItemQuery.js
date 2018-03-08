
// author:chenshoujiang
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
	// --------add by fyyang 090810----月份
	/**
	 * 月份Field
	 */
	var reportTypeStore = new Ext.data.SimpleStore({

				data : [['预算参数设置表', '1'], ['收入利润预算表', '2'], ['成本费用预算表', '3'],
						['燃料费用预算表', '4'], ['材料费预算明细表', '5'],
						['修理费支出预算明细表', '6'], ['工资及福利费预算明细表', '7'],
						['其他费用预算明细表', '8'], ['财务费用预算明细表', '9']],
				fields : ['name', 'value']
			});

	var reportTypeBox = new Ext.form.ComboBox({
				fieldLabel : '报表:',
				store : reportTypeStore,
				name : 'reportTypeBox',
				valueField : "value",
				displayField : "name",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'statItem.dataTimeType',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : true,
				emptyText : '请选择',
				anchor : '85%'
			});

	// 获得年份
	function getDate() {
		var Y;
		Y = new Date();
		Y = Y.getFullYear().toString(10);
		return Y;
	}

	// 获得月份
	function getMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString() + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		return s;
	}

	// 年月份选择
	var formatType;
	var yearRadio = new Ext.form.Radio({
				id : 'year',
				name : 'queryWayRadio',
				hideLabel : true,
				boxLabel : '年份'
			});
	var monthRadio = new Ext.form.Radio({
				id : 'month',
				name : 'queryWayRadio',
				hideLabel : true,
				boxLabel : '月份',
				checked : true,
				listeners : {
					check : function() {
						var queryType = getChooseQueryType();
						//				
						switch (queryType) {
							case 'year' : {
								formatType = 1;
								time.setValue(getDate());
								break;
							}
							case 'month' : {
								time.setValue(getMonth());
								formatType = 2;
								break;
							}
						}
					}
				}
			});

	var monthRollRadio = new Ext.form.Radio({
				id : 'monthRoll',
				name : 'queryWayRadio',
				hideLabel : true,
				boxLabel : '月份滚动',
				listeners : {
					check : function() {
						var queryType = getChooseQueryType();
						//				
						switch (queryType) {
							case 'year' : {
								formatType = 1;
								time.setValue(getDate());
								break;
							}
							case 'month' : {
								time.setValue(getMonth());
								formatType = 2;
								break;
							}
							case 'monthRoll' : {
								formatType = 1;
								time.setValue(getDate());
								break;
							}
						}
					}
				}
			});		
			
	var time = new Ext.form.TextField({
				id : 'time',
				allowBlank : true,
				readOnly : true,
				value : getMonth(),
				width : 100,
				listeners : {
					focus : function() {
						var format = '';
						if (formatType == 1)
							format = 'yyyy';
						if (formatType == 2)
							format = 'yyyy-MM';
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : format,
									alwaysUseStartDate : false
								});
					}
				}
			});
	// -----------------------------------

	// 打印盘点表按钮
	var btnPrint = new Ext.Button({
				text : "查询",
				iconCls : 'print',
				disabled : false,
				handler : printSupply
			});

	// head工具栏
	var headTbar = new Ext.Toolbar({
				region : 'north',
				// border:false,
				items : ['报表:',reportTypeBox,'-',yearRadio, '-', monthRadio, '-',monthRollRadio,'-', time, '-', btnPrint]
			});

	// 显示区域
	// var layout = new Ext.Viewport({
	// layout : 'fit',
	// // margins : '0 0 0 0',
	// // border : false,
	// autoHeight : true,
	// items : [headTbar]
	// });

	// 遍历所有的REDIO获得ID
	function getChooseQueryType() {
		var list = document.getElementsByName("queryWayRadio");
		for (var i = 0; i < list.length; i++) {
			if (list[i].checked) {
				return list[i].id;
			}
		}
	}
	/**
	 * 物料供应打印按钮按下时
	 */
	function printSupply() {
		if(monthRollRadio.checked) {
			var strMonth = time.getValue();// .substring(0,4)+time.getValue().substring(5,7);
				url = "/powerrpt/report/webfile/bqmis/reportItemMonthRoll.jsp?dateTime="
						+ strMonth+"&reportType="+reportTypeBox.getValue()+"&title="+reportTypeBox.getRawValue();
			    url = encodeURI(url);
				document.all.iframe1.src = url;
		} else{
			if(reportTypeBox.getValue() == null || reportTypeBox.getValue() == "") {
				Ext.MessageBox.alert("提示信息","请选择报表！");
			} else {
				var strMonth = time.getValue();// .substring(0,4)+time.getValue().substring(5,7);
				url = "/powerrpt/report/webfile/bqmis/reportItemYear.jsp?dateTime="
						+ strMonth+"&reportType="+reportTypeBox.getValue()+"&title="+reportTypeBox.getRawValue();
			    url = encodeURI(url);
				document.all.iframe1.src = url;
			}
		}
		// window.open(encodeURI(strReportAdds));
	}
	var url = "";
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [{
					region : "north",
					layout : 'fit',
					height : 30,
					border : false,
					split : true,
					margins : '0 0 0 0',
					items : [headTbar]
				}, {
					region : "center",
					layout : 'fit',
					border : true,
					collapsible : true,
					split : true,
					margins : '0 0 0 0',
					html : '<iframe id="iframe1" name="iframe1"  src="'
							+ url
							+ '"  frameborder="0"  width="100%" height="100%"  />'
				}]
	});

})