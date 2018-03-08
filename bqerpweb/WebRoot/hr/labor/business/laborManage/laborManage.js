Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	function getYear() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10);
		return s;
	}
		var temp="";
	var mainYear = new Ext.form.TextField({
				id : 'mainYear',
				fieldLabel : '年度',
				readOnly : true,
				width : 100,
				value : getYear(),
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y',
									alwaysUseStartDate : false,
									dateFmt : 'yyyy',
									isShowClear : false,
									onpicked : function(v) {
										this.blur();
									}
								});
					}
				}
			});

	// 年度类别
	var typeYear = new Ext.data.SimpleStore({
				data : [['0', '上半年'], ['1', '下半年'], ['2', '全年']],
				fields : ['value', 'text']
			})
	var yearType = new Ext.form.ComboBox({
				id : 'yearType',
				fieldLabel : '年度类别',
				store : typeYear,
				displayField : 'text',
				valueField : 'value',
				readOnly : true,
				hiddenName : 'hyearType',
				mode : 'local',
				triggerAction : 'all',
				width : 85,
				value : '2'
			})
	// 保险类别
	var typeInsurance = new Ext.data.SimpleStore({
				data : [['1', '养老保险'], ['4', '医疗保险'], ['3', '失业保险'],
						['5', '公积金'], ['2', '企业年金'], ['0', '医疗保险个人账户']],
				fields : ['value', 'text']
			})
	var insuranceType = new Ext.form.ComboBox({
				id : 'insuranceTyped',
				fieldLabel : '保险类别',
				store : typeInsurance,
				displayField : 'text',
				valueField : 'value',
				readOnly : true,
				hiddenName : 'hinsuranceType',
				mode : 'local',
				triggerAction : 'all',
				width : 150,
				value : '1'
			})
	insuranceType.on('select', function() {
				controlHidden(insuranceType.getValue())
				getMemo(insuranceType.getValue());
				getFormType(insuranceType.getValue());
			})
	// 导入文档
	var tfAppend = new Ext.form.TextField({
				id : 'xlsFile',
				name : 'xlsFile',
				inputType : 'file',
				width : 200
			})
	// 导入按钮
	var btnInport = new Ext.Toolbar.Button({
				id : 'btuInport',
				text : '导入',
				handler : inportField,
				iconCls : 'upLoad'
			});
	// 导出按钮
	var btnExport = new Ext.Toolbar.Button({
				id : 'btnExport',
				text : '导出',
				handler : exportField,
				iconCls : 'upLoad'
			})
	var deptName = new Ext.form.TextField({
				id : 'deptName',
				fieldLabel : '部门:'
			})
	var personelName = new Ext.form.TextField({
				id : 'personelName',
				fieldLabel : '姓名:'
			})

	function saveRecord() {
		
		var url = 'hr/addOrUpdateMange.action';
		if (!form.getForm().isValid()) {
			return false;
		}

		form.getForm().submit({
					url : url,
					method : 'post',
					params : {
						method : method,
						strYear : mainYear.getValue(),
						yearType : yearType.getValue(),
						insuranceType : insuranceType.getValue()

					},
					success : function(form, action) {

						var message = eval('(' + action.response.responseText
								+ ')');
						Ext.Msg.alert("提示", message.msg);
						queryRecord();
						win.hide();
					},
					failure : function(form, action) {
						Ext.Msg.alert('错误', '出现未知错误.');
					}
				})
	};
	function cancer() {
		form.getForm().reset();
		win.hide();
		runGridStore.reload();

	};
	var btnQuert = new Ext.Button({
				id : 'btnQuert',
				text : '查询',
				iconCls : 'query',
				handler : queryRecord
			})
	var btnDel = new Ext.Button({
				id : 'btndel',
				text : '删除',
				iconCls : 'delete',
				handler : delRecord
			})
	var btnAdd = new Ext.Button({
				id : 'btnAdd',
				text : '增加',
				iconCls : 'add',
				handler : addRecord

			})
	var btnUpdate = new Ext.Button({
				id : 'btnUpdate',
				text : '修改',
				iconCls : 'update',
				handler : updateRecord

			})

	var btnAllDelete = new Ext.Button({
				id : 'btnAllDelete',
				text : '全部删除',
				iconCls : Constants.CLS_RESUME_LEAVE,
				handler : allDetele
			})
	//  ----------------add by wpzhu 20100721--------------------------
			
			
	
	var watchbt = new Ext.Button({
		id:"watchbt",
		text : '查看通知文档',
		handler : function() {
		if(temp=="")
		{
			Ext.MessageBox.alert("提示","无附件可查看！");
			return;
		}
		
			/*if (annex.getValue() == null
					|| annex.getValue() == '') {
				Ext.Msg.alert('提示', '无附件可查看!');
				return;
			}
			*/
//			window.open(Ext.get('annex').dom.value);
			window.open(temp);
		}
	})
	var annex = new Ext.form.TextField({
				id : 'annex',
				fieldLabel:'上传通知文档',
				name : 'annex',
				inputType : 'file',
				width : 200
			})
	
	// 上传按钮
	var btnfile = new Ext.Toolbar.Button({
				id : 'btnfile',
				text : '上传通知文档',
				handler : uploadfile,
				iconCls : 'upload'
	
				});
		//add by wpzhu 20100722----------------	
	function uploadfile() {
			filePath = Ext.get("annex").dom.value;
		var url = 'hr/uploadfile.action';
		if (filePath==null||filePath=="") {
				Ext.Msg.alert('提示', '请选择文件.');
			  return false;
		}
		if(filePath==temp)
		{
				Ext.Msg.alert('提示', '请重新选择要上传的文件.');
			   return false;
		}
		headPanel.getForm().submit({
							method : 'POST',
							url : url,
							params : {
								filePath : Ext.get("annex").dom.value,
								mainYear : mainYear.getValue(),
								yearType : yearType.getValue(),
								insuranceType : insuranceType.getValue()
							},
							success : function(form, action) {
								var o = eval("(" + action.response.responseText
										+ ")");
								temp=o.msg;
								 Ext.get("annex").dom.value=o.msg;
							
								Ext.MessageBox.alert('提示', '上传成功!');
								watchbt.setVisible(true);
								annex.fieldLabel="修改通知文档";
								queryRecord();
								init();
							},
							failture : function() {
								Ext.Msg.alert(Constants.ERROR, "上传失败！");
							}
						})
   
	
	};
//--------------------------------------------------------------------
	var win;
	var form;
	var content;
	// -----------------------
	var detailId;
	var personnelCode;
	var Name;
	var dept;
	var banzuName;
	var halfYearSalary;
	var monthBase;
	var deductCriterion;
	var memo;
	var oldselfSign;
	// ---------------------------
	var yeardetailId;
	var yearCountName;
	var yearCountdept;
	var paymentMonth;
	var yearCountCode;
	var YearidentityCardNumber;
	var monthEnterAccount;
	var monthPersonnelAccount;
	var monthTotal;
	var yearselfSign;
	// ---------------------------------
	var heathdetailId;
	var heathName;
	var heathdept;
	var manualNumber;
	var medicareCardNumber;
	var identityCardNumber;
	var accountNum;

	// ---------------------
	function getFormType(type) {
		if (type == "1" || type == "3" || type == "4") {
			detailId = new Ext.form.TextField({
						name : 'oldage.detailId',
						xtype : 'hidden',
						hidden : true,
						hideLabel : true,
						anchor : '95%'
					});
			personnelCode = new Ext.form.TextField({
						fieldLabel : '人员编号',
						readOnly : false,
						allowBlank : false,
						width : 108,
						// id : 'personnelCode',
						name : "oldage.personnelCode",
						anchor : '95%'
					});
			Name = new Ext.form.TextField({
						fieldLabel : '姓名',
						readOnly : false,
						width : 108,
						name : "oldage.personelName",
						anchor : '95%'
					});
			dept = new Ext.form.TextField({
						fieldLabel : '部门',
						allowBlank : false,
						readOnly : false,
						width : 108,
						name : "oldage.deptName",
						anchor : '95%'
					});
			banzuName = new Ext.form.TextField({
						fieldLabel : '班组',
						readOnly : false,
						allowBlank : false,
						width : 108,
						// id : 'banzuName',
						name : "oldage.banzuName",
						anchor : '95%'
					});
			halfYearSalary = new Ext.form.NumberField({
						xtype : "numberfield",
						width : 108,
						fieldLabel : '本人上年实际月工资收入',
						name : "oldage.halfYearSalary",
						anchor : '95%',
						allowBlank : false,
						allowDecimals : true,
						decimalPrecision : 8

					});
			monthBase = new Ext.form.NumberField({
						xtype : "numberfield",
						width : 108,
						fieldLabel : '月缴费基数',
						name : "oldage.monthBase",
						anchor : '95%',
						allowBlank : false,
						allowDecimals : true,
						decimalPrecision : 8

					});
			monthBase.on('blur', function() {
						if (type == 1) {
							deductCriterion.setValue(monthBase.getValue()
									* 0.08);
						}
						if (type == 3) {
							deductCriterion.setValue(monthBase.getValue()
									* 0.01);
						}
						if (type == 4) {
							deductCriterion.setValue(monthBase.getValue()
									* 0.02);
						}
						if (type == 5) {
							deductCriterion.setValue(monthBase.getValue()
									* 0.08);
						}
					})

			deductCriterion = new Ext.form.NumberField({
						xtype : "numberfield",
						width : 108,
						fieldLabel : '个人扣款月标准',
						name : "oldage.deductCriterion",
						anchor : '95%',
						allowBlank : false,
						allowDecimals : true,
						decimalPrecision : 8
					});

			memo = new Ext.form.TextArea({
						fieldLabel : '备注',
						readOnly : false,
						width : 108,
						// id : 'memo',
						name : "oldage.memo",
						anchor : '95%'

					})
			oldselfSign = new Ext.form.TextField({
						fieldLabel : '职工本人签字',
						readOnly : false,
						allowBlank : false,
						width : 108,
						// id : 'selfSign',
						name : "oldage.selfSign",
						anchor : '95%'
					});
			var btnSave1 = new Ext.Button({
						id : 'btnSave1',
						text : '保存',
						iconCls : 'save',
						handler : saveRecord
					})
			var btnCancer1 = new Ext.Button({
						id : 'btndel1',
						text : '取消',
						iconCls : 'cancer',
						handler : cancer
					})
			content = new Ext.form.FieldSet({
						height : '100%',
						layout : 'form',
						items : [detailId, personnelCode, Name, dept,
								banzuName, halfYearSalary, monthBase,
								deductCriterion, memo, oldselfSign]

					});
			form = new Ext.form.FormPanel({
						bodyStyle : "padding:5px 5px 0",
						labelAlign : 'right',
						labelWidth : 120,
						autoHeight : true,
						fileUpload : true,
						region : 'center',
						border : false,
						items : [content],
						buttons : [btnSave1, btnCancer1]
					});
			win = new Ext.Window({
						title : '新增',
						modal : true,
						heigh : 500,
						width : 450,
						closeAction : 'hide',
						items : [form]
					});

		}
		else if(type == "5") {
			//add by fyyang
			detailId = new Ext.form.TextField({
						name : 'oldage.detailId',
						xtype : 'hidden',
						hidden : true,
						hideLabel : true,
						anchor : '95%'
					});
			personnelCode = new Ext.form.TextField({
						fieldLabel : '个人账户',
						readOnly : false,
						allowBlank : false,
						width : 108,
						// id : 'personnelCode',
						name : "oldage.personnelCode",
						anchor : '95%'
					});
			Name = new Ext.form.TextField({
						fieldLabel : '姓名',
						readOnly : false,
						width : 108,
						name : "oldage.personelName",
						anchor : '95%'
					});
			dept = new Ext.form.TextField({
						fieldLabel : '部门',
						allowBlank : false,
						readOnly : false,
						width : 108,
						name : "oldage.deptName",
						anchor : '95%'
					});
			banzuName = new Ext.form.TextField({
						fieldLabel : '班组',
						readOnly : false,
						allowBlank : false,
						width : 108,
						// id : 'banzuName',
						name : "oldage.banzuName",
						anchor : '95%'
					});
			halfYearSalary = new Ext.form.NumberField({
						xtype : "numberfield",
						width : 108,
						fieldLabel : '本人上年实际月工资收入',
						name : "oldage.halfYearSalary",
						anchor : '95%',
						allowBlank : false,
						allowDecimals : true,
						decimalPrecision : 8

					});
			monthBase = new Ext.form.NumberField({
						xtype : "numberfield",
						width : 108,
						fieldLabel : '企业月标准',
						name : "oldage.monthStandard",
						anchor : '95%',
						allowBlank : false,
						allowDecimals : true,
						decimalPrecision : 8

					});
			halfYearSalary.on('blur', function() {
						
							deductCriterion.setValue(halfYearSalary.getValue()
									* 0.08);
						
					})

			deductCriterion = new Ext.form.NumberField({
						xtype : "numberfield",
						width : 108,
						fieldLabel : '个人扣款月标准',
						name : "oldage.deductCriterion",
						anchor : '95%',
						allowBlank : false,
						allowDecimals : true,
						decimalPrecision : 8
					});

			memo = new Ext.form.TextArea({
						fieldLabel : '备注',
						readOnly : false,
						width : 108,
						// id : 'memo',
						name : "oldage.memo",
						anchor : '95%'

					})
			oldselfSign = new Ext.form.TextField({
						fieldLabel : '职工本人签字',
						readOnly : false,
						allowBlank : false,
						width : 108,
						// id : 'selfSign',
						name : "oldage.selfSign",
						anchor : '95%'
					});
			var btnSave1 = new Ext.Button({
						id : 'btnSave1',
						text : '保存',
						iconCls : 'save',
						handler : saveRecord
					})
			var btnCancer1 = new Ext.Button({
						id : 'btndel1',
						text : '取消',
						iconCls : 'cancer',
						handler : cancer
					})
			content = new Ext.form.FieldSet({
						height : '100%',
						layout : 'form',
						items : [detailId, personnelCode, Name, dept,
								banzuName, halfYearSalary, monthBase,
								deductCriterion, memo, oldselfSign]

					});
			form = new Ext.form.FormPanel({
						bodyStyle : "padding:5px 5px 0",
						labelAlign : 'right',
						labelWidth : 120,
						autoHeight : true,
						fileUpload : true,
						region : 'center',
						border : false,
						items : [content],
						buttons : [btnSave1, btnCancer1]
					});
			win = new Ext.Window({
						title : '新增',
						modal : true,
						heigh : 500,
						width : 450,
						closeAction : 'hide',
						items : [form]
					});

		}
		else if (type == "2") {
			yeardetailId = new Ext.form.TextField({
						// id : 'detailId',
						name : 'yearCount.detailId',
						xtype : 'hidden',
						hidden : true,
						hideLabel : true,
						anchor : '95%'
					});
			yearCountName = new Ext.form.TextField({
						fieldLabel : '姓名',
						allowBlank : false,
						readOnly : false,
						width : 108,
						name : "yearCount.personelName",
						anchor : '95%'
					});
			yearCountdept = new Ext.form.TextField({
						fieldLabel : '部门',
						readOnly : false,
						allowBlank : false,
						width : 108,
						name : "yearCount.deptName",
						anchor : '95%'
					});
			paymentMonth = new Ext.form.TextField({
						fieldLabel : '缴费月',
						allowBlank : false,
						readOnly : false,
						width : 108,
						// id : 'paymentMonth',
						name : "yearCount.paymentMonth",
						anchor : '95%'
					});
			yearCountCode = new Ext.form.TextField({
						fieldLabel : '年金帐号',
						allowBlank : false,
						readOnly : false,
						width : 108,
						// id : 'yearCountCode',
						name : "yearCount.yearCountCode",
						anchor : '95%'
					});
			YearidentityCardNumber = new Ext.form.TextField({
						fieldLabel : '身份证号',
						readOnly : false,
						allowBlank : false,
						width : 108,
						name : "yearCount.identityCardNumber",
						anchor : '95%'
					});

			monthEnterAccount = new Ext.form.NumberField({
						xtype : "numberfield",
						width : 108,
						fieldLabel : '月企业缴费',
						name : "yearCount.monthEnterAccount",
						anchor : '95%',
						allowBlank : false,
						allowDecimals : true,
						decimalPrecision : 8

					});
			monthPersonnelAccount = new Ext.form.NumberField({
						xtype : "numberfield",
						width : 108,
						fieldLabel : '月个人缴费',
						name : "yearCount.monthPersonnelAccount",
						anchor : '95%',
						allowBlank : false,
						allowDecimals : true,
						decimalPrecision : 8

					});
			monthTotal = new Ext.form.NumberField({
						xtype : "numberfield",
						width : 108,
						fieldLabel : '月缴费合计',
						name : "yearCount.monthTotal",
						anchor : '95%',
						allowBlank : false,
						allowDecimals : true,
						decimalPrecision : 8

					});

			yearselfSign = new Ext.form.TextField({
						fieldLabel : '职工本人签字',
						readOnly : false,
						allowBlank : false,
						width : 108,
						// id : 'selfSign',
						name : "yearCount.selfSign",
						anchor : '95%'
					});
			var btnSave2 = new Ext.Button({
						id : 'btnSave2',
						text : '保存',
						iconCls : 'save',
						handler : saveRecord
					})
			var btnCancer2 = new Ext.Button({
						id : 'btncan2',
						text : '取消',
						iconCls : 'cancer',
						handler : cancer
					})
			// win.setTitle("增加企业年金详细信息");
			content = new Ext.form.FieldSet({
						id : "content2",
						height : '100%',
						layout : 'form',
						items : [yeardetailId, yearCountName, yearCountdept,
								paymentMonth, yearCountCode,
								YearidentityCardNumber, monthEnterAccount,
								monthPersonnelAccount, monthTotal, yearselfSign]
					});
			form = new Ext.form.FormPanel({
						bodyStyle : "padding:5px 5px 0",
						labelAlign : 'right',
						id : 'form1',
						labelWidth : 120,
						autoHeight : true,
						fileUpload : true,
						region : 'center',
						border : false,
						items : [content],
						buttons : [btnSave2, btnCancer2]
					});
			win = new Ext.Window({
						title : '新增',
						modal : true,
						heigh : 500,
						width : 450,
						closeAction : 'hide',
						items : [form]
					});

		} else if (type == "0") {
			heathdetailId = new Ext.form.TextField({
						// id : 'detailId',
						name : 'health.detailId',
						xtype : 'hidden',
						hidden : true,
						hideLabel : true,
						anchor : '95%'
					});
			heathName = new Ext.form.TextField({
						fieldLabel : '姓名',
						readOnly : false,
						allowBlank : false,
						width : 108,
						name : "health.personelName",
						anchor : '95%'
					});
			heathdept = new Ext.form.TextField({
						fieldLabel : '部门',
						allowBlank : false,
						readOnly : false,
						width : 108,
						name : "health.deptName",
						anchor : '95%'
					});
			manualNumber = new Ext.form.TextField({
						fieldLabel : '手册号码',
						allowBlank : false,
						readOnly : false,
						width : 108,
						// id : 'manualNumber',
						name : "health.manualNumber",
						anchor : '95%'
					});
			medicareCardNumber = new Ext.form.TextField({
						fieldLabel : '医保卡号',
						allowBlank : false,
						readOnly : false,
						width : 108,
						// id : 'medicareCardNumber',
						name : "health.medicareCardNumber",
						anchor : '95%'
					});

			identityCardNumber = new Ext.form.TextField({
						fieldLabel : '身份证号',
						allowBlank : false,
						readOnly : false,
						width : 108,
						// id : 'identityCardNumber',
						name : "health.identityCardNumber",
						anchor : '95%'
					});

			accountNum = new Ext.form.NumberField({
						xtype : "numberfield",
						width : 108,
						fieldLabel : '合计',
						name : "health.accountNum",
						anchor : '95%',
						allowBlank : false,
						allowDecimals : true,
						decimalPrecision : 8

					});
			var btnSave3 = new Ext.Button({
						id : 'btnSave3',
						text : '保存',
						iconCls : 'save',
						handler : saveRecord
					})
			var btnCancer3 = new Ext.Button({
						id : 'btndel1',
						text : '取消',
						iconCls : 'cancer',
						handler : cancer
					})
			// win.setTitle("增加医疗保险个人账户详细信息");
			content = new Ext.form.FieldSet({
						id : "content3",
						height : '100%',
						layout : 'form',
						items : [heathdetailId, heathName, heathdept,
								manualNumber, medicareCardNumber,
								identityCardNumber, accountNum]
					});
			form = new Ext.form.FormPanel({
						bodyStyle : "padding:5px 5px 0",
						labelAlign : 'right',
						id : 'form2',
						labelWidth : 120,
						autoHeight : true,
						fileUpload : true,
						region : 'center',
						border : false,
						items : [content],
						buttons : [btnSave3, btnCancer3]
					});
			win = new Ext.Window({
						title : '新增',
						modal : true,
						heigh : 500,
						width : 450,
						closeAction : 'hide',
						items : [form]
					});

		}
	}

	getFormType("1");

	var method = "";
	function addRecord() {
		
		var type = insuranceType.getValue();
		getFormType(type);
		method = 'add';
		if (type == "1") {

			win.setTitle("增加养老保险详细信息");

		} else if (type == "3") {
			win.setTitle("增加失业保险详细信息");

		} else if (type == "4") {
			win.setTitle("增加医疗保险详细信息");

		} else if (type == "5") {
			win.setTitle("增加公积金详细信息");

		} else if (type == "0") {
			win.setTitle("增加医疗保险个人账户详细信息");

		} else if (type == "2") {
			win.setTitle("增加企业年金详细信息");

		}
		form.getForm().reset();
		win.show();

	}

	function delRecord() {
		var selrows = runGrid.getSelectionModel().getSelections();
		var record = runGrid.getSelectionModel().getSelected();
		if (selrows.length > 0) {
			var ids = [];
			for (var i = 0; i < selrows.length; i += 1) {
				var member = selrows[i].data;
				if (member.detailId) {
					ids.push(member.detailId);
				} else {
				}
			}
			if (record.get("personelName") == "总合计") {
				Ext.MessageBox.alert('提示', '合计行无法删除！')
				return;

			}
			Ext.Msg.confirm('提示', '删除的数据您将不能恢复,确定要删除吗?', function(b) {
						if (b == "yes") {
							Ext.Ajax.request({
										url : 'hr/delLaborManageList.action',
										params : {
											type : insuranceType.getValue(),
											id : ids.join(",")
										},
										method : 'post',
										waitMsg : '正在删除数据...',
										success : function(result, request) {
											Ext.MessageBox.alert('提示', '删除成功!');
											queryRecord();
										},
										failure : function(result, request) {
											Ext.MessageBox.alert('错误',
													'操作失败,请联系管理员!');
										}
									});
						} else {
							queryRecord();
						}

					});

		} else {
			Ext.Msg.alert('提示', '请选择您要删除的记录！');
		}
	}
	// add by ltong 20100625 全部删除
	function allDetele() {
		Ext.Msg.confirm('提示', '删除的数据您将不能恢复,确定要全部删除吗?', function(b) {
					if (b == "yes") {
						Ext.Ajax.request({
									url : 'hr/delAllLaborManageList.action',
									params : {
										strYear : mainYear.getValue(),
										yearType : yearType.getValue(),
										insuranceType : insuranceType
												.getValue(),
										deptName : deptName.getValue(),
										workName : personelName.getValue()
									},
									method : 'post',
									waitMsg : '正在删除数据...',
									success : function(result, request) {
										Ext.MessageBox.alert('提示', '删除成功!');
										queryRecord();
									},
									failure : function(result, request) {
										Ext.MessageBox.alert('错误',
												'操作失败,请联系管理员!');
									}
								});
					} else {
						queryRecord();
					}

				});
	}
	function updateRecord() {
		watchbt.setVisible(true);
			
		method = 'update';
		var type = insuranceType.getValue();
		getFormType(type);
		var selrows = runGrid.getSelectionModel().getSelections();
		if (type == "1") {

			win.setTitle("修改养老保险详细信息");
			if (selrows.length > 0) {
				var record = runGrid.getSelectionModel().getSelected();
				form.getForm().reset();
				win.show();
				form.getForm().loadRecord(record);
				detailId.setValue(record.get("detailId"));
				Name.setValue(record.get("personelName"));
				dept.setValue(record.get("deptName"));
				personnelCode.setValue(record.get("personnelCode"));
				banzuName.setValue(record.get("banzuName"))
				halfYearSalary.setValue(record.get("halfYearSalary"));
				monthBase.setValue(record.get("monthBase"));
				deductCriterion.setValue(record.get("deductCriterion"));
				memo.setValue(record.get("memo"))
				oldselfSign.setValue(record.get("selfSign"));

			} else {
				Ext.Msg.alert('提示', '请选择您要修改记录！');
			}

		} else if (type == "3") {
			win.setTitle("修改失业保险详细信息");
			if (selrows.length > 0) {
				var record = runGrid.getSelectionModel().getSelected();
				form.getForm().reset();
				win.show();
				form.getForm().loadRecord(record);
				detailId.setValue(record.get("detailId"));
				Name.setValue(record.get("personelName"));
				dept.setValue(record.get("deptName"));
				personnelCode.setValue(record.get("personnelCode"));
				banzuName.setValue(record.get("banzuName"))
				halfYearSalary.setValue(record.get("halfYearSalary"));
				monthBase.setValue(record.get("monthBase"));
				deductCriterion.setValue(record.get("deductCriterion"));
				memo.setValue(record.get("memo"))
				oldselfSign.setValue(record.get("selfSign"));

			} else {
				Ext.Msg.alert('提示', '请选择您要修改记录！');
			}

		}

		else if (type == "4") {
			win.setTitle("修改医疗保险详细信息");
			if (selrows.length > 0) {
				var record = runGrid.getSelectionModel().getSelected();
				form.getForm().reset();
				win.show();
				form.getForm().loadRecord(record);
				detailId.setValue(record.get("detailId"));
				Name.setValue(record.get("personelName"));
				dept.setValue(record.get("deptName"));
				personnelCode.setValue(record.get("personnelCode"));
				banzuName.setValue(record.get("banzuName"))
				halfYearSalary.setValue(record.get("halfYearSalary"));
				monthBase.setValue(record.get("monthBase"));
				deductCriterion.setValue(record.get("deductCriterion"));
				memo.setValue(record.get("memo"))
				oldselfSign.setValue(record.get("selfSign"));

			} else {
				Ext.Msg.alert('提示', '请选择您要修改记录！');
			}

		} else if (type == "5") {
			win.setTitle("修改公积金详细信息");
			if (selrows.length > 0) {
				var record = runGrid.getSelectionModel().getSelected();
				form.getForm().reset();
				win.show();
				form.getForm().loadRecord(record);
				detailId.setValue(record.get("detailId"));
				Name.setValue(record.get("personelName"));
				dept.setValue(record.get("deptName"));
				personnelCode.setValue(record.get("personnelCode"));
				banzuName.setValue(record.get("banzuName"))
				halfYearSalary.setValue(record.get("halfYearSalary"));
				monthBase.setValue(record.get("monthStandard"));
				deductCriterion.setValue(record.get("deductCriterion"));
				memo.setValue(record.get("memo"))
				oldselfSign.setValue(record.get("selfSign"));

			} else {
				Ext.Msg.alert('提示', '请选择您要修改记录！');
			}

		}

		else if (type == "0") {
			win.setTitle("修改医疗保险个人账户详细信息");
			if (selrows.length > 0) {
				var record = runGrid.getSelectionModel().getSelected();
				form.getForm().reset();
				win.show();
				form.getForm().loadRecord(record);
				heathdetailId.setValue(record.get("detailId"));
				heathName.setValue(record.get("personelName"));
				heathdept.setValue(record.get("deptName"));
				manualNumber.setValue(record.get("manualNumber"));
				medicareCardNumber.setValue(record.get("medicareCardNumber"))
				identityCardNumber.setValue(record.get("identityCardNumber"));
				accountNum.setValue(record.get("accountNum"));

			} else {
				Ext.Msg.alert('提示', '请选择您要修改记录！');
			}

		}

		else if (type == "2") {
			win.setTitle("修改企业年金详细信息");
			if (selrows.length > 0) {
				var record = runGrid.getSelectionModel().getSelected();
				form.getForm().reset();
				win.show();
				form.getForm().loadRecord(record);
				yeardetailId.setValue(record.get("detailId"));
				yearCountName.setValue(record.get("personelName"));
				yearCountdept.setValue(record.get("deptName"));
				paymentMonth.setValue(record.get("paymentMonth"));
				yearCountCode.setValue(record.get("yearCountCode"));
				YearidentityCardNumber.setValue(record
						.get("identityCardNumber"))
				monthEnterAccount.setValue(record.get("monthEnterAccount"));
				monthPersonnelAccount.setValue(record
						.get("monthPersonnelAccount"));
				monthTotal.setValue(record.get("monthTotal"));
				yearselfSign.setValue(record.get("selfSign"));

			} else {
				Ext.Msg.alert('提示', '请选择您要修改记录！');
			}

		}

		// if (selrows.length > 0) {
		// var record = runGrid.getSelectionModel().getSelected();
		// // form.getForm().reset();
		// win.show();
		// alert(record.get("personelName"))
		// form.getForm().loadRecord(record);
		// // Name.setValue(record.get("personelName"));
		// // dept.setValue(record.get("deptName"));
		//			
		// // win.setTitle("修改养老失业保险详细信息");
		// } else {
		// Ext.Msg.alert('提示', '请选择您要修改记录！');
		// }
	}

	// grid中的数据
	var Record = new Ext.data.Record.create([{
				// id //add by wpzhu
				name : "detailId"
			}, {
				// 个人编号
				name : "personnelCode"
			}, {
				// 姓名
				name : "personelName"
			}, {
				// 部门
				name : "deptName"
			}, {
				// 班组
				name : "banzuName"
			}, {
				// 本人上年实际月工资收入
				name : "halfYearSalary"
			}, {
				// 月缴费基数
				name : "monthBase"
			}, {
				// 职工本人签字
				name : "selfSign"
			}, {
				// 备注
				name : "memo"
			}, {
				// 手册号码
				name : "manualNumber"
			}, {
				// 医保卡号
				name : "medicareCardNumber"
			}, {
				// 身份证号
				name : "identityCardNumber"
			}, {
				// 合计
				name : "accountNum"
			}, {
				// 年金帐号
				name : "yearCountCode"
			}, {
				// 月企业缴费
				name : "monthEnterAccount"
			}, {
				// 月个人缴费
				name : "monthPersonnelAccount"
			}, {
				// 月缴费合计
				name : "monthTotal"
			}, {
				// 缴费月
				name : "paymentMonth"
			}, {
				// 个人扣款月标准
				name : "deductCriterion"
			}, {
				// 企业月标准
				name : "monthStandard"
			}, {
				// 公积金合计
				name : "fundTotal"
			}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'hr/findLaborManageList.action'
			});

	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, Record);

	var runGridStore = new Ext.data.Store({
				proxy : dataProxy,
				reader : theReader
			});

	// 选择列
	var smCSM = new Ext.grid.CheckboxSelectionModel({
				id : "check",
				width : 35,
				singleSelect : false
			});

	var columnModel = new Ext.grid.ColumnModel([smCSM,
			new Ext.grid.RowNumberer({
						header : '序号',
						width : 35,
						align : 'center'
					}), {
				header : '个人编号',
				align : 'center',
				width : 100,
				dataIndex : 'personnelCode'
			}, {
				header : '姓名',
				align : 'center',
				width : 50,
				dataIndex : 'personelName'
			}, {
				header : '部门',
				align : 'center',
				width : 100,
				dataIndex : 'deptName'
			}, {
				header : '班组',
				align : 'center',
				width : 100,
				dataIndex : 'banzuName'
			}, {
				header : '本人上年实际月工资收入',
				align : 'center',
				width : 150,
				dataIndex : 'halfYearSalary'
			}, {
				header : '月缴费基数',
				align : 'center',
				dataIndex : 'monthBase'
			}, {
				header : '缴费月',
				align : 'center',
				dataIndex : 'paymentMonth'
			}, {
				header : '个人扣款月标准',
				align : 'center',
				dataIndex : 'deductCriterion'
			}, 
				{ //add by fyyang 
				header : '企业月标准',
				align : 'center',
				width : 190,
				dataIndex : 'monthStandard'
			},{//add by fyyang 
				header : '公积金合计',
				align : 'center',
				width : 180,
				dataIndex : 'fundTotal',
				renderer:function(value, cellmeta, record, rowIndex, columnIndex, store)
				{
					if(record.get("deductCriterion")!=null&&record.get("monthStandard")!=null)
					{
					return record.get("deductCriterion") +record.get("monthStandard");
					}
					else return "";
				}
			},{
				header : '备注',
				align : 'center',
				dataIndex : 'memo'
			}, {
				header : '手册号码',
				align : 'center',
				dataIndex : 'manualNumber'
			}, {
				header : '医保卡号',
				align : 'center',
				dataIndex : 'medicareCardNumber'
			}, {
				header : '身份证号',
				align : 'center',
				dataIndex : 'identityCardNumber'
			}, {
				header : '合计',
				align : 'center',
				dataIndex : 'accountNum'
			}, {
				header : '年金帐号',
				align : 'center',
				dataIndex : 'yearCountCode'
			}, {
				header : '月企业缴费',
				align : 'center',
				dataIndex : 'monthEnterAccount'
			}, {
				header : '月个人缴费',
				align : 'center',
				dataIndex : 'monthPersonnelAccount'
			}, {
				header : '月缴费合计',
				align : 'center',
				dataIndex : 'monthTotal'
			}, {
				header : '职工本人签字',
				align : 'center',
				dataIndex : 'selfSign'
			}]);
	// ---------------------------

	var headTbar = new Ext.Toolbar({
				items : ['年度：', mainYear, '-',
						'保险类型：', insuranceType, '-', tfAppend, btnInport,
						btnExport,'-',watchbt,'-',annex,btnfile]
			});

	var headPanel = new Ext.form.FormPanel({
				region : 'north',
				id : 'center-panel',
				frame : false,
				fileUpload : true,
				layout : 'form',
				items : [headTbar]
			});

	var runGrid = new Ext.grid.GridPanel({
				renderTo : 'mygrid',
				store : runGridStore,
				sm : smCSM,
				region : 'center',
				layout : 'fit',
				frame : false,
				border : false,
				enableColumnHide : true,
				enableColumnMove : false,
				autoWidth : true,
				tbar : headPanel,
				viewConfig : {
					forceFit : true
				},
				cm : columnModel,
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : runGridStore,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						})
			});

	// -----------tbar----
	new Ext.Toolbar({
				renderTo : runGrid.tbar,
				items : [ '年度类型:',yearType, '-','部门：', deptName, '-', '姓名：', personelName, '-',
						btnQuert, btnAdd, btnUpdate, btnDel, btnAllDelete]
			});
	var memoText1 = "1.基本养老保险个人扣款月标准为月缴费基数*0.08;\n";
	var memoText2 = "2.基本医疗保险个人扣款月标准为月缴费基数*0.02;\n";
	var memoText3 = "3.基本失业保险个人扣款月标准为月缴费基数*0.01;\n";
	var memoText4 = "4.基本公积金个人扣款月标准为月缴费基数*0.08;\n";
	var memoText5 = "5.企业年金：每满一年工龄*3元；";

	var totalMemo1 = new Ext.form.TextArea({
				id : "Memo1",
				fieldLabel : '备注',
				allowBlank : true,
				readOnly : false,
				value : memoText1,
				width : 980
			});

	var totalMemo;
	var Form;
	function getMemo(type) {

		if (type == "1") {
			totalMemo1.setValue(memoText1);

		} else if (type == "2") {
			totalMemo1.setValue(memoText5);
		} else if (type == "3") {
			totalMemo1.setValue(memoText3);

		} else if (type == "4") {
			totalMemo1.setValue(memoText2);

		} else if (type == "5") {
			totalMemo1.setValue(memoText4);

		} else {
			totalMemo1.setValue("");
		}
		totalMemo1.setDisabled(true);
	}
	getMemo("1");
	Form = new Ext.Toolbar({
				items : ["备注:", totalMemo1]

			});
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				items : [{
							region : 'center',
							layout : 'fit',
							items : [runGrid]
						}, {
							region : 'south',
							border : false,
							height : 110,
							items : [{
										xtype : 'panel',
										border : false,
										items : [{
													border : false,
													height : 75,
													items : [Form]
												}]
									}]
						}]
			});

	function queryRecord() {
		runGridStore.baseParams = {
			strYear : mainYear.getValue(),
			yearType : yearType.getValue(),
			insuranceType : insuranceType.getValue(),
			deptName : deptName.getValue(),
			workName : personelName.getValue()
		}
		runGridStore.load({
					params : {
						start : 0,
						limit : 18
					}
				})
				init();
	}

	function inportField() {
		var filePath = tfAppend.getValue();
		// 文件路径为空的情况
		if (filePath == "") {
			Ext.Msg.alert("提示", "请选择文件！");
			return;
		} else {
			// 取得后缀名并小写
			var suffix = filePath.substring(filePath.length - 3,
					filePath.length);
			if (suffix.toLowerCase() != 'xls')
				Ext.Msg.alert("提示", "导入的文件格式必须是Excel格式");
			else {
				Ext.Msg.wait("正在导入,请等待....");
				headPanel.getForm().submit({
							method : 'POST',
							url : 'hr/importLaborManageInfo.action',
							params : {
								mainYear : mainYear.getValue(),
								yearType : yearType.getValue(),
								insuranceType : insuranceType.getValue()
							},
							success : function(form, action) {
								var o = eval("(" + action.response.responseText
										+ ")");
								Ext.Msg.alert("提示", o.msg);
								queryRecord();
							},
							failture : function() {
								Ext.Msg.alert(Constants.ERROR, "导入失败！");
							}
						})
			}
		}
	}

	
	// 导出
	function tableToExcel(tableHTML) {
		window.clipboardData.setData("Text", tableHTML);
		try {
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
				ExWSh.Columns("A").ColumnWidth  = 13;
				//ExWSh.Cells.NumberFormatLocal = "@";
			ExApp.DisplayAlerts = false;
			ExApp.visible = true;
		} catch (e) {
			if (e.number != -2146827859)
				alert("您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}

	function exportField() {
		Ext.Ajax.request({
			url : 'hr/findLaborManageList.action',
			params : {
				strYear : mainYear.getValue(),
				yearType : yearType.getValue(),
				insuranceType : insuranceType.getValue(),
				deptName : deptName.getValue(),
				workName : personelName.getValue()
			},
			success : function(response) {
				var json = eval('(' + response.responseText.trim() + ')');
				var records = json.list;
				var typeValue = insuranceType.getValue();
				if (typeValue == '0') {
					var html = ['<table border=1><tr><th>姓名</th><th>部门</th><th>手机号码</th><th>医保卡号</th><th>身份证号</th><th>合计</th></tr>'];
					for (var i = 0; i < records.length; i += 1) {
						var rc = records[i];
						html.push('<tr><td>'
								+ (rc.personelName == null
										? ""
										: rc.personelName)
								+ '</td><td>'
								+ (rc.deptName == null ? "" : rc.deptName)
								+ '</td><td>'
								+ (rc.manualNumber == null
										? ""
										: rc.manualNumber)
								+ '</td><td>'
								+ (rc.medicareCardNumber == null
										? ""
										: rc.medicareCardNumber)
								+ '</td><td>'
								+ (rc.identityCardNumber == null
										? ""
										: rc.identityCardNumber) + '</td><td>'
								+ (rc.accountNum == null ? "" : rc.accountNum)
								+ '</td></tr>');
					}
				} else if (typeValue == '1' || typeValue == '3'
						 || typeValue == '4') {
					var html = ['<table border=1><tr><th>个人编号</th><th>姓名</th><th>部门</th><th>班组</th><th>本人上年实际月工资收入</th><th>月缴费基数</th><th>个人扣款月报标准</th><th>备注</th><th>职工本人签字</th></tr>'];
					for (var i = 0; i < records.length; i += 1) {
						var rc = records[i];
						html.push('<tr><td>'
								+ (rc.personnelCode == null
										? ""
										: rc.personnelCode)
								+ '&nbsp</td><td>'
								+ (rc.personelName == null
										? ""
										: rc.personelName)
								+ '</td><td>'
								+ (rc.deptName == null ? "" : rc.deptName)
								+ '</td><td>'
								+ (rc.banzuName == null ? "" : rc.banzuName)
								+ '</td><td>'
								+ (rc.halfYearSalary == null
										? ""
										: rc.halfYearSalary)
								+ '</td><td>'
								+ (rc.monthBase == null ? "" : rc.monthBase)
								+ '</td><td>'
								+ (rc.deductCriterion == null
										? ""
										: rc.deductCriterion) + '</td><td>'
								+ (rc.memo == null ? "" : rc.memo)
								+ '</td><td>'
								+ (rc.selfSign == null ? "" : rc.selfSign)
								+ '</td></tr>');
					}
				}
				else if (typeValue == '5'){
					var html = ['<table border=1><tr><th>公积金个人账户</th><th>姓名</th><th>部门</th><th>班组</th><th>本人上年实际月工资收入</th><th>个人扣款月报标准</th><th>企业月标准</th><th>公积金合计</th><th>备注</th><th>职工本人签字</th></tr>'];
					for (var i = 0; i < records.length; i += 1) {
						var rc = records[i];
						var fundTotal=rc.monthStandard +rc.deductCriterion;
						html.push('<tr><td>'
								+ (rc.personnelCode == null
										? ""
										: rc.personnelCode)
								+ '&nbsp</td><td>'
								+ (rc.personelName == null
										? ""
										: rc.personelName)
								+ '</td><td>'
								+ (rc.deptName == null ? "" : rc.deptName)
								+ '</td><td>'
								+ (rc.banzuName == null ? "" : rc.banzuName)
								+ '</td><td>'
								+ (rc.halfYearSalary == null
										? ""
										: rc.halfYearSalary)
								+ '</td><td>'
								+ (rc.deductCriterion == null
										? ""
										: rc.deductCriterion) 
								+ '</td><td>'
								+ (rc.monthStandard == null ? "" : rc.monthStandard)+ '</td><td>'
								+fundTotal+'</td><td>'
								+ (rc.memo == null ? "" : rc.memo)
								+ '</td><td>'
								+ (rc.selfSign == null ? "" : rc.selfSign)
								+ '</td></tr>');
					}
				}
				else if (typeValue == '2') {
					var html = ['<table border=1><tr><th>姓名</th><th>部门</th><th>缴费月</th><th>年金账号</th><th>月企业缴费</th><th>月个人缴费</th><th>月缴费合计</th><th>职工本人签字</th></tr>'];
					for (var i = 0; i < records.length; i += 1) {
						var rc = records[i];
						html.push('<tr><td>'
								+ (rc.personelName == null
										? ""
										: rc.personelName)
								+ '</td><td>'
								+ (rc.deptName == null ? "" : rc.deptName)
								+ '</td><td>'
								+ (rc.paymentMonth == null
										? ""
										: rc.paymentMonth)
								+ '</td><td>'
								+ (rc.yearCountCode == null
										? ""
										: rc.yearCountCode)
								+ '</td><td>'
								+ (rc.monthEnterAccount == null
										? ""
										: rc.monthEnterAccount)
								+ '</td><td>'
								+ (rc.monthPersonnelAccount == null
										? ""
										: rc.monthPersonnelAccount)
								+ '</td><td>'
								+ (rc.monthTotal == null ? "" : rc.monthTotal)
								+ '</td><td>'
								+ (rc.selfSign == null ? "" : rc.selfSign)
								+ '</td></tr>');
					}
				}
				html.push('</table>');
				html = html.join(''); // 最后生成的HTML表格
				// alert(html);
				tableToExcel(html);
			},
			failure : function(response) {
				Ext.Msg.alert('信息', '失败');
			}
		});
	}
	//add by wpzhu 20100722-------------------
	function init() {
		Ext.Ajax.request({
			url : 'hr/getAnnexVlaue.action',
			params : {
				strYear : mainYear.getValue(),
				yearType : yearType.getValue(),
				insuranceType : insuranceType.getValue()

			},
			method : 'post',
			success : function(response, options) {

				var res = response.responseText;
				if (res.toString() != "") {
					var str = res.toString();
					temp=str
				Ext.get("annex").dom.value=str;
				 Ext.get("watchbt").dom.style.display = ""
				}else
				{
					Ext.get("annex").dom.value="";
					temp="";
					watchbt.hidden=true;
					 Ext.get("watchbt").dom.style.display = "none"
					
				}
			},

			failure : function(result, request) {
				Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
			}
		});
	}

//--------------------------------------------------------------------------
	// 合计行
	// runGridStore.on("load", function() {
	// if (insuranceType.getValue() == '0') {
	// var totalAccountNum = 0; // 合计数量
	// for (var j = 0; j < runGridStore.getCount(); j++) {
	// var temp = runGridStore.getAt(j);
	// if (temp.get("accountNum") != null) {
	// totalAccountNum = parseFloat(totalAccountNum)
	// + parseFloat(temp.get("accountNum"));
	// }
	// }
	// if (runGridStore.getCount() > 0) {
	// var mytotal = new Record({
	// personelName : '合计',
	// accountNum : totalAccountNum.toFixed(2)
	// });
	// runGridStore.add(mytotal);
	// }
	// } else if (insuranceType.getValue() == '2') {
	// var totalMonthEnterAccount = 0; // 月企业缴费
	// var totalMonthPersonnelAccount = 0;// 月个人缴费
	// var totalMonthTotal = 0;// 月缴费合计
	// for (var j = 0; j < runGridStore.getCount(); j++) {
	// var temp = runGridStore.getAt(j);
	// if (temp.get("monthEnterAccount") != null) {
	// totalMonthEnterAccount = parseFloat(totalMonthEnterAccount)
	// + parseFloat(temp.get("monthEnterAccount"));
	// }
	// if (temp.get("monthPersonnelAccount") != null) {
	// totalMonthPersonnelAccount = parseFloat(totalMonthPersonnelAccount)
	// + parseFloat(temp.get("monthPersonnelAccount"));
	// }
	// if (temp.get("monthTotal") != null) {
	// totalMonthTotal = parseFloat(totalMonthTotal)
	// + parseFloat(temp.get("monthTotal"));
	// }
	// }
	// if (runGridStore.getCount() > 0) {
	// var mytotal = new Record({
	// deptName : '合计',
	// monthEnterAccount : totalMonthEnterAccount.toFixed(2),
	// monthPersonnelAccount : totalMonthPersonnelAccount.toFixed(2),
	// monthTotal : totalMonthTotal.toFixed(2)
	// });
	// runGridStore.add(mytotal);
	// }
	// }
	// });
  
	function controlHidden(type) {
		if (type == 0) {
		
			columnModel.setHidden(1, false);
			columnModel.setHidden(3, false);
			columnModel.setHidden(4, false);
			columnModel.setHidden(13, false);
			columnModel.setHidden(14, false);
			columnModel.setHidden(15, false);
			columnModel.setHidden(16, false);

			columnModel.setHidden(2, true);
			columnModel.setHidden(5, true);
			columnModel.setHidden(6, true);
			columnModel.setHidden(20, true);
			columnModel.setHidden(21, true);
			columnModel.setHidden(9, true);
			columnModel.setHidden(12, true);
			columnModel.setHidden(8, true);
			columnModel.setHidden(17, true);
			columnModel.setHidden(18, true);
			columnModel.setHidden(19, true);
			columnModel.setHidden(7, true);
			columnModel.setHidden(10, true);
			columnModel.setHidden(11, true);

			queryRecord();

		} else if (type == 1 || type == 3 || type == 4) {
			columnModel.setHidden(1, false);
			columnModel.setHidden(2, false);
			columnModel.setHidden(3, false);
			columnModel.setHidden(4, false);
			columnModel.setHidden(5, false);
			columnModel.setHidden(6, false);
			columnModel.setHidden(7, false);
			columnModel.setHidden(9, false);
			columnModel.setHidden(12, false);
			columnModel.setHidden(21, false);

			
			columnModel.setHidden(10, true);
			columnModel.setHidden(11, true);
			columnModel.setHidden(8, true);
			columnModel.setHidden(13, true);
			columnModel.setHidden(14, true);
			columnModel.setHidden(15, true);
			columnModel.setHidden(16, true);
			columnModel.setHidden(17, true);
			columnModel.setHidden(18, true);
			columnModel.setHidden(19, true);
			columnModel.setHidden(20, true);

			queryRecord();

		}
		 else if(type == 5) {
			columnModel.setHidden(1, false);
			columnModel.setHidden(2, false);
			columnModel.setHidden(3, false);
			columnModel.setHidden(4, false);
			columnModel.setHidden(5, false);
			columnModel.setHidden(6, false);
			columnModel.setHidden(7, true);
			columnModel.setHidden(9, false);
			columnModel.setHidden(12, false);
			columnModel.setHidden(21, false);

			
			columnModel.setHidden(10, false);
			columnModel.setHidden(11, false);
			columnModel.setHidden(8, true);
			columnModel.setHidden(13, true);
			columnModel.setHidden(14, true);
			columnModel.setHidden(15, true);
			columnModel.setHidden(16, true);
			columnModel.setHidden(17, true);
			columnModel.setHidden(18, true);
			columnModel.setHidden(19, true);
			columnModel.setHidden(20, true);

			queryRecord();

		}
		else if (type == 2) {
			columnModel.setHidden(1, false);
			columnModel.setHidden(3, false);
			columnModel.setHidden(4, false);
			columnModel.setHidden(8, false);
			columnModel.setHidden(17, false);
			columnModel.setHidden(18, false);
			columnModel.setHidden(19, false);
			columnModel.setHidden(20, false);
			columnModel.setHidden(21, false);
				columnModel.setHidden(15, false);

			columnModel.setHidden(2, true);
			columnModel.setHidden(5, true);
			columnModel.setHidden(6, true);
			columnModel.setHidden(7, true);
			columnModel.setHidden(9, true);
			columnModel.setHidden(10, true);
			columnModel.setHidden(11, true);
			columnModel.setHidden(12, true);
			columnModel.setHidden(13, true);
			columnModel.setHidden(14, true);
		
			columnModel.setHidden(16, true);

			queryRecord();
		}
	}
	controlHidden("1");
    init();
	if (hiddenBtn == "2") {
		Ext.get("xlsFile").dom.style.display = "none"; // 隐藏
		Ext.get("btuInport").dom.style.display = "none";
		Ext.get("btnAdd").dom.style.display = "none";
		Ext.get("btndel").dom.style.display = "none";
		Ext.get("btnUpdate").dom.style.display = "none";
		Ext.get("btnAllDelete").dom.style.display = "none";
		Ext.get("annex").dom.style.display = "none";
	    Ext.get("btnfile").dom.style.display = "none";
	    Ext.get("btnup").dom.style.display = "none"
	    
		btnfile.hidden=true;
		annex.hidden=true;

	} else if (hiddenBtn == "1") {
		Ext.get("btnExport").dom.style.display = "none";
		runGrid.on('rowdblclick', updateRecord);
	}

});