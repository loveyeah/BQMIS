Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
var JDZY_ID=parent.JDZY_ID;
			/** 左边的grid * */
			var con_item = Ext.data.Record.create([{
						name : 'yqybjdId'
					}, {
						name : 'yqybjdName'
					}, {
						name : 'jdzyId'
					}]);

			var con_sm = new Ext.grid.CheckboxSelectionModel({
						singleSelect : false

					});
			var con_ds = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : 'productionrec/findJDList.action'
								}),
						reader : new Ext.data.JsonReader({
									totalProperty : "totalCount",
									root : "list"
								}, con_item)
					});
			con_ds.load({
						params : {
							jdzyId : JDZY_ID
						}
					})

			var con_item_cm = new Ext.grid.ColumnModel([con_sm,
					new Ext.grid.RowNumberer(), {
						header : '精度',
						dataIndex : 'yqybjdName',
						align : 'center',
						
						editor : new Ext.form.TextField()
					}]);
			con_item_cm.defaultSortable = true;

			// 增加
			function addTopic() {

				var count = con_ds.getCount();

				var currentIndex = count;

				var o = new con_item({
							
							'yqybjdName' : '',
							'jdzyId' : JDZY_ID

						});

				Grid.stopEditing();
				con_ds.insert(currentIndex, o);
				con_sm.selectRow(currentIndex);
				Grid.startEditing(currentIndex, 2);
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
						if (member.get("yqybjdId") != null) {
							topicIds.push(member.get("yqybjdId"));
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

				if (modifyRec.length > 0 || topicIds.length > 0) {

					if (!confirm("确定要保存修改吗?"))
						return;
					// var newData = new Array();
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						// if (modifyRec[i].get("topicName") == "") {
						// Ext.MessageBox.alert('提示信息', '主题名称不能为空！')
						// return
						// }

						updateData.push(modifyRec[i].data);
						// }
					}

					Ext.Ajax.request({
								url : 'productionrec/saveJD.action',
								method : 'post',
								params : {
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
			var contbar = new Ext.Toolbar({
						items : [{
									id : 'btnAdd',
									iconCls : 'add',
									text : "新增",
									handler : addTopic

								}, {
									id : 'btnDelete',
									iconCls : 'delete',
									text : "删除",
									handler : deleteTopic

								}, {
									id : 'btnCancer',
									iconCls : 'cancer',
									text : "取消",
									handler : cancerTopic

								}, '-', {
									id : 'btnSave',
									iconCls : 'save',
									text : "保存修改",
									handler : saveTopic
								}]

					});
			var Grid = new Ext.grid.EditorGridPanel({
						sm : con_sm,
						ds : con_ds,
						cm : con_item_cm,
						autoScroll : true,
						tbar : contbar,
						border : true,
						autoScroll : true,
						clicksToEdit : 2,
						viewConfig:{
							forceFit:true
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
