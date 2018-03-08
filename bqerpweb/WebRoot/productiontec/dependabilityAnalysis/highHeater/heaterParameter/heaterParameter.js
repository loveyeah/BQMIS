Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
			var pId = parent.flagId;
			// alert(pId)
			// 辅机ID
			var auxiliaryId = new Ext.form.Hidden({
						id : 'auxiliaryId',
						name : 'phParams.auxiliaryId'
					})
			// 高压参数id
			var heaterId = new Ext.form.Hidden({
						id : 'heaterId',
						name : 'phParams.heaterId'
					})

			// 加热面积
			var heatArea = new Ext.form.NumberField({
						id : 'heatArea',
						name : 'phParams.heatArea',
						fieldLabel : '加热面积(m²)',
						decimalPrecision : 2,
						allowNegative : false,
						anchor : '100%'
					})
			// 进口汽温
			var importAirTpr = new Ext.form.NumberField({
						id : 'importAirTpr',
						name : 'phParams.importAirTpr',
						fieldLabel : '进口汽温(℃)',
						decimalPrecision : 2,
						allowNegative : false,
						anchor : '100%'
					})

			// 进口水温
			var importWaterTpr = new Ext.form.NumberField({
						id : 'importWaterTpr',
						name : 'phParams.importWaterTpr',
						fieldLabel : '进口水温(℃)',
						decimalPrecision : 2,
						allowNegative : false,
						anchor : '100%'
					})

			// 出口水温
			var exportWaterTpr = new Ext.form.NumberField({
						id : 'exportWaterTpr',
						name : 'phParams.exportWaterTpr',
						fieldLabel : '出口水温(℃)',
						decimalPrecision : 2,
						allowNegative : false,
						anchor : '100%'
					})

			// 水测流量
			var waterFlux = new Ext.form.NumberField({
						id : 'waterFlux',
						name : 'phParams.waterFlux',
						fieldLabel : '水测流量(t/h)',
						decimalPrecision : 2,
						allowNegative : false,
						anchor : '100%'
					})

			// 汽测流量
			var airFlux = new Ext.form.NumberField({
						id : 'airFlux',
						name : 'phParams.airFlux',
						fieldLabel : '汽测流量(t/h)',
						decimalPrecision : 2,
						allowNegative : false,
						anchor : '100%'
					})

			// 工作水压
			var workWaterPressure = new Ext.form.NumberField({
						id : 'workWaterPressure',
						name : 'phParams.workWaterPressure',
						fieldLabel : '工作水压(Mpa)',
						decimalPrecision : 4,
						allowNegative : false,
						anchor : '100%'
					})

			// 工作汽压
			var workAirPressure = new Ext.form.NumberField({
						id : 'workAirPressure',
						name : 'phParams.workAirPressure',
						fieldLabel : '工作汽压(Mpa)',
						decimalPrecision : 4,
						allowNegative : false,
						anchor : '100%'
					})

			// 旁路型式
			var bypassType = new Ext.form.TextField({
						id : 'bypassType',
						name : 'phParams.bypassType',
						fieldLabel : '旁路型式',
						anchor : '100%'
					})

			// 备注
			var memo = new Ext.form.TextField({
						id : 'memo',
						// store : [],
						name : 'phParams.memo',
						fieldLabel : '备注',
						anchor : '100%'
					})

			// 原编码
			var oldCode = new Ext.form.TextField({
						id : 'oldCode',
						name : 'phParams.oldCode',
						fieldLabel : '原编码',
						anchor : '100%'
					})

			// 布置方式
			var layType = new Ext.form.TextField({
						id : 'layType',
						name : 'phParams.layType',
						fieldLabel : '布置方式',
						anchor : '100%'
					})
			var set1 = new Ext.form.FieldSet({
		         id : 'set1',
		         frame : true,
		         border : true,
		         layout : 'column',
		         title : '参数',
		         draggable : false,
		         collapsible : true,
		         items : [{
							layout : 'form',
							columnWidth : .5,
							border : false,
							items : [auxiliaryId, heaterId, heatArea,
									importAirTpr]
						}, {
							layout : 'form',
							columnWidth : .5,
							border : false,
							items : [exportWaterTpr, importWaterTpr]
						}, {
							layout : 'form',
							columnWidth : .5,
							border : false,
							items : [airFlux, waterFlux]
						}, {
							layout : 'form',
							columnWidth : .5,
							border : false,
							items : [workAirPressure, workWaterPressure]
						}, {
							layout : 'form',
							columnWidth : .5,
							border : false,
							items : [oldCode, bypassType]
						}, {
							layout : 'form',
							columnWidth : .5,
							border : false,
							items : [layType, memo]
						}]
	})

			var formPanel = new Ext.form.FormPanel({
						id : 'formPanel',
						autoScroll : true,
						width : "100%",
						frame : false,
						border : false,
						layout : 'column',
						labelAlign : 'right',
						labelWidth : 110,
						tbar : [{
									text : '保存',
									id : "btnS",
									iconCls : 'save',
									handler : saveInfo
								}],
						items : [{
			                    columnWidth : 1,
			                    frame : false,
			                    border : false,
			                    layout : 'form',
			                    items : [set1]
		                        }]		
						
					})
			function saveInfo() {
				auxiliaryId.setValue(pId);
				if (!formPanel.getForm().isValid()) {
					return false;
				}
				formPanel.getForm().submit({
							url : 'productionrec/saveHeaterParameter.action',
							method : 'POST',
							success : function(form, action) {
								var o = eval('(' + action.response.responseText
										+ ')');
								heaterId.setValue(o.heaterId);
								Ext.Msg.alert('提示', '保存成功');
							},
							failure : function() {
								Ext.Msg.alert('提示', '操作失败，请联系管理员！');
							}
						})
			}
			if (pId != null) {
				formPanel.show();
				auxiliaryId.setValue(pId);
				Ext.Ajax.request({
							url : 'productionrec/getHeaterParameter.action',
							method : 'post',
							params : {
								auxiliaryId : pId
							},
							success : function(result, request) {
								 str = result.responseText;
								 var o = eval('('
								 + str.substring(1, str.length - 1)
								 + ')');
								airFlux.setValue(o.airFlux);
								auxiliaryId.setValue(o.auxiliaryId);
								bypassType.setValue(o.bypassType);
								exportWaterTpr.setValue(o.exportWaterTpr);
								heatArea.setValue(o.heatArea);
								heaterId.setValue(o.heaterId);
								importAirTpr.setValue(o.importAirTpr);
								importWaterTpr.setValue(o.importWaterTpr);
								layType.setValue(o.layType);
								memo.setValue(o.memo);
								oldCode.setValue(o.oldCode);
								waterFlux.setValue(o.waterFlux);
								workAirPressure.setValue(o.workAirPressure);
								workWaterPressure.setValue(o.workWaterPressure);
								
							}
						})
			} else {
				formPanel.hide();
			}
			var view = new Ext.Viewport({
						enableTabScroll : true,
						layout : "border",
						border : false,
						frame : false,
						items : [{
									 layout : 'fit',
									border : false,
									frame : false,
									region : "center",
									autoScroll : true,
									items : [formPanel]
								}]
					});
		})