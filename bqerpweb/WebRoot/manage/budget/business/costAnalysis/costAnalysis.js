Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	
	var timeFlag = 1;
	
	function numberFormat(value) {
		value = String(value);
		if (value == null || value == "null") {
			value = "0";
		}
		// 整数部分
		var whole = value;
		// 小数部分
		var sub = ".00";
		// 如果有小数
		if (value.indexOf(".") > 0) {
			whole = value.substring(0, value.indexOf("."));
			sub = value.substring(value.indexOf("."), value.length);
			sub = sub + "00";
			if (sub.length > 3) {
				sub = sub.substring(0, 3);
			}
		}
		var r = /(\d+)(\d{3})/;
		while (r.test(whole)) {
			whole = whole.replace(r, '$1' + ',' + '$2');
		}
		v = whole + sub;
		if (whole == null || whole == "null" || whole == "") {
			v = "0.00";
		}
		return v;
	}
	function getDate() {
		var d, s, t,day;
		d = new Date();
		s = d.getFullYear().toString() + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		s +='-';
		day = d.getDate()
		s +=(day > 9 ? "" : "0") + day;
		return s;
	}
	function getMonth()
	{
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString() + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;		
		return s;
	}
	function getYear()
	{
		var d, s;
		d = new Date();
		s = d.getFullYear().toString();
		return s;
	}
	
	// 年
	var year = new Ext.form.Radio({
		id : 'year',
		name : 'time',
		boxLabel : '年',
		checked : true,
		hideLabel : true
	})
	
	// 月
	var month = new Ext.form.Radio({
		id : 'month',
		name : 'time',
		boxLabel : '月',
		hideLabel : true
	})
	// 日
	var day = new Ext.form.Radio({
		id : 'day',
		name : 'time',
		boxLabel : '日',
		hideLabel : true
	})
	
	var time = new Ext.form.TextField({
		id : 'timeField',
		readOnly : true,
		width : 100,
		style : 'cursor:pointer',
		value : getYear(),
		listeners : {
			focus : function(){
				var fmt = '';
				if(timeFlag == 1)
					fmt = 'yyyy';
				else if(timeFlag == 2)
					fmt = 'yyyy-MM';
				else 
					fmt = 'yyyy-MM-dd';
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : fmt,
					isShowClear : false,
					alwaysUseStartDate : false
				});
				this.blur();
			}
		}
	})
	year.on('check',function(){
		if(year.getValue() == true)
		{
			timeFlag = 1;
			time.setValue(getYear())
		}
		if(month.getValue() == true)
		{
			timeFlag = 2;
			time.setValue(getMonth())
		}
		if(day.getValue() == true)
		{
			timeFlag = 3;
			time.setValue(getDate())
		}
	})
	var queryBtn = new Ext.Button({
		id : 'queryBtn',
		iconCls : 'query',
		text : '查询',
		handler : query
	})
	
	var saveBtn = new Ext.Button({
		id : 'saveBtn',
		iconCls : 'save',
		text : '保存修改',
		handler : saveRec
	})
	var cancel = new Ext.Button({
		id : 'cancelBtn',
		iconCls : 'cancer',
		text : '取消',
		handler : cancelRec
	})
	
	function query()
	{
		ds.baseParams = {
			time : time.getValue()
		}
		ds.load({
			params : {
				start : 0,
				limit : 18
			}
		})
	}
	
	function saveRec()
	{
		if(ds.getTotalCount() == 0)
		{
			Ext.Msg.alert('提示','无数据保存！');
			return;
		}
		Ext.Msg.confirm('提示','确认要保存修改数据？',function(id){
			if(id == 'yes')
			{
				var addData = new Array();
				var updateData = new Array();
				
				for(var i = 0; i < ds.getTotalCount(); i++)
				{
					var rec = ds.getAt(i);
					if(rec.get('costId') == null || rec.get('costId') == '')
						addData.push(rec.data);
					else
						updateData.push(rec.data)
				}
				Ext.Ajax.request({
					url : 'managebudget/saveAllCostMod.action',
					method : 'post',
					params : {
						add : Ext.util.JSON.encode(addData),
						update : Ext.util.JSON.encode(updateData)
					},
					success : function(result,request){
						var obj = eval('(' + result.responseText + ')');
						Ext.Msg.alert('提示',obj.msg);
						ds.rejectChanges();
						ds.reload();
					},
					failure : function(result,request){
						Ext.Msg.alert('提示','数据保存失败！');
						ds.reload();
					}
				})
			}
		})
	}
	function cancelRec()
	{
		var mod = ds.getModifiedRecords();
		if(mod.length > 0)
		{
			Ext.Msg.confirm('提示','确认取消所做修改？',function(id){
				if(id == 'yes')
				{
					ds.rejectChanges();
					ds.reload();
				}
			})	
		}
	}
	var item = new Ext.data.Record.create([{
		name : 'costId'
	},{
		name : 'analyseDate'
	},{
		name : 'itemId'
	},{
		name : 'factValue'
	},{
		name : 'memo'
	},{
		name : 'itemName'
	},{
		name : 'comeFrom'
	},{
		name : 'itemType'
	},{
		name : 'isUse'
	}]);
	
	var ds = new Ext.data.JsonStore({
		url : 'managebudget/getCostAnalysisList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : item
	})
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	})

	var grid = new Ext.grid.EditorGridPanel({
		id : 'grid',
		region : "center",
		frame : false,
		border : false,
		autoScroll : true,
		sm : sm,
		ds : ds,
		layout : 'fit',
		clicksToEdit : 1,
		tbar : ['类型：',year,month,day,time,'-',queryBtn,'-',saveBtn,'-',cancel],
		columns : [sm,new Ext.grid.RowNumberer({
			header : '行号',
			width : 31
		})
		,{
			header : '时间',
			align : 'center',
			sortable : true,
			dataIndex : 'analyseDate'
		},{
			header : '指标',
			align : 'center',
			sortable : true,
			dataIndex : 'itemName'
		},{
			header : '类型',
			align : 'center',
			sortable : true,
			dataIndex : 'itemType',
			renderer : function(v){
				if(v == 1)
					return '年';
				else if(v == 2)
					return '月';
				else if(v == 3)
					return '日';
			}
		},{
			header : '产生方式',
			align : 'center',
			sortable : true,
			dataIndex : 'comeFrom',
			renderer : function(v){
				if(v == 1)
					return '录入';
				else if(v == 2)
					return '采集';
				else if(v == 3)
					return '计算';
			}
		},{
			header : '实际值',
			align : 'center',
			sortable : true,
			dataIndex : 'factValue',
			editor : new Ext.form.NumberField(),
			renderer : function(v){
				return numberFormat(v);
			}
		},{
			header : '备注',
			align : 'center',
			sortable : true,
			width : 150,
			dataIndex : 'memo',
			editor : new Ext.form.TextField()
		}
		],
		bbar : new Ext.PagingToolbar({
									pageSize : 18,
									store : ds,
									displayInfo : true,
									displayMsg : "第{0} 条到第 {1} 条/共 {2} 条",
									emptyMsg : "没有记录"
								})
	})
	grid.on('beforeedit',function(e){
		if(e.record.get('comeFrom') != '1')
		{
			if(grid.getColumnModel().getDataIndex(e.column) == 'factValue')
				return false;
		}
	})
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		border : false,
		frame : false,
		items : [{
			layout : 'fit',
			border : false,
			frame : false,
			region : "center",
			items : [grid]
		}]
	});
	query();
});
