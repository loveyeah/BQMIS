Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

function stopCompute(){
	 
		if(confirm("终止后将不能继续执行，确定要终止计算吗?"))
		{
			Ext.Ajax.request({
				url : 'manager/stopCompute.action',
				callback:function(options,success,response){
					var result = Ext.util.JSON.decode(response.responseText);      
		   			if(!result.success)
		   			{
		   				Ext.Msg.alert("错误",result.msg);
		   			}
		
				}
			});		
		}  
}
Ext.onReady(function() {
	// 时间段Field
	function ChangeDateToString(d) {
		var d, s, t;
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		t = d.getDate();
		s += "-" + (t > 9 ? "" : "0") + t + " ";
		return s;
	}
	var enddate = new Date();
	var startdate = enddate.add(Date.DAY, -1);
	var sdate = ChangeDateToString(startdate);
	var edate = ChangeDateToString(startdate);

	function ChangeTimeToString(d) {
		var d, s, t;
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		t = d.getDate();
		s += "-" + (t > 9 ? "" : "0") + t + " ";
		s += "00"
		return s;
	}
	var endtime = new Date();
	var starttime = enddate.add(Date.DAY, -1);
	var stime = ChangeTimeToString(starttime);
	var etime = ChangeTimeToString(endtime);
	var fromTime = new Ext.form.TextField({
				id : 'fromTime',
				name : '_fromTime',
				hideLabel : true,
				style : 'cursor:pointer',
				cls : 'Wdate',
				width : 150,
				value : stime,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-01',
									dateFmt : 'yyyy-MM-dd HH',
									alwaysUseStartDate : true
								});
					}
				}
			});
	var toTime = new Ext.form.TextField({
				name : '_toTime',
				value : etime,
				id : 'toTime',
				hideLabel : true,
				style : 'cursor:pointer',
				cls : 'Wdate',
				width : 150,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-01',
									dateFmt : 'yyyy-MM-dd HH',
									alwaysUseStartDate : true
								});
					}
				}
			});
	var fromDate = new Ext.form.TextField({
				id : 'timefromDate',
				name : '_timefromDate',
				hideLabel : true,
				style : 'cursor:pointer',
				cls : 'Wdate',
				width : 150,
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
	var toDate = new Ext.form.TextField({
				name : '_timetoDate',
				value : edate,
				id : 'timetoDate',
				hideLabel : true,
				style : 'cursor:pointer',
				cls : 'Wdate',
				width : 150,
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

	/**
	 * 月份Field
	 */
	var nowdate = new Date();
	var date1 = nowdate.toLocaleDateString();
	var exdate;
	if (date1.substring(6, 7) == '月') {
		exdate = date1.substring(0, 4) + '-0' + date1.substring(5, 6);
	} else {
		exdate = date1.substring(0, 4) + "-" + date1.substring(5, 7);
	}
	var enddate = exdate;
	var fromMonth = new Ext.form.TextField({
				name : '_fromMonth',
				value : exdate,
				id : 'fromMonth',
				style : 'cursor:pointer',
				hideLabel : true,
				cls : 'Wdate',
				width : 150,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M',
									dateFmt : 'yyyy-MM',
									alwaysUseStartDate : true
								});
					}
				}
			});
	var toMonth = new Ext.form.TextField({
				name : '_toMonth',
				value : exdate,
				id : 'toMonth',
				style : 'cursor:pointer',
				hideLabel : true,
				cls : 'Wdate',
				width : 150,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M',
									dateFmt : 'yyyy-MM',
									alwaysUseStartDate : true
								});
					}
				}
			});
	/**
	 * 年份Field
	 */
	var d = new Date();
	year = d.getFullYear();
	var fromYear = new Ext.form.TextField({
				id : 'fromYear',
				name : '_fromYear',
				style : 'cursor:pointer',
				hideLabel : true,
				cls : 'Wdate',
				width : 150,
				value : year,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y',
									dateFmt : 'yyyy',
									alwaysUseStartDate : true
								});
					}
				}
			});
	var toYear = new Ext.form.TextField({
				id : 'toYear',
				name : '_toYear',
				style : 'cursor:pointer',
				hideLabel : true,
				cls : 'Wdate',
				width : 150,
				value : year,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y',
									dateFmt : 'yyyy',
									alwaysUseStartDate : true
								});
					}
				}
			});
	/**
	 * 季度Field
	 */

	var fromQuarter = new Ext.form.TextField({
				id : 'fromQuarter',
				name : '_fromQuarter',
				style : 'cursor:pointer',
				hideLabel : true,
				cls : 'Wdate',
				value : year,
				width : 60,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y',
									dateFmt : 'yyyy',
									alwaysUseStartDate : true
								});
					}
				}
			});
	var fromQuarterBox = new Ext.form.ComboBox({
				store : [['1', '一季'], ['2', '二季'], ['3', '三季'], ['4', '四季']],
				id : 'fromQuarterBox',
				hideLabel : true,
				name : 'fromQuarterBoxName',
				valueField : "value",
				displayField : "text",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'fromQuarterBoxName',
				editable : false,
				triggerAction : 'all',
				width : 65,
				selectOnFocus : true,
				value : 1
			});
	var toQuarter = new Ext.form.TextField({
				id : 'toQuarter',
				name : '_toQuarter',
				style : 'cursor:pointer',
				hideLabel : true,
				cls : 'Wdate',
				width : 60,
				value : year,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y',
									dateFmt : 'yyyy',
									alwaysUseStartDate : true
								});
					}
				}
			});
	var toQuarterBox = new Ext.form.ComboBox({
				store : [['1', '一季'], ['2', '二季'], ['3', '三季'], ['4', '四季']],
				id : 'toQuarterBox',
				name : 'toQuarterBoxName',
				valueField : "value",
				displayField : "text",
				hideLabel : true,
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'quarterBoxName',
				editable : false,
				triggerAction : 'all',
				width : 65,
				selectOnFocus : true,
				value : 1
			});

	var timeBox = new Ext.form.Checkbox({
				id : 'timeBox',
				height:22,
				name : 'queryWayBox',
				boxLabel : '时间区间：',
				hideLabel : true
			});

	var dateBox = new Ext.form.Checkbox({
				id : 'dateBox',
				height:22,
				name : 'queryWayBox',
				boxLabel : '日期区间：',
				hideLabel : true,
				checked : true
			});

	var quarterBox = new Ext.form.Checkbox({
				id : 'quarterBox',
				height:22,
				name : 'queryWayBox',
				hideLabel : true,
				boxLabel : ' 季度区间:'
			});

	var monthBox = new Ext.form.Checkbox({
				id : 'monthBox',
				height:22,
				name : 'queryWayBox',
				hideLabel : true,
				boxLabel : '月份区间:'
			});

	var yearBox = new Ext.form.Checkbox({
				id : 'yearBox',
				height:22,
				name : 'queryWayBox',
				hideLabel : true,
				boxLabel : '年份区间:'
			});

	// add by bjxu
	var groupBox = new Ext.form.Checkbox({
				id : 'groupBox',
				height:22,
				name : 'queryWayBox',
				hideLabel : true,
				boxLabel : '分组区间:'
			})

	var groupfromDate = new Ext.form.TextField({
				id : 'groupfromDate', 
				name : '_groupfromDate',
				hideLabel : true,
				style : 'cursor:pointer',
				cls : 'Wdate',
				width : 150,
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
	var grouptoDate = new Ext.form.TextField({
				name : '_grouptoDate',
				value : edate,
				id : 'grouptoDate',
				hideLabel : true,
				style : 'cursor:pointer',
				cls : 'Wdate',
				width : 150,
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

	/**
	 * 季度Panel
	 */
	var fromQuarterPanel = new Ext.Panel({
				id : 'fromQuarterPanel',
				layout : 'column',
				border : false,
				items : [{
							border : false,
							columnWidth : 0.37,
							anchor : '100%',
							layout : 'form',
							items : [fromQuarter]
						}, {
							border : false,
							columnWidth : 0.5,
							anchor : '100%',
							layout : 'form',
							items : [fromQuarterBox]
						}]
			});
	var toQuarterPanel = new Ext.Panel({
				id : 'toQuarterPanel',
				layout : 'column',
				border : false,
				items : [{
							border : false,
							columnWidth : 0.37,
							anchor : '100%',
							layout : 'form',
							items : [toQuarter]
						}, {
							border : false,
							columnWidth : 0.5,
							anchor : '100%',
							layout : 'form',
							items : [toQuarterBox]
						}]
			});
	function validate() {
		var msg = "";
		if (timeBox.checked) {
			if (fromTime.getValue() > toTime.getValue())
				msg += "选择结束时间应大于等于开始时间;<br>";
		}
		if (dateBox.checked) {
			if (fromDate.getValue() > toDate.getValue())
				msg += "选择结束日期应大于等于开始日期;<br>";
		}
		if (groupBox.checked) {
			if (groupfromDate.getValue() > grouptoDate.getValue())
				msg += "选择分组结束日期应大于等于开始日期;<br>";
		}
		if (monthBox.checked) {
			if (fromMonth.getValue() > toMonth.getValue())
				msg += "选择结束月份应大于等于开始月份;<br>";
		}
		if (quarterBox.checked) {
			if ((fromQuarter.getValue() > toQuarter.getValue())
					|| ((fromQuarter.getValue() == toQuarter.getValue() && fromQuarterBox
							.getValue() > toQuarterBox.getValue())))
				msg += "选择结束季度应大于等于开始季度;<br>";
		}
		if (yearBox.checked) {
			if (fromYear.getValue() < toYear.getValue())
				msg += "选择结束年份应大于等于开始年份;";
		}
		if (msg == "") {
			return true;
		} else {
			Ext.Msg.alert('提示', msg);
			return false;
		}
	}
	function compute() {
		if (!validate()) {
			return false;
		}
		Ext.Msg.wait("正在处理数据!,请等待...<a href='javascript:stopCompute();'>终止计算</a>"); 
		var params = {
				"compute.isCollect" : collectBox.checked,
				"compute.isCompute" : computeBox.checked,
				"compute.isTime" : timeBox.checked,
				"compute.isDate" : dateBox.checked,
				"compute.isGroup" : groupBox.checked,
				"compute.isMonth" : monthBox.checked,
				"compute.isQuarter" : quarterBox.checked,
				"compute.isYear" : yearBox.checked,
				"compute.startTime" : fromTime.getValue(),
				"compute.endTime" : toTime.getValue(),
				"compute.startDate" : fromDate.getValue(),
				"compute.endDate" : toDate.getValue(),
				"compute.groupStartDate" : groupfromDate.getValue(),
				"compute.groupEndDate" : grouptoDate.getValue(),
				"compute.startMonth" : fromMonth.getValue(),
				"compute.endMonth" : toMonth.getValue(),
				"compute.startQuarterYear" : fromQuarter.getValue(),
				"compute.endQuarterYear" : toQuarter.getValue(),
				"compute.startQuarter" : fromQuarterBox.getValue(),
				"compute.endQuarter" : toQuarterBox.getValue(),
				"compute.startYear" : fromYear.getValue(),
				"compute.endYear" : toYear.getValue()
			}; 
			
			
		Ext.Ajax.request({
			url : 'manager/statItemCollectCopmute.action',
			method : 'post',
			params : params,
			timeout:86400000,
			success : function(result, request) {
				var o = eval('(' + result.responseText + ')');
				Ext.Msg.hide();
				Ext.MessageBox.alert('提示信息', o.msg);
				if (computeBox.checked) {
					ds.on('beforeload', function() {
								Ext.apply(this.baseParams, {
											"compute.isTime" : timeBox.checked,
											"compute.isDate" : dateBox.checked,
											"compute.isGroup" : groupBox.checked,
											"compute.isMonth" : monthBox.checked,
											"compute.isQuarter" : quarterBox.checked,
											"compute.isYear" : yearBox.checked
										});
							})
					ds.load({
								params : {
									"compute.isTime" : timeBox.checked,
									"compute.isDate" : dateBox.checked,
									"compute.isGroup" : groupBox.checked,
									"compute.isMonth" : monthBox.checked,
									"compute.isQuarter" : quarterBox.checked,
									"compute.isYear" : yearBox.checked,
									start : 0,
									limit : 18
								}
							});
				}
			},
			failure : function(result, request) {
				Ext.Msg.hide();
				Ext.MessageBox.alert('提示信息', '未知错误，请联系管理员！')
			}

		})
	}
	var btnCompute = new Ext.Button({
				id : 'btnCompute',
				text : '确  定',
				minWidth : 70,
				handler : compute
			});
	 

	// var btnStop = new Ext.Button({
	// id : 'btnPrint',
	// text : '停 止',
	// minWidth : 70,
	// handler : function() {
	//
	// }
	// });
	var Query = new Ext.form.FieldSet({
				title : '时间设置',
				border : true,
				height : 160,
				// collapsible : true,
				anchor : '100%',
				layout : 'column',
				items : [{
					border : false,
					columnWidth : 0.3,
					align : 'center',
					layout : 'form',
					items : [timeBox, groupBox, dateBox, monthBox, quarterBox,
							yearBox]
				}, {
					border : false,
					columnWidth : 0.35,
					anchor : '100%',
					layout : 'form',
					items : [fromTime,groupfromDate, fromDate,  fromMonth,
							fromQuarterPanel, fromYear]
				}, {
					border : false,
					columnWidth : 0.35,
					anchor : '100%',
					layout : 'form',
					items : [toTime,grouptoDate,  toDate, toMonth,
							toQuarterPanel, toYear]
				}]
			});
	var collectBox = new Ext.form.Checkbox({
				id : 'collectBox',
				boxLabel : '采集数据',
				hideLabel : true,
				name : 'queryTypeBox'
			});
	var computeBox = new Ext.form.Checkbox({
				id : 'computeBox',
				boxLabel : '指标计算',
				hideLabel : true,
				name : 'queryTypeBox',
				checked : true
			})
	var QueryType = new Ext.form.FieldSet({
				title : '计算设置',
				border : true,
				height : 160,
				buttonAlign : 'center',
				// collapsible : true,
				layout : 'form',
				items : [ collectBox, computeBox,btnCompute
				// {
				// layout : 'column',
				// border : false,
				// items : [
				// // {
				// // border : false,
				// // columnWidth : 0.3,
				// // layout : 'form'
				// // },
				// {
				// border : false,
				// columnWidth : 0.7,
				// layout : 'form',
				// items : [collectBox, computeBox]
				// }
				// ]
				// }
				]
//				,
//				buttons : []
			});
	var form = new Ext.form.FormPanel({
				bodyStyle : "padding:1px 1px 1px 1px",
				labelAlign : 'right',
				id : 'shift-form',
				// labelWidth : 5,
				autoHeight : true,
				region : 'center',
				border : false,
				layout : 'form',
				items : [{
							layout : 'column',
							border : false,
							items : [{
										border : false,
										columnWidth : 0.75,
										align : 'center',
										layout : 'form',
										anchor : '100%',
										items : [Query]
									}, {
										border : false,
										columnWidth : 0.01,
										align : 'center',
										layout : 'form',
										anchor : '100%'
									}, {
										border : false,
										columnWidth : 0.18,
										align : 'center',
										layout : 'form',
										anchor : '100%',
										items : [QueryType]
									}]
						}]
			});

	/*-----------------------------------------------------------------------------------------------------------------*/
	var item = Ext.data.Record.create([{
				name : 'accountOrder'
			}, {
				name : 'itemCode'
			}, {
				name : 'itemName'
			}, {
				name : 'dataCollectWay'
			}]);
	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
						header : '行',
						width : 30,
						align : 'center'
					}), {
				header : '等级',
				dataIndex : 'accountOrder',
				width : 50,
				align : 'center'
			}, {
				header : '指标编码',
				dataIndex : 'itemCode',
				width : 80,
				align : 'center'
			}, {
				header : '指标名称',
				dataIndex : 'itemName',
				width : 80,
				align : 'center'
			}, {
				header : '产生方式',
				dataIndex : 'dataCollectWay',
				width : 80,
				align : 'center',
				renderer : function(e) {
					if (e == 1)
						return "手工录入";
					if (e == 2)
						return "实时采集";
					if (e == 3)
						return "派生计算";
				}
			}]);
	cm.defaultSortable = true;
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manager/findComputeStatItemList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'totalCount',
							root : 'list'
						}, item)
			});
	var Grid = new Ext.grid.GridPanel({
				ds : ds,
				cm : cm,
				sm : sm,
				split : true,
				autoScroll : true,
				loadMask : {
					msg : '读取数据中 ...'
				},
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : ds,
							displayInfo : true,
							displayMsg : '显示第 {0} 条到 {1} 条记录，一共 {2} 条',
							emptyMsg : "没有记录"
						}),
				border : false,
				viewConfig : {
					forceFit : false
				}
			});
	var panelleft = new Ext.Panel({
				region : 'west',
				title : '指标计算',
				layout : 'fit',
				width : 220,
				autoScroll : true,
				containerScroll : true,
				border : false,
				collapsible : true,
				split : true,
				items : [Grid]
			});

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				items : [{
							region : "north",
							layout : 'fit',
							height : 180,
							border : false,
							split : true,
							items : [form]
						}, {
							region : "center",
							layout : "fit",
							collapsible : true,
							border : false,
							items : [panelleft]
						}]
			});
})
