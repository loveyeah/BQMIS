Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	var deptMainId = null;
	var finishWorkNo = null;
	var finishStatus = null;
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t
		return s;
	}


//	function getRecord() {
//		Ext.Msg.wait('正在查询数据……');
//		Ext.Ajax.request({
//					url : 'manageplan/getPlanCompleteAllQuery.action',
//					method : 'post',
//					params : {
//						isApprove : 'Y',
//						planTime : planTime.getValue()
//					},
//					success : function(response, options) {
//						Ext.Msg.hide();
//						var res = Ext.util.JSON.decode(response.responseText);
//						if (res && res[0]) {
//							var resu = res[0].toString().split(',');
//							if (resu[1]) {
//								con_ds.baseParams = {
//									deptMainId : resu[1]
//								}
//								con_ds.load();
//								editName.setValue(resu[5]);
//								editDate.setValue(resu[6]);
//								Ext.getCmp('btnreport').setDisabled(false);
//								deptMainId = resu[1];
//								finishWorkNo = resu[8];
//								finishStatus = resu[9];
//								alert(finishWorkNo)
//								alert(finishStatus)
//
//							}
//						} else {
//							Ext.Msg.alert('提示', '该月份的工作计划汇总审批未结束，不能提前填写完成情况！');
//							editName.setValue(null);
//							editDate.setValue(null);
//							Ext.getCmp('btnreport').setDisabled(true);
//						}
//
//					}
//				})
//	}
	/** 左边的grid * */
	var con_item = Ext.data.Record.create([
	{
				name : 'planTime',
				mapping : 0

			}, {
				name : 'finishWorkFlowNo',
				mapping : 1
			}, {
				name : 'finishSignStatus',
				mapping : 2
			}, {
				name : 'deptCode',
				mapping : 3
			}, {
				name : 'deptName',
				mapping : 4
			}, {
				name : 'jobId',
				mapping : 5
			}, {
				name : 'jobContent',
				mapping : 6
			}, {
				name : 'ifComplete',
				mapping : 7
			}, {
				name : 'completeDesc',
				mapping : 8
			}, {
				name : 'completeDate',
				mapping : 9
			}]);

	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false
			});
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manageplan/getPlanCompleteAllQuery.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, con_item)
			});
	con_ds.on('load',function(){
		if(con_ds.getTotalCount() > 0){
			if(con_ds.getAt(0).get('finishSignStatus')== '2')
			{
				Ext.getCmp('btnreport').setDisabled(true)
			}else
				Ext.getCmp('btnreport').setDisabled(false)
		}
	})
	var con_item_cm = new Ext.grid.ColumnModel([con_sm, {
				header : '部门',
				dataIndex : 'deptName',
				align : 'center'
			}, {
				header : '工作内容',
				dataIndex : 'jobContent',
				align : 'center'

			}, {
				header : '完成时间',
				dataIndex : 'completeDate',
				align : 'center',
				renderer : function(v) {
					if (v == '0') {
						return '当月';
					} else if (v == '1') {
						return '跨越';
					} else if (v == '2') {
						return '长期';
					} else
						return '';
				}

			}, {
				header : '完成情况',
				dataIndex : 'ifComplete',
				align : 'center',
				renderer : function(v) {
					if (v == '0')
						return '未完成';
					else if (v == '1')
						return '进行中';
					else if (v == '2')
						return '已完成';
					else
						return '';
				}
			}, {
				header : '考核说明',
				dataIndex : 'completeDesc',
				align : 'center'
			}]);
	con_item_cm.defaultSortable = true;
	
	
	// 计划时间
	var planTime = new Ext.form.TextField({
				name : 'planTime',
				fieldLabel : '计划时间',
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
	planTime.setValue(getDate());

	function queryFun() {
		con_ds.baseParams = {
					isApprove : 'Y',
					planTime : planTime.getValue()
				}
		con_ds.load();

	}
	var query = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : queryFun

			})

	var contbar = new Ext.Toolbar({
				items : [planTime, query,{
							text : "审批",
							id : 'btnreport',
							iconCls : 'approve',
							handler : approveFun
						}]
			});

	var Grid = new Ext.grid.GridPanel({
				sm : con_sm,
				ds : con_ds,
				cm : con_item_cm,
				// height : 425,
				autoScroll : true,
				tbar : contbar,
				border : true
			});


	function approveFun() {
		if (con_ds.getTotalCount() == 0) {
			Ext.Msg.alert('提示', '无数据进行审批！');
			return;
		}
//		if (finishStatus == '1' || finishStatus == '2') {
//			Ext.Msg.alert('提示', '该月份数据已上报！');
//			return;
//		}
		var url = "sign.jsp";
		var args = new Object();
		args.entryId = con_ds.getAt(0).get('finishWorkFlowNo');
		args.planTime = planTime.getValue();
		args.workflowType = "bqDeptJobFinishGather";
		var obj = window.showModalDialog(url, args,
				'status:no;dialogWidth=770px;dialogHeight=550px');
		if(obj){
			queryFun()
		}
	}

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [{
							layout : 'fit',
							border : false,
							frame : false,
							region : "center",
							items : [Grid]
						}]
			});
			
			queryFun();
})
