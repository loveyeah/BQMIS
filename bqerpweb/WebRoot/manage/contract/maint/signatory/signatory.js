Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
function viewSignFile(v) {
	if (v != "" && v.length > 0) {
		alert(v);
	}
}
Ext.onReady(function() {
	var methods = "";
	var signatoryFile = {
		name : "filePath",
		xtype : "textfield",
		fieldLabel : '签名',
		allowBlank : false,
		inputType : "file",
		height : 22
	}
	var form = new Ext.form.FormPanel({
		id : 'sign-form',
		el : 'form',
		defaultType : 'textfield',
		labelAlign : 'right',
		title : '员工签名维护',
		labelWidth : 120,
		frame : true,
		layout : 'form',
		autoWidth : true,
		autoHeight : true,
		waitMsgTarget : true,
		items : [{
			id : "signId",
			name : "sign.signId",
			xtype : 'hidden'
		}, {
			id : 'signatoryBy',
			name : 'sign.signatoryBy',
			xtype : 'trigger',
			allowBlank : false,
			readOnly : true,
			fieldLabel : '姓名<font color="red">*</font>',
			onTriggerClick : function(e) {
				var emp = window
						.showModalDialog("findEmpByDept.jsp", "",
								"dialogHeight:600px;dialogWidth:800px;status:no;scroll:yes;help:no");
				if (emp != null) {
					document.getElementById("workerCode").value = emp.codes;
					document.getElementById("sign.signatoryBy").value = emp.names;
				}
			}
		}, {
			id : "workerCode",
			name : "workerCode",
			fieldLabel : '工号',
			readOnly : true
		}, signatoryFile, {
			id : 'lastModifiedBy',
			name : 'sign.lastModifiedBy',
			readOnly : true,
			fieldLabel : '修改人'
		}, {
			id : 'lastModifiedDate',
			name : 'sign.lastModifiedDate',
			readOnly : true,
			fieldLabel : '修改时间'
		}],
		buttons : [{
			text : '保存',
			handler : function() {
				if (form.getForm().isValid()) {
					var _url = "";
					if (methods == "add")
						_url = 'managecontract/addSignatory.action'
					else
						_url = 'managecontract/updateSignatory.action'
					form.getForm().submit({
						url : _url,
						method : 'post',
						params : {
							filePath : Ext.get("filePath").dom.value
						},
						success : function(form, action) {
							signatoryDs.reload();
						},
						failure : function(form, action) {
							Ext.Msg.alert('提示信息', '未知错误！')
						}
					})
					win.hide();
				}
			}
		}, {
			text : '取消',
			handler : function() {
				win.hide();
			}
		}]
	});
	form.render();
	var signatory = Ext.data.Record.create([{
		name : 'signId'
	}, {
		name : 'signatoryBy'
	}, {
		name : 'signatoryFile'
	}, {
		name : 'workerCode'

	}]);
	var signatoryDs = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managecontract/findSignatoryList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : 'list'
		}, signatory)
	});
	/* 设置每一行的选择框 */
	var signatorySm = new Ext.grid.RowSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) {
			}
		}
	});

	signatoryDs.load();
	var signatoryCm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
		header : '序号',
		width : 50,
		align : 'left'
	}), {
		header : '工号',
		dataIndex : 'workerCode',
		align : 'left'
			// width : 50
			}, {
				header : '姓名',
				dataIndex : 'signatoryBy',
				align : 'left'
			// width : 150
			}, {
				header : '签名',
				dataIndex : 'signatoryFile',
				align : 'left',
				// width : 150,
				renderer : function(v) {
//					var st = v.substring(v.indexOf("#") + 1, v.length);
//					var _v = v.substring(0, v.indexOf("#"));
//					return ((st == "true")
//							? ''
//							: ("<div onclick=\"viewSignFile('" + _v + "');\">查看</div>"));
//					//
						return (v==null
							? ''
							: ("<div onclick=\"viewSignFile('" + v + "');\">查看</div>"));
				}
			}]);
	signatoryCm.defaultSortable = true;
	var bbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : signatoryDs,
		displayInfo : true,
		displayMsg : '共 {2} 条',
		emptyMsg : "没有记录"
	})
	var tbar = new Ext.Toolbar({
		items : [{
			id : 'btnAdd',
			iconCls : 'add',
			text : "增加",
			handler : function() {
				methods = "add";
				win.show();
				form.getForm().reset();
			}
		}, '-', {
			id : 'btnDelete',
			iconCls : 'delete',
			text : "删除",
			handler : function() {
				var rec = signatoryGrid.getSelectionModel().getSelections();
				if (rec.length > 0) {
					Ext.Msg.confirm('提示信息', '确定要删除选择的记录吗？', function(b) {
						if (b == "yes") {
							var signId = rec[0].data.signId;
							Ext.Ajax.request({
								url : 'managecontract/delSignatory.action?id='
										+ signId,
								method : 'post',
								success : function(result, form) {
									signatoryDs.reload();
								},
								failure : function(result, form) {
									Ext.msg.alert('提示信息', '未知错误！');
								}
							})
						}
					})
				} else {
					Ext.Msg.alert('提示信息', '请选择你要删除的行！');
				}
			}
		}, '-', {
			id : 'btnUpdate',
			iconCls : 'update',
			text : "修改",
			handler : function() {
				methods = "update";
				var rec = signatoryGrid.getSelectionModel().getSelections();
				if (rec.length > 0) {
					win.show();
					form.getForm().loadRecord(rec[0]);
				} else {
					Ext.Msg.alert('提示信息', '请选择你要修改的行！');
				}
			}
		}]
	})
	var signatoryGrid = new Ext.grid.EditorGridPanel({
		ds : signatoryDs,
		cm : signatoryCm,
		sm : signatorySm,
		bbar : bbar,
		tbar : tbar,
		autoWidth : true,
		fitToFrame : true,
		border : false,
		viewConfig : {
			forceFit : false
		}
	});
	signatoryDs.on('beforeload', function() {
		Ext.apply(this.baseParams);
	});
	signatoryGrid.on("dblclick", function() {
		Ext.get("btnUpdate").dom.click();
	});
	var win = new Ext.Window({
		el : 'window-win',
		width : 400,
		height : 250,
		closeAction : 'hide',
		modal : true,
		items : [form],
		buttons : []
	});
	var viewport = new Ext.Viewport({
		layout : 'fit',
		autoWidth : true,
		autoHeight : true,
		fitToFrame : true,
		items : [signatoryGrid]
	});
});