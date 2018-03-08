Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function(){
	 function getParameter(psName) {
		var url = self.location;
		var result = "";
		var str = self.location.search.substring(0);
		if (str.indexOf(psName) != -1
				&& (str.substr(str.indexOf(psName) - 1, 1) == "?" || str
						.substr(str.indexOf(psName) - 1, 1) == "&")) {
			if (str.substring(str.indexOf(psName), str.length).indexOf("&") != -1) {
				var Test = str.substring(str.indexOf(psName), str.length);
				result = Test.substr(Test.indexOf(psName), Test.indexOf("&")
								- Test.indexOf(psName));
				result = result.substring(result.indexOf("=") + 1,
						result.length);
			} else {
				result = str.substring(str.indexOf(psName), str.length);
				result = result.substring(result.indexOf("=") + 1,
						result.length);
			}
		}
		return result;
	}
	// 工作票编号
	var txtWorkticketNo = new Ext.form.TextField({
		id : 'workticketNo',
		fieldLabel : "工作票编号",
		//cls:'disable',
		xtype : 'textfield',
		///value : '',
		//emptyText : "自动生成",
		readOnly : true,
		anchor : "85%"
	});
	// 申请单号
	var applyNo = new Ext.form.ComboBox({
		fieldLabel : '工作申请单号',
		id : 'applyNo',
		mode : 'remote',
		readOnly : true,
		disabled : true,
		anchor : "85%"
	});
	// 工单号
	var woCode = new Ext.form.TextField({
		fieldLabel : '工单号',
		id : 'woCode',
		mode : 'remote',
		readOnly : true,
		anchor : "85%"
	});
	// 工作票种类
	var storeWorkticketType = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'workticket/getDetailWorkticketTypeName.action'
						}),
				reader : new Ext.data.JsonReader({
							root : 'list'
						}, [{
									name : 'workticketTypeCode',
									mapping : 'workticketTypeCode'
								}, {
									name : 'workticketTypeName',
									mapping : 'workticketTypeName'
								}])
	});
	storeWorkticketType.load();
	var cbxWorkticketType = new Ext.form.ComboBox({
				id : 'workticketTypeCode',
				fieldLabel : "工作票种类<font color='red'>*</font>",
				store : storeWorkticketType,
				displayField : "workticketTypeName",
				valueField : "workticketTypeCode",
				hiddenName : 'workticketBaseInfo.workticketTypeCode',
				mode : 'local',
				triggerAction : 'all',
				value : '',
				readOnly : true,
				disabled : true,
				anchor : "85%"
//				listeners : {
//					select : workticketTypeSelected
//				}
	})
	// 工作票来源
	var storeWorkticketSource = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'workticket/getDetailWorkticketSource.action'
						}),
				reader : new Ext.data.JsonReader({
							root : 'list'
						}, [{
									name : 'sourceId',
									mapping : 'sourceId'
								}, {
									name : 'sourceName',
									mapping : 'sourceName'
								}])
	});
	storeWorkticketSource.load();
	var cbxWorkticketSource = new Ext.form.ComboBox({
				id : 'sourceId',
				fieldLabel : "工作票来源<font color='red'>*</font>",
				store : storeWorkticketSource,
				displayField : "sourceName",
				valueField : "sourceId",
				hiddenName : 'workticketBaseInfo.sourceId',
				mode : 'local',
				triggerAction : 'all',
				value : '',
				readOnly : true,
				disabled : true,
				anchor : "85%"
	})
	// 检修专业
	var storeRepairSpecail = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'workticket/getDetailRepairSpecialityType.action'
						}),
				reader : new Ext.data.JsonReader({
							root : 'list'
						}, [{
									name : 'specialityCode',
									mapping : 'specialityCode'
								}, {
									name : 'specialityName',
									mapping : 'specialityName'
								}])
	});
	storeRepairSpecail.load();
	var cbxRepairSpecail = new Ext.form.ComboBox({
				id : 'repairSpecailCode',
				fieldLabel : "检修专业<font color='red'>*</font>",
				store : storeRepairSpecail,
				displayField : "specialityName",
				valueField : "specialityCode",
				hiddenName : 'workticketBaseInfo.repairSpecailCode',
				mode : 'local',
				triggerAction : 'all',
				value : '',
				disabled : true,
				readOnly : true,
				anchor : "85%"
	})
   // 缺陷单号 ，当工作票来源是缺陷时缺陷单号可填
	var failureCode = new Ext.form.ComboBox({
				fieldLabel : '缺陷单号',
				name : 'workticketBaseInfo.failureCode',
				id : 'failureCode',
				mode : 'remote',
				 hiddenName : 'workticketBaseInfo.failureCode',
				triggerAction : 'all',
				readOnly : true,
				anchor : "85%",
				disabled : true
	});
	//负责人
	var triggerChargeBy = new Ext.form.TriggerField({
				id : 'chargebyname',
				fieldLabel : "工作负责人<font color='red'>*</font>",
				//onTriggerClick : chargeBySelect,
				value : '',
				readOnly : true,
				disabled : true,
				anchor : "85%"
	})
	// 所属部门
	var txtChargeDept = new Ext.form.TextField({
				id : 'deptname',
				fieldLabel : "所属部门",
				value : '',
				readOnly : true,
				disabled : true,
				anchor : "85%"
	})
	// 接收专业
	var storeReceiveSpecail = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
			url : 'workticket/getDetailReceiveSpecialityType.action'
		}),
		reader : new Ext.data.JsonReader({
			id : "unit"
		}, [{
			name : 'specialityCode'
		}, {
			name : 'specialityName'
		}])
	});
	storeReceiveSpecail.load();
	var cbxReceiveSpecail = new Ext.form.ComboBox({
				id : 'permissionDept',
				fieldLabel : "接收专业<font color='red'>*</font>",
				store : storeReceiveSpecail,
				displayField : "specialityName",
				valueField : "specialityCode",
				hiddenName : 'workticketBaseInfo.permissionDept',
				mode : 'local',
				triggerAction : 'all',
				value : '',
				readOnly : true,
				disabled : true,
				anchor : "85%"
	})
	// 工作条件
	var cbxWorkCondition = new Ext.form.TextField({
				id : 'conditionName',
				fieldLabel : "工作条件",
				// xtype : 'textfield',
				name : 'workticketBaseInfo.conditionName',
				value : '',
				disabled : true,
				anchor : "85%"
	});
	// 所属机组或系统
	var storeChargeBySystem = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'workticket/getDetailEquList.action'
						}),
				reader : new Ext.data.JsonReader({
							root : 'list'
						}, [{
									name : 'blockCode',
									mapping : 'blockCode'
								}, {
									name : 'blockName',
									mapping : 'blockName'
								}])
	});
	storeChargeBySystem.load();
	var cbxChargeBySystem = new Ext.form.ComboBox({
				id : 'equAttributeCode',
				fieldLabel : "所属机组或系统<font color='red'>*</font>",
				store : storeChargeBySystem,
				displayField : "blockName",
				valueField : "blockCode",
				hiddenName : 'workticketBaseInfo.equAttributeCode',
				mode : 'local',
				triggerAction : 'all',
				value : '',
				readOnly : true,
				disabled : true,
				anchor : "85%"
	})
	// 工作位置/地点
	var locationName = new Ext.form.TriggerField({
				fieldLabel : '工作地点<font color="red">*</font>',
				id : "locationName",
				name : "workticketBaseInfo.locationName",
				allowBlank : false,
				disabled : true,
				anchor : "85%"
	});
	// 计划开始时间
	var dfPlanStartDate = new Ext.form.TextField({
				id : 'planStartDate',
				fieldLabel : "计划开始时间",
				name : 'workticketBaseInfo.planStartDate',
				style : 'cursor:pointer',
				disabled : true,
				anchor : "85%"
//				value : getDate()
//				listeners : {
//					focus : function() {
//						WdatePicker({
//									startDate : '%y-%M-%d 00:00:00',
//									dateFmt : 'yyyy-MM-dd HH:mm:ss',
//									alwaysUseStartDate : true
//								});
//					}
//				}
	})
	// 计划结束时间
	var dfPlanEndDate = new Ext.form.TextField({
				id : 'planEndDate',
				fieldLabel : "计划结束时间",
				name : 'workticketBaseInfo.planEndDate',
				style : 'cursor:pointer',
				disabled : true,
				anchor : "85%"
//				value : getDate()
//				listeners : {
//					focus : function() {
//						var pkr = WdatePicker({
//									startDate : '%y-%M-%d 00:00:00',
//									dateFmt : 'yyyy-MM-dd HH:mm:ss',
//									alwaysUseStartDate : true
//								});
//					}
//				}
	});
	//总人数
   var totalCount = new Ext.form.TextField({ 
		id : "workticketBaseInfo.memberCount",
		name : "workticketBaseInfo.memberCount",
		fieldLabel : "总人数",
		labelAlign : 'right',
		//disabled : true,
		readOnly:true,
		width : 40
	});
   // 工作班成员
   var workMembers = new Ext.form.TriggerField({
		id : 'workticketBaseInfo.members',
		name : 'workticketBaseInfo.members',
		fieldLabel : "工作班成员", 
		//emptyText:'人员名称以逗号分隔', 
//		onTriggerClick : selectWorkMember,
		readOnly:true, 
		height : 50,
	    anchor : "95%"
	});
	// 工作内容
	var txtWorkContent = new Ext.form.TextArea({
		id : 'workticketBaseInfo.workticketContent',
		name : 'workticketBaseInfo.workticketContent',
		fieldLabel : "工作内容",
		//style:'cursor:hand;',
		readOnly:true, 
		//emptyText : '双击填写工作内容',
		//allowBlank:false,
		xtype : 'textarea',  
		height : 80,
		anchor : "95%"
	}); 
	
	// 备注
	var txtRemark = {
		id : 'workticketMemo',
		fieldLabel : "备注",
		xtype : 'textarea',
		name : 'workticketBaseInfo.workticketMemo',
		value : '',
		height : 60,
		readOnly:true,
		//disabled : true,
		anchor : "95%"
	}
	var baseInfoField = new Ext.form.FieldSet({
		// height : 440,
		// autoHeight:true,
		title : "工作票明细",
		height : '100%',
	    //collapsible : true,
		layout : 'form',
		items : [
				// 工作票编号
				{
			layout : "column",
			border : false,
			items : [{
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [{
									//id : 'hideWorkticketNo',
									//name : 'workticketBaseInfo.workticketNo',
									//value : '',
									//xtype : 'hidden'
								}, txtWorkticketNo]
					}]
		         },
					// 工单号 申请单号
				 {
					layout : "column",
					border : false,
					items : [{
								columnWidth : 0.5,
								layout : "form",
								border : false,
								items : [applyNo]
							}, {
								columnWidth : 0.5,
								layout : "form",
								border : false,
								items : [woCode]
							}]
				},
				// 工作票种类和检修专业
				{
					layout : "column",
					border : false,
					items : [{
								columnWidth : 0.5,
								layout : "form",
								border : false,
								items : [cbxWorkticketType]
							}, {
								columnWidth : 0.5,
								layout : "form",
								border : false,
								items : [cbxRepairSpecail]
							}]
				},
				// 工作票来源和缺陷单号
				{
					layout : "column",
					border : false,
					items : [{
								columnWidth : 0.5,
								layout : "form",
								border : false,
								items : [cbxWorkticketSource]
							}, {
								columnWidth : 0.5,
								layout : "form",
								border : false,
								items : [failureCode]
							}]
				},// 工作负责人
				{
					layout : "column",
					border : false,
					items : [{
								columnWidth : 0.5,
								layout : "form",
								border : false,
								items : [{
											id : 'chargeBy',
											xtype : 'hidden',
											id:'workticketBaseInfo.chargeBy',
											name : 'workticketBaseInfo.chargeBy'
										}, triggerChargeBy]
							}, {
								columnWidth : 0.5,
								layout : "form",
								border : false,
								items : [{
											id : 'chargeDept',
											xtype : 'hidden',
											name : 'workticketBaseInfo.chargeDept'
										}, txtChargeDept]
							}]
				},
				// 接收专业 所属机组或系统
				{
					layout : "column",
					border : false,
					items : [{
								columnWidth : 0.5,
								layout : "form",
								border : false,
								items : [cbxReceiveSpecail]
							},{
								columnWidth : 0.5,
								layout : "form",
								border : false,
								items : [cbxWorkCondition]
							}]
				},
				// 工作条件 工作地点
				{
					layout : "column",
					border : false,
					items : [ {
								columnWidth : 0.5,
								layout : "form",
								border : false,
								items : [cbxChargeBySystem]
							}, {
								columnWidth : 0.5,
								layout : "form",
								border : false,
								items : [locationName]
							}]
				},
				// 计划开始时间
				{
					layout : "column",
					border : false,
					items : [{
								columnWidth : 0.5,
								layout : "form",
								border : false,
								items : [dfPlanStartDate]
							},{
								columnWidth : 0.5,
								layout : "form",
								border : false,
								items : [dfPlanEndDate]
							}]
				},
				//工作班成员
				{
					layout : "column",
					border : false,
					items : [ {
								columnWidth :  0.77,
								layout : "form",
								border : false,
								items : [workMembers]
							},{
							     columnWidth: 0.2, 
							     layout:'form',
							     //border: true, 
							     items:[totalCount]
							}]
							
				},
				//工作内容
				{
					layout : "column",
					border : false,
					items : [{
								columnWidth : 0.97,
								layout : "form",
								border : false,
								items : [txtWorkContent]
							}]
							
							
				},
				// 备注
				{
					layout : "column",
					border : false,
					items : [{
								columnWidth : 0.97,
								layout : "form",
								border : false,
								items : [txtRemark]
							}]
				}] 
	});
	Ext.lib.Ajax.request('POST',
				'workbill/findWorkticketByWorkticketNo.action?workticketNo='+getParameter("workticketNo"), {
					success : function(action) { 
						var result = eval("(" + action.responseText + ")"); 
					    Ext.get('workticketNo').dom.value =(result.model.workticketNo == null) ? "" : result.model.workticketNo;	
					    Ext.get('woCode').dom.value = getParameter("woCode");
					    Ext.get('applyNo').dom.value =(result.model.applyNo == null) ? "" : result.model.applyNo;
					    Ext.get('workticketTypeCode').dom.value =(result.workticketTypeName == null) ? "" : result.workticketTypeName;
					    Ext.get('sourceId').dom.value =(result.sourceName == null) ? "" : result.sourceName;
					    Ext.get('failureCode').dom.value =(result.model.failureCode == null) ? "" : result.model.failureCode;
					    Ext.get('chargebyname').dom.value =(result.chargeByName == null) ? "" : result.chargeByName;
					    Ext.get('deptname').dom.value =(result.deptName== null) ? "" : result.deptName;
					    Ext.get('workticketBaseInfo.members').dom.value =(result.model.members == null) ? "" : result.model.members;
					    Ext.get('workticketBaseInfo.workticketContent').dom.value =(result.model.workticketContent == null) ? "" : result.model.workticketContent;
					    Ext.get('workticketMemo').dom.value =(result.model.workticketMemo == null) ? "" : result.model.workticketMemo;
					    Ext.get('planStartDate').dom.value =(result.planStartDate == null) ? "" : result.planStartDate;
					    Ext.get('planEndDate').dom.value =(result.planEndDate == null) ? "" : result.planEndDate;
					    Ext.get('locationName').dom.value =(result.model.locationName == null) ? "" : result.model.locationName;
					    Ext.get('equAttributeCode').dom.value =(result.blockName == null) ? "" : result.blockName;
					    Ext.get('workticketBaseInfo.memberCount').dom.value =(result.model.memberCount == null) ? "" : result.model.memberCount;
					    Ext.get('permissionDept').dom.value =(result.receiveSpecail == null) ? "" : result.receiveSpecail;
					    Ext.get('repairSpecailCode').dom.value =(result.repairSpecail == null) ? "" : result.repairSpecail;
					}
	});
	var baseInfoFormPanel = new Ext.FormPanel({
				region : 'center',
				border : false,
				frame : true,
				items : [baseInfoField]
				
			});
	
	var baseInfoViewport = new Ext.Viewport({ 
		        enableTabScroll : true,
				layout : "border",
				border : false, 
				items : [baseInfoFormPanel],
				defaults : {
					autoScroll : true
				}
			})
});