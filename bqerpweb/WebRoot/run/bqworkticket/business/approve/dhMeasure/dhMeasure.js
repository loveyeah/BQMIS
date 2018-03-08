Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.form.Label.prototype.setText = function(argText) {
	this.el.dom.innerHTML = argText;
}
// 获取时间
function getDate() {
    var d, s, t;
    d = new Date();
    s = d.getFullYear().toString(10) + "-";
    t = d.getMonth() + 1;
    s += (t > 9 ? "" : "0") + t + "-";
    t = d.getDate();
    s += (t > 9 ? "" : "0") + t + " ";
    t = d.getHours();
    s += (t > 9 ? "" : "0") + t + ":";
    t = d.getMinutes();
    s += (t > 9 ? "" : "0") + t + ":";
    t = d.getSeconds();
    s += (t > 9 ? "" : "0") + t;
    return s;
}
var nrs = "";
    function removeAppointNextRoles(){
	if(confirm("确定要清除指定下一步人吗？")){
		nrs = "";
		Ext.get("showAppointNextRoles").dom.innerHTML = "";
	}
}
Ext.onReady(function() {
    var arg = window.dialogArguments
    
    var strBusiNo = arg.busiNo;
    var workticketType=arg.workticketType;
    var entryId =arg.entryId;
    var flowCode=arg.flowCode;
    var mytitle="动火票测量";
    var actionId="";
    var eventIdentify="";
     var workerCode="";
     
     var busiStatus=arg.busiStatus;//add 
    var ifSeeDanger=false;
    var isSeePrint=false;	
      var fireLevelId=arg.fireLevelId;
      
      var workerName="";
         var blockCode=arg.blockCode;
   
    //---add by fyyang 090407 --增加滚动字幕--
    var noticeInfoText=getApproveNoticeInfo(blockCode);

     var noticeInfoLabel=new Ext.form.Label({
        id : "label",
        html : '<marquee scrollamount="2" direction="Left" >'+noticeInfoText+'</marquee >', 
        style : "color:red;font-size:20px;font-weight:bold"
    }); 
     var noticeField = new Ext.Panel({
        border : true,
        layout:'form',
        height : 40,
        items : [noticeInfoLabel]
     });
    //----------------
    
    // ↓↓*******************停送电申请单上报页面**************************************
    

    //停送电申请单上报label
    var label = new Ext.form.Label({
        id : "label",
        text : mytitle,
         style : "color:blue;font-size:25px;line-height:50px;padding-left:150px;font-weight:bold"
    });
    // 标题
    var titleField = new Ext.Panel({
        border : false,
        height : 60,
        items : [label]
    })
    
    // 设置响应时间
    var timeCheck = new Ext.form.Checkbox({
        boxLabel : "设置响应时间",
        style : "padding-top:120;border-width:1px 0 0 0;",
        listeners : {
            check : function(box, checked) {
                if (checked) {
                    // 如果checkbox选中,显示时间选择textfield
                    Ext.getCmp('datePicker').setVisible(true);
                } else {
                    Ext.getCmp('datePicker').setVisible(false);
                }
            }
        }
    });
    // 时间选择
    var datePicker = new Ext.form.TextField({
        style : 'cursor:pointer',
        id : "datePicker",
        hidden : true,
        value : getDate(),
        listeners : {
            focus : function() {
                WdatePicker({
                            startDate : '%y-%M-01 00:00:00',
                            dateFmt : 'yyyy-MM-dd HH:mm:ss',
                            alwaysUseStartDate : true
                        });
            }
        }
    });
    // 图形显示
    var graphButton = new Ext.Button({
        text : "图形展示",
        handler : function(){  
        	var url="";
        	if(entryId==null||entryId=="")
        	{
			 url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="+flowCode;
        	}
        	else
        	{
        		url = "/power/workflow/manager/show/show.jsp?entryId="+entryId;
        	}
        	window.open(url);
		}
    });
    // 查看审批记录
    var approveHistoryButton = new Ext.Button({
        text : "查看审批记录",
        handler : function() {
			var url = "/power/workflow/manager/approveInfo/approveInfo.jsp?entryId="
					+ entryId;
			window.showModalDialog(
							url,
							null,
							"dialogWidth:650px;dialogHeight:400px;;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;");
		}
    });
    // 显示下步角色
    
    var btnNextDisplay = new Ext.Button({
        text : "显示下步角色",
        handler:getNextSteps
    });
    // 显示指定下一步角色
	var showAppointNextRoles = new Ext.form.Label({
		border : false,
		id:'showAppointNextRoles',
		style : "color:red;font-size:12px"
	});
    	function getNextSteps() {
		//var actionId = Ext.ComponentMgr.get('approve-radio').getGroupValue();
		var args = new Object();
		args.flowCode =flowCode;
		args.actionId =actionId;
		args.entryId = entryId;
		var url="";
		if(entryId==null||entryId=="")
		{
			url = "/power/workflow/manager/appointNextRole/appointNextRoleForReport.jsp";
		}
		else
		{
		 url = "/power/workflow/manager/appointNextRole/appointNextRole.jsp";
		}
		
		var ro = window
				.showModalDialog(
						url,
						args,
						"dialogWidth:400px;dialogHeight:350px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;");
		if (typeof(ro) != "undefined") {
			if (ro) {
				nrs = ro.nrs;
				showAppointNextRoles
						.setText("<span style=\"cursor:hand;color:blue\"  onclick=\"removeAppointNextRoles();\">[取消指定]</span>  您指定下一步角色为：[ "
								+ ro.nrsname + " ]");
			}
		}
	};
    // 第二行
    var tableField = new Ext.Panel({
        border : false,
        height : 70,
        autoWidth : false,
        anchor : '100%',
        style : "padding-top:20;padding-bottom:20;border-width:1px 0 0 0;",
        layout : "column",
        items : [
				// 设置响应时间checkbox
				{
					columnWidth : 0.185,
					id : "tiemcheck",
					layout : "form",
					hideLabels : true,
					border : false,
					items : [timeCheck]
				},
				// 时间选择
				{
					columnWidth : 0.265,
					hideLabels : true,
					id : "datepickerPanel",
					layout : "form",
					border : false,
					items : [datePicker]
				}, {
					columnWidth : 0.02,
					border : false
				},
				// 图形显示按钮
				{
					columnWidth : 0.145,
					layout : "form",
					hideLabels : true,
					border : false,
					items : [graphButton]
				},
				// 查看审批记录按钮
				{
					columnWidth : 0.19,
					layout : "form",
					hideLabels : true,
					border : false,
					id:'approve_his',
					items : [approveHistoryButton]
				},
				// 下一步按钮
				{
					columnWidth : 0.19,
					layout : "form",
					hideLabels : true,
					border : false,
					id:'approve_next',
					items : [btnNextDisplay]
				}]

    });
    // 审批方式label
    var approveLabel = new Ext.form.Label({
        text : " 审批方式：",
        style : 'font-size:12px'
    });
    // 单选按钮组
    var radioSet = new Ext.Panel({
        height : 50,
        border : false,
        layout : "column"
    });
    // 审批方式panel
    var approveSet = new Ext.Panel({
        height : 80,
        border : false,
        layout : 'form',
        style : "padding-top:20;border-width:1px 0 0 0;",
        items : [approveLabel, radioSet]
    });

    /**
	 * 审批方式radio button生成
	 */

	function radioAddHandler(radio) {
		
		if (radio instanceof Array) {
			if (!radioSet.items) {
				radioSet.items = new Ext.util.MixedCollection();
			}
			if (radio.length > 0) {
				var _radio = new Ext.form.Radio({
					boxLabel : "<font size='2.6px'>" + radio[0].actionName
							+ "</font>",
					id : 'approve-radio'+radio[0].actionId,
					name : 'rb-approve',
					inputValue : radio[0].actionId,
					url:  radio[0].url,
					eventIdentify: radio[0].changeBusiStateTo,
					listeners:{
						'check' : function(){
							 var _actionId = Ext.ComponentMgr.get('approve-radio'+radio[0].actionId).getGroupValue();   
							 var obj = Ext.getCmp('approve-radio'+_actionId );
							 actionId = _actionId;
							 eventIdentify=obj.eventIdentify
							
						}	 
					},
					checked : true
				}); 
				radioSet.items.add(_radio); 
				// 添加隐藏域，为了使radio正常显示
				radioSet.items.add(new Ext.form.Hidden());

				for (var i = 1; i < radio.length; i++) {
					// 添加隐藏域，为了使radio正常显示
					radioSet.items.add(new Ext.form.Hidden());
					var _radio = new Ext.form.Radio({
						boxLabel : "<font size='2.6px'>" + radio[i].actionName
								+ "</font>",
						id : 'approve-radio'+radio[i].actionId,
						name : 'rb-approve', 
						url:  radio[i].url,
						eventIdentify: radio[i].changeBusiStateTo,
						inputValue : radio[i].actionId
					});
					  
					radioSet.items.add(_radio);
					// 添加隐藏域，为了使radio正常显示
					radioSet.items.add(new Ext.form.Hidden());
				}
			}

		}
		radioSet.doLayout();
	}

    // *****************************备注******************
    // 备注label
    var backLabel = new Ext.form.Label({
        text : " 备注：",
        style : 'font-size:12px',
        height : 20
    });
    // 备注输入框
    var approveText  = new Ext.form.TextArea({
        width : '100%',
        autoScroll : true,
        name : 'power.approveText',
        isFormField : true,
        border : false,
        value : "请填写审批意见！",
        height : 90
    })
    // 第三行
    var remarkSet = new Ext.Panel({
        height : 120,
        border : false,
        style : "padding-top:10;border-width:1px 0 0 0;",
        items : [backLabel, approveText]
    });
  
    //-----------------------测量信息--------------------
    
        var masureLocation  = new Ext.form.TextField({
        name : 'measure.measureLocation',
        id:'masureLocation',
        fieldLabel : '测量地点',
        anchor : "95%",
        border : false
    });
    
         var masureTool  = new Ext.form.TextField({
        name : 'measure.useTool',
        id:'masureTool',
        fieldLabel : '使用仪器',
        anchor : "95%",
        border : false
    });
    
           var measureGas  = new Ext.form.TextField({
        name : 'measure.combustibleGas',
        id:'measureGas',
        fieldLabel : '可燃气体(粉尘浓度)',
        anchor : "95%",
        border : false
    });
    
    
    	var measurePanel = new Ext.Panel({
       // height : 40,
        border : false,
        style : "padding-top:10;border-width:1px 0 0 0;",
        layout : "column",
        labelWidth:120,
        labelAlign:"right",
        items : [{
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [masureLocation,measureGas]
				},{
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [masureTool]
				}]
    });
    
 //------------------------------------------------------  
//--------测量grid--------------------------------------
    //测量人编码
    var  measureMan=new Ext.form.TextField({
    id:'measureMan',
    name:'measureMan'
    });
    
           var measureBy = new Ext.form.ComboBox({
		fieldLabel : '测量人',
		name : 'measureBy',
		id : 'measureBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
	//	 hiddenName : 'hisModel.oldChargeBy',
		editable : false,
	//	anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '大唐灞桥热电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(emp) != "undefined") {
				Ext.getCmp('measureBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('measureBy'), emp.workerName);
						
	         grid.getSelectionModel().getSelected().set("measureMan",
											emp.workerCode);
			}
		}
	});
	
  var measureData = new Ext.form.TextField({
        name : 'measureData',
        id:'measureData',
        fieldLabel : '测量值',
        anchor : "95%",
        border : false
    });
    
    
    var measureDate = new Ext.form.TextField({
        id : 'measureDate',
        name : 'measureDate',
        style : 'cursor:pointer',
        anchor : "80%",
        value : getDate(),
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d 00:00:00',
                    dateFmt : 'yyyy-MM-dd HH:mm:ss',
                    alwaysUseStartDate : true
                });
            }
        }
    })
    
    
    var MyRecord = Ext.data.Record.create([
	{name : 'measureDataId'},
    {name : 'measureData'},
    {name : 'measureDate'},
    {name : 'measureMan'},
    {name:'measureManName'}
	]);

	var DataProxy = new Ext.data.HttpProxy(

			{
				url:'bqworkticket/findMeasureList.action'
			}

	);

	var TheReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"

	}, MyRecord);

	var  store = new Ext.data.Store({
		 
		proxy : DataProxy,

		reader : TheReader

	});
		store.on('beforeload', function() {
		Ext.apply(this.baseParams, {
         workticketNo : strBusiNo
		});
	});
	store.load({
			params : {
				start : 0,
				limit : 5		
			}
		});
		
var sm = new Ext.grid.CheckboxSelectionModel();

	var grid = new Ext.grid.EditorGridPanel({
		store : store,
		height:200,
		columns : [sm,
		new Ext.grid.RowNumberer({
		header : '序号',
		width : 40,
		align : 'center'
	}),{
			
			header : "ID",
			width : 75,
			sortable : true,
			dataIndex : 'measureDataId',
			hidden:true
		},{
			header : "测量值",
			width : 150,
			sortable : true,
			dataIndex : 'measureData',
			editor : measureData,
			align : 'left'
		},{
			header : "测量时间",
			width : 200,
			sortable : true,
			dataIndex : 'measureDate',	renderer : function(value) {
			if (!value)
				return '';
			if (value instanceof Date)
				return renderDate(value);
			var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
			var reTime = /\d{2}:\d{2}:\d{2}/gi;
			var strDate = value.match(reDate);
			var strTime = value.match(reTime);
			if (!strDate)
				return "";
			strTime = strTime ? strTime : '00:00:00';
			return strDate + " " + strTime;
		},
		editor : new Ext.form.TextField({
			readOnly : true,
			id : "temp",
			listeners : {
				focus : function() {
					WdatePicker({
						startDate : '%y-%M-%d 00:00:00',
						dateFmt : 'yyyy-MM-dd HH:mm:ss',
						alwaysUseStartDate : true
						,
						onpicked : function() {
							if (Ext.get("temp").dom.value < new Date()
									.dateFormat('Y-m-d 00:00:00')) {
								Ext.Msg.alert(Constants.ERROR, "完成时间不能小于当前时间！");
								return false;
							}
							grid.getSelectionModel().getSelected()
									.set("measureDate",
											Ext.get("temp").dom.value);
						}
					});
				}
			}
		}),
			align : 'left'
		},{
			header : "测量人",
			width : 150,
			sortable : true,
			dataIndex : 'measureManName',
			editor :measureBy,
			align : 'left'
		},
		{
			header : "测量人编码",
			width : 100,
			sortable : true,
			dataIndex : 'measureMan',
			editor :measureMan,
			align : 'left',
			hidden:true
		}
		],
		clicksToEdit : 1,
		sm : sm,
		tbar:['测量信息  ',
			{ text:'增加',
			iconCls:'add',
			handler:addRecord},
			{text:'保存',
			iconCls:'update',
			handler:saveRecord
			},{
				text:'删除',
				iconCls:'delete',
				handler:deleteRecord
			},{text:'取消',
			   iconCls:'cancer',
			   handler:function(){
			  store.reload();
		
			   }
			   }
		],
			//分页
		bbar : new Ext.PagingToolbar({
			pageSize : 5,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});
	
	function addRecord()
	{
	         var   m = new MyRecord({
	           	measureDataId:"",
				measureData:"",
				measureDate:getDate(),
				measureManName:workerName,
				measureMan:workerCode
				});

	var	mycount = grid.getStore().getCount();
				grid.stopEditing();
		store.insert(mycount, m);
		grid.startEditing(mycount + 1, 0);
		
	}
	
	function saveRecord()
	{
	   
		var record = grid.getStore().getModifiedRecords();
		var modifyRecords = new Array(); 
		for (var i = 0; i < record.length; i++) { 
				modifyRecords.push(record[i].data); 
			}
		// var str = "[";
		if (record.length > 0) {
//			for (var i = 0; i < record.length; i++) {
//				var data = record[i];
//				str = str + "{'measureDataId':'" + data.get("measureDataId")
//				        +"','measureData':'"+data.get("measureData")
//						+ "','measureDate':'" + data.get("measureDate")
//						+"','measureMan':'" + data.get("measureMan")
//						+"','workticketNo':'"+strBusiNo
//						+ "'},"
//			}
//			if (str.length > 1) {
//				str = str.substring(0, str.length - 1);
//			}
//			str = str + "]";
			Ext.Ajax.request({
				url : 'bqworkticket/saveMeasureInfo.action',
				params : {
					//data : str
					data : Ext.util.JSON.encode(modifyRecords),
					workticketNo:strBusiNo
				},
				method : 'post',
				waitMsg : '正在加载数据...',
				success : function(result, request) {
					var json = eval('(' + result.responseText + ')');
					Ext.Msg.alert("注意", json.msg);
					if(json.msg=="保存成功！")
					{
					
					store.reload();
					store.rejectChanges();
					}
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('错误', '操作失败!');
				}
			});
		} else {
			alert("没有对数据进行任何修改！");
		}
	}
	
	function deleteRecord()
	{
		
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {

			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.measureDataId) {
					ids.push(member.measureDataId); 
				} else {
					
					store.remove(store.getAt(i));
				}
			}
			if(ids=="")
			{
				Ext.Msg.alert("提示", "请选择要删除的记录！");
				return;
			}
			
			Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(
					buttonobj) {

				if (buttonobj == "yes") {

					Ext.lib.Ajax.request('POST',
							'bqworkticket/deleteMeasureInfo.action', {
								success : function(action) {
									Ext.Msg.alert("提示", "删除成功！")
								
						         	store.reload();
								},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
							}, 'ids=' + ids);
				}
			});
		}

	}
    
    
    
//---------测量grid end--------------------------------    

    var form = new Ext.FormPanel({
		columnWidth : 0.8,
		border : true,
		labelAlign : 'center',
		buttonAlign : 'right',
		buttons : [{
			text : "确定",
			handler : function() {
				// 显示用户验证窗口
//         if(workticketType!="4")
//         {
         	//动火票无危险点，不需判断
				if (!ifSeeDanger) {
					Ext.Msg.alert("提示", "请查看危险点！");
					return;
				}
				 if(!isSeePrint)
				{
				 Ext.Msg.alert("提示", "请查看票面！");
					return; 	
				}
       //  }
				workerPanel.getForm().reset();
				validateWin.show();

			}
		}, {
			text : "取消",
			handler : function() {
				// layout.hidden = true;
				window.close();
			}
		}],
		items : [noticeField,titleField, measurePanel,grid,tableField, showAppointNextRoles, approveSet,
				remarkSet]
	});
  


    // ↓↓****************员工验证窗口****************

    // 工号
    var workCode = new Ext.form.TextField({
        id : "workCode",
        fieldLabel : '工号<font color ="red">*</font>',
        allowBlank : false,
        width : 120
    });

    // 密码
    var workPwd = new Ext.form.TextField({
        id : "workPwd",
        fieldLabel : '密码<font color ="red">*</font>',
        allowBlank : false,
        inputType : "password",
        width : 120
    });
    // 弹出窗口panel
    var workerPanel = new Ext.FormPanel({
        frame : true,
        labelAlign : 'right',
        height : 120,
        items : [workCode, workPwd]
    });

    // 弹出窗口
    var validateWin = new Ext.Window({
        width : 300,
        height : 140,
        title : "请输入工号和密码",
        buttonAlign : "center",
        resizable : false,
        modal:true,
        items : [workerPanel],
        buttons : [{
            text : '确定',
            id:'btnSign',
            handler : function() {
           
            	
                // 工号确认
                Ext.lib.Ajax.request('POST', 'comm/workticketApproveCheckUser.action',
                    {success : function(action) {
                        var result = eval(action.responseText);
                        // 如果验证成功，进行签字操作
                        if (result) {
             //----------审批------------------------
            form.getForm().submit({
			url : 'bqworkticket/fireMeasureApprove.action',
			waitMsg : '正在保存数据...',
			method : 'post',
			params : {
				workticketNo : strBusiNo,
				workerCode : workCode.getValue(),
				//workflowNo:entryId,
				nextRoles : nrs,
				actionId:actionId,
				eventIdentify:eventIdentify,
				responseDate:datePicker.getValue(),
				approveText:approveText.getValue()
			},
			success : function(form,action) { 
				var o = eval("(" + action.response.responseText + ")"); 
				Ext.Msg.alert("提示", o.msg, function() {
					if(o.msg.indexOf('成功')!=-1)
					{
						window.returnValue=true;
					window.close();
					
					}
					validateWin.hide();
				});

			},
			faliue : function() {
				Ext.Msg.alert("提示","未知错误");
			}

		}); 
                        //------------	
    
                        } else {
                            Ext.Msg.alert("提示","密码错误");
                        }
                    }
                }, "workerCode=" + workCode.getValue() + "&loginPwd=" + workPwd.getValue());
            }
        }, {
            // 取消按钮
            text :"取消",
            iconCls :"cancer",
            handler : function() {
                validateWin.hide();
            }
        }],
        listeners : {
            show : function(com) {
                // 取得默认工号
               workCode.setValue(workerCode);
            }
        },
        closeAction : 'hide'
    });
    
    function getWorkCode()
    {
     Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
                    success : function(action) {
                    	 var result = eval("(" + action.responseText + ")");
                        if (result.workerCode) {
                            // 设定默认工号
                        	workerCode=result.workerCode;
                        	workerName=result.workerName;
                          
                        }
                    }
                });
    }
    getWorkCode();
    
　　 // ↑↑****************员工验证窗口****************

    	function getFirstSteps() {
    	
		Ext.Ajax.request({
			url : "MAINTWorkflow.do?action=getFirstStep",
			method : 'post',
			params : {
				flowCode : flowCode
				//jsonArgs : "{'isDBP':false,'is12Failure':true,'isYSZC':true}"
			},
			success : function(result, request) {
				
				var radio = eval('(' + result.responseText + ')'); 
			
				radioAddHandler(radio.actions);
			},
			failure : function() {
				 Ext.Msg.alert("提示","错误");
			}
		});
	};
	
 function getCurrentSteps() {
		Ext.Ajax.request({
			url : "MAINTWorkflow.do?action=getCurrentSteps",
			method : 'post',
			params : {
				entryId : entryId,
				workerCode : workerCode
//				jsonArgs : "{'isDBP':false,'is12Failure':true,'isYSZC':true}"
			},
			success : function(result, request) {
				var radio = eval('(' + result.responseText + ')'); 
				radioAddHandler(radio[0].actions);
			},
			failure : function() {
				
			}
		});
	};
	
   
    
    //签字时的Enter
    document.onkeydown=function() {  
          if (event.keyCode==13) {  
         		 document.getElementById('btnSign').click();
   			}
    }
    if(entryId==null||entryId=="")
    {
    	Ext.get("approve_his").dom.style.display = 'none';
    	getFirstSteps();
    
    }
    else
    {
        getCurrentSteps();
    }
    
    //-------------------------布局-------------------
       var signPanel = new Ext.Panel({
       	id:"tab1",
        layout : "column",
		  autoScroll : true,
        autoHeight : false,
        title : '签字审批', 
        height : 600,
        border : true,
        items : [{
                    columnWidth : 0.1,
                    border : false
                }, form, {
                    columnWidth : 0.1,
                    border : false
                }],
        defaults : {
            labelAlign : 'center'    
        }
    });

    // 票面浏览panel
   var tab4Href=getReportUrl(workticketType,strBusiNo,fireLevelId);   //"/powerrpt/report/webfile/ElectricOne.jsp?no="+strBusiNo;
   
     var tab4Html = "<iframe name='tabSafety' src='" + tab4Href + "'style='width:100%;height:100%;border:0px;'></iframe>";
    var ticketPanel = new Ext.Panel({
                title : "票面浏览",
                id:'tab4',
                html : tab4Html
            });
   //----------安措信息控制-------------------------------------   
     var showSafetyTyep="";
     var ifdosafety="";
     if(workticketType!="4")
     {
			     if(busiStatus=="2"||busiStatus=="3")
			     {
			     	//已上报和已签发
			     	showSafetyTyep="N";
			     }
			     else if(busiStatus=="17"||busiStatus=="18"||busiStatus=="4")
			     {
			     	//4已接票17点检已审核签发18值长已审核
			     	showSafetyTyep="Y";
			     }
			     else if(busiStatus=="5")
			     {
			     	//5已批准 即安措办理
			     	showSafetyTyep="Y";
			     	ifdosafety=true;
			     }
			     else
			     {
			     	showSafetyTyep="";
			     }
     
     }
     else
     {
       //动火票	
     	 if(busiStatus=="2") showSafetyTyep="repair"; 	//签发时可修改检修安措
     	else if(busiStatus=="3") showSafetyTyep="run"; //许可人填写运行时安措
     	 else if(busiStatus=="23") {showSafetyTyep="repair"; ifdosafety=true;} //工作负责人办理检修安措
     	else if(busiStatus=="24") {showSafetyTyep="run"; ifdosafety=true;} //工作许可人办理运行安措
     	else { showSafetyTyep="";}
     }
 //---------------------------------------------------------------------------------------------------------------- 
     var tab2Href="run/bqworkticket/business/approve/safety/workticketSafety.jsp?workticketNo="+strBusiNo+"&workticketType="+workticketType+"&showSafetyTyep="+showSafetyTyep+"&ifdosafety="+ifdosafety;
     var tab2Html = "<iframe name='tabSafety' src='" + tab2Href + "'style='width:100%;height:100%;border:0px;'></iframe>";
    
    //危险点
     var tab3Href="run/bqworkticket/business/approve/danger/danger.jsp?workticketNo="+strBusiNo;
       var tab3Html = "<iframe name='tabDanger' src='" + tab3Href + "'style='width:100%;height:100%;border:0px;'></iframe>";
       
    // tabpanel
    var tabPanel;
    //-------------tabPanel------------------------------------
//    if(workticketType!="4")
//    {
    	//非动火票有危险点
		    tabPanel= new Ext.TabPanel({
		        activeTab : 0,
		        layoutOnTabChange : true,
		        border : false,
		        items : [signPanel, {
		            title : '安措信息', 
		            id:'tab2',
		            html : tab2Html
		        },{
		            title : '危险点信息', 
		            id:'tab3',
		            html : tab3Html
		        }, ticketPanel]
		    })
		  
		
   
//    }
//    else
//    {
//    	//动火票无危险点
//    	   tabPanel= new Ext.TabPanel({
//		        activeTab : 0,
//		        layoutOnTabChange : true,
//		        border : false,
//		        items : [signPanel, {
//		            title : '安措信息', 
//		            id:'tab2',
//		            html : tab2Html
//		        },ticketPanel]
//		    })
//    }
   
       tabPanel.on("tabchange",function(){
		   	if(tabPanel.getActiveTab().getId()=="tab3")
		   	{
		   		ifSeeDanger=true;
		   	}
		   	 if(tabPanel.getActiveTab().getId()=="tab4")
		   	{
		    	isSeePrint=true;	
		   	}
		   });
   //-----------------------------------------------


    new Ext.Viewport({
                enableTabScroll : true,
                layout : "fit",
                border : false,
                items : [{
                            layout : 'fit',
                            border : false,
                            margins : '0 0 0 0',
                            region : 'center',
                            autoScroll : true,
                            items : [tabPanel]
                        }]
            });
            
  
    
});
