Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() { 
	//var runEquId = 10;
	var runEquId = getParameter("runEquId");
	var equStatus = new Ext.data.Record.create([{
		name : 'equstatusId'
	}, {
		name : 'statusName'
	}, {
		name : 'statusDesc'
	}, {
		name : 'colorValue'
	}, {
		name : 'isUse'
	}, {
		name : 'enterpriseCode'
	}]); 
	var equ_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/findEquStatus.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'equStatusList'
		}, equStatus)
	});
	equ_ds.load();

	var waitequStatus = new Ext.data.Record.create([{
		name : 'runstatusId'
	}, {
		name : 'runEquId'
	}, , {
		name : 'equstatusId'
	}, {
		name : 'statusName'
	}, {
		name : 'colorValue'
	}, {
		name : 'statusDesc'
	}]);
	var wait_equ_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/findRunStatus.action'
		}),
		method : 'post',
		reader : new Ext.data.JsonReader({
			root : 'data'
		}, waitequStatus)
	});
	wait_equ_ds.load({params : {
			runEquId : runEquId 
		}});

	/* 设置记录行的选择框 */
	var equ_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			rowselect : function(sm, row, rec) {
			}
		}
	});
	var wait_equ_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			rowselect : function(sm, row, rec) {
			}
		}
	});

	// 显示颜色
	function showColor(v) {
		return "<div  style='width:40; background:" + v
				+ "'>&nbsp;&nbsp;&nbsp;&nbsp;</div>";
	}

	var equ_cm = new Ext.grid.ColumnModel([equ_sm, {
		header : '状态编码',
		dataIndex : 'equstatusId',
		align : 'center',
		hidden : true
	}, {
		header : '状态名称',
		dataIndex : 'statusName',
		align : 'center'
	}, {
		header : '颜色',
		dataIndex : 'colorValue',
		renderer : showColor,
		align : 'center'
	}, {
		header : '状态说明',
		dataIndex : 'statusDesc',
		align : 'center',
		hidden : true
	}]);

	equ_cm.defaultSortable = true;
	var wait_equ_cm = new Ext.grid.ColumnModel([wait_equ_sm, {
		header : 'ID',
		dataIndex : 'runstatusId',
		hidden : true
	}, {
		header : '运行方式下设备ID',
		dataIndex : 'runEquId',
		align : 'center'
	}, {
		header : '状态编码',
		dataIndex : 'equstatusId',
		align : 'center',
		hidden : true
	}, {
		header : '状态名称',
		dataIndex : 'statusName',
		align : 'center'
	}, {
		header : '颜色',
		dataIndex : 'colorValue',
		renderer : showColor,
		align : 'center'
	}, {
		header : '状态说明',
		dataIndex : 'statusDesc',
		align : 'center'
	}]);
	wait_equ_cm.defaultSortable = true;

	var waitEquGrid = new Ext.grid.EditorGridPanel({
		el : 'rightDiv',
		ds : wait_equ_ds,
		cm : wait_equ_cm,
		sm : wait_equ_sm,
		title:'<font color="red">已选择的设备状态列表:</font>',
		width : 460,
		height: 360,
		collapsible : true,
		fitToFrame : true,
		border : true 
	});
	waitEquGrid.render();

	var alreadyEquGrid = new Ext.grid.GridPanel({
		el : 'leftDiv',
		title:'<font color="red">所有设备状态列表:</font>',
		ds : equ_ds,
		cm : equ_cm,
		sm : equ_sm,
		width : 240,
		height: 360,
		collapsible : true,
		fitToFrame : true,
		border : true 
	});
	alreadyEquGrid.render();
	
	var revokeBar = new Ext.Button({
			id : 'btnDelete',
			el:'revoke-div',
			text : "<<<", 
			handler : function() {
				var rec = waitEquGrid.getSelectionModel().getSelected();
				// alert(rec.data.runstatusId);
				if (rec) {
					if (confirm("确定要删除\"" + rec.data.statusName + "\"记录吗？")) {
						Ext.Ajax.request({
							url : 'runlog/deleteRunStatus.action?status.runstatusId='
									+ rec.get("runstatusId"),
							method : 'post',
							waitMsg : '正在删除数据...',
							success : function(result, request) {
								Ext.Msg.alert('提示', '删除成功!');
								wait_equ_ds.remove(rec);
								wait_equ_ds.reload();
							},
							failure : function(result, request) {
								Ext.Msg.alert('提示', '删除失败!');
							}
						});
					}
				} else {
					Ext.Msg.alert('提示', '请从右边选择要删除的信息!');

				}
			}
	}); 
	revokeBar.render();
	
	var grantBar = new Ext.Button({
				id : 'btnAdd',
			el:'grant-div',
			text : ">>>", 
			handler : function() {
				var selectedRows = alreadyEquGrid.getSelectionModel().getSelections();
				var ids = "";
				for (i = 0; i < selectedRows.length; i++) {
					ids += selectedRows[i].data.equstatusId + ",";
				}
				if (ids.length > 0) {
					ids = ids.substring(0, ids.length - 1); 
					//alert(runEquId);
						Ext.Ajax.request({
							url : 'runlog/addRunStatus.action',
							params : {
								ids : ids,
								runEquId : runEquId
							},
							method : 'post',
							waitMsg : '正在处理...',
							success : function(result, request) {
								for (i = 0; i < selectedRows.length; i++) {
									var r = selectedRows[i];
									
									wait_equ_ds.insert(0, r);
									wait_equ_ds.reload();							
									// equ_ds.remove(r);
								}							
							var o = eval('(' + result.responseText + ')');
							Ext.Msg.alert("注意", o.msg);
							},
							failure : function(result, request) {
								Ext.MessageBox.alert('错误', '操作失败!');
							}
						});
					 
				} else {
					Ext.MessageBox.alert('提示', '请从左边选择要添加的状态!');
				}
			}
	}); 
	grantBar.render();
	
	
});