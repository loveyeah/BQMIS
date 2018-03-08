Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	// 币别
	var typeStore = new Ext.data.JsonStore({
				url : 'managecontract/getConCurrencyList.action',
				root : 'list',
				fields : ['currencyId', 'currencyName']
			})
	typeStore.load();

	var id = getParameter("id");
	var entryId;
	var sum;
	var prosy_by;  // 委托人	
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
//				unitBox.setValue((o.data.operateLeadName == null) ? "" :o.data.operateLeadName);
				itemBox.setValue(o.data.itemName);
//				Ext.get('prosy_byName').dom.value = o.data.prosy_byName;
//				Ext.get("secondcharge").dom.value = o.data.constructionName == null
//						? ""
//						: o.data.constructionName;
				Ext.get("projectNum").dom.value = (o.data.projectId == null)
						? ""
						: o.data.projectId;
				if (o.data.isSum == "Y") {
					Ext.get('isTotal').dom.checked = true;
					Ext.get('isTotal').dom.readOnly = true
					Ext.get('isTotal').dom.disabled = true
				} else {
					Ext.get('isTotal').dom.checked = false;
					Ext.get('isTotal').dom.readOnly = true
				}
//				if (o.data.workflowStatus == 2) {
//					Ext.get('btnSign').dom.disabled = false
//				}
				cliendBox.setValue((o.data.cliendName == null)?"":o.data.cliendName);
				thirdBox.setValue((o.data.thirdClientName == null)?"":o.data.thirdClientName);
				projectNum.setValue(o.data.prjShow);
				//entryId = o.data.workflowNo;
				entryId = o.data.workflowNoDg;
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
			},
			failure : function(result, request) {
				Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
			}
		});
	};

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
				anchor : '85.5%'
			});

	// 有无总金额
	// if(actAmount.getValue()>0){
	// isTotal.checked=true;
	// } else {
	// isTotal.checked=false;
	// }
	var isTotal = new Ext.form.Checkbox({
				id : 'isTotal',
				// name : 'con.isSum',
				fieldLabel : '有无总金额',
				checked : false,
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

	// 合作伙伴
	var cliendBox = new Ext.form.ComboBox({
				fieldLabel : '合作伙伴',
				id : 'cliendId',
				name : 'cliendId',
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'con.cliendId',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				emptyText : '请选择',
				anchor : "85%"

			});
	cliendBox.on('beforequery', function() {
				return false
			});

	// 乙方负责人
	secondcharge = new Ext.form.TextField({
				id : "secondcharge",
				fieldLabel : '乙方负责人',
				name : 'secondcharge',
				readOnly : true,
				anchor : "85%"
			})
	// 第三方
	var thirdBox = new Ext.form.ComboBox({
				fieldLabel : '第三方',
				// store : [['1', '燃料合作伙伴'], ['2', '设备合作伙伴']],
				id : 'thirdClientId',
				name : 'con.thirdClientId',
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
		// hiddenName : "equJWo.projectNum",
		fieldLabel : "项目编号",
		emptyText : '',
		readOnly : true,
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
				// triggerAction : 'all',
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
			
// 委托开始日期
	var prosyStartDate = new Ext.form.TextField({
				id : 'prosyStartDate',
				fieldLabel : "委托开始时间",
				name : 'con.prosyStartDate',
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
				name : 'con.prosyEndDate',
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
			
	// 合同文本
	var oriFileName = {
		id : "filePath",
		xtype : 'fileuploadfield',
		name : "conFile",
		// xtype : "textfield",
		fieldLabel : '合同文本',
		// inputType : "file",
		// readOnly : true,
		height : 22,
		anchor : "95%",
		buttonCfg : {
			text : '浏览...',
			iconCls : 'upload-icon'
		}
	}
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

	// var btnSubmit = new Ext.Button({
	// id : 'btnSubmit',
	// text : '上传',
	// handler : function() {
	// if (id == "" || Ext.get('filePath').dom.value == "") {
	// Ext.Msg.alert('提示', '请选择合同');
	// return false;
	// } else {
	// form.getForm().submit({
	// url : 'managecontract/modifyMeetConInfo.action',
	// params : {
	// conid : id,
	// filePath : Ext.get('filePath').dom.value
	// },
	// method : 'post',
	// waitMsg : '正在上传...',
	// success : function(form, action) {
	// Ext.MessageBox.alert('提示', '上传成功!');
	// },
	// failure : function(form, action) {
	// Ext.MessageBox.alert('错误',
	// '操作失败,请联系管理员!');
	// }
	// })
	// // Ext.Ajax.request({
	// // url : 'managecontract/modifyMeetConInfo.action',
	// // params : {
	// // conid : id,
	// // // filePath : Ext.get('filePath').dom.value
	// // conFile : oriFileName.value
	// // },
	// // method : 'post',
	// // waitMsg : '正在上传...',
	// // success : function(result, request) {
	// // Ext.MessageBox.alert('提示', '上传成功!');
	// // },
	// // failure : function(result, request) {
	// // Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
	// // }
	// // });
	// }
	// }
	// });

	// 会签票面浏览
	function CheckRptPreview() {
			var url = "/powerrpt/report/webfile/bqmis/GCContractMeetSign.jsp?conId="
					+ id;
			window.open(url);

		
	};
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
				url = "../../../../../../manage/projectFact/business/contractDelegation/approve/meetSign/sign.jsp?entryId="
						+ entryId + "&conid=" + id + "&sum=" + sum;
				var o = window.showModalDialog(url, '',
						'dialogWidth=800px;dialogHeight=530px;status=no');
				if (o) {
					id = "";
					entryId = "";
					parent.iframe1.document.getElementById("btnQuery").click();
					// parent.Ext.getCmp("maintab").setActiveTab(0);
					parent.Ext.getCmp("maintab").setActiveTab(1);
					// var url =
					// "manage/projectFact/business/contractMeetSign/approve/arrpoveList/list.jsp";
					 parent.document.all.iframe2.src = "";
//					var url1 = "manage/projectFact/business/contractMeetSign/approve/conInfo/conInfo.jsp";
//					parent.document.all.iframe2.src = url1;
					parent.Ext.getCmp("maintab").setActiveTab(0);

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
				if (entryId == null || entryId == "") {
					Ext.Msg.alert('提示', '流程尚未启动！');
				} else {
					var url = "/power/workflow/manager/show/show.jsp?entryId="
							+ entryId;
					window.open(url);
				}
			}
		}]
	});
	var form = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		collapsible : true,
		width : Ext.get('div_lay').getWidth(),
		// title : '采购合同信息',
		autoHeight : true,
		// region : 'center',
		border : false,
		//tbar : formtbar,
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
														value : '自动生成',
														readOnly : true,
														allowBlank : true,
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
														fieldLabel : '经办部门',
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
														fieldLabel : '经办人',
														readOnly : true,
														anchor : '85%'
													}
//													, unitBox
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
										}
//										, {
//											border : false,
//											layout : 'form',
//											columnWidth : 1,
//											labelWidth : 110,
//											items : [{
//														id : 'prosy_byName',
//						                                hidden : true,
//														xtype : 'trigger',
//														fieldLabel : '委托人',
//														readOnly : true,
//														// allowBlank : false,
//														anchor : '92.5%'
//							                          }]
//										}
										,{
											columnWidth : .5,
											layout : 'form',
											labelWidth : 110,
											border : false,
											hidden : true,
											items : [prosyStartDate]
										},{
											columnWidth : .5,
											layout : 'form',
											labelWidth : 110,
											border : false,
											hidden : true,
											items : [prosyEndDate]
										},{
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
										}
								// , {
								// border : false,
								// layout : 'form',
								// columnWidth : 0.05,
								// labelWidth : 10,
								// items : [btnSubmit]
								// }
								]
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
				title : '合同凭据',
				collapsible : true,
				ds : Credent_ds,
				cm : Credent_item_cm,
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