Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function(){
	
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
	
	function getCurrentYear()
	{
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString();		
		return s;
	}
	// 系统当前月
	function getMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString() + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t ;
		return s;
	}
	
	// 从session取登录人编码姓名部门相关信息
//	function getWorkerCode() {
//		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
//					success : function(action) {
//						var result = eval("(" + action.responseText + ")");
//						if (result.workerCode) {
//							// 设定默认工号，赋给全局变量
////							deptCode = result.deptCode;
////							deptName = result.deptName;
//							budgetDept.setValue(deptName);
//							queryRec();
//						}
//					}
//				});
//	}
//	getWorkerCode();
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
	// 预算主题
	var topicH = new Ext.form.Hidden({
		id : 'topicH',
		name : 'topicH'
	})
	var topic = new Ext.form.TextField({
		id : 'topic',
		fieldLabel : '主题',
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
				var url = "../../maint/themeSelect.jsp";
				var theme = window
						.showModalDialog(
								url,
								args,
								'dialogWidth:500px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;')
				if (typeof(theme)!= "undefined") {
					topicH.setValue(theme.topicId);
					topic.setValue(theme.topicName);
				}
				this.blur();
			}
		}
	})
	var formatType;
	var yearRadio = new Ext.form.Radio({
			id : 'year',
			name : 'queryWayRadio',
			hideLabel : true,
			boxLabel : '年份'	
		});
	var monthRadio = new Ext.form.Radio({
			id : 'month',
			name : 'queryWayRadio',
			hideLabel : true,
			boxLabel : '月份',
			checked : true,
			listeners : {
				check : function() {
					var queryType = getChooseQueryType();
					switch (queryType) {
						case 'year' : {
							formatType = 1;
							time.setValue(getCurrentYear());
							break;
						}
						case 'month' : {
							time.setValue(getMonth());
							formatType = 2;
							break;
						}
					}
				}
			}
		});	
	var time = new Ext.form.TextField({
		id : 'time',
		allowBlank : true,
		// value : getDate(),
		width : 100,
		readOnly : true,
		listeners : {
			focus : function() {
				var format = '';
				if(formatType == 1)
					format = 'yyyy';
				if(formatType == 2)
					format = 'yyyy-MM';
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : format,
					alwaysUseStartDate : true,
					onclearing : function() {
										planStartDate.markInvalid();
									}
									
				});
			}
		}
	});
	//遍历所有的REDIO获得ID
	function getChooseQueryType() {
		var list = document.getElementsByName("queryWayRadio");
		for (var i = 0; i < list.length; i++) {
			if (list[i].checked) {
				return list[i].id;
			}
		}
	}
	
	var query = new Ext.Button({
		id : 'btnAdd',
		iconCls : 'query',
		text : "查询",
		handler : queryRec
	})
	var expor = new Ext.Button({
		id : 'btnExport',
		iconCls : 'export',
		text : "导出",
		handler : exportRec
	})

	// sm
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	})
	// data
	var record = new Ext.data.Record.create([{
		name : 'centerId'
	},{
		name : 'topicId'
	},{
		name : 'budgetTime'
	},{
		name : 'itemId'
	},{
		name : 'itemAlias'
	},{
		name : 'unitName'
	},{
		name : 'forecastBudget'
	},{
		name : 'adviceBudget'
	},{
		name : 'budgetAdd'
	},{
		name : 'ensureBudget'
	},{
		name : 'factHappen'
	},{
		name : 'financeHappen'
	},{
		name : 'itemCode'
	}])
	
	//  store
	var store = new Ext.data.JsonStore({
		url : 'managebudget/getComplexBudgetList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : record 
	})
	
	var grid = new Ext.grid.GridPanel({
		id : 'grid',
		frame : false,
		border : false,
		autoScorll : true,
		sm : sm,
		ds : store,
		region : 'center',
	    layout : 'fit',
		columns : [sm,new Ext.grid.RowNumberer({
			header : '行号',
			width : 31
		}),{
			header : '预算科目',
			width : 150,
			align : 'left',
			sortable : true,
			dataIndex : 'itemAlias',
			//add by ltong 分层次显示 按itemCode（zbbmtx_code）排序
			renderer : function(value, cellmeta, record, rowIndex, columnIndex, store){
						var level=0;
						if(record.get("itemCode")!=null||record.get("itemCode")!="")
						{
							level= (record.get("itemCode").length/3)-2;
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
			header : '计量单位',
			width : 110,
			align : 'center',
			sortable : true,
			dataIndex : 'unitName'
		},{
			header : '建议预算',
			width : 80,
			align : 'center',
			sortable : true,
			dataIndex : 'adviceBudget'
		},{
			header : '预算变更',
			width : 80,
			align : 'center',
			sortable : true,
			dataIndex : 'budgetAdd'
		},{
			header : '审定预算',
			width : 80,
			align : 'center',
			sortable : true,
			dataIndex : 'ensureBudget'
		},{
			header : '实际发生',
			width : 120,
			align : 'center',
			sortable : true,
			dataIndex : 'factHappen'
		},{
			header : '财务发生',
			width : 130,
			align : 'center',
			sortable : true,
			hidden:true,
			dataIndex : 'financeHappen'
		}]
//		,
//		bbar : new Ext.PagingToolbar({
//									pageSize : 18,
//									store : store,
//									displayInfo : true,
//									displayMsg : "第{0} 条到第 {1} 条/共 {2} 条",
//									emptyMsg : "没有记录"
//								})
	})	
	
	var panel = new Ext.Panel({
		id : 'panel',
		layout : "border",
		autoScorll : true,
		frame : false,
		border : false,
		tbar : ['部门：',budgetDept,'-','主题:',topic,
		yearRadio,monthRadio,time,
		'-',query,'-',expor],
		items : [{
			region : 'center',
			layout : 'fit',
			border : false,
			frame : false,		
			items : [grid]
		}]
	})		
	
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
//		if(Ext.get('topic').dom.value == null || Ext.get('topic').dom.value == "")
//		{
//			Ext.Msg.alert('提示信息','请先选择主题！')
//			return;
//		}
		if(Ext.get("time").dom.value == null || Ext.get("time").dom.value == "")
		{
			Ext.Msg.alert('提示信息','请先选择时间！')
			return;
		}
			store.baseParams = {
				centerId : deptH.getValue(),
				topicId : topicH.getValue(),
				budgetTime : time.getValue()
			}
			store.load({
						params : {
							start : 0,
							limit : 18
						}
					})
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
			if (store.getTotalCount() == 0) {
				Ext.Msg.alert('提示信息', '无数据进行导出！');
				return;
			}
			Ext.Msg.confirm('提示信息', '确认要导出数据吗？', function(id) {
				if (id == 'yes') {					
					var html = ['<table border=1><tr><th>预算科目</th><th>计量单位</th><th>建议预算</th><th>预算变更</th>' 
					+'<th>审定预算</th><th>实际发生</th><th>财务发生</th></tr>'];
					for (var i = 0; i < store.getTotalCount(); i += 1) {
						var rc = store.getAt(i);
						html.push('<tr><td>' + rc.get('itemAlias')
								+ '</td><td>' + rc.get('unitName') 
								+ '</td><td>'+ rc.get('adviceBudget') 
								+ '</td><td>' + rc.get('budgetAdd')
								+ '</td><td>' + rc.get('ensureBudget') 
								+ '</td><td>'+ rc.get('factHappen') 
								+ '</td><td>' + rc.get('financeHappen')
								+ '</td><td>' );
					}
					html.push('</table>');
					html = html.join(''); // 最后生成的HTML表格
					tableToExcel(html);
				}
			})
	}
})