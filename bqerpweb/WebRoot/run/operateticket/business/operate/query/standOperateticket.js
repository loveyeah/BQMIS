// 设置树的点击事件
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var selectType = window.dialogArguments;  
	var newOrOldValue = 'new';
	var standOp = new Ext.data.Record.create([{
		name : 'opticketCode'
	}, {
		name : 'opticketName'
	}, {
		name : 'specialityCode'
	}]);
	var standOpDs = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : "opticket/getStantdOpticktetList.action?isStandar='Y'",
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list'
		}, standOp)
	});

	standOpDs.on("beforeload", function() {
		Ext.Msg.wait("正在查询数据,请等待...");
	});
	standOpDs.on("load", function() {
		Ext.Msg.hide();
	});

	var standOpSm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) {
			}
		}
	});
	var storeOpSpecial = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'opticket/getOpticketSpeciality.action'
		}),
		reader : new Ext.data.JsonReader({
			root : "list"
		}, [{
			name : 'specialityCode'
		}, {
			name : 'specialityName'
		}])
	});
	storeOpSpecial.on('render', function() {

	})
	storeOpSpecial.on('load', function(e, records) {
		opticketSpecialCbo.setValue(records[0].data.specialityCode);
	});
	storeOpSpecial.load();
	var opticketSpecialCbo = new Ext.form.ComboBox({
		fieldLabel : '专业',
		id : "opticketSpecialCbo",
		store : storeOpSpecial,
		width : 150,
		resizable : true,
		triggerAction : 'all',
		valueField : "specialityCode",
		displayField : "specialityName",
		mode : 'local',
		selectOnFocus : true,
		readOnly : true,
		listeners : {
			'select' : function() {
				queryGrid();
			}
		}
	});

	var optiketTypeCbo = new Ext.ux.ComboBoxTree({
		fieldLabel : "操作票类别",
		id : 'opticketType',
		hiddenName : 'operateTaskCode',
		displayField : 'text',
		valueField : 'id',
		blankText : '请选择',
		emptyText : '请选择',
		resizable : true,
		readOnly : true,
		width : 300,
		allowBlank : false,
		tree : {
			xtype : 'treepanel',
			rootVisible : true,
			loader : new Ext.tree.TreeLoader({
				// dataUrl : 'opticket/getTreeNode.action'
				dataUrl : 'opticket/getDetailOpTaskTree.action'
			}),
			root : new Ext.tree.AsyncTreeNode({
				id : '-1',
				text : '所有'
			})
		},
		selectNodeModel : 'all',
		listeners : {
			collapse : function() {
				if (this.value) {
					this.el.dom.readOnly = false;
				}
			}
		}
	});
	var standOpCm = new Ext.grid.ColumnModel([standOpSm, {
		id : 'opticketCode',
		header : '操作票编号',
		width : 130,
		dataIndex : 'opticketCode',
		sortable : true
	}, {
		header : '操作任务名称',
		dataIndex : 'opticketName',
		width : 600
	}]);
	var key = new Ext.form.TextField({
		name : 'key',
		emptyText : '操作任务名称'
	});
	// 排序
	standOpCm.defaultSortable = true;
	// standOpDs.baseParams = {
	// opticketType : "",
	// specialityCode : opticketSpecialCbo.getValue(),
	// key : key.getValue()
	// };
	// standOpDs.load({
	// params : {
	// start : 0,
	// limit : 18
	// }
	// });
		function getSelectValue()
	{
		var ns = document.getElementsByName("newOrOld"); 
		for(var i=0;i<ns.length;i++)
		{
			if(ns[i].checked)
			{
				return ns[i].value;	
			}
		}
	}
	var newOrOld = new Ext.Panel({
		hideLabel : true,
		layout : 'column',
		width:200,
		style:{cursor:'hand'},
		height:20,
		items : [new Ext.form.Radio({
			columnWidth : 0.5,
			checked : true, // 设置当前为选中状态,仅且一个为选中.
			boxLabel : "新系统数据", // Radio标签
			name : "newOrOld", // 用于form提交时传送的参数名
			inputValue : "new", // 提交时传送的参数值
			listeners : {
				check : function(checkbox, checked) { // 选中时,调用的事件
					newOrOldValue =getSelectValue();
					queryGrid();
				}
			}
		}), new Ext.form.Radio({
			columnWidth : 0.5,
			boxLabel : "老系统数据",
			name : "newOrOld",
			inputValue : "old" 
		})]
	});
	var tbar = new Ext.Toolbar({
		items : ['专业', '-', opticketSpecialCbo, '任务:', key, {
			id : 'query',
			iconCls : 'query',
			text : "查询",
			handler : function() {
				queryGrid();
			}
		}, '->', {
			id : 'btnConfirm',
			iconCls : 'confirm',
			hidden:true,
			text : "确定",
			handler : function() {
				var ss = [];
				var sm = grid.getSelectionModel();
				var selected = sm.getSelections();
				if (selected.length == 0) {
					Ext.Msg.alert("提示", "请选择要删除的记录！");
				} else {
					for (var i = 0; i < selected.length; i += 1) {
						var member = selected[i];
						ss.push(member.get("opticketCode"));
					} 
					window.returnValue = ss.join(",");
				} 
				window.close();
			}
		}]
	});
	var tbar2 = new Ext.Toolbar({
		items : [newOrOld, '操作票类别', '-', optiketTypeCbo]
	});
	var tbarpanel = new Ext.Panel({
		items : [tbar2, tbar]
	});
	function queryGrid() {
		try {
			var _type = optiketTypeCbo.getCurrentNode().attributes.code;
			if (_type == "-1") {
				_type = "%";
			}
		} catch (err) {
			_type = "%";
		}
		standOpDs.baseParams = {
			newOrOld : newOrOldValue,
			opticketType : _type,
			specialityCode : opticketSpecialCbo.getValue(),
			key : key.getValue()
		};
		standOpDs.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}
	var bbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : standOpDs,
		displayInfo : true,
		displayMsg : '共 {2} 条',
		emptyMsg : "没有记录"
	});
	var grid = new Ext.grid.GridPanel({
		id : 'grid',
		ds : standOpDs,
		cm : standOpCm,
		sm : standOpSm,
		bbar : bbar,
		tbar : tbarpanel,
		border : false,
		autoWidth : true,
		fitToFrame : true
	});
	grid.on('render', function() {
		newNode = new Ext.tree.TreeNode({
			id : '-1',
			text : '所有'
		})
		optiketTypeCbo.setValue(newNode);
	});
	grid.on('rowdblclick', function(grid, rowIndex, e) {
		if(selectType == "multiple")
		return ;
		 var rec = grid.getSelectionModel().getSelected();
		 var opCode = rec.get('opticketCode');
		 window.returnValue = opCode; 
		window.close();
	});
	var viewport = new Ext.Viewport({
		layout : 'fit',
		autoWidth : true,
		autoHeight : true,
		fitToFrame : true,
		items : [grid]
	});

	if(selectType=="multiple")
	{
		Ext.getCmp("btnConfirm").setVisible(true);
	}
});