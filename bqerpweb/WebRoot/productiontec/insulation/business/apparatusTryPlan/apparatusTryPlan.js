// 绝缘仪器仪表预试计划

Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.QuickTips.init();
Ext.onReady(function() {
	var strMethod;
	var strMsg;
	// ============== 定义grid ===============
	var record = Ext.data.Record.create([
	
		{
			// 仪器仪表序号
			name : 'pjj.regulatorId'
		},
		{
			// 仪器仪表名称
			name : 'pjj.names'
		},
		{
			//  编号
			name : 'pjj.regulatorNo'
		},
		{
			// 制造厂家
			name : 'pjj.factory'
		},
		{
			// 型号
			name : 'pjj.sizes'
		},
		{
			// 量程
			name : 'pjj.userRange'
		},
		{
			// 检验周期(月)
			name : 'pjj.testCycle'
		},
		{
			// 备注
			name : 'pjj.memo'
		},
		{
			// 单位编码
			name : 'pjj.enterpriseCode'
		},
		{
			// 下次试验时间
			name : 'nextDate'
		},
		{
			// 最近试验人员
			name : 'operateBy'
		},
		{
			// 最近试验人员姓名
			name : 'operateName'
		},
		{
			// 最近试验日期
			name : 'operateDate'
		}
		])

	// 定义获取数据源
	var dataProxy = new Ext.data.HttpProxy({
				url : 'productionrec/findPjjTryList.action'
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
				emptyText : "（仪器仪表名称）"
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
									header : "序号",
									dataIndex : 'pjj.regulatorId',
									hidden : true,
									sortable : true
								},
								{
							header : "仪器仪表名称",
							dataIndex : 'pjj.names',
							sortable : true
						}, {
							header : "编号",
							dataIndex : 'pjj.regulatorNo',
							sortable : true
						}, {
							header : "制造厂家",
							dataIndex : 'pjj.factory',
							sortable : true
						},{
							header : '型号',
							dataIndex : 'pjj.sizes',
							sortable : true
						},{
							header : '量程',
							hidden : true,
							dataIndex : 'pjj.userRange',
							sortable : true
						},{
							header : '检验周期（月）',
							dataIndex : 'pjj.testCycle',
							sortable : true
						},{
							header : '最近试验日期',
							dataIndex : 'operateDate',
							sortable : true
						},{
							header : '最近试验人员',
							dataIndex : 'operateName',
							sortable : true
						},{
							header : '下次试验日期',
							dataIndex : 'nextDate',
							sortable : true
						},{
							header : '备注',
							dataIndex : 'pjj.memo',
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
							text : "预试情况录入",
							iconCls : Constants.CLS_ADD,
							handler : editRecord
						}
//						, "-", {
//							text : Constants.BTN_UPDATE,
//							iconCls : Constants.CLS_UPDATE,
//							handler : updateRecord
//						}, "-", {
//							text : Constants.BTN_DELETE,
//							iconCls : Constants.CLS_DELETE,
//							handler : deleteRecord
//						}
						],

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
	grid.on("rowdblclick", editRecord);

	// ***************************弹出窗口**************************//

	// 仪器仪表序号
	var regulatorId = new Ext.form.Hidden({
		id : 'regulatorId',
		name : 'pjj.regulatorId'
	})
	// 仪器仪表名称
	var names = new Ext.form.TextField({
		fieldLabel : '仪器仪表名称',
		id : 'names',
		width : 180,
		name : 'pjj.names',
		readOnly : true
	})
	// 编号
	var regulatorNo = new Ext.form.TextField({
		fieldLabel : '编号',
		id : 'regulatorNo',
		width : 180,
		name : 'pjj.regulatorNo'
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
	// 检验周期（月）
//	var testCycle = new Ext.form.NumberField({
//		fieldLabel : '检验周期（月）',
//		id : 'testCycle',
//		width : 180,
//		name : 'pjj.testCycle',
//		allowDecimals : false,
//		allowNegative : false
//	})
	// 备注
    var memo = new Ext.form.TextArea({
        id : 'memo',
        name : "pjj.memo",
        fieldLabel : '备注',
        width : 180
    
    })
    // 最近试验时间
    var lastDate = new Ext.form.TextField({
    	fieldLabel : '最近试验时间',
    	id : 'lastDate',
    	name : 'lastDate',
    	width : 180,
    	readOnly : true
    })
    // 本次实验人员
    var operateName = new Ext.form.TriggerField({
				fieldLabel : '本次试验人员',
				 width : 180,
				id : "operateName",
				displayField : 'text',
				valueField : 'id',
				hiddenName : 'operateName',
				blankText : '请选择',
				emptyText : '请选择',
				maxLength : 100,
				readOnly : true
			});
	operateName.onTriggerClick = selectPersonWin;
	var operateBy = new Ext.form.Hidden({
		id : 'operateBy',
				name : 'operateBy'
			})
	//  检验周期（月）
	var testCycle = new Ext.form.Hidden({
		id : 'testCycle',
				name : 'testCycle'
			})
	/**
	 * 人员选择画面处理
	 */
	function selectPersonWin() {
		var args = {
			selectModel : 'signal',
			notIn : "",
			rootNode : {
				id : '-1',
				text : '合肥电厂'
			}
		}
		var person = window
				.showModalDialog(
						'../../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(person) != "undefined") {
			operateName.setValue(person.workerName);
			operateBy.setValue(person.workerCode);
		}
	}
	// 本次试验时间
	var operateDate = new Ext.form.DateField({
		fieldLabel : '本次试验时间',
		format : 'Y-m-d',
		name : 'operateDate',
		id : 'operateDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		checked : true,
		allowBlank : false,
		readOnly : true,
		value : new Date(),
		emptyText : '请选择',
		width : 180
	})
	// Form
	var addPanel = new Ext.FormPanel({
				frame : true,
				labelAlign : 'right',
				items : [regulatorId,names,lastDate,operateName,operateBy,testCycle,operateDate,
				memo
				     ]
			});

	// 弹出窗口
	var win = new Ext.Window({
				width : 360,
				height : 250,
				modal : true,
				title : '绝缘仪器仪表预试情况录入',
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
	 *  编辑记录
	 */
	function editRecord() {
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
			addPanel.getForm().reset();
			win.show();
			Ext.get('regulatorId').dom.value = rec.get('pjj.regulatorId');
			Ext.get('testCycle').dom.value = rec.get('pjj.testCycle') == null
			      ? "" : rec.get('pjj.testCycle');
			Ext.get('names').dom.value = rec.get('pjj.names')  == null
			      ? "" : rec.get('pjj.names');
			Ext.get('lastDate').dom.value = rec.get('operateDate') == null
			      ? "" : rec.get('operateDate');
			Ext.get('memo').dom.value = rec.get('pjj.memo') == null
			      ? "" : rec.get('pjj.memo');
			
		}
		}
		
								
		
	}



	/**
	 * 确定button压下的操作
	 */
	function confirmRecord() {
		if(Ext.get('operateBy').dom.value ==null || Ext.get('operateBy').dom.value =="")
		{
			Ext.Msg.alert("提示信息","请选择本次试验人员！")
		}
		else
		{
			Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_001,
				function(buttonobj) {
					if (buttonobj == 'yes') {
							var myurl = "";
									myurl = 'productionrec/editPjjTryInfo.action';
										addPanel.getForm().submit({
											method : Constants.POST,
											params : {
												regulatorId : Ext.get('regulatorId').dom.value,
												operateBy : Ext.get('operateBy').dom.value,
												operateDate : Ext.get('operateDate').dom.value,
												memo : Ext.get('memo').dom.value,
												testCycle : Ext.get('testCycle').dom.value
											},
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
				})
		}
		
		
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









							