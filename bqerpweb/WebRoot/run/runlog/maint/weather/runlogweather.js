Ext.BLANK_IMAGE_URL = 'ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var gridForm = new Ext.FormPanel({
		id : 'weather-form',
		frame : true,
		autoWidth : true,
		autoHeight : true,
		align : 'center',
		labelAlign : 'left',
		items : [{
			xtype : 'fieldset',
			labelAlign : 'right',
			title : '天气状况维护',
			defaultType : 'textfield',
			autoHeight : true,
			border : true,
			items : [
			{ 
				name : 'weather.weatherKeyId',
				xtype : 'hidden'
			},
			{			 
				fieldLabel : '天气编码',
				allowBlank : false,
				anchor : '90%',
				name : 'weather.weatherCode'
			},
			{			 
				fieldLabel : '天气说明',
				allowBlank : true,
				anchor : '90%',
				name : 'weather.weatherName'
			},
			{			 
				fieldLabel : '显示顺序',
				allowBlank : false,
				anchor : '90%',
				xtype : 'numberfield',
				name : 'weather.diaplayNo'
			}],
			buttons : [{
				text : '保存',
				iconCls : 'save',
				handler : function() {
					if (gridForm.getForm().isValid()) {
						gridForm.getForm().submit({
							url : 'runlog/'+(Ext.get("weather.weatherKeyId").dom.value==""?"addWeather":"updateWeather")+'.action',
							waitMsg : '正在保存数据...',
							method : 'post',
								success : function(form, action) {

						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("注意", o.msg);
						ds.load();
						win.hide(); 
					},
					faliue : function() {
						Ext.Msg.alert('错误', '出现未知错误.');
					}
				});
					}
				}
			}, {
				text : '取消',
				iconCls:'cancer',
				handler : function() {
					win.hide(); 
				}
			}]
		}]
	});
	

	var weather = new Ext.data.Record.create([
	{name : 'weatherKeyId'},
    {name : 'weatherCode'},
	{name : 'weatherName'},
	{name : 'diaplayNo'},
	{name : 'isUse'},
	{name : 'enterpriseCode'}]);
	
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/findWeatherList.action',
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, weather)
	});
	ds.load();
	
	var box3= new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) { }
		}
	});

	var colModel = new Ext.grid.ColumnModel([	
	box3,{
			id : 'weatherKeyId',
			header : 'ID',
			hidden:true,
			dataIndex : 'weatherKeyId',
			sortable : true,
			width : 100
		},{
		id : 'weatherCode',
		header : '天气编码',
		dataIndex : 'weatherCode',
		width : 150,
		sortable : true
	}, {
		header : '天气说明',
		dataIndex : 'weatherName',
		width : 250
	}, 
	{
		header : '使用状态',
		dataIndex : 'isUse',
		width : 100,
		renderer : function changeIt(val) {
					return (val == "Y") ? "使用" : "停用";
				}
	}, {
		header : '显示顺序',
		dataIndex : 'diaplayNo',
		width : 100
	}]);
	
		var fuuzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});
	
	colModel.defaultSortable = true;
	var tbar = new Ext.Toolbar({
		items : [fuuzy, {
			text : "查询",
			iconCls : 'query',
			handler : function() {
				var fuzzytext = fuuzy.getValue();
				ds.baseParams = {
					fuzzy : fuzzytext
				};
				ds.load();
			}
		},'-',{
			id : 'btnAdd',
			text : "新增",
			iconCls : 'add',
			handler : function() { 
				win.show();
				gridForm.getForm().reset();
//				Ext.get("weather.weatherCode").dom.focus();
			}
		}, '-', {
					id : 'btnUpdate',
					text : "修改",
					iconCls : 'update',
					handler : function() {
			var rec = weatherGrid.getSelectionModel().getSelections();
		    if(rec.length!=1){
			    Ext.Msg.alert("提示","请选择一行记录!");
			    return false;
		    }
		    else{
							win.show();
							var rec = weatherGrid.getSelectionModel().getSelected();
							Ext.get("weather.weatherKeyId").dom.value=rec.get("weatherKeyId");
							Ext.get("weather.weatherCode").dom.value=rec.get("weatherCode");
							Ext.get("weather.weatherName").dom.value=rec.get("weatherName");
							Ext.get("weather.diaplayNo").dom.value=rec.get("diaplayNo");						
							
						} 
					}
				}, '-', {
			id : 'btnDelete',
			text : "删除",
			iconCls : 'delete',
			handler : function() {
				var rec = weatherGrid.getSelectionModel().getSelections();
				var names = "";
				if (rec.length == 0) {
					Ext.Msg.alert("提示", "请选择要删除的记录！");
				} else {
					for (i = 0; i < rec.length; i++) {
						names += rec[i].data.weatherName + ",";
					}
					names = names.substring(0, names.length - 1);

					if (confirm("确定要删除\"" + names + "\"天气吗？")) {
						for (i = 0; i < rec.length; i++) {
							Ext.Ajax.request({
								url : 'runlog/deleteWeather.action?weather.weatherKeyId='
										+ rec[i].get("weatherKeyId"),

								method : 'post',
								waitMsg : '正在删除数据...',
								success : function(result, request) {
									Ext.Msg.alert('提示', '删除成功!');
									ds.load();
								},
								failure : function(result, request) {
									Ext.Msg.alert('提示', '删除失败!');
								}
							});
						}
					}
				}
			}
		}, '-', {
			id : 'btnReflesh',
			text : "刷新",
			iconCls : 'reflesh',
			handler : function() {
				fuuzy.setValue("");
				ds.baseParams = {
					fuzzy : ""
				};
				ds.load();				
			}
		}]
	});


	
	var weatherGrid = new Ext.grid.GridPanel({
				id : 'weather-grid',
				autoScroll : true,
				ds : ds,
				cm : colModel,
				sm : box3,
				tbar : tbar,
				border : true
			});
	weatherGrid.on('rowdblclick', function(grid, rowIndex, e) {
				win.show();
				var rec = weatherGrid.getStore().getAt(rowIndex);
							Ext.get("weather.weatherKeyId").dom.value=rec.get("weatherKeyId");
							Ext.get("weather.weatherCode").dom.value=rec.get("weatherCode");
							Ext.get("weather.weatherName").dom.value=rec.get("weatherName");
							Ext.get("weather.diaplayNo").dom.value=rec.get("diaplayNo");						
							
			});
	var win = new Ext.Window({
		el : 'form-div',
		width : 400,
		height : 200,
		modal:true,
		closeAction : 'hide',
		items : [gridForm]
	});
	var viewport = new Ext.Viewport({
		region : "center",
		layout : 'fit',
		autoWidth:true,
		autoHeight:true,
		fitToFrame : true,
		items : [weatherGrid]
	});

	setTimeout(function() {
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut({
			remove : true
		});
	}, 250);
});