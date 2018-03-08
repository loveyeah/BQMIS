<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<html>
	<head>
		<title>电动工器具和电焊机检验记录</title>
		<jsp:include page="../../../comm/jsp/commjsp.jsp"></jsp:include>
		<script type="text/javascript" src="comm/scripts/Constants.js"></script>
		<script type="text/javascript" src="comm/datepicker/WdatePicker.js"
			defer="defer"></script>
		<script type="text/javascript" src="comm/component/dept/dept.js"
			defer="defer"></script>
		<script type="text/javascript" src="comm/component/person/person.js"
			defer="defer"></script>
		<script type="text/javascript"
			src="run/securityproduction/spTools/spTools.js"></script>
		<script type="text/javascript"
			src="run/securityproduction/spWeldingRepair/spWelding.js"></script>
	</head>
	<body>
		<script type="text/javascript">
			Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
			Ext.onReady(function(){
				var welding = new SpWelding(false,2)
				welding.init()
				new Ext.Viewport({
					id : 'viewPort',
					layout : 'fit',
					items : [welding.grid]
				})
			})
		</script>
	</body>
</html>