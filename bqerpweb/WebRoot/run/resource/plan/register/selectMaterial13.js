Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.form.Label.prototype.setText = function(argText) {
	this.el.dom.innerHTML = argText;
}
Ext.onReady(function() {
	var materialClassCode = "5-E-05";
	// ↓↓*****弹出窗口*******↓↓//
	// 组件默认宽度
	var width = 80;
	// 物料编码
	var lblMaterialId = new Ext.form.Label({
				style : "fontSize:11px",
				id : 'materialId',
				width : width
			});

	// 物料名称
	var lblMaterialName = new Ext.form.Label({
				style : "fontSize:11px",
				id : 'materialName',
				width : width
			});

	// 图号
	var lblDocNo = new Ext.form.Label({
				style : "fontSize:11px",
				id : 'docNo',
				width : width
			});
	// 返回button
	var btnCancel = new Ext.Button({
				text : "返回",
				handler : function() {
					// 关闭画面
					win.hide();
					// 返回空
					return;
				}
			});
	// 显示图片
	var txtImage = new Ext.form.TextField({
				id : "materialMap",
				height : 260,
				width : 500,
				name : 'materialMap',
				inputType : 'image',
				anchor : "95%"
			});

	// formpanel
	var accessoryPanel = new Ext.FormPanel({
				tbar : ['物料编码： ', lblMaterialId, '-', '物料名称： ',
						lblMaterialName, '-', '图号： ', lblDocNo, '-', btnCancel],
				frame : false,
				layout : 'fit',
				items : [txtImage]
			});

	var tooltip = null;
	// 弹出窗口
	var win = new Ext.Window({
		title : '附件查看',
		width : 500,
		height : 320,
		modal : true,
		buttonAlign : "center",
		resizable : false,
		layout : 'fit',
		closeAction : 'hide',
		items : [accessoryPanel],

		listeners : {
			"show" : function() {
				if (tooltip == null) {
					tooltip = new Ext.ToolTip({
								target : 'materialName',
								html : "&nbsp;",
								dismissDelay : 0,
								showDelay : 200,
								hideDelay : 0,
								autoHeight : true,
								autowidth : true
							});
					tooltip.on("show", function() {
								var _ttv = Ext.DomQuery.select(".x-tip-body",
										this.el.dom)[0];
								if (win["_materialName"]) {
									_ttv.firstChild.nodeValue = win["_materialName"];
								} else {
									_ttv.firstChild.nodeValue = "&nbsp;"
								}
							})
				}
			}
		}
	});

	// ↑↑********弹出窗口*********↑↑//

	// ↓↓********** 主画面*******↓↓//

	// 查询条件
	var txtFuzzy = new Ext.form.TextField({
				id : "fuzzy",
				name : "fuzzy",
				emptyText : '物料名称/物料编码/规格型号/物料分类',
				width : 220
			});
	// 模糊查询
	var btnQuery = new Ext.Button({
				text : "模糊查询",
				handler : function() {
					materialStore.load({
								params : {
									start : 0,
									limit : Constants.PAGE_SIZE
								}
							});
				}
			});
	// 确认
	var btnSave = new Ext.Button({
				text : "确认",
				iconCls : Constants.CLS_SAVE,
				handler : function() {
					// 如果没有选择，弹出提示信息
					if (!materialGrid.selModel.hasSelection()) {
						Ext.Msg.alert(Constants.SYS_REMIND_MSG,
								Constants.COM_I_001);
						return;
					}
					// 返回
					var modifyRec = materialGrid.getSelectionModel()
							.getSelections();
					if (modifyRec.length == 1) {
						returnList();
					} else {
						// for (var i = 0; i < modifyRec.length; i += 1) {
						// alert(Ext.encode(modifyRec[i].data))
						// }
						window.returnValue = modifyRec;
						// 关闭画面
						window.close();
					}
				}
			});
	// 取消
	var btnCancel = new Ext.Button({
		text : "取消",
		iconCls : Constants.CLS_CANCEL,
		handler : function() {
			var obj = new Object();
			// 返回值
			obj.materialId = "", obj.materialNo = "", obj.materialName = "", obj.specNo = "", obj.parameter = "", obj.stockUmId = "", obj.factory = "", obj.docNo = "", obj.className = "", obj.locationNo = "", obj.maxStock = "", obj.qaControlFlag = "", window.returnValue = obj;
			// 关闭画面
			window.close();
		}
	});

	// 物料信息明细部
	var material = Ext.data.Record.create([
			// 物料ID
			{
		name : 'materialId'
	},
			// 物料编码
			{
				name : 'materialNo'
			},
			// 物料名称
			{
				name : 'materialName'
			},
			// 规格型号
			{
				name : 'specNo'
			},
			// 材质/参数
			{
				name : 'parameter'
			},
			// start jincong 增加单位ID 2008-01-10
			// 单位ID
			{
				name : 'stockUmId'
			},
			// end jincong 增加单位ID 2008-01-10
			// 单位
			{
				name : 'stockUmName'
			},
			// 生产厂家
			{
				name : 'factory'
			},
			// 图号
			{
				name : 'docNo'
			},
			// ・・・（附件选择)
			{
				name : 'accessory'
			},
			// 物料类别名称
			{
				name : 'className'
			},
			// 最大库存量
			{
				name : 'maxStock'
			},
			// 是否免检
			{
				name : 'qaControlFlag'
			},
			// 采购计量单位ID
			{
				name : 'purUmId'
			}, {
				name : 'stdCost'
			},
			// 物料分类 add by ltong
			{
				name : 'classNo'
			}]);
	// 物料grid的store
	var materialStore = new Ext.data.JsonStore({
				url : 'resource/getMaterialNameList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : material
			});
	// before load事件,传递查询字符串作为参数
	materialStore.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							fuzzy : txtFuzzy.getValue(),
							materialClassCode : materialClassCode
						});
			});
	// 载入数据
	materialStore.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			});

	var smAnnex = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false
			});

	// 物料grid
	var materialGrid = new Ext.grid.GridPanel({
		region : "center",
		// layout : 'fit',
		autoScroll : true,
		height : 540,
		isFormField : false,
		border : true,
		anchor : "0",
		// 标题不可以移动
		enableColumnMove : false,
		store : materialStore,
		columns : [smAnnex, new Ext.grid.RowNumberer({
							header : "行号",
							width : 35
						}),
				// 物料ID
				{
					header : "物料ID",
					width : 100,
					sortable : false,
					hidden : true,
					dataIndex : 'materialId'
				},
				// 物料编码
				{
					header : "物料编码",
					width : 100,
					sortable : false,
					dataIndex : 'materialNo'
				},
				// 物料名称
				{
					header : "物料名称",
					width : 200,
					sortable : false,
					dataIndex : 'materialName'
				},
				// 规格型号
				{
					header : "规格型号",
					width : 100,
					sortable : false,
					dataIndex : 'specNo'
				},
				// 材质/参数
				{
					header : "材质/参数",
					width : 100,
					sortable : false,
					dataIndex : 'parameter'
				},
				// 单位
				{
					header : "单位",
					width : 100,
					sortable : false,
					dataIndex : 'stockUmName'
				}, {
					header : "标准单价",
					width : 100,
					sortable : false,
					dataIndex : 'stdCost'
				},
				// 生产厂家
				{
					header : "生产厂家",
					width : 100,
					sortable : false,
					dataIndex : 'factory'
				},
				// 图号
				{
					header : "图号",
					width : 100,
					sortable : false,
					dataIndex : 'docNo'
				},
				// ・・・（附件选择)
				{
					header : " ",
					width : 35,
					sortable : false,
					dataIndex : 'accessory',
					renderer : creatButton
				},
				// 物料类别名称
				{
					header : "物料类别名称",
					width : 100,
					sortable : false,
					dataIndex : 'className'
				},
				// 最大库存量
				{
					header : "最大库存量",
					width : 100,
					hidden : true,
					sortable : false,
					dataIndex : 'maxStock'
				},
				// 是否免检
				{
					header : "是否免检",
					width : 100,
					hidden : true,
					sortable : false,
					dataIndex : 'qaControlFlag'
				},
				// 物料分类 add by ltong 
				{
					header : "物料分类",
					width : 100,
					hidden : true,
					sortable : false,
					dataIndex : 'classNo'
				}],
		// autoSizeColumns : true,
		sm : new Ext.grid.CheckboxSelectionModel({
					singleSelect : false
				}),
		// 分页
		bbar : new Ext.PagingToolbar({
					pageSize : Constants.PAGE_SIZE,
					store : materialStore,
					displayInfo : true,
					displayMsg : Constants.DISPLAY_MSG,
					emptyMsg : Constants.EMPTY_MSG
				}),
		// viewConfig : {
		// forceFit : true
		// },
		// grid监听
		listeners : {
			cellclick : function(grid, rowIndex, columnIndex, e) {
				var record = grid.getStore().getAt(rowIndex);
				var fieldName = grid.getColumnModel().getDataIndex(columnIndex);
				if ('accessory' == fieldName) {
					var _materialName = record.get("materialName") || "";
					win["_materialName"] = _materialName;
					win.show();

					lblMaterialId.setText(record.get("materialNo") == null
							? ""
							: Ext.util.Format.substr(record.get("materialNo"),
									0, 9));

					if (_materialName.length > 8) {
						_materialName = Ext.util.Format.substr(_materialName,
								0, 8)
								+ "...";
					}
					lblMaterialName.setText(_materialName);
					lblDocNo
							.setText(record.get("docNo") == null
									? ""
									: Ext.util.Format.substr(record
													.get("docNo"), 0, 9));
					Ext.get("materialMap").dom.src = "resource/getMaterialMap.action?docNo="
							+ record.get("docNo");
				}
			}
		}
	});

	// 表单panel
	var formPanel = new Ext.FormPanel({
				tbar : [txtFuzzy, '-', btnQuery, '-', btnSave, '-', btnCancel],
				labelAlign : 'right',
				border : true,
				autoScroll : true,
				items : [materialGrid]
			});

	// ↑↑********** 主画面*******↑↑//

	// ↓↓*******************************处理****************************************

	/**
	 * 双击事件
	 */
	materialGrid.addListener('rowdblclick', returnList);
	function returnList() {
		// 选择的记录
		var obj = new Object();
		var modifyRec = materialGrid.getSelectionModel().getSelections();
		// // 返回值
		// obj.materialId = record.get("materialId"), obj.materialNo = record
		// .get("materialNo"), obj.materialName = record
		// .get("materialName"), obj.specNo = record.get("specNo"),
		// obj.parameter = record
		// .get("parameter"), obj.stockUmId = record.get("stockUmName"),
		// // start jincong 增加单位ID 2008-01-10
		// obj.stock = record.get("stockUmId"),
		// // end jincong 增加单位ID 2008-01-10
		// obj.factory = record
		// .get("factory"), obj.docNo = record.get("docNo"), obj.className =
		// record
		// .get("className"), obj.locationNo = record.get("locationNo"),
		// obj.maxStock = record
		// .get("maxStock"), obj.qaControlFlag = record
		// .get("qaControlFlag");
		// // 采购计量单位ID
		// obj.purUmId = record.get("purUmId");
		// window.returnValue = obj;
		window.returnValue = modifyRec;
		// 关闭画面
		window.close();
	}
	/**
	 * 弹出窗口
	 */
	function creatButton() {
		var str = "<input type='button' value='···' width=20 style='height:20' />";
		return str;
	}
	// -----------add by fyyang 090623--------------------------
	// var root =new Ext.tree.AsyncTreeNode({
	// text : '物料',
	// isRoot : true,
	// id : '-1'
	//		
	// });

	// var mytree = new Ext.tree.TreePanel({
	// renderTo : "treepanel",
	// autoHeight:true,
	// border:false,
	// //height : 900,
	// root : root,
	// requestMethod : 'GET',
	// loader : new Ext.tree.TreeLoader({
	// url : "resource/getMaterialClass.action"
	// })
	// });

	// mytree.on("click", clickTree, this);
	// mytree.on('beforeload', function(node) {
	// mytree.loader.dataUrl = 'resource/getMaterialClass.action';
	// }, this);
	//
	// function clickTree(node) {
	// materialClassCode=node.id;
	//		
	// materialStore.load({
	// params : {
	// start : 0,
	// limit : Constants.PAGE_SIZE
	// }
	// });
	//		
	// };
	// root.expand();//展开根节点
	// ----------------------------------------------------------------

	// 显示区域
	var layout = new Ext.Viewport({
				layout : 'border',
				margins : '0 0 0 0',
				border : true,
				items : [
						// {
						// region : 'west',
						// split : true,
						// width : 160,
						// layout : 'fit',
						// minSize : 175,
						// maxSize : 600,
						// margins : '0 0 0 0',
						// collapsible : true,
						// border : false,
						// autoScroll : true,
						// items : [mytree]
						// },
						{
					region : "center",
					layout : 'fit',
					collapsible : true,
					autoScroll : true,
					items : [formPanel]
				}]
			});

});