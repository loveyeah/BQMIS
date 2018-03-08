Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
	var empIdUsing = null;
	var empChsName = null;
	var empCodeUsing = null;

	// 修改按钮
	var btnModify = new Ext.Button({
				text : Constants.BTN_UPDATE,
				iconCls : Constants.CLS_UPDATE,
				handler : function() {
					setEnable(true);
					btnModify.setDisabled(true);
				}
			});
	// 保存按钮
	var btnSave = new Ext.Button({
				text : Constants.BTN_SAVE,
				iconCls : Constants.CLS_SAVE,
				handler : saveContact
			})
	function saveContact() {
		if (tfEmpCode.getValue() == null || tfEmpCode.getValue() == '') {
			Ext.Msg.alert('提示', '工号不可为空！');
			return;
		}
		Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_001, function(
						buttonobj) {
					if (buttonobj == "yes") {
						// 保存数据
						contactForm.getForm().submit({
							method : Constants.POST,
							url : 'hr/saveContactForm.action',
							success : function(form, action) {
								var o = eval('(' + action.response.responseText
										+ ')');
								Ext.Msg.alert(Constants.REMIND,
										Constants.COM_I_004);
								loadEmp()
								setEnable(false);
							},
							failure : function(form, action) {
								Ext.Msg.alert('提示', '数据保存失败!');
							}
						});
					}
				});
	}
	// 删除按钮
	var btnDelete = new Ext.Button({
				text : Constants.BTN_DELETE,
				iconCls : Constants.CLS_DELETE,
				handler : deleteContact
			});
	function deleteContact() {
		if (contactId.getValue() == null || contactId.getValue() == '') {
			Ext.Msg.alert('提示', '该员工没有联系信息！');
			return;
		}
		Ext.Msg.confirm('提示', '确定要删除该员工联系信息？', function(buttonobj) {
					if (buttonobj == "yes") {
						contactForm.getForm().submit({
									method : Constants.POST,
									url : 'hr/deleteContactForm.action',
									success : function(form, action) {
										Ext.Msg.alert('提示', '删除成功!');
										formReset()
									},
									failure : function(form, action) {
										Ext.Msg.alert('提示', '删除失败!');
									}
								});
					}
				});
	}

	var tfAppend = new Ext.form.TextField({
				name : 'xlsFile',
				inputType : 'file',
				width : 200
			})
	//----------begin----------
	function downloadMoudle(){
		window.open("../downloadMoudle/联系方式-模板.xls")
	}
	
	var btnDownload = new Ext.Toolbar.Button({
		id : 'btnDownload',
		text : '模板下载',
		handler : downloadMoudle,
		iconCls : 'export'
	});
	// 导入按钮
	var btnInport = new Ext.Toolbar.Button({
				id : 'contact_inport',
				text : '导入',
				handler : uploadQuestFile,
				iconCls : 'upLoad'
			});
	// 上传附件
	function uploadQuestFile() {
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
				headForm.getForm().submit({
							method : 'POST',
							url : 'hr/importContactForm.action',
							success : function(form, action) {
								var o = eval("(" + action.response.responseText
										+ ")");
								Ext.Msg.alert("提示", o.msg);
								loadEmp();
							},
							failture : function() {
								Ext.Msg.alert(Constants.ERROR, "导入失败！");
							}
						})
			}
		}
	}

	var headTbar = new Ext.Toolbar({
				items : [btnModify, btnSave, btnDelete, tfAppend, btnInport,btnDownload
				// add by sychen 20100713
    	                  ,{
							text : "导出",
							id : 'btnreport',
							iconCls : 'export',
							handler :function(){
			Ext.Ajax.request({
				url : 'hr/getContactForm.action',
				params : {
				empId : empIdUsing,
				start : 0,
                limit : Constants.PAGE_SIZE
			},
				method : 'post',
				success : function(response,options){
			var rec = eval('(' + response.responseText + ')');
			  if(rec){
				Ext.Msg.confirm('提示', '确认要导出数据？', function(id) {
			    if (id == 'yes') {
				var tableHeader = "<table border=1><tr><th>人员编码</th><th>载波电话 </th><th>外线电话</th><th>手机号码" +
						"</th><th>邮箱地址</th><th>邮政编码</th><th>联系地址</tr>";
				var html = [tableHeader];
				
   	        	    html.push('<tr><td align=left >' + (rec.newEmpCode==null?"":rec.newEmpCode) +'</td>' +
   	        	                          '<td align=left>'+( rec.contactCarrier==null?"":rec.contactCarrier)+'</td>' +
   	        	                          '<td align=left>'+ (rec.contactTel==null?"":rec.contactTel)+ '</td>' +
   	        	                          '<td align=left>'+ (rec.contactMobile==null?"":rec.contactMobile)+ '</td>' +
   	        	                          '<td align=left>'+( rec.contactEmail==null?"":rec.contactEmail)+  '</td>' +
   	        	                          '<td align=left>'+( rec.contactPostalcode==null?"":rec.contactPostalcode)+'</td>' +
   	        	                          '<td align=left>'+( rec.contactAddress==null?"":rec.contactAddress)+ '</td></tr>')
			
				html.push('</table>');
				html = html.join(''); // 最后生成的HTML表格
				tableToExcel(html);
			}
		})
			  }else
						{
						      Ext.Msg.alert('提示', '无数据进行导出！');
			                  return;
						}
				},
				failure : function(response,options){
					
							Ext.Msg.alert('提示',"导出失败！");
					}
			})
		  }
		}
    // add by sychen 20100713  end
				]
			})
			
		 // add by sychen 20100713
  function tableToExcel(tableHTML) {
		window.clipboardData.setData("Text", tableHTML);
		try {
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExWSh.Columns("A").ColumnWidth  = 15;
			ExWSh.Columns("B").ColumnWidth  = 15;
			ExWSh.Columns("C").ColumnWidth  = 15;
			ExWSh.Columns("D").ColumnWidth  = 15;
			ExWSh.Columns("E").ColumnWidth  = 25;
			ExWSh.Columns("F").ColumnWidth  = 15;
			ExWSh.Columns("G").ColumnWidth  = 25;
			ExApp.DisplayAlerts = false;
			ExApp.visible = true;
		} catch (e) {
			if (e.number != -2146827859)
				alert("您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}		
			
	var headForm = new Ext.form.FormPanel({
				region : 'north',
				id : 'center-panel',
				height : 28,
				frame : false,
				fileUpload : true,
				layout : 'form',
				items : [headTbar]
			});

	// 表ID
	var contactId = new Ext.form.Hidden({
				id : 'contactId',
				name : 'conform.contactId'
			})

	// 员工id
	var tfEmpId = new Ext.form.Hidden({
				id : 'empId',
				name : 'conform.empId'
			})

	// 员工姓名
	var tfChsName = new Ext.form.TextField({
				id : 'chsName',
				fieldLabel : "姓名",
				readOnly : true,
				anchor : '90%'
			});
	// 员工工号
	var tfEmpCode = new Ext.form.TextField({
				id : 'newEmpCode',
				name : 'conform.newEmpCode',
				fieldLabel : "人员编码",
				readOnly : true,
				anchor : '90%'
			});

	// 载波电话
	var carrierTel = new Ext.form.TextField({
				id : 'contactCarrier',
				name : 'conform.contactCarrier',
				fieldLabel : "载波电话",
				anchor : '90%'
			})
	// 外线电话
	var outTel = new Ext.form.TextField({
				id : 'contactTel',
				name : 'conform.contactTel',
				fieldLabel : "外线电话",
				anchor : '90%'
			})
	// 手机号码
	var mobileTel = new Ext.form.TextField({
				id : 'contactMobile',
				name : 'conform.contactMobile',
				fieldLabel : "手机号码",
				anchor : '90%'
			})
	// 邮箱地址
	var eMail = new Ext.form.TextField({
				id : 'contactEmail',
				name : 'conform.contactEmail',
				fieldLabel : "邮箱地址",
				anchor : '90%'
			})
	// 邮政编码
	var postalcode = new Ext.form.TextField({
				id : 'contactPostalcode',
				name : 'conform.contactPostalcode',
				fieldLabel : "邮政编码",
				anchor : '90%'
			})
	// 联系地址
	var address = new Ext.form.TextField({
				id : 'contactAddress',
				name : 'conform.contactAddress',
				fieldLabel : "联系地址",
				anchor : '63%'
			})
	var contactForm = new Ext.form.FormPanel({
				id : 'contactForm',
				layout : 'column',
				frame : true,
				border : false,
				fileUpload : true,
				labelAlign : 'left',
				labelWidth : 80,
				defaults : {
					layout : 'form'
				},
				items : [{
					columnWidth : .33,
					items : [contactId, tfEmpId, tfChsName, carrierTel,
							mobileTel, postalcode]
				}, {
					columnWidth : .33,
					items : [tfEmpCode, outTel, eMail]
				}, {
					columnWidth : 1,
					items : [address]
				}]
			})
	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				enableTabScroll : true,
				items : [{
							region : 'north',
							layout : 'fit',
							border : false,
							margins : '0 0 0 0',
							height : 25,
							items : [headForm]
						}, {
							region : 'center',
							layout : 'fit',
							border : false,
							margins : '0 0 0 0',
							split : true,
							collapsible : false,
							items : [contactForm]
						}]
			});

	// ========== 加载员工基本信息 ===========
	function loadEmp() {
		if (empIdUsing == null || empIdUsing == '') {
			formReset()
			return;
		}
		// 加载数据
		Ext.Ajax.request({
					method : Constants.POST,
					url : 'hr/getContactForm.action',
					params : {
						empId : empIdUsing
					},
					success : function(result, request) {
						var record = eval('(' + result.responseText + ')');
						// 数据库异常
						if (record && typeof record.msg != 'undefined'
								&& record.msg == "SQL") {
							Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
							return;
						}

						if (!record) {
							// 清除所有项目
							formReset();
							return;
						}
						contactForm.getForm().setValues(record);
						tfChsName.setValue(empChsName)
					},
					failure : function() {
						Ext.Msg.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
					}
				});
	}

	// 设置画面控件的值
	function formReset() {
		contactForm.getForm().reset();
		tfEmpId.setValue(empIdUsing)
		tfChsName.setValue(empChsName)
		tfEmpCode.setValue(empCodeUsing)
	}
	// 设置Form是否可以编辑
	function setEnable(argFlag) {
		contactForm.getForm().items.each(function(f) {
					var xtype = f.getXType();
					if (f.el.dom
							&& (xtype == 'textfield' || xtype == 'CodeField'
									|| xtype == 'numfield'
									|| xtype == 'textarea' || xtype == 'radio'
									|| xtype == 'NaturalNumberField'
									|| xtype == 'CmbHRBussiness'
									|| xtype == 'CmbHRCode' || xtype == 'combo')) {
						f.setDisabled(!argFlag);
					}
				});
		btnSave.setDisabled(!argFlag);
		btnModify.setDisabled(argFlag);
	}

	// 加载员工基本信息
	if (parent.empParam) {
		empIdUsing = parent.empParam.data.empId;
		empChsName = parent.empParam.data.chsName;
		empCodeUsing = parent.empParam.data.newEmpCode;
		loadEmp();
		setEnable(false);
		headForm.setDisabled(false);
	} else {
		headForm.setDisabled(true);
	}

});
