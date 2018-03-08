Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();

	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		t = d.getHours();
		s += (t > 9 ? "" : "0") + t + ":";
		t = d.getMinutes();
		s += (t > 9 ? "" : "0") + t + ":";
		t = d.getSeconds();
		s += (t > 9 ? "" : "0") + t;
		return s;
	}

	function getYear() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10);
		return s;
	}

	var yearFuzzy = new Ext.form.TextField({
		style : 'cursor:pointer',
		name : 'year',
		fieldLabel : '年度',
		readOnly : true,
		width : 70,
		value : getYear(),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y',
					alwaysUseStartDate : false,
					dateFmt : 'yyyy',
					isShowClear : false,
					onpicked : function(v) {
						store.reload();
						this.blur();
					}
				});
			}
		}
	});

	var advicenoteNo = new Ext.form.TextField({
		id : 'advicenoteNo',
		name : '通知单号',
		anchor : "40%"
	});

	var deptHidden = new Ext.form.Hidden({
		id : 'newDeptid'
	});
	var dept = new Ext.form.ComboBox({
		fieldLabel : '部门',
		value : "请选择...",
		mode : 'remote',
		editable : false,
		readOnly : true,
		anchor : "40%",
		onTriggerClick : function() {
			var args = {
				selectModel : 'single',
				rootNode : {
					id : "0",
					text : '灞桥热电厂'
				}
			}
			var url = "/power/comm/jsp/hr/dept/dept.jsp";
			var rvo = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:250px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (typeof(rvo) != "undefined") {
				deptHidden.setValue(rvo.ids);
				dept.setValue(rvo.names);
			}
		}
	});

	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'newEmpid',
		mapping : 0
	}, {
		name : 'empId',
		mapping : 1
	}, {
		name : 'empCode',
		mapping : 2
	}, {
		name : 'empName',
		mapping : 3
	}, {
		name : 'advicenoteNo',
		mapping : 4
	}, {
		name : 'newDeptid',
		mapping : 5
	}, {
		name : 'newDeptName',
		mapping : 6
	}, {
		name : 'checkStationGrade',
		mapping : 7
	}, {
		name : 'stationId',
		mapping : 8
	}, {
		name : 'stationName',
		mapping : 9
	}, {
		name : 'salaryPoint',
		mapping : 10
	}, {
		name : 'missionDate',
		mapping : 11
	}, {
		name : 'startsalaryDate',
		mapping : 12
	}, {
		name : 'memo',
		mapping : 13
	}, {
		name : 'checkStationGradeName',
		mapping : 14
	}]);
	var dataProxy = new Ext.data.HttpProxy({
		url : 'hr/findNewEmployeeList.action'
	});

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);
	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});

	var titleLabel = new Ext.form.FieldSet({
		anchor : '100%',
		border : false,
		region : 'north',
		height : 35,
		style : "padding-top:8px;padding-bottom:0px;border:0px;margin:0px;text-align:center;",
		items : [new Ext.form.Label({
			text : '新进员工花名册',
			style : 'font-size:22px;'
		})]
	})

	var sm = new Ext.grid.CheckboxSelectionModel({
			// singleSelect : true
	});
	var grid = new Ext.grid.GridPanel({
		region : "center",
		store : store,
		//items : [titleLabel],
		columns : [sm, new Ext.grid.RowNumberer({
			header : '行号',
			width : 35
		}), {
			header : '工号',
			dataIndex : 'empCode'
		}, {
			header : '姓名',
			dataIndex : 'empName'
		}, {
			header : '通知单号',
			width : 80,
			dataIndex : 'advicenoteNo'
			 //add by sychen 20100717
			,
			renderer:function(value){
				
				return "人新进("+yearFuzzy.getValue()+")第"+value+"号";
			}
		}, {
		}, {
			header : '岗位',
			width : 100,
			dataIndex : 'stationName'
		}, {
			header : '岗级',
			width : 100,
			dataIndex : 'checkStationGradeName'
		}, {
			header : '薪点',
			dataIndex : 'salaryPoint'
		}, {
			header : '部门',
			dataIndex : 'newDeptName'
		}, {
			header : '到厂日期',
			dataIndex : 'missionDate'
		}, {
			header : '起薪日期',
			dataIndex : 'startsalaryDate'
		}, {
			header : '备注',
			dataIndex : 'memo'
		}],
		sm : sm,
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});
	// ---------------------------------------

	var fullPanel = new Ext.Panel({
		tbar : ['年度:', yearFuzzy, '-', '部门:', dept, deptHidden, '-', "通知单号:",
				advicenoteNo, '-', {
					text : "查询",
					iconCls : 'query',
					handler : queryRecord
				}, {
					text : "导出",
					iconCls : Constants.CLS_EXPORT,
					handler : exportRecord
				}],
		layout : 'border',
		border : false,
		items : [titleLabel, grid]
	})

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [fullPanel]
	});
	// -------------------

	// 查询
	function queryRecord() {
		store.baseParams = {
			year : yearFuzzy.getValue(),
			advicenoteNo : advicenoteNo.getValue(),
			dept : deptHidden.getValue()
		}
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}

	function exportRecord() {
		 Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_007, function(buttonobj) {
            if (grid.selModel.hasSelection()) {
            	var records = grid.selModel.getSelections();
	            var recordslen = records.length;
	            if (recordslen >= 1) {
					var date = new Date();
	            	var strDate = date.format('y')+"年"+date.format('m')+"月"+date.format('d')+"日";
	            	var recs = grid.getSelectionModel().getSelections();
					var html = ['<table border=1><tr><th colspan=15>大唐陕西发电有限公司灞桥热电厂'+date.getYear()+"年"+'新进员工通知单</th>'];
					if(recordslen==1)
					{
						//add by fyyang 20100702
					html.push('<tr></tr>');
					html.push('<tr><th colspan=3 align="center">查照</th><th colspan=7 align="center">'+strDate+'</th><th colspan=5 align="center">人新进（'+ date.getYear()+'）第'+recs[0].get('advicenoteNo')+'号</th></tr>');
					}
					
					html.push('<tr><th rowspan=2>姓名</th><th rowspan=2 colspan=2>职务（岗位）</th><th rowspan=2>岗级</th><th rowspan=2>薪点</th><th rowspan=2 colspan=2>工作部门</th><th colspan=3>到厂日期</th><th colspan=3>起薪日期</th><th rowspan=2 colspan=2>备注</th></tr>');
					html.push('<tr><th>年</th><th>月</th><th>日</th><th>年</th><th>月</th><th>日</th></tr>');
					for(var i = 0; i<recs.length; i++){
						var re = recs[i];
						var dateStr = re.get('missionDate')
						var stpslryStr = re.get('startsalaryDate')
						html.push('<tr><td>'+re.get('empName')+'</td><td colspan=2>'+re.get('stationName')+'</td><td>'+re.get('checkStationGradeName')+'</td><td>'+re.get('salaryPoint')+'</td>' +
								'<td colspan=2>'+re.get('newDeptName')+'</td><td>'+dateStr.substring(0,4)+'</td>' +
								'<td>'+dateStr.substring(5,7)+'</td><td>'+dateStr.substring(8)+'</td>' +
										'<td>'+stpslryStr.substring(0,4)+'</td><td>'+stpslryStr.substring(5,7)+'</td>' +
												'<td>'+stpslryStr.substring(8)+'</td><td colspan=2>'+re.get('memo')+'</td></tr>');
					}						
					for(var j = 0; j<3; j++){
						html.push('<tr><td></td><td colspan=2></td><td></td><td ></td><td colspan=2></td><td></td><td></td><td></td><td></td><td></td>' +
											'<td></td><td colspan=2></td></tr>')
					}	
					html.push('<tr border=0><th colspan=3 align="left">厂长:</th><th colspan=5 align="left">人力资源部主任:</th><th colspan=5 align="left">制单:</th></tr>');
					html.push('</table>');
					html = html.join(''); // 最后生成的HTML表格
					tableToExcel(html);
	            } 
				}else{
					Ext.Msg.alert('提示','请选择要导出的数据！')
				}
        });
	}
	  /**
     * 将HTML转化为Excel文档
     */
    function tableToExcel(tableHTML){
		window.clipboardData.setData("Text",tableHTML);
		try{
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExApp.visible = true;
		}catch(e){
			if(e.number != -2146827859){
				Ext.Msg.alert('提示','您的电脑没有安装Microsoft Excel软件!')
			}
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}

	queryRecord();
});
