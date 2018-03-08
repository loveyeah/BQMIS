Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
	/**
	 * 金钱格式化
	 */
	function moneyFormat(v) {
		if (v == null || v == "") {
			return "0.0000";
		}
		v = (Math.round((v - 0) * 10000)) / 10000;
		v = (v == Math.floor(v))
				? v + ".0000"
				: ((v * 10 == Math.floor(v * 10))
						? v + "000"
						: ((v * 100 == Math.floor(v * 100)) ? v + "00" : ((v
								* 1000 == Math.floor(v * 1000)) ? v + "0" : v)));
		v = String(v);
		var ps = v.split('.');
		var whole = ps[0];
		var sub = ps[1] ? '.' + ps[1] : '.0000';
		var r = /(\d+)(\d{3})/;
		while (r.test(whole)) {
			whole = whole.replace(r, '$1' + ',' + '$2');
		}
		v = whole + sub;
		if (v.charAt(0) == '-') {
			return '-' + v.substr(1);
		}
		return v;
	}
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'eventId'
	}, {
		name : 'appraiseItem'
	}, {
		name : 'appraiseMark'
	}, {
		name : 'appraiseCriterion'
	}, {
		name : 'displayNo'
	}, {
		name : 'isUse'
	}]);

	var dataProxy = new Ext.data.HttpProxy({
		url : 'clients/findConAppraiseItemList.action'		
	});

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);

	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
	
	var sm = new Ext.grid.CheckboxSelectionModel({
	});

	var grid = new Ext.grid.GridPanel({
		region : "center",
		layout : 'fit',
		store : store,
		columns : [sm, new Ext.grid.RowNumberer(), {
			header : "ID",
			width : 75,
			sortable : false,
			hidden : true,
			dataIndex : 'eventId'
		}, {
			header : "评价项目名称",
			width : 75,
			sortable : false,
			dataIndex : 'appraiseItem',
			renderer : showColor
		}, {
			header : "标准分数",
			width : 75,
			sortable : false,
			dataIndex : 'appraiseMark',
			renderer : showColor
		}, {
			header : "评价标准",
			width : 75,
			sortable : false,
			dataIndex : 'appraiseCriterion'
		}, {
			header : "显示顺序",
			width : 75,
			sortable : false,
			dataIndex : 'displayNo',
			renderer : function returnValue(val) {
				if (val == "9999") {
					return '';
				}else{
					return val;
				}
			}
		}, {
			header : "是否启用",
			width : 75,
			sortable : false,
			dataIndex : 'isUse',
			renderer : function returnValue(val) {
				if (val == "N") {
					return '未启用';
				} else if (val == "Y") {
					return '已启用';
				}
			}
		}],
		sm : sm,
		autoSizeColumns : true,
		
		viewConfig : {
			forceFit : true
		},
		tbar : [{
			text : "新增",
			id :'btnAdd',
			iconCls : 'add',
			handler : addRecord
		}, {
			text : "修改",
			id :'btnUpdate',
			iconCls : 'update',
			handler : updateRecord
		}, {
			text : "删除",
			id :'btnDelete',
			iconCls : 'delete',
			handler : deleteRecord
		}, {
			text : "决定启用",
			id : 'btnSave',
			iconCls : 'save',
			handler : confirmRecord
		}],
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});

	//grid.on("rowdblclick", updateRecord);
	// ---------------------------------------

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [grid]
	});

	// -------------------

	var eventId = new Ext.form.Hidden({
		id : "eventId",
		fieldLabel : 'ID',
		anchor : '95%',
		readOnly : true,
		value : '',
		name : 'item.eventId'

	});

	var appraiseItem = new Ext.form.TextField({
		id : "appraiseItem",
		fieldLabel : '评价项目名称',
		allowBlank : false,
		anchor : '95%',
		name : 'item.appraiseItem'
	});

	var appraiseMark = new Ext.form.NumberField({
		id : "appraiseMark",
		fieldLabel : '标准分数',
		allowBlank : false,
		anchor : '95%',
		name : 'item.appraiseMark',
		maxLength : 2,
		maxLengthText : '最多输入2个数字！'
	});

	var appraiseCriterion = new Ext.form.TextArea({
		id : "appraiseCriterion",
		fieldLabel : '评价标准',
		anchor : '94.5%',
		name : 'item.appraiseCriterion',
		height : 80
	});
	
	var displayNo = new Ext.form.NumberField({
		id : "displayNo",
		fieldLabel : '显示顺序',
		allowBlank : false,
		anchor : '95%',
		name : 'item.displayNo'
	});
	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		labelWidth : 80,
		closeAction : 'hide',
		title : '增加/修改评价项目设置',
		items : [eventId, appraiseItem, appraiseMark,appraiseCriterion,displayNo]
	});

	function checkInput() {
		var msg = "";
		if (appraiseItem.getValue() == "") {
			msg = "'评价项目名称";
		}
		if (Ext.get("displayNo").dom.value == "") {
			if (msg != "") {
				msg = msg + ",显示顺序'!";
			} else {
				msg = "显示顺序'!";
			}
		}
		if (msg != "") {
			Ext.Msg.alert("提示", "请输入" + msg);
			return false
		} else {
			return true;
		}
	}

	var win = new Ext.Window({
		width : 450,
		height : 300,
		buttonAlign : "center",
		items : [myaddpanel],
		layout : 'fit',
		closeAction : 'hide',
//		draggable : true,
		resizable : false,
		modal : true,
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
				var myurl = "";
				if (Ext.get("eventId").dom.value == ""
						|| Ext.get("eventId").dom.value == null) {
					myurl = "clients/addConAppraiseItem.action";
				} else {
					myurl = "clients/updateConAppraiseItem.action";
				}
				if (!checkInput())
					return;
				myaddpanel.getForm().submit({
					method : 'POST',
					url : myurl,
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("提示", o.msg);
						store.load({
							params : {
								start : 0,
								limit : 18
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
			iconCls : 'cancer',
			handler : function() {
				win.hide();
			}
		}]
	});

	function addRecord() {
		myaddpanel.getForm().reset();
		win.show();
		myaddpanel.setTitle("增加评价项目设置");
	}
	
	function updateRecord() {
		if (grid.selModel.hasSelection()) {
		var records = grid.selModel.getSelections();
			var recordslen = records.length;
		for (var i = 0; i < records.length; i += 1) {
				var member = records[i].data;
			if (member.eventId == 0) {
				Ext.Msg.alert('提示', '合计列不能修改！');
				return ;
			}
		}
			if (recordslen > 1) {
				Ext.Msg.alert("提示", "请选择其中一项进行编辑！");
			} else {
				var record = grid.getSelectionModel().getSelected();
				win.setPosition(200, 100);
				win.show();
				myaddpanel.getForm().loadRecord(record);
				myaddpanel.setTitle("修改评价项目设置");
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的记录!");
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
				if(member.eventId == 0)
				{
					Ext.Msg.alert('提示', '合计列不能删除！');
					return;
				}
				else if (member.eventId != 0) {
					ids.push(member.eventId);
					names.push(member.appraiseItem);
				} else {
					store.remove(store.getAt(i));
				}
			}
			Ext.Msg.confirm("删除", "是否确定删除名称为'" + names + "'的记录？", function(
					buttonobj) {
				if (buttonobj == "yes") {
					Ext.lib.Ajax.request('POST',
							'clients/deleteConAppraiseItem.action', {
								success : function(action) {
									Ext.Msg.alert("提示", "删除成功！")
									store.load({
										params : {
											start : 0,
											limit : 18
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
	
	function confirm() {
		var totalSum = 0;
		var total = 0;
		for (var i = 0; i < store.getCount() - 1; i++)
		{
			totalSum += store.getAt(i).get('appraiseMark');
		}
		total =  moneyFormat(totalSum);
		if(total != 100)
		{
			Ext.Msg.alert('提示','合计标准分数必须等于100分才能进行"确定"！')
			return false;
		}
		return true;
	}
	
	function confirmRecord()
	{
		if (confirm() == false)
			return;
			Ext.Msg.confirm("提示", "设置成功后页面所有数据不可再更改，是否确定设置评价项目?", function(buttonobj) {
				if (buttonobj == "yes") {
					Ext.Msg.wait("操作进行中...", "请稍候");
					Ext.Ajax.request({
						method : 'post',
						url : 'clients/confirmConAppraiseItem.action',
						success : function(action) {
							Ext.Msg.alert("提示", "评价项目设置成功！");
							store.reload();
						},
						failure : function() {
							Ext.Msg.alert(Constants.SYS_REMIND_MSG,
									Constants.COM_E_014);
						}
					});
				}
			})
	}
	
	function check() {
		
		var isuse ;
		for (var i = 0; i < store.getCount() - 1; i++)
		{
			isuse = store.getAt(i).get('isUse');
			if(isuse == 'Y')
			{
				Ext.get('btnAdd').dom.disabled = true
				Ext.get('btnUpdate').dom.disabled = true
				Ext.get('btnDelete').dom.disabled = true
				Ext.get('btnSave').dom.disabled = true
			}else{
				Ext.get('btnAdd').dom.disabled = false
				Ext.get('btnUpdate').dom.disabled = false
				Ext.get('btnDelete').dom.disabled = false
				Ext.get('btnSave').dom.disabled = false
			}
		}
	}
	 // 根据不同的状态显示不同的颜色
    function showColor(value, cellmeta, record, rowIndex, columnIndex, store) { 
    	var eventId = record.data["eventId"];   	
    	if(eventId ==0) {
    	return "<font color='red'>" + value + "</font>";
    	} 
    	else
    	{  	
    	return value;
    	}
    }
    store.load();
	store.on("load",check);
});