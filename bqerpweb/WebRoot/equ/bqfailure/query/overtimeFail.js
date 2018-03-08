Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
			var w = screen.availWidth - 210;
			var currentPanel = "main";
			var overtimeFail = Ext.data.Record.create([{
				name : 'roleId'
			}, {
				name : 'roleName'
			}, {
				name : 'memo'
			}]);
			var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
					url : 'system/findRole.action',
					method : 'post'
				}),
				reader : new Ext.data.JsonReader({
					totalProperty : 'data.total',
					root : 'data.rolesList'
				}, overtimeFail)
			});
			/* 设置每一行的选择框 */
			var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false,
				listeners : {
					rowselect : function(sm, row, rec) {
						
					}
				}
			});

			ds.on('beforeload', function() {	
			});
			var cm = new Ext.grid.ColumnModel([sm, {
				header : '缺陷编码',
				dataIndex : 'roleId',
				align : 'left'
			}, {
				header : '当前状态',
				dataIndex : 'memo',
				width : 250,
				align : 'left'
			}, {
				header : '缺陷类别',
				dataIndex : 'roleName',
				align : 'left'
			}]);
//			cm.defaultSortable = true;
			/* 设置分页的工具条 */
			var bbar = new Ext.PagingToolbar({
				pageSize : 30,
				store : ds,
				displayInfo : true,
				displayMsg : '显示第 {0} 条到 {1} 条记录，一共 {2} 条',
				emptyMsg : "没有记录"
			})
			var tbar = new Ext.Toolbar({
				items : ['从',{
					xtype : 'datefield',
					format : 'Y-m-d',
					name : 'date1',
					value : new Date().getYear(),
					readOnly : true
				}, '到',{
					xtype : 'datefield',
					format : 'Y-m-d',
					name : 'date2',
					value : new Date().getYear(),
					readOnly : true
				},'-','处理类别:',new Ext.ux.ComboBoxTree({
					id:'delType',
					width : 120, 
					value:{id:'0',text:'全部',attributes:{description:'delType'}},
					tree : {
						xtype : 'treepanel', 
						loader : new Ext.tree.TreeLoader({
							dataUrl : ''
						}),
						root : new Ext.tree.AsyncTreeNode({
							id : '0',
							text : '全部'
						} 
						)
					},
					selectNodeModel : 'all' 
				}),
			'-','工单类别:',new Ext.ux.ComboBoxTree({
					id:'oaType',
					width : 120, 
					value:{id:'0',text:'全部',attributes:{description:'oaType'}},
					tree : {
						xtype : 'treepanel', 
						loader : new Ext.tree.TreeLoader({
							dataUrl : ''
						}),
						root : new Ext.tree.AsyncTreeNode({
							id : '0',
							text : '全部'
						} 
						)
					},
					selectNodeModel : 'all' 
				}),
			'-','全部专业:',new Ext.ux.ComboBoxTree({
					id:'type',
					width : 120, 
					value:{id:'0',text:'全部专业',attributes:{description:'type'}},
					tree : {
						xtype : 'treepanel', 
						loader : new Ext.tree.TreeLoader({
							dataUrl : ''
						}),
						root : new Ext.tree.AsyncTreeNode({
							id : '0',
							text : '全部专业'
						} 
						)
					},
					selectNodeModel : 'all' 
				}), '-',{
					id : 'query',
					text : "查询",
					handler : queryIt
				},{
					id : 'export',
					text : "导出",
					handler : exporIt
				}, {
					id : 'fildler',
					text : "过滤",
					handler : fildler
				}]
			})
			function exporIt(){
			
			}
			function fildler(){
			
			}
			function queryIt() {
				ds.load({
					params : {
						start : 0,
						limit : 30
					}
				});
			}
			
			/* 创建表格 */
			var grid = new Ext.grid.GridPanel({ 
				ds : ds, 
				cm : cm,
				autoScroll:true,
				autoWidth : true,
				autoHeight : true,  
				sm : sm,
				bbar : bbar,
				tbar : tbar,
				title : '缺陷列表',
				border : false
			});
			
			//////////////////////////////缺陷类别
			var failSM = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true,
				listeners : {
					rowselect : function(sm, row, rec) {   
						var currentfailType = rec.data.roleName; 
						ds.load({
								params : {
									start : 0,
									limit : 30,
									likename : currentfailType
								}
						});
					}
				}
			});
		
			var failRecord = Ext.data.Record.create([{
				name : 'roleId'
			}, {
				name : 'roleName'
			}]);
			var failDS  = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
					url : 'system/findRole.action',
					method : 'post'
				}),
				reader : new Ext.data.JsonReader({
					totalProperty : 'data.total',
					root : 'data.rolesList'
				}, failRecord)
			});
			failDS.load({params : {
				start : 0,
				limit : 30,
				likename : ''
			}});
		
			var failCM = new Ext.grid.ColumnModel([failSM, {
				header : '类别编码',
				dataIndex : 'roleId', 
				width:50,
				align : 'left'
			}, {
				header : '类别名称',
				dataIndex : 'roleName',
				width : 250,
				align : 'left'
			}]);
//			failCM.defaultSortable = true;  
			var failCMGrid = new Ext.grid.GridPanel({ 
				ds : failDS, 
				cm : failCM,
				sm:failSM,
				autoScroll:true,
				autoWidth : true,
				autoHeight : true,
				fitToFrame : true, 
				title : '缺陷类别',
				border : false, 
				listeners : {
					render : function(g) {
						failCMGrid.getSelectionModel().selectRow(0);
					},
					delay : 10
				}
			}); 
			var viewport = new Ext.Viewport({
			layout : 'border',
			items : [{
				region : 'west',
				id : 'west-panel',
				split : true,
				width : 200,
				autoScroll:true,
				minSize : 175,
				layout : 'fit',
				maxSize : 400,
				margins : '0 0 0 0', 
				collapsible : true, 
				items : [failCMGrid]
			}, {
				containerScroll:true,
				region : 'center',
				id : 'center-panel',
				autoScroll:true,
				align:'center',
				layout : 'fit' ,
				items:[grid]
			} 
			]
		});
});