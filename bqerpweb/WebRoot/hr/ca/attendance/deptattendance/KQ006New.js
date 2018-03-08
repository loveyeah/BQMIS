Ext.namespace("hr.Attendance");
var init;
var queryAll;//add by sychen 20100907
var queryInit;//add by sychen 20100908
var queryFlag=true;//add by sychen 20100908
hr.Attendance = function() {
	var checkFlag;
	Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
	Ext.QuickTips.init();
	// 开始日期
	var strStartDate;
	// 结束日期
	var strEndDate;
	// 颜色
	var color = '';
	// 上班休息flag
	var workOrRest = null;
	// 控件类型: 选择
	var TYPE_SELECT = "S";
	// 控件类型: 输入
	var TYPE_INPUT = "I";
	// 标识: 0
	var FLAG_0 = "0";
	// 标识: 1
	var FLAG_1 = "1";
	// 标准上午上班时间
	var amBegingTime = '';
	// 标准上午下班时间
	var amEndTime = '';
	// 标准下午上班时间
	var pmBegingTime = '';
	// 标准下午下班时间
	var pmEndTime = '';
	// 数据加载标记
	var countLoad = 0;
	// 员工id
	var empId = '';
	
	//add by sychen 20100726
	var authorizeDeptId=null;
	
	function getWorkCode() {
			Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							authorizeDeptId = result.deptId;
						}
					}
				});
	}
	//add by sychen 20100726 end
	
	// 登记年月
	var examineDate = new Ext.form.TextField({
				id : 'startDate',
				style : 'cursor:pointer',
				readOnly : true,
				width : 100,
				value : new Date().format("Y-m"),
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M',
									dateFmt : 'yyyy-MM',
									alwaysUseStartDate : true,
									alwaysUseStartDate : false,
									isShowClear : false
								});
						this.blur();
					}
				}
			});
	var storeDept = new Ext.data.JsonStore({
				root : 'list',
				url : "ca/getNewAttendanceDeptForRegister.action",
				fields : ['attendanceDeptId', 'attendanceDeptName']
			})
	storeDept.load();
	// 审核部门
	var examineDept = new Ext.form.ComboBox({
				readOnly : true,
				triggerAction : 'all',
				allowBlank : false,
				store : storeDept,
				mode : 'local',
				valueNotFoundText : '',
				displayField : 'attendanceDeptName',
				valueField : 'attendanceDeptId'
			})

	// 查询按钮
	var btnQuery = new Ext.Button({
				text : Constants.BTN_QUERY,
				iconCls : Constants.CLS_QUERY,
				handler : function() {
					queryAll();
				}
			});
	// 上报
	var btnExport = new Ext.Button({
				text : "上报",
				disabled : true,
				iconCls : "",
				handler : function() {
					if(storeMain.getCount()>0)
					{
					Ext.MessageBox.confirm(Constants.CONFIRM,
							"确定将该月部门考勤上报?", function(button, text) {
								if (button == "yes") {
									var report = new dept.Report({
												month : examineDate.getValue(),
												attendanceDeptId : examineDept.getValue(),
												workflowType : 'bqWorkAttendance'
												,authorizeDeptId:authorizeDeptId//add by sychen 20100726
											})
									report.reportWin.show();
								}
							})
					}
					else
					{
						Ext.Msg.alert("提示","无数据进行上报！");
					}
				}
			});
	// 取消
	var btnCancel = new Ext.Button({
				text : Constants.BTN_CANCEL,
				iconCls : Constants.CLS_CANCEL,
				handler : function() {
					Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_005,
							function(buttonobj) {
								if (buttonobj == "yes") {
									cls();
								}
							});
				}
			});
	// 头部工具栏
	var topBar = new Ext.Toolbar({
				items : ["登记年月<font color='red'>*</font>:", examineDate, "-",
						"考勤部门<font color='red'>*</font>:", examineDept, "-",
						btnQuery, btnExport, btnCancel]
			})

	// 定义主画面store
	var storeMain = new Ext.data.JsonStore({
				root : 'list',
				url : ''
			});
	// 定义考勤审核store
	storeMain.on('metachange', function(store, meta) {
				var item;
				for (var i = 0; i < meta.fields.length; i++) {
					item = meta.fields[i];
					if (item.renderer) {
						// 解析renderer
						
						item.renderer = eval('(' + item.renderer + ')');
					}
					if (item.sortable) {
						// 解析renderer
						item.sortable = eval('(' + item.sortable + ')');
					}
				}
				meta.fields.splice(0, 1, new Ext.grid.RowNumberer({
									header : '行号',
									width : 35,
									locked:true//add by sychen 20100730
								}));
				gridMain.getColumnModel().setConfig(meta.fields);
			});

	
	// 定义gird
			//update by sychen 20100730
	var gridMain = new Ext.grid.LockingGridPanel({
				id:'div_grid',
				region : "center",
				layout : 'fit',
				store : storeMain,
				cm : new Ext.grid.LockingColumnModel([]),
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : true
						}),
				// 头部工具栏
				// tbar : gridBar,
				enableColumnMove : false
			});
	var fullPanel = new Ext.Panel({
				tbar : topBar,
				layout : 'border',
				border : false,
				items : [
				// titleLabel,
				gridMain]
			});

//用于判断考勤部门为空时在考勤员登记页面保存后不调用queryAll查询方法 add by sychen 20100908 
 queryInit=function(){
 if (examineDept.getValue() == '') {
		queryFlag=false;
		}
}




	/**
	 * 查询函数
	 */
	queryAll=function (flag){	//update  by sychen 20100907	
//	function queryAll(flag) {
		
        getWorkCode() ;
		var msg = '';
		if (examineDate.getValue() == '') {
			msg += String.format(Constants.COM_E_003, '登记年月') + '<br />'
		};
		if (examineDept.getValue() == '') {
			msg += String.format(Constants.COM_E_003, '登记部门') + '<br />'
			
		}
		// 必须项都不为空
		if (msg == '') {
			// 日期小于当前日期
			if (!compareDateStr(examineDate.getValue(), new Date()
							.format("Y-m"))) {
				Ext.Ajax.request({
					url : 'ca/getNewAttendanceList.action',
					method : Constants.POST,
					params : {
						examineDate : examineDate.getValue(),
						examineDept : examineDept.getValue()
					},
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						var o = eval("(" + action.responseText + ")");
//						cls();
						loadMainData(o, flag);
						init();
					}
				});
			} else {
				Ext.Msg.alert(Constants.ERROR, String.format(
								Constants.COM_E_053, "登记年月"));
			}
		} else {
			Ext.Msg.alert(Constants.ERROR, msg);
		}
	}

	init = function (){
		Ext.Ajax.request({
					url : 'ca/getAttendanceInfo.action',
					method : 'post',
					params : {
						strMonth : examineDate.getValue(),
						deptId :examineDept.getValue()
					},
						success : function(action) {
						var result = eval("(" + action.responseText + ")");
                        if(result.sendState!=null&& result.sendState==1){
                          	btnExport.setDisabled(true);
                        }
                        else
                          	btnExport.setDisabled(false);
					}
				})
	
	}
	
	
	
	/**
	 * textField显示时间比较方法
	 */
	function compareDate(argDate1, argDate2) {
		return argDate1.getTime() > argDate2.getTime();
	}
	/**
	 * textField显示时间比较方法 date1>date2 返回ture
	 */
	function compareDateStr(argDateStr1, argDateStr2) {
		var date1 = Date.parseDate(argDateStr1, 'Y-m');
		var date2 = Date.parseDate(argDateStr2, 'Y-m');
		return compareDate(date1, date2);
	}

	/**
	 * 数据加载
	 */
	function loadMainData(obj, flag) {
		examineDate.setDisabled(true);
		examineDept.setDisabled(true);
		checkFlag = obj.checkFlag;
		workOrRest = obj.workOrRestList;
		color = obj.strColor;
		storeMain.loadData(obj.store);
//		controlButton(true);
		
	}
	/** 画面按钮控制 */
	function controlButton(flag) {
		btnExport.setDisabled(!flag);
	}



	/**
	 * 设置假日颜色
	 */
	function setColor(value, cellmeta, record, rowIndex, columnIndex, store) {
		if (workOrRest != null) {
			if (workOrRest[columnIndex - 6].workOrRest == '1') {
				return "<font color='" + color + "'>" + value + "</font>";
			} else {
				return value;
			}
		} else {
			return value;
		}
	}
	/**
	 * 取消事件，清空页面
	 */
	function cls(flag) {
		controlButton(false);
		storeMain.removeAll();
		gridMain.getColumnModel().setConfig([]);
		examineDate.setDisabled(false);
		examineDept.setDisabled(false);
		examineDept.clearInvalid();
	}
	/**
	 * 合计项数据格式化
	 */

	function setTotalCountNumber2(v, cellmeta, record, rowIndex, columnIndex,
			store) {
		if (v) {
			var t = '';
			v = String(v);
			while ((t = v.replace(/^(-?[0-9]+)([0-9]{3}.*)$/, '$1,$2')) !== v)
				v = t;
			return v;
		} else
			return "0";
	}
	return {
		fullPanel : fullPanel
	}
}