Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
// 序列化为JSON字符串,不包括函数
Object.prototype.toJSONString = function() {
	var str = '';
	var obj = this;
	if (obj instanceof Array) {
		// 数组
		str = '[';
		for (var i = 0; i < obj.length; i++) {
			str += obj[i].toJSONString() + ',';
		}
		if (obj.length > 0) {
			// 去除最后的逗号
			str = str.slice(0, -1);
		}
		str += ']';
		return str;
	}

	str = '{';
	var sub = null;
	for (var prop in obj) {
		sub = obj[prop];
		if (sub == null || sub == undefined) {
			// 为NULL
			str += '"' + prop + '":null,';
		} else if (typeof sub == 'object') {
			if (sub instanceof Date) {
				// 转换时间格式
				str += '"' + prop + '":"' + renderDate(sub) + '",';
			} else {
				str += '"' + prop + '":' + sub.toJSONString() + ',';
			}
		} else if (typeof sub == 'number' || typeof sub == 'boolean') {
			// 布尔型或者数字
			str += '"' + prop + '":' + sub + ',';
		} else if (typeof sub != 'function') {
			str += '"' + prop + '":"' + sub + '",';
		}
	}
	if (str != '{') {
		// 去除最后的逗号
		str = str.slice(0, -1);
	}
	str += '}';
	return str;
}
Ext.onReady(function() {
	/** 房间类型 */
	var room = 0;
	/** 录入信息类型 1.会议名称；2.会议地点；3.会议要求；4.套间用品；5.单间用品；6.标间用品 */
	var inputType = 0;
	/** 员工名字 */
	var workerName = "";
	/** 员工部门 */
	var deptName = "";
	
	/************会议住宿房间物品选择弹出窗口 *********/
	
	var chkLabel = new Ext.form.Checkbox({
		boxLabel : "会议标签",
		hideLabel : true,
        inputValue : Constants.CHECKED_VALUE
	});
	var chkWater = new Ext.form.Checkbox({
		boxLabel : "矿泉水",
		hideLabel : true,
        inputValue : Constants.CHECKED_VALUE
	});
	var chkFruit = new Ext.form.Checkbox({
		boxLabel : "水果",
		hideLabel : true,
        inputValue : Constants.CHECKED_VALUE
	});
	var chkFlower = new Ext.form.Checkbox({
		boxLabel : "鲜花",
		hideLabel : true,
        inputValue : Constants.CHECKED_VALUE
	});
	var chkCiga = new Ext.form.Checkbox({
		boxLabel : "烟",
		hideLabel : true,
        inputValue : Constants.CHECKED_VALUE
	});
	var btnSubmit = new Ext.Button({
		text : "提交",
		handler : mySubmit
	})
	
	var itemsPanel = new Ext.FormPanel({
		frame : true,
		border : false,
		layout : "column",
		anchor : '95%',
		items : [{
					columnWidth : 0.2,
					layout : "form",
					border : false,
					items : [chkLabel]
				}, {
					columnWidth : 0.2,
					layout : "form",
					border : false,
					items : [chkWater]
				}, {
					columnWidth : 0.2,
					layout : "form",
					border : false,
					items : [chkFruit]
				}, {
					columnWidth : 0.2,
					layout : "form",
					border : false,
					items : [chkFlower]
				}, {
					columnWidth : 0.2,
					layout : "form",
					border : false,
					items : [chkCiga]
				}]
	});
	
	var itemsWin = new Ext.Window({
//		autoScroll : true,
		width : 450,
		height : 100,
		title : "会议住宿房间物品选择",
		buttonAlign : "center",
		items : [itemsPanel],
		layout : 'fit',
		closeAction : 'hide',
		modal : true,
		resizable : false,
		buttons : [btnSubmit]
	})
	
	/************  弹出窗口 *********/
		// 审批单号
		var txtBill = new Ext.form.TextField({
			        fieldLabel : "审批单号",
					readOnly : true
				});
		// 第0个panel
		var topPanel = new Ext.Panel({
					layout : 'form',
					labelWidth : 60,
					border : false,
					frame : false,
					style : 'padding-left:10px',
//					anchor : "95%",
					items : [txtBill]
				});
		/******************参会人员及会场情况**************/
		// 申请人
		var txtApplyer = new Ext.form.TextField({
					id : 'workerName',
					name : 'workerName',
					fieldLabel : "申请人",
					anchor : '95%',
					readOnly : true
				});
		// 申请部门
		var txtApplyDept = new Ext.form.TextField({
					id : 'deptName',
					name : 'deptName',
					fieldLabel : "申请部门",
					anchor : '95%',
					readOnly : true
				});
		// 第1panel第1行
		var line11 = new Ext.Panel({
					frame : false,
					border : false,
					layout : "column",
					anchor : '100%',
					items : [{
								columnWidth : 0.5,
								layout : "form",
								border : false,
								items : [txtApplyer]
							}, {
								columnWidth : 0.5,
								layout : "form",
								border : false,
								items : [txtApplyDept]
							}]
				});
		// 会议开始时间
		var txtMeetStartTime = new Ext.form.TextField({
					id : 'meetStartTime',
					name : 'meetStartTime',
					fieldLabel : "会议开始时间<font color='red'>*</font>",
					isFormField : true,
					readOnly : true,
					allowBlank : false,
					style : 'cursor:pointer',
					width : 128,
					listeners : {
						focus : function() {
							WdatePicker({
								dateFmt : 'yyyy-MM-dd HH:mm',
			                    isShowClear : false,
			                    onpicked : function(){
			                        txtMeetStartTime.clearInvalid();
			                    },
			                    onclearing:function(){
			                    	txtMeetStartTime.markInvalid();
			                    }
							});
						}
					},
					anchor : '95%'
				});
		// 会议结束时间
		var txtMeetEndTime = new Ext.form.TextField({
					id : 'meetEndTime',
					name : 'meetEndTime',
					fieldLabel : "会议结束时间",
					isFormField : true,
					readOnly : true,
					style : 'cursor:pointer',
					width : 128,
					//                name : 'entity.newStartTime',
					listeners : {
						focus : function() {
							WdatePicker({
										dateFmt : 'yyyy-MM-dd HH:mm'
									});
						}
					},
					anchor : '95%'
				});
		// 第1panel第2行
		var line12 = new Ext.Panel({
					frame : false,
					border : false,
					layout : "column",
					anchor : '100%',
					items : [{
								columnWidth : 0.5,
								layout : "form",
								border : false,
								items : [txtMeetStartTime]
							}, {
								columnWidth : 0.5,
								layout : "form",
								border : false,
								items : [txtMeetEndTime]
							}]
				});
		// 会议名称
		var txtMeetName = new Ext.form.TextField({
					id : 'meetInfo.meetName',
					name : 'meetInfo.meetName',
					fieldLabel : "会议名称<font color='red'>*</font>",
					width : 330,
					maxLength : 50,
					allowBlank : false
				});
		// 响应双击事件，打开录入窗口
//		txtMeetName.onDblClick(function() {
//			inputType = 1;
//			txtPop.setValue(txtMeetName.getValue());
//			txtPop.maxLength = 50;
//			winInput.show();
//	    })
		// 会议地点
		var txtMeetPlace = new Ext.form.TextField({
					id : 'meetInfo.meetPlace',
					name : 'meetInfo.meetPlace',
					fieldLabel : "会议地点",
					width : 330,
					maxLength : 25
				});
		// 响应双击事件，打开录入窗口
//		txtMeetPlace.onDblClick(function() {
//			inputType = 2;
//			txtPop.setValue(txtMeetPlace.getValue());
//			txtPop.maxLength = 25;
//			winInput.show();
//	    })
		// 会议要求
		var txtMeetNeed = new Ext.form.TextField({
					id : 'meetInfo.roomNeed',
					name : 'meetInfo.roomNeed',
					fieldLabel : "会议要求",
					width : 330,
					maxLength : 75
				});
		// 响应双击事件，打开录入窗口
//		txtMeetNeed.onDblClick(function() {
//			inputType = 3;
//			txtPop.setValue(txtMeetNeed.getValue());
//			txtPop.maxLength = 75;
//			winInput.show();
//	    })
		// 会议其他要求
		var txtOtherNeed = new Ext.form.TextArea({
					id : 'meetInfo.meetOther',
					name : 'meetInfo.meetOther',
					fieldLabel : "会议其他要求",
					height : 100,
					width : 328,
					maxLength : 100
				})
		// 第1panel第3行
		var line13 = new Ext.Panel({
					frame : false,
					border : false,
					layout : "form",
					anchor : '100%',
					items : [txtMeetName, txtMeetPlace, txtMeetNeed,
							txtOtherNeed]
				});
		// 会议就餐时间
		var txtDinnerTime = new Ext.form.TextField({
					id : 'dinnerTime',
					name : 'dinnerTime',
					fieldLabel : "会议就餐时间",
					isFormField : true,
					anchor : '95%',
					readOnly : true,
					style : 'cursor:pointer',
					width : 128,
					//                name : 'entity.newStartTime',
					listeners : {
						focus : function() {
							WdatePicker({
										dateFmt : 'yyyy-MM-dd HH:mm'
									});
						}
					}
				});
		// 会议就餐人数
		var numDinnerPeople = new Ext.form.MoneyField({
//					id : 'meetInfo.dinnerNum',
//					name : 'meetInfo.dinnerNum',
					fieldLabel : "会议就餐人数",
					anchor : '95%',
					allowDecimals : false,
                	allowNegative : false,
					style : 'text-align:right',
					appendChar : '人',
					maxLength : 8
				});
		// 第1panel第4行
		var line14 = new Ext.Panel({
					frame : false,
					border : false,
					layout : "column",
					anchor : '100%',
					items : [{
								columnWidth : 0.5,
								layout : "form",
								border : false,
								items : [txtDinnerTime]
							}, {
								columnWidth : 0.5,
								layout : "form",
								border : false,
								items : [numDinnerPeople]
							}]
				});
		// 第一个panel
		var firstPanel = new Ext.form.FieldSet({
					title : "参会人员及会场情况",
					border : true,
					labelWidth : 90,
					anchor : "95%",
					items : [line11, line12, line13, line14]
				});
		/******************会议用烟**************/
		// 名称
		var txtCigName = new Ext.form.TextField({
					id : 'meetInfo.cigName',
					name : 'meetInfo.cigName',
					fieldLabel : "名称",
					anchor : '95%',
					maxLength : 25
				});
		// 单价
		var numCigPrice = new Ext.form.MoneyField({
					id : 'numCigPrice',
					fieldLabel : "单价",
					style : 'text-align:right',
					anchor : '95%',
					padding : 2,
					readOnly : true,
					appendChar : '元/包'
				});
		var hideCigPrice = new Ext.form.Hidden({
			id : 'hideCigPrice',
			name : 'meetInfo.cigPrice'
		});
		// 数量
		var numCigQty = new Ext.form.MoneyField({
//					id : 'meetInfo.cigNum',
//					name : 'meetInfo.cigNum',
					fieldLabel : "数量",
					style : 'text-align:right',
					anchor : '95%',
					appendChar : '包',
					allowDecimals : false,
                	allowNegative : false,
					maxLength : 8
				});
		// 第二个panel里的第一个panel
		var second1Panel = new Ext.form.FieldSet({
					title : "会议用烟",
					labelWidth : 40,
					border : true,
					anchor : "100%",
					items : [txtCigName, numCigPrice, hideCigPrice, numCigQty]
				});
		/******************会议用酒**************/
		// 名称
		var txtWineName = new Ext.form.TextField({
					id : 'meetInfo.wineName',
					name : 'meetInfo.wineName',
					fieldLabel : "名称",
					anchor : '95%',
					maxLength : 25
				});
		// 单价
		var numWinePrice = new Ext.form.MoneyField({
					id : 'numWinePrice',
					labelWidth : 10,
					fieldLabel : "单价",
					style : 'text-align:right',
					anchor : '95%',
					padding : 2,
					readOnly : true,
					appendChar : '元/瓶'
				});
		var hideWinePrice = new Ext.form.Hidden({
			id : 'hideWinePrice',
			name : 'meetInfo.winePrice'
		});
		// 数量
		var numWineQty = new Ext.form.MoneyField({
//					id : 'meetInfo.wineNum',
//					name : 'meetInfo.wineNum',
					fieldLabel : "数量",
					anchor : '95%',
					allowDecimals : false,
                	allowNegative : false,
					style : 'text-align:right',
					appendChar : '瓶',
					maxLength : 8
				});
		// 第二个panel里的第二个panel
		var second2Panel = new Ext.form.FieldSet({
					title : "会议用酒",
					border : true,
					labelWidth : 40,
					anchor : "100%",
					items : [txtWineName, numWinePrice, hideWinePrice, numWineQty]
				});
		// 第二个panel
		var secondPanel = new Ext.Panel({
					frame : false,
					border : false,
					layout : "column",
					anchor : '95%',
					items : [{
								columnWidth : 0.475,
								layout : "form",
								border : false,
								items : [second1Panel]
							}, {
								columnWidth : 0.05,
								layout : "form",
								border : false
							}, {
								columnWidth : 0.475,
								layout : "form",
								border : false,
								items : [second2Panel]
							}]
				});
		/******************会议住宿及就餐费用**************/
		// 套间数量
		var numTJQty = new Ext.form.MoneyField({
//					id : 'meetInfo.tfNum',
//					name : 'meetInfo.tfNum',
					anchor : "95%",
					fieldLabel : "套间数量",
					style : 'text-align:right',
					allowDecimals : false,
                	allowNegative : false,
					appendChar : '间',
					maxLength : 8
				});
		// 套间用品
		var txtTJThing = new Ext.form.TextField({
					id : 'meetInfo.tfThing',
					name : 'meetInfo.tfThing',
					anchor : "95%",
					fieldLabel : "套间用品",
					maxLength : 50
				});
		// ...按钮
		var btnTJ = new Ext.Button({
					text : "...",
					handler : function() {
						// check是否为空
						// 不为空
						if (null != txtTJThing.getValue() && "" != txtTJThing.getValue()) {
							Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, 
							MessageConstants.AR001_C_001 , function(buttonobj) {
								if ("yes" == buttonobj) {
									room = 1;
									setTableItems();
								}
							})
						} else {
							room = 1;
							setTableItems();
						}
					}
				});
		// 第3个panel的第1行
		var line31 = new Ext.Panel({
					frame : false,
					border : false,
					layout : "column",
					anchor : '100%',
					items : [{
								columnWidth : 0.35,
								layout : "form",
								border : false,
								items : [numTJQty]
							}, {
								columnWidth : 0.55,
								layout : "form",
								border : false,
								items : [txtTJThing]
							}, {
								columnWidth : 0.1,
								layout : "form",
								border : false,
								items : [btnTJ]
							}]
				});
		// 单间数量
		var numDJQty = new Ext.form.MoneyField({
//					id : 'meetInfo.djNum',
//					name : 'meetInfo.djNum',
					anchor : "95%",
					fieldLabel : "单间数量",
					allowDecimals : false,
                	allowNegative : false,
					style : 'text-align:right',
					appendChar : '间',
					maxLength : 8
				});
		// 单间用品
		var txtDJThing = new Ext.form.TextField({
					id : 'meetInfo.djThing',
					name : 'meetInfo.djThing',
					anchor : "95%",
					fieldLabel : "单间用品",
					maxLength : 50
				});
		// ...按钮
		var btnDJ = new Ext.Button({
					text : "...",
					handler : function() {
						// check是否为空
						// 不为空
						if (null != txtDJThing.getValue() && "" != txtDJThing.getValue()) {
							Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, 
							MessageConstants.AR001_C_001 , function(buttonobj) {
								if ("yes" == buttonobj) {
									room = 2;
									setTableItems();
								}
							})
						} else {
							room = 2;
							setTableItems();
						}
					}
				});
		// 第3个panel的第2行
		var line32 = new Ext.Panel({
					frame : false,
					border : false,
					layout : "column",
					anchor : '100%',
					items : [{
								columnWidth : 0.35,
								layout : "form",
								border : false,
								items : [numDJQty]
							}, {
								columnWidth : 0.55,
								layout : "form",
								border : false,
								items : [txtDJThing]
							}, {
								columnWidth : 0.1,
								layout : "form",
								border : false,
								items : [btnDJ]
							}]
				});
		// 标间数量
		var numBJQty = new Ext.form.MoneyField({
//					id : 'meetInfo.bjNum',
//					name : 'meetInfo.bjNum',
					anchor : "95%",
					fieldLabel : "标间数量",
					allowDecimals : false,
                	allowNegative : false,
					style : 'text-align:right',
					appendChar : '间',
					maxLength : 8
				});
		// 标间用品
		var txtBJThing = new Ext.form.TextField({
					id : 'meetInfo.bjThing',
					name : 'meetInfo.bjThing',
					anchor : "95%",
					fieldLabel : "标间用品",
					maxLength : 50
				});
		// ...按钮
		var btnBJ = new Ext.Button({
					text : "...",
					handler : function() {
						// check是否为空
						// 不为空
						if (null != txtBJThing.getValue() && "" != txtBJThing.getValue()) {
							Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, 
							MessageConstants.AR001_C_001 , function(buttonobj) {
								if ("yes" == buttonobj) {
									room = 3;
									setTableItems();
								}
							})
						} else {
							room = 3;
							setTableItems();
						}
					}
				});
		// 第3个panel的第3行
		var line33 = new Ext.Panel({
					frame : false,
					border : false,
					layout : "column",
					anchor : '100%',
					items : [{
								columnWidth : 0.35,
								layout : "form",
								border : false,
								items : [numBJQty]
							}, {
								columnWidth : 0.55,
								layout : "form",
								border : false,
								items : [txtBJThing]
							}, {
								columnWidth : 0.1,
								layout : "form",
								border : false,
								items : [btnBJ]
							}]
				});
		// 会议就餐标准
		var txtDinnerBZ = new Ext.form.MoneyField({
					id : 'meetInfo.dinnerBz',
					anchor : "95%",
					fieldLabel : "会议就餐标准",
					padding : 2,
					style : 'text-align:right',
					readOnly : true
				});
		// 费用预算汇总
		var txtAllCost = new Ext.form.MoneyField({
					id : 'meetInfo.budpayInall',
					anchor : "95%",
					fieldLabel : "费用预算汇总",
					padding : 2,
					style : 'text-align:right',
					readOnly : true
				});
		// 实际费用汇总
		var txtAllCostTrue = new Ext.form.MoneyField({
					id : 'meetInfo.realpayInall',
					anchor : "95%",
					fieldLabel : "实际费用汇总",
					padding : 2,
					style : 'text-align:right',
					readOnly : true
				});
		// 第3个panel的第4行
		var line34 = new Ext.Panel({
					frame : false,
					border : false,
					layout : "column",
					anchor : '100%',
//					items : [{
//								columnWidth : 0.3,
//								layout : "form",
//								border : false,
//								items : [txtDinnerBZ]
//							}, {
//								columnWidth : 0.35,
//								layout : "form",
//								border : false,
//								items : [txtAllCost]
//							}, {
//								columnWidth : 0.35,
//								layout : "form",
//								border : false,
//								items : [txtAllCostTrue]
//							}]
					items : [{
								columnWidth : 0.5,
								layout : "form",
								border : false,
								items : [txtDinnerBZ]
							}, {
								columnWidth : 0.5,
								layout : "form",
								border : false,
								items : [txtAllCost]
							}]
				});
		var line35 = new Ext.Panel({
					frame : false,
					border : false,
					layout : "column",
					anchor : '100%',
					items : [{
								columnWidth : 0.5,
								layout : "form",
								border : false,
								items : [txtAllCostTrue]
							}]
				});
		// 第三个panel
		var thirdPanel = new Ext.form.FieldSet({
					title : "会议住宿及就餐费用",
					border : true,
					labelWidth : 80,
					anchor : "95%",
					items : [line31, line32, line33, line34, line35]
				});
		/******************会议材料及会议桌签**************/
		// 上传附件
	    var picSelectTxt = new Ext.form.TextField({
	        id : "picSelect",
	        name : "file",
	        height : 21,
	        inputType : 'file',
	        initEvents : function(){
	        	Ext.form.TextField.prototype.initEvents.apply(this, arguments);
//	        	var keydown = function(e){
//		            e.stopEvent();
//		        };
//		        this.el.on("keydown", keydown, this);
	        }
//	        anchor : "100%"
	    });
		var btnUpload = new Ext.Button({
					text : "上传附件",
					iconCls : Constants.CLS_UPLOAD,
					handler : onUpload
				});
		var filePanel = new Ext.form.FormPanel({
			frame : false,
			border : false,
			fileUpload : true,
			height : 30,
			items : [picSelectTxt, btnUpload]
		});
		// 删除附件
//		var btnDelete = new Ext.Button({
//					text : "删除附件",
//					handler : onDeleteFile
//				});
		// 文件名grid
		// grid中的数据bean
		var rungridlist = new Ext.data.Record.create([{
					name : 'fileName'
				}, {
					name : 'id'
				}, {
					name : 'fileText'
				}, {
					name : 'updateTime'
				}]);
		// grid的store
		var searchStore = new Ext.data.JsonStore({
					url : 'administration/getMeetApplyReportAffixList.action',
					root : 'list',
					totalProperty : 'totalCount',
					fields : rungridlist
				});
//		searchStore.setDefaultSort('fileName', 'ASC');

		var mycolumn = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
					header : '行号',
					sortable : true,
					width : 35,
					align : 'left'
				}),{
					header : "文件名",
					width : 320,
					sortable : true,
					dataIndex : 'fileName',
					renderer : showFile
				}, {
					header : "删除附件",
					width : 70,
					sortable : true,
					dataIndex : 'fileName',
					align : 'center',
					renderer : dele
				}
				]);
		// 页面的Grid主体
		var rungrid = new Ext.grid.GridPanel({
				autoScroll : true,
				id : 'rungrid',
				height : 150,
				store : searchStore,
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : true
						}),
				// 头部工具栏
				tbar : new Ext.Toolbar(filePanel),
				cm : mycolumn,
				viewConfig : {
					forceFit : false
				},
				border : false,
				enableColumnHide : true,
				enableColumnMove : false
			});
//		var gridpPanel = new Ext.FormPanel({
//			frame : false,
//			border : false,
//			anchor : '95%',
//			items : [rungrid]
//		});
		// 第4个panel
		var fourthPanel = new Ext.form.FieldSet({
			title : "会议材料及会议桌签",
			border : true,
			anchor : "95%",
			layout : 'form',
			items : [rungrid]
		});
		/******************其他*************************/
		// 保存按钮
		var btnSave = new Ext.Button({
					text : "保存",
					iconCls : Constants.CLS_SAVE,
					handler : onSave
				});
		// 取消按钮
		var btnCancel = new Ext.Button({
					text : "取消",
					iconCls : Constants.CLS_CANCEL,
					handler : onCancle
				});
		/******************隐藏域*************************/
		// 上次修改时间
		var hidLastModify = new Ext.form.Hidden({
			id : 'modifyTime'
		})
		// 流水号
		var hidId = new Ext.form.Hidden({
			id : 'meetInfo.id'
		})
		// 申请单编号
		var hidNo = new Ext.form.Hidden({
			id : 'meetInfo.meetId'
		})

		// 定义弹出窗体中的form
		var mypanel = new Ext.FormPanel({
					autoScroll : true,
					labelAlign : 'right',
					//                autoHeight : true,
					border : false,
					frame : true,
//					tbar : ["审批单号:", txtBill],
					fileUpload : true,
					items : [topPanel, firstPanel, secondPanel, 
					thirdPanel, fourthPanel, hidLastModify,
					hidId, hidNo]
				});

		// 定义弹出窗体
		var win = new Ext.Window({
					autoScroll : true,
					width : 510,
					height : 500,
					title : "",
					buttonAlign : "center",
					items : [mypanel],
					layout : 'fit',
					closeAction : 'hide',
					modal : true,
					resizable : false,
					buttons : [btnSave, btnCancel]
				});
		
	/**************弹出窗口结束...************/
//		win.show();
	// grid中的数据bean
	var maingridlist = new Ext.data.Record.create([{
			// 申请人
				name : 'workerName'
			}, {
			// 申请部门
				name : 'deptName'
			}, {
			// 会议名称
				name : 'meetInfo.meetName'
			}, {
			// 会议开始时间
				name : 'meetStartTime'
			}, {
			// 会议结束时间
				name : 'meetEndTime'
			}, {
			// 会议地点
				name : 'meetInfo.meetPlace'
			}, {
			// 会场要求
				name : 'meetInfo.roomNeed'
			}, {
			// 会议其他要求
				name : 'meetInfo.meetOther'
			}, {
			// 会议就餐时间
				name : 'dinnerTime'
			}, {
			// 会议就餐人数
				name : 'meetInfo.dinnerNum'
			}, {
			// 会议用烟名称
				name : 'meetInfo.cigName'
			}, {
			// 会议用烟单价
				name : 'meetInfo.cigPrice'
			}, {
			// 会议用烟数量
				name : 'meetInfo.cigNum'
			}, {
			// 会议用酒名称
				name : 'meetInfo.wineName'
			}, {
			// 会议用酒单价
				name : 'meetInfo.winePrice'
			}, {
			// 会议用酒数量
				name : 'meetInfo.wineNum'
			}, {
			// 套间数量
				name : 'meetInfo.tfNum'
			}, {
			// 套间用品
				name : 'meetInfo.tfThing'
			}, {
			// 单间数量
				name : 'meetInfo.djNum'
			}, {
			// 单间用品
				name : 'meetInfo.djThing'
			}, {
			// 标间数量
				name : 'meetInfo.bjNum'
			}, {
			// 标间用品
				name : 'meetInfo.bjThing'
			}, {
			// 会议就餐标准
				name : 'meetInfo.dinnerBz'
			}, {
			// 费用预算汇总
				name : 'meetInfo.budpayInall'
			}, {
			// 上次修改日期
				name : 'modifyTime'
			}, {
			// 流水号
				name : 'meetInfo.id'
			},{
			// 会议申请单号
				name : 'meetInfo.meetId'
			}, {
			// 费用实际汇总
				name : 'meetInfo.realpayInall'
			}]);

	// grid的store
	var mainStore = new Ext.data.JsonStore({
				url : 'administration/getMeetApplyReportList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : maingridlist
			});
	myLoad();
	// 设置默认排序列——部门名称
//	mainStore.setDefaultSort('deptName', 'ASC');
	// 列
	var maincolumn = new Ext.grid.ColumnModel([
		new Ext.grid.RowNumberer({
				header : '行号',
				sortable : true,
				width : 35,
				align : 'left'
			}), {
				header : "申请人",
				width : 70,
				sortable : true,
				dataIndex : 'workerName'
			}, {
				header : "申请部门",
				width : 70,
				sortable : true,
				dataIndex : 'deptName'
			}, {
				header : "会议名称",
				width : 50,
				sortable : true,
				dataIndex : 'meetInfo.meetName'
			}, {
				header : "会议开始时间",
				width : 100,
				sortable : true,
				dataIndex : 'meetStartTime'
			}]);
	// tbar 的button
	// 新增按钮
	var addBtn = new Ext.Button({
				text : Constants.BTN_ADD,
				iconCls : Constants.CLS_ADD,
				handler : onAdd
			});
	// 修改按钮
	var modifyBtn = new Ext.Button({
				text : Constants.BTN_UPDATE,
				iconCls : Constants.CLS_UPDATE,
				handler : onModify
			});
	// 删除按钮
	var deleteBtn = new Ext.Button({
				text : Constants.BTN_DELETE,
				iconCls : Constants.CLS_DELETE,
				handler : onDelete
			});
	// 上报按钮
	var reportBtn = new Ext.Button({
				text : "上报",
				handler : onReport,
				iconCls : Constants.CLS_REPOET
			});
	// 页面的Grid主体
	var maingrid = new Ext.grid.GridPanel({
				id : 'maingrid',
				store : mainStore,
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : true
						}),
				cm : maincolumn,
				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : Constants.PAGE_SIZE,
							store : mainStore,
							displayInfo : true,
							displayMsg : MessageConstants.DISPLAY_MSG,
							emptyMsg : MessageConstants.EMPTY_MSG
						}),
				viewConfig : {
					forceFit : true
				},
				tbar : [addBtn, modifyBtn, deleteBtn, reportBtn],
				border : false,
				enableColumnHide : true,
				enableColumnMove : false
	});
	// 双击处理
	maingrid.on('rowdblclick', onModify);
//	mainStore.load({
//				params : {
//					start : 0,
//					limit : Constants.PAGE_SIZE
//				}
//			});
	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : 'fit',
				border : false,
				frame : true,
				items : [{
							layout : 'fit',
							border : false,
							items : [maingrid]
						}]
			});
	
	//*****详细信息录入窗口*******//
	var txtPop = new Ext.form.TextArea({
				id : "pop",
				name : "pop",
				maxLength : 100
			});

	var winInput = new Ext.Window({
				width : 350,
				modal : true,
				height : 200,
				title : '详细信息录入窗口',
				buttonAlign : "center",
				items : [txtPop],
				layout : 'fit',
				closeAction : 'hide',
				resizable : false,
				buttons : [{
					text : "确认",
					id : "back",
					handler : function() {
						if(!txtPop.validate()) {
							return;
						}
						
						var tempValue = txtPop.getValue().replace(/\n/g, '');
						if (1 == inputType) {
							txtMeetName.setValue(tempValue);
						} else if (2 == inputType) {
							txtMeetPlace.setValue(tempValue);
						} else if (3 == inputType) {
							txtMeetNeed.setValue(tempValue);
						} else if (4 == inputType) {
							txtTJThing.setValue(tempValue);
						} else if (5 == inputType) {
							txtDJThing.setValue(tempValue);
						} else if (6 == inputType) {
							txtBJThing.setValue(tempValue);
						}
						winInput.hide();
					}
				},{
					text : "取消",
					id : "cancel",
					handler : function() {
						winInput.hide();
					}
				}]
			});
	/******************处理函数***********************/
	win.on('show', function() {
        // 清除附件内容
        picSelectTxt.clearFilePath();
        // 滚动条
        mypanel.body.scrollTo("top");
	});
	
	/**
	 * 新增记录
	 */
	function onAdd() {
		mypanel.getForm().reset();
		txtBill.setValue("");
		
		win.setTitle("新增会议申请");
		// 设定该用户的姓名和部门
		txtApplyer.setValue(workerName);
		txtApplyDept.setValue(deptName);
		win.show();
		
        // 更新附件列表
		getAffixList(true);
	}
	
	/**
	 * 修改记录
	 */
	function onModify() {
		if (maingrid.selModel.hasSelection()) {
			var selectedRow = maingrid.selModel.getSelected();
			
			txtBill.setValue(selectedRow.get("meetInfo.meetId"));
			win.show();
			mypanel.getForm().loadRecord(selectedRow);
			
			// 为不能submit的字段亲自赋值
			/** 画面就餐人数 */
			numDinnerPeople.setValue(selectedRow.get("meetInfo.dinnerNum"));
			/** 画面用烟数量 */
			numCigQty.setValue(selectedRow.get("meetInfo.cigNum"));
			/** 画面用酒数量 */
			numWineQty.setValue(selectedRow.get("meetInfo.wineNum"));
			/** 画面套间数量 */
			numTJQty.setValue(selectedRow.get("meetInfo.tfNum"));
			/** 画面单间数量 */
			numDJQty.setValue(selectedRow.get("meetInfo.djNum"));
			/** 画面标间数量 */
			numBJQty.setValue(selectedRow.get("meetInfo.bjNum"));
			/** 画面用烟单价 */
			numCigPrice.setValue(selectedRow.get("meetInfo.cigPrice"));
			/** 画面用酒单价 */
			numWinePrice.setValue(selectedRow.get("meetInfo.winePrice"));
			win.setTitle("修改会议申请");
			
	        // 更新附件列表
			getAffixList();
		} else {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, 
			MessageConstants.COM_E_016);
		}
	}
	
	/**
	 * 删除记录
	 */
	function onDelete() {
		if (maingrid.selModel.hasSelection()) {
			Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, MessageConstants.COM_C_002 , function(
					buttonobj) {
				if (buttonobj == "yes") {
					// 获得选中的记录
					var record = maingrid.selModel.getSelected();
					// 获得记录的申请单号
					var meetNo = record.get("meetInfo.meetId");
					// 获得该记录被读出时的修改时间
					var userLastModifyTime = record.get("modifyTime");
					Ext.Ajax.request({
							method : Constants.POST,
							url : 'administration/deleteMeetApplyReport.action',
							success : function(result, request) {
								// 成功，显示删除成功信息
								var o = eval("(" + result.responseText + ")");
								// 排他异常
								if(o.msg == "U") {
									Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
											MessageConstants.COM_I_002);
									myLoad();
								// 数据库异常
								} else if(o.msg == "SQL") {
									Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
											MessageConstants.COM_E_014);
								// 删除成功
								} else {
									Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
											MessageConstants.COM_I_005, myLoad);
								}
							},
							failure : function() {
								Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
										Constants.UNKNOWN_ERR);
									myLoad();
							},
							params : {
								meetNo : meetNo,
								userLastModifyTime : userLastModifyTime
							}
						});
				}
			});
		} else {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, 
			MessageConstants.COM_E_016);
		}
	}
	/**
	 * 上报记录
	 */
	function onReport() {
		if (maingrid.selModel.hasSelection()) {
			Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, MessageConstants.COM_C_006 , function(
					buttonobj) {
				if (buttonobj == "yes") {
					// 获得选中的记录
					var record = maingrid.selModel.getSelected();
					// 获得记录的申请单号
					var meetNo = record.get("meetInfo.meetId");
					// 获得该记录被读出时的修改时间
					var userLastModifyTime = record.get("modifyTime");
					Ext.Ajax.request({
							method : Constants.POST,
							url : 'administration/reportMeetApplyReport.action',
							success : function(result, request) {
								// 成功，显示上报成功信息
								var o = eval("(" + result.responseText + ")");
								// 排他异常
								if(o.msg == "U") {
									Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
											Constants.COM_I_002);
									myLoad();
								// 数据库异常
								} else if(o.msg == "SQL") {
									Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
											MessageConstants.COM_E_014);
									myLoad();
								// 上报成功
								} else {
									Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
											MessageConstants.COM_I_007, myLoad);
								}
							},
							failure : function() {
								Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
										Constants.UNKNOWN_ERR);
									myLoad();
							},
							params : {
								meetNo : meetNo,
								userLastModifyTime : userLastModifyTime
							}
						});
				}
			});
		} else {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, 
			MessageConstants.COM_E_016);
		}
	}
	
	// 设置隐藏字段的值
	function setHideFieldValue() {
		// 会议用烟单价
		hideCigPrice.setValue(numCigPrice.getValue());
		// 会议用酒单价
		hideWinePrice.setValue(numWinePrice.getValue());
	}
	
	/**
	 * 上传附件
	 */
	function onUpload() {
		if ("新增会议申请" == win.title) {
			Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, 
				MessageConstants.AS002_C_001);
			return;
		}
		
		if (!checkFilePath(picSelectTxt.getValue())) {
			Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
							MessageConstants.COM_E_024);
			return;
		}
		// 设置隐藏字段的值
		setHideFieldValue();
		// 提交表单
		mypanel.getForm().submit({
			method : Constants.POST,
			params : {
				meetNo : txtBill.getValue()
			},
			url : 'administration/addMeetApplyReportAffix.action',
			success : function(form, action) {
				var o = eval("(" +action.response.responseText + ")");
				// 排他
				if (o.msg == Constants.DATA_USING) {
					Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
							MessageConstants.COM_I_002);
					return;
				}
				// Sql错误
				if (o.msg == Constants.SQL_FAILURE) {
					Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
							MessageConstants.COM_E_014);
					return;
				}
				// IO错误
				if (o.msg == Constants.IO_FAILURE) {
					Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
							MessageConstants.COM_E_022);
					return;
				}
				// 数据格式化错误
				if (o.msg == Constants.DATE_FAILURE) {
					Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
							MessageConstants.COM_E_023);
					return;
				}
				// 文件不存在
				if (o.msg == Constants.FILE_NOT_EXIST) {
					Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
							MessageConstants.COM_E_024);
					return;
				}
				
				Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
						MessageConstants.UPLOAD_SUCCESS, function() {
							// 重新加载数据
							searchStore.reload();
				});
			},
			failure : function() {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, MessageConstants.UNKNOWN_ERR);
			}
		});
	}
	
    function renderModifyDate(value) {
    	if (!value) return "";
    	if (value instanceof Date) {
    		return value.dateFormat('Y-m-d H:i:s');
    	}
        var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
        var reTime = /\d{2}:\d{2}:\d{2}/gi;
        var strDate = value.match(reDate);
        var strTime = value.match(reTime);
        if (!strDate) return "";
        strTime = strTime ? strTime[0] : '00:00:00';
        return strDate[0] + " " + strTime;
    }
	/**
	 * 删除附件
	 */
	deleteQuestFile = function () {
		if (rungrid.selModel.hasSelection()) {
			Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, MessageConstants.COM_C_002 , function(
					buttonobj) {
				if (buttonobj == "yes") {
					// 获得选中的记录
					var record = rungrid.selModel.getSelected();
					// 获得记录的申请单号
					var affixId = record.get("id");
					
					var fileUpdateTime = renderModifyDate(record.get("updateTime"));
					Ext.Ajax.request({
							method : Constants.POST,
							url : 'administration/deleteMeetApplyReportAffix.action',
							success : function(result, request) {
								// 成功，显示删除成功信息
								var o = eval("(" + result.responseText + ")");
								// 排他
								if (o.msg == Constants.DATA_USING) {
									Ext.Msg.alert(
											MessageConstants.SYS_ERROR_MSG,
											MessageConstants.COM_I_002);
									return;
								}
								// Sql错误
								if (o.msg == Constants.SQL_FAILURE) {
									Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
											MessageConstants.COM_E_014);
									return;
								}
								// IO错误
								if (o.msg == Constants.IO_FAILURE) {
									Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
											MessageConstants.COM_E_022);
									return;
								}
								// 数据格式化错误
								if (o.msg == Constants.DATE_FAILURE) {
									Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
											MessageConstants.COM_E_023);
									return;
								}
								
								Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
										MessageConstants.COM_I_005, getAffixList);
							},
							failure : function() {
								Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
										Constants.UNKNOWN_ERR);
									getAffixList();
							},
							params : {
								affixId : affixId,
								fileUpdateTime : fileUpdateTime
							}
						});
				}
			});
		} else {
			Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, 
			MessageConstants.SELECT_NULL_DEL_MSG);
		}
	}
	
	/**
	 * 由申请单号得到会议附件list
	 */
	function getAffixList(addFlag) {
		var meetNo = '';
		if (addFlag !== true) {
			// 获得选中的记录
			var record = maingrid.selModel.getSelected();
			// 获得记录的申请单号
			meetNo = record.get("meetInfo.meetId");
		}
		
		searchStore.baseParams = {
			meetNo : meetNo
		}
		searchStore.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			}
		});
	}
	/**
	 * 设置桌签
	 */
	function setTableItems() {
		itemsPanel.getForm().reset();
		itemsWin.show();
	}
	/**
	 * 房间物品提交
	 */
	function mySubmit() {
		/** 选中的物品 */
		var sitems = "";
		// check是否没有选中任何一个checkbox
		if (!chkLabel.checked && !chkWater.checked &&
			!chkFruit.checked && !chkFlower.checked &&
			!chkCiga.checked) {
				Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, MessageConstants.AR001_E_001);
		} else {
			if (chkLabel.checked) {
				sitems += chkLabel.boxLabel;
				sitems += " ";
			}
			if (chkWater.checked) {
				sitems += chkWater.boxLabel;
				sitems += " ";
			}
			if (chkFruit.checked) {
				sitems += chkFruit.boxLabel;
				sitems += " ";
			}
			if (chkFlower.checked) {
				sitems += chkFlower.boxLabel;
				sitems += " ";
			}
			if (chkCiga.checked) {
				sitems += chkCiga.boxLabel;
				sitems += " ";
			}
			sitems = sitems.substring(0, sitems.length - 1);
			itemsWin.hide();
			if (1 == room) {
				txtTJThing.setValue(sitems);
			} else if (2 == room) {
				txtDJThing.setValue(sitems);
			} else if (3 == room) {
				txtBJThing.setValue(sitems);
			}
		}
	}

	/**
	 * 保存
	 */
	function onSave() {
		// 设置隐藏字段的值
		setHideFieldValue();
		
		// 开始时间必须大于结束时间
		if (txtMeetStartTime.getValue() !== "" && txtMeetEndTime.getValue() !== "") {
			if (txtMeetStartTime.getValue() > txtMeetEndTime.getValue()) {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
								MessageConstants.COM_E_009, "开始时间", "结束时间"));
				return;
			}
		}
		
		// 检查输入是否合法
		if (txtMeetStartTime.getValue() !== "" && txtMeetName.getValue() !== "") {
			Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, 
				MessageConstants.COM_C_001 , function(buttonobj) {
					if ("yes" == buttonobj) {
								// 为几个渲染的字段取值，然后以param方式传给后台
								/** 画面就餐人数 */
								var dinnerPeople = numDinnerPeople.getValue();
								/** 画面用烟数量 */
								var cigNum = numCigQty.getValue();
								/** 画面用酒数量 */
								var wineNum = numWineQty.getValue();
								/** 画面套间数量 */
								var TJNum = numTJQty.getValue();
								/** 画面单间数量 */
								var DJNum = numDJQty.getValue();
								/** 画面标间数量 */
								var BJNum = numBJQty.getValue();
	//						txtMeetName.isValid() && txtMeetPlace.isValid() &&
	//							txtMeetNeed.isValid() && txtOtherNeed.isValid() &&
	//							numDinnerPeople.isValid() && txtCigName.isValid() &&
	//							numCigQty.isValid() && txtWineName.isValid() &&
	//							numWineQty.isValid() && numTJQty.isValid() &&
	//							txtTJThing.isValid() && numDJQty.isValid() &&
	//							txtDJThing.isValid() && numBJQty.isValid() &&
	//							txtBJThing.isValid() && txtMeetStartTime.isValid()
							// 新增记录
							if ("新增会议申请" == win.title) {
					    	        // 提交表单
							        mypanel.getForm().submit({
							            method : Constants.POST,
							            url : 'administration/addMeetApplyReport.action',
							            params : {
											/** 画面就餐人数 */
											dinnerPeople : dinnerPeople,
											/** 画面用烟数量 */
											cigNum : cigNum,
											/** 画面用酒数量 */
											wineNum : wineNum,
											/** 画面套间数量 */
											TJNum : TJNum,
											/** 画面单间数量 */
											DJNum : DJNum,
											/** 画面标间数量 */
											BJNum : BJNum
										},
							            success : function(form, action) {
											var result = eval("(" + action.response.responseText + ")");
											if (result.success == true) {
												// 数据库异常
												if(result.msg == "SQL") {
													Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
															MessageConstants.COM_E_014);
												// 修改成功
												} else {
													Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
															MessageConstants.COM_I_004, function(){
																myLoad();
																win.hide();
													});
												}
											}
							            },
							            faliue : function() {
							            	// 保存失败
											Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
													MessageConstants.OPERATE_ERROR_MSG);
							            }
								  });
							// 修改记录
							} else if ("修改会议申请" == win.title){
								// 获得选中的记录
								var record = maingrid.selModel.getSelected();
								// 获得记录的申请单号
								var meetNo = record.get("meetInfo.meetId");
								// 获得该记录被读出时的修改时间
								var userLastModifyTime = record.get("modifyTime");
								// 提交表单
							        mypanel.getForm().submit({
							            method : Constants.POST,
							            url : 'administration/modifyMeetApplyReport.action',
							            params : {
											meetNo : meetNo,
											userLastModifyTime : userLastModifyTime,
											/** 画面就餐人数 */
											dinnerPeople : dinnerPeople,
											/** 画面用烟数量 */
											cigNum : cigNum,
											/** 画面用酒数量 */
											wineNum : wineNum,
											/** 画面套间数量 */
											TJNum : TJNum,
											/** 画面单间数量 */
											DJNum : DJNum,
											/** 画面标间数量 */
											BJNum : BJNum
										},
							            success : function(form, action) {
											var result = eval("(" + action.response.responseText + ")");
											if (result.success == true) {
												// 排他异常
												if(result.msg == "U") {
													Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
															MessageConstants.COM_I_002);
												// 数据库异常
												} else if(result.msg == "SQL") {
													Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
															MessageConstants.COM_E_014);
												// 修改成功
												} else {
													Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
															MessageConstants.COM_I_004, function(){
																myLoad();
																win.hide();
													});
												}
											}
							            },
							            faliue : function() {
							            	// 保存失败
											Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
													MessageConstants.OPERATE_ERROR_MSG);
							            }
								  });
							}
						
					}
				})
			// 会议开始时间
			} else if ("" === txtMeetStartTime.getValue()) {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
								MessageConstants.COM_E_003, "会议开始时间"));
				return;
			// 会议名称
			} else if ("" === txtMeetName.getValue()) {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
								MessageConstants.COM_E_002, "会议名称"));
				return;
			}
	}
	/**
	 * 取消
	 */
	function onCancle() {
		Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, 
		MessageConstants.COM_C_005, function(buttonobj) {
			if ("yes" == buttonobj) {
				win.hide();
			}
		})
	}
	
	/** 
	 * 通过另一种方式load，可以取得登陆用户的名字和部门
	 */
	function myLoad() { 
	Ext.Ajax.request({
			url : 'administration/getMeetApplyReportList.action',
			method : 'POST',
			params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				},
			success :function(action){
				var res = action.responseText;
				var obj = eval( "(" + res + ")" );
				mainStore.loadData(obj);
				// 查询结果是否为空的判断，如为空则提示
//				if (0 == mainStore.getCount()) {
//					Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, 
//					MessageConstants.COM_I_003);
//				}
				workerName = obj.workerName;
				deptName = obj.deptName;
			}
		})
	}
	function dele(value) {
		if(value) {
    		return "<a href='#'  onclick= 'deleteQuestFile();return false;'><img src='comm/ext/tool/dialog_close_btn.gif'></a>";
    	} else {
    		return "";
    	}
	}
	
	// 下载的实现
	 /**
     * 附件名
     */
    function showFile(value, cellmeta, record) {
    	if(value != "") {
    		var idFile = record.get("id");
    		var download = 'download("' + idFile + '");return false;';
    		return "<a href='#' onclick='"+ download+ "'>" + value + "</a>";
    	} else {
    		return "";
    	}
    }
    
    /**
	 * 下载文件
	 */
	 download = function(idFile) {
		document.all.blankFrame.src = "administration/downloadMeetApplyReportAffix.action?affixId=" + idFile;
	}
});