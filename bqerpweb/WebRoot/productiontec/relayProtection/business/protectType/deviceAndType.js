Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	Ext.grid.CheckColumn = function(config) {
	    Ext.apply(this, config);
	    if (!this.id) {
	        this.id = Ext.id();
	    }
	    this.renderer = this.renderer.createDelegate(this);
	};
	Ext.grid.CheckColumn.prototype = {
	    init : function(grid) {
	        this.grid = grid;
	        this.grid.on('render', function() {
		            var view = this.grid.getView();
		            view.mainBody.on('mousedown', this.onMouseDown, this);
		        }, this);
	    },

	
	    onMouseDown : function(e, t) {
	        if (t.className && t.className.indexOf('x-grid3-cc-' + this.id) != -1) {
	            e.stopEvent();
	            var index = this.grid.getView().findRowIndex(t);
	            var record = this.grid.store.getAt(index);
	            if (record.data[this.dataIndex] == Constants.CHECKED_VALUE) {	            	
	                record.set(this.dataIndex, Constants.UNCHECKED_VALUE);
	            } else {
	                record.set(this.dataIndex, Constants.CHECKED_VALUE);
	            }
	        }
	    },
	    renderer : function(v, p, record) {
	        p.css += ' x-grid3-check-col-td';
	    	p.css += ' x-item-disabled';
	        return '<div class="x-grid3-check-col' + (v == 'Y' ? '-on' : '')
	                + ' x-grid3-cc-' + this.id + '">&#160;</div>';
	    }
	}
	
	
	
	var westsm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
		});
	// 左边列表中的数据
	var westdatalist = new Ext.data.Record.create([

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
				fields : westdatalist
			});

	westgrids.load({
				params : {
					start : 0,
					limit : 18
				}
			});
	
	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
				width : 430,
//				height : 510,
				autoScroll : true,
				ds : westgrids,
				columns : [westsm, new Ext.grid.RowNumberer({
									header : "行号",
									width : 31
								}), {
							header : "装置编号",
							width : 120,
							hidden : true,
							align : "center",
							sortable : true,
							dataIndex : 'ptjd.deviceId'
						}, {
							header : "装置名称",
							width : 260,
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

	westsm.on('rowselect',function(){
		if(westsm.hasSelection())
		{
			eastgrids.load({
				params : {
					start : 0,
					limit : 18,
					devId : westsm.getSelected().get('ptjd.deviceId')
				}
			})
		}		
	});
	
	
	var eastsm = new Ext.grid.CheckboxSelectionModel();
	// 右边列表中的数据
	var datalist = new Ext.data.Record.create([

	{
				name : 'protectTypeId'
			}, {
				name : 'protectTypeName'
			},{
				name : 'chooseFlag'
			}]);

	var eastgrids = new Ext.data.JsonStore({
				url : 'productionrec/findDeviceProList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});

	eastgrids.load({
				params : {
					start : 0,
					limit : 18
				}
			});

			
		//  显示为Checkbox
    var checkColumn = new Ext.grid.CheckColumn({
        header : "类型选择",
        dataIndex : 'chooseFlag',
        width : 80
    });
   
	// 右边列表
	var eastgrid = new Ext.grid.EditorGridPanel({
//				width : 300,
//				height : 510,
				plugins:[checkColumn],
				autoScroll : true,
				ds : eastgrids,
				columns : [ new Ext.grid.RowNumberer({
									header : "行号",
									width : 31
								}), {
							header : "继电保护类型编号",
							width : 110,
							align : "center",
							hidden : true,
							sortable : true,
							dataIndex : 'protectTypeId'
						}, {
							header : "继电保护类型名称",
							width : 200,
							align : "center",
							sortable : false,
							dataIndex : 'protectTypeName'
						}
						,checkColumn
						],
				sm : eastsm,
				frame : false,
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : eastgrids,
							displayInfo : true,
							displayMsg : "{0} 到 {1} /共 {2} 条",
							emptyMsg : "没有记录"
						}),
				border : true,
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid',
				clicksToEdit:1
			});

	var saveButton = new Ext.Button({
			text : '  保存  ',
			iconCls : 'save',
			handler : function(){
			   saveData();
			}
		})
		
		var headBar=new Ext.Toolbar({
			items:[saveButton]
		});

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [
		{
			region : 'north',
			layout : 'fit',
			items:[headBar]
		},
		{
			region:'west',
			layout : 'fit',
			width : 350,
			items:[westgrid]
		},
		{
			region:'center',
			layout : 'fit',
			items:[eastgrid]
		}
		]
	});

			
	function saveData()
	{
		if(!westsm.getSelected())
		{
			Ext.Msg.alert("提示信息","请选择要保存的装置!");
			return;
		}
		var str = "";
		for(var i= 0;i <= eastgrids.getTotalCount() - 1; i++)
		{
				if(eastgrids.getAt(i).get('chooseFlag') == Constants.CHECKED_VALUE)
				{	
					str += eastgrids.getAt(i).get(('protectTypeId')) + ',';
				}
		}
		if(str == "")
		{
			Ext.Msg.alert("提示信息","请选择装置的保护类型！");
			return;
		}
		str = str.substring(0,str.length - 1);
			
		Ext.Msg.confirm("提示信息","确认保存选中的数据",function(button,text)
		{
			if(button == "yes")
			{
				var devId = westsm.getSelected().get('ptjd.deviceId');	
				Ext.Ajax.request({
					waitMsg : '保存中，请稍候...',
					url : 'productionrec/saveDeviceAndType.action',
					params : {
						devId : devId,
						typeIds : str
					}
					,
					success : function(response,options)
					{
						Ext.Msg.alert("提示信息","数据保存成功！");
						westgrids.reload();
						eastgrids.load({
							params : {
							start : 0,
							limit : 18
							}
						});
					}
				});
				}
			})
		
			

	}

});