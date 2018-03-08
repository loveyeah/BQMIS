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
	var date=new Date();
	var startdate=date.getFirstDateOfMonth();
	var enddate=date.getLastDateOfMonth();
	var sdate=ChangeDateToString(startdate);
	var edate=ChangeDateToString(enddate);
	
	var unitStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/findUintProfessionList.action'
		}),
		reader : new Ext.data.JsonReader({}, [{
			name : 'specialityCode'
		}, {
			name : 'specialityName'
		}])
	});
	unitStore.load();
	unitStore.on("load", function(ds, records, o) {
		unitBox.setValue(records[0].data.specialityCode);
	});

	var unitBox = new Ext.form.ComboBox({
		fieldLabel : '所属专业',
		store : unitStore,
		valueField : "specialityCode",
		displayField : "specialityName",
		mode : 'local',
		triggerAction : 'all',
		forceSelection : true,
		hiddenName : 'specialityCode',
		editable : false,
		allowBlank : false,
		selectOnFocus : true,
		id : 'unitBoxspecialityCode',
		// name : 'specialityCode',
		width : 150
	});
	var startDate = new Ext.form.DateField({
		format : 'Y-m-d',// 此处换为'Y'即可
		name : 'startDate',
		// value : '2008-06',
		id : 'startDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		checked : true,
		value:sdate,
		emptyText : '请选择',
		width : 150
	});
	var endDate = new Ext.form.DateField({
		format : 'Y-m-d',// 此处换为'Y'即可
		name : 'endDate',
		 value : edate,
		id : 'endDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		checked : true,
		emptyText : '请选择',
		width : 150
	});

	var tbar = new Ext.Toolbar({
		items : ['专业：', unitBox, '-', '查询时间：', startDate, '~', endDate, '-', {
			id : 'btnReflesh',
			text : "查询",
			iconCls : 'query',
			handler : function() {
				var startdate = Ext.get("startDate").dom.value;
				var enddate = Ext.get("endDate").dom.value;
				Ext.Ajax.request({
					url : 'runlog/dutyQuery.action',
					params : {
						startDate : startdate,
						endDate : enddate,
						specialityCode : unitBox.value,
						specialityName : Ext.get("unitBoxspecialityCode").dom.value
					},
					method : 'post',
					waitMsg : '正在加载数据...',
					success : function(result, request) {
						var responseArray = Ext.util.JSON
								.decode(result.responseText);
						if (responseArray.success == true) {
							var o = eval('(' + result.responseText + ')');
							creategrid(o.json);
						} else {
							var o = eval('(' + result.responseText + ')');
							Ext.MessageBox.show({
								title : '错误',
								msg : o.errorMsg,
								buttons : Ext.Msg.OK,
								icon : Ext.MessageBox.ERROR
							});
						}
					},
					failure : function(result, request) {
						var o = eval('(' + result.responseText + ')');
						Ext.MessageBox.show({
							title : '错误',
							msg : "服务器发生异常...",
							buttons : Ext.Msg.OK,
							icon : Ext.MessageBox.ERROR
						});
					}
				});
			}
		}]
	});
	var json = {
		'columModle' : [],
		'data' : [],
		'fieldsNames' : []
	};
	creategrid(json);
	var v;
	var grid;
	function creategrid(json) {
		var cm = new Ext.grid.ColumnModel({
			columns : json.columModle,
			defaultSortable : false
		});
		var ds = new Ext.data.JsonStore({
			data : json.data,
			fields : json.fieldsNames
		});
			grid = new Ext.grid.GridPanel({
			el : 'orgGrid',
			split : true,
			border : false,
			cm : cm,
			ds : ds,
			width : Ext.get("orgGrid").getWidth(),
			enableColumnMove : false,
			tbar : tbar
		});
		//grid.render();
		v = new Ext.Viewport({
			layout : 'fit',
			split : true,
			items : [grid]
		});
	};
});
function changeColor(value) {
	return "<span style='color:green;'>"+value+"</span>";
}