Ext.namespace('Power.operateItem');
Power.operateItem = function(config,isShowOne,cmoboConfig){
	// 经营指标
	// config 配置信息，为以后功能扩充用
	// isShowOne 是否只传回一个指标
	// cmoboConfig 组合框的配置信息
	var firstRecord = new Ext.data.Record.create([
	{
		name : 'itemCode'
	},{
		name : 'itemName'
	},{
		name : 'unitId'
	},{
		name : 'unitName'
	}, {
		name : 'displayNo'
	}]) 
	
	var dataProxy = new Ext.data.HttpProxy({
		url : ''
	});

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, firstRecord);

	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});

	var sm = new Ext.grid.CheckboxSelectionModel();

	number = new Ext.grid.RowNumberer({
		header : "",
		align : 'left'
	})

	// 重置排序号
	function resetLine() {
		for (var j = 0; j < store.getCount(); j++) {
			var temp = store.getAt(j);
			temp.set("displayNo", j + 1);
		}
	}

	// 删除记录

	function deleteRecords() {
		var sm = grid.getSelectionModel();
		var selections = sm.getSelections();
		if (selections.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selections.length; i += 1) {
				var member = selections[i];
				grid.getStore().remove(member);
			}
			resetLine();
		}
	}
	// 确定返回数据
	function saveRecords() {
		grid.stopEditing();
		var ros = new Array();
		var i;
		if(isShowOne != null && isShowOne == true)
		{
			if(store.getCount() > 1)
			{
				Ext.Msg.alert('提示','只能选择一个指标！');
				return;
			}
		}
		for (i = 0; i < store.getCount(); i++) {
			var obj = store.getAt(i).data
			ros.push(obj);
		}
		if(store.getCount() > 0)
			setValue(store.getAt(0).get('itemCode'),store.getAt(0).get('itemName'))
		win.hide();
		return ros;
	}

	// 关闭
	function cancelRecords() {
		win.hide();
		return null;
	}
	
	var confirmBtu = new Ext.Button({
			text : "确定",
			iconCls : 'save',
			handler : saveRecords
		})
	var closeBtu = new Ext.Button({
			text : "关闭",
			iconCls : 'cancer',
			handler : cancelRecords
		})
	// 定义grid
	var grid = new Ext.grid.EditorGridPanel({
		store : store,
		layout : 'fit',
		columns : [sm, 
				number, {
					header : "指标编码",
					sortable : false,
					dataIndex : 'itemCode'
				}, {
					header : "指标名称",
					align : "center",
					sortable : true,
					dataIndex : 'itemName',
					renderer:function(value, metadata, record){ 
							metadata.attr = 'style="white-space:normal;"'; 
							return value;  
					}
				}, {
					header : "单位",
					align : "center",
					sortable : true,
					dataIndex : 'unitName',
					width : 70
				}
				],
		tbar : [{
			text : "删除",
			iconCls : 'delete',
			handler : deleteRecords

		}, '-',confirmBtu , '-',closeBtu ],
		sm : sm,
		viewConfig : {
			forceFit : true
		}
	});
	
	/* 模糊查询的grid */

	var query = new Ext.form.TextField({
		id : 'argFuzzy',
		fieldLabel : "模糊查询",
		hideLabel : true,
		emptyText : '指标编码、指标名称',
		name : 'argFuzzy',
		value : '',
		anchor : '75%'
	});
	function fuzzyQuery() {
		con_ds.baseParams = {
			argFuzzy : query.getValue()
		}
		con_ds.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	};
	var btnUt = new Ext.Button({
		text : '查询',
		iconCls : 'query',
		handler : fuzzyQuery
	});

	// 签字时的Enter
	document.onkeydown = function() {
		if (event.keyCode == 13 && document.getElementById('argFuzzy')) {
			fuzzyQuery();
		}
	}
	var con_item = Ext.data.Record.create([{
		name : 'itemCode',
		mapping : 0
	}, {
		name : 'itemName',
		mapping : 1
	},{
		name : 'unitId',
		mapping : 2
	},{
		name : 'unitName',
		mapping : 3
	}]);
	var con_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true

	});
	var con_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'comm/getOperateItemForSelect.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : "totalCount",
			root : "list"
		}, con_item)
	});
	
	
	var con_item_cm = new Ext.grid.ColumnModel([

	{
		header : '指标编码',
		dataIndex : 'itemCode',
		align : 'center',
		width : 100
	}, {
		header : '指标名称',
		dataIndex : 'itemName',
		align : 'center',
		width : 180,
		renderer:function(value, metadata, record){ 
				metadata.attr = 'style="white-space:normal;"'; 
				return value;
		}
	}]);
	con_item_cm.defaultSortable = true;

	var gridbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : con_ds,
		displayInfo : true,
		displayMsg : "共 {2} 条",
		emptyMsg : "没有记录",
		beforePageText : '页',
		afterPageText : "共{0}"
	});
	var Grid = new Ext.grid.GridPanel({
		id : 'Grid',
		layout : 'fit',
		ds : con_ds,
		cm : con_item_cm,
		split : true,
		height : 385,
		autoScroll : true,
		bbar : gridbbar,
		border : false
	});

	Grid.on('rowdblclick', modifyBtn);
	function modifyBtn() {
		var recR = Grid.getSelectionModel().getSelected();
		var i;
		for (i = 0; i < store.getCount(); i++) {
			if (store.getAt(i).get("itemCode") == recR.data.itemCode) {
				Ext.Msg.alert("提示", "该指标已选择！");
				return;
			}
		}
		if (recR) {
			var currentRecord = grid.getSelectionModel().getSelected();
			var rowNo = store.indexOf(currentRecord);
			var count = store.getCount();
			var currentIndex = currentRecord ? rowNo : count;
			var o = new firstRecord({
				'itemCode' : recR.data.itemCode,
				'displayNo' : '',
				'itemName' : recR.data.itemName,
				'unitId' : recR.data.unitId,
				'unitName' : recR.data.unitName
			});
			grid.stopEditing();
			store.insert(currentIndex, o);
			sm.selectRow(currentIndex);
			resetLine();

		} else {
			Ext.Msg.alert("提示", "请选择左边的一条记录！")
		}
	}

	// 指标体系树
	var rootNode = {
		id : '0',
		text : '指标编码体系',
		attributes : {
			"isItem" : "N"
		}
	};
	var rootNodeObj = new Ext.tree.AsyncTreeNode(rootNode);
	var deptTree = new Ext.tree.TreePanel({

		loader : new Ext.tree.TreeLoader({
			dataUrl : 'comm/findOperateItemTreeForSelect.action'
		}),
		root : rootNodeObj,
		autoWidth : true,
		layout : 'fit',
		autoScroll : true,
		animate : true,
		enableDD : false,
		border : false,
		hidden : true,
		rootVisible : true,
		containerScroll : true
	});
	deptTree.on('beforeload', function(node) {
		deptTree.loader.dataUrl = 'comm/findOperateItemTreeForSelect.action?pid=' + node.id;
	});

	deptTree.on('click', function(node, e) {
		e.stopEvent();
		for (i = 0; i < store.getCount(); i++) {
			if (store.getAt(i).get("itemCode") == node.id) {
				Ext.Msg.alert("提示", "该指标已选择！");
				return;
			}
		}
		if (node.attributes.description == 'Y') {

			var currentRecord = grid.getSelectionModel().getSelected();
			var rowNo = store.indexOf(currentRecord);
			var count = store.getCount();
			var currentIndex = currentRecord ? rowNo : count;
			var o = new firstRecord({
				'itemCode' : node.id,
				'displayNo' : '',
				'itemName' : node.text,
				'unitId' : node.attributes.code,
				'unitName' : node.attributes.openType
			});
			grid.stopEditing();
			store.insert(currentIndex, o);
			sm.selectRow(currentIndex);
			resetLine();

		} else {
			Ext.Msg.alert("提示", "该指标无数据！")
		}

	});
	

		var innerPanel1 = new Ext.Panel({
						layout : "fit",
						id : 'innerPanel1',
						hidden : true,
						autoScroll : true,
						border : false,
						frame : false,
						tbar : [query, ' ', btnUt],
						items : [Grid]
					});

			var rightPanel = new Ext.Panel({
						layout : "fit",
						border : true,
						tbar : [{
									text : "项目选择",
									id : "prjChose",
									handler : prjChose
								}, '-', {
									text : "模糊查询",
									id : "Query",
									handler : Query
								}],
						items : [{
									layout : 'fit',
									border : false,
									frame : false,
									items : [deptTree, innerPanel1]
								}]
					})

	var win = new Ext.Window({
		closeAction : 'hide',
		title : '经营指标选择',
		modal : true,
		width : 600,
		height : 500,
		layout : 'border',
		items : [
			{
									layout : 'fit',
									border : false,
									frame : false,
									region : "west",
									width : '50%',
									items : [grid]
								},
								{
									region : "center",
									border : false,
									bodyStyle : "padding: 0,0,0,5",
									frame : false,
									layout : 'fit',
									items : [rightPanel]
								}]
	});

	win.on('show',function(){
		store.removeAll();		
	})
	function prjChose() {
		innerPanel1.hide();
		deptTree.show();
		rootNodeObj.expand();
	}
	function Query() {
		deptTree.hide();
		innerPanel1.show();
		fuzzyQuery();
	}
	
	var cbStore = new Ext.data.Store({
		reader : new Ext.data.JsonReader({},firstRecord)
	}); 
	var combo = new Ext.form.ComboBox({
		fieldLabel : '选择指标',
		store : cbStore,
		mode : 'local',
		hiddenName :'item',
		name : 'item',
		width: 180,
		valueField : 'itemCode',
		displayField : 'itemName',
		editable : true,
		triggerAction : 'all',
		forceSelection:true,
		readOnly : true, 
		listeners:(cmoboConfig&&cmoboConfig.listeners)?cmoboConfig.listeners:{},
		onTriggerClick : function() {
			win.show();
		}
	});
	Ext.apply(combo, cmoboConfig);
	 
	function setValue(itemCode, itemName) {
		var d1 = new firstRecord({
			itemCode : itemCode,
			itemName : itemName
		});
		cbStore.removeAll();
		cbStore.add(d1);
		combo.setValue(itemCode);
	}
	return {
		win : win,
		confirmBtu : confirmBtu,
		closeBtu : closeBtu,
		saveRecords : saveRecords,
		cancelRecords : cancelRecords,
		combo : combo
	}
}
	
	