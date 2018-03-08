Ext.onReady(function() {

	// 编辑人编码
	var editBy = new Ext.form.TextField({
				id : 'editBy',
				name : 'editBy'
			});

	// 从session取登录人编码姓名部门相关信息
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号
							var workerCode = result.workerCode;
							var workerName = result.workerName;
							editBy.setValue(workerCode);
						}
					}
				});
	}

	var applyInOut = new Ext.form.ComboBox({
				fieldLabel : '投/退',
				store : new Ext.data.SimpleStore({
							fields : ['value', 'text'],
							data : [['I', '投入申请单'], ['O', '退出申请单']]
						}),
				id : 'inOut',
				name : 'inOut',
				valueField : "value",
				displayField : "text",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'protectApply.inOut',
				editable : false,
				allowBlank : false,
				triggerAction : 'all',
				selectOnFocus : true,
				width : 100
			});

	var ApplyProtectionType = new Ext.form.ComboBox({
				fieldLabel : '投退保护类型',
				store : new Ext.data.SimpleStore({
							fields : ['value', 'text'],
							data : [['1', '设备继电保护及安全自动装置、调动自动化投入申请单'],
							        ['2', '设备继电保护及安全自动装置、调动自动化退出申请单'],
							        ['3', '热控保护投入申请单'],
									['4', '热控保护退出申请单']]
						}),
				id : 'protectionType',
				name : 'protectionType',
				valueField : "value",
				displayField : "text",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'protectApply.protectionType',
				editable : false,
				allowBlank : false,
				triggerAction : 'all',
				selectOnFocus : true,
				width : 320
			});

	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'applyId',
		mapping : 0
	}, {
		name : 'applyCode',
		mapping : 1
	}, {
		name : 'inOut',
		mapping : 2
	}, {
		name : 'protectionType',
		mapping : 3
	}, {
		name : 'applyDep',
		mapping : 4
	}, {
		name : 'deptName',
		mapping : 5
	}, {
		name : 'applyBy',
		mapping : 6
	}, {
		name : 'applyName',
		mapping : 7
	}, {
		name : 'applyTime',
		mapping : 8
	}, {
		name : 'protectionName',
		mapping : 9
	}, {
		name : 'protectionReason',
		mapping : 10
	}, {
		name : 'measures',
		mapping : 11
	}, {
		name : 'workflowNo',
		mapping : 12
	}, {
		name : 'status',
		mapping : 13
	}, {
		name : 'statusName',
		mapping : 14
	}, {
		name : 'classLeader',
		mapping : 15
	}, {
		name : 'classLeaderName',
		mapping : 16
	}, {
		name : 'executor',
		mapping : 17
	}, {
		name : 'executorName',
		mapping : 18
	}, {
		name : 'guardian',
		mapping : 19
	}, {
		name : 'guardianName',
		mapping : 20
	}, {
		name : 'executeTime',
		mapping : 21
	}, {
		name : 'memo',
		mapping : 22
	}, {
		name : 'blockId',
		mapping : 23
	}]);
	var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'productionrec/getCastBackProtectApproveList.action'
						}),
				reader : new Ext.data.JsonReader({
							id : 0,
							totalProperty : 'totalCount',
							root : 'list'
						}, MyRecord)
			});

	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});

	var cm = new Ext.grid.ColumnModel({
		columns : [sm, new Ext.grid.RowNumberer({
			header : '序号',
			width : 35,
			align : 'center'
		}), {
			header : "编号",
			width : 65,
			sortable : true,
			dataIndex : 'applyCode'
		}, {
			header : "退/投",
			width : 80,
			sortable : true,
			dataIndex : 'inOut',
			renderer : function(v) {
				if (v == 'I') {
					return "投入申请单";
				}
				if (v == 'O') {
					return "退出申请单";
				}
			}
		}, {
			header : "退投保护类型",
			width : 250,
			sortable : true,
			dataIndex : 'protectionType',
			renderer : function(v) {
				if (v == '1') {
					return "设备继电保护及安全自动装置、调动自动化投入申请单";
				}
				if (v == '2') {
					return "设备继电保护及安全自动装置、调动自动化退出申请单";
				}
				if (v == '3') {
					return "热控保护投入申请单";
				}
				if (v == '4') {
					return "热控保护退出申请单";
				}
			}
		}, {
			dataIndex : "blockId",
			header : '机组',
			width : 150,
			renderer : function(v) {
				if (v == 1)
					return '300MW机组';
				else if (v == 4)
					return '125MW机组';
			}
		}, {
			header : "申请部门",
			width : 90,
			sortable : true,
			dataIndex : 'deptName'
		}, {
			header : "申请人",
			width : 90,
			sortable : true,
			dataIndex : 'applyName'
		}, {
			header : "申请时间",
			width : 130,
			sortable : true,
			dataIndex : 'applyTime'
		}, {
			header : "保护名称",
			width : 140,
			sortable : true,
			dataIndex : 'protectionName'
		}, {
			header : "申请单状态",
			width : 100,
			sortable : true,
			dataIndex : 'statusName'
		}]
	});

	var grid = new Ext.grid.GridPanel({
				region : "center",
				store : store,
				cm : cm,
				sm : sm,
				tbar : [
//				'投/退:', applyInOut, '-',
				'投退保护类型:',
						ApplyProtectionType, '-', {
							text : "查询",
							id : 'btnQuery',
							iconCls : 'query',
							handler : queryRecord
						}, '-', {
							id : 'detailsBtu',
							text : "详细信息",
							iconCls : 'update',
							handler : detailsRecord
						}, '-', {
							id : 'btnFlow',
							text : "流程展示",
							iconCls : 'view',
							handler : flowQuery
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

	grid.on('rowdblclick', detailsRecord);

	function queryRecord() {
		store.baseParams = {
			status : "approve",
			inOut : applyInOut.getValue(),
			protectionType : ApplyProtectionType.getValue()
		}
		store.load({
					params : {
						start : 0,
						limit : 18
					}
				});
	}
	function detailsRecord() {

		if (!sm.hasSelection() || sm.getSelections().length > 1)
			Ext.Msg.alert('提示', '请先选择一行记录！');
		else {
			var rec = sm.getSelected();
			var status = rec.get('status');
			if (status == '7') {
				if (rec.get('executor') == editBy.getValue()) {
					applyId = rec.get('applyId');
				    applyCode = rec.get('applyCode');
					workflowNo = rec.get('workflowNo');
					window.location.replace("cBPReport.jsp?status=" + status
							+ "&applyId=" + applyId 
						    + "&applyCode=" + applyCode 
							+ "&workflowNo="+ workflowNo);
				} else {
					Ext.Msg.alert("提示", "你不是执行人无权进入该页面！");
					return;
				}

			} else {
				applyId = rec.get('applyId');
				workflowNo = rec.get('workflowNo');
				window.location.replace("cBPApproveDetail.jsp?status=" + status
						+ "&applyId=" + applyId
						+ "&workflowNo=" + workflowNo
						+ "&protectionType=" + rec.get('protectionType')
						+ "&blockId=" + rec.get('blockId')
						+ "&executor=" + rec.get('executor'));

			}
		}
	};

	function flowQuery() {
		if (!sm.hasSelection() || sm.getSelections().length > 1)
			Ext.Msg.alert('提示', '请先选择一条记录！');
		else {
			var url = '';
			var rec = sm.getSelections();
			if (rec[0].get('workflowNo') == null
					|| rec[0].get('workflowNo') == '') {
				url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
						+ "bqCastBackProtectApprove";
				window.open(url);

			} else {
				url = "/power/workflow/manager/show/show.jsp?entryId="
						+ rec[0].get('workflowNo');
				window.open(url);

			}
		}
	}

	queryRecord();

	getWorkCode()

	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							title : "",
							region : 'center',
							layout : 'fit',
							border : false,
							margins : '0 0 0 0',
							split : true,
							collapsible : false,
							items : [grid]
						}]
			});

});