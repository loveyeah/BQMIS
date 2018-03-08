// var flowCode = 'workticketdh';
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
 
Ext.onReady(function() {
	var flowCode = getParameter("flowCode");
	if (flowCode != '') {
		xmlloadWF(flowCode);
	}
	function xmlloadWF(flowCode) {
		Ext.Ajax.request({
			url : 'MAINTWorkflow.do',
			method : 'post',
			params : {
				action : 'loadCurrentVersion',
				flowCode : flowCode
			},
			success : function(result, request) {
				var xml = result.responseText;
				if (xml != null && xml != "") {
					schema.loadByXmlData(xml);
				}
			}
		});
	}
});
function showMenu(id) {
	event.returnValue = false;
	event.cancelBubble = true;
	return false;
}
// });
