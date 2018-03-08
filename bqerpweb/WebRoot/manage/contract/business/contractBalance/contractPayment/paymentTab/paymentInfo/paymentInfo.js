Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'qtip';
	// alert(parent.flagId);
	var flagId = parent.flagId;
	// alert(flagId)

	// 标记删除的明细数据的结算id
	var delIds = [];

	// 从session取登录人编码姓名部门相关信息
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						// var result = eval("(" + action.responseText + ")");
						var result = Ext.util.JSON.decode(action.responseText)
						// alert(Ext.util.JSON.encode(result))
						if (result.workerCode) {
							entryBy.setValue(result.workerCode)
							entryByName.setValue(result.workerName)
							operateDepCode.setValue(result.deptCode)
							operateDepName.setValue(result.deptName)
						}
					}
				});
	}
	/**
	 * 金钱格式化
	 */
	function moneyFormat(v) {
		v = (Math.round((v - 0) * 100)) / 100;
		v = (v == Math.floor(v))
				? v + ".0000"
				: ((v * 10 == Math.floor(v * 10))
						? v + "000"
						: ((v * 100 == Math.floor(v * 100)) ? v + "00" : ((v
								* 1000 == Math.floor(v * 1000)) ? v + "0" : v)));
		v = String(v);
		var ps = v.split('.');
		var whole = ps[0];
		var sub = ps[1] ? '.' + ps[1] : '.0000';
		var r = /(\d+)(\d{3})/;
		while (r.test(whole)) {
			whole = whole.replace(r, '$1' + ',' + '$2');
		}
		v = whole + sub;
		if (v.charAt(0) == '-') {
			return '-' + v.substr(1);
		}
		return v;
	}
	function getCurrentDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t
		return s;
	}
	function getCurrentMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t
		return s;
	}

	// 申请id 需在数据库中建表确立
	var appId = new Ext.form.Hidden({
				id : 'appId',
				name : 'appId'
			});
	// 制表人编码及姓名
	var entryBy = new Ext.form.Hidden({
				id : 'entryBy',
				name : 'entryBy'
			})
	var entryByName = new Ext.form.TextField({
				id : 'entryByName',
				name : 'entryByName',
				readOnly : true,
				fieldLabel : '制表人',
				anchor : '80%'
			})
	// 上报部门编码及名称
	var operateDepCode = new Ext.form.Hidden({
				id : 'operateDepCode',
				name : 'operateDepCode'
			})
	var operateDepName = new Ext.form.TextField({
				id : 'operateDepName',
				name : 'operateDepName',
				readOnly : true,
				fieldLabel : '上报部门',
				anchor : '80%'
			});
	// 制表时间
	var entryDateString = new Ext.form.TextField({
				id : 'entryDateString',
				name : 'entryDateString',
				readOnly : true,
				fieldLabel : '制表时间',
				value : getCurrentDate(),
				anchor : '80%'
			})
	// 申请付款时间
	var applicatDateString = new Ext.form.TextField({
				id : 'applicatDateString',
				fieldLabel : '申请付款时间',
				style : 'cursor:pointer',
				value : getCurrentMonth(),
				readOnly : true,
				anchor : '80%',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M',
									dateFmt : 'yyyy-MM',
									alwaysUseStartDate : true,
									isShowClear : false
								});
						this.blur();
					}
				}
			});
	// 金额单位
	var moneyUnit = new Ext.form.ComboBox({
				fieldLabel : '金额单位',
				id : 'moneyUnit',
				name : 'moneyUnit',
				readOnly : true,
				store : new Ext.data.SimpleStore({
							fields : ['unit', 'value'],
							data : [[1, '元'], [2, '万元']]
						}),
				mode : 'local',
				displayField : 'value',
				valueField : 'unit',
				triggerAction : 'all',
				value : 1,
				anchor : '80%'
			})
	// 表单form
	var form = new Ext.form.FormPanel({
				id : 'form',
				border : false,
				frame : true,
				layout : 'column',
				items : [{
					columnWidth : .5,
					layout : 'form',
					items : [appId, entryBy, entryByName, entryDateString,
							applicatDateString]
				}, {
					columnWidth : .5,
					layout : 'form',
					items : [operateDepCode, operateDepName, moneyUnit]
				}]
			})
	var field = new Ext.form.FieldSet({
				id : 'field',
				layout : 'fit',
				title : '主信息',
				collapsible : true,
				items : [form]
			})
	// 明细数据
	// 明细列表中的数据对象
	var detailRec = new Ext.data.Record.create([
			// {
			// name : 'balanceId'
			// }, {
			// name : 'conId'
			// }, {
			// name : 'balaFlag'
			// }, {
			// name : 'contractName'
			// }, {
			// name : 'clientName'
			// }, {
			// name : 'actAccount'
			// }, {
			// name : 'itemName'
			// }, {
			// name : 'applicatPrice'
			// }, {
			// name : 'applicatDate'
			// }, {
			// name : 'operateName'
			// }, {
			// name : 'passPrice'
			// }, {
			// name : 'passDate'
			// }, {
			// name : 'balancePrice'
			// }, {
			// name : 'balaDate'
			// }, {
			// name : 'balanceName'
			// }, {
			// name : 'currencyName'
			// }, {
			// name : 'balanceBy'
			// },
			// {
			// // 结算状态
			// name : 'balaFlag'
			// },
			// 添加数据开始
			{
		// 结算id
		name : 'balanceId'
	}, {
		// 合同id
		name : 'conId'
	}, {
		// 合同编号
		name : 'contractNo'
	}, {
		// 收款单位
		name : 'clientName'
	}, {
		// 合同名称
		name : 'contractName'
	}, {
		// 合同总金额
		name : 'actAccount'
	}, {
		// 已付款金额
		name : 'payedAccount'
	}, {
		// 本次申请付款金额
		name : 'applicatPrice'
	}, {
		// 本次批准付款金额
		name : 'passPrice'
	}, {
		// 余额金额
		name : 'balance'
	}, {
		// 备注
		name : 'memo'
	}, {
		// 申请id
		name : 'appId'
	}
	// 添加数据结束
	])
	// 明细列表中的选择模式
	var detailsm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			})
	// 明细列表中的列模式
	var detailcm = new Ext.grid.ColumnModel([detailsm,
			new Ext.grid.RowNumberer({
						header : '序号',
						width : 35,
						align : 'center'
					}), {
				header : '合同编号',
				dataIndex : 'contractNo',
				align : 'center'
			}, {
				header : '收款单位',
				dataIndex : 'clientName',
				align : 'center'
			}, {
				header : '合同名称',
				dataIndex : 'contractName',
				align : 'center'
			}, {
				header : '合同总总额',
				dataIndex : 'actAccount',
				align : 'center',
				renderer : moneyFormat
			}, {
				header : '已付款金额',
				dataIndex : 'payedAccount',
				align : 'center',
				renderer : moneyFormat
			}, {
				header : '本次申请付款金额',
				dataIndex : 'applicatPrice',
				align : 'center',
				editor : new Ext.form.NumberField({
							allowDecimals : true,
							decimalPrecision : 4,
							allowNegative : false
						}),
				renderer : function(value, metadata, record, rowIndex, colInex,
						store) {
					if (record.get('applicatPrice') != null
							&& record.get('applicatPrice') != ''
							&& record.get('passPrice') == null)
						record.set('passPrice', record.get('applicatPrice'));
					return moneyFormat(value);
				}
			}, {
				header : '本次批准付款金额',
				dataIndex : 'passPrice',
				align : 'center',
				editor : new Ext.form.NumberField({
							allowDecimals : true,
							decimalPrecision : 4,
							allowNegative : false
						}),
				renderer : function(value, metadata, record, rowIndex, colInex,
						store) {
					var count = record.get('actAccount') + 0;
					var payed = record.get('payedAccount') + 0;
					var allowed = value + 0;
					record.set('balance', count - payed - allowed);
					return moneyFormat(value);
				}
			}, {
				header : '余额金额',
				dataIndex : 'balance',
				align : 'center',
				renderer : moneyFormat
			}, {
				header : '备注',
				dataIndex : 'memo',
				align : 'center',
				editor : new Ext.form.TextField()
			}]);
	// 明细列表中的store
	var detailstore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'managecontract/bqfindBalanceList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'totalCount',
							root : 'list'
						}, detailRec)
			});

	// 按钮
	var addBut = new Ext.Button({
				id : 'addBut',
				iconCls : 'add',
				text : '增加',
				handler : addRec
			})
	var saveBut = new Ext.Button({
				id : 'saveBut',
				iconCls : 'save',
				text : '保存',
				handler : saveRec
			})
	var delBut = new Ext.Button({
				id : 'delBut',
				iconCls : 'delete',
				text : '删除',
				handler : deleteRec
			})
	var repBut = new Ext.Button({
				id : 'repBut',
				iconCls : 'report',
				text : '上报',
				handler : repRec
			})
	var blancebar = new Ext.Toolbar({
				items : [addBut, saveBut, delBut, repBut]
			})
	var detailgrid = new Ext.grid.EditorGridPanel({
				ds : detailstore,
				cm : detailcm,
				sm : detailsm,
				split : true,
				width : Ext.getBody().getWidth(),
				height : 300,
				autoScroll : true,
				border : false,
				tbar : blancebar,
				viewConfig : {
					forceFit : false
				}
			});

	// 申请附件

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
						header : '行号',
						width : 35,
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
			}, {
				header : '查看附件',
				dataIndex : 'conDocId',
				align : 'center',
				renderer : function(val) {
					if (val != "" && val != null) {
						return "<a style=\"cursor:hand;color:red\" onClick=\"window.open('/power/managecontract/showConFile.action?conid="
								+ flagId
								+ "&conDocId="
								+ val
								+ "&type=SHENQING');\"/>查看附件</a>"
					} else {
						return "";
					}
				}

			}]);
	annex_item_cm.defaultSortable = true;
	var annex_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'managecontract/findBalDocList.action'
						}),
				reader : new Ext.data.JsonReader({
						// root : 'data'
						}, annex_item)
			});

	var annextbar = new Ext.Toolbar({
		items : ['申请附件：', {
					id : 'btnAnnexAdd',
					text : "增加",
					iconCls : 'add',
					handler : function() {
						if (flagId != null && flagId != '') {
							docmethod = "add";
							docform.getForm().reset();
							docwin.show();
						} else {
							Ext.Msg.alert('提示信息', '请先保存采购合同付款申请！')
						}
					}
				}, '-', {
					id : 'btnAnnexDelete',
					text : "删除",
					iconCls : 'delete',
					handler : function() {
						var seldocs = annexGrid.getSelectionModel()
								.getSelections();
						if (seldocs.length > 0) {
							Ext.Msg.confirm('提示', '删除的数据您将不能恢复,确定要删除吗?',
									function(b) {
										if (b == "yes") {
											var rec = annexGrid
													.getSelectionModel()
													.getSelected();
											Ext.Ajax.request({
												url : 'managecontract/deleteBalDoc.action',
												params : {
													docid : rec.data.conDocId
												},
												method : 'post',
												waitMsg : '正在删除数据...',
												success : function(result,
														request) {
													Ext.MessageBox.alert('提示',
															'删除成功!');
													annex_ds.reload();
												},
												failure : function(result,
														request) {
													Ext.MessageBox.alert('错误',
															'操作失败,请联系管理员!');
												}
											});

										}
									});

						} else {
							Ext.Msg.alert('提示', '请选择您要删除的付款附件！');
						}

					}
				}]
	});
	var annexGrid = new Ext.grid.GridPanel({
				ds : annex_ds,
				cm : annex_item_cm,
				sm : annex_sm,
				tbar : annextbar,
				split : true,
				height : 100,
				autoScroll : true,
				border : false,
				viewConfig : {
					forceFit : false
				}
			});
	var docFile = {
		id : "docFile",
		xtype : "textfield",
		inputType : "file",
		fieldLabel : '选择附件',
		allowBlank : false,
		anchor : "95%",
		height : 22,
		// readOnly : true,
		name : 'docFile'
	}

	var lastModifiedDate = new Ext.form.TextField({
				id : 'lastModifiedDate',
				fieldLabel : "上传时间",
				name : 'doc.lastModifiedDate',
				type : 'textfield',
				readOnly : true,
				style : 'cursor:pointer',
				anchor : "95%",
				value : getCurrentDate()
			});
	var lastModifiedName = new Ext.form.TextField({
				id : 'lastModifiedName',
				name : 'lastModifiedName',
				fieldLabel : '上传人',
				readOnly : true,
				anchor : '95%'
			});
	var doccontent = new Ext.form.FieldSet({
				height : '100%',
				layout : 'form',
				items : [{
							id : 'conDocId',
							name : 'doc.conDocId',
							readOnly : false,
							hidden : true
						}, {
							id : 'keyId',
							name : 'doc.keyId',
							readOnly : false,
							hidden : true
						}, docFile, {
							id : 'docName',
							name : 'doc.docName',
							xtype : 'textfield',
							fieldLabel : '附件名称',
							allowBlank : false,
							readOnly : false,
							anchor : '95%'
						}, {
							id : 'docMemo',
							name : 'doc.docMemo',
							xtype : 'textarea',
							fieldLabel : '备注',
							readOnly : false,
							allowBlank : true,
							anchor : '94.5%'
						}, lastModifiedDate, lastModifiedName, {
							id : 'docType',
							name : 'doc.docType',
							xtype : 'textfield',
							fieldLabel : '类型',
							readOnly : false,
							hidden : true,
							hideLabel : true,
							anchor : '95%'
						}],
				buttons : [{
					text : '保存',
					iconCls : 'save',
					handler : function() {
						var url = '';
						if (docmethod == "add") {
							url = 'managecontract/addBalDoc.action';

						} else {
							url = 'managecontract/updateBalDoc.action';
						}
						if (!docform.getForm().isValid()) {
							return false;
						}
						docform.getForm().submit({
							url : url,
							method : 'post',
							params : {
								filePath : Ext.get('docFile').dom.value,
								balanceId : flagId,
								type : "SHENQING"
							},
							success : function(form, action) {
								var message = eval('('
										+ action.response.responseText + ')');
								Ext.Msg.alert("成功", message.data);
								annex_ds.load({
											params : {
												balanceId : flagId,
												type : "SHENQING"
											}
										});
								docwin.hide();
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
						docform.getForm().reset();
						docwin.hide();
					}
				}]
			});
	var docform = new Ext.form.FormPanel({
				bodyStyle : "padding:0px 0px 0",
				labelAlign : 'right',
				id : 'shift-form',
				// hidden : true,
				labelWidth : 80,
				autoHeight : true,
				fileUpload : true,
				region : 'center',
				border : false,
				items : [doccontent]
			});
	var docwin = new Ext.Window({
				title : '新增',
				modal : true,
				autoHeight : true,
				width : 450,
				// height : 450,
				closeAction : 'hide',
				items : [docform]
			})

	annexGrid.on('rowdblclick', function(grid, rowIndex, e) {
				var record = annexGrid.getSelectionModel().getSelected();
				docmethod = 'update';
				docform.getForm().reset();
			});
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				items : [{
							// title : "执行中的合同列表",
							region : 'north',
							layout : 'fit',
							border : false,
							margins : '0 0 0 0',
							height : 150,
							split : true,
							collapsible : false,
							items : [field]

						}, {
							title : "合同列表",
							region : "center",
							layout : 'fit',
							border : false,
							margins : '0 0 0 0',
							split : true,
							collapsible : true,
							items : [detailgrid]
						}, {
							title : "申请附件列表",
							region : "south",
							layout : 'fit',
							height : 150,
							border : false,
							margins : '0 0 0 0',
							split : true,
							collapsible : true,
							items : [annexGrid]
						}

				]
			});

	if (flagId != null) {
		appId.setValue(flagId)
		Ext.Msg.wait("正在加载数据……");
		Ext.Ajax.request({
					url : 'managecontract/bqfindAppConList.action',
					method : 'post',
					params : {
						appId : flagId
					},
					success : function(response, options) {
						var obj = Ext.util.JSON.decode(response.responseText);
						// alert(response.responseText)
						entryBy.setValue(obj.entryBy);
						entryByName.setValue(obj.entryByName);
						operateDepCode.setValue(obj.operateDepDode);
						operateDepName.setValue(obj.operateDepName);
						entryDateString.setValue(obj.entryDate);
						applicatDateString.setValue(obj.applicatDate);
						moneyUnit.setValue(obj.moneyUnit);
						Ext.Msg.hide();
					},
					failure : function(response, options) {
						Ext.Msg.alert('提示信息', '加载采购合同付款申请失败！');
					}
				})
		detailstore.baseParams = {
			appId : flagId
		}
		detailstore.load({
					params : {
						start : 0,
						limit : 18
					}
				})
		annex_ds.baseParams = {
			balanceId : flagId,
			type : 'SHENQING'
		};
		annex_ds.load();
	} else {
		getWorkCode();
	}

	function deleteRec() {
		if (detailgrid.getSelectionModel().hasSelection()) {
			var recs = detailgrid.getSelectionModel().getSelections()
			for (var i = 0; i <= recs.length - 1; i++) {
				detailstore.remove(recs[i]);
				delIds.push(recs[i].get('balanceId'))
			}
			detailgrid.getView().refresh();
		} else {
			if (flagId == null) {
				Ext.Msg.alert('提示信息', '该采购申请尚未保存！');
				return;
			}
			Ext.Msg.confirm('提示', '确认要删除该采购申请？', function(button) {
						if (button == 'yes') {
							Ext.Ajax.request({
										url : 'managecontract/bqDelRecByAppId.action',
										method : 'post',
										params : {
											appId : flagId
										},
										success : function(response, options) {
											form.getForm().reset();
											detailstore.removeAll();
											annex_ds.removeAll();
											delIds = [];
											flagId = null;
											parent.flagId = null;
											getWorkCode();
											Ext.Msg.alert('提示信息', '删除采购申请成功！');
										},
										failure : function(response, options) {
											Ext.Msg.alert('提示信息', '删除采购申请失败！');
										}
									})
						}
					});
		}
	}

	function addRec() {
		var url = "conselect.jsp";
		var con = window.showModalDialog(url, '',
				'dialogWidth=600px;dialogHeight=400px;status=no');
		if (typeof(con) != "undefined" && con != null) {
			var conLen = con.length;
			if (conLen > 0) {
				for (i = 0; i < conLen; i++) {
					var record = new detailRec({
								balaFlag : 0,
								balanceId : null,
								conId : con[i].conId,
								contractNo : con[i].conttreesNo,
								clientName : con[i].clientName,
								contractName : con[i].contractName,
								actAccount : con[i].actAmount,
								payedAccount : con[i].payedAmount,
								applicatPrice : null,
								passPrice : null,
								balance : null,
								memo : null,
								appId : flagId
							})
					detailstore.add(record);
					detailgrid.getView().refresh();
				}
			}
			// alert(Ext.encode(con))
		}

	}

	function saveRec() {
		if (detailstore.getCount() == 0) {
			Ext.Msg.alert('提示信息', '无合同信息进行保存，请先增加！');
			return;
		}
		for (var i = 0; i <= detailstore.getCount() - 2; i++)
			for (var j = i + 1; j <= detailstore.getCount() - 1; j++) {
				if (detailstore.getAt(i).get('conId') == detailstore.getAt(j)
						.get('conId')) {
					Ext.Msg.alert('提示信息', '该付款申请中有合同重复，请检查！');
					return;
				}
			}
		Ext.Msg.confirm('提示信息', '确认要保存数据吗？', function(button) {
					if (button == 'yes') {

						var addObj = new Array();
						var updateObj = new Array();
						for (var i = 0; i <= detailstore.getCount() - 1; i++) {
							if (detailstore.getAt(i).get('balanceId') == null
									|| detailstore.getAt(i).get('balanceId') == '') {
								addObj.push(detailstore.getAt(i).data)
							} else {
								updateObj.push(detailstore.getAt(i).data)
							}
						}
						// alert(appId.getValue());
						// alert(Ext.util.JSON.encode(addObj))
						// alert(Ext.util.JSON.encode(updateObj))
						// alert(delIds.join(','))
						Ext.Msg.wait('正在保存中……')
						Ext.Ajax.request({
									url : 'managecontract/bqAddorUpdateBalance.action',
									method : 'post',
									waitTitle : '提示',
									params : {
										appId : appId.getValue(),
										entryBy : entryBy.getValue(),
										operateDepCode : operateDepCode
												.getValue(),
										entryDateString : entryDateString
												.getValue(),
										applicatDateString : applicatDateString
												.getValue(),
										moneyUnit : moneyUnit.getValue(),
										add : Ext.util.JSON.encode(addObj),
										update : Ext.util.JSON
												.encode(updateObj),
										ids : delIds.join(',')
									},
									success : function(result, options) {
										// alert(result.responseText)
										var res = Ext.util.JSON
												.decode(result.responseText);
										if (res != null) {
											if (res.value != null
													&& res.value != 'null') {
												flagId = res.value;
												parent.flagId = res.value
												appId.setValue(res.value)
												detailstore.baseParams = {
													appId : res.value
												};
												detailstore.load();
											}
										}
										delIds = [];
										Ext.Msg.alert('提示信息', '数据保存成功！');
									},
									failure : function(result, options) {
										// detailstore.reload();
										delIds = [];
										Ext.Msg.alert('提示信息', '数据保存失败！');
									}

								})
					}
				})
	}

	function repRec() {
		if (flagId == null) {
			Ext.Msg.alert('提示信息', '该采购申请尚未保存！');
			return;
		}
		Ext.Msg.confirm('提示信息', '确认要上报该采购合同付款申请？', function(button) {
					if (button == 'yes') {
						Ext.Msg.wait('正在上报中……');
						Ext.Ajax.request({
									url : 'managecontract/bqReportByAppId.action',
									method : 'post',
									params : {
										appId : flagId
									},
									success : function(response, options) {
										Ext.Msg.alert('提示信息', '申请上报成功！');
										form.getForm().reset();
										getWorkCode();
										flagId = null;
										parent.flagId = null;
										detailstore.removeAll();
										annex_ds.removeAll();
									},
									failure : function(reponse, options) {
										Ext.Msg.alert('提示信息', '申请上报失败！');
									}
								})
					}
				})
	}
})