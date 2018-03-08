Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var cost = new Ext.data.Record.create([{
		name : 'deptCode'
	}, {
		name : 'deptName'
	}, {
		name : 'costCode'
	}, {
		name : 'costName'
	}, {
		name : 'costQty'
	}, {
		name : 'budgect'
	}]);

	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'resource/getCostFrom.action',
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, cost)
	});

	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) {
			}
		}
	});

	var colModel = new Ext.grid.ColumnModel([sm, {
		id : 'deptName',
		header : '部门名称',
		dataIndex : 'deptName',
		sortable : true,
		width : 150
	}, {
		id : 'costName',
		header : '费用来源',
		dataIndex : 'costName',
		width : 150,
		sortable : true,
		renderer:function(value){
		return "<pre>"+value+"</pre>";
		}
	}, {
		header : '已发生',
		dataIndex : 'costQty',
		width : 150
	}, {
		header : '预算费用',
		dataIndex : 'budgect',
		width : 150

	}]);
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		s+='00'+':'+'00'+':'+'01'
		return s;
	}
	function getTime() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate() ;
		s += (t > 9 ? "" : "0") + t + " ";
		s+='00'+':'+'00'+':'+'59'
		
		return s;
	}

	var strDate = new Ext.form.TextField({
		id : 'fromDate',
		name : 'fromDate',
		fieldLabel : "开始时间",
		style : 'cursor:pointer',
		width : 150,
		cls : 'Wdate',
		value : getDate(),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d 00:00:01',
					dateFmt : 'yyyy-MM-dd HH:mm:ss',
					alwaysUseStartDate : true
				});
			}
		}
	});
	var endDate = new Ext.form.TextField({
		name : 'toDate',
		value : getTime(),
		width : 150,
		id : 'toDate',
		fieldLabel : "结束时间",
		style : 'cursor:pointer',
		cls : 'Wdate',
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d 00:00:59',
					dateFmt : 'yyyy-MM-dd HH:mm:ss',
					alwaysUseStartDate : true
				});
			}
		}
	});
	var outSTime = new Ext.form.TextField({
		id : 'Date',
		name : 'fromDate',
		fieldLabel : "开始时间",
		style : 'cursor:pointer',
		width : 150,
		cls : 'Wdate',
		value : getDate(),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d 00:00:01',
					dateFmt : 'yyyy-MM-dd HH:mm:ss',
					alwaysUseStartDate : true
				});
			}
		}
	});
	var outETime = new Ext.form.TextField({
		name : 'todate',
		value : getTime(),
		width : 150,
		id : 'toDate',
		fieldLabel : "结束时间",
		style : 'cursor:pointer',
		cls : 'Wdate',
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d 00:00:59',
					dateFmt : 'yyyy-MM-dd HH:mm:ss',
					alwaysUseStartDate : true
				});
			}
		}
	});
   
	function choseItem() {
		var args = {
			onlyLeaf : 'false'
		}
		var item = window
				.showModalDialog(
						'../../../../comm/jsp/item/budget/budget.jsp',
						args,
						'dialogWidth:560px;dialogHeight:440px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(item) != "undefined") {
			costFromName.setValue(item.itemName);
			costFrom.setValue(item.itemCode);
		}
	}

	// 费用来源
	var costFromName = new Ext.form.TriggerField({
		fieldLabel : '费用来源<font color ="red">*</font>',
		displayField : 'text',
		valueField : 'id',
		blankText : '请选择',
		emptyText : '请选择',
		anchor : '100%',
		name : "itemName",
		// value : '生产类费用',
		allowBlank : false,
		readOnly : true,
		hidden : false,
		hideLabel : false
	});
	costFromName.onTriggerClick = choseItem;
	var costFrom = new Ext.form.Hidden({
		name : 'mr.itemCode',
		value : ''
	})
	function choseDept() {
		var args = {
			selectModel : 'single',
			rootNode : {
				id : '0',
				text : '合肥电厂'
			}
		}
		var dept = window
				.showModalDialog(
						'../../../../comm/jsp/hr/dept/dept.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(dept) != "undefined") {
			Dept.setValue(dept.names);
			deptCode.setValue(dept.codes);
		}
	}
	// 部门
	var Dept = new Ext.form.TriggerField({
		fieldLabel : '部门',
		width : 150,
		id : "DeptId",
		displayField : 'text',
		valueField : 'id',
		hiddenName : 'mrDept',
		blankText : '请选择',
		emptyText : '请选择',
		maxLength : 100,
		anchor : '100%',
		readOnly : true
	});
	Dept.onTriggerClick = choseDept;
	var deptCode = new Ext.form.Hidden({
		hiddenName : 'mrDept'
	})

	colModel.defaultSortable = true;
	var tbar = new Ext.Toolbar({
		items : ['财务审核日期从:', strDate,'-', '至:', endDate,'-', '部门编码:', Dept,'-', '费用来源:',
				costFromName, {
					text : "查询",
					iconCls : 'query',
					handler : function() {
						Ext.Msg.wait("正在查询数据,请等待...");
						ds.load();
						Ext.MessageBox.hide();
						
					}
				},'-', {
					text : "导出",
					iconCls : 'export',
					handler : function() {
						outdata();

					}
				}]
	});
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
	// 导出

	function outdata() {
		if (ds.getTotalCount() == 0) {
			Ext.MessageBox.alert("提示", "无数据可导出！")
			return;
		}
		Ext.Msg.confirm('提示', '确认要导出数据？', function(id) {
			if (id == 'yes') {
				var tableHeader = "<table border=1><tr><th colspan = 4>材料领用费用查询</th></tr>";
				var html = [tableHeader];
				html
						.push("<tr><th>部门名称</th><th>费用来源</th><th>已发生</th><th>预算费用</th></tr>")
				for (var i = 0; i < ds.getTotalCount(); i++) {
					var rec = ds.getAt(i);
					rec.set('deptName', rec.get('deptName') == null ? "" : rec
							.get('deptName'));
					rec.set('costName', rec.get('costName') == null ? "" : rec
							.get('costName'));
					rec.set('costQty', rec.get('costQty') == null ? "" : rec
							.get('costQty'));
					rec.set('budgect', rec.get('budgect') == null ? "" : rec
							.get('budgect'));

					html.push('<tr><td >' + rec.get('deptName') + '</td>'
							+ '<td >' + rec.get('costName') + '</td>' + '<td >'
							+ rec.get('costQty') + '</td>' + '<td>'
							+ rec.get('budgect') + '</td>' + '</tr>')
				}
				html.push('</table>');
				html = html.join(''); // 最后生成的HTML表格
				tableToExcel(html);
			}
		})
	}

	ds.on("beforeload", function() {
		Ext.apply(this.baseParams, {
			strDate : strDate.getValue(),
			endDate : endDate.getValue(),
			outSTime:outSTime.getValue(),
			outETime:outETime.getValue(),
			deptCode : deptCode.getValue(),
			costFrom : costFrom.getValue()

		});

	});
   
   
	var costGrid = new Ext.grid.GridPanel({
		id : 'weather-grid',
		autoScroll : true,
		ds : ds,
		cm : colModel,
		sm : sm,
		tbar : tbar,
		border : true
	});

	var viewport = new Ext.Viewport({
		region : "center",
		layout : 'fit',
		autoWidth : true,
		autoHeight : true,
		fitToFrame : true,
		items : [costGrid]
	});
    var headTbar = new Ext.Toolbar({
      	id : 'secondTbar',
		renderTo : costGrid.tbar,
		items : ['出库日期查询从:',outSTime,'-','至:',outETime]
	});
	setTimeout(function() {
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut({
			remove : true
		});
	}, 250);
});