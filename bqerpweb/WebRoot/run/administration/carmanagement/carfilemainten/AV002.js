Ext.QuickTips.init();
Ext.onReady(function() {
	Ext.override(Ext.grid.GridView, {
				ensureVisible : function(row, col, hscroll) {
					if (typeof row != "number") {
						row = row.rowIndex;
					}
					if (!this.ds) {
						return;
					}
					if (row < 0 || row >= this.ds.getCount()) {
						return;
					}
					col = (col !== undefined ? col : 0);
					var rowEl = this.getRow(row), cellEl;
					if (!(hscroll === false && col === 0)) {
						while (this.cm.isHidden(col)) {
							col++;
						}
						cellEl = this.getCell(row, col);
					}
					if (!rowEl) {
						return;
					}
					var c = this.scroller.dom;
					var ctop = 0;
					var p = rowEl, stop = this.el.dom;
					while (p && p != stop) {
						ctop += p.offsetTop;
						p = p.offsetParent;
					}
					ctop -= this.mainHd.dom.offsetHeight;
					var cbot = ctop + rowEl.offsetHeight;
					var ch = c.clientHeight;
					var stop = parseInt(c.scrollTop, 10);
					var sbot = stop + ch;
					if (ctop < stop) {
						c.scrollTop = ctop;
					} else if (cbot > sbot) {
						c.scrollTop = cbot - ch;
					}
					if (hscroll !== false) {
						var cleft = parseInt(cellEl.offsetLeft, 10);
						var cright = cleft + cellEl.offsetWidth;
						var sleft = parseInt(c.scrollLeft, 10);
						var sright = sleft + c.clientWidth;
						if (cleft < sleft) {
							c.scrollLeft = cleft;
						} else if (cright > sright) {
							c.scrollLeft = cright - c.clientWidth;
						}
					}
					return cellEl ? Ext.fly(cellEl).getXY() : [this.el.getX(),
							Ext.fly(rowEl).getY()];
				}
			});
	
	
	// 导出按钮
	var btnExport = new Ext.Button({
				id : "export",
				text : Constants.BTN_EXPORT,
				iconCls : Constants.CLS_EXPORT,
				disabled : true,
				handler : exportIt
			});
	// 起始时间控件
	var txtStartDate = new Ext.form.TextField({
				id : 'startDate',
				name : 'startDate',
				width : 80,
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
	// 截止时间控件
	var txtEndDate = new Ext.form.TextField({
				id : 'endDate',
				name : 'endDate',
				width : 80,
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
	// 部门名称
	var txtDepName2 = new Ext.form.TextField({
		        width : 120,
				readOnly : true
			});
	txtDepName2.onClick(selectDep2);
	// 部门编码
	var hdnDepCode2 = new Ext.form.Hidden({
				value : ""
			});
	// 顶部工具栏
	var tbar2 = new Ext.Toolbar({
				items : ["购买日期:", "从", txtStartDate, "到", txtEndDate, "-",
						"所属部门:", txtDepName2, hdnDepCode2,"-",{
							id : "query",
							text : Constants.BTN_QUERY,
							iconCls : Constants.CLS_QUERY,
							handler : queryIt2
						}, btnExport]
			});
    function numberFormat(value){
		if(value == null || value == "") 
		return "0.00";
	    value = String(value);
        // 整数部分
        var whole = value;
        // 小数部分
        var sub = ".00";
        // 如果有小数
		if (value.indexOf(".") > 0) {
		    whole = value.substring(0, value.indexOf("."));
			sub = value.substring(value.indexOf("."), value.length);
			sub = sub + "00";
			if(sub.length > 3){
			    sub = sub.substring(0,3);
		    }
		}
        var r = /(\d+)(\d{3})/;
        while (r.test(whole)){
           whole = whole.replace(r, '$1' + ',' + '$2');
        }
        v = whole + sub;
        return  v;
	}
	// 导出按钮处理函数
	function exportIt() {
		Ext.MessageBox.confirm(MessageConstants.SYS_CONFIRM_MSG,
				MessageConstants.COM_C_007, function(button, text) {
					if(button=="yes"){
						document.all.blankFrame.src = "administration/exportCarFile.action";
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
			if (typeof(object.names) != "undefined") {
				txtDepName2.setValue(object.names);
			}
			if (typeof(object.codes) != "undefined") {
				hdnDepCode2.setValue(object.codes);
			}
		}
	}
	// 查询按钮处理函数
	function queryIt2() {
		if ((txtStartDate.getValue() != "") && (txtEndDate.getValue() != "")) {
			if (txtStartDate.getValue() > txtEndDate.getValue()) {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
								MessageConstants.COM_E_009, "开始日期", "结束日期"));
				return;
			}
		}
		store2.baseParams.strStartDate = txtStartDate.getValue();
		store2.baseParams.strEndDate = txtEndDate.getValue();
		store2.baseParams.strDepCode = hdnDepCode2.getValue();
		Ext.Ajax.request({
			url : "administration/carFileQuery.action",
			method : "post",
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE,
				strStartDate : txtStartDate.getValue(),
				strEndDate : txtEndDate.getValue(),
				strDepCode : hdnDepCode2.getValue()
			},
			success : function(result, request) {
				var gridData = eval('(' + result.responseText + ')');
				if ((result.msg != null)
						&& (result.msg == Constants.SQL_FAILURE)) {
					Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, MessageConstants.COM_E_014);
					return;
				}
				if ((result.msg != null)
						&& (result.msg == Constants.DATE_FAILURE)) {
					Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, MessageConstants.COM_E_023);
					return;
				}
				store2.loadData(gridData);
			},
			failure : function(result, request) {}
		});
	}
	// grid选择模式设为单行选择模式
    var sm2 = new Ext.grid.RowSelectionModel({
	       singleSelect : true
	});
	// grid中的列定义
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
				header : "车名",
				width : 75,
				dataIndex : 'carName'
			}, {
				header : "车种",
				width : 75,
				dataIndex : 'carKind'
			}, {
				header : "车型",
				width : 75,
				dataIndex : 'carType'
			}, {
				header : "车牌号",
				width : 75,
				dataIndex : 'carNo'
			}, {
				header : "车架",
				width : 75,
				dataIndex : 'carFrame'
			}, {
				header : "发动机号码",
				width : 75,
				dataIndex : 'engineNo'
			}, {
				header : "载人数(人)",
				width : 75,
				dataIndex : 'loadman',
				align : "right"
			}, {
				header : "载重量(吨)",
				dataIndex : "weight",
				width : 75,
				renderer : numberFormat,
				align : "right"
			}, {
				header : "特殊设备",
				dataIndex : "equip",
				align : "left"
			}, {
				header : "驾驶员",
				dataIndex : "name",
				align : "left"
			}, {
				header : "所在部门",
				dataIndex : "deptName",
				align : "left"
			}, {
				header : "行驶证",
				dataIndex : "runLic",
				align : "left"
			}, {
				header : "通行证",
				dataIndex : "runAllLic",
				align : "left"
			}, {
				header : "购买日期",
				dataIndex : "buyDate",
				align : "left"
			}, {
				header : "购买金额(万元)",
				dataIndex : "price",
				renderer : numberFormat,
				align : "right"
			}, {
				header : "销售商家",
				dataIndex : "carshop",
				align : "left"
			}, {
				header : "发票号",
				dataIndex : "invoiceNo",
				align : "left"
			}, {
				header : "耗油率",
				dataIndex : "oilRate",
				align : "left"
			}, {
				header : "保险费",
				dataIndex : "isurance",
				align : "left"
			}, {
				header : "行车里程(公里)",
				dataIndex : "runMile",
				align : "left",
				align : "right"
			}]);
	cm2.defaultSortable = true;
	// grid中的数据
	var recordCar2 = Ext.data.Record.create([{
				name : "id"
			}, {
				name : "carNo"
			}, {
				name : "carName"
			}, {
				name : "carKind"
			}, {
				name : "carType"
			}, {
				name : "carFrame"
			}, {
				name : "engineNo"
			}, {
				name : "loadman"
			}, {
				name : "weight"
			}, {
				name : "equip"
			}, {
				name : "deptName"
			}, {
				name : "name"
			}, {
				name : "runLic"
			}, {
				name : "runAllLic"
			}, {
				name : "buyDate"
			}, {
				name : "price"
			}, {
				name : "carshop"
			}, {
				name : "invoiceNo"
			}, {
				name : "oilRate"
			}, {
				name : "isurance"
			}, {
				name : "runMile"
			}]);
	// grid中的store
	var store2 = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "administration/carFileQuery.action"
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, recordCar2)
			});
	store2.baseParams = ({
		strStartDate : txtStartDate.getValue(),
		strEndDate : txtEndDate.getValue(),
		strDepCode : hdnDepCode2.getValue()
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
				pageSize : Constants.PAGE_SIZE,
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
				id : "carFile.id",
				name : "carFile.id",
				value : ""
			});
	// 车牌号
	var txtCarNo = new Ext.form.TextField({
				id : "carFile.carNo",
				name : "carFile.carNo",
				fieldLabel : "车牌号<font color='red'>*</font>",
				allowBlank : false,
				readOnly : true,
				width : 120,
				maxLength : 10
			});
	// 车种
	var txtCarKind = new Ext.form.TextField({
				id : "carFile.carKind",
				name : "carFile.carKind",
				fieldLabel : "车种",
				width : 120,
				maxLength : 30
			});
	// 车架
	var txtCarFrame = new Ext.form.TextField({
				id : "carFile.carFrame",
				name : "carFile.carFrame",
				fieldLabel : "车架",
				width : 120,
				maxLength : 20
			});
	// 载人数
	var txtLoadman = new Ext.form.MoneyField({
				id : "formloadman",
				fieldLabel : "载人数",
				style : 'text-align:right',
				allowNegative : false,
				allowDecimals : false,
				appendChar : "人",
				width : 120,
				maxValue : "99999",
				maxLength : 5
			});
	var hdnLoadman = new Ext.form.Hidden({
	           name : "carFile.loadman"
	});
	// 特殊设备
	var txtEquip = new Ext.form.TextField({
				id : "carFile.equip",
				name : "carFile.equip",
				fieldLabel : "特殊设备",
				width : 120,
				maxLength : 100
			});
	// 所在部门名称
	var txtDepName = new Ext.form.TextField({
				id : "formDepName",
				name : "formDepName",
				readOnly : true,
				fieldLabel : "所在部门",
				width : 120
			});
	txtDepName.onClick(selectDep);
	// 隐藏部门编码
	var hdnDepCode = new Ext.form.Hidden({
		        id : "carFile.dep",
		        name : "carFile.dep",
				value : ""
			});
	// 行驶证
	var txtRunLic = new Ext.form.TextField({
				id : "carFile.runLic",
				name : "carFile.runLic",
				fieldLabel : "行驶证",
				width : 120,
				maxLength : 20
			});
	// 购买日期
	var txtBuyDate = new Ext.form.TextField({
				id : 'carFile.buyDate',
				name : "carFile.buyDate",
				fieldLabel : "购买日期",
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
	// 销售商家
	var txtCarshop = new Ext.form.TextField({
				id : "carFile.carshop",
				name : "carFile.carshop",
				fieldLabel : "销售商家",
				width : 120,
				maxLength : 100
			});
	// 耗油率
	var txtOilRate = new Ext.form.MoneyField({
				id : "formOilRate",
				name : "formOilRate",
				allowNegative : false,
				fieldLabel : "耗油率",
				appendChar : "升/百公里",
				style : 'text-align:right',
				padding : 2,
				maxValue : "99.99",
				width : 120,
				maxLength : 10
			});
	var hdnOilRate = new Ext.form.Hidden({
		name : "carFile.oilRate"
	});
	// 行车里程
	var txtRunMile = new Ext.form.MoneyField({
				id : "formrunMile",
				fieldLabel : "行车里程<font color='red'>*</font>",
				allowNegative : false,
				width : 120,
				allowBlank : false,
				style : 'text-align:right',
				appendChar : "公里",
				padding : 2,
				maxValue : "9999999999999.99",
				maxLength : 15
			});
	var hdnRunMile = new Ext.form.Hidden({
		name : "carFile.runMile"
	});
	// 车名
	var txtCarName = new Ext.form.TextField({
				id : "carFile.carName",
				name : "carFile.carName",
				fieldLabel : "车名<font color='red'>*</font>",
				allowBlank : false,
				width : 120,
				maxLength : 40
			});
	// 车型
	var txtCarType = new Ext.form.TextField({
				id : "carFile.carType",
				name : "carFile.carType",
				fieldLabel : "车型",
				width : 120,
				maxLength : 30
			});
	// 发动机号码
	var txtEngineNo = new Ext.form.TextField({
				id : "carFile.engineNo",
				name : "carFile.engineNo",
				fieldLabel : "发动机号码",
				width : 120,
				maxLength : 20
			});
	// 载重量
	var txtWeight = new Ext.form.MoneyField({
				id : "formweight",
				fieldLabel : "载重量",
				allowNegative : false,
				style : 'text-align:right',
				maxValue : "999999",
				appendChar : "吨",
				padding : 2,
				width : 120
			});
	var hdnWeight = new Ext.form.Hidden({
	            name : "carFile.weight"
	});
	// 司机
    var drpDriver = new Ext.form.CmbDriver({
    	id : "formDriverName",
    	name : "formDriverName",
    	fieldLabel : "司机",
    	readOnly : true,
    	width : 120
    });
    drpDriver.store.load();
	// 通行证
	var txtRunAllLic = new Ext.form.TextField({
				id : "carFile.runAllLic",
				name : "carFile.runAllLic",
				fieldLabel : "通行证",
				width : 120,
				maxLength : 20
			});
	// 购买金额
	var txtPrice = new Ext.form.MoneyField({
				id : "formprice",
				fieldLabel : "购买金额<font color='red'>*</font>",
				width : 120,
				style : 'text-align:right',
				maxValue : "9999999",
				appendChar : "万元",
				padding : 2,
				allowBlank : false
			});
	var hdnPrice = new Ext.form.Hidden({
		name : "carFile.price"
	});
	// 发票号
	var txtInvoiceNo = new Ext.form.TextField({
				id : "carFile.invoiceNo",
				name : "carFile.invoiceNo",
				fieldLabel : "发票号",
				width : 120,
				maxLength : 30
			});
	// 保险费
	var txtIsurance = new Ext.form.TextField({
				id : "carFile.isurance",
				name : "carFile.isurance",
				fieldLabel : "保险费",
				width : 120,
				maxLength : 30
			});
	var fs = new Ext.Panel({
		height : "100%",
		width  : 450,
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
				items : [hdnId, hdnDepCode, txtCarNo, txtCarName, txtCarKind, txtCarFrame,
						txtLoadman, hdnLoadman, txtEquip, txtRunLic,
						txtBuyDate, txtCarshop, txtOilRate, hdnOilRate, hdnRunMile]
			}, {
				columnWidth : 0.5,
				border : false,
				layout : "form",
				items : [txtDepName, txtCarType, txtEngineNo, txtWeight, hdnWeight,
						drpDriver, txtRunAllLic, txtPrice, hdnPrice,
						txtInvoiceNo, txtIsurance, txtRunMile]
			}]
		}]
	});
	// 增加或修改面板
	var fp = new Ext.form.FormPanel({
				id : "form",
				region : "center",
				labelAlign : "right",
				labelWidth : 75,
				frame : true,
				autoHeight : true,
				items : [fs]
			});
	// 选择部门处理函数
	function selectDep(){
		var args = {selectModel:'single',rootNode:{id:'0',text:'合肥电厂'},onlyLeaf:true};
		var object = window.showModalDialog(
						'../../../../comm/jsp/hr/dept/dept.jsp',
						args,
						'dialogWidth=500px;dialogHeight=320px;center=yes;help=no;resizable=no;status=no;');
		// 根据返回值设置画面的值
		if (object) {
			if (typeof(object.names) != "undefined") {
				txtDepName.setValue(object.names);
			}
			if (typeof(object.codes) != "undefined") {
				hdnDepCode.setValue(object.codes);
			}
		}
	}
	// 保存按钮处理函数
	function save() {
		// 车牌号不能为空
        if(txtCarNo.getValue()==""){
        	txtCarNo.focus();
        	Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
							MessageConstants.COM_E_002, "车牌号"));
            return;						
        }
        // 车名不能为空
        if(txtCarName.getValue()==""){
        	txtCarName.focus();
        	Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
							MessageConstants.COM_E_002, "车名"));
            return;						
        }
        // 购买金额不能为空
        if(txtPrice.getValue()==""){
        	txtPrice.focus();
        	Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
							MessageConstants.COM_E_002, "购买金额"));
            return;						
        }
        // 行车里程不能为空
        if(txtRunMile.getValue()===""){
        	txtRunMile.focus();
        	Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
							MessageConstants.COM_E_002, "行车里程"));
            return;						
        }
		hdnLoadman.setValue(txtLoadman.getValue());
		hdnWeight.setValue(txtWeight.getValue());
		hdnPrice.setValue(txtPrice.getValue());
		hdnRunMile.setValue(txtRunMile.getValue());
		hdnOilRate.setValue(txtOilRate.getValue())
		Ext.MessageBox.confirm(MessageConstants.SYS_CONFIRM_MSG,
				MessageConstants.COM_C_001, function(button, text) {
					if (button == "yes") {
						fp.getForm().submit({
							url : "administration/"
									+ (Ext.get("carFile.id").dom.value == ""
											? "addCarFile"
											: "modifyCarFile") + ".action",
							params : {
								"carFile.driver" : drpDriver.getValue()
							},
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
										&& (result.msg == Constants.ADD_CARNO_FAILURE)) {
									Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
											String.format(MessageConstants.COM_E_007, "车辆档案"));
									return;
								}
								// 显示成功信息
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
							failure : function(form, action) {}
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
		        title : "车辆档案查询",
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
				buttonAlign : 'center',
				width : 500,
				height : 320,
				closeAction : "hide",
				autoScroll : true,
				resizable : false,
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
		win.setTitle("新增车辆信息");
		fp.getForm().reset();
		win.show();
		Ext.get("carFile.carNo").dom.readOnly = false;
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
		Ext.MessageBox.confirm(MessageConstants.SYS_CONFIRM_MSG,
				MessageConstants.COM_C_002, function(button, text) {
					if (button == "yes") {
						Ext.Ajax.request({
							url : "administration/deleteCarFile.action",
							method : 'post',
							params : {
								"carFile.id" : lastSelected.get("id")
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
								Ext.Msg.alert(Constants.ERROR,
										Constants.UNKNOWN_ERR);
							}
						});
					}
				});
	}
	// 修改按钮处理函数
	function modify() {
		win.setTitle("修改车辆信息");
		fp.getForm().reset();
		var selectedRows = grid.getSelectionModel().getSelections();
		if (selectedRows.length < 1) {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
			return;
		}
		var lastSelected = selectedRows[selectedRows.length - 1];
		win.show();
		Ext.get("carFile.carNo").dom.readOnly = true;
		Ext.get("carFile.id").dom.value = lastSelected.get("id") == null
				? ""
				: lastSelected.get("id");
		Ext.get("carFile.carNo").dom.value = lastSelected.get("carNo") == null
				? ""
				: lastSelected.get("carNo");
		Ext.get("carFile.carKind").dom.value = lastSelected.get("carKind") == null
				? ""
				: lastSelected.get("carKind");
		Ext.get("carFile.carFrame").dom.value = lastSelected.get("carFrame") == null
				? ""
				: lastSelected.get("carFrame");
		Ext.get("carFile.loadman").dom.value = lastSelected.get("loadman") == null
				? ""
				: lastSelected.get("loadman");
		txtLoadman.setValue(lastSelected.get("loadman"));
		Ext.get("carFile.equip").dom.value = lastSelected.get("equip") == null
				? ""
				: lastSelected.get("equip");
		Ext.get("formDepName").dom.value = lastSelected.get("deptName") == null
		        ? ""
		        : lastSelected.get("deptName");
		Ext.get("carFile.runLic").dom.value = lastSelected.get("runLic") == null
				? ""
				: lastSelected.get("runLic");
		Ext.get("carFile.buyDate").dom.value = lastSelected.get("buyDate") == null
				? ""
				: lastSelected.get("buyDate");
		Ext.get("carFile.carshop").dom.value = lastSelected.get("carshop") == null
				? ""
				: lastSelected.get("carshop");
		Ext.get("carFile.oilRate").dom.value = lastSelected.get("oilRate") == null
				? ""
				: lastSelected.get("oilRate");
		txtOilRate.setValue(lastSelected.get("oilRate"));
		Ext.get("carFile.runMile").dom.value = lastSelected.get("runMile") == null
				? ""
				: lastSelected.get("runMile");
		txtRunMile.setValue(lastSelected.get("runMile"));
		Ext.get("carFile.carName").dom.value = lastSelected.get("carName") == null
				? ""
				: lastSelected.get("carName");
		Ext.get("carFile.carType").dom.value = lastSelected.get("carType") == null
				? ""
				: lastSelected.get("carType");
	    Ext.get("carFile.engineNo").dom.value = lastSelected.get("engineNo") == null
				? ""
				: lastSelected.get("engineNo");
		Ext.get("carFile.weight").dom.value = lastSelected.get("weight") == null
				? ""
				: lastSelected.get("weight");
		txtWeight.setValue(lastSelected.get("weight"));
		drpDriver.setValue(lastSelected.get("empCode"));
		Ext.get("carFile.runAllLic").dom.value = lastSelected.get("runAllLic") == null
				? ""
				: lastSelected.get("runAllLic");
		Ext.get("carFile.price").dom.value = lastSelected.get("price") == null
				? ""
				: lastSelected.get("price");
		txtPrice.setValue(lastSelected.get("price"));
		Ext.get("carFile.invoiceNo").dom.value = lastSelected.get("invoiceNo") == null
				? ""
				: lastSelected.get("invoiceNo");
		Ext.get("carFile.isurance").dom.value = lastSelected.get("isurance") == null
				? ""
				: lastSelected.get("isurance");
		hdnDepCode.setValue(lastSelected.get("dep"));
	}
	// 查询按钮处理函数
    function queryIt(){
    	store2.removeAll();
    	txtStartDate.setValue("");
    	txtEndDate.setValue("");
    	txtDepName2.setValue("");
    	hdnDepCode2.setValue("");
    	btnExport.disable();
    	win2.show();
    }
	function showUseStatus(value){
		if(value=="Y"){
			return "使用";
		}
		if(value=="N"){
			return "空闲";
		}
		return "";
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
				header : "部门名称",
				dataIndex : "deptName",
				align : "left"
			}, {
				header : "车牌号",
				width : 75,
				dataIndex : 'carNo'
			}, {
				header : "车名",
				width : 75,
				dataIndex : 'carName'
			}, {
				header : "驾驶员",
				dataIndex : "name",
				align : "left"
			}]);
	cm.defaultSortable = true;
	// grid中的数据
	var recordCar = Ext.data.Record.create([{
				name : "id"
			}, {
				name : "carNo"
			}, {
				name : "carName"
			}, {
				name : "carKind"
			}, {
				name : "carType"
			}, {
				name : "carFrame"
			}, {
				name : "engineNo"
			}, {
				name : "loadman"
			}, {
				name : "weight"
			}, {
				name : "equip"
			}, {
				name : "deptName"
			}, {
				name : "name"
			}, {
				name : "empCode"
			}, {
				name : "runLic"
			}, {
				name : "runAllLic"
			}, {
				name : "buyDate"
			}, {
				name : "price"
			}, {
				name : "carshop"
			}, {
				name : "invoiceNo"
			}, {
				name : "oilRate"
			}, {
				name : "isurance"
			}, {
				name : "runMile"
			}, {
				name : "dep"
			}]);
	// grid中的store
	var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "administration/carFileQuery.action"
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, recordCar)
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