Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	

	function numberFormat(value) {
		value = String(value);
		if (value == null || value == "null") {
			value = "0";
		}
		if(value == '利息')
		{
			return null;
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
		if(value == '利息')
		{
			return null;
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
	function getCurrentYear()
	{
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString();		
		return s;
	}
		// 年度
	var year = new Ext.form.TextField({
				// fieldLabel : '时间',
				readOnly : true,
				width : 80,
				id : 'year',
				style : 'cursor:pointer',
				value : getCurrentYear(),
				listeners : {
					focus : function() {
						WdatePicker({
									// 时间格式
									dateFmt : 'yyyy',
									alwaysUseStartDate : false
								});
						this.blur();
					}
				}
			});
	// 费用store
	var feeStore = new Ext.data.SimpleStore({
		fields : ['id','name'],
		data : [['1','长期借款'],['2','短期借款']]
	})
	var fee = new Ext.form.ComboBox({
			store : feeStore,
			displayField : 'name',
			valueField : 'id',
			mode : 'local',
			triggerAction : 'all',
			readOnly : true,
			allowBlank : false,
			width : 100,
			value : 1
	})
	var item = Ext.data.Record.create([{
		name : 'financeId'
	},{
		name : 'budgetTime'
	}, {
		name : 'financeType'
	}, {
		name : 'workFlowNo'
	}, {
		name : 'workFlowStatus'
	}, {
		name : 'financeDetailId'
	},{
		name : 'loanName'
	},{
		name : 'lastLoan'
	},{
		name : 'newLoan'
	},{
		name : 'repayment'
	},{
		name : 'balance'
	},{
		name : 'interest'
	},{
		name : 'memo'
	},{
		name : 'isUse'
	}
	]);

	
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false

	});
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managebudget/getFinanceDetailList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalCount : "totalCount",
			root : "list"
		}, item)
	});

	ds.on('load',function(){
		var obj;
		var currentIndex = ds.getCount();
			var lastLoanCount = 0;
			var newLoanCount = 0;
			var repaymentCount = 0;
			var balanceCount = 0;
			for(var i = 0; i <= ds.getTotalCount() - 1; i++)
			{
				lastLoanCount += ds.getAt(i).get('lastLoan');
				newLoanCount += ds.getAt(i).get('newLoan');
				repaymentCount += ds.getAt(i).get('repayment');
				balanceCount += ds.getAt(i).get('balance');
			}
			obj = new item({
//				  用以标记合计行
						'financeId' : null,
						'loanName' : '合计',
						'lastLoan' : lastLoanCount,
						'newLoan' : newLoanCount,
						'repayment' : repaymentCount,
						'balance' : balanceCount,
						'interest' : '利息',
						'memo' : null
					});			
//		}
		Grid.stopEditing();
		ds.add(obj);
	});
	// 事件状态
	var cm = new Ext.grid.ColumnModel([sm,
		new Ext.grid.RowNumberer({
			header : "行号",
			width : 31
			}),
	{
		header : '借款名称',
		dataIndex : 'loanName',
		width : 110,
//		css : CSS_GRID_INPUT_COL,
		editor : new Ext.form.TextField({})
	}, {
		header : '去年年末余额',
		dataIndex : 'lastLoan',
		align : 'right',
		width : 90,
//		css : CSS_GRID_INPUT_COL,
		editor : new Ext.form.NumberField({
							allowBlank : false,
							allowDecimal : true,
							allowNegative : false,
							decimalPrecision : 2
						}),
		renderer : function(value) {
			return numberFormat(value);
		}
	}, {
		header : '本年新增借款',
		dataIndex : 'newLoan',
		align : 'right',
		width : 90,
//		css : CSS_GRID_INPUT_COL,
		editor : new Ext.form.NumberField({
							allowBlank : false,
							allowDecimal : true,
							allowNegative : false,
							decimalPrecision : 2
						}),
		renderer : function(value) {
			return numberFormat(value);
		}
	}, {
		header : '本年到期还款',
		dataIndex : 'repayment',
		align : 'right',
		width : 90,
//		css : CSS_GRID_INPUT_COL,
		editor : new Ext.form.NumberField({
							allowBlank : false,
							allowDecimal : true,
							allowNegative : false,
							decimalPrecision : 2
						}),
		renderer : function(value) {
			return numberFormat(value);
		}
	}, {
		header : "本年年末余款",
		width : 90,
		dataIndex : 'balance',
		align : 'right',
		renderer : function(value) {
			return numberFormat(value);
		}
	}, {
		header : '本年支付利息',
		dataIndex : 'interest',
		align : 'right',
		width : 90,
//		css : CSS_GRID_INPUT_COL,
		editor : new Ext.form.NumberField({
							allowBlank : false,
							allowDecimal : true,
							allowNegative : false,
							decimalPrecision : 3
						})
						,
		renderer : function(value) {
			return numberFormat(value);
		}
	},{
			header : '备注',
			width : 130,
			align : 'left',
			sortable : true,
//			css : CSS_GRID_INPUT_COL,
			editor : new Ext.form.TextArea({}),
			dataIndex : 'memo'
		}
	]);
		
	var gridbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : ds,
		displayInfo : true,
		displayMsg : "显示第{0}条到{1}条，共{2}条",
		emptyMsg : "没有记录"
	});
	// 增加
	function addDetail() {
		if(ds.getTotalCount() > 0)
		{
			if(ds.getAt(0).get('budgetTime') != year.getValue())
			{
				Ext.Msg.alert('提示信息','请先查询数据！');
				return;
			}
			if(ds.getAt(0).get('financeType') != fee.getValue())
			{
				Ext.Msg.alert('提示信息','请先查询数据！');
				return;
			}
			if(ds.getAt(0).get('workFlowStatus') == '1' || ds.getAt(0).get('workFlowStatus') == '2')
			{
				Ext.Msg.alert('提示信息','审批中和审批通过的数据不允许增加！')
				return;
			}
		}
		var count = ds.getCount();
		var currentIndex = count;
		var o;
		if (count == 0) {
			o = new item({
						'financeId' : null,
						'budgetTime' : year.getValue(),
						'financeType' : fee.getValue(),
						'workFlowNo' : ds.getAt(0).get('workFlowNo'),
						'workFlowStatus' : ds.getAt(0).get('workFlowStatus'),
						'financeDetailId' : null,
						'loanName' : null,
						'lastLoan' : 0,
						'newLoan' : 0,
						'repayment' : 0,
						'balance' : 0,
						'interest' : 0,
						'memo' : null
					});
		}
		else{
			o = new item({
						'financeId' : ds.getAt(0).get('financeId'),
						'budgetTime' : year.getValue(),
						'financeType' : fee.getValue(),
						'workFlowNo' : ds.getAt(0).get('workFlowNo'),
						'workFlowStatus' : ds.getAt(0).get('workFlowStatus'),
						'financeDetailId' : null,
						'loanName' : null,
						'lastLoan' : 0,
						'newLoan' : 0,
						'repayment' : 0,
						'balance' : 0,
						'interest' : 0,
						'memo' : null
					});			
		}
		Grid.stopEditing();
		ds.insert(currentIndex -1, o);
		Grid.getView().refresh();
		sm.selectRow(currentIndex - 1);
		Grid.startEditing(currentIndex - 1, 1);
	}

	// 删除记录
	var ids = new Array();
	function deleteDetail() {
		if(ds.getTotalCount() > 0)
		{
			if(ds.getAt(0).get('budgetTime') != year.getValue())
			{
				Ext.Msg.alert('提示信息','请先查询数据！');
				return;
			}
			if(ds.getAt(0).get('financeType') != fee.getValue())
			{
				Ext.Msg.alert('提示信息','请先查询数据！');
				return;
			}
			if(ds.getAt(0).get('workFlowStatus') == '1' || ds.getAt(0).get('workFlowStatus') == '2')
			{
				Ext.Msg.alert('提示信息','审批中和审批通过的数据不允许删除！')
				return;
			}
		}
		Grid.stopEditing();
		var sm = Grid.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if(member.get('loanName') != '合计')
				{
					if (member.get("financeDetailId") != null) {
					ids.push(member.get("financeDetailId"));
				}				
				Grid.getStore().remove(member);
				Grid.getStore().getModifiedRecords().remove(member);
				}			
			}
		}
	}
	// 保存
	function saveDetail() {
		if(ds.getTotalCount() > 0)
		{
			if(ds.getAt(0).get('budgetTime') != year.getValue())
			{
				Ext.Msg.alert('提示信息','请先查询数据！');
				return;
			}
			if(ds.getAt(0).get('financeType') != fee.getValue())
			{
				Ext.Msg.alert('提示信息','请先查询数据！');
				return;
			}
			if(ds.getAt(0).get('workFlowStatus') == '1' || ds.getAt(0).get('workFlowStatus') == '2')
			{
				Ext.Msg.alert('提示信息','审批中和审批通过的数据不允许保存修改！')
				return;
			}
		}
		Grid.stopEditing();
		var modifyRec = Grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			for (var i = 0; i < modifyRec.length; i++) {
				if (modifyRec[i].data.loanName == null
						|| modifyRec[i].data.loanName == "") {
					Ext.Msg.alert('提示信息', '借款名称不可为空，请输入！')
					return;
				}
			}
			Ext.Msg.confirm('提示信息', '确定要保存修改吗？', function(button) {
				if (button == 'yes') {
					var addData = new Array();
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
							if (modifyRec[i].get('isUse') == 'Y') {
								updateData.push(modifyRec[i].data);
							} else if (modifyRec[i].get('isUse') != 'Y' && modifyRec[i].get('loanName') != '合计'){
								addData.push(modifyRec[i].data);
							}
					}
					Ext.Ajax.request({
								url : 'managebudget/saveFinanceDetailModified.action',
								method : 'post',
								params : {
									isAdd : Ext.util.JSON.encode(addData),
									isUpdate : Ext.util.JSON.encode(updateData),
									ids : ids.join(",")
								},
								success : function(result, request) {
//									var o = eval('(' + result.responseText
//											+ ')');
									Ext.Msg.alert("提示信息", "数据保存修改成功！");
										ds.rejectChanges();
										ids = [];
										ds.reload();
								},
								failure : function(result, request) {
									// var o = eval("(" + result.responseText
									// + ")");
									Ext.Msg.alert("提示信息", "数据保存修改失败！");
									ds.rejectChanges();
									ids = [];
									ds.reload();
								}
							})
				}
			})

		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}
	// 取消
	function cancerDetail() {
		var modifyRec = ds.getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			Ext.Msg.confirm('提示信息', '确定要放弃修改吗？', function(button) {
						if (button == 'yes') {
							ds.reload();
							ds.rejectChanges();
							ids = [];
						} 
					})
		}
		else
		ds.reload()
	}
	function fuzzyQuery() {
		if(year.getValue() == null || year.getValue() == "")
		{
			Ext.Msg.alert('提示信息','年度不可为空，请先选择！');
			return;
		}
		ds.baseParams = {
			budgetTime : year.getValue(),
			financeType : fee.getValue()
		};
		ds.load({
			params : {
//				start : 0,
//				limit : 18
			}
		});
		ids = [];
	};
	
	
	// tbar
	var contbar = new Ext.Toolbar({
		items : ['年度:',year,'-','费用:',fee,'-',{
			id : 'query',
			iconCls : 'query',
			text : "查询",
			handler : fuzzyQuery
		},'-',{
			id : 'btnAdd',
			iconCls : 'add',
			text : "新增",
			handler : addDetail
		}, '-',{
			id : 'btnDelete',
			iconCls : 'delete',
			text : "删除",
			handler : deleteDetail

		},'-', {
			id : 'btnCancer',
			iconCls : 'cancer',
			text : "取消",
			handler : cancerDetail

		}, '-', {
			id : 'btnSave',
			iconCls : 'save',
			text : "保存修改",
			handler : saveDetail
		},'-',{
		text : '上报',
		iconCls : 'upcommit',
		handler : reportRec			
		},'-',{
			text : '审批查询',
			iconCls : 'reflesh',
			handler : judgeQuery
	}]

	});
	var Grid = new Ext.grid.EditorGridPanel({
		sm : sm,
		ds : ds,
		cm : cm,
		autoScroll : true,
//		bbar : gridbbar,
		tbar : contbar,
		border : true,
		clicksToEdit : 1,
		viewConfig : {
		// forceFit : true
		}
	});
	
	 Grid.on('beforeedit', function(obj) {
	 	if(obj.row == ds.getCount() -1)
	 	{
	 		return false;
	 	}
	 	if (obj.record.get('workFlowStatus') == 1
						|| obj.record.get('workFlowStatus') == 2) {
					return false;
				}
//	 	if(obj.record.get('loanName') == '合计'){
//    			return false;
//    		}  		
    	return true;
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
  function reportRec()
  {
  	if (ds.getTotalCount() > 0) {
  		if(ds.getAt(0).get('budgetTime') != year.getValue())
			{
				Ext.Msg.alert('提示信息','请先查询数据！');
				return;
			}
			if(ds.getAt(0).get('financeType') != fee.getValue())
			{
				Ext.Msg.alert('提示信息','请先查询数据！');
				return;
			}
			if (ds.getAt(0).get('workFlowStatus') == '1'
					|| ds.getAt(0).get('workFlowStatus') == '2') {
				Ext.Msg.alert('提示信息', '审批中和审批通过的数据不允许上报！')
				return;
			}

		
		
		var url = "../financeApprove/report.jsp";
			var args = new Object();
			args.entryId = ds.getAt(0).get('workFlowNo');
			args.workflowType = "budgetDetailApprove";
			args.budgetGatherId =ds.getAt(0).get('financeId');
			var obj = window.showModalDialog(url, args,
					'status:no;dialogWidth=750px;dialogHeight=550px');
			// 按钮设为不可用
			if(obj)
			{		
			      fuzzyQuery();
		    }
	}
	else{
		Ext.Msg.alert('提示信息','无数据可上报！');
		return;
	}
  	
  }
  function judgeQuery()
  {
  	if (ds.getTotalCount() > 0) {
  		if(ds.getAt(0).get('budgetTime') != year.getValue())
			{
				Ext.Msg.alert('提示信息','请先查询数据！');
				return;
			}
			if(ds.getAt(0).get('financeType') != fee.getValue())
			{
				Ext.Msg.alert('提示信息','请先查询数据！');
				return;
			}
		
			var entryId=ds.getAt(0).get('workFlowNo');
			if(entryId==null||entryId=="")
			{
			  Ext.Msg.alert("提示","无审批信息！");	
			  return ;
			}
			var url="../../../../workflow/manager/show/show.jsp?entryId="+entryId;
			window.open(url);
			
		}else{
		Ext.Msg.alert('提示信息','无数据进行审批查询！');
		return;
	}
  
  }
  
  fuzzyQuery();
})