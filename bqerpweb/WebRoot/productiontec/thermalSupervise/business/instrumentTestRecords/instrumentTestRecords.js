Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	var testCycle = 0;
	// 仪器名称

	var names = new Ext.form.ComboBox({

		name : "names",
		fieldLabel : "名称",
		triggerAction : 'all',

		emptyText : '请选择...',
		editable : false,
		readOnly : true,
		allowBlank : false,
		anchor : "95%",
		onTriggerClick : function() {
			selectWin();
		}

	})
	var regulatorId = new Ext.form.Hidden({
		name : 'model.regulatorId'

	});
	function selectWin() {
		var arg = {
			jdzyId : JDZY_ID

		}
		var url = "/power/productiontec/thermalSupervise/account_comm.jsp";

		var rvo = window
				.showModalDialog(
						url,
						arg,
						'dialogWidth:500px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;')
		if (typeof(rvo) != "undefined") {
			regulatorId.setValue(rvo.regulatorId);
			names.setValue(rvo.names);
			testCycle = rvo.testCycle;

			queryTestParams(regulatorId.getValue(), null);

			getnextCheckDate();
			if (op == 'add') {

				getplanCheckDate(regulatorId.getValue());
			}

		}

	};

	// 计划检验时间
	var planCheckDate = new Ext.form.TextField({
		fieldLabel : '计划检验时间',
		name : 'model.planCheckDate',
		readOnly : true,
		anchor : '45%'

	})

	function getplanCheckDate(regulatorId) {
		Ext.Ajax.request({
			url : 'productionrec/findJLByTz.action',
			method : 'post',
			params : {
				regulatorId : regulatorId
			},
			success : function(result, request) {
				var result = eval("(" + result.responseText + ")");
				// alert(Ext.encode(result))
				if (result != null && result.nextCheckDate) {
					planCheckDate.setValue(result.nextCheckDate
							.substring(0, 10));
				} else {
					planCheckDate.setValue("");
				}
			},
			failure : function(result, request) {
				Ext.MessageBox.alert('提示信息', '未知错误！')
			}
		})
	}
	
	function getnextCheckDate() {
		if (checkDate.getValue() != "") {
			var nextCheckMonth = checkDate.getValue().getMonth()  + 1
					+ testCycle;
			var nextCheckYear = checkDate.getValue().getFullYear();
			if (nextCheckMonth <= 12) {
				nextCheckMonth = nextCheckMonth;
				nextCheckYear = nextCheckYear;
			} else if (nextCheckMonth <= 24) {
				nextCheckMonth = nextCheckMonth - 12;
				nextCheckYear += 1;
			} else if (nextCheckMonth <= 36) {
				nextCheckMonth = nextCheckMonth - 24;
				nextCheckYear += 2;
			} else if (nextCheckMonth <= 48) {
				nextCheckMonth = nextCheckMonth - 36;
				nextCheckYear += 3;
			}
//			nextCheckDate.setValue(nextCheckYear + "-" + nextCheckMonth + '-'
//					+ checkDate.getValue().getDate()); modify by drdu 091105
			// modified by liuyi 20100512 
//			Ext.get('nextCheckDate').dom.value = nextCheckYear + "-" + nextCheckMonth + '-'
//					+ checkDate.getValue().getDate();
			Ext.get('nextCheckDate').dom.value = nextCheckYear + "-" + (nextCheckMonth > 9 ? nextCheckMonth : ('0' + nextCheckMonth)) + '-'
					+ (checkDate.getValue().getDate() > 9 ? checkDate.getValue().getDate() : ('0' + checkDate.getValue().getDate()));	
		}
	}
	// 检验时间
	var checkDate = new Ext.form.DateField({
		format : 'Y-m-d',
		fieldLabel : '检验时间',
		name : 'model.checkDate',
		anchor : '90%',
		listeners : {
			change : function() {
				getnextCheckDate();
			}
		}

	})
	// 下次检验时间
	var nextCheckDate = new Ext.form.DateField({
		format : 'Y-m-d',
		id : 'nextCheckDate',
		fieldLabel : '下次检验时间',
		name : 'model.nextCheckDate',
		readOnly : true,
		anchor : '90%'

	})
	// 检验温度
	var checkTemperature = new Ext.form.NumberField({
		fieldLabel : '检验温度',
		name : 'model.checkTemperature',
		anchor : '90%'

	})
	// 检验湿度
	var checkWet = new Ext.form.NumberField({
		fieldLabel : '检验湿度',
		name : 'model.checkWet',
		anchor : '90%'

	})

	// 检验结果
	// var rusult = new Ext.form.TextField({
	// fieldLabel : '检验结果',
	// name : 'model.rusult',
	// anchor : '95%'
	//
	// })
    //add by drdu 091105
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
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		anchor : '95%'
	});

	// 备注
	var memo = new Ext.form.TextArea({
		fieldLabel : "备注",
		name : 'model.memo',
		anchor : '95%'

	})

	// 检验人名称
	var checkManName = new Ext.form.TextField({
		fieldLabel : "送验人",
		name : 'checkManName',
		emptyText : '请选择...',
		listeners : {
			focus : function() {
				var args = {
					selectModel : 'single',
					rootNode : {
						id : "0",
						text : '大唐灞桥热电厂'
					}
				}
				var url = "/power/comm/jsp/hr/workerByDept/workerByDept2.jsp";

				var rvo = window
						.showModalDialog(
								url,
								args,
								'dialogWidth:500px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;')
				if (typeof(rvo) != "undefined") {
					checkMan.setValue(rvo.workerCode);
					checkManName.setValue(rvo.workerName);
					// checkDepCode.setValue(rvo.deptCode);
					// checkDepName.setValue(rvo.deptName);
				}
				this.blur();
			}
		},
		readOnly : true,
		anchor : '90%'
	})

	// 送验人编码
	var checkMan = new Ext.form.Hidden({
		name : 'model.checkMan'
	})
	// 检验单位
	var checkDepCode = new Ext.form.TextField({
		id : 'checkDepCode',
		fieldLabel : "检验单位",
		name : 'model.checkDepCode',
		// emptyText : '请选择...',
		readOnly : false,
		anchor : '95%'
	})

	// 检验单位编码
	// var checkDepCode = new Ext.form.Hidden({
	// name : 'model.checkDepCode'
	//
	// })

	// 负责人名称
	var testManName = new Ext.form.TextField({
		fieldLabel : "负责人",
		name : 'testManName',
		emptyText : '请选择...',
		listeners : {
			focus : function() {
				var args = {
					selectModel : 'single',
					rootNode : {
						id : "0",
						text : '大唐灞桥热电厂'
					}
				}
				var url = "/power/comm/jsp/hr/workerByDept/workerByDept2.jsp";

				var rvo = window
						.showModalDialog(
								url,
								args,
								'dialogWidth:500px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;')
				if (typeof(rvo) != "undefined") {
					testMan.setValue(rvo.workerCode);
					testManName.setValue(rvo.workerName);

				}
				this.blur();
			}
		},
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
	var paramsGrid = new Ext.grid.EditorGridPanel({
		ds : paramsGrids,
		columns : [new Ext.grid.RowNumberer(), {
			header : "检验项目",

			// align : "center",
			sortable : true,

			dataIndex : 'parameterName'
		}, {
			header : "检验值",

			sortable : false,

			dataIndex : 'parameterValue',
			editor : new Ext.form.NumberField()
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
		height : 430,

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

		}],
		buttons : [{
			text : '保存',
			iconCls:'save',
			handler : function() {

				if (!blockForm.getForm().isValid()) {
					return;
				}
				paramsGrid.stopEditing();
				var modifyRec = paramsGrids.getRange(0, paramsGrids.getCount());
				if (!confirm("确定要保存修改吗?"))
					return;
				var updateData = new Array();
				for (var i = 0; i < modifyRec.length; i++) {
					updateData.push(modifyRec[i].data);
				}
				var myurl = "";
				if (op == "add") {
					myurl = "productionrec/saveCSZ.action";
				} else if (op == 'edit') {
					myurl = "productionrec/saveCSZ.action";
				} else {
					Ext.Msg.alert('错误', '出现未知错误.');
				}
				blockForm.getForm().submit({
					method : 'POST',
					url : myurl,
					params : {
						isUpdate : Ext.util.JSON.encode(updateData)
					},
					success : function(form, action) {

						fuzzyQuery();
						blockAddWindow.hide();

					},
					faliue : function() {
						Ext.Msg.alert('错误', '出现未知错误.');
					}
				});

			}
		}, {
			text : '取消',
			iconCls:'cancer',
			handler : function() {
				blockForm.getForm().reset();
				blockAddWindow.hide();
			}
		}]
	});

	// 新建按钮
	var westbtnAdd = new Ext.Button({
		text : '新增',
		iconCls : 'add',
		handler : function() {
			blockForm.getForm().reset();
			queryTestParams("11111", "11111");
			blockAddWindow.show();
			op = 'add';
			blockAddWindow.setTitle("仪器仪表信息新增");
		}
	});

	// 修改按钮
	var westbtnedit = new Ext.Button({
		id : 'btnUpdate',
		text : '修改',
		iconCls : 'update',
		handler : function() {
			if (westgrid.selModel.hasSelection()) {

				var records = westgrid.getSelectionModel().getSelections();
				var recordslen = records.length;
				if (recordslen > 1) {
					Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
				} else {
					blockAddWindow.show();
					var record = westgrid.getSelectionModel().getSelected();
					// add by liuyi 20100512 
					testCycle = record.get('testCycle') - 0;
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
					blockAddWindow.setTitle("仪器仪表信息修改");
				}
			} else {
				Ext.Msg.alert("提示", "请先选择要编辑的行!");
			}
		}
	});

	// 删除按钮
	var westbtndel = new Ext.Button({
		text : '删除',
		iconCls : 'delete',
		handler : function() {
			var records = westgrid.selModel.getSelections();
			var ids = [];
			if (records.length == 0) {
				Ext.Msg.alert("提示", "请选择要删除的记录！");
			} else {
				for (var i = 0; i < records.length; i += 1) {
					var member = records[i];
					if (member.get("model.jyjlId")) {
						ids.push(member.get("model.jyjlId"));
					} else {

						westgrids.remove(member);
					}
				}
				if (ids.length <= 0) {
					alert()
				} else {
					Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(buttonobj) {

						if (buttonobj == "yes") {
							Ext.lib.Ajax.request('POST',
									'productionrec/deleteJLAndCSZ.action', {
										success : function(action) {

											fuzzyQuery();
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
	});

	// 刷新按钮
	var westbtnref = new Ext.Button({
		text : '刷新',
		iconCls : 'reflesh',
		handler : function() {
			fuzzyQuery();
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
		name : 'model.checkDepCode'
	}, {
		name : 'testManName'
	}, {
		name : 'names'
	}
	// add by liuyi 20100512 
	,{
		name : 'testCycle'
	}
	]);

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
		tbar : [query, querybtn, westbtnAdd, {
			xtype : "tbseparator"
		}, westbtnedit, {
			xtype : "tbseparator"
		}, westbtndel, {
			xtype : "tbseparator"
		}, westbtnref],
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