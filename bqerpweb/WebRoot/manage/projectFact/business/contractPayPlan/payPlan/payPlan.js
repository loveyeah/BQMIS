Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
function getDate() {
	var d, s, t;
	d = new Date();
	s = d.getFullYear().toString(10) + "-";
	t = d.getMonth() + 1;
	s += (t > 9 ? "" : "0") + t + "-";
	t = d.getDate();
	s += (t > 9 ? "" : "0") + t + " ";
	t = d.getHours();
	s += (t > 9 ? "" : "0") + t + ":";
	t = d.getMinutes();
	s += (t > 9 ? "" : "0") + t + ":";
	t = d.getSeconds();
	s += (t > 9 ? "" : "0") + t;
	return s;
}
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var sessioncode;
	var sessionname;
	var method = "add";
	Ext.Ajax.request({
		url : 'managecontract/getSessionInfo.action',
		params : {},
		method : 'post',
		waitMsg : '正在加载数据...',
		success : function(result, request) {
			var responseArray = Ext.util.JSON.decode(result.responseText);
			if (responseArray.success == true) {
				var tt = eval('(' + result.responseText + ')');
				o = tt.data;
				sessioncode = o[0];
				sessionname = o[1];

			} else {
				Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
			}
		},
		failure : function(result, request) {
			Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
		}
	});
	var method;
	var id = getParameter("id");
	var currencyType = getParameter("currencyType");
	var actAmount = getParameter("actAmount");
	var tbar = new Ext.Toolbar({
		items : [{
			id : 'btnAdd',
			text : "增加",
			iconCls : 'add',
			handler : function() {
				if (id == "") {
					Ext.Msg.alert('提示', '请先从列表中选择需要增加付款计划的合同!');
					return false;
				}
				win.show();
				form.getForm().reset();
				Ext.get('lastModifyName').dom.value = sessionname;
				Ext.get('actAmount').dom.value = actAmount;
				Ext.getCmp('currencyType').setValue(currencyType);
				method = "add";
			}
		}, '-', {
			id : 'btnSave',
			text : "修改",
			iconCls : 'update',
			handler : function() {
				var selrows = payGrid.getSelectionModel().getSelections();
				if (selrows.length > 0) {
					var record = payGrid.getSelectionModel().getSelected();
					if ((record.data.payStatu == null)
							|| (record.data.payStatu == "")) {
						form.getForm().reset();
						win.show();
						form.getForm().loadRecord(record);
						Ext.getCmp('currencyType')
								.setValue(record.data.currencyName);
						var num = Ext.get('payPrice').dom.value;
						var total = Ext.get('actAmount').dom.value;
						Ext.get('curPayRate').dom.value = ((num / total) * 100)
								.toPrecision(4).toString();
						method = "update";
					} else {
						Ext.Msg.alert('提示', '正在付款或已结算，不可修改');
					}
				} else {
					Ext.Msg.alert('提示', '请选择要修改的记录！');
				}
			}
		}, '-', {
			id : 'btndelete',
			text : "删除",
			iconCls : 'delete',
			handler : function() {
				var selrows = payGrid.getSelectionModel().getSelections();
				if (selrows.length > 0) {
					var ids = [];
					Ext.Msg.confirm('提示', '删除的数据您将不能恢复,确定要删除吗?', function(b) {
						if (b == "yes") {
							for (var i = 0; i < selrows.length; i += 1) {
								var member = selrows[i].data;
								if (member.paymentId) {
									ids.push(member.paymentId);
								}
							}
							Ext.Ajax.request({
								url : 'managecontract/deletePayPlan.action',
								params : {
									ids : ids.join(",")
								},
								method : 'post',
								waitMsg : '正在删除数据...',
								success : function(result, request) {
									Ext.Msg.alert('提示', '删除成功！');
									ds.reload();
								},
								failure : function(result, request) {
									Ext.MessageBox.alert('提示', '操作失败,请联系管理员!');
								}
							});
						}
					});
				} else {
					Ext.Msg.alert('提示', '请选择您要删除的记录!');
				}
			}
		}, '-', {
			id : 'btnQuery',
			text : "查询",
			iconCls : 'query',
			handler : function() {

			}
		}]
	});

	var item = Ext.data.Record.create([{
		name : 'paymentId'
	}, {
		name : 'conId'
	}, {
		name : 'payStatu'
	}, {
		name : 'paymentMoment'
	}, {
		name : 'memo'
	}, {
		name : 'payPrice'
	}, {
		name : 'currencyName'
	}, {
		name : 'payDate'
	}, {
		name : 'lastModifiedBy'
	}, {
		name : 'lastModifyName'
	}, {
		name : 'lastModifiedDate'
	}, {
		name : 'actAmount'
	}, {
		name : 'currencyName'
	}, {
		name : 'payRate'
	}]);
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	});
	var item_cm = new Ext.grid.ColumnModel([sm, new Ext.grid.RowNumberer({
		header : '项次号',
		width : 50,
		align : 'center'
	}), {
		header : '付款状态',
		dataIndex : 'payStatu',
		align : 'center',
		renderer : statusname
	}, {
		header : '付款阶段',
		dataIndex : 'paymentMoment',
		align : 'center'
	}, {
		header : '付款说明',
		dataIndex : 'memo',
		align : 'center'
	}, {
		header : '付款金额',
		dataIndex : 'payPrice',
		align : 'center'
	}, {
		header : '付款比例',
		dataIndex : 'payRate',
		align : 'center'
	}, {
		header : '币别',
		dataIndex : 'currencyName',
		align : 'center',
		renderer : function(v) {

			if (v == 1) {
				return "RMB";
			}
			if (v == 2) {
				return "USD";
			} else {
				return "异常";
			}

		}
	}, {
		header : '计划付款日期',
		dataIndex : 'payDate',
		align : 'center'
	}]);
	item_cm.defaultSortable = true;
	function statusname(v) {
		if (v == 2) {
			return "已结算";
		}
		if (v == "" || v == null) {
			return "未付款";
		} else {
			return "付款中";
		}
	}
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managecontract/findPayPlanList.action'
		}),
		reader : new Ext.data.JsonReader({}, item)
	});
	if ((id != null) && (id != "")) {
		ds.load({
			params : {
				conId : id
			}
		});
	}
	var payGrid = new Ext.grid.GridPanel({
		ds : ds,
		cm : item_cm,
		sm : sm,
		autoWidth : true,
		autoScroll : true,
		tbar : tbar,
		border : false
	});
	payGrid.on('rowdblclick', function(grid, rowIndex, e) {
		var record = payGrid.getSelectionModel().getSelected();
		if ((record.data.payStatu == null) || (record.data.payStatu == "")) {
			form.getForm().reset();
			win.show();
			form.getForm().loadRecord(record);
			Ext.getCmp('currencyType').setValue(record.data.currencyName);
			var num = Ext.get('payPrice').dom.value;
			var total = Ext.get('actAmount').dom.value;
			Ext.get('curPayRate').dom.value = ((num / total) * 100)
					.toPrecision(4).toString();
			method = "update";
		} else {
			Ext.Msg.alert('提示', '正在付款或已结算，不可修改');
		}
	});
	var layout = new Ext.Viewport({
		layout : 'fit',
		border : true,
		items : [{
			layout : 'fit',
			margins : '0 0 0 0',
			region : 'center',
			autoScroll : true,
			items : payGrid
		}]
	});
	var paymentId = {
		id : "paymentId",
		xtype : "hidden",
		fieldLabel : 'ID',
		width : 300,
		readOnly : true,
		name : 'pay.paymentId'

	}
	var paymentMoment = new Ext.form.TextField({
		id : 'paymentMoment',
		xtype : "textfield",
		fieldLabel : "付款阶段",
		allowBlank : false,
		name : 'pay.paymentMoment',
		type : 'textfield',
		width : 300
	});
	var currencyName = new Ext.form.ComboBox({
		fieldLabel : '币别',
		store : [['1', 'RMB'], ['2', 'USD']],
		id : 'currencyType',
		name : 'currencyType',
		valueField : "value",
		displayField : "text",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'paycurrencyType',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		width : 300,
		emptyText : '请选择'
	});
	currencyName.on('beforequery', function() {
		return false
	});
	var totalMoney = {
		id : "actAmount",
		xtype : "textfield",
		fieldLabel : '合同总金额',
		value : totalMoney,
		readOnly : true,
		width : 300,
		name : 'actAmount'
	}

	var currentpayRate = new Ext.form.NumberField({
		id : "payPrice",
		xtype : "numberfield",
		fieldLabel : '本次付款金额',
		width : 300,
		allowBlank : false,
		name : 'pay.payPrice',
		listeners : {
			change : function() {
				var num = Ext.get('payPrice').dom.value;
				var total = Ext.get('actAmount').dom.value;
				Ext.get('curPayRate').dom.value = ((num / total) * 100)
						.toPrecision(4).toString();

			}
		}
	});
	var lable = new Ext.form.Label({
		html : "<font size='2'>%</font>",
		autoWidth : true
	});
	var cbpayRate = new Ext.form.Checkbox({
		id : 'cbpayRate',
		fieldLabel : '付款比例',
		listeners : {
			check : function() {
				if (cbpayRate.checked) {
					Ext.get('curPayRate').dom.readOnly = false;
				} else {
					Ext.get('curPayRate').dom.readOnly = true;
				}
			}
		}
	});
	var curPayRate = new Ext.form.NumberField({
		id : "curPayRate",
		xtype : "numberfield",
		fieldLabel : '本次付款比例',
		width : 115,
		readOnly : true,
		name : 'curPayRate',
		listeners : {
			change : function() {
				if (cbpayRate.checked) {
					var total = Ext.get('actAmount').dom.value;
					var rate = Ext.get('curPayRate').dom.value;
					Ext.get('payPrice').dom.value = ((total * rate) / 100)
							.toString();
				}
			}
		}
	});
	var payDate = new Ext.form.TextField({
		id : 'payDate',
		fieldLabel : "计划付款时间",
		name : 'pay.payDate',
		type : 'textfield',
		style : 'cursor:pointer',
		width : 300,
		value : getDate(),
		listeners : {
			focus : function() {
				var pkr = WdatePicker({
					startDate : '%y-%M-%d 00:00:00',
					dateFmt : 'yyyy-MM-dd HH:mm:ss',
					alwaysUseStartDate : true
				});
			}
		}
	});
	var memo = {
		id : "memo",
		xtype : "textarea",
		fieldLabel : '付款说明',
		width : 298,
		name : 'pay.memo'

	}

	var lastModifyName = {
		id : "lastModifyName",
		xtype : "textfield",
		fieldLabel : '修改人',
		readOnly : true,
		width : 300
			// name : 'pay.lastModifyName'

	};
	var lastModifiedDate = new Ext.form.TextField({
		id : 'lastModifiedDate',
		fieldLabel : "修改日期",
		name : 'pay.lastModifiedDate',
		type : 'textfield',
		style : 'cursor:pointer',
		width : 300,
		readOnly : true,
		value : getDate()
	});
	var content = new Ext.form.FieldSet({
		title : '付款计划设置',
		height : '100%',
		layout : 'form',
		items : [paymentId, paymentMoment, currencyName, totalMoney,
				currentpayRate, {
					layout : 'column',
					border : false,
					items : [{
						border : false,
						columnWidth : 0.3,
						layout : 'form',
						items : [cbpayRate]
					}, {
						border : false,
						columnWidth : 0.5,
						layout : 'form',
						items : [curPayRate]
					}, {
						border : false,
						columnWidth : 0.1,
						layout : 'form',
						items : [lable]
					}]
				}, payDate, memo, lastModifyName, lastModifiedDate],
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
				var url = '';
				if (method == "add") {
					url = 'managecontract/addPayPlan.action';

				} else {
					url = 'managecontract/updatePayPlan.action';
				}
				if (!form.getForm().isValid()) {
					return false;
				}
				form.getForm().submit({
					url : url,
					method : 'post',
					params : {
						conId : id
					},
					success : function(form, action) {
						var message = eval('(' + action.response.responseText
								+ ')');
						Ext.Msg.alert("成功", message.data);
						ds.reload();
						win.hide();
					},
					failure : function(form, action) {
						Ext.Msg.alert('错误', '出现未知错误.');
					}
				})
			}

		}, {
			text : '取消',
			iconCls : 'cancer',
			handler : function() {
				form.getForm().reset();
				win.hide();
			}
		}]
	});

	var form = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		id : 'shift-form',
		labelWidth : 80,
		autoHeight : true,
		region : 'center',
		border : false,
		items : [content]
	});

	var win = new Ext.Window({
		el : 'win',
		modal : true,
		autoHeight : true,
		width : 450,
		closeAction : 'hide',
		items : [form]
	})
});