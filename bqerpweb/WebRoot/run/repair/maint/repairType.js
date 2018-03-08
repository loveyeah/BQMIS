Ext.onReady(function() {
	var  isAdd =0;
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		return s;
	}
	
	var sm = new Ext.grid.CheckboxSelectionModel();
	// grid列表数据源

	var Record = new Ext.data.Record.create([sm, {

				name : 'repairTypeId',
				mapping:0
			}, {
				name : 'repairTypeName',
				mapping:1
			}, {
				name : 'entryBy',
				mapping:2
			}, {
				name : 'entryByName',
				mapping:3
			}, {
				name : 'entryDate',
				mapping:4
			}, {
				name : 'memo',
				mapping:5
			}, {
				name : 'isUse',
				mapping:6
			}]);
	// 填写时间

	var fillDate = new Ext.form.TextField({
				readOnly : true,
				name : 'editDate'
			});
			fillDate.setValue(getDate());		
	// 填写人
	var fillByName = new Ext.form.TextField({
		        fieldLabel : '填写人',
				readOnly : true,
				name : 'editByName'

			});
		
	var fillByCode = new Ext.form.Hidden({
				name : 'editBy'
			});
	
	// 从session取登录人编码姓名部门相关信息
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号
							var workerCode = result.workerCode;
							var workerName = result.workerName;
					         fillByName.setValue(workerName);
					         fillByCode.setValue(workerCode);
					         
							

						}
					}
				});
	}
	getWorkCode();
	
	var gridTbar = new Ext.Toolbar({
				items : [{
							id : 'add',
							text : "新增",
							iconCls : 'add',
							handler : addRepairType
						}, '-', {
							id : 'save',
							text : "保存",
							iconCls : 'save',
							handler : saveRepairType
						}, '-', {
							id : 'cancer',
							text : "取消",
							iconCls : 'cancer',
							handler : cancerChange
						}]
			});

	function cancerChange() {

		store.reload();
		store.rejectChanges();

	}

//		store.load();


	
	function addRepairType() {
		
		var count = store.getCount();
		var currentIndex = count;
		var o = new Record({
					'repairTypeName' : '',
					'entryByName' : fillByName.getValue(),								
					'entryDate' : fillDate.getValue(),					
					'memo' : '',
					'isUse' :'Y'	

				});
		repairTypegrid.stopEditing();
		store.insert(currentIndex, o);
		sm.selectRow(currentIndex);
		repairTypegrid.startEditing(currentIndex, 1);
		isAdd++;

	}

	function saveRepairType() {
		var alertMsg = "";
		repairTypegrid.stopEditing();
		var modifyRec = repairTypegrid.getStore().getModifiedRecords();
		 if(modifyRec.length>0)
		 {
		 	
		Ext.Msg.confirm('提示', '确定要保存修改数据吗?', function(button) {
					if (button == 'yes') {
						var updateData = new Array();
						for (var i = 0; i < modifyRec.length; i++) {
							if (modifyRec[i].get("repairTypeName") == null
									|| modifyRec[i].get("repairTypeName") == "") {
								alertMsg += "修理类别不能为空</br>";
							}
							if (alertMsg != "") {
								Ext.Msg.alert("提示", alertMsg);
								return;
							}
							updateData.push(modifyRec[i].data);
						}
						Ext.Ajax.request({
									url : 'manageplan/saveRepairType.action',
									method : 'post',
									params : {
										isUpdate : Ext.util.JSON
												.encode(updateData)
									},
									success : function(form, options) {
									var obj = Ext.util.JSON
											.decode(form.responseText)
									if (obj && obj.msg) {
										Ext.Msg.alert('提示', obj.msg);
										if (obj.msg.indexOf('已经存在') != -1)
											return;
									}
									store.rejectChanges();
									store.reload();
								},
									failure : function(result, request) {
										Ext.MessageBox.alert('提示信息', '操作失败！')
									}
								})
					}
				})
	}
	else  if(isAdd>0) { Ext.MessageBox.alert('提示', '请填写修理类别！')}
	else if (isAdd==0){
		Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		store.reload();
	}
	}
	
	var planDetailId = new Ext.form.Hidden({
				name : 'planDetailId'
			});

	var store = new Ext.data.JsonStore({
				url : 'manageplan/getRepairType.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : Record
			});


	// 页面的Grid主体
	var repairTypegrid = new Ext.grid.EditorGridPanel({
		store : store,
		layout : 'fit',
		columns : [sm, new Ext.grid.RowNumberer({
							header : '行号',
							width : 35
						}),// 选择框
			  	{
					header : '修理类别',
					dataIndex : 'repairTypeName',
					align : 'center',
					width : 150,
					editor : new Ext.form.TextField({
								allowBlank : false,
								id : 'repairTypeName'
							})
				},{
					header : '备注',
					dataIndex : 'memo',
					align : 'center',
					width : 180,
					editor : new Ext.form.TextField({
								allowBlank : true,
								id : 'memo'
							})

				},{
					header : "启用",
					width : 100,
					align : 'center',
					sortable : true,
					dataIndex : 'isUse',
					renderer : function(v) {
						if (v =='Y') {
							return '是';
						} else {
							return '否';
						}
					},
					editor : new Ext.form.ComboBox({
				readOnly : true,
				name : 'isUse',
				hiddenName : 'isUse',
				mode : 'local',
				width : 70,
				value : 'Y',
				fieldLabel : '是否使用',
				triggerAction : 'all',
				listeners : {
					"select" : function() {
//						store.reload();

					}
				},
				store : new Ext.data.SimpleStore({
							fields : ['name', 'value'],
							data : [['是', 'Y'], ['否', 'N']]
						}),
				valueField : 'value',
				displayField : 'name',
				anchor : "15%",
				listeners : {
					"select" : function() {
						
					}
				}
			})
				}, {
					header : '填写人',
					dataIndex : 'entryByName',
					align : 'center',
					width : 100	

				}, {
					header : '填写时间',
					dataIndex : 'entryDate',
					align : 'center',
					width : 100
				}],

		sm : sm, // 选择框的选择
		tbar : gridTbar,
		clicksToEdit : 1,
		viewConfig : {
			forceFit : true
		}
	});
	store.load();
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
							items : [repairTypegrid]
						}]
			});
})