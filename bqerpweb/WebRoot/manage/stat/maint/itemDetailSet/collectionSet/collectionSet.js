Ext.onReady(function() {
	var args = window.dialogArguments;
	var dateType = args.dateType;
	var itemCode = args.itemCode;
	var itemName = args.itemName;
	var isCo;
	function initData() {
		Ext.Ajax.request({
			url : 'manager/findCorrespondByItem.action',
			method : 'post',
			params : {
				itemCode : itemCode
			},
			success : function(result, request) {
				var o = eval('(' + result.responseText + ')');
				if (o != null) {
					Ext.getCmp("txtNodeCode").setValue(o.nodeCode);
					Ext.getCmp("txtNodeName").setValue(o.nodeName);
					Ext.getCmp("txtNodeDesc").setValue(o.descriptor);
					Ext.getCmp("txtNodeApartCode").setValue(o.apartCode);
					Ext.getCmp("txtNodeApartName").setValue(o.apartName);
					isCo = true;
				} else {
					isCo = false;
				}
			},
			failure : function(result, request) {
				Ext.MessageBox.alert('提示信息', '错误,请联系管理员！')
			}
		})
	}
	var form = new Ext.FormPanel({
		bodyStyle : "padding:5px 5px 5px 5px",
		labelAlign : 'right',
		autoHeight : true,
		border : false,
		items : [new Ext.form.FieldSet({
			title : '采集点设置',
			labelWidth : 100,
			height : '100%',
			layout : 'form',
			items : [{
				id : 'txtItemCode',
				xtype : 'textfield',
				readOnly : true,
				value : itemCode,
				cls : 'disable',
				fieldLabel : '指标编码',
				anchor : '80%'
			}, {
				id : 'txtItemName',
				xtype : 'textfield',
				readOnly : true,
				value : itemName,
				fieldLabel : '指标名称',
				cls : 'disable',
				anchor : '80%'
			}, {
				border : false,
				anchor : '80%',
				layout : "column",
				items : [{
					layout : 'form',
					isFormField : true,
					border : false,
					columnWidth : .905,
					items : {
						id : 'txtNodeCode',
						xtype : 'textfield',
						readOnly : true,
						style:'cursor:hand;',
						fieldLabel : '采集点编码',
						emptyText : '点击选择采集点', 
						anchor : '99%'
					}
				}, {
					layout : 'form',
					border : false,
					items : [{
						xtype : 'button',
						text : "取消对应",
						iconCls : 'delete',
						handler : function() {
							if (isCo == false) {
								Ext.MessageBox.alert('提示', '该指标尚未对应采集点！')
							} else {
								Ext.Ajax.request({
									url : 'manager/cancelCorrespond.action',
									method : 'post',
									params : {
										itemCode : itemCode
									},
									success : function(result, request) {
										var o = eval('(' + result.responseText
												+ ')');
										Ext.getCmp("txtNodeCode").setValue("");
										Ext.getCmp("txtNodeName").setValue("");
										Ext.getCmp("txtNodeDesc").setValue("");
										Ext.getCmp("txtNodeApartCode").setValue(""); 
										Ext.getCmp("txtNodeApartName").setValue("");
										isCo = false;
										Ext.MessageBox.alert('提示', "取消对应成功");
									},
									failure : function(result, request) {
										Ext.MessageBox
												.alert('提示', '错误，请联系管理员！')
									}
								})
							}
						}
					}]
				}]
			}, {
				id : 'txtNodeDesc',
				xtype : 'textfield',
				readOnly : true,
				fieldLabel : '采集点描述',
				anchor : '80%'
			}, {
				id : 'txtNodeName',
				xtype : 'textfield',
				readOnly : true,
				fieldLabel : '采集点名称',
				anchor : '80%'
			}, {
				id : 'txtNodeApartCode',
				xtype : 'hidden'
			}, {
				id : 'txtNodeApartName',
				xtype : 'textfield',
				readOnly : true,
				fieldLabel : '所属机组',
				anchor : '80%'
			} 
			]
		})],
		buttons : [{
			id : 'btnSave',
			text : '保存',
			iconCls : 'save',
			handler : function() {
				nodeCode = Ext.getCmp("txtNodeCode").getValue();
				apartCode = Ext.getCmp("txtNodeApartCode").getValue();  
				if (nodeCode == null || nodeCode == "") {
					Ext.Msg.alert('提示', '请选择采集点！');
					return false;
				}  
					Ext.Ajax.request({
						url : 'manager/generateCorrespond.action',
						method : 'post',
						params : {
							nodeCode : nodeCode,
							itemCode : itemCode,
							apartCode : apartCode
						},
						success : function(result, request) {
							var o = eval('(' + result.responseText + ')');
							isCo = true;
							Ext.MessageBox.alert('提示信息', "对应成功");
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('提示信息', '错误,请联系管理员！')
						}
					}) 
			}
		}, {
			id : 'btnClose',
			text : '关闭',
			iconCls : 'cancer',
			handler : function() {
				window.close();
			}
		}]
	});
	var view = new Ext.Viewport({
		layout : 'fit',
		border : false,
		items : [form]
	});
	initData();
	Ext.getCmp("txtNodeCode").getEl().on("click", function() {
		var url = "../../../../system/selCollectNode/selCollectNode.jsp";
		var res = window
				.showModalDialog(
						url,'',
						'dialogWidth:650px;dialogHeight:450px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(res) != "undefined") {
			Ext.getCmp("txtNodeCode").setValue(res.nodeCode);
			Ext.getCmp("txtNodeName").setValue(res.nodeName);
			Ext.getCmp("txtNodeDesc").setValue(res.descriptor);
			Ext.getCmp("txtNodeApartCode").setValue(res.apartCode); 
			Ext.getCmp("txtNodeApartName").setValue(res.blockName);
		}
	});
});
