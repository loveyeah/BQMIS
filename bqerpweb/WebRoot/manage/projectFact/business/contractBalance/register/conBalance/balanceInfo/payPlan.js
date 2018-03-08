Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
function getDate() {
	var d, s, t;
	d = new Date();
	s = d.getFullYear().toString(10) + "-";
	t = d.getMonth() + 1;
	s += (t > 9 ? "" : "0") + t + "-";
	t = d.getDate();
	s += (t > 9 ? "" : "0") + t + " ";
	t = d.getHours();
	s += (t > 9 ? "" : "0") + t + ":";
	t = d.getMinutes();
	s += (t > 9 ? "" : "0") + t + ":";
	t = d.getSeconds();
	s += (t > 9 ? "" : "0") + t;
	return s;
}
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var contractId = getParameter("conId");
	var tbar = new Ext.Toolbar({
		items : [{
			id : 'confer',
			text : "确定",
//			iconCls : 'add',
			handler : getPayPlan
		}, '-', {
			id : 'close',
			text : "关闭",
//			iconCls : 'update',
			handler : function() {
				window.close();
			}
		}]
	});
   
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
	},{
		name : 'operateName'
	},{
		name : 'operateBy'
	}]);
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			rowselect : function(sm, row, rec) {
				
			}
		}
	});
	var item_cm = new Ext.grid.ColumnModel([sm, new Ext.grid.RowNumberer({
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
	if (id != null) {
		ds.load({
			params : {
				conId : contractId
			}
		});
	}
	var payGrid = new Ext.grid.GridPanel({
		ds : ds,
		cm : item_cm,
		sm : sm,
		autoWidth : true,
		autoScroll : true,
		tbar : tbar,
		border : false
	});
	 function getPayPlan(){
    	var selrows = payGrid.getSelectionModel().getSelections();
    	if(selrows.length==1){
    		if(selrows[0].data.payStatu=="" || selrows[0].data.payStatu==null){
    			var o=new Object({
    				pay : selrows[0].data.payPrice,
    				paymentId : selrows[0].data.paymentId,
    				operateBy : selrows[0].data.operateBy,
    				operateName : selrows[0].data.operateName,
    				currencyName : selrows[0].data.currencyName
    			})
    			window.returnValue = o;
				window.close();
    		}
    		else
    			Ext.Msg.alert('提示信息','请选择未付款阶段！')		
    	}
    	else
    		Ext.Msg.alert('提示信息','请选择一行');
    }
	payGrid.on('rowdblclick', function(grid, rowIndex, e) {
		getPayPlan();
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