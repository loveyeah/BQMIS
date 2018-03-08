Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var isWo = "";
	var isMa = "";
	var Fuzzy = "";
	var register = parent.Ext.getCmp('tabPanel').register;
	// 将时间转成字符串格式
	function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";
		// 初始化时间
		Year = DateIn.getYear();
		Month = DateIn.getMonth() + 1;
		Day = DateIn.getDate();
		CurrentDate = Year + "-";
		if (Month >= 10) {
			CurrentDate = CurrentDate + Month + "-";
		} else {
			CurrentDate = CurrentDate + "0" + Month + "-";
		}
		if (Day >= 10) {
			CurrentDate = CurrentDate + Day;
		} else {
			CurrentDate = CurrentDate + "0" + Day;
		}
		return CurrentDate;
	}

	// 系统当前时间
	var enddate = new Date();
	// 系统到现在前七天
	var startdate = new Date();
	startdate.setDate(enddate.getDate() - 7);
	// startdate = startdate.getFirstDateOfMonth();
	var sdate = ChangeDateToString(startdate);
	var edate = ChangeDateToString(enddate);
	// 初始时间
	var fromDate = new Ext.form.DateField({
				format : 'Y-m-d',
				name : 'startDate',
				id : 'fromDate',
				itemCls : 'sex-left',
				clearCls : 'allow-float',
				checked : true,
				fieldLabel : "申请时间",
				allowBlank : false,
				readOnly : true,
				value : sdate,
				emptyText : '请选择',
				anchor : '100%'
			});
	// 结束时间
	var toDate = new Ext.form.DateField({
				format : 'Y-m-d',
				name : 'endDate',
				value : edate,
				id : 'toDate',
				itemCls : 'sex-left',
				clearCls : 'allow-float',
				fieldLabel : "至",
				allowBlank : false,
				readOnly : true,
				emptyText : '请选择',
				anchor : '100%'
			});
	// 工单状态
			
	var statusBox = new Ext.form.ComboBox({
				fieldLabel : '状态',
				store : [['', '全部'],['1', '已上报'],['2', '点检长已确认'],['3', '检修班长已审批'],['4', '工作负责人已审批'],['5', '点检验收确认'],['6', '点检长验收确认'],['7', '审批结束'],['8', '已退回']
				// modified by liuyi 20100329 只要两个状态
//				, ['2', '审核完结'],
//						['3', '审核退回'], [null, '全部']]
						],
				id : 'workflowStatusName',
				name : 'workflowStatus',
				valueField : "value",
				displayField : "text",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'workflowStatus',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				emptyText : '请选择',
				value:'',
				anchor : '80%'
			});
	// 工单类型
	var typeBox = new Ext.form.ComboBox({
				fieldLabel : '类型',
				store : [[null,'全部'],['Q', '消缺'],['D', '大修'], ['Z', '中修'],['X', '小修'], 
				['L', '临修'], ['J', '计划检修'], ['G', '项目']],
				// id : 'workflowStatusName',
				name : 'workflowStatus',
				valueField : "value",
				displayField : "text",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'workflowStatus',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				emptyText : '请选择',
				anchor : '100%'
			});
	// 专业
	//var storeRepairSpecail = new Ext.data.Store({
	//			proxy : new Ext.data.HttpProxy({
	//						url : 'workticket/getDetailRepairSpecialityType.action'
	//					}),
	//			reader : new Ext.data.JsonReader({
	//						root : 'list'
	//					}, [{
	//								name : 'specialityCode',
	//								mapping : 'specialityCode'
	//							}, {
	//								name : 'specialityName',
	//							}])
	//		});
	//storeRepairSpecail.load();
	var url = "timework/useprolist.action";
	var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", url, false);
	conn.send(null);   
	var search_data4 = Ext.decode(conn.responseText); 
	search_data4.push(["全部",""]);
	var professionStore = new Ext.data.SimpleStore({
							fields : ['name', 'id'],
							data : search_data4
						}); 
	var professionBox = new Ext.form.ComboBox({
				fieldLabel : '专业',
				id : 'professionCode',
				name : 'professionCode',
				store : professionStore,
				displayField : "name",
				valueField : "id",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'model.professionCode',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				emptyText : '请选择',
				anchor : '100%'
			});
		 
	 
		//检修部门
		//var repairDeptStore = new Ext.data.Store({
		//proxy : new Ext.data.HttpProxy({
		//	url : 'equfailure/repairDept.action',
		//	method : 'post'
		//}),
		//reader : new Ext.data.JsonReader({
		//	root : 'list'
		//}, [{
		//	name : 'deptCode'
		//}, {
		//	name : 'deptName'
		//}])
	//});
	var addRec = new Ext.data.Record.create([

	{
				name : 'deptCode'

			}, {
				name : 'deptName'

			}

	])
	var repairDeptStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'bqfailure/repairDept.action',
							method : 'post'
						}),
				reader : new Ext.data.JsonReader({
							root : 'list'
						}, addRec)
			});

	repairDeptStore.load();
	repairDeptStore.on('load', function() {
				this.insert(0, new addRec({

									deptCode : "",
									deptName : "全部"

								}));

			})
		var departmentsBox = new Ext.form.ComboBox({
		id : 'repairDepartment',
		store : repairDeptStore,
		fieldLabel : "检修部门",
		valueField : "deptCode",
		displayField : "deptName",
		mode : 'local',
		hiddenName : 'repairDepart',
		triggerAction : 'all',
		/*blankText : '请选择...',
		emptyText : '请选择...',*/
		readOnly : true,
		anchor : '80%'
	});
	
	// 模糊查询
	var query = new Ext.form.TextField({
				id : 'argFuzzy',
				xtype : "textfield",
				fieldLabel : "模糊查询",
				emptyText : '根据工单编码、KKS编码、申请人查询',
				name : 'argFuzzy',
				value : '',
				// anchor : '90%'
				width : '100%'
			});
	// 按钮
	var btnQuery = new Ext.Button({
				id : 'btnQuery',
				text : '查  询',
				minWidth : 80,
				handler : function() {
					con_ds.load({
								params : {
									startdate : Ext.get('fromDate').dom.value,
									enddate : Ext.get('toDate').dom.value,
									start : 0,
									limit : 18,
									// argFuzzy : Ext.get('argFuzzy').dom.value,
									//argFuzzy : Ext.getCmp("argFuzzy").value,
									argFuzzy : query.getValue(),
									workorderStatus : statusBox.getValue(),
									workorderType : typeBox.getValue(),
									professionCode : professionBox.getValue(),
									repairDepartment : departmentsBox.getValue()
								}
								
					});
				}
			});
	

	
	// 关联工作票
	//var isWorkbill = new Ext.form.Checkbox({
	//			id : 'isWorkbill',
	//			fieldLabel : '关联工作票',
	//			checked : false,
				// hideLabel : true,
	//			anchor : "20%",
	//			listeners : {
	//				check : isWorkTicketCheck
	//			}
	//		});
	//function isWorkTicketCheck() {
	//	if (isWorkbill.checked) {
	//		isWo = "Y";
	//	} else {
	//		isWo = "N";
	//	}
	//}
	// 关联领料单
	//var isTake = new Ext.form.Checkbox({
	//			id : 'isTake',
	//			fieldLabel : '关联领料单',
	//			checked : false,
	//			// hideLabel : true,
	//			anchor : "20%",
	//			listeners : {
	//				check : isTakeCheck
	//			}
	//		});
	//function isTakeCheck() {
	//	if (isTake.checked) {
	//		isMa = "Y";
	//	} else {
	//		isMa = "N";
	//	}
	//}
	var queryField = new Ext.form.FieldSet({
				title : '查询条件',
				height : '100%',
				collapsible : true,
				layout : 'form',
				items : [{
							layout : 'column',
							border : false,
							items : [{
										border : false,
										columnWidth : 0.25,
										layout : 'form',
										items : [fromDate]
									}, {
										border : false,
										columnWidth : 0.25,
										layout : 'form',
										items : [toDate]
									}, {
										border : false,
										columnWidth : 0.3,
										layout : 'form',
										items : [statusBox]
									}, {
										border : false,
										columnWidth : 0.1,
										align : 'center',
										layout : 'form',
										items : [btnQuery]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										border : false,
										columnWidth : 0.25,
										layout : 'form',
										items : [typeBox]
									}, {
										border : false,
										columnWidth : 0.25,
										layout : 'form',
										items : [professionBox]
									}, {
										border : false,
										columnWidth : 0.3,
										layout : 'form',
										items : [departmentsBox]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										border : false,
										columnWidth : 0.5,
										layout : 'form',
										items : [query]
									} 
									//	{
									//	border : false,
									//	columnWidth : 0.25,
									//	layout : 'form',
									//  hidden : true,
									//	items : [isWorkbill]
									//}, {
									//	border : false,
									//	columnWidth : 0.3,
									//  hidden : true,
									//	layout : 'form',
									//	items : [isTake]
									//}, 
									]
						}]
			});
	var form = new Ext.form.FormPanel({
				bodyStyle : "padding:5px 5px 0",
				labelAlign : 'right',
				id : 'shift-form',
				labelWidth : 80,
				autoHeight : true,
				region : 'center',
				border : false,
				items : [queryField]
			});
	// /////////////////////////////////////////////////////////////////////////////
	var con_item = Ext.data.Record.create([{
				name : 'model.woId'
			}, {
				name : 'model.woCode'
			}, {
				name : 'model.woCodeShow'
			}, {
				name : 'model.faWoCode'
			}, {
				name : 'model.workorderType'
			}, {
				name : 'model.workorderStatus'
			}, {
				name : 'model.workorderContent'
			}, {
				name : 'model.professionCode'
			}, {
				name : 'professionName'
			}, {
				name : 'model.repairModel'
			}, {
				name : 'model.equCode'
			}, {
				name : 'equipmentName'
			}, {
				name : 'model.workChargeCode'
			}, {
				name : 'workchargeName'
			}, {
				name : 'model.requireManCode'
			}, {
				name : 'requiremanName'
			}, {
				name : 'model.repairDepartment'
			}, {
				name : 'repairdepartmentName'
			}, {
				name : 'model.projectNum'
			}, {
				name : 'failureCode'
			}, {
				name : 'model.kksCode'
			}, {
				name : 'model.equPostionCode'
			}, {
				name : 'model.remark'
			}, {
				name : 'failureCode'
			}, {
				name : 'failureContent'
			},{
				name : 'model.wfState'
			},{
				name : 'model.workFlowNo'
			}]);
	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true

			});
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'workbill/getAllWorkbillList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "data.totalCount",
							root : "data.list"
						}, con_item)
			});
			
		con_ds.load({
			params : {
				start : 0,
				limit : 18,
				flag:'approve',
				startdate : sdate,
				enddate : edate
			}
		});
	con_ds.on('beforeload', function() {
		Ext.apply(this.baseParams, {
									startdate : Ext.get('fromDate').dom.value,
									enddate : Ext.get('toDate').dom.value,
									//argFuzzy : Ext.getCmp("argFuzzy").value,
									argFuzzy : query.getValue(),
									workorderStatus : statusBox.getValue(),
									workorderType : typeBox.getValue(),
									professionCode : professionBox.getValue(),
									repairDepartment : departmentsBox.getValue(),
									flag:'approve'
									//ifWorkticket : isWo,
									//ifMaterials : isMa
								});
	});
	
	var con_item_cm = new Ext.grid.ColumnModel([con_sm,
			new Ext.grid.RowNumberer({
						header : '序号',
						width : 35,
						align : 'center'
					}), {
				header : '',
				dataIndex : 'model.woId',
				hidden : true,
				align : 'center'
			}, {
				header : '',
				dataIndex : 'model.woCode',
				hidden : true,
				align : 'center'
			}, {
				header : '',
				dataIndex : 'model.faWoCode',
				hidden : true,
				align : 'center'
			}, {
				header : '工单编号',
				dataIndex : 'model.woCodeShow',
				align : 'center'
			}, {
				header : '工单类型',
				dataIndex : 'model.workorderType',
				align : 'center',
				renderer: function chargeIt(v){
					if(v == "Q"){
					return "消缺";
				}else if(v == "D"){
					return "大修";
				}else if(v == "X"){
					return "小修";
				}else if(v == "Z"){
					return "中修";
				}else if(v == "L"){
					return "临修";
				}else if(v == "J"){
					return "计划检修";
				}else if(v == "G"){
					return "项目";
				}else{
					return "状态异常";
				}
				}
			}/*, {
				header : '当前状态',
				dataIndex : 'model.workorderStatus',
				align : 'center',
				renderer : function chargeIt(v){
					if(v == "0"){
						return "开始工作";
					}else if(v == "1"){
						return "工作结束";
					}else if(v == "2"){
						return "审核完结";
					}else if(v == "3"){
						return "审核退回";
					}else{
						return "状态异常";
					}
				}
			}*/, {
				header : '工单内容',
				dataIndex : 'model.workorderContent',
				align : 'center'
			}, {
				header : '设备编码',
				dataIndex : 'model.kksCode',
				align : 'center'

			}, {
				header : '设备名称',
				dataIndex : 'equipmentName',
				align : 'center'
			}, {
				header : '',
				dataIndex : 'model.workChargeCode',
				hidden : true,
				align : 'center'
			}, {
				header : '工作负责人',
				dataIndex : 'workchargeName',
				align : 'center'
			}, {
				header : '',
				dataIndex : 'model.requireManCode',
				hidden : true,
				align : 'center'
			}, {
				header : '申请人',
				dataIndex : 'requiremanName',
				align : 'center'

			}, {
				header : '',
				dataIndex : 'model.repairDepartment',
				hidden : true,
				align : 'center'
			}, {
				header : '检修部门',
				dataIndex : 'repairdepartmentName',
				align : 'center'
			}, {
				header : '',
				dataIndex : 'model.professionCode',
				hidden : true,
				align : 'center'
			}, {
				header : '检修专业',
				dataIndex : 'professionName',
				align : 'center'
			}, {
				header : '审批状态',
				dataIndex : 'model.wfState',
				align : 'center',
				renderer:function(v){
					if(v=='0'){
						return '已填写';
					}if(v=='1'){
						return '已上报';
					}if(v=='2'){
						return '点检长已确认';
					}if(v=='3'){
						return '检修班长已审批';
					}if(v=='4'){
						return '工作负责人已审批';
					}if(v=='5'){
						return '点检验收确认';
					}if(v=='6'){
						return '点检长验收确认';
					}if(v=='7'){
						return '审批结束';
					}if(v=='8'){
						return '已退回';
					}
				}
			}]);
	con_item_cm.defaultSortable = true;

	var gridbbar = new Ext.PagingToolbar({
				pageSize : 18,
				store : con_ds,
				displayInfo : true,
				displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
				emptyMsg : "没有记录",
				beforePageText : '页',
				afterPageText : "共{0}"
			});
	var Grid = new Ext.grid.GridPanel({
				ds : con_ds,
				cm : con_item_cm,
				sm : con_sm,
				// title : '合同列表',
				width : Ext.get('div_lay').getWidth(),
				split : true,
				autoScroll : true,
				tbar:[{
					text:'工单审批',
					iconCls:'approve',
					handler:approve
				}],
				bbar : gridbbar,
				// tbar : contbar,
				border : false,
				viewConfig : {
					forceFit : true
				}
			});

			
	function approve(){
			var selections = Grid.getSelectionModel().getSelections();
			if(selections.length!=1){
				Ext.Msg.alert("提示", '请选择一条申请单信息！！');
				return false;
			}else{
				var rec =  Grid.getSelectionModel().getSelected();
				var entryId = rec.get("model.workFlowNo");
				if (entryId == null || entryId == "" || entryId == undefined) {
					Ext.Msg.alert('提示', '流程还未启动');
				} else if (status == "8") {
					Ext.Msg.alert('提示', '审批流程已结束');
				} else {
						var args=new Object();
						args.entryId=entryId;
						args.id=rec.get("model.woId");
						args.status=rec.get("model.wfState");
					  var o = window.showModalDialog('sign.jsp',
	                args, 'dialogWidth=700px;dialogHeight=500px;status=no;');
					if (o) {
						con_ds.reload();
						register.refresh();
					}
				}
			}	
	}
	Grid.on('rowdblclick', modifyBtn);
	function modifyBtn() {
		var rec = Grid.getSelectionModel().getSelected();
		if (rec) {
			var woCode = rec.get('model.woCode');
//			var	woCodeShow = rec.get('model.woCodeShow')
			var	faWoCode = "";
			var Status = rec.get('model.workorderStatus');
			
			register.edit(woCode,faWoCode,Status);
		}
	}
    Grid.on('rowclick',rowclickBtn);
	function rowclickBtn(){
	  var rec = Grid.getSelectionModel().getSelected();
	  if(rec) {
	   var woCode = rec.get('model.woCode');
	   var faWoCode = "";
	   var Status = rec.get('model.workorderStatus');
	   
	   register.rowl(woCode,faWoCode,Status);
	   }
	}
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				items : [{
							region : "north",
							layout : 'fit',
							height : 150,
							border : true,
							split : true,
							margins : '0 0 0 0',
							items : [form]
						}, {
							region : "center",
							layout : 'fit',
							title : '工单列表',
							border : false,
							collapsible : true,
							split : true,
							margins : '0 0 0 0',
							items : [Grid]
						}]
			});

});