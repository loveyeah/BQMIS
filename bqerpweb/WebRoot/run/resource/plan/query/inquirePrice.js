Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
var storeData = null;
var workerCode;
Ext.onReady(function() {
	Ext.grid.CheckColumn = function(config) {
		Ext.apply(this, config);
		if (!this.id) {
			this.id = Ext.id();
		}
		this.renderer = this.renderer.createDelegate(this);
	};
	Ext.grid.CheckColumn.prototype = {
		init : function(grid) {
			this.grid = grid;
			this.grid.on('render', function() {
						var view = this.grid.getView();
						view.mainBody.on('mousedown', this.onMouseDown, this);
					}, this);
		},

		onMouseDown : function(e, t) {
			e.stopEvent();
			return false;
			if (t.className
					&& t.className.indexOf('x-grid3-cc-' + this.id) != -1) {
				e.stopEvent();
				var index = this.grid.getView().findRowIndex(t);
				var record = this.grid.store.getAt(index);
				if (record.data[this.dataIndex] == Constants.CHECKED_VALUE) {
					record.set(this.dataIndex, Constants.UNCHECKED_VALUE);
				} else {
					record.set(this.dataIndex, Constants.CHECKED_VALUE);
				}
			}
		},

		renderer : function(v, p, record) {
			p.css += ' x-grid3-check-col-td';
			p.css += ' x-item-disabled';
			return '<div class="x-grid3-check-col' + (v == 'Y' ? '-on' : '')
					+ ' x-grid3-cc-' + this.id + '">&#160;</div>';
		}
	}

	// grid中的数据Record
	var gridRecord = new Ext.data.Record.create([{
				// 物料ID
				name : 'gatherId'
			}, {
				// 物料ID
				name : 'materialId'
			}, {
				// 物料编码
				name : 'materialNo'
			}, {
				// 物料名称
				name : 'materialName'
			}, {
				// 规格型号
				name : 'className'
			}, {
				// 规格型号
				name : 'specNo'
			}, {
				// 材质/参数
				name : 'parameter'
			}, {
				// 采购数量
				name : 'approvedQty'
			}, {
				// 已收数量
				name : 'issQty'
			}, {
				// 计量单位
				name : 'stockUmName'
			}, {
				// 最大库存量
				name : 'maxStock'
			}, {
				// 是否免检
				name : 'qaControlFlag'
			}, {
				name : 'isEnquire' // 状态
			}, {
				// 采购员
				name : 'buyer'
			},{
			   name:'gatherTime'
			},{
			   name : 'applyReason' // 申请理由 add by sychen 20100510
			},{
			   name : 'planOriginalId' // 计划种类 add by sychen 20100510
			},{
			   name : 'memo' // 备注 add by sychen 20100510
			}]);

	// 是否免检 显示为Checkbox
	var ckcQaControlFlag = new Ext.grid.CheckColumn({
				header : "是否免检",
				dataIndex : 'qaControlFlag',
				width : 60
			});

	var dataProxy = new Ext.data.HttpProxy({
				url : 'resource/getMaterialGatherDetail.action'
			});

	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, gridRecord);

	var store = new Ext.data.Store({
				proxy : dataProxy,
				reader : theReader
			});

	// ----查询条件--------------
		//add by fyyang 091222-------
			function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";
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
	var enddate = new Date();
	var startdate = enddate.add(Date.MONTH, -1);
	startdate = startdate.getFirstDateOfMonth();
	var sdate = ChangeDateToString(startdate);
	var edate = ChangeDateToString(enddate);
			var timefromDate = new Ext.form.TextField({
		id : 'timefromDate',
		name : '_timefromDate',
		fieldLabel : "开始",
		style : 'cursor:pointer',
		cls : 'Wdate',
		width : 90,
		value : sdate,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-01',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true
				});
			}
		}
	});
	var timetoDate = new Ext.form.TextField({
		name : '_timetoDate',
		value : edate,
		id : 'timetoDate',
		fieldLabel : "结束",
		style : 'cursor:pointer',
		cls : 'Wdate',
		width : 90,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-01',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true
				});
			}
		}
	});
		//-add end----------------	
	// 物料名称
	var txtMaterialName = new Ext.form.TextField({
				fieldLabel : '物料名称',
				readOnly : false,
				width : 120,
				id : "materialName",
				anchor : '100%'
			});
	// txtMaterialName.onClick(selectMaterial);
	/**
	 * 弹出物料选择窗口
	 */
	// function selectMaterial() {
	// var mate = window.showModalDialog('../RP001.jsp', window,
	// 'dialogWidth=800px;dialogHeight=550px;status=no');
	// if (typeof(mate) != "undefined") {
	// // 设置物料名
	// txtMaterialName.setValue(mate.materialName);
	// }
	// }
	var txtBuyer = new Ext.form.TextField({
				name : 'txtBuyer',
				fieldLabel : '采购员'
			});
	function chosebuyer() {
		var args = {
			selectModel : 'single',
			rootNode : {
				id : '0',
				text : '合肥电厂'
			}
		}
		var dept = window
				.showModalDialog(
						'../../comm/purchaserForSelect.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(dept) != "undefined") {
			txtBuyer.setValue(dept.names);
			txtBuyerHide.setValue(dept.codes);

			queryRecord();
		}
	}
	// 采购员 U by bjxu 091103
	var txtBuyer = new Ext.form.TriggerField({
		fieldLabel : '采购员',
		id : "mytxtBuyer",
		displayField : 'text',
		valueField : 'id',
		hiddenName : 'txtBuyer',
		blankText : '请选择',
		emptyText : '请选择',
		// maxLength : 100,
		width : 150
//		,
//		onTriggerClick : function(e) {
//			if (!this.disabled) {
//				chosebuyer()
//			}
//			this.blur();
//		}
			// readOnly : true,
			// allowBlank : false
		});
	 txtBuyer.onTriggerClick = chosebuyer;
	var txtBuyerHide = new Ext.form.Hidden({
				hiddenName : 'txtBuyerHide'
			})
	// ------------------------
	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false
			});
	var grid = new Ext.grid.GridPanel({
				region : "center",
				layout : 'fit',
				autoScroll : true,
				height : 410,
				isFormField : false,
				border : true,
				anchor : "0",
				// 标题不可以移动
				enableColumnMove : false,
				store : store,

				columns : [sm, new Ext.grid.RowNumberer({
									header : "行号",
									width : 35
								}), {
							header : "物料汇总ID<font color='red'>*</font>",
							sortable : true,
							width : 80,
							hidden : true,
							dataIndex : 'gatherId',
							css : CSS_GRID_INPUT_COL

						}, {
							header : "物料ID<font color='red'>*</font>",
							sortable : true,
							width : 80,
							hidden : true,
							dataIndex : 'materialId',
							css : CSS_GRID_INPUT_COL

						}, {
							header : "物料编码<font color='red'>*</font>",
							sortable : true,
							width : 80,
							dataIndex : 'materialNo'
							// css:CSS_GRID_INPUT_COL

					}	, {
							header : "物料名称",
							sortable : true,
							width : 80,
							dataIndex : 'materialName'
						}, {
							header : "采购数量<font color='red'>*</font>",
							sortable : true,
							width : 60,
							align : 'right',
							dataIndex : 'approvedQty'
						}, {
							header : "规格型号",
							sortable : true,
							width : 60,
							dataIndex : 'specNo'
						}, {
							header : "材质/参数",
							sortable : true,
							width : 60,
							dataIndex : 'parameter'
						}, {
							header : "已收数量",
							hidden : true,
							sortable : true,
							width : 60,
							align : 'right',
							dataIndex : 'issQty'
						}, {
							header : '计量单位',
							hidden : false,
							dataIndex : 'stockUmName'
						}, {
							header : '最大库存量',
							hidden : true,
							dataIndex : 'maxStock'
						}, ckcQaControlFlag, {

							header : '状态',
							hidden : false,
							dataIndex : 'isEnquire',
							renderer : function(value) {
								if (value == "N")
									return "未询价";
								else if (value == "Q")
									return "询价中";
							    else if (value == "Y")
									return "已询价";		
							}
						},{
							header : "采购员",
							sortable : true,
							width : 60,
							dataIndex : 'buyer'
						},{
						    header : "汇总时间",
							sortable : true,
							width : 60,
							dataIndex : 'gatherTime'
						},{//add by sychen 20100510
							header : '申请理由',
							width : 80,
							hidden : false,
							dataIndex : 'applyReason'
						}, {//add by sychen 20100510
							header : "计划种类",
							width : 100,
							sortable : true,
							renderer : function(value, cellmeta, record,
									rowIndex, columnIndex, store) {
									return planOriginalName(record
													.get("planOriginalId"),
											value);
							},
							dataIndex : 'planOriginalId'
						},{//add by sychen 20100510
							header : '备注',
							width : 80,
							hidden : false,
							dataIndex : 'memo'
						}],
				sm : sm,

				autoSizeColumns : true,
				viewConfig : {
					forceFit : true
				},
				// title : '物料需求计划汇总查询',
				tbar : ['汇总的物料信息', '-','汇总时间：',timefromDate,'~',timetoDate,'-', '物料名称', txtMaterialName, '-', '采购员',
						txtBuyer, {
							text : '查询',
							iconCls : 'query',
							handler : queryRecord

						}, '-', {
							text : '更改采购员',
							iconCls : 'list',
							handler : chooseBuyer

						},'-',{
						   text:'导出',
						   	iconCls : 'export',
						   handler : exportInfo
							
						}],
				// 分页
				bbar : new Ext.PagingToolbar({
					id : 'pagingtoolbar',
					pageSize : 18,
					store : store,
					displayInfo : true,
					displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
					emptyMsg : "没有记录"
						// onLoad: function(store, r, o){
						// alert(this.cursor );
						// return;
						// }
					})

			});
			
			//----------add by fyyang 091222 导出
		function tableToExcel(tableHTML) {
		window.clipboardData.setData("Text", tableHTML);
		try {
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExWSh.Columns("C:C").ColumnWidth = 15;
			ExWSh.Columns("D:E").ColumnWidth = 20;
			ExApp.DisplayAlerts = false;
			ExApp.visible = true;
		} catch (e) {
			if (e.number != -2146827859)
				Ext.Msg.alert('提示信息', "您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
		ExWBk.worksheets(1).Paste;

	}
			
			function exportInfo()
			{
				
				Ext.Ajax.request({
			url : 'resource/getMaterialGatherDetail.action',
			mothod : 'post',
			params : {
			materialName : txtMaterialName.getValue(),
			buyer : txtBuyer.getValue(),
			sdate:timefromDate.getValue(),
			edate:timetoDate.getValue(),
			flag:'1'

			},
			success : function(action) {
				 
				var rs = eval("(" + action.responseText + ")");
				if(rs.totalCount==0) 
				{
					Ext.Msg.alert("提示信息","无数据进行导出！");
				}
				var tableHeader = "<table border=1><tr><th>序号</th><th>采购员</th><th>汇总时间</th><th>物资编码</th><th>物资名称</th>" +
						"<th >规格型号</th><th>材质/参数</th><th>采购数量</th><th>计量单位</th><th>询价状态" +
						"</th><th>申请理由</th><th>计划种类</th><th>备注</th>" ;// add by sychen 20100510
				var html = [tableHeader];
				for(var i=0;i<rs.totalCount;i++)
				{
					var num=i+1;
					var obj=rs.list[i];
					var specNo="";
					var parameter="";
					if(obj.specNo!=null)
					{
						specNo=obj.specNo;
					}
					if(obj.parameter!=null)
					{
					 parameter=obj.parameter;	
					}
					var strisEnquire="";
					if(obj.isEnquire== "N") strisEnquire="未询价"
					else if(obj.isEnquire== "Q") strisEnquire="询价中"
					else strisEnquire="已询价";
					
					// add by dychen 20100510---------------//
					var planOriginalName="";
					if(obj.planOriginalId== "6") planOriginalName="行政类季度计划"
					else if(obj.planOriginalId== "7") planOriginalName="行政类紧急计划"
					else if(obj.planOriginalId== "4") planOriginalName="生产类月度计划"
					else if(obj.planOriginalId== "10") planOriginalName="生产类紧急计划"
					else if(obj.planOriginalId== "3") planOriginalName="固定资产类计划"
					else if(obj.planOriginalId== "12") planOriginalName="计算机相关材料"
					else if(obj.planOriginalId== "13") planOriginalName="计算机材料月度计划"
					else if(obj.planOriginalId== "14") planOriginalName="计算机材料紧急计划"
					else if(obj.planOriginalId== "15") planOriginalName="劳保用品计划"
					else planOriginalName="";
					//add end -----------------------------//
					
					html.push("<tr><td>"+num+"</td>");
					html.push("<td>"+obj.buyer+"</td><td>"+obj.gatherTime+"</td><td>"+obj.materialNo+"</td>");
					html.push("<td>"+obj.materialName+"</td><td>"+specNo+"</td><td>"+parameter+"</td>");
					html.push("<td>"+obj.approvedQty+"</td><td>"+obj.stockUmName+"</td><td>"+strisEnquire+"</td>");
					html.push("<td>"+obj.applyReason+"</td><td>"+planOriginalName+"</td><td>"+obj.memo+"</td>");// add by sychen 20100510
					html.push("</tr>");
				}
				html.push('</table>');
				html = html.join(''); // 最后生成的HTML表格
				
				tableToExcel(html);
				

			},
			failure : function(result, request) {

			}
		})
				
			}

	// ---------------------------------------

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				// layout : "fit",
				items : [grid]
			});

	// 查询
	function queryRecord() {
		store.baseParams = {
			materialName : txtMaterialName.getValue(),
			buyer : txtBuyer.getValue(),
			sdate:timefromDate.getValue(),
			edate:timetoDate.getValue(),
			flag:'1'
		};
		store.load({
					params : {
						start : 0,
						limit : 18
					}
				});
	}

	function chooseBuyer(){
		
		if (sm.hasSelection()) {
			var ids = new Array();
			var selections = sm.getSelections();
					for (var i = 0; i < selections.length; i++) {
						var rec = selections[i];
						if(rec.get('isEnquire') == 'N')
						{
							ids.push(rec.get('gatherId'));	
						}
						else 
						{
							Ext.Msg.alert('提示','只有未询价的数据可以更改采购员！');
							return;
						}
						
					}
			Ext.Msg.confirm('提示', '确认要更改所选数据的采购员？', function(id) {
				if (id == 'yes') {
					
					if (ids.length > 0) {
						var args = {
							selectModel : 'single',
							rootNode : {
								id : '0',
								text : '合肥电厂'
							}
						}
						var chooser = window
								.showModalDialog(
										'../../comm/purchaserForSelect.jsp',
										args,
										'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
						if (typeof(chooser) != "undefined") {
							if(chooser.codes != null && chooser.codes != '')
							{
								Ext.Ajax.request({
									url : 'resource/chooserBuyer.action',
									mothod : 'post',
									params : {
										ids : ids.join(','),
										buyer : chooser.codes
									},
									success : function(result, request) {
										Ext.Msg.alert('提示','采购员更改成功！');
										store.reload();
									},
									failure : function(result, request) {
										store.reload();
									} 
								})
							}
							
						}
						
					}
				}
			})

		} else
		{
			Ext.Msg.alert('提示','请先选择要更改的数据！')
		}
		
	}
	

	function refreshGrid(start, end) {
		store.baseParams = {
			materialName : txtMaterialName.getValue(),
			buyer : txtBuyer.getValue()
		};
		store.load({
					params : {
						start : start,
						limit : end
					}
				});
	}

	queryRecord();
});