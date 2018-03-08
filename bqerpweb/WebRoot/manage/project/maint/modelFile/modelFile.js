Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
function getDate() {
	var d, s, t;
	d = new Date();
	s = d.getFullYear().toString(10) + "-";
	t = d.getMonth() + 1;
	s += (t > 9 ? "" : "0") + t + "-";
	t = d.getDate();
	s += (t > 9 ? "" : "0") + t + " ";
	t = d.getHours();
	s += (t > 9 ? "" : "0") + t + ":";
	t = d.getMinutes();
	s += (t > 9 ? "" : "0") + t + ":";
	t = d.getSeconds();
	s += (t > 9 ? "" : "0") + t;
	return s;
}
Ext.onReady(function() {
	var method = "add";
	var sessWorname;
	var sessWorcode;
	var sessDeptCode;
	var sessDeptName;
	var bview;

	var fuzzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});

	var annex_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});

	var annex_item = Ext.data.Record.create([{
		name : 'checkFileId'
	}, {
		name : 'fileType'
	}, {
		name : 'fileNo'
	}, {
		name : 'fileName'
	}, {
		name : 'lastModifiedBy'
	}, {
		name : 'lastModifiedDate'
	}, {
		name : 'isModel'
	}, {
		name : 'fileUrl'
	}, {
		name : 'enterpriseCode'
	}, {
		name : 'isUse'
	}]);

	var annex_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'manageproject/findCheckFileList.action'
		}),
		reader : new Ext.data.JsonReader({}, annex_item)
	});

	var annex_item_cm = new Ext.grid.ColumnModel([annex_sm,
			new Ext.grid.RowNumberer({
				header : '序号',
				width : 50,
				align : 'center'
			}), {
				header : 'ID',
				dataIndex : 'checkFileId',
				hidden : true,
				align : 'center'
			}, {
				header : '编号',
				dataIndex : 'fileNo',
				hidden : true,
				align : 'center'
			}, {
				header : '名称',
				width : 150,
				dataIndex : 'fileName',
				align : 'center'
			}, {
				header : '文件类型',
				dataIndex : 'fileType',
				align : 'center',
				width : 150,
				renderer : function(val) {
					if (val == '1') {
						return '可行性报告';
					} else if (val == '2') {
						return '工程交工竣工验收证书';
					} else if (val == '3') {
						return '工程项目竣工验收会签单';
					}
				}
			}, {
				header : '上传日期',
				dataIndex : 'lastModifiedDate',
				width : 120,
				align : 'center',
				renderer : function(val) {
					if (val != null && val != "") {
						return val.substring(0, 10) + " "
								+ val.substring(10, 10)
						// + val.substring(12, val.length)
					}
				}
			}, {
				header : '上传人',
				dataIndex : 'lastModifiedBy',
				align : 'center'
			}, {
				header : '查看模板',
				dataIndex : 'fileUrl',
				align : 'center',
				renderer : function(val) {
					// window.open(val);
					// var val = record.get("fileCode")
					// + record.get("fileType");
					if (val != "" && val != null) {
						return "<a style=\"cursor:hand;color:red\" onClick=\"window.open('"
								+ val + "');\"/>查看附件</a>"
					} else {
						return "";
					}
				}
			}]);

	var lastModifiedDate = new Ext.form.TextField({
		id : 'lastModifiedDate',
		fieldLabel : "上传时间",
		name : 'doc.lastModifiedDate',
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

	var fjdocFile = {
		id : "oriFile",
		xtype : "fileuploadfield",
		// inputType : "file",
		fieldLabel : '选择附件',
		// allowBlank : false,
		anchor : "95%",
		height : 22,
		name : 'fjdocFile',
		buttonCfg : {
			text : '浏览...',
			iconCls : 'upload-icon'
		}
	}
	// 查看
	var btnView = new Ext.Button({
		id : 'btnView',
		text : '查看',
		handler : function() {
			window.open(bview);
		}
	});
	btnView.setVisible(false);

	// 物料名称
	var fileName = new Ext.form.TextField({
		fieldLabel : '文件名称',
		readOnly : false,
		width : 108,
		id : 'fileName',
		name : "checkFile.fileName",
		anchor : '95%'
	});

	// 文件类型
	var fileType = new Ext.form.ComboBox({
		fieldLabel : '文件类型',
		id : "stateCob",
		store : [['1', '可行性报告']/*,['2', '工程交工竣工验收证书'], ['3', '工程项目竣工验收会签单']*/],
		displayField : "statusName",
		valueField : "statusCode",
		mode : 'local',
		anchor : '95%',
		// width : 40,
		triggerAction : 'all',
		hiddenName : 'checkFile.fileType',
		readOnly : true,
		value : '',
		listeners : {
			select : function(combo, record, index) {
				fileName.setValue(record.data.text);
			}
		}
	});

	var checkFileId = new Ext.form.TextField({
		id : 'checkFileId',
		name : 'checkFile.checkFileId',
		xtype : 'hidden',
		fieldLabel : '序号',
		readOnly : false,
		hidden : true,
		hideLabel : true,
		anchor : '95%'
	});

	var lastModifiedName = new Ext.form.TextField({
		id : 'lastModifiedName',
		xtype : 'textfield',
		fieldLabel : '上传人',
		readOnly : true,
		anchor : '95%'
	});
	var isModel = new Ext.form.TextField({
		id : 'isModel',
		name : 'checkFile.isModel',
		xtype : 'textfield',
		fieldLabel : '类型',
		readOnly : false,
		hidden : true,
		hideLabel : true,
		anchor : '95%'
	});

	var doccontent = new Ext.form.FieldSet({
		height : '100%',
		layout : 'form',
		items : [checkFileId, fjdocFile, fileName, fileType, lastModifiedDate,
				lastModifiedName, isModel]
	});
	
	
	function checkInput() {
		var msg = "";
		if (Ext.get('oriFile').dom.value == "") {
			msg = "'附件'";
		}
		if (fileType.getValue() == "") {
			if (msg != "") {
				msg = msg + ",'文件类型'";
			} else {
				msg = "'文件类型'!";
			}
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
				var url = '';
				if (docmethod == "add") {
					url = 'manageproject/addCheckFile.action';

				} else {
					url = 'manageproject/updateCheckFile.action';
				}
				if (!docform.getForm().isValid()) {
					return false;
				}
				docform.getForm().submit({
					url : url,
					method : 'post',
					params : {
						filePath : Ext.get('oriFile').dom.value
					},
					success : function(form, action) {
						var message = eval('(' + action.response.responseText
								+ ')');
						Ext.Msg.alert("成功", message.data);
						queryRecord();
						docwin.hide();
					},
					failure : function(form, action) {
						Ext.Msg.alert('错误', '出现未知错误.');
					}
				})
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
		el : 'win',
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
			text : "增加",
			iconCls : 'add',
			handler : function() {
				if (id == "") {
					Ext.Msg.alert('提示', '请先从列表中选择合同！');
					return false;
				}
				docwin.setTitle("增加验收书");
				docform.getForm().reset();
				docwin.show();
				btnView.setVisible(false);
				// Ext.get("fjdocFile").dom.select();
				// document.selection.clear();
				Ext.get('isModel').dom.value = "Y";
				Ext.get('lastModifiedName').dom.value = sessWorname;
				docmethod = 'add';
			}
		}, '-', {
			id : 'btnAnnexSave',
			text : "修改",
			iconCls : 'update',
			handler : updateRecord
		}, '-', {
			id : 'btnAnnexDelete',
			text : "删除",
			iconCls : 'delete',
			handler : function() {
				var selrows = annexGrid.getSelectionModel().getSelections();
				if (selrows.length > 0) {
					var ids = [];
					for (var i = 0; i < selrows.length; i += 1) {
						var member = selrows[i].data;
						if (member.checkFileId) {
							ids.push(member.checkFileId);
						} else {
							// store.remove(store.getAt(i));
						}
					}
					Ext.Msg.confirm('提示', '删除的数据您将不能恢复,确定要删除吗?', function(b) {
						if (b == "yes") {
							var record = annexGrid.getSelectionModel()
									.getSelected();
							Ext.Ajax.request({
								url : 'manageproject/deleteCheckFile.action',
								params : {
									checkFileId : ids
								},
								method : 'post',
								waitMsg : '正在删除数据...',
								success : function(result, request) {
									Ext.MessageBox.alert('提示', '删除成功!');
									queryRecord();
								},
								failure : function(result, request) {
									Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
								}
							});
						}
					});

				} else {
					Ext.Msg.alert('提示', '请选择您要删除的合同附件！');
				}
			}
		}]
	});

	function queryRecord() {
		annex_ds.load({
			params : {
				fuzzytext : fuzzy.getValue(),
				flag : 'Y'
			}
		});
	}

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
						+ " " + record.data.lastModifiedDate.substring(10, 10))
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
			docwin.setTitle("修改验收书");
		} else {
			Ext.Msg.alert('提示', '请选择您要修改的合同附件！');
		}
	}

	var annexGrid = new Ext.grid.GridPanel({
		ds : annex_ds,
		cm : annex_item_cm,
		sm : annex_sm,
		// title : '合同附件',
		width : Ext.get('div_lay').getWidth(),
		split : true,
		// autoHeight:true,
		height : 800,
		autoScroll : true,
		// collapsible : true,
		tbar : annextbar,
		border : false
	});

	annexGrid.on('rowdblclick', updateRecord);

	var layout = new Ext.Panel({
		autoWidth : true,
		autoHeight : true,
		border : false,
		autoScroll : true,
		split : true,
		items : [annexGrid]
	});
	queryRecord();
	getWorkCode();
	layout.render(Ext.getBody());

	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result.workerCode) {
					// 设定默认工号
					sessWorcode = result.workerCode;
					sessWorname = result.workerName;
					sessDeptCode = result.deptCode;
					// deptId = result.deptId;
					sessDeptName = result.deptName;
					// loadData();
				}
				// typeBox.setValue(2);
			}
		});
	}
});