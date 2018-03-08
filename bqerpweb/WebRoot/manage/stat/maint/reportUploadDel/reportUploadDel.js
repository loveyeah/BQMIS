Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	var reportCode = "";
	var annex_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
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
		items : ['名称：', fuzzy, {
			text : "查询",
			iconCls : 'query',
			handler : queryRecord
		},
        {
			id : 'btnAnnexDelete',
			text : "删除",
			iconCls : 'delete',
			handler : function() {
				var selrows = annexGrid.getSelectionModel().getSelections();
				if (selrows.length > 0) {
					var ids = [];
					for (var i = 0; i < selrows.length; i += 1) {
						var member = selrows[i].data;
						if (member.loadId) {
							ids.push(member.loadId);
						} else {
						}
					}
				
					Ext.Msg.confirm('提示', '删除的数据您将不能恢复,确定要删除吗?', function(b) {
						if (b == "yes") {
							var record = annexGrid.getSelectionModel()
									.getSelected();
							Ext.Ajax.request({
								url : 'manager/delReportLoad.action',
								params : {
									loadID :ids.join(",")
								},
								method : 'post',
								waitMsg : '正在删除数据...',
								success : function(result, request) {
									Ext.MessageBox.alert('提示', '删除成功!');
									queryRecord();
								},
								failure : function(result, request) {
									Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
								}
							});
						}

					});

				} else {
					Ext.Msg.alert('提示', '请选择您要删除报表记录！');
				}
			}
		}]
	});
	function queryRecord() {
		if(reportCode != "") {
			annex_ds.load({
				params : {
					loadName : fuzzy.getValue(),
					reportCode : reportCode,
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