Ext.namespace('Power.dept'); 
/**
 * 
 * @param {} config
 * @param {} isQueryAllChildDept  是否查询所有部门，包括已注销部门
 * @return {}
 */
Power.dept = function(config,isQueryAllChildDept,cbConfig) { 
	var rootNode = (config && config.rootNode) ? config.rootNode : {
		id : '0', 
		text : '灞桥电厂'
	};
	var rootNodeObj = new Ext.tree.AsyncTreeNode(rootNode);
	var deptTree = new Ext.tree.TreePanel({
		loader : new Ext.tree.TreeLoader({
			dataUrl : isQueryAllChildDept?'comm/getAllDeptsByPid.action':'comm/getDeptsByPid.action'
		}),
		root : rootNodeObj,
		autoWidth : true,
		autoScroll : true,
		animate : true,
		enableDD : false,
		border : false,
		rootVisible : true,
		containerScroll : true
	});  
	deptTree.on("click",function(node,e){
		 e.stopEvent();
    });
	var btnConfrim = new Ext.Button({
			text:"确认",
			id:'btnConfrim',
			iconCls:'confirm',
			handler:function(){
				var obj = getValue();
			    setValue(obj.key, obj.value);
				win.hide();
			}
		});

	var win = new Ext.Window({
		closeAction : 'hide',
		width:600,
		height:450,
		title : "部门选择",
		modal : true,
		layout : 'border',
		items : [{
			region : 'center',
			layout : 'fit',
			split:true,
			items : [deptTree]
		}],
		buttons:[btnConfrim,{
			text:"取消",
			iconCls:'cancer',
			handler:function(){
				win.hide();
			}
		}]
	});  
	var KeyValue = new Ext.data.Record.create([{
			name : 'key'
		}, {
			name : 'value'
		}]);  
	var cbStore = new Ext.data.Store({
		reader : new Ext.data.JsonReader({}, KeyValue)
	});  
	var combo = new Ext.form.ComboBox({
		fieldLabel : "部门",
		store : cbStore,
		mode : 'local',
		hiddenName :'dept',
		name : 'dept',
		width: 180,
		valueField : 'key',
		displayField : 'value',
		editable : true,
		triggerAction : 'all',
		forceSelection:true,
		readOnly : true,
		onTriggerClick : function() {
			if(!this.disabled)
			{
				win.show();
			}
		}
	});
	
	Ext.apply(combo,cbConfig);
	function setValue(key,value) {
		var d1 = new KeyValue({
			key : key,
			value : value
		});  
		cbStore.removeAll();
		cbStore.add(d1);
		combo.setValue(key);
	}
	function getValue() { 
		var node =deptTree.getSelectionModel().getSelectedNode(); 
		if (typeof(node) != "undefined") {
			var ro = new Object(); 
			ro.key = node.id; 
			ro.value = node.text;
			// add by liuyi 20100421 
			ro.code = null;
			if(node.attributes.code)
				ro.code = node.attributes.code
			return ro;
		} else {
			return null;
		}
	};
	return {
		treeRoot:rootNodeObj,
		tree : deptTree,
		win:win,
		btnConfrim:btnConfrim,
		combo:combo,
		setValue:setValue,
		getValue:getValue
	}
};