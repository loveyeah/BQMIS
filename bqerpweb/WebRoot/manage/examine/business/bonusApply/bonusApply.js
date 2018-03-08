Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	var itemId = '';
	
	var con_item = Ext.data.Record.create([{
				name : 'executionid1'
			}, {
				name : 'executionid2'
			}, {
				name : 'executionid3'
			}, {
				name : 'itemcode'
			}, {
				name : 'itemid'
			}, {
				name : 'itemname'
			}, {
				name : 'unitname'
			}, {
				name : 'planValue1'
			}, {
				name : 'realValue1'
			}, {
				name : 'flawValue1'
			}, {
				name : 'planValue2'
			}, {
				name : 'realValue2'
			}, {
				name : 'flawValue2'
			}, {
				name : 'planValue3'
			}, {
				name : 'realValue3'
			}, {
				name : 'flawValue3'
			}]);
			
	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'managexam/getBonusApplyList.action' // 考核主题
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, con_item)
			});
	var con_item_cm = new Ext.grid.ColumnModel({
		columns : [con_sm,
				// {
				// header : '编码',
				// width : 40,
				// dataIndex : 'topicId',
				// align : 'center'
				// },
				{
			header : '指标名称',
			dataIndex : 'itemname'
				// ,
				// editor : new Ext.form.TextField()
		}, {
			header : '计量单位',
			dataIndex : 'unitname'
		}, {
			header : '计划值',
			dataIndex : 'planValue1'
		}, {
			header : '实际值',
			dataIndex : 'realValue1'
		}, {
			header : '完成情况',
			dataIndex : 'flawValue1'
		}, {
			header : '挂靠部门',
			dataIndex : 'planValue2'
		}, {
			header : '挂靠级别',
			dataIndex : 'realValue2'
		}, {
			header : '挂靠标准',
			dataIndex : 'flawValue2'
		}, {
			header : '兑现奖金',
			dataIndex : 'planValue3'
		}, {
			header : '备注',
			dataIndex : 'realValue3'
		}]
	});
	
	function getMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		return s;
	}
	
	var meetingMonth = new Ext.form.TextField({
				style : 'cursor:pointer',
				// id : 'month',
				columnWidth : 0.5,
				readOnly : true,
				anchor : "70%",
				fieldLabel : '年份',
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
	
	
	// 载入数据
	function getTopic() {
		con_ds.load({
					params : {
						datetime : meetingMonth.getValue()
					}
				});
	}
	
	// 保存
	function saveTopic() {
		Grid.stopEditing();
		var modifyRec = Grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || topicIds.length > 0) {
			if (!confirm("确定要保存修改吗?"))
				return;
			// var newData = new Array();
			var updateData = new Array();
			for (var i = 0; i < modifyRec.length; i++) {
				if (modifyRec[i].get("topicName") == "") {
					Ext.MessageBox.alert('提示信息', '报表名称不能为空！')
					return
				}
				updateData.push(modifyRec[i].data);
				// }
			}
			Ext.Ajax.request({
						url : 'managexam/saveExecutionTable.action',
						method : 'post',
						params : {
							addOrUpdateRecords : Ext.util.JSON
									.encode(updateData),
							type : postType,
							datetime : meetingMonth.getValue()
						},
						success : function(result, request) {
							var o = eval('(' + result.responseText + ')');
							Ext.MessageBox.alert('提示信息', o.msg);
							con_ds.rejectChanges();
							topicIds = [];
							con_ds.reload();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('提示信息', '未知错误！')
						}
					})
		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
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
	function exportRecord() {		
		Ext.Msg.confirm('提示', '确认要导出数据？', function(id) {
			if (id == 'yes') {
				var tableHeader = "<table border=1><tr><th colspan = 9>年度计划执行情况查询</th></tr>";
				var html = [tableHeader];
				html.push("<tr><th rowspan = 2 colspan = 2>考核指标</th><th rowspan = 2>单位</th><th colspan = 3>#11、12机</th><th colspan = 3>#1、2机</th><th colspan = 3>全厂合并</th></tr>")
				html.push("<tr><th>年度计划</th><th>实际值</th><th>差值</th><th>年度计划</th><th>实际值</th><th>差值</th><th>年度计划</th><th>实际值</th><th>差值</th></tr>")
				for (var i = 0; i < con_ds.getTotalCount(); i++) {
					var rec = con_ds.getAt(i);
					rec.set('itemname',rec.get('itemname')== null ? "" : rec.get('itemname')) ;
					rec.set('unitname',rec.get('unitname')== null ? "" : rec.get('unitname')) ;
					rec.set('planValue1',rec.get('planValue1')== null ? "" : rec.get('planValue1')) ;
					rec.set('realValue1',rec.get('realValue1')== null ? "" : rec.get('realValue1')) ;
					rec.set('flawValue1',rec.get('flawValue1')== null ? "" : rec.get('flawValue1')) ;
					rec.set('planValue2',rec.get('planValue2')== null ? "" : rec.get('planValue2')) ;
					rec.set('realValue2',rec.get('realValue2')== null ? "" : rec.get('realValue2')) ;
					rec.set('flawValue2',rec.get('flawValue2')== null ? "" : rec.get('flawValue2')) ;
					rec.set('planValue3',rec.get('planValue3')== null ? "" : rec.get('planValue3')) ;
					rec.set('realValue3',rec.get('realValue3')== null ? "" : rec.get('realValue3')) ;
					rec.set('flawValue3',rec.get('flawValue3')== null ? "" : rec.get('flawValue3')) ;
					html.push('<tr><td colspan = 2>' + rec.get('itemname') + '</td><td>' 							
							+ rec.get('unitname') + '</td><td>'
							+ rec.get('planValue1') + '</td><td>'
							+ rec.get('realValue1') + '</td><td>'
							+ rec.get('flawValue1') + '</td><td>'
							+ rec.get('planValue2') + '</td><td>'
							+ rec.get('realValue2') + '</td><td>'
							+ rec.get('flawValue2') + '</td><td>'
							+ rec.get('planValue3') + '</td><td>'
							+ rec.get('realValue3') + '</td><td>'
							+ rec.get('flawValue3') + '</td>'
							+ '</tr>')
				}
				html.push('</table>');
				html = html.join(''); // 最后生成的HTML表格
				tableToExcel(html);
			}
		})
	}
	
	function reportRecord() {
		alert("上报");
	}
	
	var contbar = new Ext.Toolbar({
				items : ['月份',meetingMonth,{
							id : 'btnQuery',
							iconCls : 'query',
							text : "载入",
							handler : getTopic
						}, '-', {
							id : 'btnSave',
							iconCls : 'save',
							text : "保存",
							handler : saveTopic
						}, '-', {
							id : 'btnExport',
							iconCls : 'export',
							text : "导出",
							handler : exportRecord
						}, '-', {
							id : 'btnReport',
							iconCls : Constants.CLS_REPOET,
							text : "上报",
							handler : reportRecord
						}]

			});
	
	var Grid = new Ext.grid.EditorGridPanel({
				viewConfig : {
					forceFit : true
				},
				sm : con_sm,
				ds : con_ds,
				cm : con_item_cm,
				height : 425,
				split : true,
				autoScroll : true,
				layout : 'fit',
				frame : false,
				// bbar : gridbbar,
				tbar : contbar,
				border : true
			});		

	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							region : "center",
							layout : 'fit',
							collapsible : true,
							split : true,
							margins : '0 0 0 0',
							items : [Grid]

						}]
			})
});
