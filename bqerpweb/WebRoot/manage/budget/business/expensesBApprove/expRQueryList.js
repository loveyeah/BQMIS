Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
function getDate() {
	var d, s, t;
	d = new Date();
	s = d.getFullYear().toString(10) + "-";
	t = d.getMonth() + 1;
	s += (t > 9 ? "" : "0") + t + "-";
	t = d.getDate();
	s += (t > 9 ? "" : "0") + t + " ";
	return s;
}
Ext.onReady(function() {
		var start=0;
		var limit=18;
		var record = new Ext.data.Record.create([{
						// 报销申请单id
						name : 'reportId',
						mapping : 0
					}, {
						// 报销金额大写
						name : 'reportMoneyUpper',
						mapping : 1
					}, {
						// 报销金额小写
						name : 'reportMoneyLower',
						mapping : 2
					}, {
						// 用途
						name : 'reportUse',
						mapping : 3
					}, {
						// 备注
						name : 'memo',
						mapping : 4
					}, {
						// 报销人
						name : 'reportBy',
						mapping : 5
					}, {
						// 报销时间
						name : 'reportDate',
						mapping : 6
					}, {
						// 审批状态
						name : 'workFlowStatus',
						mapping : 7
					} ,{
						// 工作流实例号
						id:'workFlowNo',
						name : 'workFlowNo',
						mapping : 8
					}, {
						// 报销部门
						name : 'reportDept',
						mapping : 9
					}, {
						// 费用来源
						name : 'itemName',
						mapping : 10
					}])

		var dataProxy = new Ext.data.HttpProxy({
				url:'managebudget/finExpRList.action'
			});
	    var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	    }, record);

	    var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	   });
		var sm = new Ext.grid.CheckboxSelectionModel();
		// 报销人
		var reportByName = new Ext.form.TextField({
				inputType:'text',
				fieldLabel : ' 报销人',
				//allowBlank : false,
				anchor : '50%'
		});
		// 用途
		var reportUse = new Ext.form.TextField({
				inputType:'text',
				fieldLabel : ' 用途',
				//allowBlank : false,
				anchor : '50%'
		});
//		// 上报按钮
//		var reportBtn = new Ext.Button({
//			text : '上报',
//			iconCls : 'upcommit',
//			handler : function() {
//				//alert();
//			//update by kzhang 20100821
////			if (reportIds==null||reportIds=="") {
////				Ext.Msg.alert("提示", "请选择一条费用报销单进行上报！");
////				return false;
////			}
//				var reportIds=38;
//				alert(reportIds);
//					var args=new Object();
//					args.busiNo=reportIds;
//					args.flowCode="cBillwout";
//					args.entryId="";
//					args.title="费用报销单上报";
//					var danger = window.showModalDialog('reportSign.jsp',
//	                args, 'dialogWidth=700px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');
//	               // blockForm.getForm().reset();
//	               // reportIds=null;
//			}
//		});
		//状态类型
		var statusValue = new Ext.form.ComboBox({
				store : [['', '全部'], ['0', '未上报'], ['1', '已上报'], ['2,3,4', '部门主任已审核'], ['5', '生产厂长/总工已审核'], ['6', '经营厂长已审核'], ['7', '经营厂长/厂长已审核'], ['8', '厂长已审核'], ['9', '财务已审核'], ['10', '已退回']],
				value : '',
				//id : 'smartDate',
				name : 'status',
				width:220,
				valueField : "value",
				displayField : "text",
				fieldLabel : "状态选择：",
				mode : 'local',
				readOnly : true,
				anchor : '140%',
				typeAhead : true,
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				disabled : false,
				selectOnFocus : true
			});
		//查询按钮
		var queryButton = new Ext.Button({
						id : 'query',
						text : '查询',
						iconCls : 'query',
						handler : queryRecord
					})	
		//审批处理按钮
	   var dealButton = new Ext.Button({
						id : 'deal',
						text : '审批查询',
						iconCls : 'view',
						handler : updateRecord
					})
		var gridPanel = new Ext.grid.GridPanel({
			            id:'grid_div',
						sm : sm,
						store : store,
						columns : [sm, new Ext.grid.RowNumberer({
									header : "序号",
									width : 40,
									align : "center"
								}), {
									header : "报销金额",
									width : 75,
									align : "center",
									sortable : true,
									dataIndex : 'reportMoneyLower'
								}, {
									header : "用途",
									width : 75,
									align : "center",
									sortable : true,
									dataIndex : 'reportUse'
								}, {
									header : "备注",
									width : 75,
									align : "center",
									sortable : true,
									dataIndex : 'memo'
								}, {
									header : "报销人",
									width : 75,
									align : "center",
									sortable : true,
									dataIndex : 'reportBy'
								}, {
									header : "报销时间",
									width : 75,
									align : "center",
									sortable : true,
									dataIndex : 'reportDate',
									renderer:function(v){
										if(v!=null&&v!=""){
											v = v.substring(0,10);
											var vtemp  = v.split('-');
											if (vtemp.length!=3) {
												return false;
											}
											v = vtemp[0]+'年'+vtemp[1]+'月'+vtemp[2]+'日';
											return v;
										}else{
											return "";
										}
									}
								}, {
									header : "审批状态",
									width : 75,
									align : "center",
									sortable : true,
									dataIndex : 'workFlowStatus',
									renderer:function(v){
										if(v=='0')return '未上报';
										if(v=='1')return '已上报';
										if(v=='2'||v=='3'||v=='4')return '部门主任已审核';
										if(v=='5')return '生产厂长/总工已审核';
										if(v=='6')return '经营厂长已审核';
										if(v=='7')return '经营厂长/厂长已审核';
										if(v=='8')return '厂长已审核';
										if(v=='9')return '财务已审核';
										if(v=='10')return '已退回';
									}
								}],
						viewConfig : {
							forceFit : true
						},
						frame : true,
						bbar : new Ext.PagingToolbar({
									pageSize : limit,
									store : store,
									displayInfo : true,
									displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
									emptyMsg : "没有记录"
								}),
						tbar : [' 报销人:',reportByName,'-',' 用途:',reportUse,'-',' 审批状态:',statusValue,'-',queryButton,'-',dealButton],
						border : true,
						enableColumnHide : false,
						enableColumnMove : false,
						iconCls : 'icon-grid'
					});
	
	gridPanel.on('rowdblclick',updateRecord);
	function updateRecord()
	{
		if (gridPanel.selModel.hasSelection()) {
			var records = gridPanel.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择一条报销单进行审批查询！");
			} else {
		 	var url="";
		 	var workFlowNos=records[0].get('workFlowNo');
        	if(workFlowNos==null||workFlowNos=="")
        	{
			 url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode=cBillwout";
        	}
        	else
        	{
        		url = "/power/workflow/manager/show/show.jsp?entryId="+workFlowNos;
        	}
        	window.open( url);
			}
		} else {
			Ext.Msg.alert("提示", "请选择一条报销单进行审批查询!");
		}
	}
		function queryRecord() {
		store.load({
			params : {
				start : start,
				limit : limit,
				reportorName :reportByName.getValue(),
				reportUse :reportUse.getValue(),
				status :statusValue.getValue(),
				flag:"2"
			}
		});
	}
		queryRecord();
			// 设定布局器及面板
			var layout = new Ext.Viewport({
						layout : "border",
						border : false,
						items : [{
									region : 'center',
									layout : 'fit',
									border : false,
									margins : '1 0 1 1',
									collapsible : true,
									items : [gridPanel]

								}]
					});

		});