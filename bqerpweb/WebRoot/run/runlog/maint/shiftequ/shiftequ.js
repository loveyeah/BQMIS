Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	// =================运行专业下拉框================

	// var specialcode = getParameter("specialcode");
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var unitStore = new Ext.data.Store({
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
	unitStore.load();
	unitStore.on("load", function(ds, records, o) {
		unitBox.setValue(records[0].data.specialityCode);
	});

	var unitBox = new Ext.form.ComboBox({
		fieldLabel : '所属专业',
		store : unitStore,
		valueField : "specialityCode",
		displayField : "specialityName",
		mode : 'local',
		triggerAction : 'all',
		forceSelection : true,
		hiddenName : 'specialityCode',
		editable : false,
		selectOnFocus : true,
		name : 'specialityCode',
		id : 'unitBox',
		anchor : '75%'
	});
	var unitFormStore = new Ext.data.Store({
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
	unitFormStore.load();
	var unitBoxForm = new Ext.form.ComboBox({
		fieldLabel : '所属专业',
		store : unitFormStore,
		valueField : "specialityCode",
		displayField : "specialityName",
		mode : 'remote',
		triggerAction : 'all',
		forceSelection : true,
		hiddenName : 'equ.specialityCode',
		editable : false,
		allowBlank : true,
		selectOnFocus : true,
		readOnly : true,
		name : 'equ.specialityCode',
		id : 'unitBoxForm',
		anchor : '75%'
	});
	// ============== 运行方式下拉框================
	var runwaystore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/getRunWayAllList.action'
		}),
		reader : new Ext.data.JsonReader({}, [{
			name : 'id'
		}, {
			name : 'name'
		}])
	});
	runwaystore.load();
	runwaystore.on("load", function(ds, records, o) {
		runwayComboBox.setValue(records[0].data.id);
//		alert(Ext.get('specialityCode').dom.value);
//		alert(Ext.get('runwayId').dom.value);
		runMethodDs.load({
			params : {
				start : 0,
				limit : 18,
				runwayId : records[0].data.id,
				specialcode : Ext.get('specialityCode').dom.value

			}
		});
	});

	var runwayComboBox = new Ext.form.ComboBox({
		// id:'cbrunkey',
		fieldLabel : '运行方式',
		name : 'runwayId',
		store : runwaystore,
		valueField : "id",
		displayField : "name",
		mode : 'remote',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'runwayId',
		editable : false,
		triggerAction : 'all',
		anchor : '75%',
		blankText : '请选择',
		emptyText : '请选择'
			// selectOnFocus : true

	});
	var runwayFormstore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/getRunWayAllList.action'
		}),
		reader : new Ext.data.JsonReader({}, [{
			name : 'id'
		}, {
			name : 'name'
		}])
	});
	runwayFormstore.load();
	var runwayComboBoxForm = new Ext.form.ComboBox({
		// id:'cbrunkey',
		fieldLabel : '运行方式',
		name : 'equ.runKeyId',
		store : runwayFormstore,
		valueField : "id",
		displayField : "name",
		mode : 'remote',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'equ.runKeyId',
		editable : false,
		triggerAction : 'all',
		anchor : '75%',
		blankText : '请选择',
		emptyText : '请选择'
			// selectOnFocus : true

	});

	var runEquId = {
		name : 'equ.runEquId',
		xtype : 'hidden'
	}
	var equName = {
		xtype : "textfield",
		fieldLabel : '设备名称',
		anchor : '75%',
		allowBlank : false,
		name : 'equ.equName'
	}
	var runMethodForm = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		title : '运行方式设备信息',
		items : [runEquId, equName, unitBoxForm, runwayComboBoxForm]
	});
	var runMethodWin = new Ext.Window({
		width : 350,
		height : 200,
		modal : true,
		closeAction : 'hide',
		items : [runMethodForm],
		layout : 'fit',
		buttons : [{
			text : '保存',
			handler : function() {
				runMethodForm.getForm().submit({
					method : 'POST',
					url : 'runlog/addShiftEqu.action',

					success : function(form, action) {
						// ---add----
						// alert(action.response.responseText);
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("注意", o.msg);
						runMethodWin.hide();
						runMethodDs.reload();
					},
					failure : function(result, request) {
						Ext.MessageBox.alert('错误', '操作失败!');
					}
				});
			}
		}, {
			text : '取消',
			handler : function() {
				runMethodWin.hide();
			}
		}]
	});

	var runMethodDs = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/findShiftEqu.action',
			method : 'post'
		}),

		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : 'root'
		//	root : 'shiftEquList'
		}, [{
			name : 'runEquId'
		}, {
			name : 'specialityName'
		}, {
			name : 'runwayName'
		}, {
			name : 'attributeCode'
		}, {
			name : 'equName'
		}, {
			name : 'isUse'
		}, {
			name : 'enterpriseCode'
		}])
	});
	runMethodDs.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			runwayId : runwayComboBox.getValue(),
			specialcode : unitBox.getValue()
		});
	});
	runMethodDs.load({
		params : {
			start : 0,
			limit : 18
		}
	});

	/* 设置记录行的选择框 */
	var runMethodSm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			rowselect : function(sm, index, rec) {

			}
		}
	});
	var runMethodCm = new Ext.grid.ColumnModel([runMethodSm, {
		header : '运行方式下设备ID',
		dataIndex : 'runEquId',
		align : 'center',
		sortable : true,
		hidden : true
	}, {
		header : '运行专业',
		dataIndex : 'specialityName',
		width : 200,
		sortable : true,
		align : 'center'
	}, {
		header : '运行方式',
		dataIndex : 'runwayName',
		width : 200,
		sortable : true,
		align : 'center'
	}, {
		header : '设备功能码',
		dataIndex : 'attributeCode',
		width : 200,
		sortable : true,
		align : 'center'
	}, {
		header : '设备名称',
		dataIndex : 'equName',
		editor : new Ext.form.TextField({
			allowBlank : false,
			align : 'left'
		}),
		width : 200,
		sortable : true,
		align : 'center'
	}]);
	function saveEquName() {
		var str = "[";
		var record = runMethodGrid.getStore().getModifiedRecords();
		if (record.length > 0) {
			for (var i = 0; i < record.length; i++) {
				var data = record[i];
				str = str + "{'runEquId':" + data.get("runEquId")
						+ ",'equName':'" + data.get("equName") + "' },"
			}
			if (str.length > 1) {
				str = str.substring(0, str.length - 1);
			}
			str = str + "]";
			Ext.Ajax.request({
				url : 'runlog/updateRunShiftEqu.action',
				params : {
					data : str
				},
				method : 'post',
				waitMsg : '正在加载数据...',
				success : function(result, request) {
					var json = eval('(' + result.responseText + ')');
					Ext.Msg.alert("注意", json.msg);
					runMethodDs.load({
					params : {
						start : 0,
						limit : 18,
						runwayId : runwayComboBox.getValue(),
						specialcode : unitBox.getValue()

					}
				});
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('错误', '操作失败!');
				}
			});
		} else {
			alert("没有对数据进行任何修改！");
		}

	}
	runMethodCm.defaultSortable = true;

	var runMethodtbar = new Ext.Toolbar({
		items : ['运行专业:', unitBox, '运行方式:', runwayComboBox, '-', {
			id : 'btnReflesh',
			text : "查询",
			iconCls : 'query',
			handler : function() {
				runMethodDs.load({
					params : {
						start : 0,
						limit : 18,
						runwayId : runwayComboBox.getValue(),
						specialcode : unitBox.getValue()

					}
				});
			}
		}, '-', {
			id : 'btnAdd',
			text : "新增",
			iconCls : 'add',
			handler : function() {
				runMethodWin.show();
				runMethodForm.getForm().reset();
				unitBoxForm.setValue(Ext.get('specialityCode').dom.value);
				runwayComboBoxForm.setValue(Ext.get('runwayId').dom.value);
			}
		}, '-', {
			id : 'btnUpdate',
			text : "保存",
			iconCls : 'save',
			handler : saveEquName
		}, '-', {
			id : 'btnDelete',
			text : "删除",
			iconCls : 'delete',
			handler : function() {
				var rec = runMethodGrid.getSelectionModel().getSelected();
				if (rec) {
					if (confirm("确定要删除\"" + rec.data.equName + "\"运行方式及其设备状态吗？")) {

						Ext.Ajax.request({
							url : 'runlog/deleteShiftEqu.action?equ.runEquId='
									+ rec.get("runEquId"),
							method : 'post',
							waitMsg : '正在删除数据...',
							success : function(result, request) {
								Ext.Msg.alert('提示', '删除成功!');
								runMethodDs.load({
									params : {
										start : 0,
										limit : 18,
										runwayId : runwayComboBox.getValue(),
										specialcode : unitBox.getValue()

									}
								});
							},
							failure : function(result, request) {
								Ext.Msg.alert('提示', '删除失败!');
							}
						});
					}
				} else {
					Ext.Msg.alert('提示', '请选择要删除的信息!');

				}
			}
		}, '-', {
			id : 'btnUpdate',
			text : "编辑运行设备状态",
			iconCls : 'update',
			handler : function() {
				var rec = runMethodGrid.getSelectionModel().getSelected();
				if (rec) {
					var myrunEquId = rec.data.runEquId;
					shiftStatusWin = createWin(myrunEquId);
					shiftStatusWin.show();
				} else {
					Ext.Msg.alert('提示', '请选择一条记录!');

				}
			}
		}]
	});
	var runMethodbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : runMethodDs,
		displayInfo : true,
		displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
		emptyMsg : "没有记录",
		//beforePageText : '页',
		afterPageText : "共{0}页"
	})

	var runMethodGrid = new Ext.grid.EditorGridPanel({
		id : 'runMethodGrid',
		// el : 'equ-div',
		ds : runMethodDs,
		cm : runMethodCm,
		sm : runMethodSm,
		bbar : runMethodbbar,
		tbar : runMethodtbar,
		border : false,
		autoWidth : true,
		height : 500,
		fitToFrame : true
	});
	// runMethodGrid.render();

	// ========双击弹出WIN============

	function createWin(myrunEquId) {
		var shiftStatusWin = new Ext.Window({
			width : 770,
			height : 400,
			// closeAction : 'hide',
			html : '<iframe src="run/runlog/maint/shiftequ/runstatusmaint.jsp?runEquId='
					+ myrunEquId
					+ '" frameBorder="0" width="100%" height="100%"/>'
		});
		return shiftStatusWin;
	}

	// =================双击事件===================

	// runMethodGrid.on('rowdblclick', function(grid, index, e) {
	//
	// var rec = runMethodGrid.getStore().getAt(index);
	//
	// var myrunEquId = rec.get("runEquId");
	//
	// shiftStatusWin = createWin(myrunEquId);
	// shiftStatusWin.show();
	// });

	var layout = new Ext.Viewport({
		layout : 'border',
		border : false,
		items : [{
			layout : 'fit',
			margins : '0 0 0 0',
			region : 'center',
			items : runMethodGrid
		}]
	});
});
