Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	// 装置编号
	var deviceId = new Ext.form.Hidden({
		id : 'deviceId',
		name : 'ptjd.deviceId'
	})
	// 装置名称
	var equName = new Ext.form.TriggerField({
				fieldLabel : '装置名称',
				width : 402,
				id : "equName",
				hiddenName : 'equName',
				blankText : '请选择',
				emptyText : '请选择',
				maxLength : 100,
				readOnly : true
			});
	equName.onTriggerClick = selectEqu;
	// 装置名称编码
	var equCode = new Ext.form.Hidden({
				id : 'equCode',
				name : 'ptjd.equCode'
			})

	/**
	 * 设备选择画面处理
	 */
	function selectEqu() {
		var args = {
			selectModel : 'signal',
			notIn : "",
			rootNode : {
				id : '-1',
				text : '合肥电厂'
			}
		}
		var url = "../../../../comm/jsp/equselect/selectAttribute.jsp?";
		url += "op=one";
		var equ = window.showModalDialog(url, window,
				'dialogWidth=600px;dialogHeight=400px;status=no');
		if (typeof(equ) != "undefined") {
			equName.setValue(equ.name);
			equCode.setValue(equ.code);
		}
	}
	// 被保护设备编号
	var protectedDeviceId = new Ext.form.Hidden({
				id : 'protectedDeviceId',
				name : 'ptjd.protectedDeviceId'
			})

	// 被保护设备名称
	var protectedDeviceName = new Ext.form.TriggerField({
				fieldLabel : '被保护设备',
				width : 402,
				id : "protectedEquName",
				hiddenName : 'protectedEquName',
				blankText : '请选择',
				emptyText : '请选择',
				maxLength : 100,
				readOnly : true
			});
	protectedDeviceName.onTriggerClick = selectDevice;

	/**
	 * 设备选择画面处理
	 */
	function selectDevice() {
		var args = {
			selectModel : 'signal',
			notIn : "",
			rootNode : {
				id : '-1',
				text : '合肥电厂'
			}
		}
		var url = "../protectedDevSelect.jsp";
		var equ = window.showModalDialog(url, window,
				'dialogWidth=600px;dialogHeight=400px;status=no');
		if (typeof(equ) != "undefined") {
			protectedDeviceName.setValue(equ.protectedDeviceName);
			protectedDeviceId.setValue(equ.protectedDeviceId);
		}
	}
	// 电压等级store
	var voltageStore = new Ext.data.SimpleStore({
				fields : ['id', 'name'],
				data : [['', ''], ['1', '500KV'], ['2', '220KV'],
						['3', '20KV'], ['4', '10KV'], ['5', '3KV']]
			});
	// 电压等级
	var voltage = new Ext.form.ComboBox({
				store : voltageStore,
				id : "voltage",
				displayField : "name",
				valueField : "id",
				mode : 'local',
				width : 145,
				fieldLabel : "电压等级",
				triggerAction : 'all',
				readOnly : true,
				hiddenName : 'ptjd.voltage'
			})
	// 装置类别store
	var deviceTypeStore = new Ext.data.SimpleStore({
				fields : ['id', 'name'],
				data : [['', ''], ['1', '线路'], ['2', '元件'],
						['3', '自动装置']]
			});
	// 装置类别
	var deviceType = new Ext.form.ComboBox({
				store : deviceTypeStore,
				id : "deviceType",
				displayField : "name",
				valueField : "id",
				mode : 'local',
				width : 145,
				fieldLabel : "装置类别",
				triggerAction : 'all',
				readOnly : true,
				hiddenName : 'ptjd.deviceType'
			})
	// 型号类别store
	var sizeTypeStore = new Ext.data.SimpleStore({
				fields : ['id', 'name'],
				data : [['', ''], ['1', '微机'], ['2', '集成电路'],
						['3', '晶体管'],['4','整流管']]
			});
	// 型号类别
	var sizeType = new Ext.form.ComboBox({
				store : sizeTypeStore,
				id : "sizeType",
				displayField : "name",
				valueField : "id",
				mode : 'local',
				width : 145,
				fieldLabel : "型号类别",
				triggerAction : 'all',
				readOnly : true,
				hiddenName : 'ptjd.sizeType'
			})
	// 安装地点
	var installPlace = new Ext.form.TextField({
				fieldLabel : '安装地点',
				id : 'installPlace',
				name : 'ptjd.installPlace',
				width : 402
			})
	// 制造厂商
	var manufacturer = new Ext.form.TextField({
				fieldLabel : "制造厂商",
				id : 'manufacturer',
				name : 'ptjd.manufacturer',
				width : 402
			})
	// 规格型号
	var sizes = new Ext.form.TextField({
				fieldLabel : '规格型号',
				id : 'sizes',
				name : 'ptjd.sizes',
				width : 145
			})
	// 出厂编号
	var outFactoryNo = new Ext.form.TextField({
				fieldLabel : "出厂编号",
				id : 'outFactoryNo',
				name : 'ptjd.outFactoryNo',
				width : 145
			})
	// 出厂日期
	var outFactoryDate = new Ext.form.DateField({
				fieldLabel : '出厂日期',
				id : 'outFactoryDate',
				name : 'ptjd.outFactoryDate',
				width : 145,
				format : 'Y-m-d'
			})
	// 试验周期
	var testCycle = new Ext.form.NumberField({
		id : 'testCycle',
		name : 'ptjd.testCycle',
		fieldLabel : '试验周期(月)',
		width : 145,
		allowDecimals : false,
		allowNegative : false,
		allowBlank : false
	})
	// 负责人
	var chargeMan = new Ext.form.TriggerField({
				fieldLabel : '负责人',
				width : 145,
				id : "chargeMan",
				hiddenName : 'chargeName',
				blankText : '请选择',
				emptyText : '请选择',
				maxLength : 100,
				readOnly : true
			});
	chargeMan.onTriggerClick = selectPersonWin;
	var chargeManH = new Ext.form.Hidden({
				id : 'chargeManH',
				name : 'ptjd.chargeBy'
			})
	/**
	 * 人员选择画面处理
	 */
	function selectPersonWin() {
		var args = {
			selectModel : 'signal',
			notIn : "",
			rootNode : {
				id : '-1',
				text : '合肥电厂'
			}
		}
		var person = window
				.showModalDialog(
						'../../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(person) != "undefined") {
			chargeMan.setValue(person.workerName);
			chargeManH.setValue(person.workerCode);
		}
	}
	// 备注
	var memo = new Ext.form.TextArea({
				fieldLabel : '备注',
				id : 'memo',
				name : 'ptjd.memo',
				width : 402
			})

	// 弹窗的表单对象
	var blockForm = new Ext.form.FormPanel({
				labelAlign : 'right',
				border : true,
				frame : true,
				autoHeight : true,
				labelWidth : 90,
				style : 'padding:10px,5px,0px,5px',
				items : [{
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 1,
										layout : 'form',
										items : [deviceId,equName,equCode]
									},{
										columnWidth : 1,
										layout : 'form',
										items : [protectedDeviceId,protectedDeviceName]
									}, {
										columnWidth : 0.5,
										layout : 'form',
										items : [voltage]
									}, {
										columnWidth : 0.5,
										layout : 'form',
										items : [deviceType]
									}, {
										columnWidth : 0.5,
										layout : 'form',
										items : [sizeType]
									}, {
										columnWidth : 0.5,
										layout : 'form',
										items : [sizes]
									}, {
										columnWidth : 1,
										layout : 'form',
										items : [manufacturer]
									}, {
										columnWidth : 0.5,
										layout : 'form',
										items : [outFactoryDate]
									}, {
										columnWidth : 0.5,
										layout : 'form',
										items : [outFactoryNo]
									}, {
										columnWidth : 1,
										layout : 'form',
										items : [installPlace]
									}, {
										columnWidth : 0.5,
										layout : 'form',
										items : [testCycle]
									}, {
										columnWidth : 0.5,
										layout : 'form',
										items : [chargeMan, chargeManH]
									} ,{
										columnWidth : 1,
										layout : 'form',
										items : [memo]
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
						if (Ext.get('equCode').dom.value == null
								|| Ext.get('equCode').dom.value == "") {
							Ext.Msg.alert("提示信息", "装置名称不能为空，请选择！");
							return;
						}
						if(Ext.get('protectedDeviceId').getValue() == null
							|| Ext.get('protectedDeviceId').getValue() == "")
						{
							Ext.Msg.alert("提示信息", "被保护设备名称不能为空，请选择！");
							return;
						}
						if(Ext.get('testCycle').dom.value == null
							|| Ext.get('testCycle').dom.value == "")
							{
								Ext.Msg.alert("提示信息", "试验周期(月)不能为空，请输入！")
							}
						if (blockForm.getForm().isValid())
							if (op == "add") {
								blockForm.getForm().submit({
									waitMsg : '保存中,请稍后...',
									url : "productionrec/addProtectEqu.action",
									success : function(form, action) {
										var result = eval('('
												+ action.response.responseText
												+ ')');
										Ext.Msg.alert("提示信息", result.msg);
										blockForm.getForm().reset();
										blockAddWindow.hide();
										westgrids.reload();
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
									url : "productionrec/updateProtectEqu.action",
									success : function(form, action) {
										var result = eval('('
												+ action.response.responseText
												+ ')');

										// 显示成功信息
										Ext.Msg.alert("提示信息", result.msg);
										blockForm.getForm().reset();
										blockAddWindow.hide();
										westgrids.reload();
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
			blockAddWindow.setTitle("新增保护装置信息");
		} else if (op == "edit") {
			blockAddWindow.setTitle("修改保护装置信息");
			blockAddWindow.show();
			var rec = westgrid.getSelectionModel().getSelected();

			Ext.get('deviceId').dom.value = rec
					.get('ptjd.deviceId') == null ? "" : rec
					.get('ptjd.deviceId')
			equName.setValue(rec.get('equName') == null
					? ""
					: rec.get('equName'))
			Ext.get('equCode').dom.value = rec.get('ptjd.equCode') == null
					? ""
					: rec.get('ptjd.equCode')				
			Ext.get('protectedDeviceId').dom.value = rec.get('ptjd.protectedDeviceId') == null
					? ""
					: rec.get('ptjd.protectedDeviceId')
			protectedDeviceName.setValue(rec.get('protectedDeviceName') == null
					? ""
					: rec.get('protectedDeviceName'));
			voltage.setValue(rec.get('ptjd.voltage') == null
					? ""
					: rec.get('ptjd.voltage'));
			deviceType.setValue(rec.get('ptjd.deviceType') == null
					? ""
					: rec.get('ptjd.deviceType'));		
			sizeType.setValue(rec.get('ptjd.sizeType') == null
					? ""
					: rec.get('ptjd.sizeType'));					
			Ext.get('installPlace').dom.value = rec.get('ptjd.installPlace') == null
					? ""
					: rec.get('ptjd.installPlace')
			Ext.get('manufacturer').dom.value = rec.get('ptjd.manufacturer') == null
					? ""
					: rec.get('ptjd.manufacturer')
			Ext.get('sizes').dom.value = rec.get('ptjd.sizes') == null
					? ""
					: rec.get('ptjd.sizes')
			Ext.get('outFactoryNo').dom.value = rec.get('ptjd.outFactoryNo') == null
					? ""
					: rec.get('ptjd.outFactoryNo')
			outFactoryDate.setValue(rec.get('outFactoryDate') == null
					? ""
					: rec.get('outFactoryDate'));			
			testCycle.setValue(rec.get('ptjd.testCycle') == null
					? ""
					: rec.get('ptjd.testCycle'));					
			chargeMan.setValue(rec.get('chargeName') == null
					? ""
					: rec.get('chargeName'))
			Ext.get('chargeManH').dom.value = rec.get('ptjd.chargeBy') == null
					? ""
					: rec.get('ptjd.chargeBy')
			Ext.get('memo').dom.value = rec.get('ptjd.memo') == null ? "" : rec
					.get('ptjd.memo')
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
				Ext.Msg.confirm('提示信息', '确认删除选中记录? ', function(button, text) {
					if (button == 'yes') {
						var rec = westgrid.getSelectionModel().getSelections();
						var str = "";
						for (var i = 0; i < rec.length; i++) {
							if (i < rec.length - 1)
								str += rec[i].get('ptjd.deviceId')
										+ ",";
							else
								str += rec[i].get('ptjd.deviceId');
						}
						Ext.Ajax.request({
									waitMsg : '删除中,请稍后...',
									url : 'productionrec/deleteProtectEqu.action',
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
	var westbtnref = new Ext.Button({
				text : '刷新',
				iconCls : 'reflesh',
				handler : function() {
					westgrids.load({
								params : {
									start : 0,
									limit : 18
								}
							});
				}
			});

	var westsm = new Ext.grid.CheckboxSelectionModel();
	// 左边列表中的数据
	var datalist = new Ext.data.Record.create([

	{
				name : 'ptjd.deviceId'
			}, {
				name : 'ptjd.protectedDeviceId'
			}, {
				name : 'ptjd.equCode'
			}, {
				name : 'ptjd.voltage'
			}, {
				name : 'ptjd.deviceType'
			}, {
				name : 'ptjd.sizeType'
			}, {
				name : 'ptjd.sizes'
			}, {
				name : 'ptjd.manufacturer'
			}, {
				name : 'ptjd.outFactoryDate'
			}, {
				name : 'ptjd.outFactoryNo'
			}, {
				name : 'ptjd.installPlace'
			}
			, {
				name : 'ptjd.testCycle'
			}
			, {
				name : 'ptjd.chargeBy'
			}, {
				name : 'ptjd.memo'
			}, {
				name : 'equName'
			}, {
				name : 'protectedDeviceName'
			}
			,
			{
				name : 'outFactoryDate'
			}, {
				name : 'chargeName'
			}]);

	var westgrids = new Ext.data.JsonStore({
				url : 'productionrec/findProtectEquList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});

	westgrids.load({
				params : {
					start : 0,
					limit : 18
				}
			});

	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
				ds : westgrids,
			
				columns : [westsm, new Ext.grid.RowNumberer({
									header : "行号",
									width : 31
								}), {
							header : "装置编号",
							width : 120,
							align : "center",
							hidden : true,
							sortable : true,
							dataIndex : 'ptjd.deviceId'
						}, {
							header : "被保护设备编号",
							width : 120,
							align : "center",
							sortable : true,
							hidden : true,
							dataIndex : 'ptjd.protectedDeviceId'
						}, {
							header : "装置名称",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'equName'
						}, {
							header : "被保护设备名称",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'protectedDeviceName'
						}, {
							header : "电压等级",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'ptjd.voltage',
							renderer : function(v){
								if(v == '1')
								{
									return '500KV';
								}
								else if(v == '2')
								{
									return '220KV';
								}
								else if(v == '3')
								{
									return '20KV';
								}
								else if(v == '4')
								{
									return '10KV';
								}
								else if(v == '5')
								{
									return '3KV';
								}
							}
						}, {
							header : "装置类别",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'ptjd.deviceType',
							renderer : function(v){
								if(v == '1')
								{
									return '线路';
								}
								else if(v == '2')
								{
									return '元件';
								}
								else if(v == '3')
								{
									return '自动装置';
								}
							}
						}, {
							header : "型号类别",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'ptjd.sizeType',
							renderer : function(v){
								if(v == '1')
								{
									return '微机';
								}
								else if(v == '2')
								{
									return '集成电路';
								}
								else if(v == '3')
								{
									return '晶体管';
								}
								else if( v == '4')
								{
									return '整流管';
								}
							}
						},
						
						{
							header : "检验周期（月）",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'ptjd.testCycle'
						},
						{
							header : "安装地点",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'ptjd.installPlace'
						}, {
							header : "制造厂商",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'ptjd.manufacturer'
						}, {
							header : "规格型号",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'ptjd.sizes'
						}, {
							header : "出厂编号",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'ptjd.outFactoryNo'
						}, {
							header : "出厂日期",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'outFactoryDate'
						}, {
							header : "负责人",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'chargeName'
						}, {
							header : "备注",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'ptjd.memo'
						}],
				tbar : [westbtnAdd, {
							xtype : "tbseparator"
						}, westbtnedit, {
							xtype : "tbseparator"
						}, westbtndel, {
							xtype : "tbseparator"
						}, westbtnref],
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