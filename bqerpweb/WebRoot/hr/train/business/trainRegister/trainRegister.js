Ext.ns("trainMaint.trainRegister");
trainMaint.trainRegister = function() {
	var trainId = null;
	var  isCopy;
	// 姓名
	var person = new Power.person({
				hiddenName : 'outtrain.workCode',
				id : 'workCode',
				anchor : '95%',
				fieldLabel : '姓名'
			});
	person.btnConfirm.on("click", function() {
				var wc = person.getSelectedPersonCode();
				Ext.Ajax.request({
							method : 'post',
							url : 'com/getEmployeeInfo.action',
							params : {
								workerCode : wc
							},

							success : function(res, action) {
								var emp = res.responseText;
								var o = Ext.decode(emp);
								sex.setValue(o.sex);
								stationId.setValue(o.stationName);
								technologyTitlesTypeId
										.setValue(o.technologyTitlesTypeName);
								nowStationId.setValue(o.stationName);
								dept.combo.setValue(o.deptName);
								Ext.get("outtrain.deptCode").dom.value = o.deptCode;
							}

						});
			});
	// 性别
	var sex = new Ext.form.ComboBox({
				fieldLabel : '性别',
				store : new Ext.data.SimpleStore({
							fields : ['value', 'text'],
							data : [['M', '男'], ['W', '女']]
						}),
				id : 'sex',
				hiddenName : 'outtrain.sex',
				valueField : "value",
				displayField : "text",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				anchor : '95%'
			});
	// 工作部门
	var dept = new Power.dept({}, false, {
				hiddenName : 'outtrain.deptCode',
				anchor : '95%',
				fieldLabel : '工作部门'
			});

	var stationIdRecord = Ext.data.Record.create([{
				name : 'stationId',
				mapping : 0
			}, {
				name : 'stationName',
				mapping : 1
			}]);

	var stationIdStore = new Ext.data.JsonStore({
				url : 'com/findStationIdList.action',
				root : 'list',
				fields : stationIdRecord
			});

	stationIdStore.load();

	// 职务
	var stationId = new Ext.form.ComboBox({
				fieldLabel : '职务',
				store : stationIdStore,
				id : 'stationId',
				name : 'outtrain.stationId',
				valueField : "stationId",
				displayField : "stationName",
				mode : 'local',
				typeAhead : true,
				editable : true,
				triggerAction : 'all',
				allowBlank : false,
				anchor : '95%'
			});

	var technologyTitlesTypeIdRecord = Ext.data.Record.create([{
				name : 'technologyTitlesTypeId',
				mapping : 0
			}, {
				name : 'technologyTitlesTypeName',
				mapping : 1
			}]);

	var technologyTitlesTypeIdStore = new Ext.data.JsonStore({
				url : 'com/findTechnologyTitlesType.action',
				root : 'list',
				fields : technologyTitlesTypeIdRecord
			});

	technologyTitlesTypeIdStore.load();
	// 职称
	var technologyTitlesTypeId = new Ext.form.ComboBox({
				fieldLabel : '职称',
				store : technologyTitlesTypeIdStore,
				id : 'technologyTitlesTypeId',
				name : 'outtrain.technologyTitlesTypeId',
				valueField : "technologyTitlesTypeId",
				displayField : "technologyTitlesTypeName",
				mode : 'local',
				typeAhead : false,
				editable : true,
				triggerAction : 'all',
				allowBlank : false,
				anchor : '95%'
			});

	var nowStationIdRecord = Ext.data.Record.create([{
				name : 'stationId',
				mapping : 0
			}, {
				name : 'stationName',
				mapping : 1
			}]);

	var nowStationIdStore = new Ext.data.JsonStore({
				url : 'com/findStationIdList.action',
				root : 'list',
				fields : nowStationIdRecord
			});

	nowStationIdStore.load();
	// 现任岗位
	var nowStationId = new Ext.form.ComboBox({
				fieldLabel : '现任岗位',
				store : nowStationIdStore,
				name : "outtrain.nowStationId",
				valueField : "stationId",
				displayField : "stationName",
				mode : 'local',
				typeAhead : true,
				editable : true,
				triggerAction : 'all',
				allowBlank : false,
				anchor : '95%'
			});

	// 培训任务来源
	var trainSource = new Ext.form.TextField({
				name : 'outtrain.trainSource',
				xtype : 'textfield',
				fieldLabel : '培训任务来源（发文单位及文件号）',
				readOnly : false,
				anchor : '88%',
				blankText : '不可为空！'
			});

	// 培训地点
	var trainSite = new Ext.form.TextField({
				name : 'outtrain.trainSite',
				xtype : 'textfield',
				fieldLabel : '培训地点',
				readOnly : false,
				anchor : '88%',
				blankText : '不可为空！'
			});

	// 培训内容
	var trainContent = new Ext.form.TextField({
				name : 'outtrain.trainContent',
				xtype : 'textfield',
				fieldLabel : '培训内容',
				readOnly : false,
				anchor : '88%',
				blankText : '不可为空！'
			});

	var trainStart = new Date();
	trainStart = trainStart.format('Y-01-01');
	// 培训开始时间
	var trainStartdate = new Ext.form.TextField({
//				width : 100,
		        anchor : '95%',
				style : 'cursor:pointer',
				name : "outtrain.trainStartdate",
				fieldLabel : '培训时间',
				allowBlank : true,
				readOnly : true,
				value : trainStart,
				listeners : {
					focus : function() {
						WdatePicker({
									isShowClear : true,
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd'
								});
					}
				}
			});

	var trainEnd = new Date();
	trainEnd = trainEnd.format('Y-m-d');

	// 培训结束时间
	var trainEnddate = new Ext.form.TextField({
				//width : 100,
		         anchor : '95%',
				style : 'cursor:pointer',
				name : "outtrain.trainEnddate",
				fieldLabel : '至',
				allowBlank : true,
				value : trainEnd,
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
									isShowClear : true,
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd'
								});
					}
				}
			});
  //  培训机构 add by wpzhu 20100702-------------
	var trainInsititution = new Ext.form.TextField({
				name : 'outtrain.trainInsititution',
				xtype : 'textfield',
				fieldLabel : '培训机构 ',
				readOnly : false,
				//width : 130,
				 anchor : '95%',
				blankText : '不可为空！'
			});
  var isReceive = new Ext.form.ComboBox({
				fieldLabel : '证件是否收到',
				store : new Ext.data.SimpleStore({
							fields : ['value', 'text'],
							data : [['Y', '是'], ['N', '否']]
						}),
				id : 'documents',
				hiddenName : 'outtrain.isReceived',
				valueField : "value",
				displayField : "text",
				mode : 'local',
				typeAhead : true,
				value:'Y',
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				anchor : '95%'
			});
			//-------------------------------------------------------
	// 培训性质
	var rbgTrainCharacter = new Ext.form.RadioGroup({
				width : 500,
				fieldLabel : '培训性质',
				hideLabel : false,
				columns : 3,
				vertical : true,
				items : [{
							boxLabel : '学历培训',
							inputValue : '1',
							name : 'outtrain.trainCharacter',
							checked : true
						}, {
							boxLabel : '岗位提升',
							inputValue : '2',
							name : 'outtrain.trainCharacter'
						}, {
							boxLabel : '取证培训',
							inputValue : '3',
							name : 'outtrain.trainCharacter'
						}, {
							boxLabel : '复证培训',
							inputValue : '4',
							name : 'outtrain.trainCharacter'
						}, {
							boxLabel : '技术竞赛',
							inputValue : '5',
							name : 'outtrain.trainCharacter'
						}, {
							boxLabel : '其他',
							inputValue : '6',
							name : 'outtrain.trainCharacter'
						}]
			});

	// 证书类别

	var rbgCertificateSort = new trainMaint.certificate({}, false, {
				hiddenName : 'outtrain.certificateSort',
				//width : 100,
				anchor : '95%',
				fieldLabel : '证书类别'
			});

	// var rbgCertificateSort = new Ext.form.RadioGroup({
	// width : 500,
	// fieldLabel : '证书类别',
	// hideLabel : false,
	// columns : 4,
	// vertical : true,
	// items : [{
	// boxLabel : '资格证书',
	// inputValue : 'a',
	// name : 'outtrain.certificateSort',
	// checked : true
	// }, {
	// boxLabel : '上岗证书',
	// inputValue : 'b',
	// name : 'outtrain.certificateSort'
	// }, {
	// boxLabel : '结业证书',
	// inputValue : 'c',
	// name : 'outtrain.certificateSort'
	// }, {
	// boxLabel : '毕业证书',
	// inputValue : 'd',
	// name : 'outtrain.certificateSort'
	// }]
	// });

	// 证书编号
	var certificateNo = new Ext.form.TextField({
				name : 'outtrain.certificateNo',
				xtype : 'textfield',
				fieldLabel : '证书编号',
				readOnly : false,
				//width : 100,
				anchor : '95%',
				blankText : '不可为空！'
			});

			
	var certificateStart = new Date();
	certificateStart = certificateStart.format('Y-01-01');
	// 证书有效开始时间
	var certificateStartdate = new Ext.form.TextField({
				//width : 100,
		         anchor : '95%',
				style : 'cursor:pointer',
				name : "outtrain.certificateStartTime",
				fieldLabel : '证书有效时间',
				allowBlank : true,
				readOnly : true,
				value : certificateStart,
				listeners : {
					focus : function() {
						WdatePicker({
									isShowClear : true,
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd'
								});
					}
				}
			});

	var certificateEnd = new Date();
	certificateEnd = certificateEnd.format('Y-m-d');

	// 证书有效结束时间
	var certificateEnddate = new Ext.form.TextField({
			//	width : 130,
		        anchor : '40.5%',
				style : 'cursor:pointer',
				name : "outtrain.certificateEndTime",
				fieldLabel : '至',
				allowBlank : true,
				value : certificateEnd,
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
									isShowClear : true,
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd'
								});
					}
				}
			});
			
	var save = new Ext.Button({
				text : '保存',
				iconCls : 'save',
				handler : function() {
					var myurl = "";
					if (trainId == "" || trainId == null) {
						myurl = "com/saveOuttrainInfo.action";
					} else {
						
						myurl = "com/updateOuttrainInfo.action";
					}
					if(person.combo.getValue()==null||person.combo.getValue()=="")
					{
						Ext.MessageBox.alert("提示","请填写姓名！");
						return;
					}
				   
					outTrainForm.getForm().submit({
						method : 'POST',
						url : myurl,
						params : {
							trainStartdate : trainStartdate.getValue(),
							trainEnddate : trainEnddate.getValue(),
							trainId : trainId
						},
						success : function(form, action) {
							var o = eval("(" + action.response.responseText
									+ ")");
							Ext.Msg.alert("注意", o.msg);
							trainId = o.id;
							Ext.getCmp("btnAdd").setDisabled(false);
							Ext.getCmp("btnDelete").setDisabled(false);
							Ext.getCmp("btnSave").setDisabled(false);
							// if (o.msg.indexOf("成功") != -1) {
							// outTrainForm.getForm().reset();
							// }
						},
						faliue : function() {
							Ext.Msg.alert('错误', '出现未知错误.');
						}
					});
				}

			});

	// 费用明细增加时取费用类别
	var mySortRecord = Ext.data.Record.create([{
				name : 'feeSortId',
				mapping : 0
			}, {
				name : 'feeSortName',
				mapping : 1
			}]);
	var sortStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'com/findTrainSortList.action'
						}),

				reader : new Ext.data.JsonReader({
							totalCount : "totalCount",
							root : "list"
						}, mySortRecord)
			});
	sortStore.load()

	var myRecord = Ext.data.Record.create([{
				name : 'feeId',
				mapping : 0
			}, {
				name : 'trainId',
				mapping : 1
			}, {
				name : 'feeSortId',
				mapping : 2
			}, {
				name : 'feeSortName',
				mapping : 3
			}, {
				name : 'actualFee',
				mapping : 4
			}, {
				name : 'feeDept',
				mapping : 5
			}, {
				name : 'deptName',
				mapping : 6
			}, {
				name : 'memo',
				mapping : 7
			}]);
	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false

			});
	var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'com/findOuttrainDetailList.action'
						}),
				reader : new Ext.data.ArrayReader({}, myRecord)
			})
	var detailDept = new Power.dept({
				anchor : '100%'
			});
	detailDept.btnConfrim.on("click", function() {
				if (grid.getSelectionModel().hasSelection()) {
					var rec = grid.getSelectionModel().getSelected();
					var aDept = detailDept.getValue();
					rec.set('feeDept', aDept.key);
					rec.set('deptName', aDept.value);
				}
			});

	// 增加
	function addTheme() {
		var count = store.getCount();
		var currentIndex = count;
		var o = new myRecord({
					'trainId' : trainId,
					'feeId' : null,
					'feeSortId' : null,
					'feeSortName' : null,
					'actualFee' : null,
					'feeDept' : null,
					'deptName' : null,
					'memo' : null
				});
		grid.stopEditing();
		store.insert(currentIndex, o);
		sm.selectRow(currentIndex);
		grid.startEditing(currentIndex, 3);
	}

	// 删除记录
	var ids = new Array();
	function deleteTheme() {
		grid.stopEditing();
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("feeId") != null) {
					ids.push(member.get("feeId"));
				}
				grid.getStore().remove(member);
				grid.getStore().getModifiedRecords().remove(member);
			}
		}
	}

	// 保存
	function saveTheme() {
		grid.stopEditing();
		var modifyRec = grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			for (var i = 0; i < modifyRec.length; i++) {
				if (modifyRec[i].data.feeSortId == null
						|| modifyRec[i].data.feeSortId == "") {
					Ext.Msg.alert('提示信息', '费用类别不可为空，请输入！')
					return;
				}
				if (modifyRec[i].data.actualFee == null
						|| modifyRec[i].data.actualFee == "") {
					Ext.Msg.alert('提示信息', '实际费用不可为空，请输入！')
					return;
				}
				if (modifyRec[i].data.feeDept == null
						|| modifyRec[i].data.feeDept == "") {
					Ext.Msg.alert('提示信息', '培训归口部门不可为空，请输入！')
					return;
				}
			}
			Ext.Msg.confirm('提示信息', '确定要保存修改吗？', function(button) {
				if (button == 'yes') {
					var addData = new Array();
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {

						if (modifyRec[i].get('feeId') != null) {
							updateData.push(modifyRec[i].data);
						} else {
							addData.push(modifyRec[i].data);
						}

					}
					Ext.Ajax.request({
								url : 'com/saveOuttrainDetailInfo.action',
								method : 'post',
								params : {
									trainId : trainId,
									isAdd : Ext.util.JSON.encode(addData),
									isUpdate : Ext.util.JSON.encode(updateData),
									ids : ids.join(",")

								},
								success : function(result, request) {
									var o = Ext.util.JSON
											.decode(result.responseText);
									Ext.Msg.alert("提示信息", o.msg);
									store.rejectChanges();
									ids = [];
									store.load({
												params : {
													trainId : trainId
												}
											});
								},
								failure : function(result, request) {
									Ext.Msg.alert("提示信息", "数据保存修改失败！");
									store.rejectChanges();
									ids = [];
									store.reload();
								}
							})
				}
			})

		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}
	var contbar = new Ext.Toolbar({
				items : [{
							id : 'btnAdd',
							iconCls : 'add',
							text : "增加",
							minWidth : 70,
							disabled : true,
							handler : addTheme
						}, '-', {
							id : 'btnDelete',
							iconCls : 'delete',
							minWidth : 70,
							disabled : true,
							text : "删除",
							handler : deleteTheme
						}, '-', {
							id : 'btnSave',
							iconCls : 'save',
							text : "保存修改",
							minWidth : 70,
							disabled : true,
							handler : saveTheme
						}]

			});
	var grid = new Ext.grid.EditorGridPanel({
				layout : 'fit',
				title : "费用明细",
				autoHeight : true,
				autoScrolla : true,
				sm : sm,
				enableColumnMove : false,
				store : store,
				columns : [sm, new Ext.grid.RowNumberer({
									header : "行号",
									width : 35
								}), {
							header : "ID",
							sortable : true,
							dataIndex : 'feeId',
							hidden : true
						}, {
							header : "费用类别",
							width : 200,
							sortable : true,
							dataIndex : 'feeSortId',
							editor : new Ext.form.ComboBox({
										store : sortStore,
										displayField : "feeSortName",
										valueField : "feeSortId",
										id : 'feeSortName',
										allowBlank : false,
										mode : 'local',
										triggerAction : 'all',
										readOnly : true
									}),
							renderer : function(v) {
								for (var i = 0; i < sortStore.getCount(); i++) {
									var rec = sortStore.getAt(i);
									if (rec.get("feeSortId") == v) {
										return rec.get("feeSortName");
									}
								}
								return v;
							}
						}, {
							header : "实际费用",
							width : 200,
							sortable : true,
							dataIndex : 'actualFee',
							editor : new Ext.form.TextField({
										style : 'cursor:pointer',
										allowNegative : false,
										allowDecimal : false,
										allowBlank : true
									})
						}, {
							header : "费用归口部门",
							width : 200,
							sortable : true,
							dataIndex : 'feeDept',
							editor : detailDept.combo,
							renderer : function(value, metadata, record) {
								if (value != null)
									return record.get('deptName')
							}
						}, {
							header : "说明",
							width : 180,
							sortable : true,
							dataIndex : 'memo',
							editor : new Ext.form.TextField({
										style : 'cursor:pointer',
										allowNegative : false,
										allowDecimal : false,
										allowBlank : true
									})
						}],
				tbar : contbar,
				clicksToEdit : 1,
				autoSizeColumns : true

			});

	var outTrainForm = new Ext.form.FormPanel({
				title : '主信息',
				bodyStyle : "padding:5px 5px 0",
				labelAlign : 'right',
				// labelWidth : 70,
				autoHeight : true,
				layout : 'form',
				border : false,
				tbar : [save, {
							xtype : "tbseparator"
						}],
				items : [{
					border : false,
					layout : 'form',
					items : [{
								border : false,
								layout : 'column',
								items : [{
											columnWidth : .3,
											layout : 'form',
											border : false,
											labelWidth : 100,
											items : [person.combo, stationId]
										}, {
											columnWidth : .3,
											layout : 'form',
											labelWidth : 100,
											border : false,
											items : [sex,
													technologyTitlesTypeId]
										}, {
											columnWidth : .3,
											layout : 'form',
											labelWidth : 100,
											border : false,
											items : [dept.combo, nowStationId]
										}]
							}, trainSource, trainSite, trainContent, {
								border : false,
								layout : 'column',
								items : [{
											columnWidth : .3,
											layout : 'form',
											labelWidth :100,
											border : false,
											items : [trainStartdate]
										}, {
											columnWidth : .3,
											layout : 'form',
											labelWidth : 100,
											border : false,
											items : [trainEnddate]
										},{
											columnWidth : .3,
											layout : 'form',
											labelWidth : 100,
											border : false,
											items : [trainInsititution]
										}]
							}, rbgTrainCharacter, {
								border : false,
								layout : 'column',
								items : [{

											columnWidth : .3,
											layout : 'form',
											labelWidth : 100,
											border : false,
											items : [rbgCertificateSort.combo]
										}, {
											columnWidth : .3,
											layout : 'form',
											labelWidth : 100,
											border : false,
											items : [certificateNo]
										},{
											columnWidth : .3,
											layout : 'form',
											labelWidth : 100,
											border : false,
											items : [isReceive]
										}]
							},{
							border : false,
							layout: 'column',
							items : [{
								columnWidth : .3,
								layout : 'form',
								labelWidth : 100,
								border : false,
								items :[certificateStartdate]
							},{
								columnWidth : .7,
								layout : 'form',
								labelWidth : 100,
								border : false,
								items :[certificateEnddate]
							}]
							}]

				}]

			});

	// panel
	var detailsPanel = new Ext.Panel({
				layout : 'border',
				border : false,
				items : [{
							region : 'north',
							height : 350,
							split : true,
							items : [outTrainForm]
						}, {
							region : 'center',
							items : [grid]
						}]
			});

	return {
		panel : detailsPanel,
		rbgTrainCharacter : rbgTrainCharacter,
		setTrainId : function(_trainId,flag) {//add by wpzhu 20100702
			trainId = _trainId;
			isCopy=flag;
			store.load({
						params : {
							trainId : trainId
						}
					});
			Ext.Ajax.request({
				method : 'post',
				url : 'com/getOuttrainInfo.action',
				params : {
					trainId : trainId
				},
				success : function(res, action) {
					var emp = res.responseText;
					var o = Ext.decode(emp);
					if(flag==1)
					{
						 trainId=null;
						 person.combo.setValue("");
						 Ext.get("outtrain.workCode").dom.value = "";
						 person.combo.enable();
						 sex.enable();
						 dept.combo.setValue(o.deptName);
						 dept.combo.enable();
				
					}else
					{
				    person.combo.setValue(o.workerName);
					Ext.get("outtrain.workCode").dom.value = o.workCode;
					person.combo.disable();
					sex.setValue(o.sex);
					sex.disable();
					dept.combo.setValue(o.deptName);
					dept.combo.disable();
					}
					
					
					Ext.get("outtrain.deptCode").dom.value = o.deptCode;
                
//					rbgCertificateSort.combo.setValue(o.certificateName);
//					rbgCertificateSort.combo.setRawValue(o.certificateName);
				   rbgCertificateSort.setValue(o.certificateSort,o.certificateName);//modify by wpzhu 20100702
					Ext.get("outtrain.certificateSort").dom.value = o.certificateSort;
					
					//add by wpzhu 
				
					trainInsititution.setValue(o.trainInsititution);
					
					isReceive.setValue(o.isReceived);
					stationId.setValue(o.stationId);
					technologyTitlesTypeId.setValue(o.technologyTitlesTypeId);
					nowStationId.setValue(o.nowStationId);
					trainSource.setValue(o.trainSource);
					trainSite.setValue(o.trainSite);
					trainContent.setValue(o.trainContent);
					trainStartdate.setValue(o.trainStartdate);
					trainEnddate.setValue(o.trainEnddate);
					rbgTrainCharacter.setValue(o.trainCharacter);
//					rbgCertificateSort.setValue(o.certificateSort);
					certificateNo.setValue(o.certificateNo);
					certificateStartdate.setValue(o.certificateStartTime);
					certificateEnddate.setValue(o.certificateEndTime);
				}
			});

		},
		setFeeDetailBtn : function(b) {
			if (b) {
				Ext.getCmp("btnSave").enable();
				Ext.getCmp("btnDelete").enable();
				Ext.getCmp("btnAdd").enable();
			} else {
				Ext.getCmp("btnSave").disable();
				Ext.getCmp("btnDelete").disable();
				Ext.getCmp("btnAdd").disable();
			}
		},
		resetInputField : function() {
			outTrainForm.getForm().reset();
			person.combo.enable();
			sex.enable();
			dept.combo.enable();
			trainId = null;
			ids = [];
			store.removeAll();
			grid.getView().refresh();

		}
	}

};