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
	
	
	//查询类型
	var valueSort = new Ext.form.ComboBox({
		store : [['L', 'L值'], ['B2', 'B2值'], ['D', 'D值']],
		value : 'L',
		//id : 'smartDate',
		name : 'model.assessSort',
		valueField : "value",
		displayField : "text",
		fieldLabel : "值类型选择：",
		mode : 'local',
		readOnly : true,
		anchor : '90%',
		typeAhead : true,
		forceSelection : true,
		editable : false,
		triggerAction : 'all',
		disabled : false,
		selectOnFocus : true
	});

	if(checkReport=='1'){
		checkval.setVisible(true);
		sure.setVisible(true);
		westbtnedit.setVisible(false);
		westbtnrep.setVisible(false);
		westbtnupl.setVisible(false);
	}
		
	
	// 查询按钮
	var westbtnref = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : function() { 
					var cur_type = valueSort.getValue();
					if(cur_type =='D')
					{
						var _h= Ext.getCmp("westgrid").getEl().getHeight();
						westgrid.hide();
				    	westgrid_d.show(); 
				    	westgrid_d.setHeight(_h); 
				    	westgrids_d.baseParams = {
								year:year.getValue(),
								type:valueSort.getValue(),
							    reportType : reType,
								timeType : tiType
							}
							westgrids_d.load({
								params : {
									year:year.getValue(),
									type:valueSort.getValue(),
									start : 0,
									limit : 18				
								},
								callback:addLine_d
							});
				    	westgrid_d.getView().refresh();
					}else
					{
						westgrid.show();
			    		westgrid_d.hide();
			    		queryRecord();
					} 
					    
				}
			});
			var sm = new Ext.grid.CheckboxSelectionModel();
			var sm_d = new Ext.grid.CheckboxSelectionModel();	
	// 左边列表中的数据
	
			
				var datalist = new Ext.data.Record.create([

			{			
				name : 'danger_id',
				mapping:0
			}, {
				name : 'danger_name',
				mapping:1
			},{
				name : 'score1',
				mapping:2
			}, {
				name : 'score2',
				mapping:3
				
			}, {
				name : 'score3',
				mapping:4
			}, {
				name : 'score4',
				mapping:5
			}, {
				name : 'score5',
				mapping:6
			}, {
				name : 'score6',
				mapping:7
			}, {
				name : 'score7',
				mapping:8
			}, {
				name : 'score8',
				mapping:9
			}, {
				name : 'score9',
				mapping:10
			}, {
				name : 'score10',
				mapping:11
			}]);
			
					var datalist_d = new Ext.data.Record.create([

			{			
				name : 'danger_id',
				mapping:0
			}, {
				name : 'danger_name',
				mapping:1
			},{
				name : 'd1_value',
				mapping:2
			}, {
				name : 'value_level',
				mapping:3
				
			}]);

	var westgrids = new Ext.data.JsonStore({
				url : 'security/getDangerValueList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});
			var westgrids_d = new Ext.data.JsonStore({
				url : 'security/getDangerValueList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist_d
			});

	// 左边列表
			
    function addLine() {

		// 统计行
		var newRecord = new datalist({
					'danger_name' : '合计',
					'score1' : '',
					'score2' : '',
					'score3' : '',
					'score4' : '',
					'score5' : '',
					'score6' : '',
					'score7' : '',
					'score8' : '',
					'score9' : '',
					'score10' : ''
					
				});

		// 原数据个数
		var count = westgrids.getCount();
		// 停止原来编辑
		// grid.stopEditing();
		// 插入统计行
		westgrids.insert(count, newRecord);
		westgrid.getView().refresh();
	};
	 function addLine_d() {

		// 统计行
		var newRecord = new datalist_d({
					'danger_name' : '合计',
					'd1_value' : '',
					'value_level' : '',
					'':''
				});

		// 原数据个数
		var count = westgrids_d.getCount();
		// 停止原来编辑
		// grid.stopEditing();
		// 插入统计行
		westgrids_d.insert(count, newRecord);
		westgrids_d.reload();
	}
	var columnModel = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(),
		{
							header : "重大危险源名称",
							width : 100,
							//align : "center",
							sortable : true,
							dataIndex : 'danger_name'
						}, {
					header : '1分',
					dataIndex : 'score1',
					align : 'center',
					width : 100,
					renderer:function(value, cellmeta, record,
									rowIndex, columnIndex, store){
									         if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                            
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                            	
		                                totalSum =totalSum+ parseInt(store.getAt(i).get('score1'));
		                            }
		                            if (store.getAt(store.getCount() - 1).get('danger_name') == '合计') {
		                                store.getAt(store.getCount() - 1).set('score1', totalSum);
		                            }
		                            return value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                            	if(store.getAt(i).get('score1')!=null)
		                                totalSum =totalSum + parseInt(store.getAt(i).get('score1'));
		                            }
		                            return totalSum;
		                        }
									}

				}, {
					header : '2分',
					dataIndex : 'score2',
					align : 'center',
					width : 100,
					renderer:function(value, cellmeta, record,
									rowIndex, columnIndex, store){
									         if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                            
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                            	
		                                totalSum =totalSum+ parseInt(store.getAt(i).get('score2'));
		                            }
		                            if (store.getAt(store.getCount() - 1).get('danger_name') == '合计') {
		                                store.getAt(store.getCount() - 1).set('score2', totalSum);
		                            }
		                            return value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                            	if(store.getAt(i).get('score2')!=null)
		                                totalSum =totalSum + parseInt(store.getAt(i).get('score2'));
		                            }
		                            return totalSum;
		                        }
									}
					

				}, {
					header : '3分',
					dataIndex : 'score3',
					align : 'center',
					width : 100,
					renderer:function(value, cellmeta, record,
									rowIndex, columnIndex, store){
									         if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                            
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                            	
		                                totalSum =totalSum+ parseInt(store.getAt(i).get('score3'));
		                            }
		                            if (store.getAt(store.getCount() - 1).get('danger_name') == '合计') {
		                                store.getAt(store.getCount() - 1).set('score3', totalSum);
		                            }
		                            return value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                            	if(store.getAt(i).get('score3')!=null)
		                                totalSum =totalSum + parseInt(store.getAt(i).get('score3'));
		                            }
		                            return totalSum;
		                        }
									}
					

				}, {
					header : '4分',
					dataIndex : 'score4',
					align : 'center',
					width : 100,
					renderer:function(value, cellmeta, record,
									rowIndex, columnIndex, store){
									         if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum = parseInt(totalSum+store.getAt(i).get('score4'));
		                            }
		                            if (store.getAt(store.getCount() - 1).get('danger_name') == '合计') {
		                                store.getAt(store.getCount() - 1).set('score4', totalSum);
		                            }
		                            return value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                            	if(store.getAt(i).get('score4')!=null)
		                                totalSum =totalSum+parseInt(store.getAt(i).get('score4'));
		                            }
		                            return totalSum;
		                        }
									}
				

				}, {
					header : '5分',
					dataIndex : 'score5',
					align : 'center',
					width : 100,
					renderer:function(value, cellmeta, record,
									rowIndex, columnIndex, store){
									         if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                            
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                            	
		                                totalSum =totalSum+ parseInt(store.getAt(i).get('score5'));
		                            }
		                            if (store.getAt(store.getCount() - 1).get('danger_name') == '合计') {
		                                store.getAt(store.getCount() - 1).set('score5', totalSum);
		                            }
		                            return value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                            	if(store.getAt(i).get('score5')!=null)
		                                totalSum =totalSum + parseInt(store.getAt(i).get('score5'));
		                            }
		                            return totalSum;
		                        }
									}
					
					

				}, {
					header : '6分',
					dataIndex : 'score6',
					align : 'center',
					width : 100,
					renderer:function(value, cellmeta, record,
									rowIndex, columnIndex, store){
									         if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                            
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                            	
		                                totalSum =totalSum+ parseInt(store.getAt(i).get('score6'));
		                            }
		                            if (store.getAt(store.getCount() - 1).get('danger_name') == '合计') {
		                                store.getAt(store.getCount() - 1).set('score6', totalSum);
		                            }
		                            return value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                            	if(store.getAt(i).get('score6')!=null)
		                                totalSum =totalSum + parseInt(store.getAt(i).get('score6'));
		                            }
		                            return totalSum;
		                        }
									}
					
					

				}, {
					header : '7分',
					dataIndex : 'score7',
					align : 'center',
					width : 100,
					renderer:function(value, cellmeta, record,
									rowIndex, columnIndex, store){
									         if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                            
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                            	
		                                totalSum =totalSum+ parseInt(store.getAt(i).get('score7'));
		                            }
		                            if (store.getAt(store.getCount() - 1).get('danger_name') == '合计') {
		                                store.getAt(store.getCount() - 1).set('score7', totalSum);
		                            }
		                            return value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                            	if(store.getAt(i).get('score7')!=null)
		                                totalSum =totalSum + parseInt(store.getAt(i).get('score7'));
		                            }
		                            return totalSum;
		                        }
									}
					

				}, {
					header : '8分',
					dataIndex : 'score8',
					align : 'center',
					width : 100,
					renderer:function(value, cellmeta, record,
									rowIndex, columnIndex, store){
									         if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                            
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                            	
		                                totalSum =totalSum+ parseInt(store.getAt(i).get('score8'));
		                            }
		                            if (store.getAt(store.getCount() - 1).get('danger_name') == '合计') {
		                                store.getAt(store.getCount() - 1).set('score8', totalSum);
		                            }
		                            return value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                            	if(store.getAt(i).get('score8')!=null)
		                                totalSum =totalSum + parseInt(store.getAt(i).get('score8'));
		                            }
		                            return totalSum;
		                        }
									}
					

				}, {
					header : '9分',
					dataIndex : 'score9',
					align : 'center',
					width : 100,
					renderer:function(value, cellmeta, record,
									rowIndex, columnIndex, store){
									         if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                            
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                            	
		                                totalSum =totalSum+ parseInt(store.getAt(i).get('score9'));
		                            }
		                            if (store.getAt(store.getCount() - 1).get('danger_name') == '合计') {
		                                store.getAt(store.getCount() - 1).set('score9', totalSum);
		                            }
		                            return value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                            	if(store.getAt(i).get('score9')!=null)
		                                totalSum =totalSum + parseInt(store.getAt(i).get('score9'));
		                            }
		                            return totalSum;
		                        }
									}

				}, {
					header : '10分',
					dataIndex : 'score10',
					align : 'center',
					width : 100,
					renderer:function(value, cellmeta, record,
									rowIndex, columnIndex, store){
									         if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                            
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                            	
		                                totalSum =totalSum+ parseInt(store.getAt(i).get('score10'));
		                            }
		                            if (store.getAt(store.getCount() - 1).get('danger_name') == '合计') {
		                                store.getAt(store.getCount() - 1).set('score10', totalSum);
		                            }
		                            return value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                            	if(store.getAt(i).get('score10')!=null)
		                                totalSum =totalSum + parseInt(store.getAt(i).get('score10'));
		                            }
		                            return totalSum;
		                        }
									}
				}
	]);
	var jk_unit ='';
	var columnModel_d = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(),
		{
							header : "重大危险源名称",
							width : 100,
							//align : "center",
							sortable : true,
							dataIndex : 'danger_name'
						}, {
					header : '相对扣分率（%）D1值',
					dataIndex : 'd1_value',
					align : 'center',
					width : 150,
					renderer:function(value, cellmeta, record,
									rowIndex, columnIndex, store){
									         if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum = parseInt(totalSum+store.getAt(i).get('d1_value'));
		                            }
		                            if (store.getAt(store.getCount() - 1).get('danger_name') == '合计') {
		                                store.getAt(store.getCount() - 1).set('d1_value', totalSum);
		                            }
		                            return value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                            	if(store.getAt(i).get('d1_value')!=null)
		                                totalSum =totalSum+parseInt(store.getAt(i).get('d1_value'));
		                            }
		                            return totalSum;
		                        }
									}
				}, {
					header : '等级',
					dataIndex : 'value_level',
					align : 'center',
					width : 100,
					renderer:function(v){
						jk_unit = "";
						if(v=='1'){
							jk_unit = "无";
							return "1级";
						}
						else if(v=='2'){
							jk_unit = "无";
							return "2级";
						}
						else if(v=='3'){
						 	jk_unit = "发电公司";
						 	return "3级";
						}
						else if(v=='4'){
							jk_unit = "本厂";
							return "4级";
						}
							
						
					}
					
				}, {
					header : '监控单位',
					dataIndex : '',
					align : 'center',
					width : 100,
					renderer:function(){
						return jk_unit;
					}
				}
	]);
	var westgrid = new Ext.grid.GridPanel({ 
		        id:'westgrid',
				ds : westgrids, 
				cm:columnModel,  
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : westgrids,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						}), 
				enableColumnHide : false,
				enableColumnMove : false 
			});  
	
	  var westgrid_d = new Ext.grid.GridPanel({
				ds : westgrids_d, 
				autoScroll:true,
				cm:columnModel_d,
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : westgrids_d,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						}), 
						
				enableColumnHide : false,
				enableColumnMove : false
			});  
			//westgrid_d.hide();
	var panel = new Ext.Panel({
		tbar : ['年度：', year, '-', '类型选择：', valueSort, westbtnref, {
			xtype : "tbseparator"
		},'-',{
			text:'导出',
			iconCls:'export',
			handler:function(){
					//exportRecord();
					var url = "/power/security/exportData.action?type="+valueSort.getValue()+"&year="+year.getValue();
					window.open(url);
			}
			
			
		}],
		layout:'fit',
		items : [westgrid,westgrid_d] 
	});  
/*
	function exportRecord()
	{	
		var ds = westgrids;
		if (ds == null || ds.getTotalCount() == 0) {
				Ext.Msg.alert('提示信息', '无数据进行导出！');
				return;
			}
		
			Ext.Msg.confirm('提示信息', '确认要导出数据吗？', function(id) {
				if (id == 'yes') {
					
				}
			})
	}*/
	
	 /**
     * 将HTML转化为Excel文档
     */
    function tableToExcel(tableHTML){
		window.clipboardData.setData("Text",tableHTML);
		try{
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExApp.visible = true;
		}catch(e){
			if(e.number != -2146827859){
				Ext.Msg.alert('提示','您的电脑没有安装Microsoft Excel软件!')
			}
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}		
			
 // 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "fit",
				border : false,
				items : [panel]
			}); 

	// westgrid 的事件
	function queryRecord()
	{
		
		westgrids.baseParams = {
					year:year.getValue(),
					type:valueSort.getValue(),
				    reportType : reType,
					timeType : tiType
				}
				westgrids.load({
					params : {
						year:year.getValue(),
						type:valueSort.getValue(),
						start : 0,
						limit : 18				
					},
					callback:addLine
					
				});
			
			
			
	}
	
			queryRecord();
});