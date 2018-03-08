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
	}, {
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
		fieldLabel : '月份'
	//	value : getMonth()
	});
	// 期号
	var issue = new Ext.form.NumberField({
		id : "issue",
		fieldLabel : '期号',
		name : 'briefnews.issue',
		readOnly : true,
		allowBlank : true,
		anchor : "80%"
	})
	// 填写人
	var commonBy = new Ext.form.TextField({
		id : "commonBy",
		fieldLabel : '填写人',
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
		readOnly : true,
		anchor : "90%"
	})
	// 简报信息
	var briefnews = new Ext.form.FieldSet({
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
		items : [briefnews]
	});

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
		split : false,
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
})
