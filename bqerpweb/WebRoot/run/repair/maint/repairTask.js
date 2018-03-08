Ext.onReady(function() {
	var ishas=0;
	function getYear() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) /*+ "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";*/
		return s;
	}
	function getTime() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		return s;
	}


	var year = new Ext.form.TextField({
				style : 'cursor:pointer',
				name : 'year',
				fieldLabel : '年度',
				readOnly : true,
				anchor : "80%",
				value : getYear(),
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y',
									alwaysUseStartDate : false,
									dateFmt : 'yyyy',
									isShowClear : false,		
									onpicked : function(v) {
										store.reload();
										this.blur();
									}
								});
					}
				}
			});
	var sm = new Ext.grid.CheckboxSelectionModel();
	// grid列表数据源

	var Record = new Ext.data.Record.create([sm, {

				name : 'tasklistId',
				mapping:0
			}, {
				name : 'tasklistYear',
				mapping:1
			}, {
				name : 'tasklistName',
				mapping:2
			}, {
				name : 'entryBy',
				mapping:3
			}, {
				name : 'entryByName',
				mapping:4
			}, {
				name : 'entryDate',
				mapping:5
			}]);
	// 填写时间

	var fillDate = new Ext.form.TextField({
				readOnly : true,
				name : 'editDate'
			});
			fillDate.setValue(getTime());		
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
				items : ['年度' ,year,{
							id : 'querybtn',
							text : "查询",
							iconCls : 'query',
							handler : queryRepairTask
						},'-',{
							id : 'addbtn',
							text : "新增",
							iconCls : 'add',
							handler : addRepairTask
						}, '-', {
							id : 'savebtn',
							text : "保存",
							iconCls : 'save',
							handler : saveRepairTask
						}, '-',{
							id : 'delbtn',
							text : "删除",
							iconCls : 'delete',
							handler : delRepairTask
						},'-', {
							id : 'cancer',
							text : "取消",
							iconCls : 'cancer',
							handler : cancerChange
						}]
			});
  function queryRepairTask()
  {
  	store.reload();
  }
	function cancerChange() {

		store.reload();
		store.rejectChanges();

	}

//		store.load();


	function addRepairTask() {
		var count = store.getCount();
		var currentIndex = count;
		var o = new Record({
					'tasklistName' : '',
					'entryByName' : fillByName.getValue(),								
					'entryDate' : fillDate.getValue()			
			
				});
		repairTaskgrid.stopEditing();
		store.insert(currentIndex, o);
		sm.selectRow(currentIndex);
		repairTaskgrid.startEditing(currentIndex, 1);
		ishas++;

	}
   function delRepairTask() {
		var sm = repairTaskgrid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		var names = [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.tasklistId) {
					ids.push(member.tasklistId);
				}
				store.remove(selected[i]);
			}

			if (ids.length > 0) {
				Ext.Msg.confirm("删除", "是否确定删除所选记录？", function(buttonobj) {
							if (buttonobj == "yes") {
								Ext.lib.Ajax.request('POST',
										'manageplan/delRepairTask.action', {
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
			}
		}
	}
	function saveRepairTask() {
		var alertMsg = "";
		repairTaskgrid.stopEditing();
		var modifyRec = repairTaskgrid.getStore().getModifiedRecords();
		 if(modifyRec.length>0)
		 {
		Ext.Msg.confirm('提示', '确定要保存修改数据吗?', function(button) {
					if (button == 'yes') {
						var updateData = new Array();
						for (var i = 0; i < modifyRec.length; i++) {
							if (modifyRec[i].get("tasklistName") == null
									|| modifyRec[i].get("repairTypeName") == "") {
								alertMsg += "任务单名称不能为空</br>";
							}
							if (alertMsg != "") {
								Ext.Msg.alert("提示", alertMsg);
								return;
							}
							updateData.push(modifyRec[i].data);
						}
						Ext.Ajax.request({
									url : 'manageplan/saveRepairTask.action',
									method : 'post',
									params : {
										year:year.getValue(),
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
									/*success : function(form, options) {
										var obj = Ext.util.JSON
												.decode(form.responseText)
										Ext.MessageBox.alert('提示信息', '保存成功！')
										store.rejectChanges();
										store.reload();
									},*/
									failure : function(result, request) {
										Ext.MessageBox.alert('提示信息', '操作失败！')
									}
								})
					}
				})
	}
	else  if(ishas>0) {Ext.MessageBox.alert('提示', '请填写任务单名称！')}
	else if(ishas==0){
		Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		store.reload();
	}
	}
	
	var store = new Ext.data.JsonStore({
				url : 'manageplan/getRepairTask.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : Record
			});


	// 页面的Grid主体
	var repairTaskgrid = new Ext.grid.EditorGridPanel({
		store : store,
		layout : 'fit',
		columns : [sm, new Ext.grid.RowNumberer({
							header : '行号',
							width : 35
						}),// 选择框
			  	{
					header : '检修任务单名称',
					dataIndex : 'tasklistName',
					align : 'left',
					width : 120,
					editor : new Ext.form.TextField({
								allowBlank : false,
								id : 'tasklistName'
							})
				}, {
					header : '填写人',
					dataIndex : 'entryByName',
					align : 'left',
					width : 80	

				}, {
					header : '填写时间',
					dataIndex : 'entryDate',
					align : 'left',
					width : 80
				}],

		sm : sm, // 选择框的选择
		tbar : gridTbar,
		clicksToEdit : 1,
		viewConfig : {
			forceFit : true
		}
	});
	
	store.on("beforeload", function() {
				Ext.apply(this.baseParams, {
							year : year.getValue()

						});

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
							items : [repairTaskgrid]
						}]
			});
			
})