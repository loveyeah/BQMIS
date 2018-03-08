Ext.ns("Power.budgetItem");
/**
 * 
 * @param {} cbConfig
 * @param {} itemType 指标类别 1----费用指标 2------财务指标
 * @return {}
 */
Power.budgetItem = function(cbConfig,itemType)
{
	
	

	function getDate() {
		var Y;
		Y = new Date();
		Y = Y.getFullYear().toString(10);
		return Y;
	}
	
	var time = new Ext.form.TextField({
				id : 'time',
				allowBlank : true,
				readOnly : true,
				value : getDate(),
//				width : 100,
				listeners : {
					focus : function() {
						WdatePicker({
									dateFmt : 'yyyy',
									alwaysUseStartDate : false,
									onclearing : function() {
										planStartDate.markInvalid();
									}
								});
					}
				}
			});
			
			// 查询
	var btnQuery = new Ext.Button({
				id : "btnQuery",
				text : "查询",
				iconCls : "query",
				handler : function query() {
//					alert(itemType);
					itemTree.loader.dataUrl = 'managebudget/getItemIdTree.action?budgetTime='+time.getValue()+'&itemType='+itemType;
					rootNodeObj.reload();

					
				}
			})

function clickTree()
{
	var node =itemTree.getSelectionModel().getSelectedNode();
    if(node==null)
    {
    	Ext.Msg.alert("提示","请选择指标！");
    	return;
    }
	
				var obj = getValue();
							if(itemType==2)
		{
		if(obj.description!="2")
		{
			Ext.Msg.alert("提示","此指标不是财务指标！");
			return;
		}
		}
		else
		{
			if(obj.description==null||obj.description=="")
		  {
			Ext.Msg.alert("提示","此指标未编制，不能选择！");
			return;
		  }
		}
					    setValue(obj.key, obj.value);
						win.hide();
}
			
			
	var btnOk = new Ext.Button({
				id : "btnOk",
				text : "确定",
				iconCls : "",
				handler :clickTree
			})
	var rootNodeObj =new Ext.tree.AsyncTreeNode({
				text : '预算指标',
				iconCls : 'x-tree-node-icon',
				draggable : false,
				id : '000'
			});
	var itemTree = new Ext.tree.TreePanel({
		loader : new Ext.tree.TreeLoader({
			dataUrl : 'managebudget/getItemIdTree.action?pid=000&budgetTime='+time.getValue()+'&itemType='+itemType
		}),
		root : rootNodeObj,
		autoWidth : true,
		autoScroll : true,
		animate : true,
		enableDD : false,
		border : false,
		rootVisible : true,
		containerScroll : true,
		tbar:['年份',time, '-', btnQuery,'-',btnOk]
	});  
	
	itemTree.on('beforeload', function(node) {
		
				pid=node.attributes.code==null?'000':node.attributes.code;
//				alert(pid);
				itemTree.loader.dataUrl = 'managebudget/getItemIdTree.action?pid='+pid+'&budgetTime='+time.getValue()+'&itemType='+itemType;

			});
	itemTree.on("click",function(node,e){
		
		if(itemType==2)
		{
		if(node.attributes.description!="2")
		{
			Ext.Msg.alert("提示","此指标不是财务指标！");
			
		}
		}
		else
		{
			if(node.attributes.description==null||node.attributes.description=="")
		  {
			Ext.Msg.alert("提示","此指标未编制，不能选择！");
			
		  }
		}
		 e.stopEvent();
    });

    itemTree.on("dblclick", clickTree, this);

	var win = new Ext.Window({
		closeAction : 'hide',
		width:600,
		height:450,
		title : "费用来源选择",
		modal : true,
		layout : 'border',
		items : [{
			region : 'center',
			layout : 'fit',
			split:true,
			items : [itemTree]
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
		fieldLabel : "费用来源",
		store : cbStore,
		mode : 'local',
		hiddenName :'hItem',
		name : 'myItem',
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
		var node =itemTree.getSelectionModel().getSelectedNode(); 
		if (typeof(node) != "undefined") {
			var ro = new Object();
			ro.key = node.id; 
			ro.value = node.text;
			ro.description=node.attributes.description;
			return ro;
		} else {
			return null;
		}
	};
	return {
		treeRoot:rootNodeObj,
		tree : itemTree,
		win:win,
		
		combo:combo,
		setValue:setValue,
		getValue:getValue
	}
	
}