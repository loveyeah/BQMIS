Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var MyRecord = Ext.data.Record.create([{
		name : 'id'
	}, {
		name : 'reportId'
	}, {
		name : 'itemCode'
	}, {
		name : 'itemAlias'
	}, {
		name : 'dataType'
	}, {
		name : 'orderBy'
	}, {
		name : 'bpCSmallitemRowtype.rowDatatypeId'
	}
	,{
		name : 'compluteMethod'
	}
	,{
		name : 'isIgnoreZero'
	}]);

	var dataProxy = new Ext.data.HttpProxy({
		url : 'manager/getRelationItems.action'
	});

	var theReader = new Ext.data.JsonReader({
			// root : "list",
			// totalProperty : "totalCount"
			}, MyRecord);

	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
	store.on("beforeload",function(){
		Ext.Msg.wait("正在查询数据,请等待...") ;
	});
	store.on("load",function(){
		Ext.Msg.hide();
	});
	var sm = new Ext.grid.CheckboxSelectionModel({
			// singleSelect : true
	});

	number = new Ext.grid.RowNumberer({
		header : "",
		align : 'left'
	})

	var dataTypeBox = new Ext.form.ComboBox({
		// fieldLabel : '数据类型',
		store : new Ext.data.SimpleStore({
			fields : ['value', 'text'],
			data : [['0', '整数'], ['1', '一位小数'], ['2', '二位小数'], ['3', '三位小数'],
					['4', '四位小数']]
		}),
		id : 'dataType',
		name : 'dataTypeText',
		valueField : "value",
		displayField : "text",
		mode : 'local',
		value : "0",
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'model.dataType',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false
	});
	
	var isIgnorZeroBox = new Ext.form.ComboBox({
		// fieldLabel : '数据类型',
		store : new Ext.data.SimpleStore({
			fields : ['value', 'text'],
			data : [['1', '是'], ['0', '否']]
		}),
		id : 'dataType',
		name : 'dataTypeText',
		valueField : "value",
		displayField : "text",
		mode : 'local',
		value : "0",
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'model.dataType',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true 
	});
	
	var compluteMethodBox = new Ext.form.ComboBox({
		// fieldLabel : '数据类型',
		store : new Ext.data.SimpleStore({
			fields : ['value', 'text'],
			data : [['ADD', '求合计'], ['AVG', '求平均'],['MAX','最大值'],['MIN','最小值']]
		}),
		id : 'dataType',
		name : 'dataTypeText',
		valueField : "value",
		displayField : "text",
		mode : 'local',
		value : "0",
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'model.dataType',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true 
	});
	var rowData = Ext.data.Record.create([{
		name : 'rowDatatypeName'
	}, {
		name : 'rowDatatypeId'
	}]);

	var roweStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'manager/findSmallItemReportRowTypeList.action'
		}),
		reader : new Ext.data.JsonReader({}, rowData)
	});

	var rowTypeBox = new Ext.form.ComboBox({
		fieldLabel : '行名称',
		store : roweStore,
		id : 'rowType',
		name : 'rowType',
		valueField : "rowDatatypeId",
		displayField : "rowDatatypeName",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		editable : false,
		hiddenName : 'bpCSmallitemRowtype.rowDatatypeId',
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : true,
		emptyText : '请选择',
		anchor : '85%'
	});
	// 定义grid
	var grid = new Ext.grid.EditorGridPanel({
		store : store,
		layout : 'fit',
		columns : [sm, number,  {
			header : "指标编码",
			sortable : false,
			dataIndex : 'itemCode'
		}, {
			header : "列头名称",
			align : "center",
			width:130,
			sortable : false,
			dataIndex : 'itemAlias',
			renderer:function(value, metadata, record){ 
				metadata.attr = 'style="white-space:normal;"'; 
				return value;  
			},
			editor : new Ext.form.TextField({})
		},{
			header : "行头名称",
			align : "left",
			width:100,
			sortable : false,
			dataIndex : 'bpCSmallitemRowtype.rowDatatypeId',
			editor : rowTypeBox,
			renderer : function changeIt(val) { 
				for (i = 0; i < roweStore.getCount(); i++) {
					if (roweStore.getAt(i).get("rowDatatypeId") == val)
						return roweStore.getAt(i).get("rowDatatypeName");
				}
			}
		} , {
			header : "数据类型",
			align : "center",
			sortable : false,
			dataIndex : 'dataType',
			editor : dataTypeBox,
			renderer : function idByType(type) {
				if (type == 0) {
					return "整数"
				} else if (type == 1) {
					return "一位小数"
				} else if (type == 2) {
					return "二位小数"
				} else if (type == 3) {
					return "三位小数"
				} else if (type == 4) {
					return "四位小数"
				}
			}
		}, {
			header : "显示顺序",
			align : "center",
			sortable : false,
			dataIndex : 'orderBy',
			editor : new Ext.form.NumberField({
				maxLength : 10
			})
		}
		,{
			header:'计算方法',
			align:'center',
			editor:compluteMethodBox,
			dataIndex:'compluteMethod',
			renderer:function(v)
			{
				var dv = "";
				switch (v) {
					case 'ADD' : 
						dv = "求合计";
						break;
					case 'AVG':
					    dv = "求平均";
					    break;
					case 'MAX':
						dv = '最大值';
						break;
					case 'MIN':
						dv="最小值";
						break;
				}
				return dv;
			} 
		} ,{
			header:'忽略零值',
			align:'center',
			editor:isIgnorZeroBox,
			dataIndex : 'isIgnoreZero',
			renderer:function(value, metadata, record, rowIndex, colIndex, store){
				if (record.get("compluteMethod")=='AVG') 
				return value=="1"?"是":"否";
			}
		}],
		tbar : [{
			text : "指标选择",
			iconCls : 'add',
			handler : addRecord
		},'-',{
			text : "新增计算值",
			iconCls : 'add',
			handler : addTotalRecord
		}, '-', {
			text : "删除",
			iconCls : 'delete',
			handler : deleteRecords
		}, '-', {
			text : "保存",
			iconCls : 'save',
			handler : saveModifies
		}, '-', {
			text : "取消",
			iconCls : 'cancer',
			handler : cancel
		},'-','<font color="red">相同别名的指标显示顺序请设置为相同！</font>'],
		sm : sm, 
		clicksToEdit : 1
	});
	grid.getColumnModel().defaultSortable = false;
	 
	var con_item = Ext.data.Record.create([{
		name : 'reportId'
	}, {
		name : 'reportName'
	}, {
		name : 'dataType'
	}, {
		name : 'rowHeadName'
	}, {
		name : 'isShowTotal'
	}, {
		name : 'columnNum'
	}]);

	var con_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});
	var con_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'manager/findSmallItemReportList.action' // 小指标报表项目名称
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : "totalCount",
			root : "list"
		}, con_item)
	});
	con_ds.load({
		params : {
			start : 0,
			limit : 18
		}
	})

	var con_item_cm = new Ext.grid.ColumnModel([ {
		header : '小指标报表名称',
		dataIndex : 'reportName',
		align : 'left',
		width : 200
	},{
		header : '编码',
		dataIndex : 'reportId',
		align : 'center',
		width : 50
	}]);
	con_item_cm.defaultSortable = false;

	var gridbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : con_ds,
		displayInfo : true,
		displayMsg : "共{2}条",
		emptyMsg : "没有记录",
		beforePageText : '',
		afterPageText : ""
	});

	var Grid = new Ext.grid.GridPanel({
//		title : '小指标报表列表',
		ds : con_ds,
		cm : con_item_cm,
		height : 425,
		split : true,
		autoScroll : true,
		bbar : gridbbar,
		border : true
	});
	Grid.on('rowclick', modifyBtn); 
	 
	function modifyBtn() {
		var recL = Grid.getSelectionModel().getSelected();
		if (recL) {
			roweStore.load({
				params : {
					reportId : recL.get("reportId")
				}
			});
			store.on('beforeload', function() {
				Ext.apply(this.baseParams, {
					reportId : recL.get("reportId")
				});
			})
			store.load({
				params : {
					reportId : recL.get("reportId"),
					start : 0,
					limit : 18
				}
			});
		} else {
			Ext.Msg.alert("提示", "请选择左边的一条记录！")
		}
	}
	//增加统计数据
	function addTotalRecord(){
//		commwin.show(); 
		var recL = Grid.getSelectionModel().getSelected();  
				var currentIndex = store.getCount()
				var rvo = new Object(); 
				rvo.reportId = recL.get("reportId")
				rvo.itemCode =''; 
				rvo.itemName = '';
				rvo.displayNo= '';  
				rvo.compluteMethod='ADD';
				rvo.isIgnoreZero = '1';//忽略零值 
				insertOneRecord(currentIndex,rvo);　
	};

	// 指标选择
	function addRecord() {
		var recL = Grid.getSelectionModel().getSelected();
		if (recL) {
			var args = {
				reportId : recL.get("reportId"),
				reportType : recL.get("dataType")
			}
			var url = "selectItem.jsp";
			var rvo = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:600px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(rvo) != "undefined") {
				var i;
				for (i = 0; i < rvo.length; i++) {
					var count = store.getCount();
					var currentIndex = count; 
					insertOneRecord(currentIndex,rvo[i]);  
				}
			}
		} else {
			Ext.Msg.alert("提示", "请选择左边的一条记录！")
		}
	}
	function insertOneRecord(currentIndex, rvo) {
		var checkSame = true;
		
		for (var j = 0; j < store.getCount(); j++) {
			if (store.getAt(j).get("itemCode") == rvo.itemCode) {
				checkSame = false;
				break;
			}
		}
		if (checkSame) {
			var o = new MyRecord({
				'id' : '',
				'reportId' : '',
				'itemCode' : '',
				'itemAlias' : '',
				'dataType' : '0',
				'orderBy' : '',
				'compluteMethod':'',
				'isIgnoreZero':'',
				'bpCSmallitemRowtype.rowDatatypeId' : ''
			});
			grid.stopEditing();
			store.insert(currentIndex, o);
			sm.selectRow(currentIndex); 
			o.set("reportId", rvo.reportId);
//			o.set("isItem",rvo.isItem);
			o.set("isIgnoreZero",rvo.isIgnoreZero);
			o.set("itemCode", rvo.itemCode);
			o.set("itemAlias", rvo.itemName);
			o.set("orderBy", rvo.displayNo);
			o.set("bpCSmallitemRowtype.rowDatatypeId", '');
			o.set("compluteMethod", rvo.compluteMethod);
			o.set("isIgnoreZero", rvo.isIgnoreZero);
		}
	}

	// 删除记录
	var ids = new Array();
	function deleteRecords() {
		var sm = grid.getSelectionModel();
		var selections = sm.getSelections();
		if (selections.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selections.length; i += 1) {
				var member = selections[i];
				var id = new Array();
				var code = new Array();
				if (member.get("id") != null && member.get("id") != "") {
					ids.push(member.get("id"));
				}
				grid.getStore().remove(member);
				grid.getStore().getModifiedRecords().remove(member);
			}
		}
	}
	// 保存
	function saveModifies() {
		grid.stopEditing();
		var modifyRec = grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			Ext.Msg.confirm('提示', '确定要保存修改吗?', function(b) {
				if (b == 'yes') {
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) { 
						if (modifyRec[i].data.itemAlias == "") {
							Ext.MessageBox.alert('提示信息', '指标别名不能为空！')
							return
						}
						if (modifyRec[i]
								.get('bpCSmallitemRowtype.rowDatatypeId') == "") {
							Ext.MessageBox.alert('提示信息', '行名称不能为空！')
							return
						} 
						updateData.push(modifyRec[i].data);
					}

					Ext.Ajax.request({
						url : 'manager/smallReprtItemsMaint.action',
						method : 'post',
						params : {
							isUpdate : Ext.util.JSON.encode(updateData),
							isDelete : ids.join(",")
						},
						success : function(result, request) {
							var o = eval('(' + result.responseText + ')');
							Ext.MessageBox.alert('提示信息', o.msg);
							store.rejectChanges();
							ids = [];
							store.reload();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('提示信息', '发生错误！')
						}
					})
				}
			})
		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}
	// 取消
	function cancel() {
		var modifyRec = store.getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			Ext.Msg.confirm('提示', '确定要放弃修改吗?', function(b) {
				if (b == "yes") {
					store.reload();
					store.rejectChanges();
					ids = [];
				} else {
					return;
				}
			})
		} else {
			store.reload();
			store.rejectChanges();
			ids = [];
		}
	}

	new Ext.Viewport({
		enableTabScroll : true, 
		layout : "border",
		border : false,
	    split:true, 
		frame : false,
		items : [{
			split : true,
			bodyStyle : "padding: 2,2,2,2",
			layout : 'fit',
			border : false,
			frame : false,
			region : "west",
			width : '25%',
			items : [Grid]
		}, {
			bodyStyle : "padding: 2,2,2,2",
			region : "center",
			border : false,
			frame : false,
			layout : 'fit',
			items : [grid]
		}]
	});
	
	/**
	 * 选择公用行类型
	 */
//	var computeMethodData = Ext.data.Record.create([{
//		name : 'value'
//	}, {
//		name : 'key'
//	}]);
//	var computeMethodStore = new Ext.data.JsonStore({
//		url : 'comm/getBpBasicDataByType.action?type=COMPUTE_METHOD',
//		fields : computeMethodData
//	});
//	computeMethodStore.load();
//	var computeMethodGrid = new Ext.grid.GridPanel({ 
//		ds : computeMethodStore,
//		cm : new Ext.grid.ColumnModel([
//			{
//				header : '编码',
//				dataIndex : 'key',
//				width : 40,
//				align : 'left' 
//			},
//			{
//				header : '名称',
//				dataIndex : 'value', 
//				align : 'left' 
//			}
//		]),  
//		viewConfig : {
//			forceFit : true
//		},
//		autoWidth : true,
//		border : false ,
//		listeners:{
//			'rowdblclick':function(grid, rowIndex, e){
//				var recL = Grid.getSelectionModel().getSelected(); 
//				var rec = grid.getStore().getAt(rowIndex); 
//				var currentIndex = store.getCount()
//				var rvo = new Object(); 
//				rvo.reportId = recL.get("reportId")
//				rvo.itemCode = rec.get("key"); 
//				rvo.itemName = rec.get("value");
//				rvo.displayNo= ''; 
////				rvo.isItem = '0';
//				rvo.isIgnoreZero = '1';//忽略零值 
//				insertOneRecord(currentIndex,rvo);
//				commwin.hide(); 
//			}
//		}
//	});
//	var commwin = new Ext.Window({
//		title : "公共行类型选择",
//		closeAction : 'hide',
//		width : 300,
//		height : 200,
//		plain : true,
//		modal : true,
//		layout : 'fit',
//		items : [computeMethodGrid]
//	}); 
})