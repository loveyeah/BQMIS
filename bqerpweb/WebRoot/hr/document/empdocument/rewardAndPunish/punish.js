Ext.namespace('Personnel.EmpPunish');
Personnel.EmpPunish = function(config){	
	var empId;
	Ext.form.TextField.prototype.width = 180;
    Ext.QuickTips.init();
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
		window.open("../personnelfiles/downloadMoudle/处罚情况-模板.xls")
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
		id : 'reward_inport',
		text : '导入',
		handler : uploadQuestFile,
		iconCls : 'upLoad'
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
				url : 'hr/getPunishList.action',
				params : {
				empId : empId,
				start : 0,
                limit : Constants.PAGE_SIZE
			},
				method : 'post',
				success : function(response,options){
				var res = Ext.decode(response.responseText).list;
				
				if(store.getTotalCount()==0)
				{
					 Ext.Msg.alert('提示', '无数据进行导出！');
			         return;
				}else
				{
			  if(res){
				Ext.Msg.confirm('提示', '确认要导出数据？', function(id) {
			    if (id == 'yes') {
				var tableHeader = "<table border=1><tr><th>人员编码</th><th>处罚名称 </th><th>处罚时间</th><th>给予处罚单位" +
						"</th><th>处罚原因</th><th>处罚起始时间</th><th>处罚终止时间</th><th>处罚执行期限</th><th>备注</th></tr>";
				var html = [tableHeader];
					for (var i = 0; i <res.length; i++) {
					var rec = res[i];
   	        	    html.push('<tr><td align=left >' + (rec[2]==null?"":rec[2]) +'</td>' +
   	        	                          '<td align=left>'+( rec[3]==null?"":rec[3])+'</td>' +
   	        	                          '<td align=left>'+ (rec[4]==null?"":rec[4] )+ '</td>' +
   	        	                          '<td align=left>'+ (rec[5]==null?"":rec[5])+ '</td>' +
   	        	                          '<td align=left>'+( rec[9]==null?"":rec[9])+  '</td>' +
   	        	                          '<td align=left>'+( rec[6]==null?"":rec[6])+'</td>' +
   	        	                          '<td align=left>'+( rec[7]==null?"":rec[7])+  '</td>' +
   	        	                          '<td align=left>'+( rec[8]==null?"":rec[8])+'</td>' +
   	        	                          '<td align=left>'+( rec[10]==null?"":rec[10])+ '</td></tr>')
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
			ExWSh.Columns("F").ColumnWidth  = 15;
			ExWSh.Columns("G").ColumnWidth  = 15;
			ExWSh.Columns("H").ColumnWidth  = 15;
			ExWSh.Columns("I").ColumnWidth  = 25;
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
	

	var MyRecord = Ext.data.Record.create([{
				name : 'punishinfId',
				mapping : 0
			}, {
				name : 'empId',
				mapping : 1
			}, {
				name : 'empCode',
				mapping : 2
			}, {
				name : 'punishName',
				mapping : 3
			}, {
				name : 'punishTime',
				mapping : 4
			}, {
				name : 'punishUnit',
				mapping : 5
			},{
				name  : 'punishExecuteTime',
				mapping : 6
			},{
				name : 'punishEndTime',
				mapping : 7
			},{
				name  : 'punishDeadline',
				mapping : 8
			},{
				name : 'punishReason',
				mapping : 9
			},{
				name  : 'memo',
				mapping : 10
			}]);

	var dataProxy = new Ext.data.HttpProxy(
			{
				url:'hr/getPunishList.action'
			}

	);

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"

	}, MyRecord);

	var store = new Ext.data.Store({

		proxy : dataProxy,

		reader : theReader

	});
    var sm = new Ext.grid.CheckboxSelectionModel({
    	singleSelect : false
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
					url : 'hr/importPunishInf.action',
					params : {
						type : 'punishInf'
					},
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						store.reload();
						Ext.Msg.alert("提示", o.msg);
						
					},
					failture : function() {
						Ext.Msg.alert(Constants.ERROR, "导入失败！");
					}
				})
			}
		}
	}
	var grid = new Ext.grid.GridPanel({
		store : store,
		columns : [sm, 
		new Ext.grid.RowNumberer({
		header : '行号',
		width : 35,
		align:'center'
	}), {
			header : "人员编码",
			sortable : true,
			width : 75,
			dataIndex : 'empCode',
			align:'center'
		}, 
		{
			header : "处罚名称",
			sortable : true,
			width : 100,
			dataIndex : 'punishName',
			align:'center'
		}, 
		{
			header : "处罚时间",
			sortable : true,
			width : 80,
			dataIndex : 'punishTime',
			align:'center'
		}, 
		{
			header : "给予处罚单位",
			sortable : true,
			dataIndex : 'punishUnit',
			align:'center'
		},{
			header : "处罚原因",
			sortable : true,
			width : 80,
			dataIndex : 'punishReason',
			align:'center'
		}, 
		{
			header : "处罚起始时间",
			sortable : true,
			dataIndex : 'punishExecuteTime',
			align:'center'
		},{
			header : "处罚终止时间",
			sortable : true,
			dataIndex : 'punishEndTime',
			align:'center'
		}, 
		{
			header : "处罚执行期限(天)",
			sortable : true,
			dataIndex : 'punishDeadline',
			align:'center'
		},{
			header : "备注",
			sortable : true,
			dataIndex : 'memo',
			align:'center'
		}
		],
		sm : sm,
		tbar : headForm,	
		bbar : new Ext.PagingToolbar({
                pageSize : Constants.PAGE_SIZE,
                store : store,
                displayInfo : true,
                displayMsg : Constants.DISPLAY_MSG,
                emptyMsg : Constants.EMPTY_MSG
            })
	});
	
	var punishPanel = new Ext.Panel({
		id : 'punishPanel',
		title : "惩罚情况",
		frame : true,
		border : false,
		layout : 'fit',
		items : [grid]
	})

	
	var punishId = new Ext.form.Hidden({
		id : 'punishinfId',
		name : 'punish.punishinfId'
	});
   var empCode = new Ext.form.TextField({
		id : 'punish.empCode',
		fieldLabel : "人员编码",
		readOnly : true,
		anchor : '80%',
		value : (config && config.newEmpCode) ? config.newEmpCode : null
	});
	empCode.setDisabled(true);
	var punishName = new Ext.form.TextField({
		id : 'punishName',
		name : 'punish.punishName',
		fieldLabel : '处罚名称',
		allowBlank : false,
		anchor : '80%'
	});
   var punishTime = new Ext.form.TextField({
		id : 'punishTime',
		name : 'punish.punishTime',
		style : 'cursor:pointer',
		fieldLabel : "处罚时间",
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
	var punishUnit = new Ext.form.TextField({
		id : 'punishUnit',
		name : 'punish.punishUnit',
		fieldLabel : '给予处罚单位',
		allowBlank : false,
		anchor : '80%'
	});
   var punishReason = new Ext.form.TextField({
		id : 'punishReason',
		name : 'punish.punishReason',
		fieldLabel : '处罚原因',
		allowBlank : false,
		anchor : '80%'
	});
	var punishExecuteTime = new Ext.form.TextField({
		id : 'punishExecuteTime',
		name : 'punish.punishExecuteTime',
		style : 'cursor:pointer',
		fieldLabel : "处罚起始时间",
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
   var punishEndTime = new Ext.form.TextField({
		id : 'punishEndTime',
		name : 'punish.punishEndTime',
		style : 'cursor:pointer',
		fieldLabel : "处罚终止时间",
		anchor : '80%',
		readOnly : true,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd',
					onpicked : function() {
								if (punishExecuteTime.getValue() == ""
										|| punishExecuteTime.getValue() > punishEndTime
												.getValue()) {
									Ext.Msg.alert("提示", "必须大于或等于执行开始时间");
									punishEndTime.setValue("")
									return;
								}
							}
				});
			}
		}
	});
	var punishDeadline = new Ext.form.TextField({
		id : 'punishDeadline',
		name : 'punish.punishDeadline',
		fieldLabel : '处罚执行期限',
		allowBlank : false,
		anchor : '80%'
	});
	var memo = new Ext.form.TextArea({
		id : 'memo',
		name : 'punish.memo',
		fieldLabel : "备注",
		anchor : '80%',
		height : 60
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
		items : [punishId, empCode, punishName, punishTime,
				punishUnit,punishReason, punishExecuteTime, punishEndTime,punishDeadline,memo]
	});

	// 定义弹出窗体
	var win = new Ext.Window({
		modal : true,
		resizable : false,
		width : 380,
		height : 350,
		layout : 'fit',
		buttonAlign : "center",
		closeAction : 'hide',
		items : [formPanel],
		buttons : [btnSave, btnCancel]
	});
	function onAdd() {
		
		win.setTitle('新增惩罚情况信息');
		win.show();
		win.center();
		formPanel.getForm().reset();
		formPanel.getForm().setValues({
        	punishId: '',
        	punishName: '',
        	punishTime: '',
        	punishUnit: '',
        	punishReason: '',
        	punishExecuteTime: '',
        	punishEndTime: '',
        	punishDeadline:'',
        	memo:''
        });
         formPanel.getForm().clearInvalid();
		empCode.setValue(newEmpCode);
	}
	function onDelete() {
		if (sm.hasSelection()) {
			Ext.Msg.confirm('提示', '确认要删除数据吗？', function(buttonId) {
				if (buttonId == 'yes') {
					var ids = new Array();
					var selects = sm.getSelections();
					for (var i = 0; i < selects.length; i++) {
						ids.push(selects[i].get('punishinfId'))
					}
					if (ids.length > 0) {
						Ext.Ajax.request({
							url : 'hr/delPunish.action',
							method : 'post',
							params : {
								ids : ids.join(",")
							},
							success : function(response, options) {
								if (response && response.responseText) {
									var res = Ext.decode(response.responseText)
									Ext.Msg.alert('提示', res.msg);
									store.reload();
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

	function onModify() {
		if (sm.hasSelection()) {
			if (sm.getSelections().length > 1)
				Ext.Msg.alert('提示', '请选择其中一条数据!');
			else {
				win.setTitle('修改惩罚情况信息')
				win.show();
				formPanel.getForm().setValues(sm.getSelected().data)
			}
		} else
			Ext.Msg.alert('提示', '请先选择记录！')
	}
	grid.on('rowdblclick', onModify);
	function reloadGrid(options) {
		store.reload(options);
	}
	// 保存按钮处理
	var	method;
	function onSave() {
		
		Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_001, function(
				buttonobj) {
			if (buttonobj == "yes") {
				var ID  = punishId.getValue();
				if((ID==null)||(ID==""))
				{
				var	method='add';
				}else
				{
					method='update';
				}
				// 保存数据
				formPanel.getForm().submit({
					method : Constants.POST,
					url : 'hr/addOrUpdatePunish.action',
					params : {
						'empId' : empId,
						'method' : method
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
						store.reload();
						reloadGrid();
						Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004,
								function() {
								formPanel.getForm().reset();
									win.hide();
								});
					}
				});
			}
		});
	}
	var newEmpCode;
	function loadEmp(empParam) {
		if (!empParam ||( empParam.data.empId == null)) {
			Ext.Msg.alert('提示', '人员id不存在，出现异常!');
			grid.getTopToolbar().setDisabled(true);
			return;
		}
		grid.getTopToolbar().setDisabled(false);
		empId = empParam.data.empId;
		empCode.setValue(empParam.data.newEmpCode);
		newEmpCode=empParam.data.newEmpCode;
		store.baseParams = {
			empId : empId
		};
		store.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			}
		});
	}
	this.punishPanel = punishPanel;
	this.loadEmp = loadEmp;
	this.grid = grid;
};
