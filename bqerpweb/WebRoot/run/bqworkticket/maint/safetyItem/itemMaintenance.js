Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {

	//add by fyyang 090302 

	 //动火票时分一级二级
	    var radioOne = new Ext.form.Radio({
        id : 'radioOne',
        boxLabel : '一级',
        name : 'keyType',
        inputValue : '1',
        checked : true
    })
    var radioTwo = new Ext.form.Radio({
        id : 'radioTwo',
        boxLabel : '二级',
        name : 'keyType',
        inputValue : '2'
    })
	// ↓↓*****弹出窗口*******↓↓//
	// 组件默认宽度
	var width = 240;
	// 安全措施id
	var safetyIdField = new Ext.form.TextField({
				fieldLabel : "安全措施ID",
				id : "safety",
				readOnly : true,
				name : "safetyId",
				width : width,
				value : Constants.AUTO_CREATE
			});

	// 工作票类型编码
	var workticketTypeCodeField = new Ext.form.TextField({
				fieldLabel : "工作票类型编码",
				id : "workticketTypeCode",
				readOnly : true,
				name : "workticketTypeCode",
				width : width,
				value : Constants.AUTO_CREATE
			});

	// 安全措施编码
	var safetyCodeField = new Ext.form.TextField({
				fieldLabel : "安全措施编码<font color='red'>*</font>",
				id : "safetyCode",
				allowBlank : false,
				name : "safetyCode",
				width : width,
				maxLength : 3
			});

	// 安全措施描述
	var safetyDescField = new Ext.form.TextArea({
				fieldLabel : "安全措施描述<font color='red'>*</font>",
				id : "safetyDesc",
				allowBlank : false,
				maxLength : 200,
				width : 237,
				height : 90,
				name : "safetyDesc"
			});

	// 挂牌内容检索
	var url = "workticket/getMarkcardType.action";
	var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", url, false);
	conn.send(null);
	var markcardType_data
	markcardType_data = Ext.util.JSON.decode("[" + "['　','0'],"
			+ conn.responseText + "]");
	// store
	var markcardTypeStore = new Ext.data.SimpleStore({
				fields : ['name', 'id'],
				data : markcardType_data
			});
	// 挂牌内容组合框
	var markcardTypeNameCbo = new Ext.form.ComboBox({
				fieldLabel : "挂牌内容",
				id : "markcardTypeNameCbo",
				name : "markcardTypeId",
				width : width,
				store : markcardTypeStore,
				hiddenName : "markcard",
				displayField : "name",
				valueField : "id",
				mode : 'local',
				triggerAction : 'all',
				emptyText : '请选择挂牌内容...',
				blankText : '挂牌内容',
				// allowBlank : false,
				readOnly : true
			});

	// 是否补充安全措施
	var isRunAddStore = new Ext.data.SimpleStore({
				fields : ["text", "value"],
				data : [["是", "Y"], ["否", "N"]]
			});

	// 安措类型
	var safetyTyep = new Ext.data.SimpleStore({
				fields : ["text", "value"],
				data : [["检修", "repair"], ["运行", "run"], ["消防队", "fire"],
						["补充安措", "Y"], ["非补充安措", "N"], ["检修补充", "addrepari"],
						["运行补充", "addrun"], ["- - - - - - - -", ""]]
			});

	var isRunAddCbo = new Ext.form.ComboBox({
				id : "safetyType",
				fieldLabel : "安措类型",
				allowBlank : false,
				triggerAction : 'all',
				name : "safetyType",
				width : width,
				store : safetyTyep,
				hiddenName : "safe.safetyType",
				displayField : 'text',
				valueField : 'value',
				mode : 'local',
				value : "Y",
				readOnly : true
			});

	// panel
	var myaddpanel = new Ext.FormPanel({
				frame : true,
				labelWidth : 110,
				labelAlign : 'right',
				defaultType : "textfield",
				items : [safetyIdField, workticketTypeCodeField,
						safetyCodeField, safetyDescField, markcardTypeNameCbo,
						isRunAddCbo]

			});

	// 弹出窗口
	var win = new Ext.Window({
		width : 425,
		height : 320,
		buttonAlign : "center",
		resizable : false,
		modal : true,
		items : [myaddpanel],
		title : '工作票安全措施项目维护',
		buttons : [{
			text : Constants.BTN_SAVE,
			id : "add",
			iconCls : Constants.CLS_SAVE,
			handler : function() {
				// start jincong 2008-12-27 编码验证
				
				if (validateCode(safetyCodeField.getValue())) {
					var myurl = "";
					if (Ext.get("safety").dom.value == Constants.AUTO_CREATE) {
						myurl = "workticket/addSafety.action";
					} else {
						myurl = "workticket/updateSafety.action";
					}
					
					if(workticketTypeCodeField.getValue()=="4")
					{
				
					if(radioOne.checked)
					{
						workticketTypeCodeField.setValue("4");
					}
					if(radioTwo.checked)
					{
						workticketTypeCodeField.setValue("6");
					}
					}

					myaddpanel.getForm().submit({
						method : 'POST',
						url : myurl,
						success : function(form, action) {
							var o = eval("(" + action.response.responseText
									+ ")");
							Ext.Msg.show({
										title : Constants.SYS_REMIND_MSG,
										msg : o.msg,
										buttons : Ext.Msg.OK,
										icon : Ext.MessageBox.INFO
									});
							searchStore.load({
										params : {
											workticketTypeCode : worktiketTypeCbo.value
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
			}
				// end jincong 2008-12-27 编码验证
		}, {
			text : Constants.BTN_CANCEL,
			id : "close",
			iconCls : Constants.CLS_CANCEL,
			handler : function() {
				win.hide();
				myaddpanel.getForm().reset();
			}
		}],
		layout : 'fit',
		closeAction : 'hide'
	});
	// ↑↑********弹出窗口*********↑↑//

	// ↓↓********** 主画面*******↓↓//

	// 工作票类型检索
	var worktiketTypeData = Ext.data.Record.create([{
				name : 'workticketTypeCode'
			}, {
				name : 'workticketTypeName'
			}]);

	var storeCbx = new Ext.data.JsonStore({
				root : 'list',
				url : "workticket/getWorkticketType.action",
				fields : worktiketTypeData
			})
	storeCbx.load({
		// start jincong 2008-12-27 增加“公用”
		callback : function() {
			// 新记录
			var record = new worktiketTypeData({
						workticketTypeCode : 'C',
						workticketTypeName : '公用'
					});
			var count = storeCbx.getCount();
			storeCbx.insert(count, record);
		}
			// end jincong 2008-12-27 增加“公用”
		});

	// 工作票类型组合框
	var worktiketTypeCbo = new Ext.form.ComboBox({
				id : "worktiketTypeCbo",
				name : 'worktiketTypeCbo',
				allowBlank : true,
				triggerAction : 'all',
				store : storeCbx,
				displayField : "workticketTypeName",
				valueField : "workticketTypeCode",
				mode : 'local',
				// emptyText : '工作票类型...',
				blankText : '工作票类型',
				readOnly : true,
				listeners : {
					select : levelShow
				}
			});

	// 通过store的装载初始化所属系统下拉框的默认选项为store的第一项
	storeCbx.on("load", function(e, records, o) {
				worktiketTypeCbo.setValue(records[0].data.workticketTypeCode);
				searchStore.load({
							params : {
								workticketTypeCode : worktiketTypeCbo.value
							}
						});
			});

	// 查询按钮
	var searchBtn = new Ext.Button({
				id : 'search',
				text : Constants.BTN_QUERY,
				iconCls : Constants.CLS_QUERY
			});
	// 增加按钮
	var addBtn = new Ext.Button({
				id : 'add',
				text : Constants.BTN_ADD,
				iconCls : Constants.CLS_ADD,
				handler : function() {
				}
			});
	// 修改按钮
	var editBtn = new Ext.Button({
				id : 'edit',
				text : Constants.BTN_UPDATE,
				iconCls : Constants.CLS_UPDATE,
				handler : function() {
				}
			});
	// 删除按钮
	var deleteBtn = new Ext.Button({
				id : 'delete',
				text : Constants.BTN_DELETE,
				iconCls : Constants.CLS_DELETE,
				handler : function() {
				}
			});
	// 选择列
	var sm2 = new Ext.grid.CheckboxSelectionModel();

	// grid中的数据
	var rungridlist = new Ext.data.Record.create([{
				name : 'safe.safetyCode'
			}, {
				name : 'safe.workticketTypeCode'
			}, {
				name : 'safe.safetyId'
			}, {
				name : 'safe.safetyDesc'
			}, {
				name : 'workticketTypeName'
			}, {
				name : 'markcardTypeName'
			}, {
				name : 'safe.safetyType'
			}]);

	// grid的store
	var searchStore = new Ext.data.JsonStore({
				url : 'workticket/searchSafety.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : rungridlist
			});
	// 排序
	searchStore.setDefaultSort('safe.safetyCode', 'ASC');
	// 运行执行的Grid主体
	var rungrid = new Ext.grid.GridPanel({
				store : searchStore,
				columns : [sm2, {
							header : "安措编码",
							width : 60,
							sortable : true,
							dataIndex : 'safe.safetyCode'
						}, {
							header : "安措内容",
							width : 200,
							sortable : true,
							dataIndex : 'safe.safetyDesc'
						}, {
							header : "挂牌内容",
							width : 60,
							sortable : true,
							dataIndex : 'markcardTypeName'
						}, {

							hidden : true,
							dataIndex : 'safe.isRunAdd'
						}],
				viewConfig : {
					forceFit : true
				},
				tbar : ['工作票类型: ', worktiketTypeCbo, {
							xtype : "tbseparator"
						}, radioOne,radioTwo,searchBtn, addBtn, editBtn, deleteBtn],
				sm : sm2,
				frame : false,
				border : false,
				enableColumnMove : false,
				autoExpandColumn : 2

			});

	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,

				items : [{
							title : "",
							region : 'center',
							layout : 'fit',
							border : false,
							margins : '0 0 0 0',
							split : true,
							collapsible : false,
							items : [rungrid]
						}]
			});
	// ↑↑********** 主画面*******↑↑//

	// ↓↓*********处理***********↓↓//
	// 增加处理
	addBtn.handler = function() {
		var value = worktiketTypeCbo.value;
		if (value == undefined || value == "") {
			Ext.MessageBox.alert(Constants.NOTICE, "请选择一个工作票类型");
			return;
		}
		myaddpanel.getForm().reset();
		workticketTypeCodeField.setValue(value);
		win.show();
	}
	// 编辑处理
	editBtn.handler = function() {
		if (rungrid.selModel.hasSelection()) {
			var records = rungrid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG,
						Constants.SELECT_COMPLEX_MSG);
			} else {
				var record = rungrid.getSelectionModel().getSelected();
				win.show();
				myaddpanel.getForm().loadRecord(record);
				var value = getMarkcardValue(record.get("markcardTypeName"));
				if (value) {
					markcardTypeNameCbo.setValue(value);
				}
				safetyIdField.setValue(record.get("safe.safetyId"));
				// workticketTypeCodeField.setValue(worktiketTypeCbo.value);
				workticketTypeCodeField.setValue(record
						.get("safe.workticketTypeCode"));
				safetyCodeField.setValue(record.get("safe.safetyCode"));
				safetyDescField.setValue(record.get("safe.safetyDesc"));

				isRunAddCbo.setValue(record.get("safe.safetyType"));
			}
		} else {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG,
					Constants.SELECT_NULL_UPDATE_MSG);
		}
	}
	/**
	 * 根据挂牌内容名称获得编号
	 */
	function getMarkcardValue(markcardTypeName) {
		for (var i = 0; i < markcardTypeStore.getCount(); i++) {
			var record = markcardTypeStore.getAt(i);
			if (record.get('name') == markcardTypeName) {
				return record.get('id');
			}
		}
		return null;
	}
	// 双击编辑
	rungrid.on("rowdblclick", editBtn.handler);
	// 查询处理
	searchBtn.handler = function() {
		var value = worktiketTypeCbo.value;
		if (value == undefined || value == "") {
			Ext.MessageBox.alert(Constants.NOTICE, "请选择一个工作票类型");
			return;
		}
		else if(value=="4"&&radioTwo.checked)
		{
			value="6";
		}
		searchStore.load({
					params : {
						workticketTypeCode : value
					}
				});
	}

	// 删除处理
	deleteBtn.handler = function() {
		var sm = rungrid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		if (selected.length == 0) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG,
					Constants.SELECT_NULL_DEL_MSG);
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var record = selected[i];
				if (record.get("safe.safetyId")) {
					ids.push(record.get("safe.safetyId"));
				} else {
					store.remove(store.getAt(i));
				}
			}
			Ext.Msg.confirm(Constants.BTN_DELETE, Constants.DelMsg, function(
					buttonobj) {
				if (buttonobj == "yes") {
					Ext.lib.Ajax.request('POST',
							'workticket/deleteSafety.action', {
								success : function(action) {
									Ext.Msg.alert(Constants.SYS_REMIND_MSG,
											Constants.DEL_SUCCESS
													+ "&nbsp&nbsp&nbsp&nbsp")
									searchStore.load({
										params : {
											workticketTypeCode : worktiketTypeCbo.value
										}
									});
								},
								failure : function() {
									Ext.Msg.alert(Constants.ERROR,
											Constants.DEL_ERROR
													+ "&nbsp&nbsp&nbsp&nbsp");
								}
							}, 'ids=' + ids);
				}
			});
		}
	}
	
	//add by fyyang 090302
	//仅动火票分一级二级
	function levelShow()
	{
	if(worktiketTypeCbo.getValue()=="4")
	{
		Ext.get("radioOne").dom.parentNode.style.display = '';
		Ext.get("radioTwo").dom.parentNode.style.display = '';
		
	}
	else
	{
		Ext.get("radioOne").dom.parentNode.style.display = 'none';
		Ext.get("radioTwo").dom.parentNode.style.display = 'none';
	}
	}
	levelShow();
});