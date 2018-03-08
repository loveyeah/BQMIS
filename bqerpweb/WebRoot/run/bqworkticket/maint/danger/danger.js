Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var currentNode = new Object();
	var currentReasonId = "0";
	currentNode.id = "0";
	currentNode.text = "危险点";

	var wd = 180;

	var dangerId = {
		id : "dangerId",
		xtype : "textfield",
		fieldLabel : 'ID',
		value : '自动生成',
		width : 80,
		readOnly : true,
		name : 'dangerId'

	}

	var PDangerId = {
		id : "PDangerId",
		xtype : "textfield",
		fieldLabel : '父ID',
		readOnly : true,
		width : 80,
		name : 'danger.PDangerId'

	}
    
	//定义危险点类型数据源
	var dangerStore = new Ext.data.JsonStore({
		url : 'workticket/getQueryDangerType.action',
		root : 'list',
		fields : [{
			name : 'dangerTypeName'
		}, {
			name : 'dangerTypeId'
		}]
	})
	dangerStore.load();
//	dangerStore.on('load');

	// 危险点类型名称下拉框
	var dangerTypeId = new Ext.form.ComboBox({
		id : 'dangerTypeId',
		fieldLabel : '危险点类型',
		allowBlank : true,
		triggerAction : 'all',
		store : dangerStore,
		displayField : 'dangerTypeName',
		valueField : 'dangerTypeId',
		name : 'danger.dangerTypeId',
		hiddenName : 'danger.dangerTypeId',
		mode : 'local',
		emptyText : '请选择危险点类型...',
	    blankText : '危险点类型',
		readOnly : true,
		width : 80
	})

	var dangerName = {
		id : "dangerName",
		xtype : "textfield",
		fieldLabel : '危险点内容',
		name : 'danger.dangerName',
		width : 80,
		allowBlank : false
	}

	var modifyBy = {
		id : "modifyBy",
		xtype : "textfield",
		fieldLabel : '填写人',
		width : 80,
		name : 'danger.modifyBy',
		readOnly : true
	}

	var modifyDate = {
		id : 'modifyDate',
		xtype : "textfield",
		fieldLabel : '填写时间',
		name : 'danger.modifyDate',
		format : 'Y-m-d',
		width : 80,
		readOnly : true

	};

	var orderBy = {
		id : 'orderBy',
		xtype : "textfield",
		fieldLabel : '排序',
		name : 'danger.orderBy',
		width : 80

	};

	function renderDate(value) {
		if (!value)
			return "";
		var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
		var reTime = /\d{2}:\d{2}:\d{2}/gi;
		var strDate = value.match(reDate);
		var strTime = value.match(reTime);
		if (!strDate)
			return "";
		return strTime ? strDate + " " + strTime : strDate;
	}

	var mypanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		labelWidth : 80,
		title : '工作票危险点基本信息',
		items : [{
			layout : 'column',
			items : [{
				columnWidth : 0.5,
				layout : 'form',
				items : [dangerId, dangerName, orderBy]
			}, {
				columnWidth : 0.5,
				layout : 'form',
				items : [modifyBy, modifyDate,dangerTypeId]
			}]
		} 
//		{
//			layout : 'column',
//			items : [{
//				columnWidth : 1,
//				layout : 'form',
//				items : [dangerTypeId]
//			}]
//		}
	    ],
		buttons : [{
			text : '保存',
			handler : function() {
				var op = "";
				if (Ext.get("dangerId").dom.value == "自动生成") {
					op = "add";
				} else {
					op = "update";
				}
				mypanel.getForm().submit({
					method : 'POST',
					url : 'workticket/' + op + 'Danger.action?method=' + op,
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("注意", o.msg);

						// mypanel.getForm().reset();
						store.reload();
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

	// 定义grid 危险点列表
	var MyRecord1 = Ext.data.Record.create([{
		name : 'dangerId'
	}, {
		name : 'dangerName'
	}, {
		name : 'modifyBy'
	}, {
		name : 'modifyDate'
	}, {
		name : 'orderBy'
	}, {
		name : 'PDangerId'
	}]);

	var dataProxy1 = new Ext.data.HttpProxy(

			{

				url : 'workticket/getDangerList.action'
			}

	);

	var theReader1 = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"

	}, MyRecord1);

	var reasonStore = new Ext.data.Store({

		proxy : dataProxy1,

		reader : theReader1

	});

	var fuzzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});
	var sm = new Ext.grid.CheckboxSelectionModel();

	var gridreason = new Ext.grid.GridPanel({
		store : reasonStore,
		height : 200,
		columns : [// 设计页面显示的表格信息，字段与Ext.data.Record.create匹配
		sm, new Ext.grid.RowNumberer({
			width : 20,
			align : 'center'
		}), {
			header : "ID",
			sortable : true,
			dataIndex : 'dangerId',
			hidden : true

		}, {
			header : "危险点名称",
			sortable : true,
			dataIndex : 'dangerName',
			width : wd,
			align : 'center'
		}, {
			header : "填写人",
			sortable : true,
			dataIndex : 'modifyBy',
			width : wd,
			align : 'center'
		}, {
			header : "填写日期",
			sortable : true,
			dataIndex : 'modifyDate',
			renderer : renderDate,
			width : wd,
			align : 'center'
		}, {
			header : "排序",
			sortable : true,
			dataIndex : 'orderBy',
			width : wd,
			align : 'center'
		}, {
			header : "父ID",
			sortable : true,
			dataIndex : 'PDangerId',
			hidden : true,
			align : 'center'

		}],
		sm : sm,
		title : '工作票危险点详细信息',

		tbar : [fuzzy, {
			text : "查询",
			handler : queryreason
		}, {
			text : "新增",
			iconCls : 'add',
			handler : addreason
		}, {
			text : "修改",
			iconCls : 'update',
			handler : updatereason
		}, {
			text : "删除",
			iconCls : 'delete',
			handler : deletereason
		}]
	});
	gridreason.on("dblclick", updatereason);
	gridreason.addListener('rowClick', rowClick);
	function rowClick(grid, rowIndex, e) {
		var record = grid.getStore().getAt(rowIndex);
		var dangerId = record.get("dangerId");
		var dangerName = record.get("dangerName");
		currentReasonId = dangerId;
		gridsolution.setTitle(dangerName + "---对应的控制措施");
		solutionstore.load({
			params : {
				start : 0,
				limit : 10,
				PDangerId : currentReasonId,
				fuzzy : '%'
			}
		});

	}

	function queryreason() {
		if (currentNode.id != "0") {
			reasonStore.load({
				params : {
					start : 0,
					limit : 10,
					fuzzy : fuzzy.getValue(),
					PDangerId : currentNode.id
				}
			});
		} else {
			alert("请选择工作票危险点内容！");
		}
	}

	// -------危险点内容增加/修改页面-------------
	var dangerId = {
		id : "dangerId1",
		xtype : "textfield",
		fieldLabel : 'ID',
		value : '自动生成',
		width : wd,
		readOnly : true,
		name : 'dangerId'

	}
	var dangerName = {
		id : "dangerName1",
		xtype : "textfield",
		fieldLabel : '危险点名称',
		// readOnly : true,
		width : wd,
		name : 'danger.dangerName'

	}
	var modifyBy = {
		id : "modifyBy1",
		xtype : "textfield",
		fieldLabel : '填写人',
		readOnly : true,
		width : wd,
		name : 'danger.modifyBy'

	}
	var modifyDate = {
		id : "modifyDate1",
		xtype : "textfield",
		fieldLabel : '填写日期',
		readOnly : true,
		width : wd,
		name : 'danger.modifyDate'

	}
	var orderBy = {
		id : "orderBy1",
		xtype : "textfield",
		fieldLabel : '排序',
		// readOnly : true,
		width : wd,
		name : 'danger.orderBy'

	}
	var PDangerId1 = {
		id : "PDangerId1",
		xtype : "textfield",
		fieldLabel : '父ID',
		// readOnly : true,
		hidden : true,
		width : wd,
		name : 'danger.PDangerId'

	}

	var reasonform = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		title : '增加/修改危险点信息',
		items : [dangerId, dangerName, modifyBy, modifyDate, orderBy]

	});

	var reasonwin = new Ext.Window({
		width : 400,
		height : 280,
		buttonAlign : "center",
		items : [reasonform],
		layout : 'fit',
		closeAction : 'hide',
		modal : true,
		buttons : [{
			text : '保存',
			handler : function() {
				var myurl = "";
				if (Ext.get("dangerId1").dom.value == "自动生成") {

					myurl = 'workticket/addDanger.action?PDangerId='
							+ currentNode.id;

				} else {
					myurl = "workticket/updateDanger.action?PDangerId="
							+ currentNode.id;;
				}
				// alert(op);
				reasonform.getForm().submit({
					method : 'POST',
					url : myurl,
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("注意", o.msg);
						reasonwin.hide();
						queryreason();
					},
					faliue : function() {
						Ext.Msg.alert('错误', '出现未知错误.');
					}
				});
			}
		}, {
			text : '取消',
			handler : function() {
				reasonwin.hide();
			}
		}]

	});

	function addreason() {
		if (currentNode.id != "0") {
			reasonform.getForm().reset();
			var form = reasonform.getForm();
			reasonwin.show();
			reasonform.setTitle("增加'" + currentNode.text + "'的危险点");
		} else {
			alert('请选择危险点内容！');
		}

	}

	function updatereason() {
		if (gridreason.selModel.hasSelection()) {
            
			var records = gridreason.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				var record = gridreason.getSelectionModel().getSelected();
				reasonwin.show();
				reasonform.getForm().loadRecord(record);
				Ext.get("dangerName1").dom.value = record.get("dangerName");
				Ext.get("modifyBy1").dom.value = record.get("modifyBy");
				Ext.get("modifyDate1").dom.value = renderDate(record
						.get("modifyDate"));
				Ext.get("orderBy1").dom.value = record.get("orderBy");

			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}

	function deletereason() {
		var sm = gridreason.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];

		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {

			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.dangerId) {
					ids.push(member.dangerId);
				} else {

					store.remove(store.getAt(i));
				}
			}

			Ext.Msg.confirm("删除", "是否确定删除所选择的" + ids.length + "条记录？", function(
					buttonobj) {

				if (buttonobj == "yes") {

					Ext.lib.Ajax.request('POST',
							'workticket/deleteDanger.action', {
								success : function(action) {
									Ext.Msg.alert("提示", "删除成功！")

									// reasonStore.baseParams = {
									// dangerId : currentNode.id,
									// fuzzy : fuuzy.getValue()
									// };
									// reasonStore.load({
									// params : {
									// start : 0,
									// limit : 10
									// }
									// });
									queryreason()
									gridsolution.setTitle("控制措施方案");
									querySolution();
									currentReasonId = "0";
								},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
							}, 'ids=' + ids);
				}

			});
		}

	};

	// -------------控制措施方案--------------
	// 定义grid 控制措施方案
	var MyRecord2 = Ext.data.Record.create([{
		name : 'dangerId'
	}, {
		name : 'dangerName'
	}, {
		name : 'PDangerId'
	}, {
		name : 'modifyBy'
	}, {
		name : 'modifyDate'
	}, {
		name : 'orderBy'
	}]);

	var dataProxy2 = new Ext.data.HttpProxy(

			{
				url : 'workticket/getDangerList.action'
			}

	);

	var theReader2 = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"

	}, MyRecord2);

	var solutionstore = new Ext.data.Store({

		proxy : dataProxy2,

		reader : theReader2

	});

	var solutionFuzzy = new Ext.form.TextField({
		id : "solutionFuzzy",
		name : "solutionFuzzy"
	});
	var mysm = new Ext.grid.CheckboxSelectionModel();

	var gridsolution = new Ext.grid.GridPanel({
		store : solutionstore,
		columns : [mysm, new Ext.grid.RowNumberer({
			width : 20,
			align : 'center'
		}), {

			header : "ID",
			sortable : true,
			dataIndex : 'dangerId',
			hidden : true
		}, {
			header : "控制措施",
			width : wd,
			sortable : true,
			dataIndex : 'dangerName'
		},

		{
			header : "父ID",
			hidden : true,
			sortable : true,
			dataIndex : 'PDangerId'
		}, {
			header : "填写人",
			width : wd,
			sortable : true,
			readOnly : true,
			dataIndex : 'modifyBy'
		}, {
			header : "填写日期",
			width : wd,
			sortable : true,
			readOnly : true,
			renderer : renderDate,
			dataIndex : 'modifyDate'
		}, {
			header : "排序",
			width : wd,
			sortable : true,
			dataIndex : 'orderBy'
		}],
		sm : mysm,
		title : '控制措施',

		tbar : [solutionFuzzy, {
			text : "查询",
			handler : querySolution
		}, {
			text : "新增",
			iconCls : 'add',
			handler : addSolution
		}, {
			text : "修改",
			iconCls : 'update',
			handler : updateSolution
		}, {
			text : "删除",
			iconCls : 'delete',
			handler : deleteSolution
		}]
	});
	gridsolution.on("dblclick", updateSolution);
	function querySolution() {
		if (currentReasonId != "0") {
			solutionstore.load({
				params : {
					start : 0,
					limit : 10,
					PDangerId : currentReasonId,
					fuzzy : solutionFuzzy.getValue()
				}
			});
		} else {
			alert("请选择危险点名称！");
		}
	}

	// -----------故障解决方案增加和修改页面
	var dangerId = {
		id : "dangerId2",
		xtype : "textfield",
		fieldLabel : 'ID',
		value : '自动生成',
		width : wd,
		readOnly : true,
		name : 'dangerId'

	}
	var dangerName = {
		id : "dangerName2",
		xtype : "textfield",
		fieldLabel : '控制措施',
		allowBlank : false,
		// readOnly : true,
		width : wd,
		name : 'danger.dangerName'

	}

	var modifyBy = {
		id : "modifyBy2",
		xtype : "textfield",
		fieldLabel : '填写人',
		width : wd,
		readOnly : true,
		name : 'danger.modifyBy'

	}

	var modifyDate = {
		id : "modifyDate2",
		xtype : "textfield",
		fieldLabel : '填写日期',
		width : wd,
		readOnly : true,
		name : 'danger.modifyDate'

	}

	var orderBy = {
		id : "orderBy2",
		xtype : "textfield",
		fieldLabel : '排序',
		width : wd,
		name : 'danger.orderBy'

	}

	var PDangerId = {
		id : "PDangerId2",
		xtype : "textfield",
		fieldLabel : '父ID',
		width : wd,
		hidden : true,
		name : 'danger.PDangerId'

	}

	var solutionform = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		title : '增加/修改控制措施信息',
		items : [dangerId, dangerName, modifyBy, modifyDate, orderBy]
	});

	var solutionwin = new Ext.Window({
		width : 400,
		height : 280,
		buttonAlign : "center",
		items : [solutionform],
		layout : 'fit',
		closeAction : 'hide',
		buttons : [{
			text : '保存',
			handler : function() {
				var myurl = "";
				if (Ext.get("dangerId2").dom.value == "自动生成") {
					myurl = 'workticket/addDanger.action?PDangerId='
							+ currentReasonId;
				} else {
					myurl = "workticket/updateDanger.action";
				}
				solutionform.form.submit({
					method : 'POST',
					url : myurl,
					success : function(form, action) {

						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("注意", o.msg);

						solutionwin.hide();
						querySolution();

					},
					faliue : function() {
						Ext.Msg.alert('错误', '出现未知错误.');
					}
				});
			}
		}, {
			text : '取消',
			handler : function() {
				solutionwin.hide();
			}
		}]

	});

	function addSolution() {
		if (currentReasonId != "0") {
			solutionform.getForm().reset();
			solutionwin.show();
			solutionform.setTitle("增加原因ID为" + currentReasonId + "的解决方案");
		} else {
			alert("请选择危险点！");
		}
	}

	function updateSolution() {
		if (gridsolution.selModel.hasSelection()) {
			var records = gridsolution.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				var record = gridsolution.getSelectionModel().getSelected();
				solutionwin.show();
				solutionform.getForm().loadRecord(record);
				Ext.get("dangerName2").dom.value = record.get("dangerName");
				Ext.get("modifyBy2").dom.value = record.get("modifyBy");
				Ext.get("modifyDate2").dom.value = renderDate(record
						.get("modifyDate"));
				Ext.get("orderBy2").dom.value = record.get("orderBy");
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}

	}
	function deleteSolution() {
		var sm = gridsolution.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {

			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.dangerId) {
					ids.push(member.dangerId);
				} else {

					store.remove(store.getAt(i));
				}
			}

			Ext.Msg.confirm("删除", "是否确定删除所选的" + ids.length + "条记录？", function(
					buttonobj) {

				if (buttonobj == "yes") {

					Ext.lib.Ajax.request('POST',
							'workticket/deleteDanger.action', {
								success : function(action) {
									Ext.Msg.alert("提示", "删除成功！")

									querySolution();
								},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
							}, 'ids=' + ids);
				}
			});
		}

	}

	// ------------------------------------
	// -------------add 故障列表----------------
	var MyRecord = Ext.data.Record.create([{
		name : 'dangerId'
	}, {
		name : 'dangerTypeId'
	}, {
		name : 'dangerName'
	}, {
		name : 'orderBy'
	}, {
		name : 'modifyDate'
	}, {
		name : 'modifyBy'
	}, {
		name : 'PDangerId'
	}]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url : 'workticket/getDangerList.action'

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

	// 定义工作票种类数据源
	var dangerTypeStore = new Ext.data.JsonStore({
		url : 'workticket/getQueryDangerType.action',
		root : 'list',
		fields : [{
			name : 'dangerTypeName'
		}, {
			name : 'dangerTypeId'
		}]
	})
	dangerTypeStore.load();
	dangerTypeStore.on('load', function(e, records, o) {
		dangerTypeCbo.setValue(records[0].data.dangerTypeId);
	});

	// 危险点类型名臣下拉框
	var dangerTypeCbo = new Ext.form.ComboBox({
		id : 'dangerTypeCbo',
		allowBlank : true,
		triggerAction : 'all',
		store : dangerTypeStore,
		displayField : 'dangerTypeName',
		valueField : 'dangerTypeId',
		mode : 'local',
		readOnly : true,
		width : 90
	})

	// 分页
	store.load({
		params : {
			start : 0,
			limit : 12
		}
	});
	store.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			fuzzy : dangerFuzzy.getValue(),
			dangerTypeId : dangerTypeCbo.getValue()
		});
	});

	var sm = new Ext.grid.CheckboxSelectionModel();
	var dangerFuzzy = new Ext.form.TextField({
		id : "dangerFuzzy",
		name : "dangerFuzzy",
		width : 70
	});
	var grid = new Ext.grid.GridPanel({
		store : store,
		columns : [sm, new Ext.grid.RowNumberer({
			width : 50,
			align : 'center'
		}), {
			header : "ID",
			sortable : true,
			dataIndex : 'dangerId',
			hidden : true
		},

		{
			header : "危险点类型",
			sortable : true,
			dataIndex : 'dangerTypeId',
			align : 'center',
			hidden : true
		},

		{
			header : "排序",
			sortable : true,
			dataIndex : 'orderBy',
			hidden : true
		},

		{
			header : "填写人",
			sortable : true,
			dataIndex : 'modifyBy',
			hidden : true
		},

		{
			header : "填写日期",
			sortable : true,
			dataIndex : 'modifyDate',
			renderer : renderDate,
			hidden : true
		},

		{
			header : "危险点内容",
			width : 200,
			sortable : true,
			dataIndex : 'dangerName',
			align : 'center'
		}, {
			header : "父ID",
			sortable : true,
			dataIndex : 'PDangerId',
			hidden : true
		}],
		sm : sm,
		tbar : ["危险点类型", dangerTypeCbo, dangerFuzzy, {
			text : "查询",
			iconCls : 'query',
			handler : queryRecord
		}, {
			text : "新增",
			iconCls : 'add',
			handler : addRecord
		}, {
			text : '删除',
			iconCls : 'delete',
			handler : deleteRecord
		}],
		bbar : new Ext.PagingToolbar({
			pageSize : 12,
			store : store,
			displayInfo : true,
			displayMsg : "",
			emptyMsg : ""
		})
	});
	grid.on("dblclick", updateRecord);
	grid.addListener('rowClick', gridrowClick);
	function gridrowClick(grid, rowIndex, e) {

		var record = grid.getStore().getAt(rowIndex);
		var dangerId = record.get("dangerId");
		var modifyDate = record.get("modifyDate");
		var dangerName = record.get("dangerName");
		gridreason.setTitle(dangerName + "----对应的危险点名称");
		mypanel.getForm().loadRecord(record);
		Ext.get("modifyDate").dom.value = renderDate(modifyDate);
		currentNode.id = dangerId;
		currentNode.text = dangerName;
		currentReasonId = "0";

		reasonStore.load({
			params : {
				start : 0,
				limit : 10,
				PDangerId : currentNode.id,
				fuzzy : '%'
			}
		});
		// gridsolution.setTitle("控制措施");
		//		                    
		solutionstore.load({
			params : {
				start : 0,
				limit : 10,
				PDangerId : '-1',
				fuzzy : '%'
			}
		});
	}

	function queryRecord() {
		store.load({
			params : {
				start : 0,
				limit : 12
			}
		});

	}

	// 增加
	function addRecord() {
		mypanel.getForm().reset();
		reasonStore.load({
			params : {
				start : 0,
				limit : 10,
				fuzzy : '%',
				PDangerId : "-1"
			}
		});
		solutionstore.load({
			params : {
				start : 0,
				limit : 10,
				PDangerId : '-1',
				fuzzy : '%'
			}
		});
	}

	// 修改
	function updateRecord() {
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
				if (member.dangerId) {
					ids.push(member.dangerId);
					names.push(member.dangerName);
				} else {

					store.remove(store.getAt(i));
				}
			}

			Ext.Msg.confirm("删除", "是否确定删除名称为'" + names + "'的记录？", function(
					buttonobj) {

				if (buttonobj == "yes") {
					Ext.Ajax.request({
						url : 'workticket/deleteDanger.action',
						params : {
							ids : ids
						},
						method : 'post',
						waitMsg : '正在加载数据...',
						success : function(result, request) {
							var json = eval('(' + result.responseText + ')');
							Ext.Msg.alert("注意", json.msg);
							queryRecord();
							if (json.msg == "删除成功！") {
								store.reload();
								currentReasonId = "0";
								gridreason.setTitle("工作票危险点详细信息");
								queryreason();
								gridsolution.setTitle("控制措施");
								querySolution();

							}
						},
						failure : function() {
							Ext.Msg.alert('错误', '删除时出现未知错误.');
						}
					})

				}
			})

		}
	}

	// ---------------------------------------
	// ----------布局---------------------

	var right = new Ext.Panel({
		region : "center",
		layout : 'border',
		items : [{
			region : 'north',
			layout : 'fit',
			height : 150,
			items : [mypanel]
		}, {
			region : 'center',
			layout : 'fit',
			height : 100,
			items : [gridreason]
		}, {
			region : 'south',
			layout : 'fit',
			height : 200,
			items : [gridsolution]
		}]

	});

	var viewport = new Ext.Viewport({
		layout : 'border',
		items : [{
			region : 'west',
			split : true,
			width : 360,
			layout : 'fit',
			minSize : 175,
			maxSize : 380,
			margins : '0 0 0 0',
			collapsible : true,
			border : false,
			// autoScroll : true,
			items : [grid]
		}, right// 初始标签页
		]
	});

});
