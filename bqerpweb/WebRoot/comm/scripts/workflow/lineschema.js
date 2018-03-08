
//将节点对应得xml中得值初始化到HTML中
function initHtmlFromNode(v_curLine)
{

	setSelectActivity();
	
	//设置显示名称
	document.getElementsByName("displayName")[0].value=v_curLine.selectSingleNode("propertyList/property[@name='displayName']/value").text;
	
	//设置“默认”选项
	var v_isDefault = v_curLine.selectSingleNode("propertyList/property[@name='isDefault']/value").text;
	if(v_isDefault == "true"){
		document.getElementsByName("checkbox_isDefault")[0].checked = true;
		isDefault();
	}
	if(v_isDefault == "false"){
     	document.getElementsByName("checkbox_isDefault")[0].checked = false;
		//从线上的属性中读取线上判断条件的中文名称
     	document.getElementsByName("complexExpressionValue")[0].value= curLine_grahic.expconnector.selectSingleNode("cndesc").text;
     	//设置优先级
		document.getElementsByName("priority")[0].value=v_curLine.selectSingleNode("propertyList/property[@name='priority']/value").text;

  		isDefault();

	}
	
}


//将页面HTML上得元素值设置到节点上
function setNodeFromHtml()
{
	//设置显示名称
	v_curLine.selectSingleNode("propertyList/property[@name='displayName']/value").text=document.getElementsByName("displayName")[0].value;
	var v_isDefault = document.getElementsByName("checkbox_isDefault")[0].checked;	
	if(v_isDefault == true){
		v_curLine.selectSingleNode("propertyList/property[@name='isDefault']/value").text = "true";
	}
	
	if(v_isDefault == false){
		v_curLine.selectSingleNode("propertyList/property[@name='isSimpleExpression']/value").text = "false";
		
		
		//从页面上获取复杂表达式的值
		var v_complexExpressionValue = document.getElementsByName("complexExpressionValue")[0].value;
		//将此值的中文名存在线上的complexValue属性中，便于打开页面时直接从此属性读取中文名称
		curLine_grahic.expconnector.selectSingleNode("cndesc").text=v_complexExpressionValue;
		
		//调用函数将中文翻译为引擎能够识别的规则集合,此方法定义在ruleOperation.js文件中
		var v_rule = translateFromCnToRule(v_complexExpressionValue);
		v_rule = v_rule.replace(/\?/g,curLine_grahic.iconEnd.activityId);
		alert(v_rule);
		//alert(v_rule);
		
		if(v_rule.trim()==""){
			alert('复杂表达式不能为空');
			return;
		}
		//将表达式列表存在当前线的属性上
		curLine_grahic.ruleList=ruleList;
		v_curLine.selectSingleNode("propertyList/property[@name='complexExpressionValue']/value").text = v_rule;
		//设置优先级
		v_curLine.selectSingleNode("propertyList/property[@name='priority']/value").text=document.getElementsByName("priority")[0].value;
		v_curLine.selectSingleNode("propertyList/property[@name='isDefault']/value").text = "false";
	}
	
	
    window.close();
}

//通过是否允许多工作项进行控制
function isDefault(){
	
	var v_isDefault = document.getElementsByName("checkbox_isDefault")[0];
	if(v_isDefault.checked ==true){
		document.getElementsByName("complexExpressionValue")[0].disabled=true;
		document.getElementsByName("priority")[0].disabled=true;
		document.getElementsByName("leftvalue")[0].disabled=true;
		document.getElementsByName("operator")[0].disabled=true;
		document.getElementsByName("rightvalue")[0].disabled=true;
		document.getElementsByName("addSimpleValue")[0].disabled=true;
		document.getElementsByName("select_activity")[0].disabled=true;
		eval( "document.all.expressresource" ).disabled=true;
		eval( "document.all.relativeEntity" ).disabled=true;
	}
	
	if(v_isDefault.checked ==false){
		document.getElementsByName("complexExpressionValue")[0].disabled=false;
		document.getElementsByName("priority")[0].disabled=false;
		document.getElementsByName("leftvalue")[0].disabled=false;
		document.getElementsByName("operator")[0].disabled=false;
		document.getElementsByName("rightvalue")[0].disabled=false;
		document.getElementsByName("addSimpleValue")[0].disabled=false;
		document.getElementsByName("select_activity")[0].disabled=false;
		eval( "document.all.expressresource" ).disabled=false;
		eval( "document.all.relativeEntity" ).disabled=false;
	}	
	
}

function setSelectActivity(){
	
		var v_states = curLine_grahic.states.selectNodes("state[@typeName='ManualActivity' or @typeName='AutoActivity']");

			for(var j=0;j<v_states.length;j++){
				var v_option = document.createElement("OPTION");
				select_activity.options.add(v_option);
				v_option.innerText = v_states[j].selectSingleNode("propertyList/property[@name='activityName']/value").text;
				v_option.value = v_states[j].selectSingleNode("propertyList/property[@name='activityId']/value").text;
			}

}

