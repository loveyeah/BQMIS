Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function(){
	var d = new Array();
	d[0] = new Array();
	d[1] = new Array();

	var ids = "";
	var isBreakOuts = "";
	
	// 设置改变lable元素的方法
	Ext.form.Label.prototype.setText = function(argText) {
		this.el.dom.innerHTML = argText;
	}

	var workN = window.dialogArguments.workN;
	var workType = window.dialogArguments.workType;

	// ============== 定义安措拆除详细grid ===============
	var DetailRecord = Ext.data.Record.create([{
				name : 'safetyName'
			}, {
				name : 'safetyContent'
			}, {
				name : 'exeDesc'
			}, {
				name : 'exeMan'
			}, {
				name : 'exeDate'
			}, {
				name : 'breakOutMan'
			}, {
				name : 'beakOutDate'
			}, {
				name : 'id'
			}, {
				name : 'isBreakOut'
			}]);

	// 定义获取数据源
	var detailProxy = new Ext.data.HttpProxy({
				url : 'workticket/getSecurityDetailList.action'
			});

	// 定义格式化数据
	var detailReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, DetailRecord);

	// 定义封装缓存数据的对象
	var detailStore = new Ext.data.Store({
				// 访问的对象
				proxy : detailProxy,
				// 处理数据的对象
				reader : detailReader
			});

	// ============== 定义安措未拆除原因grid ===============
	var NotRemoveRecord = Ext.data.Record.create([{
				name : 'reason'
			}, {
				name : 'approveBy'
			}, {
				name : 'approveDate'
			}]);

	// 定义获取数据源
	var NotRemoveProxy = new Ext.data.HttpProxy({
				url : 'workticket/getNotSecurityList.action'
			});

	// 定义格式化数据
	var NotRemoveReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, NotRemoveRecord);

	// 定义封装缓存数据的对象
	var NotRemoveStore = new Ext.data.Store({
				// 访问的对象
				proxy : NotRemoveProxy,
				// 处理数据的对象
				reader : NotRemoveReader
			});

	// -----------------控件定义----------------------------------

	// ↓↓****************员工验证窗口****************
	var wd = 120;
	// 工号
	var workID = new Ext.form.TextField({
				id : "workID",
				fieldLabel : '工号<font color ="red">*</font>',
				allowBlank : false,
				// value : session.getAttribute("user.workerCode"),
				width : wd
			});
	// 密码
	var workPwd = new Ext.form.TextField({
				id : "workPwd",
				fieldLabel : '密码<font color ="red">*</font>',
				allowBlank : false,
				inputType : "password",
				width : wd
			});
	// 弹出窗口panel
	var workerPanel = new Ext.FormPanel({
				frame : true,
				labelAlign : 'right',
				height : 120,
				items : [workID, workPwd]
			});

	// 弹出窗口
	var validateWin = new Ext.Window({
				width : 300,
				height : 140,
				title : "请输入工号和密码",
				buttonAlign : "center",
				resizable : false,
				modal : true,
				items : [workerPanel],
				buttons : [{
					text : '确定',
					id:'btnSign',
					handler : function() {
						Ext.lib.Ajax.request('POST',
								'comm/workticketApproveCheckUser.action',
								{
									success : function(action) {
										var result = eval(action.responseText);
										if (result) {
											saveCls();
										} else {
											Ext.Msg.alert(
													Constants.SYS_REMIND_MSG,
													Constants.USER_CHECK_ERROR);
										}
									}
								}, "workerCode=" + workID.getValue()
										+ "&loginPwd=" + workPwd.getValue());
					}
				}, {
					text : Constants.BTN_CANCEL,
					iconCls : Constants.CLS_CANCEL,
					handler : function() {
						validateWin.hide();
					}
				}],
				listeners : {
					show : function(com) {
						// 把上次输入的信息清空
						workID.setValue("");
						workPwd.setValue("");
						// 设定默认工号
						Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
                    success : function(action) {
                    	 var result = eval("(" + action.responseText + ")");
                        if (result.workerCode) {
                            // 设定默认工号
                        		workID.setValue(result.workerCode);
                        	
                          
                        }
                    }
                });
//						Ext.lib.Ajax.request('POST',
//								'workticket/getDefaultWorkerCode.action', {
//									success : function(action) {
//										if (action.responseText) {
//											workID
//													.setValue(action.responseText);
//										}
//									}
//								});
					}
				},
				closeAction : 'hide'
			});
	// ↑↑****************员工验证窗口****************
	function saveCls() {
		Ext.Ajax.request({
					url : 'workticket/saveSafeSecurityReason.action',

					success : function(action) {
						var o = eval("(" + action.responseText + ")");
						Ext.Msg.alert(Constants.NOTICE, o.msg
										+ "&nbsp&nbsp&nbsp&nbsp&nbsp");
						validateWin.hide();
						reasonText.setValue("");
						detailStore.load();
						NotRemoveStore.load();
					},
					failure : function() {
						Ext.Msg.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
					},
					params : {
						workerCode : workID.getValue(),
						workticketNo : workN,
						reasonText : reasonText.getValue(),
						ids : ids,
						isBreakOuts : isBreakOuts
					}
				})

	}
	
	// 定义选择列
	var detailSm = new Ext.grid.CheckboxSelectionModel({
		header : '选择',
		id : 'sm',
		width : 35
	});

	// -----------安措拆除详细GridPanel-------------------
	var detailGrid = new Ext.grid.GridPanel({
				region : "north",
				height : 200,
				store : detailStore,
				columns : [detailSm, {
							header : "id",
							sortable : true,
							hidden : true,
							dataIndex : 'id'
						}, {
							header : "安措项目",
							sortable : true,
							width : 60,
							dataIndex : 'safetyName'
						}, {
							header : "安措内容",
							sortable : true,
							width : 240,
							dataIndex : 'safetyContent'
						}, {
							header : "执行情况",
							sortable : true,
							width : 60,
							dataIndex : 'exeDesc'
						}, {
							header : "安措执行人",
							sortable : true,
							width : 70,
							dataIndex : 'exeMan'
						}, {
							header : "安措执行时间",
							sortable : true,
							width : 110,
							dataIndex : 'exeDate'
						}, {
							header : "安措拆除人",
							width : 70,
							sortable : true,
							dataIndex : 'breakOutMan'
						}, {
							header : "安措拆除时间",
							sortable : true,
							width : 110,
							dataIndex : 'beakOutDate'
						}],

				sm : detailSm,
				autoScrol : true,
				enableColumnMove : false,
				autoSizeColumns : true
			});

	// -----------安措未拆除原因GridPanel-------------------
	var NotRemoveGrid = new Ext.grid.GridPanel({
				region : "south",
				height : 150,
				store : NotRemoveStore,
				columns : [new Ext.grid.RowNumberer({
									header : '序号',
									width : 40
								}), {
							header : "安措未拆除原因",
							sortable : true,
							dataIndex : 'reason',
							width : 500
						}, {
							header : "记录人",
							sortable : true,
							dataIndex : 'approveBy',
							width : 90
						}, {
							header : "记录时间",
							sortable : true,
							dataIndex : 'approveDate',
							renderer : function(value) {
								if (!value) return "";
						        var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
						        var reTime = /\d{2}:\d{2}:\d{2}/gi;
						        var strDate = value.match(reDate);
						        var strTime = value.match(reTime);
						        if (!strDate) return "";
						        strTime = strTime ? strTime : '00:00:00';
						        return strDate + " " + strTime;
							},
							width : 120
						}],
				enableColumnMove : false,
				autoSizeColumns : true
			});

	// --gridpanel显示格式定义-----结束--------------------

	var reasonText = new Ext.form.TextArea({
				id : 'reasonText',
				height : 50,
				width : 300,
				fieldLabel : "如果安措未完全拆除，请填写原因 "
			});
	var printBut = new Ext.Button({
				text : "打印安措拆除卡",
				handler :viewRecord
			});
	var allBut = new Ext.Button({
				text : "全选",
				handler : function() {
					detailSm.selectAll();
				}
			});
	var noneBut = new Ext.Button({
				text : "全不选",
				handler : function() {
					detailSm.clearSelections();
				}
			});

	var saveBut = new Ext.Button({
				text : "保存",
				handler : function() {
					var st = detailGrid.getStore();
					
					var intTemp = 0;
					var blnAlert = false;
					for (var i = 0; i < st.getCount(); i++) {
						var rec = st.getAt(i);
						
						var temp = detailSm.isSelected(i);
						if (temp) {
							d[0][intTemp] = rec.get("id");
							d[1][intTemp++] = "CLA";
						} else if (rec.get("isBreakOut") != '已拆除') {
							blnAlert = true;
						}
					}
					if (blnAlert) {
						if (!reasonText.getValue()) {
							Ext.Msg.alert(Constants.SYS_REMIND_MSG, '还有安措项目未拆除，请填写原因!');
							return;
						}
					}
					ids = d[0].join(",");
					isBreakOuts = d[1].join(",");
					if(isBreakOuts=="")
					{
						Ext.Msg.alert(Constants.SYS_REMIND_MSG, '请选择要操作的安措记录!');
							return;
					}
					
					validateWin.show();
				}
			});

	var workLable = new Ext.form.Label({
				id : 'workLable'
			});
			
	var myaddpanel = new Ext.FormPanel({
		region : 'center',
		layout : 'form',
		border : false,
		frame : true,
		labelAlign : 'top',
		bodyStyle : {
			'padding' : '5px 5px 0'		
		},
		items : [{
                    layout : "column",
                    border : false,
                    items : [{
                                columnWidth : 0.58,
                                layout : "form",
                                border : false,
                                items : [reasonText]
                            }, {
                                columnWidth : 0.38,
                                layout : "form",
                                border : false,
                                items : [workLable]
                            }],
                    buttonAlign : 'right',
                    buttons : [printBut, allBut, noneBut]
                }, {
                    layout : 'form',
                    border : false,
                    buttonAlign : 'right',
                    buttons : [saveBut]
                }
		]
	});
	
    // 弹出窗口
    new Ext.Viewport({
        enableTabScroll : true,   
        layout : "border",
        items : [detailGrid, myaddpanel, NotRemoveGrid]
    });
	
	// ==============     初始化        ==================
	// 安措拆除详细grid数据加载
	detailStore.baseParams = {
		workticketNo : workN
	};
	detailStore.on('load', function() {
		var rows = [];
		for (var i = 0; i < detailStore.getCount(); i++) {
			var record = detailStore.getAt(i);
			if (record.get("isBreakOut") == '已拆除') {
				rows.push(i);
			}
		}
		detailSm.selectRows(rows);
	})
	detailStore.load();
	
	// 安措未拆除原因grid数据加载
	NotRemoveStore.baseParams = {
		workticketNo : workN
	};
	NotRemoveStore.load();
	
	workLable.setText("<font color='red' size = 3>" + "工作票号为："
			+ workN + "</font>");
			
    	// 预览票面
    function viewRecord(){
       	 window.open(Constants.BirtReport(workN,Constants.SAFETY_CODE,Constants.SAFETY_KBN_CANCEL,""));                

    }
	
    //add by fyyang 090109
    //签字时的Enter
    document.onkeydown=function() {  
          if (event.keyCode==13) {  
         		 document.getElementById('btnSign').click();
   			}
    } 
});