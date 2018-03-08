Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var currentType = 'add';
	var currentNode = new Object();
	var cNode = "";
	// currentNode.id = "root";
	// currentNode.text = "锅炉设备树";
	// // -----------定义tree-----------------
	var root = new Ext.tree.AsyncTreeNode({
		id : "root",
		text : "锅炉设备树"
	});

	currentNode = root;
	var mytree = new Ext.tree.TreePanel({
		renderTo : "treepanel",
		autoHeight : true,
		root : root,
		border : false,
		loader : new Ext.tree.TreeLoader({
			url : "security/getTreeListForBoiler.action",
			baseParams : {
				id : 'root'
			}
		})
	});

	// ---------------------------------------
	// -----------树的事件----------------------
	// 树的单击事件
	root.expand();// 展开根节点
	mytree.on("click", clickTree, this);
	mytree.on('beforeload', function(node) {
		// 指定某个节点的子节点
		mytree.loader.dataUrl = 'security/getTreeListForBoiler.action?id='
				+ node.id;
	}, this);
	// ------------
	function initForm() {
		Ext.get("boilerId").dom.value = "";
		Ext.get("boilerName").dom.value = "";
		Ext.get("boilerType").dom.value = "";
		Ext.get("manufacturer").dom.value = "";
		Ext.get("boilerNumber").dom.value = "";
		Ext.get("annex").dom.value = "";
	}
	// ---------树的click事
	function clickTree(node) {
		watchbt.setVisible(true);
		if (node.id == "root") {
			// 根节点
			currentType = 'add';
			currentNode = node;
			cNode = node;
			initForm();
			myaddpanel.setTitle("在【" + node.text + "】下增加");
			Ext.get("fboilerId").dom.value = "0";
		} else {
			currentNode = node;
			cNode = node;
			currentType = "update";
			initForm();
			myaddpanel.setTitle("修改【" + node.text + "】基本信息");
			Ext.Ajax.request({
				url : 'security/getInfoBoilerByCode.action',
				params : {
					id : node.id
				},
				method : 'post',
				waitMsg : '正在加载数据...',
				success : function(result, request) {
					var obj = eval('(' + result.responseText + ')');
					Ext.get("boilerId").dom.value = obj.boilerId;
					FBoilerId.setValue(obj.FBoilerId);
					Ext.get("boilerName").dom.value = obj.boilerName;
					if (obj.boilerType != null) {
						Ext.get("boilerType").dom.value = obj.boilerType;
					}
					if (obj.manufacturer != null) {
						Ext.get("manufacturer").dom.value = obj.manufacturer;
					}
					if (obj.boilerNumber != null) {
						Ext.get("boilerNumber").dom.value = obj.boilerNumber;
					}
					if (obj.annex != null) {
						Ext.get("annex").dom.value = obj.annex;
					}
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('错误', '操作失败!');
				}
			});
		};

	};

	// ID
	var boilerId = new Ext.form.Hidden({
		id : "boilerId",
		name : 'boiler.boilerId'
	})
	var FBoilerId = new Ext.form.Hidden({
		id : "FBoilerId",
		name : 'boiler.FBoilerId'
	})

	var boilerName = new Ext.form.TextField({
		id : "boilerName",
		fieldLabel : '设备名称',
		name : 'boiler.boilerName',
		anchor : "80%",
		allowBlank : false
	})

	var boilerType = new Ext.form.TextField({
		id : "boilerType",
		fieldLabel : '设备型号',
		name : 'boiler.boilerType',
		anchor : "80%"
	})

	var manufacturer = new Ext.form.TextField({
		id : "manufacturer",
		fieldLabel : '生产厂家',
		name : 'boiler.manufacturer',
		anchor : "80%"
	})
	var boilerNumber = new Ext.form.NumberField({
		id : "boilerNumber",
		fieldLabel : '数量',
		name : 'boiler.boilerNumber',
		anchor : "80%"
	})
	var annex = {
		id : "annex",
		xtype : "fileuploadfield",
		fieldLabel : '具体参数',
		readOnly : true,
		value : '具体参数，附件形式上传',
		anchor : "95%",
		height : 33,
		name : "annex",
		buttonCfg : {
			text : '浏览...',
			iconCls : 'upload-icon'
		}
	}
	var addbt = new Ext.Button({
		id : "add",
		text : '新增',
		iconCls : 'add',
		handler : addInfo
	})
	var deletebt = new Ext.Button({
		text : '删除',
		iconCls : 'delete',
		handler : deleteInfo
	})
	var savebt = new Ext.Button({
		text : '保存',
		iconCls : 'save',
		handler : saveInfo
	})
	var watchbt = new Ext.Button({
		text : '查看附件',
		handler : function() {
			// modified by liuyi 20100426
			if (Ext.get('annex').dom.value == null
					|| Ext.get('annex').dom.value == '') {
				Ext.Msg.alert('提示', '无附件可查看!');
				return;
			}
			window.open(Ext.get('annex').dom.value);
		}
	})
	// 方法
	function addInfo() {
		node = currentNode;
		currentType = "add";
		initForm();
		watchbt.setVisible(false);
		myaddpanel.setTitle("在【" + node.text + "】下增加");
		if (node.id == 'root')
			FBoilerId.setValue(0);
		else
			FBoilerId.setValue(node.id)
	}
	function deleteInfo() {
		if (cNode == "") {
			Ext.Msg.alert("提示", "请先选择节点!");
		} else if (cNode.id == 'root') {
			Ext.Msg.alert("提示", "根节点不可删除!");
		} else {
			Ext.Msg.confirm("删除", "是否确定删除" + currentNode.text + "吗？", function(
					buttonobj) {
				if (buttonobj == "yes") {
					Ext.Ajax.request({
						url : 'security/deleteBoilerInfoById.action',
						params : {
							id : currentNode.id
						},
						method : 'post',
						waitMsg : '正在加载数据...',
						success : function(result, request) {
							var json = eval('(' + result.responseText + ')');
							Ext.Msg.alert("注意", json.msg);
							if (json.msg == '删除成功！') {
								var rnode = currentNode.parentNode;
								rnode.removeChild(currentNode);
								currentNode.id = "root";
								currentNode.text = "锅炉设备树";
								clickTree(currentNode);
								root.select();
							}
						}
					});
				}

			});
		}
	}
	function saveInfo() {
		if (boilerName.getValue() == null || boilerName.getValue() == '') {
			Ext.Msg.alert('提示', '请选择输入锅炉设备名称！');
			return;
		}
		myaddpanel.form.submit({
			method : 'POST',
			params : {
				filePath : Ext.get('annex').dom.value,
				method : currentType,
				flag : currentNode.id
			},
			//url : 'security/addOrUpdateBoilerInfo.action?method=' + currentType,
			url : 'security/addOrUpdateBoilerInfo.action',
			success : function(form, action) {
				var o = eval("(" + action.response.responseText + ")");
				Ext.Msg.alert("注意", o.msg);
				if (currentType == "add") {
					if (currentNode.isLeaf()) {
						currentNode.parentNode.reload();
						mytree.getRootNode().reload();
						myaddpanel.getForm().reset();
					} else {
						mytree.getRootNode().reload();
						myaddpanel.getForm().reset();
					}
				} else {
					if (o.msg == "修改成功！") {
						var cnode = mytree.getNodeById(currentNode.id);
						cnode.setText(currentNode.id + " "
								+ Ext.get("boilerName").dom.value);
						 mytree.getNodeById(cnode.parentNode.id).reload();
						 myaddpanel.getForm().reset();
						Ext.Msg.alert("注意", o.msg);
					}
				}
			},
			faliue : function() {
				Ext.Msg.alert('错误', '出现未知错误.');
			}
		});
	}

	// ------------

	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'left',
		title : '在目录【锅炉设备树】下增加',
		bodyStyle : 'padding:30px',
		fileUpload : true,
		items : [{
			xtype : 'fieldset',
			id : 'formSet',
			title : '详细信息',
			labelAlign : 'right',
			labelWidth : 80,
			// defaultType : 'textfield',
			autoHeight : true,
			bodyStyle : Ext.isIE
					? 'padding:0 0 5px 15px;'
					: 'padding:10px 15px;',
			border : true,
			// style : {
			// "margin-left" : "10px",
			// "margin-right" : Ext.isIE6 ? (Ext.isStrict
			// ? "-10px"
			// : "-13px") : "0"
			// },
			layout : 'column',
			defaults : {
				layout : 'form'
			},
			items : [{
				columnWidth : 1,
				items : [boilerId, FBoilerId, boilerName, boilerType,
						manufacturer, boilerNumber]
			}, {
				columnWidth : .7,
				items : [annex]
			}, {
				columnWidth : .3,
				items : [watchbt]
			}]
		}],
		tbar : [addbt, {
			xtype : "tbseparator"
		}, deletebt, {
			xtype : "tbseparator"
		}, savebt]

	});
	var right = new Ext.Panel({
		id : 'right',
		region : "center",
		autoScroll : true,
		containerScroll : true,
		layout : 'fit',
		items : [myaddpanel]
	});

	var viewport = new Ext.Viewport({
		layout : 'border',
		items : [{
			region : 'west',
			split : true,
			width : 250,
			layout : 'fit',
			minSize : 175,
			maxSize : 600,
			margins : '0 0 0 0',
			collapsible : true,
			border : false,
			autoScroll : true,
			items : [mytree]
		}, right// 初始标签页
		]
	});
	watchbt.setVisible(false);

});