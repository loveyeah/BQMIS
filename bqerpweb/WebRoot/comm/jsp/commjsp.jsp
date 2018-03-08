<%@page import="power.ear.comm.Employee"%> 
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Employee employee =(Employee) session.getAttribute("employee");
	String style = (employee==null?"xtheme-gray":(employee.getStyle()==null||"".equals(employee.getStyle())?"xtheme-gray":employee.getStyle()));
%>
<base href="<%=basePath%>">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">

<link rel="stylesheet" type="text/css" href="comm/ext/resources/css/ext-all.css" /> 
<link rel="stylesheet" type="text/css" href="comm/ext/button.css" />
<link rel="stylesheet" type="text/css" href="comm/ext/file-upload.css" />
<link href="comm/ext/resources/css/<%=style%>.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="comm/ext/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="comm/ext/ext-all-debug.js"></script>
<script type="text/javascript" src="comm/ext/ext-lang-zh_CN.js"></script>
<script type="text/javascript" src="comm/ext/FileUploadField.js"></script>
<script language="javascript">
   var ocss_grid_input_col = {};
   
   ocss_grid_input_col["xtheme-gray"] = "background:#F5DEB3;";
   ocss_grid_input_col["xtheme-black"] = "background:#F5DEB3;";
   ocss_grid_input_col["xtheme-darkgray"] = "background:#F5DEB3;";
   ocss_grid_input_col["xtheme-galdaka"] = "background:#B0E0E6;";
   ocss_grid_input_col["xtheme-purple"] = "background:#FFB6C1;";
   ocss_grid_input_col["xtheme-slate"] = "background:#87CEFA; display:block;";

   CSS_GRID_INPUT_COL = ocss_grid_input_col["<%=style%>"];
   CSS_GRID_NOT_INPUT_COL = "background:gray;color: white !important;";
   
 
   Ext.grid.GridPanel.prototype.enableHdMenu = false;
   Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';  
   //Backspace
   document.onkeydown = function() { 
		if ((event.keyCode == 8)&& (event.srcElement.type != "text") && (event.srcElement.type != "textarea") && (event.srcElement.type != "password") ||(event.srcElement.readOnly == true)) {
			event.cancelBubble = true
			event.returnValue = false;
			return false;
		}
	}    
   
</script>
<style>
.disable {
   	   background: #aaaaaa;color:blank;border:1px solid #777777; important!
   }
</style>