Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	// 定义grid
	Ext.ns("PrjCheck.meetSign");
	PrjCheck.meetSign=function ()
	{
		
		
var prjRegister = new maint.prjRegList();
	
//立项名称
var prjReg = new Ext.form.ComboBox({
	fieldLabel:'立项名称',
	store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
	}),
	mode : 'remote',
	anchor : '80%',
	editable : false,
	triggerAction : 'all',
		onTriggerClick : function() {
				prjRegister.win.show();
	}
});

var prjId = new Ext.form.Hidden({
	name:'prjMeetSign.prjId'
})

prjRegister.win.on('hide',function(){
	var value = prjRegister.returnValue;
	prjReg.setValue(value.prjName);
	prjId.setValue(value.prjId);
});

		var SignId = new Ext.form.Hidden({
				id : "checkSignId",
				name : "prjMeetSign.checkSignId"
			})
		var reportCode = new Ext.form.TextField({
			id : "reportCode",
			xtype : "textfield",
			fieldLabel : '编号',
			anchor : '80%',
			name : 'prjMeetSign.reportCode'
			
		});
		 var budgetCost = new Ext.form.NumberField({
			id : "budgetCost",
			fieldLabel : '预算费用',
			anchor : '80%',
			name : 'prjMeetSign.budgetCost',
			allowBlank : false,
			allowDecimals : true,
		    decimalPrecision : 2
			
		});
		var starDate = new Ext.form.TextField({
		id : 'startDate',
		name : 'prjMeetSign.startDate',
		fieldLabel : "开工时间",
		style : 'cursor:pointer',
		anchor : '80%',
		cls : 'Wdate',
		value : '',
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true
				});
			}
		}
	});
	
	var endDate = new Ext.form.TextField({
		id : 'endDate',
		name : 'prjMeetSign.endDate',
		fieldLabel : "竣工时间",
		style : 'cursor:pointer',
		anchor : '80%',
		cls : 'Wdate',
		value : '',
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true
				});
			}
		}
	});
		
	
	//合同ID
		var conId = new Ext.form.Hidden({
				id : "conId",
				name : "prjMeetSign.conId"
			})
		
		//合同名称
		var conNameCom = new Ext.form.ComboBox({
		name : 'prjMeetSign.conName',
		fieldLabel : "合同名称",
		mode : 'remote',
		editable : true,
		allowBlank : false,
		emptyText : '请选择',
		anchor : '90%',
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../manage/project/bussiness/prjContractQuery/prjContract.jsp";
			var contract = window
					.showModalDialog(
							url,
							null,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(contract) != "undefined") {
				
				conNameCom.setValue(contract.contract_name);//承包商名称
				conId.setValue(contract.con_id);//承包商id
				
				
				Ext.Ajax.request({
				method:'POST',
				url:'manageproject/getEndPrjByCon.action',
				params:{
					con_no:contract.conttrees_no
				},
				success:function(resp){
					var result = eval("(" + resp.responseText + ")");
					if(result!=null){
						reportCode.setValue(result.reportCode);//编号
						starDate.setValue(result.startDate.substring(0,10));//开工日期
						endDate.setValue(result.endDate.substring(0,10));//竣工日期
						
				
						
					}else{
						
						
					}
					
				},
				failure:function(resp){
					Ext.Msg.alert('警告','出现未知错误！');
				}
			});
				
				
			}
		}
	});
	
	//乙方单位id
		var contractorId = new Ext.form.Hidden({
				id : "contractorId",
				name : "prjMeetSign.contractorId"
			})
		//乙方单位名称
		var cliendBox = new Ext.form.ComboBox({
		name : 'prjMeetSign.contractorName',
		fieldLabel : "乙方单位名称",
		mode : 'remote',
		editable : true,
		allowBlank : false,
		emptyText : '请选择',
		anchor : '80%',
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../manage/client/business/clientSelect/clientSelect.jsp?approveFlag=2";
			var client = window
					.showModalDialog(
							url,
							null,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(client) != "undefined") {
				cliendBox.setValue(client.clientName);
				contractorId.setValue(client.cliendId);
			}
		}
	});
	//------------------------------------------
	var checkSignId="";
	
	  var owner = new Ext.form.TextField({
			id : "owner",
			xtype : "textfield",
			fieldLabel : '甲方单位',
			emptyText :"大唐陕西发电有限公司灞桥热电厂",
			blankText:'大唐陕西发电有限公司灞桥热电厂',
			anchor : '80%',
			name : 'prjMeetSign.owner'
		});
		
	 var changeInfo = new Ext.form.TextArea({
			id : "changeInfo",
			xtype : "textfield",
			fieldLabel : '工程变更描述',
			readOnly:false,
			anchor : '90%',
			name : 'prjMeetSign.changeInfo',
			allowBlank : false
		});
	 var devolveOnInfo = new Ext.form.TextArea({
			id : "devolveOnInfo",
			xtype : "textfield",
			fieldLabel : '工程资料移交情况',
			readOnly:false,
			anchor : '90%',
			name : 'prjMeetSign.devolveOnInfo',
			allowBlank : false
		});
	
	
	var memo = new Ext.form.TextArea({
		id : "memo",
		fieldLabel : '备注',
		anchor : '90%',
		name : 'prjMeetSign.memo',
		allowBlank : true

	})
	  var applyBy = new Ext.form.TextField({
			id : "applyBy",
			xtype : "textfield",
			fieldLabel : '申请人',
			readOnly:true,
			anchor : '80%',
			name : 'prjMeetSign.applyBy',
			allowBlank : false
		});
		
	 var applyDate = new Ext.form.TextField({
			id : "applyDate",
			xtype : "textfield",
			fieldLabel : '申请时间',
			readOnly:true,
			value:getDate(),
			anchor : '80%',
			name : 'prjMeetSign.applyDate',
			allowBlank : false
		});
		
 function getDate() {
	var d, s, t;
	d = new Date();
	s = d.getFullYear().toString(10) + "-";
	t = d.getMonth() + 1;
	s += (t > 9 ? "" : "0") + t + "-";
	t = d.getDate();
	s += (t > 9 ? "" : "0") + t 
	
	return s;
}
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号
							sessWorcode = result.workerCode;
							sessWorname = result.workerName;
							sessDeptCode = result.deptCode;
							deptId = result.deptId;
							sessDeptName = result.deptName;
							applyBy.setValue(sessWorname);
							
							
						
						}
						
					}
				});
	}
	
	getWorkCode();

	var method='add';
	var  bianhao;
	
	
	
	var formtbar = new Ext.Toolbar({
		id : 'tbar',
		items : [{
			id : 'add',
			text : "增加",
			iconCls : 'add',
			handler : addRec
		}, '-',{
			id : 'save',
			text : "保存",
			iconCls : 'save',
			handler : saveData
		},'-', {
			id : 'delete',
			text : "删除",
			iconCls : 'delete',
			handler : deleRecord
		},'-',{
			id : 'cancel',
			text : "取消",
			iconCls : 'cancer',
			handler : cancel
		},'-',{
			id : 'print',
			text : "打印会签单",
			iconCls : 'print',
			handler : printRec
		},'-',
		{
			id : 'update',
			text : "验收会签单补录签字信息",
			iconCls : 'update',
			handler : signInfo
		}]
	});

	 function signInfo() {

		if (checkSignId == null||checkSignId=="") {
			Ext.Msg.alert("提示", "请选择一条记录！");
		} else {
			var arg = new Object();
			arg.checkSignId = checkSignId;
		 
			window.showModalDialog('prjRegisterAddInfo.jsp', arg,
					'status:no;dialogWidth=750px;dialogHeight=450px');
		}
	}	
	var form = new Ext.form.FormPanel({
			bodyStyle : "padding:5px 5px 0",
			labelAlign : 'left',
			region : 'north', 
			layout : 'column',
			height : 400,
			border : false,
			tbar : formtbar,
			items : [{
				border : false,
				layout : 'form',
				columnWidth : 1,
				labelWidth : 110,
				items : [SignId,conId, conNameCom]
			},{
				border : false,
				layout : 'form',
				columnWidth : 1,
				labelWidth : 110,
				items : [prjReg]
			}, {
				border : false,
				layout : 'form',
				columnWidth : 1,
				labelWidth : 110,
				hidden:true,
				items : [prjId]
			},{
				border : false,
				layout : 'form',
				columnWidth : .5,
				labelWidth : 110,
				items : [reportCode]
			},{
				border : false,
				layout : 'form',
				columnWidth : .5,
				labelWidth : 110,
				items : [budgetCost]
			}, {
				border : false,
				layout : 'form',
				columnWidth : .5,
				labelWidth : 110,
				items : [contractorId,cliendBox]
			}, {
				border : false,
				layout : 'form',
				columnWidth : .5,
				labelWidth : 110,
				items : [owner]
			},{
				border : false,
				layout : 'form',
				columnWidth : .5,
				labelWidth : 110,
				items : [starDate]
			},{
				border : false,
				layout : 'form',
				columnWidth : .5,
				labelWidth : 110,
				items : [endDate]
			}, {
				border : false,
				layout : 'form',
				columnWidth : 1,
				labelWidth : 110,
				items : [changeInfo]
			}, {
				border : false,
				layout : 'form',
				columnWidth : 1,
				labelWidth : 110,
				items : [devolveOnInfo]
			},{
				border : false,
				layout : 'form',
				columnWidth : 1,
				labelWidth : 110,
				items : [memo]
			},{
				border : false,
				layout : 'form',
				columnWidth : .5,
				labelWidth : 110,
				items : [applyBy]
			},{
				border : false,
				layout : 'form',
				columnWidth : .5,
				labelWidth : 110,
				items : [applyDate]
			}]
		});
		
		
		
		
		function saveData()
  {
  	
  	if(prjId.getValue()==''||prjReg.getValue()==''){
  		Ext.Msg.alert('警告','立项名称不能为空！')
  		return;
  	}
  	var url="";
  		if (method == "add") {
					url = 'manageproject/addPrjCheckMeetSign.action';

				} else {
					url = 'manageproject/updatePrjCheckMeetSign.action';
				}
				
				
				if (!form.getForm().isValid()) {
					return false;
				}
			
				form.getForm().submit({
					url : url,
					method : 'post',
					params : {
						reportCode:reportCode.getValue()
					},
					success : function(form, action) {
					var message = eval('(' + action.response.responseText + ')');
					Ext.Msg.alert("成功", "保存成功！");
					if(method=="add")
					{
					checkSignId = message.id;
					}
				  SignId.setValue(checkSignId);
	               equ_ds.reload();
				   setButn();
				  method="update";
				},
				failure : function(form, action) {
					Ext.Msg.alert('错误', '出现未知错误.');
					}
				})
			
  }

	 

			function printRec()
	{
		
			if(checkSignId!=null&&checkSignId!="")
			{
		
				Ext.Msg.confirm("提示", '确认要打印吗？', function(
						buttonobj) {
					// 如果选择是
					if (buttonobj == "yes") {
						var signId = checkSignId;
					
						window.open("/powerrpt/report/webfile/hr/prjCheckMeetSign.jsp?checkSingId="
											+ signId+"");
						

			}
						})
			}else
			{
				Ext.Msg.alert('提示', '请选择一条记录！');
				
			}
		
			
			
	
		
		
	}
	
	function addRec()
	{
		form.getForm().reset();
		equ_ds.removeAll();
		checkSignId = "";
		method = "add";
		setButn();
		
	}
	function cancel()
	{
		form.getForm().reset();
		equ_ds.removeAll();
		checkSignId = "";
		
		
	}
	function deleRecord()
	{
			if(equ_ds.getCount()>0)
			{
				Ext.MessageBox.alert("提示","请先删除设备使用部门信息！");
				return;
			}
				if (checkSignId!=null&&checkSignId!="") {
					Ext.Msg.confirm('提示', '删除的数据您将不能恢复,确定要删除吗?', function(b) {
						if (b == "yes") {
							
							Ext.Ajax.request({
								url : 'manageproject/delPrjCheckMeetSign.action',
								params : {
									checkSignId : checkSignId
								},
								method : 'post',
								waitMsg : '正在删除数据...',
								success : function(result, request) {
									Ext.MessageBox.alert('提示', '删除成功!');
									form.getForm().reset();
									checkSignId="";
									setButn();
								},
								failure : function(result, request) {
									Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
								}
							});
						}

					});

				} else {
					Ext.Msg.alert('提示', '请选择您要删除的记录！');
				}
	}
	

	
	// ----------------------------------------------------------
  

	
	function deleteEquDept() {
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		var names = [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.id) {
					ids.push(member.id);
				}
			}

			if (ids.length > 0) {
				Ext.Msg.confirm("删除", "是否确定删除所选记录？", function(buttonobj) {
							if (buttonobj == "yes") {
								Ext.lib.Ajax.request('POST',
										'manageproject/delEquDept.action', {
											success : function(action) {
												Ext.Msg.alert("提示", "删除成功！")
												equ_ds.reload();
											},
											failure : function() {
												Ext.Msg.alert('错误',
														'删除时出现未知错误.');
											}
										}, 'ids=' + ids);
							} else {
								equ_ds.reload();
							}
						});
			}else
			{
				equ_ds.reload();
			}
		}
	}
	
	function saveEquDept() {
		var alertMsg = "";
		grid.stopEditing();
		var modifyRec = grid.getStore().getModifiedRecords();
		if(modifyRec.length>0)
		{
		Ext.Msg.confirm('提示', '确定要保存修改数据吗?', function(button) {
					if (button == 'yes') {
						var updateData = new Array();
						for (var i = 0; i < modifyRec.length; i++) {
							

							if (alertMsg != "") {
								Ext.Msg.alert("提示", alertMsg);
								return;
							}
							updateData.push(modifyRec[i].data);
						}
						Ext.Ajax.request({
									url : 'manageproject/saveEquDept.action',
									method : 'post',
									params : {
									     checkSignId:checkSignId,
										isUpdate : Ext.util.JSON
												.encode(updateData)
									
									},
									success : function(form, options) {
										var obj = Ext.util.JSON
												.decode(form.responseText)
										Ext.MessageBox.alert('提示信息', '保存成功！')
										equ_ds.rejectChanges();
										equ_ds.reload();
									},
									failure : function(result, request) {
										Ext.MessageBox.alert('提示信息', '操作失败！')
									}
								})
					}
				})
		}else
		{
			Ext.MessageBox.alert('提示信息', '未做任何修改!')
			equ_ds.rejectChanges();
			equ_ds.reload();
		}
	}
	
	function cancerChange()
	{
		equ_ds.rejectChanges();
		equ_ds.reload();
	}
		var gridTbar = new Ext.Toolbar({
				items : [{
							id : 'gridadd',
							text : "增加",
							iconCls : 'add',
							handler : addEquDept
						}, '-', {
							id : 'griddelete',
							text : "删除",
							iconCls : 'delete',
							handler : deleteEquDept
						}, '-', {
							id : 'gridsave',
							text : "保存",
							iconCls : 'save',
							handler : saveEquDept
						}, '-', {
							id : 'gridcancer',
							text : "取消",
							iconCls : 'cancer',
							handler : cancerChange
						}]
			});
		function addEquDept() {
	
		var count = equ_ds.getCount();
		var currentIndex = count;
		var o = new equ_record({
					'id' : '',
					'checkSignId' : '',									
					'deptId' : '',								
					'assortDep' : '',
					'deptName' : ''
				
				});
		grid.stopEditing();
		equ_ds.insert(currentIndex, o);
		sm.selectRow(currentIndex);
		grid.startEditing(currentIndex, 1);

	}
	var equ_record = Ext.data.Record.create([{
				name : 'id',
				mapping:0
			}, {
				name : 'checkSignId',
				mapping:1
			}, {
				name : 'deptId',
				mapping:2
			},{
				name : 'deptName',
				mapping:3
			}]);
	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false
			});
	
	var equ_ds = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
				url : 'manageproject/getEquDept.action'
			}),
			reader : new Ext.data.JsonReader({
				root : "list",
				totalProperty : "total"
			},equ_record )

		});
	
    	
	var grid = new Ext.grid.EditorGridPanel({
		store : equ_ds,
		layout : 'fit',
		region:'center',
		columns : [sm, new Ext.grid.RowNumberer({
							header : '序号',
							width : 35
						}),
						 {
							header : 'deptId',
							hidden : true,
							dataIndex : 'deptId'

						}, // 选择框
			{
					header : "部门",
					width : 200,
					align : 'center',
					dataIndex : 'deptName',
					editor : new Ext.form.ComboBox({
						fieldLabel : '部门',
						value : "请选择...",
						mode : 'remote',
						editable : false,
						readOnly : true,
						width : 100,
						onTriggerClick : function() {
							var args = {
								selectModel : 'single',
								rootNode : {
									id : "0",
									text : '灞桥热电厂'
								}
							}
							var url = "/power/comm/jsp/hr/dept/dept.jsp";
							var rvo = window
									.showModalDialog(
											url,
											args,
											'dialogWidth:250px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
							if (typeof(rvo) != "undefined") {

								var record = grid.getSelectionModel()
										.getSelected();
								record.set("deptName", rvo.names);
								record.set("deptId", rvo.ids);
								
							}
						}
					})

				}],

		sm : sm, // 选择框的选择
		tbar : gridTbar,
		clicksToEdit : 1,
		viewConfig : {
			forceFit : false
		}
	});
	
	//----------------------------------------
	var layout = new Ext.form.FieldSet({ 
			layout : 'border',
			items : [form, grid]
		}); 
		function setButn() { 
			if (checkSignId == "") {
				gridTbar.setDisabled(true);
			} else {
				gridTbar.setDisabled(false);
			}
		} 
	
		return {
			layout : layout,
			grid : grid,
			form : form,
			setFormRec : function(v) {
				var rec = v; 
				form.getForm().loadRecord(v);
				checkSignId = v.get("checkSignId");
				conNameCom.setValue(v.get("conName"));
				contractorId.setValue(v.get("contractorId"));
				cliendBox.setValue(v.get("contractorName"));
				prjReg.setValue(v.get("prjName"));
				prjId.setValue(v.get("prjId"));
				
				setButn();
				equ_ds.on("beforeload", function() {
				Ext.apply(this.baseParams, {
					checkSignId : checkSignId
							
						});

			});
			
				equ_ds.load();
				method = "update";
			},
			resetFormRec : function() {
				form.getForm().reset();
				equ_ds.removeAll();
				checkSignId = "";
				method = "add";
				setButn();
			}
		}
	}
	
});