Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Constants.RS012_E_001 = "当前领料单已经上报了，不能操作。请确认。";
Ext.QuickTips.init();
Ext.onReady(function() {
	
	var txtMrByH = new Ext.form.Hidden();
	
	var issueApprove = new IssueApprove();
	var entryId;
	var orginalId;
	Ext.override(Ext.grid.GridView, {
		doRender : function(cs, rs, ds, startRow, colCount, stripe) {
			var ts = this.templates, ct = ts.cell, rt = ts.row, last = colCount
					- 1;
			var tstyle = 'width:' + this.getTotalWidth() + ';';
			// buffers
			var buf = [], cb, c, p = {}, rp = {
				tstyle : tstyle
			}, r;
			for (var j = 0, len = rs.length; j < len; j++) {
				r = rs[j];
				cb = [];
				var rowIndex = (j + startRow);
				for (var i = 0; i < colCount; i++) {
					c = cs[i];
					p.id = c.id;
					p.css = i == 0 ? 'x-grid3-cell-first ' : (i == last
							? 'x-grid3-cell-last '
							: '');
					p.attr = p.cellAttr = "";
					p.value = c.renderer(r.data[c.name], p, r, rowIndex, i, ds);
					p.style = c.style;
					if (p.value == undefined || p.value === "")
						p.value = "&#160;";
					if (r.dirty && typeof r.modified[c.name] !== 'undefined') {
						p.css += ' x-grid3-dirty-cell';
					}
					cb[cb.length] = ct.apply(p);
				}
				var alt = [];
				if (stripe && ((rowIndex + 1) % 2 == 0)) {
					alt[0] = "x-grid3-row-alt";
				}
				if (r.dirty) {
					alt[1] = " x-grid3-dirty-row";
				}
				rp.cols = colCount;
				if (this.getRowClass) {
					alt[2] = this.getRowClass(r, rowIndex, rp, ds);
				}
				rp.alt = alt.join(" ");
				rp.cells = cb.join("");
				buf[buf.length] = rt.apply(rp);
			}
			return buf.join("");
		}
	});
	// record
	var queryRecord = new Ext.data.Record.create([
			// 领料单ID
			{
				name : 'issueHeadId'
			}, // 单据状态
			{
				name : 'issueStatus'
			},
			// 领料单编号，
			{
				name : 'issueNo'
			},
			// 项目编号
			{
				name : 'projectCode'
			},
			// 工单编号
			{
				name : 'woNo'
			},
			// 成本分摊项目编码
			{
				name : 'costItemId'
			},
			// 费用来源ID
			{
				name : 'itemId'
			},
			// 需求计划申请单编号
			{
				name : 'mrNo'
			},// 申请领料人
			{
				name : 'receiptBy'
			},// 领用部门
			{
				name : 'receiptDep'
			},// 申请领用日期
			{
				name : 'dueDate'
			},// 费用归口部门
			{
				name : 'feeByDep'
			},// 费用归口专业
			{
				name : 'feeBySpecial'
			},// 备注
			{
				name : 'memo'
			},// 单据状态
			{
				name : 'issueStatus'
			},// 是否紧急领用
			{
				name : 'isEmergency'
			},// 上次修改日期
			{
				name : 'lastModifiedDate'
			},// 工作流实例号
			{
				name : 'workFlowNo'
			},// 计划来源id
			{
				name : 'planOriginalId'
			}]);
	// 领料单grid的store
	var queryStore = new Ext.data.JsonStore({
		url : 'resource/getIssueListByLoginJoin.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : queryRecord
	});
//	queryStore.load({
//		params : {
//			start : 0,
//			limit : Constants.PAGE_SIZE
//		}
//	});
	// 确认按钮是否可用
	queryStore.on("load", function() {
//		if (queryStore.getCount() == 0) {
//			confirmBtn.setDisabled(true);
//		} else {
//			confirmBtn.setDisabled(false);
//		}
		var date;
		for (var i = 0; i < queryStore.getCount(); i++) {
			if (!queryStore.getAt(i).get('feeBySpecial')) {
				queryStore.getAt(i).set('feeBySpecial', "");
			}
		}

	});
//	var confirmBtn = new Ext.Button({
//		text : '审批',
//		id : 'confirmBtn',
//		name : 'confirmBtn',
//		disabled : false,
//		handler : function() {
//			issueSign();
//			// 5/7/09 调用双击grid相同的方法 yiliu
////			ShowIssueHeadRegister();
//		}
//	});
	// grid工具栏
	var txtIssueNo=new Ext.form.TextField({
	    name:'txtIssueNo',
	    fieldLabel:'领料单号'
	});
	    	// 定义审批状态
	var stateComboBox = new Ext.form.ComboBox({
				id : "stateCob",
				store : issueApprove.approveQueryStatus,
				displayField : "name",
				valueField : "value",
				mode : 'local',
				triggerAction : 'all',
				hiddenName:'stateComboBox',
				readOnly : true,
				value:'',
				width : 135
			});
			
	var btnView = new Ext.Button({
		text : '查看流程',
		iconCls:'view',
		id : 'btnView',
		name : 'btnView',
		disabled : false,
		handler : function() {
			if (queryGrid.selModel.hasSelection()) {
			var record = queryGrid.getSelectionModel().getSelected();
			var entryId = record.get("workFlowNo");
		    var url = application_base_path + "workflow/manager/show/show.jsp?entryId="+entryId;
			
			  window.open(url);
			}
			else
			{
				Ext.Msg.alert("提示","请选择要查看的记录！");
			}
		}
	});
	
	function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";
		Year = DateIn.getYear();
		Month = DateIn.getMonth() + 1;
		Day = DateIn.getDate();
		CurrentDate = Year + "-";
		if (Month >= 10) {
			CurrentDate = CurrentDate + Month + "-";
		} else {
			CurrentDate = CurrentDate + "0" + Month + "-";
		}
		if (Day >= 10) {
			CurrentDate = CurrentDate + Day;
		} else {
			CurrentDate = CurrentDate + "0" + Day;
		}
		return CurrentDate;
	}
	
	var enddate = new Date();
	var startdate = enddate.add(Date.MONTH, -1);
	startdate = startdate.getFirstDateOfMonth();
	var sdate = ChangeDateToString(startdate);
	var edate = ChangeDateToString(enddate);
	
	var timefromDate = new Ext.form.TextField({
		id : 'timefromDate',
		name : '_timefromDate',
		fieldLabel : "开始",
		style : 'cursor:pointer',
		cls : 'Wdate',
		width : 90,
		value : sdate,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-01',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true
				});
			}
		}
	});
	var timetoDate = new Ext.form.TextField({
		name : '_timetoDate',
		value : edate,
		id : 'timetoDate',
		fieldLabel : "结束",
		style : 'cursor:pointer',
		cls : 'Wdate',
		width : 90,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-01',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true
				});
			}
		}
	});
	
	// 是否显示所有
    var ckbShowAll = new Ext.form.Checkbox({
        id : 'ckbShowAll',
        boxLabel : "显示所有",
        width : 140,
        hideLabel : true,
        listeners : {
            check : showAllHandler
        }
    })
    function showAllHandler(ckb, value) {
    	if(txtMrByH.getValue() != null && txtMrByH.getValue() !="") {
    		txtMrByH.setValue("");
    		findFuzzy();
    	} else {
    		getUserName();
    	}
    }
				
	var headTbar = new Ext.Toolbar({
		region : "north",
		height : 25,
		items : [ckbShowAll,'-',timefromDate,'-',timetoDate,'-','领料单号：',txtIssueNo,'-','审批状态：',stateComboBox,{
			text : "查询",
			iconCls : Constants.CLS_QUERY,
			handler : function() {
				queryStore.baseParams={
				    dateFrom : timefromDate.getValue(),// txtMrDateFrom.getValue(),
				    dateTo : timetoDate.getValue(), // txtMrDateTo.getValue(),
				    issueNo:txtIssueNo.getValue(),
				    status:stateComboBox.getValue(),
				    mrBy : txtMrByH.getValue()
				};
				queryStore.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});
			}
		}]
	})
	// 查询grid
	var queryGrid = new Ext.grid.GridPanel({
		region : "center",
		border : false,
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
		fitToFrame : true,
		store : queryStore,
		columns : [new Ext.grid.RowNumberer({
			header : "行号",
			width : 35
		}),
				// 领料单编号
				{
					header : "领料单编号",
					sortable : true,
					width : 80,
					dataIndex : 'issueNo'
				},
				// 单据状态
				{
					header : "单据状态",
					sortable : true,
					width : 100,
					renderer : IssueStatusValue,
					dataIndex : 'issueStatus'
				},
				// 需求计划单编号
				{
					header : "需求计划单编号",
					sortable : true,
					width : 100,
					dataIndex : 'mrNo'
				},
				// 申请领料人
				{
					header : "申请领料人",
					sortable : true,
					width : 100,
					renderer : function(val) {
						// 查找对应的申请人名字
						var url = "resource/getReceiptByName.action?receiptBy="
								+ val;
						var conn = Ext.lib.Ajax.getConnectionObject().conn;
						conn.open("POST", url, false);
						conn.send(null);
						if (conn.status == 200) {
							return conn.responseText;
						}

						return "";
					},
					dataIndex : 'receiptBy'
				},
				// 日期
				{
					header : "申请日期",
					sortable : true,
					width : 100,
					renderer : renderDate,
					dataIndex : 'dueDate'
				},
				// 日期
				{
					header : "备注",
					sortable : true,
					width : 200,
					dataIndex : 'memo'
				}],
		viewConfig : {
			forceFit : true
		},
		enableColumnMove : false,
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : Constants.PAGE_SIZE,
			store : queryStore,
			displayInfo : true,
			displayMsg : Constants.DISPLAY_MSG,
			emptyMsg : Constants.EMPTY_MSG
		})
	});

	// ↓↓*******************查看备注************************
	// 备注
	var memoText = new Ext.form.TextArea({
		id : "memoText",
		readOnly : true,
		width : 180
	});

	// 弹出画面
	var win = new Ext.Window({
		height : 170,
		width : 350,
		layout : 'fit',
		modal : true,
		resizable : false,
		closeAction : 'hide',
		items : [memoText],
		buttonAlign : "center",
		title : '详细信息查看窗口',
		buttons : [{
			text : '返回',
			handler : function() {
				win.hide();
			}
		}]
	});
	// ↑↑*******************查看备注************************
//	queryGrid.on({
//		// 'rowdblclick' : ShowIssueHeadRegister,
//		'rowdblclick' : issueSign,
//		'cellclick' : showMemoWin
//	});
	// 用于保存明细初值
	var detailRecords = [];
	// ↓↓*********************登记tab***************************

	var btnSaveIssue = new Ext.Button({
		text : "签字",
		id : "btnSaveIssue",
		iconCls : Constants.CLS_SAVE,
		handler : saveIssueHandler
	});
	// 领料单表头流水号
	var hdnIssueHeadId = new Ext.form.Hidden({
		id : 'issueHeadId',
		name : "headInfo.issueHeadId",
		value : ""
	});
	// 领料单审批状态
	var hdnIssueStatus = new Ext.form.Hidden({
		id : 'issueStatus',
		name : "headInfo.issueStatus"
	});
	// 明细flag(标记明细grid中数据的来源)
	var hdnDetailFlag = new Ext.form.Hidden({
		value : "1"
	});
	// 申请单id
	var hdnRequimentHeadId = new Ext.form.Hidden({
		value : ""
	});
	// 上次修改时间
	var hdnLastModifiedDate = new Ext.form.Hidden({
		value : "",
		id : "lastModifiedDate",
		name : "headInfo.lastModifiedDate"
	});
	var wd = 120;
	// 是否紧急领用
	var isEmergency = new Ext.form.Checkbox({
		id : "isEmergency",
		hideLabel : true,
		width : wd,
		boxLabel : "是否紧急领用",
		disabled : true,
		name : "headInfo.isEmergency"
	});
	// 领料单编号
	var issueNo = new Ext.form.TextField({
		id : "issueNo",
		fieldLabel : '领料单编号',
		readOnly : true,
		width : wd,
		isFormField : true,
		name : 'headInfo.issueNo'

	});
	// 工单编号
	var woNo = new Ext.form.TextField({
		id : "woNo",
		fieldLabel : '工单编号',
		readOnly : true,
		width : wd,
		isFormField : true,
		name : 'headInfo.woNo'

	});
	// 需求计划单编号
	var mrNo = new Ext.form.TextField({
		id : "mrNo",
		fieldLabel : '需求计划单编号',
		readOnly : true,
		isFormField : true,
		width : wd,
		name : 'headInfo.mrNo'
	});
	// 项目编号
	var projectCode = new Ext.form.TextField({
		id : "projectCode",
		fieldLabel : '项目编号',
		readOnly : true,
		isFormField : true,
		width : wd,
		name : 'headInfo.projectCode'
	});
	// TODO 成本分摊项目
	var txtCostItemStore = new Ext.data.SimpleStore({
		fields : ['id', 'name'],
		data : [['1', '成本分摊项目1'], ['2', '成本分摊项目2'], ['3', '成本分摊项目3'],
				['4', '成本分摊项目4']]
	});
	// 成本分摊隐藏域
	var hdnCostItemId = new Ext.form.ComboBox({
		id : "hdnCostItemId",
		name : "headInfo.costItemId",
		disabled : true
	});
	// TODO 成本分摊项目
	var costItemId = new Ext.form.ComboBox({
		store : txtCostItemStore,
		id : "costItemId",
		displayField : "name",
		valueField : "id",
		mode : 'local',
		width : wd,
		fieldLabel : "成本分摊项目",
		triggerAction : 'all',
		disabled : true,
		readOnly : true,
		listeners : {
			'select' : function(combo, record, index) {
				hdnCostItemId.setValue(record.get('id'));
			}
		}
	})
	// TODO 费用来源store
	var txtMrItemStore = new Ext.data.SimpleStore({
		fields : ['id', 'name'],
		data : [['1', '费用来源1'], ['2', '费用来源2'], ['3', '费用来源3'], ['4', '费用来源4']]
	});
	// 费用来源隐藏域
	var hdnItemId = new Ext.form.Hidden({
		id : "hdnItemId",
		name : "headInfo.itemId"
	});
	// TODO 费用来源
	var itemId = new Ext.form.ComboBox({
		store : txtMrItemStore,
		id : "itemId",
		displayField : "name",
		valueField : "id",
		mode : 'local',
		width : wd,
		fieldLabel : "费用来源",
		triggerAction : 'all',
		readOnly : true,
		disabled : true,
		listeners : {
			'select' : function(combo, record, index) {
				hdnItemId.setValue(record.get('id'));
			}
		}
	})
	// 申请领用人
	var receiptBy = new Ext.form.TriggerField({
		isFormField : true,
		width : wd,
		fieldLabel : "申请领用人",
		readOnly : true
	});
	// 申请领用人隐藏域
	var hdnReceiptBy = new Ext.form.Hidden({
		id : "receiptBy",
		isFormField : true,
		name : "headInfo.receiptBy"
	});
	// 联动用事件
	hdnReceiptBy.addEvents('valueChanged');
	// 联动处理
	hdnReceiptBy.on('valueChanged', setDeptNameByEmpCode);
	// 领用部门
	var receiptDep = new Ext.form.TextField({
		width : wd,
		fieldLabel : "领用部门",
		readOnly : true
	});
	// 领用部门
	var hdnReceiptDep = new Ext.form.Hidden({
		id : "receiptDep",
		name : "headInfo.receiptDep"
	});
	// 申请领用日期
	var dueDate = new Ext.form.TextField({
		style : 'cursor:pointer',
		id : "dueDate",
		name : 'headInfo.dueDate',
		readOnly : true,
		width : wd,
		allowBlank : false,
		fieldLabel : '申请领用日期'

	});

	// 费用归口部门
	var feeByDep = new Ext.form.TextField({
		width : wd,
		readOnly : true,
		fieldLabel : "费用归口部门"
	});
	// 费用归口部门
	var hdnFeeByDep = new Ext.form.Hidden({
		id : "feeByDep",
		name : "headInfo.feeByDep"
	});
	// 申请领用人的store
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
		name : "headInfo.feeBySpecial"
	});
	// 费用归口专业
	var feeBySpecial = new Ext.form.ComboBox({
		store : feeBySpecialStore,
		id : "feeBySpecial",
		displayField : "specialityName",
		valueField : "specialityCode",
		mode : 'local',
		width : wd,
		fieldLabel : "费用归口专业",
		disabled : true,
		triggerAction : 'all',
		readOnly : true,
		listeners : {
			'select' : function(combo, record, index) {
				hdnFeeBySpecial.setValue(record.get('specialityCode'));
			}
		}
	});
	// 备注
	var memo = new Ext.form.TextArea({
		id : "memo",
		name : "headInfo.memo",
		height : Constants.TEXTAREA_HEIGHT,
		maxLength : 127,
		readOnly : true,
		anchor : "98.45%",
		fieldLabel : "备注"
	});

	// 计划来源
	var txtMrOriginal = new Ext.ux.ComboBoxTree({
		fieldLabel : '计划来源<font color ="red">*</font>',
		anchor : '100%',
		displayField : 'text',
		valueField : 'id',
		allowBlank : false,
		id : 'planOriginalId',
		hiddenName : 'headInfo.planOriginalId',
		blankText : '请选择',
		emptyText : '请选择',
		readOnly : true,
		disabled : true,
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
		},
		selectNodeModel : 'leaf',
		listeners : {
			'select' : function(combo, record, index) {
				txtMrOriginalH.setValue(record.get('planOriginalId'));

			}
		}
	});
	var txtMrOriginalH = new Ext.form.Hidden();

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
	// 明细grid的store
	var detailStore = new Ext.data.JsonStore({
		url : 'resource/getIssueHeadRegisterDetailList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : detailRecord
	});
	// 个数变更
	detailStore.addEvents('countChanged');
	detailStore.on({
		// 传递参数
		'beforeload' : function() {
			Ext.apply(this.baseParams, {
				issueHeadId : hdnIssueHeadId.getValue(),
				flag : hdnDetailFlag.getValue(),
				requimentHeadId : hdnRequimentHeadId.getValue()
			});
		}
	});
	// 明细grid
	var detailGrid = new Ext.grid.EditorGridPanel({
		height : 220,
		region : "center",
		style : "padding-top:0px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		border : false,
		clicksToEdit : 1,
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
					header : "物料编码",
					sortable : true,
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
					header : "申请数量",
					sortable : true,
					width : 75,
					align : "right",
					renderer : numRenderer,
					dataIndex : 'appliedCount'
				},
				// 核准数量
				{
					header : "核准数量<font color='red'>*</font>",
					sortable : true,
					align : "right",
					renderer : numRenderer,
					width : 75,
					css : CSS_GRID_INPUT_COL,
					editor : new Ext.form.NumberField({
						maxValue : 999999999999999.99
					}),
					dataIndex : 'approvedCount'
				},
				// 已发货数量
				{
					header : "已发货数量",
					sortable : true,
					align : "right",
					renderer : numRenderer,
					width : 75,
					dataIndex : 'actIssuedCount'
				},
				// 待发货数量
				{
					header : "待发货数量",
					sortable : true,
					align : "right",
					renderer : numRenderer,
					width : 75,
					dataIndex : 'waitCount'
				},
				// 成本分摊项目
				{
					header : "成本分摊项目",
					sortable : true,
					width : 100,
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

		enableColumnMove : false,
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : Constants.PAGE_SIZE,
			store : detailStore,
			displayInfo : true,
			displayMsg : Constants.DISPLAY_MSG,
			emptyMsg : Constants.EMPTY_MSG
		})
	});

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
				items : [hdnIssueHeadId, hdnLastModifiedDate, hdnIssueStatus,
						issueNo]
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
				items : [costItemId, hdnCostItemId]
			}, {
				columnWidth : 0.3,
				layout : "form",
				border : false,
				height : 26,
				items : [itemId, hdnItemId]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.3,
				layout : "form",
				border : false,
				height : 26,
				items : [hdnReceiptBy, receiptBy]
			}, {
				columnWidth : 0.3,
				layout : "form",
				border : false,
				height : 26,
				items : [receiptDep, hdnReceiptDep]
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
				items : [feeByDep, hdnFeeByDep]
			}, {
				columnWidth : 0.3,
				layout : "form",
				border : false,
				height : 26,
				items : [feeBySpecial, hdnFeeBySpecial]
			}, {
				columnWidth : 0.29,
				layout : "form",
				border : false,
				height : 26,
				items : [txtMrOriginal, txtMrOriginalH]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.9,
				layout : "form",
				height : 49,
				border : false,
				items : [memo]
			}]
		}]
	});
	 
	var formPanel = new Ext.FormPanel({
		border : false,
		region : "north",
		width : 650,
		height : 180,
		style : "padding-top:0px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		labelAlign : 'right',
		items : [headForm]
	});
	// 表单增加设置初始值事件
	formPanel.addEvents('doInit');
	formPanel.on('doInit', function() {
		formPanel.getForm().items.each(function(f) {
			f.originalValue = f.getValue();
		});
	})

	var panel = new Ext.Panel({
		region : "center",
		layout : 'border',
		items : [formPanel, detailGrid],
		tbar : [btnSaveIssue]
	})
	
//	var timePanel = new Ext.Panel({
//		id : 'timeAreaPanel',
//		border : false,
//		layout : 'form',
//		labelWidth : 32,
//		items : [{
//			layout : 'column',
//			bodyStyle : "padding:0 0 0 38",
//			border : false,
//			items : [{
//				border : false,
//				columnWidth : 0.5,
//				align : 'center',
//				layout : 'form',
//				items : [timefromDate]
//			}, {
//				align : 'center',
//				layout : 'form',
//				border : false,
//				columnWidth : 0.5,
//				items : [timetoDate]
//			}]
//
//		}]
//	});
	
	// 表单panel
	var formPanel = new Ext.FormPanel({
		labelAlign : 'right',
		labelPad : 15,
		labelWidth : 75,
		border : false,
		autoHeight : true,
		region : 'north',
		items : [headTbar]
	});

	// 供应商查询 Panel
	var tabQuery = new Ext.Panel({
		title : '查询',
		layout : 'border',
		border : false,
		items : [formPanel, queryGrid]
	})
	// 供应商查询 Panel
	var tabRegister = new Ext.Panel({
		title : '审批',
		layout : 'fit',
		border : false,
		items : [panel]
	})

	// **********主画面********** //
	var tabPanel = new Ext.TabPanel({
		activeTab : 0,
		layoutOnTabChange : true,
		tabPosition : 'bottom',
		border : false,
		items : [tabQuery
		// , tabRegister
		]
	})

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		border : false,
		items : [{
			layout : 'fit',
			border : false,
			margins : '0 0 0 0',
			region : 'center',
			autoScroll : true,
			items : [tabPanel]
		}]
	});
	
	getUserName();
	
	function findFuzzy(start) {
		if (start > 0) {
			start = start;
		} else {
			start = 0;
		}
		var startDate = timefromDate.getValue();
		var endDate = timetoDate.getValue();
		if (startDate != "" && endDate != "") {
			var res = compareDateStr(startDate, endDate);
			if (res) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(
						Constants.COM_E_006, "计划月份（开始月份）", "至（终止月份）"));
			} else {
				queryStore.baseParams = {
					dateFrom : startDate,
					dateTo : endDate,
					mrBy : txtMrByH.getValue(),
					status : stateComboBox.getValue()
				};
				queryStore.load({
					params : {
						start : start,
						limit : Constants.PAGE_SIZE
					}
				});
			}
		}
		
	}
	
	// 从session取登录人编码姓名部门相关信息
	function getUserName() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result.workerCode) {
					// 设定默认工号，赋给全局变量
					txtMrByH.setValue(result.workerCode);
					workCode = result.workerCode;
				}
				findFuzzy();// add by ywliu 200911003
			}
		});
	}
	
	/**
	 * textField显示时间比较方法
	 */
	function compareDate(argDate1, argDate2) {
		return argDate1.getTime() >= argDate2.getTime();
	}
	
	/**
	 * textField显示时间比较方法
	 */
	function compareDateStr(argDateStr1, argDateStr2) {
		var date1 = Date.parseDate(argDateStr1, 'Y-m-d');
		var date2 = Date.parseDate(argDateStr2, 'Y-m-d');
		return compareDate(date1, date2);
	}

	/**
	 * 根据人员code查找对应名字
	 */
	function findEmpName(empCode) {
		// 画面初始值设置
		Ext.lib.Ajax.request('POST', 'resource/getReceiptByName.action', {
			success : function(action) {
				var name = action.responseText;
				receiptBy.setValue(name);
				formPanel.fireEvent('doInit');
				hdnReceiptBy.fireEvent('valueChanged', true);
			}
		}, 'receiptBy=' + empCode);
	}
	function IssueStatusValue(value) {
//		if (value == "0") {
//			return "待审批状态";
//		}
//		if (value == "1") {
//			return "审批中状态";
//		}
//		if (value == "2") {
//			return "审批结束状态";
//		}
//		if (value == "9") {
//			return "退回状态";
//		}
		return issueApprove.getStatusName(value);
	}

	function ShowIssueHeadRegister() {
		if (queryGrid.selModel.hasSelection()) {
			var record = queryGrid.getSelectionModel().getSelected();
			showRegisterTab(record);
		} else {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
		}
	}

	/**
	 * 显示登记tab
	 * 
	 * @param record
	 *            要登记的记录
	 */
	function showRegisterTab(record) {
		// 跳到登记tab
		tabPanel.setActiveTab(1);

		// 费用来源
		if (record.get('itemId') == null) {
			record.set('itemId', "");
		}
		// 成本分摊项目
		if (record.get('costItemId') == null) {
			record.set('costItemId', "");
		}
		// 费用归口专业
		if (record.get('feeBySpecial') == null) {
			record.set('feeBySpecial', "");
		}
		if (record.data['planOriginalId'] != "") {
			Ext.Ajax.request({
				params : {
					planOrigianlId : record.data['planOriginalId']
				},
				url : 'resource/getOrigianlName.action',
				method : 'post',
				success : function(result) {
					var recordSub = eval('(' + result.responseText + ')');
					var obj = new Object();
					if (recordSub.data != null && recordSub.data != "") {
						obj.text = recordSub.data['planOriginalDesc'];
						obj.id = parseInt(record.data['planOriginalId']);
						txtMrOriginal.setValue(obj);
					}
				},
				failure : function(action) {
				}

			});
		};
		// 单据状态
		var status = record.get('issueStatus');
		// 需求计划单编号
		var mrNo = record.get('mrNo');
		formPanel.getForm().trackResetOnLoad = true;
		formPanel.getForm().loadRecord(record);
		// 申请领用日期
		dueDate.setValue(renderDate(record.get("dueDate")));
		dueDate.originalValue = dueDate.getValue();
		// 成本分摊项目
		hdnCostItemId.setValue(record.get('costItemId'));
		// 费用来源id
		hdnItemId.setValue(record.get('itemId'));
		// 归口专业
		hdnFeeBySpecial.setValue(record.get('feeBySpecial'));
		// 是否紧急领用
		if (record.get("isEmergency") == "Y") {
			isEmergency.setValue(true);
			isEmergency.originalValue = true;
		}
		// 归口部门
		findEmpName(hdnReceiptBy.getValue());
		formPanel.fireEvent('doInit');
		receiptBy.fireEvent('valueChanged', true);
		// 明细标记：'1'->从领料单明细取数据
		hdnDetailFlag.setValue('1');
		detailStore.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			}
		});
		entryId = record.get('workFlowNo');
		orginalId = record.get('planOriginalId');
		// 保存载入的数据，用于更新时check数据有无变更
		detailStore.on('load', function(store, records, options) {
			detailRecords = cloneDetailsFields(detailStore);
		})
	}
	/**
	 * 显示备注窗口
	 */
	function showMemoWin(grid, row, col, e) {
		// 记录
		var record = grid.getStore().getAt(row);
		// 字段
		var fieldName = grid.getColumnModel().getDataIndex(col);
		if (fieldName == 'memo') {
			var data = record.get(fieldName);
			memoText.setValue(data);
			win.show();
		}
	}
	function renderDate(value) {
		if (!value)
			return "";
		var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
		var strDate = value.match(reDate);
		if (!strDate)
			return "";
		return strDate;
	}

	/**
	 * 数字格式化
	 */
	function numRenderer(value) {
		if (value === "" || value == null) {
			return value
		}
		value = String(value);
		// 整数部分
		var whole = value;
		// 小数部分
		var sub = ".00";
		// 如果有小数
		if (value.indexOf(".") > 0) {
			whole = value.substring(0, value.indexOf("."));
			sub = value.substring(value.indexOf("."), value.length);
			sub = sub + "00";
			if (sub.length > 3) {
				sub = sub.substring(0, 3);
			}
		}
		var r = /(\d+)(\d{3})/;
		while (r.test(whole)) {
			whole = whole.replace(r, '$1' + ',' + '$2');
		}
		v = whole + sub;
		return v;
	}

	/**
	 * 保存操作处理
	 */
	function saveIssueHandler() {
		// 明细有没有变化
		// if (!isDetailRecordsChanged()) {
		// Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_006);
		// return;
		// }
		if (checkBeforeSave()) {
			var url = "RS015.jsp?issueHeadId=" + hdnIssueHeadId.getValue()
					+ "&entryId=" + entryId + "&orginalId=" + orginalId;
			var o = window.showModalDialog(url, '',
					'dialogWidth=800px;dialogHeight=500px;status=no');
			if (typeof(o) != "undefined") {
				var params = "issueHeadId=" + hdnIssueHeadId.getValue()
						+ "&actionId=" + o.actionId + "&approveText="
						+ o.opinion + "&workerCode=" + o.workerCode + "&nrs="
						+ o.nrs + "&eventIdentify=" + o.eventIdentify
						+ "&updateDetails="
						+ Ext.util.JSON.encode(getDetailRecords());
				Ext.lib.Ajax.request('POST',
						'resource/approveIssueHeadRecord.action', {
							success : function(action) {
								var result = eval("(" + action.responseText
										+ ")");
								if (!result.success) {
									Ext.Msg.alert("提示", "审批失败！");
								} else {
									Ext.Msg.alert("提示", "审批成功！");
									tabPanel.setActiveTab(0);
									initAllDatas();
								}
							},
							faliure : function(action) {
								Ext.Msg.alert("提示", "审批失败!");
							}
						}, params);
			}

		}
	}
	/**
	 * 保存前的check处理
	 */
	function checkBeforeSave() {
		// 核准数量必须输入check
		var flagA = false;
		for (var i = 0; i < detailStore.getCount(); i++) {
			var record = detailStore.getAt(i);
			if (!record.get('approvedCount')
					&& record.get('approvedCount') !== 0) {
				flagA = true;
			}
			if (flagA) {
				break;
			}
		}
		var msg = "";
		if (flagA) {
			msg += String.format(Constants.COM_E_002, "核准数量") + "<br/>";
		}
		if (msg) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, msg);
			return false;
		}
		// 画面有无明细check
		if (detailStore.getCount() == 0) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_E_018);
			return false;
		}
		// 核准数量大于零check
		for (var i = 0; i < detailStore.getCount(); i++) {
			var record = detailStore.getAt(i);
			if (record.get('approvedCount') <= 0) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(
						Constants.COM_E_013, '核准数量'));
				return false;
			}
		}
		return true;
	}
	/**
	 * 判断明细部分有无变化
	 */
	function isDetailRecordsChanged() {
		for (var i = 0; i < detailStore.getCount(); i++) {
			var nowRecord = detailStore.getAt(i);
			var initRecord = getDetailsRecord(nowRecord.get('issueDetailsId'));
			// 核准数量
			if (nowRecord.get('approvedCount') != initRecord['approvedCount']) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 从保存的明细数据中,根据明细流水号找到对应的记录
	 * 
	 * @param issueDetailsId
	 *            明细流水号
	 */
	function getDetailsRecord(issueDetailsId) {
		for (var i = 0; i < detailRecords.length; i++) {
			if (detailRecords[i]['issueDetailsId'] == issueDetailsId) {
				return detailRecords[i];
			}
		}
	}
	/**
	 * 获取明细数据中可变字段记录集
	 * 
	 * @param detailsStore
	 *            明细数据store
	 */
	function cloneDetailsFields(detailsStore) {
		var records = [];
		// 遍历
		for (var i = 0; i < detailsStore.getCount(); i++) {
			var record = detailsStore.getAt(i);
			var data = {};
			data['issueDetailsId'] = record.get('issueDetailsId');
			data['approvedCount'] = record.get('approvedCount');
			records.push(data);
		}
		return records;
	}
	/**
	 * 获取明细grid中的数据,传回后台
	 * 
	 * @param isNew
	 *            新记录
	 */
	function getDetailRecords() {
		var results = [];
		for (var i = 0; i < detailStore.getCount(); i++) {
			var record = detailStore.getAt(i);
			results.push(record.data);
		}
		return results;
	}
	/**
	 * 画面初期化
	 */
	function initAllDatas() {
		// 列表tab重新载入
		queryStore.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			}
		});

		// 清空保存的明细数据
		detailRecords = [];

		// 登记tab初始化
		formPanel.getForm().reset();
		// 清空form
		clearForm();
		// setEditable(1);
		detailStore.removeAll();
		detailStore.totalLength = 0;
		detailStore.fireEvent('countChanged');
		detailGrid.getBottomToolbar().updateInfo();
	}
	/**
	 * 清空form
	 */
	function clearForm() {
		var rec = new queryRecord({
			// 流水号
			'issueHeadId' : "",
			// 领料单编号
			"issueNo" : "",
			// 工单编号
			"woNo" : "",
			// 需求计划单编号
			"mrNo" : "",
			// 项目编号
			"projectCode" : "",
			// 成本分摊项目编码
			'costItemId' : "",
			// 费用来源
			"itemId" : "",
			// 申请领用人
			"receiptBy" : "",
			// 领用部门
			"receiptDep" : "",
			// 申请领用日期
			"dueDate" : "",
			// 费用归口部门
			"feeByDep" : "",
			// 备注
			'memo' : "",
			// 归口专业
			"feeBySpecial" : ""
		});
		formPanel.getForm().loadRecord(rec);
		dueDate.clearInvalid();
		// initReceiptBy();
		// 紧急领用清除
		isEmergency.setValue(false);
		isEmergency.originalValue = false;
		formPanel.fireEvent('doInit');
	}
	/**
	 * 根据申请领料人联动，显示部门
	 * 
	 * @param init
	 *            ->true如果是初始值
	 */
	function setDeptNameByEmpCode(init) {
		var empCode = hdnReceiptBy.getValue();
		// 查找部门信息
		Ext.lib.Ajax.request('POST', 'resource/getDeptInfoByEmpCode.action', {
			success : function(action) {
				// 部门信息
				var dept = eval("(" + action.responseText + ")");
				if (dept == null) {
					// 领用部门
					receiptDep.setValue("");
					hdnReceiptDep.setValue("");
					// 费用归口部门
					feeByDep.setValue("");
					hdnFeeByDep.setValue("");
				} else {
					// 领用部门
					receiptDep.setValue(dept.deptName);
					hdnReceiptDep.setValue(dept.deptCode);
					// 费用归口部门
					feeByDep.setValue(dept.deptName);
					hdnFeeByDep.setValue(dept.deptCode)
				}
				if (init) {
					receiptDep.originalValue = receiptDep.getValue();
					hdnReceiptDep.originalValue = hdnReceiptDep.getValue();
					// 费用归口部门
					feeByDep.originalValue = feeByDep.getValue();
					hdnFeeByDep.originalValue = hdnFeeByDep.getValue();
					// 申请领用人
					hdnReceiptBy.originalValue = hdnReceiptBy.getValue();
				}
			},
			failure : function() {
			}
		}, 'empCode=' + empCode);
	}
	function issueSign() {
		if (queryGrid.selModel.hasSelection()) {
			var _record = queryGrid.getSelectionModel().getSelected();
			var _issueHeadId = _record.get("issueHeadId");
			var _entryId = _record.get("workFlowNo");
			var _orginalId = _record.get("planOriginalId");
			var _arr = [];
			var url = "RS015.jsp?issueHeadId=" + _issueHeadId + "&entryId="
					+ _entryId + "&orginalId=" + _orginalId;
			var o = window.showModalDialog(url, '',
					'dialogWidth=800px;dialogHeight=500px;status=no');
			if (typeof(o) != "undefined") {
				var params = "issueHeadId=" + _issueHeadId
						+ "&actionId=" + o.actionId + "&approveText="
						+ o.opinion + "&workerCode=" + o.workerCode + "&nrs="
						+ o.nrs + "&eventIdentify=" + o.eventIdentify;
				Ext.lib.Ajax.request('POST',
						'resource/approveIssueHeadRecord.action', {
							success : function(action) {
								var result = eval("(" + action.responseText
										+ ")");
								if (!result.success) {
									Ext.Msg.alert("提示", "审批失败！");
								} else {
									Ext.Msg.alert("提示", "审批成功！");
									tabPanel.setActiveTab(0);
									initAllDatas();
								}
							},
							faliure : function(action) {
								Ext.Msg.alert("提示", "审批失败!");
							}
						}, params);
			}

		} else {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
		}
	}
});
