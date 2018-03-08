Ext.onReady(function() {
	var method = "one";
	var equCode = "";
	var equName = "";
	var index;
	// // -----------定义tree-----------------
	var root = new Ext.tree.AsyncTreeNode({
		id : "root",
		text : "设备树"
	});

	var mytree = new Ext.tree.TreePanel({
		renderTo : "treepanel",
		autoHeight : true,
		root : root,
		animate : true,
		enableDD : true,
		enableDrag : true,
		border : false,
		rootVisible : true,
		containerScroll : true,
		// checkModel: "single" ,
		requestMethod : 'GET',
		loader : new Ext.tree.TreeLoader({
			url : "equ/getTreeForSelect.action",
			baseParams : {
				id : 'root',
				method : method
			}

		})
	});

	// -----------树的事件----------------------
	// 树的单击事件
	mytree.on("click", clickTree, this);
	mytree.on('beforeload', function(node) {
		// 指定某个节点的子节点
		mytree.loader.dataUrl = 'equ/getTreeForSelect.action?method=' + method
				+ '&id=' + node.id;
	}, this);

	function clickTree(node) {
		equCode = node.id;
		equName = node.text.substring(node.text.indexOf(' ') + 1,
				node.text.length);
		equcode.setValue(equCode);
		equname.setValue(equName);
		querydata();
	}

	root.expand();// 展开根节点

	// --------------------------------------

	// -----------设备列表--------------------

	var MyRecord = Ext.data.Record.create([{
		name : 'equId'
	}, {
		name : 'equName'
	}, {
		name : 'attributeCode'
	}, {
		name : 'locationCode'
	}, {
		name : 'installationCode'
	}, {
		name : 'assetnum'
	}]);

	var dataProxy = new Ext.data.HttpProxy({
		url : 'equ/findEquListByFuzzy.action'
	});

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);

	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
	// 分页
	store.load({
		params : {
			start : 0,
			limit : 18
		}
	});

	var fuuzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});
	var grid = new Ext.grid.GridPanel({
		store : store,
		border : false,
		columns : [{
			header : "ID",
			sortable : true,
			dataIndex : 'equId',
			hidden : true
		}, {
			header : "设备功能码",
			width : 100,
			sortable : true,
			dataIndex : 'attributeCode'
		}, {
			header : "设备名称",
			width : 100,
			sortable : true,
			dataIndex : 'equName'
		}],
		tbar : [fuuzy, {
			text : "查询",
			handler : queryRecord
		}],
		// 分页
		//modify by ypan 20100925
		bbar : new Ext.PagingToolbar({
			id : 'bbar1',
			pageSize : 18,
			store : store
//			displayInfo : true
//			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
//			emptyMsg : "没有记录"
		})
		
		/*//分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})*/
	});

	grid.on("click", selectOneRecord);

	// ------------------

	// 查询
	function queryRecord() {
		var fuzzytext = fuuzy.getValue();
		store.baseParams = {
			fuzzy : fuzzytext
		};
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}

	function selectOneRecord() {
		var record = grid.getSelectionModel().getSelected();
		equCode = record.get("attributeCode");
		equName = record.get("equName");
		equcode.setValue(equCode);
		equname.setValue(equName);
		if(equCode==""||equCode==null)
		{
			Ext.MessageBox.alert("提示","数据错误!");
			return;
		
		}else
		{
		querydata();
		}
	}

	// --------------------------------------

	// -----------设备维修列表--------------------
	var record = Ext.data.Record.create([{
		name : 'order.id'
	}, {
		name : 'order.failureCode'
	}, {
		name : 'order.attributeCode'
	}, {
		name : 'order.equName'
	}, {
		name : 'order.checkAttr'
	}, {
		name : 'order.preContent'
	}, {
		name : 'order.description'
	}, {
		name : 'order.parameters'
	}, {
		name : 'order.problem'
	}, {
		name : 'order.spareParts'
	}, {
		name : 'startDate'
	}, {
		name : 'endDate'
	}, {
		name : 'order.supervisor'
	}, {
		name : 'supervisorName'
	}, {
		name : 'order.participants'
	},{name:'order.workFlowNo'},
	{name:'order.status'}]);

	var xproxy = new Ext.data.HttpProxy({
		url : 'equstandard/findListByEquCode.action'
	});

	var xreader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, record);

	var censtore = new Ext.data.Store({
		proxy : xproxy,
		reader : xreader
	});
	// 分页

	var equcode = new Ext.form.TextField({
		id : "equcode",
		name : "equcode",
		readOnly : true
	});
	var equname = new Ext.form.TextField({
		id : "equname",
		name : "equname",
		width : '400',
		readOnly : true
	});
	var cenGrid = new Ext.grid.GridPanel({
		store : censtore,
		autoHeght : true,
		id : 'cenGrid',
		loadMask : {
			msg : '读取数据中 ...'
		},
		columns : [new Ext.grid.RowNumberer(), {
			header : "ID",
			sortable : true,
			dataIndex : 'order.id',
			hidden : true
		}, {
			header : "设备名称",
			anchor : '54%',
			dataIndex : 'order.equName',
			sortable : true
		},{
			header : "检修性质",
			anchor : '54%',
			dataIndex : 'order.checkAttr',
			sortable : true,
			renderer:function(v){
				if(v==1)return 'A级'
				else if(v==2)return 'B级'
				else if(v==3)return 'C级'
				else if (v==4)return 'D级'
				else if(v==5)return '重大消缺'
			}
		},{
			header : "修前情况",
			anchor : '54%',
			dataIndex : 'order.preContent',
			sortable : true
		}, {
			header : "检修情况",
			anchor : '54%',
			dataIndex : 'order.description',
			sortable : true
		}, {
			header : "修后技术参数",
			anchor : '54%',
			dataIndex : 'order.parameters',
			sortable : true
		},{
			header : "存在问题",
			anchor : '54%',
			dataIndex : 'order.problem',
			sortable : true
		}, {
			header : "更换备品备件",
			anchor : '54%',
			dataIndex : 'order.spareParts',
			sortable : true
		}, {
			header : "开始日期",
			anchor : '54%',
			dataIndex : 'startDate',
			sortable : true
		},  {
			header : "结束日期",
			anchor : '54%',
			dataIndex : 'endDate',
			sortable : true
		}, {
			header : "负责人",
			anchor : '54%',
			dataIndex : 'supervisorName',
			sortable : true
		}, {
			header : "参加人",
			anchor : '54%',
			dataIndex : 'order.participants',
			sortable : true
		}, 
		{
		  
		  header : "状态",
			anchor : '54%',
			dataIndex : 'order.status',
			sortable : true,
			hidden:true,
			renderer:function(value)
			{
			  return	getStatusName(value);
			}
		}],
		tbar : new Ext.Toolbar({
			items : [{
				text : '刷新',
				iconCls : "reflesh",
				handler : querydata
			}, '-', {
				text : "新增",
				iconCls : "add",
				handler : add
			}, '-', {
				text : "修改",
				iconCls : "update",
				id:'btnUpdate',
				//modify by ypan 20100921
				handler : update
			}, '-', {
				text : "删除",
				iconCls : "delete",
				id:'btnDelete',
				handler : del
			}
			//modify by ypan 20100916
			/*,'-',{
			text : "上报",
				iconCls : "delete",
				id:'btnReport',
				handler : report
			}*/]
		}),
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : censtore,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});
	function querydata() {
		censtore.baseParams = {
			equCode : equCode
		};
		censtore.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}
	//modify by ypan 20100921
	cenGrid.on("rowdblclick", function(thisgrid,columnIndex,grid) {
//		lastModifyTime.setValue(cenGrid.getStore().getAt(columnIndex+1).data.get('startDate'));
		var selections = cenGrid.getSelections();
		if (selections.length > 0) {
			win.show();
			form.getForm().reset();
			var record = cenGrid.getSelectionModel().getSelected();
			form.getForm().loadRecord(record);
			realStartDate.setValue(record.get('startDate'));
			realEndDate.setValue(record.get('endDate'));
//			alert(cenGrid.getStore().getAt(columnIndex+1).get('startDate'));
//				
			if(columnIndex==cenGrid.getStore().getCount()-1){
				lastModifyTime.setValue('');
			}else{
				Ext.get('lastRepairDate').dom.value = cenGrid.getStore().getAt(columnIndex+1).get('startDate');
			}
		} else {
			Ext.Msg.alert('提示', '请从列表中选择需要修改的记录！');
		}
	
		
	})//update);
	
		cenGrid.on("rowclick", function(thisgrid,rowIndex,grid) {
			index = rowIndex+1;
	})
	
	cenGrid.on("rowclick", function()
	{
	var record = cenGrid.getSelectionModel().getSelected();
	if(record.get("order.status")=="1"||record.get("order.status")=="2")
	{
		
		Ext.get("btnUpdate").dom.disabled=true;
		
			Ext.get("btnDelete").dom.disabled=true;
			//modify by ypan 20100916
//				Ext.get("btnReport").dom.disabled=true;
	}
	else
	{
		Ext.get("btnUpdate").dom.disabled=false;
			Ext.get("btnDelete").dom.disabled=false;
			
//				Ext.get("btnReport").dom.disabled=false;
				
	}
	});

	var viewport = new Ext.Viewport({
		layout : 'border',
		items : [{
			region : 'west',
			layout : 'fit',
			margins : '0 0 0 0',
			width : '170',
			collapsible : true,
			border : false,
			items : [new Ext.TabPanel({
				activeTab : 0,
				layoutOnTabChange : true,
				border : false,
				items : [{
					title : '树形方式',
					border : false,
					autoScroll : true,
					items : [mytree]
				}, {
					title : '列表方式',
					border : false,
					autoScroll : true,
					layout : 'fit',
					items : [grid]
				}]
			})]
		}, {
			region : 'center',
			layout : 'fit',
			bodyStyle : 'padding: 0,0,0,5',
			tbar : new Ext.Toolbar({
				items : ["设备：", equcode, '-', equname]
			}),
			items : [cenGrid]
		}]
	});
	var nowdate = new Date();
	// ----------------------------------------------------------------------------------------------------
	var btnSelect = new Ext.Button({
		id : "btnSelect",
		text : "...",
		name : "first",
		handler : function() {
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '安庆电厂'
				}
			};
			var obj = window
					.showModalDialog(
							'../../../comm/jsp/hr/workerByDept/workerByDept2.jsp',
							args,
							'dialogWidth=600px;dialogHeight=420px;center=yes;help=no;resizable=no;status=no;');
			if (typeof(obj) == 'object') {
				Ext.get("order.supervisor").dom.value = obj.workerCode;
				Ext.get("supervisorName").dom.value = obj.workerName;
			}
		}
	});
	var btnView = new Ext.Button({
		id : 'btnView',
		text : '查看',
		handler : function() {
			window.open("/power/managecontract/showConFile.action?conid=" + id
					+ "&type=CON");
		}
	});
	//add by ypan 20100916
	var stateData = new Ext.data.SimpleStore({
				data : [[1, 'A级'],[2,'B级'],[3,'C级'], [4,'D级'],[5,'重大消缺']],
				fields : ['value', 'name']
			});
	// 定义状态
	var stateComboBox = new Ext.form.ComboBox({
		        fieldLabel : '检修性质',
				id : "stateCob",
				store : stateData,
				displayField : "name",
				valueField : "value",
				mode : 'local',
				triggerAction : 'all',
				hiddenName:'order.checkAttr',
				readOnly : true,
				value: 1,
				anchor : '85%'
			});
		//add by ypan 20100921
		var lastModifyTime = new Ext.form.TextField({
//	    xtype : 'datefield',
//		format : 'Y-m-d',
		fieldLabel : '上次检修时间',
		name : 'lastRepairDate',
		id : 'lastRepairDate',
//		itemCls : 'sex-left',
//		clearCls : 'allow-float',
		readOnly : true,
		editable:false,
//		checked : true,
		anchor : '85%',
		value : '',
		allowBlank : true,
		style : 'cursor:pointer'
		/*listeners : {
			focus : function() {
				WdatePicker({
					startDate : " ",
					alwaysUseStartDate : false,
					dateFmt : 'yyyy-MM-dd'
				

				});
				this.blur();
			}
		}*/
	});		
	
	//add by ypan 20100921
	var realStartDate = new Ext.form.TextField({
//		id : 'endDate',
//		fieldLabel : '竣工日期',
//		anchor : "85%",
//		style : 'cursor:pointer',
//		name:'model.endDate',
//		        xtype : 'datefield',
//					format : 'Y-m-d',
				fieldLabel : '实际开始时间',
				name : 'order.startDate',
				id : 'realStartDate',
//					itemCls : 'sex-left',
//					clearCls : 'allow-float',
//					checked : true,
				anchor : '90%',
				allowBlank : false,
				style : 'cursor:pointer',
//				forceSelection : true,
//				selectOnFocus : true,
//				readOnly : true,
				listeners : {
					focus : function() {
						 WdatePicker({
							        startDate : '%y-%m-%d',
					                dateFmt : 'yyyy-MM-dd',
					                alwaysUseStartDate : true,
									onpicked : function() {
										if (lastModifyTime.getValue() > realStartDate
														.getValue()) {
											Ext.Msg.alert("提示", "实际开始时间必须大于上次检修时间");
											realStartDate.setValue("")
											return;
										}
										//realStartDate.clearInvalid();
									}
									/*onclearing : function() {
										realStartDate.markInvalid();
									}*/
								});
					}
				}
			});
			
		//add by ypan 20100921
	var realEndDate = new Ext.form.TextField({

//					xtype : 'datefield',
//					format : 'Y-m-d',
					fieldLabel : '实际结束时间',
					name : 'order.endDate',
					
//					itemCls : 'sex-left',
//					clearCls : 'allow-float',
//					readOnly : true,
//					checked : true,
					anchor : '90%',
				
				
			
				
				id : 'realEndDate',
//					itemCls : 'sex-left',
//					clearCls : 'allow-float',
//					checked : true,
				anchor : '90%',
				allowBlank : false,
				style : 'cursor:pointer',
//				forceSelection : true,
//				selectOnFocus : true,
//				readOnly : true,
				listeners : {
					focus : function() {
						 WdatePicker({
							        startDate : '%y-%m-%d',
					                dateFmt : 'yyyy-MM-dd',
					                alwaysUseStartDate : true,
									onpicked : function() {
										if (realEndDate.getValue() < realStartDate
														.getValue()) {
											Ext.Msg.alert("提示", "实际结束时间必须大于实际开始时间");
											realEndDate.setValue("")
											return;
										}
										//realStartDate.clearInvalid();
									}
									/*onclearing : function() {
										realStartDate.markInvalid();
									}*/
								});
					}
				}
			});
	var content = new Ext.form.FieldSet({
		title : '检修台帐编辑',
		height : '100%',
		layout : 'form',
		buttonAlign : 'center',
		items : [{
			id : 'order.id',
			name : 'order.id',
			xtype : 'hidden',
			readOnly : true,
			allowBlank : true,
			anchor : '85%'
		}, {
			id : 'order.failureCode',
			name : 'order.failureCode',
			xtype : 'hidden',
			readOnly : true,
			allowBlank : true,
			anchor : '85%'
		}, {
			id : 'txtequcode',
			name : 'order.attributeCode',
			xtype : 'textfield',
			fieldLabel : '系统/设备',
			readOnly : true,
			allowBlank : true,
			anchor : '85%'
		}, {
			id : 'txtequname',
			name : 'order.equName',
			xtype : 'textfield',
			fieldLabel : '系统/设备名称',
			readOnly : true,
			allowBlank : true,
			anchor : '85%'
		}, /*{
			id : 'order.checkAttr',
			name : 'order.checkAttr',
			xtype : 'textfield',
			fieldLabel : '检修性质',
			readOnly : false,
			allowBlank : false,
			anchor : '85%'
		}*/stateComboBox,
		  //add by ypan 20100916
			lastModifyTime, {
			id : 'order.preContent',
			name : 'order.preContent',
			xtype : 'textarea',
			fieldLabel : '修前情况',
			height : '40',
			readOnly : false,
			allowBlank : false,
			anchor : '84.35%'
		}, {
			id : 'order.description',
			name : 'order.description',
			xtype : 'textarea',
			fieldLabel : '检修情况',
			height : '40',
			readOnly : false,
			allowBlank : false,
			anchor : '84.35%'
		}, {
			id : 'order.parameters',
			name : 'order.parameters',
			xtype : 'textarea',
			fieldLabel : '修后技术参数',
			height : '40',
			readOnly : false,
			allowBlank : true,
			anchor : '84.35%'
		}, {
			id : 'order.problem',
			name : 'order.problem',
			xtype : 'textarea',
			fieldLabel : '存在问题',
			height : '40',
			readOnly : false,
			allowBlank : true,
			anchor : '84.35%'
		}, {
			id : 'order.spareParts',
			name : 'order.spareParts',
			xtype : 'textarea',
			fieldLabel : '更换的备品备件',
			height : '40',
			readOnly : false,
			allowBlank : true,
			anchor : '84.35%'
		}, {
			layout : 'column',
			border : false,
			anchor : '90%',
			items : [{
				layout : 'form',
				labelWidth : 100,
				columnWidth : .5,
				border : false,
				items : [realStartDate]
			}, {
				layout : 'form',
				labelWidth : 100,
				columnWidth : .5,
				border : false,
				items : [realEndDate]
			}]
		}, {
			layout : 'column',
			border : false,
			anchor : '90%',
			items : [{
				layout : 'form',
				labelWidth : 100,
				columnWidth : .6,
				border : false,
				items : [{
					id : 'supervisorName',
					name : 'supervisorName',
					xtype : 'textfield',
					fieldLabel : '负责人',
					readOnly : true,
					allowBlank : false,
					anchor : '95%'
				}]
			}, {
				layout : 'form',
				labelWidth : 100,
				columnWidth : .2,
				border : false,
				items : [btnSelect]
			}]
		}, {
			id : 'order.supervisor',
			name : 'order.supervisor',
			xtype : 'hidden',
			value : '999999',
			fieldLabel : '负责人',
			readOnly : false,
			allowBlank : true,
			anchor : '85%'
		}, {
			id : 'order.participants',
			name : 'order.participants',
			xtype : 'textarea',
			fieldLabel : '参加人',
			height : '40',
			readOnly : false,
			allowBlank : true,
			anchor : '84.35%'
		}],
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
				if (!form.getForm().isValid()) {
					return false;
				}
				var msg='';
				if (lastModifyTime.getValue() > realStartDate.getValue()) {
					msg+="实际开始时间必须大于上次检修时间<br/>";
				};
				if (realEndDate.getValue() < realStartDate.getValue()) {
					msg+="实际结束时间必须大于实际开始时间<br/>";									
				};
				if (msg!='') {
					Ext.Msg.alert('提示',msg);
					return;
				}
				var url = "";
				if (Ext.get('order.id').dom.value == "") {
					url = 'equstandard/addMaintenance.action';
				} else {
					url = 'equstandard/updateMaintenance.action';
				}
				form.getForm().submit({
					url : url,
					method : 'post',
//					params : {
//						filePath : Ext.get("annex").dom.value
//					},
					success : function(form, action) {
						var message = eval('(' + action.response.responseText
								+ ')');
						Ext.Msg.alert("成功", message.Msg);
						querydata();
						win.hide();
					},
					failure : function() {
						Ext.Msg.alert('错误', '操作失败！.');
					}
				})
			}
		}, {
			text : '取消',
			iconCls : 'cancer',
			handler : function() {
				win.hide();
			}
		}]
	});
	var form = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		labelWidth : 100,
		autoHeight : true,
		region : 'center',
		border : false,
		items : [content]
	});

	var win = new Ext.Window({
		// title : '检修台帐编辑',
		autoHeight : true,
		modal : true,
		width : 600,
		closeAction : 'hide',
		items : [form]
	})
	//modify by ypan 20100921
	function add() {
		if (equCode == ""||equCode==null) {//modify by wpzhu
			Ext.Msg.alert('提示', '请从左边选择一设备！');
			return false;
		}
		win.show();
		form.getForm().reset();
		Ext.get('txtequcode').dom.value = equCode;
		Ext.get('txtequname').dom.value = equName;
		/*alert(Ext.get('startDate').dom.value);
		Ext.get('lastRepairDate').dom.value=(Ext.get('startDate').dom.value!=''?Ext.get('startDate').dom.value:'');//add by ypan 20100916
*/		Ext.get('realStartDate').dom.value = nowdate.format('Y-m-d');
		Ext.get('realEndDate').dom.value = nowdate.format('Y-m-d');
		var count = censtore.getCount();
		if (censtore.getCount() > 0) {
			lastModifyTime.setDisabled(true);
			var date = censtore.getAt(0).get("startDate");
			//var dd = StringToDate(date);
			//dd.setMonth(dd.getMonth()+1) 
			//var startdate = ChangeDateToString(dd);
			lastModifyTime.setValue(date);
			lastModifyTime.setDisabled(true);
		} else {
			Ext.get('lastRepairDate').dom.value='';
		}
		
	}
	//modify by ypan 20100921
	
	function update() {
		
		var selections = cenGrid.getSelections();
		if (selections.length > 0) {
			win.show();
			form.getForm().reset();
			var record = cenGrid.getSelectionModel().getSelected();
			form.getForm().loadRecord(record);
			realStartDate.setValue(record.get('startDate'));
			realEndDate.setValue(record.get('endDate'));
			if(index==cenGrid.getStore().getCount()){
				lastModifyTime.setValue('');
			}else{
           Ext.get('lastRepairDate').dom.value = cenGrid.getStore().getAt(index).get('startDate'); 			}
			
		} else {
			Ext.Msg.alert('提示', '请从列表中选择需要修改的记录！');
		}
	}
	function del() {
		var selections = cenGrid.getSelections();
		if (selections.length > 0) {
			var record = cenGrid.getSelectionModel().getSelected();
			Ext.Msg.confirm('提示', '删除的数据您将不能恢复,确定要删除吗?', function(b) {
				if (b == 'yes') {
					Ext.Ajax.request({
						url : 'equstandard/deleteMaintenance.action',
						params : {
							"order.id" : record.get("order.id")
						},
						method : 'post',
						waitMsg : '正在删除数据...',
						success : function(result, request) {
							querydata();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
						}
					});
				}
			})
		} else {
			Ext.Msg.alert('提示', '请从列表中选择需要删除的记录！');
		}
	}
	//modify by ypan 20100916
	/*function report()
	{
		
		var selections = cenGrid.getSelections();
		if (selections.length > 0) {
		
			var record = cenGrid.getSelectionModel().getSelected();
			var obj=new Object();
			obj.busiNo=record.get("order.id");
			obj.flowCode="equRepair";
			obj.entryId=record.get("order.workFlowNo");
		
			 var danger = window.showModalDialog('reportSign.jsp',
                obj, 'dialogWidth=700px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');
               
                querydata();
                
		} else {
			Ext.Msg.alert('提示', '请从列表中选择需上报的记录！');
		}
	}*/
	
	function getStatusName(status)
	{
	  	if(status=="1") return "已上报";
	  	else if(status=="2") return "已审批结束";
	  	else if(status=="3") return "已退回";
	  	else return "未上报";
	}
});