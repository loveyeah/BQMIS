Ext.onReady(function() {
	
	var flagId;
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
									fillBy.setValue(result.workerName);
									fillByH.setValue(result.workerCode);
								}
							}
						});
			}
			var Record = Ext.data.Record.create([{
						name : 'dzjssm.dzjssmId'
					}, {
						name : 'dzjssm.jssmName'
					}, {
						name : 'dzjssm.content'
					}, {
						name : 'dzjssm.memo'
					}, {
						name : 'dzjssm.fillBy'
					}, {
						name : 'dzjssm.fillDate'
					}, {
						name : 'fillName'
					},{
						name : 'fillDate'
					}]);
			var sm = new Ext.grid.RowSelectionModel({
						singleSelect : false
					});
			var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({header:'行号',width : 50}), {
						header : "定值计算说明编号",
						dataIndex : "dzjssm.dzjssmId",
						hidden : true
					}, {
						header : "计算说明主题",
						dataIndex : "dzjssm.jssmName",
						width : 100
					}]);
			var ds = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : 'productionrec/getAllDzjssm.action'
								}),
						reader : new Ext.data.JsonReader({
								 totalProperty : "totalCount",
								 root : "list"
								}, Record)
					});
			//
			var fuzzy = new Ext.form.TextField({
						style : 'cursor:pointer',
						id : "fuzzy",
						name : 'fuzzy',
						readOnly : true,
						anchor : "80%"
					});
			ds.load({
						params : {
							name : fuzzy.getValue(),
							start : 0,
							limit : Constants.PAGE_SIZE
						}
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
						// 分页
						bbar : new Ext.PagingToolbar({
							pageSize : Constants.PAGE_SIZE,
							store : ds,
							displayInfo : true,
							displayMsg : '共 {2} 条',
							emptyMsg : Constants.EMPTY_MSG
						})
					});
			rightGrid.on('rowclick', dblclick);
			
			// 计算说明主题编号
			var dzjssmId = new Ext.form.Hidden({
				id : 'dzjssmId',
				name : 'dzjssm.dzjssmId'
			})
			
			// 计算说明主题
			var jssmName = new Ext.form.TextField({
						id : "jssmName",
						name : 'dzjssm.jssmName',
						anchor : "70%",
						fieldLabel : '计算说明主题',
						allowBlank : false
					});
			// 计算说明
			var content = new Ext.form.TextField({
						id : "content",
						fieldLabel : '计算说明',
						name : 'dzjssm.content',
						readOnly : false,
//						allowBlank : false,
						anchor : "70%"
					})
			
			// 备注
			var memo = new Ext.form.TextArea({
						id : "memo",
						fieldLabel : '备注',
						name : 'dzjssm.memo',
						height : 140,
						// readOnly : true,
						anchor : "70%"
					})
					
			// 填写日期
			var fillDate = new Ext.form.TextField({
						id : "fillDate",
						name : 'dzjssm.fillDate',
						readOnly : true,
						anchor : "70%",
						fieldLabel : '填写日期',
						value : getDate()
					})
					
			// 填写人
			var fillBy = new Ext.form.TextField({
						id : "fillBy",
						fieldLabel : '填写人',
						// name : 'briefnews.commonBy',
						readOnly : true,
						anchor : "70%"
					})
			var fillByH = new Ext.form.Hidden({
				id : 'fillByH',
				name : 'dzjssm.fillBy'
			})	
			function dblclick(){
			var rec = rightGrid.getSelectionModel().getSelected();
			flagId = rec.get("dzjssm.dzjssmId");
			dzjssmId.setValue(rec.get("dzjssm.dzjssmId")) ;
			jssmName.setValue(rec.get("dzjssm.jssmName"));
			content.setValue(rec.get("dzjssm.content"));
			memo.setValue(rec.get("dzjssm.memo"));
			fillDate.setValue(rec.get("fillDate"));
			fillBy.setValue(rec.get("fillName"));
			fillByH.setValue(rec.get("dzjssm.fillBy"))
			}
			
			
			
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
												columnWidth : 1,
												border : false,
												labelWidth : 100,
												layout : 'form',
												items : [dzjssmId]
											}, {
												columnWidth : 1,
												border : false,
												labelWidth : 100,
												layout : 'form',
												items : [jssmName]
											}, {
												columnWidth : 1,
												border : false,
												labelWidth : 100,
												layout : 'form',
												items : [content]
											}]
								}, {
									layout : 'column',
									border : false,
									items : [{
												columnWidth : 1,
												border : false,
												labelWidth : 100,
												layout : "form",
												items : [memo]
											}]
								}, {
									layout : 'column',
									border : false,
									items : [{
												columnWidth : 1,
												border : false,
												labelWidth : 100,
												layout : 'form',
												items : [fillDate]
											}, {
												columnWidth : 1,
												border : false,
												labelWidth : 100,
												layout : 'form',
												items : [fillBy,fillByH]
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
									iconCls : 'save',
									handler : Addnews
								}],
						items : [briefnews]
					});

			function Add() {
				myPanel.getForm().reset();
				flagId = "";
				getWorkCode();
				ds.reload();
			}

			function DeLnews() {
				if (rightGrid.getSelectionModel().getSelections().length > 0) {
				Ext.Msg.confirm('提示信息', '确认删除所选中记录?', function(button, text) {
					if (button == 'yes') {
						var rec = rightGrid.getSelectionModel().getSelections();
						var str = "";
						for (var i = 0; i < rec.length; i++) {
							if (i < rec.length - 1)
								str += rec[i].get('dzjssm.dzjssmId')
										+ ",";
							else
								str += rec[i].get('dzjssm.dzjssmId');
						}
						Ext.Ajax.request({
									waitMsg : '删除中,请稍后...',
									url : 'productionrec/deleteDzjssm.action',
									params : {
										ids : str
									},
									success : function(response, options) {
										Ext.Msg.alert('提示信息', '数据删除成功！')
										
										myPanel.getForm().reset();
										getWorkCode();
										flagId = "";
										ds.reload();
									},
									failure : function() {
										Ext.Msg.alert('提示信息', '服务器错误,请稍候重试!')
									}
								});
					}
				})
			} else {
				Ext.Msg.alert('提示信息', "请至少选择一行！");
				return false;
			}

			
			}

			function Addnews() {
				var url = "";
				if (flagId == null || flagId == "") {
					url = "productionrec/saveDzjssm.action";
				} else {
					url = "productionrec/updateDzjssm.action";
				}
				if (myPanel.getForm().isValid()) {
					myPanel.getForm().submit({
						method : 'POST',
						url : url,
//						params : {
//							dzjssmId : flagId
//						},
						success : function(form, action) {
										var result = eval('('
												+ action.response.responseText
												+ ')');
										// 显示成功信息
										Ext.Msg.alert("提示信息", result.msg);
							ds.load({
								params : {
									start : 0,
								limit : Constants.PAGE_SIZE,
									name : fuzzy.getValue()
								}
							});
							myPanel.getForm().reset();
							getWorkCode();
							flagId = "";
						},
						failure : function(form, action) {
										var result = eval('('
												+ action.response.responseText
												+ ')');
										Ext.Msg.alert("提示信息", result.msg);
						}
					});
				
				}
				
			}
			// 左边的panel
			var leftPanel = new Ext.Panel({
						title : '计算说明列表',
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
				        title : '计算说明信息',
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
						bodyStyle : 'padding : 5 5 5 5',
						autoScroll : true,
						layout : "border",
						items : [leftPanel, rightPanel]
					});
			getWorkCode()
		})
