Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	function getCurrentDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		return s;
	}
	
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result.workerCode) {
					// 设定工作负责人为登录人
					Ext.get("recordBy").dom.value = result.workerName
							? result.workerName
							: '';
				}
			}
		});
	}
	
	// 定义grid
var MyRecord = Ext.data.Record.create([
	{name : 'intervalId'},
    {name : 'beginDate'},
	{name : 'endDate'},
	{name : 'evaluationDays'},
	{name : 'memo'},
	{name : 'recordBy'},
	{name : 'recordDate'}
	]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url:'clients/findClientIntervalList.action'
			}

	);

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"

	}, MyRecord);

	var store = new Ext.data.Store({

		proxy : dataProxy,

		reader : theReader

	});
//分页
	store.load({
			params : {
				start : 0,
				limit : 10				
			}
		});
	



	var sm = new Ext.grid.CheckboxSelectionModel({
	singleSelect:true
	});

	var grid = new Ext.grid.GridPanel({
		region : "center",
		layout : 'fit',
		store : store,

		columns : [
		sm, new Ext.grid.RowNumberer({header:'序号',width : 50}),{
			
			header : "ID",
			width : 75,
			sortable : true,
			dataIndex : 'intervalId',
			hidden:true
		},
		{
			header : "评价周期开始时间",
			width : 75,
			sortable : true,
			dataIndex : 'beginDate',
			renderer:function(value)
			{
				return value.substring(0,10);
			}
		},
		{
			header : "评价周期结束时间",
			width : 75,
			sortable : true,
			dataIndex : 'endDate',
			renderer:function(value)
			{
				return value.substring(0,10);
			}
		},
		{
			header : "评分天数",
			width : 75,
			sortable : true,
			dataIndex : 'evaluationDays'
		},
		{
			header : "备注",
			width : 75,
			sortable : true,
			dataIndex : 'memo'
		}
		],
		sm : sm,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : true
		},
		tbar : [
		{
			text : "新增",
            iconCls : 'add',
			handler :addRecord
		}, {
			text : "修改",
			iconCls : 'update',
			handler : updateRecord
		}, {
			text : "删除",
			iconCls : 'delete',
			handler : deleteRecord
		}],
		//分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});

grid.on("rowdblclick", updateRecord);
//---------------------------------------

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
	//	layout : "fit",
		items : [grid]
	});

	// -------------------
	var wd = 240;

	var intervalId = new Ext.form.Hidden({
		id : "intervalId",
		fieldLabel : 'ID',
		width : wd,
		readOnly : true,
		value:'',
		name : 'interval.intervalId'

	});
	
	var beginDate=new Ext.form.DateField({
	    id:'beginDate',
	    allowBlank:false,
	    fieldLabel : '评价周期开始时间',
	 	name : 'interval.beginDate',
	 	width:wd,
	 	format:'Y-m-d'
	});

	var endDate=new Ext.form.DateField({
	    id:'endDate',
	    allowBlank:false,
	    width:wd,
	    fieldLabel : '评价周期结束时间',
	 	name : 'interval.endDate',
	 	format:'Y-m-d'
	});
	
	var evaluationDays=new Ext.form.NumberField({
	 id:'evaluationDays',
	 width:wd,
	    allowBlank:false,
	       fieldLabel : '评价天数',
	 	name : 'interval.evaluationDays'
	});
	
	
 
	var memo=new Ext.form.TextArea({
	 id : "memo",
		fieldLabel : '备注',
		width : wd,
		name : 'interval.memo',
		height:80
	
	}); 
	
	var recordBy=new Ext.form.TextField({
	fieldLabel : '记录人',
	 id:'recordBy',
	 width:wd,
	 name : 'interval.recordBy',
	 readOnly:true
	});
	
	var recordDate=new Ext.form.TextField({
	 fieldLabel : '记录时间',
	 id:'recordDate',
	 width:wd,
	 name : 'interval.recordDate',
	 readOnly:true,
	 value : getCurrentDate()
	});
	
	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		labelWidth:110,
		closeAction : 'hide',
		title : '增加/修改评价周期设置',
		items : [intervalId,beginDate,endDate,evaluationDays,memo,recordBy,recordDate] 
	});
	
	function checkInput()
	{
		var msg="";
		if(beginDate.getValue()=="")
		{
			msg="'评价周期开始时间'";
		}
		if(endDate.getValue()=="")
		{
			msg="'评价周期结束时间'";
		}
		if(evaluationDays.getValue()=="")
		{
			msg="'评价天数'";
		}
		if(msg!="")
		{
			Ext.Msg.alert("提示","请输入"+msg);
			return false;
		}
		else if(beginDate.getValue()!=""&&endDate.getValue()!="")
		{
		
			var strBegin=Date.parseDate(Ext.get("beginDate").dom.value, 'Y-m-d');
			var strEnd=Date.parseDate(Ext.get("endDate").dom.value, 'Y-m-d');
			
			if (strBegin>=strEnd)
			{
				Ext.Msg.alert("提示","'评价周期结束时间'必须大于'评价周期开始时间'！");
			    return false;
			}
		}
		return true;
	}
		

	var win = new Ext.Window({
		width : 400,
		height : 350,
		buttonAlign : "center",
		items : [myaddpanel],
		layout : 'fit',
		closeAction : 'hide',
		resizable : false,
		modal : true,
		buttons : [{
			text : '保存',
			iconCls:'save',
			handler : function() {
				var myurl="";
				if (intervalId.getValue() == "") {
					myurl="clients/addClientIntervalInfo.action";
				} else {
						myurl="clients/updateClientIntervalInfo.action";
				}
				
				if(!checkInput()) return;
				myaddpanel.getForm().submit({
					method : 'POST',
					url : myurl,
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("注意", o.msg);
						if(o.msg.indexOf("成功")!=-1)
						{
							store.load({
		                                        	params : {
				                                    start : 0,
				                                    limit : 18
			                                        }
		                                          });
						win.hide(); 
						}
					},
					faliue : function() {
						Ext.Msg.alert('错误', '出现未知错误.');
					}
				});
			}
		}, {
			text : '取消',
			iconCls:'cancer',
			handler : function() { 
				win.hide();
			}
		}]

	});
	
	
	
	
	function addRecord()
	{  
		myaddpanel.getForm().reset(); 
		win.show();  
		getWorkCode();
		myaddpanel.setTitle("增加评价周期设置");
	}
	
	function updateRecord()
	{
			if (grid.selModel.hasSelection()) {
		
			var records = grid.getSelectionModel().getSelected();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				  win.show(); 
				var record = grid.getSelectionModel().getSelected();
		        myaddpanel.getForm().reset();
		        myaddpanel.form.loadRecord(record);
		        beginDate.setValue(record.get("beginDate").substring(0,10));
		        endDate.setValue(record.get("endDate").substring(0,10));
		        recordDate.setValue(record.get("recordDate").substring(0,10));
				myaddpanel.setTitle("修改评价周期设置");
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}
	
	function deleteRecord()
	{
		var records = grid.selModel.getSelections();
		var ids = [];
		var strDate="";
		if (records.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {

			
			for (var i = 0; i < records.length; i += 1) {
				var member = records[i];
				if (member.get("intervalId")) {
					ids.push(member.get("intervalId")); 
					strDate=member.get("endDate").substring(0,10);
				} else {
					
					store.remove(store.getAt(i));
				}
			}
			var nowDate=new Date();
			 var strMonth=nowDate.getMonth()+1;
			 strMonth=strMonth+"";
			 if(strMonth.length==1) strMonth="0"+strMonth;
			 var strDay=nowDate.getDate();
			 strDay=strDay+"";
			 	 if(strDay.length==1) strDay="0"+strDay;
		  		var strNow=  nowDate.getYear()+"-"+strMonth+"-"+strDay;
		
			if(Date.parseDate(strNow, 'Y-m-d')<=Date.parseDate(strDate, 'Y-m-d'))
			{
			Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(
					buttonobj) {

				if (buttonobj == "yes") {

					Ext.lib.Ajax.request('POST',
							'clients/deleteClientIntervalInfo.action', {
								success : function(action) {
									Ext.Msg.alert("提示", "删除成功！")
                                   		store.load({
		                                        	params : {
				                                    start : 0,
				                                    limit : 10
			                                        }
		                                          });
						         	
								},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
							}, 'ids=' + ids);
				}
			});
			
			}
			else
			{
				Ext.Msg.alert("提示","此条记录不能删除！");
			}
		}

	}
	
});