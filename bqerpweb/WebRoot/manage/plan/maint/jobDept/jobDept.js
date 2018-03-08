Ext.onReady(function() {
	
	var sm = new Ext.grid.CheckboxSelectionModel();
	// grid列表数据源
	var Record = new Ext.data.Record.create([sm, {

				name : 'deptCode',
				mapping:0
			}, {
				name : 'deptName',
				mapping:1
			}, {
				name : 'id',
				mapping:2
			}, {
				name : 'orderBy',
				mapping:3
			}]);
	
	var deptName = new Ext.form.TextField({
		id : "deptname",
		name : "deptname"
	});
		
	function queryDept() {
		store.rejectChanges();
		store.reload();
	}

	function saveDept() {
		deptgrid.stopEditing();
		var modifyRec = deptgrid.getStore().getModifiedRecords();
		Ext.Msg.confirm('提示', '确定要保存修改数据吗?', function(button) {
					if (button == 'yes') {
						var updateData = new Array();
						for (var i = 0; i < modifyRec.length; i++) {
							if((modifyRec[i].data.id==null)&&(modifyRec[i].data.orderBy==null))
							{
							}else
							{
							updateData.push(modifyRec[i].data);	
							}
						
						}
						if(updateData.length==0)
						{
							Ext.MessageBox.alert('提示信息', '未做修改！');
							store.rejectChanges();
							store.reload();
							return;
							
						}
						Ext.Ajax.request({
									url : 'manageplan/saveJobDept.action',
									method : 'post',
									params : {
										isUpdate : Ext.util.JSON
												.encode(updateData)
									},
									success : function(form, options) {
										var obj = Ext.util.JSON
												.decode(form.responseText)
										Ext.MessageBox.alert('提示信息', '保存成功！')
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
	}
	

	var store = new Ext.data.JsonStore({
				url : 'manageplan/getLevelOneDept.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : Record
			});

	store.on("beforeload", function() {
				Ext.apply(this.baseParams, {
							deptName : deptName.getValue()

						});

			});
			
   store.load();
	var gridTbar = new Ext.Toolbar({
				items : ['根据部门名称模糊查询' ,deptName, {
							id : 'query',
							text : "查询",
							iconCls : 'query',
							handler : queryDept
						}, '-', {
							id : 'save',
							text : "保存",
							iconCls : 'save',
							handler : saveDept
						},'-', {
							id : 'reflesh',
							text : "刷新",
							iconCls : 'reflesh',
							handler : reRecord
						}]
			});

	  function  reRecord()
	  {
	  	store.rejectChanges();
	  	store.reload();
	  }
	
	// 页面的Grid主体
	var deptgrid = new Ext.grid.EditorGridPanel({
		store : store,
		layout : 'fit',
//		autoScroll:true,
//		autoHeight : true,
		columns : [sm, new Ext.grid.RowNumberer({
							header : '行号',
							width : 35
						}),// 选择框
				{
					header : '部门名称',
					dataIndex : 'deptName',
					align : 'left',
					width :300
				}, {
					header : "排序号",
					width : 250,
					align : 'left',
					sortable : true,
					dataIndex : 'orderBy',
					editor :  new Ext.form.NumberField({
						     allowDecimals:false
								
							})
				}],

		sm : sm, // 选择框的选择
		tbar : gridTbar,
		clicksToEdit : 1/*,
		viewConfig : {
			forceFit : true
		}*/
	});
	
	var viewport = new Ext.Viewport({
		enableTabScroll:true,
		region : "center",
		layout : 'fit',
		autoWidth:true,
		autoHeight:true,
		fitToFrame : true,
		items : [deptgrid]
	});

})