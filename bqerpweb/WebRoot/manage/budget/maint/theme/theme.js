Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	

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

	var item = Ext.data.Record.create([{
		name : 'topicId'
	},{
		name : 'topicCode'
	}, {
		name : 'topicName'
	}, {
		name : 'budgetType'
	}, {
		name : 'dataType'
	}, {
		name : 'timeType'
	},{
		name : 'isUse'
	}
	]);

	// 预算类别store
	var budgetTypeStore = new Ext.data.SimpleStore({
		fields : ['id','name'],
		data : [['1','公司汇总'],['2','部门预算']]
	})
	//数据类别store
	var dataTypeStore = new Ext.data.SimpleStore({
		fields : ['id','name'],
		data : [['1','经营预算'],['2','现金预算'],['3','财务预算']]
	})
	
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false

	});
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managebudget/getThemeList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalCount : "totalCount",
			root : "list"
		}, item)
	});
	ds.load({
		params : {
			start : 0,
			limit : 18
		}
	})

	// 事件状态
	var cm = new Ext.grid.ColumnModel([sm,
		new Ext.grid.RowNumberer({
			header : "行号",
			width : 31
		}),{
		header : '主题id',
		dataIndex : 'topicId',
		hidden : true
	}, {
		header : '主题编码',
		dataIndex : 'topicCode',
		align : 'right',
		width : 100,
//		css : CSS_GRID_INPUT_COL,
		editor : new Ext.form.TextField({
			style : 'cursor:pointer',
			allowBlank : false
//			name : 'topicCode'
		})
	}, {
		header : '主题名称',
		dataIndex : 'topicName',
		align : 'right',
		width : 110,
//		css : CSS_GRID_INPUT_COL,
		editor : new Ext.form.TextField({
			style : 'cursor:pointer',
			name : 'topicName',
			allowBlank : false
		})
	}, {
		header : '预算类别',
		dataIndex : 'budgetType',
		align : 'center',
		width : 110,
//		css : CSS_GRID_INPUT_COL,
		editor : new Ext.form.ComboBox({
			store : budgetTypeStore,
			displayField : 'name',
			valueField : 'id',
			mode : 'local',
			triggerAction : 'all',
			readOnly : true,
			allowBlank : false
		}),
		renderer : function(v){
			if(v == 1)
			{
				return '公司汇总';
			}
			else if(v == 2)
			{
				return '部门预算';
			}
		}
	}, {
		header : "数据类别",
		width : 100,
		dataIndex : 'dataType',
//		css : CSS_GRID_INPUT_COL,
		editor : new Ext.form.ComboBox({
			store : dataTypeStore,
			displayField : 'name',
			valueField : 'id',
			mode : 'local',
			triggerAction : 'all',
			readOnly : true,
			allowBlank : false
		}),
		renderer : function(v){
			if(v == 1)
			{
				return '经营预算';
			}
			else if(v == 2)
			{
				return '现金预算';
			}
			else if(v == 3)
			{
				return '财务预算';
			}
		}
	}
	]);
		
	var gridbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : ds,
		displayInfo : true,
		displayMsg : "显示第{0}条到{1}条，共{2}条",
		emptyMsg : "没有记录"
	});
	// 增加
	function addTheme() {
		var count = ds.getCount();
		var currentIndex = count;
		var o = new item({
			'topicId' : null,
			'topicCode' : null,
			'topicName' : null,
			'budgetType' : null,
			'dataType' : null
		});
		Grid.stopEditing();
		ds.insert(currentIndex, o);
		sm.selectRow(currentIndex);
		Grid.startEditing(currentIndex, 1);
	}

	// 删除记录
	var themeIds = new Array();
	function deleteTheme() {
		Grid.stopEditing();
		var sm = Grid.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("topicId") != null) {
					themeIds.push(member.get("topicId"));
				}
				Grid.getStore().remove(member);
				Grid.getStore().getModifiedRecords().remove(member);
			}
		}
	}
	// 保存
	function saveTheme() {
		Grid.stopEditing();
		var modifyRec = Grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || themeIds.length > 0) {
			for (var i = 0; i < modifyRec.length; i++) {
				if (modifyRec[i].data.topicCode == null
						|| modifyRec[i].data.topicCode == "") {
					Ext.Msg.alert('提示信息', '主题编码不可为空，请输入！')
					return;
				}
				if (modifyRec[i].data.topicName == null
						|| modifyRec[i].data.topicName == "") {
					Ext.Msg.alert('提示信息', '主题名称不可为空，请输入！')
					return;
				}
				if (modifyRec[i].data.budgetType == null
						|| modifyRec[i].data.budgetType == "") {
					Ext.Msg.alert('提示信息', '预算类别不可为空，请选择！')
					return;
				}
				if (modifyRec[i].data.dataType == null
						|| modifyRec[i].data.dataType == "") {
					Ext.Msg.alert('提示信息', '数据类别不可为空，请选择！')
					return;
				}
				if (modifyRec[i].data.topicCode.length > 3) {
					Ext.Msg.alert('提示信息', '主题编码最多为3位，请重新输入！')
					return;
				}
			}
			Ext.Msg.confirm('提示信息', '确定要保存修改吗？', function(button) {
				if (button == 'yes') {
					var addData = new Array();
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {

						if (modifyRec[i].get('isUse') == 'Y') {
							updateData.push(modifyRec[i].data);
						} else {
							addData.push(modifyRec[i].data);
						}

					}

					// alert(Ext.encode(addData[0].topicCode));
					for (var i = 0; i < addData.length - 1; i++) {
						for (var j = i + 1; j < addData.length; j++) {
							if (addData[i].topicCode == addData[j].topicCode) {
								Ext.Msg.alert('提示信息', '新增数据的主题编码不可重复，请重新输入！')
								ds.rejectChanges();
								return;
							}
							if (addData[i].topicName == addData[j].topicName) {
								Ext.Msg.alert('提示信息', '新增数据的主题名称不可重复，请重新输入！')
								ds.rejectChanges();
								return;
							}
						}
					}
					Ext.Ajax.request({
								url : 'managebudget/saveDependThemeInput.action',
								method : 'post',
								params : {
									isAdd : Ext.util.JSON.encode(addData),
									isUpdate : Ext.util.JSON.encode(updateData),
									isDelete : themeIds.join(",")
								},
								success : function(result, request) {
									var o = eval('(' + result.responseText
											+ ')');
									Ext.Msg.alert("提示信息", o.msg);
									if (o.msg.indexOf("输入") == -1) {
										ds.rejectChanges();
										themeIds = [];
										ds.reload();
									}
								},
								failure : function(result, request) {
									// var o = eval("(" + result.responseText
									// + ")");
									Ext.Msg.alert("提示信息", "数据保存修改失败！");
									ds.rejectChanges();
									themeIds = [];
									ds.reload();
								}
							})
				}
			})

		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}
	// 取消
	function cancerTheme() {
		var modifyRec = ds.getModifiedRecords();
		if (modifyRec.length > 0 || themeIds.length > 0) {
			Ext.Msg.confirm('提示信息', '确定要放弃修改吗？', function(button) {
						if (button == 'yes') {
							ds.reload();
							ds.rejectChanges();
							themeIds = [];
						} 
					})
		}
		else
		ds.reload()
	}
	function fuzzyQuery() {
		ds.load({
			params : {
				start : 0,
				limit : 18
			}
		});
		themeIds = [];
	};
	// tbar
	var contbar = new Ext.Toolbar({
		items : [{
			id : 'btnAdd',
			iconCls : 'add',
			text : "新增",
			handler : addTheme
		}, {
			id : 'btnDelete',
			iconCls : 'delete',
			text : "删除",
			handler : deleteTheme

		}, {
			id : 'btnCancer',
			iconCls : 'cancer',
			text : "取消",
			handler : cancerTheme

		}, '-', {
			id : 'btnSave',
			iconCls : 'save',
			text : "保存修改",
			handler : saveTheme
		}]

	});
	var Grid = new Ext.grid.EditorGridPanel({
		sm : sm,
		ds : ds,
		cm : cm,
		title : '预算主题维护',
		autoScroll : true,
		bbar : gridbbar,
//		tbar : contbar,
		border : true,
		clicksToEdit : 1,
		viewConfig : {
		// forceFit : true
		}
	});
	
	 Grid.on('beforeedit', function(obj) {
	 	if(obj.record.get('isUse') == 'Y'){
	 		if (obj.field == 'topicCode') {
    			return false;
    		}
	 	}
    		
    	return true;
    });

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		border : false,
		frame : false,
		items : [{
			layout : 'fit',
			border : false,
			frame : false,
			region : "center",
			items : [Grid]
		}]
	});

//	/** 事件状态选择 */
//	function blockStatusSelect() {
//		var url = "blockStatusSelect.jsp";
//		var equ = window.showModalDialog(url, window,
//				'dialogWidth=300px;dialogHeight=400px;status=no');
//		if (typeof(equ) != "undefined") {
//			var record = Grid.selModel.getSelected()
//			var names = equ.text.split(",");
//			var codes = equ.id.split(",");
//			record.set("jzztName", names[0]);
//			record.set("jzztId", codes[0]);
//			Grid.getView().refresh();
//		}
//	};
})