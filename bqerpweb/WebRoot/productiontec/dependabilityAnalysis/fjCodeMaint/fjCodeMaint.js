Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	
	// 定义grid
var MyRecord = Ext.data.Record.create([
	{name : 'fjCode'},
    {name : 'fjName'},
	{name : 'orderBy'}
	]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url:'productionrec/findFjCodeList.action'
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
	



	var fuzzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});
	var sm = new Ext.grid.CheckboxSelectionModel();

	var grid = new Ext.grid.GridPanel({
		region : "center",
		layout : 'fit',
		store : store,

		columns : [
		sm, new Ext.grid.RowNumberer({header:'序号',width : 50}),{
			
			header : "辅机编码",
			width : 75,
			sortable : true,
			dataIndex : 'fjCode'
		},

		{
			header : "辅机名称",
			width : 75,
			sortable : true,
			dataIndex : 'fjName'
		},

		{
			header : "排序",
			width : 75,
			sortable : true,
			dataIndex : 'orderBy'
		}
		],
		sm : sm,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : true
		},
		tbar : ['辅机名称：',fuzzy, {
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
		//分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});

grid.on("rowdblclick", updateRecord);
//---------------------------------------

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
	//	layout : "fit",
		items : [grid]
	});

	// -------------------
	var wd = 240;

	var fjCode = new Ext.form.TextField({
		id : "fjCode",
		fieldLabel : '辅机编码',
		allowBlank : false,
		width : wd,
		name : 'code.fjCode'

	});
	
	var fjName =new Ext.form.TextField( {
		id : "fjName",
		fieldLabel : '辅机名称', 
		allowBlank : false,
		//height:80,
		width : wd,
		name : 'code.fjName'
	});
	
 
	var orderBy=new Ext.form.NumberField({
	id : "orderBy",
		fieldLabel : '排序',
		width : wd,
		name : 'code.orderBy'
	
	}); 
	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		labelWidth:80,
		closeAction : 'hide',
		title : '增加/修改辅机信息',
		items : [fjCode,fjName,orderBy] 
	});
	
	function checkInput()
	{
		if(fjCode.getValue()=="")
		{
			Ext.Msg.alert("提示","请输入辅机编码！");
			return false;
		}
		if(fjName.getValue()=="")
		{
			Ext.Msg.alert("提示","请输入辅机名称！");
			return false;
		}
		return true;
	}
		

	var win = new Ext.Window({
		width : 400,
		height : 250,
		buttonAlign : "center",
		items : [myaddpanel],
		layout : 'fit',
		closeAction : 'hide',
		resizable : false,
		modal : true,
		buttons : [{
			text : '保存',
			iconCls:'save',
			handler : function() {
				var op= Ext.get("fjCode").dom.readOnly;
				var myurl="";
				if (op==false) {
					myurl="productionrec/addFjCodeInfo.action";
				} else {
						myurl="productionrec/updateFjCodeInfo.action";
				}
				
				if(!checkInput()) return;
				myaddpanel.getForm().submit({
					method : 'POST',
					url : myurl,
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("注意", o.msg);
						if(o.msg.indexOf("成功")!=-1)
						{
							store.load({
		                                        	params : {
				                                    start : 0,
				                                    limit : 18
			                                        }
		                                          });
						win.hide(); 
						}
					},
					faliue : function() {
						Ext.Msg.alert('错误', '出现未知错误.');
					}
				});
			}
		}, {
			text : '取消',
			iconCls:'cancer',
			handler : function() { 
				win.hide();
			}
		}]

	});
	
	
		  
	// 查询
	function queryRecord() {
		var fuzzytext = fuzzy.getValue();
		store.baseParams = {
			fjName : fuzzytext
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
		myaddpanel.setTitle("增加辅机信息");
		Ext.get("fjCode").dom.readOnly=false;
	}
	
	function updateRecord()
	{
			if (grid.selModel.hasSelection()) {
		
			var records = grid.selModel.getSelections();//将getSelectionModel().getSelected()改为selModel.getSelections() modify by ywliu 20091022
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				  win.show(); 
				var record = grid.getSelectionModel().getSelected();
		        myaddpanel.getForm().reset();
		        myaddpanel.form.loadRecord(record);
				myaddpanel.setTitle("修改辅机信息");
				Ext.get("fjCode").dom.readOnly=true;
				
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}
	
	function deleteRecord()
	{
		var records = grid.selModel.getSelections();
		var ids = [];
		var names=[];
		if (records.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {

			for (var i = 0; i < records.length; i += 1) {
				var member = records[i];
				if (member.get("fjCode")) {
					ids.push(member.get("fjCode")); 
					names.push(member.get("fjName"));
				} else {
					
					store.remove(store.getAt(i));
				}
			}
			
			Ext.Msg.confirm("删除", "是否确定删除名称为'" + names + "'的记录？", function(
					buttonobj) {

				if (buttonobj == "yes") {

					Ext.lib.Ajax.request('POST',
							'productionrec/deleteFjCodeInfo.action', {
								success : function(action) {
									Ext.Msg.alert("提示", "删除成功！")
                                   		store.load({
		                                        	params : {
				                                    start : 0,
				                                    limit : 10
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