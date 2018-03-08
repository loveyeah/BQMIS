Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
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
				name : 'inquireDetailId'
			}, {
				name : 'gatherId'
			}, {
				name : 'inquireSupplier'
			}, {
				name : 'materialId'
			}, {
				name : 'materialName'
			}, {
				name : 'supplyName'
			}, {
				name : 'inquireQty'
			}, {
				name : 'unitPrice'
			}, {
				name : 'totalPrice'
			}, {
				name : 'qualityTime'
			}, {
				name : 'offerCycle'
			}, {
				name : 'specNo'
			}, {
				name : 'effectStartDate'
			}, {
				name : 'effectEndDate'
			}, {
				// 采购员
				name : 'buyer'
			}, {
				// 汇总时间
				name : 'gatherTime'
			}, {
				// 生产厂家 add by bjxu 091214
				name : 'factory'
			}, {
				// 建议供应商 add by bjxu 091214
				name : 'supplier'
			}]);

	// 是否免检 显示为Checkbox
	var ckcQaControlFlag = new Ext.grid.CheckColumn({
				header : "是否免检",
				dataIndex : 'qaControlFlag',
				width : 60
			});

	var dataProxy = new Ext.data.HttpProxy({
				url : 'resource/findQuotedPriceList.action'
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
	// txtMaterialName.onClick(selectMaterial); modify by drdu 091124
	/**
	 * 弹出物料选择窗口
	 */
	function selectMaterial() {
		var mate = window.showModalDialog('../RP001.jsp', window,
				'dialogWidth=800px;dialogHeight=550px;status=no');
		if (typeof(mate) != "undefined") {
			// 设置物料名
			txtMaterialName.setValue(mate.materialName);
		}
	}

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
	// 采购员
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

							header : "ID",
							width : 75,
							sortable : true,
							dataIndex : 'inquireDetailId',
							hidden : true
						},

						{
							header : "汇总ID",
							width : 75,
							sortable : true,
							dataIndex : 'gatherId',
							hidden : true
						}, {
							header : "物料名称",
							width : 75,
							sortable : true,
							dataIndex : 'materialName'
						}, {
							header : "供应商",
							width : 75,
							sortable : true,
							dataIndex : 'supplyName'
						}, {
							header : "规格型号",
							width : 75,
							sortable : true,
							dataIndex : 'specNo'
						}, {
							header : "采购数量",
							width : 75,
							sortable : true,
							dataIndex : 'inquireQty'
						}, {
							header : "单价",
							width : 75,
							sortable : true,
							dataIndex : 'unitPrice'
						}, {
							header : "总价",
							width : 75,
							sortable : true,
							dataIndex : 'totalPrice'
						}, {
							header : "报价有效开始日期",
							width : 75,
							sortable : true,
							dataIndex : 'effectStartDate'
						}, {
							header : "报价有效结束日期",
							width : 75,
							sortable : true,
							dataIndex : 'effectEndDate'
						}, {

							header : '状态',
							hidden : true,
							dataIndex : 'isEnquire',
							renderer : function(value) {
								if (value == "N")
									return "未询价";
								else if (value == "Q")
									return "已询价";
							}
						}, {
							header : "采购员",
							width : 75,
							sortable : true,
							dataIndex : 'buyer'
						}, {
							header : "汇总时间",
							sortable : true,
							width : 60,
							dataIndex : 'gatherTime'
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
						}],
				sm : sm,

				autoSizeColumns : true,
				viewConfig : {
					forceFit : true
				},
				// title : '物料需求计划汇总查询',
				tbar : ['物料报价信息', '-', '物料名称', txtMaterialName, '-', '采购员',
						txtBuyer, {
							text : '查询',
							iconCls : 'query',
							handler : queryRecord

						}, '-', {
							text : '查看需求计划物资信息',
							iconCls : 'list',
							handler : queryMaterialDetail
						}],
				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : 10,
							store : store,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						})

			});

	grid.on("dblclick", queryDetail);
	// ---------------------------------------
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
			buyer : txtBuyer.getValue()
		};
		store.load({
					params : {
						start : 0,
						limit : 10
					}
				});
	}

	function queryDetail() {
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		if (grid.selModel.hasSelection()) {
			var data = selected[0];
			var gatherIds = data.get("gatherId");

			var url = "inquireDetailQuery.jsp";
			var args = new Object();
			args.gatherIds = gatherIds;
			var location = window
					.showModalDialog(
							url,
							args,
							'dialogWidth=750px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');

		} else {
			Ext.Msg.alert("提示", "请选择要打印的物资!");
		}
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