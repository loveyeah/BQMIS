Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
function getDate() {
	var d, s, t;
	d = new Date();
	s = d.getFullYear().toString(10) + "-";
	t = d.getMonth() + 1;
	s += (t > 9 ? "" : "0") + t + "-";
	t = d.getDate();
	// s += (t > 9 ? "" : "0") + t;
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
	Date.prototype.format = function(format) {
		var o = {
			"M+" : this.getMonth() + 1, // month
			"d+" : this.getDate(), // day
			"h+" : this.getHours(), // hour
			"m+" : this.getMinutes(), // minute
			"s+" : this.getSeconds(), // second
			"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
			"S" : this.getMilliseconds()
				// millisecond
		}
		if (/(y+)/.test(format))
			format = format.replace(RegExp.$1, (this.getFullYear() + "")
					.substr(4 - RegExp.$1.length));
		for (var k in o)
			if (new RegExp("(" + k + ")").test(format))
				format = format.replace(RegExp.$1, RegExp.$1.length == 1
						? o[k]
						: ("00" + o[k]).substr(("" + o[k]).length));
		return format;
	};

	var nowdate = new Date().format('yyyy-MM-dd');
	var d = new Date();
	d.setMonth(d.getMonth() - 1);
	var firstdate = d.format('yyyy-MM-dd');
	/*----------------------------------------------缺陷基本信息-----------------------------------------------------------------*/
	var stime = {
		xtype : 'datefield',
		format : 'Y-m-d',
		id : 'stime',
		name : 'stime',
		value : firstdate,
		readOnly : true
	};
	var etime = {
		xtype : 'datefield',
		format : 'Y-m-d',
		id : 'etime',
		name : 'etime',
		value : nowdate,
		readOnly : true
	};
	var dominationProfessionStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'bqfailure/dominationProfessionList.action'
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
	dominationProfessionStore.load();
	var dominationProfessionComboBox = new Ext.form.ComboBox({
		id : 'dominationProfession-combobox',
		store : dominationProfessionStore,
		fieldLabel : "管辖专业<font color='red'>*</font>",
		valueField : "specialityCode",
		displayField : "specialityName",
		mode : 'local',
		forceSelection : true,
		hiddenName : 'failure.dominationProfession',
		editable : false,
		allowBlank : false,
		triggerAction : 'all',
		selectOnFocus : true,
		blankText : '请选择...',
		emptyText : '请选择...',
		anchor : '95%'
	});
	var tbardominationProfessionStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'bqfailure/querydominationProfessionList.action'
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
	tbardominationProfessionStore.load();
	var tbardominationProfessionComboBox = new Ext.form.ComboBox({
		id : 'tbardominationProfession-combobox',
		store : tbardominationProfessionStore,
		valueField : "specialityCode",
		displayField : "specialityName",
		mode : 'local',
		forceSelection : true,
		hiddenName : 'specialityCode',
		editable : false,
		allowBlank : false,
		triggerAction : 'all',
		selectOnFocus : true,
		blankText : '管辖专业',
		emptyText : '管辖专业',
		value : '',
		anchor : '95%'
	});
	var runProfessionStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/findUintProfessionList.action'
		}),
		reader : new Ext.data.JsonReader({
			id : "unit"
		}, [{
			name : 'specialityCode'
		}, {
			name : 'specialityName'
		}])
	});
	runProfessionStore.load();
	var runProfessionComboBox = new Ext.form.ComboBox({
		id : 'special-combobox',
		fieldLabel : "运行专业<font color='red'>*</font>",
		store : runProfessionStore,
		valueField : "specialityCode",
		displayField : "specialityName",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'failure.runProfession',
		editable : false,
		allowBlank : false,
		triggerAction : 'all',
		blankText : '请选择...',
		emptyText : '请选择...',
		selectOnFocus : true,
		anchor : "95%"
	});
	var blockStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'bqfailure/blocklistByRe.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, [{
			name : 'blockCode',
			mapping : 'blockCode'
		}, {
			name : 'blockName',
			mapping : 'blockName'
		}])
	});
	blockStore.load();
	var blockComboBox = new Ext.form.ComboBox({
		id : 'block-combobox',
		fieldLabel : "所属系统<font color='red'>*</font>",
		store : blockStore,
		valueField : "blockCode",
		displayField : "blockName",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'failure.belongSystem',
		editable : false,
		allowBlank : false,
		triggerAction : 'all',
		blankText : '请选择...',
		emptyText : '请选择...',
		selectOnFocus : true,
		anchor : "95%"
	});

	var dsfailureType = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'bqfailure/findEquFailureTypelist.action',
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({
			root : 'failureTypelist'
		}, [{
			name : 'failuretypeCode',
			mapping : 'failuretypeCode'
		}, {
			name : 'failuretypeName',
			mapping : 'failuretypeName'
		}, {
			name : 'failurePri',
			mapping : 'failurePri'
		}])
	});
	dsfailureType.load();
	var failureTypeComboBox = new Ext.form.ComboBox({
		id : 'failureType-combobox',
		store : dsfailureType,
		fieldLabel : "缺陷类别<font color='red'>*</font>",
		valueField : "failuretypeCode",
		displayField : "failuretypeName",
		mode : 'local',
		forceSelection : true,
		hiddenName : 'failure.failuretypeCode',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		blankText : '请选择...',
		emptyText : '请选择...',
		anchor : '95%',
		listeners:{
			change:function(){
		repairDeptStore.proxy.conn.url ='bqfailure/repairDeptByEqu.action?fType=4'; 	
			repairDeptStore.load();
			}
		}
	});

	failureTypeComboBox.on('select', function() {
		Ext.Ajax.request({
			url : 'bqfailure/findFailureType.action?failureType.failuretypeCode='
					+ failureTypeComboBox.getValue(),
			method : 'post',
			success : function(result, request) {
				var json = eval('(' + result.responseText + ')');
				Ext.get("failure.failureLevel").dom.value = json.failureTypelist[0].failuretypeCode;
				Ext.get("failureLevel").dom.value = json.failureTypelist[0].failurePri;
			}
		});
	}, this);

	var repairDeptStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'bqfailure/repairDeptByEqu.action?fType=4',
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, [{
			name : 'deptCode',
			mapping : 'deptCode'
		}, {
			name : 'deptName',
			mapping : 'deptName'
		}])
	});
	repairDeptStore.load();
	var repairDepComboBox = new Ext.form.ComboBox({
		id : 'repairDep-combobox',
		store : repairDeptStore,
		fieldLabel : "检修部门<font color='red'>*</font>",
		valueField : "deptCode",
		displayField : "deptName",
		mode : 'local',
		
		hiddenName : 'failure.repairDep',
		editable : true,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		blankText : '请选择...',
		emptyText : '请选择...',
		anchor : '95%',
		forceSelection : true,
		typeAhead : true
		
	});
	var tbarrepairDeptStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'bqfailure/queryrepairDept.action',
			method : 'post'
		}),
//		params:
//		{
//			fType:Ext.getCmp('failureType-combobox').value
//		},
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, [{
			name : 'deptCode',
			mapping : 'deptCode'
		}, {
			name : 'deptName',
			mapping : 'deptName'
		}])
	});
	tbarrepairDeptStore.load();
	var tbarrepairDepComboBox = new Ext.form.ComboBox({
		id : 'tbarrepairDep-combobox',
		store : tbarrepairDeptStore,
		fieldLabel : '检修部门',
		valueField : "deptCode",
		displayField : "deptName",
		mode : 'local',
		forceSelection : true,
		hiddenName : 'deptCode',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		blankText : '请选择...',
		emptyText : '请选择...',
		value : '',
		anchor : '95%'
	});
	var ifStopRunComboBox = new Ext.form.ComboBox({
		id : 'ifStopRun-combobox',
		store : new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"],
			data : [['Y', '是'], ['N', '否']]
		}),
		fieldLabel : '是否限制运行',
		valueField : "retrunValue",
		displayField : "displayText",
		mode : 'local',
		forceSelection : true,
		hiddenName : 'failure.ifStopRun',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		blankText : '请选择...',
		emptyText : '请选择...',
		anchor : '95%'
	});
	// var message;
	var telManName = new Ext.form.TextField({
		fieldLabel : "电话通知人",
		id : 'telManName',
		name : "telManName",
		disabled : true,
		readOnly : true,
		anchor : "100%"
	});
	var btnTel = new Ext.Button({
		id : "btnTel",
		text : "...",
		disabled : true,
		name : "first",
		handler : function() {
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '灞桥电厂'
				}
			};
			var obj = window
					.showModalDialog(
							'../../../comm/jsp/hr/workerByDept/workerByDept2.jsp',
							args,
							'dialogWidth=600px;dialogHeight=420px;center=yes;help=no;resizable=no;status=no;');
			if (typeof(obj) == 'object') {
				Ext.get("failure.telMan").dom.value = obj.workerCode;
				Ext.get("telManName").dom.value = obj.workerName;
			}
		}
	});
	var telTime = new Ext.form.TextField({
		fieldLabel : "电话通知时间",
		id : 'telTime',
		name : "failure.telTime",
		cls : 'Wdate',
		style : 'cursor:pointer',
		anchor : "95%",
		disabled : true,
		// value : getDate(),
		listeners : {
			focus : function() {
				WdatePicker({
					dateFmt : 'yyyy-MM-dd HH:mm:ss'
				});
			}
		}
	});
	// 是否电话通知
	var isTel = new Ext.form.Checkbox({
		id : 'isTel',
		fieldLabel : '是否电话通知',
		checked : false,
		listeners : {
			check : isTelCheck
		}
	});
	function isTelCheck() {
		if (isTel.checked) {
			btnTel.setDisabled(false);
			telManName.setDisabled(false);
			telTime.setDisabled(false);
			// tel = "Y";
		} else {
			btnTel.setDisabled(true);
			telManName.setDisabled(true);
			telTime.setDisabled(true);
			telManName.setValue("");
			telTime.setValue("");
			Ext.get("failure.telMan").dom.value = "";
			// tel = "N";
		}
	}
	// 是否短信通知
	var isMessage = new Ext.form.Checkbox({
		id : 'isMessage',
		// name : 'con.isSum',
		fieldLabel : '是否短信通知',
		disabled : false,
		checked : false
			// ,
			// listeners : {
			// check : isMegCheck
			// }
	});
	// function isMegCheck() {
	// if (isMessage.checked) {
	// message = "Y";
	// } else {
	// message = "N";
	// }
	// }
	// 是否产生工单
	var isOrder = new Ext.form.Checkbox({
		id : 'isOrder',
		fieldLabel : '是否产生工单',
		disabled : false,
		checked : false
			// ,
			// listeners : {
			// // check : isTotalCheck
			// }
	});
	// 是否点检产生
	var isCheck = new Ext.form.Checkbox({
		id : 'isCheck',
		fieldLabel : '是否点检系统产生',
		disabled : true,
		checked : false
			// ,
			// listeners : {
			// // check : isTotalCheck
			// }
	});
	var failureFormPanel = new Ext.FormPanel({
		title : '缺陷登记',
		border : false,
		labelAlign : 'right',
		items : [new Ext.form.FieldSet({
			title : '缺陷详细信息',
			autoHeight : true,
			items : [{
				layout : "column",
				border : false,
				items : [{
					name : 'failure.id',
					xtype : 'hidden'
				}, {
					columnWidth : 0.25,
					layout : "form",
					border : false,
					items : [{
						xtype : "textfield",
						fieldLabel : "发现人<font color='red'>*</font>",
						name : "findByName",
						readOnly : true,
						allowBlank : false,
						value: document.getElementById('workName').value,
						anchor : "100%"
					}, {
						xtype : "hidden",
						name : "failure.findBy",
						value : document.getElementById('workCode').value
					}]
				}, {
					columnWidth : 0.05,
					layout : "form",
					anchor : "100%",
					border : false,
					items : [{
						xtype : "button",
						text : "...",
						name : "first",
						handler : function() {
							var args = {
								selectModel : 'single',
								notIn : "'999999'",
								rootNode : {
									id : '-1',
									text : '灞桥电厂'
								}
							};
							var obj = window
									.showModalDialog(
											'../../../comm/jsp/hr/workerByDept/workerByDept2.jsp',
											args,
											'dialogWidth=600px;dialogHeight=420px;center=yes;help=no;resizable=no;status=no;');
							if (typeof(obj) == 'object') {
								Ext.get("failure.findBy").dom.value = obj.workerCode;
								Ext.get("findByName").dom.value = obj.workerName;
								Ext.get("failure.findDept").dom.value = obj.deptCode;
								Ext.get("findDeptName").dom.value = obj.deptName;
							}
						}
					}]
				}, {
					columnWidth : 0.30,
					layout : "form",
					border : false,
					items : [{
						xtype : "textfield",
						fieldLabel : "发现部门<font color='red'>*</font>",
						name : "findDeptName",
						value : document.getElementById('deptName').value,
						readOnly : true,
						allowBlank : false,
						anchor : "95%"
					}, {
						xtype : "hidden",
						value : document.getElementById('deptCode').value,
						name : "failure.findDept"
					}]
				}, {
					columnWidth : 0.30,
					layout : "form",
					border : false,
					items : [{
						xtype : "textfield",
						fieldLabel : "发现时间",
						name : "failure.findDate",
						cls : 'Wdate',
						style : 'cursor:pointer',
						anchor : "95%",
						value : getDate(),
						listeners : {
							focus : function() {
								WdatePicker({
									dateFmt : 'yyyy-MM-dd HH:mm:ss'
								});
							}
						}
					}]
				}]
			}, {
				layout : "column",
				border : false,
				items : [{
					xtype : "hidden",
					name : "failure.attributeCode"
				}, {
					columnWidth : 1,
					layout : "form",
					border : false,
					items : [{
						xtype : "textfield",
						fieldLabel : "设备名称<font color='red'>*</font>",
						name : "failure.equName",
						allowBlank : false,
						anchor : "88.3%",
						listeners : {
							change : function() {
								Ext.get("failure.failureContent").dom.value = Ext
										.get("failure.equName").dom.value;
							}
						}
					}]
				}
//				, {
//					columnWidth : 0.15,
//					layout : "form",
//					border : false,
//					items : [{
//						xtype : "button",
//						text : "...",
//						name : "first",
//						handler : function() {
//							var obj = window
//									.showModalDialog(
//											'../../../comm/jsp/equselect/selectAttribute.jsp',
//											null,
//											'dialogWidth=600px;dialogHeight=420px;center=yes;help=no;resizable=no;status=no;');
//
//							if (typeof(obj) == 'object') {
//								Ext.get("failure.attributeCode").dom.value = obj.code;
//								Ext.get("failure.equName").dom.value = obj.name;
//								Ext.get("failure.failureContent").dom.value = Ext
//										.get("failure.equName").dom.value
//										+ Ext.get("bugCode_text").dom.value;
//							}
//						}
//					}]
//				}
				]
			}, {
				layout : "column",
				border : false,
				items : [{
					columnWidth : 0.3,
					layout : "form",
					border : false,
					items : [blockComboBox]
				}, {
					columnWidth : 0.25,
					layout : "form",
					border : false,
					items : [{
						xtype : "textfield",
						fieldLabel : "故障类型<font color='red'>*</font>",
						name : "bugCode_text",
						readOnly : true,
						allowBlank : false,
						anchor : "100%",
						value : "其它",
						listeners : {
							change : function() {
								if(Ext.get("bugCode_text").dom.value != '其它')
								{
									Ext.get("failure.failureContent").dom.value = Ext
									.get("failure.equName").dom.value;
								}
						
							}
						}
					}, {
						xtype : "hidden",
						name : "failure.bugCode",
						value :"qt",
						allowBlank : false
					}]
				}, {
					columnWidth : 0.05,
					layout : "form",
					border : false,
					items : [{
						xtype : "button",
						text : "...",
						name : "first",
						handler : function() {
							var obj = window
									.showModalDialog(
											'../../../equ/base/business/bug/selectBug.jsp',
											null,
											'dialogWidth=850px;dialogHeight=420px;center=yes;help=no;resizable=no;status=no;');

							if (typeof(obj) == 'object') {
								Ext.get("failure.bugCode").dom.value = obj.code;
								Ext.get("bugCode_text").dom.value = obj.name;
								Ext.get("failure.likelyReason").dom.value = obj.reason;
							}
							Ext.get("failure.failureContent").dom.value = Ext
									.get("failure.equName").dom.value;
						}
					}]
				}, {
					columnWidth : 0.3,
					layout : "form",
					border : false,
					items : [{
						xtype : "textfield",
						fieldLabel : "当前状态",
						value : '未上报',
						name : "woStatus_text",
						readOnly : true,
						anchor : "95%"
					}, {
						xtype : "hidden",
						value : '0',
						name : "failure.woStatus"
					}]
				}]
			}, {
				layout : "column",
				border : false,
				items : [{
					columnWidth : 0.3,
					layout : "form",
					border : false,
					items : [runProfessionComboBox]
				}, {
					columnWidth : 0.3,
					layout : "form",
					border : false,
					items : [ifStopRunComboBox]
				}, {
					columnWidth : 0.3,
					layout : "form",
					border : false,
					items : [dominationProfessionComboBox]
				}]
			}, {
				layout : "column",
				border : false,
				items : [{
					columnWidth : 0.3,
					layout : "form",
					border : false,
					items : [failureTypeComboBox]
				}, {
					columnWidth : 0.3,
					layout : "form",
					border : false,
					items : [{
						xtype : "textfield",
						fieldLabel : "优先级",
						name : "failureLevel",
						readOnly : true,
						anchor : "95%"
					}, {
						xtype : "hidden",
						name : "failure.failureLevel",
						readOnly : true
					}]
				}, {
					columnWidth : 0.3,
					layout : "form",
					border : false,
					items : [repairDepComboBox]
				}]
			}, {
				layout : "column",
				border : false,
				items : [{
					columnWidth : 1,
					layout : "form",
					border : false,
					items : [{
						xtype : "textarea",
						fieldLabel : "未消除前措施",
						name : "failure.preContent",
						anchor : "88.5%",
						height : 55
					}]
				}]
			}, {
				layout : "column",
				border : false,
				items : [{
					columnWidth : 1,
					layout : "form",
					border : false,
					items : [{
						xtype : "textarea",
						fieldLabel : "缺陷内容<font color='red'>*</font>",
						name : "failure.failureContent",
						anchor : "88.5%",
						allowBlank : false,
						height : 55
					}]
				}]
			}, {
				layout : "column",
				border : false,
				items : [{
					columnWidth : 1,
					layout : "form",
					border : false,
					items : [{
						xtype : "textarea",
						fieldLabel : "可能原因",
						name : "failure.likelyReason",
						anchor : "88.5%",
						height : 55
					}]
				}]
			}, {
				layout : "column",
				border : false,
				items : [{
					columnWidth : 0.3,
					layout : "form",
					border : false,
					items : [isTel]
				}, {
					columnWidth : 0.25,
					layout : "form",
					border : false,
					items : [telManName, {
						xtype : "hidden",
						name : "failure.telMan"
					}]
				}, {
					columnWidth : 0.05,
					layout : "form",
					anchor : "100%",
					border : false,
					items : [btnTel]
				}, {
					columnWidth : 0.30,
					layout : "form",
					border : false,
					items : [telTime]
				}]
			}, {
				layout : "column",
				border : false,
				items : [{
					columnWidth : 0.3,
					layout : "form",
					border : false,
					items : [isMessage]
				}, {
					columnWidth : 0.3,
					layout : "form",
					border : false,
					items : [isOrder]
				}, {
					columnWidth : 0.3,
					layout : "form",
					border : false,
					items : [isCheck]
				}]
			}, {
				layout : "column",
				border : false,
				items : [{
					columnWidth : 0.3,
					layout : "form",
					border : false,
					items : [{
						xtype : "textfield",
						fieldLabel : "填写人",
						name : "writeName",
						readOnly : true,
						value : document.getElementById('workName').value,
						anchor : "95%"
					}, {
						xtype : "hidden",
						name : "failure.writeBy",
						value : document.getElementById('workCode').value,
						anchor : "95%"
					}]
				}, {
					columnWidth : 0.3,
					layout : "form",
					border : false,
					items : [{
						xtype : "textfield",
						fieldLabel : "填写部门",
						name : "writeDeptName",
						readOnly : true,
						value : document.getElementById('deptName').value,
						anchor : "95%"
					}, {
						xtype : "hidden",
						name : "failure.writeDept",
						value : document.getElementById('deptCode').value
					}]
				}, {
					columnWidth : 0.3,
					layout : "form",
					border : false,
					items : [{
						xtype : "textfield",
						fieldLabel : "填写时间",
						name : "failure.writeDate",
						readOnly : true,
						value : getDate(),
						anchor : "95%"
					}]
				}]
			}]
		})],
		buttons : [{
			id : 'btnFailureSave',
			xtype : 'button',
			text : "保存",
			iconCls : 'save',
			handler : function() {	
			if (failureFormPanel.getForm().isValid()) {
					failureFormPanel.getForm().submit({
						url : 'bqfailure/'
								+ (Ext.get("failure.id").dom.value == ""
										? "addFailure"
										: "updateFailure") + '.action',
						params : {
							'failure.isTel' : (isTel.checked ? 'Y' : 'N'),
							'failure.isMessage' : (isMessage.checked
									? 'Y'
									: 'N'),
							'failure.ifOpenWorkorder' : (isOrder.checked
									? 'Y'
									: 'N'),
							'failure.isCheck' : (isCheck.checked ? 'Y' : 'N')
						},
						waitMsg : '正在保存数据...',
						method : 'post',
						success : function(form, action) {
							if (Ext.get("failure.id").dom.value == "") {
								Ext.Msg.alert("提示", "增加成功！");
							} else {
								Ext.Msg.alert("提示", "修改成功！");
							}
							failureFormPanel.getForm().reset();
						},
						failure : function(form, action) {
							Ext.Msg.alert("提示", "保存失败！");
						}
					});
				}
			}
		}, {
			id : 'btnFailureCancel',
			xtype : 'button',
			text : "取消",
			iconCls : 'cancer',
			handler : function() {
				failureFormPanel.getForm().reset();
			}
		}]
	});

	// 缺陷登记表单
	var failureForm = new Ext.FormPanel({
		id : 'failure-form',
		frame : true,
		autoWidth : true,
		labelAlign : 'left',
		align : 'center',
		items : [{
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.05,
				border : false
			}, {
				columnWidth : 0.9,
				layout : "form",
				border : false,
				items : [failureFormPanel]
			}, {
				columnWidth : 0.05,
				border : false
			}]
		}]
	});

	var url = "bqfailure/blocklist.action";
	var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", url, false);
	conn.send(null);
	var blockStoreGrid = eval('(' + conn.responseText + ')').list;

	var blockComboBoxList = new Ext.data.Record.create([{
		name : 'blockCode'
	}, {
		name : 'blockName'
	}]);

	var blockComboBoxGrids = new Ext.data.JsonStore({
		data : blockStoreGrid,
		fields : blockComboBoxList
	});

	var blockComboBoxGrid = new Ext.form.ComboBox({
		id : 'block-comboboxgrid',
		fieldLabel : '所属系统',
		store : blockComboBoxGrids,
		valueField : "blockCode",
		displayField : "blockName",
		mode : 'local',
		value : "",
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'belongSystemGrid',
		editable : false,
		triggerAction : 'all',
		blankText : '请选择...',
		emptyText : '请选择...',
		selectOnFocus : true,
		anchor : "95%"
	});

	/*-------------------------------------------------------缺陷列表------------------------------------------------------*/
	var Failure = new Ext.data.Record.create([{
		name : 'id'
	}, {
		name : 'failureCode'
	}, {
		name : 'woStatusName'
	}, {
		name : 'failuretypeName'
	}, {
		name : 'failurePri'
	}, {
		name : 'attributeCode'
	}, {
		name : 'equName'
	}, {
		name : 'failureContent'
	}, {
		name : 'locationDesc'
	}, {
		name : 'findDate'
	}, {
		name : 'findBy'
	}, {
		name : 'findByName'
	}, {
		name : 'findDept'
	}, {
		name : 'findDeptName'
	}, {
		name : 'woCode'
	}, {
		name : 'bugCode'
	}, {
		name : 'bugName'
	}, {
		name : 'failuretypeCode'
	}, {
		name : 'failureLevel'
	}, {
		name : 'woStatus'
	}, {
		name : 'preContent'
	}, {
		name : 'ifStopRun'
	}, {
		name : 'ifStopRunName'
	}, {
		name : 'runProfession'
	}, {
		name : 'dominationProfession'
	}, {
		name : 'dominationProfessionName'
	}, {
		name : 'repairDep'
	}, {
		name : 'repairDepName'
	}, {
		name : 'installationCode'
	}, {
		name : 'installationDesc'
	}, {
		name : 'belongSystem'
	}, {
		name : 'belongSystemName'
	}, {
		name : 'likelyReason'
	}, {
		name : 'woPriority'
	}, {
		name : 'writeBy'
	}, {
		name : 'writeByName'
	}, {
		name : 'writeDept'
	}, {
		name : 'writeDeptName'
	}, {
		name : 'writeDate'
	}, {
		name : 'repairDept'
	}, {
		name : 'realrepairDept'
	}, {
		name : 'ifOpenWorkorder'
	}, {
		name : 'ifRepeat'
	}, {
		name : 'supervisor'
	}, {
		name : 'workFlowNo'
	}, {
		name : 'wfState'
	}, {
		name : 'entrepriseCode'
	}, {
		name : 'isTel'
	}, {
		name : 'telMan'
	}, {
		name : 'telManName'
	}, {
		name : 'telTime'
	}, {
		name : 'isMessage'
	}, {
		name : 'isCheck'
	}

	]);

	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'bqfailure/findEquFailurelist.action',
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : 'list'
		}, Failure)
	});

	// 单击Grid行事件
	var sm = new Ext.grid.RowSelectionModel({
		singleSelect : true
	});

	var colModel = new Ext.grid.ColumnModel([
			new Ext.grid.CheckboxSelectionModel(), new Ext.grid.RowNumberer({
				header : '序号',
				width : 35
			}), {
				header : "id",
				dataIndex : "id",
				hidden : true
			}, {
				header : '缺陷编码',
				dataIndex : 'failureCode',
				width : 100
			}, {
				header : '所属系统',
				dataIndex : 'belongSystemName',
				width : 100
			}, {
				header : '缺陷内容',
				dataIndex : 'failureContent',
				width : 200
			}, {
				header : '管辖专业',
				dataIndex : 'dominationProfessionName',
				width : 60
			}, {
				header : '检修部门',
				dataIndex : 'repairDepName',
				width : 120
			}, {
				header : '状态',
				dataIndex : 'woStatusName',
				width : 50
			}, {
				header : '发现时间',
				dataIndex : 'findDate',
				width : 120
			}, {
				header : '发现人',
				dataIndex : 'findByName',
				width : 60
			}, {
				dataIndex : "failuretypeCode",
				hidden : true
			}, {
				header : '发现部门',
				dataIndex : 'findDeptName',
				width : 100
			}, {
				header : '类别',
				dataIndex : 'failuretypeName',
				width : 60
			}, {
				dataIndex : "failureLevel",
				hidden : true
			}, {
				header : '优先级',
				dataIndex : 'failurePri',
				width : 140
			}, {
				header : '设备功能码',
				dataIndex : 'attributeCode',
				width : 120
			}, {
				header : '故障名称',
				dataIndex : "bugName"
			}, {
				header : '设备名称',
				dataIndex : 'equName',
				width : 120
			}, {
				header : '填写人',
				dataIndex : 'writeByName',
				width : 60
			}, {
				header : '填写人部门',
				dataIndex : 'writeDeptName',
				width : 100
			}, {
				header : '填写时间',
				dataIndex : 'writeDate',
				width : 120
			}, {
				dataIndex : "ifStopRun",
				hidden : true
			}, {
				dataIndex : "ifStopRunName",
				hidden : true
			}, {
				dataIndex : "belongSystem",
				hidden : true
			}]);
	// 排序
	colModel.defaultSortable = true;

	var failureGrid = new Ext.grid.GridPanel({
		id : 'failure-grid',
		border : false,
		autoWidth : true,
		autoScroll : true,
		title : '缺陷列表',
		ds : ds,
		cm : colModel,
		sm : sm,
		loadMask : {
			msg : '读取数据中 ...'
		},
		listeners : {
			activate : function() {
				ds.load({
					params : {
						start : 0,
						limit : 18,
						belongBlock : "",
						specialityCode : "",
						deptCode : ""
					}
				});
			}
		},
		border : true,
		tbar : new Ext.Toolbar({
			items : [stime, '到', etime, '所属系统', blockComboBoxGrid, '管辖专业',
					tbardominationProfessionComboBox, '检修部门',
					tbarrepairDepComboBox, '|', {
						id : 'btnReflesh',
						text : "查询",
						iconCls : 'query',
						handler : function() {
							ds.load({
								params : {
									start : 0,
									limit : 18
								}
							});
						}
					}]
		}),
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : ds,
			displayInfo : true,
			displayMsg : '显示第 {0} 条到 {1} 条记录，一共 {2} 条',
			emptyMsg : "没有记录"
		})
	});

	ds.load({
		params : {
			start : 0,
			limit : 18,
			belongBlock : "",
			specialityCode : "",
			deptCode : ""
		}
	});

	ds.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			sdate : document.getElementById("stime").value,
			edate : document.getElementById("etime").value,
			belongBlock : document.getElementById("belongSystemGrid").value,
			specialityCode : tbardominationProfessionComboBox.getValue(),
			deptCode : tbarrepairDepComboBox.getValue()
		});
		if (!document.getElementById('secondTbar')) {
			var tbar2 = new Ext.Toolbar({
				id : 'secondTbar',
				renderTo : failureGrid.tbar,
				items : [{
					id : 'btnAdd',
					text : "新增",
					iconCls : 'add',
					handler : function() {
						failureFormPanel.show();
						failureFormPanel.getForm().reset();
					}
				}, '-', {
					id : 'btnSave',
					text : "修改",
					iconCls : 'update',
					handler : function() {
						var rec = failureGrid.getSelectionModel().getSelected();
						if (rec) {
							if (rec.get("writeBy") == document
									.getElementById("workCode").value) {
								failureFormPanel.show();
								Ext.get("failure.id").dom.value = rec.get("id");
								Ext.get("findByName").dom.value = rec
										.get("findByName");
								Ext.get("failure.findBy").dom.value = rec
										.get("findBy");
								Ext.get("failure.findDept").dom.value = rec
										.get("findDept");
								Ext.get("findDeptName").dom.value = rec
										.get("findDeptName");
								Ext.get("failure.findDate").dom.value = rec
										.get("findDate");
								Ext.get("failure.equName").dom.value = rec
										.get("equName");
								Ext.get("failure.attributeCode").dom.value = rec
										.get("attributeCode");
								blockComboBox.setValue(rec.get("belongSystem"));
								Ext.get("failure.bugCode").dom.value = rec
										.get("bugCode");
								Ext.get("bugCode_text").dom.value = rec
										.get("bugName");
								failureTypeComboBox.setValue(rec
										.get("failuretypeCode"));
								runProfessionComboBox.setValue(rec
										.get("runProfession"));
								dominationProfessionComboBox.setValue(rec
										.get("dominationProfession"));
								repairDepComboBox
										.setValue(rec.get("repairDep"));
								Ext.get("failure.failureLevel").dom.value = rec
										.get("failureLevel");
								Ext.get("failureLevel").dom.value = rec
										.get("failurePri");
								ifStopRunComboBox
										.setValue(rec.get("ifStopRun"));
								Ext.get("failure.preContent").dom.value = rec
										.get("preContent");
								Ext.get("failure.failureContent").dom.value = rec
										.get("failureContent");
								Ext.get("failure.likelyReason").dom.value = rec
										.get("likelyReason");
								if (rec.get("isMessage") == "Y") {
									isMessage.setValue(true);
								}
								if (rec.get("isTel") == "Y") {
									isTel.setValue(true);
								}
								if (rec.get("ifOpenWorkorder") == "Y") {
									isOrder.setValue(true);
								}
								if (rec.get("isCheck") == "Y") {
									isCheck.setValue(true);
								}
								Ext.get("failure.telMan").dom.value = rec
										.get("telMan");
								Ext.get("telManName").dom.value = rec
										.get("telManName");
								Ext.get("telTime").dom.value = rec
										.get("telTime");
								Ext.get("writeName").dom.value = rec
										.get("writeByName");
								Ext.get("failure.writeBy").dom.value = rec
										.get("writeBy");
							} else {
								Ext.Msg.alert("提示", "不是您填写的,不能修改!");
							}
						} else {
							Ext.Msg.alert("提示", "请选择要修改的信息!");
						}
					}
				}, '-', {
					id : 'btnDelete',
					text : "删除",
					iconCls : 'delete',
					handler : function() {
						var rec = failureGrid.getSelectionModel().getSelected();
						if (rec) {
							if (rec.get("writeBy") == document
									.getElementById("workCode").value) {
								if (rec.get("woStatus") != 10) {
									if (confirm("确定要删除\""
											+ rec.data.failureCode + "\"缺陷吗？")) {
										Ext.Ajax.request({
											url : 'bqfailure/deleteFailure.action?failure.id='
													+ rec.get("id"),
											method : 'post',
											waitMsg : '正在删除数据...',
											success : function(result, request) {
												Ext.MessageBox.alert("提示",
														'删除成功!');
												ds.load({
													params : {
														start : 0,
														limit : 18
													}
												});
											},
											failure : function(result, request) {
												Ext.MessageBox.alert("提示",
														'删除失败！');
											}
										});
									}
								} else {
									Ext.Msg.alert("提示", "退回的记录不能删除!");
								}
							} else {
								Ext.Msg.alert("提示", "不是您填写的,不能删除!");
							}
						} else {
							Ext.Msg.alert("提示", "请选择要删除的信息!");
						}
					}
				}, '-', {
					id : 'btnReport',
					text : "上报",
					iconCls : 'upcommit',
					handler : function() {
						var rec = failureGrid.getSelectionModel().getSelected();
						if (rec) {
							if (rec.get("writeBy") == document
									.getElementById("workCode").value) {
								url = "failureReport.jsp?failureId="
										+ rec.get("id") + "&failureType="
										+ rec.get("failuretypeCode")
										+ "&entryId=" + rec.get("workFlowNo");
								var o = window
										.showModalDialog(url, '',
												'dialogWidth=800px;dialogHeight=500px;status=no');
								if (o) {
									ds.load({
										params : {
											start : 0,
											limit : 18
										}
									});
								}
							} else {
								Ext.Msg.alert("提示", '不是您填写的，不能上报!');
							}
						} else {
							Ext.Msg.alert("提示", '请选择要上报的信息!');
						}
						failureFormPanel.getForm().reset();
					}
				}]
			});
		}

		failureGrid
				.setHeight(document.getElementById("failure-grid").offsetHeight
						- document.getElementById("secondTbar").offsetHeight);
	});

	failureGrid.on('rowdblclick', function(grid, rowIndex, e) {
		var rec = failureGrid.getStore().getAt(rowIndex);
		if (rec) {
			if (rec.get("writeBy") == document.getElementById("workCode").value) {
				failureFormPanel.show();
				Ext.get("failure.id").dom.value = rec.get("id");
				Ext.get("findByName").dom.value = rec.get("findByName");
				Ext.get("failure.findBy").dom.value = rec.get("findBy");
				Ext.get("failure.findDept").dom.value = rec.get("findDept");
				Ext.get("findDeptName").dom.value = rec.get("findDeptName");
				Ext.get("failure.findDate").dom.value = rec.get("findDate");
				Ext.get("failure.equName").dom.value = rec.get("equName");
				Ext.get("failure.attributeCode").dom.value = rec
						.get("attributeCode");
				blockComboBox.setValue(rec.get("belongSystem"));
				Ext.get("failure.bugCode").dom.value = rec.get("bugCode");
				Ext.get("bugCode_text").dom.value = rec.get("bugName");
				failureTypeComboBox.setValue(rec.get("failuretypeCode"));
				runProfessionComboBox.setValue(rec.get("runProfession"));
				dominationProfessionComboBox.setValue(rec
						.get("dominationProfession"));
				repairDepComboBox.setValue(rec.get("repairDep"));
				Ext.get("failure.failureLevel").dom.value = rec
						.get("failureLevel");
				Ext.get("failureLevel").dom.value = rec.get("failurePri");
				ifStopRunComboBox.setValue(rec.get("ifStopRun"));
				Ext.get("failure.preContent").dom.value = rec.get("preContent");
				Ext.get("failure.failureContent").dom.value = rec
						.get("failureContent");
				Ext.get("failure.likelyReason").dom.value = rec
						.get("likelyReason");
				if (rec.get("isMessage") == "Y") {
					isMessage.setValue(true);
				}
				if (rec.get("isTel") == "Y") {
					isTel.setValue(true);
				}
				if (rec.get("ifOpenWorkorder") == "Y") {
					isOrder.setValue(true);
				}
				if (rec.get("isCheck") == "Y") {
					isCheck.setValue(true);
				}
				Ext.get("failure.telMan").dom.value = rec.get("telMan");
				Ext.get("telManName").dom.value = rec.get("telManName");
				Ext.get("telTime").dom.value = rec.get("telTime");
				Ext.get("writeName").dom.value = rec.get("writeByName");
				Ext.get("failure.writeBy").dom.value = rec.get("writeBy");
			} else {
				Ext.Msg.alert("提示", "不是您填写的,不能修改!");
			}
		} else {
			Ext.Msg.alert("提示", '请选择要修改的信息!');
		}
	});
	/*-------------------------------------------------------页面布局------------------------------------------------------------------------------*/
	var panel = new Ext.Panel({
		layout : 'fit',
		border : false,
		items : [new Ext.TabPanel({
			tabPosition : 'bottom',
			border : false,
			activeTab : 0,
			items : [failureFormPanel, failureGrid]
		})]
	});

	// 设定布局器及面板
	var layout = new Ext.Viewport({
		layout : "border",
		border : false,
		items : [{
			title : "",
			region : 'center',
			layout : 'fit',
			border : false,
			margins : '0 0 0 0',
			split : true,
			collapsible : false,
			items : [panel]
		}]
	});

	setTimeout(function() {
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut({
			remove : true
		});
	}, 250);

	/*-------------------------------------------------------缺陷类别说明窗口------------------------------------------------------------------------------*/
	var FailureType = new Ext.data.Record.create([{
		name : 'id'
	}, {
		name : 'failuretypeCode'
	}, {
		name : 'failuretypeName'
	}, {
		name : 'failurePri'
	}, {
		name : 'failuretypeDesc'
	}, {
		name : 'needCaclOvertime'
	}, {
		name : 'isUse'
	}, {
		name : 'enterpriseCode'
	}]);
	var typeds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'bqfailure/findEquFailureTypelist.action',
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({
			root : 'failureTypelist'
		}, FailureType)
	});
	typeds.load();
	var colModel = new Ext.grid.ColumnModel([{
		header : '类别名称',
		dataIndex : 'failuretypeName',
		width : 60
	}, {
		header : '缺陷优先级',
		dataIndex : 'failurePri',
		width : 100
	}, {
		header : '类别描述',
		dataIndex : 'failuretypeDesc',
		width : 320,
		renderer : function change(val) {
			return ' <span style="white-space:normal;">' + val + ' </span>';
		}
	}]);
	// 排序
	colModel.defaultSortable = true;
	// 单击Grid行事件
	var sm = new Ext.grid.RowSelectionModel({
		singleSelect : true
	});
	var failureTypeGrid = new Ext.grid.GridPanel({
		id : 'failureType-grid',
		autoScroll : true,
		ds : typeds,
		cm : colModel,
		sm : sm,
		border : true
	});

	var tipswindow = new Ext.Window({
		width : 500,
		height : 350,
		closeAction : 'hide',
		closable : false,
		resizable : false,
		layout : 'fit',
		items : [failureTypeGrid]
	});
	document.getElementById("failureType-combobox").onmouseover = function() {
		tipswindow.show();
	};// document.getElementById("tips-div").style.display="block";};
	document.getElementById("failureType-combobox").onmouseout = function() {
		tipswindow.hide();
	};
});