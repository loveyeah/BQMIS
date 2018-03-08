Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'runlogParmId'
	}, {
		name : 'itemCode'
	}, {
		name : 'itemName'
	}, {
		name : 'specialityCode'
	}, {
		name : 'unitMessureId'
	}, {
		name : 'statType'
	}, {
		name : 'statTypeName'
	}, {
		name : 'diaplayNo'
	}, {
		name : 'unitName'
	}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'runlog/getRunLogParmList.action'
			}

	);

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"

	}, MyRecord);

	var store = new Ext.data.Store({

		proxy : dataProxy,

		reader : theReader

	});
	// 分页

	var fuuzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});
	var specialityStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/findUintProfessionList.action'
		}),
		reader : new Ext.data.JsonReader({
			id : "unit"
		}, [{
			name : 'specialityCode'
		}, {
			name : 'specialityName'
		}])
	});
	specialityStore.load();
	specialityStore.on("load", function(ds, records, o) {
		specialityCode.setValue(records[0].data.specialityCode);
		queryRecord();
	});
	var specialityCode = new Ext.form.ComboBox({
		fieldLabel : '所属专业',
		store : specialityStore,
		valueField : "specialityCode",
		displayField : "specialityName",
		mode : 'local',
		triggerAction : 'all',
		forceSelection : true,
		hiddenName : 'specialityCode',
		editable : false,
		allowBlank : false,
		selectOnFocus : true,
		name : 'specialityCode',
		width : wd
	});

	var sm = new Ext.grid.CheckboxSelectionModel();

	var grid = new Ext.grid.GridPanel({
		region : "center",
		store : store,
		columns : [sm, {
			header : "ID",
			width : 100,
			sortable : true,
			dataIndex : 'runlogParmId',
			hidden : true
		},

		{
			header : "指标编码",
			width : 100,
			sortable : true,
			dataIndex : 'itemCode'
		},

		{
			header : "指标名称",
			width : 250,
			sortable : true,
			dataIndex : 'itemName'
		}, {
			header : "专业名称",
			width : 100,
			sortable : true,
			dataIndex : 'specialityCode',
			hidden : true
		}, {
			header : "计量单位",
			width : 100,
			sortable : true,
			dataIndex : 'unitMessureId',
			hidden : true
		}, {
			header : "计量单位",
			width : 100,
			sortable : true,
			dataIndex : 'unitName'
		}, {
			header : "统计类型",
			width : 100,
			sortable : true,
			dataIndex : 'statType',
			hidden : true
		}, {
			header : "统计类型",
			width : 100,
			sortable : true,
			dataIndex : 'statTypeName'
		}, {
			header : "排序",
			width : 100,
			sortable : true,
			dataIndex : 'diaplayNo',
			hidden : true
		}],
		sm : sm,

		tbar : ['指标编码或名称：', fuuzy, '专业：', specialityCode, {
			text : "查询",
			iconCls : 'query',
			handler : queryRecord
		}, {
			text : "新增",
			iconCls : 'add',
			handler : addRecord
		}, {
			text : "修改",
			iconCls : 'update',
			handler : updateRecord
		}, {
			text : "删除",
			iconCls : 'delete',
			handler : deleteRecord
		}],
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : 10,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});

	grid.on("dblclick", updateRecord);
	// ---------------------------------------

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});

	// -------------------
	var wd = 180;

	var runlogParmId = {
		id : "runlogParmId",
		xtype : "textfield",
		fieldLabel : 'ID',
		value : '自动生成',
		width : wd,
		readOnly : true,
		name : 'runlogParmId'

	}
	var itemId = {
		id : "itemId",
		xtype : "hidden",
		fieldLabel : '指标ID',
		allowBlank : false,
		width : wd,
		value : '0',
		name : 'parm.itemId'

	}
	var itemCode = {
		id : "itemCode",
		xtype : "textfield",
		fieldLabel : '指标编码',
		name : 'parm.itemCode',
		value : '*',
		width : wd,
		readOnly : true
	}
	var itemName = {
		id : "itemName",
		xtype : "textfield",
		fieldLabel : '指标名称',
		name : 'parm.itemName',
		width : wd,
		allowBlank : false,
		blankText : '不可为空！'
	}

	// 计量单位
	var unitStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'manager/getUnitList.action?special='
					+ specialityCode.getValue()
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, [{
			name : 'unitId'
		}, {
			name : 'unitName'
		}])
	});
	unitStore.load();
	var unitMessureId = {
		xtype : 'combo',
		id : 'unitMessureId',
		fieldLabel : '计量单位',
		store : unitStore,
		valueField : "unitId",
		displayField : "unitName",
		mode : 'remote',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'parm.unitMessureId',
		editable : false,
		triggerAction : 'all',
		width : wd,
		blankText : '请选择',
		emptyText : '请选择',
		selectOnFocus : true,
		allowBlank : false
	};

	var statType = new Ext.form.ComboBox({
		fieldLabel : '统计类型',
		store : new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"],
			data : [['1', '累加值'], ['2', '最新值'], ['3', '平均值']]
		}),
		valueField : "retrunValue",
		displayField : "displayText",
		mode : 'local',
		forceSelection : true,
		blankText : '请选择',
		emptyText : '请选择',
		hiddenName : 'parm.statType',
		value : '1',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		name : 'parm.statType',
		width : wd
	});

	var diaplayNo = {
		id : "diaplayNo",
		xtype : "textfield",
		fieldLabel : '显示顺序',
		name : 'diaplayNo',
		width : wd
	}

	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		title : '增加/修改指标参数信息',
		items : [runlogParmId, itemId, itemCode, itemName, unitMessureId,
				statType, diaplayNo]

	});

	var win = new Ext.Window({
		width : 400,
		height : 300,
		buttonAlign : "center",
		items : [myaddpanel],
		layout : 'fit',
		modal:true,
		closeAction : 'hide',
		buttons : [{
			text : '保存',
			handler : function() {

				if (Ext.get("diaplayNo").dom.value != "") {
					var r = /^[0-9]*[0-9][0-9]*$/;
					if (r.test(Ext.get("diaplayNo").dom.value) == false) {
						alert("显示顺序请输入数字");
						return false;
					}
				}

				var myurl = "";
				if (Ext.get("runlogParmId").dom.value == "自动生成") {
					myurl = "runlog/addRunLogParm.action?special="
							+ specialityCode.getValue();
				} else {
					myurl = "runlog/updateRunLogParm.action";
				}
				// alert(op);
				myaddpanel.getForm().submit({
					method : 'POST',
					url : myurl,
					success : function(form, action) {

						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("注意", o.msg);
						queryRecord();
//						store.load({
//							params : {
//								start : 0,
//								limit : 10
//							}
//						});
						win.hide();
					},
					faliue : function() {
						Ext.Msg.alert('错误', '出现未知错误.');
					}
				});
			}
		}, {
			text : '取消',
			handler : function() {
				win.hide();
			}
		}]

	});

	// 查询
	function queryRecord() {
		var fuzzytext = fuuzy.getValue();
		store.baseParams = {
			fuzzy : fuzzytext,
			special : specialityCode.getValue()
		};
		store.load({
			params : {
				start : 0,
				limit : 10
			}
		});
	}

	function addRecord() {
		myaddpanel.getForm().reset();
		win.show();
		myaddpanel.setTitle("增加运行参数信息");
	}

	function updateRecord() {
		if (grid.selModel.hasSelection()) {

			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				var record = grid.getSelectionModel().getSelected();
				myaddpanel.getForm().reset();
				win.show();
				myaddpanel.getForm().loadRecord(record);
				statType.setValue(record.get("statType"));
				myaddpanel.setTitle("修改运行参数信息");
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}

	function deleteRecord() {

		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		var names = [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {

			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.runlogParmId) {
					ids.push(member.runlogParmId);
					names.push(member.itemName);
				} else {

					store.remove(store.getAt(i));
				}
			}

			Ext.Msg.confirm("删除", "是否确定删除指标名称为'" + names + "'的记录？", function(
					buttonobj) {

				if (buttonobj == "yes") {

					Ext.lib.Ajax.request('POST',
							'runlog/deleteRunLogparm.action', {
								success : function(action) {
									Ext.Msg.alert("提示", "删除成功！")
									queryRecord();

								},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
							}, 'ids=' + ids);
				}
			});
		}

	}

});