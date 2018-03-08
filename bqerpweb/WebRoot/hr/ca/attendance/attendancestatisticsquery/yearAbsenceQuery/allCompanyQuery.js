Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var checkFlag;
	
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
	// 审核年月
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
//	var storeDept = new Ext.data.JsonStore({
//				root : 'list',
//				url : "ca/getNewAttendanceDeptForRegister.action",
//				fields : ['attendanceDeptId', 'attendanceDeptName']
//			})
//	storeDept.load();
//	// 审核部门
//	var examineDept = new Ext.form.ComboBox({
//				readOnly : true,
//				triggerAction : 'all',
//				allowBlank : false,
//				store : storeDept,
//				mode : 'local',
//				valueNotFoundText : '',
//				displayField : 'attendanceDeptName',
//				valueField : 'attendanceDeptId'
//			})
			
	var examineDept = new Ext.ux.ComboBoxTree({
		fieldLabel : '部门',
		allowBlank : true,
		width : 200,
		id : "attendanceDeptId",
		anchor : '90%',
		displayField : 'text',
		valueField : 'id',
		hiddenName : 'attendanceDeptName',
		blankText : '请选择',
		emptyText : '请选择',
		tree : {
			xtype : 'treepanel',
			rootVisible : false,

			loader : new Ext.tree.TreeLoader({
				dataUrl : 'ca/getAttendanceDeptData.action'
			}),
			root : new Ext.tree.AsyncTreeNode({
				id : '0',
				name : '灞桥热电厂',
				text : '灞桥热电厂'
			}),
			listeners : { 
				click : function(node, e) {
					txtDeptHid.setValue(node.attributes.id);
					txtDeptName.setValue(node.attributes.text);
				}
			}
		},
		selectNodeModel : 'all'
	});
	
	var txtDeptHid = new Ext.form.Hidden();
	var txtDeptName = new Ext.form.TextField();

	// 查询按钮
	var btnQuery = new Ext.Button({
				text : Constants.BTN_QUERY,
				iconCls : Constants.CLS_QUERY,
				handler : function() {
					queryAll();
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
				items : ["月份<font color='red'>*</font>:", examineDate, "-",
						"考勤部门:", examineDept, "-",
						btnQuery, btnCancel]
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







	/**
	 * 查询函数
	 */
	function queryAll(flag) {
		
		var msg = '';
		if (examineDate.getValue() == '') {
			msg += String.format(Constants.COM_E_003, '审核年月') + '<br />'
		};
//		if (examineDept.getValue() == '') {
//			msg += String.format(Constants.COM_E_003, '审核部门') + '<br />'
//		}
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

						loadMainData(o, flag);
					}
				});
			} else {
				Ext.Msg.alert(Constants.ERROR, String.format(
								Constants.COM_E_053, "审核年月"));
			}
		} else {
			Ext.Msg.alert(Constants.ERROR, msg);
		}
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
//		examineDate.setDisabled(true);
//		examineDept.setDisabled(true);
		checkFlag = obj.checkFlag;
		workOrRest = obj.workOrRestList;
		color = obj.strColor;
		storeMain.loadData(obj.store);
		
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
		
		storeMain.removeAll();
		gridMain.getColumnModel().setConfig([]);
//		examineDate.setDisabled(false);
//		examineDept.setDisabled(false);
		examineDept.clearInvalid();
		examineDept.setValue("");
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
	
new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [{
							layout : 'fit',
							border : false,
							frame : false,
							region : "center",
							items : [fullPanel]
						}]
			});

});