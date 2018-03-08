Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
			var args = window.dialogArguments;
			var dateType = args.dateType;
			var itemCode = args.itemCode;
			var itemName = args.itemName;
			var txtItemCode = new Ext.form.TextField({
						id : 'itemName',
						anchor : '70%',
						fieldLabel : '指标编码',
						value : itemName,
						readOnly : true,
						cls : 'disable'
					});
			var txtTableCode = new Ext.form.TextField({
						id : 'tableCode',
						anchor : '70%',
						readOnly : true,
						cls : 'disable',
						value : '001',
						fieldLabel : '表值编码'

					});
			var txtMaxTableValue = new Ext.form.NumberField({
						id : 'maxTableValue',
						allowBlank : true,
						allowDecimals :true,//add by wpzhu 20100707
						decimalPrecision :4,
						anchor : '70%',
						fieldLabel : '表码最大值'
					});
			var txtMultiple = new Ext.form.NumberField({
						id : 'multiple',
						allowBlank : false,
						anchor : '70%',
						fieldLabel : '倍率'
					});
			var txtStartValue = new Ext.form.NumberField({
						id : 'startValue',
						allowBlank : true,
						allowDecimals :true,
						decimalPrecision :4,
						anchor : '70%',
						fieldLabel : '初始值'
					});
			var txtEndValue = new Ext.form.NumberField({
						id : 'endValue',
						allowBlank : true,
						allowDecimals :true,
						decimalPrecision :4,
						anchor : '70%',
						fieldLabel : '终止值'
					});
			var txtFixDate = new Ext.form.TextField({
						id : 'fixDate',
						anchor : '50%',
						allowBlank : false,
						fieldLabel : '安装日期',
						listeners : {
							focus : function() {
								WdatePicker({
											startDate : '%y-%M-%d',
											dateFmt : 'yyyy-MM-dd',
											alwaysUseStartDate : true
										});
							}
						}

					});
			var txtEndDate = new Ext.form.TextField({
						id : 'endDate',
						anchor : '50%',
						allowBlank : true,
						fieldLabel : '终止日期',
						listeners : {
							focus : function() {
								WdatePicker({
											startDate : '%y-%M-%d',
											dateFmt : 'yyyy-MM-dd',
											alwaysUseStartDate : true
										});
							}
						}

					});
			var cbIsUse = new Ext.form.ComboBox({
						store : new Ext.data.SimpleStore({
									fields : ["retrunValue", "displayText"],
									data : [['1', '是'], ['0', '否']]
								}),
						fieldLabel : '是否使用',
						valueField : "retrunValue",
						displayField : "displayText",
						hiddenName : 'ifUsed',
						mode : 'local',
						value : '1',
						forceSelection : true,
						editable : false,
						triggerAction : 'all',
						selectOnFocus : true,
						allowBlank : false,
						anchor : '70%'
					});

			var form = new Ext.FormPanel({
						layout : 'form',
						bodyStyle : "padding:5px 5px 5px 5px",
						autoHeight : true,
						labelAlign : 'right',
						items : [new Ext.form.FieldSet({
									title : '倍率设置',
									labelWidth : 200,
									height : '100%',
									layout : 'form',
									items : [txtItemCode, txtTableCode,
											txtFixDate, txtEndDate,
											txtMaxTableValue, txtMultiple,
											txtStartValue, txtEndValue, cbIsUse]
								})],
						buttons : [new Ext.Button({
									id : 'btnSave',
									iconCls : 'add',
									text : '保存',
									handler : function() {
										if (!form.getForm().isValid())
											return;
										Ext.Msg.wait("正在保存信息,请等待...");
										form.getForm().submit({
											method : 'post',
											url : 'manager/saveBpCMetricTable.action',
											params : {
												'itemCode' : itemCode
											},
											success : function(form, action) {
												var o = eval("("
														+ action.response.responseText
														+ ")");
												Ext.Msg.alert('提示', '操作成功!');
											},
											faliue : function() {
												Ext.Msg.alert('提示', '保存失败');
											}
										});
									}
								}), new Ext.Button({
									id : 'btnClose',
									iconCls : 'cancer',
									text : '关闭',
									handler : function() {
										window.close();
									}
								})]
					});
			var view = new Ext.Viewport({
						items : [form]
					});

			Ext.Ajax.request({
						url : 'manager/getBpCMetricTable.action',
						method : 'post',
						params : {
							itemCode : itemCode
						},
						waitMsg : '正在加载数据...',
						success : function(result, request) {
							var rec = eval('(' + result.responseText + ')');
							var o = new Object();
							o.data = rec;
							form.getForm().loadRecord(o);
						}
					})

		});