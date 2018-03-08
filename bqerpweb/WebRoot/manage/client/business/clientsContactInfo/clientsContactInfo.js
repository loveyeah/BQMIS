Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
var cliendId = parent.cliendId;
Ext.onReady(function() {
	if(cliendId == "" ) {
		Ext.MessageBox.alert('提示', '请从列表中选择一条数据！');
		return;
	}
	var pageSize = 18;
			
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
			cliendId : cliendId
		});
	});

	// 左侧Grid
	var westgrid = new Ext.grid.GridPanel({
		store : westStore,
		columns : [new Ext.grid.RowNumberer(), {
					header : "联系人",
					width : 80,
					sortable : false,
					dataIndex : 'contactName',
					align : 'center'
				}],
		viewConfig : {
			forceFit : true
		},
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
			
	});

	westStore.load({
				params : {
					start : 0,
					limit : pageSize
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
				readOnly : true,
				allowBlank : true
	});

	// 合作伙伴名称
	var clientName = new Ext.form.ComboBox({
				id : 'clientName',
				fieldLabel : "合作伙伴名称",
				name : 'clientName',
				anchor : '95%',
				readOnly : true,
				allowBlank : true
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
				readOnly : true,
				maxValue : '100',
				minValue : '0'
	});
	
	// 职务
	var position = new Ext.form.TextField({
				id : 'position',
				fieldLabel : "职务",
				name : 'base.position',
				anchor : '95%',
				readOnly : true,
				allowBlank : true
				
	});
	
	// 工龄
	var workAge = new Ext.form.NumberField({
				id : 'workAge',
				fieldLabel : "工龄",
				name : 'base.workAge',
				anchor : '95%',
				allowBlank : true,
				readOnly : true,
				maxValue : '100',
				minValue :  '0'
	});
	
	var department = new Ext.form.TextField({
				id : 'department',
				name : 'base.department',
				fieldLabel : '所属部门',
				readOnly : true,
				allowBlank : false,
				height : 40,
				anchor : '97%'
	});
	
	var identityCard = new Ext.form.TextField({
				id : 'identityCard',
				name : 'base.identityCard',
				fieldLabel : '身份证号',
				readOnly : true,
				allowBlank : false,
				height : 40,
				anchor : '97%'
	});
	
	var address = new Ext.form.TextField({
				id : 'address',
				name : 'base.address',
				fieldLabel : '主要业绩',
				readOnly : true,
				allowBlank : false,
				height : 40,
				anchor : '97%'
	});

	var address = new Ext.form.TextField({
			id : 'address',
			name : 'base.address',
			readOnly : true,
			fieldLabel : '办公地址',
			anchor : '97%'
	});
	
	var officephone = new Ext.form.TextField({
			id : 'officephone',
			name : 'base.officephone',
			readOnly : true,
			fieldLabel : '办公电话',
			anchor : '97%'
	});
	
	var cellphone = new Ext.form.NumberField({
			id : 'cellphone',
			name : 'base.cellphone',
			readOnly : true,
			fieldLabel : '手机号码',
			anchor : '94%',
			maxLength : 11,
			maxLengthText : '最多输入11个数字！'
	});
	
	var fax = new Ext.form.TextField({
			id : 'fax',
			name : 'base.fax',
			readOnly : true,
			fieldLabel : '传真',
			anchor : '97%'
	});
	
	var qq = new Ext.form.NumberField({
			id : 'qq',
			name : 'base.qq',
			readOnly : true,
			fieldLabel : 'QQ号码',
			anchor : '94%'
	});

	var email = new Ext.form.TextField({
			id : 'email',
			name : 'base.email',
			readOnly : true,
			fieldLabel : '电子邮箱',
			anchor : '97%'
	});

	var memo = new Ext.form.TextArea({
				id : 'base.memo',
				xtype : 'textarea',
				fieldLabel : '备注',
				readOnly : true,
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
			anchor : '89%'
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
				items : [formContent]
			});

	// ↑↑主窗口部件

	var layout = new Ext.Viewport({
		layout : "border",
		border : false,
		items : [{
					title : '合作伙伴联系人明细',
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
					width : 220,
					layoutConfig : {
						animate : true
					},
					layout : 'fit',
					items : [westgrid]
				}]
	});

});