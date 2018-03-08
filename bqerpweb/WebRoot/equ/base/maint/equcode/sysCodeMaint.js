Ext.onReady(function() {

	
	// 定义grid
var MyRecord = Ext.data.Record.create([
	{name : 'equClassId'},
    {name : 'classCode'},
	{name : 'className'},
	{name : 'remark'}
	]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url:'equ/findSysList.action'
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
		layout : 'fit',
		// region:"east",
		store : store,

		columns : [// 设计页面显示的表格信息，字段与Ext.data.Record.create匹配
		sm, {
			
			header : "ID",
			width : 75,
			sortable : true,
			dataIndex : 'equClassId',
			hidden:true
		},
		{
			header : "系统编码",
			width : 75,
			sortable : true,
			dataIndex : 'classCode'
		},
		{
			header : "系统名称",
			width : 75,
			sortable : true,
			dataIndex : 'className'
		},
		{
			header : "备注",
			width : 75,
			sortable : true,
			dataIndex : 'remark'
		}
		],
		sm : sm,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : true
		},
		tbar : [fuuzy, {
			text : "查询",
			 iconCls : 'query',
			handler : queryRecord
		},
		{
			text : "新增",
            iconCls : 'add',
			handler : addRecord
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
		items : [grid]
	});

	// -------------------

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
	
	// -----删除----------
	function deleteRecord() {
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		var names=[];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {

			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.equClassId) {
					ids.push(member.equClassId); 
					names.push(member.className);
				} else {
					
					store.remove(store.getAt(i));
				}
			}

			// alert(ids[0]);

			Ext.Msg.confirm("删除", "是否确定删除名称为'" + names + "'的记录？", function(
					buttonobj) {

				if (buttonobj == "yes") {

					Ext.lib.Ajax.request('POST',
							'equ/deleteClass.action', {
								success : function(action) {
									Ext.Msg.alert("提示", "删除成功！")
	
									store.reload();
									//queryRecord();
								},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
							}, 'ids=' + ids);
				}

			});
		}

	};

	// 创建增加和修改的页面
	
		function createupdatepanel(mytitle,myid) {
			
			
	 var equClassId = {
		id : "equClassId",
		xtype : "textfield",
		fieldLabel : '编号',
		value:'自动生成',
		readOnly:true,
		name : 'equClassId',
		anchor : '80%'
	}
		 var classCode = {
		id : "classCode",
		xtype : "textfield",
		fieldLabel : '系统编码',
		allowBlank:false,
		name : 'equCclass.classCode',
		maxLength:3,
		anchor : '80%'
	}
	
		 var className = {
		id : "className",
		xtype : "textfield",
		fieldLabel : '系统名称',
		name : 'equCclass.className',
		anchor : '80%'
	}
	
		 var remark = {
		id : "remark",
		xtype : "textfield",
		fieldLabel : '备注',
		name : 'equCclass.remark',
		anchor : '80%'
	}
			

		var myaddpanel = new Ext.FormPanel({
			frame : true,
			labelAlign : 'center',
			title : mytitle,
			items : [equClassId,classCode,className,remark]

		});
	
	
//  		if(myid!='自动生成')
//  		{
//  			//修改时显示信息
//	  				myaddpanel.form.load({ 
//						url : "findblock.action?id=" + myid
//
//			});
//  		}
		
		return myaddpanel;
	}
	
	
	//创建增加和修改窗口
		function createaddwin(mypanel, op) {

		var win = new Ext.Window({
			width : 400,
			height : 200,
			buttonAlign : "center",
			items : mypanel,
			layout:'fit',
			draggable : true,
			modal : true, 
			buttons : [{
				text : '保存',
				iconCls : 'save',
				handler : function() {
					// alert(op);
				mypanel.form.submit({
						method : 'POST',
						url : 'equ/saveSysCode.action?method=' + op,
						success : function(form,action) {
							//---add----
							//alert(action.response.responseText);
							var o = eval("(" + action.response.responseText + ")");
							Ext.Msg.alert("注意", o.msg);
							//----------
							
							win.close();
							grid.store.reload();
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
					win.close();
				}
			}]

		});
		return win;

	}





//增加学位信息
	function addRecord() {
			if (!myaddpanel) {
			var myaddpanel = createupdatepanel("增加系统编码","自动生成");
		}
		if (!win) {
			var win = createaddwin(myaddpanel, "add");
		}
		win.setPosition(200, 100);
		win.show();

	};

	// -----------------修改-----------------
	function updateRecord() {
		
			if (grid.selModel.hasSelection()) {
		
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				var record = grid.getSelectionModel().getSelected();
			
				if (!myaddpanel) {
					var myaddpanel = createupdatepanel("修改系统编码",record.get("id"));
				
				}
				if (!win) {
					var win = createaddwin(myaddpanel, "update");
				}

				win.setPosition(200, 100);
				win.show();
				myaddpanel.form.loadRecord(record);
						
          
				
				
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	
	}

});