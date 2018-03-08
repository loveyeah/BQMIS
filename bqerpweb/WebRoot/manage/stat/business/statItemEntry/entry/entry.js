Ext.onReady(function() {
	var delayTime;// 延时时间
	var delayUnit;// 延时单位
	var dateType;// 报表类型
	var m = new Map();
	var grid;
	var ds;
	var myDate = new Date();
	var nowHours=myDate.getHours();      
	function saveData() {
		grid.stopEditing();
		// var u = m.values();
		// for(var i=0;i<u.length;i++)
		// {
		// alert(u[i].itemCode + "|" + u[i].date + "|" + u[i].value);
		// }
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
							var o = eval('(' + result.responseText + ')');
							Ext.MessageBox.alert('提示信息', o.msg);
							query();
							m = new Map();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('提示信息', '未知错误，请联系管理员！')
						}
					})
				}
			})
		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}
	 
	
	function query() {
		var reportCode = reportNameBox.getValue();
		if (reportCode == null || reportCode == '') {
			Ext.Msg.alert("提示", "请选择报表!");
			return;
		}
		Ext.Msg.wait("正在查询数据!请等待...");
		Ext.Ajax.request({
			url : 'manager/findRunReportEntryList.action',
			params : {
				reportCode : reportCode,
				dateType : dateType,
				delayTime : delayTime,
				delayUnit : delayUnit,
				method : "entry"
			},
			method : 'post',
			waitMsg : '正在加载数据...',
			success : function(result, request) {
				var json = eval('(' + result.responseText + ')'); 
				document.getElementById("gridDiv").innerHTML = "";
				ds = new Ext.data.JsonStore({
					data : json.data,
					fields : json.fieldsNames
				});
				var wd = Ext.get('gridDiv').getWidth()
				var ht = Ext.get('gridDiv').getHeight()
//				Ext.grid.LockingEditorGridPanel.prototype.rows =[];
//             LockingEditorGridPanel			
				grid = new Ext.grid.EditorGridPanel({
					renderTo : 'gridDiv',
					stripeRows: true, 
					id : 'grid',
					split : true,
					width : wd,
					height : ht,
					autoScroll : true,
					border : false,
					columns : json.columModle, 
					cm : new Ext.grid.ColumnModel({
						columns : json.columModle,
//						defaultSortable : true,
						rows : json.rows
					}),
					enableColumnMove : false,  
					plugins : [new Ext.ux.plugins.GroupHeaderGrid(null,json.rows)],
					ds : ds,
					clicksToEdit : 1,
					listeners : {
						'beforeedit' : function(e) {
							
							//班组指标
							if(dateType == '2')
							{
								/*if(nowHours >2 && e.row <4)
								{
									if((nowHours < 8 && e.row >=1||
										nowHours < 14 && e.row >=2 ||
										nowHours < 20 && e.row >=3) ) 
									{
										return false;
									}
								}*/
								/*if(reportCode == '32' || reportCode == '37' || reportCode == '36')
								{
								   if(nowHours >2 && nowHours <8 && e.row==0 ||
								      nowHours >8 && nowHours <14 && e.row==1 ||
								      nowHours >14 && nowHours <20 && e.row==2 ||
								      nowHours >20 && e.row==3 ||
								      nowHours <2 && e.row==3)
								   {
								  // return true;
								   }else return false;
								}*/
								if(reportCode == '52')
								{
								if(e.row <4 )
								   {
								   return false;
								   }
								}
								if(reportCode == '56')
								{
								if(e.row >3 )
								   {
								   return false;
								   }
								}
								if(reportCode == '49')
								{
								   if(e.row <3 || e.row >3)
								   {
								   return false;
								   }
								}
							}
						 
							
							var type = e.record.get(e.field + 'type');
							grid
									.getColumnModel()
									.setEditor(
											e.column,
											new Ext.grid.GridEditor(new Ext.form.NumberField({
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
//				grid.rows = json.rows;
				grid.render();
				btnSave.show();
				Ext.Msg.hide();
			},
			failure : function(result, request) {
				Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
				Ext.Msg.hide();
			}
		});
	}
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
		delayTime = records[0].data.timeDelay;
		delayUnit = records[0].data.timeUnit;
		dateType = records[0].data.reportType;
	});

	reportNameStore.load({
		params : {
			type :'lr',
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
		width : 220,
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : true,
		emptyText : '请选择',
		listeners : {
			select : function(index, record) {
				delayTime = record.data.timeDelay;
				delayUnit = record.data.timeUnit;
				dateType = record.data.reportType;
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
	var tbar = new Ext.Toolbar({
		id : 'tbar',
		height : 25,
		items : ['运行报表名称：', reportNameBox, '-', btnQuery, '-', btnSave]
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
				html : '<div id="gridDiv" ></div>'
			}]
		})]
	});
	Ext.get('gridDiv').setWidth(Ext.get('panel').getWidth());
	Ext.get('gridDiv').setHeight(Ext.get('panel').getHeight() - 25);
});
