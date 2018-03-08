Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {	
	
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		t = d.getHours();
		s += (t > 9 ? "" : "0") + t + ":";
		t = d.getMinutes();
		s += (t > 9 ? "" : "0") + t + ":";
		t = d.getSeconds();
		s += (t > 9 ? "" : "0") + t;
		return s;
	}
	function getCurrentYear()
	{
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString();		
		return s;
	}
	// 系统当前月
	function getMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString() + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t ;
		return s;
	}
	
	var formatType;
	var yearRadio = new Ext.form.Radio({
			id : 'year',
			name : 'queryWayRadio',
			hideLabel : true,
			boxLabel : '年份'	
		});
	var monthRadio = new Ext.form.Radio({
			id : 'month',
			name : 'queryWayRadio',
			hideLabel : true,
			boxLabel : '月份',
			checked : true,
			listeners : {
				check : function() {
					var queryType = getChooseQueryType();
					switch (queryType) {
						case 'year' : {
							formatType = 1;
							time.setValue(getCurrentYear());
							break;
						}
						case 'month' : {
							time.setValue(getMonth());
							formatType = 2;
							break;
						}
					}
				}
			}
		});	
	var time = new Ext.form.TextField({
		id : 'time',
		allowBlank : true,
		// value : getDate(),
		width : 100,
		readOnly : true,
		listeners : {
			focus : function() {
				var format = '';
				if(formatType == 1)
					format = 'yyyy';
				if(formatType == 2)
					format = 'yyyy-MM';
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : format,
					alwaysUseStartDate : true,
					onclearing : function() {
										planStartDate.markInvalid();
									}
									
				});
			}
		}
	});
	//遍历所有的REDIO获得ID
	function getChooseQueryType() {
		var list = document.getElementsByName("queryWayRadio");
		for (var i = 0; i < list.length; i++) {
			if (list[i].checked) {
				return list[i].id;
			}
		}
	}
	
	var item = Ext.data.Record.create([{
			name : 'businessItemId'
		},{
			name : 'itemName'
		}, {
			name : 'unitId'
		}, {
			name : 'itemName1'
		}, {
			name : 'itemId1'
		},{
			name : 'itemName2'
		},{
			name : 'itemId2'
	}]);

	
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false

	});
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managebudget/getBusinessItemList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalCount : "totalCount",
			root : "list"
		}, item)
	});

	ds.on('load',function(){
		var obj;
		var currentIndex = ds.getCount();
			var materialCostCount = 0;
			var workingCostCount = 0;
			var otherCostCount = 0;
			var deviceCostCount = 0;
			var totalCostCount = 0;
//			for(var i = 0; i <= ds.getTotalCount() - 1; i++)
//			{
//				materialCostCount += ds.getAt(i).get('materialCost');
//				workingCostCount += ds.getAt(i).get('workingCost');
//				otherCostCount += ds.getAt(i).get('otherCost');
//				deviceCostCount += ds.getAt(i).get('deviceCost');
//				totalCostCount += ds.getAt(i).get('totalCost')
//			}
//			obj = new item({
////				  用以标记合计行
//						'capitalId' : null,
//						'project' : '合计',
//						'materialCost' : materialCostCount,
//						'workingCost' : workingCostCount,
//						'otherCost' : otherCostCount,
//						'deviceCost' : deviceCostCount,
//						'totalCost' : totalCostCount,
//						'memo' : null
//					});			
//		}
		Grid.stopEditing();
//		ds.add(obj);
	});
	
	// 计量单位
	var unitData = Ext.data.Record.create([{
		name : 'unitName'
	}, {
		name : 'unitId'
	}]);

	var allUnitStore = new Ext.data.JsonStore({
		url : 'resource/getAllUnitList.action',
		root : 'list',
		fields : unitData
	});
	allUnitStore.load();

	var statItemUnit = new Ext.form.ComboBox({
		fieldLabel : '计量单位',
		store : allUnitStore,
		id : 'unitId',
		name : 'unitId',
		valueField : "unitId",
		displayField : "unitName",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'reportItem.unitId',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : true,
		emptyText : '请选择',
		anchor : '92.5%',
		listeners : {
			render : function() {
				this.clearInvalid();
			}
		}
	});
	
	var itemName = new Ext.form.TriggerField({
		id : "itemName",
		name : "reportItem.itemName",
		fieldLabel : '指标名称',
		 readOnly : true,
		anchor : '92.5%',
		allowBlank : false,
		emptyText : '选择指标',
		onTriggerClick : function(e) {
			var args = {
				selectModel : 'single',
				rootNode : {
					id : "0",
					text : '指标名称'
				}
			}
			var url = "../../../../manage/budget/maint/budgetItem/budgetItemTree.jsp";
			var rvo = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:650px;dialogHeight:450px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (typeof(rvo) != "undefined") {
				var record = Grid.selModel.getSelected()
				record.set("itemName1", rvo.itemName);
				record.set("itemId1", rvo.itemId);
				Grid.getView().refresh();
			}
		},
		listeners : {
				render : function(f) {
					f.el.on('keyup', function(e) {
						Grid.getSelectionModel().getSelected().set("itemId1",'temp');
						Grid.getSelectionModel().getSelected().set("itemName1",'temp');
					});
				}
			}	
	});
	
	var avgItemName = new Ext.form.ComboBox({
		id : "avgItemName",
		name : "reportItem.avgItemName",
		fieldLabel : '指标名称',
		 readOnly : true,
		anchor : '92.5%',
		allowBlank : false,
		emptyText : '选择指标',
		onTriggerClick : function(e) {
				var args = {
					selectModel : 'single',
					rootNode : {
						id : "0",
						text : '指标名称'
					}
				}
				var url = "../../../../manage/budget/maint/budgetItem/budgetItemTree.jsp";
				var rvo = window
						.showModalDialog(
								url,
								args,
								'dialogWidth:650px;dialogHeight:450px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
				if (typeof(rvo) != "undefined") {
					var record = Grid.selModel.getSelected()
					record.set("itemName2", rvo.itemName);
					record.set("itemId2", rvo.itemId);
					Grid.getView().refresh();
				}
			},
			listeners : {
				render : function(f) {
					f.el.on('keyup', function(e) {
						Grid.getSelectionModel().getSelected().set("itemId2",'temp');
						Grid.getSelectionModel().getSelected().set("itemName2",'temp');
					});
				}
			}	
	});
	
	// 事件状态
	var cm = new Ext.grid.ColumnModel([sm,
		new Ext.grid.RowNumberer({
			header : "行号",
			width : 31
			}),
	{
		header : '指标名称',
		dataIndex : 'businessItemId',
		width : 100,
		align : 'center',
		hidden : true,
		editor : new Ext.form.TextField({})
	}, {
		header : '指标名称',
		dataIndex : 'itemName',
		width : 100,
		align : 'center',
		editor : new Ext.form.TextField({})
	}, {
		header : '计量单位',
		dataIndex : 'unitId',
		align : 'center',
		width : 100,
//		css : CSS_GRID_INPUT_COL,
		editor : statItemUnit,
		renderer : function(value) {
			for (i = 0; i < allUnitStore.getCount(); i++) {
				if (allUnitStore.getAt(i).get("unitId") == value)
					return allUnitStore.getAt(i).get("unitName");
			}
		}
	}, {
		header : '预算指标',
		dataIndex : 'itemName1',
		align : 'center',
		width : 150,
//		css : CSS_GRID_INPUT_COL,
		editor : itemName,
		renderer : function(value) {
			return (value);
		}
	}, {
		header : '预算指标',
		dataIndex : 'itemId1',
		align : 'center',
		width : 150,
		hidden : true
//		css : CSS_GRID_INPUT_COL,
	}, {
		header : '平均值预算指标',
		dataIndex : 'itemName2',
		align : 'center',
		width : 120,
//		css : CSS_GRID_INPUT_COL,
		editor : avgItemName
	}, {
		header : '平均值预算指标',
		dataIndex : 'itemId2',
		align : 'center',
		width : 100,
		hidden : true
//		css : CSS_GRID_INPUT_COL,
	}]);
		
	var gridbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : ds,
		displayInfo : true,
		displayMsg : "显示第{0}条到{1}条，共{2}条",
		emptyMsg : "没有记录"
	});
	// 增加
	function addDetail() {
		var currentRecord = Grid.getSelectionModel().getSelected();
		var rowNo = ds.indexOf(currentRecord);
		var count = ds.getCount();
		var currentIndex = count;
		var o = new item({
					'businessItemId' : '',
					'itemName' : '',
					'unitId' : '',
					'itemName1' : '',
					'itemName2' : '',
					'itemId1' : '',
					'itemId2' : ''
				});
		Grid.stopEditing();
		ds.insert(currentIndex, o);
		Grid.getView().refresh();
		sm.selectRow(currentIndex );
		Grid.startEditing(currentIndex , 2);
	}

	// 删除记录
	var ids = new Array();
	function deleteDetail() {
		Grid.stopEditing();
		var sm = Grid.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				
				if (member.get("businessItemId") != null) {
				ids.push(member.get("businessItemId"));
							
				Grid.getStore().remove(member);
				Grid.getStore().getModifiedRecords().remove(member);
				}			
			}
		}
	}
	// 保存
	function saveDetail() {
		Grid.stopEditing();
		var modifyRec = Grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			Ext.Msg.confirm('提示信息', '确定要保存修改吗？', function(button) {
				if (button == 'yes') {
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						updateData.push(modifyRec[i].data);
					}
					Ext.Ajax.request({
								url : 'managebudget/saveOrUpdateBusinessItem.action',
								method : 'post',
								params : {
									isUpdate : Ext.util.JSON.encode(updateData),
									ids : ids.join(",")
								},
								success : function(result, request) {
									var o = eval('(' + result.responseText
											+ ')');
									Ext.Msg.alert("提示信息", o.msg);
										ds.rejectChanges();
										ids = [];
										ds.reload();
								},
								failure : function(result, request) {
									// var o = eval("(" + result.responseText
									// + ")");
									Ext.Msg.alert("提示信息", "数据保存修改失败！");
									ds.rejectChanges();
									ids = [];
									ds.reload();
								}
							})
				}
			})

		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}
	// 取消
	function cancerDetail() {
		var modifyRec = ds.getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			Ext.Msg.confirm('提示信息', '确定要放弃修改吗？', function(button) {
						if (button == 'yes') {
							ds.reload();
							ds.rejectChanges();
							ids = [];
						} 
					})
		}
		else
		ds.reload()
	}
	function fuzzyQuery() {
		ds.baseParams = {
			budgetTime : time.getValue()
		};
		ds.load({
			params : {
//				start : 0,
//				limit : 18
			}
		});
		ids = [];
	};
	
	
	// tbar
	var contbar = new Ext.Toolbar({
		items : [{
			id : 'btnAdd',
			iconCls : 'add',
			text : "新增",
			handler : addDetail
		}, '-',{
			id : 'btnDelete',
			iconCls : 'delete',
			text : "删除",
			handler : deleteDetail

		},'-', {
			id : 'btnCancer',
			iconCls : 'cancer',
			text : "取消",
			handler : cancerDetail

		}, '-', {
			id : 'btnSave',
			iconCls : 'save',
			text : "保存修改",
			handler : saveDetail
		}]

	});
	var Grid = new Ext.grid.EditorGridPanel({
		sm : sm,
		ds : ds,
		cm : cm,
		autoScroll : true,
//		bbar : gridbbar,
		tbar : contbar,
		border : true,
		clicksToEdit : 1,
		viewConfig : {
		// forceFit : true
		}
	});
	
//	 Grid.on('beforeedit', function(obj) {
//	 	if(obj.row == ds.getCount() -1)
//	 	{
//	 		return false;
//	 	}
//	 	if (obj.record.get('workFlowStatus') == 1
//						|| obj.record.get('workFlowStatus') == 2) {
//					return false;
//				}
////	 	if(obj.record.get('project') == '合计'){
////    			return false;
////    		}  		
//    	return true;
//    });

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		border : false,
		frame : false,
		items : [{
			layout : 'fit',
			border : false,
			frame : false,
			region : "center",
			items : [Grid]
		}]
	});
  function reportRec()
  {
  	if (ds.getTotalCount() > 0) {
  		if(ds.getAt(0).get('budgetTime') != time.getValue())
			{
				Ext.Msg.alert('提示信息','请先查询数据！');
				return;
			}
			if (ds.getAt(0).get('workFlowStatus') == '1'
					|| ds.getAt(0).get('workFlowStatus') == '2') {
				Ext.Msg.alert('提示信息', '审批中和审批通过的数据不允许上报！')
				return;
			}
		var url = "../capitalApprove/report.jsp";
			var args = new Object();
			args.entryId = ds.getAt(0).get('workFlowNo');
			args.workflowType = "budgetDetailApprove";
			args.capitalId =ds.getAt(0).get('capitalId');
			var obj = window.showModalDialog(url, args,
					'status:no;dialogWidth=750px;dialogHeight=550px');
			// 按钮设为不可用
			if(obj)
			{		
			      fuzzyQuery();
		    }
	}
	else{
		Ext.Msg.alert('提示信息','无数据可上报！');
		return;
	}
  	
  }
   function judgeQuery()
  {
  	if (ds.getTotalCount() > 0) {
  		if(ds.getAt(0).get('budgetTime') != time.getValue())
			{
				Ext.Msg.alert('提示信息','请先查询数据！');
				return;
			}
			var entryId=ds.getAt(0).get('workFlowNo');
			if(entryId==null||entryId=="")
			{
			  Ext.Msg.alert("提示","无审批信息！");	
			  return ;
			}
			var url="../../../../workflow/manager/show/show.jsp?entryId="+entryId;
			window.open(url);			
		}
		else{
		Ext.Msg.alert('提示信息','无数据进行审批查询！');
		return;
	}
  
  }
  
  fuzzyQuery();
})