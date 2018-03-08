Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
	var bview;
	
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		t = d.getHours();
		s += (t > 9 ? "" : "0") + t + ":";
		t = d.getMinutes();
		s += (t > 9 ? "" : "0") + t + ":";
		t = d.getSeconds();
		s += (t > 9 ? "" : "0") + t;
		return s;
	}
	function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";
		// 初始化时间
		Year = DateIn.getYear();
		Month = DateIn.getMonth() + 1;
		Day = DateIn.getDate();
		CurrentDate = Year + "-";
		if (Month >= 10) {
			CurrentDate = CurrentDate + Month + "-";
		} else {
			CurrentDate = CurrentDate + "0" + Month + "-";
		}
		if (Day >= 10) {
			CurrentDate = CurrentDate + Day;
		} else {
			CurrentDate = CurrentDate + "0" + Day;
		}
		return CurrentDate;
	}
	var enddate = new Date();
	var startdate = enddate.add(Date.MONTH, -1);
	startdate = startdate.getFirstDateOfMonth();
	var sdate = ChangeDateToString(startdate);
	var edate = ChangeDateToString(enddate);

	var beginDate = new Ext.form.TextField({
		id : 'beginDate',
		fieldLabel : "事故发生日期",
		name : 'happenDate',
		style : 'cursor:pointer',
		width : 85,
		value : sdate,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true,
					onpicked : function() {
						if (endDate.getValue() != "") {
							if (beginDate.getValue() == ""
									|| beginDate.getValue() > endDate
											.getValue()) {
								Ext.Msg.alert("提示", "必须小于后一日期");
								return;
							}
						}
					},
					onclearing : function() {
						beginDate.markInvalid();
					}
				});
			}
		}
	});

	var endDate = new Ext.form.TextField({
		id : 'endDate',
		fieldLabel : "至",
		name : 'happenDate',
		style : 'cursor:pointer',
		width : 85,
		value : edate,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true,
					onpicked : function() {
						if (beginDate.getValue() == ""
								|| beginDate.getValue() > endDate.getValue()) {
							Ext.Msg.alert("提示", "必须大于前一日期");
							return;
						}
					},
					onclearing : function() {
						endDate.markInvalid();
					}
				});
			}
		}
	});

	var equNameQuery = new Ext.form.TextField({
		id : 'equNameQuery',
		fieldLabel : '设备名称',
		readOnly : true,
		anchor : "85%",
		listeners : {
			focus : equNameSelect
		}
	});
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'jysgId'
	}, {
		name : 'accidentTitle'
	}, {
		name : 'equName'
	}, {
		name : 'equId'
	}, {
		name : 'happenDate'
	}, {
		name : 'handleDate'
	}, {
		name : 'reasonAnalyse'
	}, {
		name : 'handleStatus'
	}, {
		name : 'memo'
	}, {
		name : 'annex'
	}]);
	var dataProxy = new Ext.data.HttpProxy({
		url : 'productionrec/findSGDJList.action'
	});

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);
	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
	// 分页
	store.load({
		params : {
			start : 0,
			limit : 18
		}
	});

	var sm = new Ext.grid.CheckboxSelectionModel({
			// singleSelect : true
	});
	var grid = new Ext.grid.GridPanel({
		region : "center",
		store : store,
		columns : [sm, new Ext.grid.RowNumberer({
			header : '序号',
			width : 35,
			align : 'left'
		}), {
			header : "ID",
			width : 110,
			sortable : true,
			hidden : true,
			align : 'left',
			dataIndex : 'jysgId'
		}, {
			header : "事故主题",
			width : 80,
			sortable : true,
			align : 'left',
			dataIndex : 'accidentTitle'
		}, {
			header : "设备名称",
			width : 130,
			sortable : true,
			align : 'left',
			dataIndex : 'equName'
		}, {
			header : "发生日期",
			width : 100,
			sortable : true,
			align : 'left',
			dataIndex : 'happenDate'
		}, {
			header : "处理日期",
			width : 100,
			sortable : true,
			align : 'left',
			dataIndex : 'handleDate'
		}, {
			header : "处理结果",
			width : 130,
			sortable : true,
			align : 'left',
			dataIndex : 'handleStatus',
			renderer : function(v) {
				if (v == '0') {
					return "未处理";
				}
				if (v == '1') {
					return "处理中";
				}
				if (v == '2') {
					return "已处理";
				}
			}
		}, {
			header : "原因分析",
			width : 120,
			sortable : true,
			align : 'left',
			dataIndex : 'reasonAnalyse'
		}, {
			header : "备注",
			width : 130,
			sortable : true,
			align : 'left',
			dataIndex : 'memo'
		}, {
			header : "equId",
			width : 130,
			sortable : true,
			hidden : true,
			align : 'left',
			dataIndex : 'equId'
		},{
			header : "附件",
			width : 75,
			sortable : true,
			dataIndex : 'annex',
			renderer:function(v){
				if(v !=null && v !='')
				{ 
					var s =  '<a href="#" onclick="window.open(\''+v+'\');return  false;">[查看]</a>';
					return s;
				}
			} 
			
		}],
		sm : sm,
		tbar : ["事故开始日期:", beginDate, "~", endDate,'-', "设备名称:", equNameQuery, {
			text : "查询",
			iconCls : 'query',
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
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});

	grid.on("rowdblclick", updateRecord);

	// ---------------------------------------
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});
	// -------------------
	// 定义FORM
	var jysgId = new Ext.form.TextField({
		id : "jysgId",
		fieldLabel : '事故ID',
		xtype : "textfield",
		readOnly : true,
		name : 'sgdj.jysgId',
		anchor : "85%"
	});

	var accidentTitle = new Ext.form.TextField({
		id : "accidentTitle",
		fieldLabel : '事故主题',
		xtype : "textfield",
		name : 'sgdj.accidentTitle',
		anchor : "85%"

	});

	var equId = new Ext.form.TextField({
		id : "equId",
		fieldLabel : 'equId',
		xtype : "textfield",
		readOnly : true,
		name : 'sgdj.equId',
		anchor : "85%"
	});

	var equName = new Ext.form.TextField({
		id : 'equName',
		fieldLabel : '设备名称',
		readOnly : true,
		anchor : "85%",
		name : 'sgdj.equName',
		listeners : {
			focus : equSelect
		}
	});

	var happenDate = new Ext.form.TextField({
		id : 'happenDate',
		fieldLabel : "发生日期",
		name : 'sgdj.happenDate',
		style : 'cursor:pointer',
		anchor : "70%",
		value : getDate(),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d 00:00:00',
					dateFmt : 'yyyy-MM-dd HH:mm:ss',
					alwaysUseStartDate : true,
					onpicked : function() {
						if (handleDate.getValue() != "") {
							if (happenDate.getValue() == ""
									|| happenDate.getValue() > handleDate.getValue()) {
								Ext.Msg.alert("提示", "必须小于后一日期");
								happenDate.setValue("");
								return;
							}
						}
					},
					onclearing : function() {
						beginDate.markInvalid();
					}
				});
			}
		}
	});

	var handleDate = new Ext.form.TextField({
		id : 'handleDate',
		fieldLabel : "处理日期",
		name : 'sgdj.handleDate',
		style : 'cursor:pointer',
		anchor : "70%",
		value : getDate(),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d 00:00:00',
					dateFmt : 'yyyy-MM-dd HH:mm:ss',
					alwaysUseStartDate : true,
					onpicked : function() {
						if (happenDate.getValue() == ""
								|| happenDate.getValue() > handleDate.getValue()) {
							Ext.Msg.alert("提示", "必须大于前一日期");
							handleDate.setValue("");
							return;
						}
					},
					onclearing : function() {
						endDate.markInvalid();
					}
				});
			}
		}
	});

	var handleStatus = new Ext.form.ComboBox({
		fieldLabel : '处理结果',
		store : new Ext.data.SimpleStore({
			fields : ['value', 'text'],
			data : [['0', '未处理'], ['1', '处理中'], ['2', '已处理']]
		}),
		id : 'handleStatus',
		name : 'handleStatus',
		valueField : "value",
		displayField : "text",
		value : '0',
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'sgdj.handleStatus',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		anchor : "85%"
	});

	var reasonAnalyse = new Ext.form.TextArea({
		id : "reasonAnalyse",
		height : 50,
		fieldLabel : '原因分析',
		name : 'sgdj.reasonAnalyse',
		anchor : "85%"
	});

	// 附件
	var annexFile = {
		id : "annexFile",
		xtype : 'fileuploadfield',
		isFormField : true,
		name : "annexFile",
		fieldLabel : '附件',
		height : 21,
		anchor : "100%",
		buttonCfg : {
			text : '浏览...',
			iconCls : 'upload-icon'
		}
	}

	// 查看
	var btnView = new Ext.Button({
		id : 'btnView',
		text : '查看',
		handler : function() {
			window.open(bview);
		}
	});
	btnView.setVisible(false);

	var memo = new Ext.form.TextArea({
		id : "memo",
		height : 50,
		fieldLabel : '备注',
		name : 'sgdj.memo',
		anchor : "85%"
	});

	var myaddpanel = new Ext.FormPanel({
		title : '绝缘事故增加/修改',
		height : '100%',
		layout : 'form',
		frame : true,
		fileUpload : true,
		labelAlign : 'center',
		items : [{
			border : false,
			hidden : true,
			layout : 'form',
			columnWidth : 1,
			labelWidth : 70,
			items : [jysgId]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 1,
			labelWidth : 70,
			items : [accidentTitle]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 1,
			labelWidth : 70,
			items : [equName]
		}, {
			border : false,
			layout : 'column',
			items : [{
				columnWidth : .5,
				layout : 'form',
				border : false,
				labelWidth : 70,
				items : [happenDate]

			}, {
				columnWidth : .5,
				layout : 'form',
				labelWidth : 70,
				border : false,
				items : [handleDate]
			}]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 1,
			labelWidth : 70,
			items : [{
				columnWidth : .5,
				layout : 'form',
				border : false,
				labelWidth : 70,
				items : [handleStatus]

			}]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 1,
			labelWidth : 70,
			items : [reasonAnalyse]
		}, {
			layout : 'column',
			border : false,
			items : [{
				columnWidth : 0.78,
				border : false,
				labelWidth : 70,
				layout : "form",
				items : [annexFile]
			}, {
				columnWidth : 0.22,
				border : false,
				layout : "form",
				items : [btnView]

			}]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 1,
			labelWidth : 70,
			items : [memo]
		}, {
			border : false,
			hidden : true,
			layout : 'form',
			columnWidth : 1,
			labelWidth : 70,
			items : [equId]
		}]
	});

	function checkInput() {
		var msg = "";
		if (accidentTitle.getValue() == "") {
			msg = "'事故主题";
		}
		if (equName.getValue() == "") {
			if (msg != "") {
				msg = msg + ",'设备名称'";
			} else {
				msg = "'设备名称'";
			}
		}
		if (happenDate.getValue() == "") {
			if (msg != "") {
				msg = msg + ",'发生日期'";
			} else {
				msg = "'发生日期'";
			}
		}
		if (handleDate.getValue() == "") {
			if (msg != "") {
				msg = msg + ",'处理日期'";
			} else {
				msg = "'处理日期'";
			}
		}
		if (handleStatus.getValue() == "") {
			if (msg != "") {
				msg = msg + ",'处理结果'";
			} else {
				msg = "'处理结果'";
			}
		}
		if (msg != "") {
			Ext.Msg.alert("提示", "请输入" + msg + "!");
			return false
		} else {
			return true;
		}
	}

	var win = new Ext.Window({
		width : 600,
		height : 380,
		buttonAlign : "center",
		items : [myaddpanel],
		layout : 'fit',
		closeAction : 'hide',
		draggable : true,
		modal : true,
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
				var myurl = "";
				if (Ext.get("jysgId").dom.value == "") {
					myurl = "productionrec/addSGDJInfo.action";
				} else {
					myurl = "productionrec/updateSGDJInfo.action";
				}
				if (!checkInput())
					return;
				myaddpanel.getForm().submit({
					method : 'POST',
					url : myurl,
					params : {
						filePath : Ext.get("annexFile").dom.value
					},
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("提示", o.msg);
						equNameQuery.setValue("");
						queryRecord();
						win.hide();
						 bview="";
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

	// 查询
	function queryRecord() {
		store.load({
			params : {
				start : 0,
				limit : 18,
				equName : equNameQuery.getValue(),
				sDate : Ext.get('beginDate').dom.value,
				eDate : Ext.get('endDate').dom.value
			}
		});
	}

	function addRecord() {
		myaddpanel.getForm().reset();
		win.setPosition(100, 50);
		win.show();
		myaddpanel.setTitle("增加绝缘事故登记");

	}

	function updateRecord() {
		if (grid.selModel.hasSelection()) {
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				var record = grid.getSelectionModel().getSelected();
				win.show();
				myaddpanel.getForm().reset();
				myaddpanel.getForm().loadRecord(record);
				if (record.get("annex") != null && record.get("annex") != "") {
					bview = record.get("annex");
					btnView.setVisible(true);
					Ext.get("annexFile").dom.value = bview.replace('/power/upload_dir/productionrec/','');
				} else {
					btnView.setVisible(false);
				}

				myaddpanel.setTitle("修改绝缘事故登记");
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
				if (member.jysgId) {
					ids.push(member.jysgId);
					names.push(member.accidentTitle);
				} else {
					store.remove(store.getAt(i));
				}
			}
			Ext.Msg.confirm("删除", "是否确定删除选中的记录？", function(
					buttonobj) {
				if (buttonobj == "yes") {
					Ext.lib.Ajax.request('POST',
							'productionrec/deleteSGDJInfo.action', {
								success : function(action) {
									Ext.Msg.alert("提示", "删除成功！")
									queryRecord();
								},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
							}, 'ids=' + ids);
				}
			});
		}

	}

	/** 设备选择处理 */
	function equSelect() {
		var url = "../../../../comm/jsp/equselect/selectAttribute.jsp?";
		//url += "op=many";
		var equ = window.showModalDialog(url, window,
				'dialogWidth=600px;dialogHeight=400px;status=no');
		if (typeof(equ) != "undefined") {
			equId.setValue(equ.code);
			equName.setValue(equ.name);
		}
	};
	/** 设备选择处理(查询条件） */
	function equNameSelect() {
		var url = "../../../../comm/jsp/equselect/selectAttribute.jsp?";
		//url += "op=many";
		var equ = window.showModalDialog(url, window,
				'dialogWidth=600px;dialogHeight=400px;status=no');
		if (typeof(equ) != "undefined") {
			equNameQuery.setValue(equ.name);
		}
	};
	queryRecord();
});