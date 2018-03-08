Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function(){
	var monthDis = true;
	
	function numberFormat(value) {
		value = String(value);
		if (value == null || value == "null") {
			value = "0";
		}
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
		if (whole == null || whole == "null" || whole == "") {
			v = "0.00";
		}
		return v;
	}
	function percentFormat(value)
	{
		if (value == null || value == "null") {
			value = "0";
		}
		value = String(value * 100);
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
		var v = whole + sub;
		return v + '%';
	}
	function getDate() {
		var d, s, t,day;
		d = new Date();
		s = d.getFullYear().toString() + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		s +='-';
		day = d.getDate()
		s +=(day > 9 ? "" : "0") + day;
		return s;
	}
	function getCurrentMonth()
	{
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString() + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;		
		return s;
	}
	
	
	// 部门
	var deptH = new Ext.form.Hidden({
		id : 'deptH',
		name : 'deptH'
	})
	var budgetDept = new Ext.form.TextField({
		id : 'budgetDept',
		fieldLabel : '部门',
		readOnly : true,
		width : 100,
		listeners : {
			focus : function() {
				var args = {
					selectModel : 'single',
					rootNode : {
						id : "0",
						text : '合肥电厂'
					}
				}
				var url = "../../maint/budegtDeptSelect.jsp";
				var dept = window
						.showModalDialog(
								url,
								args,
								'dialogWidth:500px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;')
				if (typeof(dept)!= "undefined") {
					deptH.setValue(dept.centerId);
					budgetDept.setValue(dept.depName);
				}
				this.blur();
			}
		},
		allowBlank : false
	})
	// 时间
	var monthTime = new Ext.form.TextField({
				fieldLabel : '时间',
				readOnly : true,
				width : 80,
				id : 'monthTime',
				style : 'cursor:pointer',
				value : getCurrentMonth(),
				listeners : {
					focus : function() {
						WdatePicker({
									// 时间格式
									dateFmt : 'yyyy-MM',
									alwaysUseStartDate : false
								});
								this.blur();
					}
				}
			});
	// 本月对比
	var monthComp = new Ext.form.Radio({
		id : 'monthComp',
		name : 'comp',
//		fieldLabel : '本月对比',
		boxLabel : '本月对比',
		checked : true,
		hideLabel : true
	})
	
	// 累计对比
	var countComp = new Ext.form.Radio({
		id : 'countComp',
		name : 'comp',
		boxLabel : '累计对比',
		hideLabel : true
	})
	var query = new Ext.Button({
		id : 'btnAdd',
		iconCls : 'query',
		text : "查询",
		handler : queryRec
	})
	var save = new Ext.Button({
		id : 'btnSave',
		iconCls : 'save',
		text : "保存",
		handler : saveRec
	})
	var clear = new Ext.Button({
		id : 'btnClear',
		iconCls : 'delete',
		text : "清空",
		handler : clearRec
	})
	var expor = new Ext.Button({
		id : 'btnExport',
		iconCls : 'export',
		text : "导出",
		handler : exportRec
	})

	//month sm
	var monthsm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	})
	// month data
	var monthRecord = new Ext.data.Record.create([{
		name : 'analysisMonthId'
	},{
		name : 'itemId'
	},{
		name : 'centerId'
	},{
		name : 'dataTime'
	},{
		name : 'budgetValue'
	},{
		name : 'factValue'
	},{
		name : 'addReduce'
	},{
		name : 'itemContent'
	},{
		name : 'itemExplain'
	},{
		name : 'isUse'
	},{
		name : 'finaceName'
	},{
		name : 'itemAlias'
	},{
		name : 'zbbmtxCode'
	}])
	
	// month store
	var monthStore = new Ext.data.JsonStore({
		url : 'managebudget/getMonthAnlysisList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : monthRecord 
	})
	
	var monthGrid = new Ext.grid.GridPanel({
		id : 'monthGird',
		frame : false,
		border : false,
		autoScorll : true,
		sm : monthsm,
		ds : monthStore,
		region : 'center',
	    layout : 'fit',
		columns : [monthsm,new Ext.grid.RowNumberer({
			header : '行号',
			width : 31
		}),{
			header : '科目',
			width : 110,
			align : 'center',
			sortable : true,
			dataIndex : 'finaceName'
		},{
			header : '项目名称',
			width : 110,
			align : 'left',
			sortable : true,
			dataIndex : 'itemAlias',
			//add by ltong 分层次显示 按zbbmtxCode排序
			renderer : function(value, cellmeta, record, rowIndex, columnIndex, store){
						var level=0;
						if(record.get("zbbmtxCode")!=null||record.get("zbbmtxCode")!="")
						{
							level= (record.get("zbbmtxCode").length/3)-2;
						}
						 if(level>0)
						 {
						 var levelNo="";
						 for(var i=0;i<level;i++)
						{
							levelNo="  "+levelNo;
						}
						
						value=levelNo+value;
						 }
					return "<pre>"+value+"</pre>";
				}
		},{
			header : '预算值',
			width : 80,
			align : 'center',
			sortable : true,
			dataIndex : 'budgetValue'
		},{
			header : '实际值',
			width : 80,
			align : 'center',
			sortable : true,
			dataIndex : 'factValue'
		},{
			header : '差值',
			width : 80,
			align : 'center',
			sortable : true,
			dataIndex : 'addReduce'
		},{
			header : '项目内容',
			width : 120,
			align : 'center',
			sortable : true,
			dataIndex : 'itemContent'
		},{
			header : '原因分析',
			width : 130,
			align : 'center',
			sortable : true,
			dataIndex : 'itemExplain'
		}],
		bbar : new Ext.PagingToolbar({
									pageSize : 18,
									store : monthStore,
									displayInfo : true,
									displayMsg : "第{0} 条到第 {1} 条/共 {2} 条",
									emptyMsg : "没有记录"
								})
	})	
	
	// year sm
	var yearsm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	})
	// year record
	var yearRecord = new Ext.data.Record.create([{
		name : 'analysisYearId'
	},{
		name : 'itemId'
	},{
		name : 'centerId'
	},{
		name : 'dataTime'
	},{
		name : 'totalFact'
	},{
		name : 'yearBudget'
	},{
		name : 'percentValue'
	},{
		name : 'itemContent'
	},{
		name : 'itemExplain'
	},{
		name : 'isUse'
	},{
		name : 'finaceName'
	},{
		name : 'itemAlias'
	},{
		name : 'complatePercent'
	}])
	// year store
	var yearStore = new Ext.data.JsonStore({
		url : 'managebudget/getYearAnalysisList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : yearRecord
	})
	// year grid
	var yearGrid = new Ext.grid.EditorGridPanel({
		hide  : true,
		id : 'yearGird',
		frame : false,
		border : false,
		autoScorll : true,
		sm : yearsm,
		ds : yearStore,
		height :Ext.getBody().getViewSize().height-25.5,
		region : 'center',
	    layout : 'fit',
		columns : [monthsm,new Ext.grid.RowNumberer({
			header : '行号',
			width : 31
		}),{
			header : '科目',
			width : 110,
			align : 'center',
			sortable : true,
			dataIndex : 'finaceName'
		},{
			header : '项目名称',
			width : 110,
			align : 'center',
			sortable : true,
			dataIndex : 'itemAlias'
		},{
			header : '累计发生',
			width : 80,
			align : 'center',
			sortable : true,
			dataIndex : 'totalFact'
//			,
//			renderer : function(v)
//			{
//				return numberFormat(v);
//			}
		},{
			header : '年度预算',
			width : 80,
			align : 'center',
			sortable : true,
			dataIndex : 'yearBudget'
//			,
//			renderer : function(v)
//			{
//				return numberFormat(v);
//			}
		},{
			header : '完成百分比',
			width : 80,
			align : 'center',
			sortable : true,
			dataIndex : 'complatePercent'
		},{
			header : '项目内容',
			width : 120,
			align : 'center',
			sortable : true,
			css : CSS_GRID_INPUT_COL,
			editor : new Ext.form.TextField({}),
			dataIndex : 'itemContent'
		},{
			header : '原因分析',
			width : 130,
			align : 'center',
			sortable : true,
			editor : new Ext.form.TextArea({}),
			dataIndex : 'itemExplain',
			css : CSS_GRID_INPUT_COL
		}],
		bbar : new Ext.PagingToolbar({
									pageSize : 18,
									store : yearStore,
									displayInfo : true,
									displayMsg : "第{0} 条到第 {1} 条/共 {2} 条",
									emptyMsg : "没有记录"
								})
	})
	var panel = new Ext.Panel({
		id : 'panel',
		layout : "border",
		autoScorll : true,
		frame : false,
		border : false,
		tbar : ['部门：',budgetDept,'-','时间：',monthTime,'-',monthComp,'-',countComp,
		'-',query,'-',expor],
		items : [{
			region : 'center',
			layout : 'fit',
			border : false,
			frame : false,		
			items : [monthGrid,yearGrid]
		}]
	})		
	monthComp.on('check',function(monthComp,checked){
		if(monthComp.getValue() == true)
		{
			if(monthDis == true)
			{
			}
			else
			{
				monthGrid.show();
				monthDis = true;
			}						
		}
		else
		{
			if(monthDis == true)
			{
				monthGrid.hide();
				yearGrid.show();
				monthDis = false;
			}
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
			items : [panel]
		}]
	});
	
	function queryRec()
	{
		if(Ext.get('budgetDept').dom.value == null || Ext.get('budgetDept').dom.value == "")
		{
			Ext.Msg.alert('提示信息','请先选择部门！')
			return;
		}
		if(Ext.get("monthTime").dom.value == null || Ext.get("monthTime").dom.value == "")
		{
			Ext.Msg.alert('提示信息','请先选择时间！')
			return;
		}
		if (monthDis == true) {
			monthStore.baseParams = {
				dataTime : monthTime.getValue(),
				centerId : deptH.getValue()
			}
			monthStore.load({
						params : {
							start : 0,
							limit : 18
						}
					})
		}
		if(monthDis == false)
		{
			yearStore.baseParams = {
				dataTime : monthTime.getValue(),
				centerId : deptH.getValue()
			}
			yearStore.load({
						params : {
							start : 0,
							limit : 18
						}
					})
		}
		
	}
	
	function saveRec() {
		if (monthDis == true) {
			if (monthStore.getTotalCount() == 0) {
				Ext.Msg.alert('提示信息', '无数据进行保存！');
				return;
			}
			Ext.Msg.confirm('提示信息', '确认要保存吗？', function(id) {
				if (id == 'yes') {
					var add = new Array();
					var update = new Array();
					for (var i = 0; i < monthStore.getTotalCount(); i++) {
						if (monthStore.getAt(i).get('analysisMonthId') == null
								|| monthStore.getAt(i).get('analysisMonthId') == "") {
							add.push(monthStore.getAt(i).data)
						} else {
							update.push(monthStore.getAt(i).data)
						}
					}
					Ext.Ajax.request({
								url : 'managebudget/saveMonthModified.action',
								method : 'post',
								params : {
									isAdd : Ext.util.JSON.encode(add),
									isUpdate : Ext.util.JSON.encode(update)
								},
								success : function(result, request) {
									Ext.Msg.alert('提示信息', '数据保存成功！');
									monthStore.reload();
								},
								failure : function(result, request) {
									Ext.Msg.alert('提示信息', '数据保存失败！');
									monthStore.reload();
								}
							})
				}
			})
		}
//		年度
		if (monthDis == false) {
			if (yearStore.getTotalCount() == 0) {
				Ext.Msg.alert('提示信息', '无数据进行保存！');
				return;
			}
			Ext.Msg.confirm('提示信息', '确认要保存吗？', function(id) {
				if (id == 'yes') {
					var add = new Array();
					var update = new Array();
					for (var i = 0; i < yearStore.getTotalCount(); i++) {
						if (yearStore.getAt(i).get('analysisYearId') == null
								|| yearStore.getAt(i).get('analysisYearId') == "") {
							add.push(yearStore.getAt(i).data)
						} else {
							update.push(yearStore.getAt(i).data)
						}
					}
					Ext.Ajax.request({
								url : 'managebudget/saveYearAnalysisModified.action',
								method : 'post',
								params : {
									isAdd : Ext.util.JSON.encode(add),
									isUpdate : Ext.util.JSON.encode(update)
								},
								success : function(result, request) {
									Ext.Msg.alert('提示信息', '数据保存成功！');
									yearStore.reload();
								},
								failure : function(result, request) {
									Ext.Msg.alert('提示信息', '数据保存失败！');
									yearStore.reload();
								}
							})
				}
			})
		}
	}
	
	function clearRec() {
		if (monthDis == true) {
			monthGrid.stopEditing();
			if(monthStore.getTotalCount() == 0)
			{
				Ext.Msg.alert('提示信息','无数据进行清空！');
				return ;
			}
			for(var i = 0; i< monthStore.getTotalCount(); i++)
			{
				if(monthStore.getAt(i).get('analysisMonthId') == null ||
				 monthStore.getAt(i).get('analysisMonthId') == "")
				 {
				 	Ext.Msg.alert('提示信息','数据未保存，请先保存！')
				 	return ;
				 }
			}
			Ext.Msg.confirm('提示信息', '确认要清空该科目该月的所有记录？', function(id) {
				if (id == 'yes') {
					var ids = '';
					for(var i = 0; i< monthStore.getTotalCount(); i++)
					{
						ids += monthStore.getAt(i).get('analysisMonthId') + ','
					}
					ids = ids.substring(0,ids.length - 1);
					Ext.Ajax.request({
								url : 'managebudget/deleteMonthAnlysis.action',
								method : 'post',
								params : {
									ids : ids
								},
								success : function(result, request) {
									Ext.Msg.alert('提示信息', '数据清空成功！');
									monthStore.reload();
								},
								failure : function(result, request) {
									Ext.Msg.alert('提示信息', '数据清空失败！');
									monthStore.reload();
								}
							})
				}

		})
		}
		// 年度
		if (monthDis == false) {
			yearGrid.stopEditing();
			if(yearStore.getTotalCount() == 0)
			{
				Ext.Msg.alert('提示信息','无数据进行清空！');
				return ;
			}
			for(var i = 0; i< yearStore.getTotalCount(); i++)
			{
				if(yearStore.getAt(i).get('analysisYearId') == null ||
				 yearStore.getAt(i).get('analysisYearId') == "")
				 {
				 	Ext.Msg.alert('提示信息','数据未保存，请先保存！')
				 	return ;
				 }
			}
			Ext.Msg.confirm('提示信息', '确认要清空该科目该月的所有记录？', function(id) {
				if (id == 'yes') {
					var ids = '';
					for(var i = 0; i< yearStore.getTotalCount(); i++)
					{
						ids += yearStore.getAt(i).get('analysisYearId') + ',';
					}
					ids = ids.substring(0,ids.length - 1);
					Ext.Ajax.request({
								url : 'managebudget/deleteYearAnalysis.action',
								method : 'post',
								params : {
									ids : ids
								},
								success : function(result, request) {
									Ext.Msg.alert('提示信息', '数据清空成功！');
									yearStore.reload();
								},
								failure : function(result, request) {
									Ext.Msg.alert('提示信息', '数据清空失败！');
									yearStore.reload();
								}
							})
				}

		})
		}
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
				Ext.Msg.alert('提示信息',"您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}
	function exportRec() {
		if (monthDis == true) {
			if (monthStore.getTotalCount() == 0) {
				Ext.Msg.alert('提示信息', '无数据进行导出！');
				return;
			}
			Ext.Msg.confirm('提示信息', '确认要导出数据吗？', function(id) {
				if (id == 'yes') {					
					var html = ['<table border=1><tr><th>科目</th><th>项目名称</th><th>预算值</th><th>实际值</th><th>差值</th><th>项目内容</th><th>原因分析</th></tr>'];
					for (var i = 0; i < monthStore.getTotalCount(); i += 1) {
						var rc = monthStore.getAt(i);
						html.push('<tr><td>' + rc.get('finaceName')
								+ '</td><td>' + rc.get('itemAlias') 
								+ '</td><td>'+ rc.get('budgetValue') 
								+ '</td><td>' + rc.get('factValue')
								+ '</td><td>' + rc.get('addReduce') 
								+ '</td><td>'+ rc.get('itemContent') 
								+ '</td><td>' + rc.get('itemExplain')
								+ '</td><td>' );
					}
					html.push('</table>');
					html = html.join(''); // 最后生成的HTML表格
					tableToExcel(html);
				}
			})
		}
//		年度
		if (monthDis == false) {
			if (yearStore.getTotalCount() == 0) {
				Ext.Msg.alert('提示信息', '无数据进行导出！');
				return;
			}
			Ext.Msg.confirm('提示信息', '确认要导出数据吗？', function(id) {
				if (id == 'yes') {
					var html = ['<table border=1><tr><th>科目</th><th>项目名称</th><th>累计发生</th><th>年度预算</th><th>完成百分比</th><th>项目内容</th><th>原因分析</th></tr>'];
					for (var i = 0; i < yearStore.getTotalCount(); i += 1) {
						var rc = yearStore.getAt(i);
						html.push('<tr><td>' + rc.get('finaceName') 
								+ '</td><td>'+ rc.get('itemAlias') 
								+ '</td><td>' + rc.get('totalFact')
								+ '</td><td>' + rc.get('yearBudget') 
								+ '</td><td>'+ rc.get('complatePercent') 
								+ '</td><td>' + rc.get('itemContent')
								+ '</td><td>' + rc.get('itemExplain') 
								+ '</td><td>');
					}
					html.push('</table>');
					html = html.join(''); // 最后生成的HTML表格
					tableToExcel(html);
				}
			})
		}

	}
})