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
	var professionRadio = new Ext.form.Radio({
		id : 'professionRadio',
		boxLabel : '按专业',
		hideLabel : true,
		name : 'queryTypeRadio',
		anchor : '100%',
		checked : true
	});
	var deptRadio = new Ext.form.Radio({
		id : 'deptRadio',
		boxLabel : '按部门',
		hideLabel : true,
		anchor : '100%',
		name : 'queryTypeRadio'
	})

	var btnQuery = new Ext.Button({
		id : 'btnQuery',
		text : '统  计',
		iconCls : 'query',
		minWidth : 70,
		handler : function() {
			var type;
			if(professionRadio.checked == true)
			{
				type=1;
			}
			else
			{
				type=2;
			}
			var url="/powerrpt/report/webfile/bqmis/failureReport.jsp?year="+Ext.get('yearDate').dom.value+"&type="+type+"";
			document.all.iframe1.src=url;
		}
	});
	var btnPrint = new Ext.Button({
		id : 'btnPrint',
		text : '打  印',
		minWidth : 70,
		handler : function() {

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
				columnWidth : 0.2,
				layout : 'form'
			},{
				border : false,
				columnWidth : 0.2,
				layout : 'form',
				labelWidth : 60,
				items : [yearDate]
			}, {
				border : false,
				columnWidth : 0.1,
				layout : 'form',
				items : [professionRadio]
			}, {
				border : false,
				columnWidth : 0.1,
				align : 'center',
				layout : 'form',
				items : [deptRadio]
			}, {
				border : false,
				columnWidth : 0.1,
				align : 'center',
				layout : 'form',
				items : [btnQuery]
			}, {
				border : false,
				columnWidth : 0.1,
				align : 'center',
				layout : 'form'
				//items : [btnPrint]
			}]
		}]
	});
 	var url="/powerrpt/report/webfile/bqmis/failureReport.jsp?year="+year+"&type=1";  
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