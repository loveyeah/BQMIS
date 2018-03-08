Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	Ext.QuickTips.init();

	function getParameter(psName) {
		var url = self.location;
		var result = "";
		var str = self.location.search.substring(0);
		if (str.indexOf(psName) != -1
				&& (str.substr(str.indexOf(psName) - 1, 1) == "?" || str
						.substr(str.indexOf(psName) - 1, 1) == "&")) {
			if (str.substring(str.indexOf(psName), str.length).indexOf("&") != -1) {
				var Test = str.substring(str.indexOf(psName), str.length);
				result = Test.substr(Test.indexOf(psName), Test.indexOf("&")
						- Test.indexOf(psName));
				result = result.substring(result.indexOf("=") + 1,
						result.length);
			} else {
				result = str.substring(str.indexOf(psName), str.length);
				result = result.substring(result.indexOf("=") + 1,
						result.length);
			}
		}
		return result;
	}

	var woCode = getParameter("woCode");
	var opCode = getParameter("opCode");

	/** 弹窗元素↓↓* */

	// 工种
	var url = "com/getTypeOfWorkList.action";
	var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", url, false);
	conn.send(null);
	var planLaborCode_data = eval('(' + conn.responseText + ')');
	var mystore = new Ext.data.SimpleStore({
		fields : ['planLaborName', 'planLaborCode'],
		data : planLaborCode_data
	});



	/** 弹窗元素↑↑* */
	/** 窗口元素↓↓* */

	var datalist = new Ext.data.Record.create([{
		name : 'id'
	}, {
		name : 'orderby'
	}, {
		name : 'planFee'
	}, {
		name : 'planLaborCode'
	}, {
		name : 'planLaborHrs'
	}, {
		name : 'planLaborQty'
	}, {
		name : 'woCode'
	}, {
		name : 'operationStep'
	}]);

	var centerGridsm = new Ext.grid.CheckboxSelectionModel();

	var centerGrids = new Ext.data.JsonStore({
		url : 'equstandard/getEquCStandardManplanList.action?woCode=' + woCode
				+ '&opCode=' + opCode,
		root : 'list',
		totalProperty : 'totalCount',
		fields : datalist
	});

	centerGrids.load({
		params : {
			start : 0,
			limit : 18
		}
	});

	/** centerGrid的按钮组↓↓* */
	// 增加
	function addRecords() {
		// var currentRecord = gird.getSelectionModel().getSelected();
		var count = centerGrids.getCount();
		var currentIndex = count;
		// var currentIndex = currentRecord
		// ? currentRecord.get("displayNo") - 1
		// : count;
		var o = new datalist({
			woCode : woCode,
			operationStep : opCode,
			planLaborCode : '',
			planLaborHrs : '',
			planLaborQty : '',
			planFee : ''
			

		});


		centerGrid.stopEditing();
		centerGrids.insert(currentIndex, o);
		centerGridsm.selectRow(currentIndex);
		centerGrid.startEditing(currentIndex, 2);
		// resetLine();
	};

	// 删除记录
	var ids = new Array();
	function deleteRecords() {
		centerGrid.stopEditing();
		var centerGridsm = centerGrid.getSelectionModel();
		var selected = centerGridsm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("id") != null) {
					ids.push(member.get("id"));
				}
				centerGrid.getStore().remove(member);
				centerGrid.getStore().getModifiedRecords().remove(member);
			}
			// resetLine();
		}
	};
	// 保存
	function save() {
		centerGrid.stopEditing();
		var modifyRec = centerGrid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			Ext.Msg.confirm('提示信息', '确定要保存修改数据吗?', function(button) {
				if (button == 'yes') {
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						updateData.push(modifyRec[i].data);
					}

					Ext.Ajax.request({
								url : 'equstandard/saveEquCStandardManplan.action',
								method : 'post',
								params : {
									isUpdate : Ext.util.JSON.encode(updateData),
									isDelete : ids.join(",")
								},
								success : function(result, request) {
									var o = eval('(' + result.responseText
											+ ')');
									Ext.MessageBox.alert('提示信息', o.msg);
									centerGrids.rejectChanges();
									ids = [];
									centerGrids.reload();
								},
								failure : function(result, request) {
									Ext.MessageBox.alert('提示信息', '未知错误！')
								}
							})
				}
			})

		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}
	// 取消
	function cancer() {
		var modifyRec = centerGrids.getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			Ext.Msg.confirm('提示信息', '确定要放弃修改数据吗?', function(button) {
						if (button == 'yes') {
							centerGrids.reload();
							centerGrids.rejectChanges();
							ids = [];
						}
					})

		} else {
			centerGrids.reload();
			centerGrids.rejectChanges();
			ids = [];
		}
	}

	// 新增
	var centerGridAdd = new Ext.Button({
		text : '新增',
		iconCls : 'add',
		handler : addRecords
			
	});

	

	var centerGridDel = new Ext.Button({
		text : '删除',
		iconCls : 'delete',
		handler : deleteRecords
	});
	var centerGridSave = new Ext.Button({
		text : '保存修改',
		iconCls : 'save',
		handler : save
	});
	// 取消
	var centerGridCancel = new Ext.Button({
		text : '取消',
		iconCls : 'cancer',
		handler : cancer
			// function() {
			// centerGrids.load({
			// params : {
			// start : 0,
			// limit : 18
			// }
			// });
			// }
	});
	/** centerGrid的按钮组↑↑* */

	// 列表
	var centerGrid = new Ext.grid.EditorGridPanel({
		ds : centerGrids,
		columns : [centerGridsm, new Ext.grid.RowNumberer(), {
			header : "工种",
			sortable : false,
			dataIndex : 'planLaborCode',
			renderer : function changeIt(val) {
				for (i = 0; i < mystore.getCount(); i++) {
					if (mystore.getAt(i).get("planLaborCode") == val)
						return mystore.getAt(i).get("planLaborName");
				}

			},
			editor : new Ext.form.ComboBox({
				id : "planLaborCode",				
				fieldLabel : '工种',
				name : 'planLaborCode',
				allowBlank : false,
				blankText : '工种...',
				anchor : '100%',
				store : mystore,
				hiddenName : 'baseInfo.planLaborCode',
				displayField : 'planLaborName',
				valueField : 'planLaborCode',
				mode : 'local',
				value : '',
				readOnly : true,
				triggerAction : 'all'
			})

		}, {
			header : "人数",
			sortable : false,
			dataIndex : 'planLaborQty',
			editor : new Ext.form.NumberField({
				allowNegative:false,
				allowDecimals:false
				
			})
		}, {
			header : "工时",
			sortable : false,
			dataIndex : 'planLaborHrs',
			editor : new Ext.form.NumberField({//modify by wpzhu
				allowNegative:false,
				allowDecimals:true,
				decimalPrecision:5
			})
		}, {
			header : "费用",
			sortable : false,
			dataIndex : 'planFee',
			editor : new Ext.form.NumberField()
		}],
		sm : centerGridsm,
		tbar : [centerGridAdd,  centerGridDel, centerGridCancel , {
			xtype : "tbseparator"
		},centerGridSave ],
		frame : true,
		border : true,
		enableColumnHide : false,
		enableColumnMove : false,
		iconCls : 'icon-grid'
	});

	// 设定布局器及面板
	var layout = new Ext.Viewport({
		layout : "border",
		border : false,
		items : [{
			title : "",
			region : 'center',
			layout : 'fit',
			border : false,
			margins : '0',
			height : 200,
			split : false,
			collapsible : false,
			items : [centerGrid]
		}]
	});
})