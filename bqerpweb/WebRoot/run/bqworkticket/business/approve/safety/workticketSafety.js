var deleteRecords = []; 
// 工作票编号
var strWorkticketNo = getParameter("workticketNo");
	
		// 工作票类型编号
var strWorkticketType = getParameter("workticketType");
	// 执行状态 已执行
var EXESTATE_EXE = "EXE";
// 执行状态 检修自理
var EXESTATE_REP = "REP"

//是否可以修改非运行补充安措 
var showSafetyTyep="";
var ifupdateN=false;
//是否可以修改运行补充安措
var ifupdateY=false;
//
var ifupdateRun=false;
var ifupdateRepair=false;
var ifupdateFire=false;
//---------是否安措办理-----
var ifdosafety=false;
//-------是否需要显示挂牌
var ifmarkcard=false;
//tbar文字
var tbarText="安措信息";
//bbar文字
var bbarText="";

if(getParameter("ifdosafety")!=null&&getParameter("ifdosafety")!="")
{
	ifdosafety=getParameter("ifdosafety");
}
ifmarkcard=ifdosafety;

if(getParameter("showSafetyTyep")!=null&&getParameter("showSafetyTyep")!="")
{
	showSafetyTyep=getParameter("showSafetyTyep");
	if(showSafetyTyep.indexOf('Y')!=-1)
	{
		ifupdateY=true;
	}
	if(showSafetyTyep.indexOf('N')!=-1)
	{
		ifupdateN=true;
	}
	if(showSafetyTyep.indexOf('run')!=-1)
	{
		ifupdateRun=true;
	}
	if(showSafetyTyep.indexOf('repair')!=-1)
	{
		ifupdateRepair=true;
	}
	if(showSafetyTyep.indexOf('fire')!=-1)
	{
		ifupdateFire=true;
	}
}
//alert(ifupdateFire);
if(ifdosafety)
{
	tbarText="(安措执行)";
	bbarText="将状态行为改变为:";
}
else if(ifupdateY)
{
	tbarText="(可修改补充安措信息)";
	
}
else if(ifupdateN||ifupdateRun||ifupdateRepair||ifupdateFire)
{
	tbarText="(可修改安措信息)";
	
}
else
{
	tbarText="(安措信息)";
}
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
	/**
	 * 20090515 wzhyan add
	 * 判断消防安措是否有内容
	 */
	function hasXiaFangSafety()
	{
		var ds = Ext.getCmp("grid-div").getStore(); 
		if(ds)
		{
			for(var i=0; i<ds.getCount();i++)
			{
				var re = ds.getAt(i);
				if(re.get("safetyCode") == "43" && re.get("safetyContent") != null && re.get("safetyContent") != '')
				{ 
					return true;
				}
			}
		}
		return false; 
	}
Ext.onReady(function() { 
// ↓↓*******************安措信息tab**************************************
	
	// ↓↓********************主画面************************
	// grid中的store
//	var ds = new Ext.data.JsonStore({
//		root : 'list',
//		url : 'bqworkticket/getSafetyContent.action',
//		fields : ['safetyCode', 'safetyDesc', 'safetyContent',
//				'safetyType', 'markcardTypeId']
//	});
//modify by fyyang 090318--------------------------
var MyDsRecord=Ext.data.Record.create([
	{name : 'safetyCode'},
    {name : 'safetyDesc'},
	{name : 'safetyContent'},
	{name:'safetyType'},
	{name:'line'},
	{name:'markcardTypeId'},
	{name:'safetyExeContent'}
	
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
	});
	
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
//---------------------------------------	
	// 载入数据
	ds.load({
		params : {
			workticketNo : strWorkticketNo,
			workticketTypeCode:strWorkticketType
		}
	});

	// 单选列

	
	var sm = new Ext.grid.RowSelectionModel({
		singleSelect : true,
		listeners:{
			rowselect :function(obj,rowIndex,rec){
				setRadioByDangerId(rec.get("safetyCode")) ;
			}
		}
	});

	// 列内容
	var item_cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
		header : '安全措施描述',
		dataIndex : 'safetyDesc',
		sortable : true,
		width : 300,
		renderer : function change(val) {
					return ' <span style="white-space:normal;">' + val
							+ ' </span>';
				}
	}, {
		header : '安全措施内容',
		dataIndex : 'safetyContent',
		sortable : true,
		align : 'left',
		width : 400,
			renderer:function(value, metadata, record){ 
			metadata.attr = 'style="white-space:normal;"'; 
			return value;  
	}
	}]);
	

	// 工具条
	var tbar = new Ext.Toolbar({
		items : [{id : 'myUpdate',
			text : '修改',
			iconCls:"update",
			handler :dbclickHandler},{
			text : '打印安措执行卡',
			id:'btnPrint',
			handler : function() {
				// 打印安措执行卡操作
				Ext.lib.Ajax.request('POST',
						'bqworkticket/printMeasureExeCard.action', {
							success : function(action) {
								var result = eval("(" + action.responseText
										+ ")");
								// 弹出返回信息
								Ext.Msg.alert(Constants.SYS_REMIND_MSG,
										result.msg);
							},
							failure : function() {
								Ext.Msg.alert(Constants.ERROR,
										Constants.UNKNOWN_ERR);
							}
						}, 'workticketNo=' + strWorkticketNo);
			}
		}, {
			xtype : 'tbspacer'
		}, '<font color="red">'+tbarText+'</font>']
	});
	// 安措信息画面grid
	var grid = new Ext.grid.GridPanel({
		id:'grid-div',
		ds : ds,
		cm : item_cm,
		enableColumnMove : false,
		sm : sm,
		tbar : tbar,
		autoWidth : true,
		autoHeight : true,
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
	
			ds.on('load', function() {
		var radios = document.getElementsByName("selectOne");
		if (radios) {
			if(radios[0]!=null)
			{
			radios[0].checked = true;
			}
		}
	});
	// 主画面grid双击处理
		grid.on("rowdblclick",dbclickHandler);

	// ↑↑********************主画面************************

	// ↓↓*************增加/修改工作票安全措施内容********↓↓
	var wd = 180;
	// 前关键词combox的store
	var frontKeywordStore = new Ext.data.JsonStore({
		url : "workticket/getKeywordList.action",
		root : 'list',
		fields : [{
			name : "safetyKeyName"
		}, {
			name : "safetyKeyId"
		}]
	});

//	// 前关键词
//	var cbxFrontKeyword = new Ext.form.ComboBox({
//		fieldLabel : "前关键词",
//		allowBlank : true,
//		triggerAction : 'all',
//		width : wd,
//		id : "frontKeyId",
//		name : "safety.frontKeyword",
//		store : frontKeywordStore,
//		displayField : 'safetyKeyName',
//		valueField : 'safetyKeyId',
//		mode : 'local',
//		emptyText : '请选择前关键词...',
//		readOnly : true
//	});
	
		// 前关键词
	var cbxFrontKeyword = new Ext.form.ComboBox({
		fieldLabel : "前关键词",
		allowBlank : true,
			editable : true,
		triggerAction : 'all',
		width : wd,
		id : "frontKeyId",
		name : "frontKeyword",
		store : frontKeywordStore,
		displayField : 'safetyKeyName',
		mode : 'local',
		emptyText : '请选择前关键词...',
		readOnly : false
	});

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

	// 设备名称内容
	var triEquName = new Ext.form.TriggerField({
		fieldLabel : '设备名称内容<font color="red">*</font>',
		width : wd,
		id : "equName",
		name : "safety.equName",
		allowBlank : false,
		//readOnly : true,
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
		allowBlank : true,
		editable : true,
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
//	// 标点
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
//
//	// 设备名称内容
//	var hideAttributeCode = new Ext.form.Hidden({
//		id : 'hideAttributeCode',
//		name : 'safety.attributeCode'
//	});
//	// 弹出窗口panel
//	var myaddpanel = new Ext.FormPanel({
//		frame : true,
//		labelAlign : 'right',
//		items : [hiddenWorkticketNo, hiddenId, hiddenSafetyCode,
//				cbxFrontKeyword, triEquName, hideAttributeCode, cbxBackKeyword,
//				cbxFlagId, numOperationOrder]
//	});
//
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
//					// 增加
//					myurl = "workticket/addSafetyBaseInfo.action";
//				} else {
//					// 修改
//					myurl = "workticket/updateSafetyBaseInfo.action";
//				}
//				// 表单提交
//				myaddpanel.getForm().submit({
//					method : 'POST',
//					url : myurl,
//					params : {
//						// 参数 前关键词ID
//						'safety.frontKeyId' : cbxFrontKeyword.value,
//						// 参数 后关键词ID
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
			operationOrder:'',
			safetyExecuteCode:'',
	        markcardCode:''
	
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
		var url = "../../../business/register/safety/selectPressboard.jsp?";
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

	// ↓↓***********弹出画面*********** //
//	// 隐藏域，保存isRunAdd
//	var safetyType = new Ext.form.Hidden({
//		id : "safetyType",
//		value : ""
//	});
	// 隐藏域，保存markcardTypeId
	var markcardTypeId = new Ext.form.Hidden({
		id : "markcardTypeId",
		value : ""
	});
	
	
	// grid中的store
//	var popDs = new Ext.data.JsonStore({
//		root : 'list',
//		url : 'workticket/getSafetyDetail.action',
//		fields : ['frontKeyword', 'equName', 'attributeCode', 'backKeyword',
//				'flagId', 'safetyCode', 'isReturn', 'markcardCode',
//				'safetyExecuteCode', 'id']
//	});
	
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
	},
	{name:'safetyExecuteCode'}
	,
	{name:'markcardCode'}]); 
	var popDs = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'workticket/getSafetyDetail.action'
		}),
		reader : new Ext.data.JsonReader({
			root : "list"
		}, MyPopRecord),
		sortInfo : {
			field : "operationOrder",
			direction : "ASC"
		}
	}); 

	// 选择列
	var popSm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	})

	// 列内容
	var popCm = new Ext.grid.ColumnModel([popSm, {
		header : '前关键词',
		width : 80,
		dataIndex : 'frontKeyword',
		sortable : true,
		editor:cbxFrontKeyword
	}, {
		header : '设备名称内容',
		sortable : true,
		dataIndex : 'equName'
		,
		editor :triEquName
	}, {
		header : '设备功能编码',
		width : 80,
		sortable : true,
		dataIndex : 'attributeCode',
		renderer:function(value){
           return "<span style='color:gray;'>"+value+"</span>";

		}
	}, {
		header : '后关键词',
		width : 80,
		sortable : true,
		dataIndex : 'backKeyword'
		,
		editor:cbxBackKeyword
	}, {
		header : '标点',
		width : 40,
		sortable : true,
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
		renderer:function(value){
           return "<span style='color:gray;'>"+value+"</span>";

		}
	},
//		{
//		header : '是否换行',
//		width : 60,
//		sortable : true,
//		dataIndex : 'isReturn',
//		renderer : function(value) {
//			if (value && value == "Y") {
//				return "换行";
//			} else {
//				return "不换行";
//			}
//		}
//	}, 
		{
		header : '执行状态',
		width : 60,
		sortable : true,
		dataIndex : 'safetyExecuteCode',
		renderer : function(value) {
			if (EXESTATE_EXE == value) {
				return "<span style='color:gray;'>已执行</span>";
			} else if (EXESTATE_REP == value) {
				return "<span style='color:gray;'>检修自理</span>";
			} else {
				return  "<span style='color:gray;'>未处理</span>";
			}
		}
	}, {
		header : '挂牌编号',
		width : 80,
		sortable : true,
		dataIndex : 'markcardCode',
		hidden : true,
		renderer : function(val, p, record) {
			if (!val)
				val = "";
			// 增加textfield
			return "<input type='text'  maxLength='100' style='width:60px' id='txt"
					+ record.get('id') + "' value= '" + val + "'/>";
		}
	}]);
	// ↓↓*********************挂牌编号选择框*******************
	// 取得标识牌名称
	var marktypeNameStore = new Ext.data.JsonStore({
		url : 'workticket/getMarkcardTypeList.action',
		root : 'list',
		fields : [{
			name : 'markcardTypeId'
		}, {
			name : 'markcardTypeName'
		}]
	});
	// 载入数据
	marktypeNameStore.load();
	// 定义grid
	var MyRecord = Ext.data.Record.create([
			// 标示牌ID
			{
				name : 'markcardId'
			},
			// 标示牌编号
			{
				name : 'markcardCode'
			},
			// 标示牌名称
			{
				name : 'markcardTypeName'
			}]);
	// 代理
	var dataProxy = new Ext.data.HttpProxy({
		url : 'bqworkticket/getMarkcardList.action'
	});
	// render
	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);
	// grid的store
	var markcardStore = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
	// 输入框
	var fuzzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});
	// baseparams
	markcardStore.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			fuzzy : fuzzy.getValue(),
			markcardTypeID : markcardTypeId.getValue()
		})
	});
	// 隐藏域，保存弹出window的记录的行号
	var hiddenRowIndex = new Ext.form.Hidden({
		id : "rowIndex"
	});
	// 分页栏
	var abbar = new Ext.PagingToolbar({
		pageSize : Constants.PAGE_SIZE,
		store : markcardStore,
		displayInfo : true,
		displayMsg : "共{2}条数据",
		emptyMsg : Constants.EMPTY_MSG
	});
	// grid
	var markcardGrid = new Ext.grid.GridPanel({
		region : "center",
		layout : 'fit',
		width : 380,
		height : 380,
		enableColumnMove : false,
		draggable : false,
		store : markcardStore,
		columns : [sm, {
			header : "标示牌ID",
			sortable : true,
			width : 50,
			dataIndex : 'markcardId'
		}, {
			header : "标识牌编号",
			sortable : true,
			width : 50,
			dataIndex : 'markcardCode'
		}, {
			header : "标识牌名称",
			sortable : true,
			width : 50,
			dataIndex : 'markcardTypeName',
			renderer : function() {
				var value = markcardTypeId.getValue();
				// 查找对应的标点
				for (var i = 0; i < marktypeNameStore.getCount(); i++) {
					if (marktypeNameStore.getAt(i).data.markcardTypeId == value) {
						// 显示对应的标点名称
						return marktypeNameStore.getAt(i).data.markcardTypeName;
					}
				}
			}
		}],
		viewConfig : {
			forceFit : true
		},
		bbar : abbar,
		tbar : ['快速查找(标示牌编号):', fuzzy, {
			text : Constants.BTN_QUERY,
			iconCls : Constants.CLS_QUERY,
			handler : function() {
				markcardStore.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});
			}
		}, {
			text : '确定',
			handler : selectMarkcardCode
		}]
	});
	// 双击选择挂牌编号
	markcardGrid.on('celldblclick', selectMarkcardCode);
	var formPanel = new Ext.FormPanel({
		frame : true,
		width : 400,
		height : 420,
		hideLabels : true,
		items : [hiddenRowIndex, markcardGrid]
	})
	// 弹出窗口
	var markcardWin = new Ext.Window({
		width : 400,
		height : 420,
		title : "挂牌编号选择",
		resizable : false,
		items : [formPanel],
		layout : 'fit',
		closeAction : 'hide'
	});
	// 窗口显示
	function showmMarkcardWin(rowIndex) {
		// 清空原来的数据
		formPanel.getForm().reset();
		// 保存grid的行号
		hiddenRowIndex.setValue(rowIndex);
		markcardStore.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			}
		});
		// 显示窗口
		markcardWin.show();
	}
	// ↑↑*********************挂牌编号选择框*******************
	// 执行状态combox
	// 数据源
	var runStateStore = new Ext.data.SimpleStore({
		fields : ["text", "value"],
		data : [["已执行", EXESTATE_EXE], ["检修自理", EXESTATE_REP], ["未处理", ""]]
	});
	// 执行状态
	var runState = new Ext.form.ComboBox({
		id : "safetyExecuteCode",
		triggerAction : 'all',
		width : 180,
		store : runStateStore,
		displayField : 'text',
		valueField : 'value',
		mode : 'local',
		value : "EXE",
		readOnly : true
	});
	var spacer = new Ext.Toolbar.Spacer({
		style : "width:20px"
	});
	spacer.enable();
	// 工具条增加按钮
	var addbtn = new Ext.Toolbar.Button({
		id : 'addbtn',
		text : '插入',
		iconCls : 'insert',
		handler : addHandler,
		disabled:true
	});
	// 工具条修改按钮
	var updatebtn = new Ext.Toolbar.Button({
		id : 'updatebtn',
		text : '保存',
		iconCls : 'save',
		handler : updateHandler,
		disabled:true
	});
	// 工具条删除按钮
	var deletebtn = new Ext.Toolbar.Button({
		id : 'deletebtn',
		text : Constants.BTN_DELETE,
		iconCls : Constants.CLS_DELETE,
		handler : deleteHandler,
		disabled:true
	});
	
	var savebtn=new Ext.Toolbar.Button({
	        id : 'savebtn',
			text : '安措办理保存',
			iconCls : Constants.CLS_SAVE,
			handler : saveHandler
	
	});
	// 弹出画面工具条
	var popBar = new Ext.Toolbar({
		items : [bbarText, runState,
//		{
//			id : 'savebtn',
//			text : Constants.BTN_SAVE,
//			iconCls : Constants.CLS_SAVE,
//			handler : saveHandler,
//		    disabled:true
//		},
		savebtn,
		spacer, new Ext.Toolbar.Fill(), addbtn, updatebtn, deletebtn, {
			id : 'closebtn',
			text : "关闭",
			iconCls : Constants.CLS_CANCEL,
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
				
				  if(win.ifReflesh)
				{
						ds.load({
					params : {
						workticketNo : strWorkticketNo,
						workticketTypeCode:strWorkticketType
					}
				});
				}
				

			}
		}]
	})

	// 弹出文本区域
	var txtArea = new Ext.form.TextArea({
		id : 'txtArea',
		width : 630,
		autoScroll : true,
		height : 180,
		readOnly : true,
		value : ""
	})

	// gridPanel
	var popGrid = new Ext.grid.EditorGridPanel({
		id : "popGrid",
		ds : popDs,
		cm : popCm,
		width : 630,
		enableColumnMove : false,
		enableColumnHide : false,
		 clicksToEdit:1,
		sm : popSm,
		bbar : popBar,
		autoWidth : true,
		height : 200,
		autoScroll : true,
		border : false
	})
	
	//popGrid右键菜单
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
	// 双击挂牌编码textfield弹出挂牌编码选择窗口
	popGrid.on('celldblclick', function(grid, rowIndex, columnIndex, e) {
		// 挂牌编码列
		if (columnIndex == 8) {
			showmMarkcardWin(rowIndex);
		}
	});
	Ext.Window.prototype.ifReflesh = false;
	// 弹出窗口
	var win = new Ext.Window({
		resizable : false,
		title : '安全措施',
		height : 410,
		width : 650,
		modal : true,
		closable :false,
		closeAction : 'hide',
//		listeners : {
//			'hide' : function() {
//				ds.load({
//		params : {
//			workticketNo : strWorkticketNo,
//			workticketTypeCode:strWorkticketType
//		}
//	});
//			}
//		},
		items : [popGrid, txtArea, markcardTypeId]
	})

	// ***********处理*********** //
	/**
	 * 增加处理
	 */
	function addHandler() {
//		// 重置表单
//		myaddpanel.getForm().reset();
//		// 工作票编号
//		hiddenWorkticketNo.setValue(strWorkticketNo);
//		// 安措编码,增加时来自主画面
//		var records = grid.getSelectionModel().getSelections();
//		//hiddenSafetyCode.setValue(records[0].get('safetyCode'));
//			hiddenSafetyCode.setValue(getSafetyCode());
//		// 增加修改区分
//		hiddenIsAdd.setValue(true);
//
//		// 显示增加窗口
//		maintWin.show();
		  var currentRecord = popGrid.getSelectionModel().getSelected();
				var count = popDs.getCount(); 
				var currentIndex = currentRecord?currentRecord.get("operationOrder")-1:count 
				addRecord(currentIndex); 
				resetLine();  
				popSm.selectRow(currentIndex);
				popGrid.startEditing(currentIndex, 2);
					win.ifReflesh = false; 
	}
	/**
	 * 选择挂牌编号
	 */
	function selectMarkcardCode() {

		var grid = Ext.getCmp('popGrid');
		var record = grid.getStore().getAt(hiddenRowIndex.getValue());
		// 获得选择的标识牌编号
		var resultRecord = markcardGrid.selModel.getSelected();
		// 如果选择
		if (resultRecord) {
			record.beginEdit();
			// 设置选择的值
			record.set('markcardCode', resultRecord.data['markcardCode']);
			record.endEdit();
			// 隐藏窗口
			markcardWin.hide();
		} else {
			// 没有选择记录时显示提示信息
			Ext.Msg.alert(Constants.SYS_REMIND_MSG,
					Constants.SELECT_NULL_UPDATE_MSG);
		}

	}
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
				win.ifReflesh = true;
					//-------------
					
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
//		// 判断有无选择记录
//		if (popGrid.selModel.hasSelection()) {
//			var records = popGrid.selModel.getSelections();
//			var recordslen = records.length;
//			// 选择多条数据时弹出提示信息
//			if (recordslen > 1) {
//				Ext.Msg.alert(Constants.SYS_REMIND_MSG,
//						Constants.SELECT_COMPLEX_MSG);
//			} else {
//				// 获得选择的记录
//				var record = popGrid.getSelectionModel().getSelected();
//				// 显示修改画面
//				maintWin.show();
//				// 传入修改数据
//				myaddpanel.getForm().loadRecord(record);
//				// 工作票编号
//				hiddenWorkticketNo.setValue(strWorkticketNo);
//				// 前关键词
//				cbxFrontKeyword.setRawValue(record.get('frontKeyword'));
//				// 后关键词
//				cbxBackKeyword.setRawValue(record.get('backKeyword'))
//				// 增加修改区分
//				hiddenIsAdd.setValue(false);
//
//			}
//		} else {
//			// 没有选择记录时显示提示信息
//			Ext.Msg.alert(Constants.SYS_REMIND_MSG,
//					Constants.SELECT_NULL_UPDATE_MSG);
//		}
	}
	/**
	 * 主画面grid双击处理
	 */
	function dbclickHandler() { 
		win.ifReflesh = false;  
		var safetyCode = getSafetyCode();
		var rec;
		if(safetyCode!=null)
		{
			 rec=getSafetyData(safetyCode);
		}
		else
		{
			Ext.Msg.alert("提示", "请选择要编辑的安措！");
			return;
		}
		var  isRun=rec.get("safetyType");
			// 刷新窗口
		
			refreshPopWin(rec.get("safetyCode"));
			// 挂牌id
			markcardTypeId.setValue(rec.get("markcardTypeId"));
			
//-----------------------------------------------------------------------	
		
		// isRun为Y时可以对安全措施内容维护
		if (isRun == "Y") {
			//运行补充安措
			refreshPopWin(rec.get("safetyCode"));
			// 挂牌id
			markcardTypeId.setValue(rec.get("markcardTypeId"));
            if(ifupdateY)
            {
            	
            	//可以增删改
			Ext.getCmp('addbtn').setDisabled(false);
			Ext.getCmp('updatebtn').setDisabled(false);
			Ext.getCmp('deletebtn').setDisabled(false);
            }
            else
            {
            	//不可以增删改
            	Ext.getCmp('addbtn').setDisabled(true);
			    Ext.getCmp('updatebtn').setDisabled(true);
			    Ext.getCmp('deletebtn').setDisabled(true);
            }
            if(ifupdateY||ifdosafety)
            {
            	win.show();
            }
            else
            {
            	Ext.Msg.alert("提示", "此条安措不允许编辑！");
            }
			
		} else if (isRun == 'N') {
			//非运行补充安措
		
			if(ifupdateN)
			{
			//可以增删改
			Ext.getCmp('addbtn').setDisabled(false);
			Ext.getCmp('updatebtn').setDisabled(false);
			Ext.getCmp('deletebtn').setDisabled(false);
		   }
		   else
		   {
		   	//不可以增删改
		   	Ext.getCmp('addbtn').setDisabled(true);
			Ext.getCmp('updatebtn').setDisabled(true);
			Ext.getCmp('deletebtn').setDisabled(true);
		   }
		   if(ifdosafety||ifupdateN)
		   {
		   	win.show();
		   }
		    else
            {
            	Ext.Msg.alert("提示", "此条安措不允许编辑！");
            }
		}
		else if(isRun=='run')
		{
			if(ifupdateRun)
			{
				//可以增删改
			Ext.getCmp('addbtn').setDisabled(false);
			Ext.getCmp('updatebtn').setDisabled(false);
			Ext.getCmp('deletebtn').setDisabled(false);
			}
			 else
		   {
		   	//不可以增删改
		   	Ext.getCmp('addbtn').setDisabled(true);
			Ext.getCmp('updatebtn').setDisabled(true);
			Ext.getCmp('deletebtn').setDisabled(true);
		   }
		   if(ifupdateRun)
		   {
		   	win.show();
		   } else
            {
            	Ext.Msg.alert("提示", "此条安措不允许编辑！");
            }
		}
		else if(isRun=='repair')
		{
			if(ifupdateRepair)
			{
				//可以增删改
			Ext.getCmp('addbtn').setDisabled(false);
			Ext.getCmp('updatebtn').setDisabled(false);
			Ext.getCmp('deletebtn').setDisabled(false);
			}
			 else
		   {
		   	//不可以增删改
		   	Ext.getCmp('addbtn').setDisabled(true);
			Ext.getCmp('updatebtn').setDisabled(true);
			Ext.getCmp('deletebtn').setDisabled(true);
		   }
		    if(ifupdateRepair)
		   {
		   	win.show();
		   } else
            {
            	Ext.Msg.alert("提示", "此条安措不允许编辑！");
            }
		}
		else if(isRun=='fire')
		{
			if(ifupdateFire)
			{
				//可以增删改
			Ext.getCmp('addbtn').setDisabled(false);
			Ext.getCmp('updatebtn').setDisabled(false);
			Ext.getCmp('deletebtn').setDisabled(false);
			}
			 else
		   {
		   	//不可以增删改
		   	Ext.getCmp('addbtn').setDisabled(true);
			Ext.getCmp('updatebtn').setDisabled(true);
			Ext.getCmp('deletebtn').setDisabled(true);
		   }
		   if(ifupdateFire)
		   {
		   	win.show();
		   } else
            {
            	Ext.Msg.alert("提示", "此条安措不允许编辑！");
            }
		}
		else
		{
				//可以增删改
			Ext.getCmp('addbtn').setDisabled(false);
			Ext.getCmp('updatebtn').setDisabled(false);
			Ext.getCmp('deletebtn').setDisabled(false);
			win.show();
		}
//--------------------------------------------------		
		//是否是安措办理
		if(ifdosafety)
		{
			Ext.getCmp('savebtn').setVisible(true);
			
		}
		else
		{
				Ext.getCmp('savebtn').setVisible(false);
		}
		//是否显示窗口
//		if(ifupdateN||ifupdateY||ifmarkcard||ifdosafety||ifupdateRun||ifupdateRepair||ifupdateFire)
//		{
//			// 显示窗口
//			 win.show();
//		}
		
		//modify by fyyang 090105
		//无挂牌时不显示挂牌编号列
		if (rec.get('markcardTypeId') == null||rec.get('markcardTypeId')=='0'||!ifmarkcard) {
			popCm.setHidden(8, true);
		} else {
			popCm.setHidden(8, false);
		}
	}
	
	// modify by fyyang
	function saveHandler() {
		// 判断有无选择记录
		if (popGrid.selModel.hasSelection()) {
			var records = popGrid.selModel.getSelections();
			var recordslen = records.length;
			var str = "[";
			for (var i = 0; i < recordslen; i += 1) {
				// 获得选择的记录
				var record = records[i];
				if(record.data['id']==null||record.data['id']=="")
				{
					Ext.Msg.alert("提示","请先保存安措信息！");
					return false;
				}
				str = str + "{'id':'" + record.data['id']
						+ "','safetyExecuteCode':'" + runState.getValue()
						+ "','markcardCode':'"
						+ Ext.get('txt' + record.get('id')).getValue() + "'},"
			}
			if (str.length > 1) {
				str = str.substring(0, str.length - 1);
			}
			str = str + "]";
			// alert(str);
			Ext.Ajax.request({
				url : 'bqworkticket/updateWorkticketSafetyRecord.action',
				method : 'POST',
				params : {
					data : str
				},
				success : function(result, request) {
					// 刷新页面
					refreshPopWin(getSafetyCode());
				},
				failure : function(result, request) {
					Ext.Msg.alert(Constants.UNKNOWN_ERR);
				}
			});

		} else {
			// 没有选择记录时显示提示信息
			Ext.Msg.alert(Constants.SYS_REMIND_MSG,
					Constants.SELECT_NULL_UPDATE_MSG);
		}
	}
	/**
	 * 刷新弹出画面
	 */
	function refreshPopWin(safetyCode) {
             deleteRecords = [];
			 popDs.rejectChanges();
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
				// 工作票编号
				workticketNo : strWorkticketNo,
				// 安措编码
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
	/**
	 * 删除处理
	 */
	function deleteHandler(a,e) {
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
		e.stopEvent();
	}
//	// 安措信息tab
//	var measurePanel = new Ext.Panel({
//		title : "安措信息",
//		items : [grid]
//	});
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
	//是否显示打印安措执行卡
	if(ifdosafety)
	{
		 
		// grid.getTopToolbar().items.get('btnPrint').setVisible(true);
		  runState.setVisible(true);
		
	}
	else
	{
		
		runState.setVisible(false);
	}
	grid.getTopToolbar().items.get('btnPrint').setVisible(false);
	
	// ↑↑*******************安措信息tab**************************************
	});