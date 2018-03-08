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

	var flagDateType = 1;
	var obj;
	var bmIdArray = new Array();

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
	// 获得年份
	function getYear() {
		var Y;
		Y = new Date();
		Y = Y.getFullYear().toString(10);
		return Y;
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
						text : '灞桥电厂'
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
	// ----------------------------
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
			}, {
				name : 'forecastBudget'
			}, {
				name : 'adviceBudget'
			}, {
				name : 'adjustBudget'
			}, {
				name : 'budgetAdd'
			}, {
				name : 'budgetChange'
			}, {
				name : 'judgeBudget'
			}, {
				name : 'ensureBudget'
			}, {
				name : 'factHappen'
			}, {
				name : 'financeHappen'
			}, {
				name : 'budgetBasis'
			}, {
				name : 'budgetStatus'
			}, {
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
			
	var formatType;		
	// 遍历所有的REDIO获得ID
	function getChooseQueryType() {
		var list = document.getElementsByName("queryWayRadio");
		for (var i = 0; i < list.length; i++) {
			if (list[i].checked) {
				return list[i].id;
			}
		}
	}
	var yearRadio = new Ext.form.Radio({
		id : 'year',
		name : 'queryWayRadio',
		// blankText : getDate(),
		hideLabel : true,
		boxLabel : '年份',
		checked : true,
		listeners : {
			check : function() {
				var queryType = getChooseQueryType();
				switch (queryType) {
					case 'year' : {
						formatType = 1;
						monthTime.setValue(getYear());
						break;
					}
					case 'month' : {
						monthTime.setValue(getCurrentMonth());
						formatType = 2;
						break;
					}
				}
			}
		}
	});
	
	var monthRadio = new Ext.form.Radio({
		id : 'month',
		name : 'queryWayRadio',
		hideLabel : true,
		boxLabel : '月份'
	});		

	// 预算时间
	var monthTime = new Ext.form.TextField({
				fieldLabel : '预算时间',
				readOnly : true,
				width : 80,
				style : 'cursor:pointer',
				value : getCurrentMonth(),
//				listeners : {
//					focus : function() {
//						WdatePicker({
//									// 时间格式
//									dateFmt : 'yyyy-MM',
//									alwaysUseStartDate : false
//								});
//
//					}
				listeners : {
					focus : function() {
						var format = '';
						if (formatType == 1)
							format = 'yyyy';
						if (formatType == 2)
							format = 'yyyy-MM';
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : format,
									alwaysUseStartDate : false,
									onclearing : function() {
										planStartDate.markInvalid();
									}
								});
					}

				}

			});
	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
				autoScroll : true,
				ds : westgrids,
				tbar : ['预算部门：', budgetDept, '-', '预算时间：', yearRadio, monthRadio, monthTime, {
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
			width : 100,
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
							header : "预算值",
							width : 100,
							align : "center",
							sortable : true,
							dataIndex : 'ensureBudget'
						}, {
							header : "发生值",
							width : 100,
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
	function queryWestRec() {
		if (deptH.getValue() == null || deptH.getValue() == "") {
			Ext.Msg.alert('提示信息', '请先选择预算部门！');
			return;
		}
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
		westgrids.on('load',function() {
			if(westgrids.getCount() == 0) {
				Ext.Msg.alert("提示","请先到全面预算数据处理页面构造当前月数据!")
			}
		})		
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
				budgetItemId : flagBudgetItemId,
				isFeeRegister : 'N'
			}
		})
	}
	// ******************以上未处理*************************************

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
	// *************************以下为经济指标录入右边部分****************************
	// 执行控制ID
	var happenId = new Ext.form.Hidden({
				id : 'happenId',
				name : 'cjm.happenId'
			})
	// 部门预算明细Id
	var budgetItemId = new Ext.form.Hidden({
				id : 'budgetItemId',
				name : 'cjm.budgetItemId'
			})
	// 预算部门id
	var centerId = new Ext.form.Hidden({
				id : 'centerId',
				name : 'cjm.centerId'
			})
	// 预算部门编码
	var centerCode = new Ext.form.TextField({
				id : 'centerCode',
				name : 'centerCode',
				width : 120,
				fieldLabel : '预算部门编码',
				readOnly : true
			})
	// 预算部门名称
	var centerName = new Ext.form.TextField({
				id : 'centerName',
				name : 'centerName',
				width : 120,
				fieldLabel : '预算部门',
				readOnly : true
			})
	// 指标id
	var itemId = new Ext.form.Hidden({
				id : 'itemId',
				name : 'cjm.itemId'
			})
	// 指标编码
	var itemCode = new Ext.form.TextField({
				id : 'itemCode',
				name : 'itemCode',
				fieldLabel : '指标编码',
				width : 120,
				readOnly : true
			})
	// 指标名称
	var itemName = new Ext.form.TextField({
				id : 'itemName',
				name : 'itemName',
				fieldLabel : '指标',
				width : 120,
				readOnly : true
			})
	// 预算时间
	var budgetTime = new Ext.form.TextField({
				id : 'budgetTime',
				name : 'cjm.budgetTime',
				fieldLabel : '预算时间',
				width : 120,
				readOnly : true
			})
	// 说明
	var happenExplain = new Ext.form.TextArea({
				id : 'happenExplain',
				name : 'cjm.happenExplain',
				width : 365,
				fieldLabel : '说明'
			})
	// 实际发生值
	var happenValue = new Ext.form.NumberField({
				id : 'happenValue',
				name : 'cjm.happenValue',
				fieldLabel : '实际发生值',
				width : 120,
				allowBlank : false,
				allowDecimal : true,
				allowNegative : false,
				decimalPrecision : 2
			})
	// 填写人编码
	var fillBy = new Ext.form.Hidden({
				id : 'fillBy',
				name : 'cjm.fillBy'
			})
	// 填写人姓名
	var fillName = new Ext.form.TextField({
				id : 'fillName',
				name : 'fillName',
				fieldLabel : '填写人',
				width : 120,
				readOnly : true
			})
	// 填写时间
	var fillTimeH = new Ext.form.Hidden({
				id : 'fillTimeH',
				name : 'cjm.fillTime'
			})
	// 填写时间
	var fillTime = new Ext.form.TextField({
				id : 'fillTime',
				name : 'fillTime',
				fieldLabel : '填写时间',
				readOnly : true,
				width : 120
			})

	var panel = new Ext.form.FormPanel({
				id : 'panel',
				name : 'panel',
				autoScroll : true,
				width : 550,
				// height : Ext.getBody().getViewSize().height - 3,
				autoHeight : true,
				// height :window.screen.availHeight,
				labelWidth : 100,
				frame : true,
				border : true,
				tbar : [{
							id : 'btnSave',
							iconCls : 'save',
							text : "保存",
							handler : saveRecord
						}],
				items : [{
							text : '',
							height : 10,
							columnWidth : 1
						}, {
							layout : 'column',
							items : [{
								layout : 'form',
								columnWidth : 0.5,
								items : [happenId, budgetItemId, centerId,
										centerCode]
							}, {
								layout : 'form',
								columnWidth : 0.5,
								items : [centerName]
							}, {
								layout : 'form',
								columnWidth : 0.5,
								items : [itemId, itemCode]
							}, {
								layout : 'form',
								columnWidth : 0.5,
								items : [itemName]
							}, {
								layout : 'form',
								columnWidth : 0.5,
								items : [happenValue]
							}, {
								layout : 'form',
								columnWidth : 0.5,
								items : [budgetTime]
							}, {
								layout : 'form',
								columnWidth : 1,
								items : [happenExplain]
							}, {
								layout : 'form',
								columnWidth : 0.5,
								items : [fillBy, fillName]
							}, {
								layout : 'form',
								columnWidth : 0.5,
								items : [fillTimeH, fillTime]
							}]
						}]

			})

	var exPanel = new Ext.form.FormPanel({
				layout : 'fit',
				id : 'exPanel',
				name : 'exPanel',
				items : [panel]
			})
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
					eastgrids.on('load', function() {
								if (eastgrids.getTotalCount() > 0) {
									obj = eastgrids.getAt(0);
									obj.set('cjm.fillBy', flagFillBy);
									panel.getForm().loadRecord(obj);
								} else {
									obj = new datalist({
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

									panel.getForm().loadRecord(obj);
								}
							})
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
	function saveRecord() {
		if (!westsm.hasSelection()) {
			Ext.Msg.alert('提示信息', '请先选择预算指标！');
			return;
		}
		if (Ext.get('happenValue').dom.value == null
				|| Ext.get('happenValue').dom.value == "") {
			Ext.Msg.alert('提示信息', '实际发生值不可为空，请输入！')
			return;
		}
		Ext.Msg.confirm('提示信息', "确定要保存修改吗?", function(button, text) {
					if (button == 'yes') {
						var records = new Array();
						if (Ext.get('happenValue').dom.value != null
								&& Ext.get('happenValue').dom.value != "") {
							obj.set('cjm.happenValue',
									Ext.get('happenValue').dom.value)
						}
						if (Ext.get('happenExplain').dom.value != null
								&& Ext.get('happenExplain').dom.value != "") {
							obj.set('cjm.happenExplain', Ext
											.get('happenExplain').dom.value)
						}
						records.push(obj);
						var addData = new Array();
						var updateData = new Array();
						for (var i = 0; i < records.length; i++) {
							if (records[i].get('cjm.isUse') == 'Y') {
								updateData.push(records[i].data);
							} else {
								addData.push(records[i].data);
							}
						}
						// alert(Ext.util.JSON.encode(bmIdArray));
						Ext.Ajax.request({
									url : 'managebudget/saveControlFeeModified.action',
									method : 'post',
									params : {
										isAdd : Ext.util.JSON.encode(addData),
										isUpdate : Ext.util.JSON
												.encode(updateData),
										isFeeRegister : 'N',
										isHave : isHave,
										bmIdArray : Ext.util.JSON
												.encode(bmIdArray)
									},

									success : function(result, request) {
										Ext.Msg.alert('提示信息', '数据保存修改成功！')
										bmIdArray = [];
										eastgrids.reload();
										westgrids.reload();
									},
									failure : function(result, request) {
										Ext.Msg.alert("提示信息", "数据保存修改失败！");
										bmIdArray = [];
										eastgrids.reload();
									}
								})
					}
				})

	}
	// *************************以上为经济指标录入右边部分****************************

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				items : [{
							region : 'west',
							layout : 'fit',
							width : 500,
							items : [westgrid]
						}, {
							region : 'center',
							autoScroll : true,
							items : [panel]
						}]
			});

});