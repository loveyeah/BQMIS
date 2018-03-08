Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	// 系统当前时间
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "";

		return s;
	}

	// 计划时间
	var planTime = new Ext.form.TextField({

				name : 'planTime',
				fieldLabel : '计划时间',
				allowBlank : false,
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '',
									alwaysUseStartDate : true,
									dateFmt : "yyyy-MM"

								});
						this.blur();
					}

				}

			});
	planTime.setValue(getDate());
	// 计划主题
	var url = "manageplan/getPlanTopic.action";
	var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", url, false);
	conn.send(null);
	var search_data4 = eval('(' + conn.responseText + ')');

	var planTopic = new Ext.form.ComboBox({
				id : "planTopic",
				name : "planTopic",
				hiddenName : "topicCode",
				fieldLabel : "计划主题",
				triggerAction : 'all',
				store : new Ext.data.SimpleStore({
							fields : ["name", "id"],
							data : search_data4
						}),
				displayField : "name",
				valueField : "id",
				emptyText : '请选择...',
				mode : 'local',
				readOnly : true,
				allowBlank : false,
				anchor : "90%"
			});

	function query() {
		if (planTime.getValue() == null || planTime.getValue() == "") {
			Ext.Msg.alert('提示', '请选择计划时间！')
		} else if (planTopic.getValue() == null || planTopic.getValue() == "") {
			Ext.Msg.alert('提示', '请选择计划主题！')
		} else {
			store.rejectChanges();
			store.load({
						params : {
							topicCode : Ext.get("topicCode").dom.value,
							planTime : planTime.getValue(),

							start : 0,
							limit : 18
						}
					});
		}

	}
	var query = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : query

			})

	//
	var myform = new Ext.form.FormPanel({
				bodyStyle : "padding: 20,10,0,20",
				layout : 'column',
				items : [{
							columnWidth : '.2',
							layout : 'form',
							border : false,
							labelWidth : 60,
							items : [planTime]

						}, {
							columnWidth : '.2',
							labelWidth : 60,
							layout : 'form',
							border : false,
							items : [planTopic]
						}, {
							columnWidth : '.1',
							layout : 'form',
							border : false,

							items : [query]
						}

				]

			})

	var MyRecord = Ext.data.Record.create([{
				name : 'topicItemId'
			}, {
				name : 'reportId'
			}, {
				name : 'itemCode'
			}, {
				name : 'planValue'
			}, {
				name : 'lastValue'
			}, {
				name : 'itemName'
			}, {
				name : 'unitName'
			}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'manageplan/queryBpJPlanTopicItemList.action '
			});

	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"

			}, MyRecord);

	var store = new Ext.data.Store({

				proxy : dataProxy,

				reader : theReader

			});

	var sm = new Ext.grid.CheckboxSelectionModel();

	number = new Ext.grid.RowNumberer({
				header : "",
				align : 'left'
			})

	// 删除记录

	function deleteRecords() {
		Ext.Ajax.request({
					url : 'manageplan/deleteAllRefer.action',
					method : 'post',
					params : {
						planTime : planTime.getValue(),
						topicCode : planTopic.getValue()

					},
					success : function(result, request) {

						store.rejectChanges();
						ids = [];
						store.load({
									params : {
										topicCode : Ext.get("topicCode").dom.value,
										planTime : planTime.getValue(),
										start : 0,
										limit : 18
									}
								});
					},
					failure : function(form, action) {
						Ext.MessageBox.alert('提示信息', '未知错误！')
					}
				})
	}

	// 保存
	function saveModifies() {
		grid.stopEditing();
		var modifyRec = store.getRange(0, store.getCount());

		if (modifyRec.length > 0) {
			if (!confirm("确定要保存修改吗?"))
				return;

			var updateData = new Array();
			for (var i = 0; i < modifyRec.length; i++) {

				updateData.push(modifyRec[i].data);

			}

			Ext.Ajax.request({
						url : 'manageplan/saveBpJPlanTopicAndItem.action',
						method : 'post',
						params : {
							planTime : planTime.getValue(),
							topicCode : planTopic.getValue(),
							isUpdate : Ext.util.JSON.encode(updateData)
						},
						success : function(result, request) {
							// var o = eval('(' +
							// action.response.responseText
							// + ')');
							// depMainId.setValue(o.obj);
							// // 保存后上报按钮可用
							// Ext.getCmp("btnupcommit").setDisabled(false);
							Ext.MessageBox.alert('提示信息', "保存成功");
							store.rejectChanges();
							ids = [];
							store.load({
										params : {
											topicCode : Ext.get("topicCode").dom.value,
											planTime : planTime.getValue(),

											start : 0,
											limit : 18
										}
									});
						},
						failure : function(form, action) {
							Ext.MessageBox.alert('提示信息', '未知错误！')
						}
					})

		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}

	// 定义grid

	var grid = new Ext.grid.EditorGridPanel({
				// region : "west",
				store : store,
				layout : 'fit',
				// width:'0.5',
				columns : [

						sm, // 选择框
						number, {

							header : "指标编码",

							sortable : false,
							dataIndex : 'itemCode'
						}, {

							header : "指标名称",

							sortable : false,
							dataIndex : 'itemName'

						}, {

							header : "计量单位",

							sortable : false,
							dataIndex : 'unitName'
						}, {

							header : "上一年同期值",

							sortable : false,
							dataIndex : 'lastValue',
							editor : new Ext.form.TextField()

						}, {

							header : "本期计划值",

							sortable : false,
							dataIndex : 'planValue',
							editor : new Ext.form.TextField()

						}

				],
				tbar : [planTime, '-', planTopic, query, {
							text : "保存",
							id : 'btnsave',
							iconCls : 'save',
							handler : saveModifies
						}], //{
							//text : "删除",
						//	id : 'btndel',
							//iconCls : 'delete',
							//handler : deleteRecords
						//}],
				sm : sm

			});
	/** 右边的grid * */

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [
						// {
						//
						// layout : 'fit',
						// border : false,
						// frame : false,
						// region : "north",
						// height : 80,
						// items : [myform]
						// },
						{
					// bodyStyle : "padding: 20,20,20,0",
					region : "center",
					border : false,
					frame : false,
					layout : 'fit',
					height : '50%',
					// width : '50%',
					items : [grid]
				}]
			});
		// // 页面载入未选时间时所有按钮不可用
		// Ext.getCmp("btnapprove").setDisabled(true);
		//
		// Ext.getCmp("btnadd").setDisabled(true);
		// Ext.getCmp("btndelete").setDisabled(true);
		// Ext.getCmp("btnsave").setDisabled(true);
		// Ext.getCmp("btncancer").setDisabled(true);
		// Ext.getCmp("btnupcommit").setDisabled(true);

})
