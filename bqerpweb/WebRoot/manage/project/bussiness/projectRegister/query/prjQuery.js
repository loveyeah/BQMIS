Ext.onReady(function() {
	function getYear() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10);
		return s;
	}
	
	
	var year = new Ext.form.TextField({
				style : 'cursor:pointer',
				name : 'time',
				fieldLabel : '计划时间',
				readOnly : true,
				anchor : "80%",
//				value : getYear(),
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
				anchor : "85%",
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
	
	var isFunds = new Ext.form.ComboBox({
				readOnly : true,
				name : 'isFunds',
				hiddenName : 'isFunds',
				mode : 'local',
				width : 70,
				value : "",
				fieldLabel : '落实资金',
				triggerAction : 'all',
				listeners : {
					"select" : function() {
						query();
						
					}
				},

				store : new Ext.data.SimpleStore({
							fields : ['name', 'value'],
							data : [['全部', ''],['是', 'Y'], ['否', 'N']]
						}),
				valueField : 'value',
				displayField : 'name',
				anchor : "15%"
				
			})
   function renderMoney(v) {
    	return renderNumber(v, 2);//修改计算金额现在2位小数
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
	var sm = new Ext.grid.CheckboxSelectionModel();
	// grid列表数据源
	

	var Record = new Ext.data.Record.create([sm, {

				name : 'prjId',
				mapping:0
			}, {
				name : 'prjNo',
				mapping:1
			}, {
				name : 'prjName',
				mapping:2
			}, {
				name : 'prjDept',
				mapping:3
			},{
				name : 'prjDeptName',
				mapping:4
			},  {
				name : 'prjTypeId',
				mapping:5
			}, {
				name : 'applyFunds',
				mapping:6
			}, {
				name : 'approvedFunds',
				mapping:7
			}, {
				name : 'isFundsFinish',
				mapping:8
			}, {
				name : 'prjYear',
				mapping:9
			}, {
				name : 'duration',
				mapping:10
			}, {
				name : 'prjBy',
				mapping:14
			},{
				name : 'prjByName',
				mapping:15
			},{
				name : 'prjTypeName',
				mapping:16
			},//add by ypan 20100913
				{
		name : 'filePath',
		mapping : 17
	},{
		name : 'statusId',
		mapping : 18
	}]);


	var gridTbar = new Ext.Toolbar({
				items : ['年度',year,'-','项目类别',prjTypeName,'-','落实资金',isFunds,'-',{
							id : 'query',
							text : "查询",
							iconCls : 'query',
							handler : query
						}]
			});

	
			
	function query() {
		store.load({
					params : {
						start : 0,
						limit : 18
					
					}
				})
	}
     function addLine()
  {
  	
		// 统计行
		var o = new Record({
					'prjId' : '',
					'prjNo' : '',
					'statusId' : '',		
					'prjName' : '',
					'prjDept' : '',
					'prjTypeId' : '',
					'applyFunds' : '',
					'approvedFunds' : '',
					'isFundsFinish' : '',
					'prjYear' : '',
					'duration' : '',
					'prjBy' : '',
					'isNewRecord':'total'

				});
				var count = store.getCount();
				var currentIndex = count;
				prjgrid.stopEditing();
				store.insert(currentIndex, o);	
				prjgrid.getView().refresh();
						
		
  }
	
	var btnQuery = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : query
			});


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
		columns : [sm, new Ext.grid.RowNumberer({
							header : '行号',
							width : 35
						}),// 选择框
				{
					header : '项目编号',
					dataIndex : 'prjNo',
					align : 'left',
					width : 100,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount()-1 ) {
							var prjNo = record.data.prjNo;
							// 强行触发renderer事件
							return prjNo;
						} else {
							
							if(record.get("isNewRecord")=="total")
							{
						
							return "<font color='red'>"+"合计"+"</font>";
							}
						}
						
					}
					
				}, {
					header : '状态',
					dataIndex : 'statusId',
					align : 'left',
					width : 100
				},{
					header : '项目名称',
					dataIndex : 'prjName',
					align : 'left',
					width : 100
					
				},{
					header : "立项部门",
					width : 100,
					align : 'center',
					dataIndex : 'prjDeptName'
					
					
				}, {
					header : '立项人',
					dataIndex : 'prjByName',
					align : 'left',
					width : 100
					
				}, {
					header : '项目类别',
					dataIndex : 'prjTypeName',
					align : 'left',
					width : 100
					
					
				}, {
					header : '申请资金',
					dataIndex : 'applyFunds',
					align : 'left',
					width : 100,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							var applyFunds = record.data.applyFunds;
							// 强行触发renderer事件
							var totalSum = 0;
							return applyFunds;
						} else {
						
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('applyFunds');
							}
										return "<font color='red'>" +renderMoney( totalSum)
									+ "</font>";
							}
							
						}
					
				}, {
					header : '已审批资金',
					dataIndex : 'approvedFunds',
					align : 'left',
					width : 100,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
					if (rowIndex < store.getCount() - 1) {
							var approvedFunds = record.data.approvedFunds;
							// 强行触发renderer事件
					  return  renderMoney(approvedFunds);
						} else {
						
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('approvedFunds');
							}
										return "<font color='red'>" + renderMoney(totalSum)
									+ "</font>";
							
						} 
						
					}
				},{
					header : '是否已落实资金',
					dataIndex : 'isFundsFinish',
					width : 100,
					align : 'center',
					renderer:function (val)
					{
						if(val=="Y")
						{
						return "是";
						}
						else if(val=="N")
						{
						return"否";
						}
					}
					
		
				}, {
					header : '项目年份',
					dataIndex : 'prjYear',
					readOnly:true,
					width : 120,
					align : 'center'
				
				}, {
					header : '工期',
					dataIndex : 'duration',
					align : 'center',
					width : 80
				
				},  //add by ypan 20100913
					{
					header : "附件",
					sortable : true,
					dataIndex : 'filePath',
					renderer : function(v) {
						if(v !=null && v !='')
								{ 
									var s =  '<a href="#" onclick="window.open(\''+v+'\');return  false;">[查看]</a>';
									return s;
								}else{
									return '没有附件';
								}
						}
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
		viewConfig : {
			forceFit : true
		}
	});
	// 进入页面时执行一次查询


	store.on("beforeload", function() {
		Ext.apply(this.baseParams, {
			year : year.getValue(),
			prjType : prjTypeName.getValue(),
			isFunds : isFunds.getValue(),
			flag:'query'
		});
	});
	store.on("load",addLine);
	
	query();
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "fit",
				items : [prjgrid]
			});




	})