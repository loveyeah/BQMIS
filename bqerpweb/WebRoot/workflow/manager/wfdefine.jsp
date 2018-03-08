<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns:v="urn:schemas-microsoft-com:vml">
	<head>  
		<title>自定义工作流</title> 
		<jsp:include page="../../comm/jsp/commjsp.jsp"></jsp:include> 
		<script language="javascript" type="text/javascript"
			src="comm/scripts/workflow/graphic.js" charset="UTF-8"></script>
		<script language="javascript" type="text/javascript"
			src="comm/scripts/workflow/wfschema.js" charset="UTF-8"></script> 
		<style type="text/css">
		v\: * {
			behavior: url(#default#VML);
		}
</style>
	</head>
	<body ondblclick="setProcessProperty()"
		style="margin-left: 0px; margin-right: 0px; margin-top: 0px; margin-bottom: 0px;">
		<form id="form1" runat="server">
			<table align="left" border="2"
				style="position: absolute; height: 100%; border-right: #3399ff ridge;">
				<tr>
					<td
						style="width: 235px; font-size: 12pt; background-color: #ffffcc; height: 30px;">
						面版
					</td>
				</tr>
				<tr>
					<td class="fb_add_content"
						style="width: 235px; font-size: 10pt; height: 30px;">
						<img alt="" src="comm/images/workflow/connection.gif"
							onclick="drawLineStatus = !drawLineStatus; if (drawLineStatus) {document.body.style.cursor='default'; draw_label.innerText='可以连线';} else {document.body.style.cursor='hand';  draw_label.innerText='可以拖动';}" />
						连线 <<-点击它（
						<font id="draw_label" color="red">可以连线</font>）
					</td>
				</tr>
				<tr>
					<td style="height: 30px; width: 235px;">
						<input type="file" ID="xmlfileBrowse" />
					</td>
				</tr>
				<tr>
					<td style="width: 235px; height: 30px;">
						<input type="button" name="btnDown" value="下载"
							onclick="loadWFFromDB();" />
						<input type="button" name="loaddb" value="导入" onclick="loadWF();" />
						<input type="button" name="savedb" value="保存" onclick="saveWF();" />
						<input type="button" ID="btnUpload" value="编译"
							OnClick="compileWF();" />
					</td>
				</tr>
				<tr>
					<td style="width: 235px; height: 10px; background-color: #ccffff;">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td
						style="width: 235px; font-size: 12pt; background-color: #ffffcc; height: 30px;">
						资源
					</td>
				</tr>
				<tr>
					<td style="width: 235px; font-size: 10pt;" valign="top">

						<script language="JavaScript" type="text/javascript">   
						
                  var startStr1 = def_start_node.selectSingleNode("propertyList/property[@name='ActivityName']/value").text; 
                  var startStr2 = def_end_node.selectSingleNode("propertyList/property[@name='ActivityName']/value").text; 
                  var startStr3 = def_manual_node.selectSingleNode("propertyList/property[@name='ActivityName']/value").text; 
                   
                    </script>

						<img alt="" src="comm/images/workflow/start.gif" />
						<script language="JavaScript" type="text/javascript">    
   document.writeln(startStr1+"<br/>");  
  //-->   
                    </script>
						<img alt="" src="comm/images/workflow/manual.gif"
							ondragstart="setCapture();xx=event.x-this.offsetLeft;yy=event.y-this.offsetTop;"
							ondrag='this.style.left=event.x-xx; this.style.top=event.y-yy'
							ondragend="releaseCapture();m_drag_icon(this, 'ManualActivity', event.x, event.y, 5, 65,'','人工活动');" />
						人工活动
						<br />
						<!-- 
						<img alt="" src="comm/images/workflow/router.gif"
							ondragstart="setCapture();xx=event.x-this.offsetLeft;yy=event.y-this.offsetTop;"
							ondrag='this.style.left=event.x-xx; this.style.top=event.y-yy'
							ondragend="releaseCapture();m_drag_icon(this, 'RouterActivity', event.x, event.y, 5, 65,'','路由活动');" />
						路由活动
						</br>
						<img alt="" src="comm/images/workflow/.gif"
							ondragstart="setCapture();xx=event.x-this.offsetLeft;yy=event.y-this.offsetTop;"
							ondrag='this.style.left=event.x-xx; this.style.top=event.y-yy'
							ondragend="releaseCapture();m_drag_icon(this, 'JoinActivity', event.x, event.y, 5, 65,'','聚合活动');" />
						聚合活动
						<br />
						<img alt="" src="comm/images/workflow/auto.gif"
							ondragstart="setCapture();xx=event.x-this.offsetLeft;yy=event.y-this.offsetTop;"
							ondrag='this.style.left=event.x-xx; this.style.top=event.y-yy'
							ondragend="releaseCapture();m_drag_icon(this, 'AutoActivity', event.x, event.y, 5, 65,'','自由活动');" />
						自由活动
						<br />
						 -->
						<img alt="" src="comm/images/workflow/router.gif"
							ondragstart="setCapture();xx=event.x-this.offsetLeft;yy=event.y-this.offsetTop;"
							ondrag='this.style.left=event.x-xx; this.style.top=event.y-yy'
							ondragend="releaseCapture();m_drag_icon(this, 'RouterActivity', event.x, event.y, 5, 65,'','路由活动');" />
						路由活动
						</br>
						<img alt="" src="comm/images/workflow/join.gif"  
							ondragstart="setCapture();xx=event.x-this.offsetLeft;yy=event.y-this.offsetTop;"
							ondrag='this.style.left=event.x-xx; this.style.top=event.y-yy'
							ondragend="releaseCapture();m_drag_icon(this, 'JoinActivity', event.x, event.y, 5, 65,'','聚合活动');" />
						聚合活动
						<br />
						
						<img alt="" src="comm/images/workflow/end.gif" 
							ondragstart="setCapture();xx=event.x-this.offsetLeft;yy=event.y-this.offsetTop;"
							ondrag='this.style.left=event.x-xx; this.style.top=event.y-yy'
							ondragend="releaseCapture();m_drag_icon(this, 'EndActivity', event.x, event.y, 5, 65,'','结束活动');" />
						结束活动
					</td>
				</tr>
			</table>
		</form>
		<div id="icondiv" style="display: none">
			<table border="1" width="100%" height="100%" bgcolor="#cccccc"
				style="border: thin" cellspacing="0">
				<tr>
					<td style="cursor: default; border: outset 1; font-size: 12px;"
						align="center" onclick="parent.setIconProperty()">
						图元属性
					</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1; font-size: 12px;"
						align="center" onclick="parent.removeCurIcon()">
						删除图元
					</td>
				</tr>
			</table>
		</div>
		<div id="linediv" style="display: none">
			<table border="1" width="100%" height="100%" bgcolor="#cccccc"
				style="border: thin" cellspacing="0">
				<tr>
					<td class="fb_add_content"
						style="cursor: default; border: outset 1; font-size: 12px;"
						align="center" onclick="parent.setLineProperty()">
						连线属性
					</td>
				</tr>
				<tr>
					<td class="fb_add_content"
						style="cursor: default; border: outset 1; font-size: 12px;"
						align="center" onclick="parent.removeCurLine()">
						删除连线
					</td>
				</tr>
			</table>
		</div>
		<script type="text/javascript">
		var drag_x = 0, drag_y = 0;
var xx = 0, yy = 0;
var xx0, yy0;
var drawLineStatus = true; 
var action = "add";
function checkWF() { 
	// 验证节点
	var acts = schema.schema.selectNodes("autoGraphics/graphic/states/state");
	for (var i = 0; i < acts.length; i++) { 
		var type = acts[i].getAttribute("typeName");
		var id = acts[i].getAttribute("id");
		if (type == "Process")
			continue;
		var StrActivityName = acts[i]
				.selectSingleNode("propertyList/property[@name='ActivityName']/value").text;
		var StrActivityCode = acts[i]
				.selectSingleNode("propertyList/property[@name='ActivityCode']/value").text;
		if (StrActivityCode == "" || StrActivityName == "") {
			alert('所有活动的图元属性都必须设置名称！');
			return false;
		}
	}
	// 验证连线
	var line = schema.schema
			.selectNodes("autoGraphics/graphic/connectors/connector");
	for (var i = 0; i < line.length; i++) {
		var lineName = line[i]
				.selectSingleNode("propertyList/property[@name='lineName']/value").text;
		var lineCode = line[i]
				.selectSingleNode("propertyList/property[@name='lineCode']/value").text;
		if (lineCode == "" || lineName == "") {
			alert('所有的连线属性都必须设置名称！');
			return false;
		}
	} 
	return true; 
}
function compileWF() {
	var processCode = schema.schema.selectSingleNode("autoGraphics/graphic/states/state[@typeName='Process']/propertyList/property[@name='ProcessCode']/value").text;
	var processVersion = schema.schema.selectSingleNode("autoGraphics/graphic/states/state[@typeName='Process']/propertyList/property[@name='ProcessVersion']/value").text;
	var processName = schema.schema.selectSingleNode("autoGraphics/graphic/states/state[@typeName='Process']/propertyList/property[@name='ProcessName']/value").text;
	if(processCode==""||processVersion==""||processName=="")
	{
		alert("请双击设置流程属性!");
		return false;
	} 
	if(checkWF())
	{ 
		Ext.Msg.wait("提示","正在编译流程,请等待...");
		Ext.Ajax.request({
			url : 'MAINTWorkflow.do',
			method : 'post',
			params:{
				action : action,
				flowCode : processCode,
				flowVersion : processVersion,
				flowName : processName, 
				flowType : processCode + "-" + processVersion,
				xml : schema.schema.xml.replace("<?xml version=\"1.0\"","<?xml version=\"1.0\" encoding=\"UTF-8\"")
			},
			success : function(result, request) {  
				Ext.Msg.alert("提示","操作成功!");
				//Ext.Msg.hide();
			},
			failure : function(result, request){
				Ext.Msg.alert("提示","编译失败!");
			}
		}); 
	} 
}
function downFlow() {
	window.open(
					"../Maint/WorkflowTypeMaint.aspx",
					"",
					"dialogWidth:800px;dialogHeight:450px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;");
} 
var schema = new WFSchema(); // 当前工作流流程定义对象  
schema.newSchema("", "", "v1.0");


// 拖动图元的事件
function m_drag_icon(obj, type, x, y, x0, y0, ActivityCode, ActivityName) {
	var len = icons.length;
	// 创建新的图元
	icons[len] = new Icon();
	// 初始化图元
	icons[len].init(type, x - 20, y - 20, ActivityName, ActivityCode);
	obj.style.left = x0;
	obj.style.top = y0;
	// 在schema中添加state节点
	schema.create_state(icons[len]);
}
function loadWF() {
	var f = document.all("xmlfileBrowse");
	if (f.value == "") {
		alert("请选择一个文件！");
		f.focus();
		return false;
	}
	schema.load(f.value);  
}
function loadWFFromDB(){ 
	 var flowType = document.getElementById("xmlfileBrowse").value; 
	 Ext.Ajax.request({
		url : 'MAINTWorkflow.do',
		method : 'post',
		params:{
			action:'load',
			flowType : flowType
			},
		success : function(result, request) {  
			var xml = result.responseText ;
			if(xml == "")
			{
				alert("没有找到流程文件!");
				return false;
			}
			if(xml!=null && xml!="")
			{ 
				schema.loadByXmlData(xml); 
			}
			action = "update";
		} ,
		failure : function()
		{
			Ext.Msg.alert("提示","没有找到相关流程");
			//var xml = result.responseText ; 
		    //schema.loadByXmlData(xml);  
		}
	});
	 
}



// 将流程写到本地文件
function saveWF() {
	var processCode = schema.schema
			.selectSingleNode("autoGraphics/graphic/states/state[@typeName='Process']/propertyList/property[@name='ProcessCode']/value").text;
	var processName = schema.schema
			.selectSingleNode("autoGraphics/graphic/states/state[@typeName='Process']/propertyList/property[@name='ProcessName']/value").text;
	var processVersion = schema.schema
			.selectSingleNode("autoGraphics/graphic/states/state[@typeName='Process']/propertyList/property[@name='ProcessVersion']/value").text;
	if (processCode == "" || processName == "" || processVersion == "") {
		alert("请先双击设置流程属性!");
		return false;
	}
	var f = document.all("xmlfileBrowse");
	if (f.value == "") {
		var fso = new ActiveXObject("Scripting.FileSystemObject");
		if (!fso.FolderExists("c:\\wfxml")) {
			fso.CreateFolder("c:\\wfxml");
		}
		var fileName = "c:\\wfxml\\" + processCode + "_" + processVersion
				+ ".xml";
		var a = fso.CreateTextFile(fileName, true);
		// a.WriteLine("This is a test.hello");
		a.Close();
		schema.writeToFile(fileName); 
		f.focus()
		new ActiveXObject("wscript.shell").sendKeys(fileName)
	} else {
		// checkWF();
		schema.writeToFile(f.value);
	}

}
// 右键点击图元,弹出设置属性对话框
function setIconProperty() {
	// if(wftype!=""&&versionInfo!="")
	{
		var url = "state/statemaint.jsp"; 
		window.showModalDialog(
						url,
						curIcon,
						"dialogWidth:450px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;");
		// 更改图元下方的文字
		curIcon.text.value = curIcon.activity
				.selectSingleNode("propertyList/property[@name='ActivityName']/value").text;
		curIcon.displayName = curIcon.text.value;
		// curIcon.img.src = graphic_icon_path + "subflow.gif";
		curIcon.moveText();
	}
	// else
	{
		// alert('请先双击设置流程属性！');
		// return false;

	}

}
// 双击画图面板弹出流程属性对话框
function setProcessProperty() {
	if (curLine != null) {
		curLine.points.push(new Point(event.x, event.y));
		curLine.redraw();
		curLine = null;
		return false;
	} else {
		var url = "process/processmaint.jsp";
		var v_process_node = schema.schema
				.selectSingleNode("autoGraphics/graphic/states/state[@typeName='Process']");
		var similarVersion = window
				.showModalDialog(
					url,
					v_process_node,
					"dialogWidth:400px;dialogHeight:276px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;");
	}
}
// 设置连线属性
function setLineProperty() {
	var url = "event/eventmaint.jsp";
 
 	window.showModalDialog(
						url,
						curLine,
						"dialogWidth:450px;dialogHeight:380px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;");
	
 	curLine.setDisplayName(
					curLine.connector
							.selectSingleNode("propertyList/property[@name='lineCode']/value").text,
					curLine.connector
							.selectSingleNode("propertyList/property[@name='lineName']/value").text);
	curLine.drawText();
}
// 根据当前线的属性获得线的起始图元对应的环节的页面模板
function getPrevIconIdByCurLine() { 
	if (curLine != null) { 
		var v_fromIcon = curLine.connector.selectSingleNode("from").text; 
		var v_iconIdArray = v_fromIcon.split(".");
		var v_iconId = v_iconIdArray[v_iconIdArray.length - 1];
		// 环节的activityID
		var v_activityId = schema.schema
				.selectSingleNode("autoGraphics/graphic/states/state[@id='"
						+ v_iconId
						+ "']/propertyList/property[@name='ActivityCode']/value").text;

		return v_activityId ;
	} else {
		return null;
	}
}
		
		</script>
	</body>


</html>