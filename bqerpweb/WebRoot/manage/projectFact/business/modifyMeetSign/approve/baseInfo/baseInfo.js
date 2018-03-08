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
	//var conId = getParameter("conId");
	if (id != null && id !="") {
		Ext.Ajax.request({
			url : 'managecontract/findModifyBaseInfo.action',
			params : {
				modifyId : id
			},
			method : 'post',
			waitMsg : '正在加载数据...',
			success : function(result, request) {
				var o = eval('(' + result.responseText + ')');
				myform.getForm().loadRecord(o);
				entryId=o.data.workFlowNo;
				if (o.data.workflowStatus == 2) {
					Ext.get('btnSign').dom.disabled = true
				}
				oriFileName.setValue((o.data.filePath == null)?"":o.data.filePath);
				if(o.data.filePath == null || o.data.filePath == ""){
							Ext.getCmp("btnView").setVisible(false);
//							Ext.get('oriFileName').setVisible(false);
							var text = "无合同文本";
							oriFileName.setValue(text);
							}
				//alert(entryId);
				storeAnnex.load();
				storeProof.load();
			},
			failure : function(result, request) {
				Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
			}
		});
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
		name : 'operateName',
		xtype : 'textfield',
		id : 'operateName',
		mode : 'remote',
		readOnly : true,
		anchor : "90%"
	};

	// 申请部门
	var operateDepCode = {
		fieldLabel : '申请部门',
		name : 'operateDeptName',
		xtype : 'textfield',
		id : 'operateDeptName',
		readOnly : true,
		anchor : "90%"
	};

	var operateLeadBy = {
		fieldLabel : '部门负责人',
		name : 'operateLeadName',
		xtype : 'textfield',
		id : 'operateLeadName',
		readOnly : true,
		anchor : "86%"
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
				anchor : '90%'
			});
	typeBox.on('beforequery', function() {
				return false
			});
//	var currencyType = {
//		fieldLabel : '币别',
//		name : 'currencyType',
//		xtype : 'combo',
//		id : 'currencyType',
//		store :typeStore,
//		mode : 'remote',
//		hiddenName : 'apply.currencyType',
//		readOnly : true,
//		editable : false,
//		anchor : "90%"
//	};

	var actAmount = {
		id : "actAmount",
		xtype : "numberfield",
		fieldLabel : '原合同金额',
		readOnly : true,
		name : 'apply.actAmount',
		anchor : "90%"
	}
	var modiyActAmount = {
		id : "modiyActAmount",
		xtype : "numberfield",
		fieldLabel : '现合同金额',
		name : 'apply.modiyActAmount',
		readOnly : true,
		anchor : "86%"
	}
	var startDate = new Ext.form.TextField({
		id : "startDate",
		fieldLabel : '合同履行开始时间',
		name : 'apply.startDate',
		style : 'cursor:pointer',
		readOnly : true,
		editable : false,
		anchor : "90%",
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d 00:00:00',
					dateFmt : 'yyyy-MM-dd HH:mm:ss',
					alwaysUseStartDate : true
				});
			}
		}	
	})
	var endDate = new Ext.form.TextField({
		id : "endDate",
		fieldLabel : '合同履行结束时间',
		readOnly : true,
		editable : false,
		anchor : "90%",
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d 00:00:00',
					dateFmt : 'yyyy-MM-dd HH:mm:ss',
					alwaysUseStartDate : true
				});
			}
		}	
	})
	var conomodifyName = {
		id : "conomodifyName",
		xtype : "textarea",
		fieldLabel : '变更原因',
		name : 'apply.conomodifyName',
		readOnly : true,
		anchor : "95%"
	}
	var entryBy = {
		id : "entryName",
		xtype : "textfield",
		fieldLabel : '起草人',
		name : 'entryName',
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
	var oriFileName = new Ext.form.TextField({
		id : "oriFileName",
		xtype : "textfield",
		fieldLabel : '合同变更文本',
		readOnly : true,
//		hideLabel:true,
		name : 'apply.oriFileName',
		width : 300,
		height : 22
	})

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
	
	   // 票面浏览
	function CheckRptPreview() {
		if (id == "" || id==null)
				{
					Ext.Msg.alert('提示','请在审批列表中选择待签字的合同！');
					return false;
				}
			var url = "/powerrpt/report/webfile/bqmis/GCContractModifyMeetSign.jsp?modifyId="
					+ id;
			window.open(url);

		
	};	
	
	var formtbar = new Ext.Toolbar({
		items : [{
			id : 'btnSign',
			text : "签字",
			iconCls : 'write',
			handler : function() {
				if (id == "" || id==null)
				{
					Ext.Msg.alert('提示','请在审批列表中选择待签字的合同！');
					return false;
				}
				url = "../../../../../../manage/projectFact/business/modifyMeetSign/approve/meetSign/sign.jsp?entryId="
						+ entryId+"&id="+ id;
				var o = window.showModalDialog(url, '',
						'dialogWidth=800px;dialogHeight=500px;status=no');
				if (o) {
//					var url1= "manage/projectFact/business/modifyMeetSign/approve/baseInfo/baseInfo.jsp";
//					parent.document.all.iframe2.src=url1;
//					var url = "manage/projectFact/business/modifyMeetSign/approve/arrpoveList/list.jsp";
//					parent.document.all.iframe1.src = url ;
					parent.iframe1.document.getElementById("btnQuery").click();
					parent.Ext.getCmp("maintab").setActiveTab(1);
//					var url1 = "manage/projectFact/business/modifyMeetSign/approve/baseInfo/baseInfo.jsp";
					parent.document.all.iframe2.src = "";
					parent.Ext.getCmp("maintab").setActiveTab(0);
//					var url = "manage/projectFact/business/contractMeetSign/approve/arrpoveList/list.jsp";
//					parent.document.all.iframe1.src = url;
					
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
				if (id == "" || id==null)
				{
					Ext.Msg.alert('提示','请在审批列表中选择待签字的合同！');
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
		}]
	});
	var myform = new Ext.FormPanel({
		frame : true,
		labelAlign : 'left',
		bodyStyle : 'padding:5px',
		tbar : formtbar,
		items : [{
			xtype : 'fieldset',
			title : '基本信息',
			// width : 800,
			labelAlign : 'right',
			labelWidth : 110,
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
					columnWidth : 0.35,
					layout : 'form',
					items : [operateLeadBy, modiyActAmount]
				}]
			}, {
				layout : 'column',
				items : [{
					columnWidth : 0.5,
					layout : 'form',
					items : [startDate]
				}, {
					columnWidth : 0.5,
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
					columnWidth : 0.65,
					layout : 'form',
					items : [oriFileName]
				}, {
					columnWidth : 0.3,
					layout : 'form',
					items : [btnView]
				}]
			}]

		}]
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
		title :'合同附件',
		collapsible : true,
		autoScroll : true,
		store : storeAnnex,
		width : Ext.get('div_lay').getWidth(),
		height : 200,
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
		}, {
			width : 80,
			header : "查看",
			sortable : true

		}, new Ext.grid.RowNumberer({
			header : '项次号',
			width : 50,
			align : 'center'
		}), {
			header : "名称",
			width : 150,
			sortable : true,
			dataIndex : 'docName',
			align : 'center'
		}, {
			header : "备注",
			width : 150,
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
			width : 150,
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
		title :'合同凭据',
		collapsible : true,
		autoScroll: true,
		height : 200,
		width : Ext.get('div_lay').getWidth(),
//		autoHeight : true,
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
		}, {

			header : "查看",
			sortable : true,
			width : 80
		}, new Ext.grid.RowNumberer({
			header : '项次号',
			width : 50,
			align : 'center'
		}), {
			header : "名称",
			width : 150,
			sortable : true,
			dataIndex : 'docName',
			align : 'center'
		}, {
			header : "备注",
			width : 150,
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
			width : 150,
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

	var layout = new Ext.Panel({
		autoWidth : true,
		autoHeight : true,
		border : false,
		autoScroll : true,
		split : true,
		items : [myform, gridAnnex, gridProof]
	});
	layout.render(Ext.getBody());
});