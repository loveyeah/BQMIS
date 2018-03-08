Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var mantype = "";
	var method = "add";
	var pageSize = 18;
	// 已存在的人员
	str = "";
	
	function moneyFormat(v) {
		if (v == null || v == "") {
			return "0.00";
		}
		v = (Math.round((v - 0) * 100)) / 100;
		v = (v == Math.floor(v)) ? v + ".00" : ((v * 10 == Math.floor(v * 10))
				? v + "0"
				: v);

		v = String(v);
		var ps = v.split('.');
		var whole = ps[0];
		var sub = ps[1] ? '.' + ps[1] : '.00';
		var r = /(\d+)(\d{3})/;
		while (r.test(whole)) {
			whole = whole.replace(r, '$1' + ',' + '$2');
		}
		v = whole + sub;
		if (v.charAt(0) == '-') {
			return '-' + v.substr(1);
		}
		return "￥" + v;
	};

	// 人员列表

	var encourageManName = {
		header : "人员",
		sortable : false,
		dataIndex : 'punishManName'
	}

	var encourageMan = new Ext.form.TextField({
				dataIndex : 'punishDetailsInfo.punishMan',
				hidden : true
			});

	var encourageWay = {
		header : "处罚方式",
		dataIndex : 'punishDetailsInfo.punishType'
	};

	var encourageMoney = {
		header : "处罚金额（元）",
		align : 'right',
		dataIndex : 'punishDetailsInfo.punishMoney',
		renderer : function(value, cellmeta, record, rowIndex, columnIndex,
				store) {
			if (rowIndex < store.getCount()) {
				return moneyFormat(value)
			}
		}
	};

	var absSm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});

	var absStorelist = new Ext.data.Record.create([absSm, {
				name : 'punishDetailsInfo.punishDetailsId'
			}, {
				name : 'punishDetailsInfo.punishId'
			}, {
				name : 'punishDetailsInfo.punishMan'
			}, {
				name : 'punishDetailsInfo.punishType'
			}, {
				name : 'punishDetailsInfo.punishMoney'
			}, {
				name : 'punishManName'
			}, {
				name : 'punishManDep'
			}]);

	var absStore = new Ext.data.JsonStore({
				url : 'security/getSafePunishDetailsList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : absStorelist
			});

	// 缺席人员Grid
	var absManlistGrid = new Ext.grid.EditorGridPanel({
				store : absStore,
				columns : [new Ext.grid.RowNumberer(), encourageManName,
						encourageMan, {
							header : "所在部门",
							width : 100,
							sortable : false,
							dataIndex : 'punishManDep'
						}, encourageWay, encourageMoney],
				viewConfig : {
					forceFit : true
				},
				sm : absSm,
				frame : true,
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});

	// 人员列表
	var yeardata = '';
	var myDate = new Date();

	var myMonth = myDate.getMonth() + 1;

	myMonth = (myMonth < 10 ? "0" + myMonth : myMonth);

	var myDay = myDate.getDate();

	myDay = (myDay < 10 ? "0" + myDay : myDay);

	for (var i = 2004; i < myDate.getFullYear() + 2; i++) {
		if (i < myDate.getFullYear() + 1)
			yeardata += '[' + i + ',' + i + '],';
		else
			yeardata += '[' + i + ',' + i + ']';
	}
	var yeardata = eval('[' + yeardata + ']');

	var meetingYear = new Ext.form.ComboBox({
				xtype : "combo",
				store : new Ext.data.SimpleStore({
							fields : ['value', 'key'],
							data : yeardata
						}),
				id : 'itemType',
				name : 'itemType',
				valueField : "key",
				displayField : "value",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'statItem.itemType',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : true,
				width : 80,
				value : myDate.getFullYear()
			});

	var westbtnAdd = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : function() {
					westStore.load({
								params : {
									start : 0,
									limit : pageSize
								}
							});
				}
			});

	var westSm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});

	var westStorelist = new Ext.data.Record.create([westSm, {
				name : 'punishInfo.punishId'
			}, {
				name : 'punishDepName'
			}, {
				name : 'punishDateString'
			}]);

	var westStore = new Ext.data.JsonStore({
				url : 'security/getSafePunishList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : westStorelist
			});

	// 左侧Grid
	var westgrid = new Ext.grid.GridPanel({
				store : westStore,
				columns : [new Ext.grid.RowNumberer(), {
							header : "处罚部门",
							width : 210,
							sortable : false,
							dataIndex : 'punishDepName'
						}, {
							header : "处罚时间",
							width : 90,
							sortable : false,
							dataIndex : 'punishDateString'
						}],
				viewConfig : {
					forceFit : true
				},
				tbar : ['处罚时间 ', meetingYear, westbtnAdd],
				sm : westSm,
				bbar : new Ext.PagingToolbar({
							pageSize : pageSize,
							store : westStore,
							displayInfo : true,
							displayMsg : "{2}条记录",
							emptyMsg : "没有记录"
						}),
				frame : true,
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});

	westgrid.on('rowclick', function(grid, rowIndex, e) {
				Ext.Ajax.request({
							url : 'security/getSafePunish.action',
							params : {
								punishId : grid.getStore().getAt(rowIndex)
										.get("punishInfo.punishId")
							},
							method : 'post',
							waitMsg : '正在加载数据...',
							success : function(result, request) {
								var o = eval('(' + result.responseText + ')');

								awardid.setValue(grid.getStore()
										.getAt(rowIndex)
										.get("punishInfo.punishId"));
								department.setValue(o.punishDepName != null
										? o.punishDepName
										: "");
								depCode.setValue(o.punishInfo.punishDep != null
										? o.punishInfo.punishDep
										: "");
								timeSelect.setValue(o.punishDateString != null
										? o.punishDateString
										: "");
								approvalMan.setValue(o.approvalByName != null
										? o.approvalByName
										: "");
								approvalBy
										.setValue(o.punishInfo.approvalBy != null
												? o.punishInfo.approvalBy
												: "");
								completeMan.setValue(o.completeByName != null
										? o.completeByName
										: "");
								completeBy
										.setValue(o.punishInfo.completeBy != null
												? o.punishInfo.completeBy
												: "");
								place
										.setValue(o.punishInfo.punishOpinion != null
												? o.punishInfo.punishOpinion
												: "");
								memo.setValue(o.punishInfo.punishReason != null
										? o.punishInfo.punishReason
										: "");

								method = "update";
								absStore.load({
											params : {
												punishId : grid
														.getStore()
														.getAt(rowIndex)
														.get("punishInfo.punishId")
											}
										});
							},
							failure : function(result, request) {
								Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
							}
						});
			});

	westStore.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							punishDate : meetingYear.getValue()
						});
			});

	westStore.load({
				params : {
					punishDate : myDate.getFullYear(),
					start : 0,
					limit : pageSize
				}
			});

	var awardid = new Ext.form.Hidden({
				id : 'punishInfo.punishId',
				name : 'punishInfo.punishId'
			});

	var timeSelect = new Ext.form.TextField({
				id : 'punishDate',
				fieldLabel : "处罚日期",
				name : 'punishDate',
				readOnly : true,
				anchor : '95%',
				allowBlank : true
			});

	var department = new Ext.form.ComboBox({
				id : 'punishDepName',
				name : 'punishDepName',
				fieldLabel : "部门",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				anchor : '95%'
			});

	var depCode = new Ext.form.Hidden({
				id : 'punishInfo.punishDep',
				name : 'punishInfo.punishDep'
			});

	var approvalMan = new Ext.form.ComboBox({
				id : 'approvalByName',
				name : 'approvalByName',
				fieldLabel : "批准人",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				anchor : '95%',
				onTriggerClick : function() {
					mantype = "approvalBy"
				}
			});

	var approvalBy = new Ext.form.Hidden({
				id : 'punishInfo.approvalBy',
				name : 'punishInfo.approvalBy'
			});

	var completeMan = new Ext.form.TextField({
				id : 'completeByName',
				name : 'completeByName',
				fieldLabel : "填写人",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				readOnly : true,
				anchor : '95%'
			});

	var completeBy = new Ext.form.Hidden({
				id : 'punishInfo.completeBy',
				name : 'punishInfo.completeBy'
			});

	var place = new Ext.form.TextArea({
				name : 'punishInfo.punishOpinion',
				xtype : 'TextArea',
				readOnly : true,
				height : 40,
				fieldLabel : '处罚意见',
				anchor : '97%'
			});

	var memo = new Ext.form.TextArea({
				id : 'punishInfo.punishReason',
				xtype : 'textarea',
				fieldLabel : '处罚原因',
				readOnly : true,
				height : 60,
				anchor : '97%'
			});

	var formContent = new Ext.form.FieldSet({
				border : false,
				layout : 'column',
				items : [{
							columnWidth : .5,
							layout : 'form',
							border : false,
							labelWidth : 70,
							items : [department]
						}, {
							columnWidth : .5,
							layout : 'form',
							labelWidth : 70,
							border : false,
							items : [timeSelect]
						}, {
							border : false,
							layout : 'form',
							columnWidth : 1,
							labelWidth : 70,
							items : [memo]
						}, {
							border : false,
							layout : 'form',
							columnWidth : 1,
							labelWidth : 70,
							items : [place]
						}, {
							columnWidth : .5,
							layout : 'form',
							border : false,
							labelWidth : 70,
							items : [approvalMan]
						}, {
							columnWidth : .5,
							layout : 'form',
							labelWidth : 70,
							border : false,
							items : [completeMan, depCode, awardid, approvalBy,
									completeBy]
						}]
			});

	// 右侧Form
	var eastform = new Ext.form.FormPanel({
				bodyStyle : "padding:5px 5px 0",
				labelAlign : 'right',
				labelWidth : 70,
				region : 'center',
				border : false,
				items : [formContent]
			});

	// ↑↑主窗口部件

	var fieldSet = new Ext.Panel({
				layout : 'border',
				border : false,
				items : [{
							region : 'north',
							split : true,
							collapsible : true,
							titleCollapse : true,
							margins : '1',
							height : 230,
							layoutConfig : {
								animate : true
							},
							layout : 'fit',
							items : [eastform]
						}, {
							title : '人员列表',
							region : "center",
							layout : 'fit',
							items : [absManlistGrid]
						}]
			});

	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							title : '奖励列表',
							region : 'west',
							split : true,
							collapsible : true,
							titleCollapse : true,
							margins : '1',
							width : 320,
							layoutConfig : {
								animate : true
							},
							layout : 'fit',
							items : [westgrid]
						}, {
							title : '奖励信息',
							region : "center",
							split : true,
							collapsible : false,
							titleCollapse : true,
							margins : '1',
							layoutConfig : {
								animate : true
							},
							layout : 'fit',
							items : [fieldSet]
						}]
			});

});