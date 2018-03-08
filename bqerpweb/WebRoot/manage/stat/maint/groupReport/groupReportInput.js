Ext.onReady(function() {
			var centersm = new Ext.grid.CheckboxSelectionModel({
						singleSelect : true
					});

			var storelist = new Ext.data.Record.create([centersm, {
						name : 'itemCode'
					}, {
						name : 'itemName'
					}, {
						name : 'unitName'
					}, {
						name : 'sdataValue'
					}, {
						name : 'dataValue'
					}]);

			var centerds = new Ext.data.JsonStore({
						url : 'manager/getGroupReportItem.action',
						root : 'list',
						totalProperty : 'totalCount',
						fields : storelist
					});

			centerds.on('beforeload', function() {
						Ext.apply(this.baseParams, {
									reportcode : reportName.getValue(),
									reportdate : timeSelect.getValue()
								});
						Ext.Msg.wait("正在查询数据!请等待...");
					});

			centerds.on('load', function() {
						Ext.Msg.hide();
					});

			var reportStore = Ext.data.Record.create([{
						name : 'reportName'
					}, {
						name : 'reportCode'
					}]);

			var allReportStore = new Ext.data.JsonStore({
						url : 'manager/getBpCInputReportList.action?type=2',
						root : 'list',
						fields : reportStore
					});

			allReportStore.load();

			var reportName = new Ext.form.ComboBox({
						store : allReportStore,
						id : 'reportName',
						name : 'reportName',
						valueField : "reportCode",
						displayField : "reportName",
						mode : 'local',
						typeAhead : true,
						forceSelection : true,
						hiddenName : 'statItem.reportCode',
						editable : false,
						triggerAction : 'all',
						selectOnFocus : true,
						allowBlank : true,
						emptyText : '请选择',
						anchor : '85%',
						resizable : true,
						listeners : {
							render : function() {
								this.clearInvalid();
							}
						}
					});

			var timeSelect = new Ext.form.TextField({
						id : 'timeSelect',
						fieldLabel : "日期",
						name : 'timeSelect',
						style : 'cursor:pointer',
						readOnly : true,
						anchor : '95%',
						allowBlank : true,
						listeners : {
							focus : function() {
								WdatePicker({
											startDate : '%y-%M-%d',
											dateFmt : 'yyyy-MM-dd',
											alwaysUseStartDate : true
										});
								this.blur();
							}
						}
					});

			var ctbtnQuery = new Ext.Button({
						text : '查询',
						iconCls : 'query',
						handler : function() {
							var reportCode = reportName.getValue();
							if (reportCode == null || reportCode == '') {
								Ext.Msg.alert("提示", "请选择报表!");
								return;
							}
							centerds.load();
						}
					});

			function upSave() {
				centergrid.stopEditing();
				var modifyRec = centergrid.getStore().getModifiedRecords();
				if (modifyRec.length > 0) {
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						updateData.push(modifyRec[i].data);
					}
					Ext.Ajax.request({
								url : 'manager/saveGroupReportValue.action',
								method : 'post',
								params : {
									isUpdate : Ext.util.JSON.encode(updateData),
									reportcode : reportName.getValue(),
									reportdate : timeSelect.getValue()
								},
								success : function(result, request) {
									var o = eval('(' + result.responseText
											+ ')');
									Ext.MessageBox.alert('提示信息', o.msg);
									centerds.rejectChanges();
									centerds.reload();
								},
								failure : function(result, request) {
									Ext.MessageBox.alert('提示信息', '保存失败请联系管理员！')
								}
							})
				} else {
					Ext.MessageBox.alert('提示信息', '没有做任何修改！')
				}
			}
			
			var upBtn = new Ext.Button({
						id : 'upBtn',
						text : '保存修改',
						iconCls : 'update',
						handler : function() {
							upSave();
						}
					});

			// Center中的Grid主体
			var centergrid = new Ext.grid.EditorGridPanel({
						store : centerds,
						columns : [new Ext.grid.RowNumberer({}), {
									header : "指标名称",
									width : 50,
									align : "center",
									dataIndex : 'itemName'
								}, {
									header : "计量单位",
									width : 50,
									align : "center",
									sortable : false,
									dataIndex : 'unitName'
								}, {
									header : "上期值",
									width : 30,
									align : "right",
									sortable : true,
									dataIndex : 'sdataValue'
								}, {
									header : "本期值",
									width : 30,
									align : "right",
									sortable : true,
									dataIndex : 'dataValue',
									editor : new Ext.form.NumberField({
										        decimalPrecision: 4,
									            allowDecimals : true,
												allowNegative : false
									})
								}],
						viewConfig : {
							forceFit : true
						},
						tbar : ['日期', timeSelect, {
									xtype : "tbseparator"
								}, '报表名', reportName, {
									xtype : "tbseparator"
								}, ctbtnQuery, {
									xtype : "tbseparator"
								}, upBtn],
						sm : centersm,
						frame : true,
						enableColumnHide : false,
						enableColumnMove : false,
						iconCls : 'icon-grid'
					});

			// 设定布局器及面板
			var layout = new Ext.Viewport({
						layout : "border",
						border : false,
						items : [{
									title : '',
									region : "center",
									split : true,
									collapsible : false,
									titleCollapse : true,
									margins : '0',
									layoutConfig : {
										animate : true
									},
									layout : 'fit',
									items : [centergrid]
								}]
					});

			var myDate = new Date();

			var myMonth = myDate.getMonth() + 1;

			myMonth = (myMonth < 10 ? "0" + myMonth : myMonth);

			var myDay = myDate.getDate();

			myDay = (myDay < 10 ? "0" + myDay : myDay);

			timeSelect.setValue(myDate.getFullYear() + "-" + myMonth + "-"
					+ myDay);

		})