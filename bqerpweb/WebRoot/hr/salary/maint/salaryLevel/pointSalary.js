Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.ns("GangLevel.pointSalary");
	GangLevel.pointSalary = function() {
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'pointSalaryId'
	}, {
		name : 'checkStationGrade'
	}, {
		name : 'salaryLevel'
	}, {
		name : 'jobPoint'
	},{
		name : 'memo'	
	},{
		name : 'checkStationName'
	},{
		name : 'levelName'
	}]);

	var dataProxy = new Ext.data.HttpProxy({
		url : 'hr/fingAllPointSalary.action'
	});

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);

	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
	
	store.on('load', function() {
				if (store.getTotalCount() > 0) {
					for (var i = 0; i < store.getTotalCount(); i++) {
						if (store.getAt(i).get('checkStationGrade') == null
								|| store.getAt(i).get('checkStationGrade') == '') {
							store.getAt(i).set('checkStationGrade',
									checkStationGrade.getValue());
							store.getAt(i).set('checkStationName',
									Ext.get('checkStationGrade').dom.value)
						}

					}
				}
			})

	// 岗位级别:
	var stationLestore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'empInfoManage.action?method=getStationlevel'
						}),
				reader : new Ext.data.JsonReader({
						// id : "plant"
						}, [{
							name : 'id'
						}, {
							name : 'text'
						}])
			});
	stationLestore.load();
	var checkStationGrade = new Ext.form.ComboBox({
		fieldLabel : '岗级',
		name : 'checkStationGrade',
//		xtype : 'combo',
		id : 'checkStationGrade',
		store : stationLestore,
		valueField : "id",
		displayField : "text",
		mode : 'remote',
		typeAhead : true,
		forceSelection : true,
//		hiddenName : 'checkStation',
		editable : false,
		triggerAction : 'all',
		width : 120,
		selectOnFocus : true
	});
	checkStationGrade.on('select',function(){
		queryRecord()
	})
	var sm = new Ext.grid.CheckboxSelectionModel();

	var grid = new Ext.grid.EditorGridPanel({
		region : "center",
		layout : 'fit',
		store : store,
		clicksToEdit : 1,
		columns : [sm, new Ext.grid.RowNumberer({
			header : '序号',
			width : 35,
			align : 'left'
		}), {
			header : "岗位薪点工资ID",
			width : 75,
			sortable : true,
			dataIndex : 'pointSalaryId',
			hidden : true
		}, {
			header : "岗级ID",
			width : 75,
			hidden : true,
			sortable : true,
			dataIndex : 'checkStationGrade'
		}, {
			header : "岗级名称",
			width : 75,
			hidden : true,
			sortable : true,
			dataIndex : 'checkStationName'
		}, {
			header : "薪级ID",
			width : 75,
			hidden : true,
			sortable : true,
			dataIndex : 'salaryLevel'
		}, {
			header : "薪级",
			width : 120,
			sortable : true,
			dataIndex : 'levelName'
		}, {
			header : "薪点",
			width : 120,
			sortable : true,
			dataIndex : 'jobPoint',
			editor : new Ext.form.NumberField({
				allowDecimals : true,
				allowNegative : false,
				decimalPrecision : 1
			})
		}, {
			header : "备注",
			width : 150,
			hidden : false,
			sortable : true,
			dataIndex : 'memo',
			editor : new Ext.form.TextField({})
		}],
		sm : sm,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : false
		},
		tbar : ['岗级:',checkStationGrade, {
			text : "保存",
			iconCls : 'save',
			handler : saveRecords
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
	// ---------------------------------------

//	new Ext.Viewport({
//		enableTabScroll : true,
//		layout : "border",
//		items : [grid]
//	});

	// -------------------

	function queryRecord() {
		store.baseParams = {
			checkGrade : checkStationGrade.getValue()
		}
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}
	function saveRecords()
	{
		if(checkStationGrade.getValue() == null || checkStationGrade.getValue() == '')
		{
			Ext.Msg.alert('提示','请先选择一个岗级！');
			return;
		}
		var num = store.getTotalCount();
		if(num > 0)
		{
			Ext.Msg.confirm('提示','确认要保存数据吗？',function(button){
				if(button == 'yes')
				{
					var add = new Array();
					var update = new Array();
					for (var i = 0; i < num; i++) {
								if (store.getAt(i).get('pointSalaryId') == null
										|| store.getAt(i).get('pointSalaryId') == '') {
									add.push(store.getAt(i).data);
								} else
									update.push(store.getAt(i).data)
							}
					Ext.Ajax.request({
						url : 'hr/savePointSalaryRec.action',
						method : 'post',
						params : {
							add : Ext.encode(add),
							update : Ext.encode(update)
						},
						success : function(result,request){
							Ext.Msg.alert('提示','数据保存成功！')
							store.reload();
						},
						failure : function(result,request){
							Ext.Msg.alert('提示','出现错误！');
						}
					})
				}
			})
		}
		else
		{
			Ext.Msg.alert('提示','无数据保存！');
			return;
		}
	}
	
	return {
			grid : grid
		}
	};
});