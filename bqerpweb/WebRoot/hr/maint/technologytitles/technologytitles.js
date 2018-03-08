Ext.onReady(function() {
//定义全局变量
	var layout;
	var Border = Ext.Viewport;
	var selrows;//定义用来记录所选列表行数变量
	var op;//定义区分新添\修改的变量
	var blockwindow;
	Ext.QuickTips.init();// 支持tips提示
	Ext.form.Field.prototype.msgTarget = 'side';// 提示的方式
	Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
	var sm = new Ext.grid.CheckboxSelectionModel( 
	);//定义选择框列

	//定义列模板
	var cm = new Ext.grid.ColumnModel([sm, new Ext.grid.RowNumberer(),
	{
		header : "技术职称编号",
		dataIndex : "technologyTitlesId",
		hidden:true,
		sortable : true
	}, {
		header : "技术职称类别名称",
		dataIndex : "technologyTitlesName",
		width:200,
		sortable : true
	}, {
		header : "技术职称类别",
		dataIndex : "technologyTitlesType",
		width:150,
		sortable : true
	},{
		header : "技术职称等级",
		dataIndex : "technologyTitlesLevel",
		width:150,
		sortable : true
	},{
		header : "使用标志",
		width:100,
		sortable : true,
		dataIndex : "isUse",
		renderer : function(v) {
			if (v == 'U') {
				return "使用";
			}
			if (v == 'N') {
				return "停用"
				
			}
			if (v == 'L') { 
				return "注销";
				
			}
		}
	}, {
		header : "检索码",
		dataIndex : "retrieveCode",
		sortable : true

	}]);
/*------------start-定义读取数据源和取数据的格式------------*/
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'getALLTechnologyTitlesInfo.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : "totalProperty",
			root : "root"
		}, [{
			name : "technologyTitlesId"
		}, {
			name : "technologyTitlesName"
		}, {
		    name : "technologyTitlesType"
	    },{
			name : "technologyTitlesLevel"
		}, {
			name : "isUse"
		}, {
			name : "retrieveCode"
		}])
	});
	
/*------------end-定义读取数据源和取数据的格式------------*/
	ds.baseParams={method : "get"}
	ds.load({
		params : {
			
			start : 0,
			limit : 18
		}
	})
// ---------定义技术职称类别设置按钮的方法--------
	
//添加
	var typeadd = function() {
		op = "insert";
		blockform.getForm().reset();
		webpagenewWin(); 
	};
//修改
	 function typeupdate() {
		op = "update";
		selrows = grid.getSelectionModel().getSelections();
		if (selrows.length > 0) {
			if (selrows.length > 1) {
				Ext.Msg.show({
					title : "提示信息",
					msg : "请从下面列表中选择一条需要修改的技术职称类别信息!",
					buttons : Ext.MessageBox.OK,
					icon : Ext.MessageBox.INFO
				});
			} else {
				webpagenewWin();
				// // 显示表单所在窗体
			}
		} else {
			Ext.Msg.show({
				title : "提示信息",
				msg : "请从下面列表中选择需要修改的技术职称类别信息!",
				buttons : Ext.MessageBox.OK,
				icon : Ext.MessageBox.INFO
			});
		}

	};
//删除
	var typedelete = function() {
		selrows = grid.getSelectionModel().getSelections();
		var typeids = "";
		for (i = 0; i < selrows.length; i++) {
			typeids += selrows[i].data.technologyTitlesId + ",";
		}
		if (selrows.length > 0) {
			typeids = typeids.substring(0, typeids.length - 1);
			Ext.Msg.show({
				title : "提示信息",
				msg : "你确信要删除当前所有选择的技术职称类别吗?",
				buttons : Ext.MessageBox.OKCANCEL,
				icon : Ext.MessageBox.QUESTION,
				fn : function(e) {
					if (e == 'ok') {
						Ext.Ajax.request({
							url : 'getALLTechnologyTitlesInfo.action',
							params : {
								techtitlesids : typeids,
								method : "del"
							},
							method : 'post',
							waitMsg : '正在处理...',
							success : function(result, request) {
								var json = result.responseText;
						     // 将json字符串转换成对象
					    	  var o = eval("(" + json + ")");
								Ext.MessageBox.alert('提示信息', o.message);
								ds.baseParams={method : "get"}
								ds.load({
									params : {
										
										start : 0,
										limit : 18
									}
								});
							},
							failure : function(result, request) {
								Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
							}
						});
					}
				}
			});
		} else {
			Ext.Msg.show({
				title : "提示信息",
				msg : "请从下面列表中选择需要删除的角色!",
				buttons : Ext.MessageBox.OK,
				icon : Ext.MessageBox.INFO
			});
		}
	};
/*----------定义展开技术职称类别信息grid列表------*/
	var grid = new Ext.grid.GridPanel({
		ds : ds,
		cm : cm,
		sm : sm,
		tbar : [{
			id : "addtechnologytitlestype",
			text : "新增职称",
			iconCls : 'add',
			handler : typeadd
		}, {
			xtype : "tbseparator"
		}, {
			id : "updatetechnologytitlestype",
			text : "修改职称",
			iconCls : 'update',
			handler : typeupdate
		}, {
			xtype : "tbseparator"
		}, {
			id : "deletetechnologytitlestype",
			text : "删除职称",
			iconCls : 'delete',
			handler : typedelete
		}],
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : ds,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录",
			beforePageText:'页',
			afterPageText:'共{0}页'
		})
	});
	grid.on('dblclick',function(){
		typeupdate(); 
	}); 

	 
//	var isUseStore = new Ext.data.Store({
//		fields:[{id:'id'},{name:'name'}],
//		url:''
//	})
//	isUseStore.load({});
//	
	var techtypestore = new Ext.data.JsonStore({
		fields : [{
			name : "id",
			mapping : "id"
		}, {
			name : "name",
			mapping : "name"
		}],
		url : "getALLTechnologyTitlesInfo.action"
	});
	techtypestore.load({
		params : {
				method:"type"		
				}
		
	});

	// 技术职称等级
	var techgradestore = new Ext.data.JsonStore({
		fields : [{
			name : "id",
			mapping : "id"
		}, {
			name : "name",
			mapping : "name"
		}],
		url : "getALLTechnologyTitlesInfo.action"
	});
	techgradestore.load({
		params : {
				method:"grade"		
				}
		
	});
// -----定义新添、修改表单内容控件

	var typeid = new Ext.form.TextField({
		id : "technologyTitlesId",
		xtype : 'textfield',
		fieldLabel : '技术职称编号',
		name : 'newtl.technologyTitlesId',
		readOnly : true,
		anchor : '90%'
	});
	var typename = new Ext.form.TextField({
		id : "technologyTitlesName",
		xtype : 'textfield',
		fieldLabel : '技术职称名称',
		name : 'newtl.technologyTitlesName',
		allowBlank : false,
		blankText : '请输入技术职称类别名称...',
		anchor : '90%'

	});
	var techgrades = new Ext.form.ComboBox({
					id : 'technologyTitlesLevel',
					xtype : 'textfield',
					fieldLabel : '职称等级',
					displayField : "name",
					valueField : "id",
					mode : "local",
					emptyText : '请选择',
					allowBlank:false,
					blankText:'请选择技术职称等级！',
					triggerAction : 'all',
					hiddenName : 'newtl.technologyTitlesLevel',
					name : 'newtl.technologyTitlesLevel',
					anchor : '90%',
					store : techgradestore,
					editable : false
				})
	var techtypes = new Ext.form.ComboBox({
					id : 'technologyTitlesTypeId',
					xtype : 'textfield',
					fieldLabel : '职称类别',
					displayField : "name",
					valueField : "id",
					mode : "local",
					emptyText : '请选择',
					allowBlank:false,
					blankText:'请选择技术职称类别！',
					triggerAction : 'all',
					hiddenName : 'newtl.technologyTitlesTypeId',
					name : 'newtl.technologyTitlesTypeId',
					anchor : '90%',
					store : techtypestore,
					editable : false
				})

	var isUseStore = new Ext.data.Store({
					proxy:new Ext.data.HttpProxy({
							url:'getIsUseOfTL.action'
							}),
							reader : new Ext.data.JsonReader({
								totalProperty:'total',
								root:'root'
							},[{
								name :'id',
								mapping : "id"
							},{
								name: 'name',
								mapping : "name"
							}])
					});	
	isUseStore.load();				
	var techIsUse = new Ext.form.ComboBox({
					id : 'isUse',
					xtype : 'textfield',
					fieldLabel : '状态',
					displayField : "name",
					valueField : "id",
					mode : "local",
					emptyText : '请选择',
					triggerAction : 'all',
					hiddenName : 'newtl.isUse',
					name : 'newtl.isUse',
					anchor : '90%',
					store : isUseStore,
					editable : false,
					allowBlank:false
				})
	var typerievecode = new Ext.form.TextField({
		id : "retrieveCode",
		xtype : 'textfield',
		fieldLabel : '检索码',
		name : 'newtl.retrieveCode',
		readOnly : true,
		anchor : '90%'
	});
//定义表单读取数据格式
	var typeRecord = Ext.data.Record.create([{
		name : "technologyTitlesId"
	}, {
		name : "technologyTitlesName"
	}, {
		name : "technologyTitlesTypeId"
	},{
		name : "technologyTitlesLevel"
	}, {
		name : "isUse"
	}, {
		name : "retrieveCode"
	}]);
 
	var blockform = new Ext.FormPanel({
		id : "block",
		labelAlign : 'left',
		buttonAlign : 'center',
		frame : true,
		items : [typeid, typename, techtypes,techIsUse,techgrades, typerievecode],
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
				if (blockform.getForm().isValid())
				{
					blockform.getForm().submit({
						waitMsg : '保存中,请稍后...',
						url : "getALLTechnologyTitlesInfo.action",
						params : {
							method : op
						},
						success : function(form, action) {
							var json = action.response.responseText; 
							var o = eval("(" + json + ")");
							Ext.Msg.alert('提示信息', o.message);
                            blockform.getForm().reset();
							blockwindow.hide();
							ds.load({
									params : {
										
										start : 0,
										limit : 18
									}
								});
						},
						failure : function(form,action) {
								var o = eval("(" + action.response.responseText + ")");
								Ext.Msg.alert('错误', o.errMsg);
							}
					});
				}
			}
		}, {
			text : '取消',
			iconCls : 'cancer',
			handler : function() {
				blockwindow.hide();
//				if(op=="update")//修改时重置事件所选项的数据重填充修改表单
//				blockform.getForm().load({
//				url : 'getALLTechnologyTitlesInfo.action',
//				params : {
//					ttypeid : grid.getSelectionModel().getSelections()[0].data.technologyTitlesTypeId,
//					method : "form"
//				}
//			});
//			else{blockform.getForm().reset();//新添时重置是将表单置空
//			}
				
				
				
			}
		}],
// 面板中按钮的排列方式
		buttonAlign : 'center',

		reader : new Ext.data.JsonReader({
			root : 'root',
			message : 'message'
		}, typeRecord)
	});
	
/*------------------新添、修改表单窗体定义------------------------*/

	var webpagenewWin = function() {
		
		

/*-----新添、修改窗口定义---*/
		if (!blockwindow) {

			blockwindow = new Ext.Window({
				title : '',
				items : blockform,
				width : 400,
				height : 230,
				modal : true,
				layout : 'fit',
				closable : true,
				resizable : false,
				closeAction : 'hide',
				plain : true

			})

		}
		if (op == "insert") {
			blockwindow.setTitle("新增技术职称类别信息");
		    blockform.getForm().reset();
//自动添加检索码
			typename.on("change", function(filed, newValue, oldValue) {
				if (newValue != oldValue) {

					Ext.Ajax.request({
					url : 'getALLTechnologyTitlesInfo.action',
					params : {
						method : "getcode",
						titlesnames : newValue
					},
					method : 'post',
					success : function(result, request) {
						
						var json = result.responseText;
						// 将json字符串转换成对象
						var o = eval("(" + json + ")");
						typerievecode.setValue(o.message)
					}

				});

				}

			})
		} else {

			blockwindow.setTitle("修改技术职称类别信息");

			blockform.getForm().load({
				url : 'getALLTechnologyTitlesInfo.action',
				params : {
					ttypeid : grid.getSelectionModel().getSelections()[0].data.technologyTitlesId,
					method : "form"
				}
			});
		}
		//自动添加检索码
		typename.on("change", function(filed, newValue, oldValue) {
			if (newValue != oldValue) {
				Ext.Ajax.request({
					url : 'getALLTechnologyTitlesInfo.action',
					params : {
						method : "getcode",
						titlesnames : newValue
					},
					method : 'post',
					success : function(result, request) {
						
						var json = result.responseText;
						// 将json字符串转换成对象
						var o = eval("(" + json + ")");
						typerievecode.setValue(o.message)
					}

				});

			}

		})
		blockwindow.show();
	}
//页面布局
		new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});
});