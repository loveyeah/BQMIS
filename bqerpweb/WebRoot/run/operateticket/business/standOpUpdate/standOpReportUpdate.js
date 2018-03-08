Ext.onReady(function() {
	// 分页时每页显示记录条数
	var PAGE_SIZE = Constants.PAGE_SIZE;
	// 系统当天日期
	var startDate = new Date();
	var endDate = new Date();
	// 系统当天前30天的日期
	startDate.setDate(startDate.getDate() - 30);
	// 系统当天后30天的日期
	endDate.setDate(endDate.getDate() + 30);

	// ********** 主画面******* //
	var wd = 100;
	var code = "";
	// 操作票类型检索
	var optiketTypeCbo = new Ext.ux.ComboBoxTree({
		id : 'optiketTypeCbo',
		displayField : 'text',
		width : 300,
		resizable : true,
		valueField : 'id',
		tree : {
			xtype : 'treepanel',
			// 虚拟节点,不能显示
			rootVisible : true,
			loader : new Ext.tree.TreeLoader({
				// dataUrl : 'opticket/getTreeNode.action'
				dataUrl : 'opticket/getDetailOpTaskTree.action'
			}),
			root : new Ext.tree.AsyncTreeNode({
				id : '-1',
				text : '所有操作任务'
			})
		},
		selectNodeModel : 'all'
	});

	var createBy = new Ext.form.TriggerField({
		id : 'createBy',
		name : 'createBy',
		readOnly : true,
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
				hidecreateBy.setValue(emp.workerCode);
				Ext.get('createBy').dom.value = emp.workerName
			}
		}
	})

	var hidecreateBy = new Ext.form.Hidden({
		id : 'hidecreateBy',
		name : 'hidecreateBy'
	})
	var optaskName = new Ext.form.TextField({
		id : 'taskName',
		name : 'taskName',
		fieldLable : '操作任务名称',
		width : 300
	})

	var newOrOld = new Ext.Panel({
		hideLabel : true,
		layout : 'column',
		width : 200,
		style : {
			cursor : 'hand'
		},
		height : 20,
		items : [new Ext.form.Radio({
			columnWidth : 0.5,
			checked : true, // 设置当前为选中状态,仅且一个为选中.
			boxLabel : "新系统数据", // Radio标签
			name : "newOrOld", // 用于form提交时传送的参数名
			inputValue : "new", // 提交时传送的参数值
			listeners : {
				check : function(checkbox, checked) { // 选中时,调用的事件
					newOrOldValue = getSelectValue();
					searchOptickets();
				}
			}
		}), new Ext.form.Radio({
			columnWidth : 0.5,
			boxLabel : "老系统数据",
			name : "newOrOld",
			inputValue : "old"
		})]
	});

	// 操作票专业
	var storeOpSpecial = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'opticket/getReportOpSpecials.action'
		}),
		reader : new Ext.data.JsonReader({
			root : "list"
		}, [{
			name : 'specialityCode'
		}, {
			name : 'specialityName'
		}])
	});
	storeOpSpecial.on('load', function(e, records) {
		records[0].data.specialityCode = "";
		records[0].data.specialityName = "所有";
		opticketSpecialCbo.setValue(records[0].data.specialityCode);
	});
	storeOpSpecial.load();
	// 操作票专业组合框
	var opticketSpecialCbo = new Ext.form.ComboBox({
		id : "opticketSpecialCbo",
		resizable : true,
		store : storeOpSpecial,
		triggerAction : 'all',
		valueField : "specialityCode",
		displayField : "specialityName",
		mode : 'local',
		selectOnFocus : true,
		readOnly : true,
		listeners : {
			'select' : function() {
				searchOptickets();
			}
		}
	});

	// grid工具栏
	var gridTbar = new Ext.Toolbar({
		items : [
				// "从", planStartDate, "到", planEndDate, '-',
				'操作票类型:', optiketTypeCbo, '-', '专业:', opticketSpecialCbo, '-',
				'操作任务:', optaskName, '-', '填写人：', createBy, '-', {
					text : Constants.BTN_QUERY,
					id : 'queryBtn',
					iconCls : 'query',
					handler : searchOptickets
				}]
	});
	var tooTbar = new Ext.Toolbar({
		items : [newOrOld,
				// '-',{
				// text : '填写',
				// iconCls : 'write',
				// handler : function() {
				// var sm = rungrid.getSelectionModel();
				// var selected = sm.getSelections();
				// // if (selected.length == 0) {
				// parent.edit("");
				// // } else {
				// // opticketCode = selected[0].get('opticket.opticketCode');
				// // parent.edit(opticketCode);
				// // }
				// }
				// },
				'-', {
					text : '修改',
					iconCls : 'update',
					handler : function() {
						var sm = rungrid.getSelectionModel();
						var selected = sm.getSelections();
						if (selected.length == 0) {
							Ext.Msg.alert('提示信息', '请选择你要修改的操作票！')
						} else {
							if (code.substring(0, 5) == "125MW") {
								parent.edit(code);
							} else {
								Ext.Ajax.request({
									url : 'opticket/creatNewOptickectByNo.action',
									method : 'post',
									params : {
										optickectNo : code
									},
									success : function(result, request) {
										parent.edit(result.responseText);
										searchOptickets();
									},
									failure : function(result, request) {
//										parent.edit(code);
										Ext.Msg.alert('提示信息', '操作失败！');										
									}
								})
							}
							// parent.edit(code);
						}
					}
				}, '-', {
					text : Constants.BTN_DELETE,
					iconCls : 'delete',
					handler : deleteOpticket
				},
				// '-', {
				// text : "预览",
				// handler : viewRecord
				// },
				'-', {
					text : "操作前检查项目",
					iconCls : 'next',
					handler : checkBef,
					id : 'checkBef'
				}, '-', {
					text : "操作后完成工作",
					handler : workAft,
					iconCls : 'last',
					id : 'workAft'
				}]
	});
	var toolPanel = new Ext.Panel({
		layout : 'form',
		items : [gridTbar, tooTbar]
	});
	// 选择列
	var smCSM = new Ext.grid.CheckboxSelectionModel({
		header : "选择",
		id : "check",
		width : 35,
		singleSelect : true,
		listeners : {
			rowselect : function(sm, row, rec) {
				var currentCode = rec.get('opticket.opticketCode');
				if (rec.get("opticket.opticketType").substring(0, 2) == "00") {
					Ext.get("checkBef").dom.disabled = false;
					Ext.get("workAft").dom.disabled = false;
				} else {
					Ext.get("workAft").dom.disabled = true;
					Ext.get("checkBef").dom.disabled = true;
				}
				code = currentCode;
			},
			rowdeselect : function() {
				code = "";
			}
		}
	});
	// grid中的数据Record
	var rungridlist = new Ext.data.Record.create([{
		// 编号
		name : 'opticket.opticketCode'
	}, {
		// 操作任务
		name : 'opticket.opticketName'
	}, {
		// 操作票状态
		name : 'opticket.opticketStatus'
	}, {
		// 操作票类别
		name : 'opticket.opticketType'
	}, {
		name : 'createDate'
	}, {
		name : 'specialityName'
	}, {
		name : 'createrName'
	}]);

	// grid的store
	var queryStore = new Ext.data.JsonStore({
		url : 'opticket/getOpticketList.action?isStandar=Y',
		root : 'list',
		totalProperty : 'totalCount',
		fields : rungridlist
	});

	// 分页工具栏
	var pagebar = new Ext.PagingToolbar({
		pageSize : PAGE_SIZE,
		store : queryStore,
		displayInfo : true,
		displayMsg : Constants.DISPLAY_MSG,
		emptyMsg : Constants.EMPTY_MSG
	});

	// 页面的Grid主体
	var rungrid = new Ext.grid.GridPanel({
		store : queryStore,
		viewConfig : {
			forceFit : true
		},
		loadMask : {
			msg : '<img src="comm/images/extanim32.gif" width="32" height="32" style="margin-right: 8px;" align="absmiddle" />读取数据中 ...'
		},
		columns : [smCSM, {
			header : "编号",
			sortable : true,
			width : 100,
			dataIndex : 'opticket.opticketCode'
		}, {
			header : "操作任务",
			sortable : true,
			width : 320,
			dataIndex : 'opticket.opticketName'
		}, {
			header : "操作票类别",
			hidden : true,
			dataIndex : 'opticket.opticketType'
		}, {
			header : "接收专业",
			sortable : true,
			width : 100,
			dataIndex : 'specialityName'
		}, {
			header : "操作票状态",
			sortable : true,
			width : 100,
			dataIndex : 'opticket.opticketStatus',
			renderer : function(value) {
				return getOpStatusName(value)
			}
		}, {
			header : "创建人",
			sortable : true,
			dataIndex : 'createrName'
		}, {
			header : "创建时间",
			sortable : true,
			dataIndex : 'createDate'
		}],
		// tbar : gridTbar,toolPanel
		tbar : toolPanel,
		bbar : pagebar,
		sm : smCSM,
		frame : false,
		border : false,
		enableColumnMove : false
	});
	// rungrid.on('rowdblclick', editOpticket);
	rungrid.on('render', function() {
		var currentNode = null
		newNode = new Ext.tree.TreeNode({
			id : '-1',
			text : '灞桥电厂'
		})
		optiketTypeCbo.setValue(newNode);
		gridFresh(currentNode);
	});

	// 主框架
	new Ext.Viewport({
		layout : "border",
		items : [{
			xtype : "panel",
			region : 'center',
			layout : 'fit',
			border : false,
			items : [rungrid]
		}]
	});
	// ↑↑********** 主画面*******↑↑//

	// ↓↓*********处理***********↓↓//
	// 查询处理
	function searchOptickets() {
		var currentNode = optiketTypeCbo.getCurrentNode();
		// if (planStartDate.value
		// && planEndDate.value
		// && planStartDate.getValue().getTime() > planEndDate.getValue()
		// .getTime()) {
		// Ext.Msg.alert(Constants.SYS_REMIND_MSG, "起始时间不能大于结束时间。");
		// } else {
		gridFresh(currentNode);
		// }
	}

	// 删除处理
	function deleteOpticket() {
		// var sm = rungrid.getSelectionModel();
		// var selected = sm.getSelections();
		//
		// if (selected.length == 0) {
		// Ext.Msg.alert(Constants.SYS_REMIND_MSG,
		// Constants.SELECT_NULL_DEL_MSG);
		if (code == "") {
			Ext.Msg.alert("提示信息", "未选择记录或者该记录编号为空");
		} else {
			// var opticketCode = selected[0].get('opticket.opticketCode');

			// 弹出确认删除的信息
			Ext.Msg.confirm(Constants.BTN_DELETE, Constants.DelMsg, function(
					buttonobj) {
				if (buttonobj == "yes") {
					Ext.lib.Ajax.request('POST',
							'opticket/deleteReportOpticket.action', {
								success : function(action) {
									Ext.Msg.alert(Constants.SYS_REMIND_MSG,
											Constants.DEL_SUCCESS);
									gridReload();
								},
								failure : function() {
									Ext.Msg.alert(Constants.ERROR,
											Constants.UNKNOWN_ERR);
								}
							}, 'opticketCode=' + code);
				}
			});

		}
	}

	// // 编辑Grid
	// function editOpticket(grid, rowIndex) {
	// var opticketCode = grid.getStore().getAt(rowIndex)
	// .get('opticket.opticketCode');
	// // 编辑操作票记录
	// parent.edit(opticketCode);
	// }

	// 根据条件加载Grid
	function gridFresh(currentNode) {
		if (currentNode != null) {
			var code = currentNode.attributes.code;
		} else {
			var code = "";
		}
		queryStore.baseParams = {
			// sdate : planStartDate.value,
			// edate : planEndDate.value,
			sdate : "",
			edate : "",
			// opType : optiketTypeCbo.value,
			opType : code,
			specialCode : opticketSpecialCbo.value,
			createBy : hidecreateBy.getValue(),
			optaskName : optaskName.getValue(),
			opticketStatus : OpStatus.EngineerReady,
			newOrOld : newOrOldValue
		};
		queryStore.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}

	// 重新加载Grid中的数据
	function gridReload() {
		queryStore.reload();
	}
	// ↑↑*********处理***********↑↑//
	// 预览票面
	// function viewRecord() {
	// var sm = rungrid.getSelectionModel();
	// var selected = sm.getSelections();
	// var opticketNo = "";
	// if (selected.length < 1) {
	// // 没有选择数据时
	// Ext.Msg.alert(Constants.SYS_REMIND_MSG,
	// Constants.SELECT_NULL_VIEW_MSG);
	// } else if (selected.length > 1) {
	// // 选择多行数据时
	// Ext.Msg.alert(Constants.SYS_REMIND_MSG,
	// Constants.SELECT_COMPLEX_VIEW_MSG);
	// } else {
	// // 选择一行数据时
	// opticketNo = selected[0].get('opticketCode');
	// window.open("/powerrpt/report/webfile/opticketPrint.jsp?no="
	// + opticketNo);
	//
	// }
	//
	// }
	function checkBef() {
		// var sm = rungrid.getSelectionModel();
		// var selected = sm.getSelections();
		// if (selected.length < 1) {
		// // 没有选择数据时
		// Ext.Msg.alert(Constants.SYS_REMIND_MSG,
		// Constants.SELECT_NULL_VIEW_MSG);
		// } else if (selected.length > 1) {
		// // 选择多行数据时
		// Ext.Msg.alert(Constants.SYS_REMIND_MSG,
		// Constants.SELECT_COMPLEX_VIEW_MSG);
		if (code == "") {
			Ext.Msg.alert("提示信息", "未选择记录或者该记录编号为空");
		} else {
			// 选择一行数据时
			// opticketNo = selected[0].get('opticket.opticketCode');
			var url = "../register/content/befCheckStep.jsp?opCode=" + code;
			window
					.showModalDialog(
							url,
							null,
							'dialogWidth=700px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');
		}

	}
	function getSelectValue() {
		var ns = document.getElementsByName("newOrOld");
		for (var i = 0; i < ns.length; i++) {
			if (ns[i].checked) {
				return ns[i].value;
			}
		}
	}
	function workAft() {
		// var sm = rungrid.getSelectionModel();
		// var selected = sm.getSelections();
		// if (selected.length < 1) {
		// // 没有选择数据时
		// Ext.Msg.alert(Constants.SYS_REMIND_MSG,
		// Constants.SELECT_NULL_VIEW_MSG);
		// } else if (selected.length > 1) {
		// // 选择多行数据时
		// Ext.Msg.alert(Constants.SYS_REMIND_MSG,
		// Constants.SELECT_COMPLEX_VIEW_MSG);
		if (code == "") {
			Ext.Msg.alert("提示信息", "未选择记录或者该记录编号为空");
		} else {
			// 选择一行数据时
			var url = "../register/content/finishWork.jsp?opCode=" + code;
			window
					.showModalDialog(
							url,
							null,
							'dialogWidth=720px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');

		}
	}
	// gridReload();
	setTimeout(function() {
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut({
			remove : true
		});
	}, 250);
});