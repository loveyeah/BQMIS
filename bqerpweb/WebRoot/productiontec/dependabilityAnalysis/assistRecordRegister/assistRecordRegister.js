Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	// 所属机组或系统
	var storeAssistEqu = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'productionrec/findFjCodeList.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, [{
			name : 'fjCode',
			mapping : 'fjCode'
		}, {
			name : 'fjName',
			mapping : 'fjName'
		}])
	});
	storeAssistEqu.load();
		/** 事件状态选择 */
	function blockStatusSelect() {
		var url = "fjStatusSelect.jsp";
		var jzzt = window.showModalDialog(url, window,
				'dialogWidth=300px;dialogHeight=400px;status=no');
		if (typeof(jzzt) != "undefined") {
			var record = Grid.selModel.getSelected()
			record.set("jzztName", jzzt.name);
			record.set("jzztId", jzzt.code);
			Grid.getView().refresh();
		}
	};
	function numberFormat(value) {
		if (value == null || value == "") {
			return "0.0";
		} else if (value.toString() == "NaN") {
			return "";
		} else if (value.toString() == "Infinity") {
			return "";
		} else {
			value = (Math.round((value - 0) * 100)) / 100;
			value = (value == Math.floor(value))
					? value + ".00"
					: ((value * 10 == Math.floor(value * 10))
							? value + "0"
							: value);
			value = String(value);
			var ps = value.split(".");
			var whole = ps[0];
			var sub = ps[1] ? '.' + ps[1].substring(0, 2) : '.00';
			var v = whole + sub;
			return v;
		}
	}
	function getMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t
		return s;
	}

	var item = Ext.data.Record.create([{
				name : 'fjId'
			}, {
				name : 'strMonth'
			}, {
				name : 'fjCode'
			}, {
				name : 'jzztId'
			}, {
				name : 'keepTime'
			}, {
				name : 'standbyNum'
			}, {
				name : 'repairMandays'
			}, {
				name : 'repairCost'
			}, {
				name : 'eventCode'
			}, {
				name : 'eventReason'
			}, {
				name : 'eventOtherReason'
			}, {
				name : 'entryBy'
			}, {
				name : 'fjName'
			}, {
				name : 'startDateString'
			}, {
				name : 'endDateString'
			},{
				name : 'jzztName'
			},{
				name : 'entryName'
			}]);

	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false

			});
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'productionrec/findAssistRecList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalCount : "totalCount",
							root : "list"
						}, item)
			});

	var cm = new Ext.grid.ColumnModel({
				columns : [sm, new Ext.grid.RowNumberer({
									header : "行号",
									width : 31
								}),  {
		header : '辅机',
		dataIndex : 'fjCode',
		align : 'center',
		width : 200,
		editor : new Ext.form.ComboBox({
			store : storeAssistEqu,
			displayField : "fjName",
			valueField : "fjCode",
			hiddenName : 'fjCode',
			mode : 'local',
			triggerAction : 'all',
			readOnly : true
		}),
		renderer : function(v) {
			var recIndex = storeAssistEqu.find("fjCode", v);
			if(recIndex != -1)
			return storeAssistEqu.getAt(recIndex).get("fjName");
		}
	}, {
		header : '事件开始时间',
		dataIndex : 'startDateString',
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
			return v;
		}

	}, {
		header : '事件结束时间',
		dataIndex : 'endDateString',
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
			return v;
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
			readOnly : true,
			listeners : {
				render : function(f) {
					f.el.on('keyup', function(e) {
						Grid.getSelectionModel().getSelected().set("jzztId",null);
					});
				}
			}
		})
	}, {
		header : '状态持续小时',
		dataIndex : 'keepTime',
		width : 100,
		align : 'center',
		editor : new Ext.form.NumberField({
			allowNegative : false
		})
	},{
		header : '备用次数',
		dataIndex : 'standbyNum',
		align : 'center',
		editor : new Ext.form.NumberField({
			allowNegative : false,
			allowDecimals : false
		})
	},{
		header : '检修工日',
		dataIndex : 'repairMandays',
		align : 'center',
		editor : new Ext.form.NumberField({
			allowNegative : false,
			allowDecimals : true,
			decimalPrecision : 1
		})
	},{
		header : '检修费用(万元)',
		dataIndex : 'repairCost',
		align : 'center',
		editor : new Ext.form.NumberField()
	},{
		header : "事件编码",
		width : 100,
		dataIndex : 'eventCode',
		editor : new Ext.form.TextField(),
		renderer : function(value) {
			if (value == null)
				value = "";
			return "<span style='color:gray;'>" + value + "</span>";

		}
	} ,{
		header : "事件原因",
		width : 100,
		dataIndex : 'eventReason',
		editor : new Ext.form.TextField()
	},{
		header : "事件原因补充说明",
		width : 100,
		dataIndex : 'eventOtherReason',
		editor : new Ext.form.TextField()
	}]
			});

	function checkTime1() {
		var date = this.value;
		Grid.getSelectionModel().getSelected().set("startDateString", date);
	}
	function checkTime2() {
		var date = this.value;
		Grid.getSelectionModel().getSelected().set("endDateString", date);
	}
	var gridbbar = new Ext.PagingToolbar({
				pageSize : 18,
				store : ds,
				displayInfo : true,
				displayMsg : "显示第{0}条到{1}条，共{2}条",
				emptyMsg : "没有记录"
			});
	// 增加
	function addRec() {
		var count = ds.getCount();
		var currentIndex = count;
		var ob = new item({
					'fjId' : null,
					'strMonth' : month.getValue(),
					'fjCode' : null,
					'jzztId' : null,
					'keepTime' : null,
					'standbyNum' : null,
					'repairMandays' : null,
					'repairCost' : null,
					'eventCode' : null,
					'eventReason' : null,
					'eventOtherReason' : null,
					'entryBy' : null,
					'fjName' : null,
					'startDateString' : null,
					'endDateString' : null,
					'jzztName' : null,
					'entryName' : null,
					'entryDateString' : null
				});
		Grid.stopEditing();
		ds.insert(currentIndex, ob);
		sm.selectRow(currentIndex);
		Grid.startEditing(currentIndex, 2);

	}

	// 删除记录
	var ids = new Array();
	function deleteRec() {
		Grid.stopEditing();
		var sm = Grid.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("fjId") != null) {
					ids.push(member.get('fjId'));
				}
				Grid.getStore().remove(member);
				Grid.getStore().getModifiedRecords().remove(member);
			}
		}
	}
	// 保存
	function saveRec() {
		Grid.stopEditing();
		var modifyRec = Grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			for (var i = 0; i < modifyRec.length; i++) {
				if (modifyRec[i].data.fjCode == null
						|| modifyRec[i].data.fjCode == "") {
					Ext.Msg.alert('提示', '辅机不可为空，请选择！')
					return;
				}
				if (modifyRec[i].data.jzztId == null
						|| modifyRec[i].data.jzztId == "") {
					Ext.Msg.alert('提示', '事件状态不可为空，请选择！')
					return;
				}
			}
			Ext.Msg.confirm('提示信息', '确定要保存修改吗？', function(button) {
						if (button == 'yes') {
							var addData = new Array();
							var updateData = new Array();
							for (var i = 0; i < modifyRec.length; i++) {
								if (modifyRec[i].get('fjId') != null) {
									updateData.push(modifyRec[i].data);
								} else {
									addData.push(modifyRec[i].data);
								}
							}
							Ext.Ajax.request({
										url : 'productionrec/modifyAssistRec.action',
										method : 'post',
										params : {
											add : Ext.util.JSON.encode(addData),
											update : Ext.util.JSON
													.encode(updateData),
											ids : ids.join(",")
										},
										success : function(result, request) {
											var o = eval('('
													+ result.responseText + ')');
											Ext.Msg.alert("提示信息", o.msg);
											if (o.msg.indexOf("输入") == -1) {
												ds.rejectChanges();
												ids = [];
												ds.reload();
											}
										},
										failure : function(result, request) {
											Ext.Msg.alert("提示信息", "数据保存修改失败！");
											ds.rejectChanges();
											ids = [];
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
	function cancelRec() {
		var modifyRec = ds.getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			Ext.Msg.confirm('提示信息', '确定要放弃修改吗？', function(button) {
						if (button == 'yes') {
							ds.reload();
							ds.rejectChanges();
							ids = [];
						}
					})
		} else
			ds.reload()
	}

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
							handler : addRec
						}, {
							id : 'btnDelete',
							iconCls : 'delete',
							text : "删除",
							handler : deleteRec

						}, {
							id : 'btnCancer',
							iconCls : 'cancer',
							text : "取消",
							handler : cancelRec

						}, '-', {
							id : 'btnSave',
							iconCls : 'save',
							text : "保存修改",
							handler : saveRec
						}, '-', {
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
				var tableHeader = "<table border=1><tr><th colspan = 11>辅助设备月度事件数据报表</th></tr>";
				var html = [tableHeader];
				var time = month.getValue().substring(0, 4) + "年"
						+ month.getValue().substring(5, 7) + "月";
				html
						.push("<tr><td colspan = 9></td>"
								+ "<td colspan = 2>月份："
								+ time + "</td></tr>")
				html
						.push('<tr><th>辅机</th><th>事件起始时间</th><th>事件终止时间</th><th>事件状态</th><th>状态持续小时</th><th>备用次数</th><th>检修工日</th><th>事件编码</th><th>事件原因</th><th>事件原因补充说明</th><th>检修费用(万元)</th></tr>')
				for (var i = 0; i < ds.getTotalCount(); i++) {
					var rec = ds.getAt(i);			
					html.push('<tr><td>' + rec.get('fjName') + '</td><td>' 
							+ "'"+rec.get('startDateString') + '</td><td>'// modify by ywliu 20091022
							+ "'"+rec.get('endDateString') + '</td><td>'
							+ rec.get('jzztName') + '</td><td>'
							+ rec.get('keepTime') + '</td><td>'
							+ rec.get('standbyNum') + '</td><td>'
							+ rec.get('repairMandays') + '</td><td>'
							+ rec.get('repairCost') + '</td><td>'
							+ rec.get('eventCode') + '</td><td>'
							+ rec.get('eventReason') + '</td><td>'
							+ rec.get('eventOtherReason')
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
				sm : sm,
				ds : ds,
				cm : cm,
				autoScroll : true,
				bbar : gridbbar,
				tbar : contbar,
				border : true,
				clicksToEdit : 1,
				enableColumnMove : false,
				viewConfig : {
				}
			});


	var view = new Ext.Viewport({
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
	query();
})