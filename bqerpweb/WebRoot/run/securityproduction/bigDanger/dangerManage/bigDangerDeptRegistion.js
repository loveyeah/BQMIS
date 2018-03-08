var start=0;
var limit=18;
var dangerYear;
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

	var store = new Ext.data.Store({

		proxy : dataProxy,

		reader : theReader

	});
	
	function cancerChange() {

		store.reload();
		store.rejectChanges();

	};
//从Execel导入信息
function uploadQuestFile() {
		//if (trainStartdate.getValue()==null||trainStartdate.getValue()=="") {
		if(dangerYear==null||dangerYear==""){
			Ext.Msg.alert('提示', '请选择年度！！');
			return;
		};
		var filePath = tfAppend.getValue();
		// 文件路径为空的情况
		if (filePath == "") {
			Ext.Msg.alert("提示", "请选择文件！");
			return;
		} else {
			// 取得后缀名并小写
			var suffix = filePath.substring(filePath.length - 3,
					filePath.length);
			if (suffix.toLowerCase() != 'xls')
				Ext.Msg.alert("提示", "导入的文件格式必须是Excel格式");
			else {
				Ext.Msg.wait("正在导入,请等待....");
				headForm.getForm().submit({
					method : 'POST',
					url : 'security/importBigDangerDeptValue.action',
					params : {
						dangerYear:dangerYear//trainStartdate.getValue()
					},
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("提示", o.msg);
						tfAppend.setValue("");
						store.reload();
					},
					failture : function() {
						Ext.Msg.alert(Constants.ERROR, "导入失败！");
					}
				})
			}
		}
	}
//分发	
function sendToDept() {
		var alertMsg = "";
		
		dangerTypegrid.stopEditing();
		var modifyRec = dangerTypegrid.getStore().getModifiedRecords();
		 if(modifyRec.length==0)
		 {
		 if (sm.hasSelection()) {
		 	var msg=[];
		 	var ids = new Array();
		 	var selected = dangerTypegrid.getSelectionModel().getSelections();
		 		for (var i = 0; i < selected.length; i += 1) {
		 			if (selected[i].get('status')=="1") {
		 				msg+="第"+selected[i].get('rowNumber')+"行已发送，不能再次发送！！<br/>";
		 			}else{
		 				ids.push(selected[i].get('dangerId'));
		 			}
		 		}
			if (msg!="") {
				Ext.Msg.alert('提示', msg);
			}else if(ids.length>0){
				Ext.Msg.confirm('提示', '确认要分发信息吗？', function(buttonId) {
					if (buttonId == 'yes') {
						Ext.Ajax.request({
									url : 'security/sendTodept.action',
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
				Ext.Msg.alert('提示', "无分发信息！！");
				return;
			}
		 }else{
		 	Ext.Msg.alert('提示', '请选择要分发的信息！');
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
		dangerTypegrid.stopEditing();
		var modifyRec = dangerTypegrid.getStore().getModifiedRecords();
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
							//update by kzhang 20100810
//							if (modifyRec[i].get("chargeByName") == null
//									|| modifyRec[i].get("chargeByName") == "") {
//								alertMsg += "负责人不能为空</br>";
//							}
//							if (modifyRec[i].get("orderBy") == null
//									|| modifyRec[i].get("orderBy") == "") {
//								alertMsg += "排序不能为空</br>";
//							}
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
								
									modifyRec=dangerTypegrid.getStore().getModifiedRecords();
									if(modifyRec.length>0)
									{
										for (var i = 0; i < modifyRec.length;)
										{
											if(modifyRec[i].get("isUse")=="N")
											{
												
												store.getModifiedRecords().remove(modifyRec[i]);
												
											}
											else
											{
											 i++;	
											}
									    
										}
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
//	else  if(isAdd>0) { Ext.MessageBox.alert('提示', '请填写修理类别！')}
//	else if (isAdd==0){
//		Ext.MessageBox.alert('提示信息', '没有做任何修改！')
//		store.reload();
}
	
//默认年度，当前年
//var trainStart = new Date();
//	trainStart = trainStart.format('Y');
////年度选择
//var trainStartdate = new Ext.form.TextField({
//				width : 100,
//				style : 'cursor:pointer',
//				allowBlank : true,
//				readOnly : true,
//				value : trainStart,
//				listeners : {
//					focus : function() {
//						WdatePicker({
//									isShowClear : true,
//									startDate : '%y',
//									dateFmt : 'yyyy'
//								});
//								this.blur();
//					}
//				}
//			});
			var trainStartdate = new Ext.form.TextField({
				fieldLabel : "年度",
				readOnly : true,
				anchor : '90%',
				//name : 'model.year',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : new Date().getFullYear()
											.toString(),
									alwaysUseStartDate : true,
									dateFmt : "yyyy"

								});
						this.blur();
					}
				},
				value : new Date().getFullYear()
			});

query();

//查看按钮是否可用
function checkIsEditable(){
	Ext.Ajax.request({
									url : 'security/checkIsEditable.action',
									method : 'post',
									params : {
										rewardYear : trainStartdate.getValue()
									},
									success : function(response, options) {
										if (response && response.responseText) {
											var res = Ext
													.decode(response.responseText)
											if (res>0) {
												addB.setDisabled(false);
												saveB.setDisabled(false);
												deleteB.setDisabled(false);
												sendB.setDisabled(false);
												tfAppend.setDisabled(false);
												importB.setDisabled(false);
											}else{
												addB.setDisabled(true);
												saveB.setDisabled(true);
												deleteB.setDisabled(true);
												sendB.setDisabled(true);
												tfAppend.setDisabled(true);
												importB.setDisabled(true);
											}
										}
									}
//									,failure : function(response, options) {
//										Ext.Msg.alert('提示', '删除出现异常！')
//									}
								});
}


//查询	
function query() {
	if (trainStartdate.getValue()==null||trainStartdate.getValue()=="") {
		Ext.Msg.alert('提示', '请选择年度！！');
		return;
	};
	dangerYear=trainStartdate.getValue();
	checkIsEditable();
	store.baseParams = {
			rewardYear : dangerYear,//trainStartdate.getValue(),
			status:"0",
			choose:"0"
	};
	store.load({
					params : {
						rewardYear : dangerYear,//trainStartdate.getValue(),
						status:"0",
						choose:"0",
						start : start,
						limit : limit
					}
				})
};
//删除
function delDangerDeptRegister() {
		if (sm.hasSelection()) {
			Ext.Msg.confirm('提示', '确认要删除数据吗？', function(buttonId) {
				if (buttonId == 'yes') {
					var recode=[];
					var sm = dangerTypegrid.getSelectionModel();
					var selected = sm.getSelections();
					var modifyRec = dangerTypegrid.getStore().getModifiedRecords();
					var ids=new Array();
					for (var i = 0; i < selected.length; i += 1) {
						recode=selected[i];
						if(recode.get('dangerId')!=null){
						ids.push(recode.get('dangerId'));
						};
						//dangerTypegrid.getSelectionModel().getSelected().set("isUse","N");
						//Record.set("isUse","N");
						store.remove(recode); 
						modifyRec.remove(recode);
					};
					if(ids.length>0){
						Ext.Ajax.request({
									url : 'security/deleteBigDangerDeptRegistion.action',
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
										Ext.Msg.alert('提示', '删除出现异常！')
									}
								});
					}else{
						Ext.Msg.alert('提示', '操作成功！')
					}
					//dangerTypegrid.getSelectionModel().getSelected().set("isUse","N");
					//var recode = dangerTypegrid.getSelectionModel().getSelected(); 
					//store.remove(recode); 
					}
				}
			)
	} else
			Ext.Msg.alert('提示', '请先选择要删除的数据！')
	};
//负责人选择
var chargeByName = new Ext.form.ComboBox({
	name : 'chargeByName',
	// xtype : 'combo',
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
			dangerTypegrid.getSelectionModel().getSelected().set("chargeBy",emp.workerCode);
			Ext.form.ComboBox.superclass.setValue.call(Ext
			.getCmp('chargeByName'), emp.workerName);
		}
	}
});
//评估部门选择
var belongDepName = new Ext.form.ComboBox({
	name : 'deptName',
	// xtype : 'combo',
	id : 'deptName',
	store : new Ext.data.SimpleStore({
		fields : ['id', 'name'],
		data : [[]]
	}),
	mode : 'remote',
	hiddenName:'assessDept',
	editable : false,
		anchor : "80%",
		triggerAction : 'all',
			onTriggerClick : function() {
				var url = "../../../../comm/jsp/hr/dept/dept.jsp";
				var args = {
				selectModel : 'single',
				//notIn : "'999999'",
				rootNode : {
				id : '0',
				text : '灞桥热电厂'
				}
			}
	var dept = window
		.showModalDialog(
		url,
		args,
		'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
	if (typeof(dept) != "undefined") {
	dangerTypegrid.getSelectionModel().getSelected().set("assessDept",dept.codes);
	Ext.form.ComboBox.superclass.setValue.call(Ext
		.getCmp('deptName'), dept.names);
	}
	}
});
//增加
function addDangerDeptRegister() {
		//if (trainStartdate.getValue()==null||trainStartdate.getValue()=="") {
		if(dangerYear==null||dangerYear==""){
			Ext.Msg.alert('提示', '请选择年度！！');
			return;
		};
		var count = store.getCount();
		var currentIndex = count;
		var o = new Record({
					'dangerName' : '',
					'finishDate' :'',								
					'deptName' : '',					
					'chargeByName' : '',
					'memo' :''	,
					'status' :'0'	,
					'orderBy' :'',
					'dangerYear': dangerYear//trainStartdate.getValue()
				});
		dangerTypegrid.stopEditing();
		store.insert(currentIndex, o);
		sm.selectRow(currentIndex);
		dangerTypegrid.startEditing(currentIndex, 1);
		//isAdd++;

	}   
//文件选择
var tfAppend = new Ext.form.TextField({
		text:"浏览",
		id : 'xlsFile',
		name : 'xlsFile',
		inputType : 'file',
		width : 200
	});
//底部分页工具栏
var bbar=new Ext.PagingToolbar({
				pageSize : limit,
				store : store,
				displayInfo : true,
				displayMsg : '第{0}条到第{1}条记录，共 {2} 条',
				emptyMsg : "没有记录"
			});
//增加按钮
var addB = new Ext.Button({
							text : "新增",
							iconCls : 'add',
							handler : addDangerDeptRegister
});
//删除按钮
var deleteB = new Ext.Button({
							text : "删除",
							iconCls : 'delete',
							handler : delDangerDeptRegister
});
//分发按钮
var sendB = new Ext.Button({
							text : "分发各部门",
							iconCls : 'upcommit',
							handler : sendToDept
});
//导入按钮
var importB = new Ext.Button({
							text : "导入",
							iconCls : 'upLoad',
							handler : uploadQuestFile
});
//保存按钮
var saveB = new Ext.Button({
			id : 'save',
			text : "保存",
			iconCls : 'save',
			handler : saveDangerDeptRegister
});
//上部工具栏
var gridTbar = new Ext.Toolbar({
				items : ["年度:", trainStartdate, {
					text : "查询",
					iconCls : 'query',
					minWidth : 70,
					handler : query},addB,
					saveB, deleteB,sendB, 
						tfAppend
						,importB]
			});	
//
var headForm = new Ext.form.FormPanel({
		region : 'north',
		id : 'bigDangerDeptValue',
		height : 28,
		frame : false,
		fileUpload : true,
		layout : 'form',
		items:[gridTbar]
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
							
							dangerTypegrid.getSelectionModel().getSelected()
									.set("finishDate",
											Ext.get("finishDate").dom.value);
						}
				});
				
			}
		}
	})
// 页面的Grid主体
var dangerTypegrid = new Ext.grid.EditorGridPanel({
		id:'dangerTypegrid',
		store : store,
		layout : 'fit',
		tbar : headForm,
		sm:sm,
		columns : [sm,new Ext.grid.RowNumberer({
									header : '序号',
									id: 'rowNumber',
									width : 35
				}),{
					header : '危险源名称',
					dataIndex : 'dangerName',
					align : 'left',
					width : 250,
					editor : new Ext.form.TextField({
								allowBlank : true
							})
					
				}, {
					header : '完成时间',
					dataIndex : 'finishDate',
					align : 'center',
					width : 100,
					renderer: function(value)
					{
					 return value.substring(0,10);
					},
					editor : finishDateField

				},
					{ 
					header : '评估部门',
					dataIndex : 'deptName',
					align : 'center',
					width : 100,
					editor : belongDepName
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
					dataIndex : 'chargeBy',
					align : 'center',
					hidden:true,
					width : 100
				
				},{
					header : '备注',
					dataIndex : 'memo',
					align : 'center',
					width : 250,
					editor : new Ext.form.TextField({
								allowBlank : true,
								id : 'memo'
							})

				}, {
					header : '已发送',
					dataIndex : 'status',
					id : 'status',
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
					editor : new Ext.form.NumberField({
								allowBlank : true,
								id : 'orderBy'
							})
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


		
//		store.load();

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
							items : [dangerTypegrid]
	
	}]
			});
			
		
})