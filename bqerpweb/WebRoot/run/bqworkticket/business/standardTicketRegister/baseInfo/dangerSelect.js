Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var workticketType=window.dialogArguments.workticketType;
	//getParameter("workticketType");
	//alert(workticketType);
	var currentNode = new Object();
	var currentReasonId = "0";
	currentNode.id = "0";
	currentNode.text = "危险点";

	var wd = 180;

	
	function renderDate(value) {
		if (!value)
			return "";
		var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
		var reTime = /\d{2}:\d{2}:\d{2}/gi;
		var strDate = value.match(reDate);
		var strTime = value.match(reTime);
		if (!strDate)
			return "";
		return strTime ? strDate + " " + strTime : strDate;
	}

	

	// 定义grid 危险点列表
	var MyRecord1 = Ext.data.Record.create([{
		name : 'dangerId'
	}, {
		name : 'dangerName'
	}, {
		name : 'modifyBy'
	}, {
		name : 'modifyDate'
	}, {
		name : 'orderBy'
	}, {
		name : 'PDangerId'
	}]);

	var dataProxy1 = new Ext.data.HttpProxy(

			{

				url : 'workticket/getDangerList.action'
			}

	);

	var theReader1 = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"

	}, MyRecord1);

	var reasonStore = new Ext.data.Store({

		proxy : dataProxy1,

		reader : theReader1

	});

	var fuzzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});
	var sm = new Ext.grid.CheckboxSelectionModel();

	var gridreason = new Ext.grid.GridPanel({
		store : reasonStore,
		height : 200,
		columns : [// 设计页面显示的表格信息，字段与Ext.data.Record.create匹配
		sm, new Ext.grid.RowNumberer({
			width : 20,
			align : 'center'
		}), {
			header : "ID",
			sortable : true,
			dataIndex : 'dangerId',
			hidden : true

		}, {
			header : "危险点名称",
			sortable : true,
			dataIndex : 'dangerName',
			width : 340,
			align : 'center'
		}, {
			header : "填写人",
			sortable : true,
			dataIndex : 'modifyBy',
			width : wd,
			align : 'center',
			hidden : true
		}, {
			header : "填写日期",
			sortable : true,
			dataIndex : 'modifyDate',
			renderer : renderDate,
			width : wd,
			align : 'center',
			hidden : true
		}, {
			header : "排序",
			sortable : true,
			dataIndex : 'orderBy',
			width : wd,
			align : 'center',
			hidden : true
		}, {
			header : "父ID",
			sortable : true,
			dataIndex : 'PDangerId',
			hidden : true,
			align : 'center'

		}],
		sm : sm,
		title : '工作票危险点详细信息',

		tbar : [fuzzy, {
			text : "查询",
			iconCls : 'query',
			handler : queryreason
		}]
	});
	
	gridreason.addListener('rowClick', rowClick);
	function rowClick(grid, rowIndex, e) {
		var record = grid.getStore().getAt(rowIndex);
		var dangerId = record.get("dangerId");
		var dangerName = record.get("dangerName");
		currentReasonId = dangerId;
		gridsolution.setTitle(dangerName + "---对应的控制措施");
		solutionstore.load({
			params : {
				start : 0,
				limit : 10,
				PDangerId : currentReasonId,
				fuzzy : '%'
			}
		});

	}

	function queryreason() {
		if (currentNode.id != "0") {
			reasonStore.load({
				params : {
					start : 0,
					limit : 10,
					fuzzy : fuzzy.getValue(),
					PDangerId : currentNode.id
				}
			});
		} else {
			alert("请选择工作票危险点内容！");
		}
	}

	

	

	
	// -------------控制措施方案--------------
	// 定义grid 控制措施方案
	var MyRecord2 = Ext.data.Record.create([{
		name : 'dangerId'
	}, {
		name : 'dangerName'
	}, {
		name : 'PDangerId'
	}, {
		name : 'modifyBy'
	}, {
		name : 'modifyDate'
	}, {
		name : 'orderBy'
	}]);

	var dataProxy2 = new Ext.data.HttpProxy(

			{
				url : 'workticket/getDangerList.action'
			}

	);

	var theReader2 = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"

	}, MyRecord2);

	var solutionstore = new Ext.data.Store({

		proxy : dataProxy2,

		reader : theReader2

	});

	var solutionFuzzy = new Ext.form.TextField({
		id : "solutionFuzzy",
		name : "solutionFuzzy"
	});
	var mysm = new Ext.grid.CheckboxSelectionModel();

	var gridsolution = new Ext.grid.GridPanel({
		store : solutionstore,
		columns : [mysm, new Ext.grid.RowNumberer({
			width : 20,
			align : 'center'
		}), {

			header : "ID",
			sortable : true,
			dataIndex : 'dangerId',
			hidden : true
		}, {
			header : "控制措施",
			width : 340,
			sortable : true,
			dataIndex : 'dangerName'
		},

		{
			header : "父ID",
			hidden : true,
			sortable : true,
			dataIndex : 'PDangerId',
			hidden : true
		}, {
			header : "填写人",
			width : wd,
			sortable : true,
			readOnly : true,
			dataIndex : 'modifyBy',
			hidden : true
		}, {
			header : "填写日期",
			width : wd,
			sortable : true,
			readOnly : true,
			renderer : renderDate,
			dataIndex : 'modifyDate',
			hidden : true
		}, {
			header : "排序",
			width : wd,
			sortable : true,
			dataIndex : 'orderBy',
			hidden : true
		}],
		sm : mysm,
		title : '控制措施',

		tbar : [solutionFuzzy, {
			text : "查询",
			iconCls : 'query',
			handler : querySolution
		}]
	});
	
	function querySolution() {
		if (currentReasonId != "0") {
			solutionstore.load({
				params : {
					start : 0,
					limit : 10,
					PDangerId : currentReasonId,
					fuzzy : solutionFuzzy.getValue()
				}
			});
		} else {
			alert("请选择危险点名称");
		}
	}

	
	// ------------------------------------
	// -------------add 故障列表----------------
	var MyRecord = Ext.data.Record.create([{
		name : 'dangerId'
	}, {
		name : 'dangerTypeId'
	}, {
		name : 'dangerName'
	}, {
		name : 'orderBy'
	}, {
		name : 'modifyDate'
	}, {
		name : 'modifyBy'
	}, {
		name : 'PDangerId'
	},{ name:'typeName'}
	]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url : 'workticket/findDangerListForSelect.action'

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

	// 定义工作票种类数据源
	var dangerTypeStore = new Ext.data.JsonStore({
		url : 'workticket/CoDataDangerType.action?workticketType='+workticketType,
		root : 'list',
		fields : [{
			name : 'dangerTypeName'
		}, {
			name : 'dangerTypeId'
		}]
	})
	dangerTypeStore.load();
	dangerTypeStore.on('load', function(e, records, o) {
		dangerTypeCbo.setValue(records[0].data.dangerTypeId);
	});

	// 危险点类型名臣下拉框
	var dangerTypeCbo = new Ext.form.ComboBox({
		id : 'dangerTypeCbo',
		allowBlank : true,
		triggerAction : 'all',
		store : dangerTypeStore,
		displayField : 'dangerTypeName',
		valueField : 'dangerTypeId',
		mode : 'local',
		readOnly : true,
//		width : 90
		width : 120
	})


	store.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			fuzzy : dangerFuzzy.getValue(),
			dangerTypeId : dangerTypeCbo.getValue(),
			workticketTypeCode:workticketType
		});
	});

	var sm = new Ext.grid.CheckboxSelectionModel();
	var dangerFuzzy = new Ext.form.TextField({
		id : "dangerFuzzy",
		name : "dangerFuzzy",
		width : 90
	});
	var grid = new Ext.grid.GridPanel({
		store : store,
		columns : [sm, new Ext.grid.RowNumberer({
			width : 50,
			align : 'center'
		}), {
			header : "ID",
			sortable : true,
			dataIndex : 'dangerId',
			hidden : true
		},

		{
			header : "危险点类型",
			sortable : true,
			dataIndex : 'dangerTypeId',
			align : 'center',
			hidden : true
		},

		{
			header : "排序",
			sortable : true,
			dataIndex : 'orderBy',
			hidden : true
		},

		{
			header : "填写人",
			sortable : true,
			dataIndex : 'modifyBy',
			hidden : true
		},

		{
			header : "填写日期",
			sortable : true,
			dataIndex : 'modifyDate',
			renderer : renderDate,
			hidden : true
		},

		{
			header : "危险点内容",
			width : 200,
			sortable : true,
			dataIndex : 'dangerName',
			align : 'center'
		}, {
			header : "父ID",
			sortable : true,
			dataIndex : 'PDangerId',
			hidden : true
		}, {
			header : "类型名称",
			sortable : true,
			dataIndex : 'typeName',
			hidden : true
		}],
		sm : sm,
//		tbar : ["危险点类型", dangerTypeCbo, dangerFuzzy, {
//			text : "查询",
//			iconCls : 'query',
//			handler : queryRecord
//		}],
		tbar : ['编号/名称：',dangerFuzzy, {
			text : "查询",
			iconCls : 'query',
			handler : queryRecord
		}],
		bbar : new Ext.PagingToolbar({
			pageSize : 12,
			store : store,
			displayInfo : true,
			displayMsg : "",
			emptyMsg : ""
		})
	});
     grid.on("dblclick", selectrecord);
    function selectrecord()
     {
     	if (grid.selModel.hasSelection()) {
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				var record = grid.getSelectionModel().getSelected();
					var danger = new Object();
		danger.id=record.get("dangerId");
		danger.name=record.get("dangerName");
		danger.type=record.get("dangerTypeId");
		danger.typeName=record.get("typeName");
		window.returnValue = danger;
        window.close();
				
				
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
     }
	grid.addListener('rowClick', gridrowClick);
	function gridrowClick(grid, rowIndex, e) {

		var record = grid.getStore().getAt(rowIndex);
		var dangerId = record.get("dangerId");
		var modifyDate = record.get("modifyDate");
		var dangerName = record.get("dangerName");
		gridreason.setTitle(dangerName + "----对应的危险点名称");
	
		currentNode.id = dangerId;
		currentNode.text = dangerName;
		currentReasonId = "0";

		reasonStore.load({
			params : {
				start : 0,
				limit : 10,
				PDangerId : currentNode.id,
				fuzzy : '%'
			}
		});
		// gridsolution.setTitle("控制措施");
		//		                    
		solutionstore.load({
			params : {
				start : 0,
				limit : 10,
				PDangerId : '-1',
				fuzzy : '%'
			}
		});
	}

	function queryRecord() {
		store.load({
			params : {
				start : 0,
				limit : 12
			}
		});

	}
	
  function  initData()
  {
  	store.load({
			params : {
				start : 0,
				limit : 12,
				fuzzy : "",
			  dangerTypeId : "",
			workticketTypeCode:workticketType
			}
		});

  }
initData();

	// ---------------------------------------
	// ----------布局---------------------

	var right = new Ext.Panel({
		region : "center",
		layout : 'border',
		items : [{
			region : 'center',
			layout : 'fit',
			height : 200,
			items : [gridreason]
		}, {
			region : 'south',
			layout : 'fit',
			height : 240,
			items : [gridsolution]
		}]

	});

	var viewport = new Ext.Viewport({
		layout : 'border',
		items : [{
			region : 'west',
			split : true,
			width : 280,
			layout : 'fit',
			minSize : 175,
			maxSize : 600,
			margins : '0 0 0 0',
			collapsible : true,
			border : false,
			tbar : ["危险点类型:", dangerTypeCbo],
			// autoScroll : true,
			items : [grid]
		}, right// 初始标签页
		]
	});

});
