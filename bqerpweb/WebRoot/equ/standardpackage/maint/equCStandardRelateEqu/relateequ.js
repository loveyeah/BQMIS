Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

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
	};

	var woCode1 = getParameter("woCode");

	// 设备选择

	function scanEqu() {

		var object = window
				.showModalDialog(
						'../../../../comm/jsp/equselect/selectAttribute.jsp?op=many',

						'dialogWidth=550px;dialogHeight=350px;center=yes;help=no;resizable=no;status=no;');
		if (typeof(object) == 'object') {

			var str = object.code;

			Ext.Ajax.request({
				waitMsg : '锁定中,请稍后...',
				url : 'equstandard/saveEquCStandardEqu.action',
				params : {
					woCode : woCode1,
					ids2 : str
				},

				success : function(response, options) {
					ds.reload();
					Ext.Msg.alert('提示信息', '关联设备成功！');
				},
				failure : function() {
					Ext.Msg.alert('提示信息', '服务器错误,请稍候重试!')
				}
			});

		}
	};

	var rec = Ext.data.Record.create([{
		name : 'model.kksCode'
	}, {
		name : 'model.id'
	}, {
		name : 'model.memo'
	}, {
		name : 'equName'
	}, {
		name : 'model.status'
	}]);

	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'equstandard/getEquCStandardEquList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list'
		}, rec)
	});

	ds.load({
		params : {
			start : 0,
			limit : 10000,
			woCode : woCode1
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
	var cm = new Ext.grid.ColumnModel([sm,
	rn,{header : "状态",
		width : 40,
		align : "center",
		sortable : true,
		renderer : function changeIt(val) {
			if (val == "C") {
				return "正常";
			} else if (val == "L") {
				return "锁定";
			} else if (val == "O") {
				return "注销";
			} else {
				return "状态异常";
			}
		},
		dataIndex : 'model.status'
	}, {
		header : '设备名称',
		dataIndex : 'equName',
		align : 'left',
		width : 100
	}, {
		header : '设备KKS编码',
		dataIndex : 'model.kksCode',
		align : 'left',
		width : 100
	}]);

	// 锁定
	function Lockhandler() {

		if (grid.getSelectionModel().getSelections().length > 0) {
			Ext.Msg.confirm('提示信息', '确认 锁定 选中记录?', function(button, text) {
				if (button == 'yes') {
					var rec = grid.getSelectionModel().getSelections();
						var str = "";
						for (var i = 0; i < rec.length; i++) {
							if (i < rec.length - 1)
								str += rec[i].get("model.id")+ ",";
							else
								str += rec[i].get("model.id");
					}
					Ext.Ajax.request({
						waitMsg : '锁定中,请稍后...',
						url : 'equstandard/lockEquCStandardEqu.action',
						params : {
							//method : "lock",
							ids : str
						},
						success : function(response, options) {
							ds.reload();
							Ext.Msg.alert('提示信息', '锁定记录成功！');
						},
						failure : function() {
							Ext.Msg.alert('提示信息', '服务器错误,请稍候重试!')
						}
					});
				}
			})
		} else {
			Ext.Msg.alert('提示信息', "请至少选择一行！");
			return false;
		}

	};

	var northGridLock = new Ext.Button({
		text : '锁定',
		iconCls : 'locked',
		handler : Lockhandler
	});

	// 解锁
	function Unlockhandler() {

		if (grid.getSelectionModel().getSelections().length > 0) {
			Ext.Msg.confirm('提示信息', '确认 解锁 选中记录?', function(button, text) {
				if (button == 'yes') {
					var rec = grid.getSelectionModel().getSelections();
					var str = "";
						for (var i = 0; i < rec.length; i++) {
							if (i < rec.length - 1)
								str += rec[i].get("model.id")+ ",";
							else
								str += rec[i].get("model.id");
					}
					Ext.Ajax.request({
						waitMsg : '解锁中,请稍后...',
						url : 'equstandard/unlockEquCStandardEqu.action',
						params : {
							method : "unlock",
							ids : str
						},
						success : function(response, options) {
							ds.reload();
							Ext.Msg.alert('提示信息', '解锁记录成功！');
						},
						failure : function() {
							Ext.Msg.alert('提示信息', '服务器错误,请稍候重试!')
						}
					});
				}
			})
		} else {
			Ext.Msg.alert('提示信息', "请至少选择一行！");
			return false;
		}

	};

	var northGridUnlock = new Ext.Button({
		text : '解锁',
		iconCls : 'unlocked',
		handler : Unlockhandler
	});

	var tbar = new Ext.Toolbar({
		items : [{
			id : 'btnAdd',
			iconCls : 'add',
			text : "关联设备",
			handler : scanEqu

		},

		{
			id : 'btnDelete',
			iconCls : 'delete',
			text : "删除",
			handler : function() {
				var selectedRows = grid.getSelectionModel().getSelections();
				if (selectedRows.length > 0) {
					var Ids = "";
					Ext.Msg.confirm('提示', '删除的数据您将不能恢复,确定要删除吗?', function(b) {
						if (b == 'yes') {
							Ids = selectedRows[0].get('model.id');

							for (i = 1; i < selectedRows.length; i++) {
								Ids += "," + selectedRows[i].get('model.id');
							}

							Ext.Ajax.request({
								url : 'equstandard/deleteEquCStandardEqu.action',
								params : {
									ids : Ids
								},
								method : 'post',
								waitMsg : '正在删除数据...',
								success : function(result, request) {
									// for (i = 0; i < selectedRows.length; i++)
									// {
									// ds.remove(selectedRows[i]);
									// }
									// ds.commitChanges();

									Ext.MessageBox.alert('提示信息', '删除记录成功！');

									ds.load({
										params : {
											start : 0,
											limit : 18,
											woCode : woCode1

										}
									});

									grid.getView().refresh();

								},
								failure : function(result, request) {
									Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
								}
							});
						}
					});
				} else {
					Ext.Msg.alert('提示', '请选择您要删除的用户!');
				}
			}
		}, '-', {
			id : 'btnReflesh',
			iconCls : 'reflesh',
			text : "刷新",
			handler : function() {
				window.location = window.location;
			}
		}, northGridLock, '-', northGridUnlock]

	});

	/* 创建表格 */
	var grid = new Ext.grid.EditorGridPanel({

		ds : ds,
		cm : cm,
		sm : sm,
//       clicksToEdit : 1,
		tbar : tbar,
		title : '',
		autoWidth : true,
		fitToFrame : true,
		border : true,
		frame : true,
		viewConfig : {
			forceFit : false
		}
	});

	// ds.on('beforeload', function() {
	// Ext.apply(this.baseParams, {
	// userlike : Ext.get("userlike").dom.value
	// });
	// });

	/*grid.on("rowdblclick", function() {
//		Ext.get("btnUpdate").dom.click();
	});
*/
	// 设定布局器及面板
	var viewport = new Ext.Viewport({
		layout : 'fit',
		autoWidth : true,
		autoHeight : true,
		fitToFrame : true,
		items : [grid]
	});

})