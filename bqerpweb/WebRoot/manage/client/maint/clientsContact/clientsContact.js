Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	
	function selectClientsInfo() {
		var clientsInfo = window.showModalDialog("/power/manage/client/maint/clientsContact/selectClientsInfo.jsp");
		if (typeof(clientsInfo) != "undefined") {
			clientName.setValue(clientsInfo.clientName);
			Ext.get('clientId').dom.value = clientsInfo.cliendId;
		}
	}
	
	function getCurrentDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		return s;
	}
	
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result.workerCode) {
					// 设定工作负责人为登录人
					lastModifiedByH.setValue(result.workerCode);
					Ext.get("lastModifiedBy").dom.value = result.workerName
							? result.workerName
							: '';
				}
			}
		});
	}

	var mantype = "";
	var method = "add";
	var pageSize = 18;
	// 已存在的人员
	var str = '';

	var yeardata = '';
	var myDate = new Date();
	var myMonth = myDate.getMonth() + 1;

	myMonth = (myMonth < 10 ? "0" + myMonth : myMonth);

	for (var i = 2004; i < myDate.getFullYear() + 2; i++) {
		if (i < myDate.getFullYear() + 1)
			yeardata += '[' + i + ',' + i + '],';
		else
			yeardata += '[' + i + ',' + i + ']';
	}
	var yeardata = eval('[' + yeardata + ']');

	var meetingYear = new Ext.form.ComboBox({
				xtype : "combo",
				store : new Ext.data.SimpleStore({
							fields : ['value', 'key'],
							data : yeardata
						}),
				id : 'itemType',
				name : 'itemType',
				valueField : "key",
				displayField : "value",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'statItem.itemType',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				width : 80,
				value : myDate.getFullYear()
			});
			
	var fuzzyText = new Ext.form.TextField({
		id : 'fuzzyText',
		name : 'fuzzyText',
		width : 200,
		emptyText : '合作伙伴名称/联系人名称'
	})		

	var westbtnQuery = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : function() {
					westStore.load({
								params : {
									start : 0,
									limit : pageSize
								}
							});
				}
			});

	var westSm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});

	var westStorelist = new Ext.data.Record.create([westSm, {
				name : 'contactId'
			}, {
				name : 'clientId'
			}, {
				name : 'contactName'
			}, {
				name : 'sex'
			},  {
				name : 'age'
			}, {
				name : 'position'
			}, {
				name : 'workAge'
			}, {
				name : 'department'
			}, {
				name : 'identityCard'
			}, {
				name : 'address'
			}, {
				name : 'officephone'
			}, {
				name : 'cellphone'
			}, {
				name : 'fax'
			}, {
				name : 'qq'
			}, {
				name : 'email'
			}, {
				name : 'memo'
			}, {
				name : 'lastModifiedBy'
			}, {
				name : 'lastModifiedDate'
			}, {
				name : 'clientName'
			}, {
				name : 'lastModifiedName'
			}]);

	var westStore = new Ext.data.JsonStore({
				url : 'clients/getClientsContactList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : westStorelist
			});

	westStore.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							fuzzyText : fuzzyText.getValue()
						});
			});

	// 左侧Grid
	var westgrid = new Ext.grid.GridPanel({
				store : westStore,
				columns : [new Ext.grid.RowNumberer(), {
							header : "合作伙伴名称",
							width : 90,
							sortable : false,
							dataIndex : 'clientName',
							align : 'center'
						}, {
							header : "联系人",
							width : 80,
							sortable : false,
							dataIndex : 'contactName',
							align : 'center'
						}],
				viewConfig : {
					forceFit : true
				},
				tbar : ['模糊查询:', fuzzyText, westbtnQuery],
				sm : westSm,
				bbar : new Ext.PagingToolbar({
							pageSize : pageSize,
							store : westStore,
							displayInfo : true,
							displayMsg : "{2}条记录",
							emptyMsg : "没有记录"
						}),
				frame : true,
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});

	westgrid.on('rowclick', function(grid, rowIndex, e) {
		var o = grid.getStore().getAt(rowIndex).data
		contactId.setValue(o.contactId);
		contactName.setValue(o.contactName);
		clientId.setValue(o.clientId);
		clientName.setValue(o.clientName != null
				? o.clientName
				: "");
		sex.setValue(o.sex != null ? o.sex : "");
		age.setValue(o.age != null? o.age: "");
		position.setValue(o.position != null? o.position: "");
		workAge.setValue(o.workAge != null? o.workAge: "");
		department.setValue(o.department != null
				? o.department
				: "");
		identityCard.setValue(o.identityCard != null
				? o.identityCard
				: "");		
		address.setValue(o.address != null
				? o.address
				: "");
		officephone.setValue(o.officephone != null
				? o.officephone
				: "");
		cellphone.setValue(o.cellphone != null? o.cellphone: "");
		fax.setValue(o.fax != null? o.fax: "");
		qq.setValue(o.qq != null? o.qq: "");
		email.setValue(o.email != null
				? o.email
				: "");
		lastModifiedBy.setValue(o.lastModifiedName != null
				? o.lastModifiedName
				: "");
		lastModifiedByH.setValue(o.lastModifiedBy != null
				? o.lastModifiedBy
				: "");
		var date = o.lastModifiedDate.substring(0,10);
		lastModifiedDate.setValue(date != null
				? date
				: "");
		memo.setValue(o.memo != null
				? o.memo
				: "");
		method = "update";
		eastbtnSav.setText("修改");
			
	});

	westStore.load({
				params : {
					start : 0,
					limit : pageSize
				}
			});

	var eastbtnAdd = new Ext.Button({
				text : '增加',
				iconCls : 'add',
				handler : function() {
					eastform.getForm().reset();
					method = "add";
					eastbtnSav.setText("保存");
					getWorkCode();
				}
			});

	var eastbtnDel = new Ext.Button({
				text : '删除',
				iconCls : 'delete',
				handler : function() {
					if (contactId.getValue() != null
							&& contactId.getValue() != "") {
						Ext.Ajax.request({
		                	url : 'clients/daleteClientsContact.action',
							method : 'post',
			               	params : {
								contactId : contactId.getValue()
							},
							success : function(action) {
								var o = eval('(' + action.responseText
										+ ')');
								Ext.MessageBox.alert('提示', o.msg);
								westStore.load({
											params : {
												start : 0,
												limit : pageSize
											}
										});
								eastform.getForm().reset();
								method = "add";
								eastbtnSav.setText("保存");
							},
							failure : function(form, action) {
								var o = eval('(' + action.responseText
										+ ')');
								Ext.MessageBox.alert('错误', "删除失败！");
							}
						});		
					} else {
						Ext.MessageBox.alert('提示', '请选择一条要删除的记录!');
					}
				}
			});

	var eastbtnSav = new Ext.Button({
				text : '保存',
				iconCls : 'save',
				handler : function() {
					if (eastform.getForm().isValid()) {
						var url;
						if(method == "add") {
							url = "clients/saveClientsContact.action";
						} else if(method == "update") {
							url = "clients/updateClientsContact.action";
						}
						eastform.getForm().submit({
							method : 'post',
							url : url,
							params : {
								contactId : contactId.getValue()
							},
							success : function(form, action) {
								var o = eval('(' + action.response.responseText
										+ ')');
								Ext.MessageBox.alert('提示', o.msg);
								fuzzyText.setValue("");
								westStore.load({
											params : {
												start : 0,
												limit : pageSize
											}
										});
								eastform.getForm().reset();
								method = "add";
								eastbtnSav.setText("保存");
							},
							failure : function(form, action) {
								var o = eval('(' + action.response.responseText
										+ ')');
								Ext.MessageBox.alert('错误', o.msg);
							}
						})
					}
				}
			});
	
	// 联系人ID
	var contactId = new Ext.form.Hidden({
				id : 'base.contactId',
				name : 'base.contactId'
	});

	// 联系人姓名
	var contactName = new Ext.form.TextField({
				id : 'contactName',
				fieldLabel : "联系人姓名",
				name : 'base.contactName',
				anchor : '95%',
				allowBlank : false
	});

	// 合作伙伴名称
	var clientName = new Ext.form.ComboBox({
				id : 'clientName',
				fieldLabel : "合作伙伴名称",
				name : 'clientName',
				anchor : '95%',
				readOnly : true,
				allowBlank : false,
				onTriggerClick : function() {
					selectClientsInfo();
				}
	});
	// 公司名称
	var clientId = new Ext.form.Hidden({
				id : 'clientId',
				name : 'base.cliendId'
	});
	
	var sexStore = new Ext.data.SimpleStore({
		root : 'list',
		data : [['1','男'],['2','女']],
		fields : ['name' ,'key']
	})
	
	//类型
	var sex = new Ext.form.ComboBox({
				id : 'sex',
				name : 'base.sex',
				fieldLabel : "性别",
				mode : 'local',
				typeAhead : true,
				valueField : "name",
				displayField : "key",
				store : sexStore,
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				anchor : '95%'
	});
	
	// 年龄
	var age = new Ext.form.NumberField({
				id : 'age',
				fieldLabel : "年龄",
				name : 'base.age',
				anchor : '95%',
				allowBlank : true,
				maxValue : '100',
				minValue : '0'
	});
	
	// 职务
	var position = new Ext.form.TextField({
				id : 'position',
				fieldLabel : "职务",
				name : 'base.position',
				anchor : '95%',
				allowBlank : true
				
	});
	
	// 工龄
	var workAge = new Ext.form.NumberField({
				id : 'workAge',
				fieldLabel : "工龄",
				name : 'base.workAge',
				anchor : '95%',
				allowBlank : true,
				maxValue : '100',
				minValue :  '0'
	});
	
	var department = new Ext.form.TextField({
				id : 'department',
				name : 'base.department',
				fieldLabel : '所属部门',
				readOnly : false,
				allowBlank : false,
				height : 40,
				anchor : '97%'
	});
	
	var identityCard = new Ext.form.TextField({
				id : 'identityCard',
				name : 'base.identityCard',
				fieldLabel : '身份证号',
				readOnly : false,
				allowBlank : false,
				height : 40,
				anchor : '97%',
				minLength : 18,
				maxLength : 18,
				maxLengthText : '最多输入18个数字！'
	});
	
	var address = new Ext.form.TextField({
				id : 'address',
				name : 'base.address',
				fieldLabel : '主要业绩',
				readOnly : false,
				allowBlank : false,
				height : 40,
				anchor : '97%'
	});

	var address = new Ext.form.TextField({
			id : 'address',
			name : 'base.address',
			readOnly : false,
			fieldLabel : '办公地址',
			anchor : '97%'
	});
	
	var officephone = new Ext.form.TextField({
			id : 'officephone',
			name : 'base.officephone',
			readOnly : false,
			fieldLabel : '办公电话',
			anchor : '97%'
	});
	
	var cellphone = new Ext.form.NumberField({
			id : 'cellphone',
			name : 'base.cellphone',
			readOnly : false,
			fieldLabel : '手机号码',
			anchor : '94%',
			maxLength : 11,
			maxLengthText : '最多输入11个数字！'
	});
	
	var fax = new Ext.form.TextField({
			id : 'fax',
			name : 'base.fax',
			readOnly : false,
			fieldLabel : '传真',
			anchor : '97%'
	});
	
	var qq = new Ext.form.NumberField({
			id : 'qq',
			name : 'base.qq',
			readOnly : false,
			fieldLabel : 'QQ号码',
			anchor : '94%'
	});

	var email = new Ext.form.TextField({
			id : 'email',
			name : 'base.email',
			readOnly : false,
			fieldLabel : '电子邮箱',
			anchor : '97%'
	});

	var memo = new Ext.form.TextArea({
				id : 'base.memo',
				xtype : 'textarea',
				fieldLabel : '备注',
				readOnly : false,
				height : 80,
				anchor : '94%'
	});
	
	var lastModifiedBy = new Ext.form.TextField({
			id : 'lastModifiedBy',
			name : 'lastModifiedBy',
			readOnly : true,
			fieldLabel : '登记人',
			anchor : '94%'
	});
	
	var lastModifiedByH = new Ext.form.Hidden({
			id : 'lastModifiedByH',
			name : 'base.lastModifiedBy'
	});
	
	var lastModifiedDate = new Ext.form.TextField({
			id : 'lastModifiedDate',
			name : 'base.lastModifiedDate',
			readOnly : true,
			fieldLabel : '登记时间',
			anchor : '89%',
			value : getCurrentDate()
	});
			
	var firstLine = new Ext.Panel({
		border : false,
		layout : 'column',
		autoScroll : true,
		items : [{
					columnWidth : 1,
					layout : 'form',
					border : false,
					labelWidth : 100,
					items : [contactId,contactName]
				}, {
					columnWidth : 1,
					layout : 'form',
					border : false,
					labelWidth : 100,
					items : [clientName,clientId]
				}]	
	})
	
	var secondLine = new Ext.form.FieldSet({
		title : '联系人个人信息',
		collapsible : true,
		border : true,
		autoScroll : true,
		height : '100%',
		layout : 'column',
		anchor : '95%',
		items : [{
					columnWidth : 1,
					layout : 'form',
					labelWidth : 100,
					border : false,
					items : [{
						border : false,
					    layout : 'column',
					    items : [{
						    	columnWidth : 0.5,
								layout : 'form',
								labelWidth : 100,
								border : false,
								items : [sex]
					   		}, {columnWidth : 0.5,
								layout : 'form',
								labelWidth : 100,
								border : false,
								items : [age]
							}]
					    }]
				},{
					columnWidth : 1,
					layout : 'form',
					labelWidth : 100,
					border : false,
					items : [{
						border : false,
					    layout : 'column',
					    items : [{
						    	columnWidth : 0.5,
								layout : 'form',
								labelWidth : 100,
								border : false,
								items : [position]
					   		}, {columnWidth : 0.5,
								layout : 'form',
								labelWidth : 100,
								border : false,
								items : [workAge]
							}]
					    }]
				},{
					columnWidth : 1,
					layout : 'form',
					border : false,
					labelWidth : 100,
					items : [department]
				},{
					columnWidth : 1,
					layout : 'form',
					border : false,
					labelWidth : 100,
					items : [identityCard]
				}]	
	})
	
	var thirdLine = new Ext.form.FieldSet({
		title : '联系人联系方式',
		collapsible : true,
		border : true,
		height : '100%',
		autoScroll : true,
		layout : 'column',
		anchor : '95%',
		items : [{
					columnWidth : 1,
					layout : 'form',
					border : false,
					labelWidth : 100,
					items : [address]
				},{
					columnWidth : 1,
					layout : 'form',
					labelWidth : 100,
					border : false,
					items : [{
						border : false,
					    layout : 'column',
					    items : [{
						    	columnWidth : 0.5,
								layout : 'form',
								labelWidth : 100,
								border : false,
								items : [officephone]
					   		}, {columnWidth : 0.5,
								layout : 'form',
								labelWidth : 100,
								border : false,
								items : [cellphone]
							}]
					    }]
				},{
					columnWidth : 1,
					layout : 'form',
					labelWidth : 100,
					border : false,
					items : [{
						border : false,
					    layout : 'column',
					    items : [{
						    	columnWidth : 0.5,
								layout : 'form',
								labelWidth : 100,
								border : false,
								items : [fax]
					   		}, {columnWidth : 0.5,
								layout : 'form',
								labelWidth : 100,
								border : false,
								items : [qq]
							}]
					    }]
				},{
					columnWidth : 1,
					layout : 'form',
					border : false,
					labelWidth : 100,
					items : [email]
				}]	
	})
	
	var forthLine = new Ext.Panel({
		border : false,
		layout : 'column',
		autoScroll : true,
		items : [{
					columnWidth : 1,
					layout : 'form',
					border : false,
					labelWidth : 100,
					items : [memo]
				}, {
					columnWidth : 1,
					layout : 'form',
					labelWidth : 100,
					border : false,
					items : [{
						border : false,
					    layout : 'column',
					    items : [{
						    	columnWidth : 0.5,
								layout : 'form',
								labelWidth : 100,
								border : false,
								items : [lastModifiedBy]
					   		}, {columnWidth : 0.5,
								layout : 'form',
								labelWidth : 100,
								border : false,
								items : [lastModifiedDate]
							}]
					    }]
				}]	
	})
	
	var formContent = new Ext.Panel({
				border : false,
				layout : 'column',
				autoScroll : true,
				items : [{
							columnWidth : 1,
							layout : 'form',
							labelWidth : 50,
							border : false,
							items : [firstLine]
						}, {
							border : false,
							layout : 'form',
							columnWidth : 1,
							labelWidth : 50,
							items : [secondLine]
						}, {
							border : false,
							layout : 'form',
							columnWidth : 1,
							labelWidth : 50,
							items : [thirdLine]
						}, {
							border : false,
							layout : 'form',
							columnWidth : 1,
							labelWidth : 50,
							items : [forthLine]
						}]
			});

	// 右侧Form
	var eastform = new Ext.form.FormPanel({
				bodyStyle : "padding:5px 5px 0",
				labelAlign : 'right',
				labelWidth : 50,
				autoScroll : true,
				region : 'center',
				border : false,
				tbar : [eastbtnAdd, {
							xtype : "tbseparator"
						}, eastbtnDel, {
							xtype : "tbseparator"
						}, eastbtnSav],
				items : [formContent]
			});

	// ↑↑主窗口部件

	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							title : '合作伙伴联系人登记',
							region : "center",
							split : true,
							collapsible : false,
							titleCollapse : true,
							margins : '1',
							layoutConfig : {
								animate : true
							},
							layout : 'fit',
							items : [eastform]
						}, {
							title : '合作伙伴联系人查询',
							region : 'west',
							split : true,
							collapsible : true,
							titleCollapse : true,
							margins : '1',
							width : 320,
							layoutConfig : {
								animate : true
							},
							layout : 'fit',
							items : [westgrid]
						}]
			});
	getWorkCode();

});