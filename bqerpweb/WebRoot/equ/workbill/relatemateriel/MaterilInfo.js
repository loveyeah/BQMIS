Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	// var fuzzy;
	function getParameter(psName) {
		var url = self.location;
		var result = "";
		var str = self.location.search.substring(0);
		if (str.indexOf(psName) != -1
				&& (str.substr(str.indexOf(psName) - 1, 1) == "?" || str
						.substr(str.indexOf(psName) - 1, 1) == "&")) {
			if (str.substring(str.indexOf(psName), str.length).indexOf("&") != -1) {
				var Test = str.substring(str.indexOf(psName), str.length);
				result = Test.substr(Test.indexOf(psName), Test.indexOf("&")
								- Test.indexOf(psName));
				result = result.substring(result.indexOf("=") + 1,
						result.length);
			} else {
				result = str.substring(str.indexOf(psName), str.length);
				result = result.substring(result.indexOf("=") + 1,
						result.length);
			}
		}
		return result;
	}

	var issueHeadId = getParameter("issueHeadId");
//	var woCode = getParameter("woCode");

	var wd = 120;
	// 是否紧急领用
	var isEmergency = new Ext.form.Checkbox({
				id : "isEmergency",
				hideLabel : true,
				width : wd,
				boxLabel : "是否紧急领用",
				name : "isEmergency"
			});
	// 领料单编号
	var issueNo = new Ext.form.TextField({
				id : "issueNo",
				fieldLabel : '领料单编号',
				readOnly : true,
				width : wd,
				isFormField : true,
				name : 'issueNo'

			});
	// 工单编号
	var woNo = new Ext.form.TextField({
				id : "woNo",
				fieldLabel : '工单编号',
				readOnly : true,
				width : wd,
				isFormField : true,
				name : 'woNo'

			});
//	woNo.setValue(woCode);
	// 需求计划单编号
	var mrNo = new Ext.form.TextField({
				id : "mrNo",
				fieldLabel : '需求计划单编号',
				readOnly : true,
				isFormField : true,
				width : wd,
				name : 'mrNo'
			});
	// 项目编号
	var projectCode = new Ext.form.TextField({
				id : "projectCode",
				fieldLabel : '项目编号',
				readOnly : true,
				isFormField : true,
				width : wd,
				name : 'projectCode'
			});
	// TODO 成本分摊项目
	var txtCostItemStore = new Ext.data.SimpleStore({
				fields : ['id', 'name'],
				data : [['1', '成本分摊项目1'], ['2', '成本分摊项目2'], ['3', '成本分摊项目3'],
						['4', '成本分摊项目4']]
			});
	// TODO 成本分摊项目
	var costItemId = new Ext.form.ComboBox({
		store : txtCostItemStore,
		id : "costItemId",
		displayField : "name",
		valueField : "id",
		mode : 'local',
		width : wd,
		disabled : true,
		fieldLabel : "成本分摊项目",
		// triggerAction : 'all',
		readOnly : true
			// listeners : {
			// 'select' : function(combo, record, index) {
			// hdnCostItemId.setValue(record.get('id'));
			// }
			// }
		})
	// TODO 费用来源store
	var txtMrItemStore = new Ext.data.SimpleStore({
				fields : ['id', 'name'],
				data : [['1', '费用来源1'], ['2', '费用来源2'], ['3', '费用来源3'],
						['4', '费用来源4']]
			});
	// TODO 费用来源
	var itemId = new Ext.form.ComboBox({
		store : txtMrItemStore,
		id : "itemId",
		displayField : "name",
		valueField : "id",
		mode : 'local',
		width : wd,
		disabled : true,
		fieldLabel : "费用来源",
		// triggerAction : 'all',
		readOnly : true
			// listeners : {
			// 'select' : function(combo, record, index) {
			// hdnItemId.setValue(record.get('id'));
			// }
			// }
		})
	// 申请领用人
	var receiptBy = new Ext.form.ComboBox({
		id : 'receiptBy',
		isFormField : true,
		width : wd,
		fieldLabel : "申请领用人",
		readOnly : true
		});
	
	// 领用部门
	var receiptDep = new Ext.form.TextField({
				id : 'receiptDep',
				width : wd,
				fieldLabel : "领用部门",
				readOnly : true
			});

	// 申请领用日期
	var dueDate = new Ext.form.TextField({
				style : 'cursor:pointer',
				id : "dueDate",
				name : 'dueDate',
				readOnly : true,
				width : wd,
				fieldLabel : '申请领用日期'
			});

	// 费用归口部门
	var feeByDep = new Ext.form.TextField({
				id : 'feeByDep',
				width : wd,
				readOnly : true,
				fieldLabel : "费用归口部门"
			});

	// 费用归口专业
	var feeBySpecialStore = new Ext.data.JsonStore({
				url : 'resource/getAllFeeBySpecial.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : [
						// 专业编码
						{
					name : 'specialityCode'
				},
						// 专业名称，
						{
							name : 'specialityName'
						}]
			});
	feeBySpecialStore.load();
	// 隐藏域费用归口专业
	var hdnFeeBySpecial = new Ext.form.Hidden({
				id : "hdnFeeBySpecial",
				name : "feeBySpecial"
			});
	// 费用归口专业
	var feeBySpecial = new Ext.form.ComboBox({
				store : feeBySpecialStore,
				id : "feeBySpecial",
				displayField : "specialityName",
				valueField : "specialityCode",
				mode : 'local',
				width : wd,
				disabled : true,
				fieldLabel : "费用归口专业",
//				triggerAction : 'all',
				readOnly : true,
				listeners : {
					'select' : function(combo, record, index) {
						hdnFeeBySpecial.setValue(record.get('specialityCode'));
					}
				}
			});

	// 计划来源
	var txtMrOriginal = new Ext.ux.ComboBoxTree({
				fieldLabel : '计划来源',
				// anchor : '100%',
				displayField : 'text',
				valueField : 'id',
				width : wd,
				disabled : true,
				id : 'planOriginalId',
				// hiddenName : 'head.planOriginalId',
				// hiddenName : 'head.txtMrOriginal',
				triggerAction : 'one',
				blankText : '请选择',
				emptyText : '请选择',
				readOnly : true,
				tree : {
					xtype : 'treepanel',
					// 虚拟节点,不能显示
					rootVisible : false,
					loader : new Ext.tree.TreeLoader({
								dataUrl : 'resource/getPlanOriginalNode.action'
							}),
					root : new Ext.tree.AsyncTreeNode({
								id : '0',
								name : '合肥电厂',
								text : '合肥电厂'
							})
					// listeners : {
					// click : setTxtMrCostSpecial
					// }
				},
				selectNodeModel : 'leaf',
				listeners : {
					'select' : function(combo, record, index) {
						// Ext.get("txtMrOriginalH").dom.value = record
						// .get('planOriginalId');
						// Ext.get("txtMrOriginalH").dom.value =
						// record.get('planOriginalId');
						txtMrOriginalH.setValue(record.get('planOriginalId'));

					}
				}
			});

	// 备注
	var memo = new Ext.form.TextArea({
				id : "memo",
				name : "memo",
				// height : Constants.TEXTAREA_HEIGHT,
				maxLength : 127,
				anchor : "98.45%",
				fieldLabel : "备注"
			});
	// form
	var headForm = new Ext.form.FieldSet({
				border : false,
				labelAlign : 'right',
				style : {
					'border' : 0
				},
				layout : 'form',
				width : 800,
				autoHeight : true,
				items : [{
							layout : "column",
							border : false,
							items : [{
										width : 30,
										border : false
									}, {
										columnWidth : 0.3,
										layout : "form",
										border : false,
										style : "padding-top:3px;",
										height : 22,
										items : [isEmergency]
									}]

						}, {
							layout : "column",
							border : false,
							items : [{
										columnWidth : 0.3,
										layout : "form",
										border : false,
										height : 26,
										items : [issueNo]
									}, {
										columnWidth : 0.3,
										layout : "form",
										border : false,
										height : 26,
										items : [mrNo]
									}, {
										columnWidth : 0.3,
										layout : "form",
										border : false,
										height : 26,
										items : [woNo]
									}]
						}, {
							layout : "column",
							border : false,
							items : [{
										columnWidth : 0.3,
										layout : "form",
										border : false,
										height : 26,
										items : [projectCode]
									}, {
										columnWidth : 0.3,
										layout : "form",
										border : false,
										height : 26,
										items : [costItemId]
									}, {
										columnWidth : 0.3,
										layout : "form",
										border : false,
										height : 26,
										items : [itemId]
									}]
						}, {
							layout : "column",
							border : false,
							items : [{
										columnWidth : 0.3,
										layout : "form",
										border : false,
										height : 26,
										items : [receiptBy]
									}, {
										columnWidth : 0.3,
										layout : "form",
										border : false,
										height : 26,
										items : [receiptDep]
									}, {
										columnWidth : 0.3,
										layout : "form",
										border : false,
										height : 26,
										items : [dueDate]
									}]
						}, {
							layout : "column",
							border : false,
							items : [{
										columnWidth : 0.3,
										layout : "form",
										border : false,
										height : 26,
										items : [feeByDep]
									}, {
										columnWidth : 0.3,
										layout : "form",
										border : false,
										height : 26,
										items : [feeBySpecial]
									}, {
										columnWidth : 0.29,
										layout : "form",
										border : false,
										height : 26,
										items : [txtMrOriginal]
									}]
						}, {
							layout : "column",
							border : false,
							items : [{
										columnWidth : 0.9,
										layout : "form",
										// height : 39,
										border : false,
										items : [memo]
									}]
						}]
			});

	// var fuzzy = "1"
	Ext.lib.Ajax.request('POST',
			'resource/getIssueHeadById.action?issueHeadId=' + issueHeadId, {
				success : function(action) {
					var result = eval("(" + action.responseText + ")");
					// alert(action.responseText)
					// headForm.getForm().loadRecord(result);
					Ext.get("issueNo").dom.value = (result.issueNo == null)
							? ""
							: result.issueNo;
					Ext.get("woNo").dom.value = (result.woNo == null)
							? ""
							: result.woNo;
					Ext.get("projectCode").dom.value = (result.projectCode == null)
							? ""
							: result.projectCode;
					costItemId.setValue((result.costItemId == null)
							? ""
							: result.costItemId);
					itemId.setValue((result.itemId == null)
							? ""
							: result.itemId);
					// Ext.get("receiptBy").dom.value =
					// (result.receiptBy==null)?"":result.receiptBy;
					// receiptBy.setValue((result.receiptBy==null)?"":result.receiptBy)
					// 申请人
					Ext.lib.Ajax.request('POST',
							'resource/getReceiptByName.action', {
								success : function(action) {
									receiptBy.setValue(action.responseText);
								},
								faliure : function(action) {
								}
							}, "receiptBy=" + result.receiptBy);

					// Ext.get("receiptDep").dom.value =
					// (result.receiptDep==null)?"":result.receiptDep;
					// 查找部门信息
					Ext.lib.Ajax.request('POST',
							'resource/getDeptInfoByEmpCode.action', {
								success : function(action) {
									// 部门信息
									var dept = eval("(" + action.responseText
											+ ")");
									if (dept == null) {
										// 领用部门
										receiptDep.setValue("");
										// 费用归口部门
										feeByDep.setValue("");
									} else {
										// 领用部门
										receiptDep.setValue(dept.deptName);
										// 费用归口部门
										feeByDep.setValue(dept.deptName);
									}
								},
								failure : function() {
								}
							}, 'empCode=' + result.receiptDep);

					Ext.get("dueDate").dom.value = (result.dueDate.substring(0,
							10) == null) ? "" : result.dueDate.substring(0, 10);
					// Ext.get("feeByDep").dom.value =
					// (result.feeByDep==null)?"":result.feeByDep;
					feeBySpecial.setValue((result.feeBySpecial == null)
							? ""
							: result.feeBySpecial);
					// Ext.get("planOriginalId").dom.value =
					// (result.planOriginalId==null)?"":result.planOriginalId;
					// 计划来源
//					Ext.get("planOriginalId").dom.disabled = true;
					if (result.planOriginalId != null) {
						Ext.Ajax.request({
							params : {
								planOrigianlId : result.planOriginalId
							},
							url : 'resource/getOrigianlName.action',
							method : 'post',
							success : function(result) {
								var recordSub = eval('(' + result.responseText
										+ ')');
								// alert(result.responseText)
								var obj = new Object();
								if (recordSub.data != null
										&& recordSub.data != "") {
									obj.text = recordSub.data['planOriginalDesc'];
									obj.id = parseInt(result.planOriginalId);
									txtMrOriginal.setValue(obj);
								};

							},
							failure : function(action) {
							}

						});
					} else {
						txtMrOriginal.setValue("")
					}
					// txtMrOriginal.setValue((result.planOriginalId==null)?"":result.planOriginalId);
					Ext.get("memo").dom.value = (result.memo == null)
							? ""
							: result.memo;
				}
			});
	var mypanl = new Ext.Panel({
		border : false,
		region : "north",
		// width : 650,
		// height : 180,
		style : "padding-top:10px;padding-right:00px;padding-bottom:0px;margin-bottom:0px",
		labelAlign : 'right',
		items : [headForm]
	});
	// 明细记录
	var detailRecord = Ext.data.Record.create([
			// 领料单明细id
			{
		name : 'issueDetailsId'
	},
			// 上次修改日时
			{
				name : "lastModifiedDate"
			},
			// 物料id
			{
				name : "materialId"
			},
			// 物料编码，
			{
				name : 'materialNo'
			},
			// 物料名称
			{
				name : 'materialName'
			},
			// 规格型号
			{
				name : 'specNo'
			},
			// 材质/参数
			{
				name : 'parameter'
			},
			// 存货计量单位id
			{
				name : 'stockUmId'
			},
			// 存货计量单位名称
			{
				name : "unitName"
			},
			// 申请数量
			{
				name : 'appliedCount'
			},
			// 核准数量
			{
				name : 'approvedCount'
			},
			// 物资需求明细id
			{
				name : "requirementDetailId"
			},
			// 实际数量
			{
				name : 'actIssuedCount'
			},
			// 待发货数量
			{
				name : 'waitCount'
			},
			// 成本分摊项目编码
			{
				name : 'costItemId'
			},
			// 费用来源id
			{
				name : "itemId"
			},
			// 费用来源名称
			{
				name : 'itemName'
			},
			// 是否是新规的记录
			{
				name : "isNew"
			},
			// 是否是删除的记录
			{
				name : "isDeleted"
			}]);

	// var issueHeadId = '1';
	var flag = "1";
	// 明细grid的store
	var detailStore = new Ext.data.JsonStore({
				url : 'resource/getIssueHeadRegisterDetailList.action?issueHeadId='
						+ issueHeadId + "&flag=" + flag,
				root : 'list',
				totalProperty : 'totalCount',
				fields : detailRecord
			});
	detailStore.load({
				params : {
					issueHeadId : "1",
					flag : "1"
				}
			})
	// 明细grid
	var detailGrid = new Ext.grid.GridPanel({
		// height : 220,
		region : "center",
		style : "padding-top:0px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		border : false,
		// clicksToEdit : 1,
		autoScroll : true,
		sm : new Ext.grid.RowSelectionModel({
					singleSelect : true
				}),
		store : detailStore,
		columns : [new Ext.grid.RowNumberer({
							header : "行号",
							width : 35
						}),
				// 到货单号
				{
					header : "项次号",
					sortable : true,
					width : 75,
					dataIndex : 'issueDetailsId'
				},
				// 供应商
				{
					header : "物料编码<font color='red'>*</font>",
					sortable : true,
					css : CSS_GRID_INPUT_COL,
					width : 75,
					dataIndex : 'materialNo'
				},
				// 物料名称
				{
					header : "物料名称",
					sortable : true,
					width : 75,
					dataIndex : 'materialName'
				},
				// 规格型号
				{
					header : "规格型号",
					sortable : true,
					width : 75,
					dataIndex : 'specNo'
				},
				// 材质/参数
				{
					header : "材质/参数",
					sortable : true,
					width : 75,
					dataIndex : 'parameter'
				},
				// 单位
				{
					header : "单位",
					sortable : true,
					width : 75,
					dataIndex : 'unitName'
				},
				// 申请数量
				{
					header : "申请数量<font color='red'>*</font>",
					sortable : true,
					width : 75,
					align : "right",
					// renderer : numRenderer,
					// css : CSS_GRID_INPUT_COL,
					// editor : new Ext.form.NumberField({
					// maxValue : 999999999999999.99
					// }),
					dataIndex : 'appliedCount'
				},
				// 核准数量
				{
					header : "核准数量",
					sortable : true,
					align : "right",
					// renderer : numRenderer,
					width : 75,
					dataIndex : 'approvedCount'
				},
				// 已发货数量
				{
					header : "已发货数量",
					sortable : true,
					align : "right",
					// renderer : numRenderer,
					width : 75,
					dataIndex : 'actIssuedCount'
				},
				// 待发货数量
				{
					header : "待发货数量",
					sortable : true,
					align : "right",
					// renderer : numRenderer,
					width : 75,
					dataIndex : 'waitCount'
				},
				// 成本分摊项目
				{
					header : "成本分摊项目",
					sortable : true,
					width : 100,
					// css : CSS_GRID_INPUT_COL,
					editor : new Ext.form.ComboBox({
								store : txtCostItemStore,
								displayField : "name",
								valueField : "id",
								mode : 'local',
//								triggerAction : 'all',
								readOnly : true
							}),
					renderer : function(val) {
						if (val == "1") {
							return "成本分摊项目1";
						} else if (val == "2") {
							return "成本分摊项目2";
						} else if (val == "3") {
							return "成本分摊项目3";
						} else if (val == "4") {
							return "成本分摊项目4";
						} else {
							return "";
						}
					},
					dataIndex : 'costItemId'
				},
				// TODO 费用来源
				{
					header : "费用来源",
					sortable : true,
					width : 75,
					css : CSS_GRID_INPUT_COL,
					editor : new Ext.form.ComboBox({
								store : txtMrItemStore,
								displayField : "name",
								valueField : "id",
								mode : 'local',
								triggerAction : 'all',
								readOnly : true
							}),
					renderer : function(val) {
						if (val == "1") {
							return "费用来源1";
						} else if (val == "2") {
							return "费用来源2";
						} else if (val == "3") {
							return "费用来源3";
						} else if (val == "4") {
							return "费用来源4";
						} else {
							return "";
						}
					},
					dataIndex : 'itemId'
				}],

		enableColumnMove : false
	});
	new Ext.Viewport({
				layout : 'border',
				// border : false,
				enableTabScroll : true,
				items : [{
							// layout : 'fit',
							border : false,
							region : "north",
							height : 205,
							split : true,
							margins : '0 0 0 0',
							items : [mypanl]
						}, {
							layout : 'fit',
							region : "center",
							border : false,
							collapsible : true,
							split : true,
							margins : '0 0 0 0',
							items : [detailGrid]
						}]
			});

});