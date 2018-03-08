Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function(){
	
	// 系统当天日期
	var sd = new Date();
	var ed = new Date();
	// 系统当天前30天的日期
	sd.setDate(sd.getDate() - 30);
	// 系统当天后30天的日期
	ed.setDate(ed.getDate() + 30);
			
    // 开始时间选择
    var planStartDate = new Ext.form.DateField({
    	id : 'planStartDate',
        width : 90,
        allowBlank : true,
        readOnly : false,
        value : sd,
        format : 'Y-m-d'
    })
    planStartDate.setValue(sd);
    planStartDate.on('change', checkDate);
    
    
    // 结束时间选择
    var planEndDate = new Ext.form.DateField({
    	id : 'planEndDate',
        width : 90,
        allowBlank : true,
        readOnly : false,
        value : ed,
        format : 'Y-m-d'
    })
    planEndDate.setValue(ed);
    planEndDate.on('change', checkDate);
	
	// 检查时间输入
	function checkDate() {
		var strStartDate = Ext.get("planStartDate").dom.value;
		var strEndDate = Ext.get("planEndDate").dom.value;
		if((strStartDate.length == 10 || strStartDate.length == 0)
			&& (strEndDate.length == 10 || strEndDate.length == 0)) {
			if((strStartDate.length == 0) && (strEndDate.length == 0)) {
				planStartDate.setValue("");
				planEndDate.setValue("");
				return true;
			} else if(strStartDate.length == 0) {
				planStartDate.setValue("");
				if((Date.parseDate(strStartDate, 'Y-m-d')) != "undefined") {
					return true;
				} else {
					Ext.Msg.alert(Constants.NOTICE, "日期格式不正确！");
					return false;
				}
			} else if(strEndDate.length == 0) {
				planEndDate.setValue("");
				if((Date.parseDate(strEndDate, 'Y-m-d')) != "undefined") {
					return true;
				} else {
					Ext.Msg.alert(Constants.NOTICE, "日期格式不正确！");
					return false;
				}
			} else {
				var dateStart = Date.parseDate(strStartDate, 'Y-m-d');
				var dateEnd = Date.parseDate(strEndDate, 'Y-m-d');
				if(dateStart != "undefined" && dateEnd != "undefined") {
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
        url : 'workticket/getDetailWorkticketTypeRef.action',
        root : 'list',  
        fields : [{
            name : 'workticketTypeName'},
            {name : 'workticketTypeCode'}]      
    })
    // 工作票种类下拉框
    var workticketTypeCbo = new Ext.form.ComboBox({
        allowBlank : true,
        triggerAction : 'all',
        store :workticketTypeStore,
        displayField : 'workticketTypeName',
        valueField : 'workticketTypeCode',
        mode : 'local',
        readOnly : true,
        width : 100
    })
    
    // 工作票状态下拉框数据源
    var workticketStatusStore = new Ext.data.JsonStore({
        url : 'workticket/getDetailWorkticketStatusRef.action',
        root : 'list',              
        fields : [{
            name : 'workticketStatusName'},
            {name : 'workticketStausId'}]      
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
        width : 160
    })
    
    // 所属机组或系统下拉框数据源
	var equBlockStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'workticket/getEquBlock.action'
		}),
		reader : new Ext.data.JsonReader({
			id : "name"
		}, [{
			name : 'blockCode'
		}, {
			name : 'blockName'
		}])
	    });
 	 	equBlockStore.load();
 	 	equBlockStore.on('load', function(e, records, o) {
			equBlock.setValue(records[0].data.blockCode);
		});
	 
	// 所属机组或系统下拉框	
	var equBlock = new Ext.form.ComboBox({
		fieldLabel : '所属机组或系统',
		store : equBlockStore,
		valueField : "blockCode",
		displayField : "blockName",
		width:140,
		mode : 'local',
		triggerAction : 'all',
		hiddenName : 'permissionDept',
		selectOnFocus : true,
		allowBlank : true,
		readOnly : true
	});
	
	
	//是否标准票
		// 定义状态
	var standardData = new Ext.data.SimpleStore({
				data : [['N','非标准票'],['Y', '标准票']],
				fields : ['value', 'name']
			});
	// 是否标准票
	var standardComboBox = new Ext.form.ComboBox({
				id : "standardCob",
				store : standardData,
				displayField : "name",
				valueField : "value",
				mode : 'local',
				triggerAction : 'all',
				hiddenName:'standardComboBox',
				readOnly : true,
				value:'N',
				width : 100,  
				listeners : {
                 select : standardSelected
                }
			});
	function standardSelected()
	{
		
		if(standardComboBox.getValue()=="Y")
		{
			planStartDate.setDisabled(true);
			planEndDate.setDisabled(true);
		}
		else
		{
			planStartDate.setDisabled(false);
			planEndDate.setDisabled(false);
		}
	}

    
    // 查询按钮
    var queryBtn = new Ext.Button({
        id : 'query',
        text : Constants.BTN_QUERY,
        iconCls : Constants.CLS_QUERY,
        handler : function() {
        	var args = window.dialogArguments;
        	if(checkDate()) {
        		var mySdate=null;
        		var myEdate=null;
        		if(standardComboBox.getValue()=="N")
        		{
        			mySdate=planStartDate.value;
        			myEdate=planEndDate.value;
        		}
        		
	            runGridStore.baseParams = {
					startDate : mySdate,
					endDate : myEdate,
					workticketTypeCode : workticketTypeCbo.value,
					workticketStatusId : workticketStatusCbo.value,
					block : equBlock.value,
					workticketNo:args.workticketNo,
					isStandard:standardComboBox.getValue()
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
    }])
    
    // grid中的store
    var runGridStore = new Ext.data.Store({
        proxy : new Ext.data.HttpProxy({
            url : 'workticket/getHisWorkticketList.action'
        }),
        reader : new Ext.data.JsonReader({
            root : 'list',
            totalProperty : 'totalCount'
        }, runGridList)
    })
    runGridStore.setDefaultSort('model.workticketNo', 'asc');

	workticketTypeStore.on('load', function(e, records) {
	    // 由已终结票生成时
	    	// 工作票种类下拉框
	    	workticketTypeCbo.setValue(window.dialogArguments.workticketTypeCode);
	    	// 设为不可用
	    	workticketTypeCbo.setDisabled(true);
	    // 初始化时,显示所有数据
	    var args = window.dialogArguments;
	    	var myInitSdate=null;
        		var myInitEdate=null;
        		if(standardComboBox.getValue()=="N")
        		{
        			myInitSdate=planStartDate.value;
        			myInitEdate=planEndDate.value;
        		}
	    runGridStore.baseParams = {
			startDate :myInitSdate, //planStartDate.value,
			endDate :  myInitEdate,//planEndDate.value,
			workticketTypeCode : workticketTypeCbo.value,
			workticketStatusId : workticketStatusCbo.value,
			block : equBlock.value,
			workticketNo:args.workticketNo,
			isStandard:standardComboBox.getValue()
		};
		runGridStore.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			}
		});
	});
    workticketTypeStore.load();
    // 选择列:单选
    var sm = new Ext.grid.CheckboxSelectionModel({
        header : '选择',
        id : 'check',
        width : 35,
        singleSelect : true 
    });
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
    
    // 运行执行的Grid主体
    var runGrid = new Ext.grid.GridPanel({
    	region : 'center',
    	layout : 'fit',
        store : runGridStore,
        viewConfig : {
			forceFit : true 
		},
        columns : [ expander,sm, {
                    header : '工作票编号',
                    width : 200,
                    sortable : true,
                    dataIndex : 'model.workticketNo'
                }, {
                    header : '状态',
                   // width : 80,
                    sortable : true,
                    dataIndex : 'statusName'
                }, {
                    header : '来源',
                  //  width : 100,
                    sortable : true,
                    dataIndex : 'sourceName'
                }, {
                    header : '工作负责人',
                   // width : 100,
                    sortable : true,
                    dataIndex : 'chargeByName'
                }, {
                    header : '所属部门',
                  //  width : 100,
                    sortable : true,
                    dataIndex : 'deptName'
                }, {
                    header : '计划开始时间',
                  //  width : 110,
                    sortable : true,
                    dataIndex : 'planStartDate'
                }, {
                    header : '计划结束时间',
                   // width : 110,
                    sortable : true,
                    dataIndex : 'planEndDate'
                }, {
                    header : '所属系统',
                   // width : 100,
                    sortable : true,
                    dataIndex : 'blockName'
                }, {
                    header : '工作内容',
                    hidden:true,
                   // width : 220,
                    sortable : true,
                    dataIndex : 'model.workticketContent',
                    renderer : changeLine
                }, {
                    header : '是否紧急票',
                   // width : 100,
                    sortable : true,
                    dataIndex : 'isEmergencyText'
                }],
       
        tbar : [
//        '时间范围：', planStartDate, '~', planEndDate,
//        		{xtype : "tbseparator"}, '类型：', workticketTypeCbo, '-','所属机组或系统：', equBlock,
            	{xtype : "tbseparator"}, '状态：', workticketStatusCbo,
            	{xtype : "tbseparator"},'种类:',standardComboBox,
            	{xtype : "tbseparator"}, queryBtn],
        //分页
        bbar : new Ext.PagingToolbar({
            pageSize : Constants.PAGE_SIZE,
            store : runGridStore,
            displayInfo : true,
            displayMsg : Constants.DISPLAY_MSG,
            emptyMsg : Constants.EMPTY_MSG
        }),
       sm : sm,
       frame : false,
       border : false,
       enableColumnHide : true,
       enableColumnMove : false,
     //  autoWidth : true,
		 plugins: expander,
		  collapsible: true,
        animCollapse: false
    });
    
    	// modify by fyyang
	var headTbar = new Ext.Toolbar({
		region : 'north',
		items : ['时间范围：', planStartDate, '~', planEndDate, '-', '类型：',
				workticketTypeCbo, '-', '所属机组或系统：', equBlock]

	});
    
    // 判断是否换行
    function changeLine(value) {
    	if(value != null) {
	    	strValue = value.replace(/\r/g, "<br/>");
	    	return strValue;
    	} else {
    		return "";
    	}
    }
    
    // 双击一行，返回此行的相关数据        
    runGrid.on("rowdblclick", returnValues);

    // 弹出窗口
    new Ext.Viewport({
        enableTabScroll : true,   
        layout : "border",
        items : [headTbar,runGrid]
    });
    
    // 返回数据
    function returnValues() {
        if (runGrid.selModel.hasSelection()) {
			var smodel = runGrid.getSelectionModel();
			var selected = smodel.getSelections();
			if (selected.length == 0) {
				Ext.Msg.alert(Constants.NOTICE, Constants.SELECT_NULL_DEL_MSG);
			} else {
				var member = selected[0];
				var object = new Object();
				// 返回工作票编号
				object.refWorkticketNo = member.get('model.workticketNo');
				
				// 判断是“关联主票查询”还是“由已终结票生成”按钮
				var args = window.dialogArguments;
					Ext.Msg.confirm(Constants.SYS_REMIND_MSG, '是否确定选择工作票编号为' + object.refWorkticketNo + '的票',
						function(buttonobj) {
	                    if (buttonobj == "yes") {
							// 执行终结票的方法
							Ext.lib.Ajax.request('POST',
								'workticket/updateDetailWorkticketInfoByOld.action', {
								success : function(action) {
									var o = eval("(" + action.responseText + ")");
									Ext.Msg.alert(Constants.NOTICE, o.msg);
									window.returnValue = object;
									window.close();
								},
								faliue : function() {
									Ext.Msg.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
								}
							}, "workticketNoNew=" + args.workticketNo
								+ "&workticketNoOld=" + member.get('model.workticketNo'));
	                    }
                    });
				
			}
        }
    }
});