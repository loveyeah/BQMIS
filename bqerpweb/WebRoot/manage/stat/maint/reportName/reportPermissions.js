Ext.namespace('Power.reportPermissions');

Power.reportPermissions = function(config) {
	
	var reportCode = config.reportCode;
	
	var atnSm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});

	var atnStorelist = new Ext.data.Record.create([atnSm, {
				name : 'model.id'
			}, {
				name : 'model.code'
			}, {
				name : 'model.workerCode'
			}, {
				name : 'workerName'
			}]);
	var atnStore = new Ext.data.JsonStore({
				url : 'comm/getUsersList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : atnStorelist
			})
	// modified by liuyi 20100531
	// add by bjxu 091103
	atnStore.on('beforeload', function() {
		// Ext.apply(this.baseParams, {
		// start : 0,
		// limit : 18
		// });
		Ext.apply(this.baseParams, {
			code : reportCode

		});
	});
//	atnStore.load({
//				params : {
//					code : reportCode
//				}
//			});
	atnStore.load({
		params : {
			start : 0,
			limit : 18
		}
	});
	var str="";
	// 参加人员
	var atnManName = {
		header : "人员",
		sortable : false,
		dataIndex : 'workerName',
		width : 50,
		editor : new Ext.form.TextField({
					readOnly : true
				})
	};
	var code = new Ext.form.TextField({
				dataIndex : 'model.code',
				hidden : true
			});

	var id = new Ext.form.TextField({
				dataIndex : 'model.id',
				hidden : true
			});

	var workerCode = {
		header : "工号",
		width : 100,
		sortable : false,
		dataIndex : 'model.workerCode',
		width : 135
	};
	function addAtnRecords() {
	
//		for (var i = 0; i < atnStore.getCount()(); i++) {
//			if (atnStore.getAt(i).get("model.workerCode") != ""
//					&& atnStore.getAt(i).get("model.workerCode") != null)
//				str += "'" + atnStore.getAt(i).get("model.workerCode") + "',";
//		}
//		str = str.substring(0, str.length - 1);
//		
		var args = {
			selectModel : 'multiple',//modify by wpzhu  20100824
			notIn : str
		}
		var person = window
				.showModalDialog(
						'../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(person) != "undefined") {
			
			var codes=person.codes;
			var ids=codes.split(",");
			var  names=person.names;
			var name=names.split(",");
			for(i=0;i<person.list.length;i++)
			{
			var count = atnStore.getCount();
			var currentIndex = count;
			var o = new atnStorelist({
						'model.id' : '',
						'model.code' : '',
						'model.workerCode' : '',
						'workerName' : ''
					});
			userManlistGrid.stopEditing();
			atnStore.insert(currentIndex, o);
			atnSm.selectRow(currentIndex);
			o.set("model.id", null);
			o.set("model.code", reportCode);
			o.set("model.workerCode", ids[i]);
			o.set("workerName", name[i]);
			userManlistGrid.startEditing(currentIndex, 2);
		 }
		}
	};

	var atnBtnAdd = new Ext.Button({
				text : '增加',
				iconCls : 'add',
				handler : function() {
					addAtnRecords();
				}
			});
	var ids = new Array();
	var atnBtnDel = new Ext.Button({
				text : '删除',
				iconCls : 'delete',
				handler : function() {
					userManlistGrid.stopEditing();
					var atnSm = userManlistGrid.getSelectionModel();
					var selected = atnSm.getSelections();
					if (selected.length == 0) {
						Ext.Msg.alert("提示", "请选择要删除的记录！");
					} else {
						for (var i = 0; i < selected.length; i += 1) {
							var member = selected[i];
							if (member.get("model.id") != null) {
								ids.push(member.get("model.id"));
							}
							userManlistGrid.getStore().remove(member);
							userManlistGrid.getStore().getModifiedRecords()
									.remove(member);
						}
					}
				}
			});
	var atnBtnSav = new Ext.Button({
		text : '保存',
		iconCls : 'save',
		handler : function() {
			userManlistGrid.stopEditing();
			var modifyRec = userManlistGrid.getStore().getModifiedRecords();
			if (modifyRec.length > 0 || ids.length > 0) {
				Ext.Msg.confirm('提示信息', '是否确定修改？', function(button, text) {
					if (button == 'yes') {
						var updateData = new Array();
						for (var i = 0; i < modifyRec.length; i++) {
							updateData.push(modifyRec[i].data);
						}
						Ext.Ajax.request({
									url : 'comm/saveRoleUsers.action',
									method : 'post',
									params : {
										isUpdate : Ext.util.JSON
												.encode(updateData),
										isDelete : ids.join(","),
										id : reportCode
									},
									success : function(form, options) {
                                   
									var obj = Ext.util.JSON
											.decode(form.responseText)
									if (obj && obj.msg) {
										Ext.Msg.alert('提示', obj.msg);
										if (obj.msg.indexOf('已经存在') != -1)
										{
										atnStore.rejectChanges();
									    atnStore.reload();
									    return;
										}else {
											
											atnStore.rejectChanges();
									        atnStore.reload();
											
										}
											
									}
									ids = [];
									atnStore.rejectChanges();
									atnStore.reload();
								},	
									failure : function(result, request) {
										Ext.MessageBox.alert('提示信息', '操作失败！')
										atnStore.rejectChanges();
									    atnStore.reload();
									}
								})
									/*success : function(result, request) {
										var o = eval('(' + result.responseText
												+ ')');
										Ext.MessageBox.alert('提示信息', o.msg);
										atnStore.rejectChanges();
										ids = [];
										// modified by liuyi 20100531 
//										atnStore.load({
//													params : {
//														code :reportCode
//													}
//												});
										atnStore.reload();
									},
									failure : function(result, request) {
										Ext.MessageBox.alert('提示信息', '未知错误！')
									}*/
								
					}
				});
			} else {
				Ext.MessageBox.alert('提示信息', '没有做任何修改！')
			}
		}
	});
	// 参加人员Grid
	var userManlistGrid = new Ext.grid.EditorGridPanel({
		store : atnStore,
		columns : [new Ext.grid.RowNumberer(), atnManName, code, id, workerCode],
		viewConfig : {
			forceFit : true
		},
		tbar : [atnBtnAdd, atnBtnDel, atnBtnSav],
		sm : atnSm,
		frame : true,
		enableColumnHide : false,
		enableColumnMove : false,
		iconCls : 'icon-grid',
		bbar : new Ext.PagingToolbar({
					pageSize : 18,
					store : atnStore,
					displayInfo : true,
					displayMsg : '共 {2} 条',
					emptyMsg : "没有记录"
				})
	});
	var atnManlistWindow = new Ext.Window({
				title : '人员名单',
				layout : 'fit',
				width : 400,
				height : 450,
				closable : true,
				border : false,
				resizable : false,
				closeAction : 'hide',
				modal : true,
				plain : true,
				buttonAlign : 'center',
				items : [userManlistGrid]
			});

	return {
		win : atnManlistWindow,
		atnStore : atnStore,
		grid : userManlistGrid
	}
}