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
		// 是否选中节点
		this.isCurrentClick = false;

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
				formPanel.getForm().reset();
				
				formPanel.getForm().loadRecord({
					data : {
						parentNodeName : this._currentNode.text,
						parentPressboardId : this._currentNode.id
					}
				});
				
				// 设置叶子节点显示
				isLeafRadio.setDisabled(false);
				Ext.getCmp('Y').setValue(true);
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
				isLeafRadio.setDisabled(false);
				if (this._currentNode.isLeaf()) {
					Ext.getCmp('Y').setValue(true);
				} else {
					// Bug修改: PB0096
					Ext.getCmp('N').setValue(true);
					
					// 设置叶子节点选项是否能编辑
					if (this._currentNode.loaded) {
						if (this._currentNode.childNodes
							&& this._currentNode.childNodes.length > 0) {
							isLeafRadio.setDisabled(true);
						}
					} else {
						this._currentNode.on('load', function(){
							if (this.childNodes
								&& this.childNodes.length > 0) {
								isLeafRadio.setDisabled(true);
							}
						});
						// 展开该节点
						this._currentNode.expand();
					}
				}
			}

			CustomNode.prototype.deleteNode = function() {
				var blnLeaf = this._currentNode.isLeaf();
				if (!this.isCurrentClick) {
					// 未选中节点时
					Ext.Msg.alert(Constants.SYS_REMIND_MSG, "请先选择要删除的节点！");
					return false;
				} else if (this._currentNode.isRoot) {
					// 树形根节点不能修改
					Ext.Msg.alert(Constants.SYS_REMIND_MSG, "树形根节点不能修改！");
					return false;
				} else if (!blnLeaf && this._currentNode.childNodes
						&& this._currentNode.childNodes.length > 0) {
					Ext.Msg.alert(Constants.SYS_REMIND_MSG, "不能删除有子节点的节点！");
					return false;
				}

				Ext.Msg.confirm(Constants.SYS_REMIND_MSG, "是否确定删除压板名称为" + this._currentNode.text + "的节点？",
					function(buttonobj) {
						if (buttonobj == "yes") {
							Ext.Ajax.request({
								method : Constants.POST,
								url : 'workticket/deletePressboard.action',
								success : function(action) {
									Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.DEL_SUCCESS)
									// 树形同步显示
									customNode._parentNode.reload();
									// 设置当前节点
									customNode.setCurrentNode(rootNode);
									customNode.editNode();
									// 未选中节点
									customNode.isCurrentClick = false;
								},
								failure : function() {
									Ext.Msg.alert(Constants.ERROR, Constants.DEL_ERROR);
								},
								params : {
									id : customNode._currentNode.id
								}
							});
						}
				});
			}
			
			CustomNode.prototype.save = function() {
				if (!formPanel.getForm().isValid()) {
					return false;
				}
				
				// start jincong 2008-12-27 编码验证
				if(!validateCode(Ext.get("pressboardCode").dom.value)) {
					return false;
				}
				// end jincong 2008-12-27 编码验证
				
				var strOrderBy = Ext.get("orderBy").dom.value;
				if (strOrderBy) {
					Ext.get("hiddenOrderBy").dom.value = strOrderBy
				}

				var myurl = "";
				var blnEdit = Ext.get("pressboardId").dom.value;
				if (!blnEdit) {
					myurl = "workticket/addPressboard.action";
				} else {
					myurl = "workticket/updatePressboard.action";
				}

				// 添加时父节点是否是叶子节点
				var isCurrentLeaf = this._currentNode.isLeaf();
				var tempCurrent = this._currentNode;
				var tempParent = this._parentNode;
				formPanel.getForm().submit({
					method : Constants.POST,
					waitMsg : Constants.DATA_SAVING,
					url : myurl,
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert(Constants.SYS_REMIND_MSG, o.msg);

						if (!blnEdit && !isCurrentLeaf) {
							customNode._parentNode = customNode._currentNode;
							tempParent = customNode._parentNode;
						}
						var nodeLoadHandler = function() {
							customNode._setNodeAfterLoad(blnEdit, isCurrentLeaf);
						}
						customNode._tempLoadHandler = nodeLoadHandler;
						tempParent.on("load", nodeLoadHandler);
						// 树形同步显示
						tempParent.reload();
					},
					faliue : function() {
						Ext.Msg.alert(Constants.ERROR,
								Constants.UNKNOWN_ERR);
					}
				});
			}
			
			// 加载子节点后设置当前的节点
			CustomNode.prototype._setNodeAfterLoad = function(blnEdit, isCurrentLeaf) {
				// 删除节点监听
				this._parentNode.un("load", this._tempLoadHandler);
				delete this._tempLoadHandler;
				
				var temp = null;
				if (!blnEdit) {
					// 显示父节点
					temp = isCurrentLeaf ? this._currentNode : this._parentNode;
				} else {
					temp = this._findNodeByCode(Ext.get("pressboardCode").dom.value);
				}
				
				// 设置当前节点
				this.setCurrentNode(temp);
				this.editNode();
			}

			// 通过节点的Code查找节点
			CustomNode.prototype._findNodeByCode = function(argCode) {
				var lstNodes = this._parentNode.childNodes;
				// 子节点的长度
				var len = lstNodes ? lstNodes.length : 0;
				
				var result = null;
				for (var intCnt = 0; intCnt < len; intCnt++) {
					if (lstNodes[intCnt].attributes.pressboardCode == argCode) {
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
		// 压板ID
		this.pressboardId = this.id;
		// 压板名称
		this.pressboardName = this.text;

		// 父压板
		if (this.isRoot) {
			this.parentNodeName = "根目录";
		} else {
			// 父压板ID
			this.parentPressboardId = argParent.id;
			// 父压板名称
			this.parentNodeName = argParent.text;
		}
	};

	// ----layout开始 ------
	var rootNode = new Ext.tree.AsyncTreeNode({
				text : '保护压板结构树',
				id : '0',
				isRoot : true
			});
	var customNode = new CustomNode(rootNode);

	function treeClick(node, e) {
		if (node.isLeaf()) {
			e.stopEvent();
		}

		// 选中节点
		customNode.isCurrentClick = true;
		// 设置为现在的节点
		customNode.setCurrentNode(node);
		// 显示该节点记录
		customNode.editNode();
		node.toggle();
	}

	var treePanel = new Ext.tree.TreePanel({
				renderTo : "treePanel",
				region : 'center',
				animate : true,
				autoHeight : true,
				root : rootNode,
				border : false,
				loader : new Ext.tree.TreeLoader({
							dataUrl : "workticket/getPressboardTreeNode.action"
						})
			});
	treePanel.on("click", treeClick);
	treePanel.render();
	rootNode.expand();

	// ============== 定义formPanel ===============
	var wd = 180;
	var parentPressboardId = {
		id : "parentPressboardId",
		xtype : "hidden",
		name : 'pressboard.parentPressboardId'
	}

	var parentPressboardName = {
		id : "parentNodeName",
		xtype : "textfield",
		fieldLabel : '父压板名称<font color="red">*</font>',
		width : wd,
		readOnly : true
	}

	var pressboardId = {
		id : "pressboardId",
		xtype : "hidden",
		name : 'pressboard.pressboardId'
	}

	var pressboardCode = {
		id : "pressboardCode",
		xtype : "textfield",
		fieldLabel : '压板编号<font color="red">*</font>',
		allowBlank : false,
		width : wd,
		name : 'pressboard.pressboardCode',
        maxLength : 20
	}

	var pressboardName = {
		id : "pressboardName",
		xtype : "textfield",
		fieldLabel : '压板名称<font color="red">*</font>',
		allowBlank : false,
		width : wd,
		name : 'pressboard.pressboardName',
        maxLength : 100
	};

	var orderByShow = {
		id : "orderBy",
		xtype : "numberfield",
		fieldLabel : '显示顺序',
		width : wd,
        maxLength : 10
	}

	var hiddenOrderBy = {
		id : "hiddenOrderBy",
		xtype : "hidden",
		name : 'pressboard.orderBy'
	}

	var modifyBy = {
		id : "modifyBy",
		xtype : "textfield",
		fieldLabel : '填写人',
		name : 'pressboard.modifyBy',
		width : wd,
		readOnly : true
	}

	var modifyDate = {
		id : "modifyDate",
		xtype : "textfield",
		fieldLabel : '填写日期',
		name : 'pressboard.modifyDate',
		width : wd,
		readOnly : true
	}

	var isLeafRadio = new Ext.form.Radio({
				id : 'Y',
				boxLabel : '是',
				name : 'pressboard.isLeaf',
				inputValue : 'Y',
				checked:true
			})

	var isLeafRadioNot = new Ext.form.Radio({
				id : 'N',
				boxLabel : '否',
				name : 'pressboard.isLeaf',
				inputValue : 'N'
			})

	var isLeafCheck = {
		id : 'isLeafCheck',
		layout : 'column',
		isFormField : true,
		name : 'pressboard.isLeaf',
		fieldLabel : "叶子节点标识<font color='red'>*</font>",
		border : false,
		items : [{
					columnWidth : .4,
					border : false,
					items : [isLeafRadio]
				}, {
					columnWidth : .4,
					border : false,
					items : [isLeafRadioNot]
				}]
	}

	var fieldSet = {
		xtype : 'fieldset',
		border : false,
		width : 500,
		labelAlign : 'right',
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
		items : [parentPressboardId, parentPressboardName, pressboardId,
				pressboardCode, pressboardName, orderByShow, hiddenOrderBy,
				modifyBy, modifyDate, isLeafCheck],
		buttons : [{
					text : Constants.BTN_SAVE,
					iconCls : Constants.CLS_SAVE,
					handler : getFun("save")
				}, {
					text : Constants.BTN_CANCEL,
					iconCls : Constants.CLS_CANCEL,
					handler : function() {
						customNode.setCurrentNode(rootNode);
						customNode.editNode();
					}
				}]
	}

	var formPanel = new Ext.FormPanel({
				frame : true,
				labelAlign : 'center',
				items : [fieldSet],
				tbar : [{
							text : "新增压板",
							iconCls : Constants.CLS_ADD,
							handler : getFun("addNode")
						}, {
							text : "删除压板",
							iconCls : Constants.CLS_DELETE,
							handler : getFun("deleteNode")
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
				customNode.save();
			}
		}
	}

	// ============== 定义treePanel ===============
	var panelLeft = new Ext.Panel({
				region : 'west',
				layout : 'fit',
				width : 200,
				autoScroll : true,
				containerScroll : true,
				collapsible : true,
				split : true,
				items : [treePanel]
			});

	var panelRight = new Ext.Panel({
				region : "center",
				layout : 'fit',
				items : [formPanel]
			});

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				items : [panelLeft, panelRight]
			});

	customNode.editNode();
});