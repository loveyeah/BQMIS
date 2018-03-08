Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	var m = new Map(); //add by fyyang 20100409 
	var grid;
	var ds ;
	// canSave 为1，则可以保存；为0，则不可以保存
	var canSave = 1;
	var isSaved = true;
	// 创建一个对象
	var obj = new Ext.data.Record.create([{
		// 预算汇总ID
		name : 'budgetGatherId'
	},{
		// 预算主题
		name : 'topicId'
	},{
		// 预算时间
		name : 'budgetTime'
	},{
		// 汇总人
		name : 'gatherBy'
	},{
		// 工作流序号
		name : 'gatherWorkFlowNo'
	},{
		// 汇总状态
		name : 'gatherStatus'
	},{
		// 部门编码
		name : 'centerId'
	},{
		// 指标编码
		name : 'itemId'
	},{
		// 建议预算
		name : 'adviceBudget'
	},{
		// 数据来源
		name : 'dataSource'
	},{
      // 编制单id
		name : 'budgetMakeId'
	}])
	
	// 系统当前时间
	function getMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString() + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t ;
		return s;
	}
	function getDate(){
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString()
		return s;
	}
	// 从session取登录人编码姓名部门相关信息
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号，赋给全局变量
							fillBy.setValue(result.workerCode);
//							fillName.setValue(result.workerName);

						}
					}
				});
	}
	function query(){
		getWorkCode();
		Ext.Msg.wait("正在查询数据!请等待...");
		Ext.Ajax.request({
			url : 'managebudget/findBudgetGatherList.action',
			params : {
				topicId : '2',
				budgetTime : time.getValue()
			},
			method : 'post',
			waitMsg : '正在加载数据...',
			success : function(result, request) {
				var str ="{'data':" +
						"[{'itemName':'#2发电量','itemId' : '3','unitName':'兆瓦时','null':'0','null':'0','companyBudget' : 0.0,'budgetGatherId' : null,'topicId' : 23,'budgetTime' : '2009-08' }" +
						",{'itemName':'#1发电量','itemId' : '2','unitName':'兆瓦时','null':'0','null':'0','companyBudget' : 0.0,'budgetGatherId' : null,'topicId' : 23,'budgetTime' : '2009-08' }]" +
						",'columModel':[{'header' : '预算指标','width':100,'dataIndex' : 'itemName','align':'left'},{'header' : '计量单位','width':100,'dataIndex' : 'unitName','align':'right'},{'header' :'未分配','width': 100,'dataIndex': '4','align':'right','editor':new Ext.form.NumberField({allowDecimals : true,decimalPrecision : 0})},{'header' :'设备部','width': 100,'dataIndex': '10','align':'right','editor':new Ext.form.NumberField({allowDecimals : true,decimalPrecision : 0})},{'header' : '公司预算','width':100,'dataIndex' : 'companyBudget','align':'right'}],'fieldsNames' : [{'name':'itemName'},{'name':'unitName'},{'name':'4'},{'name':'10'}]}"
				
				var json = eval('(' + result.responseText + ')');
//				var json = eval('(' + str + ')');
				document.getElementById("gridDiv").innerHTML = "";
				if(json.data!="[]")
				{
				
				ds = new Ext.data.JsonStore({
					data :json.data,
					fields : Ext.decode(json.fieldsNames)
				});
				
				}
				else
				{
					ds = new Ext.data.JsonStore({
					data :[],
					fields : Ext.decode(json.fieldsNames)
				});
				}
				
//				ds = new Ext.data.JsonStore({
//					data : json.data,
//					fields : Ext.decode(json.fieldsNames)
//				});
				var wd = Ext.get('gridDiv').getWidth()
				var ht = Ext.get('gridDiv').getHeight()	
				var sm = new Ext.grid.CellSelectionModel({});
				// add by wpzhu 20100609----------------------
				if(ds.getCount()>0)
				{
				if(ds.getAt(0).get("gatherStatus")=='2')
				{
					 btnsave.setDisabled(true)
                     btnupcommit.setDisabled(true);
				}else
				{
					btnsave.setDisabled(false)
                     btnexport.setDisabled(false);
                     btnupcommit.setDisabled(false);
				}
				}else
				{
					btnsave.setDisabled(true)
                     btnexport.setDisabled(true);
                     btnupcommit.setDisabled(true);
				}
	//---------------------------------------------------------------	
				grid = new Ext.grid.EditorGridPanel({
					renderTo : 'gridDiv',
					stripeRows: true, 
					id : 'grid',
					split : true,
					width : wd,
					height : ht,
					autoScroll : true,
					border : false, 
					sm : sm,
					cm : new Ext.grid.ColumnModel(
						{
						columns : Ext.decode(json.columModel)
					}),
					enableColumnMove : false,  
					//plugins : [new Ext.ux.plugins.GroupHeaderGrid(null,json.rows)],
					ds : ds,
					clicksToEdit : 1
				});  
//				grid.rows = json.rows;
				grid.render();
				canSave=1;
				for (var j = 2; j <= grid.getColumnModel().getColumnCount() - 2; j++)
				{
					// 用以记录该列中值不为空的数目
					var numCount = 0;
					for (var i = 0; i <= ds.getTotalCount() - 1; i++)
					{
						var hearder = grid.getColumnModel().getDataIndex(j);
						var advice = grid.getStore().getAt(i).get(hearder);
						if (advice != null && (advice != ""||advice =='0')) 
						{
							numCount++;
						}
					}
					
					if(numCount > 0)
						canSave = 1;
					else 
					{
						canSave = 0;
						break;
					}
				}
				ds.on('update',function(ds,record,edit){
					isSaved = false;
				});

					if (ds != null && ds.getTotalCount() > 0) {
					if (ds.getAt(0).get('gatherStatus') == null
							|| ds.getAt(0).get('gatherStatus') == "")
						isSaved = false;
				}
				grid.on('beforeedit', function(e) {
							// true可以编辑，false 不可编辑
							if (e.record.get('gatherStatus') != 1
									&& e.record.get('gatherStatus') != 2) {
								var columnCount = e.grid.getColumnModel()
										.getColumnCount();
								var rowCount = e.grid.getStore()
										.getTotalCount();
								var item = e.record.data.itemId;
								var topic = e.record.data.topicId;
								var center = e.grid.getColumnModel()
										.getDataIndex(e.column);

								var conn = Ext.lib.Ajax.getConnectionObject().conn;
								conn.open("POST",
										'managebudget/judgeToEdit.action?itemId='
												+ item + '&topicId=' + topic
												+ '&centerId=' + center, false);
								conn.send(null);
								if (conn.status == "200") {
									var result = eval('(' + conn.responseText
											+ ')');

									if (result.msg == "1") {
										return true;
									} else {
										return false;
									}
								}
							} else {
								return false;
							}

						})
				grid.on('afteredit', function(e){
					var v1 = new Object();
							v1.centerId = e.field;
							v1.itemId = e.record.get("itemId");
							v1.adviceBudget = e.record.get(e.field);
							
							v1.budgetGatherId = e.record.get("budgetGatherId");
						v1.topicId = e.record.get("topicId");
						v1.budgetTime = e.record.get("budgetTime");
						v1.gatherBy = fillBy.getValue();
						v1.gatherWorkFlowNo = e.record.get("gatherWorkFlowNo");
						v1.gatherStatus = e.record.get("gatherStatus");
						v1.dataSource = null,
						v1.budgetMakeId = e.record.get("budgetMakeId");
						m.put(e.field + " " + e.record.get("itemId"), v1);
						});
				Ext.Msg.hide();
			},
			failure : function(result, request) {
				Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
				Ext.Msg.hide();
			}
		});
	}
	var formatType;
	var yearRadio = new Ext.form.Radio({
			id : 'year',
			name : 'queryWayRadio',
			hideLabel : true,
			boxLabel : '年份',
			checked : true,
			listeners : {
				check : function() {
					var queryType = getChooseQueryType();
					switch (queryType) {
						case 'year' : {
							formatType = 1;
							time.setValue(getDate());
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
	var monthRadio = new Ext.form.Radio({
			id : 'month',
			name : 'queryWayRadio',
			hideLabel : true,
			boxLabel : '月份'
		});
	
	
	var time = new Ext.form.TextField({
		id : 'time',
		allowBlank : true,
		readOnly : true,
		// value : getDate(),
		width : 100,
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
	
	// 查询按钮
	var btnquery = new Ext.Button({
		text : '查询',
		iconCls : 'query',
		handler : function(){
			query();
		}
	});


	// 保存按钮
	var btnsave = new Ext.Button({
		text : '保存',
		iconCls : 'save',
		handler : function(){
			saveRecords()
		}
	});

	// 报送按钮
	var btnupcommit = new Ext.Button({
		text : '汇总确认',
		iconCls : 'upcommit',
		handler : function(){
			reportRecord();
			
		}
	});
//	// 审批查询按钮
//	var btnapprovel = new Ext.Button({
//		text : '审批查询',
//		iconCls : 'reflesh',
//		handler : function() {
//			judgeQuery()
//		}
//	});
	var btnexport = new Ext.Button({
		text : '导出',
		iconCls : 'export',
		handler : function() {
			exportRecord();
		}
	})
	
	// 汇总人
	var fillBy = new Ext.form.Hidden({
		id : 'fillBy',
		name : 'fillName'
	})
	// 设定布局器及面板
	var view = new Ext.Viewport({
				layout : 'fit',
				margins : '0 0 0 0',
				collapsible : true,
				split : true,
				border : false,
				items : [new Ext.Panel({
							id : 'panel',
							border : false,
							tbar : [monthRadio,yearRadio,{
									xtype : "tbseparator"
								},time,{
									xtype : "tbseparator"
								},btnquery, {
									xtype : "tbseparator"
								}, btnsave, {
									xtype : "tbseparator"
								}, btnupcommit,{
									xtype : "tbseparator"
								},btnexport,fillBy],
							items : [{
										html : '<div id="gridDiv"></div>'
									}]
						})]
			});
	Ext.get('gridDiv').setWidth(Ext.get('panel').getWidth());
	Ext.get('gridDiv').setHeight(Ext.get('panel').getHeight() - 25);
	//遍历所有的REDIO获得ID
	function getChooseQueryType() {
		var list = document.getElementsByName("queryWayRadio");
		for (var i = 0; i < list.length; i++) {
			if (list[i].checked) {
				return list[i].id;
			}
		}
	}
	
	function saveRecords() {
		var modifyRec = grid.getStore().getModifiedRecords();
		
		//if (ds == null || ds.getTotalCount() == 0) {
		if(modifyRec.length==0){
			Ext.Msg.alert('提示信息', '无数据进行保存！')
			return;
		} 
		if(canSave == 0)
		{
			Ext.Msg.alert('提示信息','数据中有编制审批未通过的数据，不可保存！')
			return;
		}
		grid.stopEditing();
			for (var i = 0; i <= ds.getTotalCount() - 1; i++) {
				if(ds.getAt(i).get('gatherStatus') != null)
					if (ds.getAt(i).get('gatherStatus') == 1
							|| ds.getAt(i).get('gatherStatus') == 2)
							{
							Ext.Msg.alert('提示信息', '汇总审批中和汇总审批通过的数据不可以保存！')
							return;
						}
			}
				
			Ext.Msg.confirm('提示信息', '确认要保存吗？', function(id) {
				if (id == 'yes') {	
					Ext.Msg.wait("正在保存!请等待...");
					
					Ext.Ajax.request({
								url : 'managebudget/saveBudgetGatherMod.action',
								timeout:1500000,
								method : 'post',
								params : {
									mod : Ext.util.JSON.encode(m.values())//Ext.util.JSON.encode(mod)
								},
								success : function(result, request) {
									Ext.Msg.alert('提示信息', '数据保存成功！');
									isSaved = true;
								
							
								    setTimeout(function(){Ext.Msg.hide();}, 2000);
										
                                    setTimeout(function(){query();}, 2000);
									canSave = 1;
									m = new Map();
								},
								failure : function(result, request) {
									Ext.Msg.alert('提示信息', '数据保存失败！');
									
								}
							})
				}
			})
	}
	function reportRecord()
	{
		
		if(ds == null || ds.getTotalCount() == 0)
		{
			Ext.Msg.alert('提示信息','无数据可报送！');
			return;
		}
		
		var gatherIdToJudge = ds.getAt(0).get('budgetGatherId')
		if(gatherIdToJudge == null || gatherIdToJudge == '')
		{
			Ext.Msg.alert('提示信息','数据未保存，请先保存！');
			return ;
		}
		for (var i = 0; i <= ds.getTotalCount() - 1; i++) {
				if(ds.getAt(i).get('gatherStatus') != null)
				{
					if (ds.getAt(i).get('gatherStatus') == 1
							|| ds.getAt(i).get('gatherStatus') == 2)
							{
							Ext.Msg.alert('提示信息', '只有未上报或退回的数据可以报送！')
							return;
						}
				}				
			}
//		var reportObj = {
//			budgetGatherId : ds.getAt(0).get('budgetGatherId'),
//			topicId : ds.getAt(0).get('topicId'),
//			budgetTime : ds.getAt(0).get('budgetTime'),
//			gatherBy : fillBy.getValue(),
//			gatherWorkFlowNo : ds.getAt(0).get('gatherWorkFlowNo'),
//			gatherStatus : ds.getAt(0).get('gatherStatus')
//		};
			
//			var url = "budgetGatherReport.jsp";
//			var args = new Object();
//			args.entryId = ds.getAt(0).get('gatherWorkFlowNo');
//			args.workflowType = "budgetGatherApprove";
//			args.budgetGatherId =ds.getAt(0).get('budgetGatherId'); 
//			var obj = window.showModalDialog(url, args,
//					'status:no;dialogWidth=750px;dialogHeight=550px');
//                     if(obj)
			/*{		
			     query();
		    }*/
// modify by wpzhu 20100609-----------------------------------------
		Ext.Ajax.request({     
								url:'managebudget/budgetGatherReportTo.action',
								method : 'post',
								params : {
								budgetGatherId:ds.getAt(0).get('budgetGatherId')
								},
								success : function(result, request) {
									query();
									Ext.Msg.alert('提示信息', '数据汇总成功！');
							
								},
								failure : function(result, request) {
									Ext.Msg.alert('提示信息', '数据上报失败！');
									
								}
			});
			
		
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
	function exportRecord()
	{
		if (ds == null || ds.getTotalCount() == 0||(ds.getAt(0).get('budgetGatherId')==undefined)) {//modify by wphzu 20100609
				Ext.Msg.alert('提示信息', '无数据进行导出！');
				return;
			}
		
			Ext.Msg.confirm('提示信息', '确认要导出数据吗？', function(id) {
				if (id == 'yes') {
					var deptName  = '';
					for(var j = 2; j <= grid.getColumnModel().getColumnCount() - 2; j++)
					{
						deptName += '<th>' + grid.getColumnModel().getColumnHeader(j) + '</th>';
					}
					var tableHeader = '<table border=1><tr><th>预算指标</th><th>计量单位</th>' + deptName + 
							'<th>公司预算</th></tr>';
					var html = [tableHeader];
					for (var i = 0; i < ds.getTotalCount(); i += 1) {
						var rc = ds.getAt(i);
						var deptAdvice = '';
						for(var j = 2; j <= grid.getColumnModel().getColumnCount() - 2; j++)
						{
							var deptId = grid.getColumnModel().getDataIndex(j);
							deptAdvice += '<td>' + rc.get(deptId) + '</td>';
							
							
						}
						var rowValue = '<tr><td>' + rc.get('itemName')+ '</td>' +
										'<td>' + rc.get('unitName')+ '</td>' +
										deptAdvice + 
										'<td>' + rc.get('companyBudget')+ '</td>' +
										'</tr>';
						html.push(rowValue );
					}
					html.push('</table>');
					html = html.join(''); // 最后生成的HTML表格
					tableToExcel(html);
				}
			})
	}
	
	/*function judgeQuery()
  {
  	if (ds.getTotalCount() > 0) {
  		if(ds.getAt(0).get('budgetTime') != time.getValue())
			{
				Ext.Msg.alert('提示信息','请先查询数据！');
				return;
			}
			var entryId=ds.getAt(0).get('gatherWorkFlowNo');
			if(entryId==null||entryId=="")
			{
			  Ext.Msg.alert("提示","无审批信息！");	
			  return ;
			}
			var url="../../../../workflow/manager/show/show.jsp?entryId="+entryId;
			window.open(url);			
		}
		else{
		Ext.Msg.alert('提示信息','无数据进行审批查询！');
		return;
	}
  
  }*/
	query();
});