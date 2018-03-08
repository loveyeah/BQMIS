Ext.onReady(function() {

	var args = window.dialogArguments;
	var ArgItemCode = args ? args.itemCode : '1';
	var ArgItemName = args ? args.itemName : '2';

	function init() {
		Ext.Ajax.request({
					url : 'manager/getBpCEnthalpyFormula.action',
					params : {
						itemCode : ArgItemCode
					},
					success : function(result, request) {
						var o = eval('(' + result.responseText + ')');

						if (o.itemCode != null) {

							ylZbbm.setValue(o.ylZbbm);
							wdZbbm.setValue(o.wdZbbm);
							ylZbbmName.setValue(o.ylZbbmName);
							wdZbbmName.setValue(o.wdZbbmName);
						}
					},
					failure : function(result, request) {

						Ext.Msg.alert("错误", "初始化失败！");
					}
				});

	}

	// 指标编码
	var itemCode = new Ext.form.TextField({
				id : "itemCode",
				name : "statItem.itemCode",
				fieldLabel : "指标编码",
				readOnly : true,
				anchor : "85%",
				value : ArgItemCode
			})
	// 指标名称
	var itemName = new Ext.form.TextField({
				id : "itemName",
				name : "itemName",
				fieldLabel : "指标名称",
				readOnly : true,
				anchor : "85%",
				value : ArgItemName
			})
	// 压力指标
	var ylZbbmName = new Ext.form.ComboBox({
		id : "ylZbbmName",
		name : "ylZbbmName",
		fieldLabel : '压力指标',
		readOnly : true,
		anchor : '85%',
		allowBlank : false,
		onTriggerClick : function(e) {
			var args = {
				selectModel : 'single',
				rootNode : {
					id : "0",
					text : '指标名称'
				}
			}
			var url = "selectItem.jsp";
			var rvo = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:250px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (typeof(rvo) != "undefined") {
				if (rvo.itemCode == ArgItemCode) {
					Ext.Msg.alert("提示", "指标公式不能包含该指标本身！");

				} else {
					ylZbbm.setValue(rvo.itemCode);
					ylZbbmName.setValue(rvo.itemName);
				}
			}
		}
	});
	// 温度指标
	var wdZbbmName = new Ext.form.ComboBox({
		id : "wdZbbmName",
		name : "wdZbbmName",
		fieldLabel : '温度指标',
		readOnly : true,
		anchor : '85%',
		allowBlank : false,
		onTriggerClick : function(e) {
			var args = {
				selectModel : 'single',
				rootNode : {
					id : "0",
					text : '指标名称'
				}
			}
			var url = "selectItem.jsp";
			var rvo = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:250px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (typeof(rvo) != "undefined") {
				if (rvo.itemCode == ArgItemCode) {
					Ext.Msg.alert("提示", "指标公式不能包含该指标本身！");

				} else {
					wdZbbm.setValue(rvo.itemCode);
					wdZbbmName.setValue(rvo.itemName);
				}
			}
		}
	});
	// 压力指标编码
	var ylZbbm = new Ext.form.Hidden({
				name : 'statItem.ylZbbm'
			});
	// 温度指标编码
	var wdZbbm = new Ext.form.Hidden({
				name : 'statItem.wdZbbm'
			});
	var tbar = new Ext.Toolbar({
				items : [{
					id : 'btnSave',
					text : '保存',
					iconCls : 'save',
					handler : function() {

						if (form.getForm().isValid()) {
							form.getForm().submit({
								method : 'post',
								url : 'manager/saveBpCEnthalpyFormula.action',
								params : {},
								success : function(form, action) {
									var o = eval('('
											+ action.response.responseText
											+ ')');

									Ext.MessageBox.alert('提示信息', o.msg);
								},
								failure : function(form, action) {
									alert()
									var o = eval('('
											+ action.response.responseText
											+ ')');
									Ext.MessageBox.alert('错误', o.msg);
								}
							})
						}
					}
				}, '-', {

					id : 'btnDelete',
					text : '删除',
					iconCls : 'delete',
					handler : DeleteMonth
				}, '-', {

					id : 'btnClose',
					text : '关闭',
					iconCls : 'exit',
					handler : returndata

				}]
			})
	// 关闭
	function returndata() {
		var object = "焓值(" + ylZbbmName.getValue() + ","
				+ wdZbbmName.getValue() + ")";

		// alert(record.get('standardInfo.professionCode'));
		window.returnValue = object;
		window.close();
	}

	// 监听窗口关闭事件
	window.onbeforeunload = onbeforeunload_handler;

	function onbeforeunload_handler() {
		var object = "焓值(" + ylZbbmName.getValue() + ","
				+ wdZbbmName.getValue() + ")";

		// alert(record.get('standardInfo.professionCode'));
		window.returnValue = object;

	}

	function DeleteMonth() {
		Ext.Msg.confirm('提示', '删除后您将不能恢复!确定要删除吗?', function(response) {
					if (response == 'yes') {
						Ext.Msg.wait("正在删除.....");
						Ext.Ajax.request({
									url : 'manager/deleteBpCEnthalpyFormula.action',
									params : {
										itemCode : ArgItemCode
									},
									success : function(result, request) {

										Ext.Msg.alert("提示", "删除成功！");
										window.close();
									},
									failure : function(result, request) {

										Ext.Msg.alert("错误", "删除失败！");
									}
								});

					}
				});

	}
	var form = new Ext.form.FormPanel({
				bodyStyle : "padding:5px 5px 0",
				labelAlign : 'right',
				labelWidth : 50,
				autoHeight : false,
				region : 'center',
				border : false,
				tbar : tbar,
				items : [{
							border : false,
							layout : 'form',
							columnWidth : .5,
							labelWidth : 70,
							items : [itemCode]
						}, {
							border : false,
							layout : 'form',
							columnWidth : .5,
							labelWidth : 70,
							items : [itemName]
						}, {
							border : false,
							layout : 'form',
							columnWidth : .5,
							labelWidth : 70,
							items : [ylZbbmName]
						}, {
							border : false,
							layout : 'form',
							columnWidth : .5,
							labelWidth : 70,
							items : [wdZbbmName]
						}, ylZbbm, wdZbbm]
			})

	// 设定布局器及面板
	new Ext.Viewport({
				enableTabScroll : true,
				align : 'center',
				layout : 'border',
				height : 3000,
				items : [form]

			});
	init();

})