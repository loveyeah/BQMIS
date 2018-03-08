Ext.onReady(function() {
	/* 模糊查询的grid */
	function choseEdit(absManlistGrid, rowIndex, columnIndex, e) {
		    str = "";
			for (var i = 0; i < atnStore.getCount(); i++) {
				if (atnStore.getAt(i).get("atnInfo.attendCode") != ""
						&& atnStore.getAt(i).get("atnInfo.attendCode") != null)
					str += "'" + atnStore.getAt(i).get("atnInfo.attendCode")
							+ "',";
			}
			str = str.substring(0, str.length - 1);
			var args = {
				selectModel : 'signal',
				notIn : str,
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
				
					var record = atnManlistGrid.getSelectionModel()
							.getSelected();
					record.set("atnInfo.attendCode", person.workerCode);
					record.set("workerName", person.workerName);
					//record.set("atnInfo.depCode", person.deptCode);
					record.set("deptName", person.deptName);	
		}
		
	};
	function getDate() {
				var d, s, t;
				d = new Date();
				s = d.getFullYear().toString(10) + "-";
				t = d.getMonth() + 1;
				s += (t > 9 ? "" : "0") + t + "-";
				t = d.getDate();
				s += (t > 9 ? "" : "0") + t + " ";
				return s;
	}
	var yeardata = '';
	var myDate = new Date();
	var myMonth = myDate.getMonth() + 1;

	myMonth = (myMonth < 10 ? "0" + myMonth : myMonth);

	for (var i = 2004; i < myDate.getFullYear() + 7; i++) {
		if (i < myDate.getFullYear() + 6)
			yeardata += '[' + i + ',' + i + '],';
		else
			yeardata += '[' + i + ',' + i + ']';
	}
	var yeardata = eval('[' + yeardata + ']');

	var query = new Ext.form.ComboBox({
				xtype : "combo",
				store : new Ext.data.SimpleStore({
							fields : ['value', 'key'],
							data : yeardata
						}),
				id : 'itemType',
				name : 'itemType',
				valueField : "key",
				displayField : "value",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'statItem.itemType',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				width : 80,
				value : myDate.getFullYear()
			});
	var querryButton = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : fuzzyQuery
			})

	function fuzzyQuery() {
		trainingStore.reload({
					params : {
						start : 0,
						limit : 18,
						argFuzzy : query.getValue()

					}
				});
	};
	var vartrainingId 
	var trainingRecord = Ext.data.Record.create([{
				name : 'baseInfo.trainingId'
			}, {
				name : 'baseInfo.depCode'
			}, {
				name : 'baseInfo.trainingSubject'
			}, {
				name : 'baseInfo.trainingTime'
			}, {
				name : 'baseInfo.trainingSpeaker'
			}, {
				name : 'baseInfo.completion'
			}, {
				name : 'baseInfo.content'
			}, {
				name : 'baseInfo.memo'
			}, {
				name : 'speakerName'
			}, {
				name : 'deptName'
			}, {
				name : 'trainingTime'
			}]

	);

	var trainingStore = new Ext.data.JsonStore({
				url : 'security/getSpJSafetrainingList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : trainingRecord
			})

	trainingStore.load({
				params : {
					start : 0,
					limit : 18,
					argFuzzy : query.getValue()
				}

			})

	var trainingGrid = new Ext.grid.GridPanel({
				store : trainingStore,
				layout : 'fit',

				border : false,
				autoScroll : false,
				viewConfig : {
					forceFit : true
				},
				columns : [new Ext.grid.RowNumberer(), {
							header : '部门',
							align : 'center',
							width : 120,
							sortable : false,
							dataIndex : 'deptName'
						}, {
							header : '时间',
							align : 'center',
							width : 90,
							sortable : false,
							dataIndex : 'trainingTime'
						}, {
							header : '主题',
							align : 'center',
							width : 80,
							sortable : false,
							dataIndex : 'baseInfo.trainingSubject'
						}],
				bbar : 
						new Ext.PagingToolbar({
							pageSize : 18,
							store : trainingStore,
							displayInfo : true,
							displayMsg : "{2}条记录",
							emptyMsg : "没有记录"
						}),
				tbar : ['选择年份：   '

				, query, '-', querryButton]

			});
		trainingGrid.on('rowclick', dblclick);
			function dblclick(){
			var rec = trainingGrid.getSelectionModel().getSelected();
			vartrainingId = rec.get("baseInfo.trainingId");
			deptName.setValue(rec.get("deptName"));
			depCode.setValue(rec.get('baseInfo.depCode'));
			trainingSubject.setValue(rec.get("baseInfo.trainingSubject"));
			trainingTime.setValue(rec.get("trainingTime"));
			speakerName.setValue(rec.get("speakerName"));
			content.setValue(rec.get("baseInfo.content"));
			memo.setValue(rec.get('baseInfo.memo'));
			completion.setValue(rec.get('baseInfo.completion'));
			trainingId.setValue(rec.get('baseInfo.trainingId'));
			trainingSpeaker.setValue(rec.get('baseInfo.trainingSpeaker'))
			trainingSave.setText("修改");
		}
	
	function selectDept() {
		var args = {
			selectModel : 'single',
			rootNode : {
				id : '0',
				text : '合肥电厂'
			},
			onlyLeaf : false
		};
		var dept = window
				.showModalDialog(
						'../../../comm/jsp/hr/dept/dept.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(dept) != "undefined") {
			depCode.setValue(dept.codes);
			deptName.setValue(dept.names);
		}
	}
	var deptName = new Ext.form.ComboBox({
				id : 'deptName',
				name : 'deptName',
				fieldLabel : "部门",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				anchor : '90%',
				onTriggerClick : function() {
					selectDept();
				}
			});
	var depCode = new Ext.form.Hidden({
				id : 'baseInfo.depCode',
				name : 'baseInfo.depCode'
			});

	var trainingTime = new Ext.form.TextField({
				
				id : 'trainingTime',
				fieldLabel : "培训时间",
				name : 'trainingTime',
				style : 'cursor:pointer',
				readOnly : true,
				anchor : '90%',
				allowBlank : true,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : true
								});
						this.blur();
					}
				},
				value : getDate()
			
			});

	var trainingSubject = new Ext.form.TextField({
				id : '',
				fieldLabel : '主题',
				allowBlank : false,
				name : 'baseInfo.trainingSubject',
				anchor : '95%'
			})

	var speakerName = new Ext.form.TriggerField({
				id : 'speakerName',
				name : 'speakerName',
				fieldLabel : "主持人",
				mode : 'local',
				readOnly : true,
				typeAhead : true,
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				anchor : '90%',
				onTriggerClick : function() {
					selectPersonWin();
				}
		});
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
			
				speakerName.setValue(person.workerName);
				trainingSpeaker.setValue(person.workerCode);
			
		}
	}
	var trainingSpeaker = new Ext.form.Hidden({
				id : 'trainingSpeaker',
				name : 'baseInfo.trainingSpeaker'

			})

	var trainingId = new Ext.form.Hidden({
				id : 'trainingId',
				name : 'baseInfo.trainingId'

			})

	var completion = new Ext.form.TextField({
				id : 'completion',
				name : 'baseInfo.completion',
				fieldLabel : '完成情况',
				anchor : "90%"

			});

	var content = new Ext.form.TextArea({
				id : 'content',
				name : 'baseInfo.content',
				fieldLabel : '内容',
				height : 120,
				anchor : "95%"

			})
	var memo = new Ext.form.TextArea({
				id : 'memo',
				height : 140,
				name : 'baseInfo.memo',
				fieldLabel : '说明',
				anchor : "95%"

			})

	function trainingAdd() {
		trainingFormPanle.getForm().reset();
		vartrainingId = "";
		trainingSave.setText("保存");
	}

	function trainingDel() {
		if(vartrainingId == null || vartrainingId == ""){
					Ext.Msg.alert("提示","请在左边列表中选择一条记录!");
					return false;
		}
		Ext.Msg.confirm('提示', '删除将不能恢复，确定删除吗？', function(button) {
					if (button == 'yes') {
						Ext.Ajax.request({
									waitMsg : '删除中,请稍后...',
									url : 'security/deleteSpJSafetraining.action',
									params : {
										trainingId : vartrainingId
									},
									success : function(response, options) {
										trainingFormPanle.getForm().reset();
		                                vartrainingId = "";
		                                trainingSave.setText("保存");
										trainingStore.load({
											params : {
												start : 0,
												limit : 18
											}
										});
									},
									failure : function() {
										Ext.Msg.alert('提示信息', '服务器错误,请稍候重试!')
									}
								});

					} else
						return;
				})

	}

	function trainingSave() {
		if (trainingFormPanle.getForm().isValid())
			trainingFormPanle.getForm().submit({
						waitMsg : '保存中,请稍后...',
						url : "security/saveSpJSafetraining.action",
						params : {
							trainingTime : trainingTime.getValue()
						},
						success : function(form, action) {
							query.setValue(trainingTime.getValue().substring(0,4));
							trainingStore.load({
								params : {
									argFuzzy : trainingTime.getValue().substring(0,4),
									start : 0,
									limit : 18
								}
							});
							trainingFormPanle.getForm().reset();
							vartrainingId == "";
							 trainingSave.setText("保存");
							var o = eval('(' + action.response.responseText
									+ ')');
							Ext.MessageBox.alert('提示', o.msg);
						},
						failure : function(form, action) {
							var o = eval('(' + action.response.responseText
									+ ')');
							Ext.MessageBox.alert('错误', o.msg);
						}
					});

	}
	function attendBtn() {
		if (trainingId.getValue() != null
				&& trainingId.getValue() != "") {
				atnStore.removeAll();
				atnManlistWindow.show();
				atnStore.load({
					params : {
							trainingId : trainingId.getValue()
					}
				});
		} else
				Ext.MessageBox.alert('错误', '请先选择主记录！');	
	}

	var trainingAdd = new Ext.Button({
				text : '增加',
				iconCls : 'add',
				handler : trainingAdd

			})

	var trainingDel = new Ext.Button({
				text : '删除',
				iconCls : 'delete',
				handler : trainingDel

			})
	var trainingSave = new Ext.Button({
				text : '保存',
				iconCls : 'save',
				handler : trainingSave

			})
	var eastattendBtn = new Ext.Button({
				text : '参与人员',
				iconCls : 'save',
				handler : attendBtn
			})

	
	var formContent = new Ext.form.FieldSet({
		border : false,
		layout : 'column',
		items : [{
					columnWidth : 0.5,
					layout : 'form',
					border : false,
					labelWidth : 50,
					items : [deptName, speakerName]
				}, {
					columnWidth : 0.5,
					layout : 'form',
					labelWidth : 70,
					border : false,
					items : [trainingTime, completion]
				}, {
					border : false,
					layout : 'form',
					columnWidth : 1,
					labelWidth : 50,
					items : [trainingSubject]
				}, {
					border : false,
					layout : 'form',
					columnWidth : 1,
					labelWidth : 50,
					items : [content]
				}, {
					border : false,
					layout : 'form',
					columnWidth : 1,
					labelWidth : 50,
					items : [memo, trainingSpeaker, trainingId,depCode]
				}]
	});
	// 右侧Form
	var trainingFormPanle = new Ext.form.FormPanel({
				bodyStyle : "padding:5px 5px 0",
				labelAlign : 'right',
				labelWidth : 50,
				autoHeight : false,
				region : 'center',
				border : false,
				tbar : [trainingAdd, {
							xtype : "tbseparator"
						}, trainingDel, {
							xtype : "tbseparator"
						}, trainingSave, {
							xtype : "tbseparator"
						}, eastattendBtn],
				items : [formContent]
			});

	
	// 参加人员
	var atnManName = {
		header : "人员",
		sortable : false,
		dataIndex : 'workerName',
		width : 50,
		editor : new Ext.form.TextField({
					readOnly : true
				})
	};

	var atnMan = new Ext.form.TextField({
				dataIndex : 'atnInfo.attendCode',
				hidden : true
			});

	var atnTrainingId = new Ext.form.TextField({
				dataIndex : 'atnInfo.trainingId',
				hidden : true
			});

	var atnManDepName = {
		header : "部门",
		width : 100,
		sortable : false,
		dataIndex : 'deptName',
		width : 135
	};

	//var atnManDep = new Ext.form.TextField({
	//			dataIndex : 'atnInfo.depCode',
	//			hidden : true
	//		});

	function addAtnRecords() {
		var count = atnStore.getCount();
		var currentIndex = count;
		var o = new atnStorelist({
					'atnInfo.attendId' : '',
					'atnInfo.trainingId' : trainingId.getValue(),
					'atnInfo.attendCode' : '',
					'workerName' : '',
					'deptName' : ''
				});

		atnManlistGrid.stopEditing();
		atnStore.insert(currentIndex, o);
		atnSm.selectRow(currentIndex);
		atnManlistGrid.startEditing(currentIndex, 2);
		
	};
	var atnBtnAdd = new Ext.Button({
				text : '增加',
				iconCls : 'add',
				handler : function() {
					addAtnRecords();
				}
			});

	var ids = new Array();
	var atnBtnDel = new Ext.Button({
				text : '删除',
				iconCls : 'delete',
				handler : function() {
					atnManlistGrid.stopEditing();
					var atnSm = atnManlistGrid.getSelectionModel();
					var selected = atnSm.getSelections();
					if (selected.length == 0) {
						Ext.Msg.alert("提示", "请选择要删除的记录！");
					} else {
						for (var i = 0; i < selected.length; i += 1) {
							var member = selected[i];
							if (member.get("atnInfo.attendId") != null) {
								ids.push(member.get("atnInfo.attendId"));
							}
							atnManlistGrid.getStore().remove(member);
							atnManlistGrid.getStore().getModifiedRecords()
									.remove(member);
						}
					}
				}
			});
	var atnBtnSav = new Ext.Button({
		text : '保存',
		iconCls : 'save',
		handler : function() {
			atnManlistGrid.stopEditing();
			var modifyRec = atnManlistGrid.getStore().getModifiedRecords();
			var doIf;
			if (modifyRec.length > 0 || ids.length > 0) {
				Ext.Msg.confirm('提示信息', '是否确定修改？', function(button, text) {
					if (button == 'yes') {
						var updateData = new Array();
						for (var i = 0; i < modifyRec.length; i++) {
							updateData.push(modifyRec[i].data);
						}

						Ext.Ajax.request({
							url : 'security/saveSpJSafetrainingAttend.action',
							method : 'post',
							params : {
								isUpdate : Ext.util.JSON.encode(updateData),
								isDelete : ids.join(","),
								trainingId : trainingGrid.getSelectionModel()
										.getSelected()
										.get("baseInfo.trainingId")
							},
							success : function(result, request) {
								var o = eval('(' + result.responseText + ')');
								Ext.MessageBox.alert('提示信息', o.msg);
								atnStore.rejectChanges();
								ids = [];
								atnStore.load({
											params : {
												trainingId : trainingGrid
														.getSelectionModel()
														.getSelected()
														.get("baseInfo.trainingId")
											}
										});
							},
							failure : function(result, request) {
								Ext.MessageBox.alert('提示信息', '未知错误！')
							}
						})
					}
				});
			} else {
				Ext.MessageBox.alert('提示信息', '没有做任何修改！')
			}
		}
	});
	var atnSm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});

	var atnStorelist = new Ext.data.Record.create([atnSm, {
				name : 'atnInfo.attendId'
			}, {
				name : 'atnInfo.trainingId'
			}, {
				name : 'atnInfo.attendCode'
			}, {
				name : 'deptName'
			}, {
				name : 'workerName'
			}]);

	var atnStore = new Ext.data.JsonStore({
				url : 'security/getSpJSafetrainingAttendList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : atnStorelist
			});
	// 参加人员Grid
	var atnManlistGrid = new Ext.grid.EditorGridPanel({
				store : atnStore,
				columns : [new Ext.grid.RowNumberer(), atnManName, atnMan,
						atnTrainingId, atnManDepName],
				viewConfig : {
					forceFit : true
				},
				tbar : [atnBtnAdd, atnBtnDel, atnBtnSav],
				sm : atnSm,
				frame : true,
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});

	atnManlistGrid.on('celldblclick', choseEdit);

	var atnManlistWindow = new Ext.Window({
				title : '参加人员名单',
				layout : 'fit',
				width : 400,
				height : 450,
				closable : true,
				border : false,
				resizable : false,
				closeAction : 'hide',
				modal : true,
				plain : true,
				buttonAlign : 'center',
				items : [atnManlistGrid]
			});

	// ↑↑参加人员
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							title : '培训信息',
							region : "center",
							split : true,
							collapsible : false,
							titleCollapse : true,
							margins : '1',
							layoutConfig : {
								animate : true
							},
							layout : 'fit',
							items : [trainingFormPanle]
						}, {
							title : '培训列表',
							region : 'west',
							split : true,
							//collapsible : true,
							titleCollapse : true,
							margins : '1',
							width : 320,
							layoutConfig : {
								animate : true
							},
							layout : 'fit',
							items : [trainingGrid]
						}]
			});

})
