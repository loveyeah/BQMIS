Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
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
	// 审批状态
	// var status;

	// 培训计划类别下拉框数据源
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
									dateFmt : "yyyy-MM",
									isShowClear : false

								});
						this.blur();
					}
				}
			});

	// 培训类别
	var planTypeCombobox = new Ext.form.ComboBox({
				fieldLabel : '培训类别',
				id : 'trainingTypeName',
				name : 'trainingTypeId',
				allowBlank : true,
				style : "border-bottom:solid 1px",
				triggerAction : 'all',
				editable : false,
				store : allPlanTypeStore,
				blankText : '',
				emptyText : '',
				valueField : 'trainingTypeId',
				displayField : 'trainingTypeName',
				hiddenName : 'trainDetail.trainingTypeId',
				mode : 'local',
				width : 120
			});

			var manageDeptRec = new Ext.data.Record.create([{
		name : 'deptId'
	},{
		name : 'deptCode'
	},{
		name : 'deptName'
	}])
	var manageDeptStore = new Ext.data.JsonStore({
		url : 'manageplan/getManageDeptList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : manageDeptRec
	})
	manageDeptStore.load();
	manageDeptStore.on('load',function(){
		manageDeptStore.insert(0,new manageDeptRec({
			'deptId' : '',
			'deptCode' : '',
			'deptName' : ''
		}))
	})
	var manageDept = new Ext.form.ComboBox({
		id : 'manageDept',
		mode : 'local',
		store : manageDeptStore,
		valueField : 'deptCode',
		displayField : "deptName",
		triggerAction : 'all',
		forceSelection : true,
		typeAhead : true,
		width : 140
	})
	manageDept.on('select',function(){
		editDepcode.setValue(manageDept.getValue())
	})
	// 部门名称
//	var editDepName = new Ext.form.ComboBox({
//		fieldLabel : '部门',
//		mode : 'remote',
//		editable : false,
//		width : 120,
//		onTriggerClick : function() {
//			var args = {
//				selectModel : 'single',
//				rootNode : {
//					id : "0",
//					text : '灞桥热电厂'
//				}
//			}
//			var url = "/power/comm/jsp/hr/dept/dept.jsp";
//			var rvo = window
//					.showModalDialog(
//							url,
//							args,
//							'dialogWidth:250px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
//			if (typeof(rvo) != "undefined") {
//				editDepcode.setValue(rvo.codes);
//				editDepName.setValue(rvo.names);
//
//			}
//		}
//	});

	// 部门编码
	var editDepcode = new Ext.form.Hidden({
				name : 'editDepcode'
			});

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
				name : 'trainingTypeName'
			}, {
				name : 'fillDate'
			}, {
				name : 'trainingMonth'
			}, {
				name : 'chargeName'
			}, {
				name : 'deptName'
			}, {
				name : 'deptCode'
			}, {
				name : 'gatherId'
			}, {
				name : 'workflowNo'
			}, {
				name : 'planTypeName'
			}]);

	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false
			});
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manageplan/getPlanGatherQueryList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, con_item)
			});

	function queryRec() {
		var this_title = "<center><font color='black' size='2'>"
				+ planDate.getValue().substring(0, 4) + '年'
				+ planDate.getValue().substring(5, 7) + "月厂各部门月度培训计划</center>";
		Grid.setTitle(this_title);
		con_ds.baseParams = {
			planTime : planDate.getValue(),
			editDepcode : editDepcode.getValue(),
			planType : planTypeCombobox.getValue(),
		//各部门审批通过了的计划
			 approve : "signStatus"
			 // add by liuyi 20100427 汇总查询，isGatherQuery 为1，查询时查询已汇总过的数据
			 ,isGatherQuery : '1'
		}
		con_ds.load({
					params : {
						start : 0,
						limit : 18
					}
				})
	}
	var con_item_cm = new Ext.grid.ColumnModel([con_sm, {
		header : "部门",
		width : 100,
		sortable : false,
		align : 'center',
		dataIndex : 'deptName',
		renderer : function(value, matedata, record, rowIndex, colIndex, store) {
			if (record && rowIndex > 0) {
				if (con_ds.getAt(rowIndex).get('deptName') == con_ds
						.getAt(rowIndex - 1).get('deptName')
						|| con_ds.getAt(rowIndex).get('deptName') == '')
					return '';
			}
			return value;
		}

	}, {
		header : "培训类别",
		width : 100,
		sortable : false,
		align : 'center',
		dataIndex : 'planTypeName'
	}, {
		header : "培训项目计划",
		width : 200,
		sortable : false,
		dataIndex : 'trainDetail.trainingName'
	}, {
		header : "计划人数",
		width : 100,
		sortable : false,
		align : 'center',
		dataIndex : 'trainDetail.trainingNumber'
	}, {
		header : "负责人",
		width : 150,
		sortable : false,
		align : 'center',
		dataIndex : 'trainDetail.chargeBy'
	}, {
		header : "培训课时",
		width : 150,
		sortable : false,
		align : 'center',
		dataIndex : 'trainDetail.trainingHours'
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

	// tbar
	var contbar = new Ext.Toolbar({
				items : ['计划时间：', planDate, '-', '部门：', manageDept, '-',
						'培训类别：', planTypeCombobox, '-', {
							id : 'query',
							iconCls : 'query',
							text : "查询",
							handler : queryRec
						}, '-', {
							id : 'btnapprove',
							text : "审批信息",
							iconCls : 'approve',
							handler : approveQuery
						}, '-', {
							id : 'btnexport',
							text : "导出",
							iconCls : 'export',
							handler : exportRec
						}]
			});

	var title = "<center><font color='black' size='2'>"
			+ planDate.getValue().substring(0, 4) + '年'
			+ planDate.getValue().substring(5, 7) + "月厂各部门月度培训计划</center>";

	var Grid = new Ext.grid.GridPanel({
				sm : con_sm,
				region : 'center',
				ds : con_ds,
				layout : 'fit',
				cm : con_item_cm,
				autoScroll : true,
				bbar : gridbbar,
				title : title,
				border : true,
				viewConfig : {
					forceFit : false
				}
			});

	new Ext.Viewport({
				autoHeight : true,
				layout : 'border',
				fitToFrame : true,
				items : [{
							region : 'north',
							height : 25,
							items : [contbar],
							style : 'padding-bottom:0.8px'
						}, Grid]
			})

	queryRec();

	// add by liuyi 091216
	function approveQuery() {
		Ext.Ajax.request({
//			url : 'manageplan/getCompleteDescription.action',
			url : 'manageplan/getPlanGatherDesc.action',
			method : 'post',
			params : {
				month : planDate.getValue()
			},
			success : function(response, action) {
				var obj = Ext.util.JSON.decode(response.responseText);
				var url = '';
				if (obj) {
					if (obj.entryId == null || obj.entryId == 'null') {
						url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
//								+ "bpTrainPlanBackGather";
						+ "bpTrainPlanGather";
						window.open(url);;
					} else {
						var url = "/power/workflow/manager/show/show.jsp?entryId="
								+ obj.entryId;
						window.open(url);
					}
				}

			},
			failure : function(response, action) {
			}
		})
	}

	// 导出
	function tableToExcel(tableHTML) {
		window.clipboardData.setData("Text", tableHTML);
		try {
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExApp.DisplayAlerts = false;
			ExApp.visible = true;
		} catch (e) {
			if (e.number != -2146827859)
				alert("您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}

	// add by drdu 20100104
	function exportRec() {
		// Ext.Ajax.request({
		// url : 'manageplan/getPlanGatherQueryList.action',
		// params : {
		// planTime : planDate.getValue(),
		// editDepcode : editDepcode.getValue(),
		// planType : planTypeCombobox.getValue()
		// },
		// success : function(response) {
		// var json = eval('(' + response.responseText.trim() + ')');
		// var records = json.list;
		// //alert(records.length)
		// var html = ['<table
		// border=1><tr><th>部门</th><th>培训类别</th><th>培训项目计划</th><th>计划人数</th><th>培训课时</th><th>负责人</th></tr>'];
		// for (var i = 0; i < records.length; i += 1) {
		// var rc = records[i];
		// html.push('<tr><td>' + rc.deptName + '</td><td>'
		// + rc.planTypeName + '</td><td>' + rc.trainDetail.trainingName
		// + '</td><td>' + rc.trainDetail.trainingNumber + '</td><td>'
		// + rc.trainDetail.trainingHours + '</td><td>' +
		// rc.trainDetail.chargeBy
		// + '</td></tr>');
		// }
		// html.push('</table>');
		// html = html.join(''); // 最后生成的HTML表格
		// // alert(html);
		// tableToExcel(html);
		// },
		// failure : function(response) {
		// Ext.Msg.alert('信息', '失败');
		// }
		// });
		var month = planDate.getValue().substring(0, 4) + "-"
				+ planDate.getValue().substring(5, 7);
		var url = "/powerrpt/frameset?__report=bqmis/itemPlanDep.rptdesign";
		url += "&__action=print&month=" + month + "&__format=xls";
		window.open(url);
	}
})