Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var arg = window.dialogArguments
	var currentRecord = arg.record;
	var strBusiNo = arg.busiNo;
	// ////////////////////////////详细信息start
	var applyId = {
		id : "applyId",
		xtype : "hidden",
		fieldLabel : 'ID',
		value : '',
		readOnly : true,
		name : 'power.applyId',
		anchor : "40%"
	}
	var protectNo = {
		id : "protectNo",
		xtype : "textfield",
		fieldLabel : '申请单号',
		value : '自动生成',
		readOnly : true,
		name : 'power.protectNo',
		anchor : "93%"
	}
	var applyDept = {
		fieldLabel : '申请部门',
		readOnly : true,
		name : 'applyDept',
		xtype : 'textfield',
		id : 'applyDept',
		anchor : "93%"
	};

	var applyBy = {
		fieldLabel : '申请人',
		name : 'applyBy',
		xtype : 'textfield',
		id : 'applyBy',
		readOnly : true,
		anchor : "93%"
	};

	var applyDate = new Ext.form.TextField({
		id : 'applyDate',
		fieldLabel : "申请日期",
		readOnly : true,
		name : 'power.applyDate',
		anchor : "93%"
	});

	var equCode = {
		fieldLabel : '设备名称',
		name : 'equCode',
		xtype : 'textfield',
		readOnly : true,
		id : 'equCode',
		anchor : "93%"
	};

	var protectName = {
		id : "protectName",
		readOnly : true,
		xtype : "textfield",
		fieldLabel : '保护名称',
		name : 'power.protectName',
		anchor : "93%"
	}
	var protectReason = {
		id : "protectReason",
		height : 80,
		readOnly : true,
		xtype : "textarea",
		fieldLabel : '投退原因',
		name : 'power.protectReason',
		anchor : "93%"
	}
	var equEffect = {
		id : "equEffect",
		height : 80,
		readOnly : true,
		xtype : "textarea",
		fieldLabel : '对系统或设备的影响',
		name : 'power.equEffect',
		anchor : "93%"
	}

	var outSafety = {
		id : "outSafety",
		height : 80,
		readOnly : true,
		xtype : "textarea",
		fieldLabel : '保护退出时安措',
		name : 'power.outSafety',
		anchor : "93%"
	}
	var memo = {
		id : "memo",
		height : 60,
		readOnly : true,
		xtype : "textarea",
		fieldLabel : '备注',
		name : 'power.memo',
		anchor : "93%"
	}
	var applyStartDate = new Ext.form.TextField({
		id : 'applyStartDate',
		fieldLabel : "申请开工时间",
		readOnly : true,
		name : 'power.applyStartDate',
		anchor : "93%"
	});
	var applyEndDate = new Ext.form.TextField({
		id : 'applyEndDate',
		fieldLabel : "申请完工时间",
		readOnly : true,
		name : 'power.applyEndDate',
		anchor : "93%"
	});
	var baseInfoField = new Ext.form.FieldSet({
		autoHeight : true,
		autoScroll:true,
		labelWidth : 120,
		style : {
			"padding-top" : '0',
			"padding-left" : '5'
		},
		bodyStyle : Ext.isIE ? 'padding:0 0 0px 5px;' : 'padding:0px 5px;',
		anchor : '-20',
		border : false,
		items : [applyId, protectNo, equCode, applyBy, applyDept, protectName,
				applyDate, applyStartDate, applyEndDate, protectReason,
				equEffect, outSafety, memo]
	});

	var detailPanel = new Ext.Viewport({
		border : false,
		frame : true,
		layout : 'fit', 
		items : [baseInfoField],
		bodyStyle : {
			'padding-top' : '5px'
		},
		defaults : {
			labelAlign : 'right'
		}
	});

	if (currentRecord != null) {
		Ext.get("protectNo").dom.value = strBusiNo;
		Ext.getCmp('applyBy').setValue(currentRecord.get("power.applyBy"));

		Ext.form.ComboBox.superclass.setValue.call(Ext.getCmp('applyBy'),
				currentRecord.get("applyName"));

		Ext.getCmp('applyDept').setValue(currentRecord.get("power.applyDept"));

		Ext.form.ComboBox.superclass.setValue.call(Ext.getCmp('applyDept'),
				currentRecord.get("applyDeptName"));
		Ext.get("applyDate").dom.value = currentRecord.get("applyDate");
		Ext.getCmp('equCode').setValue(currentRecord.get("power.equCode"));
		Ext.form.ComboBox.superclass.setValue.call(Ext.getCmp('equCode'),
				currentRecord.get("equName"));
		Ext.get("protectName").dom.value = currentRecord
				.get("power.protectName");
		Ext.getCmp('applyStartDate').setValue(currentRecord
				.get("applyStartDate"));
		Ext.getCmp('applyEndDate').setValue(currentRecord.get("applyEndDate"));
		Ext.get("protectReason").dom.value = currentRecord
				.get("power.protectReason");
		Ext.get("equEffect").dom.value = currentRecord.get("power.equEffect");
		Ext.get("outSafety").dom.value = currentRecord.get("power.outSafety");
		Ext.get("memo").dom.value = currentRecord.get("power.memo");
	}

		// ////////////////////////////详细信息end
	});