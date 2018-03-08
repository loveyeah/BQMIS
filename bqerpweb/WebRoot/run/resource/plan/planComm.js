
function getFlowCode(planSource)
{
	var flowCode="";
	if(planSource==4||planSource==5||planSource==10||planSource==11) flowCode="hfResourcePlanSC";
	else if(planSource==6||planSource==7) flowCode="hfResourcePlanXZ";
	else if(planSource==3) flowCode="hfResourcePlanGDZC";
	else if(planSource==12||planSource==13||planSource==14) flowCode="hfResourcePlanJSJ";
	else if(planSource==15) flowCode="hfResourcePlanLB";
	else flowCode="";
	return flowCode;
}

function getPlanDateInfoByDate(planType,planDate)
{
	//planType计划来源    planDate计划日期

		var myYear=parseInt(planDate.substring(0,4));
		var myMonth=parseInt(planDate.substring(5,7));
		
		var myDay=parseInt(planDate.substring(8,10));
		var mySeason=0;
		if(myMonth==0)  
		{
		myMonth=parseInt(planDate.substring(6,7));
		}
		if(myDay==0) 
		{
		myDay=parseInt(planDate.substring(9,10));
		}
		
	if(planType==4||planType==5||planType==13)
	{
	 //月份加1
		
		if(myMonth==12)
			{
				myYear=myYear+1;
				myMonth=1;
				
			}
			else
			{
				myMonth=myMonth+1;
			}
		
		
//		//月度计划
//		if(myDay>25)
//		{
//			if(myMonth==12)
//			{
//				myYear=myYear+1;
//				myMonth=1;
//				
//			}
//			else
//			{
//				myMonth=myMonth+1;
//			}
//		}
		return myYear+"年"+myMonth+"月";
	}
	else if(planType==6)
	{
		if(myMonth<=3) 
		{
		  
		  if(myMonth==3&&myDay>25)  mySeason=2;
		  else  mySeason=1;
		}
		else if(myMonth<=6) 
		{
		 if(myMonth==6&&myDay>25)  mySeason=3;
		  else  mySeason=2;
		
		}
		else if(myMonth<=9) 
		{
			 if(myMonth==9&&myDay>25)  mySeason=4;
		     else  mySeason=3;
		}
		else if(myMonth<=12) 
		{
			 if(myMonth==12&&myDay>25)  {mySeason=1; myYear=myYear+1;}
		     else  mySeason=4;
		}
		else mySeason=0;
		
//		//季度加1
//		if(mySeason==4)
//		{
//			myYear=myYear+1;
//			mySeason=1;
//		}
//		else
//		{
//			mySeason=mySeason+1;
//		}
		
		
		return myYear+"年第"+mySeason+"季度";
		
	}
	else return myYear+"年"+myMonth+"月"+myDay+"日";
	
	
	
	 
}

function planOriginalName(id) {
	if (id == "6") {
		return "行政类季度计划";
	} else if (id == "7") {
		return "行政类紧急计划";
	} else if (id == "4") {
		return "生产类月度计划";
	} else if (id == "10") {
		return "生产类紧急计划";
	} else if (id == "3") {
		return "固定资产类计划";
	} else if (id == "12") {
		return "计算机相关材料";
	} else if (id == "13") {
		return "计算机材料月度计划";
	} else if (id == "14") {
		return "计算机材料紧急计划";
	}  else if (id == "15") {
		return "劳保用品计划";
	}else {
		return "";
	}
}