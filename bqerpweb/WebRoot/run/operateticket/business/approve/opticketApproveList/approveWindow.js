/**
 * 操作人审批画面
 * 
 * @author 黄维杰
 * @modify sltang
 * @version 1.0
 */
Ext.onReady(function() {
	var arg = window.dialogArguments;
	var opticketCode = arg.opticketCode;
	var opticketStatus = arg.opticketStatus;
	var opticketType = arg.opticketType;
	var isSingle = arg.isSingle;
	var entryId = arg.entryId;
	var flowCode = arg.flowCode;

	var panel1 = new Ext.FormPanel({
		id : 'tab1',
		layout : 'fit',
		// width : 950,
		title : getName(opticketStatus, isSingle),
		items : [{
			html : '<iframe border="0" src="run/operateticket/business/approve/opticketApproveList/opApprove.jsp?"  style="width:100%;height:480;border:0px;""></iframe>'
		}]
	});
	var firstPanelSrc = "run/operateticket/business/approve/opticketApproveList/operateStepInfoList.jsp";
	var panel2 = new Ext.FormPanel({
		id : 'tab2',
		layout : 'fit',
		title : '操作步骤信息',
		items : [{
			html : '<iframe border="0" id="step" name="step"  src="'
					+ firstPanelSrc
					+ '"  style="width:100%;height:480;border:0px;""></iframe>'
		}]
	});
	var panel4 = new Ext.FormPanel({
		id : 'tab4',
		layout : 'fit',
		title : '操作前检查项目信息',
		items : [{
			html : '<iframe border="0" src="run/operateticket/business/approve/opticketApproveList/befCheckStepInfo.jsp?"  style="width:100%;height:480;border:0px;""></iframe>'
		}]
	});
	var panel5 = new Ext.FormPanel({
		id : 'tab5',
		layout : 'fit',
		title : '操作后完成工作信息',
		items : [{
			html : '<iframe border="0" src="run/operateticket/business/approve/opticketApproveList/finishWorkInfo.jsp?"  style="width:100%;height:480;border:0px;""></iframe>'
		}]
	});
	var panel3 = new Ext.FormPanel({
		id : 'tab3',
		layout : 'fit',
		title : '危险点控制信息',
		// render : function(){
		// if(opticketType.substring(0,2)=="01"){
		// panel3.hideMode=true;
		// }
		// },
		items : [{
			html : '<iframe border="0" src="run/operateticket/business/approve/opticketApproveList/dangerMeasureInfo.jsp?"  style="width:100%;height:480;border:0px;""></iframe>'
		}]
	});
	var tabPanel = new Ext.TabPanel({
		title : 'mytab',
		activeTab : 0,
		autoScroll : true,

		items : [{
			id : 'tab1',
			layout : 'fit',
			// width : 950,
			title : getName(opticketStatus, isSingle),
			listeners : {
				render : function() {
					if (opticketType.substring(0, 2) == "01") {
						tabPanel.hideTabStripItem("tab5");
						tabPanel.hideTabStripItem("tab4");
					}
				}
			},
			items : [{
				html : '<iframe border="0" src="run/operateticket/business/approve/opticketApproveList/opApprove.jsp?"  style="width:100%;height:480;border:0px;""></iframe>'
			}]
		}, panel2, panel3, {
			id : 'tab4',
			layout : 'fit',
			title : '操作前检查项目信息',
			items : [{
				html : '<iframe border="0" src="run/operateticket/business/approve/opticketApproveList/befCheckStepInfo.jsp?"  style="width:100%;height:440;border:0px;""></iframe>'
			}]
		}, {
			id : 'tab5',
			layout : 'fit',
			title : '操作后完成工作信息',
			items : [{
				html : '<iframe border="0" src="run/operateticket/business/approve/opticketApproveList/finishWorkInfo.jsp?"  style="width:100%;height:440;border:0px;""></iframe>'
			}]
		}]
	});
	var wnd = new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [tabPanel]
	});

	function getName() {
		if (OpStatus.readyReport == opticketStatus) {
			if (isSingle == "Y") {
				return opStatusName.chargerApprove;
			} else {
				return opStatusName.watcherApprove;
			}
		}else if(OpStatus.watcherReady==opticketStatus){
			return opStatusName.chargerApprove
		}else if(OpStatus.chargeReady==opticketStatus){
			return opStatusName.dutyApprove
		}else if(OpStatus.dutyReady==opticketStatus){
			if (isSingle == "Y") {
				return opStatusName.operatorBackFill;
			} else {
				return opStatusName.watcherBackFill;
			}
		}
	}
});
