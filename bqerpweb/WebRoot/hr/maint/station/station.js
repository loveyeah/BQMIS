Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'stationId'
	}, {
		name : 'stationTypeId'
	}, {
		name : 'stationCode'
	}, {
		name : 'stationName'
	}, {
		name : 'stationDuty'
	}, {
		name : 'isUse'
	}, {
		name : 'retrieveCode'
	}, {
		name : 'memo'
	}, {
		name : 'workKind'
	}, {
		name : 'stationLevelId'
	}, {
		name : 'stationLevelName'
	}]);
	var dataProxy = new Ext.data.HttpProxy({
		url : 'stationManage.action?method=getlist'
	});

	var theReader = new Ext.data.JsonReader({
		root : "root",
		totalProperty : "total"
	}, MyRecord);

	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
	// 分页
	store.load({
		params : {
			start : 0,
			limit : 18
		}
	});

	
	var txtStationName=new Ext.form.TextField({
	    name:'txtStationName',
	    emptyText:'岗位名称'
		
	});
	
	
	// store.load();
	var sm = new Ext.grid.CheckboxSelectionModel();

	var grid = new Ext.grid.GridPanel({
		region : "center",
		store : store,
		columns : [// 设计页面显示的表格信息，字段与Ext.data.Record.create匹配
		sm, {
			header : "岗位ID",
			hidden : true,
			sortable : true,
			dataIndex : 'stationId'
		}, {
			header : "岗位类别",
			width : 100,
			sortable : true,
			dataIndex : 'stationTypeId',
			align : 'center'
		}, {
			header : "岗位编号",
			sortable : true,
			dataIndex : 'stationCode',
			hidden : true
		}, {
			header : "岗位名称",
			width : 200,
			sortable : true,
			dataIndex : 'stationName',
			align : 'center'
		}, {
			header : "岗位职责",
			width : 100,
			sortable : true,
			dataIndex : 'stationDuty',
			align : 'center'
		}, {
			header : "岗位级别",
			width : 100,
			sortable : true,
			dataIndex : 'stationLevelName',
			align : 'center'
		}, {
			header : "工作类别",
			width : 100,
			sortable : true,
			dataIndex : 'workKind',
			align : 'center'
		}, {
			header : "使用标志",
			width : 75,
			sortable : true,
			dataIndex : 'isUse',
			align : 'center'
		}, {
			header : "检索码",
			width : 75,
			sortable : true,
			dataIndex : 'retrieveCode',
			align : 'center'
		}, {
			header : "备注",
			width : 200,
			sortable : true,
			dataIndex : 'memo',
			align : 'center'
		}],
		sm : sm,
		tbar : [txtStationName,{
		   text:'查询',
		   handler:queryRecord
		},'-',{
			text : "新增岗位",
			iconCls : 'add',
			handler : addRecord
		}, {
			text : "修改岗位",
			iconCls : 'update',
			handler : updateRecord
		}, {
			text : "删除岗位",
			iconCls : 'delete',
			handler : deleteRecord
		}],
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录",
			beforePageText : '页',
			afterPageText : "共{0}"
		})
	});
	grid.on("dblclick", updateRecord); // 双击修改

	// ---------------------------------------

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});

	// -------------------
      function queryRecord()
      {
      	
      	store.baseParams=
      	{
      		stationName:txtStationName.getValue()
      	}
     store.load({
		params : {
			start : 0,
			limit : 18
		}
	});
      }
	// -----删除----------
	function deleteRecord() {
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		var names = [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {

			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.stationId) {
					ids.push(member.stationId);
					names.push(member.stationName);
				} else {
					store.remove(store.getAt(i));
				}
			}
			Ext.Msg.confirm("删除", "是否确定删除岗位名称为'" + names + "'的记录？", function(
					buttonobj) {
				if (buttonobj == "yes") {
					Ext.lib.Ajax.request('POST',
							'stationManage.action?method=delete', {
								success : function(action) {
									Ext.Msg.alert("提示", "删除成功！")

									store.reload();
								},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
							}, 'ids=' + ids);
				}

			});
		}
	};

	// 创建增加和修改的页面

	function createupdatepanel(mytitle, id) {

		var stationId = {
			id : "stationId",
			xtype : "hidden",
			anchor : '90%',
			readOnly : true,
			name : 'staInfo.stationId'
		}
		// 岗位类别下拉框
		var comStore = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
				url : 'stationTypeManage.action'
			}),
			reader : new Ext.data.JsonReader({
				id : "station"
			}, [{
				name : 'id'
			}, {
				name : 'name'
			}])
		})
		var stationcombox = new Ext.form.ComboBox({
			// id : 'stationTypeId',
			id : 'stationTypeName',
			xtype : 'textfield',
			fieldLabel : '岗位类别',
			name : 'staInfo.stationTypeId',
			hiddenName : 'staInfo.stationTypeId',
			anchor : '90%',
			readOnly : true,
			store : comStore,
			displayField : "name",
			allowBlank : false,
			valueField : "id",
			mode : "remote",
			blankText : '请选择',
			emptyText : '请选择',
			triggerAction : 'all'
		})
		var stationCode = {
			id : "stationCode",
			xtype : "hidden",
			fieldLabel : '岗位编号',
			anchor : '90%',
			name : 'staInfo.stationCode'
		}
		var typeCode = {
			id : "stationTypeId",
			xtype : "hidden"
		}
		var stationName = new Ext.form.TextField({
			id : "stationName",
			xtype : "textfield",
			fieldLabel : '岗位名称',
			anchor : '90%',
			name : 'staInfo.stationName',
			allowBlank : false
		});

		var stationDuty = {
			id : "stationDuty",
			xtype : "textfield",
			anchor : '90%',
			fieldLabel : '岗位职责',
			name : 'staInfo.stationDuty'
		}
		var retrieveCode = {
			id : "retrieveCode",
			xtype : "textfield",
			fieldLabel : '检索码',
			anchor : '90%',
			name : 'staInfo.retrieveCode'
		}
		var memo = {
			id : "memo",
			xtype : "textarea",
			fieldLabel : '备注',
			anchor : '90%',
			name : 'staInfo.memo'
		}

		// add by drdu 090921
		var workKind = new Ext.form.ComboBox({
			fieldLabel : '工作类别',
			store : new Ext.data.SimpleStore({
				fields : ['value', 'text'],
				data : [['1', '常白班'], ['2', '运行班']]
			}),
			id : 'workKind',
			name : 'workKind',
			valueField : "value",
			displayField : "text",
			value : '1',
			mode : 'local',
			typeAhead : true,
			forceSelection : true,
			hiddenName : 'staInfo.workKind',
			editable : false,
			triggerAction : 'all',
			selectOnFocus : true,
			allowBlank : false,
			anchor : '90%'
		});

		// 定义岗位级别 add by drdu 090923
		var comStore = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
				url : 'findAllList.action'
			}),
			reader : new Ext.data.JsonReader({
				id : "stationLevel"
			}, [{
				name : 'id'
			}, {
				name : 'name'
			}])
		})
		var stationLevelComboBox = new Ext.form.ComboBox({
			id : 'stationLevelComboBox',
			xtype : 'textfield',
			fieldLabel : '岗位级别',
			name : 'staInfo.stationLevelId',
			hiddenName : 'staInfo.stationLevelId',
			anchor : '90%',
			readOnly : true,
			store : comStore,
			displayField : "name",
			allowBlank : false,
			valueField : "id",
			mode : "remote",
			blankText : '请选择',
			emptyText : '请选择',
			triggerAction : 'all'
		})

		stationName.on("change", function(filed, newValue, oldValue) {

			Ext.Ajax.request({
				url : 'manager/getRetrieveCode.action',
				params : {
					name : newValue
				},
				method : 'post',
				success : function(result, request) {
					var json = result.responseText;
					var o = eval("(" + json + ")");
					Ext.get("retrieveCode").dom.value = o.substring(0, 8);
				}
			});
		});

		var myaddpanel = new Ext.FormPanel({
			frame : true,
			labelAlign : 'center',
			labelWidth : 60,
			anchor : '90%',
			title : mytitle,
			items : [typeCode, stationId, stationcombox, stationCode,
					stationName, stationDuty, workKind, stationLevelComboBox,
					retrieveCode, {
						id : 'isUse',
						layout : 'column',
						isFormField : true,
						fieldLabel : '状态',
						border : false,
						items : [{
							columnWidth : .3,
							border : false,
							items : new Ext.form.Radio({
								id : 'U',
								boxLabel : '使用',
								name : 'staInfo.isUse',
								inputValue : 'U',
								checked : true
							})
						}, {

							columnWidth : .3,
							border : false,
							items : new Ext.form.Radio({
								id : 'N',
								boxLabel : '停用',
								name : 'staInfo.isUse',
								inputValue : 'N'
							})
						}, {
							columnWidth : .3,
							border : false,

							items : new Ext.form.Radio({
								id : 'L',
								boxLabel : '注销',
								name : 'staInfo.isUse',
								inputValue : 'L'
							})
						}]
					}, memo]

		});

		if (id != '自动生成') {
			// 修改时显示信息
			myaddpanel.form.load({
				url : "stationManage.action?method=getdata&id=" + id,
				success : function(form, action) {
					var o = eval("(" + action.response.responseText + ")");

					Ext.getCmp(o.data.isUse).setValue(true);
				}
			});
		}
		return myaddpanel;
	}

	// 创建增加和修改窗口
	function createaddwin(mypanel, op) {
		var mytxt = "增加成功";
		if (op == "update") {
			mytxt = "修改成功";
		}
		var win = new Ext.Window({
			width : 400,
			height : 350,
			buttonAlign : "center",
			items : mypanel,
			layout : 'fit',
			modal : true,
			buttons : [{
				text : '保存',
				iconCls : 'save',
				handler : function() {
					if (mypanel.getForm().isValid()) {
						if (op == "update") {
							Ext.get("staInfo.stationTypeId").dom.value = Ext
									.get("stationTypeId").dom.value;
						} else {
							Ext.get("stationTypeId").dom.value = "";
						}
						mypanel.getForm().submit({
							method : 'POST',
							url : 'stationManage.action?method=' + op,
							success : function(form, action) {
								var o = eval("(" + action.response.responseText
										+ ")");
								Ext.Msg.alert("注意", mytxt);
								win.close();
								grid.store.reload();
							},
							failure : function(form, action) {
								var o = eval("(" + action.response.responseText
										+ ")");
								Ext.Msg.alert('错误', o.errMsg);
							}
						});
					}
				}
			}, {
				text : '取消',
				iconCls : 'cancer',
				handler : function() {
					win.close();
				}
			}]

		});
		return win;

	}

	// 增加岗位信息
	function addRecord() {
		if (!myaddpanel) {
			var myaddpanel = createupdatepanel("增加岗位信息", "自动生成");
		}
		if (!win) {
			var win = createaddwin(myaddpanel, "add");
		}
		win.show();
	};

	// -----------------修改-----------------
	function updateRecord() {
		if (grid.selModel.hasSelection()) {
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				var record = grid.getSelectionModel().getSelected();
				if (!myaddpanel) {
					var myaddpanel = createupdatepanel("修改岗位信息", record
							.get("stationId"));
				}
				if (!win) {
					var win = createaddwin(myaddpanel, "update");
				}
				win.show();
				// add by drdu 090924
				Ext.getCmp('stationLevelComboBox').setValue(record
						.get("stationLevelId"));
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('stationLevelComboBox'), record
						.get("stationLevelName"));
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}

	}
});