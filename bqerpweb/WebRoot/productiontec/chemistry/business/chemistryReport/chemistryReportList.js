Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		// 月报主表编号
		name : 'phj.zxybybzbId'
	}, {
		// 机组 编码
		name : 'phj.deviceCode'
	}, {
		// 月份 日期型
		name : 'phj.reportTime'
	}, {
		// 备注
		name : 'phj.memo'
	}, {
		// 填报人 编码
		name : 'phj.fillBy'
	}, {
		// 填报日期
		name : 'phj.fillDate'
	}, {
		// 工作流序号
		name : 'phj.workFlowNo'
	}, {
		// 工作流状态
		name : 'phj.workFlowStatus'
	}, {
		// 单位编码
		name : 'phj.enterpriseCode'
	}, {
		// 机组名
		name : 'deviceName'
	}, {
		// 月份
		name : 'month'
	}, {
		// 填报人姓名
		name : 'fillName'
	}, {
		// 填报时间
		name : 'fillDate'
	}
//	, {
//		// 月报编号
//		name : 'zxybybId'
//	}, {
//		// 仪表ID
//		name : 'meterId'
//	}, {
//		// 必投台数
//		name : 'mustThrowNum'
//	}, {
//		// 配备台数
//		name : 'equipNum'
//
//	}, {
//		// 投运台数
//		name : 'throwNum'
//	}, {
//		// 配备率
//		name : 'equipRate'
//
//	}, {
//		// 投入率
//		name : 'throwRate'
//	}, {
//		// 抄表率
//		name : 'searchRate'
//	}, {
//		// 仪表名称
//		name : 'meterName'
//	}
	]);
	var dataProxy = new Ext.data.HttpProxy(

			{
				url : 'productionrec/findPtHxjdJZxybybzbList.action'
			});
	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);
	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});


	// 所属机组
	var storeCharge = new Ext.data.Store({
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
	storeCharge.load();
	var cbxCharge = new Ext.form.ComboBox({
				id : 'cbxCharge',
				fieldLabel : "机组<font color='red'>*</font>",
				store : storeCharge,
				displayField : "blockName",
				valueField : "blockCode",
				hiddenName : 'phj.deviceCode',
				mode : 'local',
				triggerAction : 'all',
				value : '',
				readOnly : true,
				width : 130,
				listeners : {
					select : function() {
					}
				}
			})

			
		// 月份
	var month = new Ext.form.TextField({
				fieldLabel : "月份",
				id : 'month',
				readOnly : true,
				width : 100,
				name : 'month',
				listeners : {
					focus : function() {
						WdatePicker({
									alwaysUseStartDate : true,
									dateFmt : "yyyy-MM"

								});
						this.blur();
					}
				}
			});

	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	});

	var grid = new Ext.grid.GridPanel({
		region : "center",
		store : store,
		autoScroll : true,
		viewConfig : {
			forceFit : false
		},
		loadMask : {
			msg : '<img src="comm/images/extanim32.gif" width="32" height="32" style="margin-right: 8px;" align="absmiddle" />读取数据中 ...'
		},
		columns : [sm, new Ext.grid.RowNumberer({
			header : '行号',
			width : 35,
			align : 'left'
		}), {
			header : "月报主表编号",
			width : 100,
			sortable : true,
			dataIndex : 'phj.zxybybzbId',			
			hidden : true
		}, {
			header : "机组",
			width : 80,
			sortable : true,
			dataIndex : 'deviceName'
		}, {
			header : "月份",
			width : 80,
			sortable : true,
			dataIndex : 'month'
		}, {
			header : "备注",
			width : 80,
			sortable : true,
			dataIndex : 'phj.memo'
		}, {
			header : "填报人",
			width : 130,
			sortable : true,
//			hidden : true,
			dataIndex : 'fillName'
		}, {
			header : "填报日期",
			width : 130,
			sortable : true,
//			hidden : true,
			dataIndex : 'fillDate'
		}, {
			header : "工作流序号",
			width : 100,
			sortable : true,
			align : 'left',
			dataIndex : 'phj.workFlowNo'
		}, {
			header : "工作流状态",
			width : 100,
			sortable : true,
			align : 'center',
			dataIndex : 'phj.workFlowStatus'
			}],
		sm : sm,
		tbar : ['机组', cbxCharge, '-', '月份', month 
		, {
			text : "查询",
			iconCls : 'query',
			handler : queryRecord
		}, '-', {
			text : "新增",
			iconCls : 'add',
			handler : function() {
				parent.currentRecord = null;
				parent.document.all.iframe1.src = "productiontec/chemistry/business/chemistryReport/chemistryReportInfo.jsp";
				parent.Ext.getCmp("maintab").setActiveTab(0);
			}
		}, '-', {
			text : "修改",
			iconCls : 'update',
			handler : updateRecord
		}, '-', {
			text : "删除",
			iconCls : 'delete',
			handler : deleteRecord
		}, '-', {
			text : "上报",
			iconCls : 'upcommit',
			handler : reportBtn
		}
		],
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});
	grid.on('rowdblclick', updateRecord);
	function updateRecord() {
		if (grid.selModel.hasSelection()) {
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				var member = records[0];
				parent.currentRecord = member;
					var ffid = member.get("phj.zxybybzbId");
					var url = "productiontec/chemistry/business/chemistryReport/chemistryReportInfo.jsp";
					parent.document.all.iframe1.src = url;
					parent.Ext.getCmp("maintab").setActiveTab(0);
				

			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}
	function deleteRecord() {
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		var nos = [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {

			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("phj.zxybybzbId")) {	
						ids.push(member.get("phj.zxybybzbId"));
						nos.push(member.get("deviceName"));

				} else {

					store.remove(store.getAt(i));
				}
			}

			if (nos.length > 0) {
				Ext.Msg.confirm("删除", "是否确定删除所选的数据？", function(
						buttonobj) {
					if (buttonobj == "yes") {
						// 待做
						Ext.lib.Ajax.request('POST',
								'productionrec/deletePtHxjdJZxybybzb.action', {
									success : function(action) {
										Ext.Msg.alert("提示", "数据删除成功！");
										queryRecord();
										parent.currentRecord = null;
									},
									failure : function() {
										Ext.Msg.alert('错误', '删除时出现未知错误.');
									}
								}, 'ids=' + ids);
					}
				});
			}

		}
	}
	function queryRecord() {
		store.baseParams = {
			name : cbxCharge.getValue(),
			month : Ext.get('month').dom.value
		}
		store.load({
					params : {
						start : 0,
						limit : 18
					}
				});
	}

	// 上报处理
	function reportBtn() {
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var materialNo;
		var isReport;
		if (selected.length != 1) {
			Ext.Msg.alert("系统提示信息", "请选择其中一项进行处理！");
		} else {	
			alert("上报处理暂时未做，等待后面完成")
		}
	}


	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [grid]
	});

	queryRecord();


});