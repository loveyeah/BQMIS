Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var item = Ext.data.Record.create([{
		name : 'count'
	}, {
		name : 'writeBy'
	}, {
		name : 'writeByName'
	}, {
		name : 'writeDeptName'
	}]);
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});
	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
		header : '序号',
		width : 35,
		align : 'center'
	}), {
		header : '登记人',
		dataIndex : 'writeByName',
		width : 100,
		align : 'center'
	}, {
		header : '部门',
		dataIndex : 'writeDeptName',
		width : 120,
		align : 'center'
	}, {
		header : '数量',
		dataIndex : 'count',
		width : 50,
		align : 'center'
	}]);
	cm.defaultSortable = true;
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'bqfailure/noKKSfailureQuery.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : 'list'
		}, item)
	});
	ds.load({
		params : {
			start : 0,
			limit : 18
		}
	});
	var Grid = new Ext.grid.GridPanel({
		ds : ds,
		cm : cm,
		title : "无KKS编码缺陷统计(双击查看详细)",
		sm : sm,
		split : true,
		autoScroll : true,
		bbar : gridbbar,
		border : false,
		viewConfig : {
			forceFit : false
		}
	});
	Grid.on('rowdblclick', function(grid, rowIndex, e) {
		var rec = grid.getSelectionModel().getSelected();
		if (rec) {
			var workercode = rec.get('writeBy');
			if (workercode != null && workercode != "") {
				searchds.load({
					params : {
						start : 0,
						limit : 18,
						workercode : workercode
					}
				});
				win.setTitle("<" + rec.get('writeByName')
						+ ">登记的无KKS编码缺陷列表(右键查看详细)")
				win.show();
			}
		}
	});

	var gridbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : ds,
		displayInfo : true,
		displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
		emptyMsg : "没有记录",
		beforePageText : '页',
		afterPageText : "共{0}"
	});
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [Grid]
	});
	var searchFail = new Ext.data.Record.create([{
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
	}]);
	var searchds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'bqfailure/noKKSfailureQueryByWorker.action',
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : 'list'
		}, searchFail)
	});
	/* 设置每一行的选择框 */
	var searchsm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			rowselect : function(sm, row, rec) {

			}
		}
	});

	var searchcm = new Ext.grid.ColumnModel([searchsm,
			new Ext.grid.RowNumberer({
				header : '序号',
				width : 35
			}), {
				header : "id",
				dataIndex : "id",
				hidden : true
			}, {
				header : '缺陷编码',
				dataIndex : 'failureCode',
				width : 100
			}, {
				header : '所属系统',
				dataIndex : 'belongSystemName',
				width : 100
			}, {
				header : '缺陷内容',
				dataIndex : 'failureContent',
				width : 300,
				renderer : function change(val) {
					return ' <span style="white-space:normal;">' + val
							+ ' </span>';
				}
			}, {
				header : '管辖专业',
				dataIndex : 'dominationProfessionName',
				width : 60
			}, {
				header : '检修部门',
				dataIndex : 'repairDepName',
				width : 120
			}, {
				header : '状态',
				dataIndex : 'woStatusName',
				width : 120
			}, {
				header : '发现时间',
				dataIndex : 'findDate',
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
				header : '填写时间',
				dataIndex : 'writeDate',
				width : 120
			}]);
	// cm.defaultSortable = true;
	/* 设置分页的工具条 */
	var bbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : searchds,
		displayInfo : true,
		displayMsg : '显示第 {0} 条到 {1} 条记录，一共 {2} 条',
		emptyMsg : "没有记录"
	})
	var searchgrid = new Ext.grid.GridPanel({
		id : 'gridid',
		width : 630,
		height : 415,
		ds : searchds,
		cm : searchcm,
		autoScroll : true,
		loadMask : {
			msg : '读取数据中 ...'
		},
		sm : searchsm,
		bbar : bbar,
		border : false
	});
	searchgrid.on("rowcontextmenu", function(g, i, e) {
		e.stopEvent();
		var record = searchgrid.getStore().getAt(i);
		// 右键菜单
		var menu = new Ext.menu.Menu({
			id : 'mainMenu',
			items : [new Ext.menu.Item({
				text : '详细信息',
				iconCls : 'list',
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
				iconCls : 'view',
				handler : function() {
					if (record.get("workFlowNo") != ""
							&& record.get("workFlowNo") != null) {
						window
								.open("/power/workflow/manager/show/show.jsp?entryId="
										+ record.get("workFlowNo"));
					} else {
						var flowCode = "";
						if (record.get("belongSystem") == '5'
								|| record.get("belongSystem") == 'Y') {
							flowCode = "failuren";
						} else {
							flowCode = "failureo";
						}
						url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
								+ flowCode;
						window.open(url);
					}
				}
			})]
		});
		var coords = e.getXY();
		menu.showAt([coords[0], coords[1]]);
	});
	var win = new Ext.Window({
		autoScroll : true,
		width : 650,
		height : 450,
		closeAction : 'hide',
		items : [searchgrid]
	});
})