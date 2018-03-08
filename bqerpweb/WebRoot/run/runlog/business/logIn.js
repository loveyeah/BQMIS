Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var runlogid;
	var runlogno;
	var specialcode;
	var takeworkercode;
	var weather = new Ext.data.Record.create([{
		name : 'weatherKeyId'
	}, {
		name : 'weatherName'
	}]);
	var weatherStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/findWeatherList.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, weather)
	});
	weatherStore.load();
	var weatherBox = new Ext.form.ComboBox({
		fieldLabel : '所属专业',
		store : weatherStore,
		valueField : "weatherKeyId",
		displayField : "weatherName",
		mode : 'local',
		triggerAction : 'all',
		forceSelection : true,
		hiddenName : 'weatherKeyId',
		editable : false,
		selectOnFocus : true,
		name : 'weatherKeyId',
		width : 100
	});
	Ext.Ajax.request({
		url : 'runlog/findRunLogByWorker.action',
		params : {
			method : 'DYZ'
		},
		method : 'post',
		waitMsg : '正在加载数据...',
		success : function(result, request) {
			var responseArray = Ext.util.JSON.decode(result.responseText);
			if (responseArray.success == true) {
				var tt = eval('(' + result.responseText + ')');
				var o = tt.data;
				runlogid = o[0];
				runlogno = o[1];
				specialcode = o[6];
				weatherBox.setValue(o[8]);
				var west_item = Ext.data.Record.create([{
					name : 'runLogid'
				}, {
					name : 'runLogno'
				}, {
					name : 'shiftId'
				}, {
					name : 'shiftName'
				}, {
					name : 'shiftTimeId'
				}, {
					name : 'shiftTimeName'
				}, {
					name : 'specialityCode'
				}, {
					name : 'specialityName'
				}, {
					name : 'weatherKeyId'
				}, {
					name : 'shiftCharge'
				}, {
					name : 'awayClassLeader'
				}, {
					name : 'takeClassLeader'
				}, {
					name : 'awayClassTime'
				}, {
					name : 'takeClassTime'
				}, {
					name : 'approveMan'
				}, {
					name : 'approveContent'
				}, {
					name : 'awayLeaderName'
				}, {
					name : 'weathername'
				}]);

				var west_ds = new Ext.data.Store({
					proxy : new Ext.data.HttpProxy({
						url : 'runlog/findLatestRunLogList.action'
					}),
					reader : new Ext.data.JsonReader({}, west_item)
				});
				var west_item_cm = new Ext.grid.ColumnModel([{
					// header : '运行日志号',
					// width:'10',
					dataIndex : 'runLogid',
					renderer : showCharger,
					align : 'center'
				}, {
					header : '运行日志号',
					width : '50',
					dataIndex : 'runLogno',
					align : 'left'
				}, {
					header : '单元或专业',
					width : '50',
					dataIndex : 'specialityName',
					align : 'center'
				}, {
					header : '值别名称',
					width : '50',
					dataIndex : 'shiftName',
					align : 'center'
				}, {
					header : '班次名称',
					width : '50',
					dataIndex : 'shiftTimeName',
					align : 'center'
				}, {
					header : '值班负责人',
					width : '50',
					dataIndex : 'awayLeaderName',
					align : 'center'
				}, {
					header : '接班时间',
					width : '50',
					dataIndex : 'takeClassTime',
					align : 'center'
				}, {
					header : '当班天气',
					width : '50',
					dataIndex : 'weathername',
					align : 'center'
				}, {
					header : '当前状态',
					width : '50',
					dataIndex : 'runLogno',
					renderer : showColor,
					align : 'center'
				}]);
				function showCharger(v) {
					if (v == runlogid) {
						return "<img src='comm/images/shift.gif'/>";;
					}
				}
				function showColor(v) {
					if (v > runlogno) {
						return "<div  style='width:40; background:blue'>&nbsp;&nbsp;&nbsp;&nbsp;</div>";
					} else if (v == runlogno) {
						return "<div  style='width:40; background:Green'>&nbsp;&nbsp;&nbsp;&nbsp;</div>";
					} else if (v < runlogno) {
						return "<div  style='width:40; background:Red'>&nbsp;&nbsp;&nbsp;&nbsp;</div>";
					}
				}
				west_ds.load();
					var reasontext = new Ext.form.TextArea({
					id : 'reason',
					name : 'reason',
					fieldLabel : '换班原因',
					readOnly : false,
					allowBlank : false,
					blankText : '不可为空！',
					width:350
				})
				var addwin = new Ext.Window({
					title : '请输入换班原因',
					el : 'win',
					autoHeight : true,
					width : 340,
					closeAction : 'hide',
					items : [reasontext],
					buttons : [{
						text : '确定',
						handler : function() {
							if(Ext.get('reason').dom.value == ""){
								Ext.Msg.alert('提示', '换班原因不可为空！');
							}
							else
							{
									addwin.hide();
									var url = '../../../comm/jsp/validate.jsp';
									var o = window
											.showModalDialog(url, '',
													'dialogWidth=350px;dialogHeight=200px;status=no');
									if (typeof(o) != "undefined") {
										takeworkercode = o.workerCode;
										Ext.Ajax.request({
											url : 'runlog/changeShiftCharger.action',
											params : {
												runlogid : runlogid,
												changeWorker : takeworkercode,
												reason : Ext.get('reason').dom.value
											},
											method : 'post',
											waitMsg : '正在加载数据...',
											success : function(result, request) {
												var responseArray = Ext.util.JSON
														.decode(result.responseText);
												if (responseArray.success == true) {
													Ext.MessageBox.alert('提示',
															responseArray.data,
															function() {
																parent.location = "/power"
															});

												} else {
													var o = eval('('
															+ result.responseText
															+ ')');
													Ext.MessageBox.show({
														title : '错误',
														msg : o.errorMsg,
														buttons : Ext.Msg.OK,
														icon : Ext.MessageBox.ERROR
													});
												}
											}
										})
									}
							}
						}
					}]
				})
				var tbar = new Ext.Toolbar({
					items : [{
						id : 'btnHandOver',
						text : "交接班",
						iconCls : 'add',
						handler : function() {
							Ext.Msg.confirm('提示', '确认进行交接班吗?', function(b) {
								if (b == 'yes') {
									var url = "../../../comm/jsp/validate.jsp";
									var o = window
											.showModalDialog(url, '',
													'dialogWidth=350px;dialogHeight=200px;status=no');
									if (typeof(o) != "undefined") {
										takeworkercode = o.workerCode;
										Ext.Ajax.request({
											url : 'runlog/shiftHandOver.action',
											params : {
												runlogid : runlogid,
												handWorker : takeworkercode
											},
											method : 'post',
											waitMsg : '正在加载数据...',
											success : function(result, request) {
												var responseArray = Ext.util.JSON
														.decode(result.responseText);
												if (responseArray.success == true) {
													Ext.MessageBox.alert('提示',
															responseArray.data);
													Ext.MessageBox.alert('提示',
															responseArray.data,
															function() {
																parent.location = "/power"
															});
												} else {
													var o = eval('('
															+ result.responseText
															+ ')');
													Ext.MessageBox.show({
														title : '错误',
														msg : o.errorMsg,
														buttons : Ext.Msg.OK,
														icon : Ext.MessageBox.ERROR
													});
												}
											}
										})
									}

								}
							})
						}
					}, '-', {
						id : 'btnChange',
						text : "换班",
						iconCls : 'update',
						handler : function() {
							Ext.Msg.confirm('提示', '确认让其他人接替您的日志权限吗?', function(
									b) {
								if (b == 'yes') {
								addwin.show();
									Ext.get('reason').dom.value="";
								}
							})
						}
					}, '-', '值班天气:', weatherBox, '-', {
						id : 'btnSave',
						text : "保存",
						iconCls : 'save',
						handler : function() {
							Ext.Ajax.request({
								url : 'runlog/saveWeather.action',
								params : {
									runlogid : runlogid,
									weatherKeyId : weatherBox.value
								},
								method : 'post',
								waitMsg : '正在加载数据...',
								success : function(result, request) {
									var responseArray = Ext.util.JSON
											.decode(result.responseText);
									if (responseArray.success == true) {
										west_ds.load();
										Ext.MessageBox.alert('提示',
												responseArray.data);
									} else {
										var o = eval('(' + result.responseText
												+ ')');
										Ext.MessageBox.show({
											title : '错误',
											msg : o.errorMsg,
											buttons : Ext.Msg.OK,
											icon : Ext.MessageBox.ERROR
										});
									}
								}
							})
						}
					}, '-', {
						id : 'btnReflesh',
						text : "刷新",
						iconCls : 'reflesh',
						handler : function() {
							west_ds.reload();
						}
					}]
				});
				var westGrid = new Ext.grid.GridPanel({
					title : '基本信息',
					ds : west_ds,
					cm : west_item_cm,
					collapsible : true,
					border : true,
					animCollapse : false,
					split : true,
					tbar : tbar,
					viewConfig : {
						forceFit : true
					}
				});
				// var panel1 = new Ext.FormPanel({
				// id : 'tab1',
				// layout:'fit',
				// title : '基本信息',
				// items : [westGrid]
				// });
				var _url2 = "run/runlog/business/logrecord/shiftrecord.jsp?runlogId="
						+ runlogid;
				var _url3 = "run/runlog/business/worker/worker.jsp?runlogId="
						+ runlogid;
				var _url4 = "run/runlog/business/shiftequ/shift.jsp?runlogId="
						+ runlogid + "&specialcode=" + specialcode;
				var _url5 = "run/runlog/maint/shiftparm/runshiftparm.jsp?runlogId="
						+ runlogid;
				var _url6 = "run/runlog/business/logrecord/notcompletion.jsp?runlogId="
						+ runlogid;
				var _url7="productiontec/dependabilityAnalysis/assistRecordRegister/assistRecordRegister.jsp";		
				var tabpanel = new Ext.TabPanel({
					activeTab : 1,
					//tabPosition : 'bottom',
					border : false,
					// autoScroll : true,
					items : [{
						id : 'tab1',
						title : '基本信息',
						layout : 'border',
						items : [{
							region : "center",
							layout : 'fit',
							border : false,
							items : [westGrid]
						}, {
							region : "south",
							height : 20,
							layout : 'fit',
							border : false,
							collapsible : false,
							split : false,
							html : "<div style='font-size:10pt'>值班状态说明：<span style='width:40; background:Green'>&nbsp;&nbsp;&nbsp;&nbsp;</span> 正在值班 <span style='width:40;background:Red'>&nbsp;&nbsp;&nbsp;&nbsp;</span> 还未交班 <span style='width:40;background:blue'>&nbsp;&nbsp;&nbsp;&nbsp;</span> 已经交班 <span style='width:40;background:black'>&nbsp;&nbsp;&nbsp;&nbsp;</span> 没有记录</div>"
						}]
					}, {
						id : 'tab2',
						title : '值班记事',
						html : '<iframe  src="'
								+ _url2
								+ '"  frameborder="0"  width="100%" height="100%" />'
					}, {
						id : 'tab3',
						title : '值班人员',
						html : '<iframe  src="'
								+ _url3
								+ '"  frameborder="0"  width="100%" height="100%" />'
					}, {
						id : 'tab4',
						title : '交接班方式',
						html : '<iframe  src="'
								+ _url4
								+ '"  frameborder="0"  width="100%" height="100%" />'
					}, {
						id : 'tab5',
						title : '交接班参数',
						html : '<iframe  src="'
								+ _url5
								+ '"  frameborder="0"  width="100%" height="100%" />'
					}, {
						id : 'tab6',
						title : '未完成项',
						html : '<iframe  src="'
								+ _url6
								+ '"  frameborder="0"  width="100%" height="100%" />'
					},{
						id : 'tab7',
						title : '辅机事件',
						html : '<iframe  src="'
								+ _url7
								+ '"  frameborder="0"  width="100%" height="100%" />'
					}]

				});

				new Ext.Viewport({
					enableTabScroll : true,
					collapsible : true,
					split : true,
					margins : '0 0 0 0',
					layout : "fit",
					items : [tabpanel]
				});
				// var layout = new Ext.Viewport({
				// layout : "border",
				// border : false,
				// items : [{
				// region : "center",
				// layout : 'fit',
				// collapsible : true,
				// split : true,
				// margins : '0 0 0 0',
				// title : '值班信息',
				// // 注入表格
				// items : [westGrid]
				//
				// }, {
				// region : "south",
				// height : 50,
				// layout : 'fit',
				// collapsible : false,
				// split : false,
				// margins : '0 0 0 0',
				// //title : '值班信息',
				// // 注入表格
				// html : "<div style='font-size:10pt'>值班状态说明：<span
				// style='width:40;
				// background:Green'>&nbsp;&nbsp;&nbsp;&nbsp;</span> 正在值班 <span
				// style='width:40;
				// background:Red'>&nbsp;&nbsp;&nbsp;&nbsp;</span> 还未交班 <span
				// style='width:40;
				// background:blue'>&nbsp;&nbsp;&nbsp;&nbsp;</span> 已经交班 <span
				// style='width:40;
				// background:black'>&nbsp;&nbsp;&nbsp;&nbsp;</span> 没有记录</div>"
				//
				// }]
				// })

			} else {
				var o = eval('(' + result.responseText + ')');
				Ext.MessageBox.show({
					title : '错误',
					msg : o.errorMsg,
					buttons : Ext.Msg.OK,
					icon : Ext.MessageBox.ERROR
				});
			}
		},
		failure : function(result, request) {
			Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
		}
	});
})
