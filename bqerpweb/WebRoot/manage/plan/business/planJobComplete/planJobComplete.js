Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	
	var actionId;
	var deptId;
	
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() ;
		s += (t > 9 ? "" : "0") + t
		return s;
	}

		// 系统当前时间
	function getToDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		return s;
	}
	

		// 从session取登录人编码姓名部门相关信息
		
	function getWorkCode() {
			Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
					
						if (result.workerCode) {
							deptH.setValue(result.deptCode);
							dept.setValue(result.deptName);
							workercode.setValue(result.workerCode);
							editName.setValue(result.workerName);
							editDate.setValue(finishEditDate.getValue());
							deptId = result.deptId;
							query();
						}
					}
				});
	}
	

	var con_item = Ext.data.Record.create([{
				name : 'jobId',
				mapping : 0
			}, {
				name : 'deptMainId',
				mapping : 1
			}, {
				name : 'jobContent',
				mapping : 2
			}, {
				name : 'ifComplete',
				mapping : 3
			}, {
				name : 'completeDesc',
				mapping : 4
			}, {
				name : 'completeData',
				mapping : 5
			}, {
				name : 'chargeBy',
				mapping : 6
			}, {
				name : 'orderBy',
				mapping : 7
			}, {
				name : 'editDepcode',
				mapping : 8
			}, {
				name : 'deptName',
				mapping : 9
			}, {
				name : 'finishEditBy',
				mapping : 10
			},{
				name : 'editName',
				mapping : 11
			},{
				name : 'finishSignStatus',
				mapping : 12
			},{
				name : 'finishWorkFlowNo',
				mapping : 13
			},{
				name : 'linkJobId',
				mapping : 14
			},{
			   name:'level1DeptName',
			   mapping:18
			}]);

	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false
			});
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manageplan/getPlanJobCompleteList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, con_item)
			});


			
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
						var record = Grid.selModel.getSelected();
						record.set("jobContent", Ext.get("memoText").dom.value);
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
			
			
	var con_item_cm = new Ext.grid.ColumnModel([con_sm, {
				header : '部门',
				dataIndex : 'level1DeptName',
				width : 150,
				align : 'left'

			},{
			        width : 80,
					header : "序号",
					align : 'center',
					sortable : false,
					dataIndex : 'orderBy',
					editor : new Ext.form.TextField({
								id : 'orderBy',
								allowBlank : false
							})
				}, {
				header : '工作内容',
				dataIndex : 'jobContent',
				width : 430,
				align : 'left',
				editor : new Ext.form.TextArea({
								listeners : {
									"render" : function() {
										this.el.on("dblclick", function() {
													Grid.stopEditing();
													var record =Grid
															.getSelectionModel()
															.getSelected();
													var value = record
															.get('jobContent');
													memoText.setValue(value);
													win.x = undefined;
													win.y = undefined;
													win.show();
												})
									}
								}
							})

			}, {
				header : '完成时间',
				dataIndex : 'completeData',
				align : 'center',
				editor : new Ext.form.ComboBox({
								name : 'name111',
								hiddenName : 'hiddenName111',
								mode : 'local',
								triggerAction : 'all',
								store : new Ext.data.SimpleStore({
											fields : ['name', 'value'],
											data : [['当月', 0], ['跨月', 1],
													['长期', 2], ['全年', 3]]
										}),
								displayField : 'name',
								valueField : 'value'

							}),
				renderer : function(v) {
					if (v == '0') {
						return '当月';
					} else if (v == '1') {
						return '跨月';
					} else if (v == '2') {
						return '长期';
					} else
						return '全年';
				}

			}, {
				header : '完成情况',
				dataIndex : 'ifComplete',
				align : 'center',
				editor : new Ext.form.ComboBox({
							store : new Ext.data.SimpleStore({
										data : [['0', '未完成'], ['1', '进行中'],
												['2', '已完成']],
										fields : ['value', 'text']
									}),
							displayField : 'text',
							valueField : 'value',
							mode : 'local',
							triggerAction : 'all'
						}),
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
				align : 'center',
				editor : new Ext.form.TextField()

			}]);
	con_item_cm.defaultSortable = true;
	var deptH = new Ext.form.Hidden({
				id : 'deptH',
				name : 'deptH'
			})
	var workercode = new Ext.form.Hidden({
				id : 'workercode',
				name : 'workercode'
			})
	var dept = new Ext.form.TextField({
				id : 'dept',
				name : 'dept',
				readOnly : true
			})
	var editName = new Ext.form.TextField({
				id : 'editName',
				readOnly : true
			})
	var editDate = new Ext.form.TextField({
				id : 'editDate',
				readOnly : true
			})
	var gridbbar = new Ext.Toolbar({
				items : ['编辑部门：', deptH, dept, '填写人：', editName, '填写时间：',
						editDate]
			})
			
	var finishEditDate = new Ext.form.TextField({
				name : 'finishEditDate',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '',
									alwaysUseStartDate : true,
									dateFmt : "yyyy-MM-dd"
								});
						this.blur();
					}
				}
			});		
	finishEditDate.setValue(getToDate());	
			
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

		function init(){
		Ext.Ajax.request({
			url : 'manageplan/getPlanCompleteStatus.action',
					method : 'post',
					params : {
			                planTime : planTime.getValue()
					},
					success :function(response,options) {
						var res = response.responseText;
						if(res.toString() == '1'||res.toString() == '2'||res.toString() == '3'||
						   res.toString() == '4'||res.toString() == '5'||res.toString() == '6'||
						   res.toString() == '7'||res.toString() == '8'||res.toString() == '9'||
						   res.toString() == '10'||res.toString() == '11')
						{

							Ext.getCmp('btnsave').setDisabled(true);
							Ext.getCmp('btncancer').setDisabled(true);
			                Ext.getCmp('btnreport').setDisabled(true);
			                Ext.getCmp('btnadd').setDisabled(true);
			                Ext.getCmp('btndelete').setDisabled(true);
						}else{
			                Ext.getCmp('btnsave').setDisabled(false);
							Ext.getCmp('btncancer').setDisabled(false);
			                Ext.getCmp('btnreport').setDisabled(false);
			                Ext.getCmp('btnadd').setDisabled(false);
			                Ext.getCmp('btndelete').setDisabled(false);
                       }
					}
				});
	
	}
	
	function query() {
        init();
		con_ds.baseParams = {
			planTime : planTime.getValue()
		}
		con_ds.load();
	
	}


	var contbar = new Ext.Toolbar({
				items : ["计划时间:", planTime, '-', {
							text : '查询',
							iconCls : 'query',
							handler : query
						}, '-', {
							text : "新增",
							id : 'btnadd',
							iconCls : 'add',
							handler : addRecord
						}, '-', {
							text : "删除",
							id : 'btndelete',
							iconCls : 'delete',
							handler : deleteRecords
						}, {
							text : "保存",
							id : 'btnsave',
							iconCls : 'save',
							handler : saveFun
						}, {
							text : "取消",
							id : 'btncancer',
							iconCls : 'cancer',
							handler : cancel
						}, {
							text : "上报",
							id : 'btnreport',
							iconCls : 'upcommit',
							handler : reportFun
						}]
			});

	var Grid = new Ext.grid.EditorGridPanel({
				sm : con_sm,
				ds : con_ds,
				cm : con_item_cm,
				autoScroll : true,
				bbar : gridbbar,
				tbar : contbar,
				border : true,
				clicksToEdit : 1
			});
			
			
	Grid.on('beforeedit', function() {
				if (Ext.getCmp('btnsave').disabled == true)
					return false;
			})

			
		Grid.on("beforeedit", function(obj) {
				var record = obj.record;
				var field = obj.field;
				if(field=="orderBy"||field=="jobContent"||field=="completeData")
				{
					if(record.get("jobId")!=null){
						return false;
					}
				}
			});	
			
	function addRecord() {
		var count = con_ds.getCount();
		var currentIndex = count;
		var o = new con_item({
			        'level1DeptName':dept.getValue(),
					'ifComplete' : '0',
					'deptMainId' : '0',
					'chargeBy':workercode.getValue(),
					'chargeName':editName.getValue(),
					'jobContent' : '',
					'completeData' : '0',
					'completeDesc':'',
					'orderBy':'' 
				});
		Grid.stopEditing();
		con_ds.insert(currentIndex, o);
		con_sm.selectRow(currentIndex);
		Grid.startEditing(currentIndex, 2);
	}
	// 删除记录
  var j=0;//add by wpzhu
	var ids = new Array();
	function deleteRecords() {
		var sm = Grid.getSelectionModel();
		var selections = sm.getSelections();
		if (selections.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selections.length; i += 1) {
				var member = selections[i];

					if (member.get("jobId") != null&&member.get("jobId")!="") {
						ids.push(member.get("jobId"));
						j++;//add by wpzhu
					}
					Grid.getStore().remove(member);
					Grid.getStore().getModifiedRecords().remove(member);
					
			
			}
		}
	}			

	
	function saveFun() {
		Grid.stopEditing();
		var modifieds = con_ds.getModifiedRecords();
		if (modifieds.length == 0 && ids.length==0 && con_ds.getTotalCount()==0) {
			Ext.Msg.alert('提示', '没有任何修改！');
			return;
		}
	
	 if(con_ds.getTotalCount()>0){
	 	for(i=0;i< con_ds.getTotalCount()-j;i++){//modify by wpzhu 20100806
		  if((con_ds.getAt(i).get("completeData")=='0' )&&
		  (con_ds.getAt(i).get("ifComplete")=='0'||con_ds.getAt(i).get("ifComplete")=='1')&&
		   (con_ds.getAt(i).get("completeDesc")==null||con_ds.getAt(i).get("completeDesc")=="")){
		  Ext.Msg.alert('提示', '当月下未完成或进行中的考核说明不能为空！');
		  return;
		   }
		}
	 }
	 	 if(modifieds.length>0){
	 	for(i=0;i<modifieds.length;i++){
		  if((con_ds.getAt(i).get("completeData")=='0' )&&
		  (con_ds.getAt(i).get("ifComplete")=='0'||con_ds.getAt(i).get("ifComplete")=='1')&&
		   (con_ds.getAt(i).get("completeDesc")==null||con_ds.getAt(i).get("completeDesc")=="")){
		  Ext.Msg.alert('提示', '当月下未完成或进行中的考核说明不能为空！');
		  return;
		   }
		}
	 }	
	 
		Ext.Msg.confirm('提示', '确认要保存数据吗？', function(id) {
					if (id == 'yes') {
						var updates = new Array();
						for (var i = 0; i < modifieds.length; i++) {
							updates.push(modifieds[i].data)
						}
						Ext.Ajax.request({
									url : 'manageplan/savePlanJobComplete.action',
									method : 'post',
									params : {
							             planTime : planTime.getValue(),
							             isUpdate : Ext.util.JSON.encode(updates),
							             isDelete : ids.join(",")
									},
									success : function(response, options) {
										Ext.Msg.alert('提示', '数据保存成功！');
										con_ds.rejectChanges();
										con_ds.reload()
										j=0;//add by wphzu 20100806
									},
									failure : function(response, options) {
										Ext.Msg.alert('提示', '数据保存失败！');
									}
								})
					}
				}

		)

	}
	
		// 取消
	function cancel() {
		var modifyRec = con_ds.getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			if (!confirm("确定要放弃修改吗"))
				return;
			con_ds.reload();
			con_ds.rejectChanges();
			ids = [];
		} else {
			con_ds.reload();
			con_ds.rejectChanges();
			ids = [];
		}
	}

   function reportFun() {
		if (con_ds.getTotalCount() == 0) {
			Ext.Msg.alert('提示', '无数据进行上报！');
			return;
		}
		 if(con_ds.getAt(0).get("finishSignStatus")==null){
		  Ext.Msg.alert('提示', '请先保存数据！');
			return;
		 }
		var url = "reportSign.jsp";
		var args = new Object();
		args.entryId = con_ds.getAt(0).get("finishWorkFlowNo");
		args.deptMainId = con_ds.getAt(0).get("deptMainId");
		args.workflowType = "bqDeptPlanFinishApprove";
		args.actionId = actionId;
		args.deptId = deptId;
		
		args.roles = true;
		var obj = window.showModalDialog(url, args,
				'status:no;dialogWidth=770px;dialogHeight=550px');
		if (obj) {
			query();
			Ext.getCmp('btnsave').setDisabled(true);
			Ext.getCmp('btnreport').setDisabled(true);
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

	getWorkCode();
})
