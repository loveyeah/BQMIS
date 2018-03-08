Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
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
};
Ext.onReady(function() {
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

	var nowdate = new Date().format('yyyy-MM-dd');
	var d = new Date();
	d.setMonth(d.getMonth() - 1);
	var firstdate = d.format('yyyy-MM-dd');
	var statuslist = [["全部", "%"], ["待消缺", "1"], ["待确认", "2"],
			["已确认待消缺", "18"], ["点检待验收", "3"], ["运行待验收", "14"], ["验收退回", "9"],
			["设备部仲裁", "7"], ["已仲裁待消缺", "8"], ["点检延期待处理审批", "11"],
			["设备部主任延期待处理审批", "12"], ["发电部延期待处理审批", "13"], ["总工延期待处理审批", "20"],
			["已处理", "5"], ["延期待处理退回", "15"]];
	var stime = {
		xtype : 'datefield',
		format : 'Y-m-d',
		id : 'stime',
		name : 'stime',
		value : firstdate,
		readOnly : true
	};
	var etime = {
		xtype : 'datefield',
		format : 'Y-m-d',
		id : 'etime',
		name : 'etime',
		value : nowdate,
		readOnly : true
	};

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
		anchor : "95%"
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
		anchor : '95%'
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
		anchor : '95%',
		typeAhead : true
	});

	var currentPanel = "main";
	var Failure = new Ext.data.Record.create([{
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
		name : 'toEliminDate'
	}, {
		name : 'groupName'
	}]);
	var ds = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url : 'bqfailure/approveFailurelist.action',
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : 'list'
		}, Failure),
		groupField : 'groupName',
		sortInfo:'',
		remoteGroup:true
//		groupOnSort : true, 
//		sortInfo : {
//			field : 'groupName',
//			direction : "ASC"
//		}
	});
	ds.load({
		params : {
			start : 0,
			limit : 18,
			status : '%',
			sdate : firstdate,
			edate : nowdate,
			belongBlock : "",
			specialityCode : "",
			deptCode : ""
		}
	});
	ds.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			status : document.getElementById("status").value,
			sdate : document.getElementById("stime").value,
			edate : document.getElementById("etime").value,
			belongBlock : document.getElementById("belongSystemGrid").value,
			specialityCode : tbardominationProfessionComboBox.getValue(),
			deptCode : tbarrepairDepComboBox.getValue()
		});
	});
	/* 设置每一行的选择框 */
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});
	var colModel = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
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
	},  {
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
	}, {
		header : '消缺时限',
		dataIndex : 'toEliminDate',
		renderer: function(v){
			return "<font color='red'>"+v+"</font>";
		},
		width : 120
	}, {
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
	},{
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
	}, {
		header : '设备功能码',
		dataIndex : 'attributeCode',
		width : 120
	}, {
		header : '设备名称',
		dataIndex : 'equName',
		width : 120
	}, {
		dataIndex : 'workFlowNo',
		hidden : true
	}, {
		header : '填写人',
		dataIndex : 'writeByName',
		width : 60
	}, {
		header : '填写人部门',
		dataIndex : 'writeDeptName',
		width : 100
	}, {
		header : '类别',
		dataIndex : 'groupName',
		align : 'left',
		hidden : true,
		width : 120
	}]);
	colModel.defaultSortable = true;
	/* 设置分页的工具条 */
	var bbar = new Ext.PagingToolbar({
		id : 'barid',
		pageSize : 18,
		store : ds,
		displayInfo : true,
		displayMsg : '显示第 {0} 条到 {1} 条记录，一共 {2} 条',
		emptyMsg : "没有记录"
	})
	function queryIt() {
		ds.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}
	function queryWF() {
		var rec = grid.getSelectionModel().getSelected();
		if (rec) {
			if (rec.get("workFlowNo") != "") {
				window.open("../../../workflow/manager/show/show.jsp?entryId="
						+ rec.get("workFlowNo"));
			}
		} else {
			Ext.MessageBox.alert('提示','请选择一条记录!');
		}
	}
	/* 创建表格 */
	var grid = new Ext.grid.GridPanel({
		id : 'gridid',
		ds : ds,
		cm : colModel,
		autoScroll : true,
		autoWidth : true,
		sm : sm,
		bbar : bbar,
		tbar : new Ext.Toolbar({
			items : ['从', stime, '到', etime, '-', '状态', status, '-', '所属系统',
					blockComboBoxGrid]
		}),
		title : '缺陷审批列表<font color="red">(双击进行审批)</font>',
		view : new Ext.grid.GroupingView({
			forceFit : false,
			enableGroupingMenu : false,
			hideGroupedColumn : true,
			showGroupName : false,
			startCollapsed : false,
			groupTextTpl : '{text}'
		}),
		border : false
	});

	grid.on('rowdblclick', function(grid, rowIndex, e) {
		var rec = grid.getSelectionModel().getSelected();
		if (rec) {
			Ext.Ajax.request({
				url : 'bqfailure/approveFailure.action?failure.id='
						+ rec.get("id"),
				method : 'post',
				waitMsg : '正在处理数据...',
				success : function(result, request) {
					var json = eval('(' + result.responseText + ')');
					window
							.showModalDialog(
									json.msg,
									json,
									'dialogWidth=860px;dialogHeight=700px;center=yes;help=no;resizable=no;status=no;');
					ds.load({
						params : {
							start : 0,
							limit : 18
						}
					});
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('提示','审批失败！');
				}
			});
		}
	});
	var layout = new Ext.Viewport({
		layout : "fit",
		border : false,
		items : [grid]
	});

	var tbar2 = new Ext.Toolbar({
		id : 'secondTbar',
		renderTo : grid.tbar,
		items : ['管辖专业', tbardominationProfessionComboBox, '-', '检修部门',
				tbarrepairDepComboBox, '-', {
					id : 'query',
					text : "查询",
					iconCls : 'query',
					handler : queryIt
				}, '-', {
					id : 'approve',
					text : "审批签字",
					iconCls : 'write',
					handler : function() {
						var rec = grid.getSelectionModel().getSelected();
						if (rec) {
							Ext.Ajax.request({
								url : 'bqfailure/approveFailure.action?failure.id='
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
													'dialogWidth=860px;dialogHeight=700px;center=yes;help=no;resizable=no;status=no;');
									ds.load({
										params : {
											start : 0,
											limit : 18
										}
									});
								},
								failure : function(result, request) {
									Ext.MessageBox.alert('提示', '操作失败！');
								}
							});
						} else {
							Ext.MessageBox.alert('提示','请选择一条记录!');
						}
					}
				}, '-', {
					id : 'queryWF',
					text : "查看流程",
					iconCls : 'view',
					handler : queryWF
				}]
	});
	grid.setHeight(document.getElementById("gridid").offsetHeight
			- document.getElementById("secondTbar").offsetHeight);

});