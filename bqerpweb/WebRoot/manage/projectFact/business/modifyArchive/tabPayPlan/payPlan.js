Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var id = getParameter("id");
	var item = Ext.data.Record.create([{
		name : 'paymentId'
	}, {
		name : 'conId'
	}, {
		name : 'payStatu'
	}, {
		name : 'paymentMoment'
	}, {
		name : 'memo'
	}, {
		name : 'payPrice'
	}, {
		name : 'currencyName'
	}, {
		name : 'payDate'
	}, {
		name : 'lastModifiedBy'
	}, {
		name : 'lastModifyName'
	}, {
		name : 'lastModifiedDate'
	}, {
		name : 'actAmount'
	}, {
		name : 'currencyName'
	}, {
		name : 'payRate'
	}]);
	
	var item_cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
		header : '项次号',
		width : 50,
		align : 'center'
	}), {
		header : '付款状态',
		dataIndex : 'payStatu',
		align : 'center',
		renderer : statusname
	}, {
		header : '付款阶段',
		dataIndex : 'paymentMoment',
		align : 'center'
	}, {
		header : '付款说明',
		dataIndex : 'memo',
		align : 'center'
	}, {
		header : '付款金额',
		dataIndex : 'payPrice',
		align : 'center'
	}, {
		header : '付款比例',
		dataIndex : 'payRate',
		align : 'center'
	},{
		header : '币别',
		dataIndex : 'currencyName',
		align : 'center'
	}, {
		header : '计划付款日期',
		dataIndex : 'payDate',
		align : 'center'
	}]);
	item_cm.defaultSortable = true;
	function statusname(v) {
		if (v == 2) {
			return "已结算";
		}
		if (v == "" || v == null) {
			return "未付款";
		} else {
			return "付款中";
		}
	}
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managecontract/findPayPlanList.action'
		}),
		reader : new Ext.data.JsonReader({}, item)
	});
	if ((id != null ) && (id != "")) {
		ds.load({
			params : {
				conId : id
			}
		});
	}
	var payGrid = new Ext.grid.GridPanel({
		title:'付款计划',
		ds : ds,
		cm : item_cm,
		autoWidth : true,
		autoScroll : true,
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
			items : payGrid
		}]
	});
});