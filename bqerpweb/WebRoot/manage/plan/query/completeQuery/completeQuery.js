Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	// 取当前时间
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "";
		return s;
	}
	
		
		// 从session取登录人部门编码
	function getDepCodeOnly() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							var DeptCode = result.deptCode;
							var workerCode = result.workerCode;
							editBy.setValue(workerCode);
						}
					}
				});
	}
	
	// 计划时间
	var planTime = new Ext.form.TextField({

				name : 'planTime',
				fieldLabel : '计划时间',
				listeners : {
					focus : function() {
						WdatePicker({
							startDate : '',
							alwaysUseStartDate : true,
							dateFmt : "yyyy-MM"
							});
					}
				}

			});
	planTime.setValue(getDate());
	
// 编辑人编码
	var editBy = new Ext.form.TextField({
				name : 'editBy'
			});
	
	// 编辑人所在部门名称
	var editDepName = new Ext.form.ComboBox({
		fieldLabel : '编辑部门',
		readOnly : true,
		onTriggerClick : function() {
			var args = {
				selectModel : 'single',
				rootNode : {
					id : "0",
					text : '灞桥热电厂'
				}
			}
			var url = "/power/comm/jsp/hr/dept/dept.jsp";
			var rvo = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:250px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (typeof(rvo) != "undefined") {
				editDepcode.setValue(rvo.codes);
				editDepName.setValue(rvo.names);

			}
		}

	});

	// 编辑人所在部门编码
	var editDepcode = new Ext.form.Hidden({
				name : 'editDepcode'
			});
	function query() {
		store.load({
					params : {
						flag : 'query',
						editDepcode : editDepcode.getValue(),
						planTime : planTime.getValue()
					}
				})

	}
	var btnQuery = new Ext.Button({
				id : 'btnQuery',
				text : '查询',
				iconCls : 'query',
				handler : query

			});
	
	var btnApproveQuery = new Ext.Button({
				text : '审批查询',
				disabled : true,
				iconCls : 'write',
				handler : approveQuery

			});

				
	function approveQuery() {
		if (!sm.hasSelection() || sm.getSelections().length > 1)
			Ext.Msg.alert('提示', '请选择一条数据进行查看！');
		else {
			var rec = sm.getSelected();
			var workflowType = 'bqDeptPlanFinishApprove';
			var entryId = rec.get('finishWorkFlowNo');
			var url;
			if (entryId == null || entryId == "")
				url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
						+ workflowType;
			else
				url = url = "/power/workflow/manager/show/show.jsp?entryId="
						+ entryId;
			window.open(url);

		}
	}

	var MyRecord = Ext.data.Record.create([{
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
				name:'finishEditDate',
				mapping : 12
			},{
				name : 'finishSignStatus',
				mapping : 13
			},{
				name : 'finishWorkFlowNo',
				mapping : 14
			},{
				name : 'linkJobId',
				mapping : 15
			},{
				name : 'level1DeptName',
				mapping : 16
			}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'manageplan/getPlanJobCompleteQuestList.action '
			});

	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"

			}, MyRecord);

	var store = new Ext.data.Store({

				proxy : dataProxy,

				reader : theReader

			});

	var sm = new Ext.grid.CheckboxSelectionModel();

	number = new Ext.grid.RowNumberer({
				header : "",
				align : 'left'
			})

	// 定义grid

	var grid = new Ext.grid.GridPanel({
				store : store,
				layout : 'fit',
				columns : [

						sm, // 选择框
						number, {
							header : "部门",
							sortable : false,
							dataIndex : 'level1DeptName'

						}, {
							header : "编辑人",
							sortable : false,
							dataIndex : 'editName'

						}, {

							header : "编辑时间",
							sortable : false,
							dataIndex : 'finishEditDate'

						}, {
							width : 100,
							header : "序号",
							sortable : false,
							align:'center',
							dataIndex : 'orderBy'

						}, {
							width : 300,
							header : "工作内容",
							sortable : false,
							dataIndex : 'jobContent'

						},{

							header : "完成时间",

							sortable : false,
							dataIndex : 'completeData',
							renderer : function changeIt(val) {
								if (val == "0") {
									return "当月";
								} else if (val == "1") {
									return "跨月";
								} else if (val == "2") {
									return "长期";
								}else if (val == "3") {
									return "全年";
								} else {
									return "";
								}
							}
						},{

							header : "完成情况",

							sortable : false,
							dataIndex : 'ifComplete',
							renderer : function changeIt(val) {
								if (val == "0") {
									return "未完成";
								} else if (val == "1") {
									return "进行中";
								} else if (val == "2") {
									return "已完成";
								} else {
									return "";
								}
							}
						}, {
							width : 300,
							header : "考核说明",

							sortable : false,
							dataIndex : 'completeDesc'
						}],

				sm : sm, // 选择框的选择 Shorthand for
				// selModel（selectModel）
				//顶部工具栏
				tbar : [
					"计划时间:",planTime,'-',
					"编辑部门:",editDepName,'-',
					btnQuery,
					'-',btnApproveQuery
				]
			});
	/** 右边的grid * */

				grid.on("rowclick", function() {

				if (grid.getSelectionModel().hasSelection()) {
					
			        btnApproveQuery.setDisabled(false);

				} else {
					
				}
			})	
			
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [{
							region : "center",
							border : false,
							frame : false,
							layout : 'fit',
							height : '50%',
							items : [grid]
						}]
			});

getDepCodeOnly();
query();
})
