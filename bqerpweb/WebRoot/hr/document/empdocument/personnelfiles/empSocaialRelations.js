Ext.ns("Personnel")
Personnel.EmpSocialRelations = function(config) {
	Ext.QuickTips.init();
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
		name : 'uploadFile',
		inputType : 'file',
		width : 200
	})
	
	//----------begin----------
	function downloadMoudle(){
		window.open("./downloadMoudle/社会主要关系-模板.xls")
	}
	
	var btnDownload = new Ext.Toolbar.Button({
		id : 'btnDownload',
		text : '模板下载',
		handler : downloadMoudle,
		iconCls : 'export'
	});
	//------------end-----------
	
	// 导入按钮
	var btnInport = new Ext.Toolbar.Button({
		id : 'socialRelation_inport',
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
					url : 'hr/importEmpSocialRelationFiles.action',
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("提示", o.msg);
						relationStore.reload();
					},
					failture : function() {
						Ext.Msg.alert(Constants.ERROR, "导入失败！");
					}
				})
			}
		}
	}

	var myRecord = Ext.data.Record.create([{
		name : 'relationId',
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
		name : 'relationName',
		mapping : 4
	}, {
		name : 'relationTitle',
		mapping : 5
	}, {
		name : 'isDeath',
		mapping : 6
	}, {
		name : 'deathDate',
		mapping : 7
	}, {
		name : 'isMajorProblem',
		mapping : 8
	}, {
		name : 'majorProblem',
		mapping : 9
	}, {
		name : 'professional',
		mapping : 10
	}, {
		name : 'face',
		mapping : 11
	}, {
		name : 'isNationals',
		mapping : 12
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
		header : '姓名',
		width : 120,
		dataIndex : 'relationName'
	}, {
		header : '称谓',
		width : 80,
		dataIndex : 'relationTitle'
	}, {
		header : '是否已故',
		hidden : true,
		width : 100,
		dataIndex : 'isDeath',
		renderer : function(value) {
			if (value == 'Y') {
				return '是';
			} else if (value == 'N') {
				return '否';
			}
			return '';
		}
	}, {
		header : '已故时间',
		width : 100,
		hidden : true,
		dataIndex : 'deathDate'
	}, {
		header : '有无重大历史问题',
		dataIndex : 'isMajorProblem',
		hidden:true,//add by sychen 20100715
		renderer : function(value) {
			if (value == 'Y') {
				return '有';
			} else if (value == 'N') {
				return '无';
			}
			return '';
		}
	}//update by sychen 20100715
//	, {
//		header : '重大历史问题',
//		dataIndex : 'majorProblem'
//	}
	, {
		header : '获得奖励情况',
		dataIndex : 'majorProblem'
	}
	, {
		header : '职业',
		dataIndex : 'professional'
	}, {
		header : '政治面貌',
		dataIndex : 'face'
	}, {
		header : '是否港澳台侨民',
		dataIndex : 'isNationals',
		renderer : function(value) {
			if (value == 'Y') {
				return '是';
			} else if (value == 'N') {
				return '否';
			}
			return '';
		}
	}]);
	workCM.defaultSortable = true;

	var relationStore = new Ext.data.JsonStore({
		url : 'hr/findEmpSocialRelation.action',
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
				url : 'hr/findEmpSocialRelation.action',
				params : {
				empId : empIdUsing,
				start : 0,
                limit : Constants.PAGE_SIZE
			},
				method : 'post',
				success : function(response,options){
				var res = Ext.decode(response.responseText).list;
				
				var isNationals=null;
				if(relationStore.getTotalCount()==0)
				{
					 Ext.Msg.alert('提示', '无数据进行导出！');
			         return;
				}else
				{
			  if(res){
				Ext.Msg.confirm('提示', '确认要导出数据？', function(id) {
			    if (id == 'yes') {
				var tableHeader = "<table border=1><tr><th>人员编码</th><th>姓名</th><th>称谓</th>" +
						"<th>获得奖励情况</th><th>职业</th><th>政治面貌</th><th>是否港澳台侨民</th></tr>";
				var html = [tableHeader];
					for (var i = 0; i <res.length; i++) {
					var rec = res[i];
					
			 if (rec[12] == 'Y') {
				isNationals= '是';
			 } else if (rec[12] == 'N') {
				isNationals= '否';
			}
			else isNationals= '';
			
   	        	    html.push('<tr><td align=left >' + (rec[2]==null?"":rec[2]) +'</td>' +
   	        	                          '<td align=left>'+( rec[4]==null?"":rec[4])+'</td>' +
   	        	                          '<td align=left>'+ (rec[5]==null?"":rec[5])+ '</td>' +
   	        	                          '<td align=left>'+( rec[9]==null?"":rec[9])+  '</td>' +
   	        	                          '<td align=left>'+( rec[10]==null?"":rec[10])+'</td>' +
   	        	                          '<td align=left>'+( rec[11]==null?"":rec[11])+  '</td>' +
   	        	                          '<td align=left>'+isNationals+ '</td></tr>')
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
			ExWSh.Columns("A").ColumnWidth  = 12;
			ExWSh.Columns("B").ColumnWidth  = 12;
			ExWSh.Columns("C").ColumnWidth  = 12;
			ExWSh.Columns("D").ColumnWidth  = 35;
			ExWSh.Columns("E").ColumnWidth  = 12;
			ExWSh.Columns("F").ColumnWidth  = 12;
			ExWSh.Columns("G").ColumnWidth  = 15;
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
		id : 'socialRelation-panel',
		height : 28,
		frame : false,
		fileUpload : true,
		layout : 'form',
		items : [tbar]
	});

	var relationGrid = new Ext.grid.GridPanel({
		id : 'relationGrid',
		store : relationStore,
		sm : sm,
		cm : workCM,
		bbar : new Ext.PagingToolbar({
			pageSize : Constants.PAGE_SIZE,
			store : relationStore,
			displayInfo : true,
			displayMsg : Constants.DISPLAY_MSG,
			emptyMsg : Constants.EMPTY_MSG
		}),
		tbar : headForm,
		border : false,
		enableColumnMove : false
	});
	var relationPanel = new Ext.Panel({
		id : 'relationPanel',
		title : "社会主要关系",
		frame : true,
		border : false,
		layout : 'fit',
		items : [relationGrid]
	})

	relationGrid.on('rowdblclick', onModify);

	// ============== 定义弹出画面 ===========
	Ext.form.TextField.prototype.width = 180;

	var tfChsName = new Ext.form.Hidden({
		id : 'socialRelation_chsName',
		fieldLabel : "员工姓名",
		readOnly : true,
		anchor : '80%',
		value : (config && config.chsName) ? config.chsName : null
	});

	var relationId = new Ext.form.Hidden({
		id : 'relationId',
		name : 'relation.relationId'
	});

	var relationName = new Ext.form.TextField({
		id : 'relationName',
		name : 'relation.relationName',
		fieldLabel : '姓名',
		allowBlank : false,
		anchor : '80%'
	});

	var relationTitle = new Ext.form.TextField({
		id : 'relationTitle',
		name : 'relation.relationTitle',
		fieldLabel : '称谓',
		allowBlank : false,
		anchor : '80%'
	});

	var deathDate = new Ext.form.TextField({
		id : 'deathDate',
		name : 'relation.deathDate',
		style : 'cursor:pointer',
		fieldLabel : "已故日期",
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

	var isDeath = {
		id : 'isDeath',
		layout : 'Column',
		isFormField : true,
		fieldLabel : '是否已故',
		border : false,
		items : [{
			columnWidth : .5,
			border : false,
			items : new Ext.form.Radio({
				id : 'isDeath1',
				boxLabel : '是',
				name : 'relation.isDeath',
				inputValue : 'Y',
				//checked : true
					listeners:{
						   'check' : function(radio,check){
							if(check)
							{
								deathDate.setDisabled(false);
							}
						}	 
					}
			})
		}, {
			columnWidth : .5,
			items : new Ext.form.Radio({
				id : 'isDeath2',
				boxLabel : '否',
				name : 'relation.isDeath',
				inputValue : 'N',
				listeners:{
						'check' : function(radio,check){
							if(check)
							{
								deathDate.setValue("");
								deathDate.setDisabled(true);
							}
						}	 
					}
			})
		}]
	};

	var isMajorProblem = {
		id : 'isMajorProblem',
		layout : 'Column',
		isFormField : true,
		fieldLabel : '是否有重大历史问题',
		border : false,
		items : [{
			columnWidth : .5,
			border : false,
			items : new Ext.form.Radio({
				id : 'isMajorProblem1',
				boxLabel : '是',
				name : 'relation.isMajorProblem',
				inputValue : 'Y',
				//checked : true,
				listeners:{
						   'check' : function(radio,check){
							if(check)
							{
								majorProblem.setDisabled(false);
							}
						}	 
					}
			})
		}, {
			columnWidth : .5,
			items : new Ext.form.Radio({
				id : 'isMajorProblem2',
				boxLabel : '否',
				name : 'relation.isMajorProblem',
				inputValue : 'N',
				listeners:{
						'check' : function(radio,check){
							if(check)
							{
								majorProblem.setValue("");
								majorProblem.setDisabled(true);
							}
						}	 
					}
			})
		}]
	};

	var isNationals = {
		id : 'isNationals',
		layout : 'Column',
		isFormField : true,
		fieldLabel : '是否港澳台侨民',
		border : false,
		items : [{
			columnWidth : .5,
			border : false,
			items : new Ext.form.Radio({
				id : 'isNationals1',
				boxLabel : '是',
				name : 'relation.isNationals',
				inputValue : 'Y',
				checked : true
			})
		}, {
			columnWidth : .5,
			items : new Ext.form.Radio({
				id : 'isNationals2',
				boxLabel : '否',
				name : 'relation.isNationals',
				inputValue : 'N'
			})
		}]
	};
	
	var majorProblem = new Ext.form.TextArea({
		id : 'majorProblem',
		name : 'relation.majorProblem',
//		fieldLabel : "重大历史问题",
		fieldLabel : "获得奖励情况",
		anchor : '89%',
		height : 80
	});

	var professional = new Ext.form.TextField({
		id : 'professional',
		name : 'relation.professional',
		fieldLabel : "职业",
		anchor : '80%'
	});

	var face = new Ext.form.TextField({
		id : 'face',
		name : 'relation.face',
		fieldLabel : "政治面貌",
		anchor : '80%'
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
		items : [{
			border : false,
			layout : 'column',
			items : [{
				columnWidth : .5,
				layout : 'form',
				border : false,
				labelWidth : 70,
				items : [relationId, tfChsName, relationName, relationTitle]
			}, {
				columnWidth : .5,
				layout : 'form',
				labelWidth : 70,
				border : false,
				items : [professional, face]
			}]
		}, {
			border : false,
			layout : 'column',
			items : [{
				columnWidth : .5,
				layout : 'form',
				labelWidth : 70,
				border : false,
				items : [isDeath, isNationals]
			}, {
				columnWidth : .5,
				layout : 'form',
				border : false,
				labelWidth : 70,
				items : [deathDate
//				, isMajorProblem
				]

			}]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 1,
			labelWidth : 70,
			items : [majorProblem]
		}]
	});

	// 定义弹出窗体
	var win = new Ext.Window({
		modal : true,
		resizable : false,
		width : 550,
		height : 330,
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
			relationGrid.getTopToolbar().setDisabled(true);
			return;
		}
		relationGrid.getTopToolbar().setDisabled(false);
		empIdUsing = empParam.data.empId;
		tfChsName.setValue(empParam.data.chsName);
		relationStore.baseParams = {
			empId : empIdUsing
		};
		relationStore.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			}
		});
	}

	// 重新加载Grid
	function reloadGrid(options) {
		relationStore.reload(options);
	}

	function onAdd() {
		formPanel.getForm().reset();
		win.setTitle('新增社会主要关系');
		win.show();
		win.center();
		formPanel.getForm().setValues({
			relationId : '',
			relationName : '',
			relationTitle : '',
			deathDate : '',
			majorProblem : '',
			professional : '',
			face : ''
		});
		Ext.get("isDeath1").dom.checked = true;
//		Ext.get("isMajorProblem1").dom.checked = true;
	}

	function onModify() {
		if (sm.hasSelection()) {
			if (sm.getSelections().length > 1)
				Ext.Msg.alert('提示', '请选择其中一条数据!');
			else {
				formPanel.getForm().reset();
				win.setTitle('修改社会主要关系')
				win.show();
				var record = relationGrid.getSelectionModel().getSelected();
				formPanel.getForm().loadRecord(record);
				if (record.get('isDeath') == 'N') {
					Ext.get("isDeath2").dom.checked = true;
					deathDate.setDisabled(true);
				}else{
					Ext.get("isDeath1").dom.checked = true;
					deathDate.setDisabled(false);
				}
				if (record.get('isMajorProblem') == 'N') {
					Ext.get("isMajorProblem2").dom.checked = true;
					majorProblem.setDisabled(true);
				}else{
//					Ext.get("isMajorProblem1").dom.checked = true;
					majorProblem.setDisabled(false);
				}
				if (record.get('isNationals') == 'N') {
					Ext.get("isNationals2").dom.checked = true;
				}
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
						ids.push(selects[i].get('relationId'))
					}
					if (ids.length > 0) {
						Ext.Ajax.request({
							url : 'hr/deleteRelation.action',
							method : 'post',
							params : {
								ids : ids.join(",")
							},
							success : function(response, options) {
								if (response && response.responseText) {
									var res = Ext.decode(response.responseText)
									Ext.Msg.alert('提示', res.msg);
									relationStore.reload();
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
		if (relationName.getValue() == '') {
			msg += String.format(Constants.COM_E_002, "姓名") + '<br/>';
		}
		 if (relationTitle.getValue()=='') {
            msg += String.format(Constants.COM_E_002, "称谓") + '<br/>';
        }
        if(Ext.get("isDeath1").dom.checked == true)
        {
        	if(deathDate.getValue()=='')
        	{
        		 msg += String.format(Constants.COM_E_002, "已故日期") + '<br/>';
        	}
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
				var isAddFlag = !relationId.getValue();
				//	setComboValue();
				// 保存数据
				formPanel.getForm().submit({
					method : Constants.POST,
					url : 'hr/addAndUpdateRelation.action',
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

	this.relationPanel = relationPanel;
	this.loadEmp = loadEmp;
	this.relationGrid = relationGrid;
}
