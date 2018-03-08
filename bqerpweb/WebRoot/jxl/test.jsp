<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		<title>列表</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
	</head>

	<body>
	<form  method="post" action="comm/test.action">
		<table border="1" width="100%">
			<tr>
				<td colspan="2" align="center"> 
					5excel报表管理测试 
				</td>
			</tr>
			<tr>
				<td width="30%">
					 模块编号：
				</td>
				<td style="width:560;"> 
					 <input type="text" id="template" name="template" value="shengchangyuebao"/>
				</td>
			</tr>
			<tr>
				<td width="30%">
					 报表标题：
				</td>
				<td style="width:560;"> 
					 <input type="text" id="title" name="title"/>
				</td>
			</tr> 
			<tr>
				<td>
					 制表人：
				</td>
				<td>
					 <input type="text" id="workerCode" name="workerCode"/>
				</td>
			</tr> 
			<tr>
				<td>
					 制表日期：
				</td>
				<td>
					 <input  style="width:560;" type="text" value="2009-08-03" id="date" name="date"/>
				</td>
			</tr> 
			<tr>
				<td>
					 隐藏列：
				</td>
				<td>
					 <input  style="width:560;" type="text" id="hiddenCols" name="hiddenCols"/>
				</td>
			</tr> 
			<tr>
				<td>
					 列表条件1：$(strWhere)
				</td>
				<td>
					 <textarea  style="width:560;" id="strWhere" name="strWhere" > rownum <20 and t.data_value <> 0</textarea>
				</td>
			</tr>
			<tr>
				<td>
					 列表条件2：$(strWhere2)
				</td>
				<td>
					 <input type="text"  style="width:560;" id="strWhere2" name="strWhere2" value=""/>
				</td>
			</tr> 
			<tr>
				<td>
					 列表条件3：$(strWhere3)
				</td>
				<td>
					 <input type="text"  style="width:560;" id="strWhere3" name="strWhere3" value=""/>
				</td>
			</tr> 
			<tr>
				<td  colspan="2" align="center">
					  <input type="submit" value="提   交" />
				</td>
				 
			</tr> 
		</table>
		</form>
		<script>
			var args = window.dialogArguments;
			if(args!= null)
			{
				document.getElementById("template").value = args.code;
				document.getElementById("template").ReadOnly = true;
			}
			
		</script>
	</body>
</html>
