Ext.onReady( function (){
	var selct_mod = new Ext.grid.CheckboxSelectionModel({
		singleSelect:false 
	});
var MyRecord = Ext.data.Record.create([{

						name : 'trainingTypeId'
					}, {
						name : 'trainingTypeName'
					}, {
						name : 'memo'
					}
						
					]);
					
var cm = new Ext.grid.ColumnModel([selct_mod, 
	new Ext.grid.RowNumberer({
		header : '行号',
		width : 35
	}),{
		header : '序号',
		dataIndex : 'trainingTypeId',
		width : 100,
		hidden : true,
		align : 'left'
	}, {
		header : '培训计划类别',
		dataIndex : 'trainingTypeName',
		width : 200,
		align : 'left'
	}, {
		header : '备注',
		dataIndex : 'memo',
		width : 250,
		align : 'left'
			
	}]);
	var typeId = new Ext.form.Hidden({
		id : "trainingTypeId",
		fieldLabel : 'ID',
		anchor : '95%',
		readOnly : true,
		value : '',
		name : 'type.trainingTypeId'

	});
	var typeName = new Ext.form.TextField({
		id : "trainingTypeName",
		fieldLabel : '培训计划类别',
		allowBlank : false,
		anchor : '95%',
		name : 'type.trainingTypeName',
		emptyText : '请输入文本...'
	});

	var memo = new Ext.form.TextArea({
		id : "memo",
		fieldLabel : '备注',
//		emptyText : '请输入文本...',
		anchor : '95%',
		name : 'type.memo',
		allowscroll:true ,
		height : 80
	});
	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		labelWidth : 80,
		closeAction : 'hide',
		title : '培训计划类别增加/修改',
		items : [typeId, typeName, memo]
	});
	var win = new Ext.Window({
		width : 350,
		height : 200,
		buttonAlign : "center",
		items : [myaddpanel],
		layout : 'fit',
		closeAction : 'hide',
//		draggable : true,
//		modal : true,
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
						var myurl = "";
						if (Ext.get("trainingTypeId").dom.value == ""
								|| Ext.get("trainingTypeId").dom.value == null) {
							var passmethod = "add"
							myurl = "manageplan/addTrainPlanType.action";

						} else {
							var passmethod = "update"
							myurl = "manageplan/addTrainPlanType.action";

						}
						if (!checkInput())
							return;
							
						myaddpanel.getForm().submit({
									method : 'POST',
									url : myurl,
									params : {
										passmethod : passmethod
									},
									success : function(form, action) {
										if (action.result.existFlag == true) {
											// modified by liuyi 091214  增加修改时的唯一性提示
//											if (passmethod == "add")
												Ext.Msg.alert("提示",
														"该内容已存在 ,请重新输入!");
										} else {
											Ext.Msg.alert("提示", "操作成功");
										}
//										store.load({
//													params : {
//														start : 0,
//														limit : 20
//													}
//												});
										store.reload()
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
	function updatedata() {
		if (Grid.selModel.hasSelection()) {
			
			var records = Grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				var record = Grid.getSelectionModel().getSelected();
//				alert(Ext.encode(record.data))
//				alert(record.data.trainingTypeId)
//				
//				typeId.setValue(record.trainingTypeId)
//				typeName.setValue(record.data.trainingTypeName)
//				memo.setValue(record.data.memo)
				//win.setPosition(200, 100);
				win.show();
				myaddpanel.getForm().loadRecord(record);
				myaddpanel.setTitle("培训计划类别修改");
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}
	
	function checkInput() {
		var msg = "";
		if (typeName.getValue() == "") {
			msg = "'培训计划类别名称'";
		}
		if (msg != "") {
			Ext.Msg.alert("提示", "请输入" + msg);
			return false
		} else {
			return true;
		}
	}
			var dataProxy = new Ext.data.HttpProxy({
						url : 'manageplan/getTrainPlanList.action'
					});

			var theReader = new Ext.data.JsonReader({
						root : "list",
						totalProperty : "totalCount"
					}, MyRecord);

			var store = new Ext.data.Store({
						proxy : dataProxy,
						reader : theReader
					});

			store.load({
						params : {
							start : 0,
							limit : 18
							
						}

})


function adddata (){

		myaddpanel.getForm().reset();
		win.show();
		myaddpanel.setTitle("培训计划类别增加");


}
function  deletedata(){ 
		var sm = Grid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		var names = [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.trainingTypeId) {
					ids.push(member.trainingTypeId);
				} else {
					store.remove(store.getAt(i));
				}
			}
			Ext.Msg.confirm("删除", "是否确定删除所选记录？", function(
					buttonobj) {
				if (buttonobj == "yes") {
					Ext.lib.Ajax.request('POST',
							'manageplan/delTrainPlanType.action', {
								success : function(action) {
									Ext.Msg.alert("提示", "删除成功！")
//									store.load({
//										params : {
//											start : 0,
//											limit : 20
//										}
//									});
									store.reload();
								},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
							}, 'ids=' + ids);
				}
			});
		}
	}





var selbar = new Ext.Toolbar({
		items : [ {
			id : 'btnadd',
			iconCls : 'add',
			text : "增加",
			handler : adddata
		}, '-', {
			id : 'btnupdate',
			iconCls : 'update',
			text : "修改",
			handler : updatedata
		}, '-', {
			id : 'btndelete',
			iconCls : 'delete',
			text : "删除",
			handler : deletedata
		}]
		})
		

var Grid = new Ext.grid.GridPanel({
		viewConfig : {
			forceFit : true
		},
		
		store : store,
		cm : cm,
		sm:selct_mod,
		height : 425,
		split : true,
		autoScroll : true,
		layout : 'fit',
		// bbar : gridbbar,
		tbar : selbar,
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});
	Grid.on("rowdblclick", updatedata);
	new Ext.Viewport({
		layout : "fit",
		border : false,
		frame : false,
		items : [Grid]
	});

})