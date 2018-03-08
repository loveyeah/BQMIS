Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
	// 对应变更
	
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
function numberFormat(value){
	
			if(value===""||value==null){
				return value
			}
		    value = String(value);
            // 整数部分
            var whole = value;
            // 小数部分
            var sub = ".00";
            // 如果有小数
		    if (value.indexOf(".") > 0) {
		    	whole = value.substring(0, value.indexOf("."));
			    sub = value.substring(value.indexOf("."), value.length);
			    sub = sub + "00";
			    if(sub.length > 3){
			    	sub = sub.substring(0,3);
			    }
		    }
            var r = /(\d+)(\d{3})/;
            while (r.test(whole)){
                whole = whole.replace(r, '$1' + ',' + '$2');
            }
            v = whole + sub;
            return v;	
	}
	function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";
		// 初始化时间
		Year = DateIn.getYear();
		Month = DateIn.getMonth() + 1;
		Day = DateIn.getDate();
		CurrentDate = Year + "-";
		if (Month >= 10) {
			CurrentDate = CurrentDate + Month + "-";
		} else {
			CurrentDate = CurrentDate + "0" + Month + "-";
		}
		if (Day >= 10) {
			CurrentDate = CurrentDate + Day;
		} else {
			CurrentDate = CurrentDate + "0" + Day;
		}
		return CurrentDate;
	}
	var enddate = new Date().add(Date.MONTH, +1);
	var startdate = new Date().add(Date.MONTH, -1);
	startdate = startdate.getFirstDateOfMonth();
	var sdate = ChangeDateToString(startdate);
	var edate = ChangeDateToString(enddate);
	var planStartDate = new Ext.form.DateField({
		fieldLabel : '出库时间',
		format : 'Y-m-d',
		name : 'startDate',
		id : 'planStartDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		checked : true,
		allowBlank : false,
		readOnly : true,
		value : sdate,
		emptyText : '请选择',
		anchor : '100%'
	});
	var planEndDate = new Ext.form.DateField({
		fieldLabel : '至',
		format : 'Y-m-d',
		name : 'endDate',
		value : edate,
		id : 'planEndDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		allowBlank : false,
		readOnly : true,
		emptyText : '请选择',
		anchor : '100%'
	});

	// 物料编码 modify by drdu 2009/06/01
	var txtMaterialNo = new Ext.form.TextField({
		fieldLabel : '物料编码',
		isFormField : true,
		id : "materialNo",
		name : "materialNo",
		anchor : '100%'
// ,
// listeners : {
// focus : selectMaterial
// }
	});
	// 物料名称
	var txtMaterialName = new Ext.form.TextField({
		fieldLabel : '物料名称',
		anchor : '100%',
		readOnly : false,
		id : "materialName",
		name : "materialName"
	});
	
		var stateComboBox = new Ext.form.ComboBox({
				id : "stateCob",
				fieldLabel : '是否红单',
				store : [['','所有'],['N','否'],['Y','是']],
				displayField : "name",
				valueField : "value",
				mode : 'local',
				triggerAction : 'all',
				hiddenName:'stateComboBox',
				readOnly : true,
				value:'',
				anchor : '100%'
			});
	// 规格型号
// var txtSpecNo = new Ext.form.TextField({
// fieldLabel : '规格型号',
// readOnly : true,
// anchor : '100%',
// id : "specNo",
// name : 'specNo'
// });
	// 领料单号
	var txtIssueNo = new Ext.form.TextField({
		fieldLabel : '领料单号',
		anchor : '100%',
		id : "issueNo"
	});
	// 替代物料流水号
	var hdnAltMatId = new Ext.form.Hidden({
		id : "materialId",
		name : "materialId"
	});
	
	var btnQuery = new Ext.Button({
		text : "查询",
		// iconCls : 'query',
		minWidth : 70,
		handler : searchBtn
			
	});
	
	var btnClear = new Ext.Button({
		id : 'btnClear',
		text : '清除条件',
		minWidth : 70,
		handler : function() {
			form.getForm().reset();
		}
	});
	
	var btnExport = new Ext.Button({
		id : 'btnExport',
		text : '导  出',
		minWidth : 70,
		handler : function() {
			myExport();
		}
	});

	var btnPrint = new Ext.Button({
		id : 'btnPrint',
		text : '打  印',
		minWidth : 70,
		handler : function() {
		}
	});
	
	// 部门 add by liuyi 091102
	var txtMrDept = new Ext.form.TriggerField({
				fieldLabel : '领料部门',
				id : "mrDeptId",
				displayField : 'text',
				valueField : 'id',
				hiddenName : 'mrDept',
				blankText : '请选择',
				emptyText : '请选择',
				maxLength : 100,
				readOnly : true,
				anchor : '100%'
			});
	txtMrDept.onTriggerClick = choseDept;
	var txtMrDeptH = new Ext.form.Hidden({
				hiddenName : 'mrDept'
			})
		function choseDept() {
		var args = {
			selectModel : 'single',
			rootNode : {
				id : '0',
				text : '合肥电厂'
			}
		}
		var dept = window
				.showModalDialog(
						'../../../../comm/jsp/hr/dept/dept.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(dept) != "undefined") {
			txtMrDept.setValue(dept.names);
			txtMrDeptH.setValue(dept.codes);
		}
	}
	// 物料类别
	var txtMaterialClass = new Ext.ux.ComboBoxTree({
				fieldLabel : '物料类别',
				allowBlank : true,
				id : "materialClassId",
				anchor : '100%',
				displayField : 'text',
				valueField : 'id',
				hiddenName : 'materialClass',
				blankText : '请选择',
				emptyText : '请选择',
				// readOnly : true,
				tree : {
					xtype : 'treepanel',
					// 虚拟节点,不能显示
					rootVisible : false,

					loader : new Ext.tree.TreeLoader({
								dataUrl : 'resource/getMaterialClassList.action'
							}),
					root : new Ext.tree.AsyncTreeNode({
								id : '-1',
								name : '合肥电厂',
								text : '合肥电厂'
							})
				},
				selectNodeModel : 'all'
			});
	// 仓库 DataStore
	var dsDelayStore = new Ext.data.JsonStore({
				root : 'list',
				url : "resource/getWarehouseList.action",
				fields : ['whsNo', 'whsName']
			});
	// 仓库
	dsDelayStore.load();
	dsDelayStore.on('load', function() {
				if (dsDelayStore.getTotalCount() > 0) {
					var recordLocation = dsDelayStore.getAt(0);
					dsDelayStore.remove(recordLocation);
					// var record = new Ext.data.Record({
					// whsNo:"",
					// whsName:"&nbsp"
					// })
					// dsDelayStore.insert(0, record);
				}
			})
	// 仓库组合框
	var cboDelayStore = new Ext.form.ComboBox({
				fieldLabel : "仓库",
				name : "delayStore",
// width : 100,
				anchor : '100%',
				store : dsDelayStore,
				displayField : "whsName",
				valueField : "whsNo",
				mode : 'local',
				triggerAction : 'all',
				readOnly : true
			});
			
			//-------发料人 add by fyyang 20100504--
			var txtSendPerson=new Ext.form.TextField({
			   name:'sendPerson',
			   fieldLabel : "发料人",
			   anchor : '100%'
			});
			//-----add  end -----------------------
			// --------------add by fyyang 091218-------------------------
			var txtGetPerson=new Ext.form.TextField({
			   name:'getPerson',
			   fieldLabel : "领料人",
			   anchor : '100%'
			});
			function choseItem() {

		var item = window
				.showModalDialog(
						'../../../../comm/jsp/item/budget/budgetquery.jsp',
						null,
						'dialogWidth:560px;dialogHeight:440px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(item) != "undefined") {
			txtMrItem.setValue(item.itemName);
			txtMrItemH.setValue(item.itemCode);
			
			
		}
	}

	// 费用来源
	var txtMrItem = new Ext.form.TriggerField({
		fieldLabel : '费用来源<font color ="red">*</font>',
		displayField : 'text',
		valueField : 'id',
		blankText : '请选择',
		emptyText : '请选择',
		anchor : '100%',
		name : "itemName",
		// value : '生产类费用',
		allowBlank : false,
		readOnly : true,
		hidden : false,
		hideLabel : false
	});
	txtMrItem.onTriggerClick = choseItem;
	var txtMrItemH = new Ext.form.Hidden({
		name : 'mr.itemCode',
		value : ''
	})
			
			// -----------------------------------------------------------------
	var content = new Ext.form.FieldSet({
		title : '查询条件',
		height : '100%',
		collapsible : true,
		layout : 'form',
		items : [{
			layout : 'column',
			border : false,
			items : [{
				border : false,
				columnWidth : 0.25,
				layout : 'form',
				items : [planStartDate]
			}, {
				border : false,
				columnWidth : 0.25,
				layout : 'form',
				items : [planEndDate]
			}, 
				{
				border : false,
				columnWidth : 0.24,
				layout : 'form',
				items : [txtIssueNo]
			}
			// by liuyi 05/25/09
			,{
	            border : false,
				columnWidth : 0.01		
			},
				{
				border : false,
				columnWidth : 0.11,
				layout : 'form',
				items : [btnQuery]
			}, {
				border : false,
				columnWidth : 0.14,
				layout : 'form',
				items : [btnClear]
			}]
		},{
			layout : 'column',
			border : false,
			items : [
// {
// border : false,
// columnWidth : 0.25,
// layout : 'form',
// items : [txtMaterialNo]
// },
				{
				border : false,
				columnWidth : 0.25,
				layout : 'form',
				items : [txtMaterialName]
			},
				{
				border : false,
				columnWidth : 0.25,
				layout : 'form',
				items : [stateComboBox]
			}, 
			{
	            border : false,
				columnWidth : 0.24,
				layout : 'form',
				items : [txtMrDept,txtMrDeptH]
			},{
	            border : false,
				columnWidth : 0.01		
			},{
				border : false,
				columnWidth : 0.11,
				layout : 'form',
				items : [btnExport]
			}, {
				border : false,
				columnWidth : 0.13,
				layout : 'form',
				items : [btnPrint]
			}]
		},{
			layout : 'column',
			border : false,
			items : [
				{
				border : false,
				columnWidth : 0.25,
				layout : 'form',
				items : [txtMaterialClass]
			},
				{
				border : false,
				columnWidth : 0.25,
				layout : 'form',
				items : [cboDelayStore]
			}, 
			{
	            border : false,
				columnWidth : 0.24,
				layout : 'form',
				items : [txtGetPerson]
			}]
		},{
			layout : 'column',
			border : false,
			items : [
				{
				border : false,
				columnWidth : 0.25,
				layout : 'form',
				items : [txtMaterialNo]
			},
				{
				border : false,
				columnWidth : 0.25,
				layout : 'form',
				items : [txtMrItem,txtMrItemH]
			}, 
			{
	            border : false,
				columnWidth : 0.24,
				layout : 'form',
				items : [txtSendPerson]
			}]
		}
		]
	});
	var form = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		id : 'shift-form',
		labelWidth : 80,
		autoHeight : true,
		region : 'center',
		border : false,
		items : [content]
	});
	// 物料异动grid
	var isseList = Ext.data.Record.create([{
			name : 'issueDetailsId'
		}, {
			name : 'lastModifiedDate'
		}, {
			name : 'issueNo'
		}, {
			name : 'freeDeptName'
		}, {
			name : 'freeSpecialName'
		}, {
			name : 'itemCode'
		}, {
			name : 'materialNo'
		}, {
			name : 'materialName'
		}, {
			name : 'specNo'
		}, {
			name : 'stockUmId'
		}, {
			name : 'actIssuedCount'
		}, {
			name : 'unitPrice'
		}, {
			name : 'actPrice'
		}, {
			name : 'appliedQty'
		}, {
			name : 'estimatedPrice'
		}, {
			name : 'actEstimatePrice'
		}, {
			name : 'whsNo'
		}, {
			name : 'lastModifyBy'
		}, {
			name : 'memo'
		},{name:'refIssueNo'},
		{name:'getPerson'}
		,{name:'unitName'}
		]);
	
	var queryStore = new Ext.data.JsonStore({
		url : 'resource/findIssueList.action',
		totalProperty : 'total',
		root : 'root',
		fields : isseList
	});
	// 载入数据
// queryStore.load({
// params : {
// start : 0,
// limit : 18
// }
// });

	queryStore.on("load",function()
	{
		Ext.Msg.hide();
	}
	);
	// grid
	var transPanel = new Ext.grid.GridPanel({
		region : "center",
		enableColumnMove : false,
		enableColumnHide : true,
		frame : false,
		border : false,
		store : queryStore,
		// modify by ywliu 20100127
		viewConfig : {
			forceFit : false,
			getRowClass : function(record, rowIndex, rowParams, store) {
				// 禁用数据显示红色
				if (record.data.actIssuedCount < 0) {
					return 'x-grid-record-red';
				} else if (record.data.actIssuedCount > 0 && record.data.refIssueNo != null) {
					return 'x-grid-record-blue';
				} else {
					return '';
				} 
			}
		},
		// 单选
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
		columns : [new Ext.grid.RowNumberer({
			header : "行号",
			width : 35
		}),
				// 入库日期
				{
					header : "最后出库日期",
					anchor : '85%',
					sortable : true,
					dataIndex : 'lastModifiedDate',
					renderer : function(v, cellmeta, record, rowIndex,
									columnIndex, store) {// modify by wpzhu
								if (v != null && v != "") {
									if (record.get('lastModifiedDate') == '本页合计'||record.get('lastModifiedDate')=='所有页合计')
										return "<font color=red>"+ v + "</font>";
									else
							         return v;		
								}
							}
					
				},
				
				{
					header : "领料单号",
					anchor : '85%',
					sortable : true,
					dataIndex : 'issueNo'
				},
				// 费用归口部门
				{
					header : "费用归口部门",
					anchor : '85%',
					sortable : true,
					dataIndex : 'freeDeptName'
				},
				// 费用归口专业
				{
					header : "费用归口专业",
					anchor : '85%',
					sortable : true,
					hidden : true,
					dataIndex : 'freeSpecialName'
				},
				// 费用来源
				{
					header : "费用来源",
					anchor : '85%',
					sortable : true,
					 renderer : function(val) {
					 	if(val==""||val==null)// modify by wpzhu 100408
					 	{
					 	return"";
					 	}

					 	if (val == "zzfy") {
							return "生产类费用";
						} else if (val == "sclfy") {
							return "生产类费用";
						}else {
							return getBudgetName(val);
						}},
					dataIndex : 'itemCode'
				},
				// 物料名称
				{
					header : "物料名称",
					anchor : '85%',
					sortable : true,
					dataIndex : 'materialName'
				},
				// 物料编码 add by bjxu
				{
					header : "物资编码",
					anchor : '85%',
					sortable : true,
					dataIndex : 'materialNo'	
				},
				// 规格型号
				{
					header : "规格型号",
					anchor : '85%',
					sortable : true,
					dataIndex : 'specNo'
				},// 单位
				{
					header : "单位",
					width : 50,
					sortable : true,
					// renderer:unitName,
					dataIndex : 'unitName'
				},
				
				// 入库数
				{
					header : "出库数量",
					width : 75,
					sortable : true,
					renderer : function(v, cellmeta, record, rowIndex,
									columnIndex, store) {// modify by wpzhu
								if (v != null && v != "") {
									if (record.get('lastModifiedDate') == '本页合计'||record.get('lastModifiedDate')=='所有页合计')
										return "<font color=red>"+ moneyFormat(v) + "</font>";
									else
							         return v;		
								}
							},
					align : 'right',
					dataIndex : 'actIssuedCount'
				},
				// 单价
				{
					header : "单价",
					width : 75,
					sortable : true,
					renderer : numberFormat,
					align : 'right',
					dataIndex : 'unitPrice'
				},
				// 金额
				
				{
					header : "金额",
					width : 80,
					sortable : true,
					renderer :  function(v, cellmeta, record, rowIndex,
									columnIndex, store) {// modify by wpzhu
															// 100409
								if (v != null && v != "") {
									if (record.get('lastModifiedDate') == '本页合计'||record.get('lastModifiedDate')=='所有页合计')
										return "<font color=red>"+ numberFormat(v) + "</font>";
									else
							         return v;		
								}
							},
					align : 'right',
					dataIndex : 'actPrice'
				},
				// 入库人员
				{
					header : "估计数量",
					width : 70,
					sortable : true,
					hidden : true,
					renderer : numberFormat,
					dataIndex : 'appliedQty'
				},
				{
					header : "估计单价",
					width : 70,
					sortable : true,
					hidden : true,
					renderer : numberFormat,
					dataIndex : 'estimatedPrice'
				},
				{
					header : "估计金额",
					width : 110,
					sortable : true,
					hidden : true,
					renderer : numberFormat,
					dataIndex : 'actEstimatePrice'
				},
				{
					header : "仓库",
					anchor : '85%',
					sortable : true,
					hidden : true,
					dataIndex : 'whsNo'
				},
				{
					header : "发料人",
					width : 100,
					sortable : true,
					dataIndex : 'lastModifyBy'
				},
				{
					header : "备注",
					width : 100,
					sortable : true,
					hidden : true,
					dataIndex : 'memo'
				},
				{
					header : "红单操作的领料单号",
					width : 100,
					sortable : true,
					dataIndex : 'refIssueNo'
				},
				{
				    header : "领料人",
					width : 100,
					sortable : true,
					dataIndex : 'getPerson'
				}
				],
		 tbar:[{text:'查看出库明细',
			    	iconCls : Constants.CLS_QUERY,
			    	handler:queryStorageDetail
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

	// 显示区域
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [{
			region : "north",
			layout : 'fit',
			height : 140,
			border : true,
			split : true,
			margins : '0 0 0 0',
			// 注入表格
			items : [form]
		}, {
			region : "center",
			layout : 'fit',
			border : false,
			collapsible : true,
			split : true,
			margins : '0 0 0 0',
			// 注入表格
			items : [transPanel]
		}]
	});
	// ↓↓*******************************处理****************************************
	// 查询处理函数
	function searchBtn()
	{
		var ftime = Ext.get('planStartDate').dom.value;
		var ttime = Ext.get('planEndDate').dom.value;
		if (ftime > ttime) {
			Ext.Msg.alert('提示', '起始时间不能大于结束时间！');
			return false;
		}
		Ext.Msg.wait("正在查询数据，请等待...");
		
		queryStore.baseParams ={
			startdate : Ext.get('planStartDate').dom.value,
			enddate : Ext.get('planEndDate').dom.value,
			materialName : Ext.get('materialName').dom.value,
			isRedOp:Ext.get('stateComboBox').dom.value,
			issueNo : Ext.get('issueNo').dom.value,// add by liuyi 091102
													// 专业，仓库，物资类型，部门
			delayStore : cboDelayStore.getValue(),
		    materialClass : txtMaterialClass.getValue(),
		    dept : txtMrDeptH.getValue(),
		    getPerson:txtGetPerson.getValue(),
		    freeFrom:txtMrItemH.getValue(),// add by wpzhu
		    materialNo : Ext.get('materialNo').dom.value, // add by ywliu
															// 20100204
		    sendPerson: txtSendPerson.getValue()//add by fyyang 20100504
		};
		
			queryStore.load({
				params : {
					
					start : 0,
					limit : 18
// materialNo : Ext.get('materialNo').dom.value,
					
				// specNo : Ext.get('specNo').dom.value,
				// warehouseName : Ext.get('warehouseName').dom.value,
				   
				  
				   
				}
			});
		}
  
		// 导出
	function tableToExcel(tableHTML) {
		window.clipboardData.setData("Text", tableHTML);
		try {
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExApp.DisplayAlerts = false;
			ExApp.visible = true;
		} catch (e) {
			if (e.number != -2146827859)
				alert("您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}
	function myExport() {
		Ext.Ajax.request({
			// add by ywliu 20100204
			params : {
				startdate : Ext.get('planStartDate').dom.value,
				enddate : Ext.get('planEndDate').dom.value,
				materialName : Ext.get('materialName').dom.value,
				isRedOp:Ext.get('stateComboBox').dom.value,
				issueNo : Ext.get('issueNo').dom.value,// add by liuyi 091102
														// 专业，仓库，物资类型，部门
				delayStore : cboDelayStore.getValue(),
			    materialClass : txtMaterialClass.getValue(),
			    dept : txtMrDeptH.getValue(),
			    getPerson:txtGetPerson.getValue(),
			    freeFrom:txtMrItemH.getValue(),
			    materialNo : Ext.get('materialNo').dom.value, // add by ywliu
				sendPerson: txtSendPerson.getValue()									// 20100204
			},
			url : 'resource/findIssueList.action',
			success : function(response) {
				var json = eval('(' + response.responseText.trim() + ')');
				var records = json.root;
				// alert(records.length);
				var html = ['<table border=1><tr><th>出库日期</th><th>领料单号</th><th>费用归口部门</th><th>费用来源</th><th>物料名称</th><th>规格型号</th><th>单位</th><th>出库数量</th><th>单价</th><th>金额</th></tr>'];
				for (var i = 0; i < records.length; i += 1) {
					var rc = records[i];
					if(rc.lastModifiedDate!=null&&rc.lastModifiedDate=="本页合计")
					{
					    continue;
					}
					// add by ywliu 201020205
					var lastModifiedDate = "";
					var issueNo = "";
					var freeDeptName = "";
					var itemCode = "";
					var materialName = "";
					var specNo = "";
					var unitName = "";
					var actIssuedCount = "";// add by wpzhu 100408
					var unitPrice="";
					var actPrice="";
					if(rc.lastModifiedDate != null) {
						lastModifiedDate = rc.lastModifiedDate;
					}
					if(rc.issueNo != null) {
						issueNo = rc.issueNo;
					}
					if(rc.freeDeptName != null) {
						freeDeptName = rc.freeDeptName;
					}
					if(rc.itemCode != null) {
						//itemCode = rc.itemCode;
						if (rc.itemCode == "zzfy") {
							itemCode= "生产类费用";
						} else if (rc.itemCode == "sclfy") {
							itemCode= "生产类费用";
						}else {
							itemCode= getBudgetName(rc.itemCode);
						}
					}
					if(rc.materialName != null) {
						materialName = rc.materialName;
					}
					if(rc.specNo != null) {
						specNo = rc.specNo;
					}
					if(rc.unitName != null) {
						unitName = rc.unitName;
					}
					if(rc.actIssuedCount != null) {
						actIssuedCount = rc.actIssuedCount;
					}
					if(rc.unitPrice != null) {
						unitPrice = rc.unitPrice;
					}
					if(rc.actPrice != null) {
						actPrice = rc.actPrice;
					}
					html.push('<tr><td>' + lastModifiedDate + '</td><td>'
							+ issueNo + '</td><td>' + freeDeptName
							+ '</td><td>'
							+ itemCode + '</td><td>' + materialName
							+ '</td><td>' + specNo + '</td><td>'
							+ unitName + '</td><td>' + actIssuedCount
							+ '</td><td>'
							+ unitPrice + '</td><td>' + actPrice
							+ '</td></tr>');
				}
				html.push('</table>');
				html = html.join(''); // 最后生成的HTML表格
				// alert(html);
				tableToExcel(html);
			},
			failure : function(response) {
				Ext.Msg.alert('信息', '失败');
			}
		});
	}
	/**
	 * 查看出库操作明细
	 */
	 function queryStorageDetail()
	 {
	 	
	 	var record = transPanel.getSelectionModel().getSelected();
	 	if(record==null)
	 	{
	 		Ext.Msg.alert("提示","请选择一条记录！");
	 	}
	 	else
	 	{
	 		var arg = new Object();
	 		// alert(record.get("materialNo"));
	 		arg.materialNo=record.get("materialNo");
	 		arg.issueNo=record.get("issueNo");
	 		window.showModalDialog('issueDetailQuery.jsp', arg,
							'status:no;dialogWidth=750px;dialogHeight=450px');
	 	}
	 }
	
	/**
	 * 选择物料信息表单信息:form
	 */
	function selectMaterial() {
		var mate = window.showModalDialog('../../plan/RP001.jsp', window,
				'dialogWidth=800px;dialogHeight=550px;status=no');
		if (typeof(mate) != "undefined") {
			// 设置物料编码
			txtMaterialNo.setValue(mate.materialNo);
			// 设置物料名称
			txtMaterialName.setValue(mate.materialName);
			// 设置规格型号
			txtSpecNo.setValue(mate.specNo);
			// 设置物料id
			hdnAltMatId.setValue(mate.materialId);
		}
		Ext.get("materialName").focus();
	}
	
	/**
	 * 格式化数据----计量单位
	 */
	function unitName(value) {
		if(value!==null&&value!==""){
		var url = "resource/getRS001UnitName.action?unitCode=" + value;
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("POST", url, false);
		conn.send(null);
		return conn.responseText;
		}else{
			return "";
		}
		
	};
	
	searchBtn();
});