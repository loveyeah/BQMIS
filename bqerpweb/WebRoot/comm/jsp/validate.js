Ext.onReady(function() {
			var fs = new Ext.form.FieldSet({
						title : '请输入用户名与密码',
						// height : '30%',
						// width : '30%',
						layout : 'form',
						Align : 'center',
						items : [{
									name : "plantCode",
									xtype : 'hidden',
									value : 'hfdc',
									width : 200
								}, {
									name : 'workerCode',
									fieldLabel : '用户名',
									xtype : 'textfield',
									width : 200
								}, {
									fieldLabel : '密码',
									name : "password",
									xtype : 'textfield',
									inputType : 'password',
									width : 200
								}],
						buttons : [{
							text : '确定',
							handler : function() {
								form.getForm().submit({
									url : 'system/validateUser.action',
									methods : 'post',
									success : function(form, action) {
										var o = eval('('
												+ action.response.responseText
												+ ')');
										window.returnValue = o;
										window.close();
									},
									failure : function(form, action) {
										alert("用户名或密码输入不正确！");
									}
								});
							}
						}, {
							text : '取消',
							iconCls : 'canse',
							handler : function() {
								window.close();
							}
						}]
					});
			var form = new Ext.FormPanel({
						id : 'validate-form',
						// frame : true,
						// labelAlign : 'center',
						// width : '100%',
						// height : '100%',
						// bodyStyle : 'padding:30px',
						labelWidth : 50,
						frame : true,
						bodyStyle : "padding:5px 5px 0",
						labelAlign : 'right',
						width : 350,
						items : [fs]
					})
			var viewport = new Ext.Viewport({
						layout : 'fit',
						items : [{
									region : 'center',
									layout : 'fit',
									width : 350,
									// height : 400,
									id : 'center-panel',
									items : [form]
								}]
					});
		})