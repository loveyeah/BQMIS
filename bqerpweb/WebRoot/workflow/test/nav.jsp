<%@ page contentType="text/html; charset=GB2312" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <script>
        function checkNumber() {
            var o = document.getElementById("id");
            var v = new RegExp(/^[1-9]{1}[\d]*$/);
            if(!v.test(o.value)){
               alert('������������');
            }  else{
                document.forms[0].submit();
            }
        }
    </script>
</head> 
<body>

<a href="default.jsp">��ҳ</a> |
<a href="list.jsp?un=<%=request.getParameter("un")%>&workflowType=<%=request.getParameter("workflowType")%>">�������б�</a> |
<a href="newdoc.jsp?un=<%=request.getParameter("un")%>&workflowType=<%=request.getParameter("workflowType")%>">�����µĹ�����</a> 
<hr>

<form action="test.jsp" target="_blank">
    �鿴������ #��
    <input type="hidden" name="un" value="<%=request.getParameter("un")%>"/>
    <input type="hidden" name="workflowType" value="<%=request.getParameter("workflowType")%>"/>
    <input type="text" name="id" size="5" class="inputText">
    <input type="button" value="�鿴������" class="btn" onclick="checkNumber();">
</form>
</body>
</html>
