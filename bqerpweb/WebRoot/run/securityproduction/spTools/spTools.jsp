<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<html>
	<head>
		<title>电动工具和电气安全用具清册</title>
		<jsp:include page="../../../comm/jsp/commjsp.jsp"></jsp:include>
		<script type="text/javascript" src="comm/scripts/Constants.js"></script>
		<script type="text/javascript" src="comm/datepicker/WdatePicker.js" defer="defer"></script>
		<script type="text/javascript" src="run/securityproduction/spTools/spTools.js"></script>
	</head>
	<body>
		<script type="text/javascript">
			Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
			Ext.onReady(function(){
				var tool = new Tool(null,false,null,null,1)
				tool.init()
				new Ext.Viewport({
					id : 'viewPort',
					layout : 'fit',
					items : [tool.grid]
				})
			})
		</script>
	</body>
</html>