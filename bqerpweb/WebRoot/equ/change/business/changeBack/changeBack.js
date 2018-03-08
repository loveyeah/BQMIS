Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	
	
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号，赋给全局变量
							dept.setValue(result.deptName);
							backEntryBy.setValue(result.workerName);
							backEntry.setValue(result.workerCode);
							deptCodeValue.setValue(result.deptCode);
						}
					}
				});
	}
var actionId;
		// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'change.equChangeId'
	}, {
		name : 'change.equChangeNo'
	}, {
		name : 'change.changeTitle'
	}, {
		name : 'specialityName'
	}, {
		name : 'sourceName'
	}, {
		name : 'change.changeType'
	}, {
		name : 'changePlanDate'
	}, {
		name : 'change.wfState'
	}, {
		name : 'change.workFlowNo'
	}, {
		name : 'deptCode'
	}, {
		name : 'deptName'
	}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'equchange/findChangeList.action'
			});

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"

	}, MyRecord);

	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
	
	var changetitle = new Ext.form.TextField({
		id : "changetitle",
		name : "changetitle"
	});
	
	var sm = new Ext.grid.CheckboxSelectionModel();
	
	var changeNo = new Ext.form.TextField({
		name : "changeNo"
	})

	var grid = new Ext.grid.GridPanel({
		region : "center",
		store : store,
		columns : [
		sm, {
			
			header : "ID",
			sortable : true,
			dataIndex : 'change.equChangeId',
			hidden:true
		},

		{
			header : "异动编号",
			width : 100,
			sortable : true,
			dataIndex : 'change.equChangeNo'
		},
		{
			header : "异动简题",
			width : 300,
			sortable : true,
			dataIndex : 'change.changeTitle'
		},
		{
			header : "申请专业",
			width : 100,
			sortable : true,
			dataIndex : 'specialityName'
		},
		{
			header : "来源",
			width : 100,
			sortable : true,
			dataIndex : 'sourceName'
		},
		{
			header : "变更类型",
			width : 100,
			sortable : true,
			dataIndex : 'change.changeType'
		},
		{
			header : "计划开始时间",
			width : 200,
			sortable : true,
			dataIndex : 'changePlanDate'
		}],
		tbar : ['编号',changeNo,'异动简题',changetitle, {
			text : "查询",
			iconCls : 'query',
			handler : queryRecord
		},'-',{
			text : "回录",
            iconCls : 'save',
			handler :approveRecord
		}],
		sm:sm,
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});
	
grid.on('rowdblclick', approveRecord);

var equChangeNum ;

var dept ;

var backEntryBy;


var deptCodeValue ;
var backEntry ;
var executeTime;

var equChangeId;
var equChangeNo;
var sourceCode ;
var specialCode;
var deptCodeHidden;
var deptCombox ;
var changeTitle;
var changeType ;
var changeReason ;
var assetnum ;
var equName ;
var equNewCode;
var changePlanDate;
var frontThing;
var oldFileName ;
var applyDate;
var newImage;
var newFileName;
var backThing ;
var oldImage;
var equOldCode;
var executeStatus ;


var saveButton ;
function createupdatepanel(mytitle,myid)
	{
		
		
equChangeId = {
		id : "equChangeId",
		xtype : "textfield",
		fieldLabel : 'ID',
		value : '自动生成',
		disabled:true,
		anchor : "90%",
		readOnly : true,
		name : 'equChangeId'

	}
	
	equChangeNo =new Ext.form.TextField( {
		id : "equChangeNo",
		xtype : "textfield",
		disabled:true,
		fieldLabel : '编号',
		readOnly : true,
		anchor : "90%",
		name : 'change.equChangeNo'

	});
			
		var sourceStore = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
				url : 'equchange/findChangeSourceList.action'
			}),
			reader : new Ext.data.JsonReader({
					root : 'list'
					}, [{
						name : 'sourceCode'
					}, {
						name : 'sourceName'
					}])
		});
		sourceStore.load();
		sourceCode = {
			xtype : 'combo',
			id : 'sourceCode',
			disabled:true,
			fieldLabel : '来源',
			store : sourceStore,
			valueField : "sourceCode",
			displayField : "sourceName",
			mode : 'remote',
			typeAhead : true,
			forceSelection : true,
			hiddenName : 'change.sourceCode',
			editable : false,
			triggerAction : 'all',
			anchor : "90%",
			blankText : '请选择',
			emptyText : '请选择',
			selectOnFocus : true
		};
		
	
	
	specialCode = {
		id : "specialCode",
		xtype : "textfield",
		fieldLabel : '专业',
		disabled:true,
		readOnly : true,
		name : 'specialCode',
		anchor : "90%"
	}
	

	deptCodeHidden = new Ext.form.Hidden({
		id : 'deptCode',
		name : 'change.deptCode'
	
	})
	deptCombox = new Ext.form.TriggerField({
			id : 'deptName',
			fieldLabel : '部门',
			disabled:true,
			allowBlank : false,
			name : 'deptName',
			anchor : "95%",
			onTriggerClick : function() {
				var args = {
					selectModel : 'multiple',
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
					Ext.getCmp('deptCode').setValue(rvo.codes);
					Ext.getCmp('deptName').setValue(rvo.names);
				}
			}
		});
	
	
		changeTitle = {
		id : "changeTitle",
		xtype : "textarea",
		disabled:true,
		fieldLabel : '异动简题',
		allowBlank : false,
		name : 'change.changeTitle',
		anchor : "95%"
	}
	

	
	changeType = new Ext.form.ComboBox({
		id : 'changeType',
		disabled:true,
		fieldLabel : '变更类型',
		store : new Ext.data.SimpleStore({
			fields : ["changeType", "displayText"],
			data : [['1', '安装'], ['2', '改装'],['3','拆除'],['4','改造']]
		}),
		valueField : "changeType",
		displayField : "displayText",
		mode : 'local',
		forceSelection : true,
		blankText : '请选择变更类型',
		emptyText : '请选择变更类型',
		hiddenName : 'change.changeType',
		value : '1',
		editable : false,
		typeAhead : true,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		name : 'change.changeType',
		anchor : "90%"
	});
	
	
	
	
		changeReason = {
		id : "changeReason",
		disabled:true,
		xtype : "textarea",
		readOnly : true,
		fieldLabel : '异动原因',
		name : 'change.changeReason',
		anchor : "95%"
	
	}
	
	assetnum =new Ext.form.TextField( {
		id : "assetnum",
		disabled:true,
		xtype : "textfield",
		readOnly : true,
		fieldLabel : '设备资产编号',
		anchor : "90%",
		name : 'change.assetnum'

	});
	
		equName =new Ext.form.TextField( {
		id : "equName",
		xtype : "textfield",
		disabled:true,
		readOnly : true,
		fieldLabel : '设备资产名称',
		anchor : "90%",
		name : 'change.equName'

	});
	

		equOldCode = {
		fieldLabel : '原功能码',
		disabled:true,
		name : 'equOldCode',
		xtype : 'combo',
		id : 'equOldCode',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'change.equOldCode',
		anchor : "90%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../base/business/kksselect/selectAttribute.jsp";
			var equ = window.showModalDialog(url, '',
					'dialogWidth=400px;dialogHeight=400px;status=no');
			if (typeof(equ) != "undefined") {
				Ext.getCmp('equOldCode').setValue(equ.code);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('equOldCode'), equ.name);
			}
		   }
	    };

	
	
	equNewCode = {
		fieldLabel : '新功能码',
		name : 'equNewCode',
		disabled:true,
		xtype : 'combo',
		id : 'equNewCode',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'change.equNewCode',
		anchor : "90%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../base/business/kksselect/selectAttribute.jsp";
			var equ = window.showModalDialog(url, '',
					'dialogWidth=400px;dialogHeight=400px;status=no');
			if (typeof(equ) != "undefined") {
				Ext.getCmp('equNewCode').setValue(equ.code);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('equNewCode'), equ.name);
			}
		   }
	    };
	
	
	    var mydate=new Date();
	changePlanDate = {
		id : "changePlanDate",
		xtype : "datefield",
		disabled:true,
		readOnly : true,
		fieldLabel : '计划时间',
		name : 'change.changePlanDate',
		format : 'Y-m-d h:i:s',
		value:mydate,
		anchor : "90%"
	}
	
		frontThing = {
		id : "frontThing",
		disabled:true,
		readOnly : true,
		xtype : "textarea",
		fieldLabel : '异动前情况',
		name : 'change.frontThing',
		anchor : "95%"
	}
	
		oldFileName = new Ext.form.TextField({ 
		id : "oldFileName",
		xtype : "textfield",
		disabled:true,
		readOnly : true,
		inputType : "file",
		fieldLabel : '异动前附图',
		anchor : "90%",
		name : 'oldFileName'
	});
	
	   oldImage = {
			id : "oldImage",
			xtype : "textfield",
			readOnly : true,
			disabled:true,
			fieldLabel : '图片',
			hideLabel : true,
		   anchor : "80%",
			height : 140,
			autoCreate : {
				tag : 'input',
				type : 'image',
				src :'http://localhost:8080/power/comm/images/UnknowBody.jpg',
				style : 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);',
				name : 'oldImage'
			}
		};

	
	backThing = {
		id : "backThing",
		readOnly : true,
		disabled:true,
		xtype : "textarea",
		fieldLabel : '异动后情况',
		name : 'change.backThing',
		anchor : "95%"
	}
	
		newFileName = ({
		id : "newFileName",
		disabled:true,
		readOnly : true,
		xtype : "textfield",
		inputType : "file",
		fieldLabel : '异动后附图',
		name : 'newFileName',
		anchor : "90%"
	});
	
	   newImage = {
			id : "newImage",
			readOnly : true,
			disabled:true,
			xtype : "textfield",
			fieldLabel : '图片',
			hideLabel : true,
			anchor : "80%",
			height : 140,
			autoCreate : {
				tag : 'input',
				type : 'image',
				src :'http://localhost:8080/power/comm/images/UnknowBody.jpg',
				style : 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);',
				name : 'newImage'
			}			
			
		};

	
	applyDate = {
		id : "applyDate",
		xtype : "datefield",
		disabled:true,
		readOnly : true,
		fieldLabel : '申请日期',
		name : 'change.applyDate',
		format : 'Y-m-d h:i:s',
		value:mydate,
		anchor : "90%"
	}


	var panelone = new Ext.FormPanel({
		frame : true,
		id:'tab1',
		labelAlign : 'right',
		title : '增加/修改异动来源类型',
			items : [{
			layout : 'column',
			items : [{
				columnWidth : 0.5,
				layout : 'form',
				items :  [equChangeId,sourceCode] 
			}, {
				columnWidth : 0.5,
				layout : 'form',
				items :   [equChangeNo,specialCode] 
			}]
		},{
			layout : 'column',
			items : [{
				columnWidth : 1,
				layout : 'form',
				items :  [deptCodeHidden,deptCombox]                                   
			}]
		},{
			layout : 'column',
			items : [{
				columnWidth : 1,
				layout : 'form',
				items :  [changeTitle]                                   
			}]
		},{
			layout : 'column',
			items : [{
				columnWidth : 1,
				layout : 'form',
				items :  [changeReason]                                  
			}]
		},{
			layout : 'column',
			items : [{
				columnWidth : 0.5,
				layout : 'form',
				items :  [changeType,assetnum,equOldCode]                                   
			}, {
				columnWidth : 0.5,
				layout : 'form',
				items :   [changePlanDate,equName,equNewCode]                               
			}]
		},{
			layout : 'column',
			items : [{
				columnWidth : 0.5,
				layout : 'form',
				items:[{
					columnWidth : 0.3,
					layout : "form",
					border : false,
					items : [{
						xtype : "textfield",
						fieldLabel : "申请人",
						disabled:true,
						name : "applyName",
						readOnly : true,
						value : document.getElementById("workName").value,
						anchor : "90%"
					}, {
						xtype : "hidden",
						name : "change.applyMan",
						value : document.getElementById('workCode').value,
						anchor : "90%"
					}]
				}]
			}, {
				columnWidth : 0.5,
				layout : 'form',
				items :   [applyDate]                                
			}]
		}]
	});
	
	
	var paneltwo = new Ext.FormPanel({
		frame : true,
		id:'tab2',
		labelAlign : 'right',
		title : '异动情况',
		items:[{
			layout : 'column',
			items : [{
				columnWidth : 1,
				layout : 'form',
				items :  [frontThing]                                  
			}]
		},{
			layout : 'column',
			items : [{
				columnWidth : 1,
				layout : 'form',
				items :  [backThing]                                  
			}]
		},
			{
			layout : 'column',
			items : [{
				columnWidth : 0.5,
				layout : 'form',
				items :  [oldFileName,oldImage]                                   
			}, {
				columnWidth : 0.5,
				layout : 'form',
				items :   [newFileName,newImage]                                 
			}]
		}]
	});

equChangeNum = new Ext.form.TextField({
	name:'equChangeNum',
	fieldLabel:'异动单编号',
	readOnly:true,
	anchor : "85%"
})

dept = new Ext.form.TextField({
	fieldLabel:'部门名称',
	readOnly:true,
	anchor : "85%"
})

backEntryBy= new Ext.form.TextField({
	fieldLabel:'回录人',
	readOnly:true,
	anchor : "85%"
})


deptCodeValue = new Ext.form.Hidden({
	name:'dept'
}) 
backEntry = new Ext.form.Hidden({
	name:'backEntryBy'
}) 

executeTime =  new Ext.form.TextField({
		id : 'executeTime',
		fieldLabel : '开工时间',
		anchor : "85%",
		style : 'cursor:pointer',
		name:'executeTime',
		listeners : {
			focus : function(){
				WdatePicker({
					startDate : '%y-%m-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true
						
				});
				
			}
		}
	})

executeStatus = new Ext.form.TextArea({
	fieldLabel:'执行情况',
	name:'executeStatus',
	height:'200',
	anchor : "85%"
})
	var panelthree= new Ext.FormPanel({
		frame : true,
		id:'tab3',
		labelAlign : 'right',
		title : '异动单回录',
		items:[{
			layout : 'column',
			items : [{
				columnWidth : 1,
				layout : 'form',
				items :  [equChangeNum]                                  
			}]
		},{
			layout : 'column',
			items : [{
				columnWidth : 1,
				layout : 'form',
				items :  [dept]                                  
			}]
		},{
			layout : 'column',
			items : [{
				columnWidth : 1,
				layout : 'form',
				items :  [backEntryBy]                                  
			}]
		},{
			layout : 'column',
			items : [{
				columnWidth : 1,
				layout : 'form',
				items :  [executeTime]                                  
			}]
		},{
			layout : 'column',
			items : [{
				columnWidth : 1,
				layout : 'form',
				items :  [executeStatus]                                  
			}]
		},{
			layout : 'column',
			items : [{
				columnWidth : 1,
				layout : 'form',
				items :  [deptCodeValue]                                  
			}]
		},{
			layout : 'column',
			items : [{
				columnWidth : 1,
				layout : 'form',
				items :  [backEntry]                                  
			}]
		}]
	});
	
		var tabpanel = new Ext.TabPanel({
			title : 'mytab',
			activeTab : 1,
			autoScroll : true,
			layoutOnTabChange : true,
			items : [
			panelthree,panelone,paneltwo]

		});	
	var wd=180;
	
		tabpanel.getItem('tab2').on('render', function(f) {
		 
			tabpanel.getItem('tab2').form.findField('oldFileName').on('render',
					function() {
						
						Ext.get('oldFileName').on('change',
								function(field, newValue, oldValue) {
									
									var url = Ext.get('oldFileName').dom.value;
										Ext.get('oldImage').dom.src = url;
							      

								});
					});
					
					tabpanel.getItem('tab2').form.findField('newFileName').on('render',
					function() {
						Ext.get('newFileName').on('change',
								function(field, newValue, oldValue) {
									
									var url = Ext.get('newFileName').dom.value;
										Ext.get('newImage').dom.src = url;
								});
					});
		})
    return tabpanel;
	}
	
	
	
	function createaddwin(tabpanel)
	{
	
		
//		saveButton = new Ext.Button({
//			text : '保存',
//			iconCls : 'save',
//			id : 'saveButton',
//			handler : function() {
//				if(executeTime.getValue().trim()!=''&&executeStatus.getValue().trim()!=''){
//					tabpanel.getItem('tab3').form.submit({
//						method : 'POST',
//						url : 'equchange/saveBackFill.action',
//						success : function(form, action) {
//						   var o = eval("(" + action.response.responseText + ")");
//						   Ext.Msg.alert('提示','保存成功');
//							win.close();
//						},faliue : function() {
//							Ext.Msg.alert('错误', '出现未知错误.');
//						}
//						});
//				}else{
//					Ext.Msg.alert('警告','开工时间和执行情况不能为空!');
//				}
//			
//						
//			}
//		})
		var win = new Ext.Window({
		width : 650,
		height : 480,
		buttonAlign : "center",
		items : [tabpanel],
		layout : 'fit',
		modal : true,
		buttons : [
//		saveButton,
		{
			text : '保存',
			iconCls : 'save',
			id : 'saveButton',
			handler : function() {
				if(executeTime.getValue().trim()!=''&&executeStatus.getValue().trim()!=''){
					tabpanel.getItem('tab3').form.submit({
						method : 'POST',
						url : 'equchange/saveBackFill.action',
						success : function(form, action) {
						   var o = eval("(" + action.response.responseText + ")");
						   Ext.Msg.alert('提示','保存成功');
							win.close();
						},faliue : function() {
							Ext.Msg.alert('错误', '出现未知错误.');
						}
						});
				}else{
					Ext.Msg.alert('警告','开工时间和执行情况不能为空!');
				}
			
						
			}
		
		},
		{
			text : '取消',
			iconCls : 'cancer',
			handler : function() {
				win.close();
			}
		}]

	});
	
	return win;
	
	}
	
	function approveRecord(){
		if (grid.selModel.hasSelection()) {
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行审批！");
			} else {
	            var record = grid.getSelectionModel().getSelected();
	            if (!tabpanel) {
					var tabpanel = createupdatepanel("修改", record.get("equChangeId"));
				}
				if (!win) {
					var win = createaddwin(tabpanel, "update", record.get("change.equChangeId"),record.get("change.workFlowNo"));
				}
				
				win.show();
				
				 tabpanel.setActiveTab(1);
				
	            tabpanel.getItem('tab1').getForm().load({
	            	url : "equchange/findChangeModel.action",
					params : {
						id:record.get("change.equChangeId")
					},
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
							var record = grid.getSelectionModel().getSelected();
							var changeNum = record.get("change.equChangeNo");
							
							equChangeNum.setValue(changeNum);
							getWorkCode();
						  if(o.data.equOldCode!=null)
						  {
						    Ext.getCmp('equOldCode').setValue(o.data.equOldCode);
						    Ext.form.ComboBox.superclass.setValue.call(Ext
								.getCmp('equOldCode'), o.oldname);
						  }
						  if(o.data.equNewCode!=null)
						  {
							Ext.getCmp('equNewCode').setValue(o.data.equNewCode);
						    Ext.form.ComboBox.superclass.setValue.call(Ext
								.getCmp('equNewCode'), o.newname);	
						  }
						   if(o.data.deptCode!=null)
						  {
							Ext.getCmp('deptCode').setValue(o.data.deptCode);
							Ext.getCmp('deptName').setValue(o.deptName);
						  }
						    Ext.getCmp('specialCode').setValue(o.specialityName);
						    
							if(o.data.changePlanDate!=null)
							{
								var plandate=o.data.changePlanDate;
								plandate= plandate.substring(0,plandate.indexOf('T'))+" "+plandate.substring(plandate.indexOf('T')+1,plandate.length);
								Ext.get("changePlanDate").dom.value=plandate;
							}
							if(o.data.applyDate!=null)
							{
								var plandate=o.data.applyDate;
								plandate= plandate.substring(0,plandate.indexOf('T'))+" "+plandate.substring(plandate.indexOf('T')+1,plandate.length);
							
								Ext.get("applyDate").dom.value=plandate;
							}
								 tabpanel.setActiveTab(2);
								 if(o.data.frontThing!=null)
								 {
								Ext.get("frontThing").dom.value=o.data.frontThing;
								 }
								 if(o.data.backThing!=null)
								 {
								Ext.get("backThing").dom.value=o.data.backThing;
								 }
								  Ext.get('oldImage').dom.src = "equchange/showphoto.action?changeNo="+o.data.equChangeNo+"&method=old";
								  Ext.get('newImage').dom.src = "equchange/showphoto.action?changeNo="+o.data.equChangeNo+"&method=new";
									 deptCombox.setDisabled(true) ;
									 changeType.setDisabled(true) ;

								 tabpanel.setActiveTab(0);
					}
	            });
	            
	            tabpanel.getItem('tab1').setTitle("异动基本信息");
	            tabpanel.setActiveTab(0);
	            
	            tabpanel.getItem('tab3').getForm().submit({
	            	url : "equchange/findBackFillModel.action",
					params : {
						changeNo:record.get("change.equChangeNo")
					},
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						var dataStatus = o.status;
						if(dataStatus=='data'){
							backEntryBy.setValue(o.backEntry);
							dept.setValue(o.dept);
							executeTime.setValue(o.executeTime);
							executeStatus.setValue(o.executeStatus);
//							saveButton.setDisabled(true);
							Ext.get("saveButton").dom.disabled = true;
						}else{
//							saveButton.setDisabled(false);
							Ext.get("saveButton").dom.disabled = false;
						}
						
					}
					});
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要回录的行!");
		}
	}
	
	
	function queryRecord()
	{
	   store.baseParams = {
			flag:'back',
			changeNo: changeNo.getValue(),
			changeTitle:changetitle.getValue()
		};
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		});

	}
	
	

	
	queryRecord();
	   new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});
	
})