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
		s += (t > 9 ? "" : "0") + t + " ";

		return s;
	}
	
	
	// 从session取登录人编码姓名部门相关信息
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号，赋给全局变量
							fillByCode.setValue(result.workerCode);
							entryBy.setValue(result.workerCode);

						}
					}
				});
	}

	

	

	
  //年度关键字
	
	var year = new Ext.form.TextField({
				fieldLabel : "年度",
				readOnly : true,
				anchor : '90%',
				//name : 'model.year',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : new Date().getFullYear()
											.toString(),
									alwaysUseStartDate : true,
									dateFmt : "yyyy"

								});
						this.blur();
					}
				},
				value : new Date().getFullYear()
			});
	
	var con_name = new Ext.form.TextField({
			fieldLabel:"合同名称",
			inputType:'text',
			name:'contract_name'
	})



	
	// 查询按钮
	var westbtnref = new Ext.Button({
				text : '查询',
				tabIndex:1,
				iconCls : 'query',
				handler : function() {
					queryRecord();
				}
			});

	//确定按钮
	var canfirm = new Ext.Button({
				text : '确定',
				iconCls : '',
				handler : returnValue
			});
	
			
	
	var westsm = new Ext.grid.CheckboxSelectionModel({singleSelect:true});
	// 左边列表中的数据
	
			
var datalist = new Ext.data.Record.create([
			{			
				name : 'con_id',
				mapping:0
			}, {
				name : 'conttrees_no',
				mapping:1
			},{
				name:'contract_name',
				mapping:2
			},{
				name : 'client_name',
				mapping:3
			}, {
				name : 'act_amount',
				mapping:4
			}, {
				name : 'con_year',
				mapping:5
			}, {
				name : 'client_id',
				mapping:6
			}]);

	var westgrids = new Ext.data.JsonStore({
				url : 'managecontract/queryContract.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});

	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
				ds : westgrids,
				listeners : {
					'rowdblclick' : function() {
						returnValue();
					}
				},
				columns : [westsm, new Ext.grid.RowNumberer(), {
							header : "合同编号",
							width : 75,
							//align : "center",
							sortable : true,
							dataIndex : 'conttrees_no'
						}, {
							header : "合同名称",
							width : 75,
							//align : "center",
							sortable : false,
							dataIndex : 'contract_name',
							renderer:function(v){
								return v.substring(0,10);
							}
							
						},{
							header : "承包商",
							width : 75,
							sortable : true,
							dataIndex : 'client_name'
		                  },{
							header : "总金额",
							width : 75,
							sortable : true,
							dataIndex : 'act_amount'
		                  },{
							header : "合同年份",
							width : 75,
							sortable : true,
							dataIndex : 'con_year'
							}],
				viewConfig : {
			                 forceFit : true
		           },
				tbar : ['合同年份：',year,'合同名称：',con_name,westbtnref, {
							xtype : "tbseparator"
						},canfirm],
				sm : westsm,
				frame : true,
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : westgrids,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						}),
				border : true,
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});

			
	function returnValue(){
		
			var record = westgrid.getSelectionModel().getSelected();
			if (typeof(record) != "object") {
				Ext.Msg.alert("提示", "请选择合同!");
				return false;
			}
			var ro = record.data;
			window.returnValue = ro;
			window.close();
		
	}
	// westgrid 的事件
	function queryRecord()
	{
		
		westgrids.baseParams = {
				contract_name:con_name.getValue(),
				year:year.getValue()
		}
		
		westgrids.load({
			params : {
				contract_name:con_name.getValue(),
				year:year.getValue(),
				start : 0,
				limit : 18				
			}
		});
	}
	
	
	
	
			queryRecord();
	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							region : 'center',
							layout : 'fit',
							border : false,
							margins : '1 0 1 1',
							collapsible : true,
							items : [westgrid]

						}]
			});
			
});