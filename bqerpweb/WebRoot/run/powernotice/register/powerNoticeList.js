Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif'; 
Ext.onReady(function() {

		// 定义状态
	var stateData = new Ext.data.SimpleStore({
				data : [['', '所有状态'], [1, '未上报'], [4,'已退回']],
				fields : ['value', 'name']
			});
	// 定义状态
	var stateComboBox = new Ext.form.ComboBox({
				id : "stateCob",
				store : stateData,
				displayField : "name",
				valueField : "value",
				mode : 'local',
				triggerAction : 'all',
				hiddenName:'stateComboBox',
				readOnly : true,
				value:'',
				width : 120
			}); 
	// 定义grid
var MyRecord = Ext.data.Record.create([
	{name : 'power.noticeId'},
	{name:'power.noticeNo'},
    {name : 'power.contactDept'},
	{name : 'power.contactMonitor'},
	{name : 'power.contactContent'},
	{name : 'power.memo'},
	{name : 'deptName'},
	{name : 'monitorName'},
	{name : 'busiStateName'},
	{name : 'power.busiState'},
	{name : 'power.workFlowNo'},
	{name:'contactDate'},
	{name:'power.noticeSort'}
	]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url:'powernotice/findPowerNoticeList.action'
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

	
		var fuzzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});

	var sm = new Ext.grid.CheckboxSelectionModel();

	var grid = new Ext.grid.GridPanel({
			region : "center",
		store : store,

		columns : [
		sm, new Ext.grid.RowNumberer({
		header : '项次号',
		width : 50,
		align : 'left'
	}),{
			
			header : "ID",
			width : 100,
			sortable : true,
			dataIndex : 'power.noticeId',
			hidden:true
		},{
			
			header : "状态ID",
			width : 100,
			sortable : true,
			dataIndex : 'power.busiState',
			hidden:true
		},{
			
			header : "通知单号",
			width : 80,
			sortable : true,
			dataIndex : 'power.noticeNo'
		},{
			
			header : "状态",
			width : 70,
			sortable : true, 
			dataIndex : 'busiStateName' 
		},

		{
			header : "联系班组编号",
			width : 120,
			sortable : true,
			dataIndex : 'power.contactDept', 
			hidden:true
		},

		{
			header : "申请班组",
			width : 100,
			sortable : true,
			dataIndex : 'deptName' 
		},
		{
			header : "联系班长工号",
			width : 100,
			sortable : true,
			dataIndex : 'power.contactMonitor', 
			hidden:true
		},
		{
			header : "申请人",
			width : 70,
			sortable : true,
			dataIndex : 'monitorName'  
		},
		{
			header : "申请时间",
			width : 110,
			sortable : true,
			dataIndex : 'contactDate'  
		},
		{
			header : "联系内容",
			width : 300,
			sortable : true,
			renderer:function(v){
				return v.replace(/\r/g, "<br/>"); 
			},
			dataIndex : 'power.contactContent' 
		},
		{
			header : "备注",
			width : 200,
			sortable : true,
			dataIndex : 'power.memo' 
		},{
			header : "类别号",
			width : 100,
			sortable : true,
			dataIndex : 'power.noticeSort',
			renderer : function (v)
			{
				if(v==1)
				return 'aaa';
				else if(v==4)
				return 'bb';
			},
			hidden:true
		},{
			header : "类别",
			width : 200,
			sortable : true,
			dataIndex : 'noticeSort',
			hidden:true
		}
		
		],
		sm : sm,
		tbar : ['状态',stateComboBox,
		{
			text : "查询",
            iconCls : 'query',
            handler:queryRecord
		},'-',  {
			text : "新增",
			iconCls : 'add',
			handler:function()
			{
				 parent.document.all.iframe1.src = "run/powernotice/register/powerNoticeBase.jsp";
				  parent.Ext.getCmp("maintab").setActiveTab(0);
			}
	
		},'-', {
			text : "修改",
			iconCls : 'update',
			handler:updateRecord
	
		},'-',{
			text : "删除",
			iconCls : 'delete',
			handler:deleteRecord
	
		},'-',{
			text : "上报",
			iconCls : 'upcommit',
			handler:reportBtn
	
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

	
	grid.on('rowdblclick',updateRecord);
	function updateRecord()
	{
		
		if (grid.selModel.hasSelection()) {
		
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				var member = records[0];
				var no=member.get("power.noticeNo");
				var id=member.get("power.noticeId");
				var monitorCode=member.get("power.contactMonitor");
				var monitorName=member.get("monitorName");
				var contactDeptCode=member.get("power.contactDept");
				var contactDeptName=member.get("deptName");
				var contactDate=member.get("contactDate");
				var memo=member.get("power.memo");
				var contactContent=member.get("power.contactContent");
				
				var noticeSort = member.get("power.noticeSort");
				
				var url= "run/powernotice/register/powerNoticeBase.jsp?id="+id+"&no="+no+"&monitorCode="+monitorCode;
				  url=url+"&monitorName="+monitorName+"&contactDeptCode="+contactDeptCode;
				  url=url+"&contactDeptName="+contactDeptName+"&contactDate="+contactDate;
				  url=url+"&memo="+memo+"&contactContent="+contactContent+"&noticeSort="+noticeSort;
				  parent.document.all.iframe1.src =url;
				  parent.Ext.getCmp("maintab").setActiveTab(0);
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}
	function deleteRecord()
	{
			var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		var nos=[];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {

			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("power.noticeId")) {
					ids.push(member.get("power.noticeId"));
					nos.push(member.get("power.noticeNo"));
				} else {
					
					store.remove(store.getAt(i));
				}
			}

			

			Ext.Msg.confirm("删除", "是否确定删除通知单'" + nos + "'？", function(
					buttonobj) {

				if (buttonobj == "yes") {

					Ext.lib.Ajax.request('POST',
							'powernotice/deletePowerNotice.action', {
								success : function(action) {
									Ext.Msg.alert("提示", "删除成功！");
									queryRecord();
							
								},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
							}, 'ids=' + ids);
				}

			});
		}
	}
     function queryRecord() {
	
		store.load({
			params : {
				start : 0,
				limit : 18,
				busiState :stateComboBox.getValue()
				//contactDept:Ext.getCmp('deptId').value
			
			}
		});
	}
	
	// 上报处理
	function reportBtn() {
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var noticeNo;
		var isReport;
		if (selected.length == 0) {
			Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
		} else {
			var menber = selected[0];
			noticeNo = menber.get('power.noticeNo');
			isReport = menber.get('power.busiState');
			if (isReport !="1"
					&& isReport != "4") {
				Ext.Msg.alert("系统提示信息", "只有未上报和已退回的票允许上报");
			} else {
				
				var args=new Object();
				args.busiNo=noticeNo;
				args.flowCode="bqStopSendElec";
				args.entryId="";
				
				args.title="停送电通知单上报";
				  var danger = window.showModalDialog('reportSign.jsp',
                args, 'dialogWidth=700px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');
               queryRecord();
			}
		}
	}
//---------------------------------------

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});
	
	queryRecord();

});