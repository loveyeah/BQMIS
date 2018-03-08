Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var wcode=document.getElementById("workCode").value;
	function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";
		Year = DateIn.getYear();
		Month = DateIn.getMonth() + 1;
		Day = DateIn.getDate();
		CurrentDate = Year + "-";
		if (Month >= 10) {
			CurrentDate = CurrentDate + Month + "-";
		} else {
			CurrentDate = CurrentDate + "0" + Month + "-";
		}
		if (Day >= 10) {
			CurrentDate = CurrentDate + Day;
		} else {
			CurrentDate = CurrentDate + "0" + Day;
		}
		return CurrentDate;
	}
	var enddate = new Date();
	var startdate = enddate.add(Date.MONTH, -1);
	startdate = startdate.getFirstDateOfMonth();
	var sdate = ChangeDateToString(startdate);
	var edate = ChangeDateToString(enddate);
	function getChooseQueryType() {
		var list = document.getElementsByName("queryTypeRadio");
		for (var i = 0; i < list.length; i++) {
			if (list[i].checked) {
				return list[i].id;
			}
		}
	}
	function query() {
		var ftime = Ext.get('timefromDate').dom.value;
		var ttime = Ext.get('timetoDate').dom.value;
		if (ftime > ttime) {
			Ext.Msg.alert('提示', '选择后一日期应比前一日期大！');
			return false;
		}
		find_ds.load({
			params : {
				startDate : ftime,
				endDate : ttime,
				start : 0,
				limit : 18,
				workercode : wcode
			}
		});
		el_ds.load({
			params : {
				startDate : ftime,
				endDate : ttime,
				start : 0,
				limit : 18,
				workercode : wcode
			}
		});

	}
	var timefromDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : '_timefromDate',
		id : 'timefromDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		checked : true,
		allowBlank : false,
		readOnly : true,
		value : sdate,
		emptyText : '请选择',
		anchor : '70%'
	});
	var timetoDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : '_timetoDate',
		value : edate,
		id : 'timetoDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		allowBlank : false,
		readOnly : true,
		emptyText : '请选择',
		anchor : '70%'
	});

	var peopleFindRadio = new Ext.form.Radio({
		id : 'peopleFindRadio',
		// boxLabel : '个人发现',
		hideLabel : true,
		name : 'queryTypeRadio',
		checked : true
	});
	var peopleEliminateRadio = new Ext.form.Radio({
		id : 'peopleEliminateRadio',
		// boxLabel : '个人消缺',
		hideLabel : true,
		name : 'queryTypeRadio',
		listeners : {
			check : function() {
				var queryType = getChooseQueryType();
				switch (queryType) {
					case 'peopleFindRadio' : {
						el_Grid.hide();
						Grid.show();
						break;
					}
					case 'peopleEliminateRadio' : {
						var hh = Ext.get('Grid').getHeight();
						Grid.hide();
						el_Grid.show();
						el_Grid.setHeight(hh);
						break;
					}
				}
			}
		}
	});
	var operateBy = new Ext.form.ComboBox({
		fieldLabel : '人员',
		name : 'operateBy',
		id : 'operateBy',
		mode : 'remote',
		hiddenName : 'power.operateBy',
		editable : false,
		triggerAction : 'all',
		emptyText : '选人文本框',
		onTriggerClick : function() {
			var url = "/power/comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				rootNode : {
					id : '-1',
					text : '灞桥电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:600px;dialogHeight:420px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (typeof(emp) != "undefined") {
				Ext.getCmp('operateBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('operateBy'), emp.workerName);
				wcode=emp.workerCode;
				
			}
		}
	});
	var btnQuery = new Ext.Button({
		id : 'btnQuery',
		text : '查 询',
		iconCls : 'query',
		handler : query
	});
	var tbar = new Ext.Toolbar({
		items : ['查询时间段：', timefromDate, '  ', '至 ', timetoDate, '-', '查询类型:',
				peopleFindRadio, '个人发现', peopleEliminateRadio, '个人消缺', '-',
				operateBy, '-', btnQuery]
	})
	/*---------------------------------------------------------------------------------------------------------------------------------------*/
	var find_item = Ext.data.Record.create([{
		name : 'id'
	}, {
		name : 'failureCode'
	}, {
		name : 'findByName'
	}, {
		name : 'woStatusName'
	}, {
		name : 'failureContent'
	}, {
		name : 'equName'
	}, {
		name : 'bugName'
	}, {
		name : 'findDate'
	}, {
		name : 'repairDepName'
	}, {
		name : 'findDeptName'
	}, {
		name : 'id'
	}, {
		name : 'workFlowNo'
	}]);
	var find_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});
	var find_item_cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
		header : '序号',
		width : 50,
		align : 'center'
	}), {
		header : '缺陷编号',
		dataIndex : 'failureCode',
		align : 'center'
	}, {
		header : '发现人',
		dataIndex : 'findByName',
		width : 50,
		align : 'center'
	}, {
		header : '发现班组',
		dataIndex : 'findDeptName',
		align : 'center'
	}, {
		header : '当前状态',
		dataIndex : 'woStatusName',
		align : 'center'
	}, {
		header : '缺陷内容',
		dataIndex : 'failureContent',
		width : 150,
		align : 'left',
		renderer : function change(val) {
			return ' <span style="white-space:normal;">' + val + ' </span>';
		}
	}, {
		header : '设备名称',
		dataIndex : 'equName',
		width : 120,
		align : 'center'
	}, {
		header : '故障名称',
		dataIndex : 'bugName',
		align : 'center'
	}, {
		header : '发现时间',
		width : 120,
		dataIndex : 'findDate',
		align : 'center'
	}, {
		header : '检修部门',
		dataIndex : 'repairDepName',
		align : 'center'
	}]);
	find_item_cm.defaultSortable = true;
	var find_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'bqfailure/failureFindQuery.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : 'list'
		}, find_item)
	});
	find_ds.load({
		params : {
			startDate : sdate,
			endDate : edate,
			start : 0,
			limit : 18,
			workercode : document.getElementById("workCode").value
		}
	});
	var gridbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : find_ds,
		displayInfo : true,
		displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
		emptyMsg : "没有记录",
		beforePageText : '页',
		afterPageText : "共{0}"
	});
	var Grid = new Ext.grid.GridPanel({
		id : 'Grid',
		ds : find_ds,
		cm : find_item_cm,
		split : true,
		autoScroll : true,
		// tbar : tbar,
		bbar : gridbbar,
		border : false,
		viewConfig : {
			forceFit : false
		}
	});
	Grid.on("rowcontextmenu", function(g, i, e) {
		e.stopEvent();
		var record = Grid.getStore().getAt(i);
		// 右键菜单
		var menu = new Ext.menu.Menu({
			id : 'mainMenu',
			items : [new Ext.menu.Item({
				text : '详细信息',
				handler : function() {
					Ext.Ajax.request({
						url : 'bqfailure/findFailureById.action?failure.id='
								+ record.get("id"),
						method : 'post',
						waitMsg : '正在处理数据...',
						success : function(result, request) {
							var json = eval('(' + result.responseText + ')');
							window
									.showModalDialog(
											"/power/equ/bqfailure/query/detailFailure.jsp",
											json,
											'dialogWidth=860px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');
						},
						failure : function(result, request) {
							Ext.Msg.alert('提示', '操作失败，请联系管理员！');
						}
					});
				}
			}), new Ext.menu.Item({
				text : '图形展示',
				handler : function() {
					if (record.get("workFlowNo") != "" && record.get("workFlowNo") != null) {
						window.open("/power/workflow/manager/show/show.jsp?entryId="
										+ record.get("workFlowNo"));
					}
				}
			})]
		});
		var coords = e.getXY();
		menu.showAt([coords[0], coords[1]]);
	});
	var el_item = Ext.data.Record.create([{
		name : 'failureCode'
	}, {
		name : 'approvePeopleName'
	}, {
		name : 'approveTime'
	}, {
		name : 'eliminateClassName'
	}, {
		name : 'elininateTypeName'
	}, {
		name : 'woStatusName'
	}, {
		name : 'failureContent'
	}, {
		name : 'equName'
	}, {
		name : 'bugName'
	}, {
		name : 'findDate'
	}, {
		name : 'repairDepName'
	}, {
		name : 'id'
	}, {
		name : 'workFlowNo'
	}]);
	var el_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});
	var el_item_cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
		header : '序号',
		width : 50,
		align : 'center'
	}), {
		header : '缺陷编号',
		dataIndex : 'failureCode',
		align : 'center'
	}, {
		header : '消缺人',
		dataIndex : 'approvePeopleName',
		width : 50,
		align : 'center'
	}, {
		header : '消缺时间',
		dataIndex : 'approveTime',
		width : 120,
		align : 'center'
	}, {
		header : '消缺班组',
		dataIndex : 'eliminateClassName',
		width : 120,
		align : 'center'
	}, {
		header : '消缺方式',
		dataIndex : 'elininateTypeName',
		align : 'center'
	}, {
		header : '当前状态',
		dataIndex : 'woStatusName',
		align : 'center'
	}, {
		header : '缺陷内容',
		dataIndex : 'failureContent',
		width : 150,
		align : 'left',
		renderer : function change(val) {
			return ' <span style="white-space:normal;">' + val + ' </span>';
		}
	}, {
		header : '设备名称',
		dataIndex : 'equName',
		width : 120,
		align : 'center'
	}, {
		header : '故障名称',
		dataIndex : 'bugName',
		align : 'center'
	}, {
		header : '发现时间',
		width : 120,
		dataIndex : 'findDate',
		align : 'center'
	}, {
		header : '检修部门',
		width : 120,
		dataIndex : 'repairDepName',
		align : 'center'
	}]);
	el_item_cm.defaultSortable = true;
	var el_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'bqfailure/failureEliminateQuery.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : 'list'
		}, el_item)
	});
	el_ds.load({
		params : {
			startDate : sdate,
			endDate : edate,
			start : 0,
			limit : 18,
			workercode : document.getElementById("workCode").value
		}
	});
	var elbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : el_ds,
		displayInfo : true,
		displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
		emptyMsg : "没有记录",
		beforePageText : '页',
		afterPageText : "共{0}"
	});
	var el_Grid = new Ext.grid.GridPanel({
		ds : el_ds,
		cm : el_item_cm,
		split : true,
		autoScroll : true,
		bbar : elbbar,
		border : false,
		viewConfig : {
			forceFit : false
		}
	});
el_Grid.on("rowcontextmenu", function(g, i, e) {
		e.stopEvent();
		var record = el_Grid.getStore().getAt(i);
		// 右键菜单
		var menu = new Ext.menu.Menu({
			id : 'mainMenu',
			items : [new Ext.menu.Item({
				text : '详细信息',
				handler : function() {
					Ext.Ajax.request({
						url : 'bqfailure/findFailureById.action?failure.id='
								+ record.get("id"),
						method : 'post',
						waitMsg : '正在处理数据...',
						success : function(result, request) {
							var json = eval('(' + result.responseText + ')');
							window
									.showModalDialog(
											"/power/equ/bqfailure/query/detailFailure.jsp",
											json,
											'dialogWidth=860px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');
						},
						failure : function(result, request) {
							Ext.Msg.alert('提示', '操作失败，请联系管理员！');
						}
					});
				}
			}), new Ext.menu.Item({
				text : '图形展示',
				handler : function() {
					if (record.get("workFlowNo") != "" && record.get("workFlowNo") != null) {
						window.open("/power/workflow/manager/show/show.jsp?entryId="
										+ record.get("workFlowNo"));
					}
				}
			})]
		});
		var coords = e.getXY();
		menu.showAt([coords[0], coords[1]]);
	});
	var panel = new Ext.Panel({
		id : 'panel',
		layout : 'fit',
		border : false,
		autoScroll : true,
		tbar : tbar,
		items : [Grid, el_Grid]
	});
	el_Grid.hide();
	new Ext.Viewport({
		enableTabScroll : true,
		layout : 'fit',
		border : false,
		collapsible : true,
		autoScroll : true,
		tbar : tbar,
		split : true,
		margins : '0 0 0 0',
		items : [panel]
	});

})
