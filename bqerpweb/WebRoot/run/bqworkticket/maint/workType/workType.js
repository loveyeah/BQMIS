Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	// 定义grid
	var MyRecord = Ext.data.Record.create([{
				name : 'worktypeId'
			}, {
				name : 'worktypeName'
			}, {
				name : 'orderBy'
			}, {
				name : 'workticketTypeCode'
			}]);
	// 定义获取数据源
	var dataProxy = new Ext.data.HttpProxy({
				url : 'workticket/getWorkType.action'
			});

	// 定义格式化数据
	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, MyRecord);

	// 定义封装缓存数据的对象
	var store = new Ext.data.Store({

		proxy : dataProxy,// 访问的对象

		reader : theReader
			// 处理数据的对象

		});
	// 通过reader处理proxy得到的数据，因为时异步加载，
	// 所以需要用回调函数或者load()方法显示数据
	store.setDefaultSort('worktypeName', 'asc');

	// 定义查询参数
	var fuuzy = new Ext.form.TextField({
				id : "fuzzy",
				name : "fuzzy"
			});
	// 定义checkbox
	var sm = new Ext.grid.CheckboxSelectionModel();
	// 定义workticketTypeCode combo 数据源
	var storeCbx = new Ext.data.JsonStore({
				root : 'list',
				url : "workticket/getWorkTypeListAll.action",
				fields : ['workticketTypeCode', 'workticketTypeName']
			})
	storeCbx.load();
	// 定义workticketTypeCode combo
	var ticketComboBox = new Ext.form.ComboBox({
				id : "wordType",
				store : storeCbx,
				displayField : "workticketTypeName",
				valueField : "workticketTypeCode",
				mode : 'local',
				triggerAction : 'all',
				allowBlank : false,
				readOnly : true
			});

	// 通过store的装载初始化所属系统下拉框的默认选项为store的第一项
	storeCbx.on("load", function(e, records, o) {
				ticketComboBox.setValue(records[0].data.workticketTypeCode);
				store.load({
							params : {
								start : 0,
								limit : Constants.PAGE_SIZE,
								fuzzy : '',
								workticketTypeCode : ticketComboBox.getValue()
							}
						});
			});

	store.baseParams = {
		fuzzy : fuuzy.getValue(),
		workticketTypeCode : ticketComboBox.getValue()
	};
	store.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			}

	);
	// --gridpanel显示格式定义-----开始-------------------
	var grid = new Ext.grid.GridPanel({
				region : "center",
				layout : 'fit',
				store : store,
				columns : [sm, {
							header : "ID",
							dataIndex : 'worktypeId',
							hidden : true

						}, {
							header : "工作类型",
							width : 40,
							sortable : true,
							dataIndex : 'worktypeName'
						}, {
							header : "显示顺序",
							width : 0,
							sortable : true,
							dataIndex : 'orderBy'
						}],
				sm : sm,
				autoSizeColumns : true,
				viewConfig : {
					forceFit : true
					// 还会对超出的部分进行缩减，让每一列的尺寸适应GRID的宽度大小，阻止水平滚动条的出现
				},

				// 头部工具栏
				tbar : ['工作票类型:', ticketComboBox,'&nbsp', '工作类型名称:', fuuzy, {
							text : Constants.BTN_QUERY,
							iconCls : Constants.CLS_QUERY,
							handler : queryRecord
						}, {
							text : Constants.BTN_ADD,
							iconCls : Constants.CLS_ADD,
							handler : addRecord
						}, {
							text : Constants.BTN_UPDATE,
							iconCls : Constants.CLS_UPDATE,
							handler : updateRecord
						}, {
							text : Constants.BTN_DELETE,
							iconCls : Constants.CLS_DELETE,
							handler : deleteRecord
						}],
				// 底部工具栏
				bbar : new Ext.PagingToolbar({
							pageSize : Constants.PAGE_SIZE,
							store : store,
							displayInfo : true,
							displayMsg : Constants.DISPLAY_MSG,
							emptyMsg : Constants.EMPTY_MSG
						}),
				enableColumnMove : false
			});
	// --gridpanel显示格式定义-----结束--------------------

	// 注册双击事件
	grid.on("rowdblclick", updateRecord);

	// 页面加载显示数据
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				items : [grid]
			});

	var wd = 180;
	// 设定隐藏域
	var flag = {
		id : "flag",
		xtype : "hidden",
		value : "",
		name : "flag"
	}
	// 定义form中的关键字
	var typeName = {
		id : "worktypeName",// 通过id可与定义的grid同名数据项关联
		xtype : "textfield",
		fieldLabel : "名称<font color='red'>*</font>",
		allowBlank : false,
		width : wd,
		maxLength : 100,
		name : 'bean.worktypeName'// 发送给后台的参数名

	}
	// 定义form中的显示顺序
	var orderBy = new Ext.form.NumberField({
				id : "orderBy",
				fieldLabel : '显示顺序',
				allowBlank : true,
				width : wd,
				name : 'bean.orderBy',
				maxLength : 10
			})
	// 定义form中的工作类型ID，隐藏字段
	var typeId = {
		id : "worktypeId",
		xtype : "hidden",
		name : "bean.worktypeId"

	}
	// 定义form中的工作类型，隐藏字段
	var workticketTypeCode = {
		id : "workticketTypeCode",
		xtype : "hidden",
		name : "bean.workticketTypeCode"
	}

	// 定义弹出窗体中的form
	var mypanel = new Ext.FormPanel({
				labelAlign : 'right',
				autoHeight : true,
				frame : true,
				items : [typeName, orderBy, typeId, workticketTypeCode, flag]

			});

	// 定义弹出窗体
	var win = new Ext.Window({
				width : 350,
				autoHeight : 90,
				title : "工作类型维护",
				buttonAlign : "center",
				items : [mypanel],
				layout : 'fit',
				closeAction : 'hide',
				resizable : false,
                modal:true,
				buttons : [{
					text : Constants.BTN_SAVE,
					iconCls : Constants.CLS_SAVE,
					handler : function() {
						var myurl = "";
						if (Ext.get("flag").dom.value == "0") {
							myurl = "workticket/addWorkType.action";
							Ext.get("workticketTypeCode").dom.value = ticketComboBox
									.getValue();
						} else {
							myurl = "workticket/updateWorkType.action";
						}
						mypanel.getForm().submit({
							method : 'POST',
							url : myurl,
							success : function(form, action) {

								var o = eval("(" + action.response.responseText
										+ ")");
								Ext.Msg.alert(Constants.NOTICE, o.msg);
								store.baseParams = {
									fuzzy : fuuzy.getValue(),
									workticketTypeCode : ticketComboBox
											.getValue()
								};
								store.load({
											params : {
												start : 0,
												limit : Constants.PAGE_SIZE
											}
										});
								win.hide();
							},
							faliue : function() {
								Ext.Msg.alert(Constants.ERROR,
										Constants.UNKNOWN_ERR);
							}
						});
					}
				}, {
					text : Constants.BTN_CANCEL,
					iconCls : Constants.CLS_CANCEL,
					handler : function() {
						win.hide();
					}
				}]
			});

	// 查询函数
	function queryRecord() {
		store.baseParams = {
			fuzzy : fuuzy.getValue(),
			workticketTypeCode : ticketComboBox.getValue()
		};
		store.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});
	}
	// 增加函数
	function addRecord() {
        if(ticketComboBox.getValue()!=""){
            
		mypanel.getForm().reset();
		win.show();
		Ext.get("flag").dom.value = "0";
        }else{
            Ext.Msg.alert(Constants.SYS_REMIND_MSG,"请先选择一种工作票类型！")        
        }

	}
	// 更新函数
	function updateRecord() {
		if (grid.selModel.hasSelection()) {// 是否有被选项

			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG,
						Constants.SELECT_COMPLEX_MSG);
			} else {
				var record = grid.getSelectionModel().getSelected();

				win.show();
				// 将被选择的第一条数据加载给form
				mypanel.getForm().loadRecord(record);
				Ext.get("flag").dom.value = "1";

			}
		} else {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG,
					Constants.SELECT_NULL_UPDATE_MSG);
		}
	}
	// 删除函数
	function deleteRecord() {

		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		var id;
		if (selected.length < 1) {
			// 没有选择数据时
			Ext.Msg.alert(Constants.SYS_REMIND_MSG,
					Constants.SELECT_NULL_DEL_MSG);
		} else {
			// 选择多条数据时
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.worktypeId) {
					ids.push(member.worktypeId);
				} else {
					store.remove(store.getAt(i));// 从画面移去不存在的数据
				}
			}
			Ext.Msg.confirm(Constants.BTN_DELETE, Constants.DelMsg, function(
							buttonobj) {
						if (buttonobj == "yes") {
							Ext.lib.Ajax.request(Constants.POST,
									'workticket/delsWorkType.action', {
										success : function(action) {
											Ext.Msg.alert(
													Constants.SYS_REMIND_MSG,
													Constants.DEL_SUCCESS)
											store.baseParams = {
												fuzzy : fuuzy.getValue(),
												workticketTypeCode : ticketComboBox
														.getValue()
											};
											store.load({
														params : {
															start : 0,
															limit : Constants.PAGE_SIZE
														}
													});
										},
										failure : function() {
											Ext.Msg.alert(Constants.ERROR,
													Constants.DEL_ERROR);
										}
									}, 'bean.workTypeIds=' + ids);// 将选取的多条worktypeId传送给后台
						}
					});
		}
	}
});