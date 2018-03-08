Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.MessageBox.minWidth=150;
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'sickSalaryId'
	}, {
		name : 'factWorkyearBottom'
	}, {
		name : 'factWorkyearTop'
	}, {
		name : 'localWorkageBottom'
	}, {
		name : 'localWorkageTop'
	}, {
		name : 'salaryScale'
	}, {
		name : 'memo'
	}]);

	var dataProxy = new Ext.data.HttpProxy({
		url : 'com/findSickSalaryList.action'
	});

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);

	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	});
	// 月出勤天数grid
	var grid = new Ext.grid.GridPanel({
		region : "center",
		layout : 'fit',
		store : store,
		columns : [sm, new Ext.grid.RowNumberer({
			header : '序号',
			width : 35
		}), {
			header : "ID",
			width : 75,
			sortable : true,
			dataIndex : 'sickSalaryId',
			hidden : true
		}, {
			header : "实际工作年限（下限）",
			width : 40,
			sortable : true,
			dataIndex : 'factWorkyearBottom'
		}, {
			header : "实际工作年限（上限）",
			width : 40,
			sortable : false,
			dataIndex : 'factWorkyearTop'
		}, {
			header : "本单位工作年限（下限）",
			width : 40,
			sortable : true,
			dataIndex : 'localWorkageBottom'
		}, {
			header : "本单位工作年限（上限）",
			width : 40,
			sortable : true,
			dataIndex : 'localWorkageTop'
		}, {
			header : "工资发放比例",
			width : 40,
			sortable : true,
			dataIndex : 'salaryScale'
		}, {
			header : "备注",
			width : 40,
			sortable : true,
			dataIndex : 'memo'
		}],
		viewConfig : {
			forceFit : true
		},
		tbar : [{
			text : "新增",
			iconCls : 'add',
			handler : addRecord
		}, {
			text : "修改",
			iconCls : 'update',
			handler : updateRecord
		}, {
			text : "删除",
			iconCls : 'delete',
			handler : deleteRecord
		}],
		sm : sm,
		frame : true,
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});

	grid.on("rowdblclick", updateRecord);
	// ---------------------------------------

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [grid]
	});

	var wd = 120;
	var sickSalaryId = new Ext.form.Hidden({
		id : "sickSalaryId",
		width : wd,
		name : 'sickSalary.sickSalaryId'
	});

	var factWorkyearBottom = new Ext.form.NumberField({
		id : "factWorkyearBottom",
		fieldLabel : '实际工作年限（下限）',
		width : wd,
		allowBlank : false,
		name : 'sickSalary.factWorkyearBottom'
	});

	var factWorkyearTop = new Ext.form.NumberField({
		id : "factWorkyearTop",
		fieldLabel : '实际工作年限（上限）',
		width : wd,
		allowBlank : false,
		name : 'sickSalary.factWorkyearTop'
	});
	
	var localWorkageBottom = new Ext.form.NumberField({
		id : "localWorkageBottom",
		fieldLabel : '本单位工作年限（下限）',
		width : wd,
		allowBlank : false,
		name : 'sickSalary.localWorkageBottom'
	});
	
	var localWorkageTop = new Ext.form.NumberField({
		id : "localWorkageTop",
		fieldLabel : '本单位工作年限（上限）',
		width : wd,
		allowBlank : false,
		name : 'sickSalary.localWorkageTop'
	});
	
	var salaryScale = new Ext.form.NumberField({
		id : "salaryScale",
		fieldLabel : '工资发放比例',
		width : wd,
		name : 'sickSalary.salaryScale'
	});
	
	var memo = new Ext.form.TextArea({
		id : "memo",
		fieldLabel : '备注',
		width : 410,
		name : 'sickSalary.memo',
		height : 100
	});

	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		labelWidth : 150,
		closeAction : 'hide',
		title : '增加/修改病假工资标准',
		items : [{
				    layout : "column",
					border : false,
					items : [{
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [sickSalaryId, factWorkyearBottom]
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [factWorkyearTop]
					}]
				},
				{
				    layout : "column",
					border : false,
					items : [{
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [localWorkageBottom]
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [localWorkageTop]
					}]
				}, salaryScale, memo]
	});

	var win = new Ext.Window({
		width : 600,
		height : 280,
		buttonAlign : "center",
		items : [myaddpanel],
		layout : 'fit',
		closeAction : 'hide',
		resizable : false,
		modal : true,
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
				var myurl = "";
				if (sickSalaryId.getValue() == "") {
					myurl = "com/addSickSalary.action";
				} else {
					myurl = "com/updateSickSalary.action";
				}
				if (!checkInput())
					return;
				myaddpanel.getForm().submit({
				    method : 'POST',
				    url : myurl,
					params : {
					},
					success : function(form, action) {
					    var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("注意", "<center>" + o.msg + "</center>");
						win.hide();
						queryRecord();
					},
					faliue : function() {
						Ext.Msg.alert('错误', '出现未知错误.');
					}
				});
			}
		}, {
			text : '取消',
			iconCls : 'cancer',
			handler : function() {
				win.hide();
			}
		}]
	});

	function checkInput() {
		var msg = "";
		if (factWorkyearBottom.getValue() == "") {
			msg = "'实际工作年限（下限）'";
		}
		if (factWorkyearTop.getValue() == "") {
			if (msg == "")
				msg = "'实际工作年限（上限）'";
			else
				msg = msg + ",'实际工作年限（上限）'";
		}
		if (localWorkageBottom.getValue() == "") {
			if (msg == "")
				msg = "'本单位工作年限（下限）'";
			else
				msg = msg + ",'本单位工作年限（下限）'";
		}
		if (localWorkageTop.getValue() == "") {
			if (msg == "")
				msg = "'本单位工作年限（上限）'";
			else
				msg = msg + ",'本单位工作年限（上限）'";
		}
		if (msg != "") {
			Ext.Msg.alert("提示", "请输入" + msg);
			return false
		}
		if (factWorkyearBottom.getValue() > factWorkyearTop.getValue()){
			Ext.Msg.alert("提示", "实际工作年限（上限）应大于等于实际工作年限（下限）");
			return false;
		}
		if (localWorkageBottom.getValue() > localWorkageTop.getValue()){
			Ext.Msg.alert("提示", "本单位工作年限（上限）应大于等于本单位工作年限（下限）");
			return false;
		}
		return true;
	}
	function queryRecord() {
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}

	function addRecord() {
		myaddpanel.getForm().reset();
		win.show();
		myaddpanel.setTitle("病假工资增加");
	}

	function updateRecord() {
		if (grid.selModel.hasSelection()) {
			var records = grid.getSelectionModel().getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("提示", "请选择其中一项进行编辑！");
			} else {
				win.show();
				var record = grid.getSelectionModel().getSelected();
				myaddpanel.getForm().reset();
				myaddpanel.form.loadRecord(record);
				factWorkyearBottom.setValue(record.get("factWorkyearBottom"));
				factWorkyearTop.setValue(record.get("factWorkyearTop"));
				localWorkageBottom.setValue(record.get("localWorkageBottom"));
				localWorkageTop.setValue(record.get("localWorkageTop"));
				salaryScale.setValue(record.get("salaryScale"));
				memo.setValue(record.get("memo"));
				
				myaddpanel.setTitle("病假工资修改");
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}

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
				if (member.sickSalaryId) {
					ids.push(member.sickSalaryId);
				} else {
					store.remove(store.getAt(i));
				}
			}
			Ext.Msg.confirm("删除", "是否确定删除所选记录？", function(buttonobj) {
				if (buttonobj == "yes") {
					Ext.lib.Ajax.request('POST',
							'com/deleteSickSalary.action', {
								success : function(action) {
									Ext.Msg.alert("提示", "<center>删除成功！</center>")
									queryRecord();
								},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
							}, 'ids=' + ids);
				}
			});
		}
	}

	queryRecord();
});