Ext.namespace('Power.maintenanceQuery');
Power.maintenanceQuery=function(){
	// -----------设备维修列表--------------------
	var record = Ext.data.Record.create([{
		name : 'order.id'
	}, {
		name : 'order.failureCode'
	}, {
		name : 'order.attributeCode'
	}, {
		name : 'order.equName'
	}, {
		name : 'order.checkAttr'
	}, {
		name : 'order.preContent'
	}, {
		name : 'order.description'
	}, {
		name : 'order.parameters'
	}, {
		name : 'order.problem'
	}, {
		name : 'order.spareParts'
	}, {
		name : 'startDate'
	}, {
		name : 'endDate'
	}, {
		name : 'order.supervisor'
	}, {
		name : 'supervisorName'
	}, {
		name : 'order.participants'
	}]);

	var xproxy = new Ext.data.HttpProxy({
		url : 'equstandard/findListByEquCode.action'
	});

	var xreader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, record);

	var censtore = new Ext.data.Store({
		proxy : xproxy,
		reader : xreader
	});
	// 分页
	var equcode = new Ext.form.TextField({
		id : "equcode",
		name : "equcode",
		readOnly : true
	});
	var equname = new Ext.form.TextField({
		id : "equname",
		name : "equname",
		width : '300',
		readOnly : true
	});
	var cenGrid = new Ext.grid.GridPanel({
		store : censtore,
		region : "center",
		layout:'fit',
		autoHeght : true,
		id : 'cenGrid',
		loadMask : {
			msg : '读取数据中 ...'
		},
		tbar : new Ext.Toolbar({
				items : ["设备：", equcode, '-', equname]
			}),
		columns : [new Ext.grid.RowNumberer(), {
			header : "ID",
			sortable : true,
			dataIndex : 'order.id',
			hidden : true
		}, {
			header : "设备名称",
			width : 100,
			dataIndex : 'order.equName',
			sortable : true
		}, {
			header : "修前情况",
			width : 100,
			dataIndex : 'order.preContent',
			sortable : true
		}, {
			header : "实际开始日期",
			width : 120,
			dataIndex : 'startDate',
			sortable : true
		}, {
			header : "负责人",
			width : 100,
			dataIndex : 'supervisorName',
			sortable : true
		}, {
			header : "检修性质",
			width : 100,
			dataIndex : 'order.checkAttr',
			sortable : true,
			renderer:function(v){
				if(v==1)return 'A级'
				else if(v==2)return 'B级'
				else if(v==3)return 'C级'
				else if (v==4)return 'D级'
				else if(v==5)return '重大消缺'
			}
		}, {
			header : "检修情况",
			width : 100,
			dataIndex : 'order.description',
			sortable : true
		}, {
			header : "存在问题",
			width : 100,
			dataIndex : 'order.problem',
			sortable : true
		}, {
			header : "更换备品备件",
			width : 100,
			dataIndex : 'order.spareParts',
			sortable : true
		}, {
			header : "修后技术参数",
			width : 100,
			dataIndex : 'order.parameters',
			sortable : true
		}],
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : censtore,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
//		,viewConfig : {
//					forceFit : true
//				}
	});
	function querydata() {
		censtore.baseParams = {
			equCode : equcode.getValue()
		};
		censtore.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}
	
	var win = new Ext.Window({
		title : '设备检修台帐',
		modal : true,
		width : 600,
		height : 400,
		layout:'fit',
		closeAction : 'hide',
		items : [cenGrid]
	})
	return {
		win : win,
		setValue : function(equCode,equName) {
			//textForm.getForm().reset();
			if(equCode!=null&&equCode!=''){
				equCode=equCode;
				equcode.setValue(equCode);
			}
			if (equName!=null&&equName!='') {
				equname.setValue(equName);
			}
		},
		query:querydata
		,reset : function(){
			equcode.setValue('');
			equname.setValue('');
			censtore.removeAll();
		}
	}
	

};