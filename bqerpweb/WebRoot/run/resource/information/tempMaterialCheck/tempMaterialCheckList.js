Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	if (parent.first) {
		parent.Ext.getCmp("maintab").setActiveTab(1);
		parent.Ext.getCmp("maintab").setActiveTab(0);
		parent.first = false;

	}
	// 对应变更
	function numberFormat(value) {
		if (value === "" || value == null) {
			//			return value
			return "0.00";
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
			if (sub.length > 3) {
				sub = sub.substring(0, 3);
			}
		}
		var r = /(\d+)(\d{3})/;
		while (r.test(whole)) {
			whole = whole.replace(r, '$1' + ',' + '$2');
		}
		v = whole + sub;
		return v;
	}
	// 物料名称
	var txtMaterialName = new Ext.form.TextField({
		fieldLabel : '物料名称',
		anchor : '100%',
		id : "materialName",
		name : "materialName"
	});
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'temp.tempId'
	}, {
		name : 'temp.materialNo'
	}, {
		name : 'temp.materialName'
	}, {
		name : 'temp.specNo'
	}, {
		name : 'temp.parameter'
	}, {
		name : 'temp.stockUmId'
	}, {
		name : 'temp.defaultWhsNo'
	}, {
		name : 'temp.maertialClassId'
	}, {
		name : 'maertialClassName'
	}, {
		name : 'temp.factory'
	}, {
		name : 'temp.actPrice'
	}, {
		name : 'checkDate'
	}, {
		name : 'temp.checkBy'
	}, {
		name : 'checkName'
	}, {
		name : 'temp.telNo'
	}, {
		name : 'temp.memo'
	}, {
		name : 'temp.statusId'
	}, {
		name : 'approveDate'
	}, {
		name : 'temp.approveBy'
	}, {
		name : 'approveName'
	}]);
	var dataProxy = new Ext.data.HttpProxy({
		url : 'resource/findApproveList.action'
	});
	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);
	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});

	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});

	var grid = new Ext.grid.GridPanel({
		region : "center",
		store : store,
		viewConfig : {
			forceFit : true
		},
		loadMask : {
			msg : '<img src="comm/images/extanim32.gif" width="32" height="32" style="margin-right: 8px;" align="absmiddle" />读取数据中 ...'
		},
		columns : [sm, new Ext.grid.RowNumberer({
			header : '序号',
			width : 35,
			align : 'left'
		}), {
			header : "ID",
			width : 100,
			sortable : true,
			dataIndex : 'temp.tempId',
			hidden : true
		}, {
			header : "正式物资编码",
			width : 180,
			sortable : true,
			dataIndex : 'temp.materialNo'
		}, {
			header : "物资名称",
			width : 130,
			sortable : true,
			dataIndex : 'temp.materialName'
		}, {
			header : "状态",
			width : 120,
			sortable : true,
			renderer : function(value) {
				if (value == 1) {
					return "未发送";
				} else if (value == 2) {
					return "已发送";
				} else if (value == 3) {
					return "已审核";
				} else
					;
			},
			dataIndex : 'temp.statusId'
		}, {
			header : "规格型号",
			width : 130,
			sortable : true,
			dataIndex : 'temp.specNo'
		}, {
			header : "材质/参数",
			width : 100,
			sortable : true,
			dataIndex : 'temp.parameter'
		}, {
			header : "单位",
			width : 80,
			sortable : true,
			renderer : unitName,
			dataIndex : 'temp.stockUmId'
		}, {
			header : "仓库",
			width : 120,
			sortable : true,
			renderer : getWhs,
			dataIndex : 'temp.defaultWhsNo'
		}, {
			header : "物资类别",
			width : 120,
			sortable : true,
			dataIndex : 'maertialClassName'
		}, {
			header : "生产厂家",
			width : 120,
			sortable : true,
			dataIndex : 'temp.factory'
		}, {
			header : "实际阶格",
			width : 120,
			sortable : true,
			renderer : numberFormat,
			dataIndex : 'temp.actPrice'
		}, {
			header : "上报人",
			width : 120,
			sortable : true,
			dataIndex : 'checkName'
		}, {
			header : "上报日期",
			width : 120,
			sortable : true,
			dataIndex : 'checkDate'
		}, {
			header : "联系电话",
			width : 120,
			sortable : true,
			dataIndex : 'temp.telNo'
		}, {
			header : "备注",
			width : 120,
			sortable : true,
			dataIndex : 'temp.memo'
		}, {
			header : "审核人",
			width : 120,
			sortable : true,
			//hidden : true,
			dataIndex : 'approveName'
		}, {
			header : "审核日期",
			width : 120,
			sortable : true,
			//hidden : true,
			dataIndex : 'approveDate'
		}],
		sm : sm,
		tbar : ['物料名称:', txtMaterialName, {
			text : "查询",
			iconCls : 'query',
			handler : queryRecord
		}, '-', {
			text : "修改",
			iconCls : 'update',
			handler : updateRecord
		}, '-', {
			text : "生成正式物资编码",
			iconCls : 'add',
			handler : createMaterialNo
		},'-',{
		    text:'退回',
		    handler:opBack
		}],
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});
	
	//------------add by fyyang  090804 增加退回功能--------------------------
	var backReason=new Ext.form.TextArea({
	id : "backReason",
		fieldLabel : '退回原因',
		width : 200,
		name : 'backReason',
		height:80
	
	}); 
	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		labelWidth:80,
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
			iconCls:'save',
			handler : function() {
			var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var menber = selected[0];
	    var	id=menber.get('temp.tempId');
					Ext.Ajax.request({
						method : 'post',
						url : 'resource/backTempMaterial.action',
						params : {
							backReason : backReason.getValue(),
							tempId : id
						},
						success : function(action) {
							Ext.Msg.alert("提示", "退回成功！");
							win.hide();
							queryRecord();
							var myurl = "";
							parent.document.all.iframe2.src = myurl;
						},
						failure : function() {
							Ext.Msg.alert(Constants.SYS_REMIND_MSG,
									Constants.COM_E_014);
						}
					});
				
			}
		}, {
			text : '取消',
			iconCls:'cancer',
			handler : function() { 
				win.hide();
			}
		}]

	});
	
	function opBack()
	{
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var menber = selected[0];
		if (grid.selModel.hasSelection()) {
			
			var materialClassId = menber.get('temp.tempId');
			
			Ext.Msg.confirm("提示", "是否确定退回?", function(buttonobj) {
				if (buttonobj == "yes") {
					myaddpanel.getForm().reset();
				  win.show();
				}
			})
		} else {
			Ext.Msg.alert("提示", "请先选择要退回的记录!");
		}
	}
	
	//---------add end-------------------------------------------------
	
	grid.on('rowdblclick', updateRecord);
	function updateRecord() {
		if (grid.selModel.hasSelection()) {
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				var member = records[0];
				parent.currentRecord = member;
				var url = "run/resource/information/tempMaterialCheck/tempMaterialCheckBase.jsp";
				parent.document.all.iframe2.src = url;
				parent.Ext.getCmp("maintab").setActiveTab(1);
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}

	function confirm() {
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var menber = selected[0];
		var msg = "";
		if (menber.get("temp.defaultWhsNo") == null
				|| menber.get("temp.defaultWhsNo") == "") {
			msg = "仓库";
		}
		if (menber.get("temp.materialName") == ""
				|| menber.get("temp.materialName") == null) {
			if (msg != "") {
				msg = msg + ",物资名称";
			} else {
				msg = "物资名称";
			}
		}
		if (menber.get("temp.maertialClassId") == ""
				|| menber.get("temp.maertialClassId") == null) {
			if (msg != "") {
				msg = msg + ",物资类别";
			} else {
				msg = "物资类别";
			}
		}
		if (menber.get("temp.stockUmId") == null
				|| menber.get("temp.stockUmId") == "") {
			if (msg != "") {
				msg = msg + ",单位";
			} else {
				msg = "单位";
			}
		}
		if (msg != "") {
			Ext.Msg.alert('提示', "请先填写'" + msg + "'!");
			return false;
		}
		return true;
	}
	function createMaterialNo() {

		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var whsNo;
		var materialClassId;
		var id;
		var menber = selected[0];
		if (grid.selModel.hasSelection()) {
			if (confirm() == false)
				return;
			whsNo = menber.get('temp.defaultWhsNo');
			materialClassId = menber.get('temp.maertialClassId');
			id = menber.get('temp.tempId');
			Ext.Msg.confirm("提示", "是否确定生成正式物资编码?", function(buttonobj) {
				if (buttonobj == "yes") {
					Ext.Msg.wait("操作进行中...", "请稍候");
					Ext.Ajax.request({
						method : 'post',
						url : 'resource/addForUpdateMaterial.action',
						params : {
							whsNo : whsNo,
							materialClassId : materialClassId,
							tempId : id
						},
						success : function(action) {
							Ext.Msg.alert("提示", "物资编码生成成功！");
							queryRecord();
							var myurl = "";
							parent.document.all.iframe2.src = myurl;
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

	function queryRecord() {
		store.load({
			params : {
				start : 0,
				limit : 18,
				materialName : Ext.get("materialName").dom.value
			}
		});
	}

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});

	queryRecord();

	/**
	 * 格式化仓库数据
	 */
//	function comboBoxWarehouseRenderer(value, cellmeta, record) {
//		var whsNo = record.data["temp.defaultWhsNo"];
//		if (!whsNo) {
//			return;
//		}
//		var url = "resource/getWarehouseName.action?whsNo=" + whsNo;
//		var conn = Ext.lib.Ajax.getConnectionObject().conn;
//		conn.open("POST", url, false);
//		conn.send(null);
//		return conn.responseText;
//
//	};

	/**
	 * 格式化数据----计量单位
	 */
	function unitName(value) {
		if (value !== null && value !== "") {
			var url = "resource/getRS001UnitName.action?unitCode=" + value;
			var conn = Ext.lib.Ajax.getConnectionObject().conn;
			conn.open("POST", url, false);
			conn.send(null);
			return conn.responseText;
		} else {
			return "";
		}
	};

	/**
	 * 根据物料分类ID取仓库名称
	 */
	function getWhs(value, cellmeta, record, rowIndex, columnIndex, store) {
		if (record.data["temp.maertialClassId"] != null
				&& record.data["temp.maertialClassId"] != "") {

			var url = "resource/getParentCode.action?classId="
					+ record.data["temp.maertialClassId"];
			var conn = Ext.lib.Ajax.getConnectionObject().conn;
			conn.open("POST", url, false);
			conn.send(null);
			var obj = conn.responseText;
			var mytext = obj.substring(1, obj.length - 1);

			var arr = mytext.split(',');
			return arr[1];
		} else {
			return value;
		}

	}
});