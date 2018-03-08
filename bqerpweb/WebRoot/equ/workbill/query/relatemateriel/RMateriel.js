// 相关物料
Ext.onReady(function() {

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
	var workorderStatus = getParameter("workorderStatus");
	var MyRecord = Ext.data.Record.create([{
				name : 'matCode'
			}, {
				name : 'receiptBy'
			}, {
				name : 'className'
			}, {
				name : 'issueStatus'
			}, {
				name : 'materialNo'
			}, {
				name : 'materialName'
			}, {
				name : 'specNo'
			}, {
				name : 'appliedCount'
			}, {
				name : 'actIssuedCount'
			}, {
				name : 'stockUmId'
			}, {
				name : 'unitPrice'
			}, {
				name : 'totalPrice'
			}, {
				name : 'statusFlag'
			}, {
				name : 'unitName'
			}, {
				name : 'workflowNo'
			}, {
				name : 'issueHeadId'
			}]);

	// 定义获取数据源
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'workbill/relateMaterielList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalProperty",
							root : "root"
						}, MyRecord)
			});

	ds.load({
				params : {
					woCode : woCode
				}

			});

	// var expander = new Ext.grid.RowExpander({
	// tpl : new Ext.XTemplate(
	// '<div class="detailData">',
	// '',
	// '</div>'
	// )
	// });

	// expander.on("expand",function(expander,r,body,rowIndex){
	// //查找 grid
	// window.testEle=body;
	// //alert(body.id);
	// if (Ext.DomQuery.select("div.x-panel-bwrap",body).length==0){
	// //alert("a");
	// var data=r.json[3];
	// var store=new Ext.data.SimpleStore({
	// fields: ["class","degrade"]
	// ,data:data
	// });
	// }});

	// grid 头
	var rn = new Ext.grid.RowNumberer({
				header : "序列号",
				selectMode : 'true',
				width : 70
			});
	var cm = new Ext.grid.ColumnModel([rn, {
				header : "领料单编号",
				// hidden : true,
				dataIndex : 'matCode'
			}, {
				header : "领料人",
				// hidden : true,
				dataIndex : 'receiptBy'
			}, {
				header : "物质类别",
				sortable : true,
				width : 65,
				dataIndex : 'className'
			}, {
				header : "",
				hidden : true,
				dataIndex : 'issueStatus'
			}, {
				header : "状态",
				sortable : true,
				width : 70,
				dataIndex : 'statusFlag'
			}, {
				header : "物质编码",
				sortable : true,
				width : 65,
				dataIndex : 'materialNo'
			}, {
				header : "物质名称",
				sortable : true,
				width : 65,
				dataIndex : 'materialName'
			}, {
				header : "规格型号",
				sortable : true,
				width : 75,
				dataIndex : 'specNo'
			}, {
				header : "申请数",
				sortable : true,
				width : 75,
				dataIndex : 'appliedCount'
			}, {
				header : "领用数",
				sortable : true,
				width : 60,
				dataIndex : 'actIssuedCount'
			}, {
				header : "计量单位",
				sortable : true,
				dataIndex : 'unitName'
			}, {
				header : "",
				hidden : true,
				sortable : true,
				dataIndex : 'issueHeadId'

			}
	// , {
	// header : "单价",
	// sortable : true,
	// headerAlign : 'center',
	// align : 'left',
	// // renderer : divide4,
	// width : 60,
	// dataIndex : 'unitPrice'
	// }, {
	// header : "总价",
	// // hidden : true,
	// dataIndex : 'totalPrice'
	// }
	]);

	var messageSm = new Ext.grid.RowSelectionModel({
				singleSelect : true,
				listeners : {
					rowselect : function(sm, row, rec) {
						if (rec.get("issueStatus") == 0) {
							Ext.get("del").dom.disabled = true
						} else {
							Ext.get("del").dom.disabled = false
						}
					}
				}
			});

	// --gridpanel显示格式定义-----开始-------------------
	var grid = new Ext.grid.GridPanel({
				region : "center",
				layout : 'fit',
				ds : ds,
				// sm : messageSm,
				cm : cm,
				autoScroll : true,
				enableColumnMove : false,
				autoSizeColumns : true,
				// plugins:[expander] ,
				viewConfig : {
					forceFit : false
				},
				// 按钮
				tbar : [{
							id : "preview",
							text : "预览",
							iconCls : "preview",
							handler : btnPreview
						}, '-', {
							// 刷新按钮
							text : '刷新',
							iconCls : 'reflesh',
							handler : function() {
								ds.reload();
							}

						}]
			});

	// 撤销关联
	function CancelAdd() {
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();

		if (selected.length == 0 || selected.length < 0) {
			Ext.Msg.alert("提示", "请选择要撤销的记录！");
			return;
		}
		var rec = selected[0].data;
		var ids = [];
		if (selected.length == 0 || selected.length < 0) {

		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.matCode) {
					ids.push(member.matCode);
				} else {
					ds.remove(ds.getAt(i));
				}
				Ext.Msg.confirm("撤销", "是否确定撤销的记录？", function(buttonobj) {
							if (buttonobj == "yes") {
								Ext.lib.Ajax.request('POST',
										'workbill/CancelIssueMateriel.action',
										{
											success : function(action) {
												Ext.Msg.alert("提示", "撤销成功！")
												ds.reload();
											},
											failure : function() {
												Ext.Msg.alert('错误',
														'撤销时出现未知错误.');
											}
										}, 'matCode=' + ids.join(","));
							}
						});
			}
		}

	}
	// 领料单选择
	function ChooseAdd() {
		var args = {
			selectModel : 'multiple'
		}
		var rvo = window
				.showModalDialog(
						'MaterilChoose.jsp',
						args,
						'dialogWidth:900px;dialogHeight:600px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		var issueNo = rvo;
		if (issueNo != null) {
			Ext.Ajax.request({
						url : 'workbill/chooseIssueMateriel.action',
						method : 'post',
						params : {
							issueNo : issueNo,
							woCode : woCode
						},
						success : function(result, request) {
							Ext.Msg.alert("提示", "添加成功!");
							ds.reload();

						},
						failure : function(result, request) {
							Ext.Msg.alert("提示", "操作失败");
						}
					});
		}

	}
	// 删除
	function deleteRecord() {
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();

		if (selected.length == 0 || selected.length < 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
			return;
		}
		var rec = selected[0].data;
		if (selected[0].data.issueStatus == "9") {
			var ids = [];
			if (selected.length == 0 || selected.length < 0) {

			} else {
				for (var i = 0; i < selected.length; i += 1) {
					var member = selected[i].data;
					if (member.matCode) {
						ids.push(member.matCode);
					} else {
						ds.remove(ds.getAt(i));
					}
					Ext.Msg.confirm("删除", "是否确定删除的记录？", function(buttonobj) {
								if (buttonobj == "yes") {
									Ext.lib.Ajax.request('POST',
											'workbill/deleteMateriel.action', {
												success : function(action) {
													Ext.Msg
															.alert("提示",
																	"删除成功！")
													ds.reload();
												},
												failure : function() {
													Ext.Msg.alert('错误',
															'删除时出现未知错误.');
												}
											}, 'matCode=' + ids.join(","));
								}
							});
				}
			}
		} else {
			Ext.Msg.alert("提示", "该领料单已上报审批，无法删除！");
			return;
		}
	}
	// 领料单填写
	function issueAdd() {
		myaddpanel.getForm().reset();
		initReceiptBy()
		win.show();
		win.setTitle("领料单填写");
	}

	// 审批查询
	function btnRecord() {
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();

		if (selected.length == 0 || selected.length < 0) {
			Ext.Msg.alert("提示", "请选择要查看的记录！");
			return;
		}
		var entryId = selected[0].data.workflowNo;
		if (entryId == "" || entryId == "null" || entryId == null) {
			Ext.Msg.alert("提示", "流程还未启动!");
		} else {
			var url = "/power/workflow/manager/approveInfo/approveInfo.jsp?entryId="
					+ entryId;
			window
					.showModalDialog(
							url,
							null,
							"dialogWidth:650px;dialogHeight:400px;;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;");

		}
	}
	// 预览
	function btnPreview() {
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		   if(selected.length == 0 || selected.length<0){
		        	Ext.Msg.alert("提示", "请选择工作票！");
		        }else{
		var issueHeadId = selected[0].data.issueHeadId;
		var url = "MaterilInfo.jsp?issueHeadId=" + issueHeadId+"&woCode="+woCode;
		// var url ="faBillSelect.jsp";
		var fo = window.showModalDialog(url, null,
				'dialogWidth=800px;dialogHeight=700px;status=no');
	}}
	//双击预览
	grid.on("dblclick", function() {
				Ext.get("preview").dom.click();
			});
	/**
	 * 画面初始值
	 */
	function initReceiptBy() {
		// 画面初始值设置
		Ext.lib.Ajax.request('POST', 'resource/getInitUserCode.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result) {
							hdnReceiptBy.setValue(result.workerCode);
							receiptBy.setValue(result.workerName);
							hdnReceiptDep.setValue(result.deptCode);
							hdnFeeByDep.setValue(result.deptCode);
							receiptDep.setValue(result.deptName);
							feeByDep.setValue(result.deptName)
						}

					}
				});
	}

	var wd = 150;
	// 领料单编号
	var issueNo = new Ext.form.TextField({
		id : "issueNo",
		fieldLabel : '领料单编号',
		readOnly : true,
		width : wd
			// isFormField : true,
			// name : 'head.issueNo'

		});
	// 申请领用日期
	var dueDate = new Ext.form.TextField({
				style : 'cursor:pointer',
				id : "dueDate",
				name : 'head.dueDate',
				readOnly : true,
				width : wd,
				allowBlank : false,
				fieldLabel : '申请领用日期<font color="red">*</font>',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									alwaysUseStartDate : false,
									dateFmt : 'yyyy-MM-dd',
									onpicked : function() {
										dueDate.clearInvalid();
									},
									onclearing : function() {
										dueDate.markInvalid();
									}
								});
					}
				}
			});
	// /**
	// * 设置归口专业与计划来源联动
	// */
	// function setTxtMrCostSpecial(node) {
	// Ext.Ajax.request({
	// url : 'resource/getOriginalType.action',
	// method : 'post',
	// params : {
	// nodeId : node.id
	// },
	// success : function(action) {
	// var radioData = action.responseText;
	// // Ext.get("txtMrOriginalH").dom.value = radioData;
	// // txtMrOriginalH.setValue(radioData);
	// // if (radioData == 'M') {
	// // feeBySpecial.allowBlank = false;
	// // feeBySpecial.enable();
	// // } else if (radioData == 'O') {
	// // feeBySpecial.setValue("");
	// // feeBySpecial.clearInvalid();
	// // feeBySpecial.disable();
	// // }
	// },
	// failure : function(action) {
	//
	// }
	// });
	// }
	// 计划来源
	var txtMrOriginal = new Ext.ux.ComboBoxTree({
				fieldLabel : '计划来源<font color ="red">*</font>',
				// anchor : '100%',
				displayField : 'text',
				valueField : 'id',
				allowBlank : false,
				width : wd,
				id : 'planOriginalId',
				// hiddenName : 'head.planOriginalId',
				// hiddenName : 'head.txtMrOriginal',
				blankText : '请选择',
				emptyText : '请选择',
				readOnly : true,
				tree : {
					xtype : 'treepanel',
					// 虚拟节点,不能显示
					rootVisible : false,
					loader : new Ext.tree.TreeLoader({
								dataUrl : 'resource/getPlanOriginalNode.action'
							}),
					root : new Ext.tree.AsyncTreeNode({
								id : '0',
								name : '合肥电厂',
								text : '合肥电厂'
							})
					// listeners : {
					// click : setTxtMrCostSpecial
					// }
				},
				selectNodeModel : 'leaf',
				listeners : {
					'select' : function(combo, record, index) {
						// Ext.get("txtMrOriginalH").dom.value = record
						// .get('planOriginalId');
						// Ext.get("txtMrOriginalH").dom.value =
						// record.get('planOriginalId');
						txtMrOriginalH.setValue(record.get('planOriginalId'));

					}
				}
			});
	var txtMrOriginalH = new Ext.form.Hidden({
				id : 'txtMrOriginalH',
				name : 'head.planOriginalId'
			});
	// 申请领用人
	var receiptBy = new Ext.form.TriggerField({
				isFormField : true,
				width : wd,
				fieldLabel : "申请领用人",
				readOnly : true,
				onTriggerClick : function() {
					selectPersonWin();
				}
			});
	// 申请领用人隐藏域
	var hdnReceiptBy = new Ext.form.Hidden({
				id : "receiptBy",
				isFormField : true,
				name : "head.receiptBy"
			});

	// 领用部门
	var receiptDep = new Ext.form.TextField({
				width : wd,
				fieldLabel : "领用部门",
				readOnly : true
			});
	// 领用部门
	var hdnReceiptDep = new Ext.form.Hidden({
				id : "receiptDep",
				name : "head.receiptDep"
			});

	// 费用归口部门
	var feeByDep = new Ext.form.TextField({
				width : wd,
				readOnly : true,
				fieldLabel : "费用归口部门"
			});
	// 费用归口部门
	var hdnFeeByDep = new Ext.form.Hidden({
				id : "feeByDep",
				name : "head.feeByDep"
			});

	/**
	 * 人员选择画面处理
	 */
	function selectPersonWin() {
		if (receiptBy.disabled) {
			return;
		}
		var args = {
			selectModel : 'signal',
			notIn : "",
			rootNode : {
				id : '-1',
				text : '合肥电厂'
			}
		}
		var person = window
				.showModalDialog(
						'../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(person) != "undefined") {
			receiptBy.setValue(person.workerName);
			hdnReceiptBy.setValue(person.workerCode);
			hdnReceiptDep.setValue(person.deptCode);
			hdnFeeByDep.setValue(person.deptCode);
			receiptDep.setValue(person.deptName);
			feeByDep.setValue(person.deptName)
		}
	}

	// 物料编码
	var materialNo = new Ext.form.ComboBox({
				id : "materialNo",
				// hiddenName : "Details.materialId",
				fieldLabel : "物料编码<font color ='red'>*</font>",
				emptyText : '',
				// displayField : "faWoCode",
				// valueField : "faWoCode",
				readOnly : true,
				width : wd,
				onTriggerClick : function(e) {
					var mate = window.showModalDialog(
							'../../../run/resource/plan/RP001.jsp', window,
							'dialogWidth=800px;dialogHeight=550px;status=no');
					if (typeof(mate) != "undefined") {
						// 设置物料
						// return mate;
						materialNo.setValue(mate.materialNo);
						// alert(mate.materialId)
						materialId.setValue(mate.materialId)

					}
				}
			});
	// 物料ID
	var materialId = new Ext.form.Hidden({
				id : 'materialId',
				name : 'materialId'
			});

	// 申请数量
	var appliedCount = {
		id : "appliedCount",
		xtype : "numberfield",
		fieldLabel : '申请数量<font color ="red">*</font>',
		// boxLabel : '预算费用(万元)',
		name : 'appliedCount',
		readOnly : false,
		value : '',
		width : wd
	}

	var myaddpanel = new Ext.FormPanel({
				frame : true,
				labelAlign : 'right',
				fileUpload : true,
				title : '',
				items : [issueNo, dueDate, txtMrOriginal, txtMrOriginalH,
						receiptBy, hdnReceiptBy, receiptDep, hdnReceiptDep,
						feeByDep, hdnFeeByDep, materialNo, appliedCount,
						materialId]
			});

	var win = new Ext.Window({
				width : 300,
				height : 270,
				buttonAlign : "center",
				items : [myaddpanel],
				layout : 'fit',
				closeAction : 'hide',
				modal : true,
				buttons : [{
					text : '保存',
					handler : function() {
						Ext.get("head.planOriginalId").dom.value = txtMrOriginal.value;
						if (myaddpanel.getForm().isValid()) {
							myaddpanel.getForm().submit({
								waitMsg : '保存中,请稍后...',
								url : 'workbill/addIssueMateriel.action',
								method : 'post',
								params : {
									woCode : woCode
								},
								success : function(form, action) {
									var o = eval('('
											+ action.response.responseText
											+ ')');
									Ext.Msg.alert('提示', o.msg);
									ds.reload();
									myaddpanel.getForm().reset();
									win.hide();
								},
								failure : function(form, action) {
									Ext.Msg.alert('提示', "保存失败");
									win.hide();
								}
							});
						}
					}
				}, {
					text : '取消',
					handler : function() {
						win.hide();
					}
				}]
			});

	// 设定布局器及面板
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "fit",
				items : [grid]
			});
	// 设置按钮
//	if (workorderStatus != 0) {
//		Ext.get("addcus").dom.disabled = true;
//		Ext.get("del").dom.disabled = true;
//		Ext.get("btnChoose").dom.disabled = true;
//		Ext.get("cancerbtn").dom.disabled = true;
//	}
})
