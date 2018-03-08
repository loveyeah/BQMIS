Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var d = new Date();
	var year = d.getFullYear();
	var yearDate = new Ext.form.TextField({
		id : 'yearDate',
		name : '_yearDate',
		fieldLabel : "统计年份",
		style : 'cursor:pointer',
		cls : 'Wdate',
		anchor : '90%',
		value : year,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y',
					dateFmt : 'yyyy',
					alwaysUseStartDate : true
				});
			}
		}
	});

	var btnQuery = new Ext.Button({
		id : 'btnQuery',
		text : '统  计',
		iconCls : 'query',
		minWidth : 70,
		handler : function() {
			url="/powerrpt/report/webfile/bqmis/failureBugReport.jsp?year="+Ext.get('yearDate').dom.value+"";
			document.all.iframe1.src = url;
		}
	});
	var form = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		id : 'shift-form',
		autoHeight : true,
		border : false,
		items : [{
			layout : 'column',
			border : false,
			items : [{
				border : false,
				columnWidth : 0.3,
				layout : 'form'
			}, {
				border : false,
				columnWidth : 0.2,
				layout : 'form',
				labelWidth : 60,
				items : [yearDate]
			}, {
				border : false,
				columnWidth : 0.2,
				layout : 'form',
				items : [btnQuery]
			}]
		}]
	});
	var url="/powerrpt/report/webfile/bqmis/failureBugReport.jsp?year="+year+"";
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
			items : [form]
		}, {
			region : "center",
			layout : 'fit',
			border : true,
			collapsible : true,
			split : true,
			margins : '0 0 0 0',
			html : '<iframe id="iframe1" name="iframe1"  src="' + url
					+ '"  frameborder="0"  width="100%" height="100%"  />'
		}]
	});

})