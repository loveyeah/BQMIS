Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	// 币别 modify by drdu 2009/05/05
	var typeStore = new Ext.data.JsonStore({
				url : 'managecontract/getConCurrencyList.action',
				root : 'list',
				fields : ['currencyId', 'currencyName']
			})
	typeStore.load();
	var id = getParameter("id");
	if (id != "") {
		Ext.Ajax.request({
					url : 'managecontract/findMeetConModel.action',
					params : {
						conid : id
					},
					method : 'post',
					waitMsg : '正在加载数据...',
					success : function(result, request) {
						var o = eval('(' + result.responseText+ ')');
//						 alert(o.data.constructionName)
						 Ext.get("secondcharge").dom.value = o.data.constructionName ==null ?"":o.data.constructionName;
						form.getForm().loadRecord(o);
						prosy_by.setValue(o.data.prosy_byName)
						 itemBox.setValue(o.data.itemName);
						if(o.data.filePath == null || o.data.filePath == ""){
							Ext.getCmp("btnView").setVisible(false);
							}
						if (o.data.projectId != null) {
							Ext.get("projectNum").dom.value = o.data.projectId;
							hdnprojectNum.setValue(o.data.projectId);
						} else {
							Ext.get("projectNum").dom.value = "";
							hdnprojectNum.setValue("");
						}
						projectNum.setValue(o.data.prjShow);
						cliendBox.setValue((o.data.clientName == null)
								? ""
								: o.data.clientName);
						thirdBox.setValue((o.data.thirdClientName == null)
								? ""
								: o.data.thirdClientName);
						if (o.data.isSum == "Y") {
							Ext.get('isTotal').dom.checked = true;
							Ext.get('isTotal').dom.disabled = true
						} else {
							Ext.get('isTotal').dom.checked = false;
							Ext.get('isTotal').dom.disabled = true
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
					},
					failure : function(result, request) {
						Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
					}
				});
	};
	
		// 委托人 add by bjxu
	var prosy_by = new Ext.form.ComboBox({
				id : 'prosy_by',
				fieldLabel : '本项目委托代理人',
				readOnly : true,
				anchor : '85%'
			})
	prosy_by.on("beforequery", function() {
				return false;
			})
	// 合同类别
	var contypeBox = new Ext.form.TextField({
				fieldLabel : '合同类别',
				id : 'conTypeName',
				name : 'conTypeName',
				readOnly : true,
				anchor : "85%"
			});

	// 总金额
	var actAmount = new Ext.form.NumberField({
				id : 'actAmount',
				name : 'con.actAmount',
				fieldLabel : '总金额',
				readOnly : true,
				anchor : '81%'
			});
	// 有无总金额
	var isTotal = new Ext.form.Checkbox({
				id : 'isTotal',
				// name : 'con.isSum',
				fieldLabel : '有无总金额',
				// checked : true,
				readOnly : true,
				anchor : "20%"
			});
	// 部门负责人
	var unitBox = new Ext.form.TextField({
				fieldLabel : '部门负责人',
				id : 'operateLeadName',
				name : 'operateLeadName',
				readOnly : true,
				anchor : "85%"
			});
	// 供应商
	// var cliendBox = new Ext.form.ComboBox({
	// fieldLabel : '合作伙伴',
	// store : [['1', '燃料供应商'], ['2', '设备供应商']],
	// id : 'cliendId',
	// name : 'cliendId',
	// valueField : "value",
	// displayField : "text",
	// mode : 'local',
	// typeAhead : true,
	// forceSelection : true,
	// hiddenName : 'con.cliendId',
	// editable : false,
	// triggerAction : 'all',
	// selectOnFocus : true,
	// emptyText : '请选择',
	// anchor : '92.5%'
	// });
	// 燃料供应商
	var cliendBox = new Ext.form.ComboBox({
				fieldLabel : '合作伙伴',
				// store : [['1', '燃料供应商'], ['2', '设备供应商']],
//				store : clientStore,
				id : 'cliendId',
				name : 'cliendId',
//				valueField : "value",
//				displayField : "text",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
//				hiddenName : 'con.cliendId',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				emptyText : '请选择',
				anchor : '92.5%'
			});
	cliendBox.on('beforequery', function() {
				return false
			});
	// 乙方负责人
	secondcharge = new Ext.form.TextField({
				id : "secondcharge",
				fieldLabel : '乙方负责人',
				name : 'secondcharge',
				anchor : "85%"
			})
	// 第三方
	var thirdBox = new Ext.form.ComboBox({
				fieldLabel : '第三方',
				// store : [['1', '燃料供应商'], ['2', '设备供应商']],
//				store : clientStore,
				id : 'thirdClientId',
				name : 'con.thirdClientId',
//				valueField : "value",
//				displayField : "text",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'con.thirdClientId',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				emptyText : '请选择',
				anchor : '92.5%'
			});
	thirdBox.on('beforequery', function() {
				return false
			});
	// 项目编号
	var projectNum = new Ext.form.ComboBox({
		id : "projectNum",
		name : "",
		fieldLabel : "项目编号",
		emptyText : '',
		readOnly : true,
		hideTrigger : true,
		anchor : "85%"
			// ,
			// onTriggerClick : function(e) {
			// var args = {
			// selectModel : 'single',
			// rootNode : {
			// id : "0",
			// text : '合同类别'
			// }
			// }
			// var url = "ProjectByCode.jsp";
			// var rvo = window
			// .showModalDialog(
			// url,
			// args,
			// 'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			// if (typeof(rvo) != "undefined") {
			// Ext.get("projectNum").dom.value = rvo.prjNoShow;
			// Ext.get("projectName").dom.value = rvo.prjName;
			// Ext.get("hdnprojectNum").dom.value = rvo.prjNo
			// }
			// }
		});
	var hdnprojectNum = new Ext.form.Hidden({
				id : 'hdnprojectNum',
				name : 'con.projectId'
			});
	// 项目名称
	var projectName = new Ext.form.TextField({
				id : "projectName",
				xtype : "textfield",
				fieldLabel : '项目名称',
				name : '',
				readOnly : true,
				anchor : "85%"
			});
	// cliendBox.on('beforequery', function() {
	// return false
	// });
	// 费用来源
	var itemBox = new Ext.form.ComboBox({
				fieldLabel : '费用来源',
				store : [['1', '电力供应'], ['2', '公司']],
				id : 'itemId',
				name : 'itemId',
				valueField : "value",
				displayField : "text",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'con.itemId',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				emptyText : '请选择',
				anchor : '85%'
			});
	itemBox.on('beforequery', function() {
				return false
			});
	// 币别
	var typeBox = new Ext.form.ComboBox({
				fieldLabel : '币别',
				store : typeStore,
				id : 'currencyType',
				valueField : "currencyId",
				displayField : "currencyName",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'con.currencyType',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				emptyText : '请选择',
				anchor : '75.5%'
			});
	typeBox.on('beforequery', function() {
				return false
			});
	// 合同开始时间
	var startDate = new Ext.form.TextField({
				id : 'startDate',
				fieldLabel : "合同履行开始时间",
				name : 'con.startDate',
				readOnly : true,
				style : 'cursor:pointer',
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
				style : 'cursor:pointer',
				readOnly : true,
				anchor : "85%"
			});
	// 合同文本
	var oriFileName = {
		id : "filePath",
		xtype : "textfield",
		fieldLabel : '合同文本',
		readOnly : true,
		anchor : "95%"
	}
	// 查看
	var btnView = new Ext.Button({
				id : 'btnView',
				text : '查看',
				handler : function() {
					if (id == "") {
						Ext.Msg.alert('提示', '请选择合同');
						return false;
					}
					window.open("/power/managecontract/showConFile.action?conid="
									+ id + "&type=CON");
					// Ext.Ajax.request({
					// url : 'managecontract/showConFile.action',
					// params : {
					// conid : id,
					// type : "CON"
					// },
					// method : 'post'
					// });
				}
			});
	var btnSign = new Ext.Button({
				id : 'btnSign',
				text : '会签表',
				handler : function() {
				CheckRptPreview();
				}
			});
			// 会签票面浏览
	function CheckRptPreview() {
			var url = "/powerrpt/report/webfile/bqmis/GCContractMeetSign.jsp?conId="
					+ id;
			window.open(url);
	};
	// var formtbar = new Ext.Toolbar({
	// items : [{
	// id : 'btnMeet',
	// text : "会签表",
	// // iconCls : 'update',
	// handler : function() {
	//
	// }
	// }]
	// });
	var form = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		collapsible : true,
		width : Ext.get('div_lay').getWidth(),
		// title : '采购合同信息',
		autoHeight : true,
		// region : 'center',
		border : false,
		// tbar : formtbar,
		items : [new Ext.form.FieldSet({
					title : '基本信息',
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
														xtype : 'numberfield',
														fieldLabel : '合同id',
														readOnly : false,
														hidden : true,
														hideLabel : true,
														anchor : '85%'
													}]
										}, {
											border : false,
											layout : 'form',
											hidden : true,
											columnWidth : .6,
											items : [btnSign]
										}, {
											border : false,
											layout : 'form',
											columnWidth : 1,
											labelWidth : 110,
											items : [{
														id : 'contractName',
														name : 'con.contractName',
														xtype : 'textfield',
														fieldLabel : '合同名称',
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
														name : 'con.conYear',
														xtype : 'numberfield',
														fieldLabel : '合同年份',
														readOnly : true,
														anchor : '85%'
													}]
										}, {
											border : false,
											layout : 'form',
											columnWidth : 1,
											labelWidth : 110,
											items : [{
														id : 'conttreesNo',
														// name :
														// 'con.conttreesNo',
														xtype : 'textfield',
														fieldLabel : '合同编号',
														readOnly : true,
														anchor : '92.5%'
													}]
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
											hidden : true,
											labelWidth : 110,
											items : [secondcharge]
										}, {
											border : false,
											layout : 'form',
											columnWidth : 1,
											labelWidth : 110,
											hidden : true,
											items : [thirdBox]
										}, {
											border : false,
											layout : 'form',
											columnWidth : .5,
											labelWidth : 110,
											hidden : true,
											items : [projectNum]
										}, {
											border : false,
											layout : 'form',
											columnWidth : .5,
											hidden : true,
											labelWidth : 110,
											items : [projectName, hdnprojectNum]
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
														readOnly : true,
														anchor : '85%'
													}]
										}, {
											border : false,
											layout : 'form',
											columnWidth : .5,
											labelWidth : 110,
											items : [{
														id : 'operateName',
														xtype : 'textfield',
														fieldLabel : '承办人',
														readOnly : true,
														anchor : '85%'
													}
//													, unitBox
													]
										}, {
											border : false,
											layout : 'form',
											columnWidth : .2,
											labelWidth : 110,
											items : [isTotal]
										}, {
											border : false,
											layout : 'form',
											columnWidth : .3,
											labelWidth : 80,
											items : [typeBox]
										}, {
											border : false,
											layout : 'form',
											columnWidth : .4,
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
											items : [{
														id : 'conAbstract',
														name : 'con.conAbstract',
														xtype : 'textarea',
														fieldLabel : '合同简介',
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
											columnWidth : 0.4,
											labelWidth : 110,
											items : [oriFileName]
										}, {
											border : false,
											layout : 'form',
											columnWidth : 0.2,
											labelWidth : 80,
											items : [btnView]
										},{
											border : false,
											layout : 'form',
											columnWidth : 0.5,
											labelWidth : 110,
											items : [prosy_by]
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
							url : 'managecontract/findDocList.action'
						}),
				reader : new Ext.data.JsonReader({}, annex_item)
			});

	var annexGrid = new Ext.grid.GridPanel({
				collapsible : true,
				ds : annex_ds,
				cm : annex_item_cm,
				width : Ext.get('div_lay').getWidth(),
				tbar : new Ext.Toolbar({
							items : ['合同附件']
						}),
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
							url : 'managecontract/findDocList.action'
						}),
				reader : new Ext.data.JsonReader({}, Credent_item)
			});

	var CredentGrid = new Ext.grid.GridPanel({
				collapsible : true,
				ds : Credent_ds,
				cm : Credent_item_cm,
				tbar : new Ext.Toolbar({
							items : ['合同凭据']
						}),
				split : true,
				height : 150,
				width : Ext.get('div_lay').getWidth(),
				border : false
			});
	var layout = new Ext.Panel({
				autoWidth : true,
				autoHeight : true,
				border : false,
				autoScroll : true,
				split : true,
				items : [form, annexGrid, CredentGrid]
			});
	layout.render(Ext.getBody());
})