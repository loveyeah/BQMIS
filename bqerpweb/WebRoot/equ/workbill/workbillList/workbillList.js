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
				store : [['0', '开始工作'], ['1', '工作结束']
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
				anchor : '80%'
			});
	// 工单类型
	var typeBox = new Ext.form.ComboBox({
				fieldLabel : '类型',
				store : [[null,'全部'],['Q', '消缺'],['D', '大修'],['Z', '中修'], ['X', '小修'], 
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
	var btnBliud = new Ext.Button({
				id : 'btnClear',
				text : '新建工单',
				minWidth : 80,
				handler : function() {
					register.Add();
				}
			});
	var btnInfo = new Ext.Button({
				id : 'btnInfo',
				text : '生成子工单',
				minWidth : 80,
				handler : function() {
					var sm = Grid.getSelectionModel();
					var selected = sm.getSelections();
					var rowselected = sm.getSelected();

					var _url3 = "equ/workbill/billMessage/BMessage.jsp";
					if (selected.length == 0 || selected.length < 0) {
						Ext.Msg.alert("提示", "请选择父工单！");
					} else {
						var rec = Grid.getSelectionModel().getSelected();
						// alert(rec.get('model.woCode'))
						woCode = rec.get('model.woCode')
						woCodeShow = rec.get('model.woCodeShow')
//						faWoCode = rec.get('model.faWoCode')
						register.parentoAdd(woCode, woCodeShow);
						// parent.Ext.getCmp("tabPanel").setActiveTab(1);
					}

				}
			});
	var btnDelete = new Ext.Button({
				id : 'btnSign',
				text : '删除工单 ',
				minWidth : 80,
				handler : function() {
				    var sm = Grid.getSelectionModel();
					var selected = sm.getSelections();
					var rowselected = sm.getSelected();
					if (selected.length == 0 || selected.length < 0) {
						Ext.Msg.alert("提示", "请选择一条工单记录！");
					} else {
						var url = "workbill/checkIfDelete.action?woCode="
					   + Grid.getSelectionModel().getSelected().get('model.woCode');
			            var conn = Ext.lib.Ajax.getConnectionObject().conn;
			            conn.open("POST", url, false);
			            conn.send(null);
			            isdelete = conn.responseText;
			            if (isdelete == 0){
							Ext.Msg.confirm('提示信息', '确认删除选中记录?', function(button, text) {
										if (button == 'yes') {
											Ext.Ajax.request({
														waitMsg : '删除中,请稍后...',
														url : 'workbill/deleteWorkbill.action',
														params : {
														    woCode : Grid.getSelectionModel().getSelected().get('model.woCode')
														},
														success : function(response,options) {
															con_ds.reload();
															Ext.Msg.alert('提示信息','删除记录成功！');
														},
														failure : function() {
															Ext.Msg.alert('提示信息','服务器错误,请稍候重试!')
														}
													});
										}
									})
						} else {
							Ext.Msg.alert('提示信息', "不能删除有关联关系的工单！");
							return false;
						}
					}
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
			
	var reportBt = new Ext.Button({
		text:'上       报',
		iconCls:'report',
		minWidth : 80,
		handler:function(){
			var sm = Grid.getSelectionModel();
				var selected = sm.getSelections();
				var noticeNo;
				//var isReport;
				if (selected.length != 1) {
					Ext.Msg.alert("系统提示信息", "请选择其中一项进行上报！");
				} else {
					var menber = selected[0];
					noticeNo = menber.get('model.woId');
						var args=new Object();
						args.busiNo=noticeNo;
						args.flowCode="GDSP";
						args.entryId="";
						
						args.title="工单上报";
						  var danger = window.showModalDialog('reportSign.jsp',
		                args, 'dialogWidth=700px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');
		               con_ds.reload();
			}
		}
	})
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
									}, {
										border : false,
										columnWidth : 0.1,
										layout : 'form',
										items : [btnBliud]
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
									},{
										border : false,
										columnWidth : 0.1,
										layout : 'form',
										items : [btnInfo]
									}, {
										border : false,
										columnWidth : 0.1,
										layout : 'form',
										items : [btnDelete]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										border : false,
										columnWidth : 0.5,
										layout : 'form',
										items : [query]
									} , {
										border : false,
										columnWidth : 0.3
//										items : [{}]
									}, {
										border : false,
										columnWidth : 0.1,
										layout : 'form',
										items : [reportBt]
									}
//									, {
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
									repairDepartment : departmentsBox.getValue()
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
				}else if(v == "Z"){
					return "中修";
				}else if(v == "X"){
					return "小修";
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
			},/* {
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
			}, */{
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
				bbar : gridbbar,
				// tbar : contbar,
				border : false,
				viewConfig : {
					forceFit : true
				}
			});

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