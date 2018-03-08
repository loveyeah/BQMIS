Ext.ns("Personnel")
Personnel.EmpSpecialty = function(config) {

	var empIdUsing = null;

	// 新增按钮
	var btnAdd = new Ext.Button({
		text : Constants.BTN_ADD,
		iconCls : Constants.CLS_ADD,
		handler : onAdd
	});

	// 修改按钮
	var btnModify = new Ext.Button({
		text : Constants.BTN_UPDATE,
		iconCls : Constants.CLS_UPDATE,
		handler : onModify
	});

	// 删除按钮
	var btnDelete = new Ext.Button({
		text : Constants.BTN_DELETE,
		iconCls : Constants.CLS_DELETE,
		handler : onDelete
	});
	var tfAppend = new Ext.form.TextField({
		name : 'xlsFile',
		inputType : 'file',
		width : 200
	})
	
	//----------begin----------
	function downloadMoudle(){
		window.open("./downloadMoudle/人员特长-模板.xls")
	}
	
	var btnDownload = new Ext.Toolbar.Button({
		id : 'btnDownload',
		text : '模板下载',
		handler : downloadMoudle,
		iconCls : 'export'
	});
	
	// 导入按钮
	var btnInport = new Ext.Toolbar.Button({
		id : 'specialty_import',
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
					url : 'hr/importEmpSpecialtyFile.action',
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("提示", o.msg);
						speStore.reload();
					},
					failture : function() {
						Ext.Msg.alert(Constants.ERROR, "导入失败！");
					}
				})
			}
		}
	}

	var myRecord = Ext.data.Record.create([{
		name : 'specialtyId',
		mapping : 0
	}, {
		name : 'empId',
		mapping : 1
	}, {
		name : 'newEmpCode',
		mapping : 2
	}, {
		name : 'empName',
		mapping : 3
	}, {
		name : 'specialtyName',
		mapping : 4
	}, {
		name : 'specialtyLevel',
		mapping : 5
	}, {
		name : 'awardUnit',
		mapping : 6
	}, {
		name : 'awardDate',
		mapping : 7
	}, {
		name : 'memo',
		mapping : 8
	}]);

	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	})
	var workCM = new Ext.grid.ColumnModel([sm, new Ext.grid.RowNumberer({
		header : '行号',
		width : 35
	}), {
		header : '人员编码',
		width : 120,
		dataIndex : 'newEmpCode'
	}, {
		header : '特长名称',
		width : 120,
		dataIndex : 'specialtyName'
	}, {
		header : '特长等级',
		width : 80,
		dataIndex : 'specialtyLevel'
	}, {
		header : '发证单位',
		width : 100,
		dataIndex : 'awardUnit'
	}, {
		header : '发证时间',
		width : 100,
		dataIndex : 'awardDate'
	}, {
		header : '备注',
		width : 200,
		dataIndex : 'memo'
	}]);
	workCM.defaultSortable = true;

	var speStore = new Ext.data.JsonStore({
		url : 'hr/findSpecialtyList.action',
		params : {
			empId : empIdUsing
		},
		root : 'list',
		totalProperty : 'totalCount',
		fields : myRecord
	});

	var tbar = new Ext.Toolbar({
		items : [btnAdd, btnModify, btnDelete, tfAppend, btnInport,btnDownload
		 // add by sychen 20100713
    	                  ,{
							text : "导出",
							id : 'btnreport',
							iconCls : 'export',
							handler :function(){
			Ext.Ajax.request({
				url : 'hr/findSpecialtyList.action',
				params : {
				empId : empIdUsing,
				start : 0,
                limit : Constants.PAGE_SIZE
			},
				method : 'post',
				success : function(response,options){
				var res = Ext.decode(response.responseText).list;
				if(speStore.getTotalCount()==0)
				{
					 Ext.Msg.alert('提示', '无数据进行导出！');
			         return;
				}else
				{
			  if(res){
				Ext.Msg.confirm('提示', '确认要导出数据？', function(id) {
			    if (id == 'yes') {
				var tableHeader = "<table border=1><tr><th>人员编码</th><th>特长名称</th><th>特长等级</th><th>发证单位" +
						"</th><th>发证时间</th><th>备注</th></tr>";
				var html = [tableHeader];
					for (var i = 0; i <res.length; i++) {
					var rec = res[i];
					
   	        	    html.push('<tr><td align=left >' + (rec[2]==null?"":rec[2]) +'</td>' +
   	        	                          '<td align=left>'+( rec[4]==null?"":rec[4])+'</td>' +
   	        	                          '<td align=left>'+ (rec[5]==null?"":rec[5])+ '</td>' +
   	        	                            '<td align=left>'+ (rec[6]==null?"":rec[6])+ '</td>' +
   	        	                          '<td align=left>'+( rec[7]==null?"":rec[7])+  '</td>' +
   	        	                          '<td align=left>'+( rec[8]==null?"":rec[8])+ '</td></tr>')
				}
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
					
				}},
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
			ExWSh.Columns("E").ColumnWidth  = 15;
			ExWSh.Columns("F").ColumnWidth  = 35;
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
		items : [tbar]
	});

	var speGrid = new Ext.grid.GridPanel({
		id : 'speGrid',
		store : speStore,
		sm : sm,
		cm : workCM,
		bbar : new Ext.PagingToolbar({
			pageSize : Constants.PAGE_SIZE,
			store : speStore,
			displayInfo : true,
			displayMsg : Constants.DISPLAY_MSG,
			emptyMsg : Constants.EMPTY_MSG
		}),
		tbar : headForm,
		border : false,
		enableColumnMove : false
	});
	var spePanel = new Ext.Panel({
		id : 'spePanel',
		title : "人员特长",
		frame : true,
		border : false,
		layout : 'fit',
		items : [speGrid]
	})

	speGrid.on('rowdblclick', onModify);

	// ============== 定义弹出画面 ===========
	Ext.form.TextField.prototype.width = 180;

	var tfChsName = new Ext.form.TextField({
		id : 'workResume_chsName',
		fieldLabel : "员工姓名",
		readOnly : true,
		anchor : '80%',
		value : (config && config.chsName) ? config.chsName : null
	});

	var specialtyId = new Ext.form.Hidden({
		id : 'specialtyId',
		name : 'specialty.specialtyId'
	});

	var specialtyName = new Ext.form.TextField({
		id : 'specialtyName',
		name : 'specialty.specialtyName',
		fieldLabel : '特长名称',
		allowBlank : false,
		anchor : '80%'
	});

	var specialtyLevel = new Ext.form.TextField({
		id : 'specialtyLevel',
		name : 'specialty.specialtyLevel',
		fieldLabel : '特长级别',
		allowBlank : false,
		anchor : '80%'
	});

	var awardUnit = new Ext.form.TextField({
		id : 'awardUnit',
		name : 'specialty.awardUnit',
		fieldLabel : '发证单位',
		anchor : '80%'
	})

	var awardDate = new Ext.form.TextField({
		id : 'awardDate',
		name : 'specialty.awardDate',
		style : 'cursor:pointer',
		fieldLabel : "发证日期",
		anchor : '80%',
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

	var memo = new Ext.form.TextArea({
		id : 'memo',
		name : 'specialty.memo',
		fieldLabel : "备注",
		anchor : '80%',
		height : 80
	});

	var btnSave = new Ext.Button({
		text : Constants.BTN_SAVE,
		iconCls : Constants.CLS_SAVE,
		handler : onSave
	});

	var btnCancel = new Ext.Button({
		text : Constants.BTN_CANCEL,
		iconCls : Constants.CLS_CANCEL,
		handler : function() {
			Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_005, function(
					button, text) {
				if (button == "yes") {
					win.hide();
				}
			});
		}
	});

	var formPanel = new Ext.FormPanel({
		labelAlign : 'right',
		labelWidth : 80,
		frame : true,
		border : false,
		trackResetOnLoad : true,
		items : [specialtyId, tfChsName, specialtyName, specialtyLevel,
				awardUnit, awardDate, memo]
	});

	// 定义弹出窗体
	var win = new Ext.Window({
		modal : true,
		resizable : false,
		width : 380,
		height : 320,
		layout : 'fit',
		buttonAlign : "center",
		closeAction : 'hide',
		items : [formPanel],
		buttons : [btnSave, btnCancel]
	});

	// 加载员工工作简历信息
	function loadEmp(empParam) {
		if (!empParam || empParam.data.empId == null) {
			Ext.Msg.alert('提示', '人员id不存在，出现异常!');
			speGrid.getTopToolbar().setDisabled(true);
			return;
		}
		speGrid.getTopToolbar().setDisabled(false);
		empIdUsing = empParam.data.empId;
		tfChsName.setValue(empParam.data.chsName);
		speStore.baseParams = {
			empId : empIdUsing
		};
		speStore.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			}
		});
	}

	// 重新加载Grid
	function reloadGrid(options) {
		speStore.reload(options);
	}

	function onAdd() {
		formPanel.getForm().reset();
		win.setTitle('新增人员特长');
		win.show();
		win.center();
		formPanel.getForm().setValues({
			specialtyId : '',
			specialtyName : '',
			specialtyLevel : '',
			awardUnit : '',
			awardDate : '',
			memo : ''
		});
	}

	function onModify() {
		if (sm.hasSelection()) {
			if (sm.getSelections().length > 1)
				Ext.Msg.alert('提示', '请选择其中一条数据!');
			else {
				win.setTitle('修改人员特长')
				win.show();
				formPanel.getForm().setValues(sm.getSelected().data)
			}
		} else
			Ext.Msg.alert('提示', '请先选择记录！')
	}

	function onDelete() {
		if (sm.hasSelection()) {
			Ext.Msg.confirm('提示', '确认要删除数据吗？', function(buttonId) {
				if (buttonId == 'yes') {
					var ids = new Array();
					var selects = sm.getSelections();
					for (var i = 0; i < selects.length; i++) {
						ids.push(selects[i].get('specialtyId'))
					}
					if (ids.length > 0) {
						Ext.Ajax.request({
							url : 'hr/deleteEmpSpecialtyRecord.action',
							method : 'post',
							params : {
								ids : ids.join(",")
							},
							success : function(response, options) {
								if (response && response.responseText) {
									var res = Ext.decode(response.responseText)
									Ext.Msg.alert('提示', res.msg);
									speStore.reload();
								}
							},
							failure : function(response, options) {
								Ext.Msg.alert('提示', '删除数据出现异常！')
							}
						})
					}
				}
			})

		} else
			Ext.Msg.alert('提示', '请先选择要删除的数据！')
	}

	// Check处理
	function checkFields() {
		var msg = '';
		if (specialtyName.getValue() === '') {
			msg += String.format(Constants.COM_E_002, "特长名称") + '<br/>';
		}
		if (msg.length > 0) {
			Ext.Msg.alert(Constants.ERROR, msg.replace(/<br\/>$/, ''));
			return false;
		}
		return true;
	}

	// 保存按钮处理
	function onSave() {
		if (!checkFields()) {
			return;
		}
		Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_001, function(
				buttonobj) {
			if (buttonobj == "yes") {
				var isAddFlag = !specialtyId.getValue();
				// 保存数据
				formPanel.getForm().submit({
					method : Constants.POST,
					url : 'hr/editEmpSpecialtyRecord.action',
					params : {
						'empId' : empIdUsing,
						'isAdd' : isAddFlag
					},
					success : function(form, action) {
						var o = eval('(' + action.response.responseText + ')');
						// 排他异常
						if (o.msg == "U") {
							Ext.Msg.alert(Constants.ERROR, Constants.COM_E_015);
							return;
						}
						// 数据库异常
						if (o.msg == "SQL") {
							Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
							return;
						}
						// 重新加载Grid
						reloadGrid();
						Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004,
								function() {
									win.hide();
								});
					}
				});
			}
		});
	}
	// 员工姓名不可用
	tfChsName.setDisabled(true);

	this.spePanel = spePanel;
	this.loadEmp = loadEmp;
	this.speGrid = speGrid;
}
