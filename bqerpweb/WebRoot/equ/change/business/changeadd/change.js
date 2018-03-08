Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var specialCode;
	var oldFileName;
	var ecId;
	var actionId;
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'change.equChangeId'
	}, {
		name : 'change.equChangeNo'
	}, {
		name : 'change.changeTitle'
	}, {
		name : 'specialityName'
	}, {
		name : 'sourceName'
	}, {
		name : 'change.changeType'
	}, {
		name : 'changePlanDate'
	}, {
		name : 'change.wfState'
	}, {
		name : 'change.workFlowNo'
	}, {
		name : 'deptCode'
	}, {
		name : 'deptName'
	}, {
		name : 'change.annex'
	}]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url : 'equchange/findChangeList.action'
			}

	);

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"

	}, MyRecord);

	var store = new Ext.data.Store({

		proxy : dataProxy,

		reader : theReader

	});

	var changeno = new Ext.form.TextField({
		id : "changeno",
		name : "changeno"
	});

	var changetitle = new Ext.form.TextField({
		id : "changetitle",
		name : "changetitle"
	});

	var sm = new Ext.grid.CheckboxSelectionModel();

	var grid = new Ext.grid.GridPanel({
		region : "center",
		store : store,
		columns : [sm, {

			header : "ID",
			sortable : true,
			dataIndex : 'change.equChangeId',
			hidden : true
		},

		{
			header : "异动编号",
			width : 100,
			sortable : true,
			dataIndex : 'change.equChangeNo'
		}, {
			header : "异动简题",
			width : 300,
			sortable : true,
			dataIndex : 'change.changeTitle'
		}, {
			header : "申请专业",
			width : 100,
			sortable : true,
			dataIndex : 'specialityName'
		}, {
			header : "来源",
			width : 100,
			sortable : true,
			dataIndex : 'sourceName'
		}, {
			header : "变更类型",
			width : 100,
			sortable : true,
			dataIndex : 'change.changeType'
		}, {
			header : "计划开始时间",
			width : 200,
			sortable : true,
			dataIndex : 'changePlanDate'
		}, {
			header : "当前状态",
			width : 100,
			sortable : true,
			dataIndex : 'change.wfState',
			renderer : function(value) {
				if (value == "0")
					return "已登记";
				else if (value == "1" || value == "2")
					return "审批中";
				else if (value == "3")
					return "已退回";
				else if (value == "4")
					return "审批结束";
			}
		}],
		tbar : ['编号：', changeno, '简题', changetitle, {
			text : "查询",
			iconCls : 'query',
			handler : queryRecord
		}, {
			text : "新增",
			iconCls : 'add',
			handler : addRecord
		}, {
			text : "修改",
			iconCls : 'update',
			handler : updateRecord
		}, {
			text : "删除",
			iconCls : 'delete',
			handler : deleteRecord
		}, {
			text : "上报",
			iconCls : 'upcommit',
			handler : reportFun
		}],
		sm : sm,
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});

	grid.on('rowdblclick', updateRecord);

	var applyDate;
	var panelone;
	var newImage;
	var backThing;
	var oldImage;
	var frontThing;
	var changePlanDate;
	var equNewCode;
	var equOldCode;
	var equName;
	var assetnum;
	var changeReason;
	var changeType;
	var changeTitle;
	var deptCombox;
	var deptCodeHidden;
	var specialityStore;
	var specialCode;
	var specialCode;
	var sourceCode;
	var sourceStore;
	var equChangeNo;
	var equChangeId;

	function reportFun() {
		if (grid.selModel.hasSelection()) {
			var records = grid.selModel.getSelections();
			var record = grid.getSelectionModel().getSelected();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("提示", "请选择一条数据进行上报！");
				return;
			}
			var url = "sign.jsp";
			var args = new Object();
			args.entryId = record.get("change.workFlowNo");
			args.equChangeId = record.get("change.equChangeId");
			args.workflowType = "bqEquChange";
			args.actionId = actionId;

			var obj = window.showModalDialog(url, args,
					'status:no;dialogWidth=770px;dialogHeight=550px');
			if (obj) {
				queryRecord();
			}
		} else {

			Ext.Msg.alert("提示", "请选择一条数据进行上报！");
			return;
		}
	}

	// ----------增加/修改-----

	function createupdatepanel(mytitle, myid) {
		var wd = 180;

		equChangeId = {
			id : "equChangeId",
			xtype : "textfield",
			fieldLabel : 'ID',
			value : '自动生成',
			anchor : "90%",
			readOnly : true,
			name : 'equChangeId'

		}

		equChangeNo = new Ext.form.TextField({
			id : "equChangeNo",
			xtype : "textfield",
			fieldLabel : '编号',
			anchor : "90%",
			readOnly : true,
			name : 'change.equChangeNo'

		});

		sourceStore = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
				url : 'equchange/findChangeSourceList.action'
			}),
			reader : new Ext.data.JsonReader({
				root : 'list'
			}, [{
				name : 'sourceCode'
			}, {
				name : 'sourceName'
			}])
		});
		sourceStore.load();
		sourceCode = {
			xtype : 'combo',
			id : 'sourceCode',
			fieldLabel : '来源',
			store : sourceStore,
			valueField : "sourceCode",
			displayField : "sourceName",
			mode : 'remote',
			typeAhead : true,
			forceSelection : true,
			hiddenName : 'change.sourceCode',
			editable : false,
			triggerAction : 'all',
			anchor : "90%",
			blankText : '请选择',
			emptyText : '请选择',
			selectOnFocus : true,
			allowBlank : false
		};

		specialCode = {
			xtype : "textfield",
			fieldLabel : '专业',
			name : 'change.specialCode',
			anchor : "90%"
		}

		specialityStore = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
				url : 'equchange/findSpecialList.action'
			}),
			reader : new Ext.data.JsonReader({
				id : "unit"
			}, [{
				name : 'specialityCode'
			}, {
				name : 'specialityName'
			}])
		});
		specialityStore.load();

		specialCode = new Ext.form.ComboBox({
			id : "specialCode",
			name : 'specialCode',
			hiddenName : 'change.specialCode',
			store : specialityStore,
			fieldLabel : '所属专业',
			triggerAction : 'all',
			valueField : "specialityCode",
			displayField : "specialityName",
			readOnly : true,
			mode : 'local',
			anchor : "90%",
			listeners : {
				select : function() {
					var special = specialCode.getValue();
					Ext.lib.Ajax.request('POST',
							'equchange/getEquChangeNo.action?special='
									+ special, {
								success : function(action) {
									equChangeNo.setValue(action.responseText);
								}
							});

				}
			}
		})

		deptCodeHidden = new Ext.form.Hidden({
			id : 'deptCode',
			name : 'change.deptCode'

		})
		deptCombox = new Ext.form.TriggerField({
			id : 'deptName',
			fieldLabel : '部门',
			allowBlank : false,
			name : 'deptName',
			anchor : "95%",
			onTriggerClick : function() {
				var args = {
					selectModel : 'multiple',
					rootNode : {
						id : "0",
						text : '灞桥热电厂'
					}
				}
				var url = "/power/comm/jsp/hr/dept/dept.jsp";
				var rvo = window
						.showModalDialog(
								url,
								args,
								'dialogWidth:250px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
				if (typeof(rvo) != "undefined") {
					Ext.getCmp('deptCode').setValue(rvo.codes);
					Ext.getCmp('deptName').setValue(rvo.names);
				}
			}
		});

		changeTitle = {
			id : "changeTitle",
			xtype : "textarea",
			fieldLabel : '异动简题',
			allowBlank : false,
			name : 'change.changeTitle',
			anchor : "95%"
		}

		changeType = new Ext.form.ComboBox({
			id : 'changeType',
			fieldLabel : '变更类型',
			store : new Ext.data.SimpleStore({
				fields : ["changeType", "displayText"],
				data : [['1', '安装'], ['2', '改装'], ['3', '拆除'], ['4', '改造']]
			}),
			valueField : "changeType",
			displayField : "displayText",
			mode : 'local',
			forceSelection : true,
			blankText : '请选择变更类型',
			emptyText : '请选择变更类型',
			hiddenName : 'change.changeType',
			value : '1',
			editable : false,
			typeAhead : true,
			triggerAction : 'all',
			selectOnFocus : true,
			allowBlank : false,
			name : 'change.changeType',
			anchor : "90%"
		});

		changeReason = {
			id : "changeReason",
			xtype : "textarea",
			fieldLabel : '异动原因',
			name : 'change.changeReason',
			anchor : "95%"

		}

		assetnum = new Ext.form.TextField({
			id : "assetnum",
			xtype : "textfield",
			fieldLabel : '设备资产编号',
			anchor : "90%",
			name : 'change.assetnum'

		});

		equName = new Ext.form.TextField({
			id : "equName",
			xtype : "textfield",
			fieldLabel : '设备资产名称',
			anchor : "90%",
			name : 'change.equName'

		});

		equOldCode = {
			fieldLabel : '原功能码',
			name : 'equOldCode',
			xtype : 'combo',
			id : 'equOldCode',
			store : new Ext.data.SimpleStore({
				fields : ['id', 'name'],
				data : [[]]
			}),
			mode : 'remote',
			hiddenName : 'change.equOldCode',
			allowBlank : false,
			anchor : "90%",
			triggerAction : 'all',
			onTriggerClick : function() {
				var url = "../../../base/business/kksselect/selectAttribute.jsp";
				var equ = window.showModalDialog(url, '',
						'dialogWidth=400px;dialogHeight=400px;status=no');
				if (typeof(equ) != "undefined") {
					Ext.getCmp('equOldCode').setValue(equ.code);
					Ext.form.ComboBox.superclass.setValue.call(Ext
							.getCmp('equOldCode'), equ.name);
				}
			}
		};

		equNewCode = {
			fieldLabel : '新功能码',
			name : 'equNewCode',
			xtype : 'combo',
			id : 'equNewCode',
			store : new Ext.data.SimpleStore({
				fields : ['id', 'name'],
				data : [[]]
			}),
			mode : 'remote',
			hiddenName : 'change.equNewCode',
			allowBlank : false,
			// editable : false,
			anchor : "90%",
			triggerAction : 'all',
			onTriggerClick : function() {
				var url = "../../../base/business/kksselect/selectAttribute.jsp";
				var equ = window.showModalDialog(url, '',
						'dialogWidth=400px;dialogHeight=400px;status=no');
				if (typeof(equ) != "undefined") {
					Ext.getCmp('equNewCode').setValue(equ.code);
					Ext.form.ComboBox.superclass.setValue.call(Ext
							.getCmp('equNewCode'), equ.name);
				}
			}
		};

		var mydate = new Date();
		changePlanDate = {
			id : "changePlanDate",
			xtype : "datefield",
			fieldLabel : '计划时间',
			name : 'change.changePlanDate',
			format : 'Y-m-d h:i:s',
			value : mydate,
			anchor : "90%"
		}

		frontThing = new Ext.form.TextArea({
			id : "frontThing",
			fieldLabel : '异动前情况',
			anchor : "95%",
			name : 'change.frontThing'
		});

		/*
		 * oldImage = { id : "oldImage", xtype : "textfield", fieldLabel : '图片',
		 * hideLabel : true, anchor : "80%", height : 140, autoCreate : { tag :
		 * 'input', type : 'image', src : '/power/comm/images/UnknowBody.jpg',
		 * style :
		 * 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);',
		 * name : 'oldImage' } };
		 */

		backThing = new Ext.form.TextArea({
			id : "backThing",
			fieldLabel : '异动后情况',
			anchor : "95%",
			name : 'change.backThing'
		});

		newImage = {
			id : "newImage",
			xtype : "textfield",
			fieldLabel : '图片',
			hideLabel : true,
			anchor : "80%",
			height : 140,
			autoCreate : {
				tag : 'input',
				type : 'image',
				src : '/power/comm/images/UnknowBody.jpg',
				style : 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);',
				name : 'newImage'
			}

		};

		applyDate = {
			id : "applyDate",
			xtype : "datefield",
			fieldLabel : '申请日期',
			name : 'change.applyDate',
			format : 'Y-m-d h:i:s',
			value : mydate,
			anchor : "90%"
		}

		panelone = new Ext.FormPanel({
			frame : true,
			id : 'tab1',
			fileUpload : true,
			labelAlign : 'right',
			title : '增加/修改异动来源类型',
			items : [{
				layout : 'column',
				items : [{
					columnWidth : 0.5,
					layout : 'form',
					items : [equChangeId, sourceCode]
				}, {
					columnWidth : 0.5,
					layout : 'form',
					items : [equChangeNo, specialCode]
				}]
			}, {
				layout : 'column',
				items : [{
					columnWidth : 1,
					layout : 'form',
					items : [deptCodeHidden, deptCombox]
				}]
			}, {
				layout : 'column',
				items : [{
					columnWidth : 1,
					layout : 'form',
					items : [changeTitle]
				}]
			}, {
				layout : 'column',
				items : [{
					columnWidth : 1,
					layout : 'form',
					items : [changeReason]
				}]
			}, {
				layout : 'column',
				items : [{
					columnWidth : 0.5,
					layout : 'form',
					items : [changeType, assetnum, equOldCode]
				}, {
					columnWidth : 0.5,
					layout : 'form',
					items : [changePlanDate, equName, equNewCode]
				}]
			}, {
				layout : 'column',
				items : [{
					columnWidth : 0.5,
					layout : 'form',
					items : [{
						columnWidth : 0.3,
						layout : "form",
						border : false,
						items : [{
							xtype : "textfield",
							fieldLabel : "申请人",
							name : "applyName",
							readOnly : true,
							value : document.getElementById("workName").value,
							anchor : "90%"
						}, {
							xtype : "hidden",
							name : "change.applyMan",
							value : document.getElementById('workCode').value,
							anchor : "90%"
						}]
					}]
				}, {
					columnWidth : 0.5,
					layout : 'form',
					items : [applyDate]
				}]
			}]
		});

		oldFileName = new Ext.form.TextField({
			inputType : "file",
			fieldLabel : '异动相关附件',
			anchor : "90%",
			name : 'annex'
		});
		
		var checkAnnex = new Ext.Button({
			text:'查看',
			handler:function(){
				var record = grid.getSelectionModel().getSelected();
				window.open(record.get("change.annex"));
			}
		})		
		
		
		// oldFileName = new Ext.form.TextField({
		// id : "oldFileName",
		// xtype : "textfield",
		// inputType : "file",
		// fieldLabel : '异动相关附件',
		// anchor : "90%",
		// name : 'oldFileName'
		// });
		// newFileName = new Ext.form.TextField({
		// id : "newFileName",
		// inputType : "file",
		// fieldLabel : '异动后附图',
		// name : 'newFileName',
		// anchor : "90%"
		// })
		var paneltwo = new Ext.FormPanel({
			frame : true,
			id : 'tab2',
			labelAlign : 'right',
			fileUpload : true,
			title : '异动情况',
			items : [{
				layout : 'column',
				items : [{
					columnWidth : 1,
					layout : 'form',
					items : [frontThing]
				}]
			}, {
				layout : 'column',
				items : [{
					columnWidth : 1,
					layout : 'form',
					items : [backThing]
				}]
			}, {
				layout : 'column',
				items : [{
					columnWidth :0.8,
					layout : 'form',
					items : [oldFileName]
				},{
					columnWidth : 0.2,
					layout : 'form',
					items : [checkAnnex]
				}]
			}]
		});

		var tabpanel = new Ext.TabPanel({
			title : 'mytab',
			activeTab : 0,
			autoScroll : true,
			layoutOnTabChange : true,
			items : [panelone, paneltwo]

		});

		/*
		 * tabpanel.getItem('tab2').on('render', function(f) {
		 * tabpanel.getItem('tab2').form.findField('oldFileName').on('render',
		 * function() { Ext.get('oldFileName').on('change', function(field,
		 * newValue, oldValue) { var url = Ext.get('oldFileName').dom.value; var
		 * image = Ext.get('oldImage').dom; if (Ext.isIE7) { image.src =
		 * Ext.BLANK_IMAGE_URL;// 覆盖原来的图片
		 * image.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src =
		 * url; } else { image.src = url; } }); });
		 */
		/*
		 * tabpanel.getItem('tab2').form.findField('newFileName').on('render',
		 * function() { Ext.get('newFileName').on('change', function(field,
		 * newValue, oldValue) {
		 * 
		 * var url = Ext.get('newFileName').dom.value; var image =
		 * Ext.get('newImage').dom; if (Ext.isIE7) { image.src =
		 * Ext.BLANK_IMAGE_URL;// 覆盖原来的图片
		 * image.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src =
		 * url; } else { image.src = url; }
		 * 
		 * }); });
		 *  })
		 */

		return tabpanel;
	}

	function createaddwin(tabpanel, op) {

		var win = new Ext.Window({
			width : 600,
			height : 450,
			buttonAlign : "center",
			items : [tabpanel],
			layout : 'fit',
			modal : true,
			buttons : [{
				text : '保存',
				iconCls : 'save',
				handler : function() {

					var myurl = "";
					var tab2url = "";
					var record = grid.getSelectionModel().getSelected();

					if (op == "add") {
						myurl = "equchange/updateChange.action";

					} else {
						myurl = "equchange/updateChange.action";
						ecId = record.get("change.equChangeId");
					}

					tab2url = "equchange/saveThingAndImage.action";
					if ((oldFileName.getValue() != null
							&& oldFileName.getValue() != '' && typeof(oldFileName
							.getValue()) != 'undefined')
							|| (frontThing.getValue() != null
									&& frontThing.getValue() != '' && frontThing
									.getValue() != 'null')
							|| (backThing.getValue() != null
									&& backThing.getValue() != '' && backThing
									.getValue() != 'null')) {

						tabpanel.setActiveTab(1);

						tabpanel.getItem('tab2').form.submit({
							method : 'POST',
							url : tab2url,
							params : {
								changeId : ecId,
								front : oldFileName.getValue(),
								frontThing : frontThing.getValue(),
								backThing : backThing.getValue()
							},
							success : function(form, action) {
								var o = eval("(" + action.response.responseText
										+ ")");
								if (o.changeId != 'none') {
									ecId = o.changeId;
									tabpanel.setActiveTab(0);
									tabpanel.getItem('tab1').form.submit({
										method : 'POST',
										url : myurl,
										params : {
											changeId : ecId
										},
										success : function(form, action) {
											// var o = eval("(" +
											// action.response.responseText +
											// ")");
											// if(o.id!=""&&typeof(o.id)!='undefined'){
											// tab2url="equchange/saveThingAndImage.action";
											// }

											if (op == "add") {
												Ext.Msg.alert("注意", "增加成功");
											} else {
												Ext.Msg.alert("注意", "修改成功");
											}
											ecId = "";
											grid.store.reload();
											win.close();

										},
										faliue : function() {
											Ext.Msg.alert('错误', '出现未知错误.');
										}
									});
								}
							},
							faliue : function() {
								Ext.Msg.alert('错误', '出现未知错误.');
							}
						});
					} else {
						myurl = "equchange/addChange.action";
					}
					
					tabpanel.setActiveTab(0);
					tabpanel.getItem('tab1').form.submit({
						method : 'POST',
						url : myurl,
						params : {
							changeId : ecId
						},
						success : function(form, action) {
							// var o = eval("(" + action.response.responseText +
							// ")");
							// if(o.id!=""&&typeof(o.id)!='undefined'){
							// tab2url="equchange/saveThingAndImage.action";
							// }

							if (op == "add") {
								Ext.Msg.alert("注意", "增加成功");
							} else {
								Ext.Msg.alert("注意", "修改成功");
							}
							ecId = "";
							grid.store.reload();
							win.close();

						},
						faliue : function() {
							Ext.Msg.alert('错误', '出现未知错误.');
						}
					});
				}
			}, {
				text : '取消',
				iconCls : 'cancer',
				handler : function() {
					win.close();
				}
			}]

		});

		return win;

	}

	function queryRecord() {
		store.baseParams = {
			flag : "register",
			changeNo : changeno.getValue(),
			changeTitle : changetitle.getValue()
		};
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}

	function addRecord() {
		if (!tabpanel) {
			var tabpanel = createupdatepanel("增加", '自动生成');

		}
		if (!win) {
			var win = createaddwin(tabpanel, "add");
		}
		win.show();

		tabpanel.getItem('tab1').setTitle("异动基本信息");

	}

	function updateRecord() {

		if (grid.selModel.hasSelection()) {

			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				var record = grid.getSelectionModel().getSelected();
				if (!tabpanel) {
					var tabpanel = createupdatepanel("修改", record
							.get("equChangeId"));

				}
				if (!win) {
					var win = createaddwin(tabpanel, "update");
				}

				win.show();
				tabpanel.setActiveTab(0);
				tabpanel.getItem('tab1').getForm().load({
					url : "equchange/findChangeModel.action",
					params : {
						id : record.get("change.equChangeId")
					},
					success : function(form, action) {
						// 禁止修改specialCode所属专业
						var o = eval("(" + action.response.responseText + ")");
						if (o.data.equOldCode != null) {
							Ext.getCmp('equOldCode')
									.setValue(o.data.equOldCode);
							Ext.form.ComboBox.superclass.setValue.call(Ext
									.getCmp('equOldCode'), o.oldname);
						}
						if (o.data.equNewCode != null) {
							Ext.getCmp('equNewCode')
									.setValue(o.data.equNewCode);
							Ext.form.ComboBox.superclass.setValue.call(Ext
									.getCmp('equNewCode'), o.newname);
						}
						if (o.data.deptCode != null) {
							Ext.getCmp('deptCode').setValue(o.data.deptCode);
							Ext.getCmp('deptName').setValue(o.deptName);
						}
						if (o.specialityName != null) {
							Ext.getCmp('specialCode').setDisabled(true);
						}

						if (o.data.changePlanDate != null) {
							var plandate = o.data.changePlanDate;
							plandate = plandate.substring(0, plandate
									.indexOf('T'))
									+ " "
									+ plandate.substring(plandate.indexOf('T')
											+ 1, plandate.length);
							Ext.get("changePlanDate").dom.value = plandate;
						}

						if (o.data.applyDate != null) {
							var plandate = o.data.applyDate;
							plandate = plandate.substring(0, plandate
									.indexOf('T'))
									+ " "
									+ plandate.substring(plandate.indexOf('T')
											+ 1, plandate.length);

							Ext.get("applyDate").dom.value = plandate;
						}
						tabpanel.setActiveTab(1);
						if (o.data.frontThing != null) {
							Ext.get("frontThing").dom.value = o.data.frontThing;
						}
						if (o.data.backThing != null) {
							Ext.get("backThing").dom.value = o.data.backThing;
						}

						// Ext.get('oldImage').dom.src =
						// "equchange/showphoto.action?changeNo="+o.data.equChangeNo+"&method=old";
						// Ext.get('newImage').dom.src =
						// "equchange/showphoto.action?changeNo="+o.data.equChangeNo+"&method=new";
						tabpanel.setActiveTab(0);

					}

				});
				tabpanel.getItem('tab1').setTitle("异动基本信息");

			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}

	}

	function deleteRecord() {
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {

			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("change.equChangeId")) {
					ids.push(member.get("change.equChangeId"));
				} else {

					store.remove(store.getAt(i));
				}
			}

			Ext.Msg.confirm("删除", "是否确定删除所选记录？", function(buttonobj) {

				if (buttonobj == "yes") {

					Ext.lib.Ajax.request('POST',
							'equchange/deleteChange.action', {
								success : function(action) {
									Ext.Msg.alert("提示", "删除成功！")
									store.load({
										params : {
											start : 0,
											limit : 18
										}
									});

								},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
							}, 'ids=' + ids);
				}
			});
		}
	}
	queryRecord();
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});

})