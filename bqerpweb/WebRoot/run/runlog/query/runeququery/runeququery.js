Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var specialcode = parent.document.getElementById("specialityCode").value;
	var fromdate = parent.document.getElementById("fromDate").value;
	var todate = parent.document.getElementById("toDate").value;
	var waitequStatus = new Ext.data.Record.create([{
		name : 'runKeyId'
	}, {
		name : 'runWayName'
	}]);
	var wait_equ_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			rowselect : function(sm, index, rec) {
				var rec = waitEquGrid.getStore().getAt(index);
				if (typeof(rec) != "undefined") {
					// right_ds.load({
					// params : {
					// specialcode : specialcode,
					// formdate : fromdate,
					// todate : todate,
					// runkeyid : rec.get("runKeyId")
					// }
					// });
					right_ds.on('beforeload', function() {
						Ext.apply(this.baseParams, {
							method : "all",
							specialcode : specialcode,
							formdate : fromdate,
							todate : todate,
							runkeyid : rec.get("runKeyId")
						});
					});
					right_ds.load({
						params : {
							start : 0,
							limit : 18
						}
					});
				} else {
					alert('请选择要操作的信息!');
				}
			}
		}
	});
	var wait_equ_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/findRunWayByProfession.action'
			//url:'runlog/findRunWay.action'
		}),
		method : 'post',
		reader : new Ext.data.JsonReader({
			root : 'data'
		}, waitequStatus)
	});
	wait_equ_ds.load({
		params : {
			specialcode : specialcode
		}
	});
	var wait_equ_cm = new Ext.grid.ColumnModel([{
		header : 'ID',
		dataIndex : 'runKeyId',
		hidden : true
	}, {
		header : '运行方式名称',
		dataIndex : 'runWayName',
		align : 'center'
	}]);
	wait_equ_cm.defaultSortable = true;
	var waitEquGrid = new Ext.grid.GridPanel({
		ds : wait_equ_ds,
		cm : wait_equ_cm,
		sm : wait_equ_sm,
		fitToFrame : true,
		border : true,
		viewConfig : {
			forceFit : true
		}
	});

	var right_item = Ext.data.Record.create([{
		name : 'shiftEqustatusId'
	}, {
		name : 'runLogid'
	}, {
		name : 'runLogno'
	}, {
		name : 'specialityCode'
	}, {
		name : 'specialityName'
	}, {
		name : 'attributeCode'
	}, {
		name : 'equName'
	}, {
		name : 'equStatusId'
	}, {
		name : 'equStatusName'
	}, {
		name : 'colorValue'
	}]);
	var bbar = new Ext.Toolbar({
		items : [{
			id : 'btnearth',
			text : "接地线/闸刀查询",
			iconCls : 'query',
			handler : function() {
				addwin.show();
			}
		}]
	})
	var right_ds = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/queryEquStatusList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : 'root'
		}, right_item),
		groupField : 'name',
		groupOnSort : true,
		sortInfo : {
			field : 'runLogno',
			direction : "DESC"
		}
	});
	var right_item_cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
		header : '日志号',
		dataIndex : 'runLogno',
		align : 'center'
	}, {
		header : '专业',
		dataIndex : 'specialityName',
		align : 'center'
	}, {
		header : '设备名称',
		dataIndex : 'equName',
		align : 'center'
	}, {
		header : '状态',
		dataIndex : 'equStatusName',
		align : 'center'
	}, {
		header : '状态颜色',
		dataIndex : 'colorValue',
		align : 'center',
		renderer : showColor
	}]);
	function showColor(v) {
		return "<div  style='width:40; background:" + v
				+ "'>&nbsp;&nbsp;&nbsp;&nbsp;</div>";
	}
	var rightbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : right_ds,
		displayInfo : true,
		displayMsg : "共 {2} 条",
		emptyMsg : "没有记录",
		beforePageText : '页',
		afterPageText : "共{0}"
	});
	var rightGrid = new Ext.grid.GridPanel({
		id : 'right-grid',
		ds : right_ds,
		cm : right_item_cm,
		bbar:rightbbar,
		tbar : bbar,
		fitToFrame : true,
		autoScroll : true,
		view : new Ext.grid.GroupingView({
			forceFit : true,
			sortAscText : '正序',
			sortDescText : '倒序',
			columnsText : '列显示/隐藏',
			groupByText : '依本列分组',
			showGroupsText : '分组显示',
			groupTextTpl : '{text}'
		// groupTextTpl : '{text} ({[values.rs.length]} 条记录)'
		}),
		border : true,
		viewConfig : {
			forceFit : true
		}
	});
	var earth_item = Ext.data.Record.create([{
		name : 'earthRecordId'
	}, {
		name : 'earthName'
	}, {
		name : 'earthName'
	}, {
		name : 'installManName'
	}, {
		name : 'installTime'
	}, {
		name : 'installPlace'
	}, {
		name : 'backoutManName'
	}, {
		name : 'backoutTime'
	}, {
		name : 'specialityName'
	}]);
	var unitStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/findUintProfessionList.action'
		}),
		reader : new Ext.data.JsonReader({
			id : "unit"
		}, [{
			name : 'specialityCode'
		}, {
			name : 'specialityName'
		}])
	});
	unitStore.load();
	unitStore.on("load", function(xx, records, o) {
		unitBox.setValue(records[0].data.specialityCode);
		earth_ds.on('beforeload', function() {
			Ext.apply(this.baseParams, {
				specialcode : records[0].data.specialityCode
			});
		});
		earth_ds.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	});

	var unitBox = new Ext.form.ComboBox({
		fieldLabel : '所属专业',
		id : 'unitBox',
		store : unitStore,
		valueField : "specialityCode",
		displayField : "specialityName",
		mode : 'local',
		triggerAction : 'all',
		forceSelection : true,
		hiddenName : 'specialityCode',
		editable : false,
		allowBlank : false,
		selectOnFocus : true,
		name : 'specialityCode',
		width : 150
	});
	var tbar = new Ext.Toolbar({
		items : ['专业：', unitBox, '-', {
			id : 'btnrecordquery',
			text : "查询",
			iconCls : 'query',
			handler : function() {
				earth_ds.on('beforeload', function() {
					Ext.apply(this.baseParams, {
						specialcode : Ext.get('specialityCode').dom.value
					});
				});
				earth_ds.load({
					params : {
						start : 0,
						limit : 18
					}
				});
			}
		}]
	})
	var earth_ds = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/queryEarthRecordList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : 'root'
		}, earth_item)
	});
	var earht_item_cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
		header : '专业',
		dataIndex : 'specialityName',
		align : 'center',
		width : 50
	}, {
		header : '地线名称',
		dataIndex : 'earthName',
		align : 'center',
		width : 80
	}, {
		header : '装设人',
		dataIndex : 'installManName',
		align : 'center',
		width : 50
	}, {
		header : '装设时间',
		dataIndex : 'installTime',
		align : 'center',
		width : 120
	}, {
		header : '安装位置',
		dataIndex : 'installPlace',
		align : 'center',
		width : 150
	}, {
		header : '拆除人',
		dataIndex : 'backoutManName',
		align : 'center',
		width : 50
	}, {
		header : '拆除时间',
		dataIndex : 'backoutTime',
		align : 'center',
		width : 120
	}]);
	var earthbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : earth_ds,
		displayInfo : true,
		displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
		emptyMsg : "没有记录",
		beforePageText : '页',
		afterPageText : "共{0}"
	});
	var earthGrid = new Ext.grid.GridPanel({
		id : 'earth-grid',
		ds : earth_ds,
		cm : earht_item_cm,
		width : 700,
		height : 400,
		bbar : earthbbar,
		tbar : tbar,
		fitToFrame : false,
		autoScroll : true,
		border : true
			// viewConfig : {
			// forceFit : true
			// }
	});
	var addwin = new Ext.Window({
		title : '接地线/闸刀登记记录',
		el : 'win',
		autoHeight : true,
		width : 720,
		modal : true,
		closeAction : 'hide',
		items : [earthGrid]
	})
	var layout = new Ext.Viewport({
		layout : "border",
		border : false,
		items : [{
			region : "west",
			layout : 'fit',
			width : '250',
			border : false,
			collapsible : true,
			split : true,
			margins : '0 0 0 0',
			// 注入表格
			items : [waitEquGrid]
		}, {
			region : "center",
			layout : 'fit',
			width : '500',
			border : false,
			collapsible : true,
			split : true,
			margins : '0 0 0 0',
			// 注入表格
			items : [rightGrid]
		}]
	})
	setTimeout(function() {
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut({
			remove : true
		});
	}, 250);
})