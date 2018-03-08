Ext.onReady(function() {
	//	// -----------定义tree-----------------
	var root = new Ext.tree.AsyncTreeNode({
		id : "root",
		text : "设备树"
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
		   myform.getForm().load({
			url : "equ/getBaseInfoByCode.action?code="+node.id,
			success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.get("PEquName").dom.value=o.pEquName;
						
						if(o.data.installdate!=null)
							{
								var myinstalldate=o.data.installdate;
								myinstalldate= myinstalldate.substring(0,myinstalldate.indexOf('T'));
								Ext.get("installdate").dom.value=myinstalldate;
							}
							
							if(o.data.warrantyexpdate!=null)
							{
								var mywarrantyexpdate=o.data.warrantyexpdate;
								mywarrantyexpdate= mywarrantyexpdate.substring(0,mywarrantyexpdate.indexOf('T'));
								Ext.get("warrantyexpdate").dom.value=mywarrantyexpdate;
							}
							if(o.data.changeDate!=null)
							{
								var mychangeDate=o.data.changeDate;
								mychangeDate=  mychangeDate.substring(0,mychangeDate.indexOf('T'))+" "+mychangeDate.substring(mychangeDate.indexOf('T')+1,mychangeDate.length);
								Ext.get("changeDate").dom.value=mychangeDate;
							}
							if(o.data.belongTeam!=null)
							{
								
							 //  Ext.getCmp('belongTeam').dom.setValue(o.data.belongTeam);
								belongTeam.setValue(o.data.belongTeam);
	                           Ext.form.ComboBox.superclass.setValue.call(Ext.getCmp('belongTeam'),o.deptName);
							}
					}
					
		});
		myform.setTitle("<<"+node.text+">>基本信息");
		}
	}
	
	var PEquName = {
		id : "PEquName",
		xtype : "textfield",
		fieldLabel : '名称',
		readOnly : true,
		name : 'PEquName',
		anchor : "90%"
	}
	var attributeCode = {
		id : "attributeCode",
		xtype : "textfield",
		fieldLabel : '设备功能码',
		readOnly : true,
		name : 'equ.attributeCode',
		anchor : "90%"
	}
	
	
		var PAttributeCode = {
		id : "PAttributeCode",
		xtype : "textfield",
		fieldLabel : '父编码',
		readOnly : true,
		name : 'equ.PAttributeCode',
		anchor : "90%"
	}
		var equName = {
		id : "equName",
		xtype : "textfield",
		fieldLabel : '设备名称',
		readOnly : true,
		allowBlank : false,
		name : 'equ.equName',
		anchor : "90%"
	}
		var assetnum = {
		id : "assetnum",
		xtype : "textfield",
		fieldLabel : '设备物资编码',
		allowBlank : false,
		name : 'equ.assetnum',
		anchor : "90%"
	}
	
		var assetType = {
		id : "assetType",
		xtype : "textfield",
		fieldLabel : '设备型号',
		allowBlank : false,
		name : 'equ.assetType',
		anchor : "90%"
	}
	
	
//		var materialCode = {
//		id : "materialCode",
//		xtype : "textfield",
//		fieldLabel : '周转设备物资编码',
//		name : 'equ.materialCode'
//	}
//		var binnum = {
//		id : "binnum",
//		xtype : "textfield",
//		fieldLabel : '周转设备所在货架',
//		name : 'equ.binnum'
//	}
	
//	var classsStructureId = {
//		id : "classsStructureId",
//		xtype : "numberfield",
//		fieldLabel : '技术规范分类编号',
//		name : 'equ.classsStructureId',
//		anchor : "90%"
//	}
	
		var priority = {
		id : "priority",
		xtype : "numberfield",
		fieldLabel : '设备优先级',
		name : 'equ.priority',
		anchor : "90%"
	}
//	var estimationLevel = {
//		id : "estimationLevel",
//		xtype : "textfield",
//		fieldLabel : ' 设备评定级别',
//		name : 'equ.estimationLevel',
//		anchor : "90%"
//	}
	
	
	var vendor = {
		id : "vendor",
		xtype : "textfield",
		fieldLabel : '供应商',
		name : 'equ.vendor',
		anchor : "95%"
	}
	var manufacturer = {
		id : "manufacturer",
		xtype : "textfield",
		fieldLabel : '制造厂商',
		name : 'equ.manufacturer',
		anchor : "95%"
	}
	
	var purchaseprice = {
		id : "purchaseprice",
		xtype : "numberfield",
		fieldLabel : '采购价格',
		name : 'equ.purchaseprice',
		anchor : "90%"
	}
	var installdate = {
		id : "installdate",
		xtype : "datefield",
		fieldLabel : '安装日期',
		name : 'equ.installdate',
		format : 'Y-m-d',
		anchor : "90%"
	}
	var warrantyexpdate = {
		id : "warrantyexpdate",
		xtype : "datefield",
		fieldLabel : '保修截止日期',
		format : 'Y-m-d',
		name : 'equ.warrantyexpdate',
		anchor : "90%"
	}
	var designlife = {
		id : "designlife",
		xtype : "numberfield",
		fieldLabel : '设备设计寿命',
		name : 'equ.designlife',
		anchor : "90%"
	}

	
		var specialityStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'equ/findSpecialList.action'
		}),
		reader : new Ext.data.JsonReader({
			id : "unit"
		}, [{
			name : 'specialityCode'
		}, {
			name : 'specialityName'
		}])
	});
	specialityStore.load();
	var belongProfession =({
		fieldLabel : '所属专业',
		xtype : 'combo',
		id : 'belongProfession',
		store : specialityStore,
		valueField : "specialityCode",
		displayField : "specialityName",
		mode : 'remote',
		typeAhead : true,
		forceSelection : true,
		triggerAction : 'all',
		hiddenName : 'equ.belongProfession',
		editable : false,
		allowBlank : false,
		selectOnFocus : true,
		name : 'belongProfession',
	    anchor : "90%"
	});
//		var belongTeam = {
//		id : "belongTeam",
//		xtype : "textfield",
//		fieldLabel : '所属部门',
//		name : 'equ.belongTeam',
//		anchor : "90%"
//	}
	
	var belongTeam = new Ext.form.ComboBox({
		 fieldLabel:'所属部门',
		 name:'equ.belongTeam',
		 id:'belongTeam',
		 hiddenName : 'equ.belongTeam',
		 // valueField : "id",
		 // displayField : "name",
		 store:new Ext.data.SimpleStore({fields:['id','name'],data:[[]]}),
		 editable:false,
		 shadow:false,
		 anchor : "90%",
		 mode: 'local',
		 triggerAction:'all',
		 //maxHeight: 200,
		 tpl: '<tpl for="."><div style="height:200px"><div id="tree1"></div></div></tpl>',
		 selectedClass:'',
		 onSelect:Ext.emptyFn
		 });
		 var tree1 = new Ext.tree.TreePanel({
		 loader: new Ext.tree.TreeLoader({dataUrl:'equ/findDeptList.action?id=0'}),
		 border:false,
		 root:new Ext.tree.AsyncTreeNode({text: '皖能合肥电厂',id:'0'})
		 });
		tree1.on('beforeload', function(node) {
		tree1.loader.dataUrl = 'equ/findDeptList.action?id=' + node.id;
	     }, this);
		 
		 
		 
		 tree1.on('click',function(node){
		 belongTeam.setValue(node.id);
	     Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('belongTeam'),node.text);
		 belongTeam.collapse();
		 });
		 belongTeam.on('expand',function(){
		 tree1.render('tree1');
		 });
		 
	
	
		var chargeBy = {
		id : "chargeBy",
		xtype : "textfield",
		fieldLabel : '责任人',
		name : 'equ.chargeBy',
		anchor : "90%"
	}
	
		var changeBy = {
		id : "changeBy",
		xtype : "textfield",
		fieldLabel : '修改人',
		name : 'equ.changeBy',
		anchor : "90%"
	}
		var changeDate = {
		id : "changeDate",
		xtype : "datefield",
		fieldLabel : '修改时间',
		format : 'Y-m-d h:i:s',
		name : 'equ.changeDate',
		anchor : "90%"
	}
	
	var disabled = new Ext.form.ComboBox({
		id : 'disabled',
		fieldLabel : '使用状态',
		store : new Ext.data.SimpleStore({
			fields : ["disabled", "displayText"],
			data : [['Y', '在用'], ['N', '停用']]
		}),
		valueField : "disabled",
		displayField : "displayText",
		mode : 'local',
		forceSelection : true,
		blankText : '请选择使用状态',
		emptyText : '请选择使用状态',
		hiddenName : 'equ.disabled',
		value : 'Y',
		editable : false,
		typeAhead : true,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		name : 'equ.changeType',
		anchor : "90%"
	});
	
	
	
	
	
	var myform = new Ext.FormPanel({
		frame : true,
		labelAlign : 'left',
		title : '设备基本信息',
		bodyStyle : 'padding:30px',
		items:[{
			xtype : 'fieldset',
			title : '基本信息',
			width : 700,
			labelAlign : 'right',
			labelWidth : 100,
			
			autoHeight : true,
			bodyStyle : Ext.isIE
					? 'padding:0 0 5px 15px;'
					: 'padding:10px 15px;',
			border : true,
			style : {
				"margin-left" : "10px",
				"margin-right" : Ext.isIE6
						? (Ext.isStrict ? "-10px" : "-13px")
						: "0"
			},
		
			items : [{
				layout : 'column',
				items : [{
					columnWidth : 0.5,
					layout : 'form',
					items : [PAttributeCode,attributeCode,assetnum]           //
				}, {
					columnWidth : 0.5,
					layout : 'form',
					items : [PEquName,equName,assetType]  //
				}]
			},{
			layout : 'column',
			items : [{
				columnWidth : 1,
				layout : 'form',
				items :  [vendor]                                   
			}]
		},{
			layout : 'column',
			items : [{
				columnWidth : 1,
				layout : 'form',
				items :  [manufacturer]                                   
			}]
		},{
				layout : 'column',
				items : [{
					columnWidth : 0.5,
					layout : 'form',
					items : [purchaseprice,warrantyexpdate,belongProfession,chargeBy,changeBy,disabled]          
				}, {
					columnWidth : 0.5,
					layout : 'form',
					items : [designlife,installdate,belongTeam,priority,changeDate]  
				}]
			}],
			buttons : [{
				text : '保存',
				id : 'mysave',
				handler:function()
				{
					myform.getForm().submit({
							method : 'POST',
							url : 'equ/saveEquBaseInfo.action',
							success : function(form, action){
								var o = eval("("
														+ action.response.responseText
														+ ")");
												Ext.Msg.alert("注意", o.msg);
							}
					});
				}
					
			}, {
				text : '取消',
				id : 'mycancel',
				handler:function(){
					var node=new Object();
					node.id=Ext.get("attributeCode").dom.value;
					node.text=Ext.get("equName").dom.value;
					clickTree(node);
				}
			}]
			
			}]
			

		

	});
	
	
	
	
	
		var panelleft = new Ext.Panel({
		region : 'west',
		layout : 'fit',
		width : 250,
		collapsible : true,
		split:true,
		items : [mytree]
	});
	var right = new Ext.Panel({
		region : "center",
		layout : 'fit',
		collapsible : true,
		items : [myform]
	});
	
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [panelleft, right]
	});
})