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
	getWorkCode()
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
			dataUrl : "security/findByParentCode.action?check=1"
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
	
	cheStore.on('load',function(){
		if(cheStore.getTotalCount() > 0)
		{
			Ext.getCmp("btnReport").setDisabled(false);
			Ext.getCmp("btnUdate").setDisabled(false);
			Ext.getCmp("btnDelete").setDisabled(false);
		}
	})
	var chesm = new Ext.grid.CheckboxSelectionModel({
	})
	// 年份
	var year = new Ext.form.TextField({
		id : 'year',
		name : 'year',
		style : 'cursor:pointer',
		readOnly : true,
		width : 80,
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
		width : 80,
		mode : 'local',
		triggerAction : 'all',
		value : getSeason()
	})
	// 反事故措施
	var tbar1 = new Ext.Toolbar({
		items : ['年度：',year,'季度：',sea,{id : 'btnQuery',
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
					status : '03',
					season : season,
					start : 0,
					limit : 18
				}
			})
		}},{
			id : 'btnAdd',
			text : "新增",
			iconCls : 'add',
			handler : function() {
				if(flagMeasure == null)
				{
					Ext.Msg.alert('提示','请先选择一项措施！');
					return;
				}
				win.show();
				measureCode.setValue(flagMeasure);
				measureName.setValue(flagName);
			}
		}, '-',{
			id : 'btnUdate',
			text : '修改',
			iconCls : 'update',
			disabled : true,
			handler : checkUpdate
		},'-', {
			id : 'btnDelete',
			text : "删除",
			iconCls : 'delete',
			disabled : true,
			handler : function() {				
				var recs = chGrid.getSelectionModel().getSelections();
						if (recs.length > 0) {
							for (var i = 0; i < recs.length; i++) {
								ids.push(recs[i].get('check.checkupId'))
							}
							Ext.Msg.confirm("删除", "是否确定删除该记录？", function(
									buttonobj) {
								if (buttonobj == "yes") {
									Ext.Ajax.request({
												method : 'POST',
												url : 'security/deleteCheckup.action',
												params : {
													ids : ids.join(',')
												},
												success : function(reuslt,
														action) {
													Ext.Msg.alert('提示',
															'数据删除成功！');
													cheStore.reload();
													amendStore.removeAll();
													ids = [];
												},
												failure : function() {
													Ext.Msg.alert('错误',
															'删除时出现未知错误.');
												}
											})

								}
							})

						} else {
							Ext.Msg.alert('提示', '请先选择要删除的数据！');
							return;
						}

					}
		},'-',{
			id : 'btnReport',
			text : '上报',
			iconCls : 'report',
			disabled : true,
			handler : function(){
				var recs = chGrid.getSelectionModel().getSelections();
				if (recs.length > 0) {
					if(recs.length != 1)
					{
						Ext.Msg.alert('提示','请选择其中一项');
						return;
					}
					else 
					{
						Ext.Msg.confirm('提示','确认要上报吗？',function(button){
							if(button == 'yes')
							{
								var rec = chGrid.getSelectionModel().getSelected();
								Ext.Ajax.request({
									method : 'POST',
									url : 'security/reportCheckup.action',
									params : {
										id : rec.get('check.checkupId')
									},
									success : function(){
										Ext.Msg.alert('提示','数据上报成功！');
									},
									failure : function(){
										Ext.Msg.alert('提示','出现未知错误！')
									}
								})
							}
						})
						
						
					}
				}
				else
				{
					Ext.Msg.alert('提示', '请先选择要上报的数据！');
					return;
				}
			}
		}]
	});
	
	// 检查修改
	function checkUpdate()
	{
		var recs = chGrid.getSelectionModel().getSelections();
				if (recs.length > 0) {
					if(recs.length != 1)
					{
						Ext.Msg.alert('提示','请选择其中一项');
						return;
					}
					else 
					{
						flagMe = 'update';
						var rec = chGrid.getSelectionModel().getSelected();
						win.setTitle('修改检查信息')
						win.show();
						var isP = rec.get('check.isProblem')
						if(isP == 'Y')
						{
							yes.setValue(true)
							no.setValue(false)
						}
						else
						{
							yes.setValue(false);
							no.setValue(true)
						}
						var yeSe = rec.get('check.season')
						var ye = yeSe.substring(0,4);
						var se = yeSe.substring(4,5);
						seasonyear.setValue(ye);
						season.setValue(se);
						form.getForm().loadRecord(rec);
					}
				}
				else
				{
					Ext.Msg.alert('提示', '请先选择要修改的数据！');
					return;
				}
	}
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
				Ext.getCmp("add").setDisabled(false);
				amendStore.load({
					params : {
						checkupId : flagCheckupId
					}
				})
			}
			else
			{
				amendStore.removeAll();
				Ext.getCmp("add").setDisabled(true);
				Ext.getCmp("delete").setDisabled(true);
				Ext.getCmp("update").setDisabled(true);
			}
				
		}
	})
	
	chGrid.on('dblclick',function(){
		chGrid.getSelectionModel().hasSelection()
		{
			checkUpdate()	
		}
	})
	// ************检查grid结束***************
	
	
	
	
	// *********formPanel开始
	// 检查ID
	var checkupId = new Ext.form.Hidden({
		id : 'checkupId',
		name : 'check.checkupId'
	})
	//措施编号
	var measureCode = new Ext.form.Hidden({
		id : 'measureCode',
		name : 'check.measureCode'
	})
	//措施名称
	var measureName = new Ext.form.TextField({
		id : 'measureName',
		name : 'measureName',
		fieldLabel : '措施名称:',
		labelSeparator : '',
//		allowBlank : false,
		readOnly : true,
		width : 340
	});
	// 是否存在问题
	var yes = new Ext.form.Radio({
		id : 'yes',
		name : 'isProblem',
		fieldLabel : '存在问题:',
		labelSeparator : '',
		boxLabel : '是'
	})
	yes.on('change',function(){
		if(yes.getValue() == true)
			isProblem = "Y";
		else 
			isProblem = "N";
	})
	var no = new Ext.form.Radio({
		id : 'no',
		name : 'isProblem',
		boxLabel : '否',
		checked : true,
		labelSeparator : ''
	})
	// 检查人
	var checkName = new Ext.form.TextField({
		fieldLabel : "检查人:",
		labelSeparator : '',
//		allowBlank : false,
		id : 'checkName',
		name : 'checkName',
		disabled : true,
		readOnly : true
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
	// 检查时间
	var checkDate = new Ext.form.TextField({
		id : 'checkDate',
		name : 'checkDate',
		style : 'cursor:pointer',
		labelSeparator : '',
		fieldLabel : '检查时间：',
//		readOnly : true,
		value : getDate(),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d',
					alwaysUseStartDate : false,
					dateFmt : 'yyyy-MM-dd',
					onpicked : function() {
					},
					onclearing : function() {
					}
				});
			}
		}
	})
	//  审核人
//	var approveName = new Ext.form.TextField({
//		fieldLabel : "审核人",
////		allowBlank : false,
//		id : 'approveName',
//		name : 'approveName',
//		editable : false,
//		disabled : true,
//		readOnly : true
//	})
//	approveName.onClick(function() {
//			var ps = personSelect();
//			if (ps != null) {
//				approveBy.setValue(ps.workerCode);
//				approveName.setValue(ps.workerName);
//			}
//		})
//	var approveBy = new Ext.form.Hidden({
//		id : 'approveBy',
//		name : 'check.approveBy'
//	})
	
	// 审核时间
//	var approveDate = new Ext.form.TextField({
//		id : 'approveDate',
//		name : 'approveDate',
//		style : 'cursor:pointer',
//		readOnly : true,
//		width : 80,
//		value : getDate(),
//		listeners : {
//			focus : function() {
//				WdatePicker({
//					startDate : '%y-%M-%d',
//					alwaysUseStartDate : false,
//					dateFmt : 'yyyy-MM-dd',
//					onpicked : function() {
//					},
//					onclearing : function() {
//					}
//				});
//			}
//		}
//	})
	// 年份
	var seasonyear = new Ext.form.TextField({
		id : 'seasonyear',
		name : 'seasonyear',
		style : 'cursor:pointer',
		readOnly : true,
		width : 60,
		value : getYear(),
		fieldLabel : '季度:',
		labelSeparator : '',
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
	var season = new Ext.form.ComboBox({
		id : 'season',
		name : 'season',
		store : seStore,
		valueField : 'id',
		displayField : 'name',
		width : 60,
		mode : 'local',
		triggerAction : 'all',
		value : getSeason(),
		labelSeparator : ''
	})
	
	// 修改人
	var modifyBy = new Ext.form.Hidden({
		id : 'modifyBy',
		name : 'check.modifyBy'
	})
	var modifyName = new Ext.form.Hidden({
		id : '修改人',
		name : 'modifyName',
		fieldLabel : '修改人',
		readOnly : true
	})
	// 修改时间
	var modifyDate = new Ext.form.Hidden({
		id : 'modifyDate',
		name : 'modifyDate',
		fieldLabel : '修改时间',
		value : getDate()
	})
	var form = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 5px 5px",
		labelAlign : 'right',
		autoHeight : true,
		layout : 'column',
		region : 'center',
		border : false,
		items : [{
			border : false,
			layout : 'form',
			columnWidth : 1,
			labelWidth : 60,
			items : [checkupId,measureCode,measureName]
		}, {
			border : false,
			layout : 'form',
			columnWidth : .3,
			labelWidth : 60,
			items : [yes]
		}, {
			border : false,
			layout : 'form',
			columnWidth : .2,
			labelWidth : 5,
			items : [no]
		}, {
			border : false,
			layout : 'form',
			columnWidth : .5,
			labelWidth : 60,
			items : [checkName,checkBy]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 0.5,
			labelWidth : 60,
			items : [checkDate]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 0.3,
			labelWidth : 60,
			items : [seasonyear]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 0.2,
			labelWidth : 1,
			items : [season]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 0.5,
			labelWidth : 60,
			items : [modifyBy,modifyName,modifyDate]
		}]
	});
	
	var win = new Ext.Window({
		height : 170,
		width : 450,
		layout : 'fit',
		resizable : false,
		closeAction : 'hide',
		modal : true,
		items : [form],
		buttonAlign : "center",
		title : '新增检查信息',
		buttons : [{
			text : "保存",
			iconCls : 'save',
			handler : function() {
				if(checkBy.getValue() == null || checkBy.getValue() == '')
				{
					Ext.Msg.alert('提示','检查人不能为空！');
					return;
				}
				if(checkDate.getValue() == null || checkDate.getValue() == '')
				{
					Ext.Msg.alert('提示','检查不能为空！');
					return;
				}
				if(seasonyear.getValue() == null || seasonyear.getValue() == ''
					|| season.getValue() == null || season.getValue() == '')
				{
					Ext.Msg.alert('提示','请选择季度！');
					return;
				}
				Ext.Msg.confirm('提示','确认要保存吗？',function(button){
					if(button == 'yes')
					{
						var myurl = '';
						if(flagMe == 'add')
							myurl = 'security/saveCheckup.action'
						else if(flagMe == 'update')
							myurl = 'security/updateCheckup.action'
						form.getForm().submit({
							mothod : 'POST',
							url : myurl,
							params : {
								checkDate : checkDate.getValue(),
								season : seasonyear.getValue() + season.getValue(),
								isProblem : isProblem
							},
							success : function(forma,action){
								var o = eval("(" + action.response.responseText + ")");
								Ext.Msg.alert('提示',o.msg);
								if (o.msg.indexOf('存在') == -1) {
											win.hide();
											cheStore.reload();
											flagMe = 'add';
										}
								
							},
							failure : function(){
								Ext.Msg.alert('错误', '出现未知错误.');
							}
							})
					}
				})
			}
		}, {
			text : "取消",
			iconCls : 'canse',
			handler : function() {
				win.hide();
			}
		}]
	});
	
	function treeClick(node, e) {
		// 提示修改是否保存
		e.stopEvent();
		currentNode = node;
		node.toggle();
		if (currentNode.id != 0) {
			var temp = currentNode.id.length;
			
			if (temp == 6) {
				flagMeasure = currentNode.text.substring(0,6);
				flagName = currentNode.text.substring(6);
				cheStore.load({
				params : {
								status : '03',
								measureCode : flagMeasure,
								start : 0,
								limit : 18
							}
						})
				year.setValue(null);
				sea.setValue(null);
				Ext.getCmp("btnReport").setDisabled(false);
				Ext.getCmp("btnAdd").setDisabled(false);
				Ext.getCmp("btnUdate").setDisabled(false);
				Ext.getCmp("btnDelete").setDisabled(false);
				
				
			} else {
				flagMeasure = null;
				flagName = null;
				
				Ext.getCmp("btnAdd").setDisabled(true);
				Ext.getCmp("btnDelete").setDisabled(true);
				Ext.getCmp("btnReport").setDisabled(true);
				Ext.getCmp("btnUdate").setDisabled(true);
		
				
				cheStore.removeAll()
				amendStore.removeAll();
			}
		} else {
			flagMeasure = null;
			flagName = null;
			
			Ext.getCmp("btnAdd").setDisabled(true);
			Ext.getCmp("btnUdate").setDisabled(true);			
			Ext.getCmp("btnDelete").setDisabled(true);
			Ext.getCmp("btnReport").setDisabled(true);
			
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
	

	function deptSelect() {
		var args = {
			selectModel : 'single',
			rootNode : {
				id : '0',
				text : '灞桥电厂'
			},
			onlyLeaf : false
		};
		var dept = window
				.showModalDialog(
						'../../../comm/jsp/hr/dept/dept.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(dept) != "undefined") {
			return dept;
		} else {
			return null;
		}
	};

	
	
	
	// 整改
	var tbar2 = new Ext.Toolbar({
		items : [{
			id : 'add',
			text : "新增",
			iconCls : 'add',
			disabled : true,
			handler : function() {
				amendMe = 'add';
				amendWin.show();
				amendForm.getForm().reset();
				amendCheckupId.setValue(flagCheckupId);
				amendWin.setTitle('新增整改信息')
			}
		},{
			id : 'update',
			text : "修改",
			iconCls : 'update',
			disabled : true,
			handler : amendUpdate
		}, '-', {
			id : 'delete',
			text : "删除",
			disabled : true,
			iconCls : 'delete',
			handler : function() {
				var selected = grid.getSelectionModel().getSelections();
				if (selected.length == 0) {
					Ext.Msg.alert("提示", "请选择要删除的记录！");
				} else {
					for (var i = 0; i < selected.length; i += 1) {
						var member = selected[i];
						if (member.get("amend.amendId") != null
								&& member.get("amend.amendId") != "") {
							ids.push(member.get("amend.amendId"));
						}
					}
					if (ids.length > 0) {
						Ext.Msg.confirm("删除", "是否确定删除记录？", function(buttonobj) {
							if (buttonobj == "yes") {
								Ext.Ajax.request({
									url : 'security/deleteAmend.action',
									method : 'post',
									params : {
										ids : ids.join(',')
									},
									success : function(result, request) {
										 Ext.Msg.alert('提示', '数据删除成功！');
										 amendStore.reload();
									},
									failure : function(result, request) {
										Ext.Msg.alert('提示', '操作失败！');
									}
								})
							}
						})
					}
				}

			}
		}]
	});
	
	// 整改的修改方法
	function amendUpdate() {
		var selections = grid.getSelectionModel().getSelections();
		if (selections.length == 0) {
			Ext.Msg.alert("提示", "请选择要修改的记录！");
			return;
		} else if (selections.length != 1) {
			Ext.Msg.alert('提示', '请选择其中一项！');
			return;
		} else {
			amendMe = 'update';
			var selected = grid.getSelectionModel().getSelected();
			amendWin.show();
			amendForm.getForm().loadRecord(selected);
			planFinishDate.setValue(selected.get('planFinishDate'));
			amendFinishDate.setValue(selected.get('amendFinishDate'))
			amendWin.setTitle('修改整改信息');
		}
	}
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
	amendStore.on('load',function(){
		if(amendStore.getTotalCount() > 0)
		{
			Ext.getCmp("delete").setDisabled(false);
			Ext.getCmp("update").setDisabled(false);
		}
		else
		{
			Ext.getCmp("delete").setDisabled(true);
			Ext.getCmp("update").setDisabled(true);
		}
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
		tbar : tbar2,	
		autoScroll : true,
		frame : false,
		border : false
	});
	grid.on('dblclick',function(){
		if(grid.getSelectionModel().hasSelection())
			amendUpdate();
	})
	//************* 整改formPanel开始**********************
	// 整改ID 
	var amendId = new Ext.form.Hidden({
		id : 'amendId',
		name : 'amend.amendId'
	})
	// 检查ID 
	var amendCheckupId = new Ext.form.Hidden({
		id : 'checkupId',
		name : 'amend.checkupId',
		value : flagCheckupId
	})
	// 存在的问题
	var existProblem = new Ext.form.TextField({
		id : 'existProblem',
		name : 'amend.existProblem',
		fieldLabel : '存在的问题',
		allowBlank : false,
		width : 340
	})
	//整改措施
	var amendMeasure = new Ext.form.TextArea({
		id : 'amendMeasure',
		name : 'amend.amendMeasure',
		fieldLabel : '整改措施',
		width : 340,
		height : 50
	});
	//整改前的防范措施
	var beforeAmendMeasure = new Ext.form.TextArea({
		id : 'beforeAmendMeasure',
		name : 'amend.beforeAmendMeasure',
		fieldLabel : '防范措施',
		width : 340,
		height : 50
	});
	// 计划完成时间
	var planFinishDate = new Ext.form.TextField({
				id : 'planFinishDate',
				name : 'planFinishDate',
				style : 'cursor:pointer',
				fieldLabel : '计划完成时间',
				readOnly : true,
				value : getDate(),
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									alwaysUseStartDate : false,
									dateFmt : 'yyyy-MM-dd',
									onpicked : function() {
									},
									onclearing : function() {
									}
								});
					}
				}
			})
	// 整改完成时间
	var amendFinishDate = new Ext.form.TextField({
				id : 'amendFinishDate',
				name : 'amendFinishDate',
				style : 'cursor:pointer',
				fieldLabel : '整改完成时间',
				readOnly : true,
//				value : getDate(),
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									alwaysUseStartDate : false,
									dateFmt : 'yyyy-MM-dd',
									onpicked : function() {
									},
									onclearing : function() {
									}
								});
					}
				}
			})
	// 整改责任部门
	var amendChargeDept = new Ext.form.Hidden({
		id : 'amendChargeDept',
		name : 'amend.chargeDept'
	})
	var amendChargeDeptName = new Ext.form.TextField({
		id : 'amendChargeDeptName',
		name : 'chargeDeptName',
		fieldLabel : '整改责任部门',
		readOnly : true
	})
	//整改责任人
	var  amendChargeBy　= new Ext.form.Hidden({
		id : 'amendChargeBy',
		name : 'amend.chargeBy'
	})
	var amendChargeName = new Ext.form.TextField({
		id : 'amendChargeName',
		name : 'chargeName',
		fieldLabel : '整改责任人',
		readOnly : true
	})
	amendChargeName.onClick(function() {
			var ps = personSelect();
			if (ps != null) {
				amendChargeBy.setValue(ps.workerCode);
				amendChargeName.setValue(ps.workerName);
				amendChargeDept.setValue(ps.deptCode)
				amendChargeDeptName.setValue(ps.deptName);
			}
		});
	// 整改监督部门
	var amendSuperviseDept = new Ext.form.Hidden({
		id : 'amendSuperviseDept',
		name : 'amend.superviseDept'
	})
	var amendSuperviseDeptName = new Ext.form.TextField({
		id : 'amendSuperviseDeptName',
		name : 'superviseDeptName',
		fieldLabel : '整改监督部门',
		readOnly : true
	})
	//整改监督人
	var  amendSuperviseBy　= new Ext.form.Hidden({
		id : 'amendSuperviseBy',
		name : 'amend.superviseBy'
	})
	var amendSuperviseName = new Ext.form.TextField({
		id : 'amendSuperviseName',
		name : 'superviseName',
		fieldLabel : '整改监督人',
		readOnly : true
	})
	amendSuperviseName.onClick(function() {
			var ps = personSelect();
			if (ps != null) {
				amendSuperviseBy.setValue(ps.workerCode);
				amendSuperviseName.setValue(ps.workerName);
				amendSuperviseDept.setValue(ps.deptCode)
				amendSuperviseDeptName.setValue(ps.deptName);
			}
		});
	
	//未整改原因 
	var noAmendReason = new Ext.form.TextArea({
		id : 'noAmendReason',
		name : 'amend.noAmendReason',
		fieldLabel : '未整改原因',
		width : 340
	});
	
	// 问题性质
	// 季度
	var kindStore = new Ext.data.SimpleStore({
		fields : ['id','name'],
		data : [['',''],['1','一般问题'],['2','重大问题']]
	})
	var problemKind = new Ext.form.ComboBox({
		id : 'problemKind',
		hiddenName : 'amend.problemKind',
		store : kindStore,
		fieldLabel : '问题性质',
		valueField : 'id',
		displayField : 'name',
		mode : 'local',
		triggerAction : 'all'
	})
	
	var amendForm = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 5px 5px",
		labelAlign : 'right',
		autoHeight : true,
		layout : 'column',
		region : 'center',
		border : false,
		items : [{
			border : false,
			layout : 'form',
			columnWidth : 1,
			labelWidth : 80,
			items : [amendId,amendCheckupId,existProblem]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 1,
			labelWidth : 80,
			items : [amendMeasure]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 1,
			labelWidth : 80,
			items : [beforeAmendMeasure]
		}, {
			border : false,
			layout : 'form',
			columnWidth : .5,
			labelWidth : 80,
			items : [planFinishDate]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 0.5,
			labelWidth : 80,
			items : [amendFinishDate]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 0.5,
			labelWidth : 80,
			items : [amendChargeBy,amendChargeName]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 0.5,
			labelWidth : 80,
			items : [amendChargeDept,amendChargeDeptName]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 0.5,
			labelWidth : 80,
			items : [amendSuperviseBy,amendSuperviseName]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 0.5,
			labelWidth : 80,
			items : [amendSuperviseDept,amendSuperviseDeptName]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 1,
			labelWidth : 80,
			items : [noAmendReason]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 1,
			labelWidth : 80,
			items : [problemKind]
		}]
	});
	
	var amendWin = new Ext.Window({
		height : 410,
		width : 450,
		layout : 'fit',
		resizable : false,
		closeAction : 'hide',
		modal : true,
		items : [amendForm],
		buttonAlign : "center",
		title : '新增整改信息',
		buttons : [{
			text : "保存",
			iconCls : 'save',
			handler : function() {
				if(existProblem.getValue() == null || existProblem.getValue() == '')
				{
					Ext.Msg.alert('提示','存在的问题不能为空！');
					return;
				}
				if(planFinishDate.getValue() == null || planFinishDate.getValue() == '')
				{
					Ext.Msg.alert('提示','计划完成时间不能为空！');
					return;
				}
//				if(amendFinishDate.getValue() == null || amendFinishDate.getValue() == '')
//				{
//					Ext.Msg.alert('提示','整改完成时间不能为空！');
//					return;
//				}
				Ext.Msg.confirm('提示','确认要保存吗？',function(button){
					if(button == 'yes')
					{
						var myurl = '';
						if(amendMe == 'add')
							myurl = 'security/saveAmend.action'
						else if(amendMe == 'update')
							myurl = 'security/updateAmend.action'
						amendForm.getForm().submit({
							mothod : 'POST',
							url : myurl,
							params : {
								planFinishDate : planFinishDate.getValue(),
								amendFinishDate : amendFinishDate.getValue()
							},
							success : function(form,action){
								Ext.Msg.alert('提示','数据保存成功！');
								amendWin.hide();
//								cheStore.reload();
								amendStore.reload();
								amendMe = 'add';
							},
							failure : function(){
								Ext.Msg.alert('错误', '出现未知错误.');
							}
							})
					}
				})
			}
		}, {
			text : "取消",
			iconCls : 'canse',
			handler : function() {
				amendWin.hide();
			}
		}]
	});
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