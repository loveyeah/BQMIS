Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	
	// 定义grid
	var MyRecord = Ext.data.Record.create([
	{name : 'flagId'},
	{name : 'flagName'},
	{name : 'orderBy'}
	]);

	var dataProxy = new Ext.data.HttpProxy(
		{
			url:'workticket/getFlagList.action'
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
	store.load();
	
	var fuuzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});
	var sm = new Ext.grid.CheckboxSelectionModel();

	var grid = new Ext.grid.GridPanel({
		region : "center",
		layout : 'fit',
		store : store,

		columns : [
		sm,
		{
			header : "编码",
			width : 75,
			sortable : true,
			dataIndex : 'flagId'
		},

		{
			header : "名称",
			width : 75,
			sortable : true,
			dataIndex : 'flagName'
		},

		{
			header : "显示顺序",
			width : 75,
			sortable : true,
			dataIndex : 'orderBy'
		}
		],
		sm : sm,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : true
		},
		
		tbar : ['编码或名称: ',fuuzy, {
			text : Constants.BTN_QUERY,
			iconCls : Constants.CLS_QUERY,
			handler : queryRecord
		},
		{
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
		}],
		enableColumnMove : false
	});

	grid.on("rowdblclick", updateRecord);
	//---------------------------------------

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [grid]
	});

	//=============  定义弹出画面  ===============
	var wd = 180;

	var flagId = {
		id : "flagId",
		xtype : "textfield",
		fieldLabel : '编码',
		value : Constants.AUTO_CREATE,
		allowBlank : false,
		width : wd,
		readOnly : true,
		name : 'flagId'
	};

	var flagName = {
		id : "flagName",
		xtype : "textfield",
		fieldLabel : "名称<font color='red'>*</font>",
		name : 'flag.flagName',
		width : wd,
		allowBlank : false,
		maxLength : 30
	}

	var orderBy = {
		id : "orderBy",
		xtype : "numberfield",
		fieldLabel : '显示顺序',
		name : 'flag.orderBy',
		width : wd,
		maxLength : 10
	}

	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'right',
		labelWidth : 80,
		items : [flagId,flagName,orderBy]
	});

	var win = new Ext.Window({
		width : 320,
		height : 160,
		title : '标点符号维护',
		buttonAlign : "center",
		items : [myaddpanel],
		layout : 'fit',
		closeAction : 'hide',
		resizable : false,
		modal:true,
		buttons : [{
			text : Constants.BTN_SAVE,
			iconCls : Constants.CLS_SAVE,
			handler : function() {
				var myurl="";
				if (Ext.get("flagId").dom.value == Constants.AUTO_CREATE) {
					myurl="workticket/addFlag.action";
				} else {
					myurl="workticket/updateFlag.action";
				}
				myaddpanel.getForm().submit({
					method : Constants.POST,
					url : myurl,
					success : function(form, action) {
						// 显示成功信息
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert(Constants.NOTICE, o.msg);
							store.load();
						win.hide(); 
					},
					faliue : function() {
						Ext.Msg.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
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
		  
	/**
	 * 查询
	 */
	function queryRecord() {
		var fuzzytext = fuuzy.getValue();
		store.baseParams = {
			fuzzy : fuzzytext
		};
		store.load();
	}
	
	/**
	 * 增加
	 */
	function addRecord()
	{
		myaddpanel.getForm().reset();
		// 显示维护画面 
		win.show();
		// 增加的情况下，编码为“自动生成”
		Ext.get("flagId").dom.value =Constants.AUTO_CREATE;
	}
	
	/**
	 * 更新
	 */
	function updateRecord()
	{
		if (grid.selModel.hasSelection()) {
			// 有选择的情况下

			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				// 选择的记录条数大于1
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.SELECT_COMPLEX_MSG);
			} else {
				// 选择的记录条数为1
				// 取得选择的数据
				var record = grid.getSelectionModel().getSelected();

				// 显示维护画面 
				win.show();
				// 将选择的数据显示在维护画面上
				myaddpanel.getForm().loadRecord(record);
			}
		} else {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.SELECT_NULL_UPDATE_MSG);
		}
	}
	
	/**
	 * 删除
	 */
	function deleteRecord()
	{
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		if (selected.length == 0) {
			// 一条都没有选中
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.SELECT_NULL_DEL_MSG);
		} else {

			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.flagId) {
					// 将选中的记录的flagId放入ids中
					ids.push(member.flagId); 
				} else {
					// 如果标点符号的flagId没有值，则把这条数据从画面上除去
					store.remove(store.getAt(i));
				}
			}
			
			// 删除前的提示信息
			Ext.Msg.confirm(Constants.BTN_DELETE, Constants.DelMsg, function(
					buttonobj) {

				if (buttonobj == "yes") {

					Ext.lib.Ajax.request(Constants.POST,
						'workticket/deleteFlag.action', {
							success : function(action) {
								Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.DEL_SUCCESS)
                               		store.load();
					         	
							},
							failure : function() {
								Ext.Msg.alert(Constants.ERROR, Constants.DEL_ERROR);
							}
						}, 'ids=' + ids);
				}
			});
		}

	}
});