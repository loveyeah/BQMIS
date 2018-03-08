Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var type = parent.type;
	var flagId = null;
	// 所属机组
	var blockName = new Ext.form.TriggerField({
				fieldLabel : '所属机组',
				width : 145,
				id : "blockName",
				hiddenName : 'sylbName',
				blankText : '请选择',
				emptyText : '请选择',
				maxLength : 100,
				readOnly : true
			});
	blockId = new Ext.form.Hidden({
		id : 'blockId',
		name : 'blockId'
	})
	blockName.onTriggerClick = blockSelect;
	function blockSelect() {
		var args = {
			selectModel : 'signal',
			notIn : "",
			rootNode : {
				id : '-1',
				text : '合肥电厂'
			}
		}
		var url = "../../blockRegister/blockSelect.jsp";
		var block = window.showModalDialog(url, window,
				'dialogWidth=600px;dialogHeight=400px;status=no');
		if (typeof(block) != "undefined") {
			blockName.setValue(block.blockName);
			blockId.setValue(block.blockId);
		}
	}
	
	var name = new Ext.form.TextField({
		id : 'name',
		width : 80
	})
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'auxiliaryId'
	}, {
		name : 'blockId'
	}, {
		name : 'auxiliaryTypeId'
	}, {
		name : 'auxiliaryCode'
	}, {
		name : 'auxiliaryName'
	}, {
		name : 'auxiliaryModel'
	}, {
		name : 'updateNo'
	}, {
		name : 'leaveFactoryNo'
	}, {
		name : 'factoryCode'
	}, {
		name : 'produceFactory'
	}, {
		name : 'blockName'
	}, {
		name : 'typeName'
	}, {
		name : 'startProDateString'
	}, {
		name : 'stopStatDateString'
	}, {
		name : 'leaveFactoryDateString'
	}, {
		name : 'statDateString'
	}, {
		name : 'stopUseDateString'
	},{
		name : 'nameplateCapability'
	}]);

	var dataProxy = new Ext.data.HttpProxy({
		url : 'productionrec/getAllAuxiliaryInfo.action'
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
			dataIndex : 'auxiliaryId',
			hidden : true
		}, {
			header : "所属机组",
			width : 100,
			sortable : true,
			align : 'center',
			dataIndex : 'blockName'
		}, {
			header : "辅机类型",
			width : 100,
			sortable : true,
			dataIndex : 'typeName',
			align : 'center'
		}, {
			header : "辅机编码",
			width : 100,
			sortable : true,
			dataIndex : 'auxiliaryCode',
			align : 'center'
		}, {
			header : "辅机名称",
			width : 140,
			sortable : true,
			dataIndex : 'auxiliaryName',
			align : 'center'
		}, {
			header : "型号",
			width : 140,
			sortable : true,
			dataIndex : 'auxiliaryModel',
			align : 'center'
		}, {
			header : "更新号",
			width : 120,
			sortable : true,
			dataIndex : 'updateNo',
			align : 'center'
		}, {
			header : "投产日期",
			width : 100,
			sortable : true,
			dataIndex : 'startProDateString',
			align : 'center'
		}, {
			header : "停统日期",
			width : 100,
			sortable : true,
			dataIndex : 'stopStatDateString',
			align : 'center'
		}, {
			header : "出厂日期",
			width : 100,
			sortable : true,
			dataIndex : 'leaveFactoryDateString',
			align : 'center'
		}, {
			header : "统计日期",
			width : 100,
			sortable : true,
			dataIndex : 'statDateString',
			align : 'center'
		}, {
			header : "停用日期",
			width : 100,
			sortable : true,
			dataIndex : 'stopUseDateString',
			align : 'center'
		}, {
			header : "出厂序号",
			width : 100,
			sortable : true,
			dataIndex : 'leaveFactoryNo',
			align : 'center'
		}, {
			header : "厂家编码",
			width : 100,
			sortable : true,
			dataIndex : 'factoryCode',
			align : 'center'
		}, {
			header : "制造厂家",
			width : 100,
			sortable : true,
			dataIndex : 'produceFactory',
			align : 'center'
		}],
		sm : sm,
		tbar : ["所属机组:", blockId,blockName,
		'辅机名称：',name,{
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
				var url = "productiontec/dependabilityAnalysis/auxiliaryRegister/fuelInfo/fuelInfo.jsp";
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
				var url1 = "productiontec/dependabilityAnalysis/auxiliaryRegister/fuelInfo/fuelInfo.jsp";
				if (type == '1') {
					if (parent.document.all.iframe3 != null) {
						parent.document.all.iframe3.src = "productiontec/dependabilityAnalysis/auxiliaryRegister/fuelParam/fuelParam.jsp"
					}
				}else if (type == '2') {
					if (parent.document.all.iframe3 != null) {
						parent.document.all.iframe3.src = "productiontec/dependabilityAnalysis/feedpumpParameter/parameterBase/parameterBase.jsp"
					}
				}
				else if (type == '3') {
					if (parent.document.all.iframe3 != null) {
						parent.document.all.iframe3.src = "productiontec/dependabilityAnalysis/blowerRegister/blowerParam/blowerParam.jsp"
					}
				}
				else if (type == '4') {
					if (parent.document.all.iframe3 != null) {
						parent.document.all.iframe3.src = "productiontec/dependabilityAnalysis/IDFRegister/IDFParam/IDFParam.jsp"
					}
				}else if (type == '5') {
					if (parent.document.all.iframe3 != null) {
						parent.document.all.iframe3.src = "productiontec/dependabilityAnalysis/highHeater/heaterParameter/heaterParameter.jsp"
					}
				}else if (type == '6') {
					if (parent.document.all.iframe3 != null) {
						parent.document.all.iframe3.src = "productiontec/dependabilityAnalysis/precipitationRegister/precipitationParam/precipitationParam.jsp"
					}
				}
				else if (type == '7') {
					if (parent.document.all.iframe3 != null) {
						parent.document.all.iframe3.src = "productiontec/dependabilityAnalysis/desulSysRegister/desulSysParam/desulSysParam.jsp"
					}
				}
				flagId = record.auxiliaryId;
				parent.flagId = flagId;
				parent.document.all.iframe2.src = url1 + "?auxiliaryInfo="
						+ Ext.util.JSON.encode(record);
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}

	function deleteRecord() {
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
//		var nos = [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("auxiliaryId")) {
					ids.push(member.get("auxiliaryId"));
//					nos.push(member.get("blockName"));
				} else {
					store.remove(store.getAt(i));
				}
			}
			if (ids.length > 0) {
				Ext.Msg.confirm("删除", "是否确定删除所选的数据？", function(buttonobj) {
					if (buttonobj == "yes") {
						Ext.lib.Ajax.request('POST',
								'productionrec/deleteAuxiliaryInfo.action', {
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
		store.baseParams = {
			blockId : blockId.getValue(),
			typeId  : type,
			name : name.getValue()
		}
		store.load({
			params : {
				start : 0,
				limit : 18
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