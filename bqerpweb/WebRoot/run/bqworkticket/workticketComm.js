function getReportUrl(workticketType, strBusiNo, fireLevelId) {
	
var url="";
	if (workticketType == "1")
		url= "/powerrpt/report/webfile/bqmis/ElectricOne.jsp?no=" + strBusiNo;
	else if (workticketType == "2")
		url= "/powerrpt/report/webfile/bqmis/ElectricTwo.jsp?no=" + strBusiNo;
	else if (workticketType == "3")
		url= "/powerrpt/report/webfile/bqmis/energMachineOne.jsp?no="
				+ strBusiNo;
	else if (workticketType == "5")
		url= "/powerrpt/report/webfile/bqmis/energControlOne.jsp?no="
				+ strBusiNo;
	else if (workticketType == "7")
		url= "/powerrpt/report/webfile/bqmis/energMachineTwo.jsp?no="
				+ strBusiNo;
	else if (workticketType == "8")
		url= "/powerrpt/report/webfile/bqmis/energControlTwo.jsp?no="
				+ strBusiNo;

	else if (workticketType == "4") {
		if (fireLevelId == "1") {
			url= "/powerrpt/report/webfile/bqmis/HeatOne.jsp?no=" + strBusiNo;
		}
		else if (fireLevelId == "2") {
			url= "/powerrpt/report/webfile/bqmis/HeatTwo.jsp?no=" + strBusiNo;
		}
		else
		{
			url="";
		}
		
	} else  url= "";
		
		
	url=encodeURI(url);
	return url;
}

function getFlowCode(workticketType, firelevelId) {
	// 灞桥
	if (workticketType == "1") {
		flowCode = "bqWorkticket1";
	} else if (workticketType == "2") {
		flowCode = "bqWorkticket2";
	} else if (workticketType == "3") {
		flowCode = "bqWorkticketRJ1";
	} else if (workticketType == "4") {
		// flowCode="bqWorkticketDH1";
		// 动火票级别
		var level = firelevelId;
		if (level == "1") {
			flowCode = "bqWorkticketDH1";
		} else {
			flowCode = "bqWorkticketDH2";
		}
	} else if (workticketType == "5") {
		flowCode = "bqWorkticketRK1";
	}
	// else if(workticketType=="6")
	// {
	// flowCode="bqWorkticketDH2";
	// }
	else if (workticketType == "7") {
		flowCode = "bqWorkticketRJ2";
	} else if (workticketType == "8") {
		flowCode = "bqWorkticketRK2";
	} else {
		flowCode = "";
	}
	return flowCode;
};
var Workticket = {
	spellWorkContent : function(store){
	//	var content = "(1) ";
		var content = "";
		var l=2;
		for(var i=0;i<store.getCount();i++)
		{ 
			var tr = "";
			var record = store.getAt(i);
			if(record.get("frontKeyDesc") != null)
			tr += record.get("frontKeyDesc");
			//if(record.get("locationName") != null)
			//tr += record.get("locationName");
			if(record.get("equName") != null)
			tr += record.get("equName"); 
			//tr +=(record.get("attributeCode") == null?"":("("+record.get("attributeCode")) +")");
			
			if(!(record.get("attributeCode")==null||record.get("attributeCode")=="temp"))
			{
				tr+="("+record.get("attributeCode")+")";
			}
			if(record.get("backKeyDesc") != null)
			tr += record.get("backKeyDesc");
			if(record.get("worktypeName") != null)
			tr += record.get("worktypeName");
			if(record.get("flagDesc") != null)
			tr += record.get("flagDesc"); 
			if(record.get("isreturn") != "N")
			{
				tr += "\n";
//				if(i != (store.getCount()-1)) {
//					tr += "(" + l + ") ";
//					l++;
//				}
			} 
			content += tr; 
		} 
//		if(content == "(1) ")
//		{
//			content = "";
//		}
		return  content ;
	},
		spellContentByArray:function(contentAryay)
	{
		var content="";
	
		
		var l = 2;
		for (var i = 0; i < contentAryay.length; i++) {
			var tr = "";
			var record = contentAryay[i];
			if (record.frontKeyDesc != null)
				tr += record.frontKeyDesc;
			if (record.locationName != null)
				tr += record.locationName;
			if (record.equName!= null)
				tr += record.equName;

			if (!(record.attributeCode == null || record
					.attributeCode == "temp")) {
				tr += "(" + record.attributeCode + ")";
			}
			if (record.backKeyDesc != null)
				tr += record.backKeyDesc;
			if (record.worktypeName!= null)
				tr += record.worktypeName;
			if (record.flagDesc!= null)
				tr += record.flagDesc;
			if (record.isreturn!= "N") {
				tr += "\n";
//				if (i != (contentAryay.length - 1)) {
//					tr += "(" + l + ") ";
//					l++;
//				}
			}
			content += tr;
		}
		if(content == "(1) ")
		{
		  	content="";
		}
		
		return content;
	}
};

function getApproveNoticeInfo(blockCode)
{
	var notice="";
	//1号机组、2号机组、1号和2号公用 ：300MW
	//11号机组、12号机组、11和12号公用 ：125MW
	//全场公用系统： 全场公用
	if(blockCode=="10"||blockCode=="20"||blockCode=="B0")
	{
		notice="300MW运行人员处理";
	}
	else if(blockCode=="1"||blockCode=="2"||blockCode=="A0")
	{
		notice="125MW运行人员处理";
	}
	else if(blockCode=="00")
	{
		notice="全场公用";
	}
	else
	{
		notice="";
	}
	return notice;
	
}
