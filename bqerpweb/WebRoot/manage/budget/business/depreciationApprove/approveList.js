Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	function percentFormat(value) {
		if (value == null || value == "null") {
			value = "0";
		}
		value = String(value);
		// 整数部分
		var whole = value;
		// 小数部分
		var sub = ".00";
		// 如果有小数
		if (value.indexOf(".") > 0) {
			whole = value.substring(0, value.indexOf("."));
			sub = value.substring(value.indexOf("."), value.length);
			sub = sub + "00";
			if (sub.length > 3) {
				sub = sub.substring(0, 3);
			}
		}
		var v = whole + sub;
		return v + '%';
	}

	function getDate() {
		var Y;
		Y = new Date();
		Y = Y.getFullYear().toString(10);
		return Y;
	}
	var txtYear = new Ext.form.TextField({
		id : 'txtYear',
		allowBlank : true,
		readOnly : true,
		value : getDate(),
		width : 100,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy',
					alwaysUseStartDate : false,
					onclearing : function() {
						planStartDate.markInvalid();
					}
				});
			}
		}
	});

	var sm = new Ext.grid.CheckboxSelectionModel();
	// 创建记录类型
	var Myrecord = Ext.data.Record.create([{
		name : 'depreciationDetailId'
	}, {
		name : 'depreciationId'
	}, {
		name : 'assetName'
	}, {
		name : 'lastAsset'
	}, {
		name : 'addAsset'
	}, {
		name : 'reduceAsset'
	}, {
		name : 'newAssetCount'
	}, {
		name : 'depreciationRate'
	}, {
		name : 'depreciationNumber'
	}, {
		name : 'depreciationSum'
	}, {
		name : 'memo'
	}, {
		name : 'budgetTime'
	}, {
		name : 'workFlowNo'
	}, {
		name : 'workFlowStatus'
	}]);
	// 配置数据集
	var store = new Ext.data.Store({
		/* 创建代理对象 */
		proxy : new Ext.data.HttpProxy({
			url : 'managebudget/depreciationApproveQuery.action'
		}),
		/* 创建解析Json格式数据的解析器 */
		reader : new Ext.data.JsonReader({
			root : "list",
			totalProperty : "totalCount"
		}, Myrecord)
	});

	function queryRecord() {
		store.baseParams = {
			budgetTime : txtYear.getValue()
		};
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}

	// 查询
	var query = new Ext.Button({
		id : "btnQuery",
		text : "查询",
		iconCls : "query",
		handler : queryRecord
	})

	// 定义grid
	// 事件状态
	var sm_store_item = new Ext.grid.ColumnModel([sm,
			new Ext.grid.RowNumberer({
				header : "行号",
				width : 31
			}), {
				header : '资产名称',
				width : 40,
				dataIndex : 'assetName',
				align : 'left',
				editor : new Ext.form.TextField({})
			}, {
				header : '去年年末资产',
				dataIndex : 'lastAsset',
				width : 40,
				align : 'left',
				editor : new Ext.form.NumberField({})
			}, {
				header : '本年预算增加',
				width : 40,
				dataIndex : 'addAsset',
				align : 'left',
				editor : new Ext.form.NumberField({})

			}, {
				header : '本年预算减少',
				width : 40,
				dataIndex : 'reduceAsset',
				align : 'left',
				editor : new Ext.form.NumberField({})
			}, {
				header : '本年年末资产',
				width : 40,
				dataIndex : 'newAssetCount',
				align : 'left',
				editor : new Ext.form.NumberField({})
			}, {
				header : '年折旧率',
				width : 40,
				dataIndex : 'depreciationRate',
				align : 'left',
				editor : new Ext.form.NumberField({}),
				renderer : function(value) {
					if (value == '999999999999999') {
						return null;
					} else {
						return percentFormat(value);
					}
				}
			}, {
				header : '年折旧预算数',
				width : 40,
				dataIndex : 'depreciationNumber',
				align : 'left',
				editor : new Ext.form.NumberField({})
			}, {
				header : '累计折旧额',
				width : 40,
				dataIndex : 'depreciationSum',
				align : 'left',
				editor : new Ext.form.NumberField({})
			}, {
				header : '备注',
				width : 40,
				dataIndex : 'memo',
				align : 'left',
				editor : new Ext.form.TextArea({})
			}]);

	// 底部分页栏
	var bbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : store,
		displayInfo : true,
		displayMsg : "{0} 到 {1} /共 {2} 条",
		emptyMsg : "没有记录",
		beforePageText : '',
		afterPageText : ""
	});
	// 顶部工具栏
	var tbar = new Ext.Toolbar({
		items : ['年度', txtYear, '-', query, {
			text : '审批',
			iconCls : 'view',
			handler : report
		}]
	});
	// 可编辑的表格
	var Grid = new Ext.grid.GridPanel({
		sm : sm,
		ds : store,
		cm : sm_store_item,
		autoScroll : true,
		bbar : bbar,
		tbar : tbar,
		border : true,
		clicksToEdit : 1,
		viewConfig : {
			forceFit : true
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
	});

	// Grid.on("rowdblclick", report);
	function report() {
		// ----------------
		if (store.getCount() == 0) {
			Ext.Msg.alert("提示", "没有记录！");
			return;
		}
		var record = store.getAt(0);
		var workFlowNo = record.get('workFlowNo');

		Ext.Ajax.request({
			url : 'MAINTWorkflow.do?action=getCurrentStepsInfo',
			method : 'POST',
			params : {
				entryId : workFlowNo
			},
			success : function(result, request) {
				var url = "";
				var obj = eval("(" + result.responseText + ")");
				var args = new Object();
				if (obj[0].url == null || obj[0].url == "") {
					url = "approveSign.jsp";
				} else {
					url = "../../../../../" + obj[0].url;
				}

				args.busiNo = record.get('depreciationId');
				args.entryId = record.get("workFlowNo");
				var obj = window.showModalDialog(url, args,
						'status:no;dialogWidth=750px;dialogHeight=550px');
				if (obj) {
					queryRecord();
				}

			},
			failure : function(result, request) {
				Ext.Msg.alert("提示", "错误");
			}

		});
		//-----------------

	}

	queryRecord();

})
