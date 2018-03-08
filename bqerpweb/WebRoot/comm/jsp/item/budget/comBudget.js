function getBudgetName(budgetCode,flag,deptCode)
{
//	var budgetName="";
//	if(budgetCode=="zzfy") budgetName="生产类费用";
//	else if(budgetCode=="lwcb") budgetName="劳务费用";
//	else if(budgetCode=="2") budgetName="#5机凝泵变频改造项目";
//	else if(budgetCode=="3") budgetName="600MW机组仿真系统建设项目";
//	else if(budgetCode=="HF1005A03") budgetName="#5机组增加一套空压机系统";
//	else if(budgetCode=="HF1005A04") budgetName="F磨加装等离子装置一套";
//	else if(budgetCode=="HF1099A01") budgetName="数字光谱仪";
//	else if(budgetCode=="HF1099A02") budgetName="粉煤灰全电子汽车衡";
//	else if(budgetCode=="HF1005A07") budgetName="主机转子支架及高、低压缸检修平台";
//	else if(budgetCode=="HF1005A08") budgetName="发电机转子抽穿专用装置";
//	else if(budgetCode=="HF1099A03") budgetName="锅炉滤油机购置";
//	else if(budgetCode=="HF1099A05") budgetName="电气试验仪表购置";
//	else if(budgetCode=="HF1005B01") budgetName="磨煤机磨辊及衬板";
//	else if(budgetCode=="HF1005B02") budgetName="捞渣机链条";
//	else if(budgetCode=="HF1005A17") budgetName="一次风机、送风机轴承组拆卸专用液压拉马";
//	else if(budgetCode=="HF1005B03") budgetName="水冷壁检修升降架";
//	else if(budgetCode=="HF1005A18") budgetName="＃5机组部分流量检测装置更换";
//	else if(budgetCode=="HF1005A19") budgetName="工业废水，输煤监控；工业水、消防水；制氢站；生活污水控制系统加装自带电池组UPS";
//	else if(budgetCode=="HF1099B02") budgetName="数字化煤场建设";
//	else if(budgetCode=="HF1099A06") budgetName="调度数据网二次安防";
//	else if(budgetCode=="HF1099A07") budgetName="负荷调度监控软件系统";
//	else if(budgetCode=="4") budgetName="自控资本性投资费用";
//	else if(budgetCode=="5") budgetName="厂控修理项目";
//	else if(budgetCode=="6") budgetName="安全两措计划";
//	else if(budgetCode=="51") budgetName="辅汽供热改造";
//	else if(budgetCode=="52") budgetName="工业水系统改造两子项";
//	else budgetName=budgetCode;
// 	return budgetName;
	
 
   var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", 'managebudget/getItemNameByItemCode.action?itemCode='
			+ budgetCode+'&deptCode='+deptCode, false);
	conn.send(null);
	 
	
	// 成功状态码为200
	if (conn.status == "200") {
		var result = conn.responseText;
		var data=result.split(',');
		if(flag==null||flag=="")
		{
			// add by liuyi 20100409 如果为中文就直接输出
			if(typeof(budgetCode) == 'undefined')
				return '';
			var patrn = /[U4E00-U9FA5]/g;
			if (!patrn.test(budgetCode))
			{
				return budgetCode;
			}
			
			return data[0];
		}
		else if(flag==1)
		{
		  //获得预算值
		  return data[1];	
		}
		else if(flag==2)
		{
			//获得实际值
				var conn1 = Ext.lib.Ajax.getConnectionObject().conn;
	         conn1.open("POST", 'resource/getYearIssuePriceByDept.action?itemCode='+ budgetCode+'&deptCode='+deptCode, false);
	          conn1.send(null);
			
	          if (conn1.status == "200")
	          {
	          	var value=conn1.responseText;
	          var	mydata=value.split(",");
			    return mydata[0];
	          }
		}
		else if(flag==3)
		{
			//获得实际值
				var conn1 = Ext.lib.Ajax.getConnectionObject().conn;
	         conn1.open("POST", 'resource/getYearIssuePriceByDept.action?itemCode='+ budgetCode+'&deptCode='+deptCode, false);
	          conn1.send(null);
			
	          if (conn1.status == "200")
	          {
	          	var value=conn1.responseText;
	        
			    return value;
	          }
		}
		else
		{
			return  data[0];
		}
	
	}
}


function getPrjShowNo(prjNo)
{
	if(prjNo==null||prjNo=="")
	{
		return "";
	}
	
	var conn1 = Ext.lib.Ajax.getConnectionObject().conn;
	         conn1.open("POST", 'manageproject/getProjectNoShow.action?prjNo='+ prjNo, false);
	          conn1.send(null);
			
	          if (conn1.status == "200")
	          {
	          	return conn1.responseText;
	          }
	
}