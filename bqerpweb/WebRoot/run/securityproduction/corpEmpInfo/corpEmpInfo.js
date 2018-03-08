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
	// 人员id
	var empId = new Ext.form.Hidden({
		id : 'empId',
		name : 'empId'
	})
	// 人员姓名
	var empName = new Ext.form.TextField({
		fieldLabel : '姓名',
		id : 'empName',
		name : 'empName',
		width : 340
	})
	
	//职责
	var empDuty = new Ext.form.TextArea({
		fieldLabel : '职责',
		id : 'empDuty',
		name : 'empDuty',
		width : 340
	})
	
	//修改人编码
	var modifyByH = new Ext.form.Hidden({
		id : 'modifyby',
		name : 'modifyby'
	})
	// 修改人姓名
	var modifyName = new Ext.form.TextField({
		fieldLabel : '修改人',
		readOnly : true,
		id : 'modifyName',
		name : 'modifyName',
		width : 99
	})
	//修改时间
	var modifyDateString = new Ext.form.TextField({
		fieldLabel : '修改时间',
		readOnly  : true,
		id : 'modifyDateString',
		name : 'modifyDateString',
		value : getDate(),
		width : 99
	})
	getWorkerCode();
	
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
	
	
	

	// 弹窗的表单对象
	var blockForm = new Ext.form.FormPanel({
				labelAlign : 'right',
				frame : true,
				labelWidth : 85,
				layout : 'column',
				items : [{

							bodyStyle : "padding:20px 0px 0px 0px",
							border : false,
							layout : 'form',
							columnWidth : 1,
							items : [empId,empName]
						}, {
							columnWidth : 1,
							layout : 'form',
							border : false,
							items : [empDuty]
						}, {
							columnWidth : .5,
							layout : 'form',
							border : false,
							items : [modifyByH,modifyName]
						}, {
							columnWidth : .5,
							layout : 'form',
							border : false,
							items : [modifyDateString]
						}]

			});

	// 左边的弹出窗体
	var blockAddWindow = new Ext.Window({
				title : '',
				layout : 'fit',
				width : 500,
				height : 220,
				modal : true,
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
							iconCls : 'save',
							handler : function() {
								var myurl = "";
	
								if(empName.getValue() == null || empName.getValue() == "")
								{
									Ext.Msg.alert('提示','姓名不能为空！');
									return;
								}
								if (!blockForm.getForm().isValid()) {
									return;
								}
								if (op == "add") {
									myurl = "security/addEmpInfo.action";
								} else if (op == 'edit') {
									myurl = "security/updateEmpInfo.action";
								} else {
									Ext.Msg.alert('错误', '出现未知错误.');
								}
								blockForm.getForm().submit({
											method : 'POST',
											url : myurl,
											success : function(form, action) {
												fuzzyQuery();
												blockAddWindow.hide();
												if (op == "add") {
													Ext.Msg.alert('提示','新增人员成功！');
												}
												 else if (op == 'edit') {
													Ext.Msg.alert('提示','修改人员成功！');
												}
												
												
											},
											faliue : function() {
												Ext.Msg.alert('错误', '出现未知错误.');
											}
										});

							}
						}, {
							text : '取消',
							iconCls : 'cancer',
							handler : function() {
								blockForm.getForm().reset();
								getWorkerCode();
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
					getWorkerCode();
					blockAddWindow.show();
					op = 'add';
					blockAddWindow.setTitle("发电公司人员新增");
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
								.getSelections();
						var recordslen = records.length;
						if (recordslen > 1) {
							Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
						} else {
							blockAddWindow.show();
							var record = westgrid.getSelectionModel()
									.getSelected();
							blockForm.getForm().reset();
							blockForm.form.loadRecord(record);

							op = 'edit';
							blockAddWindow.setTitle("发电公司人员修改");
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
							if (member.get("empId")) {
								ids.push(member.get("empId"));
							} else {

								westgrids.remove(westgrids.getAt(i));
							}
						}

						Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(
										buttonobj) {
									if (buttonobj == "yes") {
										Ext.lib.Ajax
												.request(
														'POST',
														'security/deleteEmpInfo.action',
														{
															success : function(
																	action) {			
																fuzzyQuery();
																Ext.Msg.alert('提示','删除人员成功！');
															},
															failure : function() {
																Ext.Msg
																		.alert(
																				'错误',
																				'删除时出现未知错误.');

															}
														}, 'ids=' + ids);
									}
								});

					}

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
				tbar : [query, querybtn, westbtnAdd, {
							xtype : "tbseparator"
						}, westbtnedit, {
							xtype : "tbseparator"
						}, westbtndel],
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
				Ext.get("btnUpdate").dom.click();
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