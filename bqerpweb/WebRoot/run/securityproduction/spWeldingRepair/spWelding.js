SpWelding = function(isQuery, type) {

	// grid列表数据源
	var Record = new Ext.data.Record.create([sm, {
				name : 'repairId',
				mapping : 0
			}, {
				name : 'toolCode',
				mapping : 1
			}, {

				name : 'toolType',
				mapping : 2
			}, {
				name : 'toolModel',
				mapping : 3
			}, {
				name : 'belongDepName',
				mapping : 4
			}, {
				name : 'factoryDate',
				mapping : 5
			}, {
				name : 'insulation',
				mapping : 6
			}, {
				name : 'resistance',
				mapping : 7
			}, {
				name : 'outsideCheck',
				mapping : 8
			}, {
				name : 'repairResult',
				mapping : 9
			}, {
				name : 'repairByName',
				mapping : 10
			}, {
				name : 'repairBegin',
				mapping : 11
			}, {
				name : 'repairEnd',
				mapping : 12
			}, {
				name : 'nextTime',
				mapping : 13
			}, {
				name : 'memo',
				mapping : 14
			}, {
				name : 'toolId',
				mapping : 15
			}, {
				name : 'toolName',
				mapping : 16
			}, {
				name : 'repairBy',
				mapping : 17
			}, {
				name : 'belongDep',
				mapping : 18
			}, {
				name : 'fillBy',
				mapping : 19
			}, {
				name : 'fillByName',
				mapping : 20
			}]);

	var label1 = new Ext.form.Label({
				text : '检验时间：'
			})
	var queryStartTime = new Ext.form.TextField({
				id : 'startTime',
				readOnly : true,
				width : 80,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%m-%d',
									dateFmt : 'yyyy-MM-dd'
								})
					}
				}
			})

	var label2 = new Ext.form.Label({
				text : '至：'
			})
	var queryEndTime = new Ext.form.TextField({
				id : 'endTime',
				readOnly : true,
				width : 80,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%m-%d',
									dateFmt : 'yyyy-MM-dd'
								})
					}
				}
			})
	var label3 = new Ext.form.Label({
				text : '编号：'
			})
	var queryCode = new Ext.form.TextField({
				id : 'queryCode'
			})

	var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'security/findWeldingList.action'
						}),
				reader : new Ext.data.JsonReader({
							root : 'list',
							totalProperty : 'totalCount'
						}, Record)

			})
	store.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							beginTime : queryStartTime.getValue(),
							endTime : queryEndTime.getValue(),
							toolCode : queryCode.getValue(),
							toolType : type,
							isMaint : (isQuery != null && isQuery) ? null : "1"
						})
			})
	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false
			})
	// 增加
	var addBtu = new Ext.Button({
				id : 'addBtu',
				text : '新增',
				iconCls : 'add',
				handler : addBoiler
			})
	function addBoiler() {
		editWin.setTitle('新增电动工器具和电焊机检修记录')
		editWin.show();
		formPanel.getForm().reset()
	}
	// 删除
	var deleteBtu = new Ext.Button({
				id : 'deleteBtu',
				text : '删除',
				iconCls : 'delete',
				handler : deleteBoiler
			})
	function deleteBoiler() {
		if (sm.hasSelection()) {
			Ext.Msg.confirm('提示', '确认要删除数据吗？', function(buttonId) {
				if (buttonId == 'yes') {
					var ids = new Array();
					var selects = sm.getSelections();
					for (var i = 0; i < selects.length; i++) {
						ids.push(selects[i].get('repairId'))
					}
					if (ids.length > 0) {
						Ext.Ajax.request({
									url : 'security/deleteWelding.action',
									method : 'post',
									params : {
										ids : ids.join(",")
									},
									success : function(response, options) {
										if (response && response.responseText) {
											var res = Ext
													.decode(response.responseText)
											Ext.Msg.alert('提示', res.msg);
											queryFun()
										}
									},
									failure : function(response, options) {
										Ext.Msg.alert('提示', '删除数据出现异常！')
									}
								})
					}
				}
			})

		} else
			Ext.Msg.alert('提示', '请先选择要删除的数据！')

	}

	// 修改
	var updateBtu = new Ext.Button({
				id : 'exportBtu',
				text : '修改',
				iconCls : 'update',
				handler : updateBoiler
			})
	function updateBoiler() {
		if (sm.hasSelection()) {
			if (sm.getSelections().length > 1)
				Ext.Msg.alert('提示', '请选择其中一条数据!');
			else {
				editWin.setTitle('修改电动工器具和电焊机检修记录')
				editWin.show();
				formPanel.getForm().loadRecord(sm.getSelected())
				belongDep.setValue(sm.getSelected().get('belongDep'));
				tool.setValue(sm.getSelected().get('toolId'), sm.getSelected()
								.get('toolName'));
				toolType.setValue('电动工具')
				person.setValue(sm.getSelected().get('repairBy'), sm
								.getSelected().get('repairByName'))
			}
		} else
			Ext.Msg.alert('提示', '请先选择要修改的数据！')

	}

	// 查询
	var queryBtu = new Ext.Button({
				id : 'queryBtu',
				text : '查询',
				iconCls : 'query',
				handler : queryFun
			})
	function queryFun() {
		store.load({
					params : {
						start : 0,
						limit : 18
					}
				})

	}
	// 导出
	var exportBtu = new Ext.Button({
				id : 'exportBtu',
				text : '导出',
				iconCls : 'export',
				handler : exportFun
			})

	function tableToExcel(tableHTML) {
		window.clipboardData.setData("Text", tableHTML);
		try {
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExApp.DisplayAlerts = false;
			ExApp.visible = true;
		} catch (e) {
			if (e.number != -2146827859)
				alert("您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}
	function myExport() {
		if (repairStoreCopy.getTotalCount() == 0) {
			Ext.Msg.alert('提示', '无数据可导出！')
		} else {
			var html = ['<table border=1><tr><th>编号</th><th>类别</th><th>规格型号</th><th>所属部门</th><th>出厂日期</th><th>绝缘电阻</th><th>线间电阻</th><th>外挂检查</th><th>检验结果</th><th>检查人</th><th>检验开始时间</th><th>检验结束时间</th>'
					+ '<th>下次检验时间</th><th>备注</th></tr>'];
			for (var i = 0; i < repairStoreCopy.getTotalCount(); i++) {
				var rc = repairStoreCopy.getAt(i);
				var typeName = rc.get('toolType') == null ? '' : rc
						.get('toolType');
				if (typeName) {
					if (typeName == 1)
						typeName = '电气安全用具';
					else if (typeName == 2)
						typeName = '电动工具';
					else if (typeName == 3)
						typeName = '安全带清册';
				}
				html
						.push('<tr><td>'
								+ (rc.get('toolCode') == null ? "" : rc
										.get('toolCode'))
								+ '</td>'
								+ '<td>'
								+ typeName
								+ '</td>'
								+ '<td>'
								+ (rc.get('toolModel') == null ? "" : rc
										.get('toolModel'))
								+ '</td>'
								+ '<td>'
								+ (rc.get('belongDepName') == null ? "" : rc
										.get('belongDepName'))
								+ '</td>'
								+ '<td>'
								+ (rc.get('madeDate') == null ? "" : rc
										.get('madeDate'))
								+ '</td>'
								+ '<td>'
								+ (rc.get('insulation') == null ? "" : rc
										.get('insulation'))
								+ '</td>'
								+ '<td>'
								+ (rc.get('resistance') == null ? "" : rc
										.get('resistance'))
								+ '</td>'
								+ '<td>'
								+ (rc.get('outsideCheck') == null ? "" : rc
										.get('outsideCheck'))
								+ '</td>'
								+ '<td>'
								+ (rc.get('repairResult') == null ? "" : rc
										.get('repairResult'))
								+ '</td>'
								+ '<td>'
								+ (rc.get('repairByName') == null ? "" : rc
										.get('repairByName'))
								+ '</td>'
								+ '<td>'
								+ (rc.get('repairBegin') == null ? "" : rc
										.get('repairBegin'))
								+ '</td>'
								+ '<td>'
								+ (rc.get('repairEnd') == null ? "" : rc
										.get('repairEnd'))
								+ '</td>'
								+ '<td>'
								+ (rc.get('nextTime') == null ? "" : rc
										.get('nextTime'))
								+ '</td>'
								+ '<td>'
								+ (rc.get('memo') == null ? "" : rc.get('memo'))
								+ '</td>' + '</tr>')

			}
			html.push('</table>');
			html = html.join(''); // 最后生成的HTML表格
			tableToExcel(html);
		}

	}
	function exportFun() {
		if (repairStoreCopy.getTotalCount() == 0) {
			repairStoreCopy.load()
		} else {
			repairStoreCopy.reload();
		}
	}
	var gridTbar = new Ext.Toolbar({
				items : [label1, queryStartTime, label2, queryEndTime, label3,
						queryCode, queryBtu, addBtu, updateBtu, deleteBtu,
						exportBtu]
			});
	var bbar = new Ext.PagingToolbar({
				pageSize : 18,
				store : store,
				displayInfo : true,
				displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
				emptyMsg : "没有记录"
			})
	// 页面的Grid主体
	var grid = new Ext.grid.GridPanel({
				store : store,
				layout : 'fit',
				autoScroll : true,
				frame : 'true',
				border : false,
				columns : [sm, new Ext.grid.RowNumberer({
									header : '行号',
									width : 35
								}),// 选择框
						{
							header : '编号',
							align : 'center',
							readOnly : true,
							dataIndex : 'toolCode'
						}, {
							header : '类别',
							align : 'center',
							readOnly : true,
							dataIndex : 'toolType',
							renderer : function(v) {
								if (v == 1)
									return '电气安全用具';
								else if (v == 2)
									return '电动工具';
								else if (v == 3)
									return '安全带清册';
								else
									return '';
							}
						}, {
							header : '型号及规格',
							align : 'center',
							readOnly : true,
							dataIndex : 'toolModel'
						}, {
							header : '所属部门',
							align : 'center',
							readOnly : true,
							dataIndex : 'belongDepName'
						}, {
							header : '出厂日期',
							align : 'center',
							readOnly : true,
							dataIndex : 'factoryDate'
						}, {
							header : '绝缘电阻（мΩ）',
							align : 'center',
							readOnly : true,
							dataIndex : 'insulation'
						}, {
							header : '线间电阻（Ω）',
							align : 'center',
							readOnly : true,
							dataIndex : 'resistance'
						}, {
							header : '外挂检查',
							align : 'center',
							readOnly : true,
							dataIndex : 'outsideCheck'
						}, {
							header : '检验结果',
							align : 'center',
							readOnly : true,
							dataIndex : 'repairResult'
						}, {
							header : '检验人',
							align : 'center',
							readOnly : true,
							dataIndex : 'repairByName'
						}, {
							header : '检验开始时间',
							align : 'center',
							readOnly : true,
							dataIndex : 'repairBegin'
						}, {
							header : '检验结束时间',
							align : 'center',
							readOnly : true,
							dataIndex : 'repairEnd'
						}, {
							header : '下次检验时间',
							align : 'center',
							readOnly : true,
							dataIndex : 'nextTime'
						}, {
							header : '备注',
							align : 'center',
							readOnly : true,
							dataIndex : 'memo'
						}],

				sm : sm, // 选择框的选择
				tbar : gridTbar,
				bbar : bbar
			});
	// 检测ID
	var repairId = new Ext.form.Hidden({
				id : 'repairId',
				name : 'welding.repairId'
			})
	// 工具ID
	var tool = new Tool({
				fieldLabel : '电动工具',
				hiddenName : 'welding.toolId',
				anchor : '90%',
				allowBlank : false
			}, true, 2, true)
	tool.init();
	tool.queryFun();
	tool.confirmBtu.on('click', function() {
				var result = tool.confirmFun();
				if (result) {
					{
						toolCode.setValue(result.get('toolCode'))
						toolModel.setValue(result.get('toolModel'))
						factoryDate.setValue(result.get('factoryDate'))
					}
				}
			})
	// 编号
	var toolCode = new Ext.form.TextField({
				id : 'toolCode',
				name : 'toolCode',
				fieldLabel : '编号',
				disabled : true,
				anchor : '90%'
			})

	var toolType = new Ext.form.TextField({
				id : 'toolType',
				fieldLabel : '类别',
				anchor : '90%',
				value : '电动工具和电焊机',
				disabled : true,
				readOnly : true
			})
	// 规格型号
	var toolModel = new Ext.form.TextField({
				id : 'toolModel',
				name : 'toolModel',
				fieldLabel : '规格型号',
				disabled : true,
				anchor : '90%'
			})
	// 所属部门
	var belongDep = new Ext.form.Hidden({
				id : 'belongDep',
				name : 'welding.belongDep'
			})
	var dept = new Power.dept(null, false, {
				fieldLabel : '所属部门',
				hiddenName : 'belongDepName',
				anchor : '90%',
				allowBlank : false
			});
	dept.btnConfrim.on('click', function() {
				var deptRes = dept.getValue();
				if (deptRes.code != null) {
					belongDep.setValue(deptRes.code)
				} else {
					belongDep.setValue(null);
				}
			})
	// 出厂日期
	var factoryDate = new Ext.form.TextField({
				id : 'factoryDate',
				name : 'welding.madeDate',
				fieldLabel : '出厂日期',
				anchor : '90%',
				disabled : false,
				readOnly : true
			})
	// 绝缘电阻
	var insulation = new Ext.form.TextField({
				id : 'insulation',
				name : 'welding.insulation',
				fieldLabel : '绝缘电阻',
				anchor : '90%'
			})
	// 线间电阻
	var resistance = new Ext.form.TextField({
				id : 'resistance',
				name : 'welding.resistance',
				fieldLabel : '线间电阻',
				anchor : '90%'
			})
	// 外挂检查
	var outsideCheck = new Ext.form.TextField({
				id : 'outsideCheck',
				name : 'welding.outsideCheck',
				fieldLabel : '外挂检查',
				anchor : '90%'
			})
	// 检验结果
	var repairResult = new Ext.form.TextArea({
				id : 'repairResult',
				name : 'welding.repairResult',
				fieldLabel : '检验结果',
				anchor : '90%',
				height : 75,
				allowBlank : false
			})
	// 检验人
	var person = new Power.person({
				fieldLabel : '检验人',
				hiddenName : 'welding.repairBy',
				anchor : '90%'
			}, {
				selectModel : 'single'
			})
	// 检修开始时间
	var repairBegin = new Ext.form.TextField({
				id : 'repairBegin',
				name : 'welding.repairBegin',
				fieldLabel : '检修开始时间',
				style : 'cursor:pointer',
				anchor : '90%',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%m-%d',
									dateFmt : 'yyyy-MM-dd'
								})
					}
				}
			})
	// 检修结束时间
	var repairEnd = new Ext.form.TextField({
				id : 'repairEnd',
				name : 'welding.repairEnd',
				fieldLabel : '检修结束时间',
				style : 'cursor:pointer',
				anchor : '90%',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%m-%d',
									dateFmt : 'yyyy-MM-dd'
								})
					}
				}
			})
	// 下次检验时间
	var nextTime = new Ext.form.TextField({
				id : 'nextTime',
				name : 'welding.nextTime',
				fieldLabel : '下次检验时间',
				style : 'cursor:pointer',
				anchor : '90%',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%m-%d',
									dateFmt : 'yyyy-MM-dd'
								})
					}
				}
			})
	// 备注
	var memo = new Ext.form.TextArea({
				id : 'memo',
				name : 'welding.memo',
				fieldLabel : '备注',
				anchor : '90%',
				height : 75
			})

	// 保存按钮
	var saveBtu = new Ext.Button({
				id : 'saveBtu',
				text : '保存',
				iconCls : 'save',
				handler : saveFun
			})
	function saveFun() {
		if (formPanel.getForm().isValid()) {
			if (belongDep.getValue() == null || belongDep.getValue() == '') {
				Ext.Msg.alert('提示', '请选择一具体部门！');
				return;
			}
			Ext.Msg.confirm("提示", '确认要保存数据吗？', function(buttonId) {
						if (buttonId == 'yes') {
							formPanel.getForm().submit({
								url : 'security/saveWelding.action',
								method : 'post',
								success : function(form, action) {
									if (action && action.response
											&& action.response.responseText) {
										var res = Ext
												.decode(action.response.responseText)
										Ext.Msg.alert('提示', '保存成功！')
									}
									editWin.hide();
									queryFun();
								},
								failure : function(form, action) {
									Ext.Msg.alert('提示', '保存出现异常！')
								}
							})
						}
					})
		}
	}
	// 取消按钮
	var cancelBtu = new Ext.Button({
				id : "cancelBtu",
				text : '取消',
				iconCls : 'cancer',
				handler : cancelFun
			})
	function cancelFun() {
		formPanel.getForm().reset()
		editWin.hide()
	}
	// formpanel
	var formPanel = new Ext.form.FormPanel({
				id : 'formPanel',
				frame : true,
				border : false,
				layout : 'column',
				buttons : [saveBtu, cancelBtu],
				buttonAlign : 'center',
				labelAlign : 'right',
				labelWidth : 120,
				defaults : {
					layout : 'form',
					frame : false,
					border : false
				},
				items : [{
					columnWidth : 0.5,
					items : [repairId, tool.combo, toolType, dept.combo,
							insulation]
				}, {
					columnWidth : 0.5,
					items : [toolCode, toolModel, belongDep, factoryDate,
							resistance]
				}, {
					columnWidth : 1,
					items : [repairResult]
				}, {
					columnWidth : 0.5,
					items : [repairBegin, outsideCheck, person.combo]
				}, {
					columnWidth : 0.5,
					items : [repairEnd, nextTime]
				}, {
					columnWidth : 1,
					items : [memo]
				}]
			})

	var editWin = new Ext.Window({
				id : 'editWin',
				width : 600,
				height : 450,
				items : [formPanel],
				modal : true,
				closeAction : 'hide'
			})

	var repairStoreCopy = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'security/findWeldingList.action'
						}),
				reader : new Ext.data.JsonReader({
							root : 'list',
							totalProperty : 'totalCount'
						}, Record)

			})
	repairStoreCopy.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							beginTime : queryStartTime.getValue(),
							endTime : queryEndTime.getValue(),
							toolCode : queryCode.getValue(),
							toolType : type,
							isMaint : (isQuery != null && isQuery) ? null : "1"
						})
			})
	repairStoreCopy.on('load', myExport)

	this.grid = grid;
	this.init = function() {
		if (isQuery != null && isQuery) {
			addBtu.setVisible(false)
			updateBtu.setVisible(false)
			deleteBtu.setVisible(false)
			grid.purgeListeners()
		} else {
			exportBtu.setVisible(false)
			grid.on('rowdblclick', updateBoiler)
		}
		queryFun()
	};
}