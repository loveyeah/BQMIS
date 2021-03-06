Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	
	
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result.workerCode) {
					// 设定工作负责人为登录人
					writeByH.setValue(result.workerCode);
					Ext.get("writeBy").dom.value = result.workerName
							? result.workerName
							: '';
					Ext.get('writeByDept').dom.value = result.deptName
							? result.deptName
							: '';
				}
			}
		});
	}
	
	function selectRecipientsByWin() {
		var args = {
			selectModel : 'signal',
			notIn : "",
			rootNode : {
				id : '-1',
				text : '合肥电厂'
			}
		}
		var person = window
				.showModalDialog(
						'../../../comm/jsp/hr/workerByDept/workerByDept2.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(person) != "undefined") {
			recipientsBy.setValue(person.workerName);
			Ext.get('recipientsByCode').dom.value = person.workerCode;
			Ext.get('recipientsByDept').dom.value = person.deptName;
		}
	}
	
	function selectOfferByWin() {
		var args = {
			selectModel : 'signal',
			notIn : "",
			rootNode : {
				id : '-1',
				text : '合肥电厂'
			}
		}
		var person = window
				.showModalDialog(
						'../../../comm/jsp/hr/workerByDept/workerByDept2.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(person) != "undefined") {
			offerBy.setValue(person.workerName);
			offerByH.setValue(person.workerCode)
		}
	}
	
	function getCurrentDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1+"-";
		y = d.getDay();
		s += (t > 9 ? "" : "0") + t +(y > 9 ? "" : "0")+ y;
		return s;
	}
	
	function checkInput()
	{
		var msg="";
		if(safeCapNo.getValue()=="")
		{msg="'安全帽编码'";}
		if(offerBy.getValue()=="")
		{
			if(msg=="") msg="'发放人'";
			else msg=msg+",'发放人'";
		}
		if(recipientsBy.getValue()=="")
		{
			if(msg=="") msg="'领用人'";
			else msg=msg+",'领用人'";
		}
		if(msg!="")
		{
		Ext.Msg.alert("提示","请输入"+msg);
		return false
		}
		else
		{
			return true;
		}
		
	}

	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'safeCapNo'
	}, {
		name : 'recipientsByDeptName'
	}, {
		name : 'recipientsBy'
	}, {
		name : 'safeCapType'
	}, {
		name : 'safeCapColor'
	}, {
		name : 'offerBy'
	}, {
		name : 'recipientsDate'
	}, {
		name : 'writeBy'
	}, {
		name : 'memo'
	}, {
		name : 'safecapOfferId'
	}]);

	var dataProxy = new Ext.data.HttpProxy({
		url : 'security/findSafeCapList.action'
	});

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);

	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
     
//	 //���幤��Ʊ�������Դ
//	 var dangerTypeStore = new Ext.data.JsonStore({
//	 url : 'workticket/getDangerTypeQueryWorkticketType.action',
//	 root : 'list',
//	 fields : [{
//	 name : 'workticketTypeName'},
//	 {name : 'workticketTypeCode'}]
//	 })
//	 dangerTypeStore.load();
//	 dangerTypeStore.on('load', function(e, records, o) {
//	 dangerTypeCbo.setValue(records[0].data.workticketTypeCode);
//	 });
//	
//	 // ����Ʊ������-��
//	 var dangerTypeCbo = new Ext.form.ComboBox({
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

	// ��ҳ
//	store.load({
//		params : {
//			start : 0,
//			limit : 10
//		}
//	});

	var fuzzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});
	var sm = new Ext.grid.CheckboxSelectionModel();
    
    function renderDate(value) {
        if (!value) return "";
        var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
//        var reTime = /\d{2}:\d{2}:\d{2}/gi;
        
        var strDate = value.match(reDate);
//        var strTime = value.match(reTime);s
        if (!strDate) return "";
//        return strTime ? strDate + " " + strTime : strDate;
        return strDate;
    }
    
	var grid = new Ext.grid.GridPanel({
		region : "center",
		layout : 'fit',
		store : store,
		columns : [ sm,
		{
			header : "领用人",
			width : 75,
			sortable : true,
			dataIndex : 'recipientsBy'
		},

		{
			header : "安全帽编号",
			width : 75,
			sortable : true,
			dataIndex : 'safeCapNo'
//			hidden : true
		},

		{
			header : "安全帽规格",
			width : 75,
			sortable : true,
			dataIndex : 'safeCapType'
//			hidden : true
		}, {
			header : "安全帽颜色",
			width : 75,
			sortable : true,
			dataIndex : 'safeCapColor'
//			hidden : true
		}, {
			header : "发放人",
			width : 75,
			sortable : true,
			dataIndex : 'offerBy'
//			hidden : true
		}, {
			header : "发放日期",
			width : 75,
			sortable : true,
			dataIndex : 'recipientsDate',
			renderer : renderDate
//			hidden : true
		}, {
			header : "填写人",
			width : 75,
			sortable : true,
			dataIndex : 'writeBy'
//			hidden : true
		}, {
			header : "备注",
			width : 75,
			sortable : true,
			dataIndex : 'memo'
//			hidden : true
		}],
		sm : sm,
		stripeRows : true,
		autoSizeColumns : true,
		autoScroll : true,
		enableColumnMove : false,
		viewConfig : {
			forceFit : true
		},

		tbar : ["领用人或安全帽编码查询：",fuzzy,{
			text : "查询",
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
		layout : "border",
		// layout : "fit",
		items : [grid]
	});

	// -------------------
	var wd = 180;

	var safecapOfferId = {
		id : "safecapOfferId",
		xtype : "textfield",
		fieldLabel : 'ID',
		value : '自动生成',
		width : wd,
		readOnly : true,
		name : 'safecapOfferId'

	}
	
//	var hiddenTypeId = {
//        id : "hiddenTypeId",
//        xtype : "hidden",
//        value: '0',
//        name : 'dangerType.dangerTypeId'
//    }
	
	var safeCapNo = new Ext.form.TextField({
		id : "safeCapNo",
		xtype : "textfield",
		fieldLabel : '安全帽编号',
		allowBlank : false,
		width : wd,
		name : 'safecap.safeCapNo'

	});

//	var workticketTypeCode = new Ext.form.ComboBox({
//		id : "workticketTypeCode",
////		xtype : "textfield",
//		allowBlank : true,
//		triggerAction : 'all',
//		store :dangerTypeStore,
//	    fieldLabel : '����Ʊ����',
//	    displayField : 'workticketTypeName',
//	    valueField : 'workticketTypeCode',
//		name : 'dangerType.workticketTypeCode',
//		hiddenName:'dangerType.workticketTypeCode',
//		mode : 'local',
//	    readOnly : true,
//		width : wd
//	})
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

	var safeCapType = {
		id : "safeCapType",
		xtype : "numberfield",
		fieldLabel : '安全帽规格',
		name : 'safecap.safeCapType',
		width : wd
	}

	var safeCapColor = {
		id : "safeCapColor",
		xtype : "textfield",
		fieldLabel : '安全帽颜色',
		name : 'safecap.safeCapColor',
		width : wd
	}
	
	var offerBy = new Ext.form.TriggerField({
		fieldLabel : '发放人',
		//width : 108,
		id : "offerBy",
		displayField : 'text',
		valueField : 'id',
		hiddenName : 'offerBy',
		blankText : '请选择',
		emptyText : '请选择',
		maxLength : 100,
		anchor : '76.5%',
		allowBlank : false,
		readOnly : true
	});
	offerBy.onTriggerClick = selectOfferByWin;
	var offerByH = new Ext.form.Hidden({
		name : 'safecap.offerBy'
	})

	var recipientsDate = {
		id : "recipientsDate",
		xtype : "textfield",
		fieldLabel : '发放日期',
		name : 'safecap.recipientsDate',
		readOnly : true,
		allowBlank : false,
		width : wd,
		value : getCurrentDate()
	}
	
	var memo = {
		id : "memo",
		xtype : "textarea",
		fieldLabel : '备注',
		name : 'safecap.memo',
		width : wd
	}
	
	var writeBy = {
		id : "writeBy",
		xtype : "textfield",
		fieldLabel : '填写人',
		name : 'writeBy',
		allowBlank : false,
		readOnly : true,
		width : wd
	}
	
	var writeByH = new Ext.form.Hidden({
		name : 'safecap.writeBy'
	})
	
	var writeByDept = {
		id : "writeByDept",
		xtype : "textfield",
		fieldLabel : '填写人部门',
		name : 'writeByDept',
		allowBlank : false,
		readOnly : true,
		width : wd
	}
	
	var recipientsBy = new Ext.form.TriggerField({
		fieldLabel : '领用人',
		// width : 108,
		id : "recipientsBy",
		displayField : 'text',
		valueField : 'id',
		hiddenName : 'recipientsBy',
		blankText : '请选择',
		emptyText : '请选择',
//		maxLength : 100,
		anchor : '76.5%',
		allowBlank : false,
		readOnly : true
	});
	recipientsBy.onTriggerClick = selectRecipientsByWin;
	
	var recipientsByCode = {
		id : "recipientsByCode",
		xtype : "textfield",
		fieldLabel : '领用人工号',
		name : 'safecap.recipientsBy',
		readOnly : true,
		allowBlank : false,
		width : wd
	}
	
	var recipientsByDept = {
		id : "recipientsByDept",
		xtype : "textfield",
		fieldLabel : '领用人部门',
		name : 'recipientsByDept',
		readOnly : true,
		allowBlank : false,
		width : wd
	}
	
	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		title : '增加/修改安全帽信息',
		items : [safecapOfferId, recipientsBy,recipientsByCode,recipientsByDept,safeCapNo, safeCapType, safeCapColor,
				offerBy,offerByH,recipientsDate,memo,writeBy,writeByH,writeByDept]

	});
	
	var win = new Ext.Window({
		width : 400,
		height : 450,
		buttonAlign : "center",
		items : [myaddpanel],
		layout : 'fit',
		resizable : false,
        modal : true,
		closeAction : 'hide',
		buttons : [{
			text : "保存",
			iconCls : 'save',
			handler : function() {
				var myurl = "";
				var safecapOfferId = Ext.get("safecapOfferId").dom.value;
				if (safecapOfferId == "自动生成" ) {
				   myurl = "security/addSafeCapInfo.action";
				} else {
				   myurl = "security/updateSafeCapInfo.action";
					
				}
				if(!checkInput()) return;
				myaddpanel.getForm().submit({
					method : 'POST',
					url : myurl,
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("注意", o.msg);
						fuzzy.setValue("");
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
			iconCls:'cancer',
			handler : function() {
				win.hide();
			}
		}]

	});

	function queryRecord() {
		store.load({
			params : {
				start : 0,
				limit : 18,
				fuzzytext : fuzzy.getValue()
			}
		});
	}
	
    function addRecord() {
        myaddpanel.getForm().reset();
        win.show();
        getWorkCode();
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
                // 显示该行记录��ʾ���м�¼
                myaddpanel.getForm().loadRecord(record);
                Ext.Ajax.request({
                	url : 'security/getDeptNameAndWorkCode.action',
					method : 'post',
	                params : {
						workName : Ext.get('writeBy').dom.value
					},
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						// 设定工作负责人为登录人
						Ext.get('writeByDept').dom.value = result[2];
					}
				});
				Ext.Ajax.request({
                	url : 'security/getDeptNameAndWorkCode.action',
					method : 'post',
	                params : {
						workName : Ext.get('recipientsBy').dom.value
					},
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						// 设定工作负责人为登录人
						Ext.get('recipientsByCode').dom.value = result[0];
						Ext.get('recipientsByDept').dom.value = result[2];
					}
				});
                // 设置日期数据�����������
                Ext.get("recipientsDate").dom.value = renderDate(Ext.get("recipientsDate").dom.value);
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
				if (member.safecapOfferId) {
					ids.push(member.safecapOfferId);
					names.push(member.safeCapNo);
				} else {
					store.remove(store.getAt(i));
				}
			}
			Ext.Msg.confirm("删除", "是否确定删除安全帽编号为'" + names + "'的记录？", function(
					buttonobj) {
				if (buttonobj == "yes") {
					Ext.lib.Ajax.request('POST', 'security/deleteSafeCapInfo.action', {
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
	
	store.load({
		params : {
			start : 0,
			limit : 18
		}
	});
	
});