/**
 * 操作人审批画面
 * 
 * @author 黄维杰
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
		title : getName(isSingle),
		autoScroll : true,
		items : [{
			html : '<iframe src="run/operateticket/business/approve/opticketApproveList/backFillAppRJ/backFillOpApproveRJ.jsp" name="app" style="width:100%;height:480;border:0px;"></iframe>'
		}]
	});
	
//		firstPanelSrc = "run/operateticket/business/approve/opticketApproveList/backFillAppRJ/updateOpStepInfoRJ.jsp";
	firstPanelSrc = "run/operateticket/business/approve/opticketApproveList/backFillApp/updateOpStepInfo.jsp";
	
	var panel2 = new Ext.FormPanel({
		id : 'tab2',
		layout : 'fit',
		title : '操作步骤信息',
		items : [{
			html : '<iframe id="step" name="step"  src="'+ firstPanelSrc +'"  style="width:100%;height:480;border:0px;""></iframe>'
		}]
	});
	var panel3 = new Ext.FormPanel({
		id : 'tab3',
		layout : 'fit',
		title : '危险点控制信息',
		items : [{
//			html : '<iframe src="run/operateticket/business/approve/opticketApproveList/backFillAppRJ/dangerMeasureRJ.jsp"  style="width:100%;height:520;border:0px;""></iframe>'
			html : '<iframe src="run/operateticket/business/approve/opticketApproveList/backFillApp/dangerMeasure.jsp"  style="width:100%;height:480;border:0px;""></iframe>'
		}]
	});
	var tabPanel = new Ext.TabPanel({
		title : 'mytab',
		activeTab : 0,
		autoScroll : true,
		items : [panel1, panel2,panel3]

	});

	var wnd = new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [tabPanel]
	});

	function getName() {
//			if (opticketStatus == "1") {
//				return "监护人审核";
//			} else if (opticketStatus == "2") {
//				return "值班负责人审核";
//			} else if (opticketStatus == "3") {
//				return "值长批准";
//			} else if (opticketStatus == "4") {
				if(isSingle=="Y"){
					return opStatusName.operatorBackFill;
				}else{
					return opStatusName.watcherBackFill;
				}
			}
//	}
});

	