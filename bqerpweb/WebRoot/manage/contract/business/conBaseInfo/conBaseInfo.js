Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var id = getParameter("id");
	var entryId;
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
				if(o.data.itemId != null){
				itemBox.setValue((o.data.itemId == "zzfy")?"生产成本":"劳务成本");
				entryId = o.data.workflowNo;
				}
				if(o.data.filePath == null || o.data.filePath == ""){
							Ext.getCmp("btnView").setVisible(false);
							}
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
				Material_ds.load({
					params : {
						conId : id
					}
				})
			},
			failure : function(result, request) {
				Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
			}
		});
	};
	// 币别
	var typeBox = new Ext.form.ComboBox({
		fieldLabel : '币别',
		store : [['1', 'RMB'], ['2', 'USD']],
		id : 'currencyName',
		name : 'currencyType',
		valueField : "value",
		displayField : "text",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'con.currencyType',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		emptyText : '请选择',
		anchor : '75.5%'
	});
	typeBox.on('beforequery', function() {
		return false
	});
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
	var cliendBox = new Ext.form.ComboBox({
		fieldLabel : '供应商',
		store : [['1', '燃料供应商'], ['2', '设备供应商']],
		id : 'cliendId',
		name : 'cliendId',
		valueField : "value",
		displayField : "text",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'clientName',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		emptyText : '请选择',
		anchor : '92.5%'
	});
	cliendBox.on('beforequery', function() {
		return false
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
		emptyText : '请选择',
		anchor : '85%'
	});
	itemBox.on('beforequery', function() {
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
	var btnSign = new Ext.Toolbar({
		items : [{
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
					url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
								+ "bqCGContract";
								window.open(url);
				} else {
					var url = "/power/workflow/manager/show/show.jsp?entryId="
							+ entryId;
					window.open(url);
				}
			
				}
		}]
	});
	var form = new Ext.FormPanel({
			title : '基本信息',
			//width : 800,
			tbar : btnSign,
			labelAlign : 'right',
			labelWidth : 100,
			collapsible : true,// add
			autoHeight : true,
		items : [
		 new Ext.form.FieldSet({
		 title : '基本信息',
		 collapsible : true,
		 height : '100%',
		 layout : 'form',
		 items : [
		{
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
					// name : 'con.conttreesNo',
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
				labelWidth : 110,
				items : [itemBox, {
					id : 'operateDepName',
					// name : 'con.operateDepCode',
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
//				, unitBox
				]
			}, {
				border : false,
				layout : 'form',
				columnWidth : 0.3,
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
				columnWidth : 0.4,
				labelWidth : 110,
				items : [actAmount]
			}
//			, {
//				columnWidth : .5,
//				layout : 'form',
//				labelWidth : 110,
//				border : false,
//				items : [startDate]
//			}, {
//				columnWidth : .5,
//				layout : 'form',
//				labelWidth : 110,
//				border : false,
//				items : [endDate]
//			}
			, {
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
			}]
		}
		 ]
		 })
		]
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
		//tbar : new Ext.Toolbar({
		//items : ['合同附件']
		//}),
		 title : '合同附件',
		 collapsible : true,
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
		//tbar : new Ext.Toolbar({
		//items : ['合同凭据']
		//}),
		 title : '合同凭据',
		 collapsible : true,
		split : true,
		height : 150,
		width : Ext.get('div_lay').getWidth(),
		border : false
	});
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
			url : 'managecontract/findMaterialsByConId.action'
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
})