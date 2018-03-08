Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	// 取当前时间
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "";
		// t = d.getDate();
		// s += (t > 9 ? "" : "0") + t + " ";
		// t = d.getHours();
		// s += (t > 9 ? "" : "0") + t + ":";
		// t = d.getMinutes();
		// s += (t > 9 ? "" : "0") + t
		// + ":";
		// t = d.getSeconds();
		// s += (t > 9 ? "" : "0") + t;
		return s;
	}
	
		//add by sychen 20100414
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
								// ,
								// onpicked : function() {
								// year.clearInvalid();
								//
								// },
								// onclearing : function() {
								// year.markInvalid();
								//
								// }
							});
					}
				}

			});
	planTime.setValue(getDate());
	
// 编辑人编码//add by sychen 20100414
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
	
			//add by fyyang 20100525
	var btnApproveQuery = new Ext.Button({
				text : '审批查询',
				disabled : true,
				iconCls : 'query',
				handler : approveQuery

			});

				
	function approveQuery() {
		if (!sm.hasSelection() || sm.getSelections().length > 1)
			Ext.Msg.alert('提示', '请选择一条数据进行查看！');
		else {
			var rec = sm.getSelected();
			var workflowType = 'bqDeptPlanFinishApprove';
			var entryId = rec.get('baseInfo.finishWorkFlowNo');
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
//	var myform = new Ext.form.FormPanel({
//				bodyStyle : "padding: 20,10,20,20",
//				layout : 'column',
//				items : [{
//							columnWidth : '.25',
//							layout : 'form',
//							border : false,
//							labelWidth : 60,
//							items : [planTime]
//						}, {
//							columnWidth : '.25',
//							labelWidth : 60,
//							layout : 'form',
//							border : false,
//							items : [editDepName]
//						},
//
//						{
//							columnWidth : '.1',
//							layout : 'form',
//							border : false,
//							labelWidth : 60,
//							items : [query]
//						}, editDepcode
//
//				]
//
//			})

	var MyRecord = Ext.data.Record.create([{
				name : 'baseInfo.depMainId'
			}, {
				name : 'editDate'
			}, {
				name : 'editDepName'
			}, {
				name : 'editByName'
			}, {
				name : 'jobContent'
			},
			{
				name : 'completeData'
			},{
				name : 'ifComplete'
			}, {
				name : 'completeDesc'
			},{
			    name:'baseInfo.finishWorkFlowNo'
			},{
			    name:'orderBy'
			},{
				name:'level1DeptName'
			}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'manageplan/queryBpJPlanJobDepDetail.action '
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
				// region : "west",
				store : store,
				layout : 'fit',
				// width:'0.5',
				columns : [

						sm, // 选择框
						number, {

							header : "部门",

							sortable : false,
//							dataIndex : 'editDepName'
							//add by sychen 20100528
							dataIndex : 'level1DeptName'

						}, {

							header : "编辑人",

							sortable : false,
							dataIndex : 'editByName'

						}, {

							header : "编辑时间",

							sortable : false,
							dataIndex : 'editDate'

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
				// 分页
//				bbar : new Ext.PagingToolbar({
//							pageSize : 18,
//							store : store,
//							displayInfo : true,
//							displayMsg : "显示第{0}条到{1}条，共{2}条",
//							emptyMsg : "没有记录",
//							beforePageText : '',
//							afterPageText : ""
//						})
			});
	/** 右边的grid * */

				grid.on("rowclick", function() {

				if (grid.getSelectionModel().hasSelection()) {
					
			        btnApproveQuery.setDisabled(false);

				} else {
				//	status.setValue(null);
					
				}
			})	
			
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [
//					{
//							bodyStyle : "padding: 20,10,20,20",
//							layout : 'fit',
//							border : false,
//							frame : false,
//							region : "north",
//							height : 100,
//							items : [myform]
//						},
						{
							// bodyStyle : "padding: 20,20,20,0",
							region : "center",
							border : false,
							frame : false,
							layout : 'fit',
							height : '50%',
							// width : '50%',
							items : [grid]
						}]
			});

getDepCodeOnly();//add by sychen 20100414
query();
})
