Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var balanceId = getParameter("balanceId");
	var balaFlag = getParameter("balaFlag");
	if(balanceId == ""){
	 	balanceId = parent.balanceCode;
		balaFlag = parent.balaFlagCode;
	}
	var method = "update";
	var count = 0;
	balanceId= parent.balanceCode;
	function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";
		// 初始化时间
		Year = DateIn.getYear();
		Month = DateIn.getMonth() + 1;
		Day = DateIn.getDate();
		hour = DateIn.getHours();
		min = DateIn.getMinutes();
		se = DateIn.getSeconds();
		CurrentDate = Year + "-";
		if (Month >= 10) {
			CurrentDate = CurrentDate + Month + "-";
		} else {
			CurrentDate = CurrentDate + "0" + Month + "-";
		}
		if (Day >= 10) {
			CurrentDate = CurrentDate + Day;
		} else {
			CurrentDate = CurrentDate + "0" + Day;
		}
		CurrentDate += " "
		if (hour >= 10) {
			CurrentDate = CurrentDate + hour + ":";
		} else {
			CurrentDate = CurrentDate + "0" + hour + ":";
		}
		if (min >= 10) {
			CurrentDate = CurrentDate + min + ":";
		} else {
			CurrentDate = CurrentDate + "0" + min + ":";
		}
		if (se >= 10) {
			CurrentDate = CurrentDate + se + ":";
		} else {
			CurrentDate = CurrentDate + "0" + se + ":";
		}
		return CurrentDate;
	}
	var date = new Date();
	var currentdate = ChangeDateToString(date);
	// add BY ywliu 2009/05/04
	var opman=parent.iframe1.Ext.getCmp("operateBy").getValue();
	var opmanName=parent.iframe1.document.getElementById("operateBy").value;
	var sessWorname = document.getElementById("workerName").value;
	var sessWorcode = document.getElementById("workerCode").value;
	var flag;
//	findMan(balanceId);
//	function findMan(_balanceId) {
//		if (_balanceId != "") {
//			Ext.Ajax.request({
//				url : 'managecontract/findBalanceInfo.action',
//				params : {
//					balanceId : _balanceId
//				},
//				method : 'post',
//				waitMsg : '正在加载数据...',
//				success : function(result, request) {
//					var o = eval('(' + result.responseText + ')');
//					opman = o.data.operateBy;
//					opmanName = o.data.operateName
//					flag = o.data.balaFlag;
//					if (flag == 0 || flag == 3) {
//						Ext.get("add").dom.disabled = false;
//						Ext.get("delete").dom.disabled = false;
//						Ext.get("btnSave").dom.disabled = false;
//					} else {
//						Ext.get("add").dom.disabled = true;
//						Ext.get("delete").dom.disabled = true;
//						Ext.get("btnSave").dom.disabled = true;
//					}
//				},
//				failure : function(result, request) {
//					Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
//				}
//			});
//		}
//	}

	function renderDate(value) {
		return value ? value.dateFormat('Y-m-d H:i:s') : '';
	}
	var dateColumn = ({
		header : "开票日期",
		sortable : true,
		width : 180,
		dataIndex : 'invoiceDate',
		renderer : function(value) {
			if (!value)
				return '';
			if (value instanceof Date)
				return renderDate(value);
			var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
			var reTime = /\d{2}:\d{2}:\d{2}/gi;
			var strDate = value.match(reDate);
			var strTime = value.match(reTime);
			if (!strDate)
				return "";
			strTime = strTime ? strTime : '00:00:00';
			return strDate + " " + strTime;
		}
			// editor : new Ext.form.TextField({
			// readOnly : true,
			// id : "temp",
			// listeners : {
			// focus : function() {
			// WdatePicker({
			// startDate : '%y-%M-%d 00:00:00',
			// dateFmt : 'yyyy-MM-dd HH:mm:ss',
			// alwaysUseStartDate : true,
			// onpicked : function() {
			// invoiceGrid.getSelectionModel().getSelected().set(
			// "invoiceDate", Ext.get("temp").dom.value);
			// }
			// });
			// }
			// }
			// })
	})
	var tbar = new Ext.Toolbar({
		items : [{
			id : 'add',
			text : "增加",
			iconCls : 'add',
			handler : addRow
		}, '-', {
			id : 'delete',
			text : "删除",
			iconCls : 'delete',
			handler : deleteRecords
//			function() {
//				var selected = invoiceGrid.getSelectionModel().getSelections();
//				if (selected.length > 0) {
//					for (i = 0; i < selected.length; i++) {
//						if (selected[i].data.invoiceNo == "") {
//							invoiceGrid.getStore().remove(selected[i]);
//						} else {
//							var delId = "";
//							delId = delId + selected[i].data.invoiceNo;
//							delId += ",";
//							if (delId.length > 0) {
//								delId = delId.substring(0, delId.length - 1);
//								Ext.Ajax.request({
//									url : 'managecontract/delBalInvoice.action',
//									params : {
//										delId : delId
//									},
//									method : 'post',
//									success : function(result, request) {
//										invoice_ds.load({
//											params : {
//												balanceId : balanceId
//											}
//										});
//									},
//									failure : function(result, request) {
//										Ext.Msg.alert('提示信息', '删除失败');
//									}
//								});
//							}
//						}
//					}
//				} else {
//					Ext.Msg.alert('提示信息', '请选择要删除的记录');
//				}
//
//			}
		}, '-', {
			id : 'btnSave',
			text : "保存",
			iconCls : 'save',
			handler : save
		}, '-', {
			id : 'btnCan',
			text : "取消",
			iconCls : 'cancer',
			handler : cancer
		}]
		
	});
	var invoiceItem = Ext.data.Record.create([{
		name : 'invoiceNo'
	}, {
		name : 'balanceId'
	}, {
		name : 'invoiceCode'
	}, {
		name : 'invoiceId'
	}, {
		name : 'invoiceDate'
	}, {
		name : 'invoiceName'
	}, {
		name : 'invoicePrice'
	}, {
		name : 'drawerBy'
	}, {
		name : 'operateBy'
	}, {
		name : 'drawerName'
	}, {
		name : 'operateName'
	}, {
		name : 'memo'
	}]);
	var invoice_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) {

			}
		}
	});
	// var invoice_sm = new Ext.grid.RowSelectionModel({
	// singleSelect : false,
	// listeners : {
	// rowselect : function(sm, row, rec) {
	//
	// }
	// }
	// });

	var invoice_cm = new Ext.grid.ColumnModel([invoice_sm,
			new Ext.grid.RowNumberer({
				header : '项次号',
				width : 50,
				align : 'center'
			}), {
				header : '票据编号',
				dataIndex : 'invoiceCode',
				align : 'center',
				editor : new Ext.form.TextField({
					allowBlank : false
				})
			},
			// {
			// header : '票据类型',
			// dataIndex : 'invoiceId',
			// align : 'center',
			// editor : new Ext.form.TextField({
			// allowBlank : false
			// })
			// }
			{
				header : '票据类型',
				dataIndex : 'invoiceId',
				align : 'center',
				editor : new Ext.form.TextField({
				})
//				editor : new Ext.form.ComboBox({
//					typeAhead : true,
//					triggerAction : 'all',
//					readOnly:true,
//					// transform : 'light',
//					listClass : 'x-combo-list-small',
//					id : 'invoiceId',
//					name : 'invoiceId',
//					store : [['1', 'aa'], ['2', 'bb']],
//					valueField : "value",
//					displayField : "text",
//					mode : 'local',
//					// forceSelection : true,
//					// hiddenName : 'invoiceId',
//					selectOnFocus : true
//				}),
//				renderer : function(v) {
//					if ("1" == v)
//						return "aa";
//					if ("2" == v)
//						return "bb";
//				}
			},  {
				header : '票据说明',
				dataIndex : 'invoiceName',
				align : 'center',
				editor : new Ext.form.TextField({
					allowBlank : false
				})
			}, {
				header : '票据金额',
				dataIndex : 'invoicePrice',
				align : 'center',
				editor : new Ext.form.NumberField({
					allowBlank : false,
					allowNegative : false,
					maxValue : 1000000000
				})
			}, {
				header : '开票人',
				dataIndex : 'drawerBy',
				align : 'center',
				renderer : function(v) {
					return sessWorname
				}
			}, dateColumn,{
				header : '经办人',
				dataIndex : 'operateBy',
				hidden : true,
				align : 'center',
				renderer : function(v) {
					return opmanName
				}
				
			}]);
	invoice_cm.defaultSortable = true;
	var invoice_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managecontract/findBalInvoice.action'
		}),
		reader : new Ext.data.JsonReader({}, invoiceItem)
	});
 
	if (balanceId != "") {
		invoice_ds.load({
			params : {
				balanceId : balanceId
			}
		});
	}

	var invoiceGrid = new Ext.grid.EditorGridPanel({
		store : invoice_ds,
		cm : invoice_cm,
		sm : invoice_sm,
		tbar : tbar,
		frame : false,
		border : false,
		// enableColumnMove : false,
		// autoExpandColumn : 1,
		// autoExpandMax : 420,
		autoWidth : true,
		autoScroll : true,
		clicksToEdit : 1
	});
	// invoiceGrid.addListener('rowclick', function() {
	// method = "update";
	// })
//	function save() {
//		if (method == "add") {
//			var d = "["
//			var rec = invoice_ds.getAt(count);
//			if (rec.get("invoiceNo").trim == ""
//					|| rec.get("invoiceId").trim == ""
//					|| rec.get("invoicePrice") == "") {
//				Ext.MessageBox.alert('提示信息', '票号，票据类型，开票金额不能为空');
//				return;
//			}
//			var d = d + "{'invoiceNo':'" + rec.get("invoiceNo")
//					+ "','invoiceId':'" + rec.get("invoiceId")
//					+ "','invoiceName':'" + rec.get("invoiceName")
//					+ "','invoicePrice':'" + rec.get("invoicePrice")
//					+ "','drawerBy':'" + rec.get("drawerBy")
//					+ "','operateBy':'" + rec.get("operateBy") + "'}]"
//			Ext.Ajax.request({
//				url : 'managecontract/addBalInvoice.action',
//				method : 'post',
//				params : {
//					str : d,
//					balanceId : balanceId
//				},
//				success : function() {
//					invoice_ds.rejectChanges();
//					invoice_ds.load({
//						params : {
//							balanceId : balanceId
//						}
//					});
//				},
//				failure : function() {
//					Ext.Msg.alert('提示信息', '增加失败！');
//				}
//			})
//		} else {
//			var _str = "[";
//			var record = invoiceGrid.getStore().getModifiedRecords();
//			if (record.length > 0) {
//				for (var i = 0; i < record.length; i++) {
//					var _data = record[i];
//					_str = _str + "{'invoiceNo':'" + _data.get("invoiceNo")
//							+ "','invoiceId':'" + _data.get("invoiceId")
//							+ "','invoiceName':'" + _data.get("invoiceName")
//							+ "','invoicePrice':'" + _data.get("invoicePrice")
//							+ "','drawerBy':'" + _data.get("drawerBy")
//							+ "','operateBy':'" + _data.get("operateBy")
//							+ "'},"
//				}
//				if (_str.length > 1) {
//					_str = _str.substring(0, _str.length - 1);
//				}
//				_str = _str + "]";
//			} else {
//				Ext.Msg.alert('提示信息', "没有对数据进行任何修改！");
//			}
//			Ext.Ajax.request({
//				url : 'managecontract/updateBalInvoice.action',
//				method : 'post',
//				params : {
//					str : _str,
//					balanceId : balanceId
//				},
//				success : function() {
//					invoice_ds.load({
//						params : {
//							balanceId : balanceId
//						}
//					});
//				},
//				failure : function() {
//					Ext.Msg.alert('提示信息', '修改失败！');
//				}
//			})
//		}
//	}
	
	// 删除记录
	var ids = new Array();
	function deleteRecords() {
		invoiceGrid.stopEditing();
		var centerGridsm = invoiceGrid.getSelectionModel();
		var selected = centerGridsm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("invoiceNo") != null) {
					ids.push(member.get("invoiceNo"));
				}
				invoiceGrid.getStore().remove(member);
				invoiceGrid.getStore().getModifiedRecords().remove(member);
			}
			// resetLine();
		}
	};
	// 保存
	function save() {
		invoiceGrid.stopEditing();
		var modifyRec = invoiceGrid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			if (!confirm("确定要保存修改吗?"))
				return;
			// var newData = new Array();
			var updateData = new Array();
			for (var i = 0; i < modifyRec.length; i++) {
				// if (modifyRec[i].get("id") == "") {
				// newData.push(modifyRec[i].data);
				// } else {
				updateData.push(modifyRec[i].data);
				// }
			}

			Ext.Ajax.request({
			url : 'managecontract/updateBalInvoice.action',
				method : 'post',
				params : {
					// isAdd : Ext.util.JSON.encode(newData),
					isUpdate : Ext.util.JSON.encode(updateData),
					isDelete : ids.join(",")
				},
				success : function(result, request) {
					var o = eval('(' + result.responseText + ')');
					Ext.MessageBox.alert('提示信息', o.msg);
					invoice_ds.rejectChanges();
					ids = [];
					invoice_ds.reload();
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('提示信息', '未知错误！')
				}
			})
		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}
	// 取消
	function cancer() {
		// addBy Liuyingwen 09/04/23  invoiceGrid.getStore().getModifiedRecords()
		if(invoiceGrid.getStore().getModifiedRecords() != "") {
			var modifyRec = invoiceGrid.getStore().getModifiedRecords();
			if (modifyRec.length > 0 || ids.length > 0) {
				if (!confirm("确定要放弃修改吗"))
					return;
				invoice_ds.reload();
				invoice_ds.rejectChanges();
				ids = [];
			} else {
				invoice_ds.reload();
				invoice_ds.rejectChanges();
				ids = [];
			}
		}
	}
//新增
	function addRow() {
		if (balanceId == "") {
			Ext.Msg.alert('提示信息', '请先填写合同结算申请！');
			return
		}
		method = "add";
		count = invoiceGrid.getStore().getCount();
		var v = new invoiceItem({
			balanceId:balanceId,
			invoiceCode : "",
			invoiceId : "",
			invoiceDate : currentdate,
			invoiceName : "",
			invoicePrice : "",
			drawerBy : sessWorcode,
			operateBy : opman,
			memo : ""
			
		});
		invoiceGrid.stopEditing();
		invoice_ds.insert(count, v);
		invoice_sm.selectRow(count);
		invoiceGrid.startEditing(count, 0);

	}
	var layout = new Ext.Viewport({
		layout : 'fit',
		border : true,
		items : [{
			layout : 'fit',
			margins : '0 0 0 0',
			region : 'center',
			autoScroll : true,
			items : invoiceGrid
		}]
	});
		// Ext.invoiceGrid.CheckColumn.prototype = {
		// init : function(grid) {
		// this.grid = grid;
		// this.grid.on('render', function() {
		// var view = this.grid.getView();
		// view.mainBody.on('mousedown', this.onMouseDown, this);
		// }, this);
		// },
		//
		// onMouseDown : function(e, t) {
		// if (t.className
		// && t.className.indexOf('x-grid3-cc-' + this.id) != -1) {
		// e.stopEvent();
		// var index = this.grid.getView().findRowIndex(t);
		// var record = this.grid.store.getAt(index);
		// record.set(this.dataIndex, !record.data[this.dataIndex]);
		// }
		// },
		//
		// renderer : function(v, p, record) {
		// p.css += ' x-grid3-check-col-td';
		// return '<div class="x-grid3-check-col' + (v ? '-on' : '')
		// + ' x-grid3-cc-' + this.id + '">&#160;</div>';
		// }
		// };
		if(balaFlag == 1 || balaFlag == 2 || balaFlag == ""){
		tbar.setDisabled(true);
		}
});