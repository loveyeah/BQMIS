Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	
	
		// 定义grid
var MyRecord = Ext.data.Record.create([
	{name : 'sourceId'},
    {name : 'sourceCode'},
	{name : 'sourceName'}
	]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url:'equchange/findChangeSourceList.action'
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
//分页
	store.load({
			params : {
				start : 0,
				limit : 18				
			}
		});

	var fuuzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});
	var sm = new Ext.grid.CheckboxSelectionModel();

	var grid = new Ext.grid.GridPanel({
		region : "center",
		store : store,
		columns : [
		sm, {
			
			header : "ID",
			width : 75,
			sortable : true,
			dataIndex : 'sourceId',
			hidden:true
		},

		{
			header : "来源类型编码",
			width : 100,
			sortable : true,
			dataIndex : 'sourceCode'
		},

		{
			header : "来源类型名称",
			width : 300,
			sortable : true,
			dataIndex : 'sourceName'
		}
		],
		tbar : ['编码或名称：',fuuzy, {
			text : "查询",
			  iconCls : 'query',
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
		sm:sm,
		//分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});
	
	//--------增加/修改
		var wd = 180;

	var sourceId = {
		id : "sourceId",
		xtype : "textfield",
		fieldLabel : 'ID',
		value : '自动生成',
		width : wd,
		readOnly : true,
		name : 'sourceId'

	}
	var sourceCode =new Ext.form.TextField( {
		id : "sourceCode",
		xtype : "textfield",
		fieldLabel : '编码',
		allowBlank : false,
		width : wd,
		name : 'source.sourceCode'

	});
	


	var sourceName = {
		id : "sourceName",
		xtype : "textfield",
		fieldLabel : '名称',
		allowBlank : false,
		name : 'source.sourceName',
		width : wd
	}
	


	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		title : '增加/修改异动来源类型',
		items : [sourceId,sourceCode,sourceName]

	});
		

	var win = new Ext.Window({
		width : 400,
		height : 200,
		buttonAlign : "center",
		items : [myaddpanel],
		layout : 'fit',
		closeAction : 'hide',
		modal : true,
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
				var myurl="";
				if (Ext.get("sourceId").dom.value == "自动生成") {
					myurl="equchange/addChangeSource.action";
				} else {
						myurl="equchange/updateChangeSource.action";
				}
				
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
			iconCls : 'cancer',
			handler : function() {
				win.hide();
			}
		}]

	});
	
	
	
	
   function queryRecord()
   {
   	
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
	myaddpanel.setTitle("增加故障来源类型");
   }
      function updateRecord() {
		if (grid.selModel.hasSelection()) {

			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				var record = grid.getSelectionModel().getSelected();
				win.show();
				myaddpanel.getForm().loadRecord(record);
				myaddpanel.setTitle("修改异动来源类型");
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
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {

			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.sourceId) {
					ids.push(member.sourceId); 
				} else {
					
					store.remove(store.getAt(i));
				}
			}
			
			Ext.Msg.confirm("删除", "是否确定删除id为" + ids + "的记录？", function(
					buttonobj) {

				if (buttonobj == "yes") {

					Ext.lib.Ajax.request('POST',
							'equchange/deleteChangeSource.action', {
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
   
   
   
   new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});
	
})