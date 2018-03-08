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
	var bview;
		var btnView = new Ext.Button({
				id : 'btnView',
				text : '查看',
				handler : function() {
					window.open(bview);
				}
			});
	// 标题
	

	
	var annex = new Ext.form.TextField({
		id : "annex",
		inputType : "file",
		fieldLabel : '选择附件',
		width : 300,
		height:20,
		name : 'annex' 
	})


	
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
	
	
	// 新建按钮
	var westbtnAdd = new Ext.Button({
				text : '保存',
				iconCls : 'save',
				handler : addRecord
			});

	
	// 填写L值，B2值按钮
	var westbtnedit = new Ext.Button({
				text : '填写L值,B2',
				iconCls : 'write',
				handler : function (){
//					alert("");
//					var scoreWin = ScoreWrite();
//					scoreWin.win.show();
					if (westgrid.selModel.hasSelection()) {
					var srecords = westgrid.getSelectionModel().getSelections();
					var leng = srecords.length;
						if (leng > 1) {
							Ext.Msg.alert("系统提示信息", "请选择其中一项填写L，B2值！");
						} else {
							var arg=new Object();
							arg.dangerId=srecords[0].data.dangerId;
							window.showModalDialog('dangerScoreWrite.jsp', arg,
							'status:no;dialogWidth=600px;dialogHeight=250px');
					        
						}}else{
							Ext.Msg.alert("提示", "请先选择一条记录填写!");
						}
					

				}
			});


		// 确定按钮
	var sure = new Ext.Button({
		text : '确定',
		iconCls : Constants.CLS_OK,
		handler : function (){
			Ext.Msg.confirm('提示', '确定上报数据吗?', function(button) {
														if (button == 'yes') {
															Ext.Msg.wait('正在确定数据，请稍候...');
															Ext.Ajax.request({
															url : 'security/confirmData.action',
															method : 'post',
															success : function(form, options) {
															var obj = Ext.util.JSON
																	.decode(form.responseText)
															if (obj && obj.msg) {
																Ext.Msg.alert('提示', obj.msg);
																
															}
															westgrids.reload();
														},
															failure : function(result, request) {
																Ext.MessageBox.alert('提示信息', '确定失败！')
															}
														});
														
													}});
		}
	});
	
	// 查询按钮
	var westbtnref = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : function() {
					queryRecord();
				}
			});
	var sep = new Ext.Button({
		text:'-'
	});

	var westsm = new Ext.grid.CheckboxSelectionModel();
	// 左边列表中的数据
	
			
				var datalist = new Ext.data.Record.create([

	{			
				name : 'dangerId'
			}, {
				name : 'dangerYear'
			},{
				name:'dangerName'
			},{
				name : 'finishDate'
			}, {
				name : 'assessDept'
			}, {
				name : 'chargeBy'
			}, {
				name : 'memo'
			}, {
				name : 'workFlowNo'
			}, {
				name : 'status'
			}, {
				name : 'orderBy'
			}, {
				name : 'DValue'
			}, {
				name : 'd1Value'
			}, {
				id:'valueLevel',
				name : 'valueLevel'
			}, {
				name : 'annex'
			}]);

	var westgrids = new Ext.data.JsonStore({
				url : 'security/findDangerReportList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});

	// 左边列表
	var westgrid = new Ext.grid.EditorGridPanel({
				ds : westgrids,
				clicksToEdit:1,
				columns : [westsm, new Ext.grid.RowNumberer(), {
							header : "危险源名称",
							width : 75,
							//align : "center",
							sortable : true,
							dataIndex : 'dangerName'
						}, {
							header : "完成时间",
							width : 75,
							//align : "center",
							sortable : false,
							dataIndex : 'finishDate',
							renderer:function(v){
								return v.substring(0,10);
							}
							
						},{
							header : "D值",
							width : 75,
							sortable : true,
							dataIndex : 'DValue',
							editor : new Ext.form.NumberField({
								allowBlank : true,
								id : 'DValue'
							})

			
		                  },{
							header : "D1值",
							width : 75,
							sortable : true,
							dataIndex : 'd1Value',
								editor : new Ext.form.NumberField({
								allowBlank : true,
								id : 'd1Value'
							})
			
		                  },{
							header : "级别",
							width : 75,
							sortable : true,
							dataIndex : 'valueLevel',
							renderer:function(v){
								if(v=='1'){
									return '1级';					
								}if(v=='2'){
									return '2级';					
								}if(v=='3'){
									return '3级';					
								}if(v=='4'){
									return '4级';					
								}
							},
								editor : new Ext.form.ComboBox({
									store : [['1', '1级'],['2', '2级'], ['3', '3级'],['4', '4级']],
									value : '',
									name : 'valueLevel',
									valueField : "value",
									displayField : "text",
									fieldLabel : "级别",
									mode : 'local',
									readOnly : true,
									anchor : '90%',
									typeAhead : true,
									forceSelection : true,
									editable : false,
									triggerAction : 'all',
									disabled : false,
									selectOnFocus : true
								})
			
		                  },{
							header : "评估部门",
							width : 75,
							sortable : true,
							dataIndex : 'assessDept'
		                  },{
							header : "责任人",
							width : 75,
							sortable : true,
							dataIndex : 'chargeBy'
		                  },{
							header : "备注",
							width : 75,
							sortable : true,
							dataIndex : 'memo'
		                  },{
							header : "附件",
							width : 75,
							sortable : true,
							dataIndex : 'annex',
							renderer:function(v){
								if(v !=null && v !='')
								{ 
									var s =  '<a href="#" onclick="window.open(\''+v+'\');return  false;">[查看]</a>';
									return s;
								}else{
									return '没有附件';
								}
							} 
						}],
				viewConfig : {
			                 forceFit : true
		           },
				tbar : ['年度：',year,westbtnref, {
							xtype : "tbseparator"
						},westbtnAdd , {
							xtype : "tbseparator"
						}, sure],
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

	// westgrid 的事件
	function queryRecord()
	{
		
		westgrids.baseParams = {
				status:'3',
				year:year.getValue(),
			    reportType : reType,
				timeType : tiType
		}
		westgrids.load({
			params : {
				status:'3',
				year:year.getValue(),
				start : 0,
				limit : 18				
			}
		});
	}
	function addRecord()
	{  
		westgrid.stopEditing();
		var modifyRec = westgrid.getStore().getModifiedRecords();
		 if(modifyRec.length>0)
		 {
		 	
		Ext.Msg.confirm('提示', '确定要保存修改数据吗?', function(button) {
					if (button == 'yes') {
						var updateData = new Array();
						for (var i = 0; i < modifyRec.length; i++) {
							
							updateData.push(modifyRec[i].data);
							
						}
						Ext.Ajax.request({
									url : 'security/updateDangerReport.action',
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
									westgrids.rejectChanges();
									westgrids.reload();
								},
									failure : function(result, request) {
										Ext.MessageBox.alert('提示信息', '操作失败！')
									}
								})
					}
				})
	}
	else   {
		Ext.Msg.alert('提示','没有更改的数据!')
	}
	}
	
	
	 
	var vc = new valueCheck();
	
	 var tabs = new Ext.TabPanel({
				activeTab : 0,
				tabPosition : 'bottom',
				plain : false,
				defaults : {
					autoScroll : true
				},
				items : [{
							id : 'westgrid',
							title : '危险源评估报告',
							items : westgrid,
							autoScroll : true,
							layout : 'fit'
						}, {
							id : 'panel',
							title : '查看L,B2,D1值',
							autoScroll : true,
							items : vc.vp,
							layout : 'fit'
						}]
			});
			
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
							items : [tabs]

						}]
			});
			
});