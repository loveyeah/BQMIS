<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="power.ear.comm.Employee"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<script type="text/javascript" src="../ext/urlparams.js"></script>
		<script LANGUAGE=javascript> 
<!--   
var welcomeUrl = "http://"+location.host+"/power/"; 
document.write('<style type="text/css"> #tdObject {height:'+(screen.height-110)+'px } </style>'  );
var currentFile = null;

function onDocumentOpened(url, obj) {   
	//obj.Application.Name 文档类型
    //currentFile.name = url.substr(url.lastIndexOf("/")+1); 
	//currentFile.type =  url.substr(url.lastIndexOf(".")+1);
} 
function notifyCtrlReady()
{ 
	var params = window.dialogArguments;   
	currentFile = params;
	openWebUrl(params) 
}
<% 
    Employee employee = (Employee) session.getAttribute("employee");
	String workerName = employee.getWorkerName();
	System.out.println(workerName);
%> 
//进入留痕状态 
function openWebUrl(params){ 
		currentFile = params;
	    document.getElementById("oframe").SetMenuDisplay(0);//禁用保存按钮
		document.getElementById("oframe").Open(welcomeUrl+params.url, true);   
        document.getElementById("oframe").SettrackRevisions(params.showTrack);//1,0,4
        document.getElementById("oframe").Toolbars = true;//!params.readOnly;
        document.getElementById("trBtn").style.display = params.readOnly?'none':'block';
        document.getElementById("oframe").SetCurrUserName("<%=workerName%>"); 
        document.getElementById("oframe").ProtectDoc((params.readOnly?1:0),1,"nbstar"); 
		document.getElementById("oframe").focus();
		document.title = params.title;
}
function saveCopyDocToServer(){ 
	    var url = welcomeUrl+ "managecontract/saveContractFile.action";  
		document.getElementById("oframe").HttpInit();  
        document.getElementById("oframe").HttpAddPostString("id",currentFile.id);
        document.getElementById("oframe").HttpAddPostString("type",currentFile.type);
        document.getElementById("oframe").HttpAddPostCurrFile("docFile","tem.doc");  
        var  returnValue = document.getElementById("oframe").HttpPost(url);  
        if("success" == returnValue){
                alert("操作成功");        
        }else if("failed" == returnValue)
                alert("操作成功失败") 
	}
	function showOrHidenToolbar(){
		
	} 
	//关闭
	function closeDocument()
	{  
		 document.getElementById("oframe").Close();  
	}
	function checkIsDirty()
	{
		//var isDirty = document.getElementById("oframe").IsDirty; 
		//if(isDirty)
		//{
		//	event.returnValue = "您对文档做了修改，但还没有保存到服务器。确定要放弃修改吗？";
		//	return false;
		//}
	} 
	
//-->
</script>
 <script LANGUAGE="javascript" FOR="oframe"
			EVENT="OnDocumentOpened(str, obj)">
<!--
 onDocumentOpened(str, obj)
//-->
</script>
 
<script LANGUAGE="javascript" FOR="oframe" EVENT="NotifyCtrlReady">
<!--
 notifyCtrlReady()
//-->
</script> 
		 
<style type="text/css"></style>
	</head>
	<body onbeforeunload="return checkIsDirty()" onunload="closeDocument()">
		<table border=0 cellPadding=0 cellSpacing=0 width=100% height="500">
			<tr id="trBtn" bgcolor="#C3C9D1" height="30" valign="bottom">
				<td align=center>
					<div align="left" > 
						<input type="button" value="保存到服务器" onClick="saveCopyDocToServer()">
					</div>
				</td>
			</tr>
			<tr>
				<td id="tdObject" width="100%"  align=center><OBJECT VIEWASTEXT
						classid="clsid:00460182-9E5E-11D5-B7C8-B8269041DD57" 
						codebase="/power/comm/dsoframer/MyOffice.CAB#V2,3,0,0"
						  width="100%" height="90%"
						id="oframe" ><PARAM NAME="_ExtentX" VALUE="6350">
						<PARAM NAME="_ExtentY" VALUE="6350">
						<PARAM NAME="BorderColor" VALUE="-2147483632">
						<PARAM NAME="BackColor" VALUE="-2147483643">
						<PARAM NAME="ForeColor" VALUE="-2147483640">
						<PARAM NAME="TitlebarColor" VALUE="-2147483635">
						<PARAM NAME="TitlebarTextColor" VALUE="-2147483634">
						<PARAM NAME="BorderStyle" VALUE="1"> 
						<PARAM NAME="Titlebar" VALUE="0"> 
						<PARAM NAME="Menubar" VALUE="0"> 
						</OBJECT>
				</td>
			</tr> 
		</table>
	</body>
</html>
