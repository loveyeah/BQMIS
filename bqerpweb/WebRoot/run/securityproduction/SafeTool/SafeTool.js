Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	
	// 定义grid
var MyRecord = Ext.data.Record.create([
	{name : 'tool.toolsId'},
    {name : 'tool.toolsNames'},
	{name : 'tool.toolsCode'},
	{name : 'tool.toolsSizes'},
	{name : 'tool.factory'},
	{name : 'strManuDate'},
	{name : 'chargeName'},
	{name : 'tool.chargeMan'},
	{name : 'strCheckDate'},
	{name : 'tool.state'},
	{name : 'tool.memo'}
	]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url:'security/findSafeToolList.action'
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
		sm, {
			
			header : "ID",
			width : 75,
			sortable : true,
			dataIndex : 'tool.toolsId',
			hidden:true
		},

		{
			header : "工具名称",
			width : 75,
			sortable : true,
			dataIndex : 'tool.toolsNames'
		},

		{
			header : "工具编号",
			width : 75,
			sortable : true,
			dataIndex : 'tool.toolsCode'
		},

		{
			header : "负责人",
			width : 75,
			sortable : true,
			dataIndex : 'chargeName'
		},
		{
			header : "规格型号",
			width : 75,
			sortable : true,
			dataIndex : 'tool.toolsSizes'
		},
		{
			header : "状态",
			width : 75,
			sortable : true,
			dataIndex : 'tool.state',
			renderer:function(value){
				if(value=="0") return "不合格";
				else if(value=="1") return "合格";
				else return "";
			}
		}
		],
		sm : sm,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : true
		},
		//title : '安全工器具维护',
		
		tbar : ['工具名称或负责人：',fuzzy, {
			text : "查询",
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
	var wd = 220;

	var toolsId = new Ext.form.Hidden({
		id : "toolsId",
		fieldLabel : 'ID',
		width : wd,
		readOnly : true,
		value:'',
		name : 'tool.toolsId'

	});
	
	var toolsNames =new Ext.form.TextField( {
		id : "toolsNames",
		fieldLabel : '工具名称', 
		allowBlank : false,
		width : wd,
		name : 'tool.toolsNames'
	});
	
  var toolsCode =new Ext.form.TextField( {
		id : "toolsCode",
		fieldLabel : '工具编号',
		allowBlank : false,
		width : wd,
		name : 'tool.toolsCode'
	});

	  var toolsSizes =new Ext.form.TextField( {
		id : "toolsSizes",
		fieldLabel : '规格型号',
		width : wd,
		name : 'tool.toolsSizes'
	});

 var factory =new Ext.form.TextField( {
		id : "factory",
		fieldLabel : '生产厂家',
		width : wd,
		name : 'tool.factory'
	});
	
	 var manuDate =new Ext.form.DateField( {
		id : "manuDate",
		fieldLabel : '生产日期',
		readOnly:true,
		width : wd,
		name : 'tool.manuDate',
	    format:'Y-m-d'
	});


	var chargeMan = {
		fieldLabel : '负责人',
		name : 'chargeMan',
		xtype : 'combo',
		id : 'chargeMan',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'tool.chargeMan',
		allowBlank : false,
		editable : false,
		width : wd,
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '合肥电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(emp) != "undefined") {
				Ext.getCmp('chargeMan').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('chargeMan'), emp.workerName);
			}
		}
	};
	
	var checkDate=new Ext.form.DateField({
	   id:'checkDate',
	   fieldLabel : '检修日期',
	   readOnly:true,
	   width : wd,
	    name : 'tool.checkDate',
	    format:'Y-m-d'
	});
	
	var state = new Ext.form.ComboBox({
		fieldLabel : '状态',
		store : new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"],
			data : [['0', '不合格'], ['1', '合格']]
		}),
		valueField : "retrunValue",
		displayField : "displayText",
		mode : 'local',
	//	forceSelection : true,
		hiddenName : 'tool.state',
		value : '',
		editable : false,
		triggerAction : 'all',
	//	selectOnFocus : true,
	//	allowBlank : false,
		name : 'state',
		width : wd
			
	});
	
	var memo=new Ext.form.TextArea({
	id : "memo",
		fieldLabel : '备注',
		width : 260,
		name : 'tool.memo',
		height:80
	
	}); 
	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		labelWidth:80,
		closeAction : 'hide',
		title : '增加/修改工器具信息',
		items : [toolsId,toolsNames,toolsCode,toolsSizes,
		factory,manuDate,chargeMan,checkDate,state,memo] 
	});
	
	function checkInput()
	{
		var msg="";
		if(toolsNames.getValue()=="")
		{msg="'工具名称'";}
		if(toolsCode.getValue()=="")
		{
			if(msg=="") msg="'工具编号'";
			else msg=msg+",'工具编号'";
		}
		if(Ext.get("chargeMan").dom.value=="")
		{
			if(msg=="") msg="'负责人'";
			else msg=msg+",'负责人'";
		}
		if(msg!="")
		{
		Ext.Msg.alert("提示","请输入"+msg);
		return false
		}
		else
		{
			return true;
		}
		
	}
		

	var win = new Ext.Window({
		width : 400,
		height : 400,
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
				if (toolsId.getValue() == "") {
					myurl="security/addSafeToolInfo.action";
				} else {
						myurl="security/updateSafeToolInfo.action";
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
			toolOrMan : fuzzytext
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
		myaddpanel.setTitle("增加工器具信息");
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
				toolsId.setValue(record.get("tool.toolsId"));
				toolsNames.setValue(record.get("tool.toolsNames"));
				toolsCode.setValue(record.get("tool.toolsCode"));
				toolsSizes.setValue(record.get("tool.toolsSizes"));
				factory.setValue(record.get("tool.factory"));
				manuDate.setValue(record.get("strManuDate"));
				checkDate.setValue(record.get("strCheckDate"));
				state.setValue(record.get("tool.state"));
				memo.setValue(record.get("tool.memo"));
				Ext.getCmp('chargeMan').setValue(record.get("tool.chargeMan"));
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('chargeMan'), record.get("chargeName"));
						
				
				myaddpanel.setTitle("修改工器具信息");
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
				if (member.get("tool.toolsId")) {
					ids.push(member.get("tool.toolsId")); 
					names.push(member.get("tool.toolsNames"));
				} else {
					
					store.remove(store.getAt(i));
				}
			}
			
			Ext.Msg.confirm("删除", "是否确定删除名称为'" + names + "'的记录？", function(
					buttonobj) {

				if (buttonobj == "yes") {

					Ext.lib.Ajax.request('POST',
							'security/deleteSafeToolInfo.action', {
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