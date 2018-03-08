// 特种作业人员管理

Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.QuickTips.init();
Ext.onReady(function() {
	var strMethod;
	var strMsg;
	// ============== 定义grid ===============
	var record = Ext.data.Record.create([{
				// ID
				name : 'spj.offerId'
			}, {
				// 工号
				name : 'spj.workerCode'
			}, {
				// 操作项目
				name : 'spj.projectOperation'
			}, {
				// 岗位年限
				name : 'spj.postYear'
			}, {
				// 证书名称
				name : 'spj.offerName'
			}, {
				// 证书编号
				name : 'spj.offerCode'
			}, {
				// 证书发放日期
				name : 'offerDate'
			}, {
				// 证书有效开始日期
				name : 'offerStartDate'
			}, {
				// 证书有效结束日期
				name : 'offerEndDate'
			}, {
				// 体检日期
				name : 'medicalDate'
			}, {
				// 体检结果
				name : 'spj.medicalResult'
			}, {
				// 备注
				name : 'spj.memo'
			}, {
				// 企业编号
				name : 'spj.enterpriseCode'
			}, {
				// 员工姓名
				name : 'workerName'
			}])

	// 定义获取数据源
	var dataProxy = new Ext.data.HttpProxy({
				url : 'security/findSpecialoperatorsList.action'
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
	var txtFuzzy1 = new Ext.form.TextField({
				id : "fuzzy",
				name : "fuzzy",
				width : 235,
				emptyText : "姓名"
			});

	// 定义选择列
	var sm = new Ext.grid.RowSelectionModel({
				singleSelect : true
			});

	var check = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false
			});
	// --gridpanel显示格式定义-----开始-------------------
	var grid = new Ext.grid.GridPanel({
				region : "center",
				layout : 'fit',
				store : queryStore,
				stripeRows : true,
				columns : [check,
						// 自动行号
						new Ext.grid.RowNumberer({
									header : "行号",
									width : 31
								}), {
							header : "ID",
							dataIndex : 'spj.offerId',
							hidden : true,
							sortable : true
						}, {
							header : "姓名",
							dataIndex : 'workerName',
							sortable : true
						}, {
							header : "操作项目",
							dataIndex : 'spj.projectOperation',
							sortable : true
						}, {
							header : "岗位年限",
							dataIndex : 'spj.postYear',
							sortable : true
						}, {
							header : '证书名称',
							dataIndex : 'spj.offerName',
							sortable : true
						}, {
							header : '证书发放日期',
							hidden : true,
							dataIndex : 'offerDate',
							sortable : true
						}, {
							header : '体检结果',
							dataIndex : 'spj.medicalResult',
							sortable : true
						}, {
							header : '体检日期',
							dataIndex : 'medicalDate',
							sortable : true
						}, {
							header : '备注',
							dataIndex : 'spj.memo',
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
				tbar : [{
							text : "模糊查询:"
						}, txtFuzzy1, {
							text : "查询",
							iconCls : Constants.CLS_QUERY,
							handler : queryRecord
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

	// ID
	var offerId = new Ext.form.Hidden({
				id : 'offerId',
				name : 'spj.offerId'
			})
	// 人员姓名
	var worker = {
		fieldLabel : '人员姓名',
		name : 'workerCode',
		xtype : 'combo',
		id : 'workerCode',
		store : new Ext.data.SimpleStore({
					fields : ['id', 'name'],
					data : [[]]
				}),
		mode : 'remote',
		hiddenName : 'spj.workerCode',
		allowBlank : false,
		editable : false,
		anchor : "85%",
		triggerAction : 'all',
		onTriggerClick : function() {
			
		}
	};

	// 操作项目
	var operPro = new Ext.form.TextField({
				fieldLabel : '操作项目',
				id : 'projectOperation',
				width : 180,
				readOnly : true,
				name : 'spj.projectOperation'
			})
	// 岗位年限
	var postYear = new Ext.form.NumberField({
				fieldLabel : '岗位年限',
				id : 'postYear',
				labelAlign : 'right',
				width : 180,
				name : 'spj.postYear',
				allowNegative : false,
				allowDecimals : false,
				readOnly : true,
				maxValue : 99
			})

	// 体检日期
	var medicalDate = new Ext.form.TextField({
				id : 'medicalDate',
				name : 'spj.medicalDate',
				width : 180,
				readOnly : true,
				fieldLabel : '体检日期'
			});
	// 体检结果
	var medicalResult = new Ext.form.TextField({
				fieldLabel : "体检结果",
				id : 'medicalResult',
				name : 'spj.medicalResult',
				width : 180,
				readOnly : true,
				isFormField : true

			})

	// 证书名称
	var offerName = new Ext.form.TextField({
				fieldLabel : '证书名称',
				id : 'offerName',
				name : 'spj.offerName',
				readOnly : true,
				width : 180
			})
	// 证书编号
	var offerCode = new Ext.form.TextField({
				fieldLabel : '证书编号',
				id : 'offerCode',
				name : 'spj.offerCode',
				readOnly : true,
				width : 180
			})
	// 证书发放日期
	var offerDate = new Ext.form.TextField({
				id : 'offerDate',
				name : 'spj.offerDate',
				width : 180,
				readOnly : true,
				fieldLabel : '证书发放日期'
			});

	// 证书有效期限 从
	var offerStartDate = new Ext.form.TextField({
				id : 'offerStartDate',
				name : 'spj.offerStartDate',
				width : 180,
				readOnly : true,
				fieldLabel : '证书有效期限'
			});
	// 证书有效期限 至
	var offerEndDate = new Ext.form.TextField({
				id : 'offerEndDate',
				name : 'spj.offerEndDate',
				width : 180,
				readOnly : true,
				fieldLabel : '至'
			});
	var memo = new Ext.form.TextArea({
				id : 'memo',
				name : "spj.memo",
				fieldLabel : '备注',
				readOnly : true,
				width : 180

			})
	// Form
	var addPanel = new Ext.FormPanel({
				frame : true,
				labelAlign : 'right',
				items : [offerId, worker, operPro, postYear, medicalDate,
						medicalResult, offerName, offerCode, offerDate,
						offerStartDate, offerEndDate, memo]
			});

	// 弹出窗口
	var win = new Ext.Window({
				width : 360,
				height : 430,
				modal : true,
				buttonAlign : 'center',
				items : [addPanel],
				layout : 'fit',
				closeAction : 'hide',
				resizable : false
			});

	/**
	 * 
	 * 查询记录
	 */
	function queryRecord() {
		// 查询参数
		var workerName = txtFuzzy1.getValue();
		queryStore.baseParams = {
			workerName : workerName
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
	 * 修改记录
	 */
	function updateRecord() {
		var records = grid.getSelectionModel().getSelections();
		var recordslen = records.length;
		if (recordslen > 1) {
			Ext.Msg.alert("提示信息", "请选择其中一项进行编辑！");
		} else {
			var rec = grid.getSelectionModel().getSelected();
			if (!rec) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
				return false;
			} else {
				strMethod = "update";
				addPanel.getForm().reset();
				win.setTitle("查看特种作业人员详细信息");
				win.show();
				Ext.get("spj.workerCode").dom.value = rec.get('spj.workerCode');
				Ext.get('offerId').dom.value = rec.get('spj.offerId');
				Ext.get('workerCode').dom.value = rec.get('spj.workerCode') == null
						? ""
						: rec.get('workerName');
				Ext.get('projectOperation').dom.value = rec
						.get('spj.projectOperation') == null ? "" : rec
						.get('spj.projectOperation');
				Ext.get('postYear').dom.value = rec.get('spj.postYear') == null
						? ""
						: rec.get('spj.postYear');
				Ext.get('medicalDate').dom.value = rec.get('medicalDate') == null
						? ""
						: rec.get('medicalDate');
				Ext.get('medicalResult').dom.value = rec
						.get('spj.medicalResult') == null ? "" : rec
						.get('spj.medicalResult');
				Ext.get('offerName').dom.value = rec.get('spj.offerName') == null
						? ""
						: rec.get('spj.offerName');
				Ext.get('offerCode').dom.value = rec.get('spj.offerCode') == null
						? ""
						: rec.get('spj.offerCode');
				Ext.get('offerDate').dom.value = rec.get('offerDate') == null
						? ""
						: rec.get('offerDate');
				Ext.get('offerStartDate').dom.value = rec.get('offerStartDate') == null
						? ""
						: rec.get('offerStartDate');
				Ext.get('offerEndDate').dom.value = rec.get('offerEndDate') == null
						? ""
						: rec.get('offerEndDate');
				Ext.get('memo').dom.value = rec.get('spj.memo') == null
						? ""
						: rec.get('spj.memo');
			}
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