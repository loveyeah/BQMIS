Ext.onReady(function() {
			// 系统当前时间
			function getMonth() {
				var d, s, t;
				d = new Date();
				s = d.getFullYear().toString(10) + "-";
				t = d.getMonth() + 1;
				s += (t > 9 ? "" : "0") + t;
				return s;
			}
			var guideCode;
			var Record = Ext.data.Record.create([{
						name : 'guideNew.guideId'
					}, {
						name : 'editDate'
					}, {
						name : 'guideNew.guideContent'
					}, {
						name : 'guideNew.referDepcode'
					}, {
						name : 'guideNew.mainDepcode'
					}, {
						name : 'guideNew.otherDepcode'
					}, {
						name : 'guideNew.editBy'
					},{
						name : 'mainDepName'
					}, {
						name : 'referDepName'
					},{
						name : 'editByName'
					},{
						name : 'guideNew.ifComplete'
					},{
						name : 'guideNew.completeDesc'
					},{
						name : 'guideCode'
					}]);
			var sm = new Ext.grid.RowSelectionModel({
						singleSelect : false
					});
			var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
						header : "",
						dataIndex : "",
						hidden : true
					},{
					header : '顺序号',
					dataIndex : 'guideCode',
					width : 50
					}, {
						header : "计划内容",
						dataIndex : "guideNew.guideContent",
						sortable : true,
						width : 145
					}, {
						header : "主要落实单位",
						dataIndex : "mainDepName",
						width : 80
					}]);
			var ds = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : 'manageplan/getPlanNewsList.action'
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
						items : ["计划月份:", fuzzy, {
									text : "查询",
									iconCls : 'query',
									handler : function() {
										ds.load({
													params : {
														queryString : fuzzy.getValue()
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
			guideCode = rec.get("guideNew.guideId");
			commonDate.setValue(rec.get("editDate"));
			content.setValue(rec.get("guideNew.guideContent"));
			commonBy.setValue(rec.get("editByName"));
			hiddcommonBy.setValue(rec.get("guideNew.editBy"));
			referName.setValue(rec.get("referDepName"));
			hidreferDepcode.setValue(rec.get("guideNew.referDepcode"));
			mainName.setValue(rec.get("mainDepName"));
			mainDepcode.setValue(rec.get("guideNew.mainDepcode"));
			otherDepcode.setValue(rec.get("guideNew.otherDepcode"));
			guideId.setValue(rec.get("guideCode"));
			completeDesc.setValue(rec.get("guideNew.completeDesc"));
			ifComplete.setValue((rec.get("guideNew.ifComplete") == null)?"":rec.get("guideNew.ifComplete"));
			referName.setDisabled(true);
			mainName.setDisabled(true);
			}
			// 顺序号
			var guideId = new Ext.form.TextField({
						id : "guideId",
						fieldLabel : '顺序号',
//						name : 'briefnews.issue',
						readOnly : true,
						disabled : true,
//						allowBlank : false,
						value : '',
						anchor : "90%"
			});
			// 编制人
			var commonBy = new Ext.form.TextField({
						id : "commonBy",
						fieldLabel : '编制人',
						// name : 'briefnews.commonBy',
						readOnly : true,
						disabled : true,
						anchor : "90%"
					})
			var hiddcommonBy = new Ext.form.Hidden({
						id : 'hiddcommonBy',
						name : 'guideNew.editBy'
					})
			//  编制日期
			var commonDate = new Ext.form.TextField({
						id : "commonDate",
						name : 'guideNew.editDate',
						readOnly : true,
						anchor : "90%",
						disabled : true,
						fieldLabel : ' 编制日期',
						value : ""
					})
			// 内容
			var content = new Ext.form.TextArea({
						id : "content",
						fieldLabel : '内容',
						name : 'guideNew.guideContent',
						height : 140,
						disabled : true,
						 readOnly : true,
						anchor : "94%"
					})
					//提出单位
			var referName = new  Ext.form.ComboBox({
						fieldLabel : '提出单位',
						readOnly : true,
						disabled : true,
						anchor : "90%"
			})
			referName.on('beforequery',function(){
			 return false;
			})
		 var hidreferDepcode = new Ext.form.Hidden({
		 	id : "hidreferDepcode",
		 	name : 'guideNew.referDepcode'
		 })
		 //主要落实单位
		 var mainName = new Ext.form.ComboBox({
						fieldLabel : '主要落实单位',
						readOnly : true,
						disabled : true,
						anchor : "90%"
			
		 })
		 mainName.on('beforequery',function(){
		 return false;
		 })
		 var mainDepcode = new Ext.form.Hidden({
						  id :'mainDepcode',
						  name :'guideNew.mainDepcode'
		 })
		 //其他落实单位
		 var otherDepcode = new Ext.form.TextField({
		 			id : 'otherDepcode',
		 			name : 'guideNew.otherDepcode',
		 			fieldLabel : '其他落实单位',
		 			readOnly : true,
		 			disabled : true,
		 			anchor : '95%'
		 			
		 })
		 //完成情况
		 var ifComplete = new Ext.form.ComboBox({
					readOnly : true,
					name : 'name',
					hiddenName : 'guideNew.ifComplete',
					mode : 'local',
					fieldLabel : '完成情况',
					triggerAction : 'all',
					store : new Ext.data.SimpleStore(
						{
							fields : ['name','value'],
							data : [
									['未完成', '0'],
									['进行中', '1'],
									['已完成', '2']]
						}),
					valueField : 'value',
					displayField : 'name',
					anchor : "95%"
		 })
		 //完成说明
		 var completeDesc = new Ext.form.TextArea({
					 	id : "completeDesc",
						fieldLabel : '完成说明',
						name : 'guideNew.completeDesc',
						height : 90,
						// readOnly : true,
						anchor : "94%"
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
												labelWidth : 80,
												layout : 'form',
												items : [guideId]
											}]
								},  {
									layout : 'column',
									border : false,
									items : [{
												columnWidth : 1,
												border : false,
												labelWidth : 80,
												layout : "form",
												items : [content]
											}]
								},{
									layout : 'column',
									border : false,
									items : [{
												columnWidth : 0.5,
												border : false,
												labelWidth :80,
												layout : "form",
												items : [referName,hidreferDepcode]
											},{
												columnWidth :0.5,
												border : false,
												labelWidth : 80,
												layout : "form",
												items : [mainName,mainDepcode]
											}]
								},{
									layout : 'column',
									border : false,
									items : [{
												columnWidth : 1,
												border : false,
												labelWidth : 80,
												layout : "form",
												items : [otherDepcode]
											}]
								
								},{
									layout : 'column',
									border : false,
									items : [{
												columnWidth : 1,
												border : false,
												labelWidth : 80,
												layout : "form",
												items : [ifComplete]
											}]
								},{
									layout : 'column',
									border : false,
									items : [{
												columnWidth : 1,
												border : false,
												labelWidth : 80,
												layout : "form",
												items : [completeDesc]
											}]
								},{
									layout : 'column',
									border : false,
									items : [{
												columnWidth : 0.5,
												border : false,
												labelWidth : 80,
												layout : 'form',
												items : [commonBy,hiddcommonBy]
											},{
												columnWidth : 0.5,
												border : false,
												labelWidth : 80,
												layout : 'form',
												items : [commonDate]
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
									text : '取消',
									iconCls : 'delete',
									hidden : true,
									handler : Add
								},'-', {
									id : 'btnAdd',
									text : '保存',
									iconCls : 'add',
									handler : Addnews
								}],
						items : [briefnews]
					});

			function Add() {
				ifComplete.setValue("");
				completeDesc.setValue("");
//				myPanel.getForm().reset();
				guideCode = "";
			}
			function Addnews() {
				if(guideCode == null || guideCode == ""){
					Ext.Msg.alert('提示','请选择一条记录!');
					return false;
				}
				var url = "manageplan/updatePlanNews.action";
				if (myPanel.getForm().isValid()) {
					myPanel.getForm().submit({
						method : 'POST',
						url : url,
						params : {
							guideCode : guideCode,
							planTime : fuzzy.getValue()
						},
						success : function(form, action) {
							var o = eval("(" + action.response.responseText
									+ ")")
							guideCode=o.guideId;
							guideId.setValue(guideCode.substring(6,10));
							Ext.Msg.alert("提示",'保存成功!');
							ds.load({
								params : {
									queryString : fuzzy.getValue()
								}
							});
						},
						faliue : function() {
							Ext.Msg.alert('提示', '保存失败.');
						}
					});

				}
			}
			// 左边的panel
			var leftPanel = new Ext.Panel({
						title : '厂部临时安排列表',
						region : 'west',
						layout : 'fit',
						width : 300,
						autoScroll : true,
						border : false,
						containerScroll : true,
						collapsible : true,
						split :false,
						titleCollapse : true,
						items : [rightGrid]
					});

			var rightPanel = new Ext.Panel({
				        title : '厂部临时安排信息',
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
		})
