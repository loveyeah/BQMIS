Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() { 
	// 从上个页面传过来的数据
	var register = parent.Ext.getCmp('tabPanel').register;
	// 设置工作票编号
	var strWorkticketNo = register.workticketNo;
	// 工作票类型编码(tab1页中所选择的工作票类型)
	var strWorkticketTypeCode = register.workticketTypeCode;
	// 所属机组或系统（从上个tab页（工作票明细中获取））
	var strBlockCode = register.equAttributeCode; 
	//add by fyyang 工作地点
	var strLocationName=register.locationName;
	var deleteRecords = [];
//	// 选择工作班成员
//	var selectMember = new Ext.form.TextField({ 
//		fieldLabel : "选择工作班成员",
//		id : "select",
//		readOnly : true, 
//		name : "select",
//		width : 80,
//		value : "选择",
//		style : {
//			'text-decoration' : 'underline',
//			'text-align' : 'center'
//		},
//		listeners : {
//			focus : function() {
//				var url = "workticketMemberSelectWindow.jsp?workticketNo="
//						+ strWorkticketNo+"&chargeDept="+register.chargeDept;
//				this.blur();
//				var selectedMembers = window
//						.showModalDialog(
//								url,
//								null,
//								'dialogWidth=700px;dialogHeight=520px;center=yes;help=no;resizable=no;status=no;');
//
//				if (typeof(selectedMembers) != "undefined") {
//					personDescribe.setValue(selectedMembers.names);
//					personCount.setValue(selectedMembers.quantity);
//				}
//			}
//		}
//	});
//
//	// 总人数
//	var personCount = new Ext.form.TextField({
//		fieldLabel : "总人数",
//		id : "personCount",
//		readOnly : false,
//		name : "personCount",
//		align : "right",
//		width : 80
//	});
//
//	// 具体人名显示
//	var personDescribe = new Ext.form.TextArea({
//		id : "describeId",  
//		hideLabel : true,
//		labelWidth:10,
//		readOnly : true, 
//		height : 50, 
//		anchor : '100%',
//		listeners : {
//			render : function getContentWorkticketMember() {
//				Ext.Ajax.request({
//					method : 'POST',
//					url : "workticket/getContentWorkticketMember.action",
//					params : {
//						// 工作票号
//						workticketNo : strWorkticketNo
//					},
//					success : function(result, request) {
//						if (result.responseText) {
//							var o = eval("(" + result.responseText + ")");
//							personCount.setValue(o.count);
//							personDescribe.setValue(o.content);
//						}
//					}
//				});
//			}
//		}
//	});
	
	// 工作内容查询
	var contentDescribe = new Ext.form.TextArea({ 
		id : "workticketContent", 
		hideLabel : true, 
		height : 140,
		anchor : '100%',
		style : {
				"margin-top" : "0px" 
			},
		allowBlank : true,
		readOnly : true
	}); 
	// 底部工具条
	var bbar = new Ext.Toolbar({
		height:20,
		//align:'center',
		items : [new Ext.Toolbar.Fill(), {
			id : 'lastBtn',
			text : '上一步',
			iconCls:'priorStep',
			handler : function() {
			register.toTab1();
			}
		}
		,{
			id : 'nextBtn',
			text : '下一步',
			iconCls:'nextStep',
			handler : function() {
				register.toTab3();
			}
		}
		]
	}) ;
//	var content = new Ext.Panel({
//		layout : 'column',
//		width : 500,
//		style:{
//			"margin-top" : "5px" 
//		},
//		border : false,
//		height : 30,
//		items : [
//			{
//			labelWidth : 91,
//			columnWidth : .4,
//			layout : 'form',
//			border : false,
//			items : selectMember
//		}, 
//			{
//			labelWidth : 43,
//			columnWidth : .3,
//			layout : 'form',
//			border : false,
//			items : personCount
//		}, {
//			xtype : 'label',
//			style : {
//				'fontSize' : 12,
//				'padding-top' : '4%'
//			},
//			text : "人"
//		}]
//	});
 
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : "id" ,type: 'float'
	}, {
		name : 'workticketNo'
	}, {
		name : 'locationName'
	}, {
		name : 'locationId' ,type: 'float'
	}, {
		name : 'equName'
	}, {
		name : 'frontKeyId',type: 'float'
	}, {
		name : 'frontKeyDesc'
	}, {
		name : 'attributeCode'
	}, {
		name : 'backKeyDesc'
	}, {
		name : 'backKeyId',type: 'float'
	}, {
		name : 'worktypeName'
	}, {
		name : 'worktypeId'  ,type: 'float'
	}, {
		name : 'isreturn'
	},{
		name:'flagDesc' 
	}, 
	{
		name : 'line'
	}]); 
	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'workticket/getContentWorkticketList.action'
		}),
		reader : new Ext.data.JsonReader({
			root : "list",
			totalProperty : "totalCount"
		}, MyRecord),
		sortInfo : {
			field : "line",
			direction : "ASC"
		}
	}); 
	var sm = new Ext.grid.CheckboxSelectionModel({ 
		id:'check',
		width : 35,
		listeners:{
			
		}
	}); 
	// 填写工作票内容TextField
	var addWorkticket = new Ext.Button({
		id : "add", 
		name : "add", 
		iconCls:'list',
		text : "由现有票生成", 
		handler:
		 function() {
				this.blur();
				var args = new Object();
				//args.btnType = "2";
				args.workticketNo = strWorkticketNo;
				args.workticketTypeCode = strWorkticketTypeCode;
				var object = window
						.showModalDialog(
								'selectHisWorkticket.jsp',
								args,
								'dialogWidth=800px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');
				if (typeof(object) == 'object') {
					// 将已终结票对应的数据塞到现在工作票对应的表中
					Ext.Ajax.request({
						method : Constants.POST,
						url : 'workticket/updateContent.action',
						// 参数
						params : {
							lastWorkticketNo : object.refWorkticketNo,
							workticketNo : strWorkticketNo
						},
						success : function(action) { 
							store.load({
								params : { 
									workticketNo : strWorkticketNo
								}
							})

							selectWorkticketContent();
							register.updateSafetyAndDanger();
						},
						failure : function() {
						}

					});  
			}
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
		var url = "selectLocation.jsp?op=many&blockCode=" + strBlockCode;
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
//				grid.startEditing(currentIndex, 2);
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
		items:['填写工作票内容:', {
			text : "插入",
			tooltip :'不选中任何行,在最后增加.',
			id:"btnGridAdd", 
			iconCls : 'insert',
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
		 
		},'-',  {
			text : "保存",  
			id:'btnGridSave',
			iconCls : 'save', 
			handler: saveModifyRecords
		},'-',{
			text : "取消", 
			id:'btnGridCancel',
			iconCls : 'reflesh', 
			handler: function(){
				store.reload();
				store.rejectChanges();
			}
			
		},'-',{
			text : "预览工作内容", 
			id:'btnGridViewUpdate',
			iconCls : 'view', 
			handler: function(){
				if(store.getModifiedRecords().length>0)
				selectWorkticketContent();
			} 
		},'->', addWorkticket] 
	});  
	var cm = new Ext.grid.ColumnModel([sm, new Ext.grid.RowNumberer({
		header : '行号'
	}), {
		header : "前关键词", // add by fyyang 090106
		width : 40,
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
		width : 40,
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
		width : 40,
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
		width : 40,
		dataIndex : 'attributeCode',
		renderer:function(value){
           return "<span style='color:gray;'>"+value+"</span>";

		}
	}, {
		header : "后关键词",
		width : 30,
		dataIndex : 'backKeyDesc',
		editor : new Ext.form.ComboBox({
			typeAhead : true,
			triggerAction : 'all',
			name:'backKeyDesc',
			listClass : 'x-combo-list-small',
			store : afterKeyDescStore,
			valueField : "contentKeyName",
			displayField : "contentKeyName",
			mode : 'remote',
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
		width : 30,
		dataIndex : 'worktypeName',
		editor : new Ext.form.ComboBox({
			typeAhead : true,
			triggerAction : 'all',
			name:'worktypeName',
			listClass : 'x-combo-list-small',
			store : worktypeNameStore,
			valueField : "worktypeName",
			displayField : "worktypeName",
			mode : 'remote',
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
		width : 15,
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
		width : 15,
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
		bbar:bbar,
		autoScroll:true,  
		store : store,  
		//plugins : [checkColumn],
	    clicksToEdit:1,
		cm : cm,
		sm : sm,
		autoSizeColumns : true,
		tbar : buttonBar, 
		autoSizeColumns : true,
		autoExpandColumn : 'locationName',
		enableColumnHide : true,
		enableColumnMove : false,
		viewConfig : {
			forceFit : true 
		}
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
		var rec = new MyRecord({
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
		var modifyRec = grid.getStore().getModifiedRecords(); 
		if (modifyRec.length > 0 || deleteRecords.length>0) {
			if(!confirm("确定要保存修改吗?")){
				return false; 	
			}
			Ext.Msg.wait("正在保存数据,请等待...");
			var modifyRecords = new Array(); 
			for (var i = 0; i < modifyRec.length; i++) { 
				modifyRecords.push(modifyRec[i].data); 
			}
			Ext.Ajax.request({
				url : 'workticket/modifyContents.action',
				method : 'post',
				params : {
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
			})
		}
		else
		{
			Ext.MessageBox.alert('提示', '您没有做任何修改！');
		}
	}
	
	// 删除数据
	function deleteRecord() {
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
		this.baseParams = {
			workticketNo : strWorkticketNo
		};
	}); 
	// 数据加载时，加载工作票内容
	store.on('load', selectWorkticketContent);
	// 加载数据
	store.load({
		params : {
			workticketNo : strWorkticketNo
		}
	});
	
	  
	var topPanel = new Ext.Panel({
		autoHight:true,
		autoWidth:true,
		layout:'form',
		items:[
		//content, 
		//personDescribe,
			{
			xtype:'label',
			style : {
				'fontSize' : 12,
				'padding-bottom' : '0px'
			}, 
			text:'工作票内容预览：'
		},contentDescribe]
	}); 
	// 显示的主窗口
	new Ext.Viewport({
		layout : 'border', 
		border : false,
		items : [
		{
			region : 'north', 
			height : 150,
			split:true, 
			layout:'fit',
			items:[topPanel] 
		}, {
			region : 'center',
			xtype : 'panel', 
			layout : 'fit',   
			border : false,
			items : [ grid]
		}] 
	});
 
	// 选择工作内容方法
	function selectWorkticketContent() {
		contentDescribe.setValue(Workticket.spellWorkContent(store));
	} 
}); 


