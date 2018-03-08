Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var mantype = "";
	var method = "add";
	var pageSize = 18;
	// 已存在的人员
	str = "";



	var itemName = {
		header : "指标名称",
		sortable : false,
		dataIndex : 'itemName',
		editor : new Ext.form.TextField({
					readOnly : true
				})
	}


	var absStore = new Ext.data.JsonStore({
				url : 'managexam/getStandardList.action',
				root : 'list',
				totalProperty : 'totalCount'
			});


	var westbtnAdd = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : function() {
					westStore.load({
								params : {
									start : 0,
									limit : pageSize
								}
							});
				}
			});

	var westSm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});

	var westStorelist = new Ext.data.Record.create([westSm, {
				name : 'afInfo.affiliatedId'
			}, {
				name : 'afInfo.affiliatedLevel'
			}, {
				name : 'afInfo.affiliatedValue'
			}, {
				name : 'afInfo.depCode'
			}, {
				name : 'afInfo.itemId'
			}, {
				name : 'itemname'
			}, {
				name : 'deptname'
			},{
				name : 'ifBranchItem'
			}]);

	var westStore = new Ext.data.JsonStore({
				url : 'managexam/getStandardList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : westStorelist
			});

	function getlevelName(v) {
		if (v == "1") {
			v = "牵头部门";
		} else if (v == "2") {
			v = "生产面责任部门";
		} else if (v == "3") {
			v = "非生产面责任部门";
		} else if (v == "4") {
			v = "非生产面责任部门（多经公司）";
		}else {
			v = "";
		}
		return v;
	};

	// 左侧Grid
	var westgrid = new Ext.grid.GridPanel({
				store : westStore,
				columns : [new Ext.grid.RowNumberer(), {
							header : "指标名称",
							width : 100,
							sortable : false,
							dataIndex : 'itemname'
						},{
							header : "挂靠级别",
							width : 150,
							sortable : false,
							dataIndex : 'afInfo.affiliatedLevel',
							renderer : getlevelName
						}, {
							header : "指标所属部门",
							width : 100,
							sortable : false,
							dataIndex : 'deptname'
						}, {
							header : "挂靠标准",
							width : 100,
							sortable : false,
							dataIndex : 'afInfo.affiliatedValue'
						}, {
							header : "ifBranchItem",
							width : 100,
							sortable : false,
							hidden : true,
							dataIndex : 'ifBranchItem'
						}],
				viewConfig : {
					forceFit : true
				},
				sm : westSm,
				bbar : new Ext.PagingToolbar({
							pageSize : pageSize,
							store : westStore,
							displayInfo : true,
							displayMsg : "{2}条记录",
							emptyMsg : "没有记录"
						}),
				frame : true,
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});

	westgrid.on('rowclick', function(grid, rowIndex, e) {
		var rec = westgrid.getSelectionModel().getSelected();
		var deptName = rec.get("deptname")
		//----------add by drdu 091201--------------
		var ifBranchItemCheck;
		ifBranchItemCheck = grid.getStore().getAt(rowIndex).get("ifBranchItem");
//		if (ifBranchItemCheck == 'Y') {
	   //--------------------------------------------		
			Ext.Ajax.request({
				url : 'managexam/getStandardInfo.action',
				params : {
					aid : grid.getStore().getAt(rowIndex)
							.get("afInfo.affiliatedId")
				},
				method : 'post',
				waitMsg : '正在加载数据...',
				success : function(result, request) {
					var o = eval('(' + result.responseText + ')');
//					alert(result.responseText)
					affiliatedId.setValue(grid.getStore().getAt(rowIndex)
							.get("afInfo.affiliatedId"));
					// memo.setValue(o[0] != null ? o[0] : "");
					itemId.setValue(o[1] != null ? o[1] : "");
					itemcode.setValue(o[2] != null ? o[2] : "");
					itemname.setValue(o[3] != null ? o[3] : "");
					deptcode.setValue(o[4] != null ? o[4] : "");
					itemdept.setValue(deptName);
					aflevel.setValue(o[6] != null ? o[6] : "");
					afvalue.setValue(o[7] != null ? o[7] : "");
					viewnumber.setValue(o[8] != null ? o[8] : "");
					method = "update";
					eastbtnSav.setText("修改");
					absStore.load({
						params : {
							awardId : grid.getStore().getAt(rowIndex)
									.get("awardInfo.awardId")
						}
					});
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
				}
			});
//		} else {//add by drdu 091201
//			Ext.Msg.alert("提示", "该指标不属于分公司指标，不参与奖金申报!");
//		}
	});

	westStore.on('beforeload', function() {
				Ext.apply(this.baseParams, {
						});
			});

	westStore.load({
				params : {
					start : 0,
					limit : pageSize
				}
			});

	var eastbtnAdd = new Ext.Button({
				text : '新增',
				iconCls : 'add',
				handler : function() {
					eastform.getForm().reset();
					method = "add";
					eastbtnSav.setText("保存");
					absStore.removeAll();
				}
			});

	var eastbtnDel = new Ext.Button({
				text : '删除',
				iconCls : 'delete',
				handler : function() {
					var rec = westgrid.getSelectionModel().getSelected();
					if(rec)
					{  
						Ext.Msg.confirm('提示', '删除后您将不能恢复!确定要删除吗?', function(response) {
					    if (response == 'yes') {
						Ext.Ajax.request({
							method:'post',
							url:'managexam/deleteStandard.action',
							params:{
								'affiliatedId' : rec.get("afInfo.affiliatedId"),
								start : 0,
								limit : pageSize
							},
							callback:function(options,success,response)
							{
								eastform.getForm().reset();
							 	westgrid.getStore().reload();
							 	method = "add";
								eastbtnSav.setText("保存");
							}
						}); 
						}
			        	});
					} 

				}
			});

	var eastbtnSav = new Ext.Button({
				text : '保存',
				iconCls : 'save',
				handler : function() {
					if (eastform.getForm().isValid()) {
						 var rec1 = westgrid.getSelectionModel().getSelected();
						eastform.getForm().submit({
							method : 'post',
							url : 'managexam/saveStandard.action',
							params : {
								method : method,
								'id' : rec1?rec1.get("afInfo.affiliatedId"):''
							},
							success : function(form, action) {
							if (method == 'add') {
								westStore.load({
											params : {
												start : 0,
												limit : pageSize
											}
										});
								eastform.getForm().reset();
								method = 'update';
								Ext.Msg.alert('提示', '数据保存成功！')
								eastbtnSav.setText("保存");
								absStore.removeAll();
							}else if (method == 'update') {
							      itemId.setValue(rec1.get("afInfo.itemId"));
							      deptcode.setValue(rec1.get("afInfo.depCode"));
							      westStore.load({
											params : {
												start : 0,
												limit : pageSize
												
											}
										});
								  eastform.getForm().reset();
								  Ext.Msg.alert('提示', '数据修改成功！')
								  eastbtnSav.setText("保存");
								  absStore.removeAll();
							 
							 }
							},
							failure : function(form, action) {
							 
								var o = eval('(' + action.response.responseText
										+ ')');
								Ext.MessageBox.alert('错误', o.msg);
							}
						})
					}
				}
			});

	var affiliatedId = new Ext.form.Hidden({
				id : 'afInfo.affiliatedId',
				name : 'afInfo.affiliatedId'
			});


	function selectPersonWin() {
		var args = {
			selectModel : 'signal',
			notIn : "",
			rootNode : {
				id : '-1',
				text : '灞桥电厂'
			}
		}
		var person = window
				.showModalDialog(
						'selectItem.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

		if (typeof(person) != "undefined") {

			    itemId.setValue(person.id);
				itemcode.setValue(person.code);
				itemname.setValue(person.name);
		}
	}

	function selectDept() {
		var args = {
			selectModel : 'multiple',
			rootNode : {
				id : '0',
				text : '灞桥电厂'
			},
			onlyLeaf : false
		};
		var dept = window
				.showModalDialog(
						'../../../../comm/jsp/hr/dept/dept.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(dept) != "undefined") {
			deptcode.setValue(dept.codes);
			itemdept.setValue(dept.names);
			
		}
	}

	var itemcode = new Ext.form.ComboBox({
				id : 'itemCode',
				name : 'itemCode',
				fieldLabel : "指标编码",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				anchor : '90%',
				onTriggerClick : function() {
					selectPersonWin();
				}
			});
			
	var itemId = new Ext.form.Hidden({
				id : 'afInfo.itemId',
				name : 'itemId'
			});

	var itemname = new Ext.form.TextField({
				name : 'itemName',
				xtype : 'textfield',
				readOnly : true,
				fieldLabel : '指标名称',
				anchor : '90%',
				allowBlank : false
			});

	var itemdept = new Ext.form.ComboBox({
				id : 'depName',
				name : 'depName',
				fieldLabel : "所属部门",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				anchor : '90%',
				onTriggerClick : function() {
					selectDept();
				}
			});

	var deptcode = new Ext.form.Hidden({
				id : 'afInfo.depCode',
				name : 'deptcode'
			});

	var aflevel_data = [["牵头部门", "1"], ["生产面责任部门", "2"],
			["非生产面责任部门", "3"], ["非生产面责任部门（多经公司）", "4"]];

	var aflevel = new Ext.form.ComboBox({
				id : "affiliatedLevel",
				xtype : "combo",
				name : 'affiliatedLevel',
				allowBlank : true,
				triggerAction : 'all',
				store : new Ext.data.SimpleStore({
							fields : ['name', 'id'],
							data : aflevel_data
						}),
				hiddenName : 'afInfo.affiliatedLevel',
				displayField : 'name',
				valueField : 'id',
				fieldLabel : "挂靠级别",
				mode : 'local',
				emptyText : '请选择...',
				blankText : '请选择',
				anchor : '90%',
				allowBlank : false,
				readOnly : true
			});

	var afvalue = new Ext.form.NumberField({
				name : 'afInfo.affiliatedValue',
				xtype : 'numberfield',
				readOnly : false,
				fieldLabel : '挂靠标准',
				anchor : '90%',
				allowBlank : false
			});
	var viewnumber = new Ext.form.NumberField({
				name : 'afInfo.viewNo',
				xtype : 'numberfield',
				readOnly : false,
				fieldLabel : '显示顺序',
				anchor : '90%',
				allowBlank : false
			});

	var formContent = new Ext.form.FieldSet({
				border : false,
				layout : 'column',
				items : [{
							border : false,
							layout : 'form',
							columnWidth : 1,
							labelWidth : 70,
							items : [itemcode,itemId]
						}, {
							border : false,
							layout : 'form',
							columnWidth : 1,
							labelWidth : 70,
							items : [itemname]
						}, {
							border : false,
							layout : 'form',
							columnWidth : 1,
							labelWidth : 70,
							items : [aflevel]
						}, {
							border : false,
							layout : 'form',
							columnWidth : 1,
							labelWidth : 70,
							items : [itemdept,deptcode]
						}, {
							border : false,
							layout : 'form',
							columnWidth : 1,
							labelWidth : 70,
							items : [afvalue]
						},{
							border : false,
							layout : 'form',
							columnWidth : 1,
							labelWidth : 70,
							items : [viewnumber]
						}]
			});

	// 右侧Form
	var eastform = new Ext.form.FormPanel({
				bodyStyle : "padding:5px 5px 0",
				labelAlign : 'right',
				labelWidth : 70,
				region : 'center',
				border : false,
				tbar : [eastbtnAdd, {
							xtype : "tbseparator"
						}, eastbtnDel, {
							xtype : "tbseparator"
						}, eastbtnSav],
				items : [formContent]
			});

	// ↑↑主窗口部件

	var fieldSet = new Ext.Panel({
				layout : 'border',
				border : false,
				items : [{
							region : 'north',
							split : true,
							collapsible : true,
							titleCollapse : true,
							margins : '1',
							height : 300,
							layoutConfig : {
								animate : true
							},
							layout : 'fit',
							items : [eastform]
						}]
			});

	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							title : '标准列表',
							region : 'west',
							split : true,
							collapsible : true,
							titleCollapse : true,
							margins : '1',
							width : 520,
							layoutConfig : {
								animate : true
							},
							layout : 'fit',
							items : [westgrid]
						}, {
							title : '标准信息',
							region : "center",
							split : true,
							collapsible : false,
							titleCollapse : true,
							margins : '1',
							layoutConfig : {
								animate : true
							},
							layout : 'fit',
							items : [eastform]
						}]
			}); 

});