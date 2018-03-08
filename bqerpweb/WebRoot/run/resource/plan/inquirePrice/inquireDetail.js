Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Constants.PAGE_SIZE = 18;

function openAnnex(url)
{
	window.open(url);
}

Ext.onReady(function() {
	Ext.QuickTips.init();
	var args = window.dialogArguments;
	var storeData = args.storeData;
	var gatherIds = args.gatherIds;
	var gatherId = "";
	var gatherNum = "";
	var gatherMetail="";
	var gatherSpeno="";
	var deleteMaterialIds = [];
	var totalCount = 0;
	
	
	/**
	 * 金钱格式化
	 */
	function moneyFormat(v) {
		if (v == null || v == "") {
			return "0.0000";
		}
		v = (Math.round((v - 0) * 10000)) / 10000;
		v = (v == Math.floor(v))
				? v + ".0000"
				: ((v * 10 == Math.floor(v * 10))
						? v + "000"
						: ((v * 100 == Math.floor(v * 100)) ? v + "00" : ((v
								* 1000 == Math.floor(v * 1000)) ? v + "0" : v)));
		v = String(v);
		var ps = v.split('.');
		var whole = ps[0];
		var sub = ps[1] ? '.' + ps[1] : '.0000';
		var r = /(\d+)(\d{3})/;
		while (r.test(whole)) {
			whole = whole.replace(r, '$1' + ',' + '$2');
		}
		v = whole + sub;
		if (v.charAt(0) == '-') {
			return '-' + v.substr(1);
		}
		return v;
	}

	/**
	 * 数字格式化
	 */
	function numberFormat(v) {
		if (v == null || v == "") {
			return "0.00";
		}
		v = (Math.round((v - 0) * 100)) / 100;
		v = (v == Math.floor(v)) ? v + ".00" : ((v * 10 == Math.floor(v * 10))
				? v + "0"
				: v);
		v = String(v);
		var ps = v.split('.');
		var whole = ps[0];
		var sub = ps[1] ? '.' + ps[1] : '.00';
		var r = /(\d+)(\d{3})/;
		while (r.test(whole)) {
			whole = whole.replace(r, '$1' + ',' + '$2');
		}
		v = whole + sub;
		if (v.charAt(0) == '-') {
			return '-' + v.substr(1);
		}
		return v;
	}
	/**
	 * 去掉时间中T
	 */
	function renderDate(value) {
		if (!value)
			return "";

		var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
		var reTime = /\d{2}:\d{2}:\d{2}/gi;

		var strDate = value.match(reDate);
		var strTime = value.match(reTime);
		if (!strDate)
			return "";
		return strTime ? strDate : strDate;
	}
	/**
	 * 去掉时间中T
	 */
	function formatTime(value) {
		if (!value)
			return "";

		var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
		var reTime = /\d{2}:\d{2}:\d{2}/gi;

		var strDate = value.match(reDate);
		var strTime = value.match(reTime);
		if (!strDate)
			return "";
		return strTime ? strDate + " " + strTime : strDate;
	}
	
	/**
	 * 获取当前时间
	 */
	function getCurrentDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t;
		return s;
	}
		
	// 供应商
	var supplier2 = new Ext.form.TextField({
				fieldLabel : '供应商',
				readOnly : true,
				id : "supplier2",
				name : "mrBy",
				anchor : '90%'
			});
	supplier2.onClick(selectSupplierWin);
	var supplierH = new Ext.form.Hidden();
	
	var stocktbar = new Ext.Toolbar({
		height : 25,
		items : ['供应商:', supplier2,{text : '刷新',
		iconCls : 'reflesh',
			handler : function() {
				stockStore.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});
			}
		}]
	});
	var ContentRecord = Ext.data.Record.create([{

		name : 'gatherId'
	}, {
		name : 'materialName'
	}, {
		name : 'approvedQty'
	},
	{name:'specNo'}]);

	var stockStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({}),
		reader : new Ext.data.JsonReader({
			root : "list",
			totalProperty : "totalCount"
		}, ContentRecord)
	});
	

	// 汇总panel
	var stockPanel = new Ext.grid.GridPanel({
		layout : 'fit',
		buttonAlign : 'center',
		bodyStyle : 'width:100%',
		border : true,
		fitToFrame : true,
		autoWidth : true,
		// 标题不可以移动
		enableColumnMove : false,
		store : stockStore,
		// 单选
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
		columns : [new Ext.grid.RowNumberer({
			header : "行号",
			align : 'right',
			width : 35
		}), {
			header : "汇总ID",
			width : 30,
			sortable : true,
			align : 'left',
			defaultSortable : true,
			dataIndex : 'gatherId'
		}, {
			header : "物资名称",
			width : 65,
			sortable : true,
			align : 'left',
			dataIndex : 'materialName'
		}],
		viewConfig : {
			forceFit : true,
			autoFill : true
		},
		tbar : stocktbar,
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : Constants.PAGE_SIZE,
			store : stockStore,
			displayInfo : true,
			displayMsg : '共 {2} 条',
			emptyMsg : Constants.EMPTY_MSG
		})
	})
	// 双击事件
	stockPanel.on('rowclick', submitBtn);
	function submitBtn() {
		var sm = stockPanel.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		if (stockPanel.selModel.hasSelection()) {
			var data = selected[0];
			gatherId = data.get("gatherId");
			gatherNum = data.get("approvedQty");
			 gatherMetail=data.get("materialName");
	        gatherSpeno=data.get("specNo");
			rightStore.load({
				params : {
					gatherIds : gatherId,
					start : 0,
					limit : 18
				}
			});
		} else {
			Ext.Msg.alert("提示", "请选择物资!");
		}
	}

	// add by liuyi 091103 批处理询价有效开始时间
	var startDate = new Ext.form.TextField({
				itemCls : 'sex-left',
				readOnly : true,
				clearCls : 'allow-float',
				checked : true,
				width : 80,
				style : 'cursor:pointer',
				listeners : {
					focus : function() {
						WdatePicker({
							// 时间格式
							startDate : '%y-%M-%d',
							dateFmt : 'yyyy-MM-dd',
							alwaysUseStartDate : false,
							onclearing : function() {
//								rightGrid.getSelectionModel().getSelected()
//										.set("effectStartDate", "")
							}
//							,
//							onpicked : checkTime1
						});
					}
				}
			})
	// add by liuyi 091103 批处理询价有效开始时间
	var endDate = new Ext.form.TextField({
				itemCls : 'sex-left',
				readOnly : true,
				clearCls : 'allow-float',
				checked : true,
				width : 80,
				style : 'cursor:pointer',
				listeners : {
					focus : function() {
						WdatePicker({
							// 时间格式
							startDate : '%y-%M-%d',
							dateFmt : 'yyyy-MM-dd',
							alwaysUseStartDate : false,
							onclearing : function() {
//								rightGrid.getSelectionModel().getSelected()
//										.set("effectStartDate", "")
							}
//							,
//							onpicked : checkTime1
						});
					}
				}
			})
	var btnAdd = new Ext.Button({
		text : "增加明细",
		iconCls : "add",
		id : "btnAdd",
		disabled : false,
		handler : addInquireDetail
	});
	// 保存按钮
	var btnSave = new Ext.Button({
		text : Constants.BTN_SAVE,
		iconCls : Constants.CLS_SAVE,
		id : "btnSave",
		disabled : false,
		handler : saveForm
	});

	// 删除按钮
	var btnDelete = new Ext.Button({
		text : Constants.BTN_DELETE,
		iconCls : Constants.CLS_DELETE,
		disabled : false,
		handler : deleteInquireDetail
	});
	
	//是否选择供应商
	var btnSelectSupply = new Ext.Button({
		text : "设置为供应商",
		iconCls : "add",
		disabled : false,
		handler : selectSupply
	});
	// add by liuyi 20100407 开始
	
	var fileName = new Ext.form.TextField({
		id : "fileName",
	//	xtype : "textfield",
		inputType : "file",
		fieldLabel : '附件<a  style="cursor:hand;color:black" id="a_viewFileContent" >【查看】</a>',
		width : 300,
		name : 'solutionFile' 
	});

	var fileform = new Ext.FormPanel({
		frame : true,
		fileUpload : true,
		labelAlign : 'center',
		items : [fileName]
	});
	var filewin = new Ext.Window({
				title : '增加附件',
				id : 'win',
				modal : true,
				autoHeight : true,
				width : 450,
				closeAction : 'hide',
				items : [fileform],
				buttonAlign : 'center',
				buttons : [{
					text : '保存',
					handler : function() {
						var addr = Ext.get("fileName").dom.value;
						if(addr == null || addr == ''){
							Ext.Msg.alert('提示','无附件可上传！');
							return;
						}
						var myurl = "resource/addInquireDetailsFile.action";
						var detailId = rightGrid.getSelectionModel().getSelected().get('inquireDetailId');
						fileform.form.submit({
									method : 'POST',
									url : myurl,
									params : {
										'detailId' : detailId,
										'filepath' : Ext.get("fileName").dom.value
									},
									success : function(form, action) {
										var o = eval("("
												+ action.response.responseText
												+ ")");
										Ext.Msg.alert("注意", '附件上传成功！');
										if (o && o.msg && o.msg.indexOf('成功') != -1) {
											Ext.get("fileName").dom.select();
											document.selection.clear();
										}
										filewin.hide();
										rightStore.reload();

									},
									faliue : function() {
										Ext.Msg.alert('错误', '出现未知错误.');
									}
								});
								
//						if (Ext.get("equSolutionId").dom.value == "自动生成") {
//							myurl = "equbug/addSolution.action";
//						} else {
//							myurl = "equbug/updateSolution.action";
//						}
//						var myfiletype = "";
//						if (Ext.get("fileName").dom.value != "") {
//							myfiletype = Ext.get("fileName").dom.value;
//							myfiletype = myfiletype.substring(myfiletype
//											.lastIndexOf(".")
//											+ 1, myfiletype.length);
//						}
						
					}
				}, {
					text : '取消',
					handler : function() {
						filewin.hide();
					}
				}]
			});
	// 上传附件按钮
	var btnAnnexAdd = new Ext.Button({
		id : 'btuAnnexAdd',
		text : '增加附件',
//		iconCls : 'add',
		handler : function() {
			var sm = rightGrid.getSelectionModel();
			var selected = sm.getSelections();
//			var detailId;
			var menber = selected[0];
			if (rightGrid.selModel.hasSelection()) {
				if (confirm() == false)
					return;
				if (selected.length != 1) {
					Ext.Msg.alert("提示", "请选择一条记录！");
					return;
				}
				var fileAddr = menber.get('filePath');
				if(fileAddr != null && fileAddr != '')
				{
					Ext.Msg.alert('提示','该条数据附件已存在，请先删除！');
					return;
				}
				filewin.show();
			} else {
				Ext.Msg.alert("提示", "请先选择要编辑的行!");
			}

		}
	})
	var btuAnnexDel = new Ext.Button({
		id : 'btuAnnexDel',
		text : '删除附件',
//		iconCls : 'delete',
		handler : function(){
			var sm = rightGrid.getSelectionModel();
			var selected = sm.getSelections();
			var detailId;
			var menber = selected[0];
			if (rightGrid.selModel.hasSelection()) {
				if (confirm() == false)
					return;
				if (selected.length != 1) {
					Ext.Msg.alert("提示", "请选择一条记录！");
					return;
				}
				detailId = menber.get('inquireDetailId');
				Ext.Ajax.request({
					url : 'resource/deleteInquireFile.action',
					method : 'post',
					params : {
						detailId : detailId
					},
					success : function(request,reponse){
						Ext.Msg.alert('提示','附件删除成功！');
						rightStore.reload();
					},
					failure : function(request,reponse){
						Ext.Msg.alert('提示','附件删除失败！');
					}
				})
			} else {
				Ext.Msg.alert("提示", "请先选择要删除附件的行!");
			}

		}
	})
	// add by liuyi 20100407 结束
	var rightStoretbar = new Ext.Toolbar({
		height : 25,
		items : ['有效开始时间：',startDate,'-',
		'有效结束时间：',endDate,
		btnAdd, '-', btnSave, '-', btnDelete,'-',btnSelectSupply,'-',
		
		{
			text:'刷新',
			iconCls : "reflesh",
			handler:function()
			{
					if (gatherId != null && gatherId != "") {
						rightStore.load({
							params : {
								gatherIds : gatherId,
								start : 0,
								limit : 18
							}
						});
					} else {
						rightStore.load({
							params : {
								gatherIds : gatherIds,
								start : 0,
								limit : 18
							}
						});
					}
			}
		
		}
		// add by liuyi 20100407 关于附件
		,'-',btnAnnexAdd,'-',btuAnnexDel 
		]
	});

	var rightData = Ext.data.Record.create([{
		name : 'inquireSupplier'
	}, {name:'materialName'},
		{
		name : 'supplyName'
	}, {
		name : 'inquireQty'
	}, {
		name : 'unitPrice'
	}, {
		name : 'totalPrice'
	}, 
	{name:'specNo'},
			{
		name : 'modifyByName'
	}, {
		name : 'memo'
	}, {
		name : 'qualityTime'
	}, {
		name : 'offerCycle'
	}, {
		name : 'isSelectSupplier'
	}, {
		name : 'gatherId'
	}, {
		name : 'effectStartDate'
	}, {
		name : 'effectEndDate'
	}, {
		name : 'modifyDate'
	}, {
		name : 'inquireDetailId'
	}
	// add by liuyi 20100409 附件
	,{
		name : 'filePath'
	}
	]);
	
	var RightDataProxy = new Ext.data.HttpProxy(
			{
				url:'resource/findPlanInquireListForBill.action'
			}
	);

	var RightTheReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, rightData);

	var  rightStore = new Ext.data.Store({
		proxy : RightDataProxy,
		reader : RightTheReader
	});
	// modify by liuyi 091022
//	rightStore.load({
//		params : {
//			gatherIds : gatherIds,
//			start : 0,
//			limit : 18
//		}
//	});
	/**
	 * 明细部分日期check
	 */
	function checkTime2() {
		var startDate = getCurrentDate();
		var endDate = this.value;
		rightGrid.getSelectionModel().getSelected().set("qualityTime", endDate);
	}

	function checkTime1() {
		var startDate = getCurrentDate();
		var endDate = this.value;
		rightGrid.getSelectionModel().getSelected().set("effectStartDate",
				endDate);
	}
	function checkTime3() {
		var startDate = getCurrentDate();
		var endDate = this.value;
		rightGrid.getSelectionModel().getSelected().set("effectEndDate",
				endDate);
	}
	var rsm = new Ext.grid.RowSelectionModel({
		ingleSelect : false
	});

	  var rightsm=new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	});
	// 详细grid
	var rightGrid = new Ext.grid.EditorGridPanel({
		//layout : 'fit',
		region : "center",
	//	anchor : "100%",
	//	autoScroll : true,
	//	style : "border-top:solid 1px",
	//	border : false,
	//	autoScroll : true,
	//	enableColumnMove : false,
		sm : rightsm,
		clicksToEdit : 1,
		store : rightStore,
		columns : [rightsm,new Ext.grid.RowNumberer({
			header : "行号",
			align : 'right',
			width : 35
		}),
		{
			header : "物资名称",
			width : 100,
			align : 'left',
			sortable : true,
			readOnly:true,
			dataIndex : 'materialName'
		},
			{
			
			header : "规格型号",
			width : 60,
			align : 'left',
			sortable : true,
			readOnly:true,
			dataIndex : 'specNo'
		},{
			header : "供应商",
			width : 120,
			align : 'left',
			sortable : true,
			css : CSS_GRID_INPUT_COL,
			dataIndex : 'supplyName'
		}
		, {
			header : "数量",
			width : 70,
			align : 'left',
			sortable : true,
			css : CSS_GRID_INPUT_COL,
			editor : new Ext.form.NumberField({
				maxValue : 99999999999.99,
				minValue : 0,
				decimalPrecision : 4
			}),
			renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
				if (rowIndex < store.getCount()) {
					return moneyFormat(value)
				}
			},
			dataIndex : 'inquireQty'
		}
		, {
			header : "单价",
			width : 70,
			align : 'left',
			sortable : true,
			renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
				if (rowIndex < store.getCount()) {
					return moneyFormat(value)
				}
			},
			css : CSS_GRID_INPUT_COL,
			editor : new Ext.form.NumberField({
				maxValue : 99999999999.9999,
				minValue : 0,
				decimalPrecision : 4
			}),
			dataIndex : 'unitPrice'
		}
		,{
			header : "报价有效开始日期",
			width : 120,
			align : 'left',
			sortable : true,
			renderer : renderDate,
			css : CSS_GRID_INPUT_COL,
			editor : new Ext.form.TextField({
				format : 'Y-m-d',
				itemCls : 'sex-left',
				readOnly : true,
				clearCls : 'allow-float',
				checked : true,
				width : 100,
				anchor : '100%',
				style : 'cursor:pointer',
				listeners : {
					focus : function() {
						WdatePicker({
							// 时间格式
							startDate : '%y-%M-%d',
							dateFmt : 'yyyy-MM-dd',
							alwaysUseStartDate : false,
							onclearing : function() {
								rightGrid.getSelectionModel().getSelected()
										.set("effectStartDate", "")
							},
							onpicked : checkTime1
						});

					}

				}

			}),
			dataIndex : 'effectStartDate'
		}, {
			header : "报价有效结束日期",
			width : 120,
			align : 'left',
			sortable : true,
			renderer : renderDate,
			css : CSS_GRID_INPUT_COL,
			editor : new Ext.form.TextField({
				format : 'Y-m-d',
				itemCls : 'sex-left',
				readOnly : true,
				clearCls : 'allow-float',
				checked : true,
				width : 100,
				anchor : '100%',
				style : 'cursor:pointer',
				listeners : {
					focus : function() {
						WdatePicker({
							// 时间格式
							startDate : '%y-%M-%d',
							dateFmt : 'yyyy-MM-dd',
							alwaysUseStartDate : false,
							onclearing : function() {
								rightGrid.getSelectionModel().getSelected()
										.set("effectEndDate", "")
							},
							onpicked : checkTime3
						});
					}
				}
			}),
			dataIndex : 'effectEndDate'
		},  {
			header : "总价",
			width : 70,
			align : 'left',
			sortable : true,
			renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {

				if (rowIndex < store.getCount()) {

					if (record.data.inquireQty != null && record.data.unitPrice != null) {								
						var subSum = record.data.inquireQty * record.data.unitPrice;								
						return moneyFormat(subSum);
					} else {                      
						return moneyFormat(0);
					}
				}
			},
			dataIndex : 'totalPrice'
		},
		
		 {
			header : "质保期",
			width : 70,
			align : 'right',
			sortable : true,
			renderer : renderDate,
			css : CSS_GRID_INPUT_COL,
			editor : new Ext.form.TextField({
				format : 'Y-m-d',
				itemCls : 'sex-left',
				readOnly : true,
				clearCls : 'allow-float',
				checked : true,
				width : 100,
				anchor : '100%',
				style : 'cursor:pointer',
				listeners : {
					focus : function() {
						WdatePicker({
							// 时间格式
							startDate : '%y-%M-%d',
							dateFmt : 'yyyy-MM-dd',
							alwaysUseStartDate : false,
							onclearing : function() {
								rightGrid.getSelectionModel().getSelected()
										.set("qualityTime", "")
							},
							onpicked : checkTime2
						});
					}
				}
			}),
			dataIndex : 'qualityTime'
		}, {
			header : "供货周期",
			width : 70,
			align : 'right',
			sortable : true,
			css : CSS_GRID_INPUT_COL,
			editor : new Ext.form.TextField({
				maxLength : 100
			}),
			renderer : function(v, cellmeta, record, rowIndex, columnIndex,
					store) {
				if (rowIndex = store.getCount()) {
				}
				return v;
			},
			dataIndex : 'offerCycle'
		},{
			header : "询价人",
			width : 70,
			align : 'right',
			sortable : true,
			dataIndex : 'modifyByName'
		}, {
			header : "是否选择供应商",
			width : 70,
			align : 'left',
			sortable : true,
			dataIndex : 'isSelectSupplier',
			renderer : function(v) {
				if (v == 'Y') {
					return "是";
				}
				if (v == 'N') {
					return "否";
				}
			}
		}
				  
		, {
			header : "备注",
			width : 100,
			align : 'left',
			css : CSS_GRID_INPUT_COL,
			sortable : true,
			dataIndex : 'memo'
		}, {
			header : "汇总ID",
			width : 50,
			align : 'right',
			hidden : true,
			sortable : true,
			dataIndex : 'gatherId'
		}
		// add by liuyi 20100409 
		, {
			header : "附件",
			sortable : true,
			dataIndex : 'filePath',
			renderer:function(v){
				if(v !=null && v !='')
				{   
					var s =  '<a href="javascript:openAnnex(\''+v+'\');">[查看]</a>';
					return s;
				}
			} 
		} 
		],
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : Constants.PAGE_SIZE,
			store : rightStore,
			displayInfo : true,
			displayMsg : '共 {2} 条',
			emptyMsg : Constants.EMPTY_MSG
		})
	});
	

	rightGrid.on('cellclick', choseEdit);
	rightGrid.on('celldblclick', editMemo);

	// 备注
	var memoText = new Ext.form.TextArea({
		id : "memoText",
		maxLength : 250,
		width : 180
	});

	// 弹出画面
	var win = new Ext.Window({
		height : 170,
		width : 350,
		layout : 'fit',
		resizable : false,
		modal : true,
		closeAction : 'hide',
		items : [memoText],
		buttonAlign : "center",
		title : '详细信息录入窗口',
		buttons : [{
			text : Constants.BTN_SAVE,
			iconCls : Constants.CLS_SAVE,
			handler : function() {
				var rec = rightGrid.getSelectionModel().getSelections();
				if (Ext.get("memoText").dom.value.length <= 250) {
					rec[0].set("memo", Ext.get("memoText").dom.value);
					win.hide();
				}
			}
		}, {
			text : Constants.BTN_CANCEL,
			iconCls : Constants.CLS_CANCEL,
			handler : function() {
				win.hide();
			}
		}]
	});
	
	var leftPanel = new Ext.Panel({
		region : 'west',
		layout : 'fit',
		width : 240,
		autoScroll : true,
		border : false,
		containerScroll : true,
		collapsible : true,
		split : false,
		items : [stockPanel]
	});
	// 右边的panel
	var rightPanel = new Ext.Panel({
		region : "center",
		layout : 'border',
		border : true,
		containerScroll : true,
		collapsible : true,
		tbar : rightStoretbar,
		items : [rightGrid]
	});
	
	// 显示区域
	var view = new Ext.Viewport({
		enableTabScroll : true,
		autoScroll : true,
		layout : "border",
		items : [leftPanel, rightPanel]
	})

	function detailCheck(record, index) {
		var msg = "";
		index = index;
		if (record['supplyName'] == "" || record['supplyName'] == null)
			msg += String.format(Constants.COM_E_002, "供应商") + "<br />";
		if (record['unitPrice'] != "" && record['unitPrice'] != null)
		{
			if(record['effectStartDate']==""||record['effectStartDate']==null)
			msg += String.format(Constants.COM_E_002, "报价有效开始日期") + "<br />";
			
			if(record['effectEndDate']==""||record['effectEndDate']==null)
			msg += String.format(Constants.COM_E_002, "报价有效结束日期") + "<br />";
			
			if(record['effectStartDate']!=null&&record['effectStartDate']!=""&&record['effectEndDate']!=null&&record['effectEndDate']!="")
			{
				var dateStart = Date.parseDate(record['effectStartDate'], 'Y-m-d');
				var dateEnd = Date.parseDate(record['effectEndDate'], 'Y-m-d');
				if (dateStart.getTime() > dateEnd.getTime()) {
					msg += "报价有效结束日期必须大于报价有效开始日期" + "<br />";
				}
			}
			
		}
		return msg;
	}
	
	function isRight() {
		var msg = "";
		var msgSub = "";
		for (var index = 0; index < rightStore.getCount(); index++) {
			var record = rightStore.getAt(index).data;
			msgSub = detailCheck(record, index);
			if (msgSub != "") {
				msg = msg  + msgSub
				break;
			}
		}
		if (msg.trim() != "") {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, msg);
			return false;
		}
	}

	/**
	 * 保存
	 */
	function saveForm() {
		saveModifyRecords();
	}
	function saveModifyRecords() {
		rightGrid.stopEditing();
		if (isRight() == false) {
			return;
		} else {
			Ext.Msg.wait("正在保存数据,请等待...");
			var modifyRec = rightGrid.getStore().getModifiedRecords();
			if (modifyRec.length == 0 && deleteMaterialIds.length == 0) {
				Ext.Msg.hide();
				return;
			}
			var modifyRecords = new Array();
			for (var i = 0; i < modifyRec.length; i++) {
				modifyRecords.push(modifyRec[i].data);
			}
			Ext.Ajax.request({
				url : 'resource/modifyContents.action',
				method : 'post',
				params : {
					addOrUpdateRecords : Ext.util.JSON.encode(modifyRecords),
					deleteRecords : deleteMaterialIds.join(",")
				},
				success : function(result, request) {
					Ext.MessageBox.alert('提示', '操作成功！');
					if (gatherId != null && gatherId != "") {
						rightStore.load({
							params : {
								gatherIds : gatherId,
								start : 0,
								limit : 18
							}
						});
					} else {
						rightStore.load({
							params : {
								gatherIds : gatherIds,
								start : 0,
								limit : 18
							}
						});
					}
					deleteMaterialIds = [];
					rightGrid.getStore().rejectChanges();
					window.returnValue="ok";
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('提示', '操作失败！');
					rightGrid.getStore().rejectChanges();
					deleteMaterialIds = [];
				}
			});
		}
	}
	/**
	 * 删除明细
	 */
	function deleteInquireDetail(obj, e) {
		rightGrid.stopEditing();
		var sm = rightGrid.getSelectionModel();
		var selections = sm.getSelections();
		var len = selections.length;
		if (len == 0) {
			Ext.Msg.alert("提示", "请选中您要删除的记录!");
		} else {
			for (var i = 0; i < len; i++) {
				var record = selections[i];
				if (record.get("inquireDetailId") != ""
						&& record.get("inquireDetailId") != null) {
					deleteMaterialIds.push(record.get("inquireDetailId"));
				}
				rightStore.remove(selections[i]); 
				rightStore.getModifiedRecords().remove(record);
			}
		}
	};

	// 加载左边GRID数据
	for (var i = 0; i < storeData.length; i++) {
		stockStore.add(new ContentRecord(storeData[i]));
	}
	/**
	 * 增加明细
	 */
	
	function addInquireDetail() {
//=data.get("materialName");
	  //      gatherSpeno=data.get("specNo");
		// 增加单条新记录
		if (gatherId != "" || gatherNum != "") {
			var myrightRecord = new rightData({
				inquireSupplier : supplierH.getValue(),
				materialName: gatherMetail,
				supplyName : supplier2.getValue(),
				inquireQty : gatherNum,
				unitPrice : "",
				totalPrice : "",
				specNo:gatherSpeno,
				modifyByName : "",
				memo : "",
				qualityTime : "",
				offerCycle : "",
				isSelectSupplier : "",
				gatherId : "",
				effectStartDate : startDate.getValue(),
				effectEndDate : endDate.getValue(),
				modifyDate : "",
				inquireDetailId : ""
			});
			// 原数据个数
			var mycount = rightGrid.getStore().getCount();
			rightGrid.stopEditing();
			rightStore.insert(mycount, myrightRecord);
			myrightRecord.set('gatherId',gatherId);
		}// 增加多条新记录
		else {
			for (var i = 0; i < storeData.length; i++) {
				var myrightRecord = new rightData({
					inquireSupplier : supplierH.getValue(),//把选择的供应商的值付过来
					materialName: storeData[i].materialName,
					supplyName :supplier2.getValue(),
					inquireQty : storeData[i].approvedQty,//从STORE中取值
					unitPrice : "",
					totalPrice : "",
					specNo:storeData[i].specNo,
					modifyByName : "",
					memo : "",
					qualityTime : "",
					offerCycle : "",
					isSelectSupplier : "",
					gatherId : "",
					effectStartDate : startDate.getValue(),
					effectEndDate : endDate.getValue(),
					modifyDate : "",
					inquireDetailId : ""
				});
				// 原数据个数
				var mycount = rightGrid.getStore().getCount();
				rightGrid.stopEditing();
				rightStore.insert(mycount, myrightRecord);
				myrightRecord.set('gatherId',storeData[i].gatherId);
			}
		}
	}
	/**
	 * 双击修改明备注
	 */
	function editMemo(grid, rowIndex, columnIndex, e) {
		var record = grid.getStore().getAt(rowIndex);
		var fieldName = grid.getColumnModel().getDataIndex(columnIndex);
		if (fieldName == "memo") {
			win.show();
			memoText.setValue(record.get("memo"));
			memoText.focus(false);
		}
	}
	function confirm() {
		var sm = rightGrid.getSelectionModel();
		var selected = sm.getSelections();
		var menber = selected[0];
		var msg = "";
		var modifyRec = rightGrid.getStore().getModifiedRecords();
		if(modifyRec!=null&&modifyRec.length!=0)
		{
		  	Ext.Msg.alert('提示', "数据已改变，请先保存！");
			return false;
		}
		if (menber.get("inquireDetailId") == null||menber.get("inquireDetailId") == "") 
		{
			msg = "请先保存新增的记录!"+ "<br /><br />";
		}
		if (menber.get("unitPrice") == ""||menber.get("unitPrice") == null) 
		{
			if (msg != "") {
				msg = msg + "单价不能为空!";
			} else {
				msg = "单价不能为空!";
			}
		}
		
		if (msg != "") {
			Ext.Msg.alert('提示', "" +msg + "");
			return false;
		}
		return true;
	}
	//设置为供应商
	function selectSupply()
	{
		var sm = rightGrid.getSelectionModel();
		var selected = sm.getSelections();
		
		var detailId;
		var menber = selected[0];
		if (rightGrid.selModel.hasSelection()) {
			if (confirm() == false)
			return;
			
			
			if(selected.length!=1)  
			{
			
			Ext.Msg.alert("提示", "请选择一条记录！");
			return;
			}
			detailId = menber.get('inquireDetailId');
			Ext.Msg.confirm("提示", "是否确定选择该供应商?", function(buttonobj) {
				if (buttonobj == "yes") {
					Ext.Msg.wait("操作进行中...", "请稍候");
					Ext.Ajax.request({
						method : 'post',
						url : 'resource/chooseSupplierForInquire.action',
						params : {
							detailId : detailId
						},
						success : function(action) {
							Ext.Msg.alert("提示", "供应商设置成功！");
							if (gatherId != null && gatherId != "") {
								rightStore.load({
									params : {
										gatherIds : gatherId,
										start : 0,
										limit : 18
									}
								});
							} else {
								rightStore.load({
									params : {
										gatherIds : gatherIds,
										start : 0,
										limit : 18
									}
								});
							} 
							window.returnValue=true;
//							window.close();
						},
						failure : function() {
							Ext.Msg.alert(Constants.SYS_REMIND_MSG,
									Constants.COM_E_014);
						}
					});
				}
			})
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}
	
	/**
	 * 供应商
	 */
	function choseEdit(rightGrid, rowIndex, columnIndex, e) {
		var record = rightGrid.getStore().getAt(rowIndex);
		var fieldName = rightGrid.getColumnModel().getDataIndex(columnIndex);
		if (fieldName == "supplyName") {
			var supply = window
					.showModalDialog(
							'../../../../comm/jsp/supplierQuery/supplierQuery.jsp',
							null,
							'dialogWidth=750px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no');
			if (typeof(supply) != "undefined") {

				record.set('supplyName', supply.supplyName == null
						? ""
						: supply.supplyName);
				record.set('inquireSupplier', supply.supplierId);
			}
		}
		if (fieldName == "clickToChose") {
			choseSpare();
		}
	}
	/**
	 * 供应商选择页面
	 */
	function selectSupplierWin() {
		var args = {
			selectModel : 'signal',
			notIn : "",
			rootNode : {
				id : '-1',
				text : '合肥电厂'
			}
		}
		var supplier = window
				.showModalDialog(
						'../../../../comm/jsp/supplierQuery/supplierQuery.jsp',
						args,
						'dialogWidth:750px;dialogHeight:500px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(supplier) != "undefined") {
			supplier2.setValue(supplier.supplyName);
			supplierH.setValue(supplier.supplierId);
		}
	}
});