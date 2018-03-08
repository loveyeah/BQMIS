Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var isNew;
var record = Ext.data.Record.create([{
	
		name : 'empId',
		mapping : 0
	}, {
		name : 'empCode',
		mapping : 1
	}, {
		name : 'chsName',
		mapping : 2
	}, {
		name : 'deptId',
		mapping : 3
	}, {
		name : 'deptName',
		mapping : 4
	}]);

	var ds = new Ext.data.JsonStore({
		url : 'hr/getChairmanList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : record
	});

	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	});
   function workingChargeSelect() {
   	if(isNew)
   	{
		var url = "../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
   	
		var args = {
			selectModel : 'single',
			notIn : "'999999'",
			rootNode : {
				id : '-1',
				text : '灞桥电厂'
			}
		}
		var emp = window
				.showModalDialog(
						url,
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

		if (typeof(emp) != "undefined") {
			var record = unionGrid.selModel.getSelected()
			record.set("chsName", emp.workerName);
			record.set("empId",emp.empId);
			record.set("deptName",emp.deptName);
			unionGrid.getView().refresh();
			
		}
	
   }else
   {
   }
   }
	var cm = new Ext.grid.ColumnModel([sm,
			new Ext.grid.RowNumberer({
				header : '序号',
				width : 35,
				align : 'center'
			}), {
				header : 'empId',
				dataIndex : '',
				align : 'center',
				hidden : true
			}, {
				header : '姓名',
				dataIndex : 'chsName',
				width : 150,
				align : 'center',
				editor : new Ext.form.TriggerField({
					width : 320,
					readOnly : true,
					allowBlank : false,
					onTriggerClick : workingChargeSelect,
					listeners : {
						render : function(f) {
							f.el.on('keyup', function(e) {
								
							});
						}
					}
				}),
				renderer : function(value, cellmeta, record, rowIndex, columnIndex,store) {
							
							if (store.getAt(store.getCount() - 1).get('flag') == 'new') {
								isNew=true;
						     }else
						     {
						     	isNew=false;
						     }
						     return value;
					}
			}, {
				header : '部门',
				dataIndex : 'deptName',
				width:250,
				align : 'center'
				
			}]);
	cm.defaultSortable = true;
	var btnQuery= new Ext.Toolbar.Button({
		id : 'btnquery',
		text : '查询',
		iconCls:"query",
		handler : query
		
	})
	var btnAdd = new Ext.Toolbar.Button({
		id : 'btnadd',
		text : '增加',
		iconCls : 'add',
		handler : addRecord
		
	})
	var btnSave = new Ext.Toolbar.Button({
		id : 'btnsave',
		text : '保存',
		iconCls : 'save',
		handler : saveRecord
		
	})
	var btnDel = new Ext.Toolbar.Button({
		id : 'btnsave',
		text : '删除',
		iconCls : 'delete',
		handler : delRecord
		
	})
	var btnreflesh= new Ext.Toolbar.Button({
		id : 'btnreflesh',
		text : '刷新',
		iconCls:"reflesh",
		handler : query
		
	})
	function addRecord() {
		var count = ds.getCount();
		var currentIndex = count;
		var o = new record({
			'empId' : '',
			'empCode' : '',
			'chsName' : '',
			'deptId' : '',
			'deptName' : '',
			'flag':'new'
		
			
		});
	
		ds.insert(currentIndex, o);
		unionGrid.stopEditing();
		sm.selectRow(currentIndex);
		unionGrid.startEditing(currentIndex, 1);
		
		
	}
	function saveRecord()
	{
		unionGrid.stopEditing();
	
				var modifyRec = unionGrid.getStore().getModifiedRecords();
				if (modifyRec.length > 0) {
					Ext.Msg.confirm('提示', '确定要保存修改数据吗?', function(button) {
						if (button == 'yes') {
							var modifyIds= new Array();
							for (var i = 0; i < modifyRec.length; i++) {
								modifyIds.push(modifyRec[i].data.empId);
							}
							Ext.Ajax.request({
								url : 'hr/saveChairmanList.action',
								method : 'post',
								params : {
									modifyIds : modifyIds.join(',')
								},
								success : function(form, options) {
									var obj = Ext.util.JSON
											.decode(form.responseText)
									Ext.MessageBox.alert('提示', '保存成功！')
									  var i=modifyRec.length;
									  	var count = ds.getCount();
									  var currentIndex = count+i;
									
								
									


									ds.rejectChanges();
									ds.reload();
									
								},
								failure : function(result, request) {
									Ext.MessageBox.alert('提示信息', '操作失败！')
								}
							})
						}
					})
				} else {
					Ext.MessageBox.alert('提示信息', '没有做任何修改！')
				}
	}
	function delRecord()
	{
		var sm = unionGrid.getSelectionModel();
				var selected = sm.getSelections();
				var ids = [];
				if (selected.length == 0) {
					Ext.Msg.alert("提示", "请选择要删除的记录！");
				} else {
					for (var i = 0; i < selected.length; i += 1) {
						var member = selected[i].data;
						if (member.empId) {
							ids.push(member.empId);
						} else {
							
						}
					}
					Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(buttonobj) {
						if (buttonobj == "yes") {
							Ext.lib.Ajax.request('POST',
									'hr/delChairmanList.action', {
										success : function(action) {
											Ext.Msg.alert("提示", "删除成功！")
											ds.reload();
										},
										failure : function() {
											Ext.Msg.alert('错误', '删除时出现未知错误.');
										}
									}, 'ids=' + ids);
						}
					});
				}

	}
	
	
	
	var empName = new Ext.form.TextField({
		id : 'name',
		fieldLabel : '姓名',
		name : 'Name',
		anchor : "85%"
		

	});
	var tbar = new Ext.Toolbar({
		items : ['姓名:', empName, '-',btnQuery,'-', btnAdd,'-', btnSave, '-',btnDel,btnreflesh]
	});

	
		function query()
	{
		ds.baseParams = {
			name :empName.getValue()
			
		}
		ds.load();
		
	}
	query();
	var unionGrid = new Ext.grid.EditorGridPanel({
	    renderTo : 'mygrid',
		ds : ds,
		cm : cm,
		sm : sm,
		clicksToEdit : 1,
		split : true,
		autoHeight : true,
		autoScroll : true,
		tbar : tbar,
		
		/*bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : ds,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		}),*/
		border : false
	});
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "fit",
				border : false,
				frame : false,
				items : [unionGrid]
			});
	
});
