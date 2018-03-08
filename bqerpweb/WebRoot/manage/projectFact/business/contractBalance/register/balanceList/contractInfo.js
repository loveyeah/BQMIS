Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var contractId = getParameter("conId");
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
	// 币别 modify by zhangbaisong 2009/05/27
	var typeStore = new Ext.data.JsonStore({
				url : 'managecontract/getConCurrencyList.action',
				root : 'list',
				fields : ['currencyId', 'currencyName']
			})
	typeStore.load();
	
	// 合同类别
	var contypeBox = new Ext.ux.ComboBoxTree({
		fieldLabel : '合同类别',
		id : 'conType',
		name : 'conTypetext',
		displayField : 'text',
		valueField : 'id',
		hiddenName : 'con.conTypeId',
		readOnly : true,
		allowBlank : false,
		anchor : "85%",
		blankText : '请选择',
		emptyText : '请选择',
		tree : {
			xtype : 'treepanel',
			loader : new Ext.tree.TreeLoader({
				dataUrl : 'managecontract/findByPId.action'
			}),
			root : new Ext.tree.AsyncTreeNode({
				id : '0',
				name : '合同类别',
				text : '合同类别'
			})
		}
//		selectNodeModel : 'all'
	});

	// 总金额
	var actAmount = new Ext.form.NumberField({
		id : 'actAmount',
		name : 'con.actAmount',
		fieldLabel : '总金额',
		readOnly : true,
		allowBlank : false,
		anchor : '85.5%'
	});
	// 有无总金额
	var isTotal = new Ext.form.Checkbox({
		id : 'isTotal',
		// name : 'con.isSum',
		fieldLabel : '有无总金额',
		checked : true,
		anchor : "20%"
//		listeners : {
//			check : isTotalCheck
//		}
	});
	function isTotalCheck() {
		if (isTotal.checked) {
			actAmount.setDisabled(false);
			issum = "Y";
		} else {
			actAmount.setDisabled(true);
			issum = "N";
		}
	}
	// 要求会签
	var isMeet = new Ext.form.Checkbox({
		id : 'isMeet',
		fieldLabel : '要求会签',
		checked : true,
		// hideLabel : true,
		anchor : "20%",
		listeners : {
			check : isMeetCheck
		}
	});
	function isMeetCheck() {
		if (isMeet.checked) {
			issign = "Y";
		} else {
			issign = "N";
		}
	}
	// 要求紧急采购
	var isEmergency = new Ext.form.Checkbox({
		id : 'isEmergency',
		fieldLabel : '要求紧急采购',
		// hideLabel : true,
		anchor : "20%",
		checked : false,
		listeners : {
			check : isEmergencyCheck
		}
	});
	function isEmergencyCheck() {
		if (isEmergency.checked) {
			isinstant = "Y";
		} else {
			isinstant = "N";
		}
	};
	// 部门负责人

	var unitStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managecontract/getEmpByDept.action'
		}),
		reader : new Ext.data.JsonReader({
			id : "unit"
		}, [{
			name : 'empCode'
		}, {
			name : 'chsName'
		}])
	});

	var unitBox = new Ext.form.ComboBox({
		fieldLabel : '部门负责人',
		id : 'operateLeadBy',
		name : 'con.operateLeadBy',
		store : unitStore,
		valueField : "empCode",
		displayField : "chsName",
		mode : 'local',
//		triggerAction : 'all',
		forceSelection : true,
		hiddenName : 'con.operateLeadBy',
		editable : false,
		allowBlank : false,
		selectOnFocus : true,
		anchor : "85%"
	});
//	// 燃料供应商
//	var cliendBox = new Ext.form.ComboBox({
//		fieldLabel : '供应商',
//		store : [['1', '燃料供应商'], ['2', '设备供应商']],
//		id : 'cliendId',
//		name : 'cliendId',
//		valueField : "value",
//		displayField : "text",
//		mode : 'local',
//		typeAhead : true,
//		forceSelection : true,
//		hiddenName : 'con.cliendId',
//		editable : false,
////		triggerAction : 'all',
//		selectOnFocus : true,
//		allowBlank : false,
//		emptyText : '请选择',
//		anchor : '92.5%'
//	});
	
	// 燃料供应商
	var cliendBox = new Ext.form.ComboBox({
				fieldLabel : '合作伙伴',
				// store : [['1', '燃料供应商'], ['2', '设备供应商']],
				id : 'cliendId',
				name : 'cliendId',
				valueField : "value",
				displayField :"text",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'con.cliendId',
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
	// 第三方
	var thirdBox = new Ext.form.ComboBox({
				fieldLabel : '第三方',
				// store : [['1', '燃料供应商'], ['2', '设备供应商']],
				id : 'thirdClientId',
				name : 'con.thirdClientId',
				valueField : "value",
				displayField : "text",
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
// 币别
	var typeBox = new Ext.form.ComboBox({
		fieldLabel : '币别',
		//store : [['1', 'RMB'], ['2', 'USD']],
		store: typeStore,
		id : 'currencyType',
		name : 'currencyType',
		valueField : "value",
		displayField : "text",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'con.currencyType',
		editable : true,
//		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		emptyText : '请选择',
		anchor : '75.5%'
	});
	// 项目编号
	var projectNum = new Ext.form.ComboBox({
		id : "projectNum",
		name : "",
		fieldLabel : "项目编号",
		emptyText : '',
		displayField : "value",
		valueField : "text",
		readOnly : true,
		anchor : "85%"
//		,
//		onTriggerClick : function(e) {
//			var args = {
//				selectModel : 'single',
//				rootNode : {
//					id : "0",
//					text : '合同类别'
//				}
//			}
//			var url = "ProjectByCode.jsp";
//			var rvo = window
//					.showModalDialog(
//							url,
//							args,
//							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
//			if (typeof(rvo) != "undefined") {
//				Ext.get("projectNum").dom.value = rvo.prjNoShow;
//				Ext.get("projectName").dom.value = rvo.prjName;
//				Ext.get("hdnprojectNum").dom.value = rvo.prjNo
//			}
//		}
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
//		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		emptyText : '请选择',
		anchor : '85%'
	});
	// 合同开始时间
	var startDate = new Ext.form.TextField({
		id : 'startDate',
		fieldLabel : "合同履行开始时间",
		name : 'con.startDate',
		style : 'cursor:pointer',
		anchor : "85%",
		value : getDate()
//		listeners : {
//			focus : function() {
//				WdatePicker({
//					startDate : '%y-%M-%d 00:00:00',
//					dateFmt : 'yyyy-MM-dd HH:mm:ss',
//					alwaysUseStartDate : true
//				});
//			}
//		}
	});
	// 合同结束时间
	var endDate = new Ext.form.TextField({
		id : 'endDate',
		fieldLabel : "合同履行结束时间",
		name : 'con.endDate',
		style : 'cursor:pointer',
		anchor : "85%",
		value : getDate()
//		listeners : {
//			focus : function() {
//				var pkr = WdatePicker({
//					startDate : '%y-%M-%d 00:00:00',
//					dateFmt : 'yyyy-MM-dd HH:mm:ss',
//					alwaysUseStartDate : true
//				});
//			}
//		}
	});
	// 起草人
	var entryName = new Ext.form.TextField({
		id : 'entryName',
		// name : 'con.entryBy',
		xtype : 'textfield',
		fieldLabel : '起草人',
		readOnly : true,
		allowBlank : false,
		anchor : '85.5%'
	});
	// 起草时间
	var entryDate = new Ext.form.TextField({
		id : 'entryDate',
		fieldLabel : "起草日期",
		name : 'con.entryDate',
		style : 'cursor:pointer',
		readOnly : true,
		anchor : "85%",
		value : getDate()
	});

	// 合同文本
	var oriFileName = {
		id : "filePath",
		xtype : "textfield",
		fieldLabel : '合同文本',
		inputType : "file",
		// name : 'con.oriFileName',
		height : 22,
		anchor : "95%"
	}
	// 查看

	var btnView = new Ext.Button({
		id : 'btnView',
		text : '查看'	,
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

	var formtbar = new Ext.Toolbar({
		items : [{
			id : 'meetSignTab',
			text : "会签表",
			// iconCls : '',
			handler : function() {

			}
		}]
	});

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
					items : [{
						id : 'contractName',
						name : 'con.contractName',
						xtype : 'textfield',
						fieldLabel : '合同名称',
						readOnly : false,
						allowBlank : false,
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
						readOnly : false,
						allowBlank : false,
						anchor : '85%'
					}]
				}, {
					border : false,
					layout : 'form',
					columnWidth : 1,
					labelWidth : 110,
					items : [{
						id : 'conttreesNo',
						// name : 'con.conttreesNo',
						xtype : 'textfield',
						fieldLabel : '合同编号',
						value : '自动生成',
						readOnly : true,
						allowBlank : true,
						anchor : '92.5%'
					}]
				},  {
				border : false,
				layout : 'form',
				columnWidth : 1,
				labelWidth : 110,
				items : [cliendBox]
			   }, {
			    border : false,
				layout : 'form',
				columnWidth : 1,
				labelWidth : 110,
				items : [thirdBox]
			   }, {
				border : false,
				layout : 'form',
				columnWidth : .5,
				labelWidth : 110,
				items : [projectNum]
			   }, {
				border : false,
				layout : 'form',
				columnWidth : .5,
				labelWidth : 110,
				items : [projectName, hdnprojectNum]
			   }, {
					border : false,
					layout : 'form',
					columnWidth : .5,
					labelWidth : 110,
					items : [itemBox, {
						id : 'operateDepName',
						// name : 'con.operateDepCode',
						xtype : 'textfield',
						fieldLabel : '经办部门',
						readOnly : true,
						allowBlank : false,
						anchor : '85%'
					}]
				}, {
					border : false,
					layout : 'form',
					columnWidth : .5,
					labelWidth : 110,
					items : [{
						id : 'operateName',
						// name : 'con.operateBy',
						xtype : 'trigger',
						fieldLabel : '经办人',
						readOnly : true,
						allowBlank : false,
						anchor : '85%'
//						onTriggerClick : function(e) {
//						}
					}, unitBox]
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
						name : 'con.conAbstract',
						xtype : 'textarea',
						fieldLabel : '合同简介',
						readOnly : false,
						height : 120,
						anchor : '92.5%'
					}]
				}, {
					columnWidth : .5,
					layout : 'form',
					border : false,
					labelWidth : 110,
					items : [isMeet]
				}, {
					columnWidth : .5,
					layout : 'form',
					border : false,
					labelWidth : 110,
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
					columnWidth : 0.4,
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
					columnWidth : 1,
					labelWidth : 110,
					items : [{
						id : 'conId',
						name : 'con.conId',
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
	if (contractId != "") {
		Ext.Ajax.request({
			url : 'managecontract/findMeetConModel.action',
			params : {
				conid : contractId
			},
			method : 'post',
			waitMsg : '正在加载数据...',
			success : function(result, request) {
				var o = eval('(' + result.responseText + ')');
				form.getForm().loadRecord(o);
				 var node=new Object();
				 node.id=o.data.conTypeId;
				 node.text="测试";
				 Ext.getCmp('conType').setValue(node);
//				Ext.form.ComboBox.superclass.setValue.call(Ext
//				 .getCmp('conType'),"测试");
				method = "update";
				entryBy = o.data.entryBy;
				operateBy = o.data.operateBy;
				operateDepCode = o.data.operateDepCode;
					entryId = o.data.workflowNo;
						if (o.data.projectId != null) {
							Ext.get("projectNum").dom.value = o.data.projectId;
							hdnprojectNum.setValue(o.data.projectId);
						} else {
							Ext.get("projectNum").dom.value = "";
							hdnprojectNum.setValue("");
						}
						if (o.data.operateLeadBy != null) {
							Ext.getCmp('operateLeadBy')
									.setValue(o.data.operateLeadBy);
							Ext.form.ComboBox.superclass.setValue.call(Ext
											.getCmp('operateLeadBy'),
									o.data.operateLeadName);
						}
				if (o.data.isSum == "Y") {
					Ext.get('isTotal').dom.checked = true;
				} else {
					Ext.get('isTotal').dom.checked = false;
				}
				if (o.data.isSign == "Y") {
					Ext.get('isMeet').dom.checked = true;
				} else {
					Ext.get('isMeet').dom.checked = false;
				}
				if (o.data.isInstant == "Y") {
					Ext.get('isEmergency').dom.checked = true;
				} else {
					Ext.get('isEmergency').dom.checked = false;
				}
				annex_ds.load({
					params : {
						conid : contractId,
						type : "CONATT"
					}
				});
				Credent_ds.load({
					params : {
						conid : contractId,
						type : "CONEVI"
					}
				});
			},
			failure : function(result, request) {
				Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
			}
		});
	};

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
		// tbar : annextbar,
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
			}]);
	Credent_item_cm.defaultSortable = true;
	var Credent_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managecontract/findDocList.action'
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
		// title : '合同凭据',
		// collapsible : true,
		border : false
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

	});
	var docFile = {
		id : "docFile",
		xtype : "textfield",
		inputType : "file",
		fieldLabel : '选择附件',
		// allowBlank : false,
		anchor : "95%",
		height : 22,
		name : 'docFile'
	}
	var doccontent = new Ext.form.FieldSet({
		title : '合同附件/凭据维护',
		height : '100%',
		layout : 'form',
		items : [{
			id : 'conDocId',
			name : 'doc.conDocId',
			xtype : 'numberfield',
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