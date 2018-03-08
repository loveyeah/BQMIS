Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() { 
	var splitType = new Ext.form.ComboBox({
		fieldLabel : '输出模式',
		store : new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"],
			data : [['XOR', '单一分支'], ['OR', '多路分支'], ['AND', '全部分支']]
		}),
		valueField : "retrunValue",
		displayField : "displayText",
		mode : 'local',
		forceSelection : true,
		hiddenName : 'splitType',
		value : '1',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		name : 'splitType',//只能用name 不能用id
		width:300
	});
	var joinType = new Ext.form.ComboBox({
		fieldLabel : '输入模式',
		store : new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"],
			data : [['XOR', '单一聚合'], ['OR', '多路聚合'], ['AND', '全部聚合']]
		}),
		valueField : "retrunValue",
		displayField : "displayText",
		mode : 'local',
		forceSelection : true,
		hiddenName : 'joinType',
		value : '1',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		name : 'joinType',
		width:300
	});
	var baseInfoForm = new Ext.FormPanel({ 
		frame : true, 
		autoWidth : true, 
		autoHeight : true, 
		align : 'center',
		layout : 'fit',
		el : 'baseDiv',
		items : [{
			xtype : 'fieldset',
			width : 260,
			labelAlign : 'right',
			labelWidth : 55,
			autoHeight : true,
			defaultType : 'textfield', 
			border : true,
			items : [{
				id : 'activitycode',
				fieldLabel : '状态ID',
				width:300,
				readOnly : true
			}, {
				id : 'activityname',
				fieldLabel : '状态名称',
				width:300,
				allowBlank : false
			},{
				id:'url',
				fieldLabel : '审批链接',
				width:300
			},{
				id:'timeLimit',
				xtype:'numberfield',
				fieldLabel : '审批时限',
				width:300
			}, {
				id : 'description',
				xtype:'textarea',
				fieldLabel : '备注',
				width:300
			}, splitType, joinType],
			buttons : [{
				text : '保存',
				iconCls : 'save',
				handler : function() {
					if (baseInfoForm.getForm().isValid()) {
						setNodeFromHtml(v_nodeXml);
					}
				}
			},{
				text : '取消',
				iconCls : 'cancel',
				handler : function() {
					window.close();
				}
			}]
		}]
	});
	baseInfoForm.render(); 
	
	var curIcon_grahic = window.dialogArguments;
	var v_nodeXml = curIcon_grahic.activity;
	initHtmlFromNode(v_nodeXml);
	function initHtmlFromNode(v_nodeXml) {
		// <-----------------------------------------------------基本信息--------------------------------------------------->
		Ext.get("activitycode").dom.value = v_nodeXml.getAttribute("id"); 
		Ext.get("activityname").dom.value = v_nodeXml.selectSingleNode("propertyList/property[@name='ActivityName']/value").text;
		Ext.get("description").dom.value = v_nodeXml.selectSingleNode("description").text;
		var stateType =v_nodeXml.getAttribute("typeName"); 
		switch (stateType)
		{ 
			case 'ManualActivity':
			{
				initMA(v_nodeXml);
				break;
			}
			case 'StartActivity':
			{
				initSA(v_nodeXml);
				break;
			}
			case 'JoinActivity':
			{
				joinType.setValue(v_nodeXml.selectSingleNode("propertyList/property[@name='joinType']/value").text); 
				break;
			}
			case 'EndActivity':
			{
				initEA(v_nodeXml);
				break;
			} 
		} 
	}
	function initEA(v_nodeXml)
	{
		Ext.get("url").dom.readOnly = true;
		Ext.get("timeLimit").dom.readOnly = true; 
		joinType.setValue(v_nodeXml.selectSingleNode("propertyList/property[@name='joinType']/value").text); 
	}
	function initSA(v_nodeXml)
	{
		Ext.get("url").dom.value = v_nodeXml
				.selectSingleNode("propertyList/property[@name='url']/value").text; 
		Ext.get("timeLimit").dom.value = v_nodeXml
				.selectSingleNode("propertyList/property[@name='timeLimit']/value").text; 	
		splitType.setValue(v_nodeXml
						.selectSingleNode("propertyList/property[@name='splitType']/value").text); 
	} 
	function initMA(v_nodeXml)
	{ 
	 	initSA(v_nodeXml);
		joinType.setValue(v_nodeXml.selectSingleNode("propertyList/property[@name='joinType']/value").text); 
	}
	
	function setNodeFromHtml(v_nodeXml) {
		// <-----------------------------------------------------基本信息--------------------------------------------------->
		v_nodeXml.selectSingleNode("propertyList/property[@name='ActivityName']/value").text = Ext.get("activityname").dom.value; 
		v_nodeXml.selectSingleNode("propertyList/property[@name='ActivityCode']/value").text = v_nodeXml.getAttribute("id");
		v_nodeXml.selectSingleNode("description").text = Ext.get("description").dom.value;
			var stateType =v_nodeXml.getAttribute("typeName"); 
		switch (stateType)
		{ 
			case 'ManualActivity':
			{
				setMA(v_nodeXml);
				break;
			}
			case 'StartActivity':
			{
				setSA(v_nodeXml);
				break;
			}
			case 'JoinActivity':
			{
				
				v_nodeXml.selectSingleNode("propertyList/property[@name='joinType']/value").text = Ext.get("joinType").dom.value;
				break;
			}
			case 'EndActivity':
			{
				setEA(v_nodeXml);
				break;
			} 
		} 
		
		window.close();
	} 
	
	function setEA(v_nodeXml)
	{
		v_nodeXml.selectSingleNode("propertyList/property[@name='joinType']/value").text = Ext.get("joinType").dom.value;
	}
	function setSA(v_nodeXml)
	{
		v_nodeXml.selectSingleNode("propertyList/property[@name='url']/value").text = Ext
				.get("url").dom.value;
		v_nodeXml
				.selectSingleNode("propertyList/property[@name='timeLimit']/value").text = Ext
				.get("timeLimit").dom.value; 
		v_nodeXml
				.selectSingleNode("propertyList/property[@name='splitType']/value").text = Ext
				.get("splitType").dom.value;
	} 
	function setMA(v_nodeXml)
	{ 
	 	setSA(v_nodeXml);
		v_nodeXml.selectSingleNode("propertyList/property[@name='joinType']/value").text = Ext.get("joinType").dom.value;
	} 
});