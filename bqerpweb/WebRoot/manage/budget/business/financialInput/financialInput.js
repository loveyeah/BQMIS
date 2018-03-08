Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	var flagBudgetItemId = null;
	var flagCenterId = null;
	var flagBudgetTime = null;
	var flagItemId = null;
	var flagFillBy = null;
	var flagCenterCode = null;
	var flagCenterName = null;
	var flagItemCode = null;
	var flagItemName = null;
	var flagFillName = null;
	var deptCode = null;
	var deptName = null;
	
	var flagDateType = 1;
	var obj;
	
			
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
			
	// 预算部门
	var budgetDept = new Ext.form.TextField({
		fieldLabel : '预算部门',
		readOnly : true,
		width : 90,
		value : deptName
	})
	// 从session取登录人编码姓名部门相关信息
	function getWorkerCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号，赋给全局变量
							flagFillBy = result.workerCode;
							flagFillName = result.workerName;
							deptCode = result.deptCode;
							deptName = result.deptName;
							budgetDept.setValue(deptName);
							queryWestRec();
						}
					}
				});
	}
	getWorkerCode();
//	*****************以下未处理******************************
			var westsm = new Ext.grid.CheckboxSelectionModel({
						singleSelect : true
					});
			// 左边列表中的数据
			var westdatalist = new Ext.data.Record.create([{
						name : 'budgetItemId'
					}, {
						name : 'itemAlias'
					}, {
						name : 'centerItemId'
					}, {
						name : 'centerId'
					}, {
						name : 'topicId'
					}, {
						name : 'budgetTime'
					}, {
						name : 'itemId'
					}, {
						name : 'budgetMakeId'
					}
//					, {
//						name : 'forecastBudget'
//					}, {
//						name : 'adviceBudget'
//					}, {
//						name : 'adjustBudget'
//					}, {
//						name : 'budgetAdd'
//					}, {
//						name : 'budgetChange'
//					}, {
//						name : 'judgeBudget'
//					}
					, {
//						
						name : 'ensureBudget'
					}, {
						name : 'factHappen'
					}, {
//						
						name : 'financeHappen'
					}
//					, {
//						name : 'budgetBasis'
//					}, {
//						name : 'budgetStatus'
//					}
					, {
						name : 'centerCode'
					}, {
						name : 'centerName'
					}, {
						name : 'itemCode'
					}, {
						name : 'itemName'
					}, {
//						
						name : 'deptItemAlias'
					}, {
//						
						name : 'dataSource'
					}, {
						name : 'dispalyNo'
					}]);

			var westgrids = new Ext.data.JsonStore({
						url : 'managebudget/getDeptBudetDetails.action',
						root : 'list',
						totalProperty : 'totalCount',
						fields : westdatalist
					});

		
	
	// 预算时间
	var monthTime = new Ext.form.TextField({
				fieldLabel : '预算时间',
				readOnly : true,
				id : 'monthTime',
				width : 80,
				style : 'cursor:pointer',
				value : getCurrentMonth(),
				listeners : {
					focus : function() {
						WdatePicker({
									// 时间格式
									dateFmt : 'yyyy-MM',
									alwaysUseStartDate : false
								});

					}

				}

			});			
				// 左边列表
			var westgrid = new Ext.grid.EditorGridPanel({
						autoScroll : true,
						ds : westgrids,
						tbar :['预算部门：',budgetDept,'-','预算时间：',monthTime,'-',
							{
									id : 'btnAdd',
									iconCls : 'query',
									text : "查询",
									handler : queryWestRec
								},'-',{
							id : 'btnSave',
							iconCls : 'save',
							text : "保存",
							handler : saveRecord
						}],
						columns : [westsm, new Ext.grid.RowNumberer({
											header : "行号",
											width : 31
										}), {
									header : "预算指标",
									width : 110,
									align : "center",
									sortable : true,
									dataIndex : 'deptItemAlias'
								}, {
									header : "预算值（万）",
									 width:120,
									align : "center",
									sortable : true,
									dataIndex : 'ensureBudget'
								}, {
									header : "财务发生值（万）",
									 width:120,
									align : "center",
									sortable : true,
									dataIndex : 'financeHappen',
									css : CSS_GRID_INPUT_COL,
									editor : new Ext.form.NumberField({
										allowNegative : false,
										decimalPrecision : 6
									})
								}],
						sm : westsm,
						frame : true,
						bbar : new Ext.PagingToolbar({
									pageSize : 18,
									store : westgrids,
									displayInfo : true,
									displayMsg : "{0} 到 {1} /共 {2} 条",
									emptyMsg : "没有记录"
								}),
						clicksToEdit : 1,
						border : true
					});
	 westgrid.on('beforeedit', function(obj) {
	 	if(obj.record.get('dataSource') == '1'){
	 		if (obj.field == 'financeHappen') {
    			return true;
    		}
	 	}
	 	if(obj.record.get('dataSource') == '2'){
	 		if (obj.field == 'financeHappen') {
    			return false;
    		}
	 	}
    		
    	return true;
    });
	function queryWestRec() {
		westgrids.baseParams = {
			deptCode : deptCode,
			budgetTime : monthTime.getValue()
		}
		westgrids.load({
					params : {
						start : 0,
						limit : 18
					}
				})
	}
	function saveRecord() {
		if(westgrids.getTotalCount() == 0)
		{
			Ext.Msg.alert('提示信息','无数据进行保存！');
			return;
		}
		Ext.Msg.confirm('提示信息','确认要保存吗？',function(id){
			if(id == 'yes')
			{
				var modified = new Array();
				for(var i = 0 ;i < westgrids.getTotalCount(); i++)
				{
					modified.push(westgrids.getAt(i).data)
				}
				Ext.Ajax.request({
					url : 'managebudget/saveFinancialModified.action',
					method : 'post',
					params : {
						modified : Ext.util.JSON.encode(modified)
					},
					success : function(result, request){
						Ext.Msg.alert('提示信息','数据保存成功！');
						westgrids.reload();
					},
					failure : function(result, request){
						Ext.Msg.alert('提示信息','数据保存失败！');
						westgrids.reload();
					}
				})
			}
		})
	}
// *************************以上为经济指标录入右边部分****************************

	new Ext.Viewport({
						enableTabScroll : true,
						layout : "border",
						items : [
							{
									region : 'center',
									layout : 'fit',
									width : 390,
									items : [westgrid]
								}
								]
					});
					
});