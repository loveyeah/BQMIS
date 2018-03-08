Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	// 获得年份
	function getDate() {
		var Y;
		Y = new Date();
		Y = Y.getFullYear().toString(10);
		return Y;
	}
	// 获得月份
	function getMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString() + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		return s;
	}

	// 系统当前时间
	function getTime() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		return s;
	}
	// 定义从页面取得数据

	var budgetMakeId=getParameter("budgetMakeId");
	
	function queryData() {
		
	 store.load({
			params : {
				budgetMakeId : budgetMakeId
			}
		});
		store.rejectChanges();
	 
	}
	
	// 年月份选择
	var formatType;
	var yearRadio = new Ext.form.Radio({
		id : 'year',
		name : 'queryWayRadio',
		// blankText : getDate(),
		hideLabel : true,
		boxLabel : '年份'
	});
	var monthRadio = new Ext.form.Radio({
		id : 'month',
		name : 'queryWayRadio',
		hideLabel : true,
		boxLabel : '月份',
		checked : true,
		listeners : {
			check : function() {
				var queryType = getChooseQueryType();
				switch (queryType) {
					case 'year' : {
						formatType = 1;
						time.setValue(getDate());
						break;
					}
					case 'month' : {
						time.setValue(getMonth());
						formatType = 2;
						break;
					}
				}
			}
		}
	});

	var time = new Ext.form.TextField({
		id : 'time',
		allowBlank : true,
		readOnly : true,
		value : getMonth(),
		width : 100,
		listeners : {
			focus : function() {
				var format = '';
				if (formatType == 1)
					format = 'yyyy';
				if (formatType == 2)
					format = 'yyyy-MM';
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : format,
					alwaysUseStartDate : false,
					onclearing : function() {
						planStartDate.markInvalid();
					}
				});
			}
		}
	});
	// 遍历所有的REDIO获得ID
	function getChooseQueryType() {
		var list = document.getElementsByName("queryWayRadio");
		for (var i = 0; i < list.length; i++) {
			if (list[i].checked) {
				return list[i].id;
			}
		}
	}

	var sm = new Ext.grid.CheckboxSelectionModel();
	// 创建记录类型
	var Myrecord = Ext.data.Record.create([{
		// 预算指标id
		name : 'itemId'
	}, {
		// 预算指标名称
		name : 'itemAlias'
	},
	{
		name:'financeItem'
	},{
		// 计量单位名称
		name : 'unitName'
	}, {
		// 预算值
		name : 'adviceBudget'
	}, {
		// 编制依据
		name : 'budgetBasis'
	}, {
		// 预算编制单ID
		name : 'budgetMakeId'
	}, {
		// 预算明细ID
		name : 'budgetItemId'
	}, {
		// 预算部门id
		name : 'centerId'
	}, {
		// 预算主题id
		name : 'topicId'
	}, {
		// 预算时间
		name : 'budgetTime'
	}, {
		// 工作流序号
		name : 'workFlowNo'
	}, {
		// 编制状态
		name : 'makeStatus'
	},
	{ name:'dataSource'}
	]);
	// 配置数据集
	var store = new Ext.data.Store({
		/* 创建代理对象 */
		proxy : new Ext.data.HttpProxy({
			url : 'managebudget/findMakeItemListByMakeId.action'
		}),
		/* 创建解析Json格式数据的解析器 */
		reader : new Ext.data.JsonReader({
			root : "list",
			totalProperty : "totalCount"
		}, Myrecord)
	});
	
	



	// 保存
	var save = new Ext.Button({
		id : "btnSave",
		text : "保存",
		iconCls : "save",
		handler : function saveModifies() {
			Grid.stopEditing();
			var modifyRec = Grid.getStore().getModifiedRecords();
			if (modifyRec.length > 0) {
				if (!confirm("确定要保存修改吗?")) {
					return false;
				}
				Ext.Msg.wait("正在保存数据,请等待...");
				var modifyRecords = new Array();
				for (var i = 0; i < modifyRec.length; i++) {
					modifyRecords.push(modifyRec[i].data);
				}
				
				Ext.Ajax.request({
					url : 'managebudget/saveBudgetMake.action',
					method : 'post',
					params : {
						addOrUpdateRecords : Ext.util.JSON.encode(modifyRecords)
					},
					success : function(result, request) {
						Ext.MessageBox.alert('提示', '操作成功！');
						store.reload();
						store.rejectChanges();
	
	
					},
					failure : function(result, request) {
						Ext.MessageBox.alert('提示', '操作失败！');
						store.reload();
						store.rejectChanges();
					}
				})
			} else {
				Ext.Msg.alert('提示', '您没有做任何修改！'); 
			}

		}
	})



	// 定义grid
	// 事件状态
	var sm_store_item = new Ext.grid.ColumnModel([sm, {
		header : '预算项目',
		align : 'center',
		width : 110,
		sortable : true,
		dataIndex : 'itemAlias'
			}, {
				header : '财务科目编码',
				align : 'center',
				width : 110,
				sortable : true,
				dataIndex : 'financeItem'
			}, {
				header : '计量单位',
				align : 'center',
				width : 110,
				sortable : true,
				dataIndex : 'unitName'
			}, {
				header : '预算值',
				align : 'center',
				width : 110,
				sortable : true,
				dataIndex : 'adviceBudget',
				editor : new Ext.form.NumberField()
			}, {
				header : '编制依据',
				align : 'center',
				width : 110,
				sortable : true,
				dataIndex : 'budgetBasis',
				editor : new Ext.form.TextField()
			}, {
				header : "预算编制单Id",
				align : 'center',
				width : 110,
				sortable : true,
				hidden : true,
				dataIndex : 'budgetMakeId'
			}, {
				header : '预算部门',
				align : 'center',
				width : 110,
				sortable : true,
				hidden : true,
				dataIndex : 'centerId'
			}, {
				header : '预算主题',
				align : 'center',
				width : 110,
				sortable : true,
				hidden : true,
				dataIndex : 'topicId'
			}, {
				header : '预算时间',
				align : 'center',
				width : 110,
				sortable : true,
				hidden : true,
				dataIndex : 'budgetTime'
			}, {
				header : '工作流序号',
				align : 'center',
				width : 110,
				sortable : true,
				hidden : true,
				dataIndex : 'workFlowNo'
			}, {
				header : '编制状态',
				align : 'center',
				width : 110,
				sortable : true,
				//hidden : true,
				dataIndex : 'makeStatus',
				editor : new Ext.form.TextField(),
				renderer:function(value)
				{
					if(value=="0") return "未上报";
					else if(value=="1") return "编制审批中";
					else if(value=="2") return "编制审批通过";
					else if(value=="3") return "编制审批退回";
					else return value;
				}
			},
			{
				
				header : '来源',
				align : 'center',
				width : 110,
				sortable : true,
				dataIndex : 'dataSource',
				renderer:function(value)
				{
					if(value=="1") return "编制录入";
					if(value=="2") return "编制计算";
				}
			}
			]);


	// 顶部工具栏
	var tbar = new Ext.Toolbar({
		items : [ save]
	});
	// 可编辑的表格
	var Grid = new Ext.grid.EditorGridPanel({
		sm : sm,
		ds : store,
		cm : sm_store_item,
	//	title : '编制科目',
		autoScroll : true,
		tbar : tbar,
		border : true,
			listeners : {
				'beforeedit' : function(e) { 
					if (e.field == "adviceBudget")
						{ var column = e.record.get('dataSource'); 
						  if(column=="2")
						  {
						  	return false;
						  }
						}
				}
					
				},
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
	
	queryData();
})
