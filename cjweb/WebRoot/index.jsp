<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head> 
<title>漕泾热电厂管理信息系统</title>
<link rel="shortcut icon"  href="comm/images/favicon.ico" type="image/x-icon" />
<link href="comm/style/login.css" rel="stylesheet" type="text/css" />  
<script src="comm/scripts/AC_RunActiveContent.js" type="text/javascript"></script>
<script type="text/javascript" src="comm/ext/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="comm/ext/ext-all.js"></script> 
<script type="text/javascript" src="comm/ext/urlparams.js"></script>

 <script type='text/javascript' src='comm/datepicker/WdatePicker.js'></script>
<script type="text/javascript" src="index.js"></script> 
	<script type="text/javascript">    
	document.onkeydown=function() {   
          if (event.keyCode==13) {  
         		 document.getElementById('btnLogin').click();
   			}
    } 
    function focusText(){
    	document.getElementById('un').focus();
    }
    
    	</script>
<body scroll="no" onload="focusText()">
<table id="flash" border="0">
<tr>
<td>
	 <img src="comm/images/powercj.jpg" width="660" height="350" />
</td>
</tr>
</table>
<div id="login_line"> 
 <form id="loginForm" action="system/checkUserLoginRight.action"  method="post">
<table border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td height="32" align="left"><img src="comm/images/login/user.gif" width="44" height="12" /></td>
    <td colspan="2" align="left"><table width="152" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="4" height="21" background="comm/images/login/input_bg.gif"><img 

src="comm/images/login/input_bg_1.gif" width="4" height="21" /></td>
        <td width="144" background="comm/images/login/input_bg.gif"><input id="un" name="user.workerCode" 

type="text" size="25" /></td>
        <td width="4" background="comm/images/login/input_bg.gif"><img src="comm/images/login/input_bg_2.gif" 

width="4" height="21" /></td>
      </tr>
    </table></td>
  </tr>
  <tr> 
    <td height="32" align="left"><img  src="comm/images/login/password.gif" width="44" height="12" /></td>
    <td align="left"><table width="80" border="0" cellpadding="0" cellspacing="0"> 
      <tr>
        <td width="4" height="21" background="comm/images/login/input_bg.gif"><img 

src="comm/images/login/input_bg_3.gif" width="4" height="21" /></td>
        <td width="72" background="comm/images/login/input_bg.gif"><input name="user.loginPwd" type="password" 

size="15" /></td>
        <td width="4" background="comm/images/login/input_bg.gif"><img src="comm/images/login/input_bg_4.gif" 

width="4" height="21" /></td>
      </tr>
    </table></td>
    <td align="right"><img id="btnLogin" style="cursor:hand;" src="comm/images/login/login.gif"  
onclick="return checkFormData();" width="50" height="18" border="0" align="absmiddle"/>
<label id="lblErrorMsg" style="COLOR:red;nowrap"></label>
</td>
  </tr>
</table>
 <input style="display:none"  name="user.enterpriseCode" value="hfdc"/>
</form>
</div>
<div id="bottom"><table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
  	<td style="filter:progid:DXImageTransform.Microsoft.DropShadow(color=#ffffff,offX=1,offY=1,positives=1);">
  		<a href="setup/pdf/setup.exe">报表阅读器下载</a>
  	</td>
  </tr>
  <tr>
    <td style="filter:progid:DXImageTransform.Microsoft.DropShadow(color=#ffffff,offX=1,offY=1,positives=1);">技术支持：安徽科大恒星电子商务技术有限公司</td>
  </tr>
</table>
</div>
</body>
</html>