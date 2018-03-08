<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="power.ear.comm.Employee"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>漕泾电厂CRM系统</title>  
		<jsp:include page="../../../comm/jsp/commjsp.jsp"></jsp:include>
		<script src="comm/scripts/AC_RunActiveContent.js"
			type="text/javascript"></script>
		<script type="text/javascript" src="comm/jquery/jquery.js"></script>
		<script type="text/javascript" src="comm/jquery/interface.js"></script>
		<link rel="stylesheet" href="comm/jquery/style.css" type="text/css"></link>
		<!--[if lt IE 7]>
		 <style type="text/css">
		 div, img { behavior: url(comm/jquery/iepngfix.htc) } 
		 </style>
		<![endif]-->
		<style type="text/css">
		#west-title {  
			color:red !important;
		}
		</style>
		<script type="text/javascript" src="system/business/main/main.js"></script> 
		<link rel="shortcut icon"  href="/power/comm/images/favicon.ico" type="image/x-icon" />
	</head>
	<body
		style="margin: 0; background-color: #fff; background: url(comm/images/login/index_topbg.gif); background-repeat: repeat-x; margin: 0px; font-family: Verdana, Arial, Helvetica, sans-serif;">
		<DIV id="north-div" style="display:none">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="23%" background="comm/images/login/logo_bg.jpg">
						<script type="text/javascript">
			AC_FL_RunContent( 'codebase','http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,19,0','width','298','height','98','src','comm/images/swf/logo','quality','high','pluginspage','http://www.macromedia.com/go/getflashplayer','wmode','transparent','movie','comm/images/swf/logo' ); //end AC code
			</script>
						<noscript>
							<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
								codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,19,0"
								width="298" height="98">
								<param name="movie" value="comm/images/swf/logo.swf" />
								<param name="quality" value="high" />
								<param name="wmode" value="transparent" />
								<embed src="comm/images/swf/logo.swf" width="298" height="98"
									quality="high"
									pluginspage="http://www.macromedia.com/go/getflashplayer"
									type="application/x-shockwave-flash" wmode="transparent"></embed>
							</object>
						</noscript>
					<br></td>
					<td width="27%" align="left" valign="top"
						style="padding: 15px 0 0 0; white-space: nowrap;">
						<div>
							<div class="dock" id="dock">
								<div class="dock-container">
									<a class="dock-item" href="/power" onclick="return Go (1)"><span>重新登录</span>
										<img src="comm/jquery/images/home.png" alt="重新登录" /> </a>
									<a class="dock-item" href="/power"
										onclick="return modifyPass()"><span>修改密码</span> <img
											src="comm/jquery/images/calendar.png" alt="修改密码" /> </a>
									<a class="dock-item" href="#" onClick="closeWindow();"><span>退出系统</span>
										<img src="comm/jquery/images/rss.png" alt="退出系统" /> </a>
								</div>
							</div>
						</div>
					<br></td>
					<td valign="top" width="*">&nbsp;<!-- 此处可以加很多内容 -->

						<table width="100%" border="0" cellspacing="0" cellpadding="0"
							style="font-size: 9pt; color: #5E5E5E; filter: progid :             DXImageTransform .             Microsoft .             DropShadow(color =             #ffffff, offX =             1, offY =             1, positives =             1)">
							<tr>
								<td height="45" align="right">
									<img src="comm/images/datetime.gif" width="16" height="14"
										align="absmiddle" />
									今天是：
									<SCRIPT LANGUAGE=JavaScript TYPE=text/JavaScript> 
									today=new Date(); 
									function initArray(){ 
									this.length=initArray.arguments.length 
									for(var i=0;i<this.length;i++) 
									this[i+1]=initArray.arguments[i] } 
									var d=new initArray( 
									"星期日", 
									"星期一", 
									"星期二", 
									"星期三", 
									"星期四", 
									"星期五", 
									"星期六"); 
									document.write( 
									today.getYear(),"年", 
									today.getMonth()+1,"月", 
									today.getDate(),"日 ", 
									d[today.getDay()+1], 
									"" ); 
									</SCRIPT>
								</td>
							</tr>
							<tr>
								<td align="right">
									<%
										Employee employee = (Employee) session.getAttribute("employee");
										out.print("【" + employee.getWorkerName() + "】:欢迎您!");
									%>
								</td>
							</tr>
						</table>

					</td>
				</tr>
			</table>
		</DIV>

		<div id="div-buttons"></div>
		<div id="loading-mask"></div>
		<div id="loading">
			<div class="loading-indicator">
				<img src="comm/images/extanim32.gif" width="32" height="32"
					style="margin-right: 8px;" />
				加载中...
			</div>
		</div>
	</body> 
</html>
