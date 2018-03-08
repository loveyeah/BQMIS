Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	var flagBudgetItemId = null;
	var flagCenterId = null;
	var flagBudgetTime = null;
	var flagItemId = null;
	var flagFillBy = null;
	var flagCenterCode = null;
	var flagCenterName = null;
	var flagItemCode = null;
	var flagItemName = null;
	var flagFillName = null;
	var deptCode = null;
	var deptName = null;
	// 判断当前日期是否存在数据
	var isHave = null;
	var bmIdArray = new Array();

	var flagDateType = 2;
	var obj;

	function numberFormat(value) {
		value = String(value);
		if (value == null || value == "null") {
			value = "0";
		}
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
		if (whole == null || whole == "null" || whole == "") {
			v = "0.00";
		}
		return v;
	}
	function getDate() {
		var d, s, t, day;
		d = new Date();
		s = d.getFullYear().toString() + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		s += '-';
		day = d.getDate()
		s += (day > 9 ? "" : "0") + day;
		return s;
	}
	function getCurrentMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString() + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		return s;
	}

	// 登录人部门
	// var dept = new Ext.form.Hidden({
	// id : 'dept',
	// name : 'dept'
	// })
	// 预算部门
	// var budgetDept = new Ext.form.TextField({
	// fieldLabel : '预算部门',
	// readOnly : true,
	// width : 90,
	// value : deptName
	// })

	// 预算部门 add by ltong -------------
	var deptH = new Ext.form.Hidden({
				id : 'deptH',
				name : 'deptH'
			})
	var budgetDept = new Ext.form.TextField({
		id : 'budgetDept',
		fieldLabel : '部门',
		readOnly : true,
		width : 100,
		listeners : {
			focus : function() {
				var args = {
					selectModel : 'single',
					rootNode : {
						id : "0",
						text : '合肥电厂'
					}
				}
				var url = "../../maint/budegtDeptSelect.jsp";
				var dept = window
						.showModalDialog(
								url,
								args,
								'dialogWidth:500px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;')
				if (typeof(dept) != "undefined") {
					deptH.setValue(dept.depCode);
					budgetDept.setValue(dept.depName);
				}
				this.blur();
			}
		},
		allowBlank : false
	})
	// 从session取登录人编码姓名部门相关信息
	function getWorkerCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号，赋给全局变量
							flagFillBy = result.workerCode;
							flagFillName = result.workerName;
							deptCode = result.deptCode;
							deptName = result.deptName;
							budgetDept.setValue(deptName);
							deptH.setValue(deptCode)
							queryWestRec();
						}
					}
				});
	}
	getWorkerCode();
	// *****************以下未处理******************************
	var westsm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	// 左边列表中的数据
	var westdatalist = new Ext.data.Record.create([{
				name : 'budgetItemId'
			}, {
				name : 'itemAlias'
			}, {
				name : 'centerItemId'
			}, {
				name : 'centerId'
			}, {
				name : 'topicId'
			}, {
				name : 'budgetTime'
			}, {
				name : 'itemId'
			}, {
				name : 'budgetMakeId'
			}
			// , {
			// name : 'forecastBudget'
			// }, {
			// name : 'adviceBudget'
			// }, {
			// name : 'adjustBudget'
			// }, {
			// name : 'budgetAdd'
			// }, {
			// name : 'budgetChange'
			// }, {
			// name : 'judgeBudget'
			// }
			, {
				name : 'ensureBudget'
			}, {
				name : 'factHappen'
			}, {
				name : 'financeHappen'
			}
			// , {
			// name : 'budgetBasis'
			// }, {
			// name : 'budgetStatus'
			// }
			, {
				name : 'centerCode'
			}, {
				name : 'centerName'
			}, {
				name : 'itemCode'
			}, {
				name : 'itemName'
			}
			// 判断当前日期是否存在数据
			, {
				name : 'changeId'
			},{
			    name:'dispalyNo'  //指标编码
			}]);

	var westgrids = new Ext.data.JsonStore({
				url : 'managebudget/getDeptBudetDetails.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : westdatalist
			});

	// 预算时间
	var monthTime = new Ext.form.TextField({
				fieldLabel : '预算时间',
				readOnly : true,
				width : 80,
				style : 'cursor:pointer',
				value : getCurrentMonth(),
				listeners : {
					focus : function() {
						WdatePicker({
									// 时间格式
									dateFmt : 'yyyy-MM',
									alwaysUseStartDate : false
								});
						this.blur();
					}
				}

			});
	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
				width : 200,
				// height : 510,
				autoScroll : true,
				ds : westgrids,
				tbar : ['预算部门：', budgetDept, '-', '预算时间：', monthTime, {
							id : 'btnAdd',
							iconCls : 'query',
							text : "查询",
							handler : queryWestRec
						}],
				columns : [westsm, new Ext.grid.RowNumberer({
									header : "行号",
									width : 31
								}), {
							header : "预算指标",
							width : 110,
							align : "left",
							sortable : true,
							dataIndex : 'itemAlias',
			renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
				var level = 0;
				if (record.get("dispalyNo") != null
						|| record.get("dispalyNo") != "") {
					level = (record.get("dispalyNo").length / 3) - 2;
				}
				if (level > 0) {
					var levelNo = "";
					for (var i = 0; i < level; i++) {
						levelNo = " " + levelNo;
					}

					value = levelNo + value;
				}
				return "<pre>" + value + "</pre>";
			}
						}, {
							header : "预算值（万）",
							width : 90,
							align : "center",
							sortable : true,
							dataIndex : 'ensureBudget'
						}, {
							header : "发生值（万）",
							width : 90,
							align : "center",
							sortable : true,
							dataIndex : 'factHappen'
						}],
				sm : westsm,
				frame : true,
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : westgrids,
							displayInfo : true,
							displayMsg : "{0} 到 {1} /共 {2} 条",
							emptyMsg : "没有记录"
						}),
				border : true
			});

	westsm.on('rowselect', function() {
				if (westsm.hasSelection()) {
					flagBudgetItemId = westsm.getSelected().get('budgetItemId');
					flagCenterId = westsm.getSelected().get('centerId');
					flagBudgetTime = westsm.getSelected().get('budgetTime');
					flagItemId = westsm.getSelected().get('itemId');
					flagCenterCode = westsm.getSelected().get('centerCode');
					flagCenterName = westsm.getSelected().get('centerName');
					flagItemCode = westsm.getSelected().get('itemCode');
					flagItemName = westsm.getSelected().get('itemName');
					isHave = westsm.getSelected().get('changeId');
					for (var i = 0; i < westgrids.getCount(); i++) {
						bmIdArray.push(westgrids.getAt(i).get('budgetMakeId'));
					}
					undulpicate(bmIdArray);
					queryRec();
				}
			});
	// 去除数组中重复元素 ltong
	function undulpicate(array) {
		for (var i = 0; i < array.length; i++) {
			for (var j = i + 1; j < array.length; j++) {
				// 注意 ===
				if (array[i] === array[j]) {
					array.splice(j, 1);
					j--;
				}
			}
		}
		return array;
	}
	function queryWestRec() {
		westgrids.baseParams = {
			// deptCode : deptCode,
			deptCode : deptH.getValue(),
			dataType : flagDateType,
			budgetTime : monthTime.getValue()
		};
		westgrids.load({
					params : {
						start : 0,
						limit : 18
					}
				});
	}
	function queryRec() {
		eastgrids.load({
					params : {
						// deptCode : deptCode,
						deptCode : deptH.getValue(),
						dataType : flagDateType,
						budgetTime : monthTime.getValue(),
						// start : 0,
						// limit : 18,
						budgetItemId : flagBudgetItemId
					}
				})
	}
	// ******************以上未处理*************************************
	// ******************以下为右边部分***********************************
	var eastsm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	// 右边列表中的数据
	var datalist = new Ext.data.Record.create([{
				name : 'cjm.happenId'
			}, {
				name : 'cjm.budgetItemId'
			}, {
				name : 'cjm.centerId'
			}, {
				name : 'cjm.budgetTime'
			}, {
				name : 'cjm.itemId'
			}, {
				name : 'cjm.happenSerial'
			}, {
				name : 'cjm.happenValue'
			}, {
				name : 'cjm.happenExplain'
			}, {
				name : 'cjm.fillBy'
			}, {
				name : 'cjm.fillTime'
			}, {
				name : 'cjm.workFlowNo'
			}, {
				name : 'cjm.happenStatus'
			}, {
				name : 'cjm.isUse'
			}, {
				name : 'centerCode'
			}, {
				name : 'centerName'
			}, {
				name : 'itemCode'
			}, {
				name : 'itemName'
			}, {
				name : 'fillName'
			}, {
				name : 'fillTime'
			}]);

	var eastgrids = new Ext.data.JsonStore({
				url : 'managebudget/getControlFeeList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});

	var contbar = new Ext.Toolbar({
				items : [{
							id : 'btnAdd',
							iconCls : 'add',
							text : "新增",
							handler : addRec
						}, '-', {
							id : 'btnDelete',
							iconCls : 'delete',
							text : "删除",
							handler : deleteRec

						}, '-', {
							id : 'btnCancer',
							iconCls : 'cancer',
							text : "取消",
							handler : cancerRec

						}, '-', {
							id : 'btnSave',
							iconCls : 'save',
							text : "保存修改",
							handler : saveRec
						}, '-', {
							id : 'btnReport',
							iconCls : 'upcommit',
							text : "上报",
							handler : reportRec
						}]
			});

	// 右边列表
	var eastgrid = new Ext.grid.EditorGridPanel({
				tbar : contbar,
				autoScroll : true,
				ds : eastgrids,
				layout : 'fit',
				columns : [eastsm, new Ext.grid.RowNumberer({
									header : "行号",
									width : 31
								}), {
							header : "执行控制ID",
							hidden : true,
							dataIndex : 'cjm.happenId'
						}, {
							header : "部门预算明细id",
							hidden : true,
							dataIndex : 'cjm.budgetItemId'
						}, {
							header : "预算部门id",
							hidden : true,
							dataIndex : 'cjm.centerId'
						}, {
							header : "预算部门编码",
							width : 70,
							align : "left",
							sortable : true,
							hidden : true,
							dataIndex : 'centerCode'
						}, {
							header : "预算部门",
							align : "left",
							sortable : true,
							dataIndex : 'centerName'
						}, {
							header : "指标id",
							sortable : false,
							hidden : true,
							dataIndex : 'cjm.itemId'
						}, {
							header : "预算指标编码",
							width : 70,
							align : "left",
							sortable : true,
							hidden : true,
							dataIndex : 'itemCode'
						}, {
							header : "预算指标",
							align : "left",
							sortable : true,
							dataIndex : 'itemName'
						}, {
							header : "预算时间",
							align : "center",
							width : 80,
							sortable : true,
							dataIndex : 'cjm.budgetTime'
						}, {
							header : "状态",
							align : "center",
							width : 80,
							sortable : true,
							dataIndex : 'cjm.happenStatus',
							renderer : function(v) {
								if (v == 0) {
									return '未上报';
								} else if (v == 1) {
									return '审批中';
								} else if (v == 2) {
									return '审批通过';
								} else if (v == 3) {
									return '审批退回';
								}
							}
						}, {
							header : "序列号",
							align : "center",
							width : 80,
							hidden : true,
							sortable : true,
							dataIndex : 'cjm.happenSerial'
						}, {
							header : "报销值（元）",
							align : "center",
							width : 120,
							sortable : true,
							dataIndex : 'cjm.happenValue',
							css : CSS_GRID_INPUT_COL,
							editor : new Ext.form.NumberField({
										allowBlank : false,
										allowDecimal : true,
										allowNegative : false,
										decimalPrecision : 2
									}),
							renderer : function(value) {
								return numberFormat(value);
							}
						}, {
							header : "填写人编码",
							hidden : true,
							sortable : true,
							dataIndex : 'cjm.fillBy'
						}, {
							header : "填写人",
							align : "center",
							width : 80,
							sortable : true,
							dataIndex : 'fillName'
						}, {
							header : "填写时间",
							align : "center",
							width : 80,
							sortable : true,
							dataIndex : 'fillTime'
						}, {
							header : "报销说明",
							width : 110,
							align : "left",
							sortable : false,
							dataIndex : 'cjm.happenExplain',
							css : CSS_GRID_INPUT_COL,
							editor : new Ext.form.TextArea({
										style : 'cursor:pointer'
									})
						}],
				forceFit : true,
				sm : eastsm,
				frame : true,
				// bbar : new Ext.PagingToolbar({
				// pageSize : 18,
				// store : eastgrids,
				// displayInfo : true,
				// displayMsg : "{0} 到 {1} /共 {2} 条",
				// emptyMsg : "没有记录"
				// }),
				border : true,
				enableColumnHide : false,
				iconCls : 'icon-grid',
				clicksToEdit : 1,
				autoSizeColumns : true
			});

	eastgrid.on('beforeedit', function(obj) {
				if (obj.record.get('cjm.happenStatus') == 1
						|| obj.record.get('cjm.happenStatus') == 2)
					return false;
				else
					return true;
			})

	// 增加
	function addRec() {

		if (!westsm.hasSelection()) {
			Ext.Msg.alert('提示信息', '请先选择预算指标！');
			return;
		}
		var count = eastgrids.getCount();
		var currentIndex = count;
		var o = new datalist({
					'cjm.budgetItemId' : flagBudgetItemId,
					'cjm.centerId' : flagCenterId,
					'cjm.budgetTime' : flagBudgetTime,
					'cjm.itemId' : flagItemId,
					'cjm.happenSerial' : null,
					'cjm.happenValue' : 0,
					'cjm.happenExplain' : '',
					'cjm.fillBy' : flagFillBy,
					'cjm.fillTime' : getDate(),
					'centerCode' : flagCenterCode,
					'centerName' : flagCenterName,
					'itemCode' : flagItemCode,
					'itemName' : flagItemName,
					'fillName' : flagFillName,
					'fillTime' : getDate()
				});
		eastgrid.stopEditing();
		eastgrids.insert(currentIndex, o);
		eastsm.selectRow(currentIndex);
		eastgrid.startEditing(currentIndex, 1);

	}

	// 删除记录
	var ids = new Array();
	function deleteRec() {
		eastgrid.stopEditing();
		var sm = eastgrid.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				if (selected[i].get('cjm.happenStatus') == 1
						|| selected[i].get('cjm.happenStatus') == 2) {
					Ext.Msg.alert('提示信息', '只有未上报或退回的数据可以删除！')
					return;
				}
			}
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("cjm.happenId") != null) {
					ids.push(member.get("cjm.happenId"));
				}
				eastgrid.getStore().remove(member);
				eastgrid.getStore().getModifiedRecords().remove(member);
			}
		}
	}
	// 保存
	function saveRec() {
		eastgrid.stopEditing();
		var modifyRec = eastgrid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			Ext.Msg.confirm('提示信息', '确定要保存修改吗？', function(button) {
				if (button == 'yes') {
					for (var i = 0; i < modifyRec.length; i += 1) {
						if (modifyRec[i].get('cjm.happenStatus') == 1
								|| modifyRec[i].get('cjm.happenStatus') == 2) {
							Ext.Msg.alert('提示信息', '只有未上报或退回的数据可以保存修改！')
							return;
						}
					}
					var addData = new Array();
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						if (modifyRec[i].get('cjm.isUse') == 'Y') {
							updateData.push(modifyRec[i].data);
						} else {
							addData.push(modifyRec[i].data);
						}
					}
					Ext.Ajax.request({
								url : 'managebudget/saveControlFeeModified.action',
								method : 'post',
								params : {
									isAdd : Ext.util.JSON.encode(addData),
									isUpdate : Ext.util.JSON.encode(updateData),
									isDelete : ids.join(","),
									isFeeRegister : 'Y',
									isHave : isHave,
									bmIdArray : Ext.util.JSON.encode(bmIdArray)
								},
								success : function(result, request) {
									Ext.Msg.alert('提示信息', '数据保存修改成功！')
									eastgrids.rejectChanges();
									ids = [];
									bmIdArray = [];
									westgrids.reload();
									eastgrids.reload();
								},
								failure : function(result, request) {
									Ext.Msg.alert("提示信息", "数据保存修改失败！");
									eastgrids.rejectChanges();
									ids = [];
									bmIdArray = [];
									eastgrids.reload();
								}
							})
				}
			})

		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}
	// 取消
	function cancerRec() {
		var modifyRec = eastgrids.getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			Ext.Msg.confirm('提示信息', '确定要放弃修改吗？', function(button) {
						if (button == 'yes') {
							eastgrids.reload();
							eastgrids.rejectChanges();
							ids = [];
						}
					})
		} else {
			eastgrids.reload();
		}
	}

	function reportRec() {
		eastgrid.stopEditing();
		var sm = eastgrid.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示信息", "请选择要上报的记录！");
		} else {
			if (selected.length != 1) {
				Ext.Msg.alert('提示信息', '请选择其中一项！')
				return;
			}
			for (var i = 0; i < selected.length; i += 1) {
				if (selected[i].get('cjm.happenStatus') == null) {
					Ext.Msg.alert('提示信息', '存在数据未保存，请先保存！')
					return;
				}
				if (selected[i].get('cjm.happenStatus') != 0
						&& selected[i].get('cjm.happenStatus') != 3) {
					Ext.Msg.alert('提示信息', '只有未上报或退回的记录可以上报！')
					return;
				}
			}
			var url = "budgetExeControlReport.jsp";
			var args = new Object();
			args.entryId = selected[0].get("cjm.workFlowNo");
			args.workflowType = "budgetExeControlApprove";
			args.happenId = selected[0].get("cjm.happenId");
			var obj = window.showModalDialog(url, args,
					'status:no;dialogWidth=750px;dialogHeight=550px');
			// 按钮设为不可用
			if (obj) {
				queryRec();
			}

		}
	}

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				items : [{
							region : 'west',
							layout : 'fit',
							width : 370,
							items : [westgrid]
						}, {
							region : 'center',
							layout : 'fit',
							items : [eastgrid]
						}]
			});

});