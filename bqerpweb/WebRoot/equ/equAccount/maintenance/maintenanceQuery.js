Ext.onReady(function() {
	var method = "one";
	var equCode = "";
	var equName = "";
	// // -----------定义tree-----------------
	var root = new Ext.tree.AsyncTreeNode({
		id : "root",
		text : "设备树"
	});

	var mytree = new Ext.tree.TreePanel({
		renderTo : "treepanel",
		autoHeight : true,
		root : root,
		animate : true,
		enableDD : true,
		enableDrag : true,
		border : false,
		rootVisible : true,
		containerScroll : true,
		// checkModel: "single" ,
		requestMethod : 'GET',
		loader : new Ext.tree.TreeLoader({
			url : "equ/getTreeForSelect.action",
			baseParams : {
				id : 'root',
				method : method
			}

		})
	});

	// -----------树的事件----------------------
	// 树的单击事件
	mytree.on("click", clickTree, this);
	mytree.on('beforeload', function(node) {
		// 指定某个节点的子节点
		mytree.loader.dataUrl = 'equ/getTreeForSelect.action?method=' + method
				+ '&id=' + node.id;
	}, this);

	function clickTree(node) {
		equCode = node.id;
		equName = node.text.substring(node.text.indexOf(' ') + 1,
				node.text.length);
		equcode.setValue(equCode);
		equname.setValue(equName);
		querydata();
	}

	root.expand();// 展开根节点

	// --------------------------------------

	// -----------设备列表--------------------

	var MyRecord = Ext.data.Record.create([{
		name : 'equId'
	}, {
		name : 'equName'
	}, {
		name : 'attributeCode'
	}, {
		name : 'locationCode'
	}, {
		name : 'installationCode'
	}, {
		name : 'assetnum'
	}]);

	var dataProxy = new Ext.data.HttpProxy({
		url : 'equ/findEquListByFuzzy.action'
	});

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);

	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
	// 分页
	store.load({
		params : {
			start : 0,
			limit : 18
		}
	});

	var fuuzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});
	var grid = new Ext.grid.GridPanel({
		store : store,
		border : false,
		columns : [{
			header : "ID",
			sortable : true,
			dataIndex : 'equId',
			hidden : true
		}, {
			header : "设备功能码",
			width : 100,
			sortable : true,
			dataIndex : 'attributeCode'
		}, {
			header : "设备名称",
			width : 100,
			sortable : true,
			dataIndex : 'equName'
		}],
		tbar : [fuuzy, {
			text : "查询",
			handler : queryRecord
		}],
		// 分页
		bbar : new Ext.PagingToolbar({
			id : 'bbar1',
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "共 {2} 条",
			emptyMsg : "没有记录"
		})
	});

	grid.on("click", selectOneRecord);

	// ------------------

	// 查询
	function queryRecord() {
		var fuzzytext = fuuzy.getValue();
		store.baseParams = {
			fuzzy : fuzzytext
		};
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}

	function selectOneRecord() {
		var record = grid.getSelectionModel().getSelected();
		equCode = record.get("attributeCode");
		equName = record.get("equName");
		equcode.setValue(equCode);
		equname.setValue(equName);
		querydata();
	}

	// --------------------------------------

	// -----------设备维修列表--------------------
	var record = Ext.data.Record.create([{
		name : 'order.id'
	}, {
		name : 'order.failureCode'
	}, {
		name : 'order.attributeCode'
	}, {
		name : 'order.equName'
	}, {
		name : 'order.checkAttr'
	}, {
		name : 'order.preContent'
	}, {
		name : 'order.description'
	}, {
		name : 'order.parameters'
	}, {
		name : 'order.problem'
	}, {
		name : 'order.spareParts'
	}, {
		name : 'startDate'
	}, {
		name : 'endDate'
	}, {
		name : 'order.supervisor'
	}, {
		name : 'supervisorName'
	}, {
		name : 'order.participants'
	}]);

	var xproxy = new Ext.data.HttpProxy({
		url : 'equstandard/findListByEquCode.action'
	});

	var xreader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, record);

	var censtore = new Ext.data.Store({
		proxy : xproxy,
		reader : xreader
	});
	// 分页

	var equcode = new Ext.form.TextField({
		id : "equcode",
		name : "equcode",
		readOnly : true
	});
	var equname = new Ext.form.TextField({
		id : "equname",
		name : "equname",
		width : '400',
		readOnly : true
	});
	var cenGrid = new Ext.grid.GridPanel({
		store : censtore,
		autoHeght : true,
		id : 'cenGrid',
		loadMask : {
			msg : '读取数据中 ...'
		},
		columns : [new Ext.grid.RowNumberer(), {
			header : "ID",
			sortable : true,
			dataIndex : 'order.id',
			hidden : true
		}, {
			header : "设备名称",
			width : 100,
			dataIndex : 'order.equName',
			sortable : true
		}, {
			header : "修前情况",
			width : 100,
			dataIndex : 'order.preContent',
			sortable : true
		}, {
			header : "实际开始日期",
			width : 120,
			dataIndex : 'startDate',
			sortable : true
		}, {
			header : "负责人",
			width : 100,
			dataIndex : 'supervisorName',
			sortable : true
		}, {
			header : "检修性质",
			width : 100,
			dataIndex : 'order.checkAttr',
			sortable : true,
			renderer:function(v){
				if(v==1)return 'A级'
				else if(v==2)return 'B级'
				else if(v==3)return 'C级'
				else if (v==4)return 'D级'
				else if(v==5)return '重大消缺'
			}
		}, {
			header : "检修情况",
			width : 100,
			dataIndex : 'order.description',
			sortable : true
		}, {
			header : "存在问题",
			width : 100,
			dataIndex : 'order.problem',
			sortable : true
		}, {
			header : "更换备品备件",
			width : 100,
			dataIndex : 'order.spareParts',
			sortable : true
		}, {
			header : "修后技术参数",
			width : 100,
			dataIndex : 'order.parameters',
			sortable : true
		}],
//		tbar : new Ext.Toolbar({
//			items : [{
//				text : '刷新',
//				iconCls : "reflesh",
//				handler : querydata
//			}, '-', {
//				text : "新增",
//				iconCls : "add",
//				handler : add
//			}, '-', {
//				text : "修改",
//				iconCls : "update",
//				handler : update
//			}, '-', {
//				text : "删除",
//				iconCls : "delete",
//				handler : del
//			}]
//		}),
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : censtore,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});
	function querydata() {
		censtore.baseParams = {
			equCode : equCode
		};
		censtore.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}
	cenGrid.on("rowdblclick", update);
	var viewport = new Ext.Viewport({
		layout : 'border',
		items : [{
			region : 'west',
			layout : 'fit',
			margins : '0 0 0 0',
			width : '300',
			collapsible : true,
			border : false,
			items : [new Ext.TabPanel({
				activeTab : 0,
				layoutOnTabChange : true,
				border : false,
				items : [{
					title : '树形方式',
					border : false,
					autoScroll : true,
					items : [mytree]
				}, {
					title : '列表方式',
					border : false,
					autoScroll : true,
					layout : 'fit',
					items : [grid]
				}]
			})]
		}, {
			region : 'center',
			layout : 'fit',
			bodyStyle : 'padding: 0,0,0,5',
			tbar : new Ext.Toolbar({
				items : ["设备：", equcode, '-', equname]
			}),
			items : [cenGrid]
		}]
	});
	var nowdate = new Date();
	// ----------------------------------------------------------------------------------------------------
//	var btnView = new Ext.Button({
//		id : 'btnView',
//		text : '查看',
//		handler : function() {
//			window.open("/power/managecontract/showConFile.action?conid=" + id
//					+ "&type=CON");
//		}
//	});
	var content = new Ext.form.FieldSet({
		title : '检修台帐查询',
		height : '100%',
		layout : 'form',
		buttonAlign : 'center',
		items : [{
			id : 'order.id',
			name : 'order.id',
			xtype : 'hidden',
			readOnly : true,
			allowBlank : true,
			anchor : '85%'
		}, {
			id : 'order.failureCode',
			name : 'order.failureCode',
			xtype : 'hidden',
			readOnly : true,
			allowBlank : true,
			anchor : '85%'
		}, {
			id : 'txtequcode',
			name : 'order.attributeCode',
			xtype : 'textfield',
			fieldLabel : '系统/设备',
			readOnly : true,
			allowBlank : true,
			anchor : '85%'
		}, {
			id : 'txtequname',
			name : 'order.equName',
			xtype : 'textfield',
			fieldLabel : '系统/设备名称',
			readOnly : true,
			allowBlank : true,
			anchor : '85%'
		}, {
			id : 'order.checkAttr',
			name : 'order.checkAttr',
			xtype : 'textfield',
			fieldLabel : '检修性质',
			readOnly : true,
			allowBlank : false,
			anchor : '85%'
		}, {
			id : 'order.preContent',
			name : 'order.preContent',
			xtype : 'textarea',
			fieldLabel : '修前情况',
			height : '40',
			readOnly : true,
			allowBlank : false,
			anchor : '84.35%'
		}, {
			id : 'order.description',
			name : 'order.description',
			xtype : 'textarea',
			fieldLabel : '检修情况',
			height : '40',
			readOnly : true,
			allowBlank : false,
			anchor : '84.35%'
		}, {
			id : 'order.parameters',
			name : 'order.parameters',
			xtype : 'textarea',
			fieldLabel : '修后技术参数',
			height : '40',
			readOnly : true,
			allowBlank : true,
			anchor : '84.35%'
		}, {
			id : 'order.problem',
			name : 'order.problem',
			xtype : 'textarea',
			fieldLabel : '存在问题',
			height : '40',
			readOnly : true,
			allowBlank : true,
			anchor : '84.35%'
		}, {
			id : 'order.spareParts',
			name : 'order.spareParts',
			xtype : 'textarea',
			fieldLabel : '更换的备品备件',
			height : '40',
			readOnly : true,
			allowBlank : true,
			anchor : '84.35%'
		}, {
			layout : 'column',
			border : false,
			anchor : '90%',
			items : [{
				layout : 'form',
				labelWidth : 100,
				columnWidth : .5,
				border : false,
				items : [{
					xtype : 'textfield',
					format : 'Y-m-d',
					fieldLabel : '实际开始时间',
					name : 'order.startDate',
					id : 'startDate',
					itemCls : 'sex-left',
					clearCls : 'allow-float',
					readOnly : true,
					checked : true,
					anchor : '90%',
					allowBlank : false
				}]
			}, {
				layout : 'form',
				labelWidth : 100,
				columnWidth : .5,
				border : false,
				items : [{
					xtype : 'textfield',
					format : 'Y-m-d',
					fieldLabel : '实际结束时间',
					name : 'order.endDate',
					id : 'endDate',
					itemCls : 'sex-left',
					clearCls : 'allow-float',
					readOnly : true,
					checked : true,
					anchor : '90%',
					allowBlank : false
				}]
			}]
		}, {
			layout : 'column',
			border : false,
			anchor : '90%',
			items : [{
				layout : 'form',
				labelWidth : 100,
				columnWidth : .6,
				border : false,
				items : [{
					id : 'supervisorName',
					name : 'supervisorName',
					xtype : 'textfield',
					fieldLabel : '负责人',
					readOnly : true,
					allowBlank : false,
					anchor : '95%'
				}]
			}]
		}, {
			id : 'order.supervisor',
			name : 'order.supervisor',
			xtype : 'hidden',
			value : '999999',
			fieldLabel : '负责人',
			readOnly : true,
			allowBlank : true,
			anchor : '85%'
		}, {
			id : 'order.participants',
			name : 'order.participants',
			xtype : 'textarea',
			fieldLabel : '参加人',
			height : '40',
			readOnly : true,
			allowBlank : true,
			anchor : '84.35%'
		}]
	});
	var form = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		labelWidth : 100,
		autoHeight : true,
		region : 'center',
		border : false,
		items : [content]
	});

	var win = new Ext.Window({
		// title : '检修台帐编辑',
		autoHeight : true,
		modal : true,
		width : 600,
		closeAction : 'hide',
		items : [form]
	})
	function update() {
		var selections = cenGrid.getSelections();
		if (selections.length > 0) {
			win.show();
			form.getForm().reset();
			var record = cenGrid.getSelectionModel().getSelected();
			form.getForm().loadRecord(record);
		} else {
			Ext.Msg.alert('提示', '请从列表中选择需要修改的记录！');
		}
	}

});