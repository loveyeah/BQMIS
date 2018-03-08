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
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号
							commonBy.setValue(result.workerName);
							hiddcommonBy.setValue(result.workerCode);
						}
					}
				});
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
			}, {
				name : 'mainDepName'
			}, {
				name : 'referDepName'
			}, {
				name : 'editByName'
			}, {
				name : 'guideCode'
			}]);
	var sm = new Ext.grid.RowSelectionModel({
				singleSelect : false
			});
	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
				header : "",
				dataIndex : "",
				hidden : true
			}, {
				header : '顺序号',
				dataIndex : 'guideCode',
				width : 50
			}, {
				header : "计划内容",
				dataIndex : "guideNew.guideContent",
				sortable : true,
				width : 170
			}, {
				header : "编制人",
				dataIndex : "editByName",
				width : 55
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
				items : ["月份:", fuzzy, {
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
	function dblclick() {
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
	}
	// 顺序号
	var guideId = new Ext.form.TextField({
				id : "guideId",
				fieldLabel : '顺序号',
				// name : 'briefnews.issue',
				readOnly : true,
				// allowBlank : false,
				value : '自动生成',
				anchor : "90%"
			});
	// 编制人
	var commonBy = new Ext.form.TextField({
				id : "commonBy",
				fieldLabel : '编制人',
				// name : 'briefnews.commonBy',
				readOnly : true,
				anchor : "90%"
			})
	var hiddcommonBy = new Ext.form.Hidden({
				id : 'hiddcommonBy',
				name : 'guideNew.editBy'
			})
	// 编制日期
	var commonDate = new Ext.form.TextField({
				id : "commonDate",
				name : 'guideNew.editDate',
				readOnly : true,
				anchor : "90%",
				fieldLabel : ' 编制日期',
				value : getDate()
			})
	// 内容
	var content = new Ext.form.TextArea({
				id : "content",
				fieldLabel : '内容',
				name : 'guideNew.guideContent',
				height : 140,
				allowBlank : false,
				// readOnly : true,
				anchor : "94%"
			})
	// 提出单位
	var referName = new Ext.form.ComboBox({
		fieldLabel : '提出单位',
		readOnly : true,
		anchor : "90%",
		allowBlank : false,
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
							'dialogWidth:270px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (typeof(rvo) != "undefined") {
				hidreferDepcode.setValue(rvo.codes);
				referName.setValue(rvo.names);
			}
		}
	})
	var hidreferDepcode = new Ext.form.Hidden({
				id : "hidreferDepcode",
				name : 'guideNew.referDepcode'
			})
	// 主要落实单位
	var mainName = new Ext.form.ComboBox({
		fieldLabel : '主要落实单位',
		readOnly : true,
		anchor : "90%",
		allowBlank : false,
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
							'dialogWidth:270px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (typeof(rvo) != "undefined") {
				mainDepcode.setValue(rvo.codes);
				mainName.setValue(rvo.names);
			}
		}

	})
	var mainDepcode = new Ext.form.Hidden({
				id : 'mainDepcode',
				name : 'guideNew.mainDepcode'
			})
	// 其他落实单位
	var otherDepcode = new Ext.form.TextField({
				id : 'otherDepcode',
				name : 'guideNew.otherDepcode',
				fieldLabel : '其他落实单位',
				anchor : '95%'

			})
	// 简报信息
	var briefnews = new Ext.form.FieldSet({
				// height : '100%',
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
						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 1,
										border : false,
										labelWidth : 80,
										layout : "form",
										items : [content]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 0.5,
										border : false,
										labelWidth : 80,
										layout : "form",
										items : [referName, hidreferDepcode]
									}, {
										columnWidth : 0.5,
										border : false,
										labelWidth : 80,
										layout : "form",
										items : [mainName, mainDepcode]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 1,
										border : false,
										labelWidth : 80,
										layout : "form",
										items : [otherDepcode]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 0.5,
										border : false,
										labelWidth : 80,
										layout : 'form',
										items : [commonBy, hiddcommonBy]
									}, {
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
		guideCode = "";
		getWorkCode();
	}

	function DeLnews() {
		if (guideCode == "undefined" || guideCode == null || guideCode == "") {
			Ext.Msg.alert("提示", "请在左边列表中选择一条记录");
			return false;
		}
		Ext.Msg.confirm("删除", "是否确定删除该记录？", function(buttonobj) {
					if (buttonobj == "yes") {
						Ext.lib.Ajax.request('POST',
								'manageplan/deletePlanNews.action', {
									success : function(action) {
										Ext.Msg.alert("提示", "删除成功！")
										myPanel.getForm().reset();
										briefnewsId = "";
										getWorkCode();
										ds.reload();

									},
									failure : function() {
										Ext.Msg.alert('错误', '删除失败.');
									}
								}, 'guideCode=' + guideCode);
					}
				});
	}

	function Addnews() {
		var url = "";
		if (guideCode == null || guideCode == "") {
			url = "manageplan/addPlanNews.action";
		} else {
			url = "manageplan/updatePlanNews.action";
		}
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
							guideCode = o.guideId;
							guideId.setValue(guideCode.substring(6, 10));
							Ext.Msg.alert("注意", o.msg);
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
				title : '厂部临时安排列表',
				region : 'west',
				layout : 'fit',
				width : 300,
				autoScroll : true,
				border : false,
				containerScroll : true,
				collapsible : true,
				split : false,
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
	getWorkCode()
})
