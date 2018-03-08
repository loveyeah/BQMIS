Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	// 继电保护装置名称
	var fuzzyText = new Ext.form.TextField({
		id : 'fuzzyText',
		name : 'fuzzyText',
		width : 120,
		emptyText : '继电保护装置名称'
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
				name : 'ptjd.deviceId'
			}, {
				name : 'ptjd.protectedDeviceId'
			}, {
				name : 'ptjd.equCode'
			}, {
				name : 'ptjd.voltage'
			}, {
				name : 'ptjd.deviceType'
			}, {
				name : 'ptjd.sizeType'
			}, {
				name : 'ptjd.sizes'
			}, {
				name : 'ptjd.manufacturer'
			}, {
				name : 'ptjd.outFactoryDate'
			}, {
				name : 'ptjd.outFactoryNo'
			}, {
				name : 'ptjd.installPlace'
			}
			, {
				name : 'ptjd.testCycle'
			}
			, {
				name : 'ptjd.chargeBy'
			}, {
				name : 'ptjd.memo'
			}, {
				name : 'equName'
			}, {
				name : 'protectedDeviceName'
			}
			,
			{
				name : 'outFactoryDate'
			}, {
				name : 'chargeName'
			}]);

	var westgrids = new Ext.data.JsonStore({
				url : 'productionrec/findProtectEquList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});

	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
				autoScroll : true,
				ds : westgrids,
				columns : [westsm, new Ext.grid.RowNumberer({
									header : "行号",
									width : 31
								}), {
							header : "装置编号",
							width : 120,
							align : "center",
							hidden : true,
							sortable : true,
							dataIndex : 'ptjd.deviceId'
						}, {
							header : "装置名称",
							width : 260,
							align : "center",
							sortable : true,
							dataIndex : 'equName'
						}, {
							header : "被保护设备",
							width : 260,
							align : "center",
							sortable : true,
							dataIndex : 'protectedDeviceName'
						}],
				tbar : ['模糊查询 ：', fuzzyText, queryButton,
				{ text:'确定',
				  iconCls:'ok',
				  handler:selectRecord
				},
				{
				 text:'取消',
				 iconCls:'cancer',
				 handler:cancelHandler
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
			
		westgrids.load({
				params : {
					start : 0,
					limit : 18,
					name : fuzzyText.getValue()
				}
			});
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
		         client.deviceId=record.get("ptjd.deviceId");
		         client.deviceCode=record.get("ptjd.equCode");
		         client.deviceName=record.get("equName");
		         client.protectedDeviceId=record.get("ptjd.protectedDeviceId");
		         client.protectedDeviceName=record.get("protectedDeviceName");
		         window.returnValue=client;
		         window.close();

			}
		} else {
			Ext.Msg.alert("提示", "请先选择继电保护装置名称!");
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
				items : [{
							region : 'center',
							layout : 'fit',
							border : false,
							margins : '1 0 1 1',
							collapsible : true,
							items : [westgrid]

						}]
			});
});