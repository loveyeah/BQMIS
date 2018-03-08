Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t;
		return s;
	};

	var sNextTime = new Ext.form.TextField({
				id : "sNextTime",
				fieldLabel : '下次检修时间',
				width : 80,
				value : new Date().format('Y-m'),
				name : 'repair.sNextTime',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M',
									dateFmt : 'yyyy-MM',
									alwaysUseStartDate : true
								});
					}
				}
			});

	var record = Ext.data.Record.create([{
				name : 'boilerRepairId',
				mapping : 0
			}, {
				name : 'boilerId',
				mapping : 1
			}, {
				name : 'type',
				mapping : 2
			}, {
				name : 'boilerName',
				mapping : 3
			}, {
				name : 'repairBegin',
				mapping : 4
			}, {
				name : 'repairEnd',
				mapping : 5
			}, {
				name : 'reportNo',
				mapping : 6
			}, {
				name : 'repairUnit',
				mapping : 7
			}, {
				name : 'repairResult',
				mapping : 8
			}, {
				name : 'nextTime',
				mapping : 9
			}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'security/getSpJPressureRepairList.action'
			});

	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, record);

	var store = new Ext.data.Store({
				proxy : dataProxy,
				reader : theReader
			});

	var sm = new Ext.grid.CheckboxSelectionModel();

	var grid = new Ext.grid.GridPanel({
				region : "center",
				layout : 'fit',
				store : store,
				columns : [sm, new Ext.grid.RowNumberer({
									header : '行号',
									width : 35,
									align : 'left'
								}), {
							header : "检验记录ID",
							width : 75,
							sortable : true,
							dataIndex : 'boilerRepairId',
							hidden : true
						}, {
							header : "锅炉Id",
							width : 200,
							sortable : true,
							dataIndex : 'boilerId',
							hidden : true
						}, {
							header : "设备名称",
							width : 200,
							sortable : true,
							dataIndex : 'boilerName'
						}, {
							header : "检验开始时间",
							width : 200,
							sortable : true,
							dataIndex : 'repairBegin'
						}, {
							header : "检验结束时间",
							width : 200,
							sortable : true,
							dataIndex : 'repairEnd'
						}, {
							header : "检验报告编号",
							width : 200,
							sortable : true,
							dataIndex : 'reportNo'
						}, {
							header : "检验单位",
							width : 200,
							sortable : true,
							dataIndex : 'repairUnit'
						}, {
							header : "检验结果",
							width : 200,
							sortable : true,
							dataIndex : 'repairResult',
							renderer : function(v) {
								if (v == '1') {
									return "一级";
								}
								if (v == '2') {
									return "二级";
								}
								if (v == '3') {
									return "三级";
								}
								if (v == '4') {
									return "四级";
								}
							}
						}, {
							header : "下次检验时间",
							width : 200,
							sortable : true,
							dataIndex : 'nextTime'
						}],
				sm : sm,
				autoSizeColumns : true,
				viewConfig : {
					forceFit : true
				},
				tbar : ["下次检修时间:", sNextTime, '-', {
							text : "查询",
							iconCls : 'query',
							minWidth : 60,
							handler : query
						}, '-', {
							text : "新增",
							iconCls : 'add',
							minWidth : 60,
							handler : addRecord
						}, '-', {
							text : "修改",
							iconCls : 'update',
							minWidth : 60,
							handler : updateRecord
						}, '-', {
							text : "删除",
							iconCls : 'delete',
							minWidth : 60,
							handler : deleteRecord
						}],
				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : store,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						})
			});
	grid.on("rowdblclick", updateRecord);

	var boilerRepairId = new Ext.form.Hidden({
				id : "boilerRepairId",
				fieldLabel : '检验记录Id',
				width : 150,
				name : 'repair.boilerRepairId'
			});

	var boilerId = new Ext.form.Hidden({
				id : "boilerId",
				fieldLabel : '锅炉ID',
				readOnly : true,
				name : 'repair.boilerId'
			});

	var boilerName = new Ext.form.TriggerField({
		id : 'boilerName',
		fieldLabel : '设备名称',
		allowBlank : false,
		name : 'boilerName',
		width : 150,
		onTriggerClick : function() {
			var args = {
				selectModel : 'single',
				rootNode : {
					id : "root",
					text : '锅炉设备树'
				}
			}
			var url = "/power/run/securityproduction/BoilerRepair/blockAndType.jsp";
			var rvo = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:250px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (typeof(rvo) != "undefined") {

				boilerId.setValue(rvo.code);
				boilerName.setValue(rvo.name);
			}
		}
	})

	var repairBegin = new Ext.form.TextField({
				id : "repairBegin",
				fieldLabel : '检验开始时间',
				width : 150,
				name : 'repair.repairBegin',
				value : getDate(),
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : true
								});
					}
				}
			});

	var repairEnd = new Ext.form.TextField({
				id : "repairEnd",
				fieldLabel : '检验结束时间',
				width : 150,
				name : 'repair.repairEnd',
				value : getDate(),
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : true
								});
					}
				}
			});

	var reportNo = new Ext.form.TextField({
				id : "reportNo",
				fieldLabel : '检验报告编号',
				allowBlank : false,
				width : 150,
				name : 'repair.reportNo'
			});

	var repairUnit = new Ext.form.TextField({
				id : "repairUnit",
				fieldLabel : '检验单位',
				width : 150,
				name : 'repair.repairUnit'
			});
	var repairResult = new Ext.form.ComboBox({
				fieldLabel : '检验结果',
				store : new Ext.data.SimpleStore({
							fields : ['value', 'text'],
							data : [['1', '一级'], ['2', '二级'], ['3', '三级'],
									['4', '四级']]
						}),
				id : 'repairResult',
				name : 'repairResult',
				valueField : "value",
				displayField : "text",
				mode : 'local',
				typeAhead : true,
				allowBlank : false,
				forceSelection : true,
				hiddenName : 'repair.repairResult',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				width : 150
			});

	var nextTime = new Ext.form.TextField({
				id : "nextTime",
				fieldLabel : '下次检验时间',
				allowBlank : false,
				width : 150,
				name : 'repair.nextTime',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : true
								});
					}
				}
			});

	var addpanel = new Ext.FormPanel({
				frame : true,
				labelAlign : 'center',
				labelWidth : 100,
				closeAction : 'hide',
				fileUpload : true,
				title : '增加/修改锅炉或压力容器检修',
				items : [{
					layout : 'column',
					items : [{// 第一列
						columnWidth : 0.5,
						layout : 'form',
						items : [boilerRepairId, boilerId, boilerName,
								repairUnit, repairResult, reportNo]
					}, {	// 第二列
								columnWidth : 0.5,
								layout : 'form',
								items : [repairBegin, repairEnd, nextTime]
							}]

				}]
			});

	function query() {
		store.baseParams = {
			sNextTime : sNextTime.getValue(),
			type : '1',
			isMaint : '1'// add by ltong 20100428
		}
		store.load({
					start : 0,
					limit : 18
				})
	}

	var win = new Ext.Window({
				width : 550,
				height : 230,
				buttonAlign : "center",
				items : [addpanel],
				layout : 'fit',
				closeAction : 'hide',
				resizable : false,
				modal : true,
				buttons : [{
					text : '保存',
					iconCls : 'save',
					handler : function() {

						var myurl = "";
						if (boilerRepairId.getValue() == "") {
							myurl = "security/saveSpJPressureRepair.action";
						} else {
							myurl = "security/updateSpJPressureRepair.action";
						}
						addpanel.getForm().submit({
							method : 'POST',
							url : myurl,
							params : {
								type : '1'
							},
							success : function(form, action) {
								var o = eval("(" + action.response.responseText
										+ ")");
								Ext.Msg.alert("注意", o.msg);
								if (o.msg.indexOf("成功") != -1) {
									query();
									win.hide();
								}
							},
							faliue : function() {
								Ext.Msg.alert('错误', '出现未知错误.');
							}
						});
						// Ext.Ajax.request({
						// url : myurl,
						// method : 'post',
						// params : {
						// type: '1'
						// },
						// success : function(form, options) {
						// var obj = Ext.util.JSON
						// .decode(form.responseText)
						// if (obj && obj.msg) {
						// Ext.Msg.alert('提示', obj.msg);
						// }
						// if (o.msg.indexOf("成功") != -1) {
						// query();
						// win.hide();
						// }
						// },
						// failure : function(result, request) {
						// Ext.MessageBox.alert('提示信息', '操作失败！')
						// }
						// })
					}
				}, {
					text : '取消',
					iconCls : 'cancer',
					handler : function() {
						win.hide();
					}
				}]

			});

	function addRecord() {
		addpanel.getForm().reset();
		win.show();
		addpanel.setTitle("增加锅炉或压力容器检修信息");
	}

	function updateRecord() {
		if (grid.selModel.hasSelection()) {
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				win.show();
				var record = grid.getSelectionModel().getSelected();
				addpanel.getForm().reset();
				addpanel.getForm().loadRecord(record)
				addpanel.setTitle("修改锅炉或压力容器检修信息");
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}

	function deleteRecord() {
		var records = grid.selModel.getSelections();
		var boilerRepairId = [];
		if (records.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {

			for (var i = 0; i < records.length; i += 1) {
				var member = records[i];
				if (member.get("boilerRepairId")) {
					boilerRepairId.push(member.get("boilerRepairId"));
				} else {
					store.remove(store.getAt(i));
				}
			}
			Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(buttonobj) {
						if (buttonobj == "yes") {
							Ext.lib.Ajax.request('POST',
									'security/deleteSpJPressureRepair.action',
									{
										success : function(action) {
											Ext.Msg.alert("提示", "删除成功！");
											query();
										},
										failure : function() {
											Ext.Msg.alert('错误', '删除时出现未知错误.');
										}
									}, 'boilerRepairId=' + boilerRepairId);
						}
					});
		}
	}
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "fit",
				items : [grid]
			});
	query()
});