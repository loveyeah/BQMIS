Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	// 系统当天日期
	var startDate = new Date();
	var endDate = new Date();
	// 系统当天前30天的日期
	startDate.setDate(startDate.getDate() - 30);
	// 系统当天后30天的日期
	endDate.setDate(endDate.getDate() + 30);
	// 起始日期
	var sdate = new Ext.form.DateField({
		name : 'sdate',
		width : 90,
		altFormats : 'Y-m-d',
		format : 'Y-m-d',
		readOnly : true,
		value : startDate
	});

	// 结束日期
	var edate = new Ext.form.DateField({
		name : 'edate',
		width : 90,
		altFormats : 'Y-m-d',
		format : 'Y-m-d',
		readOnly : true,
		value : endDate
	});
	
	
		// 定义状态
	var stateData = new Ext.data.SimpleStore({
				data : [['', '所有状态'], [1, '未上报'],[2,'已上报'],[3,'值班负责人已审批'], [4,'已退回']],
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
				width : 90
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
	{name:'receiveTeam'}
	]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url:'powernotice/findPowerNoticeQueryList.action'
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

	/*	
	var storeWorkticketType = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'powernotice/queryData.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, [{
			name : 'teamCode',
			mapping : 0
		}, {
			name : 'teamName',
			mapping : 1
		}])
	});
	// storeWorkticketType.load();
	var teamValue= new Ext.form.ComboBox({
		id : 'teamCode',
		//fieldLabel : "工作票种类<font color='red'>*</font>",
		store : storeWorkticketType,
		displayField : "teamName",
		valueField : "teamCode",
		hiddenName : 'workticketBaseInfo.workticketTypeCode',
		mode : 'remote',
		triggerAction : 'all',
		value : '',
		readOnly :false,
		anchor : "85%",
		listeners : {
			//select : teamSelected
		}
	})*/
	var teamData = new Ext.data.SimpleStore({
				data : [['', '所有'],['一值', '一值'], ['二值', '二值'],['三值','三值'],['四值','四值'], ['五值','五值']],
				fields : ['value', 'name']
			});
	// 定义状态
	var teamValue = new Ext.form.ComboBox({
				id : "teamValue",
				store : teamData,
				displayField : "name",
				valueField : "value",
				mode : 'local',
				triggerAction : 'all',
//				hiddenName:'teamValue',
				readOnly : false,
				value:'',
				width : 60
			});
			
	var sm = new Ext.grid.CheckboxSelectionModel();

		var btnDelete = new Ext.Button({
		id : "btnDelete",
		text : "删除",
		hidden : true,
		iconCls : 'delete',
		handler : function() {
			var record = grid.getSelectionModel().getSelected();
			if (record) {
				if (confirm("删除后数据不能恢复,确定要删除该条停送电通知单吗?")) {
					var busiNo = record.get("power.noticeNo");
					var entryId = record.get("power.workFlowNo");
					Ext.Ajax.request({
						url : "MAINTWorkflow.do?action=mangerDelete",
						method : 'post',
						params : {
							busiType : 'power',
							entryId : entryId,
							busiNo : busiNo
						},
						success : function(result, request) {
							Ext.Msg.alert("提示", "操作成功!");
							grid.getStore().remove(record);
							btnDelete.setVisible(false);
						},
						failure : function() {
							Ext.Msg.alert("提示", "操作失败!");
						}
					});
				}
			} else {
				Ext.Msg.alert("提示", "请选择一条删除的记录！");
			}
		}
	});
	 var apply = new Ext.form.TextField({
		id : "apply",
		width:65,
		name : "apply"
	});
	var grid = new Ext.grid.GridPanel({
			region : "center",
		store : store,

		columns : [
		sm, new Ext.grid.RowNumberer({
		header : '项次号',
		width : 50,
		align : 'center'
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
			width : 110,
			sortable : true,
			align : 'center',
			dataIndex : 'busiStateName'
//				renderer : function changeIt(val) {
//				
//				
//			}
		},

		{
			header : "联系班组编号",
			width : 120,
			sortable : true,
			dataIndex : 'power.contactDept',
			align:'center',
			hidden:true
		},

		{
			header : "申请班组",
			width : 100,
			sortable : true,
			dataIndex : 'deptName',
			align:'center'
		},
		{
			header : "联系班长工号",
			width : 100,
			sortable : true,
			dataIndex : 'power.contactMonitor',
			align:'center',
			hidden:true
		},
		{
			header : "申请人",
			width : 70,
			sortable : true,
			dataIndex : 'monitorName',
			align:'center'
		},
		{
			header : "申请时间",
			width : 110,
			sortable : true,
			dataIndex : 'contactDate',
			align:'center'
		},
		{
			header : "联系内容",
			width : 140,
			sortable : true,
			dataIndex : 'power.contactContent',
			align:'center'
		},{
			header : "接受班组",
			width : 75,
			sortable : true,
			dataIndex : 'receiveTeam',
			align:'center'
		},
		{
			header : "备注",
			width : 75,
			sortable : true,
			dataIndex : 'power.memo',
			align:'center'
		}
		
		],
		sm : sm,
		tbar : [' 时间:      ',sdate,'~',edate,'-','状态:',stateComboBox,'接受班组：',teamValue,'申请人：',apply,
		{
			text : "查询",
            iconCls : 'query',
            handler:queryRecord
		},'->',btnDelete,'<div id="divManagerDel">停送电联系单列表</div>'],
		
		//分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});

	
  

     function queryRecord() {
	
     	store.baseParams={
     	busiState :stateComboBox.getValue(),
				sdate: sdate.value.toString(),
				edate: edate.value.toString(),
				teamValue:teamValue.getRawValue(),
				apply:apply.getValue()
     	};
     	
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}
	
	grid.on("rowcontextmenu", function(g, i, e) { 
				e.stopEvent();
				var record = grid.getStore().getAt(i);
				// 右键菜单
				var menu = new Ext.menu.Menu({
					id : 'mainMenu',
					items : [new Ext.menu.Item({
						text : '票面浏览',
						handler : function() { 
						var noticeNo = record.get("power.noticeNo");
					    var strReportAdd = "";
					    strReportAdd = "/powerrpt/report/webfile/bqmis/adviceNote.jsp?no="+noticeNo;
					    if(strReportAdd != Constants.BirtNull) {
						    window.open(strReportAdd);
					    }
							//workticketPrint(record);
						}
					}), new Ext.menu.Item({
						text : '图形展示',
						handler : function() {
							var entryId = record.get("power.workFlowNo");
							var url = ""; 
							if(entryId == "" || entryId == null)
							{
								
								var flowCode ="bqStopSendElec";
								url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="+flowCode; 
							}
							else
							{
								url = "/power/workflow/manager/show/show.jsp?entryId="+entryId;
							}
							window.open(url);
					}
					}), new Ext.menu.Item({
						text : '查看审批记录',
						handler : function() {
							var entryId = record.get("power.workFlowNo");
							if(entryId != "" && entryId != null)
							{
								var url = "/power/workflow/manager/approveInfo/approveInfo.jsp?entryId="
										+ entryId;
								window.showModalDialog(
												url,
												null,
												"dialogWidth:650px;dialogHeight:400px;;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;");
						    }
						    else{
						    	Ext.Msg.alert("提示","流程尚未启动");
						    }
						}
					})]
				});
				var coords = e.getXY();
				menu.showAt([coords[0], coords[1]]);
			});
	
	
//---------------------------------------

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});
	
	queryRecord();
	
	document.getElementById("divManagerDel").onclick = function() {
		if ((currentUser == "999999" || currentUser == "1001003")
				&& event.ctrlKey && event.altKey) {
			btnDelete.setVisible(true);
		}
	};
});