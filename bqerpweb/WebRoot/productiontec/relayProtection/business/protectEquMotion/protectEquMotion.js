Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	
	// 系统当前时间
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";

		return s;
	}
	// 从session取登录人编码姓名部门相关信息
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号，赋给全局变量
							fillName.setValue(result.workerName);
							fillBy.setValue(result.workerCode);
						}
					}
				});
	}
	
	
	// 保护装置名称
	var fuzzyText = new Ext.form.TextField({
		id : 'fuzzyText',
		name : 'fuzzyText',
		width : 100,
		emptyText : '（保护装置名称）'
	})		
	var fromDate = new Ext.form.TextField({
		id : 'fromDate',
		name : 'fromDate',
		fieldLabel : "开始",
		style : 'cursor:pointer',
		cls : 'Wdate',
		width : 85,
//		value : sdate,
		listeners : {
			focus : function() {
				WdatePicker({
//					startDate : '%y-%M-01',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true
				});
			}
		}
	});
	var toDate = new Ext.form.TextField({
		name : 'toDate',
//		value : edate,
		id : 'timetoDate',
		fieldLabel : "结束",
		style : 'cursor:pointer',
		cls : 'Wdate',
		width : 85,
		listeners : {
			focus : function() {
				WdatePicker({
//					startDate : '%y-%M-01',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true
				});
			}
		}
	});

	var queryButton = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : function() {
					if(fromDate.getValue() != null && fromDate.getValue() != ""
						&& toDate.getValue() != null && toDate.getValue() != "")
					{
						if(fromDate.getValue() > toDate.getValue())	
						{
							Ext.Msg.alert('提示信息','查询动作时间中开始时间大于结束时间！');
							return;
						}
					}
					recordQuery();
//					westgrids.load({
//								params : {
//									name : fuzzyText.getValue(),
//									fromTime : fromDate.getValue(),
//									toTime : toDate.getValue(),
//									start : 0,
//									limit : 18
//								}
//							});
				}
			});
	
	// 保护装置动作编号
	var bhzzdzId = new Ext.form.Hidden({
		id : 'bhzzdzId',
		name : 'pjj.bhzzdzId'
	})
	// 装置编号
	var deviceId = new Ext.form.Hidden({
		id : 'deviceId',
		name : 'pjj.deviceId'
	})
	// 装置名称
	var deviceName = new Ext.form.TriggerField({
				fieldLabel : '装置名称',
				width : 402,
				id : "deviceName",
				hiddenName : 'deviceName',
				blankText : '请选择',
				emptyText : '请选择',
				maxLength : 100,
				readOnly : true
			});
	deviceName.onTriggerClick = deviceSelect;

	/**
	 * 设备选择画面处理
	 */
	function deviceSelect() {
		var args = {
			selectModel : 'signal',
			notIn : "",
			rootNode : {
				id : '-1',
				text : '合肥电厂'
			}
		}
		var url = "../deviceSelect.jsp?";
		var equ = window.showModalDialog(url, window,
				'dialogWidth=600px;dialogHeight=400px;status=no');
		if (typeof(equ) != "undefined") {
			deviceName.setValue(equ.deviceName);
			deviceId.setValue(equ.deviceId);
		}
	}
	// 动作时间
	var actDate =  new Ext.form.DateField({
				fieldLabel : '动作时间',
				id : 'actDate',
				name : 'pjj.actDate',
				width : 145,
				format : 'Y-m-d',
				allowBlank : false
			})
	//责任部门
	var chargeDep = new Ext.form.TextField({
		fieldLabel : "责任部门",
		name : 'chargeDep',
		emptyText : '请选择...',
		width : 145,
		listeners : {
			focus : function() {
				var args = {
					selectModel : 'single',
					rootNode : {
						id : "0",
						text : '合肥电厂'
					}
				}
				var url = "/power/comm/jsp/hr/dept/dept.jsp";
				var rvo = window
						.showModalDialog(
								url,
								args,
								'dialogWidth:500px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;')
				if (typeof(rvo)!= "undefined") {
					chargeDepH.setValue(rvo.codes);
					chargeDep.setValue(rvo.names);

				}
				this.blur();
			}
		},
		readOnly : true,
		allowBlank : true
	})
	// 部门编码
	var chargeDepH = new Ext.form.Hidden({
				name : 'pjj.chargeDep'

			})


	// 动作评价stroe
	var actAppaiseStore = new Ext.data.SimpleStore({
				fields : ['id', 'name'],
				data : [['', ''], ['1', '正确'], ['2', '误动'],
						['3', '拒动']]
			});
	// 动作评价
	var actAppaise = new Ext.form.ComboBox({
				store : actAppaiseStore,
				id : "actAppaise",
				displayField : "name",
				valueField : "id",
				mode : 'local',
				width : 145,
				fieldLabel : "动作评价",
				triggerAction : 'all',
				readOnly : true,
				hiddenName : 'pjj.actAppaise'
			})
	// 动作次数
	var actNum = new Ext.form.NumberField({
		id : 'actNum',
		name : 'pjj.actNum',
		fieldLabel : '动作次数',
		width : 145,
		allowDecimals : false,
		allowNegative : false,
		allowBlank : true
	}) 
	// 录波次数
	var waveNumber = new Ext.form.NumberField({
		id : 'waveNumber',
		name : 'pjj.waveNumber',
		fieldLabel : '录波次数',
		width : 145,
		allowDecimals : false,
		allowNegative : false,
		allowBlank : true
	}) 
	// 录波完好次数
	var waveGoodNumber = new Ext.form.NumberField({
		id : 'waveGoodNumber',
		name : 'pjj.waveGoodNumber',
		fieldLabel : '录波完好次数',
		width : 145,
		allowDecimals : false,
		allowNegative : false,
		allowBlank : true
	}) 
	// 保护动作情况
	var protectAct = new Ext.form.TextArea({
				fieldLabel : '保护动作情况',
				id : 'protectAct',
				name : 'pjj.protectAct',
				width : 402
	})
	// 不正确动作分析
	var errorAnalyze = new Ext.form.TextArea({
				fieldLabel : '不正确动作分析',
				id : 'errorAnalyze',
				name : 'pjj.errorAnalyze',
				width : 402
	})
	// 填报人
	var fillName = new Ext.form.TextField({
				fieldLabel : '填报人',
				id : 'fillName',
				name : 'fillName',
				readOnly : true,
				width : 145
			})
	var fillBy = new Ext.form.Hidden({
		id : 'fillBy',
		name : 'pjj.fillBy'
	})
	getWorkCode();
	// 填报时间
	var fillDate = new Ext.form.TextField({
				fieldLabel : "填报时间",
				readOnly : true,
				id : 'fillDate',
				name : 'fillDate',
				value : getDate(),
				width : 145
			})
	var fillDateH = new Ext.form.Hidden({
		id : 'fillDateH',
		value : getDate(),
		name : 'pjj.fillDate'
	})

	// 弹窗的表单对象
	var blockForm = new Ext.form.FormPanel({
				labelAlign : 'right',
				border : true,
				frame : true,
				autoHeight : true,
				labelWidth : 100,
				style : 'padding:10px,5px,0px,5px',
				items : [{
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 1,
										layout : 'form',
										items : [bhzzdzId,deviceId,deviceName]
									},{
										columnWidth : 0.5,
										layout : 'form',
										items : [actDate]
									}, {
										columnWidth : 0.5,
										layout : 'form',
										items : [chargeDep,chargeDepH]
									}, {
										columnWidth : 0.5,
										layout : 'form',
										items : [actAppaise]
									}, {
										columnWidth : 0.5,
										layout : 'form',
										items : [actNum]
									}, {
										columnWidth : 0.5,
										layout : 'form',
										items : [waveNumber]
									}, {
										columnWidth : 0.5,
										layout : 'form',
										items : [waveGoodNumber]
									}, {
										columnWidth : 1,
										layout : 'form',
										items : [protectAct]
									}, {
										columnWidth : 1,
										layout : 'form',
										items : [errorAnalyze]
									}, {
										columnWidth : 0.5,
										layout : 'form',
										items : [fillName,fillBy]
									}, {
										columnWidth : 0.5,
										layout : 'form',
										items : [fillDate,fillDateH]
									}]
						}]
			});

	// 左边的弹出窗体
	var blockAddWindow;
	function showAddWindow() {
		if (!blockAddWindow) {
			blockAddWindow = new Ext.Window({
				title : '',
				layout : 'fit',
				width : 550,
				height : 360,
				modal : true,
				closable : true,
				border : false,
				resizable : false,
				closeAction : 'hide',
				plain : true,
				// 面板中按钮的排列方式
				buttonAlign : 'center',
				items : [blockForm],
				buttons : [{
					text : '保存',
					iconCls : 'save',
					handler : function() {
						if (Ext.get('deviceId').dom.value == null
								|| Ext.get('deviceId').dom.value == "") {
							Ext.Msg.alert("提示信息", "装置名称不能为空，请选择！");
							return;
						}
						if(Ext.get('actDate').getValue() == null
							|| Ext.get('actDate').getValue() == "")
						{
							Ext.Msg.alert("提示信息", "动作时间不能为空，请选择！");
							return;
						}
						if(waveNumber.getValue() < waveGoodNumber.getValue())
						{
							Ext.Msg.alert('提示信息','录波完好次数不得大于录波次数！');
							return;
						}
						if (blockForm.getForm().isValid())
							if (op == "add") {
								blockForm.getForm().submit({
									waitMsg : '保存中,请稍后...',
									url : "productionrec/addPProtectDevMotion.action",
									success : function(form, action) {
										var result = eval('('
												+ action.response.responseText
												+ ')');
										Ext.Msg.alert("提示信息", result.msg);
										blockForm.getForm().reset();
										blockAddWindow.hide();
										fuzzyText.setValue("")
										fromDate.setValue("")
										toDate.setValue("")
//										westgrids.reload();
										recordQuery();
									},
									failure : function(form, action) {
										var result = eval('('
												+ action.response.responseText
												+ ')');
										Ext.Msg.alert("提示信息", result.msg);
									}
								});
							} else if (op == "edit") {
								blockForm.getForm().submit({
									waitMsg : '保存中,请稍后...',
									url : "productionrec/updateProtectDevMotion.action",
									success : function(form, action) {
										var result = eval('('
												+ action.response.responseText
												+ ')');
										// 显示成功信息
										Ext.Msg.alert("提示信息", result.msg);
										blockForm.getForm().reset();
										blockAddWindow.hide();
										fuzzyText.setValue("")
										fromDate.setValue("")
										toDate.setValue("")
										recordQuery();
									},
									failure : function(form, action) {
										var result = eval('('
												+ action.response.responseText
												+ ')');
										Ext.Msg.alert("提示信息", result.msg);

									}
								});
							} else {
								Ext.MessageBox.alert('错误', '未定义的操作');
							}
					}
				}, {
					text : '取消',
					iconCls : 'cancer',
					handler : function() {
						blockForm.getForm().reset();
						blockAddWindow.hide();
					}
				}]
			});
		}
		if (op == "add") {
			blockForm.getForm().reset();
			// 新增时，赋初始值
			blockAddWindow.setTitle("新增保护装置动作情况信息");
		} else if (op == "edit") {
			blockAddWindow.setTitle("修改保护装置动作情况信息");
			blockAddWindow.show();
			var rec = westgrid.getSelectionModel().getSelected();
	
			Ext.get('bhzzdzId').dom.value = rec
					.get('pjj.bhzzdzId') == null ? "" : rec
					.get('pjj.bhzzdzId')
			Ext.get('deviceId').dom.value = rec
					.get('pjj.deviceId') == null ? "" : rec
					.get('pjj.deviceId')
			deviceName.setValue(rec.get('deviceName') == null
					? ""
					: rec.get('deviceName'))
			actDate.setValue(rec.get('actDate') == null
					? ""
					: rec.get('actDate'))
			chargeDep.setValue(rec.get('chargeDeptName') == null
					? ""
					: rec.get('chargeDeptName'));
			chargeDepH.setValue(rec.get('pjj.chargeDep') == null
					? ""
					: rec.get('pjj.chargeDep'))
			actAppaise.setValue(rec.get('pjj.actAppaise') == null
					? ""
					: rec.get('pjj.actAppaise'))
			actNum.setValue(rec.get('pjj.actNum') == null
					? ""
					: rec.get('pjj.actNum'))
			waveNumber.setValue(rec.get('pjj.waveNumber') == null
					? ""
					: rec.get('pjj.waveNumber'))
			waveGoodNumber.setValue(rec.get('pjj.waveGoodNumber') == null
					? ""
					: rec.get('pjj.waveGoodNumber'))
			protectAct.setValue(rec.get('pjj.protectAct') == null
					? ""
					: rec.get('pjj.protectAct'))
			errorAnalyze.setValue(rec.get('pjj.errorAnalyze') == null
					? ""
					: rec.get('pjj.errorAnalyze'))
			fillName.setValue(rec.get('fillName') == null
					? ""
					: rec.get('fillName'))
			fillBy.setValue(rec.get('pjj.fillBy') == null
					? ""
					: rec.get('pjj.fillBy'))
			fillDate.setValue(rec.get('fillDate') == null
					? ""
					: rec.get('fillDate'))
			fillDateH.setValue(rec.get('pjj.fillDate') == null
					? ""
					: rec.get('pjj.fillDate'))
			
		} else {
		}
		blockAddWindow.show()
	};

	// 左边按钮
	// 新建按钮
	var westbtnAdd = new Ext.Button({
				text : '新增',
				iconCls : 'add',
				handler : function() {
					op = "add";
					showAddWindow();
				}
			});

	// 选择判断
	function CKSelectdone() {
		var rec = westgrid.getSelectionModel().getSelections();
		if (rec.length != 1) {
			Ext.Msg.alert('提示信息', "请选择一行！");
			return false;
		} else {
			op = "edit";
			showAddWindow();
		}
	}

	// 修改按钮
	var westbtnedit = new Ext.Button({
				text : '修改',
				iconCls : 'update',
				handler : CKSelectdone
			});

	// 删除按钮
	var westbtndel = new Ext.Button({
		text : '删除',
		iconCls : 'delete',
		handler : function() {
			if (westgrid.getSelectionModel().getSelections().length > 0) {
				Ext.Msg.confirm('提示信息', '确认删除所选中记录? ', function(button, text) {
					if (button == 'yes') {
						var rec = westgrid.getSelectionModel().getSelections();
						var str = "";
						for (var i = 0; i < rec.length; i++) {
							if (i < rec.length - 1)
								str += rec[i].get('pjj.bhzzdzId')
										+ ",";
							else
								str += rec[i].get('pjj.bhzzdzId');
						}
						Ext.Ajax.request({
									waitMsg : '删除中,请稍后...',
									url : 'productionrec/deleteProtectDevMotion.action',
									params : {
										ids : str
									},
									success : function(response, options) {
										Ext.Msg.alert('提示信息', '数据删除成功！')
										westgrids.reload();
									},
									failure : function() {
										Ext.Msg.alert('提示信息', '服务器错误,请稍候重试!')
									}
								});
					}
				})
			} else {
				Ext.Msg.alert('提示信息', "请至少选择一行！");
				return false;
			}
		}
	});

	// 刷新按钮
//	var westbtnref = new Ext.Button({
//				text : '刷新',
//				iconCls : 'reflesh',
//				handler : function() {
//					westgrids.load({
//								params : {
//									start : 0,
//									limit : 18
//								}
//							});
//				}
//			});

	var westsm = new Ext.grid.CheckboxSelectionModel();
	// 左边列表中的数据
	var datalist = new Ext.data.Record.create([

	{
				name : 'pjj.bhzzdzId'
			}, {
				name : 'pjj.deviceId'
			}, {
				name : 'pjj.actDate'
			}, {
				name : 'pjj.chargeDep'
			}, {
				name : 'pjj.actAppaise'
			}, {
				name : 'pjj.actNum'
			}, {
				name : 'pjj.waveNumber'
			}, {
				name : 'pjj.waveGoodNumber'
			}, {
				name : 'pjj.protectAct'
			}, {
				name : 'pjj.errorAnalyze'
			}, {
				name : 'pjj.fillBy'
			}
			, {
				name : 'pjj.fillDate'
			}
			, {
				name : 'pjj.enterpriseCode'
			}
			, {
				name : 'deviceName'
			}, {
				name : 'actDate'
			},{
				name : 'chargeDeptName'
			}
			,
			{
				name : 'fillName'
			}, {
				name : 'fillDate'
			}]);

	var westgrids = new Ext.data.JsonStore({
				url : 'productionrec/findProtectDevMotionList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});

			
	function recordQuery() {		
		westgrids.baseParams = {
			name : fuzzyText.getValue(),
			fromTime : fromDate.getValue(),
			toTime : toDate.getValue()
		}
		westgrids.load({
					params : {
						start : 0,
						limit : 18
					}
				});
	}
//	westgrids.load({
//				params : {
//					start : 0,
//					limit : 18
//				}
//			});

	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
				ds : westgrids,			
				columns : [westsm, new Ext.grid.RowNumberer({
									header : "行号",
									width : 31
								}), {
							header : "保护装置动作编号",
							width : 120,
							align : "center",
							sortable : true,
							hidden : true,
							dataIndex : 'pjj.bhzzdzId'
						}, {
							header : "装置编号",
							width : 120,
							align : "center",
							sortable : true,
							hidden : true,
							dataIndex : 'pjj.deviceId'
						}, {
							header : "装置名称",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'deviceName'
						}, {
							header : "动作时间",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'actDate'
						}, {
							header : "责任部门",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'chargeDeptName'
						}, {
							header : "动作评价",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'pjj.actAppaise',
							renderer : function(v){
								if(v == '1')
								{
									return '正确';
								}
								else if(v == '2')
								{
									return '误动';
								}
								else if(v == '3')
								{
									return '拒动';
								}
							}
						}, {
							header : "动作次数",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'pjj.actNum'
						},
						
						{
							header : "录波次数",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'pjj.waveNumber'
						},
						{
							header : "录波完好次数",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'pjj.waveGoodNumber'
						}, {
							header : "保护动作情况",
							width : 160,
							align : "center",
							sortable : true,
							dataIndex : 'pjj.protectAct'
						}, {
							header : "不正确动作分析",
							width : 160,
							align : "center",
							sortable : true,
							dataIndex : 'pjj.errorAnalyze'
						}, {
							header : "填报人",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'fillName'
						}, {
							header : "填报日期",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'fillDate'
						}],
				tbar : [{text : '保护装置名称：'},fuzzyText,'-',
					{text : '动作时间:'},fromDate,'-',{text : '至：'},toDate,
						queryButton,westbtnAdd, {
							xtype : "tbseparator"
						}, westbtnedit, {
							xtype : "tbseparator"
						}, westbtndel
//						, {
//							xtype : "tbseparator"
//						}, westbtnref
						],
				sm : westsm,
				frame : true,
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : westgrids,
							displayInfo : true,
							displayMsg : "{0} 到 {1} /共 {2} 条",
							emptyMsg : "没有记录"
						}),
				border : true
			});

	// westgrid 的事件
	westgrid.on('rowdblclick', function(grid, rowIndex, e) {
				CKSelectdone()
			})

	recordQuery();
	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				autoScroll : true,
				items : [
					{
							region : 'center',
							layout : 'fit',
							border : false,
							margins : '1 0 1 1',
							autoScroll : true,
							items : [westgrid]
						}
						]
			});
});