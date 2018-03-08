Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() { 
	function numberFormat(value) {
		if (value === "") {
			return value
		}
		value = String(value);
		// 整数部分
		var whole = value;
		// 小数部分
		var sub = ".0000";
		// 如果有小数
		if (value.indexOf(".") > 0) {
			whole = value.substring(0, value.indexOf("."));
			sub = value.substring(value.indexOf("."), value.length);
			sub = sub + "0000";
			if (sub.length > 3) {
				sub = sub.substring(0, 5);
			}
		}
		var r = /(\d+)(\d{3})/;
		while (r.test(whole)) {
			whole = whole.replace(r, '$1' + ',' + '$2');
		}
		v = whole + sub;
		return v;
	}

	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'apartCode'
	},{
		name : 'blockName'
	}, {
		name : 'nodeCode'
	}, {
		name : 'nodeName'
	}, {
		name : 'collectNow'
	}, {
		name : 'collectHis'
	}, {
		name : 'descriptor'
	}, {
		name : 'nodeType'
	}, {
		name : 'maxValue'
	}, {
		name : 'minValue'
	}, {
		name : 'standardValue'
	}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'manager/findDcsNode.action'
			});

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);
	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	}); 
	store.on("beforeload",function(){ 
		Ext.Msg.wait("正在加载数据!,请等待...");   
		Ext.apply(   
        this.baseParams,   
        {   
            sys:apartCodeBox.getValue(),
            queryKey:queryKey.getValue()
        });  
	});
	store.on("load",function(){
		Ext.Msg.hide();
	});
	var fuuzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});
	// 机组
	var storeChargeBySystem = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'workticket/getDetailEquList.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, [{
			name : 'blockCode',
			mapping : 'blockCode'
		}, {
			name : 'blockName',
			mapping : 'blockName'
		}])
	});
	storeChargeBySystem.on("load",function(){
		var all = new Ext.data.Record({
			'blockCode':'%',
			'blockName':'所有机组'
		});
		storeChargeBySystem.insert(0,all);
		apartCodeBox.setValue(all.data.blockCode);
	});
	storeChargeBySystem.load();
	var apartCodeBox = new Ext.form.ComboBox({
		id : 'apartCode',
		fieldLabel : "机组", 
		store : storeChargeBySystem,
		displayField : "blockName",
		valueField : "blockCode",
		hiddenName : 'node.apartCode',
		selectOnFocus:true,
		mode : 'local',
		triggerAction : 'all',
		value : '',
		readOnly : true,
		width : wd,
		listeners:{
			'select':function(){ 
				queryRecord();
			}
		}
	}); 
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});
	/**
	 * 查询条件
	 */
	var queryKey = new Ext.form.TextField({
		id:'queryKey',
		emptyText:'结点编码/结点名称' 
	});
	
	var grid = new Ext.grid.GridPanel({
		region : "center",
		store : store,
		columns : [sm, new Ext.grid.RowNumberer({
			header : '序号',
			width : 35,
			align : 'left'
		}), 
		{
			header : "机组",
			sortable : true,
			hidden : true,
			dataIndex : 'apartCode',
			align : 'center'
		}, 
		{
			header : "机组",
			sortable : true,
			dataIndex : 'blockName',
			width : 80,
			align : 'center'
		}, {
			header : "节点编码",
			width : 150,
			sortable : true,
			align : 'left',
			dataIndex : 'nodeCode',
			renderer:function(value, metadata, record){ 
					metadata.attr = 'style="white-space:normal;"'; 
					return value;//modify by ywliu 20090911  
			}
		}, {
			header : "节点名称",
			width : 250,
			sortable : true,
			dataIndex : 'nodeName',
			align : 'left',
			renderer:function(value, metadata, record){ 
					metadata.attr = 'style="white-space:normal;"'; 
					return value;//modify by ywliu 20090911  
			}
		}, {
			header : "实时采集",
			width : 100,
			sortable : true,
			dataIndex : 'collectNow',
			align : 'center',
			renderer : function(v) {
				if (v == '1') {
					return "是";
				}
				if (v == '0') {
					return "否";
				}
			}
		}, {
			header : "历史采集",
			width : 120,
			sortable : true,
			dataIndex : 'collectHis',
			align : 'center',
			renderer : function(v) {
				if (v == '1') {
					return "是";
				}
				if (v == '0') {
					return "否";
				}
			}
		}, {
			header : "节点描述",
			width : 120,
			sortable : true,
			dataIndex : 'descriptor',
			align : 'left'
		}, {
			header : "节点类别",
			width : 120,
			sortable : true,
			dataIndex : 'nodeType',
			align : 'center',
			renderer : function(v) {
				if (v == '1') {
					return "模拟量";
				}
				if (v == '2') {
					return "信号量";
				}
			}
		}, {
			header : "最小值",
			width : 120,
			sortable : true,
			renderer : numberFormat,
			dataIndex : 'minValue',
			align : 'center'
		}, {
			header : "最大值",
			width : 120,
			sortable : true,
			renderer : numberFormat,
			dataIndex : 'maxValue',
			align : 'center'
		}, {
			header : "标准值",
			width : 120,
			sortable : true,
			renderer : numberFormat,
			dataIndex : 'standardValue',
			align : 'center'
		}],
		sm : sm,
		tbar : ["机组编码:", apartCodeBox, queryKey,{
			text : "查询",
			iconCls : 'query',
			handler : queryRecord
		}, {
			text : "新增",
			iconCls : 'add',
			handler : addRecord
		}, {
			text : "修改",
			iconCls : 'write',
			handler : updateRecord
		}, {
			text : "删除",
			iconCls : 'delete',
			handler : deleteRecord
		}],
		//分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
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
	var wd = 250;

	

	var nodeCode = new Ext.form.TextField({
		id : "nodeCode",
		xtype : "textfield",
		fieldLabel : '节点编码', 
		allowBlank:false,
		width : wd,
		name : 'node.nodeCode'
	});

	var nodeName = new Ext.form.TextField({
		id : "nodeName",
		xtype : "textfield",
		fieldLabel : '节点名称',
		allowBlank:false,
		name : 'node.nodeName',
		width : wd
	});
	var collectNowBox = new Ext.form.ComboBox({
		fieldLabel : '实时采集',
		store : new Ext.data.SimpleStore({
			fields : ['value', 'text'],
			data : [['1', '是'], ['0', '否']]
		}),
		id : 'collectNow',
		name : 'collectNow',
		valueField : "value",
		displayField : "text",
		value : '0',
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'node.collectNow',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		width : wd
	});

	var collectHisBox = new Ext.form.ComboBox({
		fieldLabel : '历史采集',
		store : new Ext.data.SimpleStore({
			fields : ['value', 'text'],
			data : [['1', '是'], ['0', '否']]
		}),
		id : 'collectHis',
		name : 'collectHis',
		valueField : "value",
		displayField : "text",
		value : '1',
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'node.collectHis',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		width : wd
	});
	var descriptor = new Ext.form.TextField({
		id : "descriptor",
		xtype : "textfield",
		fieldLabel : '节点描述',
		name : 'node.descriptor',
		width : wd
	});

	var nodeTypeBox = new Ext.form.ComboBox({
		fieldLabel : '节点类别',
		store : new Ext.data.SimpleStore({
			fields : ['value', 'text'],
			data : [['1', '模拟量'], ['2', '信号量']]
		}),
		id : 'nodeType',
		name : 'nodeType',
		valueField : "value",
		displayField : "text",
		value : '1',
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'node.nodeType',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		width : wd
	});

	var minValue = new Ext.form.NumberField({
		id : "minValue",
		xtype : "NumberField",
		fieldLabel : '最小值',
		width : wd,
		name : 'node.minValue'
	});
	var maxValue = new Ext.form.NumberField({
		id : "maxValue",
		xtype : "NumberField",
		fieldLabel : '最大值',
		width : wd,
		name : 'node.maxValue'
	});
	var standardValue = new Ext.form.NumberField({
		id : "standardValue",
		xtype : "NumberField",
		fieldLabel : '标准值',
		width : wd,
		name : 'node.standardValue'
	});
//	Ext.FormPanel.prototype.action = "update";
	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		labelWidth : 100,
		title : '实时节点增加/修改',
		items : [nodeCode, nodeName, collectNowBox,
				collectHisBox, descriptor, nodeTypeBox, minValue, maxValue,
				standardValue]
	});  
	
	Ext.Window.prototype.action = "update";
	var win = new Ext.Window({
		width : 450,
		height : 380,
		buttonAlign : "center",
		items : [myaddpanel],
		layout : 'fit',
		closeAction : 'hide',
		draggable : true,
		modal : true,
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
					if (!myaddpanel.getForm().isValid()) {
						
					return false;
				}
				
				var myurl = "";
				if ( win.action == "add") {
					myurl = "manager/addDcsNode.action";
				} else {
					myurl = "manager/updateDcsNode.action";
				}
				myaddpanel.getForm().submit({
					method : 'POST',
					url : myurl,
					params:{
						'node.apartCode': apartCodeBox.getValue()
					},
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("提示", o.msg);
						if(!o.repeat)
						{
							store.load({
								params : {
									start : 0,
									limit : 18
								}
							});
							win.hide();
						}
						else
						{
							nodeCode.focus();
						}
					},
					failure : function() {
						Ext.Msg.alert('错误', '出现未知错误.');
					}
				});
			}
		}, {
			text : '取消',
			iconCls : 'cancer',
			handler : function() {
				win.hide();
			}
		}],
		listeners : {
			'show' : function() {
				var o = (win.action == "add" ? false : true);  
				nodeCode.getEl().dom.readOnly = o;   
			} 
		}
	});

	// 查询
	function queryRecord() { 
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}

	function addRecord() {
		win.action = "add";
		myaddpanel.getForm().reset();
		win.setPosition(200, 100);
		win.show();
		myaddpanel.setTitle("增加实时节点");
	}

	function updateRecord() {
		win.action = "update";
		if (grid.selModel.hasSelection()) {
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				var record = grid.getSelectionModel().getSelected();
				win.setPosition(200, 100);
				win.show();
				myaddpanel.getForm().loadRecord(record);
				myaddpanel.setTitle("修改实时节点");
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
				if (member.nodeCode) {
					ids.push(member.nodeCode);
					names.push(member.nodeName);
				} else {
					store.remove(store.getAt(i));
				}
			}
			Ext.Msg.confirm("删除", "是否确定删除名称为'" + names + "'的记录？", function(
					buttonobj) {
				if (buttonobj == "yes") {
					Ext.lib.Ajax.request('POST',
							'manager/deleteDcsNode.action', {
								success : function(action) {
									Ext.Msg.alert("提示", "删除成功！")
									store.load({
										params : {
											start : 0,
											limit : 18
										}
									});
								},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
							}, 'ids=' + ids);
				}
			});
		} 
	}
//	store.reload();
	queryRecord();// modify by ywliu 20090911
});