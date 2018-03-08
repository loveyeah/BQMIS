Ext.onReady(function() {
	var workFlowNo="";
	var sendCardId="";
	
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
			},{
				name : 'sendYear',
				mapping:12
			},{
				name : 'sendKind',
				mapping:13
			},{
				name : 'sendState',
				mapping:14
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
							DeptName = result.deptName;
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

	var gridTbar = new Ext.Toolbar({
				items : [{
							id : 'add',
							text : "新增",
							iconCls : 'add',
							handler : addRecord
						}, '-', {
							id : 'delete',
							text : "删除",
							iconCls : 'delete',
							handler : deleteRecord
						}, '-', {
							id : 'save',
							text : "保存",
							iconCls : 'save',
							handler : saveRecord
						}, '-', {
							id : 'refresh',
							text : "刷新",
							iconCls : 'reflesh',
							handler : cancerChange
						}, '-', {
							id : 'approve',
							text : "签字",
							iconCls : 'approve',
							handler : approveRecord
						}]
			});

	function cancerChange() {
		querySendCard();
		store.rejectChanges();

	}
	
	function getRecordByEntryBy() {
		Ext.Ajax.request({
					url : 'hr/getSendCardApproveInfo.action',
					method : 'post',
					params : {
						sendYear :sendYear.getValue() ,
				        sendKind : sendKind.getValue()
					},
						success : function(action) {
						var result = eval("(" + action.responseText + ")");
                     
						if (result && result.length > 0) {
							var mainId = "";
							var workflowNo = "";
							for (var i = 0; i < result.length; i++) {
								var ob = result[i];
								
								mainId += ob[0] + ",";
								workflowNo += ob[1] + ",";
								
							}
							sendCardId = mainId.substring(0, mainId.length - 1);
							workFlowNo = workflowNo.substring(0,workflowNo.length - 1)
						

						}

					}
				})
	}
	getRecordByEntryBy();
	
	function init(){
		Ext.Ajax.request({
			url : 'hr/getTbarStatus.action',
					method : 'post',
					params : {
						sendYear :sendYear.getValue() ,
				        sendKind : sendKind.getValue()
			               
					},
					success :function(response,options) {
						var res = response.responseText;
						if(res.toString() == '2')
						{

						Ext.get("add").dom.disabled = true;
						Ext.get("delete").dom.disabled = true;
						Ext.get("save").dom.disabled = true;
						Ext.get("refresh").dom.disabled = true;
						Ext.get("approve").dom.disabled = true;
						}else{
			            Ext.get("add").dom.disabled = false;
						Ext.get("delete").dom.disabled = false;
						Ext.get("save").dom.disabled = false;
						Ext.get("refresh").dom.disabled = false;
						Ext.get("approve").dom.disabled = false;
                       }
					}
				});
	
	}
	init();
   function  approveRecord(){
		if(store.getCount()==1  &&store.getAt(0).get("isNewRecord")=="total")
		{
			Ext.Msg.alert("提示", "无数据可审批！");
			return;
		}
		if(store.getCount()==0)
		{
			Ext.Msg.alert("提示", "无数据可审批！");
			return;
		}
	
		var url = "sign.jsp";
		if (store.getCount() > 0) {
			var args = new Object();
			args.entryId = workFlowNo;
			args.mainId=sendCardId;//主表ID
			var obj = window.showModalDialog(url, args,
					'status:no;dialogWidth=770px;dialogHeight=550px');
		}
		if (obj) {
			querySendCard();
			           Ext.get("add").dom.disabled = true;
						Ext.get("delete").dom.disabled = true;
						Ext.get("save").dom.disabled = true;
						Ext.get("refresh").dom.disabled = true;
						Ext.get("approve").dom.disabled = true;
		}
	}

	function querySendCard() {
		init();
		
		store.baseParams = {
			    flag:'approve',
				sendYear :sendYear.getValue() ,
				sendKind : sendKind.getValue()
		}
	
		store.load({
						params : {
							
						},
						callback : addLine
					});
   getRecordByEntryBy();
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
					sendYear:"",
					sendKind:"",
					isNewRecord : "total"
				});
				var count = store.getCount();
		
		// 停止原来编辑
		sendCardgrid.stopEditing();
		// 插入统计行
		store.insert(count, record);
		sendCardgrid.getView().refresh();
		
  }
 
	var btnQuery = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : querySendCard
			});

	var headerTbar = new Ext.Toolbar({
				items : ["年", sendYear,'-',"类别",sendKind, btnQuery
	]
			});
	
	var headForm = new Ext.form.FormPanel({
		region : 'north',
//		id : 'center-panel',
		height : 28,
		frame : false,
		fileUpload : true,
		layout : 'border',
		items : [{
							bodyStyle : "padding: 1,1,1,0",
							region : "center",
							border : true,
							frame : false,
							layout : 'form',
							items : [headerTbar]
						}]
		
	});
	function addRecord()
	{
		var count = store.getCount();
		var currentIndex = count;
		
		var o = new Record({
					'sendcardId' : '',
					'detailId' : '',														
					'deptName' : '',												
					'factNum' : '',
					'sendStandard' : '',
					'money' : '',
					'signName' : '',
					'sendYear':sendYear.getValue(),
					'sendKind':sendKind.getValue(),
					'memo' : ''/*,
					'isNewRecord':'total'*/

				});
		sendCardgrid.stopEditing();
		store.insert(currentIndex-1, o);
		sm.selectRow(currentIndex);
		sendCardgrid.startEditing(currentIndex, 1);

	};

	function saveRecord() {
		var alertMsg = "";
		sendCardgrid.stopEditing();
		var modifyRec = sendCardgrid.getStore().getModifiedRecords();
		if(modifyRec.length!=0)
		{
		Ext.Msg.confirm('提示', '确定要保存修改数据吗?', function(button) {
					if (button == 'yes') {
						var updateData = new Array();
						for (var i = 0; i < modifyRec.length; i++) {
							if (modifyRec[i].get("deptName") == null
									|| modifyRec[i].get("deptName") == "") {
								alertMsg += "部门名称不能为空</br>";
							}
							if (modifyRec[i].get("factNum") == null
									|| modifyRec[i].get("factNum") == "") {
								alertMsg += "实用人数不能为空</br>";
							}
							if (modifyRec[i].get("sendStandard") == null
									|| modifyRec[i].get("sendStandard") == "") {
								alertMsg += "发卡标准不能为空</br>";
							}
							if (modifyRec[i].get("signName") == null
									|| modifyRec[i].get("signName") == "") {
								alertMsg += "签名不能为空</br>";
							}
							
							if (alertMsg != "") {
								Ext.Msg.alert("提示", alertMsg);
								return;
							}
							updateData.push(modifyRec[i].data);
						}
						Ext.Ajax.request({
									url : 'hr/saveSendCard.action',
									method : 'post',
									params : {
										flag:"approve",
										sendYear : sendYear.getValue(),
										sendKind : sendKind.getValue(),
										costItem:costFrom.getValue(),
										isUpdate : Ext.util.JSON
												.encode(updateData)
										
									},
									success : function(form, options) {
										var obj = Ext.util.JSON
												.decode(form.responseText)
										Ext.MessageBox.alert('提示信息', '保存成功！')
										store.rejectChanges();
									    querySendCard();
									},
									failure : function(result, request) {
										Ext.MessageBox.alert('提示信息', '操作失败！')
									}
								})
					}
				})
		}else
		{
			Ext.MessageBox.alert("提示","未做任何修改!");
			store.rejectChanges();
			querySendCard();
		}
	}
	function deleteRecord() {
		var sm = sendCardgrid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.detailId) {
					ids.push(member.detailId);
				}
			}
			if (ids.length > 0) {
				Ext.Msg.confirm("删除", "是否确定删除所选记录？", function(buttonobj) {
							if (buttonobj == "yes") {
								Ext.lib.Ajax.request('POST',
										'hr/delSendCard.action', {
											success : function(action) {
												Ext.Msg.alert("提示", "删除成功！")
												querySendCard();
											},
											failure : function() {
												Ext.Msg.alert('错误',
														'删除时出现未知错误.');
											}
										}, 'ids=' + ids);
							} else {
								querySendCard();
							}
						});
			}
		}
	}


	var planDetailId = new Ext.form.Hidden({
				name : 'planDetailId'
			});

	var store = new Ext.data.JsonStore({
				url : 'hr/getSendCardList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : Record
			});

	
	store.on('load', function() {
				if (store.getAt(0) != null && store.getAt(0) != "") {
					
					fillByCode.setValue(store.getAt(0).data.entryBy);
					fillByName.setValue(store.getAt(0).data.entryByName);
					fillDate.setValue(store.getAt(0).data.entryDate.substring(0,10));
					
					if (store.getAt(0).data.sendState == "2") {
						sendCardgrid.stopEditing(true);
						store.rejectChanges();
						Ext.get("add").dom.disabled = true;
						Ext.get("delete").dom.disabled = true;
						Ext.get("save").dom.disabled = true;
						Ext.get("refresh").dom.disabled = true;
						Ext.get("approve").dom.disabled = true;
					} else {
						Ext.get("add").dom.disabled = false;
						Ext.get("delete").dom.disabled = false;
						Ext.get("save").dom.disabled = false;
						Ext.get("refresh").dom.disabled = false;
						Ext.get("approve").dom.disabled = false;
					}

				} else {

					Ext.get("add").dom.disabled = false;
					Ext.get("delete").dom.disabled = false;
					Ext.get("save").dom.disabled = false;
					Ext.get("refresh").dom.disabled = false;
					Ext.get("approve").dom.disabled = false;

					fillByCode.setValue(null);
					fillByName.setValue(null);
					fillDate.setValue(null);
				}
			})

	
 
	// 页面的Grid主体
	var sendCardgrid = new Ext.grid.EditorGridPanel({
		store : store,
		layout : 'fit',
		columns : [sm, new Ext.grid.RowNumberer({
							header : '行号',
							width : 35
						}),// 选择框
						{
					header : '年度',
					dataIndex : 'sendYear',
					align : 'center',
					width : 100,
					hidden:true
				}, {
					header : '类别',
					dataIndex : 'sendKind',
					align : 'center',
					width : 100,
					hidden:true,
					renderer:function (v)
					{
						if(v=="1")
						{
						return "夏季";
						}
						else  if(v=="2")
						{
							return "冬季";
						}
						
					}
				},
				{
					header : '部门',
					dataIndex : 'deptName',
					align : 'center',
					width : 100,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount()-1 ) {
							var deptName = record.data.deptName;
							// 强行触发renderer事件
							return deptName;
						} else {
							
					
							return "<font color='red'>"+"合计"+"</font>";
						}
					},
					editor : new Ext.form.TextField({
								allowBlank : false,
								id : 'deptName'
							})
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
							return  factNum;
						} else {
				
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('factNum');
							}
							return "<font color='red'>" + totalSum
									+ "</font>";
						}
					},
					editor :new Ext.form.NumberField({
						allowBlank : false,
						allowDecimals : false
						
					})

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
					  return  renderMoney(sendStandard);
						} else {
						var j=store.getCount()-1;
						if(j>0)
						{
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('sendStandard');
							}
							return "<font color='red'>" + renderMoney(totalSum/j)
									+ "</font>";
						}else return  "<font color='red'>" + renderMoney(sendStandard)
									+ "</font>";
						}
					},
					editor :new Ext.form.NumberField({
						allowBlank : false,
						allowDecimals : true,
						decimalPrecision : 2

					})

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
							return  renderMoney(subSum);
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
					width : 100,
					editor : new Ext.form.TextField({
								allowBlank : false,
								id : 'signName'
							})
				}, {
					header : '备注',
					dataIndex : 'memo',
					align : 'center',
					width : 120,
					editor : new Ext.form.TextField({
								allowBlank : true,
								id : 'memo'
							})

				}],

		sm : sm, // 选择框的选择
		tbar : gridTbar,
		clicksToEdit : 1,
		viewConfig : {
			forceFit : true
		}
	});
	// 进入页面时执行一次查询
	querySendCard();
	/*sendCardgrid.on('beforeedit', function() {
				if (Ext.get("save").dom.disabled == true) {
					return false;
				}
	})*/
	sendCardgrid.on('beforeedit', function(e) {
		if(e.record.get('isNewRecord')=='total')
		{
		return false;
		}
		
		
	});
    
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [{
							region : 'north',
							items : [headForm],
							height : 25,
							style : 'padding-bottom:0.8px'
						},{
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