Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	var postType = "plan";
	var currentNode = '';
	var currnetType = '';
	var itemId = '';
	function treeClick(node, e) { 
		if(node.id=="0" || node.attributes.description == 'N')
		Grid.setVisible(false);
		else
		{
			Grid.setVisible(true);
			if (e) {
				e.stopEvent();
			}
			if (node.id == '0') {
				form.getForm().reset();
				content.hide();
				currentNode = node;
			} else {
				itemId = node.attributes.id;
				getTopic();
			}
		}
	};
	var Tree = Ext.tree;
	var tree = new Tree.TreePanel({
		autoScroll : true,
		root : root,
		animate : true,
		enableDD : false,
		useArrows : false, // 文件夹前显示的图标改变了不在是+号了
		border : false,
		rootVisible : true,
		containerScroll : true,
		lines : true,
		tbar : new Ext.Panel({
			id : 'treetbar',
			layout : 'column',
			items : [{
				xtype : 'textfield',
				id : 'searchKey',
				width : '100%',
				listeners : {
					specialkey : function(field, e) {
						if (e.getKey() == Ext.EventObject.ENTER) {
							root.reload();
						}
					}
				}
			}]
		}),
		loader : new Tree.TreeLoader({
			dataUrl : 'managexam/findItemTree.action',
			listeners : {
				"beforeload" : function(treeloader, node) {
					var key = Ext.getCmp("searchKey").getValue();
					if (typeof(key) == "undefined")
						key = '';
					treeloader.baseParams = {
						pid : node.id,
						method : 'POST',
						searchKey : key
					};
				}
			}
		})
	});

	var root = new Tree.AsyncTreeNode({
		text : '绩效指标体系',
		iconCls : 'x-tree-node-icon',
		draggable : false,
		id : '0'
	});
	tree.setRootNode(root);
	root.expand();
	tree.on('click', treeClick);
	var con_item = Ext.data.Record.create([{
		name : 'executionid1'
	}, {
		name : 'executionid2'
	}, {
		name : 'executionid3'
	}, {
		name : 'itemcode'
	}, {
		name : 'itemid'
	}, {
		name : 'itemname'
	}, {
		name : 'unitname'
	}, {
		name : 'value1'
	}, {
		name : 'value2'
	}, {
		name : 'value3'
	},{
		name : 'dateTime'
	}]);

	var con_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});
	var con_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managexam/getYearExecutionTableByItemId.action' // 考核主题
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : "totalCount",
			root : "list"
		}, con_item)
	});
	con_ds.on("beforeload",function(){
		Ext.Msg.wait("正在加载数据，请等待...");
		
	});
	con_ds.on("load",function(){
		Ext.Msg.hide();
		
	});
	var con_item_cm = new Ext.grid.ColumnModel([con_sm, {
		header : '项目',
		dataIndex : 'itemname',
		width:200
	}, {
		header : '单位',
		dataIndex : 'unitname'
	}, {
		header : '#11、12机',
		dataIndex : 'value1',
		editor : new Ext.form.NumberField(
													{
														allowDecimals : true,
														decimalPrecision : 4
													})
	}, {
		header : '#1、2机',
		dataIndex : 'value2',
		editor : new Ext.form.NumberField(
													{
														allowDecimals : true,
														decimalPrecision : 4
													})
	}, {
		header : '全厂合并',
		dataIndex : 'value3',
		editor : new Ext.form.NumberField(
													{
														allowDecimals : true,
														decimalPrecision : 4
													})
	}]);

	function getMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10);
		return s;
	}

	var meetingMonth = new Ext.form.TextField({
		style : 'cursor:pointer',
		columnWidth : 0.5,
		readOnly : true,
		anchor : "70%",
		fieldLabel : '年份',
		name : 'month',
		value : getMonth(),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y',
					dateFmt : 'yyyy',
					alwaysUseStartDate : true
				});
				this.blur();
			}
		}
	});

	// 载入数据
	function getTopic() {
		if (itemId != null && itemId != "") {
			con_ds.load({
				params : {
					type : postType,
					datetime : meetingMonth.getValue(),
					itemId : itemId
				}
			});
		} else {
			Ext.MessageBox.alert('提示信息', '请选择要查询的指标！')
		}
	}

	// 保存
	function saveTopic() {
		Grid.stopEditing();
		var modifyRec = Grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0) {
			if (!confirm("确定要保存修改吗?"))
				return;
			var updateData = new Array();
			for (var i = 0; i < modifyRec.length; i++) {  
				updateData.push(modifyRec[i].data); 
			}  
			Ext.Ajax.request({
				url : 'managexam/saveYearExecutionTable.action',
				method : 'post',
				params : {
					addOrUpdateRecords : Ext.util.JSON.encode(updateData),
					type : postType,
					datetime : meetingMonth.getValue()
				},
				success : function(result, request) {
					var o = eval('(' + result.responseText + ')');
					Ext.MessageBox.alert('提示信息', o.msg);
					con_ds.rejectChanges();
					
					con_ds.reload();
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('提示信息', '未知错误！')
				}
			})
		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}
	// 取消
	function cancerTopic() {
		var modifyRec = con_ds.getModifiedRecords();
		if (modifyRec.length > 0 ) {
			if (!confirm("确定要放弃修改吗"))
				return;
			con_ds.reload();
			con_ds.rejectChanges();
			
		} else {
			con_ds.reload();
			con_ds.rejectChanges();
			
		}
	}

	var contbar = new Ext.Toolbar({
		items : ['年份', meetingMonth, {
			id : 'btnQuery',
			iconCls : 'query',
			text : "查询",
			handler : getTopic
		}, '-', {
			id : 'btnSave',
			iconCls : 'save',
			text : "保存",
			handler : saveTopic
		}, '-', {
			id : 'btnCancer',
			iconCls : 'cancer',
			text : "取消",
			handler : cancerTopic
		}]

	});

	var Grid = new Ext.grid.EditorGridPanel({
		clicksToEdit:1,
		viewConfig : {
			forceFit : true
		},
		sm : con_sm,
		ds : con_ds,
		cm : con_item_cm,
		height : 425,
		split : true,
		autoScroll : true,
		layout : 'fit',
		frame : false,
		tbar : contbar,
		border : true
	});

	var layout = new Ext.Viewport({
		layout : "border",
		border : false,
		items : [{
			region : "center",
			layout : 'fit',
			collapsible : true,
			split : true,
			margins : '0 0 0 0',
			items : [Grid]

		}, {
			title : "绩效指标",
			region : 'west',
			margins : '0 0 0 0',
			split : true,
			collapsible : true,
			titleCollapse : true,
			width : 200,
			layoutConfig : {
				animate : true
			},
			layout : 'fit',
			items : [tree]
		}]
	});
});
