Ext.namespace('Equ.SpmaintChoose');
Equ.SpmaintChoose = function(config,kks){
	var btnConfirm = new Ext.Button({
		text : '确定', 
		id:'confirm',
		iconCls : 'confirm',
		handler : function() {
			var ro = getValue();
			if(ro != null)
				setValue(ro.get('woCode'), ro.get('workorderTitle'));
			win.hide();
		}
	});

	var kksCode = (kks == null) ? 0 : kks;

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
	 name : 'standardInfo.professionCode'
	},{
	 name : 'standardInfo.filepackageCode'
	},{
	 name : 'standardInfo.repairModel'
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
	name : 'professionName'
	},{
	name : 'repairmodelName'
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

	ds.baseParams = {
		kksCode : kksCode
	}
	ds.load({
				params : {
					start : 0,
					limit : 18
				}
			});
			
			var westsm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true

			});



	var toolbar = new Ext.Toolbar({
				items : [{
							text : 'KKS编码/工作指令模糊查询',
							xtype : 'label'
						}, {
							name : 'queryKey',
							width : '70pt',
							xtype : 'textfield',
							id : 'queryKey'
						}, {
							text : '查询',
							iconCls : 'query',
							xtype : 'button',
							handler : function() {
								ds.baseParams = {
									queryKey : Ext.get("queryKey").dom.value,
									kksCode : kksCode
								}
								ds.load({
											params : {
												start : 0,
												limit : 18
											}
										});

							}
						}, btnConfirm, '-', {
							text : '取消',
							iconCls : 'cancer',
							handler : function() {
								win.hide();
							}
						}]
			});
	

	var westgrid = new Ext.grid.GridPanel({
				ds : ds,
				columns : [westsm, new Ext.grid.RowNumberer(),{
							header : '标准工作包编码',
							hidden : false,
							dataIndex : 'standardInfo.woCode'
						},{
							header : "KKS编码",
							width : 100,
							sortable : true,
							dataIndex : 'kksCode'
						
						},{
							header : '设备名称',
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
				tbar : toolbar,
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
			
//	westgrid.on("rowdblclick", function() {
//			chooseStdpackage();
//		});
//		
//	function chooseStdpackage(){
//		var record = westgrid.getSelectionModel().getSelected();
//			if (typeof(record) != "object") {
//				Ext.Msg.alert("提示", "请选择标准包!");
//				return false;
//			}
//			var object = new Object();
//			object = {
//			stdwoCode :record.get('standardInfo.woCode'),
//			professionCode : record.get('standardInfo.professionCode'),
//			repairModel : record.get('standardInfo.repairModel'),
//			professionName : record.data.professionName,
//			repairmodelName : record.data.repairmodelName,
//			kksCode : record.data.kksCode,
//			equName : record.data.equName
//			};
//			//alert(record.get('standardInfo.professionCode'));
//			window.returnValue = object;
//			window.close();	
//			
//		}	
	var win = new Ext.Window({
				id : 'win',
				height : 350,
				pageX : 10,
				pageY : 10,
				// width : 830,
				title : '标准工作包选择',
				layout : 'border',
				modal : true,
				closeAction : 'hide',
				items : [{
							title : "",
							region : 'center',
							layout : 'fit',
							// border : false,
							// margins : '0',
							// height : 200,
							// split : false,
							// collapsible : false,
							items : [westgrid]
						}
				// {
				// title : "标准包列表",
				// region : 'west',
				// layout : 'fit',
				// border : false,
				// margins : '1 0 1 1',
				// split : true,
				// collapsible : true,
				// width : 350,
				// items : [westgrid]
				//
				// }, {
				// title : "单选左侧列表中记录显示",
				// id : "titlepanel",
				// region : 'center',
				// layout : 'fit',
				// border : false,
				// margins : '1 0 -3 0',
				//							split : true,
				//							collapsible : true,
				//							items : [eastab]
				//						}
				]
			})
	

	var cbrecord = new Ext.data.Record.create([{
		name : 'woCode'
	},{
		name : 'workorderTitle'
	}])
	var cbStore = new Ext.data.Store({
		reader : new Ext.data.JsonReader({},datalist)
	})
	var combo = new Ext.form.ComboBox({
		fieldLabel : '标准包',
		store : cbStore,
		id : 'abc',
		mode : 'local',
		width: 120,
//		valueField : 'standardInfo.woCode',
//		displayField : 'standardInfo.workorderTitle',
		valueField : 'woCode',
		displayField : 'workorderTitle',
		editable : true,
		triggerAction : 'all',
		forceSelection:true,
		readOnly : true, 
		listeners:(config&&config.listeners)?config.listeners:{},
		onTriggerClick : function() {
			win.show();
		}
	});
	
	Ext.apply(combo, config);
	
	function setValue(woCode, workorderTitle) {
		var d1 = new cbrecord({
			'woCode' : woCode,
			'workorderTitle' : workorderTitle
		});
		cbStore.removeAll();
		cbStore.add(d1);
		combo.setValue(woCode);
	}
	function getValue() {
		var record = westgrid.getSelectionModel().getSelected();
		if (typeof(record) != "undefined") {
			var newcbRec = new cbrecord({
				'woCode' : null,
				'workorderTitle' : null
			});
			newcbRec.set('woCode',record.get('standardInfo.woCode'))
			newcbRec.set('workorderTitle',record.get('standardInfo.workorderTitle'))
			return newcbRec;
		} else {
			return null;
		}
	}
	
	this.combo = combo;
	this.win = win;
	this.setValue = setValue;
	this.getValue = getValue;
	this.btnConfirm = btnConfirm;



}