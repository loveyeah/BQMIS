Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	
	// 仪器名称
	var names = new Ext.form.TextField({
				fieldLabel : "名称<font color='red'>*</font>",
				name : 'model.names',
				anchor : '95%'

			});
	// 类别
	var lb_item = Ext.data.Record.create([{
				name : 'yqyblbName'
			}, {
				name : 'yqyblbId'
			}]);
	var lbStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'productionrec/findLBList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalCount : "totalCount",
							root : "list"
						}, lb_item)
			});
	lbStore.load({
				params : {
					jdzyId : JDZY_ID
				}
			})

	var yqyblbId = new Ext.form.ComboBox({

				hiddenName : "model.yqyblbId",
				fieldLabel : "类别<font color='red'>*</font>",
				triggerAction : 'all',
				store : lbStore,
				displayField : "yqyblbName",
				valueField : "yqyblbId",
				emptyText : '请选择...',
				mode : 'local',
				readOnly : true,
				allowBlank : false,
				anchor : "90%"
			});
//	// 等级
//	var dj_item = Ext.data.Record.create([{
//				name : 'yqybdjName'
//			}, {
//				name : 'yqybdjId'
//			}]);
//	var djStore = new Ext.data.Store({
//				proxy : new Ext.data.HttpProxy({
//							url : 'productionrec/findDJList.action'
//						}),
//				reader : new Ext.data.JsonReader({
//							totalCount : "totalCount",
//							root : "list"
//						}, dj_item)
//			});
//	djStore.load({
//				params : {
//					jdzyId : JDZY_ID
//				}
//			})
//
//	var yqybdjId = new Ext.form.ComboBox({
//				id : "",
//				name : "",
//				hiddenName : "model.yqybdjId",
//				fieldLabel : "等级",
//				triggerAction : 'all',
//				store : djStore,
//				displayField : "yqybdjName",
//				valueField : "yqybdjId",
//				emptyText : '请选择...',
//				mode : 'local',
//				readOnly : true,
//				//allowBlank : false,
//				anchor : "90%"
//			});
	// 精度
//	var jd_item = Ext.data.Record.create([{
//				name : 'yqybjdName'
//			}, {
//				name : 'yqybjdId'
//			}]);
//	var jdStore = new Ext.data.Store({
//				proxy : new Ext.data.HttpProxy({
//							url : 'productionrec/findJDList.action'
//						}),
//				reader : new Ext.data.JsonReader({
//							totalCount : "totalCount",
//							root : "list"
//						}, jd_item)
//			});
//	jdStore.load({
//				params : {
//					jdzyId : JDZY_ID
//				}
//			})
//
//	var yqybjdId = new Ext.form.ComboBox({
//				id : "",
//				name : "",
//				hiddenName : "model.yqybjdId",
//				fieldLabel : "精度",
//				triggerAction : 'all',
//				store : jdStore,
//				displayField : "yqybjdName",
//				valueField : "yqybjdId",
//				emptyText : '请选择...',
//				mode : 'local',
//				readOnly : true,
////				allowBlank : false,
//				anchor : "90%"
//			});

	// 年度
	var userRange = new Ext.form.TextField({
				fieldLabel : '量程',
				name : 'model.userRange',
				anchor : '90%'

			})
	// 型号
	var sizes = new Ext.form.TextField({
				fieldLabel : '型号',
				name : 'model.sizes',
				anchor : '90%'

			})
	// 生产厂商
	var factory = new Ext.form.TextField({
				fieldLabel : '生产厂商',
				name : 'model.factory',
				anchor : '90%'

			})

	// 出厂编号
	var outFactoryNo = new Ext.form.TextField({
				fieldLabel : '出厂编号',
				name : 'model.outFactoryNo',
				anchor : '90%'

			})
	// 出厂日期
	var outFactoryDate = new Ext.form.TextField({
				format : 'Y-m-d',
				fieldLabel : '出厂日期',
				readOnly : true,
				name : 'model.outFactoryDate',
				anchor : '90%',
				listeners : {
					focus : function() {
						WdatePicker({
									dateFmt : 'yyyy-MM-dd'
								})
					}

				}
			})
	// 购买日期
	var buyDate = new Ext.form.TextField({
				format : 'Y-m-d',
				fieldLabel : '购买日期',
				name : 'model.buyDate',
				readOnly : true,
				anchor : '90%',
				listeners : {
				focus : function(){
				WdatePicker({
					dateFmt : 'yyyy-MM-dd'
				})
			}
		}
			})
	// 投用日期
	var useDate = new Ext.form.TextField({
				format : 'Y-m-d',
				fieldLabel : '投用日期',
				readOnly : true,
				name : 'model.useDate',
				anchor : '90%',
				listeners : {
				focus : function(){
				WdatePicker({
					dateFmt : 'yyyy-MM-dd'
				})
			}
		}
			})

	// 备注
	var memo = new Ext.form.TextArea({
				fieldLabel : "备注",
				name : 'model.memo',
				anchor : '90%'

			})

	// 负责人名称
	var chargerName = new Ext.form.TextField({
		fieldLabel : "负责人",

		name : 'chargerName',

		emptyText : '请选择...',

		listeners : {
			focus : function() {
				var args = {
					selectModel : 'single',
					rootNode : {
						id : "0",
						text : '合肥电厂'
					}
				}
				var url = "/power/comm/jsp/hr/workerByDept/workerByDept2.jsp";

				var rvo = window
						.showModalDialog(
								url,
								args,
								'dialogWidth:500px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;')
				if (typeof(rvo) != "undefined") {
					charger.setValue(rvo.workerCode);
					chargerName.setValue(rvo.workerName);
					depCode.setValue(rvo.deptCode);
					depName.setValue(rvo.deptName);

				}
				this.blur();
			}
		},
		readOnly : true,
//		allowBlank : false,

		anchor : '90%'

	})

	// 负责人编码
	var charger = new Ext.form.Hidden({
				name : 'model.charger'

			})
	// 部门名称
	var depName = new Ext.form.TextField({
				fieldLabel : "部门",

				name : 'depName',

				emptyText : '',
				readOnly : true,
//				allowBlank : false,

				anchor : '90%'

			})

	// 部门编码
	var depCode = new Ext.form.Hidden({
				name : 'model.depCode'

			})

	// 检验周期
	var testCycle = new Ext.form.NumberField({
				name : "model.testCycle",
				fieldLabel : "检验周期(月)<font color='red'>*</font>",
				maxLength : '3',
				maxLengthText : "检验周期最大只能3位数字",
				allowBlank : false,
				anchor : '90%'
			})

	// 使用状态

	// 是
	var ifUsedY = new Ext.form.Radio({
				boxLabel : '是',
				inputValue : 'Y',
				name : 'model.ifUsed',
				fieldLabel : '使用状态',
				checked : true
			});

	// 否
	var ifUsedN = new Ext.form.Radio({
				boxLabel : '否',
				inputValue : 'N',
				hideLabel : 'true',
				name : 'model.ifUsed'

			});

	// 仪器仪表主表id
	var regulatorId = new Ext.form.Hidden({
				name : 'model.regulatorId'

			})
	// 监督专业
	var jdzyId = new Ext.form.Hidden({
				name : 'model.jdzyId',
				value : JDZY_ID

			})

	// add by liuyi 20100511 开始 *****************************************
	// 等级
	var grade = new Ext.form.TextField({
		id : 'grade',
		fieldLabel : '等级',
		name : 'model.grade',
		maxLength : 50,
		anchor : '90%'
	})
	// 精度
	var precision = new Ext.form.TextField({
		id : 'precision',
		fieldLabel : '精度',
		name : 'model.precision',
		maxLength : 50,
		anchor : '90%'
	})
	// 校验机构
	 var checkDeptCode = new Ext.form.TextField({
    	 id : 'checkDeptCode',
    	 name : 'model.checkDeptCode',
        fieldLabel : '校验机构',
        maxLength : 20,
        anchor : '90%',
        readOnly : false
    });
	// 主要技术参数
	var mainParam = new Ext.form.TextArea({
		id : 'mainParam',
		fieldLabel : '主要技术参数',
		name : 'model.mainParam',
		maxLength : 500,
		anchor : '90%'
	})
	
	//上次校验时间
	var lastCheckTime = new Ext.form.TextField({
		id : 'lastCheckTime',
		name : 'lastCheckTime',
		fieldLabel : '上次校验时间',
		readOnly : true,
		disabled : true,
		anchor : '90%'
	})
	// 计划校验时间
	var planCheckTime = new Ext.form.TextField({
		id : 'planCheckTime',
		name : 'planCheckTime',
		fieldLabel : '计划校验时间',
		readOnly : true,
		disabled : true,
		anchor : '90%'
	})
	// 上次校验结果
	var lastCheckResult = new Ext.form.TextArea({
		id : 'lastCheckResult',
		name : 'lastCheckResult',
		fieldLabel : '上次校验结果',
		readOnly : true,
		disabled : true,
		anchor : '90%'
	})
	// 校验时间
	var checkTime = new Ext.form.TextField({
		id : 'checkTime',
		fieldLabel : '校验时间',
		readOnly : true,
		anchor : '90%',
		listeners : {
			focus : function(){
				WdatePicker({
					dateFmt : 'yyyy-MM-dd',
					onpicked : function(){
						getnextCheckDate()
					},
					onclearing : function(){
						nextCheckTime.setValue(null);
						checkResult.setValue(null);
					}
				})
			}
		}
	})
	// 下次校验时间
	var nextCheckTime = new Ext.form.TextField({
		id : 'nextCheckTime',
		fieldLabel : '下次校验时间',
		readOnly : true,
		anchor : '90%'
	})
	function getnextCheckDate() {
		if(testCycle.getValue() == null || (testCycle.getValue() == '' && testCycle.getValue() != '0'))
		{
			Ext.Msg.alert('提示','检验周期不可为空');
			checkTime.setValue(null);
			return;
		}
		if (checkTime.getValue() != null && checkTime.getValue() != "") {
			var date=new Date(Date.parse(checkTime.getValue().replace(/-/g,"/")));
			var nextCheckMonth = date.getMonth()  + 1
					+ (testCycle.getValue() - 0);
			var nextCheckYear = date.getFullYear();
			nextCheckYear += Math.floor(nextCheckMonth / 12);
			nextCheckMonth = nextCheckMonth % 12;
			nextCheckTime.setValue(nextCheckYear + "-" + (nextCheckMonth > 9 ? nextCheckMonth : ('0' + nextCheckMonth)) + '-'
					+ (date.getDate() > 9 ? date.getDate() : ('0' + date.getDate())));	
		}
	}
	// 校验结果
	var checkResult = new Ext.form.TextArea({
		id : 'checkResult',
//		name : 'checkResult',
		fieldLabel : '校验结果',
		anchor : '90%'
	})
	// add by liuyi 20100511 结束 *****************************************
	// 弹窗的表单对象
	var blockForm = new Ext.form.FormPanel({
				labelAlign : 'right',
				frame : true,
				labelWidth : 85,
				layout : 'column',
				items : [{

							bodyStyle : "padding:10px 0px 0px 0px",
							border : false,
							layout : 'form',
							columnWidth : 1,
							items : [names]
						}, {
							columnWidth : .33,
							layout : 'form',
							border : false,
							items : [yqyblbId]
						},
//						{
//							columnWidth : .5,
//							layout : 'form',
//							border : false,
//							items : [yqybdjId]
//						},
//						{
//							columnWidth : .5,
//							layout : 'form',
//							border : false,
//							items : [yqybjdId]
//						},
						// add by liuyi 20100512 *******************
						{
							columnWidth : .33,
							layout : 'form',
							border : false,
							items : [grade]
						},{
							columnWidth : .33,
							layout : 'form',
							border : false,
							items : [precision]
						},{
							columnWidth : .33,
							layout : 'form',
							border : false,
							items : [checkDeptCode]
						},
						// add by liuyi 20100512 *******************
						 {
							columnWidth : .33,
							layout : 'form',
							border : false,
							items : [userRange]
						}, {

							border : false,
							layout : 'form',
							columnWidth : 0.33,
							items : [sizes]
						}, {

							border : false,
							layout : 'form',
							columnWidth : .33,
							items : [factory]
						}, {
							columnWidth : .33,
							layout : 'form',
							border : false,
							items : [outFactoryDate]
						}, {
							columnWidth : .33,
							layout : 'form',
							border : false,
							items : [outFactoryNo]
						}, {
							columnWidth : .33,
							layout : 'form',
							border : false,
							items : [buyDate]
						}, {
							columnWidth : .33,
							layout : 'form',
							border : false,
							items : [useDate]
						}, {
							columnWidth : .33,
							layout : 'form',
							border : false,
							items : [chargerName]
						}, {
							columnWidth : .33,
							layout : 'form',
							border : false,
							items : [depName]
						}, {
							columnWidth : .33,
							layout : 'form',
							border : false,
							items : [testCycle]
						}, {
							columnWidth : .33,
							layout : 'column',
							border : false,
							items : [{
										columnWidth : .5,
										layout : 'form',
										border : false,
										items : [ifUsedY]
									}, {
										columnWidth : .5,
										layout : 'form',
										border : false,
										items : [ifUsedN]
									}]
						},{
							columnWidth : 0.5,
							layout : 'form',
							border : false,
							items : [mainParam]
						}, {

							border : false,
							layout : 'form',
							columnWidth : 0.5,
							items : [memo, charger, depCode, regulatorId,
									jdzyId]
						},{
							columnWidth : 0.5,
							layout : 'form',
							border : false,
							items : [lastCheckTime,planCheckTime,lastCheckResult]
						},{
							columnWidth : 0.5,
							layout : 'form',
							border : false,
							items : [checkTime,nextCheckTime,checkResult]
						}]

			});

	// 左边的弹出窗体

	var blockAddWindow = new Ext.Window({
				// el : 'window_win',
				title : '',
				layout : 'fit',
				width : 700,
				height : 450,
				modal : true,
				closable : true,
				border : false,
				resizable : false,
				closeAction : 'hide',
				plain : true,
				// 面板中按钮的排列方式
				buttonAlign : 'center',
				items : [blockForm],
				buttons : [{
							text : '保存',
							iconCls:'save',
							handler : function() {
								var myurl = "";
								if(names.getValue() == null 
									|| names.getValue() == "")
									{
										Ext.Msg.alert('提示信息','名称不能为空！');
										return ;
									}
								if (buyDate.getValue() != ""
								     && buyDate.getValue() < outFactoryDate
										        .getValue()) {
							        Ext.Msg.alert("提示", "购买日期不能早于出厂日期");
							        return false;
						        }
						        if (useDate.getValue() != ""
								    && useDate.getValue() < buyDate.getValue()) {
							         Ext.Msg.alert("提示", "投用日期不能早于购买日期");
							        return false;
						         }
						       if (outFactoryDate.getValue() != ""
								&& buyDate.getValue() < outFactoryDate
										.getValue()) {
							      Ext.Msg.alert("提示", "购买日期不能早于出厂日期");
							     return false;
						        }
						        if (buyDate.getValue() != ""
								&& useDate.getValue() < buyDate.getValue()) {
							     Ext.Msg.alert("提示", "投用日期不能小于购买日期");
							     return false;
						        }

								if (!blockForm.getForm().isValid()) {
									return;
								}
								if (op == "add") {
									myurl = "productionrec/saveTZ.action";
								} else if (op == 'edit') {
									myurl = "productionrec/updateTz.action";
								} else {
									Ext.Msg.alert('错误', '出现未知错误.');
								}
								blockForm.getForm().submit({
											method : 'POST',
											url : myurl,
											params : {
												lastCheckTime : checkTime.getValue(),
												nextCheckTime : nextCheckTime.getValue(),
												checkResult : checkResult.getValue()
											},
											success : function(form, action) {

												fuzzyQuery();
												blockAddWindow.hide();

											},
											faliue : function() {
												Ext.Msg.alert('错误', '出现未知错误.');
											}
										});

							}
						}, {
							text : '取消',
							iconCls:'cancer',
							handler : function() {
								blockForm.getForm().reset();
								blockAddWindow.hide();
							}
						}]
			});

	// 新建按钮
	var westbtnAdd = new Ext.Button({
				text : '新增',
				iconCls : 'add',
				handler : function() {
					blockForm.getForm().reset();

					blockAddWindow.show();
					op = 'add';
					blockAddWindow.setTitle("仪器仪表信息新增");
				}
			});

	// 修改按钮
	var westbtnedit = new Ext.Button({
				id : 'btnUpdate',
				text : '修改',
				iconCls : 'update',
				handler : function() {
					if (westgrid.selModel.hasSelection()) {

						var records = westgrid.getSelectionModel()
								.getSelections();
						var recordslen = records.length;
						if (recordslen > 1) {
							Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
						} else {
							blockAddWindow.show();
							var record = westgrid.getSelectionModel()
									.getSelected();
							blockForm.getForm().reset();
							blockForm.form.loadRecord(record);

							if(record.get('model.lastCheckDate') != null && record.get('model.lastCheckDate') != '')
								lastCheckTime.setValue(record.get('model.lastCheckDate').substring(0, 10))
							if(record.get('model.nextCheckDate') != null && record.get('model.nextCheckDate') != '')
								planCheckTime.setValue(record.get('model.nextCheckDate').substring(0, 10))
							lastCheckResult.setValue(record.get('model.checkResult'))
							if (record.get("model.outFactoryDate") != null)
								outFactoryDate.setValue(record
										.get("model.outFactoryDate").substring(
												0, 10));
							if (record.get("model.buyDate") != null)
								buyDate.setValue(record.get("model.buyDate")
										.substring(0, 10));
							if (record.get("model.useDate") != null)
								useDate.setValue(record.get("model.useDate")
										.substring(0, 10));
							op = 'edit';
							blockAddWindow.setTitle("仪器仪表信息修改");
						}
					} else {
						Ext.Msg.alert("提示", "请先选择要编辑的行!");
					}
				}
			});

	// 删除按钮
	var westbtndel = new Ext.Button({
				text : '删除',
				iconCls : 'delete',
				handler : function() {
					var records = westgrid.selModel.getSelections();
					var ids = [];
					if (records.length == 0) {
						Ext.Msg.alert("提示", "请选择要删除的记录！");
					} else {
						for (var i = 0; i < records.length; i += 1) {
							var member = records[i];
							if (member.get("model.regulatorId")) {
								ids.push(member.get("model.regulatorId"));
							} else {

								westgrids.remove(westgrids.getAt(i));
							}
						}

						Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(
										buttonobj) {

									if (buttonobj == "yes") {
										Ext.lib.Ajax
												.request(
														'POST',
														'productionrec/deleteTZ.action',
														{
															success : function(
																	action) {

																fuzzyQuery();
															},
															failure : function() {
																Ext.Msg
																		.alert(
																				'错误',
																				'删除时出现未知错误.');

															}
														}, 'ids=' + ids);
									}
								});

					}

				}
			});

	// 刷新按钮
	var westbtnref = new Ext.Button({
				text : '刷新',
				iconCls : 'reflesh',
				handler : function() {
					fuzzyQuery();
				}
			});

	var westsm = new Ext.grid.CheckboxSelectionModel();
	// 左边列表中的数据
	var datalist = new Ext.data.Record.create([

	{
				name : 'model.regulatorId'
			}, {
				name : 'model.names'
			}, {
				name : 'model.yqyblbId'
			}, {
				name : 'model.yqybdjId'
			}, {
				name : 'model.yqybjdId'
			}, {
				name : 'model.userRange'
			}, {
				name : 'model.sizes'
			}, {
				name : 'model.testCycle'
			}, {
				name : 'model.factory'
			}, {
				name : 'model.outFactoryDate'
			}, {
				name : 'model.outFactoryNo'
			}, {
				name : 'model.buyDate'
			}, {
				name : 'model.useDate'
			}, {
				name : 'model.depCode'
			}, {
				name : 'model.charger'
			}, {
				name : 'model.ifUsed'
			}, {
				name : 'model.memo'
			}, {
				name : 'model.lastCheckDate'
			}, {
				name : 'model.nextCheckDate'
			}, {
				name : 'model.jdzyId'
			}, {
				name : 'depName'

			}, {
				name : 'chargerName'
			}, {
				name : 'nameOfSort'
			}
			// add by liuyi 20100512 
			,{
				name : 'model.grade'
			},{
				name : 'model.precision'
			},{
				name : 'model.checkDeptCode'
			},{
				name : 'model.mainParam'
			},{
				name : 'model.checkResult'
			}
			]);

	var westgrids = new Ext.data.JsonStore({
				url : 'productionrec/findAccountByNames.action',
				root : 'list',
				totalCount : 'totalCount',
				fields : datalist
			});

	var query = new Ext.form.TextField({
				id : 'argFuzzy',
				fieldLabel : "模糊查询",
				hideLabel : false,
				emptyText : '仪器名称..',
				name : 'argFuzzy',
				width : 150,
				value : ''
			});
	function fuzzyQuery() {
		westgrids.baseParams = {
			fuzzy : query.getValue(),
			jdzyId : jdzyId.getValue()
		}
		westgrids.load({
					params : {
						start : 0,
						limit : 18
						
					}
				});
	};
	// 查询时Enter
	document.onkeydown = function() {
		if (event.keyCode == 13 && document.getElementById('argFuzzy')) {
			fuzzyQuery();

		}
	}
	var querybtn = new Ext.Button({
				iconCls : 'query',
				text : '查询',
				handler : function() {
					fuzzyQuery();
				}
			})
	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
				ds : westgrids,
				columns : [westsm, new Ext.grid.RowNumberer(), {
							header : "仪表名称",

							// align : "center",
							sortable : true,

							dataIndex : 'model.names'
						}, {
							header : "类别",

							sortable : false,

							dataIndex : 'nameOfSort'
						}],
				tbar : [query, querybtn, westbtnAdd, {
							xtype : "tbseparator"
						}, westbtnedit, {
							xtype : "tbseparator"
						}, westbtndel, {
							xtype : "tbseparator"
						}, westbtnref],
				sm : westsm,
				viewConfig : {
					forceFit : true
				},
				frame : true,
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : westgrids,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						}),
				border : true,
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});

	// westgrid 的事件
	westgrid.on('rowdblclick', function() {
				Ext.get("btnUpdate").dom.click();
			})

	fuzzyQuery();
	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							title : "",
							region : 'center',
							layout : 'fit',
							border : false,
							margins : '1 0 1 1',
							split : true,
							collapsible : true,

							items : [westgrid]

						}]
			});
});