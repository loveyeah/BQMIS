Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	// 被保护设备名称
	var fuzzyText = new Ext.form.TextField({
		id : 'fuzzyText',
		name : 'fuzzyText',
		width : 120,
		emptyText : '被保护设备名称'
	})	
	var queryButton = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : function() {
					westgrids.load({
								params : {
									name : fuzzyText.getValue(),
									start : 0,
									limit : 18
								}
							});
				}
			});
			
	var westsm = new Ext.grid.CheckboxSelectionModel();
	// 左边列表中的数据
	var datalist = new Ext.data.Record.create([

	{
				name : 'pjjb.protectedDeviceId'
			}, {
				name : 'pjjb.equCode'
			}, {
				name : 'pjjb.equLevel'
			}, {
				name : 'pjjb.voltage'
			}, {
				name : 'pjjb.installPlace'
			}, {
				name : 'pjjb.manufacturer'
			}, {
				name : 'pjjb.sizes'
			}, {
				name : 'pjjb.outFactoryNo'
			}, {
				name : 'pjjb.outFactoryDate'
			}, {
				name : 'pjjb.chargeMan'
			}, {
				name : 'pjjb.memo'
			}, {
				name : 'equName'
			}, {
				name : 'outFactoryDate'
			}, {
				name : 'chargeName'
			}]);

	var westgrids = new Ext.data.JsonStore({
				url : 'productionrec/findProtectedDeviceList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});

	westgrids.load({
				params : {
					start : 0,
					limit : 18,
					name : fuzzyText.getValue()
				}
			});

	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
				ds : westgrids,
				tbar : ['模糊查询 ：', fuzzyText, queryButton,
					{ 	text:'确定',
				 		 iconCls:'ok',
				  		handler:selectRecord
					},
					{
					 	text:'取消',
					 	iconCls:'cancer',
					 	handler:cancelHandler
					}],
				columns : [westsm, new Ext.grid.RowNumberer({
									header : "行号",
									width : 60
								}), {
							header : "被保护设备编号",
							width : 120,
							align : "center",
							sortable : true,
							hidden : true,
							dataIndex : 'pjjb.protectedDeviceId'
						}, {
							header : "设备名称",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'equName'
						}],
				sm : westsm,
				frame : true,
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : westgrids,
							displayInfo : true,
							displayMsg : "{0} 到 {1} /共 {2} 条",
							emptyMsg : "没有记录"
						}),
				border : true
			});

	// westgrid 的事件
	westgrid.on('rowdblclick',selectRecord);
	
	function selectRecord()
	{
		if (westgrid.selModel.hasSelection()) {
		
			var records = westgrid.getSelectionModel().getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项！");
			} else {
				 
				var record = westgrid.getSelectionModel().getSelected();
		         var client=new Object();
		         client.protectedDeviceId=record.get("pjjb.protectedDeviceId");
		         client.protectedDeviceName=record.get("equName");
		         window.returnValue=client;
		         window.close();

			}
		} else {
			Ext.Msg.alert("提示", "请先选择继电保护类型名称!");
		}
	}
	function cancelHandler(){
				var object = new Object();	
				window.returnValue = object;
				window.close();
			}
	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				autoScroll : true,
				items : [
					{
							region : 'center',
							layout : 'fit',
							border : false,
							margins : '1 0 1 1',
							autoScroll : true,
							items : [westgrid]
						}
						]
			});
});