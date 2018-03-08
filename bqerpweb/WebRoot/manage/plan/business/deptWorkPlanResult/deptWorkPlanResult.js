Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	// 取当前时间
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "";
		// t = d.getDate();
		// s += (t > 9 ? "" : "0") + t + " ";
		// t = d.getHours();
		// s += (t > 9 ? "" : "0") + t + ":";
		// t = d.getMinutes();
		// s += (t > 9 ? "" : "0") + t
		// + ":";
		// t = d.getSeconds();
		// s += (t > 9 ? "" : "0") + t;
		return s;
	}
	// 从session取登录人部门编码
	function getDepCodeOnly() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {

							var DeptCode = result.deptCode;
							editDepcode.setValue(DeptCode);

						}
						init();
					}
				});
	}

	function init() {
		Ext.lib.Ajax.request('POST', 'manageplan/getBpJPlanJobDepMain.action',
				{
					success : function(action) {
						var result = eval("(" + action.responseText + ")");

						if (result.baseInfo != null) {

							// 设定默认工号
							var mainId = result.baseInfo.depMainId;
							var workerName = result.editByName;
							var date = result.editDate;
							var DeptName = result.editDepName;

							depMainId.setValue(mainId);
							editByName.setValue(workerName);
							editDate.setValue(date);
							editDepName.setValue(DeptName);
							Ext.getCmp("save").setDisabled(false);
							store.rejectChanges();
							store.load({
										params : {
											depMainId : depMainId.getValue(),
											start : 0,
											limit : 18
										}
									});

						} else {
							depMainId.setValue('');
							editByName.setValue('');
							editDepName.setValue('');
							editBy.setValue('');

							editDate.setValue('');
							Ext.getCmp("save").setDisabled(true);
							store.rejectChanges();
							store.load({
										params : {

											start : 0,
											limit : 18
										}
									});
						}

					}
				}, 'planTime=' + planTime.getValue() + '&editDepcode='
						+ editDepcode.getValue());
	}
	// 计划时间
	var planTime = new Ext.form.TextField({
				name : 'planTime',
				fieldLabel : '计划时间',
				listeners : {
					focus : function() {
						WdatePicker({
							startDate : '',
							alwaysUseStartDate : true,
							dateFmt : "yyyy-MM"
								// ,
								// onpicked : function() {
								// year.clearInvalid();
								//
								// },
								// onclearing : function() {
								// year.markInvalid();
								//
								// }
							});
					}
				}

			});
	planTime.setValue(getDate());

	function query() {
		getDepCodeOnly();

	}
	// var query = new Ext.Button({
	// text : '查询',
	// iconCls : 'query',
	// handler : query
	//
	// })

	// 编辑人名称
	var editByName = new Ext.form.TextField({
				readOnly : true,
				fieldLabel : '编辑人',
				anchor : '80%'
			});

	// 编辑时间
	var editDate = new Ext.form.TextField({
				readOnly : true,
				name : 'editDate',
				fieldLabel : '编辑时间'
			});

	// 编辑人所在部门名称
	var editDepName = new Ext.form.TextField({
				fieldLabel : '编辑部门',
				readOnly : true

			});

	// 编辑人编码
	var editBy = new Ext.form.Hidden({
				name : 'editBy'
			});
	// 编辑人所在部门编码
	var editDepcode = new Ext.form.Hidden({
				name : 'editDepcode'
			});
	// 计划部门id
	var depMainId = new Ext.form.Hidden({
				name : 'depMainId'
			});

	//
	var MyRecord = Ext.data.Record.create([{
				name : 'jobId'
			}, {
				name : 'depMainId'
			}, {
				name : 'jobContent'
			}, {
				name : 'completeData'
			}, {
				name : 'ifComplete'
			}, {
				name : 'completeDesc'
			}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'manageplan/getBpJPlanJobDepDetail.action '
			});

	var theReader = new Ext.data.JsonReader({
				root : "",
				totalProperty : ""

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

	var tbar = new Ext.Toolbar({
				items : ['计划时间', planTime, '-', {
							id : 'btnQuery',
							text : "查询",
							iconCls : 'query',
							handler : function() {
								query();
							}
						}, '-', {
							text : "保存",
							id : 'save',
							iconCls : 'save',
							disabled : true,
							handler : saveModifies
						}]
			})

	var bbar = new Ext.Toolbar({
				items : ['编辑部门', editDepName, '编辑人', editByName, '编辑时间',
						editDate]
			})
	// 保存
	function saveModifies() {
		grid.stopEditing();

		if (depMainId.getValue() == undefined || depMainId.getValue() == "") {
			alert("没有计划！")
			return;
		}
		var modifyRec = store.getModifiedRecords();

		if (modifyRec.length > 0) {
			if (!confirm("确定要保存修改吗?"))
				return;

			var updateData = new Array();
			for (var i = 0; i < modifyRec.length; i++) {
				updateData.push(modifyRec[i].data);

			}
			Ext.Ajax.request({
						url : 'manageplan/saveBpJPlanJobDep.action',
						method : 'post',
						params : {
							planTime : planTime.getValue(),
							mainId : depMainId.getValue(),
							isUpdate : Ext.util.JSON.encode(updateData)
						},
						success : function(form, action) {
							var o = eval('(' + action.responseText + ')');

							Ext.MessageBox.alert('提示信息', "保存成功");
							store.rejectChanges();
							// ids = [];
							store.load({
										params : {
											depMainId : depMainId.getValue(),
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
	};

	var mystore = new Ext.data.SimpleStore({
				fields : ['deptName', 'deptCode'],
				data : [[1, 1], [1, 1]]
			});

	// 定义grid
	// 弹出画面

	var memoText = new Ext.form.TextArea({
				id : "memoText",
				maxLength : 128,
				width : 180
			});

	var win = new Ext.Window({
				height : 170,
				width : 350,
				layout : 'fit',
				modal : true,
				resizable : false,
				closeAction : 'hide',
				items : [memoText],
				buttonAlign : "center",
				title : '详细信息录入窗口',
				buttons : [{
					text : '保存',
					iconCls : 'save',
					handler : function() {
						if (!memoText.isValid()) {
							return;
						}
						var record = grid.selModel.getSelected();
						record.set("completeDesc",
								Ext.get("memoText").dom.value);
						win.hide();
					}
				}, {
					text : '关闭',
					iconCls : 'cancer',
					handler : function() {
						win.hide();
					}
				}]
			});

	var grid = new Ext.grid.EditorGridPanel({
		store : store,
		layout : 'fit',
		tbar : tbar,
		sm : sm,
		columns : [
				sm, // 选择框
				number, {
					width : 300,
					header : "工作内容",
					sortable : false,
					dataIndex : 'jobContent'

				        }, {
				    header : "完成时间",

							sortable : false,
							dataIndex : 'completeData',
							renderer : function changeIt(val) {
								if (val == "0") {
									return "当月完成";
								} else if (val == "1") {
									return "跨月完成";
								} else if (val == "3") {
									return "全年";
								} else if (val == "2") {
									return "长期";
								} else {
									return "";
								}
							}
						},{
					header : "完成情况",
					sortable : false,
					dataIndex : 'ifComplete',
					editor : new Ext.form.ComboBox({
								name : 'name111',
								hiddenName : 'hiddenName111',
								mode : 'local',
								triggerAction : 'all',
								store : new Ext.data.SimpleStore({
											fields : ['name', 'value'],
											data : [['未完成', 0], ['进行中', 1],
													['已完成', 2]]
										}),
								displayField : 'name',
								valueField : 'value'

							}),
					renderer : function changeIt(val) {
						if (val == '0') {
							return "未完成";
						} else if (val == '1') {
							return "进行中";
						} else if (val == '2') {
							return "已完成";
						} else {
							return "";
						}
					}
				}, {
					width : 300,
					header : "考核说明",
					sortable : false,
					dataIndex : 'completeDesc',
					editor : new Ext.form.TextArea({
								listeners : {
									"render" : function() {
										this.el.on("dblclick", function() {
													grid.stopEditing();
													var record = grid
															.getSelectionModel()
															.getSelected();
													var value = record
															.get('completeDesc');
													memoText.setValue(value);
													win.x = undefined;
													win.y = undefined;
													win.show();
												})
									}
								}
							})

				}],
		// 分页
		bbar : bbar,
		clicksToEdit : 2
	});
	/** 右边的grid * */

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [{
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

})
