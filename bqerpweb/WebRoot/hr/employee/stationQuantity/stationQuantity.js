Ext.BLANK_IMAGE_URL = 'ext/resources/images/default/s.gif';
Ext.onReady(function() {
	
	var sm = new Ext.grid.CheckboxSelectionModel({
	singleSelect : false
	});
	// grid列表数据源
	

	var Record = new Ext.data.Record.create([sm, {
				name : 'quantifyId',
				mapping:0
			}, {
				name : 'stationName',
				mapping:1
			}, {
				name : 'quantifyProportion',
				mapping:2
			}]);
	
var store = new Ext.data.JsonStore({
				url : 'hr/getStationQuantity.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : Record
			});
	
	
	function addRecord() {
		
	
		var count = store.getCount();
		var currentIndex = count;	
		var o = new Record({
					'quantifyId' : '',
					'stationName' : '',									
					'quantifyProportion' : ''								
				
				});
		stationGrid.stopEditing();
		store.insert(currentIndex, o);
		sm.selectRow(currentIndex);
		stationGrid.startEditing(currentIndex, 1);
		}

	

	function saveRecord() {
		var alertMsg = "";
		stationGrid.stopEditing();
		var modifyRec = stationGrid.getStore().getModifiedRecords();
		if(modifyRec.length>0)
		{
		Ext.Msg.confirm('提示', '确定要保存修改数据吗?', function(button) {
					if (button == 'yes') {
						var updateData = new Array();
						for (var i = 0; i < modifyRec.length; i++) {
							if (modifyRec[i].get("stationName") == null
									|| modifyRec[i].get("stationName") == "") {
								alertMsg += "岗位不能为空</br>";
							}
							
							if (modifyRec[i].get("quantifyProportion") == null
									|| modifyRec[i].get("quantifyProportion") == "") {
								alertMsg += "比例不能为空</br>";
							}
							

							if (alertMsg != "") {
								Ext.Msg.alert("提示", alertMsg);
								return;
							}
							updateData.push(modifyRec[i].data);
						}
						
						Ext.Ajax.request({
									url : 'hr/saveStationQuantity.action',
									method : 'post',
									params : {
										isUpdate : Ext.util.JSON
												.encode(updateData)
										
									},
									success : function(form, options) {
										var obj = Ext.util.JSON
												.decode(form.responseText)
										Ext.MessageBox.alert('提示信息', '保存成功！');
										
										store.rejectChanges();
										store.reload();
								
									},
									failure : function(result, request) {
										Ext.MessageBox.alert('提示信息', '操作失败！')
									}
								})
					}else
					{
						                store.rejectChanges();
										store.reload();
						
					}
				})
		}else
		{
			Ext.Msg.alert("提示", "未做任何修改！");
			store.rejectChanges();
			store.reload();
		}
	}
	function deleteRecord() {
		var sm = stationGrid.getSelectionModel();
		
		var selected = sm.getSelections();
		var ids = [];
		
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.quantifyId) {
					ids.push(member.quantifyId);
				}
				
			}
			if (ids.length > 0) {
				Ext.Msg.confirm("删除", "是否确定删除所选记录？", function(buttonobj) {
							if (buttonobj == "yes") {
								Ext.lib.Ajax.request('POST',
										'hr/delStationQuantity.action', {
											success : function(action) {
												Ext.Msg.alert("提示", "删除成功！")
												store.reload();											
											},
											failure : function() {
												Ext.Msg.alert('错误',
														'删除时出现未知错误.');
											}
										}, 'ids=' + ids);
							} else {
								store.reload();
							}
						});
			}else
			{
			  store.reload();
			}
		}
	}


	
	

	
	
	
	var gridTbar = new Ext.Toolbar({
				items : [{
							id : 'add',
							text : "新增",
							iconCls : 'add',
							handler : addRecord
						}, '-', {
							id : 'delete',
							text : "删除",
							iconCls : 'delete',
							handler : deleteRecord
						}, '-', {
							id : 'save',
							text : "保存",
							iconCls : 'save',
							handler : saveRecord
						}, '-', {
							id : 'reflesh',
							text : "刷新",
							iconCls : 'reflesh',
							handler : query
						}]
			});

	function query()
	{
		store.rejectChanges();
		store.load();
	}
	//进入页面时进行一次查询
	query();

	// 页面的Grid主体
	var  stationGrid =  new Ext.grid.EditorGridPanel({
		store :store,
		layout : 'fit',
		columns : [sm,
		new Ext.grid.RowNumberer({
							header : '行号',
							width : 35
						}),// 选择框
				{
					header : '岗位',
					dataIndex : 'stationName',
					align : 'left',
					width : 220,
					editor : new Ext.form.TextField({
								allowBlank : false,
								id : 'station'
							})
				},{
					header : '比例',
					dataIndex : 'quantifyProportion',
					align : 'center',
					width : 180,
					editor : new Ext.form.NumberField({
							    allowBlank:false,
							    selectOnFocus :true,
							    allowDecimals :true,
							    decimalPrecision:5,
								id : 'proportion'
							})
					

				} ],

		sm : sm, // 选择框的选择
		tbar : gridTbar,
		clicksToEdit : 1
		
	});
	
	
	
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [{
							bodyStyle : "padding: 1,1,1,0",
							region : "center",
							border : false,
							frame : false,
							layout : 'fit',
							items : [stationGrid]
						}]
			});
			
			
			
			
})