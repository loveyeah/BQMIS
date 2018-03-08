Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var flag = false;
	// 定义根节点：
	var root = new Ext.tree.AsyncTreeNode({
				text : Constants.POWER_NAME,
				isRoot : true,
				id : '0'
			});

	var workcode;
	// 定义加载器

	var treeLoader = new Ext.tree.TreeLoader({
				dataUrl : 'hr/getDeptEmpTreeList.action'
			})

	// 定义部门树

	var treeEmployee = new Ext.tree.TreePanel({
				autoScroll : true,
				containerScroll : true,
				collapsible : true,
				split : true,
				border : false,
				rootVisible : false,
				root : root,
				animate : true,
				enableDD : false,
				loader : treeLoader

			});

	treeEmployee.on("click", treeClick);
	treeEmployee.setRootNode(root);
	root.select();

	/**
	 * 点击树时
	 */
	function treeClick(node, e) {
		workcode = node.id;
		if (node.isLeaf()) {
			findByEmpId(node.id);

		}
	}

	function findByEmpId(empId) {
		Ext.Ajax.request({
					url : 'hr/findLaborChangeList.action',
					method : Constants.POST,
					params : {
						// 员工ID
						empId : workcode
					},

					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.msg != undefined
								&& result.msg == Constants.SQL_FAILURE) {
							// 失败时，弹出提示
							Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
						} else {
							// 清楚已修改记录，重新加载
							sel_ds.modified = [];
							sel_ds.loadData(result);

						}
					}
				});
	}

	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		t = d.getHours();
		s += (t > 9 ? "" : "0") + t + ":";
		t = d.getMinutes();
		s += (t > 9 ? "" : "0") + t + ":";
		t = d.getSeconds();
		s += (t > 9 ? "" : "0") + t;
		return s;
	}

	var leftPanel = new Ext.Panel({
				region : 'west',
				layout : 'fit',
				width : '20%',
				frame : false,
				autoScroll : true,
				containerScroll : true,
				collapsible : true,
				split : true,
				items : [treeEmployee]
			});

	// 新劳保工种下拉框数据源
	var unitData = Ext.data.Record.create([{
				name : 'text'
			}, {
				name : 'id'
			}]);

	var allUnitStore = new Ext.data.JsonStore({
		url : 'hr/getAllWorkInfo.action',
		root : 'list',
		fields : unitData
			/*
			 * listeners: { load: function() { alert(unitCombobox.getValue());
			 * unitCombobox.setValue(unitCombobox.getValue());
			 * alert(unitCombobox.getValue()); } }
			 */
		});

	allUnitStore.load();

//	var unitCombobox = new Ext.form.ComboBox({
//				fieldLabel : '工种',
//				id : 'unitCode',
//				name : 'id',
//				allowBlank : true,
//				style : "border-bottom:solid 2px",
//				triggerAction : 'all',
//				editable : false,
//				store : allUnitStore,
//				blankText : '',
//				emptyText : '',
//				valueField : 'id',
//				displayField : 'text',
//				mode : 'local',
//				anchor : '85%',
//				listeners : {
//					change : function() {
//						alert(unitCombobox.getValue());
//						Grid.getSelectionModel().getSelected().set(
//								"newLbWorkId", unitCombobox.getValue());
//					}
//				}
//			})

	var sm = new Ext.grid.CheckboxSelectionModel();
	var selct_mod = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	var MyRecord = Ext.data.Record.create([{
				name : 'laborChangeId'
			}, {
				name : 'changeDate'
			}, {
				name : 'startDate'
			}, {
				name : 'oldLbWorkId'
			}, {
				name : 'oldLbWorkName'
			}, {
				name : 'newLbWorkId'
			}, {
				name : 'newLbWorkName'
			}

	]);
	function getMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		return s;
	}
	var dteStart = new Ext.form.TextField({
				readOnly : true,
				allowBlank : false,
				checked : true,
				fieldLabel : '<font color ="red">开始日期*</font>',
				anchor : '100%',
				width : 120,
				id : 'startDate',
				name : 'startDate',
				value : getMonth(),
				style : 'cursor:pointer'

			});
	var dteEnd = new Ext.form.TextField({
				readOnly : false,
				checked : true,
				allowBlank : false,
				fieldLabel : '变更日期<font color ="red">*</font>',
				anchor : '100%',
				width : 120,
				id : 'changeDatee',
				name : 'changeDate',
				value : getMonth(),
				style : 'cursor:pointer'
			});

	function checkTime1() {
		var startdate = this.value;
		var changedate = Grid.getSelectionModel().getSelected()
				.get("changeDate");

		if (startdate != "") {
			if (startdate > changedate && changedate != "") {
				Ext.Msg.alert("提示", "开始日期必须早于变更日期");
				return;
			}
		}

		Grid.getSelectionModel().getSelected().set("startDate", startdate);
	}
	function checkTime2() {

		var startdate = Grid.getSelectionModel().getSelected().get("startDate");
		var changedate = this.value;

		if (changedate != "") {
			if (changedate < startdate && startdate != "") {
				Ext.Msg.alert("提示", "变更日期必须晚于开始日期");
				return;
			}
		}
		Grid.getSelectionModel().getSelected().set("changeDate", changedate);
	}

	var cm = new Ext.grid.ColumnModel([sm,
			// selct_mod,
			new Ext.grid.RowNumberer({
						header : '行号',
						width : 35
					}), {
				header : '变更日期',
				dataIndex : 'changeDate',
				width : 150,
				align : 'left',
				// editor : new Ext.form.TextField({
				allowBlank : false,
				style : 'cursor:pointer',
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
									// 时间格式
									startDate : '%y-%M-%d 00:00:00',
									dateFmt : 'yyyy-MM-dd HH:mm:ss',
									alwaysUseStartDate : false,
									onpicked : checkTime2

								});

					}
				}
				// })
		}	, {
				header : '开始日期',
				dataIndex : 'startDate',
				width : 150,
				align : 'left',
				editor : new Ext.form.TextField({
							allowBlank : false,
							style : 'cursor:pointer',
							readOnly : true,
							listeners : {
								focus : function() {
									WdatePicker({
												// 时间格式
												startDate : '%y-%M-%d 00:00:00',
												dateFmt : 'yyyy-MM-dd HH:mm:ss',
												alwaysUseStartDate : false,
												onpicked : checkTime1
											});

								}
							}
						})

			}, {
				header : '原劳保工种',
				dataIndex : 'oldLbWorkName',
				width : 150,
				align : 'left',
				clicksToEdit : 0,

				editor : new Ext.form.TextField({
							readOnly : true
						})
			},/*
				 * new Ext.form.ComboBox({ fieldLabel : '工种', id :
				 * 'oldunitcode', name : 'id', allowBlank : true, style :
				 * "border-bottom:solid 2px", triggerAction : 'all', editable :
				 * false, store : allUnitStore, blankText : '', emptyText : '',
				 * valueField : 'id', readOnly :true, displayField : 'text',
				 * mode : 'local', anchor : '85%' }) },
				 */
			// {
			// header : '新劳保工种',
			// dataIndex : 'newLbWorkName',
			// width : 100,
			// align : 'left',
			// editor : unitCombobox
			// // renderer: Ext.util.Format.comboRenderer(unitCombobox)
			//
			// }
			{
				header : "新劳保工种",
				width : 100,
				sortable : true,
				dataIndex : 'newLbWorkId',
				editor : new Ext.form.ComboBox({
							store : allUnitStore,
							displayField : "text",
							valueField : "id",
							id : 'newLbWorkName',
							allowBlank : false,
							mode : 'local',
							triggerAction : 'all',
							readOnly : true
						}),
				renderer : function(v) {
					for (var i = 0; i < allUnitStore.getCount(); i++) {
						var rec = allUnitStore.getAt(i);
						if (rec.get("id") == v) {
							return rec.get("text");
						}
					}
					return v;
				}

			}]);

	var selbar = new Ext.Toolbar({
				items : [{
					id : 'btnQuery',
					iconCls : 'query',
					text : "查询",
					handler : findByEmpId
						// querydata
					}, '-', {
					id : 'btnSave',
					iconCls : 'save',
					text : "保存",
					handler : savedata
				}, '-', {
					id : 'btnAdd',
					iconCls : 'add',
					text : "增加",
					handler : adddata
				}, '-', {
					id : 'btndeldata',
					iconCls : 'delete',
					text : "删除",
					handler : deletedata
				}]
			})

	function deletedata() {
		var sm = Grid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = null;
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			var sel = sm.getSelected();
			var comp = sel_ds.getAt(sel_ds.getTotalCount() - 1);
			if (sel.get('laborChangeId') != comp.get('laborChangeId')) {
				Ext.Msg.alert('提示', '请先删除最后一条变更记录！');
				return;
			}

			Ext.Msg.confirm('提示', '是否确定删除所选的记录', function(response) {
						if (response == 'yes') {
							Ext.Ajax.request({
										method : 'post',
										url : 'hr/deleteLaborChangeInfo.action',
										params : {
											ids : sel.get('laborChangeId')
										},
										success : function(action) {
											Ext.Msg.alert("提示", "删除成功！")
											findByEmpId(workcode);
										},
										failure : function() {
											Ext.Msg.alert('错误', '删除时出现未知错误.');
										}

									});
						}
					});
		}
	};

	function adddata() {
		flag = true;
		var count = sel_ds.getCount();
		if (count > 0) {
			var oldLbWorkId = sel_ds.getAt(count - 1).get('newLbWorkId');
			var oldLbWorkName = sel_ds.getAt(count - 1).get('newLbWorkName');
		} else {
			var oldLbWorkId = null;
			var oldLbWorkName = null;
		}

		// 动态从后台获取增加ID及其他值
		var currentIndex = count;
		var o = new MyRecord({
					'laborChangeId' : null,
					// 'changeDate' : null,
					'changeDate' : getDate(),
					'startDate' : null,
					'oldLbWorkId' : oldLbWorkId,
					'oldLbWorkName' : oldLbWorkName,
					'newLbWorkId' : null,
					'newLbWorkName' : null
				});
		Grid.stopEditing();
		sel_ds.insert(currentIndex, o);
		selct_mod.selectRow(currentIndex);
		Grid.startEditing(currentIndex, 3);
	}

	/*
	 * function querydata() { sel_ds.load({ params : { empId : workcode } }); }
	 */

	function checkName() {
		var i = sel_ds.getCount() - 1;
		if (sel_ds.getAt(i).get('newLbWorkId') == sel_ds.getAt(i)
				.get('oldLbWorkId')) {
			Ext.Msg.alert('提示', '新劳保工种不能与就劳保工种相同！');
			return false;
		}
		return true;
	}
	var type = "";
	function savedata() {
		Grid.stopEditing();
		if (!checkName())
			return;
		var modifyRec = Grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0) {
			if (!confirm("确定要保存修改吗?"))
				return;
			var updateData = new Array();
			for (var i = 0; i < modifyRec.length; i++) {
				updateData.push(modifyRec[i].data);
			}
			if (type != 'add')
				type = 'update';
			Ext.Ajax.request({
						url : 'hr/saveLaborChangeInfo.action',
						method : 'post',
						params : {
							updateData : Ext.util.JSON.encode(updateData),
							empId : workcode
						},
						success : function(result, request) {
							Ext.Msg.alert('提示信息', "操作成功!");

							findByEmpId(workcode);
							flag = false;

						},
						failure : function(result, request) {
							Ext.Msg.alert('提示信息', '操作失败')
						}
					})

		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}

	var sel_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'hr/findLaborClassList.action'
						}),
				reader : new Ext.data.JsonReader({}, MyRecord)
			});

	cm.defaultSortable = false;
	var Grid = new Ext.grid.EditorGridPanel({
				viewConfig : {
					forceFit : false
				},
				sm : selct_mod,
				clicksToEdit : 1,
				ds : sel_ds,
				cm : cm,
				height : 425,
				split : true,
				autoScroll : true,
				layout : 'fit',
				frame : false,
				// bbar : gridbbar,
				tbar : selbar,
				border : true
			});

	Grid.on('beforeedit', function(e) {
				if (flag)
					return true;
				else
					return false;
			})
	/*
	 * new Ext.({ enableTabScroll : true, layout : "fit", border : false, frame :
	 * false, items : [Grid] })
	 */
	var fullPanel = new Ext.Panel({
				layout : 'border',
				border : false,
				/* tbar : tbrButton, */
				defaults : {
					autoScroll : true
				},
				items : [leftPanel, {
							region : 'center',
							layout : 'form',
							frame : false,
							border : true,
							items : [Grid]
						}]
			});

	// 设定布局器及面板
	new Ext.Viewport({
				layout : "fit",
				defaults : {
					autoScroll : true
				},
				items : [fullPanel]
			});

})