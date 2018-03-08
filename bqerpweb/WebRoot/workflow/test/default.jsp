<%@ page contentType="text/html; charset=utf-8"%>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="../css/style.css">
		<title>工作流功能测试</title>
		<script>
        function checkNumber() {
            var o = document.getElementById("u");
            var v = new RegExp(/^[1-9]{1}[\d]*$/);
            //if(!v.test(o.value)){
              //alert('请输入正整数');
            //}  else{
                document.forms[0].submit();
            //}
        }
    </script>
	</head>
	<body>
		<%
			String username = request.getParameter("u");
			if (username != null) {
				response.sendRedirect("list.jsp?un=" + username
						+ "&workflowType="
						+ request.getParameter("workflowType"));
			}
		%>

		<form method="POST" action="default.jsp">
			<table border="0">
				<tr>
					<td>
						用户ID：
					</td>
					<td>
						<input type="text" name="u" value="2" class="inputTextFixed20">
					</td>
				</tr>
				<tr>
					<td>
						密 码：
					</td>
					<td>
						<input type="password" name="p" class="inputTextFixed20">
					</td>
				</tr>
				<tr>
					<td>
						用户组：
					</td>
					<td>
						<input type="text" name="g" class="inputTextFixed20">
					</td>
				</tr>
				<tr>
					<td>
						工作流类型：
					</td>
					<td>
						<input type="text" name="workflowType" value="sun"
							class="inputTextFixed20">
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<input type="button" value="登录" onclick="checkNumber();"
							class="btn">
					</td>
				</tr>
			</table>
			<%
				out.println("request.getContextPath():" + request.getContextPath()
						+ "<br/>");
				out.println("request.getPathInfo():" + request.getPathInfo()
						+ "<br/>");
				out.println("request.getPathTranslated():"
						+ request.getPathTranslated() + "<br/>");
				out.println("request.getLocalAddr():" + request.getLocalAddr()
						+ "<br/>");
				out.println("request.getServletPath():" + request.getServletPath()
						+ "<br/>");
				out.println("request.getServerName():" + request.getServerName()
						+ "<br/>");
				out.println("request.getServerPort():" + request.getServerPort()
						+ "<br/>");
				out.println(" request.getHeader('User-Agent'):"
						+ request.getHeader("User-Agent") + "<br/>");
			%>
		</form>
	</body>
</html>
