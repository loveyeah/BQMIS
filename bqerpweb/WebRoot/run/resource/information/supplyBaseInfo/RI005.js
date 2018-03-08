// 供应商基础资料维护
// author:wangyun
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	Ext.QuickTips.init();

	var start = '0';
	var limit = Constants.PAGE_SIZE;
	var lastModifyDate = null;
	// **********供应商登记TAB********** //
	// 新增 Button
	var btnAdd = new Ext.Button({
				text : "新增",
				iconCls : Constants.CLS_ADD,
				handler : btnAddHandler
			})

	// 删除 Button
	var btnDelete = new Ext.Button({
				text : Constants.BTN_DELETE,
				iconCls : Constants.CLS_DELETE,
				disabled : true,
				handler : btnDeleteHandler
			})

	// 保存 Button
	var btnSave = new Ext.Button({
				text : Constants.BTN_SAVE,
				iconCls : Constants.CLS_SAVE,
				handler : btnSaveHandler
			})

	// 打印 Button
	var btnPrint = new Ext.Button({
				text : '打印',
				iconCls : 'print',
				handler : btnPrintHandler
			})

	// 流水号
	var hdnSupplierId = new Ext.form.Hidden({
				id : "supplierId",
				name : "supply.cliendId"
			})

	// 供应商登记 Toolbar
	var tbarRegister = new Ext.Toolbar({
				items : [btnAdd, "-", btnDelete, "-", btnSave, "-", btnPrint]
			})

	// 编码 TextField
	var txtCode = new Ext.form.TextField({
				id : 'clientCode',
				name : 'supply.clientCode',
				fieldLabel : '编码<font color ="red">*</font>',
				allowBlank : false,
				isFormField : true,
				anchor : '100%',
				height : 22,
				codeField : "yes",
				style : {
					'ime-mode' : 'disabled'
				},
				maxLength : 4
			})

	// 名称 TextArea
	var txtName = new Ext.form.TextArea({
				id : 'clientName',
				name : 'supply.clientName',
				fieldLabel : '名称<font color ="red">*</font>',
				allowBlank : false,
				height : 22,
				anchor : '98%',
				maxLength : 30
			})

	// 第一行
	var fldSetFirstLine = new Ext.form.FieldSet({
		border : false,
		height : 25,
		layout : 'column',
		style : "padding-top:2px;padding-right:0px;padding-bottom:0px;margin-bottom:0px;border:0px",
		anchor : '100%',
		items : [{
					columnWidth : 0.3,
					border : false,
					layout : 'form',
					items : [txtCode]
				}, {
					columnWidth : 0.7,
					border : false,
					layout : 'form',
					items : [txtName]
				}]
	})

	// 法人代表 Textfield
	var txtLegalDeputy = new Ext.form.TextField({
				id : 'corporation',
				name : 'supply.corporation',
				fieldLabel : '法人代表',
				isFormField : true,
				anchor : '100%',
				height : 22,
				maxLength : 15
			})

	// 公司负责人 Textfield
	var txtPrincipal = new Ext.form.TextField({
				id : 'burdenman',
				name : 'supply.burdenman',
				fieldLabel : '公司负责人',
				isFormField : true,
				anchor : '100%',
				height : 22,
				maxLength : 5
			})

	// 公司性质 DataStore
	var dsCompanyType = new Ext.data.JsonStore({
				url : 'resource/getCompanyType.action',
				root : 'list',
				fields : ['characterName', 'characterId']
			})
	dsCompanyType.load();

	// 公司性质 Combobox
	var cmbCompanyType = new Ext.form.ComboBox({
				fieldLabel : "合作伙伴性质<font color='red'>*</font>",
				id : "characterId",
				hiddenName : "supply.characterId",
				store : dsCompanyType,
				displayField : "characterName",
				valueField : "characterId",
				mode : 'local',
				triggerAction : 'all',
				allowBlank : false,
				anchor : '100%',
				height : 22,
				emptyText : "请选择",
				readOnly : true
			})

	// 第二行
	var fldSetSecondLine = new Ext.form.FieldSet({
		border : false,
		height : 25,
		layout : 'column',
		style : "padding-top:2px;padding-bottom:0px;margin-bottom:0px;border:0px",
		anchor : '100%',
		items : [{
					columnWidth : .45,
					border : false,
					layout : 'form',
					items : [txtLegalDeputy]
				}, {
					columnWidth : .27,
					border : false,
					layout : 'form',
					items : [txtPrincipal]
				}, {
					columnWidth : .28,
					border : false,
					layout : 'form',
					items : [cmbCompanyType]
				}]
	})

	// 纳税人资格 TextArea
	var txtEligibleTaxpayers = new Ext.form.TextArea({
				id : 'nsrzg',
				name : 'supply.nsrzg',
				fieldLabel : '纳税人资格',
				isFormField : true,
				height : 22,
				anchor : '99.4%',
				maxLength : 50
			})

	// 注册资金 TextField
	var txtRegisteredCapital = new Ext.form.TextField({
				id : 'zczj',
				hiddenName : 'supply.zczj',
				fieldLabel : '注册资金',
				isFormField : true,
				anchor : '100%',
				height : 22,
				style : "text-align:right",
				maxLength : 30
			})

	// 供应商类别 DataStore
	var dsSupplyType = new Ext.data.JsonStore({
				url : 'resource/getSupplyType.action',
				root : 'list',
				fields : ['typeName', 'clientTypeId']
			})
	dsSupplyType.load();

	// 供应商类别 ComboBox
	var cmbSupplyType = new Ext.form.ComboBox({
				fieldLabel : "合作伙伴类型<font color='red'>*</font>",
				id : "clienttypeId",
				hiddenName : "supply.clienttypeId",
				store : dsSupplyType,
				displayField : "typeName",
				valueField : "clientTypeId",
				mode : 'local',
				triggerAction : 'all',
				allowBlank : false,
				anchor : '100%',
				height : 22,
				emptyText : "请选择",
				readOnly : true
			})

	// 第三行
	var fldSetThirdLine = new Ext.form.FieldSet({
		border : false,
		height : 25,
		layout : 'column',
		style : "padding-top:2px;padding-bottom:0px;margin-bottom:0px;border:0px",
		anchor : '100%',
		items : [{
					columnWidth : .45,
					border : false,
					layout : 'form',
					items : [txtEligibleTaxpayers]
				}, {
					columnWidth : .27,
					border : false,
					layout : 'form',
					items : [txtRegisteredCapital]
				}, {
					columnWidth : .28,
					border : false,
					layout : 'form',
					items : [cmbSupplyType]
				}]
	})

	// 开户银行 TextArea
	var txtBankAccount = new Ext.form.TextArea({
				id : 'bankaccount',
				name : 'supply.bankaccount',
				fieldLabel : '开户银行',
				isFormField : true,
				height : 22,
				anchor : '99.4%',
				maxLength : 50
			})

	// 帐号 TextField
	var txtAccount = new Ext.form.TextField({
				id : "account",
				name : 'supply.account',
				fieldLabel : '帐号',
				isFormField : true,
				height : 22,
				anchor : '100%',
				maxLength : 30
			})

	// 第四行
	var fldSetForthLine = new Ext.form.FieldSet({
		border : false,
		height : 25,
		layout : 'column',
		style : "padding-top:2px;padding-bottom:0px;margin-bottom:0px;border:0px",
		anchor : '100%',
		items : [{
					columnWidth : .45,
					border : false,
					layout : 'form',
					items : [txtBankAccount]
				}, {
					columnWidth : .55,
					border : false,
					layout : 'form',
					items : [txtAccount]
				}]
	})

	// 公司电话 TextField
	var txtCompanyTel = new Ext.form.TextField({
				id : "telephone",
				name : 'supply.telephone',
				fieldLabel : '公司电话',
				isFormField : true,
				anchor : '100%',
				height : 22,
				maxLength : 20
			})

	// 公司传真 TextField
	var txtCompanyFax = new Ext.form.TextField({
				id : 'gscz',
				name : 'supply.gscz',
				fieldLabel : '公司传真',
				isFormField : true,
				anchor : '100%',
				height : 22,
				maxLength : 20
			})

	// 商务传真 TextField
	var txtBusinessFax = new Ext.form.TextField({
				id : 'swcz',
				name : 'supply.swcz',
				fieldLabel : '商务传真',
				isFormField : true,
				height : 22,
				anchor : '100%',
				maxLength : 20
			})

	// 第五行
	var fldSetFifthLine = new Ext.form.FieldSet({
		border : false,
		height : 25,
		layout : 'column',
		style : "padding-top:2px;padding-bottom:0px;margin-bottom:0px;border:0px",
		anchor : '100%',
		items : [{
					columnWidth : .45,
					border : false,
					layout : 'form',
					items : [txtCompanyTel]
				}, {
					columnWidth : .27,
					border : false,
					layout : 'form',
					items : [txtCompanyFax]
				}, {
					columnWidth : .28,
					border : false,
					layout : 'form',
					items : [txtBusinessFax]
				}]
	})

	// 联系人1 TextField
	var txtContactBy1 = new Ext.form.TextField({
				id : 'lxr1',
				name : 'supply.lxr1',
				fieldLabel : '联系人1',
				isFormField : true,
				anchor : '100%',
				height : 22,
				maxLength : 5
			})

	// 电话1 TextField
	var txtFixedTel1 = new Ext.form.TextField({
				id : 'telephone1',
				name : 'supply.telephone1',
				fieldLabel : '电话1',
				isFormField : true,
				anchor : '100%',
				height : 22,
				maxLength : 20
			})

	// 手机1 TextField
	var txtMobileTel1 = new Ext.form.TextField({
				id : 'mobile1',
				name : 'supply.mobile1',
				fieldLabel : '手机1',
				isFormField : true,
				anchor : '100%',
				height : 22,
				maxLength : 20
			})

	// 电子邮箱1 TextField
	var txtEmail1 = new Ext.form.TextField({
				id : 'mail1',
				name : 'supply.mail1',
				fieldLabel : '电子邮箱1',
				isFormField : true,
				anchor : '100%',
				height : 22,
				maxLength : 50
			})

	// 第六行
	var fldSetSixthLine = new Ext.form.FieldSet({
		border : false,
		height : 25,
		layout : 'column',
		style : "padding-top:2px;padding-bottom:0px;margin-bottom:0px;border:0px",
		anchor : '100%',
		items : [{
					columnWidth : .22,
					border : false,
					layout : 'form',
					items : [txtContactBy1]
				}, {
					columnWidth : .23,
					border : false,
					layout : 'form',
					items : [txtFixedTel1]
				}, {
					columnWidth : .27,
					border : false,
					layout : 'form',
					items : [txtMobileTel1]
				}, {
					columnWidth : .28,
					border : false,
					layout : 'form',
					items : [txtEmail1]
				}]
	})

	// 联系人2 TextField
	var txtContactBy2 = new Ext.form.TextField({
				id : 'lxr2',
				name : 'supply.lxr2',
				fieldLabel : '联系人2',
				isFormField : true,
				anchor : '100%',
				height : 22,
				maxLength : 5
			})

	// 电话2 TextField
	var txtFixedTel2 = new Ext.form.TextField({
				id : 'telephone2',
				name : 'supply.telephone2',
				fieldLabel : '电话2',
				isFormField : true,
				anchor : '100%',
				height : 22,
				maxLength : 20
			})

	// 手机2 TextField
	var txtMobileTel2 = new Ext.form.TextField({
				id : 'mobile2',
				name : 'supply.mobile2',
				fieldLabel : '手机2',
				isFormField : true,
				anchor : '100%',
				height : 22,
				maxLength : 20
			})

	// 电子邮箱2 TextField
	var txtEmail2 = new Ext.form.TextField({
				id : 'mail2',
				name : 'supply.mail2',
				fieldLabel : '电子邮箱2',
				isFormField : true,
				anchor : '100%',
				height : 22,
				maxLength : 50
			})

	// 第七行
	var fldSetSeventhLine = new Ext.form.FieldSet({
		border : false,
		height : 25,
		layout : 'column',
		style : "padding-top:2px;padding-bottom:0px;margin-bottom:0px;border:0px",
		anchor : '100%',
		items : [{
					columnWidth : .22,
					border : false,
					layout : 'form',
					items : [txtContactBy2]
				}, {
					columnWidth : .23,
					border : false,
					layout : 'form',
					items : [txtFixedTel2]
				}, {
					columnWidth : .27,
					border : false,
					layout : 'form',
					items : [txtMobileTel2]
				}, {
					columnWidth : .28,
					border : false,
					layout : 'form',
					items : [txtEmail2]
				}]
	})

	// 办公地址 TextArea
	var txtOfficeAddress = new Ext.form.TextArea({
				id : 'address',
				name : 'supply.address',
				fieldLabel : '办公地址',
				isFormField : true,
				anchor : '99.4%',
				height : 22,
				maxLength : 150
			})

	// 邮编 TextField
	var txtPostalCode = new Ext.form.TextField({
				id : 'postmaster',
				name : 'supply.postmaster',
				fieldLabel : '邮编',
				isFormField : true,
				anchor : '100%',
				height : 22,
				maxLength : 6
			})

	// 公司网址 TextField
	var txtCompanyWebsite = new Ext.form.TextField({
				id : 'intetadd',
				name : 'supply.intetadd',
				fieldLabel : '公司网址',
				isFormField : true,
				anchor : '100%',
				height : 22,
				maxLength : 50
			})

	// 第八行
	var fldSetEighthLine = new Ext.form.FieldSet({
		border : false,
		height : 25,
		layout : 'column',
		style : "padding-top:2px;padding-bottom:0px;margin-bottom:0px;border:0px",
		anchor : '100%',
		items : [{
					columnWidth : .45,
					border : false,
					layout : 'form',
					items : [txtOfficeAddress]
				}, {
					columnWidth : .27,
					border : false,
					layout : 'form',
					items : [txtPostalCode]
				}, {
					columnWidth : .28,
					border : false,
					layout : 'form',
					items : [txtCompanyWebsite]
				}]
	})

	// 主要经营产品 TextArea
	var txtMainBusinessProduct = new Ext.form.TextArea({
				id : 'zycp',
				name : 'supply.zycp',
				fieldLabel : '主要经营产品',
				isFormField : true,
				anchor : '99.7%',
				height : 22,
				maxLength : 150
			})

	// 第九行
	var fldSetNinethLine = new Ext.form.FieldSet({
		border : false,
		height : 25,
		layout : 'column',
		style : "padding-top:2px;padding-bottom:0px;margin-bottom:0px;border:0px",
		anchor : '100%',
		items : [{
					border : false,
					layout : 'form',
					items : [txtMainBusinessProduct]
				}]
	})

	// 供应商概况 TextArea
	var txtMainBusinessResult = new Ext.form.TextArea({
				id : 'clientsurvey',
				name : 'supply.clientsurvey',
				fieldLabel : '供应商概况',
				isFormField : true,
				anchor : '99.7%',
				height : 22,
				maxLength : 150
			})

	// 第十行
	var fldSetTenthLine = new Ext.form.FieldSet({
		border : false,
		height : 25,
		layout : 'column',
		style : "padding-top:2px;padding-bottom:0px;margin-bottom:0px;border:0px",
		anchor : '100%',
		items : [{
					border : false,
					layout : 'form',
					items : [txtMainBusinessResult]
				}]
	})

	// 增加明细 Button
	var btnAddDetail = new Ext.Button({
				text : '新增明细',
				iconCls : Constants.CLS_ADD,
				handler : btnAddDetailHandler
			})

	// 删除明细 Button
	var btnDeleteDetail = new Ext.Button({
				text : '删除明细',
				iconCls : Constants.CLS_DELETE,
				handler : btnDeleteDetailHandler
			})

	// 明细 Toolbar
	var tbarMid = new Ext.Toolbar({
				items : [btnAddDetail, "-", btnDeleteDetail]
			})

	// 明细记录
	var drDetail = new Ext.data.Record.create([{
				name : 'id'
			}, {
				name : 'effectiveFromDate'
			}, {
				name : 'effectiveToDate'
			}, {
				name : 'aptitudeId'
			}, {
				name : 'aptitudeName'
			}, {
				name : 'ifQb'
			}, {
				name : 'isNewRecord'
			}, {
				name : 'detailLastModifyDate'
			}])

	// 明细 DataStore
	var dsDetail = new Ext.data.JsonStore({
				url : 'resource/getSupplyQualification.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : drDetail
			})

	dsDetail.setDefaultSort('aptitudeId');

	// 资料齐备否 显示为Checkbox
	var ckIsInfoEnough = new Ext.grid.CheckColumn({
				header : "资料齐备否",
				dataIndex : 'ifQb',
				width : 118
			})

	// 资质证书名称DS
	var dsQualificationName = new Ext.data.JsonStore({
				url : 'resource/getQualificationType.action',
				root : 'list',
				fields : ['aptitudeId', 'aptitudeName']
			})

	// 资质证书名称ComboBox
	var cmbQualification = new Ext.form.ComboBox({
				id : 'cmbQualification',
				displayField : "aptitudeName",
				valueField : "aptitudeId",
				store : dsQualificationName,
				allowBlank : false,
				mode : 'local',
				triggerAction : 'all',
				readOnly : true
			})
	dsQualificationName.load();

	// 分页 PagingToolbar
	var bbarDetail = new Ext.PagingToolbar({
				pageSize : Constants.PAGE_SIZE,
				store : dsDetail,
				displayInfo : true,
				displayMsg : Constants.DISPLAY_MSG,
				emptyMsg : Constants.EMPTY_MSG
			})

	// 证书有效日期
	var txtFromDate = new Ext.form.TextField({
		readOnly : true,
		id : "effectiveFromDate",
		listeners : {
			focus : function() {
				WdatePicker({
							startDate : '%y-%M-%d',
							dateFmt : 'yyyy-MM-dd',
							alwaysUseStartDate : false,
							onclearing : function() {
								gridDetail.getSelectionModel().getSelected()
										.set("effectiveFromDate", "");
							},
							onpicked : function() {
								gridDetail
										.getSelectionModel()
										.getSelected()
										.set(
												"effectiveFromDate",
												Ext.get("effectiveFromDate").dom.value);
							}
						});
			}
		}
	})

	// 证书失效日期
	var txtToDate = new Ext.form.TextField({
		readOnly : true,
		id : "effectiveToDate",
		listeners : {
			focus : function() {
				WdatePicker({
							startDate : '%y-%M-%d',
							dateFmt : 'yyyy-MM-dd',
							alwaysUseStartDate : false,
							onclearing : function() {
								gridDetail.getSelectionModel().getSelected()
										.set("effectiveToDate", "");
							},
							onpicked : function() {
								gridDetail
										.getSelectionModel()
										.getSelected()
										.set(
												"effectiveToDate",
												Ext.get("effectiveToDate").dom.value);
							}
						});
			}
		}
	})

	// 明细 Grid
	var gridDetail = new Ext.grid.EditorGridPanel({
				region : "center",
				layout : 'fit',
				anchor : "0",
				isFormField : false,
				store : dsDetail,
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : true
						}),
				columns : [new Ext.grid.RowNumberer({
									header : "行号",
									width : 35
								}), {
							sortable : true,
							header : '资质证书名称<font color=red>*</font>',
							dataIndex : 'aptitudeId',
							width : 300,
							renderer : cmbQualificationNameHandler,
            				css:CSS_GRID_INPUT_COL,
							editor : cmbQualification
						}, ckIsInfoEnough, {
							sortable : true,
							header : '证书有效日期',
							dataIndex : 'effectiveFromDate',
							width : 180,
            				css:CSS_GRID_INPUT_COL,
							editor : txtFromDate,
							align : 'left'
						}, {
							sortable : true,
							header : '证书失效日期',
							dataIndex : 'effectiveToDate',
							width : 160,
            				css:CSS_GRID_INPUT_COL,
							editor : txtToDate,
							align : 'left'
						}],
				enableColumnMove : false,
				enableColumnHide : true,
				// 单击修改
				clicksToEdit : 1,
				border : false,
				tbar : tbarMid,
				plugins : [ckIsInfoEnough],
				viewConfig : {
					forcefit : true,
					autoFill : true
				}
				// 5/7/09 不进行分页 yiliu
//				,
//				bbar : bbarDetail
			})

	// 详细录入框中 TextArea
	var txtDetail = new Ext.form.TextArea({
				id : 'txtDetail',
				width : 180
			})

	// 弹出画面
	var win = new Ext.Window({
				id : 'win',
				height : 170,
				width : 350,
				layout : 'fit',
				resizable : false,
				modal : true,
				closeAction : 'hide',
				items : [txtDetail],
				buttonAlign : "center",
				title : '详细信息录入窗口',
				buttons : [{
							text : Constants.BTN_SAVE,
							iconCls : Constants.CLS_SAVE,
							handler : popWinHandler
						}, {
							text : Constants.BTN_CANCEL,
							iconCls : Constants.CLS_CANCEL,
							handler : function() {
								win.hide();
							}
						}]
			});

	// 供应商登记 Panel
	var formRegister = new Ext.FormPanel({
				labelAlign : 'right',
				margins : '0 0 0 0',
				region : "north",
				autoHeight : true,
				labelPad : 15,
				labelWidth : 90,
				tbar : tbarRegister,
				border : false,
				items : [hdnSupplierId, fldSetFirstLine, fldSetSecondLine,
						fldSetThirdLine, fldSetForthLine, fldSetFifthLine,
						fldSetSixthLine, fldSetSeventhLine, fldSetEighthLine,
						fldSetNinethLine, fldSetTenthLine]
			})

	// 供应商登记 Tab
	var tabRegister = new Ext.Panel({
				title : '供应商登记',
				layout : 'border',
				margins : '0 0 0 0',
				border : false,
				items : [formRegister, gridDetail]
			})

	// **********供应商经营产品TAB********** //
	// 供应商经营产品 DataStore
	var dsManagement = new Ext.data.JsonStore({
				url : 'resource/getManagementInfo.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : ['materialId', 'materialName', 'specNo', 'parameter',
						'className']
			})

	dsManagement.on("beforeload", function() {
				Ext.apply(this.baseParams, {
							supplierId : hdnSupplierId.getValue()
						});
			})
	// 设置默认排序
	dsManagement.setDefaultSort('materialId');

	// 供应商经营产品 列定义
	var cmManagement = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
						header : "行号",
						width : 35
					}), {
				sortable : true,
				header : '物资编码',
				dataIndex : 'materialId',
				align : 'left'
			}, {
				sortable : true,
				header : '物资名称',
				dataIndex : 'materialName',
				align : 'left'
			}, {
				sortable : true,
				header : '物资类别',
				dataIndex : 'className',
				align : 'left'
			}, {
				sortable : true,
				header : '规格型号',
				dataIndex : 'specNo',
				align : 'left'
			}, {
				sortable : true,
				header : '材质/参数',
				dataIndex : 'parameter',
				align : 'left'
			}])

	// 供应商经营产品 Grid
	var gridManagement = new Ext.grid.GridPanel({
				layout : 'fit',
				ds : dsManagement,
				cm : cmManagement,
				autoWidth : true,
				autoScroll : true,
				enableColumnMove : false,
				enableColumnHide : true,
				border : false,
				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : Constants.PAGE_SIZE,
							store : dsManagement,
							displayInfo : true,
							displayMsg : Constants.DISPLAY_MSG,
							emptyMsg : Constants.EMPTY_MSG
						}),
				viewConfig : {
					forceFit : true,
					autoFill : true
				}
			})

	// 供应商经营产品 Panel
	var tabManagement = new Ext.Panel({
				title : '供应商经营产品',
				layout : 'fit',
				border : true,
				items : [gridManagement]
			})

	// **********供应商查询TAB********** //
	// 供应商编码/名称 TextField
	var txtQuerySupplyCode = new Ext.form.TextField({
				id : 'querySupplyCode',
				width : 250,
				emptyText : "供应商编码/名称"
			})

	// 模糊查詢 Button
	var btnFuzzyQuery = new Ext.Button({
				text : '模糊查询',
				iconCls : Constants.CLS_QUERY,
				handler : fuzzyQueryHandler
			})

	// 确认 Button
	var btnConfirm = new Ext.Button({
				text : '确认',
				iconCls : Constants.CLS_OK,
				handler : confirmHandler
			})

	// 供应商查询DataStore
	var dsQuery = new Ext.data.JsonStore({
				url : 'resource/getSupplierGegerateInfo.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : ['supplierId', 'supplier', 'supplyName',
						'companyTypeDesc', 'principal', 'registeredCapital',
						'companyTel']
			})

	dsQuery.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			});

	// 设置默认排序
	dsQuery.setDefaultSort('supplier');

	// 供应商查询Grid列内容定义
	var cmQuery = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
						header : "行号",
						width : 35
					}), {
				sortable : true,
				header : '供应商编码',
				dataIndex : 'supplier',
				align : 'left'
			}, {
				sortable : true,
				header : '供应商名称',
				dataIndex : 'supplyName',
				align : 'left'
			}, {
				sortable : true,
				header : '公司负责人',
				dataIndex : 'principal',
				align : 'left'
			}, {
				sortable : true,
				header : '公司性质',
				dataIndex : 'companyTypeDesc',
				align : 'left'
			}, {
				sortable : true,
				header : '注册资金',
				dataIndex : 'registeredCapital',
				align : 'left'
			}, {
				sortable : true,
				header : '公司电话',
				dataIndex : 'companyTel',
				align : 'left'
			}])

	// 供应商查询 ToolBar
	var tbarQuery = new Ext.Toolbar({
				items : [txtQuerySupplyCode, "-", btnFuzzyQuery, "-",
						btnConfirm]
			})

	// 供应商查询 Grid
	var gridQuery = new Ext.grid.GridPanel({
				layout : 'fit',
				store : dsQuery,
				cm : cmQuery,
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : true
						}),
				tbar : tbarQuery,
				autoScroll : true,
				enableColumnMove : false,
				enableColumnHide : true,
				border : false,
				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : Constants.PAGE_SIZE,
							store : dsQuery,
							displayInfo : true,
							displayMsg : Constants.DISPLAY_MSG,
							emptyMsg : Constants.EMPTY_MSG
						}),
				viewConfig : {
					forceFit : true,
					autoFill : true
				}
			})

	// 供应商查询 Panel
	var tabQuery = new Ext.Panel({
				title : '供应商查询',
				layout : 'fit',
				border : true,
				items : [gridQuery]
			})

	// **********主画面********** //
	var tabPanel = new Ext.TabPanel({
				activeTab : 0,
				autoScroll : true,
				layoutOnTabChange : true,
				tabPosition : 'bottom',
				border : false,
				items : [tabQuery, tabManagement, tabRegister]
			})

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "fit",
				border : false,
				items : [{
							layout : 'fit',
							border : false,
							margins : '0 0 0 0',
							region : 'center',
							autoScroll : true,
							items : [tabPanel]
						}]
			});

	// **********主处理********** //
	// 保存查询画面初始数据
	var originalData = null
	// 保存明细部信息
	var originalDetailData = []
	// 保存已删除的明细部ID
	var deleteDetailId = []
	// 弹出画面Flag
	var popWinFlg;

	// 不能输入全角字符正则表达式
	var CD_ERR_REGX = /[^\x00-\xff]+/g;

	// 电话输入检查
	txtFixedTel2.on("change", function(t, newValue, oldValue) {
				var strTel = newValue.match(CD_ERR_REGX);
				if (strTel)
					return txtFixedTel2.setValue(oldValue);
			})

	txtFixedTel1.on("change", function(t, newValue, oldValue) {
				var strTel = newValue.match(CD_ERR_REGX);
				if (strTel)
					return txtFixedTel1.setValue(oldValue);
			})

	// 公司电话输入检查
	txtCompanyTel.on("change", function(t, newValue, oldValue) {
				var strTel = newValue.match(CD_ERR_REGX);
				if (strTel)
					return txtCompanyTel.setValue(oldValue);
			})

	// 公司传真输入检查
	txtCompanyFax.on("change", function(t, newValue, oldValue) {
				var strTel = newValue.match(CD_ERR_REGX);
				if (strTel)
					return txtCompanyFax.setValue(oldValue);
			})

	// 商务传真输入检查
	txtBusinessFax.on("change", function(t, newValue, oldValue) {
				var strTel = newValue.match(CD_ERR_REGX);
				if (strTel)
					return txtBusinessFax.setValue(oldValue);
			})

	// 邮编输入检查
	txtPostalCode.on("change", function(t, newValue, oldValue) {
				var strPos = newValue.match(CD_ERR_REGX);
				if (strPos)
					return txtPostalCode.setValue(oldValue);
			})

	// 手机输入检查
	txtMobileTel1.on("change", function(t, newValue, oldValue) {
				var strMobile = newValue.match(CD_ERR_REGX);
				if (strMobile)
					return txtMobileTel1.setValue(oldValue);
			})

	txtMobileTel2.on("change", function(t, newValue, oldValue) {
				var strMobile = newValue.match(CD_ERR_REGX);
				if (strMobile)
					return txtMobileTel2.setValue(oldValue);
			})

	// 账号输入检查
	txtAccount.on("change", function(t, newValue, oldValue) {
				var strMobile = newValue.match(CD_ERR_REGX);
				if (strMobile)
					return txtAccount.setValue(oldValue);
			})

	// 查询画面Grid双击事件
	gridQuery.on("rowdblclick", confirmHandler);

	// 传递参数
	dsDetail.on('beforeload', function() {
				// 合作伙伴Id
				Ext.apply(this.baseParams, {
							supplierId : hdnSupplierId.getValue()
						});
			});

	// 名称点击处理
	txtName.onDblClick(function() {
				txtName.allowBlank = true;
				txtDetail.setValue(txtName.getValue());
				popWinFlg = 1;
				win.show();
			});

	// 纳税人资格点击处理
	txtEligibleTaxpayers.onDblClick(function() {
				txtDetail.setValue(txtEligibleTaxpayers.getValue());
				popWinFlg = 2;
				win.show();
			})

	// 开户银行点击处理
	txtBankAccount.onDblClick(function() {
				txtDetail.setValue(txtBankAccount.getValue());
				popWinFlg = 3;
				win.show();
			})

	// 办公地址点击处理
	txtOfficeAddress.onDblClick(function() {
				txtDetail.setValue(txtOfficeAddress.getValue());
				popWinFlg = 4;
				win.show();
			})

	// 主要经营产品点击处理
	txtMainBusinessProduct.onDblClick(function() {
				txtDetail.setValue(txtMainBusinessProduct.getValue());
				popWinFlg = 5;
				win.show();
			})

	// 供应商概况点击处理
	txtMainBusinessResult.onDblClick(function() {
				txtDetail.setValue(txtMainBusinessResult.getValue());
				popWinFlg = 6;
				win.show();
			})

	function initFocus() {
		var f = Ext.get('txtDetail');
		f.focus.defer(100, f);
	}

	// 弹出画面后获取焦点
	win.on("show", function() {
				initFocus();
			})

	// 查询画面load数据处理
	dsQuery.on('load', function() {
				if (dsQuery.getCount() == 0) {
					btnConfirm.setDisabled(true);
				} else if (dsQuery.getCount() > 0) {
					btnConfirm.setDisabled(false);
				}
			});

	/**
	 * 模糊查询
	 */
	function fuzzyQueryHandler() {
		dsQuery.load({
					params : {
						supplyNameCode : txtQuerySupplyCode.getValue(),
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});
	}

	/**
	 * 登记画面初始化
	 */
	function initRegister() {
		// 删除按钮不可用
		btnDelete.setDisabled(true);
		// 登记画面表单清空
		formRegister.getForm().reset();
		// 供应商信息清空
		originalData = null;
		// 供应商资质Grid清空
		dsDetail.load({
					params : {
						supplierId : "",
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});
		// 经营产品Grid清空
		dsManagement.reload();
		// 明细部信息清空
		originalDetailData = []
		// 已删除的明细部ID清空
		deleteDetailId = []
	}

	/**
	 * 供应商查询TAB确认BUTTON
	 */
	function confirmHandler() {
		// 如果没有选择，弹出提示信息
		if (!gridQuery.selModel.hasSelection()) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
			return;
		}

		// 进入登记画面
		tabPanel.setActiveTab(2);
		// 删除按钮可用
		btnDelete.setDisabled(false);

		// 判断是否有未保存数据
		var isNewRecord = hasNewRecord();
		// 画面数据有修改，保存数据并且设值
		if (isNewRecord) {
			// 是否放弃当前修改内容提示
			Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_004,
					function(button) {
						if (button == "yes") {
							setQueryValue();
						}
					});
		} else {
			// 设值
			setQueryValue();
		}
	}

	/**
	 * 将查询画面的值设置到登记画面
	 */
	function setQueryValue() {
		// 供应商经营产品数据
		var record = gridQuery.getSelectionModel().getSelected();
		var member = record.data;
		hdnSupplierId.setValue(member["supplierId"]);
		dsManagement.load({
					params : {
						supplier : hdnSupplierId.getValue(),
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				})

		// 检索供应商表
		var url = "resource/getSupplierDetail.action?supplierId="
				+ member["supplierId"];
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("POST", url, false);
		conn.send(null);
		var mydata = conn.responseText;

		mydata = Ext.util.JSON.decode(mydata);

		// 保存最后修改时间
		lastModifyDate = mydata['lastModifiedDate']
		var first = lastModifyDate.substr(0, 10);
		var last = lastModifyDate.substr(11, 18);
		lastModifyDate = first + " " + last;
		// 保存明細信息
		dsDetail.on("load", function() {
					originalDetailData = getDetailList();
				})

		// 检索供应商资质表
		dsDetail.load({
					params : {
						supplierId : member["supplierId"],
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});

		// 供应商信息读入
		formRegister.getForm().loadRecord({
					data : mydata
				});
		// 保存原始供应商信息
		originalData = formRegister.getForm().getValues();
		// 公司性质ComboBox 数据
		dsCompanyType.load();
		dsCompanyType.on("load", function() {
			if (mydata != null) {
				cmbCompanyType.setValue(mydata.characterId);
				for (var i = 0; i < dsCompanyType.getCount(); i++) {
					var data = dsCompanyType.getAt(i).data;
					var id = data.characterId;
					if (id == mydata.characterId) {
						if (originalData != null) {
							originalData['supply.characterId'] = mydata.characterId;
						}
					}
				}
			} else {
				cmbCompanyType.emptyText = "请选择";
			}
		});

		// 供应商性质ComboBox 数据
		dsSupplyType.load();
		dsSupplyType.on("load", function() {
			if (mydata != null) {
				cmbSupplyType.setValue(mydata.clienttypeId);
				for (var i = 0; i < dsSupplyType.getCount(); i++) {
					var data = dsSupplyType.getAt(i).data;
					var id = data.clientTypeId;
					if (id == mydata.clienttypeId) {
						if (originalData != null) {
							originalData['supply.clienttypeId'] = mydata.clienttypeId;
						}

					}
				}
			} else {
				cmbSupplyType.emptyText = "请选择";
			}
		});

		// 资质证书名称COMBOBOX DS
		dsQualificationName.load();
	}

	/**
	 * 登记画面增加处理
	 */
	function btnAddHandler() {
		// 画面数据有变化
		if (hasNewRecord()) {
			// 是否放弃当前修改内容提示
			Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_004,
					function(button) {
						// 放弃
						if (button == "yes") {
							// 画面初始化操作
							initRegister();
						}
					});
		} else {
			// 画面初始化操作
			initRegister();
		}
	}

	/**
	 * 登记画面删除处理
	 */
	function btnDeleteHandler() {
		// 是否删除当前供应商
		Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_002,
				function(button) {
					if (button == "yes") {
						Ext.Ajax.request({
									url : 'resource/deleteRegister.action',
									method : 'POST',
									params : {
										supplierId : Ext.get("supplierId").dom.value,
										lastModifyDate : lastModifyDate
									},
									success : function(action) {
										var result = eval("("
												+ action.responseText + ")");
										if (result.msg == "otherUse") {
											// 排他处理
											Ext.Msg.alert(
													Constants.SYS_REMIND_MSG,
													Constants.COM_E_015);
											return;
										}
										// 画面清空初始化
										initRegister();
										// 查询TAB数据重新LOAD
										dsQuery.reload();
										// 删除成功
										Ext.Msg.alert(Constants.SYS_REMIND_MSG,
												Constants.COM_I_005)
									},
									failure : function() {
										// 删除失败
										Ext.Msg.alert(Constants.SYS_REMIND_MSG,
												Constants.DEL_ERROR)

									}
								})
					}
				});
	}

	/**
	 * 登记画面保存按钮处理
	 */
	function btnSaveHandler() {
		// 画面没有修改时，提示信息未修改
		if (!hasNewRecord()) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_006);
		}
		saveHandler();
	}

	/**
	 * 登记画面保存处理
	 */
	function saveHandler() {
		if (isformCanSave()) {
			Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_001,
					function(buttonobj) {
						if (buttonobj == "yes") {
							// 编码为空标志
							var flagCode = false;
							var msg = "";

							// 编码为空
							if (!txtCode.getValue()) {
								msg = String.format(Constants.COM_E_002, "编码");
								flagCode = true;
							}

							// 名称为空
							if (txtName.getValue() == "") {
								msg += "<br>"
										+ String.format(Constants.COM_E_002,
												"名称");
								flagCode = true;
							}

							// 公司性质为空
							if (!cmbCompanyType.getValue()) {
								msg += "<br>"
										+ String.format(Constants.COM_E_003,
												"合作伙伴性质");
								flagCode = true;
							}

							// 供应商类别为空
							if (!cmbSupplyType.getValue()) {
								msg += "<br>"
										+ String.format(Constants.COM_E_003,
												"合作伙伴类型");
								flagCode = true;
							}

							// 资质书名称为空
							for (var i = 0; i < dsDetail.getCount(); i++) {
								var data = dsDetail.getAt(i).get('aptitudeId');

								if (data == "") {
									msg += "<br>"
											+ String.format(
													Constants.COM_E_002,
													"资质书名称");
									flagCode = true;
									break;
								}
							}

							// 判断证书有效日期是否小于证书失效日期
							for (var i = 0; i < dsDetail.getCount(); i++) {
								var fromDate = dsDetail.getAt(i)
										.get('effectiveFromDate');
								var toDate = dsDetail.getAt(i)
										.get('effectiveToDate');
								if (fromDate > toDate && fromDate != ""
										&& toDate != "") {
									msg += "<br>"
											+ String.format(
													Constants.COM_E_006,
													"证书有效日期", "证书失效日期");
									flagCode = true;
									break;
								}
							}

							// 输入必要数据提示信息
							if (flagCode) {
								Ext.Msg.alert(Constants.SYS_REMIND_MSG, msg);
								return;
							}

							if (!(txtCode.isValid() && txtName.isValid()
									&& txtLegalDeputy.isValid()
									&& txtPrincipal.isValid()
									&& txtEligibleTaxpayers.isValid()
									&& txtRegisteredCapital.isValid()
									&& txtBankAccount.isValid()
									&& txtAccount.isValid()
									&& txtCompanyTel.isValid()
									&& txtCompanyFax.isValid()
									&& txtBusinessFax.isValid()
									&& txtContactBy1.isValid()
									&& txtFixedTel1.isValid()
									&& txtMobileTel1.isValid()
									&& txtEmail1.isValid()
									&& txtContactBy2.isValid()
									&& txtFixedTel2.isValid()
									&& txtMobileTel2.isValid()
									&& txtEmail2.isValid()
									&& txtOfficeAddress.isValid()
									&& txtPostalCode.isValid()
									&& txtCompanyWebsite.isValid()
									&& txtMainBusinessProduct.isValid() && txtMainBusinessResult
									.isValid())) {
								return;
							}

							// 获取编码和供应商名称
							var supplier = Ext.get("clientCode").dom.value;
							var supplyName = Ext.get("clientName").dom.value;
							var isUnique = false;

							// 增加记录时，供应商编号和供应商名称唯一性check
							if (originalData == null) {
								isUnique = checkUnique(supplier, supplyName);
								if (!isUnique) {
									return;
								}
							} else {
								// 修改记录时，检查编码和名称是否修改
								if (supplier != originalData['supply.clientCode']) {

									supplyName = "";
									isUnique = checkUnique(supplier, supplyName);
									if (!isUnique) {
										return;
									}
								} else if (supplyName != originalData['supply.clientName']) {
									supplier = "";
									isUnique = checkUnique(supplier, supplyName);
									if (!isUnique) {
										return;
									}
								}
							}

							saveSupplierInfo(refreshSupplierInfo);
							return;
						}
					});
		}
	}

	/**
	 * 检查编码和名称是否唯一
	 */
	function checkUnique(supplier, supplyName) {
		// 确认供应商编号和供应商名称都要唯一
		var url = "resource/checkUnique.action?supplier=" + supplier
				+ "&supplyName=" + supplyName;
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("POST", url, false);
		conn.send(null);
		var mydata = conn.responseText;

		var msg = "";
		if (mydata == "1") {
			msg = String.format(Constants.COM_E_007, "编号");
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, msg);
			return false;
		}

		if (mydata == "2") {
			msg = String.format(Constants.COM_E_007, "名称");
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, msg);
			return false;
		}
		return true;
	}

	/**
	 * 重新记录供应商信息表单原始值
	 */
	function refreshSupplierInfo() {
		// 新增
		if (originalData == null) {
			url = 'resource/getSupplierIdBySupplier.action?supplier='
					+ txtCode.getValue();
			var conn = Ext.lib.Ajax.getConnectionObject().conn;
			conn.open("POST", url, false);
			conn.send(null);

			var result = eval("(" + conn.responseText + ")");
			if (result) {
				hdnSupplierId.setValue(result.cliendId);
			}
		}

		// 检索供应商表
		var url = "resource/getSupplierDetail.action?supplierId="
				+ hdnSupplierId.getValue();
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("POST", url, false);
		conn.send(null);
		var mydata = conn.responseText;
		mydata = Ext.util.JSON.decode(mydata);
		// 供应商信息读入
		formRegister.getForm().loadRecord({
					data : mydata
				});
		// 保存原始供应商信息
		originalData = formRegister.getForm().getValues();
		// 删除按钮可用
		btnDelete.setDisabled(false);
		// 查询tab刷新
		dsQuery.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});
		// 载入明细部数据
		dsDetail.load({
					params : {
						supplierId : hdnSupplierId.getValue(),
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});
		// 清空修改信息
		dsDetail.modified = [];
		deleteDetailId = [];
	}

	/**
	 * 保存供应商信息
	 * 
	 * @param func
	 *            保存成功后的操作
	 * @param params
	 *            操作的参数
	 */
	function saveSupplierInfo(func, params) {
		// 表单提交
		formRegister.getForm().submit({
			url : 'resource/saveRegister.action',
			method : Constants.POST,
			params : {
				// 供应商信息是否更改
				isFormChange : isFormChange(),
				// 明细信息是否更改
				isDetailChange : isDetailChange(),
				// 新增加的明细记录
				newDetail : Ext.util.JSON.encode(getDetailList(true)),
				// 修改过的明细db记录
				dbDetail : Ext.util.JSON.encode(getDetailList()),
				// 删除的明细id集
				deleteDetailId : Ext.util.JSON.encode(deleteDetailId),
				// 最后修改时间
				lastModifyDate : lastModifyDate
			},
			success : function(form, action) {
				var result = eval("(" + action.response.responseText + ")");
				if (result.msg == "otherUse") {
					// 排他处理
					Ext.Msg
							.alert(Constants.SYS_REMIND_MSG,
									Constants.COM_E_015);
					return;
				}
				if (result.success) {
					Ext.Msg.alert(Constants.SYS_REMIND_MSG, '&nbsp&nbsp&nbsp'
									+ Constants.COM_I_004);
					// 刷新页面
					if (params) {
						func.apply(this, [params]);
					} else {
						func.apply(this);
					}
				} else {
					Ext.Msg.alert(Constants.SYS_REMIND_MSG, result.msg);
				}
			},
			failure : function(form, action) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_E_014);
			}
		});
	}

	/**
	 * 表单check
	 */
	function isformCanSave() {
		// 信息没有改变直接返回
		if (!isDetailChange() && !isFormChange()) {
			return false;
		}
		// 明细信息没有改变，不需要保存
		if (isDetailChange()) {
			// 检查资质证书名称是否重名
			msg = isDetailRepeat();
			if (msg) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, msg);
				return false;
			}
		}

		return true;
	}

	/**
	 * 打印处理
	 */
	function btnPrintHandler() {
		if (hasNewRecord() || originalData == null) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_010);
		} else {
			window.open(application_base_path+"report/webfile/supplierBaseInfo.jsp?cliendId="
					+ hdnSupplierId.getValue() + "&clientCode="
					+ txtCode.getValue())
		}
	}

	/**
	 * 明细增加处理
	 */
	function btnAddDetailHandler() {
		var record = new drDetail({
					id : '',
					effectiveFromDate : '',
					effectiveToDate : '',
					aptitudeName : '',
					aptitudeId : '',
					ifQb : 'N',
					detailLastModifyDate : "",
					isNewRecord : true
				})

		// 原数据个数
		var count = dsDetail.getCount();
		if (count == null) {
			count = 0;
		}
		// 停止原来编辑
		gridDetail.stopEditing();
		// 插入新数据
		dsDetail.insert(count, record);
		dsDetail.totalLength = dsDetail.getTotalCount() + 1;
		dsDetail.commitChanges();
		gridDetail.getView().refresh();
//		gridDetail.getBottomToolbar().updateInfo();
		// 开始编辑新记录第一列
		gridDetail.startEditing(count, 1);
	}

	/**
	 * 明细删除处理
	 */
	function btnDeleteDetailHandler() {
		var record = gridDetail.selModel.getSelected();
		if (gridDetail.selModel.hasSelection()) {
			// 如果选中一行则删除
			dsDetail.remove(record);
			dsDetail.totalLength = dsDetail.getTotalCount() - 1;
			gridDetail.getView().refresh();
			gridDetail.getBottomToolbar().updateInfo();
			// 如果不是新增加的记录, 保存删除的流水号
			if (!record.get('isNewRecord')) {
				var deleteRecord = new Object();
				deleteRecord['id'] = record.get('id');
				deleteRecord['effectiveFromDate'] = record
						.get('effectiveFromDate')
				deleteRecord['effectiveToDate'] = record.get('effectiveToDate');
				deleteRecord['ifQb'] = record.get('ifQb');
				deleteRecord['aptitudeId'] = record.get('aptitudeId');
				deleteRecord['detailLastModifyDate'] = record
						.get('detailLastModifyDate');
				deleteRecord['aptitudeName'] = record.get('aptitudeName');
				deleteDetailId.push((deleteRecord));
			}
		} else {
			if (dsDetail.getCount() > 0) {
				// 否则弹出提示信息
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, '&nbsp&nbsp&nbsp'
								+ Constants.COM_I_001);
			}
		}
	}

	/**
	 * 判断表单数据是否改变
	 */
	function isFormChange() {
		// 获取现在的表单值
		var objForm = formRegister.getForm().getValues();
		// 循环判断
		for (var prop in originalData) {
			if (objForm[prop] != originalData[prop]) {
				return true;
			}
		}

		// 供应商信息增加时候，判断是否有项目有输入
		if (originalData == null) {
			if (objForm['supply.clientCode'] || objForm['supply.clientName']
					|| objForm['supply.corporation']
					|| objForm['supply.burdenman']
					|| objForm['supply.characterId'] || objForm['supply.nsrzg']
					|| objForm['supply.zczj'] || objForm['supply.clienttypeId']
					|| objForm['supply.bankaccount']
					|| objForm['supply.account'] || objForm['supply.telephone']
					|| objForm['supply.gscz'] || objForm['supply.swcz']
					|| objForm['supply.lxr1'] || objForm['supply.telephone1']
					|| objForm['supply.mobile1'] || objForm['supply.mail1']
					|| objForm['supply.lxr2'] || objForm['supply.telephone2']
					|| objForm['supply.mobile2'] || objForm['supply.mail2']
					|| objForm['supply.address']
					|| objForm['supply.postmaster']
					|| objForm['supply.intetadd'] || objForm['supply.zycp']
					|| objForm['supply.clientsurvey']) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断明细信息是否改变
	 */
	function isDetailChange() {
		// 新库位记录
		var newRecs = getDetailList(true);
		// 如果有新增记录返回true
		if (newRecs.length > 0) {
			return true;
		}

		// 原db记录
		var oldRecs = getDetailList();
		// 长度不同,有被删除的记录，返回true
		if (oldRecs.length != originalDetailData.length) {
			return true;
		}

		// 按流水号排序
		sortLocationsById(oldRecs);
		sortLocationsById(originalDetailData);
		for (var i = 0; i < oldRecs.length; i++) {
			if (oldRecs[i]['effectiveFromDate'] != originalDetailData[i]['effectiveFromDate']) {
				return true
			};
			if (oldRecs[i]['effectiveToDate'] != originalDetailData[i]['effectiveToDate']) {
				return true
			};
			if (oldRecs[i]['aptitudeId'] != originalDetailData[i]['aptitudeId']) {
				return true
			};
			if (oldRecs[i]['ifQb'] != originalDetailData[i]['ifQb']) {
				return true
			}
		}
		return false;
	}

	/**
	 * 判断是否有未保存的数据
	 */
	function hasNewRecord() {
		if (isFormChange() || isDetailChange()) {
			return true;
		}
		return false;
	}

	/**
	 * 拷贝明细部信息
	 */
	function cloneLocationRecord(record) {
		var objClone = new Object();
		// 拷贝属性
		objClone['id'] = record['id'];
		objClone['effectiveFromDate'] = record['effectiveFromDate'];
		objClone['effectiveToDate'] = record['effectiveToDate'];
		objClone['ifQb'] = record['ifQb'];
		objClone['aptitudeId'] = record['aptitudeId'];
		objClone['detailLastModifyDate'] = record['detailLastModifyDate'];
		objClone['aptitudeName'] = record['aptitudeName'];
		return objClone;
	}

	/**
	 * 获取明细信息，db已存在的和新增加的数据分开保存
	 */
	function getDetailList(isNew) {
		// 记录
		var records = new Array();
		// 循环
		for (var index = 0; index < dsDetail.getCount(); index++) {
			var record = dsDetail.getAt(index).data;
			if (isNew) {
				// 新记录
				if (record.isNewRecord) {
					records.push(cloneLocationRecord(record));
				}
			} else {
				// db中原有记录
				if (!record.isNewRecord) {
					records.push(cloneLocationRecord(record));
				}
			}
		}
		return records;
	}

	/**
	 * 检测明细是否有重复
	 */
	function isDetailRepeat() {
		var msg = Constants.COM_E_007;
		for (var i = 0; i < dsDetail.getCount(); i++) {
			for (var j = i + 1; j < dsDetail.getCount(); j++) {
				if (dsDetail.getAt(i).get('aptitudeName') == dsDetail.getAt(j)
						.get('aptitudeName')) {
					return msg.replace("{0}", "资质证书名称:'"
									+ dsDetail.getAt(i).get('aptitudeName')
									+ "'");
				}
			}
		}
		return "";
	}

	/**
	 * 渲染时间格式
	 */
	function renderDate(value) {
		return value ? value.dateFormat('Y-m-d') : '';
	}

	/**
	 * 资质证书名称COMBOBOX渲染功能
	 */
	function cmbQualificationNameHandler(value, cellmeta, record) {
		url = 'resource/getQualificationName.action?id=' + value;
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("POST", url, false);
		conn.send(null);
		record.data['aptitudeName'] = conn.responseText;
		return conn.responseText;
	}

	/**
	 * 弹出窗口关闭处理
	 */
	function popWinHandler() {
		if (popWinFlg == 1) {
			txtName.setValue(txtDetail.getValue());
			txtName.allowBlank = false;
		}
		if (popWinFlg == 2) {
			txtEligibleTaxpayers.setValue(txtDetail.getValue());
		}
		if (popWinFlg == 3) {
			txtBankAccount.setValue(txtDetail.getValue());
		}
		if (popWinFlg == 4) {
			txtOfficeAddress.setValue(txtDetail.getValue());
		}
		if (popWinFlg == 5) {
			txtMainBusinessProduct.setValue(txtDetail.getValue());
		}
		if (popWinFlg == 6) {
			txtMainBusinessResult.setValue(txtDetail.getValue());
		}
		win.hide();
	}

	/**
	 * textField显示时间比较方法
	 */
	function compareDate(argDate1, argDate2) {
		return argDate1.getTime() >= argDate2.getTime();
	}

	/**
	 * textField显示时间比较方法
	 */
	function compareDateStr(argDateStr1, argDateStr2) {
		var date1 = Date.parseDate(argDateStr1, 'Y-m-d');
		var date2 = Date.parseDate(argDateStr2, 'Y-m-d');
		return compareDate(date1, date2);
	}

	/**
	 * 按照流水号排序
	 */
	function sortLocationsById(records) {
		var intLen = records.length;
		var temp = null;
		for (var i = intLen - 1; i > 1; i--) {
			for (var j = 0; j < i; j++) {
				if (records[j].aptitudeId > records[j + 1].aptitudeId) {
					temp = records[j];
					records[j] = records[j + 1];
					records[j + 1] = temp;
				}
			}
		}
	}
})

// ↓↓********************grid插件，用来显示一行checkbox********************↓↓ //
Ext.grid.CheckColumn = function(config) {
	Ext.apply(this, config);
	if (!this.id) {
		this.id = Ext.id();
	}
	this.renderer = this.renderer.createDelegate(this);
};

Ext.grid.CheckColumn.prototype = {
	init : function(grid) {
		this.grid = grid;
		this.grid.on('render', function() {
					var view = this.grid.getView();
					view.mainBody.on('mousedown', this.onMouseDown, this);
				}, this);
	},

	onMouseDown : function(e, t) {
		if (t.className && t.className.indexOf('x-grid3-cc-' + this.id) != -1) {
			e.stopEvent();
			var index = this.grid.getView().findRowIndex(t);
			var record = this.grid.store.getAt(index);
			if (record.data[this.dataIndex] == Constants.CHECKED_VALUE) {
				record.set(this.dataIndex, Constants.UNCHECKED_VALUE);
			} else {
				record.set(this.dataIndex, Constants.CHECKED_VALUE);
			}
		}
	},

	renderer : function(v, p, record) {
		p.css += ' x-grid3-check-col-td';
		return '<div class="x-grid3-check-col' + (v == 'Y' ? '-on' : '')
				+ ' x-grid3-cc-' + this.id + '">&#160;</div>';
	}
};