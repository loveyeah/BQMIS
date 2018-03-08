var start=0;
var limit=18;

Ext.onReady(function() {
var sm = new Ext.grid.CheckboxSelectionModel();
// grid列表数据源
var Record = new Ext.data.Record.create([{
				name : 'dangerId',
				mapping:0
			},{
				name : 'dangerYear',
				mapping:1
			},{
				name:'dangerName',
				mapping:2
			},{
				name : 'finishDate',
				mapping:3
			},{
				name : 'assessDept',
				mapping:4
				
			},{
				name : 'deptName',
				mapping:5
			},{
				name : 'chargeBy',
				mapping:6
			},{
				name : 'chargeByName',
				mapping:7
			},{
				name : 'memo',
				mapping:8
			},{
				name : 'workFlowNo',
				mapping:9
			},  {
				name : 'status',
				mapping:10
			},{
				name : 'orderBy',
				mapping:11
			},{
				name : 'isUse',
				mapping:12
			}]);
var dataProxy = new Ext.data.HttpProxy(
			{
				url : 'security/findDeptRegisterListByDangerYear.action'
			}
	);

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"

	}, Record);
//
	var store = new Ext.data.Store({

		proxy : dataProxy,

		reader : theReader

	});	
//签字
function deptChargeBy() {
		var alertMsg = "";
		dangerChargeBy.stopEditing();
		var modifyRec = dangerChargeBy.getStore().getModifiedRecords();
		
		var record = dangerChargeBy.getSelectionModel().getSelections();
		
		 if(modifyRec.length==0)
		 {
		 	for(i=0;i<record.length;i++){
			if(record[i].get("chargeBy")==null){
				Ext.Msg.alert('警告','所选行的第'+(i+1)+'行责任人未填写！');
				return ;
			}
		}
		 if (sm.hasSelection()) {
		 	var msg=[];
		 	var ids = new Array();
		 	var selected = dangerChargeBy.getSelectionModel().getSelections();
		 		for (var i = 0; i < selected.length; i += 1) {
		 			if (selected[i].get('assess_dept')=="") {
		 				msg+="第"+selected[i].get('rowNumber')+"行,请落实负责人！！<br/>";
		 			}else{
		 				ids.push(selected[i].get('dangerId'));
		 			}
		 		}
			//alert(msg);
			if (msg!="") {
				Ext.Msg.confirm('提示', msg);
			}else if(ids.length>0){
				Ext.Msg.confirm('提示', '确认要签字？', function(buttonId) {
					if (buttonId == 'yes') {
						Ext.Ajax.request({
									url : 'security/deptChargeBy.action',
									method : 'post',
									params : {
										ids : ids.join(",")
									},
									success : function(response, options) {
										if (response && response.responseText) {
											var res = Ext
													.decode(response.responseText)
											Ext.Msg.alert('提示', res.msg);
											store.reload();
										}
									},
									failure : function(response, options) {
										Ext.Msg.alert('提示', '分发出现异常！')
									}
								});
					}else{
						return;
					}
				});
			}else{
				Ext.Msg.alert('提示', "无信息需要签字！！");
				return;
			}
		 }else{
		 	Ext.Msg.alert('提示', '请选择要签字的信息！');
		 	return;
		 }
		}else{
		Ext.MessageBox.alert('提示', '信息已修改，请先保存！！！')
		return;
		}
	}
//保存
function saveDangerDeptRegister() {
		var alertMsg = "";
		dangerChargeBy.stopEditing();
		var modifyRec = dangerChargeBy.getStore().getModifiedRecords();
		 if(modifyRec.length>0)
		 {
		Ext.Msg.confirm('提示', '确定要保存修改数据吗?', function(button) {
					if (button == 'yes') {
						var updateData = new Array();
						for (var i = 0; i < modifyRec.length; i++) {
							if (modifyRec[i].get("dangerName") == null
									|| modifyRec[i].get("dangerName") == "") {
								alertMsg += "危险源名称不能为空</br>";
							}
							if (modifyRec[i].get("finishDate") == null
									|| modifyRec[i].get("finishDate") == "") {
								alertMsg += "完成时间不能为空</br>";
							}
							if (modifyRec[i].get("deptName") == null
									|| modifyRec[i].get("deptName") == "") {
								alertMsg += "评估部门不能为空</br>";
							}
							if (modifyRec[i].get("chargeByName") == null
									|| modifyRec[i].get("chargeByName") == "") {
								alertMsg += "负责人不能为空</br>";
							}
							if (alertMsg != "") {
								Ext.Msg.alert("提示", alertMsg);
								return;
							}
							updateData.push(modifyRec[i].data);
						}
						Ext.Ajax.request({
									url : 'security/saveDangerDeptRegister.action',
									method : 'post',
									params : {
										isUpdate : Ext.util.JSON
												.encode(updateData)
									},
									success : function(form, options) {
									var obj = Ext.util.JSON
											.decode(form.responseText)
									if (obj && obj.msg) {
										Ext.Msg.alert('提示', obj.msg);
										if (obj.msg.indexOf('已经存在') != -1)
											return;
									}
									store.rejectChanges();
									store.reload();
								},
									failure : function(result, request) {
										Ext.MessageBox.alert('提示信息', '操作失败！')
									}
								})
					}
				})
	}
}
//默认年度，但前年
var trainStart = new Date();
	trainStart = trainStart.format('Y');
//年度选择
var trainStartdate = new Ext.form.TextField({
				width : 100,
				style : 'cursor:pointer',
				allowBlank : true,
				readOnly : true,
				value : trainStart,
				listeners : {
					focus : function() {
						WdatePicker({
									isShowClear : true,
									startDate : '%y',
									dateFmt : 'yyyy'
								});
					}
				}
			});
//
query();

//查询		
function query() {
	store.baseParams = {
			rewardYear : trainStartdate.getValue(),
			status:"1",
			choose:"1"
	};
	store.load({
					params : {
						rewardYear : trainStartdate.getValue(),
						status:"1",
						choose:"1",
						start : start,
						limit : limit
					}
				})
};
//负责人选择
var chargeByName = new Ext.form.ComboBox({
	name : 'chargeByName',
	id : 'chargeByName',
	store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
	}),
	mode : 'remote',
	hiddenName:'oldChargeByName',
	editable : false,
	anchor : "80%",
	triggerAction : 'all',
	onTriggerClick : function() {
			var url = "../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
					selectModel : 'single',
					//notIn : "'999999'",
					rootNode : {
					id : '0',
					text : '灞桥电厂'
					}
	}
	var emp = window
		.showModalDialog(
		url,
		args,
		'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

	if (typeof(emp) != "undefined") {
		dangerChargeBy.getSelectionModel().getSelected().set("chargeBy",emp.workerCode);
		Ext.form.ComboBox.superclass.setValue.call(Ext
		.getCmp('chargeByName'), emp.workerName);
//		chargeByName.setValue(emp.workerName);
//		dangerChargeBy.getSelectionModel().getSelected()
//		.set("oldChargeBy",
//		chargeByName.getValue());
	}
	}
});


//底部分页工具栏
var bbar=new Ext.PagingToolbar({
				pageSize : limit,
				store : store,
				displayInfo : true,
				displayMsg : '第{0}条到第{1}条记录，共 {2} 条',
				emptyMsg : "没有记录"
			});
//签字按钮
var signB = new Ext.Button({
							text : "签字",
							iconCls : 'write',
							handler : deptChargeBy
});
//上部工具栏
var gridTbar = new Ext.Toolbar({
				items : ["年度:", trainStartdate, {
					text : "查询",
					iconCls : 'query',
					handler : query},
					{
							id : 'save',
							text : "保存",
							iconCls : 'save',
							handler : saveDangerDeptRegister
						},signB]
			});	


//完成时间选择		
var finishDateField = new Ext.form.TextField({
		id : 'finishDate',
		fieldLabel : '完成时间',
		style : 'cursor:pointer',
		anchor : '90%',
		listeners : {
			focus : function(){
				WdatePicker({
					startDate : '%y-%m-%d',
					dateFmt : 'yyyy-MM-dd'
					,
						onpicked : function() {
							
							dangerChargeBy.getSelectionModel().getSelected()
									.set("finishDate",
											Ext.get("finishDate").dom.value);
						}
				});
				
			}
		}
	})
	
// 页面的Grid主体
var dangerChargeBy = new Ext.grid.EditorGridPanel({
		id:'dangerChargeBy',
		store : store,
		layout : 'fit',
		tbar : gridTbar,
		sm:sm,
		columns : [sm,new Ext.grid.RowNumberer({
									header : '序号',
									width : 35
				}),{
					header : '危险源名称',
					dataIndex : 'dangerName',
					align : 'left',
					width : 250
				}, {
					header : '完成时间',
					dataIndex : 'finishDate',
					align : 'center',
					width : 100,
					renderer: function(value)
					{
					 return value.substring(0,10);
					}
				},
					{ 
					header : '评估部门',
					dataIndex : 'deptName',
					align : 'center',
					width : 100
				},
				{ 
					header : '评估部门Code',
					dataIndex : 'assessDept',
					align : 'center',
					hidden:true,
					width : 100
				
				},
					{
					header : '责任人',
					dataIndex : 'chargeByName',
					align : 'center',
					width : 100,
					editor:chargeByName
				},{ 
					header : '责任人Code',
					id:'chargeBy',
					dataIndex : 'chargeBy',
					align : 'center',
					hidden:true,
					width : 100
				
				},{
					header : '备注',
					dataIndex : 'memo',
					align : 'center',
					width : 250
					}, {
					header : '已发送',
					dataIndex : 'status',
					align : 'center',
					hidden:true,
					width : 100,
					renderer : function(v) {
						if (v =='1') {
							return '是';
						} else {
							return '否';
						}
					}

				}, {
					header : '排序',
					dataIndex : 'orderBy',
					align : 'center',
					width : 100,
					hidden:true
					},{ 
					header : 'dangerId',
					dataIndex : 'dangerId',
					id:'dangerId',
					align : 'center',
					hidden:true,
					width : 100
				
				},{ 
					header : 'workFlowNo',
					dataIndex : 'workFlowNo',
					align : 'center',
					hidden:true,
					width : 100
				
				},{ 
					header : 'isUse',
					dataIndex : 'isUse',
					align : 'center',
					hidden:true,
					width : 100
				}],
		viewConfig : {
			forceFit : true
		},
		bbar:bbar,
		clicksToEdit : 1
});
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : true,
				frame : false,
				items : [{
							bodyStyle : "padding: 1,1,1,0",
							region : "center",
							border : false,
							frame : false,
							layout : 'fit',
							items : [dangerChargeBy]
	
}]
			});
			
})