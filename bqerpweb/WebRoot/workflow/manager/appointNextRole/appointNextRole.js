Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() { 
	var arg = window.dialogArguments; 
	var entryId = arg.entryId; 
	var actionId = arg.actionId;  
	var deptId = arg.deptId;
	function getNextSteps() { 
		Ext.Ajax.request({
			url : "MAINTWorkflow.do?action=getNextSteps",
			method : 'post',
			params : {
				entryId  : entryId,
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
	//	autoScroll:true,
		sm : alreadySm,
		title : '角色列表(全部不选为默认配置)',
		columnWidth : .7,
	//	height : 380,
	//	fitToFrame : true,
		border : false,
		selModel : new Ext.grid.RowSelectionModel({
			singleSelect : false
		})
	});
	alreadyGrid.enableColumnHide = false; 
	var appointRolesPanel = new Ext.Panel({
		region:'center',
		width : 600,
		height : 450,
		modal : true,
		layout:'fit',
		buttonAlign : "center",
		resizable : false,
		items : [alreadyGrid],
		buttons : [{
			text : '确定',
			handler : function() {
				var appointRoles = alreadyGrid.getSelections();
				var nrs = "";
				var nrsname = "";
				for (var i = 0; i < appointRoles.length; i++) {
					nrs += appointRoles[i].data.groupId + ",";
					nrsname += appointRoles[i].data.groupName + ",";
				} 
				if (nrs != "") {
					
					nrs = nrs.substr(0, nrs.length - 1);
					nrsname = nrsname.substr(0, nrsname.length - 1);
					var ro = new Object();
					ro.nrs = nrs;
					ro.nrsname = nrsname;  
					window.returnValue = ro; 
					window.close();
				}
				else{
					window.returnValue = false;
					window.close();
				}  
			}
		}, {
			text : '取消',
			handler : function() { 
				window.returnValue = false;
				window.close();
			}
		}],
		closeAction : 'hide'
	});		
	var viewport = new Ext.Viewport({
		layout : "border",
		items : [appointRolesPanel]
	});
	getNextSteps();
});