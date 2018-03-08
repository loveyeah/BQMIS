Ext.onReady(function() {
	var btnView = new Ext.Button({
				id : 'btnView',
				text : '查看',
				handler : function() {
					window.open(bview);
				}
			});
/*function openAnnex(url) {
	
	window.open(url);
}*/
	function renderMoney(v) {
		return renderNumber(v, 2);// 修改计算金额现在2位小数
	}

	function getYear() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10);
		return s;
	}

	function renderNumber(v, argDecimal) {
		if (v) {
			if (typeof argDecimal != 'number') {
				argDecimal = 2;
			}
			v = Number(v).toFixed(argDecimal);
			var t = '';
			v = String(v);
			while ((t = v.replace(/^(-?[0-9]+)([0-9]{3}.*)$/, '$1,$2')) !== v)
				v = t;
			return v;
		} else
			return '';
	}

	var year = new Ext.form.TextField({
		style : 'cursor:pointer',
		name : 'time',
		fieldLabel : '计划时间',
		readOnly : true,
		anchor : "80%",
		// value : getYear(),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y',
					alwaysUseStartDate : false,
					dateFmt : 'yyyy',
					isShowClear : true,
					onpicked : function(v) {
						query();
						this.blur();
					}
				});
			}
		}
	});
	// 项目类别
	var prjTypeName = new Ext.ux.ComboBoxTree({
		fieldLabel : "项目类别<font color='red'>*</font>",
		displayField : 'text',
		valueField : 'id',
		id : 'prjTypeName',
		allowBlank : false,
		blankText : '请选择',
		emptyText : '请选择',
		readOnly : true,
		anchor : "75%",
		tree : {
			xtype : 'treepanel',
			rootVisible : false,
			loader : new Ext.tree.TreeLoader({
				dataUrl : 'manageproject/findByPId.action'
			}),
			root : new Ext.tree.AsyncTreeNode({
				id : '0',
				name : '灞桥电厂',
				text : '灞桥电厂'
			})
		},
		selectNodeModel : 'all',
		listeners : {
			'select' : function(combo, record, index) {
				query();
			}
		}
	});

	var sm = new Ext.grid.CheckboxSelectionModel();
	// grid列表数据源

	var Record = new Ext.data.Record.create([sm, {
		name : 'prjId',
		mapping : 0
	}, {
		name : 'prjNo',
		mapping : 1
	}, {
		name : 'prjName',
		mapping : 2
	}, {
		name : 'prjDept',
		mapping : 3
	}, {
		name : 'prjDeptName',
		mapping : 4
	}, {
		name : 'prjTypeId',
		mapping : 5
	}, {
		name : 'applyFunds',
		mapping : 6
	}, {
		name : 'approvedFunds',
		mapping : 7
	}, {
		name : 'isFundsFinish',
		mapping : 8
	}, {
		name : 'prjYear',
		mapping : 9
	}, {
		name : 'duration',
		mapping : 10
	}, {
		name : 'prjBy',
		mapping : 14
	}, {
		name : 'prjByName',
		mapping : 15
	}, {
		name : 'prjTypeName',
		mapping : 16
	},{
		name : 'filePath',
		mapping : 17
	},{
		name : 'statusId',
		mapping : 18
	}]);

	var gridTbar = new Ext.Toolbar({
		items : ['年度', year, '-', '项目类别', prjTypeName, '-', {
			id : 'query',
			text : "查询",
			iconCls : 'query',
			handler : query
		}, '-', {
			id : 'add',
			text : "增加",
			iconCls : 'add',
			handler : addRec
		}, '-', {
			id : 'delete',
			text : "删除",
			iconCls : 'delete',
			handler : deleteRec
		}, '-', {
			id : 'save',
			text : "保存",
			iconCls : 'save',
			handler : saveRec
		}, '-', {
			id : 'cancer',
			text : "取消",
			iconCls : 'cancer',
			handler : cancer
		}, '-', {
			//modify by ypan 20100909
			id : 'extraCommit',
			text : "上传附件",
		    iconCls : 'upcommit',
			handler : extraCommit
		}]
	});
     //add by ypan 20100909
	function confirm() {
		var sm = prjgrid.getSelectionModel();
		var selected = sm.getSelections();
		var menber = selected[0];
		var msg = "";
		var modifyRec = prjgrid.getStore().getModifiedRecords();
		if (modifyRec != null && modifyRec.length != 0) {
			Ext.Msg.alert('提示', "数据已改变，请先保存！");
			return false;
		}
		if (menber.get("prjId") == null
				|| menber.get("prjId") == "") {
			msg = "请先保存新增的记录!" + "<br /><br />";
		}
		if (msg != "") {
			Ext.Msg.alert('提示', "" + msg + "");
			return false;
		}
		return true;
	}
	
	function extraCommit(){
		/*filewin.show();
		fileform.getForm().reset();*/
	   var sm = prjgrid.getSelectionModel();
			var selected = sm.getSelections();
			var menber = selected[0];
			if (prjgrid.selModel.hasSelection()) {
				if (confirm() == false)
					return;
				if (selected.length != 1) {
					Ext.Msg.alert("提示", "请选择一条记录！");
					return;
				}
				var fileAddr = menber.get('filePath');
				filewin.show();
				fileform.getForm().reset();
				
			} else {
				Ext.Msg.alert("提示", "请先选择要编辑的行!");
			}	
	}
	 
	var fileName = new Ext.form.TextField({
		id : "fileName",
		// xtype : "textfield",
		inputType : "file",
		fieldLabel : '附件<a  style="cursor:hand;color:black" id="a_viewFileContent" >【查看】</a>',
		width : 300,
		name : 'solutionFile'
	});

	var fileform = new Ext.FormPanel({
		frame : true,
		fileUpload : true,
		labelAlign : 'center',
		items : [fileName]
	});
	
		var filewin = new Ext.Window({
		title : '上传附件',
		id : 'win',
		modal : true,
		autoHeight : true,
		width : 450,
		closeAction : 'hide',
		items : [fileform],
		buttonAlign : 'center',
		buttons : [{
			text : '保存',
			handler : function() {
				var addr = Ext.get("fileName").dom.value;
				if (addr == null || addr == '') {
					Ext.Msg.alert('提示', '无附件可上传！');
					return;
				}
				var myurl = "manageproject/extraCommit.action";
				var prjId = prjgrid.getSelectionModel().getSelected()
						.get('prjId');
				fileform.form.submit({
					method : 'POST',
					url : myurl,
					params : {
						'prjId' : prjId,
						'filepath' : Ext.get("fileName").dom.value
					},
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("注意", '附件上传成功！');
						if (o && o.msg && o.msg.indexOf('成功') != -1) {
							Ext.get("fileName").dom.select();
							document.selection.clear();
						}
						filewin.hide();
						
						query();

					},
					faliue : function() {
						Ext.Msg.alert('错误', '出现未知错误.');
					}
				});
			}
		}, {
			text : '取消',
			handler : function() {
				filewin.hide();
			}
		}]
	});
	
	function cancer() {
		store.rejectChanges();
		query();
	}

	function query() {
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		})
	}

	function addLine() {
		// 统计行
		var o = new Record({
			'prjId' : '',
			'prjNo' : '',
			'statusId' : '',		
			'prjName' : '',
			'prjDept' : '',
			'prjDeptName' : '',
			'prjTypeId' : '',
			'applyFunds' : '',
			'approvedFunds' : '',
			'isFundsFinish' : '',
			'prjYear' : '',
			'duration' : '',
			'prjBy' : '',
			'prjByName' : '',
			'prjTypeName' : '',
			'isNewRecord' : 'total'
		});
		var count = store.getCount();
		var currentIndex = count;
		prjgrid.stopEditing();
		store.insert(currentIndex, o);
		prjgrid.getView().refresh();
	}

	var btnQuery = new Ext.Button({
		text : '查询',
		iconCls : 'query',
		handler : query
	});

	function addRec() {
		var count = store.getCount();
		var currentIndex = count;
		var o = new Record({
			'prjId' : '',
			'prjNo' : '',
			'statusId' : '',
			'prjName' : '',
			'prjDept' : '',
			'prjDeptName' : '',
			'prjTypeId' : '',
			'applyFunds' : '',
			'approvedFunds' : '',
			'isFundsFinish' : '',
			'prjYear' : '',
			'duration' : '',
			'prjBy' : '',
			'prjByName' : '',
			'prjTypeName' : ''

		});
		prjgrid.stopEditing();
		store.insert(count - 1, o);
		sm.selectRow(currentIndex);
		prjgrid.startEditing(currentIndex, 1);
		prjgrid.getView().refresh();
	}

	function saveRec() {
		var alertMsg = "";
		// prjgrid.stopEditing();
		var modifyRec = prjgrid.getStore().getModifiedRecords();
		if (modifyRec.length == 0) {
			Ext.MessageBox.alert('提示信息', '未做任何修改！');
			query();
			return;
		}
		Ext.Msg.confirm('提示', '确定要保存修改数据吗?', function(button) {
			if (button == 'yes') {
				var updateData = new Array();
				for (var i = 0; i < modifyRec.length; i++) {
					if (modifyRec[i].data.isNewRecord == "total") {
						continue;
					} else {
						if (modifyRec[i].get("prjNo") == null
								|| modifyRec[i].get("prjNo") == "") {
							alertMsg += "项目编号不能为空</br>";
						}
						if (modifyRec[i].get("prjName") == null
								|| modifyRec[i].get("prjName") == "") {
							alertMsg += "项目名称不能为空</br>";
						}
						if (modifyRec[i].get("prjDeptName") == null
								|| modifyRec[i].get("prjDeptName") == "") {
							alertMsg += "立项部门不能为空</br>";
						}
						if (modifyRec[i].get("prjTypeId") == null
								|| modifyRec[i].get("prjTypeId") == "") {
							alertMsg += "项目类别不能为空</br>";
						}
						if (modifyRec[i].get("isFundsFinish") == null
								|| modifyRec[i].get("isFundsFinish") == "") {
							alertMsg += "是否已落实资金不能为空</br>";
						}
						if (modifyRec[i].get("prjYear") == null
								|| modifyRec[i].get("prjYear") == "") {
							alertMsg += "项目年份不能为空</br>";
						}

						if (alertMsg != "") {
							Ext.Msg.alert("提示", alertMsg);
							return;
						}
						updateData.push(modifyRec[i].data);
					}
				}
				Ext.Ajax.request({
					url : 'manageproject/savePrjRegister.action',
					method : 'post',
					params : {
						isUpdate : Ext.util.JSON.encode(updateData)
					},
					success : function(form, options) {
						var obj = Ext.util.JSON.decode(form.responseText)
						Ext.MessageBox.alert('提示信息', '保存成功！')
						store.rejectChanges();
						modifyRec = [];
						query();
					},
					failure : function(result, request) {
						Ext.MessageBox.alert('提示信息', '操作失败！')
					}
				})
			}
		})
	}
	function deleteRec() {
		var sm = prjgrid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		var names = [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
			query();
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.prjId) {
					ids.push(member.prjId);
				}
			}
			if (ids.length > 0) {
				Ext.Msg.confirm("删除", "是否确定删除所选记录？", function(buttonobj) {
					if (buttonobj == "yes") {
						Ext.lib.Ajax.request('POST',
								'manageproject/delPrjRegister.action', {
									success : function(action) {
										Ext.Msg.alert("提示", "删除成功！")
										store.reload();
										query();
									},
									failure : function() {
										Ext.Msg.alert('错误', '删除时出现未知错误.');
									}
								}, 'ids=' + ids);
					} else {
						store.reload();
					}
				});
			} else {
				query();
			}
		}
	}

	var store = new Ext.data.JsonStore({
		url : 'manageproject/getPrjRegister.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : Record
	});

	// 页面的Grid主体
	var prjgrid = new Ext.grid.EditorGridPanel({
		store : store,
		layout : 'fit',
		columns : [sm, new Ext.grid.RowNumberer({
			header : '行号',
			width : 35
		}),	
		 // 选择框
				{
					header : '项目编号',
					dataIndex : 'prjNo',
					align : 'left',
					width : 100,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							var prjNo = record.data.prjNo;
							// 强行触发renderer事件
							return prjNo;
						} else {

							if (record.get("isNewRecord") == "total") {

								return "<font color='red'>" + "合计" + "</font>";
							}
						}

					},
					editor : new Ext.form.TextField({
						allowBlank : false,
						id : 'prjNo'
					})
				}, {
					header : '状态',
					dataIndex : 'statusId',
					align : 'left',
					width : 100
				},{
					header : '项目名称',
					dataIndex : 'prjName',
					align : 'left',
					width : 100,
					editor : new Ext.form.TextField({
						allowBlank : false,
						id : 'prjName'
					})
				}, {
					header : "立项部门",
					width : 100,
					align : 'center',
					dataIndex : 'prjDeptName',
					editor : new Ext.form.ComboBox({
						fieldLabel : '立项部门',
						value : "请选择...",
						mode : 'remote',
						editable : false,
						readOnly : true,
						width : 100,
						onTriggerClick : function() {
							var args = {
								selectModel : 'single',
								rootNode : {
									id : "0",
									text : '灞桥热电厂'
								}
							}
							var url = "/power/comm/jsp/hr/workerByDept/workerByDept2.jsp";
							var rvo = window
									.showModalDialog(
											url,
											args,
											'dialogWidth:600px;dialogHeight:600px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
							if (typeof(rvo) != "undefined") {

								var record = prjgrid.getSelectionModel()
										.getSelected();

								record.set("prjDept", rvo.deptCode);
								record.set("prjDeptName", rvo.deptName);
								record.set("prjBy", rvo.workerCode);
								record.set("prjByName", rvo.workerName);

							}
						}
					})
				}, {
					header : '立项人',
					dataIndex : 'prjByName',
					align : 'left',
					width : 100
				}, {
					header : '项目类别',
					dataIndex : 'prjTypeId',
					align : 'left',
					width : 100,
					allowBlank : false,
					editor : new Ext.form.TextField({
						width : 100,
						displayField : 'text',
						valueField : 'id',
						listeners : {
							focus : function() {
								var args = {
									selectModel : 'single',
									rootNode : {
										id : "0",
										text : '项目类别树'
									},
									onlyLeaf : false
								};
								this.blur();
								var win = window
										.showModalDialog(
												'prjTypeTree.jsp',
												args,
												'dialogWidth:'
														+ Constants.WIDTH_COM_EMPLOYEE
														+ 'px;dialogHeight:'
														+ Constants.HEIGHT_COM_EMPLOYEE
														+ 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
								if (typeof(win) != "undefined") {
									var record = prjgrid.getSelectionModel()
											.getSelected();

									record.set("prjTypeName", win.name);
									record.set("prjTypeId", win.id);

								}
							}
						}
					}),
					renderer : function(value, meta, record) {
						if (value != null) {
							return record.get("prjTypeName")
						}
					}
				}, {
					header : '申请资金',
					dataIndex : 'applyFunds',
					align : 'left',
					width : 100,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							var applyFunds = record.data.applyFunds;
							// 强行触发renderer事件
							var totalSum = 0;
							return applyFunds;
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('applyFunds');
							}
							return "<font color='red'>" + renderMoney(totalSum)
									+ "</font>";
						}
					},
					editor : new Ext.form.NumberField({
						allowBlank : false,
						id : 'applyFunds'
					})
				}, {
					header : '已审批资金',
					dataIndex : 'approvedFunds',
					align : 'left',
					width : 100,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							var approvedFunds = record.data.approvedFunds;
							// 强行触发renderer事件
							var totalSum = 0;
							return approvedFunds;
						} else {

							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('approvedFunds');
							}
							return "<font color='red'>" + renderMoney(totalSum)
									+ "</font>";
						}

					},
					editor : new Ext.form.NumberField({
						allowBlank : false,
						id : 'approvedFunds'
					})
				}, {
					header : '是否已落实资金',
					dataIndex : 'isFundsFinish',
					width : 100,
					align : 'center',
					renderer : function(val) {
						if (val == "Y")
							return "是";
						else if (val == "N") {
							return "否";
						}
					},

					editor : new Ext.form.ComboBox({
						readOnly : true,
						name : 'isFundsFinish',
						hiddenName : 'isFundsFinish',
						mode : 'local',
						width : 70,
						value : "1",
						fieldLabel : '完成情况',
						triggerAction : 'all',
						store : new Ext.data.SimpleStore({
							fields : ['name', 'value'],
							data : [['是', 'Y'], ['否', 'N']]
						}),
						valueField : 'value',
						displayField : 'name',
						anchor : "15%"

					})
				}, {
					header : '项目年份',
					dataIndex : 'prjYear',
					readOnly : true,
					width : 120,
					align : 'center',
					editor : new Ext.form.TextField({
						allowBlank : false,
						style : 'cursor:pointer',
						readOnly : true,
						listeners : {
							focus : function() {
								WdatePicker({
									// 时间格式
									startDate : '%y ',
									dateFmt : 'yyyy',
									alwaysUseStartDate : false,
									onpicked : function() {
										prjgrid.getSelectionModel()
												.getSelected().set("prjYear",
														this.value)

									}

								});

							}
						}
					})

				}, {
					header : '工期',
					dataIndex : 'duration',
					align : 'center',
					width : 80,
					editor : new Ext.form.TextField({
						id : 'duration',
						readOnly : false
					})
				},{
					header : "附件",
					sortable : true,
					dataIndex : 'filePath',
					//update by sychen 20100915
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (v != null && v != '') {
							var s = '<a href="#" onclick="window.open(\'' + v
									+ '\');return  false;">[查看]</a>';
							return s;
						} else {
							if (record.get("isNewRecord") == "total") {
								return '';
							} else {
								return '没有附件';
							}
						}
					}
//					renderer : function(v) {
//						if(v !=null && v !='')
//								{ 
//									var s =  '<a href="#" onclick="window.open(\''+v+'\');return  false;">[查看]</a>';
//									return s;
//								}else{
//									return '没有附件';
//								}
//						}
					
					//update by sychen 20100915 end 
					}
				],

		sm : sm, // 选择框的选择
		tbar : gridTbar,
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "{0}到{1}条，共{2}条",
			emptyMsg : "没有记录",
			beforePageText : '页',
			afterPageText : "共{0}"
		}),
		clicksToEdit : 1,
		viewConfig : {
			forceFit : true
		}
	});

	store.on("beforeload", function() {
		Ext.apply(this.baseParams, {
			year : year.getValue(),
			prjType : prjTypeName.getValue(),
			flag:'register'
		});
	});
	store.on("load",addLine);

	query();
	prjgrid.on('beforeedit', function(e) {
		if (e.record.get("isNewRecord") == "total") {
			return false;
		}
	})

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [prjgrid]
	});

})