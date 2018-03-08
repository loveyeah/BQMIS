<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
<head> 
<title>脱硫台帐综合分析</title> 

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
			 response.setHeader("Pragma","No-cache");    
			 response.setHeader("Cache-Control","no-cache");    
			 response.setDateHeader("Expires", -1);
%>
<base href="<%=basePath%>">
	<script type="text/javascript"
			src="comm/datepicker/WdatePicker.js" defer="defer"></script>
<SCRIPT src="<%=basePath %>comm/weboffice/main.js" type=text/javascript></SCRIPT>
<!-- --------------------=== 调用Weboffice初始化方法 ===--------------------- -->
<SCRIPT language=javascript event=NotifyCtrlReady for=WebOffice1>
/****************************************************
*
*	在装载完Weboffice(执行<object>...</object>)
*	控件后执行 "WebOffice1_NotifyCtrlReady"方法
*
****************************************************/
	//WebOffice1_NotifyCtrlReady()  
	SetCustomToolBtn(0,"编辑模板");
	SetCustomToolBtn(1,"保存模板");
	var webObj=document.getElementById("WebOffice1");
	bToolBar_New_onclick();
	bToolBar_Open_onclick();
	bToolBar_Save_onclick();
</SCRIPT>

<SCRIPT language=javascript event=NotifyWordEvent(eventname) for=WebOffice1>
<!--
 WebOffice1_NotifyWordEvent(eventname)
//-->
</SCRIPT>


<SCRIPT language=javascript>
var isEditTemplate = false;
var templateUrl = "manage/stat/query/jxlsreport/tuliutotal.xls";
var templateType = "xls";
function viewTemplate(){
	isEditTemplate = true;
	document.all.WebOffice1.OptionFlag |= 0x0400;  
	document.all.WebOffice1.ReadOnly = 0; 
	document.all.WebOffice1.LoadOriginalFile("<%=basePath %>"+templateUrl,templateType);
	 
}

function saveTemplate(){
    if(isEditTemplate)
    { 
		var webObj=document.getElementById("WebOffice1"); 
		//if(webObj.IsModify())
		//{ 
			webObj.HttpInit();			//初始化Http引擎
			// 添加相应的Post元素 
			webObj.HttpAddPostString("id","tuoliuitem");
			webObj.HttpAddPostString("templateUrl", templateUrl);
			webObj.HttpAddPostString("DocType",templateType);
			webObj.HttpAddPostCurrFile("docFile","");		// 上传文件 
			var returnValue = webObj.HttpPost("<%=basePath %>manager/saveTuoLiuItemTmplate.action");	// 判断上传是否成功
			if("succeed" == returnValue){
				alert("模板保存成功");	
			}else if("failed" == returnValue)
			alert("模板保存失败");
		//}else
		//{
		//	alert("模板没有修改，无需保存！");
		//}
	}else{
		alert("只能保存模板，请先点击“编辑模板”");
	}
	
}

/****************************************************
*
*		控件初始化WebOffice方法
*
****************************************************/
function queryData() {
    isEditTemplate = false;
	//document.all.WebOffice1.OptionFlag |= 128;
	document.all.WebOffice1.OptionFlag |= 0x0020;
	// 新建文档
	var date = document.getElementById("date").value; 
	document.all.WebOffice1.ReadOnly = 1;
	//document.all.WebOffice1.ShowToolBar = 0; 
		document.all.WebOffice1.LoadOriginalFile("<%=basePath %>manager/queryTuoLiuItem.action?date="+date+"&templateUrl="+templateUrl,"xls");
  		document.all.WebOffice1.SetToolBarButton2("Menu Bar",1,8); 
		//document.all.WebOffice1.DownLoadFile("http://localhost:8080/jxlweb/build/adjacentlists.xls", "doc");

}
var flag=false;
function menuOnClick(id){
	var id=document.getElementById(id);
	var dis=id.style.display;
	if(dis!="none"){
		id.style.display="none";
		
	}else{
		id.style.display="block";
	}
}
/****************************************************
*
*		接收office事件处理方法
*
****************************************************/
var vNoCopy = 0;
var vNoPrint = 0;
var vNoSave = 0;
var vClose=0;
function no_copy(){
	vNoCopy = 1;
}
function yes_copy(){
	vNoCopy = 0;
}


function no_print(){
	vNoPrint = 1;
}
function yes_print(){
	vNoPrint = 0;
}


function no_save(){
	vNoSave = 1;
}
function yes_save(){
	vNoSave = 0;
}
function EnableClose(flag){
 vClose=flag;
}
function CloseWord(){
	
  document.all.WebOffice1.CloseDoc(0); 
}

function WebOffice1_NotifyWordEvent(eventname) {
	if(eventname=="DocumentBeforeSave"){
		if(vNoSave){
			document.all.WebOffice1.lContinue = 0;
			alert("此文档已经禁止保存");
		}else{
			document.all.WebOffice1.lContinue = 1;
		}
	}else if(eventname=="DocumentBeforePrint"){
		if(vNoPrint){
			document.all.WebOffice1.lContinue = 0;
			alert("此文档已经禁止打印");
		}else{
			document.all.WebOffice1.lContinue = 1;
		}
	}else if(eventname=="WindowSelectionChange"){
		if(vNoCopy){
			document.all.WebOffice1.lContinue = 0;
			//alert("此文档已经禁止复制");
		}else{
			document.all.WebOffice1.lContinue = 1;
		}
	}else   if(eventname =="DocumentBeforeClose"){
	    if(vClose==0){
	    	document.all.WebOffice1.lContinue=0;
	    } else{
	    	//alert("word");
		    document.all.WebOffice1.lContinue = 1;
		  }
 }
	//alert(eventname); 
}
</SCRIPT>
</head>
<body onunload="return window_onunload()">
<table width="100%" height="100%" cellpadding="0" cellspacing="0">
	<tr height="20">
		<td>
			<input type="text" id="date" value="2010-06-04" readOnly="true" onClick="WdatePicker()"/>
			<input type="button"  value="查询" onclick="queryData()"/>
		</td>
	</tr>
	<tr>
		<td>
		 	<object id=WebOffice1 height="100%" width='100%' 
					style='LEFT: 0px; TOP: 0px'  
					classid='clsid:E77E049B-23FC-4DB8-B756-60529A35FAD5' 
					codebase='<%=basePath %>comm/weboffice/weboffice_pdf_v6.0.5.0.cab#version=6,0,5,0'>
					<param name='_ExtentX' value='6350'>
					<param name='_ExtentY' value='6350'>
					<param name='ShowToolBar' value='0'>
			</OBJECT> 
		</td>
	</tr>
</table>
<SCRIPT LANGUAGE=javascript FOR=WebOffice1 event=NotifyToolBarClick(lCmd)> 
<!-- 
 WebOffice1_NotifyToolBarClick(lCmd) 
//--> 
</SCRIPT> 
<SCRIPT LANGUAGE=javascript>
//2. 截获保存事件处理事件 
function WebOffice1_NotifyToolBarClick(lCmd) { 
  if(32772==lCmd) { 
	  alert(9090);
  	document.all.WebOffice1.lEventRet = 0;
 }else if(32776 == lCmd){
	 document.all.WebOffice1.lEventRet = 0;
	 viewTemplate();
	 return false;
 }else if(32777 == lCmd){
	 document.all.WebOffice1.lEventRet = 0;
	 saveTemplate();
	 return false;
 } 
  
}

</script>
</body>
</html>