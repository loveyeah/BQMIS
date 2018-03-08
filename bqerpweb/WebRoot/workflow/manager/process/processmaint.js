Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() { 
	var formProcess = new Ext.FormPanel({
		id : 'process-form',
		frame : true,
		autoWidth : true,
		height : 350,
		autoHeight : true,
		align : 'center',
		layout : 'fit',
		el : 'formProcess',
		items : [{
			xtype : 'fieldset',
			width : 260,
			labelAlign : 'right',
			labelWidth : 55,
			autoHeight : true,
			defaultType : 'textfield',  
			border : true,
			items : [{
				id : 'processcode',
				fieldLabel : '流程编码',
				width:200,
				allowBlank : false
			}, {
				id : 'processname',
				width:200,
				fieldLabel : '流程名称',
				allowBlank : false
			}, {
				id : 'processversion',
				width:200,
				fieldLabel : '版本',
				allowBlank : false
			}, {
				id : 'description',
				xtype:'textarea',
				width:200,
				fieldLabel : '描述'
			}, {
				id : 'author',
				fieldLabel : '创建者',
				width:200,
				readOnly : true
			}, {
				id : 'department',
				fieldLabel : '部门',
				width:200,
				readOnly : true
			}],
			buttons : [{
				text : '保存',
				iconCls : 'save',
				handler : function() {
					if (formProcess.getForm().isValid()) {
						setNodeFromHtml(v_process_node);
					}
				}
			}]
		}]
	});
	formProcess.render();
	var v_process_node = window.dialogArguments;
	initHtmlFromNode(v_process_node);
	function initHtmlFromNode(v_process_node) {
		// <-----------------------------------------------------基本信息--------------------------------------------------->
		Ext.get("processcode").dom.value = v_process_node
				.selectSingleNode("propertyList/property[@name='ProcessCode']/value").text;
		Ext.get("processname").dom.value = v_process_node
				.selectSingleNode("propertyList/property[@name='ProcessName']/value").text;
		Ext.get("processversion").dom.value = v_process_node
				.selectSingleNode("propertyList/property[@name='ProcessVersion']/value").text;
		Ext.get("description").dom.value = v_process_node
				.selectSingleNode("description").text;
	}

	// 将页面HTML上得元素值设置到节点上
	function setNodeFromHtml(v_process_node) {
		// <-----------------------------------------------------基本信息--------------------------------------------------->
		if (Ext.get("processcode").dom.value == "") {
			alert('请输入流程ID！');
			return false;
		} else if (Ext.get("processname").dom.value == "") {
			alert('请输入流程名称！');
			return false;
		} else if (Ext.get("processversion").dom.value == "") {
			alert('请输入流程版本信息！');
			return false;
		}
		// 设置模板id
		v_process_node
				.selectSingleNode("propertyList/property[@name='ProcessCode']/value").text = Ext
				.get("processcode").dom.value;
		// 设置模板名称
		v_process_node
				.selectSingleNode("propertyList/property[@name='ProcessName']/value").text = Ext
				.get("processname").dom.value;
		v_process_node
				.selectSingleNode("propertyList/property[@name='ProcessVersion']/value").text = Ext
				.get("processversion").dom.value;
		v_process_node.selectSingleNode("description").text = Ext
				.get("description").dom.value; 
		window.close(); 
	}
});