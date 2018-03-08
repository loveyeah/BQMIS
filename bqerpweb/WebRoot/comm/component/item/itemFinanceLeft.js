
/**
 * 
 * @param {} itemId 费用来源ID
 * @param {} budgetTime 费用来源日期
 */
 function itemFinanceLeft(itemId,budgetTime){	
 	//异步调用
//				Ext.Ajax.request({
//					 method:'POST',
//					 async :  false,
//					 url:'managebudget/getCurrentItemFinanceLeft.action',
//					 params:{
//					 	itemId : itemId,
//					 	budgetTime : budgetTime
//					 },
//					 success:function(form,action){
//					 	var result = eval("(" + form.responseText
//										+ ")");
//						return result.finance;
//					 }
//				});
	//同步调用	
	var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", 'managebudget/getCurrentItemFinanceLeft.action?itemId='+ itemId+'&budgetTime='+budgetTime, false);
	conn.send(null);
	if (conn.status == "200") {
		var result = eval("(" + conn.responseText + ")");
		return result.finance;
	}
 }
