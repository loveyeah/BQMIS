Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	// 所属机组或系统
	var storeChargeBySystem = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'workticket/getDetailEquList.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, [{
			name : 'blockCode',
			mapping : 'blockCode'
		}, {
			name : 'blockName',
			mapping : 'blockName'
		}])
	});
	storeChargeBySystem.load();

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

	function getMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t
		return s;
	}
	
	function checkTime1() {
		var date = this.value;
		Grid.getSelectionModel().getSelected().set("startDate", date);
	}
	function checkTime2() {
		var date = this.value;
		Grid.getSelectionModel().getSelected().set("endDate", date);
	}
	var con_item = Ext.data.Record.create([{
		name : 'sjlrId'
	}, {
		name : 'blockCode'
	}, {
		name : 'jzztId'
	}, {
		name : 'startDate'
	}, {
		name : 'endDate'
	}, {
		name : 'keepTime'
	}, {
		name : 'reduceExert'
	}, {
		name : 'stopTimes'
	}, {
		name : 'successTimes'
	}, {
		name : 'failureTimes'
	}, {
		name : 'repairMandays'
	}, {
		name : 'repairCost'
	}, {
		name : 'stopReason'
	}, {
		name : 'jzztCode'
	}, {
		name : 'jzztName'
	}]);

	var con_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false

	});
	var con_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'productionrec/getBlockDependList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalCount : "totalCount",
			root : "list"
		}, con_item)
	});
	con_ds.load({
		params : {
			start : 0,
			limit : 18
		}
	})

	// 事件状态
	var con_item_cm = new Ext.grid.ColumnModel([con_sm, {
		header : '机组',
		dataIndex : 'blockCode',
		align : 'center',
		width : 100,
		editor : new Ext.form.ComboBox({
			store : storeChargeBySystem,
			displayField : "blockName",
			valueField : "blockCode",
			hiddenName : 'bolckCode',
			mode : 'local',
			triggerAction : 'all',
			readOnly : true
		}),
		renderer : function(v) {
			var recIndex = storeChargeBySystem.find("blockCode", v);
			return storeChargeBySystem.getAt(recIndex).get("blockName");
		}
	}, {
		header : '事件开始时间',
		dataIndex : 'startDate',
		align : 'center',
		width : 110,
		editor : new Ext.form.TextField({
			style : 'cursor:pointer',
			readOnly : true,
			listeners : {
				focus : function() {
					WdatePicker({
						// 时间格式
						startDate : '%y-%M-%d 00:00:00',
						dateFmt : 'yyyy-MM-dd HH:mm:ss',
						alwaysUseStartDate : false,
						onpicked : checkTime1
					});

				}
			}
		}),
		renderer : function(v) {
			return v.substring(0, 10) + " " + v.substring(11, 19)
		}

	}, {
		header : '事件结束时间',
		dataIndex : 'endDate',
		align : 'center',
		width : 110,
		editor : new Ext.form.TextField({
			style : 'cursor:pointer',
			readOnly : true,
			listeners : {
				focus : function() {
					WdatePicker({
						// 时间格式
						startDate : '%y-%M-%d 00:00:00',
						dateFmt : 'yyyy-MM-dd HH:mm:ss',
						alwaysUseStartDate : false,
						onpicked : checkTime2
					});

				}
			}
		}),
		renderer : function(v) {
			return v.substring(0, 10) + " " + v.substring(11, 19)
		}
	}, {
		header : "事件编码",
		width : 100,
		dataIndex : 'jzztId',
		hidden : true,
		renderer : function(value) {
			if (value == null)
				value = "";
			return "<span style='color:gray;'>" + value + "</span>";

		}
	}, {
		header : '事件状态',
		dataIndex : 'jzztName',
		align : 'center',
		width : 120,
		editor : new Ext.form.TriggerField({
			width : 320,
			allowBlank : false,
			onTriggerClick : blockStatusSelect,
			listeners : {
				render : function(f) {
					f.el.on('keyup', function(e) {
						Grid.getSelectionModel().getSelected().set("jzztId",'temp');
					});
				}
			}
		})
	}, {
		header : '事件持续时间',
		dataIndex : 'keepTime',
		width : 100,
		align : 'center',
		editor : new Ext.form.NumberField({
			style : 'direction:rtl'
		})
	}, {
		header : '降低出力',
		dataIndex : 'reduceExert',
		align : 'center',
		width : 60,
		editor : new Ext.form.NumberField()
	}, {
		header : '停运次数',
		dataIndex : 'stopTimes',
		align : 'center',
		width : 60,
		editor : new Ext.form.NumberField({
			decimalPrecision : 0
		})
	}, {
		header : '启动成功次数',
		dataIndex : 'successTimes',
		align : 'center',
		width : 90,
		editor : new Ext.form.NumberField({
			decimalPrecision : 0
		})
	}, {
		header : '启动失败次数',
		dataIndex : 'failureTimes',
		align : 'center',
		width : 90,
		editor : new Ext.form.NumberField({
			decimalPrecision : 0
		})
	}, {
		header : '检修工日',
		dataIndex : 'repairMandays',
		align : 'center',
		width : 70,
		editor : new Ext.form.NumberField({})
	}, {
		header : '检修费用',
		dataIndex : 'repairCost',
		align : 'center',
		width : 70,
		editor : new Ext.form.NumberField()
	}, {
		header : '事件原因',
		dataIndex : 'stopReason',
		align : 'center',
		width : 70,
		editor : new Ext.form.TextField()
	}]);

	con_item_cm.defaultSortable = true;
	var gridbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : con_ds,
		displayInfo : true,
		displayMsg : "显示第{0}条到{1}条，共{2}条",
		emptyMsg : "没有记录",
		beforePageText : '',
		afterPageText : ""
	});
	// 增加
	function addTopic() {
		var count = con_ds.getCount();
		var currentIndex = count;
		var o = new con_item({
			'blockCode' : 1,
			'startDate' : getDate(),
			'endDate' : getDate(),
			'jzztName' : '',
			'keepTime' : '',
			'reduceExert' : '',
			'stopTimes' : '',
			'successTimes' : '',
			'failureTimes' : '',
			'repairMandays' : '',
			'repairCost' : '',
			'stopReason' : ''
		});
		Grid.stopEditing();
		con_ds.insert(currentIndex, o);
		con_sm.selectRow(currentIndex);
		Grid.startEditing(currentIndex, 1);
	}

	// 删除记录
	var topicIds = new Array();
	function deleteTopic() {
		Grid.stopEditing();
		var sm = Grid.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("sjlrId") != null) {
					topicIds.push(member.get("sjlrId"));
				}
				Grid.getStore().remove(member);
				Grid.getStore().getModifiedRecords().remove(member);
			}
		}
	}
	// 保存
	function saveTopic() {
		Grid.stopEditing();
		var modifyRec = Grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || topicIds.length > 0) {
			if (!confirm("确定要保存修改吗?"))
				return;
			var updateData = new Array();
			for (var i = 0; i < modifyRec.length; i++) {
				updateData.push(modifyRec[i].data);
			}
			Ext.Ajax.request({
				url : 'productionrec/saveDependInput.action',
				method : 'post',
				params : {
					// isAdd : Ext.util.JSON.encode(newData),
					isUpdate : Ext.util.JSON.encode(updateData),
					isDelete : topicIds.join(",")
				},
				success : function(result, request) {
					con_ds.rejectChanges();
					topicIds = [];
					con_ds.reload();
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('提示信息', '操作失败！')
				}
			})
		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}
	// 取消
	function cancerTopic() {
		var modifyRec = con_ds.getModifiedRecords();
		if (modifyRec.length > 0 || topicIds.length > 0) {
			if (!confirm("确定要放弃修改吗"))
				return;
			con_ds.reload();
			con_ds.rejectChanges();
			topicIds = [];
		} else {
			con_ds.reload();
			con_ds.rejectChanges();
			topicIds = [];
		}
	}
	function fuzzyQuery() {
		con_ds.load({
			params : {
				start : 0,
				limit : 18
			}
		});
		topicIds = [];
	};
	
	function query() {
		ds.baseParams = {
			month : month.getValue()
		}
		ds.load({
					params : {
						start : 0,
						limit : 18
					}
				})
		ids = [];
	}

	// 单位
	var dept = new Ext.form.TextField({
				id : 'dept',
				name : 'dept',
				value : '大唐灞桥热电厂',
				disabled : true
			})
	
	// 上报时间
	var month = new Ext.form.TextField({
				fieldLabel : "月份<font color='red'>*</font>",
				id : 'month',
				readOnly : true,
				width : 100,
				anchor : '59%',
				name : 'month',
				value : getMonth(),
				listeners : {
					focus : function() {
						WdatePicker({
									alwaysUseStartDate : true,
									dateFmt : "yyyy-MM",
									isShowClear : false,
									onpicked : function(){
										query();
									}
								});
						this.blur();
					}
				}
			});
	// tbar
	var contbar = new Ext.Toolbar({
		 items : ['单位:', dept, '-', '月份:', month, '-', {
							id : 'btnQuery',
							iconCls : 'query',
							text : '查询',
							hidden : true,
							handler : query
						}, {
			id : 'btnAdd',
			iconCls : 'add',
			text : "新增",
			handler : addTopic
		}, {
			id : 'btnDelete',
			iconCls : 'delete',
			text : "删除",
			handler : deleteTopic

		}, {
			id : 'btnCancer',
			iconCls : 'cancer',
			text : "取消",
			handler : cancerTopic

		}, '-', {
			id : 'btnSave',
			iconCls : 'save',
			text : "保存修改",
			handler : saveTopic
		},'-', {
			id : 'btnExport',
			iconCls : 'export',
			text : '导出',
			handler : exportRecord
	}]
	});
	// 导出
	function tableToExcel(tableHTML) {
		window.clipboardData.setData("Text", tableHTML);
		try {
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExApp.DisplayAlerts = false;
			ExApp.visible = true;
		} catch (e) {
			if (e.number != -2146827859)
				Ext.Msg.alert('提示信息', "您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}
	function exportRecord() {	
		Ext.Msg.confirm('提示', '确认要导出数据？', function(id) {
			if (id == 'yes') {
				var tableHeader = "<table border=1><tr><th colspan = 11>机组月度事件数据报表</th></tr>";
				var html = [tableHeader];
				var time = month.getValue().substring(0, 4) + "年"
						+ month.getValue().substring(5, 7) + "月";
				html
						.push("<tr><td colspan = 9></td>"
								+ "<td colspan = 2>月份："
								+ time + "</td></tr>")
				html
						.push('<tr><th>机组</th><th>事件起始时间</th><th>事件终止时间</th><th>事件状态</th><th>状态持续小时</th><th>降低出力</th><th>停运次数</th><th>启动成功次数</th><th>启动失败次数</th><th>检修工日</th><th>检修费用（万元）</th><th>事件原因</th><th>事件原因补充说明</th></tr>')
				for (var i = 0; i < con_ds.getTotalCount(); i++) {
					var rec = con_ds.getAt(i);			
					html.push('<tr><td>' + rec.get('blockCode') + '</td><td>' 
							+ "'"+rec.get('startDate') + '</td><td>'// modify by ywliu 20091022
							+ "'"+rec.get('endDate') + '</td><td>'
							+ rec.get('jzztName') + '</td><td>'
							+ rec.get('keepTime') + '</td><td>'
							+ rec.get('reduceExert') + '</td><td>'
							+ rec.get('stopTimes') + '</td><td>'
							+ rec.get('successTimes') + '</td><td>'
							+ rec.get('failureTimes') + '</td><td>'
							+ rec.get('repairMandays') + '</td><td>'
							+ rec.get('repairCost') + '</td><td>'
							//+ rec.get('eventCode') + '</td><td>'
							+ rec.get('stopReason') + '</td><td>'
							//+ rec.get('eventOtherReason')
							+ '</td></tr>')
				}
						
				html.push("<tr><td colspan = 1>单位:</td><td></td><td></td><td>主管:</td><td></td><td></td><td>填表:</td><td></td><td></td><td>填表日期:</td><td></td></tr>")
				html.push('</table>');
				html = html.join(''); // 最后生成的HTML表格
				tableToExcel(html);
			}
		})
	}

	var Grid = new Ext.grid.EditorGridPanel({
		sm : con_sm,
		ds : con_ds,
		cm : con_item_cm,
		title : '机组事件数据录入',
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
	})
	

	/** 事件状态选择 */
	function blockStatusSelect() {
		var url = "blockStatusSelect.jsp";
		var equ = window.showModalDialog(url, window,
				'dialogWidth=300px;dialogHeight=400px;status=no');
		if (typeof(equ) != "undefined") {
			var record = Grid.selModel.getSelected()
			var names = equ.text.split(",");
			var codes = equ.id.split(",");
			record.set("jzztName", names[0]);
			record.set("jzztId", codes[0]);
			Grid.getView().refresh();
		}
	};
})