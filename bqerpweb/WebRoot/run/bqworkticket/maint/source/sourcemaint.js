Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {

	// ↓↓--------------主画面-------------------↓↓//
	// 定义grid
	var MyRecord = Ext.data.Record.create([
		// 编码
		{name : 'sourceId'},
    // 名称
    {name : 'sourceName'},
		// 显示顺序
		{name : 'orderBy'}
	]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url:'workticket/getSourceList.action'
			}

	);

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"

	}, MyRecord);
	// grid的store
	var store = new Ext.data.Store({

		proxy : dataProxy,

		reader : theReader,
        sortInfo :{field: "orderBy", direction: "ASC"}
	});
	// 加载数据
	store.load();

	// 输入框
	var fuzzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});

	// grid选择模式
	var sm = new Ext.grid.CheckboxSelectionModel();

	// grid
	var grid = new Ext.grid.GridPanel({
		region : "center",
		layout : 'fit',
		store : store,
		columns : [
		sm, {
			header : "编码",
			width : 75,
			sortable : true,
			dataIndex : 'sourceId'
		},	{
			header : "工作票来源",
			width : 75,
			sortable : true,
			dataIndex : 'sourceName'
		},	{
			header : "显示顺序",
			width : 75,
			sortable : true,
			dataIndex : 'orderBy'
		}],
		sm : sm,
		autoSizeColumns : true,
		enableColumnMove : false,
		viewConfig : {
			forceFit : true
		},
		tbar : ['编码或工作票来源：', fuzzy, {
			text : Constants.BTN_QUERY,
			iconCls: Constants.CLS_QUERY,
			handler : queryRecord
		},	{
			text : Constants.BTN_ADD,
            iconCls : Constants.CLS_ADD,
			handler :addRecord
		}, {
			text : Constants.BTN_UPDATE,
			iconCls : Constants.CLS_UPDATE,
			handler : updateRecord
		}, {
			text : Constants.BTN_DELETE,
			iconCls : Constants.CLS_DELETE,
			handler : deleteRecord
		}]
	});

	// 显示区域
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
			items : [grid]
	});

	//↑↑--------------主画面-------------------↑↑//

	// 双击编辑
	grid.on("dblclick", updateRecord);

	//↓↓--------------弹出窗口-------------------↓↓//
	// 组件宽度
	var wd = 180;

	// 编码
	var sourceIdHidden = {
		id : "sourceIdHidden",
		xtype : "textfield",
		fieldLabel : '编码',
		value : Constants.AUTO_CREATE,
		width : wd,
		readOnly : true,
		name:"sourceIdHidden"
	}

	// 工作票来源
	var sourceName =new Ext.form.TextField( {
		id : "sourceName",
		xtype : "textfield",
		fieldLabel : '工作票来源<font color ="red">*</font>',
		allowBlank : false,
		width : wd,
		name : 'source.sourceName',
        maxLength : 30
	});

	// 显示顺序
	var orderBy = {
		id : "orderBy",
		xtype : 'numberfield',
		fieldLabel : '显示顺序',
		allowBlank : true,
		name : 'source.orderBy',
		width : wd,
        maxLength : 10
	}

	// 弹出窗口panel
	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'right',
		height : 120,
		items:[sourceIdHidden,sourceName,orderBy]
	});

	// 弹出窗口
	var win = new Ext.Window({
		width : 400,
		height : 160,
		title : "工作票来源维护",
		buttonAlign : "center",
		resizable : false,
		modal:true,
		items : [myaddpanel],
		buttons : [{
			text : Constants.BTN_SAVE,
			iconCls: Constants.CLS_SAVE,
			handler : function() {
				var myurl="";
				if (Ext.get("sourceIdHidden").dom.value == Constants.AUTO_CREATE) {
						// 增加
						myurl="workticket/addSource.action";
				} else {
						// 修改
						myurl="workticket/updateSource.action";
				}
				// 提交表单
				myaddpanel.getForm().submit({
						method : Constants.POST,
						url : myurl,
						success : function(form, action) {
							var o = eval("(" + action.response.responseText + ")");
							// 显示后台返回信息
							Ext.Msg.alert(Constants.SYS_REMIND_MSG, o.msg);
							// 刷新grid
							store.load();
							// 隐藏窗口
							win.hide();
						},
						failure : function() {
							Ext.Msg.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
						}
				});
			}
			}, {
			text : Constants.BTN_CANCEL,
            iconCls:Constants.CLS_CANCEL,
			handler : function() {
				win.hide();
			}
			}],
		layout : 'fit',
		closeAction : 'hide'
	});
	//↑↑--------------弹出窗口-------------------↑↑//

	//↓↓--------------处理-------------------↓↓//
	/**
	 * 查询
	 */
	function queryRecord() {
		// 获得查询字符串
        var fuzzytext = fuzzy.getValue();
		// 查询参数
        store.baseParams = {
			fuzzy : fuzzytext
		};
        // 查询
		store.load();
	}
	/**
     *  增加
	 */
	function addRecord()
	{
		// 重置表单
		myaddpanel.getForm().reset();
		// 显示增加窗口
        win.show();
        // 自动生成字段
		Ext.get("sourceIdHidden").dom.value=Constants.AUTO_CREATE;
	}
	/**
     * 修改
	 */
	function updateRecord()
	{
			// 判断有无选择记录
            if (grid.selModel.hasSelection()) {
			     var records = grid.selModel.getSelections();
			     var recordslen = records.length;
			// 选择多条数据时弹出提示信息
            if (recordslen > 1) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.SELECT_COMPLEX_MSG);
			} else {
                // 获得选择的记录
				var record = grid.getSelectionModel().getSelected();
				// 显示修改画面
                win.show();
                // 传入修改数据
				myaddpanel.getForm().loadRecord(record);
				Ext.get("sourceIdHidden").dom.value=record.data.sourceId;
			}
		} else {
            // 没有选择记录时显示提示信息
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.SELECT_NULL_UPDATE_MSG);
		}
	}
	/**
     *  删除
	 */
	function deleteRecord()
	{
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
        // 如果没有选择记录显示提示信息
		if (selected.length == 0) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.SELECT_NULL_DEL_MSG);
		} else {
            // 获得批量删除的id集合
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.sourceId) {
					ids.push(member.sourceId);
				} else {
					store.remove(store.getAt(i));
				}
			}
            // 弹出删除确认信息
			Ext.Msg.confirm(Constants.BTN_DELETE, Constants.DelMsg, function(
					buttonobj) {
				if (buttonobj == "yes") {
                    // 删除
					Ext.lib.Ajax.request('POST',
							'workticket/deleteSource.action', {
								success : function(action) {
									// 显示返回信息
                                    Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.DEL_SUCCESS)
                                   		store.load();
								},
								failure : function() {
									Ext.Msg.alert(Constants.ERROR, Constants.DEL_ERROR);
								}
							}, 'sourceIds=' + ids);
				}
			});
		}
	}
});