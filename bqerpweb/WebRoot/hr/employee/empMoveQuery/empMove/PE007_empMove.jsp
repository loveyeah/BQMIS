<%@ page language="java" pageEncoding="utf-8"%>
<html>
    <head>
        <title>员工调动查询</title>
        <jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include>            
    </head>
    <body>
        <script type="text/javascript" src="comm/scripts/Constants.js"></script>
        <script type="text/javascript" src="comm/scripts/hr/hr-common.js"></script>
        <script type="text/javascript" src="comm/scripts/Powererp-extend.js"></script>
        <script type="text/javascript" src="comm/datepicker/WdatePicker.js"></script>
  
        <script type="text/javascript"
			src="hr/employee/empmovedeclare/PE003.js"></script>
        <Iframe name="blankFrame" src="" border="0px"
            style="visibility: hidden;" width="0px" height="0px"></IFrame>
            
     <div id='mygrid' ></div>
	<script language="javascript">
	    var hiddenBtn = "2";
	</script>
    </body>
</html>