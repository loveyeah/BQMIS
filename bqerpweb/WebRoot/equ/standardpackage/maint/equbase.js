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

	var getcode = getParameter("code");

	var method = "many";
	var show = false;
	// // -----------定义tree-----------------
	var root = new Ext.tree.AsyncTreeNode({
				id : "root",
				text : "设备树"
			});

	var mytree = new Ext.tree.TreePanel({
				renderTo : "treepanel",
				root : root,
				enableDD : true,
				enableDrag : true,
				bbar : [{
							text : '确定添加',
							hidden : show,
							handler : treeselect
						}],
				// checkModel: "single" ,
				requestMethod : 'GET',
				loader : new Ext.tree.TreeLoader({
							url : "equ/getTreeForSelect.action",
							baseParams : {
								id : 'root',
								method : method
							}

						})
			});

	// ---------------------------------------
	function treeselect() {
		if (method == "many") {
			var b = mytree.getChecked();
			var checkid = new Array;
			var checkname = new Array;
			for (var i = 0; i < b.length; i++) {
				checkid.push(b[i].id);
				checkname.push(b[i].text.substring(b[i].text.indexOf(' ') + 1,
						b[i].text.length));
			}
			var equ = new Object();
			equ.code = checkid.toString();
			equ.name = checkname.toString();
			blockTreeWindow.returnValue = equ;
			if (b.length > 0) {
				Ext.Ajax.request({
							waitMsg : '添加中,请稍后...',
							url : 'equstandard/addRealequ.action',
							params : {
								code : getcode,
								ids : equ.code
							},
							success : function(response, options) {
								centergrids.reload();
							},
							failure : function() {
								Ext.Msg.alert('提示信息', '服务器错误,请稍候重试!')
							}
						});
			} else {
				Ext.Msg.alert("系统提示信息", "请至少选择一个设备！");
			}
			blockTreeWindow.hide();
		}
	}

	function selectMany() {
		if (method == "many") {
			var mysm = grid.getSelectionModel();
			var selected = mysm.getSelections();
			var ids = [];
			var names = [];
			var codes = [];
			if (selected.length == 0) {
				Ext.Msg.alert("系统提示信息", "请至少选择一个设备！");
			} else {
				for (var i = 0; i < selected.length; i += 1) {
					var member = selected[i].data;
					if (member.equId) {
						ids.push(member.equId);
						names.push(member.equName);
						codes.push(member.attributeCode);
					} else {
						store.remove(store.getAt(i));
					}
				}
				var equ = new Object();
				equ.id = ids.toString();
				equ.code = codes.toString();
				equ.name = names.toString();
				blockTreeWindow.returnValue = equ;
				if (selected.length > 0) {
					Ext.Ajax.request({
								waitMsg : '添加中,请稍后...',
								url : 'equstandard/addRealequ.action',
								params : {
									code : getcode,
									ids : equ.code
								},
								success : function(response, options) {
									centergrids.reload();
								},
								failure : function() {
									Ext.Msg.alert('提示信息', '服务器错误,请稍候重试!')
								}
							});
				} else {
					Ext.Msg.alert("系统提示信息", "请至少选择一个设备！");
				}
				blockTreeWindow.hide();

			}
		}

	}
	// -----------树的事件----------------------
	// 树的单击事件
	// mytree.on("click", clickTree, this);
	mytree.on('beforeload', function(node) {
				// 指定某个节点的子节点
				mytree.loader.dataUrl = 'equ/getTreeForSelect.action?method='
						+ method + '&id=' + node.id;
			}, this);

	mytree.on("check", function(node, checked) {
				alert();
			});

	// function clickTree(node) {
	// var equ = new Object();
	// equ.code = node.id;
	// equ.name = node.text.substring(node.text.indexOf(' ') + 1,
	// node.text.length);
	//
	// blockTreeWindow.returnValue = equ;
	// blockTreeWindow.hide();
	// }

	// root.expand();// 展开根节点

	// --------------------------------------

	// -----------设备列表--------------------

	var MyRecord = Ext.data.Record.create([{
				name : 'equId'
			}, {
				name : 'equName'
			}, {
				name : 'attributeCode'
			}, {
				name : 'locationCode'
			}, {
				name : 'installationCode'
			}, {
				name : 'assetnum'
			}]);

	var dataProxy = new Ext.data.HttpProxy(

	{
				url : 'equ/findEquListByFuzzy.action'
			}

	);

	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"

			}, MyRecord);

	var store = new Ext.data.Store({

				proxy : dataProxy,

				reader : theReader

			});
	// 分页
	store.load({
				params : {
					start : 0,
					limit : 10
				}
			});

	var fuuzy = new Ext.form.TextField({
				id : "fuzzy",
				name : "fuzzy"
			});

	// var sm= new Ext.grid.RowNumberer();
	var sm = new Ext.grid.CheckboxSelectionModel({
				hidden : show
			});

	var grid = new Ext.grid.GridPanel({
				// region : "center",
				layout : 'fit',
				// region:"east",
				store : store,
				enableColumnHide : true,
				// height:480,
				columns : [sm, {

							header : "ID",
							width : 75,
							sortable : true,
							dataIndex : 'equId',
							hidden : true
						}, {
							header : "设备名称",
							width : 75,
							sortable : true,
							dataIndex : 'equName'
						}, {
							header : "设备功能码",
							width : 75,
							sortable : true,
							dataIndex : 'attributeCode'
						}, {
							header : "设备位置码",
							width : 75,
							sortable : true,
							dataIndex : 'locationCode'
						}, {
							header : "安装点码",
							width : 75,
							sortable : true,
							dataIndex : 'installationCode'
						}, {
							header : "物资码",
							width : 75,
							sortable : true,
							dataIndex : 'assetnum'
						}],
				sm : sm,
				autoSizeColumns : true,
				viewConfig : {
					forceFit : true
				},
				title : '设备列表',
				tbar : [fuuzy, {
							text : "查询",
							handler : queryRecord
						}, {
							text : '确定',
							id : 'gridadd',
							hidden : show,
							handler : selectMany

						}],
				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : 10,
							store : store,
							displayInfo : true,
							displayMsg : "{0} 到 {1} /共 {2} 条",
							emptyMsg : "没有记录"
						})
			});

	grid.on("dblclick", selectOneRecord);

	// grid.setHidden(1,true);
	// grid.getColumnModel().setHidden(1,true);

	// ------------------

	// grid.getTopToolbar();

	// 查询
	function queryRecord() {
		var fuzzytext = fuuzy.getValue();
		store.baseParams = {
			fuzzy : fuzzytext
		};
		store.load({
					params : {
						start : 0,
						limit : 10
					}
				});
	}

	function selectOneRecord() {
		if (grid.selModel.hasSelection()) {
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请先选择一个设备！");
			} else {
				var record = grid.getSelectionModel().getSelected();
				var equ = new Object();
				equ.id = record.get("equId");
				equ.code = record.get("attributeCode");
				equ.name = record.get("equName");

				blockTreeWindow.returnValue = equ;
				blockTreeWindow.hide();
			}
		} else {
			Ext.Msg.alert("提示", "请先选择设备!");
		}
	}

	// -------------------------------------

	var panelleft = new Ext.Panel({
				layout : 'fit',
				title : '树形方式查询',
				// width : 400,
				// autoScroll:true,
				collapsible : true,
				items : [mytree],
				frame : false,
				border : false
			});

	var panel2 = new Ext.Panel({
				id : 'tab2',
				layout : 'fit',
				title : '列表方式查询',
				items : [grid],
				frame : false,
				border : false
			});

	var tabpanel = new Ext.TabPanel({
				title : 'mytab',
				activeTab : 0,
				margins : '0',
				autoScroll : true,
				items : [panelleft, panel2],
				frame : false,
				border : false
			});

	// 弹出窗体
	var blockTreeWindow;
	function showTreeWindow() {
		if (!blockTreeWindow) {
			blockTreeWindow = new Ext.Window({
						// el : 'window_win',
						title : '',
						layout : 'fit',
						width : 420,
						height : 360,
						modal : false,
						closable : true,
						border : false,
						scroll : true,
						resizable : true,
						closeAction : 'hide',
						plain : true,
						// 面板中按钮的排列方式
						buttonAlign : 'center',
						items : tabpanel
					});
		}
		blockTreeWindow.show(Ext.get('getrole'));
	};

	// 新建按钮
	var centerbtnAdd = new Ext.Button({
				text : '新增',
				iconCls : 'add',
				handler : function() {
					op = "add";
					showTreeWindow();
				}
			});

	// 删除按钮
	var centerbtnDel = new Ext.Button({
		text : '删除',
		iconCls : 'delete',
		handler : function() {
			if (centergrid.getSelectionModel().getSelections().length > 0) {
				Ext.Msg.confirm('提示信息', '确认 删除 选中记录?', function(button, text) {
							if (button == 'yes') {
								var rec = centergrid.getSelectionModel()
										.getSelections();
								var str = "";
								for (var i = 0; i < rec.length; i++) {
									str += rec[i].get("realatequInfo.equId")
											+ ",";
								}
								Ext.Ajax.request({
											waitMsg : '删除中,请稍后...',
											url : 'equstandard/delRealequ.action',
											params : {
												ids : str
											},
											success : function(response,
													options) {
												centergrids.reload();
												Ext.Msg
														.alert('提示信息',
																'删除记录成功！');
											},
											failure : function() {
												Ext.Msg.alert('提示信息',
														'服务器错误,请稍候重试!')
											}
										});
							}
						})
			} else {
				Ext.Msg.alert('提示信息', "请至少选择一行！");
				return false;
			}
		}
	});
	// 刷新按钮
	var centerbtnref = new Ext.Button({
				text : '刷新',
				iconCls : 'refresh',
				handler : function() {
					centergrids.reload();
				}
			});
	var centersm = new Ext.grid.CheckboxSelectionModel();
	// 左边列表中的数据
	var datalist = new Ext.data.Record.create([{
				name : 'realatequInfo.equId'
			}, {
				name : 'realatequInfo.code'
			}, {
				name : 'realatequInfo.attributeCode'
			}, {
				name : 'realatequInfo.status'
			}, {
				name : 'equname'
			}]);

	var centergrids = new Ext.data.JsonStore({
				url : 'equstandard/getRealequList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});
	centergrids.load({
				params : {
					code : getcode
				}
			});

	// 列表
	var centergrid = new Ext.grid.GridPanel({
				ds : centergrids,
				columns : [centersm, new Ext.grid.RowNumberer(), {
							header : "设备名称",
							width : 180,
							sortable : false,
							dataIndex : 'equname'
						}],
				tbar : [centerbtnAdd, {
							xtype : "tbseparator"
						}, centerbtnDel, {
							xtype : "tbseparator"
						}, centerbtnref],
				sm : centersm,
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
							split : false,
							collapsible : false,
							items : [centergrid]
						}]
			});
});