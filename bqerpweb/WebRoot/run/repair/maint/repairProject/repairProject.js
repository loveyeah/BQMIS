Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var currentType = 'add';
	var currentNode = new Object();
	var cNode = "";

	var root = new Ext.tree.AsyncTreeNode({
		id : "root",
		text : "检修项目"
	});

	currentNode = root;
	var mytree = new Ext.tree.TreePanel({
		renderTo : "treepanel",
//		autoWidth : true,
		autoHeight:true,
		autoScroll:true,//add by wpzhu 20100531
		root : root,
		border : false,
		loader : new Ext.tree.TreeLoader({
			url : "manageplan/findRepairTreeList.action",
			baseParams : {
				id : 'root'
			}
		})
	});

	root.expand();
	mytree.on("click", clickTree, this);
	mytree.on('beforeload', function(node) {
		mytree.loader.dataUrl = 'manageplan/findRepairTreeList.action?id='
				+ node.id;
	}, this);

	function initForm() {
		Ext.get("repairProjectId").dom.value = "";
		Ext.get("repairProjectName").dom.value = "";
		Ext.get("workingCharge").dom.value = "";
		Ext.get("workingMenbers").dom.value = "";
		Ext.get("workingTime").dom.value = "";
		Ext.get("acceptanceFirst").dom.value = "";
		Ext.get("acceptanceSecond").dom.value = "";
		Ext.get("acceptanceThird").dom.value = "";
	}
	// ---------树的click事
	function clickTree(node) {
		if (node.id == "root") {
			// 根节点
			myaddpanel.hide();
			currentType = 'add';
			currentNode = node;
			cNode = node;
			initForm();
			myaddpanel.setTitle("在【" + node.text + "】下增加");
			Ext.get("FProjectId").dom.value = "0";
		} else {
			myaddpanel.show();
			currentNode = node;
			cNode = node;
			currentType = "update";
			initForm();
			myaddpanel.setTitle("修改【" + node.text + "】基本信息");
			Ext.Ajax.request({
				url : 'manageplan/getRepairTreeInfo.action',
				params : {
					id : node.id
				},
				method : 'post',
				waitMsg : '正在加载数据...',
				success : function(result, request) {
					var obj = eval('(' + result.responseText + ')');
					Ext.get("repairProjectId").dom.value = obj[0];
					FProjectId.setValue(obj[1]);
					Ext.get("repairProjectName").dom.value = obj[3];
					if (obj[5] != null) {
						Ext.get("workingCharge").dom.value = obj[5];
					}
					if (obj[6] != null) {
						Ext.get("workingMenbers").dom.value = obj[6];
					}
					if (obj[7] != null) {
						Ext.get("workingTime").dom.value = obj[7];
					}
					if (obj[9] != null) {
						Ext.get("acceptanceFirst").dom.value = obj[9];
					}
					if (obj[11] != null) {
						Ext.get("acceptanceSecond").dom.value = obj[11];
					}
					if (obj[13] != null) {
						Ext.get("acceptanceThird").dom.value = obj[13];
					}
					if (obj[14] != null) {
						Ext.get("acceptanceLevel").dom.value = obj[14];
					}
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('错误', '操作失败!');
				}
			});
		};
	};

	var repairProjectId = new Ext.form.Hidden({
		id : "repairProjectId",
		name : 'repair.repairProjectId'
	})
	var FProjectId = new Ext.form.Hidden({
		id : "FProjectId",
		name : 'repair.FProjectId'
	})
	var workingMenbers = new Ext.form.TextField({
		id : "workingMenbers",
		fieldLabel : '工作成员',
		name : 'repair.workingMenbers',
		anchor : "80%",
		allowBlank : true
	})
	var repairProjectName = new Ext.form.TextField({
		id : "repairProjectName",
		fieldLabel : '检修项目名称',
		name : 'repair.repairProjectName',
		anchor : "80%",
		allowBlank : false
	})
	var workingTime = new Ext.form.TextField({
		id : 'workingTime',
		fieldLabel : "工作时间",
		name : 'repair.workingTime',
		style : 'cursor:pointer',
		anchor : "80%"
	});
	var workingCharge = {
		fieldLabel : '工作负责人',
		name : 'workingCharge',
		xtype : 'combo',
		id : 'workingCharge',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'repair.workingCharge',
		allowBlank : true,
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '灞桥电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (typeof(emp) != "undefined") {
				Ext.getCmp('workingCharge').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('workingCharge'), emp.workerName);
			}
		}
	};
	var acceptanceFirst = {
		fieldLabel : '一级验收人',
		name : 'acceptanceFirst',
		xtype : 'combo',
		id : 'acceptanceFirst',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'repair.acceptanceFirst',
		allowBlank : true,
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '灞桥电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (typeof(emp) != "undefined") {
				Ext.getCmp('acceptanceFirst').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('acceptanceFirst'), emp.workerName);
			}
		}
	};
	var acceptanceSecond = {
		fieldLabel : '二级验收人',
		name : 'acceptanceSecond',
		xtype : 'combo',
		id : 'acceptanceSecond',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'repair.acceptanceSecond',
		allowBlank : true,
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '灞桥电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (typeof(emp) != "undefined") {
				Ext.getCmp('acceptanceSecond').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('acceptanceSecond'), emp.workerName);
			}
		}
	};
	var acceptanceThird = {
		fieldLabel : '三级验收人',
		name : 'acceptanceThird',
		xtype : 'combo',
		id : 'acceptanceThird',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'repair.acceptanceThird',
		allowBlank : true,
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '灞桥电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (typeof(emp) != "undefined") {
				Ext.getCmp('acceptanceThird').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('acceptanceThird'), emp.workerName);
			}
		}
	};
	var acceptanceLevel = new Ext.form.ComboBox({
		fieldLabel : '验收级别',
		store : new Ext.data.SimpleStore({
			fields : ['value', 'text'],
			data : [['2', '二级'], ['3', '三级']]
		}),
		id : 'acceptanceLevel',
		name : 'acceptanceLevel',
		valueField : "value",
		displayField : "text",
		value : '2',
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'repair.acceptanceLevel',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		anchor : "80%"
	});

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
	var btnCancel = new Ext.Button({
		text : '取消',
		iconCls : 'cancer',
		handler : function() {
			clickTree(currentNode);
		}
	});

	function addInfo() {
		myaddpanel.show();
		node = currentNode;
		currentType = "add";
		initForm();
		myaddpanel.setTitle("在【" + node.text + "】下增加");
		if (node.id == 'root')
			FProjectId.setValue(0);
		else
			FProjectId.setValue(node.id)
	}
	function deleteInfo() {
		if (cNode == "") {
			Ext.Msg.alert("提示", "请先选择节点!");
		} else if (cNode.id == 'root') {
			Ext.Msg.alert("提示", "根节点不可删除!");
		} else {
			Ext.Msg.confirm("删除", "是否确定删除所选记录吗？", function(buttonobj) {
				if (buttonobj == "yes") {
					Ext.Ajax.request({
						url : 'manageplan/deleteRepairMaintRecord.action',
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
								currentNode.text = "检修项目";
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
		if (repairProjectName.getValue() == null
				|| repairProjectName.getValue() == '') {
			Ext.Msg.alert('提示', '请选择输入检修项目名称！');
			return;
		}
		form.form.submit({
			method : 'POST',
			params : {
				method : currentType
			},
			url : 'manageplan/addAndUpdateRepairRecord.action',
			success : function(form, action) {
				var o = eval("(" + action.response.responseText + ")");
				Ext.Msg.alert("注意", o.msg);
				if (currentType == "add") {
					if (currentNode.isLeaf()) {
					pCode=currentNode.parentNode;
					clickTree(pCode);
					mytree.getNodeById(pCode.id).reload();
//						currentNode.parentNode.reload();
						mytree.getRootNode().reload();
					} else {
						mytree.getRootNode().reload();
					}
				} else {
					if (o.msg == "修改成功！") {
						var cnode = mytree.getNodeById(currentNode.id);
						cnode.setText(currentNode.id + " "
								+ Ext.get("repairProjectName").dom.value);
						mytree.getNodeById(cnode.parentNode.id).reload();
						Ext.Msg.alert("注意", o.msg);
					}
				}
			},
			faliue : function() {
				Ext.Msg.alert('错误', '出现未知错误.');
			}
		});
	}

	var myaddpanel = new Ext.form.FieldSet({
		title : '检修项目信息',
		height : '100%',
		layout : 'form',
		buttonAlign : 'center',
		buttons : [savebt, btnCancel],
		items : [{
			border : false,
			layout : 'column',
			items : [{
				columnWidth : .5,
				layout : 'form',
				border : false,
				labelWidth : 100,
				items : [repairProjectId, FProjectId, repairProjectName,
						workingCharge]
			}, {
				columnWidth : .5,
				layout : 'form',
				border : false,
				labelWidth : 100,
				items : [workingMenbers, workingTime]
			}]
		}, {
			border : false,
			layout : 'column',
			items : [{
				columnWidth : .5,
				layout : 'form',
				border : false,
				labelWidth : 100,
				items : [acceptanceFirst, acceptanceLevel]
			}, {
				columnWidth : .5,
				layout : 'form',
				border : false,
				labelWidth : 100,
				items : [acceptanceSecond, acceptanceThird]
			}]
		}]
	});

	var form = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		labelWidth : 70,
		autoHeight : true,
		border : false,
		tbar : [addbt, {
			xtype : "tbseparator"
		}, deletebt, {
			xtype : "tbseparator"
		}],
		items : [myaddpanel]

	});
	var panel = new Ext.Panel({
		border : false,
		collapsible : false,
		title : '检修项目维护',
		items : [form]
	});

	var layout = new Ext.Viewport({
		layout : "border",
		border : false,
		items : [{
			region : "center",
			layout : 'fit',
			collapsible : true,
			split : true,
			margins : '0 0 0 0',
			// 注入表格
			items : [panel]
		}, {
			title : "检修项目",
			autoScroll: true,
			region : 'west',
			margins : '0 0 0 0',
			split : true,
			collapsible : true,
			titleCollapse : true,
			width : 200,
			layoutConfig : {
				animate : true
			},
			layout : 'fit',
			items : [mytree]
		}]
	})
	myaddpanel.hide();
});