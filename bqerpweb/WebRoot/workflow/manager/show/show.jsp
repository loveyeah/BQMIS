<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns:v="urn:schemas-microsoft-com:vml">
<head> 
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>实例运行跟踪</title> 
	<jsp:include page="../../../comm/jsp/commjsp.jsp"></jsp:include> 
	<script language="text/javascript" type="text/javascript" src="comm/scripts/workflow/graphic.js"></script> 
    <script language="text/javascript" type="text/javascript" src="comm/scripts/workflow/wfschema.js"></script> 
	<script type="text/javascript" src="comm/ext/urlparams.js"></script>   
	<script type="text/javascript" src="workflow/manager/show/show.js"></script>  
	<style type="text/css">
        v\:* {behavior:url(#default#VML);}
    </style> 
    <script language="javascript" type="text/javascript">  
    var flowType = '';//当前操作工作流流程编码
	var schema = new WFSchema(); 
</script>
</head>
<body style="margin-left: 0px; margin-right: 0px; margin-top: 0px; margin-bottom: 0px;">  
<table cellpadding="0" cellspacing="0" align="left" border="2" style="position: absolute; height: 100%; border-right: #3399ff ridge;"> 
				<tr>
					<td id="buttonsDiv" height="100%">
					
					</td>
				</tr>
			</table>
			
	 <div id="icondiv" style="display: none">
	 <!-- 
        <table border="1" width="100%" height="100%" bgcolor="#cccccc" style="border: thin"
            cellspacing="0">
            <tr>
                <td style="cursor: default; border: outset 1; font-size: 12px;" align="center" onclick="alert('模块开发中')">
                    查看审批信息
                </td>
            </tr> 
        </table>
     -->
    </div>
   
 
    
</body>
</html>