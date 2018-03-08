Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	var reportCode = "";
	var annex_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	});
	function getDate1() {
	var d, s, t;
	d = new Date();
	s = d.getFullYear().toString(10) + "-";
	t = d.getMonth() ;
	s += (t > 9 ? "" : "0") + t + "-";
	t = d.getDate();
	s += (t > 9 ? "" : "0") + t;
	return s;
  }
   function getDate2() {
	var d, s, t;
	d = new Date();
	s = d.getFullYear().toString(10) + "-";
	t = d.getMonth() + 1;
	s += (t > 9 ? "" : "0") + t + "-";
	t = d.getDate();
	s += (t > 9 ? "" : "0") + t;
	return s;
  }
	var uploadStart = new Ext.form.TextField({
		id : 'startTime',
		fieldLabel : "上传日期开始",
		name : 'startTime',
		readOnly : true,
		style : 'cursor:pointer',
		anchor : "80%",
		value : getDate1(),
	 	listeners : {
			focus : function() {
			 	var pkr = WdatePicker({
			 	startDate : '%y-%M-%d',
			 	dateFmt : 'yyyy-MM-dd',
			 	alwaysUseStartDate : true
		 	});
		 	}
		}
	});
	var uploadEnd = new Ext.form.TextField({
		id : 'endTime',
		fieldLabel : "上传日期结束",
		name : 'endTime',
		readOnly : true,
		style : 'cursor:pointer',
		anchor : "95%",
		value : getDate2(),
	 	listeners : {
			focus : function() {
			 	var pkr = WdatePicker({
			 	startDate : '%y-%M-%d',
			 	dateFmt : 'yyyy-MM-dd',
			 	alwaysUseStartDate : true
		 	});
		 	}
		}
	});
	var fuzzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});
	
	var annex_item = Ext.data.Record.create([{
		name : 'loadId'
	}, {
		name : 'loadCode'
	}, {
		name : 'loadName'
	}, {
		name : 'reportCode'
	}, {
		name : 'reportTime'
	}, {
		name : 'annexAddress'
	}, {
		name : 'loadBy'
	}, {
		name : 'loadDate'
	}, {
		name : 'firstDeptCode'
	}, {
		name : 'enterpriseCode'
	}, {
		name : 'isUse'
	}]);

	var annex_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'manager/queryReportLoadListByReportCode.action'
		}),
		reader : new Ext.data.JsonReader({
			root : "list",
			totalProperty : "totalCount"
		}, annex_item)
	});

	var annex_item_cm = new Ext.grid.ColumnModel([annex_sm,
			new Ext.grid.RowNumberer({
				header : '序号',
				width : 35,
				align : 'center'
			}), {
				header : 'ID',
				dataIndex : 'loadId',
				hidden : true,
				align : 'center'
			}, {
				header : '编号',
				dataIndex : 'loadCode',
				hidden : true,
				align : 'center'
			}, {
				header : '报表名称',
				width : 150,
				dataIndex : 'loadName',
				align : 'center'
			}, {
				header : '报表日期',
				dataIndex : 'reportTime',
				align : 'center',
				width : 150
			}, {
				header : '上传日期',
				dataIndex : 'loadDate',
				width : 120,
				align : 'center',
				renderer : function(val) {
					if (val != null && val != "") {
						return val.substring(0, 10) + " "
								+ val.substring(10,10)
					}

				}
			}, {
				header : '上传人',
				dataIndex : 'loadBy',
				align : 'center'
			}, {
				header : '查看模板',
				dataIndex : 'annexAddress',
				align : 'center',
				renderer : function(val) {
					if (val != "" && val != null) {
						return "<a style=\"cursor:hand;color:red\" onClick=\"window.open('"
								+ val + "');\"/>查看附件</a>"
					} else {
						return "";
					}
				}
			}]);

	
	
	
	var annextbar = new Ext.Toolbar({
		id : 'annextbar',
		items : ['名称：', fuzzy,'-','上传日期开始',uploadStart,'-','结束',uploadEnd, '-',{
			text : "查询",
			iconCls : 'query',
			handler : queryRecord
		}]
	});
	
	function queryRecord() {
		if(reportCode != "") {
			annex_ds.load({
				params : {
					loadName : fuzzy.getValue(),
					reportCode : reportCode,
					startTime:uploadStart.getValue(),
					endTime:uploadEnd.getValue(),
					flag:'Y',
					start : 0,
					limit : 18
				}
			});
		} else {
			Ext.Msg.alert('提示','请先选择报表类型！')
		}
	}
	var annexGrid = new Ext.grid.GridPanel({
		ds : annex_ds,
		cm : annex_item_cm,
		sm : annex_sm,
		region : "center",
		layout : 'fit',
		split : true,
		autoSizeColumns : true,
		// 分页
		bbar : new Ext.PagingToolbar({
//			pageSize : 18,
			store : annex_ds,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		}),
		// collapsible : true,
		tbar : annextbar,
		border : false,
		viewConfig : {
			forceFit : true
		}
	});


	
	
	var currentNode = new Object();
	var cNode = "";
	var addFaCode = "";
	
	var root = new Ext.tree.AsyncTreeNode({
		id : "root",
		text : "指标报表类型"
	});

	currentNode = root;
	var mytree = new Ext.tree.TreePanel({
		layout : 'fit',
		animate : true,
		autoHeight : true,
		allowDomMove : false,
		autoScroll : true,
		containerScroll : true,
		collapsible : true,
		split : true,
		border : false,
		rootVisible : true,
		root : root,
		animate : true,
		enableDD : false,
		border : false,
		containerScroll : true,
		loader : new Ext.tree.TreeLoader({
			url : "manager/getReportTreeList.action",
			baseParams : {
				id : 'root'
			}
		})
	});

	root.expand();
	mytree.on("click", clickTree, this);
	mytree.on('beforeload', function(node) {
		mytree.loader.dataUrl = 'manager/getReportTreeList.action?id='
				+ node.id;
	}, this);
	
	function clickTree(node,event) {
		
		 event.stopEvent();
		 
		if (node.id == "root") {
			// 根节点
			currentNode = node;
			cNode = node;
			annexGrid.getView().refresh();
		} else {
			currentNode = node;
			cNode = node;
			reportCode = node.id;
			annex_ds.load({
				params : {
					loadName : fuzzy.getValue(),
					reportCode : reportCode,
					startTime:uploadStart.getValue(),
					endTime:uploadEnd.getValue(),
					start : 0,
					limit : 18
				}
			});
		}
	};
	
	var layout = new Ext.Viewport({
		layout : "border",
		border : false,
		items : [{
			region : "center",
			layout : 'fit',
			collapsible : true,
			split : true,
			margins : '0 0 0 0',
			// 注入表格
			items : [annexGrid]
		}, {
			title : "指标报表类型",
			region : 'west',
			margins : '0 0 0 0',
			split : true,
			collapsible : true,
			titleCollapse : true,
			width : 200,
			layoutConfig : {
				animate : true
			},
			items : [mytree]
		}]
	})
	



	
});