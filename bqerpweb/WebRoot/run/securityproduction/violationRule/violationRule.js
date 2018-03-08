// isQuery  是否为选择 true:是  
// type 类别
SpRepair = function(isQuery,type){
	
	
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t;
		return s;
	};
	
	var label1 = new Ext.form.Label({
		text : '考核时间：'
	})
	var queryStartTime = new Ext.form.TextField({
		id : 'startTime',
		readOnly : true,
		width : 80,
		listeners : {
			focus : function(){
				WdatePicker({
					startDate : '%y-%m-%d',
					dateFmt : 'yyyy-MM-dd'
				})
			}
		}
	})
	
	

	
	
	var queryBtu = new Ext.Button({
		id : 'queryBtu',
		text : '查询',
		iconCls : 'query',
		handler : queryFun
	})
	function queryFun() {
		repairStore.load({
					params : {
						start : 0,
						limit : 18
					}
				})
	}
	var addBtu = new Ext.Button({
		id : 'addBtu',
		text : '新增',
		iconCls : 'add',
		handler : addFun
	})
	function addFun(){
		editWin.setTitle('新增反违章管理记录')
		editWin.show();
	
		formPanel.getForm().reset();
			getWorkCode();
	}
	var updateBtu = new Ext.Button({
		id : 'updateBtu',
		text  : '修改',
		iconCls  : 'update',
		handler : updateFun
	})
	function updateFun(){
		if(sm.hasSelection()){
			if(sm.getSelections().length > 1)
				Ext.Msg.alert('提示','请选择其中一条数据!');
			else
			{
				editWin.setTitle('修改反违章管理记录')
				editWin.show();
				formPanel.getForm().loadRecord(sm.getSelected())
//				tool.setValue(sm.getSelected().get('toolId'),sm.getSelected().get('toolName'));
//				toolType.setValue('电气安全用具')
				txtEmpCode.setValue(sm.getSelected().get('empCode'),sm.getSelected().get('empName'));
				txtCheckBy.setValue(sm.getSelected().get('checkBy'),sm.getSelected().get('checkName'))

			}
		}else
			Ext.Msg.alert('提示','请先选择要修改的数据！')
	}
	var saveBtu = new Ext.Button({
		id : 'saveBtu',
		text : '保存',
		iconCls : 'save',
		handler : saveFun
	})
	function saveFun(){
		if(formPanel.getForm().isValid()){
			
			Ext.Msg.confirm("提示",'确认要保存数据吗？',function(buttonId){
				if(buttonId == 'yes'){
					var myUrl="";
					if(ruleId.getValue()=="") myUrl="security/saveRuleInfo.action";
					else myUrl="security/updateRuleInfo.action";
					
					formPanel.getForm().submit({
						url : myUrl,
						method : 'post',
						success : function(form,action){
							if(action && action.response && action.response.responseText)
							{
								var res = Ext.decode(action.response.responseText)
								Ext.Msg.alert('提示',res.msg)
							}
							editWin.hide();
							queryFun();
						},
						failure : function(form,action){
							Ext.Msg.alert('提示','保存出现异常！')
						}
					})
				}
			})
		}
	}
	var cancelBtu = new Ext.Button({
		id  : "cancelBtu",
		text : '取消',
		iconCls : 'cancer',
		handler : cancelFun
	})
	function cancelFun(){
		formPanel.getForm().reset()
		editWin.hide()
	}
	var deleteBtu = new Ext.Button({
		id : 'deleteBtu',
		text : '删除',
		iconCls : 'delete',
		handler : deleteFun
	})
	function deleteFun() {
		if (sm.hasSelection()) {
			Ext.Msg.confirm('提示', '确认要删除数据吗？', function(buttonId) {
				if (buttonId == 'yes') {
					var ids = new Array();
					var selects = sm.getSelections();
					for (var i = 0; i < selects.length; i++) {
						ids.push(selects[i].get('ruleId'))
					}
					if (ids.length > 0) {
						Ext.Ajax.request({
									url : 'security/deleteRuleInfo.action',
									method : 'post',
									params : {
										ids : ids.join(",")
									},
									success : function(response, options) {
										if (response && response.responseText) {
											var res = Ext
													.decode(response.responseText)
											Ext.Msg.alert('提示', res.msg);
											queryFun()
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

	
	
	var tbar = new Ext.Toolbar({
		items : [label1,queryStartTime,queryBtu,addBtu,updateBtu,deleteBtu]
	})
	var RepairRecord = new Ext.data.Record.create([{
		name : 'ruleId',
		mapping : 0
	},{
		name : 'empCode',
		mapping :1
	},{
		name : 'empName',
		mapping :2
	},{
		name : 'deptCode',
		mapping :3
	},{
		name : 'deptName',
		mapping :4
	},{
		name : 'examineDate',
		mapping :5
	},{
		name : 'examineMoney',
		mapping :6
	},{
		name : 'phenomenon',
		mapping :7
	},{
		name : 'checkBy',
		mapping :8
	},{
		name : 'checkName',
		mapping :9
	},{
		name : 'entryDate',
		mapping :10
	}])
	
	var repairStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'security/findRuleList.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list',
			totalProperty : 'totalCount'
		},RepairRecord)
		
	})
	repairStore.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							strDate : queryStartTime.getValue()
						})
			})
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	})
	
	var cm = new Ext.grid.ColumnModel([sm,new Ext.grid.RowNumberer({
		header : '行号',
		width : 35
	}),{
		header : '姓名',
		dataIndex : 'empName'
	},{
		header : '部门',
		dataIndex : 'deptName'
	},{
		header : '考核时间',
		dataIndex : 'examineDate'
	},{
		header : '违章现象',
		dataIndex : 'phenomenon'
	},{
		header : '考核（元）',
		dataIndex : 'examineMoney'
	},{
		header : '查处人',
		dataIndex : 'checkName'
	}])

	var bbar = new Ext.PagingToolbar({
						pageSize : 18,
						store : repairStore,
						displayInfo : true,
						displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
						emptyMsg : "没有记录"
					})
	var grid = new Ext.grid.GridPanel({
		id  : 'grid',
		frame : 'true',
		border : false,
		layout : 'fit',
		autoScroll : true,
		sm : sm,
		store : repairStore,
		cm : cm,
		tbar : tbar,
		bbar : bbar
	})
	
	
	// 检修记录id
	var ruleId = new Ext.form.Hidden({
		id : 'ruleId',
		name : 'rule.ruleId'
	})
	
	var txtEmpCode = new Power.person({
				fieldLabel : '姓名',
				hiddenName : 'rule.empCode',
				anchor : '90%'
			}, {selectModel:'single'})
	txtEmpCode.btnConfirm.on('click',function(){
		var personRes = txtEmpCode.chooseWorker();
		if(personRes){
			deptCodeH.setValue(personRes.get("deptCode"));
			txtDeptName.setValue(personRes.get("deptName"))
		}else{
			deptCodeH.setValue(null);
			txtDeptName.setValue(null)
		}
	})
	
	var deptCodeH = new Ext.form.Hidden({
		id :'deptCode',
		name : 'rule.deptCode'
	})
	var txtDeptName = new Ext.form.TextField({
		id : 'deptName',
		disabled : true,
		fieldLabel : '部门',
		anchor : '90%'
	})
	
	
	var txtExamineDate = new Ext.form.TextField({
		id : 'examineDate',
		name : 'rule.examineDate',
		fieldLabel : '考核时间',
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
	});
	
	var txtExamineMoney=new Ext.form.NumberField({
		id:'examineMoney',
	    name:'rule.examineMoney',
	    fieldLabel : '考核金额',
		anchor : '90%'
	});
	
		var txtCheckBy = new Power.person({
				fieldLabel : '查处人',
				hiddenName : 'rule.checkBy',
				anchor : '90%'
			}, {selectModel:'single'})
	txtCheckBy.btnConfirm.on('click',function(){
		var personRes = txtCheckBy.chooseWorker();
		
	})
	// 填写时间
	var txtEntryDate = new Ext.form.TextField({
		id : 'entryDate',
		name : 'rule.entryDate',
		fieldLabel : '填写时间',
		style : 'cursor:pointer',
		anchor : '90%',
		listeners : {
			focus : function(){
				WdatePicker({
					startDate : '%y-%m-%d',
					dateFmt : 'yyyy-MM-dd'
				})
			}
		},
	  value : getDate()
	})
	
	

	//备注
	var txtPhenomenon = new Ext.form.TextArea({
		id : 'phenomenon',
		name : 'rule.phenomenon',
		fieldLabel : '违章现象',
		anchor : '94%',
		height : 120
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
			items : [ruleId,txtEmpCode.combo,txtExamineDate]
		},{
			columnWidth : 0.5,
			items : [deptCodeH,txtDeptName,txtExamineMoney]
		},{
			columnWidth : 1,
			items : [txtPhenomenon]
		},{
			columnWidth : 0.5,
			items : [txtCheckBy.combo]
		},{
			columnWidth : 0.5,
			items : [txtEntryDate]
		}]
	})
	
	var editWin = new Ext.Window({
		id : 'editWin',
		width : 500,
		height : 300,
		items : [formPanel],
		modal : true,
		closeAction  : 'hide'
	})
	
	
	var repairStoreCopy = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'security/findToolsRepairObject.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list',
			totalProperty : 'totalCount'
		},RepairRecord)
		
	})
	repairStoreCopy.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							beginTime : queryStartTime.getValue(),
							endTime : queryEndTime.getValue(),
							toolCode : queryCode.getValue(),
							toolType : type,
							isMaint : (isQuery != null && isQuery) ? null : "1"
						})
			})
	
	
	this.grid = grid;
	this.init = function(){
		if(isQuery != null && isQuery){
			addBtu.setVisible(false)
			updateBtu.setVisible(false)
			deleteBtu.setVisible(false)
			grid.purgeListeners()
		}else{
		
			grid.on('rowdblclick',updateFun)
		}
		queryFun();
	};
	
		function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result.workerCode) {
					// 设定默认工号
					txtCheckBy.setValue(result.workerCode,result.workerName)
				}
			}
		});
	}
}