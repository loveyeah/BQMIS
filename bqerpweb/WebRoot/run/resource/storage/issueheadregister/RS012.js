Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Constants.RS012_E_001 = "当前领料单已经上报了，不能操作。请确认。";
Ext.QuickTips.init();
Ext.onReady(function() {
	var detailIds=""; //add by fyyang 20100408 需求计划中选择的明细ids
	
	var issueApprove=new IssueApprove();
	function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";
		// 初始化时间
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
	/**
	 * 获取当前时间
	 */
		var userYearMonth = "";
		var now = new Date();
		var month = now.getMonth();
		var day = now.getDate();
		var snow = now.getYear() + "-"
				+ (month + 1 > 9 ? month + 1 : ("0" + (month + 1))) + "-"
				+ (day > 9 ? day : ("0" + day));
	
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
					// 判断是否是最后行
					var status = checkStatus();
					if (c.name == 'costItemId' && status['costItemId'] == 1) {
						p.style = c.style
								.replace(/background\s*:\s*[^;]*;/, '');
					} else if (c.name == 'itemId' && status['itemId'] == 1) {
						p.style = c.style
								.replace(/background\s*:\s*[^;]*;/, '');
					} else if (c.name == 'appliedCount'
							&& status['appliedCount'] == 1) {
						p.style = c.style
								.replace(/background\s*:\s*[^;]*;/, '');
					} else if (c.name == 'materialNo'
							&& status['materialNo'] == 1) {
						p.style = c.style
								.replace(/background\s*:\s*[^;]*;/, '');
					} else if (r.data["isNewRecord"] == "total") {// 判断是否是最后行
						// 替换掉其中的背景颜色
						p.style = c.style
								.replace(/background\s*:\s*[^;]*;/, '');
					} else {
						p.style = c.style;
					}
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

//	/**
//	 * 设置归口专业与计划来源联动
//	 */
//	function setTxtMrCostSpecial(node) {
//		Ext.Ajax.request({
//			url : 'resource/getOriginalType.action',
//			method : 'post',
//			params : {
//				nodeId : node.id
//			},
//			success : function(action) {
//				var radioData = action.responseText;
//				txtMrOriginalH.setValue(radioData);
//				if (radioData == 'M') {
//					feeBySpecial.allowBlank = false;
//				//	feeBySpecial.enable();
//					feeBySpecial.disable(); //add by drdu 090618
//				} else if (radioData == 'O') {
//					feeBySpecial.setValue("");
//					feeBySpecial.clearInvalid();
//					feeBySpecial.disable();
//				}
//			},
//			failure : function(action) {
//
//			}
//		});
//	}

	// 显示所有领料单checkbox
	var issueAllCheck = new Ext.form.Checkbox({
		boxLabel : "显示所有领料单",
		anchor : '100%'
	});
	// grid工具栏
	var gridTbar = new Ext.Toolbar({
		items : [issueAllCheck]
	});
	// 查询值
	var fuzzy = new Ext.form.TextField({
		id : "fuzzy",
		value : "",
		width : 200,
		maxLength : 30,
		name : "fuzzy"
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
				name : 'itemCode'//modify by ywliu  2009/7/6
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
			}, {
				name : 'workFlowNo'
			}, {
				name : 'planOriginalId'
			}]);
	// 领料单grid的store
	var queryStore = new Ext.data.JsonStore({
		url : 'resource/getIssueHeadRigisterList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : queryRecord
	});

	queryStore.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			fuzzy : fuzzy.getValue(),
			flag : issueAllCheck.getValue() ? "1" : "0"
		});
	});
	queryStore.load({
		params : {
			start : 0,
			limit : Constants.PAGE_SIZE
		}
	});
	// 确认按钮是否可用
	queryStore.on("load", function() {
		if (queryStore.getCount() == 0) {
			confirmBtn.setDisabled(true);
		} else {
			confirmBtn.setDisabled(false);
		}
		var date;
		for (var i = 0; i < queryStore.getCount(); i++) {
			if (!queryStore.getAt(i).get('feeBySpecial')) {
				queryStore.getAt(i).set('feeBySpecial', "");
			}
		}

	});
	var confirmBtn = new Ext.Button({
		text : '确认',
		id : 'confirmBtn',
		name : 'confirmBtn',
		iconCls : Constants.CLS_OK,
		disabled : false,
		handler : function() {
			ShowIssueHeadRegister();
		}
	});
	// grid工具栏
	var headTbar = new Ext.Toolbar({
		region : "north",
		height : 25,
		items : ["领料单编号:", fuzzy, "-", {
			text : "查询",
			iconCls : Constants.CLS_QUERY,
			handler : function() {
				queryStore.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});
			}
		}]// , "-", confirmBtn 隐藏页面的“确认”按钮 modify by ywliu 090717
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
					renderer : function(value)
						{
							return issueApprove.getStatusName(value);
						
						},
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
		tbar : gridTbar,
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
	queryGrid.on({
		'rowdblclick' : ShowIssueHeadRegister,
		'cellclick' : showMemoWin
	});
	// 用于保存登记tab现在编辑的领料单数据
	var headRecord = {};
	// 用于保存已删除的领料单明细数据的流水号
	var deleteDetails = [];
	// 用于保存明细初值
	var detailRecords = [];
	// ↓↓*********************登记tab***************************
	var btnByPlan = new Ext.Button({
		text : "按计划单生成",
		iconCls : Constants.CLS_LIST,
		handler : addByPlanHandler
	});
	var btnAddIssue = new Ext.Button({
		text : "新增",
		id : "btnAddIssue",
		iconCls : Constants.CLS_ADD,
		handler : addIssueHandler
	});
	var btnDeleteIssue = new Ext.Button({
		text : "删除",
		id : "btnDeleteIssue",
		iconCls : Constants.CLS_DELETE,
		handler : deleteIssueHandler
	});
	var btnSaveIssue = new Ext.Button({
		text : "保存",
		id : "btnSaveIssue",
		iconCls : Constants.CLS_SAVE,
		handler : saveIssueHandler
	});
	var btnReport = new Ext.Button({
		text : "上报",
		id : "btnReport",
		iconCls : Constants.CLS_REPOET,
		handler : reportBtnHandler
	});
	var btnPrint = new Ext.Button({
		text : "打印",
		id : "btnPrint",
		iconCls : 'print',
		hidden : true,
		handler : printBtnHandler
	});
	// 领料单表头流水号
	var hdnIssueHeadId = new Ext.form.Hidden({
		id : 'issueHeadId',
		name : "headInfo.issueHeadId"
	//	value : ""
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
	var isEmergency = new Ext.form.Hidden({
		id : "isEmergency",
		hideLabel : true,
		width : wd,
		boxLabel : "是否紧急领用",
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
	var projectCode = new Ext.form.Hidden({
		id : "projectCode",
//		fieldLabel : '项目编号隐藏',
	//	isFormField : true,
	//	width : wd,
//		hidden:true,
//		hideLabel:true,
		name : 'headInfo.projectCode'
	});
	//add by fyyang 090602-------
	//项目编号显示
	var txtProjectCodeShow=new Ext.form.TextField({
	id : "txtProjectCodeShow",
		fieldLabel : '项目编号',
		readOnly : true,
		isFormField : true,
		width : wd,
		name : 'txtProjectCodeShow'
	});
	
	txtProjectCodeShow.onClick(selectProjectCode);
	function selectProjectCode()
	{	
		if((issueNo.getValue()==null||issueNo.getValue()=="") && txtMrOriginalH.getValue() == '2')//modify by ywliu 20091022
		{
			var args = {
				selectModel : 'single',
				rootNode : {
					id : "0",
					text : '合同类别'
				}
			}
			var url = "../../../../manage/project/query/ProjectForResourceSelect.jsp";
			var rvo = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (typeof(rvo) != "undefined") {
				Ext.get("headInfo.projectCode").dom.value=rvo.prjNo;
				//alert(rvo.prjNo);
				Ext.get("txtProjectCodeShow").dom.value=rvo.prjNoShow;
				if (rvo.prjNo != "") {
				
				itemId.setValue(getBudgetName(rvo.itemCode));
				hdnItemId.setValue(rvo.itemCode);
				txtAdviceItem.setValue(getBudgetName(rvo.itemCode, 1,
						hdnReceiptDep.getValue()));
				var data = getBudgetName(rvo.itemCode, 3, hdnReceiptDep.getValue());
				var mydata = data.split(",");
				txtFactItem.setValue(mydata[0]);
				txtCheckPrice.setValue(mydata[1]);
				var data1 = (parseFloat(mydata[1]) / parseFloat(txtAdviceItem
						.getValue()))
						* 100;
				var data2 = (parseFloat(mydata[0]) / parseFloat(txtAdviceItem
						.getValue()))
						* 100;
				txtCheckPriceRate
						.setValue(parseFloat(txtAdviceItem.getValue()) == 0
								? 0
								: numberFormat(data1));
				txtFactItemRate
						.setValue(parseFloat(txtAdviceItem.getValue()) == 0
								? 0
								: numberFormat(data2));
			} else {
				itemId.setValue("");
				hdnItemId.setValue("");
				txtAdviceItem.setValue(null);
				txtFactItem.setValue(null);
				txtCheckPrice.setValue(null);
				txtCheckPriceRate.setValue(null);
				txtFactItemRate.setValue(null);
			}

			}
		}
	}
	//----add end------------------
	// TODO 成本分摊项目
	var txtCostItemStore = new Ext.data.SimpleStore({
		fields : ['id', 'name'],
		data : [['1', '成本分摊项目1'], ['2', '成本分摊项目2'], ['3', '成本分摊项目3'],
				['4', '成本分摊项目4']]
	});
	// 成本分摊隐藏域
	var hdnCostItemId = new Ext.form.ComboBox({
		id : "hdnCostItemId",
		name : "headInfo.costItemId"
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
		readOnly : true,
		listeners : {
			'select' : function(combo, record, index) {
				hdnCostItemId.setValue(record.get('id'));
			}
		},
		hidden:true,
		hideLabel:true
	})
//	// TODO 费用来源store
//	var txtMrItemStore = new Ext.data.SimpleStore({
//		fields : ['id', 'name'],
//		data : [['1', '费用来源1'], ['2', '费用来源2'], ['3', '费用来源3'], ['4', '费用来源4']]
//	});
//	// 费用来源隐藏域
//	var hdnItemId = new Ext.form.Hidden({
//		id : "hdnItemId",
//		name : "headInfo.itemId"
//	});
//	// TODO 费用来源
//	var itemId = new Ext.form.ComboBox({
//		store : txtMrItemStore,
//		id : "itemId",
//		displayField : "name",
//		valueField : "id",
//		mode : 'local',
//		width : wd,
//		fieldLabel : "费用来源",
//		triggerAction : 'all',
//		readOnly : true,
//		listeners : {
//			'select' : function(combo, record, index) {
//				hdnItemId.setValue(record.get('id'));
//			}
//		}
//	})
	
	function choseItem() {
		
		var item = window
				.showModalDialog(
						'../../../../comm/jsp/item/budget/budget.jsp',
						null,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(item) != "undefined") {
			itemId.setValue(item.itemName);
			hdnItemId.setValue(item.itemCode);
		
			txtAdviceItem.setValue(getBudgetName(item.itemCode,1,hdnReceiptDep.getValue()));
			//txtFactItem.setValue(getBudgetName(item.itemCode,2,hdnReceiptDep.getValue()));
					//--------add by fyyang 20100419---------------
			var data=getBudgetName(item.itemCode,3,hdnReceiptDep.getValue());
			var mydata=data.split(",");
			txtFactItem.setValue(mydata[0]);
			txtCheckPrice.setValue(mydata[1]);
			var data1=( parseFloat(mydata[1])/parseFloat(txtAdviceItem.getValue()))*100;
			var data2=(parseFloat(mydata[0])/parseFloat(txtAdviceItem.getValue()))*100;
			txtCheckPriceRate.setValue(parseFloat(txtAdviceItem.getValue())==0? 0:  numberFormat(data1)) ;
            txtFactItemRate.setValue(parseFloat(txtAdviceItem.getValue())==0? 0: numberFormat(data2));
			//-------------------------------------------------------
		}
	}
	
	// 费用来源
	var itemId = new Ext.form.TriggerField({
		fieldLabel : '费用来源<font color ="red">*</font>',
		displayField : 'text',
		valueField : 'id',
		blankText : '请选择',
		emptyText : '请选择',
		anchor : '100%',
		name : "itemName",
		allowBlank : false,
		readOnly : true,
		hidden :false,
		hideLabel:false,
		onTriggerClick : function() {
			if(!this.disabled) {
				choseItem();
			}	
		}
	});
	// 费用来源隐藏域
	var hdnItemId = new Ext.form.Hidden({
		id : "hdnItemId",
		name : "headInfo.itemCode"
	});
	//  modify by ywliu ,将费用来源下拉框改为页面 2009/7/6
	
	// 申请领用人
	var receiptBy = new Ext.form.TriggerField({
		isFormField : true,
		width : wd,
		fieldLabel : "申请领用人",
		readOnly : true,
		onTriggerClick : function() {
			selectPersonWin();
		}
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
	// 申请领用日期  modify by drdu 090618
	var dueDate = new Ext.form.TextField({
		style : 'cursor:pointer',
		id : "dueDate",
		name : 'headInfo.dueDate',
		readOnly : true,
		width : wd,
		allowBlank : false,
		value : snow,
		fieldLabel : '申请领用日期<font color="red">*</font>'
//		listeners : {
//			focus : function() {
//				WdatePicker({
//					startDate : '%y-%M-%d',
//					alwaysUseStartDate : false,
//					dateFmt : 'yyyy-MM-dd',
//					onpicked : function() {
//						dueDate.clearInvalid();
//					},
//					onclearing : function() {
//						dueDate.markInvalid();
//					}
//				});
//			}
//		}
	});

	// 费用归口部门
	var feeByDep = new Ext.form.TextField({
		width : wd,
		readOnly : true,
		fieldLabel : "费用归口部门",
		hidden:true,
		hideLabel:true
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
		triggerAction : 'all',
		readOnly : true,
		listeners : {
			'select' : function(combo, record, index) {
				hdnFeeBySpecial.setValue(record.get('specialityCode'));
			}
		},
		hidden:true,
		hideLabel:true
	});
	
	var txtMrOriginal = new Ext.form.ComboBox({
		fieldLabel : '单据种类<font color ="red">*</font>',
		//name : 'planOriginalId',
		width : wd,
		id : 'planOriginalId',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'txtMrOriginal',
		//	allowBlank : false,
		editable : false,
		triggerAction : 'all',
		onTriggerClick : function() {
			if(entryId == "" || entryId == "null" || entryId == null){ // modify by ywliu 20091023
				var url = "billTypeSelectTree.jsp";
				var location = window.showModalDialog(url, '',
						'dialogWidth=300px;dialogHeight=200px;status=no');
				if (typeof(location) != "undefined") {
	                
					Ext.getCmp('planOriginalId').setValue(location.id);
					Ext.form.ComboBox.superclass.setValue.call(Ext
							.getCmp('planOriginalId'), location.text);
							txtMrOriginalH.setValue(location.id);
					if(location.id != "2") {//modify by ywliu 20091022
						txtProjectCodeShow.setValue("");
					}		
				}
			}

		}
	});

//	// 计划来源
//	var txtMrOriginal = new Ext.ux.ComboBoxTree({
//		fieldLabel : '计划来源<font color ="red">*</font>',
//		anchor : '100%',
//		displayField : 'text',
//		valueField : 'id',
//		allowBlank : false,
//		id : 'planOriginalId',
////		hiddenName : 'headInfo.planOriginalId',
//		hiddenName : 'txtMrOriginal',
//		blankText : '请选择',
//		emptyText : '请选择',
//		readOnly : true,
//		tree : {
//			xtype : 'treepanel',
//			// 虚拟节点,不能显示
//			rootVisible : false,
//			loader : new Ext.tree.TreeLoader({
//				dataUrl : 'resource/getPlanOriginalNode.action'
//			}),
//			root : new Ext.tree.AsyncTreeNode({
//				id : '0',
//				name : '合肥电厂',
//				text : '合肥电厂'
//			}),
//			listeners : {
//				click : setTxtMrCostSpecial
//			}
//		},
//		selectNodeModel : 'leaf',
//		listeners : {
//			'select' : function(combo, record, index) {
//				
//				txtMrOriginalH.setValue(record.get('planOriginalId'));
//               
//			}
//		}
//	});
	
	//-----add by fyyang 20100315-------
		  var txtAdviceItem=new Ext.form.TextField({
	  name : 'txtAdviceItem',
		fieldLabel : "本年预算费用",
		readOnly : true,
			width : 80
	  });
	
	 var txtFactItem=new Ext.form.TextField({
	  name : 'txtFactItem',
		fieldLabel : "本年已领费用",
		readOnly : true,
			width : 80
	  });
	//--------------------------------
	  
	   //----add by fyyang 20100419------------ 
	   var txtCheckPrice=new Ext.form.TextField({
	  name : 'txtCheckPrice',
		fieldLabel : "本年财务审核领料费用",
		readOnly : true,
	   width : 80
	  });
	     var txtCheckPriceRate=new Ext.form.TextField({
	  name : 'txtCheckPriceRate',
		fieldLabel : "本年财务审核领用费用完成率(%)",
		readOnly : true,
	     width : 60
	  });
	       var txtFactItemRate=new Ext.form.TextField({
	  name : 'txtFactItemRate',
		fieldLabel : "本年仓库发出费用完成率(%)",
		readOnly : true,
		  width : 70
	  });
	//---------------------------------------------
	  
	var txtMrOriginalH = new Ext.form.Hidden({
		id : 'txtMrOriginalH',
		name : 'headInfo.planOriginalId'
	});

	// 备注
	var memo = new Ext.form.TextArea({
		id : "memo",
		name : "headInfo.memo",
		height : Constants.TEXTAREA_HEIGHT,
		maxLength : 127,
		anchor : "98.45%",
		fieldLabel : "备注"
	});

	// 增加明细button
	var addDetailBtn = new Ext.Button({
		text : "新增明细",
		iconCls : Constants.CLS_ADD,
		handler : addDetailRecord
	});

	// 增加明细button
	var deleteDetailBtn = new Ext.Button({
		text : "删除明细",
		iconCls : Constants.CLS_DELETE,
		handler : deleteDetailRecord
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
			},
			// 当前库存
			{
				name : "stock" //add by ywliu 20091019
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
				strRequimentDetailIds:detailIds //modify by fyyang 20100408
				//requimentHeadId : hdnRequimentHeadId.getValue()
			});
		},
		// 判断删除明细按钮是否可用
		'countChanged' : function() {

			var isAdd = false;
			// 新规
			if (issueNo.getValue() == null || issueNo.getValue() == "") {
				isAdd = true;
			}
			if (!isAdd
					&& (headRecord.get('issueStatus') == "1" || headRecord
							.get('issueStatus') == "2")) {
				return;
			};
			if (mrNo.getValue()) {
				return;
			}
			if (detailStore.getCount() > 0) {
				deleteDetailBtn.setDisabled(false);
			} else {
				deleteDetailBtn.setDisabled(true);
			}
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
		tbar : [addDetailBtn, "-", deleteDetailBtn],
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
					hidden:true,
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
					renderer : function(value, cellmeta, record, rowIndex, columnIndex,
							store) {
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('appliedCount');
							}
							if (store.getAt(store.getCount() - 1).get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set('appliedCount',
										totalSum);
							}
							return numRenderer(value);
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('appliedCount');
							}
							return "<font color='red'>" + numRenderer(totalSum)
									+ "</font>";
						}
					},
					css : CSS_GRID_INPUT_COL,
					editor : new Ext.form.NumberField({
								maxValue : 999999999999999.99,
								minValue : 0,
								decimalPrecision : 4
							}),
					dataIndex : 'appliedCount'
				},
				// 核准数量
				{
					header : "核准数量",
					sortable : true,
					align : "right",
					renderer : function(value, cellmeta, record, rowIndex, columnIndex,
							store) {
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('approvedCount');
							}
							if (store.getAt(store.getCount() - 1).get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set('approvedCount',
										totalSum);
							}
							return numRenderer(value);
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('approvedCount');
							}
							return "<font color='red'>" + numRenderer(totalSum)
									+ "</font>";
						}
					},
					width : 75,
					dataIndex : 'approvedCount'
				},
				// 已发货数量
				{
					header : "已发货数量",
					sortable : true,
					align : "right",
					renderer : function(value, cellmeta, record, rowIndex, columnIndex,
							store) {
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('actIssuedCount');
							}
							if (store.getAt(store.getCount() - 1).get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set('actIssuedCount',
										totalSum);
							}
							return numRenderer(value);
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('actIssuedCount');
							}
							return "<font color='red'>" + numRenderer(totalSum)
									+ "</font>";
						}
					},
					width : 75,
					dataIndex : 'actIssuedCount'
				},
				// 待发货数量
				{
					header : "待发货数量",
					sortable : true,
					align : "right",
					renderer : function(value, cellmeta, record, rowIndex, columnIndex,
							store) {
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('waitCount');
							}
							if (store.getAt(store.getCount() - 1).get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set('waitCount',
										totalSum);
							}
							return numRenderer(value);
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('waitCount');
							}
							return "<font color='red'>" + numRenderer(totalSum)
									+ "</font>";
						}
					},
					width : 75,
					dataIndex : 'waitCount'
				},
				// 当前库存
				{
					// modified by liuyi 20100504 
//					header : "当前库存",
					header : "可用库存",
					sortable : true,
					renderer : function(v)
					{
						if(v != null)
							return numRenderer(v);
					},
					align : "right",
					width : 75,
					dataIndex : 'stock'
				},//add by ywliu 20091019
				// 成本分摊项目
				{
					header : "成本分摊项目",
					sortable : true,
					width : 100,
					hidden:true,
					css : CSS_GRID_INPUT_COL,
					editor : new Ext.form.ComboBox({
						store : txtCostItemStore,
						displayField : "name",
						valueField : "id",
						mode : 'local',
						triggerAction : 'all',
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
					hidden: true,
					width : 75,
//					css : CSS_GRID_INPUT_COL,
//					editor : new Ext.form.ComboBox({
//						store : txtMrItemStore,
//						displayField : "name",
//						valueField : "id",
//						mode : 'local',
//						triggerAction : 'all',
//						readOnly : true
//					}),
					renderer : function(val, cellmeta, record, rowIndex, columnIndex,
					store) {
//						if (val == "1") {
//							return "费用来源1";
//						} else if (val == "2") {
//							return "费用来源2";
//						} else if (val == "3") {
//							return "费用来源3";
//						} else if (val == "4") {
//							return "费用来源4";
//						} else {
//							return "";
//						}
						return itemId.getValue();
					},
					dataIndex : 'itemId'
				}],

		enableColumnMove : false
//		,
//		// 分页
//		bbar : new Ext.PagingToolbar({
//			pageSize : Constants.PAGE_SIZE,
//			store : detailStore,
//			displayInfo : true,
//			displayMsg : Constants.DISPLAY_MSG,
//			emptyMsg : Constants.EMPTY_MSG
//		})
	});

	detailGrid.on('beforeedit', function(obj) {
		// 记录
		var record = obj.record;
		// 字段名
		var field = obj.field;
		var isAdd = false;
		// 新规
		if (issueNo.getValue() == null || issueNo.getValue() == "") {
			isAdd = true;
		}
		// 是否上报
		var isReported = false;
		if (!isAdd
				&& (headRecord.get('issueStatus') == "1" || headRecord
						.get('issueStatus') == '2')) {
			isReported = true;
		}
		// 是否按计划单生成
		var isPlan = false;
		if (mrNo.getValue()) {
			isPlan = true;
		}
		// 成本分摊项目
		if (field == "costItemId") {
			if (isReported) {
				// 不可编辑
				obj.cancel = true;
			}
		}
		// 费用来源
		if (field == "itemId") {
			// 按计划生成或已上报不可修改
			if (isPlan || isReported) {
				obj.cancel = true;
			}
		}
		// 申请数量
		if (field == 'appliedCount') {
			// 按计划生成或已上报不可修改
			if (isPlan || isReported) {
				obj.cancel = true;
			}
		}
		if(obj.record.get('isNewRecord') == "total") {
			return false;
		}
	});
	// 获取物料编码，申请数量，费用来源编辑状态
	function checkStatus() {
		var status = {};
		var isAdd = false;
		// 新规
		if (issueNo.getValue() == null || issueNo.getValue() == "") {
			isAdd = true;
		}
		// 是否上报
		var isReported = false;
		if (!isAdd
				&& (headRecord.get('issueStatus') == "1" || headRecord
						.get('issueStatus') == '2')) {
			isReported = true;
		}
		// 是否按计划单生成
		var isPlan = false;
		if (mrNo.getValue()) {
			isPlan = true;
		}
		// 成本分摊项目
		if (isReported) {
			// 不可编辑
			status["costItemId"] = 1;
		}
		// 按计划生成或已上报不可修改
		if (isPlan || isReported) {
			status["itemId"] = 1;
			status["appliedCount"] = 1;
		}
		var isAdd = false;
		// 新规
		if (issueNo.getValue() == null || issueNo.getValue() == "") {
			isAdd = true;
		}
		// 已上报
		if (!isAdd
				&& (headRecord.get('issueStatus') == "1" || headRecord
						.get('issueStatus') == "2")) {
			status['materialNo'] = 1
		} else {
			// 不是按计划单生成
			if (mrNo.getValue() == null || mrNo.getValue() == "") {
			} else {
				status['materialNo'] = 1;
			}
		}
		return status;
	}

	detailGrid.on('cellclick', function(grid, rowIndex, columnIndex, e) {
		var record = grid.getStore().getAt(rowIndex);
		var fieldName = grid.getColumnModel().getDataIndex(columnIndex);
		if (fieldName == "materialNo") {
			var isAdd = false;
			// 新规
			if (issueNo.getValue() == null || issueNo.getValue() == "") {
				isAdd = true;
			}
			// 已上报
			if (!isAdd
					&& (headRecord.get('issueStatus') == "1" || headRecord
							.get('issueStatus') == "2")) {
				return;
			}
			// 不是按计划单生成
			if ((mrNo.getValue() == null || mrNo.getValue() == "")&& record.get('isNewRecord') != 'total') { // add by ywliu 090723
				var obj = selectMaterial();
				if (obj != null) {
					// 物料名称
					record.set('materialNo', obj.materialNo);
					// 物料id
					record.set('materialId', obj.materialId);
					// 物料名称
					record.set('materialName', obj.materialName);
					// 规格型号
					record.set('specNo', obj.specNo);
					// 材质/参数
					record.set('parameter', obj.parameter);
					// 存货计量单位id
					record.set('unitName', obj.stockUmId);
					// 当前库存
//					record.set('stock', obj.stockNum);//add by ywliu 20091019
					// 可用库存
					record.set('stock', obj.canUseStock);//add by liuyi 20100504
				}
			}
		}
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
				items : [projectCode,txtMrOriginal]
			},{
				columnWidth : 0.3,
				layout : "form",
				border : false,
				height : 26,
				items : [txtProjectCodeShow,txtMrOriginalH]
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
		},
		{ // ,txtCheckPriceRate,txtFactItemRate
			layout : "column",
			labelWidth:140,
			border : false,
			items : [{
				columnWidth : 0.3,
				layout : "form",
				height : 26,
				border : false,
				items : [txtAdviceItem]
			},{
				columnWidth : 0.3,
				layout : "form",
				height : 26,
				border : false,
				items : [txtFactItem]
			},{
				columnWidth : 0.3,
				layout : "form",
				height : 26,
				border : false,
				items : [txtCheckPrice]
			}]
		},
		{ // ,,
			layout : "column",
			labelWidth:190,
			labelAlign : 'right',
			border : false,
			items : [{
				columnWidth : 0.55,
				layout : "form",
				height : 26,
				border : false,
				items : [txtCheckPriceRate]
			},{
				columnWidth : 0.4,
				layout : "form",
				height : 26,
				border : false,
				items : [txtFactItemRate]
			}]
		},
			{
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.9,
				layout : "form",
				height : 49,
				border : false,
				items : [memo]
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
				items : [costItemId, hdnCostItemId]
			}]
		}]
	});
	var formPanel = new Ext.FormPanel({
		border : false,
		region : "north",
		width : 650,
		height : 200,
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
		tbar : [btnByPlan, "-", btnAddIssue, "-", btnDeleteIssue, "-",
				btnSaveIssue, "-", btnReport, "-", btnPrint]
	})

	// 供应商查询 Panel
	var tabQuery = new Ext.Panel({
		title : '查询',
		layout : 'border',
		border : false,
		items : [headTbar, queryGrid]
	})
	// 供应商查询 Panel
	var tabRegister = new Ext.Panel({
		title : '登记',
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
		items : [tabQuery, tabRegister]
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
	// ↓↓*******************需求计划申请单窗口*****************
	
	var txtApplyMan=new Ext.form.TextField({
	id:'txtApplyMan',
	name:'txtApplyMan'
	});
	// 确认按钮
	var btnOk = new Ext.Button({
		text : "确认",
		handler : planWinDblClickHandler
	});
	var planStore = new Ext.data.JsonStore({
		url : "resource/getPlanRequirementHeadDatas.action",
		root : 'list',
		totalProperty : 'totalCount',
		fields : [
				// 申请单ID
				{
					name : 'requimentHeadId'
				},
				// 申请单编号
				{
					name : 'mrNo'
				},
				// 工单编号
				{
					name : 'woNo'
				},
				// 费用来源
				{
					name : 'itemIdHead'
				},
				// 申请人
				{
					name : 'mrBy'
				},
				// 申请部门
				{
					name : 'mrDept'
				},
				// 需求日期
				{
					name : 'dueDate'
				},
				// 归口专业
				{
					name : 'costSpecial'
				},
				// 归口部门
				{
					name : 'costDept'
				},
				// 申请单ID
				{
					name : 'requimentHeadId'
				}, {
					name : 'planOriginalId'
				},{name:'mrReason'},
				{name:'mrByName'},
				{name:'mrDeptName'},
				{name:'costSpecialName'},
				{name:'costDeptName'},
				{name:'projectNo'}
				]
	});
	
	var planSm=new Ext.grid.CheckboxSelectionModel({singleSelect : true});
	var gridPlan = new Ext.grid.GridPanel({
			region : "north",
		border : false,
		autoScroll : true,
		sm : planSm,
		//tbar : [btnOk],
		store : planStore,
		height:300,
		columns : [planSm,new Ext.grid.RowNumberer({
			header : "行号",
			width : 35
		}),
				// 需求计划单申请编号
				{
					header : "需求计划单申请编号",
					sortable : true,
					width : 100,
					dataIndex : 'mrNo'
				},{
					header : "申请人",
					sortable : true,
					dataIndex : 'mrByName'
				},{
					header : "申请部门",
					sortable : true,
					dataIndex : 'mrDeptName'
				},{
					header : "归口专业",
					sortable : true,
					dataIndex : 'costSpecialName'
				},{
					header : "归口部门",
					sortable : true,
					dataIndex : 'costDeptName'
				},{
					header : "申请原因",
					sortable : true,
					dataIndex : 'mrReason'
				},{
					header : "需求日期",
					sortable : true,
					dataIndex : 'dueDate'
				},{
					header : "工单编号",
					sortable : true,
					dataIndex : 'woNo'
				},{
				  header : "项目编号",
					sortable : true,
					dataIndex : 'projectNo'
				}],
		enableColumnMove : false,
		tbar:['申请人',txtApplyMan,
		{
			text:'查询',
			iconCls:'query',
				handler:function(){ 
					planStore.load({
						params:{
						   applyBy:txtApplyMan.getValue(),
						   start : 0,  // add by ywliu 20091023
						   limit : Constants.PAGE_SIZE // add by ywliu 20091023
						}
		
					});
					materialStore.removeAll();
				}
		},btnOk
		],
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : Constants.PAGE_SIZE,
			store : planStore,
			displayInfo : true,
			displayMsg : '共{2}条',
			emptyMsg : Constants.EMPTY_MSG
		})
	});
	//gridPlan.on('rowdblclick', planWinDblClickHandler);
	
	//add by fyyang 090512-----------------------
	/**
	 * 数字格式化
	 */
	function numberFormat(v) {
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
		return v;
	}
	
		/**
	 * 金钱格式化
	 */
	function moneyFormat(v) {
		v = (Math.round((v - 0) * 10000)) / 10000;
		v = (v == Math.floor(v))
				? v + ".0000"
				: ((v * 10 == Math.floor(v * 10))
						? v + "000"
						: ((v * 100 == Math.floor(v * 100)) ? v + "00" : ((v
								* 1000 == Math.floor(v * 1000)) ? v + "0" : v)));
		v = String(v);
		var ps = v.split('.');
		var whole = ps[0];
		var sub = ps[1] ? '.' + ps[1] : '.0000';
		var r = /(\d+)(\d{3})/;
		while (r.test(whole)) {
			whole = whole.replace(r, '$1' + ',' + '$2');
		}
		v = whole + sub;
		if (v.charAt(0) == '-') {
			return '-' + v.substr(1);
		}
		return v;
	}
	// 物料详细记录
	var material = Ext.data.Record.create([{
				name : 'requirementDetailId'
			}, {
				name : 'materialNo'
			}, {
				name : 'materialName'
			}, {
				name : 'materSize'
			}, {
				name : 'appliedQty'
			}, {
				name : 'approvedQty'
			}, {
				name : 'issQty'
			}, {
				name : 'estimatedPrice'
			}, {
				name : 'estimatedSum'
			}, {
				name : 'purQty'
			}, {
				name : 'stockUmName'
			}, {
				name : 'usage'
			}, {
				name : 'memo'
			}, {
				name : 'needDate'
			}, {
				name : 'parameter'
			}, {
				name : 'docNo'
			}, {
				name : 'whsName'
			}, {
				name : 'qualityClass'
			}, {
				name : 'left'
			}, {
				name : 'tempNum'
			}, {
				name : 'itemId'
			}]);
	// 物料grid的store
	var materialStore = new Ext.data.JsonStore({
				url : 'resource/getMaterialDetailForIssueSelect.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : material
			});
	// 物料grid
			var detailSm=new Ext.grid.CheckboxSelectionModel();
	var materialGrid = new Ext.grid.GridPanel({
				// layout : 'fit',
			//	height : 200,
				// anchor : "100%",
				region : "center",
				border : false,
				autoScroll : true,
				enableColumnMove : false,
				// 单选
				sm : detailSm,
				// 单击修改
				store : materialStore,
				columns : [
				detailSm,
				new Ext.grid.RowNumberer({
									header : "行号",
									width : 35
								}),
						// 项次号
						{
							header : "项次号",
							width : 100,
							sortable : true,
							dataIndex : 'requirementDetailId'
						},
						// 物料编码
						{
							header : "物料编码",
							width : 100,
							sortable : true,
							dataIndex : 'materialNo'
						},// 物料名称
						{
							header : "物料名称",
							width : 100,
							sortable : true,
							dataIndex : 'materialName'
						},// 物料规格
						{
							header : "物料规格",
							width : 100,
							sortable : true,
							dataIndex : 'materSize'
						},// 申请数量
						{
							header : "申请数量",
							width : 100,
							sortable : true,
							align : 'right',
							renderer : numberFormat,
							dataIndex : 'appliedQty'
						},// 核准数量
						{
							header : "核准数量",
							width : 100,
							sortable : true,
							align : 'right',
							renderer : numberFormat,
							dataIndex : 'approvedQty'
						},// 已领数量
						{
							header : "已领数量",
							width : 100,
							sortable : true,
							align : 'right',
							renderer : numberFormat,
							dataIndex : 'issQty'
						},// 估计单价
						{
							header : "估计单价",
							width : 100,
							sortable : true,
							align : 'right',
							renderer : moneyFormat,
							dataIndex : 'estimatedPrice'
						},// 估计金额
						{
							header : "估计金额",
							width : 100,
							sortable : true,
							align : 'right',
							renderer : moneyFormat,
							dataIndex : 'estimatedSum'
						},
						// 采购数量
						{
							header : "采购数量",
							width : 100,
							sortable : true,
							align : 'right',
							renderer : numberFormat,
							dataIndex : 'purQty'
						},
						// 单位
						{
							header : "单位",
							width : 100,
							sortable : true,
							dataIndex : 'stockUmName'
						},
						// 需求日期
						{
							header : "需求日期",
							width : 100,
							sortable : true,
							dataIndex : 'needDate'
						},

						// 物料材质
						{
							header : "物料材质",
							width : 100,
							sortable : true,
							dataIndex : 'parameter'
						},
						// 物料图号
						{
							header : "物料图号",
							width : 100,
							sortable : true,
							dataIndex : 'docNo'
						},
						// 仓库
						{
							header : "仓库",
							width : 100,
							sortable : true,
							dataIndex : 'whsName'
						},// 质量等级
						{
							header : "质量等级",
							width : 100,
							sortable : true,
							dataIndex : 'qualityClass'
						},
						// 当前库存
						{
							header : "当前库存",
							width : 100,
							sortable : true,
							align : 'right',
							renderer : numberFormat,
							dataIndex : 'left'
						},
						// 暂收数量
						{
							header : "暂收数量",
							width : 100,
							sortable : true,
							align : 'right',
							renderer : numberFormat,
							dataIndex : 'tempNum'
						},
						// 费用来源
						{
							header : "费用来源",
							width : 100,
							sortable : true,
							renderer :function(val){
	                        if(val == "1"){
		                            return "费用来源1";
		                        }else if(val =="2"){
		                            return "费用来源2";
		                        }else if(val == "3"){
		                            return "费用来源3";
		                        }else if(val == "4"){
		                            return "费用来源4";
		                        }else {
		                            return "";
		                        }
		                    },
							dataIndex : 'itemId'
						},
						// 用途
						{
							header : "用途",
							width : 100,
							sortable : true,
							dataIndex : 'usage'
						},
						// 备注
						{
							header : "备注",
							width : 100,
							sortable : true,
							dataIndex : 'memo'
						}]
			});
			
				gridPlan.on('rowclick', showDetail);
					/**
	 * 当单击主grid中一行时，下面的grid显示详细信息
	 */
	function showDetail() {
		
		if (gridPlan.getSelectionModel().getSelected() != null) {
			var record = gridPlan.getSelectionModel().getSelected();
		
			materialStore.load({
						params : {
							headId : record.get('requimentHeadId')
						}
					});
		}

	}
	//---------------------------------------------------
	// 弹出画面
	var winPlan = new Ext.Window({
		height : 550,
		width : 700,
		layout : 'border',
		modal : true,
		resizable : false,
		closeAction : 'hide',
		items : [gridPlan,materialGrid],
		title : '需求计划申请单列表'
	});
	winPlan.on('beforeshow', function() {
		// 载入
		planStore.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			}
		});
		materialStore.removeAll();
	});
	// ↑↑*******************需求计划申请单窗口*****************
	initReceiptBy();
	// 初始化按钮设置
	setEditable(1);
	// ↓↓**************************处理************************
	
	function initPage(){// add by ywliu 090724 增加统计行
		tabPanel.setActiveTab(1);
		detailStore.load({
			params : {
				//start : 0,
				//limit : Constants.PAGE_SIZE,
				flag : 1
			},
			callback : addLine
		})
	}
	
	/**
	 * 画面初始值
	 */
	function initReceiptBy() {
		// 画面初始值设置
		Ext.lib.Ajax.request('POST', 'resource/getInitUserCode.action', {
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result) {
					hdnReceiptBy.setValue(result.workerCode);
					receiptBy.setValue(result.workerName);
					formPanel.fireEvent('doInit');
					hdnReceiptBy.fireEvent('valueChanged', true);
				}

			}
		});
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
//	function IssueStatusValue(value) {
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
//	}

	function ShowIssueHeadRegister() {
		if (queryGrid.selModel.hasSelection()) {
			var record = queryGrid.getSelectionModel().getSelected();
			if (isRegisterTabChanged()) {
				// 跳到登记tab
				tabPanel.setActiveTab(1);
				Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_004,
						function(buttonobj) {
							if (buttonobj == "yes") {
								showRegisterTab(record);
							}
						});
			}else{
				checkdueDate();
			}
//			else {
//				showRegisterTab(record);
//			}
		} else {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
		}
	}
	
	// check申请领用日期是否小于当前日期 
	function checkdueDate() {
		var record = queryGrid.getSelectionModel().getSelected();
		var now = new Date();
		var nowdate = ChangeDateToString(now);
		 var duedate = record.get('dueDate');
		 // modified by liuyi  091209 不对时间进行判断
//		if (duedate < nowdate) 
//			{
//				Ext.Msg.alert('提示','申请领用日期已过，不能对其进行操作！');
//				return false;
//			}
//			else {
				
				showRegisterTab(record);
//			}

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
		// 保存record
		headRecord = record;
		// 费用来源
		if (record.get('itemCode') == null) {
			record.set('itemCode', "");
		} else {
//			if (record.get('itemCode') == "zzfy") {
//				itemId.setValue("生产成本");
//			} else if (record.get('itemCode') == "lwcb") {
//				itemId.setValue("劳务成本");
//			}
			itemId.setValue(getBudgetName(record.get('itemCode')));

			txtAdviceItem.setValue(getBudgetName(record.get('itemCode'),1,hdnReceiptDep.getValue()));
			//txtFactItem.setValue(getBudgetName(record.get('itemCode'),2,hdnReceiptDep.getValue()));
			
					//--------add by fyyang 20100419---------------
			var data=getBudgetName(record.get('itemCode'),3,hdnReceiptDep.getValue());
			var mydata=data.split(",");
			txtFactItem.setValue(mydata[0]);
			txtCheckPrice.setValue(mydata[1]);
			var data1=( parseFloat(mydata[1])/parseFloat(txtAdviceItem.getValue()))*100;
			var data2=(parseFloat(mydata[0])/parseFloat(txtAdviceItem.getValue()))*100;
			txtCheckPriceRate.setValue(parseFloat(txtAdviceItem.getValue())==0? 0:  numberFormat(data1)) ;
            txtFactItemRate.setValue(parseFloat(txtAdviceItem.getValue())==0? 0: numberFormat(data2));
			//-------------------------------------------------------
		}
		
		// 成本分摊项目
		if (record.get('costItemId') == null) {
			record.set('costItemId', "");
		}
		// 费用归口专业
		if (record.get('feeBySpecial') == null) {
			record.set('feeBySpecial', "");
		}
		// 单据状态
		var status = record.get('issueStatus');
		// 需求计划单编号
		var mrNo = record.get('mrNo');
		// 已上报
		if (status == "1" || status == "2") {
			setEditable(3);
		}// 未上报且按计划单生成
		else if ((status == "0" || status == '9')
				&& (mrNo != null && mrNo.length > 0)) {
			setEditable(2);
		}// 未上报且不是按计划单生成
		else {
			setEditable(4);
		}
		formPanel.getForm().trackResetOnLoad = true;
		formPanel.getForm().loadRecord(record);
		//add by fyyang 090603 --------------headInfo.projectCode  txtProjectCodeShow
	
		if(record.data['projectCode']!="")
		{
		getPrjShow(record.data['projectCode']);
		}
		//----add end-------------------------
		//modify by fyyang 090618
		if (record.data['planOriginalId'] != "")
		{
		txtMrOriginal.setValue(getBillNameById(record.data['planOriginalId']));
		txtMrOriginalH.setValue(record.data['planOriginalId']);
		}
//		if (record.data['planOriginalId'] != "") {
//			Ext.Ajax.request({
//				params : {
//					planOrigianlId : record.data['planOriginalId']
//				},
//				url : 'resource/getOrigianlName.action',
//				method : 'post',
//				success : function(result) {
//					var recordSub = eval('(' + result.responseText + ')');
//					var obj = new Object();
//					if (recordSub.data != null && recordSub.data != "") {
//						obj.text = recordSub.data['planOriginalDesc'];
//						obj.id = parseInt(record.data['planOriginalId']);
//						txtMrOriginal.setValue(obj);
//						if (record.get('mrNo') == null
//								|| record.get('mrNo') == 'null'
//								|| record.get('mrNo') == "")// 如果不是按计划单生成，增加对归口专业的控制
//						{
//							if (recordSub.data['originalType'] == 'M') {
//								feeBySpecial.allowBlank = false;
//								//feeBySpecial.enable();
//								feeBySpecial.disable();
//							} else if (recordSub.data['originalType'] == 'O') {
//								feeBySpecial.setValue("");
//								feeBySpecial.clearInvalid();
//								feeBySpecial.disable();
//							}
//						}
//					}
//				},
//				failure : function(action) {
//				}
//
//			});
//		};
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
				flag : hdnDetailFlag.getValue()
//				start : 0,
//				limit : Constants.PAGE_SIZE
				
			},
			callback : addLine// add by ywliu 090724 增加统计行
		});
		entryId = record.get('workFlowNo');
		orginalId = record.get('planOriginalId');
		// 保存载入的数据，用于更新时check数据有无变更
		detailStore.on('load', function(store, records, options) {
			detailRecords = cloneDetailsFields(detailStore);
			// 判断是不是新记录
			// 从物资需求表新增
			if (hdnDetailFlag.getValue() == "2") {
				for (var i = 0; i < detailStore.getCount()-1; i++) {
					detailStore.getAt(i).set('isNew', true);
				}
			}
			// 判断可不可以增加删除明细
			var isAdd = false;
			// 新规
			if (issueNo.getValue() == null || issueNo.getValue() == "") {
				isAdd = true;
			}
			// 按计划
			if (Ext.getCmp('mrNo').getValue()) {
				return;
			}
			// 已上报
			if (!isAdd
					&& (headRecord.get('issueStatus') == "1" || headRecord
							.get('issueStatus') == "2")) {
				return;
			}
			if (detailStore.getCount() > 0) {
				deleteDetailBtn.setDisabled(false);
			} else {
				deleteDetailBtn.setDisabled(true);
			}
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
	 * 新增领料单按钮处理
	 */
	function addIssueHandler() {
		// 判断明细部分记录有无变化
		if (isRegisterTabChanged()) {
			Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_004,
					function(buttonobj) {
						if (buttonobj == "yes") {
							initAllDatas();
						}
					});
		} else {
			// 初始化
			initAllDatas();
		}
	}
	/**
	 * 删除领料单按钮处理
	 */
	function deleteIssueHandler() {
		// 弹出删除确认信息
		Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_002,
				function(buttonobj) {
					if (buttonobj == "yes") {
						// 已经上报过的，不可以删除
						if (isIssueReported()) {
							return;
						}
						// 删除
						Ext.lib.Ajax.request('POST',
								'resource/deleteIssueRecords.action', {
									success : function(action) {
										var result = eval("("
												+ action.responseText + ")");
										if (result.success) {
											Ext.Msg.alert(
													Constants.SYS_REMIND_MSG,
													Constants.COM_I_005,
													function() {
														// 初始化
														initAllDatas();
														tabPanel
																.setActiveTab(0);
													});
										} else {
											Ext.Msg.alert(
													Constants.SYS_REMIND_MSG,
													result.msg);
										}
									},
									failure : function() {
										Ext.Msg.alert(Constants.SYS_REMIND_MSG,
												Constants.COM_E_014);
									}
								}, 'issueHeadId='
										+ headRecord.get('issueHeadId')
										+ "&lastModifiedDate="
										+ headRecord.get('lastModifiedDate'));
					}
				});
	}
	/**
	 * 判断领料单是否已经上报
	 */
	function isIssueReported() {
		// 新增时返回false
		if (!issueNo.getValue()) {
			return false;
		}
		// 判断领料单是否已经上报
		if (headRecord.get('issueStatus') == "1"
				|| headRecord.get('issueStatus') == '2') {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.RS012_E_001);
			return true;
		}
		return false;
	}
	/**
	 * 数字格式化
	 */
	function numRenderer(v) {
//		if (value === "" || value == null) {
//			return value
//		}
//		value = String(value);
//		// 整数部分
//		var whole = value;
//		// 小数部分
//		var sub = ".00";
//		// 如果有小数
//		if (value.indexOf(".") > 0) {
//			whole = value.substring(0, value.indexOf("."));
//			sub = value.substring(value.indexOf("."), value.length);
//			sub = sub + "00";
//			if (sub.length > 3) {
//				sub = sub.substring(0, 3);
//			}
//		}
//		var r = /(\d+)(\d{3})/;
//		while (r.test(whole)) {
//			whole = whole.replace(r, '$1' + ',' + '$2');
//		}
//		v = whole + sub;
//		return v;
		if (v == null || v == "") {
			return "0.0000";
		}
		v = (Math.round((v - 0) * 10000)) / 10000;
		v = (v == Math.floor(v))
				? v + ".0000"
				: ((v * 10 == Math.floor(v * 10))
						? v + "000"
						: ((v * 100 == Math.floor(v * 100)) ? v + "00" : ((v
								* 1000 == Math.floor(v * 1000)) ? v + "0" : v)));
		v = String(v);
		var ps = v.split('.');
		var whole = ps[0];
		var sub = ps[1] ? '.' + ps[1] : '.0000';
		var r = /(\d+)(\d{3})/;
		while (r.test(whole)) {
			whole = whole.replace(r, '$1' + ',' + '$2');
		}
		v = whole + sub;
		if (v.charAt(0) == '-') {
			return '-' + v.substr(1);
		}
		return v;
	}
	/**
	 * 需求计划主表弹出窗口记录双击处理
	 */
	function planWinDblClickHandler() {
		// 有选择
		if (gridPlan.selModel.hasSelection()) {
			//---add by fyyang 20100408-------------------
			
			var sm = materialGrid.getSelectionModel();
		     var selected = sm.getSelections();
		     if (selected.length == 0) {
			   Ext.Msg.alert("提示", "请选择明细记录！");
			   return;
		     }
		     var ids = [];
		     	for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.requirementDetailId) {
					ids.push(member.requirementDetailId); 
				}
			}
			
		    detailIds=ids.join(",");
		   
			//------------------------------------------------
			// 数据初始化
			initAllDatas(1); 
			var record = gridPlan.getSelectionModel().getSelected();
			
			// 工单编号
			woNo.setValue(record.get("woNo"));
			// 需求计划单编号
			mrNo.setValue(record.get("mrNo"));
			//-------- 项目编号 add by fyyang 20100507------------------
			projectCode.setValue(record.get("projectNo"));
			if(record.get("projectNo")!=null&&record.get("projectNo")!="")
			{
			getPrjShow(record.get("projectNo"));
			
			Ext.getCmp('planOriginalId').setValue("2");
			Ext.form.ComboBox.superclass.setValue.call(Ext
							.getCmp('planOriginalId'), "专项物资类");
			txtMrOriginalH.setValue("2");
			}
			//------------------------------------------------------------				
			// 费用来源
			hdnItemId.setValue(record.get("itemIdHead"));

			itemId.setValue(getBudgetName(record.get("itemIdHead")));
		
			txtAdviceItem.setValue(getBudgetName(record.get('itemCode'),1,hdnReceiptDep.getValue()));
		//	txtFactItem.setValue(getBudgetName(record.get('itemCode'),2,hdnReceiptDep.getValue()));
			
					//--------add by fyyang 20100419---------------
			var data=getBudgetName(record.get('itemCode'),3,hdnReceiptDep.getValue());
			var mydata=data.split(",");
			txtFactItem.setValue(mydata[0]);
			txtCheckPrice.setValue(mydata[1]);
			var data1=( parseFloat(mydata[1])/parseFloat(txtAdviceItem.getValue()))*100;
			var data2=(parseFloat(mydata[0])/parseFloat(txtAdviceItem.getValue()))*100;
			txtCheckPriceRate.setValue(parseFloat(txtAdviceItem.getValue())==0? 0:  data1) ;
            txtFactItemRate.setValue(parseFloat(txtAdviceItem.getValue())==0? 0: data2);
			//-------------------------------------------------------
//			itemId.setValue(record.get("itemIdHead"));
			if (itemId.getValue() == null) {
				itemId.setValue("");
			}
			// 申请领用人
			hdnReceiptBy.setValue(record.get("mrBy"));
			Ext.lib.Ajax.request('POST', 'resource/getReceiptByName.action', {
				success : function(action) {
					receiptBy.setValue(action.responseText);
				},
				faliure : function(action) {
				}
			}, "receiptBy=" + hdnReceiptBy.getValue());
			// 部门联动
			hdnReceiptBy.fireEvent('valueChanged');
			// 领用部门
			hdnReceiptDep.setValue(record.get("mrDept"));
			// 申请领用日期
			dueDate.setValue(record.get("dueDate"));
			// 费用归口部门
			hdnFeeByDep.setValue(record.get("costDept"));
			// 单据状态->'审批结束'
			hdnIssueStatus.setValue('0');
			// 上报不可用
			btnReport.setDisabled(false);
			// 归口专业
			feeBySpecial.setValue(record.get("costSpecial") ? record
					.get("costSpecial") : "");
			hdnFeeBySpecial.setValue(feeBySpecial.getValue());
			if (feeBySpecial.getValue() == null) {
				feeBySpecial.setValue("");
			}
			findEmpName(hdnReceiptBy.getValue());
			// 明细查询
			// 明细标记：'2'->从物料需求计划明细数据
			hdnDetailFlag.setValue('2');
			// 申请单id
			hdnRequimentHeadId.setValue(record.get('requimentHeadId'));
		    

			
			detailStore.load({
				// modified by liuyi 20100504 
//				callback : addLine // add by ywliu 090724 增加统计行
				callback : function() {
							detailStore.each(function(re) {
										re.set("stock", re.get('appliedCount'))
									})
							addLine()
						}
			});
			setEditable(2);
			// 删除按钮不可用
			btnDeleteIssue.setDisabled(true);
			winPlan.hide();
			
		}
		else
		{
		   Ext.Msg.alert("提示", "请选择明细记录！");	
		}
	}
	/**
	 * 按计划单生成按钮处理
	 */
	function addByPlanHandler() {
		// 判断明细部分记录有无变化
		if (isRegisterTabChanged()) {
			Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_004,
					function(buttonobj) {
						if (buttonobj == "yes") {
							// 显示需求计划window
							showPlanWindow();
						}
					});
		} else {
			// 显示需求计划window
			showPlanWindow();
		}
	}
	/**
	 * 保存操作处理
	 */
	function saveIssueHandler() {
		// 画面没有变化
		if (!isRegisterTabChanged()) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_006);
			return;
		}
		// 是否确认保存
		Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_001,
				function(buttonobj) {
					if (buttonobj == "yes") {
						// 判断是否已经上报
						if (isIssueReported()) {
							return;
						} else {
							if (checkBeforeSave()) {
								saveDatas(true);
							}
						}
					}
				});
	}
	
//add by fyyang 091119----------start---------------------------------------	
		/**
	 * 检测物料明细是否有重复
	 */
	function isMaterialRepeat() {
		var msg = "";
		for (var i = 0; i < detailStore.getCount() - 1; i++) {
			for (var j = i + 1; j < detailStore.getCount() - 1; j++) {
				if (detailStore.getAt(i).get('materialId') == detailStore
						.getAt(j).get('materialId')
						&& detailStore.getAt(i).get('materialId') != ""
						&& detailStore.getAt(j).get('materialId') != "") {
					msg = Constants.RP002_E_002 + "<br />";
				}
			}
		}
		return msg;
	}
	
	//-----------------------------end -----------------------------------------
	/**
	 * 保存前的check处理
	 */
	function checkBeforeSave() {
		// 备注无效返回
		if (!memo.isValid()) {
			return;
		}
		// 申请领用日期check
		var now = new Date();
		var today = new Date(now.getYear(), now.getMonth(), now.getDate());
		// modified by liuyi 091209 申请领用日期不做检查
//		if (dueDate.getValue() != null && !mrNo.getValue()) {
//			var dueDay = new Date(dueDate.getValue().replace(/-/g, "/"));
//			if (today.getTime() - dueDay.getTime() > 0) {
//				Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(
//						Constants.COM_E_004, '申请领用日期'));
//				return false;
//			}
//		}
		//add by fyyang 091119--------
		 var txtMsg=isMaterialRepeat();
		 if(txtMsg!="")
		 {
		 	Ext.Msg.alert(Constants.SYS_REMIND_MSG, txtMsg);
			return false;
		 }
		//----------------------------
		
		// 物料编码和申请数量必须输入check
		var flagM = false;
		var flagA = false;
		for (var i = 0; i < detailStore.getCount()-1; i++) {// 统计行不用check modify by ywliu 090723
			var record = detailStore.getAt(i);
			if (!record.get('materialNo')) {
				flagM = true;
			}
			if (!record.get('appliedCount') && record.get('appliedCount') !== 0) {
				flagA = true;
			}
			if (flagM && flagA) {
				break;
			}
		}
		var msg = "";
		if (!dueDate.getValue()) {
			msg += String.format(Constants.COM_E_003, "申请领用日期") + "<br/>";
		}
		if (flagM) {
			msg += String.format(Constants.COM_E_003, "物料编码") + "<br/>";
		}
		if (flagA) {
			msg += String.format(Constants.COM_E_002, "申请数量") + "<br/>";
		}
		if (msg) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, msg);
			return false;
		}
		// 画面有无明细check
		if (detailStore.getCount()-1 == 0) {// 统计行不用check modify by ywliu 090723
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_E_018);
			return false;
		}
		// 申请数量大于零check
		for (var i = 0; i < detailStore.getCount()-1; i++) {// 统计行不用check modify by ywliu 090723
			var record = detailStore.getAt(i);
			if (record.get('appliedCount') <= 0) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(
						Constants.COM_E_013, '申请数量'));
				return false;
			}
		}
		return true;
	}
	/**
	 * 保存处理
	 * 
	 * @param show
	 *            要不要显示成功信息
	 * @param callback
	 *            保存成功后的操作
	 */
	function saveDatas(show, callback) {
		//alert(txtMrOriginalH.value);
	//	Ext.get("headInfo.planOriginalId").dom.value=txtMrOriginal.value;
		if(txtMrOriginalH.getValue()==null||txtMrOriginalH.getValue()=="")
		{
		  Ext.Msg.alert("提示","单据种类不能为空，请选择！");	
		  return false;
		}
		// 如果领料单编号为空，db中插入
		if (issueNo.getValue() == null || issueNo.getValue() == "") {
			formPanel.getForm().submit({
				method : 'POST',
				url : 'resource/addIssueHeadAndDetails.action',
				success : function(form, action) {
					var result = eval("(" + action.response.responseText + ")");
					if (result.success) {
						if (show) {
							Ext.Msg.alert(Constants.SYS_REMIND_MSG,
									Constants.COM_I_004);
						}
						var issueid = result.msg;
						showSavedDatas(issueid);
						if (callback) {
							callback.apply(this);
						}
					} else {
						Ext.Msg.alert(Constants.SYS_REMIND_MSG, result.msg);
					}
				},
				failure : function(form, action) {
					var result = eval("(" + action.response.responseText + ")");
					if (result) {
						Ext.Msg.alert(Constants.SYS_REMIND_MSG, result.msg);
					} else {
						Ext.Msg.alert(Constants.SYS_REMIND_MSG,
								Constants.COM_E_014);
					}
				},
				params : {
					newDetails : Ext.util.JSON.encode(getAllDetailRecords()),
					'headInfo.dueDate' : dueDate.getValue()
				}
			})
		}// 领料单编号不为空
		else {
			// 领料单数据
			var params = "form="
					+ Ext.util.JSON.encode(formPanel.getForm().getValues());
			// 新增明细数据
			params += "&newDetails="
					+ Ext.util.JSON.encode(getDetailRecords(true));
			// 修改的明细记录
			params += "&updateDetails="
					+ Ext.util.JSON.encode(getDetailRecords(false));
			// 删除的明细记录
			params += "&deletedDetails=" + Ext.util.JSON.encode(deleteDetails);
			formPanel.getForm().submit({
				method : 'POST',
				url : 'resource/updateIssueHeadAndDetails.action',
				success : function(form, action) {
					var result = eval("(" + action.response.responseText + ")");
					if (result.success) {
						if (show) {
							Ext.Msg.alert(Constants.SYS_REMIND_MSG,
									Constants.COM_I_004);
						}
						showSavedDatas(hdnIssueHeadId.getValue());
						if (callback) {
							callback.apply(this);
						}
					} else {
						Ext.Msg.alert(Constants.SYS_REMIND_MSG, result.msg);
					}
				},
				failure : function(form, action) {
					var result = eval("(" + action.response.responseText + ")");
					if (result) {
						Ext.Msg.alert(Constants.SYS_REMIND_MSG, result.msg);
					} else {
						Ext.Msg.alert(Constants.SYS_REMIND_MSG,
								Constants.COM_E_014);
					}
				},
				params : {
					newDetails : Ext.util.JSON.encode(getDetailRecords(true)),
					updateDetails : Ext.util.JSON
							.encode(getDetailRecords(false)),
					deletedDetails : Ext.util.JSON.encode(deleteDetails),
					'headInfo.dueDate' : dueDate.getValue()
				}
			})

		}
	}
	// TODO
	function showSavedDatas(issueHeadId) {
		// 重新读取数据
		queryStore.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			},
			callback : addLine
		});
		// 刷新登记页面数据
		var url = "resource/getIssueHeadById.action?issueHeadId=" + issueHeadId;
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("POST", url, false);
		conn.send(null);
		var head = Ext.util.JSON.decode("(" + conn.responseText + ")");
		var record = new queryRecord({
			// 领料单ID
			issueHeadId : head.issueHeadId,
			// 单据状态
			issueStatus : head.issueStatus,
			// 领料单编号
			issueNo : head.issueNo,
			// 项目编号
			projectCode : head.projectCode,
			// 工单编号
			woNo : head.woNo,
			// 成本分摊项目编码
			costItemId : head.costItemId,
			// 费用来源ID
			itemId : head.itemId,
			// 需求计划申请单编号
			mrNo : head.mrNo,
			// 申请领料人
			receiptBy : head.receiptBy,
			// 领用部门
			receiptDep : head.receiptDep,
			// 申请领用日期
			dueDate : head.dueDate,
			// 费用归口部门
			feeByDep : head.feeByDep,
			// 费用归口专业
			feeBySpecial : head.feeBySpecial,
			// 备注
			memo : head.memo,
			// 单据状态
			issueStatus : head.issueStatus,
			// 是否紧急领用
			isEmergency : head.isEmergency,
			// 上次修改日期
			lastModifiedDate : head.lastModifiedDate,

			workFlowNo : head.workFlowNo,
			planOriginalId : head.planOriginalId
		});
		// 显示
		showRegisterTab(record);
	}
	/**
	 * 显示需求计划申请单画面
	 */
	function showPlanWindow() {
		winPlan.show();
	}
	/**
	 * 判断画面有无变更
	 */
	function isRegisterTabChanged() {
		
		var isAdd = false;
		// 新规
		if (issueNo.getValue() == null || issueNo.getValue() == "") {
			isAdd = true;
		}
		var isPlan = false
//		// 按计划
//		if (mrNo.getValue()) {
//			isPlan = true;
//		}
//		if (isAdd && isPlan) {
		if (isAdd){
			return true
		}
		// 领料单表头数据变化
		if (formPanel.getForm().isDirty()) {
			return true;
		}
		
		// 明细部分有无变化
		if (isDetailRecordsChanged()) {
			return true;
		}
		return false;
	}
	/**
	 * 判断明细部分有无变化
	 */
	function isDetailRecordsChanged() {
		// 个数不同
		if (detailRecords.length != detailStore.getCount()-1) {// 将统计行从保存的数据中除去 modify by ywliu 090723
			return true;
		}
		// 有被删除的记录
		if (deleteDetails.length > 0) {
			return true;
		}
	
		for (var i = 0; i < detailStore.getCount()-1; i++) {// 将统计行从保存的数据中除去 modify by ywliu 090723
			
			var nowRecord = detailStore.getAt(i);
			var initRecord = getDetailsRecord(nowRecord.get('issueDetailsId'));
			//---add---------
			if(initRecord==null)
			{
			  return true;	
			}
			//------------------
			// 物料编码
			if (nowRecord.get('materialNo') != initRecord['materialNo']) {
				return true;
			}
			// 申请数量
			if (nowRecord.get('appliedCount') != initRecord['appliedCount']) {
				return true;
			}
			// 成本分摊项目
			if (nowRecord.get('costItemId') != initRecord['costItemId']) {
				return true;
			}
			// 费用来源
			if (nowRecord.get('itemId') != initRecord['itemId']) {
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
		
			
		for (var i = 0; i < detailRecords.length; i++) {// 将统计行从保存的数据中除去 modify by ywliu 090723
				if (detailRecords[i]['issueDetailsId'] == issueDetailsId)
				return detailRecords[i];
			}
			return null;
			
		
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
			data['materialNo'] = record.get('materialNo');
			data['appliedCount'] = record.get('appliedCount');
			data['costItemId'] = record.get('costItemId');
			data['itemId'] = record.get('itemId');
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
	function getDetailRecords(isNew) {
		var results = [];
		for (var i = 0; i < detailStore.getCount()-1; i++) { // 保存时将统计行除去 add by ywliu 090723
			var record = detailStore.getAt(i);
			if (isNew) {
				if (record.get('isNew')) {

					results.push(record.data);
				}
			} else {
				if (!record.get('isNew')) {
					results.push(record.data);
				}
			}
		}
		return results;
	}
	/**
	 * 获取明细grid中的数据,传回后台
	 */
	function getAllDetailRecords() {
		var results = [];
		for (var i = 0; i < detailStore.getCount()-1; i++) {// 保存时将统计行除去 add by ywliu 090723
			var record = detailStore.getAt(i);
			results.push(record.data);
		}
		return results;
	}
	/**
	 * 画面初期化
	 */
	function initAllDatas(isPlan) {
		entryId = "";// add by ywliu 20091023 
		if(isPlan != "1" ) { 
		// 列表tab重新载入
			queryStore.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				},
				callback : addLine // add by ywliu 加入统计行 090723
			});
		}
		// 清空保存的record
		headRecord = {};
		// 清空保存的明细数据
		detailRecords = [];
		// 清空保存的删除记录
		deleteDetails = [];
		// 登记tab初始化
		formPanel.getForm().reset();
		txtAdviceItem.setValue("");
		txtFactItem.setValue("");
			//--------add by fyyang 20100419---------------
			txtCheckPrice.setValue("");
			txtCheckPriceRate.setValue("") ;
            txtFactItemRate.setValue("");
			//-------------------------------------------------------
		
		itemId.setValue("请选择");// modify by ywliu 2009/7/6
	  //add by fyyang 090618
		txtMrOriginal.setValue("");
		txtMrOriginalH.setValue("");
		// 清空form
		clearForm();
		setEditable(1);
		detailStore.removeAll();
		detailStore.totalLength = 0;
		detailStore.fireEvent('countChanged');
		txtProjectCodeShow.setValue("");//add by fyyang 090603
		dueDate.setValue(snow); // add by drdu 090618
//		detailGrid.getBottomToolbar().updateInfo();
		hdnIssueStatus.setValue("");// add by ywliu 20091023
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
		initReceiptBy();
		// 紧急领用清除
		isEmergency.setValue(false);
		isEmergency.originalValue = false;
		formPanel.fireEvent('doInit');
	}
	/**
	 * 增加明细
	 */
	function addDetailRecord() {
		var msg = "";
		if(txtMrOriginalH.getValue()==null||txtMrOriginalH.getValue()=="")
		{
			msg += String.format(Constants.COM_E_003, "单据种类") + "<br />";
		 
		}
		if(itemId.getValue()==null||itemId.getValue()=="")
		{
			msg += String.format(Constants.COM_E_003, "费用来源") + "<br />";
		 
		}
		if(msg == "") {
		var record = new detailRecord({
			// 物料编码
			materialNo : "",
			// 申请数量
			appliedCount : "",
			// 核准数量
			approvedCount : 0,
			// 实际数量
			actIssuedCount : 0,
			// 待发货数量
			waitCount : 0,
			// 成本分摊项目编码
			costItemId : costItemId.getValue(),
			// 费用来源id
			itemId : hdnItemId.getValue(),
			isNew : true
		});
		// 原数据个数
		var count = detailStore.getCount();
		// 停止原来编辑
		detailGrid.stopEditing();
		// 插入新数据
		detailStore.insert(count-1, record); // ywliu 090723
		detailStore.fireEvent('countChanged');
		detailStore.totalLength = detailStore.getTotalCount() + 1;
		detailGrid.getView().refresh();
		} else {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, msg);
		}
//		detailGrid.getBottomToolbar().updateInfo();
	}
	/**
	 * 弹出物料选择窗口
	 */
	function selectMaterial() {
		var args = new Object();
		args.issue = true;
	     // modify by ywliu 20091019
		var mate = window.showModalDialog('../../../resource/storage/issueheadregister/RP001.jsp',
				args, 'dialogWidth=900px;dialogHeight=610px;status=no');// modify by ywliu 091019
		if (typeof(mate) != "undefined") {
			// 设置物料
			return mate;
		}
	}
	/**
	 * 删除明细
	 */
	function deleteDetailRecord() {
		if (detailGrid.selModel.hasSelection()) {
			var record = detailGrid.selModel.getSelected();
			if(record.get('isNewRecord') != 'total') {// 统计行不能删除 add by ywliu 090723
				// 如果选中一行则删除
				detailStore.remove(record);
				detailStore.fireEvent('countChanged');
				detailGrid.getView().refresh();
				detailStore.totalLength = detailStore.getTotalCount() - 1;
	//			detailGrid.getBottomToolbar().updateInfo();
				// 如果不是新增加的记录,保存删除的流水号
				if (!record.get('isNew')) {
					deleteDetails.push({
						'issueDetailsId' : record.get('issueDetailsId'),
						'lastModifiedDate' : record.get('lastModifiedDate')
					});
				}
			}
		} else {
			// 否则弹出提示信息
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);

		}
	}
	/**
	 * 上报按钮处理
	 */
	function reportBtnHandler() {
		// 如果画面数据未保存
		if (isRegisterTabChanged()) {
			// 是否确认保存
			Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_W_002,
					function(buttonobj) {
						if (buttonobj == "yes") {
							if (checkBeforeSave()) {
								// 保存操作
								saveDatas(false, reportIssueHead);
							}
						}
					});
		} else {
			if (!issueNo.getValue()) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_E_018)
				return;
			}
			// 上报操作
			reportIssueHead();
		}
	}
	/**
	 * 上报
	 */
	function reportIssueHead() {
		url = "../issueheadapprove/RS014.jsp?issueHeadId="
				+ hdnIssueHeadId.getValue() + "&entryId=" + entryId
				+ "&orginalId=" + orginalId;
		var o = window.showModalDialog(url, '',
				'dialogWidth=800px;dialogHeight=500px;status=no');
		if (o) {
			Ext.Msg.alert('提示', '上报成功！');
			initAllDatas();
		}

		// Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_006,
		// function(buttonobj) {
		// if (buttonobj == "yes") {
		// // 领料单数据
		// var params = "issueHeadId=" + hdnIssueHeadId.getValue()
		// + "&lastModifiedDate="
		// + hdnLastModifiedDate.getValue();
		// // 上报
		// Ext.lib.Ajax.request('POST',
		// 'resource/reportIssueHeadRecord.action', {
		// success : function(action) {
		// var result = eval("("
		// + action.responseText + ")");
		// if (!result.success) {
		// Ext.Msg.alert(
		// Constants.SYS_REMIND_MSG,
		// result.msg);
		// } else {
		// Ext.Msg.alert(
		// Constants.SYS_REMIND_MSG,
		// Constants.COM_I_007);
		// showSavedDatas(hdnIssueHeadId
		// .getValue());
		// }
		// },
		// faliure : function(action) {
		// Ext.Msg.alert(Constants.SYS_REMIND_MSG,
		// Constants.COM_E_014);
		// }
		// }, params);
		// }
		// });
	}
	/**
	 * 打印按钮处理
	 */
	function printBtnHandler() {
		// 如果画面数据未保存
		if (isRegisterTabChanged()) {
			// 是否确认保存
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_010);
		}
	}
	/**
	 * 人员选择画面处理
	 */
	function selectPersonWin() {
		if (receiptBy.disabled) {
			return;
		}
		var args = {
			selectModel : 'signal',
			notIn : "",
			rootNode : {
				id : '-1',
				text : '合肥电厂'
			}
		}
		var person = window
				.showModalDialog(
						'../../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(person) != "undefined") {
			receiptBy.setValue(person.workerName);
			hdnReceiptBy.setValue(person.workerCode);
			hdnReceiptDep.setValue(person.deptCode);
			hdnFeeByDep.setValue(person.deptCode);
			receiptDep.setValue(person.deptName);
			feeByDep.setValue(person.deptName)
		}
	}
	/**
	 * 控制是否可编辑
	 * 
	 * @param order :
	 *            1->初始化，<br/> 2->按计划单生成并且未上报的领料单，<br/> 3->已上报的领料单, <br/>
	 *            4->未上报且不是按计划单生成的领料单<br/>
	 */
	function setEditable(order) {
		// 按计划单生成
		btnByPlan.setDisabled(false);
		// 新增按钮
		btnAddIssue.setDisabled(false);
		// 增加明细button
		addDetailBtn.setDisabled(false);

		if (order == 1) {
			// 初始化时不可用
			// 删除按钮
			btnDeleteIssue.setDisabled(true);
			// 删除明细按钮不可用
			deleteDetailBtn.setDisabled(true);
			// 费用归口专业
			feeBySpecial.setDisabled(true); //add by drdu 090618
			// 上报按钮
			btnReport.setDisabled(false);
			// 保存按钮
			btnSaveIssue.setDisabled(false);
			// 打印按钮
			btnPrint.setDisabled(true);
		} else {
			// 保存按钮
			btnSaveIssue.setDisabled(false);
			// 删除按钮
			btnDeleteIssue.setDisabled(false);
			// 上报按钮
			btnReport.setDisabled(false);
			// 打印按钮
			btnPrint.setDisabled(false);
		}
		if (order == 2) {
			// 增加明细
			addDetailBtn.setDisabled(true);
			btnReport.setDisabled(false);
			// 删除明细
			deleteDetailBtn.setDisabled(true);
			// 费用来源
			itemId.setDisabled(true);
		
			txtProjectCodeShow.setDisabled(true);//add by fyyang 20100507
			
			// 申请领用人
			receiptBy.setDisabled(true);
			// 申请领用日期
			dueDate.setDisabled(true);
			// 费用归口专业
			feeBySpecial.setDisabled(true);
			// 紧急领用
			isEmergency.setDisabled(true);
			// 成本分摊项目
			costItemId.setDisabled(false);
			// 计划来源
			//modify by fyyang 090618
		//	txtMrOriginal.setDisabled(true);
		} else if (order == 3) {
			addDetailBtn.setDisabled(true);
			btnReport.setDisabled(true);
			deleteDetailBtn.setDisabled(true);
			itemId.setDisabled(true);
			txtProjectCodeShow.setDisabled(true);//add by fyyang 20100507
			receiptBy.setDisabled(true);
			dueDate.setDisabled(true);
			feeBySpecial.setDisabled(true);
			isEmergency.setDisabled(true);
			btnSaveIssue.setDisabled(true);
			btnDeleteIssue.setDisabled(true);
			costItemId.setDisabled(true);
			// 计划来源
			 txtMrOriginal.setDisabled(true);
			Ext.get('memo').dom.readOnly = true;
		} else {
			// 紧急领用
			isEmergency.setDisabled(false);
			// 成本分摊项目
			costItemId.setDisabled(false);
			// 费用来源
			itemId.setDisabled(false);
			txtProjectCodeShow.setDisabled(false);//add by fyyang 20100507
			// 申请领用人
			receiptBy.setDisabled(false);
			// 申请领用日期
			dueDate.setDisabled(false);
			// 费用归口部门
			feeByDep.setDisabled(false);
			// 费用归口专业  modify by drdu 090618
			//feeBySpecial.setDisabled(false);
			// 备注
			if (Ext.get('memo')) {
				Ext.get('memo').dom.readOnly = false;
			}
			// 计划来源
			 txtMrOriginal.setDisabled(false);
		}
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
	
	/**
	 * 添加统计行
	 */
	function addLine() {
		// 统计行
		var record = new material({
			materialId : "",
			materialName : "",
			materSize : "",
			parameter : "",
			stockUmName : "",
			appliedCount : "",
			approvedCount : "",
			actIssuedCount : "",
			isNewRecord : "total"
		});
		// 原数据个数
		var count = detailStore.getCount();
		// 停止原来编辑
		detailGrid.stopEditing();
		// 插入统计行
		detailStore.insert(count, record);
		detailGrid.getView().refresh();
		totalCount = detailStore.getCount() - 1;

	};
	
	
	function getPrjShow(prjNo)
	{
	
		Ext.Ajax.request({
				params : {
					prjNo : prjNo
				},
				url : 'manageproject/getProjectNoShow.action',
				method : 'post',
				success : function(result) {
					Ext.get("txtProjectCodeShow").dom.value=result.responseText;
					
				},
				failure : function(action) {
				}

			});
		
	}
	
	initPage();// add by ywliu 090724
});
