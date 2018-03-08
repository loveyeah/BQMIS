Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var runlogId = getParameter("runlogId");
	var specialcode = getParameter("specialcode");
	var runkeyid;

	// ============== 变更状态下拉框================
	var runwaystore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/findListExcept.action'
		}),
		reader : new Ext.data.JsonReader({}, [{
			name : 'id'
		}, {
			name : 'name'
		}])
	});
	runwaystore.on("beforeload", function() {
		var runwayrec = waitEquGrid.getSelectionModel().getSelected();
		var rec = runMethodGrid.getSelectionModel().getSelected();
		Ext.apply(this.baseParams, {
			runKeyId : runwayrec.data.runKeyId,
			specialcode : specialcode,
			shiftEqustatusId : rec.data.shiftEqustatusId
		});
	});
	var toStatusNameComboBox = new Ext.form.ComboBox({
		id : 'toStatusId',
		fieldLabel : '变更后状态',
		name : 'equStatus.toStatusName',
		store : runwaystore,
		valueField : "id",
		displayField : "name",
		mode : 'remote',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'equStatus.toStatusName',
		editable : false,
		triggerAction : 'all',
		anchor : '80%',
		blankText : '请选择',
		emptyText : '请选择',
		allowBlank : false,
		selectOnFocus : true

	});

	var runStatusHisId = {
		id : 'statusId',
		name : 'equStatus.runStatusHisId',
		xtype : 'hidden'
	}
	var runLogno = {
		xtype : "textfield",
		fieldLabel : '日志号',
		anchor : '80%',
		allowBlank : false,
		readOnly : true,
		name : 'equStatus.runLogno'
	}
	var equName = {
		xtype : "textfield",
		fieldLabel : '设备名称',
		readOnly : true,
		anchor : '80%',
		name : 'equStatus.equName'
	}
	var fromStatusName = {
		xtype : "textfield",
		fieldLabel : '设备原状态',
		anchor : '80%',
		readOnly : true,
		name : 'equStatus.equStatusName'
	}

	var date = new Date();
	var operateTime = new Ext.form.DateField({
		fieldLabel : '操作时间',
		format : 'Y-m-d H:i:s',
		name : 'equStatus.operateTime',
		// value : '2008-11',
		id : 'operateTime',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		checked : true,
		value : date,
		readOnly : true,
		// emptyText : '请选择',
		anchor : '80%'
	});
	var operateMemo = {
		id : 'hisMemo',
		xtype : "textarea",
		fieldLabel : '变更原因',
		anchor : '80%',
		name : 'equStatus.operateMemo'
	}
	var runMethodForm = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		title : '设备运行状态变更',
		items : [runStatusHisId, runLogno, equName, fromStatusName,
				toStatusNameComboBox, operateTime, operateMemo]
	});
	var runMethodWin = new Ext.Window({
		width : 400,
		height : 300,
		closeAction : 'hide',
		modal : true,
		items : [runMethodForm],
		layout : 'fit',
		buttons : [{
			text : '确定',
			handler : function() {

				var rec = runMethodGrid.getSelectionModel().getSelected();

				if (runMethodForm.getForm().isValid()) {

					runMethodForm.getForm().submit({
						url : 'runlog/addAndUpdateShiftEqu.action',
						params : {
							newstatusId : toStatusNameComboBox.value,
							newstatusName : Ext.get('toStatusId').dom.value,
							shiftEqustatusId : rec.get('shiftEqustatusId'),
							operateMemo : Ext.get('hisMemo').dom.value,
							operateTime : Ext.get('operateTime').dom.value

						},
						waitMsg : '正在保存数据...',
						method : 'post',
						success : function(form, action) {
							{
								runMethodWin.hide();
								runMethodDs.load({
									params : {
										start : 0,
										limit : 30
									}
								});
								Ext.Msg.alert('提示', '保存成功!');
							}

						},
						failure : function(form, action) {
							Ext.Msg.alert('提示', '保存失败!');

						}
					});
				}
			}
		}, {
			text : '取消',
			handler : function() {
				runMethodWin.hide();
			}
		}]
	});

	// =========================================

	var atbar = new Ext.Toolbar({
		items : ['<font color="red">所有设备状态列表:</font>']
	});

	/* 设置记录行的选择框 */
	var equ_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			rowselect : function(sm, row, rec) {
			}
		}
	});

	/* 单击事件 */
	var wait_equ_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			rowselect : function(sm, index, rec) {
				var rec = waitEquGrid.getStore().getAt(index);
				// alert(rec);
				if (typeof(rec) != "undefined") {
					runMethodDs.baseParams = {
						runKeyId : rec.get("runKeyId"),
						specialcode : specialcode,
						runlogId : runlogId
					};
					runMethodDs.load({
						params : {
							start : 0,
							limit :30
						}
					});
					runkeyid=rec.get("runKeyId");
				} else {
					alert('请选择要操作的信息!');
				}
			}
		}
	});

	// ===========变更详细信息列表Win============
	var toEquStatus = new Ext.data.Record.create([{
		name : 'runStatusHisId'
	}, {
		name : 'runLogno'
	}, {
		name : 'attributeCode'
	}, {
		name : 'fromStatusName'
	}, {
		name : 'toStatusName'
	}, {
		name : 'standingTime'
	}, {
		name : 'operaterBy'
	}, {
		name : 'operateTime'
	}, {
		name : 'operateMemo'
	}, {
		name : 'workerName'
	}]);

	var toEquStatus_ds = new Ext.data.Store({

		proxy : new Ext.data.HttpProxy({
			url : 'runlog/findEquStatusHis.action'
		}),
		reader : new Ext.data.JsonReader({}, toEquStatus)
	});
	// 加载数据之前传参数值
	toEquStatus_ds.on("beforeload", function() {
		var equcoderec = runMethodGrid.getSelectionModel().getSelected();
		Ext.apply(this.baseParams, {
			equCode : equcoderec.data.attributeCode
		});
	});

	var toEquStatus_cm = new Ext.grid.ColumnModel([{
		header : '设备运行记录ID',
		dataIndex : 'runStatusHisId',
		align : 'center',
		hidden : true
	}, {
		header : '运行日志号',
		dataIndex : 'runLogno',
		align : 'left'

	}, {
		header : '设备功能码',
		dataIndex : 'attributeCode',
		align : 'left',
		hidden : true
	}, {
		header : '原状态',
		dataIndex : 'fromStatusName',
		anchor : '80%',
		align : 'center'
	}, {
		header : '更改后状态',
		dataIndex : 'toStatusName',
		anchor : '80%',
		align : 'center'
	}, {
		header : '原状态持续时间',
		dataIndex : 'standingTime',
		anchor : '80%',
		align : 'center'
	}, {
		header : '操作人',
		dataIndex : 'workerName',
		anchor : '80%',
		align : 'center'
	}, {
		header : '操作时间',
		dataIndex : 'operateTime',
		anchor : '80%',
		align : 'center',
		// 日期格式转化
		renderer : function(val) {
			return val.substr(0, 10) + " " + val.substr(11);
		}
	}, {
		header : '备注',
		dataIndex : 'operateMemo',
		anchor : '80%',
		align : 'center'
	}]);

	var hbbar = new Ext.Toolbar({
		items : ['<font color="red">设备状态变更信息列表</font>']
	});

	var toEquStatusGrid = new Ext.grid.GridPanel({
		ds : toEquStatus_ds,
		cm : toEquStatus_cm,
		sm : equ_sm,
		tbar : hbbar,
		width : 600,
		fitToFrame : true,
		border : true,
		viewConfig : {
			forceFit : true
		}
	});

	var toEquStatusWin = new Ext.Window({
		width : 700,
		height : 400,
		modal : true,
		closeAction : 'hide',
		items : [toEquStatusGrid],
		layout : 'fit'
	});

	// ==========某个专业下的运行方式列表=============
	var waitequStatus = new Ext.data.Record.create([{
		name : 'runKeyId'
	}, {
		name : 'runWayName'
	}]);
	var wait_equ_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/findRunWayByProfession.action'
		}),
		method : 'post',
		reader : new Ext.data.JsonReader({
			root : 'data'
		}, waitequStatus)
	});
	wait_equ_ds.load({
		params : {
			specialcode : specialcode
		}
	});
	var wait_equ_cm = new Ext.grid.ColumnModel([{
		header : 'ID',
		dataIndex : 'runKeyId',
		hidden : true
	}, {
		header : '运行方式列表',
		dataIndex : 'runWayName',
		align : 'center'
	}]);
	wait_equ_cm.defaultSortable = true;

	var waitEquGrid = new Ext.grid.EditorGridPanel({
		// el : 'wait-equ-div',
		ds : wait_equ_ds,
		cm : wait_equ_cm,
		sm : wait_equ_sm,
		bbar : wbbar,
		width : 150,
		fitToFrame : true,
		border : false,
		viewConfig : {
			forceFit : true
		}
	});
	// waitEquGrid.render();

	var wbbar = new Ext.PagingToolbar({
		pageSize : 30,
		store : wait_equ_ds,
		displayInfo : true,
		displayMsg : '共 {2} 条',
		emptyMsg : "没有记录"
	})

	// ==========某种运行方式下的设备状态列表=============

	var runMethodDs = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/findShiftEquStatus.action',
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({
			root : 'root'
		}, [{
			name : 'shiftEqustatusId'
		}, {
			name : 'runLogid'
		}, {
			name : 'runLogno'
		}, {
			name : 'specialityCode'
		}, {
			name : 'attributeCode'
		}, {
			name : 'equName'
		}, {
			name : 'equStatusId'
		}, {
			name : 'equStatusName'
		}, {
			name : 'colorValue'
		}])
	});

	var runMethodSm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			rowselect : function(sm, row, rec) {
			}
		}
	});

	// 显示颜色
	function showColor(v) {
		return "<div  style='width:40; background:" + v
				+ "'>&nbsp;&nbsp;&nbsp;&nbsp;</div>";
	}

	var runMethodCm = new Ext.grid.ColumnModel([runMethodSm, {
		header : 'ID',
		id : 'shiftEquId',
		dataIndex : 'shiftEqustatusId',
		align : 'center',
		hidden : true
	}, {
		header : '运行日志流水号',
		dataIndex : 'runLogid',
		align : 'center',
		hidden : true
	}, {
		header : '运行日志号',
		dataIndex : 'runLogno',
		align : 'center',
		hidden : true
	}, {
		header : '专业编码',
		dataIndex : 'specialityCode',
		align : 'center',
		hidden : true
	}, {
		header : '设备功能码',
		dataIndex : 'attributeCode',
		align : 'center'
	}, {
		header : '设备名称',
		dataIndex : 'equName',
		align : 'center'
	}, {
		header : '设备状态ID',
		dataIndex : 'equStatusId',
		align : 'center',
		hidden : true
	}, {
		header : '状态',
		dataIndex : 'equStatusName',
		align : 'center'
	}, {
		header : '颜色',
		dataIndex : 'colorValue',
		renderer : showColor,
		align : 'center'
	}]);
	runMethodCm.defaultSortable = true;
	var runMethodbbar = new Ext.PagingToolbar({
		pageSize :30,
		store : runMethodDs,
		displayInfo : true,
		displayMsg : '共 {2} 条',
		emptyMsg : "没有记录"
	})

	// ===========模糊查询================

	var fuuzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});

	function queryRecord() {
		wait_equ_ds.load({
			params : {
				specialcode : specialcode
			}
		});
		var fuzzytext = Ext.get('fuzzy').dom.value;
		var runKeyId=runkeyid;
		runMethodDs.on("beforeload", function() {
			if (waitEquGrid.getSelectionModel().getSelected() != undefined)
			{
				var rec = waitEquGrid.getSelectionModel().getSelected();
				runKeyId=rec.get("runKeyId");
			}
			Ext.apply(this.baseParams, {
				fuzzy : fuzzytext,
				runKeyId :  runKeyId,
				runlogId : runlogId,
				specialcode : specialcode
			});
		});

		runMethodDs.load({
			params : {
				start : 0,
				limit : 30
			}
		});
	}
	var runMethodtbar = new Ext.Toolbar({
		items : ['<font color="red">设备名称:</font>', fuuzy, {
			id : 'btnReflesh',
			text : "查询",
			iconCls : 'query',
			handler : queryRecord
		}, '-', {
			id : 'btnUpdate',
			text : "编辑设备状态",
			iconCls : 'update',
			handler : function() {
				var rec = runMethodGrid.getSelectionModel().getSelections();
				if (rec.length != 1) {
					Ext.Msg.alert("提示", "请先选择要编辑的记录!");
					return false;
				} else {
					runMethodWin.show();
					runMethodForm.getForm().reset();
					var rec = runMethodGrid.getSelectionModel().getSelected();
					runwaystore.load();

					Ext.get("equStatus.runLogno").dom.value = rec
							.get("runLogno");
					Ext.get("equStatus.equName").dom.value = rec.get("equName");
					Ext.get("equStatus.equStatusName").dom.value = rec
							.get("equStatusName");
				}
			}
		}, '-', {
			id : 'btnUpdate',
			text : "变更详细信息",
			iconCls : 'update',
			handler : function() {
				var rec = runMethodGrid.getSelectionModel().getSelections();
				if (rec.length != 1) {
					Ext.Msg.alert("提示", "请先选择要查看的记录!");
					return false;
				} else {
					toEquStatusWin.show();
					toEquStatus_ds.load();
				}
			}
		}, '-', {
			id : 'btnUpdate',
			text : "接地线/闸刀记录",
			iconCls : 'update',
			handler : function() {
				var url = "earthrecord.jsp?runlogId=" + runlogId
						+ "&specialcode=" + specialcode;
				window.showModalDialog(url, '',
						'dialogWidth=700px;dialogHeight=500px;status=no;');
			}
		}]
	});

	var runMethodGrid = new Ext.grid.GridPanel({
		id : 'runMethodGrid',
		// el : 'equ-div',
		ds : runMethodDs,
		cm : runMethodCm,
		sm : runMethodSm,
		bbar : runMethodbbar,
		tbar : runMethodtbar,
		// autoWidth : true,
		// autoSizeColumns : true,
		width : 650,
		fitToFrame : true,
		border : false,
		viewConfig : {
			forceFit : true
		}
	});
	// runMethodGrid.render();
	runMethodGrid.on('rowdblclick', function(grid, rowIndex, e) {

		toEquStatusWin.show();
		toEquStatus_ds.load();
	});

	// ==========布局=========

	var panelleft = new Ext.Panel({
		region : 'west',
		layout : 'fit',
		width : 250,
		autoScroll : true,
		containerScroll : true,
		collapsible : true,
		split : true,
		items : [waitEquGrid]
	});

	var right = new Ext.Panel({
		region : "center",
		layout : 'fit',
		items : [runMethodGrid]
	});

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [panelleft, right]
	});

		// ===========================
});
