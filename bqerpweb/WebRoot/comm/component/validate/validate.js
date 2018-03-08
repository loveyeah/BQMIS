Ext.namespace('Power.validate');
Power.validate = function(config) {
	var returnValue = null;

	var fs = new Ext.form.FieldSet({
		title : '请输入用户名与密码',
		height : '100%',
		layout : 'form',
		items : [{
			name : "plantCode",
			xtype : 'hidden',
			value : 'hfdc',
			width : 180
		}, {
			name : 'workerCode',
			fieldLabel : '用户名',
			allowBlank : false,
			xtype : 'textfield',
			width : 180
		}, {
			fieldLabel : '密码',
			allowBlank : false,
			name : "password",
			xtype : 'textfield',
			inputType : 'password',
			width : 180
		}],
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
				if (form.getForm().isValid()) {
					form.getForm().submit({
						url : 'system/validateUser.action',
						methods : 'post',
						success : function(form, action) {
							var o = eval('(' + action.response.responseText
									+ ')');
							returnValue = o;
							win.hide();
						},
						failure : function(form, action) {
							returnValue = null;
							Ext.Msg.alert("提示", "用户名或密码不正确！");
						}
					});
				}
			}
		}, {
			text : '取消',
			iconCls : 'canse',
			handler : function() {
				returnValue = null;
				win.hide();
			}
		}]
	});

	var form = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		id : 'validate-form',
		labelWidth : 50,
		autoHeight : true,
		region : 'center',
		border : false,
		items : [fs]
	});

	var win = new Ext.Window({
		title : '身份验证',
		modal:true,
		autoHeight : true,
		width : 300,
		closeAction : 'hide',
		items : [form]
	})
	return {
		win : win,
		getValue : function() {
			return returnValue;
		}
	}
}