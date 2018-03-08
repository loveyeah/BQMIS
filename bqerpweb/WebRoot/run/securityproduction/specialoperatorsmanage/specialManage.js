// 特种作业人员管理

Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.QuickTips.init();
Ext.onReady(function() {
	var strMethod;
	var strMsg;
	// ============== 定义grid ===============
	var record = Ext.data.Record.create([
		{
		    // ID
		    name : 'spj.offerId'
		},{
			// 工号
			name : 'spj.workerCode'
		},{
			// 操作项目
			name : 'spj.projectOperation'
		},{
			// 岗位年限
			name : 'spj.postYear'
		},{
			// 证书名称
			name : 'spj.offerName'
		},{
			// 证书编号
			name : 'spj.offerCode'
		},{
			// 证书发放日期
			name : 'offerDate'
		},{
			// 证书有效开始日期
			name : 'offerStartDate'
		},{
			// 证书有效结束日期
			name : 'offerEndDate'
		},{
			// 体检日期
			name : 'medicalDate'
		},{
			// 体检结果
			name : 'spj.medicalResult'
		},{
			// 备注
			name : 'spj.memo'
		},{
			// 企业编号
			name : 'spj.enterpriseCode'
		}
		,{
			// 员工姓名
			name : 'workerName'
		}
		])

	// 定义获取数据源
	var dataProxy = new Ext.data.HttpProxy({
				url : 'security/findSpecialoperatorsList.action'
			});

	// 定义格式化数据
	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, record);

	// 定义封装缓存数据的对象
	var queryStore = new Ext.data.Store({
				// 访问的对象
				proxy : dataProxy,
				// 处理数据的对象
				reader : theReader
			});
	queryStore.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			});

	// -----------------控件定义----------------------------------
	// 查询参数
	var txtFuzzy1 = new Ext.form.TextField({
				id : "fuzzy",
				name : "fuzzy",
				width : 235,
				emptyText : "姓名"
			});

	//定义选择列
	var sm = new Ext.grid.RowSelectionModel({
				singleSelect : true
			});

    var check = new Ext.grid.CheckboxSelectionModel({
    		singleSelect : false
    });
	// --gridpanel显示格式定义-----开始-------------------
	var grid = new Ext.grid.GridPanel({
				region : "center",
				layout : 'fit',
				store : queryStore,
				stripeRows : true,
				columns : [
				 		check,
						//自动行号
						new Ext.grid.RowNumberer({
									header : "行号",
									width : 31
								}),{
									header : "ID",
									dataIndex : 'spj.offerId',
									hidden : true,
									sortable : true
								},
								{
							header : "姓名",
							dataIndex : 'workerName',
							sortable : true
						}, {
							header : "操作项目",
							dataIndex : 'spj.projectOperation',
							sortable : true
						}, {
							header : "岗位年限",
							dataIndex : 'spj.postYear',
							sortable : true
						},{
							header : '证书名称',
							dataIndex : 'spj.offerName',
							sortable : true
						},{
							header : '证书发放日期',
							hidden : true,
							dataIndex : 'offerDate',
							sortable : true
						},{
							header : '体检结果',
							dataIndex : 'spj.medicalResult',
							sortable : true
						},{
							header : '体检日期',
							dataIndex : 'medicalDate',
							sortable : true
						},{
							header : '备注',
							dataIndex : 'spj.memo',
							sortable : true
						}],
				sm : check,
				autoScroll : true,
				enableColumnMove : false,
				autoSizeColumns : true,
				viewConfig : {
					forceFit : false
				},

				// 头部工具栏
				tbar : [ {
							text : "模糊查询:"
						},txtFuzzy1,{
							text : "查询",
							iconCls : Constants.CLS_QUERY,
							handler : queryRecord
						},"-", {
							text : "新增",
							iconCls : Constants.CLS_ADD,
							handler : addRecord
						}, "-", {
							text : Constants.BTN_UPDATE,
							iconCls : Constants.CLS_UPDATE,
							handler : updateRecord
						}, "-", {
							text : Constants.BTN_DELETE,
							iconCls : Constants.CLS_DELETE,
							handler : deleteRecord
						}],

				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : Constants.PAGE_SIZE,
							store : queryStore,
							displayInfo : true,
							displayMsg : Constants.DISPLAY_MSG,
							emptyMsg : Constants.EMPTY_MSG
						})
			});
	// --gridpanel显示格式定义-----结束--------------------

	// 注册双击事件
	grid.on("rowdblclick", updateRecord);

	// ***************************弹出窗口**************************//


	// ID
	var offerId = new Ext.form.Hidden({
	     id : 'offerId',
	     name : 'spj.offerId'
	})
	// 人员姓名
	var worker = {
		fieldLabel : '人员姓名',
		name : 'workerCode',
		xtype : 'combo',
		id : 'workerCode',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'spj.workerCode',
		allowBlank : false,
		editable : false,
		anchor : "85%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '合肥电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(emp) != "undefined") {
				Ext.getCmp('workerCode').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('workerCode'), emp.workerName);
			}
		}
	};
  
	
	// 操作项目
	var operPro = new Ext.form.TextField({
	     fieldLabel : '操作项目',
		 id : 'projectOperation',
		 width : 180,
		 name : 'spj.projectOperation'
	})
	// 岗位年限
	var postYear = new Ext.form.NumberField({
	     fieldLabel : '岗位年限',
		 id : 'postYear',
		 labelAlign : 'right',
		 width : 180,
		 name : 'spj.postYear',
		 allowNegative : false,
		 allowDecimals : false,
		 maxValue : 99
	})
	 
	//体检日期
	var medicalDate = new Ext.form.DateField({
        id : 'medicalDate',
        name : 'spj.medicalDate',
        format : 'Y-m-d',
        width : 180,
        readOnly : true,
        fieldLabel : '体检日期'
    });
    //体检结果
    var medicalResult = new Ext.form.TextField({
         fieldLabel : "体检结果",
         id : 'medicalResult',
         name : 'spj.medicalResult',
         width : 180,
         isFormField : true
         
    })
  
    // 证书名称
    var offerName = new Ext.form.TextField({
       fieldLabel : '证书名称',
       id : 'offerName',
       name : 'spj.offerName',
       width : 180
    })
    // 证书编号
    var offerCode = new Ext.form.TextField({
       fieldLabel : '证书编号',
       id : 'offerCode',
       name : 'spj.offerCode',
       width : 180
    })
    // 证书发放日期
    var offerDate = new Ext.form.DateField({
        id : 'offerDate',
        name : 'spj.offerDate',
        format : 'Y-m-d',
        width : 180,
        readOnly : true,  
        fieldLabel : '证书发放日期'
    });

    // 证书有效期限 从
    var offerStartDate = new Ext.form.DateField({
        id : 'offerStartDate',
        name : 'spj.offerStartDate',
        format : 'Y-m-d',
        width : 180,
        readOnly : true,
        fieldLabel : '证书有效期限'
    });
    // 证书有效期限 至
    var offerEndDate = new Ext.form.DateField({
        id : 'offerEndDate',
        name : 'spj.offerEndDate',
        format : 'Y-m-d',
        width : 180,
        readOnly : true,
        fieldLabel : '至'
    });
    var memo = new Ext.form.TextArea({
        id : 'memo',
        name : "spj.memo",
        fieldLabel : '备注',
        width : 180
    
    })
	// Form
	var addPanel = new Ext.FormPanel({
				frame : true,
				labelAlign : 'right',
				items : [offerId,worker,operPro,postYear,medicalDate,medicalResult,offerName,
				     offerCode,offerDate,offerStartDate,offerEndDate,memo
				     ]
			});

	// 弹出窗口
	var win = new Ext.Window({
				width : 360,
				height : 430,
				modal : true,
				title : '增加/修改',
				buttonAlign : 'center',
				items : [addPanel],
				layout : 'fit',
				closeAction : 'hide',
				resizable : false,
				buttons : [{
							text : Constants.BTN_SAVE,
							iconCls : Constants.CLS_SAVE,
							buttonAlign : 'center',
							handler : confirmRecord
						}, {
							text : Constants.BTN_CANCEL,
							iconCls : Constants.CLS_CANCEL,
							buttonAlign : 'center',
							handler : function() {
								win.hide();
							}
						}]
			});

	/**
	 * 
	 *  查询记录 
	 */
	function queryRecord() {
		// 查询参数
		var workerName = txtFuzzy1.getValue();
		queryStore.baseParams = {
			workerName : workerName
		};
		queryStore.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});
		grid.getView().refresh();
	}

	/**
	 * 
	 *  增加记录 
	 */
	function addRecord() {
		strMethod = "add";
		addPanel.getForm().reset();
		win.setTitle("新增特种作业人员");
		win.show();
	}

	
	/**
	 * 
	 *  修改记录
	 */
	function updateRecord() {
		var records = grid.getSelectionModel().getSelections();
		var recordslen = records.length;
		if(recordslen > 1)
		{
			Ext.Msg.alert("提示信息", "请选择其中一项进行编辑！");
		}
		else
		{
			var rec = grid.getSelectionModel().getSelected();
		if (!rec) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
			return false;
		} else {
			strMethod = "update";
			addPanel.getForm().reset();
			win.setTitle("修改特种作业人员信息");
			win.show();
			Ext.get("spj.workerCode").dom.value=rec.get('spj.workerCode');
			Ext.get('offerId').dom.value = rec.get('spj.offerId');
			Ext.get('workerCode').dom.value = rec.get('spj.workerCode')  == null
			      ? "" : rec.get('workerName');
			Ext.get('projectOperation').dom.value = rec.get('spj.projectOperation') == null
			      ? "" : rec.get('spj.projectOperation');
			Ext.get('postYear').dom.value = rec.get('spj.postYear') == null
			      ? "" : rec.get('spj.postYear');
			Ext.get('medicalDate').dom.value = rec.get('medicalDate') == null
			      ? "" : rec.get('medicalDate');
			Ext.get('medicalResult').dom.value = rec.get('spj.medicalResult') == null
			      ? "" : rec.get('spj.medicalResult');
			Ext.get('offerName').dom.value = rec.get('spj.offerName') == null
			      ? "" : rec.get('spj.offerName');
			Ext.get('offerCode').dom.value = rec.get('spj.offerCode') == null
			      ? "" : rec.get('spj.offerCode');
			Ext.get('offerDate').dom.value = rec.get('offerDate') == null
			      ? "" : rec.get('offerDate');
			Ext.get('offerStartDate').dom.value = rec.get('offerStartDate') == null
			      ? "" : rec.get('offerStartDate');
			Ext.get('offerEndDate').dom.value = rec.get('offerEndDate') == null
			      ? "" : rec.get('offerEndDate');
			Ext.get('memo').dom.value = rec.get('spj.memo') == null
			      ? "" : rec.get('spj.memo');
		}
		}
		
	}

	/**
	 * 删除记录
	 */
	function deleteRecord() {
		var rec = grid.getSelectionModel().getSelections();
		var ids = [];
		if (rec.length > 0) {
			for (var i = 0; i < rec.length; i++) {
				var member = rec[i];
				if (member.get('spj.offerId')) {
					ids.push(member.get('spj.offerId'));
				} else {
				}
			}
			Ext.Msg.confirm(Constants.SYS_REMIND_MSG, "确认要删除所选数据？",
					function(buttonobj) {
						if (buttonobj == 'yes') {
							// 刪除
							Ext.Ajax.request({
								method : Constants.POST,
								url : 'security/deleteSpecialoperatorsInfo.action',
								params : {
									ids : ids.toString()
								},
								success : function(result, request) {
									var o = eval("(" + result.responseText
											+ ")");
									if(o.msg == "数据删除成功！")
									{
									    Ext.Msg.alert("提示信息",o.msg);
									}
										queryStore.load({
													params : {
														start : 0,
														limit : Constants.PAGE_SIZE
													}
												});
										grid.getView().refresh();

								},
								failure : function() {
									Ext.Msg.alert(Constants.ERROR,
											Constants.DEL_ERROR);
								}
							});
						}
					});
		} else {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
		}
	}

	/**
	 * 
	 * 时间的有效性检查
	 */
	function checkDate() {
		
		var offerStartDate = Ext.get('offerStartDate').dom.value;
		var dateStart = Date.parseDate(offerStartDate, 'Y-m-d');

		var offerEndDate = Ext.get('offerEndDate').dom.value;
		var dateEnd = Date.parseDate(offerEndDate, 'Y-m-d');

		if(dateStart != null && dateEnd != null)
		{
			if (dateStart.getTime() > dateEnd.getTime()) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(Constants.COM_E_006,
							"证书有效开始日期", "证书有效截止日期"));
			return false;
		    } else {
			return true;
		    }
		}
		return true;
	}

	/**
	 * 
	 *  在增加数据前的非空检查 
	 */
	function checkAddform() {
		strMsg = "";
		var workerCode = Ext.get('workerCode').dom.value;
		if (workerCode == "" || workerCode == null) {
			strMsg += String.format(Constants.COM_E_003, '"人员姓名" ') + "<br/>";
		}

		if (strMsg == "") {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 
	 *  在修改数据前的非空检查 
	 */
	function checkUpdateform() {
		strMsg = "";
        var workerCode = Ext.get('workerCode').dom.value;
		if (workerCode == "" || workerCode == null) {
			strMsg += String.format(Constants.COM_E_003, '"人员姓名" ') + "<br/>";
		}
		if (strMsg == "") {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 检查画面数据是否修改
	 */
	function isModified() {
		var rec = grid.getSelectionModel().getSelected();
		var flag = true;
		//ID
		if (Ext.get('offerId').dom.value != rec.get('spj.offerId')){
		    flag = false;
		}
		
		//员工姓名 
		if (Ext.get('workerCode').dom.value != rec.get('workerName')) 
		{
			flag = false;
		}
		//操作项目
		if (!(Ext.get('projectOperation').dom.value =="" &&rec.get('spj.projectOperation')==null) 
		      &&Ext.get('projectOperation').dom.value != rec.get('spj.projectOperation')) {
			flag = false;
		}
		//岗位年限
		if (!(Ext.get('postYear').dom.value =="" &&rec.get('spj.postYear')==null) 
		      &&Ext.get('postYear').dom.value != rec.get('spj.postYear')) {
			flag = false;
		}
		//体检日期
		if (!(Ext.get('medicalDate').dom.value =="" &&rec.get('medicalDate')==null) 
		      &&Ext.get('medicalDate').dom.value != rec.get('medicalDate')) {
			flag = false;
		}
		//体检结果
		if (!(Ext.get('medicalResult').dom.value =="" &&rec.get('spj.medicalResult')==null) 
		      &&Ext.get('medicalResult').dom.value != rec.get('spj.medicalResult')) {
			flag = false;
		}
		// 证书名称
		if (!(Ext.get('offerName').dom.value =="" &&rec.get('spj.offerName')==null) 
		      &&Ext.get('offerName').dom.value != rec.get('spj.offerName')) {
			flag = false;
		}
		// 证书编码 offerCode
		if (!(Ext.get('offerCode').dom.value =="" &&rec.get('spj.offerCode')==null) 
		      &&Ext.get('offerCode').dom.value != rec.get('spj.offerCode')) {
			flag = false;
		}
		// 发证时间 offerDate
		if (!(Ext.get('offerDate').dom.value =="" &&rec.get('offerDate')==null) 
		      &&Ext.get('offerDate').dom.value != rec.get('offerDate')) {
			flag = false;
		}
		// 证书有效开始时间offerStartDate
		if (!(Ext.get('offerStartDate').dom.value =="" &&rec.get('offerStartDate')==null) 
		      &&Ext.get('offerStartDate').dom.value != rec.get('offerStartDate')) {
			flag = false;
		}
		// 证书有效结束时间 offerEndDate
		if (!(Ext.get('offerEndDate').dom.value =="" &&rec.get('offerEndDate')==null) 
		      &&Ext.get('offerEndDate').dom.value != rec.get('offerEndDate')) {
			flag = false;
		}
		
		//备注
		if (!(Ext.get('memo').dom.value =="" &&rec.get('spj.memo')==null) 
		      &&Ext.get('memo').dom.value != rec.get('spj.memo')) {
			flag = false;
		}
		if (flag == false) {
			return true;
		} else {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_006);
			return false;
		}
	}

	/**
	 * 确定button压下的操作
	 */
	function confirmRecord() {
		if (strMethod == "update") {
			if (isModified()) {
			} else {
				return;
			}
		}

		Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_001,
				function(buttonobj) {
					if (buttonobj == 'yes') {
							var myurl = "";
							if (strMethod == "add") {
								if (checkAddform()) {
									myurl = 'security/addSpecialoperatorsInfo.action';
									if (checkDate()) {
										addPanel.getForm().submit({
											method : Constants.POST,
											url : myurl,
											success : function(form, action) {
												var result = eval('('
														+ action.response.responseText
														+ ')');
												Ext.Msg.alert("提示信息",result.msg);
													txtFuzzy1.setValue("");
		                                              queryStore.baseParams = {
														workerName : txtFuzzy1.getValue()
																	};
	 												  queryStore.load({
																params : {
																start : 0,
																limit : Constants.PAGE_SIZE
																}
															});	
													grid.getView().refresh();
													win.hide();
											},
											failure : function() {
											}
										});
									}
									return;
								} else {
									Ext.Msg.alert(Constants.SYS_REMIND_MSG, strMsg);
									return;
								}
							}
							
							if (strMethod == "update") {
								if (checkUpdateform()) {
									
									myurl = 'security/updateSpecialoperatorsInfo.action';
									if (checkDate()) {
										addPanel.getForm().submit({
											method : Constants.POST,
											url : myurl,
											success : function(form, action) {
												var result = eval('('
														+ action.response.responseText
														+ ')');
												
												// 显示成功信息
												Ext.Msg.alert("提示信息",result.msg);
												queryStore.load({
													params : {
														start : 0,
														limit : Constants.PAGE_SIZE
													}
												});
												grid.getView().refresh();
												win.hide();
											},
											failure : function(form, action) {
//												var result = eval('('
//														+ action.response.responseText
//														+ ')');
//												Ext.Msg.alert(result.msg,"提示信息");
												return;
											}
										});
								return;
								} else {
									return;
								}
						     } else {
							   return;
						     }
					         }
							


					}
				}) 			
	}
			

	/**
	 * 页面加载显示数据
	 */
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				items : [grid]

			});

		
})









							