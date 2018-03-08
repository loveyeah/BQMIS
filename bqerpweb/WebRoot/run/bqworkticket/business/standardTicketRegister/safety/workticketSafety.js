Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif'; 

/*根据危险点ID设置Radio的选中*/
	function setRadioByDangerId(safeId)
	{
		 var radios = document.getElementsByName("selectOne");
		for(var i=0;i<radios.length;i++)
		 {
		 	if(radios[i].value == safeId)
		 	{
		 		radios[i].checked = true;
		 	}
		 }
	}
Ext.onReady(function() {
	Ext.QuickTips.init();

		var deleteRecords = [];
	// 从前面的页面得到
	var register = parent.Ext.getCmp('tabPanel').register;
	// 工作票编号
	var strWorkticketNo = register.workticketNo;
	// 工作票类型编号
	var strWorkticketType = register.workticketTypeCode;
	// 工具条
	var tbar = new Ext.Toolbar({
		items : ['双击修改安全措施内容', '-', '确认工作票填写完整后请点击：', '-', 
		{id : 'myUpdate',
			text : '修改',
			iconCls:'update',
			handler :dbclickHandler}
			,'-',{
			id : 'upload',
			text : '上报',
			iconCls:'upcommit',
			handler : reportBtn
		}, '-', {
			id : 'pageBrowse',
			text : '票面浏览',
			iconCls:'pdfview',
			handler : workticketPrint
		}]
	})

	// 上报处理
	function reportBtn() {
		var isReport;
		var workticketNo = strWorkticketNo;
		Ext.lib.Ajax.request('POST', 'workticket/getReportWorkticket.action', {
			success : function(result, request) {
				object = eval("(" + result.responseText + ")");
				isReport = object.workticketStausId;
				if (isReport !="1"
						&& isReport != "5") {
					Ext.Msg.alert(Constants.SYS_REMIND_MSG, "只有未上报和已退回的票允许上报");
				} else {
					// 弹出确认上报的信息
							var args=new Object();
				args.workticketNo=workticketNo;
				args.entryId=register.workFlowNo;
				  var check = window.showModalDialog('../reportSign/reportSign.jsp',
                args, 'dialogWidth=700px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');
               
                if(check)
                {
                // 上报成功, 更新上报列表
				register.report();
                }
				
				}
			},
			failure : function() {
				Ext.Msg.alert(Constants.ERROR, "查询失败！");
			}
		}, 'workticketNo=' + strWorkticketNo);
	}

	// 底部工具条
	var bbar = new Ext.Toolbar({
		items : [new Ext.Toolbar.Fill(), {
			id : 'returnbtn',
			iconCls:'priorStep',
			text : '上一步',
			handler : function() {
				var tabPanel = parent.Ext.getCmp('tabPanel');
				tabPanel.setActiveTab('tabBaseInfo');
			}
		},{
			id : 'returnbtn',
			text : '下一步',
			iconCls:'nextStep',
			handler : function() {
				var tabPanel = parent.Ext.getCmp('tabPanel');
				tabPanel.setActiveTab('tabDanger');
			}
		}]
	})

	// ↓↓********************安措画面********************↓↓//

	// ↓↓*************增加/修改工作票安全措施内容********↓↓
	var wd = 180;
//	// 前关键词combox的store
	var frontKeywordStore = new Ext.data.JsonStore({
		url : "workticket/getKeywordList.action",
		root : 'list',
		fields : [{
			name : "safetyKeyName"
		}, {
			name : "safetyKeyId"
		}]
	});

	// 前关键词
	var cbxFrontKeyword = new Ext.form.ComboBox({
		fieldLabel : "前关键词",
		editable : true,
		allowBlank : true,
		triggerAction : 'all',
		width : wd,
		id : "frontKeyId",
		name : "frontKeyword",
		store : frontKeywordStore,
		displayField : 'safetyKeyName',
		mode : 'local',
		emptyText : '请选择前关键词...',
		readOnly : false
//		listeners : {
//			render : function(f) {
//				f.el.on('keyup', function(e) {
//					f.setValue(Ext.get("frontKeyword").dom.value);
//				});
//			}
//		}
	});
//
 // 设备和压板选择菜单
	var menu = new Ext.menu.Menu({
		id : 'mainMenu',
		items : [ // 定义菜单中的元素
		{
			text : '选择设备',
			handler : equSelect
		}, {
			text : '选择压板',
			handler : pressboardSelect
		}]
	});
//
	// 设备名称内容
	var triEquName = new Ext.form.TriggerField({
		fieldLabel : '设备名称内容<font color="red">*</font>',
		width : wd,
		id : "equName",
		name : "equName",
		allowBlank : false,
		//readOnly : false,
		onTriggerClick : function(e) {
			e.preventDefault();
			menu.showAt(e.getXY());
		},
		listeners : {
			render : function(f) {
				f.el.on('keyup', function(e) {
					popGrid.getSelectionModel().getSelected().set(
							"attributeCode", 'temp');
				});
			}
		}
	});
//
//	// 设备名称内容
//	var hideAttributeCode = new Ext.form.Hidden({
//		id : 'hideAttributeCode',
//		name : 'safety.attributeCode'
//	});
//
	// 后关键词combox的store
	var backKeywordStore = new Ext.data.JsonStore({
		url : "workticket/getKeywordList.action",
		root : 'list',
		width : wd,
		fields : [{
			name : "safetyKeyName"
		}, {
			name : "safetyKeyId"
		}]
	});

	// 后关键词
	var cbxBackKeyword = new Ext.form.ComboBox({
		fieldLabel : "后关键词",
		editable : true,
		allowBlank : true,
		triggerAction : 'all',
		width : wd,
		store : backKeywordStore,
		id : "backKeyId",
		name : "backKeyword",
		displayField : 'safetyKeyName',
		//valueField : 'safetyKeyId',
		mode : 'local',
		emptyText : '请选择后关键词...',
		readOnly : false
	});
//
	// 标点的store
	var flagIdStore = new Ext.data.JsonStore({
		url : "workticket/getFlagIdList.action",
		root : 'list',
		fields : [{
			name : "flagName"
		}, {
			name : "flagId"
		}],
		listeners : {
			load : selectFullpoint
		}
	});
	// 标点
//	var cbxFlagId = new Ext.form.ComboBox({
//		fieldLabel : "标点",
//		allowBlank : true,
//		triggerAction : 'all',
//		store : flagIdStore,
//		width : wd,
//		id : "flagId",
//		hiddenName : "safety.flagId",
//		displayField : 'flagName',
//		valueField : 'flagId',
//		mode : 'local',
//		emptyText : '请选择标点...',
//		readOnly : true
//	});
//
//	// 操作序号
//	var numOperationOrder = new Ext.form.NumberField({
//		fieldLabel : "操作序号",
//		id : "operationOrder",
//		name : "safety.operationOrder",
//		maxLength : 10,
//		width : wd
//	});
//
//	// 工作票编号隐藏域
//	var hiddenWorkticketNo = new Ext.form.Hidden({
//		id : "workticketNo",
//		name : "safety.workticketNo",
//		value : strWorkticketNo
//	});
//	// ID隐藏域, 修改时需要传入
//	var hiddenId = new Ext.form.Hidden({
//		id : "id",
//		name : "safetyDetailId",
//		value : ""
//	});
//	// 安措编码隐藏域
//	var hiddenSafetyCode = new Ext.form.Hidden({
//		id : "safetyCode",
//		name : "safety.safetyCode"
//	});
//	// 增加修改区分隐藏域
//	var hiddenIsAdd = new Ext.form.Hidden({
//		id : "isAdd",
//		value : true
//	});
//	// 弹出窗口panel
//	var myaddpanel = new Ext.FormPanel({
//		frame : true,
//		labelAlign : 'right',
//		items : [hiddenWorkticketNo, hiddenId, hiddenSafetyCode,
//				cbxFrontKeyword, triEquName, hideAttributeCode, cbxBackKeyword,
//				cbxFlagId, numOperationOrder]
//	});

//	// 弹出窗口
//	var maintWin = new Ext.Window({
//		title : '工作票安全措施内容维护',
//		width : 350,
//		height : 250,
//		buttonAlign : "center",
//		modal : true,
//		resizable : false,
//		items : [myaddpanel],
//		buttons : [{
//			text : '保存',
//			iconCls : "save",
//			handler : function() {
//				var myurl = "";
//				if (hiddenIsAdd.getValue()) {
//					myurl = "workticket/addSafetyBaseInfo.action";
//				} else {
//					myurl = "workticket/updateSafetyBaseInfo.action";
//				}
//
//				myaddpanel.getForm().submit({
//					method : 'POST',
//					url : myurl,
//					params : {
//						'safety.frontKeyId' : cbxFrontKeyword.value,
//						'safety.backKeyId' : cbxBackKeyword.value
//					},
//					success : function(form, action) {
//						var o = eval("(" + action.response.responseText + ")");
//						Ext.Msg.alert("注意", o.msg);
//						// 刷新原页面
//						refreshPopWin(hiddenSafetyCode.getValue());
//						maintWin.hide();
//					},
//					faliue : function() {
//						Ext.Msg.alert('错误', '出现未知错误.');
//					}
//				});
//			}
//		}, {
//			text : '取消',
//			iconCls : "cancer",
//			handler : function() {
//				maintWin.hide();
//			}
//		}],
//		layout : 'fit',
//		closeAction : 'hide'
//	});
	function resetLine(){
		for(var j=0;j<popDs.getCount();j++)
		{ 
			var temp = popDs.getAt(j); 
			temp.set("operationOrder",j+1); 
		}
	}
	/*增加*/
	function addRecord(currentIndex,equName,equCode,flagDesc){ 
   
		var rec = new MyPopRecord({
			id : '',
			frontKeyword : '',
			equName :equName?equName:'',
			attributeCode:equCode?equCode:'',
			backKeyword:'',
			flagDesc:flagDesc?flagDesc:'。',
			operationOrder:''
		});  
		popGrid.stopEditing();
		popDs.insert(currentIndex, rec);  
		
	};
	
	
	/** 设备选择处理 */
		function equSelect() {
		var url = "../../../../../comm/jsp/equselect/selectAttribute.jsp?";  
		url += "op=many"; 
		var equ = window.showModalDialog(url, window,
				'dialogWidth=600px;dialogHeight=400px;status=no');
		if (typeof(equ) != "undefined") { 
			var record = popGrid.selModel.getSelected();
			var names = equ.name.split(",");
			var codes = equ.code.split(",");
			if(names.length > 1) 
			{
				for(var i=1;i<names.length;i++)
				{
					var currentRecord = popGrid.getSelectionModel().getSelected();
					var count = popDs.getCount(); 
					var currentIndex = currentRecord?(currentRecord.get("operationOrder")+i-1):count 
					addRecord(currentIndex,names[i],codes[i],'。');
				}
				resetLine(); 
     			popGrid.startEditing(currentIndex, 2);
			}
			
			record.set("equName", names[0]);
			record.set("attributeCode", codes[0]); 
			record.set("flagDesc","。");
			popGrid.getView().refresh();   
		}
	};

	/** 压板选择处理 */
	function pressboardSelect() {
		var url = "selectPressboard.jsp?";
          url += "op=many"; 
		var pressboard = window.showModalDialog(url, window,
				'dialogWidth=400px;dialogHeight=400px;status=no');
		if (typeof(pressboard) != "undefined") {
		var record = popGrid.selModel.getSelected();
			var names = pressboard.name.split(",");
			var codes = pressboard.code.split(",");
			if(names.length > 1) 
			{
				for(var i=1;i<names.length;i++)
				{
					var currentRecord = popGrid.getSelectionModel().getSelected();
					var count = popDs.getCount(); 
					var currentIndex = currentRecord?(currentRecord.get("operationOrder")+i-1):count 
					addRecord(currentIndex,names[i],codes[i],'。');
				}
				resetLine(); 
     			popGrid.startEditing(currentIndex, 2);
			}
			
			record.set("equName", names[0]);
			record.set("attributeCode", codes[0]); 
			record.set("flagDesc","。");
			popGrid.getView().refresh();   
		}
	}

	/** 选择句号作为默认标点符号 */
	function selectFullpoint(store, records, options) {
		for (var i = 0; i < records.length; i++) {
			if ("。" == records[i].data.flagName) {
				//cbxFlagId.setValue(records[i].data.flagId);
			}
		}
	}
	// 载入前关键词
	frontKeywordStore.load({
		params : {
			workticketTypeCode : strWorkticketType,
			keyType : "1"
		}
	});
	// 载入后关键词
	backKeywordStore.load({
		params : {
			workticketTypeCode : strWorkticketType,
			keyType : "2"
		}
	});
	// 载入标点符号
	flagIdStore.load();
	// ↑↑*************增加/修改工作票安全措施内容********↑↑

	// ↓↓*****************其它工作票画面*****************↓↓//
	// grid中的store
//	var ds = new Ext.data.JsonStore({
//		root : 'list',
//		url : 'workticket/getSafetyContent.action',
//		fields : ['safetyCode', 'safetyDesc', 'safetyContent',
//				'safetyType']
//	})
	var MyDsRecord=Ext.data.Record.create([
	{name : 'safetyCode'},
    {name : 'safetyDesc'},
	{name : 'safetyContent'},
	{name:'safetyType'},
	{name:'line'}
	]);
	var ds = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url : 'workticket/getSafetyContent.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, MyDsRecord),
		groupField : 'safetyDesc',
		sortInfo:'',
		remoteGroup:true
//		groupField : 'line',
//		groupOnSort : true,
//		sortInfo : {
//			field : 'safetyDesc',
//			direction : "DESC"
//		}
	});


	ds.load({
		params : {
			workticketNo : strWorkticketNo,
			workticketTypeCode:strWorkticketType
		}
	})
 

	// 由已终结票生成时, 更新安措内容
	register.changeSaftyHandler = function() {
		ds.reload();
	}

//	// 单选列
//	var sm = new Ext.grid.CheckboxSelectionModel({
//		singleSelect : true
//	})
	
	var sm = new Ext.grid.RowSelectionModel({
		singleSelect : true,
		listeners:{
			rowselect :function(obj,rowIndex,rec){
				setRadioByDangerId(rec.get("safetyCode")) ;
			}
		}
	});

	// 列内容
	var item_cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(),
		{
		
		//sortable : true,
		header : '安全措施描述',
		dataIndex : 'safetyDesc',
		align : 'left', 
		renderer : function change(val) {
					return ' <span style="white-space:normal;">' + val
							+ ' </span>';
				}
	}, {
		//sortable : true,
		header : '安全措施内容',
		dataIndex : 'safetyContent',
		align : 'left',
		width : 450,
		renderer : function(value) {
			if (value) {
				return value.replace(/\r/g, "<br/>");
			}

		}
	}])

		/*取得选中的安措编码 add by fyyang 090318 */
	function getSafetyCode(){
		 var radios = document.getElementsByName("selectOne");
		 for(var i=0;i<radios.length;i++)
		 {
		 	if(radios[i].checked)
		 	{
		 		return radios[i].value;
		 	}
		 }
		 return null;
	};
	
	/*取得选中的危安措信息*/
	function getSafetyData(safetyCode)
	{
		for(var i=0;i<ds.getCount();i++)
		{
			var rec = ds.getAt(i);
			if(rec.get("safetyCode") == safetyCode)
			{
				return rec;
			}
		}
		return null;
	}
	
//		/*根据安措编码设置Radio的选中*/
//	function setRadioBySafetyCode(safetyCode)
//	{
//		 var radios = document.getElementsByName("selectOne");
//		for(var i=0;i<radios.length;i++)
//		 {
//		 	if(radios[i].value == safetyCode)
//		 	{
//		 		radios[i].checked = true;
//		 	}
//		 }
//	}
	// gridPanel
	var grid = new Ext.grid.GridPanel({
		ds : ds,
		cm : item_cm, 
		sm:sm,
		tbar : tbar,
		bbar : bbar,
		autoWidth : true,
		autoScroll : true,
		enableColumnMove : false,
		enableColumnHide : true,
		border : false,
			view : new Ext.grid.GroupingView({
			forceFit : false,
			enableGroupingMenu : false,
			hideGroupedColumn :true,
			showGroupName :false, 
			autoFill :true, 
			startCollapsed : false, 
			toggleGroup : function(group, expanded){  
		        this.grid.stopEditing();    
		         group = Ext.getDom(group);  
		         var clickDangerId = group.getElementsByTagName("input")[0].value;
		         if(getSafetyCode() == clickDangerId)
		         {
		         	expanded = true;
		         }
		         var gel = Ext.fly(group);  
		         expanded = expanded !== undefined ?  
		                expanded : gel.hasClass('x-grid-group-collapsed');   
		        this.state[gel.dom.id] = expanded;  
		        gel[expanded ? 'removeClass' : 'addClass']('x-grid-group-collapsed');  
		     },   
groupTextTpl : '<input type="radio"  value="{[values.rs[0].data.safetyCode]}" name="selectOne"></input><span onclick="setRadioByDangerId(\'{[values.rs[0].data.safetyCode]}\')">{[values.rs[0].data.safetyDesc]}</span>'
		})
	});
	grid.on("rowdblclick",dbclickHandler);
	
		ds.on('load', function() {
		var radios = document.getElementsByName("selectOne");
		if (radios) {
			if(radios[0]!=null)
			{
			radios[0].checked = true;
			}
		}
	});

	// 设定布局器及面板
	var layout = new Ext.Viewport({
		layout : 'fit',
		border : true,
		items : [{
			layout : 'fit',
			margins : '0 0 0 0',
			region : 'center',
			autoScroll : true,
			items : grid
		}]
	});

	// ***********弹出画面*********** //
	// grid中的store
//	var popDs = new Ext.data.JsonStore({
//		root : 'list',
//		url : 'workticket/getSafetyDetail.action',
//		fields : ['id', 'workticketNo','safetyCode','frontKeyword', 'equName', 'attributeCode', 'backKeyword',
//				'flagDesc','operationOrder']
//	})
	var MyPopRecord = Ext.data.Record.create([{
		name : "id" 
	}, {
		name : 'frontKeyword' 
	}, {
		name : 'equName'
	}, {
		name : 'attributeCode'
	}, {
		name : 'backKeyword'
	}, {
		name : 'flagDesc'
	}, {
		name : 'operationOrder'
	}]); 
	var popDs = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'workticket/getSafetyDetail.action'
		}),
		reader : new Ext.data.JsonReader({
			root : "list"
		}, MyPopRecord)
//		sortInfo : {
//			field : "operationOrder",
//			direction : "ASC"
//		}
	}); 

	//popDs.setDefaultSort('frontKeyword', 'desc');

	// 单选列
	var popSm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	})

	// 列内容
	var popCm = new Ext.grid.ColumnModel([popSm, 
		 {
		sortable : true,
		header : 'ID',
		dataIndex : 'id',
		align : 'left',
		hidden:true,
		width : 50
	  }, 
		{
		sortable : true,
		header : '前关键词',
		width : 120,
		dataIndex : 'frontKeyword',
		align : 'left',
		editor:cbxFrontKeyword
	}, {
		sortable : true,
		header : '设备名称内容',
		dataIndex : 'equName',
		align : 'left',
		width : 120,
		editor :triEquName
	}, {
		sortable : true,
		header : '设备功能编码',
		dataIndex : 'attributeCode',
		align : 'left',
		width : 100,
		renderer:function(value){
           return "<span style='color:gray;'>"+value+"</span>";

		}
	}, {
		sortable : true,
		header : '后关键词',
		dataIndex : 'backKeyword',
		align : 'left',
		width : 120,
		editor:cbxBackKeyword
	}, {
		sortable : true,
		header : '标点',
		width : 50,
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
	},
	{
	    sortable : true,
		header : '排序',
		dataIndex : 'operationOrder',
		align : 'left',
		width : 50,
		editor:new  Ext.form.TextField(),
		renderer:function(value){
           return "<span style='color:gray;'>"+value+"</span>";

		}
	}
	
	])
	

	// 弹出画面工具条
	var popBar = new Ext.Toolbar({
		items : [new Ext.Toolbar.Fill(), {
			id : 'addbtn',
			text :'插入',
			iconCls :'insert',
			handler : addHandler
		}, {
			id : 'deletebtn',
			text : Constants.BTN_DELETE,
			iconCls : Constants.CLS_DELETE,
			handler : deleteHandler
		}, {
			id : 'updatebtn',
			text : '保存',
			iconCls : 'save',
			handler : updateHandler

		}, {
			id : 'closebtn',
			iconCls:"cancer",
			text : "关闭",
			handler : function() {
				// 尚未保存
				if (deleteRecords.length > 0 || popDs.getModifiedRecords().length > 0) {
					if (confirm("您所做的修改尚未保存,确定放弃修改吗?")) { 
						 deleteRecords = [];
						 popDs.rejectChanges();
						win.hide();
						
					}
				} else { 
					win.hide();
				} 
			    deleteRecords = [];
				ds.load({
					params : {
						workticketNo : strWorkticketNo,
						workticketTypeCode:strWorkticketType
					}
				});
				
			}
		}]
	})

	// 弹出文本区域
	var txtArea = new Ext.form.TextArea({
		id : 'txtArea',
		width : 586,
		height : 170,
		readOnly : true,
		value : ""
	})

	// gridPanel
	var popGrid = new Ext.grid.EditorGridPanel({
		ds : popDs,
		cm : popCm,
		sm : popSm,
		bbar : popBar,
	//	autoWidth : true,
		width:586, //modify by fyyang 090108
	    clicksToEdit:1,
		height : 200,
		autoScroll : true,
		enableColumnMove : false,
		enableColumnHide : true,
		border : false
	})
	//右键菜单
	popGrid.on("rowcontextmenu", function(g, i, e) {
		e.stopEvent(); 
		var _store = popGrid.getStore(); 
		var menu = new Ext.menu.Menu({
			id : 'fuctionMenu',
			items : [new Ext.menu.Item({
				text : '移至顶行',
				iconCls : 'priorStep',
				handler : function() {  
				    if(i!=0)
				    {
						var record = _store.getAt(i); 
						_store.remove(record);
						_store.insert(0,record);  
						resetLine();
				    }
				}
			}),new Ext.menu.Item({
				text : '上移',
				iconCls : 'priorStep',
				handler : function() {  
				    if(i!=0)
				    {
						var record = _store.getAt(i); 
						_store.remove(record);
						_store.insert(i-1,record);  
						resetLine();
				    }
				}
			}), new Ext.menu.Item({
				text : '下移',
				iconCls : 'nextStep',
				handler : function() {
					 if((i+1)!=_store.getCount())
				    {
						var record = _store.getAt(i);
						_store.remove(record);
						_store.insert(i+1,record);
						resetLine();
				    }
				}
			}), new Ext.menu.Item({
				text : '移至最后',
				iconCls : 'nextStep',
				handler : function() {
					if((i+1)!=_store.getCount())
				    {
						var record = _store.getAt(i);
						_store.remove(record);
						_store.insert(_store.getCount(),record);
						resetLine();
				    }
				}
			})]
		});
		var coords = e.getXY();
		menu.showAt([coords[0], coords[1]]);
	});

	// 弹出窗口
	var win = new Ext.Window({
		resizable : false,
		title : '安全措施',
		height : 400,
		width : 600,
		modal : true,
		closeAction : 'hide',
		closable :false,
//		listeners : {
//			'hide' : function() {
//			// 尚未保存
//				if (deleteRecords.length > 0 || popDs.getModifiedRecords().length > 0) {
//					if (confirm("您所做的修改尚未保存,确定放弃修改吗?")) { 
//						deleteRecords = [];
//						 popDs.rejectChanges();
//						win.hide();
//					}
//				} else { 
//					win.hide();
//				} 
//			    deleteRecords = [];
//				ds.load({
//					params : {
//						workticketNo : strWorkticketNo,
//						workticketTypeCode:strWorkticketType
//					}
//				});
//				
//			}
//		},
		items : [popGrid, txtArea]
	})

	// ***********处理*********** //

	// 主画面grid双击处理
	//grid.on("rowdblclick", dbclickHandler);

	/**
	 * 主画面grid双击处理
	 */
	function dbclickHandler() {
//		var records = grid.getSelectionModel().getSelections();
//		if (!records || records.length < 1) {
//			// 未选中对象
//			return;
//		}
//		//modify by fyyang 090216
//		var isRun = records[0].get("safetyType");
//		if (isRun == "N"||isRun=="repair"||isRun==null) {
//			refreshPopWin(records[0].get("safetyCode"));
//			win.show();
//		} else {
//			Ext.Msg.alert("提示", "此条安措填写时不能编辑！");
//		}

		var safetyCode = getSafetyCode();
		
		if(safetyCode!=null)
		{
			var rec=getSafetyData(safetyCode);
			var isRun=rec.get("safetyType");
			if (isRun == "N"||isRun=="repair"||isRun==null) {
			refreshPopWin(safetyCode);
			win.show();
			}else {
			    Ext.Msg.alert("提示", "此条安措填写时不能编辑！");
		     }
		}
		else
		{
			Ext.Msg.alert("提示", "请选择要编辑的安措！");
		}
		
	}

	/**
	 * 刷新弹出画面
	 */
	function refreshPopWin(safetyCode) {
		// 取得弹出画面grid数据
		popDs.load({
			params : {
				workticketNo : strWorkticketNo,
				safetyCode : safetyCode
			}
		});

		// 取得txtArea数据
		Ext.Ajax.request({
			url : 'workticket/getSafetyContents.action',
			method : 'POST',
			params : {
				workticketNo : strWorkticketNo,
				safetyCode : safetyCode
			},
			success : function(result, request) {
				var mydata = result.responseText;
				mydata = Ext.util.JSON.decode(mydata);
				txtArea.setValue(mydata);
			},
			failure : function(result, request) {
				Ext.Msg.alert("查询失败！");
			}
		});
	}

	// 弹出画面双击处理
	//popGrid.on("dblclick", updateHandler);

	/**
	 * 修改处理
	 */
	function updateHandler() {
		 popGrid.stopEditing();
         var modifyRec = popGrid.getStore().getModifiedRecords(); 
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
				url : 'workticket/modifySafety.action',
				method : 'post',
				params : {
					addOrUpdateRecords : Ext.util.JSON.encode(modifyRecords),
					deleteRecords : deleteRecords.join(","),
					workticketNo:strWorkticketNo,
					safetyCode:getSafetyCode()
				},
				success : function(result, request) {
					Ext.MessageBox.alert('提示', '操作成功！');  
					popDs.reload();
					popDs.rejectChanges();
					deleteRecords=[];
					
					//保存成功后刷新安措内容
					// 取得txtArea数据
				Ext.Ajax.request({
					url : 'workticket/getSafetyContents.action',
					method : 'POST',
					params : {
						workticketNo : strWorkticketNo,
						safetyCode : getSafetyCode()
					},
					success : function(result, request) {
						var mydata = result.responseText;
						mydata = Ext.util.JSON.decode(mydata);
						txtArea.setValue(mydata);
					},
					failure : function(result, request) {
						Ext.Msg.alert("查询失败！");
					}
				});
							
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('提示', '操作失败！');
					popDs.reload();
					popDs.rejectChanges();
					deleteRecords=[];
				}
			})
		}
		else
		{
			Ext.MessageBox.alert('提示', '您没有做任何修改！');
		}
	}
	/**
	 * 增加处理
	 */
	function addHandler() {
            var currentRecord = popGrid.getSelectionModel().getSelected();
				var count = popDs.getCount(); 
				var currentIndex = currentRecord?currentRecord.get("operationOrder")-1:count 
				addRecord(currentIndex); 
				resetLine();  
				popSm.selectRow(currentIndex);
				popGrid.startEditing(currentIndex, 2);
	}

	/**
	 * 删除处理
	 */
	function deleteHandler() {
		 popGrid.stopEditing();
		var sm = popGrid.getSelectionModel();
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
				popDs.remove(selections[i]);
				popDs.getModifiedRecords().remove(record);
			} 
			resetLine();
		} 
	}

	// ↑↑***********弹出画面***********↑↑//
	// 票面浏览
	function workticketPrint() {

		
				var strReportAdds = getReportUrl(strWorkticketType,strWorkticketNo,register.firelevel);
				if (strReportAdds !="") {
					window.open(strReportAdds);
				} else {
					Ext.Msg.alert('提示',"目前没有该种工作票票面预览");
				}


	}

	
//	//----add by fyyang 090217------------
//	 //判断是否由标准票生成，由标准票生成的不能修改
//	    if (register.isCreateByStandard == "Y") {
//		grid.getTopToolbar().items.get('upload').setDisabled(false);
//		grid.getTopToolbar().items.get('myUpdate').setDisabled(true);
//	} else if (register.isStandard == "Y") {
//		grid.getTopToolbar().items.get('upload').setDisabled(true);
//		grid.getTopToolbar().items.get('myUpdate').setDisabled(true);
//	} else {
//		grid.getTopToolbar().items.get('upload').setDisabled(false);
//		
//		grid.getTopToolbar().items.get('myUpdate').setDisabled(false);
//
//	}
                    
	


});