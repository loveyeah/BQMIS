Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	var m = new Map();
	var grid;
	var ds;
	function saveData() {
//		alert(Ext.util.JSON.encode(m));
//		return false;
		grid.stopEditing();
		var modifyRec = grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0) {
			if (!confirm("确定要保存修改吗?"))
				return;
			if (m != null)
				Ext.Ajax.request({
							url : 'managexam/saveOverInputList.action',
							method : 'post',
							params : {
								addOrUpdateRecords : Ext.util.JSON.encode(m.values()),
								datetime : datetime.getValue()
							},
							success : function(result, request) {
								var o = eval('(' + result.responseText + ')');
								Ext.MessageBox.alert('提示信息', o.msg);
								query();
								m = new Map();
							},
							failure : function(result, request) {
								Ext.MessageBox.alert('提示信息', '未知错误！')
							}
						})
		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}

	function query() {
		var dateTime = datetime.getValue();
		if (dateTime == null || dateTime == '') {
			Ext.Msg.alert("提示", "时间必填");
			return;
		}
		Ext.Msg.wait("正在查询数据!请等待...");
		Ext.Ajax.request({
			url : 'managexam/getOverInputList.action',
			params : {
				datetime : datetime.getValue()
			},
			method : 'post',
			waitMsg : '正在加载数据...',
			success : function(result, request) {
				var json = eval('(' + result.responseText + ')');
				document.getElementById("gridDiv").innerHTML = "";
				ds = new Ext.data.JsonStore({
							data : json.data,
							fields : json.fieldsNames
						});
				var wd = Ext.get('gridDiv').getWidth()
				var ht = Ext.get('gridDiv').getHeight()
				grid = new Ext.grid.EditorGridPanel({
					renderTo : 'gridDiv',
					stripeRows : true,
					id : 'grid',
					split : true,
					width : wd,
					height : ht,
					autoScroll : true,
					border : false,
					columns : json.columModle,
					cm : new Ext.grid.ColumnModel({
								columns : json.columModle,
								rows : json.rows
							}),
					enableColumnMove : false,
					plugins : [new Ext.ux.plugins.GroupHeaderGrid(null,
							json.rows)],
					ds : ds,
					clicksToEdit : 1,
					listeners : {
						'beforeedit' : function(e) { 
							grid
									.getColumnModel()
									.setEditor(
											e.column,
											new Ext.grid.GridEditor(new Ext.form.NumberField(
													{
														allowDecimals : true,
														decimalPrecision : 2
													})));

						},
						'afteredit' : function(e) {
							var v1 = new Object();
							v1.itemCode = e.field;
							v1.id = e.record.get(e.field + 'id');
							v1.value = e.record.get(e.field);
							v1.deptid = e.record.get('deptid');
							v1.overheadid = e.record.get(e.field.substring(1,
									e.field.length)
									+ 'overheadid');
							
							m.put(e.field + v1.deptid + v1.overheadid, v1);
						}

					}
				});
				grid.render();
				btnSave.show();
				Ext.Msg.hide();
			},
			failure : function(result, request) {
				Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
				Ext.Msg.hide();
			}
		});
	}

	function getYear() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10)
		return s;
	}

	var datetime = new Ext.form.TextField({
				style : 'cursor:pointer',
				columnWidth : 0.5,
				readOnly : true,
				anchor : "70%",
				fieldLabel : '月份',
				name : 'month',
				value : getYear(),
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y',
									dateFmt : 'yyyy',
									alwaysUseStartDate : true
								});
						this.blur();
					}
				}
			});

	var btnQuery = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : query
			});

	var btnSave = new Ext.Button({
				text : '数据保存',
				iconCls : 'save',
				handler : saveData
			});

	var tbar = new Ext.Toolbar({
				id : 'tbar',
				height : 25,
				items : ['时间：', datetime, '-', btnQuery, '-', btnSave]
			});

	var view = new Ext.Viewport({
				layout : 'fit',
				margins : '0 0 0 0',
				collapsible : true,
				split : true,
				border : false,
				items : [new Ext.Panel({
							id : 'panel',
							border : false,
							tbar : tbar,
							items : [{
										html : '<div id="gridDiv" ></div>'
									}]
						})]
			});

	Ext.get('gridDiv').setWidth(Ext.get('panel').getWidth());
	Ext.get('gridDiv').setHeight(Ext.get('panel').getHeight() - 25);

});
