Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	var testCycle = 0;
	// 仪器名称

	var names = new Ext.form.TriggerField({

		name : "names",
		fieldLabel : "名称",
		triggerAction : 'all',

		emptyText : '请选择...',

		readOnly : true,
		//				allowBlank : false,
		anchor : "95%"

	})
	var regulatorId = new Ext.form.Hidden({
		name : 'model.regulatorId'

	});

	// 计划检验时间
	var planCheckDate = new Ext.form.TextField({
		fieldLabel : '计划检验时间',
		name : 'model.planCheckDate',
		readOnly : true,
		anchor : '45%'

	})

	// 检验时间
	var checkDate = new Ext.form.TextField({
		format : 'Y-m-d',
		fieldLabel : '检验时间',
		readOnly : true,
		name : 'model.checkDate',
		anchor : '90%'

	})
	// 下次检验时间
	var nextCheckDate = new Ext.form.TextField({
		fieldLabel : '下次检验时间',
		name : 'model.nextCheckDate',
		readOnly : true,
		anchor : '90%'

	})
	// 检验温度
	var checkTemperature = new Ext.form.NumberField({
		fieldLabel : '检验温度',
		name : 'model.checkTemperature',
		readOnly : true,
		anchor : '90%'

	})
	// 检验湿度
	var checkWet = new Ext.form.NumberField({
		fieldLabel : '检验温度',
		name : 'model.checkWet',
		readOnly : true,
		anchor : '90%'

	})

	// 检验结果
//	var rusult = new Ext.form.TextField({
//		fieldLabel : '检验结果',
//		name : 'model.rusult',
//		readOnly : true,
//		anchor : '95%'
//
//	})
	
		var rusult = new Ext.form.ComboBox({
		fieldLabel : '检验结果',
		store : new Ext.data.SimpleStore({
			fields : ['value', 'text'],
			data : [['Y', '合格'], ['N', '不合格']]
		}),
		id : 'rusult',
		name : 'rusult',
		valueField : "value",
		displayField : "text",
		value : 'Y',
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'model.rusult',
		editable : true,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		anchor : '95%'
	});
	rusult.on("beforequery",function(){
	return false;
	})

	// 备注
	var memo = new Ext.form.TextArea({
		fieldLabel : "备注",
		name : 'model.memo',
		readOnly : true,
		anchor : '95%'

	})

	// 检验人名称
	var checkManName = new Ext.form.TextField({
		fieldLabel : "送验人",
		name : 'checkManName',
		//emptyText : '请选择...',
		readOnly : true,
		anchor : '90%'
	})

	// 检验人编码
	var checkMan = new Ext.form.Hidden({
		name : 'model.checkMan'

	})
		// 检验单位
	var checkDepCode = new Ext.form.TextField({
		id : 'checkDepCode',
		fieldLabel : "检验单位",
		name : 'model.checkDepCode',
		readOnly : true,
		anchor : '95%'
	})

	// 校核人名称
	var testManName = new Ext.form.TextField({
		fieldLabel : "负责人",
		name : 'testManName',
		//emptyText : '请选择...',
		readOnly : true,
		anchor : '90%'
	})
	// 校核人编码
	var testMan = new Ext.form.Hidden({
		name : 'model.testMan'

	})

	// 监督专业
	var jdzyId = new Ext.form.Hidden({
		name : 'model.jdzyId',
		value : JDZY_ID

	})
	// 检验记录id
	var jyjlId = new Ext.form.Hidden({
		name : 'model.jyjlId'

	})

	// 弹窗的表单对象
	var blockForm = new Ext.form.FormPanel({
		labelAlign : 'right',

		labelWidth : 90,
		layout : 'column',
		border : false,
		frame : false,
		items : [{

			bodyStyle : "padding:10px 0px 0px 0px",
			border : false,
			layout : 'form',
			columnWidth : 1,
			items : [names, regulatorId]
		}, {
			columnWidth : 1,
			layout : 'form',
			border : false,
			items : [planCheckDate]
		}, {
			columnWidth : .5,
			layout : 'form',
			border : false,
			items : [checkDate]
		}, {
			columnWidth : .5,
			layout : 'form',
			border : false,
			items : [nextCheckDate]
		}, {

			border : false,
			hidden : true,
			layout : 'form',
			columnWidth : .5,
			items : [checkTemperature]
		}, {

			border : false,
			hidden : true,
			layout : 'form',
			columnWidth : .5,
			items : [checkWet]
		}, {
			columnWidth : 1,
			layout : 'form',
			border : false,
			items : [rusult]
		}, {
			columnWidth : 1,
			layout : 'form',
			border : false,
			items : [memo]
		}, {
			columnWidth : .5,
			layout : 'form',
			border : false,
			items : [checkManName, checkMan]
		}, {
			columnWidth : .5,
			layout : 'form',
			border : false,
			items : [testManName, testMan]
		}, {
			columnWidth : 1,
			layout : 'form',
			border : false,
			items : [checkDepCode]
		}, {
			columnWidth : .5,
			layout : 'form',
			border : false,
			items : [jdzyId, jyjlId]
		}]

	});

	// // 左边列表中的数据
	var paramslist = new Ext.data.Record.create([

	{
		name : 'jycszId'
	}, {
		name : 'jyjlId'
	}, {
		name : 'parameterId'
	}, {
		name : 'parameterValue'
	}, {
		name : 'jdzyId'
	}, {
		name : 'parameterName'
	}]);

	var paramsGrids = new Ext.data.JsonStore({
		url : 'productionrec/findCSZ.action',
		root : 'list',
		totalCount : 'totalCount',
		fields : paramslist
	});

	function queryTestParams(regulatorId, jyjlId) {

		paramsGrids.load({
			params : {

				regulatorId : regulatorId,
				jyjlId : jyjlId
			}
		});
	}

	// 左边列表
	var paramsGrid = new Ext.grid.GridPanel({
		ds : paramsGrids,
		columns : [new Ext.grid.RowNumberer(), {
			header : "检验项目",

			// align : "center",
			sortable : true,

			dataIndex : 'parameterName'
		}, {
			header : "检验值",

			sortable : false,

			dataIndex : 'parameterValue'
		}],

		viewConfig : {
			forceFit : true
		},
		frame : false,
		clicksToEdit : 2,
		border : true
	});
	// 左边的弹出窗体

	var blockAddWindow = new Ext.Window({
		// el : 'window_win',
		title : '',
		layout : 'border',
		width : 550,
		height : 400,

		modal : true,
		closable : true,
		border : false,
		resizable : false,
		closeAction : 'hide',
		plain : true,
		// 面板中按钮的排列方式
		buttonAlign : 'center',
		items : [{
			region : 'center',
			layout : 'form',
			border : false,
			frame : false,
			items : [blockForm]

		}, {
			region : 'south',
			layout : 'fit',
			height : 130,
			border : false,
			frame : false,
			autoScroll : true,
			items : [paramsGrid]

		}]
	});

	// 修改按钮
	var westbtnedit = new Ext.Button({
		id : 'btnUpdate',
		text : '查看',
		iconCls : 'list',
		handler : function() {
			if (westgrid.selModel.hasSelection()) {

				var records = westgrid.getSelectionModel().getSelections();
				var recordslen = records.length;
				if (recordslen > 1) {
					Ext.Msg.alert("系统提示信息", "请选择其中一项进行查看！");
				} else {
					blockAddWindow.show();
					var record = westgrid.getSelectionModel().getSelected();
					blockForm.getForm().reset();
					blockForm.form.loadRecord(record);
					if (record.get("model.planCheckDate") != null)
						planCheckDate.setValue(record
								.get("model.planCheckDate").substring(0, 10));
					if (record.get("model.nextCheckDate") != null)
						nextCheckDate.setValue(record
								.get("model.nextCheckDate").substring(0, 10));
					if (record.get("model.checkDate") != null)
						checkDate.setValue(record.get("model.checkDate")
								.substring(0, 10));

					queryTestParams(record.get("model.regulatorId"), record
							.get("model.jyjlId"));
					op = 'edit';
					blockAddWindow.setTitle("仪器仪表检验记录查看");
				}
			} else {
				Ext.Msg.alert("提示", "请先选择要查看的行!");
			}
		}
	});

	var westsm = new Ext.grid.CheckboxSelectionModel();
	// 左边列表中的数据
	var datalist = new Ext.data.Record.create([

	{
		name : 'model.regulatorId'
	}, {
		name : 'model.jyjlId'
	}, {
		name : 'model.planCheckDate'
	}, {
		name : 'model.checkDate'
	}, {
		name : 'model.nextCheckDate'
	}, {
		name : 'model.checkTemperature'
	}, {
		name : 'model.checkWet'
	}, {
		name : 'model.rusult'
	}, {
		name : 'model.checkMan'
	}, {
		name : 'model.checkDepCode'
	}, {
		name : 'model.testMan'
	}, {
		name : 'model.memo'
	}, {
		name : 'model.jdzyId'

	}, {
		name : 'checkManName'

	}, {
		name : 'checkDepName'
	}, {
		name : 'testManName'
	}, {
		name : 'names'
	}]);

	var westgrids = new Ext.data.JsonStore({
		url : 'productionrec/findJLList.action',
		root : 'list',
		totalCount : 'totalCount',
		fields : datalist
	});

	var query = new Ext.form.TextField({
		id : 'argFuzzy',
		fieldLabel : "模糊查询",
		hideLabel : false,
		emptyText : '仪器名称..',
		name : 'argFuzzy',
		width : 150,
		value : ''
	});
	function fuzzyQuery() {
		westgrids.baseParams = {
			fuzzy : query.getValue(),
			jdzyId : jdzyId.getValue()
		}
		westgrids.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	};
	// 查询时Enter
	document.onkeydown = function() {
		if (event.keyCode == 13 && document.getElementById('argFuzzy')) {
			fuzzyQuery();

		}
	}
	var querybtn = new Ext.Button({
		iconCls : 'query',
		text : '查询',
		handler : function() {
			fuzzyQuery();
		}
	})
	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
		ds : westgrids,
		columns : [westsm, new Ext.grid.RowNumberer(), {
			header : "仪器仪表名称",

			// align : "center",
			sortable : true,

			dataIndex : 'names'
		}, {
			header : "检验时间",

			sortable : false,

			dataIndex : 'model.checkDate',
			renderer : function(value) {

				if (value != null && value != "") {

					var myYear = value.substring(0, 4);
					var myMonth = value.substring(5, 7);

					var myDay = value.substring(8, 10);
					return myYear + '-' + myMonth + '-' + myDay;
				} else {
					return ""
				}
			}
		}],
		tbar : [query, querybtn, westbtnedit],
		sm : westsm,
		viewConfig : {
			forceFit : true
		},
		frame : true,
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : westgrids,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		}),
		border : true,
		enableColumnHide : false,
		enableColumnMove : false,
		iconCls : 'icon-grid'
	});

	// westgrid 的事件
	westgrid.on('rowdblclick', function() {
		Ext.get("btnUpdate").dom.click();
	})
	fuzzyQuery();
	// 设定布局器及面板
	var layout = new Ext.Viewport({
		layout : "border",
		border : false,
		items : [{
			title : "",
			region : 'center',
			layout : 'fit',
			border : false,
			margins : '1 0 1 1',
			split : true,
			collapsible : true,

			items : [westgrid]

		}]
	});
});