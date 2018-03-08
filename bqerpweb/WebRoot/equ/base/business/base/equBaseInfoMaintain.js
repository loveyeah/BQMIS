function getDate() {
	var d, s, t;
	d = new Date();
	s = d.getFullYear().toString(10) + "-";
	t = d.getMonth() + 1;
	s += (t > 9 ? "" : "0") + t + "-";
	t = d.getDate();
	s += (t > 9 ? "" : "0") + t + " ";
	return s;
}
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
		   myform.getForm().reset();
		   query();
		   annexAdd.setDisabled(true);
		   annexDelete.setDisabled(true);
		   infoSave.setDisabled(true);
		   infoDelete.setDisabled(true);
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
							if(data.installationCode!=null&&o.installationDesc!=null){
								locationName.setValue(o.installationDesc);
								locationCode.setValue(data.installationCode);
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
							if(data.oneResponsible!=null&&o.oneResponsibleName!=null){
								oneResponsible.setValue(data.oneResponsible,o.oneResponsibleName);
							}
							if(data.twoResponsible!=null&&o.twoResponsibleName!=null){
								twoResponsible.setValue(data.twoResponsible,o.twoResponsibleName);
							}
							if(data.threeResponsible!=null&&o.threeResponsibleName!=null){
								threeResponsible.setValue(data.threeResponsible,o.threeResponsibleName);
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
							infoSave.setDisabled(false);
							infoDelete.setDisabled(false);
							annexAdd.setDisabled(false);
							annexDelete.setDisabled(false);
						}else{
							infoSave.setDisabled(false);
							if (equBaseId.getValue()!=null&&equBaseId.getValue()!="") {
								infoDelete.setDisabled(false);
							}
							annexAdd.setDisabled(true);
							annexDelete.setDisabled(true);
						}
												},
												failure : function(result,
														request) {
													Ext.MessageBox.alert('错误',
															'操作失败,请联系管理员!');
												}
											});
		myform.setTitle("["+node.text+"] 基本信息");
		}
	}
	//设备基础信------------------------------------------------------------------------
	function baseInfoCheck(){
		var msg='';
		if(factoryDate.getValue()==null||factoryDate.getValue()==""){
			msg+='出厂日期不能为空</br>';
		}
		if(installationDate.getValue()==null||installationDate.getValue()==""){
			msg+='安装日期不能为空</br>';
		}
		if (factoryDate.getValue()!=null&&factoryDate.getValue()!=""&&installationDate.getValue()!=null&&
		installationDate.getValue()!=""&&factoryDate.getValue() > installationDate.getValue()) {
			msg+='安装日期必须大于出厂日期</br>';
		}
		if (locationCode.getValue()==null||locationCode.getValue()=='') {
			msg+='安装位置必须填写</br>';
		}
		if (price.getValue()==null||price.getValue()=='') {
			msg+='单价必须填写</br>';
		}
		if (oneResponsible.combo.getValue()==null||oneResponsible.combo.getValue()=='') {
			msg+='一级责任人必须填写</br>';
		}
		if (twoResponsible.combo.getValue()==null||twoResponsible.combo.getValue()=='') {
			msg+='二级责任人必须填写</br>';
		}
		if (threeResponsible.combo.getValue()==null||threeResponsible.combo.getValue()=='') {
			msg+='三级责任人必须填写</br>';
		}
		if (msg=='') {
			return true;
		}else{
			Ext.Msg.alert('提示',msg);
			return false;
		}
	}
	function baseInfoSave(){
		if(!baseInfoCheck()) return;
		Ext.Msg.wait("正在保存,请等待...");
		myform.getForm().submit({
			method : 'POST',
			params : {
						},
			url : 'equ/saveOrUpdateBaseInfo.action',
			success : function(form, action) {
				var o = eval("(" + action.response.responseText + ")");
				equBaseId.setValue(o.EquBaseId);
				infoDelete.setDisabled(false);
				annexAdd.setDisabled(false);
				annexDelete.setDisabled(false);
				Ext.Msg.alert("提示", '操作成功！');
			},
			faliue : function() {
				Ext.Msg.alert('错误', '出现未知错误.');
			}
		});
	}
	function baseInfoDelete(){
							Ext.Msg.confirm('提示', '删除的数据您将不能恢复,确定要删除吗?',
									function(b) {
										if (b == "yes") {
											Ext.Ajax.request({
												url : 'equ/deleteBaseInfo.action',
												params : {
													EquBaseId : equBaseId.getValue()
												},
												method : 'post',
												waitMsg : '正在删除数据...',
												success : function(result,
														request) {
													Ext.MessageBox.alert('提示',
															'删除成功!');
													myform.getForm().reset();
													query();
													annexAdd.setDisabled(true);
													annexDelete.setDisabled(true);
													infoSave.setDisabled(true);
													infoDelete.setDisabled(true);
												},
												failure : function(result,
														request) {
													Ext.MessageBox.alert('错误',
															'操作失败,请联系管理员!');
												}
											});
										}

									});
	}
	//保存按钮
	var infoSave=new Ext.Button({
				text : '保存',
				iconCls : 'save',
				handler:baseInfoSave
	});
	//删除按钮
	var infoDelete=new Ext.Button({
				text : '删除',
				iconCls : 'delete',
				handler:baseInfoDelete
	});
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
				anchor : "90%"
	});
	//设备规格
	var model=new Ext.form.TextField({
				fieldLabel : '设备规格',
				name : 'baseInfo.model',
				allowBlank:true,
				anchor : "90%"
	});
	// 出厂日期
	var factoryDate= new Ext.form.TextField({
				fieldLabel : "出厂日期",
				name : 'baseInfo.factoryDate',
				style : 'cursor:pointer',
				forceSelection : true,
				selectOnFocus : true,
				allowBlank : false,
				anchor : "90%",
				value : getDate(),
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : true
								});
					}
				}
			});
	// 安装日期
	var installationDate= new Ext.form.TextField({
				fieldLabel : "安装日期",
				name : 'baseInfo.installationDate',
				style : 'cursor:pointer',
				forceSelection : true,
				selectOnFocus : true,
				allowBlank : false,
				anchor : "90%",
				value : getDate(),
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : true
								});
					}
				}
			});
	//安装位置
	var locationName = new Ext.form.ComboBox({
		fieldLabel : '安装位置',
		mode : 'remote',
		editable : false,
		anchor : "90%",
		//allowBlank : false,
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../kksselect/selectLocation.jsp";
			var location = window.showModalDialog(url, '',
					'dialogWidth=400px;dialogHeight=400px;status=no');
			if (typeof(location) != "undefined") {
				locationName.setValue(location.name);
				locationCode.setValue(location.code);
			}
		}
	});
	var locationCode=new Ext.form.Hidden({
		name : 'baseInfo.installationCode'
	})
	//单价
	var price=new Ext.form.NumberField({
				fieldLabel : '单价',
				name : 'baseInfo.price',
				allowBlank:true,
				anchor : "90%"
	});
	//使用年限（年）
	var useYear=new Ext.form.NumberField({
				fieldLabel : '使用年限（年）',
				name : 'baseInfo.useYear',
				allowBlank:true,
				anchor : "90%"
	});
	//设备资产编号
	var assentCode=new Ext.form.TextField({
				fieldLabel : '设备资产编号',
				name : 'baseInfo.assetCode',
				allowBlank:true,
				anchor : "90%"
	});
	//主要技术参数
	var technicalParameters=new Ext.form.TextArea({
				fieldLabel : '主要技术参数',
				name : 'baseInfo.technicalParameters',
				allowBlank:true,
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
	function getFirstDeptName(deptId,flag){
		Ext.lib.Ajax.request('POST', 'comm/findFirstLeverDeptByDeptId.action?deptId='+deptId, {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
							 if (flag=='1') {
							 	oneDept.setValue(result==null?'':result.deptName);
							 }else if (flag=='2') {
							 	twoDept.setValue(result==null?'':result.deptName);
							 } else if(flag=='3'){
							 	threeDept.setValue(result==null?'':result.deptName);
							 }else{
							 }
					}
				});
	}
	//一级责任人
	var oneResponsible = new Power.person({
				anchor : '90%',
				fieldLabel : '一级责任人',
				hiddenName : 'baseInfo.oneResponsible'
			}, {
				selectModel : 'single'
			});

	oneResponsible.btnConfirm.on("click", function() {
			var emp=oneResponsible.chooseWorker();
			if (emp!=null) {
				getFirstDeptName(emp.get('deptId'),'1');
			}
			});
	
	//二级责任人
	var twoResponsible = new Power.person({
				anchor : '90%',
				fieldLabel : '二级责任人',
				hiddenName : 'baseInfo.twoResponsible'
			}, {
				selectModel : 'single'
			});

	twoResponsible.btnConfirm.on("click", function() {
			var emp=twoResponsible.chooseWorker();
			if (emp!=null) {
				getFirstDeptName(emp.get('deptId'),'2');
			}
			});
	
	//三级责任人
	var threeResponsible = new Power.person({
				anchor : '90%',
				fieldLabel : '三级责任人',
				hiddenName : 'baseInfo.threeResponsible'
			}, {
				selectModel : 'single'
			});

	threeResponsible.btnConfirm.on("click", function() {
			var emp=threeResponsible.chooseWorker();
			if (emp!=null) {
			getFirstDeptName(emp.get('deptId'),'3');
			}
			});
	
	//附件信息----------------------------------------------------------
	var annexAdd=new Ext.Button({
					id : 'btnAnnexAdd',
									text : "新增",
									iconCls : 'add',
									handler : function() {
										if (equBaseId.getValue()==null||equBaseId.getValue()== "") {
											Ext.Msg.alert('提示', '请选择设备！');
											return false;
										}
										docwin.setTitle("增加设备附件");
										docform.getForm().reset();
										docwin.show();
//										// Ext.get("fjdocFile").dom.select();
//										// document.selection.clear();
//										Ext.get('docType').dom.value = "CONATT";
//										Ext.get('keyId').dom.value = id;
//										Ext.get('lastModifiedName').dom.value = sessWorname;
									}
	});
	var annexDelete=new Ext.Button({
						id : 'btnAnnexDelete',
					text : "删除",
					iconCls : 'delete',
					handler : function() {
						var selrows = annexGrid.getSelectionModel()
								.getSelections();
						if (selrows.length > 0) {
							Ext.Msg.confirm('提示', '删除的数据您将不能恢复,确定要删除吗?',
									function(b) {
										if (b == "yes") {
											var recode=[];
											var selected = annexGrid
													.getSelectionModel()
													.getSelections();
													var ids=new Array();
													for (var i = 0; i < selected.length; i += 1) {
														recode=selected[i];
														ids.push(recode.get('annexId'));
													};
											Ext.Ajax.request({
												url : 'equ/deleteBaseAnnex.action',
												params : {
													ids : ids.join(",")
												},
												method : 'post',
												waitMsg : '正在删除数据...',
												success : function(result,
														request) {
													Ext.MessageBox.alert('提示',
															'删除成功!');
													query();
												},
												failure : function(result,
														request) {
													Ext.MessageBox.alert('错误',
															'操作失败,请联系管理员!');
												}
											});
										}

									});

						} else {
							Ext.Msg.alert('提示', '请选择您要删除的设备附件！');
						}
					}
	});
	var annextbar = new Ext.Toolbar({
		id : 'annextbar',
		items : [annexAdd, '-',annexDelete]
	});
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
	var annex_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	var annex_item_cm = new Ext.grid.ColumnModel([annex_sm,
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
				align : 'center',
				width : 300,
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
				sm : annex_sm,
				split : true,
				// autoHeight:true,
				height : 270,
				autoScroll : true,
				// collapsible : true,
				tbar : annextbar,
				border : false
//				,viewConfig : {
//					forceFit : true
//				}
			});
	annexGrid.on('rowdblclick', function(grid, rowIndex, e) {
				var record = annexGrid.getSelectionModel().getSelected();
				docform.getForm().reset();
				docwin.show();
				docform.getForm().loadRecord(record);
				docwin.setTitle("修改附件信息");
			});
	function query(){
	annex_ds.load({
					params : {
						equBaseId : equBaseId.getValue()
					}
				})
	};
	//附件弹窗-------------------------------------------------------------
	//附件选择
	var fjdocFile = {//new Ext.form.fileuploadfield(
		id : "oriFile",
		xtype : "fileuploadfield",
		// inputType : "file",
		fieldLabel : '选择附件',
		// allowBlank : false,
		anchor : "90%",
		height : 22,
		name : 'annex',
		buttonCfg : {
			text : '浏览...',
			iconCls : 'upload-icon'
		},
		listeners : {
			'fileselected' : function() {
				var url = Ext.get('oriFile').dom.value;
				var fileName = url.substring(url.lastIndexOf("\\") + 1,
						url.lastIndexOf("."));
						if (annexName.getValue()==null||annexName.getValue()=="") {
							annexName.setValue(fileName);
						}
			}
		}

	}
	
	//附件Id
	var annexId=new Ext.form.Hidden({
			name:'annexId'
	});
	//附件名称
	var annexName=new Ext.form.TextField({
			fieldLabel : '附件名称',
			allowBlank:true,
			name : 'annexName',
			anchor : "90%"
	})
	var doccontent = new Ext.form.FieldSet({
				height : '100%',
				layout : 'form',
				items : [annexId,annexName,fjdocFile]
			});
	var saveB=new Ext.Button({
				text : '保存',
				iconCls : 'save',
				handler : function() {
					if (!docform.getForm().isValid()) {
						return false;
					}
					var msg ='';
					if(annexName.getValue()==null||annexName.getValue()==''){
						msg+='请填写附件名称！！</br>';
					}
					if (Ext.get('oriFile').dom.value==null||Ext.get('oriFile').dom.value=='') {
						msg+='请选择要上传的附件！！</br>';
					}
					if (msg!='') {
						Ext.Msg.alert('提示',msg);
						return
					}
					docform.getForm().submit({
						url : 'equ/saveOrUpdateBaseAnnex.action',
						method : 'post',
						params : {
							filePath :Ext.get('oriFile').dom.value,
							annexId:annexId.getValue(),
							annexName:annexName.getValue(),
							infoId:equBaseId.getValue()
						},
						success : function(form, action) {
							var message = eval('('
									+ action.response.responseText + ')');
							Ext.Msg.alert('提示',message.msg);
							query();
							docwin.hide();
						},
						failure : function(form, action) {
							Ext.Msg.alert('错误', '出现未知错误.');
						}
					})
				}
	});
	var cancerB=new Ext.Button({
			text : '取消',
			iconCls : 'cancer',
			handler : function() {
				docform.getForm().reset();
				docwin.hide();
			}
	});
	var docform = new Ext.form.FormPanel({
				bodyStyle : "padding:5px 5px 0",
				labelAlign : 'right',
				id : 'shift-form',
				labelWidth : 80,
				autoHeight : true,
				fileUpload : true,
				region : 'center',
				border : false,
				items : [doccontent],
				buttons : [saveB,cancerB]
			});
	var docwin = new Ext.Window({
				title : '新增',
				//el : 'win',
				modal : true,
				autoHeight : true,
				width : 450,
				closeAction : 'hide',
				items : [docform]
			});
	
	var formtbar=new Ext.Toolbar({
			items:[infoDelete,'-',infoSave]
	});
			
	var myform = new Ext.FormPanel({
		frame : true,
		labelAlign : 'left',
		title : '设备基础信息维护',
		tbar : formtbar,
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
					items : [EquCode, equBaseId,manuFacturer,factoryDate,locationName,locationCode,useYear]
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
				 items : [oneResponsible.combo,twoResponsible.combo,threeResponsible.combo]
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
	annexAdd.setDisabled(true);
	annexDelete.setDisabled(true);
	infoSave.setDisabled(true);
	infoDelete.setDisabled(true);
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
	
//		installationCode.root.expand();
})