Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	function getMonth() {
	var d, s, t;
	d = new Date();
	s = d.getFullYear().toString(10) + "-";
	t = d.getMonth() + 1;
	s += (t > 9 ? "" : "0") + t;
	return s;
}
	Date.prototype.format = function(format) {
		var o = {
			"M+" : this.getMonth() + 1, // month
			"d+" : this.getDate(), // day
			"h+" : this.getHours(), // hour
			"m+" : this.getMinutes(), // minute
			"s+" : this.getSeconds(), // second
			"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
			"S" : this.getMilliseconds()
				// millisecond
		}
		if (/(y+)/.test(format))
			format = format.replace(RegExp.$1, (this.getFullYear() + "")
					.substr(4 - RegExp.$1.length));
		for (var k in o)
			if (new RegExp("(" + k + ")").test(format))
				format = format.replace(RegExp.$1, RegExp.$1.length == 1
						? o[k]
						: ("00" + o[k]).substr(("" + o[k]).length));
		return format;
	};

	var dominationProfessionurl = "bqfailure/querydominationProfessionList.action";
	var dominationProfessionconn = Ext.lib.Ajax.getConnectionObject().conn;
	dominationProfessionconn.open("POST", dominationProfessionurl, false);
	dominationProfessionconn.send(null);
	var dominationProfessionGrid = eval('('
			+ dominationProfessionconn.responseText + ')').list;

	var dominationProfessionList = new Ext.data.Record.create([{
		name : 'specialityCode'
	}, {
		name : 'specialityName'
	}]);

	var dominationProfessionGrids = new Ext.data.JsonStore({
		data : dominationProfessionGrid,
		fields : dominationProfessionList
	});

	var tbardominationProfessionComboBox = new Ext.form.ComboBox({
		id : 'tbardominationProfession-combobox',
		store : dominationProfessionGrids,
		valueField : "specialityCode",
		displayField : "specialityName",
		mode : 'local',
		forceSelection : true,
		hiddenName : 'specialityCode',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		blankText : '管辖专业',
		emptyText : '管辖专业',
		value : '',
		width : 100
	});

	var repairDepturl = "bqfailure/queryrepairDept.action";
	var repairDeptconn = Ext.lib.Ajax.getConnectionObject().conn;
	repairDeptconn.open("POST", repairDepturl, false);
	repairDeptconn.send(null);
	var repairDeptGrid = eval('(' + repairDeptconn.responseText + ')').list;

	var repairDeptList = new Ext.data.Record.create([{
		name : 'deptCode'
	}, {
		name : 'deptName'
	}]);

	var repairDeptGrids = new Ext.data.JsonStore({
		data : repairDeptGrid,
		fields : repairDeptList
	});
	var tbarrepairDepComboBox = new Ext.form.ComboBox({
		id : 'tbarrepairDep-combobox',
		store : repairDeptGrids,
		fieldLabel : '检修部门',
		valueField : "deptCode",
		displayField : "deptName",
		mode : 'local',
		forceSelection : true,
		hiddenName : 'deptCode',
		editable : true,
		triggerAction : 'all',
		selectOnFocus : true,
		blankText : '请选择...',
		emptyText : '请选择...',
		value : '',
		width : 120,
		typeAhead : true
	});
	var nowdate = new Date().format('yyyy-MM-dd');
	var d = new Date();
	d.setMonth(d.getMonth() - 1);
	var firstdate = d.format('yyyy-MM-dd');
	var w = screen.availWidth - 210;
	var currentPanel = "main";
	var overtimeFail = Ext.data.Record.create([{
		name : 'roleId'
	}, {
		name : 'roleName'
	}, {
		name : 'memo'
	}]);
	var searchFail = new Ext.data.Record.create([{
		name : 'id'
	}, {
		name : 'failureCode'
	}, {
		name : 'woStatusName'
	}, {
		name : 'failuretypeName'
	}, {
		name : 'failurePri'
	}, {
		name : 'attributeCode'
	}, {
		name : 'equName'
	}, {
		name : 'failureContent'
	}, {
		name : 'locationDesc'
	}, {
		name : 'findDate'
	}, {
		name : 'findBy'
	}, {
		name : 'findByName'
	}, {
		name : 'findDept'
	}, {
		name : 'findDeptName'
	}, {
		name : 'woCode'
	}, {
		name : 'bugCode'
	}, {
		name : 'bugName'
	}, {
		name : 'failuretypeCode'
	}, {
		name : 'failureLevel'
	}, {
		name : 'woStatus'
	}, {
		name : 'preContent'
	}, {
		name : 'ifStopRun'
	}, {
		name : 'ifStopRunName'
	}, {
		name : 'runProfession'
	}, {
		name : 'dominationProfession'
	}, {
		name : 'dominationProfessionName'
	}, {
		name : 'repairDep'
	}, {
		name : 'repairDepName'
	}, {
		name : 'installationCode'
	}, {
		name : 'installationDesc'
	}, {
		name : 'belongSystem'
	}, {
		name : 'belongSystemName'
	}, {
		name : 'likelyReason'
	}, {
		name : 'woPriority'
	}, {
		name : 'writeBy'
	}, {
		name : 'writeByName'
	}, {
		name : 'writeDept'
	}, {
		name : 'writeDeptName'
	}, {
		name : 'writeDate'
	}, {
		name : 'repairDept'
	}, {
		name : 'realrepairDept'
	}, {
		name : 'ifOpenWorkorder'
	}, {
		name : 'ifRepeat'
	}, {
		name : 'supervisor'
	}, {
		name : 'workFlowNo'
	}, {
		name : 'wfState'
	}, {
		name : 'entrepriseCode'
	}, {
		name : 'isuse'
	}, {
		name : 'isOverTime'
	}, {
		name : 'groupName'
	}, {
		name : 'delayDate'
	}, {
		name : 'isSend'
	}]);
	var ds = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url : 'bqfailure/queryListByStatus.action',
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list'
		}, searchFail),
		groupField : 'groupName',
		sortInfo : '',
		remoteGroup : true
	});
	var toptipMsg = [];
	ds.load({
		params : {
			start : 0,
			limit : 18,
			sdate : firstdate, 
			edate : nowdate, 
			status : "confirm",
			belongBlock : "",
			specialityCode : "",
			deptCode : "",
			month : getMonth()
//			,
//			stop : ''
		}
	});
	ds.on('beforeload', function() {
		var status;
		var list = document.getElementsByName("queryTypeRadio");
		for (var i = 0; i < list.length; i++) {
			if (list[i].checked) {
				status = list[i].id;
			}
		}
		Ext.apply(this.baseParams, {
			// status : document.getElementById("status").value,
			status : status,
			sdate : document.getElementById("stime").value, 
			edate : document.getElementById("etime").value, 
			belongBlock : document.getElementById("belongSystemGrid").value,
			specialityCode : "",
			deptCode : tbarrepairDepComboBox.getValue(),
			month : queryMonth.getValue()
		});
	});
	/* 设置每一行的选择框 */
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			rowselect : function(sm, row, rec) {

			}
		}
	});

	var isSendStore = new Ext.data.SimpleStore({
		data : [['Y','是'],['N','否']],
		fields : ['value','text']
	})
	
	var cm = new Ext.grid.ColumnModel([sm, new Ext.grid.RowNumberer({
		header : '序号',
		width : 35
	}), {
		header : "id",
		dataIndex : "id",
		hidden : true
	}, {
		header : '缺陷内容',
		dataIndex : 'failureContent',
		width : 300,
		renderer : function change(val) {
			return ' <span style="white-space:normal;">' + val + ' </span>';
		}
	}, {
		header : '检修部门',
		dataIndex : 'repairDepName',
		width : 120
	}, {
		header : '管辖专业',
		dataIndex : 'dominationProfessionName',
		width : 100
	}, {
		header : '状态',
		dataIndex : 'woStatusName',
		width : 120
	}, {
		header : '填写时间',
		dataIndex : 'writeDate',
		width : 120
	},  {
		header : '批准延期时间',
		dataIndex : 'delayDate',
		width : 120
	},{
		header : '发现人',
		dataIndex : 'findByName',
		width : 60
	}, {
		header : '发现部门',
		dataIndex : 'findDeptName',
		width : 120
	}, {
		header : '缺陷编码',
		dataIndex : 'failureCode',
		width : 100
	}, {
		header : '所属系统',
		dataIndex : 'belongSystemName',
		width : 100
	}, {
		header : '发现时间',
		dataIndex : 'findDate',
		width : 120
	}, {
		header : '类别',
		dataIndex : 'failuretypeName',
		width : 60
	}, {
		header : '优先级',
		dataIndex : 'failurePri',
		width : 140
	},
			// {
			// header : '设备功能码',
			// dataIndex : 'attributeCode',
			// width : 120
			// }, {
			// header : '设备名称',
			// dataIndex : 'equName',
			// width : 120
			// },
			{
				dataIndex : 'workFlowNo',
				hidden : true
			}, {
				header : '类别',
				dataIndex : 'groupName',
				align : 'left',
				hidden : true,
				width : 120
			}, {
				header : '是否上传',
				dataIndex : 'isSend',
				editor : new Ext.form.ComboBox({
							id : 'isSendCb',
							store : isSendStore,
							readOnly : true,
							displayField : 'text',
							valueField : 'value',
							mode : 'local',
							triggerAction : 'all'
						}),
				renderer : function(v)
				{
					if(v == 'Y')
						return '是';
					else if(v == 'N')
						return '否';
					else 
						return '';
				}
			}
			// {
			// header : '填写人',
			// dataIndex : 'writeByName',
			// width : 60
			// }, {
			// header : '填写人部门',
			// dataIndex : 'writeDeptName',
			// width : 100
			// },
	]);
	// cm.defaultSortable = true;
	/* 设置分页的工具条 */
	var bbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : ds,
		displayInfo : true,
		displayMsg : '显示第 {0} 条到 {1} 条记录，一共 {2} 条',
		emptyMsg : "没有记录"
	})

	var statuslist = [["全部", "%"], ["未上报", "0"], ["待消缺", "1"], ["待确认", "2"],
			["已确认待消缺", "18"], ["点检待验收", "3"], ["运行待验收", "14"], ["运行已验收", "4"],
			["验收退回", "9"], ["设备部仲裁", "7"], ["已仲裁待消缺", "8"], ["点检延期待处理审批", "11"],
			["设备部主任延期待处理审批", "12"], ["发电部延期待处理审批", "13"], ["总工延期待处理审批", "20"],
			["已处理", "5"], ["延期待处理退回", "15"], ["作废", "6"]];
	var stime = {
		xtype : 'datefield',
		format : 'Y-m-d',
		id : 'stime',
		name : 'stime',
		hidden : true,
		value : firstdate,
		readOnly : true
	};
	var etime = {
		xtype : 'datefield',
		format : 'Y-m-d',
		id : 'etime',
		name : 'etime',
		value : nowdate,
		hidden : true,
		readOnly : true
	};
	
	var queryMonth = new Ext.form.TextField({
		id : 'queryMonth',
		fieldLabel : "月份",
		name : 'queryMonth',
		style : 'cursor:pointer',
		width : 85,
		value : getMonth(),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M',
					dateFmt : 'yyyy-MM',
					alwaysUseStartDate : false,
					isShowClear : false
				});
				this.blur()
			}
		}
	});

	var url = "bqfailure/blocklist.action";
	var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", url, false);
	conn.send(null);
	var blockStoreGrid = eval('(' + conn.responseText + ')').list;

	var blockComboBoxList = new Ext.data.Record.create([{
		name : 'blockCode'
	}, {
		name : 'blockName'
	}]);

	var blockComboBoxGrids = new Ext.data.JsonStore({
		data : blockStoreGrid,
		fields : blockComboBoxList
	});

	var blockComboBoxGrid = new Ext.form.ComboBox({
		id : 'block-comboboxgrid',
		fieldLabel : '所属系统',
		triggerAction : 'all',
		store : blockComboBoxGrids,
		valueField : "blockCode",
		displayField : "blockName",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'belongSystemGrid',
		editable : false,
		value : "",
		triggerAction : 'all',
		blankText : '请选择...',
		emptyText : '请选择...',
		selectOnFocus : true,
		width : 120
	});

	var status = new Ext.form.ComboBox({
		id : "statuslist",
		xtype : "combo",
		name : 'statuslist',
		allowBlank : true,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
			fields : ['name', 'id'],
			data : statuslist
		}),
		hiddenName : 'status',
		displayField : 'name',
		valueField : 'id',
		value : "%",
		mode : 'local',
		emptyText : '请选择...',
		blankText : '请选择',
		readOnly : true
	});
	var tbar = new Ext.Toolbar({
		items : [ stime, etime, 
		'月份：',queryMonth,
				// '管辖专业',
				// tbardominationProfessionComboBox, '-',
				'检修部门', tbarrepairDepComboBox,
				// '-', '状态', status,
				'-', '所属系统', blockComboBoxGrid]
	})
	function queryWF() {
		var rec = grid.getSelectionModel().getSelected();
		if (rec) {
			if (rec.get("workFlowNo") != 0) {
				if (rec.get("workFlowNo") != "") {
					window
							.open("../../../workflow/manager/show/show.jsp?entryId="
									+ rec.get("workFlowNo"));
				}
			} else {
				var flowCode = "bqFailure";
				url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
						+ flowCode;
				window.open(url);
			}
		} else {
			Ext.Msg.alert('提示', '请从列表中选择一条缺陷！');
		}
	};
	function queryIt() {
		var status;
		var list = document.getElementsByName("queryTypeRadio");
		for (var i = 0; i < list.length; i++) {
			if (list[i].checked) {
				status = list[i].id;
			}
		}
		ds.load({
			params : {
				start : 0,
				limit : 18,
				status : status,
				sdate : document.getElementById("stime").value,
				edate : document.getElementById("etime").value,
				belongBlock : document.getElementById("belongSystemGrid").value,
				specialityCode : "",
				deptCode : tbarrepairDepComboBox.getValue(),
				month : queryMonth.getValue()
			}
		});
	}

	/* 创建表格 */
	var grid = new Ext.grid.EditorGridPanel({
		id : 'gridid',
		ds : ds,
		cm : cm,
		autoScroll : true,
		// autoWidth : true,
		width : 200,
		sm : sm,
		bbar : bbar,
		tbar : tbar,
		view : new Ext.grid.GroupingView({
			forceFit : false,
			enableGroupingMenu : false,
			hideGroupedColumn : true,
			showGroupName : false,
			startCollapsed : false,
			groupTextTpl : '{text}'
		}),
		border : false,
		clicksToEdit : 1
	});

	grid.getView().getRowClass = function(record, index) {
		return (record.data.isOverTime == 'Y' ? 'red-row' : 'black-row');
	};

	grid.on('rowdblclick', function(grid, rowIndex, e) {
		var rec = grid.getSelectionModel().getSelected();
		if (rec) {
			Ext.Ajax.request({

				url : 'bqfailure/findFailureById.action?failure.id='
						+ rec.get("id"),
				method : 'post',
				waitMsg : '正在处理数据...',
				success : function(result, request) {
					var json = eval('(' + result.responseText + ')');
					window
							.showModalDialog(
									json.msg,
									json,
									'dialogWidth=860px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('打开失败！');
				}
			});
		}
	});
	var viewport = new Ext.Viewport({
		layout : 'fit',
		border : false,
		items : [grid]
	});
	// ------add 090421
	var btnDelete = new Ext.Button({
		id : "btnDelete",
		text : "删除",
		hidden : true,
		iconCls : 'delete',
		handler : function() {
			var record = grid.getSelectionModel().getSelected();
			if (record) {
				if (confirm("删除后数据不能恢复,确定要删除该条缺陷吗?")) {
					var busiNo = record.get("failureCode");
					var entryId = record.get("workFlowNo");
					Ext.Ajax.request({
						url : "MAINTWorkflow.do?action=mangerDelete",
						method : 'post',
						params : {
							busiType : 'failure',
							entryId : entryId,
							busiNo : busiNo
						},
						success : function(result, request) {
							Ext.Msg.alert("提示", "操作成功!");
							grid.getStore().remove(record);
							btnDelete.setVisible(false);
						},
						failure : function() {
							Ext.Msg.alert("提示", "操作失败!");
						}
					});
				}
			} else {
				Ext.Msg.alert("提示", "请选择一条删除的记录！");
			}
		}
	});
	var allRadio = new Ext.form.Radio({
		id : '%',
		boxLabel : '全部',
		hideLabel : true,
		name : 'queryTypeRadio',
		listeners : {
			check : function() {
				queryIt();
			}
		}
	});
	var reportRadio = new Ext.form.Radio({
		id : 'report',
		boxLabel : '未上报',
		hideLabel : true,
		name : 'queryTypeRadio'
	});
	var confirmRadio = new Ext.form.Radio({
		id : 'confirm',
		boxLabel : '待确认',
		hideLabel : true,
		name : 'queryTypeRadio',
		checked : true
	});
	var elRadio = new Ext.form.Radio({
		id : 'eliminate',
		boxLabel : '待消缺',
		hideLabel : true,
		name : 'queryTypeRadio'
	})
	var arbitrateRadio = new Ext.form.Radio({
		id : 'arbitrate',
		boxLabel : '仲裁中',
		hideLabel : true,
		name : 'queryTypeRadio'
	})
	var awaitRadio = new Ext.form.Radio({
		id : 'await',
		boxLabel : '待延期',
		hideLabel : true,
		name : 'queryTypeRadio'
	})
	var awaitEndRadio = new Ext.form.Radio({
		id : 'awaitEnd',
		boxLabel : '已延期',
		hideLabel : true,
		name : 'queryTypeRadio'
	})
	var checkRadio = new Ext.form.Radio({
		id : 'check',
		boxLabel : '待验收',
		hideLabel : true,
		name : 'queryTypeRadio'
	})
	var endRadio = new Ext.form.Radio({
		id : 'end',
		boxLabel : '已结束',
		hideLabel : true,
		name : 'queryTypeRadio'
	})
	var invalidRadio = new Ext.form.Radio({
		id : 'invalid',
		boxLabel : '已作废',
		hideLabel : true,
		name : 'queryTypeRadio'
	})
	//add by fyyang 091214
	var stopRadio = new Ext.form.Radio({
		id : 'stop',
		boxLabel : '停机消除',
		hideLabel : true,
		name : 'queryTypeRadio'
	})
	var tbar2 = new Ext.Toolbar({
		id : 'secondTbar',
		renderTo : grid.tbar,
		items : ['状态：', allRadio, confirmRadio, elRadio,
				arbitrateRadio, awaitRadio,awaitEndRadio, checkRadio, endRadio, invalidRadio,stopRadio]
	});
	var tbar3 = new Ext.Toolbar({
		id : 'thirdTbar',
		renderTo : grid.tbar,
		items : [
				{
					id : 'query',
					text : "查询",
					iconCls : "query",
					handler : queryIt
				},
				'-',
				{
					id : 'query',
					text : "查看流程",
					iconCls : 'view',
					handler : queryWF
				},
				'-',
				{
					id : 'query',
					iconCls : 'list',
					text : "查看缺陷信息<font color='red'><双击显示详细信息></font>",
					handler : function() {
						var rec = grid.getSelectionModel().getSelected();
						if (rec) {
							Ext.Ajax.request({

								url : 'bqfailure/findFailureById.action?failure.id='
										+ rec.get("id"),
								method : 'post',
								waitMsg : '正在处理数据...',
								success : function(result, request) {
									var json = eval('(' + result.responseText
											+ ')');
									window
											.showModalDialog(
													json.msg,
													json,
													'dialogWidth=860px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');
								},
								failure : function(result, request) {
									alert('打开失败！');
								}
							});
						} else {
							alert('请选择一条记录!');
						}
					}
				},'-',
				{
					id : 'saveBtu',
					text : "保存",
					iconCls : 'save',
					handler : function() {
						if (sm.hasSelection()) {
							Ext.Msg.confirm('提示', '确认要保存吗？', function(id) {
								if (id == 'yes') {
									var ids = new Array();
									var isSends = new Array();
									var modi = ds.getModifiedRecords();
									for (var i = 0; i < modi.length; i++) {
										ids.push(modi[i].get('id'));
										isSends.push(modi[i].get('isSend'));
									}
									Ext.Ajax.request({
										url : 'bqfailure/saveFailContrSend.action',
										method : 'post',
										params : {
											ids : ids.join(','),
											isSends : isSends.join(',')
										},
										success : function(request, optinos) {
											Ext.Msg.alert('提示', '数据保存成功！');
											ds.reload()
										},
										failure : function(request, option) {
											Ext.Msg.alert('提示', '数据保存失败！')
										}
									})
								}
							})

						} else {
							Ext.Msg.alert('提示', '没有做任何修改！')
						}
					}
				}, '->', btnDelete,
				'<div id="divManagerDel" style="cursor:hand" title="双击显示汇总信息">缺陷列表</div>']
	});
	grid.setHeight(document.getElementById("gridid").offsetHeight
			- document.getElementById("secondTbar").offsetHeight
			- document.getElementById("thirdTbar").offsetHeight);

	document.getElementById("divManagerDel").onclick = function() {
		if (btnDelete.isVisible()) {
			btnDelete.setVisible(false);
		}
		if ((currentUser == "999999" || currentUser == "1001003" || currentUser == "0800005" ||  currentUser == "3400003")
				&& event.ctrlKey && event.altKey) {
			btnDelete.setVisible(true);
		}
	};
	document.getElementById("divManagerDel").ondblclick = function() {
		var status;
		var list = document.getElementsByName("queryTypeRadio");
		for (var i = 0; i < list.length; i++) {
			if (list[i].checked) {
				status = list[i].id;
			}
		}
		Ext.Ajax.request({
			url : 'bqfailure/getToptipMsg.action',
			method : 'post',
			params : {
				status : status,
				sdate : document.getElementById("stime").value,
				edate : document.getElementById("etime").value,
				belongBlock : document.getElementById("belongSystemGrid").value,
				specialityCode : "",
				deptCode : tbarrepairDepComboBox.getValue()
			},
			success : function(form, action) {

				var toptipstore = new Ext.data.SimpleStore({
					fields : [{
						name : 'count',
						type : 'float'
					}, {
						name : 'status'
					}]
				});
				toptipstore.loadData(eval('(' + form.responseText + ')'));

				var toptipgrid = new Ext.grid.GridPanel({
					store : toptipstore,
					autoScroll : true,
					columns : [{
						header : "缺陷状态",
						width : 180,
						sortable : true,
						dataIndex : 'status'
					}, {
						header : "合计",
						width : 70,
						sortable : true,
						dataIndex : 'count'
					}],
					stripeRows : true
				});
				var win = new Ext.Window({
					layout : 'fit',
					title : '缺陷信息汇总',
					width : 270,
					height : 250,
					modal : true,
					items : [toptipgrid],
					closeAction : 'hide'
				});
				win.show();
			}
		});
	}
});