Ext.useShims = true;
Ext.onReady(function(){
	var dutytime = {
			id : 'dutytime',
			xtype: 'datefield',
		    format:'Y-m-d',
		    width:150
	}; 
	var dutyclass = new Ext.form.ComboBox({ 
		  fieldLabel : '部门 ',   
		  name : 'identity',    
		  id:'identity', 
		  allowBlank : false, 
		  mode : 'local',      
		  readOnly : true,     
		  triggerAction : 'all',   
		  anchor : '90%', 
		  emptyText:'请选择..', 
		  store : new Ext.data.SimpleStore({
		   fields : ['value', 'text'], 
		   data : [['上午班 ', '上午班 '],['下午班 ', '下午班 '],['前夜班 ', '前夜班 '],['后夜班', '后夜班 ']] 
		  }), 
		  valueField : 'value',  
		  displayField : 'text'  
		 });
		 
function query() 
	{
		document.frames["report"].location.href = "/powerrpt/report/webfile/bqmis/runboilermonitor.jsp?dutyTime="
		+Ext.getCmp("dutytime").value
			+ "&dutyShift=" +Ext.getCmp("identity").value;
	}
	 var tbar = new Ext.Toolbar({
				items :['时间：',dutytime,	
			      '班次：',dutyclass,
			      {
					id : 'btnQuery',
							text : "查询",
							iconCls : 'query',
							handler : query		
					}]
			});
	new Ext.Viewport({
		enableTabScroll:true,
		layout:"fit",
		items:[{title:"",
			html:"<iframe id='report' name='report' src='/powerrpt/report/webfile/bqmis/runboilermonitor.jsp?dutyTime=2009-03-23&dutyShift=上午班' style='width:1000;height:1500;border:0px;'></iframe>",
			tbar:tbar
			      }]
	});
});