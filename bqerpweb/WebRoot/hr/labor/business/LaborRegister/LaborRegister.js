Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	var grid;
	var ds;

	// 定义树结构
	var _deptId = 0;
	var Tree = Ext.tree;

	//var flagDept = 0;
	var tree = new Tree.TreePanel({
		rootVisible : false,
		autoHeight : true,
		root : root,
		animate : true,
		enableDD : true,
		border : false,
		containerScroll : true,
		loader : new Tree.TreeLoader({
			dataUrl : 'com/findDeptList.action'
		})
	});
	var root = new Tree.AsyncTreeNode({
		text : '大唐灞桥电厂',
		draggable : false,
		id : '-1'
	});
	tree.setRootNode(root);
	root.expand(false, true);
	root.select();
	currentNode = root;
	tree.on('click', treeClick);
	function treeClick(node, e) {
		e.stopEvent();
		_deptId = node.id;
		query();
	};

	// 创建一个对象
	var MyRecord = Ext.data.Record.create([{
		name : 'loborRegisterId'
	}, {
		name : 'sendSeason'
	}, {
		name : 'deptId'
	}, {
		name : 'deptCode'
	}, {
		name : 'd'
	}, {
		name : 'lbWorkId'
	}, {
		name : 'lbWorkName'
	}, {
		name : 'empCode'
	}, {
		name : 'chsName'
	}
			//	, {
			//		name : 'sendNum'
			//	}, {
			//		name : 'materialNum'
			//	}
			]);

	//-----------------------------------查询条件----------------------------------------------

	var d = new Date();
	year = d.getFullYear();
	var yearDate = new Ext.form.TextField({
		id : 'yearDate',
		name : '_yearDate',
		fieldLabel : "年份",
		style : 'cursor:pointer',
		cls : 'Wdate',
		width : 75,
		value : year,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y',
					dateFmt : 'yyyy',
					alwaysUseStartDate : true
				});
			}
		}
	});

	var quarterBox = new Ext.form.ComboBox({
		fieldLabel : '季度',
		store : [['1', '第一季度'], ['2', '第二季度'], ['3', '第三季度'], ['4', '第四季度']],
		id : 'quarterBox',
		name : 'quarterBoxName',
		valueField : "value",
		displayField : "text",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'quarterBoxName',
		editable : false,
		triggerAction : 'all',
		width : 85,
		selectOnFocus : true,
		value : 1
	});

	//-----------------------------------end------------------------------------------------

	function query() {
		if (_deptId == null || _deptId == "") {
			Ext.Msg.alert("提示", "请选择左边的树节点!");
			return false;
		}
		var year = yearDate.getValue();
		var quarter = quarterBox.getValue();
		var sendseason = year + "-" + quarter;
		
		Ext.Ajax.request({
			url : 'com/findLaborRegisterList.action',
			params : {
				deptId : _deptId,
				sendSeason : sendseason
			},
			method : 'post',
			waitMsg : '正在加载数据...',
			success : function(result, request) {
				var json = eval('(' + result.responseText + ')');
				document.getElementById("gridDiv").innerHTML = "";
				ds = new Ext.data.JsonStore({
					data : json.data,
					fields : json.fieldsNames
				});
				var wd = Ext.get('gridDiv').getWidth()
				var ht = Ext.get('gridDiv').getHeight()
				var sm = new Ext.grid.CellSelectionModel({});
				grid = new Ext.grid.EditorGridPanel({
					renderTo : 'gridDiv',
					stripeRows : true,
					id : 'grid',
					split : true,
					width : wd,
					height : ht,
					autoScroll : true,
					border : false,
					sm : sm,
					cm : new Ext.grid.ColumnModel({
						columns : json.columModel
					}),
					enableColumnMove : false,
					ds : ds,
					clicksToEdit : 1
				});
				grid.render();

				grid.on('afteredit', function(e) {
					var count = 0.0;
					for (var j = 6; j <= grid.getColumnModel().getColumnCount()
							- 2; j++) {
						var index = grid.getColumnModel().getDataIndex(j);
						count += e.record.get(index) - 0;
					}
					e.record.set('totalSalary', count);
				})
			},
			failure : function(result, request) {
				Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
				//				Ext.Msg.hide();
			}
		});
	}

	// 查询按钮
	var btnquery = new Ext.Button({
		text : '查询',
		iconCls : 'query',
		handler : function() {
			query();
		}
	});

	// 保存按钮
	var btnsave = new Ext.Button({
		text : '保存',
		iconCls : 'save',
		handler : function() {
			saveRecords()
		}
	});

	var viewport = new Ext.Viewport({
		layout : 'border',
		items : [{
			region : 'west',
			layout : 'fit',
			split : true,
			width : 200,
			autoScroll : true,
			collapsible : true,
			border : false,
			items : [new Ext.TabPanel({
				tabPosition : 'bottom',
				activeTab : 0,
				layoutOnTabChange : true,
				items : [{
					title : '部门树',
					border : false,
					autoScroll : true,
					items : [tree],
					listeners : {
						'activate' : function() {
							root.expand();
						}
					}
				}]
			})]
		}, {
			region : 'center',
			id : 'center-panel',
			layout : 'fit',
			items : [new Ext.Panel({
				id : 'panel',
				border : false,
				tbar : ['发放日期：', yearDate, '-', '发放季度：', quarterBox, {
					xtype : "tbseparator"
				}, btnquery, {
					xtype : "tbseparator"
				}, btnsave],
				items : [{
					html : '<div id="gridDiv"></div>'
				}]
			})]
		}]
	});
	Ext.get('gridDiv').setWidth(Ext.get('panel').getWidth());
	Ext.get('gridDiv').setHeight(Ext.get('panel').getHeight() - 25);

	function saveRecords() {
		grid.stopEditing();
		if (ds == null) {
			Ext.Msg.alert('提示信息', '无数据进行保存！')
			return;
		}
		if (ds.getTotalCount() == 0) {
			Ext.Msg.alert('提示信息', '无数据进行保存！')
			return;
		}
		var mod = new Array();
		
		for (var i = 0; i <= ds.getTotalCount() - 1; i++) {
			//var laborRegisterId = ds.getAt(i).get('loborRegisterId');
			var year = yearDate.getValue();
			var quarter = quarterBox.getValue();
			var sendSeason = year + "-" + quarter;
			var empCode = ds.getAt(i).get('empCode');
			// 种类
			var laborMaterialId = new Array();
			// 值
			var materialNum = new Array();
			for (var j = 10; j < grid.getColumnModel().getColumnCount(); j++) {
				
				var dataIndex = grid.getColumnModel().getDataIndex(j);
				laborMaterialId.push(dataIndex.substring(4));
				materialNum.push(grid.getStore().getAt(i).get(dataIndex));
			}
			obj = {
				//laborRegisterId : laborRegisterId,
				sendSeason : sendSeason,
				empCode : empCode,
				laborMaterialId : laborMaterialId,
				materialNum : materialNum
			};
			mod.push(obj);
		}

		var method = '';
		if (ds.getAt(0).get('sendSeason') == null
				|| ds.getAt(0).get('sendSeason') == ''
				&& ds.getAt(0).get('laborMaterialId') == null
				|| ds.getAt(0).get('laborMaterialId') == ''
				&& ds.getAt(0).get('empCode') == null
				|| ds.getAt(0).get('empCode') == '')
				
			method = 'add';
		else
			method = 'update';
		Ext.Msg.confirm('提示信息', '确认要保存吗？', function(id) {
			if (id == 'yes') {
				Ext.Ajax.request({
					url : 'com/saveLoborRgister.action',
					method : 'post',
					params : {
						mod : Ext.util.JSON.encode(mod),
						method : method
					},
					success : function(result, request) {
						Ext.Msg.alert('提示信息', '数据保存成功！');
						query();
					},
					failure : function(result, request) {
						Ext.Msg.alert('提示信息', '数据保存失败！');

					}
				})
			}
		})

	}
		//query();
	});