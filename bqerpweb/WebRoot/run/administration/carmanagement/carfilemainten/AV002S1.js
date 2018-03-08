Ext.onReady(function() {

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
				width : 120,
				style : 'cursor:pointer',
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
							        isShowClear : false,
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
				width : 120,
				style : 'cursor:pointer',
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
							        isShowClear : false,
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd'
								});
					}
				}
			});
	// 部门名称
	var txtDepName2 = new Ext.form.TextField({
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
						"所属部门:", txtDepName2, hdnDepCode2, "-", {
							id : "query",
							text : Constants.BTN_QUERY,
							iconCls : Constants.CLS_QUERY,
							handler : queryIt2
						}, btnExport]
			});

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
		var args = {selectModel:'single',rootNode:{id:'0',text:'合肥电厂'},onlyLeaf:true};
		var object = window.showModalDialog(
						'../../../../comm/jsp/hr/dept/dept.jsp',
						args,
						'dialogWidth=500px;dialogHeight=420px;center=yes;help=no;resizable=no;status=no;');
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
				Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, String.format(
								MessageConstants.COM_E_009, "起始日期", "结束日期"));
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
					Ext.Msg.alert(Constants.NOTICE, MessageConstants.COM_E_014);
					return;
				}
				if ((result.msg != null)
						&& (result.msg == Constants.DATE_FAILURE)) {
					Ext.Msg.alert(Constants.NOTICE, MessageConstants.COM_E_023);
					return;
				}
				store2.loadData(gridData);
			},
			failure : function(result, request) {
				Ext.Msg.alert(Constants.NOTICE, MessageConstants.UNKNOWN_ERR);
			}
		});
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
				header : "载人数",
				width : 75,
				dataIndex : 'loadman'
			}, {
				header : "载重量",
				dataIndex : "weight",
				width : 75
			}, {
				header : "特殊设备",
				dataIndex : "equip",
				align : "left"
			}, {
				header : "使用情况",
				dataIndex : "useStatus",
				align : "left",
				width : 70,
				renderer : showUseStatus
			}, {
				header : "部门名称",
				dataIndex : "deptName",
				align : "left"
			}, {
				header : "驾驶员",
				dataIndex : "name",
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
				header : "购买金额",
				dataIndex : "price",
				align : "left"
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
				header : "行车里程",
				dataIndex : "runMile",
				align : "left"
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
				name : "useStatus"
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
	// 设定布局器及面板
	var layout2 = new Ext.Viewport({
				layout : "border",
				border : false,
				autoScroll : true,
				enableTabScroll : true,
				items : [grid2]
			});
	txtDepName2.focus();
})