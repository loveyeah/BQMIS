function ProtectApply()
{
	this.getStatusName=function (statusId)
	{
			//1--未上报 2---已退回 3---已上报 4----设备部专工已签发 5----设备部主任已签发 6----厂领导已批准 7-----已结束
			//8---当值值长已审批  9---当值班长已许可开工
		switch (statusId) {
			case 1 : return "未上报";
			case 2 : return "已退回";
			case 3 : return "已上报";
			case 4 : return "设备部专工已签发";
			case 5 : return "设备部主任已签发";
			case 6 : return "厂领导已批准";
			case 7 : return "已结束";
			case 8 : return "当值值长已审批";
			case 9 : return "当值班长已许可开工"
	   }
	}
	
	this.queryStatus=[
	                     ['','所有状态'],['1','未上报'],['2','已退回'],['3','已上报'],['4','设备部专工已签发'],
	                     ['5','设备部主任已签发'],['6','厂领导已批准'],['7','已结束'],['8','当值值长已审批'],['9','当值班长已许可开工']
	                   ];
	this.approveStatus= [
	                     ['','所有状态'],['3','已上报'],['4','设备部专工已签发'],
	                     ['5','设备部主任已签发'],['6','厂领导已批准'],['8','当值值长已审批'],['9','当值班长已许可开工']
	                   ];     
	 this.reportStatus=[['','所有状态'],['1','未上报'],['2','已退回']];                  
	
}