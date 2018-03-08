Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var contractNo = getParameter("contractNo");
	function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";
		// 初始化时间
		Year = DateIn.getYear();
		Month = DateIn.getMonth() + 1;
		Day = DateIn.getDate();
		hour = DateIn.getHours();
		min = DateIn.getMinutes();
		se = DateIn.getSeconds();
		CurrentDate = Year + "-";
		if (Month >= 10) {
			CurrentDate = CurrentDate + Month + "-";
		} else {
			CurrentDate = CurrentDate + "0" + Month + "-";
		}
		if (Day >= 10) {
			CurrentDate = CurrentDate + Day;
		} else {
			CurrentDate = CurrentDate + "0" + Day;
		}
		CurrentDate += " "
		if (hour >= 10) {
			CurrentDate = CurrentDate + hour + ":";
		} else {
			CurrentDate = CurrentDate + "0" + hour + ":";
		}
		if (min >= 10) {
			CurrentDate = CurrentDate + min + ":";
		} else {
			CurrentDate = CurrentDate + "0" + min + ":";
		}
		if (se >= 10) {
			CurrentDate = CurrentDate + se + ":";
		} else {
			CurrentDate = CurrentDate + "0" + se + ":";
		}
		return CurrentDate;
	}

	 var Material_item = Ext.data.Record.create([{
		name : 'purNo'
	}, {
		name : 'materialId'
	}, {
		name : 'unitPrice'
	}, {
		name : 'purqty'
	}, {
		name : 'memo'
	}, {
		name : 'materialName'
	}, {
		name : 'specModel'
	}, {
		name : 'total'
	}]);
	var Material_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});
	var Material_item_cm = new Ext.grid.ColumnModel([Material_sm,
			new Ext.grid.RowNumberer({
				header : '项次号',
				width : 50,
				align : 'center'
			}), {
				header : '采购单号',
				dataIndex : 'purNo',
				align : 'center'
			}, {
				header : '物资名称',
				dataIndex : 'materialId',
				hidden : true,
				align : 'center'
			}, {
				header : '物资名称',
				dataIndex : 'materialName',
				align : 'center'
			}, {
				header : '规格型号',
				dataIndex : 'specModel',
				align : 'center'
			}, {
				header : '单价',
				dataIndex : 'unitPrice',
				//width : 120,
				align : 'center'
			}, {
				header : '数量',
				dataIndex : 'purqty',
				align : 'center'
			}, {
				header : '总价',
				dataIndex : 'total',
				align : 'center'
			}, {
				header : '备注',
				dataIndex : 'memo',
				align : 'center'
			}]);
	Material_item_cm.defaultSortable = true;
	var Material_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managecontract/findAllMaterialByContractNo.action'
		}),
		reader : new Ext.data.JsonReader({root : "list"}, Material_item)
	});
	Material_ds.load({
		params : {
			contractNo : contractNo
		}
	})

	var MaterialGrid = new Ext.grid.GridPanel({
		ds : Material_ds,
		cm : Material_item_cm,
		sm : Material_sm,
		split : true,
		height : 150,
		width : Ext.get('div_lay').getWidth(),
		 //title : '物资明细',
		 collapsible : true,
		//tbar : Materialtbar,
		border : false
	});

	var layout = new Ext.Viewport({
		layout : 'fit',
		border : true,
		items : [{
			layout : 'fit',
			margins : '0 0 0 0',
			region : 'center',
			autoScroll : true,
			items : MaterialGrid
		}]
	});
});