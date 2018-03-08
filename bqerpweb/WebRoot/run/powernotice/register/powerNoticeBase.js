Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
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
Ext.onReady(function() {
	var id = getParameter("id");
	var no=getParameter("no");
	
	 var noticeId = {
		id : "noticeId",
		xtype : "hidden",
		fieldLabel : 'ID',
		value:'',
		readOnly:true,
		name : 'power.noticeId',
		 anchor : "40%"
	}
 var noticeNo = {
		id : "noticeNo",
		xtype : "textfield",
		fieldLabel : '通知单号',
		value:'自动生成',
		readOnly:true,
		name : 'power.noticeNo',
		 anchor : "40%"
	}
//		 var contactDept = {
//		id : "contactDept",
//		xtype : "textfield",
//		fieldLabel : '联系班组',
//		allowBlank:false,
//		name : 'power.contactDept',
//		 anchor : "40%"
//
//	}
	
	 
		var contactDept = {
		fieldLabel : '申请班组',
		name : 'contactDept',
		xtype : 'textfield',
		id : 'contactDept',
		readOnly:true,
		allowBlank : false,
		anchor : "40%"
	};
	var contactDeptHid = new Ext.form.Hidden({
	    	id : contactDeptHid,
	    	name : 'power.contactDept'
	    });
	
		var contactMonitor = {
		fieldLabel : '申请人',
		name : 'contactMonitor',
		xtype : 'textfield',
		id : 'contactMonitor',
		allowBlank : false,
		readOnly:true,
		editable : false,
		anchor : "40%"
	    };
	
	    var contactMonitorHid = new Ext.form.Hidden({
	    	id : contactMonitorHid,
	    	name : 'power.contactMonitor'
	    });
	   
	     var contactDate = new Ext.form.TextField({
        id : 'contactDate',
        fieldLabel : "申请时间",
        name : 'power.contactDate',
        style : 'cursor:pointer',
        anchor : "40%",
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
	   
      	// 定义状态
	var stateData = new Ext.data.SimpleStore({
				data : [[1, '300MW机组设备'], [4,'125MW机组设备']],
				fields : ['value', 'name']
			});
	// 定义状态
	var stateComboBox = new Ext.form.ComboBox({
				id : "stateCob",
				fieldLabel:'类别',
				name:'stateComboBox',
				store : stateData,
				displayField : "name",
				valueField : "value",
				mode : 'local',
				triggerAction : 'all',
				hiddenName:'power.noticeSort',
				readOnly : true,
				value:'',
				width : 120
			}); 
    
    
	    var contactContent = {
		id : "contactContent",
		height:100,
		xtype : "textarea",
		fieldLabel : '联系内容',
		name : 'power.contactContent',
		 anchor : "90%"
	   }
	   var memo = {
		id : "memo",
		height:100,
		xtype : "textarea",
		fieldLabel : '备注',
		name : 'power.memo',
		 anchor : "90%"
	   }
	
	  var baseInfoField = new Ext.form.FieldSet({
		autoHeight : true,
		labelWidth:60,
		style : {
			"padding-top" : '40',
			"padding-left" : '20'
		},
		bodyStyle : Ext.isIE ? 'padding:0 0 5px 15px;' : 'padding:5px 15px;',
		
		anchor : '-20',
		border : false,
		buttonAlign : 'center',
		 items : [noticeId,noticeNo,contactMonitor,contactMonitorHid,contactDeptHid,contactDept,contactDate,stateComboBox,contactContent,memo],
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : savePower
		}, {
			text : '清空',
			iconCls : 'cancer',
			handler : function() {
   	var monitorName =  Ext.getCmp('contactMonitor').getValue();
	var deptName = Ext.getCmp('contactDept').getValue();
	var monitorCode =contactMonitorHid.getValue();
	var deptCode =contactDeptHid.getValue();
    form.getForm().reset();
    Ext.getCmp('contactMonitor').setValue(monitorName);
    Ext.getCmp('contactDept').setValue(deptName);
    contactMonitorHid.setValue(monitorCode);
    contactDeptHid.setValue(deptCode);
				
			}
		}]
	
	});

		var form = new Ext.FormPanel({
		border : false,
		frame : true,
		fileUpload : true,
		items : [baseInfoField],
		bodyStyle : {
			'padding-top' : '5px'
		},
		defaults : {
			labelAlign : 'right'
		}
	});
    function savePower()
    {
    	var msg="";
    	if(Ext.get("contactMonitor").dom.value=="")
    	{
    		msg="联系申请人";
    	}
    	if(Ext.get("contactContent").dom.value=="")
    	{
    		if(msg!="")
    		{
    			msg=msg+",联系内容";
    		}
    		else
    		{
    		   msg="联系内容";
    		}
    	}
    	if(msg!="")
    	{
    		Ext.Msg.alert('提示', "请输入'"+msg+"'!");
    		return;
    	}
    	
    	
    	var url="";
    	if(Ext.get("noticeNo").dom.value=="自动生成")
    	{
    		url="powernotice/addPowerNotice.action"
    	}
    	else
    	{
    	 url="powernotice/updatePowerNotice.action"
    	}
    	form.getForm().submit({
						method : 'POST',
						url : url,
						success : function(form,action) {
							var o = eval("(" + action.response.responseText + ")");
							Ext.Msg.alert("注意", o.msg);
							//----------
							if(o.msg=="增加成功！")
							{
								Ext.get("noticeId").dom.value=o.id;
								Ext.get("noticeNo").dom.value=o.no;
							}
							
						if(parent.document.all.iframe2!=null)
						{
		           parent.document.all.iframe2.src = "run/powernotice/register/powerNoticeList.jsp";
						}
						},
						faliue : function() {
							Ext.Msg.alert('错误', '出现未知错误.');
						}
					});	
    }
    
     var baseInfoViewport = new Ext.Viewport({
        layout : "fit",
        border : false,
        items : [form],
        defaults : {
            autoScroll : true
        }
    }).show();
    if(id!=null&&id!="")
    {
    Ext.get("noticeId").dom.value=id;
    Ext.get("noticeNo").dom.value=no;
    Ext.getCmp('contactMonitor').setValue(getParameter("monitorCode"));
	Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('contactMonitor'),getParameter("monitorName"));
						
	Ext.getCmp('contactDept').setValue(getParameter("contactDeptCode"));
	Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('contactDept'),getParameter("contactDeptName"));
	Ext.get("contactDate").dom.value=getParameter("contactDate");
	Ext.get("contactContent").dom.value=getParameter("contactContent");
	Ext.get("memo").dom.value=getParameter("memo");
	var s = getParameter("noticeSort");
	if(s=='null'||s==null||s==''){
		s='';
	}

	
	Ext.getCmp('stateCob').setValue(s);
//	Ext.form.ComboBox.superclass.setValue.call(Ext
//						.getCmp('stateCob'),getParameter("contactDeptName"));
						
    }
    else 
    {
    	 form.getForm().reset();
    	 Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
                    success : function(action) {
                    	 var result = eval("(" + action.responseText + ")");
                        if (result.workerCode) {
                            // 设定默认工号
                        	workerCode=result.workerCode;
                        	 Ext.getCmp('contactMonitor').setValue(result.workerName);
                        	 Ext.getCmp('contactDept').setValue(result.deptName);
                        	 contactMonitorHid.setValue(result.workerCode);
                        	 contactDeptHid.setValue(result.deptCode);
                      	
                        }
                    }
                });
    }
    
})
	