Ext.namespace('Reward.rewardGroup');
Reward.rewardGroup = function(confing) {
	var deptId = confing.deptId;
	var deptName=confing.deptName;

	var myRecord = new Ext.data.Record.create([{
				name : 'groupId',
				mapping : '0'
			}, {
				name : 'groupName',
				mapping : '1'
			}, {
				name : 'deptName',
				mapping : '2'
			}])

	var dataProxy = new Ext.data.HttpProxy({
				url : 'hr/getGroupNameList.action'
			})
	var theReader = new Ext.data.JsonReader({
			// root : 'list',
			// totalProperty : 'totalCount'
			}, myRecord);

	var groupStore = new Ext.data.Store({
				proxy : dataProxy,
				reader : theReader
			})

	groupStore.baseParams = {
		deptId : deptId
	}

	groupStore.load();
	groupStore.on("load",function()
	{
		var obj= new myRecord({
			groupId:deptId,
			groupName:deptName,
			deptName:deptName
		});
		groupStore.add(obj);
	})

	var txtGroupName = new Ext.form.TextField({
				id : 'txtGroupName',
				name : 'txtGroupName',
				fieldLabel : '班组名称',
				width : 130
			})
	var btnQuery = new Ext.Button({
				text : "查询",
				id : 'btnQuery',
				iconCls : 'query'
			})

	var btnConfirm = new Ext.Button({
				text : '确定',
				id : 'btnConfrim',
				iconCls : 'confirm',
				handler : function() {
					win.hide();
				}
			})
	var groupSm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			})

	var groupGrid = new Ext.grid.GridPanel({
				region : "center",
				layout : 'fit',
				store : groupStore,
				columns : [groupSm, {
							header : 'groupId',
							dataIndex : 'groupId',
							hidden : true
						}, {
							header : '班组',
							dataIndex : 'groupName',
							width : 200
						}, {
							header : '部门',
							dataIndex : 'deptName',
							width : 200
						}],
				sm : groupSm,
				tbar : ["班组名称:", txtGroupName, '-', btnQuery, '->',
						"<font color ='red'>双击选中</font>"]
			})
	var groupWin = new Ext.Window({
				closeAction : 'hide',
				width : 500,
				height : 450,
				title : "证书类别",
				modal : true,
				layout : 'border',
				items : [{
							region : 'center',
							layout : 'fit',
							split : true,
							items : [groupGrid]
						}]
			});

	this.win = groupWin;
	this.groupGrid = groupGrid;

};