Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
  var repairId = new Ext.form.Hidden({
		id : 'repairId',
		name : 'beltRepair.repairId'
	})
	 var StartTime = new Ext.form.TextField({
		readOnly : true,
		width : 100,
		listeners : {
			focus : function(){
				WdatePicker({
					startDate : '%y-%m-%d',
					dateFmt : 'yyyy-MM-dd'
				})
			}
		}
	})
	var EndTime = new Ext.form.TextField({
		readOnly : true,
		width : 100,
		listeners : {
			focus : function(){
				WdatePicker({
					startDate : '%y-%m-%d',
					dateFmt : 'yyyy-MM-dd'
				})
			}
		}
	})
	var useTime = new Ext.form.TextField({
		id : 'useTime',
		name : 'beltRepair.useTime',
		fieldLabel : '使用时间',
		style : 'cursor:pointer',
		anchor : '90%',
		listeners : {
			focus : function(){
				WdatePicker({
					startDate : '%y-%m-%d',
					dateFmt : 'yyyy-MM-dd'
				})
			}
		}
	})
	
	var tool = new Tool({
				fieldLabel : '工具',
				hiddenName : 'beltRepair.toolId',
				anchor : '90%',
				allowBlank : false
			}, true, 3, true)
	tool.init()
	tool.confirmBtu.on('click',function(){
		tool.confirmFun();
		
	})
	// 所属部门
	var belongDep = new Ext.form.Hidden({
		id : 'belongDep',
		name : 'beltRepair.belongDep'
	})
	var belongDepName = new Power.dept(null, false, {
				fieldLabel : '所属部门',
				hiddenName : 'belongDepName',
				anchor : '90%',
				allowBlank : false
			});
	belongDepName.btnConfrim.on('click', function() {
				var deptRes = belongDepName.getValue();
				if (deptRes.code != null) {
					belongDep.setValue(deptRes.code)
				} else {
					// modified by liuyi 20100426 
//					Ext.Msg.alert('提示', '请选择虚拟根节点的下级部门!');
//					belongDepName.como.setValue(null);
					belongDepName.combo.setValue(null);
					return;
				}
			})
	var beltNumber = new Ext.form.NumberField({
		id : "beltNumber",
		name : 'beltRepair.beltNumber',
		fieldLabel : '数量',
		anchor : '90%',
		allowBlank : false

	});
	
	var result = new Ext.form.TextField({
		id  : 'repairResult',
		name : 'beltRepair.repairResult',
		allowBlank : false,
		fieldLabel : '检验结果',
		anchor : '90%'
	})
	var repairDep = new Ext.form.Hidden({
		id : 'repairDep',
		name : 'beltRepair.repairDep'
	})
	var repairDepName = new Ext.form.TextField({
		id : 'repairDepName',
		allowBlank:false,
		disabled : true,
		fieldLabel : '检验部门',
		anchor : '90%'
	})
	// 检验人  
	var rePeople = new Power.person({
			id : "repairBy",
			fieldLabel : '检验人',
			allowBlank:false,
			hiddenName : 'beltRepair.repairBy',
			anchor : '90%'
		}, {
			selectModel : 'single'
		})
		rePeople.btnConfirm.on('click', function() {
			var personRes = rePeople.chooseWorker();
			if (personRes) {
				repairDep.setValue(personRes.get("deptCode"));
				repairDepName.setValue(personRes.get("deptName"))
			} else {
				repairDep.setValue(null);
				repairDepName.setValue(null);
			}
	})
	
	
	
	var repairBegin = new Ext.form.TextField({
		id : 'repairBegin',
		name : 'beltRepair.repairBegin',
		fieldLabel : '检修开始时间',
		style : 'cursor:pointer',
		anchor : '90%',
		listeners : {
			focus : function(){
				WdatePicker({
					startDate : '%y-%m-%d',
					dateFmt : 'yyyy-MM-dd'
				})
			}
		}
	})
	var repairEnd = new Ext.form.TextField({
		id : 'repairEnd',
		name : 'beltRepair.repairEnd',
		fieldLabel : '检修结束时间',
		style : 'cursor:pointer',
		anchor : '90%',
		listeners : {
			focus : function(){
				WdatePicker({
					startDate : '%y-%m-%d',
					dateFmt : 'yyyy-MM-dd'
				})
			}
		}
	})
	var nextTime = new Ext.form.TextField({
		id : 'nextTime',
		name : 'beltRepair.nextTime',
		fieldLabel : '下次检修时间',
		style : 'cursor:pointer',
		anchor : '90%',
		listeners : {
			focus : function(){
				WdatePicker({
					startDate : '%y-%m-%d',
					dateFmt : 'yyyy-MM-dd'
				})
			}
		}
	})
	var memo = new Ext.form.TextArea({
		id : 'memo',
		name : 'beltRepair.memo',
		fieldLabel : '备注',
		anchor : '95%',
		height : 75
	})
	
	var saveBtu = new Ext.Button({
		id : 'saveButton',
		text : '保存',
		iconCls : 'save',
		handler : saveBeltRepair
	})
	var cancelBtu = new Ext.Button({
		id : 'canButton',
		text : '取消',
		iconCls : 'cancer',
	    handler : function() {
					win.hide(); 
				}
	})
	
  var formPanel = new Ext.form.FormPanel({
		id : 'formPanel',
		frame : true,
		border : false,
		layout : 'column',
		buttons : [saveBtu,cancelBtu],
		buttonAlign : 'center',
		labelAlign : 'right',
		labelWidth : 80,
		defaults : {
			layout : 'form',
			frame : false,
			border : false
		},
		items : [{
			columnWidth : 0.5,
			items : [tool.combo,repairId,repairBegin,repairEnd,nextTime,useTime,beltNumber]
		},{
			columnWidth : 0.5,
			items : [result,belongDep,belongDepName.combo,rePeople.combo,repairDep,repairDepName,beltNumber]
		},{
			columnWidth : 1,
			items : [memo]
		}]
	})
	
	var win = new Ext.Window({
		width : 600,
		height : 330,
		modal:true,
		closeAction : 'hide',
		items : [formPanel]
	});
	var beltRecord = new Ext.data.Record.create([
	{
		name : 'repairId',
		mapping : 0
	}, {
		name : 'toolId',
		mapping : 1
	}, {
		name : 'useTime',
		mapping : 2
	}, {
		name : 'belongDep',
		mapping:3
	}, {
		name : 'belongDepName',
		mapping : 4
	}, {
		name : 'beltNumber',
		mapping : 5
	}, {
		name : 'repairResult',
		mapping : 6
	}, {
		name : 'repairBy',
		mapping : 7
	}, {
		name : 'repairByName',
		mapping : 8
	}, {
		name : 'repairDep',
		mapping : 9
	}, {
		name : 'repairDepName',
		mapping : 10
	}, {
		name : 'repairBegin',
		mapping : 11
	}, {
		name : 'repairEnd',
		mapping : 12
	}, {
		name : 'nextTime',
		mapping : 13
	}, {
		name : 'memo',
		mapping : 14
	}, {
		name : 'toolName',
		mapping : 15
	} ]);
	
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'security/findBeltRepairList.action',
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, beltRecord)
	});
	
	var sm= new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) { }
		}
	});

	var colModel = new Ext.grid.ColumnModel([	
	sm,new Ext.grid.RowNumberer({
                header : '行号',
                width : 35
            }),{
			id : 'repairId',
			header : 'ID',
			hidden:true,
			dataIndex : 'repairId',
			sortable : true,
			width : 100
		},{
		id : 'useTime',
		header : '使用时间',
		dataIndex : 'useTime',
		width : 100,
		sortable : true
	}, {
		header : '所属部门',
		dataIndex : 'belongDepName',
		width : 110
	}, 
	{
		header : '数量',
		dataIndex : 'beltNumber',
		width : 100
		
	},{
		header : '工具名称',
		dataIndex : 'toolName',
		width : 100
		
	}, {
		header : '检验结果',
		dataIndex : 'repairResult',
		width : 150
	},
	{
		id : 'repairByName',
		header : '检修人',
		dataIndex : 'repairByName',
		width : 100,
		sortable : true
	}, {
		header : '检修部门',
		dataIndex : 'repairDepName',
		width : 110
	}, 
	{
		header : '检验开始时间',
		dataIndex : 'repairBegin',
		width : 100
		
	}, {
		header : '检验结束时间',
		dataIndex : 'repairEnd',
		width : 100
	},
	{
		header : '下次检验时间',
		dataIndex : 'nextTime',
		width : 100
		
	}, {
		header : '备注',
		dataIndex : 'memo',
		width : 150
	}]);
	function  updateBeltRepair()
	{
	if (sm.hasSelection()) {
					if (sm.getSelections().length > 1)
						Ext.Msg.alert('提示', '请选择其中一条数据!');
					else {
						win.setTitle('修改安全带清册检修记录')
						win.show();
						formPanel.getForm().loadRecord(sm.getSelected())
						tool.setValue(sm.getSelected().get('toolId'),sm.getSelected().get('toolName'));
						rePeople.setValue(sm.getSelected().get('repairBy'), sm
								.getSelected().get('repairByName'));
					}
				} else
					Ext.Msg.alert('提示', '请先选择要修改的数据！')
}
	function  saveBeltRepair()
	{
		// modified by liuyi 20100426 
		if(belongDepName.combo.getValue() == 0){
			Ext.Msg.alert('提示','请选择一具体部门');
			return;
		}	
		{
				var myurl = "";
				if (repairId.getValue() == ""||repairId.getValue()==null) {
					myurl = "security/addBeltRepair.action";
				} else {
					myurl = "security/updateBeltRepair.action";
				}
				
				
			
				formPanel.getForm().submit({
					method : 'POST',
					url : myurl,
					params : {
						nextTime : nextTime.getValue(),
						useTime : useTime.getValue(),
						repairBegin : repairBegin.getValue(),
						repairEnd : repairEnd.getValue()
					},
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("注意", o.msg);
						if (o.msg.indexOf("成功") != -1) {
							win.hide();
						  ds.reload();
						}
					},
					faliue : function() {
						Ext.Msg.alert('错误', '出现未知错误.');
					}
				});
			}
	}
		function delBeltRepair() {
		if (sm.hasSelection()) {
			Ext.Msg.confirm('提示', '确认要删除数据吗？', function(buttonId) {
				if (buttonId == 'yes') {
					var ids = new Array();
					var selects = sm.getSelections();
					for (var i = 0; i < selects.length; i++) {
						ids.push(selects[i].get('repairId'))
					}
					if (ids.length > 0) {
						Ext.Ajax.request({
									url : 'security/deleteBeltRepair.action',
									method : 'post',
									params : {
										ids : ids.join(",")
									},
									success : function(response, options) {
										if (response && response.responseText) {
											var res = Ext
													.decode(response.responseText)
											Ext.Msg.alert('提示', res.msg);
											ds.reload();
										}
									},
									failure : function(response, options) {
										Ext.Msg.alert('提示', '删除数据出现异常！')
									}
								})
					}
				}
			})

		} else
			Ext.Msg.alert('提示', '请先选择要删除的数据！')
	}
	colModel.defaultSortable = true;
	function  query()
	{
		ds.reload();
	}
	var tbar = new Ext.Toolbar({
		items : ['检验开始时间',StartTime,'至:',EndTime,{
			id : 'query2',
			text : "查询",
			iconCls : 'query',
			handler : function() {
				
				query();
			}
		}, '-',{
			id : 'btnAdd',
			text : "新增",
			iconCls : 'add',
			handler : function() {
				win.setTitle('新增安全清册检修记录');
				win.show();
				formPanel.getForm().reset();
			}
		}, '-', {
			id : 'btnupdate',
			text : "修改",
			iconCls : 'update',
			handler : updateBeltRepair
		}, '-', {
			id : 'btnDelete',
			text : "删除",
			iconCls : 'delete',
			handler : delBeltRepair
		}]
	});
	var bbar = new Ext.PagingToolbar({
						pageSize : 18,
						store : ds,
						displayInfo : true,
						displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
						emptyMsg : "没有记录"
					})
	
	var beltGrid = new Ext.grid.GridPanel({
				id : 'belt-grid',
				autoScroll : true,
				ds : ds,
				cm : colModel,
				sm : sm,
				tbar : tbar,
				bbar:bbar,
				border : true
			});
	beltGrid.on('rowdblclick',updateBeltRepair);
	var viewport = new Ext.Viewport({
		region : "center",
		layout : 'fit',
		autoWidth:true,
		autoHeight:true,
		fitToFrame : true,
		items : [beltGrid]
	});
	ds.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							beginTime : StartTime.getValue(),
							endTime : EndTime.getValue()
							
						})
			});
	ds.load({
		params : {
			start : 0,
			limit : 18

		}
	})
	
});