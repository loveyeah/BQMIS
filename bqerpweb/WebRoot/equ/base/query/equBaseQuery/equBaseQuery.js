Ext.onReady(function() {
	//	// -----------定义tree-----------------
	var root = new Ext.tree.AsyncTreeNode({
		id : "root",
		text : "设备名称"
	});

	var mytree = new Ext.tree.TreePanel({
		renderTo : "treepanel",
		height : 900,
		root : root,
		requestMethod : 'GET',
		loader : new Ext.tree.TreeLoader({
			url : "equ/getEquTreeList.action",
			baseParams : {
				id : 'root'
			}
		})
	});
	mytree.on("click", clickTree, this);
		mytree.on('beforeload', function(node) {
		mytree.loader.dataUrl = 'equ/getEquTreeList.action?id=' + node.id;
	}, this);
	 root.expand();
	function clickTree(node) {
		if(node.id=="root")
		{
			 myform.setVisible(false);
		}
		else
		{
		   myform.setVisible(true);
		   myform.getForm().reset();
		   query();
		   Ext.Ajax.request({
						url : 'equ/findBaseInfoByAttributeCode.action',
						params : {
							code : node.id
						},
						method : 'post',
						success : function(action) {
						var o = eval("(" + action.responseText + ")");
						EquCode.setValue(node.id);
						EquName.setValue(node.text);
						if(o.success==true){
							var data  = eval("(" + o.data + ")");
							if(data.equBaseId!=null){
								equBaseId.setValue(data.equBaseId);
							}
							if(data.manufacturer!=null){
								manuFacturer.setValue(data.manufacturer);
							}
							if(data.model!=null){
								model.setValue(data.model);
							}
							if(data.factoryDate!=null){
								factoryDate.setValue(data.factoryDate.toString().substring(0,10));
							}
							if(data.installationDate!=null){
								installationDate.setValue(data.installationDate.toString().substring(0,10));
							}
							if(o.installationDesc!=null){
								installationCode.setValue(o.installationDesc);
							}
							if(data.price!=null){
								price.setValue(data.price);
							}
							if(data.useYear!=null){
								useYear.setValue(data.useYear);
							}
							if(data.assetCode!=null){
								assentCode.setValue(data.assetCode);
							}
							if(data.technicalParameters!=null){
								technicalParameters.setValue(data.technicalParameters);
							}
							if(o.oneResponsibleName!=null){
								oneResponsible.setValue(o.oneResponsibleName);
							}
							if(o.twoResponsibleName!=null){
								twoResponsible.setValue(o.twoResponsibleName);
							}
							if(o.threeResponsibleName!=null){
								threeResponsible.setValue(o.threeResponsibleName);
							}
							if(o.oneDeptName!=null&&o.oneDeptName!='null'){
								oneDept.setValue(o.oneDeptName);
							}
							if(o.twoDeptName!=null&&o.twoDeptName!='null'){
								twoDept.setValue(o.twoDeptName);
							}
							if(o.threeDeptName!=null&&o.threeDeptName!='null'){
								threeDept.setValue(o.threeDeptName);
							}
							query();
						}else{
							
						}
						maintenanceQuery.setDisabled(false);
						changeQuery.setDisabled(false);
					},
					failure : function(result,
							request) {
						Ext.MessageBox.alert('错误',
								'操作失败,请联系管理员!');
					}
				});
		myform.setTitle("["+node.text+"]基本信息");
		}
	}
	//设备基础信------------------------------------------------------------------------
	//设备基础信息Id
	var equBaseId=new Ext.form.Hidden({
				name:'baseInfo.equBaseId'
	});
	//设备编码
	var EquCode=new Ext.form.TextField({
				fieldLabel : '设备编码',
				name : 'baseInfo.attributeCode',
				allowBlank:true,
				readOnly:true,
				anchor : "90%"
	});
	//设备名称
	var EquName=new Ext.form.TextField({
				fieldLabel : '设备名称',
				name : 'EquName',
				allowBlank:true,
				readOnly:true,
				anchor : "90%"
	});
	//制造厂家
	var manuFacturer=new Ext.form.TextField({
				fieldLabel : '制造厂家',
				name : 'baseInfo.manufacturer',
				allowBlank:true,
				readOnly:true,
				anchor : "90%"
	});
	//设备规格
	var model=new Ext.form.TextField({
				fieldLabel : '设备规格',
				name : 'baseInfo.model',
				allowBlank:true,
				readOnly:true,
				anchor : "90%"
	});
	// 出厂日期
	var factoryDate= new Ext.form.TextField({
				fieldLabel : "出厂日期",
				allowBlank:true,
				readOnly:true,
				anchor : "90%"
	});
	// 安装日期
	var installationDate= new Ext.form.TextField({
				fieldLabel : "安装日期",
				allowBlank:true,
				readOnly:true,
				anchor : "90%"
			});
	//安装位置
	var installationCode = new Ext.form.TextField({
				fieldLabel : "安装位置",
				allowBlank:true,
				readOnly:true,
				anchor : "90%"
			});
	//单价
	var price=new Ext.form.NumberField({
				fieldLabel : '单价',
				name : 'baseInfo.price',
				allowBlank:true,
				readOnly:true,
				anchor : "90%"
	});
	//使用年限（年）
	var useYear=new Ext.form.NumberField({
				fieldLabel : '使用年限（年）',
				name : 'baseInfo.useYear',
				allowBlank:true,
				readOnly:true,
				anchor : "90%"
	});
	//设备资产编号
	var assentCode=new Ext.form.TextField({
				fieldLabel : '设备资产编号',
				name : 'baseInfo.assetCode',
				allowBlank:true,
				readOnly:true,
				anchor : "90%"
	});
	//主要技术参数
	var technicalParameters=new Ext.form.TextArea({
				fieldLabel : '主要技术参数',
				name : 'baseInfo.technicalParameters',
				allowBlank:true,
				readOnly:true,
				anchor : "95%"
	});
	//一级责任部门
	var oneDept=new Ext.form.TextField({
				fieldLabel : '一级责任部门',
				allowBlank:true,
				readOnly:true,
				anchor : "90%"
	});
	//二级责任部门
	var twoDept=new Ext.form.TextField({
				fieldLabel : '二级责任部门',
				allowBlank:true,
				readOnly:true,
				anchor : "90%"
	});
	//三级责任部门
	var threeDept=new Ext.form.TextField({
				fieldLabel : '三级责任部门',
				allowBlank:true,
				readOnly:true,
				anchor : "90%"
	});
	//一级责任人
	var oneResponsible = new Ext.form.TextField({
				fieldLabel : '一级责任人',
				allowBlank:true,
				readOnly:true,
				anchor : "90%"
	});
	//二级责任人
	var twoResponsible = new Ext.form.TextField({
				fieldLabel : '二级责任人',
				allowBlank:true,
				readOnly:true,
				anchor : "90%"
	});
	//三级责任人
	var threeResponsible = new Ext.form.TextField({
				fieldLabel : '三级责任人',
				allowBlank:true,
				readOnly:true,
				anchor : "90%"
	});
	
	//附件信息----------------------------------------------------------
	var annex_item = Ext.data.Record.create([{
				name : 'annexId',
				mapping:0
			}, {
				name : 'annexName',
				mapping:1
			}, {
				name : 'annex',
				mapping:2
			}]);
	var annex_item_cm = new Ext.grid.ColumnModel([
			new Ext.grid.RowNumberer({
						header : '行号',
						width : 35,
						align : 'center'
					}), {
				header : '附件名称',
				dataIndex : 'annexName',
				width : 300,
				align : 'center'
			}, {
				header : '查看',
				dataIndex : 'annex',
				width : 300,
				align : 'center',
				renderer : function(v) {
						if(v !=null && v !='')
										{ 
											var s =  '<a href="#" onclick="window.open(\''+v+'\');return  false;">[查看]</a>';
											return s;
										}else{
											return '没有附件';
										}
				}
			}]);
	annex_item_cm.defaultSortable = true;
	var annex_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'equ/findBaseAnnexByBaseId.action'
						}),
				reader : new Ext.data.JsonReader({
						root : "list",
						totalProperty : "totalCount"
				}, annex_item)
			});

	var annexGrid = new Ext.grid.GridPanel({
				ds : annex_ds,
				cm : annex_item_cm,
				split : true,
				// autoHeight:true,
				height : 270,
				autoScroll : true,
				// collapsible : true,
				border : false
//				,viewConfig : {
//					forceFit : true
//				}
			});
	function query(){
	annex_ds.load({
					params : {
						equBaseId : equBaseId.getValue()
					}
				})
	};
	function querymaintenance(){
		var maintenance=new Power.maintenanceQuery();
		maintenance.setValue(EquCode.getValue(),EquName.getValue());
		maintenance.query();
		maintenance.win.show();
	}
	function querychange(){
		var change=new Power.changeQuery();
		change.setValue(EquCode.getValue(),EquName.getValue());
		change.query();
		change.win.show();
	}
	var maintenanceQuery=new Ext.Button({
			text : "查看设备检修台帐",
			iconCls : 'query',
			handler : querymaintenance
	});
	maintenanceQuery.setDisabled(true);
	var changeQuery=new Ext.Button({
			text : "查看设备异动台帐",
			iconCls : 'query',
			handler : querychange
	});
	changeQuery.setDisabled(true);
	var formtbar=new Ext.Toolbar({
			items:[maintenanceQuery,'-',changeQuery]
	});
	var myform = new Ext.FormPanel({
		frame : true,
		labelAlign : 'left',
		tbar : formtbar,
		title : '设备基础信息维护',
		items:[
		new Ext.form.FieldSet({
			title : '设备基础信息',
			collapsible : true,
			height : '100%',
			items :[
				{
				layout : 'column',
				items : [{
					columnWidth : .5,
					layout : 'form',
					border : false,
					labelWidth : 100,
					items : [EquCode, equBaseId,manuFacturer,factoryDate,installationCode,useYear]
				}, {
					columnWidth : .5,
					layout : 'form',
					border : false,
					labelWidth : 100,
					items : [EquName,model,installationDate,price,assentCode]
				}
				,{
				 columnWidth :1,
				 layout : 'form',
				 border : false,
				 labelWidth : 100,
				 items : [technicalParameters]
				 },{
				 columnWidth : .5,
				 layout : 'form',
				 border : false,
				 labelWidth : 100,
				 items : [oneDept,twoDept,threeDept]
				 },{
				 columnWidth : .5,
				 layout : 'form',
				 border : false,
				 labelWidth : 100,
				 items : [oneResponsible,twoResponsible,threeResponsible]
				 }]           
				}
			]})
			,
			new Ext.form.FieldSet({
			title : '设备附件信息',
			collapsible : true,
			height : '20%',
			layout : 'form',
			items :[annexGrid]})
			]});
	var right = new Ext.Panel({
		region : "center",
		layout : 'fit',
		collapsible : true,
		items : [myform]
	});
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [{
			region : 'west',
			split : true,
			title : "设备树",
			width : 250,
			layout : 'fit',
			minSize : 175,
			maxSize : 600,
			margins : '0 0 0 0',
			collapsible : true,
			border : false,
			autoScroll : true,
			items : [mytree]
		}, right]
	});
})