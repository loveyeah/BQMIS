Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	// 定义是否多选
	var select = 'single';
	var show = true;
	if (getParameter("select") != "") {
		select = getParameter("select");
	}
	if (select == 'many') {
		show = false;
	}
	// 定义树结构
	var _deptId = '0';
	var Tree = Ext.tree;
	var tree = new Tree.TreePanel({
				rootVisible : false,
				autoHeight : true,
				root : root,
				animate : true,
				enableDD : true,
				border : false,
				containerScroll : true,
				loader : new Tree.TreeLoader({
							dataUrl : 'com/findDeptList.action'
						})
			});
	var root = new Tree.AsyncTreeNode({
				text : '合肥电厂',
				draggable : false,
				id : '-1'
			});
	tree.setRootNode(root);
	root.expand();
	root.select();
	currentNode = root;
	tree.on('click', treeClick);
	function treeClick(node, e) {
		e.stopEvent();

		Ext.Ajax.request({
					url : 'findDeptById.action',
					method : 'post',
					params : {
						node : node.id
					},
					success : function(result, request) {
						_deptId = node.id;
						connection.url = 'empInfoManage.action?method=getlist&deptId='
								+ _deptId;
						store.load({
									params : {
										start : 0,
										limit : 18
									}
								});
						grid.store.reload();

					},
					failure : function(result, request) {
						Ext.MessageBox.alert('错误', '操作失败!');
					}
				});
	};

	// -------------------- 定义grid--------------------------
	var MyRecord = Ext.data.Record.create([{
				name : 'empId'
			}, {
				name : 'empCode'
			}, {
				name : 'chsName'
			}, {
				name : 'sex'
			}, {
				name : 'identityCard'
			}, {
				name : 'graduateSchool'
			}, {
				name : 'speciality'
			}, {
				name : 'deptId'
			}, {
				name : 'deptCode'
			}, {
				name : 'deptName'
			}, {
				name : 'stationId'
			}, {
				name : 'empState'
			}]);

	var connection = new Ext.data.Connection({
				url : 'empInfoManage.action?method=getlist&deptId=' + _deptId
			});
	var dataProxy = new Ext.data.HttpProxy(connection);
	var theReader = new Ext.data.JsonReader({
				root : "root",
				totalProperty : "total"

			}, MyRecord);

	var store = new Ext.data.Store({

				proxy : dataProxy,

				reader : theReader

			});

	store.load({
				params : {
					start : 0,
					limit : 18
				}
			});

	// store.load();

	// add

	var sm = new Ext.grid.CheckboxSelectionModel();

	// ----

	var fuuzy = new Ext.form.TextField({
				id : "fuzzy",
				name : "fuzzy"
			});

	var grid = new Ext.grid.GridPanel({
				region : "center",
				// region:"east",
				store : store,

				columns : [// 设计页面显示的表格信息，字段与Ext.data.Record.create匹配
				sm, {
							header : "员工工号",
							width : 75,
							sortable : true,
							dataIndex : 'empCode'
						},

						{
							header : "中文名",
							width : 75,
							sortable : true,
							dataIndex : 'chsName'
						}, {
							hidden : true,
							width : 75,
							sortable : true,
							dataIndex : 'deptCode'
						}, {
							header : "所属部门",
							width : 75,
							sortable : true,
							dataIndex : 'deptName'
						}],
				sm : sm,
				stripeRows : true,
				autoSizeColumns : true,
				viewConfig : {
					forceFit : true
				},

				autoExpandColumn : 'id',

				title : '员工基本信息管理',
				tbar : [fuuzy, {
							text : "查询",
							iconCls : 'query',
							handler : queryRecord
						}, {
							text : "确定",
							iconCls : 'confirm',
							handler : returnEmp,
							hidden : !show
						}, {
							text : "确定",
							iconCls : 'confirm',
							handler : manyemp,
							hidden : show
						}],
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : store,
							displayInfo : true,
							displayMsg : "{0}到{1}条，共{2}条",
							emptyMsg : "没有记录",
							beforePageText : '页',
							afterPageText : "共{0}"
						})
			});

	// 双击返回单个人员信息
	grid.on('dblclick', show ? returnEmp : manyemp);
	function returnEmp() {
		if (grid.selModel.hasSelection()) {

			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择一个人员！");
			} else {
				var record = grid.getSelectionModel().getSelected();

				var emp = new Object();
				emp.code = record.get("empCode");
				emp.name = record.get("chsName");
				emp.deptCode = record.get("deptCode");
				emp.deptName = record.get("deptName");
				emp.deptId = record.get("deptId");
				// alert(emp.deptId);
				window.returnValue = emp;
				window.close();
			}
		} else {
			Ext.Msg.alert("提示", "请先选择人员!");
		}
	}
	// 确定返回多个人员信息
	function manyemp() {
		if (select == "many") {
			var mysm = grid.getSelectionModel();
			var selected = mysm.getSelections();
			var codes = [];
			var names = [];
			if (selected.length == 0) {
				Ext.Msg.alert("提示", "请选择人员！");
			} else {
				for (var i = 0; i < selected.length; i += 1) {
					var member = selected[i].data;
					if (member.empCode) {
						codes.push(member.empCode);
						names.push(member.chsName);
					} else {
						store.remove(store.getAt(i));
					}
				}

				var emp = new Object();
				emp.codes = codes.toString();
				emp.names = names.toString();
				window.returnValue = emp;
				window.close();
			}
		}
	}

	function queryRecord() {
		var fuzzytext = fuuzy.getValue();
		store.baseParams = {
			fuzzy : fuzzytext
		};
		store.load({
					params : {
						start : 0,
						limit : 18
					}
				});
	}
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				items : [{
							region : 'west',
							id : 'west-panel',
							split : true,
							width : 250,
							layout : 'fit',
							minSize : 175,
							maxSize : 400,
							margins : '0 0 0 0',
							title : '部门树',
							collapsible : true,
							items : [new Ext.TabPanel({
										tabPosition : 'bottom',
										activeTab : 0,
										layoutOnTabChange : true,
										items : [{
													title : '部门树',
													border : false,
													autoScroll : true,
													items : [tree],
													listeners : {
														'activate' : function() {
															root.expand();
														}
													}
												}]
									})]
						}, {
							region : 'center',
							height : '100%',
							layout : 'border',
							items : [grid]
						}]
			});

});