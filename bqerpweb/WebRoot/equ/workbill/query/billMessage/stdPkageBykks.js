Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	Ext.QuickTips.init();

	function getParameter(psName) {
		var url = self.location;
		var result = "";
		var str = self.location.search.substring(0);
		if (str.indexOf(psName) != -1
				&& (str.substr(str.indexOf(psName) - 1, 1) == "?" || str
						.substr(str.indexOf(psName) - 1, 1) == "&")) {
			if (str.substring(str.indexOf(psName), str.length).indexOf("&") != -1) {
				var Test = str.substring(str.indexOf(psName), str.length);
				result = Test.substr(Test.indexOf(psName), Test.indexOf("&")
								- Test.indexOf(psName));
				result = result.substring(result.indexOf("=") + 1,
						result.length);
			} else {
				result = str.substring(str.indexOf(psName), str.length);
				result = result.substring(result.indexOf("=") + 1,
						result.length);
			}
		}
		return result;
	}

	var kksCode = getParameter("kksCode");
//	alert(kksCode)
//var	kksCode=20;

	/** 弹窗元素↓↓* */
//	var westgrids = new Ext.data.JsonStore({
//				url : 'workbill/findBystdPKage.action',
//				root : 'list',
//				totalProperty : 'totalCount',
//				fields : datalist
//			});
	// 左边列表中的数据
	var datalist = new Ext.data.Record.create([
	{	name : 'standardInfo.woId'
	},{
		 name : 'standardInfo.woCode'
	},{
		name : 'standardInfo.jobCode'
	}, {
		name : 'standardInfo.workorderTitle'
	},{
	 name : 'standardInfo.workorderMemo'
	},{
	 name : 'standardInfo.crewId'
	},{
	 name : 'standardInfo.maintDep'
	},{
	 name : 'standardInfo.repairModel'
	},{
	 name : 'standardInfo.repairmethodCode'
	},{
	 name : 'standardInfo.filepackageCode'
	},{
	 name : 'standardInfo.relationFilepackageMemo'
	},{
	 name : 'standardInfo.ifWorkticket'
	},{
	 name : 'standardInfo.equCode'
	},{
	 name : 'standardInfo.kksCode'
	},{
	 name : 'standardInfo.equName'
	},{
	 name : 'standardInfo.equPostionCode'
	},{
	 name : 'standardInfo.remark'
	},{
	 name : 'standardInfo.ifOutside'
	},{
	 name : 'standardInfo.ifFireticket'
	},{
	 name : 'standardInfo.ifMaterials'
	},{
	 name : 'standardInfo.ifReport'
	},{
	 name : 'standardInfo.ifConform'
	},{
	 name : 'standardInfo.ifRemove'
	},{
	 name : 'standardInfo.ifCrane'
	},{
	 name : 'standardInfo.ifFalsework'
	},{
	 name : 'standardInfo.planWotime'
	},{
	 name : 'standardInfo.planWofee'
	},{
	 name : 'standardInfo.orderby'
	},{
	 name : 'standardInfo.status'
	},{
	 name : 'standardInfo.enterprisecode'
	},{
	 name : 'standardInfo.ifUse'
	},{
	name : 'kksCode'
	},{
	name : 'equName'}
]);
	
	var ds = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
				url : 'workbill/findBystdPKage.action',
				method : 'post'
			}),
			reader : new Ext.data.JsonReader({
				totalProperty : "totalProperty",
				root : "root"
			}, datalist)
});

	ds.load({
				params : {
					start : 0,
					limit : 18,
					kksCode : kksCode
				}
			});
			
			var westsm = new Ext.grid.CheckboxSelectionModel({
			singleSelect:true
			
		}
);
			
		var westbtnAdd = new Ext.Button({
		text : '确定',
		iconCls : 'add',
		handler : chooseStdpackage

	});		
	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
				ds : ds,
				columns : [westsm, new Ext.grid.RowNumberer(),{
							hidden : true,
							sortable : false,
							dataIndex : 'woCode'
						},{
							header : "KKS编码",
							width : 100,
							sortable : true,
							dataIndex : 'kksCode'
						
						},{
							hidden : true,
							sortable : false,
							dataIndex : 'equName'
						},{
							header : "标准工作指令",
							width : 100,
							sortable : true,
							dataIndex : 'standardInfo.jobCode'
							
						}, {header : "标准工作名称",
							width : 180,
							sortable : false,
							dataIndex : 'standardInfo.workorderTitle'}],
				tbar : [westbtnAdd],
				sm : westsm,
				frame : true,
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : ds,
							displayInfo : true,
							displayMsg : "{0} 到 {1} /共 {2} 条",
							emptyMsg : "没有记录"
						}),
				border : true,
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});
			
	westgrid.on("rowdblclick", function() {
			chooseStdpackage();
		});
		
	function chooseStdpackage(){
		var record = westgrid.getSelectionModel().getSelected();
			if (typeof(record) != "object") {
				Ext.Msg.alert("提示", "请选择标准包!");
				return false;
			}
			var object = new Object();
			object = {
			stdwoCode :record.get('standardInfo.woCode'),
			kksCode : record.data.kksCode,
			equName : record.data.equName
			};
			window.returnValue = object;
			window.close();	
			
		}	

	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : 'border',
				border : false,
				items : [{
							title : "",
							region : 'center',
							layout : 'fit',
//							border : false,
//							margins : '0',
							// height : 200,
//							split : false,
//							collapsible : false,
							items : [westgrid]
						}]
			});
})