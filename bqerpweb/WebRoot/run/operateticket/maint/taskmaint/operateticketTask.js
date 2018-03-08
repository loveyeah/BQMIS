Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	// 自定义的节点类型
	function CustomNode(node) {
		// 当前节点
		this._currentNode = node;
		// 当前父节点
		this._parentNode = node.parentNode;
		if (!this._parentNode) {
			this._parentNode = node;
		}

		if (typeof CustomNode._initialized == "undefined") {
			CustomNode._initialized = true;
			// 设置当前节点
			CustomNode.prototype.setCurrentNode = function(argNode) {
				this._currentNode = argNode;
				this._parentNode = argNode.parentNode;
				if (!this._parentNode) {
					this._parentNode = argNode;
				}
			}

			// 在当前节点增加一个节点
			CustomNode.prototype.addNode = function() {
				if (!this._currentNode.isRoot
						&& this._currentNode.attributes.code.length >= 13) {
					Ext.Msg.alert(Constants.SYS_REMIND_MSG, '在此层不能增加节点');
					return;
				}
				formPanel.getForm().reset();
				
				formPanel.getForm().loadRecord({
					data : {
						parentOperateTaskId : this._currentNode.id
					}
				});
				 

				// 设置叶子节点显示
				if (this._currentNode.isLeaf()) {
					Ext.get("hiddenIsTask").dom.value = "Y";
				} else {
					if (this._currentNode.childNodes
							&& this._currentNode.childNodes.length > 0) {
						Ext.get("hiddenIsTask").dom.value = "N";
					} else {
						Ext.get("hiddenIsTask").dom.value = "Y";
					}

				}

				// 设置编号为'自动生成'
				Ext.get('code').dom.value = Constants.AUTO_CREATE;
			}

			// 修改当前节点
			CustomNode.prototype.editNode = function() {
				formPanel.getForm().reset();

				if (this._currentNode.isRoot) {
					// 树形根节点不能修改
					return false;
				}

				Data.prototype = this._currentNode.attributes;

				var record = {
					data : new Data(this._parentNode)
				};
				// 显示该行记录
				formPanel.getForm().loadRecord(record);

				// 设置叶子节点显示
				if (this._currentNode.isLeaf()) {
					Ext.get("hiddenIsTask").dom.value = "Y";
				} else {
					if (this._currentNode.childNodes
							&& this._currentNode.childNodes.length > 0) {
						Ext.get("hiddenIsTask").dom.value = "N";
					} else {
						Ext.get("hiddenIsTask").dom.value = "Y";
					}
				}
			}

			// 删除节点
			CustomNode.prototype.deleteNode = function() {
				var blnLeaf = this._currentNode.isLeaf();
				if (this._currentNode.isRoot) {
					// 树形根节点不能修改
					Ext.Msg.alert(Constants.SYS_REMIND_MSG, "树形根节点不能修改！");
					return false;
				} else if (!blnLeaf && this._currentNode.childNodes
						&& this._currentNode.childNodes.length > 0) {
					Ext.Msg.alert(Constants.SYS_REMIND_MSG, "不能删除有子节点的节点！");
					return false;
				}

				Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants
						.DelMsgById(this._currentNode.text),
						function(buttonobj) {
							if (buttonobj == "yes") {
								Ext.Ajax.request({
									method : Constants.POST,
									url : 'opticket/deleteOpTickTask.action',
									success : function(action) {
										Ext.Msg.alert(Constants.BTN_DELETE,
												Constants.DEL_SUCCESS)
										// 树形同步显示
										customNode._parentNode.reload();
										// 设置当前节点
										customNode.setCurrentNode(rootNode);
										customNode.editNode();
									},
									failure : function() {
										Ext.Msg.alert(Constants.ERROR,
												Constants.DEL_ERROR);
									},
									params : {
										id : customNode._currentNode.id
									}
								});
							}
						});
			}

			CustomNode.prototype.test = function(obj) {
				var testObj = this;
				var str = '';
				if (obj) {
					testObj = obj;
					for (var prop in testObj) {
						if (typeof testObj[prop] != 'function') {
							str += prop + ': ' + testObj[prop] + '\n';
						}
					}
				} else {
					for (var prop in testObj) {
						if (typeof testObj[prop] != 'function') {
							str += prop + ': id=' + testObj[prop].id + ' text='
									+ testObj[prop].text + '\n';
						}
					}
				}
			}

			// 增加树节点
			CustomNode.prototype.save = function() {
				if (!formPanel.getForm().isValid()) {
					return false;
				}

				var strOrderBy = Ext.get("openType").dom.value;

				Ext.get("hiddenDisplayNo").dom.value = strOrderBy;

				var myurl = "";
				var blnEdit = Ext.get("operateTaskId").dom.value;
				if (!blnEdit) {
					myurl = "opticket/addOpTickTask.action";
				} else {
					myurl = "opticket/updateOpTickTask.action";
				}

				var tempCurrent = this._currentNode;
				var tempParent = this._parentNode;
				formPanel.getForm().submit({
					method : Constants.POST,
					waitMsg : Constants.DATA_SAVING,
					url : myurl,
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert(Constants.SYS_REMIND_MSG, o.msg);

						if (!blnEdit) {
							customNode._parentNode = customNode._currentNode;
							tempParent = customNode._parentNode;
						}
						tempParent.on("load", function() {
							setNodeAfterLoad(blnEdit)
						});
						// 树形同步显示
						tempParent.reload();
					},
					faliue : function() {
						Ext.Msg.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
					}
				});
			}

			// 加载子节点后设置当前的节点
			function setNodeAfterLoad(blnEdit) {
				// 删除节点监听
				customNode._parentNode.un("load", setNodeAfterLoad);
				var temp = customNode
						._findNodeByCode(Ext.get("operateTaskId").dom.value);

				// 设置当前节点
				customNode.setCurrentNode(temp);
				customNode.editNode();
			}

			// 通过节点的Code查找节点
			CustomNode.prototype._findNodeByCode = function(argCode) {
				var lstNodes = this._parentNode.childNodes;
				// 子节点的长度
				var len = lstNodes ? lstNodes.length : 0;

				var result = null;
				for (var intCnt = 0; intCnt < len; intCnt++) {
					if (lstNodes[intCnt].id == argCode) {
						result = lstNodes[intCnt];
						break;
					}
				}
				if (!result) {
					// 如果找不到该节点, 则返回父节点
					result = this._parentNode;
				}

				return result;
			}
		}
	}

	function Data(argParent) {
		// 操作票ID
		this.operateTaskId = this.id;
		// 操作票名称
		this.operateTaskName = this.text;
		// 操作任务说明
		if (this.explain == "null") {
			this.operateTaskExplain = "";
		} else {
			this.operateTaskExplain = this.explain;
		}

		// 父操作票
		if (!this.isRoot) {
			// 父操作票ID
			this.parentOperateTaskId = argParent.id;
		}
	};

	// ↓↓*****************主画面*****************↓↓//

	// **********定义treePanel********** //
	var rootNode = new Ext.tree.AsyncTreeNode({
		text : '操作任务维护',
		id : '-1',
		isRoot : true
	});

	var customNode = new CustomNode(rootNode);

	var treePanel = new Ext.tree.TreePanel({
		renderTo : "treePanel",
		region : 'center',
		animate : true,
		autoHeight : true,
		root : rootNode,
		border : false,
		rootVisible : false,
		loader : new Ext.tree.TreeLoader({
			dataUrl : "opticket/getOpTickTaskTreeNode.action"
		})
	});

	treePanel.on("click", treeClick);
	treePanel.render();
	rootNode.expand();

	function treeClick(node, e) {
		if (node.isLeaf()) {
			e.stopEvent();
		}  
		// 设置为现在的节点
		customNode.setCurrentNode(node);  
		// 显示该节点记录
		customNode.editNode();
		// node.toggle(); 
		if(node.attributes.code.length == 10)
		{
			Ext.getCmp("importByStand").setVisible(true);
			Ext.getCmp("ticketNos").setVisible(true);
			
		}
		else
		{ 
			Ext.getCmp("importByStand").setVisible(false);
			Ext.getCmp("ticketNos").setVisible(false);
		}
		
	}

	// **********定义formPanel********** //
	var wd = 300;

	var parentOperateTaskId = {
		id : "parentOperateTaskId",
		//xtype : "hidden",
		xtype:'textfield',
		fieldLabel : 'ID',
		readOnly : true,
		name : 'opticketTask.parentOperateTaskId',
		value : '-1'
	}

	var operateTaskCode = {
		id : "code",
		xtype : "textfield",
		fieldLabel : '编号',
		width : wd,
		name : 'opticketTask.operateTaskCode',
		readOnly : true,
		value : Constants.AUTO_CREATE
	}

	var operateTaskId = {
		id : "operateTaskId",
		xtype : "hidden",
		name : 'opticketTask.operateTaskId'
	}

	var operateTaskName = {
		id : "operateTaskName",
		xtype : "textarea",
		fieldLabel : '操作任务名称<font color="red">*</font>',
		allowBlank : false,
		width : wd,
		name : 'opticketTask.operateTaskName',

		maxLength : 255
	};

	var operateTaskExplain = {
		id : "description",
		xtype : "textarea",
		fieldLabel : '备注',
		width : wd,
		heigth : 120,
		name : 'opticketTask.operateTaskExplain',
		maxLength : 255
	}

	var isTask = {
		id : "hiddenIsTask",
		xtype : 'hidden',
		name : 'opticketTask.isTask'
	}

	var displayNo = {
		id : "openType",
		xtype : "numberfield",
		fieldLabel : '显示顺序',
		width : wd,
		maxLength : 10
	}

	var hiddenDisplayNo = {
		id : "hiddenDisplayNo",
		xtype : "hidden",
		name : 'opticketTask.displayNo'
	}

	var fieldSet = {
		xtype : 'fieldset',
		border : true,
		title : '操作任务登记',
		width : 500,
		labelAlign : 'left',
		labelWidth : 150,
		buttonAlign : 'center',
		defaults : {
			width : 250
		},
		autoHeight : true,
		bodyStyle : Ext.isIE ? 'padding:0 0 5px 15px;' : 'padding:10px 15px;',
		style : {
			"margin-top" : "20px",
			"margin-left" : "10px",
			"margin-right" : Ext.isIE6
					? (Ext.isStrict ? "-10px" : "-13px")
					: "0"
		},
		items : [parentOperateTaskId, operateTaskCode, operateTaskId,
				operateTaskName, operateTaskExplain, displayNo,
				hiddenDisplayNo, isTask],
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : getFun("save")
		}, {
			text : Constants.BTN_CANCEL,
			iconCls : Constants.CLS_CANCEL,
			handler : function() {
				// customNode.setCurrentNode(rootNode);
				customNode.setCurrentNode(customNode._currentNode);
				customNode.editNode();
			}
		}]
	}

	var formPanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		items : [fieldSet],
		tbar : [{
			text : "新增操作票(卡)",
			iconCls : Constants.CLS_ADD,
			handler : getFun("addNode")
		}, {
			text : "删除操作票(卡)",
			iconCls : Constants.CLS_DELETE,
			handler : getFun("deleteNode")
		},'->',{
			xtype:'textfield',
			id:'ticketNos',
			readOnly:true,
			hidden:true,
			style:{cursor:'hand'},
			emptyText:'单击选择标准票',
			width:200 
		}, {
			text : "由标准操作票导入",
			iconCls : 'save',
			hidden:true,
			id:'importByStand',
			handler : function()
			{  
				if(Ext.getCmp("ticketNos").getValue() == '') 
				{
					Ext.Msg.alert('提示','请选择标准操作票!');
					return ;   
				}
				if(customNode._currentNode.attributes.id  == -1)
				{
					Ext.Msg.alert('提示','请选择操作任务的上一层目录!!');
					return ;
				}
			    Ext.Msg.wait("正在生成操作任务及步骤,请等待...");
				Ext.Ajax.request({
					method : 'post',
					url : 'opticket/addOpTickTask.action',
					params:{
						addByStand:'Y',
						ticketNos:Ext.getCmp("ticketNos").getValue(),
						parentId: customNode._currentNode.attributes.id 
					},
					success : function(action) {
						Ext.getCmp("ticketNos").setValue("");
						Ext.Msg.alert('提示','操作成功')
						// 树形同步显示
						customNode._parentNode.reload();
						// 设置当前节点
						customNode.setCurrentNode(rootNode);
						customNode.editNode();
					},
					failure : function() {
						Ext.Msg.alert('提示','操作失败!');
					} 
				});
			}
		}]
	});

	function getFun(argName) {
		if (argName == "addNode") {
			return function() {
				customNode.addNode();
			}
		} else if (argName == "editNode") {
			return function() {
				customNode.editNode();
			}
		} else if (argName == "deleteNode") {
			return function() {
				customNode.deleteNode();
			}
		} else if (argName == "save") {
			return function() {
				// 操作任务名称不能是空格
				if (!/\S+/.test(Ext.get('operateTaskName').dom.value)) {
					Ext.Msg.alert(Constants.SYS_REMIND_MSG, '操作任务名称不能是空格!');
					return;
				}
				customNode.save();
			}
		}
	}

	// ********主画面******* //
	var panelLeft = new Ext.Panel({
		region : 'west',
		layout : 'fit',
		title : '操作任务树',
		width : 180,
		autoScroll : true,
		containerScroll : true,
		collapsible : true,
		split : true,
		items : [treePanel]
	});

	var panelRight = new Ext.Panel({
		region : "center",
		layout : 'fit',
		border : true,
		items : [formPanel]
	});

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [panelLeft, panelRight]
	});
	// ↑↑*****************主画面*****************↑↑//
	customNode.editNode();
	
	Ext.getCmp("ticketNos").getEl().on("click",function(){
		var returnValue = window
				.showModalDialog(
						'../../business/operate/query/standOperateticket.jsp',
						'multiple',
						'dialogWidth=800px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');
		if (typeof(returnValue) != "undefined") {
			Ext.getCmp("ticketNos").setValue(returnValue);  
		}
		
	});
})