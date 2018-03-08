function initHtmlFromNode(v_nodeXml)
{
//<-----------------------------------------------------基本信息--------------------------------------------------->
	//设置activityName
	document.getElementsByName("activitycode")[0].value=v_nodeXml.selectSingleNode("propertyList/property[@name='lineCode']/value").text;
    document.getElementsByName("activityname")[0].value=v_nodeXml.selectSingleNode("propertyList/property[@name='lineName']/value").text;
    if(document.getElementsByName("activitycode")[0].value  == "")
    {
        var from = v_nodeXml.selectSingleNode("from").text;
        var froma = from.split(".");
        var to = v_nodeXml.selectSingleNode("to").text;
        var toa = to.split(".");
        var id = froma[2] + toa[2];
        document.getElementsByName("activitycode")[0].value = "event" + id;
    }

}
function setNodeFromHtml(v_nodeXml)
{
//<-----------------------------------------------------基本信息--------------------------------------------------->

	//设置activityName
	v_nodeXml.selectSingleNode("propertyList/property[@name='lineCode']/value").text=document.getElementsByName("activitycode")[0].value;
	v_nodeXml.selectSingleNode("propertyList/property[@name='lineName']/value").text=document.getElementsByName("activityname")[0].value;
	window.close();

}

