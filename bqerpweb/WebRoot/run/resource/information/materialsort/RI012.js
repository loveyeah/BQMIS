// 物料分类维护
// author:chenshoujiang
FLAG_MODIFY = "1";
FLAG_ADD = "0";
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();

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
					// TODO
					this._parentNode = argNode;
				}
			}

			// 在当前节点增加一个节点
			CustomNode.prototype.addNode = function() {
				var isClick = this.isCurrentClick;
				var currentId = this._currentNode.id;
				var currentName = this._currentNode.text
				if (!this.isCurrentClick) {
					NameTxt.clearInvalid();
					//codeTxt.clearInvalid();
					// 未选中节点时
					Ext.Msg
							.alert(Constants.SYS_REMIND_MSG,
									Constants.COM_I_001);
					return false;
				} else {
					if (hiddenCode.getValue() != codeTxt.getValue()
							|| hiddenName.getValue() != NameTxt.getValue()) {
						Ext.Msg.confirm(Constants.SYS_REMIND_MSG,
								Constants.COM_C_004, function(buttonobj) {
									if (buttonobj == "yes") {
										btnDelete.setDisabled(true);
										rightPanel.getForm().reset();
										Ext.get("hiddenFlag").dom.value = FLAG_ADD;
										hiddenSerial.setValue("");
										if (currentId == "-1") {
											Ext.get("fatherCodeTxt").dom.value == ""
											Ext.get("hiddenFatherCode").dom.value == "-1";
											Ext.get("fatherNameTxt").dom.value == "";
										} else {
											rightPanel.getForm().loadRecord({
												data : {
													fatherCodeTxt : currentId,
													hiddenFatherCode : currentId,
													fatherNameTxt : currentName
												}
											});
										}
										//codeTxt.setDisabled(false);
										NameTxt.setDisabled(false);
										btnSave.setDisabled(false);
									}
								})
					} else {
						var blnLeaf = this._currentNode.isLeaf();
						btnDelete.setDisabled(true);
						rightPanel.getForm().reset();
						Ext.get("hiddenFlag").dom.value = FLAG_ADD;
						hiddenSerial.setValue("");
						if (currentId == "-1") {
							Ext.get("fatherCodeTxt").dom.value = ""
							Ext.get("hiddenFatherCode").dom.value = "-1";
							Ext.get("fatherNameTxt").dom.value = "";
						} else {
							rightPanel.getForm().loadRecord({
										data : {
											fatherCodeTxt : this._currentNode.id,
											hiddenFatherCode : this._currentNode.id,
											fatherNameTxt : this._currentNode.text
										}
									});
						}
						//codeTxt.setDisabled(false);
						NameTxt.setDisabled(false);
						btnSave.setDisabled(false);
					}
				}
			}
			// 修改当前节点
			CustomNode.prototype.editNode = function() {
				btnSave.setDisabled(false);
				btnDelete.setDisabled(false);
				var proto = this._currentNode.attributes;
				var parentNode = this._parentNode;
				var current = this._currentNode;
				var tempParent = this._parentNode;
				var isCurrentLeaf = this._currentNode.isLeaf();
				var temp = null;
				if (codeTxt.getValue() != "") {
					temp = customNode._findNodeByCode(codeTxt.getValue());
				}
				if (hiddenCode.getValue() != codeTxt.getValue()
						|| hiddenName.getValue() != NameTxt.getValue()) {
					// Ext.Msg.confirm(Constants.SYS_REMIND_MSG,Constants.COM_C_004,
					// function(buttonobj) {
					// if (buttonobj == "yes") {

					rightPanel.getForm().reset();
					Ext.get("hiddenFlag").dom.value = FLAG_MODIFY;
					Data.prototype = proto
					var record = {
						data : new Data(parentNode)
					};
					// 显示该行记录
					rightPanel.getForm().loadRecord(record);

					// }else
					// {
					// if(Ext.get("hiddenFlag").dom.value == FLAG_MODIFY)
					// {
					// // 未选中节点
					// customNode.isCurrentClick = false;
					// customNode.setCurrentNode(temp);
					// // 选中节点
					// customNode.isCurrentClick = true;
					// }
					// }
					// })
				} else {

					rightPanel.getForm().reset();
					Ext.get("hiddenFlag").dom.value = FLAG_MODIFY;
					// alert(customNode._currentNode.attributes.id);
					// alert(customNode._parentNode.attributes.id);
					Data.prototype = this._currentNode.attributes;
					var record = {
						data : new Data(this._parentNode)
					};
					// 显示该行记录
					rightPanel.getForm().loadRecord(record);
				}
			}

			CustomNode.prototype.test = function() {
				msg = '';
				for (var prop in this) {
					msg += prop + '\n';
				}
				alert(msg);
			}

			CustomNode.prototype.deleteNode = function() {
				var blnLeaf = customNode._currentNode.isLeaf();
				if (!customNode.isCurrentClick) {
					// 未选中节点时
					Ext.Msg
							.alert(Constants.SYS_REMIND_MSG,
									Constants.COM_I_001);
					return false;
				} else if (!blnLeaf && customNode._currentNode.childNodes
						&& customNode._currentNode.childNodes.length > 0) {
					Ext.Msg.confirm(Constants.SYS_REMIND_MSG,
							Constants.RI012_W_001, function(buttonobj) {
								if (buttonobj == "yes") {
									Ext.Ajax.request({
										method : Constants.POST,
										url : 'resource/deleteMaterialTypeSort.action',
										success : function(action) {
											if(action.responseText=="NO") 
											{
												Ext.Msg.alert("提示","此类别下有物资存在，不能删除！");
											    return ;
											}
											var result = eval(action.responseText);
											
										
											// 如果成功，弹出删除成功
											if (result == true) {
												Ext.Msg
														.alert(
																Constants.SYS_REMIND_MSG,
																Constants.COM_I_005)
												if (customNode._parentNode.attributes.id != -1) {
													customNode
															.setCurrentNode(customNode._parentNode);
													// // 树形同步显示
													customNode._parentNode
															.reload();
												} else {
													customNode._parentNode
															.reload();
												}
												customNode.isCurrentClick = false;
												fatherCodeTxt.setValue("");
												fatherNameTxt.setValue("");
												codeTxt.setValue("");
												NameTxt.setValue("");
												hiddenCode.setValue(codeTxt
														.getValue());
												hiddenName.setValue(NameTxt
														.getValue());
												hiddenFatherCode
														.setValue(fatherCodeTxt
																.getValue());
												hiddenFlag
														.setValue(FLAG_MODIFY);
												hiddenSerial.setValue("");
												//codeTxt.setDisabled(true);
												NameTxt.setDisabled(true);
												NameTxt.clearInvalid();
												//codeTxt.clearInvalid();
											}
										},
										params : {
											// serial :
											// customNode._currentNode.attributes.serial
											serial : customNode._currentNode.attributes.code
										}
									});
								}
							});
				} else {
					// alert("当前结点id："+ customNode._currentNode.attributes.id);
					// alert("当前父节点结点id："+customNode._parentNode.attributes.id);
					Ext.Msg.confirm(Constants.SYS_REMIND_MSG,
							Constants.COM_C_002, function(buttonobj) {
								if (buttonobj == "yes") {
									Ext.Ajax.request({
										method : Constants.POST,
										url : 'resource/deleteMaterialTypeSort.action',
										success : function(action) {
											if(action.responseText=="NO") 
											{
												Ext.Msg.alert("提示","此类别下有物资存在，不能删除！");
											    return ;
											}
											var result = eval(action.responseText);
											// 如果成功，弹出删除成功
											if (result == true) {
												Ext.Msg
														.alert(
																Constants.SYS_REMIND_MSG,
																Constants.COM_I_005)
												// alert("当前父节点结点id："+customNode._parentNode.attributes.id);
												if (customNode._parentNode.attributes.id != -1) {
													customNode
															.setCurrentNode(customNode._parentNode);
													// // 树形同步显示
													customNode._parentNode
															.reload();
												} else {
													customNode._parentNode
															.reload();
												}
												// customNode.setCurrentNode(root);
												// customNode.editNode();
												customNode.isCurrentClick = false;
												fatherCodeTxt.setValue("");
												fatherNameTxt.setValue("");
												codeTxt.setValue("");
												NameTxt.setValue("");
												hiddenCode.setValue(codeTxt
														.getValue());
												hiddenName.setValue(NameTxt
														.getValue());
												hiddenFatherCode
														.setValue(fatherCodeTxt
																.getValue());
												hiddenFlag
														.setValue(FLAG_MODIFY);
												hiddenSerial.setValue("");
												//codeTxt.setDisabled(true);
												NameTxt.setDisabled(true);
												NameTxt.clearInvalid();
												//codeTxt.clearInvalid();
											}
										},
										params : {
											// serial :
											// customNode._currentNode.attributes.serial
											// //modify by fyyang 090623
											serial : customNode._currentNode.attributes.code
										}
									});
								}
							})
				}
			}
		}
		CustomNode.prototype.save = function() {
			var blnLeaf = this._currentNode.isLeaf();
			if (!this.isCurrentClick) {
				NameTxt.clearInvalid();
				//codeTxt.clearInvalid();
				// 未选中节点时
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
				return false;
			} else {
				// 添加时父节点是否是叶子节点
				var isCurrentLeaf = this._currentNode.isLeaf();
				var tempParent = this._parentNode;
				Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_001,
						function(buttonobj) {
							if (buttonobj == "yes") {
								var flagMark = "";
//								if (codeTxt.getValue() == "") {
//									flagMark = flagMark + "C";
//								}
								if (NameTxt.getValue() == "") {
									flagMark = flagMark + "N";
								}
//								if (check(codeTxt.getValue()) > 30) {
//									flagMark = flagMark + "B";
//								}
								if (check(NameTxt.getValue()) > 100) {
									flagMark = flagMark + "M";
								}
								if (flagMark == "C") {
									// 如果编码为空
//									Ext.Msg.alert(Constants.SYS_REMIND_MSG,
//											String.format(Constants.COM_E_002,
//													"编码"));
									//codeTxt.markInvalid();
								} else if (flagMark == "N") {
									// 如果名称为空
									Ext.Msg.alert(Constants.SYS_REMIND_MSG,
											String.format(Constants.COM_E_002,
													"名称"));
									NameTxt.markInvalid();
								} else if (flagMark == "CN") {
									// 如果编码和名称为空
									Ext.Msg.alert(Constants.SYS_REMIND_MSG,
//									String.format(Constants.COM_E_002,
//																	"编码")
//															+ "</br>"
//															+ 
															String
																	.format(
																			Constants.COM_E_002,
																			"名称"));
									//codeTxt.markInvalid();
									NameTxt.markInvalid();
								} 
//								else if (flagMark == "B") {
//									// 编码大于30个字符  add by bjxu91103
//									Ext.Msg.alert("提示", "编码溢出！");
//									return;
//								} 
								else if (flagMark == "M") {
									//名称大于100个字符 add by bjxu91103
									Ext.Msg.alert("提示", "名称溢出！");
									return;
								}else
								if (flagMark == "") {
									// 如果是新增状态下
									if (Ext.get("hiddenFlag").dom.value == FLAG_ADD) {
										if (isNegative(codeTxt.getValue()) == 1) {
//											Ext.Msg
//													.alert(
//															Constants.SYS_REMIND_MSG,
//															String
//																	.format(
//																			Constants.COM_E_012,
//																			"编码"));
										} else {
											var tempParent = customNode._parentNode;
											var isCurrentLeaf = customNode._currentNode
													.isLeaf();
											Ext.Ajax.request({
												method : Constants.POST,
												url : 'resource/checkCode.action',
												success : function(action) {
													var result = eval(action.responseText);
													// 如果成功，弹出删除成功
													if (result == true) {
														// 如果数据库中存在该编码
														Ext.Msg
																.alert(
																		Constants.ERROR,
																		String
																				.format(
																						Constants.COM_E_007,
																						"编码"));
													} else {
														Ext.Ajax.request({
															method : Constants.POST,
															url : 'resource/saveMaterialType.action',
															success : function(
																	action) {
																var result = eval(action.responseText);
																if (result) {
																	// 如果增加成功，弹出保存成功信息
																	Ext.Msg
																			.alert(
																					Constants.SYS_REMIND_MSG,
																					String
																							.format(Constants.COM_I_004));

																	if (!isCurrentLeaf) {
																		customNode._parentNode = customNode._currentNode;
																		tempParent = customNode._parentNode;
																	}
																	var nodeLoadHandler = function() {
																		customNode
																				._setNodeAfterLoad(
																						FLAG_ADD,
																						isCurrentLeaf);
																	}
																	customNode._tempLoadHandler = nodeLoadHandler;
																	tempParent
																			.on(
																					"load",
																					nodeLoadHandler);
																	// 树形同步显示
																	tempParent
																			.reload();
																	// 设置编码字段是可以填写
//																	codeTxt
//																			.setDisabled(true);
																	btnDelete
																			.setDisabled(false);
																}
															},
															params : {
																fatherCode : hiddenFatherCode
																		.getValue()
																		.RTrim(),
																code : codeTxt
																		.getValue()
																		.RTrim(),
																name : NameTxt
																		.getValue()
																		.RTrim()
															}
														});
													}
												},
												params : {
													code : codeTxt.getValue()
															.RTrim()
												}
											});
										}
									}
									// 如果现在是在修改状态
									else if (Ext.get("hiddenFlag").dom.value == FLAG_MODIFY) {
										var isCurrentLeaf = customNode._currentNode
												.isLeaf();
										var tempParent = customNode._parentNode;
										Ext.Ajax.request({
											method : Constants.POST,
											url : 'resource/modifyMaterialType.action',
											success : function(action) {
												var result = eval(action.responseText);
												if (result) {
													// 如果增加成功，弹出保存成功信息
													Ext.Msg
															.alert(
																	Constants.SYS_REMIND_MSG,
																	Constants.COM_I_004);
													tempParent = customNode._parentNode;
													var nodeLoadHandler = function() {
														customNode
																._setNodeAfterLoad(
																		FLAG_MODIFY,
																		isCurrentLeaf);
													}
													customNode._tempLoadHandler = nodeLoadHandler;
													tempParent.on("load",
															nodeLoadHandler);
													// 树形同步显示
													tempParent.reload();

													// 设置新增按钮可用
													btnAdd.setDisabled(false);
													btnDelete
															.setDisabled(false);
													// alert("id:"+customNode._currentNode.attributes.id)
													// alert("par:"+customNode._parentNode.attributes.id)
												}
											},
											params : {
												serial : Ext
														.get("hiddenSerial").dom.value,
												code : codeTxt.getValue()
														.RTrim(),
												name : NameTxt.getValue()
														.RTrim()
											}
										});
									}
								}
							}
						})
			}
		}

		// // 加载子节点后设置当前的节点
		CustomNode.prototype._setNodeAfterLoad = function(blnEdit,
				isCurrentLeaf) {
			// 删除节点监听
			this._parentNode.un("load", this._tempLoadHandler);
			delete this._tempLoadHandler;

			var temp = null;
			if (blnEdit != FLAG_MODIFY && !isCurrentLeaf) {
				// // 显示父节点
				temp = this._parentNode;
			} else {
				temp = this._findNodeByCode(Ext.get("codeTxt").dom.value);
			}
			hiddenCode.setValue(codeTxt.getValue());
			hiddenName.setValue(NameTxt.getValue());
			if (temp != null) {
				// 设置当前节点
				this.setCurrentNode(temp);
			}
			this.editNode();
		}

		// 通过节点的Code查找节点
		CustomNode.prototype._findNodeByCode = function(argCode) {
			var result = null;
			var blnLeaf = root.isLeaf();
			if (!blnLeaf) {
				var lstNodes = root.childNodes;
				// 子节点的长度
				var len = lstNodes ? lstNodes.length : 0;

				for (var intCnt = 0; intCnt < len; intCnt++) {
					if (lstNodes[intCnt].attributes.id == argCode) {
						result = lstNodes[intCnt];
						break;
					} else {
						if (!lstNodes[intCnt].isLeaf()) {
							result = this.findNode(argCode, lstNodes[intCnt]);
							if (result != null)
								break;
						}
					}
				}
			}
			return result;
		}

		// 查找子节点
		CustomNode.prototype.findNode = function(argCode, node) {
			var result = null;
			var lstNodes = node.childNodes;
			// 子节点的长度
			var len = lstNodes ? lstNodes.length : 0;
			for (var intCnt = 0; intCnt < len; intCnt++) {
				if (lstNodes[intCnt].attributes.id == argCode) {
					result = lstNodes[intCnt];
					break;
				} else {
					if (!lstNodes[intCnt].isLeaf()) {
						result = this.findNode(argCode, lstNodes[intCnt]);
					}
				}
			}
			return result;
		}
	}
	function Data(argParent) {
		// 根结点
		if (this.isRoot) {
			this.codeTxt = "";
			this.NameTxt = "";
			this.hiddenCode = "";
			this.hiddenName = "";
			//codeTxt.setDisabled(true);
			NameTxt.setDisabled(true);
			btnDelete.setDisabled(true);
			btnSave.setDisabled(true);
			hiddenSerial.setValue("-1");

		} else {
			// 编码
			this.codeTxt = this.id;
			this.hiddenCode = this.id;
			// 名称
			this.NameTxt = this.text.substring(0, this.text.length - 2
							- this.id.length);// 截取物料名称 modify by ywliu
												// 2006/6/26
			this.hiddenName = this.text.substring(0, this.text.length - 2
							- this.id.length);
			this.hiddenSerial = this.code; // modify by fyyang 090623
			//codeTxt.setDisabled(true);
			NameTxt.setDisabled(false);
			btnDelete.setDisabled(false);
			btnSave.setDisabled(false);
			if (argParent.id == "-1") {
				this.fatherCodeTxt = "";
				this.hiddenFatherCode = "-1";
				this.fatherNameTxt = "";
			} else {
				// 父编码
				this.fatherCodeTxt = argParent.id;
				// 父名称
				this.fatherNameTxt = argParent.text.substring(0,
						argParent.text.length - 2 - argParent.id.length);// 截取物料名称
																			// modify
																			// by
																			// ywliu
																			// 2006/6/26
			}
		}
	};

	// ↓↓*******************物料分类维护**************************************

	// 父编码textfield(txtFatherCode)
	var fatherCodeTxt = new Ext.form.TextField({
				id : "fatherCodeTxt",
				fieldLabel : '父编码',
				disabled : true,
				width : 180,
				maxLength : 30
			});

	// 父名称textfield
	var fatherNameTxt = new Ext.form.TextField({
				id : "fatherNameTxt",
				fieldLabel : '父名称',
				disabled : true,
				width : 180,
				maxLength : 100
			});

	// 编码textfield
	var codeTxt = new Ext.form.TextField({
				id : "codeTxt",
				fieldLabel : '编码',
			//	allowBlank : false,
				disabled : true,
				width : 180,
			//	codeField : "yes",
				style : {
					'ime-mode' : 'disabled'
				},
				maxLength : 30
			});

	// 名称textfield
	var NameTxt = new Ext.form.TextField({
				id : "NameTxt",
				fieldLabel : '名称<font color ="red">*</font>',
				allowBlank : false,
				width : 180,
				disabled : true,
				maxLength : 50
			});

	// 判断是删除还是修改隐藏域
	var hiddenFlag = new Ext.form.Hidden({
				id : "hiddenFlag"
			});

	// 判断是否是根结点的隐藏域
	var hiddenFatherCode = new Ext.form.Hidden({
				id : "hiddenFatherCode"
			});

	// �
	var hiddenCode = new Ext.form.Hidden({
				id : "hiddenCode"
			});

	// �
	var hiddenName = new Ext.form.Hidden({
				id : "hiddenName"
			});

	var hiddenSerial = new Ext.form.Hidden({
				id : "hiddenSerial"
			})

	// 新增按钮
	var btnAdd = new Ext.Button({
				text : "新增",
				iconCls : Constants.CLS_ADD,
				handler : getFun("addNode")
			});

	// 删除按钮
	var btnDelete = new Ext.Button({
				text : Constants.BTN_DELETE,
				iconCls : Constants.CLS_DELETE,
				disabled : true,
				handler : getFun("deleteNode")
			});

	// 保存按钮
	var btnSave = new Ext.Button({
				text : Constants.BTN_SAVE,
				iconCls : Constants.CLS_SAVE,
				disabled : true,
				handler : getFun("save")
			});

	// 物料类别树
	var Tree = Ext.tree;
	var treeLoader = new Tree.TreeLoader({
				dataUrl : 'resource/getMaterialClass.action'
			})
	// treePanel
	var materialTypeTree = new Tree.TreePanel({
				region : 'center',
				animate : true,
				// autoHeight : true,
				height : 700,
				allowDomMove : false,
				autoScroll : true,
				containerScroll : true,
				collapsible : true,
				split : true,
				// frame : false,
				border : false,
				rootVisible : true,
				root : root,
				animate : true,
				enableDD : false,
				border : false,
				containerScroll : true,
				loader : treeLoader
			});

	var root = new Tree.AsyncTreeNode({
				text : '物料',
				isRoot : true,
				id : '-1'

			});
	materialTypeTree.on("click", treeClick);
	materialTypeTree.setRootNode(root);
	root.expand();
	root.select();
	var customNode = new CustomNode(root);
	// -----------树的事件----------------------
	// 树的单击事件

	/**
	 * 点击树时
	 */
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
	// 物料树
	var panelleft = new Ext.Panel({
				region : 'west',
				layout : 'fit',
				// frame : true,
				width : 250,
				autoScroll : true,
				containerScroll : true,
				collapsible : true,
				split : true,
				items : [materialTypeTree]
			});
	// grid工具栏
	var gridTbar = new Ext.Toolbar({
				height : 25,
				items : [btnAdd, '-', btnDelete, '-', btnSave]
			});
	// 右边窗口
	var rightPanel = new Ext.FormPanel({
				border : false,
				// frame : true,
				tbar : gridTbar,
				// [btnAdd,'-',btnDelete,'-',btnSave],
				labelWidth : 65,
				labelAlign : "right",
				items : [{
							height : 20,
							border : false
						}, {
							layout : "form",
							items : fatherCodeTxt,
							border : false
						}, {
							height : 10,
							border : false
						}, {
							layout : "form",
							items : fatherNameTxt,
							border : false
						}, {
							height : 10,
							border : false
						}, {
							layout : "form",
							items : codeTxt,
							border : false
						}, {
							height : 10,
							border : false
						}, {
							layout : "form",
							items : NameTxt,
							border : false
						}, {
							height : 200,
							border : false
						}, hiddenFlag, hiddenCode, hiddenName, hiddenSerial,
						hiddenFatherCode]
			});

	var registerPanel = new Ext.Panel({
				region : "center",
				layout : 'fit',
				items : [rightPanel]
			});

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				items : [panelleft, registerPanel]
			});

	/**
	 * check字段长度
	 */
	function check(str) {
		// check是否是双字节的
		var reg = /[^\x00-\xff]/
		var s = 0;
		var ts;
		for (i = 0; i < str.length; i++) {
			// 取字符串中的一个字符
			ts = str.substring(i, i + 1);
			if (reg.test(ts)) {
				// 如果字符是双字节的，则长度加2
				s = s + 2;
			} else {
				// 如果是单字节的，则字段加1
				s = s + 1;
			}
		}
		return s;
	}

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

	/**
	 * 去掉右空格
	 */
	String.prototype.RTrim = function() {
		return this.replace(/(\s*$)/g, "");
	};

	/**
	 * check字符串是否是负数
	 */
	function isNegative(s) {
		if (s.indexOf("-") == 0 && s.lastIndexOf("-") == 0
				&& fucCheckNUM(s.substring(1, s.length)) == 1) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * check字符串是否为数字
	 */
	function fucCheckNUM(NUMstr) {
		var i, j, strTemp;
		strTemp = "0123456789";
		if (NUMstr.length == 0)
			return 0
		for (i = 0; i < NUMstr.length; i++) {
			j = strTemp.indexOf(NUMstr.charAt(i));
			if (j == -1) {
				// 说明有字符不是数字
				return 0;
			}
		}
		// 说明是数字
		return 1;
	}
})