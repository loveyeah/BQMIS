Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

			Ext.form.Label.prototype.setText = function(argText) {
				this.el.dom.innerHTML = argText;
			}

			var quarterDate = new Ext.form.TextField({
						id : 'quarterDate',
						name : '_quarterDate',
						fieldLabel : "年份",
						style : 'cursor:pointer',
						cls : 'Wdate',
						width : 90,
						value : new Date().getFullYear(),
						listeners : {
							focus : function() {
								WdatePicker({
											startDate : '%y',
											dateFmt : 'yyyy',
											alwaysUseStartDate : true,
											isShowClear : false
										});
							}
						}
					});

			var quarterBox = new Ext.form.ComboBox({
						fieldLabel : '季度',
						store : [['1', '春查'], ['2', '秋查'], ['3', '安评'],
								['4', '重大危险源'], ['5', '技术监控']],
						id : 'quarterBox',
						name : 'quarterBoxName',
						valueField : "value",
						displayField : "text",
						mode : 'local',
						typeAhead : true,
						forceSelection : true,
						hiddenName : 'quarterBoxName',
						editable : false,
						triggerAction : 'all',
						width : 85,
						selectOnFocus : true,
						value : 1
					});

			var cbFinish = new Ext.form.ComboBox({
						fieldLabel : '是否完成',
						store : [['', '所有'], ['Y', '已完成'], ['N', '未完成']],
						id : 'cbFinish',
						name : 'cbFinishName',
						valueField : "value",
						displayField : "text",
						mode : 'local',
						typeAhead : true,
						forceSelection : true,
						hiddenName : 'cbFinishName',
						editable : false,
						triggerAction : 'all',
						width : 80,
						selectOnFocus : true,
						value : ''
					});

			var txtFinish = new Ext.form.Label({
						id : "txtFinish",
						fieldLabel : '',
						width : 200,
						name : 'txtFinish'
					});

			// 定义grid
			var MyRecord = Ext.data.Record.create([{
						name : 'mainId',
						mapping : 0
					}, {
						name : 'year',
						mapping : 1
					}, {
						name : 'season',
						mapping : 2
					}, {
						name : 'detailId',
						mapping : 3
					}, {
						name : 'existQuestion',
						mapping : 4
					}, {
						name : 'wholeStep',
						mapping : 5
					}, {
						name : 'avoidStep',
						mapping : 6
					}, {
						name : 'planDate',
						mapping : 7
					}, {
						name : 'actualDate',
						mapping : 8
					}, {
						name : 'dutyDeptCode',
						mapping : 9
					}, {
						name : 'dutyDeptName',
						mapping : 10
					}, {
						name : 'dutyBy',
						mapping : 11
					}, {
						name : 'dutyName',
						mapping : 12
					}, {
						name : 'superDeptCode',
						mapping : 13
					}, {
						name : 'superDeptName',
						mapping : 14
					}, {
						name : 'superBy',
						mapping : 15
					}, {
						name : 'superName',
						mapping : 16
					}, {
						name : 'noReason',
						mapping : 17
					}, {
						name : 'issueProerty',
						mapping : 18
					}, {
						name : 'noFinishNum',
						mapping : 19
					}, {
						name : 'finishNum',
						mapping : 20
					}, {
						name : 'totalNum',
						mapping : 21
					}, {
						name : 'percent',
						mapping : 22
					}]);

			var dataProxy = new Ext.data.HttpProxy(

			{
						url : 'security/findDynamicQueryList.action'
					}

			);

			var theReader = new Ext.data.JsonReader({
						root : "list",
						totalProperty : "totalCount"

					}, MyRecord);

			var store = new Ext.data.Store({

						proxy : dataProxy,

						reader : theReader

					});

			store.on("load", function() {
						if (store.getTotalCount() > 0) {
							var obj = store.getAt(0);
							var per = parseFloat(obj.get("percent")) * 100;
							per = per.toFixed(2);
							txtFinish.setText("<font color ='red'>本次检查共存在问题"
									+ obj.get("totalNum") + "个，已完成"
									+ obj.get("finishNum") + "个，完成率为" + per
									+ "%</font>");
						} else {
							txtFinish.setText("");
						}
					});

			var sm = new Ext.grid.CheckboxSelectionModel();

			var grid = new Ext.grid.GridPanel({
						region : "center",
						layout : 'fit',
						store : store,

						columns : [sm, new Ext.grid.RowNumberer({
											header : '序号',
											width : 50
										}), {

									header : "ID",
									width : 75,
									sortable : true,
									dataIndex : 'detailId',
									hidden : true
								}, {
									header : "ID",
									width : 75,
									sortable : true,
									hidden : true,
									dataIndex : 'mainId'

								}, {
									header : "存在问题",
									width : 250,
									sortable : true,
									dataIndex : 'existQuestion'
								},

								{
									header : "整改计划",
									width : 250,
									sortable : true,
									dataIndex : 'wholeStep'
								}, {
									header : "计划完成时间",
									width : 110,
									sortable : true,
									dataIndex : 'planDate'
								}, {
									header : "整改责任人",
									width : 100,
									sortable : true,
									dataIndex : 'dutyName'
								}, {
									header : "整改责任部门",
									width : 100,
									sortable : true,
									dataIndex : 'dutyDeptName'
								}, {
									header : "整改监督人",
									width : 100,
									sortable : true,
									dataIndex : 'superName'
								},  {
									header : "整改监督部门",
									width : 100,
									sortable : true,
									dataIndex : 'superDeptName'
								}, {
									header : "问题性质",
									width : 75,
									sortable : true,
									dataIndex : 'issueProerty',
									renderer : function(v) {
										if (v == '1') {
											return "一般性质";
										} else if (v == '2') {
											return "重大性质";
										}// update by ltong
										else {
											return v;
										}
									}

								},  {
									header : "整改前防范措施",
									width : 75,
									sortable : true,
									dataIndex : 'avoidStep'
								}, {
									header : "未整改原因",
									width : 75,
									sortable : true,
									dataIndex : 'noReason'
								}, {
									header : "整改完成时间",
									width : 75,
									sortable : true,
									dataIndex : 'actualDate'
								} ],
						sm : sm,
						autoSizeColumns : true,
						viewConfig : {
							forceFit : true
						},
						tbar : ['年度：', quarterDate, "-", '检查类别：', quarterBox,
								'-', '完成情况：', cbFinish, '-', {
									text : "查询",
									iconCls : 'query',
									handler : queryRecord
								}, '-',{
									text : "导出",
									iconCls : 'export',
									handler : outdata
								},'-', txtFinish],
						// 分页
						bbar : new Ext.PagingToolbar({
									pageSize : 18,
									store : store,
									displayInfo : true,
									displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
									emptyMsg : "没有记录"
								})
					});
			grid.on("rowdblclick", updateRecord);

			// ---------------------------------------

			new Ext.Viewport({
						enableTabScroll : true,
						layout : "border",
						// layout : "fit",
						items : [grid]
					});
	// 导出 add by wpzhu 20100612------------
	var connObj = new Ext.data.Connection({
				timeout : 180000,
			   url : 'security/findDynamicQueryList.action',
				method : 'POST'
			});

	var  Export = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy(connObj),
				reader : new Ext.data.JsonReader({
							root : 'list',
							totalProperty : 'totalCount'
						}, MyRecord)
			});

	function outdata() {
		Ext.Msg.wait('正在导出数据，请稍候……')
		if (Export.getCount() == 0) {
			Export.baseParams = {
					year : quarterDate.getValue(),
					season : quarterBox.getValue(),
					isFinish : cbFinish.getValue()
				};
			Export.load();
		} else {
			Export.reload();
		}
		Export.on('load', exportFun);
	}

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
	function exportFun() {
		if (Export.getCount() == 0) {
			Ext.Msg.alert('提示', '无数据进行导出！');
			return;
		}
		Ext.Msg.confirm('提示', '确认要导出数据？', function(id) {
			if (id == 'yes') {
				var tableHeader = "<table border=1><tr><th colspan = 11>整改完成情况统计</th></tr>";
				var html = [tableHeader];
				html
						.push("<tr><th>存在问题</th><th>整改计划 </th><th>计划完成时间</th><th>整改责任人</th><th>整改责任部门</th><th>整改监督人</th><th>整改监督部门 </th><th>问题性质</th><th>整改前防范措施</th><th >未整改原因</th><th >整改完成时间</th></tr>")
					for (var i = 0; i < Export.getTotalCount(); i++) {
					var rec = Export.getAt(i);
					rec.set('existQuestion', rec.get('existQuestion') == null ? "" : rec
									.get('existQuestion'));
					rec.set('wholeStep', rec.get('wholeStep') == null
									? ""
									: rec.get('wholeStep'));
					rec.set('planDate',
							rec.get('planDate') == null ? "" : rec
									.get('planDate'));
					rec.set('dutyName', rec.get('dutyName') == null ? "" : rec
									.get('dutyName'));
					rec.set('dutyDeptName', rec.get('dutyDeptName') == null ? "" : rec
									.get('dutyDeptName'));
					rec.set('superName', rec.get('superName') == null ? "" : rec
									.get('superName'));
					rec.set('superDeptName', rec.get('superDeptName') == null
									? ""
									: rec.get('superDeptName'));
					rec.set('issueProerty', rec.get('issueProerty') == null
									? ""
									: rec.get('issueProerty'));
					rec.set('avoidStep', (rec.get('avoidStep') == null||rec.get('avoidStep')=="null") ? "" : rec
									.get('avoidStep'));
					rec.set('noReason',
							rec.get('noReason') == null? "": (rec.get('noReason')));
					rec.set('actualDate',
							rec.get('actualDate') == null? "": rec.get('actualDate'));
					html.push('<tr><td align=left >' + rec.get('existQuestion') + '</td><td align=left>'
							+ rec.get('wholeStep') + '</td><td align=left >'
							+ rec.get('planDate') + '</td><td align=left>'
							+ rec.get('dutyName') + '</td><td align=left>'
							+ rec.get('dutyDeptName') + '</td><td align=left>'
							+ rec.get('superName') + '</td><td align=left>'
							+ rec.get('superDeptName') + '</td><td align=left>'
							+ getIssueName(rec.get('issueProerty')) + '</td><td align=left>'
							+ rec.get('avoidStep') + '</td><td align=left>' 
							+ rec.get('noReason') + '</td><td align=left>' 
							+ rec.get('actualDate')+ '</td></tr>')
				}
				html.push('</table>');
				html = html.join(''); // 最后生成的HTML表格
				tableToExcel(html);
			}
		})
	}

    function  getIssueName(v)
    {
    	if (v == '1') {
		return "一般性质";	
		}
		else if (v == '2') {
		return "重大性质";
					}
			else {
					return v;
				}
    
    }
	
			// 查询
			function queryRecord() {

				store.baseParams = {
					year : quarterDate.getValue(),
					season : quarterBox.getValue(),
					isFinish : cbFinish.getValue()
				};
				store.load({
							params : {
								start : 0,
								limit : 18
							}
						});
			}
			// -------------------add by ltong----详细信息--------------------
			var wd = 240;

			var existQuestion = new Ext.form.TextArea({
						id : "existQuestion",
						fieldLabel : '存在问题',
						anchor : '94.5%',
						heigth : 80,
						maxLength : 100,
						readOnly : true,
						name : 'detailModel.existQuestion'
					});

			var wholeStep = new Ext.form.TextArea({
						id : "wholeStep",
						fieldLabel : '整改措施',
						anchor : '94.5%',
						heigth : 80,
						maxLength : 100,
						readOnly : true,
						name : 'detailModel.wholeStep'
					});

			var avoidStep = new Ext.form.TextArea({
						id : "avoidStep",
						fieldLabel : '整改前防范措施',
						anchor : '94.5%',
						heigth : 80,
						maxLength : 100,
						readOnly : true,
						name : 'detailModel.avoidStep'
					});

			var planDate = new Ext.form.TextField({
						id : 'planDate',
						name : 'detailModel.planDate',
						fieldLabel : '计划完成时间',
						anchor : '90%',
						altFormats : 'Y-m-d',
						format : 'Y-m-d',
						readOnly : true
					});

			// 整改责任人
			var dutyName = new Ext.form.TextField({
						isFormField : true,
						anchor : '90%',
						name : 'dutyName',
						fieldLabel : "整改责任人",
						readOnly : true
					});

			// 整改责任部门
			var dutyDeptName = new Ext.form.TextField({
						name : 'dutyDeptName',
						anchor : '90%',
						fieldLabel : "整改责任部门",
						readOnly : true
					});

			// 整改监督人
			var superName = new Ext.form.TextField({
						isFormField : true,
						anchor : '90%',
						name : 'superName',
						fieldLabel : "整改监督人",
						readOnly : true
					});

			// 整改监督部门
			var superDeptName = new Ext.form.TextField({
						name : 'superDeptName',
						anchor : '90%',
						fieldLabel : "整改监督部门",
						readOnly : true
					});

			var issueProerty = new Ext.form.ComboBox({
						id : "issueProerty",
						fieldLabel : '问题性质',
						anchor : '90%',
						readOnly : true,
						name : 'detailModel.issueProerty',
						store : [['1', '一般性质'], ['2', '重大性质']],
						disabled : true
					});

			var myaddpanel = new Ext.FormPanel({
						frame : true,
						labelAlign : 'center',
						labelWidth : 100,
						closeAction : 'hide',
						layout : "form",
						title : '问题详细查看',
						items : [{
									layout : "column",
									border : false,
									items : [{
												columnWidth : 0.5,
												layout : "form",
												border : false,
												items : [planDate]
											}, {
												columnWidth : 0.5,
												layout : "form",
												border : false,
												items : [issueProerty]
											}]
								}, {
									layout : "column",
									border : false,
									items : [{
												columnWidth : 0.5,
												layout : "form",
												border : false,
												items : [dutyName]
											}, {
												columnWidth : 0.5,
												layout : "form",
												border : false,
												items : [dutyDeptName]
											}]
								}, {
									layout : "column",
									border : false,
									items : [{
												columnWidth : 0.5,
												layout : "form",
												border : false,
												items : [superName]
											}, {
												columnWidth : 0.5,
												layout : "form",
												border : false,
												items : [superDeptName]
											}]
								}, {
									layout : "column",
									border : false,
									items : [{
												columnWidth : 1,
												layout : "form",
												border : false,
												items : [existQuestion]
											}]
								}, {
									layout : "column",
									border : false,
									items : [{
												columnWidth : 1,
												layout : "form",
												border : false,
												items : [wholeStep]
											}]
								}, {
									layout : "column",
									border : false,
									items : [{
												columnWidth : 1,
												layout : "form",
												border : false,
												items : [avoidStep]
											}]
								}

						]
					});
			var win = new Ext.Window({
						width : 550,
						height : 380,
						buttonAlign : "right",
						items : [myaddpanel],
						layout : 'fit',
						closeAction : 'hide',
						resizable : false,
						modal : true,
						buttons : [{
									text : '关闭',
									iconCls : 'cancer',
									handler : function() {
										win.hide();
									}
								}]

					});

			function updateRecord() {
				if (grid.selModel.hasSelection()) {
					win.show();
					var record = grid.getSelectionModel().getSelected();
					myaddpanel.getForm().reset();
					myaddpanel.form.loadRecord(record);
					myaddpanel.setTitle("问题详细查看");
				}
			}

			queryRecord();

		});