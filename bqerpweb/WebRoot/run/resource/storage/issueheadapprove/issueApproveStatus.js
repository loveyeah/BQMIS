function IssueApprove()
{
	this.getStatusName=function(statusCode)
	{
		if(statusCode=="0") return "未上报";
	    else if(statusCode=="1") return "已上报";
		else if(statusCode=="2") return "已审批结束";
		else if(statusCode=="9") return "已退回";
		else if(statusCode=="3") return "本部门领导已审批";
//		else if(statusCode=="4") return "检修安生部领导已审批";
//        else if(statusCode=="5") return "实业安生部领导已审批";
        else if(statusCode=="6") return "安生部领导已审批";
        else if(statusCode=="7") return "发电综合部领导已审批";
        else if(statusCode=="8") return "实业综合部领导已审批";
        else if(statusCode=="A") return "检修综合部领导已审批";
        else if(statusCode=="C") return "已审核";
        else if(statusCode=="B") return "红单";
        else return "";
	};
	
	this.approveQueryStatus=[
	['','所有状态'],['1','已上报'],['3','本部门领导已审批'],
	//['4','检修安生部领导已审批']
	//,['5','实业安生部领导已审批'],
	['6','安生部领导已审批'],['7','发电综合部领导已审批']
	,['8','实业综合部领导已审批'],['A','检修综合部领导已审批']
	];
	
	this.allQueryStatus=[
	['','所有状态'],['0','未上报'],['9','已退回'],['1','已上报'],['3','本部门领导已审批'],
//	['4','检修安生部领导已审批']
//	,['5','实业安生部领导已审批'],
	['6','安生部领导已审批'],['7','发电综合部领导已审批']
	,['8','实业综合部领导已审批'],['A','检修综合部领导已审批'],['2','已审批结束'],['C','已审核']
	];
	
	
}