Ext.onReady(function() {

	// 导出按钮
	var btnDerive = new Ext.Button({
				id : "derive",
				text : Constants.BTN_EXPORT,
				iconCls : Constants.CLS_EXPORT,
				disabled : true,
				handler : exportIt
			});
	var dteStart = new Date();
	var dteEnd = new Date();
	// 系统当天前15天的日期
	dteStart.setDate(dteStart.getDate() - 15);
	dteStart = dteStart.format('Y-m-d');
	// 系统当前日期
	dteEnd.setDate(dteEnd.getDate());
	dteEnd = dteEnd.format('Y-m-d');
	// 起始时间控件
	var txtStartDate = new Ext.form.TextField({
				id : 'startDate',
				name : 'startDate',
				width : 80,
				style : 'cursor:pointer',
				value : dteStart,
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
				width : 80,
				style : 'cursor:pointer',
				value : dteEnd,
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
	var txtDepText = new Ext.form.TextField({
		        width : 120,
				readOnly : true
			});
	txtDepText.onClick(selectDep);
	// 部门编码
	var hdnDepCode = new Ext.form.Hidden({
				value : ""
			});
	// 顶部工具栏
	var tbar = new Ext.Toolbar({
				items : ["来访时间:", "从", txtStartDate, "到", txtEndDate, "-",
						"被访人部门:", txtDepText, hdnDepCode, "-", {
							id : "query",
							text : Constants.BTN_QUERY,
							iconCls : Constants.CLS_QUERY,
							handler : queryIt
						}, btnDerive]
			});

	// 导出按钮处理函数
	function exportIt() {
		Ext.MessageBox.confirm(MessageConstants.SYS_CONFIRM_MSG,
				MessageConstants.COM_C_007, function(button, text) {
					if (button == "yes") {
						document.all.blankFrame.src = "administration/exportVisitorQueryFile.action";
					}
				})
	}
	// 选择部门按钮处理函数
	function selectDep() {
		var args = {selectModel:'single',rootNode:{id:'0',text:'合肥电厂'}};
		var dept = window
				.showModalDialog(
						'../../../../comm/jsp/hr/dept/dept.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(dept) != "undefined") {
			txtDepText.setValue(dept.names);
			hdnDepCode.setValue(dept.codes);
			// tfDepText.setValue(dept.codes);
		}
	}
	// 查询按钮处理函数
	function queryIt() {
		if ((txtStartDate.getValue() != "") && (txtEndDate.getValue() != "")) {
			if (txtStartDate.getValue() > txtEndDate.getValue()) {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
								MessageConstants.COM_E_009, "开始时间", "结束时间"));
				return;
			}
		}
		store.baseParams.strStartDate = txtStartDate.getValue();
		store.baseParams.strEndDate = txtEndDate.getValue();
		store.baseParams.strDepCode = hdnDepCode.value;
		Ext.Ajax.request({
					url : "administration/visitorquery.action",
					method : "post",
					params : {
						start : 0,
						limit : 18,
						strStartDate : txtStartDate.getValue(),
						strEndDate : txtEndDate.getValue(),
						strDepCode : hdnDepCode.value
					},
					success : function(result, request) {
						var gridData = eval('(' + result.responseText + ')');
						if((result.msg != null) && (result.msg == Constants.SQL_FAILURE)){
								Ext.Msg.alert(Constants.NOTICE,MessageConstants.COM_E_014);
								return;
						}
						if((result.msg != null) && (result.msg == Constants.DATE_FAILURE)){
								Ext.Msg.alert(Constants.NOTICE,MessageConstants.COM_E_023);
								return;
						}
						store.loadData(gridData);
					},
					failure : function(result, request) {}
				});
	}
	// grid选择模式设为单行选择模式
    var sm = new Ext.grid.RowSelectionModel({
	       singleSelect : true
	});
	// grid中的列
	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
						header : "行号",
						width : 35
					}), {
				header : "来访人",
				dataIndex : "insertBy",
				align : "left",
				width : 70
			}, {
				header : "来访时间",
				dataIndex : "insertDate",
				align : "left"
			}, {
				header : "证件类别",
				dataIndex : "paperTypeName",
				align : "left",
				width : 70
			}, {
				header : "证件号",
				dataIndex : "paperId",
				align : "left",
				width : 150
			}, {
				header : "来访人单位",
				dataIndex : "firm",
				align : "left"
			}, {
				header : "被访人",
				dataIndex : "name",
				align : "left",
				width : 70
			}, {
				header : "被访人部门",
				dataIndex : "depName",
				align : "left",
				width : 150
			}, {
				header : "进厂时间",
				dataIndex : "inDate",
				align : "left"
			}, {
				header : "出厂时间",
				dataIndex : "outDate",
				align : "left"
			}, {
				header : "备注",
				dataIndex : "note",
				align : "left",
				width : 200
			}, {
				header : "值班人",
				dataIndex : "onDuty",
				align : "left",
				width : 70
			}]);
	cm.defaultSortable = true;
	// grid中的数据
	var recordVisitor = Ext.data.Record.create([{
				name : "insertBy"
			}, {
				name : "insertDate"
			}, {
				name : "paperId"
			}, {
				name : "firm"
			}, {
				name : "name"
			}, {
				name : "depName"
			}, {
				name : "inDate"
			}, {
				name : "outDate"
			}, {
				name : "note"
			}, {
				name : "onDuty"
			}, {
				name : "paperTypeName"
			}]);
	// grid中的store
	var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "administration/visitorquery.action"
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, recordVisitor)
			});
	store.baseParams = ({
		strStartDate : txtStartDate.getValue(),
		strEndDate : txtEndDate.getValue(),
		strDepCode : hdnDepCode.value
	});
	// 注册store的load事件
	store.on("load", function() {
				if (this.getTotalCount() > 0) {
					btnDerive.setDisabled(false);
				} else {
					Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
							MessageConstants.COM_I_003);
					btnDerive.setDisabled(true);
				}
			});
	// 底部工具栏
	var bbar = new Ext.PagingToolbar({
				pageSize : 18,
				store : store,
				displayInfo : true,
				displayMsg : MessageConstants.DISPLAY_MSG,
				emptyMsg : MessageConstants.EMPTY_MSG
			})
	// 显示单元格内容
	var txtArea = new Ext.form.TextArea({
	     readOnly : true
	});
	// 显示窗口
	var win = new Ext.Window({
	     title : "详细信息查看窗口",
	     buttonAlign : "center",
	     modal : true,
	     height : 170,
	     width : 350,
	     layout : "fit",
	     closeAction : "hide",
	     autoScroll : true,
	     resizable : false,
	     items : [txtArea],
	     buttons : [{
					text : Constants.BTN_CLOSE,
					iconCls : Constants.CLS_CANCEL,
					handler : closeWin
				   }]
	});
	function closeWin(){
		win.hide();
	}
	// grid主体
	var grid = new Ext.grid.GridPanel({
				autoScroll : true,
				region : "center",
				layout : "fit",
				colModel : cm,
				sm : sm,
				tbar : tbar,
				bbar : bbar,
				enableColumnMove : false,
				store : store,
				autoSizeColumns : true,
				autoSizeHeaders : false
			});
	// 注册grid的单元格双击事件
	grid.on("celldblclick",cellDbClick);
    // 单元格双击处理函数
	function cellDbClick(grid, rowIndex, columnIndex, e){
		if(columnIndex==10){
			txtArea.setValue(store.getAt(rowIndex).get("note"));
			win.show();
		}
	}
	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				autoScroll : true,
				enableTabScroll : true,
				items : [grid]
			});
})