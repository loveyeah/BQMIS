Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	var grid;
	var ds ;

	// 创建一个对象
	var obj = new Ext.data.Record.create([{
		// 薪酬ID
		name : 'salaryId'
	},{
		// 人员id
		name : 'empId'
	},{
		// 月份
		name : 'salaryMonth'
	},{
		// 总工资
		name : 'totalSalary'
	},{
		// 薪酬明细类别
		name : 'salaryTypeId'
	},{
		// 金额
		name : 'salaryMoney'
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
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("POST", 'comm/getCurrentSessionEmployee.action', false);
		conn.send(null);
		if (conn.status == "200") {
			var result = eval('(' + conn.responseText + ')');
			if (result.deptId) {
				// 设部门id
				deptId.setValue(result.deptId);
				deptName.setValue(result.deptName);
				query();
			}
		}
	}
	
	function query(){
//		Ext.Msg.wait("正在查询数据!请等待...");
		if(deptName.getValue() == null || deptName.getValue() == '')
		{
			Ext.Msg.alert('提示','请先选择部门!')
			return;
		}
		Ext.Ajax.request({
			url : 'hr/findEmpSalaryList.action',
			params : {
				deptId : deptId.getValue(),
				month : time.getValue()
			},
			method : 'post',
			waitMsg : '正在加载数据...',
			success : function(result, request) {
				var json = eval('(' + result.responseText + ')');
				document.getElementById("gridDiv").innerHTML = "";
				ds = new Ext.data.JsonStore({
					data : json.data,
					fields : json.fieldsNames
				});
				var wd = Ext.get('gridDiv').getWidth()
				var ht = Ext.get('gridDiv').getHeight()	
				var sm = new Ext.grid.CellSelectionModel({});
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
						columns : json.columModel
					}),
					enableColumnMove : false,  
					ds : ds,
					clicksToEdit : 1
				});  
				grid.render();
				
				grid.on('afteredit', function(e) {
					var count = 0.0;
					for (var j = 6; j <= grid.getColumnModel().getColumnCount()- 2; j++) {
						var index = grid.getColumnModel().getDataIndex(j);
						count += e.record.get(index) - 0;
					}
					e.record.set('totalSalary',count);
								})
			},
			failure : function(result, request) {
				Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
//				Ext.Msg.hide();
			}
		});
	}	
	
	// 部门
	var deptName = new Ext.form.TextField({
				id : 'deptName',
				fieldLabel : "部门<font color='red'>*</font>",
				isFormField : true,
				maxLength : 50,
				readOnly : true,
				allowBlank : false,
				anchor : '100%',
				name : 'deptName'
			});
	// 隐藏部门
	var deptId = new Ext.form.Hidden({
				id : 'deptId',
				name : 'deptId'
			});
	deptName.onClick(selectDept);
	// 月份
	var time = new Ext.form.TextField({
		id : 'time',
		allowBlank : true,
		readOnly : true,
		value : getMonth(),
		width : 100,
		listeners : {
			focus : function() {									
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM',
					isShowClear : false,
					alwaysUseStartDate : true,
					onclearing : function() {									
									}
									
				});
				this.blur();
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
	var btnexport = new Ext.Button({
		text : '导出',
		iconCls : 'export',
		handler : function() {
			exportRecord();
		}
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
							tbar : ['部门：',deptName,deptId,'-','月份：',time,{
									xtype : "tbseparator"
								},btnquery, {
									xtype : "tbseparator"
								}, btnsave, {
									xtype : "tbseparator"
								},btnexport],
							items : [
								{
										html : '<div id="gridDiv"></div>'
									}
									]
						})]
			});
	Ext.get('gridDiv').setWidth(Ext.get('panel').getWidth());
	Ext.get('gridDiv').setHeight(Ext.get('panel').getHeight() - 25);	
	
	function saveRecords() {
		if (ds == null) {
			Ext.Msg.alert('提示信息', '无数据进行保存！')
			return;
		}
		if (ds.getTotalCount() == 0) {
			Ext.Msg.alert('提示信息', '无数据进行保存！')
			return;
		}
		var mod = new Array();
		for (var i = 0; i <= ds.getTotalCount() - 1; i++) {
			var salaryId = ds.getAt(i).get('salaryId');
			var empId = ds.getAt(i).get('empId');
			var salaryMonth = ds.getAt(i).get('month');
			var totalSalary = ds.getAt(i).get('totalSalary');
			// 种类
			var salaryTypeId = new Array();
			// 值
			var salaryMoney = new Array();
			for (var j = 6; j <= grid.getColumnModel().getColumnCount() - 2; j++) {

				var dataIndex = grid.getColumnModel().getDataIndex(j);
				salaryTypeId.push(dataIndex.substring(4))
				salaryMoney.push(grid.getStore().getAt(i).get(dataIndex));

			}
			obj = {
				salaryId : salaryId,
				empId : empId,
				salaryMonth : salaryMonth,
				totalSalary : totalSalary,
				salaryTypeId : salaryTypeId,
				salaryMoney : salaryMoney
			};
			mod.push(obj);
		}

		var method = '';
		if (ds.getAt(0).get('salaryId') == null
				|| ds.getAt(0).get('salaryId') == '')
			method = 'add';
		else
			method = 'update';
		Ext.Msg.confirm('提示信息', '确认要保存吗？', function(id) {
					if (id == 'yes') {
						Ext.Ajax.request({
									url : 'hr/saveSalaryAmount.action',
									method : 'post',
									params : {
										mod : Ext.util.JSON.encode(mod),
										method : method
									},
									success : function(result, request) {
										Ext.Msg.alert('提示信息', '数据保存成功！');
										query();
									},
									failure : function(result, request) {
										Ext.Msg.alert('提示信息', '数据保存失败！');

									}
								})
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
	function exportRecord()
	{
		if (ds == null || ds.getTotalCount() == 0) {
				Ext.Msg.alert('提示信息', '无数据进行导出！');
				return;
			}
		
			Ext.Msg.confirm('提示信息', '确认要导出数据吗？', function(id) {
				if (id == 'yes') {
					var typeName  = '';
					for(var j = 6; j <= grid.getColumnModel().getColumnCount() - 2; j++)
					{
						typeName += '<th>' + grid.getColumnModel().getColumnHeader(j) + '</th>';
					}
					var tableHeader = '<table border=1><tr><th>人员姓名</th><th>月份</th>' + typeName + 
							'<th>总工资</th></tr>';
					var html = [tableHeader];
					for (var i = 0; i < ds.getTotalCount(); i += 1) {
						var rc = ds.getAt(i);
						var salary = '';
						for(var j = 6; j <= grid.getColumnModel().getColumnCount() - 2; j++)
						{
							var type = grid.getColumnModel().getDataIndex(j);
							salary += '<td>' + rc.get(type) + '</td>';														
						}
						var rowValue = '<tr><td>' + rc.get('empName')+ '</td>' +
										'<td>' + rc.get('month')+ '</td>' +
										salary + 
										'<td>' + rc.get('totalSalary')+ '</td>' +
										'</tr>';
						html.push(rowValue );
					}
					html.push('</table>');
					html = html.join(''); // 最后生成的HTML表格
					tableToExcel(html);
				}
			})
	}
	
	// 选择部门处理函数
	function selectDept() {
		var args = new Object();
		args.selectModel = "single";
		args.rootNode = {
			id : '0',
			text : '灞桥热电厂'
		};
        args.onlyLeaf = false;
		var object = window
				.showModalDialog(
						'../../../../comm/jsp/hr/dept/dept.jsp',
						args,
						'dialogWidth=800px;dialogHeight=520px;center=yes;help=no;resizable=no;status=no;');
		// 根据返回值设置画面的值
		if (object) {
			if (typeof(object.names) != "undefined") {
				deptName.setValue(object.names);
				deptId.setValue(object.ids);
			}			
		}
	}
	
	getWorkCode();
});