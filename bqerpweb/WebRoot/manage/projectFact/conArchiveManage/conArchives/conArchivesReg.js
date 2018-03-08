Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Object.prototype.toJSONString = function() {
	var obj = this;
	var str = '';
	var obj = this;
	if (obj instanceof Array) {
		str = '[';
		for (var i = 0; i < obj.length; i++) {
			str += obj[i].toJSONString() + ',';
		}
		if (obj.length > 0) {
			str = str.slice(0, -1);
		}
		str += ']';
		return str;
	}
	str = '{';
	for (var pro in obj) {
		var temp = obj[pro];
		if (temp == null)
			str += '"' + pro + '":' + '"null",';
		if (temp == undefined)
			str += '"' + pro + '":' + '"undefined",';
		else if (typeof temp == 'object') {
			if (temp instanceof Date) {
				Str += '"' + pro + '":"' + renderDate(temp) + '",';
			} else {
				Str += '"' + pro + '":"' + temp.toJSONString + '",';
			}
		} else if (typeof temp == 'number' || temp == 'boolean') {
			str += '"' + pro + '":' + temp + ',';
		} else if (typeof temp != 'function') {
			str += '"' + pro + '":' + '"' + temp + '",';
		}
	}
	if (str.length > 1 && "{" != str) {
		str = str.substring(0, str.length - 1);
	}
	str += '}';
	return str;
}

// 渲染时间格式
function renderDate(value) {
	return value ? value.dateFormat('Y-m-d H:i:s') : '';
}

Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var methods = "update";
	function checkTime2() {
		var startDate = getCurrentDate();
		var endDate = this.value;
		archiveGrid.getSelectionModel().getSelected().set("weaveDate", endDate);
	}
	function getCurrentDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t;
		return s;
	}
	function saveModify() {
		if (methods != "add") {
			var modifyRec = archiveGrid.getStore().getModifiedRecords();
			if (modifyRec.length > 0) {
				var arr = [];
				var modify_str = "";
				for (var i = 0; i < modifyRec.length; i++) {
					var o = modifyRec[i].data;
					arr.push(o);
					// modify_str += o.toJSONString()+",";
				}

				modify_str = arr.toJSONString()
				Ext.Ajax.request({
					url : 'managecontract/updateArchives.action',
					method : 'post',
					params : {
						str : modify_str
					},
					success : function(result, request) {
						archiveDs.reload();
					},
					failure : function(result, request) {
						Ext.MessageBox.alert('提示信息', '未知错误！')
					}
				})
			}
		}
		methods = "";
	}
	function saveAdd() {
		if (methods != "update") {
			var _count = archiveDs.getTotalCount()
			var record = archiveGrid.getStore().getAt(_count);
			var o = record.data;
			if (record.data.undertakeNo == "" && record.data.archivesName == ""
					&& record.data.archivesCount == "") {
				Ext.Msg.alert('提示信息', '注意：档号，案卷提名,份数不能为空！');
				return
			}
			var add_str = o.toJSONString();
			Ext.Ajax.request({
				url : 'managecontract/addarchive.action',
				method : 'post',
				params : {
					str : add_str
				},
				success : function(result, request) {
					archiveDs.reload();
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('提示信息', '未知错误！')
				}
			})
		}
		methods = "";
	}
	var data = new Date();
	var sdate = renderDate(data);
	// 创建一个初始化对象
	var archiveTbar = new Ext.Toolbar({
		items : [{
			id : 'add',
			text : "增加",
			iconCls : 'add',
			handler : function() {
				// if (methods == "add") {
				// Ext.Msg.confirm('提示', '是否保存先前增加的行？', function(b) {
				// if (b == 'Yes') {
				// saveAdd();
				// }
				// })
				// } else {
				// if (methods == "update") {
				// var modifyRec = archiveGrid.getStore()
				// .getModifiedRecords();
				// if (modifyRec.length > 0) {
				// Ext.Msg.confirm('提示', '是否保存所改动的行？', function(b) {
				// if (b == 'yes') {
				// saveModify();
				// }
				// })
				// }
				// }
				// }
				methods = "add";
				Ext.get("btnDelete").dom.disabled = false;
				Ext.get("btnSave").dom.disabled = false;
				var count = archiveDs.getTotalCount();
				archiveGrid.stopEditing();
				var o = ob;
				archiveDs.insert(count, o);
				archiveGrid.startEditing(count + 1, 0);
			}
		}, '-', {
			id : 'btnDelete',
			text : "删除",
			iconCls : 'delete',
			handler : function() {
				var rec = archiveGrid.getSelectionModel().getSelections();
				if (rec.length > 0) {
					Ext.Msg.confirm('提示信息', '你确定要删除选择的记录吗？', function(b) {
						if (b == "yes") {
							var archiveIds = "";
							for (var i = 0; i < rec.length; i++) {
								archiveIds += rec[i].get("archivesId") + ",";
							}
							if (archiveIds.length > 0)
								archiveIds = archiveIds.substring(0,
										archiveIds.length - 1);
							Ext.Ajax.request({
								url : 'managecontract/deleteArchive.action?archiveIds='
										+ archiveIds,
								methods : 'post',
								succsess : function(result, request) {
									var o = eval('(' + request.responseText()
											+ ')');
									archiveDs.reload();
									Ext.Msg.alert('提示信息', o.succ.Msg);
								},
								failure : function(result, request) {
									Ext.Msg.alert('提示信息', '删除失败,请联系管理员！');
								}
							})
						}
					})
				} else {
					Ext.Msg.alert('提示信息', '请选择你要删除的行！');
				}
			}
		}, '-', {
			id : 'btnSave',
			text : "保存",
			iconCls : 'save',
			handler : function() {
				if (methods == "add")
					saveAdd();
				else
					saveModify();
			}
		}, '-', {
			id : 'btnCanse',
			text : "取消",
			iconCls : 'canse',
			handler : function() {
				archiveDs.reload();
			}
		}, '-', {
			id : 'btn',
			text : "封面",
			handler : function() {
			}
		}, '-', {
			id : 'btnTab',
			text : "备考表",
			handler : function() {
			}
		}]
	})
	var archive = Ext.data.Record.create([{
		name : 'archivesId'
	}, {
		name : 'undertakeNo'
	}, {
		name : 'archivesName'
	}, {
		name : 'archivesCount'
	}, {
		name : 'pieceCount'
	}, {
		name : 'pageCount'
	}, {
		name : 'unitName'
	}, {
		name : 'weaveDate'
	}, {
		name : 'timeLimit'
	}, {
		name : 'keepLevel'
	}, {
		name : 'keepPosition'
	}, {
		name : 'upbuildPeople'
	}, {
		name : 'upbuildDate'
	}, {
		name : 'jerquePeople'
	}, {
		name : 'jerqueDate'
	},
			// {
			// name : 'microNo'
			// },
			{
				name : 'charCount'
			}, {
				name : 'drawCount'
			}, {
				name : 'memo'
			}]);
	var ob = new archive({
		'archivesId' : "123",
		'undertakeNo' : '',
		'archivesName' : '',
		'archivesCount' : '',
		'pieceCount' : '',
		'pageCount' : '',
		'unitName' : '',
		'weaveDate' : sdate,
		'timeLimit' : '',
		'keepLevel' : '',
		'keepPosition' : '',
		'upbuildPeople' : '',
		'upbuildDate' : sdate,
		'jerquePeople' : '',
		'jerqueDate' : sdate,
		// 'microNo' : '',
		'charCount' : '',
		'drawCount' : '',
		'memo' : ''
	});
	var archiveSm = new Ext.grid.RowSelectionModel({
		singleSelect : true,
		listeners : {
			rowselect : function(sm, row, rec) {
				// // var modifyRec =
				// archiveGrid.getStore().getModifiedRecords();
				// var curerentRow = archiveGrid.getStore().getAt(row);
				// // if (curerentRow.length > 0) {
				// var undertakeNo = curerentRow.get("undertakeNo");
				// Ext.Ajax.request({
				// url : 'managecontract/judgeArchive.action?undertakeNo='
				// + undertakeNo,
				// method : 'post',
				// success : function(result, request) {
				// var obj = eval('(' + result.responseText + ')');
				// var isFlag = obj.isFlag;
				// if (isFlag) {
				// Ext.get("btnDelete").dom.disabled = true;
				// Ext.get("btnSave").dom.disabled = true;
				// } else {
				// Ext.get("btnDelete").dom.disabled = false;
				// Ext.get("btnSave").dom.disabled = false;
				// }
				// },
				// failure : function(result, request) {
				// Ext.Msg.alert('提示信息', '未知错误!')
				// }
				// })
				// }
				// if (modifyRec.length > 0) {
				// if (!Ext.get("btnDelete").dom.disabled) {
				// var modify_str = "";
				// Ext.Msg.confirm('提示信息', '是否保存所改动的行？', function(b) {
				// if (b == "yes")
				// saveModify();
				// })
				// }
				// }
			}
		}
	});
	var archiveCm = new Ext.grid.ColumnModel([{
		header : 'id',
		dataIndex : 'archivesId',
		hidden : true
	}, new Ext.grid.RowNumberer({
		header : '项次号',
		width : 50,
		align : 'left'
	}), {
		header : '档号',
		dataIndex : 'undertakeNo',
		align : 'center',
		editor : new Ext.form.TextField({
			allowBlank : false
		})
	}, {
		header : '案卷提名',
		dataIndex : 'archivesName',
		align : 'center',
		editor : new Ext.form.TextField({
			allowBlank : false
		})
	}, {
		header : '份数',
		dataIndex : 'archivesCount',
		align : 'center',
		editor : new Ext.form.NumberField({
			allowBlank : false,
			allowNegative : false,
			maxValue : 1000000000
		})
	}, {
		header : '每份件数',
		dataIndex : 'pieceCount',
		align : 'center',
		editor : new Ext.form.NumberField({
			allowBlank : false,
			allowNegative : false,
			maxValue : 1000000000
		})
	}, {
		header : '每份页数',
		dataIndex : 'pageCount',
		align : 'center',
		editor : new Ext.form.NumberField({
			allowBlank : false,
			allowNegative : false,
			maxValue : 1000000000
		})
	}, {
		header : '文字页数',
		dataIndex : 'charCount',
		align : 'center',
		editor : new Ext.form.NumberField({
			allowBlank : false,
			allowNegative : false,
			maxValue : 1000000000
		})
	}, {
		header : '图纸页数',
		dataIndex : 'drawCount',
		align : 'center',
		editor : new Ext.form.NumberField({
			allowBlank : false,
			allowNegative : false,
			maxValue : 1000000000
		})
	}, {
		header : '编制单位',
		dataIndex : 'unitName',
		align : 'center',
		editor : new Ext.form.TextField({
			allowBlank : false
		})
	}, {
		header : '编制日期',
		dataIndex : 'weaveDate',
		align : 'center',
		// editor : new Ext.form.DateField({
		// format : 'Y-m-d',
		// name : 'weaveDate',
		// id : 'weaveDate',
		// checked : true,
		// value : sdate,
		// width : 150,
		// listeners : {
		// focus : function() {
		// WdatePicker({
		// // 时间格式
		// startDate : '%y-%M-%d',
		// dateFmt : 'yyyy-MM-dd',
		// alwaysUseStartDate : false
		// });
		//
		// }
		//
		// }
		// })
		editor : new Ext.form.TextField({
			format : 'Y-m-d',
			itemCls : 'sex-left',
			readOnly : true,
			clearCls : 'allow-float',
			checked : true,
			width : 100,
			anchor : '100%',
			style : 'cursor:pointer',
			listeners : {
				focus : function() {
					WdatePicker({
						// 时间格式
						startDate : '%y-%M-%d',
						dateFmt : 'yyyy-MM-dd',
						alwaysUseStartDate : false,
						onclearing : function() {
							archiveGrid.getSelectionModel().getSelected().set(
									"weaveDate", "")
						},
						onpicked : checkTime2
					});

				}
			}
		})
	}, {
		header : '保管期限',
		dataIndex : 'timeLimit',
		align : 'center',
		editor : new Ext.form.ComboBox({
			typeAhead : true,
			triggerAction : 'all',
			listClass : 'x-combo-list-small',
			id : 'timeLimit',
			name : 'timeLimit',
			store : [['1', '永久'], ['2', '长期'], ['3', '短期']],
			valueField : "value",
			displayField : "text",
			mode : 'local',
			selectOnFocus : true
		}),
		renderer : function(v) {
			if ("1" == v)
				return '永久';
			if ("2" == v)
				return '长期';
			else
				("3" == v)
			return '短期'
		}
	}, {
		header : '密级',
		dataIndex : 'keepLevel',
		align : 'center',
		editor : new Ext.form.TextField({
			allowBlank : false
		})
	}, {
		header : '库位号',
		dataIndex : 'keepPosition',
		align : 'center',
		editor : new Ext.form.TextField({
			allowBlank : false
		})
	}, {
		header : '检查人',
		dataIndex : 'jerquePeople',
		align : 'center',
		editor : new Ext.form.TextField({
			allowBlank : false
		})
	}, {
		header : '检查日期',
		dataIndex : 'jerqueDate',
		align : 'center'
//		 editor : new Ext.form.DateField({
//			format : 'Y-m-d',
//			name : 'jerqueDate',
//			id : 'jerqueDate',
//			checked : true,
//			value : sdate,
//			width : 150
//		 })
	}, {
		header : '立卷人',
		dataIndex : 'upbuildPeople',
		align : 'center',
		editor : new Ext.form.TextField({
			allowBlank : false
		})
	}, {
		header : '立卷日期',
		dataIndex : 'upbuildDate'
//		editor : new Ext.form.DateField({
//			format : 'Y-m-d',
//			name : 'upbuildDate',
//			id : 'upbuildDate',
//			checked : true,
//			value : sdate,
//			width : 150
//		})
		
	}, {
		header : '备注',
		dataIndex : 'memo',
		align : 'center',
		editor : new Ext.form.TextField({
			allowBlank : false
		})
	}]);
	archiveCm.defaultSortable = true;
	var archiveDs = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managecontract/findArchiveList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : 'list'
		}, archive)
	});
	archiveDs.load();
	archiveBbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : archiveDs,
		displayInfo : true,
		displayMsg : '共 {2} 条',
		emptyMsg : "没有记录"
	});
	var archiveGrid = new Ext.grid.EditorGridPanel({
		store : archiveDs,
		cm : archiveCm,
		sm : archiveSm,
		tbar : archiveTbar,
		bbar : archiveBbar,
		frame : false,
		border : false,
		autoWidth : true,
		autoScroll : true,
		clicksToEdit : 1
	});
	var layout = new Ext.Viewport({
		layout : 'fit',
		border : true,
		items : [{
			layout : 'fit',
			margins : '0 0 0 0',
			region : 'center',
			autoScroll : true,
			items : archiveGrid
		}]
	});
})