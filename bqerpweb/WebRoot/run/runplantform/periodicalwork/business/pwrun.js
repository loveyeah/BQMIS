Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	Ext.QuickTips.init();

	// 总tabpanel
	var tabpanel = new Ext.TabPanel({
		renderTo : document.body,
		activeTab : 0,
		tabPosition : 'bottom',
		plain : true,
		defaults : {
			autoScroll : true
		},
		frame : false,
		border : false,
		items : [{
			title : '运行执行',
			html : "<iframe id='run' src='run/runplantform/periodicalwork/business/pwrunall.jsp' style='width:100%;height:100%;border:0px;'></iframe>",
			listeners : {
				activate : function() {
					document.getElementById("run").src = "run/runplantform/periodicalwork/business/pwrunall.jsp";
				}
			}
		},

				// {
				// title : '值长审批',
				// html : "<iframe id='chk'
				// src='run/runplantform/periodicalwork/business/pwrunchk.jsp'
				// style='width:100%;height:100%;border:0px;'></iframe>",
				// listeners : {
				// activate : function() {
				// document.getElementById("chk").src =
				// "run/runplantform/periodicalwork/business/pwrunchk.jsp";
				// }
				// }
				// }, {
				// title : '延期执行',
				// html : "<iframe id='delay'
				// src='run/runplantform/periodicalwork/business/pwrundelay.jsp'
				// style='width:100%;height:100%;border:0px;'></iframe>",
				// listeners : {
				// activate : function() {
				// document.getElementById("delay").src =
				// "run/runplantform/periodicalwork/business/pwrundelay.jsp";
				// }
				// }
				// },
				{
					// title : '专业查询',
					title : '查询',
					html : "<iframe id='pro' src='run/runplantform/periodicalwork/business/pwrunpro.jsp' style='width:100%;height:100%;border:0px;'></iframe>",
					listeners : {
						activate : function() {
							document.getElementById("pro").src = "run/runplantform/periodicalwork/business/pwrunpro.jsp";
						}
					}
				}
		// , {
		// title : '运行确认',
		// html : "<iframe id='con'
		// src='run/runplantform/periodicalwork/business/pwruncon.jsp'
		// style='width:100%;height:100%;border:0px;'></iframe>",
		// listeners : {
		// activate : function() {
		// document.getElementById("con").src =
		// "run/runplantform/periodicalwork/business/pwruncon.jsp";
		// }
		// }
		// }
		]
	});

	function handleActivate(tab) {
		alert(tab.title + ' was activated.');
	}

	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							title : "",
							region : 'center',
							layout : 'fit',
							border : false,
							margins : '0 0 0 0',
							split : true,
							collapsible : false,
							items : [tabpanel]
						}]
			});
});