Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
// 渲染时间格式
function renderDate(value) {
	return value ? value.dateFormat('Y-m-d H:i:s') : '';
}
Ext.onReady(function() {
	Ext.QuickTips.init();
	var str = "";
	Ext.form.Field.prototype.msgTarget = 'side';
	function check() {
		if (Ext.getCmp('queryModel_Sec').checked) {
			Ext.getCmp('undertakeNo2').setVisible(true);
		} else {
			Ext.getCmp('undertakeNo2').setVisible(false);
		}
	}
	var btnQuery = new Ext.Button({
		id : 'btnQuery',
		text : "查询",
		iconCls : 'query',
		handler : setNo
	})
	function setNo() {
		// var quyModel = Ext.getCmp('queryModel').getValue;
		// var no1 = Ext.getCmp('undertakeNo1').getValue;
		var quyModel = Ext.get('queryModel').dom.value;
		var no1 = Ext.get('undertakeNo1').dom.value;
		// alert("##"+quyModel+"**");
		// return
		if (no1 == "") {
			// Ext.MessageBox.alert('提示信息', '请输入编号');
			// return
			no1 = ""
		} else {
			if (quyModel == 0) {
				str = no1;

			} else {
				if (quyModel == 1) {
					str = no1;
				} else {
					var no2 = Ext.getCmp('undertakeNo2').getValue;
					if (no2 == "") {
						Ext.MessageBox.alert('提示信息', '请输入编号');
						return;
					} else {
						str = no1 + "#" + no2;
					}
				}
			}
		}
		query(str);

	}
	function query(paramData) {
		Ext.Ajax.request({
			url : 'managecontract/queryArch.action',
			method : 'post',
			params : {
				no : paramData
			},
			success : function(result, request) {
				var o = eval('(' + result.responseText + ')');
				archiveDs.loadData(o);
			},
			failure : function(result, request) {
			}
		})
	}
	var btnPrint = new Ext.Button({
		id : 'btnPrint',
		text : "打印",
		iconCls : 'print',
		handler : function() {
		}
	})
	var btnClose = new Ext.Button({
		id : 'btnClose',
		text : "关闭",
		iconCls : 'close',
		handler : function() {
			window.close();
		}
	})
	var btnAch = new Ext.Button({
		id : 'btnAch',
		text : "卷内目录",
		// iconCls : 'ach',
//		handler : function() {
//		}
		handler : getAchar
	})
	var content = new Ext.form.FieldSet({
		title : '查询条件',
		height : 100,
		collapsible : true,
		layout : 'column',
		items : [{
			columnWidth : 0.2,
			layout : 'form',
			align : 'left',
			labelWidth : 2,
			border : false,
			items : [{
				id : 'queryModel_Zro',
				name : 'queryModel',
				xtype : "radio",
				boxLabel : '模糊',
				align : 'left',
				labelSeparator : '',
				inputValue : '0',
				checked : true,
				listeners : {
					check : check
				}
			}, {
				id : 'queryModel_Fir',
				name : 'queryModel',
				align : 'left',
				xtype : "radio",
				boxLabel : '间隔',
				labelSeparator : '',
				inputValue : '1',
				listeners : {
					check : check
				}
			}, {
				id : 'queryModel_Sec',
				name : 'queryModel',
				align : 'left',
				xtype : "radio",
				boxLabel : '区间',
				labelSeparator : '',
				inputValue : '2',
				listeners : {
					check : check
				}
			}]
		}, {
			layout : 'column',
			columnWidth : 0.8,
			labelWidth : 35,
			border : false,
			items : [{
				border : false,
				columnWidth : 1,
				layout : 'form',
				items : [{
					border : false
				}]
			}, {
				border : false,
				columnWidth : 0.3,
				layout : 'form',
				items : [{
					id : 'undertakeNo1',
					name : 'undertakeNo1',
					xtype : "textfield",
					fieldLabel : "卷号",
					// align : 'left',
					anchor : '80%'
				}]
			}, {
				border : false,
				columnWidth : 0.3,
				layout : 'form',
				items : [{
					id : 'undertakeNo2',
					name : 'undertakeNo2',
					xtype : "textfield",
					labelSeparator : '',
					// align : 'center',
					anchor : '80%'
				}]
			}, {
				border : false,
				columnWidth : 0.1,
				layout : 'form',
				items : [btnQuery]
			}, {
				border : false,
				columnWidth : 0.1,
				layout : 'form',
				items : [btnPrint]
			}, {
				border : false,
				columnWidth : 0.1,
				layout : 'form',
				items : [btnClose]
			}, {
				border : false,
				columnWidth : 0.1,
				layout : 'form',
				items : [btnAch]
			}]
		}]
	});
	var form = new Ext.FormPanel({
		id : 'shift-form',
		labelWidth : 80,
		// autoHeight : true,
		border : false,
		items : [content]
	});
	var archive = Ext.data.Record.create([{
		name : 'archivesId'
	}, {
		name : 'undertakeNo'
	}, {
		name : 'archivesName'
	}, {
		name : 'archivesCount'
	}, {
		name : 'pieceCount'
	}, {
		name : 'pageCount'
	}, {
		name : 'unitName'
	}, {
		name : 'weaveDate'
	}, {
		name : 'memo'
	}]);
	var archiveSm = new Ext.grid.RowSelectionModel({
		singleSelect : true,
		listeners : {
			rowselect : function(sm, row, rec) {
			}
		}
	});
	var archiveCm = new Ext.grid.ColumnModel([{
		header : 'id',
		dataIndex : 'archivesId',
		hidden : true
	}, new Ext.grid.RowNumberer({
		header : '项次号',
		width : 50,
		align : 'left'
	}), {
		header : '档号',
		dataIndex : 'undertakeNo',
		align : 'center'
	}, {
		header : '案卷提名',
		dataIndex : 'archivesName',
		align : 'center'
	}, {
		header : '份数',
		dataIndex : 'archivesCount',
		align : 'center'
	}, {
		header : '每份件数',
		dataIndex : 'pieceCount',
		align : 'center'
	}, {
		header : '每份页数',
		dataIndex : 'pageCount',
		align : 'center'
	}, {
		header : '文字页数',
		dataIndex : 'charCount',
		align : 'center'
	}, {
		header : '图纸页数',
		dataIndex : 'drawCount',
		align : 'center'
	}, {
		header : '编制单位',
		dataIndex : 'unitName',
		align : 'center'
	}, {
		header : '编制日期',
		dataIndex : 'weaveDate',
		align : 'center'
	}, {
		header : '备注',
		dataIndex : 'memo',
		align : 'center'
	}]);
	archiveCm.defaultSortable = true;
	var archiveDs = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managecontract/queryArch.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : 'list'
		}, archive)
	});
	// archiveDs.load();

	archiveBbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : archiveDs,
		displayInfo : true,
		displayMsg : '共 {2} 条',
		emptyMsg : "没有记录"
	});
	var archiveGrid = new Ext.grid.EditorGridPanel({
		store : archiveDs,
		cm : archiveCm,
		sm : archiveSm,
		bbar : archiveBbar,
		frame : false,
		border : false,
		autoWidth : true,
		autoScroll : true,
		clicksToEdit : 1
	});
	archiveGrid.on('rowdblclick', function(grid, rowIndex, e){
		getAchar();
	})
	function getAchar(){
//		案卷目录
	}
	var layout = new Ext.Viewport({
		layout : 'border',
		border : true,
		items : [{
			layout : 'fit',
			margins : '0 0 0 0',
			region : 'center',
			autoScroll : true,
			items : archiveGrid
		}, {
			layout : 'fit',
			margins : '0 0 0 0',
			region : 'north',
			height : 100,
			items : form
		}]
	});
	// Ext.get('btnQuery').dom.onclick();
	setNo();
		// archiveDs.on('beforeload', function() {
		// // Ext.apply(this.baseParams, {
		// //
		// // });
		// Ext.get('btnQuery').dom.onclick();
		// });
	})