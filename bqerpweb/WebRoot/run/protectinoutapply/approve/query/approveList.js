Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	
	var protectApply=new ProtectApply();

	// ============== 定义grid ===============
	var MyRecord = Ext.data.Record.create([{
		name : 'power.protectNo'
	}, {
		name : 'power.applyBy'
	}, {
		name : 'power.applyDept'
	}, {
		name : 'power.protectName'
	}, {
		name : 'power.workFlowNo'
	}, {
		name : 'power.statusId'
	}, {
		name : 'power.equEffect'
	}, {
		name : 'power.outSafety'
	}, {
		name : 'power.memo'
	},
	{name:'applyStartDate'},
	{name:'applyEndDate'},
	{name:'power.protectReason'},
	{name:'equName'},
	{name:'applyName'},
	{name:'applyDeptName'}
	, {
		name : 'applyDate'
	}, {
		name : 'power.isIn'
	}
	]);

	// 定义获取数据源
	var dataProxy = new Ext.data.HttpProxy({
		url : 'protect/findProtectApproveList.action'
	});

	// 定义格式化数据
	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);

	// 定义封装缓存数据的对象
	var queryStore = new Ext.data.Store({
		// 访问的对象
		proxy : dataProxy,
		// 处理数据的对象
		reader : theReader
	});





	// 系统当天日期
	var sd = new Date();
	var ed = new Date();
	// 系统当天前30天的日期
	sd.setDate(sd.getDate() - 30);
	// 系统当天后30天的日期
	ed.setDate(ed.getDate() + 30);

	// 定义查询起始时间
	var startDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : 'startDate',
		id : 'startDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		value : sd,
		readOnly : false,
		checked : true,
		emptyText : '请选择',
		width : 85,
		listeners : {
			change : function() {
				// 判断输入的开始时间是否大于结束时间
				if (startDate.value != null && endDate.value != null) {
					if (Date.parseDate(startDate.value, "Y-m-d") > endDate
							.parseDate(endDate.value, "Y-m-d")) {
						Ext.Msg.alert(Constants.SYS_REMIND_MSG,
								"输入的起始时间大于结束时间，请重新输入！");
						return false;
					}
				}
			}
		}
	});

	// 定义查询结束时间
	var endDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : 'endDate',
		id : 'endDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		value : ed,
		readOnly : false,
		checked : true,
		emptyText : '请选择',
		width : 85,
		listeners : {
			change : function() {
				// 判断输入的开始时间是否大于结束时间
				if (startDate.value != null && endDate.value != null) {
					if (Date.parseDate(startDate.value, "Y-m-d") > endDate
							.parseDate(endDate.value, "Y-m-d")) {
						Ext.Msg.alert(Constants.SYS_REMIND_MSG,
								"输入的起始时间大于结束时间，请重新输入！");
						return false;
					}
				}
			}
		}
	});

	



    
    //所属部门
    var topDeptRootNode =  new Ext.tree.AsyncTreeNode({
		id : '-1',
		text : '所有'
	})
    var comboDepChoose = new Ext.ux.ComboBoxTree({
                labelwidth : 50,
                fieldLabel : '部门',
                id : 'deptId',
                displayField : 'text',
                width : 170,
                valueField : 'id',
                hiddenName : 'mydeptId',
                blankText : '请选择',
                value : topDeptRootNode,
                emptyText : '请选择', 
                readOnly : true,
                tree : {
                    xtype : 'treepanel',
                    loader : new Ext.tree.TreeLoader({
                                dataUrl : 'comm/getDeptsByPid.action'
                            }),
                    root :  new Ext.tree.AsyncTreeNode({
                                id : '-1',
                                text : '所有'
                            })
                }
                //selectNodeModel : 'exceptRoot',
                ,selectNodeModel : 'all'
              
            })
    //-------------
 
  
	// 定义状态
	var stateComboBox = new Ext.form.ComboBox({
				id : "stateCob",
				store : protectApply.approveStatus,
				displayField : "name",
				valueField : "value",
				mode : 'local',
				triggerAction : 'all',
				hiddenName:'stateComboBox',
				readOnly : true,
				value:'',
				width : 140
			});

	// 定义选择列
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		header : '选择',
		width : 35,
		id : 'sm'
	});

		
	// --gridpanel显示格式定义-----开始-------------------
	var grid = new Ext.grid.GridPanel({
			
		region : "center",
		layout : 'fit',
		store : queryStore,
		  viewConfig : {
		forceFit : true 
		},
		columns : [ {
			header : "申请单号",
			width : 100,
			sortable : true,
			dataIndex : 'power.protectNo'
		},  {
			header : "状态",
			sortable : true,
			dataIndex : 'power.statusId',
			renderer:function(value)
			{
				
				return protectApply.getStatusName(value);
			}
		},{
			header : "申请人",
			sortable : true,
			dataIndex : 'applyName'
		}, {
			header : "申请部门",
			sortable : true,
			dataIndex : 'applyDeptName'
		},{
			header : "保护名称",
			sortable : true,
			dataIndex : 'power.protectName'
		}, {
			header : "工作流实例号",
			sortable : false,
			hidden:true,
			dataIndex : 'power.workFlowNo'
		},
		{
			header : "设备名称",
			sortable : false,
			width:120,
			dataIndex : 'equName'
		},
		{
			header : "投退原因",
			sortable : false,
			dataIndex : 'power.protectReason'
		},
		{
			header : "申请开工时间",
			sortable : false,
			dataIndex : 'applyStartDate'
		},
		{
			header : "申请完工时间",
			sortable : false,
			dataIndex : 'applyEndDate'
		}],
		sm : sm,
		// 头部工具栏
		tbar : ['时间范围:', startDate, '~', endDate,'-','申请部门:',comboDepChoose,
		         '-','状态：',stateComboBox,
		         {
		         	text:'查询',
		         	iconCls : 'query',
		         	handler:queryRecord
		    
		         }
				],
		bbar : new Ext.PagingToolbar({
			pageSize : Constants.PAGE_SIZE,
			store : queryStore,
			displayInfo : true,
			displayMsg : Constants.DISPLAY_MSG,
			emptyMsg : Constants.EMPTY_MSG
		}),
		enableColumnMove : false
	});
	
	
	
	// 注册双击事件
	grid.on("rowdblclick", edit);

	// 页面加载显示数据
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [grid]
	});

	/**
	 * 查询
	 */
	function queryRecord() {
		// 判断输入的开始时间是否大于结束时间
		if (startDate.value != null && endDate.value != null) {
			if (Date.parseDate(startDate.value, "Y-m-d") > endDate.parseDate(
					endDate.value, "Y-m-d")) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, "输入的起始时间大于结束时间，请重新输入！");
				return false;
			}
		}
		queryStore.baseParams = {
			startDate : startDate.value,
			endDate : endDate.value,
			status : stateComboBox.getValue(),
			applyDept:comboDepChoose.value=='-1'?'':comboDepChoose.value
			
					
		};
		queryStore.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			}
		});
	}

	grid.on("load", queryRecord());

	
	
	/**
	 * 双击grid事件
	 */
	function edit() {
			var record = grid.getSelectionModel().getSelected();
		var workFlowNo = record.get('power.workFlowNo');
	
		Ext.Ajax.request({
			url : 'MAINTWorkflow.do?action=getCurrentStepsInfo',
			method : 'POST',
			params : {
				entryId : workFlowNo
			},
			success : function(result, request) {
				var url="";
				var obj = eval("(" + result.responseText + ")");
			
				var args = new Object();
					var title="";
				    var status=record.get("power.statusId");
				    
				if (obj[0].url==null||obj[0].url=="") {
					
					url="../equDeptApproveSign/equDeptSign.jsp";
				    if(status==3) title="设备部专工签发";
				     if(status==4) title="设备部主任签发";
				     if(status==6) title="当值值长签发";
				     if(status==8) title="当值班长许可开工";
				} 
				else
				{
					//alert(obj[0].url);
					 url = "../../../../" + obj[0].url; 
				}
			
				  
					 args.title=title;
					 
				    args.record = record;
					args.busiNo = record.get('power.protectNo');
					args.entryId = record.get("power.workFlowNo"); 
					args.flowCode="";
					args.isIn=record.get("power.isIn"); 
					args.busiStatus=status;
					
				    var  obj=window.showModalDialog(url, args,
							'status:no;dialogWidth=750px;dialogHeight=550px');
					if(obj)
					{
						
					queryRecord();
					}
				
			},
			failure : function(result, request) {
				Ext.Msg.alert("提示","错误");
			}

		});

	}
});