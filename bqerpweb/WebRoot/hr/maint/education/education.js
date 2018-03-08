Ext.onReady(function() {	
	// 定义grid
var MyRecord = Ext.data.Record.create([
	{name : 'educationId'},
    {name : 'educationName'},
	{name : 'isUse'},
	{name : 'retrieveCode'}
	]);

	var dataProxy = new Ext.data.HttpProxy(
			{
				url:'eduInfoManage.action?method=getlist'
			}

	);

	var theReader = new Ext.data.JsonReader({
		root : "root",
		totalProperty : "total"

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
	
	//store.load();



	var sm = new Ext.grid.CheckboxSelectionModel();

	var grid = new Ext.grid.GridPanel({
		region : "center",
		layout : 'fit',
		// region:"east",
		store : store,

		columns : [// 设计页面显示的表格信息，字段与Ext.data.Record.create匹配
		sm, {
			
			header : "学历编码",
			sortable : true,
			hidden:true,
			dataIndex : 'educationId'
		},

		{
			header : "学历名称",
			width : 200,
			sortable : true,
			dataIndex : 'educationName'
		},

		{
			header : "使用标志",
			width : 100,
			sortable : true,
			dataIndex : 'isUse'
		},
		{
			header : "检索码",
			width : 100,
			sortable : true,
			dataIndex : 'retrieveCode'
		}

		],
		sm : sm,
		tbar : [
		
		{
			text : "新增学历",
            iconCls : 'add',
			handler : addRecord

	
		}, {
			text : "修改学历",
			iconCls : 'update',
			handler : updateRecord
		}, {
			text : "删除学历",
			iconCls : 'delete',
			handler : deleteRecord
		}],
		//分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录",
			beforePageText:'页',
			afterPageText:"共{0}页"
		})
	});
   grid.on("dblclick", updateRecord); //双击修改

//---------------------------------------

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [grid]
	});

	// -------------------


	
	// -----删除----------
	function deleteRecord() {
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		var names= [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {

			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.educationId) {
					ids.push(member.educationId); 
					names.push(member.educationName);
				} else {
					
					store.remove(store.getAt(i));
				}
			}

			// alert(ids[0]);

			Ext.Msg.confirm("删除", "是否确定删除学历名称为'" + names + "'的记录？", function(
					buttonobj) {

				if (buttonobj == "yes") {

					Ext.lib.Ajax.request('POST',
							'eduInfoManage.action?method=delete', {
								success : function(action) {
									Ext.Msg.alert("提示", "删除成功！")

									store.reload();
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
	
		function createupdatepanel(mytitle,id) {
			
			
	 var educationId = {
		id : "educationId",
		xtype : "textfield",
		fieldLabel : '学历编号',
		value:'自动生成',
		readOnly:true,
		anchor :'%90',
		name : 'eduInfo.educationId'	
	}
		 var educationName = new Ext.form.TextField({
		id : "educationName",
		xtype : "textfield",
		fieldLabel : '学历名称',
		allowBlank:false,
		anchor :'%90',
		name : 'eduInfo.educationName'	
	});
		 var retrieveCode = {
		id : "retrieveCode",
		xtype : "textfield",
		fieldLabel : '检索码',
		anchor :'%90',
		name : 'eduInfo.retrieveCode'	
	}
		educationName.on("change", function(filed, newValue,
				oldValue) {

			Ext.Ajax.request({
				url : 'manager/getRetrieveCode.action',
				params : {
					name : newValue
				},
				method : 'post',
				success : function(result, request) {
					var json = result.responseText;
					var o = eval("(" + json + ")");
					Ext.get("retrieveCode").dom.value = o.substring(0, 8);
				}
			});
		});
			

		var myaddpanel = new Ext.FormPanel({
			frame : true,
			labelAlign : 'center',
			title : mytitle,
			items : [educationId,educationName,retrieveCode,
				{
			id : 'isUse',
			layout : 'column',
			isFormField : true,
			fieldLabel : '状态',
			border : false,
			items : [{
				columnWidth : .3,
				border : false,
				items : new Ext.form.Radio({
					id:'U',
					boxLabel : '使用',
					
					name : 'eduInfo.isUse',
					inputValue : 'U',
					checked : true
				})
			}, {
				
				columnWidth : .3,
				border : false,
				
				items : new Ext.form.Radio({
					id:'N',
					boxLabel : '停用',
					name : 'eduInfo.isUse',
					inputValue : 'N'
				})
			}, {
				columnWidth : .3,
				border : false,
			
				items : new Ext.form.Radio({
						id:'L',
					boxLabel : '注销',
					name : 'eduInfo.isUse',
					inputValue : 'L'
				})
			}]
		}]

		});
	
		
  		if(id!='自动生成')
  		{
  			//修改时显示信息
  				myaddpanel.form.load({

				url : "eduInfoManage.action?method=getdata&id=" + id,
				success : function(form,action) {
					var o = eval("(" + action.response.responseText + ")");
					//
						Ext.getCmp(o.data.isUse).setValue(true);
					   
				}

			});
  		}
		
		return myaddpanel;
	}
	
	
	//创建增加和修改窗口
		function createaddwin(mypanel, op) {

		var win = new Ext.Window({
			width : 350,
			height : 200,
			buttonAlign : "center",
			items : mypanel,
			layout:'fit',
			modal : true,
			buttons : [{
				text : '保存',
				iconCls : 'save',
				handler : function() { 
				if(mypanel.getForm().isValid())
				{
				mypanel.form.submit({
						method : 'POST',
						url : 'eduInfoManage.action?method=' + op,
						success : function(form,action) { 
							var o = eval("(" + action.response.responseText + ")");
							Ext.Msg.alert("注意", o.msg); 
							win.close();
							grid.store.reload();
						},
						failure : function(form,action) {
							var o = eval("(" + action.response.responseText + ")");
							Ext.Msg.alert('错误', o.errMsg);
						}
						
					});}
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
			var myaddpanel = createupdatepanel("增加学历信息","自动生成");
		}
		if (!win) {
			var win = createaddwin(myaddpanel, "add");
		}
		
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
					var myaddpanel = createupdatepanel("修改学历信息",record.get("educationId"));
				
				}
				if (!win) {
					var win = createaddwin(myaddpanel, "update");
				}

				
				win.show();
				//myaddpanel.form.loadRecord(record);
						
          
				
				
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	
	}

});