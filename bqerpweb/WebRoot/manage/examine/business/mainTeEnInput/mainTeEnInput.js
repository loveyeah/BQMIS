Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	function getMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		return s;
	}
	var onQuery = false;
	var grid;
	var ds ;
	var time = getMonth();
//	alert(reportId)
	var method = '';
	var topicNs = new Power.topic({width:150,hidden : true});
	var topicLabel = new Ext.form.Label({
		hidden : true,
		text : '主题：'
	})
	
	
	// 判断为新增还是修改
	function judgeAddOrUpdate(){
		if(monthDate.isVisible())
			time = monthDate.getValue()
		else
			time = yearDate.getValue()
		Ext.Ajax.request({
			url : 'managexam/judgeAddOrUpdate.action',
			method : 'post',
			params : {
				reportId : reportId,
				yearMonth : time
			},
			success : function(response,options){
				var result = Ext.util.JSON.decode(response.responseText);
				method = result.method;
				query()
			},
			failure : function()
			{
				Ext.Msg.alert('提示','出现未知错误！');
			}
		})
	}
	var monthDate = new Ext.form.TextField({
		name : '_monthDate',
		value : getMonth(),
		id : 'monthDate',
		fieldLabel : "月份",
		style : 'cursor:pointer',
		cls : 'Wdate',
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M',
					dateFmt : 'yyyy-MM',
					alwaysUseStartDate : false,
					isShowClear : false
				});
				this.blur()
			}
		}
	});
	
	var yearDate = new Ext.form.TextField({
		id : 'yearDate',
		name : '_yearDate',
		fieldLabel : "年份",
		hidden : true,
		style : 'cursor:pointer',
		cls : 'Wdate',
		// width : 150,
		value : new Date().getFullYear(),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y',
					dateFmt : 'yyyy',
					alwaysUseStartDate : false,
					isShowClear : false
				});
				this.blur()
				time = yearDate.getValue()
			}
		}
	});
	var yearRadio = new Ext.form.Radio({
			id : 'yearRadio',
			boxLabel : '年度',
			inputValue : 1,
			name : 'radio'
		})
	var monthRadio = new Ext.form.Radio({
			id : 'monthRadio',
			inputValue : 2,
			boxLabel : '月份',
			name : 'radio',
			checked : true
		})
	var radioGuoup = new Ext.form.RadioGroup({
		id : 'radioGuoup',
		allowBlank : false,
		items : [yearRadio,monthRadio]
	})
	
	yearRadio.on('check',function(){
		if(yearRadio.getValue())
		{
			yearDate.setVisible(true)
			monthDate.setVisible(false)
			time = yearDate.getValue()
		}else{
			yearDate.setVisible(false)
			monthDate.setVisible(true)
			time = monthDate.getValue()
		}
	})
	
	if(reportId == '2')
	{
		monthRadio.setValue(false)
		yearRadio.setValue(true);
		monthRadio.setVisible(false)
		monthRadio.boxLabel = ''
		
		topicLabel.setVisible(true)
		topicNs.combo.setVisible(true)
	}
	
	
	

	// 创建一个对象
	var obj = new Ext.data.Record.create([{
		// 数值录入Id
		name : 'reId'
	},{
		// 指标ID
		name : 'itemId'
	},{
		// 机组ID
		name : 'blockId'
	},{
		// 报表ID
		name : 'reportId'
	},{
		// 年月
		name : 'yearMonth'
	},{
		// 数值
		name : 'value'
	}])
	
	
	
	var sm = new Ext.grid.CellSelectionModel();
	function query(){
		if(onQuery)
			Ext.Msg.wait("正在查询数据!请等待...");
		
		
		Ext.Ajax.request({
			url : 'managexam/getReportItemInputValue.action',
			params : {
				reportId : reportId,
				yearMonth : time,
				topicId : topicNs.combo.getValue()
			},
			method : 'post',
			success : function(result, request) {
				if(onQuery)
				{
					onQuery = false;
					Ext.Msg.hide()
				}
				var json = Ext.util.JSON.decode(result.responseText);
				
				document.getElementById("gridDiv").innerHTML = "";
				
				ds = new Ext.data.JsonStore({
					data : json.data,
					fields : json.fieldsNames
				});
				var wd = Ext.get('gridDiv').getWidth()
				var ht = Ext.get('gridDiv').getHeight()	
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
							
			},
			failure : function(result, request) {
				Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
			}
		});
	}	
	
	
	
	
	
	// 查询按钮
	var queryBtu = new Ext.Button({
		id : 'queryBtu',
		text : '查询',
		iconCls : 'query',
		handler : function(){
			onQuery = true
			judgeAddOrUpdate()
		}
	})
	var saveBtu = new Ext.Button({
		id : 'saveBtu',
		text  : '保存',
		iconCls : 'save',
		handler : saveRecords
	})
	var clearBtu = new Ext.Button({
		id : 'clearBtu',
		text : '数据清空',
		iconCls : 'clear',
		handler : clearFun
	})
	var btnexport = new Ext.Button({
		text : '导出',
		iconCls : 'export',
		handler : exportRecord
	})
	var tbar = new Ext.Toolbar({
		items : [radioGuoup,monthDate,yearDate,topicLabel,topicNs.combo,'-',queryBtu,saveBtu,clearBtu,btnexport]
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
							tbar : tbar,
							items : [
								{
										html : '<div id="gridDiv"></div>'
									}
									]
						})]
			});
	Ext.get('gridDiv').setWidth(Ext.get('panel').getWidth());
	Ext.get('gridDiv').setHeight(Ext.get('panel').getHeight() - 35);	
	
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
		for (var i = 0; i <= ds.getCount() - 1; i++) {
			var reIdTemp = null;
			var itemIdTemp = ds.getAt(i).get('itemId');
			var reportIdTemp = ds.getAt(i).get('reportId');
			var yearMonthTemp = ds.getAt(i).get('yearMonth');
			var blockIdTemp = null;
			var valueTemp = null
			for(var j = 9; j <= grid.getColumnModel().getColumnCount() - 1; j++)
			{
				blockIdTemp = grid.getColumnModel().getDataIndex(j);
				valueTemp = grid.getStore().getAt(i).get(blockIdTemp);
				obj = {
					'reId' : reIdTemp,
					'itemId' : itemIdTemp,
					'blockId' : blockIdTemp,
					'reportId' : reportIdTemp,
					'yearMonth' : yearMonthTemp,
					'value' : valueTemp
				}
				mod.push(obj);
			}
			
			
		}
		
		Ext.Msg.confirm('提示信息', '确认要保存吗？', function(id) {
					if (id == 'yes') {
						Ext.Ajax.request({
									url : 'managexam/saveReportItemInput.action',
									method : 'post',
									params : {
										mod : Ext.util.JSON.encode(mod),
										method : method
									},
									success : function(result, request) {
										Ext.Msg.alert('提示信息', '数据保存成功！');
										judgeAddOrUpdate()
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
					var blockName  = '';
					for(var j = 9; j <= grid.getColumnModel().getColumnCount() - 1; j++)
					{
						blockName += '<th>' + grid.getColumnModel().getColumnHeader(j) + '</th>';
					}
					var tableHeader = '';
					if(reportId == 2)
						tableHeader = '<table border=1><tr><th>主题</th><th>指标</th><th>单位</th>' + blockName + 
							'</tr>';
					else
						tableHeader = '<table border=1><tr><th>指标</th><th>单位</th>' + blockName + 
							'</tr>';
					var html = [tableHeader];
					for (var i = 0; i < ds.getTotalCount(); i += 1) {
						var rc = ds.getAt(i);
						var value = '';
						for(var j = 9; j <= grid.getColumnModel().getColumnCount() - 1; j++)
						{
							var blockId = grid.getColumnModel().getDataIndex(j);
							value += '<td>' + rc.get(blockId) + '</td>';														
						}
						var rowValue = ''
						if(reportId == 2)
							rowValue = '<tr><td>' + rc.get('topicName')+ '</td><td>' + rc.get('alias')+ '</td>' +
										'<td>' + rc.get('unitName')+ '</td>' +
										value + 
										'</tr>';
						else
							rowValue = '<tr><td>' + rc.get('alias')+ '</td>' +
										'<td>' + rc.get('unitName')+ '</td>' +
										value + 
										'</tr>';
						html.push(rowValue );
					}
					html.push('</table>');
					html = html.join(''); // 最后生成的HTML表格
					tableToExcel(html);
				}
			})
	}
	
	function clearFun(){
		if(method == 'add'){
			Ext.Msg.alert('提示','数据库中该年月的数据尚未保存！');
		}else{
			Ext.Msg.confirm('提示','确定要清空数据吗？',function(button){
				if(button == 'yes'){
					Ext.Ajax.request({
						url : 'managexam/clearReportItemInput.action',
						method  : 'post',
						params : {
						reportId : reportId,
						yearMonth : time
						},
						success : function(response,options){
							Ext.Msg.alert('提示','数据清空成功！');
							judgeAddOrUpdate()
						},
						failure : function(){
							Ext.Msg.alert('提示','数据清空失败！');
						}
					})
				}
			})
		}
	}
	
	judgeAddOrUpdate()
});
