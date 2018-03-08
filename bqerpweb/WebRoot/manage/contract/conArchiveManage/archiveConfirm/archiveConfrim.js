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
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var sessWorname;
	var modifyId;
	function ChangeDateToString(DateIn) {
		// 初始化时间
		var Year = DateIn.getYear();
		var Month = DateIn.getMonth() + 1;
		var Day = DateIn.getDate();
		var CurrentDate = Year + "-";
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
	var fromDate = new Ext.form.DateField({
				format : 'Y-m-d',
				name : 'startDate',
				id : 'fromDate',
				itemCls : 'sex-left',
				clearCls : 'allow-float',
				checked : true,
				fieldLabel : "登记时间",
				allowBlank : false,
				readOnly : true,
				value : sdate,
				emptyText : '请选择',
				anchor : '100%'
			});
	var toDate = new Ext.form.DateField({
				format : 'Y-m-d',
				name : 'endDate',
				value : edate,
				id : 'toDate',
				itemCls : 'sex-left',
				clearCls : 'allow-float',
				fieldLabel : "至",
				allowBlank : false,
				readOnly : true,
				emptyText : '请选择',
				anchor : '80%'
			});
			
	var conttreesNo = new Ext.form.TextField({
				id : 'conttreesNo',
				xtype : "textfield",
				fieldLabel : "合同编号",
				name : 'conttreesNo',
				anchor : '100%'
			});
	var contractName = new Ext.form.TextField({
				id : 'contractName',
				xtype : "textfield",
				fieldLabel : "合同名称",
				name : 'contractName',
				anchor : '80%'
			});
	// 供应商
	var cliendId = new Ext.form.ComboBox({
		name : 'cliendId',
		id : 'cliendId',
		fieldLabel : '供应商',
		mode : 'remote',
		editable : false,
		anchor : '88.5%',
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../manage/client/business/clientSelect/clientSelect.jsp?approveFlag=2";
			var client = window
					.showModalDialog(
							url,
							null,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(client) != "undefined") {
				cliendId.setValue(client.clientName);
				hidcliendId.setValue(client.cliendId);
			}
		}
	});
	var hidcliendId = new Ext.form.Hidden({
				id : "hidcliendBox",
				name : "con.cliendId"
			})
			
	var btnQuery = new Ext.Button({
				id : 'btnQuery',
				text : '查  询',
				minWidth : 70,
				handler : function() {
					var sd = Ext.get("fromDate").dom.value;
					var ed = Ext.get("toDate").dom.value;
					if (sd > ed) {
						Ext.Msg.alert('提示', '选择后一日期应比前一日期大！');
						return false;
					}
					con_ds.load({
								params : {
									startdate : sd ,
									enddate : ed ,
									conNo : Ext.get('conttreesNo').dom.value,
									conName : Ext.get('contractName').dom.value,
									clientName : hidcliendId.getValue(),
									conTypeId : 1,
									start : 0,
									limit : 18
								}
							});
				}
			});
	var btnClear = new Ext.Button({
				id : 'btnClear',
				text : '清除条件',
				minWidth : 70,
				handler : function() {
					form.getForm().reset();
					hidcliendId.setValue();
				}
			});
	var btnInfo = new Ext.Button({
		id : 'btnInfo',
		text : '合同信息',
		minWidth : 70,
		handler : function() {
			var selrows = Grid.getSelectionModel().getSelections();
			if (selrows.length > 0) {
				var record = Grid.getSelectionModel().getSelected();
				var url;
				var conId = record.data.conId;
				var conCode = record.data.conttreesNo;
				if(conCode.length > 19){
					modifyId = record.data.conTypeId;
				}
				if(conCode.length > 19){
				url = "../../../../manage/contract/business/conBaseInfo/conModifyInfo.jsp?id="
										+ modifyId;
				} else{
				url = "../../../../manage/contract/business/conBaseInfo/conBaseInfo.jsp?id="
						+ conId;
						}
				var o = window.showModalDialog(url, '',
						'dialogWidth=800px;dialogHeight=600px;status=no');
			} else {
				Ext.Msg.alert('提示', '请从列表中选择一条记录!');
			}
		}
	});
	var btnArchive = new Ext.Button({
		id : 'btnArchive',
		text : '确认归档',
		minWidth : 70,
		handler : function() {
			var sel = Grid.getSelectionModel().getSelections();
			if (sel.length > 0) {
				var record = Grid.getSelectionModel().getSelected();
				var conCode = record.data.conttreesNo;
				if(conCode.length >19){
				modifyId = record.data.conTypeId;
				}
				if (conCode.length >19 && modifyId != null && modifyId != "") {
					Ext.Ajax.request({
						url : 'managecontract/getConModel.action',
						params : {
							conId : record.data.conId
						},
						method : 'post',
						waitMsg : '正在加载数据...',
						success : function(result, request) {
							var o = eval('(' + result.responseText + ')');
							if (o.data.fileStatus == 'OK') {
								if (o.data.fileNo != null) {
									fileNo.setValue(o.data.fileNo);
									fileNo.on('beforequery', function() {
												return false
											});
								}
								confirm_win.show();
								confirm_form.getForm().reset();
								Ext.get('conId').dom.value = record.data.conId;
								Ext.get('conNo').dom.value = record.data.conttreesNo;
								Ext.get('conName').dom.value = record.data.contractName;
								Ext.get('fileBy').dom.value = sessWorname;
								
							} else {
								Ext.MessageBox.alert('提示', '原合同归档后变更合同才可以归档！');
								return false;
							}
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
						}
					});
				} else {
					confirm_win.show();
					confirm_form.getForm().reset();
					Ext.get('conId').dom.value = record.data.conId;
					Ext.get('conNo').dom.value = record.data.conttreesNo;
					Ext.get('conName').dom.value = record.data.contractName;
					Ext.get('fileBy').dom.value = sessWorname;
				}

			} else {
				Ext.Msg.alert('提示', '请从列表中选择一条记录');
			}
		}
	});
	var btnBack = new Ext.Button({
				id : 'btnBack',
				text : '退  回',
				minWidth : 70,
				handler : function() {
					var sel = Grid.getSelectionModel().getSelections();
					if (sel.length > 0) {
						var record = Grid.getSelectionModel().getSelected();
						var conCode = record.data.conttreesNo;
						back_win.show();
						back_form.getForm().reset();
						if(conCode.length >19){
						modifyId = record.data.conTypeId;
						}
						if (modifyId != null && modifyId != "") {
							Ext.get('keyId').dom.value = record.data.conTypeId;
						} else {
							Ext.get('keyId').dom.value = record.data.conId;
						}
						Ext.get('cNo').dom.value = record.data.conttreesNo;
						Ext.get('gdWorker').dom.value = sessWorname;

					} else {
						Ext.Msg.alert('提示', '请从列表中选择一条记录');
					}
				}
			});

	var btnSign = new Ext.Button({
				id : 'btnSign',
				text : '会签表 ',
				minWidth : 70,
				handler : function() {
					CheckRptPreview()
				}
			});
	// 会签票面浏览
	function CheckRptPreview() {
		var selected = Grid.getSelectionModel().getSelections();
		var conId;
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要浏览的记录！");
		} else {
			var menber = selected[0];
			conId = menber.get('conId');
			var url = "/powerrpt/report/webfile/CGConMeetSign.jsp?conId="
					+ conId;
			window.open(url);
		}
	};

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
										columnWidth : 0.3,
										layout : 'form',
										items : [fromDate]
									}, {
										border : false,
										columnWidth : 0.4,
										layout : 'form',
										items : [toDate]
									}, {
										border : false,
										columnWidth : 0.15,
										align : 'center',
										layout : 'form',
										items : [btnQuery]
									}, {
										border : false,
										columnWidth : 0.15,
										align : 'center',
										layout : 'form',
										items : [btnClear]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										border : false,
										columnWidth : 0.3,
										layout : 'form',
										items : [conttreesNo]
									}, {
										border : false,
										columnWidth : 0.4,
										layout : 'form',
										items : [contractName]
									}, {
										border : false,
										columnWidth : 0.15,
										layout : 'form',
										items : [btnInfo]
									}, {
										border : false,
										columnWidth : 0.15,
										layout : 'form',
										items : [btnBack]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										border : false,
										columnWidth : 0.7,
										layout : 'form',
										items : [cliendId]
									}, {
										border : false,
										columnWidth : 0.15,
										layout : 'form',
										items : [btnArchive]
									}, {
										border : false,
										columnWidth : 0.15,
										layout : 'form',
										items : [btnSign]
									}]
						}]
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
	/*-----------------------------------------------------------------*/
	var con_item = Ext.data.Record.create([{
				name : 'conId'
			}, {
				name : 'conttreesNo'
			}, {
				name : 'contractName'
			}, {
				name : 'clientName'
			}, {
				name : 'isSum'
			}, {
				name : 'actAmount'
			}, {
				name : 'currencyType'
			}, {
				name : 'operateName'
			}, {
				name : 'operateDeptName'
			}, {
				name : 'signStartDate'
			}, {
				name : 'signEndDate'
			}, {
				name : 'conModifyId'
			}, {
				name : 'conTypeId'
			}, {
				name : 'conTypeName'
			}, {
				name : 'pageCount'
			}, {
				name : 'fileCount'
			}, {
				name : 'currencyName'
			}]);
	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	var con_item_cm = new Ext.grid.ColumnModel([con_sm,
			new Ext.grid.RowNumberer({
						header : '序号',
						width : 50,
						align : 'center'
					}), {
				header : '合同编号',
				dataIndex : 'conttreesNo',
				align : 'center',
				width : 140
			}, {
				header : '合同名称',
				dataIndex : 'contractName',
				align : 'center',
				width :200,
				renderer : function change(val) {
				return ' <span style="white-space:normal;">' + val + ' </span>';
		}
			}, {
				header : '供应商',
				dataIndex : 'clientName',
				align : 'center'
			}, {
				header : '有无总金额',
				dataIndex : 'isSum',
				align : 'center'
			}, {
				header : '总金额',
				dataIndex : 'actAmount',
				align : 'center'
			}, {
				header : '币别',
				dataIndex : 'currencyName',
				align : 'center'
			}, {
				header : '合同类别',
				dataIndex : 'conTypeId',
				align : 'center',
				hidden: true,
				renderer: function(v){
				if(v==1){
				return "采购合同";
				}
				}
			}, {
				header : '经办人',
				dataIndex : 'operateName',
				align : 'center'
			}, {
				header : '经办部门',
				dataIndex : 'operateDeptName',
				align : 'center'
			}, {
				header : '页号',
				dataIndex : 'pageCount',
				hidden : true,
				align : 'center'
			}, {
				header : '份数',
				dataIndex : 'fileCount',
					hidden : true,
				align : 'center'
			}]);
	con_item_cm.defaultSortable = true;
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'managecontract/getArchiveConfirmList.action'
						}),
				reader : new Ext.data.JsonReader({
				root : 'root',
				totalProperty : 'total'
				}, con_item)
			});
	con_ds.load({
				params : {
					startdate : sdate,
					enddate : edate,
					conTypeId : 1,
					start : 0,
					limit : 18
				}
			});

			var gridbbar = new Ext.PagingToolbar({
				pageSize : 18,
				store : con_ds,
				width : Ext.get('div_lay').getWidth(),
				displayInfo : true,
				displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
				emptyMsg : "没有记录",
				beforePageText : '页',
				afterPageText : "共{0}"
			});
	var Grid = new Ext.grid.GridPanel({
				ds : con_ds,
				cm : con_item_cm,
				sm : con_sm,
				title : '合同归档列表',
				width : Ext.get('div_lay').getWidth(),
				split : true,
				autoScroll : true,
				// tbar : contbar,
				bbar : gridbbar,
				border : false,
				viewConfig : {
					forceFit : false
				}
			});
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				items : [{
							region : "north",
							layout : 'fit',
							height : 125,
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
							items : [Grid]
						}]
			});
	/*-----------------------------归档确认----------------------------------------*/
	var conNo = new Ext.form.TextField({
				id : 'conNo',
				xtype : "textfield",
				fieldLabel : "合同编号",
				name : 'con.conttreesNo',
				readOnly : true,
				anchor : '95%'
			});
	var conName = new Ext.form.TextField({
				id : 'conName',
				xtype : "textfield",
				fieldLabel : "合同名称",
				readOnly : true,
				name : 'con.contractName',
				anchor : '95%'
			});
	// 档号
	var url = "managecontract/undertakeNolist.action?conTypeId="+1;
	var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", url, false);
	conn.send(null);
	var search_data4 = eval('(' + conn.responseText + ')');
	var fileNo = new Ext.form.ComboBox({
				fieldLabel : '档号',
				store : new Ext.data.SimpleStore({
							fields : ['undertakeNo', 'undertakeNo'],
							data : search_data4
						}),
				id : 'fileNo',
				name : 'fileNoxx',
				valueField : "undertakeNo",
				displayField : "undertakeNo",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'con.fileNo',
				editable : true,
				triggerAction : 'all',
				selectOnFocus : true,
				readOnly : true,
				allowBlank : false,
				emptyText : '请选择',
				anchor : '95%'
			});
	var pageCount = {
		id : "pageCount",
		xtype : "textfield",
		fieldLabel : '合同页号',
		allowBlank : false,
		name : 'con.pageCount',
		anchor : '90%'
	}

	var fileCount = new Ext.form.NumberField({
				id : "fileCount",
				xtype : "numberfield",
				fieldLabel : '合同份数',
				// regex:/^\d$/,
				// regexText:'只能输入整数',
				vtype : 'alphanum',
				vtypeText : '只能输入整数',
				allowBlank : false,
				name : 'con.fileCount',
				anchor : '90%'
			});

	var fileBy = new Ext.form.TextField({
				id : 'fileBy',
				xtype : "textfield",
				fieldLabel : "归档人",
				readOnly : true,
				name : 'fileBy',
				anchor : '90%'
			});
	var fileDate = new Ext.form.TextField({
				id : 'fileDate',
				fieldLabel : "归档日期",
				name : 'fileDate',
				xtype : 'textfield',
				style : 'cursor:pointer',
				readOnly : true,
				value : getDate(),
				anchor : '90%'
			});
	var fileMemo = {
		id : "fileMemo",
		xtype : "textarea",
		fieldLabel : '备注',
		name : 'con.fileMemo',
		anchor : '95%'

	};
	var contractId = {
		id : "conId",
		xtype : "hidden",
		// xtype : 'textfield',
		readOnly : true,
		name : 'con.conId'

	};
	var confirm = new Ext.form.FieldSet({
				title : '确认归档',
				height : '100%',
				layout : 'form',
				items : [{
							layout : 'column',
							border : false,
							items : [{
										border : false,
										columnWidth : 1,
										layout : 'form',
										items : [conNo]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										border : false,
										columnWidth : 1,
										layout : 'form',
										items : [conName]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										border : false,
										columnWidth : 1,
										layout : 'form',
										items : [fileNo]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										border : false,
										columnWidth : 0.5,
										layout : 'form',
										items : [pageCount]
									}, {
										border : false,
										columnWidth : 0.5,
										layout : 'form',
										items : [fileCount]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										border : false,
										columnWidth : 0.5,
										layout : 'form',
										items : [fileBy]
									}, {
										border : false,
										columnWidth : 0.5,
										layout : 'form',
										items : [fileDate]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										border : false,
										columnWidth : 1,
										layout : 'form',
										items : [fileMemo]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										border : false,
										columnWidth : .5,
										layout : 'form',
										items : [contractId]
									}]
						}]
			});
	var confirm_form = new Ext.form.FormPanel({
				bodyStyle : "padding:5px 5px 0",
				labelAlign : 'right',
				id : 'shift-form',
				labelWidth : 80,
				autoHeight : true,
				region : 'center',
				border : false,
				items : [confirm],
				buttons : [{
					text : '归档',
					iconCls : 'save',
					handler : function() {
						var url = 'managecontract/conArchiveConfirm.action';
						if (!confirm_form.getForm().isValid()) {
							return false;
						}
						confirm_form.getForm().submit({
							url : url,
							method : 'post',
							params : {
								modifyId : modifyId
							},
							success : function(form, action) {
								var message = eval('('
										+ action.response.responseText + ')');
								Ext.Msg.alert("成功", message.data);
								con_ds.reload();
								confirm_win.hide();
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
						confirm_form.getForm().reset();
						confirm_win.hide();
					}
				}]
			});

	var confirm_win = new Ext.Window({
				modal : true,
				autoHeight : true,
				width : 550,
				closeAction : 'hide',
				items : [confirm_form]
			});

	/*------------------------------归档退回---------------------------------------*/
	var cNo = new Ext.form.TextField({
				id : 'cNo',
				xtype : "textfield",
				fieldLabel : "合同编号",
				readOnly : true,
				name : 'file.thconttreesNo',
				anchor : '96%'
			});
	var fBy = new Ext.form.TextField({
				id : 'gdWorker',
				xtype : "textfield",
				fieldLabel : "归档人",
				readOnly : true,
				name : 'file.gdWorker',
				anchor : '90%'
			});
	var fDate = new Ext.form.TextField({
				id : 'withdrawalTime',
				fieldLabel : "归档日期",
				name : 'file.withdrawalTime',
				type : 'textfield',
				readOnly : true,
				value : getDate(),
				anchor : '95%'
			});
	var fopinion = {
		id : "opinion",
		xtype : "textarea",
		fieldLabel : '意见内容',
		name : 'file.opinion',
		height : 80,
		anchor : '96%'

	};
	var conId = {
		id : "keyId",
		xtype : "hidden",
		readOnly : true,
		name : 'file.keyId'

	};
	var back = new Ext.form.FieldSet({
				title : '归档退回',
				height : '100%',
				layout : 'form',
				items : [{
							layout : 'column',
							border : false,
							items : [{
										border : false,
										columnWidth : 1,
										layout : 'form',
										items : [cNo]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										border : false,
										columnWidth : 1,
										layout : 'form',
										items : [fopinion]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										border : false,
										columnWidth : 1,
										layout : 'form',
										items : [conId]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										border : false,
										columnWidth : 0.5,
										layout : 'form',
										items : [fBy]
									}, {
										border : false,
										columnWidth : 0.5,
										layout : 'form',
										items : [fDate]
									}]
						}]
			});
	var back_form = new Ext.form.FormPanel({
				bodyStyle : "padding:5px 5px 0",
				labelAlign : 'right',
				id : 'shift-form',
				labelWidth : 80,
				autoHeight : true,
				region : 'center',
				border : false,
				items : [back],
				buttons : [{
					text : '退回',
					iconCls : 'save',
					handler : function() {
						var url = 'managecontract/conArchiveBack.action';
						if (!back_form.getForm().isValid()) {
							return false;
						}
						back_form.getForm().submit({
							url : url,
							method : 'post',
							params : {
								modifyId : modifyId
							},
							success : function(form, action) {
								var message = eval('('
										+ action.response.responseText + ')');
								Ext.Msg.alert("成功", message.data);
								con_ds.reload();
								back_win.hide();
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
						back_form.getForm().reset();
						back_win.hide();
					}
				}]
			});

	var back_win = new Ext.Window({
				modal : true,
				autoHeight : true,
				width : 500,
				closeAction : 'hide',
				items : [back_form]
			});
	Ext.Ajax.request({
				url : 'managecontract/getSessionInfo.action',
				params : {},
				method : 'post',
				waitMsg : '正在加载数据...',
				success : function(result, request) {
					var responseArray = Ext.util.JSON
							.decode(result.responseText);
					if (responseArray.success == true) {
						var tt = eval('(' + result.responseText + ')');
						o = tt.data;
						sessWorname = o[1];
					} else {
						Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
					}
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
				}
			});

})