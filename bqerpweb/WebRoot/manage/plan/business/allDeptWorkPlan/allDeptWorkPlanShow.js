Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	var time = getParameter("planTime");

	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t
		return s;
	}

	/** 左边的grid * */
	var con_item = Ext.data.Record.create([{
				name : 'planTime',
				mapping : 0
			}, {
				name : 'editByName',
				mapping : 1
			}, {
				name : 'editDate',
				mapping : 2
			}, {
				name : 'depMainId',
				mapping : 3
			}, {
				name : 'depName',
				mapping : 4
			}, {
				name : 'jobId',
				mapping : 5
			}, {
				name : 'jobContent',
				mapping : 6
			}, {
				name : 'completeDate',
				mapping : 7
			}//add by sychen 20100506
			, {
				name : 'orderBy',
				mapping : 10
			}//add by mgxia 20100601
			, {
				name : 'level1DeptName',
				mapping : 11
			}]);

	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false

			});
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manageplan/getBpPlanDeptShowAllList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, con_item)
			});

	var con_item_cm = new Ext.grid.ColumnModel([con_sm,// add by sychen 20100506
				  {
		header : '部门',
		dataIndex : 'level1DeptName',
		align : 'center',
		renderer : function(value, matedata, record, rowIndex, colIndex, store) {
			if (record && rowIndex > 0) {
				if (store.getAt(rowIndex).get('depName') == store
						.getAt(rowIndex - 1).get('depName')
						|| store.getAt(rowIndex).get('depName') == '')
					return '';
			}
			return value;
		}
	}, {
		header : '编辑人员',
		hidden : true,
		dataIndex : 'editByName',
		align : 'center'
	}, {
		header : '编辑时间',
		hidden : true,
		dataIndex : 'editDate',
		align : 'center'

	}, {
				    width : 60,
					header : "序号",
					sortable : false,
					dataIndex : 'orderBy',
					editor : new Ext.form.TextField({
								id : 'orderBy',
								allowBlank : false
							})
				}, {
		header : '工作内容',
		dataIndex : 'jobContent',
		width : 250,
		align : 'left'

	}, {
		header : '完成时间',
		dataIndex : 'completeDate',
		align : 'center',
		renderer : function(v) {
			if (v == '0') {
				return '当月';
			} else if (v == '1') {
				return '跨月';
			} else if (v == '2') {
				return '长期';
			} else
				return '全年';
		}

	}]);
	con_item_cm.defaultSortable = true;

	var gridbbar = new Ext.PagingToolbar({
				pageSize : 18,
				store : con_ds,
				displayInfo : true,
				displayMsg : "显示第{0}条到{1}条，共{2}条",
				emptyMsg : "没有记录",
				beforePageText : '',
				afterPageText : ""
			});

	// 计划时间
	var planTime = new Ext.form.TextField({
		name : 'planTime',
		fieldLabel : '计划时间',
		value : time,
		readOnly : true
			// listeners : {
			// focus : function() {
			// WdatePicker({
			// startDate : '',
			// alwaysUseStartDate : true,
			// dateFmt : "yyyy-MM",
			// isShowClear : false
			// });
			// this.blur();
			// }
			// }
		});

	function queryFun() {
		con_ds.baseParams = {
			planTime : planTime.getValue()
		}
		con_ds.load({
					params : {
		// start : 0,
					// limit : 18
					}
				})
	}
	var query = new Ext.Button({
				text : "导出",
				id : 'btnreport',
				iconCls : 'approve',
				handler : approveFun

			})
			
				// 导出
	function tableToExcel(tableHTML) {
		//window.clipboardData.setData("Text", tableHTML);
		var elTable = document.getElementById("export_div");
		elTable.innerHTML = tableHTML;
		var oRangeRef = document.body.createTextRange();
		oRangeRef.moveToElementText( elTable ); 
		oRangeRef.execCommand( "Copy" );
		try {
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExWSh.Columns("A").ColumnWidth  = 7;
			ExWSh.Columns("B").ColumnWidth  = 4;
			ExWSh.Columns("H").ColumnWidth  = 13;
			ExWSh.Cells.NumberFormatLocal = "@";
			ExApp.DisplayAlerts = false;
			ExApp.visible = true;
		} catch (e) {
			if (e.number != -2146827859)
				alert("您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}
	// add by wpzhu 20100602 ----start ----------------------------------
	/*function outdata() {
		Ext.Msg.confirm('提示', '确认要导出数据？', function(id) {
			if (id == 'yes') {
				var tableHeader = "<table border=1><tr><th colspan = 4>全厂月度计划整体展示导出</th></tr>";
				var html = [tableHeader];
				html
						.push("<tr><th>部门</th><th>序号</th><th>工作内容</th><th>完成时间</th><</tr>")
				for (var i = 0; i < con_ds.getTotalCount(); i++) {
					var rec = con_ds.getAt(i);
					rec.set('level1DeptName', rec.get('level1DeptName') == null ? "" : rec
									.get('level1DeptName'));
					rec.set('orderBy', rec.get('orderBy') == null
									? ""
									: rec.get('orderBy'));
					rec.set('jobContent',
							rec.get('jobContent') == null ? "" : rec
									.get('jobContent'));
					rec.set('completeDate', rec.get('completeDate') == null ? "" : rec
									.get('completeDate'));
				
					html.push('<tr><td >' + renderDept(rec.get('level1DeptName'),con_ds) + '</td>'
							+ '<td >' + rec.get('orderBy') + '</td>'
							+ '<td  align = "left">' + rec.get('jobContent') + '</td>' + '<td>'
							+getComplete(rec.get('completeDate')) +'</td></tr>')
				}
				html.push('</table>');
				html = html.join(''); // 最后生成的HTML表格
				tableToExcel(html);
			}
		})
	}
	function  getComplete(v)
	{
		if (v == '0') {
				return '当月';
			} else if (v == '1') {
				return '跨月';
			} else if (v == '2') {
				return '长期';
			} else
				return '全年';
		
	}
	var 	rowIndex=0;
	function renderDept(v ,store)
	{
		if(rowIndex==0)
		{
			rowIndex++;
			return v;
			
		}else 	if (store.getAt(rowIndex).get('depName') == store
						.getAt(rowIndex - 1).get('depName')
						|| store.getAt(rowIndex).get('depName') == '')
						{
							rowIndex++;
					        return '';
					
						}else {
							rowIndex++;
							return v;
							
						}
	}*/
	//----------------end ---------------------------------------------------------------------
	function myExport() {
		Ext.Ajax.request({
			url : 'manageplan/getBpPlanDeptShowAllList.action?planTime=' + time,
			success : function(response) {
				var json = eval('(' + response.responseText.trim() + ')');
				var records = json.list;
				var html = ['<table border=0 style="font-size:5.55mm; FONT-FAMILY: 仿宋_GB2312">'];
				for (var i = 0; i < records.length; i += 1) {
					var rc = records[i];
					var deptName;
					if (i > 0) {
						if (records[i][11] == records[i - 1][11]) {
							deptName = "";
							// modify by ywliu 20100607 将序号和工作内容分开
							html.push('<tr><td align = "right" valign = "top" width="1" x:str>'+rc[10] + '.'+'</td><td align ="left" colspan="6" >'
							+ rc[6] + '</td>'
							+ '<td  colspan="1" align = "center" valign = "bottom"  x:str>' + rc[9]
							+ '</td>' + '</tr>')
						} else {
							deptName = records[i][11];
							
					html.push('<tr style="font-size:5.55mm; FONT-FAMILY: 方正仿宋简体" ><td style=" font-weight: bold" colspan ="8" align=center  >' + deptName
							+ '</td></tr>')
					html
							.push('<tr ><td style=" font-weight: bold" colspan="1">序号</td><th align = "left" colspan="6"></th><td style=" font-weight: bold" colspan="1" align = "left">完成时间</td></tr>')
				    //add by sychen 20100415	
					html.push('<tr><td align = "right" valign = "top" width="1" x:str>'+rc[10] + '.'+'</td><td align ="left" colspan="6">'
						+ rc[6] + '</td>'
						+ '<td  colspan="1" align = "center" valign = "bottom" >' + rc[9]
						+ '</td>' + '</tr>')
						}
					} else {
//						deptName = records[i][4];
						deptName = records[i][11];
							html.push('<tr style="font-size:5.55mm; FONT-FAMILY: 方正仿宋简体" align = "center"><td style=" font-weight: bold" colspan ="8"  align=center>' + deptName
						+ '</td></tr>')
					html
							.push('<tr><td style=" font-weight: bold" colspan="1">序号</td><th align = "left" colspan="6"></th><td  style=" font-weight: bold" colspan="1" align = "left">完成时间</td></tr>')
					html.push('<tr><td align = "right" valign = "top" width="1" x:str>'+rc[10] + '.'+'</td><td align ="left" colspan="6">'
						+ rc[6] + '</td>'
						+ '<td  colspan="1" align = "center" valign = "bottom">' + rc[9]
						+ '</td>' + '</tr>')
					}
				
				}
			   html.push('</table>');
				html = html.join(''); // 最后生成的HTML表格
				
				tableToExcel(html);
			},
			failure : function(response) {
				Ext.Msg.alert('信息', '失败');
			}
		});
	}
			
		function approveFun() {
		    myExport();
//			outdata();
	}
	var contbar = new Ext.Toolbar({
				items : [planTime, query]
			});
	var Grid = new Ext.grid.GridPanel({
				sm : con_sm,
				ds : con_ds,
				cm : con_item_cm,
				height : 425,
				autoScroll : true,
				// bbar : gridbbar,
				tbar : contbar,
				border : true
			});

	new Ext.Viewport({
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

	queryFun();
})
