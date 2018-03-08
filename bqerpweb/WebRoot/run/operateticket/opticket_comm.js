/**
 * 由操作票号取得操作票类型
 * 
 * @param {String}
 *            opCode 操作票号
 * @return {String}
 */
function getOpType(opCode) {
	if (typeof(opCode) != "undefined") {
		if (opCode.substring(0, 2) == "00") {
			return '电气操作票';
		} else {
			return '热力机械操作票';
		}
	} else {
		return null;
	}
};
/**
 * 取得状态描述
 * 
 * @param {}
 *            statusId
 * @return {String}
 */
function getOpStatusName(statusId) {
	switch (statusId) {
		case OpStatus.notReport : {
			return "未上报";
		}
			;
		case OpStatus.readyReport : {
			return "已上报";
		}
			;
		case OpStatus.watcherReady : {
			return "监护人已审批";
		}
			;
		case OpStatus.chargeReady : {
			return "值班负责人已审批";
		}
			;
		case OpStatus.dutyReady : {
			return "值长已审批";
		}
			;
		case OpStatus.operatorWrite : {
			return "已终结";
		}
			;
		case OpStatus.safeDeptReady : {
			return "安检部门已审批";
		}
			;
		case OpStatus.EngineerReady : {
			return "总工已审批";
		}
			;
		case OpStatus.notOrdered : {
			return "未下令";
		}
			;
		case OpStatus.backReady : {
			return "已退回";
		}
			;
		case OpStatus.AbandonedReady : {
			return "已作废";
		}
			;
		case OpStatus.undefined : {
			return "未定义";
		}
			;
		case OpStatus.headReady : {
			return "主任已审批";
		}
	}
}

/**
 * 操作任务树顶层结点
 * 
 * @type {}
 */
var OpTypeRootNode = {
	id : '-1',
	text : '灞桥电厂操作任务'
};
/**
 * 操作票状态
 * 
 * @type {}
 */
var OpStatus = {
	notReport : "0",// 未上报
	readyReport : "1",// 已上报
	watcherReady : "2",// 监护人已审批
	chargeReady : "3",// 值班负责人已审批
	dutyReady : "4",// 值长已审批
	operatorWrite : "5",// 已终结
	safeDeptReady : "6",// 安检部门已审批
	EngineerReady : "7",// 工程师已审批
	notOrdered : "9",// 未下令
	backReady : "T",// 已退回
	AbandonedReady : "Z",// 已作废
	headReady : "H",//主任已审批
	undefined : "U"// 未定义
};
var opStatusName = {
	reportName : "上报",
	watcherApprove : '监护人审核',
	chargerApprove : '值班负责人审核',
	dutyApprove : '值长审核',
	operatorBackFill : '操作人回填',
	watcherBackFill : '监护人回填',
	safeDeptmentApprove : '安环部审批',
	EngineerApprove : '工程师审批',
	headApprove :'主任审批',
	Undefined : '未定义'
}
var OperationWorkflowCode = "hfOpTicket";
var OpStatusQueryList = [['所有', ''], ['未上报', "'0'"], ['上报', "'1'"],
		['监护人已审批', "'2'"], ['值班负责人已审批', "'3'"], ['值长已审批', "'4'"], ['已终结', "'5'"],
		["已退回", "'T'"], ["已作废", "'Z'"], ['安环部已审核', "'6'"], ['总工程师已审核', "'7'"],['主任审批',"'H'"]];
var standardOpStatusQueryList =[['所有', ''], ['未上报', "'0'"], ['上报', "'1'"],
		['两票审核小组已审核',"'H'"], ['安环部已审核', "'6'"], ['总工程师已审核', "'7'"],["已退回", "'T'"],['准标准票',"'0','1','H','6'"]];
/**
 * 电气操作票票面预览
 * 
 * @param {}
 *            opticketNo
 */
function viewOperateTicketDQBirt(opticketNo) {
	var url = '/powerrpt/report/webfile/bqmis/electricOpreate.jsp?no='
			+ opticketNo;
    url = encodeURI(url);
	window.open(url);
}
/**
 * 热机操作票票面预览
 * 
 * @param {}
 *            opticketNo
 */
function viewOperateTicketRJBirt(opticketNo) {
	var url = '/powerrpt/report/webfile/bqmis/energMachineOpreate.jsp?no='
			+ opticketNo;
			url = encodeURI(url);
	window.open(url);
}
/**
 * 热机操作票危险点预览
 * 
 * @param {}
 *            opticketNo
 */
function viewOperateTicketDangerBirt(opticketNo) {
	var url = '/powerrpt/report/webfile/bqmis/energMachineDangerControl.jsp?no='
			+ opticketNo;
			url = encodeURI(url);
	window.open(url);
}
/**
 * 电气倒闸操作票操作前检查项目票预览
 * 
 * @param {}
 *            opticketNo
 */
function viewOperateTicketBEFBirt(opticketNo) {
	var url = '/powerrpt/report/webfile/bqmis/electricOperateBeforeCheckItem.jsp?no='
			+ opticketNo;
			url = encodeURI(url);
	window.open(url);
}
/**
 * 电气倒闸操作票操作后完成的工作票预览
 * 
 * @param {}
 *            opticketNo
 */
function viewOperateTicketAFTBirt(opticketNo) {
	var url = '/powerrpt/report/webfile/bqmis/electricOperateAfterCheckItem.jsp?no='
			+ opticketNo;
			url = encodeURI(url);
	window.open(url);
}
/**
 * 流程预览
 * 
 * @param {}
 *            opticketNo
 */
function viewLC(entryId, flowCode) {
	var url = "";
	if (entryId == null || entryId == "") {
		url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
				+ flowCode;
	} else {
		url = "/power/workflow/manager/show/show.jsp?entryId=" + entryId;
	}
	window
			.open(
					url,
					"",
					"height=400, width=700, toolbar =no, menubar=no, scrollbars=no, resizable=no, location=no, status=no,top=200,left=300");
}
