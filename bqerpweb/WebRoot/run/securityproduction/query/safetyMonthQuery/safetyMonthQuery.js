Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	function getMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		return s;
	}
	var safetymonthId;

	// ID
	var hiddid = new Ext.form.Hidden({
				id : 'hiddid',
				name : 'safetyMonth.safetymonthId'
			})
	// 主題
	var subject = new Ext.form.TextField({
				name : 'safetyMonth.subject',
				xtype : 'textfield',
				readOnly : true,
				allowBlank : false,
				fieldLabel : '主題',
				anchor : '97%'
			});
	// 內容
	var content = new Ext.form.TextArea({
				id : 'safetyMonth.content',
				xtype : 'textarea',
				fieldLabel : '內容',
				readOnly : true,
				// allowBlank : false,
				height : 120,
				anchor : '97%'
			});
	// 总结
	var summary = new Ext.form.TextArea({
				id : 'safetyMonth.summary',
				xtype : 'textarea',
				fieldLabel : '总结',
				readOnly : true,
				height : 80,
				anchor : '97%'
			});
	// 填写人
	var fillBy = new Ext.form.TextField({
				id : "fillBy",
				name : "workName",
				fieldLabel : '填写人',
				readOnly : true,
				anchor : "90%"
			})

	var hiddfillBy = new Ext.form.Hidden({
				id : 'hiddfillBy',
				name : 'safetyMonth.fillBy'
			})
	// 填写日期
	var hiddfillDate = new Ext.form.Hidden({
				id : "hiddfillDate",
				name : "safetyMonth.fillDate",
				mode : 'local'
			})
	var fillDate = new Ext.form.TextField({
				id : "fillDate",
				readOnly : true,
				fieldLabel : '填写日期',
				name : 'filldateString',
				anchor : "95%"
			})
	// 月份
	var month = new Ext.form.TextField({
				style : 'cursor:pointer',
				// id : 'month',
				columnWidth : 0.5,
				readOnly : true,
				anchor : "70%",
				fieldLabel : '月份',
				name : 'month',
				value : getMonth(),
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M',
									alwaysUseStartDate : false,
									dateFmt : 'yyyy-MM',
									onpicked : function() {
									},
									onclearing : function() {
										planStartDate.markInvalid();
									}
								});
					}
				}
			})

	var stocktbar = new Ext.Toolbar({
		items : ["月份:", month, {
			id : 'btnQuery',
			text : "查询",
			iconCls : 'query',
			handler : function() {
				Ext.Ajax.request({
							url : 'security/getSafetyMonthInfo.action',
							params : {
								month : month.getValue()
							},
							method : 'post',
							waitMsg : '正在加载数据...',
							success : function(result, request) {
								var o = eval('(' + result.responseText + ')');
								if (o != null) {
									subject.setValue(o.safetymonth.subject);
									content.setValue(o.safetymonth.content);
									summary.setValue(o.safetymonth.summary);
									fillBy.setValue(o.workName);
									fillDate.setValue(o.filldateString);
									hiddid
											.setValue(o.safetymonth.safetymonthId);
									safetymonthId = o.safetymonth.safetymonthId;
								} else {
									Ext.MessageBox.alert('提示信息', '没有该月的数据!');
									Myform.getForm().reset();
									getWorkCode();
								}
							},
							failure : function(result, request) {
								Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
							}
						});
			}
		}]
	});

	// mianform
	var formContent = new Ext.form.FieldSet({
				border : false,
				layout : 'column',
				items : [{
							columnWidth : 1,
							layout : 'form',
							border : false,
							labelWidth : 70,
							items : [subject]
						}, {
							columnWidth : 1,
							layout : 'form',
							labelWidth : 70,
							border : false,
							items : [content]
						}, {
							border : false,
							layout : 'form',
							columnWidth : 1,
							labelWidth : 70,
							items : [summary]
						}, {
							border : false,
							layout : 'form',
							columnWidth : .5,
							labelWidth : 70,
							items : [hiddfillBy, fillBy]
						}, {
							border : false,
							layout : 'form',
							columnWidth : .5,
							labelWidth : 70,
							items : [hiddfillDate, fillDate]
						}, hiddid]
			});
	var Myform = new Ext.form.FormPanel({
				bodyStyle : "padding:5px 5px 0",
				labelAlign : 'right',
				labelWidth : 50,
				autoHeight : false,
				region : 'center',
				border : false,
				tbar : stocktbar,
				items : [formContent]
			});
	// 设定布局器及面板
	new Ext.Viewport({
				enableTabScroll : true,
				align : 'center',
				layout : 'border',
				height : 3000,
				items : [Myform]

			});
});