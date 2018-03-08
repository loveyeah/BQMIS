/**
 * 选择工作班成员
 * 
 * @author 黄维杰
 * @version 1.0
 */
// 运行需知：需要包含 <script type="text/javascript"
// src="comm/ext/ComboBoxTree.js"></script>
Ext.onReady(function() {
    /** 临时员工常量 = 2 */
    var FLOATER = 2;
    /** 正式员工常量 = 1 */
    var NORMAL = 1;
    /** 暂时自己定义的票 */
    var workticketNo;// = 1;
    // 获取母页面传过来的工作票号
    workticketNo = getParameter("workticketNo");
    // 部门选择树下拉框
    var comboDepChoose = new Ext.ux.ComboBoxTree({
                labelwidth : 50,
                fieldLabel : '另选部门',
                id : 'deptId',
                displayField : 'text',
                width : 220,
                valueField : 'id',
                hiddenName : 'empinfo.deptId',
                blankText : '请选择',
                emptyText : '请选择',
                readOnly : true,
                tree : {
                    xtype : 'treepanel',
                    loader : new Ext.tree.TreeLoader({
                                dataUrl : 'empInfoManage.action?method=getDep'
                            }),
                    root : new Ext.tree.AsyncTreeNode({
                                id : '-1',
                                name : '合肥电厂',
                                text : '合肥电厂'
                            })
                },
                selectNodeModel : 'all',
                // 监听combobox的collapse事件，选中某个部门后
                // 查询数据库中该部门的员工，更新可选员工grid
                listeners : {
                    collapse : function() {
                        Ext.Msg.wait("请等候", "加载中", "操作进行中...");
                        // 通过部门号查询数据库
                        staff_ds.load({
                                    params : {
                                        start : 0,
										limit : Constants.PAGE_SIZE,
										depId : comboDepChoose.value
                                    }
                                });
                        setTimeout(function() {
                                    Ext.Msg.hide();
                                }, 250);
                    }
                }
            })
    // 临时工选择框
    var chkFloater = new Ext.form.Checkbox({
                id : chkFloater,
                fieldLabel : "临时工选择",
                listeners : {
                    check : function() {
                        // 选中状态
                        if (chkFloater.checked) {
                            txtFloater.show();
                            btnJoinIn.show();
                            grantBar.disable();
                            comboDepChoose.disable();
                            // 未选中状态
                        } else {
                            txtFloater.hide();
                            btnJoinIn.hide();
                            grantBar.enable();
                            comboDepChoose.enable();
                        }
                    }
                }
            })
    // 临时工名字输入框
    var txtFloater = new Ext.form.TextField({
                id : txtFloater,
                allowBlank : false,
                blankText : "请输入临时工姓名",
                maxLength : 30
            })
    // 加入按钮
    var btnJoinIn = new Ext.Button({
                text : "加入",
                handler : addFloater
            })
    // 确定按钮
    var btnConfirm = new Ext.Button({
                text : "确定",
                handler : onSubmit
            })
    // 包含部门选择和是否临时工的头部
    var headPanel = new Ext.FormPanel({
                height : "100%",
                labelAlign : 'right',
                items : [new Ext.form.FieldSet({
                            autoHeight : true,
                            anchor : '100%',
                            items : [{
                                        layout : "column",
                                        border : false,
                                        items : [{
                                                    columnWidth : 0.5,
                                                    layout : "form",
                                                    border : false,
                                                    items : [comboDepChoose]
                                                }, {
                                                    columnWidth : 0.2,
                                                    layout : "form",
                                                    border : false,
                                                    items : [chkFloater]
                                                }, {
                                                    columnWidth : 0.1,
                                                    border : false,
                                                    items : [txtFloater]
                                                }, {
                                                    columnWidth : 0.1,
                                                    layout : "form",
                                                    border : false,
                                                    items : [btnJoinIn]
                                                }, {
                                                    columnWidth : 0.1,
                                                    layout : "form",
                                                    border : false,
                                                    items : [btnConfirm]
                                                }]
                                    }]
                        })

                ]
            });
    // 初始化时把 临时工姓名输入框 和 “加入”按钮隐藏
    txtFloater.hide();
    btnJoinIn.hide();
    /** 两个grid的panel */
    // 可选成员Grid
    // 用户记录格式
    var Staff = Ext.data.Record.create([{
                // 员工号
                name : 'empCode'
            }, {
                // 员工名字
                name : 'chsName'
            }]);
    // 设置每一行的选择框
    var from_sm = new Ext.grid.CheckboxSelectionModel({
                singleSelect : false
            });
    // grid列模式
    var from_cm = new Ext.grid.ColumnModel([from_sm,
            new Ext.grid.RowNumberer({
            	header : '行号',
    			sortable : true,
    			width : 40,
    			align : 'left'
            }), {
                header : '工号',
                dataIndex : 'empCode',
                align : 'left',
                sortable : true,
                width : 175
            }, {
                header : '姓名',
                dataIndex : 'chsName',
                align : 'left',
                sortable : true,
                width : 175
            }]);
    from_cm.defaultSortable = true;
    // ↓↓**********************可选成员Grid************↓↓//
    // 数据源
    var staff_ds = new Ext.data.Store({
                proxy : new Ext.data.HttpProxy({
                            url : 'workticket/getContentStaffList.action'
                        }),
                reader : new Ext.data.JsonReader({
                            totalProperty : 'totalCount',
                            root : 'list'
                        }, Staff)
            });
    staff_ds.setDefaultSort('empCode','asc');
    // 分页工具栏
	var pagebar1 = new Ext.PagingToolbar({
					pageSize : Constants.PAGE_SIZE,
					store : staff_ds,
					displayInfo : true,
					displayMsg : "共{2}条",
					emptyMsg : Constants.EMPTY_MSG
				})
    // grid
    var fromGrid = new Ext.grid.GridPanel({
                enableColumnMove : false,
                enableColumnHide : true,
                enableDragDrop : false,
                height : 420,
                ds : staff_ds,
                cm : from_cm,
                sm : from_sm,
                bbar : pagebar1,
                width : 350,
                border : true,
                frame : true,
                viewConfig : {
		            forceFit : true
		        }
            });
    // ↑↑**********************可选成员Grid************↑↑//
    
    // 已选成员Grid
    // 用户记录格式
    var Actor = Ext.data.Record.create([{
                // 成员序列id
                name : 'actor.id'
            }, {
                // 成员员工号（部门中的）
                name : 'actor.actorCode'
            }, {
                // 成员名字
                name : 'actor.actorName'
            }, {
                // 部门名字（如果有）
                name : 'deptName'
            }, {
                // 成员类型（1.正式员工，2.临时员工）
                name : 'actortypename'
            }]);
    // 设置每一行的选择框 
    var to_sm = new Ext.grid.CheckboxSelectionModel({
                singleSelect : false
            });
    // grid列模式
    var to_cm = new Ext.grid.ColumnModel([to_sm, 
    	new Ext.grid.RowNumberer(
    		{
    			header : '行号',
    			align : 'left',
    			sortable : true,
    			width : 40
    		}), {
                header : '工号',
                dataIndex : 'actor.actorCode',
                align : 'left',
    			sortable : true
            }, {
                header : '姓名',
                dataIndex : 'actor.actorName',
                align : 'left',
    			sortable : true
            }, {
                header : '所属部门',
                dataIndex : 'deptName',
                align : 'left',
    			sortable : true
            }, {
                header : '人员类别',
                dataIndex : 'actortypename',
                align : 'left',
    			sortable : true
            }]);
    to_cm.defaultSortable = true;
    // ↓↓**********************已选成员Grid************↓↓//
    // 数据源
    var actor_ds = new Ext.data.Store({
                proxy : new Ext.data.HttpProxy({
                            url : 'workticket/getContentActorList.action'
                        }),
                reader : new Ext.data.JsonReader({
                            totalProperty : 'totalCount',
                            root : 'list'
                        }, Actor)
            });
    actor_ds.setDefaultSort('actor.actorName','asc');
    // 分页工具栏
	var pagebar = new Ext.PagingToolbar({
					pageSize : Constants.PAGE_SIZE,
					store : actor_ds,
					displayInfo : true,
					displayMsg : "共{2}条",
					emptyMsg : Constants.EMPTY_MSG
				})
    // grid
    var toGrid = new Ext.grid.GridPanel({
				enableColumnMove : false,
				enableDragDrop : false,
				enableColumnHide : true,
				height : 400,
				ds : actor_ds,
				cm : to_cm,
				sm : to_sm,
				bbar : pagebar,
				width : 380,
				border : true,
				frame : true,
				viewConfig : {
		            forceFit : true
		        }
			});
	actor_ds.baseParams = {
		// 工作票号
		workticketNo : workticketNo
	};
	actor_ds.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			});
    // ↓↓**********************中间button************↓↓//
    // 添加成员button
    var grantBar = new Ext.Button({
        id : 'btnGrant',
        text : " >>>  ",
        style : {
            'margin-top' : 220,
            'margin-left' : 10
        },
        align : 'center',
        buttonAlign : 'center',
        handler : function() {
            var selectedRows = fromGrid.getSelectionModel().getSelections();
            // 选中的
            var ids = "";
            var names = "";
            // 已添加的
            var addednames = "";
            var addedids = "";
            // 未添加的
            var addnames = "";
            var addids = "";
            // 添加标志
            var added = false;
            for (i = 0; i < selectedRows.length; i++) {
                ids += selectedRows[i].data.empId + ",";
                names += selectedRows[i].data.chsName + ",";
            }
            if (ids.length > 0) {
                ids = ids.substring(0, ids.length - 1);
                names = names.substring(0, names.length - 1);
                // 检查所选数据是否已经添加过
                for (i = 0; i < selectedRows.length; i++) {
                    var wr = selectedRows[i];
                    // 遍历已添加的员工
                    for (j = 0; j < actor_ds.data.items.length; j++) {
                        // 已经添加过
                        if (wr.data.empCode == actor_ds.getAt(j)
                                .get("actor.actorCode")) {
                            added = true;
                            break;
                        }
                    }
                    // 未添加的放入addnames和addids
                    if (added == false) {
                        addnames += wr.data.chsName + ",";
                        addids += wr.data.empCode + ",";
                    } else {
                        // 已添加的放入addednames和addedids
                        addednames += wr.data.chsName + ",";
                        addedids += wr.data.empCode + ",";
                    }
                    added = false;
                }
                addednames = addednames.substring(0, addednames.length - 1);
                ids = addids.substring(0, addids.length - 1);
                names = addnames.substring(0, addnames.length - 1);
                // 所选员工中有尚未添加的员工
                if (ids.length > 0) {
                    Ext.Ajax.request({
                                url : 'workticket/addContentNormalActor.action',
                                method : Constants.POST,
                                params : {
                                    // 把员工信息保存到工作班成员表
                                    ids : ids,
                                    names : names,
                                    actorDept : comboDepChoose.getValue(),//Ext.get("comboDepChoose").dom.value,
                                    actorType : NORMAL,
                                    workticketNo : workticketNo
                                },
                                success : function(result, request) {
                                    // 成功，显示添加成功信息和员工名字
                                    var o = eval("(" + result.responseText
                                            + ")");
                                    // 如果选中的员工中有已添加的员工，则加在后面
                                    if (addednames.length > 0) {
//                                        Ext.Msg.alert(Constants.NOTICE, o.msg
//                                                        + "<br>以下员工已经添加过："
//                                                        + addednames);
                                        // 如果没有则只显示添加成功的员工信息
                                    } else {
//                                        Ext.Msg.alert(Constants.NOTICE, o.msg);
                                    }
                                    // 更新工作班成员信息
                                    actor_ds.load({
												params : {
													start : 0,
													limit : Constants.PAGE_SIZE
												}
											});
                                },
                                // 失败
                                failure : function() {
                                    Ext.Msg.alert(Constants.ERROR,
                                            Constants.UNKNOWN_ERR);
                                }
                            });
                    // 所选员工中没有未添加的员工
                } else {
                    if (addednames.length > 0) {
//                        Ext.Msg.alert("提示", "以下员工已经添加过！" + addednames);
                    }
                }
            } else {
                Ext.MessageBox.alert('提示', '请从左边选择人员!');
            }
        }

    });
    // 去除成员button
    var revokeBar = new Ext.Button({
        id : 'btnRevoke',
        text : " <<<  ",
        style : {
            'margin-top' : 10,
            'margin-left' : 10
        }, 
        align : 'center',
        buttonAlign : 'center',
        handler : function() {
            // 得到选中项
            var selectedRows = toGrid.getSelectionModel().getSelections();
            var ids = "";
            var id = "";
            for (i = 0; i < selectedRows.length; i++) {
                ids += selectedRows[i].get("actor.id") + ",";
            }
            Ext.Msg.alert("", selectedRows.length);
            // 如果有选中项
            if (ids.length > 0) {
                // 弹出确认删除的对话框，确定删除
                Ext.Msg.confirm(Constants.BTN_DELETE, Constants.DelMsg,
                        function(buttonobj) {
                            if (buttonobj == "yes") {
                                // 批量删除
                                if (selectedRows.length > 1) {
                                    ids = ids.substring(0, ids.length - 1);
                                    Ext.Ajax.request({
                                        url : 'workticket/deleteContentActor.action',
                                        method : 'post',
                                        params : {
                                            ids : ids
                                        },
                                        waitMsg : '正在处理...',
                                        success : function(result, request) {
                                            var o = eval("("
                                                    + result.responseText + ")");
//                                            Ext.Msg.alert(Constants.NOTICE,
//                                                    o.msg);
                                           actor_ds.load({
														params : {
															start : 0,
															limit : Constants.PAGE_SIZE
														}
													});
                                        },
                                        failure : function() {
                                            Ext.Msg.alert(Constants.ERROR,
                                                    Constants.UNKNOWN_ERR);
                                        }
                                    });
                                    // 单个删除
                                } else {
                                    id = ids.substring(0, ids.length - 1);
                                    ids = null;
                                    Ext.Ajax.request({
                                        url : 'workticket/deleteContentActor.action',
                                        method : 'post',
                                        params : {
                                            id : id
                                        },
                                        waitMsg : '正在处理...',
                                        success : function(result, request) {
                                            var o = eval("("
                                                    + result.responseText + ")");
//                                            Ext.Msg.alert(Constants.NOTICE,
//                                                    o.msg);
                                            actor_ds.load({
														params : {
															start : 0,
															limit : Constants.PAGE_SIZE
														}
													});
                                        },
                                        failure : function() {
                                            Ext.Msg.alert(Constants.ERROR,
                                                    Constants.UNKNOWN_ERR);
                                        }
                                    })
                                }
                            }
                        });
            } else {
                Ext.MessageBox.alert('提示', '请从右边选择人员!');
            }
        }
    });
    // ↑↑**********************中间button************↑↑//
    /** 大体布局 */
    var top = new Ext.Panel({
                region : 'north',
                border : false,
                items : headPanel,
                height : 30
            })
    var left = new Ext.Panel({
                region : 'west',
                layout : 'fit',
                border : true,
                width : 300,
                collapsible : true,
                // autoScroll:true,
                items : [fromGrid]
            });
    var mid = new Ext.Panel({
                layout : "form",
                region : "center",
                items : [grantBar, revokeBar]
            });
    var right = new Ext.Panel({
                region : "east",
                layout : 'fit',
                width : 330,
                collapsible : true,
                border : false,
                items : [toGrid]
            });
    new Ext.Viewport({
                enableTabScroll : true,
                border : true,
                layout : "border",
                items : [top, left, mid, right]
            });
    /** 处理函数 */
    /**
     * 添加临时员工
     */
    function addFloater() {
    	if (txtFloater.getValue() == "") {
    		Ext.Msg.alert(Constants.NOTICE, "请输入临时员工的名字");
    		return;
    	}
        var myurl = "workticket/addContentFloaterActor.action";
        Ext.Ajax.request({
                    method : 'POST',
                    url : myurl,
                    params : {
                        actorType : FLOATER,
                        workticketNo : workticketNo,
                        actorName : txtFloater.getValue()
                    },
                    success : function(result, request) {
                    	// 显示添加成功信息
                        var o = eval("(" + result.responseText + ")");
//                        Ext.Msg.alert(Constants.NOTICE, o.msg);
                        // store刷新
                       actor_ds.load({
									params : {
										start : 0,
										limit : Constants.PAGE_SIZE
									}
								});
                    },
                    failure : function() {
                        Ext.Msg.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
                    }
                });
    }

    /**
     * 提交数据到工作票内容填写页面
     */
    function onSubmit() {
    	// 记录总条数
    	var pageTotal = 0;
    	// 获得记录总条数
    	var myurl = "workticket/getRecordTotal.action";
        Ext.Ajax.request({
                    method : 'POST',
                    url : myurl,
                    params : {
                        workticketNo : workticketNo
                    },
                    success : function(result, request) {
                    	// 成功，把数据全部读出，返回到上个页面
                    	pageTotal = result.responseText;
				    	actor_ds.load({
				    		params : {
										start : 0,
										limit : pageTotal
									},
							callback : function(){
					    	var actors = "";
					        var total = actor_ds.data.items.length;
					        for (j = 0; j < actor_ds.data.items.length; j++) {
					            actors += actor_ds.getAt(j).get("actor.actorName") + ",";
					        }
					        actors = actors.substring(0, actors.length - 1);
					        var selectedMembers = new Object();
					        // 成员名字
					        selectedMembers.names = actors;
					        // 成员数量
					        selectedMembers.quantity = total + 1;
					        window.returnValue = selectedMembers;
					        window.close();
					        }
					    });
                    },
                    failure : function() {
                        Ext.Msg.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
                    }
                });
    }
});