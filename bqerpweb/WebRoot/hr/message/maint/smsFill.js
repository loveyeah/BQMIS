Ext.ns("message.smsFill");
message.smsFill = function() {
	var messageId = null;
	var msgCodes=null;
	var msgNames=null;

	var userCode = new Ext.form.TextField({
				name : 'message.userCode',
				xtype : 'textfield',
				fieldLabel : '用户名',
				readOnly : false,
				anchor : '85%',
				hidden:true,
				hideLabel:true,
				value:'admin',
				blankText : '不可为空！'
			});
	
//	var userCode = new Ext.form.TextField({
//				name : 'message.userCode',
//				xtype : 'textfield',
//				fieldLabel : '用户名',
//				readOnly : false,
//				anchor : '85%',
//				blankText : '不可为空！'
//			});

	var userPsw = new Ext.form.TextField({
				name : 'message.userPsw',
				xtype : 'textfield',
				fieldLabel : '密码',
				readOnly : false,
				hidden:true,
				hideLabel:true,
				value:'admin',
				anchor : '85%',
				blankText : '不可为空！'
			});
   var msgPerson = new Ext.form.Hidden({
					id : 'msgPerson',
					name : 'message.msgPerson'
				});
			
	var msgPersonName = new Ext.form.TextField({
				name : 'msgPersonName',
				xtype : 'textfield',
				fieldLabel : '通知人员',
				readOnly : true,
				anchor : '85%',
				blankText : '不可为空！'
			});
			
		var msgContent = new Ext.form.TextArea({
				name : 'message.msgContent',
				xtype : 'textfield',
				fieldLabel : '短信内容',
				readOnly : false,
				anchor : '85%',
				height:250,
				blankText : '不可为空！'
			});		
			
   function saveMessage() {
		var url = "";
		 if(userCode.getValue()=='admin' && userPsw.getValue()=='admin' ){
		   if(msgPerson.getValue()=="" ||msgPerson.getValue()==null){
		      Ext.Msg.alert('注意', '请添加通知人员！');
		   	  return;
		   }
		   var codes=msgPerson.getValue()
		   var arr = new Array()
		   arr = codes.split(",");
		   for(i =0;i<arr.length-1;i++){
		        for(j =i+1;j<arr.length;j++){
		              if(arr[i]==arr[j]){
		                Ext.Msg.alert('注意', '添加的通知人员重复！');
		   	            return;
		              }
		        }
		   }
		     
		  
		   if (messageId == "" || messageId == null) {
			url = "com/saveMessage.action"
		} else {
			url = "com/updateMessage.action"
		}
 	
		messageForm.getForm().submit({
					method : 'POST',
					url : url,
					params : {
							messageId : messageId
						},
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("注意", o.msg);
						Ext.getCmp("div_tabs").setActiveTab(1);
			            Ext.getCmp("div_grid").getStore().reload();
					},
					faliue : function() {
						Ext.Msg.alert('错误', '出现未知错误.');
					}
				});
		 }
		else{
			Ext.Msg.alert('错误', '用户名密码输入错误，重新输入！');
		}
	};

function addMessagePerson(){
	if (msgPerson.getValue() != null && msgPerson.getValue() != ""
				&& msgPerson.getValue() != "null") {
			msgCodes = msgPerson.getValue() + ",";
		}
	else
	   msgCodes="";
	   
	if (msgPersonName.getValue() != null && msgPersonName.getValue() != ""
				&& msgPersonName.getValue() != "null") {
			msgNames = msgPersonName.getValue() + ",";
		}
   else
      msgNames="";
	 	var url = "/power/comm/jsp/hr/workerByDept/workerByDept2.jsp";
				var args = {
					selectModel : 'multiple',
					notIn : "'999999'",
				rootNode : {
					id : '0',
					text : '灞桥电厂'
				}
				}
				var obj = window
					.showModalDialog(
							'../../../comm/jsp/hr/workerByDept/workerByDept2.jsp',
							args,
							'dialogWidth=600px;dialogHeight=420px;center=yes;help=no;resizable=no;status=no;');
			if (typeof(obj) == 'object') {
				Ext.get("message.msgPerson").dom.value=msgCodes;
				Ext.get("msgPersonName").dom.value =msgNames;
				Ext.get("message.msgPerson").dom.value += obj.codes;
				Ext.get("msgPersonName").dom.value += obj.names;
			}
	}
	
	var baseInfoField = new Ext.form.FieldSet({
		autoHeight : true,
	    title : '短信通知信息',
		height : '100%',
		layout : 'form',
		style : {
			"padding-top" : '30',
			"padding-left" : '30',
			"padding-right" : '30'
		},
		bodyStyle : Ext.isIE ? 'padding:0 0 5px 15px;' : 'padding:5px 15px;',

		anchor : '-20',
		buttonAlign : 'center',
		items : [{
			border : false,
			layout : 'form',
			items : [{
						border : false,
						layout : 'column',
						items : [{
									columnWidth : .46,
									layout : 'form',
									border : false,
									labelWidth : 70,
									items : [userCode]
								}, {
									columnWidth : .46,
									layout : 'form',
									labelWidth : 70,
									border : false,
									items : [userPsw]
								}]
					}, {
						border : false,
						layout : 'column',
						items : [{
									columnWidth : .7,
									layout : 'form',
									labelWidth : 70,
									border : false,
									items : [msgPerson, msgPersonName]
								}, {
									columnWidth : .08,
									layout : 'form',
									border : false,
									items : [new Ext.Button({
												id : 'addBtu',
												text : '添加',
												iconCls : '',
												handler : addMessagePerson
											})]
								}, {
									columnWidth : .08,
									layout : 'form',
									border : false,
									items : [new Ext.Button({
										id : 'cancerBtu',
										text : '清空',
										iconCls : '',
										handler : function() {
											Ext.get("message.msgPerson").dom.value = "";
											Ext.get("msgPersonName").dom.value = "";
										}
									})]
								}]
					}, {
						border : false,
						layout : 'column',
						items : [{
									columnWidth : 1,
									layout : 'form',
									labelWidth : 70,
									border : false,
									items : [msgContent]
								}]
					}]

		}],
		buttons : [{
					text : '保存',
					iconCls : 'save',
					handler : saveMessage
				}, {
					text : '取消',
					iconCls : 'cancer',
					handler : function() {
						messageForm.getForm().reset();
					}
				}]

	});

	var messageForm = new Ext.FormPanel({
				layout:'fit',
				border : false,
				frame : true,	
				fileUpload : true,
				items : [baseInfoField],
				bodyStyle : {
					'padding-top' : '5px'
				},
				defaults : {
					labelAlign : 'center'
				}
			});

	return {
		form:messageForm,
		setMessageId : function(_messageId) {
			messageId = _messageId;
			},
		setDetail:function(member){
			  userCode.setValue(member.get("userCode"));
			  userPsw.setValue(member.get("userPsw"));
			  msgPerson.setValue(member.get("msgPerson"));
			  msgPersonName.setValue(member.get("msgName"));
			  msgContent.setValue(member.get("msgContent"));
		},
		clearForm:function()
		{
			messageForm.getForm().reset();
			messageId = null;
//			store.removeAll();
		}
	}		
};