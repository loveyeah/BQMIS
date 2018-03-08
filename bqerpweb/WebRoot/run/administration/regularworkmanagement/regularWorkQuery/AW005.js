Ext.onReady(function() {

	// 导出按钮
	var btnDerive = new Ext.Button({
				id : "derive",
				text : Constants.BTN_EXPORT,
				iconCls : Constants.CLS_EXPORT,
				disabled : true,
				handler : deriveIt
			});
	function deriveIt() {
		Ext.MessageBox.confirm(MessageConstants.SYS_CONFIRM_MSG,
				MessageConstants.COM_C_007, function(button, text) {
					if(button=="yes"){
						document.all.blankFrame.src = "administration/exportRegularWorkFile.action";
					}
				})
	}
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
	// 工作类别名
	var drpWorktypeName = new Ext.form.CmbSubWorkType({
				id : "worktypeName",
				width : 80
			});
	drpWorktypeName.store.load();
	// 完成radio
	var rdComplete = new Ext.form.Radio({
				id : "ts1",
				boxLabel : "完成",
				inputValue : "Y",
				name : "flag",
				checked : true
			});
	// 未完成radio
	var rdNonComplete = new Ext.form.Radio({
				boxLabel : "未完成",
				id : "radioGroup",
				inputValue : "N",
				name : "flag"
			});
	// 顶部工具栏
	var tbar = new Ext.Toolbar({
				items : ["工作日期:", "从", txtStartDate, "到", txtEndDate, "-",
						"类别:", drpWorktypeName, "-", "完成标志:", rdComplete,
						rdNonComplete, "-", {
							id : "query",
							text : Constants.BTN_QUERY,
							iconCls : Constants.CLS_QUERY,
							handler : queryIt
						}, btnDerive]
			});

	// 查询按钮处理函数
	function queryIt() {
		if ((txtStartDate.getValue() != "") && (txtEndDate.getValue() != "")) {
			if (txtStartDate.getValue() > txtEndDate.getValue()) {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
								MessageConstants.COM_E_009, "开始日期", "结束日期"));
				return;
			}
		}
		store.baseParams = ({
			strStartDate : txtStartDate.getValue(),
			strEndDate : txtEndDate.getValue(),
			strSubWorkTypeCode : drpWorktypeName.getValue(),
			strCompleteFlag : Ext.getCmp("radioGroup").getGroupValue()
		});

		Ext.Ajax.request({
					url : "administration/regularWorkQuery.action",
					method : "post",
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE,
						strStartDate : txtStartDate.getValue(),
						strEndDate : txtEndDate.getValue(),
						strSubWorkTypeCode : drpWorktypeName.getValue(),
						strCompleteFlag : Ext.getCmp("radioGroup")
								.getGroupValue()
					},
					success : function(result, request) {
						var gridData = eval('(' + result.responseText + ')');
						store.loadData(gridData);
					},
					failure : function(result, request) {
						Ext.Msg.alert(Constants.NOTICE, MessageConstants.UNKNOWN_ERR);
					}
				});
	}

	function showMark(mark) {
		if (mark == "Y") {
			return "完成"
		}
		if (mark == "N") {
			return "未完成"
		}
		return "";
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
				header : "工作名称",
				dataIndex : "workItemName",
				align : "left"
			}, {
				header : "工作日期",
				dataIndex : "workDate",
				align : "left",
				width : 80
			}, {
				header : "星期",
				dataIndex : "week",
				align : "left",
				width : 50
			}, {
				header : "具体工作内容",
				dataIndex : "workExplain",
				align : "left"
			}, {
				header : "工作结果",
				dataIndex : "result",
				align : "left"
			}, {
				header : "完成标志",
				dataIndex : "mark",
				align : "left",
				width : 60,
				renderer : showMark
			}, {
				header : "操作人",
				dataIndex : "operator",
				align : "left",
				width : 60
			}, {
				header : "备注",
				dataIndex : "memo",
				align : "left",
				width : 200
			}]);
	cm.defaultSortable = true;
	// grid中的数据
	var recordRegularWork = Ext.data.Record.create([{
				name : "workItemName"
			}, {
				name : "workDate"
			}, {
				name : "week"
			}, {
				name : "workExplain"
			}, {
				name : "result"
			}, {
				name : "mark"
			}, {
				name : "operator"
			}, {
				name : "memo"
			}]);
	// grid中的store
	var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "administration/regularWorkQuery.action"
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, recordRegularWork)
			});
	// // 注册store的load事件
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
				pageSize : Constants.PAGE_SIZE,
				store : store,
				displayInfo : true,
				displayMsg : MessageConstants.DISPLAY_MSG,
				emptyMsg : MessageConstants.EMPTY_MSG
			})
	// grid主体
	var grid = new Ext.grid.EditorGridPanel({
				region : "center",
				layout : "fit",
				colModel : cm,
				sm : sm,
				tbar : tbar,
				bbar : bbar,
				enableColumnMove : false,
				store : store,
				clicksToEdit : 1
			});
	// 注册grid的单元格双击事件
	grid.on("celldblclick",cellDbClick);
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
	// 单元格双击处理函数
	function cellDbClick(grid, rowIndex, columnIndex, e){
		if(columnIndex==4){
			txtArea.setValue(store.getAt(rowIndex).get("workExplain"));
			win.show();
		}
		if(columnIndex==5){
			txtArea.setValue(store.getAt(rowIndex).get("result"));
			win.show();
		}
		if(columnIndex==8){
			txtArea.setValue(store.getAt(rowIndex).get("memo"));
			win.show();
		}
	}
	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				enableTabScroll : true,
				items : [grid]
			});
})