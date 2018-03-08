Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	
		// 系统当前时间
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		return s;
	};
	
	function getMon() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t
		return s;
	};
	
	var awardTime = new Ext.form.TextField({
				id : 'awardTime',
				fieldLabel : '发放月份',
				style : 'cursor:pointer',
				readOnly : true,
				width : 80,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M',
									dateFmt : 'yyyy-MM',
									alwaysUseStartDate : true,
									isShowClear : false
								});
						this.blur();
					}
				}
			});
		var record = Ext.data.Record.create([{
					name : 'bigAwardId',
					mapping : 0
				}, {
					name : 'bigAwardName',
					mapping : 1
				}, {
					name : 'awardMonth',
					mapping : 2
				}, {
					name : 'assessmentFrom',
					mapping : 3
				}, {
					name : 'assessmentTo',
					mapping : 4
				}, {
					name : 'halfStandarddays',
					mapping : 5
				}, {
					name : 'bigAwardBase',
					mapping : 6
				}, {
					name : 'memo',
					mapping : 7
				},{
					name : 'allStandarddays',
					mapping : 8
				}]);

		var dataProxy = new Ext.data.HttpProxy({
					url : 'com/findBigAwardList.action'
				});

		var theReader = new Ext.data.JsonReader({
					root : "list",
					totalProperty : "totalCount"
				}, record);

		var store = new Ext.data.Store({
					proxy : dataProxy,
					reader : theReader
				});

		var sm = new Ext.grid.CheckboxSelectionModel({
			singleSelect:true
		});

	function query() {
		store.baseParams = {
			awardTime : awardTime.getValue()
		}
		store.load({
					start : 0,
					limit : 18
				})
	}
		
		var grid = new Ext.grid.GridPanel({
					region : "center",
					layout : 'fit',
					store : store,
					columns : [sm, new Ext.grid.RowNumberer({
										header : '行号',
										width : 35,
										align : 'left'
									}), {
								header : "ID",
								width : 75,
								sortable : true,
								dataIndex : 'bigAwardId',
								hidden : true
							}, {
								header : "大奖名称",
								width : 200,
								allowBlank : false,
								sortable : true,
								dataIndex : 'bigAwardName'
							}, {
								header : "发放月份",
								width : 200,
								allowBlank : false,
								sortable : true,
								dataIndex : 'awardMonth'
							}, {
								header : "考核开始时间",
								width : 200,
								allowBlank : false,
								sortable : true,
								dataIndex : 'assessmentFrom'
							}, {
								header : "考核结束时间",
								width : 200,
								allowBlank : false,
								sortable : true,
								dataIndex : 'assessmentTo'
							}, {
								header : "半奖扣除标准天数",
								width : 200,
								allowBlank : false,
								sortable : true,
								dataIndex : 'halfStandarddays'
							},{
								header : "全奖扣除标准天数",
								width : 200,
								allowBlank : false,
								sortable : true,
								dataIndex : 'allStandarddays'
							}, {
								header : "大奖基数",
								width : 200,
								allowBlank : false,
								sortable : true,
								dataIndex : 'bigAwardBase'
							}, {
								header : "备注",
								width : 200,
								allowBlank : false,
								sortable : true,
								dataIndex : 'memo'
							}],
					sm : sm,
					autoSizeColumns : true,
					viewConfig : {
						forceFit : true
					},
					tbar : ["发放月份:", awardTime, '-',{
								text : "查询",
								iconCls : 'query',
								minWidth : 70,
								handler : query
							}, '-', {
								text : "新增",
								iconCls : 'add',
								minWidth : 70,
								handler : addRecord
							}, '-', {
								text : "修改",
								iconCls : 'update',
								minWidth : 70,
								handler : updateRecord
							}, '-', {
								text : "删除",
								iconCls : 'delete',
								minWidth : 70,
								handler : deleteRecord
							}],
					// 分页
					bbar : new Ext.PagingToolbar({
								pageSize : 18,
								store : store,
								displayInfo : true,
								displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
								emptyMsg : "没有记录"
							})
				});
		grid.on("rowdblclick", updateRecord);

		var bigAwardId = new Ext.form.Hidden({
					id : "bigAwardId",
					name : 'bigAward.bigAwardId'
				});

		var bigAwardName = new Ext.form.TextField({
					id : "bigAwardName",
					fieldLabel : '大奖名称',
					readOnly:false,//update by sychen 20100721
//					readOnly:true,
					anchor : "80%",
					name : 'bigAward.bigAwardName'
				});

		var awardMonth = new Ext.form.TextField({
				id : 'awardMonth',
				fieldLabel : "发放月份",
				name : 'awardMonth',
				style : 'cursor:pointer',
				anchor : "80%",
				value : getMon(),
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M',
									dateFmt : 'yyyy-MM',
									alwaysUseStartDate : true
								});
					}
				}
			});	
		
		var assessmentFrom = new Ext.form.TextField({
				id : 'assessmentFrom',
				fieldLabel : "考核开始时间",
				name : 'bigAward.assessmentFrom',
				style : 'cursor:pointer',
				anchor : "80%",
				value : getDate(),
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
			
		var assessmentTo = new Ext.form.TextField({
				id : 'assessmentTo',
				fieldLabel : "考核结束时间",
				name : 'bigAward.assessmentTo',
				style : 'cursor:pointer',
				anchor : "80%",
				value : getDate(),
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
			
		var halfStandarddays = new Ext.form.NumberField({
					id : "halfStandarddays",
					fieldLabel : '半奖扣除标准天数',
					allowBlank : false,
					anchor : "80%",
					name : 'bigAward.halfStandarddays'
				});
		var allStandarddays = new Ext.form.NumberField({//add by wpzhu 20100716
					id : "allStandarddays",
					fieldLabel : '全奖扣除标准天数',
					allowBlank : false,
					anchor : "80%",
					name : 'bigAward.allStandarddays'
				});
		
		var bigAwardBase = new Ext.form.TextField({
					id : "bigAwardBase",
					fieldLabel : '大奖基数',
					allowBlank : false,
					anchor : "80%",
					name : 'bigAward.bigAwardBase'
				});
		
		var memo = new Ext.form.TextArea({
					id : "memo",
					fieldLabel : '备注',
					allowBlank : true,
					anchor : "80%",
					name : 'bigAward.memo'
				});		
				
		var addpanel = new Ext.FormPanel({
					frame : true,
					labelAlign : 'center',
					labelWidth : 110,
					closeAction : 'hide',
					title : '增加/修改劳保用品分类信息',
					items : [bigAwardId,bigAwardName,awardMonth,assessmentFrom,
					         assessmentTo,halfStandarddays,allStandarddays,bigAwardBase,memo]
				});

		var win = new Ext.Window({
					width : 450,
					height : 350,
					buttonAlign : "center",
					items : [addpanel],
					layout : 'fit',
					closeAction : 'hide',
					resizable : false,
					modal : true,
					buttons : [{
						text : '保存',
						iconCls : 'save',
						handler : function() {
							var myurl = "";
							if (bigAwardId.getValue() == "") {
								myurl = "com/saveBigAward.action";
							} else {
								myurl = "com/updateBigAward.action";
							}
							addpanel.getForm().submit({
								method : 'POST',
								url : myurl,
								params : {
									awardMonth : awardMonth.getValue()
								},
								success : function(form, action) {
									if (action.result.existFlag == true) {
										Ext.Msg.alert("提示","该月份大奖已存在，不能继续增加。");//update by sychen 20100721
//										Ext.Msg.alert("提示","该月份已经存在两次大奖，不能继续增加。");
									} else {
										var o = eval("("
												+ action.response.responseText
												+ ")");
										Ext.Msg.alert("注意", o.msg);
										if (o.msg.indexOf("成功") != -1) {
											query();
											win.hide();
										}
									}
								},
								faliue : function() {
									Ext.Msg.alert('错误', '出现未知错误.');

								}
							});
							
						}
					}, {
						text : '取消',
						iconCls : 'cancer',
						handler : function() {
							win.hide();
						}
					}]

				});

		function addRecord() {
			addpanel.getForm().reset();
			win.show();
			addpanel.setTitle("增加大奖维护信息");
		}

		function updateRecord() {
			if (grid.selModel.hasSelection()) {
				var records = grid.selModel.getSelections();
				var recordslen = records.length;
				if (recordslen > 1) {
					Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
				} else {
					win.show();
					var record = grid.getSelectionModel()
							.getSelected();
					addpanel.getForm().reset();
					addpanel.getForm().loadRecord(record)

					awardMonth.setDisabled(true);
					addpanel.setTitle("修改大奖维护信息");
					
				}
			} else {
				Ext.Msg.alert("提示", "请先选择要编辑的行!");
			}
		}

		function deleteRecord() {
			var records = grid.selModel.getSelections();
			var ids = [];
			var awardMonth;
			if (records.length == 0) {
				Ext.Msg.alert("提示", "请选择要删除的记录！");
			} else {
				for (var i = 0; i < records.length; i += 1) {
					var member = records[i];
					awardMonth=member.get("awardMonth");
					if (member.get("bigAwardId")) {
						ids.push(member.get("bigAwardId"));
					} else {
						store.remove(store.getAt(i));
					}
				}
				Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(buttonobj) {
					if (buttonobj == "yes") {
						Ext.Ajax.request({
									url : 'com/deleteBigAward.action',
									method : 'post',
									params : {
										ids : ids.join(','),
										awardMonth : awardMonth
									},
									success : function(response, options) {
											Ext.Msg.alert("提示", "删除成功！");
											query();
									},
									failure : function(response, options) {

										Ext.Msg.alert('错误', '删除时出现未知错误.');
									}
								})
					}
				});
			}
		}
	query();	
			// 显示区域
	var view = new Ext.Viewport({
				enableTabScroll : true,
				autoScroll : true,
				layout : "border",
				items : [{
							region : 'center',
							layout : 'fit',
							autoScroll : true,
							items : [grid]
						}]
			})
	
});