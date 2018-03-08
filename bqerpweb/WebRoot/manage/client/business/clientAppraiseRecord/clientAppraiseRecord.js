Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	
	// 定义grid
var MyRecord = Ext.data.Record.create([
	{name : 'recordId'},
    {name : 'eventId'},
	{name : 'intervalId'},
	{name : 'cliendId'},
	{name : 'appraisePoint'},
	{name : 'memo'},
	{name : 'eventName'},
	{name : 'gatherFlag'},
	{name : 'clientName'},
	{name : 'appraiseMark'},
	{name : 'appraiseCriterion'}
	]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url:'clients/findAppraiseRecordList.action'
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
//-----------查询条件-----------------------------------------
	
  var cbxClient = new Ext.form.ComboBox({
		name : 'cbxClient',
		id : 'cbxClient',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName:'cbxClientId',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../clientSelect/clientSelect.jsp?approveFlag=2";
			var client = window
					.showModalDialog(
							url,
							null,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(client) != "undefined") {
				
				Ext.getCmp('cbxClient').setValue(client.cliendId);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('cbxClient'), client.clientName);
						queryRecord();
			}
		}
	});
	
	var beginDate=new Ext.form.TextField({
	    id:'beginDate',
	    fieldLabel : '评价周期开始时间',
	 	name : 'beginDate',
	 	readOnly:true
	});

	var endDate=new Ext.form.TextField({
	    id:'endDate',
	    fieldLabel : '评价周期结束时间',
	 	name : 'endDate',
	 	readOnly:true
	});
	var hideIntervalId=new Ext.form.Hidden({
	 	id:'hideIntervalId'
	});
	
	var hideEvaluationDays = new Ext.form.Hidden({
	 	id:'hideEvaluationDays'
	});
	
//----------------------------------------------
	var txtAppraisePoint=new Ext.form.NumberField({
	   id:'txtAppraisePoint',
	   name:'appraisePoint',
	   minValue : 0
	});

	txtAppraisePoint.on("change",function(){
	var nowScore= grid.getSelectionModel().getSelected().get("appraisePoint");
  // grid.getSelectionModel().getSelected().set("memo","abc");
	var standard=grid.getSelectionModel().getSelected().get("appraiseMark");					
		if(txtAppraisePoint.getValue()>standard)
		{
		  Ext.Msg.alert("提示","评价分数不能大于标准分数！");	
		  txtAppraisePoint.setValue(nowScore);
		}
	})
	
 var txtMemo=new Ext.form.TextArea({
    id:'txtMemo',
    name:'memo'
 });	

var sm = new Ext.grid.CheckboxSelectionModel();
	var grid = new Ext.grid.EditorGridPanel({
		region : "center",
		layout : 'fit',
		store : store,
         clicksToEdit : 1,
		columns : [
		 new Ext.grid.RowNumberer({header:'序号',width : 50}),{
			
			header : "ID",
			width : 75,
			sortable : true,
			dataIndex : 'recordId',
			hidden:true
		},
		{
			header : "评价项目名称",
			width : 75,
			sortable : true,
			dataIndex : 'eventName'
		},
		{
			header : "标准分数",
			width : 75,
			sortable : true,
			dataIndex : 'appraiseMark'
		},
		{
			header : "评分标准",
			width : 75,
			sortable : true,
			dataIndex : 'appraiseCriterion'
		},
		{
			header : "评价分数",
			width : 75,
			sortable : true,
			dataIndex : 'appraisePoint',
			editor:txtAppraisePoint
		},
		{
			header : "评分备注",
			width : 75,
			sortable : true,
			dataIndex : 'memo',
			editor:txtMemo
		},
		{
		  	
		  header : "是否汇总",
			width : 75,
			sortable : true,
			dataIndex : 'gatherFlag',
			renderer:function(value)
			{
				if(value=="N") return "否";
				if(value=="Y") {
					// 已经汇总了的不能修改，modify by ywliu 2009/6/25
					grid.getColumnModel().setEditor(5,new Ext.grid.GridEditor(new Ext.form.NumberField({readOnly : true})));
					grid.getColumnModel().setEditor(6,new Ext.grid.GridEditor(new Ext.form.TextArea({readOnly : true})));
					return "是";
				}	
			}
		}
		],
		
		autoSizeColumns : true,
		viewConfig : {
			forceFit : true
		},
		sm:sm,
		tbar:['合作伙伴：',cbxClient,'评价区间：',beginDate,'~',endDate,hideIntervalId,
		{
		  text:'查询',
		  iconCls:'query',
		  handler:queryRecord
		},
		{
		  text:'分数保存',
		  id:'btnSave',
		  iconCls:'save',
		  handler:saveRecord
		}
		
		]
	});


//---------------------------------------

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
	//	layout : "fit",
		items : [grid]
	});


	beginDate.getEl().on("click",function(){
			var url = "../intervalSelect/intervalSelect.jsp";
			var obj = window.showModalDialog(
							url,
							null,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(obj) != "undefined") {
				beginDate.setValue(obj.beginDate);
				endDate.setValue(obj.endDate);
				hideIntervalId.setValue(obj.intervalId);
				hideEvaluationDays.setValue(obj.evaluationDays);
				queryRecord();
			}
		
	});
	


	function queryRecord()
	{
		var msg="";
		if(cbxClient.getValue()=="") msg="'合作伙伴'";
		if(hideIntervalId.getValue()=="") msg+="'评价区间'";
		if(msg!="")
		{
			Ext.Msg.alert("提示","请选择"+msg+"!");
			return;
		}
		
		    store.load({
						params : {
									clientId:Ext.get("cbxClientId").dom.value,
									intervalId:hideIntervalId.getValue()
								}
							});
			store.on('load', function() {
					for (var j = 0; j < store.getCount(); j++) {
			var temp = store.getAt(j);
			if(temp.get("gatherFlag")==null||temp.get("gatherFlag")=="")
			{
			 temp.set("gatherFlag","N");
			}
			if(temp.get("gatherFlag")==""||temp.get("gatherFlag")=="N"||temp.get("gatherFlag")==null)
			{
				grid.getTopToolbar().items.get('btnSave').setDisabled(false);
			}
			else
			{
				grid.getTopToolbar().items.get('btnSave').setDisabled(true);
			}
		}	
			});
			
						
	
	}
	
	
	function saveRecord()
	{	var date1 = endDate.getValue().substring(5,7)+'/'+endDate.getValue().substring(8,10)+'/'+endDate.getValue().substring(0,4);
		var date2 = new Date(date1).add(Date.DAY, 1);
		var date3 = new Date(date1).add(Date.DAY, hideEvaluationDays.getValue());
		if(new Date().getTime() <= date2.getTime() || new Date().getTime() >= date3.getTime()) {
			Ext.MessageBox.alert('提示', '当前时间不允许评价！');
		} else {
			grid.stopEditing();
			var modifyRec = grid.getStore().getModifiedRecords();
			if (modifyRec.length > 0) {
				if (!confirm("确定要保存修改吗?")) {
					return false;
				}
				Ext.Msg.wait("正在保存数据,请等待...");
				var modifyRecords = new Array();
				for (var i = 0; i < modifyRec.length; i++) {
					modifyRecords.push(modifyRec[i].data);
				}
				Ext.Ajax.request({
					url : 'clients/saveAppraiseRecordInfo.action',
					method : 'post',
					params : {
						addOrUpdateRecords : Ext.util.JSON.encode(modifyRecords)
					},
					success : function(result, request) {
						Ext.MessageBox.alert('提示', '操作成功！');
						store.reload();
						store.rejectChanges();
	
	
					},
					failure : function(result, request) {
						Ext.MessageBox.alert('提示', '操作失败！');
						store.reload();
						store.rejectChanges();
					}
				})
			} else {
				Ext.Msg.alert('提示', '您没有做任何修改！'); 
			}
		}	
	}
	
});