Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var postType = "plan";
	var con_item = Ext.data.Record.create([{
				name : 'executionid1'
			}, {
				name : 'executionid2'
			}, {
				name : 'executionid3'
			}, {
				name : 'itemcode'
			}, {
				name : 'itemid'
			}, {
				name : 'itemname'
			}, {
				name : 'unitname'
			}, {
				name : 'value1'
			}, {
				name : 'value2'
			}, {
				name : 'value3'
			}]);

	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});

	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'managexam/getExecutionTable.action' 
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, con_item)
			});

	var con_item_cm = new Ext.grid.ColumnModel([con_sm,
			{
		header : '指标名称',
		dataIndex : 'itemname',
		width:200
	}, {
		header : '单位',
		dataIndex : 'unitname'
	}, {
		header : '#11、12机',
		dataIndex : 'value1',
		editor :  new Ext.form.NumberField(
													{
														allowDecimals : true,
														decimalPrecision : 4
													})
	}, {
		header : '#1、2机',
		dataIndex : 'value2',
		editor :  new Ext.form.NumberField(
													{
														allowDecimals : true,
														decimalPrecision : 4
													})
	}, {
		header : '全厂合并',
		dataIndex : 'value3',
		editor :  new Ext.form.NumberField(
													{
														allowDecimals : true,
														decimalPrecision : 4
													})
	} 
	]);

	con_item_cm.defaultSortable = false;

	var gridbbar = new Ext.PagingToolbar({
				pageSize : 18,
				store : con_ds, 
				displayInfo : true,
				displayMsg : "共{2}条",
				emptyMsg : "没有记录",
				beforePageText : '',
				afterPageText : ""
			}); 
	// 载入数据
	function getTopic() {
		con_ds.load({
					params : {
						type : postType,
						datetime : meetingMonth.getValue()
					}
				});
	} 
	// 保存
	function saveTopic() {
		Grid.stopEditing();
		var modifyRec = Grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0) {
			if (!confirm("确定要保存修改吗?"))
				return;
			// var newData = new Array();
			var updateData = new Array();
			for (var i = 0; i < modifyRec.length; i++) { 
				updateData.push(modifyRec[i].data);
			}
			Ext.Ajax.request({
						url : 'managexam/saveExecutionTable.action',
						method : 'post',
						params : {
							addOrUpdateRecords : Ext.util.JSON
									.encode(updateData),
							type : postType,
							datetime : meetingMonth.getValue()
						},
						success : function(result, request) {
							var o = eval('(' + result.responseText + ')');
							Ext.MessageBox.alert('提示信息', o.msg);
							con_ds.rejectChanges();
							
							con_ds.reload();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('提示信息', '未知错误！')
						}
					})
		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}

	// 发布
	function issueTopic() {
		Grid.stopEditing();
		if(confirm("发布后数据将不可修改，确定要发布吗？"))
		{
		Ext.Ajax.request({
					url : 'managexam/issueExecutionTable.action',
					method : 'post',
					params : {
						datetime : meetingMonth.getValue()
					},
					success : function(result, request) {
						var o = eval('(' + result.responseText + ')');
						Ext.MessageBox.alert('提示信息', o.msg);
						con_ds.rejectChanges();
						
						con_ds.reload();
					},
					failure : function(result, request) {
						Ext.MessageBox.alert('提示信息', '未知错误！')
					}
				})
		}
	}

	// 取消
	function cancerTopic() {
		var modifyRec = con_ds.getModifiedRecords();
		if (modifyRec.length > 0 ) {
			if (!confirm("确定要放弃修改吗"))
				return;
			con_ds.reload();
			con_ds.rejectChanges();
			
		} else {
			con_ds.reload();
			con_ds.rejectChanges();
			
		}
	}

	function getMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		return s;
	}

	var meetingMonth = new Ext.form.TextField({
				style : 'cursor:pointer', 
				columnWidth : 0.5,
				readOnly : true,
				anchor : "70%",
				fieldLabel : '月份',
				name : 'month',
				value : getMonth(),
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M',
									dateFmt : 'yyyy-MM',
									alwaysUseStartDate : true
								});
						this.blur();
					}
				}
			});

	var contbar = new Ext.Toolbar({
				items : [
						'月份', meetingMonth, {
							id : 'btnQuery',
							iconCls : 'query',
							text : "查询",
							handler : getTopic
						}, '-', {
							id : 'btnSave',
							iconCls : 'save',
							text : "保存",
							handler : saveTopic
						}, '-', {
							id : 'btnIssue',
							iconCls : 'upcommit',
							text : "发布",
							handler : issueTopic
						}, '-', {
							id : 'btnCancer',
							iconCls : 'cancer',
							text : "取消",
							handler : cancerTopic
						}]

			});

	var Grid = new Ext.grid.EditorGridPanel({
		clicksToEdit:1,
				viewConfig : {
					forceFit : false
				},
				sm : con_sm,
				ds : con_ds,
				cm : con_item_cm,
//				height : 425,
				split : true,
				autoScroll : true,
				layout : 'fit',
				frame : false, 
				tbar : contbar,
				border : true
			}); 
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "fit",
				border : false,
				frame : false,
				items : [	Grid]
			});
})