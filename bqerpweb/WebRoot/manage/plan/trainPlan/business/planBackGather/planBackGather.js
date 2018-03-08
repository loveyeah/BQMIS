Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	var month = getParameter("month");
	
	function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";
		// 初始化时间
		Year = DateIn.getYear();
		Month = DateIn.getMonth() + 1;
		Day = DateIn.getDate();
		CurrentDate = Year + "-";
		if (Month >= 10) {
			CurrentDate = CurrentDate + Month;
		} else {
			CurrentDate = CurrentDate + "0" + Month;
		}
		return CurrentDate;
	}
	var enddate = new Date();
	var startdate = enddate.add(Date.MONTH, 1);
	startdate = startdate.getFirstDateOfMonth();

	// 计划时间
	var planTime = new Ext.form.TextField({
		id : 'planTime',
		fieldLabel : '计划时间',
		style : 'cursor:pointer',
		value : month,
		readOnly : true,
		anchor : '80%'
//		listeners : {
//			focus : function() {
//				WdatePicker({
//					startDate : '%y-%M',
//					dateFmt : 'yyyy-MM',
//					alwaysUseStartDate : true,
//					isShowClear : false
//				});
//				this.blur();
//			}
//		}
	});

	var depNameStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'manageplan/findTraingDept.action'
		}),
		reader : new Ext.data.JsonReader({
			id : "dept"
		}, [{
			name : 'id'
		}, {
			name : 'name'
		}])
	});
	depNameStore.on("beforeload", function() {
		Ext.apply(this.baseParams, {
			month : month
		});
	});
	
	var depName = new Ext.form.ComboBox({
		id : 'depName',
		fieldLabel : '部门',
		store : depNameStore,
		valueField : "id",
		displayField : "name",
		mode : 'remote',
		triggerAction : 'all',
		forceSelection : true,
		//hiddenName : 'equ.trainingDep',
		editable : false,
		allowBlank : true,
		selectOnFocus : true,
		readOnly : true,
		//name : 'equ.specialityCode',
		anchor : '70%'
	});
	
//	
//	var depName = new Ext.form.ComboBox({
//		fieldLabel : '部门',
//		mode : 'remote',
//		editable : false,
//		width:120,
//		onTriggerClick : function() {
//			var args = {
//				selectModel : 'single',
//				rootNode : {
//					id : "0",
//					text : '灞桥热电厂'
//				}
//			}
//			var url = "/power/comm/jsp/hr/dept/dept.jsp";
//			var rvo = window
//					.showModalDialog(
//							url,
//							args,
//							'dialogWidth:250px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
//			if (typeof(rvo) != "undefined") {
//				//editDepcode.setValue(rvo.codes);
//				depName.setValue(rvo.names);
//
//			}
//		}
//	});
//	
	var sm = new Ext.grid.CheckboxSelectionModel();

	var Storelist = new Ext.data.Record.create([sm, {
		name : 'trainingMainId'
	}, {
		name : 'approvalId'
	}, {
		name : 'trainingMonth'
	}, {
		name : 'trainingDep'
	}, {
		name : 'trainingDepName'
	}, {
		name : 'trainingTypeId'
	}, {
		name : 'trainingTypeName'
	}, {
		name : 'trainingNumber'
	}, {
		name : 'finishNumber'
	}, {
		name : 'workflowStatus'
	}, {
		name : 'workflowNo'
	}]);

	var store = new Ext.data.JsonStore({
		url : 'manageplan/findTrainPlanBackGatherList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : Storelist
	});

	var grid = new Ext.grid.EditorGridPanel({
		store : store,
		layout : 'fit',
		columns : [new Ext.grid.RowNumberer(), {
			header : "培训部门",
			width : 100,
			sortable : false,
			hidden : true,
			dataIndex : 'trainingDepName'
		}, {
			header : "培训类别",
			width : 100,
			sortable : false,
			dataIndex : 'trainingTypeName'
		}, {
			header : "计划人数",
			width : 100,
			sortable : false,
			css : CSS_GRID_INPUT_COL,
			dataIndex : 'trainingNumber',
			editor:new Ext.form.NumberField({
			maxLength : 100,
			allowDecimals : false
			})
		}, {
			header : "完成人数",
			width : 100,
			id : 'finishNumber',
			sortable : false,
			css : CSS_GRID_INPUT_COL,
			dataIndex : 'finishNumber',
			editor:new Ext.form.NumberField({
			maxLength : 100,
			allowDecimals : false
			})
		}, {
			header : "workflowStatus",
			width : 100,
			sortable : false,
			hidden : true,
			dataIndex : 'workflowStatus'
		}],

		tbar : ["计划时间:", planTime, '-', '部门：',depName, '-', {
			id : 'btnQuery',
			text : "查询",
			iconCls : 'query',
			handler : queryRecord
		}, '-', {
			id : 'btnSave',
			text : "保存",
			iconCls : 'save',
			handler : saveRecord
		},'-',{
			text : '返回',
			iconCls : 'query',
			handler : function(){
				window.location.replace("../../business/planBackGather/completeDescription.jsp?month="+month);
			}
		}],
		sm : sm,
		viewConfig : {
			forceFit : true
		}
	});
	var ids = new Array();
	function saveRecord() {
		grid.stopEditing();
		var modifyRec = grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			if (!confirm("确定要保存修改吗?"))
				return;
			var updateData = new Array();
			var trainingmainId;
			trainingmainId = grid.getStore().getAt(0).get("trainingMainId");
			for (var i = 0; i < grid.getStore().getCount(); i++) {
				var rec = grid.getStore().getAt(i);
				if (rec.get("finishNumber") == null || (rec.get("finishNumber") == "" && rec.get("finishNumber") != "0")) {
		
					Ext.MessageBox.alert('提示', '完成人数不能为空！')
					return
				}
				if (rec.get("trainingNumber") == null || (rec.get("trainingNumber") == "" && rec.get("trainingNumber") != "0")) {
       
					Ext.MessageBox.alert('提示', '计划人数不能为空！')
					return
				} 
				updateData.push({
							id : rec.get("trainingMainId"),       
							tn : rec.get("trainingNumber"),
							fn : rec.get("finishNumber")
						});
			}
			Ext.Ajax.request({
				url : 'manageplan/updateBackGatherRecord.action',
				method : 'post',
				params : {
					isUpdate : Ext.util.JSON.encode(updateData),
					month : planTime.getValue(),
					trainingMainId:trainingmainId
				},
				success : function(result, request) {
					var o = eval('(' + result.responseText + ')');
					Ext.MessageBox.alert('提示信息', o.msg);
					store.rejectChanges();
					queryRecord();
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('提示信息', '未知错误！')
				}
			})
		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}
	
	store.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			planDate : planTime.getValue(),
			depName : Ext.get('depName').dom.value
		});
	});

	function queryRecord() {
		if (planTime.getValue() == null || planTime.getValue() == "") {
			Ext.Msg.alert('提示', '请先选择月份！');
			return;
		}
		if(depName.getValue()==null ||depName.getValue()=="")
		{
			Ext.Msg.alert('提示', '请选择部门！');
			return;
		}
		store.reload({
			params : {
				start : 0,
				limit : 18
			}
		})
	}

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		border : false,
		frame : false,
		items : [{
			region : "center",
			border : false,
			frame : false,
			layout : 'fit',
			height : '50%',
			items : [grid]
		}]
	});

})
