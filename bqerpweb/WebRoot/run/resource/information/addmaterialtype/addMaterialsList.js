Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
var storeData = null;
Ext.onReady(function() {

	// 定义状态
	var stateData = new Ext.data.SimpleStore({
				data : [['', '所有状态'], [1, '未发送'], [2, '已发送'], [3, '已审核'],
						[4, '已退回']],
				fields : ['value', 'name']
			});
	// 定义状态
	var stateComboBox = new Ext.form.ComboBox({
				id : "stateCob",
				store : stateData,
				displayField : "name",
				valueField : "value",
				mode : 'local',
				triggerAction : 'all',
				hiddenName : 'stateComboBox',
				readOnly : true,
				value : '',
				width : 100
			});
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
				// 流水号
				name : 'temp.tempId'
			}, {
				// 临时物资名称
				name : 'temp.materialName'
			}, {
				// 规格型号
				name : 'temp.specNo'
			}, {
				// 材质/参数
				name : 'temp.parameter'
			}, {
				// 计量单位
				name : 'temp.stockUmId'
			}, {
				// 缺省仓库编码
				name : 'temp.defaultWhsNo'
			}, {
				// 缺省库位编码DEFAULT_LOCATION_NO
				name : 'temp.defaultLocationNo'
			}, {
				// 物料分类ID MAERTIAL_CLASS_ID
				name : 'temp.maertialClassId'
			}, {
				// 物料分类名称
				name : 'maertialClassName'
			}, {
				// 生产厂家
				name : 'temp.factory'
			}, {
				// 单价
				name : 'temp.actPrice'
			}, {
				// 上报日期
				name : 'checkDate'
			}, {
				// 上报人
				name : 'checkName'
			}, {
				// 联系电话
				name : 'temp.telNo'
			}, {
				// 备注
				name : 'temp.memo'
			}, {
				// 审核状态STATUS_ID
				name : 'temp.statusId'
			}, {
				// 正式物资编码
				name : 'temp.materialNo'

			}, {
				// 审核人编码APPROVE_BY
				name : 'temp.approveBy'
			}, {
				// 审核人
				name : 'approveName'

			}, {
				// 审核时间APPROVE_DATE
				name : 'approveDate'
			}, {
				// 上次修改人LAST_MODIFIED_BY
				name : 'modifyName'
			}, {
				// 上次修改时间
				name : 'modifyDate'
			}, {
				name : 'temp.backReason'
			}, {

				name : 'materialId'
			}, {

				name : 'unitName'
			}]);
	var dataProxy = new Ext.data.HttpProxy(

	{
				url : 'resource/findTempMaterialList.action'
			});
	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, MyRecord);
	var store = new Ext.data.Store({
				proxy : dataProxy,
				reader : theReader
			});
store.load()
	// 分页
	var fuzzy = new Ext.form.TextField({
				id : "fuzzy",
				name : "fuzzy"
			});

	var sm = new Ext.grid.CheckboxSelectionModel({
			// singleSelect : true
			});

	var grid = new Ext.grid.GridPanel({
		region : "center",
		store : store,
		viewConfig : {
			forceFit : false
		},
		loadMask : {
			msg : '<img src="comm/images/extanim32.gif" width="32" height="32" style="margin-right: 8px;" align="absmiddle" />读取数据中 ...'
		},
		columns : [sm, new Ext.grid.RowNumberer({
							header : '行号',
							width : 35,
							align : 'left'
						}), {
					header : "ID",
					width : 100,
					sortable : true,
					dataIndex : 'temp.tempId',
					hidden : true
				}, {
					header : "物资名称",
					width : 80,
					sortable : true,
					dataIndex : 'temp.materialName'
				}, {
					header : "正式物资编码",
					width : 80,
					sortable : true,
					dataIndex : 'temp.materialNo'
				}, {
					header : "物料分类名称",
					width : 80,
					sortable : true,
					dataIndex : 'maertialClassName'
				}, {
					header : "规格型号",
					width : 80,
					sortable : true,
					dataIndex : 'temp.specNo'
				}, {
					header : "材质/参数",
					width : 80,
					sortable : true,
					dataIndex : 'temp.parameter'
				}, {
					header : "计量单位",
					width : 70,
					sortable : true,
					dataIndex : 'unitName'
					// dataIndex : 'temp.stockUmId',
				// renderer : function unitName(value) {
				// if (value !== null && value !== "") {
				// var url = "resource/getRS001UnitName.action?unitCode="
				// + value;
				// var conn = Ext.lib.Ajax.getConnectionObject().conn;
				// conn.open("POST", url, false);
				// conn.send(null);
				// return conn.responseText;
				// } else {
				// return "";
				// }
				// }
			}	, {
					header : "状态",
					width : 70,
					sortable : true,
					align : 'center',
					renderer : function(value) {
						if (value == 1) {
							return "未发送";
						} else if (value == 2) {
							return "已发送";
						} else if (value == 3) {
							return "已审核";
						} else if (value == 4) {
							return "已退回";
						} else
							;
					},
					dataIndex : 'temp.statusId'
				}, {
					header : "生产厂家",
					width : 100,
					sortable : true,
					align : 'left',
					dataIndex : 'temp.factory'
				}, {
					header : "仓库",
					width : 100,
					sortable : true,
					align : 'center',
					dataIndex : 'temp.defaultWhsNo',
					renderer : getWhs
					// function comboBoxWarehouseRenderer(value, cellmeta,
					// record) {
				// var whsNo = record.data["temp.defaultWhsNo"];
				// if(!whsNo){
				// return;
				// }
				// var url = "resource/getWarehouseName.action?whsNo=" + whsNo;
				// var conn = Ext.lib.Ajax.getConnectionObject().conn;
				// conn.open("POST", url, false);
				// conn.send(null);
				// return conn.responseText;

				// }
			}	, {
					header : "单价",
					width : 70,
					sortable : true,
					align : 'center',
					dataIndex : 'temp.actPrice'
				}, {
					header : "发送日期",
					width : 80,
					sortable : true,
					align : 'center',
					dataIndex : 'checkDate'
				}, {
					header : "审核人",
					width : 60,
					sortable : true,
					align : 'center',
					dataIndex : 'approveName'
				}, {
					header : "审核时间",
					width : 80,
					sortable : true,
					align : 'center',
					dataIndex : 'approveDate'
				}, {
					header : "最后修改日期",
					width : 80,
					sortable : true,
					align : 'center',
					// sortable : true,
					dataIndex : 'modifyDate'
				}, {
					header : "退回原因",
					width : 80,
					sortable : true,
					align : 'center',
					dataIndex : 'temp.backReason'
				}],
		sm : sm,
		tbar : ['状态', stateComboBox, '-', '物料名称', {
					id : 'materialConditon',
					name : 'materialConditon',
					xtype : 'textfield'
				}, {
					text : "查询",
					iconCls : 'query',
					handler : queryRecord
				}, '-', {
					text : "增加",
					iconCls : 'add',
					handler : function() {
						parent.currentRecord = null;
						parent.document.all.iframe1.src = "run/resource/information/addmaterialtype/addMaterials.jsp";
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
				}, '-', {
					text : "发送",
					iconCls : 'upcommit',
					handler : reportBtn
				}, '-', {
					text : "填写需求计划",
					iconCls : 'add',
					handler : addPlanRecord
				}
		// , '-', {
		// text : "票面预览",
		// iconCls : 'pdfview',
		// handler : viewRecord
		// }
		],
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
				Ext.Msg.alert("提示", "请选择其中一项进行编辑！");
			} else {
				var member = records[0];
				parent.currentRecord = member;
				if (member.get("temp.statusId") != 1 && member.get("temp.statusId") != 4) {
					Ext.Msg.alert("提示", "只有未发送或已退回的物料可以修改！");
				} else {
					var ffid = member.get("temp.tempId");
					var url = "run/resource/information/addmaterialtype/addMaterials.jsp";
					parent.document.all.iframe1.src = url;
					parent.Ext.getCmp("maintab").setActiveTab(0);
				}

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
				if (member.get("temp.tempId")) {
					if (member.get("temp.statusId") != 1 && member.get("temp.statusId") != 4) {
						Ext.Msg.alert("提示", "只有未发送或已退回的物料可以删除！");
						return;
					} else {
						ids.push(member.get("temp.tempId"));
						nos.push(member.get("temp.materialNo"));
					}
				} else {

					store.remove(store.getAt(i));
				}
			}

			if (nos.length > 0) {
				Ext.Msg.confirm("提示", "是否确定删除所选物资？",
						function(buttonobj) {
							if (buttonobj == "yes") {
								// 待做
								Ext.lib.Ajax.request('POST',
										'resource/deleteTempMaterial.action', {
											success : function(action) {
												Ext.Msg.alert("提示", "删除成功！");
												queryRecord();
											},
											failure : function() {
												Ext.Msg.alert('错误',
														'删除时出现未知错误.');
											}
										}, 'ids=' + ids);
							}
						});
			}

		}

	}

	// 上报处理
	function reportBtn() {
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var materialNo;
		var isReport;
		var ids = [];
		var nos = [];
		if (selected.length == 0) {
			Ext.Msg.alert("系统提示信息", "请选择其中一项进行处理！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				isReport = member.get('temp.statusId');
				if (isReport != "1" && isReport != "4") {
					Ext.Msg.alert("提示", "只有未发送及已退回的物料允许发送!");
					return false;
				} else {
					ids.push(member.get('temp.tempId'));
					nos.push(member.get("temp.materialNo"));
				}
			}
			if (nos.length > 0) {
				Ext.Msg.confirm("发送", "是否确定发送选中的物料？", function(buttonobj) {
							if (buttonobj == "yes") {
								Ext.lib.Ajax.request('POST',
										'resource/reportTempMaterial.action', {
											success : function(action) {
												Ext.Msg.alert("提示", "发送成功！");
												queryRecord();
											},
											failure : function() {
												Ext.Msg.alert('错误',
														'发送时出现未知错误.');
											}
										}, 'tempId=' + ids);

							}
						})
			}
		}
	}

	// 填写需求计划 add by drdu 091023
	function addPlanRecord() {
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();

		var mIds = [];
		var nos = [];
		var storeRec = [];
		var storeData = null;

		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("temp.statusId") != 3) {
					Ext.Msg.alert("提示", "只有已审核的物料允许填写需求计划!");
					return false;
				} else {
					nos.push(member.get("temp.materialNo"));
					mIds.push(member.get("materialId"));
					storeRec.push(member);
				}
			}
			var url = "planRegister.jsp";
			var args = new Object();
			args.storeData = storeRec;
			var location = window
					.showModalDialog(
							url,
							args,
							'dialogWidth=800px;dialogHeight=600px;center=yes;help=no;resizable=no;status=no;');

		}
	}

	function queryRecord() {
		store.baseParams = {
							status : stateComboBox.getValue(),
							materialName : Ext.get("materialConditon").dom.value
	}
		store.load({
					params : {
						start : 0,
						limit : 18,
						status : stateComboBox.getValue(),
						materialName : Ext.get("materialConditon").dom.value
					}
				});
	}

	// 预览票面
	// function viewRecord() {
	// var sm = grid.getSelectionModel();
	// var selected = sm.getSelections();
	// var applyNo = "";
	// if (selected.length < 1) {
	// // 没有选择数据时
	// Ext.Msg.alert(Constants.SYS_REMIND_MSG,
	// Constants.SELECT_NULL_VIEW_MSG);
	// } else if (selected.length > 1) {
	// // 选择多行数据时
	// Ext.Msg.alert(Constants.SYS_REMIND_MSG,
	// Constants.SELECT_COMPLEX_VIEW_MSG);
	// } else {
	// // 选择一行时
	// var member = selected[0].data;
	// if (member['work.applyNo']) {
	// applyNo = member['work.applyNo'];
	// window.open("/powerrpt/report/webfile/workApplication.jsp?no="
	// + applyNo);
	// }
	// }
	//
	// };
	// ---------------------------------------

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				items : [grid]
			});

	queryRecord();
	
	function getWhs(value, cellmeta, record, rowIndex, columnIndex, store) {
		if (record.data["temp.maertialClassId"] != null
				&& record.data["temp.maertialClassId"] != "") {

			var url = "resource/getParentCode.action?classId="
					+ record.data["temp.maertialClassId"];
			var conn = Ext.lib.Ajax.getConnectionObject().conn;
			conn.open("POST", url, false);
			conn.send(null);
			var obj = conn.responseText;
			var mytext = obj.substring(1, obj.length - 1);

			var arr = mytext.split(',');
			return arr[1];
		} else {
			return value;
		}

	}

});