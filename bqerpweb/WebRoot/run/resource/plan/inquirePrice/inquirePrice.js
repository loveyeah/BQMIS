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
			}, {
				// 分配计划时间
				name : 'gatherTime'
			}, {
				// 分配计划时间
				name : 'memo'
			}, {
				// 生产厂家add by bjxu 091214
				name : 'factory'
			}, {
				// 建议供应商add by bjxu 091214
				name : 'supplier'
			}, {
				// 申报人add by ltong 20100427
				name : 'applyByName'
			}, {
				// 申报单位add by ltong 20100427
				name : 'applyDeptName'
			}, {
				// 估计单价add by ltong 20100427
				name : 'estimatedPrice'
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
		width : 150,
		onTriggerClick : function(e) {
			if (!this.disabled) {
				chosebuyer()
			}
			this.blur();
		}
			// readOnly : true,
			// allowBlank : false
		});
	// txtBuyer.onTriggerClick = chosebuyer;
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
							width : 90,
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
							width : 60,
							dataIndex : 'isEnquire',
							renderer : function(value) {
								if (value == "N")
									return "未询价";
								else if (value == "Q")
									return "询价中";
							}
						}, {
							header : "采购员",
							sortable : true,
							width : 70,
							dataIndex : 'buyer'
						}, {
							header : "分配计划时间",
							sortable : true,
							width : 70,
							dataIndex : 'gatherTime'
						}, {
							header : "备注",
							sortable : true,
							// width : 60,
							dataIndex : 'memo'
						}, {
							header : "生产厂家",
							sortable : true,
							// width : 60,
							dataIndex : 'factory'

						}, {
							header : "建议供应商",
							sortable : true,
							// width : 60,
							dataIndex : 'supplier'
						}, {
							header : "申报人",
							sortable : true,
							// width : 60,
							dataIndex : 'applyByName'
						}, {
							header : "申报单位",
							sortable : true,
							// width : 60,
							dataIndex : 'applyDeptName'
						}, {
							header : "估计单价",
							sortable : true,
							// width : 60,
							dataIndex : 'estimatedPrice'
						}],
				sm : sm,

				autoSizeColumns : true,
				viewConfig : {
					forceFit : true
				},
				// title : '物料需求计划汇总查询',//生产厂家

				tbar : ['物料名称', txtMaterialName, '-', '采购员', txtBuyer, {
							text : '查询',
							iconCls : 'query',
							handler : queryRecord

						}, '-', {
							text : '询价单打印',
							iconCls : 'pdfview',
							handler : printBill

						}, '-', {
							text : '询价录入',
							iconCls : 'list',
							handler : inquireDetails

						}, '-', {
							text : '查看需求计划物资信息',
							iconCls : 'list',
							handler : queryMaterialDetail
						}, '-', {
							text : '退回到计划员',
							iconCls : 'update',
							handler : turnPlan
						}]
			// 去除分页功能 update by sychen 20100511
//						,
//				// 分页
//				bbar : new Ext.PagingToolbar({
//					id : 'pagingtoolbar',
//					pageSize : 18,
//					store : store,
//					displayInfo : true,
//					displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
//					emptyMsg : "没有记录"
//						// onLoad: function(store, r, o){
//						// alert(this.cursor );
//						// return;
//						// }
//					})

			});

	// ---------------------------------------
	var backReason = new Ext.form.TextArea({
				id : "backReason",
				fieldLabel : '退回原因',
				width : 200,
				name : 'backReason',
				height : 80

			});
	var myaddpanel = new Ext.FormPanel({
				frame : true,
				labelAlign : 'center',
				labelWidth : 80,
				closeAction : 'hide',
				title : '填写退回原因',
				items : [backReason]
			});
	var win = new Ext.Window({
				width : 400,
				height : 180,
				buttonAlign : "center",
				items : [myaddpanel],
				layout : 'fit',
				closeAction : 'hide',
				resizable : false,
				modal : true,
				buttons : [{
					text : '保存',
					iconCls : 'save',
					handler : function() {
						var sm = grid.getSelectionModel();
						var selected = sm.getSelected();
						var backId = selected.get('gatherId');
						Ext.Ajax.request({
									method : 'post',
									url : 'resource/gatherTurnPlan.action',
									params : {
										backReason : backReason.getValue(),
										backId : backId
									},
									success : function(action) {
										Ext.Msg.alert("提示", "退回成功！");
										win.hide();
										queryRecord();
									},
									failure : function() {
										Ext.Msg.alert(Constants.SYS_REMIND_MSG,
												Constants.COM_E_014);
									}
								});

					}
				}, {
					text : '取消',
					iconCls : 'cancer',
					handler : function() {
						win.hide();
					}
				}]

			});
	function turnPlan() {

		if (grid.selModel.hasSelection()) {

			var records = grid.getSelectionModel().getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("提示", "请选择其中一项进行退回！");
			} else {

				Ext.Msg.confirm("提示", "是否确定退回所选数据?", function(buttonobj) {
							if (buttonobj == "yes") {
								myaddpanel.getForm().reset();
								win.show();
							}
						})
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要退回的记录!");
		}
	}
	function queryMaterialDetail() {
		if (grid.selModel.hasSelection()) {

			var records = grid.getSelectionModel().getSelected();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行查看！");
			} else {

				var url = "queryMaterialForGather.jsp";
				var args = new Object();
				args.gatherId = records.get("gatherId");
				var location = window
						.showModalDialog(
								url,
								args,
								'dialogWidth=800px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');

			}
		} else {
			Ext.Msg.alert("提示", "请先选择要查看的记录!");
		}
	}

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
			flag:'2'
		};
		store.load(
		//update by sychen 20100511
//			{
//					params : {
//						start : 0,
//						limit : 18
//					}
//				}
				);
	}
	function printBill() {
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		if (grid.selModel.hasSelection()) {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("gatherId")) {
					ids.push(member.get("gatherId"));
				} else {

					store.remove(store.getAt(i));
				}

			}
			var url = "inquireDetailPrint.jsp";
			var args = new Object();
			args.gatherIds = ids.join(',');
			var location = window
					.showModalDialog(
							url,
							args,
							'dialogWidth=750px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');

		} else {
			Ext.Msg.alert("提示", "请先选择物资!");
		}
	}

	function inquireDetails() {

		var strPageSize = Ext.getCmp('pagingtoolbar').pageSize;
		var strCursor = Ext.getCmp('pagingtoolbar').cursor;
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var storeRec = [];
		var ids = [];
		var qty = [];
		var location;
		// modify by liuyi 091022
		var storeData = null;
		if (grid.selModel.hasSelection()) {

			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("gatherId") != null
						&& member.get("approvedQty") != null
						&& member.get("approvedQty") != 0) {
					ids.push(member.get("gatherId"));
					qty.push(member.get("approvedQty"));
					storeRec.push(member.data);
				}
				// else {
				// store.remove(store.getAt(i));
				// }

			}
			var url = "inquireDetail.jsp";
			var args = new Object();

			// for (var i = 0; i < selected.length; i++) {
			// for (var j = 0; j < store.getCount(); j++) {
			// if (selected[i].get("gatherId") == store.getAt(j).data.gatherId)
			// {
			// storeRec.push(store.getAt(j).data);
			// }
			// }
			// }
			args.storeData = storeRec;
			args.gatherIds = ids.join(',');

			var records = grid.selModel.getSelections();
			var obj = window
					.showModalDialog(
							url,
							args,
							'dialogWidth=1050px;dialogHeight=500px;center=yes;help=no;resizable=yes;status=no;');
			if (typeof(obj) != "undefined") {

				if (obj == "ok")
					refreshGrid(strCursor, strPageSize + strCursor);
				if (obj == true) {
					Ext.Msg.alert("提示", "供应商选择成功！");
					refreshGrid(strCursor, strPageSize + strCursor);

				}

			}

		} else {
			Ext.Msg.alert("提示", "请先选择物资!");
		}
	}

	function refreshGrid(start, end) {
		store.baseParams = {
			materialName : txtMaterialName.getValue(),
			buyer : txtBuyer.getValue()
		};
		store.load(
		//update by sychen 20100511
//			{
//					params : {
//						start : start,
//						limit : end
//					}
//				}
				);
	}

	// U by bjxu 091103
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号
							txtBuyer.setValue(result.workerName);
							txtBuyerHide.setValue(result.workerCode);
							if (result.workerCode == "999999") {
								txtBuyer.setDisabled(false);
							} else {
								txtBuyer.setDisabled(true);
							}
						}
						queryRecord();
					}
				});
	}
	getWorkCode();
});