Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	// var currentType = 'add';
	var currentNode = new Object();
	var currentReasonId = "0";
	currentNode.id = "0";
	currentNode.text = "故障树";
	// -------------------------

	var wd = 180;

	var bugId = {
		id : "bugId",
		xtype : "textfield",
		fieldLabel : 'ID',
		value : '自动生成',
		width : wd,
		readOnly : true,
		name : 'bugId'

	}
	var PBugId = {
		id : "PBugId",
		xtype : "textfield",
		fieldLabel : '父ID',
		allowBlank : false,
		readOnly : true,
		width : wd,
		name : 'equCBug.PBugId'

	}

	var bugCode = new Ext.form.TextField({
		id : "bugCode",
		xtype : "textfield",
		fieldLabel : '故障码',
		name : 'equCBug.bugCode',
		width : wd,
		allowBlank : false
	});

	var bugName = {
		id : "bugName",
		xtype : "textfield",
		fieldLabel : '故障名称',
		name : 'equCBug.bugName',
		width : wd,
		allowBlank : false
	}

	var bugDesc = {
		id : "bugDesc",
		xtype : "textarea",
		fieldLabel : '故障现象描述',
		name : 'equCBug.bugDesc',
		anchor : '95%',
		height:80,
		allowBlank : false
	}

	var ifLeaf = new Ext.form.ComboBox({
		fieldLabel : '是否叶子',
		store : new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"],
			data : [['0', '否'], ['1', '是']]
		}),
		valueField : "retrunValue",
		displayField : "displayText",
		mode : 'local',
		forceSelection : true,
		blankText : '请选择是否叶子',
		emptyText : '选择是否叶子',
		hiddenName : 'equCBug.ifLeaf',
		value : '0',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		name : 'equCBug.ifLeaf',
		width : wd
			
	});

	var entryBy = {
		id : "entryBy",
		xtype : "textfield",
		fieldLabel : '填写人',
		width : wd,
		name : 'equCBug.entryBy',
		readOnly : true
	}

	var entryDate = {
		id : 'entryDate',
		xtype : "datefield",
		fieldLabel : '填写时间',
		name : 'equCBug.entryDate',
		format : 'Y-m-d',
		width : wd,
		readOnly : true

	};

	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		labelWidth:80,
		title : '增加/修改故障信息',
		items : [{
			layout : 'column',
			items : [{
				columnWidth : 0.5,
				layout : 'form',
				items : [bugId, bugCode, entryBy, ifLeaf]
			}, {
				columnWidth : 0.5,
				layout : 'form',
				items : [PBugId, bugName, entryDate]
			}]
		},{
			layout : 'column',
			items : [{
				columnWidth : 1,
				layout : 'form',
				items : [bugDesc]
			}]
		}]
			
	});

	var win = new Ext.Window({
		width : 600,
		height : 300,
		modal:true,
		buttonAlign : "center",
		items : [myaddpanel],
		layout : 'fit',
		closeAction : 'hide',
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
				var op = "";
				if (Ext.get("bugId").dom.value == "自动生成") {
					op = "add";
				} else {
					op = "update";
				}
				// alert(op);
				myaddpanel.getForm().submit({
					method : 'POST',
					url : 'equbug/saveBugInfo.action?method=' + op,
					success : function(form, action) {

						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("注意", o.msg);
						if (o.msg == "增加成功！") {

							// alert(Ext.get("equCBug.ifLeaf").dom.value);
							var isleaf = true;
							if (Ext.get("equCBug.ifLeaf").dom.value == "0") {
								isleaf = false;
							} else {
								isleaf = true;
							}
							var newNode = new Ext.tree.AsyncTreeNode({
								id : o.id,
								leaf : isleaf,
								text : Ext.get("bugName").dom.value
							});
							
							var pnode = mytree.getNodeById(currentNode.id);
									var fc = pnode.firstChild;
											if (fc == null)
												pnode.appendChild(newNode);
											else
												pnode.insertBefore(newNode,
														fc);
							    
								    clickTree(newNode);
						}
						if(o.msg=="修改成功！")
						{
								var cnode = mytree.getNodeById(currentNode.id);
											cnode.setText(Ext.get("bugName").dom.value);
									 mytree.getNodeById(cnode.parentNode.id).reload();
						}
						win.hide();
						store.reload();
					},
					faliue : function() {
						Ext.Msg.alert('错误', '操作失败!');
					}
				});
			}
		}, {
			text : '取消',
			iconCls : 'cancer',
			handler : function() {
				win.hide();
			}
		}]

	});
	// -------------------------

	// // -----------定义tree-----------------
	var root = new Ext.tree.AsyncTreeNode({
		id : "0",
		text : "故障树"
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
			url : "equbug/getBugTreeList.action",
			baseParams : {
				id : '0'
			}

		})
	});
	// --------tree的事件-----------------
	mytree.on("click", clickTree, this);
	mytree.on('beforeload', function(node) {
		mytree.loader.dataUrl = 'equbug/getBugTreeList.action?id=' + node.id;
	}, this);

	function clickTree(node) {
		gridreason.setTitle(node.text + "----对应的故障原因");
		currentNode = node;
		currentReasonId = "0";

		reasonStore.baseParams = {
			bugId : node.id,
			fuzzy : '%'
		};
		reasonStore.load({
			params : {
				start : 0,
				limit : 10
			}
		});
		gridsolution.setTitle("解决方案");
		querySolution();
		// alert(node.id);

	};
	root.expand();// 展开根节点
	// 为树加右键菜单
	mytree.on('contextmenu', function(node, e) {
		e.stopEvent();
      // alert(node.text);
		var menu = new Ext.menu.Menu({
			id : 'mainMenu',
			items : [new Ext.menu.Item({
				text : '新增',
				iconCls : 'add',
				style : 'display:' + (node.isLeaf() ? 'none' : ''),
				handler : function() {
					 currentNode = node;
					myaddpanel.getForm().reset();
					bugCode.setDisabled(false);
					win.show();
					myaddpanel.setTitle("在'" + node.text + "'下增加");
					Ext.get("PBugId").dom.value = node.id;

				}
			}), new Ext.menu.Item({
				text : '删除',
				iconCls : 'delete',
				style : 'display:' + (node.id == "0" ? 'none' : ''),
				handler : function() {

					Ext.Msg.confirm("删除", "是否确定删除" + node.text + "吗？",
							function(buttonobj) {
								if (buttonobj == "yes") {
									Ext.Ajax.request({
										url : 'equbug/deleteBugInfo.action',
										params : {
											id : node.id
										},
										method : 'post',
										waitMsg : '正在加载数据...',
										success : function(result, request) {
											var json = eval('('
													+ result.responseText + ')');
											Ext.Msg.alert("注意", json.msg);
											//add
											       if (json.msg == "删除成功！") {
												var rnode = node.parentNode;
												var pnode = rnode.parentNode;
												rnode.removeChild(node);
												// 

												if (!rnode.hasChildNodes()) {
													if(rnode.id!='0')
													{
													var newNode = new Ext.tree.AsyncTreeNode({
														id : rnode.id,
														leaf : false,
														text : rnode.text
													});
													pnode.removeChild(rnode);
													pnode.appendChild(newNode);
													pnode.expand();
													newNode.expand();
														//pnode.reload();
													}
												
												}
												root.select();
											}
									// -------------------------------------------
											gridreason.setTitle("故障原因");
											queryreason();
											gridsolution.setTitle("故障解决方案");
											querySolution();
											currentNode.id = "0";
											currentNode.text = "故障树";
											currentReasonId = "0";
											store.reload();
										},
										failure : function(result, request) {
											Ext.MessageBox.alert('错误', '操作失败!');
										}
									});

								}

							});
				}
			}), new Ext.menu.Item({
				text : '修改',
				style : 'display:' + (node.id == "0" ? 'none' : ''),
				iconCls : 'update',
				handler : function() {
					 currentNode = node;
					myaddpanel.getForm().reset();
					win.show();
					myaddpanel.setTitle("修改'" + node.text + "'基本信息");
					bugCode.setDisabled(true);
					Ext.get("bugId").dom.value = node.id;
					Ext.Ajax.request({
						url : 'equbug/findBugById.action',
						params : {
							id : node.id
						},
						method : 'post',
						waitMsg : '正在加载数据...',
						success : function(result, request) {
							var json = eval('(' + result.responseText + ')');

							var obj = json.data;
							Ext.get("bugCode").dom.value = obj.bugCode;
							Ext.get("bugId").dom.value = obj.bugId;
							Ext.get("bugDesc").dom.value = obj.bugDesc;
							Ext.get("PBugId").dom.value = obj.PBugId;
							Ext.get("bugName").dom.value = obj.bugName;
							ifLeaf.setValue(obj.ifLeaf);
							if (obj.entryBy != null) {
								Ext.get("entryBy").dom.value =  json.workName;
							}
							if (obj.entryDate != null) {
								Ext.get("entryDate").dom.value = obj.entryDate
										.substring(0, 10);
							}

						},
						failure : function(result, request) {
							Ext.MessageBox.alert('错误', '操作失败!');
						}
					});

				}
			})]
		});
		var coords = e.getXY();
		menu.showAt([coords[0], coords[1]]);
	}, this);

	// --------------------------------
	// 定义grid 故障原因
	var MyRecord = Ext.data.Record.create([{
		name : 'bugReasonId'
	}, {
		name : 'bugCode'
	}, {
		name : 'bugReasonDesc'
	}]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url : 'equbug/findBugReasonList.action'
			}

	);

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"

	}, MyRecord);

	var reasonStore = new Ext.data.Store({

		proxy : dataProxy,

		reader : theReader

	});

	var fuuzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});
	var sm = new Ext.grid.CheckboxSelectionModel();

	var gridreason = new Ext.grid.GridPanel({
		store : reasonStore,
		height : 200,
		columns : [// 设计页面显示的表格信息，字段与Ext.data.Record.create匹配
		sm, new Ext.grid.RowNumberer({width:20,align : 'center'}),
			{
			header : "ID",
			sortable : true,
			dataIndex : 'bugReasonId',
			hidden:true

		},

		{
			header : "故障编码",
			sortable : true,
			dataIndex : 'bugCode',
			hidden : true,
			align : 'center'
		},

		{
			header : "原因",
			width : 450,
			sortable : true,
			dataIndex : 'bugReasonDesc',
			align : 'center'
		}],
		sm : sm,
		title : '故障原因',

		tbar : [fuuzy, {
			text : "查询",
			iconCls : 'query',
			handler : queryreason
		}, {
			text : "新增",
			iconCls : 'add',
			handler : addreason
		}, {
			text : "修改",
			iconCls : 'update',
			handler : updatereason
		}, {
			text : "删除",
			iconCls : 'delete',
			handler : deletereason
		}],
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : 10,
			store : reasonStore,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});

	gridreason.addListener('rowClick', rowClick);
	function rowClick(grid, rowIndex, e) {
		var record = grid.getStore().getAt(rowIndex);
		var reasonId = record.get("bugReasonId");
		var reasonDesc = record.get("bugReasonDesc");
		currentReasonId = reasonId;
		gridsolution.setTitle(reasonDesc + "---对应的解决方案");
		solutionstore.baseParams = {
			reasonId : reasonId,
			fuzzy : '%'
		};
		solutionstore.load({
			params : {
				start : 0,
				limit : 10
			}
		});

	}

	function queryreason() {
		var fuzzytext = fuuzy.getValue();
		reasonStore.baseParams = {
			fuzzy : fuzzytext,
			bugId : currentNode.id
		};
		reasonStore.load({
			params : {
				start : 0,
				limit : 10
			}
		});
	}

	// -------故障原因增加/修改页面-------------
	var bugReasonId = {
		id : "bugReasonId",
		xtype : "textfield",
		fieldLabel : 'ID',
		value : '自动生成',
		width : wd,
		readOnly : true,
		name : 'bugReasonId'

	}
	var bugReasonDesc = {
		id : "bugReasonDesc",
		xtype : "textarea",
		fieldLabel : '故障原因',
		allowBlank : false,
		// readOnly : true,
		width : 400,
		height : 200,
		name : 'bugReason.bugReasonDesc'

	}
	var reasonform = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		title : '增加/修改故障原因信息',
		items : [bugReasonId, bugReasonDesc]

	});

	var reasonwin = new Ext.Window({
		width : 600,
		height : 400,
		buttonAlign : "center",
		modal:true,
		items : [reasonform],
		layout : 'fit',
		closeAction : 'hide',
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
				var myurl = "";
				// alert(currentNode.id);
				// alert(Ext.get("bugReasonId").dom.value);
				if (Ext.get("bugReasonId").dom.value == "自动生成") {
					myurl = "equbug/addBugReason.action?bugCode="
							+ currentNode.id;
				} else {
					myurl = "equbug/updateBugReason.action";
				}
				// alert(op);
				reasonform.form.submit({
					method : 'POST',
					url : myurl,
					success : function(form, action) {

						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("注意", o.msg);

						reasonwin.hide();
						reasonStore.baseParams = {
							bugId : currentNode.id,
							fuzzy : fuuzy.getValue()
						};
						reasonStore.load({
							params : {
								start : 0,
								limit : 10
							}
						});

					},
					faliue : function() {
						Ext.Msg.alert('错误', '出现未知错误.');
					}
				});
			}
		}, {
			text : '取消',
			iconCls : 'cancer',
			handler : function() {
				reasonwin.hide();
			}
		}]

	});

	function addreason() {
		if (currentNode.id != "0") {
			reasonform.getForm().reset();
			reasonwin.show();
			reasonform.setTitle("增加'" + currentNode.text + "'的原因");
		} else {
			alert('请选择故障');
		}

	}

	function updatereason() {
		if (gridreason.selModel.hasSelection()) {

			var records = gridreason.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				var record = gridreason.getSelectionModel().getSelected();

				reasonwin.show();
				reasonform.form.loadRecord(record);

			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}

	function deletereason() {
		var sm = gridreason.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {

			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.bugReasonId) {
					ids.push(member.bugReasonId);
				} else {

					store.remove(store.getAt(i));
				}
			}

			// alert(ids[0]);

			Ext.Msg.confirm("删除", "是否确定删除所选择的" + ids.length + "条记录？", function(
					buttonobj) {

				if (buttonobj == "yes") {

					Ext.lib.Ajax.request('POST',
							'equbug/deleteBugReason.action', {
								success : function(action) {
									Ext.Msg.alert("提示", "删除成功！")

									reasonStore.baseParams = {
										bugId : currentNode.id,
										fuzzy : fuuzy.getValue()
									};
									reasonStore.load({
										params : {
											start : 0,
											limit : 10
										}
									});

									gridsolution.setTitle("故障解决方案");
									querySolution();
									currentReasonId = "0";
								},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
							}, 'ids=' + ids);
				}

			});
		}

	};

	// -------------故障解决方案--------------
	// 定义grid 故障解决方案
	var MyRecord1 = Ext.data.Record.create([{
		name : 'equSolutionId'
	}, {
		name : 'equSolutionDesc'
	}, {
		name : 'memo'
	}, {
		name : 'fileName'
	}, {
		name : 'filePath'
	}]);

	var dataProxy1 = new Ext.data.HttpProxy(

			{
				url : 'equbug/findBugSolutionList.action'
			}

	);

	var theReader1 = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"

	}, MyRecord1);

	var solutionstore = new Ext.data.Store({

		proxy : dataProxy1,

		reader : theReader1

	});

	var solutionFuzzy = new Ext.form.TextField({
		id : "solutionFuzzy",
		name : "solutionFuzzy"
	});
	var mysm = new Ext.grid.CheckboxSelectionModel();

	var gridsolution = new Ext.grid.GridPanel({
		store : solutionstore,
		columns : [mysm, new Ext.grid.RowNumberer({
			width : 20,
			align : 'center'
		}), {
			header : "ID",
			sortable : true,
			dataIndex : 'equSolutionId',
			hidden : true
		}, {
			header : "解决方案描述",
			width : 280,
			sortable : true,
			dataIndex : 'equSolutionDesc'
		}, {
			header : "备注",
			width : 100,
			sortable : true,
			dataIndex : 'memo'
		}, {
			header : "文件名",
			width : 100,
			sortable : true,
			dataIndex : 'fileName'
		}, {
			header : "附件",
			sortable : true,
			dataIndex : 'filePath',
			renderer:function(v){
				if(v !=null && v !='')
				{ 
					var s =  '<a href="#" onclick="window.open(\''+v+'\');return  false;">[查看]</a>';
					return s;
				}
			} 
		} 
		],
		sm : mysm,
		title : '故障解决方案',

		tbar : [solutionFuzzy, {
			text : "查询",
			iconCls : 'query',
			handler : querySolution
		}, {
			text : "新增",
			iconCls : 'add',
			handler : addSolution
		}, {
			text : "修改",
			iconCls : 'update',
			handler : updateSolution
		}, {
			text : "删除",
			iconCls : 'delete',
			handler : deleteSolution
		}],
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : 10,
			store : solutionstore,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});

	function querySolution() {

		solutionstore.baseParams = {
			reasonId : currentReasonId,
			fuzzy : solutionFuzzy.getValue()
		};
		solutionstore.load({
			params : {
				start : 0,
				limit : 10
			}
		});

	}
	// -----------故障解决方案增加和修改页面
	var equSolutionId = {
		id : "equSolutionId",
		xtype : "textfield",
		fieldLabel : 'ID',
		value : '自动生成',
		width : wd,
		readOnly : true,
		name : 'equSolutionId'

	}
	var equSolutionDesc = {
		id : "equSolutionDesc",
		xtype : "textarea",
		fieldLabel : '故障解决措施',
		allowBlank : false,
		// readOnly : true,
		width : 400,
		height : 100,
		name : 'bugSolution.equSolutionDesc'

	}

	var memo = {
		id : "memo",
		xtype : "textarea",
		fieldLabel : '备注',
		width : 400,
		height : 100,
		name : 'bugSolution.memo'

	}

	var fileName = new Ext.form.TextField({
		id : "fileName",
	//	xtype : "textfield",
		inputType : "file",
		fieldLabel : '附件<a  style="cursor:hand;color:black" id="a_viewFileContent" >【查看】</a>',
		width : 300,
		name : 'solutionFile' //bugSolution.fileName
	});

	var solutionform = new Ext.FormPanel({
		frame : true,
		fileUpload : true,
		labelAlign : 'center',
		title : '增加/修改故障解决方案信息',
		items : [equSolutionId, equSolutionDesc, memo, fileName]
	});

	var solutionwin = new Ext.Window({
		width : 600,
		height : 400,
		buttonAlign : "center",
		modal:true,
		items : [solutionform],
		layout : 'fit',
		closeAction : 'hide',
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() { 
				var myurl = ""; 
				if (Ext.get("equSolutionId").dom.value == "自动生成") {
					myurl = "equbug/addSolution.action";
				} else {
					myurl = "equbug/updateSolution.action";
				}
				var myfiletype="";
				if(Ext.get("fileName").dom.value!="")
				{
				  myfiletype=Ext.get("fileName").dom.value;
				  myfiletype=myfiletype.substring(myfiletype.lastIndexOf(".")+1,myfiletype.length);
				}  
				solutionform.form.submit({
					method : 'POST',
					url : myurl,
					params:{
						'reasonId' : currentReasonId,
						'filepath':Ext.get("fileName").dom.value 
					},
					success : function(form, action) { 
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("注意", o.msg);
						if(o.msg.indexOf('成功')!=-1)
						{ 
							Ext.get("fileName").dom.select();   
                 			document.selection.clear();  
						} 
						solutionwin.hide();
						querySolution();

					},
					faliue : function() {
						Ext.Msg.alert('错误', '出现未知错误.');
					}
				});
			}
		}, {
			text : '取消',
			iconCls : 'cancer',
			handler : function() {
				solutionwin.hide();
			}
		}]

	});

	function addSolution() { 
		if (currentReasonId != "0") {
			solutionform.getForm().reset();
			solutionwin.show();
//			Ext.get("fileName").dom.select();   
//			document.selection.clear();  
			
            
			Ext.get("a_viewFileContent").dom.style.display = "none";

			solutionform.setTitle("增加原因ID为" + currentReasonId + "的解决方案");
		} else {
			alert("请选择故障原因");
		}
	}

	function updateSolution() {
		if (gridsolution.selModel.hasSelection()) {

			var records = gridsolution.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				var record = gridsolution.getSelectionModel().getSelected();
				var url = "";
				if (record.get("filePath") != "") {  
					url = record.get("filePath");
				} 
				solutionwin.show();
				Ext.get("a_viewFileContent").dom.style.display = "";
				if (url != "") {
					Ext.get("a_viewFileContent").dom.style.color = "red";
					Ext.get("a_viewFileContent").dom.onclick = function() {
						window.open(url);
					}
				} else {
					Ext.get("a_viewFileContent").dom.style.color = "black";
					Ext.get("a_viewFileContent").dom.onclick = function() {
						alert("没有附件");
					}

				}
				solutionform.getForm().loadRecord(record);
//				Ext.get("fileName").dom.select();   
//			    document.selection.clear();  
				// window.open(url);

			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}

	}
	function deleteSolution() {
		var sm = gridsolution.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {

			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.equSolutionId) {
					ids.push(member.equSolutionId);
				} else {

					store.remove(store.getAt(i));
				}
			}
   

			Ext.Msg.confirm("删除", "是否确定删除所选的" + ids.length + "条记录？", function(
					buttonobj) {

				if (buttonobj == "yes") {

					Ext.lib.Ajax.request('POST',
							'equbug/deleteSolution.action', {
								success : function(action) {
									Ext.Msg.alert("提示", "删除成功！")

									querySolution();
								},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
							}, 'ids=' + ids);
				}
			});
		}

	}
	// ------------------------------------
	//-------------add 故障列表----------------
	var MyRecord = Ext.data.Record.create([
	{name : 'bugId'},
    {name : 'bugCode'},
	{name : 'bugName'}
	]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url:'equbug/findBugListByName.action'
			}

	);

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"

	}, MyRecord);

	var store = new Ext.data.Store({

		proxy : dataProxy,

		reader : theReader

	});
//分页
	store.load({
			params : {
				start : 0,
				limit : 12		
			}
		});
	

	var sm = new Ext.grid.CheckboxSelectionModel();
   var bugFuzzy = new Ext.form.TextField({
		id : "bugFuzzy",
		name : "bugFuzzy"
	});
	var grid = new Ext.grid.GridPanel({
		store : store,
		columns : [
		sm, { 
			header : "ID",
			sortable : true,
			dataIndex : 'bugId',
			hidden:true
		}, 
		{
			header : "故障编码",
			sortable : true,
			dataIndex : 'bugCode',
			align:'center',
			hidden:true
		}, 
		{
			header : "故障名称",
			width : 200,
			sortable : true,
			dataIndex : 'bugName',
			align:'center'
		}
		],
		sm : sm,
		tbar : [
		bugFuzzy, {
			text : "查询",
			 iconCls : 'query',
			handler : queryRecord
		},{
			text:'删除',
			 iconCls : 'delete',
			 handler:deleteRecord
		}],	
		bbar : new Ext.PagingToolbar({
			pageSize : 12,
			store : store,
			displayInfo : true,
			displayMsg : "",
			emptyMsg : ""
		})
	});
	grid.on("dblclick", updateRecord);
	grid.addListener('rowClick', gridrowClick);
	function gridrowClick(grid, rowIndex, e) {
			var record = grid.getStore().getAt(rowIndex);
		var bugId = record.get("bugId");
		var bugName = record.get("bugName");
		gridreason.setTitle(bugName+ "----对应的故障原因");
		currentNode.id=bugId;
		currentNode.text=bugName;
		currentReasonId = "0";

		reasonStore.baseParams = {
			bugId : bugId,
			fuzzy : '%'
		};
		reasonStore.load({
			params : {
				start : 0,
				limit : 10
			}
		});
		gridsolution.setTitle("解决方案");
		querySolution();
	}

	function queryRecord() {
		var fuzzytext = bugFuzzy.getValue();
		store.baseParams = {
			fuzzy : fuzzytext
		};
		store.load({
			params : {
				start : 0,
				limit : 12
			}
		});
	}
	
	function updateRecord()
	{
			if (grid.selModel.hasSelection()) {
		
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				var record = grid.getSelectionModel().getSelected();
			      currentNode.id=record.get("bugId");
			      currentNode.text=record.get("bugName");
					myaddpanel.getForm().reset();
					win.show();
					myaddpanel.setTitle("修改'" + currentNode.text + "'基本信息");
					bugCode.setDisabled(true);
					Ext.get("bugId").dom.value = currentNode.id;
					Ext.Ajax.request({
						url : 'equbug/findBugById.action',
						params : {
							id : currentNode.id
						},
						method : 'post',
						waitMsg : '正在加载数据...',
						success : function(result, request) {
							var json = eval('(' + result.responseText + ')');

							var obj = json.data;
							Ext.get("bugCode").dom.value = obj.bugCode;
							Ext.get("bugId").dom.value = obj.bugId;
							Ext.get("bugDesc").dom.value = obj.bugDesc;
							Ext.get("PBugId").dom.value = obj.PBugId;
							Ext.get("bugName").dom.value = obj.bugName;
							ifLeaf.setValue(obj.ifLeaf);
							if (obj.entryBy != null) {
								Ext.get("entryBy").dom.value = json.workName;
							}
							if (obj.entryDate != null) {
								Ext.get("entryDate").dom.value = obj.entryDate
										.substring(0, 10);
							}

						},
						failure : function(result, request) {
							Ext.MessageBox.alert('错误', '操作失败!');
						}
					});
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
		
		
			        
	}
	
	function deleteRecord()
	{
			var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		var names=[];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {

			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.bugId) {
					ids.push(member.bugId); 
					names.push(member.bugName);
				} else {
					
					store.remove(store.getAt(i));
				}
			}
			
			Ext.Msg.confirm("删除", "是否确定删除名称为'" + names + "'的记录？", function(
					buttonobj) {

				if (buttonobj == "yes") {
					Ext.Ajax.request({
										url : 'equbug/deleteBugInfo.action',
										params : {
											id : ids
										},
										method : 'post',
										waitMsg : '正在加载数据...',
										success : function(result, request) {
											var json = eval('('
													+ result.responseText + ')');
											Ext.Msg.alert("注意", json.msg);
											if(json.msg=="删除成功！")
											{
												store.reload();
												currentNode.id = "0";
											currentNode.text = "故障树";
											currentReasonId = "0";
											gridreason.setTitle("故障原因");
											queryreason();
											gridsolution.setTitle("故障解决方案");
											querySolution();
											root.reload();
										
											}
											
										},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
					})

				}
					})

		}
	}
	
	//---------------------------------------
	// ----------布局---------------------


	var right = new Ext.Panel({
		region : "center",
		layout : 'border',
		items : [{
			region : 'north',
			height : 200,
			items : [gridreason]
		}, {
			region : 'center',
			layout : 'fit',
			items : [gridsolution]
		}]

	});
	
	
	
	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [ {
							region : 'west',
							split : true,
							width : 250,
							layout : 'fit',
							minSize : 175,
							maxSize : 600,
							margins : '0 0 0 0',
							collapsible : true,
							border : false,
							//autoScroll : true,
							items : [new Ext.TabPanel({
							tabPosition : 'bottom',
										activeTab : 0,
										layoutOnTabChange : true,
										items:[{title : '列表方式',
												    border : false,
													autoScroll : true,
													layout : 'border',
													items : [{
													region : 'center',
													layout : 'fit',
													items : [grid]
												}]
												},{
													title : '树形方式',
													border : false,
													autoScroll : true,
													items : [mytree]
												}]
							})] 
						}, right// 初始标签页
				]
			}); 
});
