Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	Ext.QuickTips.init();

	function getParameter(psName) {
		var url = self.location;
		var result = "";
		var str = self.location.search.substring(0);
		if (str.indexOf(psName) != -1
				&& (str.substr(str.indexOf(psName) - 1, 1) == "?" || str
						.substr(str.indexOf(psName) - 1, 1) == "&")) {
			if (str.substring(str.indexOf(psName), str.length).indexOf("&") != -1) {
				var Test = str.substring(str.indexOf(psName), str.length);
				result = Test.substr(Test.indexOf(psName), Test.indexOf("&")
								- Test.indexOf(psName));
				result = result.substring(result.indexOf("=") + 1,
						result.length);
			} else {
				result = str.substring(str.indexOf(psName), str.length);
				result = result.substring(result.indexOf("=") + 1,
						result.length);
			}
		}
		return result;
	}

	var woCode = getParameter("woCode");
	var opCode = getParameter("opCode");

	/** 窗口元素↓↓* */

	var datalist = new Ext.data.Record.create([{
				name : 'id',
				mapping:0
			}, {
				name : 'woCode',
				mapping:1
			}, {
				name : 'operationStep',
				mapping:2
			}, {
				name : 'materialId',
				mapping:3
			},{
				name : 'materialIdName',
				mapping:4
			}, {
				name : 'planItemQty',
				mapping:5
			}, {
				name : 'planMaterialPrice',
				mapping:6
			}, {
				name : 'planVendor',
				mapping:7
			}, {
				name : 'planVendorName',
				mapping:8
			},{
				name : 'orderBy',
				mapping:9
			}, {
				name : 'directReq',
				mapping:10
			}, {
				name : 'enterprisecode',
				mapping:11
			}, {
				name : 'ifUse',
				mapping:12
			}]);

	var centerGridsm = new Ext.grid.CheckboxSelectionModel();

	var centerGrids = new Ext.data.JsonStore({
				url : 'equstandard/getEquCStandardMainmatList.action?woCode='
						+ woCode + '&opCode=' + opCode,
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});

	centerGrids.load({
				params : {
					start : 0,
					limit : 18
				}
			});

	/** centerGrid的按钮组↓↓* */

	function addRecords() {
		var count = centerGrids.getCount();
		var currentIndex = count;
		var o = new datalist({
			         'id':'',
					'woCode' : woCode,
					'operationStep' : opCode,
					'materialId' : '',
					'materialIdName':'',
					'planItemQty' : '',
					'planMaterialPrice' : '',
					'planVendor' : '',
					'planVendoNamer' : '',
					'orderBy' : '',
					'directReq':''
				
				});

		centerGrid.stopEditing();
		centerGrids.insert(currentIndex, o);
		centerGridsm.selectRow(currentIndex);
		centerGrid.startEditing(currentIndex, 1);
		// resetLine();
	}

	// 删除记录
	var ids = new Array();
	function deleteRecords() {
		centerGrid.stopEditing();

		var centerGridsm = centerGrid.getSelectionModel();
		var selected = centerGridsm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("id") != null&&member.get("id")!="") {
					ids.push(member.get("id"));
				}
				centerGrid.getStore().remove(member);
				centerGrid.getStore().getModifiedRecords().remove(member);
			}
			// resetLine();
		}
	}

	// 保存
	
	function save() {
		var alertMsg="";
		centerGrid.stopEditing();
		var modifyRec = centerGrid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			Ext.Msg.confirm('提示', '确定要保存修改数据吗?', function(button) {
				if (button == 'yes') { 
			var updateData = new Array();
			for (var i = 0; i < modifyRec.length; i++) {
						if (modifyRec[i].get("planItemQty") == null
								|| modifyRec[i].get("planItemQty") == "") {
							alertMsg += "请输入物资数量！";
						}
						if (alertMsg != "") {
							Ext.Msg.alert("提示", alertMsg);
							return;
						}
						updateData.push(modifyRec[i].data);
					}

			Ext.Ajax.request({
						url : 'equstandard/saveEquCStandardMainmat.action',
						method : 'post',
						params : {
							isUpdate : Ext.util.JSON.encode(updateData),
							isDelete : ids.join(",")
						},
						success : function(result, request) {
							var o = eval('(' + result.responseText + ')');
							Ext.MessageBox.alert('提示信息', o.msg);
							centerGrids.rejectChanges();
							ids = [];
							centerGrids.reload();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('提示信息', '未知错误！')
						}
					})
		} 
	})
		}
		else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！');
			centerGrids.reload();
			}
	}
	
	// 取消
	function cancer() {
		var modifyRec = centerGrids.getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			Ext.Msg.confirm('提示', '确定要放弃修改吗?', function(button) {
				if (button == 'yes') {
					centerGrids.reload();
					centerGrids.rejectChanges();
					ids = [];
				} else {

				}
			})
		} else {
			centerGrids.reload()
		};
	}

	// 新增
	var centerGridAdd = new Ext.Button({
				text : '新增',
				iconCls : 'add',
				handler : addRecords

			});

	var centerGridDel = new Ext.Button({
				text : '删除',
				iconCls : 'delete',
				handler : deleteRecords
			});

	var centerGridSave = new Ext.Button({
				text : '保存修改',
				iconCls : 'save',
				handler : save
			});

	// 取消
	var centerGridCancer = new Ext.Button({
		text : '取消',
		iconCls : 'cancer',
		handler : cancer
			
		});
	

	var centerGrid = new Ext.grid.EditorGridPanel({
		ds : centerGrids,
		sm : centerGridsm,
		clicksToEdit : 1,
		columns : [centerGridsm, new Ext.grid.RowNumberer(), {
			header : "物资",
			width :180,
			sortable : false,
			dataIndex : 'materialId',
			renderer : function(value, metadata, record) {
								if (value != null)
									return record.get('materialIdName')
							},
			editor : new Ext.form.TextField({
				readOnly : true,
				listeners : {
					focus : function(e) {
						var args = {
							selectModel : 'single',
							rootNode : {
								id : '-1',
								text : '合肥电厂'
							}
						}
						var mate = window
								.showModalDialog(
										'../../../../run/resource/storage/issueheadregister/RP001.jsp',
										window,
										'dialogWidth=900px;dialogHeight=610px;status=no');// modify
						// by
						// ywliu
						// 091019
						if (typeof(mate) != "undefined") {
							var record = centerGrid.getSelectionModel()
									.getSelected();
							record.set("materialId",
									mate.materialId);
							record.set("materialIdName", mate.materialName);
						}
						this.blur();

					}
				}
			})
		}, {
			header : "物资ID",
			sortable : false,
			dataIndex : 'materialId',
			hidden : true
		}, {
			header : "数量",
			sortable : false,
			dataIndex : 'planItemQty',
			editor : new Ext.form.NumberField({
							allowDecimals : true,
							allowNegative:false,
							decimalPrecision : 4
						})
		}, {
			header : "单价",
			sortable : false,
			// add by liuyi 20100326
			hidden : true,
			dataIndex : 'planMaterialPrice',
			editor : new Ext.form.NumberField({
							allowDecimals : true,
							allowNegative:false,
							decimalPrecision : 4
						})
		}, {
			header : "主要供应商",
			sortable : false,
			width:150,
			dataIndex : 'planVendor',
			renderer : function(value, metadata, record) {
								if (value != null)
									return record.get('planVendorName')
							},
			editor : new Ext.form.TextField({
				readOnly : true,
				listeners : {
					focus : function(e) {
						var url = "../../../../comm/jsp/supplierQuery/supplierQuery.jsp";
						var selectedMember = window
								.showModalDialog(
										url,
										null,
										'dialogWidth=800px;dialogHeight=550px;center=yes;help=no;resizable=no;status=no;');
						if (typeof(selectedMember) != "undefined") {
							var record = centerGrid.getSelectionModel()
									.getSelected();
							record.set("planVendor",
									selectedMember.supplier);
							record.set("planVendorName",
									selectedMember.supplyName);
						}
						this.blur();
					}
				}
			})
		}, {
			header : "主要供应商ID",
			sortable : false,
			dataIndex : 'planVendor',
			hidden : true

		},{
			header : "排序号",
			sortable : false,
			dataIndex : 'orderBy',
			editor : new Ext.form.NumberField({
			allowDecimals:false})
		
		}],
		tbar : [centerGridAdd, centerGridDel, centerGridCancer, {
					xtype : "tbseparator"
				}, centerGridSave],
		frame : true,
		border : true,
		enableColumnHide : false,
		enableColumnMove : false,
		iconCls : 'icon-grid'
	});

	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							title : "",
							region : 'center',
							layout : 'fit',
							border : false,
							margins : '0',
							height : 200,
							split : false,
							collapsible : false,
							items : [centerGrid]
						}]
			});
})