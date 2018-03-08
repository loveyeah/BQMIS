Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var month = getParameter("month");
	
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'trainingDep',
		mapping : 0
	}, {
		name : 'trainingDepName',
		mapping : 1
	}]);
	var dataProxy = new Ext.data.HttpProxy({
				url : 'manageplan/findTraingDeptReturnSelectList.action'
			});
	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);
	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});

	store.load({
		params : {
			month : month
		}
	});

	var sm = new Ext.grid.CheckboxSelectionModel();
	var grid = new Ext.grid.GridPanel({
		region : "center",
		store : store,
		columns : [sm, new Ext.grid.RowNumberer({
			header : '序号',
			width : 35,
			align : 'left'
		}), {
			header : "部门",
			width : 100,
			sortable : true,
			dataIndex : 'trainingDep',
			hidden : false
		}, {
			header : "部门",
			width : 150,
			sortable : true,
			dataIndex : 'trainingDepName'
		}],
		sm : sm,
		tbar : [{
			text : "确定",
			iconCls : 'update',
			handler : getRecord
		}, {
			text : "关闭",
			iconCls : 'delete',
			handler : function() {
				window.close();
			}
		}]
	});
	grid.on("dblclick", getRecord);
	// function queryRecord() {
	// store.load();
	// }

	function getRecord() {
		var mysm = grid.getSelectionModel();
		var selected = mysm.getSelections();
		var names = [];
		var codes = [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择部门！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.trainingDep) {
					names.push(member.trainingDepName);
					codes.push(member.trainingDep);
				} else {
					store.remove(store.getAt(i));
				}
			}
			Ext.Msg.confirm("选择", "是否确定选择'" + names + "'？",
					function(buttonobj) {

						if (buttonobj == "yes") {
							var dept = new Object();
							dept.code = codes.toString();
							dept.name = names.toString();

							window.returnValue = dept;
							window.close();
						}
					})
		}
	}
	// ---------------------------------------
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});

		// queryRecord();

	});