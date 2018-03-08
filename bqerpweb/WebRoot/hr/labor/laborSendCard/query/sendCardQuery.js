Ext.onReady(function() {
	var workFlowNo;
	function getYear() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) ;
		return s;
	}
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate() ;
		s += (t > 9 ? "" : "0") + t + " ";
		return s;
	}
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
	//-----------------------------------------------------------------------
	 function renderMoney(v) {
    	return renderNumber(v, 2);//修改计算金额现在2位小数
    }
    
    function renderNumber(v, argDecimal) {
		if (v) {
			if (typeof argDecimal != 'number') {
				argDecimal = 2;
			}
			v = Number(v).toFixed(argDecimal);
			var t = '';
			v = String(v);
			while ((t = v.replace(/^(-?[0-9]+)([0-9]{3}.*)$/, '$1,$2')) !== v)
				v = t;
			
			return v;
		} else
			return '';
	}
	//------------------------------------------------------------------------------------
	var sendKind = new Ext.form.ComboBox({
				readOnly : true,
				name : 'sendKind',
				hiddenName : 'sendKind',
				mode : 'local',
				width : 70,
				value : "1",
				fieldLabel : '类别',
				triggerAction : 'all',
				listeners : {
					"select" : function() {
						store.reload();

					}
				},
				store : new Ext.data.SimpleStore({
							fields : ['name', 'value'],
							data : [['夏季', '1'], ['冬季', '2']]
						}),
				valueField : 'value',
				displayField : 'name',
				anchor : "15%",
				listeners : {
					"select" : function() {
						querySendCard();
					}
				}
			})

	var sendYear = new Ext.form.TextField({
				style : 'cursor:pointer',
				name : 'time',
				fieldLabel : '计划时间',
				readOnly : true,
				anchor : "80%",
				value : getYear(),
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y',
									alwaysUseStartDate : false,
									dateFmt : 'yyyy',
									isShowClear : false,
									onpicked : function(v) {
										querySendCard();
										this.blur();
									}
								});
					}
				}
			});
			
	var sm = new Ext.grid.CheckboxSelectionModel({
	singleSelect:false
	
	});
	// grid列表数据源
	
var store = new Ext.data.JsonStore({
				url : 'hr/getSendCardList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : Record
			});

	
	var Record = new Ext.data.Record.create([sm, {

				name : 'sendcardId',
				mapping:0
			}, {
				name : 'detailId',
				mapping:1
			}, {
				name : 'deptName',
				mapping:2
			}, {
				name : 'factNum',
				mapping:3
			}, {
				name : 'sendStandard',
				mapping:4
			},{
				name : 'money',
				mapping:5
			}, {
				name : 'signName',
				mapping:6
			}, {
				name : 'memo',
				mapping:7
			},{
				name : 'workFlowNo',
				mapping:8
			},{
				name : 'entryBy',
				mapping:9
			},{
				name : 'entryByName',
				mapping:10
			},{
				name : 'entryDate',
				mapping:11
			}]);
	// 制表人
	var fillByName = new Ext.form.TextField({
				readOnly : true,
				name : 'entryByName'

			});
	var fillByCode = new Ext.form.Hidden({
				name : 'entryBy'
			});

	// 制表时间

	var fillDate = new Ext.form.TextField({
				readOnly : true,
				name : 'entryDate'
			});
	// 费用来源
	var costFrom = new Ext.form.TextField({
				readOnly : true,
				value :'劳保费',
				disabled :true

			});

var deptId="";
	// 从session取登录人编码姓名部门相关信息
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号
							var workerCode = result.workerCode;
							var workerName = result.workerName;
							// 设定默认部门
							DeptCode = result.deptCode;
//							DeptName = result.deptName;
							deptId = result.deptId;
							
						}
					}
				});
	}
	getWorkCode();
	var bbar = new Ext.Toolbar({
				items : [ "制表人:", fillByName, fillByCode,
						'-', "制表时间:", fillDate,"费用来源:",costFrom]
			});

	

	
	function querySendCard() {
		store.baseParams = {
				sendYear :sendYear.getValue() ,
				sendKind : sendKind.getValue()
		}
	
		store.load({
						params : {
							
						},
						callback : addLine
					});

	}
  function addLine()
  {
  	
		// 统计行
		var record = new Record({
					sendcardId : "",
					detailId : "",
					deptName : "",
					factNum : "",
					sendStandard : "",
					money : "",
					signName : "",
					memo : "",
					workFlowNo : "",
					isNewRecord : "total"
				});

		// 原数据个数
		var count = store.getCount();
		// 停止原来编辑
		sendCardgrid.stopEditing();
		// 插入统计行
		store.insert(count, record);
		sendCardgrid.getView().refresh();
		totalCount = store.getCount() - 1;
	
  }
	function approveQuery() {
		if(sm.hasSelection()){
			if(sm.getSelections().length >1){
				Ext.Msg.alert('提示','请选择一条数据！');
				return;
			}
			var selected = sm.getSelected();
			var url = '';
			if(sm.getSelected().get("workFlowNo") == null || sm.getSelected().get("workFlowNo") == "")
			{
				url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
								+ "bqSendCard";
						window.open(url);;
			}else{
				var url = "/power/workflow/manager/show/show.jsp?entryId="
								+ sm.getSelected().get("workFlowNo");
						window.open(url);
			}
		}else{
			Ext.Msg.alert('提示','请选择要查看审批信息的数据！')
		}
	}

	var btnQuery = new Ext.Button({
		text : '查询',
		iconCls : 'query',
		handler : querySendCard
	});
	var btnApproveQuery = new Ext.Button({
		text : '审批查询',
		iconCls : 'approve',
		handler : approveQuery
	});

	var headerTbar = new Ext.Toolbar({
				items : ["年", sendYear,'-',"类别",sendKind, btnQuery, '-',
						btnApproveQuery]
			});
	var tbarPanel = new Ext.Panel({
				layout : 'form',
				items : [headerTbar]
			});
	

	var planDetailId = new Ext.form.Hidden({
				name : 'planDetailId'
			});

	var store = new Ext.data.JsonStore({
				url : 'hr/getSendCardList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : Record
			});

	store.on("beforeload", function() {
				Ext.apply(this.baseParams, {
							

						});

			});
	store.on('load', function() {
				if (store.getAt(0) != null && store.getAt(0) != "") {
					
					fillByCode.setValue(store.getAt(0).data.entryBy);
					fillByName.setValue(store.getAt(0).data.entryByName);
					fillDate.setValue(store.getAt(0).data.entryDate.substring(0,10));
				}
					
			});

	

	// 页面的Grid主体
	var sendCardgrid = new Ext.grid.EditorGridPanel({
		store : store,
		layout : 'fit',
		columns : [sm, new Ext.grid.RowNumberer({
							header : '行号',
							width : 35
						}),// 选择框
				{
					header : '部门',
					dataIndex : 'deptName',
					align : 'center',
					width : 100,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							var deptname = record.data.deptName;
							// 强行触发renderer事件
							return deptname;
						} else {
					
							return "<font color='red'>"+"合计"+"</font>";
						}
					}
					
				},  {
					header : '实用人数',
					dataIndex : 'factNum',
					align : 'center',
					width : 80,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							var factNum = record.data.factNum;
							// 强行触发renderer事件
							var totalSum = 0;
							return   factNum;
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('factNum');
							}
							return "<font color='red'>" + totalSum
									+ "</font>";
						}
					}
					
				},{
					header : '发卡标准',
					dataIndex : 'sendStandard',
					align : 'center',
					width : 80,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							var sendStandard = record.data.sendStandard;
							// 强行触发renderer事件
							var totalSum = 0;
					return renderMoney(sendStandard);
						} else {
						var j=store.getCount()-1;
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('sendStandard');
							}
							return "<font color='red'>" + renderMoney(totalSum/j)
									+ "</font>";
						}
					}
			
				},{
					header : '金额',
					dataIndex : 'money',
					align : 'center',
					width : 80,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							var subSum = record.data.factNum
									* record.data.sendStandard;
							// 强行触发renderer事件
							return renderMoney(subSum);
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('money');
							}
							return "<font color='red'>" + renderMoney(totalSum)
									+ "</font>";
						}
					}
					

				}, {
					header : '签名',
					dataIndex : 'signName',
					align : 'left',
					width : 100
				}, {
					header : '备注',
					dataIndex : 'memo',
					align : 'center',
					width : 120

				}],

		sm : sm, // 选择框的选择
		tbar : headerTbar,
		clicksToEdit : 1,
		viewConfig : {
			forceFit : true
		}
	});
	// 进入页面时执行一次查询
	querySendCard();
	
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [{
							bodyStyle : "padding: 1,1,1,0",
							region : "center",
							border : false,
							frame : false,
							layout : 'fit',
							items : [sendCardgrid]
						}, {
							region : 'south',
							items : [bbar],
							height : 25,
							style : 'padding-bottom:0.8px'
						}]
			});
})