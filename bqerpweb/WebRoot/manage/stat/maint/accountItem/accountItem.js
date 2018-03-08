Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	var MyRecord = Ext.data.Record.create([{
		name : 'baseInfo.id.accountCode'
	}, {
		name : 'baseInfo.id.itemCode'
	}, {
		name : 'baseInfo.displayNo'
	}, {
		name : 'baseInfo.dataType'
	}, {
		name : 'baseInfo.itemAlias'
	}, {
		name : 'baseInfo.itemBaseName'
	}, {
		name : 'itemName'
	}]);

	var dataProxy = new Ext.data.HttpProxy({
		url : 'manager/getBpCAnalyseAccountItem.action '
	});

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"

	}, MyRecord);

	var store = new Ext.data.Store({

		proxy : dataProxy,

		reader : theReader

	});

	// store.load({
	// params : {
	// start : 0,
	// limit : 18
	// }
	// });

	var sm = new Ext.grid.CheckboxSelectionModel();

	number = new Ext.grid.RowNumberer({
		header : "",
		align : 'left'
	})

	// 指标选择
	function addRecord() {
		var recL = Grid.getSelectionModel().getSelected();

		if (recL) {
			var args = {
				accountType : recL.get("accountType"),
				accountCode : recL.get("accountCode")
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

					// var currentRecord =
					// grid.getSelectionModel().getSelected();
					// var rowNo = store.indexOf(currentRecord);

					var count = store.getCount();

					// var currentIndex = currentRecord ? rowNo : count;
					var currentIndex = count;

					var o = new MyRecord({
						'baseInfo.id.accountCode' : '',
						'baseInfo.id.itemCode' : '',
						'baseInfo.itemAlias' : '',
						'baseInfo.itemBaseName' : '',
						'baseInfo.dataType' : '0',
						'baseInfo.displayNo' : '',
						'itemName' : ''
					});

					grid.stopEditing();

					store.insert(currentIndex, o);
					sm.selectRow(currentIndex);
					o.set("baseInfo.id.accountCode", rvo[i].accountCode);
					o.set("baseInfo.id.itemCode", rvo[i].itemCode);
					o.set("baseInfo.displayNo", rvo[i].displayNo);
					o.set("itemName", rvo[i].itemName);
					o.set("baseInfo.itemAlias", rvo[i].itemName);
					grid.startEditing(currentIndex, 3);

				}
			}

		} else {
			Ext.Msg.alert("提示", "请选择左边的一条记录！")
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

				if (member.get("baseInfo.id.accountCode") != null
						&& member.get("baseInfo.id.itemCode")) {

					id.push(member.get("baseInfo.id.accountCode"));
					id.push(member.get("baseInfo.id.itemCode"));
					var idstr = '';
					idstr = id.join(",");

					ids.push(idstr);
				}
				grid.getStore().remove(member);
				grid.getStore().getModifiedRecords().remove(member);
			}
			// resetLine();
		}
	}
	// 保存
	function saveModifies() {
		grid.stopEditing();
		var modifyRec = store.getModifiedRecords();

		if (modifyRec.length > 0 || ids.length > 0) {
			if (!confirm("确定要保存修改吗?"))
				return; 
			var updateData = new Array();
			for (var i = 0; i < modifyRec.length; i++) { 
				updateData.push(modifyRec[i].data); 
			} 
			Ext.Ajax.request({
				url : 'manager/saveBpCAnalyseAccountItem.action',
				method : 'post',
				params : {
					isUpdate : Ext.util.JSON.encode(updateData),
					isDelete : ids.join(";")
				},
				success : function(result, request) {
					var o = eval('(' + result.responseText + ')');
					Ext.MessageBox.alert('提示信息', o.msg);
					store.rejectChanges();
					ids = [];
					store.reload();
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('提示信息', '未知错误！')
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
			if (!confirm("确定要放弃修改吗"))
				return;
			store.reload();
			store.rejectChanges();
			ids = [];
		} else {
			store.reload();
			store.rejectChanges();
			ids = [];
		}
	}

	// 定义grid

	var grid = new Ext.grid.EditorGridPanel({
		// region : "west",
		store : store,
		layout : 'fit',
		// width:'0.5',
		columns : [

		sm,	// 选择框
				number, {
					header : "一级名称",

					sortable : false,
					dataIndex : 'baseInfo.itemBaseName',
					editor : new Ext.form.TextField({})

				}, {
					header : "指标编码",

					sortable : false,
					dataIndex : 'baseInfo.id.itemCode'

				}, {
					header : "指标名称",

					align : "center",
					sortable : false,
					dataIndex : 'itemName',
					renderer:function(value, metadata, record){ 
							metadata.attr = 'style="white-space:normal;"'; 
							return value;//modify by ywliu 20090911  
					}
				}, {
					header : "指标别名",

					align : "center",
					sortable : false,
					dataIndex : 'baseInfo.itemAlias',
					editor : new Ext.form.TextField({})
				}, {
					header : "数据类型",

					align : "center",
					sortable : false,
					dataIndex : 'baseInfo.dataType',

					editor : new Ext.form.ComboBox({
						// fieldLabel : '数据类型',
						store : new Ext.data.SimpleStore({
							fields : ['value', 'text'],
							data : [['0', '整数'], ['1', '一位小数'], ['2', '二位小数'],
									['3', '三位小数'], ['4', '四位小数']]
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
					}),
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
					sortable : true,
					dataIndex : 'baseInfo.displayNo',
					editor : new Ext.form.NumberField({
						maxLength : 10
					})

				}],
		tbar : [{
			text : "指标选择",
			iconCls : 'add',

			handler : addRecord
		}, '-'

		, {
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
		}],
		sm : sm, // 选择框的选择 Shorthand for
		// selModel（selectModel）
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第{0}条到{1}条，共{2}条",
			emptyMsg : "没有记录",
			beforePageText : '',
			afterPageText : ""
		})
	});

	var con_item = Ext.data.Record.create([{
		name : 'accountCode'
	}, {
		name : 'accountName'
	}, {
		name : 'accountType'
	}]);

	var con_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true

	});
	var con_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'manager/getBpCAnalyseAccountList.action'
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

	var con_item_cm = new Ext.grid.ColumnModel([

	{
		header : '台帐编码',

		dataIndex : 'accountCode',
		align : 'center',
		hidden : 'true'//modify by ywliu 20090911
	}, {
		header : '台帐名称',
		dataIndex : 'accountName',
		align : 'center',
		width : 180//modify by ywliu 20090911
	}]);
	con_item_cm.defaultSortable = true;

	var gridbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : con_ds,
		displayInfo : true,
		displayMsg : "",
		emptyMsg : "没有记录",
		beforePageText : '',
		afterPageText : ""
	});

	var Grid = new Ext.grid.GridPanel({

		ds : con_ds,
		cm : con_item_cm,
		height : 425,
		// // title : '合同列表',
		// width : Ext.get('div_lay').getWidth(),
		split : true,
		autoScroll : true,
		bbar : gridbbar,
		// tbar : contbar,
		
		border : true
			// ,
			// viewConfig : {
			// forceFit : true
			// }
	});

	Grid.on('rowclick', modifyBtn);
	function modifyBtn() {
		var recL = Grid.getSelectionModel().getSelected();

		if (recL) {
			store.on('beforeload', function() {
				Ext.apply(this.baseParams, {
					accountCode : recL.get("accountCode")
				});
			})
			store.load({
				params : {
					accountCode : recL.get("accountCode"),
					start : 0,
					limit : 18
				}
			});

		} else {
			Ext.Msg.alert("提示", "请选择左边的一条记录！")
		}
	}

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		border : false,
		frame : false,
		items : [{
			layout : 'fit',
			border : false,
			bodyStyle : "padding: 2,2,2,2",
			frame : false,
			region : "west",
			width : '25%',
			items : [Grid]
		}, {

			region : "center",
			border : false,
			bodyStyle : "padding: 2,2,2,2",
			frame : false,
			layout : 'fit',
			// width : '50%',
			items : [grid]
		}]
	});

})