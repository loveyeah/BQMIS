Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	function getParameter(psName) {
		var url = self.location;
		var result = "";
		var str = self.location.search.substring(0);
		if (str.indexOf(psName) != -1
				&& (str.substr(str.indexOf(psName) - 1, 1) == "?" || str
						.substr(str.indexOf(psName) - 1, 1) ==

				"&")) {
			if (str.substring(str.indexOf(psName), str.length).indexOf("&")

			!= -1) {
				var Test = str.substring(str.indexOf(psName),

						str.length);
				result = Test.substr(Test.indexOf(psName), Test.indexOf

						("&") - Test.indexOf(psName));
				result = result.substring(result.indexOf("=") + 1,
						result.length);
			} else {
				result = str.substring(str.indexOf(psName), str.length);
				result = result.substring(result.indexOf("=") + 1,
						result.length);
			}
		}
		return result;
	};

	var woCode1 = getParameter("woCode");

	var operationStep1 = getParameter("opCode");

	var rec = Ext.data.Record.create([{
				name : 'baseInfo.woCode'
			}, {
				name : 'baseInfo.operationStep'
			}, {
				name : 'baseInfo.planToolNum'
			}, {
				name : 'baseInfo.planLocationId'

			}, {
				name : 'baseInfo.factToolQty'
			}, {
				name : 'baseInfo.factToolHrs'
			}, {
				name : 'baseInfo.factToolPrice'

			}, {
				name : 'baseInfo.planToolDescription'

			}, {
				name : 'baseInfo.id'

			}, {
				name : 'baseInfo.orderby'
			}, {
				name : 'toolsName'
			}]);

	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'workbill/findAll.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'totalCount',
							root : 'list'
						}, rec)
			});

	ds.load({
				params : {
					woCode : woCode1,
					operationStep : operationStep1,
					start : 0,
					limit : 18
				}
			});

	/* 设置每一行的选择框 */
	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false,
				listeners : {
					rowselect : function(sm, row, rec) {
						// Ext.getCmp("form").getForm().loadRecord(rec);
					}
				}
			});

	var rn = new Ext.grid.RowNumberer({

	});

	// 弹出画面

	var memoText = new Ext.form.TextArea({
				id : "memoText",
				maxLength : 128,
				width : 180
			});

	var win = new Ext.Window({
				height : 170,
				width : 350,
				layout : 'fit',
				modal : true,
				resizable : false,
				closeAction : 'hide',
				items : [memoText],
				buttonAlign : "center",
				title : '详细信息录入窗口',
				buttons : [{
					text : '保存',
					iconCls : 'save',
					handler : function() {
						if (!memoText.isValid()) {
							return;
						}
						var record = grid.selModel.getSelected();
						record.set("factToolDescription",
								Ext.get("memoText").dom.value);
						win.hide();
					}
				}, {
					text : '关闭',
					iconCls : 'cancer',
					handler : function() {
						win.hide();
					}
				}]
			});

	var cm = new Ext.grid.ColumnModel([sm, rn

	, {
		header : '工具',
		dataIndex : 'toolsName',
		align : 'left',
		editor : new Ext.form.TextField({
			readOnly : true,
			listeners : {
				focus : function(e) {

					var args = {
						selectModel : 'single',
						rootNode : {
							id : '0',
							text : '工具类别'
						}
					}
					var rvo = window
							.showModalDialog(
									'/power/equ/standardpackage/maint/equCStandardOrderstep/tools.jsp',
									args,
									'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
					if (typeof(rvo) != "undefined") {

						var record = grid.getSelectionModel().getSelected();

						record.set("factToolsName", rvo.name);
						record.set("baseInfo.factToolNum", rvo.code);
						// rvo.serUnit
					}
					this.blur();

				}
			}
		})

			// ,
			// width : auto
	}, {
		header : '工具编码',
		dataIndex : 'baseInfo.planToolNum',
		align : 'left',
		hidden : true
			// ,
			// width : auto
		}, {
		header : '数量',
		dataIndex : 'baseInfo.factToolQty',
		align : 'left',
		editor : new Ext.form.NumberField()
	}, {
		header : '工时',
		dataIndex : 'baseInfo.factToolHrs',
		align : 'left',
		editor : new Ext.form.NumberField()
			// ,
			// width : auto
		}, {
		header : '所在仓库',
		dataIndex : 'baseInfo.planLocationId',
		align : 'left',
		hidden:true,
		editor : new Ext.form.ComboBox({
					fieldLabel : '所在仓库',
					store : mystore = new Ext.data.SimpleStore({
								fields : ["retrunValue", "displayText"],
								data : [['1', '典雅黑'], ['2', '深灰']]
							}),
					valueField : "retrunValue",
					displayField : "displayText",
					mode : 'local',
					forceSelection : true,
					blankText : '所在仓库',
					emptyText : '所在仓库',
					hiddenName : 'baseInfo.factLocationId',
					value : '',

					editable : false,
					triggerAction : 'all',
					selectOnFocus : true,
					// allowBlank : false,
					name : 'baseInfo.factLocationId',
					anchor : '99%'
				}),
		renderer : function changeIt(val) {
			for (i = 0; i < mystore.getCount(); i++) {
				if (mystore.getAt(i).get("retrunValue") == val)
					return mystore.getAt(i).get("displayText");
			}
			// return val
		}

	}, {
		header : '描述',
		dataIndex : 'baseInfo.planToolDescription',
		align : 'left',
		editor : new Ext.form.TextArea({
					maxLength : 128,
					listeners : {
						"render" : function() {
							this.el.on("dblclick", function() {
										var record = grid.getSelectionModel()
												.getSelected();
										var value = record
												.get('baseInfo.factToolDescription');
										memoText.setValue(value);
										win.x = undefined;
										win.y = undefined;
										win.show();
									})
						}
					}
				})
			// ,
			// width : auto
	}, {
		header : '费用',
		dataIndex : 'baseInfo.factToolPrice',
		align : 'left',
		editor : new Ext.form.NumberField()
			// ,
			// width : auto
		}

	]);

	// cm.defaultSortable = true;
	// 增加
	function addRecords() {
		// var currentRecord = gird.getSelectionModel().getSelected();
		var count = ds.getCount();
		var currentIndex = count;
		// var currentIndex = currentRecord
		// ? currentRecord.get("displayNo") - 1
		// : count;
		var o = new rec({
					'baseInfo.woCode' : woCode1,
					'baseInfo.operationStep' : operationStep1,
					'factToolsName' : '',
					'baseInfo.factLocationId' : '',
					'baseInfo.factToolQty' : '',
					'baseInfo.factToolHrs' : '',
					'baseInfo.factToolPrice' : '',
					'baseInfo.factToolDescription' : ''

				});

		grid.stopEditing();
		ds.insert(currentIndex, o);
		sm.selectRow(currentIndex);
		grid.startEditing(currentIndex, 3);
		// resetLine();
	}

	// 删除记录
	var ids = new Array();
	function deleteRecords() {
		grid.stopEditing();
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("baseInfo.id") != null) {
					ids.push(member.get("baseInfo.id"));
				}
				grid.getStore().remove(member);
				grid.getStore().getModifiedRecords().remove(member);
			}
			// resetLine();
		}
	}
	// 保存
	function save() {
		grid.stopEditing();
		var modifyRec = grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			Ext.Msg.confirm('提示信息', '确定要保存修改数据吗?', function(button) {
				if (button == 'yes') {
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						updateData.push(modifyRec[i].data);
					}

					Ext.Ajax.request({
								url : 'workbill/saveEquJTools.action',
								method : 'post',
								params : {
									isUpdate : Ext.util.JSON.encode(updateData),
									isDelete : ids.join(",")
								},
								success : function(result, request) {
									var o = eval('(' + result.responseText
											+ ')');
									Ext.MessageBox.alert('提示信息', o.msg);
									ds.rejectChanges();
									ids = [];
									ds.reload();
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
		var modifyRec = ds.getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			Ext.Msg.confirm('提示信息', '确定要放弃修改数据吗?', function(button) {
						if (button == 'yes') {
							ds.reload();
							ds.rejectChanges();
							ids = [];
						}
					})

		} else {
			ds.reload();
			ds.rejectChanges();
			ids = [];
		}
	}

	var tbar = new Ext.Toolbar({
				items : [
						// {
						// id : 'btnAdd',
						// iconCls : 'add',
						// text : "新增",
						// handler : addRecords
						// // handler :function() {
						// // form.getForm().reset();
						// // method = 'add';
						// // form.setTitle("新建工具");
						// // win.show(Ext.get('btnAdd'));
						// //
						// // }
						// }, {
						// id : 'btnDelete',
						// iconCls : 'delete',
						// text : "删除",
						// handler : deleteRecords
						//
						// },
						{
					id : 'btnCancer',
					iconCls : 'cancer',
					text : "取消",
					handler : cancer

				}, '-', {
					id : 'btnSave',
					iconCls : 'save',
					text : "保存",
					handler : save
				}]

			});

	/* 创建表格 */
	var grid = new Ext.grid.EditorGridPanel({
				// el : 'siteTeam',
				ds : ds,
				cm : cm,
				sm : sm,

				tbar : tbar,
				// title : '用户列表',
				autoWidth : true,
				fitToFrame : true,
				border : true,
				frame : true,
				clicksToEdit : 1,// 单击一次编辑
				viewConfig : {
					forceFit : false
				}
			});
	// ds.on('beforeload', function() {
	// Ext.apply(this.baseParams, {
	// userlike : Ext.get("userlike").dom.value
	// });
	// });
	// grid.enableColumnHide = false;
	// grid.render();
	// grid.on("rowdblclick", function() {
	// Ext.get("btnUpdate").dom.click();
	// });

	// 设定布局器及面板
	var viewport = new Ext.Viewport({
				layout : 'fit',
				autoWidth : true,
				autoHeight : true,
				fitToFrame : true,
				items : [grid]
			});
})