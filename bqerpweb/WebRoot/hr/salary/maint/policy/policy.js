Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'policyId'
	}, {
		name : 'stationId'
	}, {
		name : 'increaseRange'
	}, {
		name : 'memo'
	},{
		name : 'stationName'	
	}]);

	var dataProxy = new Ext.data.HttpProxy({
		url : 'hr/findAllPolicy.action'
	});

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);

	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});

	var fuzzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});

	var sm = new Ext.grid.CheckboxSelectionModel();
	
	//add by ming_lian 2010-8-12
	/*增加行*/
	function addRows(currentIndex,stationId,stationName){ 
   
		var rec = new MyRecord({
			policyId : '',
			stationId : stationId?stationId:'',
			stationName :'',
			increaseRange:'',
			memo:''
		});  
		grid.stopEditing();
		store.insert(currentIndex, rec);
		sm.selectRow(currentIndex);
		var currentRecord = grid.getSelectionModel().getSelected();
		currentRecord.set("stationName",stationName?stationName:'');
		
	};
	
	//modify by ming_lian 2010-8-12
	var stationName = new Ext.form.ComboBox({
		name : 'hstationName',
		id : 'hstationName',
//		store : new Ext.data.SimpleStore({
//			fields : ['id', 'name'],
//			data : [[]]
//		}),
//		mode : 'remote',
		
		hiddenName:'stationName',
		
		editable : false,
			anchor : "80%",
			triggerAction : 'all',
				onTriggerClick : function() {
					var url = "../../../../hr/salary/maint/policy/selectStation.jsp";
					
				var isSelection = window.showModalDialog(url,window,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');				
				if (typeof(isSelection) != "undefined") {
					var selectionsName = [];
					var selectionsId = [];
					for (var i = 0; i < isSelection.length; i += 1) {
								var member = isSelection[i].data;
								if (member.stationName) {
									selectionsName.push(member.stationName); 
								}
								if (member.stationId) {
									selectionsId.push(member.stationId); 
								}
								}
					if(selectionsId.length ==0 ){
						Ext.Msg.alert('提示信息', "请选择所需要的岗位!");
					}
					if(selectionsId.length ==1 ){
						grid.getSelectionModel().getSelected().set("stationName",selectionsName[0]);
						grid.getSelectionModel().getSelected().set("stationId",selectionsId[0]);	
						Ext.form.ComboBox.superclass.setValue.call(Ext
			.getCmp('hstationName'), selectionsName[0]);
					}
					if(selectionsId.length >1 ){
						for(var i=0;i<selectionsId.length;i++)
						{
							var currentRecord = grid.getSelectionModel().getSelected();
							//rowNo = store.indexOf(currentRecord);
							var count = store.getCount();
							var currentIndex = count; 
							if(i==0){
								currentRecord.set("stationName",selectionsName[i]);
								currentRecord.set("stationId",selectionsId[i]);
							}else{
								addRows(currentIndex,selectionsId[i],selectionsName[i]);

								RecordNow = store.getAt(store.getCount()-1);
								
							}
						} 
		     			grid.startEditing(currentIndex, 2);
					}
				}
				}

	});
	
	

	var grid = new Ext.grid.EditorGridPanel({
		region : "center",
		layout : 'fit',
		store : store,
		clicksToEdit:1,
		columns : [sm, new Ext.grid.RowNumberer({
			header : '序号',
			width : 35,
			align : 'left'
		}), {
			header : "倾斜政策ID",
			width : 0.5,
			sortable : true,
			dataIndex : 'policyId',
		    hidden : true
		}, {
			header : "岗位ID",
			width : 75,
			hidden : true,
			sortable : true,
			dataIndex : 'stationId'
		}, {
			header : "岗位名称",
			width : 75,
			sortable : true,
			dataIndex : 'stationName',
			editor: stationName
		}, {
			header : "增加幅度（%）",
			width : 75,
			sortable : true,
			dataIndex : 'increaseRange',
			editor : new Ext.form.NumberField({})
		}, {
			header : "备注",
			width : 75,
			hidden : false,
			sortable : true,
			dataIndex : 'memo',
			editor : new Ext.form.TextField({})
		}],
		sm : sm,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : true
		},
		tbar : ['岗位名称：', fuzzy, {
			text : "查询",
			iconCls : 'query',
			handler : queryRecord
		}, {
			text : "新增",
			iconCls : 'add',
			handler : addRecord
		}, {
			text : "保存",
			iconCls : 'save',
			handler : saveRecord
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

	//grid.on("rowdblclick", updateRecord);
	// ---------------------------------------

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [grid]
	});

	// -------------------
	var wd = 200;

	// 倾斜政策ID
	var policyId = new Ext.form.Hidden({
		id : "policyId",
		name : 'policyId'
	});

	// 工作岗位
	var empStationstore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'empInfoManage.action?method=getstation'
						}),
				reader : new Ext.data.JsonReader({
						// id : "plant"
						}, [{
							name : 'id'
						}, {
							name : 'text'
						}])
			});
	empStationstore.load();
	var stationId = new Ext.form.ComboBox({
		fieldLabel : "岗位<font color='red'>*</font>",
		name : 'stationId',
//		xtype : 'combo',
		editable : true,
		id : 'station',
		store : empStationstore,
		valueField : "id",
		displayField : "text",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'stationId',
		triggerAction : 'all',
		selectOnFocus : true,
		width : wd,
		blankText : '请选择',
		emptyText : '请选择'
	});

	// 增加幅度
	var increaseRange = new Ext.form.NumberField({
		id : 'increaseRange',
		name : 'increaseRange',
		fieldLabel : '增加幅度（%）',
		allowDecimals : false,
		width : wd,
		allowNegative : false
		//		decimalPrecision : 
	})
	// 备注
	var memo = new Ext.form.TextArea({
		id : 'memo',
		name : 'memo',
		fieldLabel : '备注',
		width : wd
	})
	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'right',
		labelWidth : 100,
		closeAction : 'hide',
		title : '增加/修改运行岗位倾斜政策',
		items : [policyId, stationId, increaseRange, memo]
	});

	function checkInput() {
		var msg = "";
		if (stationId.getValue() == null || stationId.getValue() == "") {
			msg = "'岗位'";
		}
		if (msg != "") {
			Ext.Msg.alert("提示", "请输入" + msg);
			return false
		} else {
			return true;
		}
	}

	var win = new Ext.Window({
		width : 350,
		height : 240,
		buttonAlign : "center",
		items : [myaddpanel],
		layout : 'fit',
		closeAction : 'hide',
		resizable : false,
		modal : true,
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
				var myurl = "";
				if (policyId.getValue() == null || policyId.getValue() == "") {
					myurl = "hr/addPolicy.action";
				} else {
					myurl = "hr/updatePolicy.action";
				}
				if (!checkInput())
					return;
				myaddpanel.getForm().submit({
					method : 'POST',
					url : myurl,
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("注意", o.msg);
						if (o.msg.indexOf("成功") != -1) {
							queryRecord();
							win.hide();
						}
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

	function queryRecord() {
		store.baseParams = {
			stationName : fuzzy.getValue()
		}
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}

	function addRecord() {
		//delete by ming_lian 2010-8-12
		/*myaddpanel.getForm().reset();
		win.show();
		myaddpanel.setTitle("增加运行岗位倾斜政策");*/
		var rec = new MyRecord({
			policyId : '',
			stationId : '',
			stationName :'',
			increaseRange:'',
			memo:''
		});  
		var count = store.getCount();
		var currentIndex =count;
		grid.stopEditing();
		store.insert(count, rec); 
		sm.selectRow(currentIndex);
		grid.startEditing(currentIndex, 2);
	}

	//delete by ming_lian 2010-8-12
	/*function updateRecord() {
		if (grid.selModel.hasSelection()) {
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				win.show();
				var record = grid.getSelectionModel().getSelected();
				myaddpanel.getForm().reset();
				myaddpanel.getForm().loadRecord(record);
//				stationLevelId.setValue(record.get("stationLevelId"));
//				stationLevel.setValue(record.get("stationLevel"));
//				stationLevelName.setValue(record.get("stationLevelName"));
//				retrieveCode.setValue(record.get("retrieveCode"));

				myaddpanel.setTitle("修改运行岗位倾斜政策");
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}*/
	
	function saveRecord() {
		
		var modifyRec = grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0) {
			if (!confirm("确定要保存修改吗?"))
				return;
			var updateData = new Array();
			for (var i = 0; i < modifyRec.length; i++) {
				updateData.push(modifyRec[i].data);
			}
			Ext.Ajax.request({
				url : 'hr/savePolicy.action',
				method : 'post',
				params : {
					isUpdateData : Ext.util.JSON.encode(updateData)
				},
				success : function(result, request) {				
					store.reload();
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('提示信息', '更改发生错误！')
				}
			})
		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}
	
	function deleteRecord() {
		var records = grid.selModel.getSelections();
		var ids = [];
		var names = [];
		if (records.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {

			for (var i = 0; i < records.length; i += 1) {
				var member = records[i];
				if (member.get("policyId")) {
					ids.push(member.get("policyId"));
				} else {
					store.remove(member);
					return;
				}
			}
			Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(buttonobj) {
				if (buttonobj == "yes") {
					Ext.lib.Ajax.request('POST',
							'hr/deletePolicy.action', {
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

	queryRecord();
});