Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'qualificationId'
	}, {
		name : 'cliendId'
	}, {
		name : 'clientName'
	}, {
		name : 'aptitudeName'
	}, {
		name : 'qualificationOrg'
	}, {
		name : 'sendPaperDate'
	}, {
		name : 'beginDate'
	}, {
		name : 'endDate'
	}, {
		name : 'memo'
	}, {
		name : 'lastModifiedName'
	}, {
		name : 'lastModifiedDate'
	}]);
	var dataProxy = new Ext.data.HttpProxy({
				url : 'clients/findClientsQualificationList.action'
			});
	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);
	
	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
	// 分页
	var fuzzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});

	var sm = new Ext.grid.CheckboxSelectionModel({
	//	singleSelect : true
	});

	var grid = new Ext.grid.GridPanel({
		region : "center",
		store : store,
		viewConfig : {
			forceFit : true
		},
		loadMask : {
			msg : '<img src="comm/images/extanim32.gif" width="32" height="32" style="margin-right: 8px;" align="absmiddle" />读取数据中 ...'
		},
		columns : [sm, new Ext.grid.RowNumberer({
			header : '序号',
			width : 35,
			align : 'left'
		}), {
			header : "ID",
			width : 100,
			sortable : true,
			dataIndex : 'qualificationId',
			hidden : true
		}, {
			header : "资质材料名称",
			width : 110,
			sortable : true,
			dataIndex : 'aptitudeName'
		}, {
			header : "合作伙伴",
			width : 130,
			sortable : true,
			hidden : true,
			dataIndex : 'cliendId'
		}, {
			header : "合作伙伴名称",
			width : 130,
			sortable : true,
			dataIndex : 'clientName'
		}, {
			header : "发证机构",
			width : 130,
			sortable : true,
			dataIndex : 'qualificationOrg'
		}, {
			header : "发证日期",
			width : 80,
			sortable : true,
			dataIndex : 'sendPaperDate'
		}, {
			header : "证书生效日期",
			width : 130,
			sortable : true,
			dataIndex : 'beginDate'
		}, {
			header : "证书失效日期",
			width : 80,
			sortable : true,
			align : 'center',
			dataIndex : 'endDate'
		}, {
			header : "备注",
			width : 120,
			sortable : true,
			align : 'center',
			dataIndex : 'memo'
		}, {
			header : "登记人",
			width : 120,
			sortable : true,
			align : 'center',
			dataIndex : 'lastModifiedName'
		}, {
			header : "登记日期",
			width : 120,
			sortable : true,
			align : 'center',
			dataIndex : 'lastModifiedDate'
		}],
		sm : sm,
		tbar : ['资质材料名称/合作伙伴名称:', fuzzy, {
			text : "查询",
			iconCls : 'query',
			handler : queryRecord
		}, '-', {
			text : "增加",
			iconCls : 'add',
			handler : function() {
				parent.currentRecord=null;
				parent.document.all.iframe2.src = "manage/client/maint/clientQualification/clientQualificationBase.jsp";
				parent.Ext.getCmp("maintab").setActiveTab(0);
			}
		}, '-', {
			text : "修改",
			iconCls : 'update',
			handler : updateRecord
		}, '-', {
			text : "删除",
			iconCls : 'delete',
			handler : deleteRecord
		}],
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});
	grid.on('rowdblclick', updateRecord);
	
	function updateRecord() {
		if (grid.selModel.hasSelection()) {
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				var member = records[0];
				parent.currentRecord = member;
				var url = "manage/client/maint/clientQualification/clientQualificationBase.jsp";
				parent.document.all.iframe2.src = url;
				parent.Ext.getCmp("maintab").setActiveTab(0);
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}
	function deleteRecord() {
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		var nos = [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {

			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("qualificationId")) {
					ids.push(member.get("qualificationId"));
					nos.push(member.get("aptitudeName"));
				} else {

					store.remove(store.getAt(i));
				}
			}
			Ext.Msg.confirm("删除", "是否确定删除合作伙伴资质名称为'" + nos + "'的记录？", function(
					buttonobj) {
				if (buttonobj == "yes") {
					Ext.lib.Ajax.request('POST',
							'clients/deleteQualification.action', {
								success : function(action) {
									Ext.Msg.alert("提示", "删除成功！");
									queryRecord();
									//删除记录刷新登记页面
									parent.currentRecord = null;
									parent.document.all.iframe2.src = "manage/client/maint/clientQualification/clientQualificationBase.jsp";

								},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
							}, 'ids=' + ids);
				}
			});
		}
	}
	function queryRecord() {

		store.load({
			params : {
				start : 0,
				limit : 18,
				fuzzy:Ext.get('fuzzy').dom.value
			}
		});
	}

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});

	queryRecord();

});