Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	var flag = false;

	function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";
		// 初始化时间
		Year = DateIn.getYear();
		Month = DateIn.getMonth() + 1;
		Day = DateIn.getDate();
		CurrentDate = Year + "-";
		if (Month >= 10) {
			CurrentDate = CurrentDate + Month;
		} else {
			CurrentDate = CurrentDate + "0" + Month;
		}
		return CurrentDate;
	}
	var enddate = new Date();
	var startdate = enddate.add(Date.MONTH, 1);
	startdate = startdate.getFirstDateOfMonth();

	// 计划时间
	var planDate = new Ext.form.TextField({
				name : 'planTime',
				fieldLabel : '计划时间',
				value : ChangeDateToString(startdate),
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '',
									alwaysUseStartDate : true,
									dateFmt : "yyyy-MM"
									// add by liuyi 091214 清空按钮隐藏
									,
									isShowClear : false
								});
						this.blur();
					}
				}
			});

	/** 右边的grid * */
	var scrwidth = document.body.clientWidth;

	var MyRecord = Ext.data.Record.create([sm, {
				name : 'trainDetail.trainingDetailId'
			}, {
				name : 'trainDetail.trainingName'
			}, {
				name : 'trainDetail.trainingLevel'
			}, {
				name : 'trainDetail.trainingNumber'
			}, {
				name : 'trainDetail.trainingHours'
			}, {
				name : 'trainDetail.chargeBy'
			}, {
				name : 'trainDetail.trainingDep'
			}, {
				name : 'trainDetail.fillBy'
			}, {
				name : 'trainDetail.trainingTypeId'
			}, {
				// modified by liuyi 该处有问题
				// name : 'trainingTypeName'planTypeName
				name : 'planTypeName'
			}, {
				name : 'fillDate'
			}, {
				name : 'trainingTypeName'
			}, {
				name : 'chargeName'
			}, {
				name : 'gatherId'
			}
			// add by liuyi 091214
			, {
				name : 'deptName'
			}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'manageplan/trainPlanGatherAfter.action '
			});

	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, MyRecord);

	var store = new Ext.data.Store({
				proxy : dataProxy,
				reader : theReader
			});

	store.load({
				params : {
					planTime : planDate.getValue()
					// modified by liuyi 091214 页面无分页栏
					// ,
					// start : 0,
					// limit : 18
				}
			});

	var sm = new Ext.grid.CheckboxSelectionModel();

	number = new Ext.grid.RowNumberer({
				header : "",
				align : 'left'
			})

	// 上报
	function addRecord() {
		if (store.getCount() == 0) {
			Ext.Msg.alert("提示", "无上报数据");
			return false;
		}
		var url = "reportsign.jsp";
		var gatherId = store.getAt(0).get("gatherId");
		var workFlowNo = store.getAt(0).get("workFlowNo");
		var args = new Object();
		args.entryId = workFlowNo;
		args.gatherId = gatherId;
		args.workflowType = "bpTrainPlanGather";
		var obj = window.showModalDialog(url, args,
				'status:no;dialogWidth=770px;dialogHeight=550px');
		if (obj) {
			init();
			query();
		}
	}
	// add by drdu 20100113
	function updateRecordGather() {
		flag = true;
	}
	// add by drdu 20100113
	function saveRecordGather() {
		grid.stopEditing();
		var modifyRec = grid.getStore().getModifiedRecords();
		
		if (modifyRec.length > 0 || ids.length > 0) {
			if (!confirm("确定要保存修改吗?"))
				return;
			var updateData = new Array();
			for (var i = 0; i < modifyRec.length; i++) {
				var rec = modifyRec[i];
				
				if (rec.get("trainDetail.trainingNumber") == null
						|| (rec.get("trainDetail.trainingNumber") == "" && rec
								.get("trainDetail.trainingNumber") != "0")) {

					Ext.MessageBox.alert('提示', '计划人数不能为空！')
					return
				}
				if (rec.get("trainDetail.trainingLevel")==2&&rec.get("trainDetail.chargeBy") == null
						|| rec.get("trainDetail.chargeBy") == "") {

					Ext.MessageBox.alert('提示', '负责人不能为空！')
					return
				}
				updateData.push({
							id : rec.get("trainDetail.trainingDetailId"),
							trainingTypeId : rec
									.get("trainDetail.trainingTypeId"),
							trainingName : rec.get("trainDetail.trainingName"),
							trainingNumber : rec
									.get("trainDetail.trainingNumber"),
							trainingHours:	rec.get("trainDetail.trainingHours"),
							chargeBy : rec.get("trainDetail.chargeBy")
						});
			}
			
			Ext.Ajax.request({
						url : 'manageplan/updateDeptGatherRecord.action',
						method : 'post',
						params : {
							isUpdate : Ext.util.JSON.encode(updateData)
						},
						success : function(result, request) {
							var o = eval('(' + result.responseText + ')');
							Ext.MessageBox.alert('提示信息', o.msg);
							flag = false;
							store.rejectChanges();
							init();
							query();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('提示信息', '未知错误！')
						}
					})
		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}

	// 培训计划类别下拉框数据源 add by drdu 20100105
	var planType = Ext.data.Record.create([{
				name : 'trainingTypeName'
			}, {
				name : 'trainingTypeId'
			}]);

	var allPlanTypeStore = new Ext.data.JsonStore({
				url : 'manageplan/getAllTypeList.action',
				root : 'list',
				fields : planType
			});
	allPlanTypeStore.load();

	var planTypeComboboxGather = new Ext.form.ComboBox({
				fieldLabel : '培训类别',
				id : 'trainingTypeId',
				name : 'planTypeName',
				allowBlank : true,
				triggerAction : 'all',
				editable : false,
				store : allPlanTypeStore,
				valueField : 'trainingTypeId',
				displayField : 'trainingTypeName',
				hiddenName : 'trainDetail.trainingTypeId',
				mode : 'local'
			});

	// 定义grid
	var grid = new Ext.grid.EditorGridPanel({
		// region : "west",
		el : 'wait-role-div',
		store : store,
		layout : 'fit',
		// width:'0.5',trainDetail.trainingName
		columns : [sm, // 选择框
				number,
				// add by liuyi 091214
				{
					header : '部门',
					width : 80,
					align : 'center',
					dataIndex : 'deptName',
					renderer : function(value, matedata, record, rowIndex,
							colIndex, store) {
						if (record && rowIndex > 0) {
							if (store.getAt(rowIndex).get('deptName') == store
									.getAt(rowIndex - 1).get('deptName')
									|| store.getAt(rowIndex).get('deptName') == '')
								return '';
						}
						return value;
					}
				}, {
					header : '类别',
					width : 90,
					align : 'center',
					// dataIndex : 'planTypeName',
					dataIndex : 'trainDetail.trainingTypeId',
					editor : planTypeComboboxGather,
					renderer : function(v) {
						for (var i = 0; i < allPlanTypeStore.getCount(); i++) {
							var rec = allPlanTypeStore.getAt(i);
							if (rec.get("trainingTypeId") == v) {
								return rec.get("trainingTypeName");
							}
						}
						return v;
					}
				}, {
					header : "项目级别",
					width : 100,
					hidden : true,
					sortable : false,
					dataIndex : 'trainDetail.trainingLevel',
					renderer : function(v) {
						if (v == 1) {
							return "合计";
						}
						if (v == 2) {
							return "明细";
						}
					}
				}, {
					header : "计划培训项目",
					width : 180,
					sortable : false,
					dataIndex : 'trainDetail.trainingName',
					editor : new Ext.form.TextField()
				}, {
					header : "计划人数",
					width : 60,
					align : 'center',
					sortable : false,
					dataIndex : 'trainDetail.trainingNumber',
					editor : new Ext.form.NumberField()
					// renderer : getlevelName
			}	, {
					header : "培训课时",
					width : 100,
					align : 'center',
					sortable : false,
					dataIndex : 'trainDetail.trainingHours',
					editor : new Ext.form.NumberField()
				}, {
					header : "负责人",
					width : 90,
					align : 'center',
					sortable : false,
					dataIndex : 'trainDetail.chargeBy',
					editor : new Ext.form.TextField()
				}],
		tbar : [{
					text : "上报",
					id : 'upcommit',
					iconCls : 'upcommit',
					handler : addRecord
				}, {
					text : "修改",
					id : 'btnUpdateGather',
					iconCls : 'update',
					handler : updateRecordGather
				}, {
					text : "保存",
					id : 'btnSaveGather',
					iconCls : 'save',
					handler : saveRecordGather
				},{
					text : "导出",
					id : 'btnExport',
					iconCls : 'save',
					handler : infoExport
				}],
		sm : sm
	});

	grid.on('beforeedit', function(e) {
				if (flag)
					return true;
				else
					return false;
			})

	/** 左边的grid * */
	var con_item = Ext.data.Record.create([{
				name : 'trainDetail.trainingDetailId'
			}, {
				name : 'trainDetail.trainingMainId'
			}, {
				name : 'trainDetail.trainingTypeId'
			}, {
				name : 'trainDetail.trainingName'
			}, {
				name : 'trainDetail.trainingLevel'
			}, {
				name : 'trainDetail.trainingNumber'
			}, {
				name : 'trainDetail.trainingHours'
			}, {
				name : 'trainDetail.chargeBy'
			}, {
				name : 'trainDetail.trainingDep'
			}, {
				name : 'trainDetail.fillBy'
			}, {
				name : 'planTypeName'
			}, {
				name : 'fillDate'
			}, {
				name : 'trainingTypeName'
			}, {
				name : 'chargeName'
			}, {
				name : 'deptName'
			}, {
				name : 'deptCode'
			}]);

	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false

			});
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manageplan/getPlanTrainGather.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, con_item)
			});
	con_ds.load({
				params : {
					planTime : planDate.getValue()
					// modified by liuyi 091214 页面无分页栏
					// ,
					// start : 0,
					// limit : 18
				}
			})

	// 培训计划类别下拉框数据源 add by drdu 20100105
	var planType = Ext.data.Record.create([{
				name : 'trainingTypeName'
			}, {
				name : 'trainingTypeId'
			}]);

	var allPlanTypeStore = new Ext.data.JsonStore({
				url : 'manageplan/getAllTypeList.action',
				root : 'list',
				fields : planType
			});
	allPlanTypeStore.load();

	var planTypeCombobox = new Ext.form.ComboBox({
				fieldLabel : '培训类别',
				id : 'trainingTypeId',
				name : 'planTypeName',
				allowBlank : true,
				triggerAction : 'all',
				editable : false,
				store : allPlanTypeStore,
				valueField : 'trainingTypeId',
				displayField : 'trainingTypeName',
				hiddenName : 'trainDetail.trainingTypeId',
				mode : 'local'
			});

	var con_item_cm = new Ext.grid.ColumnModel([con_sm,
			// add by liuyi 091214
			new Ext.grid.RowNumberer({
						header : '',
						align : 'left'
					}), {
				header : "trainingDetailId",
				align : 'center',
				sortable : false,
				hidden : true,
				dataIndex : 'trainDetail.trainingDetailId'
			}, {
				header : "部门",
				width : 80,
				align : 'center',
				sortable : false,
				dataIndex : 'deptName',
				renderer : function(value, matedata, record, rowIndex,
						colIndex, store) {
					if (record && rowIndex > 0) {
						if (con_ds.getAt(rowIndex).get('deptName') == con_ds
								.getAt(rowIndex - 1).get('deptName')
								|| con_ds.getAt(rowIndex).get('deptName') == '')
							return '';
					}
					return value;
				}
			}, {
				header : "类别",
				width : 80,
				align : 'center',
				sortable : false,
				dataIndex : 'trainDetail.trainingTypeId',
				editor : planTypeCombobox,
				renderer : function(v) {
					for (var i = 0; i < allPlanTypeStore.getCount(); i++) {
						var rec = allPlanTypeStore.getAt(i);
						if (rec.get("trainingTypeId") == v) {
							return rec.get("trainingTypeName");
						}
					}
					return v;
				}
			}, {
				header : "项目级别",
				width : 100,
				sortable : false,
				hidden : true,
				dataIndex : 'trainDetail.trainingLevel',
				renderer : function(v) {
					if (v == 1) {
						return "合计";
					}
					if (v == 2) {
						return "明细";
					}
				}
			}, {
				header : "计划培训项目",
				width : 170,
				sortable : false,
				dataIndex : 'trainDetail.trainingName',
				editor : new Ext.form.TextField()
			}, {
				header : "计划人数",
				width : 60,
				align : 'center',
				sortable : false,
				dataIndex : 'trainDetail.trainingNumber',
				editor : new Ext.form.NumberField()
				// renderer : getlevelName
		},
		{
				header : "培训课时",
				width : 60,
				align : 'center',
				sortable : false,
				dataIndex : 'trainDetail.trainingHours',
				editor : new Ext.form.NumberField()
				
				// renderer : getlevelName
		}	,
			{
				header : "负责人",
				width : 80,
				align : 'center',
				sortable : false,
				dataIndex : 'trainDetail.chargeBy',
				editor : new Ext.form.TextField()
			}]);
	con_item_cm.defaultSortable = true;
	var gridbbar = new Ext.PagingToolbar({
				pageSize : 18,
				store : con_ds,
				displayInfo : true,
				displayMsg : "显示第{0}条到{1}条，共{2}条",
				emptyMsg : "没有记录",
				beforePageText : '',
				afterPageText : ""
			});
	function query() {
		var date = planDate.getValue();
		con_ds.load({
					params : {
						planTime : date
						// modified by liuyi 091214 页面无分页栏
						// ,
						// start : 0,
						// limit : 18
					}
				})
		store.load({
					params : {
						planTime : date
						// modified by liuyi 091214 页面无分页栏
						// ,
						// start : 0,
						// limit : 18
					}
				})
		init();
		flag = false;
	}
	// 增加
	var contbar = new Ext.Toolbar({
				items : ['计划时间', planDate, {
							id : 'query',
							iconCls : 'query',
							text : "查询",
							handler : query
						}, {
							id : 'gather',
							iconCls : 'query',
							text : "汇总",
							handler : function() {
								var records = Grid.selModel.getSelections();
								var recordslen = records.length;
								var mainIds = new Map();
								if (recordslen > 0) {
									for (var i = 0; i <= recordslen - 1; i++) {
										var trainId = records[i]
												.get("trainDetail.trainingMainId");
										if (!mainIds.containsKey(trainId)) {
											mainIds.put(trainId, null);
										}
									}
								} else {
									Ext.Msg.alert("提示", "请选择汇总数据");
									return false;
								}
								// 汇总处理
								Ext.lib.Ajax.request('POST',
										'manageplan/trainPlanGather.action', {
											success : function(action) {
												Ext.Msg.alert("提示", "汇总成功");
												init();
												query();
											}
										}, "mainIds=" + mainIds.keys()
												+ "&planTime="
												+ planDate.getValue());

							}
						}, {
							id : 'btnUpdate',
							iconCls : 'update',
							text : "修改",
							handler : updateRecord
						}, {
							id : 'btnSave',
							iconCls : 'save',
							text : "保存",
							handler : saveRecord
						}]

			});
	var Grid = new Ext.grid.EditorGridPanel({
				el : 'role-div',
				sm : con_sm,
				ds : con_ds,
				cm : con_item_cm,
				height : 425,
				autoScroll : true,
				// bbar : gridbbar,
				tbar : contbar,
				border : true,
				clicksToEdit : 1
			});
	Grid.render();
	Grid.on('rowclick', modifyBtn);
	// add by drdu 20100105
	Grid.on('beforeedit', function(e) {
				if (flag)
					return true;
				else
					return false;
			})

	function modifyBtn() {
		var recL = Grid.getSelectionModel().getSelected();

		if (recL) {
			if (recL.get("topicCode") != null) {
				store.load({
							params : {
								topicCode : recL.get("topicCode")
								// modified by liuyi 091214 页面无分页栏
								// ,
								// start : 0,
								// limit : 18
							}
						});

			}

		} else {
			Ext.Msg.alert("提示", "请选择左边的一条记录！")
		}
	}

	function init() {
		Ext.Ajax.request({
					method : 'post',
					url : 'manageplan/trainPlanGatherAfter.action',
					params : {
						planTime : planDate.getValue()
					},
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.list.length > 0) {
							var str = Ext.encode(result.list);
							var ob = eval("("
									+ str.substring(1, str.length - 1) + ")")
							if (ob.workflowStatus == 0
									|| ob.workflowStatus == 3) {
								Ext.get("gather").dom.disabled = false;
								Ext.get("upcommit").dom.disabled = false;
								Ext.get("btnUpdate").dom.disabled = false;
								Ext.get("btnSave").dom.disabled = false;
								Ext.get("btnUpdateGather").dom.disabled = false;
								Ext.get("btnSaveGather").dom.disabled = false;
							} else {
								Ext.get("gather").dom.disabled = true;
								Ext.get("upcommit").dom.disabled = true;
								Ext.get("btnUpdate").dom.disabled = true;
								Ext.get("btnSave").dom.disabled = true;
								Ext.get("btnUpdateGather").dom.disabled = true;
								Ext.get("btnSaveGather").dom.disabled = true;
							}
						} else {
							Ext.get("gather").dom.disabled = false;
							Ext.get("btnUpdate").dom.disabled = false;
							Ext.get("btnSave").dom.disabled = false;
							Ext.get("btnUpdateGather").dom.disabled = true;
							Ext.get("btnSaveGather").dom.disabled = true;
							Ext.get("upcommit").dom.disabled = true;
						}
					}
				});
	}

	// add by drdu 20100105
	function updateRecord() {
		flag = true;
	}
	// add by drdu 20100105
	var ids = new Array();
	function saveRecord() {
		grid.stopEditing();
		var modifyRec = Grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			if (!confirm("确定要保存修改吗?"))
				return;
			var updateData = new Array();
			for (var i = 0; i < Grid.getStore().getCount(); i++) {
				var rec = Grid.getStore().getAt(i);
				if (rec.get("trainDetail.trainingNumber") == null
						|| (rec.get("trainDetail.trainingNumber") == "" && rec
								.get("trainDetail.trainingNumber") != "0")) {

					Ext.MessageBox.alert('提示', '计划人数不能为空！')
					return
				}
				if (rec.get("trainDetail.chargeBy") == null
						|| rec.get("trainDetail.chargeBy") == "") {

					Ext.MessageBox.alert('提示', '负责人不能为空！')
					return
				}
				updateData.push({
							id : rec.get("trainDetail.trainingDetailId"),
							trainingTypeId : rec
									.get("trainDetail.trainingTypeId"),
							trainingName : rec.get("trainDetail.trainingName"),
							trainingNumber : rec
									.get("trainDetail.trainingNumber"),
							trainingHours:	rec.get("trainDetail.trainingHours"),
							chargeBy : rec.get("trainDetail.chargeBy")
						});
			}
			Ext.Ajax.request({
						url : 'manageplan/updateDeptGatherRecord.action',
						method : 'post',
						params : {
							isUpdate : Ext.util.JSON.encode(updateData)
						},
						success : function(result, request) {
							var o = eval('(' + result.responseText + ')');
							Ext.MessageBox.alert('提示信息', o.msg);
							flag = false;
							con_ds.rejectChanges();
							query();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('提示信息', '未知错误！')
						}
					})
		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}

	init();
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [{
							bodyStyle : "padding: 1,1,1,1",
							layout : 'fit',
							border : false,
							frame : false,
							region : "west",
							width : '50%',
							items : [Grid]
						}, {
							bodyStyle : "padding: 1,1,1,0",
							region : "center",
							border : false,
							frame : false,
							layout : 'fit',
							items : [grid]
						}]
			})
			
			
	//add by fyyang 20100628 导出
						// 导出
	function tableToExcel(tableHTML) {
		window.clipboardData.setData("Text", tableHTML);
//		var elTable = document.getElementById("export_div");
//		elTable.innerHTML = tableHTML;
//		var oRangeRef = document.body.createTextRange();
//		oRangeRef.moveToElementText( elTable ); 
//		oRangeRef.execCommand( "Copy" );
		try {
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExWSh.Columns("A").ColumnWidth  = 15;
			ExWSh.Columns("B").ColumnWidth  = 15;
			ExWSh.Columns("C").ColumnWidth  = 40;
//			ExWSh.Cells.NumberFormatLocal = "@";
			ExApp.DisplayAlerts = false;
			ExApp.visible = true;
		} catch (e) {
			if (e.number != -2146827859)
				alert("您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}
	function infoExport()
	{
		if(store.getTotalCount()>0)
		{
			var html = ['<table border=0>'];
			html.push("<tr><td>部门</td><td>类别</td><td>计划培训项目</td><td>计划人数</td><td>培训课时</td><td>负责人</td></tr>");
			for(var i=0;i<store.getTotalCount();i++)
			{
			 var deptName=store.getAt(i).get("deptName");
			 if(i>0&&store.getAt(i-1).get("deptName")==deptName)
			 {
			 	deptName="";
			 }
			 
			 var trainingTypeName="";
			
			 for (var j = 0; j < allPlanTypeStore.getCount(); j++) {
							var rec = allPlanTypeStore.getAt(j);
							if (rec.get("trainingTypeId") == store.getAt(i).get("trainDetail.trainingTypeId")) {
								trainingTypeName= rec.get("trainingTypeName");
							}
						}
			 html.push("<tr><td>"+deptName+"</td><td>"+trainingTypeName+"</td>" );
			 html.push("<td>"+store.getAt(i).get("trainDetail.trainingName")+"</td><td>"+store.getAt(i).get("trainDetail.trainingNumber")+"</td>" );
			 html.push("<td>"+store.getAt(i).get("trainDetail.trainingHours")+"</td><td>"+store.getAt(i).get("trainDetail.chargeBy")+"</td>" );
			 html.push("</tr>");
			 
			}
			
			
			   html.push('</table>');
				html = html.join(''); // 最后生成的HTML表格
				
				tableToExcel(html);
		}
		else
		{
		   Ext.Msg.alert("提示","无数据导出!");	
		}
	}
	
	

})