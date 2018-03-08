Ext.onReady(function() {
	function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";
		// 初始化时间
		Year = DateIn.getYear();
		Month = DateIn.getMonth() + 1;
		Day = DateIn.getDate();
		CurrentDate = Year + "-";
		if (Month >= 10) {
			CurrentDate = CurrentDate + Month + "-";
		} else {
			CurrentDate = CurrentDate + "0" + Month + "-";
		}
		if (Day >= 10) {
			CurrentDate = CurrentDate + Day;
		} else {
			CurrentDate = CurrentDate + "0" + Day;
		}
		return CurrentDate;
	}
	
	function getprioDay() {
		var d;
		d = new Date();
		t= d.add(Date.DAY,-2);
		return ChangeDateToString(t);
	}
	
	var centersm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});

	var storelist = new Ext.data.Record.create([centersm, {
				name : 'itemName',
				mapping:0
			}, {
				name : 'unitName',
				mapping:1
			}, {
				name : 'ji11',
				mapping:2
			}, {
				name : 'ji12',
				mapping:3
			}, {
				name : 'heji1112',
				mapping:4
			}, {
				name : 'ji1',
				mapping:5
			}, {
				name : 'ji2',
				mapping:6
			}, {
				name : 'heji12',
				mapping:7
			}, {
				name : 'heji',
				mapping:8
			}
			]);

	var centerds = new Ext.data.JsonStore({
				url : 'manager/getDailyReport.action',
				root : '',
				totalProperty : 'totalCount',
				fields : storelist
			});

	centerds.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							reportCode : reportNameBox.getValue(),//modify by wpzhu 20100830
							reportdate : timeSelect.getValue()
						});
			});
	//-----------add by  wpzhu ----------
	var rec = Ext.data.Record.create([{
				name : 'reportCode'
			}, {
				name : 'reportName'
			}, {
				name : 'reportType'
			}]);

	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manager/getReportName.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'totalCount',
							root : 'list'
						}, rec)
			});
			
  
	var reportNameBox = new Ext.form.ComboBox({
				fieldLabel : '报表名称',
				store : ds,
				id : 'template',
				name : 'template',
				valueField : "reportCode",
				displayField : "reportName",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				editable : false,
				width : 200,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : true,
				emptyText : '请选择',
				resizable : true,
				listeners : {
					select : function(index, record) {
						centerds.reload();
//					reportCode=this.value;
						
					
					}
				}
			});		
			
		ds.load({
				params : {
					reportType : '1',//日指标
					reportCode:"'36','38'"//指标编码
					
				}
			});	
			
	//--------------------------------------------------------------

	var timeSelect = new Ext.form.TextField({
				id : 'awardDate',
				fieldLabel : "时间",
				name : 'awardDate',
				style : 'cursor:pointer',
				readOnly : true,
				value : getprioDay(),
				anchor : '95%',
				allowBlank : true,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : true
								});
						this.blur();
					}
				}
			});
    centerds.load();
	var ctbtnQuery = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : function() {
					centerds.load();
				}
			});

	var ctbtnOut = new Ext.Button({
				id : 'reflesh',
				text : '导出',
				iconCls : 'export',
				handler : function() {
					myExport();
				}
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
				alert("您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}
	function NullToSpace(tobeparse) {
		if (tobeparse == null) {
			return "";
		} else {
			return tobeparse;
		}
	}
	function myExport() {
		Ext.Ajax.request({
			url : 'manager/getDailyReport.action',
			params : {
				reportCode : "36",
				reportdate : timeSelect.getValue()
			},
			method : 'post',
			success : function(response) {
				var json = eval('(' + response.responseText.trim() + ')');
				var records = json;
				var html = ['<table border=1><tr ><th colspan ="9"><center><font size="3">大唐灞桥热电厂经营指标日报表</font></center></th></tr>' +
//				         '<tr ><th colspan ="9"><center><font size="3">'+timeSelect.getValue()+'</font></center></th></tr>' +
						'<tr><th>指标名称</th><th>计量单位</th><th>＃11机</th><th>＃12机</th><th>#11，12机小计</th><th>#1机</th><th>#2机</th><th>#1，2机小计</th><th>全厂合计</th></tr>'];
				for (var i = 0; i < records.length; i += 1) {
					var rc = records[i];
					html.push('<tr><td>' + rc[0] + '</td><td>'
							+ rc[1] + '</td><td>'
							+ NullToSpace(rc[2]) + '</td><td>'
							+ NullToSpace(rc[3]) + '</td><td>'
							+ NullToSpace(rc[4]) + '</td><td>'
							+ NullToSpace(rc[5]) + '</td><td>'
							+ NullToSpace(rc[6]) + '</td><td>'
							+ NullToSpace(rc[7]) + '</td><td>'
							+ NullToSpace(rc[8]) + '</td><td>'
							+ NullToSpace(rc[9]) + '</td></tr>');
				}
				html.push('</table>');
				html = html.join(''); // 最后生成的HTML表格
				tableToExcel(html);
			},
			failure : function(response) {
				Ext.Msg.alert('信息', '操作失败请联系管理员');
			}
		});
	}

	// Center中的Grid主体
	var centergrid = new Ext.grid.GridPanel({
				title : '<center>大唐灞桥热电厂经营指标日报表</center>',
				store : centerds,
				columns : [{
							header : "项目",
							width : 50,
							align : "center",
							dataIndex : 'itemName'
						}, {
							header : "计量单位",
							width : 50,
							align : "center",
							sortable : false,
							dataIndex : 'unitName'
						}, {
							header : "＃11机",
							width : 30,
							align : "center",
							sortable : true,
							dataIndex : 'ji11'
						}, {
							header : "＃12机",
							width : 30,
							align : "center",
							sortable : true,
							dataIndex : 'ji12'
						}, {
							header : "#11，12机小计",
							width : 30,
							align : "center",
							sortable : true,
							dataIndex : 'heji1112'
						}, {
							header : "#1机",
							width : 30,
							align : "center",
							sortable : true,
							dataIndex : 'ji1'
						}, {
							header : "#2机",
							width : 30,
							align : "center",
							sortable : true,
							dataIndex : 'ji2'
						}, {
							header : "#1，2机小计",
							width : 30,
							align : "center",
							sortable : true,
							dataIndex : 'heji12'
						}, {
							header : "全厂合计",
							width : 30,
							align : "center",
							sortable : true,
							dataIndex : 'heji'
						}],
				viewConfig : {
					forceFit : true
				},
				tbar : ['报表类型',reportNameBox,'-', '时间', timeSelect, {
							xtype : "tbseparator"
						}, ctbtnQuery, {
							xtype : "tbseparator"
						}, ctbtnOut],
				sm : centersm,
				frame : true,
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});

	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							title : '',
							region : "center",
							split : true,
							collapsible : false,
							titleCollapse : true,
							margins : '0',
							layoutConfig : {
								animate : true
							},
							layout : 'fit',
							items : [centergrid]
						}]
			});


})