Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
// 获取时间
function getSetDate() {
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
 

Ext.form.Label.prototype.setText = function(argText) {
	this.el.dom.innerHTML = argText;
}
var nrs = "";
function removeAppointNextRoles() {
	if (confirm("确定要清除指定下一步人吗？")) {
		nrs = "";
		Ext.get("showAppointNextRoles").dom.innerHTML = "";
	}
}
var flowCode;
var entryId = getParameter("entryId");
var failureId = getParameter("failureId");
var failureType = getParameter("failureType");
function gettype() {
	flowCode = "bqFailure";
}
var eventIdentify="";

Ext.onReady(function() {
	// var arg = window.dialogArguments;
	// var id = getParameter("id");
	gettype();
	// 设置响应时间
	var timeSet = new Ext.form.Checkbox({
		name : 'reponseTime',
		boxLabel : "设置响应时间",
		listeners : {
			check : function(box, checked) {
				if (checked) {
					// 如果checkbox选中,显示时间选择textfield
					Ext.getCmp('datePicker').setVisible(true);
					Ext.getCmp('approveField').doLayout();
				} else {
					Ext.getCmp('datePicker').setVisible(false);
					Ext.getCmp('approveField').doLayout();
				}
			}
		}
	});

	// 时间选择
	var timeDisplay = new Ext.form.TextField({
		id : 'datePicker',
		hidden : true,
		style : 'cursor:pointer',
		value : getSetDate(),
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

	// 图形展示
	var btnPictureDisplay = new Ext.Button({
		text : "图形展示",
		handler : function() {
			if (entryId == "" || entryId == null) {
				url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
						+ flowCode;
			} else {
				url = "/power/workflow/manager/show/show.jsp?entryId="
						+ entryId;
			}
			window.open(url);
		}
	});

	// 查看审批记录
	var btnRecord = new Ext.Button({
		text : "查看审批记录",
		handler : function() {
			if (entryId == "" || entryId == null) {
				Ext.Msg.alert("提示", "流程还未启动!");
			} else {
				var url = "/power/workflow/manager/approveInfo/approveInfo.jsp?entryId="
						+ entryId;
				window
						.showModalDialog(
								url,
								null,
								"dialogWidth:650px;dialogHeight:400px;;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;");

			}
		}
	});
	// 显示下步角色
	var btnNextDisplay = new Ext.Button({
		text : "显示下步角色",
		handler : getNextSteps
	});

	// 标题label
	var lblApprove = new Ext.form.Label({
		width : '100%',
		id : 'lblApprove',
		text : '缺陷上报',
		height : 5,
		style : "font-size:30px;color:blue;line-height:100px;padding-left:250px"
			// style :
			// "color:blue;line-height:100px;padding-left:175px;font-size:30px"
	});

	// 显示指定下一步角色
	var showAppointNextRoles = new Ext.form.Label({
		border : false,
		id : 'showAppointNextRoles',
		style : "color:red;font-size:12px"
	});

	// 审批方式label
	var lblApproveStyle = new Ext.form.Label({
		text : " 审批方式：",
		style : 'font-size:12px',
		border : false
	});

	// 单选按钮组
	var radioSet = new Ext.Panel({
		height : 30,
		border : false,
		layout : "column"
	});

	var stylePanel = new Ext.Panel({
		height : 65,
		border : false,
		style : "padding-top:20;padding-left:2px;border-width:1px 0 0 0;",
		items : [lblApproveStyle, radioSet]
	});

	// 获取审批方式
	function getCurrentSteps() {
		var is12failure;
		if(failureType=="1" || failureType=="2")
		{
			is12failure=true;
		}
		else
		{
			is12failure=false;
		}
		Ext.Ajax.request({
			url : "MAINTWorkflow.do?action=getFirstStep",
			method : 'post',
			params : {
				flowCode : flowCode
				//jsonArgs : "{'is12Failure':true,'isYSZC':true}"
			},
			success : function(result, request) {
				var radio = eval('(' + result.responseText + ')');
				var conn = Ext.lib.Ajax.getConnectionObject().conn;
				conn.open("POST", "comm/checkIsDianJianYuan.action", false);
				conn.send(null); 
				// 成功状态码为200
				if (conn.status == "200") {   
					var isDJY = conn.responseText ;  
					var actions = radio.actions;
					for(var i=0; i<actions.length; i++)
					{
						if((isDJY=="Y" || is12failure==false)&& actions[i].changeBusiStateTo=="SB(DJQR)")
						{ 
						   actions.remove(actions[i]);
						}
						else if(isDJY=="N" && is12failure==true && actions[i].changeBusiStateTo=="SB(JXXQ)")
						{  
							actions.remove(actions[i]);
						}
					}  
					radioAddHandler(actions);
				} 
			},
			failure : function() {

			}
		});
	};

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
		 url = "power/workflow/manager/appointNextRole/appointNextRole.jsp";
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

	// 备注
	var remark = new Ext.form.TextArea({
		id : "remark",
		width : '98%',
		autoScroll : true,
		border : false,
		height : 110,
		value : ''
	})
	// 备注label
	var lblRemarks = new Ext.form.Label({
		text : " 备注：",
		border : false,
		style : 'font-size:12px'
	});
	var remarkSet = new Ext.Panel({
		autoHeight : true,
		border : false,
		style : "padding-top:10;padding-left:2px;padding-bottom:2px;border-width:1px 0 0 0;",
		items : [lblRemarks, remark]
	});

	var approveField = new Ext.Panel({
		id : "approveField",
		autoWidth : true,
		height : 70,
		style : "padding-top:20;padding-bottom:20;border-width:1px 0 0 0;",
		border : false,
		layout : "column",
		anchor : '100%',
		items : [
				// 设置响应时间checkbox
				{
					columnWidth : 0.185,
					id : "tiemcheck",
					layout : "form",
					hideLabels : true,
					border : false,
					items : [timeSet]
				},
				// 时间选择
				{
					columnWidth : 0.265,
					hideLabels : true,
					layout : "form",
					border : false,
					items : [timeDisplay]
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
					items : [btnPictureDisplay]
				},
				// 查看审批记录按钮
				{
					columnWidth : 0.19,
					layout : "form",
					hideLabels : true,
					border : false,
					items : [btnRecord]
				},
				// 下一步按钮
				{
					columnWidth : 0.19,
					layout : "form",
					hideLabels : true,
					border : false,
					items : [btnNextDisplay]
				}]

	});

	// 审批签字
	var approvePanel = new Ext.form.FormPanel({
		border : false,
		labelAlign : 'right',
		layout : "column",
		items : [{
			columnWidth : 0.1,
			border : false
		}, {
			columnWidth : 0.8,
			border : true,
			layout : 'form',
			buttonAlign : 'center',
			items : [lblApprove, approveField, showAppointNextRoles,
					stylePanel, remarkSet],
			buttons : [{
				text : '确定',
				handler : function() {
					if (confirm('确定要上报吗?')) {
							Ext.Ajax.request({
								url : 'bqfailure/reportFailure.action',
								waitMsg : '正在保存数据...',
								method : 'post',
								params : {
									flowCode : flowCode,
									actionId : actionId,
									approveText : remark.getValue(),
									nextRoles : nrs,
									"failure.id" : failureId,
									eventIdentify : eventIdentify
								},
								success : function(result, request) {
									var responseArray = Ext.util.JSON
											.decode(result.responseText);
									if (responseArray.success == true) {
										Ext.MessageBox.alert('提示', '上报成功',
												function() {
													window.returnValue = true;
													window.close();
												});
									} else {
										var o = eval('(' + result.responseText
												+ ')');
										Ext.MessageBox.alert('错误', '上报失败',
												function() {
													window.returnValue = false;
													window.close();
												});
									}
								},
								failure : function() {
									var o = eval('(' + result.responseText
											+ ')');
									Ext.MessageBox.alert('错误', '上报失败',
											function() {
												window.returnValue = false;
												window.close();
											});
								}
							});
					}
				}
			}, {
				text : '取消',
				handler : function() {
					window.close();
				}
			}]
		}, {
			columnWidth : 0.1,
			border : false
		}]
	});
	var viewport = new Ext.Viewport({
		layout : "fit",
		border : false,
		items : [approvePanel]
	});
	getCurrentSteps();
});