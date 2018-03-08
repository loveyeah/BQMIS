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
Ext.form.Radio.prototype.url = ""; 
var nrs = "";
function removeAppointNextRoles(){
	if(confirm("确定要清除指定下一步人吗？")){
		nrs = "";
		Ext.get("showAppointNextRoles").dom.innerHTML = "";
	}
}
Ext.onReady(function() {  
	// var arg = window.dialogArguments;
	// var entryId = arg.entryId;
	var entryId = getParameter("entryId");
	var workerCode = '999999';
	var actionId;
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
			var url = "../../manager/show/show.jsp?entryId=" + entryId;
			window.open(url);
		}
	});

	// 查看审批记录
	var btnRecord = new Ext.Button({
		text : "查看审批记录",
		handler : function() {
			var url = "../../manager/approveInfo/approveInfo.jsp?entryId="
					+ entryId;
			window
					.showModalDialog(
							url,
							null,
							"dialogWidth:650px;dialogHeight:400px;;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;");
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
		text : '相关领导审批',
		height : 10,
		style : "color:blue;line-height:100px;padding-left:175px;font-size:30px"
	});

	// 显示指定下一步角色
	var showAppointNextRoles = new Ext.form.Label({
		border : false,
		id:'showAppointNextRoles',
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
		style : "padding-top:20;border-width:1px 0 0 0;",
		items : [lblApproveStyle, radioSet]
	});

	// 获取审批方式
	function getCurrentSteps() {
		Ext.Ajax.request({
			url : "MAINTWorkflow.do?action=getCurrentSteps",
			method : 'post',
			params : {
				entryId : entryId,
				workerCode : workerCode,
				jsonArgs : "{'isDBP':false,'is12Failure':true,'isYSZC':true}"
			},
			success : function(result, request) {
				var radio = eval('(' + result.responseText + ')'); 
				radioAddHandler(radio[0].actions);
			},
			failure : function() {
				
			}
		});
	};

	function getNextSteps() { 
		//var actionId = Ext.ComponentMgr.get('approve-radio').getGroupValue();
		var args = new Object();
		args.actionId = actionId;
		args.entryId = entryId;
		var url = "power/workflow/manager/appointNextRole/appointNextRole.jsp";
		var ro = window
				.showModalDialog(
						url,
						args,
						"dialogWidth:400px;dialogHeight:350px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;");
		if (typeof(ro) != "undefined") {
			if (ro) {
				nrs = ro.nrs;
				showAppointNextRoles.setText("<span style=\"cursor:hand;color:blue\"  onclick=\"removeAppointNextRoles();\">[取消指定]</span>  您指定下一步角色为：[ " + ro.nrsname + " ]");
			}
		} 
	};

	/**
	 * 审批方式radio button生成
	 */

	function radioAddHandler(radio) {
		 
		// radio = radio ||
		// [{actionId:1,actionName:'上报'},{actionId:2,actionName:'紧急上报'}];
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
							 alert(obj.eventIdentify);
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
		width : '99%',
		autoScroll : true,
		border : false,
		height : 110,
		value : ''
	})
	// 备注label
	var lblRemarks = new Ext.form.Label({
		text : " 备注：",
		style : 'font-size:12px'
	});
	var remarkSet = new Ext.Panel({
		autoHeight : true,
		border : false,
		style : "padding-top:10;border-width:1px 0 0 0;",
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
	// ↓↓****************员工验证窗口****************
	// 工号
	var workCode = new Ext.form.TextField({
		id : "workCode",
		value : '999999',
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
		modal : true,
		title : "请输入工号和密码",
		buttonAlign : "center",
		resizable : false,
		items : [workerPanel],
		buttons : [{
			text : '确定',
			handler : function() {  
				//var actionId = Ext.ComponentMgr.get('approve-radio').getGroupValue();
				alert(nrs);
				return false;
				Ext.Ajax.request({
					url : "MAINTWorkflow.do?action=doAction",
					method : 'post',
					params : {
						entryId : entryId,
						actionId : actionId,
						opinion : Ext.get("remark").dom.value,
						workerCode : workCode.getValue(),
						jsonArgs : "{'isDBP':false,'is12Failure':true,'isYSZC':true}",
						nrs : nrs,
						nps : ''
					},
					success : function(result, request) {
						getCurrentSteps();
						validateWin.hide();
					},
					failure : function(result, request) {
						Ext.Msg.alert("错误", result.responseText);
					}
				});
			}
		}, {
			text : '取消',
			handler : function() {
				validateWin.hide();
			}
		}],
		closeAction : 'hide'
	});
	// ↑↑****************员工验证窗口****************

	// 审批签字
	var approvePanel = new Ext.form.FormPanel({
		title : '审批签字',
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
			items : [lblApprove, approveField, showAppointNextRoles,
					stylePanel, remarkSet],
			buttons : [{
				text : '确定',
				handler : function() {
					validateWin.show();
				}
			}, {
				text : '取消',
				handler : function() {
					viewport.remove(tabpanel);
					window.close();
				}
			}]
		}, {
			columnWidth : 0.1,
			border : false

		}]
	});
	var tab2Html = "<iframe name='tabDetail' src='" + "http://www.baidu.com"
			+ "'style='width:100%;height:100%;border:0px;'></iframe>";

	// 详细信息
	var detailPanel = new Ext.Panel({
		title : '详细信息',
		border : false,
		html : tab2Html
	});

	// tabPanel定义
	var tabpanel = new Ext.TabPanel({
		activeTab : 0,
		layoutOnTabChange : true,
		items : [approvePanel, detailPanel]
	});

	var viewport = new Ext.Viewport({
		layout : "fit",
		items : [tabpanel]
	});
	getCurrentSteps();
});