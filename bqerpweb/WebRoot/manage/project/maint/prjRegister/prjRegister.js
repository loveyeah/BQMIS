Ext.ns("maint.prjRegList");
maint.prjRegList = function() {
	
	var returnObj=new Object();
	function renderMoney(v) {
		return renderNumber(v, 2);// 修改计算金额现在2位小数
	}

	function getYear() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10);
		return s;
	}

	function renderNumber(v, argDecimal) {
		if (v) {
			if (typeof argDecimal != 'number') {
				argDecimal = 2;
			}
			v = Number(v).toFixed(argDecimal);
			var t = '';
			v = String(v);
			while ((t = v.replace(/^(-?[0-9]+)([0-9]{3}.*)$/, '$1,$2')) !== v)
				v = t;
			return v;
		} else
			return '';
	}

	var year = new Ext.form.TextField({
		style : 'cursor:pointer',
		name : 'time',
		fieldLabel : '计划时间',
		readOnly : true,
		width:60,
		anchor : "20%",
		// value : getYear(),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y',
					alwaysUseStartDate : false,
					dateFmt : 'yyyy',
					isShowClear : true,
					onpicked : function(v) {
						query();
						this.blur();
					}
				});
			}
		}
	});
	// 项目类别
	var prjTypeName = new Ext.ux.ComboBoxTree({
		fieldLabel : "项目类别<font color='red'>*</font>",
		displayField : 'text',
		valueField : 'id',
		id : 'prjTypeName',
		allowBlank : false,
		blankText : '请选择',
		emptyText : '请选择',
		readOnly : true,
		width:100,
		anchor : "50%",
		tree : {
			xtype : 'treepanel',
			rootVisible : false,
			loader : new Ext.tree.TreeLoader({
				dataUrl : 'manageproject/findByPId.action'
			}),
			root : new Ext.tree.AsyncTreeNode({
				id : '0',
				name : '灞桥电厂',
				text : '灞桥电厂'
			})
		},
		selectNodeModel : 'all',
		listeners : {
			'select' : function(combo, record, index) {
				query();
			}
		}
	});
	
	var prjName = new Ext.form.TextField({
		fieldLabel:'项目名称'
	})

	var sm = new Ext.grid.CheckboxSelectionModel({singleSelect:true});
	// grid列表数据源

	var Record = new Ext.data.Record.create([sm, {
		name : 'prjId',
		mapping : 0
	}, {
		name : 'prjNo',
		mapping : 1
	}, {
		name : 'prjName',
		mapping : 2
	}, {
		name : 'prjDept',
		mapping : 3
	}, {
		name : 'prjDeptName',
		mapping : 4
	}, {
		name : 'prjTypeId',
		mapping : 5
	}, {
		name : 'applyFunds',
		mapping : 6
	}, {
		name : 'approvedFunds',
		mapping : 7
	}, {
		name : 'isFundsFinish',
		mapping : 8
	}, {
		name : 'prjYear',
		mapping : 9
	}, {
		name : 'duration',
		mapping : 10
	}, {
		name : 'prjBy',
		mapping : 14
	}, {
		name : 'prjByName',
		mapping : 15
	}, {
		name : 'prjTypeName',
		mapping : 16
	}]);
var confirm = new Ext.Button({
		text:'确定',
		handler:function(){
			chooseValue();
		}
		
	})
	var gridTbar = new Ext.Toolbar({
		items : ['年度', year, '-', '项目类别', prjTypeName, '-','项目名称', prjName,{
			id : 'query',
			text : "查询",
			iconCls : 'query',
			handler : query
		},'-',confirm]
	});


	function query() {
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		})
	}

	
	

	var btnQuery = new Ext.Button({
		text : '查询',
		iconCls : 'query',
		handler : query
	});
	

	function chooseValue(){
		var records = prjgrid.getSelectionModel().getSelections();
		if(records.length==1){
			var record = prjgrid.getSelectionModel().getSelected();
			returnObj.prjId=record.data.prjId;
			returnObj.prjName = record.data.prjName;
			win.hide();
		}else{
			Ext.Msg.alert('警告','请选择一条记录！');
		}
	
	}


	var store = new Ext.data.JsonStore({
		url : 'manageproject/getPrjRegister.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : Record
	});

	// 页面的Grid主体
	var prjgrid = new Ext.grid.GridPanel({
		store : store,
		layout : 'fit',
		listeners : {
			'rowdblclick' : function() {
				chooseValue();
			}
		},
		columns : [sm, new Ext.grid.RowNumberer({
			header : '行号',
			width : 35
		}),	// 选择框
				{
					header : '项目编号',
					dataIndex : 'prjNo',
					align : 'left',
					width : 75
				}, {
					header : '项目名称',
					dataIndex : 'prjName',
					align : 'left',
					width : 75
				}, {
					header : "立项部门",
					width : 75,
					align : 'center',
					dataIndex : 'prjDeptName'
				}, {
					header : '立项人',
					dataIndex : 'prjByName',
					align : 'left',
					width : 75
				}],

		sm : sm, // 选择框的选择
		tbar : gridTbar,
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "{0}到{1}条，共{2}条",
			emptyMsg : "没有记录",
			beforePageText : '页',
			afterPageText : "共{0}"
		}),
		clicksToEdit : 1,
		viewConfig : {
			forceFit : true
		},
		autoWidth : true,
		autoScroll : true,
		fitToFrame : true
	});

	store.on("beforeload", function() {
		Ext.apply(this.baseParams, {
			year : year.getValue(),
			prjType : prjTypeName.getValue(),
			prjName:prjName.getValue(),
			flag:'query'
		});
	});

	query();
	

	var win = new Ext.Window({
		title:'立项查询',
		closeAction : 'hide',
		width :550,
		height : 400,
		layout:'fit',
		items:[prjgrid]
	})

	
	return {
		win:win,
		returnValue:returnObj
	}


}