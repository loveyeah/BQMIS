Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {   
	var args = window.dialogArguments;
	var storeData = args.storeData;
	var strWorkticketNo = args.workticketNo;// "";//"H1200904D003";
	var strWorkticketTypeCode = args.workticketTypeCode;//"1"; 
	var strLocationName = args.locationName;// "xx";
	var strBlockCode = args.blockCode;//"";
	var deleteRecords = [];   
	 
	
	// 定义grid
	var ContentRecord = Ext.data.Record.create([{ 
		name : "id",
		type : 'float'
	}, {
		name : 'workticketNo'
	}, {
		name : 'locationName'
	}, {
		name : 'locationId',
		type : 'float'
	}, {
		name : 'equName'
	}, {
		name : 'frontKeyId',
		type : 'float'
	}, {
		name : 'frontKeyDesc'
	}, {
		name : 'attributeCode'
	}, {
		name : 'backKeyDesc'
	}, {
		name : 'backKeyId',
		type : 'float'
	}, {
		name : 'worktypeName'
	}, {
		name : 'worktypeId',
		type : 'float'
	}, {
		name : 'isreturn'
	}, {
		name : 'flagDesc'
	}, {
		name : 'line'
	}]);  
	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'workticket/getContentWorkticketList.action'
		}),
		reader : new Ext.data.JsonReader({
			root : "list",
			totalProperty : "totalCount"
		}, ContentRecord),
		sortInfo : {
			field : "line",
			direction : "ASC"
		}
	});  
	var sm = new Ext.grid.CheckboxSelectionModel({ 
		id:'check',
		width : 25,
		listeners:{ 
		}
	}); 
	// 前关键字检索Store
	var beforeKeyDescStore = new Ext.data.JsonStore({
		url : "workticket/getContentAllWithComm.action",
		fields : [{
			name : 'contentKeyId'
		}, {
			name : 'contentKeyName'
		}]
	}); 
	beforeKeyDescStore.baseParams = {
		keyType : "1",
		workticketTypeCode : strWorkticketTypeCode
	};  
   
	/** 区域选择处理 */
	function locationSelect() {
		var url = "selectLocation.jsp?op=many&blockCode=" + strBlockCode+"&workticketTypeCode="+strWorkticketTypeCode;
		var location = window.showModalDialog(url, window,
				'dialogWidth=400px;dialogHeight=300px;status=no');
		if (typeof(location) != "undefined") { 
			var record = grid.selModel.getSelected() 
			record.set("locationName", location.name);
			grid.getView().refresh();  
		}
	} ; 
	/** 设备选择处理 */
	function equSelect() {
		var url = "../../../../../comm/jsp/equselect/selectAttribute.jsp?";  
		url += "op=many"; 
		var equ = window.showModalDialog(url, window,
				'dialogWidth=600px;dialogHeight=400px;status=no');
		if (typeof(equ) != "undefined") { 
			var record = grid.selModel.getSelected() 
			var names = equ.name.split(",");
			var codes = equ.code.split(",");
			if(names.length > 1) 
			{
				for(var i=1;i<names.length;i++)
				{
					var currentRecord = grid.getSelectionModel().getSelected();
					var count = store.getCount(); 
					var currentIndex = currentRecord?(currentRecord.get("line")+i-1):count 
					addRecord(currentIndex,names[i],codes[i],'、','N');
				}
				resetLine();  
			}
			record.set("equName", names[0]);
			record.set("attributeCode", codes[0]); 
			record.set("isreturn",'N');
			record.set("flagDesc","、");
			grid.getView().refresh();   
		}
	};
		// 后关键字检索
	var afterKeyDescStore = new Ext.data.JsonStore({
		url : "workticket/getContentAllWithComm.action",
		fields : [{
			name : 'contentKeyId'
		}, {
			name : 'contentKeyName'
		}]
	});
	afterKeyDescStore.baseParams = {
		keyType : "2",
		workticketTypeCode : strWorkticketTypeCode
	}; 
	afterKeyDescStore.load(); 
		// 工作类型
	var worktypeNameStore = new Ext.data.JsonStore({
		url : "workticket/getContentWorktypeName.action",
		fields : [{
			name : 'worktypeId'
		}, {
			name : 'worktypeName'
		}]
	});
	worktypeNameStore.baseParams = {
		workticketTypeCode : strWorkticketTypeCode
	}; 
	worktypeNameStore.load();
	// 标点符号  modify by fyyang 081230 默认为句号
	var flagIdStore = new Ext.data.JsonStore({
		url : "workticket/getContentFlagId.action",
		fields : [{
			name : 'flagId'
		}, {
			name : 'flagName'
		}]
	}); 
	var buttonBar = new Ext.Toolbar({
		layout:'fit',
		items:['->',{
			text : "插入",
			tooltip :'不选中任何行,在最后增加.',
			id:"btnGridAdd", 
			iconCls : 'add',
			handler : function(){
				var currentRecord = grid.getSelectionModel().getSelected();
				var count = store.getCount(); 
				var currentIndex = currentRecord?currentRecord.get("line")-1:count 
				addRecord(currentIndex); 
				resetLine();  
				sm.selectRow(currentIndex);
				grid.startEditing(currentIndex, 2);
			}
		},'-', {
			text : "删除", 
			id:"btnGridDelete",
			iconCls : 'delete',
			handler : deleteRecord 
		 
		},'-',{
			text : "预览工作内容", 
			id:'btnGridViewUpdate',
			iconCls : 'view', 
			handler: function(){
				grid.stopEditing(); 
				selectWorkticketContent();
			} 
		},'-',{
			text : "保存", 
			id:'btnSave',
			iconCls : 'save', 
			handler: function(){
				grid.stopEditing();
				if(strWorkticketNo != null && strWorkticketNo !="")
				{
					var modifyRec = grid.getStore().getModifiedRecords();
					if (modifyRec.length > 0 || deleteRecords.length > 0) { 
						saveModifyRecords();
					} 
				}
				var ro = new Object();
			 	var contentRec = [];
			 	for(var i=0;i<store.getCount();i++)
			 	{
			 		contentRec.push(store.getAt(i).data);
			 	}
			 
			 	ro.str = Workticket.spellWorkContent(store);  
			 	ro.storeData = contentRec ; 
			 	window.returnValue = ro;
			 	window.close();
			}
		},'-',{
			text : "取消", 
			id:'btnGridCancel',
			iconCls : 'reflesh', 
			handler : function() {
				if (deleteRecords.length > 0
						|| store.getModifiedRecords().length > 0) {
					if (confirm("确定要放弃所做的修改吗?")) {
						window.close();
					}
				}
				else
				{
					window.close();
				}
			}
			
		}] 
	});  
	 
	var cm = new Ext.grid.ColumnModel([sm, 
//	new Ext.grid.RowNumberer({
//		header : '行号'
//	}),
	{
		header : "前关键词", // add by fyyang 090106
		width : 80,
		hidden:true,
		dataIndex : 'frontKeyDesc',
		editor : new Ext.form.ComboBox({
			typeAhead : true,
			editable : true,
			triggerAction : 'all',
			listClass : 'x-combo-list-small',
			name : 'frontKeyDesc',
			store : beforeKeyDescStore,
			valueField : "contentKeyName",
			displayField : "contentKeyName",
			mode : 'remote',
			maxLength : 100,
			selectOnFocus : true,
			listeners : {
				render : function(f) {
					f.el.on('keyup', function(e) {  
						f.setValue(Ext.get("frontKeyDesc").dom.value); 
					});
				}
			}
		})
	}, {
		header : "工作区域/地点",
		id : 'locationArea',
		width : 140,
		hidden:true,
		dataIndex : 'locationName',
		editor : new Ext.form.TriggerField({
			width : 320,
			maxLength : 100,
			allowBlank : true,
			onTriggerClick : locationSelect
		})
	}, {
		header : "工作设备",
		width : 140,
		dataIndex : 'equName',
		editor : new Ext.form.TriggerField({
			width : 320,
			allowBlank : false,
			onTriggerClick : equSelect,
		listeners : {
			render : function(f) {
				f.el.on('keyup', function(e) {
					grid.getSelectionModel().getSelected().set(
							"attributeCode", 'temp');
				});
			}
		}
		})
	}, {
		header : "设备功能编码",
		width : 100,
		dataIndex : 'attributeCode',
		renderer:function(value){
			if(value == null)
			value = "temp";
           return "<span style='color:gray;'>"+value+"</span>";

		}
	}, {
		header : "后关键词",
		width : 80,
		dataIndex : 'backKeyDesc',
		//editor:afterKeyDescCbo
		editor : new Ext.form.ComboBox({
			typeAhead : true,
			triggerAction : 'all',
			name:'backKeyDesc',
			listClass : 'x-combo-list-small',
			store : afterKeyDescStore,
			valueField : "contentKeyName",
			displayField : "contentKeyName",
			mode : 'local',
			selectOnFocus : true,
			listeners : {
				render : function(f) {
					f.el.on('keyup', function(e) {  
						f.setValue(Ext.get("backKeyDesc").dom.value); 
					});
				}
			}

		})
	}, {
		header : "工作类型",
		width : 100,
		dataIndex : 'worktypeName',
		editor : new Ext.form.ComboBox({
			typeAhead : true,
			triggerAction : 'all',
			name:'worktypeName',
			listClass : 'x-combo-list-small',
			store : worktypeNameStore,
			valueField : "worktypeName",
			displayField : "worktypeName",
			mode : 'local',
			selectOnFocus : true,
			listeners : {
				render : function(f) {
					f.el.on('keyup', function(e) {  
						f.setValue(Ext.get("worktypeName").dom.value); 
					});
				}
			}
		})
	}, {
		header : '标点',
		align : 'center',
		width : 40,
		dataIndex : 'flagDesc',
		editor : new Ext.form.ComboBox({
			typeAhead : true,
			triggerAction : 'all',
			listClass : 'x-combo-list-small',
			store : flagIdStore,
			valueField : "flagName",
			displayField : "flagName",
			mode : 'remote',
			selectOnFocus : true
		}),
		align : 'left'
	}, {
		header : '换行?',
		width : 60,
		dataIndex : 'isreturn',
		renderer : function(v) {
			if (v == "N") {
				return "不换行";
			}
			return "换行";
		},
		editor : new Ext.form.ComboBox({
			typeAhead : true,
			triggerAction : 'all',
			listClass : 'x-combo-list-small',
			store : [['Y', '换行'], ['N', '不换行']],
			valueField : "value",
			displayField : "text",
			mode : 'local',
			selectOnFocus : true
		})
	}
	]); 
	// 定义GridPanel
	var grid = new Ext.grid.EditorGridPanel({  
		height : 290, 
		region:'center',
		autoScroll : true,
		store : store, 
		clicksToEdit : 1,
		cm : cm,
		sm : sm,
		bbar : buttonBar, 
		autoSizeColumns : true, 
		enableColumnHide : true,
		enableColumnMove : false 
	}); 
	cm.defaultSortable = false;
	function resetLine(){
		for(var j=0;j<store.getCount();j++)
		{ 
			var temp = store.getAt(j); 
			temp.set("line",j+1); 
		}
	}
	grid.on("rowcontextmenu", function(g, i, e) {
		e.stopEvent(); 
		// 右键菜单
		var menu = new Ext.menu.Menu({
			id : 'fuctionMenu',
			items : [new Ext.menu.Item({
				text : '移至顶行',
				iconCls : 'priorStep',
				handler : function() {  
				    if(i!=0)
				    {
						var record = grid.getStore().getAt(i); 
						grid.getStore().remove(record);
						grid.getStore().insert(0,record);  
						resetLine();
				    }
				}
			}),new Ext.menu.Item({
				text : '上移',
				iconCls : 'priorStep',
				handler : function() {  
				    if(i!=0)
				    {
						var record = grid.getStore().getAt(i); 
						grid.getStore().remove(record);
						grid.getStore().insert(i-1,record);  
						resetLine();
				    }
				}
			}), new Ext.menu.Item({
				text : '下移',
				iconCls : 'nextStep',
				handler : function() {
					 if((i+1)!=grid.getStore().getCount())
				    {
						var record = grid.getStore().getAt(i);
						grid.getStore().remove(record);
						grid.getStore().insert(i+1,record);
						resetLine();
				    }
				}
			}), new Ext.menu.Item({
				text : '移至最后',
				iconCls : 'nextStep',
				handler : function() {
					 if((i+1)!=grid.getStore().getCount())
				    {
						var record = grid.getStore().getAt(i);
						grid.getStore().remove(record);
						grid.getStore().insert(grid.getStore().getCount(),record);
						resetLine();
				    }
				}
			})]
		});
		var coords = e.getXY();
		menu.showAt([coords[0], coords[1]]);
	});
	
	/*增加*/
	function addRecord(currentIndex,equName,equCode,flagDesc,isReturn){ 
		var rec = new ContentRecord({
			id : null,
			workticketNo : strWorkticketNo,
			locationName : strLocationName,
			locationId : null,
			equName : equName?equName:'',
			frontKeyId : null,
			frontKeyDesc:'',
			attributeCode:equCode?equCode:'',
			backKeyDesc:'',
			backKeyId:null,
			worktypeName:'',
			worktypeId: strWorkticketTypeCode,
			flagDesc:flagDesc?flagDesc:'。',
			isreturn:isReturn?isReturn:'Y',
			line:null
		});  
		grid.stopEditing();
		store.insert(currentIndex, rec);  
		selectWorkticketContent(); 
	};
	
	function saveModifyRecords()
	{   
			Ext.Msg.wait("正在保存数据,请等待...");
			var modifyRec = grid.getStore().getModifiedRecords();
			var modifyRecords = new Array(); 
			for (var i = 0; i < modifyRec.length; i++) { 
				modifyRecords.push(modifyRec[i].data); 
			}
			Ext.Ajax.request({
				url : 'workticket/modifyContents.action',
				method : 'post',
				params : {
					workticetNo : strWorkticketNo,
					addOrUpdateRecords : Ext.util.JSON.encode(modifyRecords),
					deleteRecords : deleteRecords.join(",")
				},
				success : function(result, request) {
					Ext.MessageBox.alert('提示', '操作成功！');  
					store.reload();
					store.rejectChanges();
					deleteRecords=[]; 
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('提示', '操作失败！');
					store.reload();
					store.rejectChanges();
					deleteRecords=[];
				}
			}) ;
	}
	
	// 删除数据
	function deleteRecord(obj,e) { 
		grid.stopEditing();
		var sm = grid.getSelectionModel();
		var selections = sm.getSelections();
		var len = selections.length; 
		if (len == 0) {
			Ext.Msg.alert("提示","请选中您要删除的记录!");
		} else { 
			for (var i = 0; i < len; i++) {
				var record = selections[i]; 
				if(record.get("id") != "" && record.get("id") != null) 
				{
					deleteRecords.push(record.get("id")); 
				}
				store.remove(selections[i]);
				store.getModifiedRecords().remove(record);
			} 
			resetLine();
			selectWorkticketContent(); 
		} 
	};  
	// 加载数据时触发
	store.on('load', function() {   
		selectWorkticketContent();
	});  
	if (strWorkticketNo== "" && storeData != null) {
		for (var i = 0; i < storeData.length; i++) {
			store.add(new ContentRecord(storeData[i]));
		}
		//selectWorkticketContent();
	}else
	{ 
		if(strWorkticketNo != null && strWorkticketNo != "")
		{ 
			store.load({
				params : {
					workticketNo : strWorkticketNo
				}
			});  
		}
	}
	
    // 选择工作内容方法
	function selectWorkticketContent() { 
		contentDescribe.setValue(Workticket.spellWorkContent(store)); 
	}
	// 工作内容查询
	var contentDescribe = new Ext.form.TextArea({
				id : "workticketContent",  
				region:'south',
				height : 120,  
				readOnly : true
			});  
	// 显示的主窗口
	var mainPanel = new Ext.Viewport({
		layout : 'border',
		border : false,
		items : [grid,contentDescribe]
	});  
});
