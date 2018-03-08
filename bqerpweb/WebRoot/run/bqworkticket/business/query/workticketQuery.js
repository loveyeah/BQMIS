Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {

	// 系统当天日期
	var sd = new Date();
	var ed = new Date();
	// 系统当天前30天的日期
	sd.setDate(sd.getDate() - 30);
	// 系统当天后30天的日期
	ed.setDate(ed.getDate() + 30);

	// 开始时间选择
	var startDate = new Ext.form.DateField({
		id : 'startDate',
		width : 100,
		allowBlank : true,
		readOnly : false,
		value : sd,
		format : 'Y-m-d'
	})
	startDate.setValue(sd);
	startDate.on('change', checkDate);

	// 结束时间选择
	var endDate = new Ext.form.DateField({
		id : 'endDate',
		width : 100,
		allowBlank : true,
		readOnly : false,
		value : ed,
		format : 'Y-m-d'
	})
	endDate.setValue(ed);
	endDate.on('change', checkDate);

	// 检查时间输入
	function checkDate() {
		var strStartDate = Ext.get("startDate").dom.value;
		var strEndDate = Ext.get("endDate").dom.value;
		if ((strStartDate.length == 10 || strStartDate.length == 0)
				&& (strEndDate.length == 10 || strEndDate.length == 0)) {
			if ((strStartDate.length == 0) && (strEndDate.length == 0)) {
				startDate.setValue("");
				endDate.setValue("");
				return true;
			} else if (strStartDate.length == 0) {
				startDate.setValue("");
				if ((Date.parseDate(strStartDate, 'Y-m-d')) != "undefined") {
					return true;
				} else {
					Ext.Msg.alert(Constants.NOTICE, "日期格式不正确！");
					return false;
				}
			} else if (strEndDate.length == 0) {
				endDate.setValue("");
				if ((Date.parseDate(strEndDate, 'Y-m-d')) != "undefined") {
					return true;
				} else {
					Ext.Msg.alert(Constants.NOTICE, "日期格式不正确！");
					return false;
				}
			} else {
				var dateStart = Date.parseDate(strStartDate, 'Y-m-d');
				var dateEnd = Date.parseDate(strEndDate, 'Y-m-d');
				if (dateStart != "undefined" && dateEnd != "undefined") {
					if (dateStart.getTime() > dateEnd.getTime()) {
						Ext.Msg.alert(Constants.NOTICE, "开始时间必须小于结束时间！");
						return false;
					} else {
						return true;
					}
				} else {
					Ext.Msg.alert(Constants.NOTICE, "日期格式不正确！");
					return false;
				}
			}
		} else {
			Ext.Msg.alert(Constants.NOTICE, "日期格式不正确！");
			return false;
		}
	}

	// 工作票类型下拉框数据源
	var workticketTypeStore = new Ext.data.JsonStore({
		url : 'workticket/getQueryWorkticketType.action',
		root : 'list',
		fields : [{
			name : 'workticketTypeName'
		}, {
			name : 'workticketTypeCode'
		}]
	})
	workticketTypeStore.load();
	workticketTypeStore.on('load', function(e, records, o) {
		workticketTypeCbo.setValue(records[0].data.workticketTypeCode);
	});

	// 工作票种类下拉框
	var workticketTypeCbo = new Ext.form.ComboBox({
		allowBlank : true,
		triggerAction : 'all',
		store : workticketTypeStore,
		displayField : 'workticketTypeName',
		valueField : 'workticketTypeCode',
		mode : 'local',
		readOnly : true,
		width : 150
	})

	// 工作票状态下拉框数据源
	var workticketStatusStore = new Ext.data.JsonStore({
		url : 'workticket/getQueryWorkticketStatus.action',
		root : 'list',
		fields : [{
			name : 'workticketStatusName'
		}, {
			name : 'workticketStausId'
		}]
	})
	workticketStatusStore.load();
	workticketStatusStore.on('load', function(e, records, o) {
		workticketStatusCbo.setValue(records[0].data.workticketStausId);
	});

	// 工作票状态下拉框
	var workticketStatusCbo = new Ext.form.ComboBox({
		allowBlank : true,
		triggerAction : 'all',
		store : workticketStatusStore,
		displayField : 'workticketStatusName',
		valueField : 'workticketStausId',
		mode : 'local',
		readOnly : true,
		width : 150
	})

	// 所属机组或系统下拉框数据源
	var equBlockStore = new Ext.data.JsonStore({
		url : 'workticket/getQueryEquBlock.action',
		root : 'list',
		fields : [{
			name : 'blockName'
		}, {
			name : 'blockCode'
		}]
	})
	equBlockStore.load();
	equBlockStore.on('load', function(e, records, o) {
		equBlockCbo.setValue(records[0].data.blockCode);
	});

	// 所属机组或系统下拉框
	var equBlockCbo = new Ext.form.ComboBox({
		allowBlank : true,
		triggerAction : 'all',
		store : equBlockStore,
		displayField : 'blockName',
		valueField : 'blockCode',
		mode : 'local',
		readOnly : true,
		width : 120
	});
	
	    // 检修专业
    var storeRepairSpecail = new Ext.data.Store({
        proxy : new Ext.data.HttpProxy({
            url : 'workticket/getDetailRepairSpecialityType.action'
        }),
        reader : new Ext.data.JsonReader({
            root : 'list'
        }, [{
                name : 'specialityCode',
                mapping : 'specialityCode'
            }, {
                name : 'specialityName',
                mapping : 'specialityName'
            }])
    });
    storeRepairSpecail.load();
    var cbxRepairSpecail = new Ext.form.ComboBox({
        id : 'repairSpecailCode',
        fieldLabel : "检修专业:",
        store : storeRepairSpecail,
        displayField : "specialityName",
        valueField : "specialityCode",
        hiddenName : 'repairCode',
        mode : 'local',
        triggerAction : 'all',
        value : '',
        readOnly : true,
        width : 112
    });
    
    storeRepairSpecail.on('load', function(e, records)
    {
    	var myrecord=new Ext.data.Record({
    	specialityName:'所有',
    	specialityCode:''}
    	);
    	storeRepairSpecail.insert(0,myrecord);
    	cbxRepairSpecail.setValue('');
    });
    
//    //所属班组
//    var topDeptRootNode =  new Ext.tree.AsyncTreeNode({
//		id : '-1',
//		text : '所有'
//	})
//    var comboDepChoose = new Ext.ux.ComboBoxTree({
//                labelwidth : 50,
//                fieldLabel : '部门',
//                id : 'deptId',
//                displayField : 'text',
//                width : 210,
//                valueField : 'id',
//                hiddenName : 'mydeptId',
//                blankText : '请选择',
//                value : topDeptRootNode,
//                emptyText : '请选择', 
//                readOnly : true,
//                tree : {
//                    xtype : 'treepanel',
//                    loader : new Ext.tree.TreeLoader({
//                                dataUrl : 'comm/getDeptsByPid.action'
//                            }),
//                    root :  new Ext.tree.AsyncTreeNode({
//                                id : '-1',
//                                text : '所有'
//                            })
//                }
//                //selectNodeModel : 'exceptRoot',
//                ,selectNodeModel : 'all'
//              
//            })
    //-------------
 
     //---------工作内容----------
         var workticketContent =new Ext.form.TextArea({
         id:'wContent',
         name:'wContent',
         height:20,
         width:440
         });

     //-------------------------       
	// 查询按钮
	var queryBtn = new Ext.Button({
		id : 'query',
		text : Constants.BTN_QUERY,
		iconCls : Constants.CLS_QUERY,
		handler : function() {
			
			if (checkDate()) {
				runGridStore.baseParams = {
					startDate : startDate.value,
					endDate : endDate.value,
					workticketTypeCode : workticketTypeCbo.value,
					workticketStatusId : workticketStatusCbo.value,
					block : equBlockCbo.value,
					repairC:cbxRepairSpecail.getValue(),
					deptId:'',//comboDepChoose.value=='-1'?'':comboDepChoose.value,
					content:workticketContent.getValue(),
					wticketNo:Ext.getCmp('wticketNo').getValue() //Ext.get('wticketNo').dom.value,
					
					
				};
				runGridStore.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});
			}
		}
	});

	// grid中的数据
	var runGridList = new Ext.data.Record.create([{
		name : 'model.workticketNo'
	}, {
		name : 'model.workticketTypeCode'
	}, {
		name : 'model.workFlowNo'
	}, {
		name : 'statusName'
	}, {
		name : 'sourceName'
	}, {
		name : 'chargeByName'
	}, {
		name : 'deptName'
	}, {
		name : 'planStartDate'
	}, {
		name : 'planEndDate'
	}, {
		name : 'blockName'
	}, {
		name : 'model.workticketContent'
	}, {
		name : 'isEmergencyText'
	}, {
		name : 'model.firelevelId'
	}
	,{name:'repairSpecailName'}
	])

	// grid中的store
	var runGridStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'workticket/getQueryWorkticketList.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list',
			totalProperty : 'totalCount'
		}, runGridList)
	})
	runGridStore.setDefaultSort('model.workticketNo', 'asc');

	// 初始化时,显示所有数据
	runGridStore.baseParams = {
		startDate : startDate.value,
		endDate : endDate.value,
		workticketTypeCode : workticketTypeCbo.value,
		workticketStatusId : workticketStatusCbo.value,
		block : equBlockCbo.value,
		repairC:cbxRepairSpecail.getValue(),
		deptId:'',
		workticketContent:'',
		wticketNo:''
	};
	runGridStore.load({
		params : {
			start : 0,
			limit : Constants.PAGE_SIZE
		}
	});
	// 选择列
	var smCSM = new Ext.grid.CheckboxSelectionModel({
		header : "选择",
		id : "check",
		width : 35,
		singleSelect : true
	});

	// ----add by fyyang - 090311----

	var expander = new Ext.grid.RowExpander({
		tpl : new Ext.Template('<p><b>工作内容：</b></p>'),
		expandRow : mygridExpend
	});
	   function  mygridExpend(row)
    {
    	   if(typeof row == 'number'){
            row = this.grid.view.getRow(row);
        }
        var record = this.grid.store.getAt(row.rowIndex);
       var no=record.get("model.workticketNo");
       var content="";
       //-----------------------
       Ext.Ajax.request({
				url : 'workticket/getContentWorkticketByNo.action',
				params : {
					workticketNo :  no 
				},
				method : 'post',
				success : function(result, request) {
					var content=result.responseText;
					content=content.substring(1,content.length-1);
				    while(content.indexOf('\\r')!=-1)
				    {
				    
				    	content=content.replace("\\r","");
				    }
				     while(content.indexOf('\\n')!=-1)
				     {
				    content=  content.replace("\\n","<br>"); 
				     } 
				     expander.tpl = new Ext.Template(
						'<p> <br><font color="blue"><b>工作内容：</b><br>'+content+'</font></p>');
					var body = Ext.DomQuery.selectNode(
								'tr:nth(2) div.x-grid3-row-body', row);
						if (expander.beforeExpand(record, body, row.rowIndex)) {
							expander.state[record.id] = true;
							Ext.fly(row).replaceClass('x-grid3-row-collapsed',
									'x-grid3-row-expanded');
							expander.fireEvent('expand', expander, record, body, row.rowIndex);
						}
			
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('错误', '操作失败!');
				}
			});

 
    }
    //----------------删除-----add by fyyang 090421-----
       var btnDelete = new Ext.Button({
		id : "btnDelete",
		text : "删除",
		hidden:true,
		iconCls:'delete',
 		handler : function() {
 			var record = runGrid.getSelectionModel().getSelected();
			if (record) {
				if (confirm("删除后数据不能恢复,确定要删除该条工作票吗?")) {
					var busiNo = record.get("model.workticketNo");
					var entryId = record.get("model.workFlowNo");
					Ext.Ajax.request({
						url : "MAINTWorkflow.do?action=mangerDelete",
						method : 'post',
						params : {
							busiType : 'workticket',
							entryId : entryId,
							busiNo : busiNo
						},
						success : function(result, request) {
							Ext.Msg.alert("提示", "操作成功!");
							runGrid.getStore().remove(record);
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
    //-------------------------------------------------
	
	
	var columnModel = new Ext.grid.ColumnModel([ {
		header : '工作票编号',
		width : 200,
		sortable : true,
		dataIndex : 'model.workticketNo'
	}, {
			header : "工作内容",
			width : 300,
			sortable : true,
			//hidden:true,
			dataIndex : 'model.workticketContent',
			renderer:function(value, metadata, record){ 
			metadata.attr = 'style="white-space:normal;"'; 
			return value;  
	       }
	},
		
			{
		header : '状态',
		width : 120,
		sortable : true,
		dataIndex : 'statusName'
	}, 

		{
		header : '所属系统',
//		width : 120,
		sortable : true,
		dataIndex : 'blockName'
	}, {
		header : '来源',
	//	width : 100,
		sortable : true,
		dataIndex : 'sourceName'
	}, {
		header : '工作负责人',
	//	width : 70,
		sortable : true,
		dataIndex : 'chargeByName'
	}, {
		header : '所属部门',
//		width : 100,
		sortable : true,
		dataIndex : 'deptName'
	}, {
			header : "检修专业",
			//width : 70,
			sortable : true,
			dataIndex : 'repairSpecailName'
		}, {
		header : '计划开始时间',
		width : 110,
		sortable : true,
		dataIndex : 'planStartDate'
	}, {
		header : '计划结束时间',
		width : 110,
		sortable : true,
		dataIndex : 'planEndDate'
	}, {
		header : '是否紧急票',
//		width : 70,
		align:'center',
		sortable : true,
		dataIndex : 'isEmergencyText' 
	}]); 
	// ---------------------------
	
		// modify by fyyang
	var headTbar = new Ext.Toolbar({
		//region : 'north',
		items : ['时间范围：', startDate, '~', endDate, '-', '工作票类型：',
				workticketTypeCbo, '-', '所属机组或系统：', equBlockCbo]

	});

	// 运行执行的Grid主体
	var runGrid = new Ext.grid.GridPanel({
		renderTo:'mygrid',
		store : runGridStore,
		sm : smCSM,
		region : 'center',
		layout : 'fit',
		frame : false,
		border : false,
		enableColumnHide : true,
		enableColumnMove : false,
		autoWidth : true,
	
		tbar : headTbar,//['',{text:''}],
		viewConfig: {  
            forceFit:true 
		},
		cm : columnModel, 
		bbar : new Ext.PagingToolbar({
			pageSize : Constants.PAGE_SIZE,
			store : runGridStore,
			displayInfo : true,
			displayMsg : Constants.DISPLAY_MSG,
			emptyMsg : Constants.EMPTY_MSG
		})
	});  
	
	
	runGrid.on("rowdblclick", gridDbl);
	
	function gridDbl()
	{
		var record = runGrid.getSelectionModel().getSelected();	
		workticketPrint(record);
	}
	


	function viewFlowPic(record) {
		var entryId = record.get("model.workFlowNo");
		var url = "";
		if (entryId == "" || entryId == null) {
			var workticketCode = record.get('model.workticketTypeCode');
			var flowCode = getFlowCode(workticketCode, record
					.get('model.firelevelId'));
			url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
					+ flowCode;
		} else {
			url = "/power/workflow/manager/show/show.jsp?entryId=" + entryId;
		}
		window.open(url);
	}

	runGrid.on("rowcontextmenu", function(g, i, e) {
		e.stopEvent();
		var record = runGrid.getStore().getAt(i);
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
					var entryId = record.get("model.workFlowNo");
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
			}), new Ext.menu.Item({
				text : '危险点票面预览',
				iconCls : 'pdfview',
				handler : function() {
					
					dangerPrint(record);
				
				}
			})]
		});
		var coords = e.getXY();
		menu.showAt([coords[0], coords[1]]);
	});
	// 票面浏览
	function workticketPrint(menber) {
		var workticketNo;
		var workticketCode;
		workticketNo = menber.get('model.workticketNo');
		workticketCode = menber.get('model.workticketTypeCode');

		var strReportAdds = getReportUrl(workticketCode, workticketNo, menber
				.get('model.firelevelId'));

		if (strReportAdds != "") {
			window.open(strReportAdds);
		} else {
			Ext.Msg.alert("目前没有该种工作票票面预览");
		}

	}
	
	function dangerPrint(menber)
	{
		var workticketNo;
		var workticketCode;
		workticketNo = menber.get('model.workticketNo');
		workticketCode = menber.get('model.workticketTypeCode');
//		if(workticketCode=="4")
//		{
//				Ext.Msg.alert("提示", "动火票无危险点！");
//		}
//		else
//		{
			var url = '/powerrpt/report/webfile/bqmis/workticketDangerControl.jsp?workticketNo='+workticketNo;
			window.open(encodeURI(url));
		//}
	}

	
	
	
	//-----------tbar----
	       new Ext.Toolbar({  
            renderTo:runGrid.tbar
           
            ,items:['工作内容：',workticketContent,'-','工作票编号：',{
					id : 'wticketNo',
					name : 'wticketNo',
					xtype : 'textfield'
				}]  
        }); 
        //---------------------tbar 工作票编号 
      /*  new Ext.Toolbar({  
            renderTo:runGrid.tbar
           
            ,items:['工作票编号：',workerticketNo]  
        });   */
	
	
     new Ext.Toolbar({
		 renderTo:runGrid.tbar,
		items : ['工作票状态：', workticketStatusCbo, '-','检修专业：',cbxRepairSpecail,
		
		'-', queryBtn, '-', {
			xtype : 'button',
			text : '票面浏览',
			iconCls : 'pdfview',
			handler : function() {
				var record = runGrid.getSelectionModel().getSelected();
				if (record) {
					workticketPrint(record);
				} else {
					Ext.Msg.alert("提示", "请选择一条记录!");
				}
			}
		}, {
			xtype : 'button',
			text : '流程展示',
			iconCls : 'view',
			handler : function() {
				var record = runGrid.getSelectionModel().getSelected();
				if (record) {
					viewFlowPic(record);
				} else {
					Ext.Msg.alert("提示", "请选择一条记录!");
				}
			}
		},
		{
			xtype : 'button',
			text : '危险点票面预览',
			iconCls : 'pdfview',
			handler : function()
			{
					var record = runGrid.getSelectionModel().getSelected();
				if (record) {
					dangerPrint(record);
				} else {
					Ext.Msg.alert("提示", "请选择一条记录!");
				}
				
			}
		}
		]
	});
        
        new Ext.Toolbar({  
            renderTo:runGrid.tbar
            ,items:['<font color="red">(右键查询相关信息)</font>','->',btnDelete,'<div id="divManagerDel">工作票列表</div>']  
        });   
        
    
        
    
    
	//-------------------------
	// 设定布局器及面板
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [runGrid]
	});
	
		document.getElementById("divManagerDel").onclick = function() {
		if ((currentUser == "999999" || currentUser == "1001003") && event.ctrlKey && event.altKey) {
			 btnDelete.setVisible(true);  
		}  
	};
	
	
  


});