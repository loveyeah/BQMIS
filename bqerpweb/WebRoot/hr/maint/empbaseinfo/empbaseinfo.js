Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();

	Ext.form.TextField.prototype.width = 180;
	var twiceWd = 453;
	var twoWd = 290;
	var height = 100;
	var _empId = null;

	var root = new Ext.tree.AsyncTreeNode({
				text : '大唐灞桥电厂',
				isRoot : true,
				id : '0'
			});

	// 定义加载器

	var treeLoader = new Ext.tree.TreeLoader({
				dataUrl : 'hr/getDeptEmpTreeList.action'
			})

	// 定义部门人员树
	var tree = new Ext.tree.TreePanel({
				autoScroll : true,
				containerScroll : true,
				collapsible : true,
				split : true,
				border : false,
				rootVisible : false,
				root : root,
				animate : true,
				enableDD : false,
				loader : treeLoader

			});

	tree.on("click", treeClick);
	tree.setRootNode(root);
	root.select();

	/**
	 * 点击树时
	 */
	
	function treeClick(node, e) {
		 e.stopEvent();
		if (node.isLeaf()) {
	      
			formPanel.getForm().trim();
			if (formPanel.getForm().isDirty()) 
			{
				// Ext.MessageBox.confirm(Constants.CONFIRM,
				// Constants.COM_C_004,
				// function(buttonobj) {
				// if (buttonobj == "yes") {
				findByEmpId(node.id);
				_empId = node.id;
				// }
				// })
			} else {
				findByEmpId(node.id);
				_empId = node.id;
			}
		}else
		{ 
		   
			Ext.MessageBox.alert("提示","请选择叶子节点(人员)！");//modify by wpzhu 20100730
			return  ;
		}
	}
	
	
	

	/**
	 * grid 人员列表 add by bjxu 100701
	 */
	var MyRecord = Ext.data.Record.create([{
				name : 'empId',
				mapping : 0
			}, {
				name : 'empCode',
				mapping : 1
			}, {
				name : 'chsName',
				mapping : 2
			}, {
				name : 'deptId',
				mapping : 3
			}, {
				name : 'deptCode',
				mapping : 4
			}, {
				name : 'deptName',
				mapping : 5
			}, {
				name : 'newEmpCode',
				mapping : 6
			}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'hr/getEmpListByFilter.action'
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
	// 分页
	store.load({
				params : {
					start : 0,
					limit : 18
				}
			});
	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	var fuzzy = new Ext.form.TextField({
				id : "fuzzy",
				width : 100,
				name : "fuzzy"
			});
	var grid = new Ext.grid.GridPanel({
				store : store,
				columns : [sm, {
							header : "员工编码",
							hidden : true,
							sortable : true,
							width : 75,
							dataIndex : 'empCode',
							align : 'center'
						}, {
							header : "员工编码",
							sortable : true,
							width : 75,
							dataIndex : 'newEmpCode',
							align : 'center'
						}, {
							header : "员工姓名",
							sortable : true,
							width : 80,
							dataIndex : 'chsName',
							align : 'center'
						}, {
							header : "部门",
							sortable : true,
							dataIndex : 'deptName',
							align : 'center'
						}],
				sm : sm,
				tbar : [fuzzy, {
							text : "查询",
							iconCls : 'query',
							handler : queryRecord
						}],
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : store,
							displayInfo : true,
							displayMsg : "",
							emptyMsg : ""
						})
			});
	function queryRecord() {
		var fuzzytext = fuzzy.getValue();
		store.baseParams = {
			fuzzy : fuzzytext
		};
		store.load({
					params : {
						start : 0,
						limit : 18
					}
				});
	}
	grid.on('rowdblclick', function() {
				var rec = grid.getSelectionModel().getSelected();
				findByEmpId(rec.get("empId"))
				_empId = rec.get("empId");
		})
	/**
	 * 根据员工Id查找人事信息
	 */
	function findByEmpId(empId) {
		Ext.Ajax.request({
			url : 'hr/getEmpMaintBaseInfo.action',
			method : Constants.POST,
			params : {
				empId : empId
			},
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				formPanel.getForm().reset();
				if (result.empCode != null) {
					Ext.get('newEmpCode').dom.value = result.empCode;
				}
				if (result.chsName != null) {
					Ext.get('chsName').dom.value = result.chsName;
				}
				if (result.sex != null) {
					if (result.sex == 'W') {
						Ext.get('sex').dom.value = '女';
					} else {
						Ext.get('sex').dom.value = '男';
					}
				}
				if (result.brithday != null) {
					Ext.get('brithday').dom.value = result.brithday;
					tfAge.setValue(getAge(Ext.get('brithday').dom.value));
				} else {
					Ext.get('brithday').dom.value = "";
				}
				if (result.politicsName != null) {
					Ext.getCmp('politicsId').setValue(result.politicsId);
					Ext.form.ComboBox.superclass.setValue.call(Ext
									.getCmp('politicsId'), result.politicsName);

				}
				if (result.deptName != null) {
					Ext.get('deptId').dom.value = result.deptId;
					Ext.get('deptName').dom.value = result.deptName;
					//add by wpzhu 20100722
					stationStore.load({
						params : {
							deptIdForm : result.deptId
						},
						callback : function() {
							stationStore.insert(0, new stationData({
												stationName : '',
												stationId : ''
											}));
						}
					})
				}
				//------------------------------------------------------
				if (result.stationId != null) {
					Ext.getCmp('stationId').setValue(result.stationId);
					Ext.form.ComboBox.superclass.setValue.call(Ext
									.getCmp('stationId'), result.stationName);
				}
				if (result.workDate != null) {
					Ext.get('workDate').dom.value = result.workDate;
					workAge.setValue(getWorkAge(Ext.get('workDate').dom.value));
				} else {
					Ext.get('workDate').dom.value = "";
				}
				if (result.missionDate != null) {
					Ext.get('missionDate').dom.value = result.missionDate;
				} else {
					Ext.get('missionDate').dom.value = "";
				}
				if (result.isSpecialTrades != null) {
					if (result.isSpecialTrades == 'Y') {
						Ext.get('isSpecialTrades').dom.value = '是';
					} else {
						Ext.get('isSpecialTrades').dom.value = '否';
					}
				}
				if (result.isCadres != null) {
					if (result.isCadres == 'Y') {
						Ext.get('isCadres').dom.value = '是';
					} else {
						Ext.get('isCadres').dom.value = '否';
					}
				}
			},
			failure : function(result, request) {
				Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
			}
		});
	}

	// 员工工号
	var tfEmpCode = new Ext.form.CodeField({
				id : 'newEmpCode',
				// name : 'emp.empCode',
				name : 'emp.newEmpCode',
				fieldLabel : "员工工号",
				readOnly : true,
//				width : 150,
				anchor : '100%',
				maxLength : 20
			});

	// 员工姓名
	var tfChsName = new Ext.form.TextField({
				id : 'chsName',
				name : 'emp.chsName',
//				width : 150,
				anchor : '100%',
				fieldLabel : "员工姓名"
			});

	// 性别
	var cbSex = new Ext.form.CmbHRCode({
				id : "sex",
				hiddenName : 'emp.sex',
				fieldLabel : '性别',
//				width : 150,
				anchor : '100%',
				selectOnFocus : true,
				type : '性别'
			});

	// 出生日期
	var tfBrithday = new Ext.form.TextField({
				id : 'brithday',
				name : 'emp.brithday',
				style : 'cursor:pointer',
//				width : 150,
				anchor : '100%',
				fieldLabel : "出生日期",
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									onpicked : function() {
										tfAge.setValue(getAge(this.value));
									},
									onclearing : function() {
										tfAge.setValue('');
									}
								});
					}
				}
			});

	// 年龄
	var tfAge = new Ext.form.TextField({
				id : 'age',
				fieldLabel : "年龄",
//				width : 150,
				anchor : '100%',
				style : 'text-align: right;',
				readOnly : true
			});

	var workAge = new Ext.form.TextField({
				id : 'workAge',
//				width : 150,
				anchor : '100%',
				fieldLabel : "工龄",
				style : 'text-align: right;',
				readOnly : true
			});

	// 政治面貌
	var cbPoliticsId = new Ext.form.CmbHRBussiness({
				id : "politicsId",
				hiddenName : 'emp.politicsId',
				fieldLabel : '政治面貌',
//				width : 150,
				anchor : '100%',
				selectOnFocus : true,
				type : '政治面貌'
			});

	var panel1 = new Ext.Panel({
				border : false,
				items : [{
							layout : "column",
							border : false,
							items : [{
										columnWidth : 0.66,
										layout : "form",
										border : false,
										items : [{
													layout : "column",
													border : false,
													items : [{
																columnWidth : 0.5,
																layout : "form",
																border : false,
																items : [tfEmpCode]
															}, {
																columnWidth : 0.5,
																layout : "form",
																border : false,
																items : [tfChsName]
															}]
												}, {
													layout : "column",
													border : false,
													items : [{
																columnWidth : 0.5,
																layout : "form",
																border : false,
																items : [tfBrithday]
															}, {
																columnWidth : 0.5,
																layout : "form",
																border : false,
																items : [tfAge]
															}]
												}, {
													layout : "column",
													border : false,
													items : [{
																columnWidth : 0.5,
																layout : "form",
																border : false,
																items : [cbSex]
															}, {
																columnWidth : 0.5,
																layout : "form",
																border : false,
																items : [workAge]
															}]
												}, {
													layout : "column",
													border : false,
													items : [{
																columnWidth : 0.5,
																layout : "form",
																border : false,
																items : [cbPoliticsId]
															}]
												}]
									}]
						}]
			});

	var deptTxt = new Ext.form.TextField({
				id : 'deptName',
				fieldLabel : '所属部门',
//				width : 150,
				anchor : '100%',
				allowBlank : false,
				readOnly : true
			});

	// 所属部门ID
	var hiddenDeptTxt = new Ext.form.Hidden({
				id : 'deptId',
				name : 'emp.newDeptid'
			});

	deptTxt.onClick(deptSelect);

	var stationData = Ext.data.Record.create([{
				name : 'stationName'
			}, {
				name : 'stationId'
			}]);
	var stationStore = new Ext.data.JsonStore({
				url : 'hr/getStationByDeptNewEmployee.action',
				root : 'list',
				fields : stationData
			});
	var tfStationName = new Ext.form.ComboBox({
				id : 'stationId',
				hiddenName : 'emp.stationId',
				fieldLabel : '工作岗位',
//				width : 150,
				anchor : '100%',
				triggerAction : 'all',
				store : stationStore,
				displayField : 'stationName',
				valueField : 'stationId',
				mode : 'local',
				readOnly : true
			});

	// 参加工作日期
	var tfWorkDate = new Ext.form.TextField({
				id : 'workDate',
				name : 'emp.workDate',
				style : 'cursor:pointer',
//				width : 150,
				anchor : '100%',
				fieldLabel : "参加工作日期",
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									onpicked : function() {
										workAge
												.setValue(getWorkAge(this.value));
									},
									onclearing : function() {
										workAge.setValue('');
									}
								});
					}
				}
			});

	// 进厂日期
	var tfMissionDate = new Ext.form.TextField({
				id : 'missionDate',
				name : 'emp.missionDate',
				style : 'cursor:pointer',
				fieldLabel : "进厂日期",
//				width : 150,
				anchor : '100%',
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd'
								});
					}
				}
			});

	var isSpecialTrades = new Ext.form.ComboBox({
				fieldLabel : '是否特殊工种',
//				width : 150,
				anchor : '100%',
				store : new Ext.data.SimpleStore({
							fields : ['value', 'text'],
							data : [['Y', '是'], ['N', '否']]
						}),
				id : 'isSpecialTrades',
				name : 'isSpecialTrades',
				valueField : "value",
				displayField : "text",
				value : 'Y',
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'empinfo.isSpecialTrades',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false
			});

	var isCadres = new Ext.form.ComboBox({
				fieldLabel : '是否干部',
//				width : 150,
				anchor : '100%',
				store : new Ext.data.SimpleStore({
							fields : ['value', 'text'],
							data : [['Y', '是'], ['N', '否']]
						}),
				id : 'isCadres',
				name : 'isCadres',
				valueField : "value",
				displayField : "text",
				value : 'Y',
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'empinfo.isCadres',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false
			});

	var panel2 = new Ext.Panel({
				border : false,
				items : [{
							layout : "column",
							border : false,
							items : [{
										columnWidth : 0.33,
										layout : "form",
										border : false,
										items : [hiddenDeptTxt, deptTxt]
									}, {
										columnWidth : 0.33,
										layout : "form",
										border : false,
										items : [tfStationName]
									}]
						}, {
							layout : "column",
							border : false,
							items : [{
										columnWidth : 0.33,
										layout : "form",
										border : false,
										items : [tfWorkDate]
									}, {
										columnWidth : 0.33,
										layout : "form",
										border : false,
										items : [tfMissionDate]
									}]
						}, {
							layout : "column",
							border : false,
							items : [{
										columnWidth : 0.33,
										layout : "form",
										border : false,
										items : [isSpecialTrades]
									}, {
										columnWidth : 0.33,
										layout : "form",
										border : false,
										items : [isCadres]
									}]
						}]
			});

	// 保存按钮
	var btnSave = new Ext.Button({
				text : Constants.BTN_SAVE,
				iconCls : Constants.CLS_SAVE,
				handler : onSave
			});
	// head工具栏
	var headTbar = new Ext.Toolbar({
				region : 'north',
				items : [btnSave]
			});

	var formPanel = new Ext.FormPanel({
				labelAlign : 'right',
				labelWidth : 80,
				border : false,
				tbar : headTbar,
				items : [panel1, panel2]
			});

	// 计算年龄
	function getAge(argDate) {
		var value = argDate;
		if (!value) {
			return '';
		}
		if (value instanceof Date) {
			value = value.dateFormat('Y-m');
		}
		var now = new Date();
		var age = now.dateFormat('Y') - Number(value.substring(0, 4)) + 1;
		// age += Math.ceil((now.dateFormat('m') - Number(value.substring(5,
		// 7))) /12);
		return age;
	}
	// 计算工龄
	function getWorkAge(argDate) {
		var value = argDate;
		if (!value) {
			return '';
		}
		if (value instanceof Date) {
			value = value.dateFormat('Y-m');
		}
		var now = new Date();
		var age = now.dateFormat('Y') - Number(value.substring(0, 4)) + 1;
		return age;
	}

	// 保存按钮处理
	function onSave() {
		if (_empId == null || _empId == "") {
			Ext.Msg.alert("提示", "请选择左边的人员节点!");
			return false;
		}
		if (deptTxt.getValue() == null || deptTxt.getValue() == '') {
			Ext.Msg.alert('提示', '部门不可为空！');
			return;
		}
		Ext.Msg.confirm("提示", "是否确定保存？", function(buttonobj) {
					if (buttonobj == "yes") {
						var params = {
							'empId' : _empId,
							// 新工号
							'newEmpCode' : tfEmpCode.getValue(),
							'isSpecialTrades' : isSpecialTrades.getValue(),
							'isCadres' : isCadres.getValue(),
							'deptId' : hiddenDeptTxt.getValue()
						};
						// 提交前设置Combo的值
						setComboValue();
						// 保存数据
						formPanel.getForm().submit({
							method : Constants.POST,
							url : 'hr/saveHrEmpInfo.action',
							params : params,
							success : function(form, action) {
								var o = eval('(' + action.response.responseText
										+ ')');
//								tree.getRootNode().reload();//update by sychen 20100702
//								formPanel.getForm().reset();//update by sychen 20100702
								Ext.Msg.alert("提示", "保存成功！");
							}
						});
					}
				});
	}
	// 提交前设置Combo的值，保证其以Number类型传回服务端
	function setComboValue() {
		// 性别
		//update by sychen 20100702
//		if (!cbSex.getValue()) {
//			cbSex.setValue('');
//		}
		// 政治面貌
		if (!cbPoliticsId.getValue()) {
			cbPoliticsId.setValue('');
		}
	}

	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							region : 'west',
							layout : 'fit',
							width : 270,
							autoScroll : true,
							containerScroll : true,
							split : true,
							collapsible : true,
							items : [new Ext.TabPanel({
										tabPosition : 'bottom',
										activeTab : 1,
										layoutOnTabChange : true,
										items : [{
													title : '列表方式',
													border : false,
													autoScroll : true,
													layout : 'border',
													items : [{
																region : 'center',
																layout : 'fit',
																items : [grid]
															}]
												}, {
													title : '部门人员树',
													border : false,
													autoScroll : true,
													items : [tree],
													listeners : {
														'activate' : function() {
															root.expand();
														}
													}
												}]
									})]
						}, {
							region : 'center',
							layout : 'fit',
							border : true,
							frame : false,
							collapsible : false,
							items : [formPanel]
						}]
			});

	function deptSelect() {
		var args = {
			selectModel : 'single',
			rootNode : {
				id : '0',
				text : '灞桥电厂'
			}
		};
		var object = window.showModalDialog('/power/comm/jsp/hr/dept/dept.jsp',
				args, 'dialogWidth=' + Constants.WIDTH_COM_DEPT
						+ 'px;dialogHeight=' + Constants.HEIGHT_COM_DEPT
						+ 'px;center=yes;help=no;resizable=no;status=no;');
		if (object) {
			tfStationName.clearValue();
			deptTxt.setValue(object.names);
			Ext.get("deptId").dom.value = object.ids;
			stationStore.load({
						params : {
							deptIdForm : object.ids
						},
						callback : function() {
							stationStore.insert(0, new stationData({
												stationName : '',
												stationId : ''
											}));
						}
					})
		}
	}
});