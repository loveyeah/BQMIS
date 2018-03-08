Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	
	// 定义grid
var MyRecord = Ext.data.Record.create([
	{name : 'salaryTypeId'},
    {name : 'salaryTypeName'},
	{name : 'isInput'},
	{name : 'isNeed'},
	{name : 'modifyBy'},
	{name : 'modifyDate'}
	]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url:'com/findSalaryTypeList.action?isBasicData=0'
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
				limit : 10				
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
			dataIndex : 'tradeId',
			hidden:true
		},

		{
			header : "类别名称",
			width : 75,
			sortable : true,
			dataIndex : 'salaryTypeName'
		},

		{
			header : "是否输入",
			width : 75,
			sortable : true,
			dataIndex : 'isInput',
			renderer:function(value)
			{
				if(value=="0") return "计算";
				if(value=="1") return "输入";
			}
		},
		{
			header : "是否使用",
			width : 75,
			sortable : true,
			dataIndex : 'isNeed',
			renderer:function(value)
			{
				if(value=="0") return "不使用";
				if(value=="1") return "使用";
			}
		},
		{
			header : "修改人",
			width : 75,
			sortable : true,
			dataIndex : 'modifyBy'
		},
		{
			header : "修改时间",
			width : 75,
			sortable : true,
			dataIndex : 'modifyDate',
			renderer:function(value)
			{
			 return value.substring(0,10);
			}
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

	var salaryTypeId = new Ext.form.Hidden({
		id : "salaryTypeId",
		fieldLabel : 'ID',
		width : wd,
		readOnly : true,
		value:'',
		name : 'salaryType.salaryTypeId'

	});
	
	var salaryTypeName =new Ext.form.TextField( {
		id : "salaryTypeName",
		fieldLabel : '类别名称', 
		allowBlank : false,
	    anchor : '80%',
		name : 'salaryType.salaryTypeName'
	});
	
 
	var isInput = new Ext.form.ComboBox({
		hiddenName : 'salaryType.isInput',
		fieldLabel : '类型',
		anchor : '80%',
		readOnly : true,
		allowBlank : true,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
			fields : ['value','text'],
			data : [['1','输入'],['0','计算']]
		}),
		displayField : 'text',
		valueField : 'value',
		mode : 'local',
		value:'1',
		onTriggerClick:function()
		{
			return false;
		}
	});
	
	var isNeed = new Ext.form.ComboBox({
		hiddenName : 'salaryType.isNeed',
		fieldLabel : '状态',
		anchor : '80%',
		readOnly : true,
		allowBlank : true,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
			fields : ['value','text'],
			data : [['1','使用'],['0','不使用']]
		}),
		displayField : 'text',
		valueField : 'value',
			value:'1',
		mode : 'local'
	});
	
	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		labelWidth:80,
		closeAction : 'hide',
		title : '增加/修改行业信息',
		items : [salaryTypeId,salaryTypeName,isInput,isNeed] 
	});
	
	function checkInput()
	{
		if(salaryTypeName.getValue()=="")
		{
			Ext.Msg.alert("提示","请输入类别名称！");
			return false;
		}
		return true;
	}
		

	var win = new Ext.Window({
		width : 400,
		height : 200,
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
				var  myurl="";
				if(salaryTypeId.getValue()=="")
				{
			    	myurl="com/addSalaryTypeInfo.action";
				}
				else
				{
					myurl="com/updateSalaryTypeInfo.action";
				}
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
		myaddpanel.setTitle("增加类别信息");
	} 

	
	function updateRecord()
	{
			if (grid.selModel.hasSelection()) {
		
			var records = grid.getSelectionModel().getSelected();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				  win.show(); 
				var record = grid.getSelectionModel().getSelected();
		        myaddpanel.getForm().reset();
		        myaddpanel.form.loadRecord(record);
		        isInput.setValue(record.get("isInput"));
		        isNeed.setValue(record.get("isNeed"));
				myaddpanel.setTitle("修改类别信息");
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
				if (member.get("salaryTypeId")) {
					ids.push(member.get("salaryTypeId")); 
					names.push(member.get("salaryTypeName"));
				} else {
					
					store.remove(store.getAt(i));
				}
			}
			
			Ext.Msg.confirm("删除", "是否确定删除名称为'" + names + "'的记录？", function(
					buttonobj) {

				if (buttonobj == "yes") {

					Ext.lib.Ajax.request('POST',
							'comm/deleteSalaryTypeInfo.action', {
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