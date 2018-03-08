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

	// 从list页面取值
	var id = getParameter("id");
	var unliquidate = getParameter("unliquidate");
	// alert(window.location);
	var modifyId = getParameter("conModifyId");
	var contractNo = getParameter("contractNo");
	var issum = 'Y';
	var sessWorname;
	Ext.Ajax.request({
		url : 'managecontract/getSession.action',
		params : {},
		method : 'post',
		waitMsg : '正在加载数据...',
		success : function(result, request) {
			var responseArray = Ext.util.JSON.decode(result.responseText);
			if (responseArray.success == true) {
				var tt = eval('(' + result.responseText + ')');
				o = tt.data;
				sessWorname = o[1];

			} else {
				Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
			}
		},
		failure : function(result, request) {
			Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
		}
	});

	if (id != "") {
		Ext.Ajax.request({
			url : 'managecontract/findConModel.action',
			params : {
				conid : id
			},
			method : 'post',
			waitMsg : '正在加载数据...',
			success : function(result, request) {
				var o = eval('(' + result.responseText + ')');
				form.getForm().loadRecord(o);
				 if(o.data.itemId != null){
				itemBox.setValue((o.data.itemId == "zzfy")?"生产成本":"劳务成本");
				}
				if(o.data.filePath == null || o.data.filePath == ""){
							Ext.getCmp("btnView").setVisible(false);
							}
				if (o.data.isSum == "Y") {
					Ext.get('isTotal').dom.checked = true;
					Ext.get('isTotal').dom.disabled = true
				} else {
					Ext.get('isTotal').dom.checked = false;
				}
				oriFileName.setValue = (o.data.filePath == null?"":o.data.filePath);
						if(o.data.filePath == null || o.data.filePath == ""){
						Ext.getCmp("btnView").setVisible(false);
						}
				annex_ds.load({
					params : {
						conid : id,
						type : "CONATT"
					}
				});
				Credent_ds.load({
					params : {
						conid : id,
						type : "CONEVI"
					}
				});
				Material_ds.load({
					params : {
						contractNo : contractNo
					}
				});
			},
			failure : function(result, request) {
				Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
			}
		});
	};
	// 币别
	var typeBox = new Ext.form.TextField({
		id : 'currencyName',
		name : 'con.currencyName',
		fieldLabel : '币别',
		readOnly : true,
		anchor : '75%'
	})
	// 合同类别
	var contypeBox = new Ext.form.TextField({
		id : 'conTypeName',
		fieldLabel : '合同类别',
		readOnly : true,
		anchor : '85%'
	})
	// 总金额
	var actAmount = new Ext.form.NumberField({
		id : 'actAmount',
		name : 'con.actAmount',
		fieldLabel : '总金额',
		readOnly : true,
		anchor : '85%'
	});
	// 有无总金额
	var isTotal = new Ext.form.Checkbox({
		id : 'isTotal',
		fieldLabel : '有无总金额',
		checked : false,
		readOnly : true,
		anchor : "20%"
	});

	// 部门负责人
	var unitBox = new Ext.form.TextField({
		fieldLabel : '部门负责人',
		id : 'operateLeadName',
		name : 'con.operateLeadBy',
		xtype : 'textfield',
		readOnly : true,
		anchor : "85%"
	});
	// 供应商
	var client = new Ext.form.TextField({
		id : 'clientName',
		xtype : 'textfield',
		fieldLabel : '供应商',
		readOnly : true,
		anchor : '85%'

	})
	var cliendIdForm = new Ext.form.TextField({
		id : 'cliendIdForm',
		name : 'con.cliendId',
		xtype : 'textfield',
		fieldLabel : '供应商',
		readOnly : true,
		anchor : '85%'

	})
	// 费用来源
	var itemBox = new Ext.form.TextField({
		id : 'itemId',
		store : [['1', '电力供应'], ['2', '公司']],
		xtype : 'textfield',
		fieldLabel : '费用来源',
		readOnly : true,
		anchor : '85%'
	})
	// 合同开始时间

	var startDate = new Ext.form.TextField({
		id : 'startDate',
		fieldLabel : "合同履行开始时间",
		name : 'con.startDate',
		style : 'cursor:pointer',
		readOnly : true,
		anchor : "85%"
	});
	// 合同结束时间
	var endDate = new Ext.form.TextField({
		id : 'endDate',
		fieldLabel : "合同履行结束时间",
		name : 'con.endDate',
		style : 'cursor:pointer',
		readOnly : true,
		anchor : "85%"
	});
	// 合同编号
	var conttreesNo = new Ext.form.TextField({
		id : 'conttreesNo',
		xtype : 'textfield',
		fieldLabel : '合同编号',
		readOnly : true,
		anchor : '85%'
	})
	var conttreesNoForm = new Ext.form.TextField({
		id : 'conttreesNoForm',
		name : 'con.conttreesNo',
		xtype : 'textfield',
		fieldLabel : '合同编号',
		readOnly : true,
		anchor : '85%'
	})
	// 合同名称
	var contractName = new Ext.form.TextField({
		id : 'contractName',
		xtype : 'textfield',
		fieldLabel : '合同名称',
		readOnly : true,
		anchor : '92.5%'
	})
	var contractNameForm = new Ext.form.TextField({
		id : 'contractNameForm',
		xtype : 'textfield',
		fieldLabel : '合同名称',
		readOnly : true,
		allowBlank : false,
		anchor : '85%'
	})
	// 合同年份
	var conYear = new Ext.form.TextField({
		id : 'conYear',
		name : 'con.conYear',
		xtype : 'numberfield',
		fieldLabel : '合同年份',
		readOnly : true,
		anchor : '85%'
	})
	// 合同简介
	var conAbstract = new Ext.form.TextField({
		id : 'conAbstract',
		name : 'con.conAbstract',
		xtype : 'textarea',
		fieldLabel : '合同简介',
		readOnly : true,
		height : 120,
		anchor : '92.5%'
	})
	// 经办人
	var operateName = new Ext.form.TextField({
		id : 'operateName',
		xtype : 'textField',
		fieldLabel : '经办人',
		readOnly : true,
		anchor : '85%'
	})
	// 经办部门
	var operateDepName = new Ext.form.TextField({
		id : 'operateDepName',
		xtype : 'textfield',
		fieldLabel : '经办部门',
		readOnly : true,
		anchor : '85%'
	})
	// 起草人
	var entryName = new Ext.form.TextField({
		id : 'entryName',
		xtype : 'textfield',
		fieldLabel : '起草人',
		readOnly : true,
		anchor : '85.5%'
	});
	// 起草时间
	var entryDate = new Ext.form.TextField({
		id : 'entryDate',
		fieldLabel : "起草日期",
		name : 'con.entryDate',
		readOnly : true,
		anchor : "85%"
	});
	// 合同文本
	var oriFileName = new Ext.form.TextField({
		id : "filePath",
		xtype : "textfield",
		fieldLabel : '合同文本',
//		inputType : "file",
		readOnly : true,
		height : 22,
		anchor : "95%"
	})
	// 查看
	var btnView = new Ext.Button({
		id : 'btnView',
		text : '查看',
		handler : function() {
			if (id == "") {
				Ext.Msg.alert('提示', '请选择合同');
				return false;
			}
			window.open("/power/managecontract/showConFile.action?conid=" + id
					+ "&type=CON");
		}
	});
	var formtbar = new Ext.Toolbar({
		items : [{
			id : 'btnUpdate',
			text : "浏览变更合同信息",
			iconCls : 'list',
			handler : function() {
				url = "../../../../../../manage/contract/business/conBaseInfo/conInfoend.jsp?conModifyId="
					+ modifyId+"&conId="+id;

				var o = window.showModalDialog(url, '',
						'dialogWidth=900px;dialogHeight=700px;status=no');
			}
		}, '-', {
			id : 'btnTerminate',
			text : "履行结束",
			iconCls : 'stop',
			handler : function() {
				checkInput();
			}
		}]
	});
	function checkInput() {
		var endDate = Ext.get('endDate').dom.value;
		var nowDate = new Date().format("Y-m-d H:i:s");
		var actAmount = Ext.get("actAmount").dom.value;

		var isAmount = (actAmount != null && unliquidate != 0);
		var isAmountNull = (actAmount != null && unliquidate == 0);

		if (endDate != "" && (endDate > nowDate) && isAmount) {
			Ext.Msg.confirm("提示", "尚未到合同履行结束时间并且有未结算金额，是否提前验收终止合同？",
					function(b) {
						if (b == 'yes') {
							conterminateForm.getForm().reset();
							win.show();
							Ext.get('cliendIdForm').dom.value = Ext
									.get('clientName').dom.value;
							Ext.get('conttreesNoForm').dom.value = Ext
									.get('conttreesNo').dom.value;
							Ext.get('contractNameForm').dom.value = Ext
									.get('contractName').dom.value;
							Ext.get('terminateByName').dom.value = sessWorname;
							win.setPosition(200, 100);
						} else {
							return false;
						}
					});
		} else if (endDate != "" && (endDate > nowDate) && isAmountNull) {
			Ext.Msg.confirm("提示", "尚未到合同履行结束时间，是否提前验收终止合同？", function(b) {
				if (b == 'yes') {
					conterminateForm.getForm().reset();
					win.show();
					Ext.get('cliendIdForm').dom.value = Ext.get('clientName').dom.value;
					Ext.get('conttreesNoForm').dom.value = Ext
							.get('conttreesNo').dom.value;
					Ext.get('contractNameForm').dom.value = Ext
							.get('contractName').dom.value;
					Ext.get('terminateByName').dom.value = sessWorname;
					win.setPosition(200, 100);
				} else {
					return false;
				}
			});
		} else if (endDate != "" && (endDate <= nowDate) && isAmountNull) {
			Ext.Msg.confirm("提示", "合同履行结束时间已到，是否验收终止合同？", function(b) {
				if (b == 'yes') {
					conterminateForm.getForm().reset();
					win.show();
					Ext.get('cliendIdForm').dom.value = Ext.get('clientName').dom.value;
					Ext.get('conttreesNoForm').dom.value = Ext
							.get('conttreesNo').dom.value;
					Ext.get('contractNameForm').dom.value = Ext
							.get('contractName').dom.value;
					Ext.get('terminateByName').dom.value = sessWorname;
					win.setPosition(200, 100);
				} else {
					return false;
				}
			});
		} else if (endDate != "" && (endDate <= nowDate) && isAmount) {
			Ext.Msg.confirm("提示", "合同履行结束时间已到,但有未结算金额，是否验收终止合同？", function(b) {
				if (b == 'yes') {
					conterminateForm.getForm().reset();
					win.show();
					Ext.get('cliendIdForm').dom.value = Ext.get('clientName').dom.value;
					Ext.get('conttreesNoForm').dom.value = Ext
							.get('conttreesNo').dom.value;
					Ext.get('contractNameForm').dom.value = Ext
							.get('contractName').dom.value;
					Ext.get('terminateByName').dom.value = sessWorname;
					win.setPosition(200, 100);
				} else {
					return false;
				}
			});
		} else if (endDate == "" && isAmount) {
			Ext.Msg.confirm("提示", "本合同无履行结束时间,但有未结算金额，是否验收终止合同？", function(b) {
				if (b == 'yes') {
					conterminateForm.getForm().reset();
					win.show();
					Ext.get('cliendIdForm').dom.value = Ext.get('clientName').dom.value;
					Ext.get('conttreesNoForm').dom.value = Ext
							.get('conttreesNo').dom.value;
					Ext.get('contractNameForm').dom.value = Ext
							.get('contractName').dom.value;
					Ext.get('terminateByName').dom.value = sessWorname;
					win.setPosition(200, 100);
				} else {
					return false;
				}
			});
		} else if (endDate == "" && isAmountNull) {
			Ext.Msg.confirm("提示", "本合同无履行结束时间，是否验收终止合同？", function(b) {
				if (b == 'yes') {
					conterminateForm.getForm().reset();
					win.show();
					Ext.get('cliendIdForm').dom.value = Ext.get('clientName').dom.value;
					Ext.get('conttreesNoForm').dom.value = Ext
							.get('conttreesNo').dom.value;
					Ext.get('contractNameForm').dom.value = Ext
							.get('contractName').dom.value;
					Ext.get('terminateByName').dom.value = sessWorname;
					win.setPosition(200, 100);
					
				} else {
					return false;
				}
			});
		}
	}
	var form = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		width : Ext.get('div_lay').getWidth(),
		autoHeight : true,
		region : 'center',
		border : false,
		tbar : formtbar,
		items : [new Ext.form.FieldSet({
			title : '合同基本信息',
			collapsible : true,
			height : '100%',
			layout : 'form',
			items : [{
				border : false,
				layout : 'column',
				items : [{
					border : false,
					layout : 'form',
					columnWidth : 1,
					labelWidth : 110,
					items : [contractName]
				}, {
					columnWidth : .5,
					layout : 'form',
					border : false,
					labelWidth : 110,
					items : [contypeBox]
				}, {
					columnWidth : .5,
					layout : 'form',
					labelWidth : 110,
					border : false,
					items : [conYear]
				}, {
					border : false,
					layout : 'form',
					columnWidth : .5,
					labelWidth : 110,
					items : [conttreesNo]
				}, {
					border : false,
					layout : 'form',
					columnWidth : .5,
					labelWidth : 110,
					items : [client]
				}, {
					border : false,
					layout : 'form',
					columnWidth : .5,
					labelWidth : 110,
					items : [itemBox, operateDepName]
				}, {
					border : false,
					layout : 'form',
					columnWidth : .5,
					labelWidth : 110,
					items : [operateName, unitBox]
				}, {
					border : false,
					layout : 'form',
					columnWidth : 0.2,
					labelWidth : 110,
					items : [isTotal]
				}, {
					border : false,
					layout : 'form',
					columnWidth : 0.3,
					labelWidth : 80,
					items : [typeBox]
				}, {
					border : false,
					layout : 'form',
					columnWidth : 0.5,
					labelWidth : 110,
					items : [actAmount]
				}, {
					columnWidth : .5,
					layout : 'form',
					labelWidth : 110,
					border : false,
					items : [startDate]
				}, {
					columnWidth : .5,
					layout : 'form',
					labelWidth : 110,
					border : false,
					items : [endDate]
				}, {
					border : false,
					layout : 'form',
					columnWidth : 1,
					labelWidth : 110,
					items : [conAbstract]
				}, {
					columnWidth : .5,
					layout : 'form',
					labelWidth : 110,
					border : false,
					items : [entryName]
				}, {
					columnWidth : .5,
					layout : 'form',
					labelWidth : 110,
					border : false,
					items : [entryDate]
				}, {
					border : false,
					layout : 'form',
					columnWidth : 0.4,
					labelWidth : 110,
					items : [oriFileName]
				}, {
					border : false,
					layout : 'form',
					columnWidth : 0.2,
					labelWidth : 80,
					items : [btnView]
				}]
			}]
		})]
	});
	var annex_item = Ext.data.Record.create([{
		name : 'conDocId'
	}, {
		name : 'keyId'
	}, {
		name : 'docName'
	}, {
		name : 'docMemo'
	}, {
		name : 'oriFileName'
	}, {
		name : 'oriFileExt'
	}, {
		name : 'lastModifiedDate'
	}, {
		name : 'lastModifiedBy'
	}, {
		name : 'lastModifiedName'
	}, {
		name : 'docType'
	}, {
		name : 'oriFile'
	}]);
	var annex_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});
	var annex_item_cm = new Ext.grid.ColumnModel([annex_sm,
			new Ext.grid.RowNumberer({
				header : '项次号',
				width : 50,
				align : 'center'
			}), {
				header : '名称',
				dataIndex : 'docName',
				align : 'center'
			}, {
				header : '备注',
				dataIndex : 'docMemo',
				align : 'center'
			}, {
				header : '原始文件',
				dataIndex : 'oriFile',
				align : 'center'
			}, {
				header : '上传日期',
				dataIndex : 'lastModifiedDate',
				width : 120,
				align : 'center'
			}, {
				header : '上传人',
				dataIndex : 'lastModifiedName',
				align : 'center'
			},{
				header : '查看附件',
				dataIndex : 'conDocId',
				align : 'center',
				renderer : function(val) {
					// var val = record.get("fileCode")
					// + record.get("fileType");
					
					if (val != "" && val != null) {
						return "<a style=\"cursor:hand;color:red\" onClick=\"window.open('/power/managecontract/showConFile.action?conid="
									+ id + "&conDocId="+val+"&type=CONATT');\"/>查看附件</a>"
					} else {
						return "";
					}
				}
			
			}]);
	annex_item_cm.defaultSortable = true;
	var annex_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managecontract/findTerminateDocList.action'
		}),
		reader : new Ext.data.JsonReader({}, annex_item)
	});
	var annexGrid = new Ext.grid.GridPanel({
		ds : annex_ds,
		cm : annex_item_cm,
		sm : annex_sm,
		width : Ext.get('div_lay').getWidth(),
		split : true,
		title : '合同附件',
		height : 150,
		autoScroll : true,
		collapsible : true,
		border : false
	});
	var Credent_item = Ext.data.Record.create([{
		name : 'conDocId'
	}, {
		name : 'keyId'
	}, {
		name : 'docName'
	}, {
		name : 'docMemo'
	}, {
		name : 'oriFileName'
	}, {
		name : 'oriFileExt'
	}, {
		name : 'lastModifiedDate'
	}, {
		name : 'lastModifiedBy'
	}, {
		name : 'lastModifiedName'
	}, {
		name : 'docType'
	}, {
		name : 'oriFile'
	}]);
	var Credent_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});
	var Credent_item_cm = new Ext.grid.ColumnModel([Credent_sm,
			new Ext.grid.RowNumberer({
				header : '项次号',
				width : 50,
				align : 'center'
			}), {
				header : '名称',
				dataIndex : 'docName',
				align : 'center'
			}, {
				header : '备注',
				dataIndex : 'docMemo',
				align : 'center'
			}, {
				header : '原始文件',
				dataIndex : 'oriFile',
				align : 'center'
			}, {
				header : '上传日期',
				dataIndex : 'lastModifiedDate',
				width : 120,
				align : 'center'
			}, {
				header : '上传人',
				dataIndex : 'lastModifiedName',
				align : 'center'
			},{
				header : '查看附件',
				dataIndex : 'conDocId',
				align : 'center',
				renderer : function(val) {
					// var val = record.get("fileCode")
					// + record.get("fileType");
					
					if (val != "" && val != null) {
						return "<a style=\"cursor:hand;color:red\" onClick=\"window.open('/power/managecontract/showConFile.action?conid="
									+ id + "&conDocId="+val+"&type=CONEVI');\"/>查看附件</a>"
					} else {
						return "";
					}
				}
			
			}]);
	Credent_item_cm.defaultSortable = true;
	var Credent_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managecontract/findTerminateDocList.action'
		}),
		reader : new Ext.data.JsonReader({}, Credent_item)
	});

	var CredentGrid = new Ext.grid.GridPanel({
		ds : Credent_ds,
		cm : Credent_item_cm,
		sm : Credent_sm,
		split : true,
		height : 150,
		width : Ext.get('div_lay').getWidth(),
		title : '合同凭据',
		collapsible : true,
		border : false
	});

	var terminateDate = new Ext.form.TextField({
		id : 'terminateDate',
		fieldLabel : "验收日期",
		name : 'con.terminateDate',
		type : 'textfield',
		readOnly : true,
		style : 'cursor:pointer',
		anchor : "85%",
		value : getDate()
	});
	var conterminate = new Ext.form.FieldSet({
		title : '合同履行结束验收终止',
		height : '100%',
		layout : 'form',
		items : [{
			id : 'conId',
			name : 'con.conId',
			xtype : 'numberfield',
			fieldLabel : '序号',
			// *********给合同编号赋值*************
			value : id,
			// ************************************
			readOnly : false,
			hidden : true,
			hideLabel : true,
			anchor : '95%'
		}, conttreesNoForm, contractNameForm, cliendIdForm, {
			id : 'terminateMome',
			name : 'con.terminateMome',
			xtype : 'textarea',
			fieldLabel : '结束说明',
			readOnly : false,
			allowBlank : true,
			anchor : '85%'
		}, {
			id : 'terminateByName',
			name : 'con.terminateBy',
			xtype : 'textfield',
			fieldLabel : '验收人',
			readOnly : true,
			anchor : '85%'
		}, terminateDate],
		buttons : [{
			text : '确定',
			iconCls : 'save',
			handler : function() {
				// alert(Ext.get("con.conId").dom.value);
				Ext.Ajax.request({
					url : "managecontract/conTerminate.action",
					method : 'post',
					params : {
						conid : Ext.get("con.conId").dom.value,
						terminateMome : Ext.get("con.terminateMome").dom.value
					},
					success : function(action) {
						parent.iframe1.document.getElementById("btnQuery").click();
						Ext.get('btnTerminate').dom.disabled = true;
						Ext.get('btnUpdate').dom.disabled = true;
//					    id = "";iframe2
//						form.getForm().reset();
						Ext.Msg.alert("提示", "验收终止成功！");
						parent.document.all.iframe2.src="";
						win.hide();
						parent.Ext.getCmp("maintab").setActiveTab(0);
					}
				})
			}

		}, {
			text : '关闭',
			iconCls : 'cancer',
			handler : function() {
				win.hide();
			}
		}]
	});

	var conterminateForm = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		id : 'shift-form',
		labelWidth : 80,
		autoHeight : true,
		region : 'center',
		border : false,
		items : [conterminate]
	});

	var win = new Ext.Window({
		title : '验收终止',
		el : 'win',
		modal : true,
		buttonAlign : "center",
		autoHeight : true,
		width : 500,
		closeAction : 'hide',
		items : [conterminateForm]
	})
	var Material_item = Ext.data.Record.create([{
		name : 'purNo'
	}, {
		name : 'materialId'
	}, {
		name : 'unitPrice'
	}, {
		name : 'purqty'
	}, {
		name : 'memo'
	}, {
		name : 'materialName'
	}, {
		name : 'specModel'
	}, {
		name : 'total'
	}]);
	var Material_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});
	var Material_item_cm = new Ext.grid.ColumnModel([Material_sm,
			new Ext.grid.RowNumberer({
				header : '项次号',
				width : 50,
				align : 'center'
			}), {
				header : '采购单号',
				dataIndex : 'purNo',
				align : 'center'
			}, {
				header : '物资名称',
				dataIndex : 'materialId',
				hidden : true,
				align : 'center'
			}, {
				header : '物资名称',
				dataIndex : 'materialName',
				align : 'center'
			}, {
				header : '规格型号',
				dataIndex : 'specModel',
				align : 'center'
			}, {
				header : '单价',
				dataIndex : 'unitPrice',
				//width : 120,
				align : 'center'
			}, {
				header : '数量',
				dataIndex : 'purqty',
				align : 'center'
			}, {
				header : '总价',
				dataIndex : 'total',
				align : 'center'
			}, {
				header : '备注',
				dataIndex : 'memo',
				align : 'center'
			}]);
	Material_item_cm.defaultSortable = true;
	var Material_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managecontract/findAllMaterialByContractNo.action'
		}),
		reader : new Ext.data.JsonReader({root : "list"}, Material_item)
	});

	var MaterialGrid = new Ext.grid.GridPanel({
		ds : Material_ds,
		cm : Material_item_cm,
		sm : Material_sm,
		split : true,
		height : 150,
		width : Ext.get('div_lay').getWidth(),
		 title : '物资明细',
		 collapsible : true,
		//tbar : Materialtbar,
		border : false
	});
	var layout = new Ext.Panel({
		autoWidth : true,
		autoHeight : true,
		border : false,
		autoScroll : true,
		split : true,
		items : [form, annexGrid, CredentGrid,MaterialGrid]
	});
	layout.render(Ext.getBody());

	// *********设置按钮为不可用状态************
	if (modifyId == null || modifyId == "") {
		form.getTopToolbar().items.get('btnUpdate').setDisabled(true);
	}
	if (id == null || id == "") {
		form.getTopToolbar().items.get('btnTerminate').setDisabled(true);
	}
})