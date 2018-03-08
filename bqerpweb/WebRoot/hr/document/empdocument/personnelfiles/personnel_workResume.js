Ext.ns("Personnel")
Personnel.WorkResume = function(config){

    var empIdUsing = null;
    var newEmpCode=null;//add by sychen 20100713
//    var employee = parent.Ext.getCmp('tabPanel').employee;
    
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
	// 导入按钮
	var btnInport = new Ext.Toolbar.Button({
		id : 'workResume_inport',
		text : '导入',
		handler : uploadQuestFile,
		iconCls : 'upLoad'
	});
	
	//模板下载 by ghzhou 2010-07-02
	//----------begin----------
	function downloadMoudle(){
		window.open("./downloadMoudle/工作经历-模板.xls")
	}
	
	var btnDownload = new Ext.Toolbar.Button({
		id : 'btnDownload',
		text : '模板下载',
		handler : downloadMoudle,
		iconCls : 'export'
	});
	//------------end-----------
	
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
					url : 'hr/importPersonnelFilesInfo.action',
					params : {
						type : 'workResume'
					},
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("提示", o.msg);
						workStore.reload();
						
					},
					failture : function() {
						Ext.Msg.alert(Constants.ERROR, "导入失败！");
					}
				})
			}
		}
	}
    // 打印按钮
//    var btnPrint = new Ext.Button({
//        text : '打印员工履历表',
//        iconCls : Constants.CLS_PRINT,
//        handler : onPrint
//    });
    
    
    var workRecord = Ext.data.Record.create([{
            // 单位名称
            name : 'unit'
        }, {
            // 开始日期
            name : 'startDate'
        }, {
            // 结束日期
            name : 'endDate'
        }, {
            // 工作岗位
            name : 'stationName'
        }, {
            // 职务名称
            name : 'headshipName'
        }, {
            // 证明人
            name : 'witness'
        }, {
            // 备注
            name : 'memo'
        }, {
            // 工作简历ID
            name : 'workresumeid'
        }, {
            // 上次修改日期
            name : 'lastModifiedDate'
    },{
    	// 工作部门
    	name : 'deptName'
    }]);
    var sm = new Ext.grid.CheckboxSelectionModel({
                singleSelect : false
            })
    // grid列模式
    var workCM = new Ext.grid.ColumnModel([sm,
        new Ext.grid.RowNumberer({
            header : '行号',
            width : 35
        }), {
            header : '单位名称',
            width : 120,
            dataIndex : 'unit'
        }, {
            header : '工作部门',
            width : 120,
            dataIndex : 'deptName'
        }, {
            header : '工作岗位',
            width : 80,
            dataIndex : 'stationName'
        }, {
            header : '开始日期',
            width : 100,
            renderer : renderDate,
            dataIndex : 'startDate'
        }, {
            header : '结束日期',
            renderer : renderDate,
            width : 100,
            dataIndex : 'endDate'
        }, {
            header : '职务名称',
            hidden : true,
            width : 80,
            dataIndex : 'headshipName'
        }, {
            header : '证明人',
            width : 60,
            hidden : true,
            dataIndex : 'witness'
        }, {
            header : '备注',
            width : 200,
            dataIndex : 'memo'
    }]);
    workCM.defaultSortable = true;
    
    // 数据源
    var workStore = new Ext.data.JsonStore({
        url : 'hr/getEmpWorkresumeInfo.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : workRecord
    });
    
    var tbar = new Ext.Toolbar({
    	items : [btnAdd, btnModify, btnDelete,tfAppend,btnInport,btnDownload/*, '->', btnPrint*/  
    // add by sychen 20100712
    	                  ,{
							text : "导出",
							id : 'btnreport',
							iconCls : 'export',
							handler :function(){
			Ext.Ajax.request({
				url : 'hr/getEmpWorkresumeInfo.action',
				params : {
				empId : empIdUsing,
				start : 0,
                limit : Constants.PAGE_SIZE
			},
				method : 'post',
				success : function(response,options){
				var res = Ext.decode(response.responseText).list;
				if(workStore.getTotalCount()==0)
				{
					 Ext.Msg.alert('提示', '无数据进行导出！');
			         return;
				}else
				{
			  if(res){
				Ext.Msg.confirm('提示', '确认要导出数据？', function(id) {
			    if (id == 'yes') {
				var tableHeader = "<table border=1><tr><th>人员编码</th><th>单位名称</th><th>工作部门 </th><th>工作岗位</th><th>开始日期</th><th>结束日期</th><th>备注</th> </tr>";
				var html = [tableHeader];
					for (var i = 0; i <res.length; i++) {
					var rec = res[i];
   	        	    html.push('<tr><td align=left>'+( newEmpCode==null?"":newEmpCode)+'</td>' +
   	        	    		              '<td align=left >' + (rec.unit==null?"":rec.unit) +'</td>' +
   	        	                          '<td align=left>'+( rec.deptName==null?"":rec.deptName)+'</td>' +
   	        	                          '<td align=left>'+ (rec.stationName==null?"":rec.stationName )+ '</td>' +
   	        	                          '<td align=left>'+ (rec.startDate==null?"":renderDate(rec.startDate))+ '</td>' +
   	        	                          '<td align=left>'+( rec.endDate==null?"":renderDate(rec.endDate))+  '</td>' +
   	        	                          '<td align=left>'+( rec.memo==null?"":rec.memo)+ '</td></tr>')
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
    // add by sychen 20100712  end
		]
    })
        function tableToExcel(tableHTML) {
		window.clipboardData.setData("Text", tableHTML);
		try {
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExWSh.Columns("A").ColumnWidth  = 20;
			ExWSh.Columns("B").ColumnWidth  = 20;
			ExWSh.Columns("C").ColumnWidth  = 20;
			ExWSh.Columns("D").ColumnWidth  = 20;
			ExWSh.Columns("E").ColumnWidth  = 20;
			ExWSh.Columns("F").ColumnWidth  = 20;
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
    // 工作简历Grid
    var workGrid = new Ext.grid.GridPanel({
    	id : 'workGrid',
        store : workStore,
//        title : "工作简历",
        sm : sm,
        cm : workCM,
        // 分页
        bbar : new Ext.PagingToolbar({
                pageSize : Constants.PAGE_SIZE,
                store : workStore,
                displayInfo : true,
                displayMsg : Constants.DISPLAY_MSG,
                emptyMsg : Constants.EMPTY_MSG
            }),
        tbar : headForm,
        border : false,
        enableColumnMove : false
    });
    var workPanel = new Ext.Panel({
    	id : 'workPanel',
    	title : "工作简历",
    	frame : true,
    	border : false,
    	layout : 'fit',
    	items : [workGrid]
    })
    // 双击处理
    workGrid.on('rowdblclick', onModify);
//    workGrid.on('celldblclick', showWindow);
    
    // 设定布局器及面板
//    var layout = new Ext.Viewport({
//        layout : "fit",
//        items : [workGrid]
//    });
    
    
    // ==============    定义弹出画面    ===========
    Ext.form.TextField.prototype.width = 180;
    var twoWd = 350;
    var height = 100;
    
    
    // 员工姓名
    var tfChsName = new Ext.form.TextField({
        id : 'workResume_chsName',
        fieldLabel : "员工姓名",
        readOnly : true,
        value : (config&&config.chsName)?config.chsName:null
    });
  
    // 开始日期
    var tfStartDate = new Ext.form.TextField({
        id : 'workResume_startDate',
        name : 'workResume.startDate',
        style : 'cursor:pointer',
        fieldLabel : "开始日期<font color='red'>*</font>",
        allowBlank : false,
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    isShowClear : false,
                    onpicked : function(){
                        tfStartDate.clearInvalid();
                    },
                    onclearing:function(){
                        tfStartDate.markInvalid();
                    }
                });
            }
        }
    });
    
    // 结束日期
    var tfEndDate = new Ext.form.TextField({
        id : 'workResume_endDate',
        name : 'workResume.endDate',
        style : 'cursor:pointer',
        fieldLabel : "结束日期",
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
    
    // 证明人
    var tfWitness = new Ext.form.Hidden({
        id : 'workResume_witness',
        name : 'workResume.witness'
    });
    
    // 单位名称
    var tfUnit = new Ext.form.TextField({
        id : 'workResume_unit',
        name : 'workResume.unit',
        fieldLabel : "单位名称<font color='red'>*</font>",
        allowBlank : false,
        maxLength : 15,
        width : twoWd
    });
    
    // 工作岗位
    var tfStationName = new Ext.form.TextField({
        id : 'workResume_stationName',
        name : 'workResume.stationName',
        fieldLabel : "工作岗位<font color='red'>*</font>",
        allowBlank : false,
        maxLength : 30
    });
    // 工作部门
    var tfDeptName = new Ext.form.TextField({
        id : 'workResume_deptName',
        name : 'workResume.deptName',
        fieldLabel : "工作部门<font color='red'>*</font>",
        allowBlank : false,
        maxLength : 50
    });
    
    // 职务名称
    var tfHeadshipName = new Ext.form.Hidden({
        id : 'workResume_headshipName',
        name : 'workResume.headshipName'
    });
    
    // 备注
    var tfMemo = new Ext.form.TextArea({
        id : 'workResume_memo',
        name : 'workResume.memo',
        fieldLabel : "备注",
        maxLength : 127,
        width : twoWd,
        height : height
    });
    
    
    // 工作简历ID
    var hideWorkResumeId = new Ext.form.Hidden({
        id : 'workResume_workresumeid',
        name : 'workResume.workresumeid'
    });
    
    // 上次修改时间
    var hideLastModifiedDate = new Ext.form.Hidden({
        id : 'workResume_lastModifiedDate',
        name : 'workResume.lastModifiedDate'
    });
    
    // 保存按钮
    var btnSave = new Ext.Button({
        text : Constants.BTN_SAVE,
        iconCls : Constants.CLS_SAVE,
        handler : onSave
    });
    
    // 取消按钮
    var btnCancel = new Ext.Button({
        text : Constants.BTN_CANCEL,
        iconCls : Constants.CLS_CANCEL,
        handler : function() {
            Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_005, function(button, text) {
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
        items : [tfChsName ,tfUnit,tfDeptName,tfStationName, tfStartDate, tfEndDate,
            tfWitness, 
            tfHeadshipName, tfMemo,
            hideWorkResumeId, hideLastModifiedDate
        ]
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
    
    // ==========       处理开始       =============
    // 添加加载员工综合信息时的监听器
//    employee.addLoadEmpHandler(loadEmpWorkInfo);
//    // 添加更改员工名字时的监听器
//    employee.addNameChangeHandler(function() {
//        tfChsName.setValue(employee.chsName);
//    });
//    // 添加加载员工综合信息前的监听器
//    employee.addBeforeLoadEmpHandler(beforLoadEmp);
//    
//    // 加载员工前的处理
//    function beforLoadEmp(argEmpCode) {
//        if (win.rendered && !win.hidden && !win.inValid) {
//	    	formPanel.getForm().trim();
//            return !formPanel.getForm().isDirty();
//        }
//        
//        return true;
//    }
    
    // 加载员工工作简历信息 
//    function loadEmpWorkInfo(empId) {
	function loadEmp(empParam) {
		if(!empParam || empParam.data.empId == null ){
			Ext.Msg.alert('提示','人员id不存在，出现异常!');
			workGrid.getTopToolbar().setDisabled(true);
			return;
		}
		workGrid.getTopToolbar().setDisabled(false);
		empIdUsing = empParam.data.empId;
		newEmpCode=empParam.data.newEmpCode;//add by sychen 20100713
		tfChsName.setValue(empParam.data.chsName);
    	// 隐藏弹出画面
//    	employee.closeWin('workResume', win);
//    	win.hide();
//    	alert(empId)
        workStore.baseParams = {
            empId : empIdUsing
        };
        
        workStore.load({
            params:{
                start : 0,
                limit : Constants.PAGE_SIZE
            }
        });
        
//        var enableFlag = employee.hasEmpId();
//        // 新增按钮可用设置
//        btnAdd.setDisabled(!enableFlag);
//        // 修改按钮可用设置
//        btnModify.setDisabled(!enableFlag);
//        // 删除按钮可用设置
//        btnDelete.setDisabled(!enableFlag);
//        // 打印按钮可用设置
//        btnPrint.setDisabled(!enableFlag);
    }
    
    // 重新加载Grid
    function reloadGrid(options) {
        workStore.reload(options);
    }
    
    function renderDate(value) {
        if (value instanceof Date) {
            return value.dateFormat('Y-m-d');
        }
        value = value ? value.match(/\d{4}-\d{2}-\d{2}/gi) : '';
        return value ? value[0] : '';
    }
    
    function renderModifyDate(value) {
        if (!value) return "";
        if (value instanceof Date) {
            return value.dateFormat('Y-m-d H:i:s');
        }
        var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
        var reTime = /\d{2}:\d{2}:\d{2}/gi;
        var strDate = value.match(reDate);
        var strTime = value.match(reTime);
        if (!strDate) return "";
        strTime = strTime ? strTime[0] : '00:00:00';
        return strDate[0] + " " + strTime;
    }
    
    // 显示时间比较方法
    function compareDate(argDate1, argDate2) {
        return argDate1.getTime() < argDate2.getTime();
    }
    
    // textField显示时间比较方法
    function compareDateStr(argDateStr1, argDateStr2){
        var date1 = Date.parseDate(argDateStr1, 'Y-m-d');
        var date2 = Date.parseDate(argDateStr2, 'Y-m-d');
        return compareDate(date1, date2);
    }
    
    // 新增按钮处理
    function onAdd() {
        formPanel.getForm().reset();
        // 新增工作简历
        win.setTitle('新增工作简历');
        win.show();
        win.center();
        
        formPanel.getForm().setValues({
        	workresumeid: '',
        	lastModifiedDate: '',
        	startDate: '',
        	endDate: '',
        	witness: null,
        	unit: '',
        	stationName: '',
        	headshipName: null,
        	memo: '',
        	deptName : ''
        });
        formPanel.getForm().clearInvalid();
    }
    
    // 显示弹出备注查看对话框
//    function showWindow(grid, row, col) {
//    	if (!btnModify.hidden) {
//            return;
//        }
//    	var dataIndex = grid.getColumnModel().getDataIndex(col);
//    	if (dataIndex === 'memo') {
//    		employee.showMemoWin(grid.getStore().getAt(row).get(dataIndex));
//    	}
//    }
    
    // 修改按钮处理
    function onModify() {
        if (btnModify.hidden) {
            return;
        }
        if (!workGrid.getSelectionModel().hasSelection()) {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_E_016);
            return;
        }
        if (workGrid.getSelectionModel().getSelections().length > 1) {
            Ext.Msg.alert(Constants.REMIND, "请选择其中一条!");
            return;
        }
        formPanel.getForm().reset();
        
        // 修改工作简历
        win.setTitle('修改工作简历');
        win.show();
        win.center();
        
        // 获得选中的记录
        var record = workGrid.getSelectionModel().getSelected();
        // 开始日期
        record.data.startDate = renderDate(record.get('startDate'));
        // 结束日期
        record.data.endDate = renderDate(record.get('endDate'));
        // 上次修改时间
        record.data.lastModifiedDate = renderDate(record.get('lastModifiedDate'));
        tfUnit.setValue(record.data.unit);
        tfDeptName.setValue(record.data.deptName);
        tfStationName.setValue(record.data.stationName);
        tfStartDate.setValue(record.data.startDate);
        tfEndDate.setValue(record.data.endDate);
        tfWitness.setValue(record.data.witness);
        tfHeadshipName.setValue(record.data.headshipName);
        tfMemo.setValue(record.data.memo);
        hideWorkResumeId.setValue(record.data.workresumeid);
        hideLastModifiedDate.setValue(record.data.lastModifiedDate);
    }
    
    // 删除按钮处理
    function onDelete() {
        if (!workGrid.getSelectionModel().hasSelection()) {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_E_016);
            return;
        }
        
        Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_002,
            function(buttonobj) {
                if (buttonobj == "yes") {
                    // 获得选中的记录
                    var records = workGrid.getSelectionModel().getSelections();
                    var ids = new Array();
                    for(var i = 0; i < records.length; i++){
                    	ids.push(records[i].get("workresumeid"))
                    }
                    // 删除数据
                    Ext.Ajax.request({
                        method : Constants.POST,
                        url : 'hr/deleteMutilEmpWorkresumeInfo.action',
                        params : {
                        	ids : ids.join(","),
                        	type : 'workResume'
                            },
                        success : function(result, request) {
                            var o = eval('(' + result.responseText + ')');
                            
                            // 重新加载Grid
                            reloadGrid({
					            params:{
					                start : 0,
					                limit : Constants.PAGE_SIZE
					            }
					        });
                            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_005);
                        },
                        failure : function() {
                            Ext.Msg.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
                        }
                    });
                }
            }
        );
    }
    
    // 打印按钮处理
//    function onPrint() {
//        alert('打印')
//    }
    
    // Check处理
    function checkFields() {
        var msg = '';
        if (tfStartDate.getValue() === '') {
            msg += String.format(Constants.COM_E_002, "开始日期") + '<br/>';
        }
        if (tfUnit.getValue() === '') {
            msg += String.format(Constants.COM_E_002, "单位名称") + '<br/>';
        }
        if (tfStationName.getValue() === '') {
            msg += String.format(Constants.COM_E_002, "工作岗位") + '<br/>';
        }
        if (tfDeptName.getValue() === '') {
            msg += String.format(Constants.COM_E_002, "工作部门") + '<br/>';
        }
        
//        if (tfHeadshipName.getValue() === '') {
//            msg += String.format(Constants.COM_E_002, "职务名称") + '<br/>';
//        }
        
        if (msg.length > 0) {
            Ext.Msg.alert(Constants.ERROR, msg.replace(/<br\/>$/, ''));
            return false;
        }
        return true;
    }
    
    // 关联Check处理
    function checkRefFields() {
    	var msg = '';
    	if (tfEndDate.getValue() !== '') {
            if (!compareDateStr(tfStartDate.getValue(), tfEndDate.getValue())) {
                msg += String.format(Constants.COM_E_006, "开始日期", "结束日期") + '<br/>';
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
        
        Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_001,
            function(buttonobj) {
                if (buttonobj == "yes") {
                	if (!checkRefFields()) {
			            return;
			        }
			        
                    var isAddFlag = !hideWorkResumeId.getValue();
                    // 保存数据
                    formPanel.getForm().submit({
                        method : Constants.POST,
                        url : 'hr/saveEmpWorkresumeInfo.action',
                        params : {
                            'workResume.empId' : empIdUsing,
                            'isAdd' : isAddFlag
                            },
                        success : function(form, action) {
                            var o = eval('(' + action.response.responseText + ')');
                            
                            // 排他异常
                            if(o.msg == "U") {
                                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_015);
                                return;
                            }
                            // 数据库异常
                            if(o.msg == "SQL") {
                                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                return;
                            }
                            
                            // 重新加载Grid
                            reloadGrid();
                            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004, function() {
                                win.hide();
                            });
                        }
                    });
                }
            }
        );
    }
    // ==========       处理结束       =============
    
    
    // ==========       初期化处理        ===========
    // 打印按钮
//    btnPrint.setDisabled(true);
    // 加载员工基本信息
    
//    if (employee.editable) {
//        // 打印按钮不可用
//        btnPrint.setVisible(false);
//        // 新增按钮可用设置
//        btnAdd.setVisible(true);
//        // 修改按钮可用
//        btnModify.setVisible(true);
//        // 保存按钮可用
//        btnDelete.setVisible(true);
//    } else {
//        // 打印按钮可用
//        btnPrint.setVisible(true);
//        // 新增按钮可用设置
//        btnAdd.setVisible(false);
//        // 修改按钮不可用
//        btnModify.setVisible(false);
//        // 保存按钮不可用
//        btnDelete.setVisible(false);
//    }
    
    // 员工姓名不可用
    tfChsName.setDisabled(true);
    
	this.workPanel = workPanel;
	this.loadEmp = loadEmp;
	this.workGrid = workGrid;
}
