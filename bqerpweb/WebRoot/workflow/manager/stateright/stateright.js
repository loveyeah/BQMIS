Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
var Process = Ext.data.Record.create([{
			name : 'workflowType'
		}, {
			name : 'name'
		}]);
var processDs = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
						url : 'MAINTWorkflow.do?action=workflowlist',
						method : 'post'
					}),
			reader : new Ext.data.JsonReader({}, Process)
		});
processDs.load();
var processSm = new Ext.grid.CheckboxSelectionModel({
			singleSelect : true,
			listeners : {
				rowselect : function(sm, row, rec) {
					Ext.Msg.wait("请等候", "加载中", "操作进行中...");
					var type = rec.data.workflowType;
					var name = rec.data.name;
					xmlloadWF(type);

					setTimeout(function() {
								Ext.Msg.hide();
							}, 250);
				}
			}
		});
var processCm = new Ext.grid.ColumnModel([processSm, {
			header : '编码',
			dataIndex : 'workflowType',
			align : 'left',
			width : 70
		}, {
			header : '流程名称',
			dataIndex : 'name',
			align : 'left',
			width : 155
		}]);
processCm.defaultSortable = true;

var workFlowText = new Ext.form.TextField({
			id : 'workFlowText',
			name : 'workFlowText',
			width : 100,
			fieldLabel : '工作流名称'
		})

var btnWorkFlow = new Ext.Button({
			id : 'btnWorkFlow',
			iconCls : 'query',
			text : "查询",
			handler : querydata
		})
function querydata() {
	processDs.load();
	processDs.on("load", function() {
				var name = workFlowText.getValue();
				if (name != null && name != "") {
					processGrid.store.filter('name', name,true);
				}
			})
}
var btntbar = new Ext.Toolbar({
			items : ["工作流名称", workFlowText, '-', btnWorkFlow]
		})

var processGrid = new Ext.grid.GridPanel({
			el : 'workflows',
			ds : processDs,
			cm : processCm,
			sm : processSm,
//			title : '工作流列表',
			width : 246,
			height : 600,
			border : false,
			tbar : btntbar
		});
processGrid.enableColumnHide = false;
Ext.onReady(function() {
			processGrid.render();
		});

	// 签字时的Enter
	document.onkeydown = function() {
		if (event.keyCode == 13 && document.getElementById('btnWorkFlow')) {
			document.getElementById('btnWorkFlow').click();
		}
	}		
		
// /////////////////////////////////
var Group = Ext.data.Record.create([{
			name : 'groupId'
		}, {
			name : 'groupName'
		}]);
var waitDs = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
						url : 'MAINTWorkflow.do',
						method : 'post'
					}),
			reader : new Ext.data.JsonReader({}, Group)
		});
var waitSm = new Ext.grid.CheckboxSelectionModel({
			singleSelect : false
		});
var waitCm = new Ext.grid.ColumnModel([waitSm, {
			header : '编码',
			dataIndex : 'groupId',
			align : 'left',
			width : 50
		}, {
			header : '角色名称',
			dataIndex : 'groupName',
			align : 'left',
			width : 150
		}]);
waitCm.defaultSortable = true;
var waitGrid = new Ext.grid.GridPanel({
			ds : waitDs,
			cm : waitCm,
			sm : waitSm,
			title : '未选角色列表',

			columnWidth : .45,
			height : 380,
			fitToFrame : true,
			border : false,
			selModel : new Ext.grid.RowSelectionModel({
						singleSelect : false
					})
		});
waitGrid.enableColumnHide = false;
// /////////////////////////////////
var alreadyDs = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
						url : 'MAINTWorkflow.do',
						method : 'post'
					}),
			reader : new Ext.data.JsonReader({}, Group)
		});
var alreadySm = new Ext.grid.CheckboxSelectionModel({
			singleSelect : false
		});
var alreadyCm = new Ext.grid.ColumnModel([alreadySm, {
			header : '编码',
			dataIndex : 'groupId',
			align : 'left',
			width : 50
		}, {
			header : '角色名称',
			dataIndex : 'groupName',
			align : 'left',
			width : 150
		}]);
alreadyCm.defaultSortable = true;
var alreadyGrid = new Ext.grid.GridPanel({
			ds : alreadyDs,
			cm : alreadyCm,
			sm : alreadySm,
			title : '已选角色列表',
			columnWidth : .45,
			height : 380,
			fitToFrame : true,
			border : false,
			selModel : new Ext.grid.RowSelectionModel({
						singleSelect : false
					})
		});
alreadyGrid.enableColumnHide = false;
// 操作按钮事件
var opebar = new Ext.Toolbar({
	layout : 'form',
	items : [{
		id : 'btnRevoke',
		text : ">>>",
		handler : function() {
			var selectedRows = waitGrid.getSelectionModel().getSelections();
			var ids = "";
			for (i = 0; i < selectedRows.length; i++) {
				ids += selectedRows[i].data.groupId + ",";
			}
			if (ids.length > 0) {
				ids = ids.substring(0, ids.length - 1);
				var stepId = curIcon.activity
						.selectSingleNode("propertyList/property[@name='ActivityCode']/value").text;
				Ext.Ajax.request({
							url : 'MAINTWorkflow.do',
							method : 'post',
							params : {
								action : 'grantGroup',
								flowType : flowType,
								stepId : stepId,
								roleIds : ids
							},
							success : function(result, request) {
								loadGridData(stepId);
							}
						});
			}
		}
	}, {
		id : 'btnGrant',
		text : "<<<",
		handler : function() {
			var selectedRows = alreadyGrid.getSelectionModel().getSelections();
			var ids = "";
			for (i = 0; i < selectedRows.length; i++) {
				ids += selectedRows[i].data.groupId + ",";
			}
			if (ids.length > 0) {
				var stepId = curIcon.activity
						.selectSingleNode("propertyList/property[@name='ActivityCode']/value").text;
				ids = ids.substring(0, ids.length - 1);
				Ext.Ajax.request({
							url : 'MAINTWorkflow.do',
							method : 'post',
							params : {
								action : 'revokeGroup',
								flowType : flowType,
								stepId : stepId,
								roleIds : ids
							},
							success : function(result, request) {
								loadGridData(stepId);
							}
						});

			}
		}
	}]
});
// 弹出设置角色窗口
var roleManagerWin = new Ext.Window({
			layout : 'column',
			modal : true,
			width : 650,
			height : 400,
			items : [waitGrid, opebar, alreadyGrid],
			closeAction : 'hide'
		});
// 设置活动角色
function setActivityRole() {
	var stepId = curIcon.activity
			.selectSingleNode("propertyList/property[@name='ActivityCode']/value").text;
	var activityType = curIcon.activity.getAttribute("typeName");
	if (activityType == "JoinActivity" || activityType == "RouterActivity") {
		Ext.Msg.alert("提示", "路由活动/聚合活动是自动触发<br/>不能设置角色权限!");
	} else if (activityType == "EndActivity") {
		Ext.Msg.alert("提示", "结束活动不能设置角色权限!");
	} else {
		loadGridData(stepId);
		roleManagerWin.show();
	}
}
// 加载数据
function loadGridData(stepId) {
	alreadyDs.load({
				params : {
					action : 'alreadyGroup',
					flowType : flowType,
					stepId : stepId
				}
			});
	waitDs.load({
				params : {
					action : 'waitGroup',
					flowType : flowType,
					stepId : stepId
				}
			});
}
function setActivityInfo() {
	stepWin.show();
	var stepId = curIcon.activity
			.selectSingleNode("propertyList/property[@name='ActivityCode']/value").text;
	Ext.Ajax.request({
				url : 'MAINTWorkflow.do',
				method : 'post',
				params : {
					action : 'getStepTimeLimit',
					flowType : flowType,
					stepId : stepId
				},
				success : function(result, request) {
					Ext.get("timeLimit").dom.value = result.responseText;
				}
			});
}
var stepInfoForm = new Ext.FormPanel({
	frame : true,
	autoWidth : true,
	autoHeight : true,
	align : 'center',
	labelAlign : 'left',
	items : [{
				xtype : 'fieldset',
				labelAlign : 'right',
				defaultType : 'textfield',
				autoHeight : true,
				border : true,
				defaults : {
					width : 270
				},
				items : [{
							xtype : 'numberfield',
							name : "timeLimit",
							allowBlank : false,
							fieldLabel : '审批时限'
						}]
			}],
	buttons : [{
		text : '保存',
		iconCls : 'delete',
		handler : function() {
			if (stepInfoForm.getForm().isValid()) {
				Ext.Msg.wait("正在保存...");
				var stepId = curIcon.activity
						.selectSingleNode("propertyList/property[@name='ActivityCode']/value").text;
				Ext.Ajax.request({
							url : 'MAINTWorkflow.do',
							method : 'post',
							params : {
								action : 'addOrUpdateStepInfo',
								flowType : flowType,
								stepId : stepId,
								timeLimit : Ext.get("timeLimit").dom.value
							},
							success : function(result, request) {
								Ext.Msg.hide();
								stepWin.hide();
							}
						});
			}
		}
	}, {
		text : '取消',
		iconCls : 'delete',
		handler : function() {
			stepWin.hide();
		}
	}]
});

var stepWin = new Ext.Window({
			width : 650,
			height : 400,
			items : [stepInfoForm],
			closeAction : 'hide'
		});

function xmlloadWF(type) {
	Ext.Ajax.request({
				url : 'MAINTWorkflow.do',
				method : 'post',
				params : {
					action : 'load',
					flowType : type
				},
				success : function(result, request) {
					flowType = type;
					var xml = result.responseText;
					if (xml != null && xml != "") {
						schema.loadByXmlData(xml);
					}
				}
			});
}
// });
