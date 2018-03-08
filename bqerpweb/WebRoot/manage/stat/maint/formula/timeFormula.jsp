<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title> </title> 
	 <jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include> 
	 <script type="text/javascript" src="manage/item_comm.js"></script>  
	 <script type="text/javascript" src="manage/stat/maint/formula/timeFormula.js"></script>  
	</head>
<body>  
<script language="javascript">
	var args = window.dialogArguments;
			var itemCode = args ? args.itemCode : 'lsyy1';
			var deriveDataType = args ? args.deriveDataType : '3';
 function setTitle(deriveDataType){
 	if(deriveDataType=='2'){
 
 	document.title="表值指标公式";
 	}
 	else 	
 	document.title="时间段指标公式";
 }
 setTitle(deriveDataType);

 

</script>

</body>
</html>