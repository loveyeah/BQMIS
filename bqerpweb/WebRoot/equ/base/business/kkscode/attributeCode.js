Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var currentType = 'add';
	var currentNode = new Object();
	currentNode.id = "root";
	currentNode.text = "设备树";
	//	// -----------定义tree-----------------
	var root = new Ext.tree.AsyncTreeNode({
		id : "root",
		text : "设备树"
	});

	var mytree = new Ext.tree.TreePanel({
		renderTo : "treepanel",
		autoHeight : true,
		root : root,
		animate : true,
		enableDD : false,
		border : false,
		rootVisible : true,
		containerScroll : true,
		loader : new Ext.tree.TreeLoader({
			url : "equ/getEquTreeList.action",
			baseParams : {
				id : 'root'
			}

		})
	});

	// ---------------------------------------
	//-----------树的事件----------------------
	//树的单击事件
	mytree.on("click", clickTree, this);
	mytree.on('beforeload', function(node) {
		// 指定某个节点的子节点
		mytree.loader.dataUrl = 'equ/getEquTreeList.action?id=' + node.id;
	}, this);
	root.expand();//展开根节点
	//为树加右键菜单
	mytree.on('contextmenu', function(node, e) {
		e.stopEvent();
		if (node.id == "root") {
			return false;
		}
		var menu = new Ext.menu.Menu({
			id : 'mainMenu',
			items : [new Ext.menu.Item({
				text : '新增',
				iconCls : 'add',
				style : 'display:' + (node.id.length == 16|| node.id.length == 17 ? 'none' : ''),
				handler : function() {
					initForm();
					currentNode = node;
					currentType = "add";
					//-----增加时各字段的显隐--------------
					if (node.id == "root") {
						Ext.get("StartSysCode").dom.parentNode.parentNode.style.display = '';
					} else {
						Ext.get("StartSysCode").dom.parentNode.parentNode.style.display = 'none';
					}
					if(node.id.length==2)
					{
						Ext.getCmp("attributeCode").setEditable(true);
					}
					else
					{
						Ext.getCmp("attributeCode").setEditable(false);
					}
					if (node.id.length == 5 || node.id.length == 7
							|| node.id.length == 12|| node.id.length == 13) {
						Ext.get("equNum").dom.parentNode.parentNode.style.display = '';
					} else {
						Ext.get("equNum").dom.parentNode.parentNode.style.display = 'none';
					}

					//  Ext.get("equNum").dom.parentNode.maxLength=2;
					//  alert(Ext.get("equNum").dom.parentNode.maxLength);
					//   				    if(node.id.length==5)
					//   				    {
					//   				    	Ext.get("attributeCode").dom.parentNode.parentNode.parentNode.style.display = 'none';
					//   				        Ext.get("equName").dom.value = node.text.substring(currentNode.text.indexOf(' ')+1,currentNode.text.length);
					//   				    }
					//   				    else
					//   				    {
					//   				    	Ext.get("attributeCode").dom.parentNode.parentNode.parentNode.style.display = '';
					//   				    }
					//-------------------------------
					Ext.get("PAttributeCode").dom.value = node.id;
					Ext.get("equId").dom.value = "自动生成";

					myaddpanel.setTitle("在目录【" + node.text + "】下增加");
				}
			}), new Ext.menu.Item({
				text : '删除',
				iconCls : 'delete',
				handler : function() {
					currentNode = node;
					Ext.Msg.confirm("删除", "是否确定删除" + node.text + "吗？",
							function(buttonobj) {
								if (buttonobj == "yes") {

									Ext.Ajax.request({
										url : 'equ/deleteEqu.action',
										params : {
											id : node.id,
											method : 'code'
										},
										method : 'post',
										waitMsg : '正在加载数据...',
										success : function(result, request) {
											var json = eval('('
													+ result.responseText + ')');
											Ext.Msg.alert("注意", json.msg);
											var rnode = currentNode.parentNode;
											rnode.removeChild(currentNode);
											currentNode.id = "root";
											currentNode.text = "设备树";
											clickTree(currentNode);
											root.select();

										}
									});

								}

							});
				}
			}), new Ext.menu.Item({
				text : '修改',
				iconCls : 'update',
				handler : function() {
					clickTree(node);
				}
			})]
		});
		var coords = e.getXY();
		menu.showAt([coords[0], coords[1]]);
	}, this);
	//-----------

	//
	// //add for tree add

	// ------------
	function initForm() {
		Ext.get("equId").dom.value = "";
		Ext.get("attributeCode").dom.value = "";
		Ext.get("PAttributeCode").dom.value = "";
		Ext.get("equName").dom.value = "";
		Ext.get("locationCode").dom.value = "";
		Ext.get("installationCode").dom.value = "";
		Ext.get("bugCode").dom.value = "";
		if (Ext.get("equNum").dom.parentNode.parentNode.style.display == '') {
			Ext.get("equNum").dom.value = "";
		}
	}
	// ---------树的click事件
	function clickTree(node) {

		Ext.get("attributeCode").dom.parentNode.parentNode.parentNode.style.display = '';
		if (node.id == "root") {
			//根节点
			currentType = 'add';
			currentNode = node;
			initForm();
			myaddpanel.setTitle("在【" + node.text + "】下增加");
			Ext.get("PAttributeCode").dom.value = "root";
			Ext.get("equId").dom.value = "自动生成";
			Ext.get("equNum").dom.parentNode.parentNode.style.display = 'none';
			//Ext.get("mydelete").dom.parentNode.style.display = 'none';
			Ext.get("StartSysCode").dom.parentNode.parentNode.style.display = '';
		} else {

			Ext.get("StartSysCode").dom.parentNode.parentNode.style.display = 'none';

			currentNode = node;
			currentType = "update";
			initForm();
			// Ext.get("PAttributeCode").dom.value=node.id;
			myaddpanel.setTitle("修改【" + node.text + "】基本信息");
			Ext.get("equNum").dom.parentNode.parentNode.style.display = 'none';
			Ext.get("mydelete").dom.parentNode.style.display = '';

			Ext.Ajax.request({
				url : 'equ/findEquByCode.action',
				params : {
					id : node.id
				},
				method : 'post',
				waitMsg : '正在加载数据...',
				success : function(result, request) {
					var json = eval('(' + result.responseText + ')');

					var obj = json.data;
					Ext.get("equId").dom.value = obj.equId;
					Ext.get("attributeCode").dom.value = obj.attributeCode;
					if(obj.attributeCode.length==5)
					{
					Ext.get("PAttributeCode").dom.value = obj.PAttributeCode;
					}
					else
					{
						//设置父编码
						var mypcode="";
						if(obj.attributeCode.length==2) {mypcode="root"; }
						if(obj.attributeCode.length==4) {mypcode=obj.attributeCode.substring(0,2); }
						if(obj.attributeCode.length==7) {mypcode=obj.attributeCode.substring(0,5); }
						if(obj.attributeCode.length==12||obj.attributeCode.length==13) {mypcode=obj.attributeCode.substring(0,7); }
						if(obj.attributeCode.length==16) {mypcode=obj.attributeCode.substring(0,12); }
						if(obj.attributeCode.length==17) {mypcode=obj.attributeCode.substring(0,13); }
						Ext.get("PAttributeCode").dom.value =mypcode;
					}
					Ext.get("equName").dom.value = obj.equName;
					if (obj.locationCode != null) {

						Ext.getCmp('locationCode').setValue(obj.locationCode);

						Ext.form.ComboBox.superclass.setValue.call(Ext
								.getCmp('locationCode'), json.locName);

					}
					if (obj.installationCode != null) {

						Ext.getCmp('installationCode')
								.setValue(obj.installationCode);

						Ext.form.ComboBox.superclass.setValue.call(Ext
								.getCmp('installationCode'), json.installName);
					}
					if (obj.bugCode != null) {
						Ext.get("bugCode").dom.value = obj.bugCode;
					}

				},
				failure : function(result, request) {
					Ext.MessageBox.alert('错误', '操作失败!');
				}
			});

		};
	};

	// -----------
	var equId = {
		id : "equId",
		xtype : "textfield",
		fieldLabel : 'ID',
		value : '自动生成',
		readOnly : true,
		allowBlank : false,
		name : 'equId',
		anchor : "80%"
	}

	var StartSysCode = {
		id : "StartSysCode",
		xtype : "textfield",
		fieldLabel : '系统前置编号',
		value : '0',
		allowBlank : false,
		name : 'StartSysCode',
		maxLength : 1,
		anchor : "80%"
	}

	var attributeCode = {
		fieldLabel : '功能码',
		name : 'equ.attributeCode',
		xtype : 'combo',
		id : 'attributeCode',
		//	store : ,
		valueField : "id",
		displayField : "name",
		mode : 'remote',
	//	allowBlank : false,
		anchor : "80%",
		//	typeAhead : true,
		//	forceSelection : true,
		//	hiddenName : 'equ.attributeCode',
		editable : false,
		triggerAction : 'all',
		onTriggerClick : function() {
			if (currentType == "add") {
				//增加时选择功能码
				var URL = "";
				if (Ext.get("PAttributeCode").dom.value == "root") {
					URL = "blockSelect.jsp";

				} else if (Ext.get("PAttributeCode").dom.value.length == 2) {
					//URL = "sysSelect.jsp?code=1";
				} else if (Ext.get("PAttributeCode").dom.value.length == 4) {
//					var mycode = Ext.get("PAttributeCode").dom.value;
//					mycode = mycode.substring(mycode.length - 1, mycode.length);

					URL = "sysSelect.jsp?code=all";
				} else if (Ext.get("PAttributeCode").dom.value.length == 7) {
					URL = "equSelect.jsp";
				} else if (Ext.get("PAttributeCode").dom.value.length == 12||Ext.get("PAttributeCode").dom.value.length == 13) {
					URL = "partSelect.jsp";
				} else {
					URL = "";
				}

				if (URL != "") {
					var block = window.showModalDialog(URL, '',
							'dialogWidth=600px;dialogHeight=400px;status=no');

					if (typeof(block) != "undefined") {
						if (currentNode != null) {
							if (currentNode.text != ""
									&& currentNode.text != "设备树") {
								var myname = currentNode.text.substring(
										currentNode.text.indexOf(' ') + 1,
										currentNode.text.length);

								Ext.get("equName").dom.value = myname
										+ block.name;
							} else {
								Ext.get("equName").dom.value = block.name;
							}
						} else {
							Ext.get("equName").dom.value = block.name;
						}
						Ext.get("attributeCode").dom.value = block.code;

					}
				}
			}
		}

	};

	var PAttributeCode = {
		id : "PAttributeCode",
		xtype : "textfield",
		fieldLabel : '父编码',
		allowBlank : false,
		readOnly : true,
		value : 'root',
		anchor : "80%",
		name : 'equ.PAttributeCode'
	}

	var equNum = {
		id : "equNum",
		xtype : "textfield",
		fieldLabel : '编号',
		name : 'equNum',
		anchor : "80%",
		maxLength : 4
	}
	var equName = {
		id : "equName",
		xtype : "textfield",
		fieldLabel : '设备名称',
		allowBlank : false,
		anchor : "80%",
		name : 'equ.equName'
	}

	var bugCode = {
		id : "bugCode",
		xtype : "hidden",
		fieldLabel : '故障码',
		anchor : "80%",
		name : 'equ.bugCode'
	}

	var locationCode = {
		fieldLabel : '位置码',
		name : 'locationCode',
		xtype : 'combo',
		id : 'locationCode',
		anchor : "80%",
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'equ.locationCode',
		//	allowBlank : false,
		editable : false,
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../kksselect/selectLocation.jsp";
			var location = window.showModalDialog(url, '',
					'dialogWidth=400px;dialogHeight=400px;status=no');
			if (typeof(location) != "undefined") {

				Ext.getCmp('locationCode').setValue(location.code);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('locationCode'), location.name);
			}

		}
	};

	var installationCode = {
		fieldLabel : '安装点码',
		name : 'installationCode',
		xtype : 'combo',
		id : 'installationCode',
		anchor : "80%",
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'equ.installationCode',
		//	allowBlank : false,
		editable : false,
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../kksselect/selectInstallation.jsp";
			var install = window.showModalDialog(url, '',
					'dialogWidth=400px;dialogHeight=400px;status=no');
			if (typeof(install) != "undefined") {

				Ext.getCmp('installationCode').setValue(install.code);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('installationCode'), install.name);
			}

		}
	};

	//------------

	var myaddpanel = new Ext.FormPanel({
		frame : true,
		//	containerScroll : true,
	//	autoScroll : true,
		labelAlign : 'left',
		title : '在目录【设备树】下增加',
		bodyStyle : 'padding:30px',

		items : [{
			xtype : 'fieldset',
			id : 'formSet',
			title : '基本信息',
			//	width : 450,
			labelAlign : 'right',
			labelWidth : 80,
			//			defaults : {
			//				width : 250
			//			},
			defaultType : 'textfield',
			autoHeight : true,
			bodyStyle : Ext.isIE
					? 'padding:0 0 5px 15px;'
					: 'padding:10px 15px;',
			border : true,
			style : {
				"margin-left" : "10px",
				"margin-right" : Ext.isIE6
						? (Ext.isStrict ? "-10px" : "-13px")
						: "0"
			},
			items : [equId, PAttributeCode, attributeCode,StartSysCode, 
					equNum, equName, locationCode, installationCode, bugCode],
			buttons : [{
				text : '删除',
				id : 'mydelete',
				iconCls : 'delete',
				handler : function() {
					if (currentType == "add") {
						Ext.Msg.alert("注意", "请选择要删除的设备");
					}
					if (currentType == "update") {

						Ext.Msg.confirm("删除", "是否确定删除"
								+ Ext.get("equName").dom.value + "吗？",
								function(buttonobj) {
									if (buttonobj == "yes") {
										myaddpanel.form.submit({
											method : 'POST',
											url : 'equ/deleteEqu.action?method=id',
											success : function(form, action) {
												var o = eval("("
														+ action.response.responseText
														+ ")");
												Ext.Msg.alert("注意", o.msg);
												var rnode = currentNode.parentNode;
												rnode.removeChild(currentNode);
												currentNode.id = "root";
												currentNode.text = "设备树";
												clickTree(currentNode);

												root.select();

											}
										})
									}

								});

					}
				}
			}, {
				text : '保存',
				id : 'mysave',
				iconCls : 'save',
				handler : function() {

					var check = true;
					if (currentType == "add") {
						if (currentNode.id == "root") {
							var r = /^[0-9]*[0-9][0-9]*$/;
							if (r
									.test(document
											.getElementById("StartSysCode").value) == false) {
								check = false;
								alert("前置编号请输入数字");
								return;
							}
						}
						if(currentNode.id.length!=5)
						{
							if(Ext.get("attributeCode").dom.value=="")
							{
							 alert("请输入功能码");	
							 return ;
							}
						}
						if(currentNode.id.length == 2)
						{
							if( Ext.get("attributeCode").dom.value.length!=2)
							{
							 	alert("专业编码请输入两位");
							 	return;
							}
						}

						if (currentNode.id.length == 5
								|| currentNode.id.length == 7
								|| currentNode.id.length == 12||currentNode.id.length == 13) {
							if (document.getElementById("equNum").value != "") {
							
								var r = /^[0-9]*[0-9][0-9]*$/;
								if (r
										.test(document.getElementById("equNum").value) == false) {
									check = false;

								}
								var num = 0;
								if (currentNode.id.length == 5) {
									num = 2;
								} else if (currentNode.id.length == 7) {
									num = 3;
								} else if (currentNode.id.length == 12||currentNode.id.length == 13) {
									num = 2;
								} else {
									num = 0;
								}

								if (num != 0) {
									if (document.getElementById("equNum").value.length != num) {
										if(num==3&&document.getElementById("equNum").value.length==4)
										{
											check = true;
										}
										else
										{
										check = false;
										alert("请输入" + num + "位数字！");
										}
									} else {
										if (check == false) {
											alert("请输入数字");
										}
									}

								}
							} else {
								check = false;
								alert("编号不能为空");
							}
						}

					}

					if (check == true) {
						myaddpanel.form.submit({
							method : 'POST',
							url : 'equ/saveEquInfo.action?method='
									+ currentType,
							success : function(form, action) {
								// ---add----
								// alert(action.response.responseText);
								var o = eval("(" + action.response.responseText
										+ ")");
								Ext.Msg.alert("注意", o.msg);
								// ----------
								if (currentType == "add") {
                                     
									if (o.id != -1) {

										//--------add----------
										var newNode = new Ext.tree.AsyncTreeNode({
											id : o.id,
											leaf : true,
											text : o.id
													+ " "
													+ Ext.get("equName").dom.value
										});
									

										var pnode = mytree
												.getNodeById(currentNode.id);
												
										if (!pnode.isLeaf()) {
											
											var fc = pnode.firstChild;

											if (fc == null)
												pnode.appendChild(newNode);
											else
												pnode.insertBefore(newNode, fc);
											newNode.select();
										} else {
										
											var rnode = new Ext.tree.AsyncTreeNode({
												id : pnode.id,
												text : pnode.text,
												leaf : false
											});
											var rsibling = pnode.nextSibling;
											var ppnode = pnode.parentNode;
											pnode.remove();
											
											if (rsibling != null)
												ppnode.insertBefore(rnode,
														rsibling);
											else
												ppnode.appendChild(rnode);
											
											rnode.appendChild(newNode);
											ppnode.expand();
											
											if (rnode.firstChild != null) {
												rnode.firstChild.select();
											}

										}
								
										currentNode = newNode;
										currentType = "update";

										clickTree(newNode);
									}
								} else {
									if (o.msg == "修改成功！") {
										var cnode = mytree
												.getNodeById(currentNode.id);
										cnode.setText(currentNode.id + " "
												+ Ext.get("equName").dom.value);
										mytree.getNodeById(cnode.parentNode.id)
												.reload();
									}
								}
							},
							faliue : function() {
								Ext.Msg.alert('错误', '出现未知错误.');
							}
						});

					}
				}

			}]

		}]

	});
	// ------- 布局add

	//------------

	//	var panelleft = new Ext.Panel({
	//		region : 'west',
	//		layout : 'fit',
	//		width : 250,
	//		//autoScroll:true,
	//		collapsible : true,
	//		split:true,
	//		items : [mytree]
	//	});
	var right = new Ext.Panel({
		region : "center",
		layout : 'fit',
		//width : 300,
		collapsible : true,
		//autoScroll : true,
		items : [myaddpanel]
	});
	//
	//	new Ext.Viewport({
	//		enableTabScroll : true,
	//		layout : "border",
	//		items : [panelleft, right]
	//	});

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
		}, right]
	});

	Ext.get("equNum").dom.parentNode.parentNode.style.display = 'none';
	//Ext.get("StartSysCode").dom.parentNode.parentNode.style.display = 'none';
		// -------------------

		//	Ext.getCmp('locationCode').setValue("test");
		//						Ext.form.ComboBox.superclass.setValue.call(Ext
		//								.getCmp('locationCode'),"value");

});