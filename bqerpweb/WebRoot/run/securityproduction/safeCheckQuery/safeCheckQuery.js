Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var flagMeasure = null;
	var flagName = null;
	var isProblem = "N";
	var flagMe = "add";
	var amendMe = 'add';
	
	var flagCheckupId = null;
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result.workerCode) {
					// 设定工作负责人为登录人
					modifyBy.setValue(result.workerCode);
					modifyName.setValue(result.workerName);
					checkBy.setValue(result.workerCode)
					checkName.setValue(result.workerName)
				}
			}
		});
	}
	// 系统当前时间
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t;
		return s;
	}
	function getYear() {
		var d, s;
		d = new Date();
		s = d.getFullYear().toString(10);	
		return s;
	}
	function getSeason() {
		var d, m;
		d = new Date();
		m = d.getMonth() + 1;
		if(m % 3 == 0)
			return m/3;
		else 
		    return Math.floor(m/3) + 1
	}
	var ids = new Array();
	var rootNode = new Ext.tree.AsyncTreeNode({
		text : '25项反事故措施条例表',
		id : '0',
		isRoot : true
	});
	var currentNode = rootNode;
	var treePanel = new Ext.tree.TreePanel({
		region : 'center',
		animate : true,
	//	autoHeight : true,
		autoScroll : true,
		root : rootNode,
		border : false,
		rootVisible : true,
		loader : new Ext.tree.TreeLoader({
//			dataUrl : "security/findByParentCode.action?check=1"
			dataUrl : "security/findByParentCode.action"
		})
	});
	treePanel.setRootNode(rootNode);
	treePanel.on("click", treeClick);
	rootNode.select();
	rootNode.expand();
	
	// ************检查grid开始***************
	// 数据
	var cheRec = new Ext.data.Record.create([{
		name : 'check.checkupId'
	},{
		name : 'check.measureCode'
	},{
		name : 'check.isProblem'
	},{
		name : 'check.checkBy'
	},{
		name : 'check.approveBy'
	},{
		name : 'check.approveText'
	},{
		name : 'check.approveStatus'
	},{
		name : 'check.season'
	},{
		name : 'check.modifyBy'
	},{
		name : 'checkName'
	},{
		name : 'checkDate'
	},{
		name : 'approveName'
	},{
		name : 'approveDate'
	},{
		name : 'modifyName'
	},{
		name : 'modifyDate'
	},{
		name : 'measureName'
	},{
		name : 'specialCode'
	},{
		name : 'specialName'
	}])
	// 检查store 
	var cheStore = new Ext.data.JsonStore({
		url : 'security/getSafeCheckupList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : cheRec
	})
	
	var chesm = new Ext.grid.CheckboxSelectionModel({
	})
	// 年份
	var year = new Ext.form.TextField({
		id : 'year',
		name : 'year',
		style : 'cursor:pointer',
		readOnly : true,
		width : 50,
		value : getYear(),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y',
					alwaysUseStartDate : false,
					dateFmt : 'yyyy',
					onpicked : function() {
						sea.setDisabled(false)
					},
					onclearing : function() {
						sea.setValue(null);
						sea.setDisabled(true)
					}
				});
			}
		}
	})
	// 季度
	var seStore = new Ext.data.SimpleStore({
		fields : ['id','name'],
		data : [['',''],['1','一季度'],['2','二季度'],['3','三季度'],['4','四季度']]
	})
	var sea = new Ext.form.ComboBox({
		id : 'sea',
		name : 'sea',
		store : seStore,
		valueField : 'id',
		displayField : 'name',
		width : 60,
		mode : 'local',
		triggerAction : 'all',
		readOnly :true,
		value : getSeason()
	})
	
	//状态
	var stuStore = new Ext.data.SimpleStore({
		fields : ['id','name'],
		data : [['','所有'],['0','未上报'],['1','已上报'],['2','已审核'],['3','退回']]
	})
	var status = new Ext.form.ComboBox({
		id : 'status',
		name : 'status',
		store : stuStore,
		valueField : 'id',
		displayField : 'name',
		width : 60,
		mode : 'local',
		triggerAction : 'all',
		readOnly : true,
		value : ''
	})
	
	// 专业
	var storeRepairSpecail = new Ext.data.Store({
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
//	storeRepairSpecail.load();
	var specialCode = new Ext.form.ComboBox({
		id : 'specialCode',
		fieldLabel : "专业",
		store : storeRepairSpecail,
		displayField : "specialityName",
		valueField : "specialityCode",
		hiddenName : 'model.specialCode',
		mode : 'remote',
		triggerAction : 'all',
		value : '',
		readOnly : true,
		width : 60
	})
	
	// 检查人
	var checkName = new Ext.form.TextField({
		fieldLabel : "检查人:",
		labelSeparator : '',
//		allowBlank : false,
		id : 'checkName',
		name : 'checkName',
		readOnly : true,
		width : 50
	})
	checkName.onClick(function() {
			var ps = personSelect();
			if (ps != null) {
				checkBy.setValue(ps.workerCode);
				checkName.setValue(ps.workerName);
			}
		});
	var checkBy = new Ext.form.Hidden({
		id : 'checkBy',
		name : 'check.checkBy'
	})
	// 反事故措施
	var tbar1 = new Ext.Toolbar({
		width : 800,
		items : ['季度：',year,sea,'-','状态：',status,'-',
			'专业：',specialCode,'-','检查人：',checkName,'-',{id : 'btnQuery',
		text : '查询',
		iconCls : 'query',
		handler : function(){
			var season = null;
			if(year.getValue() != null && year.getValue() != '')
			{
				if(sea.getValue() == null || sea.getValue() == '')
				{
					Ext.Msg.alert('提示','请选择季度');
					return;
				}
			}
			if(year.getValue() != null && year.getValue() != '')
			{
				season = year.getValue();
				season += sea.getValue();
			}
			cheStore.load({
				params : {
					status : status.getValue(),
					season : season,
					specialCode : specialCode.getValue(),
					checkBy : checkBy.getValue(),
					measureCode : flagMeasure,
					start : 0,
					limit : 18
				}
			})
		}}]
	});
	var chGrid = new Ext.grid.GridPanel({
		store : cheStore,
		tbar : tbar1,
		layout : 'fit',
		sm : chesm,
			height : 340,
		columns : [new Ext.grid.RowNumberer({
			header : '行号',
			width : 32
		}),chesm,{
			header : '措施编码',
			sortabel : true,
			dataIndex : 'check.measureCode'
		},{
			header : '措施名称',
			sortabel : true,
			dataIndex : 'measureName'
		},{
			header : '专业',
			sortabel : true,
			dataIndex : 'specialName'
		},{
			header : '季度',
			sortabel : true,
			dataIndex : 'check.season',
			renderer : function(v) {
								var numstr = "";
								if (v.substring(4, 5) == "1")
									numstr = "一季度";
								else if (v.substring(4, 5) == "2")
									numstr = "二季度";
								else if (v.substring(4, 5) == "3") 
									numstr = "三季度";
								else if (v.substring(4, 5) == "4") 
									numstr = "四季度";
								var string = v.substring(0, 4) + "年" + numstr;
								return string;
							}
		},{
			header : '是否存在问题',
			sortabel : true,
			dataIndex : 'check.isProblem',
			renderer : function(v)
			{
				if(v == 'Y')
					return '是';
				else if(v == 'N')
					return '否';
			}
		},{
			header : '检查人',
			sortabel : true,
			dataIndex : 'checkName'
		},{
			header : '检查时间',
			sortabel : true,
			dataIndex : 'checkDate'
		},{
			header : '审核状态',
			sortabel : true,
			dataIndex : 'check.approveStatus',
			renderer : function(v)
			{
				if(v == '0')
					return '未上报';
				if(v == '1')
					return '已上报';
				if(v == '2')
					return '已审核';
				if(v == '3')
					return '已退回';
			}
		}],
		//分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : cheStore,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	})
	
	
	chGrid.on('click',function(){chesm.getSelected()
		if(chGrid.getSelectionModel().hasSelection())
		{
			var record = chGrid.getSelectionModel().getSelected();
			if(record.get('check.isProblem') == 'Y')
			{
				flagCheckupId = record.get('check.checkupId');
				amendStore.load({
					params : {
						checkupId : flagCheckupId
					}
				})
			}
			else
			{
				amendStore.removeAll();
			}
				
		}
	})
	// ************检查grid结束***************
	
	
	
	
	
	function treeClick(node, e) {
		// 提示修改是否保存
		e.stopEvent();
		currentNode = node;
		node.toggle();
		if (currentNode.id != 0) {
			var temp = currentNode.id.length;
			
			if (temp == 6) {
				flagMeasure = currentNode.id;
				flagName = currentNode.text.substring(6);
				cheStore.load({
				params : {
								measureCode : flagMeasure,
								start : 0,
								limit : 18
							}
						})
				year.setValue(null);
				sea.setValue(null);
				status.setValue('');
				specialCode.setValue(null);
				checkBy.setValue(null);
				checkName.setValue(null)
				
			} else {
				flagMeasure = null;
				flagName = null;
				
				cheStore.removeAll()
				amendStore.removeAll();
			}
		} else {
			flagMeasure = null;
			flagName = null;
			
			cheStore.removeAll();
			amendStore.removeAll();
		}
	};

	function personSelect() {
		var args = {
			selectModel : 'signal',
			rootNode : {
				id : '-1',
				text : '灞桥电厂'
			}
		}
		var person = window
				.showModalDialog(
						'../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(person) != "undefined") {
			return person;
		} else {
			return null;
		}
	};
	


	
	
	
	// 整改
	
	
	var detailRec = Ext.data.Record.create([{
		name : 'amend.amendId'
	}, {
		name : 'amend.checkupId'
	}, {
		name : 'amend.existProblem'
	}, {
		name : 'amend.amendMeasure'
	}, {
		name : 'amend.beforeAmendMeasure'
	}, {
		name : 'amend.chargeDept'
	}, {
		name : 'amend.chargeBy'
	}, {
		name : 'amend.superviseDept'
	}, {
		name : 'amend.superviseBy'
	}, {
		name : 'amend.noAmendReason'
	}, {
		name : 'amend.problemKind'
	}, {
		name : 'amend.modifyBy'
	}, {
		name : 'planFinishDate'
	}, {
		name : 'amendFinishDate'
	}, {
		name : 'chargeDeptName'
	},{
		name : 'chargeName'
	},{
		name : 'superviseDeptName'
	},{
		name : 'superviseName'
	},{
		name : 'modifyName'
	},{
		name : 'modifyDate'
	}]);
	// 检查store 
	var amendStore = new Ext.data.JsonStore({
		url : 'security/getSafeAmendList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : detailRec
	})
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	});
	var cm = new Ext.grid.ColumnModel([sm, new Ext.grid.RowNumberer({
		header : '行号',
		width : 32
	}), {
		header : '存在的问题',
		dataIndex : 'amend.existProblem',
		sortable : true
		}, {
		header : '整改措施',
		dataIndex : 'amend.amendMeasure',
		sortable : true
		}, {
		header : '整改前的防范措施',
		dataIndex : 'amend.beforeAmendMeasure',
		sortable : true
		}, {
		header : '计划完成时间',
		dataIndex : 'planFinishDate',
		sortable : true
		}, {
		header : '整改完成时间',
		dataIndex : 'amendFinishDate',
		sortable : true
		}, {
		header : '整改责任部门',
		dataIndex : 'chargeDeptName',
		sortable : true
		}, {
		header : '整改责任人',
		dataIndex : 'chargeName',
		sortable : true
		}, {
		header : '整改监督部门',
		dataIndex : 'superviseDeptName',
		sortable : true
		}, {
		header : '整改监督人',
		dataIndex : 'superviseName',
		sortable : true
		}, {
		header : '未整改原因',
		dataIndex : 'amend.noAmendReason',
		sortable : true
		}, {
		header : '问题性质',
		dataIndex : 'amend.problemKind',
		sortable : true,
		renderer : function(v){
			if(v == '1')
				return '一般问题';
			else if(v == '2')
				return '重大问题';
		}
		}
	]);

	var grid = new Ext.grid.GridPanel({
		store : amendStore,
		cm : cm,
		sm : sm,
		autoScroll : true,
		frame : false,
		border : false
	});

	//************* 整改formPanel开始**********************
	//************* 整改formPanel结束**********************
	
	
	
	var layout = new Ext.Viewport({
		layout : "border",
		border : false,
		items : [{
			title : '25项反措动态检查',
			region : "center",
			split : true,
			collapsible : false,
			titleCollapse : false,
			margins : '1',
			layout : 'border',
			items : [{
				region : "north",
				split : true,
				collapsible : false,
				titleCollapse : true,
				margins : '1,1,1,1',
				height : 340,
				layout : 'fit',
				items : [chGrid]
			}, {
				region : "center",
				title : '整改措施',
				layoutConfig : {
					animate : true
				},				
				border : false,
				layout : 'fit',
				items : [grid]
			}]
		}, {
			region : 'west',
			split : true,
			collapsible : true,
			titleCollapse : true,
			margins : '1',
			width : 220,
			layoutConfig : {
				animate : true
			},
			layout : 'fit',
			items : [treePanel]
		}]
	});
	
})