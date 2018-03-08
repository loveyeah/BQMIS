Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var mantype = "";
	var method = "add";
	var pageSize = 18;
	// 已存在的人员
	var str = '';

	function choseEdit(absManlistGrid, rowIndex, columnIndex, e) {
		var record = absManlistGrid.getStore().getAt(rowIndex);
		var fieldName = absManlistGrid.getColumnModel()
				.getDataIndex(columnIndex);

		if (fieldName == "absName" || fieldName == "atnName") {
			str = "";
			if (fieldName == "atnName") {
				absStore.load({
					params : {
						meetingId : westgrid.getSelectionModel().getSelected()
								.get("meetingInfo.meetingId")
					}
				});
				for (var i = 0; i < absStore.getCount(); i++) {
					if (absStore.getAt(i).get("absInfo.workerCode") != ""
							&& absStore.getAt(i).get("absInfo.workerCode") != null)
						str += "'"
								+ absStore.getAt(i).get("absInfo.workerCode")
								+ "',";
				}
				for (var i = 0; i < atnStore.getCount(); i++) {
					if (atnStore.getAt(i).get("atnInfo.workerCode") != ""
							&& atnStore.getAt(i).get("atnInfo.workerCode") != null)
						str += "'"
								+ atnStore.getAt(i).get("atnInfo.workerCode")
								+ "',";
				}
			} else if (fieldName == "absName") {
				atnStore.load({
					params : {
						meetingId : westgrid.getSelectionModel().getSelected()
								.get("meetingInfo.meetingId")
					}
				});
				for (var i = 0; i < atnStore.getCount(); i++) {
					if (atnStore.getAt(i).get("atnInfo.workerCode") != ""
							&& atnStore.getAt(i).get("atnInfo.workerCode") != null)
						str += "'"
								+ atnStore.getAt(i).get("atnInfo.workerCode")
								+ "',";
				}
				for (var i = 0; i < absStore.getCount(); i++) {
					if (absStore.getAt(i).get("absInfo.workerCode") != ""
							&& absStore.getAt(i).get("absInfo.workerCode") != null)
						str += "'"
								+ absStore.getAt(i).get("absInfo.workerCode")
								+ "',";
				}
			} else {
			}

			str = str.substring(0, str.length - 1);
			var args = {
				selectModel : 'signal',
				notIn : str,
				rootNode : {
					id : '-1',
					text : '灞桥电厂'
				}
			}
			var person = window
					.showModalDialog(
							'../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (typeof(person) != "undefined") {
				if (fieldName == "absName") {
					var record = absManlistGrid.getSelectionModel()
							.getSelected();
					record.set("absInfo.workerCode", person.workerCode);
					record.set("absName", person.workerName);
					record.set("absInfo.depCode", person.deptCode);
					record.set("absDep", person.deptName);
				} else if (fieldName == "atnName") {
					var record = atnManlistGrid.getSelectionModel()
							.getSelected();
					record.set("atnInfo.workerCode", person.workerCode);
					record.set("atnName", person.workerName);
					record.set("atnInfo.depCode", person.deptCode);
					record.set("atnDep", person.deptName);
				} else {
				}
			}
		}
		if (fieldName == "clickToChose") {
			choseSpare();
		}
	};

	// 参加人员
	var atnManName = {
		header : "人员",
		sortable : false,
		dataIndex : 'atnName',
		width : 50,
		editor : new Ext.form.TextField({
			readOnly : true
		})
	};

	var atnMan = new Ext.form.TextField({
		dataIndex : 'atnInfo.workerCode',
		hidden : true
	});

	var atnMeetingId = new Ext.form.TextField({
		dataIndex : 'atnInfo.meetingId',
		hidden : true
	});

	var atnManDepName = {
		header : "部门",
		width : 100,
		sortable : false,
		dataIndex : 'atnDep',
		width : 135
	};

	var atnManDep = new Ext.form.TextField({
		dataIndex : 'atnInfo.depCode',
		hidden : true
	});

	function addAtnRecords() {
		var count = atnStore.getCount();
		var currentIndex = count;
		var o = new atnStorelist({
			'atnInfo.attendId' : '',
			'atnInfo.meetingId' : meetingid.getValue(),
			'atnInfo.workerCode' : '',
			'atnInfo.depCode' : '',
			'atnName' : '',
			'atnDep' : ''
		});

		atnManlistGrid.stopEditing();
		atnStore.insert(currentIndex, o);
		atnSm.selectRow(currentIndex);
		atnManlistGrid.startEditing(currentIndex, 2);
	};

	var atnBtnAdd = new Ext.Button({
		text : '增加',
		iconCls : 'add',
		handler : function() {
			addAtnRecords();
		}
	});

	var closeBtn = new Ext.Button({
		text : '关闭',
		iconCls : 'close',
		handler : function() {
			var modifyRec = atnManlistGrid.getStore().getModifiedRecords();
			if (modifyRec.length > 0 || ids.length > 0) {
				Ext.Msg.confirm('提示信息', '是否放弃修改？', function(button, text) {
					if (button == 'yes') {
						atnStore.rejectChanges();
						ids = [];
						atnManlistWindow.hide();
					}
				});
			}else{
				atnManlistWindow.hide();
			}
		}
	});
	var ids = new Array();
	var atnBtnDel = new Ext.Button({
		text : '删除',
		iconCls : 'delete',
		handler : function() {
			atnManlistGrid.stopEditing();
			var atnSm = atnManlistGrid.getSelectionModel();
			var selected = atnSm.getSelections();
			if (selected.length == 0) {
				Ext.Msg.alert("提示", "请选择要删除的记录！");
			} else {
				for (var i = 0; i < selected.length; i += 1) {
					var member = selected[i];
					if (member.get("atnInfo.attendId") != null) {
						ids.push(member.get("atnInfo.attendId"));
					}
					atnManlistGrid.getStore().remove(member);
					atnManlistGrid.getStore().getModifiedRecords()
							.remove(member);
				}
			}
		}
	});

	var atnBtnSav = new Ext.Button({
		text : '保存',
		iconCls : 'save',
		handler : function() {
			atnManlistGrid.stopEditing();
			var modifyRec = atnManlistGrid.getStore().getModifiedRecords();
			var doIf;
			if (modifyRec.length > 0 || ids.length > 0) {
				Ext.Msg.confirm('提示信息', '是否确定修改？', function(button, text) {
					if (button == 'yes') {
						var updateData = new Array();
						for (var i = 0; i < modifyRec.length; i++) {
							updateData.push(modifyRec[i].data);
						}

						Ext.Ajax.request({
							url : 'security/saveSafeMeetingAtnList.action',
							method : 'post',
							params : {
								isUpdate : Ext.util.JSON.encode(updateData),
								isDelete : ids.join(","),
								meetingId : westgrid.getSelectionModel()
										.getSelected()
										.get("meetingInfo.meetingId")
							},
							success : function(result, request) {
								var o = eval('(' + result.responseText + ')');
								Ext.MessageBox.alert('提示信息', o.msg);
								atnStore.rejectChanges();
								ids = [];
								atnStore.load({
									params : {
										meetingId : westgrid
												.getSelectionModel()
												.getSelected()
												.get("meetingInfo.meetingId")
									}
								});
							},
							failure : function(result, request) {
								Ext.MessageBox.alert('提示信息', '未知错误！')
							}
						})
					}
				});
			} else {
				Ext.MessageBox.alert('提示信息', '没有做任何修改！')
			}
		}
	});

	var atnSm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});

	var atnStorelist = new Ext.data.Record.create([atnSm, {
		name : 'atnInfo.attendId'
	}, {
		name : 'atnInfo.meetingId'
	}, {
		name : 'atnInfo.workerCode'
	}, {
		name : 'atnInfo.depCode'
	}, {
		name : 'atnDep'
	}, {
		name : 'atnName'
	}]);

	var atnStore = new Ext.data.JsonStore({
		url : 'security/getSafeMeetingAtnList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : atnStorelist
	});

	// 参加人员Grid
	var atnManlistGrid = new Ext.grid.EditorGridPanel({
		store : atnStore,
		columns : [new Ext.grid.RowNumberer(), atnManName, atnMan,
				atnMeetingId, atnManDepName, atnManDep],
		viewConfig : {
			forceFit : true
		},
		tbar : [atnBtnAdd, atnBtnDel, atnBtnSav, closeBtn],
		sm : atnSm,
		frame : true,
		enableColumnHide : false,
		enableColumnMove : false,
		iconCls : 'icon-grid'
	});

	atnManlistGrid.on('celldblclick', choseEdit);

	var atnManlistWindow = new Ext.Window({
		title : '参加人员名单',
		layout : 'fit',
		width : 400,
		height : 450,
		// closable : true,
		closable : false,
		border : false,
		resizable : false,
		closeAction : 'hide',
		modal : true,
		plain : true,
		buttonAlign : 'center',
		items : [atnManlistGrid]
	});

	// ↑↑参加人员

	var absManName = {
		header : "人员",
		sortable : false,
		dataIndex : 'absName',
		width : 50,
		editor : new Ext.form.TextField({
			readOnly : true,
			allowBlank : false
		})
	};

	var absMan = new Ext.form.TextField({
		dataIndex : 'absInfo.workerCode',
		hidden : true
	});

	var absMeetingId = new Ext.form.TextField({
		dataIndex : 'absInfo.meetingId',
		hidden : true
	});

	var absManDepName = {
		header : "部门",
		width : 100,
		sortable : false,
		dataIndex : 'absDep',
		width : 135
	};

	var absManDep = new Ext.form.TextField({
		dataIndex : 'absInfo.depCode',
		hidden : true
	});

	var absReason = {
		header : "缺席原因",
		dataIndex : 'absInfo.reason',
		editor : new Ext.form.TextArea({
			maxlength : 100
		})
	};

	var absRecord = {
		header : "补课记录",
		dataIndex : 'absInfo.makeupRecord',
		editor : new Ext.form.TextArea({
			maxlength : 200
		})
	};

	function addAbsRecords() {
		if (westgrid.getSelectionModel().getSelected() != null) {
			var count = absStore.getCount();
			var currentIndex = count;
			var o = new absStorelist({
				'absInfo.absenceId' : '',
				'absInfo.meetingId' : meetingid.getValue(),
				'absInfo.workerCode' : '',
				'absInfo.depCode' : '',
				'absInfo.reason' : '',
				'absInfo.makeupRecord' : '',
				'absName' : '',
				'absDep' : ''
			});
			absManlistGrid.stopEditing();
			absStore.insert(currentIndex, o);
			absSm.selectRow(currentIndex);
			absManlistGrid.startEditing(currentIndex, 2);
		} else {
			Ext.MessageBox.alert('错误', '请先选择主记录！');
		}
	};

	// 缺席人员
	var absBtnAdd = new Ext.Button({
		text : '增加',
		iconCls : 'add',
		handler : function() {
			addAbsRecords();
		}
	});
	var absBtnclose = new Ext.Button({
		text : '关闭',
		iconCls : 'close',
		handler : function() {
			var modifyRec = atnManlistGrid.getStore().getModifiedRecords();
			if (modifyRec.length > 0 || ids.length > 0) {
				Ext.Msg.confirm('提示信息', '是否放弃修改？', function(button, text) {
					if (button == 'yes') {
						absStore.rejectChanges();
						ids = [];
						absManlistWindow.hide();
					}
				});
			}else{
				absManlistWindow.hide();
			}
		}
	});

	var ids = new Array();
	var absBtnDel = new Ext.Button({
		text : '删除',
		iconCls : 'delete',
		handler : function() {
			absManlistGrid.stopEditing();
			var absSm = absManlistGrid.getSelectionModel();
			var selected = absSm.getSelections();
			if (selected.length == 0) {
				Ext.Msg.alert("提示", "请选择要删除的记录！");
			} else {
				for (var i = 0; i < selected.length; i += 1) {
					var member = selected[i];
					if (member.get("absInfo.absenceId") != null) {
						ids.push(member.get("absInfo.absenceId"));
					}
					absManlistGrid.getStore().remove(member);
					absManlistGrid.getStore().getModifiedRecords()
							.remove(member);
				}
			}
		}
	});

	var absBtnSav = new Ext.Button({
		text : '保存',
		iconCls : 'save',
		handler : function() {
			absManlistGrid.stopEditing();
			var modifyRec = absManlistGrid.getStore().getModifiedRecords();
			var doIf;
			if (modifyRec.length > 0 || ids.length > 0) {
				Ext.Msg.confirm('提示信息', '是否确定修改？', function(button, text) {
					if (button == 'yes') {
						var updateData = new Array();
						for (var i = 0; i < modifyRec.length; i++) {
							updateData.push(modifyRec[i].data);
						}

						Ext.Ajax.request({
							url : 'security/saveSafeMeetingAbsList.action',
							method : 'post',
							params : {
								isUpdate : Ext.util.JSON.encode(updateData),
								isDelete : ids.join(","),
								meetingId : westgrid.getSelectionModel()
										.getSelected()
										.get("meetingInfo.meetingId")
							},
							success : function(result, request) {
								var o = eval('(' + result.responseText + ')');
								Ext.MessageBox.alert('提示信息', o.msg);
								if (o.success) {
									absStore.rejectChanges();
									ids = [];
									absStore.load({
										params : {
											meetingId : westgrid
													.getSelectionModel()
													.getSelected()
													.get("meetingInfo.meetingId")
										}
									});
								}
							},
							failure : function(result, request) {
								Ext.MessageBox.alert('提示信息', '未知错误！')
							}
						})
					}
				});
			} else {
				Ext.MessageBox.alert('提示信息', '没有做任何修改！')
			}
		}
	});

	var absSm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});

	var absStorelist = new Ext.data.Record.create([absSm, {
		name : 'absInfo.absenceId'
	}, {
		name : 'absInfo.meetingId'
	}, {
		name : 'absInfo.workerCode'
	}, {
		name : 'absInfo.depCode'
	}, {
		name : 'absInfo.reason'
	}, {
		name : 'absInfo.makeupRecord'
	}, {
		name : 'absName'
	}, {
		name : 'absDep'
	}]);

	var absStore = new Ext.data.JsonStore({
		url : 'security/getSafeMeetingAbsList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : absStorelist
	});

	// 缺席人员Grid
	var absManlistGrid = new Ext.grid.EditorGridPanel({
		store : absStore,
		columns : [new Ext.grid.RowNumberer(), absManName, absMan,
				absMeetingId, absManDepName, absManDep, absReason, absRecord],
		viewConfig : {
			forceFit : true
		},
		tbar : [absBtnAdd, absBtnDel, absBtnSav,absBtnclose],
		sm : absSm,
		frame : true,
		enableColumnHide : false,
		enableColumnMove : false,
		iconCls : 'icon-grid'
	});

	absManlistGrid.on('celldblclick', choseEdit);

	var absManlistWindow = new Ext.Window({
		title : '缺席人员名单',
		layout : 'fit',
		width : 400,
		height : 450,
//		closable : true,
		closable : false,
		border : false,
		resizable : false,
		closeAction : 'hide',
		modal : true,
		plain : true,
		buttonAlign : 'center',
		items : [absManlistGrid]
	});

	// ↑↑缺席人员

	var yeardata = '';
	var myDate = new Date();
	var myMonth = myDate.getMonth() + 1;

	myMonth = (myMonth < 10 ? "0" + myMonth : myMonth);

	for (var i = 2004; i < myDate.getFullYear() + 2; i++) {
		if (i < myDate.getFullYear() + 1)
			yeardata += '[' + i + ',' + i + '],';
		else
			yeardata += '[' + i + ',' + i + ']';
	}
	var yeardata = eval('[' + yeardata + ']');

	var meetingYear = new Ext.form.ComboBox({
		xtype : "combo",
		store : new Ext.data.SimpleStore({
			fields : ['value', 'key'],
			data : yeardata
		}),
		id : 'itemType',
		name : 'itemType',
		valueField : "key",
		displayField : "value",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'statItem.itemType',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		width : 80,
		value : myDate.getFullYear()
	});

	var westbtnAdd = new Ext.Button({
		text : '查询',
		iconCls : 'query',
		handler : function() {
			westStore.load({
				params : {
					start : 0,
					limit : pageSize
				}
			});
		}
	});

	var westSm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});

	var westStorelist = new Ext.data.Record.create([westSm, {
		name : 'meetingInfo.meetingId'
	}, {
		name : 'depName'
	}, {
		name : 'meetingDateString'
	}, {
		name : 'moderatorName'
	}]);

	var westStore = new Ext.data.JsonStore({
		url : 'security/getSafeMeetingList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : westStorelist
	});

	westStore.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			meetingDate : meetingYear.getValue()
		});
	});

	// 左侧Grid
	var westgrid = new Ext.grid.GridPanel({
		store : westStore,
		columns : [new Ext.grid.RowNumberer(), {
			header : "部门",
			width : 150,
			sortable : false,
			dataIndex : 'depName',
			region : 'center'
		}, {
			header : "时间",
			width : 90,
			sortable : false,
			dataIndex : 'meetingDateString',
			align : 'center'
		}, {
			header : "主持人",
			width : 80,
			sortable : false,
			dataIndex : 'moderatorName',
			align : 'center'
		}],
		viewConfig : {
			forceFit : true
		},
		tbar : ['会议时间 ', meetingYear, westbtnAdd],
		sm : westSm,
		bbar : new Ext.PagingToolbar({
			pageSize : pageSize,
			store : westStore,
			displayInfo : true,
			displayMsg : "{2}条记录",
			emptyMsg : "没有记录"
		}),
		frame : true,
		enableColumnHide : false,
		enableColumnMove : false,
		iconCls : 'icon-grid'
	});

	westgrid.on('rowclick', function(grid, rowIndex, e) {
		Ext.Ajax.request({
			url : 'security/getSafeMeeting.action',
			params : {
				meetingId : grid.getStore().getAt(rowIndex)
						.get("meetingInfo.meetingId")
			},
			method : 'post',
			waitMsg : '正在加载数据...',
			success : function(result, request) {
				var o = eval('(' + result.responseText + ')');
				meetingid.setValue(grid.getStore().getAt(rowIndex)
						.get("meetingInfo.meetingId"));
				timeSelect.setValue(o.meetingDateString != null
						? o.meetingDateString
						: "");
				department.setValue(o.depName != null ? o.depName : "");
				depCode.setValue(o.meetingInfo.depCode != null
						? o.meetingInfo.depCode
						: "");
				controlMan.setValue(o.moderatorName != null
						? o.moderatorName
						: "");
				moderator.setValue(o.meetingInfo.moderator != null
						? o.meetingInfo.moderator
						: "");
				recordMan
						.setValue(o.recordByName != null ? o.recordByName : "");
				recordBy.setValue(o.meetingInfo.recordBy != null
						? o.meetingInfo.recordBy
						: "");
				place.setValue(o.meetingInfo.meetingAddress != null
						? o.meetingInfo.meetingAddress
						: "");
				keyWord.setValue(o.meetingInfo.content != null
						? o.meetingInfo.content
						: "");
				memo.setValue(o.meetingInfo.memo != null
						? o.meetingInfo.memo
						: "");
				method = "update";
				eastbtnSav.setText("修改");
			},
			failure : function(result, request) {
				Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
			}
		});
	});

	westStore.load({
		params : {
			meetingDate : myDate.getFullYear(),
			start : 0,
			limit : pageSize
		}
	});

	var eastbtnAdd = new Ext.Button({
		text : '增加',
		iconCls : 'add',
		handler : function() {
			eastform.getForm().reset();
			method = "add";
			eastbtnSav.setText("保存");
			timeSelect.setValue(myDate.getFullYear() + "-" + myMonth);
		}
	});

	var eastbtnDel = new Ext.Button({
		text : '删除',
		iconCls : 'delete',
		handler : function() {
			if (meetingid.getValue() != null && meetingid.getValue() != "")
				eastform.getForm().submit({
					method : 'post',
					url : 'security/deleteSafeMeeting.action',
					success : function(form, action) {
						westStore.load({
							params : {
								start : 0,
								limit : pageSize
							}
						});
						eastform.getForm().reset();
						method = "add";
						eastbtnSav.setText("保存");
						timeSelect.setValue(myDate.getFullYear() + "-"
								+ myMonth);
					},
					failure : function(form, action) {
						Ext.MessageBox.alert('错误', o.msg);
					}
				})
		}
	});

	var eastbtnSav = new Ext.Button({
		text : '保存',
		iconCls : 'save',
		handler : function() {
			if (timeSelect.getValue() == null || timeSelect.getValue() == "") {
				Ext.MessageBox.alert('错误', '日期不可为空！');
				return false;
			}
			if (eastform.getForm().isValid()) {
				eastform.getForm().submit({
					method : 'post',
					url : 'security/saveSafeMeeting.action',
					params : {
						method : method
					},
					success : function(form, action) {
						meetingYear.setValue(timeSelect.getValue().substring(0,
								4));
						westStore.load({
							params : {
								meetingDate : timeSelect.getValue(),
								start : 0,
								limit : pageSize
							}
						});
						eastform.getForm().reset();
						method = "add";
						eastbtnSav.setText("保存");
						timeSelect.setValue(myDate.getFullYear() + "-"
								+ myMonth);
					},
					failure : function(form, action) {
						var o = eval('(' + action.response.responseText + ')');
						Ext.MessageBox.alert('错误', o.msg);
					}
				})
			}
		}
	});

	var eastbtnAtn = new Ext.Button({
		text : '参加人员',
		iconCls : 'save',
		handler : function() {
			if (meetingid.getValue() != null && meetingid.getValue() != "") {
				atnStore.removeAll();
				atnManlistWindow.show();
				atnStore.load({
					params : {
						meetingId : meetingid.getValue()
					}
				});
			} else
				Ext.MessageBox.alert('错误', '请先选择主记录！');
		}
	});

	var eastbtnAbs = new Ext.Button({
		text : '缺席人员',
		iconCls : 'save',
		handler : function() {
			if (meetingid.getValue() != null && meetingid.getValue() != "") {
				absStore.removeAll();
				absManlistWindow.show();
				absStore.load({
					params : {
						meetingId : meetingid.getValue()
					}
				});
			} else
				Ext.MessageBox.alert('错误', '请先选择主记录！');
		}
	});

	var meetingid = new Ext.form.Hidden({
		id : 'meetingInfo.meetingId',
		name : 'meetingInfo.meetingId'
	});

	var timeSelect = new Ext.form.TextField({
		id : 'meetingDate',
		fieldLabel : "时间",
		name : 'meetingDate',
		style : 'cursor:pointer',
		readOnly : true,
		anchor : '95%',
		allowBlank : true,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M',
					dateFmt : 'yyyy-MM',
					alwaysUseStartDate : true
				});
				this.blur();
			}
		}
	});

	function selectDept() {
		var args = {
			selectModel : 'single',
			rootNode : {
				id : '0',
				text : '合肥电厂'
			},
			onlyLeaf : false
		};
		var dept = window
				.showModalDialog(
						'../../../comm/jsp/hr/dept/dept.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(dept) != "undefined") {
			depCode.setValue(dept.codes);
			department.setValue(dept.names);
		}
	}

	var department = new Ext.form.ComboBox({
		id : 'depName',
		name : 'depName',
		fieldLabel : "部门",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		anchor : '95%',
		onTriggerClick : function() {
			selectDept();
		}
	});

	var depCode = new Ext.form.Hidden({
		id : 'meetingInfo.depCode',
		name : 'meetingInfo.depCode'
	});

	var controlMan = new Ext.form.ComboBox({
		id : 'moderatorName',
		name : 'moderatorName',
		fieldLabel : "主持人",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		anchor : '95%',
		onTriggerClick : function() {
			mantype = "moderator"
			selectPersonWin();
		}
	});

	var moderator = new Ext.form.Hidden({
		id : 'meetingInfo.moderator',
		name : 'meetingInfo.moderator'
	});

	var recordMan = new Ext.form.ComboBox({
		id : 'recordByName',
		name : 'recordByName',
		fieldLabel : "记录人",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : true,
		anchor : '95%',
		onTriggerClick : function() {
			mantype = "recordBy"
			selectPersonWin();
		}
	});

	var recordBy = new Ext.form.Hidden({
		id : 'meetingInfo.recordBy',
		name : 'meetingInfo.recordBy'
	});

	function selectPersonWin() {
		var args = {
			selectModel : 'signal',
			notIn : "",
			rootNode : {
				id : '-1',
				text : '合肥电厂'
			}
		}
		var person = window
				.showModalDialog(
						'../../../comm/jsp/hr/workerByDept/workerByDept2.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(person) != "undefined") {
			if (mantype == "moderator") {
				controlMan.setValue(person.workerName);
				moderator.setValue(person.workerCode);
			} else if (mantype == "recordBy") {
				recordMan.setValue(person.workerName);
				recordBy.setValue(person.workerCode);
			} else if (mantype == "atn") {
			} else if (mantype == "abs") {
			} else {
			}
		}
	}

	var place = new Ext.form.TextField({
		name : 'meetingInfo.meetingAddress',
		xtype : 'textfield',
		readOnly : false,
		fieldLabel : '地点',
		anchor : '97%'
	});

	var keyWord = new Ext.form.TextArea({
		id : 'meetingInfo.content',
		xtype : 'textarea',
		fieldLabel : '纪要',
		readOnly : false,
		allowBlank : false,
		height : 120,
		anchor : '97%'
	});

	var memo = new Ext.form.TextArea({
		id : 'meetingInfo.memo',
		xtype : 'textarea',
		fieldLabel : '备注',
		readOnly : false,
		height : 80,
		anchor : '97%'
	});

	var formContent = new Ext.form.FieldSet({
		border : false,
		layout : 'column',
		items : [{
			columnWidth : .5,
			layout : 'form',
			border : false,
			labelWidth : 50,
			items : [timeSelect, controlMan]
		}, {
			columnWidth : .5,
			layout : 'form',
			labelWidth : 50,
			border : false,
			items : [department, recordMan]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 1,
			labelWidth : 50,
			items : [place]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 1,
			labelWidth : 50,
			items : [keyWord]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 1,
			labelWidth : 50,
			items : [memo, depCode, moderator, recordBy, meetingid]
		}]
	});

	// 右侧Form
	var eastform = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		labelWidth : 50,
		autoHeight : false,
		region : 'center',
		border : false,
		tbar : [eastbtnAdd, {
			xtype : "tbseparator"
		}, eastbtnDel, {
			xtype : "tbseparator"
		}, eastbtnSav, {
			xtype : "tbseparator"
		}, eastbtnAtn, {
			xtype : "tbseparator"
		}, eastbtnAbs],
		items : [formContent]
	});

	// ↑↑主窗口部件

	var layout = new Ext.Viewport({
		layout : "border",
		border : false,
		items : [{
			title : '会议信息',
			region : "center",
			split : true,
			collapsible : false,
			titleCollapse : true,
			margins : '1',
			layoutConfig : {
				animate : true
			},
			layout : 'fit',
			items : [eastform]
		}, {
			title : '会议列表',
			region : 'west',
			split : true,
			collapsible : true,
			titleCollapse : true,
			margins : '1',
			width : 320,
			layoutConfig : {
				animate : true
			},
			layout : 'fit',
			items : [westgrid]
		}]
	});

	timeSelect.setValue(myDate.getFullYear() + "-" + myMonth);
});