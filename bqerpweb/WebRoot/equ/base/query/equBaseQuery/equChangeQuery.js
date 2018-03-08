Ext.namespace('Power.changeQuery');
Power.changeQuery=function(){
var actionId;
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'equChangeId',
		mapping:0
	}, {
		name : 'equChangeNo',
		mapping:1
	}, {
		name : 'changeTitle',
		mapping:7
	}, {
		name : 'specialityName',
		mapping:21
	}, {
		name : 'sourceName',
		mapping:20
	}, {
		name : 'changeType',
		mapping:10
	}, {
		name : 'changePlanDate',
		mapping:11
	}, {
		name : 'wfState',
		mapping:17
	}, {
		name : 'workFlowNo',
		mapping:16
	}, {
		name : 'deptCode',
		mapping:22
	}, {
		name : 'deptName'
		
	}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'equchange/findChangeListByEquCode.action'
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
	var equcode = new Ext.form.TextField({
		id : "equcode",
		name : "equcode",
		readOnly : true
	});
	var equname = new Ext.form.TextField({
		id : "equname",
		name : "equname",
		width : '300',
		readOnly : true
	});
	var sm = new Ext.grid.CheckboxSelectionModel();
	var grid = new Ext.grid.GridPanel({
		region : "center",
		store : store,
		columns : [
		sm, {
			
			header : "ID",
			sortable : true,
			dataIndex : 'equChangeId',
			hidden:true
		},

		{
			header : "异动编号",
			width : 100,
			sortable : true,
			dataIndex : 'equChangeNo'
		},
		{
			header : "异动简题",
			width : 300,
			sortable : true,
			dataIndex : 'changeTitle'
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
			dataIndex : 'changeType'
		},
		{
			header : "计划开始时间",
			width : 200,
			sortable : true,
			dataIndex : 'changePlanDate'
		},
		{
			header : "当前状态",
			width : 100,
			sortable : true,
			dataIndex : 'wfState',
			renderer:function(value){
					if(value=="0"||value=="1") 
					  return "已登记";
				    else if(value=="1"||value=="2") 
					  return "审批中";
				    else if(value=="4") 
					  return "审批结束";
				}
		}],
		tbar : ['设备：',equcode,equname,{
			text : "详细查看",
            iconCls : 'query',
			handler :detailsRecord
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
	
grid.on('rowdblclick', detailsRecord);

	
function createupdatepanel(mytitle,myid)
	{
	var wd=180;
	var equChangeId = {
		id : "equChangeId",
		xtype : "textfield",
		fieldLabel : 'ID',
		value : '自动生成',
		anchor : "90%",
		readOnly : true,
		name : 'equChangeId'

	}
	
	var equChangeNo =new Ext.form.TextField( {
		id : "equChangeNo",
		xtype : "textfield",
		fieldLabel : '编号',
		anchor : "90%",
		readOnly : true,
		name : 'change.equChangeNo'

	});
			
//		var sourceStore = new Ext.data.Store({
//			proxy : new Ext.data.HttpProxy({
//				url : 'equchange/findChangeSourceList.action'
//			}),
//			reader : new Ext.data.JsonReader({
//					root : 'list'
//					}, [{
//						name : 'sourceCode'
//					}, {
//						name : 'sourceName'
//					}])
//		});
//		sourceStore.load();
//		var sourceCode = {
//			xtype : 'combo',
//			id : 'sourceCode',
//			fieldLabel : '来源',
//			store : sourceStore,
//			valueField : "sourceCode",
//			displayField : "sourceName",
//			mode : 'remote',
//			typeAhead : true,
//			forceSelection : true,
//			hiddenName : 'change.sourceCode',
//			editable : false,
//			triggerAction : 'all',
//			anchor : "90%",
//			blankText : '请选择',
//			emptyText : '请选择',
//			selectOnFocus : true,
//			allowBlank : false
//		};
		
		var sourceCode =new Ext.form.TextField( {
		id : "sourceCode",
		xtype : "textfield",
		fieldLabel : '来源',
		anchor : "90%",
		readOnly : true,
		name : 'change.sourceCode'

	});
	
	var specialCode = {
		id : "specialCode",
		xtype : "textfield",
		fieldLabel : '专业',
		name : 'specialCode',
		readOnly : true,
		anchor : "90%"
	}
	

	var deptCodeHidden = new Ext.form.Hidden({
		id : 'deptCode',
		name : 'change.deptCode'
	
	})
	var deptCombox = new Ext.form.TextField({
			id : 'deptName',
			fieldLabel : '部门',
			allowBlank : false,
			name : 'deptName',
			readOnly : true,
			anchor : "95%"
//			,
//			onTriggerClick : function() {
//				var args = {
//					selectModel : 'multiple',
//					rootNode : {
//						id : "0",
//						text : '灞桥热电厂'
//					}
//				}
//				var url = "/power/comm/jsp/hr/dept/dept.jsp";
//				var rvo = window
//						.showModalDialog(
//								url,
//								args,
//								'dialogWidth:250px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
//				if (typeof(rvo) != "undefined") {
//					Ext.getCmp('deptCode').setValue(rvo.codes);
//					Ext.getCmp('deptName').setValue(rvo.names);
//				}
//			}
		});
//	 	var deptCombox =new Ext.form.TextField( {
//		id : "deptCombox",
//		xtype : "textfield",
//		fieldLabel : '部门',
//		anchor : "90%",
//		readOnly : true,
//		name : 'change.deptCode'
//
//	});
	
		var changeTitle = {
		id : "changeTitle",
		xtype : "textarea",
		fieldLabel : '异动简题',
		allowBlank : false,
		readOnly : true,
		name : 'change.changeTitle',
		anchor : "95%"
	}

	
//	var changeType = new Ext.form.ComboBox({
//		id : 'changeType',
//		fieldLabel : '变更类型',
//		store : new Ext.data.SimpleStore({
//			fields : ["changeType", "displayText"],
//			data : [['1', '安装'], ['2', '改装'],['3','拆除'],['4','改造']]
//		}),
//		valueField : "changeType",
//		displayField : "displayText",
//		mode : 'local',
//		forceSelection : true,
//		blankText : '请选择变更类型',
//		emptyText : '请选择变更类型',
//		hiddenName : 'change.changeType',
//		value : '1',
//		editable : false,
//		typeAhead : true,
//		triggerAction : 'all',
//		selectOnFocus : true,
//		allowBlank : false,
//		name : 'change.changeType',
//		anchor : "90%"
//	});
	
		var changeType =new Ext.form.TextField( {
		id : "changeType",
		xtype : "textfield",
		fieldLabel : '变更类型',
		anchor : "90%",
		readOnly : true,
		name : 'changeType'

	});
	
	
		var changeReason = {
		id : "changeReason",
		xtype : "textarea",
		readOnly : true,
		fieldLabel : '异动原因',
		name : 'changeReason',
		anchor : "95%"
	
	}
	
	var assetnum =new Ext.form.TextField( {
		id : "assetnum",
		xtype : "textfield",
		fieldLabel : '设备资产编号',
		anchor : "90%",
		readOnly : true,
		name : 'assetnum'

	});
	
		var equName =new Ext.form.TextField( {
		id : "equName",
		xtype : "textfield",
		fieldLabel : '设备资产名称',
		anchor : "90%",
		readOnly : true,
		name : 'equName'

	});
	

//		var equOldCode = {
//		fieldLabel : '原功能码',
//		name : 'equOldCode',
//		xtype : 'combo',
//		id : 'equOldCode',
//		store : new Ext.data.SimpleStore({
//			fields : ['id', 'name'],
//			data : [[]]
//		}),
//		mode : 'remote',
//		hiddenName : 'change.equOldCode',
//		anchor : "90%",
//		triggerAction : 'all',
//		onTriggerClick : function() {
//			var url = "../../../base/business/kksselect/selectAttribute.jsp";
//			var equ = window.showModalDialog(url, '',
//					'dialogWidth=400px;dialogHeight=400px;status=no');
//			if (typeof(equ) != "undefined") {
//				Ext.getCmp('equOldCode').setValue(equ.code);
//				Ext.form.ComboBox.superclass.setValue.call(Ext
//						.getCmp('equOldCode'), equ.name);
//			}
//		   }
//	    };

		var equOldCode =new Ext.form.TextField( {
		id : "equOldCode",
		xtype : "textfield",
		fieldLabel : '原功能码',
		anchor : "90%",
		readOnly : true,
		name : 'change.equOldCode'

	});
	
//	var equNewCode = {
//		fieldLabel : '新功能码',
//		name : 'equNewCode',
//		xtype : 'combo',
//		id : 'equNewCode',
//		store : new Ext.data.SimpleStore({
//			fields : ['id', 'name'],
//			data : [[]]
//		}),
//		mode : 'remote',
//		hiddenName : 'change.equNewCode',
//		anchor : "90%",
//		triggerAction : 'all',
//		onTriggerClick : function() {
//			var url = "../../../base/business/kksselect/selectAttribute.jsp";
//			var equ = window.showModalDialog(url, '',
//					'dialogWidth=400px;dialogHeight=400px;status=no');
//			if (typeof(equ) != "undefined") {
//				Ext.getCmp('equNewCode').setValue(equ.code);
//				Ext.form.ComboBox.superclass.setValue.call(Ext
//						.getCmp('equNewCode'), equ.name);
//			}
//		   }
//	    };
	
		var equNewCode =new Ext.form.TextField( {
		id : "equNewCode",
		xtype : "textfield",
		fieldLabel : '新功能码',
		anchor : "90%",
		readOnly : true,
		name : 'change.equNewCode'

	});
	    var mydate=new Date();
//	var changePlanDate = {
//		id : "changePlanDate",
//		xtype : "datefield",
//		fieldLabel : '计划时间',
//		name : 'change.changePlanDate',
//		format : 'Y-m-d h:i:s',
//		value:mydate,
//		anchor : "90%"
//	}
	
		var changePlanDate = new Ext.form.TextField({
				id : 'changePlanDate',
				name : 'changePlanDate',
				fieldLabel : '计划时间',
				style : 'cursor:pointer',
				readOnly : true,
		        anchor : "90%"
			});
	
		var frontThing = {
		id : "frontThing",
		xtype : "textarea",
		fieldLabel : '异动前情况',
		readOnly : true,
		name : 'change.frontThing',
		anchor : "95%"
	}
	
		var oldFileName = new Ext.form.TextField({
		id : "oldFileName",
		xtype : "textfield",
		inputType : "file",
		readOnly : true,
		fieldLabel : '异动前附图',
		anchor : "90%",
		name : 'oldFileName'
	});
	
	   var oldImage = {
			id : "oldImage",
			xtype : "textfield",
			fieldLabel : '图片',
			hideLabel : true,
			readOnly : true,
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

	
	var backThing = {
		id : "backThing",
		xtype : "textarea",
		fieldLabel : '异动后情况',
		name : 'change.backThing',
		readOnly : true,
		anchor : "95%"
	}
	
		var newFileName = ({
		id : "newFileName",
		xtype : "textfield",
		inputType : "file",
		fieldLabel : '异动后附图',
		readOnly : true,
		name : 'newFileName',
		anchor : "90%"
	});
	
	   var newImage = {
			id : "newImage",
			xtype : "textfield",
			fieldLabel : '图片',
			hideLabel : true,
			readOnly : true,
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

	
	var applyDate = {
		id : "applyDate",
		xtype : "datefield",
		fieldLabel : '申请日期',
		name : 'change.applyDate',
		format : 'Y-m-d h:i:s',
		readOnly : true,
		value:mydate,
		anchor : "90%"
	}
		var applyDate = new Ext.form.TextField({
				id : 'applyDate',
				name : 'changePlanDate',
				fieldLabel : '申请日期',
				style : 'cursor:pointer',
				readOnly : true,
		anchor : "90%"
			});
		var applyName=new Ext.form.TextField({
				xtype : "textfield",
				fieldLabel : "申请人",
				name : "applyName",
				readOnly : true,
				anchor : "90%"
		});
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
		}
			,{
			layout : 'column',
			items : [
				{
				columnWidth : 0.5,
				layout : 'form',
				items:[{
					columnWidth : 0.3,
					layout : "form",
					border : false,
					items : [applyName]
				}]
			}, 
				{
				columnWidth : 0.5,
				layout : 'form',
				items :   [applyDate]                                
			}]
		}
		]
	}
	);
	
	
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
				items :  [oldImage]                                   
			}, {
				columnWidth : 0.5,
				layout : 'form',
				items :   [newImage]                                 
			}]
		}]
	});


		var tabpanel = new Ext.TabPanel({
			title : 'mytab',
			activeTab : 0,
			autoScroll : true,
			layoutOnTabChange : true,
			items : [
			panelone,paneltwo]

		});
		
		
		tabpanel.getItem('tab2').on('render', function(f) {
		 
//			tabpanel.getItem('tab2').form.findField('oldFileName').on('render',
//					function() {
//						
//						Ext.get('oldFileName').on('change',
//								function(field, newValue, oldValue) {
//									
//									var url = Ext.get('oldFileName').dom.value;
//										Ext.get('oldImage').dom.src = url;
//							      
//
//								});
//					});
//					
//					tabpanel.getItem('tab2').form.findField('newFileName').on('render',
//					function() {
//						Ext.get('newFileName').on('change',
//								function(field, newValue, oldValue) {
//									
//									var url = Ext.get('newFileName').dom.value;
//										Ext.get('newImage').dom.src = url;
//								});
//					});
		})
    return tabpanel;
	}
	
	function createaddwin(tabpanel, op,approveId,workFlowNo,workflowType)
	{
	
		var win = new Ext.Window({
		width : 650,
		height : 480,
		buttonAlign : "center",
		items : [tabpanel],
		layout : 'fit',
		modal : true,
		buttons : [{
			text : '关闭',
			iconCls : 'cancer',
			handler : function() {
				win.close();
			}
		}]

	});
	
	return win;
	
	}
	
	function queryRecord()
	{
	   store.baseParams = {
			equCode:equcode.getValue()
		};
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		});

	}
	
	
	function detailsRecord(){
		if (grid.selModel.hasSelection()) {
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行查看！");
			} else {
	            var record = grid.getSelectionModel().getSelected();
	            if (!tabpanel) {
					var tabpanel = createupdatepanel("修改", record.get("equChangeId"));
				}
				if (!win) {
					var win = createaddwin(tabpanel, "update", record.get("change.equChangeId"),record.get("change.workFlowNo"));
				}
				
				win.show();
				 tabpanel.setActiveTab(0);
	            tabpanel.getItem('tab1').getForm().load({
	            	url : "equchange/findChangeModel.action",
					params : {
						id:record.get("equChangeId")
					},
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
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
						    Ext.getCmp('changeType').setValue(o.changeTypeName);
						    Ext.getCmp('sourceCode').setValue(o.sourceName);
						    
							if(o.data.changePlanDate!=null)
							{
								var plandate=o.data.changePlanDate;
								plandate= plandate.substring(0,plandate.indexOf('T'))+" "+plandate.substring(plandate.indexOf('T')+1,plandate.length);
								//Ext.get("changePlanDate").dom.value=plandate;
							}
							if(o.data.applyDate!=null)
							{
								var plandate=o.data.applyDate;
								plandate= plandate.substring(0,plandate.indexOf('T'))+" "+plandate.substring(plandate.indexOf('T')+1,plandate.length);
							
								Ext.get("applyDate").dom.value=plandate;
							}
							if(o.applyManName!=null){
								Ext.get("applyName").dom.value=o.applyManName;
							}
								 tabpanel.setActiveTab(1);
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
								 tabpanel.setActiveTab(0);
					}
	            });
	            tabpanel.getItem('tab1').setTitle("异动基本信息");
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要查看的行!");
		}
	}
	
	//queryRecord();
	  
	
	var mainWin = new Ext.Window({
		title : '设备异动台帐',
		modal : true,
		width : 600,
		height : 400,
		layout:'fit',
		closeAction : 'hide',
		items : [grid]
	})
	return {
		win : mainWin,
		setValue : function(equCode,equName) {
			if(equCode!=null&&equCode!=''){
				equcode.setValue(equCode);
			}
			if (equName!=null&&equName!='') {
				equname.setValue(equName);
			}
		},
		query:queryRecord
		,reset : function(){
			equcode.setValue('');
			equname.setValue('');
			store.removeAll();
		}
	}
	
	
}