Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	var tabpanel = parent.Ext.getCmp('maintab');
	// 取左边store数据
	function queryRecord() {
		store.load({
					params : {
						argFuzzy : query.getValue(),
						start : 0,
						limit : 18
					}
				});
	}

	// 人员姓名
	var fillName = new Ext.form.TextField({
		fieldLabel : "人员姓名",
		name : 'workerName',
		emptyText : '请选择...',
		listeners : {
			focus : function() {
				var args = {
					selectModel : 'single',
					rootNode : {
						id : "0",
						text : '合肥电厂'
					}
				}
				var url = "/power/comm/jsp/hr/workerByDept/workerByDept2.jsp";
				var rvo = window
						.showModalDialog(
								url,
								args,
								'dialogWidth:500px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;')
				if (typeof(rvo) != "undefined") {
					workerCode.setValue(rvo.workerCode);
					fillName.setValue(rvo.workerName);
				}
				this.blur();
			}
		},
		readOnly : true,
		allowBlank : false,
		anchor : '90%'
	})

	// 焊工代码
	var weldCode = new Ext.form.TextField({
				fieldLabel : "焊工代码",
				name : 'model.weldCode',
				anchor : '90%'
			});
	// 人员编码
	var workerCode = new Ext.form.Hidden({
				name : 'model.workerCode'
			});
	// 焊龄
	var weldAge = new Ext.form.NumberField({
				fieldLabel : "焊龄(年)",
				name : 'model.weldAge',
				maxValue : 99,
				minValue : 0,
				emptyText : '最大99..',
				anchor : '90%'
			});
	// 从事焊接时间
	var weldWorkDate = new Ext.form.DateField({
				fieldLabel : "从事焊接时间",
				readOnly : true,
				name : 'model.weldWorkDate',
				value : '',
				anchor : '90%'
			});

	// 焊工等级 add by drdu 091106
	var weldLevel = new Ext.form.TextField({
		id : 'weldLevel',
		fieldLabel : "焊工等级",
		readOnly : false,
		name : 'model.weldLevel',
		anchor : '90%'
	});		
	// 主键
	var weldId = new Ext.form.Hidden({
				name : 'model.weldId'
			})

	// 弹窗的表单对象
	var blockForm = new Ext.form.FormPanel({
				labelAlign : 'right',
				frame : true,
				labelWidth : 90,
				items : [{
					bodyStyle : "padding:10px 0 0 0",
					layout : 'form',
					items : [fillName, weldCode,weldLevel, weldWorkDate, weldAge, weldId,
							workerCode]
				}]

			});
	// 左边的弹出窗体

	var blockAddWindow = new Ext.Window({
				// el : 'window_win',
				title : '',
				layout : 'fit',
				width : 350,
				height : 230,
				modal : true,
				closable : true,
				border : false,
				resizable : false,
				closeAction : 'hide',
				plain : true,
				// 面板中按钮的排列方式
				buttonAlign : 'center',
				items : [blockForm],
				buttons : [{
					text : '保存',
					handler : function() {
						var myurl = "";
						if (blockForm.getForm().isValid())
						var myurl = "";
						if (op == "add") {
							myurl = "productionrec/saveJbxx.action";
						} else if (op == "edit") {
							myurl = "productionrec/updateJbxx.action";
						} else {
							Ext.MessageBox.alert('错误', '未定义的操作');
							return;
						}
						blockForm.getForm().submit({
							method : 'POST',
							url : myurl,
							success : function(form, action) {
								var o = eval("(" + action.response.responseText
										+ ")");

								queryRecord();
								blockAddWindow.hide();

							},
							faliue : function() {

								Ext.Msg.alert('错误', '出现未知错误.');
							}
						});
					}
				}, {
					text : '取消',
					handler : function() {
						blockForm.getForm().reset();
						blockAddWindow.hide();
					}
				}]
			});
	/** 左边的grid * */

	var MyRecord = Ext.data.Record.create([{
				name : 'model.weldId'
			}, {
				name : 'model.weldCode'
			}, {
				name : 'model.weldWorkDate'
			}, {
				name : 'model.weldAge'
			}, {
				name : 'model.workerCode'
			}, {
				name : 'workerName'
			}, {
				name : 'model.weldLevel'
			}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'productionrec/findJbxxList.action '
			});

	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, MyRecord);

	var store = new Ext.data.Store({
				proxy : dataProxy,
				reader : theReader
			});

	var weldsm = new Ext.grid.CheckboxSelectionModel();
	var number = new Ext.grid.RowNumberer({
				header : "",
				align : 'left'
			})
	// 新建按钮
	var westbtnAdd = new Ext.Button({
				text : '新增',
				iconCls : 'add',
				handler : function() {
					blockForm.getForm().reset();
					op = 'add';
					blockAddWindow.show();
					blockAddWindow.setTitle("增加焊工技能信息");
				}
			});

	// 修改按钮
	var westbtnedit = new Ext.Button({
				text : '修改',
				iconCls : 'update',
				handler : function() {
					op = 'edit';
					if (grid.selModel.hasSelection()) {
						var records = grid.getSelectionModel().getSelected();
						var recordslen = records.length;
						if (recordslen > 1) {
							Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
						} else {
							blockAddWindow.show();
							var record = grid.getSelectionModel().getSelected();
							blockForm.getForm().reset();
							blockForm.form.loadRecord(record);
							weldWorkDate.setValue(record.get("model.weldWorkDate")
											.substring(0, 10));
							blockAddWindow.setTitle("修改焊工技能信息");
						}
					} else {
						Ext.Msg.alert("提示", "请先选择要编辑的行!");
					}
				}
			});

	// 删除按钮
	var westbtndel = new Ext.Button({
				text : '删除',
				iconCls : 'delete',
				handler : function() {
					var records = grid.selModel.getSelections();
					var ids = [];
					if (records.length == 0) {
						Ext.Msg.alert("提示", "请选择要删除的记录！");
					} else {
						for (var i = 0; i < records.length; i += 1) {
							var member = records[i];
							if (member.get("model.weldId")) {
								ids.push(member.get("model.weldId"));
							} else {
								store.remove(store.getAt(i));
							}
						}
						Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(
										buttonobj) {
									if (buttonobj == "yes") {
										Ext.lib.Ajax
												.request(
														'POST',
														'productionrec/deleteJbxx.action',
														{
															success : function(
																	action) {

																queryRecord();
															},
															failure : function() {
																Ext.Msg
																		.alert(
																				'错误',
																				'删除时出现未知错误.');

															}
														}, 'ids=' + ids);
									}
								});
					}
				}
			});

	// 刷新按钮
	var westbtnref = new Ext.Button({
				text : '刷新',
				iconCls : 'reflesh',
				handler : function() {
					queryRecord();
				}
			});

	var query = new Ext.form.TextField({
				id : 'argFuzzy',
				fieldLabel : "模糊查询",
				hideLabel : true,
				emptyText : '姓名..',
				name : 'argFuzzy',
				width : 70,
				value : ''
			});
	function fuzzyQuery() {
		store.load({
					params : {
						start : 0,
						limit : 18,
						argFuzzy : query.getValue()
					}
				});
	};

	// 签字时的Enter
	document.onkeydown = function() {
		if (event.keyCode == 13 && document.getElementById('argFuzzy')) {
			fuzzyQuery();
			// document.getElementById('btnSign').click();
		}
	}
	// 定义grid

	var grid = new Ext.grid.GridPanel({
		sm : weldsm,
		store : store,
		layout : 'fit',
		// width:'0.5',
		cm : new Ext.grid.ColumnModel([
				weldsm, // 选择框
				number, {
					header : "姓名",
					sortable : false,
					dataIndex : 'workerName'

				},{
					header : "weldLevel",
					sortable : false,
					hidden : true,
					dataIndex : 'model.weldLevel'

				}]),
		tbar : [query, new Ext.Button({
							iconCls : 'query',
							text : '查询',
							handler : function() {
								fuzzyQuery();
							}
						}), westbtnAdd, {
					xtype : "tbseparator"
				}, westbtnedit, {
					xtype : "tbseparator"
				}, westbtndel, {
					xtype : "tbseparator"
				}],
		bbar : new Ext.PagingToolbar({
					pageSize : 18,
					store : store,
					displayInfo : true,
					displayMsg : "共{2}条",
// beforePageText : '',
					afterPageText : "",
					emptyMsg : "没有记录"
				}),
		clicksToEdit : 1,
		viewConfig : {
			forceFit : true
		}
			// })
		});

	grid.on('click', modifyBtn);
	function modifyBtn() {
		Ext.getCmp("btnAdd").setDisabled(false);
		Ext.getCmp("btnDelete").setDisabled(false);
		Ext.getCmp("btnCancer").setDisabled(false);
		Ext.getCmp("btnSave").setDisabled(false);

		var recL = grid.getSelectionModel().getSelected();
		if (recL) {
			if (recL.get("model.weldId") != null) {
				con_ds.load({
							params : {
								weldId : recL.get("model.weldId"),
								start : 0,
								limit : 18

							}
						})
			}

		} else {
			Ext.getCmp("btnAdd").setDisabled(true);
			Ext.getCmp("btnDelete").setDisabled(true);
			Ext.getCmp("btnCancer").setDisabled(true);
			Ext.getCmp("btnSave").setDisabled(true);
		}
	}
	grid.on('rowdblclick', modifyBtn2);
	function modifyBtn2() {
		var recL = grid.getSelectionModel().getSelected();
		if (recL) {
			parent.weldId = recL.get("model.weldId");
			parent.workerName = recL.get("workerName");
			if (parent.document.all.iframe2 != null) {
				parent.document.all.iframe2.src = "productiontec/metalSupervise/business/welderInfo/assess.jsp";

			}
			tabpanel.setActiveTab(1);
		}
	}
	/** 左边的grid * */

	/** 右边的grid * */

	var con_item = Ext.data.Record.create([{
				name : 'hgjnkhId'
			}, {
				name : 'weldId'
			}, {
				name : 'examDate'
			}, {
				name : 'fetchDate'
			}, {
				name : 'checkUnit'
			}, {
				name : 'sendUnit'
			}, {
				name : 'cardCode'
			}, {
				name : 'steelCode'
			}, {
				name : 'nextCheckDate'
			}]);

	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false

			});
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'productionrec/findJnkhbListByweldId.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "",
							root : ""
						}, con_item)
			});

	var Rnumber = new Ext.grid.RowNumberer({
				header : "",
				align : 'left'
			})

	function dateParse(value) {

		if (value != null && value != "") {

			if (typeof(value) == 'object') {

				return value.format('Y-m-d');

			} else {

				var myYear = value.substring(0, 4);
				var myMonth = value.substring(5, 7);

				var myDay = value.substring(8, 10);
				return myYear + '-' + myMonth + '-' + myDay;

			}
		} else {

			return "";
		}
	}
	var con_item_cm = new Ext.grid.ColumnModel([con_sm, Rnumber,{
				header : 'weldId',
				dataIndex : 'weldId',
				hidden : true,
				align : 'center'
			},
			{
				header : '考核时间',
				dataIndex : 'examDate',
				align : 'center',
				editor : new Ext.form.DateField({
							readOnly : true
						}),
				renderer : function(value) {
					return dateParse(value);
				}
			}, {
				header : '领证日期',
				dataIndex : 'fetchDate',
				align : 'center',
				editor : new Ext.form.DateField({
							readOnly : true
						}),
				renderer : function(value) {
					return dateParse(value);
				}
			}, {
				header : '考核单位',
				dataIndex : 'checkUnit',
				align : 'center',
				editor : new Ext.form.TextField()
			}, {
				header : '发证机构',
				dataIndex : 'sendUnit',
				align : 'center',
				editor : new Ext.form.TextField()
			}, {
				header : '证件编号',
				dataIndex : 'cardCode',
				align : 'center',
				editor : new Ext.form.TextField()
			}, {
				header : '钢印编号',
				dataIndex : 'steelCode',
				align : 'center',
				editor : new Ext.form.TextField()
			}, {
				header : '下次考核时间',
				dataIndex : 'nextCheckDate',
				align : 'center',
				editor : new Ext.form.DateField({
							readOnly : true
						}),
				renderer : function(value) {
					return dateParse(value);
				}
			}]);
	con_item_cm.defaultSortable = true;

	// 增加
	function addTopic() {
		if (grid.selModel.hasSelection()
				&& grid.getSelectionModel().getSelections().length == 1) {
			var rec = grid.getSelectionModel().getSelected();
			var count = con_ds.getCount();
			var currentIndex = count;
			var o = new con_item({
						'weldId' : rec.get("model.weldId"),
						'examDate' : new Date(),
						'fetchDate' : new Date(),
						'checkUnit' : '',
						'sendUnit' : '',
						'cardCode' : '',
						'steelCode' : '',
						'nextCheckDate' : new Date()
					});
			Grid.stopEditing();
			con_ds.insert(currentIndex, o);
			con_sm.selectRow(currentIndex);
			Grid.startEditing(currentIndex, 2);
			//resetLine();
		} else {
			Ext.MessageBox.alert("提示", "请选择坐标的<一条>记录!");
		}
	}

	// 删除记录
	var topicIds = new Array();
	function deleteTopic() {
		Grid.stopEditing();
		var sm = Grid.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("hgjnkhId") != null) {
					topicIds.push(member.get("hgjnkhId"));
				}
				Grid.getStore().remove(member);
				Grid.getStore().getModifiedRecords().remove(member);
			}
			// resetLine();
		}
	}
	// 保存
	function saveTopic() {
		Grid.stopEditing();
		var modifyRec = Grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || topicIds.length > 0) {
			if (!confirm("确定要保存修改吗?"))
				return;
			// var newData = new Array();
			var updateData = new Array();
			for (var i = 0; i < modifyRec.length; i++) {
				// if (modifyRec[i].get("hgjnkhId") == "") {
				// Ext.MessageBox.alert('提示信息', '主题名称不能为空！')
				// return
				// }
				updateData.push(modifyRec[i].data);
				// }
			}
			Ext.Ajax.request({
						url : 'productionrec/saveJnkhb.action',
						method : 'post',
						params : {
							// isAdd : Ext.util.JSON.encode(newData),
							isUpdate : Ext.util.JSON.encode(updateData),
							isDelete : topicIds.join(",")
						},
						success : function(result, request) {
							con_ds.rejectChanges();
							topicIds = [];
							con_ds.reload();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('提示信息', '    未知错误！')
						}
					})
		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}
	// 取消
	function cancerTopic() {
		var modifyRec = con_ds.getModifiedRecords();
		if (modifyRec.length > 0 || topicIds.length > 0) {
			if (!confirm("确定要放弃修改吗"))
				return;
			con_ds.reload();
			con_ds.rejectChanges();
			topicIds = [];
		} else {
			con_ds.reload();
			con_ds.rejectChanges();
			topicIds = [];
		}
	}

	var contbar = new Ext.Toolbar({
				items : [{
							id : 'btnAdd',
							iconCls : 'add',
							text : "新增",
							disabled : true,
							handler : addTopic

						}, {
							id : 'btnDelete',
							iconCls : 'delete',
							disabled : true,
							text : "删除",
							handler : deleteTopic

						}, {
							id : 'btnCancer',
							iconCls : 'cancer',
							disabled : true,
							text : "取消",
							handler : cancerTopic

						}, '-', {
							id : 'btnSave',
							iconCls : 'save',
							disabled : true,
							text : "保存修改",
							handler : saveTopic
						}]

			});
	var Grid = new Ext.grid.EditorGridPanel({
				sm : con_sm,
				ds : con_ds,
				cm : con_item_cm,
				height : 425,
				split : true,
				autoScroll : true,
				tbar : contbar,
				border : true,
				clicksToEdit : 2,
				viewConfig : {
					forceFit : true
				}
			});

	/** 左边的grid * */

	new Ext.Viewport({

				layout : "border",
				border : false,
				frame : false,
				items : [{
							// bodyStyle : "padding: 20,10,20,20",
							layout : 'fit',
							border : false,
							frame : false,
							region : "west",
							width : '30%',
							items : [grid]
						}, {
							// bodyStyle : "padding: 20,20,20,0",
							region : "center",
							border : false,
							frame : false,
							layout : 'fit',
							autoScroll : true,
							items : [Grid]
						}]
			});
			
			fuzzyQuery();

})