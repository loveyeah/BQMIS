Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var selct_mod = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});

	function getlevelName(v) {
		if (v == "1") {
			v = "牵头部门";
		} else if (v == "2") {
			v = "生产面责任部门";
		} else if (v == "3") {
			v = "非生产面责任部门";
		} else if (v == "4") {
			v = "非生产面责任部门（多经公司）";
		} else {
			v = "";
		}
		return v;
	};
	var MyRecord = Ext.data.Record.create([{
				name : 'itemName'
			}, {
				name : 'unitName'
			}, {
				name : 'planValue'
			}, {
				name : 'realValue'
			}, {
				name : 'complete'
			}, {
				name : 'affiliatedDept'
			}, {
				name : 'affiliatedValue'
			}, {
				name : 'affiliatedLevel'
			}, {
				name : 'cash'
			}, {
				name : 'memo'
			}, {
				name : 'affiliatedId'
			}, {
				name : 'declarDetailId'
			}, {
				name : 'status'
			}
			// add by liuyi 091207
			, {
				name : 'topicId'
			}, {
				name : 'topicName'
			}, {
				name : 'coefficient'
			}, {
				name : 'itemCode'
			}]);
	var cm = new Ext.grid.ColumnModel([
			// selct_mod,
			new Ext.grid.RowNumberer({
						header : '行号',
						width : 35
					}), {
				header : '考核主题',
				dataIndex : 'topicName',
				width : 150,
				align : 'left'
			}, {
				header : '分配系数',
				dataIndex : 'coefficient',
				width : 150,
				align : 'left'
			}, {
				header : '指标名称',
				dataIndex : 'itemName',
				width : 150,
				align : 'left'
			}, {
				header : '计量单位',
				dataIndex : 'unitName',
				width : 100,
				align : 'left'
			}, {
				header : '计划值',
				dataIndex : 'planValue',
				width : 100,
				align : 'left'
			}, {
				header : '实际值',
				dataIndex : 'realValue',
				width : 100,
				align : 'left'
			}, {
				header : '完成率',
				dataIndex : 'complete',
				width : 100,
				align : 'left'
			}, {
				header : '挂靠级别',
				dataIndex : 'affiliatedLevel',
				width : 150,
				renderer : getlevelName,
				align : 'left'
			}, {
				header : '挂靠部门',
				dataIndex : 'affiliatedDept',
				width : 150,
				align : 'left'
			}, {
				header : '挂靠标准',
				dataIndex : 'affiliatedValue',
				width : 100,
				align : 'left'
			}, {
				header : '备注',
				dataIndex : 'memo',
				width : 200,
				align : 'left',
				editor : new Ext.form.TextField({
							allowBlank : false
						})
			}, {
				header : '兑现奖金',
				dataIndex : 'cash',
				width : 100,
				align : 'left',
				editor : new Ext.form.NumberField({
							allowDecimals : true,
							decimalPrecision : 4
						})
			}]);

	var sel_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'managexam/getAwardList.action'
						}),
				reader : new Ext.data.JsonReader({}, MyRecord)
			});
	var postType = 'real';

	function quarydata() {

		sel_ds.load({
					params : {
						datetime : Ext.getCmp("findingMonth").getValue()
					}
				});
	}
	function savedata() {
		Grid.stopEditing();
		// var changeRec = Grid.getStore().getModifiedRecords();
		// if (changeRec.length == 0)
		// Ext.Msg.alert('提示信息', "没有作任何操作!");
		// else if (changeRec.length >= 0)
		if (sel_ds.getTotalCount() == 0) {
			Ext.Msg.alert('提示', '无数据可进行保存！');
			return;
		}
		if (!confirm("确定要保存修改吗?"))
			return;
		// var newData = new Array();
		var updateData = new Array();
		for (var i = 0; i < sel_ds.getTotalCount(); i++) {

			updateData.push(sel_ds.getAt(i).data);
			// }
		}
		Ext.Ajax.request({
					url : 'managexam/saveAward.action',
					method : 'post',
					params : {
						UpdateRecords : Ext.util.JSON.encode(updateData),
						type : postType,
						datetime : findingMonth.getValue()
					},
					success : function(result, request) {
						Ext.Msg.alert('提示信息', "操作成功!");
						sel_ds.rejectChanges();
						sel_ds.reload();
					},
					failure : function(result, request) {
						Ext.Msg.alert('提示信息', '操作失败')
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
				Ext.Msg.alert('提示信息', "您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}
	// 导出
	function outdata() {
		Ext.Msg.confirm('提示', '确认要导出数据？', function(id) {
			if (id == 'yes') {
				var tableHeader = "<table border=1><tr><th colspan = 13>奖金申报填写</th></tr>";
				var html = [tableHeader];
				html
						.push("<tr><th>主题</th><th>分配系数</th><th>指标名称</th><th>计量单位</th><th>计划值</th><th>实际值</th><th>完成率</th><th>挂靠部门</th><th>挂靠级别</th><th >挂靠标准</th><th >备注</th><th> 兑现奖金</th></tr>")
				for (var i = 0; i < sel_ds.getTotalCount(); i++) {
					var rec = sel_ds.getAt(i);
					rec.set('itemName', rec.get('itemName') == null ? "" : rec
									.get('itemName'));
					rec.set('affiliatedDept', rec.get('affiliatedDept') == null
									? ""
									: rec.get('affiliatedDept'));
					rec.set('affiliatedValue',
							rec.get('affiliatedValue') == null ? "" : rec
									.get('affiliatedValue'));
					rec.set('cash', rec.get('cash') == null ? "" : rec
									.get('cash'));
					rec.set('memo', rec.get('memo') == null ? "" : rec
									.get('memo'));
					rec.set('unitName', rec.get('unitName') == null ? "" : rec
									.get('unitName'));
					rec.set('planValue', rec.get('planValue') == null
									? ""
									: rec.get('planValue'));
					rec.set('realValue', rec.get('realValue') == null
									? ""
									: rec.get('realValue'));
					rec.set('complete', rec.get('complete') == null ? "" : rec
									.get('complete'));
					// rec.set('affiliatedLevel', rec.get('affiliatedId') ==
					// null
					// ? ""
					// : getlevelName(rec.get('affiliatedId')));
					rec.set('affiliatedLevel',
							rec.get('affiliatedLevel') == null
									? ""
									: getlevelName(rec.get('affiliatedLevel')));
					html.push('<tr><td >' + rec.get('topicName') + '</td>'
							+ '<td >' + rec.get('coefficient') + '</td>'
							+ '<td >' + rec.get('itemName') + '</td>' + '<td>'
							+ rec.get('unitName') + '</td><td>'
							+ rec.get('planValue') + '</td><td>'
							+ rec.get('realValue') + '</td><td>'
							+ rec.get('complete') + '</td><td>'
							+ rec.get('affiliatedDept') + '</td><td>'
							+ rec.get('affiliatedLevel') + '</td><td>'
							+ rec.get('affiliatedValue') + '</td><td>'
							+ rec.get('memo') + '</td><td>' + rec.get('cash')
							+ '</td><td>' + '</tr>')
				}
				html.push('</table>');
				html = html.join(''); // 最后生成的HTML表格
				tableToExcel(html);
			}
		})
	}

	function getMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		return s;
	}
	var findingMonth = new Ext.form.TextField({
				style : 'cursor:pointer',
				id : 'findingMonth',
				columnWidth : 0.5,
				readOnly : true,
				anchor : "70%",
				fieldLabel : '月份',
				name : 'month',
				value : getMonth(),
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M',
									dateFmt : 'yyyy-MM',
									alwaysUseStartDate : true
								});
						this.blur();
					}
				}
			});

	var selbar = new Ext.Toolbar({
				items : ['月份', findingMonth, {
							id : 'btnQuery',
							iconCls : 'query',
							text : "查询",
							handler : quarydata
						}, '-', {
							id : 'btnSave',
							iconCls : 'save',
							text : "保存",
							handler : savedata
						}, '-', {
							id : 'btnOutput',
							iconCls : 'export',
							text : "导出",
							handler : outdata
						}, '-', {
							id : 'btnupdata',
							iconCls : 'upcommit',
							text : "上报",
							handler : reportdata
						}]

			});

	// -----------------------add by drdu 091201----------------------------
	function checkStatus() {
		var status;
		for (var i = 0; i < sel_ds.getCount(); i++) {
			status = sel_ds.getAt(i).get('status');
			if (status == '0' || status == '3') {
				Ext.get('btnupdata').dom.disabled = false
			} else if (status == '1' || status == '2' || status == null) {
				Ext.get('btnupdata').dom.disabled = true
			}
		}
	}

	function reportdata() {
		var sm = Grid.getSelectionModel();
		var selected = sm.getSelections();
		var month;
		month = Ext.getCmp("findingMonth").getValue();
		var args = new Object();
		args.busiNo = month;
		args.flowCode = "bqExamineMange";
		args.entryId = "";

		args.title = "奖金申报上报";
		var danger = window
				.showModalDialog(
						'reportSign.jsp',
						args,
						'dialogWidth=700px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');

		quarydata();
	}
	// ------------------------------------end----------------------------------------------
	cm.defaultSortable = false;
	var Grid = new Ext.grid.EditorGridPanel({
				viewConfig : {
					forceFit : false
				},
				sm : selct_mod,
				clicksToEdit : 1,
				ds : sel_ds,
				cm : cm,
				height : 425,
				split : true,
				autoScroll : true,
				layout : 'fit',
				frame : false,
				// bbar : gridbbar,
				tbar : selbar,
				border : true
			});
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "fit",
				border : false,
				frame : false,
				items : [Grid]
			});

	sel_ds.on("load", checkStatus);
});
