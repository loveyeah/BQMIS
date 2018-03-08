Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() { 
	// 定义grid
var MyRecord = Ext.data.Record.create([
	{name : 'unitId'},
    {name : 'unitName'},
	{name : 'unitAlias'},
	{name : 'retrieveCode'}
	]); 
	var dataProxy = new Ext.data.HttpProxy( 
			{
				url:'manager/getUnitList.action'
			} 
	); 
	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount" 
	}, MyRecord); 
	var store = new Ext.data.Store({ 
		proxy : dataProxy, 
		reader : theReader 
	});
	store.on("beforeload",function(){
	      Ext.Msg.wait("正在查询数据，请等待...");
	});
	store.on("load",function(){
		Ext.Msg.hide();
	});
//分页
	store.load({
			params : {
				start : 0,
				limit : 18				
			}
		}); 
	var fuuzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy",
		listeners:{
			specialkey : function(field, e) {
						if (e.getKey() == Ext.EventObject.ENTER) { 
						      queryRecord();
						}
					}
		}
	});
	var sm = new Ext.grid.CheckboxSelectionModel(); 
	var grid = new Ext.grid.GridPanel({
		region : "center",
		layout : 'fit',
		store : store, 
		columns : [
		sm, { 
			header : "ID",
			width : 75,
			sortable : true,
			dataIndex : 'unitId',
			hidden:true
		}, 
		{
			header : "计量单位名称",
			width : 75,
			sortable : true,
			dataIndex : 'unitName'
		}, 
		{
			header : "计量单位别名",
			width : 75,
			sortable : true,
			dataIndex : 'unitAlias'
		}, 
		{
			header : "检索码",
			width : 75,
			sortable : true,
			dataIndex : 'retrieveCode'
		} 	],
		sm : sm,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : true
		},
//		title : '计量单位维护', 
		tbar : [fuuzy, {
			text : "查询",
			iconCls:'query',
			handler : queryRecord
		},
		{
			text : "新增",
            iconCls : 'add',
			handler :addRecord
		}, {
			text : "修改",
			iconCls : 'update',
			handler : updateRecord
		}, {
			text : "删除",
			iconCls : 'delete',
			handler : deleteRecord
		}],
		//分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});

grid.on("dblclick", updateRecord);
//---------------------------------------

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
	//	layout : "fit",
		items : [grid]
	});

	// -------------------
	var wd = 180;

	var unitId = {
		id : "unitId",
		xtype : "textfield",
		fieldLabel : 'ID',
		value : '自动生成',
		width : wd,
		readOnly : true,
		name : 'unitId'

	}
	var unitName =new Ext.form.TextField( {
		id : "unitName",
		xtype : "textfield",
		fieldLabel : '名称',
		allowBlank : false,
		width : wd,
		name : 'unit.unitName'

	});
	


	var unitAlias = {
		id : "unitAlias",
		xtype : "textfield",
		fieldLabel : '别名',
		name : 'unit.unitAlias',
		width : wd
	}

	var retrieveCode = {
		id : "retrieveCode",
		xtype : "textfield",
		fieldLabel : '检索码',
		name : 'unit.retrieveCode',
		width : wd,
		allowBlank : false
	}
	


	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		title : '增加/修改计量单位信息',
		items : [unitId,unitName,unitAlias,retrieveCode]

	});
		

	var win = new Ext.Window({
		width : 400,
		height : 250,
		buttonAlign : "center",
		items : [myaddpanel],
		layout : 'fit',
		closeAction : 'hide',
		buttons : [{
			text : '保存',
			handler : function() {
				var myurl="";
				if (Ext.get("unitId").dom.value == "自动生成") {
					myurl="manager/addUnit.action";
				} else {
						myurl="manager/updateUnit.action";
				}
				// alert(op);
				myaddpanel.getForm().submit({
					method : 'POST',
					url : myurl,
					success : function(form, action) {

						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("注意", o.msg);
							store.load({
		                                        	params : {
				                                    start : 0,
				                                    limit : 18
			                                        }
		                                          });
						win.hide(); 
					},
					faliue : function() {
						Ext.Msg.alert('错误', '出现未知错误.');
					}
				});
			}
		}, {
			text : '取消',
			handler : function() {
				win.hide();
			}
		}]

	});
	
	
		  
	// 查询
	function queryRecord() {
		var fuzzytext = fuuzy.getValue();
		store.baseParams = {
			fuzzy : fuzzytext
		};
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}
	
	function addRecord()
	{
			
		myaddpanel.getForm().reset(); 
		         win.show();  
		         unitName.on("change", function(filed, newValue, oldValue) {
		         
							Ext.Ajax.request({
								url : 'manager/getRetrieveCode.action',
								params : {
									name : newValue
								},
								method : 'post',
								success : function(result, request) {
									var json = result.responseText;
									var o = eval("(" + json + ")");
									Ext.get("retrieveCode").dom.value = o.substring(0,8);
								}
							});
					});
		myaddpanel.setTitle("增加计量单位信息");
		unitName.focus();
		return;
	}
	
	function updateRecord()
	{
			if (grid.selModel.hasSelection()) {
		
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				var record = grid.getSelectionModel().getSelected();
		
				win.show();
					    unitName.on("change", function(filed, newValue, oldValue) {
		         
							Ext.Ajax.request({
								url : 'manager/getRetrieveCode.action',
								params : {
									name : newValue
								},
								method : 'post',
								success : function(result, request) {
									var json = result.responseText;
									var o = eval("(" + json + ")");
									Ext.get("retrieveCode").dom.value = o.substring(0,8);
								}
							});
					});
				myaddpanel.getForm().loadRecord(record);
				myaddpanel.setTitle("修改计量单位信息");
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}
	
	function deleteRecord()
	{
		
			var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		var names=[];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {

			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.unitId) {
					ids.push(member.unitId); 
					names.push(member.unitName);
				} else {
					
					store.remove(store.getAt(i));
				}
			}
			
			Ext.Msg.confirm("删除", "是否确定删除名称为'" + names + "'的记录？", function(
					buttonobj) { 
				if (buttonobj == "yes") { 
					Ext.lib.Ajax.request('POST',
							'manager/deleteUnit.action', {
								success : function(action) {
									Ext.Msg.alert("提示", "删除成功！")
                                   		store.load({
		                                        	params : {
				                                    start : 0,
				                                    limit : 18
			                                        }
		                                          }); 
								},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
							}, 'ids=' + ids);
				}
			});
		} 
	} 
});