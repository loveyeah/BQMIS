Ext.onReady(function() {
	var conId = getParameter("id");
	var url = "/powerrpt/report/webfile/bqmis/Proxyreport.jsp?conId=" + conId;
	// var tpl = new Ext.Template("<font color ='red'>请选择查看的报表 !</font>");
	// function query() {
	// Ext.Ajax.request({
	// url : 'managecontract/getProxyInfo.action',
	// params : {
	// conId : conId
	// },
	// method : 'post',
	// success : function(result, request) {
	// var str = result.responseText;
	// var tpl = new Ext.Template(str)
	// tpl.overwrite(panel.body, tpl);
	// },
	// failure : function(result, request){
	// var str = "<font color='red'> 数据出错！</font>";
	// var tpl = new Ext.Template(str)
	// tpl.overwrite(panel.body,tpl);
	// }
	// })
	// }
	// query();
	// var panel = new Ext.Panel({
	// id : 'panel',
	// autoScroll : true,
	// spilt : true,
	// autoHeight : false,
	// autoWidth : false,
	// border : false,
	// layout : 'fit',
	// bodyStyle : "padding: 2,2,2,2",
	// items : [tpl]
	// })
	var view = new Ext.Viewport({
				layout : 'fit',
				margins : '0 0 0 0',
				collapsible : true,
				split : true,
				border : false,
				items : [{
					region : "center",
					layout : 'fit',
					border : true,
					collapsible : true,
					split : true,
					margins : '0 0 0 0',
					html : '<iframe id="iframe1" name="iframe1"  src="'
							+ url
							+ '"  frameborder="0"  width="100%" height="100%"  />'
				}]
			});
});
