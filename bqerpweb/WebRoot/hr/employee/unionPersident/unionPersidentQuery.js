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
	
	var btnreflesh= new Ext.Toolbar.Button({
		id : 'btnreflesh',
		text : '刷新',
		iconCls:"reflesh",
		handler : query
		
	})
	
	
	var empName = new Ext.form.TextField({
		id : 'name',
		fieldLabel : '姓名',
		name : 'Name',
		anchor : "85%"
		

	});
	var tbar = new Ext.Toolbar({
		items : ['姓名:', empName, '-',btnQuery,btnreflesh]
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
