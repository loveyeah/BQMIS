//function getPosition(obj) {
//	var top = 0;
//	var left = 0;
//	var width = obj.offsetWidth;
//	var height = obj.offsetHeight;
//	while (obj.offsetParent) {
//		top += obj.offsetTop;
//		left += obj.offsetLeft;
//		obj = obj.offsetParent;
//	}
//	return {
//		"top" : top,
//		"left" : left,
//		"width" : width,
//		"height" : height
//	};
//}
//function query() {
//	var str = document.getElementById("txtChinese").value.trim();
//	if (str == "")
//		return;
//	var arrRslt = makePy(str);
//	var div = document.getElementById("divResult");
//	div.style.position = "absolute";
//	div.style.display = "block";
//	div.innerHTML = "<div>&nbsp;"
//			+ arrRslt.join("</div><div>&nbsp;")
//			+ "</div><div align='center'><a style='cursor:hand;color:blue' onclick='this.parentNode.parentNode.style.display=\"none\"'>隐藏</a& gt;</div>";
//	var oDiv = getPosition(document.getElementById("txtChinese"));
//	div.style.top = oDiv.top + oDiv.height;
//	div.style.left = oDiv.left;
//	div.style.width = oDiv.width;
//	var divs = div.getElementsByTagName("div");
//	for (var i = 0; i < divs.length; i++) {
//		divs[i].className = i % 2 ? "divShuang" : "divDan";
//	}
//}
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() { 
	var connType = new Ext.form.ComboBox({
		fieldLabel : '连接词',
		store : new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"],
			data : [['&&', '【与】'], ['||', '【或】'], ['!', '【非】']]
		}),
		valueField : "retrunValue",
		displayField : "displayText",
		mode : 'local',
		forceSelection : true,
		hiddenName : 'connType',
		value : '1',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		name : 'connType'// 只能用name 不能用id 
	});
	var priority = new Ext.form.ComboBox({
		fieldLabel : '优先级',
		store : new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"],
			data : [['18', '高'], ['17', '次高'], ['16', '中'], ['15', '次中'],
					['14', '低'], ['13', '次低']]
		}),
		valueField : "retrunValue",
		displayField : "displayText",
		mode : 'local',
		forceSelection : true,
		hiddenName : 'priority',
		value : '1',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		width:150,
		name : 'priority' 
	});
	var baseInfoForm = new Ext.FormPanel({
		name : 'base-info-form',
		frame : true,
		autoWidth : true, 
		autoHeight:true,
		align : 'center',
		layout : 'fit',
		el : 'baseDiv',
		items : [{
			xtype : 'fieldset', 
			labelAlign : 'right',
			labelWidth : 100,
			autoHeight : true,
			defaultType : 'textfield',
			border : true,
			items : [{
				name : 'lineCode',
				fieldLabel : '事件ID',
				width:150,
				readOnly : true
			}, {
				name : 'lineName',
				fieldLabel : '事件名称',
				listeners:{
					change:function(){ 
						if(Ext.get("lineName").dom.value =="")
						return ; 
						var arrRslt = makePy(Ext.get("lineName").dom.value);
						Ext.get("changeBusiStateTo").dom.value = arrRslt;
					}
				},
			 
				width:150,
				allowBlank : false
			}, priority,{
				name : 'url',
				width:150,
				fieldLabel : '页面地址'
			},{
				name : 'changeBusiStateTo',
				width:150,
				fieldLabel : '事件标识'
			},{
				name : 'exeBusiComponent',
				width:150,
				fieldLabel : '执行业务组件'
			},{
				name : 'restrict',
				xtype:'textarea',
				width:300,
				fieldLabel : '约束' 
			}, {
				name : 'description',
				xtype:'textarea',
				width:300,
				fieldLabel : '备注'
			}],
			buttons : [{
				text : '保存',
				iconCls : 'save',
				handler : function() {
					if (baseInfoForm.getForm().isValid()) {
						setNodeFromHtml(v_curLine);
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
	var busiInfoForm = new Ext.FormPanel({
		name : 'busi-info-form',
		frame : true,
		autoWidth : true,   
		height : 350,
		align : 'center',
		layout : 'fit',
		el : 'busiDiv',
		items : [{
			xtype : 'fieldset', 
			labelAlign : 'right',
			labelWidth : 100,
			autoHeight : true,
			defaultType : 'textarea',
			border : true,
			items : [{
				name : 'preFuction',
				width:300,
				fieldLabel : '状态改变前执行' 
			}, {
				name : 'postFuction',
				width:300,
				fieldLabel : '状态改变后执行' 
			}],
			buttons : [{
				text : '保存',
				iconCls : 'save',
				handler : function() {
					if (busiInfoForm.getForm().isValid()) {
						setNodeFromHtml(v_curLine);
					}
				}
			}]
		}] 
	});  
	busiInfoForm.render(); 
//	var tabs = new Ext.TabPanel({
//		renderTo : 'tabs1',
//		autoWidth : true, 
//		activeTab : 0,
//		frame : true,
//		width : 550,
//		height : 400, 
//        defaults:{autoScroll: true},
//		
//		items : [{
//			title : '基本信息',
//			name:'base', 
//			contentEl : 'baseDiv',
//			layout:'fit'  
//		}, {
//			title : '约束/逻辑处理' ,
//			name:'busi',
//			contentEl : 'busiDiv',
//			listeners: {activate: handleActivate}, 
//			layout:'fit' 
//		}]
//	});
//	 function handleActivate(tab){
//        Ext.get("busiDiv").dom.style.display = "";
//    } 
//	tabs.render(Ext.getBody());
	 
	
	var curLine_grahic = window.dialogArguments;
	var v_curLine = curLine_grahic.connector;
	initHtmlFromNode(v_curLine);
	/**
	 * 初始化流程数据
	 */
	function initHtmlFromNode(v_nodeXml) {  
		//基本信息
		Ext.get("lineCode").dom.value  = v_nodeXml
				.selectSingleNode("propertyList/property[@name='lineCode']/value").text;
		Ext.get("lineName").dom.value = v_nodeXml
				.selectSingleNode("propertyList/property[@name='lineName']/value").text;
				
		priority.setValue(v_nodeXml.selectSingleNode("propertyList/property[@name='priority']/value").text);
		Ext.get("description").dom.value = v_nodeXml
				.selectSingleNode("description").text;  
		if (Ext.get("lineCode").dom.value == "") { 
			Ext.get("lineCode").dom.value = createLineCode(v_nodeXml);
		}  
		//约束,逻辑处理
		Ext.get("restrict").dom.value = v_nodeXml
				.selectSingleNode("propertyList/property[@name='restrict']/value").text;
		//url
		if( v_nodeXml.selectSingleNode("propertyList/property[@name='url']/value"))
		Ext.get("url").dom.value = v_nodeXml.selectSingleNode("propertyList/property[@name='url']/value").text; 
		//changeBusiStateTo 
		if( v_nodeXml.selectSingleNode("propertyList/property[@name='changeBusiStateTo']/value"))
		Ext.get("changeBusiStateTo").dom.value = v_nodeXml.selectSingleNode("propertyList/property[@name='changeBusiStateTo']/value").text;
		//exeBusiComponent
		if( v_nodeXml.selectSingleNode("propertyList/property[@name='exeBusiComponent']/value"))
		Ext.get("exeBusiComponent").dom.value = v_nodeXml.selectSingleNode("propertyList/property[@name='exeBusiComponent']/value").text;
		
		Ext.get("preFuction").dom.value = v_nodeXml
				.selectSingleNode("propertyList/property[@name='preFuction']/value").text;
		Ext.get("postFuction").dom.value = v_nodeXml
				.selectSingleNode("propertyList/property[@name='postFuction']/value").text;
	}
	/**
	 * 由初始状态取得事件编号
	 */
	function createLineCode(v_nodeXml)
	{   
		var from = v_nodeXml.selectSingleNode("from").text; 
        var to = v_nodeXml.selectSingleNode("to").text; 
        var id = from + to; 
		return id;
	}
	/**
	 * 保存流程数据
	 */
	function setNodeFromHtml(v_nodeXml) { 
		//基本信息
		v_nodeXml.selectSingleNode("propertyList/property[@name='lineCode']/value").text = Ext.get("lineCode").dom.value;
		v_nodeXml.selectSingleNode("propertyList/property[@name='lineName']/value").text = Ext.get("lineName").dom.value;
		v_nodeXml.selectSingleNode("description").text = Ext.get("description").dom.value;
		v_nodeXml.selectSingleNode("propertyList/property[@name='priority']/value").text = Ext.get("priority").dom.value;
		window.close();
	
		//页面链接地址
		if(v_nodeXml.selectSingleNode("propertyList/property[@name='url']/value"))
		v_nodeXml.selectSingleNode("propertyList/property[@name='url']/value").text = Ext.get("url").dom.value ; 
		//将业务状态改变为
		 if(v_nodeXml.selectSingleNode("propertyList/property[@name='changeBusiStateTo']/value"))
		v_nodeXml.selectSingleNode("propertyList/property[@name='changeBusiStateTo']/value").text = Ext.get("changeBusiStateTo").dom.value ; 
		//执行业务组件exeBusiComponent
		 if(v_nodeXml.selectSingleNode("propertyList/property[@name='exeBusiComponent']/value"))
		v_nodeXml.selectSingleNode("propertyList/property[@name='exeBusiComponent']/value").text = Ext.get("exeBusiComponent").dom.value ; 
		//约束,逻辑处理
	    v_nodeXml.selectSingleNode("propertyList/property[@name='restrict']/value").text = Ext.get("restrict").dom.value ;
		v_nodeXml.selectSingleNode("propertyList/property[@name='preFuction']/value").text = Ext.get("preFuction").dom.value;
		v_nodeXml.selectSingleNode("propertyList/property[@name='postFuction']/value").text = Ext.get("postFuction").dom.value; 
	} 
});