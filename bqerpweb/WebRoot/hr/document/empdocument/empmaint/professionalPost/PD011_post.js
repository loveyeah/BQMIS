Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
    Ext.QuickTips.init();
    
    var employee = parent.Ext.getCmp('tabPanel').employee;
    
    // 获得当前日期
    function getDate() {
		var d, s, t,day;
		d = new Date();
		s = d.getFullYear().toString() + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		s +='-';
		day = d.getDate()
		s +=(day > 9 ? "" : "0") + day;
		return s;
	}
    
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
    
    // 打印按钮
    var btnPrint = new Ext.Button({
        text : '打印员工履历表',
        iconCls : Constants.CLS_PRINT,
        handler : onPrint
    });
    
    var postRecord = new Ext.data.Record.create([
    	{
    		// 技术职称id
    		name : 'id'
    	},{
    		// 人员id
    		name : 'empId'
    	},{
    		// 技术职称
    		name : 'professionalPost'
    	},{
    		// 技术等级
    		name : 'professionalLevel'
    	},{
    		// 是否当前
    		name : 'isNow'
    	},{
    		// 评定方式
    		name : 'judgeMode'
    	},{
    		// 证书名称
    		name : 'certificateName'
    	},{
    		// 证书编号
    		name : 'certificateCode'
    	},{
    		//发证部门
    		name : 'certificateDept'
    	},{
    		// 批准文号
    		name : 'approveCode'
    	},{
    		// 评定所在单位
    		name : 'judgeApproveDept'
    	},{
    		// 备注
    		name : 'memo'
    	},{
    		// 人员姓名
    		name : 'empName'
    	},{
    		// 技术职称名称
    		name : 'technologyName'
    	},{
    		// 获取日期
    		name : 'recruitmentDateString'
    	},{
    		//有效开始日期
    		name : 'validStartDateString'
    	},{
    		// 有效终止日期
    		name : 'validEndDateString'
    	}
    ])   
    
    // grid 列模式
    var postCM = new Ext.grid.ColumnModel([
    	new Ext.grid.RowNumberer({
    		header : '行号',
            width : 35
    	}),{
            header : '员工',
            width : 80,
            dataIndex : 'empName'
        },{
            header : '技术职称',
            width : 80,
            dataIndex : 'technologyName'
        },{
            header : '技术等级',
            width : 80,
            renderer : function(v){
            	if(v == '1'){
            		return '初级';
            	}else if(v == '2'){
            		return '中级';
            	}else if(v == '3'){
            		return '高级';
            	}else{
            		return '';
            	}
            },
            dataIndex : 'professionalLevel'
        },{
            header : '获取日期',
            width : 80,
            dataIndex : 'recruitmentDateString'
        },{
            header : '是否当前',
            width : 80,
            renderer : renderYesNo,
            dataIndex : 'isNow'
        },{
            header : '评定方式',
            width : 80,
            renderer : function(v){
            	if(v == '1'){
            		return '职称评定';
            	}else if(v == '2'){
            		return '资格评定';
            	}else{
            		return '';
            	}
            },
            dataIndex : 'judgeMode'
        },{
            header : '证书名称',
            width : 80,
            dataIndex : 'certificateName'
        },{
            header : '证书编号',
            width : 80,
            dataIndex : 'certificateCode'
        },{
            header : '发证部门',
            width : 80,
            dataIndex : 'certificateDept'
        },{
            header : '批准文号',
            width : 80,
            dataIndex : 'approveCode'
        },{
            header : '有效开始日期',
            width : 80,
            dataIndex : 'validStartDateString'
        },{
            header : '有效终止日期',
            width : 80,
//            renderer : renderYesNo,
            dataIndex : 'validEndDateString'
        },{
            header : '评定所在单位',
            width : 80,
            dataIndex : 'judgeApproveDept'
        },{
            header : '备注',
            width : 80,
            dataIndex : 'memo'
        }
    ])
    postCM.defaultSortable = true;
    
    // 数据源
    var postStore = new Ext.data.JsonStore({
        url : 'hr/getPostInfo.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : postRecord
    });
    // 分页工具栏
    var pagebar = new Ext.PagingToolbar({
        pageSize : Constants.PAGE_SIZE,
        store : postStore,
        displayInfo : true,
        displayMsg : Constants.DISPLAY_MSG,
        emptyMsg : Constants.EMPTY_MSG
    })
     // 职务任免Grid
    var postGrid = new Ext.grid.GridPanel({
        store : postStore,
        sm : new Ext.grid.RowSelectionModel({
                singleSelect : true
            }),
        cm : postCM,
        // 分页
        bbar : pagebar,
        tbar : [btnAdd, btnModify, btnDelete, '->', btnPrint],
        border : false,
        enableColumnMove : false
    });
    // 双击处理
    postGrid.on('rowdblclick', onModify);
    
    // 设定布局器及面板
    var layout = new Ext.Viewport({
        layout : "fit",
        items : [postGrid]
    });
    
    // ==============    定义弹出画面    ===========
    Ext.form.TextField.prototype.width = 150;
    Ext.form.CmbHRBussiness.prototype.width = 150;
    Ext.form.CmbHRCode.prototype.width = 150;
    var twoWd = 387;
    
    // 技术职称id
    var postId = new Ext.form.Hidden({
    	id : 'id',
    	name : 'post.id'
    })
    
    // 员工姓名
    var empName = new Ext.form.TextField({
    	id : 'empName',
    	name : 'empName',
    	fieldLabel : '员工姓名',
    	readOnly : true,
    	value : employee.chsName,
    	disabled : true
    })
    // 获取时间
    var recruitmentDate = new Ext.form.TextField({
    	id : 'recruitmentDate',
    	name : 'post.recruitmentDate',
    	fieldLabel : '获取时间',
    	readOnly : true,
    	value : getDate(),
    	listeners : {
    		focus :  function(){
    			WdatePicker({
                 startDate : '%y-%M-%d',
                 dateFmt : 'yyyy-MM-dd'
             });
             this.blur();
    		}
    	}
    })
     //第一面板
    var postFirstPanel = new Ext.Panel({
    	border : false,
    	layout : 'form',
    	style : 'padding-top:12px',
    	items : [{
    		border : false,
    		layout : 'column',
    		items : [{
    			columnWidth : 0.5,
    			layout : 'form',
    			items : [postId,empName]
    		},{
    			columnWidth : 0.5,
    			layout : 'form',
    			items : [recruitmentDate]
    		}
    		]
    	}]
    })
    
    // 技术职称
    var professionalPost = new Ext.form.CmbHRBussiness({
         id    :'professionalPost',
         hiddenName  :'post.professionalPost',
         fieldLabel :'技术职称',
         type   :'技术职称'
    });
    
     // 技术等级
      var levelStore = new Ext.data.SimpleStore({
      		fields : ['mode','text'],
      		data : [['',''],['1','初级'],['2','中级'],['3','高级']]
      })
      var professionalLevel = new Ext.form.ComboBox({
      	id : 'professionalLevel',
      	hiddenName : 'post.professionalLevel',
      	fieldLabel : '技术等级',
      	store : levelStore,
      	valueField : 'mode',
      	displayField : 'text',
      	mode : 'local',
      	triggerAction : 'all',
      	editable : false
      })
		//第二面板
    var postSecondPanel = new Ext.Panel({
    	border : false,
    	layout : 'form',
    	style : 'padding-top:5px',
    	items : [{
    		border : false,
    		layout : 'column',
    		items : [{
    			columnWidth : 0.5,
    			layout : 'form',
    			items : [professionalPost]
    		},{
    			columnWidth : 0.5,
    			layout : 'form',
    			items : [professionalLevel]
    		}]
    	}]
    })
	
    // 是否当前
    var isNow = {
        id : 'isNow',
        layout : 'Column',
        isFormField : true,
        fieldLabel : '是否当前',
        border : false,
        items : [{
            columnWidth : .5,
            border : false,
            items : new Ext.form.Radio({
                id : 'isNow1',
                boxLabel : '是',
                name : 'post.isNow',
                inputValue : 'Y',
                checked : false
            })
        }, {
            columnWidth : .5,
            items : new Ext.form.Radio({
                id : 'isNow2',
                boxLabel : '否',
                name : 'post.isNow',
                inputValue : 'N',
                checked : true
            })
        }]
    };
    // 评定方式
      var modeStore = new Ext.data.SimpleStore({
      		fields : ['mode','text'],
      		data : [['',''],['1','职称评定'],['2','资格评定']]
      })
      var judgeMode = new Ext.form.ComboBox({
      	id : 'judgeMode',
      	hiddenName : 'post.judgeMode',
      	fieldLabel : '评定方式',
      	store : modeStore,
      	valueField : 'mode',
      	displayField : 'text',
      	mode : 'local',
      	triggerAction : 'all',
      	editable : false
      })
    //第三面板
    var postThreePanel = new Ext.Panel({
    	border : false,
    	layout : 'form',
    	style : 'padding-top:5px',
    	items : [{
    		border : false,
    		layout : 'column',
    		items : [{
    			columnWidth : 0.5,
    			layout : 'form',
    			items : [isNow]
    		},{
    			columnWidth : 0.5,
    			layout : 'form',
    			items : [judgeMode]
    		}]
    	}]
    })
    
   // 有效开始日期
    var validStartDate = new Ext.form.TextField({
    	id : 'validStartDate',
    	name : 'post.validStartDate',
    	fieldLabel : '有效开始日期',
    	readOnly : true,
    	value : getDate(),
    	listeners : {
    		focus :  function(){
    			WdatePicker({
                 startDate : '%y-%M-%d',
                 dateFmt : 'yyyy-MM-dd'
             });
             this.blur();
    		}
    	}
    })
    
    // 有效终止日期
    var validEndDate = new Ext.form.TextField({
    	id : 'validEndDate',
    	name : 'post.validEndDate',
    	fieldLabel : '有效终止日期',
    	readOnly : true,
    	value : getDate(),
    	listeners : {
    		focus :  function(){
    			WdatePicker({
                 startDate : '%y-%M-%d',
                 dateFmt : 'yyyy-MM-dd'
             });
             this.blur();
    		}
    	}
    })
		
	//第四面板
    var postFourPanel = new Ext.Panel({
    	border : false,
    	layout : 'form',
    	style : 'padding-top:5px',
    	items : [{
    		border : false,
    		layout : 'column',
    		items : [{
    			columnWidth : 0.5,
    			layout : 'form',
    			items : [validStartDate]
    		},{
    			columnWidth : 0.5,
    			layout : 'form',
    			items : [validEndDate]
    		}]
    	}]
    })
    
    // 证书名称
    var certificateName = new Ext.form.TextField({
    	id : 'certificateName',
    	name : 'post.certificateName',
    	fieldLabel : '批准单位'
    })
    // 证书编号
    var certificateCode = new Ext.form.TextField({
    	id : 'certificateCode',
    	name : 'post.certificateCode',
    	fieldLabel : '证书编号'
    })
    
      //第五面板
    var postFivePanel = new Ext.Panel({
    	border : false,
    	layout : 'form',
    	style : 'padding-top:5px',
    	items : [{
    		border : false,
    		layout : 'column',
    		items : [{
    			columnWidth : 0.5,
    			layout : 'form',
    			items : [certificateName]
    		},{
    			columnWidth : 0.5,
    			layout : 'form',
    			items : [certificateCode]
    		}]
    	}]
    })
    // 发证部门
    var certificateDept = new Ext.form.TextField({
    	id : 'certificateDept',
    	name : 'post.certificateDept',
    	fieldLabel : '发证部门'
    })
    // 批准文号
    var approveCode = new Ext.form.TextField({
    	id : 'approveCode',
    	name : 'post.approveCode',
    	fieldLabel : '批准文号'
    })
    
    //第六面板
    var postSixPanel = new Ext.Panel({
    	border : false,
    	layout : 'form',
    	style : 'padding-top:5px',
    	items : [{
    		border : false,
    		layout : 'column',
    		items : [{
    			columnWidth : 0.5,
    			layout : 'form',
    			items : [certificateDept]
    		},{
    			columnWidth : 0.5,
    			layout : 'form',
    			items : [approveCode]
    		}]
    	}]
    })
    
    // 评定所在部门
    var judgeApproveDept = new Ext.form.TextField({
    	id : 'judgeApproveDept',
    	name : 'post.judgeApproveDept',
    	anchor : '99%',
    	fieldLabel : '评定所在部门'
    })
    // 备注
    var memo = new Ext.form.TextArea({
    	id : 'memo',
    	name : 'post.memo',
    	anchor : '99%',
    	fieldLabel : '备注'
    })
    //第七面板
    var postSevenPanel = new Ext.Panel({
    	border : false,
    	layout : 'form',
    	style : 'padding-top:5px',
    	items : [{
    		border : false,
    		layout : 'column',
    		items : [{
    			columnWidth : 1,
    			layout : 'form',
    			items : [judgeApproveDept,memo]
    		}]
    	}]
    })
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
            employee.confirm(Constants.CONFIRM, Constants.COM_C_005, function(button, text) {
                if (button == "yes") {
                    win.hide();
                }
            });
        }
    });
    
    var formPanel = new Ext.form.FormPanel({
        labelAlign :'right',
        labelWidth : 80,
        border : false,
        frame : true,
        items : [postFirstPanel,postSecondPanel,
               postThreePanel,postFourPanel,
               postFivePanel,postSixPanel,
               postSevenPanel]
    });
    
    // 定义弹出窗体
    var win = new Ext.Window({
        modal : true,
        resizable : false,
        width : 500,
        height : 390,
        layout : 'fit',
        buttonAlign : "center",
        closeAction : 'hide',
        items : [formPanel],
        buttons : [btnSave, btnCancel]
    });
    
    // ==========       处理开始       =============
    // 添加加载员工综合信息时的监听器
    employee.addLoadEmpHandler(loadEmpFamilyInfo);
    // 添加加载员工综合信息前的监听器
    employee.addBeforeLoadEmpHandler(beforLoadEmp);
    
      // 加载员工前的处理
    function beforLoadEmp(argEmpCode) {
        if (win.rendered && !win.hidden && !win.inValid) {
	    	formPanel.getForm().trim();
            return !checkPageChanged();
        }
        
        return true;
    }
    
     var pageFields = ['id',
        	'empId',
        	'professionalPost',
        	'professionalLevel',
        	'isNow',
        	'judgeMode',
        	'certificateName',
        	'certificateCode',
        	'certificateDept',
        	'approveCode',
        	'judgeApproveDept',
        	'memo',
        	'recruitmentDateString',
        	'validStartDateString',
        	'validEndDateString'];
    // 检查画面是否变更
    function checkPageChanged() {
    	var isAdd = !postId.getValue();
    	var record = isAdd ? {} : postGrid.getSelectionModel().getSelected().data;
    	
    	var pageDatas = formPanel.getForm().getValues();
    	var origialV, currentV, prop;
    	for (var i = 0; i < pageFields.length; i++) {
    		prop = pageFields[i];
    		origialV = isAdd ? '' : record[prop];
    		currentV = pageDatas[prop] || pageDatas['post.' + prop];
    		if (origialV === 'undefined'
                || origialV === 'null'
                || origialV == null) {
                origialV = "";
            }
            if (!origialV && ( prop === 'isNow')) {
        		origialV = "Y";
        	}
    		if (currentV === 'undefined'
                || currentV === 'null'
                || currentV == null) {
                currentV = "";
            }
            
    		if (origialV != currentV) {
    			return true;
    		}
    	}
    	return false;
    }
    
    function renderYesNo(value) {
        if (value == 'Y') {
            return '是';
        } else if (value == 'N') {
            return '否';
        }
        return '';
    }
    
    // 加载员工家庭成员信息 
    function loadEmpFamilyInfo() {
    	// 隐藏弹出画面
    	employee.closeWin('post', win);
    	
        postStore.baseParams = {
            empId : employee.empId
        };
        
        postStore.load({
            params:{
                start : 0,
                limit : Constants.PAGE_SIZE
            }
        });
        
        var enableFlag = employee.hasEmpId();
        // 新增按钮可用设置
        btnAdd.setDisabled(!enableFlag);
        // 修改按钮可用设置
        btnModify.setDisabled(!enableFlag);
        // 删除按钮可用设置
        btnDelete.setDisabled(!enableFlag);
        // 打印按钮可用设置
        btnPrint.setDisabled(!enableFlag);
    }
    
    // 重新加载Grid
    function reloadGrid(options) {
        postStore.reload(options);
    }
    
     // 新增按钮处理
    function onAdd() {
        formPanel.getForm().reset();
        // 新增家庭成员
        win.setTitle('新增技术职称记录');
        win.show();
        win.center();
    }
    
     // 修改按钮处理
    function onModify() {
        if (btnModify.hidden) {
            return;
        }
        if (!postGrid.getSelectionModel().hasSelection()) {
            employee.alert(Constants.REMIND, Constants.COM_E_016);
            return;
        }
        
        formPanel.getForm().reset();
        
        // 修改家庭成员
        win.setTitle('修改技术职称记录');
        win.show();
        win.center();
        
        // 获得选中的记录
        var record = postGrid.getSelectionModel().getSelected();
        formPanel.getForm().loadRecord(record);
        // 获取日期
        recruitmentDate.setValue(record.get('recruitmentDateString'))
        // 有效开始日期
        validStartDate.setValue(record.get('validStartDateString'))
        // 有效开始日期
        validEndDate.setValue(record.get('validStartDateString'))
        // 技术职称
        professionalPost.setValue(record.get('professionalPost'),true)
       
        
        // 是否当前
        if (record.get('isNow') == 'N') {
            Ext.getCmp('isNow2').setValue(true);
        }else if(record.get('isNow') == 'Y'){
        	Ext.getCmp('isNow1').setValue(true);
        }
    }
    
     // 删除按钮处理
    function onDelete() {
        if (!postGrid.getSelectionModel().hasSelection()) {
            employee.alert(Constants.REMIND, Constants.COM_E_016);
            return;
        }
        
        employee.confirm(Constants.CONFIRM, Constants.COM_C_002,
            function(buttonobj) {
                if (buttonobj == "yes") {
                    // 获得选中的记录
                    var record = postGrid.getSelectionModel().getSelected();
                          
                    // 删除数据
                    Ext.Ajax.request({
                        method : Constants.POST,
                        url : 'hr/deletePostInfo.action',
                        params : {
                            'post.id' : record.get('id')
                            },
                        success : function(result, request) {
                            var o = eval('(' + result.responseText + ')');
                            
                            // 排他异常
                            if(o.msg == "U") {
                                employee.alert(Constants.ERROR, Constants.COM_E_015);
                                return;
                            }
                            // 数据库异常
                            if(o.msg == "SQL") {
                                employee.alert(Constants.ERROR, Constants.COM_E_014);
                                return;
                            }
                            
                            // 重新加载Grid
                            reloadGrid({
					            params:{
					                start : 0,
					                limit : Constants.PAGE_SIZE
					            }
					        });
                            employee.alert(Constants.REMIND, Constants.COM_I_005);
                        },
                        failure : function() {
                            employee.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
                        }
                    });
                }
            }
        );    
    }
    
    
    // 打印按钮处理
    function onPrint() {
        employee.print();
    }
    
    // Check处理
    function checkFields() {
        var msg = '';
        if (empName.getValue() === '') {
            msg += String.format(Constants.COM_E_002, "姓名") + '<br/>';
        }
        if (!professionalPost.getValue() && professionalPost.getValue() != '0') {
            msg += String.format(Constants.COM_E_003, "技术职称") + '<br/>';
        }
        if (!validStartDate.getValue() && validStartDate.getValue() != '0') {
            msg += String.format(Constants.COM_E_003, "有效开始日期") + '<br/>';
        }
        if (!validEndDate.getValue() && validEndDate.getValue() != '0') {
            msg += String.format(Constants.COM_E_003, "有效终止日期") + '<br/>';
        }
        if (msg.length > 0) {
            employee.alert(Constants.ERROR, msg.replace(/<br\/>$/, ''));
            return false;
        }
        return true;
    }
    
    // 提交前设置Combo的值，保证其以Number类型传回服务端
    function setComboValue() {
        // 技术职称
        if (!professionalPost.getValue()) {
            professionalPost.setValue('');
        }
        // 技术等级
        if (!professionalLevel.getValue()) {
            professionalLevel.setValue('');
        }
        // 评定方式
        if (!judgeMode.getValue()) {
            judgeMode.setValue('');
        }
    }
    
    // 保存按钮处理
    function onSave() {
        if (!checkFields()) {
            return;
        }
        
        employee.confirm(Constants.CONFIRM, Constants.COM_C_001,
            function(buttonobj) {
                if (buttonobj == "yes") {
                    var isAddFlag = !postId.getValue();
                    // 提交前设置Combo的值
                    setComboValue();
                    
                    // 保存数据
                    formPanel.getForm().submit({
                        method : Constants.POST,
                        url : 'hr/savePostInfo.action',
                        params : {
                            'post.empId' : employee.empId,
                            'isAdd' : isAddFlag
                            },
                        success : function(form, action) {
                            var o = eval('(' + action.response.responseText + ')');
                            
                            // 排他异常
                            if(o.msg == "U") {
                                employee.alert(Constants.ERROR, Constants.COM_E_015);
                                return;
                            }
                            // 数据库异常
                            if(o.msg == "SQL") {
                                employee.alert(Constants.ERROR, Constants.COM_E_014);
                                return;
                            }
                            
                            // 重新加载Grid
                            reloadGrid();
                            employee.alert(Constants.REMIND, Constants.COM_I_004, function() {
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
    // 打印按钮不可用
    btnPrint.setDisabled(true);
    // 加载员工基本信息
    loadEmpFamilyInfo(); 
    if (employee.editable) {
        // 打印按钮不可用
        btnPrint.setVisible(false);
        // 新增按钮可用设置
        btnAdd.setVisible(true);
        // 修改按钮可用
        btnModify.setVisible(true);
        // 保存按钮可用
        btnDelete.setVisible(true);
    } else {
        // 打印按钮可用
        btnPrint.setVisible(true);
        // 新增按钮可用设置
        btnAdd.setVisible(false);
        // 修改按钮不可用
        btnModify.setVisible(false);
        // 保存按钮不可用
        btnDelete.setVisible(false);
    }
    
     // 右键禁用
     document.onkeydown = function()
 {
          if(event.keyCode==116) {
          event.keyCode=0;
          event.returnValue = false;
          }
}
document.oncontextmenu = function() {event.returnValue = false;} 
})