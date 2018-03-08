Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	
    var arg = window.dialogArguments

	var writeDate = arg.writeDate;
	var specialty = arg.specialty;
	var defectType = arg.defectType;

    var MyRecord = Ext.data.Record.create([{
			name : 'failureContent',
			mapping : 0
		}, {
			name : 'repairDep',
			mapping : 1
		}, {
			name : 'repairDeptName',
			mapping : 2
		}, {
			name : 'dominationProfession',
			mapping : 3
		}, {
			name : 'dominationProfessionName',
			mapping : 4
		}, {
			name : 'woStatusText',
			mapping : 5
		}, {
			name : 'writeDate',
			mapping : 6
		}, {
			name : 'delayDate',
			mapping : 7
		}, {
			name : 'findBy',
			mapping : 8
		}, {
			name : 'findByName',
			mapping : 9
		}, {
			name : 'findDept',
			mapping : 10
		}, {
			name : 'findDeptName',
			mapping : 11
		}, {
			name : 'failureCode',
			mapping : 12
		}, {
			name : 'belongSystem',
			mapping : 13
		},{
			name : 'belongSystemText',
			mapping : 14
		},{
			name : 'findDate',
			mapping : 15
		},{
			name : 'failuretypeName',
			mapping : 16
		},{
			name : 'failurePri',
			mapping : 17
		},{
			name : 'endtTime',
			mapping : 18
		},{
			name : 'reportTime',
			mapping : 19
		},{
			name : 'awaitTime',
			mapping : 20
		},{
			name : 'failuretypeCode',
			mapping : 21
		}]);

	var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'bqfailure/getEquDetailList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'totalCount',
							root : 'list'
						}, MyRecord)
			});
	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
			
	
	function query() {
		store.baseParams = {
			writeDate : writeDate,
			specialty : specialty,
			defectType:defectType
		}
		store.load({
			start : 0,
			limit:18
		});
	}
	
	
	var tbar = new Ext.Toolbar({
		items : [{
			id : 'btnPrint',
			text : '导 出',
			iconCls:'export',
			handler : function() {
				reportexport();
			}
		}]
	});
	
	
	var grid = new Ext.grid.EditorGridPanel({
		store : store,
		layout : 'fit',
		columns : [sm, new Ext.grid.RowNumberer({
			header : '序号',
			width : 35
		}), {
			header : '缺陷内容',
			dataIndex : 'failureContent',
			width : 300,
			renderer : function change(val) {
				return ' <span style="white-space:normal;">' + val + ' </span>';
			}
		}, {
			header : '检修部门',
			dataIndex : 'repairDeptName',
			width : 120
		}, {
			header : '管辖专业',
			dataIndex : 'dominationProfessionName',
			width : 100
		}, {
			header : '状态',
			dataIndex : 'woStatusText',
			width : 120
		}, {
			header : '填写时间',
			dataIndex : 'writeDate',
			width : 120
		}, {
			header : '批准延期时间',
			dataIndex : 'delayDate',
			width : 120
		}, {
			header : '发现人',
			dataIndex : 'findByName',
			width : 60
		}, {
			header : '发现部门',
			dataIndex : 'findDeptName',
			width : 120
		}, {
			header : '缺陷编码',
			dataIndex : 'failureCode',
			width : 100
		}, {
			header : '所属系统',
			dataIndex : 'belongSystemText',
			width : 100
		}, {
			header : '发现时间',
			dataIndex : 'findDate',
			width : 120
		}, {
			header : '类别',
			dataIndex : 'failuretypeName',
			width : 60
		}, {
			header : '优先级',
			dataIndex : 'failurePri',
			width : 140
		}],
		tbar : tbar,
		sm : sm,
		bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : store,
							displayInfo : true,
							displayMsg : "显示第{0}条到{1}条，共{2}条",
							emptyMsg : "没有记录",
							beforePageText : '',
							afterPageText : ""
						})
	});
	
	function tableToExcel(tableHTML) {
		window.clipboardData.setData("Text", tableHTML);
		try {
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExApp.Columns(1).ColumnWidth = 25;
			ExApp.Columns(2).ColumnWidth = 15;
			ExApp.Columns(3).ColumnWidth = 20;
			ExApp.Columns(4).ColumnWidth = 15;
			ExApp.Columns(5).ColumnWidth = 15;
			ExApp.Columns(6).ColumnWidth = 15;
			ExApp.Columns(8).ColumnWidth = 15;
			ExApp.Columns(9).ColumnWidth = 15;
			ExApp.Columns(11).ColumnWidth = 15;
			ExApp.Columns(13).ColumnWidth = 20;
			ExApp.DisplayAlerts = true;
			ExWSh.Paste();
			ExApp.visible = true;
		} catch (e) {
			if (e.number != -2146827859)
				alert("您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}

	function reportexport() {
		if(store.getCount() == 0){
			Ext.Msg.alert('提示','无数据进行导出！');
			return;
		}
        var defectTypeText="";
         if(defectType=='A'){
         	defectTypeText="已消除缺陷数";
         }
         else if(defectType=='B'){
         	defectTypeText="未消除缺陷数";
         }
         else if(defectType=='C'){
         	defectTypeText="退回缺陷数";
         }
         else if(defectType=='D'){
         	defectTypeText="超时缺陷数";
         }
         
		var html = ['<table border=1><tr><td align="center" colspan="13"><b><h1>缺陷统计月报表'
				+ defectTypeText
				+ '明细('
				+ writeDate
				+ ')</h1></b></td></tr><tr><th>缺陷内容</th><th>检修部门</th><th>管辖专业</th>' +
						'<th>状态</th><th>填写时间</th><th>批准延期时间</th><th>发现人</th>' +
						'<th>发现部门</th><th>缺陷编码</th><th>所属系统</th><th>发现时间</th>' +
						'<th>类别</th><th>优先级</th></tr>'];
		for (var i = 0; i < store.getCount(); i += 1) {
			var rc = store.getAt(i).data;
			html.push('<tr><td>' + (rc.failureContent==null?"":rc.failureContent) + '</td>' +
					'<td>'+ (rc.repairDeptName==null?"":rc.repairDeptName) + '</td>' +
					'<td>' + (rc.dominationProfessionName==null?"":rc.dominationProfessionName)+ '</td>' +
					'<td>' + (rc.woStatusText==null?"":rc.woStatusText) + '</td>' +
					'<td>' + (rc.writeDate==null?"":rc.writeDate)+ '</td>' +
					'<td>' + (rc.delayDate==null?"":rc.delayDate) + '</td>' +
					'<td>' + (rc.findByName==null?"":rc.findByName) + '</td>' +
					'<td>' + (rc.findDeptName==null?"":rc.findDeptName) + '</td>' +
					'<td>' + (rc.failureCode==null?"":rc.failureCode)+ '</td>' +
					'<td>' + (rc.belongSystemText==null?"":rc.belongSystemText) + '</td>' +
					'<td>' + (rc.findDate==null?"":rc.findDate) + '</td>' +
					'<td>' + (rc.failuretypeName==null?"":rc.failuretypeName)+ '</td>' +
					'<td>' + (rc.failurePri==null?"":rc.failurePri) + '</td></tr>');
		}
		html.push('</table>');
		html = html.join('');
		tableToExcel(html);

	}
	
	new Ext.Viewport({
				layout : 'border',
				auotHeight : true,
				items : [{
							region : 'center',
							layout : 'fit',
							items : [grid]
						}]

			});		
	query();			
})

