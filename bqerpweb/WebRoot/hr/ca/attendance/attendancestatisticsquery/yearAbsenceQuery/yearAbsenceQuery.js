Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	
	function getYear() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) ;
		
		return s;
	}

	var dept = new Ext.ux.ComboBoxTree({
		fieldLabel : '部门',
		allowBlank : true,
		width : 200,
		id : "attendanceDeptId",
		anchor : '90%',
		displayField : 'text',
		valueField : 'id',
		hiddenName : 'attendanceDeptName',
		blankText : '请选择',
		emptyText : '请选择',
		tree : {
			xtype : 'treepanel',
			rootVisible : false,

			loader : new Ext.tree.TreeLoader({
				dataUrl : 'ca/getAttendanceDeptData.action'
			}),
			root : new Ext.tree.AsyncTreeNode({
				id : '0',
				name : '灞桥热电厂',
				text : '灞桥热电厂'
			}),
			listeners : { 
				click : function(node, e) {
					txtDeptHid.setValue(node.attributes.id);
					txtDeptName.setValue(node.attributes.text);
				}
			}
		},
		selectNodeModel : 'all'
	});
	
	var txtDeptHid = new Ext.form.Hidden();
	var txtDeptName = new Ext.form.TextField();
	
	var con_item = Ext.data.Record.create([{
				name : 'attendanceDeptId',
				mapping : 0
			}, {
				name : 'empId',
				mapping : 1
			}, {
				name : 'deptName',
				mapping : 2
			}, {
				name : 'teamName',
				mapping : 3
			}, {
				name : 'chsName',
				mapping : 4
			}, {
				name : 'month1',
				mapping : 5
			}, {
				name : 'month2',
				mapping : 6
			}, {
				name : 'month3',
				mapping : 7
			}, {
				name : 'month4',
				mapping : 8
			}, {
				name : 'month5',
				mapping : 9
			}, {
				name : 'month6',
				mapping : 10
			}, {
				name : 'month7',
				mapping : 11
			}, {
				name : 'month8',
				mapping : 12
			},  {
				name : 'month9',
				mapping : 13
			},{
				name : 'month10',
				mapping : 14
			},{
				name : 'month11',
				mapping : 15
			},{
				name : 'month12',
				mapping : 16
			}]);

	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'hr/getAattendanceListByYear.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, con_item)
			});
	
	
			
	var con_item_cm = new Ext.grid.ColumnModel([sm,
			{
				header : "部门",
				align : 'left',
				dataIndex : 'deptName'
			}, {
				header : '班组',
				dataIndex : 'teamName',
				align : 'center'

			}, {
				header : '姓名',
				dataIndex : 'chsName',
				align : 'center'

			}, {
				header : '1月',
				dataIndex : 'month1',
				align : 'center'

			}, {
				header : '2月',
				dataIndex : 'month2',
				align : 'center'

			}, {
				header : '3月',
				dataIndex : 'month3',
				align : 'center'

			}, {
				header : '4月',
				dataIndex : 'month4',
				align : 'center'

			}, {
				header : '5月',
				dataIndex : 'month5',
				align : 'center'

			}, {
				header : '6月',
				dataIndex : 'month6',
				align : 'center'

			}, {
				header : '7月',
				dataIndex : 'month7',
				align : 'center'
			}, {
				header : '8月',
				dataIndex : 'month8',
				align : 'center'

			}, {
				header : '9月',
				dataIndex : 'month9',
				align : 'center'

			}, {
				header : '10月',
				dataIndex : 'month10',
				align : 'center'
			}, {
				header : '11月',
				dataIndex : 'month11',
				align : 'center'
			}, {
				header : '12月',
				dataIndex : 'month12',
				align : 'center'
			}]);
	con_item_cm.defaultSortable = true;
		
	//年份
	var years = new Ext.form.TextField({
				name : 'years',
				fieldLabel : '年份',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '',
									alwaysUseStartDate : true,
									dateFmt : "yyyy",
									isShowClear : false
								});
						this.blur();
					}
				}

			});
	years.setValue(getYear());

	function query() {
		store.baseParams = {
			strYear : years.getValue(),
			deptId: txtDeptHid.getValue()
		}
			store.load({
						params : {
							start : 0,
							limit : 18
						}
		})
		
	}

var tbar = new Ext.Toolbar({
				items : ["年份:", years, '-',"部门:", dept, '-', {
							text : '查询',
							iconCls : 'query',
							handler : query
						},{
							text : "导出",
							id : 'btnreport',
							iconCls : 'export',
							handler : reportexport
						}]
			});
	// 导出
	function tableToExcel(tableHTML) {
		window.clipboardData.setData("Text", tableHTML);
		try {
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExApp.Columns(1).ColumnWidth = 15;
			ExApp.Columns(2).ColumnWidth = 15;
			ExApp.Columns(3).ColumnWidth = 15;
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
		var deptName="";
		if(txtDeptName.getValue()==""||txtDeptName.getValue()==null
		   ||txtDeptName.getValue()==undefined)
		 deptName="所有部门";
		else
		  deptName=txtDeptName.getValue();
		Ext.Ajax.request({
			url : 'hr/getAattendanceListByYear.action?strYear=' + years.getValue()+'&deptId='+txtDeptHid.getValue(),
			success : function(response) {
				var json = eval('(' + response.responseText.trim() + ')');
				var records = json.list;
				
				var title = "";
					title = "年度缺勤汇总统计";
				var tableHeader = "<table border=1 ><tr><th align=center colspan = 15 style=font-size:5.55mm; FONT-FAMILY: 仿宋_GB2312>"+title+"</th></tr>";
				var html = [tableHeader];
				

				html
						.push("<tr><th align=right colspan = 3>年份：</th><td  align=left colspan = 4>"+years.getValue()+"年</td><th align=right colspan = 4>部门：</th><td align=left colspan = 4>"+deptName+"</td></tr>")
			
				html
						.push("<tr><th>部门</th><th>班组</th><th>姓名</th><th>1月</th><th>2月</th><th>3月</th><th>4月</th><th>5月" +
								"</th><th>6月</th><th>7月</th><th>8月</th><th>9月</th><th>10月</th><th>11月</th><th>12月</th></tr>")
				
				for (var i = 0; i < records.length; i += 1) {
					
					var rc = records[i];
					html.push('<tr><td align ="left">' + (rc[2] ==null?"":rc[2])+ '</td><td align ="left">' + (rc[3]==null?"":rc[3] )
					       + '</td><td align ="left">' + (rc[4] ==null?"":rc[4]) + '</td><td align ="left">' + (rc[5]==null?"":rc[5] )
					       + '</td><td align ="left">' + (rc[6] ==null?"":rc[6] ) + '</td><td align ="left">' + (rc[7]==null?"":rc[7])
					       + '</td><td align ="left">' + (rc[8] ==null?"":rc[8]) + '</td><td align ="left">' + (rc[9]==null?"":rc[9] )
					       + '</td><td align ="left">' + (rc[10] ==null?"":rc[10] ) + '</td><td align ="left">' + (rc[11]==null?"":rc[11])
					       + '</td><td align ="left">' + (rc[12] ==null?"":rc[12]) + '</td><td align ="left">' + (rc[13]==null?"":rc[13])
					       + '</td><td align ="left">' + (rc[14] ==null?"":rc[14]) + '</td><td align ="left">' + (rc[15]==null?"":rc[15])
					       + '</td><td align ="left">' + (rc[16] ==null?"":rc[16]) + '</td></tr>');
				}
				html.push('</table>');
				html = html.join('');
				tableToExcel(html);
			},
			failure : function(response) {
				Ext.Msg.alert('提示', '导出失败！');
			}
		});
	}
	var grid = new Ext.grid.EditorGridPanel({
				sm : sm,
				ds : store,
				cm : con_item_cm,
				tbar : tbar,
				border : false,
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
							items : [grid]
						}]
			});
			
			query();

})
