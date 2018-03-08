Ext.namespace('Reward.rewardGroup');
Reward.rewardName = function(confing) {
	var monthDate = confing.monthDate;
	var deptId = confing.deptId;

	var myRecord = new Ext.data.Record.create([{
				name : 'bigRewardMonth',
				mapping : '0'
			}, {
				name : 'bigAwardId',
				mapping : '1'
			}, {
				name : 'bigAwardName',
				mapping : '2'
			}, {
				name : 'bigRewardBase',
				mapping : '3'
			}])

	var dataProxy = new Ext.data.HttpProxy({
				url : 'hr/getBigAwareNameList.action'
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
		monthDate : monthDate,
		deptId : deptId
	}

	groupStore.load();

	// 时间
	var grantMonth = new Ext.form.TextField({
		width : 250,
		style : 'cursor:pointer',
		name : "rwGrant.bigGrantMonth",
		fieldLabel : '时间',
		allowBlank : true,
		readOnly : true
			// ,
			// listeners : {
			// focus : function() {
			// WdatePicker({
			// isShowClear : true,
			// startDate : '%y-%M',
			// dateFmt : 'yyyy-MM'
			// });
			// this.blur();
			// }

			// }
		});
	var btnQuery = new Ext.Button({
				text : "查询",
				id : 'btnQuery',
				iconCls : 'query',
				handler : function() {
					groupStore.load({
								params : {
									monthDate : grantMonth.getValue(),
									deptId : deptId
								}
							});
				}
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
							header : 'bigAwardId',
							dataIndex : 'bigAwardId',
							hidden : true
						}, {
							header : '月度',
							dataIndex : 'bigRewardMonth',
							width : 150
						}, {
							header : '大奖名称',
							dataIndex : 'bigAwardName',
							width : 150
						}, {
							header : '基数',
							dataIndex : 'bigRewardBase',
							width : 150
						}],
				sm : groupSm,
				tbar : ["月份:", grantMonth, '->',
						"<font color ='red'>双击选中</font>"]
			})
	var groupWin = new Ext.Window({
				closeAction : 'hide',
				width : 500,
				height : 450,
				title : "大奖名称",
				modal : true,
				layout : 'border',
				items : [{
							region : 'center',
							layout : 'fit',
							split : true,
							items : [groupGrid]
						}]
			});
	grantMonth.setValue(monthDate)
	this.win = groupWin;
	this.groupGrid = groupGrid;

};