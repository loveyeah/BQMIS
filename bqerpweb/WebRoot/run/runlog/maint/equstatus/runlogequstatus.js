Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
		var colorComboBox = new Ext.form.ComboBox({
		fieldLabel : '颜色值',
		store : new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"]
		}),
		id : 'colorValueId',
		valueField : "retrunValue",
		displayField : "displayText",
		mode : 'local',
		forceSelection : true,
		hiddenName : 'status.colorValue',	
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		anchor : '80%',
		onTriggerClick:function(){
		 callColor();
		}
	});
	var gridForm = new Ext.FormPanel({
		id : 'equStatus-form',
		frame : true,
		autoWidth : true,
		autoHeight : true,
		align : 'center',
		labelAlign : 'left',
		items : [{
			xtype : 'fieldset',
			labelAlign : 'right',
			title : '设备运行状态维护',
			defaultType : 'textfield',
			autoHeight : true,
			border : true,
			items:[
			{	
				name : 'status.equstatusId',
				xtype : 'hidden'
			},
			{			 
				fieldLabel : '状态名称',
				allowBlank : false,
				anchor : '80%',
				name : 'status.statusName'
			},
			{			 
				fieldLabel : '状态说明',
				allowBlank : false,
				anchor : '80%',
				name : 'status.statusDesc'
			},colorComboBox],
			buttons : [{
				text : '保存',
				iconCls : 'save',
				handler : function() {
					if (gridForm.getForm().isValid()) {
						gridForm.getForm().submit({
							url : 'runlog/'+(Ext.get("status.equstatusId").dom.value==""?"addEquStatus":"updateEquStatus")+'.action',
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
				iconCls:'cancer',
				handler : function() {
					win.hide(); 
				}
			}]
		}]
	});
	
     function callColor(){
		    var colorpage = window.showModalDialog('../../../../run/runlog/maint/equstatus/color.html','','dialogWidth:300px;dialogHeight:250px;status:no;help:no;scroll=no;top=200;left=300');   
		    colorComboBox.setValue(colorpage); 
	    }
	var equStatus = new Ext.data.Record.create([{
		 name : 'equstatusId'},
	    {name : 'statusName'},
		{name : 'statusDesc'},
		{name : 'colorValue'},
		{name : 'isUse'},
		{name : 'enterpriseCode'}]);
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/findEquStatus.action',
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({
			root : 'equStatusList'
		}, equStatus)
	});
	ds.load();
	
		var box3= new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) { }
		}
	});
	var colModel = new Ext.grid.ColumnModel([	
	box3,{
			id : 'equstatusId',
			header : '状态编码',
			dataIndex : 'equstatusId',
			sortable : true,
			hidden : true ,
			width : 250
		},{
		id : 'statusName',
		header : '状态名称',
		dataIndex : 'statusName',
		width : 200,
		sortable : true
	}, {
		header : '颜色值',
		dataIndex : 'colorValue',
		renderer:showColor,
		width : 200
	}, {
		header : '状态说明',
		dataIndex : 'statusDesc',
		width : 200
	}, 
	{
		header : '使用状态',
		dataIndex : 'isUse',
		width : 200,
		renderer : function changeIt(val) {
					return (val == "Y") ? "使用" : "停用";
				}
	}]);
	
	function showColor(v)
	{ 
		return "<div  style='width:40; background:"+v+"'>&nbsp;&nbsp;&nbsp;&nbsp;</div>";
	}

	// 排序
	colModel.defaultSortable = true;
	var tbar = new Ext.Toolbar({
		items : [ {
			id : 'btnReflesh',
			text : "查询",
			iconCls : 'query',
			handler : function() {
				ds.load();
			}
		},'-',{
			id : 'btnAdd',
			text : "新增",
			iconCls : 'add',
			handler : function() { 
				win.show(); 
				gridForm.getForm().reset(); 
		//		Ext.get("status.equstatusId").dom.focus();				
			}
		}, '-', {
					id : 'btnUpdate',
					text : "修改",
					iconCls : 'update',
					handler : function() {

						var rec = equStatusGrid.getSelectionModel().getSelections();
		    if(rec.length!=1){
			    Ext.Msg.alert("提示","请选择一行！");
			    return false;
		    }
		    else{
			                win.show();		
			                var rec = equStatusGrid.getSelectionModel().getSelected();
							
							Ext.get("status.equstatusId").dom.value=rec.get("equstatusId");
							Ext.get("status.statusName").dom.value=rec.get("statusName");
							Ext.get("status.statusDesc").dom.value=rec.get("statusDesc");	
							colorComboBox.setValue(rec.get("colorValue"));
						} 
					}
				}, '-', {
			id : 'btnDelete',
			text : "删除",
			iconCls : 'delete',
			handler : function() {
				var rec = equStatusGrid.getSelectionModel().getSelections();
				var names = "";
				if (rec.length == 0) {
					Ext.Msg.alert("提示", "请选择要删除的记录！");
				} else {
					for (i = 0; i < rec.length; i++) {
						names += rec[i].data.statusName + ",";
					}
					names = names.substring(0, names.length - 1);

					if (confirm("确定要删除\"" + names + "\"设备状态吗？")) {
						for (i = 0; i < rec.length; i++) {
							Ext.Ajax.request({
								url : 'runlog/deleteEquStatus.action?status.equstatusId='
										+ rec[i].get("equstatusId"),
								method : 'post',
								waitMsg : '正在删除数据...',
								success : function(result, request) {
									Ext.Msg.alert('提示', '删除成功!');
									ds.load();
								},
								failure : function(result, request) {
									Ext.Msg.alert('提示', '删除失败!');
								}
							});
						}
					}
				}
			}
		}]
	});


	
	var equStatusGrid = new Ext.grid.GridPanel({
				id : 'equStatus-grid',
				autoScroll : true,
				ds : ds,
				cm : colModel,
				sm : box3,
				tbar : tbar,
				border : true
			});
	equStatusGrid.on('rowdblclick', function(grid, rowIndex, e) {
				win.show();
				var rec = equStatusGrid.getStore().getAt(rowIndex);
							Ext.get("status.equstatusId").dom.value=rec.get("equstatusId");
							Ext.get("status.statusName").dom.value=rec.get("statusName");
							Ext.get("status.statusDesc").dom.value=rec.get("statusDesc");
							colorComboBox.setValue(rec.get("colorValue"));												
			});
	var win = new Ext.Window({
		el : 'form-div',
		width : 400,
		height : 200,
		region : "center",
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
		items : [equStatusGrid]
	});

	setTimeout(function() {
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut({
			remove : true
		});
	}, 250);
	
	
});