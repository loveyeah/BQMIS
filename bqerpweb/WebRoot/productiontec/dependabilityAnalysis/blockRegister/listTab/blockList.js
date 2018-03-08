Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var blockName = new Ext.form.TextField({
		id : 'blockName',
		fieldLabel : '机组名称',
		anchor : "70%"
	});

	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'blockId'
	}, {
		name : 'blockCode'
	}, {
		name : 'blockName'
	}, {
		name : 'holdingCompany'
	}, {
		name : 'holdingPercent'
	}, {
		name : 'manageCompany'
	}, {
		name : 'nameplateCapability'
	}, {
		name : 'attemperCompany'
	}, {
		name : 'belongGrid'
	}, {
		name : 'blockType'
	}, {
		name : 'fuelName'
	}, {
		name : 'productionDate'
	}, {
		name : 'statDate'
	}, {
		name : 'stopStatDate'
	}, {
		name : 'boilerFactory'
	}, {
		name : 'turbineFactory'
	}, {
		name : 'generatorFactory'
	}, {
		name : 'transformerFactory'
	}]);

	var dataProxy = new Ext.data.HttpProxy({
		url : 'productionrec/findKkxBlockList.action'
	});

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);

	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});

	var sm = new Ext.grid.CheckboxSelectionModel();
	var grid = new Ext.grid.GridPanel({
		region : "center",
		store : store,
		columns : [sm, new Ext.grid.RowNumberer({
			header : '序号',
			width : 35,
			align : 'center'
		}), {
			header : "ID",
			width : 100,
			sortable : true,
			dataIndex : 'blockId',
			hidden : true
		}, {
			header : "机组编码",
			width : 100,
			sortable : true,
			align : 'center',
			dataIndex : 'blockCode'
		}, {
			header : "机组名称",
			width : 100,
			sortable : true,
			dataIndex : 'blockName',
			align : 'center'
		}, {
			header : "控股单位",
			width : 100,
			sortable : true,
			dataIndex : 'holdingCompany',
			align : 'center'
		}, {
			header : "控股比例",
			width : 140,
			sortable : true,
			dataIndex : 'holdingPercent',
			align : 'center'
		}, {
			header : "管理单位",
			width : 140,
			sortable : true,
			dataIndex : 'manageCompany',
			align : 'center'
		}, {
			header : "铭牌容量（MW）",
			width : 120,
			sortable : true,
			dataIndex : 'nameplateCapability',
			align : 'center'
		}, {
			header : "调度单位",
			width : 100,
			sortable : true,
			dataIndex : 'attemperCompany',
			align : 'center'
		}, {
			header : "所属电网",
			width : 100,
			sortable : true,
			dataIndex : 'belongGrid',
			align : 'center'
		}, {
			header : "机组类型",
			width : 100,
			sortable : true,
			dataIndex : 'blockType',
			align : 'center'
		}, {
			header : "燃料名称",
			width : 100,
			sortable : true,
			dataIndex : 'fuelName',
			align : 'center'
		}, {
			header : "投产时间",
			width : 100,
			sortable : true,
			dataIndex : 'productionDate',
			align : 'center'
		}, {
			header : "统计时间",
			width : 100,
			sortable : true,
			dataIndex : 'statDate',
			align : 'center'
		}, {
			header : "停统时间",
			width : 100,
			sortable : true,
			dataIndex : 'stopStatDate',
			align : 'center'
		}],
		sm : sm,
		tbar : ["机组名称:", blockName, {
			id : 'btnQuery',
			text : "查询",
			iconCls : 'query',
			handler : queryRecord
		}, '-', {
			text : "新增",
			iconCls : 'add',
			handler : function() {
				// parent.pBlockId = null;
				parent.Ext.getCmp("maintab").setActiveTab(1);
				var url = "productiontec/dependabilityAnalysis/blockRegister/blockTab/blockInfo.jsp";
				if (parent.document.all.iframe2 != null) {
					parent.document.all.iframe2.src = url;
				}
			}
		}, '-', {
			text : "修改",
			iconCls : 'update',
			handler : updateRecord
		}, '-', {
			text : "删除",
			iconCls : 'delete',
			handler : deleteRecord
		}],
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});
	grid.on('rowdblclick', updateRecord);

	function updateRecord() {
		if (grid.selModel.hasSelection()) {
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				var record = grid.getSelectionModel().getSelected().data;
				parent.Ext.getCmp("maintab").setActiveTab(1);
				var url1 = "productiontec/dependabilityAnalysis/blockRegister/blockTab/blockInfo.jsp";
				
				parent.document.all.iframe2.src = url1 + "?blockInfo="
						+ Ext.util.JSON.encode(record);
				// modify by ywliu 20091022		
				parent.pBlockId=record.blockId;		
				parent.document.all.iframe3.src = "productiontec/dependabilityAnalysis/blockRegister/boilerTab/boilerInfo.jsp";
				parent.document.all.iframe4.src = "productiontec/dependabilityAnalysis/blockRegister/turbineTab/turbineInfo.jsp";
				parent.document.all.iframe5.src = "productiontec/dependabilityAnalysis/blockRegister/generatorInfo/generatorInfo.jsp";
				parent.document.all.iframe6.src = "productiontec/dependabilityAnalysis/blockRegister/transformerTab/transformerTab.jsp";
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}

	function deleteRecord() {
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		var nos = [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("blockId")) {
					ids.push(member.get("blockId"));
					nos.push(member.get("blockName"));
				} else {
					store.remove(store.getAt(i));
				}
			}
			if (nos.length > 0) {
				Ext.Msg.confirm("删除", "是否确定删除所选的数据？", function(buttonobj) {
					if (buttonobj == "yes") {
						Ext.lib.Ajax.request('POST',
								'productionrec/deleteKkxBlockInfo.action', {
									success : function(action) {
										Ext.Msg.alert("提示", "数据删除成功！");
										queryRecord();
										// 删除记录刷新登记页面
										// parent.pBlockId = null;
										// parent.document.all.iframe2.src =
										// "productiontec/chemistry/business/condenserLeak/condenserLeakBase.jsp";

									},
									failure : function() {
										Ext.Msg.alert('错误', '删除时出现未知错误.');
									}
								}, 'ids=' + ids);
					}
				});
			}
		}
	}

	function queryRecord() {
		store.load({
			params : {
				start : 0,
				limit : 18,
				fuzzytext : Ext.get('blockName').dom.value
			}
		});
	}

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});

	queryRecord();

});