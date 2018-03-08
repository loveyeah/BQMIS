Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	var actionId;
	var tempeId = null;
	var workFlowNo = null;
	
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 2;
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
							workercode.setValue(result.workerCode);
							editName.setValue(result.workerName);
							editDate.setValue(editTime.getValue());
							feeResources.setValue("劳保费");
							query();
						}
					}
				});
	}
	
	function getRecord() {
		Ext.Ajax.request({
					url : 'hr/getLaborTempeInfo.action',
					method : 'post',
					params : {
						tempeMonth : tempeMonth.getValue()
					},
						success : function(action) {
						var result = eval("(" + action.responseText + ")");
                        
						if (result && result.length > 0) {
							var mainId = "";
							var workflowNo = "";
							for (var i = 0; i < result.length; i++) {
								var ob = result[i];
								mainId += ob[0] + ",";
								workflowNo += ob[7] + ",";
								
							}
							tempeId = mainId.substring(0, mainId.length - 1);
							workFlowNo = workflowNo.substring(0,workflowNo.length - 1)

						}

					}
				})
	}
	
	var con_item = Ext.data.Record.create([{
				name : 'tempeDetailId',
				mapping : 0
			}, {
				name : 'tempeId',
				mapping : 1
			}, {
				name : 'deptName',
				mapping : 2
			}, {
				name : 'factNum',
				mapping : 3
			}, {
				name : 'highTempeNum',
				mapping : 4
			}, {
				name : 'highTempeStandard',
				mapping : 5
			}, {
				name : 'highAmount',
				mapping : 6
			}, {
				name : 'midTempeNum',
				mapping : 7
			}, {
				name : 'midTempeStandard',
				mapping : 8
			}, {
				name : 'midAmount',
				mapping : 9
			}, {
				name : 'lowTempeNum',
				mapping : 10
			}, {
				name : 'lowTempeStandard',
				mapping : 11
			}, {
				name : 'lowAmount',
				mapping : 12
			},  {
				name : 'sumAmount',
				mapping : 13
			},{
				name : 'memo',
				mapping : 14
			},{
				name : 'tempeMonth',
				mapping : 15
			},{
				name : 'costItem',
				mapping : 16
			},{
				name : 'tempeState',
				mapping : 17
			},{
				name : 'workFlowNo',
				mapping : 18
			}]);

	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false
			});
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'hr/getHrJLaborTempeList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, con_item)
			});
	
	con_ds.on("load", function(){
				var a1 = 0.0,a2=0.0,a3=0.0,b1=0,a4=0.0,a5=0.0,a6=0.0,b2=0,a7=0.0,a8=0.0,a9=0.0,b3=0,a10=0.0,a11=0.0;
			
				con_ds.each(function(rec) {
				if(rec.get('deptName') != "合计"){
				 if (rec.get('factNum') != null)
								a1 += rec.get('factNum');
							if (rec.get('highTempeNum') != null)
								a2 += rec.get('highTempeNum');
							if (rec.get('highTempeStandard') != null){
								a3 += rec.get('highTempeStandard');
							    b1+=1;
							}
							if (rec.get('highAmount') != null)
								a4 += rec.get('highAmount');
							if (rec.get('midTempeNum') != null)
								a5 += rec.get('midTempeNum');
							if (rec.get('midTempeStandard') != null){
								a6 += rec.get('midTempeStandard');
							    b2+=1;
							}
							if (rec.get('midAmount') != null)
								a7 += rec.get('midAmount');
							if (rec.get('lowTempeNum') != null)
								a8 += rec.get('lowTempeNum');
							if (rec.get('lowTempeStandard') != null){
								a9 += rec.get('lowTempeStandard');
							   b3+=1;
							}
							if (rec.get('lowAmount') != null)
								a10 += rec.get('lowAmount');
							if (rec.get('sumAmount') != null)
								a11 += rec.get('sumAmount');
						
				}})
				if(b1==0)
				  a3=0.0;
				else
				   a3/=b1;
				if(b2==0)  
				   a6=0.0;
				else
				   a6/=b2;
				if(a9==0)
				   a9=0.0;
				else
			       a9/=b3;
				var addCount = new con_item({});
				addCount.set('deptName','本页合计')
				addCount.set('factNum',a1);
				addCount.set('highTempeNum',a2);
				addCount.set('highTempeStandard',a3);
				addCount.set('highAmount',a4);
				addCount.set('midTempeNum',a5);
				addCount.set('midTempeStandard',a6);
				addCount.set('midAmount',a7);
				addCount.set('lowTempeNum',a8);
				addCount.set('lowTempeStandard',a9);
				addCount.set('lowAmount',a10);
				addCount.set('sumAmount',a11);
				
				if(con_ds.getAt(con_ds.getCount()-1).get("deptName") == "合计")
				    con_ds.insert(con_ds.getCount()-1,addCount);
				else
				    con_ds.add(addCount);
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
		title : '备注录入窗口',
		buttons : [{
					text : '保存',
					iconCls : 'save',
					handler : function() {
						if (!memoText.isValid()) {
							return;
						}
						var record = grid.selModel.getSelected();
						record.set("memo", Ext.get("memoText").dom.value);
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
			
			
	var con_item_cm = new Ext.grid.ColumnModel([con_sm,
			{
				header : "部门",
				align : 'left',
				dataIndex : 'deptName',
				editor : new Ext.form.TextField({})
			}, {
				header : '实有人数',
				dataIndex : 'factNum',
				align : 'center',
				editor : new Ext.form.NumberField()

			}, {
				header : '高温标准人数',
				dataIndex : 'highTempeNum',
				align : 'center',
				editor : new Ext.form.NumberField()

			}, {
				header : '高温标准',
				dataIndex : 'highTempeStandard',
				align : 'center',
				editor : new Ext.form.NumberField(),
				renderer:function(value){
					if(value==""||value==null) return;
				    else return value.toFixed(2);
				}

			}, {
				header : '高温金额（元）',
				dataIndex : 'highAmount',
				align : 'center'

			}, {
				header : '中温标准人数',
				dataIndex : 'midTempeNum',
				align : 'center',
				editor : new Ext.form.NumberField()

			}, {
				header : '中温标准',
				dataIndex : 'midTempeStandard',
				align : 'center',
				editor : new Ext.form.NumberField(),
				renderer:function(value){
					if(value==""||value==null) return;
				    else return value.toFixed(2);
				}

			}, {
				header : '中温金额（元）',
				dataIndex : 'midAmount',
				align : 'center'

			}, {
				header : '低温标准人数',
				dataIndex : 'lowTempeNum',
				align : 'center',
				editor : new Ext.form.NumberField()

			}, {
				header : '低温标准',
				dataIndex : 'lowTempeStandard',
				align : 'center',
				editor : new Ext.form.NumberField(),
				renderer:function(value){
					if(value==""||value==null) return;
				    else return value.toFixed(2);
				}

			}, {
				header : '低温金额（元）',
				dataIndex : 'lowAmount',
				align : 'center'

			}, {
				header : '总额（元）',
				dataIndex : 'sumAmount',
				align : 'center'

			}, {
				header : '备注',
				dataIndex : 'memo',
				width : 100,
				align : 'left',
				editor : new Ext.form.TextArea({
							listeners : {
								"render" : function() {
									this.el.on("dblclick", function() {
												grid.stopEditing();
												var record = grid
														.getSelectionModel()
														.getSelected();
												var value = record.get('memo');
												memoText.setValue(value);
												win.x = undefined;
												win.y = undefined;
												win.show();
											})
								}
							}
						})

			}]);
	con_item_cm.defaultSortable = true;
	var workercode = new Ext.form.Hidden({
				id : 'workercode',
				name : 'workercode'
			})
	var editName = new Ext.form.TextField({
				id : 'editName',
				readOnly : true
			})
	var editDate = new Ext.form.TextField({
				id : 'editDate',
				readOnly : true
			})
	
	var feeResources= new Ext.form.TextField({
				id : 'feeResources',
				name : 'feeResources',
				readOnly : true
			})
			
	var gridbbar = new Ext.Toolbar({
				items : [ '制表人：', editName, '制表时间：',editDate, '费用来源：',feeResources]
			})
			
	var editTime = new Ext.form.TextField({
				name : 'editTime',
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
	editTime.setValue(getToDate());	
			
	//月份
	var tempeMonth = new Ext.form.TextField({
				name : 'tempeMonth',
				fieldLabel : '月份',
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
	tempeMonth.setValue(getDate());

			function init(){
		Ext.Ajax.request({
			url : 'hr/getHrJLaborTempeStatus.action',
					method : 'post',
					params : {
			                tempeMonth : tempeMonth.getValue()
					},
					success :function(response,options) {
						var res = response.responseText;
						if(res.toString() == '2')
						{

							Ext.getCmp('btnsave').setDisabled(true);
							Ext.getCmp('btnreflesh').setDisabled(true);
			                Ext.getCmp('btnadd').setDisabled(true);
			                Ext.getCmp('btndelete').setDisabled(true);
							Ext.getCmp('btnapprove').setDisabled(true);
						}else{
			                Ext.getCmp('btnsave').setDisabled(false);
							Ext.getCmp('btnreflesh').setDisabled(false);
			                Ext.getCmp('btnadd').setDisabled(false);
			                Ext.getCmp('btndelete').setDisabled(false);
			                Ext.getCmp('btnapprove').setDisabled(false);
                       }
					}
				});
	
	}
	
	function query() {
		init();
		con_ds.baseParams = {
			tempeMonth : tempeMonth.getValue(),
			flag:"approve"
		}
			con_ds.load({
						params : {
							start : 0,
							limit : 18
						}
		})
		getRecord();
	}

var tbar = new Ext.Toolbar({
				items : ["月份:", tempeMonth, '-', {
							text : '查询',
							iconCls : 'query',
							handler : query
						}, '-', {
							text : "签字",
							id : 'btnapprove',
							iconCls : 'approve',
							handler : approveFun
						}]
			});
	var contbar = new Ext.Toolbar({
				items : [ {
							text : "新增",
							id : 'btnadd',
							iconCls : 'add',
							handler : addRecord
						}, '-', {
							text : "删除",
							id : 'btndelete',
							iconCls : 'delete',
							handler : deleteRecords
						},'-',  {
							text : "保存",
							id : 'btnsave',
							iconCls : 'save',
							handler : saveFun
						}, '-', {
							text : "刷新",
							id : 'btnreflesh',
							iconCls : 'reflesh',
							handler : reflesh
						}]
			});

	var grid = new Ext.grid.EditorGridPanel({
				sm : con_sm,
				ds : con_ds,
				cm : con_item_cm,
				tbar : contbar,
				border : false,
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : con_ds,
							displayInfo : true,
							displayMsg : "显示第{0}条到{1}条，共{2}条",
							emptyMsg : "没有记录",
							beforePageText : '',
							afterPageText : ""
						}),
				clicksToEdit : 1
			});
			
			grid.on("beforeedit", function(obj) {
				var record = obj.record;
				var field = obj.field;
					
				if(field=="deptName"||field=="factNum"||field=="highTempeNum"||field=="highTempeStandard"||field=="midTempeNum"||
				   field=="midTempeStandard"||field=="lowTempeNum"||field=="lowTempeStandard"||field=="memo")
				{
					if(record.get("deptName")!=null &&(record.get("deptName")=='合计'||record.get("deptName")=='本页合计'))
					{
						return false;
					}
				}
			});
			
	function addRecord() {
		var count = con_ds.getCount();
		var currentIndex = count;
		var o = new con_item({
			        'deptName':'',
					'factNum' : '',
					'highTempeNum' : '',
					'highTempeStandard' : '',
					'highAmount' : '',
					'midTempeNum':'',
					'midTempeStandard' : '',
					'midAmount' : '',
					'lowTempeNum' : '',
					'lowTempeStandard' : '',
					'lowAmount':'' ,
					'sumAmount' : '',
					'memo' : ''
				});
		grid.stopEditing();
		con_ds.insert(currentIndex, o);
		con_sm.selectRow(currentIndex);
		grid.startEditing(currentIndex, 1);
	}
	// 删除记录

	var ids = new Array();
	function deleteRecords() {
		var sm = grid.getSelectionModel();
		var selections = sm.getSelections();
		if (selections.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selections.length; i += 1) {
				var member = selections[i];

					if (member.get("tempeDetailId") != null) {
						ids.push(member.get("tempeDetailId"));
					}
					grid.getStore().remove(member);
					grid.getStore().getModifiedRecords().remove(member);
			
			}
		}
	}			

	
	function saveFun() {
		grid.stopEditing();
		var modifieds = con_ds.getModifiedRecords();
		if (modifieds.length == 0 && ids.length==0) {
			Ext.Msg.alert('提示', '没有任何修改！');
			return;
		}
	
		Ext.Msg.confirm('提示', '确认要保存数据吗？', function(id) {
					if (id == 'yes') {
						var updates = new Array();
						for (var i = 0; i < modifieds.length; i++) {
							updates.push(modifieds[i].data)
						}
						Ext.Ajax.request({
									url : 'hr/saveLaborTempe.action',
									method : 'post',
									params : {
							             tempeMonth : tempeMonth.getValue(),
							             flag:'approveAdd',
						                workFlowType:"bqHrJLaborTempeApprove",
							             costItem:"劳保费",
							             isUpdate : Ext.util.JSON.encode(updates),
							             isDelete : ids.join(",")
									},
									success : function(response, options) {
										Ext.Msg.alert('提示', '数据保存成功！');
										con_ds.rejectChanges();
										con_ds.reload()
									},
									failure : function(response, options) {
										Ext.Msg.alert('提示', '数据保存失败！');
									}
								})
					}
				}

		)

	}
	
		
	function reflesh() {
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

   function approveFun() {
		if (con_ds.getTotalCount() == 1 && con_ds.getAt(0).get('deptName')=='合计') {
			Ext.Msg.alert('提示', '无数据进行审批！');
			return;
		}

		var url = "approveSign.jsp";
		var args = new Object();
	
		args.tempeId = tempeId;
		args.entryId = workFlowNo;
		args.workflowType = "bqHrJLaborTempeApprove";
		var obj = window.showModalDialog(url, args,
				'status:no;dialogWidth=770px;dialogHeight=550px');
		if (obj) {
			query();
		}
	
	}
	//add by  wpzhu 20100716 查询最近日期的审批数据
  function getApproveData() {
		Ext.Ajax.request({
			url : 'hr/getTempeApproveInfo.action',
					method : 'post',
					success :function(response,options) {
						var res = response.responseText;
						if(res.toString() == '')
						{
							
						}else{
							var  month=res.toString();
							tempeMonth.setValue(month);
						}
				          query();
					}
					
				});
	}	
	getApproveData();

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [{
							region : 'north',
							layout : 'fit',
							height : 25,
							items : [tbar]
						}, {
							layout : 'fit',
							border : false,
							frame : false,
							region : "center",
							items : [grid]
						},{
							region : 'south',
							layout : 'fit',
							height : 25,
							items : [gridbbar]
						}]
			});

	getWorkCode();
})
