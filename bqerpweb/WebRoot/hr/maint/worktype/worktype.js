Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	var layout;
	var Border = Ext.Viewport;
	var op = "insert";
	Ext.QuickTips.init();// 支持tips提示
	Ext.form.Field.prototype.msgTarget = 'side';// 提示的方式
	Ext.state.Manager.setProvider(new Ext.state.CookieProvider());

	var sm = new Ext.grid.CheckboxSelectionModel();
	var cm = new Ext.grid.ColumnModel([sm, {
		header : "工种ID",
		hidden : true,
		dataIndex : "typeOfWorkId"
	}, {
		header : "工种名称",
		dataIndex : "typeOfWorkName",
		sortable : true,
		width : 200
	}, {
		header : "状态",
		dataIndex : "isUse",
		width : 100
	}, {
		header : "工种类别",
		dataIndex : "typeOfWorkType",
		width : 100

	}, {
		header : "索引码",
		dataIndex : "retrieveCode",
		width : 100
	}]);

	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'getWorkTypeList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : "totalProperty",
			root : "root"
		}, [{
			name : "retrieveCode"
		}, {
			name : "isUse"
		}, {
			name : "typeOfWorkId"
		}, {
			name : "typeOfWorkName"
		}, {
			name : "typeOfWorkType"
		}])
	});
	var grid = new Ext.grid.GridPanel({
		ds : ds,
		cm : cm,
		sm : sm,
		tbar : [{
			id : "addrole",
			text : "新增工种",
			iconCls : 'add',
			handler : function() {
				op = "insert";
				blockForm.getForm().reset();
				showMemerAddWindow(); // 显示表单所在窗体
			}
		}, {
			id : "updaterole",
			text : "修改工种",
			iconCls : 'update',
			handler : function() {
				op = "update";
				var selrows = grid.getSelectionModel().getSelections();
				if (selrows.length > 0) {
					if (selrows.length > 1) {
						Ext.Msg.show({
							title : "提示信息",
							msg : "请从下面列表中选择一条需要修改的工种信息!",
							buttons : Ext.MessageBox.OK,
							icon : Ext.MessageBox.INFO
						});
					} else {
						showMemerAddWindow();
						// // 显示表单所在窗体
					}
				} else {
					Ext.Msg.show({
						title : "提示信息",
						msg : "请从下面列表中选择需要修改的工种信息!",
						buttons : Ext.MessageBox.OK,
						icon : Ext.MessageBox.INFO
					});
				}
			}
		}, {
			id : "deleterole",
			text : "删除工种",
			iconCls : 'delete',
			handler : function() {
				var sm = grid.getSelectionModel();
				var selected = sm.getSelections();
				var ids = [];
			
				var names = [];
				if (selected.length == 0) {
					Ext.Msg.alert("提示", "请选择要删除的记录！");
				} else {

					for (var i = 0; i < selected.length; i += 1) {
						var member = selected[i].data;
						if (member.typeOfWorkId) {
							ids.push(member.typeOfWorkId);
							names.push(member.typeOfWorkName);
						} else {

							store.remove(store.getAt(i));
						}
					}

						Ext.Msg.confirm('提示', '是否确定删除工种名称为'+names+'的记录？', function(response) {
				if (response == 'yes') {
					Ext.Ajax.request({
						method : 'post',
						url : 'com/deleteWorkType.action',
						params : {
							ids : ids.join(",")
						},
						success : function(action) {
							Ext.Msg.alert("提示", "删除成功！");
							 ds.reload();
						},
						failure : function() {
							Ext.Msg.alert('错误', '删除时出现未知错误.');
						}
					});
				}
			});
		}
}

			
		}]
	});

	ds.load();
	// 表单窗体
	var blockAddWindow;
	function updateWorktype() {
		op = "update";
		var selrows = grid.getSelectionModel().getSelections();
		if (selrows.length > 0) {
			if (selrows.length > 1) {
				Ext.Msg.show({
					title : "提示信息",
					msg : "请从下面列表中选择一条需要修改的工种信息!",
					buttons : Ext.MessageBox.OK,
					icon : Ext.MessageBox.INFO
				});
			} else {
				blockForm.getForm().reset();
				showMemerAddWindow();

			}
		}
	}
	grid.on("dblclick", updateWorktype);

	var data = [["启用", "U"], ["禁用", "N"], ["删除", "D"], ["注销", "L"]];

	var typeTypedata = [["特殊工种", "1"], ["行业工种", "2"], ["社会工种", "3"]];

	/**
	 * 以下是表单 __________________________________________________
	 */
	var typeID = {
		id : "typeOfWorkId",
		xtype : "textfield",
		fieldLabel : '工种编码',
		name : 'workTypeBeen.typeOfWorkId',
		anchor : '90%',
		readOnly : true
	}
	var retrieveCode = {
		id : "retrieveCode",
		xtype : "textfield",
		fieldLabel : '索引码',
		name : 'workTypeBeen.retrieveCode',
		anchor : '90%',
		readOnly : true
	}
	var typeNAME = new Ext.form.TextField({
		id : "typeOfWorkName",
		xtype : "textfield",
		fieldLabel : "工种名称<font color='red'>*</font>",
		allowBank : false,
		name : 'workTypeBeen.typeOfWorkName',
		allowBlank : false,
		blankText : '请输入工种名称...',
		anchor : '90%'

	});
	var typeTYPE = {
		id : "typeOfWorkType",
		xtype : "combo",
		allowBlank : false,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
			fields : ['name', 'id'],
			data : typeTypedata
		}),
		hiddenName : 'workTypeBeen.typeOfWorkType',
		displayField : 'name',
		valueField : 'id',
		fieldLabel : "工种类别",
		mode : 'local',
		emptyText : '请选择类别...',
		blankText : '请选择类别',
		anchor : '90%',
		readOnly : true
	}
	var isUse = {
		id : "isUse",
		xtype : "combo",
		allowBlank : false,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
			fields : ['text', 'value'],
			data : data
		}),
		value : 'U',
		hiddenName : 'workTypeBeen.isUse',
		displayField : 'text',
		valueField : 'value',
		fieldLabel : "状态",
		mode : 'local',
		emptyText : '请选择状态...',
		blankText : '请选择状态',
		anchor : '90%',
		readOnly : true
	}

	// 定义一个记录
	var RoleRecord = Ext.data.Record.create([{
		name : "typeOfWorkId"
	}, {
		name : "typeOfWorkName"
	}, {
		name : "typeOfWorkType"
	}, {
		name : "isUse"
	}, {
		name : "retrieveCode"
	}]);
	// 表单对象
	var blockForm = new Ext.FormPanel({
		labelAlign : 'center',
		labelWidth : 70,
		frame : true,
		items : [typeID, typeNAME, typeTYPE, isUse, retrieveCode],
		reader : new Ext.data.JsonReader({
			root : 'root'
		}, RoleRecord)
	});

	// 窗体对象
	function showMemerAddWindow() {

		if (!blockAddWindow) {
			blockAddWindow = new Ext.Window({
				// el : 'window_win',
				title : '',
				layout : 'fit',
				width : 350,
				height : 220,
				modal : true,
				closable : true,
				resizable : false,
				closeAction : 'hide',
				plain : true,
				// 面板中按钮的排列方式
				buttonAlign : 'center',
				items : [blockForm],
				buttons : [{
					text : '保存',
					iconCls : 'save',
					handler : function() {
						var url;
						if(op == "insert"){
							url ="addWorkType.action"; 
						}else{
							url = "updateWorkType.action"
						}
						if (blockForm.getForm().isValid()) {
							blockForm.getForm().submit({
								waitMsg : '保存中,请稍后...',
								url : url,
								params : {
									method : op
								},
								success : function() {
								    if(op=="insert")
								    {
									Ext.Msg.alert("注意", "增加成功！"); 
								    }
								    else
								    {
								    	Ext.Msg.alert("注意", "修改成功！"); 
								    }
									blockForm.getForm().reset();
									blockAddWindow.hide();
									ds.reload();
								},
								failure : function(form, action) {
									var o = eval("("
											+ action.response.responseText
											+ ")");
									Ext.Msg.alert('错误', o.errMsg);
								}
							});
							blockAddWindow.hide();
						}
					}
				}, {
					text : '取消',
					iconCls : 'cancer',
					handler : function() {
						blockForm.getForm().reset();
						blockAddWindow.hide();
					}
				}]
			});
		}
		if (op == "insert") {
			// 新增时，赋初始值
			blockAddWindow.setTitle("新增工种");
		} else {
			blockAddWindow.setTitle("修改工种");
			blockForm.form.load({
				url : "getWorkType.action",
				params : {
					worktypeids : grid.getSelectionModel().getSelections()[0].data.typeOfWorkId,
					method : "getrole"
				}
			});
		}
		blockAddWindow.show(Ext.get('getrole'));

	}
	// 设定布局器及面板

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});

});
