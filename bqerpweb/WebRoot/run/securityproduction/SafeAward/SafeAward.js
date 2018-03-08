Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var mantype = "";
	var method = "add";
	var pageSize = 18;
	// 已存在的人员
	str = "";

	function moneyFormat(v) {
		if (v == null || v == "") {
			return "0.00";
		}
		v = (Math.round((v - 0) * 100)) / 100;
		v = (v == Math.floor(v)) ? v + ".00" : ((v * 10 == Math.floor(v * 10))
				? v + "0"
				: v);

		v = String(v);
		var ps = v.split('.');
		var whole = ps[0];
		var sub = ps[1] ? '.' + ps[1] : '.00';
		var r = /(\d+)(\d{3})/;
		while (r.test(whole)) {
			whole = whole.replace(r, '$1' + ',' + '$2');
		}
		v = whole + sub;
		if (v.charAt(0) == '-') {
			return '-' + v.substr(1);
		}
		return "￥" + v;
	};

	// 人员列表

	var encourageManName = {
		header : "获奖人",
		sortable : false,
		dataIndex : 'encourageManName',
		editor : new Ext.form.TextField({
					readOnly : true
				})
	}

	function choseEdit(absManlistGrid, rowIndex, columnIndex, e) {
		var record = absManlistGrid.getStore().getAt(rowIndex);
		var fieldName = absManlistGrid.getColumnModel()
				.getDataIndex(columnIndex);
		if (fieldName == "encourageManName") {
			str = "";
			for (var i = 0; i < absStore.getCount(); i++) {
				if (absStore.getAt(i).get("awardDetailsInfo.encourageMan") != ""
						&& absStore.getAt(i)
								.get("awardDetailsInfo.encourageMan") != null)
					str += "'"
							+ absStore.getAt(i)
									.get("awardDetailsInfo.encourageMan")
							+ "',";
			}

			str = str.substring(0, str.length - 1);
			var args = {
				selectModel : 'signal',
				notIn : str,
				rootNode : {
					id : '-1',
					text : '合肥电厂'
				}
			}
			var person = window
					.showModalDialog(
							'../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (typeof(person) != "undefined") {
				var record = absManlistGrid.getSelectionModel().getSelected();
				record.set("awardDetailsInfo.encourageMan", person.workerCode);
				record.set("encourageManName", person.workerName);
				record.set("encourageManDep", person.deptName);
			}
		}
		if (fieldName == "clickToChose") {
			choseSpare();
		}
	};

	var encourageMan = new Ext.form.TextField({
				dataIndex : 'awardDetailsInfo.encourageMan',
				hidden : true
			});

	var encourageWay = {
		header : "奖励方式",
		dataIndex : 'awardDetailsInfo.encourageWay',
		editor : new Ext.form.TextField()
	};

	var encourageMoney = {
		header : "金额（元）",
		align : 'right',
		dataIndex : 'awardDetailsInfo.encourageMoney',
		editor : new Ext.form.NumberField({
					maxValue : 999999.99,
					minValue : 0,
					decimalPrecision : 2
				}),
		renderer : function(value, cellmeta, record, rowIndex, columnIndex,
				store) {
			if (rowIndex < store.getCount()) {
				return moneyFormat(value)
			}
		}
	};

	function addRecords() {
		if (westgrid.getSelectionModel().getSelected() != null) {
			var count = absStore.getCount();
			var currentIndex = count;
			var o = new absStorelist({
						'awardDetailsInfo.awardDetailsId' : '',
						'awardDetailsInfo.safeawardId' : westgrid
								.getSelectionModel().getSelected()
								.get("awardInfo.awardId"),
						'awardDetailsInfo.encourageMan' : '',
						'awardDetailsInfo.encourageWay' : count > 0
								? '（同上）'
								: '',
						'awardDetailsInfo.encourageMoney' : '',
						'encourageManName' : '',
						'encourageManDep' : ''
					});

			absManlistGrid.stopEditing();
			absStore.insert(currentIndex, o);
			absSm.selectRow(currentIndex);
			absManlistGrid.startEditing(currentIndex, 2);
		} else {
			Ext.MessageBox.alert('错误', '请先选择主记录！');
		}
	};

	var absBtnAdd = new Ext.Button({
				text : '增加',
				iconCls : 'add',
				handler : function() {
					if (awardid.getValue() != null && awardid.getValue() != "")
						addRecords();
					else
						Ext.MessageBox.alert('错误', '请先选择主记录！');
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
					if (member.get("awardDetailsInfo.awardDetailsId") != null) {
						ids.push(member.get("awardDetailsInfo.awardDetailsId"));
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
									url : 'security/saveSafeAwardDetailsList.action',
									method : 'post',
									params : {
										isUpdate : Ext.util.JSON
												.encode(updateData),
										isDelete : ids.join(","),
										awardId : westgrid.getSelectionModel()
												.getSelected()
												.get("awardInfo.awardId")
									},
									success : function(result, request) {
										var o = eval('(' + result.responseText
												+ ')');
										Ext.MessageBox.alert('提示信息', o.msg);
										if (o.success) {
											absStore.rejectChanges();
											ids = [];
											absStore.reload();
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
				name : 'awardDetailsInfo.awardDetailsId'
			}, {
				name : 'awardDetailsInfo.safeawardId'
			}, {
				name : 'awardDetailsInfo.encourageMan'
			}, {
				name : 'awardDetailsInfo.encourageWay'
			}, {
				name : 'awardDetailsInfo.encourageMoney'
			}, {
				name : 'encourageManName'
			}, {
				name : 'encourageManDep'
			}]);

	var absStore = new Ext.data.JsonStore({
				url : 'security/getSafeAwardDetailsList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : absStorelist
			});

	// 缺席人员Grid
	var absManlistGrid = new Ext.grid.EditorGridPanel({
				store : absStore,
				columns : [new Ext.grid.RowNumberer(), encourageManName,
						encourageMan, {
							header : "所在部门",
							width : 100,
							sortable : false,
							dataIndex : 'encourageManDep'
						}, encourageWay, encourageMoney],
				viewConfig : {
					forceFit : true
				},
				tbar : [absBtnAdd, absBtnDel, absBtnSav],
				sm : absSm,
				frame : true,
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});

	absManlistGrid.on('celldblclick', choseEdit);
	// 人员列表

	var yeardata = '';
	var myDate = new Date();

	var myMonth = myDate.getMonth() + 1;

	myMonth = (myMonth < 10 ? "0" + myMonth : myMonth);

	var myDay = myDate.getDate();

	myDay = (myDay < 10 ? "0" + myDay : myDay);

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
				allowBlank : true,
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
				name : 'awardInfo.awardId'
			}, {
				name : 'awardInfo.encourageItem'
			}, {
				name : 'encourageDateString'
			}]);

	var westStore = new Ext.data.JsonStore({
				url : 'security/getSafeAwardList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : westStorelist
			});

	// 左侧Grid
	var westgrid = new Ext.grid.GridPanel({
				store : westStore,
				columns : [new Ext.grid.RowNumberer(), {
							header : "奖励项目",
							width : 230,
							sortable : false,
							dataIndex : 'awardInfo.encourageItem'
						}, {
							header : "奖励时间",
							width : 90,
							sortable : false,
							dataIndex : 'encourageDateString'
						}],
				viewConfig : {
					forceFit : true
				},
				tbar : ['奖励时间 ', meetingYear, westbtnAdd],
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
							url : 'security/getSafeAward.action',
							params : {
								awardId : grid.getStore().getAt(rowIndex)
										.get("awardInfo.awardId")
							},
							method : 'post',
							waitMsg : '正在加载数据...',
							success : function(result, request) {
								var o = eval('(' + result.responseText + ')');

								awardid.setValue(grid.getStore()
										.getAt(rowIndex)
										.get("awardInfo.awardId"));
								timeSelect
										.setValue(o.encourageDateString != null
												? o.encourageDateString
												: "");
								approvalMan.setValue(o.approvalByName != null
										? o.approvalByName
										: "");
								approvalBy
										.setValue(o.awardInfo.approvalBy != null
												? o.awardInfo.approvalBy
												: "");
								completeMan.setValue(o.completeByName != null
										? o.completeByName
										: "");
								completeBy
										.setValue(o.awardInfo.completeBy != null
												? o.awardInfo.completeBy
												: "");
								place
										.setValue(o.awardInfo.encourageItem != null
												? o.awardInfo.encourageItem
												: "");
								memo
										.setValue(o.awardInfo.encourageReason != null
												? o.awardInfo.encourageReason
												: "");

								method = "update";
								eastbtnSav.setText("修改");
								absStore.load({
											params : {
												awardId : grid
														.getStore()
														.getAt(rowIndex)
														.get("awardInfo.awardId")
											}
										});
							},
							failure : function(result, request) {
								Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
							}
						});
			});

	westStore.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							awardDate : meetingYear.getValue()
						});
			});

	westStore.load({
				params : {
					awardDate : myDate.getFullYear(),
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
					completeMan.setValue(currentUserName);
					completeBy.setValue(currentUser);
					timeSelect.setValue(myDate.getFullYear() + "-" + myMonth
							+ "-" + myDay);
					absStore.removeAll();
				}
			});

	var eastbtnDel = new Ext.Button({
				text : '删除',
				iconCls : 'delete',
				handler : function() {
					if (awardid.getValue() != null && awardid.getValue() != "")
						eastform.getForm().submit({
							method : 'post',
							url : 'security/deleteSafeAward.action',
							success : function(form, action) {
								westStore.load({
											params : {
												start : 0,
												limit : pageSize
											}
										});
								eastform.getForm().reset();
								completeMan.setValue(currentUserName);
								completeBy.setValue(currentUser);
								timeSelect.setValue(myDate.getFullYear() + "-"
										+ myMonth + "-" + myDay);
								method = "add";
								eastbtnSav.setText("保存");
								absStore.removeAll();
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
					if (timeSelect.getValue() == null
							|| timeSelect.getValue() == "") {
						Ext.MessageBox.alert('错误', '日期不可为空！');
						return false;
					}
					if (eastform.getForm().isValid()) {
						eastform.getForm().submit({
							method : 'post',
							url : 'security/saveSafeAward.action',
							params : {
								method : method
							},
							success : function(form, action) {
								meetingYear.setValue(timeSelect.getValue()
										.substring(0, 4));
								westStore.load({
											params : {
												awardDate : timeSelect
														.getValue(),
												start : 0,
												limit : pageSize
											}
										});
								eastform.getForm().reset();
								completeMan.setValue(currentUserName);
								completeBy.setValue(currentUser);
								timeSelect.setValue(myDate.getFullYear() + "-"
										+ myMonth + "-" + myDay);
								method = "add";
								eastbtnSav.setText("保存");
								absStore.removeAll();
							},
							failure : function(form, action) {
								var o = eval('(' + action.response.responseText
										+ ')');
								Ext.MessageBox.alert('错误', o.msg);
							}
						})
					}
				}
			});

	var awardid = new Ext.form.Hidden({
				id : 'awardInfo.awardId',
				name : 'awardInfo.awardId'
			});

	var timeSelect = new Ext.form.TextField({
				id : 'awardDate',
				fieldLabel : "奖励时间",
				name : 'awardDate',
				style : 'cursor:pointer',
				readOnly : true,
				anchor : '95%',
				allowBlank : true,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : true
								});
						this.blur();
					}
				}
			});

	var approvalMan = new Ext.form.ComboBox({
				id : 'approvalByName',
				name : 'approvalByName',
				fieldLabel : "批准人",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : true,
				anchor : '95%',
				onTriggerClick : function() {
					mantype = "approvalBy"
					selectPersonWin();
				}
			});

	var approvalBy = new Ext.form.Hidden({
				id : 'awardInfo.approvalBy',
				name : 'awardInfo.approvalBy'
			});

	var completeMan = new Ext.form.TextField({
				id : 'completeByName',
				name : 'completeByName',
				fieldLabel : "填写人",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				anchor : '95%'
			});

	var completeBy = new Ext.form.Hidden({
				id : 'awardInfo.completeBy',
				name : 'awardInfo.completeBy'
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
						'../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(person) != "undefined") {
			if (mantype == "approvalBy") {
				approvalMan.setValue(person.workerName);
				approvalBy.setValue(person.workerCode);
			} else if (mantype == "completeBy") {
				completeMan.setValue(person.workerName);
				completeBy.setValue(person.workerCode);
			} else if (mantype == "encourageTo") {
				var record = absManlistGrid.getSelectionModel().getSelected();
				record.set("awardDetailsInfo.encourageMan", person.workerName);
				record.set("encourageManName", person.workerName);
			} else {
				Ext.MessageBox.alert('错误', '未指定的操作！');
			}
		}
	}

	var place = new Ext.form.TextField({
				name : 'awardInfo.encourageItem',
				xtype : 'textfield',
				readOnly : false,
				fieldLabel : '奖励项目',
				anchor : '97%',
				allowBlank : false
			});

	var memo = new Ext.form.TextArea({
				id : 'awardInfo.encourageReason',
				xtype : 'textarea',
				fieldLabel : '奖励原因',
				readOnly : false,
				height : 80,
				anchor : '97%'
			});

	var formContent = new Ext.form.FieldSet({
				border : false,
				layout : 'column',
				items : [{
							border : false,
							layout : 'form',
							columnWidth : 1,
							labelWidth : 70,
							items : [place]
						}, {
							border : false,
							layout : 'form',
							columnWidth : 1,
							labelWidth : 70,
							items : [memo]
						}, {
							columnWidth : .5,
							layout : 'form',
							border : false,
							labelWidth : 70,
							items : [approvalMan, timeSelect]
						}, {
							columnWidth : .5,
							layout : 'form',
							labelWidth : 70,
							border : false,
							items : [completeMan, awardid, approvalBy,
									completeBy]
						}]
			});

	// 右侧Form
	var eastform = new Ext.form.FormPanel({
				bodyStyle : "padding:5px 5px 0",
				labelAlign : 'right',
				labelWidth : 70,
				region : 'center',
				border : false,
				tbar : [eastbtnAdd, {
							xtype : "tbseparator"
						}, eastbtnDel, {
							xtype : "tbseparator"
						}, eastbtnSav],
				items : [formContent]
			});

	// ↑↑主窗口部件

	var fieldSet = new Ext.Panel({
				layout : 'border',
				border : false,
				items : [{
							region : 'north',
							split : true,
							collapsible : true,
							titleCollapse : true,
							margins : '1',
							height : 300,
							layoutConfig : {
								animate : true
							},
							layout : 'fit',
							items : [eastform]
						}, {
							title : '人员列表',
							region : "center",
							layout : 'fit',
							items : [absManlistGrid]
						}]
			});

	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							title : '奖励列表',
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
						}, {
							title : '奖励信息',
							region : "center",
							split : true,
							collapsible : false,
							titleCollapse : true,
							margins : '1',
							layoutConfig : {
								animate : true
							},
							layout : 'fit',
							items : [fieldSet]
						}]
			});

	completeMan.setValue(currentUserName);
	completeBy.setValue(currentUser);
	timeSelect.setValue(myDate.getFullYear() + "-" + myMonth + "-" + myDay);

});