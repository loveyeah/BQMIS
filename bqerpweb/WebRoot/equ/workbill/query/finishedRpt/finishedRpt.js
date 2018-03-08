Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

			var woCode = getParameter("woCode");

			var id;

			Ext.Ajax.request({
				url : 'workbill/getDefaultWorkerCode.action',
				params : {},
				method : 'post',
				waitMsg : '正在加载数据...',
				success : function(result, request) {
					var responseArray = Ext.util.JSON
							.decode(result.responseText);
					if (responseArray.success == true) {
						var tt = eval('(' + result.responseText + ')');
						// var o = tt.data;
						alert(tt)
						Ext.get('workerCode').dom.value = tt;
					}
					// else {
					// Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
					// }
				}
					// ,
					// failure : function(result, request) {
					// Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
					// }
				});

			Ext.Ajax.request({
				url : 'workbill/getEquJFinishedRpt.action',
				params : {
					woCode : woCode
				},
				method : 'post',
				waitMsg : '正在加载数据...',
				success : function(result, request) {

					var responseArray = Ext.util.JSON
							.decode(result.responseText);

					if (responseArray.success == true) {

						var tt = eval('(' + result.responseText + ')');
						var o = tt.baseInfo;
						Ext.get("checkReportid").dom.value = o.checkReportid
						Ext.get("checkResultid").dom.value = o.checkResultid
						Ext.get("checkReasonid").dom.value = o.checkReasonid

						var p = tt.model;
						id = p.id

						Ext.get("peNotion").dom.value = p.peNotion

					}
					// else {
					// Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
					// }
				}
					// ,
					// failure : function(result, request) {
					// Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
					// }
				});

			// 内容
			var checkReportid = new Ext.form.TextArea({
						margins : '10 10 15 10',
						fieldLabel : '检查工作内容',

						name : "checkReportid",
						width : '98%',

						autoScroll : true,
						border : false,
						height : 55,
						value : ''
					})
			// 措施
			var checkResultid = new Ext.form.TextArea({
						fieldLabel : '检修中发现的缺陷及采取的措施',
						name : "checkResultid",
						width : '98%',
						autoScroll : true,
						border : false,
						height : 55,
						value : ''
					})
			// 意见
			var checkReasonid = new Ext.form.TextArea({
						fieldLabel : '遗留问题及原因',
						name : "checkReasonid",
						width : '98%',
						autoScroll : true,
						border : false,
						height : 55,
						value : ''
					})
			// 专工意见
			var peNotion = new Ext.form.TextArea({
						fieldLabel : '专工审查意见',
						name : "peNotion",
						width : '98%',
						autoScroll : true,
						border : false,
						height : 55,
						value : ''
					})
			// // 备注label
			// var lblRemarks = new Ext.form.Label({
			// text : " 备注：",
			// border : false,
			// style : 'font-size:12px'
			// });
			// var remarkSet = new Ext.Panel({
			// autoHeight : true,
			// border : false,
			// style :
			// "padding-top:10;padding-left:10px;padding-bottom:10px;border-width:0
			// 0 0
			// 0;",
			// items : [
			// lblRemarks,
			// remark]
			// });

			// var SaveCBill = new Ext.form.FieldSet({
			// bodyStyle : "padding:10px 0px 0px 0px",
			// title : '',
			// height : '100%',
			// collapsible : true,
			// layout : 'form',
			// border:false,
			// items : [remark]
			// });

			// ↓↓****************员工验证窗口****************
			// 工号,测试时value写死为999999
			var workCode = new Ext.form.TextField({
						id : "workerCode",
						value : '999999',
						fieldLabel : '工号<font color ="red">*</font>',
						allowBlank : false,
						width : 120
					});

			// 密码
			var workPwd = new Ext.form.TextField({
						id : "workPwd",
						fieldLabel : '密码<font color ="red">*</font>',
						allowBlank : false,
						inputType : "password",
						width : 120
					});
			// 弹出窗口panel
			var workerPanel = new Ext.FormPanel({
						frame : true,
						labelAlign : 'right',
						height : 120,
						items : [workCode, workPwd]
					});

			// 弹出窗口
			var url1;
			var validateWin = new Ext.Window({
						width : 300,
						height : 140,
						modal : true,
						title : "请输入工号和密码",
						buttonAlign : "center",
						resizable : false,
						items : [workerPanel],
						buttons : [],
						closeAction : 'hide'
					});
			// ↑↑****************员工验证窗口****************

			// 审批签字
			var approvePanel = new Ext.form.FormPanel({

						border : false,
						labelAlign : 'top',
						layout : "column",

						items : [{
									columnWidth : 1,
									border : false
								}, {
									columnWidth : 1,
									border : false
								}, {
									columnWidth : 0.1,
									border : false
								}, {
									buttonAlign : 'center',
									columnWidth : 0.8,
									border : false,
									layout : 'form',
									items : [checkReportid, checkReasonid,
											checkResultid, peNotion],
									buttons : []

								}, {
									columnWidth : 0.1,
									border : false
								}]
					});

			var viewport = new Ext.Viewport({
						layout : "border",
						border : false,
						frame : false,

						items : [{
									region : 'center',
									layout : 'fit',
									// margins : '10 10 15 10',
									items : [approvePanel]
								}]
					});
			function disable() {
				checkReportid.setDisabled(true);
				checkReasonid.setDisabled(true);
				checkResultid.setDisabled(true);
				peNotion.setDisabled(true);
			}
disable();
		});