<%@ page contentType="text/html; charset=GB2312" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <script>
        function checkNumber() {
            var o = document.getElementById("id");
            var v = new RegExp(/^[1-9]{1}[\d]*$/);
            if(!v.test(o.value)){
               alert('请输入正整数');
            }  else{
                document.forms[0].submit();
            }
        }
    </script>
</head> 
<body>

<a href="default.jsp">首页</a> |
<a href="list.jsp?un=<%=request.getParameter("un")%>&workflowType=<%=request.getParameter("workflowType")%>">工作流列表</a> |
<a href="newdoc.jsp?un=<%=request.getParameter("un")%>&workflowType=<%=request.getParameter("workflowType")%>">创建新的工作流</a> 
<hr>

<form action="test.jsp" target="_blank">
    查看工作流 #：
    <input type="hidden" name="un" value="<%=request.getParameter("un")%>"/>
    <input type="hidden" name="workflowType" value="<%=request.getParameter("workflowType")%>"/>
    <input type="text" name="id" size="5" class="inputText">
    <input type="button" value="查看工作流" class="btn" onclick="checkNumber();">
</form>
</body>
</html>
