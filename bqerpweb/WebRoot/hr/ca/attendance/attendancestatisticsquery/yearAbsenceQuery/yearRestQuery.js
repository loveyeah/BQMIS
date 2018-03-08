Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	
	function getYear() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) ;
		
		return s;
	}

	var dept = new Ext.ux.ComboBoxTree({
		fieldLabel : '部门',
		allowBlank : true,
		width : 200,
		id : "attendanceDeptId",
		anchor : '90%',
		displayField : 'text',
		valueField : 'id',
		hiddenName : 'attendanceDeptName',
		blankText : '请选择',
		emptyText : '请选择',
		tree : {
			xtype : 'treepanel',
			rootVisible : false,

			loader : new Ext.tree.TreeLoader({
				dataUrl : 'ca/getAttendanceDeptData.action'
			}),
			root : new Ext.tree.AsyncTreeNode({
				id : '0',
				name : '灞桥热电厂',
				text : '灞桥热电厂'
			}),
			listeners : { 
				click : function(node, e) {
					txtDeptHid.setValue(node.attributes.id);
					txtDeptName.setValue(node.attributes.text);
				}
			}
		},
		selectNodeModel : 'all'
	});
	//  姓名模糊查询
	var empName = new Ext.form.TextField({
				name : 'name',
				xtype : 'textfield',
				readOnly : false,
				fieldLabel : '姓名',
				anchor : '85%'
				
			});

	var txtDeptHid = new Ext.form.Hidden();
	var txtDeptName = new Ext.form.TextField();
	
	var con_item = Ext.data.Record.create([{
				name : 'deptName',
				mapping : 2
			}, {
				name : 'teamName',
				mapping : 3
			}, {
				name : 'chsName',
				mapping : 4
			}, {
				name : 'restDays',//年休假天数
				mapping : 5
			}, {
				name : 'staDate',
				mapping : 6
			}, {
				name : 'endDate',
				mapping : 7
			}, {
				name : 'hasDays',//已休年休天数
				mapping : 8
			}]);

	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'hr/queryYearRest.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, con_item)
			});
	
	
			
	var con_item_cm = new Ext.grid.ColumnModel([sm,
			{
				header : "部门",
				align : 'left',
				dataIndex : 'deptName'
			}, {
				header : '班组',
				dataIndex : 'teamName',
				align : 'center'

			}, {
				header : '姓名',
				dataIndex : 'chsName',
				align : 'center'

			}, {
				header : '年休假天数',
				dataIndex : 'restDays',
				align : 'center'

			}, {
				header : '年休开始时间',
				dataIndex : 'staDate',
				align : 'center',
				renderer:function(v)
				{
					if (v != null && v.length > 10&&v!="") {
							return v.substr(0, 10);
						} else {
							return v;
						}
				}
				  

			}, {
				header : '年休结束时间',
				dataIndex : 'endDate',
				align : 'center',
				renderer:function(v)
				{
					if (v != null && v.length > 10&&v!="") {
							return v.substr(0, 10);
						} else {
							return v;
						}
				}
				  
				

			}, {
				header : '已休年休天数',
				dataIndex : 'hasDays',
				align : 'center'

			}]);
	con_item_cm.defaultSortable = true;
		
	//年份
	var years = new Ext.form.TextField({
				name : 'years',
				fieldLabel : '年份',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '',
									alwaysUseStartDate : true,
									dateFmt : "yyyy",
									isShowClear : false
								});
						this.blur();
					}
				}

			});
	years.setValue(getYear());

	function query() {
		store.baseParams = {
			strYear : years.getValue(),
			deptId: txtDeptHid.getValue(),
			name:empName.getValue()
		}
			store.load({
						params : {
							start : 0,
							limit : 18
						}
		})
		
	}

var tbar = new Ext.Toolbar({
				items : ["年份:", years, '-',"部门:", dept, '-','姓名',empName, {
							text : '查询',
							iconCls : 'query',
							handler : query
						}]
			});
	

	
	var grid = new   Ext.grid.GridPanel({
				sm : sm,
				ds : store,
				cm : con_item_cm,
				tbar : tbar,
				border : false,
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : store,
							displayInfo : true,
							displayMsg : "显示第{0}条到{1}条，共{2}条",
							emptyMsg : "没有记录",
							beforePageText : '',
							afterPageText : ""
						})
			});
	
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [{
							layout : 'fit',
							border : false,
							frame : false,
							region : "center",
							items : [grid]
						}]
			});
			
			query();

})
