Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'dangerTypeId'
	}, {
		name : 'dangerTypeName'
	}, {
		name : 'workticketTypeCode'
	}, {
		name : 'orderBy'
	}, {
		name : 'modifyBy'
	}, {
		name : 'modifyDate'
	}, {
		name : 'enterpriseCode'
	}]);

	var dataProxy = new Ext.data.HttpProxy({
		//查询危险点类型信息
		url : 'workticket/getDangerTypeList.action'
	});

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);

	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
     
	 //定义工作票种类数据源
	 var dangerTypeStore = new Ext.data.JsonStore({
	 url : 'workticket/getDangerTypeQueryWorkticketType.action',
	 root : 'list',
	 fields : [{
	 name : 'workticketTypeName'},
	 {name : 'workticketTypeCode'}]
	 })
	 dangerTypeStore.load();
	 dangerTypeStore.on('load', function(e, records, o) {
	 dangerTypeCbo.setValue(records[0].data.workticketTypeCode);
	 });
	
	 // 工作票种类下拉框
	 var dangerTypeCbo = new Ext.form.ComboBox({
	 id:'dangerTypeCbo',
	 allowBlank : true,
	 triggerAction : 'all',
	 store :dangerTypeStore,
	 displayField : 'workticketTypeName',
	 valueField : 'workticketTypeCode',
	 mode : 'local',
	 readOnly : true,
	 width : 90
	 })

	// 分页
//	store.load({
//		params : {
//			start : 0,
//			limit : 10
//		}
//	});

	var fuuzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});
	var sm = new Ext.grid.CheckboxSelectionModel();
    
    function renderDate(value) {
        if (!value) return "";
        
        var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
        var reTime = /\d{2}:\d{2}:\d{2}/gi;
        
        var strDate = value.match(reDate);
        var strTime = value.match(reTime);
        if (!strDate) return "";
        return strTime ? strDate + " " + strTime : strDate;
    }
    
	var grid = new Ext.grid.GridPanel({
		region : "center",
		layout : 'fit',
		store : store,

		columns : [sm, {

			header : "危险类别ID",
			width : 75,
			sortable : true,
			dataIndex : 'dangerTypeId',
			hidden : true
		},

		{
			header : "类别名称",
			width : 75,
			sortable : true,
			dataIndex : 'dangerTypeName'
		},

		{
			header : "工作票类型",
			width : 75,
			sortable : true,
			dataIndex : 'workticketTypeCode',
			hidden : true
		},

		{
			header : "排序",
			width : 75,
			sortable : true,
			dataIndex : 'orderBy',
			hidden : true
		}, {
			header : "填写人",
			width : 75,
			sortable : true,
			dataIndex : 'modifyBy'
		}, {
			header : "填写时间",
			width : 75,
			sortable : true,
			dataIndex : 'modifyDate',
			renderer : renderDate
			
		}, {
			header : "企业编码",
			width : 75,
			sortable : true,
			dataIndex : 'enterpriseCode',
			hidden : true
		}],
		sm : sm,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : true
		},
		title : '危险点类别维护',

		tbar : ['工作票类型',dangerTypeCbo,'-','根据类别名称模糊查询:',fuuzy,'-',{
			text : "查询",
			iconCls:'query',
			handler : queryRecord
		}, {
			text : "新增",
			iconCls : 'add',
			handler : addRecord
		}, {
			text : "修改",
			iconCls : 'update',
			handler : updateRecord
		}, {
			text : "删除",
			iconCls : 'delete',
			handler : deleteRecord
		}],
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : 10,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});

	grid.on("dblclick", updateRecord);
	// ---------------------------------------

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		// layout : "fit",
		items : [grid]
	});

	// -------------------
	var wd = 180;

	var dangerTypeId = {
		id : "dangerTypeId",
		xtype : "textfield",
		fieldLabel : 'ID',
		value : '自动生成',
		width : wd,
		readOnly : true,
		name : 'dangerTypeId'

	}
	
//	var hiddenTypeId = {
//        id : "hiddenTypeId",
//        xtype : "hidden",
//        value: '0',
//        name : 'dangerType.dangerTypeId'
//    }
	
	var dangerTypeName = new Ext.form.TextField({
		id : "dangerTypeName",
		xtype : "textfield",
		fieldLabel : '危险点类别名称',
		allowBlank : false,
		width : wd,
		name : 'dangerType.dangerTypeName'

	});

	var workticketTypeCode = new Ext.form.ComboBox({
		id : "workticketTypeCode",
//		xtype : "textfield",
		allowBlank : true,
		triggerAction : 'all',
		store :dangerTypeStore,
	    fieldLabel : '工作票类型',
	    displayField : 'workticketTypeName',
	    valueField : 'workticketTypeCode',
		name : 'dangerType.workticketTypeCode',
		hiddenName:'dangerType.workticketTypeCode',
		mode : 'local',
	    readOnly : true,
		width : wd
	})
//	var dangerTypeCbo = new Ext.form.ComboBox({
//	 id:'dangerTypeCbo',
//	 allowBlank : true,
//	 triggerAction : 'all',
//	 store :dangerTypeStore,
//	 displayField : 'workticketTypeName',
//	 valueField : 'workticketTypeCode',
//	 mode : 'local',
//	 readOnly : true,
//	 width : 90
//	 })

	var orderBy = {
		id : "orderBy",
		xtype : "numberfield",
		fieldLabel : '排序',
		name : 'dangerType.orderBy',
		
		width : wd
	}

	var modifyBy = {
		id : "modifyBy",
		xtype : "textfield",
		fieldLabel : '填写人',
		name : 'dangerType.modifyBy',
		readOnly : true,
		width : wd
	}

	var modifyShowDate = {
		id : "modifyDate",
		xtype : "textfield",
		fieldLabel : '填写日期',
		name : 'dangerType.modifyDate',
		readOnly : true,
		width : wd
	}
	
	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		title : '增加/修改危险点类别信息',
		items : [dangerTypeId, dangerTypeName, workticketTypeCode, orderBy,
				modifyBy,modifyShowDate]

	});
    
	
	var win = new Ext.Window({
		width : 350,
		height : 350,
		buttonAlign : "center",
		items : [myaddpanel],
		layout : 'fit',
		resizable : false,
        modal : true,
		closeAction : 'hide',
		buttons : [{
			text :"保存",
			iconCls : 'save',
			handler : function() {
				var myurl = "";
				var dangerTypeId = Ext.get("dangerTypeId").dom.value;
				if (dangerTypeId == "自动生成" ) {
					if(Ext.get("workticketTypeCode").dom.value != "所有") {
					   myurl = "workticket/addDangerType.action";
					} else {
						alert("工作票类型不能选择所有！");
					}
//					Ext.get("hiddenTypeId").dom.value = "-1";
				} else {
					if(Ext.get("workticketTypeCode").dom.value != "所有") {
					   myurl = "workticket/updateDangerType.action";
					} else {
					   alert("工作票类型不能选择所有！");
					}
					
					  // 用于后台查询
//                    Ext.get("hiddenTypeId").dom.value = dangerTypeId;
				}
				myaddpanel.getForm().submit({
					method : 'POST',
					url : myurl,
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("注意", o.msg);
						store.load({
							params : {
								start : 0,
								limit : 10
							}
						});
						win.hide();
					},
					faliue : function() {
						Ext.Msg.alert('错误', '出现未知错误.');
					}
				});
			}
		}, {
			text : '取消',
			iconCls: 'cancel',
			handler : function() {
				win.hide();
			}
		}]

	});

	// 查询
	function queryRecord() {
		var fuzzytext = fuuzy.getValue();
		store.baseParams = {
			fuzzy : fuzzytext,
			workticketTypeCode:dangerTypeCbo.getValue()
		};
		store.load({
			params : {
				start : 0,
				limit : 10
			}
		});
	}
	
	// 增加
    function addRecord() {
        myaddpanel.getForm().reset();
		win.setTitle("增加危险点类别信息");
        win.show();
    }
    
	//修改
	function updateRecord() {
        if (grid.selModel.hasSelection()) {
            var records = grid.selModel.getSelections();
            var recordslen = records.length;
            if (recordslen > 1) {
                Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
            } else {
                var record = grid.getSelectionModel().getSelected();
                win.setTitle("修改危险点类别信息");
				win.show();
                // 显示该行记录
                myaddpanel.getForm().loadRecord(record);
                // 设置日期数据
                Ext.get("modifyDate").dom.value = renderDate(Ext.get("modifyDate").dom.value);
                }
        } else {
            Ext.Msg.alert("提示", "请先选择要编辑的行!");
        }
    }

	function deleteRecord() {

		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		var names = [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {

			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.dangerTypeId) {
					ids.push(member.dangerTypeId);
					names.push(member.dangerTypeName);
				} else {
					store.remove(store.getAt(i));
				}
			}
			Ext.Msg.confirm("删除", "是否确定删除名称为'" + names + "'的记录？", function(
					buttonobj) {
				if (buttonobj == "yes") {
					Ext.lib.Ajax.request('POST', 'workticket/deleteDangerType.action', {
						success : function(action) {
							Ext.Msg.alert("提示", "删除成功！")
							store.load({
								params : {
									start : 0,
									limit : 10
								}
							});

						},
						failure : function() {
							Ext.Msg.alert('错误', '删除时出现未知错误.');
						}
					}, 'ids=' + ids);
				}
			});
		}

	}
	
	queryRecord();

});