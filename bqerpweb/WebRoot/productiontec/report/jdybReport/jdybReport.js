Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function(){
	function getMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t
		return s;
	}
	
	// 取得登陆用户信息
	function initworker()
	{
		Ext.Ajax.request({
				url : "administration/getUserInfo.action",
				method : "post",
				success : function(result, request) {
					var data = eval("(" + result.responseText + ")");
					entryBy.setValue(data.userCode);
					if(data.userName){
						entryName.setValue(data.userName);
					    
					}else{
						hiddenName.setValue("");
					}									
				}
			});
	}	
	initworker();
	var jdybId = new Ext.form.Hidden({
		id : 'jdybId'
	})
	// 11
	var text11 = new Ext.form.TextField({
		id : 'text11',
		fieldLabel : '1．',
		labelSeparator  : '',
		readOnly : true,
		value : getMonth().substring(0,4) + "年" + getMonth().substring(5,7) + "月",
		width : 80
	})
	// 12
	var text12 = new Ext.form.NumberField({
		id : 'text12',
		fieldLabel : '35KV以下保护动作总数',
		//labelSeparator : '',
		allowDecimals : false,
		allowNegative : false
	})
	// 13
	var text13 = new Ext.form.NumberField({
		id : 'text13',
		fieldLabel : '，其中正确次数',
		//labelSeparator : '',
		allowDecimals : false,
		allowNegative : false
	})
	// 21
	var text21 = new Ext.form.NumberField({
		id : 'text21',
		fieldLabel : '2．110KV（包括66KV）保护动作总次数',
		//labelSeparator : '',
		allowDecimals : false,
		allowNegative : false
	})
	// 22
	var text22 = new Ext.form.NumberField({
		id : 'text22',
		allowDecimals : false,
		allowNegative : false,
		fieldLabel : '，其中正确次数'
	})
	// 31
	var text31 = new Ext.form.NumberField({
		id : 'text31',
		allowDecimals : false,
		allowNegative : false,
		fieldLabel : '3．220KV及以上保护动作总次数'
	})
	// 32 
	var text32 = new Ext.form.NumberField({
		 id : 'text32',
		 allowDecimals : false,
		 allowNegative : false,
		 //labelSeparator : '',
		 fieldLabel : '，其中正确次数'
	})
	// 41
	var text41 = new Ext.form.NumberField({
		id : 'text41',
		allowDecimals : false,
		allowNegative : false,
		//labelSeparator : '',
		fieldLabel : '4．110KV及以下重合闸动作总次数'
	})
	// 42
	var text42 = new Ext.form.NumberField({
		id : 'text42',
		allowDecimals : false,
		allowNegative : false,
		//labelSeparator : '',
		fieldLabel : '，其中正确次数装置成功次数'
	})
	// 51
	var text51 = new Ext.form.NumberField({
		id : 'text51',
		allowDecimals : false,
		allowNegative : false,
		//labelSeparator : '',
		fieldLabel : '5．220KV及以上重合闸动作总次数'
	})
	// 52
	var text52 = new Ext.form.NumberField({
		id : 'text52',
		allowDecimals : false,
		allowNegative : false,
		//labelSeparator : '',
		fieldLabel : '，其中正确次数'
	})
	// 61 
	var text61 = new Ext.form.NumberField({
		id : 'text61',
		allowDecimals : false,
		allowNegative : false,
		labelSepatator : '',
		fieldLabel : '6．故障录波器动作总次数'
	})
	// 62 
	var text62 = new Ext.form.NumberField({
		id : 'text62',
		allowDecimals : false,
		allowNegative : false,
//		labelSeparator : '',
		fieldLabel : '，录波完好次数'
	})
	
	// 设备部
	var protectedName = new Ext.form.TriggerField({
		fieldLabel : "设备部",
		readOnly : true,
		displayField : 'text',
		valueField : 'id',
		hiddenName : 'protectedName',
		name : 'protectedName'
	})
	protectedName.onTriggerClick = selectWorkerWin;
	// 设备部编码
	var protectedBy = new Ext.form.Hidden({
		id : 'protectedBy'
			})
	//选择所有人员及其所在部门
	function selectWorkerWin() {
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
						'../../../comm/jsp/hr/workerByDept/workerByDept2.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(person) != "undefined") {
			protectedName.setValue(person.workerName);
			protectedBy.setValue(person.workerCode);
		}
	}
	
	// 审核人
	var managerName = new Ext.form.TriggerField({
		fieldLabel : "审核人",
		readOnly : true,
		displayField : 'text',
		valueField : 'id',
		hiddenName : 'workerName',
		name : 'workerName'
	})
	managerName.onTriggerClick = selectManagerWin;
	//审核人编码
	var managerBy = new Ext.form.Hidden({
		id : 'managerBy'
			})
	//选择审核人
	function selectManagerWin() {
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
						'../../../comm/jsp/hr/workerByDept/workerByDept2.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(person) != "undefined") {
			managerName.setValue(person.workerName);
			managerBy.setValue(person.workerCode);
		}
	}
	
	// 批准人
	var approveName = new Ext.form.TriggerField({
		fieldLabel : "批准人",
		readOnly : true,
		displayField : 'text',
		valueField : 'id',
		hiddenName : 'approveName',
		name : 'approveName'
	})
	approveName.onTriggerClick = selectManagerWin3;
	//批准人编码
	var approveBy = new Ext.form.Hidden({
		id : 'approveBy'
			})
	//选择批准人
	function selectManagerWin3() {
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
						'../../../comm/jsp/hr/workerByDept/workerByDept2.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(person) != "undefined") {
			approveName.setValue(person.workerName);
			approveBy.setValue(person.workerCode);
		}
	}
	
	// 填表人
	var entryName = new Ext.form.TextField({
		fieldLabel : "填表人",
		readOnly : true,
		displayField : 'text',
		valueField : 'id',
		hiddenName : 'entryName',
		name : 'entryName'
	})
	//填表人编码
	var entryBy = new Ext.form.Hidden({
		id : 'entryBy'
			})
	var fieldset = new Ext.form.FieldSet({
		style : {
			"padding-top" : '5'
		},
		frame : false,
		border : false,
		width : 800,
		labelAlign : 'left',
		layout : 'column',
		items : [{
			columnWidth : 1,
			border : true,
			layout : 'column',
			items : [{
				columnWidth : .13,
				border : false,
				layout : 'form',
				labelWidth : 10,
				items : [jdybId,text11]
			},{
				columnWidth : .34,
				border : false,
				layout : 'form',
				labelWidth : 130,
				items : [text12]
			},{
				columnWidth : .3,
				border : false,
				layout : 'form',
				items : [text13]
			}]
		},{
			columnWidth : 1,
			border : true,
			layout : 'column',
			items : [{
				columnWidth : .46,
				border : false,
				layout : 'form',
				labelWidth : 220,
				items : [text21]
			},{
				columnWidth : .3,
				border : false,
				layout : 'form',
				items : [text22]
			}]
		},{
			columnWidth : 1,
			border : true,
			layout : 'column',
			items : [{
				columnWidth : .42,
				border : false,
				layout : 'form',
				labelWidth : 190,
				items : [text31]
			},{
				columnWidth : .3,
				border : false,
				layout : 'form',
				items : [text32]
			}]
		},{
			columnWidth : 1,
			border : true,
			layout : 'column',
			items : [{
				columnWidth : .44,
				border : false,
				layout : 'form',
				labelWidth : 200,
				items : [text41]
			},{
				columnWidth : .44,
				border : false,
				layout : 'form',
				labelWidth : 160,
				items : [text42]
			}]
		},{
			columnWidth : 1,
			border : true,
			layout : 'column',
			items : [{
				columnWidth : .44,
				border : false,
				layout : 'form',
				labelWidth : 200,
				items : [text51]
			},{
				columnWidth : .3,
				border : false,
				layout : 'form',
				items : [text52]
			}]
		},{
			columnWidth : 1,
			border : true,
			layout : 'column',
			items : [{
				columnWidth : .37,
				border : false,
				layout : 'form',
				labelWidth : 150,
				items : [text61]
			},{
				columnWidth : .3,
				border : false,
				layout : 'form',
				items : [text62]
			}]
		},{
			columnWidth : 1,
			border : true,
			layout : 'column',			
			items : [{
				columnWidth : .25,
				border : false,
				layout : 'form',
				labelWidth : 50,
				items : [managerName,managerBy]
			},{
				columnWidth : .25,
				border : false,
				layout : 'form',	
				labelWidth : 50,
				items : [protectedName,protectedBy]
			},{
				columnWidth : .25,
				border : false,
				labelWidth : 50,
				layout : 'form',
				items : [approveName,approveBy]
			},{
				columnWidth : .25,
				border : false,
				labelWidth : 50,
				layout : 'form',
				items : [entryName,entryBy]
			}]
		}]
	})
	
	
	
	// 数据对象
	var item = new Ext.data.Record.create([{
		name : 'jdybDetailId'
	},{
		name : 'jdybId'
	},{
		name : 'time'
	},{
		name : 'address'
	},{
		name : 'equSummary'
	},{
		name : 'equName'
	},{
		name : 'equType'
	},{
		name : 'rightNum'
	},{
		name : 'wrongNum'
	},{
		name : 'errorReason'
	},{
		name : 'memo'
	},{
		name : 'managerBy'
	},{
		name : 'protectedBy'
	},{
		name : 'approveBy'
	},{
		name : 'entryBy'
	}])
	
	// store
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'productionrec/findAllDetails.action'
		}),
		reader : new Ext.data.JsonReader({
			totalCount : 'totalCount',
			root : 'list'			
		},item)
	})
	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false
			});
	var gridbbar = new Ext.PagingToolbar({
				pageSize : 18,
				store : ds,
				displayInfo : true,
				displayMsg : "显示第{0}条到{1}条，共{2}条",
				emptyMsg : "没有记录"
			});
	var cm = new Ext.grid.ColumnModel({
				columns : [sm, new Ext.grid.RowNumberer({
									header : "行号",
									width : 31
								}), {
							header : '继电月报明细ID',
							dataIndex : 'jdybDetailId',
							hidden : true
						}, {
					header : '时间',
					dataIndex : 'time',
					align : 'center',
					width : 90,
					renderer : function(v){
						if(v == null)
						return null;
						else
						return v.substring(0,10);
					},
					editor : new Ext.form.TextField({
						style : 'cursor:pointer',
						id : "temp",
						readOnly : true,
						listeners : {
							focus : function() {
								WdatePicker({
									alwaysUseStartDate : true,
									dateFmt : "yyyy-MM-dd",
									isShowClear : true,
									onpicked : function() {
										Grid
												.getSelectionModel()
												.getSelected()
												.set(
														"time",
														Ext.get("temp").dom.value);
									},
									onclearing : function() {
										Grid.getSelectionModel().getSelected()
												.set("time", null);
									}
								});
								this.blur();
							}
						}
					})
				}, {
							header : '地点',
							dataIndex : 'address',
							align : 'right',
							width : 90,
							editor : new Ext.form.TextField({
										style : 'cursor:pointer',
										allowBlank : false
									})
						}, {
							header : '保护装置动作情况简述（包括一次系统情况，设备故障简历及对策）',
							dataIndex : 'equSummary',
							align : 'right',
							width : 130,							
							editor : new Ext.form.TextField({
										style : 'cursor:pointer',
										allowBlank : false
									})
						}, {
							header : '被保护设备名称',
							dataIndex : 'equName',
							align : 'right',
							editor : new Ext.form.TextField({
										style : 'cursor:pointer',
										allowBlank : false
									})
						}, {
							header : '保护与安全自动装置类型',
							dataIndex : 'equType',
							align : 'right',
							editor : new Ext.form.TextField({
										style : 'cursor:pointer',
										allowBlank : false
									})
						}, {
							header : '正确（次数）',
							dataIndex : 'rightNum',
							align : 'right',
							width : 80,
							editor : new Ext.form.NumberField({
										style : 'cursor:pointer',
										allowBlank : false
									})
						}, {
							header : '不正确（次数）',
							dataIndex : 'wrongNum',
							align : 'right',
							width : 80,
							editor : new Ext.form.NumberField({
										style : 'cursor:pointer',
										allowBlank : false
									})
						}, {
							header : '不正确动作责任分析',
							dataIndex : 'errorReason',
							align : 'right',	
							editor : new Ext.form.TextField({
										style : 'cursor:pointer',
										allowBlank : false
									})
						}, {
							header : '备 注',
							dataIndex : 'memo',
							align : 'right',
							width : 130,
							editor : new Ext.form.TextField({
										style : 'cursor:pointer',
										allowBlank : false
									})
						}],
				defaultSortable : true,
				rows : [[{
							rowspan : 1,
							colspan : 8
						}, {
							header : '装置的动作分析',
							colspan : 2,
							align : 'center'
						},{
							rowspan : 1,
							colspan : 2
						}]]

			});
	
			// 月份
	var month = new Ext.form.TextField({
				fieldLabel : "月份",
				id : 'month',
				readOnly : true,
				width : 100,
				anchor : '59%',
				name : 'month',
				value : getMonth(),
				listeners : {
					focus : function() {
						WdatePicker({
									alwaysUseStartDate : true,
									dateFmt : "yyyy-MM",
									isShowClear : false,
									onpicked : function(){
										text11.setValue(month.getValue().substring(0,4) + "年" + month.getValue().substring(5,7) + "月")
										query();
									}
								});
						this.blur();
					}
				}
			});
					// tbar
	var contbar = new Ext.Toolbar({
				items : ['月份:', month, '-', {
							id : 'btnQuery',
							iconCls : 'query',
							text : '查询',
							hidden : true,
							handler : query
						}, {
							id : 'btnAdd',
							iconCls : 'add',
							text : "新增",
							handler : addRec
						}, {
							id : 'btnDelete',
							iconCls : 'delete',
							text : "删除",
							handler : deleteRec

						}, {
							id : 'btnCancer',
							iconCls : 'cancer',
							text : "取消",
							handler : cancelRec

						}, '-', {
							id : 'btnSave',
							iconCls : 'save',
							text : "保存修改",
							handler : saveRec
						}, '-', {
							id : 'btnExport',
							iconCls : 'export',
							text : '导出',
							handler : exportRecord
						}]

			});
			function query()
			{
				Ext.Ajax.request({
					url : 'productionrec/getMainRecord.action',
					method : 'post',
					params : {
						month : month.getValue()
					},
					success : function(result,request){
						var obj = eval('(' + result.responseText+ ')')
						jdybId.setValue(obj.jdybId);
						text12.setValue(obj.tfMotionSum);
						text13.setValue(obj.tfMotionRight);
						text21.setValue(obj.ooMotionSum);
						text22.setValue(obj.ooMotionRight);
						text31.setValue(obj.ttMotionSum);
						text32.setValue(obj.ttMotionRight);
						text41.setValue(obj.oochzMotionSum);
						text42.setValue(obj.oochzMotionRight);
						text51.setValue(obj.ttchzMotionSum);
						text52.setValue(obj.ttchzMotionRight);
						text61.setValue(obj.errorMotionSum);
						text62.setValue(obj.errorMotionRight);
						managerBy.setValue(obj.managerBy);
						protectedBy.setValue(obj.protectedBy);
						managerName.setValue(obj.manager)
						protectedName.setValue(obj.protector)
						
						approveName.setValue(obj.approveName);
						approveBy.setValue(obj.approveBy);
						entryName.setValue(obj.entryName)
						entryBy.setValue(obj.entryBy)
						
						ds.baseParams = {
							jdybId : obj.jdybId
						}
						ds.load({
									params : {
										start : 0,
										limit : 18
									}
								})
					},
					failure : function(result,request){
					}
				})
			}
			// 增加
	function addRec() {
		var count = ds.getCount();
		var currentIndex = count;
		var ob = new item({
					'jdybDetailId' : null,
					'jdybId' : jdybId.getValue(),
					'time' : null,
					'address' : null,
					'equSummary' : null,
					'equName' : null,
					'equType' : null,
					'rightNum' : null,
					'wrongNum' : null,
					'errorReason' : null,
					'memo' : null
				});
		Grid.stopEditing();
		ds.insert(currentIndex, ob);
		sm.selectRow(currentIndex);
//		Grid.startEditing(currentIndex, 4);
	}
	
	// 删除记录
	var ids = new Array();
	function deleteRec() {
		Grid.stopEditing();
		var sm = Grid.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("jdybDetailId") != null) {
					ids.push(member.get('jdybDetailId'));
				}
				Grid.getStore().remove(member);
				Grid.getStore().getModifiedRecords().remove(member);
			}
		}
	}
		function saveRec() {
		Grid.stopEditing();
		var modifyRec = Grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			for (var i = 0; i < modifyRec.length; i++) {
				if (modifyRec[i].data.time == null
						|| modifyRec[i].data.time == "") {
					Ext.Msg.alert('提示信息', '时间不可为空，请输入！')
					return;
				}
			}
		}
		Ext.Msg.confirm('提示', '确认要保存修改吗？', function(id) {
					if (id == 'yes') {
						var addData = new Array();
							var updateData = new Array();
							for (var i = 0; i < modifyRec.length; i++) {

								if (modifyRec[i].get('jdybDetailId') != null) {
									updateData.push(modifyRec[i].data);
								} else {
									addData.push(modifyRec[i].data);
								}

							}
						Ext.Ajax.request({
									url : 'productionrec/saveModDetails.action',
									method : 'post',
									params : {
										jdybId : jdybId.getValue(),
										month : month.getValue(),
										tfMotionSum : text12.getValue(),
										tfMotionRight : text13.getValue(),
										ooMotionSum : text21.getValue(),
										ooMotionRight : text22.getValue(),
										ttMotionSum : text31.getValue(),
										ttMotionRight : text32.getValue(),
										oochzMotionSum : text41.getValue(),
										oochzMotionRight : text42.getValue(),
										ttchzMotionSum : text51.getValue(),
										ttchzMotionRight : text52.getValue(),
										errorMotionSum : text61.getValue(),
										errorMotionRight : text62.getValue(),
										managerBy : managerBy.getValue(),
										protectedBy : protectedBy.getValue(),
										approveBy : approveBy.getValue(),
										entryBy : entryBy.getValue(),								
										add : Ext.util.JSON.encode(addData),
										update : Ext.util.JSON
													.encode(updateData),
										ids : ids.join(",")
									},
									success : function(result, request) {
										var res = eval('(' + result.responseText + ')');
										Ext.Msg.alert('提示', res.msg);
										ds.rejectChanges();
												ids = [];
												query();
									},
									failure : function(result, request) {
										Ext.Msg.alert('提示', '数据保存修改失败！');
										ds.rejectChanges();
											ids = [];
											query();
									}
								})
					}
				})
	}
	// 取消
	function cancelRec() {
		var modifyRec = ds.getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			Ext.Msg.confirm('提示信息', '确定要放弃修改吗？', function(button) {
						if (button == 'yes') {
							query();
							ds.rejectChanges();
							ids = [];
						}
					})
		} else
			query();
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
	function exportRecord() {		
		Ext.Msg.confirm('提示', '确认要导出数据？', function(id) {
			if (id == 'yes') {
				var tableHeader = "<table border=1><tr><th colspan = 9>电力系统继电保护和安全自动装置动作统计分析月报表</th></tr>";
				var html = [tableHeader];
				html.push("<tr><th rowspan = 2>时间</th><th rowspan = 2>地点</th><th rowspan = 2>保护装置动作情况简述（包括一次系统情况，设备故障简历及对策）</th><th rowspan = 2>被保护设备名称</th><th rowspan = 2>保护与安全自动装置类型</th><th colspan = 2>装置的动作分析</th><th rowspan = 2>不正确动作责任分析</th><th rowspan = 2>备注</th></tr>")
				html.push("<tr><th>正确（次数）</th><th>不正确（次数）</th></tr>")
				for (var i = 0; i < ds.getTotalCount(); i++) {
					var rec = ds.getAt(i);
					rec.set('time',rec.get('time')== null ? "" : rec.get('time').substring(0,10)) ;
					rec.set('address',rec.get('address')== null ? "" : rec.get('address')) ;
					rec.set('equSummary',rec.get('equSummary')== null ? "" : rec.get('equSummary')) ;
					rec.set('equName',rec.get('equName')== null ? "" : rec.get('equName')) ;
					rec.set('equType',rec.get('equType')== null ? "" : rec.get('equType')) ;
					rec.set('rightNum',rec.get('rightNum')== null ? "" : rec.get('rightNum')) ;
					rec.set('wrongNum',rec.get('wrongNum')== null ? "" : rec.get('wrongNum')) ;
					rec.set('errorReason',rec.get('errorReason')== null ? "" : rec.get('errorReason')) ;
					rec.set('memo',rec.get('memo')== null ? "" : rec.get('memo')) ;
					html.push('<tr><td>' + rec.get('time') + '</td><td>' 							
							+ rec.get('address') + '</td><td>'
							+ rec.get('equSummary') + '</td><td>'
							+ rec.get('equName') + '</td><td>'
							+ rec.get('equType') + '</td><td>'
							+ rec.get('rightNum') + '</td><td>'
							+ rec.get('wrongNum') + '</td><td>'
							+ rec.get('errorReason') + '</td><td>'
							+ rec.get('memo') + '</td>'
							+ '</tr>')
				}
				html.push("<tr></tr>")
				html.push("<tr><td colspan = 9>1. "+ text11.getValue() + "35KV以下保护动作总数"+text12.getValue()+"，其中正确次数"+text13.getValue()+"。"+ "</td></tr>")
				html.push("<tr><td colspan = 9>2．110KV（包括66KV）保护动作总次数"+text21.getValue()+"，其中正确次数"+text22.getValue()+"。"+"</td></tr>")
				html.push("<tr><td colspan = 9>3．220KV及以上保护动作总次数"+text31.getValue()+"，其中正确次数"+text32.getValue()+"。"+"</td></tr>")
				html.push("<tr><td colspan = 9>4．110KV及以下重合闸动作总次数"+text41.getValue()+"，其中正确次数装置成功次数"+text42.getValue()+"。"+"</td></tr>")
				html.push("<tr><td colspan = 9>5．220KV及以上重合闸动作总次数"+text51.getValue()+"，其中正确次数"+text52.getValue()+"。"+"</td></tr>")
				html.push("<tr><td colspan = 9>6．故障录波器动作总次数"+text61.getValue()+"，录波完好次数"+text62.getValue()+"。"+"</td></tr>")
				html.push("<tr><td></td><td colspan = 2>审核人:"+managerName.getValue()+"</td><td colspan = 2>设备部:"+protectedName.getValue()+"</td><td colspan = 2>批准人:"+approveName.getValue()+"</td><td colspan = 2>填表人:"+entryName.getValue()+"</td></tr>")
				html.push('</table>');
				html = html.join(''); // 最后生成的HTML表格
				tableToExcel(html);
			}
		})
	}
			// 上部面板
	var panel1 = new Ext.form.FormPanel({
		frame : true,
		tbar : contbar,
		items : [fieldset]
	})
			var Grid = new Ext.grid.EditorGridPanel({
				sm : sm,
				ds : ds,
				cm : cm,
				autoScroll : true,
				bbar : gridbbar,				
				border : true,
				clicksToEdit : 1,
				enableColumnMove : false,
				plugins : [new Ext.ux.plugins.GroupHeaderGrid()]
			});
	
	var view = new Ext.Viewport({
		frame : true,
		layout : 'border',
		items : [{
			region : 'north',
			layout : 'fit',
			height : 230,
			items : [panel1]
		},{
			region : 'center',
			layout : 'fit',
			items : [Grid]
		}]
	})
	query();
})