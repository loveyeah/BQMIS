Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
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
	// 取左边store数据
	function queryRecord() {
		westgrids.baseParams = {
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
	
	// 标题
	var topic = new Ext.form.TextField({
				fieldLabel : "标题",
				name : 'model.topic',
				anchor : '95%'

			});
	// 年度
	var year = new Ext.form.TextField({
				fieldLabel : "年度",
				readOnly : true,
				anchor : '90%',
				value: new Date().getFullYear().toString(),
				name : 'model.year',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate :"",
									alwaysUseStartDate : true,
									dateFmt : "yyyy"

								});
						this.blur();
					}
				},
				value : new Date().getFullYear()
			});
	function checkInput()
	{
		if(topic.getValue()==""){
			 Ext.Msg.alert("提示","请输入标题！");
			 return false;
		}
		if(year.getValue() == ""){
			Ext.Msg.alert("提示","请输入年度！");
			return false;
		}
		return true;
	}
	// 小时间数据
	var smartDate_data = [["上半年度", "1"], ["下半年度", "2"]];
	// 小时间，半年报：1，上半年度；2，下半年度。
	var smartDate = new Ext.form.ComboBox({
				name : 'smartDate',

				allowBlank : false,
				triggerAction : 'all',
				store : new Ext.data.SimpleStore({
							fields : ['name', 'id'],
							data : smartDate_data
						}),
				hiddenName : 'model.smartDate',
				displayField : 'name',
				valueField : 'id',
				value : '1',
				fieldLabel : "半年报",
				mode : 'local',

				readOnly : true,
				anchor : '90%'
			})

	// 附件名称
	var annex = {
		id : "annex",
		xtype : 'fileuploadfield',
		isFormField : true,
		name : "annex",
		fieldLabel : '内容',
		// fileUpload : true,
		height : 21,
		anchor : "95%",
		buttonCfg : {
			text : '浏览...',
			iconCls : 'upload-icon'
		}
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
							items : [year]
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
				height : 240,
				modal : false,
				closable : true,
				border : false,
				resizable : false,
				closeAction : 'hide',
				plain : true,
				// 面板中按钮的排列方式
				buttonAlign : 'center',
				items : [blockForm],
				buttons : [{
					text : '保存',
					iconCls:'save',
					handler : function() {
						var myurl = "";
						//if (blockForm.getForm().isValid())
							var myurl = "";
						 if (reportId.getValue() == "") {
							myurl = "productionrec/addPtJReportInfo.action";
						} else {
							myurl = "productionrec/updatePtJReportInfo.action";
						}
						if(!checkInput()) return;
						blockForm.getForm().submit({
							method : 'POST',
							url : myurl,
							params : {
								reportType:reType,
								timeType : tiType,
								filePath : Ext.get("annex").dom.value
							},
							success : function(form, action) {
								var o = eval("(" + action.response.responseText
										+ ")");
								//Ext.Msg.alert("注意", o.msg);
								if (o.msg.indexOf("成功") != -1) {
									queryRecord();
									blockAddWindow.hide();

								}
							},
							faliue : function() {
								Ext.Msg.alert('错误', '出现未知错误.');
							}
						});

					}
				}, {
					text : '取消',
					iconCls:'cancer',
					handler : function() {
						blockForm.getForm().reset();
						blockAddWindow.hide();
					}
				}]
			});

	// 新建按钮
	var westbtnAdd = new Ext.Button({
				text : '新增',
				iconCls : 'add',
				handler : function() {
					blockForm.getForm().reset();
					getWorkCode();
					blockAddWindow.show();
					btnView.setVisible(false);
					blockAddWindow.setTitle("增加部件失效半年报信息");
				}
			});

	// 修改按钮
	var westbtnedit = new Ext.Button({
				id : 'btnUpdate',
				text : '修改',
				iconCls : 'update',
				handler : function() {
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
							blockAddWindow.setTitle("修改部件失效半年报信息");
						}
					} else {
						Ext.Msg.alert("提示", "请先选择要编辑的行!");
					}
				}
			});

	// 删除按钮
	var westbtndel = new Ext.Button({
				text : '删除',
				iconCls : 'delete',
				handler : function() {
					var records = westgrid.selModel.getSelections();
					var ids = [];
					if (records.length == 0) {
						Ext.Msg.alert("提示", "请选择要删除的记录！");
					} else {
						for (var i = 0; i < records.length; i += 1) {
							var member = records[i];
							if (member.get("model.reportId")) {
								ids.push(member.get("model.reportId"));
							} else {

								store.remove(store.getAt(i));
							}
						}

						Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(
										buttonobj) {

									if (buttonobj == "yes") {
										Ext.lib.Ajax.request('POST','productionrec/deletePtJReportInfo.action',
														{
															success : function(action) {
																//Ext.Msg.alert("提示","删除成功！")
																queryRecord();
															},
															failure : function() {
																Ext.Msg.alert('错误','删除时出现未知错误.');

															}
														}, 'ids=' + ids);
									}
								});

					}

				}
			});

	// 刷新按钮
	var westbtnref = new Ext.Button({
				text : '刷新',
				iconCls : 'reflesh',
				handler : function() {
					queryRecord();
				}
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

	queryRecord();

	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
				ds : westgrids,
				columns : [westsm, new Ext.grid.RowNumberer(), {
							header : "年度",
							width : 40,
							//align : "center",
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
				tbar : [westbtnAdd, {
							xtype : "tbseparator"
						}, westbtnedit, {
							xtype : "tbseparator"
						}, westbtndel, {
							xtype : "tbseparator"
						}, westbtnref],
				sm : westsm,
				viewConfig : {
					forceFit : true
				},
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
	westgrid.on('rowdblclick',function(){
			Ext.get("btnUpdate").dom.click();
	})

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