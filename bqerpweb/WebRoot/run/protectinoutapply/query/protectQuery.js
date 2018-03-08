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
	},
	{name:'applyStartDate'},
	{name:'applyEndDate'},
	{name:'power.protectReason'},
	{name:'equName'},
	{name:'applyName'},
	{name:'applyDeptName'}
	
	]);

	// 定义获取数据源
	var dataProxy = new Ext.data.HttpProxy({
		url : 'protect/findProtectQueryList.action'
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
				store : protectApply.queryStatus,
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

	
	//隐蔽删除按钮 lwqi增加
	var btnDelete = new Ext.Button({
		id : "btnDelete",
		text : "删除",
		hidden : true,
		iconCls : 'delete',
		handler : function() {
			var record = grid.getSelectionModel().getSelected();
			if (record) {
				if (confirm("删除后数据不能恢复,确定要删除该条保护投退申请吗?")) {
					var busiNo = record.get("power.protectNo");
					var entryId = record.get("power.workFlowNo");
					Ext.Ajax.request({
						url : "MAINTWorkflow.do?action=mangerDelete",
						method : 'post',
						params : {
							busiType : 'protect',
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
			width : 200,
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
		    
		         },'->',btnDelete,'<div id="divManagerDel">保护投退列表</div>'
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

	grid.on("rowcontextmenu", function(g, i, e) {
		e.stopEvent();
		var record = grid.getStore().getAt(i);
		// 右键菜单
		var menu = new Ext.menu.Menu({
			id : 'mainMenu',
			items : [new Ext.menu.Item({
				text : '票面浏览',
				iconCls : 'pdfview',
				handler : function() {
					workticketPrint(record);
				}
			}), new Ext.menu.Item({
				text : '图形展示',
				iconCls : 'view',
				handler : function() {
					viewFlowPic(record)
				}
			}), new Ext.menu.Item({
				text : '查看审批记录',
				iconCls : 'list',
				handler : function() {
					var entryId = record.get("power.workFlowNo");
					if (entryId != "" && entryId != null) {
						var url = "/power/workflow/manager/approveInfo/approveInfo.jsp?entryId="
								+ entryId;
						window
								.showModalDialog(
										url,
										null,
										"dialogWidth:650px;dialogHeight:400px;;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;");
					} else {
						Ext.Msg.alert("提示", "流程尚未启动");
					}

				}
			})]
		});
		var coords = e.getXY();
		menu.showAt([coords[0], coords[1]]);
	});
	
	
	function viewFlowPic(record) {
		var entryId = record.get("power.workFlowNo");
		var url = "";
		
		if (entryId == "" || entryId == null) {
			
			var flowCode ="bqProtectApply";
			url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
					+ flowCode;
		} else {
			url = "/power/workflow/manager/show/show.jsp?entryId=" + entryId;
		}
		window.open(url);
	}
	
		// 票面浏览
	function workticketPrint(menber) {
		
		var protectNo = menber.get('power.protectNo');

		var strReportAdds = "/powerrpt/report/webfile/bqmis/protectInOutApply.jsp?no="+protectNo;

		
			window.open(strReportAdds);
		

	}
	
	//投退保护隐蔽删除时,按Ctrl+Alt   lwqi增加
document.getElementById("divManagerDel").onclick = function() {
		if ((currentUser == "999999" || currentUser == "1001003")
				&& event.ctrlKey && event.altKey) {
			btnDelete.setVisible(true);
		}
}


});
