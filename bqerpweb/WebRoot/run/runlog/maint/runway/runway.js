	
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var gridForm = new Ext.FormPanel({
		id : 'runWay-form',
		frame : true,
		autoWidth : true,
		autoHeight : true,
		align : 'center',
		labelAlign : 'left',
		items : [{
			xtype : 'fieldset',
			labelAlign : 'right',
			title : '运行方式维护',
			defaultType : 'textfield',
			autoHeight : true,
			border : true,
			items : [
			{ 
				name : 'runway.runKeyId',
				xtype : 'hidden'
			},
			{			 
				fieldLabel : '运行方式编码',
				allowBlank : false,
				anchor : '90%',
				name : 'runway.runWayCode'
			},
			{			 
				fieldLabel : '运行方式名称',
				allowBlank : false,
				anchor : '90%',
				name : 'runway.runWayName'
			},
			{			 
				fieldLabel : '显示顺序',
				xtype : 'numberfield',
				allowBlank : false,
				anchor : '90%',
				name : 'runway.diaplayNo'
			}],
			buttons : [{
				text : '保存',
				iconCls : 'save',
				handler : function() {
					if (gridForm.getForm().isValid()) {
						gridForm.getForm().submit({
							url : 'runlog/'+(Ext.get("runway.runKeyId").dom.value==""?"addRunWay":"updateRunWay")+'.action',
							waitMsg : '正在保存数据...',
							method : 'post',
							success : function(form, action) {

						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("注意", o.msg);
						ds.load();
						win.hide(); 
					},
					faliue : function() {
						Ext.Msg.alert('错误', '出现未知错误.');
					}
				});
					}
				}
			}, {
				text : '取消',
				iconCls : 'cancer',
				handler : function() {
					win.hide();
				}
			}]
		}]
	});

	

	var RunWay = new Ext.data.Record.create([{
		name : 'runKeyId'
	}, {
		name : 'runWayCode'
	}, {
		name : 'runWayName'
	}, {
		name : 'diaplayNo'
	}, {
		name : 'isUse'
	}, {
		name : 'enterpriseCode'
	}]);
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/findRunWay.action',
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({
		//	root : 'runwayList'
		}, RunWay)
	});
	ds.load();
	
		var box2= new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) { }
		}
	});

	var colModel = new Ext.grid.ColumnModel([	
	box2,{
			id : 'runKeyId',
			header : 'ID',
			dataIndex : 'runKeyId',
			sortable : true,
			hidden : true,
			width : 200
		},{
		id : 'runWayCode',
		header : '运行方式编码',
		dataIndex : 'runWayCode',
		width : 200,
		sortable : true
	}, {
		header : '运行方式名称',
		dataIndex : 'runWayName',
		width : 200
	}, 
	{
		header : '使用状态',
		dataIndex : 'isUse',
		width : 200,
		renderer : function changeIt(val) {
					return (val == "Y") ? "使用" : "停用";
				}
	}, {
		header : '显示顺序',
		dataIndex : 'diaplayNo',
		width : 200
	}]);
	// 排序
	colModel.defaultSortable = true;
	var tbar = new Ext.Toolbar({
		items : [{
			id : 'btnReflesh',
			text : "查询",
			iconCls : 'query',
			handler : function() {
				ds.load();
			}
		} ,'-',{
			id : 'btnAdd',
			text : "新增",
			iconCls : 'add',
			handler : function() { 
				win.show();
				gridForm.getForm().reset();
				Ext.get("runway.runWayCode").dom.focus();
			}
		} , '-', {
					id : 'btnUpdate',
					text : "修改",
					iconCls : 'update',
					handler : function() {
					 var rec = runWayGrid.getSelectionModel().getSelections();	
					if(rec.length!=1){
			        Ext.Msg.alert("提示","请选择一行！");
			        return false;
		       }
		         else{
							win.show();
							var rec = runWayGrid.getSelectionModel().getSelected();
							Ext.get("runway.runKeyId").dom.value=rec.get("runKeyId");
							Ext.get("runway.runWayCode").dom.value=rec.get("runWayCode");
							Ext.get("runway.runWayName").dom.value=rec.get("runWayName");
							Ext.get("runway.diaplayNo").dom.value=rec.get("diaplayNo");						
							
						} 
					}
				}, '-', {
			id : 'btnDelete',
			text : "删除",
			iconCls : 'delete',
			handler : function() {
				var sm = runWayGrid.getSelectionModel();
				var selected = sm.getSelections();
				var ids = [];
				var names = [];
				if (selected.length == 0) {
					Ext.Msg.alert("提示", "请选择要删除的记录！");
				} else {

					for (var i = 0; i < selected.length; i += 1) {
						var member = selected[i].data;
						if (member.runKeyId) {
							ids.push(member.runKeyId);
							names.push(member.runWayName);
						} else {

							store.remove(store.getAt(i));
						}
					}

					Ext.Msg.confirm("删除", "是否确定删除名称为'" + names + "'的记录？",
							function(buttonobj) {

								if (buttonobj == "yes") {

									Ext.lib.Ajax.request('POST',
											'runlog/deleteRunWay.action', {
												success : function(action) {
													Ext.Msg
															.alert("提示",
																	"删除成功！")
													ds.load();
												},
												failure : function() {
													Ext.Msg.alert('错误',
															'删除时出现未知错误.');
												}
											}, 'ids=' + ids);
								}
							});
				}
			}
		}]

	});

	var runWayGrid = new Ext.grid.GridPanel({
				id : 'runWay-grid',
				autoScroll : true,
				ds : ds,
				cm : colModel,
				sm : box2,
				tbar : tbar,
				border : true
			});
	runWayGrid.on('rowdblclick', function(grid, rowIndex, e) {
				win.show();
				var rec = runWayGrid.getStore().getAt(rowIndex);
					Ext.get("runway.runKeyId").dom.value=rec.get("runKeyId");
					Ext.get("runway.runWayCode").dom.value=rec.get("runWayCode");
					Ext.get("runway.runWayName").dom.value=rec.get("runWayName");
					Ext.get("runway.diaplayNo").dom.value=rec.get("diaplayNo");						
					
						
			});
	var win = new Ext.Window({
		el : 'form-div',
		width : 400,
		height : 200,
		modal:true,
		closeAction : 'hide',
		items : [gridForm]
	});
	var viewport = new Ext.Viewport({
		region : "center",
		layout : 'fit',
		//layout : "border",
		autoWidth:true,
		autoHeight:true,
		fitToFrame : true,
		items : [runWayGrid]
	});

	setTimeout(function() {
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut({
			remove : true
		});
	}, 250);
});