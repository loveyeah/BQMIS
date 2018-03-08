Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

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
	return s;
}

Ext.onReady(function() {

	var fillBy;
	var sessWorname;
	var sessWorcode;
	
	var currentType = 'add';
	var currentNode = new Object();
	var cNode = "";
	var addFaCode = "";
	
	var root = new Ext.tree.AsyncTreeNode({
		id : "root",
		text : "指标报表类型"
	});

	currentNode = root;
	var mytree = new Ext.tree.TreePanel({
		renderTo : "treepanel",
		 autoHeight : true,
		 root : root,
		 border : false,
		loader : new Ext.tree.TreeLoader({
			url : "manager/getReportTreeList.action",
			baseParams : {
				id : 'root'
			}
		})
	});

	root.expand();
	mytree.on("click", clickTree, this);
	mytree.on('beforeload', function(node) {
		mytree.loader.dataUrl = 'manager/getReportTreeList.action?id='
				+ node.id;
	}, this);

	function initForm() {
		Ext.get("reportId").dom.value = "";
		Ext.get("reportCode").dom.value = "";
		Ext.get("reportName").dom.value = "";
		Ext.get("faReprotCode").dom.value = "";
	}
	// ---------树的click事
	function clickTree(node,event) {
		
		 event.stopEvent();
		 
		if (node.id == "root") {
			// 根节点
			myaddpanel.hide();
			currentType = 'add';
			currentNode = node;
			cNode = node;
			initForm();
			myaddpanel.setTitle("在【" + node.text + "】下增加");
			Ext.get("faReprotCode").dom.value = "0";
		} else {
			myaddpanel.show();
			currentNode = node;
			cNode = node;
			currentType = "update";
			initForm();
			myaddpanel.setTitle("修改【" + node.text + "】基本信息");
			Ext.Ajax.request({
				url : 'manager/getReportTreeInfo.action',
				params : {
					id : node.id
				},
				method : 'post',
				waitMsg : '正在加载数据...',
				success : function(result, request) {
					var obj = eval('(' + result.responseText + ')');
					Ext.get("reportId").dom.value = obj[0];
					if(obj[1] != null){
						Ext.get('reportCode').dom.value = obj[1];
					}
					if (obj[2] != null) {
						Ext.get("reportName").dom.value = obj[2];
					}
					if (obj[3] != null) {
						Ext.get("faReprotCode").dom.value = obj[3];
					}
					addFaCode = obj[1];
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('错误', '操作失败!');
				}
			})
		}
	};

	var reportId = new Ext.form.Hidden({
		id : "reportId",
		name : 'report.reportId'
	})
	var faReprotCode = new Ext.form.TextField({
				id : "faReprotCode",
				fieldLabel : '父编码',
				disabled : true,
				anchor : "80%"
			});

	var faReprotName = new Ext.form.TextField({
				id : "faReprotName",
				fieldLabel : '父名称',
				disabled : true,
				anchor : "80%"
			});
			
	var reportCode = new Ext.form.TextField({
		id : "reportCode",
		fieldLabel : '编码',
		anchor : "80%",
		readOnly : true,
		name : 'report.reportCode'
	})
	var reportName = new Ext.form.TextField({
		id : "reportName",
		fieldLabel : '名称',
		name : 'report.reportName',
		anchor : "80%",
		allowBlank : true
	})
	var lastModifierName = new Ext.form.TextField({
		id : 'lastModifierName',
		fieldLabel : '修改人',
		name : 'lastModifierName',
		anchor : "80%",
		readOnly : true

	});
	var lastModifierBy = new Ext.form.Hidden({
		id : "lastModifierBy",
		name : 'report.lastModifierBy'
	});
	var lastModifierDate = new Ext.form.TextField({
		id : 'lastModifierDate',
		fieldLabel : '修改时间',
		name : 'report.lastModifierDate',
		readOnly : true,
		value : getDate(),
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
			initForm();
		}
	});

	function addInfo() {
		node = currentNode;
		if (node.id == 'root') {
			Ext.Msg.alert("提示", "请先选择报表类型！");
		} else {
			myaddpanel.show();
			currentType = "add";
			initForm();
			myaddpanel.setTitle("在【" + node.text + "】下增加");
			faReprotCode.setValue(addFaCode);
			 //faReprotCode.setValue(node.id);
		}
		// faReprotCode.setValue("0");
		// else
		// faReprotCode.setValue(node.id);
	}
	
	function deleteInfo() {
		if (cNode == "") {
			Ext.Msg.alert("提示", "请先选择节点!");
		} else if (cNode.id == 'root') {
			Ext.Msg.alert("提示", "根节点不可删除!");
		}else if(reportCode.getValue() == '1'||reportCode.getValue() == '2'||reportCode.getValue() == '3'||reportCode.getValue() == '4')
		{
			Ext.Msg.alert("提示", "此节点不可删除!");
		}
		else {
			Ext.Msg.confirm("删除", "是否确定删除所选记录吗？", function(buttonobj) {
				if (buttonobj == "yes") {
					Ext.Ajax.request({
						url : 'manager/deleteReport.action',
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
								initForm();
							//	currentNode.id = "root";
								currentNode.text = "指标报表类型";
								//clickTree(currentNode,event);
								root.select();
							}
						}
					});
				}

			});
		}
	}
	function saveInfo() {
		var type = "";
		if (reportName.getValue() == null
				|| reportName.getValue() == '') {
			Ext.Msg.alert('提示', '请输入指标报表类型名称！');
			return;
		}
		form.form.submit({
			method : 'POST',
			params : {
				method : currentType,
				faReprotCode : faReprotCode.getValue(),
				reportCode : reportCode.getValue()
			},
			url : 'manager/addAndUpdateReport.action',
			success : function(form, action) {
				var o = eval("(" + action.response.responseText + ")");
				Ext.Msg.alert("注意", o.msg);
				if (currentType == "add") {
					if (currentNode.isLeaf()) {
						currentNode.parentNode.reload();
						//mytree.getRootNode().reload();
					} else {
						//mytree.getRootNode().reload();
					}
				} else {
					if (o.msg == "修改成功！") {
						var cnode = mytree.getNodeById(currentNode.id);
						cnode.setText(currentNode.id + " "+ Ext.get("reportName").dom.value);
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
		title : '指标报表类型信息',
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
				items : [reportId,faReprotCode, reportCode,reportName]
			}, {
				columnWidth : .5,
				layout : 'form',
				border : false,
				labelWidth : 100,
				items : [lastModifierBy,lastModifierName,lastModifierDate]
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
		title : '指标报表类型维护',
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
			title : "指标报表类型",
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
	
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result.workerCode) {
					// 设定默认工号
					sessWorcode = result.workerCode;
					sessWorname = result.workerName;
					loadData();
				}
			}
		});
	}
	function loadData() {
		lastModifierBy = sessWorcode;
		Ext.get('lastModifierName').dom.value = sessWorname;
	};
	
	getWorkCode();
	
	myaddpanel.hide();
});