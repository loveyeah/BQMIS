Ext.namespace('trainMaint.certificate');

trainMaint.certificate = function(config, isQueryAllChildDept, cbConfig) {
	var rootNode = {
		id : '0',
		text : '证书类别'
	};
	var rootNodeObj = new Ext.tree.AsyncTreeNode(rootNode);
	var certificateTree = new Ext.tree.TreePanel({
				loader : new Ext.tree.TreeLoader({
							dataUrl : "com/listCertificate.action?pid=0"
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
	certificateTree.on("click", function(node, e) {
				e.stopEvent();
			});
	certificateTree.on('beforeload', function(node) {
				certificateTree.loader.dataUrl = 'com/listCertificate.action?pid='
						+ node.id;
			}, this);
	var btnConfrim = new Ext.Button({
				text : "确认",
				id : 'btnConfrim',
				iconCls : 'confirm',
				handler : function() {
					var obj = getValue();
					setValue(obj.key, obj.value);
					win.hide();
				}
			});

	var win = new Ext.Window({
				closeAction : 'hide',
				width : 600,
				height : 450,
				title : "证书类别",
				modal : true,
				layout : 'border',
				items : [{
							region : 'center',
							layout : 'fit',
							split : true,
							items : [certificateTree]
						}],
				buttons : [btnConfrim, {
							text : "取消",
							iconCls : 'cancer',
							handler : function() {
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
				fieldLabel : "证书类别",
				store : cbStore,
				mode : 'local',
				hiddenName : 'dept',
				name : 'dept',
				width : 180,
				valueField : 'key',
				displayField : 'value',
				editable : true,
				triggerAction : 'all',
				forceSelection : true,
				readOnly : true,
				onTriggerClick : function() {
					if (!this.disabled) {
						win.show();
					}
				}
			});

	Ext.apply(combo, cbConfig);
	function setValue(key, value) {
		var d1 = new KeyValue({
					key : key,
					value : value
				});
		cbStore.removeAll();
		cbStore.add(d1);
		combo.setValue(key);
	}

	function getValue() {
		var node = certificateTree.getSelectionModel().getSelectedNode();
		if (typeof(node) != "undefined") {
			var ro = new Object();
			ro.key = node.id;
			ro.value = node.text;
			return ro;
		} else {
			return null;
		}
	};

	return {
		treeRoot : rootNodeObj,
		tree : certificateTree,
		win : win,
		btnConfrim : btnConfrim,
		combo : combo,
		setValue : setValue,
		getValue : getValue
	}

};