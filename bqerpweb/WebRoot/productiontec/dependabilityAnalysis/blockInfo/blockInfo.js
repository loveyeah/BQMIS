Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	// 所属机组或系统
	var storeChargeBySystem = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'workticket/getDetailEquList.action'
						}),
				reader : new Ext.data.JsonReader({
							root : 'list'
						}, [{
									name : 'blockCode',
									mapping : 'blockCode'
								}, {
									name : 'blockName',
									mapping : 'blockName'
								}])
			});
	storeChargeBySystem.load();

	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		t = d.getHours();
		s += (t > 9 ? "" : "0") + t + ":";
		t = d.getMinutes();
		s += (t > 9 ? "" : "0") + t + ":";
		t = d.getSeconds();
		s += (t > 9 ? "" : "0") + t;
		return s;
	}

	function checkTime1() {
		var date = this.value;
		Grid.getSelectionModel().getSelected().set("businessServiceDate", date);
	}
	function checkTime2() {
		var date = this.value;
		Grid.getSelectionModel().getSelected().set("statBeginDate", date);
	}
	var con_item = Ext.data.Record.create([{
				name : 'blockInfoId'
			}, {
				name : 'blockCode'
			}, {
				name : 'mgCode'
			}, {
				name : 'capacity'
			}, {
				name : 'fuelName'
			}, {
				name : 'businessServiceDate'
			}, {
				name : 'statBeginDate'
			}, {
				name : 'boilerName'
			}, {
				name : 'steamerMachine'
			}, {
				name : 'generationName'
			}, {
				name : 'primaryTransformer'
			}, {
				name : 'mgType'
			}]);

	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false

			});
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'productionrec/getBlockInfoList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalCount : "totalCount",
							root : "list"
						}, con_item)
			});
	con_ds.load({
				params : {
					start : 0,
					limit : 18
				}
			})

	// 事件状态
	var con_item_cm = new Ext.grid.ColumnModel([con_sm, {
				header : '机组',
				dataIndex : 'blockCode',
				align : 'center',
				width : 70,
				editor : new Ext.form.ComboBox({
							store : storeChargeBySystem,
							displayField : "blockName",
							valueField : "blockCode",
							hiddenName : 'bolckCode',
							mode : 'local',
							triggerAction : 'all',
							readOnly : true
						}),
				renderer : function(v) {
					var recIndex = storeChargeBySystem.find("blockCode", v);
					return storeChargeBySystem.getAt(recIndex).get("blockName");
				}
			}, {
				header : '局、厂机组编码',
				dataIndex : 'mgCode',
				align : 'center',
				width : 110,
				editor : new Ext.form.TextField()

			}, {
				header : '铭牌容量(MW)',
				dataIndex : 'capacity',
				align : 'center',
				width : 100,
				editor : new Ext.form.NumberField()
			}, {
				header : '燃料名称',
				dataIndex : 'fuelName',
				align : 'center',
				width : 70,
				editor : new Ext.form.TextField()
			}, {
				header : '商业运行时间',
				dataIndex : 'businessServiceDate',
				width : 110,
				align : 'center',
				editor : new Ext.form.TextField({
							style : 'cursor:pointer',
							readOnly : true,
							listeners : {
								focus : function() {
									WdatePicker({
												// 时间格式
												startDate : '%y-%M-%d 00:00:00',
												dateFmt : 'yyyy-MM-dd HH:mm:ss',
												alwaysUseStartDate : false,
												onpicked : checkTime1
											});

								}
							}
						}),
			renderer : function(v){
					return v.substring(0,10)+" "+v.substring(11,19) 
			}
			}, {
				header : '开始统计时间',
				dataIndex : 'statBeginDate',
				align : 'center',
				width : 110,
				editor : new Ext.form.TextField({
							style : 'cursor:pointer',
							readOnly : true,
							listeners : {
								focus : function() {
									WdatePicker({
												// 时间格式
												startDate : '%y-%M-%d 00:00:00',
												dateFmt : 'yyyy-MM-dd HH:mm:ss',
												alwaysUseStartDate : false,
												onpicked : checkTime2
											});

								}
							}
						}),
			renderer : function(v){
					return v.substring(0,10)+" "+v.substring(11,19) 
			}
			}, {
				header : '锅炉设备',
				dataIndex : 'boilerName',
				align : 'center',
				width : 150,
				editor : new Ext.form.TextField()
			}, {
				header : '汽轮机',
				dataIndex : 'steamerMachine',
				align : 'center',
				width : 150,
				editor : new Ext.form.TextField()
			}, {
				header : '发电机/电动机',
				dataIndex : 'generationName',
				align : 'center',
				editor : new Ext.form.TextField()
			}, {
				header : '主变压器',
				dataIndex : 'primaryTransformer',
				align : 'center',
				editor : new Ext.form.TextField()
			}, {
				header : '机组类型',
				dataIndex : 'mgType',
				align : 'center',
				editor : new Ext.form.TextField()
			}]);

	con_item_cm.defaultSortable = true;
	var gridbbar = new Ext.PagingToolbar({
				pageSize : 18,
				store : con_ds,
				displayInfo : true,
				displayMsg : "显示第{0}条到{1}条，共{2}条",
				emptyMsg : "没有记录",
				beforePageText : '',
				afterPageText : ""
			});
	// 增加
	function addTopic() {
		var count = con_ds.getCount();
		var currentIndex = count;
		var o = new con_item({
					'blockCode' : 1,
					'mgCode' : '',
					'capacity' : '',
					'fuelName' : '',
					'businessServiceDate' : getDate(),
					'statBeginDate' : getDate(),
					'boilerName' : '',
					'steamerMachine' : '',
					'generationName' : '',
					'primaryTransformer' : '',
					'mgType' : ''
				});
		Grid.stopEditing();
		con_ds.insert(currentIndex, o);
		con_sm.selectRow(currentIndex);
		Grid.startEditing(currentIndex, 1);
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
				if (member.get("blockInfoId") != null) {
					topicIds.push(member.get("blockInfoId"));
				}
				Grid.getStore().remove(member);
				Grid.getStore().getModifiedRecords().remove(member);
			}
		}
	}
	// 保存
	function saveTopic() {
		Grid.stopEditing();
		var modifyRec = Grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || topicIds.length > 0) {
			if (!confirm("确定要保存修改吗?"))
				return;
			var updateData = new Array();
			for (var i = 0; i < modifyRec.length; i++) {
				updateData.push(modifyRec[i].data);
			}
			Ext.Ajax.request({
						url : 'productionrec/saveBlockInfo.action',
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
							Ext.MessageBox.alert('提示信息', '操作失败！')
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
	function fuzzyQuery() {
		con_ds.load({
					params : {
						start : 0,
						limit : 18
					}
				});
		topicIds = [];
	};
	// tbar
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
				title : '机组信息',
				autoScroll : true,
				bbar : gridbbar,
				tbar : contbar,
				border : true,
				clicksToEdit : 1,
				viewConfig : {
	// forceFit : true
				}
			});

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [{
							layout : 'fit',
							border : false,
							frame : false,
							region : "center",
							items : [Grid]
						}]
			});

})