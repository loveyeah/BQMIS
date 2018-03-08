Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";
		// 初始化时间
		Year = DateIn.getYear();
		Month = DateIn.getMonth() + 1;
		Day = DateIn.getDate();
		CurrentDate = Year + "-";
		if (Month >= 10) {
			CurrentDate = CurrentDate + Month + "-";
		} else {
			CurrentDate = CurrentDate + "0" + Month + "-";
		}
		if (Day >= 10) {
			CurrentDate = CurrentDate + Day;
		} else {
			CurrentDate = CurrentDate + "0" + Day;
		}
		return CurrentDate;
	}
	var enddate = new Date();
	var startdate = enddate.add(Date.MONTH, -1);
	startdate = startdate.getFirstDateOfMonth();
	var sdate = ChangeDateToString(startdate);
	var edate = ChangeDateToString(enddate);

	var startDate = new Ext.form.TextField({
		id : 'startDate',
		readOnly : true,
		width : 75,
		value : sdate,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%m-%d',
					dateFmt : 'yyyy-MM-dd'
				})
			}
		}
	})
	var endDate = new Ext.form.TextField({
		id : 'endDate',
		readOnly : true,
		width : 75,
		value : edate,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%m-%d',
					dateFmt : 'yyyy-MM-dd'
				})
			}
		}
	})

	var _boilerId = 0;
	var currentNode = new Object();

	// 定义树结构
	var root = new Ext.tree.AsyncTreeNode({
		id : "0",
		text : "锅炉设备树"
	});
	currentNode = root;
	var mytree = new Ext.tree.TreePanel({
		renderTo : "treepanel",
		autoHeight : true,
		root : root,
		border : false,
		loader : new Ext.tree.TreeLoader({
			url : "security/getTreeListForBoiler.action",
			baseParams : {
				id : '0'
			}
		})
	});
	// 树的单击事件
	root.expand();// 展开根节点
	mytree.on("click", treeClick, this);
	mytree.on('beforeload', function(node) {
		// 指定某个节点的子节点
		mytree.loader.dataUrl = 'security/getTreeListForBoiler.action?id='
				+ node.id;
	}, this);

	function treeClick(node, e) {
		e.stopEvent();
		_boilerId = node.id;
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		})
	};

	// -------------------- 定义grid--------------------------
	var MyRecord = Ext.data.Record.create([{
		name : 'boilerName',
		mapping : 0
	}, {
		name : 'boilerType',
		mapping : 1
	}, {
		name : 'boilerRepairId',
		mapping : 2
	}, {
		name : 'boilerId',
		mapping : 3
	}, {
		name : 'taskSource',
		mapping : 4
	}, {
		name : 'repairRecord',
		mapping : 5
	}, {
		name : 'repairTime',
		mapping : 6
	}, {
		name : 'repairBy',
		mapping : 7
	}, {
		name : 'repairByName',
		mapping : 8
	}]);

	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'security/getBoilerEquList.action',
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list'
		}, MyRecord)

	});
	store.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			boilerId : _boilerId,
			sDate : startDate.getValue(),
			eDate : endDate.getValue()
		});
	});
	store.load({
		params : {
			start : 0,
			limit : 18
		}
	});

	var sm = new Ext.grid.CheckboxSelectionModel();
	var grid = new Ext.grid.GridPanel({
		region : "center",
		ds : store,
		columns : [sm, {
			header : '设备名称',
			dataIndex : 'boilerName',
			align : 'left',
			width : 220
		}, {
			header : "型号规格",
			width : 100,
			align : 'center',
			sortable : true,
			dataIndex : 'boilerType'
		}, {
			header : "任务来源",
			width : 100,
			align : 'center',
			sortable : true,
			dataIndex : 'taskSource'
		}, {
			header : '检修记录',
			dataIndex : 'repairRecord',
			align : 'left',
			width : 220
		}, {
			header : '检修时间',
			dataIndex : 'repairTime',
			readOnly : true,
			width : 120
		}, {
			header : '检修人',
			dataIndex : 'repairByName',
			align : 'center',
			readOnly : true,
			width : 80
		}],
		sm : sm,
		stripeRows : true,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : true
		},
		autoExpandColumn : 'id',
		title : '锅炉设备检修记录',
		tbar : ["检修时间:", startDate, "~", endDate, '-', {
			text : "查询",
			iconCls : 'query',
			handler : queryRecord
		}, '-', {
			text : "导出",
			iconCls : 'export',
			handler : myExport
		}],
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录",
			beforePageText : '页',
			afterPageText : "共{0}页"
		})
	});
	// -----------------------------------------------------------------
	// 查询
	function queryRecord() {
		if (_boilerId == null || _boilerId == "") {
			Ext.Msg.alert("提示", "请选择左边的树节点!");
			return false;
		}
		store.baseParams = {
			boilerId : _boilerId,
			sDate : startDate.getValue(),
			eDate : endDate.getValue()
		};
		store.load({
			start : 0,
			limit : 18
		})
	}

	// 导出
	function tableToExcel(tableHTML) {
		window.clipboardData.setData("Text", tableHTML);
		try {
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExApp.DisplayAlerts = false;
			ExApp.visible = true;
		} catch (e) {
			if (e.number != -2146827859)
				alert("您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}

	function myExport() {
		if (store.getCount() <= 0) {
			Ext.Msg.alert("提示", "没有数据！");
			return false;
		} else {
			Ext.Ajax.request({
				url : 'security/getBoilerEquList.action',
				params : {
					boilerId : _boilerId,
					sDate : startDate.getValue(),
					eDate : endDate.getValue()
				},
				success : function(response) {
					var json = eval('(' + response.responseText.trim() + ')');
					var records = json.list;
					// alert(records.length);
					var html = ['<table border=1><tr><th>设备名称</th><th>型号规格</th><th>任务来源</th><th>检修记录</th><th>检修时间</th><th>检修人</th></tr>'];
					for (var i = 0; i < records.length; i += 1) {
						var rc = records[i];
						html.push('<tr><td>' + rc[0] + '</td><td>' + rc[1]
								+ '</td><td>' + rc[4] + '</td><td>' + rc[5]
								+ '</td><td>' + rc[6] + '</td><td>' + rc[8]
								+ '</td></tr>');
					}
					html.push('</table>');
					html = html.join(''); // 最后生成的HTML表格
					// alert(html);
					tableToExcel(html);
				},
				failure : function(response) {
					Ext.Msg.alert('信息', '失败');
				}
			});
		}
	}

	new Ext.Viewport({
		layout : "border",
		items : [{
			region : 'west',
			id : 'west-panel',
			split : true,
			width : 250,
			layout : 'fit',
			minSize : 175,
			border : false,
			maxSize : 400,
			margins : '0 0 0 0',
			title : '锅炉设备树',
			collapsible : true,
			items : [new Ext.TabPanel({
				tabPosition : 'bottom',
				activeTab : 0,
				layoutOnTabChange : true,
				items : [{
					title : '锅炉设备树',
					border : false,
					autoScroll : true,
					items : [mytree],
					listeners : {
						'activate' : function() {
							root.expand();
						}
					}
				}]
			})]
		}, {
			region : 'center',
			height : '100%',
			border : false,
			layout : 'border',
			items : [grid]
		}]
	});
});