Ext.onReady(function() {
	var speciality = getParameter("sepeciality");
	
	function getYear() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10);
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
				width:80,
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
	var sm = new Ext.grid.CheckboxSelectionModel({
	  singleSelect : true
	});
	// grid列表数据源

	var Record = new Ext.data.Record.create([sm, {

				name : 'tasklistId',
				mapping:0
			},{
				name : 'tasklistName',
				mapping:2
			}]);
	
	var gridTbar = new Ext.Toolbar({
				items : ['年度' ,year,{
							id : 'querybtn',
							text : "查询",
							iconCls : 'query',
							tabIndex:1,
							handler : queryRepairTask
						}, {
							id : 'confirmBtu',
							text : '确定',
							iconCls : 'confirm',
							handler : confirmFun
								}]
			});
	
	var store = new Ext.data.JsonStore({
				url : 'manageplan/getRepairTaskSelectList.action',
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
							width : 50
						}),// 选择框
			  	{
					header : '检修任务单名称',
					dataIndex : 'tasklistName',
					align : 'left',
					width : 150
				}],

		sm : sm, // 选择框的选择
		tbar : gridTbar,
		clicksToEdit : 1,
		viewConfig : {
			forceFit : true
		}
	});
  repairTaskgrid.on("dblclick", confirmFun);	
	store.on("beforeload", function() {
				Ext.apply(this.baseParams, {
							year : year.getValue(),
							speciality : speciality
						});
			});
   store.load();
	
	 function queryRepairTask()
  {
  	store.reload();
  }
	
  function confirmFun()
	{
		if (repairTaskgrid.selModel.hasSelection()) {		
			var records = repairTaskgrid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项！");
			} else {
				var record = repairTaskgrid.getSelectionModel().getSelected();
				var repair=new Object();
				repair.id=record.get("tasklistId");  
				repair.name=record.get("tasklistName");
				window.returnValue = repair;
				window.close();
			}
		} else {
			Ext.Msg.alert("提示", "请先选择行!");
		}
	}
	
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