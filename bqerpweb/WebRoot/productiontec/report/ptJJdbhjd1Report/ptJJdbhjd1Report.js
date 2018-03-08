Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function(){
	
	function numberFormat(value) {
		if (value == null || value == "") {
			return "0.0";
		} else if (value.toString() == "NaN") {
			return "";
		} else if (value.toString() == "Infinity") {
			return "";
		} else {
			value = (Math.round((value - 0) * 100)) / 100;
			value = (value == Math.floor(value))
					? value + ".00"
					: ((value * 10 == Math.floor(value * 10))
							? value + "0"
							: value);
			value = String(value);
			var ps = value.split(".");
			var whole = ps[0];
			var sub = ps[1] ? '.' + ps[1].substring(0, 2) : '.00';
			var v = whole + sub;
			return v;
		}
	}	
	function getMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t
		return s;
	}
	
	var item = new Ext.data.Record.create([{
		name : 'jdbhjd1Id'
	},{
		name : 'strMonth'
	},{
		name : 'countType'
	},{
		name : 'dynamoNum'
	},{
		name : 'transformerNum'
	},{
		name : 'fbzProtectNum'
	},{
		name : 'factoryProctectNum'
	},{
		name : 'blockNum'
	},{
		name : 'engineer'
	},{
		name : 'equDept'
	},{
		name : 'entryBy'
	},{
		name : 'tableFlag'
	},{
		name : 'typeName'
	},{
		name : 'engineerName'
	},{
		name : 'equDeptName'
	},{
		name : 'entryByName'
	},{
		name : 'entryDateString'
	}])
	
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'productionrec/getPtJJdbhjd1List.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list',
			totalCount : 'totalCount'
		},item)
	});
	
	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false
			});
	var cm = new Ext.grid.ColumnModel({
				columns : [sm, new Ext.grid.RowNumberer({
									header : "行号",
									width : 31
								}), {
							header : 'ID',
							dataIndex : 'jdbhjd2Id',
							hidden : true
						}, {
							header : '月份',
							dataIndex : 'strMonth',
							hidden : true,
							align : 'center'
						}, {
							header : ' ',
							dataIndex : 'typeName',
							align : 'center'							
						}, {
							header : '发电机保护',
							dataIndex : 'dynamoNum',
							align : 'right',							
							editor : new Ext.form.NumberField({
										style : 'cursor:pointer',
										allowBlank : false,
										allowDecimals : false,
										allowNegative : false
									})
						}, {
							header : '变压器保护',
							dataIndex : 'transformerNum',
							align : 'right',
							editor : new Ext.form.NumberField({
										style : 'cursor:pointer',
										allowBlank : false,
										allowDecimals : false,
										allowNegative : false
									})
						}, {
							header : '发变组保护',
							dataIndex : 'fbzProtectNum',
							align : 'right',
							editor : new Ext.form.NumberField({
										style : 'cursor:pointer',
										allowBlank : false,
										allowDecimals : false,
										allowNegative : false
									})
						}, {
							header : '6KV厂用电保护',
							dataIndex : 'factoryProctectNum',
							align : 'right',
							editor : new Ext.form.NumberField({
										style : 'cursor:pointer',
										allowBlank : false,
										allowDecimals : false,
										allowNegative : false
									})
						}, {
							header : '机组故障录波器',
							dataIndex : 'blockNum',
							align : 'right',
							editor : new Ext.form.NumberField({
										style : 'cursor:pointer',
										allowBlank : false,
										allowDecimals : false,
										allowNegative : false
									})
						}]
			});

	var gridbbar = new Ext.PagingToolbar({
				pageSize : 18,
				store : ds,
				displayInfo : true,
				displayMsg : "显示第{0}条到{1}条，共{2}条",
				emptyMsg : "没有记录"
			});
			
	// 上报单位
	var dept = new Ext.form.TextField({
				id : 'dept',
				name : 'dept',
				value : '大唐灞桥热电厂',
				disabled : true,
				width : 100
			})
	// 上报时间
	var month = new Ext.form.TextField({
				fieldLabel : "月份",
				id : 'month',
				readOnly : true,
				width : 90,
				name : 'month',
				value : getMonth(),
				listeners : {
					focus : function() {
						WdatePicker({
									alwaysUseStartDate : true,
									dateFmt : "yyyy-MM",
									isShowClear : false,
									onpicked : function(){
										query();
									}
								});
						this.blur();
					}
				}
			});
		var engineerName = new Ext.form.TriggerField({
					id : 'engineerName',
					onTriggerClick : selectWorkerWin,
					readOnly : true,
					width : 90
			})
		var  engineer = new Ext.form.Hidden({
			id : 'engineer'
		})
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
			engineerName.setValue(person.workerName);
			engineer.setValue(person.workerCode);

		}
	}
	var equDeptName = new Ext.form.TriggerField({
					id : 'equDeptName',
					onTriggerClick : selectWin,
					readOnly : true,
					width : 90
			})
		var  equDept = new Ext.form.Hidden({
			id : 'equDept'
		})
		function selectWin() {
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
			equDeptName.setValue(person.workerName);
			equDept.setValue(person.workerCode);

		}
	}
	// tbar
	var contbar = new Ext.Toolbar({
				items : ['单位:', dept, '-', '月份:', month, '-',
				'总工程师：',engineer, engineerName,'-','设备部：',equDept,equDeptName,'-',{
							id : 'btnQuery',
							iconCls : 'query',
							text : '查询',
							hidden : true,
							handler : query
						}, {
							id : 'btnCancer',
							iconCls : 'cancer',
							text : "取消",
							handler : cancelRec

						}, '-', {
							id : 'btnSave',
							iconCls : 'save',
							text : "保存",
							handler : saveRec
						}, '-', {
							id : 'btnExport',
							iconCls : 'export',
							text : '导出',
							handler : exportRecord
						}]

			});

	// 保存
	function saveRec() {
		Grid.stopEditing();
		Ext.Msg.confirm('提示', '确定要保存吗？', function(button) {
					if (button == 'yes') {
						var addData = new Array();
						var updateData = new Array();
						for (var i = 0; i < ds.getTotalCount(); i++) {
							if (ds.getAt(i).get('jdbhjd1Id') != null) {
								updateData.push(ds.getAt(i).data);
							} else {
								addData.push(ds.getAt(i).data);
							}

						}
						Ext.Ajax.request({
									url : 'productionrec/savePtJjdbhjd1Mod.action',
									method : 'post',
									params : {
										add : Ext.util.JSON.encode(addData),
										update : Ext.util.JSON
												.encode(updateData),
										month : month.getValue(),
										tableFlag : tableFlag,
										engineer : engineer.getValue(),
										equDept : equDept.getValue()
									},
									success : function(result, request) {
										var o = eval('(' + result.responseText
												+ ')');
										Ext.Msg.alert("提示", o.msg);
										ds.rejectChanges();
										ds.reload();										
									},
									failure : function(result, request) {
										Ext.Msg.alert("提示", "数据保存失败！");
//										ds.rejectChanges();
//										ds.reload();
									}
								})
					}
				})
	}
	// 取消
	function cancelRec() {
		var modifyRec = ds.getModifiedRecords();
		if (modifyRec.length > 0) {
			Ext.Msg.confirm('提示信息', '确定要放弃修改吗？', function(button) {
						if (button == 'yes') {
							ds.reload();
							ds.rejectChanges();
						}
					})
		}else
			ds.reload()		
	}

	function query() {
		ds.baseParams = {
			month : month.getValue(),
			tableFlag : tableFlag
		}
		ds.load({
					params : {
						start : 0,
						limit : 18
					}
				})
	}
	ds.on('load', function() {
				if (ds.getTotalCount() > 0) {
					equDeptName.setValue(ds.getAt(0).get('equDeptName'))
					equDept.setValue(ds.getAt(0).get('equDept'))
					engineerName.setValue(ds.getAt(0).get('engineerName'))
					engineer.setValue(ds.getAt(0).get('engineer'))
				}
			})
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
		if (ds == null || ds.getTotalCount() == 0) {
			Ext.Msg.alert('提示信息', '无数据进行导出！');
			return;
		}
		
		Ext.Msg.confirm('提示', '确认要导出数据？', function(id) {
			if (id == 'yes') {
				var tableHeader = '';
				if(tableFlag == '1')
				{
					tableHeader = "<table border=1><tr><th colspan = 6>大唐集团继电保护监督报表1</th></tr>";
					tableHeader += "<tr><th colspan = 6>机组继电保护及安全自动装置校验计划完成情况月报表</th></tr>"
				}
				else
				{
					tableHeader = "<table border=1><tr><th colspan = 6>大唐集团继电保护监督报表3</th></tr>";
					tableHeader += "<tr><th colspan = 6>机组继电保护及安全自动装置动作情况月报表</th></tr>"
				}
				var html = [tableHeader];		
				html
						.push("<tr><th colspan = 3>企业名称:大唐灞桥热电厂</th><th></th>"
								+ "<th colspan = 2>报表时间："
								+ ds.getAt(0).get('entryDateString') + "</th></tr>")
				html
						.push('<tr><th></th><th>发电机保护</th><th>变压器保护</th><th>发变组保护</th><th>6KV厂用电保护</th><th>机组故障录波器</th></tr>')
				for (var i = 0; i < ds.getTotalCount(); i++) {
					var rec = ds.getAt(i);
					html.push('<tr><td>' + rec.get('typeName') + '</td><td>' 
							+ rec.get('dynamoNum') + '</td><td>'
							+ rec.get('transformerNum') + '</td><td>'
							+ rec.get('fbzProtectNum') + '</td><td>'
							+ rec.get('factoryProctectNum') + '</td><td>'
							+ rec.get('blockNum') + '</td>'
							+ '</tr>')
				}
				html.push('<tr><td colspan = 2>总工程师：'+ds.getAt(0).get('engineerName')+'</td>' +
						'<td colspan = 2>设备部：'+ds.getAt(0).get('equDeptName')+'</td>' +
						'<td colspan = 2>填表：'+ds.getAt(0).get('entryByName')+'</td></tr>')
				html.push('</table>');
				html = html.join(''); // 最后生成的HTML表格
				tableToExcel(html);
			}
		})
	}

	var Grid = new Ext.grid.EditorGridPanel({
				sm : sm,
				ds : ds,
				cm : cm,
				autoScroll : true,
				bbar : gridbbar,
				tbar : contbar,
				border : true,
				clicksToEdit : 1,
				enableColumnMove : false,
				viewConfig : {
				}
			});
	if (tableFlag == '3') {
		Grid.on('beforeedit', function(e) {
					if (e.row == 2) {
						return false;
					}
				})
		Grid.on('afteredit', function(e) {
					var index = cm.getDataIndex(e.column);
					if(ds.getAt(0).get(index) != null && ds.getAt(0).get(index) != 0
					&& ds.getAt(1).get(index) != null && ds.getAt(1).get(index) != 0)
					{
						
						ds.getAt(2).set(index,numberFormat(ds.getAt(1).get(index) / ds.getAt(0).get(index)));
					}
				})
	}
	
	var view = new Ext.Viewport({
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
		query();
})