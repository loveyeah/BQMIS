Ext.onReady(function() { 
	var scrheight = document.body.clientHeight;
	function DateAdd(datestring, value) {
		var a = new Date(datestring);
		a = a.valueOf();
		a = a + value * 24 * 60 * 60 * 1000;
		a = new Date(a);
		var olddate = a.getFullYear() + "-" + (a.getMonth() + 1) + "-"
				+ a.getDate();
		var newdate = olddate.split('-');
		if (newdate[1].length == 1)
			newdate[1] = '0' + newdate[1];
		if (newdate[2].length == 1)
			newdate[2] = '0' + newdate[2];
		var truedate = newdate[0] + '-' + newdate[1] + '-' + newdate[2];
		return truedate;
	}

	Ext.state.Manager.setProvider(new Ext.state.CookieProvider());

	// create some portlet tools using built in Ext tool ids
	var tools = [{
		id : 'gear'
			// ,
			// handler : function() {
			// Ext.Msg.alert('Message', 'The Settings tool was clicked.');
			//		}
	}, {
		id : 'close',
		handler : function(e, target, panel) {
			panel.ownerCt.remove(panel, true);
		}
	}];

	function displayfdlflag(value) {
		// var record = grid.getStore().getAt(rowIndex); // Get the Record
		// var fieldName = grid.getColumnModel().getDataIndex(2); // Get field
		// // name
		// var data = record.get(fieldName);
		var fieldfgs = blockGrid.getColumnModel().getDataIndex(6);
		var fieldName = blockGrid.getColumnModel().getDataIndex(2); // Get
		// fieldname
		var records = blockGrid.getStore();
		var maxval = 0;
		for (var i = 0; i < itemStore.getRange().length; i++) {				
			if (records.getAt(i).get(fieldfgs).substr(4,1) != "B") {
				if (records.getAt(i).get(fieldName) > maxval) {
					maxval = records.getAt(i).get(fieldName);
				}
			}

		}

		if (value == maxval && value != "") {
			return "<img src='../../ExtJS/resources/images/default/dd/flag_red.gif' ></img>"
					+ value;
		} else {
			return value;
		}
	}
	function displaygdmhflag(value) {
		var fieldfgs = blockGrid.getColumnModel().getDataIndex(6);
		var fieldName = blockGrid.getColumnModel().getDataIndex(3); // Get
		// fieldname
		var records = blockGrid.getStore();
		var maxval = 0;
		for (var i = 0; i < itemStore.getRange().length; i++) {
			if (records.getAt(i).get(fieldfgs).substr(4,1) != "B") {
				if (records.getAt(i).get(fieldName) > maxval) {
					maxval = records.getAt(i).get(fieldName);
				}
			}

		}

		if (value == maxval && value != "") {
			return "<img src='../../ExtJS/resources/images/default/dd/flag_red.gif' ></img>"
					+ value;
		} else {
			return value;
		}
	}
	function displaycydlflag(value) {
		var fieldfgs = blockGrid.getColumnModel().getDataIndex(6);
		var fieldName = blockGrid.getColumnModel().getDataIndex(4); // Get
		// fieldname
		var records = blockGrid.getStore();
		var maxval = 0;
		for (var i = 0; i < itemStore.getRange().length; i++) {
			if (records.getAt(i).get(fieldfgs).substr(4,1) != "B") {
				if (records.getAt(i).get(fieldName) > maxval) {
					maxval = records.getAt(i).get(fieldName);
				}
			}

		}

		if (value == maxval && value != "") {
			return "<img src='../../ExtJS/resources/images/default/dd/flag_red.gif' ></img>"
					+ value;
		} else {
			return value;
		}
	}
	function displayzhcydlflag(value) {
		var fieldfgs = blockGrid.getColumnModel().getDataIndex(6);
		var fieldName = blockGrid.getColumnModel().getDataIndex(5); // Get
		// fieldname
		var records = blockGrid.getStore();
		var maxval = 0;
		for (var i = 0; i < itemStore.getRange().length; i++) {
			if (records.getAt(i).get(fieldfgs).substr(4,1) != "B") {
				if (records.getAt(i).get(fieldName) > maxval) {
					maxval = records.getAt(i).get(fieldName);
				}
			}

		}

		if (value == maxval && value != "") {
			return "<img src='../../ExtJS/resources/images/default/dd/flag_red.gif' ></img>"
					+ value;
		} else {
			return value;
		}
	}
	function displayfgs(value) {
		if (value.match("公司")) {
			return "<img src='../../ExtJS/resources/images/default/dd/logo.gif' ></img>"
					+ value;
		} else {
			return value;
		}

	}
	// 给昨天付初始值
	var yesterday = DateAdd(new Date().valueOf(), -1);
	var itemStore = new Ext.data.SimpleStore({
		proxy : new Ext.data.HttpProxy({
			url : ''//'getEconomyItemDay.action?date=' + yesterday
		}),
		fields : [{
			name : 'markdeptname'
		}, {
			name : 'markoutvalue',
			type : 'float'
		}, {
			id : "markinvalue",
			name : 'markinvalue',
			type : 'float'
		}, {
			name : 'markper',
			type : 'float'
		}, {
			name : 'markallper',
			type : 'float'
		}, {
			name : 'markalias'
		}, {
			name : 'markplantcode'
		}]
	});
	//itemStore.load();

	var item_cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
		header : '单 位',
		dataIndex : 'markalias',
		align : 'left',
		renderer : displayfgs
	}, {
		header : '发电量',
		dataIndex : 'markoutvalue',
		renderer : displayfdlflag,
		align : 'right'
	}, {
		header : '供电煤耗',
		dataIndex : 'markinvalue',
		renderer : displaygdmhflag,
		align : 'right'
	}, {
		header : '厂用电率%',
		dataIndex : 'markper',
		renderer : displaycydlflag,
		align : 'right'
	}, {
		header : '综合厂用电率%',
		dataIndex : 'markallper',
		renderer : displayzhcydlflag,
		align : 'right'
	}, {
		dataIndex : 'markplantcode',
		hidden : true
	}]);
	item_cm.defaultSortable = true;

	//

	//
	var blockGrid = new Ext.grid.GridPanel({
		// el : 'shift',
		store : itemStore,
		cm : item_cm,
		resizeable : true,
		viewConfig : {
			forceFit : true
		},
		split : true,
		listeners : {
			"rowdblclick" : function(_grid, _rowindex, _e) {
				var parentctl = Ext.getCmp("graphline");
				var seldept = _grid.view.getCell(_rowindex, 1).innerText;
				var plantcode = _grid.view.getCell(_rowindex, 6).innerText;
				parentctl.setTitle(seldept + "-发电量曲线");
				displaygraphline(plantcode);
			}
		}
	});

//	function displaygraphline(plantcode) {
//		//
//		var so = new SWFObject("comm/amchart/amline/amline.swf", "amline", "100%", "100%",
//				"8", "#FFFFFF");
//		so.addVariable("path", "comm/amchart/amline/");
//		so.addVariable("settings_file", escape("comm/amchart/amline/amline_settings.xml"));
//		so.addVariable("data_file", escape("system/getElectricLine.action"));
//		so.addVariable("preloader_color", "#999999");
//		so.write("flashcontent");
//	}
//	//显示柱状图
//	function displaygraphcolumn() {
//		//
//		var so = new SWFObject("comm/amchart/amcolumn/amcolumn.swf", "amline", "100%", "100%",
//				"8", "#FFFFFF");
//		so.addVariable("path", "comm/amchart/amcolumn/");
//		so.addVariable("settings_file", escape("comm/amchart/amcolumn/amcolumn_settings.xml"));
//		so.addVariable("data_file", escape("system/getElectricColumnOnUnit.action"));
//		so.addVariable("preloader_color", "#999999");
//		so.write("flashColumn");
//	}
//	//显示饼状图
//	function displaygraphpie() {
//		//
//		var so = new SWFObject("comm/amchart/ampie/ampie.swf", "amline", "100%", "100%",
//				"8", "#FFFFFF");
//		so.addVariable("path", "comm/amchart/ampie/");
//		so.addVariable("settings_file", escape("comm/amchart/ampie/ampie_settings.xml"));
//		so.addVariable("data_file", escape("system/getElectricPieOnUnit.action"));
//		so.addVariable("preloader_color", "#999999");
//		so.write("flashPie");
//	}
	var viewport = new Ext.Viewport({
		layout : 'border',
		items : [{
			xtype : 'portal',
			region : 'center',
			margins : '0 0 0 0',
			border : false,
			items : [{
				columnWidth : .5,
				style : 'padding:5px 0px 5px 5px',
				items : [{
					id : "graphline",
					title : '发电量曲线',
					 height : (scrheight - 10) / 2,
					html : '<div id="flashcontent" style="overflow:auto;width:100%;height=100%"></div>'

				}, {
					title : '发电量年度累计值柱状图分析',
					 height : (scrheight - 10) / 2,
					layout : 'fit',
					html : '<div id="flashColumn" style="overflow:auto;width:100%;height=100%"></div>'

				}]
			}, {
				columnWidth : .5,
				style : 'padding:5px 0px 5px 5px',
				items : [{
					title : '发电量年度累计值饼图分析',
					 height : (scrheight - 10) / 2,
					layout : 'fit',
					html : '<div id="flashPie" style="overflow:auto;width:100%;height=100%"></div>'
				},{
					title : '我的事务',
					 height : (scrheight - 10) / 2,
					layout : 'fit',
					html : '<div id="myjob" style="overflow:auto;width:100%;height=100%"></div>'
				}]
			}]
		}]
//		,
//		listeners : {
//			"afterlayout" : function() {
//				displaygraphline("0551B01");
//				displaygraphcolumn();
//				displaygraphpie();
//			}
//		}
	});
    var myjboStr='';
	var conn = Ext.lib.Ajax.getConnectionObject().conn;
					conn.open("POST", "system/getMyJob.action", false);
					conn.send(null); 
					// 成功状态码为200
					if (conn.status == "200") {   
						myjboStr = conn.responseText ; 		
						Ext.get("myjob").dom.innerHTML = myjboStr;
					}
	
	Ext.get("flashcontent").dom.innerHTML += '<a href="message/maint/customer/customer.jsp">供应商维护</a><hr/>';
	Ext.get("flashcontent").dom.innerHTML += '<a href="message/maint/wordtype/wordtype.jsp">文档类型维护</a><hr/>';
	Ext.get("flashcontent").dom.innerHTML += '<a href="message/bussiness/message.jsp">发送/接收消息</a><hr/>';
	Ext.get("flashcontent").dom.innerHTML += '<a href="message/bussiness/customer_contact/customer_contact.jsp">供应商员工维护</a><hr/>';
	
});
