Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
    Ext.QuickTips.init();
    
    var rootNode = new Ext.tree.AsyncTreeNode({
        text : Constants.POWER_NAME,
        id : '0',
        isRoot : true
    });
    
    var treePanel = new Ext.tree.TreePanel({
        renderTo : "treePanel",
        autoScroll : true,
//        autoHeight : true,
        root : rootNode,
        border : false,
        rootVisible : false,
        loader : new Ext.tree.TreeLoader({
            dataUrl : "hr/getDeptEmpTreeList.action"
        })
    });

    treePanel.on("click", treeClick);
    treePanel.render();
    rootNode.expand();
    
    // 总tabpanel
    var tabPanel = new Ext.TabPanel({
        activeTab : 0,
        tabPosition : 'bottom',
        id:"tabPanel",
        plain : true,
        defaults : {
            autoScroll : true
        },
        frame : false,
        border : false,
        items : [{
            id : 'baseInfo',
            title : '基本信息',
            html : "<iframe name='baseInfo' src='hr/document/empdocument/empmaint/baseInfo/PD011_baseInfo.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        }, {
            id : 'workResume',
            title : '工作简历',
            html : "<iframe name='workResume' src='hr/document/empdocument/empmaint/resume/PD011_workResume.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        }, {
            id : 'studyResume',
            title : '学习简历',
            html : "<iframe name='studyResume' src='hr/document/empdocument/empmaint/resume/PD011_studyResume.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        }, {
            id : 'politics',
            title : '政治面貌',
            html : "<iframe name='politics' src='hr/document/empdocument/empmaint/politics/PD011_politics.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        }, {
            id : 'familyMember',
            title : '家庭成员',
            html : "<iframe name='familyMember' src='hr/document/empdocument/empmaint/family/PD011_familyMember.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        }, {
            id : 'position',
            title : '职务任免',
            html : "<iframe name='position' src='hr/document/empdocument/empmaint/position/PD011_position.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        }, {
            id : 'post',
            title : '技术职称',
            html : "<iframe name='post' src='hr/document/empdocument/empmaint/professionalPost/PD011_post.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        }]
    });

    
    
    
    //-------------add by liuyi 091231 人员列表----------------
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
				name : 'deptId',
				mapping : 3
			}, {
				name : 'deptCode',
				mapping : 4
			}, {
				name : 'deptName',
				mapping : 5
			},{
				name  : 'newEmpCode',
				mapping : 6
			}]);

	var dataProxy = new Ext.data.HttpProxy(
			{
				url:'hr/getEmpListByFilter.action'
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
//分页
	store.load({
			params : {
				start : 0,
				limit : 18		
			}
		});
    var sm = new Ext.grid.CheckboxSelectionModel({
    	singleSelect : true
    });
   var fuzzy = new Ext.form.TextField({
		id : "fuzzy",
		width : 100,
		name : "fuzzy"
	});
	var grid = new Ext.grid.GridPanel({
		store : store,
		columns : [
		sm, 
		{
			header : "员工编码",
			hidden : true,
			sortable : true,
			width : 75,
			dataIndex : 'empCode',
			align:'center'
		}, 
		{
			header : "员工编码",
			sortable : true,
			width : 75,
			dataIndex : 'newEmpCode',
			align:'center'
		}, 
		{
			header : "员工姓名",
			sortable : true,
			width : 80,
			dataIndex : 'chsName',
			align:'center'
		}, 
		{
			header : "部门",
			sortable : true,
			dataIndex : 'deptName',
			align:'center'
		}
		],
		sm : sm,
		tbar : [
		fuzzy, {
			text : "查询",
			 iconCls : 'query',
			handler : queryRecord
		}],	
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "",
			emptyMsg : ""
		})
	});
	grid.on('rowclick',function(Grid,rowIndex,e){
		var rec = store.getAt(rowIndex)
		if(rec.get('empId')){
			employee.loadEmpInfo(rec.get('empId'));
		}
	})
	function queryRecord() {
		var fuzzytext = fuzzy.getValue();
		store.baseParams = {
			fuzzy : fuzzytext
		};
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}
    // 设定布局器及面板
    var layout = new Ext.Viewport({
        layout : "border",
        border : false,
        items : [
            {
                region : 'west',
                layout : 'fit',
                width : 170,
                autoScroll : true,
                containerScroll : true,
                split : true,
                collapsible : true,
//                items : [treePanel]
                items : [new Ext.TabPanel({
							tabPosition : 'bottom',
										activeTab : 1,
										layoutOnTabChange : true,
										items:[{title : '列表方式',
												    border : false,
													autoScroll : true,
													layout : 'border',
													items : [{
													region : 'center',
													layout : 'fit',
													items : [grid]
												}]
												},{
													title : '树形方式',
													border : false,
													autoScroll : true,
													items : [treePanel]
												}]
							})]
           
            },
             {
                region : 'center',
                layout : 'fit',
                border : true,
                frame : false,
                collapsible : false,
                items : [tabPanel]
        }]
    });

    
    
    // 备注
    var taShowMemo = new Ext.form.TextArea({
		id : "taShowMemo",
		maxLength : 250,
    	width : 180,
    	disabled : true
    });
    
    // 弹出画面
	var winMemo = new Ext.Window({
		height : 170,
		width : 350,
		layout : 'fit',
		resizable : false,
		modal  : true,
		closeAction : 'hide',
		items : [taShowMemo],
		buttonAlign : "center",
		title : '详细信息查看窗口',
		buttons : [{
			text : Constants.BTN_CLOSE,
			iconCls : Constants.CLS_CANCEL,
			handler : function() {
				winMemo.hide();
			}
		}]
	});
				
    function treeClick(node, e) {
        if (node.isLeaf()) {
            e.stopEvent();
            employee.loadEmpInfo(node.id);
        }
    }
    
    function Employee() {
        // 员工对象Id
        this.empId = null;
        // 员工名字
        this.chsName = null;
        // 员工部门名
        this.deptName = null;
        // 是否可以编辑
        this.editable = false;
        // 是否已经加载完成
        this.empLoaded = false;
        
        // 加载员工综合信息的监听器
        this._loadEmpInfoHandler = [];
        // 加载员工综合信息前的监听器
        this._beforeLoadEmpInfoHandler = [];
        // 更改员工名字的监听器
        this._changeNameHandler = [];
        
        if (typeof Employee._intialized == 'undefined') {
            Employee._intialized = true;
            
            // 添加加载员工综合信息前的监听器
            Employee.prototype.addBeforeLoadEmpHandler = function(argHandler) {
                if (!argHandler || !(typeof argHandler == 'function')) {
                    return;
                }
                this._beforeLoadEmpInfoHandler.push(argHandler);
            }
            
            // 加载员工综合信息前的事件
            Employee.prototype.beforeLoadEmpInfo = function(argEmpId) {
                // 触发加载员工综合信息前的监听器
                for (var i = 0; i < this._beforeLoadEmpInfoHandler.length; i++) {
                    var fun = this._beforeLoadEmpInfoHandler[i];
//                  alert(fun)
                    // modified by liuyi 091211 先前的监听器如果页面刷新后，未清除，但已释放，若不处理
                    // 会抛出一个 不能执行已释放的脚本 异常 处理不了 注释
//                    var cando = true;// 标记该监听器可以执行 默认可执行
//                    for(var j = i+1;j < this._beforeLoadEmpInfoHandler.length; j++)
//                    {
//						var compFun = this._beforeLoadEmpInfoHandler[j];
//						alert(fun)
//						alert(compFun)
//						alert(fun.toString() == compFun.toString())
//						if (fun == compFun) {
//						if (fun && compFun) {
//							if (fun.toString() == compFun.toString()) {
//								alert('加载前的处理……');
//								cando = false;
//								break;
//							}
//						}}
                    
                    if (fun && typeof fun == 'function') {
                        var callback = fun.call(this, argEmpId);
                        if (callback === false) {
                            return false;
                        }
                    }	
                }
                
                return true;
            }
            
            // 添加加载员工综合信息时的监听器
            Employee.prototype.addLoadEmpHandler = function(argHandler) {
                if (!argHandler || !(typeof argHandler == 'function')) {
                    return;
                }
                this._loadEmpInfoHandler.push(argHandler);
            }
            
            // private: 加载员工综合信息
            Employee.prototype._loadEmp = function(argEmpId) {
            	this.empLoaded = false;
                // 设置员工对象Id
                this.empId = argEmpId;
                
                // 触发加载员工综合信息的监听器
                for (var i = 0; i < this._loadEmpInfoHandler.length; i++) {
                    var fun = this._loadEmpInfoHandler[i];
//                    alert(fun)
//                    alert(this._loadEmpInfoHandler[i])
                    // modified by liuyi 091211 先前的监听器如果页面刷新后，未清除，但已释放，若不处理
                    // 会抛出一个 不能执行已释放的脚本 异常
//                    var cando = true;// 标记该监听器可以执行 默认可执行
//                     for(var j = i+1;j < this._loadEmpInfoHandler.length; j++)
//                    {
//						var compFun = this._loadEmpInfoHandler[j];
//						if (fun.toString() == compFun.toString())
//						{ alert('加载时……');
//						cando = false;
//						break;}
//						
//					}
                   if (fun && typeof fun == 'function') {
							// 加载员工综合信息
							fun.call(this, argEmpId);
						}
                }
                
                // 迁移到员工基本信息Tab页
                this.toTab1();
            }
            
            // 加载员工综合信息
            Employee.prototype.loadEmpInfo = function(argEmpId) {
                // 触发加载员工综合信息前的监听器
                if (this.empLoaded && (this.beforeLoadEmpInfo(argEmpId) === false)) {
                	var scope = this;
                	Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_004,
			            function(buttonobj) {
			                if (buttonobj == "yes") {
				                // 加载员工综合信息
				                scope._loadEmp(argEmpId);
			                }
		                }
	                );
                    return;
                }
                
                // 加载员工综合信息
                this._loadEmp(argEmpId);
            }
            
            // 添加更改员工名字时的监听器
            Employee.prototype.addNameChangeHandler = function(argHandler) {
                if (!argHandler || !(typeof argHandler == 'function')) {
                    return;
                }
                this._changeNameHandler.push(argHandler);
            }
            
            // 更改员工名字
            Employee.prototype.changeName = function(argEmpName) {
            	this.empLoaded = true;
            	if (this.chsName == argEmpName) {
            		return;
            	}
                // 设置员工名字
                this.chsName = argEmpName;
                
                // 触发更改员工名字的监听器
                for (var i = 0; i < this._changeNameHandler.length; i++) {
                    var fun = this._changeNameHandler[i];
                    if (fun && typeof fun == 'function') {
                        // 加载员工综合信息
                        fun.call(this, argEmpName);
                    }
                }
            }
            // add by liuyi 091204 更改员工的部门
            Employee.prototype.changeDeptName = function(argDeptName) {
            	this.empLoaded = true;
            	if (this.deptName == argDeptName) {
            		return;
            	}
                // 设置员工名字
                this.deptName = argDeptName;
                
            }
            
            // 加载员工综合信息
            Employee.prototype.hasEmpId = function() {
                return this.empId || this.empId == '0';
            }
            
            // 打印员工履历表
            Employee.prototype.print = function() {
                window.open("/power/report/webfile/hr/employeeRecord.jsp?empId=" + this.empId);
            }
            
            // 迁移到员工基本信息Tab页
            Employee.prototype.toTab1 = function() {
                tabPanel.setActiveTab('baseInfo');
            }
            
            // 弹出提示信息
            Employee.prototype.alert = function(title, msg, fn, scope) {
                Ext.Msg.alert(title, msg, fn, scope);
            }
            
            // 弹出确认信息
            Employee.prototype.confirm = function(title, msg, fn, scope) {
                Ext.Msg.confirm(title, msg, fn, scope);
            }
            
            // 关闭弹出对话框
            Employee.prototype.closeWin = function(argW, win) {
            	if (!win.rendered || win.hidden) {
		    		return;
		    	}
				
		    	if (tabPanel.getActiveTab().id == argW) {
        			win.hide();
        			return;
        		}
        		
        		win.inValid = true;
        		var f = function() {
        			this.un('activate', f);
    				win.inValid = false;
        			if (win.rendered && !win.hidden) {
			    		win.hide();
			    	}
        		}
        		tabPanel.getItem(argW).on('activate', f);
            }
            
            // 弹出窗口，查看备注
			Employee.prototype.showMemoWin = function(value) {
				winMemo.show();
				taShowMemo.setValue(value);
				winMemo.center();
			}
			
            Employee.prototype.test = function(obj, flag) {
                var str = '';
                for (var prop in obj) {
                    if (flag && typeof obj[prop] == 'function') {
                        continue;
                    }
                    if (obj[prop] instanceof Array) {
                        str += prop + ': [' + obj[prop].join(',') + ']';
                    } else {
                        str += prop + ': ' + obj[prop] + '\n';
                    }
                }
                alert(str);
            }
        }
    }
    var employee = new Employee();
    Ext.getCmp('tabPanel').employee = employee;
    employee.editable = self.location.search.indexOf('flag=1') < 0;
});
