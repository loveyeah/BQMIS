Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var bview;
	var nowdate=new Date();
	var date1 = nowdate.toLocaleDateString();
	var syear=date1.substring(0, 4);
	
	var dateYear = new Ext.form.TextField({
		id : 'dateYear',
		name : 'dateYear',
		fieldLabel : "年度",
		style : 'cursor:pointer',
		cls : 'Wdate',
		value : syear,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y',
					dateFmt : 'yyyy',
					alwaysUseStartDate : true
				});
			}
		}
	});
	
	
	

	// 定义grid
var MyRecord = Ext.data.Record.create([
	{name : 'jnghzdId'},
    {name : 'mainTopic'},
	{name : 'commonDate'},
	{name : 'content'},
	{name : 'memo'}
	]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url:'productionrec/findEnergyProgramList.action'
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


	



	var sm = new Ext.grid.CheckboxSelectionModel({
	singleSelect:false
	});

	var grid = new Ext.grid.GridPanel({
		region : "center",
		layout : 'fit',
		store : store,

		columns : [
		sm, new Ext.grid.RowNumberer({header:'序号',width : 50}),{
			
			header : "ID",
			width : 75,
			sortable : true,
			dataIndex : 'jnghzdId',
			hidden:true
		},
		{
			header : "日期",
			width : 75,
			sortable : true,
			dataIndex : 'commonDate',
			renderer:function(value)
			{
				return value.substring(0,10);
			}
		},
		{
			header : "标题",
			width : 75,
			sortable : true,
			dataIndex : 'mainTopic'
		},
		{
			header : "备注",
			width : 75,
			sortable : true,
			dataIndex : 'memo'
		},
			{
			header : "内容",
			width : 75,
			sortable : true,
			dataIndex : 'content',
			renderer:function(v){
				if(v !=null && v !='')
				{ 
					var s =  '<a href="#" onclick="window.open(\''+v+'\');return  false;">[查看]</a>';
					return s;
				}
			} 
			
		}
		],
		sm : sm,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : true
		},
		tbar : ['  年度：',dateYear,
		{
			text : "查询",
            iconCls : 'query',
			handler :queryRecord
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

	var jnghzdId = new Ext.form.Hidden({
		id : "jnghzdId",
		fieldLabel : 'ID',
		anchor : "95%",
		readOnly : true,
		value:'',
		name : 'program.jnghzdId'

	});
	var mainTopic=new Ext.form.TextField({
	 id:'mainTopic',
	 fieldLabel : '标题',
		anchor : "95%",
	 name : 'program.mainTopic'
	
	});
	
var sd = new Date();
	
		var commonDate = new Ext.form.DateField({
		id : 'commonDate',
		name : 'program.commonDate',
		fieldLabel : "日期",
		format:'y-m-d',
		readOnly:true,
		anchor : "95%",
		value:sd
	});

	
		// 附件 内容
	var annex = {
		id : "annex",
		xtype : 'fileuploadfield',
		isFormField:true,
		name : "annex",
		fieldLabel : '内容',
	//	fileUpload : true,
		height : 21,
		anchor : "95%",
		buttonCfg : {
			text : '浏览...',
			iconCls : 'upload-icon'
		}
	}

	// 查看
	var btnView = new Ext.Button({
				id : 'btnView',
				text : '查看',
				handler : function() {
					window.open(bview);
				}
			});
	btnView.setVisible(false);
 
	var memo=new Ext.form.TextArea({
	 id : "memo",
		fieldLabel : '备注',
			anchor : "95%",
		name : 'program.memo',
		height:80
	
	}); 
	
	
	
	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		labelWidth:80,
		fileUpload : true,
		closeAction : 'hide',
		title : '增加/修改评价周期设置',
		layout:'column',
		items : [{
										columnWidth : 1,
										border : false,
										layout : 'form',
										items : [jnghzdId]
									},{
										columnWidth : 1,
										border : false,
										layout : 'form',
										items : [mainTopic]
									},{
										columnWidth : 1,
										border : false,
										layout : 'form',
										items : [commonDate]
									},{
										columnWidth : 0.8,
										border : false,
										layout : 'form',
										items : [annex]
									},{
										columnWidth : 0.2,
										border : false,
										layout : 'form',
										items : [btnView]
									},{
										columnWidth : 1,
										border : false,
										layout : 'form',
										items : [memo]
									}
									] 
	});
	
	function checkInput()
	{
		
		if(mainTopic.getValue()=="")
		{
		 Ext.Msg.alert("提示","请输入标题！");
		 return false;
		}
		return true;
	}
		

	var win = new Ext.Window({
		width : 500,
		height : 300,
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
				if (jnghzdId.getValue() == "") {
					myurl="productionrec/addEnergyProgramInfo.action";
				} else {
						myurl="productionrec/updateEnergyProgramInfo.action";
				}
				
				if(!checkInput()) return;
				myaddpanel.getForm().submit({
					method : 'POST',
					url : myurl,
					params:{	filePath : Ext.get("annex").dom.value},
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("注意", o.msg);
						if(o.msg.indexOf("成功")!=-1)
						{
							queryRecord();
						    win.hide(); 
						    bview="";
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
	
	
	function queryRecord()
	{
		store.baseParams = {
			dateYear:dateYear.getValue()
		}
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
		btnView.setVisible(false);
		myaddpanel.setTitle("增加节能总结");
	}
	
	function updateRecord()
	{
			if (grid.selModel.hasSelection()) {
		
			var records = grid.getSelectionModel().getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				  win.show(); 
				var record = grid.getSelectionModel().getSelected();
		        myaddpanel.getForm().reset();
		        myaddpanel.form.loadRecord(record);
		        Ext.get("program.commonDate").dom.value=record.get("commonDate").substring(0,10);
		        if(record.get("content")!=null&&record.get("content")!="")
		        {
		        bview=record.get("content");
		        btnView.setVisible(true);
		        Ext.get("annex").dom.value = bview.replace('/power/upload_dir/productionrec/','');
		        }
		        else
		        {
		        	  btnView.setVisible(false);
		        }
				myaddpanel.setTitle("修改节能总结");
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}
	
	function deleteRecord()
	{
		var records = grid.selModel.getSelections();
		var ids = [];
		if (records.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {

			
			for (var i = 0; i < records.length; i += 1) {
				var member = records[i];
				if (member.get("jnghzdId")) {
					ids.push(member.get("jnghzdId")); 
				} else {
					
					store.remove(store.getAt(i));
				}
			}
		
			Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(
					buttonobj) {

				if (buttonobj == "yes") {

					Ext.lib.Ajax.request('POST',
							'productionrec/deleteEnergyProgramInfo.action', {
								success : function(action) {
									Ext.Msg.alert("提示", "删除成功！")
                                   		queryRecord();
						         	
								},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
							}, 'ids=' + ids);
				}
			});
			
		
		}

	}
	
	queryRecord();
	
});