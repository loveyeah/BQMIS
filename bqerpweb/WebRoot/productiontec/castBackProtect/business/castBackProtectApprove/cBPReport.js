Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
	var status = getParameter("status");
	var applyId = getParameter("applyId");
	var applyCode = getParameter("applyCode");
	var workflowNo = getParameter("workflowNo");


	// head工具栏
	var headTbar = new Ext.Toolbar({
				region : 'north',
				items : [{
							id : 'backFillBtu',
							text : "执行信息回填",
							iconCls : 'save',
							handler : backfill
						}, {
							id : 'returnBtu',
							iconCls : 'home',
							text : '返回',
							handler : function() {
								window.location.replace("cBPApproveList.jsp");
							}
						}]
			});

	// 执行回填
	function backfill() {
			var args = new Object();
			args.applyId = applyId;
			args.status =  status;
			args.workflowNo = workflowNo;
			var result = window
					.showModalDialog(
							'cBPBackfill.jsp',
							 args,
							'dialogWidth=600px;dialogHeight=530px;center=yes;help=no;resizable=no;status=no;');
	
	}

	var url = "/powerrpt/report/webfile/bqmis/castBackProtect.jsp?applyId="
			+ applyId+"&applyCode="+ applyCode;
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [{
					region : "north",
					layout : 'fit',
					height : 30,
					border : false,
					split : true,
					margins : '0 0 0 0',
					items : [headTbar]
				}, {
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

})