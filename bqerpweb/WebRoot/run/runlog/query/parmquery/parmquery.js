Ext.onReady(function() {
	var specialcode = parent.document.getElementById("specialityCode").value;
	var fromdate = parent.document.getElementById("fromDate").value;
	var todate = parent.document.getElementById("toDate").value;
	Ext.Ajax.request({
		url : 'runlog/queryRunlogParm.action',
		params : {
			specialcode : specialcode,
			formdate : fromdate,
			todate : todate
		},
		method : 'post',
		waitMsg : '正在加载数据...',
		success : function(result, request) {
			if (result.responseText != "") {
				var responseArray = Ext.util.JSON.decode(result.responseText);
				if (responseArray.success == true) {
					var o = eval('(' + result.responseText + ')');
					var json = o.json;
					var cm = new Ext.grid.ColumnModel({
						columns : json.columModle,
						defaultSortable : false
					});

					var ds = new Ext.data.JsonStore({
						data : json.data,
						fields : json.fieldsNames
					});
					var grid = new Ext.grid.GridPanel({
						el : 'orgGrid',
						split : true,
						border : false,
						cm : cm,
						ds : ds,
						width : Ext.get("orgGrid").getWidth(),
						enableColumnMove : false
					});
					// grid.render();
					new Ext.Viewport({
						layout : 'fit',
						split : true,
						items : [grid]
					});
				} else {
					var o = eval('(' + result.responseText + ')');
					Ext.MessageBox.show({
						title : '错误',
						msg : o.errorMsg,
						buttons : Ext.Msg.OK,
						icon : Ext.MessageBox.ERROR
					});
				}
			}
		},
		failure : function(result, request) {
			var o = eval('(' + result.responseText + ')');
			Ext.MessageBox.show({
				title : '错误',
				msg : "服务器发生异常...",
				buttons : Ext.Msg.OK,
				icon : Ext.MessageBox.ERROR
			});
		}
	});
	setTimeout(function() {
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut({
			remove : true
		});
	}, 250);
})
function changeColor(value) {
	return "<span style='color:blue;'>" + value + "</span>";
}