<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<html>
	<head>
		<title>电气安全用具检修记录</title>
		<jsp:include page="../../../comm/jsp/commjsp.jsp"></jsp:include>
		<script type="text/javascript" src="comm/scripts/Constants.js"></script>
		<script type="text/javascript" src="comm/datepicker/WdatePicker.js" defer="defer"></script>
		<script type="text/javascript" src="comm/component/dept/dept.js" defer="defer"></script>
		<script type="text/javascript" src="comm/component/person/person.js" defer="defer"></script>
		<script type="text/javascript" src="run/securityproduction/spTools/spTools.js"></script>
		<script type="text/javascript" src="run/securityproduction/spRepair/spRepair.js"></script>
	</head>
	<body>
		<script type="text/javascript">
			Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
			Ext.onReady(function(){
				var repair = new SpRepair(false,1)
				repair.init()
				new Ext.Viewport({
					id : 'viewPort',
					layout : 'fit',
					items : [repair.grid]
				})
			})
		</script>
	</body>
</html>