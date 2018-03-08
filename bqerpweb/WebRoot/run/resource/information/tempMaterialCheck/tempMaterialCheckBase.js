Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

var currentRecord = parent.currentRecord;

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

	s = s.substring(0, 10)
	return s;
}
Ext.onReady(function() {

	var ffid;
	if (currentRecord != null) {
		ffid = currentRecord.get("temp.tempId"); // getParameter("id"); 
	}
	// 流水号
	var tempId = {
		id : "tempId",
		xtype : "hidden",
		fieldLabel : 'ID',
		value : '',
		readOnly : true,
		name : 'temp.tempId',
		anchor : "40%"
	}
	var materialName = {
		id : "materialName",
		xtype : "textfield",
		fieldLabel : '物资名称',
		value : '',
		//		readOnly:true,
		allowBlank : false,
		name : 'temp.materialName',
		anchor : "74%"
	}
	var materialNo = {
		fieldLabel : '物资编码',
		name : 'temp.materialNo',
		xtype : 'textfield',
		readOnly : true,
		id : 'materialNo'
	}
	var specNo = {
		fieldLabel : '规格型号',
		name : 'temp.specNo',
		xtype : 'textfield',
		id : 'specNo',
		anchor : '86%',
		allowBlank : true
	};
	var parameter = {
		fieldLabel : '材质/参数',
		name : 'temp.parameter',
		xtype : 'textfield',
		anchor : '57%',
		id : 'parameter'
	}
	// 各种单位下拉框数据源
	var unitData = Ext.data.Record.create([{
		name : 'unitName'
	}, {
		name : 'unitId'
	}		//''retrieveCode',
	]);
	var allUnitStore = new Ext.data.JsonStore({
		url : 'resource/getAllUnitList.action',
		root : 'list',
		fields : unitData
	});
	allUnitStore.load();
	// 计价单位组合框
	var cbxStockUnit = new Ext.form.ComboBox({
		fieldLabel : "计价单位",
		// modify by ddr 20090618
		readOnly : false,
		id : 'cbxStockUnit',
		allowBlank : false,
		style : "border-bottom:solid 2px",
		triggerAction : 'all',
		store : allUnitStore,
		blankText : '',
		emptyText : '',
		valueField : 'unitId',
		displayField : 'unitName',
		mode : 'local',
		hiddenName : 'temp.stockUmId',
		listeners : {
			render : function() {
				this.clearInvalid();
			}
		}
	})

	// 仓库 DataStore
	var cboDelayStore = new Ext.form.TextField({
		id : 'defaultWhsNo',
		fieldLabel : '仓库',
		name : 'defaultWhsNo',
		readOnly : true,
		anchor : '42.5%',
		xtype : 'textfield'

	});
	var cboDelayStoreHid = new Ext.form.Hidden({
		hidden : false,
		id : "cboDelayStoreHid",
		name : 'temp.defaultWhsNo'
	});
	/**
	 * 格式化数据
	 */
	//	function comboBoxLocationRenderer(value, cellmeta, record) {
	//		var whsNo = defaultLocationNo.getValue();
	//		var locationNo =	record.data["locationNo"];
	//		var url = "resource/getLocationName.action?whsNo=" + whsNo
	//				+ "&locationNo=" + locationNo;
	//		var conn = Ext.lib.Ajax.getConnectionObject().conn;
	//		conn.open("POST", url, false);
	//		conn.send(null);
	//		return conn.responseText;
	//
	//	}
	//	var locationStore = new Ext.data.JsonStore({
	//				root : 'list',
	//				url : "resource/getLocationList.action？whsNo" + cboDelayStore.getValue(),
	//				fields : ['locationNo', 'locationName']
	//			})
	//	locationStore.load();		
	// 库位组合框
	//    var defaultLocationNo = new Ext.form.ComboBox({
	//        fieldLabel : "库位",
	//        name : "temp.defaultLocationNo",
	//        store : locationStore,
	//		displayField : "locationName",
	//		valueField : "locationNo",
	//		mode : 'local',
	//		triggerAction : 'all',
	//		readOnly : false
	//    });
	var defaultLocationNo = {
		fieldLabel : '缺省库位',
		name : 'temp.defaultLocationNo',
		xtype : 'textfield',
		id : 'defaultLocationNo'
	}

	var cbxMClassificationHid = new Ext.form.Hidden({
		hidden : false,
		id : "cbxMClassificationHid",
		name : 'temp.maertialClassId'
	});

	function getWhs() {

		Ext.Ajax.request({
			method : 'post',
			url : 'resource/getParentCode.action',
			params : {
				classId : cbxMClassificationHid.getValue()
			},
			method : 'post',
			success : function(result, request) {
				var obj = result.responseText;
				var mytext = obj.substring(1, obj.length - 1);

				var arr = mytext.split(',');

				Ext.get("defaultWhsNo").dom.value = arr[1];

				cboDelayStoreHid.setValue(arr[0]);
			}
		});

	}
	// 物料类别
	var txtMaterialClass = new Ext.ux.ComboBoxTree({
		fieldLabel : '物料类别',
		name : 'materialClassId',
		allowBlank : true,
		width : 240,
		id : "materialClassId",
		//				anchor : '100%',
		//				displayField : 'text',
		displayField : 'text',
		valueField : 'id',
		hiddenName : 'hdMaterialClassId',
		blankText : '请选择',
		emptyText : '请选择',
		// readOnly : true,
		tree : {
			xtype : 'treepanel',
			// 虚拟节点,不能显示
			rootVisible : false,

			loader : new Ext.tree.TreeLoader({
				dataUrl : 'resource/getMaterialClass.action'
			}),
			root : new Ext.tree.AsyncTreeNode({
				id : '-1',
				name : '合肥电厂',
				text : '合肥电厂'
			}),
			listeners : {
				click : function(node, e) {
					cbxMClassificationHid.setValue(node.attributes.code);
					getWhs();
				}
			}
		},
		selectNodeModel : 'leaf'
	});
	var factory = {
		fieldLabel : '生产厂家',
		name : 'temp.factory',
		xtype : 'textfield',
		anchor : "74%",
		id : 'factory'
	}
	var actPrice = {
		fieldLabel : '单价',
		name : 'temp.actPrice',
		xtype : 'numberfield',
		anchor : '86%',
		id : 'actPrice'
	}
	var checkDate = {
		fieldLabel : '核准日期',
		name : 'temp.checkDate',
		xtype : 'textfield',
		readOnly : true,
		hidden : true,
		id : 'checkDate'
	}

	var telNo = new Ext.form.TextField({
		id : 'telNo',
		fieldLabel : '联系电话',
		name : 'temp.telNo',
		anchor : '57%'
	});
	
	var checkBy = {
		fieldLabel : '核准人',
		name : 'temp.checkBy',
		xtype : 'textfield',
		hidden : true,
		readOnly : true,
		id : 'checkBy'
	}

	var memo = {
		id : "memo",
		name : 'temp.memo',
		height : 90,
		xtype : "textarea",
		fieldLabel : '备注',
		anchor : "74%"
	}

	var workApplyField = new Ext.form.FieldSet({
		border : true,
		labelWidth : 80,
		labelAlign : 'right',
		buttonAlign : 'center',
		layout : 'form',
		title : '修改物资编码维护',
		autoHeight : true,
		style : {
			"margin-top" : "20px",
			"margin-left" : "10px",
			"margin-right" : Ext.isIE6
					? (Ext.isStrict ? "-10px" : "-13px")
					: "0"
		},
		items : [{
			layout : "column",
			border : false,
			items : [{
				columnWidth : 1,
				layout : "form",
				border : false,
				items : [tempId]
			}, {
				columnWidth : 1,
				layout : "form",
				border : false,
				items : [materialName]
			}]
		}, {
			layout : "column",
			border : false,
			items : [
					//				{
					//				columnWidth : 0.4,
					//				layout : "form",
					//				border : false,
					//				items : [materialNo]
					//				},
					{
						columnWidth : 0.4,
						layout : "form",
						border : false,
						items : [specNo]
					}, {
						columnWidth : 0.6,
						layout : "form",
						border : false,
						items : [parameter]
					}, {
						columnWidth : 0.4,
						layout : "form",
						border : false,
						//				items : [stockUmId]
						items : [cbxStockUnit]
					}, {
						columnWidth : 0.6,
						layout : "form",
						border : false,
						items : [txtMaterialClass, cbxMClassificationHid]
					}
					//			,{
					//			    
					//			    columnWidth : 0.4,
					//				layout : "form",
					//				border : false,
					////				renderer : comboBoxLocationRenderer,
					//				items : [defaultLocationNo]
					//			}
					, {
						columnWidth : 0.6,
						layout : "form",
						border : false,
						items : [cboDelayStore, cboDelayStoreHid]
					}, {
						columnWidth : 1,
						layout : "form",
						border : false,
						items : [factory]
					}, {
						columnWidth : 0.4,
						layout : "form",
						border : false,
						items : [actPrice]
					},
					//				{
					//				columnWidth : 0.6,
					//				layout : "form",
					//				border : false,
					//				items : [checkDate]
					//			},{
					//				columnWidth : 0.4,
					//				layout : "form",
					//				border : false,
					//				items : [checkBy]
					//			},
					{
						columnWidth : 0.6,
						layout : "form",
						border : false,
						items : [telNo]
					}]

		}, {
			columnWidth : 0.5,
			layout : "form",
			border : false,
			items : [memo]
		}

		],
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : savePower
		}, {
			text : '清空',
			iconCls : 'cancer',
			handler : function() {
				form.getForm().reset();
				ffid = "";
			}
		}]
	});
	var form = new Ext.FormPanel({
		border : false,
		frame : true,
		items : [workApplyField],
		bodyStyle : {
			'padding-top' : '5px'
		},
		defaults : {
			labelAlign : 'right'
		}
	});
	function savePower() {

		// 限制空物资名称的输入  liuyi 05/25/09
		if (Ext.get("temp.materialName").dom.value == null
				|| "" == Ext.get("temp.materialName").dom.value) {
			Ext.Msg.alert("提示", "物资名称不能为空！")
		} else if (Ext.get("temp.stockUmId").dom.value == null
				|| "" == Ext.get('temp.stockUmId').dom.value) {
			Ext.Msg.alert("提示", "计价单位不能为空！")
		} else {

			var url = "";
			if (ffid == null || ffid == "") {
				url = "resource/addTempMaterial.action"
			} else {
				url = "resource/updateTempMaterial.action"
			}

			form.getForm().submit({
				method : 'POST',
				url : url,
				success : function(form, action) {

					var o = eval("(" + action.response.responseText + ")");
//					Ext.Msg.alert("注意", o.msg);
					//----------
					//清除Field

					if (o.msg == "增加成功！") {
						clearAllFields();
					}
					if (parent.document.all.iframe1 != null) {
						parent.document.all.iframe1.src = parent.document.all.iframe1.src;
						parent.Ext.getCmp("maintab").setActiveTab(0);
					}
				},
				failure : function(form, action) {
					var o = eval("(" + action.response.responseText + ")");
					Ext.Msg.alert("注意", o.msg);
				}
			});
		}
	}

	// 清除所有Field
	function clearAllFields() {
		form.getForm().reset();
	}

	var workApplyViewport = new Ext.Viewport({
		layout : "fit",
		border : false,
		height : 150,
		items : [form],
		defaults : {
			autoScroll : true
		}
	}).show();
	if (ffid != null && ffid != "") {
		if (currentRecord.get("temp.materialName") == null) {
			currentRecord.set("temp.materialName", "");
		}
		if (currentRecord.get("temp.materialNo") == null) {
			currentRecord.set("temp.materialNo", "");
		}
		if (currentRecord.get("temp.specNo") == null) {
			currentRecord.set("temp.specNo", "");
		}
		if (currentRecord.get("temp.parameter") == null) {
			currentRecord.set("temp.parameter", "");
		}
		if (currentRecord.get("temp.stockUmId") == null) {
			currentRecord.set("temp.stockUmId", "");
		}
		if (currentRecord.get("temp.defaultWhsNo") == null) {
			currentRecord.set("temp.defaultWhsNo", "");
		}
		if (currentRecord.get("temp.defaultLocationNo") == null) {
			currentRecord.set("temp.defaultLocationNo", "");
		}
		if (currentRecord.get("temp.maertialClassId") == null) {
			currentRecord.set("temp.maertialClassId", "");
		}
		if (currentRecord.get("temp.factory") == null) {
			currentRecord.set("temp.factory", "");
		}
		if (currentRecord.get("temp.actPrice") == null) {
			currentRecord.set("temp.actPrice", "");
		}
		if (currentRecord.get("temp.checkDate") == null) {
			currentRecord.set("temp.checkDate", "");
		}
		if (currentRecord.get("temp.telNo") == null) {
			currentRecord.set("temp.telNo", "");
		}
		if (currentRecord.get("temp.checkBy") == null) {
			currentRecord.set("temp.checkBy", "");
		}
		if (currentRecord.get("temp.memo") == null) {
			currentRecord.set("temp.memo", "");
		}

		Ext.getCmp('tempId').setValue(currentRecord.get("temp.tempId"));
		Ext.getCmp('materialName').setValue(currentRecord
				.get("temp.materialName"));
		//       Ext.getCmp('materialNo').setValue(currentRecord.get("temp.materialNo"));
		Ext.getCmp('specNo').setValue(currentRecord.get("temp.specNo"));
		Ext.getCmp('parameter').setValue(currentRecord.get("temp.parameter"));

		//  cbxStockUnit.setValue(currentRecord.get("temp.stockUmId"));
		var stockId = currentRecord.get("temp.stockUmId");
		var stockName = "";
		//取单位名称
		if (stockId !== null && stockId !== "") {
			var url = "resource/getRS001UnitName.action?unitCode=" + stockId;
			var conn = Ext.lib.Ajax.getConnectionObject().conn;
			conn.open("POST", url, false);
			conn.send(null);
			stockName = conn.responseText;
		}
		Ext.getCmp('cbxStockUnit').setValue(stockName);
		Ext.get("temp.stockUmId").dom.value = stockId;

		//取仓库名称
//		var whsNo = currentRecord.get("temp.defaultWhsNo");
//		var whsName = "";
//		if (whsNo) {
//			var url = "resource/getWarehouseName.action?whsNo=" + whsNo;
//			var conn = Ext.lib.Ajax.getConnectionObject().conn;
//			conn.open("POST", url, false);
//			conn.send(null);
//			whsName = conn.responseText;
//		}
		Ext.getCmp('cboDelayStoreHid').setValue(currentRecord
				.get("temp.defaultWhsNo"));
		var obj = new Object();
		obj = currentRecord.get("temp.defaultWhsNo");
		cboDelayStore.setValue(obj);

		Ext.getCmp('cbxMClassificationHid').setValue(currentRecord
				.get("temp.maertialClassId"));
		var obj = new Object();
		obj.text = currentRecord.get("maertialClassName");
		obj.id = "";
		txtMaterialClass.setValue(obj);
		Ext.getCmp('factory').setValue(currentRecord.get("temp.factory"));
		Ext.getCmp('actPrice').setValue(currentRecord.get("temp.actPrice"));
		//       Ext.getCmp('checkDate').setValue(currentRecord.get("temp.checkDate"));
		Ext.getCmp('telNo').setValue(currentRecord.get("temp.telNo"));
		//       Ext.getCmp('checkBy').setValue(currentRecord.get("temp.checkBy"));
		Ext.getCmp('memo').setValue(currentRecord.get("temp.memo"));
		getWhs();
	} else {
		form.getForm().reset();
	}

})
