Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	var reType = "部件失效情况半年报";
	var tiType = "1";
	var bview;
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
							fillBy.setValue(result.workerCode);
							fillName.setValue(result.workerName);

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
	// 年度
	
	var yearTime = new Ext.form.TextField({
		        fieldLabel : "年度",
				readOnly : true,
				anchor : '90%',
				name : 'model.year'
	})
	// 小时间数据
	var smartDate_data = [["上半年度", "1"], ["下半年度", "2"],["全部",""]];
	// 小时间，半年报：1，上半年度；2，下半年度。
	var smartDate = new Ext.form.ComboBox({
				name : 'smartDate',
				triggerAction : 'all',
				store : new Ext.data.SimpleStore({
							fields : ['name', 'id'],
							data : smartDate_data
						}),
				hiddenName : 'model.smartDate',
				displayField : 'name',
				valueField : 'id',
				fieldLabel : "半年报",
				mode : 'local',
				disabled : true,
				readOnly : true,
				anchor : '90%'
			})

	// 附件名称
	var annex = {
		id : "annex",
		xtype : 'textfield',
		isFormField : true,
		name : "annex",
		readOnly : true,
		fieldLabel : '内容',
		// fileUpload : true,
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
	// 备注
	var memo = new Ext.form.TextArea({
				fieldLabel : "备注",
				name : 'model.memo',
				readOnly : true,
				anchor : '95%'

			})
	// 填报人姓名
	var fillName = new Ext.form.TextField({
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
				name : 'model.fillDate',
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
				labelWidth : 70,
				layout : 'column',
				fileUpload : true,
				items : [{
							bodyStyle : "padding:10px 0px 0px 0px",
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
						}, {
							border : false,
							layout : 'form',
							columnWidth : 0.8,
							items : [annex]
						}, {
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
							items : [fillName]
						}, {
							columnWidth : .5,
							layout : 'form',
							border : false,
							items : [fillDate]
						}, {
							border : false,
							layout : 'form',
							columnWidth : 1,
							items : [fillBy, checkMark, workFlowNo, reportId]
						}]

			});

	// 左边的弹出窗体

	var blockAddWindow = new Ext.Window({
				// el : 'window_win',
				title : '',
				layout : 'fit',
				width : 550,
				height : 260,
				modal : false,
				closable : true,
				border : false,
				resizable : false,
				closeAction : 'hide',
				plain : true,
				title : "部件失效情况半年报信息",
				// 面板中按钮的排列方式
				buttonAlign : 'center',
				items : [blockForm],
				buttons : [{
					text : '取消',
					handler : function() {
						blockForm.getForm().reset();
						blockAddWindow.hide();
					}
				}]
			});


	// 修改
	  function updateRecord() {
					if (westgrid.selModel.hasSelection()) {

						var records = westgrid.getSelectionModel()
								.getSelected();
						var recordslen = records.length;
						if (recordslen > 1) {
							Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
						} else {
							blockAddWindow.show();
							var record = westgrid.getSelectionModel()
									.getSelected();
							blockForm.getForm().reset();
							blockForm.form.loadRecord(record);
							if (record.get("model.content") != null
									&& record.get("model.content") != "") {
								bview = record.get("model.content");
								btnView.setVisible(true);
								Ext.get("annex").dom.value = bview.replace(
										'/power/upload_dir/productionrec/', '');
							} else {
								btnView.setVisible(false);
							}
						}
					} else {
						Ext.Msg.alert("提示", "请先选择要查看的记录!");
					}
				}

	

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

	
	var year = new Ext.form.TextField({
				fieldLabel : "年度",
				readOnly : true,
				anchor : '90%',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate :new Date().getFullYear().toString(),
									alwaysUseStartDate : true,
									dateFmt : "yyyy"

								});
						this.blur();
					}
				},
				value : new Date().getFullYear()
			});
			
	var date = new Ext.form.ComboBox({
				name : 'smartDate',
				triggerAction : 'all',
				store : new Ext.data.SimpleStore({
							fields : ['name', 'id'],
							data : smartDate_data
						}),
				hiddenName : 'model.smartDate',
				displayField : 'name',
				valueField : 'id',
				fieldLabel : "半年报",
				mode : 'local',
				readOnly : true,
				anchor : '90%',
				value : ""
			})
	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
				ds : westgrids,
				columns : [westsm, new Ext.grid.RowNumberer(), {
							header : "年度",
							width : 40,
							sortable : true,

							dataIndex : 'model.year'
						}, {
							header : "半年度",
							width : 40,
							sortable : false,
							renderer : function changeIt(val) {
								if (val == "1") {
									return "上半年";
								} else if (val == "2") {
									return "下半年";

								} else {
									return "数据异常";
								}
							},
							dataIndex : 'model.smartDate'
						}, {
							header : "标题",
							width : 120,
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
				tbar : ["年度:",year,"半年度:",date,{
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
				viewConfig : {
					forceFit : true
				},
				frame : true,
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : westgrids,
							displayInfo : true,
							displayMsg : "{0} 到 {1} /共 {2} 条",
							emptyMsg : "没有记录"
						}),
				border : true,
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});
	// 取左边store数据
	function queryRecord() {
		westgrids.baseParams = {
					    year : year.getValue(),
						smartDate : date.getValue(),
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
	// westgrid 的事件
	westgrid.on('rowdblclick',updateRecord)

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
	queryRecord();
});