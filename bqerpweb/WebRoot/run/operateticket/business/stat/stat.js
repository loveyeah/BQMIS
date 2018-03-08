Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
	var shiftId;
	var statId;
	var DataRecord = Ext.data.Record.create([{
		name : 'id',
		type : 'int'
	}, {
		name : 'workcode'
	}, {
		name : 'opticketCount',
		type : 'int'
	}, {
		name : 'opticketItemCount',
		type : 'int'
	}, {
		name : 'noProblemOpticketCount',
		type : 'int'
	}, {
		name : 'noProblemOpticketItemCount',
		type : 'int'
	}, {
		name : 'isclear'
	}, {
		name : 'orderBy',
		type : 'int'
	}, {
		name : 'shiftId'
	}]);
	var sessWorname = document.getElementById("workerName").value;
	var sessWorcode = document.getElementById("workerCode").value;
	var nowdate = new Date();
	var date1 = nowdate.toLocaleDateString();
	var exdate;
	if (date1.substring(6, 7) == '月') {
		exdate = date1.substring(0, 4) + '-0' + date1.substring(5, 6);
	} else {
		exdate = date1.substring(0, 4) + "-" + date1.substring(5, 7);
	}
	var monthDate = new Ext.form.TextField({
		name : '_monthDate',
		value : exdate,
		id : 'monthDate',
		fieldLabel : "月份",
		style : 'cursor:pointer',
		cls : 'Wdate',
		// width : 150,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M',
					dateFmt : 'yyyy-MM',
					alwaysUseStartDate : true
				});
			}
		}
	});
	var btnQuery = new Ext.Button({
		id : 'btnQuery',
		text : '统计',
		iconCls : 'query',
		handler : function() {
			gridDs.removeAll();
			query();

		}
	});
	var clear = new Ext.Button({
		id : 'btnClear',
		text : '清零',
		iconCls : 'reflesh ',
		handler : function() {
			var rec = grid.getSelectionModel().getSelections();
			if (rec.length <= 0) {
				Ext.Msg.alert('提示', '请选择记录');
			} else {
				if (confirm("确定要清零\"" + rec[0].get('workcode'))) {
					Ext.Ajax.request({
						url : 'opticket/clearStat.action',
						params : {
							str : Ext.encode(rec[0].data)
						},
						method : 'post',
						success : function(result, request) {
							Ext.get("btnQuery").dom.click();
						}
					})
				}
			}
		}
	})
	var print = new Ext.Button({
		id : 'print',
		text : '打印',
		iconCls : 'print',
		handler : function() {
			var title = "300MW机组电气专业" + monthDate.getValue().replace("-", "").substring(4)
					+ "月份操作票执行统计情况";
			var yearMonth = monthDate.getValue().replace("-", "");
			var specialCode = 28;
			var statName = sessWorname;
			var url = "/powerrpt/report/webfile/bqmis/opreaterCount.jsp?statBy="
					+ statName
					+ "&title="
					+ title
					+ "&yearMonth="
					+ yearMonth
					+ "&specialCode=" + specialCode;
			url = encodeURI(url);
			window.open(url);
		}
	});
	function query() {
		if (gridDs.getCount() == "0") {
			Ext.Msg.wait("正在查询,请等待...");
			Ext.Ajax.request({
				url : 'opticket/getStatData.action',
				params : {
					date : monthDate.getValue(),
					specialCode : 28,
					shiftId : shiftId
				},
				method : 'post',
				success : function(result, request) {
					var obj = Ext.decode(result.responseText);
					gridDs.loadData(obj.list);
					statFormPanel.getForm().loadRecord(obj.model);
					statId = obj.model.data.id;
					var rate = obj.model.data.qualifiedRate;
					if (rate != null) {
						rate =Math.round(rate*100); 
						qurate.setValue(rate+"%");
					}
				}
			});
		} else {
			gridDs.filter("shiftId", shiftId);
			var opticketCount_all = 0;
			var opticketItemCount_all = 0;
			var noProblemOpticketCount_all = 0;
			var noProblemOpticketItemCount_all = 0;
			for (var i = 0; i < gridDs.getCount(); i++) {
				var rec = gridDs.getAt(i);
				opticketCount_all += rec.get("opticketCount");
				opticketItemCount_all += rec.get("opticketItemCount");
				noProblemOpticketCount_all += rec.get("noProblemOpticketCount");
				noProblemOpticketItemCount_all += rec
						.get("noProblemOpticketItemCount");
			}
			var o = new DataRecord({
				workcode : '<font color="red">合计</font>',
				opticketCount : opticketCount_all,
				opticketItemCount : opticketItemCount_all,
				noProblemOpticketCount : noProblemOpticketCount_all,
				noProblemOpticketItemCount : noProblemOpticketItemCount_all
			});
			gridDs.add(o);
			Ext.Msg.hide();
		}
	}
	function getSelectValue() {
		var ns = document.getElementsByName("shift");
		for (var i = 0; i < ns.length; i++) {
			if (ns[i].checked) {
				return ns[i].value;
			}
		}
	}
	var shift = new Ext.Panel({
		hideLabel : true,
		layout : 'column',
		width : 280,
		style : {
			cursor : 'hand'
		},
		height : 20,
		items : [new Ext.form.Radio({
			columnWidth : 0.2,
			checked : true, // 设置当前为选中状态,仅且一个为选中.
			boxLabel : "一值", // Radio标签
			name : "shift", // 用于form提交时传送的参数名
			inputValue : "229", // 提交时传送的参数值
			listeners : {
				check : function(checkbox, checked) { // 选中时,调用的事件
					shiftId = getSelectValue();
//					query();
				}
			}
		}), new Ext.form.Radio({
			columnWidth : 0.2,
			boxLabel : "二值",
			name : "shift",
			inputValue : "230"
		}), new Ext.form.Radio({
			columnWidth : 0.2,
			boxLabel : "三值",
			name : "shift",
			inputValue : "231"
		}), new Ext.form.Radio({
			columnWidth : 0.2,
			boxLabel : "四值",
			name : "shift",
			inputValue : "232"
		}), new Ext.form.Radio({
			columnWidth : 0.2,
			boxLabel : "五值",
			name : "shift",
			inputValue : "233"
		})]
	});

	var gridTbar = new Ext.Toolbar({
		items : ['班次:', shift, '统计月份:', monthDate, '-', btnQuery, '-', clear,
				'-', print]
	});

	var gridDs = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'opticket/getStatData.action'
		}),
		reader : new Ext.data.JsonReader({}, DataRecord)
	});

	gridDs.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			date : monthDate.getValue(),
			specialCode : 28,
			shiftId : shiftId
		});
	});
	gridDs.on('load', function() {
		gridDs.filterBy(function(record) {
			return record.get('shiftId') == shiftId;
		});
		var opticketCount_all = 0;
		var opticketItemCount_all = 0;
		var noProblemOpticketCount_all = 0;
		var noProblemOpticketItemCount_all = 0;
		for (var i = 0; i < gridDs.getCount(); i++) {
			var rec = gridDs.getAt(i);
			opticketCount_all += rec.get("opticketCount");
			opticketItemCount_all += rec.get("opticketItemCount");
			noProblemOpticketCount_all += rec.get("noProblemOpticketCount");
			noProblemOpticketItemCount_all += rec
					.get("noProblemOpticketItemCount");
		}
		var o = new DataRecord({
			workcode : '<font color="red">合计</font>',
			opticketCount : opticketCount_all,
			opticketItemCount : opticketItemCount_all,
			noProblemOpticketCount : noProblemOpticketCount_all,
			noProblemOpticketItemCount : noProblemOpticketItemCount_all
		});
		gridDs.add(o);
		Ext.Msg.hide();
	});
	// function to2bits(flt) {
	// if (parseFloat(flt) == flt)
	// // return Math.round(flt * 100) / 100;
	// return Math.round(flt * 10000) / 100;
	// else
	// return 0;
	// }
	// bb.setValue("本单位评价:本月工作票共有 " + totlaNum + " 份,合格 "
	// + workticketQualifiedNum + " 份,合格率 "
	// + to2bits(workticketQualifiedNum / totlaNum)
	// + " %, 标准票使用率: "
	// + to2bits(usedStaworkQualifiedNum / workticketQualifiedNum)
	// + "%");
	var rn = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		hidden : true,
		listeners : {
			rowselect : function(sm, row, rec) {
				if (rec.get("id") == ""
						|| rec.get("workcode") == '<font color="red">合计</font>') {
					Ext.get("btnClear").dom.disabled = true;
				} else {
					Ext.get("btnClear").dom.disabled = false;
				}
			}
		}

	});
	var gridCm = new Ext.grid.ColumnModel([rn, {
		header : '姓名',
		dataIndex : 'workcode',
		width : 150,
		align : 'left'
	}, {
		header : '本月次',
		dataIndex : 'opticketCount',
		align : 'left'
	}, {
		header : '本月项',
		dataIndex : 'opticketItemCount',
		align : 'left'
	}, {
		header : '无差错累计(次)',
		dataIndex : 'noProblemOpticketCount',
		align : 'left'
	}, {
		header : '无差错累计(项)',
		dataIndex : 'noProblemOpticketItemCount',
		align : 'left'
	}]);
	var grid = new Ext.grid.GridPanel({
		ds : gridDs,
		tbar : gridTbar,
		cm : gridCm,
		sm : rn,
		autoWidth : true,
		autoScroll : true,
		enableColumnMove : false,
		enableColumnHide : true,
		border : false
	});
	var lable = new Ext.form.Label({
		id : 'stat',
		name : 'stat',
		anchor : "85%",
		text : '"操作票"统计'
	});
	var opcount = new Ext.form.TextField({
		fieldLabel : "操作票（张）",
		id : 'allCount',
		anchor : "85%",
		readOnly : true,
		name : 'allCount',
		lableWidth : 20
	});
	var qucount = new Ext.form.TextField({
		fieldLabel : "合格操作票",
		id : 'qualifiedCount',
		anchor : "85%",
		readOnly : true,
		name : 'qualifiedCount'
	});
	var noqucount = new Ext.form.TextField({
		fieldLabel : "不合格操作票",
		id : 'notQualifiedCount',
		anchor : "85%",
		readOnly : true,
		name : 'notQualifiedCount'
	});
	var qurate = new Ext.form.TextField({
		fieldLabel : "合格率",
		id : 'qualifiedRate',
		anchor : "85%",
		readOnly : true,
		name : 'qualifiedRate'
	});
	var all = new Ext.form.TextField({
		fieldLabel : "总合计",
		id : 'totalCount',
		anchor : "85%",
		readOnly : true,
		name : 'totalCount'
	});
	var memo = new Ext.form.TextArea({
		id : 'memo',
		fieldLabel : "备注",
		name : 'memo',
//		anchor : "85%"
		anchor : "95%"
	});
	var chargBy = new Ext.form.TriggerField({
		id : 'deptCharge',
		name : 'deptCharge',
		readOnly : true,
		anchor : "85%",
		fieldLabel : "部门负责人",
		onTriggerClick : function(e) {
			var args = {
				selectModel : 'single'
			}
			var emp = window
					.showModalDialog(
							"../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp",
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (emp != null) {
				Ext.get('deptCharge').dom.value = emp.workerName
			}
		}
	});
	var statBy = new Ext.form.Field({
		fieldLabel : "制表人",
		id : 'statBy',
		readOnly : true,
		anchor : "85%",
		name : 'statBy'
	})
	var btnSave = new Ext.Button({
		text : '保存',
		iconCls : 'save',
		handler : function() {
			statFormPanel.getForm().submit({
				url : 'opticket/saveReportInfo.action',
				method : 'post',
				params : {
					statId : statId
				},
				success : function(form, action) {
					Ext.Msg.alert('提示信息', '保存成功');
				},
				failure : function(form, action) {
					Ext.Msg.alert('', '');
				}
			})
		}
	})
	// var btnCancel = new Ext.Button({
	// text : '取消',
	// iconCls : 'cance',
	// handler : function() {}
	// })
	var statField = new Ext.form.FieldSet({
		border : true,
		labelAlign : 'left',
		labelWidth : 90,
		layout : 'form',
		autoHeight : true,
		style : {
			"margin-top" : "5px",
			"margin-left" : "5px",
			"margin-right" : Ext.isIE6
					? (Ext.isStrict ? "-10px" : "-13px")
					: "0"
		},
		items : [{
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.25,
				layout : "form",
				border : false,
				items : [opcount]
			}, {
				columnWidth : 0.25,
				layout : "form",
				border : false,
				items : [qucount]
			}, {
				columnWidth : 0.25,
				layout : "form",
				border : false,
				items : [noqucount]
			}, {
				columnWidth : 0.25,
				layout : "form",
				border : false,
				items : [qurate]
			}
			// , {
			// columnWidth : 0.2,
			// layout : "form",
			// border : false,
			// items : [all]
			// }
			]
		}, {
			columnWidth : 0.9,
			layout : "form",
			border : false,
			items : [memo]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [chargBy]
			}, {
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [statBy]
			}]
		}],
		buttons : [btnSave]
	});
	var statFormPanel = new Ext.FormPanel({
		border : false,
		frame : true,
		fileUpload : true,
		items : [statField]
	});

	// 设定布局器及面板
	var layout = new Ext.Viewport({
		layout : 'border',
		border : false,
		items : [{
			layout : 'fit',
			height : 200,
			margins : '0 0 0 0',
			region : 'south',
			items : statFormPanel
		}, {
			layout : 'fit',
			autoScroll : true,
			margins : '0 0 0 0',
			region : 'center',
			items : grid
		}]
	});
	statBy.setValue(sessWorname);
});