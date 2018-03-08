// Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var repairId = parent.repair;
	// 左边列表中的数据
	var westdatalist = new Ext.data.Record.create([{
				name : 'prepair.repairDetailId'
			}, {
				name : 'prepair.repairId'
			}, {
				name : 'prepair.contentId'
			}, {
				name : 'prepair.detailNo'
			}, {
				name : 'prepair.detailName'
			}, {
				name : 'prepair.detailContent'
			}, {
				name : 'prepair.equCode'
			}, {
				name : 'prepair.chargeBy'
			}, {
				name : 'prepair.specialityCode'
			}, {
				name : 'prepair.workingdays'
			}, {
				name : 'prepair.fare'
			}, {
				name : 'prepair.planStartTime'
			}, {
				name : 'prepair.planEndTime'
			}, {
				name : 'prepair.content'
			}, {
				name : 'prepair.memo'
			}, {
				name : 'prepair.annex'
			}, {
				name : 'chargeByName'
			}, {
				name : 'contentName'
			}, {
				name : 'equName'
			}, {
				name : 'planEndTime'
			}, {
				name : 'planStartTime'
			}, {
				name : 'specialityName'
			}]);

	var westgrids = new Ext.data.JsonStore({
				url : 'equplan/getDetailPlanList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : westdatalist
			});
	westgrids.load({
				params : {
					repairId : repairId,
					start : 0,
					limit : 18
				}
			});
	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
				title : '检修明细列表',
				width : 200,
				split : true,
				autoScroll : true,
				ds : westgrids,
				columns : [new Ext.grid.RowNumberer({
									header : "序号",
									width : 31
								}), {
							hidden : true,
							align : "center",
							sortable : true,
							dataIndex : 'prepair.repairDetailId'
						}, {
							header : "检修明细编号",
							width : 100,
							align : "center",
							sortable : true,
							dataIndex : 'prepair.detailNo'
						}, {
							header : "检修明细名称",
							width : 180,
							align : "center",
							sortable : true,
							dataIndex : 'prepair.detailName'
						}],
				// // frame : true,
				// bbar : new Ext.PagingToolbar({
				// pageSize : 18,
				// store : westgrids,
				// displayInfo : true,
				// displayMsg : "共 {2} 条",
				// emptyMsg : "没有记录"
				// }),
				border : true
			});

	// eastpanel
	var repairDetailId;
	var bview;
	// 系统当前时间
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
	// 检修明细编号
	var detailNo = new Ext.form.TextField({
				id : "detailNo",
				fieldLabel : "检修明细编号 <font color='red'>*</font>",
				name : 'prepair.detailNo',
				allowBlank : false,
				anchor : "100%"
			})
	// 检修明细名称
	var detailName = new Ext.form.TextField({
				id : "detailName",
				fieldLabel : "检修明细名称 <font color='red'>*</font>",
				name : 'prepair.detailName',
				allowBlank : false,
				anchor : "100%"
			});
	// 设备名称
	var equName = new Ext.form.TriggerField({
				fieldLabel : "设备名称<font color='red'>*</font>",
				id : "equName",
				onTriggerClick : mainEquSelect,
				anchor : "100%"
			});
	var hidequCode = new Ext.form.Hidden({
				id : 'hidequCode',
				name : 'prepair.equCode'
			})
	function mainEquSelect() {
		var url = "../../../comm/jsp/equselect/selectAttribute.jsp?";
		url += "op=one";
		var equ = window.showModalDialog(url, window,
				'dialogWidth=600px;dialogHeight=400px;status=no');
		if (typeof(equ) != "undefined") {
			var name = equ.name;
			var code = equ.code;
			equName.setValue(name);
			Ext.getCmp("hidequCode").setValue(code);
			// 设置所属系统
			// cbxChargeBySystem.setValue(code.substring(0,2) );
		}
	}

	// 检修专业
	var storeRepairSpecail = new Ext.data.Store({
				autoLoad : true,
				proxy : new Ext.data.HttpProxy({
							url : 'workticket/getDetailRepairSpecialityType.action'
						}),
				reader : new Ext.data.JsonReader({
							root : 'list'
						}, [{
									name : 'specialityCode',
									mapping : 'specialityCode'
								}, {
									name : 'specialityName',
									mapping : 'specialityName'
								}])
			});
	storeRepairSpecail.load();
	
	var specialityName = new Ext.form.ComboBox({
				id : 'repairSpecailCode',
				fieldLabel : "检修专业<font color='red'>*</font>",
				store : storeRepairSpecail,
				displayField : "specialityName",
				valueField : "specialityCode",
				hiddenName : 'prepair.specialityCode',
				mode : 'remote',
				triggerAction : 'all',
				value : '',
				readOnly : true,
				anchor : "100%"
			})

	// 维修内容
	var contentStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'equplan/findPlanContentList.action'
						}),
				reader : new Ext.data.JsonReader({
							root : 'list'
						}, [{
									name : 'contentId',
									mapping : 'contentId'
								}, {
									name : 'contentName',
									mapping : 'contentName'
								}])
			});
	contentStore.load();
	var contentName = new Ext.form.ComboBox({
				id : "contentName",
				store : contentStore,
				fieldLabel : "维修内容<font color='red'>*</font>",
				displayField : "contentName",
				valueField : "contentId",
				hiddenName : 'prepair.contentId',
				mode : 'remote',
				triggerAction : 'all',
				value : '',
				readOnly : true,
				anchor : "100%"
			})

	// 计划工期
	var startTime = new Ext.form.TextField({
				style : 'cursor:pointer',
				id : "startTime",
				name : 'prepair.planStartTime',
				readOnly : true,
				anchor : "100%",
				// allowBlank : false,
				fieldLabel : '计划工期<font color="red">*</font>',
				value : getDate(),
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d 00:00:00',
									alwaysUseStartDate : false,
									dateFmt : 'yyyy-MM-dd HH:mm:ss',
									onpicked : function() {
										if (endTime.getValue() != "") {
											if (startTime.getValue() == ""
													|| startTime.getValue() > endTime
															.getValue()) {
												Ext.Msg.alert("提示",
														"必须小于计划结束时间");
												startTime.setValue("");
												return;
											}
											// startTime.clearInvalid();
										}
									},
									onclearing : function() {
										startTime.markInvalid();
									}
								});
						this.blur();
					}
				}
			});
	var endTime = new Ext.form.TextField({
				style : 'cursor:pointer',
				id : "endTime",
				name : 'prepair.planEndTime',
				readOnly : true,
				anchor : "100%",
				// allowBlank : false,<font color='red'>*</font>
				fieldLabel : "至<font color='red'>*</font>",
				value : getDate(),
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d 00:00',
									alwaysUseStartDate : false,
									dateFmt : 'yyyy-MM-dd HH:mm:ss',
									onpicked : function() {
										if (startTime.getValue() == ""
												|| startTime.getValue() > endTime
														.getValue()) {
											Ext.Msg.alert("提示", "必须大于计划开始时间");
											endTime.setValue("")
											return;
										}
										// endTime.clearInvalid();
									},
									onclearing : function() {
										endTime.markInvalid();
									}
								});
						this.blur();
					}

				}

			})
	// 计划工日
	var planTimeLimit = new Ext.form.NumberField({
				id : "planTimeLimit",
				// xtype : "textfield",
				fieldLabel : "计划工日(天)<font color='red'>*</font>",
				name : 'prepair.workingdays',
				readOnly : false,
				value : '',
				anchor : "100%"
			})
	// 预算费用
	var planAmount = new Ext.form.NumberField({
				id : "planAmount",
				fieldLabel : '预算费用(万元)<font color="red">*</font>',
				boxLabel : '预算费用(万元)',
				name : 'prepair.fare',
				readOnly : false,
				value : '',
				anchor : "100%"
			})
	// 工程负责人
	var chargeBy = new Ext.form.TriggerField({
				isFormField : true,
				fieldLabel : "工程负责人<font color='red'>*</font>",
				readOnly : true,
				name : "chargeByName",
				anchor : "100%",
				onTriggerClick : function() {
					selectPersonWin();
				}
			});
	// 工程负责人隐藏
	var hdnchargeBy = new Ext.form.Hidden({
				id : "chargeBy",
				isFormField : true,
				name : "prepair.chargeBy"
			});
	/**
	 * 负责人选择画面处理
	 */
	function selectPersonWin() {
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
			chargeBy.setValue(person.workerName);
			hdnchargeBy.setValue(person.workerCode);
		}
	}

	// 内容
	var content = new Ext.form.TextArea({
				id : "content",
				fieldLabel : '内容',
				name : 'prepair.content',
				readOnly : false,
				anchor : "100%"
			})
	// 备注
	var memo = new Ext.form.TextArea({
				id : "memo",
				fieldLabel : '备注',
				name : 'prepair.memo',
				readOnly : false,
				anchor : "100%"
			})
	// 附件
	var annex = {
		id : "annex",
		xtype : 'fileuploadfield',
		name : "annex",
		// xtype : "textfield",
		fieldLabel : '附件',
		// inputType : "file",
		// readOnly : true,
		height : 21,
		anchor : "100%",
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
	var eastpanel = new Ext.FormPanel({
				bodyStyle : "padding:10px 10px 0px 0px",
				region : "right",
				layout : 'form',
				title : "检修明细信息",
				labelAlign : "right",
				autoheight : true,
				autoScroll : true,
				fileUpload : true,
				containerScroll : true,
				tbar : [{
							id : 'btnFirst',
							text : '新增',
							iconCls : 'add',
							handler : AddPlan
						}, '-', {
							id : 'btnAdd',
							text : '删除',
							iconCls : 'delete',
							handler : DelPlan
						}, '-', {
							id : 'btnAdd',
							text : '保存',
							iconCls : 'add',
							handler : SavePlan
						}],
				items : [{
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 0.5,
										border : false,
										layout : 'form',
										items : [detailNo]
									}, {
										columnWidth : 0.5,
										border : false,
										layout : 'form',
										items : [detailName]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 0.5,
										border : false,
										layout : 'form',
										items : [equName, hidequCode]
									}, {
										columnWidth : 0.5,
										border : false,
										layout : 'form',
										items : [chargeBy, hdnchargeBy]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 0.5,
										border : false,
										layout : "form",
										items : [specialityName]
									}, {
										columnWidth : 0.5,
										border : false,
										layout : "form",
										items : [contentName]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 0.5,
										border : false,
										layout : "form",
										items : [startTime]
									}, {
										columnWidth : 0.5,
										border : false,
										layout : "form",
										items : [endTime]
									}]

						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 0.5,
										border : false,
										layout : "form",
										items : [planTimeLimit]
									}, {
										columnWidth : 0.5,
										border : false,
										layout : "form",
										items : [planAmount]
									}]

						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 1,
										border : false,
										layout : "form",
										items : [content]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 1,
										border : false,
										layout : 'form',
										items : [memo]
									}]
						}, {

							layout : 'column',
							border : false,
							items : [{
										columnWidth : 0.75,
										bodyStyle : "padding:2px 0px 1px 0px",
										border : false,
										// labelHeight : 4160,
										layout : "form",
										items : [annex]
									}, {
										columnWidth : 0.25,
										// html : '<a
										// style=\"cursor:hand;color:red\"
										// onClick=\"window.open();\"/>查看附件</a>',
										bodyStyle : "padding:2px 0px 1px 0px",
										border : false,
										layout : "form",
										items : [btnView]
									}]
						}]
			});

	westgrid.on("rowclick", function() {
				var rec = westgrid.getSelectionModel().getSelected();
				eastpanel.getForm().loadRecord(rec);
				repairDetailId = rec.get("prepair.repairDetailId");
				startTime.setValue(rec.get("planStartTime"))
				endTime.setValue(rec.get("planEndTime"))
				contentName.setValue(rec.get("prepair.contentId"))
				if (rec.get("prepair.annex") != null) {
					bview = rec.get("prepair.annex");
					var temp = rec.get("prepair.annex").split("/");
					var annex = temp[4]
					btnView.setVisible(true);
					Ext.get("annex").dom.value = annex;
					bview = rec.get("prepair.annex");
				} else {
					Ext.get("annex").dom.value = "";
					btnView.setVisible(false);
				}
			})
	function AddPlan() {
		repairDetailId = null;
		btnView.setVisible(false);
		eastpanel.getForm().reset();
	}
	function DelPlan() {
		if (repairDetailId != null) {
			Ext.Ajax.request({
						waitMsg : '删除中,请稍后...',
						url : 'equplan/delDetailPlan.action',
						params : {
							repairDetailId : repairDetailId
						},
						success : function(result, request) {
							westgrids.reload();
							repairDetailId = null;
							btnView.setVisible(false);
							eastpanel.getForm().reset();
						},
						failure : function() {
							Ext.Msg.alert('提示信息', '服务器错误,请稍候重试!')
						}
					});
		} else {
			Ext.Msg.alert('提示信息', '请选择要删除的项目记录!')
		}
	}
	function SavePlan() {
		if (repairId != "") {
			var url;
			if (repairDetailId != null) {
				// alert(contentName.getValue())
				url = "equplan/updateDetailPlan.action";
			} else {
				url = "equplan/saveDetailPlan.action";
			}
			if (eastpanel.getForm().isValid()) {
				eastpanel.getForm().submit({
							url : url,
							method : 'post',
							params : {
								"prepair.repairId" : repairId,
								"prepair.repairDetailId" : repairDetailId,
								filePath : Ext.get("annex").dom.value
							},
							success : function(form, action) {

								// var msg = eval('(' +
								// action.response.responseText + ')');
//								Ext.Msg.alert('提示', "操作成功!");
								westgrids.reload();
								// repairId = msg.repairId;
								// parent.iframe1.document.getElementById("btnQuery")
								// .click();
							},
							failure : function(form, action) {
								var o = eval("(" + action.response.responseText
										+ ")");
								Ext.Msg.alert('提示', o.msg);
							}
						});
			}
		} else {
			Ext.Msg.alert('提示', '请走列表中选择检修！');
		}
	}
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [{
							bodyStyle : "padding: 2,2,2,2",
							layout : 'fit',
							border : false,
							frame : false,
							region : "west",
							width : '35%',
							items : [westgrid]
						}, {
							bodyStyle : "padding: 2,2,2,2",
							region : "center",
							border : false,
							autoScroll : true,
							frame : false,
							layout : 'fit',
							items : [eastpanel]
						}]
			});

});