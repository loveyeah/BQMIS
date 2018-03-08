//var tabpanel = parent.Ext.getCmp('maintab');
Ext.BLANK_IMAGE_URL = 'ext/resources/images/default/s.gif';
Ext.onReady(function() {
	
	var gridCount=0; 
	var startTime="";
	 var dataCount;
	var sm = new Ext.grid.CheckboxSelectionModel({
	singleSelect : false
	});
	// grid列表数据源
	

	var Record = new Ext.data.Record.create([sm, {

				name : 'unionPerId',
				mapping:0
			}, {
				name : 'unionPerStandard',
				mapping:1
			}, {
				name : 'effectStartTime',
				mapping:2
			}, {
				name : 'effectEndTime',
				mapping:3
			}, {
				name : 'memo',
				mapping:4
			}]);
	
var store = new Ext.data.JsonStore({
				url : 'hr/findUnionPerStandard.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : Record
			});
	
	

	
		store.on("load",function(){
		
			gridCount=store.getCount();
		});
		
	

	function getMaxtime()
	{
		Ext.Ajax.request({
									url : 'hr/getMaxEndtime.action',
									method : 'post',
									params : {
									},
									success : function(form, options) {
										var obj = form.responseText;
										
										if(obj!=null)
										{
											
											startTime=obj.toString();
										}
										else 
										{
											startTime="";
										}
									},
									failure : function(result, request) {
										Ext.MessageBox.alert('提示信息', '操作失败！')
									}
								})
	}
//	getMaxtime();
 var rowCount=0;
 var nextStartTime="";
  store.on('load', function() {
	dataCount=store.getCount();
	})
	function addRecord() {
		
		gridCount= standardgrid.getStore().getTotalCount()+1;
		rowCount++;
		if(rowCount>1)
		{
			Ext.MessageBox.alert('提示信息', '一次只能增加一条记录！')
			store.reload();
			rowCount=0;
			return;
		}else
		{
//		getMaxtime();
		var count = store.getCount();
		dataCount=count+1;
		
		if(count>0)
		{
		 nextStartTime = store.getAt(count - 1).get("effectEndTime");
		}
		var currentIndex = count;	
		var o = new Record({
					'unionPerId' : '',
					'unionPerStandard' : '',									
					'effectStartTime' : nextStartTime,									
					'effectEndTime' : '',
					'beginTime' : '',
					'endTime' : '',
					'memo' : ''
					

				});
		standardgrid.stopEditing();
		store.insert(currentIndex, o);
		sm.selectRow(currentIndex);
		standardgrid.startEditing(currentIndex, 1);
		}

	}

	function saveRecord() {
		var alertMsg = "";
		standardgrid.stopEditing();
		var modifyRec = standardgrid.getStore().getModifiedRecords();
		if(nextStartTime!=""&&modifyRec.length==0)
		{
				Ext.MessageBox.alert('提示信息', '请输入数据！');
				return;
				
		}
		if(modifyRec.length>0)
		{
		Ext.Msg.confirm('提示', '确定要保存修改数据吗?', function(button) {
					if (button == 'yes') {
						var updateData = new Array();
						for (var i = 0; i < modifyRec.length; i++) {
							if (modifyRec[i].get("unionPerStandard") == null
									|| modifyRec[i].get("unionPerStandard") == "") {
								alertMsg += "工会主席标准不能为空</br>";
							}
							
							if (modifyRec[i].get("effectEndTime") == null
									|| modifyRec[i].get("effectEndTime") == "") {
								alertMsg += "生效结束时间不能为空</br>";
							}
							

							if (alertMsg != "") {
								Ext.Msg.alert("提示", alertMsg);
								return;
							}
							updateData.push(modifyRec[i].data);
						}
						Ext.Ajax.request({
									url : 'hr/saveUnionPerStandard.action',
									method : 'post',
									params : {
										isUpdate : Ext.util.JSON
												.encode(updateData)
										
									},
									success : function(form, options) {
										var obj = Ext.util.JSON
												.decode(form.responseText)
										Ext.MessageBox.alert('提示信息', '保存成功！');
										rowCount=0;
										nextStartTime="";
										store.rejectChanges();
										store.reload();
										dataCount=store.getCount();
									
										
									
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
			rowCount=0;
			store.reload();
		}
	}
	function deleteRecord() {
		var sm = standardgrid.getSelectionModel();
		
		var selected = sm.getSelections();
		var ids = [];
		
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				/*if (member.unionPerId) {
					ids.push(member.unionPerId);
				}*/
				if(member.unionPerId&&(member.unionPerId!=standardgrid.getStore().getAt(gridCount-1).get("unionPerId")))
				{
					Ext.Msg.alert("提示", "只能删除最后一条记录！");
					store.rejectChanges();
					store.reload();
					return;
				}else
				{
					ids.push(member.unionPerId);
				}
				
			}
			if(ids==""||ids==null)
			{
				store.rejectChanges();
				store.reload();
				nextStartTime="";
				rowCount=0;
				
				return;
			}
			

			if (ids.length > 0) {
				Ext.Msg.confirm("删除", "是否确定删除所选记录？", function(buttonobj) {
							if (buttonobj == "yes") {
								Ext.lib.Ajax.request('POST',
										'hr/delUnionPerStandard.action', {
											success : function(action) {
												Ext.Msg.alert("提示", "删除成功！")
												store.reload();
//												getMaxtime();
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
			}
		}
	}


	
	

	function checkTime1() {
		var startdate1 = this.value;
		startdate2 = startdate1.substring(0, 10);

		var enddate1 = standardgrid.getSelectionModel().getSelected()
				.get("effectEndTime");
		if (enddate1 != null && enddate1 != "") {
			enddate2 = enddate1.substring(0, 10);

			if (startdate2 != "") {
				if (startdate2 > enddate2 && enddate2 != "") {
					Ext.Msg.alert("提示", "开始日期必须早于结束日期");
					return;
				}
			}
		}

		standardgrid.getSelectionModel().getSelected().set("effectStartTime",
				startdate2);
	}
	function checkTime2() {
		var endtime1 = this.value;
		var endtime2 = endtime1.substring(0, 10);

		var beginTime1 = standardgrid.getSelectionModel().getSelected()
				.get("effectStartTime");
		if (beginTime1 != null && beginTime1 != "") {
			beginTime2 = beginTime1.substring(0, 10);

			if (endtime2 != "" && beginTime2 != "") {
				if (endtime2 < beginTime2 && endtime2 != "") {
					Ext.Msg.alert("提示", "结束日期必须晚于开始日期");
					return;
				}
			}
		}
		standardgrid.getSelectionModel().getSelected().set("effectEndTime", endtime2);
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
		rowCount=0;
		store.load();
		dataCount=store.getCount();
		nextStartTime="";
	}
	//进入页面时进行一次查询
	query();

	// 页面的Grid主体
	var standardgrid =  new Ext.grid.EditorGridPanel({
		store :store,
		layout : 'fit',
		columns : [sm,
		new Ext.grid.RowNumberer({
							header : '行号',
							width : 35
						}),// 选择框
				{
					header : '工会主席标准',
					dataIndex : 'unionPerStandard',
					align : 'left',
					width : 220,
					editor : new Ext.form.NumberField({
							    allowBlank:false,
								id : 'unionPerStandard'
							})
				}, {
					header : '生效开始时间',
					dataIndex : 'effectStartTime',
					width : 120,
					align : 'center',
					renderer : function(v) {
						if (v != null && v.length > 10) {
							return v.substr(0, 10);
						} else {
							return v;
						}
					},
					editor : new Ext.form.TextField({
						 
								allowBlank : false,
								style : 'cursor:pointer',
								readOnly : true,
								listeners : {
									
									focus : function() {
										if(dataCount==0||dataCount==1)
										{
											
										WdatePicker({
													// 时间格式
													startDate : '%y-%M-%d',
													dateFmt : 'yyyy-MM-dd',
													alwaysUseStartDate : false,
													onpicked : checkTime1
												});
										}

									}
								}
							})

				}, {
					header : '生效结束时间',
					dataIndex : 'effectEndTime',
					readOnly:true,
					width : 120,
					renderer : function(v) {
						if (v != null && v.length > 10) {
							return v.substr(0, 10);
						} else {
							return v;
						}
					},
					align : 'center',
					editor : new Ext.form.TextField({
								allowBlank : false,
								style : 'cursor:pointer',
								readOnly : true,
								listeners : {
									focus : function() {
										WdatePicker({
													// 时间格式
													startDate : '%y-%M-%d ',
													dateFmt : 'yyyy-MM-dd',
													alwaysUseStartDate : false,
													onpicked : checkTime2
												});

									}
								}
							})

				}, {
					header : '备注',
					dataIndex : 'memo',
					align : 'center',
					width : 180,
					editor : new Ext.form.TextField({
								allowBlank : true,
								id : ''
							})

				}],

		sm : sm, // 选择框的选择
		tbar : gridTbar,
		clicksToEdit : 1/*,
		viewConfig : {
			forceFit : true
		}*/
	});
	
	standardgrid.on('beforeedit', function(e) {
		//alert(e.record.get('unionPerId'));
	
		//alert(standardgrid.getStore().getAt(standardgrid.getStore().getTotalCount()-1).get("unionPerId"));
		if(e.record.get('unionPerId')!=standardgrid.getStore().getAt(gridCount-1).get("unionPerId"))
		return false;
		
		
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
							items : [standardgrid]
						}]
			});
			
			
			
			
})