Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	// var isWo = "";
	// var isMa = "";
	var tabpanel = parent.Ext.getCmp('maintab');
	// 将时间转成字符串格式
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
			CurrentDate = CurrentDate + Month + "-";
		} else {
			CurrentDate = CurrentDate + "0" + Month + "-";
		}
		if (Day >= 10) {
			CurrentDate = CurrentDate + Day;
		} else {
			CurrentDate = CurrentDate + "0" + Day;
		}
		return CurrentDate;
	}

	// 系统当前时间
	var enddate = new Date();
	// // 系统到现在前七天
	// var startdate = new Date();
	// startdate.setDate(enddate.getDate() - 7);
	// // startdate = startdate.getFirstDateOfMonth();
	// var sdate = ChangeDateToString(startdate);
	var edate = ChangeDateToString(enddate);

	// 年份

	var year = new Ext.form.TextField({
				style : 'cursor:pointer',
				id : "year",
				name : 'year',
				readOnly : true,
				anchor : "90%",
				allowBlank : true,
				fieldLabel : '项目年份',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y',
									alwaysUseStartDate : false,
									dateFmt : 'yyyy',
									onpicked : function() {
										year.clearInvalid();
									},
									onclearing : function() {
										year.markInvalid();
									}
								});
					}
				}
			});

	// 状态
	var url = "manageproject/useStauslist.action?prjStatusType=" + 2;
	var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", url, false);
	conn.send(null);
	var search_data4 = eval('([["全部",""],' + conn.responseText + '])');

	var statusBox = new Ext.form.ComboBox({
				id : "workflowStatusName",
				name : "workflowStatus",
				hiddenName : "workflowStatus",
				fieldLabel : "项目状态",
				triggerAction : 'all',
				store : new Ext.data.SimpleStore({
							fields : ['name', 'id'],
							data : search_data4
						}),
				displayField : "name",
				valueField : "id",
				emptyText : '请选择...',
				mode : 'local',
				readOnly : true,
				// allowBlank : false,
				anchor : "90%"
			});

	// 类型
	var typeBox = new Ext.ux.ComboBoxTree({
				fieldLabel : '项目类别',
				anchor : '100%',
				displayField : 'text',
				valueField : 'id',
				id : 'prjTypeName',
				// hiddenName : 'head.planOriginalId',
				// hiddenName : 'head.txtMrOriginal',
				blankText : '请选择',
				emptyText : '请选择',
				// readOnly : true,
				// anchor : "70%",
				tree : {
					xtype : 'treepanel',
					// 虚拟节点,不能显示
					rootVisible : true,
					loader : new Ext.tree.TreeLoader({
								dataUrl : 'manageproject/findByPId.action'
							}),
					root : new Ext.tree.AsyncTreeNode({
								id : '0',
								text : '全部'
							})
					// listeners : {
					// click : setTxtMrCostSpecial
					// }
				},
				selectNodeModel : 'all',
				listeners : {
					'select' : function(combo, record, index) {
						// Ext.get("txtMrOriginalH").dom.value = record
						// .get('planOriginalId');
						// Ext.get("txtMrOriginalH").dom.value =
						// record.get('planOriginalId');
						typeBox.setValue(record.get('typeBox'));
					}
				}
			});

	// 部门

	var addRec = new Ext.data.Record.create([

	{
				name : 'deptCode'

			}, {
				name : 'deptName'

			}

	])
	var repairDeptStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'equfailure/repairDept.action',
							method : 'post'
						}),
				reader : new Ext.data.JsonReader({
							root : 'list'
						}, addRec)
			});

	repairDeptStore.load();
	repairDeptStore.on('load', function() {
				this.insert(0, new addRec({

									deptCode : "",
									deptName : "全部"

								}));

			})

	var departmentsBox = new Ext.ux.ComboBoxTree({
				fieldLabel : '负责部门',
				id : 'deptId',
				displayField : 'text',
				valueField : 'id',
				hiddenName : 'empinfo.deptCode',
				blankText : '请选择',
				emptyText : '请选择',
				anchor : '100%',
				// value:{id:'0',text:'合肥电厂',attributes:{description:'deptName'}},
				tree : {
					xtype : 'treepanel',
					loader : new Ext.tree.TreeLoader({
								dataUrl : 'empInfoManage.action?method=getDep'
							}),
					root : new Ext.tree.AsyncTreeNode({
								id : '0',
								name : '合肥电厂',
								text : '合肥电厂'
							})
				},
				selectNodeModel : 'all'
			});

	// 模糊查询
	var query = new Ext.form.TextField({
		id : 'argFuzzy',
		xtype : "textfield",
		fieldLabel : "模糊查询",
		emptyText : '根据项目编码、项目名称、申请人、工程负责人 模糊查询',
		name : 'argFuzzy',
		value : '',
		anchor : '95%'
			// width : '94%'
		});
	// 按钮
	var btnQuery = new Ext.Button({
				id : 'btnQuery',
				text : '查 询',
				iconCls : 'query',
				minWidth : 80,
				handler : function() {
					con_ds.reload(
							// {
							// params : {
							// prjStatusType : 2,
							//
							// year : Ext.get('year').dom.value,
							// start : 0,
							// limit : 18,
							//
							// argFuzzy : Ext.getCmp("argFuzzy").value,
							// prjStatus : statusBox.getValue(),
							// prjTypeId : typeBox.getValue(),
							//
							// chargeDep : departmentsBox
							// .getValue()
							//
							// }
							// }
							);
				}
			});
	var btnBliud = new Ext.Button({
				id : 'btnClear',
				iconCls : 'detail',
				text : '查 看',
				minWidth : 80,
				handler : modifyBtn

			});

	var btnReport = new Ext.Button({
				id : 'btnReport',
				iconCls : 'upcommit',
				text : '上 报',
				minWidth : 80,
				handler : function() {
					var record = Grid.getSelectionModel().getSelected();
					if (record) {
						if (record.get("factStartDate") == null
								|| record.get("factEndDate") == null) {
							Ext.Msg.alert('提示', '请先填写实际工期!');
							return false;
						}
						var url = "../signProject/reportProject.jsp";
						var args = new Object();
						args.prjNo = record.get("prjjInfo.prjNo");
						args.entryId = record.get("prjjInfo.proWorkFlowNo");
						args.workflowType = "hfPCheck";
						args.prjTypeId = record.get("prjjInfo.prjTypeId");
						args.prjStatus = record.get("prjjInfo.prjStatus");
						args.params = 'check';
						// alert(Ext.encode(args))
						window
								.showModalDialog(url, args,
										'status:no;dialogWidth=750px;dialogHeight=550px');
						con_ds.load({
									params : {
										prjStatusType : 2,
										entryType : 'approve',
										year : Ext.get('year').dom.value,
										start : 0,
										limit : 18,
										argFuzzy : Ext.getCmp("argFuzzy").value,
										workorderStatus : statusBox.getValue(),
										workorderType : typeBox.value,
										repairDepartment : departmentsBox
												.getValue()

									}
								})
					} else {
						Ext.Msg.alert("提示", "请选择一条项目记录！")
					}

				}
			});
	// 票面浏览
	function ApproveRptPreview() {

		var selected = Grid.getSelectionModel().getSelections();
		var projectId;

		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要浏览的项目记录！");
		} else {

			var menber = selected[0];
			projectId = menber.get('prjjInfo.id');
			var url = "/powerrpt/report/webfile/bqmis/projectcheck.jsp?projectId="
					+ projectId;
			window.open(url);

		}
	};

	var btnRptPreview = new Ext.Button({
				iconCls : 'pdfview',
				id : 'btnReport',
				text : '验收票面预览',
				minWidth : 80,
				handler : ApproveRptPreview
			});

	var btnMeetQuery = new Ext.Button({
		id : 'btnMeetQuery',
		text : "会签查询",
		iconCls : 'view',
		handler : function() {
			var record = Grid.getSelectionModel().getSelected();
			if (record != null) {
				var entryId = record.get("prjjInfo.proWorkFlowNo")
				if (entryId == null || entryId == "") {
					url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
							+ "hfPCheck";
					window.open(url);
				} else {
					var url = "/power/workflow/manager/show/show.jsp?entryId="
							+ entryId;
					window.open(url);
				}
			} else {
				Ext.Msg.alert('提示', '请从列表中选择一条记录！');
			}
		}
	})
	var queryField = new Ext.form.FieldSet({
				title : '查询条件',
				height : '100%',

				layout : 'form',
				bodyStyle : "padding:0px 0px 0px 20px",
				items : [{
							layout : 'column',
							border : false,
							items : [{
										border : false,
										columnWidth : 0.5,
										layout : 'form',
										items : [year]
									}, {
										border : false,
										columnWidth : 0.5,
										layout : 'form',
										items : [typeBox]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										border : false,
										columnWidth : 0.5,
										layout : 'form',
										items : [statusBox]
									}, {
										border : false,
										columnWidth : 0.5,
										layout : 'form',
										items : [departmentsBox]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										border : false,
										columnWidth : 1,
										layout : 'form',
										items : [query]
									}]
						}]
			});
	var form = new Ext.form.FormPanel({
				// bodyStyle : "padding:5px 5px 0",
				labelAlign : 'left',
				id : 'shift-form',
				labelWidth : 80,
				autoHeight : true,
				region : 'center',
				layout : 'column',
				border : false,

				items : [{
							bodyStyle : "padding:20px 0px 20px 20px",

							columnWidth : 0.75,
							layout : 'form',
							border : false,
							items : [queryField]
						}, {
							bodyStyle : "padding:30px 0px 0px 40px",
							columnWidth : 0.25,
							layout : 'column',
							border : false,
							items : [{
										bodyStyle : "padding:40px	20px 20px 20px",
										columnWidth : 1,
										layout : 'form',
										border : false,
										items : [

										{
													//													
													layout : 'form',
													border : false,
													items : [btnQuery]
												}]
									}, {
										// bodyStyle : "padding:50px
										// 20px 20px 20px",
										columnWidth : 1,
										layout : 'form',
										border : false

									}
							// , {
							// // bodyStyle : "padding:50px
							// // 20px 20px 20px",
							// columnWidth : 1,
							// layout : 'column',
							// border : false,
							// items : [{
							// // ,
							// layout : 'form',
							// border : false,
							// items : [btnBliud]
							// }]
							// }, {
							// bodyStyle : "padding:20px 0px 0px 0px",
							// columnWidth : 1,
							// layout : 'column',
							// border : false,
							// items : [{
							// // ,
							// layout : 'form',
							// border : false,
							// items : [btnReport]
							// }]
							// }
							]
						}]
			});
	// /////////////////////////////////////////////////////////////////////////////

	var con_item = Ext.data.Record.create([{
				name : 'prjjInfo.prjYear'
			}, {
				name : 'prjjInfo.accWorkFlowNo'
			}, {
				name : 'prjjInfo.proWorkFlowNo'
			}, {
				name : 'prjjInfo.prjTypeId'
			}, {
				name : 'prjjInfo.prjName'
			}, {
				name : 'prjjInfo.prjNoShow'
			}, {
				name : 'prjjInfo.prjNo'
			}, {
				name : 'prjjInfo.prjStatus'
			}, {
				name : 'prjjInfo.planAmount'
			}, {
				name : 'prjjInfo.chargeBy'
			}, {
				name : 'prjjInfo.prjContent'
			}, {
				name : 'prjjInfo.planStartDate'
			}, {
				name : 'prjjInfo.planEndDate'
			}, {
				name : 'prjjInfo.planTimeLimit'
			}, {}, {
				name : 'prjjInfo.factTimeLimit'
			}, {
				name : 'prjjInfo.itemId'
			}, {
				name : 'prjjInfo.memo'
			}, {
				name : 'prjjInfo.annex'
			}, {
				name : 'prjjInfo.id'
			}, {}, {
				name : 'prjjInfo.cmlAppraisal'
			}, {
				name : 'prjjInfo.constructionChargeBy'
			}, {
				name : 'chargeByName'
			}, {
				name : 'prjTypeName'
			}, {
				name : 'changeDepName'
			}, {
				name : 'enterByName'
			}, {
				name : 'planStartDate'
			}, {}, {
				name : 'factStartDate'
			}, {
				name : 'planEndDate'
			}, {}, {
				name : 'factEndDate'
			}, {
				name : 'entryDate'
			}, {
				name : 'prjStatus'
			}, {
				name : 'constructionUnitName'
			}, {
				name : 'prjjInfo.prjChangeMemo'
			}, {
				name: 'prjjInfo.prjDataMove'
			}]);
	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true

			});
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manageproject/getProjectApplyList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, con_item)
			});
	con_ds.load({
				params : {
					prjStatusType : 2,
					entryType : 'approve',
					start : 0,
					limit : 18
				}
			})

	con_ds.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							prjStatusType : 2,
							entryType : 'approve',
							prjYear : Ext.get('year').dom.value,
							argFuzzy : query.getValue(),
							prjStatus : statusBox.getValue(),
							prjTypeId : typeBox.getValue(),
							chargeDep : departmentsBox.getValue()

						});
			});
	var con_item_cm = new Ext.grid.ColumnModel([con_sm,
			new Ext.grid.RowNumberer({
						header : '序号',
						width : 50,
						align : 'center'
					}), {
				header : '项目编号',
				dataIndex : 'prjjInfo.prjNoShow',
				align : 'center'

			}, {
				header : '项目名称',
				dataIndex : 'prjjInfo.prjName',
				align : 'center'
			}, {
				header : '项目类别',
				dataIndex : 'prjTypeName',
				align : 'center'
			}, {
				header : '项目状态',
				dataIndex : 'prjStatus',
				align : 'center'
			},

			{
				header : '项目年份',
				dataIndex : 'prjjInfo.prjYear',
				align : 'center'
			}, {
				header : '预算费用',
				dataIndex : 'prjjInfo.planAmount',
				align : 'center'
			}, {
				header : '工程负责人',
				dataIndex : 'chargeByName',
				align : 'center'
			}]);
	con_item_cm.defaultSortable = true;

	var gridbbar = new Ext.PagingToolbar({
				pageSize : 18,
				store : con_ds,
				displayInfo : true,
				displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
				emptyMsg : "没有记录",
				beforePageText : '页',
				afterPageText : "共{0}"
			});
	var Grid = new Ext.grid.GridPanel({
				ds : con_ds,
				cm : con_item_cm,
				sm : con_sm,
				// title : '合同列表',
				width : Ext.get('div_lay').getWidth(),
				split : true,
				autoScroll : true,
				bbar : gridbbar,
				tbar : [btnReport, btnBliud, btnRptPreview, btnMeetQuery],
				border : false,
				viewConfig : {
					forceFit : true
				}
			});

	Grid.on('rowdblclick', modifyBtn);
	function modifyBtn() {
		var rec = Grid.getSelectionModel().getSelected();
		if (rec) {
			parent.myRecord = rec;

			// alert(rec.get("model.prjName"));

			if (parent.document.all.iframe2 != null) {
				parent.document.all.iframe2.src = "manage/project/bussiness/projectcheck/baseInfo.jsp";
			}

			tabpanel.setActiveTab(1);
			// alert(Ext.encode(rec.get('model.planAmount')));
			// var temp=model.planAmount;
			// alert(rec.data.temp);
			// alert(rec.get("model"))
			// var woCode = rec.get('model.woCode');
			// // var woCodeShow = rec.get('model.woCodeShow')
			// var faWoCode = rec.get('model.faWoCode')
			// var Status = rec.get('model.workorderStatus');
			//
			// register.edit(woCode, faWoCode, Status);
		} else {
			Ext.Msg.alert("提示", "请选择一条项目记录！")
		}
	}
	// Grid.on('rowclick',rowclickBtn);
	// function rowclickBtn(){
	// var rec = Grid.getSelectionModel().getSelected();
	// if(rec) {
	// var woCode = rec.get('model.woCode');
	// var faWoCode = rec.get('model.faWoCode');
	// var Status = rec.get('model.workorderStatus');
	//	   
	// register.rowl(woCode,faWoCode,Status);
	// }
	// }

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				items : [{
							region : "north",
							layout : 'fit',
							height : 150,
							border : true,
							split : true,
							margins : '0 0 0 0',
							items : [form]
						}, {

							region : "center",
							layout : 'fit',
							title : '立项列表',
							border : false,

							split : true,
							margins : '0 0 0 0',
							items : [Grid]
						}]
			});

});