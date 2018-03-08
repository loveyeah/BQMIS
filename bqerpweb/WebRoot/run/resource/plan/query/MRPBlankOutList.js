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
				// 需求计划明细ID
				name : 'requirementDetailId'
			},
			// 申请人
			{
				name : 'applyByName'
			},
			// 申请部门
			{
				name : 'applyDeptName'
			},
			// 申请原因
			{
				name : 'applyReason'
			},
			// 申请原因
			{
				name : 'mrDate'
			},
			// 申请原因
			{
				name : 'planOriginalId'
			}, {
				name : 'cancelReason'
			}]);

	// 是否免检 显示为Checkbox
	var ckcQaControlFlag = new Ext.grid.CheckColumn({
				header : "是否免检",
				dataIndex : 'qaControlFlag',
				width : 100
			});

	var smAnnex = new Ext.grid.CheckboxSelectionModel({});

	var dataProxy = new Ext.data.HttpProxy({
				url : 'resource/getBlankOutMaterialDetail.action'
			});

	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, gridRecord);

	var store = new Ext.data.Store({
				proxy : dataProxy,
				reader : theReader
			});

	var sm = new Ext.grid.RowSelectionModel({
				singleSelect : true
			});

	// 部门
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
	var txtMrDept = new Ext.form.TriggerField({
				fieldLabel : '部门',
				width : 108,
				id : "mrDeptId",
				displayField : 'text',
				valueField : 'id',
				hiddenName : 'mrDept',
				blankText : '请选择',
				emptyText : '请选择',
				maxLength : 100,
				anchor : '100%',
				readOnly : true
			});
	txtMrDept.onTriggerClick = choseDept;
	var txtMrDeptH = new Ext.form.Hidden({
				hiddenName : 'mrDept'
			})
	var wzName = new Ext.form.TextField({
				fieldLabel : '物资名称',
				id : "wzName"
			})
	var ggSize = new Ext.form.TextField({
				fieldLabel : '规格型号',
				id : "ggSize"
			})

	// add by fyyang 090807---增加计划作废和取消作废功能---------

	function cancelPlanOp() {
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var menber = selected[0];
		if (grid.selModel.hasSelection()) {
			if (selected.length > 1) {
				Ext.Msg.alert("提示", "请先选择一条记录!");
				return;
			}
			Ext.Msg.confirm("提示", "是否确定取消作废?", function(buttonobj) {
				if (buttonobj == "yes") {
					var sm = grid.getSelectionModel();
					var selected = sm.getSelections();
					var menber = selected[0];
					var requirementDetailId = menber.get('requirementDetailId');
					Ext.Ajax.request({
								method : 'post',
								url : 'resource/CancelBlankOutPlanMaterial.action',
								params : {
									requirementDetailId : requirementDetailId
								},
								success : function(action) {
									Ext.Msg.alert("提示", "取消作废成功！");
									queryRecord();
									window.returnValue = true;
								},
								failure : function() {
									Ext.Msg.alert(Constants.SYS_REMIND_MSG,
											Constants.COM_E_014);
								}
							});
				}
			})
		} else {
			Ext.Msg.alert("提示", "请先选择要操作的记录!");
		}
	}
	// -----------------------------------------------------
	var headTbar = new Ext.Toolbar({
				region : 'north',
				height : 30,
				border : false,
				items : ['申请部门：', txtMrDept, txtMrDeptH, '物资名称:', wzName,
						'规格型号:', ggSize, {
							id : 'btnQuery',
							text : "查询",
							iconCls : 'query',
							handler : queryRecord
						}, {
							text : '取消作废',
							handler : cancelPlanOp
						}]
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
				// enableColumnMove : false,
				store : store,
				columns : [smAnnex, new Ext.grid.RowNumberer({
									header : "行号",
									width : 35
								}), {
							header : "物料ID<font color='red'>*</font>",
							sortable : true,
							width : 80,
							hidden : true,
							dataIndex : 'materialId'
						}, {
							header : "物料编码<font color='red'>*</font>",
							sortable : true,
							width : 150,
							dataIndex : 'materialNo'
						}, {
							header : "物料名称",
							sortable : true,
							width : 150,
							dataIndex : 'materialName'
						}, {
							header : "作废原因",
							sortable : true,
							width : 150,
							dataIndex : 'cancelReason'
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
						},

						{
							header : "采购数量<font color='red'>*</font>",
							sortable : true,
							width : 60,
							align : 'right',
							dataIndex : 'approvedQty'
						}, {
							header : "已收数量",
							sortable : true,
							width : 60,
							hidden : true,
							align : 'right',
							dataIndex : 'issQty'
						}, {
							header : '计量单位',
							hidden : false,
							width : 100,
							dataIndex : 'stockUmName'
						}, {
							header : '最大库存量',
							hidden : true,
							dataIndex : 'maxStock'
						}, ckcQaControlFlag, {
							header : '需求计划明细ID',
							width : 100,
							hidden : false,
							dataIndex : 'requirementDetailId'
						}, {
							header : '申请人',
							width : 100,
							hidden : false,
							dataIndex : 'applyByName'
						}, {
							header : '申请部门',
							width : 100,
							hidden : false,
							dataIndex : 'applyDeptName'
						}, {
							header : '申请理由',
							width : 100,
							hidden : false,
							dataIndex : 'applyReason'
						}, {
							header : "申请日期",
							width : 100,
							sortable : true,
							renderer : renderDate,
							dataIndex : 'mrDate'
						}, {
							header : "计划时间",
							width : 100,
							sortable : true,
							renderer : function(value, cellmeta, record,
									rowIndex) {
								return getPlanDateInfoByDate(record
												.get("planOriginalId"), value);
							},
							dataIndex : 'mrDate'
						}, {
							header : "计划来源",
							width : 100,
							sortable : true,
							renderer : function(value, cellmeta, record,
									rowIndex) {
								return planOriginalName(record
												.get("planOriginalId"), value);
							},
							dataIndex : 'planOriginalId'
						}

				],
				sm : smAnnex,

				autoSizeColumns : true,
				viewConfig : {
					forceFit : true
				},

				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : store,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						})

			});

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				autoScroll : true,
				// layout : "fit",
				items : [headTbar, grid]
			});

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

	// 查询
	function queryRecord() {

		store.baseParams = {
			applyDept : txtMrDeptH.getValue(),
			wzName : wzName.getValue(),
			ggSize : ggSize.getValue()
		};
		store.load({
					params : {
						start : 0,
						limit : 18
					}
				});
	}
	queryRecord();

});