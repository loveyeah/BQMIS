// 绝缘设备台帐

Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.QuickTips.init();
Ext.onReady(function() {
	var strMethod;
	var strMsg;
	// ============== 定义grid ===============
	var record = Ext.data.Record.create([
	
		{
			// 设备序号
			name : 'deviceId'
		},
		{
			// 设备名称
			name : 'deviceName'
		},
		{
			//  试验周期（月）
			name : 'testCycle'
		},
		{
			// 制造厂家
			name : 'factory'
		},
		{
			// 型号
			name : 'sizes'
		},
		{
			// 量程
			name : 'userRange'
		},
		{
			// 电压
			name : 'voltage'
		},
		{
			// 备注
			name : 'memo'
		},
		{
			// 单位编码
			name : 'enterpriseCode'
		}
		])

	// 定义获取数据源
	var dataProxy = new Ext.data.HttpProxy({
				url : 'productionrec/findPtJyjdJSbtzlhList.action'
			});

	// 定义格式化数据
	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, record);

	// 定义封装缓存数据的对象
	var queryStore = new Ext.data.Store({
				// 访问的对象
				proxy : dataProxy,
				// 处理数据的对象
				reader : theReader
			});
	queryStore.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			});

	// -----------------控件定义----------------------------------
	// 查询参数
	var txtFuzzy = new Ext.form.TextField({
				id : "fuzzy",
				name : "pjj.fuzzy",
				width : 235,
				emptyText : "（设备名称）"
			});

	//定义选择列
    var check = new Ext.grid.CheckboxSelectionModel({
    		singleSelect : false
    });
	// --gridpanel显示格式定义-----开始-------------------
	var grid = new Ext.grid.GridPanel({
				region : "center",
				layout : 'fit',
				store : queryStore,
				stripeRows : true,
				columns : [
				 		check,
						//自动行号
						new Ext.grid.RowNumberer({
									header : "行号",
									width : 31
								}),{
									header : "设备序号",
									dataIndex : 'deviceId',
									hidden : true,
									sortable : true
								},
								{
							header : "设备名称",
							dataIndex : 'deviceName',
							sortable : true
						}, {
							header : "试验周期（月）",
							dataIndex : 'testCycle',
							sortable : true
						}, {
							header : "制造厂家",
							dataIndex : 'factory',
							sortable : true
						},{
							header : '型号',
							dataIndex : 'sizes',
							sortable : true
						},{
							header : '量程',
							hidden : true,
							dataIndex : 'userRange',
							sortable : true
						},{
							header : '电压',
							dataIndex : 'voltage',
							sortable : true
						},{
							header : '备注',
							dataIndex : 'memo',
							sortable : true
						}],
				sm : check,
				autoScroll : true,
				enableColumnMove : false,
				autoSizeColumns : true,
				viewConfig : {
					forceFit : false
				},

				// 头部工具栏
				tbar : [ {
							text : "模糊查询:"
						},txtFuzzy,{
							text : "查询",
							iconCls : Constants.CLS_QUERY,
							handler : queryRecord
						},"-", {
							text : "新增",
							iconCls : Constants.CLS_ADD,
							handler : addRecord
						}, "-", {
							text : Constants.BTN_UPDATE,
							iconCls : Constants.CLS_UPDATE,
							handler : updateRecord
						}, "-", {
							text : Constants.BTN_DELETE,
							iconCls : Constants.CLS_DELETE,
							handler : deleteRecord
						}],

				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : Constants.PAGE_SIZE,
							store : queryStore,
							displayInfo : true,
							displayMsg : Constants.DISPLAY_MSG,
							emptyMsg : Constants.EMPTY_MSG
						})
			});
	// --gridpanel显示格式定义-----结束--------------------

	// 注册双击事件
	grid.on("rowdblclick", updateRecord);

	// ***************************弹出窗口**************************//

	// 设备序号
	var deviceId = new Ext.form.Hidden({
		id : 'deviceId',
		name : 'pjj.deviceId'
	})
	// 设备名称
	var deviceName = new Ext.form.TextField({
		fieldLabel : '设备名称',
		id : 'deviceName',
		width : 180,
		name : 'pjj.deviceName'
	})
	// 检验周期（月）
	var testCycle = new Ext.form.NumberField({
		fieldLabel : '检验周期（月）',
		id : 'testCycle',
		width : 180,
		name : 'pjj.testCycle',
		allowDecimals : false,
		allowNegative : false
	})
	// 制造厂家
	var factory = new Ext.form.TextField({
		fieldLabel : '制造厂商',
		id : 'factory',
		width : 180,
		name : 'pjj.factory'
	})
	// 型号
	var sizes = new Ext.form.TextField({
		fieldLabel : '型号',
		id : 'sizes',
		width : 180,
		name : 'pjj.sizes'
	})
	// 量程
	var userRange = new Ext.form.TextField({
		fieldLabel : '量程',
		id : 'userRange',
		width : 180,
		name : 'pjj.userRange'
	})
	// 电压
	var voltage = new Ext.form.TextField({
		fieldLabel : '电压',
		id : 'voltage',
		width : 180,
		name : 'pjj.voltage'
	})
	// 备注
    var memo = new Ext.form.TextArea({
        id : 'memo',
        name : "pjj.memo",
        fieldLabel : '备注',
        width : 180
    
    })
	// Form
	var addPanel = new Ext.FormPanel({
				frame : true,
				labelAlign : 'right',
				items : [deviceId,deviceName,testCycle,factory,sizes,userRange,voltage,
				memo
				     ]
			});

	// 弹出窗口
	var win = new Ext.Window({
				width : 360,
				height : 310,
				modal : true,
				title : '增加/修改',
				buttonAlign : 'center',
				items : [addPanel],
				layout : 'fit',
				closeAction : 'hide',
				resizable : false,
				buttons : [{
							text : Constants.BTN_SAVE,
							iconCls : Constants.CLS_SAVE,
							buttonAlign : 'center',
							handler : confirmRecord
						}, {
							text : Constants.BTN_CANCEL,
							iconCls : Constants.CLS_CANCEL,
							buttonAlign : 'center',
							handler : function() {
								win.hide();
							}
						}]
			});

	/**
	 * 
	 *  查询记录 
	 */
	function queryRecord() {
		// 查询参数
		var name = txtFuzzy.getValue();
		queryStore.baseParams = {
			name : name
		};
		queryStore.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});
		grid.getView().refresh();
	}

	/**
	 * 
	 *  增加记录 
	 */
	function addRecord() {
		strMethod = "add";
		addPanel.getForm().reset();
		win.setTitle("新增绝缘设备信息");
		win.show();
	}

	
	/**
	 * 
	 *  修改记录
	 */
	function updateRecord() {
		var records = grid.getSelectionModel().getSelections();
		var recordslen = records.length;
		if(recordslen > 1)
		{
			Ext.Msg.alert("提示信息", "请选择其中一项进行编辑！");
		}
		else
		{
			var rec = grid.getSelectionModel().getSelected();
		if (!rec) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
			return false;
		} else {
			strMethod = "update";
			addPanel.getForm().reset();
			win.setTitle("修改绝缘设备信息");
			win.show();
			Ext.get('deviceId').dom.value = rec.get('deviceId');
			Ext.get('deviceName').dom.value = rec.get('deviceName')  == null
			      ? "" : rec.get('deviceName');
			Ext.get('testCycle').dom.value = rec.get('testCycle') == null
			      ? "" : rec.get('testCycle');
			Ext.get('factory').dom.value = rec.get('factory') == null
			      ? "" : rec.get('factory');
			Ext.get('sizes').dom.value = rec.get('sizes') == null
			      ? "" : rec.get('sizes');
			Ext.get('userRange').dom.value = rec.get('userRange') == null
			      ? "" : rec.get('userRange');
			Ext.get('voltage').dom.value = rec.get('voltage') == null
			      ? "" : rec.get('voltage');
			Ext.get('memo').dom.value = rec.get('memo') == null
			      ? "" : rec.get('memo');
		}
		}
		
	}

	/**
	 * 删除记录
	 */
	function deleteRecord() {
		var rec = grid.getSelectionModel().getSelections();
		var ids = [];
		if (rec.length > 0) {
			for (var i = 0; i < rec.length; i++) {
				var member = rec[i];
				if (member.get('deviceId')) {
					ids.push(member.get('deviceId'));
				} else {
				}
			}
			Ext.Msg.confirm(Constants.SYS_REMIND_MSG, "确认要删除所选数据？",
					function(buttonobj) {
						if (buttonobj == 'yes') {
							// 刪除
							Ext.Ajax.request({
								method : Constants.POST,
								url : 'productionrec/deletePtJyjdJSbtzlh.action',
								params : {
									ids : ids.toString()
								},
								success : function(result, request) {
									var o = eval("(" + result.responseText
											+ ")");
									if(o.msg == "数据删除成功！")
									{
									    Ext.Msg.alert("提示信息",o.msg);
									}
										queryStore.load({
													params : {
														start : 0,
														limit : Constants.PAGE_SIZE
													}
												});
										grid.getView().refresh();

								},
								failure : function() {
									Ext.Msg.alert(Constants.ERROR,
											Constants.DEL_ERROR);
								}
							});
						}
					});
		} else {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
		}
	}


	/**
	 * 
	 *  在增加数据前的非空检查 
	 */
	function checkAddform() {
		strMsg = "";
		var names = Ext.get('deviceName').dom.value;
		var testCycle = Ext.get('testCycle').dom.value;
		var sizes =  Ext.get('sizes').getValue();
		if (names == "" || names == null) {
			strMsg += String.format(Constants.COM_E_003, '"设备名称" ') + "<br/>";
		}
		if( testCycle == "" || testCycle == null)
		{
			strMsg += String.format(Constants.COM_E_003, '"检验周期（月）" ') + "<br/>";
		}
		if(sizes == "" || sizes == null)
		{
			strMsg += String.format(Constants.COM_E_003, '"型号" ') + "<br/>";
		}
		if (strMsg == "") {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 
	 *  在修改数据前的非空检查 
	 */
	function checkUpdateform() {
		strMsg = "";
        var names = Ext.get('deviceName').dom.value;
		var testCycle = Ext.get('testCycle').dom.value;
		var factory =  Ext.get('factory').getValue();
		if (names == "" || names == null) {
			strMsg += String.format(Constants.COM_E_003, '"设备名称" ') + "<br/>";
		}
		if( testCycle == "" || testCycle == null)
		{
			strMsg += String.format(Constants.COM_E_003, '"检验周期（月）" ') + "<br/>";
		}
		if(factory == "" || factory == null)
		{
			strMsg += String.format(Constants.COM_E_003, '"生产厂商" ') + "<br/>";
		}
		if (strMsg == "") {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 检查画面数据是否修改
	 */
	function isModified() {
		var rec = grid.getSelectionModel().getSelected();
		var flag = true;
		//设备序号
		if (Ext.get('deviceId').dom.value != rec.get('deviceId')){
		    flag = false;
		}
		
		//设备名称
		if (Ext.get('deviceName').dom.value != rec.get('deviceName')) 
		{
			flag = false;
		}
		//检验周期（月）
		if (!(Ext.get('testCycle').dom.value =="" &&rec.get('testCycle')==null) 
		      &&Ext.get('testCycle').dom.value != rec.get('testCycle')) {
			flag = false;
		}
		//制造厂商
		if (!(Ext.get('factory').dom.value =="" &&rec.get('factory')==null) 
		      &&Ext.get('factory').dom.value != rec.get('factory')) {
			flag = false;
		}
		//型号
		if (!(Ext.get('sizes').dom.value =="" &&rec.get('sizes')==null) 
		      &&Ext.get('sizes').dom.value != rec.get('sizes')) {
			flag = false;
		}
		//量程
		if (!(Ext.get('userRange').dom.value =="" &&rec.get('userRange')==null) 
		      &&Ext.get('userRange').dom.value != rec.get('userRange')) {
			flag = false;
		}
		//电压
		if (!(Ext.get('voltage').dom.value =="" &&rec.get('voltage')==null) 
		      &&Ext.get('voltage').dom.value != rec.get('voltage')) {
			flag = false;
		}
		// 备注
		if (!(Ext.get('memo').dom.value =="" &&rec.get('memo')==null) 
		      &&Ext.get('memo').dom.value != rec.get('memo')) {
			flag = false;
		}
		if (flag == false) {
			return true;
		} else {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_006);
			return false;
		}
	}

	/**
	 * 确定button压下的操作
	 */
	function confirmRecord() {
		if (strMethod == "update") {
			if (isModified()) {
			} else {
				return;
			}
		}
		if (!checkAddform()) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, strMsg);
			return;
		}
		Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_001,
				function(buttonobj) {
					if (buttonobj == 'yes') {
							var myurl = "";
							if (strMethod == "add") {
								
									myurl = 'productionrec/addPtJyjdJSbtzlh.action';
										addPanel.getForm().submit({
											method : Constants.POST,
											url : myurl,
											success : function(form, action) {
												var result = eval('('
														+ action.response.responseText
														+ ')');
												Ext.Msg.alert("提示信息",result.msg);
													txtFuzzy.setValue("");
		                                              queryStore.baseParams = {
														name : txtFuzzy.getValue()
																	};
	 												  queryStore.load({
																params : {
																start : 0,
																limit : Constants.PAGE_SIZE
																}
															});	
													grid.getView().refresh();
													win.hide();
											},
											failure :function(form, action) {
												var result = eval('('
														+ action.response.responseText
														+ ')');
												Ext.Msg.alert("提示信息",result.msg);
												return;
											}
										});
								
							}
							
							if (strMethod == "update") {
								if (checkUpdateform()) {
									
									myurl = 'productionrec/updatePtJyjdJSbtzlh.action';
										addPanel.getForm().submit({
											method : Constants.POST,
											url : myurl,
											success : function(form, action) {
												var result = eval('('
														+ action.response.responseText
														+ ')');
												
												// 显示成功信息
												Ext.Msg.alert("提示信息",result.msg);
												queryStore.load({
													params : {
														start : 0,
														limit : Constants.PAGE_SIZE
													}
												});
												grid.getView().refresh();
												win.hide();
											},
											failure : function(form, action) {
												var result = eval('('
														+ action.response.responseText
														+ ')');
												Ext.Msg.alert("提示信息",result.msg);
												return;
											}
										});
						     }  else {
									Ext.Msg.alert(Constants.SYS_REMIND_MSG, strMsg);
									return;
								}
					         }
							


					}
				}) 			
	}
			

	/**
	 * 页面加载显示数据
	 */
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				items : [grid]

			});

		
})









							