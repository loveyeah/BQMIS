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
				url:'com/findSalaryTypeList.action?isBasicData=1'
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
		tbar : ['基本类别信息',{
			text : "修改",
			iconCls : 'update',
			handler : updateRecord
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
		value:'',
		name : 'salaryType.salaryTypeId'

	});
	
	var salaryTypeName =new Ext.form.TextField( {
		id : "salaryTypeName",
		fieldLabel : '类别名称', 
		allowBlank : false,
	//	readOnly : true,
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
		if(tradeName.getValue()=="")
		{
			Ext.Msg.alert("提示","请输入行业名称！");
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
				var  myurl="com/updateSalaryTypeInfo.action";
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
	
	
	
	
});