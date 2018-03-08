Ext.onReady(function() { 
	var dateType;// 报表时间类型
	var code;  //报表编号  
	
	function getDate(v) {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		t = d.getDate();
		s += "-" + (t > 9 ? "" : "0") + t + " ";
		return s;
	}
	function getAttri(jsonData, columnName) {
		for (var attr in jsonData) {
			if (attr == columnName)
				return jsonData[attr]
		}
		return "";
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
		if (reportType == 'R')
			startTime.setValue(getDate()), startQuarterBox.hide();
		if (reportType == 'Y')
			startTime.setValue(exdate), startQuarterBox.hide();
		if (reportType == 'J')
			startTime.setValue(d.getFullYear()), startQuarterBox.show();
		if (reportType == 'N')
			startTime.setValue(d.getFullYear()), startQuarterBox.hide();
	}
	// 时间类型
	var startTime = new Ext.form.TextField({
		id : 'startTime', 
		allowBlank : false,
		fieldLabel : '时段', 
		width : 100,
		listeners : {
			focus : function() {
				var format = "";
				if (dateType == 'R')
					format = "yyyy-MM-dd";
				if (dateType == 'Y')
					format = "yyyy-MM";
				if (dateType == 'J')
					format = "yyyy";
				if (dateType == 'N')
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
		name : 'code'
	}, {
		name : 'memo'
	}, {
		name : 'dateType'
	}]);

	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'comm/getReportModelList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list'
		}, rec)
	});
	ds.on('load', function(e, records) {
		dateType = records[0].data.dataType;
	});
	
	ds.load();

	var reportNameBox = new Ext.form.ComboBox({
		fieldLabel : '运行报表名称',
		store : ds,
		id : 'template',
		name : 'template',
		valueField : "code", 
		displayField : "memo",
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
				dateType = record.data.dateType;
				setValue(dateType);
				
			} 
		}
	});
 
	var form = new Ext.FormPanel({
		id : 'form',
		frame : true, 
		labelAlign : 'left',
		autoWidth : true,
		autoHeight : true,
		bodyStyle : 'padding:10px',
		items : [{
			xtype : 'fieldset',
			labelAlign : 'right',
			buttonAlign : 'center',
			labelWidth : 160,
			title : 'excel报表查询',
			defaults : {
				width : 250
			},
			autoHeight : true,
			items : [reportNameBox, startTime, startQuarterBox,
			{html:'<center><a id="aQuery" href="#">[查询]</a>&nbsp;&nbsp;&nbsp;<a id="aQueryBack" href="#">[清空]</a></center>'} 
			] 
		}]
	}); 
	
	
	var view = new Ext.Viewport({
		layout : 'fit',
		margins : '0 0 0 0',
		collapsible : true,
		split : true,
		border : true,
		items : [form]
	});
	document.getElementById("aQuery").onclick=function(){ 
		var template =  reportNameBox.getValue(); 
		if(template != null && template !='')
		{
			var	date = startTime.getValue();
			
			if(date.length == 7)
			{
				var dsa = date.split("-");
		    		var cd = new Date(dsa[0],dsa[1]);
        	    		var   lastday   =   new   Date(cd.getFullYear(),cd.getMonth(),0).getDate()  ;
                    		date = date + "-"+lastday;
			} 
			var href = "../../comm/test.action?template="+template+"&date="+date; 
			//window.open(href, '_blank', 'width=200, height=50, resizable=yes, top=200, left=400');
			window.open(href, '_blank', '');
		}
		else
		{
			Ext.Msg.alert('提示','请选择报表类型!');
		}
		return false;
	}
	document.getElementById("aQueryBack").onclick=function(){ 
		form.getForm().reset();
		return false;
	} 
});