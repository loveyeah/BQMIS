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
    
    var positionRecord = new Ext.data.Record.create([
    	{
    		// 职务任免Id
    		name : 'positionId'
    	},{
    		// 人员id
    		name : 'empId'
    	},{
    		// 职务名称
    		name : 'positionName'
    	},{
    		// 是否任职
    		name : 'isPosition'
    	},{
    		// 任免文号
    		name : 'positionCode'
    	},{
    		// 当前职务
    		name : 'isNow'
    	},{
    		// 职务等级
    		name : 'positionLevel'
    	},{
    		// 任免批准单位
    		name : 'approveDept'
    	},{
    		//任免职方式
    		name : 'rmMode'
    	},{
    		// 任免原因
    		name : 'rmReason'
    	},{
    		// 任免意见
    		name : 'rmView'
    	},{
    		// 备注
    		name : 'memo'
    	},{
    		// 任免时间
    		name : 'rmDateString'
    	},{
    		// 员工姓名
    		name : 'empName'
    	},{
    		// 员工所属部门
    		name : 'empDeptName'
    	},{
    		// 职务名称 描述
    		name : 'positionDescri'
    	},{
    		// 职务等级 描述
    		name : 'levelDescri'
    	},{
    		// 任免职批准单位名称
    		name : 'approveDeptName'
    		
    	}
    ])   
    
    // grid 列模式
    var positionCM = new Ext.grid.ColumnModel([
    	new Ext.grid.RowNumberer({
    		header : '行号',
            width : 35
    	}),{
            header : '员工',
            width : 80,
            dataIndex : 'empName'
        },{
            header : '部门',
            width : 80,
            dataIndex : 'empDeptName'
        },{
            header : '任免时间',
            width : 80,
            dataIndex : 'rmDateString'
        },{
            header : '职务',
            width : 80,
            dataIndex : 'positionDescri'
        },{
            header : '职务等级',
            width : 80,
            dataIndex : 'levelDescri'
        },{
            header : '职务等级',
            width : 80,
            dataIndex : 'levelDescri'
        },{
            header : '是否任职',
            width : 80,
            renderer : renderYesNo,
            dataIndex : 'isPosition'
        },{
            header : '任免文号',
            width : 80,
            dataIndex : 'positionCode'
        },{
            header : '任免批准单位',
            width : 80,
            dataIndex : 'approveDeptName'
        },{
            header : '当前职务',
            width : 80,
            renderer : renderYesNo,
            dataIndex : 'isNow'
        },{
            header : '任免职方式',
            width : 80,
            dataIndex : 'rmMode',
            renderer : function(v){
            	if(v == '1'){
            		return '落选';
            	}else if(v == '2'){
            		return '解聘';
            	}else if(v == '3'){
            		return '罢免';
            	}else if(v == '4'){
            		return '届满';
            	}else if(v == '5'){
            		return '其他';
            	}else if(v == '6'){
            		return '任命';
            	}else if(v == '7'){
            		return '委任';
            	}else if(v == '8'){
            		return '选举';
            	}else{
            		return '';
            	} 
            }
        },{
            header : '任免职原因',
            width : 80,
            dataIndex : 'rmReason'
        },{
            header : '任免职意见',
            width : 80,
            dataIndex : 'rmView'
        },{
            header : '备注',
            width : 80,
            dataIndex : 'memo'
        }
    ])
    positionCM.defaultSortable = true;
    
    // 数据源
    var positionStore = new Ext.data.JsonStore({
        url : 'hr/getPositionInfo.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : positionRecord
    });
    // 分页工具栏
    var pagebar = new Ext.PagingToolbar({
        pageSize : Constants.PAGE_SIZE,
        store : positionStore,
        displayInfo : true,
        displayMsg : Constants.DISPLAY_MSG,
        emptyMsg : Constants.EMPTY_MSG
    })
     // 职务任免Grid
    var positionGrid = new Ext.grid.GridPanel({
        store : positionStore,
        sm : new Ext.grid.RowSelectionModel({
                singleSelect : true
            }),
        cm : positionCM,
        // 分页
        bbar : pagebar,
        tbar : [btnAdd, btnModify, btnDelete, '->', btnPrint],
        border : false,
        enableColumnMove : false
    });
    // 双击处理
    positionGrid.on('rowdblclick', onModify);
    
    // 设定布局器及面板
    var layout = new Ext.Viewport({
        layout : "fit",
        items : [positionGrid]
    });
    
    // ==============    定义弹出画面    ===========
    Ext.form.TextField.prototype.width = 150;
    Ext.form.CmbHRBussiness.prototype.width = 150;
    Ext.form.CmbHRCode.prototype.width = 150;
    var twoWd = 387;
    
    // 职务任免id
    var positionId = new Ext.form.Hidden({
    	id : 'positionId',
    	name : 'position.positionId'
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
    // 所在部门
    var empDeptName = new Ext.form.TextField({
    	id : 'empDeptName',
    	name : 'empDeptName',
    	fieldLabel : '所在部门',
    	readOnly : true,
    	disabled : true,
    	value : employee.deptName
    })
     //第一面板
    var positionFirstPanel = new Ext.Panel({
    	border : false,
    	layout : 'form',
    	style : 'padding-top:12px',
    	items : [{
    		border : false,
    		layout : 'column',
    		items : [{
    			columnWidth : 0.5,
    			layout : 'form',
    			items : [positionId,empName]
    		},{
    			columnWidth : 0.5,
    			layout : 'form',
    			items : [empDeptName]
    		}
    		]
    	}]
    })
    
    // 任免时间
    var rmDateString = new Ext.form.TextField({
    	id : 'rmDate',
    	name : 'position.rmDate',
    	fieldLabel : '任免时间',
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
    
    // 职务store
		var empStationstore = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
				url : 'empInfoManage.action?method=getstation'
			}),
			reader : new Ext.data.JsonReader({
					// id : "plant"
					}, [{
						name : 'id'
					}, {
						name : 'text'
					}])
		});
		empStationstore.load();
		var positionName = new Ext.form.ComboBox({
			fieldLabel : "职务名称<font color='red'>*</font>",
			name : 'positionName',
//			xtype : 'combo',
			editable : true,
			allowBlank : false,
			id : 'positionName',
			store : empStationstore,
			valueField : "id",
			displayField : "text",
			mode : 'local',
			typeAhead : true,
			forceSelection : true,
			hiddenName : 'position.positionName',
			triggerAction : 'all',
			selectOnFocus : true,
			blankText : '请选择',
			emptyText : '请选择',
			allowBlank : false
		});
		//第二面板
    var positionSecondPanel = new Ext.Panel({
    	border : false,
    	layout : 'form',
    	style : 'padding-top:5px',
    	items : [{
    		border : false,
    		layout : 'column',
    		items : [{
    			columnWidth : 0.5,
    			layout : 'form',
    			items : [rmDateString]
    		},{
    			columnWidth : 0.5,
    			layout : 'form',
    			items : [positionName]
    		}]
    	}]
    })
	
    // 是否任职
    var isPosition = {
        id : 'isPosition',
        layout : 'Column',
        isFormField : true,
        fieldLabel : '是否任职',
        border : false,
        items : [{
            columnWidth : .5,
            border : false,
            items : new Ext.form.Radio({
                id : 'isPosition1',
                boxLabel : '是',
                name : 'position.isPosition',
                inputValue : 'Y',
                checked : false
            })
        }, {
            columnWidth : .5,
            items : new Ext.form.Radio({
                id : 'isPosition2',
                boxLabel : '否',
                name : 'position.isPosition',
                inputValue : 'N',
                checked : true
            })
        }]
    };
    // 任免文号
    var positionCode = new Ext.form.TextField({
    	id : 'positionCode',
    	name : 'position.positionCode',
    	fieldLabel : '任免文号',
    	maxLength : 100
    })
    //第三面板
    var positionThreePanel = new Ext.Panel({
    	border : false,
    	layout : 'form',
    	style : 'padding-top:5px',
    	items : [{
    		border : false,
    		layout : 'column',
    		items : [{
    			columnWidth : 0.5,
    			layout : 'form',
    			items : [isPosition]
    		},{
    			columnWidth : 0.5,
    			layout : 'form',
    			items : [positionCode]
    		}]
    	}]
    })
    
     // 当前职务
    var isNow = {
        id : 'isNow',
        layout : 'Column',
        isFormField : true,
        fieldLabel : '当前职务',
        border : false,
        items : [{
            columnWidth : .5,
            border : false,
            items : new Ext.form.Radio({
                id : 'isNow1',
                boxLabel : '是',
                name : 'position.isNow',
                inputValue : 'Y',
                checked : false
            })
        }, {
            columnWidth : .5,
            items : new Ext.form.Radio({
                id : 'isNow2',
                boxLabel : '否',
                name : 'position.isNow',
                inputValue : 'N',
                checked : true
            })
        }]
    };
    // 岗位级别:
		var stationLestore = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
				url : 'empInfoManage.action?method=getStationlevel'
			}),
			reader : new Ext.data.JsonReader({
					// id : "plant"
					}, [{
						name : 'id'
					}, {
						name : 'text'
					}])
		});
		stationLestore.load();
		var positionLevel = new Ext.form.ComboBox({
			fieldLabel : '职务等级',
			name : 'positionLevel',
//			xtype : 'combo',
			id : 'positionLevel',
			store : stationLestore,
			valueField : "id",
			displayField : "text",
			mode : 'remote',
			typeAhead : true,
			forceSelection : true,
			hiddenName : 'position.positionLevel',
			editable : false,
			triggerAction : 'all',
			blankText : '请选择',
			emptyText : '请选择',
			selectOnFocus : true
		});
		
	//第四面板
    var positionFourPanel = new Ext.Panel({
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
    			items : [positionLevel]
    		}]
    	}]
    })
    
    // 任免批准单位
    var approveDeptName = new Ext.form.TextField({
    	id : 'approveDeptName',
    	name : 'approveDeptName',
    	fieldLabel : '批准单位',
    	readOnly : true
    })
    var approveDept = new Ext.form.Hidden({
    	id : 'approveDept',
    	name : 'position.approveDept'
    })
     // 选择部门
    approveDeptName.onClick(deptSelect);
     function deptSelect() {
                var args = {
                    selectModel : 'single',
                    rootNode : {
                        id : '0',
                        text : '灞桥电厂'
                    }
                };
                // 调用画面
                var object = window.showModalDialog(
                                '../../../../../comm/jsp/hr/dept/dept.jsp',
                                args,
                                'dialogWidth=' + Constants.WIDTH_COM_DEPT + 'px;' +
                                'dialogHeight=' + Constants.HEIGHT_COM_DEPT + 'px;' +
                                'center=yes;help=no;resizable=no;status=no;');
                // 根据返回值设置画面的值
                if (object) {
                    if (typeof(object.names) != "undefined") {
                        approveDeptName.setValue(object.names);
                    }
                    if (typeof(object.ids) != "undefined") {
                        approveDept.setValue(object.ids);
                    }
                }
            }
     // 任免职方式
      var rmModeStore = new Ext.data.SimpleStore({
      		fields : ['mode','text'],
      		data : [['',''],['1','落选'],['2','解聘'],['3','罢免'],['4','届满'],['5','其他'],
      			['6','任命'],['7','委任'],['8','选举']]
      })
      var rmMode = new Ext.form.ComboBox({
      	id : 'rmMode',
      	hiddenName : 'position.rmMode',
      	fieldLabel : '任免职方式',
      	store : rmModeStore,
      	valueField : 'mode',
      	displayField : 'text',
      	mode : 'local',
      	triggerAction : 'all',
      	editable : false
      })
      //第五面板
    var positionFivePanel = new Ext.Panel({
    	border : false,
    	layout : 'form',
    	style : 'padding-top:5px',
    	items : [{
    		border : false,
    		layout : 'column',
    		items : [{
    			columnWidth : 0.5,
    			layout : 'form',
    			items : [approveDeptName,approveDept]
    		},{
    			columnWidth : 0.5,
    			layout : 'form',
    			items : [rmMode]
    		}]
    	}]
    })
    // 任免原因
    var rmReason = new Ext.form.TextField({
    	id : 'rmReason',
    	name : 'position.rmReason',
    	anchor : '98%',
    	fieldLabel : '任免原因'
    })
    // 任免意见
    var rmView = new Ext.form.TextField({
    	id : 'rmView',
    	name : 'position.rmView',
    	anchor : '98%',
    	fieldLabel : '任免意见'
    })
    // 备注
    var memo = new Ext.form.TextArea({
    	id : 'memo',
    	name : 'position.memo',
    	anchor : '98%',
    	fieldLabel : '备注'
    })
    //第六面板
    var positionSixPanel = new Ext.Panel({
    	border : false,
    	layout : 'form',
    	style : 'padding-top:5px',
    	items : [{
    		border : false,
    		layout : 'column',
    		items : [{
    			columnWidth : 1,
    			layout : 'form',
    			items : [rmReason,rmView,memo]
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
        labelWidth : 70,
        border : false,
        frame : true,
        items : [positionFirstPanel,positionSecondPanel,
               positionThreePanel,positionFourPanel,
               positionFivePanel,positionSixPanel]
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
    
     var pageFields = ['positionId',
        	'empId',
        	'positionName',
        	'isPosition',
        	'positionCode',
        	'isNow',
        	'positionLevel',
        	'approveDept',
        	'rmMode',
        	'rmReason',
        	'rmView',
        	'memo',
        	'rmDateString'];
    // 检查画面是否变更
    function checkPageChanged() {
    	var isAdd = !positionId.getValue();
    	var record = isAdd ? {} : positionGrid.getSelectionModel().getSelected().data;
    	
    	var pageDatas = formPanel.getForm().getValues();
    	var origialV, currentV, prop;
    	for (var i = 0; i < pageFields.length; i++) {
    		prop = pageFields[i];
    		origialV = isAdd ? '' : record[prop];
    		currentV = pageDatas[prop] || pageDatas['position.' + prop];
    		if (origialV === 'undefined'
                || origialV === 'null'
                || origialV == null) {
                origialV = "";
            }
            if (!origialV && (
            	prop === 'isPosition'
                	|| prop === 'isNow')) {
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
    	employee.closeWin('position', win);
    	
        positionStore.baseParams = {
            empId : employee.empId
        };
        
        positionStore.load({
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
        positionStore.reload(options);
    }
    
     // 新增按钮处理
    function onAdd() {
        formPanel.getForm().reset();
        // 新增家庭成员
        win.setTitle('新增职务任免记录');
        win.show();
        win.center();
    }
    
     // 修改按钮处理
    function onModify() {
        if (btnModify.hidden) {
            return;
        }
        if (!positionGrid.getSelectionModel().hasSelection()) {
            employee.alert(Constants.REMIND, Constants.COM_E_016);
            return;
        }
        
        formPanel.getForm().reset();
        
        // 修改家庭成员
        win.setTitle('修改职务任免记录');
        win.show();
        win.center();
        
        // 获得选中的记录
        var record = positionGrid.getSelectionModel().getSelected();
        formPanel.getForm().loadRecord(record);
        // 任免时间
        rmDateString.setValue(record.get('rmDateString'));
        // 职务名称
        positionName.setValue(record.get('positionName'),true);
        // 职务等级
        positionLevel.setValue(record.get('positionLevel'),true)
       
        // 是否任职
        if (record.get('isPosition') == 'N') {
            Ext.getCmp('isPosition2').setValue(true);
        }else if(record.get('isPosition') == 'Y'){
        	Ext.getCmp('isPosition1').setValue(true);
        }
        // 当前职务
        if (record.get('isNow') == 'N') {
            Ext.getCmp('isNow2').setValue(true);
        }else if(record.get('isNow') == 'Y'){
        	Ext.getCmp('isNow1').setValue(true);
        }
    }
    
     // 删除按钮处理
    function onDelete() {
        if (!positionGrid.getSelectionModel().hasSelection()) {
            employee.alert(Constants.REMIND, Constants.COM_E_016);
            return;
        }
        
        employee.confirm(Constants.CONFIRM, Constants.COM_C_002,
            function(buttonobj) {
                if (buttonobj == "yes") {
                    // 获得选中的记录
                    var record = positionGrid.getSelectionModel().getSelected();
                          
                    // 删除数据
                    Ext.Ajax.request({
                        method : Constants.POST,
                        url : 'hr/deletePositionInfo.action',
                        params : {
                            'position.positionId' : record.get('positionId')
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
        if (!positionName.getValue() && positionName.getValue() != '0') {
            msg += String.format(Constants.COM_E_003, "职务名称") + '<br/>';
        }
        if (!positionLevel.getValue() && positionLevel.getValue() != '0') {
            msg += String.format(Constants.COM_E_003, "职务等级") + '<br/>';
        }
        
        if (msg.length > 0) {
            employee.alert(Constants.ERROR, msg.replace(/<br\/>$/, ''));
            return false;
        }
        return true;
    }
    
    // 提交前设置Combo的值，保证其以Number类型传回服务端
    function setComboValue() {
        // 职务等级
        if (!positionName.getValue()) {
            positionName.setValue('');
        }
        // 职务等级
        if (!positionLevel.getValue()) {
            positionLevel.setValue('');
        }
        // 任免职方式
        if (!rmMode.getValue()) {
            rmMode.setValue('');
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
                    var isAddFlag = !positionId.getValue();
                    // 提交前设置Combo的值
                    setComboValue();
                    
                    // 保存数据
                    formPanel.getForm().submit({
                        method : Constants.POST,
                        url : 'hr/savePositionInfo.action',
                        params : {
                            'position.empId' : employee.empId,
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