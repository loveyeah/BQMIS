Ext.onReady(function() {

	function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";
		// 初始化时间
		Year = DateIn.getYear();
		Month = DateIn.getMonth() + 1;
		Day = DateIn.getDate();
		CurrentDate = Year + "-";
		if (Month >= 10) {
			CurrentDate = CurrentDate + Month;
		} else {
			CurrentDate = CurrentDate + "0" + Month;
		}
		return CurrentDate;
	}
	var enddate = new Date();
	var startdate = enddate.add(Date.MONTH, 6);
	startdate = startdate.getFirstDateOfMonth();

	var retirementDate = new Ext.form.TextField({
		id : 'retirementDate',
		fieldLabel : '退休日期',
		style : 'cursor:pointer',
		value : ChangeDateToString(startdate),
		readOnly : true,
		width : 85,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M',
					dateFmt : 'yyyy-MM',
					alwaysUseStartDate : true,
					isShowClear : false
				});
				this.blur();
			}
		}
	});

	// -------------------- 定义grid--------------------------
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
		name : 'sex',
		mapping : 3
	}, {
		name : 'deptName',
		mapping : 4
	}, {
		name : 'brithday',
		mapping : 5
	}, {
		name : 'age',
		mapping : 6
	}, {
		name : 'missionDate',
		mapping : 7
	}, {
		name : 'stationName',
		mapping : 8
	}, {
		name : 'retirementDate',
		mapping : 9
	}, {
		name : 'countDown',
		mapping : 10
	}, {
		name : 'workAge',
		mapping : 11
	}, {
		name : 'isSpecialTrades',
		mapping : 12
	}, {
		name : 'isCadres',
		mapping : 13
	}, {
		name : 'standard',
		mapping : 14
	}, {
		name : 'workDate',
		mapping : 15
	},{
		name : 'politicsId',
		mapping : 16
	},{
		name : 'politicsName',
		mapping : 17
	},{
		name : 'stationId',
		mapping : 18
	},{
		name : 'deptId',
		mapping : 19
	},{
		name : 'isRetired',
		mapping : 20
	}]);

	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'com/finRetirementDateQueryList.action ',
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list'
		}, MyRecord)
	});
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
	var sm = new Ext.grid.CheckboxSelectionModel({
	   singleSelect : true
	});
	var grid = new Ext.grid.GridPanel({
		region : "center",
		ds : store,
		columns : [
		sm, new Ext.grid.RowNumberer({
			header : '行号',
			width : 35
		}), {
			header : "人员ID",
			hidden : true,
			width : 75,
			sortable : true,
			dataIndex : 'empId'
		}, {
			header : "员工工号",
			width : 80,
			sortable : true,
			dataIndex : 'empCode',
			renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						// ---add by wpzhu 20100730
//						if (rowIndex < store.getCount() - 1) {
							var standard = record.get('standard');
							var age = record.get('age');
							var a = standard-age;
							if (a>=0&&a<=2) {
								if(v!=null)
								return "<font color='green'>" + v + "</font>";
								else
								return "";
							} else 
							{
								if(v!=null)
								return v;
								else
								return"";
							}
							
//						}		
						   
					}
					//----------------------
		}, {
			header : "中文名",
			width : 80,
			sortable : true,
			dataIndex : 'chsName',
			renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						// ---add by wpzhu 20100730
//						if (rowIndex < store.getCount() - 1) {
							var standard = record.get('standard');
							var age = record.get('age');
							var a = standard-age;
							if (a>=0&&a<=2) {
								if(v!=null)
								return "<font color='green'>" + v + "</font>";
								else
								return "";
							} else 
							{
								if(v!=null)
								return v;
								else
								return"";
							}
							
						}		
						   
//					}
					//----------------------
		}, {
			header : "性别",
			width : 45,
			sortable : true,
			dataIndex : 'sex',
			renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						// ---add by wpzhu 20100730
//						if (rowIndex < store.getCount() - 1) {
							var standard = record.get('standard');
							var age = record.get('age');
							var a = standard-age;
							if (a>=0&&a<=2) {
								if(v!=null)
								return "<font color='green'>" + v + "</font>";
								else
								return "";
							} else 
							{
								if(v!=null)
								return v;
								else
								return"";
							}
							
						}		
						   
//					}
					//----------------------
		}, {
			header : "部门",
			width : 150,
			sortable : true,
			dataIndex : 'deptName',
			renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						// ---add by wpzhu 20100730
//						if (rowIndex < store.getCount() - 1) {
							var standard = record.get('standard');
							var age = record.get('age');
							var a = standard-age;
							if (a>=0&&a<=2) {
								if(v!=null)
								return "<font color='green'>" + v + "</font>";
								else
								return "";
							} else 
							{
								if(v!=null)
								return v;
								else
								return"";
							}
							
						}		
						   
//					}
					//----------------------
		},{
			header : "岗位",
			width : 100,
			sortable : true,
			dataIndex : 'stationName',
			renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						// ---add by wpzhu 20100730
//						if (rowIndex < store.getCount() - 1) {
							var standard = record.get('standard');
							var age = record.get('age');
							var a = standard-age;
							if (a>=0&&a<=2) {
								if(v!=null)
								return "<font color='green'>" + v + "</font>";
								else
								return "";
							} else 
							{
								if(v!=null)
								return v;
								else
								return"";
							}
							
						}		
						   
//					}
					//----------------------
		},
//		{
//			header : "生日",
//			width : 75,
//			sortable : true,
//			dataIndex : 'brithday'
//		}, // update by sychen 20100629
			{
			header : "年龄",
			width : 45,
			sortable : true,
			dataIndex : 'age',
			renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						// ---add by wpzhu 20100730
//						if (rowIndex < store.getCount() - 1) {
							var standard = record.get('standard');
							var age = record.get('age');
							var a = standard-age;
							if (a>=0&&a<=2) {
								if(v!=null)
								return "<font color='green'>" + v + "</font>";
								else
								return "";
							} else 
							{
								if(v!=null)
								return v;
								else
								return"";
							}
							
						}		
						   
//					}
					//----------------------
		}, {
			header : "工龄",
			width : 45,
			sortable : true,
			dataIndex : 'workAge',
			renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						// ---add by wpzhu 20100730
//						if (rowIndex < store.getCount() - 1) {
							var standard = record.get('standard');
							var age = record.get('age');
							var a = standard-age;
							if (a>=0&&a<=2) {
								if(v!=null)
								return "<font color='green'>" + v + "</font>";
								else
								return "";
							} else 
							{
								if(v!=null)
								return v;
								else
								return"";
							}
							
						}		
						   
//					}
					//----------------------
		}, {
			header : "是否特殊工种",
			width : 90,
			sortable : true,
			dataIndex : 'isSpecialTrades',
			renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						// ---add by wpzhu 20100730
//						if (rowIndex < store.getCount() - 1) {
							var standard = record.get('standard');
							var age = record.get('age');
							var a = standard-age;
							if (a>=0&&a<=2) {
								if(v!=null)
								return "<font color='green'>" + v + "</font>";
								else
								return "";
							} else 
							{
								if(v!=null)
								return v;
								else
								return"";
							}
							
						}		
						   
//					}
					//----------------------
		}, {
			header : "是否干部",
			width : 80,
			sortable : true,
			dataIndex : 'isCadres',
			renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						// ---add by wpzhu 20100730
//						if (rowIndex < store.getCount() - 1) {
							var standard = record.get('standard');
							var age = record.get('age');
							var a = standard-age;
							if (a>=0&&a<=2) {
								if(v!=null)
								return "<font color='green'>" + v + "</font>";
								else
								return "";
							} else 
							{
								if(v!=null)
								return v;
								else
								return"";
							}
							
						}		
						   
//					}
					//----------------------
		}, {
			header : "退休标准",
			width : 100,
			sortable : true,
			dataIndex : 'standard',
			renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						// ---add by wpzhu 20100730
//						if (rowIndex < store.getCount() - 1) {
							var standard = record.get('standard');
							var age = record.get('age');
							var a = standard-age;
							if (a>=0&&a<=2) {
								if(v!=null)
								return "<font color='green'>" + v + "</font>";
								else
								return "";
							} else 
							{
								if(v!=null)
								return v;
								else
								return"";
							}
							
						}		
						   
//					}
					//----------------------
		},{
			header : "出生日期",
			width : 90,
			sortable : true,
			dataIndex : 'brithday',
			renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						// ---add by wpzhu 20100730
//						if (rowIndex < store.getCount() - 1) {
							var standard = record.get('standard');
							var age = record.get('age');
							var a = standard-age;
							if (a>=0&&a<=2) {
								if(v!=null)
								return "<font color='green'>" + v + "</font>";
								else
								return "";
							} else 
							{
								if(v!=null)
								return v;
								else
								return"";
							}
							
						}		
						   
//					}
					//----------------------
		}, {
			header : "参加工作时间",
			width : 90,
			sortable : true,
			dataIndex : 'workDate',
			renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						// ---add by wpzhu 20100730
//						if (rowIndex < store.getCount() - 1) {
							var standard = record.get('standard');
							var age = record.get('age');
							var a = standard-age;
							if (a>=0&&a<=2) {
								if(v!=null)
								return "<font color='green'>" + v + "</font>";
								else
								return "";
							} else 
							{
								if(v!=null)
								return v;
								else
								return"";
							}
							
//						}		
						   
					}
					//----------------------
		}
		// add by sychen 20100629 end
//		,{
//			header : "入职时间",
//			width : 75,
//			sortable : true,
//			dataIndex : 'missionDate'
//		},  {
//			header : "退休日期",
//			width : 75,
//			sortable : true,
//			dataIndex : 'retirementDate'
//		}, {
//			header : "倒计时（天）",
//			width : 75,
//			sortable : true,
//			dataIndex : 'countDown'
//		}
		],
		sm : sm,
		stripeRows : true,
		autoSizeColumns : true,
		//update by sychen 20100629
		viewConfig : {
			forceFit : false,
			getRowClass : function(record, rowIndex, rowParams, store) {
				if(record.data.countDown <=0 )
				 return 'row_red';
				else
				 return 'row_black'
		}},
//		viewConfig : {
//			forceFit : true
//		},
		tbar : [
//		"退休日期:", retirementDate, "-",
		{
			text : "查询",
			iconCls : 'query',
			handler : queryRecord
		}, "-", {
		id : 'btnPrint',
		text : '导 出',
		iconCls : 'export',
		handler : function() {
			reportexport();
		}
	}, "-", {
		id : 'update',
		text : '修改',
		iconCls : 'update',
		handler : function() {
			updateRecord();
		}
	}
//		{
//			text : "履历表",
//			iconCls : Constants.CLS_PRINT,
//			handler : showPrintPreview
//		}
		],
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录",
			beforePageText : '页',
			afterPageText : "共{0}页"
		})
	});
	//员工工号----------------------------------
	var tfEmpCode = new Ext.form.CodeField({
				id : 'empCode',
				// name : 'emp.empCode',
				name : 'empinfo.empCode',
				fieldLabel : "员工工号",
				readOnly : true,
//				width : 150,
				anchor : '100%',
				maxLength : 20
			});
			
	// 员工姓名
	var tfChsName = new Ext.form.TextField({
				id : 'chsName',
				name : 'empinfo.chsName',
//				width : 150,
				anchor : '100%',
				readOnly:true,
				fieldLabel : "员工姓名"
			});
// 出生日期
	var tfBrithday = new Ext.form.TextField({
				id : 'brithday',
				name : 'empinfo.brithday',
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
// 参加工作日期
	var tfWorkDate = new Ext.form.TextField({
				id : 'workDate',
				name : 'empinfo.workDate',
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
  var workAge = new Ext.form.TextField({
				id : 'workAge',
//				width : 150,
				anchor : '100%',
				fieldLabel : "工龄",
				style : 'text-align: right;',
				readOnly : true
			});
// 性别
	var cbSex = new Ext.form.CmbHRCode({
				id : 'sex',
				hiddenName : 'empinfo.sex',
				fieldLabel : '性别',
//				width : 150,
				anchor : '100%',
				selectOnFocus : true,
				type : '性别'
			});
var deptTxt = new Ext.form.TextField({
				id : 'deptName',
				fieldLabel : '所属部门',
				style : 'cursor:pointer',
//				width : 150,
				anchor : '100%',
				allowBlank : false,
				readOnly : true
			});
	// 所属部门ID
	var hiddenDeptTxt = new Ext.form.Hidden({
				id : 'deptId',
				name : 'empinfo.deptId'
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
				hiddenName : 'empinfo.stationId',
				fieldLabel : '工作岗位',
//				width : 150,
				anchor : '100%',
				triggerAction : 'all',
				store : stationStore,
				displayField : 'stationName',
				valueField : 'stationId',
				mode : 'local'/*,
				readOnly : true*/
			});
// 政治面貌
	var cbPoliticsId = new Ext.form.CmbHRBussiness({
				id : "politicsId",
				hiddenName : 'empinfo.politicsId',
				fieldLabel : '政治面貌',
//				width : 150,
				anchor : '100%',
				selectOnFocus : true,
				type : '政治面貌'
			});
// 进厂日期
	var tfMissionDate = new Ext.form.TextField({
				id : 'missionDate',
				name : 'empinfo.missionDate',
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
				hiddenName : 'empinfo.isSpecailTrades',
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
				hiddenName : 'empinfo.isCardes',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false
			});
			
	//------------------------------------------
			var isRetired = new Ext.form.ComboBox({
				fieldLabel : '是否退休',
//				width : 150,
				anchor : '100%',
				store : new Ext.data.SimpleStore({
							fields : ['value', 'text'],
							data : [['1', '是'], ['0', '否']]
						}),
				id : 'isRetired',
				name : 'isRetired',
				valueField : "value",
				displayField : "text",
				value : 'Y',
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'empinfo.isRetired',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false
			});
  //-----------------------------------------------------------
			
		var empId = new Ext.form.Hidden({
				id : 'empId',
				name : 'empinfo.empId'
			});
	//------------------------------------
	var myaddpanel = new Ext.FormPanel({
		title : '员工信息修改对话框',
		height : '100%',
		layout : 'form',
		frame : true,
		labelAlign : 'center',
		items : [{
			border : false,
			layout : 'column',
			items : [{
				columnWidth : .5,
				layout : 'form',
				border : false,
				labelWidth : 80,
				items : [empId,tfEmpCode, tfBrithday, tfWorkDate, cbSex]
			}, {
				columnWidth : .5,
				layout : 'form',
				labelWidth : 70,
				border : false,
				items : [tfChsName, tfAge, workAge]
			}]
		},{
			border : false,
			layout : 'column',
			items : [{
				columnWidth : .5,
				layout : 'form',
				border : false,
				labelWidth : 80,
				items : [hiddenDeptTxt, deptTxt,cbPoliticsId,isSpecialTrades,isRetired]
			}, {
				columnWidth : .5,
				layout : 'form',
				labelWidth : 70,
				border : false,
				items : [tfStationName,tfMissionDate,isCadres]
			}]
		}]
	});
	

	var win = new Ext.Window({
		width : 550,
		height : 350,
		buttonAlign : "center",
		items : [myaddpanel],
		layout : 'fit',
		closeAction : 'hide',
		draggable : true,
		modal : true,
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
				myaddpanel.getForm().submit({
					method : 'POST',
					url : "hr/saveEmpRec.action",
					params : {
						'empId' : empId.getValue()
					},
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("提示", o.msg);
						store.load({
							params : {
								start : 0,
								limit : 18
							}
						});
						win.hide();
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
				win.hide();
			}
		}]
	});
	var deptId="";
   function updateRecord() {
		if (grid.selModel.hasSelection()) {
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("提示", "请选择其中一项进行编辑！");
			} else {
				var record = grid.getSelectionModel().getSelected();
				win.show();
				myaddpanel.getForm().loadRecord(record);
				deptId=record.get('deptId');
				stationStore.load({
						params : {
							deptIdForm :deptId
						},
						callback : function() {
							stationStore.insert(0, new stationData({
												stationName : '',
												stationId : ''
											}));
						}
					})
				tfStationName.setValue(record.get('stationId'));
				tfStationName.setRawValue(record.get('stationName'));
			
			   /* var isRetired=record.get("isRetired")
			   
				if(isRetired==""||isRetired==null||isRetired=='0')
				{
					isRetired.setValue('0');
				}else if(isRetired=='1')
				{
					isRetired.setValue('0');
//				}
					*/
				
				if(record.get("isSpecialTrades")=="是")
				{
					
					isSpecialTrades.setValue("Y");
				}else
				{
					isSpecialTrades.setValue("N");
				}
				if(record.get("sex")=="男")
				{
					cbSex.setValue("M")
				}else
				{
					cbSex.setValue("W")
				}
			    if(record.get("isCadres")=="是")
			    {
			    	isCadres.setValue("Y");
			    }else
			    {
			    	isCadres.setValue("N");
			    }
			    
				myaddpanel.setTitle("员工信息修改");
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}

	grid.on("rowdblclick",updateRecord );
	
//	grid.on("rowdblclick", showPrintPreview);

	// ------------------------查询-----------------------------------------

	function queryRecord() {
		store.baseParams = {
			fuzzy : retirementDate.getValue()
		};
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}
// add by sychen 20100629
	function tableToExcel(tableHTML) {
		window.clipboardData.setData("Text", tableHTML);
		try {
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExApp.Columns(1).ColumnWidth = 15;
			ExApp.Columns(2).ColumnWidth = 15;
			ExApp.Columns(3).ColumnWidth = 15;
			ExApp.DisplayAlerts = true;
			ExWSh.Paste();
			ExApp.visible = true;
		} catch (e) {
			if (e.number != -2146827859)
				alert("您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}

	function reportexport() {
		var year=new Date();
		Ext.Ajax.request({
			url : 'com/finRetirementDateQueryList.action',
			success : function(response) {
				var json = eval('(' + response.responseText.trim() + ')');
				var records = json.list;
				var html = ['<table border=1><tr><td align="center" colspan="12"><b><h1>灞桥热电厂'+year.getYear()+'年退休预警</h1></b></td></tr>' +
						'<tr><th>员工工号</th><th>中文名</th><th>性别</th><th>部门</th><th>岗位</th><th>年龄</th>' +
						'<th>工龄</th><th>是否特殊工种</th><th>是否干部</th><th>退休标准</th><th>出生日期</th><th>参加工作时间</th></tr>'];
				for (var i = 0; i < records.length; i += 1) {
					var rc = records[i];
					html.push('<tr><td align ="left">' + (rc[1] ==null?"":rc[1])+ '</td><td align ="left">' + (rc[2]==null?"":rc[2] )
					       + '</td><td align ="left">' + (rc[3] ==null?"":rc[3]) + '</td><td align ="left">' + (rc[4]==null?"":rc[4] )
					       + '</td><td align ="left">' + (rc[8] ==null?"":rc[8] ) + '</td><td align ="left">' + (rc[6]==null?"":rc[6])
					       + '</td><td align ="left">' + (rc[11]==null?"":rc[11]) + '</td><td align ="left">'+ (rc[12] ==null?"":rc[12])
					       + '</td><td align ="left">' + (rc[13]==null?"":rc[13])+ '</td><td align ="left">' + (rc[14] ==null?"":rc[14])
					       + '</td><td align ="left">' + (rc[5] ==null?"":rc[5])+ '</td><td align ="left">' + (rc[15] ==null?"":rc[15])
						   + '</td></tr>');
				}
				html.push('</table>');
				html = html.join('');
				tableToExcel(html);
			},
			failure : function(response) {
				Ext.Msg.alert('提示', '导出失败！');
			}
		});
	}
	// add by sychen 20100629 end
	
	function showPrintPreview() {
		var selected = grid.getSelectionModel().getSelections();
		var empId;
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要浏览的记录！");
		} else {
			var menber = selected[0];
			empId = menber.get('empId');
			var url = "/power/report/webfile/hr/employeeRecord.jsp?empId="
					+ empId;
			window.open(url);
		}
	};

	new Ext.Viewport({
		layout : "border",
		items : [{
			region : 'center',
			height : '100%',
			border : false,
			layout : 'border',
			items : [grid]
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
	queryRecord();
});