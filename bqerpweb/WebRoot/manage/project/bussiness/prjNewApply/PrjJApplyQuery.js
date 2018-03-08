var start=0;
var limit=18;
var StartAndEndDate;
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
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
	var date = new Date();
	var startdate = date.add(Date.DAY, -60);
	var enddate = date;
	var sdate = ChangeDateToString(startdate);
	var edate = ChangeDateToString(enddate);
	var fromDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : 'startDate',
		id : 'fromDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		checked : true,
		value : sdate,
		emptyText : '请选择',
		width : 110
	});
	var toDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : 'endDate',
		value : edate,
		id : 'toDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		checked : true,
		emptyText : '请选择',
		width : 110
	});
	//项目名称输入
	var projectName = new Ext.form.TextField({
				id : "projectName",
				name : "projectName",
				width : 150,
				emptyText : ""
			});
	//删除
	function deletePrj() {
		if (con_sm.hasSelection()) {
			Ext.Msg.confirm('提示', '确认要删除数据吗？', function(buttonId) {
				if (buttonId == 'yes') {
					var recode=[];
					var sm = Grid.getSelectionModel();
					var selected = sm.getSelections();
					var ids=new Array();
					for (var i = 0; i < selected.length; i += 1) {
						recode=selected[i];
						ids.push(recode.get('applyId'));
					};
						Ext.Ajax.request({
									url : 'manageproject/deletePrjApplyByIds.action',
									method : 'post',
									params : {
										ids : ids.join(",")
									},
									success : function(response, options) {
										if (response && response.responseText) {
											var res = Ext
													.decode(response.responseText)
											Ext.Msg.alert('提示', res.msg);
											con_ds.reload();
										}
									},
									failure : function(response, options) {
										Ext.Msg.alert('提示', '删除出现异常！')
									}
								});
					}else{
						Ext.Msg.alert('提示', '操作成功！')
					}
					}
			)
	} else
			Ext.Msg.alert('提示', '请先选择要删除的数据！')
	};
	//增加
	function add() {
						tabs.setActiveTab(1);
						trainRegister.resetInputField();
					
	};
	//修改
	function update() {
				var selrows = Grid.getSelectionModel().getSelections();
					if (selrows.length == 1) {
					var record = Grid.getSelectionModel().getSelected();
						tabs.setActiveTab(1);
						trainRegister.setTrainId(record);
				} else {
					Ext.Msg.alert('提示', '请选择一条记录进行修改！');
				}
			};
	// 上报
	function reportBtn() {
		var sm = Grid.getSelectionModel();
		var selected = sm.getSelections();
		var noticeNo;
		//var isReport;
		if (selected.length != 1) {
			Ext.Msg.alert("系统提示信息", "请选择其中一项进行上报！");
		} else {
			var menber = selected[0];
			noticeNo = menber.get('applyId');
				var args=new Object();
				args.busiNo=noticeNo;
				args.flowCode="prjApply";
				args.entryId="";
				
				args.title="项目开工申请单上报";
				  var danger = window.showModalDialog('reportSign.jsp',
                args, 'dialogWidth=700px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');
               query();
	}
	}
	//查询
	function query() {
	con_ds.baseParams = {
			ContractName : projectName.getValue(),
			status:"0"
	};
	con_ds.load({
					params : {
						ContractName : projectName.getValue(),
						status:"0",
						start : start,
						limit : limit
					}
				})
	};
	//查询按钮
	var queryB=new Ext.Button({
			id : 'btnQuery',
			text : "查询",
			iconCls : 'query',
			handler : query
	});
	//增加按钮
	var addB=new Ext.Button({
			id : 'btnAdd',
			text : "增加",
			iconCls : 'add',
			handler : add
	});
	//删除按钮
	var deleteB=new Ext.Button({
			id : 'btnDelete',
			text : "删除",
			iconCls : 'delete',
			handler : deletePrj
	});
	//修改按钮
	var updateB=new Ext.Button({
			id : 'btnUpdate',
			text : "修改",
			iconCls : 'update',
			handler : update
	});
	
	//上报按钮
	var reportB=new Ext.Button({
			id : 'btnReport',
			text : "上报",
			iconCls : 'upcommit',
			handler : reportBtn
	});
	//项目开工申请单列表工具栏
	var contbar = new Ext.Toolbar({
		items : ['项目名称：',projectName, '-',queryB, '-', addB, '-',updateB, '-',deleteB, '-',reportB]
	});
	var record = Ext.data.Record.create([{
		id : 'applyId',
		name : 'applyId',
		mapping : 0
	}, {
		name : 'contractName'
		,mapping : 1
	}, {
		name : 'conId'
		,mapping : 2
	}, {
		name : 'startDate'
		,mapping : 3
	}, {
		name : 'endDate'
		,mapping : 4
	}, {
		name : 'contractorName'
		,mapping : 5
	}, {
		name : 'contractorId'
		,mapping : 6
	}, {
		name : 'chargeBy'
		,mapping : 7
	}, {
		name : 'testResult'
		,mapping : 8
	}, {
		name : 'authorizeItem'
		,mapping : 9
	}, {
		name : 'personRegister'
		,mapping : 10
	}, {
		name : 'articleRegister'
		,mapping : 11
	}, {
		name : 'idCard'
		,mapping : 12
	}, {
		name : 'operateCard'
		,mapping : 13
	}, {
		name : 'cautionMoney'
		,mapping : 14
	}, {
		name : 'handInCadr'
		,mapping : 15
	}, {
		name : 'enterBy'
		,mapping : 16
	}, {
		name : 'EntryDate'
		,mapping : 17
	}, {
		name : 'workFlowNo'
		,mapping : 18
	}, {
		name : 'statusId'
		,mapping : 19
	}, {
		name : 'entryByCode'
		,mapping : 20
	}, {
		name : 'prjId'
		,mapping : 21
	}, {
		name : 'prjName'
		,mapping : 22
	}]);
	//单选框
	var con_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			rowselect : function(sm, row, rec) {
			}
		}
	});
	var con_item_cm = new Ext.grid.ColumnModel([con_sm,{
				header : '立项名称',
				dataIndex : 'prjName',
				align : 'center',
				width :200
			},
			{
				header : '项目名称',
				dataIndex : 'contractName',
				align : 'center',
				width :200
			}, {
				header : '工程开始时间',
				id:'startDate',
				dataIndex : 'startDate',
				align : 'center',
				hidden: true,
				renderer : function(v) {
					if(v!=null&&v!=""){
					v = v.substring(0,10);
					var vtemp  = v.split('-');
					if (vtemp.length!=3) {
						return false;
					}
					v = vtemp[0]+'年'+vtemp[1]+'月'+vtemp[2]+'日';
					StartAndEndDate=v;
					}
				}
			},{
				header : '工程结束时间',
				id:'startDate',
				dataIndex : 'endDate',
				align : 'center',
				hidden: true,
				renderer : function(v) {
					if(v!=null&&v!=""){
					v = v.substring(0,10);
					var vtemp  = v.split('-');
					if (vtemp.length!=3) {
						return false;
					}
					v = vtemp[0]+'年'+vtemp[1]+'月'+vtemp[2]+'日';
					StartAndEndDate+="-"+v;
					}
				}
			}, {
				header : '工程起始时间',
				//dataIndex : 'clientName',
				width: 250,
				align : 'center',
				renderer : function(v) {
				  return StartAndEndDate;
				}
			}, {
				header : '承包单位名称',
				dataIndex : 'contractorName',
				align : 'center'
				
			}, {
				header : '承包方负责人',
				dataIndex : 'chargeBy',
				align : 'center'
			}]);
		
	con_item_cm.defaultSortable = true;
	var con_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'manageproject/finPrjApplyByContractNameAndStatus.action'
		}),
		reader : new Ext.data.JsonReader({
			root : "list",
		totalProperty : "totalCount"
		}, record)
	});
	//底部分页工具栏
	var gridbbar = new Ext.PagingToolbar({
		pageSize : limit,
		store : con_ds,
		displayInfo : true,
		displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
		emptyMsg : "没有记录",
		beforePageText : '页',
		afterPageText : "共{0}"
	});
	//项目开工申请单列表
	var Grid = new Ext.grid.GridPanel({
		ds : con_ds,
		cm : con_item_cm,
		sm : con_sm,
		title : '项目开工申请单列表',
		width : Ext.get('div_lay').getWidth(),
		split : true,
		autoScroll : true,
		bbar : gridbbar,
		tbar : contbar,
		border : false,
		viewConfig : {
			forceFit : true
		}
	});
	Grid.on('rowdblclick', function(grid, rowIndex, e) {
		var record = Grid.getSelectionModel().getSelected();
						tabs.setActiveTab(1);
						trainRegister.setTrainId(record);
	});
	//query();
	var trainRegister = new trainMaint.trainRegister();
	trainRegister.resetInputField();
	var tabs = new Ext.TabPanel({
				activeTab : 1,
				tabPosition : 'bottom',
				plain : false,
				defaults : {
					autoScroll : true
				},
				items : [ {
							id : 'trainList',
							title : '开工申请单列表',
							autoScroll : true,
							items : Grid,
							layout : 'fit'
						},{
							id : 'trainRegister',
							title : '开工申请单填写',
							items : trainRegister.panel,
							autoScroll : true,
							layout : 'fit'
						}]
			});
	tabs.on('tabchange',function(thisTab,newTab) {
		var Id=newTab.getId();
		if (Id=='trainList') {
			query();
		}else{
			//trainRegister.resetInputField();
		}
	})		
//	 tabs.on('tabchange', function(tab, newtab) {
//		if (newtab.getId() == 'trainRegister') {
//			trainRegister.resetInputField();
//		}
//	})

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		autoHeight:true,
		items : [tabs]
	});
})
