Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	// 系统当前时间
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t;
		return s;
	}
	
	function getWorkerCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result.workerCode) {
					// 设定工作负责人为登录人
					modifyByH.setValue(result.workerCode);
					modifyName.setValue(result.workerName);
				}
			}
		});
	}
	
	var query = new Ext.form.TextField({
				id : 'argFuzzy',
				fieldLabel : "模糊查询",
				hideLabel : false,
				emptyText : '人员姓名..',
				name : 'query',
				width : 150,
				value : ''
			});
	
	// 数据
	var record = new Ext.data.Record.create([{
		name : 'empId'
	},{
		name : 'empName'
	},{
		name : 'empDuty'
	},{
		name : 'modifyby'
	},{
		name : 'modifyName'
	},{
		name : 'modifyDateString'
	}])
	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'security/findEmpInfoList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalCount : 'totalCount',
			root : 'list'
		},record)
	});
	
	
	



	var btnSure = new Ext.Button({
				text : '确定',
				id : 'btnSure',
				iconCls : 'sure',
				handler : function() {
					if(westgrid.getSelectionModel().hasSelection()){
						var rec = westgrid.getSelectionModel().getSelections();
						var reclen = rec.length;
						if(reclen > 1)
						{
							Ext.Msg.alert('提示','请选择其中一项！');
							return;
						}
						else 
						{
							var re = westgrid.getSelectionModel().getSelected();
							var obj = new Object();
							obj.empId = re.get('empId');
							obj.empName = re.get('empName');
							obj.empDuty = re.get('empDuty');
							window.returnValue = obj;
							window.close();
						}
					}
					else
					{
						Ext.Msg.alert('提示','请先选择人员！');
						return;
					}					
				}
			});

	
	var btnCancel = new Ext.Button({
				id : 'cancer',
				text : '取消',
				iconCls : 'update',
				handler : function() {
					var obj = new Object();
					obj.empId = null;
					obj.empName = null;
					obj.empDuty = null;
					window.returnValue = obj;
					window.close();
				}
			});



	var westsm = new Ext.grid.CheckboxSelectionModel();

	function fuzzyQuery() {
				store.baseParams = {
					name : query.getValue()
				}
				store.load({
							params : {
								start : 0,
								limit : 18
							}
						})
			};
	var querybtn = new Ext.Button({
				iconCls : 'query',
				text : '查询',
				handler : function() {
					fuzzyQuery();
				}
			})
	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
				ds : store,
				columns : [westsm, new Ext.grid.RowNumberer(), {
							header : "id",
							hidden : true,
							dataIndex : 'empId'
						}, {
							header : "姓名",
							sortable : true,
							dataIndex : 'empName'
						},{
							header : '职责',
							sortabel : true,
							dataIndex : 'empDuty'
						}],
				tbar : [query, querybtn, btnSure, {
							xtype : "tbseparator"
						}, btnCancel],
				sm : westsm,
				viewConfig : {
					forceFit : true
				},
				frame : true,
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : store,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						}),
				border : true,
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});

	// westgrid 的事件
	westgrid.on('rowdblclick', function() {
				Ext.get("btnSure").dom.click();
			})

	fuzzyQuery();
	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							title : "",
							region : 'center',
							layout : 'fit',
							border : false,
							margins : '1 0 1 1',
							split : true,
							collapsible : true,
							items : [westgrid]

						}]
			});
});