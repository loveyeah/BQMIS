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
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">

