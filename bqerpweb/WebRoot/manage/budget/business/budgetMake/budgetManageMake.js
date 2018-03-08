Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	// 获得年份
	function getDate() {
		var Y;
		Y = new Date();
		Y = Y.getFullYear().toString(10);
		return Y;
	}
	// 获得月份
	function getMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString() + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		return s;
	}

	// 系统当前时间
	function getTime() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		return s;
	}
	// 定义从页面取得数据

	var entryId; // 工作流编号
	var id; // 项目Id
	var topicId = 3;
	var deptCode; // 部门Id

	// 上报人
	var reportName = new Ext.form.Hidden({
				id : 'reportName',
				name : 'reportName',
				dataIndex : 'reportName'
			});
	// 审批状态
	var makeStatus;

	function queryData() {

		store.load({
					params : {
						budgetTime : time.getValue(),
						deptCode : deptCode,
						topicId : topicId
					}
				});
		store.rejectChanges();

	}

	// 年月份选择
	var formatType;
	var yearRadio = new Ext.form.Radio({
				id : 'year',
				name : 'queryWayRadio',
				// blankText : getDate(),
				hideLabel : true,
				boxLabel : '年份',
				checked : true,
				listeners : {
					check : function() {
						var queryType = getChooseQueryType();
						switch (queryType) {
							case 'year' : {
								formatType = 1;
								time.setValue(getDate());
								break;
							}
							case 'month' : {
								time.setValue(getMonth());
								formatType = 2;
								break;
							}
						}
					}
				}
			});
	var monthRadio = new Ext.form.Radio({
				id : 'month',
				name : 'queryWayRadio',
				hideLabel : true,
				boxLabel : '月份'
			});

	var time = new Ext.form.TextField({
				id : 'time',
				allowBlank : true,
				readOnly : true,
				value : getMonth(),
				width : 100,
				listeners : {
					focus : function() {
						var format = '';
						if (formatType == 1)
							format = 'yyyy';
						if (formatType == 2)
							format = 'yyyy-MM';
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : format,
									alwaysUseStartDate : false,
									onclearing : function() {
										planStartDate.markInvalid();
									}
								});
					}
				}
			});
	// 遍历所有的REDIO获得ID
	function getChooseQueryType() {
		var list = document.getElementsByName("queryWayRadio");
		for (var i = 0; i < list.length; i++) {
			if (list[i].checked) {
				return list[i].id;
			}
		}
	}

	var sm = new Ext.grid.CheckboxSelectionModel();
	// 创建记录类型
	var Myrecord = Ext.data.Record.create([{
				// 预算指标id
				name : 'itemId'
			}, {
				// 预算指标名称
				name : 'itemAlias'
			}, {
				name : 'financeItem'
			}, {
				// 计量单位名称
				name : 'unitName'
			}, {
				// 预算值
				name : 'adviceBudget'
			}, {
				// 编制依据
				name : 'budgetBasis'
			}, {
				// 预算编制单ID
				name : 'budgetMakeId'
			}, {
				// 预算明细ID
				name : 'budgetItemId'
			}, {
				// 预算部门id
				name : 'centerId'
			}, {
				// 预算主题id
				name : 'topicId'
			}, {
				// 预算时间
				name : 'budgetTime'
			}, {
				// 工作流序号
				name : 'workFlowNo'
			}, {
				// 编制状态
				name : 'makeStatus'
			}, {
				name : 'dataSource'
			},{name:'itemCode'}]);
	// 配置数据集
	var store = new Ext.data.Store({
				/* 创建代理对象 */
				proxy : new Ext.data.HttpProxy({
							url : 'managebudget/findBudgetMakeList.action'
						}),
				/* 创建解析Json格式数据的解析器 */
				reader : new Ext.data.JsonReader({
							root : "list",
							totalProperty : "totalCount"
						}, Myrecord)
			});

	store.on('load', function() {
		for (var j = 0; j < store.getCount(); j++) {
			var temp = store.getAt(j);
			if (temp.get("makeStatus") == null || temp.get("makeStatus") == "") {
				temp.set("makeStatus", "0");
			}
			if (temp.get("makeStatus") == "" || temp.get("makeStatus") == "0"
					|| temp.get("makeStatus") == null
					|| temp.get("makeStatus") == 3) {
				Grid.getTopToolbar().items.get('btnSave').setDisabled(false);
				Grid.getTopToolbar().items.get('btnUpcommit')
						.setDisabled(false);
			} else {
				Grid.getTopToolbar().items.get('btnSave').setDisabled(true);
				Grid.getTopToolbar().items.get('btnUpcommit').setDisabled(true);
			}
			if (temp.get("adviceBudget") == null
					|| temp.get("adviceBudget") == "") {
				temp.set("adviceBudget", "0");
			}
		}
	});

	// 查询
	var query = new Ext.Button({
				id : "btnQuery",
				text : "查询",
				iconCls : "query",
				handler : function query() {
					queryData();
				}
			})
	// 保存
	var save = new Ext.Button({
				id : "btnSave",
				text : "保存",
				iconCls : "save",
				handler : function saveModifies() {
					Grid.stopEditing();
					var modifyRec = Grid.getStore().getModifiedRecords();
					if (modifyRec.length > 0) {
						Ext.Msg.confirm('提示','确定要保存数据吗？',function(button){
							if(button=='yes'){
									Ext.Msg.wait("正在保存数据,请等待...");
						var modifyRecords = new Array();
						for (var i = 0; i < modifyRec.length; i++) {
							modifyRecords.push(modifyRec[i].data);
						}

						Ext.Ajax.request({
									url : 'managebudget/saveBudgetMake.action',
									method : 'post',
									params : {
										addOrUpdateRecords : Ext.util.JSON
												.encode(modifyRecords)
									},
									success : function(result, request) {
										Ext.MessageBox.alert('提示', '操作成功！');
										store.reload();
										store.rejectChanges();

									},
									failure : function(result, request) {
										Ext.MessageBox.alert('提示', '操作失败！');
										store.reload();
										store.rejectChanges();
									}
								})
							}
						});
					
					} else {
						Ext.Msg.alert('提示', '您没有做任何修改！');
					}

				}
			})

	// 发送 update by ltong
	var upcommit = new Ext.Button({
				id : 'btnUpcommit',
				text : '发送',
				iconCls : 'upcommit',
				handler : sendUp

			})

	// 发送
	function sendUp() {
		if (store.getCount() == 0) {
			Ext.Msg.alert("提示", "没有记录！");
			return;
		} else {
			var temp = store.getAt(0);
			if (temp.get("budgetMakeId") == null
					|| temp.get("budgetMakeId") == "") {
				Ext.Msg.alert("提示", "请先保存数据！");
				return;
			}
		}
		Ext.Msg.confirm('提示','确定要发送吗？',function(button){
			if(button=='yes'){
						var temp = store.getAt(0);
						Ext.Ajax.request({
					url : 'managebudget/sendBudgetMake.action',
					method : 'post',
					params : {
						budgetMakeId : temp.get("budgetMakeId")
					},
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						Ext.Msg.alert("提示", result.msg)
						queryData();

					},
					failure : function() {
						Ext.Msg.alert("提示", "发送失败");
					}
				})
				
			}
		})


	};

	// 上报
	function upcommit() {
		if (store.getCount() == 0) {
			Ext.Msg.alert("提示", "没有记录！");
			return;
		} else {
			var temp = store.getAt(0);
			if (temp.get("budgetMakeId") == null
					|| temp.get("budgetMakeId") == "") {
				Ext.Msg.alert("提示", "请先保存数据！");
				return;
			}
		}

		var data = store.getAt(0);
		if (!confirm("确定要上报吗？"))
			return;
		var url = "budgetMakeReport.jsp";
		var args = new Object();
		args.entryId = data.get("workFlowNo");
		args.workflowType = "budgetMakeApporval";
		args.budgetMakeId = data.get("budgetMakeId");
		args.prjStatus = data.get("makeStatus");
		var obj = window.showModalDialog(url, args,
				'status:no;dialogWidth=750px;dialogHeight=550px');
		// 按钮设为不可用
		if (obj) {
			queryData();
		}

	}
   
    function renderMoney(v) {
    	return renderNumber(v, 2);// modify by ywliu 2009/7/3 修改计算金额现在2位小数
    }
    
    function renderNumber(v, argDecimal) {
		if (v) {
			if (typeof argDecimal != 'number') {
				argDecimal = 2;
			}
			v = Number(v).toFixed(argDecimal);
			var t = '';
			v = String(v);
			while ((t = v.replace(/^(-?[0-9]+)([0-9]{3}.*)$/, '$1,$2')) !== v)
				v = t;
			
			return v;
		} else
			return '';
	}
	// 定义grid
	// 事件状态
	var sm_store_item = new Ext.grid.ColumnModel([sm, {
				header : '预算项目',
				align : 'left',
				width : 280,
				sortable : true,
				dataIndex : 'itemAlias',
				renderer : function(value, cellmeta, record, rowIndex, columnIndex, store){
						var level=0;
						if(record.get("itemCode")!=null||record.get("itemCode")!="")
						{
							level= (record.get("itemCode").length/3)-2;
						}
						 if(level>0)
						 {
						 var levelNo="";
						 for(var i=0;i<level;i++)
						{
							levelNo="  "+levelNo;
						}
						
						value=levelNo+value;
						 }
					return "<pre>"+value+"</pre>";
				}
			}, {
				header : '财务科目编码',
				align : 'center',
				width : 110,
				sortable : true,
				dataIndex : 'financeItem'
			}, {
				header : '计量单位',
				align : 'center',
				width : 110,
				sortable : true,
				dataIndex : 'unitName'
			}, {
				header : '预算值',
				align : 'right',
				width : 110,
				sortable : true,
				dataIndex : 'adviceBudget',
				renderer:function(v){
					if(v==0){
						return '0.00';
					}
					return renderMoney(v);
				},
				editor : new Ext.form.NumberField()
			}, {
				header : '编制依据',
				align : 'center',
				width : 110,
				sortable : true,
				dataIndex : 'budgetBasis',
				editor : new Ext.form.TextField()
			}, {
				header : "预算编制单Id",
				align : 'center',
				width : 110,
				sortable : true,
				hidden : true,
				dataIndex : 'budgetMakeId'
			}, {
				header : '预算部门',
				align : 'center',
				width : 110,
				sortable : true,
				hidden : true,
				dataIndex : 'centerId'
			}, {
				header : '预算主题',
				align : 'center',
				width : 110,
				sortable : true,
				hidden : true,
				dataIndex : 'topicId'
			}, {
				header : '预算时间',
				align : 'center',
				width : 110,
				sortable : true,
				hidden : true,
				dataIndex : 'budgetTime'
			}, {
				header : '工作流序号',
				align : 'center',
				width : 110,
				sortable : true,
				hidden : true,
				dataIndex : 'workFlowNo'
			}, {
				header : '编制状态',
				align : 'center',
				width : 110,
				sortable : true,
				hidden : true,
				dataIndex : 'makeStatus',
				editor : new Ext.form.TextField(),
				renderer : function(value) {
					if (value == "0")
						return "未上报";
					else if (value == "1")
						return "编制审批中";
					else if (value == "2")
						return "编制审批通过";
					else if (value == "3")
						return "编制审批退回";
					else
						return value;
				}
			}, {

				header : '来源',
				align : 'center',
				width : 110,
				sortable : true,
				// hidden:true,
				dataIndex : 'dataSource',
				renderer : function(value) {
					if (value == "1")
						return "编制录入";
					if (value == "2")
						return "编制计算";
				}
			}]);

	// 顶部工具栏
	var tbar = new Ext.Toolbar({
				items : [yearRadio, monthRadio, time, '-', query, '-', save,
						'-', upcommit]
			});
	// 可编辑的表格
	var Grid = new Ext.grid.EditorGridPanel({
				sm : sm,
				ds : store,
				cm : sm_store_item,
				title : '编制科目',
				autoScroll : true,
				tbar : tbar,
				border : true,
				listeners : {
					'beforeedit' : function(e) {
						if (e.field == "adviceBudget") {
							var column = e.record.get('dataSource');
							if (column == "2") {
								return false;
							}
						}
					}

				},
				clicksToEdit : 1,
				viewConfig : {
	// forceFit : true
				}
			});
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

	// 从session取登录人编码姓名部门相关信息
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号
							var workerCode = result.workerCode;
							reportName.setValue(result.workerName);
							deptCode = result.deptCode;

							queryData();
						}
					}
				});
	}
	
	
	getWorkCode();
})
