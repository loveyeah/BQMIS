function busiStatus(status) {
		if (status == "0")
			return "未上报";
		else if (status == "1")
			return "已上报";
		else if (status == "2")
			return "已审批结束";
		else if (status == "3")
			return "本部门领导已审批";
		else if (status == "4")
			return "实业安生部领导已审批";
		else if (status == "5")
			return "检修安生部领导已审批";
		else if (status == "6")
			return "发电安生部领导已审批";
		else if (status == "8")
			return "发电综合部领导已审批";
		else if (status == "9")
			return "已退回";
		else if (status == "A")
			return "实业综合部领导已审批";
		else if (status == "B")
			return "检修综合部领导已审批";
		else if (status == "F")
			return "安生部领导已审批";
		else if (status == "G")
			return "商务部领导已审批";
		else if (status == "H")
			return "物管中心已审批";
		else if (status == "I")
			return "财务部已审批";
		else if (status == "J")
			return "监察审计科已审批";
		else if (status == "K")
			return "分管副总已审批";
		// add by liuyi 091104 信息中心已审批
		else if (status == "P")
			return "信息中心已审批";
		else if (status == "Q")
			return "发电人力资源部已审批";
		else if (status == "R")
			return "实业人力资源部已审批";
		else if (status == "S")
			return "检修人力资源部已审批";
		else if (status == "T")
			return "技术支持部领导已审批";
		else if (status == "U")
			return "发电安监部领导已审批";
		else
			return "";
	}