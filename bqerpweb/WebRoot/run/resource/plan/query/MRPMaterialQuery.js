Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
    // add by ywliu 20091112
	var queryType = "";
	var workCode = "";
	 
	Ext.override(Ext.grid.GridView, {
		ensureVisible : function(row, col, hscroll) {
			if (typeof row != "number") {
				row = row.rowIndex;
			}
			if (!this.ds) {
				return;
			}
			if (row < 0 || row >= this.ds.getCount()) {
				return;
			}
			col = (col !== undefined ? col : 0);
			var rowEl = this.getRow(row), cellEl;
			if (!(hscroll === false && col === 0)) {
				while (this.cm.isHidden(col)) {
					col++;
				}
				cellEl = this.getCell(row, col);
			}
			if (!rowEl) {
				return;
			}
			var c = this.scroller.dom;
			var ctop = 0;
			var p = rowEl, stop = this.el.dom;
			while (p && p != stop) {
				ctop += p.offsetTop;
				p = p.offsetParent;
			}
			ctop -= this.mainHd.dom.offsetHeight;
			var cbot = ctop + rowEl.offsetHeight;
			var ch = c.clientHeight;
			var stop = parseInt(c.scrollTop, 10);
			var sbot = stop + ch;
			if (ctop < stop) {
				c.scrollTop = ctop;
			} else if (cbot > sbot) {
				c.scrollTop = cbot - ch;
			}
			if (hscroll !== false) {
				var cleft = parseInt(cellEl.offsetLeft, 10);
				var cright = cleft + cellEl.offsetWidth;
				var sleft = parseInt(c.scrollLeft, 10);
				var sright = sleft + c.clientWidth;
				if (cleft < sleft) {
					c.scrollLeft = cleft;
				} else if (cright > sright) {
					c.scrollLeft = cright - c.clientWidth;
				}
			}
			return cellEl ? Ext.fly(cellEl).getXY() : [this.el.getX(),
					Ext.fly(rowEl).getY()];
		}
	});

	Ext.QuickTips.init();
	/**
	 * 金钱格式化
	 */
	function moneyFormat(v) {
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
	 * 数字格式化
	 */
	function numberFormat(v) {
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
		return v;
	}
	/**
	 * 获取当前月的日期
	 */
	function getCurrentDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		return s;
	}

	/**
	 * 获取当前前一月的日期
	 */
	function getCurrentDateFrom() {
		var d, s, t;
		d = new Date();
		d.setDate(d.getDate() - 10)
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		day = d.getDate();
		s += (day > 9 ? "" : "0") + day;
		return s;
	}
	/**
	 * 获取当前后一月的日期
	 */
	function getCurrentDateTo() {
		var d, s, t, day;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		day = d.getDate();
		s += (day > 9 ? "" : "0") + day;
		return s;
	}
	/**
	 * 去掉时间中T
	 */
	function renderDate(value) {
		if (!value)
			return "";

		var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
		var reTime = /\d{2}:\d{2}:\d{2}/gi;

		var strDate = value.match(reDate);
		var strTime = value.match(reTime);
		if (!strDate)
			return "";
		return strTime ? strDate : strDate;
	}
	/**
	 * 需求时间与当前时间比较
	 */
	function checkTime() {
		var startDate = txtMrDateFrom.getValue();
		var endDate = txtMrDateTo.getValue();
		if (startDate != "" && endDate != "") {
			var res = compareDateStr(startDate, endDate);
			if (res) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(
						Constants.COM_E_006, "计划月份（开始月份）", "至（终止月份）"));

			}
		}
		return true;
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
	 * 赋值主grid中物料类别
	 */
	function getClassName() {
		return txtMaterialClass.getRawValue();
	}

	/**
	 * 查询函数
	 */
	function findFuzzy(start) {
		if (start > 0) {
			start = start;
		} else {
			start = 0;
		}
		materialStore.baseParams = {
			queryType : queryType,// add by ywliu 20091113
			fromDate : timefromDate.getValue(),
			toDate : timetoDate.getValue(),
			materialNo : txtMaterialNo.getValue(),
			materialName : txtMaterialName.getValue(),
			specNo : txtSpecNo.getValue(),
			needPlanNo : needPlanNo.getValue(),
		    observer : workCode, // add by ywliu 20091113
			mrDept : txtMrDeptH.getValue(),// add by ywliu 20091112
			mrBy : txtMrByH.getValue(),
			status : stateComboBox.getValue(),// add by jling
			buyBy : txtBuyByH.getValue()
		}
		materialStore.load({
			params : {
				start : start,
				limit : Constants.PAGE_SIZE
			}
		});
	}
	/**
	 * 获取申请人的姓名和工号
	 */
	function getUserName(init) {
		//btnInquire.setVisible(false); //add by fyyang 100112
		if(init) {
			Ext.get("all").dom.parentNode.style.display="none";
			// add by ywliu 20091113
			Ext.Ajax.request({
				url : 'resource/getQueryRightByWorkId.action',
				method : 'post',
				params:{fileAddr:'run/resource/plan/query/MRPInformation.jsp'},
				success : function(action) {
		            var state=action.responseText;
					if(state.indexOf('1') != -1) {
						Ext.get("all").dom.parentNode.style.display="";
					}
				}
			});
		}	
		if (!init) {// add by ywliu 20091113
			Ext.Ajax.request({
				url : 'resource/getInfo.action',
				method : 'post',
				success : function(action) {
					var result = eval("(" + action.responseText + ")");
					if (result != "" && result != null) {
//						txtMrBy.setValue(result.workerName);
//						txtMrByH.setValue(result.workerCode);
						workCode = result.workerCode;
					}
//					firstLoad();
					findFuzzy();
				}
			});
		}
	}

	/**
	 * 人员选择画面处理
	 */
	function selectPersonWin() {
		if(queryType != '1') {// add by ywliu 20091113
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
				txtMrBy.setValue(person.workerName);
				txtMrByH.setValue(person.workerCode);
			}
		}	
	}

	var totalSum = 0;

	// ///////////////////////////////////////////////////////////////////////

	var timefromDate = new Ext.form.TextField({
		id : 'timefromDate',
		name : '_timefromDate',
		fieldLabel : "开始",// modify by ywliu 20091112
		style : 'cursor:pointer',
		cls : 'Wdate',
//		width : 108,
		anchor : '90%',
		value : getCurrentDateFrom(),
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
		// value : edate,
		value : getCurrentDateTo(),
		id : 'timetoDate',
		fieldLabel : "结束",// modify by ywliu 20091112
		style : 'cursor:pointer',
		cls : 'Wdate',
		width : 108,
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
	
	// -----add by jling -----------
	// 定义状态数据
	var stateData = new Ext.data.JsonStore({
		root : 'list',
		url : "resource/findAllPlanBusiStatus.action",
		fields : ['statusCode', 'statusName']
	});

	stateData.on('load', function(e, records) {
		stateComboBox.setValue(records[0].data.statusCode);
	});
	stateData.load();
	// 定义状态
	var stateComboBox = new Ext.form.ComboBox({
		fieldLabel : '状态',
		id : "stateCob",
		store : stateData,
		displayField : "statusName",
		valueField : "statusCode",
		mode : 'local',
		anchor : '90%',
		// width : 40,
		triggerAction : 'all',
		readOnly : true
	});

	// 申请人
	var txtMrBy = new Ext.form.TextField({
		fieldLabel : '申请人',
		readOnly : true,
		maxLength : 100,
		width : 108,
		id : "mrBy",
		name : "mrBy"
	});
	txtMrBy.onClick(selectPersonWin);
	var txtMrByH = new Ext.form.Hidden();
	
	// 采购人
	var txtBuyBy = new Ext.form.TextField({
		fieldLabel : '采购人',
		readOnly : true,
		maxLength : 100,
		width : 108,
		id : "buyBy",
		name : "buyBy"
	});
	txtBuyBy.onClick(selectBuyByWin);
	var txtBuyByH = new Ext.form.Hidden();
	function selectBuyByWin() {
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
			txtBuyBy.setValue(person.workerName);
			txtBuyByH.setValue(person.workerCode);
		}
	}

	var timePanel = new Ext.Panel({
		id : 'timeAreaPanel',
		border : false,
		layout : 'form',
		anchor : '100%',
		items : [{
			layout : 'column',
			border : false,
			items : [{
				border : false,
				columnWidth : 0.3,
				align : 'center',
				layout : 'form',
//				labelWidth : 80,
				items : [timefromDate]
			}, {
				align : 'center',
				layout : 'form',
				border : false,
				columnWidth : 0.3,
				items : [timetoDate]
			}]

		}]
	});

	var toolBarTop = new Ext.Toolbar({
		region : 'north',
		items : [{
			xtype : 'tbfill',
			width : '80%'
		}, new Ext.Toolbar.Button({})]
	});
	// --------工具栏按钮-----结束
	// --------第一行textbox--开始

	// 物资规格
	var txtSpecNo = new Ext.form.TextField({
		fieldLabel : '物资规格',
		// readOnly : true,
		maxLength : 100,
		width : 108,
		id : "specNo",
		name : "specNo"
	});
	// 物资编码
	var txtMaterialNo = new Ext.form.TextField({
		fieldLabel : '物资编码',
		// readOnly : true,
		maxLength : 100,
		width : 108,
		id : "materialNo",
		name : "materialNo"
	});

	// 物料名称
	var txtMaterialName = new Ext.form.TextField({
		fieldLabel : '物料名称',
		readOnly : false,
		width : 108,
		id : "materialName"
	});
	// txtMaterialName.onClick(selectMaterial);

	// 查看流程按钮
	var btnView = new Ext.Toolbar.Button({
		text : "流程",
		iconCls : 'view',
		handler : function() {
			if (materialGrid.selModel.hasSelection()) {
				var record = materialGrid.getSelectionModel().getSelected();
				var entryId = record.get("wfNo");
				var planSource = record.get("planOriginalId");
				var flowCode = getFlowCode(planSource);
				if (flowCode == "") {
					Ext.Msg.alert("提示", "该流程不存在！");
					return;
				}
				if (entryId == null || entryId == "") {
					url = application_base_path+"workflow/manager/flowshow/flowshow.jsp?flowCode="
							+ flowCode;
				} else {
					url =application_base_path+ "workflow/manager/show/show.jsp?entryId="
							+ entryId;
				}
				window.open(url);
			} else {
				Ext.MessageBox.alert("提示", "请选择要查看的记录");
			}
		}

	});
	
	// 返回按钮 add by ywliu 20091112
	var btnReturn = new Ext.Toolbar.Button({
		text : "返回",
		handler : function() {
			window.location.replace("../query/MRPInformation.jsp");
		}

	});
	// add by ywliu 20091112
	function getChooseQueryType() {
		var list = document.getElementsByName("rightRadio");
		for (var i = 0; i < list.length; i++) {
			if (list[i].checked) {
				return list[i].id;
			}
		}
	}
	
	// add by ywliu 20091112
	var reportRadio = new Ext.form.Radio({
		id : 'report',
		name : 'rightRadio',
		boxLabel : '我上报的',
		hideLabel : true,
		checked : true,
		listeners : {
			check : function() {
				var type = getChooseQueryType();
				switch (type) {
					case 'report' : {
						queryType = '1';
						txtMrDept.setValue('请选择');// add by ywliu 20091113
						txtMrDeptH.setValue('');// add by ywliu 20091113
						txtMrBy.setValue('');
						txtMrByH.setValue('');
						getUserName();// add by ywliu 20091113
						break;
					}
					case 'approve' : {
						queryType = '2';
						txtMrDept.setValue('请选择');// add by ywliu 20091113
						txtMrDeptH.setValue('');// add by ywliu 20091113
						txtMrBy.setValue('');
						txtMrByH.setValue('');
						getUserName();// add by ywliu 20091113
						break;
					}
					case 'all' : {
						queryType = '3';
						txtMrDept.setValue('请选择');// add by ywliu 20091113
						txtMrDeptH.setValue('');// add by ywliu 20091113
						txtMrBy.setValue('');
						txtMrByH.setValue('');
						getUserName();// add by ywliu 20091113
						//btnInquire.setVisible(true);
						break;
					}
					case 'dept' : {
						queryType = '4';
						txtMrDept.setValue('请选择');// add by ywliu 20091113
						txtMrDeptH.setValue('');// add by ywliu 20091113
						txtMrBy.setValue('');
						txtMrByH.setValue('');
						getUserName();// add by ywliu 20091113
						break;
					}
				}
				findFuzzy();
			}
		}
//		disabled : true
	});
	
	var deptRadio = new Ext.form.Radio({
		id : 'dept',
		name : 'rightRadio',
		hideLabel : true,
//		disabled : true,
		boxLabel : ' 本部门的'
	});

	var approveRadio = new Ext.form.Radio({
		id : 'approve',
		name : 'rightRadio',
		hideLabel : true,
//		disabled : true,
		boxLabel : ' 我审批的'
	});

	var allRadio = new Ext.form.Radio({
		id : 'all',
		name : 'rightRadio',
		hideLabel : true,
//		disabled : true,
		value : '3',
		boxLabel : '所有的'
	});
	
	var rightRadioPanel = new Ext.Panel({
		achor : '100%',
		border : false,
		layout : 'form',
		items : [{
			layout : 'column',
			border : false,
			items : [{
				border : false,
				columnWidth : 0.25,
				align : 'center',
				layout : 'form',
				items : [reportRadio]
			}, {
				border : false,
				columnWidth : 0.25,
				align : 'center',
				layout : 'form',
				items : [deptRadio]
			}, {
				align : 'center',
				layout : 'form',
				border : false,
				columnWidth : 0.25,
				items : [approveRadio]
			}, {
				border : false,
				columnWidth : 0.25,
				align : 'center',
				layout : 'form',
				items : [allRadio]
			}]
		}]
	});
	// add by ywliu 20091112 End

	var Query = new Ext.Panel({
		border : false,
		height : 30,
		anchor : '100%',
		layout : 'column',
		items : [{
			id : 'query',
			border : false,
			columnWidth : 0.65,
			anchor : '100%',
			items : [timePanel]
		}, {// add by ywliu 20091112
			border : false,
			columnWidth : 0.35,
			anchor : '100%',
			items : [rightRadioPanel]
		}]
	});
	
	// 需求计划单号 add by liuyi 091029
	var needPlanNo = new Ext.form.TextField({
		id : 'neddPlanNo',
		name : 'neddPlanNo',
		width : 108,
		fieldLabel : '计划单号', //modify by ywliu 20091112
		anchor : '90%'
	})

	var firstLine = new Ext.Panel({
		border : false,
		// height : 30,
		layout : "column",
		// style :
		// "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
			columnWidth : 0.2,
			layout : "form",
			border : false,
//			labelWidth : 80,
			items : [needPlanNo]
		}, {
			columnWidth : 0.2,
			layout : "form",
			border : false,
			items : [txtMaterialNo]
		}, {
			columnWidth : 0.2,
			layout : "form",
			border : false,
			items : [txtMaterialName]
		}, {
			columnWidth : 0.2,
			border : false,
			layout : "form",
			items : [txtSpecNo]
		}, {
			columnWidth : 0.08,
			layout : "form",
			border : false,
			items : [{
				width : 72,
				layout : "form",
				border : false,
				items : [btnView]
			}]
		},{
			columnWidth : 0.08,
			layout : "form",
			border : false,
			items : [{
				width : 72,
				layout : "form",
				border : false,
				items : [btnReturn]//  add by ywliu 20091112
			}]
		}]
	});
	// --------第一行textbox--结束
	// --------第二行textbox--开始

	// 查询按钮
	var btnFind = new Ext.Toolbar.Button({
		text : "查询",
		iconCls : Constants.CLS_QUERY,
		handler : findFuzzy
	});
	
	function choseDept() {
		if(queryType != '1'&&queryType != '4') {// add by ywliu 20091113
			var args = {
				selectModel : 'single',
				rootNode : {
					id : '0',
					text : '合肥电厂'
				}
			}
			var dept = window
					.showModalDialog(
							'../../../../comm/jsp/hr/dept/dept.jsp',
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (typeof(dept) != "undefined") {
				txtMrDept.setValue(dept.names);
				txtMrDeptH.setValue(dept.codes);
			}
		}
	}
	
	// 部门  add by ywliu 20091112
	var txtMrDept = new Ext.form.TriggerField({
		fieldLabel : '申请部门',
		width : 108,
		id : "mrDeptId",
		displayField : 'text',
		valueField : 'id',
		hiddenName : 'mrDept',
		blankText : '请选择',
		emptyText : '请选择',
		maxLength : 100,
		anchor : '90%',
		readOnly : true
	});
	txtMrDept.onTriggerClick = choseDept;
	var txtMrDeptH = new Ext.form.Hidden({
		hiddenName : 'mrDept'
	})
	
	// add by liuyi 20100316
	var exportBtu = new Ext.Button({
				text : '导出',
				iconCls : 'export',
				id : 'export',
				handler : exportStore
			})
	
	var secondLine = new Ext.Panel({
		border : false,
		// height : 30,
		layout : "column",
		// style :
		// "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
			columnWidth : 0.2,
			layout : "form",
			border : false,
//			labelWidth : 80,
			items : [txtMrDept,txtMrDeptH]
		}, {
			align : 'center',
			layout : 'form',
			border : false,
			columnWidth : 0.2,
			items : [txtMrBy, txtMrByH]
		}, {
			align : 'center',
			layout : 'form',
			border : false,
			columnWidth : 0.2,
			items : [txtBuyBy, txtBuyByH]
		}, {                             // added by jling
			columnWidth : 0.2,
			layout : "form",
			border : false,
			items : [stateComboBox]
		}, /*{
			columnWidth : 0.2,
			border : false,
			layout : "form"
//			items : [txtSpecNo]
		},*/ {
			// width : 125,
			// 05/08/09 13:21 使上下两行数据对齐 yiliu
			width : 72,
			columnWidth : 0.08,
			layout : "form",
			border : false,
			items : [{
				width : 72,
				layout : "form",
				border : false,
				items : [btnFind]
			}]
		}
		
		// add by liuyi 20100317
		, {
			// width : 125,
			// 05/08/09 13:21 使上下两行数据对齐 yiliu
			width : 72,
			columnWidth : 0.08,
			layout : "form",
			border : false,
			items : [{
				width : 72,
				layout : "form",
				border : false,
				items : [exportBtu]
			}]
		}


		]
	});

	// var thirdLine = new Ext.Panel({
	// border : false,
	// // height : 30,
	// layout : "column",
	// // style :
	// "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
	// anchor : '100%',
	// items : [ {
	// columnWidth : 0.28,
	// layout : "form",
	// border : false,
	// items : [txtMaterialClass]
	// }, {
	// columnWidth : 0.32,
	// layout : "form",
	// border : false,
	// items : [stateComboBox]
	// }, {
	// columnWidth : 0.3,
	// border : false,
	// layout : "form",
	// items : [txtMrOriginal,txtMrOriginalH]
	// }, {
	// // width : 125,
	// // 05/08/09 13:21 使上下两行数据对齐 yiliu
	// width : 72,
	// columnWidth : 0.1,
	// layout : "form",
	// border : false,
	// items : [{
	// width : 72,
	// layout : "form",
	// border : false,
	// items : [btnFind]
	// }]
	// }
	//
	// ]
	// });

	// -----------------------
	// --------第二行textbox--结束
	// 主表一览行数据定义
	var headInfo = Ext.data.Record.create([{
		// ID
		name : 'requirementHeadId'
	}, {	// 工作流状态
		name : 'mrStatus'
	}, {	// 申请单编号
		name : 'mrNo'
	}, {	// 申请部门
		name : 'mrDept'
	}, {	// 申请人
		name : 'mrBy'
	}, {	// 申请理由
		name : 'mrReason'
	}, {	// 申请日期
		name : 'mrDate'
	}, {	// 物料类别
		name : 'materialClass'
	}, {	// 费用来源
		name : 'itemId'
	}, {	// 费用来源
		name : 'wfNo'
	}, {
		name : 'planOriginalId'
	}])
	// --------第三行grid--开始
	// 主表一览的store
	var headStore = new Ext.data.JsonStore({
		url : 'resource/getPlanHeadByFuzzy.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : headInfo
	});
	// 物料grid
	var pageBar = new Ext.PagingToolbar({
		pageSize : Constants.PAGE_SIZE,
		store : headStore,
		displayInfo : true,
		displayMsg : Constants.DISPLAY_MSG,
		emptyMsg : Constants.EMPTY_MSG
	});
	// add by fyyang 090728---增加删除------------
	var btnDelete = new Ext.Button({
		id : "btnDelete",
		text : "删除",
		hidden : true,
		iconCls : 'delete',
		handler : function() {
			var record = headGrid.getSelectionModel().getSelected();
			if (record) {
				if (confirm("删除后数据不能恢复,确定要删除该条需求计划单吗?")) {
					var headId = record.get("requirementHeadId");
					var entryId = record.get("wfNo");
					Ext.Ajax.request({
						url : "resource/deletePlanRequirement.action",
						method : 'post',
						params : {
							entryId : entryId,
							headId : headId
						},
						success : function(result, request) {
							Ext.Msg.alert("提示", "操作成功!");
							headGrid.getStore().remove(record);
							materialStore.removeAll();
							btnDelete.setVisible(false);
						},
						failure : function() {
							Ext.Msg.alert("提示", "操作失败!");
						}
					});
				}
			} else {
				Ext.Msg.alert("提示", "请选择一条删除的记录！");
			}
		}
	});
	// -------------------------------------------

	var headGrid = new Ext.grid.GridPanel({
		region : "center",
		layout : 'fit',
		height : 200,
		autoSizeColumns : true,
		border : false,
		enableColumnMove : false,
		// 单选
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
		store : headStore,
		columns : [new Ext.grid.RowNumberer({
			header : "行号",
			width : 35
		}),
				// ID
				{
					header : "ID",
					width : 60,
					sortable : true,
					hidden : true,
					dataIndex : 'requirementHeadId'
				},
				// 状态
				{
					header : "状态",
					width : 60,
					sortable : true,
					renderer : function(val) {
						return busiStatus(val);
						// if(val == "0"){
						// return "待审批";
						// }else if(val =="1"){
						// return "审批中";
						// }else if(val == "2"){
						// return "审批结束";
						// }else if(val == "0"){
						// return "退回";
						// }else {
						// return "";
						// }
					},
					dataIndex : 'mrStatus'
				},
				// 申请编号
				{
					header : "申请编号",
					dataIndex : "mrNo",
					sortable : true,
					width : 80
				},
				// 申请单位
				{
					header : "申请单位",
					width : 100,
					sortable : true,
					dataIndex : 'mrDept'
				},
				// 申请人
				{
					header : "申请人",
					width : 100,
					sortable : true,
					dataIndex : 'mrBy'
				},
				// 申请理由
				{
					header : "申请理由",
					width : 100,
					sortable : true,
					dataIndex : 'mrReason'
				},
				// 申请时间
				{
					header : "申请时间",
					width : 100,
					sortable : true,
					renderer : renderDate,
					dataIndex : 'mrDate',
					renderer : function(value, cellmeta, record, rowIndex) {

						return getPlanDateInfoByDate(record
								.get("planOriginalId"), value);
					}
				},
				// 物料类别
				{
					header : "物料类别",
					width : 100,
					sortable : true,
					renderer : getClassName,
					dataIndex : 'materialClass'
					// 05/08/09 15:46 隐藏该列 yiliu
					,
					hidden : true
				},
				// 费用来源
				{
					header : "费用来源",
					width : 100,
					sortable : true,
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
		tbar : ['->', btnDelete, '<div id="divManagerDel">需求计划列表</div>'],
		// 分页
		bbar : pageBar
	});

	/**
	 * 初始化数据load
	 */
	function firstLoad() {
		headStore.baseParams = {
			dateFrom : timefromDate.getValue(),
			dateTo : timetoDate.getValue(),
			// mrDept : txtMrDeptH.getValue(),
			mrBy : txtMrByH.getValue(),
			status : stateComboBox.getValue(),
			maName : txtMaterialName.getValue()
			
		};
		headStore.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			}
		});
	}

	headStore.on('load', function() {
	})

	var thirdLine = new Ext.Panel({
		border : false,
		layout : "column",
		anchor : '100%',
		items : [{
			columnWidth : 1,
			layout : "fit",
			border : false,
			autoScroll : false,
			items : [headGrid]
		}]
	});
	// --------第三行grid--结束
	var expander = new Ext.grid.RowExpander({
		// tpl : new Ext.Template(
		// // '<p><b>工作票号:</b> </p><br>',
		// '<p><b>工作内容：</b></p>'),
		expandRow : mygridExpend
	});
	function mygridExpend(row) {
		if (typeof row == 'number') {
			row = this.grid.view.getRow(row);
		}
		var record = this.grid.store.getAt(row.rowIndex);
		var id = record.get("requirementDetailId");
		// 增加判断明细ID是否为空 add by ywliu 090722
		if (id != null && id != "") {
			// //-----------------------
			var conn = Ext.lib.Ajax.getConnectionObject().conn;
			conn.open("POST",
					'resource/getOrdersByDetailID.action?requirementDetailId='
							+ id, false);
			conn.send(null);
			// 成功状态码为200
			if (conn.status == "200") {
				var result = conn.responseText;
				var record1 = eval('(' + result + ')');
				var purNo = "采购单号：";
				var purchaseQuatity = "采购数量："
				if (record1.materialName != null && record1.materialName != "") {
					purNo += record1.materialName
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
				} else {
					purNo = "";
				}
				if (record1.purchaseQuatity != null
						&& record1.purchaseQuatity != "") {
					purchaseQuatity += record1.purchaseQuatity
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
				} else {
					purchaseQuatity = "";
				}
				var achieveQuantity = "已收数量：";
				if (record1.achieveQuantity != null
						&& record1.achieveQuantity != "") {
					achieveQuantity += record1.achieveQuantity
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
				} else {
					achieveQuantity = "";
				}
				var clientName = "";
				if (record1.meno != null && record1.meno != "") {
					clientName = "供应商：" + record1.meno
							+ "&nbsp;&nbsp;&nbsp;&nbsp;";
				}
				var price = "";
				if (record1.quotedPrice != null && record1.quotedPrice != "") {
					price = "单价：" + record1.quotedPrice
							+ "&nbsp;&nbsp;&nbsp;&nbsp;";
				}
				var buyer = "";
				if (record1.buyerName != null && record1.buyerName != "") {
					buyer = "采购人：" + record1.buyerName
							+ "&nbsp;&nbsp;&nbsp;&nbsp;";
				}
				var buyTime = "";
				if (record1.buyTime != null && record1.buyTime != "") {
					buyTime = "采购时间：" + record1.buyTime
							+ "&nbsp;&nbsp;&nbsp;&nbsp;";
				}
				var dueDate = "";
				if (record1.dueDate != null && record1.dueDate != "") {
					dueDate = "预计到货时间：" + record1.dueDate
							+ "&nbsp;&nbsp;&nbsp;&nbsp;";
				}
			}
			conn = Ext.lib.Ajax.getConnectionObject().conn;
			conn.open("POST",
					'resource/getArrivalListByDetailID.action?requirementDetailId='
							+ id, false);
			conn.send(null);
			if (conn.status == "200") {
				var result = conn.responseText;
				// alert(result);
				var record2 = eval('(' + result + ')');
				var mifNo = "";
				if (record2 != null) {
					for (var i = 0; i < record2.length; i++) {
						var itemStatus = record2[i].itemStatus;
						if (itemStatus == "0") {
							itemStatus = "已到货";
						} else if (itemStatus == "1") {
							itemStatus = "已验收合格";
						} else if (itemStatus == "3") {
							itemStatus = "已验收不合格";
						} else if (itemStatus == "2") {
							itemStatus = "已入库";
						}

						mifNo += "到货单号："
								+ record2[i].mifNo
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+
								// "检验不合格数:"+ record2[i].badQty +
								"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ "已收数量：" + record2[i].rcvQty
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ "已入库数量:" + record2[i].recQty
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ "本次到货数量:" + record2[i].theQty
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
								+ "物料状态:" + itemStatus + "<BR>";
					}
				}
			}
			conn = Ext.lib.Ajax.getConnectionObject().conn;
			conn.open("POST",
					'resource/getIssueDetailByDetailID.action?requirementDetailId='
							+ id, false);
			conn.send(null);
			if (conn.status == "200") {
				var result = conn.responseText;
				// alert(result);
				var record3 = eval('(' + result + ')');
				var issueNo = "";
				var appliedCount = "";
				var actIssuedCount = "";
				var approvedCount = "";
				if (record3 != null) {
					if (record3.issueNo != null && record3.issueNo != "") {
						issueNo = "领料单号：" + record3.issueNo
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
					}
					if (record3.appliedCount != null
							&& record3.appliedCount != "") {
						appliedCount = "申请数量：" + record3.appliedCount
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
					}
					if (record3.approvedCount != null
							&& record3.approvedCount != "") {
						approvedCount = "核准数量：" + record3.approvedCount
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
					}
					if (record3.actIssuedCount != null
							&& record3.actIssuedCount != "") {
						actIssuedCount = "实际发货数量：" + record3.actIssuedCount;
					}
				}
			}
			expander.tpl = new Ext.Template(
					// '<p><font color="blue"><b>工作票号：</b>'+no+'</font>
					// </p><br>',
					'<p> <br><font color="blue"><b>采购单内容：</b><br>' + '<span>'
							+ purNo + '</span>' + purchaseQuatity + buyer
							+ buyTime + dueDate + '</font></p>',
					'<p> <br><font color="blue"><b>到货单内容：</b><br>' + mifNo
							+ '</font></p>',
					'<p> <br><font color="blue"><b>领料单内容：</b><br>' + issueNo
							+ appliedCount + approvedCount + actIssuedCount
							+ '&nbsp;&nbsp;</font></p>');
			var body = Ext.DomQuery.selectNode(
					'tr:nth(2) div.x-grid3-row-body', row);
			if (this.beforeExpand(record, body, row.rowIndex)) {
				this.state[record.id] = true;
				Ext.fly(row).replaceClass('x-grid3-row-collapsed',
						'x-grid3-row-expanded');
				this.fireEvent('expand', this, record, body, row.rowIndex);
			}
		}
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
	}, {
		name : 'mrDate'
	}, {
		name : 'useFlag'
	}, {
		name : 'cancelReason'
	}, {
		name : 'mrNo'
	}
			// add by liuyi 091104 开始
			, {
				// 采购项次号
				name : 'orderDetailsId'
			}, {
				// 采购单号
				name : 'purNo'
			}, {
				// 采购数量
				name : 'purchaseQuatity'
			}, {
				// 预计到货时间
				name : 'dueDate'
			}, {
				// 采购人编码
				name : 'buyerBy'
			}, {
				// 采购人名
				name : 'buyerName'
			}, {
				// 采购时间
				name : 'buyTime'
			}, {
				// 已入库数量
				name : 'recQty'
			}, {
				// 已到货数
				name : 'theQty'
			}, {
				// 已领用数
				name : 'actIssuedCount'
			}, {
				// 工作流 add by drdu 091106
				name : 'wfNo'
			}, {
				// 计划来源 add by drdu 091106
				name : 'planOriginalId'
			}
			// add by liuyi 091104 结束
			, {
				// 计划来源 add by ywliu 091112
				name : 'applyDeptName'
			},{name:'supplier'},
			{name:'unitPirce'},
			{name:'status'}
			]);
	// 物料grid的store
	var materialStore = new Ext.data.JsonStore({
		url : 'resource/findMaterialDetailsByCond.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : material
	});
	
	//---------询价按钮 add by fyyang 100112-------------------
		var btnInquire = new Ext.Toolbar.Button({
		text : "查看询价信息",
		iconCls : Constants.CLS_QUERY,
		handler : function() {
			if (materialGrid.selModel.hasSelection()) {

				var records = materialGrid.selModel.getSelections();
				var record = materialGrid.getSelectionModel().getSelected();

				var args = new Object();
				args.materialName = record.get("materialName");
				args.requirementDetailId = record.get("requirementDetailId");
				var object = window
						.showModalDialog(
								'priceQueryForSelect.jsp',
								args,
								'dialogWidth=800px;dialogHeight=400px;center=yes;help=no;resizable=no;status=no;');
			} else {
				Ext.Msg.alert("提示", "请先选择物资!");
			}
		}
	});
	
	// 物料grid
	var materialGrid = new Ext.grid.GridPanel({
		region : "center",
		border : false,
		autoScroll : true,
		enableColumnMove : false,
		viewConfig : {
			forceFit : false
		},
		// 单选
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
		// 单击修改
		store : materialStore,
		columns : [
				// expander,
				new Ext.grid.RowNumberer({
					header : "行号",
					width : 35
				}),
				// 项次号
				{
					header : "项次号",
					width : 100,
					sortable : true,
					hidden : true,
					dataIndex : 'requirementDetailId',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + v + "</font>";
						else
							return v;
					}
				}, {
					header : "计划单号",
					width : 100,
					sortable : true,
					dataIndex : 'mrNo',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + v + "</font>";
						else
							return v;
					}
				},{
					header : "状态",
					width : 100,
					sortable : true,
					dataIndex : 'status',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + busiStatus(v) + "</font>";
						else
							return busiStatus(v);
					}
				},
				// 物料编码
				{
					header : "物料编码",
					width : 100,
					sortable : true,
					dataIndex : 'materialNo',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + v + "</font>";
						else
							return v;
					}
				},// 物料名称
				{
					header : "物料名称",
					width : 100,
					sortable : true,
					dataIndex : 'materialName',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + v + "</font>";
						else
							return v;
					}
				},// 物料规格
				{
					header : "物料规格",
					width : 100,
					sortable : true,
					dataIndex : 'materSize',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + v + "</font>";
						else
							return v;
					}
				},// 申请数量
				{
					header : "申请数量",
					width : 80,
					sortable : true,
					align : 'right',
					hidden : true,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						return moneyFormat(value);
					},
					dataIndex : 'appliedQty',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + v + "</font>";
						else
							return v;
					}
				},// 核准数量
				{
					header : "核准数量",
					width : 80,
					sortable : true,
					align : 'right',
//					renderer : function(value, cellmeta, record, rowIndex,
//							columnIndex, store) {
//						return moneyFormat(value);
//					},
					dataIndex : 'approvedQty',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + moneyFormat(v) + "</font>";
						else
							return moneyFormat(v);
					}
				}, {
					header : "采购数量",
					width : 80,
					sortable : true,
					dataIndex : 'purchaseQuatity',
					align : 'right',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + moneyFormat(v) + "</font>";
						else
							return moneyFormat(v);
					}
				},
				// 采购数量
//				{
//					header : "采购数量",
//					width : 80,
//					hidden : true,
//					sortable : true,
//					align : 'right',
//					renderer : function(value, cellmeta, record, rowIndex,
//							columnIndex, store) {
//						return numberFormat(value);
//					},
//					dataIndex : 'purQty',
//					renderer : function(v, cellmeta, record, rowIndex,
//							columnIndex, store) {
//						if (record.get('useFlag') == 'C' && v != null)
//							return "<font color=red>" + v + "</font>";
//						else
//							return v;
//					}
//				},
				{
					header : "已到货数量",
					width : 80,
					sortable : true,
					dataIndex : 'theQty',
					align : 'right',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + moneyFormat(v) + "</font>";
						else
							return moneyFormat(v);
					}
				}, {
					header : "已入库数量",
					width : 80,
					sortable : true,
					align : 'right',
					dataIndex : 'recQty',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + moneyFormat(v) + "</font>";
						else
							return moneyFormat(v);
					}
				}, {
					header : "已领用数量",
					width : 80,
					sortable : true,
					align : 'right',
					dataIndex : 'actIssuedCount',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + moneyFormat(v) + "</font>";
						else
							return moneyFormat(v);
					}
				}, {//add by drdu 091126
					header : "估计单价",
					width : 80,
					sortable : true,
					dataIndex : 'estimatedPrice',
					align : 'right',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + numberFormat(v) + "</font>";
						else
							return numberFormat(v);
					}
				}, 
					 {//add by drdu 091126
					header : "采购单价",
					width : 80,
					sortable : true,
					dataIndex : 'unitPirce',
					align : 'right',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + numberFormat(v) + "</font>";
						else
							return numberFormat(v);
					}
				},{
					header : "采购人",
					width : 100,
					sortable : true,
					dataIndex : 'buyerName',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + v + "</font>";
						else
							return v;
					}
				}, {
					header : "采购时间",
					width : 100,
					sortable : true,
					dataIndex : 'buyTime',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + v + "</font>";
						else
							return v;
					}
				}, {
					header : "预计到货时间",
					width : 100,
					sortable : true,
					dataIndex : 'dueDate',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + v + "</font>";
						else
							return v;
					}
				},
				// 需求日期
				{
					header : "申请日期",
					width : 100,
					sortable : true,
					dataIndex : 'mrDate',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + v + "</font>";
						else
							return v;
					}
				}, {
					header : "采购单号",
					width : 100,
					sortable : true,
					dataIndex : 'purNo',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + v + "</font>";
						else
							return v;
					}
				},
				{
					header : "供应商",
					width : 100,
					sortable : true,
					dataIndex : 'supplier',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + v + "</font>";
						else
							return v;
					}
				},	{
					header : "是否作废",
					width : 100,
					sortable : true,
					dataIndex : 'useFlag',
					renderer : function(v) {
						if (v == 'Y')
							return '未作废';
						else if (v == 'C')
							return '已作废';
					},
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + "已作废" + "</font>";
						else
							return "未作废";
					}
				}, {
					header : "作废原因",
					width : 100,
					sortable : true,
					dataIndex : 'cancelReason',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + v + "</font>";
						else
							return v;
					}
				},// 申请单位 add by ywliu 20091112
				{
					header : "申请部门",
					width : 120,
					sortable : true,
					dataIndex : 'applyDeptName'
				},// 估计单价
				{
					header : "估计单价",
					width : 100,
					hidden : true,
					sortable : true,
					align : 'right',
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						return numberFormat(value);
					},
					dataIndex : 'estimatedPrice',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + v + "</font>";
						else
							return v;
					}
				},// 估计金额
				{
					header : "估计金额",
					width : 100,
					sortable : true,
					hidden : true,
					align : 'right',
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						return numberFormat(value);
					},
					dataIndex : 'estimatedSum',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + v + "</font>";
						else
							return v;
					}
				},
				// 单位
				{
					header : "单位",
					width : 100,
					hidden : true,
					sortable : true,
					dataIndex : 'stockUmName',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + v + "</font>";
						else
							return v;
					}
				},

				// 物料材质
				{
					header : "物料材质",
					width : 100,
					hidden : true,
					sortable : true,
					dataIndex : 'parameter',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + v + "</font>";
						else
							return v;
					}
				},
				// 物料图号
				{
					header : "物料图号",
					width : 100,
					hidden : true,
					sortable : true,
					dataIndex : 'docNo',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + v + "</font>";
						else
							return v;
					}
				},
				// 仓库
				{
					header : "仓库",
					width : 100,
					hidden : true,
					sortable : true,
					dataIndex : 'whsName',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + v + "</font>";
						else
							return v;
					}
				},// 质量等级
				{
					header : "质量等级",
					width : 100,
					sortable : true,
					hidden : true,
					dataIndex : 'qualityClass',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + v + "</font>";
						else
							return v;
					}
				},
				// 当前库存
				{
					header : "当前库存",
					width : 100,
					sortable : true,
					align : 'right',
					hidden : true,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						return numberFormat(value);
					},
					dataIndex : 'left',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + v + "</font>";
						else
							return v;
					}
				},
				// 暂收数量
				{
					header : "暂收数量",
					width : 100,
					sortable : true,
					hidden : true,
					align : 'right',
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						return numberFormat(value);
					},
					dataIndex : 'tempNum',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + v + "</font>";
						else
							return v;
					}
				},
				// 费用来源
				{
					header : "费用来源",
					width : 100,
					hidden : true,
					sortable : true,
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
					dataIndex : 'itemId',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + v + "</font>";
						else
							return v;
					}
				},
				// 用途
				{
					header : "用途",
					width : 100,
					sortable : true,
					hidden : true,
					dataIndex : 'usage',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + v + "</font>";
						else
							return v;
					}
				},
				// 备注
				{
					header : "备注",
					width : 100,
					hidden : true,
					sortable : true,
					dataIndex : 'memo',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + v + "</font>";
						else
							return v;
					}
				}, {
					header : "采购项次号",
					width : 100,
					sortable : true,
					hidden : true,
					dataIndex : 'orderDetailsId',
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (record.get('useFlag') == 'C' && v != null)
							return "<font color=red>" + v + "</font>";
						else
							return v;
					}
				}],
		tbar : ['需求计划物资明细', "<div>&nbsp;&nbsp;&nbsp;&nbsp;</div>", '颜色说明：',
				"<div>&nbsp;&nbsp;</div>",
				"<div style='width:40; color:white; background:red'>已作废</div>",
				btnInquire
		// {text:'查看询价信息',
		// iconCls : Constants.CLS_QUERY,
		// handler:function(){
		//							
		// if (materialGrid.selModel.hasSelection()) {
		//		
		// var records = materialGrid.selModel.getSelections();
		// var record = materialGrid.getSelectionModel().getSelected();
		//														  
		// var args=new Object();
		// args.materialName=record.get("materialName");
		// args.requirementDetailId=record.get("requirementDetailId");
		// var object = window
		// .showModalDialog(
		// 'priceQueryForSelect.jsp',
		// args,
		// 'dialogWidth=800px;dialogHeight=400px;center=yes;help=no;resizable=no;status=no;');
		// } else {
		// Ext.Msg.alert("提示", "请先选择物资!");
		// }
		//								
		// }
		// }
		],
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : materialStore,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		}),
		enableColumnHide : true,
		enableColumnMove : false,
		plugins : expander,
		collapsible : true,
		animCollapse : false
	});
	// 备注
	var memoText = new Ext.form.TextArea({
		id : "memoText",
		width : 180,
		readOnly : true
	});
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
			text : "返回",
			handler : function() {
				win.hide();
			}
		}]
	});
	var fourthLine = new Ext.Panel({
		// border : false,
		layout : "fit",
		anchor : '100%',
		autoHeight : true,
		items : [materialGrid]
	});

	var sixLine = new Ext.Panel({
		border : false,
		layout : "fit",
		anchor : '100%',
		autoHeight : true,
		items : [firstLine, secondLine]
	});

	var fiveLine = new Ext.Panel({
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		border : false,
		layout : "fit",
		anchor : '100%',
		// autoHeight : true,
		height : 100,
		items : [{
			layout : 'column',
			border : false,
			items : [{
				border : false,
				columnWidth : 1,
				align : 'center',
				layout : 'form',
				anchor : '100%',
				items : [Query]
			}, {
				border : false,
				columnWidth : 1,
				align : 'center',
				layout : 'form',
				anchor : '100%',
				items : [sixLine]
			}]
		}]
	});

	// 表单panel
	var formPanel = new Ext.FormPanel({
		labelAlign : 'right',
		labelPad : 15,
		labelWidth : 70,
		border : false,
		autoHeight : true,
		region : 'north',
		items : [toolBarTop, fiveLine
		// ,
		// thirdLine]
		]
	});
	var queryPanel = new Ext.Panel({
		layout : 'border',
		frame : false,
		border : false,
		items : [formPanel, materialGrid]
	});

	var layout = new Ext.Viewport({
		layout : 'fit',
		margins : '0 0 0 0',
		border : false,
		items : [queryPanel]
	});
	
	txtMrByH.on('render', getUserName(true));

	
	
	
	
	var connObj = new Ext.data.Connection({ 
            timeout : 180000, 
            url : 'resource/findMaterialDetailsByCond.action',
            method : 'POST'
        });

	var materialStoreExport = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy(connObj),
				reader : new Ext.data.JsonReader({
							root : 'list',
							totalProperty : 'totalCount'
						}, material)
			});


	// 物料grid的store
//	var materialStoreExport = new Ext.data.JsonStore({
//		url : 'resource/findMaterialDetailsByCond.action',
//		root : 'list',
//		totalProperty : 'totalCount',
//		fields : material
//	});
	
	
	function exportStore(){
		Ext.Msg.wait('正在导出数据，请稍候……')
		materialStoreExport.baseParams = {
			queryType : queryType,// add by ywliu 20091113
			fromDate : timefromDate.getValue(),
			toDate : timetoDate.getValue(),
			materialNo : txtMaterialNo.getValue(),
			materialName : txtMaterialName.getValue(),
			specNo : txtSpecNo.getValue(),
			needPlanNo : needPlanNo.getValue(),
		    observer : workCode, // add by ywliu 20091113
			mrDept : txtMrDeptH.getValue(),// add by ywliu 20091112
			mrBy : txtMrByH.getValue(),
			status : stateComboBox.getValue(),// add by jling
			buyBy : txtBuyByH.getValue()
		}
		if(materialStoreExport.getCount() == 0){
			materialStoreExport.load();
		}else{
			materialStoreExport.reload();
		}
		materialStoreExport.on('load',exportFun);
	}
	
	function parseToString(v){
		if(v == null || v == 'undefined')
			return '';
		else
			return v;
	}
	// 导出
	function tableToExcel(tableHTML) {
		window.clipboardData.setData("Text", tableHTML);
		try {
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExApp.DisplayAlerts = false;
			ExApp.visible = true;
		} catch (e) {
			if (e.number != -2146827859)
				Ext.Msg.alert('提示信息', "您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}
	function exportFun(){
			if(materialStoreExport.getCount() == 0)
			{
				Ext.Msg.alert('提示','无数据进行导出！');
			}else
			{
				var tableHeader = "<table border=1>";
				var html = [tableHeader];
				html.push('<tr><th>计划单号</th><th>状态</th><th>物料编码</th><th>物料名称</th><th>物料规格</th><th>核准数量</th>' +
						'<th>采购数量</th><th>已到货数量</th><th>已入库数量</th><th>已领用数量</th><th>估计单价</th><th>采购单价</th>' +
						'<th>采购人</th><th>采购时间</th><th>预计到货时间</th><th>申请时间</th><th>采购单号</th>' +
						'<th>供应商</th><th>是否作废</th><th>作废原因</th><th>申请部门</th></tr>')
				for (var i = 0; i < materialStoreExport.getCount(); i++) {
					var rec = materialStoreExport.getAt(i);
					var zf = '';
					if(rec.get('useFlag') == 'Y')
						zf = '未作废';
					else if(rec.get('useFlag') == 'C')
						zf = '已作废';
					
					html.push('<tr><td>' + parseToString(rec.get('mrNo')) + '</td>' +
					    '<td>' + parseToString(busiStatus(rec.get('status'))) + '</td>' + 
						'<td>' + parseToString(rec.get('materialNo')) + '</td>' + 
						'<td>' + parseToString(rec.get('materialName')) + '</td>' + 
						'<td>' + parseToString(rec.get('materSize')) + '</td>' + 
						'<td>' + parseToString(moneyFormat(rec.get('approvedQty'))) + '</td>' + 
						
						'<td>' + parseToString(moneyFormat(rec.get('purchaseQuatity'))) + '</td>' + 
						'<td>' + parseToString(moneyFormat(rec.get('theQty'))) + '</td>' + 
						'<td>' + parseToString(moneyFormat(rec.get('recQty'))) + '</td>' + 
						'<td>' + parseToString(moneyFormat(rec.get('actIssuedCount'))) + '</td>' +
						'<td>' + parseToString(numberFormat(rec.get('estimatedPrice')))+ '</td>' + 
						'<td>' + parseToString(numberFormat(rec.get('unitPirce')))+ '</td>' + 
						'<td>' + parseToString(rec.get('buyerName')) + '</td>' + 
						'<td>' + parseToString(rec.get('buyTime')) + '</td>' + 
						'<td>' + parseToString(rec.get('dueDate')) + '</td>' + 
						'<td>' + parseToString(getPlanDateInfoByDate(rec.get('planOriginalId'),rec.get('mrDate'))) + '</td>' +
						'<td>' + parseToString(rec.get('purNo')) + '</td>' + 
								
						'<td>' + parseToString(rec.get('supplier')) + '</td>' + 
						'<td>' + zf + '</td>' + 
						'<td>' + parseToString(rec.get('cancelReason')) + '</td>' + 
						'<td>' + parseToString(rec.get('applyDeptName')) + '</td>' +
							'</tr>')
							
				}
				html.push('</table>');
				html = html.join(''); // 最后生成的HTML表格
				tableToExcel(html);
				Ext.Msg.hide()
			}
	}
});