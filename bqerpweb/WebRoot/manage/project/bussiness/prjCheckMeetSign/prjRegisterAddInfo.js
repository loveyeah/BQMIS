Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var arg = window.dialogArguments;
	var checkSignId = arg.checkSignId;
	
	
	function  getFormInfo()
	{
		Ext.Ajax.request({
				method:'POST',
				url : 'manageproject/getPrjCheckMeetSignById.action',
				params:{
					checkSignId:checkSignId
				},
				success:function(result, request){
				var rec= result.responseText
					var res = eval("(" + result.responseText + ")");
					
				 if(res.list!=null)
				 {
				 	
				 	equCheckText.setValue(res.list[0][0]);
					safetyCheckText.setValue(res.list[0][1]);
					manageCheckText.setValue(res.list[0][2]);
					financeCheckText.setValue(res.list[0][3]);
					auditCheckText.setValue(res.list[0][4]);
					
				
					equCheckBy.setValue(res.list[0][5]);
					safetyCheckBy.setValue(res.list[0][7]);
					manageCheckBy.setValue(res.list[0][9]);
					financeCheckBy.setValue(res.list[0][11]);
					auditCheckBy.setValue(res.list[0][13]);
					
//					equCheckName.setValue(res.list[0][6]);
//					safetyCheckName.setValue(res.list[0][8]);
//					manageCheckName.setValue(res.list[0][10]);
//					financeCheckName.setValue(res.list[0][12]);
//					auditCheckName.setValue(res.list[0][14]);
					
					if(equCheckBy.getValue()!=''&&equCheckBy.getValue()!=null){
						equCheckName.setValue(res.list[0][6]);
					}
					if(safetyCheckBy.getValue()!=null&&safetyCheckBy.getValue()!=''){
						safetyCheckName.setValue(res.list[0][8]);
					}
					if(manageCheckBy.getValue()!=null&&manageCheckBy.getValue()!=''){
						manageCheckName.setValue(res.list[0][10]);
					}
					if(financeCheckBy.getValue()!=null&&financeCheckBy.getValue()!=''){
						financeCheckName.setValue(res.list[0][12]);
					}
					if(auditCheckBy.getValue()!=null&&auditCheckBy.getValue()!=''){
						auditCheckName.setValue(res.list[0][14]);
					}
					
					
				
				
					
					equDate.setValue(res.list[0][15]);
					safetyDate.setValue(res.list[0][16]);
					manageDate.setValue(res.list[0][17]);
					financeDate.setValue(res.list[0][18]);
					auditDate.setValue(res.list[0][19]);
						
					}else{
						
						
					}
				},
				failure:function(resp){
					Ext.Msg.alert('警告','出现未知错误！');
				}
			});
	}
	
	function  getDeptInfo()
	{
		equ_ds.on("beforeload", function() {
				Ext.apply(this.baseParams, {
					checkSignId : checkSignId
							
						});

			});
			
				equ_ds.load();
		
	}
	
	// 定义grid
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t

		return s;
	}
	
	var SignId = new Ext.form.Hidden({
				id : "checkSignId",
				value:checkSignId,
				name : "prjMeetSign.checkSignId"
			})
			
	var equCheckText = new Ext.form.TextField({
		xtype : "textfield",
		fieldLabel : '设备部验收意见',
		anchor : '95%',
		name : 'prjMeetSign.equCheckText',
		allowBlank : false
	});
	//----------设备部------------
	var equCheckBy = new Ext.form.Hidden({
		name : 'prjMeetSign.equCheckBy'
	})
	var equCheckName = new Ext.form.TextField({
		id : 'equCheckBy',
		fieldLabel : '会签人',
		valueField : 'equCheckBy',
		hiddenName : 'prjMeetSign.equCheckBy',
		width : 110,
		readOnly : true,
		listeners : {
			focus : function() {
				var args = {
					selectModel : 'multiple',
					rootNode : {
						id : '0',
						text : '灞桥电厂'
					},
					onlyLeaf : false
				};
				this.blur();
				var person = window
						.showModalDialog(
								'../../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
								args,
								'dialogWidth:'
										+ Constants.WIDTH_COM_EMPLOYEE
										+ 'px;dialogHeight:'
										+ Constants.HEIGHT_COM_EMPLOYEE
										+ 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
				if (typeof(person) != "undefined") {
					equCheckName.setValue(person.names);
					equCheckBy.setValue(person.codes);

				}
			}
		}
	});
	var equDate = new Ext.form.TextField({
		width : 110,
		name : 'prjMeetSign.equCheckDate',
		fieldLabel : '时间',
		style : 'cursor:pointer',
		readOnly : true,
		value : '',
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : false,
					isShowClear : true
				});
			}
		}
	});
	//-----------------安全监察部---------------------------
	var safetyCheckText = new Ext.form.TextField({
		xtype : "textfield",
		fieldLabel : '安全监察部验收意见',
		anchor : '95%',
		width : 100,
		name : 'prjMeetSign.safetyCheckText',
		allowBlank : false
	});
	var safetyCheckBy = new Ext.form.Hidden({
		name : 'prjMeetSign.safetyCheckBy'
	})
	var safetyCheckName = new Ext.form.TextField({
		id : 'safetyCheckBy',
		fieldLabel : '会签人',
		valueField : 'safetyCheckBy',
		hiddenName : 'prjMeetSign.safetyCheckBy',
		width : 110,
		readOnly : true,
		listeners : {
			focus : function() {
				var args = {
					selectModel : 'multiple',
					rootNode : {
						id : '0',
						text : '合肥电厂'
					},
					onlyLeaf : false
				};
				this.blur();
				var person = window
						.showModalDialog(
								'../../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
								args,
								'dialogWidth:'
										+ Constants.WIDTH_COM_EMPLOYEE
										+ 'px;dialogHeight:'
										+ Constants.HEIGHT_COM_EMPLOYEE
										+ 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
				if (typeof(person) != "undefined") {
					safetyCheckName.setValue(person.names);
					safetyCheckBy.setValue(person.codes);

				}
			}
		}
	});
	var safetyDate = new Ext.form.TextField({
		width : 110,
		name : 'prjMeetSign.safetyCheckDate',
		fieldLabel : '时间',
		style : 'cursor:pointer',
		readOnly : true,
		value : '',
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : false,
					isShowClear : true
				});
			}
		}
	});
	//--------------------------------------------------------------------
	//-----------经营管理部---------------------------
	var manageCheckText = new Ext.form.TextField({
		xtype : "textfield",
		fieldLabel : '经营管理部验收意见',
		anchor : '95%',
		width : 100,
		name : 'prjMeetSign.manageCheckText',
		allowBlank : false
	});
	var manageCheckBy = new Ext.form.Hidden({
		name : 'prjMeetSign.manageCheckBy'
	})
	var manageCheckName = new Ext.form.TextField({
		id : 'manageCheckBy',
		fieldLabel : '会签人',
		valueField : 'manageCheckBy',
		hiddenName : 'prjMeetSign.manageCheckBy',
		width : 110,
		readOnly : true,
		listeners : {
			focus : function() {
				var args = {
					selectModel : 'multiple',
					rootNode : {
						id : '0',
						text : '合肥电厂'
					},
					onlyLeaf : false
				};
				this.blur();
				var person = window
						.showModalDialog(
								'../../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
								args,
								'dialogWidth:'
										+ Constants.WIDTH_COM_EMPLOYEE
										+ 'px;dialogHeight:'
										+ Constants.HEIGHT_COM_EMPLOYEE
										+ 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
				if (typeof(person) != "undefined") {
					manageCheckName.setValue(person.names);
					manageCheckBy.setValue(person.codes);

				}
			}
		}
	});
	var manageDate = new Ext.form.TextField({
		width : 110,
		fieldLabel : '时间',
		name : 'prjMeetSign.manageCheckDate',
		style : 'cursor:pointer',
		readOnly : true,
		value : '',
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : false,
					isShowClear : true
				});
			}
		}
	});
	//--------------------------------------------
	//------------财务部---------------------------

	var financeCheckText = new Ext.form.TextField({
		xtype : "textfield",
		fieldLabel : '财务部验收意见',
		anchor : '95%',
		width : 100,
		name : 'prjMeetSign.financeCheckText',
		allowBlank : false
	});
	var financeCheckBy = new Ext.form.Hidden({
		name : 'prjMeetSign.financeCheckBy'
	})
	var financeCheckName = new Ext.form.TextField({
		id : 'financeCheckBy',
		fieldLabel : '会签人',
		valueField : 'financeCheckBy',
		hiddenName : 'prjMeetSign.financeCheckBy',
		width : 110,
		readOnly : true,
		listeners : {
			focus : function() {
				var args = {
					selectModel : 'multiple',
					rootNode : {
						id : '0',
						text : '合肥电厂'
					},
					onlyLeaf : false
				};
				this.blur();
				var person = window
						.showModalDialog(
								'../../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
								args,
								'dialogWidth:'
										+ Constants.WIDTH_COM_EMPLOYEE
										+ 'px;dialogHeight:'
										+ Constants.HEIGHT_COM_EMPLOYEE
										+ 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
				if (typeof(person) != "undefined") {
					financeCheckName.setValue(person.names);
					financeCheckBy.setValue(person.codes);

				}
			}
		}
	});
	var financeDate = new Ext.form.TextField({
		width : 110,
		fieldLabel : '时间',
		name : 'prjMeetSign.financeCheckDate',
		style : 'cursor:pointer',
		readOnly : true,
		value : '',
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : false,
					isShowClear : true
				});
			}
		}
	});
	//-------------------------------------
	//---------------审计验收意见-
	var auditCheckText = new Ext.form.TextField({
		xtype : "textfield",
		fieldLabel : '审计验收意见',
//		width : 100,
		anchor : '95%',
		name : 'prjMeetSign.auditCheckText',
		allowBlank : false
	});
	var auditCheckBy = new Ext.form.Hidden({
		name : 'prjMeetSign.auditCheckBy'
	})
	var auditCheckName = new Ext.form.TextField({
		id : 'auditCheckBy',
		fieldLabel : '会签人',
		valueField : 'auditCheckBy',
		hiddenName : 'prjMeetSign.auditCheckBy',
		width : 110,
		readOnly : true,
		listeners : {
			focus : function() {
				var args = {
					selectModel : 'multiple',
					rootNode : {
						id : '0',
						text : '合肥电厂'
					},
					onlyLeaf : false
				};
				this.blur();
				var person = window
						.showModalDialog(
								'../../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
								args,
								'dialogWidth:'
										+ Constants.WIDTH_COM_EMPLOYEE
										+ 'px;dialogHeight:'
										+ Constants.HEIGHT_COM_EMPLOYEE
										+ 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
				if (typeof(person) != "undefined") {
					auditCheckName.setValue(person.names);
					auditCheckBy.setValue( person.codes);

				}
			}
		}
	});
	var auditDate = new Ext.form.TextField({
		width : 110,
		name : 'prjMeetSign.auditCheckDate',
		fieldLabel : '时间',
		style : 'cursor:pointer',
		readOnly : true,
		value : '',
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : false,
					isShowClear : true
				});
			}
		}
	});
	//-----------------------------------------

	var formtbar = new Ext.Toolbar({
		id : 'tbar',
		items : [{
			id : 'save',
			text : "保存",
			iconCls : 'save',
			handler : saveData
		}]
	});

	var form = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'left',
		region : 'north',
		layout : 'column',
		height : 250,
		border : false,
		tbar : formtbar,
		items : [{
			border : false,
			layout : 'form',
			columnWidth : .4,
			labelWidth : 120,
			items : [SignId,equCheckText,safetyCheckText,manageCheckText,financeCheckText,auditCheckText]
		}, {
			border : false,
			layout : 'form',
			columnWidth : .3,
			labelWidth : 50,
			items : [equCheckBy,safetyCheckBy, manageCheckBy,financeCheckBy,auditCheckBy,
		 equCheckName	,safetyCheckName,manageCheckName,financeCheckName,auditCheckName]
		}, {
			border : false,
			layout : 'form',
			columnWidth : .3,
			labelWidth : 50,
			items : [equDate, safetyDate,manageDate,financeDate,auditDate]
		}/*, {
			border : false,
			layout : 'form',
			columnWidth : .3,
			labelWidth : 110,
			items : [
					]
		}, {
			border : false,
			layout : 'form',
			columnWidth : .3,
			labelWidth : 110,
			items : [ ]
		}*/]
	});

	function saveData() {
		var url = "";

		url = 'manageproject/updatePrjCheckMeetSign.action';

		if (!form.getForm().isValid()) {
			return false;
		}
		
		form.getForm().submit({
			url : url,
			method : 'post',
			params : {
				flag:'Y'
				
				
			},
			success : function(form, action) {
				var message = eval('(' + action.response.responseText + ')');
				Ext.Msg.alert("成功", "更新成功！");
			
			
			},
			failure : function(form, action) {
				Ext.Msg.alert('错误', '出现未知错误.');
			}
		})

	}
	//---------------------------------------------------

	function saveEquDept() {
		var alertMsg = "";
		grid.stopEditing();
		var modifyRec = grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0) {
			Ext.Msg.confirm('提示', '确定要保存修改数据吗?', function(button) {
				if (button == 'yes') {
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {

						if (alertMsg != "") {
							Ext.Msg.alert("提示", alertMsg);
							return;
						}
						updateData.push(modifyRec[i].data);
					}
					Ext.Ajax.request({
						url : 'manageproject/saveEquDept.action',
						method : 'post',
						params : {
							isCheck:'Y',
							checkSignId : checkSignId,
							isUpdate : Ext.util.JSON.encode(updateData)

						},
						success : function(form, options) {
							var obj = Ext.util.JSON.decode(form.responseText)
							Ext.MessageBox.alert('提示信息', '保存成功！')
							equ_ds.rejectChanges();
							equ_ds.reload();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('提示信息', '操作失败！')
						}
					})
				}
			})
		} else {
			Ext.MessageBox.alert('提示信息', '未做任何修改!')
			equ_ds.rejectChanges();
			equ_ds.reload();
		}
	}

	var gridTbar = new Ext.Toolbar({
		items : [{
			id : 'gridsave',
			text : "保存",
			iconCls : 'save',
			handler : saveEquDept
		}]
	});

	var equ_record = Ext.data.Record.create([{
		name : 'id',
		mapping : 0
	}, {
		name : 'checkSignId',
		mapping : 1
	}, {
		name : 'deptId',
		mapping : 2
	}, {
		name : 'deptName',
		mapping : 3
	},{
		name : 'checkText',
		mapping : 4
	},{
		name : 'signBy',
		mapping : 5
	},{
		name : 'signByName',
		mapping : 6
	},{
		name : 'signDate',
		mapping : 7
	}]);
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	});

	var equ_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'manageproject/getEquDept.action'
		}),
		reader : new Ext.data.JsonReader({
			root : "list",
			totalProperty : "total"
		}, equ_record)

	});

	var grid = new Ext.grid.EditorGridPanel({
		store : equ_ds,
		layout : 'fit',
		region : 'center',
		columns : [sm, new Ext.grid.RowNumberer({
			header : '序号',
			width : 35
		}), {
			header : 'deptId',
			hidden : true,
			dataIndex : 'deptId'

		},		// 选择框
				{
					header : "部门",
					width : 200,
					align : 'center',
					dataIndex : 'deptName'

				}, {
					header : '验收意见',
					dataIndex : 'checkText',
					align : 'center',
					width : 150,
					editor : new Ext.form.TextField({
						allowBlank : false,
						id : 'content'
					})
				}, {
					header : '验收人',
					dataIndex : 'signByName',
					align : 'center',
					width : 100,
					editor : new Ext.form.TextField({
						id : 'signBy',
						valueField : 'signByName',
						width : 125,
						readOnly : true,
						listeners : {
							focus : function() {
								var args = {
									selectModel : 'single',
									rootNode : {
										id : '0',
										text : '合肥电厂'
									},
									onlyLeaf : false
								};
								this.blur();
								var person = window
										.showModalDialog(
												'../../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
												args,
												'dialogWidth:'
														+ Constants.WIDTH_COM_EMPLOYEE
														+ 'px;dialogHeight:'
														+ Constants.HEIGHT_COM_EMPLOYEE
														+ 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
								if (typeof(person) != "undefined") {
									var record = grid.getSelectionModel()
										.getSelected();
							    record.set("signByName", person.workerName);
								record.set("signBy", person.workerCode);


								}
							}
						}
					})

				},{
					header : '时间',
					dataIndex : 'signDate',
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
													startDate : '%y-%M-%d',
													dateFmt : 'yyyy-MM-dd',
													alwaysUseStartDate : false,
													onpicked : function ()
													{
															grid.getSelectionModel().getSelected().set("signDate",this.value);
													}
													
												});

									}
								}
							})

				}],

		sm : sm, // 选择框的选择
		tbar : gridTbar,
		clicksToEdit : 1,
		viewConfig : {
			forceFit : false
		}
	});

	function setButn() {
		if (checkSignId == "") {
			gridTbar.setDisabled(true);
		} else {
			gridTbar.setDisabled(false);
		}
	}
	//初始化

	 getFormInfo();
	 getDeptInfo();
	 
	var layout = new Ext.Viewport({
				layout : 'border',
				autoHeight : true,
				items : [form, grid]
			});
	
	

});