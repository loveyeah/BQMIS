// 安措计划项目完成情况

Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.QuickTips.init();
Ext.onReady(function() {
	var strMethod;
	var strMsg;
	
	
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result.workerCode) {
					// 设定工作负责人为登录人
					appraisalByH.setValue(result.workerCode);
					Ext.get("appraisalBy").dom.value = result.workerName
							? result.workerName
							: '';
				}
			}
		});
	}
	// 数字格式化
	function numberFormat(value){
			if (value == null) {
			return "0.0000";
		}
		   value = String(value);
            // 整数部分
            var whole = value;
            // 小数部分
            var sub = ".0000";
            // 如果有小数
		    if (value.indexOf(".") > 0) {
		    	whole = value.substring(0, value.indexOf("."));
			    sub = value.substring(value.indexOf("."), value.length);
			    sub = sub + "0000";
			    if(sub.length > 5){
			    	sub = sub.substring(0,5);
			    }
		    }
            var r = /(\d+)(\d{3})/;
            while (r.test(whole)){
                whole = whole.replace(r, '$1' + ',' + '$2');
            }
            if(whole == null || whole == "")
            {
            	whole = 0;
            }
            v = whole + sub;
            return v;
	}
	
    function getCurrentDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1+"-";
		y = d.getDay();
		s += (t > 9 ? "" : "0") + t +(y > 9 ? "" : "0")+ y;
		return s;
	}
	// ============== 定义grid ===============
	var record = Ext.data.Record.create([
		{
		    // 安措计划项目ID1
		    name : 'spjs.securityPlanId'
		},{
			// 项目名称2
			name : 'spjs.planName'
		},{
			// 立项依据3
			name : 'spjs.planBasis'
		},{
			// 费用4
			name : 'spjs.fee'
		},{
			// 年度5
			name : 'spjs.year'
		},{
			// 完成期限6
			name : 'spjs.finishDate'
		},{
			// 负责人7
			name : 'spjs.chargeBy'
		},{
			// 负责部门8
			name : 'spjs.chargeDep'
		},{
			// 备注9
			name : 'spjs.memo'
		},{
			// 填报人10
			name : 'spjs.fillBy'
		},{
			// 填报部门11
			name : 'spjs.fillDep'
		},{
			// 填报日期12
			name : 'spjs.fillDate'
		},{
			// 完成情况13
			name : 'spjs.finishState'
		},{
			// 完成评价14
			name : 'spjs.finishAppraisal'
		},{
			// 评价人15
			name : 'spjs.appraisalBy'
		},{
			// 评价时间16
			name : 'spjs.appraisalDate'
		},{
			// 完成期限17
			name : 'finishDate'
		},{
			// 负责人姓名18
			name : 'chargeName'
		},{
			// 负责部门名称19
			name : 'chargeDepName'
		},{
			// 填报人姓名20
			name : 'fillName'
		},{
			// 填报部门名称21
			name : 'fillDepName'
		},{
			// 填报日期22
			name : 'fillDate'
		},{
			// 评价人姓名23
			name : 'appraisalName'
		},{
			// 评价日期
			name : 'appraisalDate'
		}
		])

	// 定义获取数据源
	var dataProxy = new Ext.data.HttpProxy({
				url : 'security/findSpJSecurityPlanList.action'
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
				emptyText : "（项目名称，年度）"
			});

	//定义选择列
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
									dataIndex : 'spjs.securityPlanId',
									hidden : true,
									sortable : true
								},
								{
							header : "项目名称",
							dataIndex : 'spjs.planName',
							sortable : true
						}, {
							header : "年度",
							dataIndex : 'spjs.year',
							align : 'center',
							sortable : true
						}, {
							header : "费用（万元）",
							dataIndex : 'spjs.fee',
							renderer : numberFormat,
							align : 'right',
							sortable : true
						},{
							header : '负责人',
							dataIndex : 'chargeName',
							align : 'center',
							sortable : true
						},{
							header : '完成情况',
							dataIndex : 'spjs.finishState',
							sortable : true
						},{
							header : '完成评价',
							dataIndex : 'spjs.finishAppraisal',
							sortable : true
						},{
							header : '完成期限',
							dataIndex : 'finishDate',
							sortable : true
						},{
							header : '备注',
							dataIndex : 'spjs.memo',
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
						}
//						, "-", {
//							text : Constants.BTN_UPDATE,
//							iconCls : Constants.CLS_UPDATE,
//							handler : updateRecord
						],

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
    
    //!! ******************弹出画面组件***********************!!
    // ID
    var securityPlanId = new Ext.form.Hidden({
    	id : 'securityPlanId',
	    name : 'spjs.securityPlanId'
    });
    // 项目名称
    var planName = new Ext.form.TextField({
    	fieldLabel : '项目名称',
        id : 'planName',
        name : 'spjs.planName',
        readOnly : true,
        width : 405
    });
    // 立项依据
    var planBasis = new Ext.form.TextArea({
    	id : 'planBasis',
        name : "spjs.planBasis",
        fieldLabel : '立项依据',
        readOnly : true,
        width : 405
    });
    // 费用
    var fee = new Ext.form.NumberField({
	     fieldLabel : '费用（万元）',
		 id : 'fee',
		 labelAlign : 'right',
		 width : 408,
		 name : 'spjs.fee',
//		 emptyText : '0.0000',
		 allowNegative : false,
		 allowDecimals : true,
		 readOnly : true,
		 decimalPrecision : 4
	})
	fee.on('blur',function(){
	    Ext.get('fee').dom.value = numberFormat(Ext.get('fee').dom.value)
	});
	// 年度
	var year = new Ext.form.NumberField({
		fieldLabel : '年度',
        id : 'year',
        name : 'spjs.year',
        emptyText : '',
        width : 180,
        readOnly : true,
        anchor : "85%"
	});
	var finishDate = new Ext.form.TextField({
		id : 'finishDate',
        name : 'spjs.finishDate',
        format : 'Y-m-d',
        width : 180,
        fieldLabel : '完成期限',
        readOnly : true,
        anchor : "85%"
	});
	// 负责人
	var chargeBy ={
		ctCls : "",
		hideTrigger : true,
		readOnly : true,
		fieldLabel : '负责人',
		name : 'chargeBy',
		xtype : 'combo',
		id : 'chargeBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'spjs.chargeBy',
		allowBlank : false,
		editable : false,
		anchor : "85%"
	};
	var chargeDepH = new Ext.form.Hidden({
	   id : 'chargeDepH',
	   name : 'spjs.chargeDep'
	})
	//负责部门
	var chargeDep = new Ext.form.TextField({
		readOnly : true,
		fieldLabel : '负责部门',
		id : 'chargeDep',
		name : 'chargeDep',
		readOnly:true,
		width : 180,
		anchor : "85%"
	});
	// 备注
	var memo = new Ext.form.TextArea({
		readOnly : true,
        id : 'memo',
        name : "spjs.memo",
        fieldLabel : '备注',
        width : 405 
    });
    // 填报人
    var fillBy ={
    	hideTrigger : true,
    	readOnly : true,
		fieldLabel : '填报人',
		name : 'fillBy',
		xtype : 'combo',
		id : 'fillBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'spjs.fillBy',
		allowBlank : false,
		editable : false,
		anchor : "42.5%"
	};
	var fillDepH = new Ext.form.Hidden({
	   id : 'fillDepH',
	   name : 'spjs.fillDep'
	})
	// 填报部门
	var fillDep = new Ext.form.TextField({
		readOnly : true,
		fieldLabel : '填报部门',
		id : 'fillDep',
		name : 'fillDep',
		readOnly:true,
		width : 180,
		anchor : "85%"
	});
	// 填报日期
	var fillDate = new Ext.form.TextField({
		readOnly : true,
		fieldLabel : '填报日期',
		id : 'fillDate',
		name : 'spjs.fillDate',
		//disabled : true,
		readOnly:true,
		width : 180,
		anchor : "85%",
		value : getCurrentDate()
	});
	// 完成情况
	var finishState = new Ext.form.TextField({
		fieldLabel : '完成情况',
		id : 'finishState',
		name : 'spjs.finishState',
		width : 408
	});
	// 完成评价
	var finishAppraisal = new Ext.form.TextArea({
        id : 'finishAppraisal',
        name : "spjs.finishAppraisal",
        fieldLabel : '完成评价',
        width : 405 
    });
    // 评价人
    var appraisalBy = new Ext.form.TextField({
    	readOnly : true,
		fieldLabel : '评价人',
		id : 'appraisalBy',
		name : 'appraisalBy',
		editable : false,
		anchor : '85%',
		width : 180
	});
	var appraisalByH = new Ext.form.Hidden({
	   id : 'appraisalByH',
	   name : 'spjs.appraisalBy'
	})
	// 评价时间
	var appraisalDate = new Ext.form.TextField({
		readOnly : true,
		fieldLabel : '评价时间',
		id : 'appraisalDate',
		name : 'spjs.appraisalDate',
		editable : false,
		width : 180,
		anchor : '85%',
		value : getCurrentDate()
	});
    //...................弹出画面组件..........................
	// Form
	var addPanel = new Ext.FormPanel({
				layout : 'column',
				frame : true,
				labelAlign : 'right',
				items : [
					securityPlanId
					,{
						columnWidth : 1,
						layout : 'form',
						border : false,
						labelWidth : 80,
						items : [planName]
					},{
						columnWidth : 1,
						layout : 'form',
						border : false,
						labelWidth : 80,
						items : [planBasis]
					},{
						columnWidth : 1,
						layout : 'form',
						border : false,
						labelWidth : 80,
						items : [fee]
					},{
						columnWidth : 0.5,
						layout : 'form',
						border : false,
						labelWidth : 80,
						items : [year]
					},{
						columnWidth : 0.5,
						layout : 'form',
						border : false,
						labelWidth : 80,
						items : [finishDate]
					},{
						columnWidth : 0.5,
						layout : 'form',
						border : false,
						labelWidth : 80,
						items : [chargeBy]
					},{
						columnWidth : 0.5,
						layout : 'form',
						border : false,
						labelWidth : 80,
						items : [chargeDep,chargeDepH]
					},{
						columnWidth : 1,
						layout : 'form',
						border : false,
						labelWidth : 80,
						items : [memo]
					},{
						columnWidth : 1,
						layout : 'form',
						border : false,
						labelWidth : 80,
						items : [fillBy]
					},{
						columnWidth : 0.5,
						layout : 'form',
						border : false,
						labelWidth : 80,
						items : [fillDep,fillDepH]
					},{
						columnWidth : 0.5,
						layout : 'form',
						border : false,
						labelWidth : 80,
						items : [fillDate]
					}
					,{
						columnWidth : 1,
						layout : 'form',
						border : false,
						labelWidth : 80,
						items : [finishState]
					},{
						columnWidth : 1,
						layout : 'form',
						border : false,
						labelWidth : 80,
						items : [finishAppraisal]
					},{
						columnWidth : 0.5,
						layout : 'form',
						border : false,
						labelWidth : 80,
						items : [appraisalBy,appraisalByH]
					}
					,{
						columnWidth : 0.5,
						layout : 'form',
						border : false,
						labelWidth : 80,
						items : [appraisalDate]
					}
				     ]
			});

	// 弹出窗口
	var win = new Ext.Window({
				width : 560,
				height : 500,
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
		var queryString = txtFuzzy1.getValue();
		queryStore.baseParams = {
			queryString : queryString
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
	 *  修改记录
	 */
	function updateRecord() {
		getWorkCode()
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
			win.setTitle("修改安措项目计划");
			win.show();
			Ext.get('securityPlanId').dom.value = rec.get('spjs.securityPlanId');
			Ext.get('planName').dom.value = rec.get('spjs.planName') 
			== null  ? "" : rec.get('spjs.planName');
			Ext.get('planBasis').dom.value = rec.get('spjs.planBasis') 
			== null     ? "" : rec.get('spjs.planBasis');
			Ext.get('fee').dom.value = rec.get('spjs.fee') 
			== null      ? "" : numberFormat(rec.get('spjs.fee'));
			Ext.get('year').dom.value = rec.get('spjs.year') 
			 == null     ? "" : rec.get('spjs.year');
			Ext.get('finishDate').dom.value = rec.get('finishDate') 
			== null      ? "" : rec.get('finishDate');
			Ext.get('spjs.chargeBy').dom.value = rec.get('spjs.chargeBy')
			Ext.get('chargeBy').dom.value = rec.get('spjs.chargeBy') 
			== null      ? "" : rec.get('chargeName');
			Ext.get('chargeDep').dom.value = rec.get('chargeDepName') 
			== null      ? "" : rec.get('chargeDepName');
			Ext.get('memo').dom.value = rec.get('spjs.memo') 
			== null      ? "" : rec.get('spjs.memo');
			Ext.get('spjs.fillBy').dom.value = rec.get('spjs.fillBy')
			Ext.get('fillBy').dom.value = rec.get('fillName') 
			== null      ? "" : rec.get('fillName');
			Ext.get('fillDepH').dom.Value = rec.get('spjs.fillDep')
			Ext.get('fillDep').dom.value = rec.get('fillDepName') 
			== null     ? "" : rec.get('fillDepName');
			Ext.get('fillDate').dom.value = rec.get('fillDate') 
			== null      ? "" : rec.get('fillDate');
			Ext.get('finishState').dom.value = rec.get('spjs.finishState') 
			== null      ? "" : rec.get('spjs.finishState');
			Ext.get('finishAppraisal').dom.value = rec.get('spjs.finishAppraisal') 
			== null      ? "" : rec.get('spjs.finishAppraisal');
//			Ext.get('spjs.appraisalBy').dom.value = rec.get('spjs.appraisalBy')
//			Ext.get('appraisalBy').dom.value = rec.get('appraisalName') 
//			== null      ? "" : rec.get('appraisalName');
//			Ext.get('appraisalDate').dom.value = rec.get('appraisalDate') 
//			== null      ? "" : rec.get('appraisalDate');
//			
		}
		}
		
	}




	/**
	 * 
	 *  在修改数据前的非空检查 
	 */
	function checkUpdateform() {
		strMsg = "";
        var planName = Ext.get('planName').dom.value;
		if (planName == "" || planName == null) {
			strMsg += String.format(Constants.COM_E_003, '"项目名称" ') + "<br/>";
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
		if (Ext.get('securityPlanId').dom.value != rec.get('spjs.securityPlanId')){
		    flag = false;
		}
		
		//项目名称 
		if (!(Ext.get('planName').dom.value == "" &&rec.get('spjs.planName') == null)
		    &&Ext.get('planName').dom.value != rec.get('spjs.planName')) 
		{
			flag = false;
		}
		//立项依据
		if (!(Ext.get('planBasis').dom.value =="" &&rec.get('spjs.planBasis')==null) 
		      &&Ext.get('planBasis').dom.value != rec.get('spjs.planBasis')) {
			flag = false;
		}
		//费用
		if (!(Ext.get('fee').dom.value =="" &&rec.get('spjs.fee')==null) 
		      &&Ext.get('fee').dom.value != rec.get('spjs.fee')) {
			flag = false;
		}
		//年度
		if (!(Ext.get('year').dom.value =="" &&rec.get('spjs.year')==null) 
		      &&Ext.get('year').dom.value != rec.get('spjs.year')) {
			flag = false;
		}
		//完成期限
		if (!(Ext.get('finishDate').dom.value =="" &&rec.get('finishDate')==null) 
		      &&Ext.get('finishDate').dom.value != rec.get('finishDate')) {
			flag = false;
		}
		// 负责人
		if (!(Ext.get('chargeBy').dom.value =="" &&rec.get('chargeName')==null) 
		      &&Ext.get('chargeBy').dom.value != rec.get('chargeName')) {
			flag = false;
		}
		// 负责部门名
		if (!(Ext.get('chargeDep').dom.value =="" &&rec.get('chargeDepName')==null) 
		      &&Ext.get('chargeDep').dom.value != rec.get('chargeDepName')) {
			flag = false;
		}
		// 备注
		if (!(Ext.get('memo').dom.value =="" &&rec.get('spjs.memo')==null) 
		      &&Ext.get('memo').dom.value != rec.get('spjs.memo')) {
			flag = false;
		}
		// 填报人
		if (!(Ext.get('fillBy').dom.value =="" &&rec.get('fillName')==null) 
		      &&Ext.get('fillBy').dom.value != rec.get('fillName')) {
			flag = false;
		}
		// 填报部门名
		if (!(Ext.get('fillDep').dom.value =="" &&rec.get('fillDepName')==null) 
		      &&Ext.get('fillDep').dom.value != rec.get('fillDepName')) {
			flag = false;
		}
		
		//填报日期
		if (!(Ext.get('fillDate').dom.value =="" &&rec.get('fillDate')==null) 
		      &&Ext.get('fillDate').dom.value != rec.get('fillDate')) {
			flag = false;
		}
		// 完成情况
		if (!(Ext.get('finishState').dom.value =="" &&rec.get('spjs.finishState')==null) 
		      &&Ext.get('finishState').dom.value != rec.get('spjs.finishState')) {
			flag = false;
		}
		// 完成评价
		if (!(Ext.get('finishAppraisal').dom.value =="" &&rec.get('spjs.finishAppraisal')==null) 
		      &&Ext.get('finishAppraisal').dom.value != rec.get('spjs.finishAppraisal')) {
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
							if (strMethod == "update") {
								if (checkUpdateform()) {
									
									myurl = 'security/updateSpJSecurityPlanInfo.action';
									{
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
												return;
											}
										});
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









							