Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
    /** 暂时自己定义的票号 */
    var workticketNo;// = 1;
    /** 暂时自己定义的排序号 */
    var orderBy;// = 1;
    Ext.QuickTips.init();
    // 从母页面获取传过来的工作票号
    workticketNo = getParameter("workticketNo");
    // ↓↓**********************作业方式选择********************↓↓//
    // 动火票号文本框
    var ticketNo = new Ext.form.Label({
                text : workticketNo
            })
    // ↑↑**********************角色选择********************↑↑//
    var fireContent = Ext.data.Record.create([{
                // 作业方式id
                name : 'firecontentId'
            }, {
                // 作业方式名称
                name : 'firecontentName'
            }]);

    /* 设置每一行的选择框 */
    var fire_sm = new Ext.grid.CheckboxSelectionModel({
                singleSelect : false
            });
    // grid列模式
    var fire_cm = new Ext.grid.ColumnModel([fire_sm, {
                header : '编号',
                width:50,
                dataIndex : 'firecontentId',
                align : 'left'
            }, {
                header : '作业方式',
                dataIndex : 'firecontentName',
                align : 'left'
            }]);
    fire_cm.defaultSortable = true;
    // ↓↓**********************已选用户Grid************↓↓//
    // 数据源
    var fire_ds = new Ext.data.Store({
                proxy : new Ext.data.HttpProxy({
                            url : 'workticket/getContentFireContentList.action'
                        }),
                reader : new Ext.data.JsonReader({
                            totalProperty : 'totalCount',
                            root : 'list'
                        }, fireContent)
            });
    fire_ds.setDefaultSort('firecontentId','asc');
    // grid
    var fireGrid = new Ext.grid.GridPanel({
		enableColumnMove : false,
		enableDragDrop : false,
		enableColumnHide : true,
        frame : true,
        ds : fire_ds,
        cm : fire_cm,
        sm : fire_sm,
//        width : 380,
        border : false, 
        viewConfig : {
            forceFit : true
        }
        });
	fire_ds.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			});
    // ↑↑**********************已选用户Grid************↑↑//
    // ↓↓**********************未选用户Grid************↓↓//
    var TicketContent = Ext.data.Record.create([{
                // 已选的作业方式序列号
                name : 'fire.id'
            }, {
                // 作业方式id
                name : 'fire.firecontentId'
            }, {
                // 作业方式名字
                name : 'firecontentName'
            }]);

    // 选择模式
    var ticketContent_sm = new Ext.grid.CheckboxSelectionModel({
                singleSelect : false
            });
    var ticketContent_cm = new Ext.grid.ColumnModel([ticketContent_sm, {
                header : '编号',
                dataIndex : 'fire.firecontentId',
                width:50,
                align : 'left',
                enableDragDrop : false,
                enableColumnHide : false
            }, {
                enableDragDrop : false,
                enableColumnHide : false,
                header : '作业方式',
                dataIndex : 'firecontentName',
                align : 'left'
            }]);
    ticketContent_cm.defaultSortable = true;
    // 数据源
    var ticketContent_ds = new Ext.data.Store({
                proxy : new Ext.data.HttpProxy({
                            url : 'workticket/getContentTicketFireContentList.action'
                        }),
                reader : new Ext.data.JsonReader({
                            totalProperty : 'totalCount',
                            root : 'list'
                        }, TicketContent)
            });
    ticketContent_ds.setDefaultSort('fire.firecontentId', 'asc');
    ticketContent_ds.baseParams = {
		// 工作票号
		ticketContent_ds : workticketNo
	};
    // 分页工具栏
	var pagebar2 = new Ext.PagingToolbar({
					pageSize : Constants.PAGE_SIZE,
					store : ticketContent_ds,
					displayInfo : true,
					displayMsg : "共{2}条",
					emptyMsg : Constants.EMPTY_MSG
				})
    // 待选作业方式grid
    var ticketContentGrid = new Ext.grid.GridPanel({
				enableColumnMove : false,
				enableDragDrop : false,
				enableColumnHide : true,
                ds : ticketContent_ds,
                cm : ticketContent_cm,
                sm : ticketContent_sm,
                bbar : pagebar2,
//                width : 380,
                border : false,
                frame : true,
                autoScroll : true,
                viewConfig : {
                    forceFit : true
                }
            });
    ticketContent_ds.baseParams = {
    				// 工作票号
    				workticketNo : workticketNo
				};
    ticketContent_ds.load({
        params : {
            start : 0,
			limit : Constants.PAGE_SIZE
        }
    });
    // ↑↑**********************未选作业方式Grid************↑↑//

    // ↓↓**********************中间按钮************↓↓//
    // 添加成员button
    var grantBar = new Ext.Button({
        id : 'btnGrant',
        height : 80,
        text : " >>>  ",
        layout : 'form',
        width: 40,
        handler : function() {
            var selectedRows = fireGrid.getSelectionModel().getSelections();
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
                ids += selectedRows[i].data.firecontentId + ",";
                names += selectedRows[i].data.firecontentName + ",";
            }
            if (ids.length > 0) {
                ids = ids.substring(0, ids.length - 1);
                names = names.substring(0, names.length - 1);
                // 检查所选数据是否已经添加过
                for (i = 0; i < selectedRows.length; i++) {
                    var wr = selectedRows[i];
                    // 遍历已添加的方式
                    for (j = 0; j < ticketContent_ds.data.items.length; j++) {
                        // 已经添加过
                        if (wr.data.firecontentId == ticketContent_ds.getAt(j)
                                .get("fire.firecontentId")) {
                            added = true;
                            break;
                        }
                    }
                    // 未添加的放入addnames和addids
                    if (added == false) {
                        addnames += wr.data.firecontentName + ",";
                        addids += wr.data.firecontentId + ",";
                    } else {
                        // 已添加的放入addednames和addedids
                        addednames += wr.data.firecontentName + ",";
                        addedids += wr.data.firecontentId + ",";
                    }
                    added = false;
                }
                addednames = addednames.substring(0, addednames.length - 1);
                ids = addids.substring(0, addids.length - 1);
                names = addnames.substring(0, addnames.length - 1);
                // 所选作业方式中有尚未添加的作业方式
                if (ids.length > 0) {
                    Ext.Ajax.request({
                                url : 'workticket/addContentFireContent.action',
                                method : Constants.POST,
                                params : {
                                    // 添加的记录的ids
                                    ids : ids,
                                    // 添加的记录的排序号
                                    orderBy : orderBy,
                                    // 添加的记录的工作票号
                                    workticketNo : workticketNo
                                },
                                success : function(result, request) {
                                    // 成功，显示添加成功信息和员工名字
                                    var o = eval("(" + result.responseText
                                            + ")");
                                    // 如果选中的作业方式中有已添加的作业方式
                                    if (addednames.length > 0) {
//                                        Ext.Msg.alert(Constants.NOTICE, o.msg
//                                                        + names
//                                                        + "<br>以下作业方式已经添加过："
//                                                        + addednames);
                                        // 如果没有，则只显示添加成功的作业方式名称
                                    } else {
//                                        Ext.Msg.alert(Constants.NOTICE, o.msg);
                                    }
                                    // 更新已选的作业方式信息
                                    ticketContent_ds.load({
                                    	params : {
                                    		workticketNo : workticketNo,
                                    		start : 0,
											limit : Constants.PAGE_SIZE
                                    	}
                                    });
                                },
                                // 添加失败
                                failure : function() {
                                    Ext.Msg.alert(Constants.ERROR,
                                            Constants.UNKNOWN_ERR);
                                }
                            });
                    // 所选作业方式中没有未添加的作业方式
                } else {
                    if (addednames.length > 0) {
//                        Ext.Msg.alert("提示", "以下作业方式已经添加过！" + addednames);
                    }
                }
            } else {
                Ext.MessageBox.alert('提示', '请从左边选择作业方式!');
            }
        }
    });
    // 去除成员button
    var revokeBar = new Ext.Button({
        id : 'btnRevoke',
        text : " <<<  ",
        style : {
            'margin-top' : '30'
        },
        handler : function() {
            var selectedRows = ticketContentGrid.getSelectionModel()
                    .getSelections();
            var ids = "";
            var id = "";
            for (i = 0; i < selectedRows.length; i++) {
                ids += selectedRows[i].get("fire.id") + ",";
            }
            if (ids.length > 0) {
                Ext.Msg.confirm(Constants.BTN_DELETE, Constants.DelMsg,
                        function(buttonobj) {
                            if (buttonobj == "yes") {
                                // 批量删除
                                if (selectedRows.length > 1) {
                                    ids = ids.substring(0, ids.length - 1);
                                    Ext.Ajax.request({
                                        url : 'workticket/deleteContentFireContent.action',
                                        method : 'post',
                                        params : {
                                            ids : ids
                                        },
                                        waitMsg : '正在处理...',
                                        success : function(result, request) {
                                            // 成功，显示添加成功信息和员工名字
                                            var o = eval("("
                                                    + result.responseText + ")");
//                                            Ext.Msg.alert(Constants.NOTICE,
//                                                    o.msg);
                                            ticketContent_ds.load({
		                                    	params : {
		                                    		workticketNo : workticketNo,
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
                                        url : 'workticket/deleteContentFireContent.action',
                                        method : 'post',
                                        params : {
                                            id : id
                                        },
                                        waitMsg : '正在处理...',
                                        success : function(result, request) {
                                            // 成功，显示添加成功信息和员工名字
                                            var o = eval("("
                                                    + result.responseText + ")");
//                                            Ext.Msg.alert(Constants.NOTICE,
//                                                    o.msg);
                                            ticketContent_ds.load({
		                                    	params : {
		                                    		workticketNo : workticketNo,
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
                Ext.MessageBox.alert('提示', '请从右边选择作业方式!');
            }
        }
    });
    // ↑↑**********************中间按钮************↑↑//
    // 确定按钮
    var btnSubmit = new Ext.Button({
                style : {
                    'margin-left' :'60%'
//                    'margin-bottom' : 10
                },
    	 
                text : "确定",
                handler : function() {
			        // 记录总条数
			    	var pageTotal = 0;
			    	// 获得记录总条数
			    	var myurl = "workticket/getFireRecordTotal.action";
			        Ext.Ajax.request({
			                    method : 'POST',
			                    url : myurl,
			                    params : {
			                        workticketNo : workticketNo
			                    },
			                    success : function(result, request) {
			                    	// 成功，读出所有记录并返回到上个页面
			                    	pageTotal = result.responseText;
							    	ticketContent_ds.load({
							    		params : {
													start : 0,
													limit : pageTotal
												},
										callback : function(){
						                    var contents = "";
						                    // 取出已选作业方式的名称
						                    for (j = 0; j < ticketContent_ds.data.items.length; j++) {
						                        contents += ticketContent_ds.getAt(j)
						                                .get("firecontentName")
						                                + ",";
						                    }
						                    contents = contents.substring(0, contents.length - 1);
						                    var selectedContent = new Object();
						                    // 把已选作业方式放入返回值中返回到父页面
						                    selectedContent.names = contents;
						                    window.returnValue = selectedContent;
						                    // 关闭弹出窗口
						                    window.close();
										}
				               		 })
			                    },
			                    failure : function() {
			                        Ext.Msg.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
			                    }
			                });
			    	
            }
    });
    var atbar = new Ext.Toolbar({
                items : ['<font color="blue">动火票号为：</font>', ticketNo,btnSubmit]
            });
    var top = new Ext.Panel({
                region : 'north',
                border : false,
                items : [atbar]
            })
    var left = new Ext.Panel({
                region : 'west',
                layout : 'fit',
                border : true,
                width : 250,
                collapsible : true,
                items : [fireGrid]
            });
    var intop = new Ext.Panel({
                region : 'north',
                layout : 'fit',
                items : "",
                height : 180,
                border : false
            })
    var incenter = new Ext.Panel({
                region : 'center',
                layout : 'form',
                height : 50,
                items : [grantBar, revokeBar],
                border : false
            })
    var inwest = new Ext.Panel({
                region : 'west',
                layout : 'form',
                width : 8,
                items : "",
                border : false
            })
    var mid = new Ext.Panel({
                layout : "border",
                region : "center",
                items : [intop, inwest, incenter]
            });
    var right = new Ext.Panel({
                region : "east",
                layout : 'fit',
                width : 250,
                collapsible : true,
                border : false,
                items : [ticketContentGrid]
            });
//    var bottom = new Ext.Panel({
//                layout : "border",
//                region : "south",
//                height : 20,
//                items : [btnSubmit]
//            });
    new Ext.Viewport({
                enableTabScroll : true,
                layout : "border",
                items : [top, left, mid, right]
            });
});
