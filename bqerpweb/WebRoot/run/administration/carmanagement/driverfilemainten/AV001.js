Ext.QuickTips.init();
Ext.onReady(function() {
	
	// 导出按钮
	var btnExport = new Ext.Button({
				id : "export",
				text : Constants.BTN_EXPORT,
				iconCls : Constants.CLS_EXPORT,
				disabled : true,
				handler : exportIt
			});
	// 查询按钮
	var btnQuery = new Ext.Button({
				id : "query2",
				text : Constants.BTN_QUERY,
				iconCls : Constants.CLS_QUERY,
				handler : queryIt2
			});
	// 所在部门名称
	var txtDepName2 = new Ext.form.TextField({
				id : "txtDepName2",
				name : "txtDepName2",
				readOnly : true,
				width : 100
			});
	txtDepName2.onClick(selectDep2);
	// 姓名
    var drpName = new Ext.form.CmbWorkerByDept({
				id : "strName",
				name : "strName",
				fieldLabel : "姓名",
				maxLength : 15,
				width : 100
			});
	// 驾照类型
	var drpLicence2 = new Ext.form.ComboBox({
				id : "drpLicence2",
				name : "drpLicence2",
				fieldLabel : "驾照类型",
				width : 70,
				readOnly : true,
				triggerAction : 'all',
				mode : 'local',
				displayField : 'text',
				valueField : 'value',
				store : new Ext.data.JsonStore({
							fields : ['value', 'text'],
							data : [{
										value : '',
										text : ''
									}, {
										value : 'A',
										text : 'A'
									}, {
										value : 'B',
										text : 'B'
									}, {
										value : 'C',
										text : 'C'
									}]
						})
			});
	// 部门编码
	var hdnDepCode = new Ext.form.Hidden({
				value : ""
			});
	// 顶部工具栏
	var tbar2 = new Ext.Toolbar({
				items : ["所在部门:", txtDepName2, "-", "姓名:", drpName, "-",
						"驾照类型:", drpLicence2, "-", btnQuery, btnExport,hdnDepCode]
			});

	// 导出按钮处理函数
	function exportIt() {
		Ext.MessageBox.confirm(MessageConstants.SYS_CONFIRM_MSG,
				MessageConstants.COM_C_007, function(button, text) {
					if(button=="yes"){
						document.all.blankFrame.src = "administration/exportDriverFile.action";
					}
				})
	}
	// 选择部门处理函数
	function selectDep2(){
		var args = {selectModel:'single',rootNode:{id:'0',text:'合肥电厂'}};
		var object = window.showModalDialog(
						'../../../../comm/jsp/hr/dept/dept.jsp',
						args,
						'dialogWidth=500px;dialogHeight=320px;center=yes;help=no;resizable=no;status=no;');
		// 根据返回值设置画面的值
		if (object) {
			drpName.store.removeAll();
			drpName.setValue("");
			if (typeof(object.names) != "undefined") {
				txtDepName2.setValue(object.names);
			}
			if (typeof(object.codes) != "undefined") {
				hdnDepCode.setValue(object.codes);
				drpName.store.load({
				    params : {
				    	strDeptCode : object.codes
				    }
				});
			}
		}
	}
	// 查询按钮处理函数
	function queryIt2() {
		store2.baseParams.strDepCode = hdnDepCode.getValue();
		store2.baseParams.strWorkerCode = drpName.getValue();
		store2.baseParams.strLicence = drpLicence2.getValue();
		Ext.Ajax.request({
					url : "administration/driverFileQuery.action",
					method : "post",
					params : {
						start : 0,
						limit : 18,
						strDepCode : hdnDepCode.getValue(),
						strWorkerCode : drpName.getValue(),
						strLicence : drpLicence2.getValue()
					},
					success : function(result, request) {
						var gridData = eval('(' + result.responseText + ')');
						if((result.msg != null) && (result.msg == Constants.SQL_FAILURE)){
								Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,MessageConstants.COM_E_014);
								return;
						}
						if((result.msg != null) && (result.msg == Constants.DATE_FAILURE)){
								Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,MessageConstants.COM_E_023);
								return;
						}
						store2.loadData(gridData);
					},
					failure : function(result, request) {}
				});
	}
	function showSex(value){
		if(value == "M"){
			return "男";
		}
		if(value == "W"){
			return "女";
		}
		return "";
	}
	// grid选择模式设为单行选择模式
    var sm2 = new Ext.grid.RowSelectionModel({
	       singleSelect : true
	});
	// grid中的列
	var cm2 = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
						header : "行号",
						width : 35
					}), {
				header : "ID",
				width : 75,
				sortable : true,
				dataIndex : 'id',
				hidden : true
			}, {
				header : "姓名",
				dataIndex : "name"
			}, {
				header : "性别",
				width : 75,
				sortable : true,
				dataIndex : 'sex',
				renderer : showSex
			}, {
				header : "年龄",
				width : 75,
				sortable : true,
				dataIndex : 'ages'
			}, {
				header : "所在部门",
				dataIndex : "depName",
				align : "left"
			}, {
				header : "驾照类型",
				dataIndex : "licence",
				align : "left"
			}, {
				header : "驾照号码",
				width : 75,
				sortable : true,
				dataIndex : 'licenceNo'
			}, {
				header : "办照日期",
				dataIndex : "licenceDate",
				align : "left"
			}, {
				header : "年检日期",
				dataIndex : "checkDate",
				align : "left"
			}, {
				header : "手机号码",
				width : 75,
				sortable : true,
				dataIndex : 'mobileNo'
			}, {
				header : "家庭电话",
				width : 75,
				sortable : true,
				dataIndex : 'telNo'
			}, {
				header : "家庭住址",
				width : 75,
				sortable : true,
				dataIndex : 'homeAddr'
			}, {
				header : "通讯地址",
				width : 75,
				sortable : true,
				dataIndex : 'comAddr'
			}]);
	cm2.defaultSortable = true;
	// grid中的数据
	var recordDriver2 = Ext.data.Record.create([{
				name : "id"
			}, {
				name : "name"
			}, {
				name : "sex"
			}, {
				name : "ages"
			}, {
				name : "depName"
			}, {
				name : "licence"
			}, {
				name : "licenceNo"
			}, {
				name : "licenceDate"
			}, {
				name : "checkDate"
			}, {
				name : "mobileNo"
			}, {
				name : "telNo"
			}, {
				name : "homeAddr"
			}, {
				name : "comAddr"
			}]);
	// grid中的store
	var store2 = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "administration/driverFileQuery.action"
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, recordDriver2)
			});
	store2.baseParams = ({
		strDepCode : hdnDepCode.getValue(),
		strWorkerCode : drpName.getValue(),
		strLicence : drpLicence2.getValue()
	});
	// 注册store的load事件
	store2.on("load", function() {
				if (this.getTotalCount() > 0) {
					btnExport.setDisabled(false);
				} else {
					Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
							MessageConstants.COM_I_003);
					btnExport.setDisabled(true);
				}
			});
	// 底部工具栏
	var bbar2 = new Ext.PagingToolbar({
				pageSize : 18,
				store : store2,
				displayInfo : true,
				displayMsg : MessageConstants.DISPLAY_MSG,
				emptyMsg : MessageConstants.EMPTY_MSG
			})
	// grid主体
	var grid2 = new Ext.grid.GridPanel({
				autoScroll : true,
				region : "center",
				layout : "fit",
				colModel : cm2,
				sm : sm2,
				tbar : tbar2,
				bbar : bbar2,
				enableColumnMove : false,
				store : store2,
				autoSizeColumns : true,
				autoSizeHeaders : false
			});
	
	
	
	
	// 允许输入的数据
	var strAllow = "0123456789";
	// 隐藏ID
	var hdnId = new Ext.form.Hidden({
				id : "driverFile.id",
				name : "driverFile.id",
				value : ""
			});
	// 隐藏修改时间
	var hdnUpdateTime = new Ext.form.Hidden({
				id : "driverFile.updateTime",
				name : "driverFile.updateTime",
				value : ""
			});
    // 姓名
	var drpDriverName2 = new Ext.form.TextField({
				id : "drpDriverName2",
				name : "drpDriverName2",
				fieldLabel : "姓名<font color='red'>*</font>",
				maxLength : 13,
				allowBlank : false,
				readOnly : true,
				width : 120
			});
	drpDriverName2.onClick(selectName2);
    // 隐藏人员编码
	var hdnDriverCode = new Ext.form.Hidden({
				id : "driverFile.driverCode",
				name : "driverFile.driverCode",
				value : ""
			});
	// 所在部门名称
	var txtDepName = new Ext.form.TextField({
				id : "formDepName",
				name : "formDepName",
				readOnly : true,
				fieldLabel : "所在部门<font color='red'>*</font>",
				allowBlank : false,
				width : 120
			});
	// 性别
	var txtSex = new Ext.form.TextField({
				id : "formSex",
				name : "formSex",
				fieldLabel : "性别",
				readOnly : true,
				width : 120
			});
	// 驾照类型
	var drpLicence = new Ext.form.ComboBox({
				id : "driverFile.licence",
				name : "driverFile.licence",
				fieldLabel : "驾照类型",
				width : 120,
				readOnly : true,
				triggerAction : 'all',
				mode : 'local',
				displayField : 'text',
				valueField : 'value',
				store : new Ext.data.JsonStore({
							fields : ['value', 'text'],
							data : [{
										value : '',
										text : ''
									}, {
										value : 'A',
										text : 'A'
									}, {
										value : 'B',
										text : 'B'
									}, {
										value : 'C',
										text : 'C'
									}]
						})
			});
	// 办照时间控件
	var txtGetLicenceDate = new Ext.form.TextField({
				id : 'driverFile.licenceDate',
				name : 'driverFile.licenceDate',
				fieldLabel : "办照日期",
				width : 120,
				style : 'cursor:pointer',
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd'
								});
					}
				}
			});
	// 手机号码
    var txtMobileNo = new Ext.form.NumberField({
		id : "driverFile.mobileNo",
		name : "driverFile.mobileNo",
		fieldLabel : "手机号码",
		allowDecimals : false,
		maxLength : 15,
		width : 120,
		parseValue : function(value){
            return value;
        },
        fixPrecision : function(value){
            return value;
        },
        setValue : function(value){
            Ext.form.NumberField.superclass.setValue.call(this, value);
        },
        getValue : function(){
            return Ext.form.NumberField.superclass.getValue.call(this);
        }
	});
	// 年龄
    var txtAge = new Ext.form.TextField({
				id : "formAge",
				name : "formAge",
				fieldLabel : "年龄",
				maxLength : 3,
				readOnly : true,
				width : 120
			});
	// 驾照号码
    var txtLicenceNo = new Ext.form.TextField({
				id : "driverFile.licenceNo",
				name : "driverFile.licenceNo",
				fieldLabel : "驾照号码",
				maxLength : 10,
				width : 120
			});
	// 年检时间控件
	var txtCheckDate = new Ext.form.TextField({
				id : 'driverFile.checkDate',
				name : 'driverFile.checkDate',
				fieldLabel : "年检日期",
				width : 120,
				style : 'cursor:pointer',
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd'
								});
					}
				}
			});
	// 家庭电话
    var txtTelNo = new Ext.form.NumberField({
		id : "driverFile.telNo",
		name : "driverFile.telNo",
		fieldLabel : "家庭电话",
		allowDecimals : false,
		maxLength : 15,
		width : 120,
		parseValue : function(value){
            return value;
        },
        fixPrecision : function(value){
            return value;
        },
        setValue : function(value){
            Ext.form.NumberField.superclass.setValue.call(this, value);
        },
        getValue : function(){
            return Ext.form.NumberField.superclass.getValue.call(this);
        }
	});

    // 家庭住址
	var txaHomeAddr = new Ext.form.TextArea({
		        id : "driverFile.homeAddr",
				name : "driverFile.homeAddr",
	            fieldLabel : "家庭住址",
	            maxLength : 50,
	            height : 45,
	            anchor : "94%"
	
	});
	// 通讯地址
	var txaComAddr = new Ext.form.TextArea({
		        id : "driverFile.comAddr",
				name : "driverFile.comAddr",
	            fieldLabel : "通讯地址",
	            height : 45,
	            maxLength : 50,
	            anchor : "94%"
	
	});
	var fs = new Ext.Panel({
		height : "100%",
		width : 450,
		layout : "form",
		border : false,
		buttonAlign : "center",
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		items : [{
			border : false,
			layout : "column",
			items : [{
				columnWidth : 0.5,
				border : false,
				layout : "form",
				items : [hdnId, hdnDriverCode, hdnUpdateTime,
						drpDriverName2, txtSex, drpLicence,
						txtGetLicenceDate, txtMobileNo]
			}, {
				columnWidth : 0.5,
				border : false,
				layout : "form",
				items : [txtDepName, txtAge, txtLicenceNo, txtCheckDate,
						txtTelNo]
			}]
		}, txaHomeAddr, txaComAddr]
	});
	// 增加或修改面板
	var fp = new Ext.form.FormPanel({
				id : "form",
				labelAlign : "right",
				labelWidth : 75,
				frame : true,
				items : [fs]
			});
	function selectName2() {
		if (drpDriverName2.flag) {
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '合肥电厂'
				}
			}
			var object = window
					.showModalDialog(
							'../../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
							args,
							'dialogWidth:550px;dialogHeight:320px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			// 根据返回值设置画面的值
			if (object) {
				if (typeof(object.workerName) != "undefined") {
					if (object.workerName != "") {
						Ext.Ajax.request({
							url : "administration/driverFileEmployeeQuery.action",
							method : "post",
							params : {
								strWorkerCode : object.workerCode
							},
							success : function(result, request) {
								var data = eval("(" + result.responseText + ")");
								if ((result.msg != null)
										&& (result.msg == Constants.SQL_FAILURE)) {
									Ext.Msg.alert(
											MessageConstants.SYS_ERROR_MSG,
											MessageConstants.COM_E_014);
									return;
								}
								if (data.sex == "M") {
									txtSex.setValue("男");
								} else if (data.sex == "W") {
									txtSex.setValue("女");
								} else {
									txtSex.setValue("");
								}
								txtAge.setValue(data.age);
							},
							failure : function(result, request) {
							}
						});
					} else {
						txtSex.setValue("");
						txtAge.setValue("");
					}
					drpDriverName2.setValue(object.workerName);
				}
				if (typeof(object.workerCode) != "undefined") {
					hdnDriverCode.setValue(object.workerCode);
				}
				if (typeof(object.deptName) != "undefined") {
					txtDepName.setValue(object.deptName);
				}
			}
		}
	}
	// 保存按钮处理函数
	function save() {
		// 姓名不能为空
        if(drpDriverName2.getValue()==""){
        	Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
							MessageConstants.COM_E_002, "姓名"));
			drpDriverName2.focus();
            return;						
        }
		// 所在部门不能为空
        if(txtDepName.getValue()==""){
        	Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
							MessageConstants.COM_E_002, "所在部门"));
			txtDepName.focus();
            return;						
        }
		Ext.MessageBox.confirm(MessageConstants.SYS_CONFIRM_MSG,
				MessageConstants.COM_C_001, function(button, text) {
					if (button == "yes") {
						fp.getForm().submit({
							url : "administration/"
									+ (Ext.get("driverFile.id").dom.value == ""
											? "addDriverFile"
											: "modifyDriverFile") + ".action",
							method : "post",
							success : function(form, action) {
								var result = eval('('
										+ action.response.responseText + ')');
								if ((result.msg != null)
										&& (result.msg == Constants.SQL_FAILURE)) {
									Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
											MessageConstants.COM_E_014);
									return;
								}
                                if ((result.msg != null)
										&& (result.msg == Constants.ADD_DRIVERCODE_FAILURE)) {
									Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
											String.format(MessageConstants.COM_E_007, "司机档案"));
									return;
								}
								Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
										MessageConstants.COM_I_004, function() {
											win.hide();
											store.load({
														params : {
															start : 0,
															limit : Constants.PAGE_SIZE
														}
													});
										});

								grid.getView().refresh();
							},
							failure : function(form, action) {
							}
						});
					}
				});
	}
	// 取消按钮处理函数
	function cancel() {
		Ext.MessageBox.confirm(MessageConstants.SYS_CONFIRM_MSG,
				MessageConstants.COM_C_005, function(button, text) {
					if (button == "yes") {
						win.hide();
					}
				});
	}
	// 查询窗口
	var win2 = new Ext.Window({
		        title : "司机档案查询",
				modal : true,
				width : 540,
				layout : "border",
				height : 320,
				closeAction : "hide",
				resizable : false,
				items : [grid2]
			});
	// 编辑窗口
	var win = new Ext.Window({
				modal : true,
				width : 500,
				height : 320,
				layout : 'fit',
				closeAction : "hide",
				resizable : false,
				autoScroll : true,
				buttonAlign : 'center',
				items : [fp],
				buttons : [{
							text : Constants.BTN_SAVE,
							iconCls : Constants.CLS_SAVE,
							handler : save
						}, {
							text : Constants.BTN_CANCEL,
							iconCls : Constants.CLS_CANCEL,
							handler : cancel
						}]
			});
	// 工具栏
	var tbar = new Ext.Toolbar({
				items : [{
							id : "add",
							text : Constants.BTN_ADD,
							iconCls : Constants.CLS_ADD,
							handler : add
						}, {
							id : "deleteIt",
							text : Constants.BTN_DELETE,
							iconCls : Constants.CLS_DELETE,
							handler : deleteIt
						}, {
							id : "modify",
							text : Constants.BTN_UPDATE,
							iconCls : Constants.CLS_UPDATE,
							handler : modify
						}, {
							id : "query",
							text : Constants.BTN_QUERY,
							iconCls : Constants.CLS_QUERY,
							handler : queryIt
						}]
			});
	// 新增按钮处理函数
	function add() {
		win.setTitle("新增司机档案");
		fp.getForm().reset();
		win.show();
        drpDriverName2.flag=true;
	}
	// 删除按钮处理函数
	function deleteIt() {
		var selectedRows = grid.getSelectionModel().getSelections();
		if (selectedRows.length < 1) {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
			return;
		}
		var lastSelected = selectedRows[selectedRows.length - 1];
		Ext.MessageBox.confirm(Constants.NOTICE_CONFIRM,
				MessageConstants.COM_C_002, function(button, text) {
					if (button == "yes") {
						Ext.Ajax.request({
							url : "administration/deleteDriverFile.action",
							method : 'post',
							params : {
								"driverFile.id" : lastSelected.get("id")
							},
							success : function(result, request) {
								var data = eval("(" + result.responseText + ")");
								if ((result.msg != null)
										&& (result.msg == Constants.SQL_FAILURE)) {
									Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
											MessageConstants.COM_E_014);
									return;
								}
								Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
										MessageConstants.COM_I_005, function() {
											store.load({
														params : {
															start : 0,
															limit : Constants.PAGE_SIZE
														}
													});
										});
							},
							failure : function(result, request) {
								var o = eval('(' + result.responseText + ')');
								Ext.Msg.alert(Constants.NOTICE, o.errMsg);
							}
						});
					}
				});
	}
	// 修改按钮处理函数
	function modify() {
		win.setTitle("修改司机档案");
		fp.getForm().reset();
		var selectedRows = grid.getSelectionModel().getSelections();
		if (selectedRows.length < 1) {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
			return;
		}
		var lastSelected = selectedRows[selectedRows.length - 1];
		win.show();
		Ext.get("driverFile.id").dom.value = lastSelected.get("id") == null
				? ""
				: lastSelected.get("id");
		Ext.get("formDepName").dom.value = lastSelected.get("depName") == null
				? ""
				: lastSelected.get("depName");
		Ext.get("drpDriverName2").dom.value = lastSelected.get("name") == null
				? ""
				: lastSelected.get("name");
		if(lastSelected.get("sex")=="M"){
			Ext.get("formSex").dom.value="男";
		} else if(lastSelected.get("sex")=="W"){
			Ext.get("formSex").dom.value="女";
		} else {
			Ext.get("formSex").dom.value="";
		}
		Ext.get("formAge").dom.value = lastSelected.get("ages") == null
				? ""
				: lastSelected.get("ages");
		Ext.get("driverFile.licence").dom.value = lastSelected.get("licence") == null
				? ""
				: lastSelected.get("licence");
		Ext.get("driverFile.licenceNo").dom.value = lastSelected.get("licenceNo") == null
		        ? ""
		        : lastSelected.get("licenceNo");
		Ext.get("driverFile.licenceDate").dom.value = lastSelected.get("licenceDate") == null
				? ""
				: lastSelected.get("licenceDate");
		Ext.get("driverFile.checkDate").dom.value = lastSelected.get("checkDate") == null
				? ""
				: lastSelected.get("checkDate");
		Ext.get("driverFile.mobileNo").dom.value = lastSelected.get("mobileNo") == null
				? ""
				: lastSelected.get("mobileNo");
		Ext.get("driverFile.telNo").dom.value = lastSelected.get("telNo") == null
				? ""
				: lastSelected.get("telNo");
		Ext.get("driverFile.homeAddr").dom.value = lastSelected.get("homeAddr") == null
				? ""
				: lastSelected.get("homeAddr");
		Ext.get("driverFile.comAddr").dom.value = lastSelected.get("comAddr") == null
				? ""
				: lastSelected.get("comAddr");
        drpDriverName2.flag=false;
	}
	// 查询按钮处理函数
    function queryIt(){
    	txtDepName2.setValue("");
    	hdnDepCode.setValue("");
    	drpName.store.removeAll();
    	drpName.setValue("");
    	drpLicence2.setValue("");
    	store2.removeAll();
    	btnExport.disable();
    	win2.show();
    }
    // grid选择模式设为单行选择模式
    var sm = new Ext.grid.RowSelectionModel({
	       singleSelect : true
	});
	// grid中的列定义
	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
						header : "行号",
						width : 35
					}), {
				header : "姓名",
				dataIndex : "name",
				sortable : true,
				width : 75
			}, {
				header : "所在部门",
				dataIndex : "depName",
				align : "left",
				width : 140
			}, {
				header : "驾照类型",
				dataIndex : "licence",
				align : "left",
				width : 70
			}, {
				header : "办照时间",
				dataIndex : "licenceDate",
				align : "left"
			}, {
				header : "年检时间",
				dataIndex : "checkDate",
				align : "left"
			}]);
	cm.defaultSortable = true;
	// grid中的数据
	var recordDriver = Ext.data.Record.create([{
				name : "id"
			}, {
				name : "name"
			}, {
				name : "sex"
			}, {
				name : "ages"
			}, {
				name : "depName"
			}, {
				name : "licence"
			}, {
				name : "licenceNo"
			}, {
				name : "licenceDate"
			}, {
				name : "checkDate"
			}, {
				name : "mobileNo"
			}, {
				name : "telNo"
			}, {
				name : "homeAddr"
			}, {
				name : "comAddr"
			}]);
	// grid中的store
	var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "administration/driverFileQuery.action"
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, recordDriver)
			});
	store.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			});
	// 底部工具栏
	var bbar = new Ext.PagingToolbar({
				pageSize : Constants.PAGE_SIZE,
				store : store,
				displayInfo : true,
				displayMsg : MessageConstants.DISPLAY_MSG,
				emptyMsg : MessageConstants.EMPTY_MSG
			})
	// grid主体
	var grid = new Ext.grid.GridPanel({
				region : "center",
				layout : "fit",
				colModel : cm,
				sm : sm,
				tbar : tbar,
				bbar : bbar,
				enableColumnMove : false,
				store : store
			});
	// 注册双击grid事件
	grid.on("rowdblclick", gridDb);

	function gridDb() {
		Ext.get("modify").dom.click();
	}
	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				enableTabScroll : true,
				items : [grid]
			});
})