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
	var typeChooseId = getParameter("typeChoose");
//	alert(id);
	var entryId;
	var conId = getParameter("conId");
	if (id != null) {
		Ext.Ajax.request({
			url : 'managecontract/findModifyBaseInfo.action',
			params : {
				modifyId : id
			},
			method : 'post',
			waitMsg : '正在加载数据...',
			success : function(result, request) {
				var o = eval('(' + result.responseText + ')');
//				alert(result.responseText)
				myform.getForm().loadRecord(o);
				//update by drdu  2009/05/04
				if (o.data.operateBy != null) {
					Ext.getCmp('operateBy').setValue(o.data.operateBy);
					Ext.form.ComboBox.superclass.setValue.call(Ext
							.getCmp('operateBy'), o.data.operateName);
				}
				if (o.data.operateDepCode != null) {
					Ext.getCmp('operateDepCode')
							.setValue(o.data.operateDepCode);
					Ext.form.ComboBox.superclass.setValue.call(Ext
							.getCmp('operateDepCode'), o.data.operateDeptName);
				}
				if (o.data.operateLeadBy != null) {
					Ext.getCmp('operateLeadBy').setValue(o.data.operateLeadBy);
					Ext.form.ComboBox.superclass.setValue.call(Ext
							.getCmp('operateLeadBy'), o.data.operateLeadName);
				}
				if (o.data.entryName != null) {
					Ext.get("entryBy").dom.value = o.data.entryName;
				}
				if (o.data.startDate != null) {
					Ext.get("startDate").dom.value = o.data.startDate.substring(0, 10);

				}
				if (o.data.endDate != null) {
					Ext.get("endDate").dom.value = o.data.endDate.substring(0,10);
				}
				Ext.get("oriFileName").dom.value = (o.data.filePath == null?"":o.data.filePath);
				// alert(o.data.conomodifyName);
				if(o.data.filePath == null || o.data.filePath == ""){
							Ext.getCmp("btnView").setVisible(false);
							}
//							alert(o.data.workFlowNo)
				entryId = o.data.workFlowNo;
//				Ext.get("startDate").dom.value = o.data.startDate;
//				Ext.get("endDate").dom.value = o.data.endDate;
				storeAnnex.load();
				storeProof.load();
			},
			failure : function(result, request) {
				Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
			}
		});
//		myform.getForm().load({
//			url : 'managecontract/findModifyBaseInfo.action?modifyId=' + id,
//			success : function(form, action) {
//				var o = eval("(" + action.response.responseText + ")");
//				if (o.data.operateBy != null) {
//					Ext.getCmp('operateBy').setValue(o.data.operateBy);
//					Ext.form.ComboBox.superclass.setValue.call(Ext
//							.getCmp('operateBy'), o.data.operateName);
//
//				}
//				if (o.data.operateDepCode != null) {
//					Ext.getCmp('operateDepCode')
//							.setValue(o.data.operateDepCode);
//					Ext.form.ComboBox.superclass.setValue.call(Ext
//							.getCmp('operateDepCode'), o.data.operateDeptName);
//				}
//				if (o.data.operateLeadBy != null) {
//					Ext.getCmp('operateLeadBy').setValue(o.data.operateLeadBy);
//					Ext.form.ComboBox.superclass.setValue.call(Ext
//							.getCmp('operateLeadBy'), o.data.operateLeadName);
//				}
//				if (o.data.entryName != null) {
//					Ext.get("entryBy").dom.value = o.data.entryName;
//				}
//				if (o.data.startDate != null) {
//					Ext.get("startDate").dom.value = o.data.startDate
//							.substring(0, 10);
//
//				}
//				if (o.data.endDate != null) {
//					Ext.get("endDate").dom.value = o.data.endDate.substring(0,
//							10);
//				}
//				storeAnnex.load();
//				storeProof.load();
//			}
//		});
	}

	// ------------定义form---------------
	// 合同编号
	var conModifyNo = {
		id : "conModifyNo",
		xtype : "textfield",
		fieldLabel : '合同变更号',
		readOnly : true,
		name : 'apply.conModifyNo',
		anchor : "90%"
	}

	// 变更类型
	var conomodifyType = {
		id : "conomodifyType",
		xtype : "combo",
		name : 'apply.conomodifyType',
		allowBlank : false,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
			fields : ['text', 'value'],
			data : [["合同变更", "1"], ["合同解除", "2"]]
		}),
		value : '1',
		hiddenName : 'apply.conomodifyType',
		displayField : 'text',
		valueField : 'value',
		fieldLabel : "变更类型",
		mode : 'local',
		emptyText : '请选择',
		blankText : '请选择',
		readOnly : true,
		anchor : "90%"
	}
	// 合同编号
	var conttreesNo = {
		fieldLabel : '合同编号',
		name : 'conttreesNo',
		xtype : 'textfield',
		id : 'conttreesNo',
		readOnly : true,
		anchor : "95%"
	};

	// 合同名称
	var contractName = {
		id : "contractName",
		xtype : "textfield",
		fieldLabel : '合同名称',
		name : 'apply.contractName',
		readOnly : true,
		anchor : "95%"
	}
	// 变更经办人
	var operateBy = {
		fieldLabel : '变更经办人',
		name : 'operateBy',
		xtype : 'textfield',
		id : 'operateBy',
		mode : 'remote',
		hiddenName : 'apply.operateBy',
		readOnly : true,
		anchor : "95%"
	};

	// 申请部门
	var operateDepCode = {
		fieldLabel : '申请部门',
		name : 'operateDepCode',
		xtype : 'textfield',
		id : 'operateDepCode',
		hiddenName : 'apply.operateDepCode',
		readOnly : true,
		anchor : "95%"
	};

	var operateLeadBy = {
		fieldLabel : '部门负责人',
		name : 'operateLeadBy',
		xtype : 'textfield',
		id : 'operateLeadBy',
		hiddenName : 'apply.operateLeadBy',
		readOnly : true,
		anchor : "100%"
	};

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
				hiddenName : 'apply.currencyType',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				emptyText : '请选择',
				anchor : '95%'
			});
	typeBox.on('beforequery', function() {
				return false
			});
//	var currencyType = {
//		fieldLabel : '币别',
//		name : 'currencyType',
//		xtype : 'combo',
//		id : 'currencyType',
//		store : new Ext.data.SimpleStore({
//			fields : ['id', 'name'],
//			data : [[]]
//		}),
//		mode : 'remote',
//		hiddenName : 'apply.currencyType',
//		readOnly : true,
//		editable : false,
//		anchor : "95%"
//	};

	var actAmount = {
		id : "actAmount",
		xtype : "numberfield",
		fieldLabel : '原合同金额',
		readOnly : true,
		name : 'apply.actAmount',
		anchor : "95%"
	}
	var modiyActAmount = {
		id : "modiyActAmount",
		xtype : "numberfield",
		fieldLabel : '现合同金额',
		name : 'apply.modiyActAmount',
		readOnly : true,
		anchor : "100%"
	}
	var startDate = {
		id : "startDate",
		xtype : "datefield",
		format : 'Y-m-d',
		fieldLabel : '合同履行开始时间',
		name : 'apply.startDate',
		readOnly : true,
		editable : false,
		anchor : "90%"
	}
	
	var endDate = {
		id : "endDate",
		xtype : "datefield",
		format : 'Y-m-d',
		fieldLabel : '合同履行结束时间',
		readOnly : true,
		editable : false,
		anchor : "90%"
	}
	var conomodifyName = {
		id : "conomodifyName",
		xtype : "textarea",
		fieldLabel : '变更原因',
		name : 'apply.conomodifyName',
		readOnly : true,
		anchor : "95%"
	}
	var entryBy = {
		id : "entryBy",
		xtype : "textfield",
		fieldLabel : '起草人',
		name : 'apply.entryBy',
		readOnly : true,
		anchor : "90%"
	}
	var entryDate = {
		id : "entryDate",
		xtype : "datefield",
		format : 'Y-m-d h:i:s',
		fieldLabel : '起草日期',
		name : 'apply.entryDate',
		readOnly : true,
		anchor : "90%"
	}
	var oriFileName = {
		id : "oriFileName",
		xtype : "textfield",
		fieldLabel : '合同变更文本',
//		inputType : "file",
		readOnly : true,
		name : 'apply.oriFileName',
		anchor : "95%"
	}

	var btnView = new Ext.Button({
		id : 'btnView',
		text : '查看',
		handler : function() {
			if (id == "") {
				Ext.Msg.alert('提示', '请选择合同');
				return false;
			}
			window.open("/power/managecontract/showConFile.action?conid=" + id
					+ "&type=MCON");
		}
	});

	// 会签票面浏览
	function CheckRptPreview() {
			if (id == "") {
				Ext.Msg.alert('提示', '请选择合同');
				return false;
			}
			var url = "/powerrpt/report/webfile/CGContractModifyMeetSign.jsp?modifyId="+id
			window.open(url);
	};
	var formtbar = new Ext.Toolbar({
		items : [{
			id : 'btnAdd',
			text : "申请归档",
			iconCls : 'add',
			handler : function() {
				if (id == "" || id == null) {
					Ext.MessageBox.alert('提示', '请在合同列表页面选择一条需要归档的合同!');
					return false;
				}
				Ext.Msg.confirm('提示',
						'确认将该合同归档吗?', function(b) {
							if (b == "yes") {
								Ext.Ajax.request({
									url : 'managecontract/contractArchive.action',
									params : {
										typeChooseId : typeChooseId,
										conId : id
									},
									method : 'post',
									waitMsg : '正在保存数据...',
									success : function(result, request) {
										parent.iframe1.document.getElementById("btnQuery").click();
										parent.Ext.getCmp("maintab").setActiveTab(1);
										parent.document.all.iframe2.src = "";
//										parent.Ext.getCmp("maintab").setActiveTab(2);
//										parent.document.all.iframe3.src = "";
										parent.Ext.getCmp("maintab").setActiveTab(0);
									},
									failure : function(result, request) {
										Ext.MessageBox.alert('错误',
												'操作失败,请联系管理员!');
									}
								});
							}

						});
			}
		}, '-', {
			id : 'btnSave',
			text : "退回意见",
			iconCls : 'list',
			handler : function() {
				if (id == "" || id == null) {
					Ext.MessageBox.alert('提示', '请在合同列表页面选择一条需要查看的合同!');
					return false;
				}
				back_ds.load({
							params : {
								conId : id,
								type : 'CMOD'
							}
						});
				docwin.show();
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
		}, '-', {
			id : 'btnMeet',
			text : "会签表",
			iconCls : 'pdfview',
			handler : function() {
				CheckRptPreview()
			}
		}]
	});
	
	var myform = new Ext.FormPanel({
		frame : true,
		labelAlign : 'left',
		bodyStyle : 'padding:5px',
		tbar : formtbar,
		items : [new Ext.form.FieldSet({
			xtype : 'fieldset',
			title : '基本信息',
			//width : 800,
			labelAlign : 'right',
			labelWidth : 100,
			collapsible : true,// add
			autoHeight : true,
			bodyStyle : Ext.isIE
					? 'padding:0 0 5px 15px;'
					: 'padding:10px 15px;',
			border : true,
			style : {
				"margin-left" : "10px",
				"margin-right" : Ext.isIE6
						? (Ext.isStrict ? "-10px" : "-13px")
						: "0"
			},

			items : [{
				layout : 'column',
				items : [{
					columnWidth : 0.5,
					layout : 'form',
					items : [conModifyNo]
				}, {
					columnWidth : 0.5,
					layout : 'form',
					items : [conomodifyType]
				}]
			}, {
				layout : 'column',
				items : [{
					columnWidth : 1,
					layout : 'form',
					items : [conttreesNo]
				}]
			}, {
				layout : 'column',
				items : [{
					columnWidth : 1,
					layout : 'form',
					items : [contractName]
				}]
			}, {
				layout : 'column',
				items : [{
					columnWidth : 0.3,
					layout : 'form',
					items : [operateBy, typeBox]
				}, {
					columnWidth : 0.35,
					layout : 'form',
					items : [operateDepCode, actAmount]
				}, {
					columnWidth : 0.3,
					layout : 'form',
					items : [operateLeadBy, modiyActAmount]
				}]
			}, {
				layout : 'column',
				items : [{
					columnWidth : 0.5,
					layout : 'form',
						labelWidth : 110,
					items : [startDate]
				}, {
					columnWidth : 0.5,
						labelWidth : 110,
					layout : 'form',
					items : [endDate]
				}]
			}, {
				layout : 'column',
				items : [{
					columnWidth : 1,
					layout : 'form',
					items : [conomodifyName]
				}]
			}, {
				layout : 'column',
				items : [{
					columnWidth : 0.5,
					layout : 'form',
					items : [entryBy]
				}, {
					columnWidth : 0.5,
					layout : 'form',
					items : [entryDate]
				}]
			}, {
				layout : 'column',
				items : [{
					columnWidth : 0.5,
					layout : 'form',
					items : [oriFileName]
				}, {
					columnWidth : 0.3,
					layout : 'form',
					items : [btnView]
				}]
			}]

		})]
	})

	// --------------------------------
	// --------------定义合同附件grid------------------------------------------
	var MyRecordAnnex = Ext.data.Record.create([{
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
		name : 'oriFile'
	}, {
		name : 'lastModifiedName'
	}, {
		name : 'lastModifiedDate'
	}]);

	var dataProxyAnnex = new Ext.data.HttpProxy({
				url : 'managecontract/findDocModifyList.action?modifyId=' + id
						+ '&docType=MCONATT'
			});

	var theReaderAnnex = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"

	}, MyRecordAnnex);

	var storeAnnex = new Ext.data.Store({

		proxy : dataProxyAnnex,

		reader : theReaderAnnex

	});
	var smAnnex = new Ext.grid.CheckboxSelectionModel();

	var gridAnnex = new Ext.grid.GridPanel({
		region : "center",
		store : storeAnnex,
		width : Ext.get('div_lay').getWidth(),
		height : 200,
		title : '合同附件',
		columns : [smAnnex, {

			header : "ID",
			sortable : true,
			dataIndex : 'conDocId',
			hidden : true
		}, {

			header : "合同ID",
			sortable : true,
		
			dataIndex : 'keyId',
			hidden : true
		}, new Ext.grid.RowNumberer({
			header : '项次号',
			sortable : true,
			width : 50,
			align : 'center'
		}), {
			header : "名称",
			
			sortable : true,
			dataIndex : 'docName',
			align : 'center'
		}, {
			header : "备注",
			
			sortable : true,
			dataIndex : 'docMemo',
			align : 'center'
		}, {
			header : "原始文件名",
			
			sortable : true,
			dataIndex : 'oriFileName',
			align : 'center',
			hidden : true
		}, {
			header : "原始文件",
			
			sortable : true,
			dataIndex : 'oriFile',
			align : 'center'
		}, {
			header : "上传日期",
			
			sortable : true,
			dataIndex : 'lastModifiedDate',
			align : 'center'
		}, {
			header : "上传人",
		
			sortable : true,
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
									+ id + "&conDocId="+val+"&type=MCONATT');\"/>查看附件</a>"
					} else {
						return "";
					}
				}
			}],
		sm : smAnnex
	});
	// ---------------------------------------------
	// -------------------定义合同凭据grid------------
	var MyRecordProof = Ext.data.Record.create([{
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
		name : 'oriFile'
	}, {
		name : 'lastModifiedName'
	}, {
		name : 'lastModifiedDate'
	}]);

	var dataProxyProof = new Ext.data.HttpProxy(

			{
				url : 'managecontract/findDocModifyList.action?modifyId=' + id
						+ '&docType=MCONEVI'
			}

	);

	var theReaderProof = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"

	}, MyRecordProof);

	var storeProof = new Ext.data.Store({

		proxy : dataProxyProof,

		reader : theReaderProof

	});
	// 分页
	var smProof = new Ext.grid.CheckboxSelectionModel();

	var gridProof = new Ext.grid.GridPanel({
		region : "center",
		store : storeProof,
		width : Ext.get('div_lay').getWidth(),
		autoHeight : 200,
		title : '合同凭据',
		columns : [smProof, {

			header : "ID",
			sortable : true,
			width : 50,
			dataIndex : 'conDocId',
			hidden : true
		}, {

			header : "合同ID",
			sortable : true,
			width : 50,
			dataIndex : 'keyId',
			hidden : true
		}, new Ext.grid.RowNumberer({
			header : '项次号',
			width : 50,
			sortable : true,
			align : 'center'
		}), {
			header : "名称",
			sortable : true,
			dataIndex : 'docName',
			align : 'center'
		}, {
			header : "备注",
			sortable : true,
			dataIndex : 'docMemo',
			align : 'center'
		}, {
			header : "原始文件名",
			sortable : true,
			dataIndex : 'oriFileName',
			align : 'center',
			hidden : true
		}, {
			header : "原始文件",
			sortable : true,
			dataIndex : 'oriFile',
			align : 'center'
		}, {
			header : "上传日期",
			sortable : true,
			dataIndex : 'lastModifiedDate',
			align : 'center'
		}, {
			header : "上传人",
			sortable : true,
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
									+ id + "&conDocId="+val+"&type=MCONEVI');\"/>查看附件</a>"
					} else {
						return "";
					}
				}
			
			}],
		sm : smProof
	});
	// 布局
//	var myPanel = new Ext.Panel({
//		// region : "center",
//		// layout : 'form',
//		autoScroll : true,
//		items : [myform, gridAnnex, gridProof]
//	});
//
//	new Ext.Viewport({
//		enableTabScroll : true,
//		autoScroll : true,
//		collapsible : true,
//		layout : "fit",
//		items : [myPanel]
//	});
	var layout = new Ext.Panel({
		autoWidth : true,
		autoHeight : true,
		border : false,
		autoScroll : true,
		split : true,
		items : [myform, gridAnnex, gridProof]
	});
	layout.render(Ext.getBody());
	
	var back_item = Ext.data.Record.create([{
				name : 'opinionId'
			}, {
				name : 'keyId'
			}, {
				name : 'fileType'
			}, {
				name : 'opinion'
			}, {
				name : 'gdWorker'
			}, {
				name : 'gdWorkerName'
			}, {
				name : 'withdrawalTime'
			}]);

	var back_item_cm = new Ext.grid.ColumnModel([{
				header : '退回时间',
				dataIndex : 'withdrawalTime',
				width : 50,
				align : 'center'
			}, {
				header : '退回意见',
				dataIndex : 'opinion',
				align : 'center'
			}, {
				header : '归档人',
				dataIndex : 'gdWorkerName',
				width : 30,
				align : 'center'
			}]);
			
	back_item_cm.defaultSortable = true;
	var back_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'managecontract/getFileOpinionList.action'
						}),
				reader : new Ext.data.JsonReader({}, back_item)
			});
	var backGrid = new Ext.grid.GridPanel({
				ds : back_ds,
				cm : back_item_cm,
				width : 540,
				height : 290,
				split : true,
				border : false,
				viewConfig : {
					forceFit : true
				}
			});
	var docwin = new Ext.Window({
				title : '归档退回意见',
				modal : true,
				height : 300,
				width : 550,
				closeAction : 'hide',
				items : [backGrid],
				buttons : [{
							id : 'btnClose',
							text : "关闭",
							handler : function() {
								docwin.hide();
							}
						}]
			})
});