Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

var protectApply = new ProtectApply();

Ext.onReady(function() {

	// 系统当天日期
	var sdate = new Date();
	var edate = new Date();
	// 系统当天前30天的日期
	sdate.setDate(sdate.getDate() - 30);
	// 系统当天后30天的日期
	edate.setDate(edate.getDate() + 30);

	var fromDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : 'startDate',
		id : 'fromDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		checked : true,
		fieldLabel : "申请日期",
		allowBlank : false,
		readOnly : true,
		value : sdate,
		emptyText : '请选择',
		anchor : '100%'
	});
	fromDate.setValue(sdate);
	var toDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : 'endDate',
		value : edate,
		id : 'toDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		fieldLabel : "至",
		allowBlank : false,
		readOnly : true,
		emptyText : '请选择',
		anchor : '80%'
	});
	toDate.setValue(edate);

//	// 定义状态
//	var stateComboBox = new Ext.form.ComboBox({
//		id : "stateCob",
//		store : protectApply.reportStatus,
//		displayField : "name",
//		valueField : "value",
//		mode : 'local',
//		triggerAction : 'all',
//		hiddenName : 'stateComboBox',
//		readOnly : true,
//		value : '',
//		width : 120
//	});

	// 所属部门
	var topDeptRootNode = new Ext.tree.AsyncTreeNode({
		id : '-1',
		text : '所有'
	})
	var comboDepChoose = new Ext.ux.ComboBoxTree({
		labelwidth : 50,
		fieldLabel : '部门',
		id : 'deptId',
		displayField : 'text',
		width : 170,
		valueField : 'id',
		hiddenName : 'power.applyDept',
		blankText : '请选择',
		value : topDeptRootNode,
		emptyText : '请选择',
		readOnly : true,
		tree : {
			xtype : 'treepanel',
			loader : new Ext.tree.TreeLoader({
				dataUrl : 'comm/getDeptsByPid.action'
			}),
			root : new Ext.tree.AsyncTreeNode({
				id : '-1',
				text : '所有'
			})
		}
		// selectNodeModel : 'exceptRoot',
		,
		selectNodeModel : 'all'

	});
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'power.applyId'
	}, {
		name : 'power.protectNo'
	}, {
		name : 'power.applyDept'
	}, {
		name : 'applyDeptName'
	}, {
		name : 'power.applyBy'
	}, {
		name : 'applyName'
	}, {
		name : 'applyDate'
	}, {
		name : 'power.equCode'
	}, {
		name : 'equName'
	}, {
		name : 'power.protectName'
	}, {
		name : 'power.protectReason'
	}, {
		name : 'power.equEffect'
	}, {
		name : 'power.outSafety'
	}, {
		name : 'power.memo'
	}, {
		name : 'applyStartDate'
	}, {
		name : 'applyEndDate'
	}, {
		name : 'power.statusId'
	}, {
		name : 'statusName'
	}, {
		name : 'power.workFlowNo'
	}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'protect/findByIsinList.action'
			});

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);

	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
	// 分页
	var fuzzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});

	var sm = new Ext.grid.CheckboxSelectionModel();
	var grid = new Ext.grid.GridPanel({
		region : "center",
		store : store,
		columns : [sm, new Ext.grid.RowNumberer({
			header : '序号',
			width : 35,
			align : 'left'
		}), {

			header : "ID",
			width : 100,
			sortable : true,
			dataIndex : 'power.applyId',
			hidden : true
		}, {

			header : "状态ID",
			width : 100,
			sortable : true,
			dataIndex : 'power.statusId',
			hidden : true
		}, {

			header : "申请单号",
			width : 80,
			sortable : true,
			dataIndex : 'power.protectNo'
		}, {

			header : "状态",
			width : 70,
			sortable : true,
			dataIndex : 'power.statusId',
			renderer : function(value) {

				return protectApply.getStatusName(value);
			}
		},

		{
			header : "申请人编码",
			width : 100,
			sortable : true,
			dataIndex : 'power.applyBy',
			hidden : true
		},

		{
			header : "申请人",
			width : 85,
			sortable : true,
			dataIndex : 'applyName'
		}, {
			header : "申请部门编码",
			width : 100,
			sortable : true,
			dataIndex : 'power.applyDept',
			hidden : true
		}, {
			header : "申请部门",
			width : 70,
			sortable : true,
			dataIndex : 'applyDeptName'
		}, {
			header : "申请时间",
			width : 110,
			sortable : true,
			dataIndex : 'applyDate'
		}, {
			header : "设备名称",
			width : 110,
			sortable : true,
			dataIndex : 'equName'
		}, {
			header : "保护名称",
			width : 110,
			sortable : true,
			dataIndex : 'power.protectName'
		}, {
			header : "投退原因",
			width : 150,
			sortable : true,
			renderer : function(v) {
				return v.replace(/\r/g, "<br/>");
			},
			dataIndex : 'power.protectReason'
		}, {
			header : "对系统或设备的影响",
			width : 150,
			sortable : true,
			renderer : function(v) {
				return v.replace(/\r/g, "<br/>");
			},
			dataIndex : 'power.equEffect'
		}, {
			header : "保护退出时安措",
			width : 150,
			sortable : true,
			renderer : function(v) {
				return v.replace(/\r/g, "<br/>");
			},
			dataIndex : 'power.outSafety'
		}, {
			header : "申请开工时间",
			width : 110,
			sortable : true,
			dataIndex : 'applyStartDate'
		}, {
			header : "申请完工时间",
			width : 110,
			sortable : true,
			dataIndex : 'applyEndDate'
		}, {
			header : "备注",
			width : 150,
			sortable : true,
			dataIndex : 'power.memo'
		}],
		sm : sm,
		tbar : ['时间范围：', fromDate, '~', toDate,
				"&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp部门:", comboDepChoose,
//				"&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp状态:",
//				stateComboBox, 
					{
					text : "查询",
					iconCls : 'query',
					handler : queryRecord
				}],
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});

	grid.on('rowdblclick', getDate);
	function getDate() {
		if (grid.selModel.hasSelection()) {
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系提示", "请选择其中一条！");
			} else {
				var member = records[0];
				var obj = new Object;
				obj.protectNo=records[0].get("power.protectNo");
				obj.protectName=records[0].get("power.protectName");
				obj.equCode=records[0].get("power.equCode");
				obj.equName=records[0].get("equName");
				window.returnValue = obj;
				window.close();
			}
		} else {
			Ext.Msg.alert("提示", "请选择其中一条!");
		}
	}

	function queryRecord() {
		var ftime = Ext.get('fromDate').dom.value;
		var ttime = Ext.get('toDate').dom.value;
		if (ftime > ttime) {
			Ext.Msg.alert('提示', '选择后一日期应比前一日期大！');
			return false;
		}
		store.load({
			params : {
				start : 0,
				limit : 18,
//				status : stateComboBox.getValue(),
				status : 7,
				startDate : Ext.get('fromDate').dom.value,
				endDate : Ext.get('toDate').dom.value,
				applyDept : comboDepChoose.value == '-1'
						? ''
						: comboDepChoose.value,
				isIn : 'N'
			}
		});
	}

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});

	queryRecord();

});