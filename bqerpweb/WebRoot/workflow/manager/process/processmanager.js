Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var Process = Ext.data.Record.create([{
				name : 'workflowType'
			}, {
				name : 'version'
			}, {
				name : 'name'
			}, {
				name : 'desc'
			}, {
				name : 'flowListUrl'
			}, {
				name : 'tableName'
			}, {
				name : 'wfIdColName'
			}, {
				name : 'wfStatusColName'
			}, {
				name : 'tableKeyColName'
			}, {
				name : 'isRunning'
			}, {
				name : 'name'
			}, {
				name : 'isShowInMyJob'
			}, {
				name : 'displayNo'
			}]);
	var processDs = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'MAINTWorkflow.do?action=workflowlist',
							method : 'post'
						}),
				reader : new Ext.data.JsonReader({}, Process)
			});
	processDs.load();
	var processSm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
			// ,
			// listeners : {
			// rowselect : function(sm, row, rec) {
			// //Ext.Msg.wait("请等候", "加载中", "操作进行中...");
			// var type = rec.data.workflowType;
			// var name = rec.data.name;
			// win.show();
			// form.getForm().loadRecord(rec);
			// }
			// }
		});

	var processCm = new Ext.grid.ColumnModel([processSm, {
				header : '编码',
				dataIndex : 'workflowType',
				align : 'left',
				width : 80,
				editor : new Ext.form.TextField({})
			}, {
				header : '流程名称',
				dataIndex : 'name',
				align : 'left',
				width : 130
			}, {
				header : '版本号',
				dataIndex : 'version',
				align : 'left',
				width : 80
			}, {
				header : '审批链接地址',
				dataIndex : 'flowListUrl',
				align : 'left',
				width : 120
			}, {
				header : '说明',
				dataIndex : 'desc',
				align : 'left',
				width : 130
			}, {
				header : '业务主表名称',
				dataIndex : 'tableName',
				align : 'left',
				width : 130
			}, {
				header : '工作流实例号列名',
				dataIndex : 'wfIdColName',
				align : 'left',
				width : 130
			}, {
				header : '工作流状态列名',
				dataIndex : 'wfStatusColName',
				align : 'left',
				width : 130
			}, {
				header : '业务主表主键列名',
				dataIndex : 'tableKeyColName',
				align : 'left',
				width : 100
			}, {
				header : '当前运行版本',
				dataIndex : 'isRunning',
				renderer : function(v) {
					return v == 'Y' ? '√' : '×';
				},
				align : 'left',
				width : 100
			}, {
				header : '是否在我的事务中显示',
				dataIndex : 'isShowInMyJob',
				renderer : function(v) {
					return v == 'Y' ? '√' : '×';
				},
				align : 'left',
				width : 100
			}, {
				header : '排序',
				dataIndex : 'displayNo',
				align : 'left',
				width : 130
			}]);

	var workFlowText = new Ext.form.TextField({
				id : 'workFlowText',
				name : 'workFlowText',
				fieldLabel : '工作流名称'
			})

	var btnWorkFlow = new Ext.Button({
				id : 'btnWorkFlow',
				iconCls : 'query',
				text : "查询",
				handler : querydata
			})
	function querydata() {
		processDs.load();
		processDs.on("load", function() {
					var name = workFlowText.getValue();
					if (name != null && name != "") {
						processGrid.store.filter('name',name,true);
					}
				})
	}
	var btntbar = new Ext.Toolbar({
				items : ["工作流名称", workFlowText, '-', btnWorkFlow]
			})
	processCm.defaultSortable = true;
	var processGrid = new Ext.grid.EditorGridPanel({
				layout : 'fit',
				ds : processDs,
				cm : processCm,
				sm : processSm,
				// title : '工作流列表',
				autoScroll : true,
				autoWidth : true,
				autoHeight : true,
				border : false,
				tbar : btntbar,
				clicksToEdit  :1
				
			});
	processGrid.enableColumnHide = false;
	processGrid.on("dblclick", function() {
				var rec = processGrid.getSelectionModel().getSelected();
				win.show();
				form.getForm().loadRecord(rec);
			});
	processGrid.render(Ext.getBody());
	var isRunning = new Ext.Panel({
				id : 'isRunning',
				layout : 'column',
				isFormField : true,
				fieldLabel : '是否当前运行',
				border : false,
				items : [{
							columnWidth : .2,
							border : false,
							items : new Ext.form.Radio({
										boxLabel : '运行',
										name : 'isRunning',
										inputValue : 'Y',
										checked : true
									})
						}, {
							columnWidth : .3,
							border : false,
							items : new Ext.form.Radio({
										boxLabel : '注销',
										name : 'isRunning',
										inputValue : 'N'
									})
						}]
			});

	var isShowInMyJob = {
		xtype : "panel",
		id : 'isShowInMyJob',
		layout : 'column',
		isFormField : true,
		fieldLabel : '是否在<我的事务>中显示',
		border : false,
		items : [{
					columnWidth : .2,
					border : false,
					items : new Ext.form.Radio({
								boxLabel : '显示',
								name : 'isShowInMyJob',
								inputValue : 'Y',
								checked : true
							})
				}, {
					columnWidth : .3,
					border : false,
					items : new Ext.form.Radio({
								boxLabel : '不显示',
								name : 'isShowInMyJob',
								inputValue : 'N'
							})
				}]
	};
	var form = new Ext.FormPanel({
		name : 'form',
		frame : true,
		autoWidth : 400,
		autoHeight : 350,
		align : 'center',
		layout : 'fit',
		items : [{
			xtype : 'fieldset',
			labelAlign : 'right',
			labelWidth : 160,
			autoHeight : true,
			defaultType : 'textfield',
			border : true,
			items : [{
						name : 'workflowType',
						fieldLabel : '工作流类型',
						width : 300,
						readOnly : true
					}, {
						name : 'version',
						width : 300,
						fieldLabel : '版本号',
						readOnly : true
					}, {
						name : 'name',
						width : 300,
						fieldLabel : '名称(*)',
						allowBlank : false
					}, {
						name : 'displayNo',
						xtype : 'numberfield',
						width : 300,
						fieldLabel : '排序'
					}, isRunning, isShowInMyJob, {
						name : 'flowListUrl',
						width : 300,
						fieldLabel : '审批列表地址'
					}, {
						name : 'desc',
						xtype : 'textarea',
						width : 300,
						fieldLabel : '说明'
					}, {
						name : 'tableName',
						width : 300,
						fieldLabel : '主表名'
					}, {
						name : 'wfIdColName',
						width : 300,
						fieldLabel : '实例号列名'
					}, {
						name : 'wfStatusColName',
						width : 300,
						fieldLabel : '状态列名'
					}, {
						name : 'tableKeyColName',
						width : 300,
						fieldLabel : '主键列名'
					}],
			buttons : [{
				text : '保存',
				iconCls : 'save',
				handler : function() {
					if (form.getForm().isValid()) {
						form.getForm().submit({
									method : 'POST',
									url : 'MAINTWorkflow.do?action=savewfbaseinfo',
									success : function(form, action) {
										win.hide();
									},
									failure : function(form, action) {
										// var o = eval("(" +
										// action.response.responseText + ")");
										// Ext.Msg.alert('错误', o.errMsg);
										alert('error');
									}
								});
					}
				}
			}, {
				text : '取消',
				iconCls : 'cancel',
				handler : function() {
					win.hide();
				}
			}]
		}]
	});

	// 弹出设置角色窗口
	var win = new Ext.Window({
				layout : 'fit',
				title : '工作流类型基本信息修改',
				width : 650,
				height : 430,
				modal : true,
				items : [form],
				closeAction : 'hide'
			});
	// 签字时的Enter
	document.onkeydown = function() {
		if (event.keyCode == 13 && document.getElementById('btnWorkFlow')) {
			document.getElementById('btnWorkFlow').click();
		}
	}
});