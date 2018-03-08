Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var arg = window.dialogArguments;
	var entryId = arg.entryId;
	//			var entryId ="196764";
	//			var actionId = "53";
	var deptParm = arg.deptParm;
	var actionId = arg.actionId;
	var sepeciality = arg.sepeciality;
	function getNextSteps() {
		Ext.Ajax.request({
			url : "MAINTWorkflow.do?action=getNextSteps",
			method : 'post',
			params : {
				entryId : entryId,
				actionId : actionId
			},
			success : function(result, request) {
				var ro = eval('(' + result.responseText + ')');
				// 会签时只显示下一步状态的第一个状态
				alreadyDs.load({
					params : {
						action : 'getEntryStepGroup',
						entryId : entryId,
						stepId : ro[0].stepId
					}
				});
			},
			failure : function() {

			}
		})
	};
	// **********************显示下一步角色*************************
	var Group = Ext.data.Record.create([{
		name : 'groupId'
	}, {
		name : 'groupName'
	}]);
	var alreadyDs = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'MAINTWorkflow.do',
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({}, Group)
	});

	//add by drdu 20100531 根据专业过滤
	alreadyDs.on("load", function() {
		if (sepeciality != null && sepeciality != "") {
			alreadyDs.filter('groupName', sepeciality, true);
		}
	})

	var alreadySm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	});

	var alreadyCm = new Ext.grid.ColumnModel([{
		header : '编码',
		dataIndex : 'groupId',
		align : 'left',
		width : 50
	}, {
		header : '角色名称',
		dataIndex : 'groupName',
		align : 'left',
		width : 120
	}]);
	alreadyCm.defaultSortable = true;
	var alreadyGrid = new Ext.grid.GridPanel({
		ds : alreadyDs,
		cm : alreadyCm,
		autoScroll : true,
		sm : alreadySm,
		title : '角色列表',
		columnWidth : .7,
		height : 380,
		fitToFrame : true,
		border : false,
		viewConfig : {
			forceFit : false
		}
	});
	alreadyGrid.enableColumnHide = false;
	alreadyGrid.on('rowclick', function() {
		var record = alreadyGrid.getSelectionModel().getSelected();
		roleId = record.data.groupId;
		person_ds.load({
			params : {
				roleId : roleId,
				deptParm : deptParm,
				start : 0,
				limit : 1
			}
		})
	})
	var person_item = Ext.data.Record.create([{
		name : 'workerCode'
	}, {
		name : 'workerName'
	}]);

	var person_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'system/getWorkerByRoleId.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list'
		}, person_item)
	});

	var personSm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	});
	var personCm = new Ext.grid.ColumnModel([personSm, {
		header : '人员编码',
		dataIndex : 'workerCode',
		align : 'left',
		width : 80
	}, {
		header : '姓名',
		dataIndex : 'workerName',
		align : 'left',
		width : 80
	}]);

	alreadyPerson = new Ext.grid.GridPanel({
		ds : person_ds,
		cm : personCm,
		autoScroll : true,
		sm : personSm,
		//						title : '角色对应人员',
//		columnWidth : .7,
//		height : 380,
//		autoHeight:true,
		fitToFrame : true,
		border : false,
		selModel : new Ext.grid.RowSelectionModel({
			singleSelect : false
		})
	})

	var appointRolesPanel = new Ext.Panel({
		//						width : 600,
//		height : 450,
		modal : true,
		//						title : '角色对应人员',
		buttonAlign : "center",
		resizable : false,
		layout : 'fit',
		items : [alreadyPerson],
		tbar : ['角色对应人员', {
			text : '确定',
			handler : function() {
				var appointRoles = alreadyPerson.getSelections();
				var nrs = "";
				var nrsname = "";
				for (var i = 0; i < appointRoles.length; i++) {
					nrs += appointRoles[i].data.workerCode + ",";
					nrsname += appointRoles[i].data.workerName + ",";
				}
				if (nrs != "") {
					nrs = nrs.substr(0, nrs.length - 1);
					nrsname = nrsname.substr(0, nrsname.length - 1);
					var ro = new Object();
					ro.nrs = nrs;
					ro.nrsname = nrsname;
					window.returnValue = ro;
					window.close();
				} else {
					window.returnValue = false;
					window.close();
				}
			}
		}, '-', {
			text : '取消',
			handler : function() {
				window.returnValue = false;
				window.close();
			}
		}],
		closeAction : 'hide'
	});

	var viewport = new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		border : false,
		frame : false,
		items : [{
			layout : 'fit',
			border : false,
			frame : false,
			region : "west",
			width : '45%',
			items : [alreadyGrid]
		}, {
			region : "center",
			border : false,
			frame : false,
			layout : 'fit',
			items : [appointRolesPanel]
		}]
	});
	getNextSteps();
});