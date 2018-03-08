	function getBillNameById(typeId)
	{
		if(typeId=="1") return "固定资产类";
		else if(typeId=="2") return "专项物资类";
		else if(typeId=="3") return "维修费用领用";
		else if(typeId=="4") return "生产类";
		else if(typeId=="5") return "行政办公类";
		else if(typeId=="12") return "计算机相关材料";// add by ywliu 20091023
		else if(typeId=="15") return "劳保用品类";
		else return "";
	}