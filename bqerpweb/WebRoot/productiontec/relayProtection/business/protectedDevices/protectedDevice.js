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

	// ***********!!!!!*******************

	// 从session取登录人编码姓名部门相关信息
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号，赋给全局变量
							workerCode = result.workerCode;
							workerName = result.workerName;

						}
					}
				});
	}
	// 被保护设备编号
	var protectedDeviceId = new Ext.form.Hidden({
				id : 'protectedDeviceId',
				name : 'pjjb.protectedDeviceId'
			})

	// 被保护设备
	var equName = new Ext.form.TriggerField({
				fieldLabel : '被保护设备',
				width : 402,
				id : "equName",
				hiddenName : 'equName',
				blankText : '请选择',
				emptyText : '请选择',
				maxLength : 100,
				readOnly : true
			});
	equName.onTriggerClick = selectDevice;
	var equCode = new Ext.form.Hidden({
				id : 'equCode',
				name : 'pjjb.equCode'
			})

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
		var url = "../../../../comm/jsp/equselect/selectAttribute.jsp?";
		url += "op=one";
		var equ = window.showModalDialog(url, window,
				'dialogWidth=600px;dialogHeight=400px;status=no');
		if (typeof(equ) != "undefined") {
			equName.setValue(equ.name);
			equCode.setValue(equ.code);
		}
	}
	// 设备级别store
	var levelStore = new Ext.data.SimpleStore({
				fields : ['id', 'name'],
				data : [['', ''], ['1', '一类'], ['2', '二类'], ['3', '三类']]
			});
	// 设备级别
	var equLevel = new Ext.form.ComboBox({
				store : levelStore,
				id : "equLevel",
				displayField : "name",
				valueField : "id",
				mode : 'local',
				width : 120,
				fieldLabel : "设备级别",
				triggerAction : 'all',
				readOnly : true,
				hiddenName : 'pjjb.equLevel'
			})
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
				width : 120,
				fieldLabel : "电压等级",
				triggerAction : 'all',
				readOnly : true,
				hiddenName : 'pjjb.voltage'
			})
	// 安装地点
	var installPlace = new Ext.form.TextField({
				fieldLabel : '安装地点',
				id : 'installPlace',
				name : 'pjjb.installPlace',
				width : 402
			})
	// 制造厂商
	var manufacturer = new Ext.form.TextField({
				fieldLabel : "制造厂商",
				id : 'manufacturer',
				name : 'pjjb.manufacturer',
				width : 402
			})
	// 规格型号
	var sizes = new Ext.form.TextField({
				fieldLabel : '规格型号',
				id : 'sizes',
				name : 'pjjb.sizes',
				width : 402
			})
	// 出厂编号
	var outFactoryNo = new Ext.form.TextField({
				fieldLabel : "出厂编号",
				id : 'outFactoryNo',
				name : 'pjjb.outFactoryNo',
				width : 402
			})
	// 出厂日期
	var outFactoryDate = new Ext.form.DateField({
				fieldLabel : '出厂日期',
				id : 'outFactoryDate',
				name : 'pjjb.outFactoryDate',
				width : 120,
				format : 'Y-m-d'
			})
	// 负责人
	var chargeMan = new Ext.form.TriggerField({
				fieldLabel : '负责人',
				width : 120,
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
				name : 'pjjb.chargeMan'
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
				name : 'pjjb.memo',
				width : 402
			})

	// 弹窗的表单对象
	var blockForm = new Ext.form.FormPanel({
				labelAlign : 'right',
				border : true,
				frame : true,
				autoHeight : true,
				labelWidth : 74,
				style : 'padding:10px,5px,0px,5px',
				items : [{
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 1,
										layout : 'form',
										items : [protectedDeviceId, equName,
												equCode]
									}, {
										columnWidth : 0.5,
										layout : 'form',
										items : [equLevel]
									}, {
										columnWidth : 0.5,
										layout : 'form',
										items : [voltage]
									}, {
										columnWidth : 1,
										layout : 'form',
										items : [installPlace]
									}, {
										columnWidth : 1,
										layout : 'form',
										items : [manufacturer]
									}, {
										columnWidth : 1,
										layout : 'form',
										items : [sizes]
									}, {
										columnWidth : 1,
										layout : 'form',
										items : [outFactoryNo]
									}, {
										columnWidth : 0.5,
										layout : 'form',
										items : [outFactoryDate]
									}, {
										columnWidth : 0.5,
										layout : 'form',
										items : [chargeMan, chargeManH]
									}, {
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
				height : 340,
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
							Ext.Msg.alert("提示信息", "被保护设备不能为空，请选择！");
							return;
						}
						if (blockForm.getForm().isValid())
							if (op == "add") {
								blockForm.getForm().submit({
									waitMsg : '保存中,请稍后...',
									url : "productionrec/addProtectedDevice.action",
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
									url : "productionrec/updateProtectedDevice.action",
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
			blockAddWindow.setTitle("新增被保护设备信息");
		} else if (op == "edit") {
			blockAddWindow.setTitle("修改被保护设备信息");
			blockAddWindow.show();
			var rec = westgrid.getSelectionModel().getSelected();

			Ext.get('protectedDeviceId').dom.value = rec
					.get('pjjb.protectedDeviceId') == null ? "" : rec
					.get('pjjb.protectedDeviceId')
			equName.setValue(rec.get('equName') == null
					? ""
					: rec.get('equName'))
			Ext.get('equCode').dom.value = rec.get('pjjb.equCode') == null
					? ""
					: rec.get('pjjb.equCode')
			equLevel.setValue(rec.get('pjjb.equLevel') == null ? "" : rec
					.get('pjjb.equLevel'));
			voltage.setValue(rec.get('pjjb.voltage') == null ? "" : rec
					.get('pjjb.voltage'));
			Ext.get('installPlace').dom.value = rec.get('pjjb.installPlace') == null
					? ""
					: rec.get('pjjb.installPlace')
			Ext.get('manufacturer').dom.value = rec.get('pjjb.manufacturer') == null
					? ""
					: rec.get('pjjb.manufacturer')
			Ext.get('sizes').dom.value = rec.get('pjjb.sizes') == null
					? ""
					: rec.get('pjjb.sizes')
			Ext.get('outFactoryNo').dom.value = rec.get('pjjb.outFactoryNo') == null
					? ""
					: rec.get('pjjb.outFactoryNo')
			outFactoryDate.setValue(rec.get('outFactoryDate') == null
					? ""
					: rec.get('outFactoryDate'));
			chargeMan.setValue(rec.get('chargeName') == null
					? ""
					: rec.get('chargeName'))
			Ext.get('chargeManH').dom.value = rec.get('pjjb.chargeMan') == null
					? ""
					: rec.get('pjjb.chargeMan')
			Ext.get('memo').dom.value = rec.get('pjjb.memo') == null ? "" : rec
					.get('pjjb.memo')
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
								str += rec[i].get('pjjb.protectedDeviceId')
										+ ",";
							else
								str += rec[i].get('pjjb.protectedDeviceId');
						}
						Ext.Ajax.request({
									waitMsg : '删除中,请稍后...',
									url : 'productionrec/deleteProtectedDevice.action',
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
				name : 'pjjb.protectedDeviceId'
			}, {
				name : 'pjjb.equCode'
			}, {
				name : 'pjjb.equLevel'
			}, {
				name : 'pjjb.voltage'
			}, {
				name : 'pjjb.installPlace'
			}, {
				name : 'pjjb.manufacturer'
			}, {
				name : 'pjjb.sizes'
			}, {
				name : 'pjjb.outFactoryNo'
			}, {
				name : 'pjjb.outFactoryDate'
			}, {
				name : 'pjjb.chargeMan'
			}, {
				name : 'pjjb.memo'
			}, {
				name : 'equName'
			}, {
				name : 'outFactoryDate'
			}, {
				name : 'chargeName'
			}]);

	var westgrids = new Ext.data.JsonStore({
				url : 'productionrec/findProtectedDeviceList.action',
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
							header : "被保护设备编号",
							width : 120,
							align : "center",
							hidden : true,
							sortable : true,
							dataIndex : 'pjjb.protectedDeviceId'
						}, {
							header : "设备名称",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'equName'
						}, {
							header : "设备级别",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'pjjb.equLevel',
							renderer : function(v){
								if(v == '1')
								{
									return '一类';
								}
								else if(v == '2')
								{
									return '二类';
								}
								else if(v == '3')
								{
									return '三类';
								}
							}
						}, {
							header : "电压等级",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'pjjb.voltage',
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
							header : "安装地点",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'pjjb.installPlace'
						}, {
							header : "制造厂商",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'pjjb.manufacturer'
						}, {
							header : "规格型号",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'pjjb.sizes'
						}, {
							header : "出厂编号",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'pjjb.outFactoryNo'
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
							dataIndex : 'pjjb.memo'
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