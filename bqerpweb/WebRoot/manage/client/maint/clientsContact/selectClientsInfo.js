Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var pageSize = 18;
	// 已存在的人员
	var str = '';
			
	var fuzzyText = new Ext.form.TextField({
		id : 'fuzzyText',
		name : 'fuzzyText',
		width : 180,
		emptyText : '合作伙伴编号/名称'
	})		

	var westbtnQuery = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : function() {
					westStore.load({
								params : {
									fuzzyText : fuzzyText.getValue(),
									start : 0,
									limit : pageSize
								}
							});
				}
			});

	var westSm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});

	var westStorelist = new Ext.data.Record.create([westSm, {
				name : 'cliendId'
			}, {
				name : 'typeId'
			}, {
				name : 'characterId'
			}, {
				name : 'importanceId'
			}, {
				name : 'tradeId'
			}, {
				name : 'clientCode'
			}, {
				name : 'clientName'
			}, {
				name : 'cliendChargeby'
			}, {
				name : 'clientOverview'
			}, {
				name : 'mainProducts'
			}, {
				name : 'mainPerformance'
			}, {
				name : 'address'
			}, {
				name : 'telephone'
			}, {
				name : 'zipcode'
			}, {
				name : 'email'
			}, {
				name : 'website'
			}, {
				name : 'fax'
			}, {
				name : 'legalRepresentative'
			}, {
				name : 'taxQualification'
			}, {
				name : 'bank'
			}, {
				name : 'account'
			}, {
				name : 'taxnumber'
			}, {
				name : 'regFunds'
			}, {
				name : 'regAddress'
			}, {
				name : 'memo'
			}, {
				name : 'approveFlag'
			}, {
				name : 'lastModifiedBy'
			}, {
				name : 'lastModifiedDate'
			}]);

	var westStore = new Ext.data.JsonStore({
				url : 'clients/getClientsInfoList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : westStorelist
			});

	// 左侧Grid
	var westgrid = new Ext.grid.GridPanel({
				store : westStore,
				columns : [new Ext.grid.RowNumberer(), {
							header : "合作伙伴编码",
							width : 90,
							sortable : false,
							dataIndex : 'clientCode',
							region : 'center'
						}, {
							header : "合作伙伴名称",
							width : 90,
							sortable : false,
							dataIndex : 'clientName',
							align : 'center'
						}, {
							header : "公司负责人",
							width : 80,
							sortable : false,
							dataIndex : 'cliendChargeby',
							align : 'center'
						}, {
							header : "状态",
							width : 80,
							sortable : false,
							dataIndex : 'approveFlag',
							align : 'center',
							renderer : function returnValue(val) {
								if(val == "1") {
									return '未启用';
								} else if(val == "2"){
									return '已启用';
								}
							}
						}],
				viewConfig : {
					forceFit : true
				},
				tbar : ['模糊查询 ：', fuzzyText, westbtnQuery],
				sm : westSm,
				bbar : new Ext.PagingToolbar({
							pageSize : pageSize,
							store : westStore,
							displayInfo : true,
							displayMsg : "{2}条记录",
							emptyMsg : "没有记录"
						}),
				frame : true,
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});
			
	westgrid.on("rowdblclick", updateRecord);		

	westStore.load({
				params : {
					start : 0,
					limit : pageSize
				}
			});
			
	function updateRecord() {
        if (westgrid.selModel.hasSelection()) {
            var records = westgrid.selModel.getSelections();
            var recordslen = records.length;
            if (recordslen > 1) {
                Ext.Msg.alert("系统提示信息", "请选择其中一项！");
            } else {
                var record = westgrid.getSelectionModel().getSelected();
                var ro = record.data;
				window.returnValue = ro;
				window.close();
            }
        } else {
           Ext.Msg.alert("提示", "请先选择一项!");
        }
    }		
	// ↑↑主窗口部件

	var layout = new Ext.Viewport({
				layout : "fit",
				border : false,
				items : [westgrid]
			});

});