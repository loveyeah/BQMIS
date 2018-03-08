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
	// 币别 modify by drdu 2009/05/05
	var currencyTypeStore = new Ext.data.JsonStore({
				url : 'managecontract/getConCurrencyList.action',
				root : 'list',
				fields : ['currencyId', 'currencyName']
			});
	currencyTypeStore.load();

	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';

	var id = getParameter("id");
	// var revoke = getParameter("revoke");
	var status = false;
	var check = "add";
	var conId = "";
	conId = getParameter("conId");
	var sessWorname;
	var sessWorcode;
	var sessDeptCode;
	var sessDeptName;
	var operateBy;
	var entryBy;
	var operateDepCode;
	var entryId;
	var deptId = -1;
	// alert(id);
	// alert(conId);
	if (id != null && id != "") {
		check = "update";
	} else {
		parent.Ext.getCmp("maintab").setActiveTab(2);
		parent.Ext.getCmp("maintab").setActiveTab(1);
	}

	var workflowStatus = getParameter("workflowStatus");
	// ------------定义form---------------
	// 合同编号
	var conModifyNo = {
		id : "conModifyNo",
		xtype : "textfield",
		fieldLabel : '合同变更号',
		readOnly : true,
		name : 'apply.conModifyNo',
		anchor : "90%",
		value : '自动生成'
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
		xtype : 'combo',
		id : 'conttreesNo',
		store : new Ext.data.SimpleStore({
					fields : ['id', 'name'],
					data : [[]]
				}),
		mode : 'remote',
		hiddenName : 'apply.conttreesNo',
		allowBlank : false,
		editable : false,
		anchor : "95%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "conselect.jsp";
			var con = window.showModalDialog(url, '',
					'dialogWidth=600px;dialogHeight=400px;status=no');
			if (typeof(con) != "undefined") {
				conId = con.conId;
				Ext.getCmp('conttreesNo').setValue(con.conNo);
				Ext.get("contractName").dom.value = con.conName;
				Ext.getCmp('operateBy').setValue(con.operateBy);
				Ext.form.ComboBox.superclass.setValue.call(Ext
								.getCmp('operateBy'), con.operateName);
				Ext.getCmp('operateDepCode').setValue(con.operateDepCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
								.getCmp('operateDepCode'), con.operateDeptName);
				Ext.getCmp('operateLeadBy').setValue(con.operateLeadName);
				// Ext.form.ComboBox.operateLeadBy.setValue.call(Ext
				// .getCmp('operateLeadBy'), con.operateLeadName);
				Ext.getCmp('currencyType').setValue(con.currencyType);
				Ext.get("actAmount").dom.value = con.actAmount;
				// Ext.get("startDate").dom.value = con.startDate.substring(0,
				// 10);
				// Ext.get("endDate").dom.value = con.endDate.substring(0, 10);
				// modifyBy ywliu 2009/04/24
				Ext.get("startDate").dom.value = con.startDate;
				Ext.get("endDate").dom.value = con.endDate;
				hidunitBox.setValue(con.operateLeadBy);
				deptId = con.deptId;
			}

		}
	};

	// 合同名称
	var contractName = {
		id : "contractName",
		xtype : "textfield",
		fieldLabel : '合同名称',
		name : 'apply.contractName',
		allowBlank : false,
		readOnly : true,
		anchor : "95%"
	}

	// 部门负责人 modify by bjxu 20090615

	var operateLeadBy = new Ext.form.ComboBox({
		fieldLabel : '部门负责人',
		id : 'operateLeadBy',
		mode : 'local',
		triggerAction : 'all',
		forceSelection : true,
		editable : false,
		allowBlank : false,
		selectOnFocus : true,
		anchor : "100%",
		onTriggerClick : function(e) {
			var url = "../../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				notIn : "",
				rootNode : {
					id : deptId,
					text : Ext.get('operateDepCode').dom.value
				}
			}
			var o = window.showModalDialog(url, args,
					'dialogWidth=650px;dialogHeight=500px;status=no');
			if (typeof(o) == "object") {
				operateLeadBy.setValue(o.workerName);
				hidunitBox.setValue(o.workerCode);
			}
		}
	});
	var hidunitBox = new Ext.form.Hidden({
				id : 'hidunitBox',
				name : 'apply.operateLeadBy'

			})
	// 变更经办人 modify by drdu 20090317

	var operateBy = {
		fieldLabel : '变更经办人',
		name : 'operateBy',
		xtype : 'combo',
		id : 'operateBy',
		store : new Ext.data.SimpleStore({
					fields : ['id', 'name'],
					data : [[]]
				}),
		mode : 'remote',
		hiddenName : 'apply.operateBy',
		allowBlank : false,
		editable : false,
		anchor : "95%",
		triggerAction : 'all',
		onTriggerClick : function(e) {
			var url = "../../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				notIn : "",
				rootNode : {
					id : '-1',
					text : '灞桥电厂'
				}
			}
			var o = window.showModalDialog(url, args,
					'dialogWidth=650px;dialogHeight=500px;status=no');
			if (typeof(o) == "object") {
				Ext.get('operateBy').dom.value = o.workerName;
				Ext.get('operateDepCode').dom.value = o.deptName;
				operateLeadBy.setValue("");
				operateBy = o.workerCode;
				operateDepCode = o.deptCode;
				deptId = o.deptId;
			}
		}
	};

	// 申请部门
	var operateDepCode = {
		fieldLabel : '申请部门',
		name : 'operateDepCode',
		xtype : 'combo',
		id : 'operateDepCode',
		store : new Ext.data.SimpleStore({
					fields : ['id', 'name'],
					data : [[]]
				}),
		mode : 'remote',
		hiddenName : 'apply.operateDepCode',
		allowBlank : false,
		editable : false,
		anchor : "95%",
		triggerAction : 'all',
		onTriggerClick : function() {
		}
	};
	// 币别
	var currencyType = new Ext.form.ComboBox({
				fieldLabel : '币别',
				store : currencyTypeStore,
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
				anchor : '95%'
			});
	currencyType.on('beforequery', function() {
				return false
			});
	var actAmount = {
		id : "actAmount",
		xtype : "numberfield",
		fieldLabel : '原合同金额',
		allowBlank : false,
		readOnly : true,
		name : 'apply.actAmount',
		anchor : "95%"
	}
	var modiyActAmount = new Ext.form.NumberField({
				id : "modiyActAmount",
				xtype : "numberfield",
				fieldLabel : '现合同金额',
				name : 'apply.modiyActAmount',
				allowBlank : false,
				anchor : "100%"
			})
	// 合同开始时间 modify by drdu 090317

	var startDate = new Ext.form.TextField({
				id : 'startDate',
				fieldLabel : "合同履行开始时间",
				name : 'apply.startDate',
				style : 'cursor:pointer',
				readOnly : true,
				anchor : "90%"
			});
	// 合同结束时间
	var endDate = new Ext.form.TextField({
				id : 'endDate',
				fieldLabel : "合同履行结束时间",
				name : 'apply.endDate',
				style : 'cursor:pointer',
				readOnly : true,
				anchor : "90%"
			});
	var conomodifyName = {
		id : "conomodifyName",
		xtype : "textarea",
		fieldLabel : '变更原因',
		name : 'apply.conomodifyName',
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

	// modify by drdu 090317
	var entryDate = new Ext.form.TextField({
				id : 'entryDate',
				fieldLabel : "起草日期",
				name : 'apply.entryDate',
				style : 'cursor:pointer',
				readOnly : true,
				anchor : "90%",
				value : getDate()

			})
	var oriFileName = {
		id : "oriFileName",
		xtype : "fileuploadfield",
		fieldLabel : '合同变更文本',
		name : 'conFile',
		// inputType : "file",
		// name : 'apply.oriFileName',
		height : 27,
		anchor : "100%",
		buttonCfg : {
			text : '浏览...',
			iconCls : 'upload-icon'
		}

	}

	var btnView = new Ext.Button({
				id : 'btnView',
				text : '查看',
				handler : function() {
					if (id == "") {
						Ext.Msg.alert('提示', '请选择合同');
						return false;
					}
					window
							.open("/power/managecontract/showConFile.action?conid="
									+ id + "&type=MCON");
				}
			});

	var myform = new Ext.FormPanel({
				frame : true,
				fileUpload : true,
				labelAlign : 'left',
				bodyStyle : 'padding:5px',
				items : [{
					xtype : 'fieldset',
					title : '基本信息',
					// width : 800,
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
						"margin-right" : Ext.isIE6 ? (Ext.isStrict
								? "-10px"
								: "-13px") : "0"
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
											items : [operateBy, currencyType]
										}, {
											columnWidth : 0.35,
											layout : 'form',
											items : [operateDepCode, actAmount]
										}, {
											columnWidth : 0.3,
											layout : 'form',
											items : [operateLeadBy, hidunitBox,
													modiyActAmount]
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
											columnWidth : 0.7,
											layout : 'form',
											items : [oriFileName]
										}, {
											columnWidth : 0.3,
											layout : 'form',
											items : [btnView]
										}]
							}]

				}]
			});
	// ----------------------------------------------------------------
	// --------附件维护窗口--------------

	var wd = 300;

	var conDocId = {
		id : "conDocId",
		xtype : "hidden",
		fieldLabel : '附件ID',
		width : wd,
		readOnly : true,
		name : 'conDoc.conDocId'

	}
	// MCONATT 合同附件, MCONEVI 合同凭据
	var docType = {
		id : "docType",
		xtype : "hidden",
		fieldLabel : '文件类型',
		width : wd,
		readOnly : true,
		name : 'conDoc.docType'
	}

	var docFile = {
		id : "docFile",
		xtype : "fileuploadfield",
		// inputType : "file",
		fieldLabel : '选择附件',
		width : wd,
		name : 'docFile',
		height : 27,
		anchor : "82%",
		buttonCfg : {
			text : '浏览...',
			iconCls : 'upload-icon'
		}
	}

	var docName = {
		id : "docName",
		xtype : "textfield",
		fieldLabel : '附件名称',
		allowBlank : false,
		name : 'conDoc.docName',
		width : wd
	}

	var docMemo = {
		id : "docMemo",
		xtype : "textarea",
		fieldLabel : '备注',
		width : wd,
		name : 'conDoc.docMemo'

	}

	var lastModifiedDate = {
		id : "lastModifiedDate",
		xtype : "datefield",
		fieldLabel : '上传时间',
		readOnly : true,
		name : 'conDoc.lastModifiedDate',
		width : wd,
		format : 'Y-m-d h:i:s',
		value : getDate()
	}
	var lastModifiedName = {
		id : "lastModifiedName",
		xtype : "textfield",
		fieldLabel : '上传人',
		width : wd,
		readOnly : true,
		name : 'conDoc.lastModifiedName'
	}

	var myaddpanel = new Ext.FormPanel({
				frame : true,
				fileUpload : true,
				labelAlign : 'center',
				labelWidth : 80,
				title : '附件维护',
				items : [conDocId, docType, docFile, docName, docMemo,
						lastModifiedDate, lastModifiedName]

			});

	var win = new Ext.Window({
				width : 500,
				height : 300,
				buttonAlign : "center",
				items : [myaddpanel],
				layout : 'fit',
				closeAction : 'hide',
				draggable : true,
				modal : true,
				buttons : [{
					text : '保存',
					handler : function() {
						var myurl = "";
						if (Ext.get("conDocId").dom.value == "") {
							if (Ext.get("docFile").dom.value == "") {
								Ext.msg.alert("提示","请选择附件");
								return;
							}
							myurl = "managecontract/addDocModifyInfo.action";
							// +
							// "?modifyId="+ id
							// + "&fileName=" + Ext.get("docFile").dom.value;
						} else {
							myurl = "managecontract/updateDocModifyInfo.action";
							// +
							// "?modifyId="+ id
							// + "&fileName=" + Ext.get("docFile").dom.value;
						}
						myaddpanel.getForm().submit({
							method : 'POST',
							url : myurl,
							params : {
								fileName : Ext.get("docFile").dom.value,
								modifyId : id
							},
							success : function(form, action) {
								var o = eval("(" + action.response.responseText
										+ ")");
								Ext.Msg.alert("注意", o.msg);
								if (Ext.get("docType").dom.value == "MCONATT") {
									// modify by ywliu 09/04/24
									storeAnnex.load({
												params : {
													modifyId : id,
													docType : 'MCONATT',
													start : 0,
													limit : 18
												}
											});
								}
								if (Ext.get("docType").dom.value == "MCONEVI") {
									// modify by ywliu 09/04/24
									storeProof.load({
												params : {
													modifyId : id,
													docType : 'MCONEVI',
													start : 0,
													limit : 18
												}
											});
								}

								win.hide();
							},
							faliue : function() {
								Ext.Msg.alert('错误', '出现未知错误.');
							}
						});
					}
				}, {
					text : '取消',
					handler : function() {
						win.hide();
					}
				}]

			});

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

	var dataProxyAnnex = new Ext.data.HttpProxy(

	{
				// modifyBy ywliu 09/04/24
				url : 'managecontract/findDocModifyList.action'
			}

	);

	var theReaderAnnex = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"

			}, MyRecordAnnex);

	var storeAnnex = new Ext.data.Store({

				proxy : dataProxyAnnex,

				reader : theReaderAnnex

			});
	// 分页
	if (check == "update") {
		// modifyBy ywliu 09/04/24
		storeAnnex.load({
					params : {
						modifyId : id,
						docType : 'MCONATT',
						start : 0,
						limit : 18
					}
				});
	}

	var smAnnex = new Ext.grid.CheckboxSelectionModel();

	var gridAnnex = new Ext.grid.GridPanel({
		store : storeAnnex,
		height : 200,
		autoScroll : true,
		columns : [smAnnex, {

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
							align : 'center'
						}), {
					header : "名称",
					width : 160,
					sortable : true,
					dataIndex : 'docName',
					align : 'center'
				}, {
					header : "备注",
					width : 100,
					sortable : true,
					dataIndex : 'docMemo',
					align : 'center'
				}, {
					header : "原始文件名",
					width : 100,
					sortable : true,
					dataIndex : 'oriFileName',
					align : 'center',
					hidden : true
				}, {
					header : "原始文件",
					width : 100,
					sortable : true,
					dataIndex : 'oriFile',
					align : 'center'
				}, {
					header : "上传日期",
					width : 130,
					sortable : true,
					dataIndex : 'lastModifiedDate',
					align : 'center'
				}, {
					header : "上传人",
					width : 90,
					sortable : true,
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
									+ "&type=MCONATT');\"/>查看附件</a>"
						} else {
							return "";
						}
					}
				}],
		sm : smAnnex,
		tbar : ['合同附件  ', {
					text : "新增",
					iconCls : 'add',
					handler : addAnnex,
					id : 'annexAdd'
				}, {
					text : "修改",
					iconCls : 'update',
					handler : updateAnnex,
					id : 'annexUpdate'
				}, {
					text : "删除",
					iconCls : 'delete',
					handler : deleteAnnex,
					id : 'annexDelete'
				}]
	});

	function addAnnex() {
		myaddpanel.getForm().reset();
		win.setPosition(200, 100);
		win.show();
		// Ext.get("docFile").dom.select();
		// document.selection.clear();
		Ext.get("docType").dom.value = "MCONATT";
		Ext.get("lastModifiedName").dom.value = sessWorname;
		myaddpanel.setTitle("增加合同附件");
	}
	function updateAnnex() {
		if (gridAnnex.selModel.hasSelection()) {

			var records = gridAnnex.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				var record = gridAnnex.getSelectionModel().getSelected();
				win.setPosition(200, 100);
				win.show();
				// Ext.get("docFile").dom.select();
				// document.selection.clear();
				myaddpanel.getForm().loadRecord(record);
				Ext.get("docType").dom.value = "MCONATT";
				myaddpanel.setTitle("修改合同附件");
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}
	function deleteAnnex() {
		var sm = gridAnnex.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		var names = [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {

			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.conDocId) {
					ids.push(member.conDocId);
					names.push(member.docName);
				} else {

					storeAnnex.remove(storeAnnex.getAt(i));
				}
			}

			Ext.Msg.confirm("删除", "是否确定删除名称为'" + names + "'的记录？", function(
							buttonobj) {

						if (buttonobj == "yes") {

							Ext.lib.Ajax
									.request(
											'POST',
											'managecontract/deleteDocModifyInfo.action',
											{
												success : function(action) {
													Ext.Msg
															.alert("提示",
																	"删除成功！")
													// modifyBy ywliu 09/04/24
													// storeAnnex
													storeAnnex.load({
														params : {
															modifyId : id,
															docType : 'MCONATT',
															start : 0,
															limit : 18
														}
													});
												},
												failure : function() {
													Ext.Msg.alert('错误',
															'删除时出现未知错误.');
												}
											}, 'ids=' + ids);
						}
					});
		}
	}
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
				// modifyBy ywliu 09/04/24
				url : 'managecontract/findDocModifyList.action'
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
	if (check == "update") {
		// modifyBy ywliu 09/04/24
		storeProof.load({
					params : {
						modifyId : id,
						docType : 'MCONEVI',
						start : 0,
						limit : 18
					}
				});
	}
	var smProof = new Ext.grid.CheckboxSelectionModel();

	var gridProof = new Ext.grid.GridPanel({
		store : storeProof,
		height : 200,
		autoScroll : true,
		// autoHeight:true,
		columns : [smProof, {

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
							width : 50,
							align : 'center'
						}), {
					header : "名称",
					width : 160,
					sortable : true,
					dataIndex : 'docName',
					align : 'center'
				}, {
					header : "备注",
					width : 100,
					sortable : true,
					dataIndex : 'docMemo',
					align : 'center'
				}, {
					header : "原始文件名",
					width : 100,
					sortable : true,
					dataIndex : 'oriFileName',
					align : 'center',
					hidden : true
				}, {
					header : "原始文件",
					width : 100,
					sortable : true,
					dataIndex : 'oriFile',
					align : 'center'
				}, {
					header : "上传日期",
					width : 130,
					sortable : true,
					dataIndex : 'lastModifiedDate',
					align : 'center'
				}, {
					header : "上传人",
					width : 90,
					sortable : true,
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
									+ "&type=MCONEVI');\"/>查看附件</a>"
						} else {
							return "";
						}
					}

				}],
		sm : smProof,
		tbar : ['合同凭据  ', {
					text : "新增",
					iconCls : 'add',
					handler : addProof,
					id : 'proofAdd'

				}, {
					text : "修改",
					iconCls : 'update',
					handler : updateProof,
					id : 'proofUpdate'
				}, {
					text : "删除",
					iconCls : 'delete',
					handler : deleteProof,
					id : 'proofDelete'
				}]
	});

	function addProof() {
		myaddpanel.getForm().reset();
		win.setPosition(200, 100);
		win.show();
		// Ext.get("docFile").dom.select();
		// document.selection.clear();
		Ext.get("docType").dom.value = "MCONEVI";
		Ext.get("lastModifiedName").dom.value = sessWorname;
		myaddpanel.setTitle("增加合同凭据");
	}
	function updateProof() {
		if (gridProof.selModel.hasSelection()) {

			var records = gridProof.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				var record = gridProof.getSelectionModel().getSelected();
				win.setPosition(200, 100);
				win.show();
				// Ext.get("docFile").dom.select();
				// document.selection.clear();
				myaddpanel.getForm().loadRecord(record);
				Ext.get("docType").dom.value = "MCONEVI";
				myaddpanel.setTitle("修改合同凭据");
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}
	function deleteProof() {
		var sm = gridProof.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		var names = [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {

			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.conDocId) {
					ids.push(member.conDocId);
					names.push(member.docName);
				} else {

					storeProof.remove(storeProof.getAt(i));
				}
			}

			Ext.Msg.confirm("删除", "是否确定删除名称为'" + names + "'的记录？", function(
							buttonobj) {

						if (buttonobj == "yes") {

							Ext.lib.Ajax
									.request(
											'POST',
											'managecontract/deleteDocModifyInfo.action',
											{
												success : function(action) {
													Ext.Msg
															.alert("提示",
																	"删除成功！")
													// modifyBy ywliu 09/04/24
													storeProof.load({
														params : {
															modifyId : id,
															docType : 'MCONEVI',
															start : 0,
															limit : 18
														}
													});
												},
												failure : function() {
													Ext.Msg.alert('错误',
															'删除时出现未知错误.');
												}
											}, 'ids=' + ids);
						}
					});
		}
	}
	function getworkCode() {
	Ext.Ajax.request({
				url : 'managecontract/getSessionInfo.action',
				params : {},
				method : 'post',
				waitMsg : '正在加载数据...',
				success : function(result, request) {
					var responseArray = Ext.util.JSON
							.decode(result.responseText);
					if (responseArray.success == true) {
						var tt = eval('(' + result.responseText + ')');
						o = tt.data;
						sessWorname = o[1];
						sessWorcode = o[0];
						sessDeptCode = o[2];
						sessDeptName = o[3];
						initBase();
					} else {
						Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
					}
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
				}
			});}
	// --------------------------------
	function initBase() {
		myform.getForm().reset();
		// addBy ywliu 09/04/24
		entryBy = sessWorcode;
		operateBy = sessWorcode;
		operateDepCode = sessDeptCode;
		Ext.get('entryBy').dom.value = sessWorname;
		// addBy ywliu 09/04/24
		if (workflowStatus == 1) {

			Ext.get('btnSave').dom.disabled = true;
			Ext.get('btnDelete').dom.disabled = true;
			Ext.get('btnReport').dom.disabled = true;
		} else {
			Ext.get('btnSave').dom.disabled = false;
			Ext.get('btnDelete').dom.disabled = false;
			Ext.get('btnReport').dom.disabled = false;
		}
		btnSet(false);
	}
	// addBy ywliu 09/04/24
	function newBase() {
		check = "add";
		workflowStatus = "0"
		id = "";
		conId = "";
		initBase();
		storeAnnex.load({
					params : {
						modifyId : '-1',
						docType : 'MCONATT',
						start : 0,
						limit : 18
					}
				});
		storeProof.load({
					params : {
						modifyId : '-1',
						docType : 'MCONEVI',
						start : 0,
						limit : 18
					}
				});
	}
	function saveBase() {
		var url = "";
		if (check == "add") {
			url = "managecontract/addModifyBase.action?conId=" + conId;
		} else {
			url = "managecontract/updateModifyBase.action?modifyId=" + id
		}
		myform.getForm().submit({
			method : 'POST',
			url : url,
			params : {
				filePath : Ext.get("conFile").dom.value
			},
			success : function(form, action) {
				parent.iframe1.document.getElementById("btnQuery").click();
				var o = eval("(" + action.response.responseText + ")");
				Ext.get("conModifyNo").dom.value = o.conModifyNo;
				var currency = currencyType.getValue();
				var actMAmount = modiyActAmount.getValue();
				// if (check == "add") {
				var _url3 = "manage/projectFact/business/modifyMeetSign/register/tabPayPlan/payplan.jsp";
				parent.document.all.iframe3.src = _url3 + "?id=" + id
						+ "&conId=" + conId + "&totalMoney=" + actMAmount
						+ "&currencyType=" + currency;
				// url =
				// "manage/projectFact/business/modifyMeetSign/register/tabPayPlan/payplan.jsp";
				// parent.document.all.iframe3.src = url + "?id=" + o.id;
				btnSet(true);
				check = "update";
				id = o.id;
				Ext.Msg.alert("注意", o.msg);
				// }

			},
			faliue : function() {
				Ext.Msg.alert('错误', '出现未知错误.');
			}
		});
	}

	function deleteBase() {
		if (check == "update") {
			Ext.Msg.confirm("删除", "是否确定删除变更号为'"
							+ Ext.get("conModifyNo").dom.value + "'的记录？",
					function(buttonobj) {

						if (buttonobj == "yes") {

							Ext.lib.Ajax.request('POST',
									'managecontract/deleteModifyBase.action', {
										success : function(action) {
											parent.iframe1.document
													.getElementById("btnQuery")
													.click();
											Ext.Msg.alert("提示", "删除成功！")
											initBase();
											url = "manage/projectFact/business/modifyMeetSign/register/tabList/list.jsp";
											parent.document.all.iframe3.src = url;
										},
										failure : function() {
											Ext.Msg.alert('错误', '删除时出现未知错误.');
										}
									}, 'id=' + id);
						}
					});
		} else {
			Ext.Msg.alert('提示', '请选择要删除的记录.');
		}

	}

	function conModifyReport() {
		if (id == "") {
			Ext.MessageBox.alert('提示', '请选择需上报的记录!');
			return false;
		}
				var url = 'reportsign.jsp'
				var args = new Object();
				args.entryId = entryId;
				args.conModId = id;
				args.workflowType = "prjConModify";
				var o = window.showModalDialog(url, args,
						'status:no;dialogWidth=800px;dialogHeight=550px');
				if (o) {
					parent.iframe1.document.getElementById("btnQuery").click();
//					Ext.get('btnSave').dom.disabled = true;
//					Ext.get('btnDelete').dom.disabled = true;
//					Ext.get('btnReport').dom.disabled = true;
					myform.getForm().reset();
					getworkCode();
					Ext.get('entryBy').dom.value = sessWorname;
					var _url3 = "manage/projectFact/business/modifyMeetSign/register/tabPayPlan/payplan.jsp";
					parent.document.all.iframe3.src = _url3;
					id = "";
					entryId = "";
					storeAnnex.load();
					storeProof.load();
					btnSet(false);
					parent.Ext.getCmp("maintab").setActiveTab(0);
					var _url1 = "manage/projectFact/business/modifyMeetSign/register/tabList/list.jsp";
					parent.document.all.iframe1.src = _url1;
				}
				// Ext.MessageBox.alert('提示', '上报成功!');
				// },
				// failure : function(result, request) {
				// Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
				// }
				// });
	}
	// 会签票面浏览
	function CheckRptPreview() {
		if (id == null || id == "") {
			Ext.Msg.alert('提示', '请选择一个合同！');
		} else {
			var url = "/powerrpt/report/webfile/bqmis/GCContractModifyMeetSign.jsp?modifyId="
					+ id;
			window.open(url);
		}
	};
	// 布局
	var myPanel = new Ext.Panel({
		// region : "center",
		// layout : 'form',
		autoScroll : true,
		containerScroll : true,
		items : [myform, gridAnnex, gridProof],
		tbar : [{
					id : 'btnAdd',
					text : '增加',
					iconCls : 'add',
					handler : newBase
				}, '-', {
					id : 'btnDelete',
					text : '删除',
					iconCls : 'delete',
					handler : deleteBase
				}, '-', {
					id : 'btnSave',
					text : '保存',
					iconCls : 'save',
					handler : saveBase
				}, '-', {
					id : 'btnReport',
					text : '上报',
					iconCls : 'upcommit',
					handler : conModifyReport
				}, '-', {
					id : 'btnMeet',
					text : "会签表",
					iconCls : 'pdfview',
					handler : function() {
						CheckRptPreview();
					}
				}, '-', {
					text : '会签查询',
					iconCls : 'view',
					handler : function() {
						if (id == null || id == "") {
							Ext.Msg.alert('提示', '请选择一个合同！');
							return false;
						}
						if (entryId == null || entryId == "") {
							url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
									+ "prjConModify";
							window.open(url);
						} else {
							var url = "/power/workflow/manager/show/show.jsp?entryId="
									+ entryId;
							window.open(url);
						}
					}
				}, '-', {
					text : '合同信息',
					iconCls : 'list',
					handler : function() {
						if (id == null || id == "") {
							Ext.Msg.alert('提示', '请选择一个合同！');
							return false;
						}
						if (conId != null || conId != "") {
							url = "../../../../../../manage/projectFact/business/conBaseInfo/conBaseInfo.jsp?id="
									+ conId;

							var o = window
									.showModalDialog(url, '',
											'dialogWidth=800px;dialogHeight=800px;status=no');
						} else {
							Ext.Msg.alert("提示", "请在合同变更列表中选择一条记录");
						}
					}
				}]

	});

	new Ext.Viewport({
				// enableTabScroll : true,
				// autoScroll : true,
				collapsible : true,
				layout : "fit",
				items : [myPanel]
			});

	if (check == "update") {
		myform.getForm().load({
			url : 'managecontract/findModifyBaseInfo.action?modifyId=' + id,
			success : function(form, action) {
				var o = eval("(" + action.response.responseText + ")");
				deptId = o.data.deptId;
				operateLeadBy.setValue(o.data.operateleadName);
				hidunitBox.setValue(o.data.operateLeadBy)
				entryId = o.data.workFlowNo;
				if (o.data.operateBy != null) {
					Ext.getCmp('operateBy').setValue(o.data.operateBy);
					Ext.form.ComboBox.superclass.setValue.call(Ext
									.getCmp('operateBy'), o.data.operateName);

				}
				if (o.data.operateDepCode != null) {
					Ext.getCmp('operateDepCode')
							.setValue(o.data.operateDepCode);
					Ext.form.ComboBox.superclass.setValue.call(Ext
									.getCmp('operateDepCode'),
							o.data.operateDeptName);
				}
				if (o.data.operateLeadBy != null) {
					Ext.getCmp('operateLeadBy').setValue(o.data.operateLeadBy);
					Ext.form.ComboBox.superclass.setValue.call(Ext
									.getCmp('operateLeadBy'),
							o.data.operateLeadName);
				}
				if (o.data.entryName != null) {
					Ext.get("entryBy").dom.value = o.data.entryName;
				}
				if (o.data.startDate != null) {
					Ext.get("startDate").dom.value = o.data.startDate;

				}
				if (o.data.endDate != null) {
					Ext.get("endDate").dom.value = o.data.endDate;
				}
				// alert(o.data.filePath)
				Ext.get("oriFileName").dom.value = (o.data.filePath == null
						? ""
						: o.data.filePath);
				// alert(o.data.conomodifyName);
				if (o.data.filePath == null || o.data.filePath == "") {
					Ext.getCmp("btnView").setVisible(false);
				}
				if (o.data.workflowStatus == 0) {
					btnSet(true);
				}

			}
		});
	}
	// 合同附件及合同凭据增加时不能修改
	if (check == "add") {
		btnSet(false);
	}

	function btnSet(status) {
		gridAnnex.getTopToolbar().items.get('annexAdd').setVisible(status);
		gridAnnex.getTopToolbar().items.get('annexUpdate').setVisible(status);
		gridAnnex.getTopToolbar().items.get('annexDelete').setVisible(status);
		gridProof.getTopToolbar().items.get('proofAdd').setVisible(status);
		gridProof.getTopToolbar().items.get('proofUpdate').setVisible(status);
		gridProof.getTopToolbar().items.get('proofDelete').setVisible(status);
	}
getworkCode()
		// 判断工作流以设置按钮，修改by 何学良 09/04/08

});