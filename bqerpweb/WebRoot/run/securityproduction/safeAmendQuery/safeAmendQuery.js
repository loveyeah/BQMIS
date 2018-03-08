Ext.onReady(function(){
		// 整改
	var detailRec = Ext.data.Record.create([{
		name : 'amend.amendId'
	}, {
		name : 'amend.checkupId'
	}, {
		name : 'amend.existProblem'
	}, {
		name : 'amend.amendMeasure'
	}, {
		name : 'amend.beforeAmendMeasure'
	}, {
		name : 'amend.chargeDept'
	}, {
		name : 'amend.chargeBy'
	}, {
		name : 'amend.superviseDept'
	}, {
		name : 'amend.superviseBy'
	}, {
		name : 'amend.noAmendReason'
	}, {
		name : 'amend.problemKind'
	}, {
		name : 'amend.modifyBy'
	}, {
		name : 'planFinishDate'
	}, {
		name : 'amendFinishDate'
	}, {
		name : 'chargeDeptName'
	},{
		name : 'chargeName'
	},{
		name : 'superviseDeptName'
	},{
		name : 'superviseName'
	},{
		name : 'modifyName'
	},{
		name : 'modifyDate'
	},{
		name : 'proType'
	},{
		name : 'specialCode'
	},{
		name : 'specialName'
	}]);
	// 检查store 
	var amendStore = new Ext.data.JsonStore({
		url : 'security/getSafeAmendList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : detailRec
	})
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	});
	var cm = new Ext.grid.ColumnModel([sm, new Ext.grid.RowNumberer({
		header : '行号',
		width : 32
	}), {
		header : '项目类别',
		dataIndex : 'proType',
		sortable : true
		}, {
		header : '专业',
		dataIndex : 'specialName',
		sortable : true
		}, {
		header : '存在的问题',
		dataIndex : 'amend.existProblem',
		sortable : true
		}, {
		header : '整改措施',
		dataIndex : 'amend.amendMeasure',
		sortable : true
		}, {
		header : '整改前的防范措施',
		dataIndex : 'amend.beforeAmendMeasure',
		sortable : true
		}, {
		header : '计划完成时间',
		dataIndex : 'planFinishDate',
		sortable : true
		}, {
		header : '整改完成时间',
		dataIndex : 'amendFinishDate',
		sortable : true
		}, {
		header : '整改责任部门',
		dataIndex : 'chargeDeptName',
		sortable : true
		}, {
		header : '整改责任人',
		dataIndex : 'chargeName',
		sortable : true
		}, {
		header : '整改监督部门',
		dataIndex : 'superviseDeptName',
		sortable : true
		}, {
		header : '整改监督人',
		dataIndex : 'superviseName',
		sortable : true
		}, {
		header : '未整改原因',
		dataIndex : 'amend.noAmendReason',
		sortable : true
		}, {
		header : '问题性质',
		dataIndex : 'amend.problemKind',
		sortable : true,
		renderer : function(v){
			if(v == '1')
				return '一般问题';
			else if(v == '2')
				return '重大问题';
		}
		}
	]);

	var grid = new Ext.grid.GridPanel({
		store : amendStore,
		cm : cm,
		sm : sm,
		autoScroll : true,
		frame : false,
		border : false,
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : amendStore,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});
	amendStore.load({
		params : {
			start : 0,
			limit : 18
		}
	})
	var layout = new Ext.Viewport({
		layout : "border",
		border : false,
		items : [{
			title : '25项反措整改计划',
			region : "center",
			split : true,
			collapsible : false,
			titleCollapse : false,
			margins : '1',
			layout : 'fit',
			items : [ {
				region : "center",
				layoutConfig : {
					animate : true
				},	
				layout : 'fit',
				border : false,
				items : [grid]
			}]
			}]
	});
})