Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
function getDate() {
	var d, s, t;
	d = new Date();
	s = d.getFullYear().toString(10) + "-";
	t = d.getMonth() + 1;
	s += (t > 9 ? "" : "0") + t + "-";
	t = d.getDate();
//	s += (t > 9 ? "" : "0") + t + " ";
//	t = d.getHours();
//	s += (t > 9 ? "" : "0") + t + ":";
//	t = d.getMinutes();
//	s += (t > 9 ? "" : "0") + t + ":";
//	t = d.getSeconds();
	s += (t > 9 ? "" : "0") + t;
	return s;
}
Ext.onReady(function() {
	var reportCode = "";
	var sessWorname;
	var sessWorcode;
	var sessDeptCode;
	var sessDeptName;

	var bview;

	var annex_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});
	
	var fuzzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});
	
	var annex_item = Ext.data.Record.create([{
		name : 'loadId'
	}, {
		name : 'loadCode'
	}, {
		name : 'loadName'
	}, {
		name : 'reportCode'
	}, {
		name : 'reportTime'
	}, {
		name : 'annexAddress'
	}, {
		name : 'loadBy'
	}, {
		name : 'loadDate'
	}, {
		name : 'firstDeptCode'
	}, {
		name : 'enterpriseCode'
	}, {
		name : 'isUse'
	}]);

	var annex_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'manager/queryReportLoadListByReportCode.action'
		}),
		reader : new Ext.data.JsonReader({
			root : "list",
			totalProperty : "totalCount"
		}, annex_item)
	});

	var annex_item_cm = new Ext.grid.ColumnModel([annex_sm,
			new Ext.grid.RowNumberer({
				header : '序号',
				width : 35,
				align : 'center'
			}), {
				header : 'ID',
				dataIndex : 'loadId',
				hidden : true,
				align : 'center'
			}, {
				header : '编号',
				dataIndex : 'loadCode',
				hidden : true,
				align : 'center'
			}, {
				header : '报表名称',
				width : 150,
				dataIndex : 'loadName',
				align : 'center'
			}, {
				header : '报表日期',
				dataIndex : 'reportTime',
				align : 'center',
				width : 150
			}, {
				header : '上传日期',
				dataIndex : 'loadDate',
				width : 120,
				align : 'center',
				renderer : function(val) {
					if (val != null && val != "") {
						return val.substring(0, 10) + " "
								+ val.substring(10,10)
					}

				}
			}, {
				header : '上传人',
				dataIndex : 'loadBy',
				align : 'center'
			}, {
				header : '查看模板',
				dataIndex : 'annexAddress',
				align : 'center',
				renderer : function(val) {
					// window.open(val);
					// var val = record.get("fileCode")
					// + record.get("fileType");
					if (val != "" && val != null) {
						return "<a style=\"cursor:hand;color:red\" onClick=\"window.open('"
								+ val + "');\"/>查看报表</a>"
					} else {
						return "";
					}
				}
			}]);

	var loadId = new Ext.form.TextField({
		id : 'loadId',
		name : 'reportLoad.loadId',
		xtype : 'hidden',
		fieldLabel : '序号',
		readOnly : false,
		hidden : true,
		hideLabel : true,
		anchor : '95%'
	});

	var loadCode = new Ext.form.TextField({
		fieldLabel : '文件编号',
		readOnly : true,
		xtype : 'hidden',
		width : 108,
		id : 'loadCode',
		hidden : true,
		hideLabel : true,
		name : "reportLoad.loadCode",
		anchor : '95%'
	});

	var lastModifiedDate = new Ext.form.TextField({
		id : 'lastModifiedDate',
		fieldLabel : "上传时间",
		name : 'lastModifiedDate',
		type : 'textfield',
		readOnly : true,
		style : 'cursor:pointer',
		anchor : "95%",
		value : getDate()
			// listeners : {
			// focus : function() {
			// var pkr = WdatePicker({
			// startDate : '%y-%M-%d 00:00:00',
			// dateFmt : 'yyyy-MM-dd HH:mm:ss',
			// alwaysUseStartDate : true
			// });
			// }
			// }
	});

	var fjdocFile = new Ext.form.FileUploadField ({
		id : "oriFile",
		fieldLabel : '选择附件',
		anchor : "95%",
		height : 22,
		name : 'fjdocFile',
		buttonCfg : {
			text : '浏览...',
			iconCls : 'upload-icon'
		}
	})
	
	fjdocFile.on('fileselected',function(file,val) {
		var result = val.substring(val.lastIndexOf("\\") + 1);
		loadName.setValue(result);
	})
	
	var loadName = new Ext.form.TextField({
		fieldLabel : '报表名称',
		readOnly : false,
		width : 108,
		id : 'loadName',
		name : "reportLoad.loadName",
		anchor : '95%'
	});

	var reportTime = new Ext.form.TextField({
		id : 'reportTime',
		fieldLabel : "报表日期",
		name : 'reportLoad.reportTime',
		readOnly : true,
		style : 'cursor:pointer',
		anchor : "95%",
		value : getDate(),
	 	listeners : {
			focus : function() {
			 	var pkr = WdatePicker({
			 	startDate : '%y-%M-%d',
			 	dateFmt : 'yyyy-MM-dd',
			 	alwaysUseStartDate : true
		 	});
		 	}
		}
	});

	var lastModifiedName = new Ext.form.TextField({
		id : 'lastModifiedName',
		xtype : 'textfield',
		fieldLabel : '上传人',
		readOnly : true,
		anchor : '95%'
	});

	var reportCodeTxt = new Ext.form.TextField({
		id : 'reportCodeTxt',
		name : 'reportLoad.reportCode',
		readOnly : false,
		hidden : true,
		hideLabel : true,
		anchor : '95%'
	});

	var doccontent = new Ext.form.FieldSet({
		height : '100%',
		layout : 'form',
		items : [loadId, loadCode, fjdocFile, loadName, reportTime,
				lastModifiedDate, lastModifiedName, reportCodeTxt]
	});
	
	function checkInput() {
		var msg = "";
		if (Ext.get('oriFile').dom.value == "") {
			msg = "'附件'";
		}
		if (Ext.get('reportCodeTxt').dom.value == "") {
			msg = "'报表类型'";
		}
		if (msg != "") {
			Ext.Msg.alert("提示", "请输入" + msg);
			return false
		} else {
			return true;
		}
	}			

	var docform = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		id : 'shift-form',
		labelWidth : 80,
		autoHeight : true,
		fileUpload : true,
		region : 'center',
		border : false,
		items : [doccontent],
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
				if (!checkInput())
					return;
				if (!docform.getForm().isValid()) {
					return false;
				}
				Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_001,
					function(buttonobj) {
						if (buttonobj == "yes") {
							docform.getForm().submit({
								url : 'manager/saveOrUpdateReportLoad.action',
								method : 'post',
								params : {
									filePath : Ext.get('oriFile').dom.value
								},
								success : function(form, action) {
									var message = eval('(' + action.response.responseText
											+ ')');
									Ext.Msg.alert("成功", message.data);
									if(message.data.indexOf('成功') != -1){
										queryRecord();
										docwin.hide();
									}
								},
								failure : function(form, action) {
									Ext.Msg.alert('错误', '出现未知错误.');
								}
							})
						}
				});
			}
		}, {
			text : '取消',
			iconCls : 'cancer',
			handler : function() {
				docform.getForm().reset();
				docwin.hide();
			}
		}]
	});

	var docwin = new Ext.Window({
		title : '新增',
		layout : 'fit',
		modal : true,
		autoHeight : true,
		width : 450,
		closeAction : 'hide',
		items : [docform]
	});

	var annextbar = new Ext.Toolbar({
		id : 'annextbar',
		items : ['名称：', fuzzy, {
			text : "查询",
			iconCls : 'query',
			handler : queryRecord
		}, {
			id : 'btnAnnexAdd',
			text : "新增",
			iconCls : 'add',
			handler : function() {
				if (id == "") {
					Ext.Msg.alert('提示', '请先从列表中选择合同！');
					return false;
				}
				docwin.setTitle("增加报表模板");
				docform.getForm().reset();
				docwin.show();
				Ext.get('lastModifiedName').dom.value = sessWorname;
				reportCodeTxt.setValue(reportCode);
			}
//		}, '-', {
//			id : 'btnAnnexSave',
//			text : "修改",
//			iconCls : 'update',
//			handler : updateRecord
//		}, '-', {
//			id : 'btnAnnexDelete',
//			text : "删除",
//			iconCls : 'delete',
//			handler : function() {
//				var selrows = annexGrid.getSelectionModel().getSelections();
//				if (selrows.length > 0) {
//					var ids = [];
//					for (var i = 0; i < selrows.length; i += 1) {
//						var member = selrows[i].data;
//						if (member.checkFileId) {
//							ids.push(member.checkFileId);
//						} else {
//							// store.remove(store.getAt(i));
//						}
//					}
//					Ext.Msg.confirm('提示', '删除的数据您将不能恢复,确定要删除吗?', function(b) {
//						if (b == "yes") {
//							var record = annexGrid.getSelectionModel()
//									.getSelected();
//							Ext.Ajax.request({
//								url : 'manageproject/deleteCheckFile.action',
//								params : {
//									checkFileId : ids
//								},
//								method : 'post',
//								waitMsg : '正在删除数据...',
//								success : function(result, request) {
//									Ext.MessageBox.alert('提示', '删除成功!');
//									queryRecord();
//								},
//								failure : function(result, request) {
//									Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
//								}
//							});
//						}
//
//					});
//
//				} else {
//					Ext.Msg.alert('提示', '请选择您要删除的合同附件！');
//				}
//			}
		}]
	});
	function queryRecord() {
		if(reportCode != "") {
			annex_ds.load({
				params : {
					loadName : fuzzy.getValue(),
					reportCode : reportCode,
					start : 0,
					limit : 18
				}
			});
		} else {
			Ext.Msg.alert('提示','请先选择报表类型！')
		}
	}
	var annexGrid = new Ext.grid.GridPanel({
		ds : annex_ds,
		cm : annex_item_cm,
		sm : annex_sm,
		region : "center",
		layout : 'fit',
		split : true,
		autoSizeColumns : true,
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : annex_ds,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		}),
		// collapsible : true,
		tbar : annextbar,
		border : false,
		viewConfig : {
			forceFit : true
		}
	});

	annexGrid.on('rowdblclick', updateRecord);

	function updateRecord() {
		docmethod = 'update';
		var selrows = annexGrid.getSelectionModel().getSelections();
		if (selrows.length > 0) {
			var record = annexGrid.getSelectionModel().getSelected();
			docform.getForm().reset();
			docwin.show(Ext.get('btnAnnexSave'));
			docform.getForm().loadRecord(record);
			if (record.data.fileType != null && record.data.fileType != "") {
				fileType.setValue(record.data.fileType);
			}
			if (record.data.lastModifiedDate != null
					&& record.data.lastModifiedDate != "") {
				lastModifiedDate.setValue(record.data.lastModifiedDate
						.substring(0, 10)
						+ " " + record.data.lastModifiedDate.substring(10,10));
			}
			if (record.data.lastModifiedBy != null
					&& record.data.lastModifiedBy != "") {
				Ext.get('lastModifiedName').dom.value = record.data.lastModifiedBy;
			}
			 if(record.get("fileUrl")!=null&&record.get("fileUrl")!="")
		        {
		       bview=record.get("fileUrl");
		       btnView.setVisible(true);
		         Ext.get("oriFile").dom.value = bview.replace('/power/upload_dir/project/','');
		        }
		        else
		        {
		        	  btnView.setVisible(false);
		        }
			docwin.setTitle("修改竣工报告书");
		} else {
			Ext.Msg.alert('提示', '请选择您要修改的合同附件！');
		}
	}

//	var layout = new Ext.Panel({
//		autoWidth : true,
//		autoHeight : true,
//		border : false,
//		autoScroll : true,
//		split : true,
//		items : [annexGrid]
//	});
//	new Ext.Viewport({
//		enableTabScroll : true,
//		layout : "border",
//	//	layout : "fit",
//		items : [annexGrid]
//	});
	
	var currentNode = new Object();
	var cNode = "";
	var addFaCode = "";
	
	var root = new Ext.tree.AsyncTreeNode({
		id : "root",
		text : "指标报表类型"
	});

	currentNode = root;
	var mytree = new Ext.tree.TreePanel({
		layout : 'fit',
		animate : true,
		autoHeight : true,
		allowDomMove : false,
		autoScroll : true,
		containerScroll : true,
		collapsible : true,
		split : true,
		border : false,
		rootVisible : true,
		root : root,
		animate : true,
		enableDD : false,
		border : false,
		containerScroll : true,

		// autoHeight : true,
		// root : root,
		// border : false,
		loader : new Ext.tree.TreeLoader({
			url : "manager/getReportTreeList.action",
			baseParams : {
				id : 'root'
			}
		})
	});

	root.expand();
	mytree.on("click", clickTree, this);
	mytree.on('beforeload', function(node) {
		mytree.loader.dataUrl = 'manager/getReportTreeList.action?id='
				+ node.id;
	}, this);
	
	function clickTree(node,event) {
		
		 event.stopEvent();
		 
		if (node.id == "root") {
			// 根节点
			currentNode = node;
			cNode = node;
			annexGrid.getView().refresh();
		} else {
			currentNode = node;
			cNode = node;
			reportCode = node.id;
			annex_ds.load({
				params : {
					loadName : fuzzy.getValue(),
					reportCode : reportCode,
					start : 0,
					limit : 18
				}
			});
		}
	};
	
	var layout = new Ext.Viewport({
		layout : "border",
		border : false,
		items : [{
			region : "center",
			layout : 'fit',
			collapsible : true,
			split : true,
			margins : '0 0 0 0',
			// 注入表格
			items : [annexGrid]
		}, {
			title : "指标报表类型",
			region : 'west',
			margins : '0 0 0 0',
			split : true,
			collapsible : true,
			titleCollapse : true,
			width : 200,
			layoutConfig : {
				animate : true
			},
			items : [mytree]
		}]
	})
	
//	queryRecord();
	getWorkCode();

	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result.workerCode) {
					// 设定默认工号
					sessWorcode = result.workerCode;
					sessWorname = result.workerName;
					sessDeptCode = result.deptCode;
					sessDeptName = result.deptName;
				}
			}
		});
	}
});