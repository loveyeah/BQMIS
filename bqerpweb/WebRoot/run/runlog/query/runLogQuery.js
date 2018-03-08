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
	var date = new Date();
	var startdate = date.add(Date.DAY, -2);
	var enddate = date;
	var sdate = ChangeDateToString(startdate);
	var edate = ChangeDateToString(enddate);

	var unitStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'runlog/findUintProfessionList.action'
						}),
				reader : new Ext.data.JsonReader({
							id : "unit"
						}, [{
									name : 'specialityCode'
								}, {
									name : 'specialityName'
								}])
			});
	unitStore.load();
	unitStore.on("load", function(xx, records, o) {
				unitBox.setValue(records[0].data.specialityCode);
				var fn = "iframe"
						+ tabpanel.getActiveTab().getId().substring(3);
				Ext.get(fn).dom.src = Ext.get(fn).dom.src;
			});

	var unitBox = new Ext.form.ComboBox({
				fieldLabel : '所属专业',
				id : 'unitBox',
				store : unitStore,
				valueField : "specialityCode",
				displayField : "specialityName",
				mode : 'remote',
				triggerAction : 'all',
				forceSelection : true,
				hiddenName : 'specialityCode',
				editable : false,
				allowBlank : false,
				selectOnFocus : true,
				name : 'specialityCode',
				width : 150,
				listeners : {
					select : function() {
						query();
					}
				}
			});
	var fromDate = new Ext.form.DateField({
				format : 'Y-m-d',// 此处换为'Y'即可
				name : 'startDate',
				// value : '2008-06',
				id : 'fromDate',
				itemCls : 'sex-left',
				clearCls : 'allow-float',
				checked : true,
				value : sdate,
				emptyText : '请选择',
				width : 150
			});
	var toDate = new Ext.form.DateField({
				format : 'Y-m-d',// 此处换为'Y'即可
				name : 'endDate',
				value : edate,
				id : 'toDate',
				itemCls : 'sex-left',
				clearCls : 'allow-float',
				checked : true,
				emptyText : '请选择',
				width : 150
			});

	function query() {
		var fn = "iframe" + tabpanel.getActiveTab().getId().substring(3);
		// document.getElementById(fn).src = document.getElementById(fn).src;
		Ext.get(fn).dom.src = Ext.get(fn).dom.src;
		// Ext.get(fn).dom.src = "http://www.baidu.com";
		// Ext.get("iframe1").dom.reload(true);
	}

	var tbar = new Ext.Toolbar({
				items : ['查询时间从：', fromDate, '到:', toDate, '-', '专业：', unitBox,
						'-', {
							id : 'btnQuery',
							text : "查询",
							iconCls : 'query',
							handler : query
						}]
			});
	var _url1 = "run/runlog/query/basicinfo/basicinfo.jsp";
	var _url2 = "run/runlog/query/recordquery/recordQuery.jsp";
	var _url3 = "run/runlog/query/workerquery/workerquery.jsp";
	var _url4 = "run/runlog/query/runeququery/runeququery.jsp";
	var _url5 = "run/runlog/query/parmquery/parmquery.jsp";
	var _url6 = "run/runlog/query/recordquery/notCompletionQuery.jsp";

	var tabpanel = new Ext.TabPanel({
				id : 'maintab',
				activeTab : 0,
				border : false,
				// tabPosition : 'bottom',
				// autoScroll : true,
				items : [{
					id : 'tab1',
					title : '基本信息',
					html : '<iframe id="iframe1" name="iframe1"  src="'
							+ _url1
							+ '"  frameborder="0"  width="100%" height="100%" />',
					listeners : {
						activate : function() {
							Ext.get("iframe1").dom.src = Ext.get("iframe1").dom.src;
							// document.getElementById("iframe1").src =
							// document.getElementById("iframe1").src;
						}
					}
				}, {
					id : 'tab2',
					title : '值班记事',
					html : '<iframe id="iframe2"  src="'
							+ _url2
							+ '"  frameborder="0"  width="100%" height="100%" />',
					listeners : {
						activate : function() {
							Ext.get("iframe2").dom.src = Ext.get("iframe2").dom.src;
							// document.getElementById("iframe2").src =
							// document.getElementById("iframe2").src;
						}
					}
				}, {
					id : 'tab3',
					title : '值班人员',
					html : '<iframe  id="iframe3" src="'
							+ _url3
							+ '"  frameborder="0"  width="100%" height="100%" />',
					listeners : {
						activate : function() {
							document.getElementById("iframe3").src = document
									.getElementById("iframe3").src;
						}
					}
				}, {
					id : 'tab4',
					title : '交接班方式',
					html : '<iframe  id="iframe4" src="'
							+ _url4
							+ '"  frameborder="0"  width="100%" height="100%" />',
					listeners : {
						activate : function() {
							document.getElementById("iframe4").src = document
									.getElementById("iframe4").src;
						}
					}
				}, {
					id : 'tab5',
					title : '交接班参数',
					html : '<iframe  id="iframe5" src="'
							+ _url5
							+ '"  frameborder="0"  width="100%" height="100%" />',
					listeners : {
						activate : function() {
							document.getElementById("iframe5").src = document
									.getElementById("iframe5").src;
						}
					}
				}, {
					id : 'tab6',
					title : '未完成项',
					html : '<iframe  id="iframe6" src="'
							+ _url6
							+ '"  frameborder="0"  width="100%" height="100%" />',
					listeners : {
						activate : function() {
							document.getElementById("iframe6").src = document
									.getElementById("iframe6").src;
						}
					}
				}]

			});
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							region : "center",
							layout : 'fit',
							collapsible : true,
							split : true,
							margins : '0 0 0 0',
							// 注入表格
							items : [tabpanel]
						}, {
							region : "north",
							layout : 'fit',
							collapsible : false,
							split : false,
							margins : '0 0 0 0',
							items : [new Ext.Panel({
										border : false,
										collapsible : true,
										tbar : tbar
									})]
						}]
			})
})
