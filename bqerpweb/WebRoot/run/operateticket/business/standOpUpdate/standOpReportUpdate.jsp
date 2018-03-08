<%@ page language="java" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <title>标准操作票修改列表</title>
        <jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include>
        <script type="text/javascript" src="comm/scripts/Constants.js"></script>        
        <script type="text/javascript" src="comm/datepicker/WdatePicker.js" defer="defer"></script>
        <script type="text/javascript" src="comm/ext/ComboBoxTree.js"></script>
         <script type="text/javascript" src="run/operateticket/opticket_comm.js"></script>
        <script type="text/javascript" src="run/operateticket/business/standOpUpdate/standOpReportUpdate.js"></script>
    </head>
    <body>  
    <div id="loading-mask" style=""></div>
		<div id="loading">
			<div class="loading-indicator">
				<img src="comm/images/extanim32.gif" width="32" height="32"
					style="margin-right: 8px;" align="absmiddle" />
				Loading...
			</div>
		</div>	      
    </body>
</html>