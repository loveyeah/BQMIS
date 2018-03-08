Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	// 所属机组或系统
//	var storeAssistEqu = new Ext.data.Store({
//		proxy : new Ext.data.HttpProxy({
//			url : 'productionrec/findFjCodeList.action'
//		}),
//		reader : new Ext.data.JsonReader({
//			root : 'list'
//		}, [{
//			name : 'fjCode',
//			mapping : 'fjCode'
//		}, {
//			name : 'fjName',
//			mapping : 'fjName'
//		}])
//	});
//	storeAssistEqu.load();
		/** 事件状态选择 */
//	function blockStatusSelect() {
//		var url = "fjStatusSelect.jsp";
//		var jzzt = window.showModalDialog(url, window,
//				'dialogWidth=300px;dialogHeight=400px;status=no');
//		if (typeof(jzzt) != "undefined") {
//			var record = Grid.selModel.getSelected()
//			record.set("jzztName", jzzt.name);
//			record.set("jzztId", jzzt.code);
//			Grid.getView().refresh();
//		}
//	};
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
				name : 'chekupId'
			}, {
				name : 'strMonth'
			}, {
				name : 'protectEqu'
			}, {
				name : 'protectDevice'
			}, {
				name : 'finishThing'
			}, {
				name : 'notFinishReason'
			}, {
				name : 'hasProblem'
			}, {
				name : 'approveBy'
			}, {
				name : 'checkBy'
			}, {
				name : 'entryBy'
			}, {
				name : 'protectEquName'
			}, {
				name : 'protectDeviceName'
			}, {
				name : 'lastCheckDateString'
			}, {
				name : 'planFinishDateString'
			}, {
				name : 'factFinishDateString'
			},{
				name : 'entryDateString'
			},{
				name : 'approveName'
			},{
				name : 'checkName'
			},{
				name : 'entryName'
			}]);

	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false

			});
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'productionrec/getProtectCheckupRecAll.action'
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
		header : '被保护设备',
		dataIndex : 'protectEqu',
		align : 'center',
		width : 100,
		editor : new Ext.form.TextField()
	},{
		header : '被保护设备名称',
		dataIndex : 'protectEquName',
		hidden : true,
		align : 'center',
		width : 100,
		editor : new Ext.form.TextField()
	},{
		header : '保护装置型号',
		dataIndex : 'protectDevice',
		align : 'center',
		width : 100,
		editor : new Ext.form.TextField()
	},{
		header : '保护装置型号名称',
		dataIndex : 'protectDeviceName',
		hidden : true,
		align : 'center',
		width : 100,
		editor : new Ext.form.TextField()
	}, {
		header : '上次定检完成时间',
		dataIndex : 'lastCheckDateString',
		align : 'center',
		width : 110,
		editor : new Ext.form.TextField({
			style : 'cursor:pointer',
			readOnly : true,
			listeners : {
				focus : function() {
					WdatePicker({
						// 时间格式
						startDate : '%y-%M-%d',
						dateFmt : 'yyyy-MM-dd',
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
		header : '本次计划完成时间',
		dataIndex : 'planFinishDateString',
		align : 'center',
		width : 110,
		editor : new Ext.form.TextField({
			style : 'cursor:pointer',
			readOnly : true,
			listeners : {
				focus : function() {
					WdatePicker({
						// 时间格式
						startDate : '%y-%M-%d',
						dateFmt : 'yyyy-MM-dd',
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
		header : '实际定检完成时间',
		dataIndex : 'factFinishDateString',
		align : 'center',
		width : 110,
		editor : new Ext.form.TextField({
			style : 'cursor:pointer',
			readOnly : true,
			listeners : {
				focus : function() {
					WdatePicker({
						// 时间格式
						startDate : '%y-%M-%d',
						dateFmt : 'yyyy-MM-dd',
						alwaysUseStartDate : false,
						onpicked : checkTime3
					});
				}
			}
		}),
		renderer : function(v) {
			return v;
		}
	}, {
		header : '定检完成情况（含发现问题及处理结果）',
		dataIndex : 'finishThing',
		align : 'center',
		width : 120,
		editor : new Ext.form.TextField()
	}, {
		header : '定检未完成原因',
		dataIndex : 'notFinishReason',
		width : 100,
		align : 'center',
		editor : new Ext.form.TextField({
		})
	},{
		header : '有无遗留问题',
		dataIndex : 'hasProblem',
		align : 'center',
		editor : new Ext.form.TextField({
		})
	}]
			});

	function checkTime1() {
		var date = this.value;
		Grid.getSelectionModel().getSelected().set("lastCheckDateString", date);
	}
	function checkTime2() {
		var date = this.value;
		Grid.getSelectionModel().getSelected().set("planFinishDateString", date);
	}
	function checkTime3() {
		var date = this.value;
		Grid.getSelectionModel().getSelected().set("factFinishDateString", date);
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
					'chekupId' : null,
					'strMonth' : month.getValue(),
					'protectEqu' : null,
					'protectDevice' : null,
					'finishThing' : null,
					'notFinishReason' : null,
					'hasProblem' : null,
					'approveBy' : null,
					'checkBy' : null,
					'entryBy' : null,
					'protectEquName' : null,
					'protectDeviceName' : null,
					'lastCheckDateString' : null,
					'planFinishDateString' : null,
					'factFinishDateString' : null,
					'entryDateString' : null,
					'approveName' : null,
					'checkName' : null,
					'entryName' : null
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
				if (member.get("chekupId") != null) {
					ids.push(member.get('chekupId'));
				}
				Grid.getStore().remove(member);
				Grid.getStore().getModifiedRecords().remove(member);
			}
		}
	}
	// 保存
	function saveRec() {
		Grid.stopEditing();
		if (ds.getCount() > 0 || ids.length > 0) {
			for (var i = 0; i < ds.getCount(); i++) {
				var mod = ds.getAt(i)
				if (mod.data.protectEqu == null || mod.data.protectEqu == "") {
					Ext.Msg.alert('提示', '被保护设备不可为空，请选择！')
					return;
				}
				if (mod.data.protectDevice == null
						|| mod.data.protectDevice == "") {
					Ext.Msg.alert('提示', '保护装置型号不可为空，请选择！')
					return;
				}
			}
		}

		Ext.Msg.confirm('提示信息', '确定要保存修改吗？', function(button) {
			if (button == 'yes') {
				var addData = new Array();
				var updateData = new Array();
				for (var i = 0; i < ds.getCount(); i++) {
					var data = ds.getAt(i)
					if (data.get('chekupId') != null) {
						updateData.push(data.data);
					} else {
						addData.push(data.data);
					}
				}
				Ext.Ajax.request({
							url : 'productionrec/saveProtectCheckupRecMod.action',
							method : 'post',
							params : {
								add : Ext.util.JSON.encode(addData),
								update : Ext.util.JSON.encode(updateData),
								ids : ids.join(","),
								month : month.getValue(),
								approveBy : approveBy.getValue(),
								checkBy : checkBy.getValue()
							},
							success : function(result, request) {
								var o = eval('(' + result.responseText + ')');
								Ext.Msg.alert("提示信息", o.msg);
								ds.rejectChanges();
								ids = [];
								ds.reload();
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
		ds.reload();
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
	ds.on('load', function() {
				if (ds.getTotalCount() > 0) {
					approveBy.setValue(ds.getAt(0).get('approveBy'))
					approveName.setValue(ds.getAt(0).get('approveName'))
					checkBy.setValue(ds.getAt(0).get('checkBy'))
					checkName.setValue(ds.getAt(0).get('checkName'))
				}
			})
	
	// 单位
	var dept = new Ext.form.TextField({
				id : 'dept',
				name : 'dept',
				value : '大唐灞桥热电厂',
				disabled : true,
				width : 100
			})
	// 上报时间
	var month = new Ext.form.TextField({
				fieldLabel : "月份<font color='red'>*</font>",
				id : 'month',
				readOnly : true,
				width : 70,
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
	var approveName = new Ext.form.TriggerField({
					id : 'approveName',
					onTriggerClick : selectWorkerWin,
					readOnly : true,
					width : 80
			})
		var  approveBy = new Ext.form.Hidden({
			id : 'engineer'
		})
		function selectWorkerWin() {
		var args = {
			selectModel : 'signal',
			notIn : "",
			rootNode : {
				id : '-1',
				text : '合肥电厂'
			}
		}
		var person = window
				.showModalDialog(
						'../../../comm/jsp/hr/workerByDept/workerByDept2.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(person) != "undefined") {
			approveName.setValue(person.workerName);
			approveBy.setValue(person.workerCode);

		}
	}
	var checkName = new Ext.form.TriggerField({
					id : 'checkName',
					onTriggerClick : selectWin,
					readOnly : true,
					width : 80
			})
		var  checkBy = new Ext.form.Hidden({
			id : 'checkBy'
		})
		function selectWin() {
		var args = {
			selectModel : 'signal',
			notIn : "",
			rootNode : {
				id : '-1',
				text : '合肥电厂'
			}
		}
		var person = window
				.showModalDialog(
						'../../../comm/jsp/hr/workerByDept/workerByDept2.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(person) != "undefined") {
			checkName.setValue(person.workerName);
			checkBy.setValue(person.workerCode);

		}
	}
	// tbar
	var contbar = new Ext.Toolbar({
				items : ['单位:', dept, '-', '月份:', month, '-', 
					'批准人:',approveName,approveBy,'-','审核人:',checkName,checkBy,'-',{
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
							text : "保存",
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
		if(ds.getTotalCount() == 0)
		{
			Ext.Msg.alert('提示','无数据导出!');
			return;
		}
		Ext.Msg.confirm('提示', '确认要导出数据？', function(id) {
			if (id == 'yes') {
				var tableHeader = "<table border=1><tr><th colspan = 8>继电保护装置定检完成情况报表</th></tr>";			
				var html = [tableHeader];
				html.push("<tr><th colspan = 8>（每月5日前上报）</th></tr>")
				var time = month.getValue().substring(0, 4) + "年"
						+ month.getValue().substring(5, 7) + "月";
				html
						.push("<tr><th colspan = 3>报表单位："+ dept.getValue()+"</th>"
								+ "<th colspan = 3></th><th colspan = 2>月份："
								+ time + "</th></tr>")
				html
						.push('<tr><th>被保护设备</th><th>保护装置型号</th><th>上次定检完成时间</th><th>本次计划完成时间</th><th>实际定检完成时间</th><th>定检完成情况（含发现问题及处理结果）</th><th>定检未完成原因</th><th>有无遗留问题</th></tr>')
				for (var i = 0; i < ds.getTotalCount(); i++) {
					var rec = ds.getAt(i);			
					html.push('<tr><td>' + rec.get('protectEqu') + '</td><td>' 
							+ rec.get('protectDevice') + '</td><td>'
							+ rec.get('lastCheckDateString') + '</td><td>'
							+ rec.get('planFinishDateString') + '</td><td>'
							+ rec.get('factFinishDateString') + '</td><td>'
							+ rec.get('finishThing') + '</td><td>'
							+ rec.get('notFinishReason') + '</td><td>'
							+ rec.get('hasProblem') + '</td>'
							+ '</tr>')
				}
				html.push("<tr><td colspan = 2></td><td colspan = 2>批准:"+ds.getAt(0).get('approveName')+"</td>" +
						"<td colspan = 2>审核："+ds.getAt(0).get('checkName')+"</td>" +
								"<td colspan = 2>填表:"+ds.getAt(0).get('entryName')+"</td></tr>")
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