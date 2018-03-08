Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	

	function numberFormat(value) {
		if (value == null || value == '')
			return '0%';
		else {
			value = (value - 0) * 100;
			value = value.toString();
			if (value.indexOf(".") > 0) {
				value = value.substring(0, value.indexOf("."));
			}
			return value + "%";
		}
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

	var item = Ext.data.Record.create([{
		name : 'topicId',
		mapping : 0,
		type : 'int'
	},{
		name : 'topicName',
		mapping : 1,
		type : 'string'
	}, {
		name : 'coefficient',
		mapping : 2,
		type : 'float'
	}, {
		name : 'displayNo',
		mapping : 3,
		type : 'int'
	}
	]);	
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false

	});
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managexam/findAllTopic.action'
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
		header : '考核主题',
		dataIndex : 'topicName',
		align : 'right',
		width : 100,
//		css : CSS_GRID_INPUT_COL,
		editor : new Ext.form.TextField({
			style : 'cursor:pointer',
			allowBlank : false
		})
	}, {
		header : '分配系数',
		dataIndex : 'coefficient',
		align : 'right',
		width : 110,
//		css : CSS_GRID_INPUT_COL,
		editor : new Ext.form.NumberField({
			style : 'cursor:pointer',
			allowBlank : false
		}),
		renderer : numberFormat
	}, {
		header : '显示顺序',
		dataIndex : 'displayNo',
		align : 'center',
		width : 110,
//		css : CSS_GRID_INPUT_COL,
		editor : new Ext.form.NumberField({
			style : 'cursor:pointer',
			allowNegative : false,
			allowDecimal : false,
			allowBlank : true
		})
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
			'topicName' : null,
			'coefficient' : 0,
			'displayNo' : null
		});
		Grid.stopEditing();
		ds.insert(currentIndex, o);
		sm.selectRow(currentIndex);
		Grid.startEditing(currentIndex, 3);
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
				if (modifyRec[i].data.topicName == null
						|| modifyRec[i].data.topicName == "") {
					Ext.Msg.alert('提示信息', '考核主题不可为空，请输入！')
					return;
				}
				else if(modifyRec[i].data.topicName.length > 30)
				{
					Ext.Msg.alert('提示信息', '考核主题最多为30位！')
					return;
				}
			}
			Ext.Msg.confirm('提示信息', '确定要保存修改吗？', function(button) {
				if (button == 'yes') {
					var addData = new Array();
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {

						if (modifyRec[i].get('topicId') != null) {
							updateData.push(modifyRec[i].data);
						} else {
							addData.push(modifyRec[i].data);
						}

					}

//					for (var i = 0; i < addData.length - 1; i++) {
//						for (var j = i + 1; j < addData.length; j++) {
//							if (addData[i].topicName == addData[j].topicName) {
//								Ext.Msg.alert('提示信息', '新增数据的考核主题不可重复，请重新输入！')
//								ds.rejectChanges();
//								return;
//							}
//						}
//					}
					Ext.Msg.wait('操作正在进行中……');
					Ext.Ajax.request({
								url : 'managexam/saveModifiedTopicRec.action',
								method : 'post',
								params : {
									isAdd : Ext.util.JSON.encode(addData),
									isUpdate : Ext.util.JSON.encode(updateData),
									ids : themeIds.join(",")
								},
								success : function(result, request) {
									var o = Ext.util.JSON.decode(result.responseText);
									Ext.Msg.alert("提示信息", o.msg);
										ds.rejectChanges();
										themeIds = [];
										ds.reload();
								},
								failure : function(result, request) {
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
		title : '考核主题维护',
		autoScroll : true,
		bbar : gridbbar,
		tbar : contbar,
		border : true,
		clicksToEdit : 1,
		viewConfig : {
		// forceFit : true
		}
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


})