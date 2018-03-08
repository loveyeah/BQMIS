Ext.onReady(function() {
	var applyId="";
	
	var sm = new Ext.grid.CheckboxSelectionModel();
	var rn = new Ext.grid.RowNumberer({
		header : '序号',
		width : 60,
		align : 'center'
	})

	var colModel = new Ext.grid.ColumnModel({
		columns : [sm, rn,{
			dataIndex : "applyCode",
			header : '编号',
			width : 100
		}, {
			dataIndex : "inOut",
			header : '退/投',
			width : 60,
			renderer:function(value, metadata, record) {
								if (value =='I')
									return "投";
									else if (value=='O')
									return "退";
			}
			
		}, {
			dataIndex : "protectionType",
			header : '投退保护类型',
			width :300,
			renderer : function(value, metadata, record) {
				if (value == '1')
				//update by sychen 20100824
				   return "设备继电保护及安全自动装置、调动自动化投入申请单";
//					return "其它继电保护及安全自动装置、调度自动化设备投、退申请单";
				else if (value == '2')
					return "设备继电保护及安全自动装置、调动自动化退出申请单";
				else if (value == '3')
					return "热控保护投入申请单";
				else if (value == "4")
					return "热控保护退出申请单";
			}
		}, {
			dataIndex : "blockId",
			header : '机组',
			width : 150,
			renderer : function(v) {
				if (v == 1)
					return '300MW机组';
				else if (v == 4)
					return '125MW机组';
			}
		}, {
			dataIndex : "applyDep",
			header : '申请部门',
			width : 100,
			renderer:function(value, metadata, record) {
								if (value != null)
									return record.get('applyDepName')}
			
		}, {
			dataIndex : "applyBy",
			align : 'left',
			header : '申请人',
			width : 100,
			renderer : function(v, metadata,record) {
				if (v != null)
					return record.get('applyByName')
			}
		}, {
			dataIndex : "applyTime",
			header : '申请时间',
			width : 100
		}, {
			dataIndex : "protectionName",
			header : '保护名称',
			width : 150
		},{
			dataIndex : "status",
			header : '申请单状态',
			width : 150, 
			renderer:function(value, metadata, record) {
								if (value =='0')
									return "未上报";
									else if (value=='1')
									return "已上报";
									else if (value=='2')
									return "检修公司高管已审批";
									else if (value=='3')
									return "设备部高管已审批";
									else if (value=='4')
									return "值长已审批";
									else if (value=='5')
									return "总工已审批";
									else if (value=='6')
									return "值长已二次复核";
									else if (value=='7')
									return "机组长、运行班长/程控班班长指定执行人已审批";
									else if (value=='8')
									return "审批结束";
									else if (value=='9')
									return "审批退回";
			
			}
			
		}],
		defaultSortable : false
	});

	var OUTRecord = Ext.data.Record.create([{
		name : 'applyId',
		mapping : 0
	}, {
		name : 'applyCode',
		mapping : 1
	}, {
		name : 'inOut',
		mapping : 2
	}, {
		name : 'protectionType',
		mapping : 3
	}, {
		name : 'applyDep',
		mapping : 4
	},{
		name : 'applyDepName',
		mapping : 5
	}, {
		name : 'applyBy',
		mapping : 6
	}, {
		name : 'applyByName',
		mapping : 7
	}, {
		name : 'applyTime',
		mapping :8
	}, {
		name : 'protectionName',
		mapping : 9
	}, {
		name : 'protectionReason',
		mapping :10
	}, {
		name : 'measures',
		mapping : 11
	}, {
		name : 'workflowNo',
		mapping : 12
	}, {
		name : 'status',
		mapping : 13
	}, {
		name : 'executeTime',
		mapping : 14
	}, {
		name : 'memo',
		mapping : 15
	}, {
		name : 'blockId',
		mapping : 16
	},{
		name : 'classLeaderName',
		mapping : 17
	}, {
		name : 'executorName',
		mapping : 18
	}, {
		name : 'guardianName',
		mapping : 19
	}]);
	
	
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : "productionrec/getProtectApply.action"
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list'
		}, OUTRecord)
	});
	
	var bbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : ds,
		displayInfo : true,
		displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
		emptyMsg : "没有记录"
		})
	 
	var btnQuery = new Ext.Button({
		text : '查询',
		iconCls : 'query',
		handler : query
	});
	var btnDetail = new Ext.Button({
		text : '详细信息',
		iconCls : 'update',
		handler : detail
	});
//	 var Type = new Ext.form.ComboBox({
//		name : 'Type',
//		hiddenName : 'protectionType',
//		store : new Ext.data.SimpleStore({
//			fields : ['name', 'value'],
//			data : [['所有', ''],
//					['退出', 'O'],
//					['投入', 'I']]
//		}),
//		value : "",
//		fieldLabel : '投/退类型',
//		triggerAction : 'all',
//		readOnly : true,
//		valueField : 'value',
//		displayField : 'name',
//		mode : 'local',
//		width : 150,
//		anchor : '100%',
//		listeners : {
//			"select" : function() {
//
//			}
//		}
//	})
	
	var protectType = new Ext.form.ComboBox({
		name : 'protectType',
		hiddenName : 'protectionType',
		store : new Ext.data.SimpleStore({
			fields : ['name', 'value'],
			data : [['所有', ''],['设备继电保护及安全自动装置、调动自动化投入申请单', '1'],
					['设备继电保护及安全自动装置、调动自动化退出申请单', '2'],
					['热控保护投入申请单', '3'],
					['热控保护退出申请单', '4']]
		}),
		value : "",
		fieldLabel : '投退保护类型',
		triggerAction : 'all',
		readOnly : true,
		valueField : 'value',
		displayField : 'name',
		mode : 'local',
		width : 380,
		anchor : '100%',
		listeners : {
			"select" : function() {

			}
		}
	})
	function query()
	{
		ds.baseParams = {
            protectType : protectType.getValue(),
			//inOut : Type.getValue(),
			approve : ""
        }
		ds.load({
					params : {
						start : 0,
						limit : 18
					}
				});
		
	}
	
	function  detail()
	{
	var selected=grid.getSelections();
		if(selected.length==0)
		{
		Ext.MessageBox.alert("提示","请选择要查看的记录");
		return ;
		}else 
		 tabs.setActiveTab(1);
		 var record = selected[0];
			applyId = record.get('applyId');
			applyCode = rec.get('applyCode');
			var url = "/powerrpt/report/webfile/bqmis/castBackProtect.jsp?applyId="
					+ applyId+ "&applyCode=" + applyCode ;
			document.all.iframe1.src = url;
	}
	
	function flowQuery() {
		if (!sm.hasSelection() || sm.getSelections().length > 1)
			Ext.Msg.alert('提示', '请先选择一条记录！');
		else {
			var url = '';
			var rec = sm.getSelections();
			if (rec[0].get('workflowNo') == null
					|| rec[0].get('workflowNo') == '') {
				url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
						+ "bqCastBackProtectApprove";
				window.open(url);
			} else {
				url = "/power/workflow/manager/show/show.jsp?entryId="
						+ rec[0].get('workflowNo');
				window.open(url);
			}
		}
	}
	
	var tbar = new Ext.Toolbar({
		height : 25,
		items : [ 
//		'退/投：',Type,'-',
		'投退保护类型:',protectType,btnQuery,'-', btnDetail, '-', {
							id : 'btnFlow',
							text : "流程展示",
							iconCls : 'view',
							handler : flowQuery
						}]
	});
  
	var grid = new Ext.grid.GridPanel({
		ds : ds,
		cm : colModel,
		sm : sm,
		bbar : bbar,
		tbar : tbar,
		border : false,
		autoWidth : true,
		fitToFrame : true
	});
	 
	var url = "/powerrpt/report/webfile/bqmis/castBackProtect.jsp?applyId="
					+ applyId;
   
	grid.on('rowdblclick',function (){
		detail();
	
	})
	var tabs = new Ext.TabPanel({
		activeTab : 0,
		tabPosition : 'bottom',
		plain : true,
		defaults : {
			autoScroll : true
		},
		listeners : {
			beforetabchange : function(tabs, newTab, currentTab) {
				if (newTab.id == 'detail') {
					rec = grid.getSelectionModel().getSelected();
				
					
					if (rec != null) {
						
					} else {
						Ext.Msg.alert("提示", "请先选中要查看的记录！");
						return false;
					}
				} else if (newTab.id == 'list') {
					
					}
				}
			
		},
		items : [{
			id : 'list',
			title : '审批列表',
			autoScroll : true,
			items :grid,
			layout : 'fit'
		}, {
			id : 'detail',
			title : '详细信息',
			items : 
	    {
					region : "center",
					layout : 'fit',
					border : true,
					collapsible : true,
					split : true,
					margins : '0 0 0 0',
					html : '<iframe id="iframe1" name="iframe1"  src="'
							+ url
							+ '"  frameborder="0"  width="100%" height="100%"  />'
				},
			autoScroll : true,
			layout : 'fit'
		}]
	});
	var view = new Ext.Viewport({
		layout : 'fit',
		items : [tabs]
	});
	
query()
	
	
});