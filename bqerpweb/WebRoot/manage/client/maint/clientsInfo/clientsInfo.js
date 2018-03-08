Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	
	function selectType() {
		var typeObject = window.showModalDialog("/power/manage/client/maint/clientsInfo/selectType.jsp");
		if (typeof(typeObject) != "undefined") {
			type.setValue(typeObject.typeName);
			Ext.get('typeId').dom.value = typeObject.typeId;
		}
	}
	
	function selectcCharacter() {
		var characterObject = window.showModalDialog("/power/manage/client/maint/clientsInfo/selectCharacter.jsp");
		if (typeof(characterObject) != "undefined") {
			character.setValue(characterObject.characterName);
			Ext.get('characterId').dom.value = characterObject.characterId;
		}
	}
	
	function selectcImportance() {
		var importanceObject = window.showModalDialog("/power/manage/client/maint/clientsInfo/selectImportance.jsp");
		if (typeof(importanceObject) != "undefined") {
			importance.setValue(importanceObject.importanceName);
			Ext.get('importanceId').dom.value = importanceObject.importanceId;
		}
	}
	
	function selectcTrade() {
		var tradeObject = window.showModalDialog("/power/manage/client/maint/clientsInfo/selectTrade.jsp");
		if (typeof(tradeObject) != "undefined") {
			trade.setValue(tradeObject.tradeName);
			Ext.get('tradeId').dom.value = tradeObject.tradeId;
		}
	}
	
	function getCurrentDate() {
//		var d, s, t;
//		d = new Date();
//		s = d.getFullYear().toString(10) + "-";
//		t = d.getMonth() + 1+"-";
//		y = d.getDay();
//		s += (t > 9 ? "" : "0") + t +(y > 9 ? "" : "0")+ y;
//		return s;
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		return s;
	}
	
	function saveBeforeCheck() {
		
	}
	
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result.workerCode) {
					// 设定工作负责人为登录人
					Ext.get("lastModifiedBy").dom.value = result.workerName
							? result.workerName
							: '';
					lastModifiedByH.setValue(result.workerCode);
					
				}
			}
		});
	}

	var mantype = "";
	var method = "add";
	var pageSize = 18;
			
	var fuzzyText = new Ext.form.TextField({
		id : 'fuzzyText',
		name : 'fuzzyText',
		width : 180,
		emptyText : '合作伙伴编号/名称'
	})		

	var westbtnQuery = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : function() {
					westStore.load({
								params : {
									fuzzyText : fuzzyText.getValue(),
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
				name : 'cliendId'
			}, {
				name : 'typeId'
			}, {
				name : 'characterId'
			}, {
				name : 'importanceId'
			}, {
				name : 'tradeId'
			}, {
				name : 'clientCode'
			}, {
				name : 'clientName'
			}, {
				name : 'cliendChargeby'
			}, {
				name : 'clientOverview'
			}, {
				name : 'mainProducts'
			}, {
				name : 'mainPerformance'
			}, {
				name : 'address'
			}, {
				name : 'telephone'
			}, {
				name : 'zipcode'
			}, {
				name : 'email'
			}, {
				name : 'website'
			}, {
				name : 'fax'
			}, {
				name : 'legalRepresentative'
			}, {
				name : 'taxQualification'
			}, {
				name : 'bank'
			}, {
				name : 'account'
			}, {
				name : 'taxnumber'
			}, {
				name : 'regFunds'
			}, {
				name : 'regAddress'
			}, {
				name : 'memo'
			}, {
				name : 'approveFlag'
			}, {
				name : 'lastModifiedBy'
			}, {
				name : 'lastModifiedDate'
			}]);

	var westStore = new Ext.data.JsonStore({
				url : 'clients/getClientsInfoList.action',
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
							header : "合作伙伴编码",
							width : 90,
							sortable : false,
							dataIndex : 'clientCode',
							region : 'center'
						}, {
							header : "合作伙伴名称",
							width : 90,
							sortable : false,
							dataIndex : 'clientName',
							align : 'center'
						}, {
							header : "公司负责人",
							width : 80,
							sortable : false,
							dataIndex : 'cliendChargeby',
							align : 'center'
						}, {
							header : "状态",
							width : 80,
							sortable : false,
							dataIndex : 'approveFlag',
							align : 'center',
							renderer : function returnValue(val) {
								if(val == "1") {
									return '未启用';
								} else if(val == "2"){
									return '已启用';
								}
							}
						}],
				viewConfig : {
					forceFit : true
				},
				tbar : ['模糊查询 ：', fuzzyText, westbtnQuery],
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
		cliendId.setValue(o.cliendId);
		clientName.setValue(o.clientName != null
				? o.clientName
				: "");
		clientCode.setValue(o.clientCode != null ? o.clientCode : "");
		cliendChargeby.setValue(o.cliendChargeby != null? o.cliendChargeby: "");
		typeId.setValue(o.typeId != null? o.typeId: "");
		Ext.Ajax.request({
        	url : 'clients/getClientsTypeList.action',
			method : 'post',
            params : {
				fuzzytext : typeId.getValue()
			},
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				// 设定工作负责人为登录人
				var typeName = result.list[0].typeName;
				type.setValue(typeName != null? typeName: "");
			}
		});
		characterId.setValue(o.characterId != null
				? o.characterId
				: "");
		Ext.Ajax.request({
        	url : 'clients/getClientsCharacterList.action',
			method : 'post',
            params : {
				fuzzytext : characterId.getValue()
			},
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				// 设定工作负责人为登录人
				var characterName = result.list[0].characterName;
				character.setValue(characterName != null? characterName: "");
			}
		});		
		importanceId.setValue(o.importanceId != null
				? o.importanceId
				: "");
		Ext.Ajax.request({
        	url : 'clients/getClientsImportanceList.action',
			method : 'post',
            params : {
				fuzzytext : importanceId.getValue()
			},
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				// 设定工作负责人为登录人
				var importanceName = result.list[0].importanceName;
				importance.setValue(importanceName != null? importanceName: "");
			}
		});				
		tradeId.setValue(o.tradeId != null? o.tradeId: "");
		Ext.Ajax.request({
        	url : 'clients/getClientsTradeList.action',
			method : 'post',
            params : {
				fuzzytext : tradeId.getValue()
			},
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				// 设定工作负责人为登录人
				var tradeName = result.list[0].tradeName;
				trade.setValue(tradeName != null? tradeName: "");
			}
		});	
		clientOverview.setValue(o.clientOverview != null
				? o.clientOverview
				: "");
		mainProducts.setValue(o.mainProducts != null
				? o.mainProducts
				: "");
		mainPerformance.setValue(o.mainPerformance != null
				? o.mainPerformance
				: "");
		address.setValue(o.address != null
				? o.address
				: "");
		zipcode.setValue(o.zipcode != null
				? o.zipcode
				: "");
		telephone.setValue(o.telephone != null
				? o.telephone
				: "");
		fax.setValue(o.fax != null
				? o.fax
				: "");
		email.setValue(o.email != null
				? o.email
				: "");
		website.setValue(o.website != null
				? o.website
				: "");
		legalRepresentative.setValue(o.legalRepresentative != null
				? o.legalRepresentative
				: "");
		taxnumber.setValue(o.taxnumber != null
				? o.taxnumber
				: "");
		taxQualification.setValue(o.taxQualification != null
				? o.taxQualification
				: "");
		bank.setValue(o.bank != null
				? o.bank
				: "");
		account.setValue(o.account != null
				? o.account
				: "");
		regFunds.setValue(o.regFunds != null
				? o.regFunds
				: "");		
		regAddress.setValue(o.regAddress != null
				? o.regAddress
				: "");
		lastModifiedBy.setValue(o.lastModifiedBy != null
				? o.lastModifiedBy
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
		approveFlag.setValue(o.approveFlag != null
				? o.approveFlag
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
					if(cliendId.getValue() != null && cliendId.getValue() != ""){
						if (approveFlag.getValue() == 1) {
							Ext.Ajax.request({
			                	url : 'clients/deleteClientsInfo.action',
								method : 'post',
				               	params : {
									cliendId : cliendId.getValue()
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
									Ext.MessageBox.alert('错误', o.msg);
								}
							});	
						} else if(approveFlag.getValue() == 2) {
							Ext.MessageBox.alert('提示', "当前合作伙伴已启用，不能删除！");
						}
					} else {
						Ext.MessageBox.alert('提示', '请选择一条要删除的记录!');
					}
				}
			});

	var eastbtnSav = new Ext.Button({
				text : '保存',
				iconCls : 'save',
				handler : function() {
					saveBeforeCheck();
					if (eastform.getForm().isValid()) {
						var url;
						if(method == "add") {
							url = "clients/saveClientsInfo.action";
						} else if(method == "update") {
							url = "clients/updateClientsInfo.action";
						}
						eastform.getForm().submit({
							method : 'post',
							url : url,
							params : {
								cliendId : cliendId.getValue()
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
	
	// 合作伙伴ID
	var cliendId = new Ext.form.Hidden({
				id : 'cliendId',
				name : 'cliendId'
	});

	// 合作伙伴名称
	var clientName = new Ext.form.TextField({
				id : 'clientName',
				fieldLabel : "合作伙伴名称",
				name : 'base.clientName',
				anchor : '95%',
				allowBlank : false
	});

	// 合作伙伴编码
	var clientCode = new Ext.form.TextField({
				id : 'clientCode',
				fieldLabel : "合作伙伴编码",
				name : 'base.clientCode',
				anchor : '95%',
				allowBlank : false
	});
	
	// 公司负责人
	var cliendChargeby = new Ext.form.TextField({
				id : 'cliendChargeby',
				fieldLabel : "公司负责人",
				name : 'base.cliendChargeby',
				anchor : '90%',
				allowBlank : false
	});	
	
	//类型
	var type = new Ext.form.ComboBox({
				id : 'typeName',
				name : 'typeName',
				fieldLabel : "类型",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				anchor : '95%',
				onTriggerClick : function() {
					selectType();
				}
	});
	
	var typeId = new Ext.form.Hidden({
				id : 'typeId',
				name : 'base.typeId'
	});
	
	// 性质
	var character = new Ext.form.ComboBox({
				id : 'characterName',
				name : 'characterName',
				fieldLabel : "性质",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				anchor : '95%',
				onTriggerClick : function() {
					selectcCharacter();
				}
	});
	
	var characterId = new Ext.form.Hidden({
				id : 'characterId',
				name : 'base.characterId'
	});
	
	// 重要程度
	var importance = new Ext.form.ComboBox({
				id : 'importanceName',
				name : 'importanceName',
				fieldLabel : "重要程度",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				anchor : '95%',
				onTriggerClick : function() {
					selectcImportance();
				}
	});
	
	var importanceId = new Ext.form.Hidden({
				id : 'importanceId',
				name : 'base.importanceId'
	});
	
	// 行业
	var trade = new Ext.form.ComboBox({
				id : 'tradeName',
				name : 'tradeName',
				fieldLabel : "行业",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				anchor : '95%',
				onTriggerClick : function() {
					selectcTrade();
				}
	});
	
	var tradeId = new Ext.form.Hidden({
				id : 'tradeId',
				name : 'base.tradeId'
	});
	
	var clientOverview = new Ext.form.TextArea({
				id : 'clientOverview',
				name : 'base.clientOverview',
				fieldLabel : '基本介绍',
				readOnly : false,
				allowBlank : false,
				height : 40,
				anchor : '97%'
	});
	
	var mainProducts = new Ext.form.TextArea({
				id : 'mainProducts',
				name : 'base.mainProducts',
				fieldLabel : '业务范围',
				readOnly : false,
				allowBlank : true,
				height : 40,
				anchor : '97%'
	});
	
	var mainPerformance = new Ext.form.TextArea({
				id : 'mainPerformance',
				name : 'base.mainPerformance',
				fieldLabel : '主要业绩',
				readOnly : false,
				allowBlank : true,
				height : 40,
				anchor : '97%'
	});

	var address = new Ext.form.TextField({
			id : 'address',
			name : 'base.address',
			readOnly : false,
			fieldLabel : '公司地址',
			anchor : '97%'
	});
	
	var zipcode = new Ext.form.NumberField({
			id : 'zipcode',
			name : 'base.zipcode',
			readOnly : false,
			fieldLabel : '邮编',
			anchor : '97%',
			maxLength : '6',
			maxLengthText : '最多只能输入6位！'
	});
	
	var telephone = new Ext.form.TextField({
			id : 'telephone',
			name : 'base.telephone',
			readOnly : false,
			fieldLabel : '电话',
			anchor : '94%'
	});
	
	var fax = new Ext.form.TextField({
			id : 'fax',
			name : 'base.fax',
			readOnly : false,
			fieldLabel : '传真',
			anchor : '97%'
	});

	var email = new Ext.form.TextField({
			id : 'email',
			name : 'base.email',
			readOnly : false,
			fieldLabel : '电子邮箱',
			anchor : '97%'
	});
	
	var website = new Ext.form.TextField({
			id : 'website',
			name : 'base.website',
			readOnly : false,
			fieldLabel : '公司网址',
			anchor : '97%'
	});
	
	var legalRepresentative = new Ext.form.TextField({
			id : 'legalRepresentative',
			name : 'base.legalRepresentative',
			readOnly : false,
			fieldLabel : '法人代表',
			anchor : '97%'
	});
	var taxnumber = new Ext.form.TextField({
			id : 'taxnumber',
			name : 'base.taxnumber',
			readOnly : false,
			fieldLabel : '税号',
			anchor : '94%'
	});
	
	var taxQualification = new Ext.form.TextField({
			id : 'taxQualification',
			name : 'base.taxQualification',
			readOnly : false,
			fieldLabel : '纳税人资格',
			anchor : '97%'
	});
	
	var bank = new Ext.form.TextField({
			id : 'bank',
			name : 'base.bank',
			readOnly : false,
			fieldLabel : '开户银行',
			anchor : '97%'
	});
	
	var account = new Ext.form.TextField({
			id : 'account',
			name : 'base.account',
			readOnly : false,
			fieldLabel : '银行账号',
			anchor : '94%'
	});
	
	var regFunds = new Ext.form.NumberField({
			id : 'regFunds',
			name : 'base.regFunds',
			readOnly : false,
			fieldLabel : '注册资金',
			anchor : '97%'
	});
	
	var unitLable = new Ext.form.Label({
		fieldLabel : '万元',
		anchor : '97%'
	}) 
	
	var regAddress = new Ext.form.TextField({
			id : 'regAddress',
			name : 'base.regAddress',
			readOnly : false,
			fieldLabel : '注册地址',
			anchor : '97%'
	});
	
	var memo = new Ext.form.TextArea({
				id : 'memo',
				name : 'base.memo',
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
	
	var approveFlag = new Ext.form.Hidden({
			id : 'approveFlag',
			name : 'base.approveFlag'
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
					items : [cliendId,clientName]
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
								items : [clientCode]
					   		}, {columnWidth : 0.5,
								layout : 'form',
								labelWidth : 100,
								border : false,
								items : [cliendChargeby]
							}]
					    }]
				}]	
	})
	
	var secondLine = new Ext.form.FieldSet({
		title : '合同伙伴概况',
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
								items : [type,typeId]
					   		}, {columnWidth : 0.5,
								layout : 'form',
								labelWidth : 100,
								border : false,
								items : [character,characterId]
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
								items : [importance,importanceId]
					   		}, {columnWidth : 0.5,
								layout : 'form',
								labelWidth : 100,
								border : false,
								items : [trade,tradeId]
							}]
					    }]
				},{
					columnWidth : 1,
					layout : 'form',
					border : false,
					labelWidth : 100,
					items : [clientOverview]
				},{
					columnWidth : 1,
					layout : 'form',
					border : false,
					labelWidth : 100,
					items : [mainProducts]
				},{
					columnWidth : 1,
					layout : 'form',
					border : false,
					labelWidth : 100,
					items : [mainPerformance]
				}]	
	})
	
	var thirdLine = new Ext.form.FieldSet({
		title : '合同伙伴联系方式',
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
								items : [zipcode]
					   		}, {columnWidth : 0.5,
								layout : 'form',
								labelWidth : 100,
								border : false,
								items : [telephone]
							}]
					    }]
				},{
					columnWidth : 1,
					layout : 'form',
					border : false,
					labelWidth : 100,
					items : [fax]
				},{
					columnWidth : 1,
					layout : 'form',
					border : false,
					labelWidth : 100,
					items : [email]
				},{
					columnWidth : 1,
					layout : 'form',
					border : false,
					labelWidth : 100,
					items : [website]
				}]	
	})
	
	var forthLine = new Ext.form.FieldSet({
		title : '合同伙伴财务信息',
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
								items : [legalRepresentative]
					   		}, {columnWidth : 0.5,
								layout : 'form',
								labelWidth : 100,
								border : false,
								items : [taxnumber]
							}]
					    }]
				},{
					columnWidth : 1,
					layout : 'form',
					border : false,
					labelWidth : 100,
					items : [taxQualification]
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
								items : [bank]
					   		}, {columnWidth : 0.5,
								layout : 'form',
								labelWidth : 100,
								border : false,
								items : [account]
							}]
					    }]
				},{
					columnWidth : 1,
//					layout : 'form',
					border : false,
//					labelWidth : 100,
					items : [{
						border : false,
					    layout : 'column',
					    items : [{
						    	columnWidth : 0.5,
								layout : 'form',
								labelWidth : 100,
								border : false,
								items : [regFunds]
					   		}, {columnWidth : 0.5,
//								layout : 'form',
//								labelWidth : 100,
								border : false,
								items : [unitLable]
							}]
					    }]
				},{
					columnWidth : 1,
					layout : 'form',
					border : false,
					labelWidth : 100,
					items : [regAddress]
				}]	
	})
	
	var fifthLine = new Ext.Panel({
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
								items : [lastModifiedBy,lastModifiedByH]
					   		}, {columnWidth : 0.5,
								layout : 'form',
								labelWidth : 100,
								border : false,
								items : [lastModifiedDate,approveFlag]
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
						}, {
							border : false,
							layout : 'form',
							columnWidth : 1,
							labelWidth : 50,
							items : [fifthLine]
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
							title : '合作伙伴信息登记',
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
							title : '合作伙伴信息查询',
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