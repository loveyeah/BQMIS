Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var id = getParameter("id");
	var contractNo = getParameter("contractNo");
	var entryId;
	var sum;
	var prosy_by; // 委托人
	var jhflg; // 判断物资部门，计划经营部

	// 币别
	var typeBox = new Ext.form.ComboBox({
				fieldLabel : '币别',
				id : 'currencyName',
				name : 'currencyName',
				readOnly : true,
				disabled : true,
				anchor : '75.5%'
			});
	typeBox.on('beforequery', function() {
				return false
			});

	// add by bjxu 091116
	var maifang = new Ext.form.TextField({
				id : 'maifang',
				// name : 'con.operateAdvice',
				fieldLabel : "合同买方",
				disabled : true,
				anchor : '85%',
				value : "大唐陕西发电有限公司灞桥热电厂"
			})

	// 合同编号
	var conttreesNo = new Ext.form.TextField({
				id : 'conttreesNo',
				// name :
				// disabled : true,
				// 'con.conttreesNo',
				disabled : true,
				xtype : 'textfield',
				fieldLabel : '合同编号',
				// readOnly : true,
				anchor : '92.5%'
			})
	// 合同类别 modify by drdu 20090423
	var contypeBox = new Ext.form.ComboBox({
				fieldLabel : '合同类别',
				store : [['CL', '材料'], ['BP', '备品'], ['SB', '设备']],
				id : 'conType',
				name : 'conTypetext',
				disabled : true,
				valueField : "value",
				displayField : "text",
				// value : '1',
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				// hiddenName : 'con.conCode',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				emptyText : '请选择',
				anchor : '85%'
			});
	contypeBox.on('beforequery', function() {
				return false
			});
	// 总金额
	var actAmount = new Ext.form.NumberField({
				id : 'actAmount',
				// name : 'con.actAmount',
				fieldLabel : '总金额',
				disabled : true,
				readOnly : true,
				anchor : '85.5%'
			});
	// 有无总金额
	var isTotal = new Ext.form.Checkbox({
				id : 'isTotal',
				// name : 'con.isSum',
				fieldLabel : '有无总金额',
				disabled : true,
				// checked : true,
				readOnly : true,
				anchor : "20%"
			});
	// 部门负责人
	var unitBox = new Ext.form.TextField({
				fieldLabel : '部门负责人',
				id : 'operateLeadName',
				// name : 'operateLeadName',
				disabled : true,
				readOnly : true,
				anchor : "85%"
			});
	// 供应商
	var cliendBox = new Ext.form.ComboBox({
				fieldLabel : '供应商',
				store : [['1', '燃料供应商'], ['2', '设备供应商']],
				id : 'cliendId',
				name : 'cliendId',
				valueField : "value",
				displayField : "text",
				mode : 'local',
				disabled : true,
				typeAhead : true,
				forceSelection : true,
				// hiddenName : 'clientName',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				anchor : '92.5%'
			});
	cliendBox.on('beforequery', function() {
				return false
			});

	// 费用来源
	var itemBox = new Ext.form.ComboBox({
				fieldLabel : '费用来源',
				// store : [['1', '电力供应'], ['2', '公司']],
				id : 'itemId',
				// name : 'itemId',
				// valueField : "value",
				// displayField : "text",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				// hiddenName : 'con.itemId',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				emptyText : '请选择',
				anchor : '85%'
			});

	var hiditemId = new Ext.form.Hidden({
				id : 'hiditemId'
			})
	function itemClick() {
		var args = {
			selectModel : 'single',
			rootNode : {
				id : "",
				text : ""
			}
		}
		var rvo = window
				.showModalDialog(
						'../../../../../contract/maint/conitemsourceSelect/conitemsourceSelect.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(rvo) != "undefined") {
			hiditemId.setValue(rvo.itemCode);
			itemBox.setValue(rvo.itemName);
		}
	}
	function exit() {

	}
	itemBox.onTriggerClick = itemClick;

	// 合同开始时间
	var startDate = new Ext.form.TextField({
				id : 'startDate',
				fieldLabel : "合同履行开始时间",
				// name : 'con.startDate',
				readOnly : true,
				style : 'cursor:pointer',
				disabled : true,
				anchor : "85%"
			});
	// 合同结束时间
	var endDate = new Ext.form.TextField({
				id : 'endDate',
				fieldLabel : "合同履行结束时间",
				// name : 'con.endDate',
				style : 'cursor:pointer',
				disabled : true,
				readOnly : true,
				anchor : "85%"
			});

	// 委托开始日期
	var prosyStartDate = new Ext.form.TextField({
				id : 'prosyStartDate',
				fieldLabel : "委托开始时间",
				// name : 'con.prosyStartDate',
				style : 'cursor:pointer',
				anchor : "85%",
				// allowBlank : false,
				// value : getDate(),
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

	// 委托开始日期
	var prosyEndDate = new Ext.form.TextField({
				id : 'prosyEndDate',
				fieldLabel : "委托结束时间",
				// name : 'con.prosyEndDate',
				style : 'cursor:pointer',
				anchor : "85%",
				// allowBlank : false,
				// value : getDate(),
				listeners : {
					focus : function() {
						var pkr = WdatePicker({
									startDate : '%y-%M-%d 00:00:00',
									dateFmt : 'yyyy-MM-dd HH:mm:ss',
									alwaysUseStartDate : true
								});
					}
				},
				validator : function() {
					var sd = prosyStartDate.getValue();
					var ed = prosyEndDate.getValue();
					if (sd >= ed) {
						return "委托结束时间应大于开始时间！";
					} else {
						return true;
					}
				}
			});

	// 起草人
	var entryName = new Ext.form.TextField({
				id : 'entryName',
				xtype : 'textfield',
				fieldLabel : '起草人',
				disabled : true,
				readOnly : true,
				anchor : '85.5%'
			});
	// 起草时间
	var entryDate = new Ext.form.TextField({
				id : 'entryDate',
				fieldLabel : "起草日期",
				// name : 'con.entryDate',
				disabled : true,
				style : 'cursor:pointer',
				readOnly : true,
				anchor : "85%"
			});
	// 经办人联系电话
	var operateTel = new Ext.form.TextField({
				fieldLabel : '联系电话',
				id : 'operateTel',
				disabled : true,
				// name : 'con.operateTel',
				anchor : '85%'
			})
	// 合同文本
	var oriFileName = new Ext.form.TextField({
				id : "filePath",
				name : "conFile",
				fieldLabel : '合同文本',
				readOnly : true,
				height : 22,
				anchor : "95%"
			})
	// 查看
	var btnView = new Ext.Button({
				id : 'btnView',
				text : '查看',
				handler : function() {
					window
							.open("/power/managecontract/showConFile.action?conid="
									+ id + "&type=CON");
				}
			});

	var btnSubmit = new Ext.Button({
				id : 'btnSubmit',
				text : '上传',
				handler : function() {
					if (id == "" || Ext.get('filePath').dom.value == "") {
						Ext.Msg.alert('提示', '请选择合同');
						return false;
					} else {
						form.getForm().submit({
									url : 'managecontract/modifyMeetConInfo.action',
									params : {
										conid : id,
										filePath : Ext.get('filePath').dom.value
									},
									method : 'post',
									waitMsg : '正在上传...',
									success : function(form, action) {
										Ext.MessageBox.alert('提示', '上传成功!');
									},
									failure : function(form, action) {
										Ext.MessageBox.alert('错误',
												'操作失败,请联系管理员!');
									}
								})
						// Ext.Ajax.request({
						// url : 'managecontract/modifyMeetConInfo.action',
						// params : {
						// conid : id,
						// // filePath : Ext.get('filePath').dom.value
						// conFile : oriFileName.value
						// },
						// method : 'post',
						// waitMsg : '正在上传...',
						// success : function(result, request) {
						// Ext.MessageBox.alert('提示', '上传成功!');
						// },
						// failure : function(result, request) {
						// Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
						// }
						// });
					}
				}
			});

	var formtbar = new Ext.Toolbar({
		items : [{
			id : 'btnSign',
			text : "签字",
			iconCls : 'write',
			handler : function() {
				if (id == "" || id == null) {
					Ext.Msg.alert('提示', '请在审批列表中选择待签字的合同！');
					return false;
				}
				var url;
				if (jhflg == "W") {
					url = "../../../../../../manage/contract/business/contractMeetSign/approve/meetSign/JHsign.jsp?entryId="
							+ entryId + "&conid=" + id + "&sum=" + sum;
				} else {
					url = "../../../../../../manage/contract/business/contractMeetSign/approve/meetSign/sign.jsp?entryId="
							+ entryId + "&conid=" + id + "&sum=" + sum;
				}
				// if(){
				// url =
				// "../../../../../../manage/contract/business/contractMeetSign/approve/meetSign/JHsign.jsp?entryId="
				// + entryId + "&conid=" + id + "&sum=" + sum;}
				// else{
				// url =
				// "../../../../../../manage/contract/business/contractMeetSign/approve/meetSign/sign.jsp?entryId="
				// + entryId + "&conid=" + id + "&sum=" + sum;}
				// }
				var o = window.showModalDialog(url, '',
						'dialogWidth=800px;dialogHeight=600px;status=no');
				if (o) {

					// parent.Ext.getCmp("maintab").setActiveTab(1);
					// var url1 =
					// "manage/contract/business/contractMeetSign/approve/conInfo/conInfo.jsp";
					// parent.document.all.iframe2.src = url1+"?id="+"";
					form.getForm().reset();
					id = "";
					entryId = "";
					parent.Ext.getCmp("maintab").setActiveTab(0);
					var url = "manage/contract/business/contractMeetSign/approve/arrpoveList/list.jsp";
					parent.document.all.iframe1.src = url;
				}
			}
		}, '-', {
			id : 'btnMeet',
			text : "会签表",
			iconCls : 'pdfview',
			handler : function() {
				CheckRptPreview();
			}
		}, '-', {
			id : 'btnQuery',
			text : "会签查询",
			iconCls : 'view',
			handler : function() {
				if (id == "" || id == null) {
					Ext.Msg.alert('提示', '请在审批列表中选择待签字的合同！');
					return false;
				}
				if (entryId == null || entryId == "") {
					Ext.Msg.alert('提示', '流程尚未启动！');
				} else {
					var url = "/power/workflow/manager/show/show.jsp?entryId="
							+ entryId;
					window.open(url);
				}
			}
		}, '-', {
			id : 'btnSave',
			text : "保存",
			iconCls : 'save',
			handler : function() {
				if (id == "") {
					Ext.Msg.alert("提示", "请选择一个合同!");
					return;
				}
				itemCode = hiditemId.getValue();
				Ext.Ajax.request({
							url : 'managecontract/addMeetConInfo.action',
							params : {
								id : id,
								itemCode : itemCode,
								method : "JHupdate"
							},
							method : 'post',
							success : function(result, request) {
								Ext.MessageBox.alert('提示', '修改成功!');
							},
							failure : function(result, request) {
								Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
							}
						});
			}

		}]
	});

	// 会签票面浏览
	function CheckRptPreview() {
		if (id == "" || id == null) {
			Ext.Msg.alert('提示', '请在审批列表中选择待签字的合同！');
			return false;
		}
		var url = "/powerrpt/report/webfile/bqmis/CGConMeetSign.jsp?conId="
				+ id;
		window.open(url);

	};

	var form = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		collapsible : true,
		width : Ext.get('div_lay').getWidth(),
		// title : '采购合同信息',
		autoHeight : true,
		// region : 'center',
		border : false,
//		tbar : formtbar,
		fileUpload : true,
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
											columnWidth : .4,
											labelWidth : 110,
											items : [{
														id : 'conId',
														xtype : 'hidden',
														fieldLabel : '合同id',
														readOnly : false,
														anchor : '85%'
													}]
										}, {
											border : false,
											layout : 'form',
											columnWidth : 1,
											labelWidth : 110,
											items : [{
														id : 'contractName',
														// name :
														// 'con.contractName',
														xtype : 'textfield',
														fieldLabel : '合同名称',
														disabled : true,
														readOnly : true,
														anchor : '92.5%'
													}]
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
											items : [{
														id : 'conYear',
														// name : 'con.conYear',
														xtype : 'numberfield',
														fieldLabel : '合同年份',
														disabled : true,
														readOnly : true,
														anchor : '85%'
													}]
										}, {
											border : false,
											layout : 'form',
											columnWidth : 1,
											labelWidth : 110,
											items : [conttreesNo]
										}, {
											border : false,
											layout : 'form',
											columnWidth : 1,
											labelWidth : 110,
											items : [cliendBox]
										}, {
											border : false,
											layout : 'form',
											columnWidth : .5,
											labelWidth : 110,
											items : [itemBox, {
														id : 'operateDepName',
														// name :
														// 'con.operateDepCode',
														xtype : 'textfield',
														fieldLabel : '承办部门',
														disabled : true,
														readOnly : true,
														anchor : '85%'
													}]
										}, {
											border : false,
											layout : 'form',
											columnWidth : 0.5,
											labelWidth : 110,
											items : [maifang]
										}, {
											border : false,
											layout : 'form',
											columnWidth : .5,
											labelWidth : 110,
											items : [{
														id : 'operateName',
														xtype : 'textfield',
														fieldLabel : '承办人',
														disabled : true,
														readOnly : true,
														anchor : '85%'
													}
											// , unitBox
											]
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
										}
										// , {
										// columnWidth : .5,
										// layout : 'form',
										// labelWidth : 110,
										// border : false,
										// items : [startDate]
										// }, {
										// columnWidth : .5,
										// layout : 'form',
										// labelWidth : 110,
										// border : false,
										// items : [endDate]
										// }
										, {
											border : false,
											layout : 'form',
											columnWidth : 1,
											labelWidth : 110,
											items : [{
														id : 'prosy_byName',
														disabled : true,
														xtype : 'trigger',
														fieldLabel : '本项目委托代理人',
														readOnly : true,
														// allowBlank : false,
														anchor : '92.5%'

													}]
										}
										// ,{
										// columnWidth : .5,
										// layout : 'form',
										// labelWidth : 110,
										// border : false,
										// items : [prosyStartDate]
										// },{
										// columnWidth : .5,
										// layout : 'form',
										// labelWidth : 110,
										// border : false,
										// items : [prosyEndDate]
										// }
										, {
											border : false,
											layout : 'form',
											columnWidth : 1,
											labelWidth : 110,
											items : [{
														id : 'conAbstract',
														// name :
														// 'con.conAbstract',
														xtype : 'textarea',
														fieldLabel : '合同简介',
														disabled : true,
														readOnly : true,
														height : 120,
														anchor : '92.5%'
													}]
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
											columnWidth : 0.5,
											labelWidth : 110,
											items : [operateTel]
										}, {
											border : false,
											layout : 'form',
											columnWidth : 0.4,
											labelWidth : 110,
											items : [oriFileName]
										}, {
											border : false,
											layout : 'form',
											columnWidth : 0.1,
											labelWidth : 50,
											items : [btnView
											// {
											// id : 'dd',
											// name : 'dd',
											// xtype : 'textfield',
											// fieldLabel : '<a
											// href="/power/managecontract/showConFile.action?conid='+id+'&type=CON">查看</a>'
											// }
											]
										}, {
											border : false,
											layout : 'form',
											// add by dswang 2009/6/2
											hidden : true,
											columnWidth : 0.05,
											labelWidth : 10,
											items : [btnSubmit]
										}]
							}]
				})]
	});

	// Ext.getCmp("dd").hidden=true;
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
	var annex_item_cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
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
			}, {
				header : '查看附件',
				dataIndex : 'conDocId',
				align : 'center',
				renderer : function(val) {
					// var val = record.get("fileCode")
					// + record.get("fileType");

					if (val != "" && val != null) {
						return "<a style=\"cursor:hand;color:red\" onClick=\"window.open('/power/managecontract/showConFile.action?conid="
								+ id
								+ "&conDocId="
								+ val
								+ "&type=CONATT');\"/>查看附件</a>"
					} else {
						return "";
					}
				}
			}]);
	annex_item_cm.defaultSortable = true;
	var annex_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'managecontract/findDocList.action'
						}),
				reader : new Ext.data.JsonReader({}, annex_item)
			});

	var annexGrid = new Ext.grid.GridPanel({
				title : '合同附件',
				collapsible : true,
				ds : annex_ds,
				cm : annex_item_cm,
				width : Ext.get('div_lay').getWidth(),
				split : true,
				height : 150,
				autoScroll : true,
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

	var Credent_item_cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
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
			}, {
				header : '查看附件',
				dataIndex : 'conDocId',
				align : 'center',
				renderer : function(val) {
					// var val = record.get("fileCode")
					// + record.get("fileType");

					if (val != "" && val != null) {
						return "<a style=\"cursor:hand;color:red\" onClick=\"window.open('/power/managecontract/showConFile.action?conid="
								+ id
								+ "&conDocId="
								+ val
								+ "&type=CONEVI');\"/>查看附件</a>"
					} else {
						return "";
					}
				}
			}]);
	Credent_item_cm.defaultSortable = true;
	var Credent_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'managecontract/findDocList.action'
						}),
				reader : new Ext.data.JsonReader({}, Credent_item)
			});

	var CredentGrid = new Ext.grid.GridPanel({
				title : '合同凭据',
				collapsible : true,
				ds : Credent_ds,
				cm : Credent_item_cm,
				split : true,
				height : 150,
				width : Ext.get('div_lay').getWidth(),
				border : false
			});
	// 物资明细确定
	// var Materialtbar = new Ext.Toolbar({
	// items : ['物资明细：', {
	// id : 'btnCredentSel',
	// text : "采购单选择",
	// iconCls : 'add',
	// handler : function() {
	// }
	// },'-', {
	// id : 'btnCredentDelete',
	// text : "删除",
	// iconCls : 'delete',
	// handler : function() {
	// }
	// }]
	// });

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
				// width : 120,
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
				reader : new Ext.data.JsonReader({
							root : "list"
						}, Material_item)
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
				// tbar : Materialtbar,
				border : false
			});

	if (id != "") {
		Ext.Ajax.request({
					url : 'managecontract/findMeetConModel.action',
					params : {
						conid : id
					},
					method : 'post',
					waitMsg : '正在加载数据...',
					success : function(result, request) {
						var o = eval('(' + result.responseText + ')');
						form.getForm().loadRecord(o);
						Ext.get("conFile").dom.value = (o.data.filePath == null
								? ""
								: o.data.filePath);
						if (o.data.filePath == null || o.data.filePath == "") {
							Ext.getCmp("btnView").setVisible(false);
						}
						//
						jhflg = o.data.jyjhAdvice;
						// ---add by bjxu
						contypeBox.setValue(o.data.conCode)
						itemBox.setValue(o.data.itemName);
						hiditemId.setValue(o.data.itemId)
						if (jhflg == "Y") {
							itemBox.onTriggerClick = itemClick;
							itemBox.setDisabled(false);
//							Ext.get('btnSave').dom.disabled = false;
						} else {
							itemBox.onTriggerClick = exit;
							itemBox.setDisabled(true);
//							Ext.get('btnSave').dom.disabled = true;
						}

						Ext.get('prosy_byName').dom.value = o.data.prosy_byName;
						cliendBox.setValue((o.data.cliendName == null)
								? ""
								: o.data.cliendName);
						if (o.data.isSum == "Y") {
							Ext.get('isTotal').dom.checked = true;
							Ext.get('isTotal').dom.readOnly = true
						} else {
							Ext.get('isTotal').dom.checked = false;
							Ext.get('isTotal').dom.readOnly = true
						}
//						if (o.data.workflowStatus == 2) {
//							Ext.get('btnSign').dom.disabled = true
//						}
						entryId = o.data.workflowNo;
						sum = o.data.actAmount;
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
//						if (jhflg == "Y") {
//							Ext.get('btnSave').dom.disabled = false
//						} else {
//							Ext.get('btnSave').dom.disabled = true;
//						}
					},
					failure : function(result, request) {
						Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
					}
				});
	};

	var layout = new Ext.Panel({
				autoWidth : true,
				autoHeight : true,
				border : false,
				autoScroll : true,
				split : true,
				items : [form, annexGrid, CredentGrid, MaterialGrid]
			});
	layout.render(Ext.getBody());
})