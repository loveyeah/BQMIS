Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

			// 监督专业，定值，热控为6
			
			/** 左边的grid * */
			var con_item = Ext.data.Record.create([{
						name : 'parameterId'
					}, {
						name : 'yqyblbId'
					}, {
						name : 'parameterNames'
					}, {
						name : 'memo'
					}, {
						name : 'jdzyId'
					}]);

			var con_sm = new Ext.grid.CheckboxSelectionModel({
						singleSelect : false

					});
			var con_ds = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : 'productionrec/findJycsBySort.action'
								}),
						reader : new Ext.data.JsonReader({
									totalCount : "",
									root : ""
								}, con_item)
					});

			var con_item_cm = new Ext.grid.ColumnModel([con_sm,

			{
						header : '检验参数',
						dataIndex : 'parameterNames',
						align : 'center',
						editor : new Ext.form.TextField()

					}, {
						header : '备注',
						dataIndex : 'memo',
						align : 'center',
						editor : new Ext.form.TextArea()
					}]);
			con_item_cm.defaultSortable = true;

			var gridbbar = new Ext.PagingToolbar({
						pageSize : 18,
						store : con_ds,
						displayInfo : true,
						displayMsg : "显示第{0}条到{1}条，共{2}条",
						emptyMsg : "没有记录"
						//beforePageText : '',
						//afterPageText : ""
					});
			// 增加
			function addTopic() {

				var count = con_ds.getCount();

				var currentIndex = count;

				var o = new con_item({
							'yqyblbId' : yqyblbId.getValue(),
							'parameterNames' : '',
							'memo' : '',
							'jdzyId' : JDZY_ID

						});

				Grid.stopEditing();
				con_ds.insert(currentIndex, o);
				con_sm.selectRow(currentIndex);
				Grid.startEditing(currentIndex, 1);
				// resetLine();
			}

			// 删除记录
			var topicIds = new Array();
			function deleteTopic() {
				Grid.stopEditing();
				var sm = Grid.getSelectionModel();
				var selected = sm.getSelections();
				if (selected.length == 0) {
					Ext.Msg.alert("提示", "请选择要删除的记录！");
				} else {
					for (var i = 0; i < selected.length; i += 1) {
						var member = selected[i];
						if (member.get("parameterId") != null) {
							topicIds.push(member.get("parameterId"));
						}
						Grid.getStore().remove(member);
						Grid.getStore().getModifiedRecords().remove(member);
					}
					// resetLine();
				}
			}
			// 保存
			function saveTopic() {
				Grid.stopEditing();
				var modifyRec = Grid.getStore().getModifiedRecords();

				if (Ext.get("yqyblbId").dom.value == ""
						|| Ext.get("yqyblbId").dom.value == undefined) {
					Ext.Msg.alert("提示", "请先选择仪器仪表类别！");
					return;
				}
				if (modifyRec.length > 0 || topicIds.length > 0) {

					if (!confirm("确定要保存修改吗?"))
						return;
					// var newData = new Array();
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						if (modifyRec[i].get("topicName") == "") {
							Ext.MessageBox.alert('提示信息', '主题名称不能为空！')
							return
						}

						updateData.push(modifyRec[i].data);
						// }
					}

					Ext.Ajax.request({
								url : 'productionrec/save.action',
								method : 'post',
								params : {
									// isAdd : Ext.util.JSON.encode(newData),
									isUpdate : Ext.util.JSON.encode(updateData),
									isDelete : topicIds.join(",")
								},
								success : function(result, request) {

									con_ds.rejectChanges();
									topicIds = [];
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
			// 取消
			function cancerTopic() {
				var modifyRec = con_ds.getModifiedRecords();
				if (modifyRec.length > 0 || topicIds.length > 0) {
					if (!confirm("确定要放弃修改吗"))
						return;
					con_ds.reload();
					con_ds.rejectChanges();
					topicIds = [];
				} else {
					con_ds.reload();
					con_ds.rejectChanges();
					topicIds = [];
				}
			}

			// 类别
			var lb_item = Ext.data.Record.create([{
						name : 'yqyblbName'
					}, {
						name : 'yqyblbId'
					}]);
			var lbStore = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : 'productionrec/findLBList.action'
								}),
						reader : new Ext.data.JsonReader({
									totalCount : "totalCount",
									root : "list"
								}, lb_item)
					});
			lbStore.load({
						params : {
							jdzyId : JDZY_ID
						}
					})

			var yqyblbId = new Ext.form.ComboBox({
						width : 150,
						id : 'yqyblbId',
						hiddenName : "model.yqyblbId",
						name : "yqyblbIdName",
						fieldLabel : "类别",
						triggerAction : 'all',
						store : lbStore,
						displayField : "yqyblbName",
						valueField : "yqyblbId",
						emptyText : '类别...',
						mode : 'local',
						readOnly : true,
						allowBlank : false,
						listeners : {
							select : fuzzyQuery
						}

					});

			function fuzzyQuery() {
				Ext.getCmp("btnAdd").setDisabled(false);
				Ext.getCmp("btnDelete").setDisabled(false);
				Ext.getCmp("btnCancer").setDisabled(false);
				Ext.getCmp("btnSave").setDisabled(false);
				con_ds.baseParams = {
					yqyblbId : yqyblbId.getValue(),
					jdzyId : JDZY_ID
				}
				con_ds.load({
							params : {
								start : 0,
								limit : 18
								
							}
						});
				topicIds = [];
			};
			// 查询时Enter
			document.onkeydown = function() {
				if (event.keyCode == 13 && document.getElementById('yqyblbId')) {
					fuzzyQuery();

				}
			}

			// var querybtn = new Ext.Button({
			// iconCls : 'query',
			// text : '查询',
			// handler : function() {
			// fuzzyQuery();
			// }
			// })

			var contbar = new Ext.Toolbar({
						items : [yqyblbId, {
									id : 'btnAdd',
									iconCls : 'add',
									text : "新增",
									disabled : true,
									handler : addTopic
								}, {
									id : 'btnDelete',
									iconCls : 'delete',
									text : "删除",
									disabled : true,
									handler : deleteTopic

								}, {
									id : 'btnCancer',
									iconCls : 'cancer',
									text : "取消",
									disabled : true,
									handler : cancerTopic

								}, '-', {
									id : 'btnSave',
									iconCls : 'save',
									text : "保存修改",
									disabled : true,
									handler : saveTopic
								}]

					});
			var Grid = new Ext.grid.EditorGridPanel({
						sm : con_sm,
						ds : con_ds,
						cm : con_item_cm,
						height : 425,
						// // title : '合同列表',
						// width : Ext.get('div_lay').getWidth(),
						// split : true,
						autoScroll : true,
						bbar : gridbbar,
						tbar : contbar,
						border : true,
						clicksToEdit : 2,
						viewConfig : {
							forceFit : true
						}
					});
			/** 左边的grid * */

			new Ext.Viewport({
						enableTabScroll : true,
						layout : "border",
						border : false,
						frame : false,
						items : [{
									// bodyStyle : "padding: 20,10,20,20",
									layout : 'fit',
									border : false,
									frame : false,
									region : "center",

									items : [Grid]
								}]
					});

		})