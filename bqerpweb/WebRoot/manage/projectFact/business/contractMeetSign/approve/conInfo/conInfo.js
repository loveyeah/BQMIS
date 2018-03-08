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
	var method = "add";
	var docmethod;
	var sessWorname;
	var sessWorcode;
	var sessDeptCode;
	var sessDeptName;
	var operateBy;
	var entryBy;
	var operateDepCode;
	var filetype;
	var entryId;
	var deptId = "";
	var id = getParameter("id");

	var jhflg = getParameter("jhflg");
	// 承办部门code
	var flagDeptCode;
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号
							sessWorcode = result.workerCode;
							sessWorname = result.workerName;
							sessDeptCode = result.deptCode;
							deptId = result.deptId;
							sessDeptName = result.deptName;
						}
						typeBox.setValue(2);
					}
				});
	}
	// 币别 modify by drdu 2009/05/05
	var typeStore = new Ext.data.JsonStore({
				url : 'managecontract/getConCurrencyList.action',
				root : 'list',
				fields : ['currencyId', 'currencyName']
			})
	typeStore.load();
	// var revoke = getParameter("revoke");
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var issum = 'Y';
	var issign = 'Y';
	var isinstant = "N";
	var conttreesNo = new Ext.form.TextField({
				id : 'conttreesNo',
				// name : 'con.conttreesNo',
				xtype : 'textfield',
				fieldLabel : '合同编号',
				value : '自动生成',
				allowBlank : true,
				anchor : '92.5%'
			})

	// 委托人 add by bjxu
	var prosy_by = new Ext.form.ComboBox({
				id : 'prosy_by',
				fieldLabel : '本项目委托代理人',
				readOnly : true,
				disabled : true,
				anchor : '85%'
			})
	prosy_by.on("beforequery", function() {
				return false;
			})

	// add by bjxu 20091012
	// 经办人联系电话
	var operateTel = new Ext.form.TextField({
				fieldLabel : '联系电话',
				id : 'operateTel',
				disabled : true,
				anchor : '85%'
			})

	// 合同类别 modify by drdu 20090423
	var contypeBox = new Ext.form.ComboBox({
				fieldLabel : '合同类别',
				store : [['2', '项目合同']],
				id : 'conType',
				name : 'conTypetext',
				valueField : "value",
				displayField : "text",
				value : '2',
				disabled : true,
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				emptyText : '请选择',
				anchor : '85%'
			});
	contypeBox.on('beforequery', function() {
				return false
			});
	// 项目类别
	var prjTypeName = new Ext.ux.ComboBoxTree({
				fieldLabel : "项目类别<font color='red'>*</font>",
				// anchor : '100%',
				displayField : 'text',
				valueField : 'id',
				id : 'prjTypeName',
				allowBlank : false,
				disabled : true,
				// hiddenName : 'head.planOriginalId',
				// hiddenName : 'head.txtMrOriginal',
				blankText : '请选择',
				emptyText : '请选择',
				readOnly : true,
				anchor : "85%"
			});
	prjTypeName.on("beforequery", function(e) {
				return false;
			})
	// 承办部门
	var operateDeptName = new Ext.ux.ComboBoxTree({
				fieldLabel : '承办部门',
				id : 'operateDepName',
				displayField : 'text',
				valueField : 'id',
				// hiddenName : 'con.operateDeptId',
				blankText : '请选择',
				emptyText : '请选择',
				// allowBlank : false,
				disabled : true,
				anchor : "85%"
			})
	operateDeptName.on("beforequery", function(e) {
				return false;
			});
	// 合同年份
	var conyear = new Ext.form.TextField({
				// style : 'cursor:pointer',
				id : "conyear",
				readOnly : true,
				anchor : "85%",
				// value : Year,
				disabled : true,
				allowBlank : false,
				fieldLabel : '合同年份'
			});
	// 总金额
	var actAmount = new Ext.form.NumberField({
				id : 'actAmount',
				fieldLabel : '总金额',
				readOnly : false,
				disabled : true,
				// allowBlank : false,
				anchor : '85.5%'
			});
	// 有无总金额
	var isTotal = new Ext.form.Checkbox({
				id : 'isTotal',
				// name : 'con.isSum',
				fieldLabel : '有无总金额',
				checked : true,
				disabled : true,
				anchor : "20%"
			});

	// 要求会签
	var isMeet = new Ext.form.Checkbox({
				id : 'isMeet',
				fieldLabel : '要求会签',
				checked : true,
				hidden : true,
				// hideLabel : true,
				anchor : "20%"
			});
	// 要求紧急采购
	var isEmergency = new Ext.form.Checkbox({
				id : 'isEmergency',
				fieldLabel : '要求紧急采购',
				// hideLabel : true,
				anchor : "20%",
				checked : false
			});
	// 部门负责人
	var unitBox = new Ext.form.ComboBox({
				fieldLabel : '部门负责人',
				id : 'operateLeadBy',
				mode : 'local',
				triggerAction : 'all',
				forceSelection : true,
				disabled : true,
				editable : false,
				allowBlank : false,
				selectOnFocus : true,
				anchor : "85%"
			});
	var hidunitBox = new Ext.form.Hidden({
				id : 'hidunitBox'
			})
	// 合作伙伴
	var cliendBox = new Ext.form.ComboBox({
				name : 'cliendId',
				id : 'cliendId',
				fieldLabel : '合作伙伴',
				mode : 'remote',
				editable : false,
				allowBlank : false,
				disabled : true,
				emptyText : '请选择',
				anchor : '92.5%'
			});
	var hidcliendBox = new Ext.form.Hidden({
				id : "hidcliendBox"
			})
	// 乙方负责人
	secondcharge = new Ext.form.TextField({
				id : 'secondcharge',
				fieldLabel : '乙方负责人',
				readOnly : false,
				// allowBlank : false,
				disabled : true,
				anchor : "85%"
			})
	// 第三方
	var thirdBox = new Ext.form.ComboBox({
				id : 'thirdClientId',
				name : 'thirdClientId',
				fieldLabel : '第三方',
				mode : 'remote',
				disabled : true,
				editable : false,
				emptyText : '请选择',
				anchor : '92.5%'
			});
	var hidthirdBox = new Ext.form.Hidden({
				id : "hidthirdBox"
			})
	// 项目编号
	var projectNum = new Ext.form.ComboBox({
				name : 'projectNum',
				id : 'projectNum',
				fieldLabel : '项目编号',
				mode : 'remote',
				editable : false,
				disabled : true,
				// allowBlank : false,
				emptyText : '请选择',
				anchor : '85%'
			});
	var hdnprojectNum = new Ext.form.Hidden({
				id : 'hdnprojectNum'
			});
	// 项目名称
	var projectName = new Ext.form.TextField({
				id : "projectName",
				xtype : "textfield",
				fieldLabel : '项目名称',
				disabled : true,
				name : '',
				readOnly : true,
				anchor : "85%"
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
			// name : 'con.itemId'
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
				editable : false,
				disabled : true,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				emptyText : '请选择',
				anchor : '75.5%'
			});
			
	// 是否数据源
	var storeIfYN = [{
				ifSecrityId : 'Y',
				ifSecrityName : '是'
			}, {
				ifSecrityId : 'N',
				ifSecrityName : '否'
			}];

			
	// 是否已签订安全协议
	var ifSecrityId = new Ext.form.ComboBox({
				fieldLabel : '已签订安全协议',
				store : new Ext.data.JsonStore({
							fields : ['ifSecrityId', 'ifSecrityName'],
							data : storeIfYN
						}),

				id : 'ifSecrityId',
				valueField : "ifSecrityId",
				displayField : "ifSecrityName",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'con.ifSecrity',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				emptyText : '请选择',
				align : 'center',
				disabled : true,
				anchor : '85%'
			});			
			
	// 合同开始时间
	var startDate = new Ext.form.TextField({
				id : 'startDate',
				fieldLabel : "合同履行开始时间",
				style : 'cursor:pointer',
				disabled : true,
				anchor : "85%",
				allowBlank : false
			});
	// 合同结束时间
	var endDate = new Ext.form.TextField({
				id : 'endDate',
				fieldLabel : "合同履行结束时间",
				style : 'cursor:pointer',
				anchor : "85%",
				disabled : true,
				allowBlank : false
			});
	// 起草人
	var entryName = new Ext.form.TextField({
				id : 'entryName',
				xtype : 'textfield',
				fieldLabel : '起草人',
				disabled : true,
				readOnly : true,
				allowBlank : false,
				anchor : '85.5%'
			});
	// 起草时间
	var entryDate = new Ext.form.TextField({
				id : 'entryDate',
				fieldLabel : "起草日期",
				disabled : true,
				style : 'cursor:pointer',
				readOnly : true,
				anchor : "85%"
			});
	// 合同文本
	var oriFileName = {
		id : "conFile",
		xtype : "fileuploadfield",
		fieldLabel : '合同文本',
		// inputType : "file",
		name : 'conFile',
		height : 22,
		anchor : "95%",
		// width : 200,
		buttonCfg : {
			text : '浏览...',
			iconCls : 'upload-icon'
		},
				listeners : {
			'fileselected' : function() {
				var url = Ext.get('conFile').dom.value;
				var format = url.substring(url.lastIndexOf("\\") + 1,
						url.length);
				Ext.get('conFile').dom.value = format;
			}
		}
	}
	// 查看

	var btnView = new Ext.Button({
				id : 'btnView',
				text : '查看',
				disabled : true,
				handler : function() {
					if (id == "") {
						Ext.Msg.alert('提示', '请选择合同');
						return false;
					}
					window
							.open("/power/managecontract/showConFile.action?conid="
									+ id + "&type=CON");
				}
			});

	// 质保期限（月）
	var warrantyPeriod = new Ext.form.NumberField({
				id : 'warrantyPeriod',
				fieldLabel : '质保期限（月）',
				maxValue : 9999,
				minValue : 0,
				allowBlank : true,
				allowNegative : false,
				allowDecimals : false,
				anchor : '85%'
			})

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
				if (jhflg == "Y") {
					url = "../../../../../../manage/projectFact/business/contractMeetSign/approve/meetSign/JHsign.jsp?entryId="
							+ entryId + "&conid=" + id;
				} else {
					url = "../../../../../../manage/projectFact/business/contractMeetSign/approve/meetSign/sign.jsp?entryId="
							+ entryId + "&conid=" + id;
				}
				var o = window.showModalDialog(url, '',
						'dialogWidth=800px;dialogHeight=600px;status=no');
				if (o) {
					form.getForm().reset();
					id = "";
					entryId = "";
					parent.Ext.getCmp("maintab").setActiveTab(0);
					var url = "manage/projectFact/business/contractMeetSign/approve/arrpoveList/list.jsp";
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
				if (id == null || id == "") {
					Ext.Msg.alert('提示', '请选择一个合同！');
					return false;
				}
				if (entryId == null || entryId == "") {
					url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
							+ "bqProContract";
					window.open(url);
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
				form.getForm().submit({
					url : 'managecontract/addMeetConInfo.action',
					method : 'POST',
					params : {
						filePath : Ext.get("conFile").dom.value,
						id : id,
						conttreesNo : conttreesNo.getValue(),
						itemCode : itemCode,
						method : "JHupdate"
					},
					success : function(form, action) {
						parent.iframe1.document.getElementById("btnQuery")
								.click();
						var message = eval('(' + action.response.responseText
								+ ')');
						parent.iframe1.document.getElementById("btnQuery")
								.click();
						id = message.data.conId;
						Ext.get("conttreesNo").dom.value = message.data.conttreesNo;
						Ext.get('conId').dom.value = message.data.conId;
						method = "JHupdate";
						var currencyType = message.data.currencyType;
						var actAmount = message.data.actAmount;
						annextbar.setDisabled(false);
						Credenttbar.setDisabled(false);
						if (message.path != null && message.path != "null") {
							btnView.setDisabled(false);
						} else {
							btnView.setDisabled(true);
						}
						Ext.Msg.alert("成功", "数据保存成功!");
					},
					failure : function() {
						Ext.Msg.alert('提示', '操作失败，请联系管理员！');
					}
				})
			}
		}]
	});
	// 会签票面浏览
	function CheckRptPreview() {
		if (id == null || id == "") {
			Ext.Msg.alert('提示', '请选择一个合同！');
		} else {
			var url = "/powerrpt/report/webfile/bqmis/GCContractMeetSign.jsp?conId="
					+ id;
			window.open(url);
		}
	};

	var form = new Ext.form.FormPanel({
				bodyStyle : "padding:5px 5px 0",
				labelAlign : 'right',
				width : Ext.get('div_lay').getWidth(),
				autoHeight : true,
				fileUpload : true,
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
											items : [{
														id : 'contractName',
														xtype : 'textfield',
														fieldLabel : '合同名称',
														disabled : true,
														readOnly : false,
														allowBlank : false,
														anchor : '92.5%'
													}]
										}, /*
											 * { columnWidth : .5, layout :
											 * 'form', border : false,
											 * labelWidth : 110, items :
											 * [contypeBox] },
											 */{
											columnWidth : .5,
											layout : 'form',
											border : false,
											labelWidth : 110,
											items : [prjTypeName]
										}, {
											columnWidth : .5,
											layout : 'form',
											labelWidth : 110,
											border : false,
											items : [conyear]
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
											items : [cliendBox, hidcliendBox]
										}, {
											border : false,
											layout : 'form',
											columnWidth : .5,
											labelWidth : 110,
											hidden : true,
											items : [secondcharge]
										}, {
											border : false,
											layout : 'form',
											columnWidth : 1,
											labelWidth : 110,
											hidden : true,
											items : [thirdBox, hidthirdBox]
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
											labelWidth : 110,
											hidden : true,
											items : [projectName, hdnprojectNum]
										}, {
											border : false,
											layout : 'form',
											columnWidth : .5,
											labelWidth : 110,
											items : [itemBox, hiditemId,
													operateDeptName]
										}, {
											border : false,
											layout : 'form',
											columnWidth : .5,
											labelWidth : 110,
											items : [{
														id : 'operateName',
														xtype : 'trigger',
														fieldLabel : '承办人',
														readOnly : true,
														disabled : true,
														allowBlank : false,
														anchor : '85%'
													}

											// ,unitBox, hidunitBox
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
										}, {
											border : false,
											layout : 'form',
											columnWidth : 1,
											labelWidth : 110,
											items : [{
														id : 'conAbstract',
														xtype : 'textarea',
														fieldLabel : '合同简介',
														readOnly : false,
														disabled  : true,
														height : 120,
														anchor : '92.5%'
													}]
										}
										// , {
										// columnWidth : 1,
										// layout : 'form',
										// border : false,
										// labelWidth : 110,
										// items : [isMeet]
										// }
										, {
											columnWidth : .5,
											layout : 'form',
											border : false,
											labelWidth : 110,
											hidden : true,
											items : [isEmergency]
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
											columnWidth : 0.52,
											labelWidth : 110,
											items : [oriFileName]
										}, {
											border : false,
											layout : 'form',
											columnWidth : 0.2,
											labelWidth : 80,
											items : [btnView]
										}, {
											border : false,
											layout : 'form',
											columnWidth : 0.5,
											hidden : true,
											labelWidth : 110,
											items : [warrantyPeriod]
										}, {
											border : false,
											layout : 'form',
											columnWidth : 0.5,
											labelWidth : 110,
											items : [operateTel]
										},{
											border : false,
											layout : 'form',
											columnWidth : 0.5,
											labelWidth : 110,
											items : [prosy_by]
										}, {
											border : false,
											layout : 'form',
											columnWidth : 0.5,
											labelWidth : 110,
											items : [ifSecrityId]
										},{
											border : false,
											layout : 'form',
											columnWidth : 1,
											labelWidth : 110,
											items : [{
														id : 'conId',
														xtype : 'numberfield',
														fieldLabel : '合同id',
														readOnly : false,
														hidden : true,
														hideLabel : true,
														anchor : '92.5%'
													}]
										}]
							}]
						})]
			});
	var annextbar = new Ext.Toolbar({
		id : 'annextbar',
		items : ['合同附件：', {
					id : 'btnAnnexAdd',
					text : "增加",
					iconCls : 'add',
					handler : function() {
						if (id == "") {
							Ext.Msg.alert('提示', '请先从列表中选择合同！');
							return false;
						}
						docwin.setTitle("增加合同附件");
						docform.getForm().reset();
						docwin.show();
						// Ext.get("fjdocFile").dom.select();
						// document.selection.clear();
						Ext.get('docType').dom.value = "CONATT";
						Ext.get('keyId').dom.value = id;
						Ext.get('lastModifiedName').dom.value = sessWorname;
						docmethod = 'add';
					}
				}, '-', {
					id : 'btnAnnexSave',
					text : "修改",
					iconCls : 'update',
					handler : function() {
						docmethod = 'update';
						var selrows = annexGrid.getSelectionModel()
								.getSelections();
						if (selrows.length > 0) {
							var record = annexGrid.getSelectionModel()
									.getSelected();
							docform.getForm().reset();
							docwin.show(Ext.get('btnAnnexSave'));
							docform.getForm().loadRecord(record);
							docwin.setTitle("修改合同附件");
						} else {
							Ext.Msg.alert('提示', '请选择您要修改的合同附件！');
						}
					}
				}, '-', {
					id : 'btnAnnexDelete',
					text : "删除",
					iconCls : 'delete',
					handler : function() {
						var selrows = annexGrid.getSelectionModel()
								.getSelections();
						if (selrows.length > 0) {
							Ext.Msg.confirm('提示', '删除的数据您将不能恢复,确定要删除吗?',
									function(b) {
										if (b == "yes") {
											var record = annexGrid
													.getSelectionModel()
													.getSelected();
											Ext.Ajax.request({
												url : 'managecontract/deleteConDoc.action',
												params : {
													docid : record.data.conDocId
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
							Ext.Msg.alert('提示', '请选择您要删除的合同附件！');
						}
					}
				}]
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
				ds : annex_ds,
				cm : annex_item_cm,
				sm : annex_sm,
				// title : '合同附件',
				width : Ext.get('div_lay').getWidth(),
				split : true,
				// autoHeight:true,
				height : 150,
				autoScroll : true,
				// collapsible : true,
				tbar : annextbar,
				border : false
			});
	annexGrid.on('rowdblclick', function(grid, rowIndex, e) {
				var record = annexGrid.getSelectionModel().getSelected();
				docmethod = 'update';
				docform.getForm().reset();
				docwin.show(Ext.get('btnAnnexSave'));
				docform.getForm().loadRecord(record);
				docwin.setTitle("修改合同附件");

			});
	var Credenttbar = new Ext.Toolbar({
		hidden : true,
		items : ['合同凭据：', {
					id : 'btnCredentAdd',
					text : "增加",
					iconCls : 'add',
					handler : function() {
						if (id == "") {
							Ext.Msg.alert('提示', '请先从列表中选择合同!');
							return false;
						}
						docwin.setTitle("增加合同凭据");
						docform.getForm().reset();
						docwin.show();
						// Ext.get("fjdocFile").dom.select();
						// document.selection.clear();
						Ext.get('docType').dom.value = "CONEVI";
						Ext.get('keyId').dom.value = id;
						Ext.get('lastModifiedName').dom.value = sessWorname;
						docmethod = 'add';
					}
				}, '-', {
					id : 'btnCredentDelete',
					text : "删除",
					iconCls : 'delete',
					handler : function() {
						var selrows = CredentGrid.getSelectionModel()
								.getSelections();
						if (selrows.length > 0) {
							Ext.Msg.confirm('提示', '删除的数据您将不能恢复,确定要删除吗?',
									function(b) {
										if (b == "yes") {
											var record = CredentGrid
													.getSelectionModel()
													.getSelected();
											Ext.Ajax.request({
												url : 'managecontract/deleteConDoc.action',
												params : {
													docid : record.data.conDocId
												},
												method : 'post',
												waitMsg : '正在删除数据...',
												success : function(result,
														request) {
													Credent_ds.reload();
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
							Ext.Msg.alert('提示', '请选择您要删除的合同凭据！');
						}
					}
				}]
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
				title : "合同凭据",
				ds : Credent_ds,
				cm : Credent_item_cm,
				sm : Credent_sm,
				split : true,
				height : 150,
				autoScroll : true,
				width : Ext.get('div_lay').getWidth(),
				// title : '合同凭据',
				// collapsible : true,
				// tbar : Credenttbar,
				border : false
			});
	CredentGrid.on('rowdblclick', function(grid, rowIndex, e) {
				var record = CredentGrid.getSelectionModel().getSelected();
				docform.getForm().reset();
				docwin.show(Ext.get('btnCredentSave'));
				docform.getForm().loadRecord(record);
				docwin.setTitle("修改合同凭据");

			});
	var lastModifiedDate = new Ext.form.TextField({
		id : 'lastModifiedDate',
		fieldLabel : "上传时间",
		name : 'doc.lastModifiedDate',
		type : 'textfield',
		readOnly : true,
		style : 'cursor:pointer',
		anchor : "95%",
		value : getDate()
			// listeners : {
			// focus : function() {
			// var pkr = WdatePicker({
			// startDate : '%y-%M-%d 00:00:00',
			// dateFmt : 'yyyy-MM-dd HH:mm:ss',
			// alwaysUseStartDate : true
			// });
			// }
			// }
		});

	var fjdocFile = {
		id : "oriFile",
		xtype : "fileuploadfield",
		// inputType : "file",
		fieldLabel : '选择附件',
		// allowBlank : false,
		anchor : "95%",
		height : 22,
		name : 'fjdocFile',
		buttonCfg : {
			text : '浏览...',
			iconCls : 'upload-icon'
		},
			listeners : {
			'fileselected' : function() {
				var url = Ext.get('oriFile').dom.value;
				var format = url.substring(url.lastIndexOf("\\") + 1,
						url.length - 4);
				Ext.get('docName').dom.value = format;
			}
		}

	}
	var doccontent = new Ext.form.FieldSet({
				height : '100%',
				layout : 'form',
				items : [{
							id : 'conDocId',
							name : 'doc.conDocId',
							xtype : 'hidden',
							fieldLabel : '序号',
							readOnly : false,
							hidden : true,
							hideLabel : true,
							anchor : '95%'
						}, {
							id : 'keyId',
							name : 'doc.keyId',
							xtype : 'numberfield',
							fieldLabel : '序号',
							readOnly : false,
							hidden : true,
							hideLabel : true,
							anchor : '95%'
						}, fjdocFile, {
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
						}, lastModifiedDate, {
							id : 'lastModifiedName',
							xtype : 'textfield',
							fieldLabel : '上传人',
							readOnly : true,
							anchor : '95%'
						}, {
							id : 'docType',
							name : 'doc.docType',
							xtype : 'textfield',
							fieldLabel : '类型',
							readOnly : false,
							hidden : true,
							hideLabel : true,
							anchor : '95%'
						}]
			});

	var docform = new Ext.form.FormPanel({
				bodyStyle : "padding:5px 5px 0",
				labelAlign : 'right',
				id : 'shift-form',
				labelWidth : 80,
				autoHeight : true,
				fileUpload : true,
				region : 'center',
				border : false,
				items : [doccontent],
				buttons : [{
					text : '保存',
					iconCls : 'save',
					handler : function() {
						var url = '';
						if (docmethod == "add") {
							url = 'managecontract/addConDoc.action';

						} else {
							url = 'managecontract/updateConDoc.action';
						}
						if (!docform.getForm().isValid()) {
							return false;
						}
						docform.getForm().submit({
							url : url,
							method : 'post',
							params : {
								filePath : Ext.get('oriFile').dom.value
							},
							success : function(form, action) {
								var message = eval('('
										+ action.response.responseText + ')');
								Ext.Msg.alert("成功", message.data);
								if (Ext.get('docType').dom.value == "CONATT") {
									annex_ds.load({
												params : {
													conid : id,
													type : "CONATT"
												}
											});
								} else {
									Credent_ds.load({
												params : {
													conid : id,
													type : "CONEVI"
												}
											});
								}
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

	var docwin = new Ext.Window({
				title : '新增',
				el : 'win',
				modal : true,
				autoHeight : true,
				width : 450,
				closeAction : 'hide',
				items : [docform]
			});
	getWorkCode()
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
				if (o.data.workflowStatus == 2) {
					Ext.get('btnSign').dom.disabled = true
				}
				prosy_by.setValue(o.data.prosy_byName)
				method = "JHupdate";
				entryBy = o.data.entryBy;
				flagDeptCode = o.data.operateDepCode;
				entryId = o.data.workflowNo;
				hiditemId.setValue(o.data.itemId);
				// itemBox.setValue((o.data.itemId == "zzfy") ? "生产成本" :
				// "劳务成本");
				itemBox.setValue(o.data.itemName);
				
				if (o.data.ifSecrity == "Y"){
					ifSecrityId.setValue("是");
				} else if(o.data.ifSecrity == "N"){
				   ifSecrityId.setValue("否");
				} else 
				   ifSecrityId.setValue("");
				   
//				ifSecrityId.setValue((o.data.ifSecrity == "Y") ? "是":"否");
				
				var obj = new Object();
				obj.text = o.data.prjTypeName;
				prjTypeName.setValue(obj);
				if (o.data.operateDepName != null) {
					Ext.get("operateDepName").dom.value = o.data.operateDepName;
				}
				// prjtypeId.setValue(o.data.prjtypeId);
				// prjTypeName.setValue(o.data.prjTypeName);
				Ext.get("conFile").dom.value = (o.data.filePath == null
						? ""
						: o.data.filePath);
				if (o.data.filePath == null || o.data.filePath == "") {
					Ext.getCmp("btnView").setDisabled(true);
				} else {
					btnView.setDisabled(false);
				}
				Ext.get("secondcharge").dom.value = o.data.constructionName == null
						? ""
						: o.data.constructionName;
				if (o.data.projectId != null) {
					Ext.get("projectNum").dom.value = o.data.projectId;
					hdnprojectNum.setValue(o.data.projectId);
				} else {
					Ext.get("projectNum").dom.value = "";
					hdnprojectNum.setValue("");
				}
				cliendBox.setValue((o.data.cliendName == null)
						? ""
						: o.data.cliendName);
				hidcliendBox.setValue((o.data.cliendId == null)
						? ""
						: o.data.cliendId);
				hidunitBox.setValue((o.data.operateLeadBy == null)
						? ""
						: o.data.operateLeadBy);
				hidthirdBox.setValue((o.data.thirdClientId == null)
						? ""
						: o.data.thirdClientId);
				thirdBox.setValue((o.data.thirdClientName == null)
						? ""
						: o.data.thirdClientName);
				conyear
						.setValue((o.data.conYear == null)
								? ""
								: o.data.conYear);
				projectNum.setValue(o.data.prjShow);
				warrantyPeriod.setValue(o.data.warrantyPeriod);
				if (o.data.operateLeadBy != null) {
					Ext.getCmp('operateLeadBy').setValue(o.data.operateLeadBy);
					Ext.form.ComboBox.superclass.setValue.call(Ext
									.getCmp('operateLeadBy'),
							o.data.operateLeadName);
				}
				if (o.data.isSum == "Y") {
					Ext.get('isTotal').dom.checked = true;
					isTotal.checked = true;
				} else {
					Ext.get('isTotal').dom.checked = false;
					isTotal.checked = false;
					actAmount.setDisabled(true);
				}
				// if (o.data.isSign == "Y") {
				// Ext.get('isMeet').dom.checked = true;
				// } else {
				// Ext.get('isMeet').dom.checked = false;
				// }
				if (o.data.isInstant == "Y") {
					Ext.get('isEmergency').dom.checked = true;
				} else {
					Ext.get('isEmergency').dom.checked = false;
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
				if (jhflg != "" && jhflg != "null") {
					Ext.get('btnSave').dom.disabled = false;
				} else {
					Ext.get('btnSave').dom.disabled = true;
				}
			},
			failure : function(result, request) {
				Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
			}
		});
		method = "JHupdate";
	} else {
		parent.Ext.getCmp("maintab").setActiveTab(2);
		parent.Ext.getCmp("maintab").setActiveTab(1);
	}

	// ---add by bjxu
	if (jhflg == "Y") {
		itemBox.onTriggerClick = itemClick;
		itemBox.setDisabled(false);
		conttreesNo.setDisabled(false);
		annextbar.setDisabled(false);
		// Ext.get('btnSave').dom.disabled = false;
	} else {
		itemBox.onTriggerClick = exit;
		conttreesNo.setDisabled(true);
		itemBox.setDisabled(true);
		annextbar.setDisabled(true);
		// Ext.get('btnSave').dom.disabled = true;
	}
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