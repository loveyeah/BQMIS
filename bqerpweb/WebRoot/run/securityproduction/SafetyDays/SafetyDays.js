Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定工作负责人为登录人
							writeByH.setValue(result.workerCode);
							Ext.get("writeBy").dom.value = result.workerName
									? result.workerName
									: '';
							Ext.get('writeByDept').dom.value = result.deptName
									? result.deptName
									: '';
						}
					}
				});
	}
	// 根据开始时间和结束时间计算安全天数
	function DateDiff(sDate1, sDate2) {
		// sDate1和sDate2是年-月-日格式
		var arrDate, objDate1, objDate2, intDays;
		arrDate = sDate1.split("-");
		objDate1 = new Date(arrDate[1] + '-' + arrDate[2] + '-' + arrDate[0]);// 转换为月-日-年格式
		arrDate = sDate2.split("-");
		objDate2 = new Date(arrDate[1] + '-' + arrDate[2] + '-' + arrDate[0]);
		intDays = parseInt(Math.abs(objDate1 - objDate2) / 1000 / 60 / 60 / 24); // 把相差的毫秒数转换为天数
		return intDays;
	}
	function getCurrentDate() {
		var myDate = new Date();

	var myMonth = myDate.getMonth() + 1;

	myMonth = (myMonth < 10 ? "0" + myMonth : myMonth);

	var myDay = myDate.getDate();

	myDay = (myDay < 10 ? "0" + myDay : myDay);
	return myDate.getFullYear() + "-" + myMonth + "-" + myDay;
	}

	// 记录id
	var safeId；
	// 记录人
	var workerCode;
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号
							workerCode = result.workerCode;
							recordBy.setValue(result.workerName);
							hiddrecordBy.setValue(workerCode);
						}
					}
				});
	}

	// 定义grid
	var MyRecord = Ext.data.Record.create([{
				name : 'safetyDaysRecord.recordId'
			}, {
				name : 'safetyDaysRecord.ifBreak'
			}, {
				name : 'safetyDaysRecord.safetyDays'
			}, {
				name : 'safetyDaysRecord.memo'
			}, {
				name : 'safetyDaysRecord.recordBy'
			}, {
				name : 'startDate'
			}, {
				name : 'endDate'
			}, {
				name : 'recordTime'
			}, {
				name : 'recorderName'
			}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'security/getSafetyDaysRecordList.action'
			});

	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, MyRecord);

	var store = new Ext.data.Store({
				proxy : dataProxy,
				reader : theReader
			});
	var sm = new Ext.grid.CheckboxSelectionModel();

	var grid = new Ext.grid.GridPanel({
				region : "center",
				layout : 'fit',
				store : store,
				columns : [new Ext.grid.RowNumberer(), {
							header : "",
							dataIndex : 'safetyDaysRecord.recordId',
							hidden : true
						}, {
							header : "安全记录开始日期",
							width : 75,
							sortable : true,
							align : 'center',
							dataIndex : 'startDate'
						}, {
							header : "安全记录结束日期",
							width : 75,
							sortable : true,
							align : 'center',
							dataIndex : 'endDate'
						}, {
							header : "安全天数",
							width : 50,
							sortable : true,
							align : 'center',
							dataIndex : 'safetyDaysRecord.safetyDays'
						}, {
							header : "备注",
							width : 100,
							sortable : true,
							align : 'center',
							dataIndex : 'safetyDaysRecord.memo'
						}, {
							header : "记录人",
							width : 75,
							sortable : true,
							align : 'center',
							dataIndex : 'recorderName'
						}, {
							header : "",
							dataIndex : 'safetyDaysRecord.recordBy',
							hidden : true
						}, {
							header : "记录日期",
							width : 75,
							sortable : true,
							align : 'center',
							dataIndex : 'recordTime'
						}],
				sm : sm,
				stripeRows : true,
				autoSizeColumns : true,
				autoScroll : true,
				enableColumnMove : false,
				viewConfig : {
					forceFit : true
				},

				tbar : [{
							text : "新增",
							iconCls : 'add',
							handler : function() {
								win.setTitle("新增安全天数");
								myaddpanel.getForm().reset();
								safeId = null;
								var count = store.getCount();
								if (count > 0) {
									var date = store.getAt(count-1).get("endDate");
									arrDate = date.split("-");
									var objDate= new Date(arrDate[1] + '-' + arrDate[2] + '-' + arrDate[0]);
									objDate.setDate(objDate.getDate() + 2);
									var temp = objDate.getYear()+"-"+((objDate.getMonth()+1)<10?("0"+(objDate.getMonth()+1)):(objDate.getMonth()+1))+
									"-"+(objDate.getDate()<10?("0"+objDate.getDate()):objDate.getDate());
									startDate.setValue(temp);
									endDate.setValue(temp);
								}
								win.show(); // 显示表单所在窗体
								safetyDays.setValue(DateDiff(endDate.getValue(),startDate.getValue())+1);
							}
						}, {
							text : "修改",
							iconCls : 'update',
							handler : updateRecord
						}],

				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : store,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						})
			});

	grid.on("rowdblclick", updateRecord);
	// ---------------------------------------

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				// layout : "fit",
				items : [grid]
			});

	// -------------------
	var wd = 200;
	
// function AddDays(date,value)
// {
// date.setDate(date.getDate()+value);
// }
	  var count = store.getCount();
			if (count > 0) {
				var tmp = store.getAt(count-1).get("endDate");
				AddDays(tmp,2);
				alert(tmp);
			}
	// 安全记录开始日期
	var startDate = new Ext.form.TextField({
		style : 'cursor:pointer',
		id : "startDate",
		name : 'safeDays.startDate',
		readOnly : true,
		width : wd,
		 allowBlank : false,
// value : tmp,
		fieldLabel : '安全记录开始日期',
		listeners : {
			focus : function() {
				WdatePicker({
							startDate : '%y-%M-%d',
							alwaysUseStartDate : false,
							dateFmt : 'yyyy-MM-dd',
							onpicked : function() {
								var count = store.getCount();
								if (count > 0) {
									var tmp = store.getAt(count-1).get("endDate");
									if (DateDiff(startDate.getValue(), tmp) < 2
											|| startDate.getValue() < tmp) {
										Ext.Msg.alert("提示", "2条记录之间的时间间隔必须大于2天!");
										startDate.setValue("");
										return;
									}
								}
								if (endDate.getValue() != "") {
									if (startDate.getValue() == ""
											|| startDate.getValue() > endDate
													.getValue()) {
										Ext.Msg.alert("提示", "必须小于安全记录结束日期");
										startDate.setValue("");
										return;
									}
									Ext.get("safetyDays").dom.value = DateDiff(
											startDate.getValue(), endDate
													.getValue())+1;
									startDate.clearInvalid();
								}
							},
							onclearing : function() {
								startDate.markInvalid();
							}
						});
			}
		}
	});
	// 安全记录结束日期
	var endDate = new Ext.form.TextField({
				style : 'cursor:pointer',
				id : "endDate",
				name : 'safeDays.endDate',
				readOnly : true,
				width : wd,
//				value:getCurrentDate() ,
				// allowBlank : false,
				fieldLabel : '安全记录结束日期',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									alwaysUseStartDate : false,
									dateFmt : 'yyyy-MM-dd',
									onpicked : function() {
										if (startDate.getValue() == ""
												|| startDate.getValue() > endDate
														.getValue()) {
											Ext.Msg.alert("提示", "必须大于安全记录开始日期");
											endDate.setValue("")
											return;
										}
										Ext.get("safetyDays").dom.value = DateDiff(
												startDate.getValue(), endDate
														.getValue())+1;
										endDate.clearInvalid();
									},
									onclearing : function() {
										endDate.markInvalid();
									}

								});
					}
				}
			})

	// 安全天数
	var safetyDays = new Ext.form.NumberField({
				id : "safetyDays",
				// xtype : "textfield",
				fieldLabel : '安全天数(天)',
				name : 'safeDays.safetyDays',
				readOnly : true,
				value : '',
				width : wd
			})
	// 备注
	var memo = new Ext.form.TextArea({
				id : "memo",
				fieldLabel : '备注',
//				allowBlank : false,
				width : wd,
				name : 'safeDays.memo'
			});
	// 记录人
	var recordBy = new Ext.form.TextField({
				fieldLabel : '记录人',
				id : "recordBy",
				// name : "recordBy",
				width : wd,
				readOnly : true
			});
	// offerBy.onTriggerClick = selectOfferByWin;
	var hiddrecordBy = new Ext.form.Hidden({
				id : "hiddrecordBy",
				name : 'safeDays.recordBy'
			})
	// 记录日期
	var recordTime = new Ext.form.TextField({
				id : "recordTime",
				fieldLabel : '记录日期',
				name : 'safeDays.recordTime',
				readOnly : true,
				width : wd,
				value : getCurrentDate()
			})

	var myaddpanel = new Ext.FormPanel({
				frame : true,
				labelAlign : 'center',
				title : '',
				items : [startDate, endDate, safetyDays, memo, recordBy,
						hiddrecordBy, memo, recordTime]

			});

	var win = new Ext.Window({
				width : 350,
				height : 300,
				buttonAlign : "center",
				items : [myaddpanel],
				layout : 'fit',
				resizable : false,
				modal : true,
				closeAction : 'hide',
				buttons : [{
							text : "保存",
							iconCls : 'save',
							handler : function() {
								myaddpanel.getForm().submit({
											method : 'POST',
											url : "security/addSafeDays.action",
											params : {
												safeId :safeId
											},
											success : function(form, action) {
													 var o = eval("(" +
												 action.response.responseText
												 + ")");
												 safeId = o.safeId;
												Ext.Msg.alert("注意", "保存成功!");
												store.load({
															params : {
																start : 0,
																limit : 18
															}
														});
												win.hide();
											},
											faliue : function() {
												Ext.Msg.alert('错误', '保存失败!');
											}
										});
							}
						}, {
							text : '中断',
							id : 'stop',
							iconCls : 'cancer',
							handler : function() {
								myaddpanel.getForm().submit({
											method : 'POST',
											url : "security/stopSafeDays.action",
											params : {
												safeId : safeId
											},
											success : function(form, action) {
												 var o = eval("(" +
												 action.response.responseText
												 + ")");
												 safeId = o.safeId;
												Ext.Msg.alert("注意", "中断成功!");
												store.load({
															params : {
																start : 0,
																limit : 18
															}
														});
												win.hide();
											},
											faliue : function() {
												Ext.Msg.alert('错误', '中断失败!');
											}
										});
							}
						}]

			});

	function updateRecord() {
		var rec = grid.getSelectionModel().getSelected();
		if (rec) {
			if (rec.get("safetyDaysRecord.ifBreak") == "N") {
				safeId = rec.get('safetyDaysRecord.recordId')
				win.show();
				safetyDays.setValue(rec.get('safetyDaysRecord.safetyDays'));
				memo.setValue(rec.get('safetyDaysRecord.memo'));
				myaddpanel.getForm().loadRecord(rec);
			} else {
				Ext.Msg.alert('提示信息', '此项记录已中断!');
			}
		} else {
			Ext.Msg.alert('提示信息', '请选择一条修改记录!');
		}
	}

	
	getWorkCode()
	store.load({
				params : {
					start : 0,
					limit : 18
				}
			});

});