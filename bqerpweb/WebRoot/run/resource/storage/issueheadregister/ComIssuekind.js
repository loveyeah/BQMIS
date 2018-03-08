function getIssuekindName(kindCode)
{
	var kindName="";
	if(kindCode=="1") kindName="固定资产类";
	else if(kindCode=="2") kindName="专项物资类";
	else if(kindCode=="12") kindName="计算机相关材料";
	else if(kindCode=="4") kindName="生产类";
	else if(kindCode=="5") kindName="行政办公类";
	else if(kindCode=="15") kindName="劳保用品类";
	else kindName=kindCode;
 	return kindName;
}