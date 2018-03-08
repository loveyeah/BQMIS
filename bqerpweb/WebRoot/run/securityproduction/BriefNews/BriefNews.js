Ext.onReady(function() {
			// 系统当前时间
			function getDate() {
				var d, s, t;
				d = new Date();
				s = d.getFullYear().toString(10) + "-";
				t = d.getMonth() + 1;
				s += (t > 9 ? "" : "0") + t + "-";
				t = d.getDate();
				s += (t > 9 ? "" : "0") + t + " ";
				return s;
			}
			function getMonth() {
				var d, s, t;
				d = new Date();
				s = d.getFullYear().toString(10) + "-";
				t = d.getMonth() + 1;
				s += (t > 9 ? "" : "0") + t;
				return s;
			}
			function getWorkCode() {
				Ext.lib.Ajax.request('POST',
						'comm/getCurrentSessionEmployee.action', {
							success : function(action) {
								var result = eval("(" + action.responseText
										+ ")");
								if (result.workerCode) {
									// 设定默认工号
									commonBy.setValue(result.workerName);
									hiddcommonBy.setValue(result.workerCode);
								}
							}
						});
			}
			var briefnewsId;
			var Record = Ext.data.Record.create([{
						name : 'briefnews.briefnewsId'
					}, {
						name : 'month'
					}, {
						name : 'briefnews.issue'
					}, {
						name : 'briefnews.content'

					}, {
						name : 'briefnews.commonBy'
					}, {
						name : 'commondate'
					}, {
						name : 'enterpriseCode'
					},{
						name : 'workName'
					}]);
			var sm = new Ext.grid.RowSelectionModel({
						singleSelect : false
					});
			var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
						header : "",
						dataIndex : "",
						hidden : true
					}, {
						header : "月份",
						dataIndex : "month",
						sortable : true,
						width : 100
					}, {
						header : "期号",
						dataIndex : "briefnews.issue",
						width : 100
					}]);
			var ds = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : 'security/getBriefNews.action'
								}),
						reader : new Ext.data.JsonReader({
								// totalProperty : "totalProperty",
								// root : "root"
								}, Record)
					});
			//
			var fuzzy = new Ext.form.TextField({
						style : 'cursor:pointer',
						id : "fuzzy",
						name : 'fuzzy',
						readOnly : true,
						anchor : "80%",
						value : getMonth(),
						listeners : {
							focus : function() {
								WdatePicker({
											startDate : '%y-%M',
											alwaysUseStartDate : false,
											dateFmt : 'yyyy-MM',
											onpicked : function() {
											},
											onclearing : function() {
												planStartDate.markInvalid();
											}
										});
							}
						}
					});
			ds.load({
						params : {
							queryString : fuzzy.getValue()
						}
					});
			var stocktbar = new Ext.Toolbar({
						// height : 25,
						items : ["月份:", fuzzy, {
									text : "查询",
									iconCls : 'query',
									handler : function() {
										ds.load({
													params : {
														queryString : fuzzy
																.getValue()
													}
												});
									}
								}]
					});

			var rightGrid = new Ext.grid.GridPanel({
						layout : 'fit',
						region : 'west',
						region : "center",
						anchor : "100%",
						autoScroll : true,
						style : "border-top:solid 1px",
						border : false,
						enableColumnMove : false,
						sm : sm,
						ds : ds,
						cm : cm,
						tbar : stocktbar
					});
			rightGrid.on('rowclick', dblclick);
			function dblclick(){
			var rec = rightGrid.getSelectionModel().getSelected();
			briefnewsId = rec.get("briefnews.briefnewsId");
			month.setValue(rec.get("month"));
			issue.setValue(rec.get("briefnews.issue"));
			commonBy.setValue(rec.get("workName"));
			commonDate.setValue(rec.get("commondate"));
			content.setValue(rec.get("briefnews.content"));
			}
			// 月份
			var month = new Ext.form.TextField({
						style : 'cursor:pointer',
						id : "month",
						name : 'month',
						readOnly : true,
						anchor : "70%",
						fieldLabel : '月份',
						value : getMonth(),
						listeners : {
							focus : function() {
								WdatePicker({
											startDate : '%y-%M',
											alwaysUseStartDate : false,
											dateFmt : 'yyyy-MM',
											onpicked : function() {
											},
											onclearing : function() {
												planStartDate.markInvalid();
											}
										});
							}
						}
					});
			// 期号
			var issue = new Ext.form.NumberField({
						id : "issue",
						fieldLabel : '期号',
						name : 'briefnews.issue',
						readOnly : false,
						allowBlank : false,
						anchor : "80%"
					})
			// 填写人
			var commonBy = new Ext.form.TextField({
						id : "commonBy",
						fieldLabel : '填写人',
						// name : 'briefnews.commonBy',
						readOnly : true,
						anchor : "70%"
					})
			var hiddcommonBy = new Ext.form.Hidden({
						id : 'hiddcommonBy',
						name : 'briefnews.commonBy'
					})
			// 填写日期
			var commonDate = new Ext.form.TextField({
						id : "commonDate",
						name : 'briefnews.commonDate',
						readOnly : true,
						anchor : "80%",
						fieldLabel : '填写日期',
						value : getDate()
					})
			// 内容
			var content = new Ext.form.TextArea({
						id : "content",
						fieldLabel : '内容',
						name : 'briefnews.content',
						height : 140,
						// readOnly : true,
						anchor : "90%"
					})
			// 简报信息
			var briefnews = new Ext.form.FieldSet({
						//height : '100%',
				        border : false,
						collapsible : false,
						layout : 'form',
						items : [{
									layout : 'column',
									border : false,
									items : [{
												columnWidth : 0.5,
												border : false,
												labelWidth : 50,
												layout : 'form',
												items : [month]
											}, {
												columnWidth : 0.5,
												border : false,
												labelWidth : 60,
												layout : 'form',
												items : [issue]
											}]
								}, {
									layout : 'column',
									border : false,
									items : [{
												columnWidth : 0.5,
												border : false,
												labelWidth : 50,
												layout : 'form',
												items : [commonBy, hiddcommonBy]
											}, {
												columnWidth : 0.5,
												border : false,
												labelWidth : 60,
												layout : 'form',
												items : [commonDate]
											}]
								}, {
									layout : 'column',
									border : false,
									items : [{
												columnWidth : 1,
												border : false,
												labelWidth : 50,
												layout : "form",
												items : [content]
											}]
								}]
					})

			var myPanel = new Ext.form.FormPanel({
						bodyStyle : "padding:5px 5px 0",
						region : "center",
						labelAlign : 'right',
						layout : 'form',
						autoheight : false,
						autoScroll : true,
						containerScroll : true,
						tbar : [{
									id : 'btnFirst',
									text : '增加',
									iconCls : 'add',
									handler : Add
								}, '-', {
									id : 'delete',
									text : '删除',
									iconCls : 'delete',
									handler : DeLnews

								}, '-', {
									id : 'btnAdd',
									text : '保存',
									iconCls : 'add',
									handler : Addnews
								}],
						items : [briefnews]
					});

			function Add() {
				myPanel.getForm().reset();
				briefnewsId = "";
				getWorkCode();
			}

			function DeLnews() {
				if(briefnewsId == "undefined" || briefnewsId == null || briefnewsId == ""){
					Ext.Msg.alert("提示","请在左边列表中选择一条记录");
					return false;
				}
				Ext.Msg.confirm("删除",
//						"是否确定删除简报编号为'" + briefnewsId + "'的记录？", function(
				"是否确定删除该记录？", function(
								buttonobj) {
							if (buttonobj == "yes") {
								Ext.lib.Ajax.request('POST',
										'security/deleteBriefnews.action', {
											success : function(action) {
												Ext.Msg.alert("提示", "删除成功！")
												myPanel.getForm().reset();
												briefnewsId = "";
												getWorkCode();
												ds.reload();

											},
											failure : function() {
												Ext.Msg.alert('错误',
														'删除时出现未知错误.');
											}
										}, 'briefnewsId=' + briefnewsId);
							}
						});
			}

			function Addnews() {
				var url = "";
				if (briefnewsId == null || briefnewsId == "") {
					url = "security/addBriefNews.action";
				} else {
					url = "security/updateBriefNews.action";
				}
				if (myPanel.getForm().isValid()) {
					myPanel.getForm().submit({
						method : 'POST',
						url : url,
						params : {
							briefnewsId : briefnewsId
						},
						success : function(form, action) {
							var o = eval("(" + action.response.responseText
									+ ")")
							briefnewsId =o.briefnewsId;
							Ext.Msg.alert("注意", o.msg);
							fuzzy.setValue(month.getValue());
							ds.load({
								params : {
									queryString : fuzzy.getValue()
								}
							});
						},
						faliue : function() {
							Ext.Msg.alert('错误', '出现未知错误.');
						}
					});

				}
			}
			// 左边的panel
			var leftPanel = new Ext.Panel({
						title : '简报列表',
						region : 'west',
						layout : 'fit',
						width : 270,
						autoScroll : true,
						border : false,
						containerScroll : true,
						collapsible : true,
						split :false,
						titleCollapse : true,
						items : [rightGrid]
					});

			var rightPanel = new Ext.Panel({
				        title : '简报信息',
						region : "center",
						layout : 'border',
						border : false,
						margins : '0',
						layoutConfig : {
								animate : true
							},
						items : [myPanel]
					});
			var view = new Ext.Viewport({
						enableTabScroll : true,
						autoScroll : true,
						layout : "border",
						items : [leftPanel, rightPanel]
					});
			getWorkCode()
		})
