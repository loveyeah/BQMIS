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
							workerCode = result.workerCode;
							workerName = result.workerName;

						}
					}
				});
	}
	// 标题
	var topic = new Ext.form.TextField({
				fieldLabel : "标题",
				readOnly : true,
				name : 'model.topic',
				anchor : '95%'

			});
	//年度
	var yearTime = new Ext.form.TextField({
		        fieldLabel : "年度",
				readOnly : true,
				anchor : '90%',
				name : 'model.year'
	})
	//月份
	var smartDate = new Ext.form.TextField({
				fieldLabel : "月份",
				readOnly : true,
				anchor : '90%',
				name : 'model.smartDate'
	})
	// 附件名称
	var annex = {
		id : "annex",
		xtype : 'textfield',
		isFormField:true,
		name : "annex",
		readOnly : true,
		fieldLabel : '内容',
	//	fileUpload : true,
		height : 22,
		anchor : "95%"
	}

	// 查看
	var btnView = new Ext.Button({
				id : 'btnView',
				text : '查看',
				handler : function() {
					window.open(bview);
				}
			});
	btnView.setVisible(false);
	// 备注
	var memo = new Ext.form.TextArea({
				fieldLabel : "备注",
				name : 'model.memo',
				readOnly : true,
				anchor : '95%'

			})
	// 填报人姓名
	var fillByName = new Ext.form.TextField({
				fieldLabel : "填报人",
				readOnly : true,
				name : 'fillName',
				anchor : '90%'

			})
	// 填报人编码
	var fillBy = new Ext.form.Hidden({
				name : 'model.fillBy'

			})
	// 填报时间
	var fillDate = new Ext.form.TextField({
				fieldLabel : "填报时间",
				readOnly : true,
				name : 'fillDate',
				value : getDate(),
				anchor : '90%'

			})
	// 审批标志
	var checkMark = new Ext.form.Hidden({
				name : 'model.checkMark'

			})
	// 工作流号
	var workFlowNo = new Ext.form.Hidden({
				name : 'model.workFlowNo'

			})
	// 主键
	var reportId = new Ext.form.Hidden({
				name : 'model.reportId'

			})

	// 弹窗的表单对象
	var blockForm = new Ext.form.FormPanel({
				labelAlign : 'right',
				frame : true,
				labelWidth : 80,
				style : 'padding:10px,0px,0px,5px',
				layout : 'column',
				fileUpload : true,
				items : [{

							border : false,
							layout : 'form',
							columnWidth : 1,
							items : [topic]
						}, {
							columnWidth : .5,
							layout : 'form',
							border : false,
							items : [yearTime]
						}, {
							columnWidth : .5,
							layout : 'form',
							border : false,
							items : [smartDate]
						},{
							columnWidth : 0.8,
							border : false,
							layout : 'form',
							items : [annex]
						},{
							columnWidth : 0.2,
							border : false,
							layout : 'form',
							items : [btnView]
						}, {
							border : false,
							layout : 'form',
							columnWidth : 1,
							items : [memo]
						}, {
							columnWidth : .5,
							layout : 'form',
							border : false,
							items : [fillByName]
						}, {
							columnWidth : .5,
							layout : 'form',
							border : false,
							items : [fillDate]
						}]

			});

	// 弹出窗体
	var win = new Ext.Window({
		width : 500,
		height : 260,
		buttonAlign : "center",
		items : [blockForm],
		layout : 'fit',
		closeAction : 'hide',
		title : "电除尘器运行监督月报信息",
		resizable : false,
		modal : true,
		buttons : [{
			text : '取消',
			iconCls:'cancer',
			handler : function() { 
				win.hide();
			}
		}]

	});


	var westsm = new Ext.grid.CheckboxSelectionModel();
	// 左边列表中的数据
	var datalist = new Ext.data.Record.create([

	{
				name : 'model.reportId'
			}, {
				name : 'model.topic'
			}, {
				name : 'model.content'
			}, {
				name : 'model.year'
			}, {
				name : 'model.smartDate'
			}, {
				name : 'model.workFlowNo'
			}, {
				name : 'model.checkMark'
			}, {
				name : 'model.fillBy'
			}, {
				name : 'fillDate'
			}, {
				name : 'model.memo'
			}, {
				name : 'fillName'
			}]);


	var westgrids = new Ext.data.JsonStore({
				url : 'productionrec/findPtJReportList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});
	// 年度
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
	//var d = new Date();
	//year = d.getMonth()+1;
	//alert(year);
	//月份
	var month = new Ext.form.TextField({
		fieldLabel : "月份",
				readOnly : true,
				anchor : '90%',
				//name : 'model.smartDate',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : new Date().getMonth()+1
											.toString(),
									alwaysUseStartDate : true,
									dateFmt : "MM"

								});
						this.blur();
					}
				},
				value : ((new Date().getMonth()+1) > 9 ? "" : "0")+(new Date().getMonth()+1)
	});
	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
				ds : westgrids,
				columns : [westsm, new Ext.grid.RowNumberer(), {
							header : "年度",
							width : 60,
							//align : "center",
							sortable : true,

							dataIndex : 'model.year'
						}, {
							header : "月份",
							width : 60,
							//align : "center",
							sortable : false,
							dataIndex : 'model.smartDate'
						}, {
							header : "标题",
							width : 150,
							//align : "center",
							sortable : true,
							dataIndex : 'model.topic'
						},{
							header : "内容",
							width : 75,
							sortable : true,
							dataIndex : 'model.content',
							renderer:function(v){
								if(v !=null && v !='')
								{ 
									var s =  '<a href="#" onclick="window.open(\''+v+'\');return  false;">[查看]</a>';
									return s;
								}
							} 
			
		                  }],
				viewConfig : {
			                 forceFit : true
		           },
				tbar : ["年度:",year,"月份:",month,{
			              id : 'btnQuery',
					       text : "查询",
						   iconCls : 'query',
					       handler : queryRecord
		                  },{
			               text : "查看详细信息",
			               iconCls : 'list',
			              handler : function() {updateRecord()}
		                 }],
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
	function updateRecord()
	{
			if (westgrid.selModel.hasSelection()) {
		
			var records = westgrid.getSelectionModel().getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一行信息查看！");
			} else {
				  win.show(); 
				var record = westgrid.getSelectionModel().getSelected();
		        blockForm.getForm().reset();
		        blockForm.form.loadRecord(record);
		        if(record.get("model.content")!=null&&record.get("model.content")!="")
		        {    
		               bview=record.get("model.content");
		              btnView.setVisible(true);
		              Ext.get("annex").dom.value = bview.replace('/power/upload_dir/productionrec/','');
		        }
		        else
		        {
		        	  btnView.setVisible(false);
		        }
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要查看的记录!");
		}
	}
	function queryRecord()
	{
		westgrids.baseParams = {
			    year : year.getValue(),
				smartDate : month.getValue(),
				reportType : reType,
				timeType : tiType
		}
		westgrids.load({
			params : {
				start : 0,
				limit : 18				
			}
		});
	}
	// grid 的事件
	westgrid.on('rowdblclick', updateRecord);

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
	queryRecord()
});