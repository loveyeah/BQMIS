Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	// ============== 定义grid ===============
	var MyRecord = Ext.data.Record.create([{
		name : 'model.workticketNo'
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
		name : 'model.workFlowNo'
	}, {
		name : 'model.workticketTypeCode'
	},{
		name : 'model.workticketStausId'
	},
	{ 
	name:'model.firelevelId'},
		{
		name : 'model.isEmergency'
	},
	{name:'model.equAttributeCode'}
	,{name:'repairSpecailName'}
	
	]);

	// 定义获取数据源
	var dataProxy = new Ext.data.HttpProxy({
		url : 'bqworkticket/getApproveList.action'
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

	// 定义类型数据
	var typeDate = new Ext.data.JsonStore({
		root : 'list',
		url : "bqworkticket/getWorkticketTypeList.action",
		fields : ['workticketTypeCode', 'workticketTypeName']
	});
	typeDate.on('load', function(e, records) {
		typeComboBox.setValue(records[0].data.workticketTypeCode);
	});
	typeDate.load();

	// 定义机组或系统数据
	var systemDate = new Ext.data.JsonStore({
		root : 'list',
		url : "bqworkticket/getSystemList.action",
		fields : ['blockCode', 'blockName']
	});
	systemDate.on('load', function(e, records) {
		systemComboBox.setValue(records[0].data.blockCode);
	});
	systemDate.load();

	// 定义状态数据
	var stateDate = new Ext.data.JsonStore({
		root : 'list',
		url : "bqworkticket/getApproveStatusList.action",
		fields : ['workticketStausId', 'workticketStatusName']
	});
	
	stateDate.on('load', function(e, records) {
		stateComboBox.setValue(records[0].data.workticketStausId);
	});
	stateDate.load();

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

	// 定义类型    modify by drdu  090327
	var typeComboBox = new Ext.form.ComboBox({
		id : "typeCob",
		store : typeDate,
		displayField : "workticketTypeName",
		valueField : "workticketTypeCode",
		mode : 'local',
		triggerAction : 'all',
		width : 150,
		readOnly : true
	});

	// 定义所属机组或系统
	var systemComboBox = new Ext.form.ComboBox({
		id : "systemCob",
		store : systemDate,
		displayField : "blockName",
		valueField : "blockCode",
		mode : 'local',
		width : 134,
		triggerAction : 'all',
		readOnly : true
	});

	// 定义状态   modify by drdu  090327
	var stateComboBox = new Ext.form.ComboBox({
		id : "stateCob",
		store : stateDate,
		displayField : "workticketStatusName",
		valueField : "workticketStausId",
		mode : 'local',
		width : 150,
		triggerAction : 'all',
		readOnly : true
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
//                width : 170,
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
         width:390
         });
     //-------------------------   

	// 定义选择列
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		header : '选择',
		width : 35,
		id : 'sm'
	});

		//----add by fyyang - 090311----
	
	   var expander = new  Ext.grid.RowExpander({
        tpl : new Ext.Template(
        //    '<p><b>工作票号:</b> </p><br>',
            '<p><b>工作内容：</b></p>'
        ),
        expandRow:mygridExpend
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
   
	// ---------------------------
	// --gridpanel显示格式定义-----开始-------------------
	var grid = new Ext.grid.GridPanel({
			renderTo:'mygrid',
		region : "center",
		layout : 'fit',
		store : queryStore,
		  viewConfig : {
		forceFit : true 
		},
		columns : [expander, {
			header : "工作编号",
			width : 200,
			sortable : true,
			dataIndex : 'model.workticketNo'
		}, {
			header : "状态",
			sortable : true,
			dataIndex : 'statusName'
		}, {
			header : "工作内容",
			sortable : true,
	      width : 150,
			dataIndex : 'model.workticketContent',
			renderer : function(value) {
				if (value) {
					return value.replace(/\n/g, '<br/>');
				}
				return '';
			}
		}, {
			header : "来源",
			sortable : true,
			dataIndex : 'sourceName'
		}, {
			header : "工作负责人",
			sortable : false,
			dataIndex : 'chargeByName'
		}, {
			header : "所属部门",
			sortable : true,
			dataIndex : 'deptName'
		}, {
			header : "检修专业",
			//width : 70,
			sortable : true,
			dataIndex : 'repairSpecailName'
		}, {
			header : "计划开始时间",
		//	width : 110,
			sortable : true,
			dataIndex : 'planStartDate'
		}, {
			header : "计划结束时间",
		//	width : 110,
			sortable : true,
			dataIndex : 'planEndDate'
		}, {
			header : "所属系统",
			sortable : true,
			dataIndex : 'blockName'
		}, {
			header : "是否紧急票",
		//	width : 90,
			sortable : true,
			dataIndex : 'isEmergencyText'
		}],
		sm : sm,
		// 头部工具栏
		tbar : ['时间范围:', startDate, '~', endDate,'-', '工作票类型:', typeComboBox,
				'-','所属机组或系统:', systemComboBox],
		bbar : new Ext.PagingToolbar({
			pageSize : Constants.PAGE_SIZE,
			store : queryStore,
			displayInfo : true,
			displayMsg : Constants.DISPLAY_MSG,
			emptyMsg : Constants.EMPTY_MSG
		}),
		enableColumnMove : false,
		 plugins: expander,
		  collapsible: true,
        animCollapse: false
	});
	// --gridpanel显示格式定义-----结束--------------------

	     new Ext.Toolbar({  
            renderTo:grid.tbar
           
            ,items:['工作内容：',workticketContent,'-','工作票编号:',{
					id : 'wticketNo',
					name : 'wticketNo',
					xtype : 'textfield'
				}]  
        });  
        
          new Ext.Toolbar({  
            renderTo:grid.tbar
           
            ,items:['状态:', stateComboBox,'-','检修专业：',cbxRepairSpecail,{
					text : Constants.BTN_QUERY,
					iconCls : Constants.CLS_QUERY,
					handler : queryRecord
				}]  
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
			startD : startDate.value,
			endD : endDate.value,
			typeC : typeComboBox.value,
			systemC : systemComboBox.value,
			stateC : stateComboBox.value,
			repairC:cbxRepairSpecail.getValue(),
			deptId:'',//comboDepChoose.value=='-1'?'':comboDepChoose.value,
			content:workticketContent.getValue(),
			wticketNo:Ext.getCmp('wticketNo').getValue()
					
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
		var workFlowNo = record.get('model.workFlowNo');
		var typeCode=record.get('model.workticketTypeCode');
		Ext.Ajax.request({
			url : 'MAINTWorkflow.do?action=getCurrentStepsInfo',
			method : 'POST',
			params : {
				entryId : workFlowNo
			},
			success : function(result, request) {
				var url="";
				var obj = eval("(" + result.responseText + ")");
			//	alert(obj[0].url);
				var args = new Object();
					var title="";
				    var status=record.get("model.workticketStausId");
				      var safetyType="";
				if (obj[0].url==null||obj[0].url=="") {
					//Ext.Msg.alert("提示","无地址");
					url="../sign/workticketSign.jsp";
				
				  
				    //电一、热控一、热力机械一
				    var isEmergency= record.get("model.isEmergency"); 
				    if('1,2,3,5,7,8'.indexOf(typeCode)!=-1)
				    {
							     if(status=="2")
							     {
							     	if(isEmergency=="Y") title="值班人员接票";
							     	else title="工作票签发";
							     	
							     }
							     else if(status=="3")
							     {  // 热控票没有点检签发，直接值长审核 modify by ywliu 20100122
							     	if(typeCode == '5' || typeCode == '8') {
							     		title="值长审核";
							     	} else {
							     		title="点检员签发";
							     	}
							     }
							     else if(status=="17")
							     {
							     	title="值长审核";
							     }
							     else if(status=="18")
							     {
							     	title="值班人员接票";
							     }
							     else if(status=="4")
							     {
							     	if('1,3,5'.indexOf(typeCode)!=-1)
							     	{
							     		title="值长批准";
							     	}
							     	if('2,7,8'.indexOf(typeCode)!=-1)
							     	{
							     	  title="工作许可人许可开工";
							     	}
							     	
							     }
							      else if(status=="5")
							     {
							     	title="值班负责人安措办理";
							     }
							       else if(status=="6")
							     {
							     	title="工作许可人许可开工";
							     }
							       else if(status=="7")
							     {
							     	title="工作负责人终结";
							     }
				    }
				    else title="";
				     args.title=title;
				} 
				else
				{
					//alert(obj[0].url);
					 url = "../../../../../" + obj[0].url; 
				}
			
				    if(typeCode=="4")
				    {
				    	
				    	 if(record.get("model.firelevelId")==1)
				    	 {
				    	  if(status=="2")  title="工作票签发";
						  else if(status==3)  title="许可人安措填写";
						  else if(status==29) title="值长审核";
							else if(status==20)  title="值长审核";
							else if(status==18)  title="消防监护人填写消防部门应采取的安措";
							else if(status==22)  title="消防部门负责人审批";
							else if(status==10) title="安监部门负责人审批";
							else if(status==11) title="领导批准";
							else if(status==23) {title="工作负责人安措办理";  safetyType="repair";}
							else if(status==24) {title="许可人安措办理";  safetyType="run";}
							else if(status==25) { title="消防监护人审批";  safetyType="fire";}
							else if(status==26) title="值长批准"; 
							else if(status==27) title="值班负责人打印";
							else if(status==30) title="工作负责人终结";
							else title="";
				    	 }
				    	 if(record.get("model.firelevelId")==2)
				    	 {
				    	 	 if(status=="2")  title="工作票签发";
						  else if(status==3)  title="许可人安措填写";
							else if(status==20)  title="班长审核";
							else if(status==21)  title="消防人员审核";
							else if(status==22)  title="安监部门负责人审批";
							else if(status==11) title="值长批准";//"领导批准";
							else if(status==23) { title="工作负责人安措办理"; safetyType="repair";}
							else if(status==24) { title="许可人安措办理";  safetyType="run";}
							else if(status==25) title="消防监护人审批"; 
							else if(status==26) title="值班负责人批准"; 
							else if(status==27) title="工作负责人终结";
							else title="";
				    	 }
				    	
				    }
					 args.title=title;
					 
					 
					args.busiNo = record.get('model.workticketNo');
					args.entryId = record.get("model.workFlowNo"); 
					args.flowCode="";
					args.workticketType=record.get("model.workticketTypeCode"); 
					args.busiStatus=record.get("model.workticketStausId");
					args.safetyType=safetyType; //动火票安措类型
					args.fireLevelId=record.get("model.firelevelId");
					args.blockCode=record.get("model.equAttributeCode");
					
				    var  obj=window.showModalDialog(url, args,
							'dialogWidth:'+window.screen.width+';dialogHeight:'+window.screen.height+';directories:yes;help:no;status:no;resizable:yes;scrollbars:yes;');
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