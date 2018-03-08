Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() { 
	var args = window.dialogArguments; 
	var dateType = args.dateType; 
	var itemCode = args.itemCode; 
	var itemName=args.itemName;
	function idByType(type) {
		if (type == 1) {
			return "time"
		} else if (type == 3) {
			return "day"
		} else if (type == 4) {
			return "month"
		} else if (type == 5) {
			return "quarter"
		} else if (type == 6) {
			return "yearDate"
		} else{
			alert("时间点类型异常！")
		}
	}
	
	function generateDateType() {
		if (dateType == "6") {
			yearPanel.show();
			quarterPanel.hide();
			monthPanel.hide();
			dayPanel.hide();
			timePanel.hide();
		} else if (dateType == "5") {
			yearPanel.hide();
			quarterPanel.show();
			monthPanel.hide();
			dayPanel.hide();
			timePanel.hide();
			generateQuarterCheckBox();
		} else if (dateType == "4") {
			yearPanel.hide();
			quarterPanel.hide();
			monthPanel.show();
			dayPanel.hide();
			timePanel.hide();
			generateMonthCheckBox();
		} else if (dateType == "3") {
			yearPanel.hide();
			quarterPanel.hide();
			monthPanel.hide();
			dayPanel.show();
			timePanel.hide();
			generateDayCheckBox();
		} else if (dateType == "1") {
			yearPanel.hide();
			quarterPanel.hide();
			monthPanel.hide();
			dayPanel.hide();
			timePanel.show();
			generateTimeCheckBox();
		}
	}

	function init() {
		generateDateType();
		Ext.Ajax.request({
					url : 'manager/queryDateType.action',
					method : 'post',
					params : {
						itemCode : itemCode
					},
					success : function(result, request) {
						var o = eval('(' + result.responseText + ')');
						for (var i = 0, j = 1; i < o.length; j++, i++) {
							verId = idByType(o[i].dataTimeType);
							if (o[i].ifCollect == "1") {
								day = verId + j;
								// alert(day);
								Ext.get(day).dom.checked = true;
								
							} else if (o[i].ifCollect == "0") {
								day = verId + j;
								Ext.get(day).dom.checked = false;
							}
							if(o[0].ifAutoSetup == "1") {
								Ext.get(day).dom.disabled = true;
								Ext.get('selectAll').dom.checked = true;
							} else {
								Ext.get(day).dom.disabled = false;
								Ext.get('selectPart').dom.checked = true;
							}
						}
						
					},
					failure : function(result, request) {
						Ext.MessageBox.alert('提示信息', '未知错误！')
					}
				})
	}

	function save() {
		var selectAll = Ext.get('selectAll').dom.checked;
		if (!selectAll) {
			var updateData = new Array();
			if (dateType == "1") {
				for (var i = 1; i < 25; i++) {
					var data = [];
					time = "time" + i;
					timeValue = Ext.get(time).dom.checked;
					data.push(time);
					data.push(timeValue);
					updateData.push(data);
				}
			} else if (dateType == "3") {
				for (var i = 1; i < 32; i++) {
					var data = [];
					day = "day" + i;
					dayValue = Ext.get(day).dom.checked;
					data.push(day);
					data.push(dayValue);
					updateData.push(data);
				}
			} else if (dateType == "4") {
				for (var i = 1; i < 13; i++) {
					var data = [];
					month = "month" + i;
					monthValue = Ext.get(month).dom.checked;
					data.push(month);
					data.push(monthValue);
					updateData.push(data);
				}
			} else if (dateType == "5") {
				for (var i = 1; i < 5; i++) {
					var data = [];
					quarter = "quarter" + i;
					quarterValue = Ext.get(quarter).dom.checked;
					data.push(quarter);
					data.push(quarterValue);
					updateData.push(data);
				}
			} else if (dateType == "6") {
				var data = [];
				yearValue = Ext.get('yearDate1').dom.checked ;
				data.push("yearDate1");
				data.push(yearValue);
				updateData.push(data);
			}
		}
		Ext.Ajax.request({
			url : 'manager/saveDateType.action',
			method : 'post',
			params : {
				saveDatail : Ext.util.JSON.encode(updateData),
				itemCode : itemCode,
				dateType : dateType,
				selectAll : selectAll
			},
			success : function(result, request) {
				var o = eval('(' + result.responseText + ')');
				Ext.MessageBox.alert('提示信息', o.msg);
			},
			failure : function(result, request) {
				Ext.MessageBox.alert('提示信息', '未知错误！')
			}
		})
	}

	function cancel() {
		window.close();
	}

	var selectAll = new Ext.form.Radio({
				boxLabel : '所有时间点',
				hideLabel : true,
				id : 'selectAll',
				// checked : true,
				name : 'myactionId',
				listeners : {
					check : function(radio, checked) {
						if (checked) {
							if (dateType == "1") {
								for (var i = 1; i < 25; i++) {
									time = "time" + i;
									Ext.get(time).dom.checked = true;
									Ext.get(time).dom.disabled = true;
								}
							} else if (dateType == "3") {
								for (var i = 1; i < 32; i++) {
									day = "day" + i;
									Ext.get(day).dom.checked = true;
									Ext.get(day).dom.disabled = true;
								}
							} else if (dateType == "4") {
								for (var i = 1; i < 13; i++) {
									month = "month" + i;
									Ext.get(month).dom.checked = true;
									Ext.get(month).dom.disabled = true;
								}
							} else if (dateType == "5") {
								for (var i = 1; i < 5; i++) {
									quarter = "quarter" + i;
									Ext.get(quarter).dom.checked = true;
									Ext.get(quarter).dom.disabled = true;
								}
							} else if (dateType == "6") {
								Ext.get('yearDate1').dom.checked = true;
								Ext.get('yearDate1').dom.disabled = true;
							}
						}
					}
				}
			});

	// selectAll.on('click', function (){
	// alert();
	// if(selectAll.checked) {
	//			
	// Ext.get('selectPart') = false;
	// } else {
	// Ext.get('selectAll').checked = false;
	// Ext.get('selectPart').checked = true;
	// }
	// });

	var saveButton = new Ext.Toolbar.Button({
				text : "保存",
				iconCls : 'save',
				handler : save
			})

	var cancelButton = new Ext.Toolbar.Button({
				text : "关闭",
				iconCls : 'cancer',
				handler : cancel
			})

	var selectPart = new Ext.form.Radio({
				boxLabel : '任选时间点',
				hideLabel : true,
				id : 'selectPart',
				name : 'myactionId',
				// checked : true,
				listeners : {
					check : function(radio, checked) {
						if (checked) {
							if (dateType == "1") {
								for (var i = 1; i < 25; i++) {
									time = "time" + i;
									Ext.get(time).dom.disabled = false;
								}
							} else if (dateType == "3") {
								for (var i = 1; i < 32; i++) {
									day = "day" + i;
									Ext.get(day).dom.disabled = false;
								}
							} else if (dateType == "4") {
								for (var i = 1; i < 13; i++) {
									month = "month" + i;
									Ext.get(month).dom.disabled = false;
								}
							} else if (dateType == "5") {
								for (var i = 1; i < 5; i++) {
									quarter = "quarter" + i;
									Ext.get(quarter).dom.disabled = false;
								}
							} else if (dateType == "6") {
								Ext.get('yearDate1').dom.disabled = false;
							}
						}
					}
				}
			});

	// selectPart.on('click', function (){
	// alert();
	// if(selectPart.checked) {
	// Ext.get('selectAll').checked = false;
	// } else {
	// Ext.get('selectAll').checked = true;
	// Ext.get('selectPart').checked = false;
	// }
	// });

	var firstLine = new Ext.Panel({
		border : false,
		height : 30,
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [selectAll]
				}, {
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [saveButton]
				}]
	});

	var secondLine = new Ext.Panel({
		border : false,
		height : 30,
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [selectPart]
				}, {
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [cancelButton]
				}]
	});

	var tableField = new Ext.form.FieldSet({
				autoWidth : false,
				autoHeight : true,
				border : true,
				title : itemName,
				layout : "column",
				anchor : '100%',
				//style : "padding-top:10;",
				items : [{
							border : false,
							layout : "column",
							columnWidth : 1,
							items : [firstLine]
						}, {
							border : false,
							layout : "column",
							columnWidth : 1,
							items : [secondLine]
						}]
			});

	var yearDate = new Ext.form.Checkbox({
				boxLabel : '一年',
				id : 'yearDate1',
				hideLabel : true,
				checked : true,
				disabled : true
			})

	var yearPanel = new Ext.Panel({
				id : 'yearPanel',
				// hidden : true,
				border : false,
				layout : 'form',
				labelWidth : 32,
				items : [yearDate]
			});

	// var quarterDateOne = new Ext.form.Checkbox({
	// boxLabel : '一季',
	// checked : true,
	// hideLabel : true
	//
	// })
	//	
	// var quarterDateTwo = new Ext.form.Checkbox({
	// boxLabel : '二季',
	// checked : true,
	// hideLabel : true
	//
	// })
	//	
	// var quarterDateThree = new Ext.form.Checkbox({
	// boxLabel : '三季',
	// checked : true,
	// hideLabel : true
	//
	// })
	//	
	// var quarterDateFour = new Ext.form.Checkbox({
	// boxLabel : '四季',
	// checked : true,
	// hideLabel : true
	//
	// })

	function generateQuarterCheckBox() {
		for (var i = 1; i < 5; i++) {
			// 添加radio
			var label = i + "季";
			quarterPanel.items.add(new Ext.form.Hidden());
			quarterPanel.items.add(new Ext.form.Checkbox({
						boxLabel : label,
						id : 'quarter' + i,
						name : 'actionId',
						inputValue : '1',
						checked : true,
						disabled : true
					}));
			quarterPanel.items.add(new Ext.form.Hidden());
		}
		quarterPanel.doLayout();
	}

	var quarterPanel = new Ext.Panel({
		id : 'quarterPanel',
		labelWidth : 32,
		layout : 'column',
		border : false
			// hidden : true,
			// items : [{
			// layout : 'column',
			// border : false,
			// items : [{
			// border : false,
			// columnWidth : 0.3,
			// align : 'center',
			// layout : 'form',
			// items : [quarterDateOne]
			// },{
			// border : false,
			// columnWidth : 0.3,
			// align : 'center',
			// layout : 'form',
			// items : [quarterDateTwo]
			// },{
			// border : false,
			// columnWidth : 0.3,
			// align : 'center',
			// layout : 'form',
			// items : [quarterDateThree]
			// }]
			//					
			// },{border : false,
			// columnWidth : 1,
			// align : 'center',
			// layout : 'form',
			// items : [quarterDateFour]
			// }
			// ]
		});

	function generateDayCheckBox() {
		for (var i = 1; i < 32; i++) {
			// 添加radio
			if (i < 10) {
				var label = "&nbsp;&nbsp;" + i + "号";
				dayPanel.items.add(new Ext.form.Hidden());
				dayPanel.items.add(new Ext.form.Checkbox({
							boxLabel : label,
							id : 'day' + i,
							name : 'actionId',
							inputValue : '1',
							// checked :true,
							disabled : true,
							listeners : {
								check : function(radio, checked) {
									if (checked) {
									}
								}
							}

						}));
				dayPanel.items.add(new Ext.form.Hidden());
			} else {
				dayPanel.items.add(new Ext.form.Hidden());
				dayPanel.items.add(new Ext.form.Checkbox({
							boxLabel : i + "号",
							id : 'day' + i,
							name : 'actionId',
							inputValue : '1',
							checked : true,
							disabled : true,
							listeners : {
								check : function(radio, checked) {
									if (checked) {
									}
								}
							}

						}));
				dayPanel.items.add(new Ext.form.Hidden());
			}
		}
		dayPanel.doLayout();
	}

	// var January = new Ext.form.Checkbox({
	// boxLabel : '一月',
	// checked : true,
	// hideLabel : true
	// })
	//	
	// var February = new Ext.form.Checkbox({
	// boxLabel : '二月',
	// checked : true,
	// hideLabel : true
	// })
	// var March = new Ext.form.Checkbox({
	// boxLabel : '三月',
	// checked : true,
	// hideLabel : true
	// })
	// var April = new Ext.form.Checkbox({
	// boxLabel : '四月',
	// checked : true,
	// hideLabel : true
	// })
	// var May = new Ext.form.Checkbox({
	// boxLabel : '五月',
	// checked : true,
	// hideLabel : true
	// })
	// var June = new Ext.form.Checkbox({
	// boxLabel : '六月',
	// checked : true,
	// hideLabel : true
	// })
	// var July = new Ext.form.Checkbox({
	// boxLabel : '七月',
	// checked : true,
	// hideLabel : true
	// })
	// var August = new Ext.form.Checkbox({
	// boxLabel : '八月',
	// checked : true,
	// hideLabel : true
	// })
	// var September = new Ext.form.Checkbox({
	// boxLabel : '九月',
	// checked : true,
	// hideLabel : true
	// })
	// var October = new Ext.form.Checkbox({
	// boxLabel : '十月',
	// checked : true,
	// hideLabel : true
	// })
	// var November = new Ext.form.Checkbox({
	// boxLabel : '十一月',
	// checked : true,
	// hideLabel : true
	// })
	// var December = new Ext.form.Checkbox({
	// boxLabel : '十二月',
	// checked : true,
	// hideLabel : true
	// })

	function generateMonthCheckBox() {
		for (var i = 1; i < 13; i++) {
			// 添加radio
			if (i < 10) {
				var label = "&nbsp;&nbsp;" + i + "月";
				monthPanel.items.add(new Ext.form.Hidden());
				monthPanel.items.add(new Ext.form.Checkbox({
							boxLabel : label,
							id : 'month' + i,
							name : 'actionId',
							inputValue : '1',
							checked : true,
							disabled : true,
							listeners : {
								check : function(radio, checked) {
									if (checked) {
									}
								}
							}

						}));
				monthPanel.items.add(new Ext.form.Hidden());
			} else {
				monthPanel.items.add(new Ext.form.Hidden());
				monthPanel.items.add(new Ext.form.Checkbox({
							boxLabel : i + "月",
							id : 'month' + i,
							name : 'actionId',
							inputValue : '1',
							checked : true,
							disabled : true,
							listeners : {
								check : function(radio, checked) {
									if (checked) {
									}
								}
							}

						}));
				monthPanel.items.add(new Ext.form.Hidden());
			}
		}
		monthPanel.doLayout();
	}

	var monthPanel = new Ext.Panel({
		id : 'monthPanel',
		labelWidth : 32,
		border : false,
		layout : 'column'
			// hidden : true,
			// layout : 'form',
			// items : [{
			// layout : 'column',
			// border : false,
			// items : [{
			// border : false,
			// columnWidth : 0.3,
			// align : 'center',
			// layout : 'form',
			// items : [January]
			// },{
			// border : false,
			// columnWidth : 0.3,
			// align : 'center',
			// layout : 'form',
			// items : [February]
			// },{
			// border : false,
			// columnWidth : 0.3,
			// align : 'center',
			// layout : 'form',
			// items : [March]
			// }]
			//					
			// },{
			// layout : 'column',
			// border : false,
			// items : [{
			// border : false,
			// columnWidth : 0.3,
			// align : 'center',
			// layout : 'form',
			// items : [April]
			// },{
			// border : false,
			// columnWidth : 0.3,
			// align : 'center',
			// layout : 'form',
			// items : [May]
			// },{
			// border : false,
			// columnWidth : 0.3,
			// align : 'center',
			// layout : 'form',
			// items : [June]
			// }]
			//					
			// },{
			// layout : 'column',
			// border : false,
			// items : [{
			// border : false,
			// columnWidth : 0.3,
			// align : 'center',
			// layout : 'form',
			// items : [July]
			// },{
			// border : false,
			// columnWidth : 0.3,
			// align : 'center',
			// layout : 'form',
			// items : [August]
			// },{
			// border : false,
			// columnWidth : 0.3,
			// align : 'center',
			// layout : 'form',
			// items : [September]
			// }]
			//					
			// },{
			// layout : 'column',
			// border : false,
			// items : [{
			// border : false,
			// columnWidth : 0.3,
			// align : 'center',
			// layout : 'form',
			// items : [October]
			// },{
			// border : false,
			// columnWidth : 0.3,
			// align : 'center',
			// layout : 'form',
			// items : [November]
			// },{
			// border : false,
			// columnWidth : 0.3,
			// align : 'center',
			// layout : 'form',
			// items : [December]
			// }]
			//					
			// }]
		});

	// var January = new Ext.form.Checkbox({
	// boxLabel : '一号',
	// checked : true,
	// hideLabel : true
	// })
	//	
	// var February = new Ext.form.Checkbox({
	// boxLabel : '二号',
	// checked : true,
	// hideLabel : true
	// })
	// var March = new Ext.form.Checkbox({
	// boxLabel : '三号',
	// checked : true,
	// hideLabel : true
	// })
	// var April = new Ext.form.Checkbox({
	// boxLabel : '四号',
	// checked : true,
	// hideLabel : true
	// })
	// var May = new Ext.form.Checkbox({
	// boxLabel : '五号',
	// checked : true,
	// hideLabel : true
	// })
	// var June = new Ext.form.Checkbox({
	// boxLabel : '六号',
	// checked : true,
	// hideLabel : true
	// })
	// var July = new Ext.form.Checkbox({
	// boxLabel : '七号',
	// checked : true,
	// hideLabel : true
	// })
	// var August = new Ext.form.Checkbox({
	// boxLabel : '八号',
	// checked : true,
	// hideLabel : true
	// })
	// var September = new Ext.form.Checkbox({
	// boxLabel : '九号',
	// checked : true,
	// hideLabel : true
	// })
	// var October = new Ext.form.Checkbox({
	// boxLabel : '十号',
	// checked : true,
	// hideLabel : true
	// })
	// var November = new Ext.form.Checkbox({
	// boxLabel : '十一号',
	// checked : true,
	// hideLabel : true
	// })
	// var December = new Ext.form.Checkbox({
	// boxLabel : '十二号',
	// checked : true,
	// hideLabel : true
	// })
	//	
	// var January = new Ext.form.Checkbox({
	// boxLabel : '十三号',
	// checked : true,
	// hideLabel : true
	// })
	//	
	// var February = new Ext.form.Checkbox({
	// boxLabel : '十四号',
	// checked : true,
	// hideLabel : true
	// })
	// var March = new Ext.form.Checkbox({
	// boxLabel : '十五号',
	// checked : true,
	// hideLabel : true
	// })
	// var April = new Ext.form.Checkbox({
	// boxLabel : '十六号',
	// checked : true,
	// hideLabel : true
	// })
	// var May = new Ext.form.Checkbox({
	// boxLabel : '十七号',
	// checked : true,
	// hideLabel : true
	// })
	// var June = new Ext.form.Checkbox({
	// boxLabel : '十八号',
	// checked : true,
	// hideLabel : true
	// })
	// var July = new Ext.form.Checkbox({
	// boxLabel : '十九号',
	// checked : true,
	// hideLabel : true
	// })
	// var August = new Ext.form.Checkbox({
	// boxLabel : '二十号',
	// checked : true,
	// hideLabel : true
	// })
	// var September = new Ext.form.Checkbox({
	// boxLabel : '二十一号',
	// checked : true,
	// hideLabel : true
	// })
	// var October = new Ext.form.Checkbox({
	// boxLabel : '二十二号',
	// checked : true,
	// hideLabel : true
	// })
	//	
	// var November = new Ext.form.Checkbox({
	// boxLabel : '二十三号',
	// checked : true,
	// hideLabel : true
	// })
	// var December = new Ext.form.Checkbox({
	// boxLabel : '二十四号',
	// checked : true,
	// hideLabel : true
	// })
	// var January = new Ext.form.Checkbox({
	// boxLabel : '二十五号',
	// checked : true,
	// hideLabel : true
	// })
	//	
	// var February = new Ext.form.Checkbox({
	// boxLabel : '二十六号',
	// checked : true,
	// hideLabel : true
	// })
	// var March = new Ext.form.Checkbox({
	// boxLabel : '二十七号',
	// checked : true,
	// hideLabel : true
	// })
	// var April = new Ext.form.Checkbox({
	// boxLabel : '二十八号',
	// checked : true,
	// hideLabel : true
	// })
	// var May = new Ext.form.Checkbox({
	// boxLabel : '二十九号',
	// checked : true,
	// hideLabel : true
	// })
	// var June = new Ext.form.Checkbox({
	// boxLabel : '六月',
	// checked : true,
	// hideLabel : true
	// })
	// var July = new Ext.form.Checkbox({
	// boxLabel : '七月',
	// checked : true,
	// hideLabel : true
	// })
	// var August = new Ext.form.Checkbox({
	// boxLabel : '八月',
	// checked : true,
	// hideLabel : true
	// })
	// var September = new Ext.form.Checkbox({
	// boxLabel : '九月',
	// checked : true,
	// hideLabel : true
	// })
	// var October = new Ext.form.Checkbox({
	// boxLabel : '十月',
	// checked : true,
	// hideLabel : true
	// })
	// var November = new Ext.form.Checkbox({
	// boxLabel : '十一月',
	// checked : true,
	// hideLabel : true
	// })
	// var December = new Ext.form.Checkbox({
	// boxLabel : '十二月',
	// checked : true,
	// hideLabel : true
	// })

	var dayPanel = new Ext.Panel({
		id : 'dayPanel',
		labelWidth : 32,
		border : false,
		// hidden : true,
		layout : "column"
			// layout : 'form',
			// items : [dayDate]
		});

	function generateTimeCheckBox() {
		for (var i = 1; i < 25; i++) {
			// 添加radio
			if (i < 10) {
				var label = "&nbsp;&nbsp;" + i + "点";
				timePanel.items.add(new Ext.form.Checkbox({
							boxLabel : label,
							name : 'actionId',
							id : 'time' + i,
							inputValue : '1',
							disabled : true,
							checked : true
						}));
			} else {
				timePanel.items.add(new Ext.form.Checkbox({
							boxLabel : i + "点",
							name : 'actionId',
							id : 'time' + i,
							inputValue : '1',
							disabled : true,
							checked : true
						}));
			}
		}
		timePanel.doLayout();
	}

	var timePanel = new Ext.Panel({
		id : 'timePanel',
		labelWidth : 32,
		border : false,
		anchor : '100%',
		layout : 'column'

			// hidden : true
			// layout : 'form',
			// items : [timeDate]
		});

	var timeSelectField = new Ext.form.FieldSet({
				autoWidth : false,
				autoHeight : true,
				border : true,
				title : '时间点选择',// ,dayPanel,timePanel
				layout : "column",
				anchor : '100%',
				//style : "padding-top:10;",
				items : [yearPanel, quarterPanel, monthPanel, dayPanel,
						timePanel]
			});

	var formPanel = new Ext.FormPanel({
		labelAlign : 'right',
		labelPad : 15,
		border : false,
		height : 350,
		// autoHeight : true,
		// region : 'fit',
		items : [{
			border : false,
			style : "padding-top:5;padding-bottom:5;padding-right:20;padding-left:20;",
			items : [tableField]
		}, {
			border : false,
			style : "padding-top:5;padding-bottom:5;padding-right:20;padding-left:20;",
			items : [timeSelectField]
		}]
	});

	var queryPanel = new Ext.Panel({
				layout : 'fit',
				frame : false,
				border : false,
				items : [formPanel]
			});

	var layout = new Ext.Viewport({
				layout : 'fit',
				margins : '0 0 0 0',
				border : false,
				items : [queryPanel]
			});

	init();
	
});