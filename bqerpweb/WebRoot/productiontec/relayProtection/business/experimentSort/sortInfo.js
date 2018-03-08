Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	
	// 定义grid
var MyRecord = Ext.data.Record.create([
	{name : 'sylbId'},
    {name : 'sylbName'},
	{name : 'displayNo'}
	]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url:'productionrec/findExperimentSortList.action'
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
	



	
	var sm = new Ext.grid.CheckboxSelectionModel();

	var grid = new Ext.grid.GridPanel({
		region : "center",
		layout : 'fit',
		store : store,

		columns : [
		sm, new Ext.grid.RowNumberer({header:'序号',width : 50}),{
			
			header : "ID",
			width : 75,
			sortable : true,
			dataIndex : 'sylbId',
			hidden:true
		},

		{
			header : "类别名称",
			width : 75,
			sortable : true,
			dataIndex : 'sylbName'
		},

		{
			header : "显示顺序",
			width : 75,
			sortable : true,
			dataIndex : 'displayNo'
		}
		],
		sm : sm,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : true
		},
		tbar : [
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

	var sylbId = new Ext.form.Hidden({
		id : "sylbId",
		fieldLabel : '类别ID',
		width : wd,
		readOnly : true,
		value:'',
		name : 'sort.sylbId'

	});
	
	var sylbName =new Ext.form.TextField( {
		id : "sylbName",
		fieldLabel : '类别名称', 
		allowBlank : false,
		//height:80,
		width : wd,
		name : 'sort.sylbName'
	});
	
 
	var displayNo=new Ext.form.NumberField({
	id : "displayNo",
		fieldLabel : '显示顺序',
		width : wd,
		name : 'sort.displayNo'
	
	}); 
	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		labelWidth:80,
		closeAction : 'hide',
		title : '增加/修改实验类别信息',
		items : [sylbId,sylbName,displayNo] 
	});
	
	function checkInput()
	{
		if(sylbName.getValue()=="")
		{
			Ext.Msg.alert("提示","请输入类别名称！");
			return false;
		}
		return true;
	}
		

	var win = new Ext.Window({
		width : 400,
		height :150,
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
				var myurl="";
				if (sylbId.getValue() == "") {
					myurl="productionrec/addExperimentSortInfo.action";
				} else {
						myurl="productionrec/updateExperimentSortInfo.action";
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
	
	
		  

	
	function addRecord()
	{  
		myaddpanel.getForm().reset(); 
		win.show();  
		myaddpanel.setTitle("增加实验类别信息");
	}
	
	function updateRecord()
	{
			if (grid.selModel.hasSelection()) {
		
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				  win.show(); 
				var record = grid.getSelectionModel().getSelected();
		        myaddpanel.getForm().reset();
		        myaddpanel.form.loadRecord(record);
				myaddpanel.setTitle("修改实验类别信息");
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
				if (member.get("sylbId")) {
					ids.push(member.get("sylbId")); 
					names.push(member.get("sylbName"));
				} else {
					
					store.remove(store.getAt(i));
				}
			}
			
			Ext.Msg.confirm("删除", "是否确定删除名称为'" + names + "'的记录？", function(
					buttonobj) {

				if (buttonobj == "yes") {

					Ext.lib.Ajax.request('POST',
							'productionrec/deleteExperimentSortInfo.action', {
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