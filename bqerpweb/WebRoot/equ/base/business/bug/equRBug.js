Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var currentNode = new Object();
	currentNode.id = "root";
	currentNode.text = "故障树";
	// // -----------定义tree-----------------
	var root = new Ext.tree.AsyncTreeNode({
		id : "root",
		text : "故障树"
	});

	var mytree = new Ext.tree.TreePanel({
		renderTo : "treepanel",
		autoHeight:true,
		border:false,
		root : root,
		loader : new Ext.tree.TreeLoader({
			url : "equbug/getBugTreeByCode.action",
			baseParams : {
				id : 'root'
			}

		})
	});

	mytree.on("click", clickTree, this);
	mytree.on('beforeload', function(node) {
		mytree.loader.dataUrl = 'equbug/getBugTreeByCode.action?id=' + node.id;
			//mytree.loader.dataUrl = 'test.txt';
		}, this);

	function clickTree(node) {
		currentNode = node;
		grid.setTitle("【" + node.text + "】对应设备")
		store.baseParams = {
			bugCode : node.id
		};
		store.load({
			params : {
				start : 0,
				limit : 10
			}
		});
	};
	root.expand();//展开根节点

	//------定义grid-----
	var MyRecord = Ext.data.Record.create([{
		name : 'id'
	}, {
		name : 'bugCode'
	}, {
		name : 'attributeCode'
	}, {
		name : 'equName'
	}]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url : 'equbug/findEquRBugList.action'
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


	var sm = new Ext.grid.CheckboxSelectionModel();

	var grid = new Ext.grid.GridPanel({
		store : store,
		title : '对应设备',
		columns : [// 设计页面显示的表格信息，字段与Ext.data.Record.create匹配
		sm, {

			header : "ID",
			sortable : true,
			dataIndex : 'id',
			hidden : true
		},

		{
			header : "故障码",
			width : 100,
			sortable : true,
			dataIndex : 'bugCode'
		},

		{
			header : "功能码",
			width : 100,
			sortable : true,
			dataIndex : 'attributeCode'
		},

		{
			header : "设备名称",
			width : 200,
			sortable : true,
			dataIndex : 'equName'
		}],
		sm : sm,
		tbar : [{
			id : 'btnAdd',
			text : "新增",
			iconCls : 'add',
			handler : addRecord
		}, {
			text : "删除",
			iconCls : 'delete',
			handler : deleteRecord
		}],
		//分页
		bbar : new Ext.PagingToolbar({
			pageSize : 10,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});

	function addRecord() {
		var url = "../kksselect/selectAttribute.jsp?op=many";
		var equ = window.showModalDialog(url, '',
				'dialogWidth=400px;dialogHeight=400px;status=no');

		if (typeof(equ) != "undefined") {

			// addEquRLocation

			Ext.Ajax.request({
				url : 'equbug/addEquRBug.action',
				params : {
					codes : equ.code,
					bugCode : currentNode.id
				},
				method : 'post',
				waitMsg : '正在加载数据...',
				success : function(result, request) {
					var json = eval('(' + result.responseText + ')');
   Ext.Msg.alert("注意", json.msg);
					//alert(json.msg);
					//刷新grid
					store.baseParams = {
						bugCode : currentNode.id
					};
					store.load({
						params : {
							start : 0,
							limit : 10
						}
					});

				},
				failure : function(result, request) {
					Ext.MessageBox.alert('错误', '操作失败!');
				}
			});

		}

	}

	function deleteRecord() {
		var mysm = grid.getSelectionModel();
		var selected = mysm.getSelections();
		var ids = [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择设备！");
		} else {

			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.id) {
					ids.push(member.id);
				} else {

					store.remove(store.getAt(i));
				}
			}
			Ext.Msg.confirm("删除", "确定删除选择的记录吗？", function(buttonobj) {
				if (buttonobj == "yes") {
					Ext.Ajax.request({
						url : 'equbug/deleteEquRBug.action',
						params : {
							ids : ids.toString()

						},
						method : 'post',
						waitMsg : '正在加载数据...',
						success : function(result, request) {
							var json = eval('(' + result.responseText + ')');
                                 Ext.Msg.alert("注意", json.msg);
							//alert(json.msg);
							//刷新grid
							store.baseParams = {
								bugCode : currentNode.id
							};
							store.load({
								params : {
									start : 0,
									limit : 10
								}
							});

						},
						failure : function(result, request) {
							Ext.MessageBox.alert('错误', '操作失败!');
						}
					});
				}
			})
		}
	}
	//-------------------

	var right = new Ext.Panel({
		region : "center",
		layout : 'fit',
		autoScroll : true,
		items : [grid]
	});

	
	var viewport = new Ext.Viewport({
		layout : 'border',
		items : [{
			region : 'west',
			split : true,
			width : 250,
			layout : 'fit',
			minSize : 175,
			maxSize : 600,
			margins : '0 0 0 0',
			collapsible : true,
			border : false,
			autoScroll : true,
			items : [mytree]
		}, right]
	});

});