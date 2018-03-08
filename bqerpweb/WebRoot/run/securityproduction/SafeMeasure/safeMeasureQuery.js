Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result.workerCode) {
					// 设定工作负责人为登录人
					entryBy.setValue(result.workerCode);
					entryName.setValue(result.workerName);
					entryDept.setValue(result.deptCode);
					entryDeptName.setValue(result.deptName)
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
			dataUrl : "security/findByParentCode.action"
		})
	});
	treePanel.setRootNode(rootNode);
	treePanel.on("click", treeClick);
	rootNode.select();
	rootNode.expand();
	
	
	// 反事故措施详细
	var obj2 = Ext.data.Record.create([{
		name : 'detailsId'
	}, {
		name : 'measureCode'
	}, {
		name : 'content'
	}]);
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) {
			}
		}
	});
	var cm = new Ext.grid.ColumnModel([sm, new Ext.grid.RowNumberer({
		header : '行号',
		width : 35,
		hidden : true,
		sortable : false,
		align : 'left'
	}), {
		header : '内容',
		dataIndex : 'content',
		sortable : false,
		renderer : function(v) {
			return "<div style='white-space:normal;'>" + v + "</div>";
		},
		width : 400,
		editor : new Ext.form.TextField({
			listeners : {
				"render" : function() {
					this.el.on("dblclick", function() {
						var record = grid.getSelectionModel().getSelected();
						var value = record.get('content');
						contentText.setValue(value);
						win.show();
					})
				}
			}
		})
	}

	]);
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'security/findByMeasureCode.action'
		}),
		reader : new Ext.data.JsonReader({
				}, obj2)
	});
	var grid = new Ext.grid.EditorGridPanel({
		store : ds,
		cm : cm,
		sm : sm,
//		tbar : [{text : ' '}],
		viewConfig : {
			forceFit : true
		},
		autoScroll : true,
		frame : false,
		border : false,
		clicksToEdit : 1
	});
	//措施编号
	var measureCode = new Ext.form.Hidden({
		id : 'measureCode',
		name : 'model.measureCode'
	})
	//措施名称
	var measureName = new Ext.form.TextArea({
		id : 'measureName',
		name : 'model.measureName',
		fieldLabel : '措施名称',
//		allowBlank : false,
		readOnly : false,
		width : 390,
		height : 40
	});
	
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
		disabled : true,
		width : 125
	})
//	// 专业编码
//	var specialCode = new Ext.form.Hidden({
//		id : 'specialCode',
//		name : 'model.specialCode'
//	})
//	//专业编码 名
//	var specialName = new Ext.form.TextField({
//		id : 'specialName',
//		name : 'specialName',
//		fieldLabel : '专业编码',
////		allowBlank : false,
//		readOnly : true
//	});
	// 责任领导（发电企业）
	var fdDutyLeaderName = new Ext.form.TextField({
		fieldLabel : "责任领导",
//		allowBlank : false,
		id : 'fdDutyLeaderName',
		name : 'fdDutyLeaderName',
		editable : false,
		disabled : true,
		readOnly : true
	})
	fdDutyLeaderName.onClick(function() {
			var ps = personSelect();
			if (ps != null) {
				fdDutyLeader.setValue(ps.workerCode);
				fdDutyLeaderName.setValue(ps.workerName);
			}
		});
	var fdDutyLeader = new Ext.form.Hidden({
		id : 'fdDutyLeader',
		name : 'model.fdDutyLeader'
	})
	
	//  管理责任人（发电企业）
	var fdManagerName = new Ext.form.TextField({
		fieldLabel : "管理责任人",
//		allowBlank : false,
		id : 'fdManagerName',
		name : 'fdManagerName',
		editable : false,
		disabled : true,
		readOnly : true
	})
	fdManagerName.onClick(function() {
			var ps = personSelect();
			if (ps != null) {
				fdManager.setValue(ps.workerCode);
				fdManagerName.setValue(ps.workerName);
			}
		})
	var fdManager = new Ext.form.Hidden({
		id : 'fdManager',
		name : 'model.fdManager'
	})
	
	// 技术责任人（发电企业）
	var fdTechnologyName = new Ext.form.TextField({
		fieldLabel : "技术责任人",
//		allowBlank : false,
		id : 'fdTechnologyName',
		name : 'fdTechnologyName',
		editable : false,
		disabled : true,
		readOnly : true
	})
	fdTechnologyName.onClick(function() {
			var ps = personSelect();
			if (ps != null) {
				fdTechnologyBy.setValue(ps.workerCode);
				fdTechnologyName.setValue(ps.workerName);
			}
		});
	var fdTechnologyBy = new Ext.form.Hidden({
		id : 'fdTechnologyBy',
		name : 'model.fdTechnologyBy'
	})
	
	// 监督责任人（发电企业）
	var fdSuperviseName = new Ext.form.TextField({
		fieldLabel : "责任领导",
//		allowBlank : false,
		id : 'fdSuperviseName',
		name : 'fdSuperviseName',
		editable : false,
		disabled : true,
		readOnly : true 
	})
	fdSuperviseName.onClick(function() {
			var ps = personSelect();
			if (ps != null) {
				fdSuperviseBy.setValue(ps.workerCode);
				fdSuperviseName.setValue(ps.workerName);
			}
		});
	var fdSuperviseBy = new Ext.form.Hidden({
		id : 'fdSuperviseBy',
		name : 'model.fdSuperviseBy'
	})
	
	// 责任领导（大唐陕西）
	var dtDutyLeaderName = new Ext.form.TextField({
		fieldLabel : "责任领导",
//		allowBlank : false,
		id : 'dtDutyLeaderName',
		name : 'dtDutyLeaderName',
		editable : false,
		disabled : true,
		readOnly : true
	})
	dtDutyLeaderName.onClick(function() {
			var ps = empSelect();
			if (ps != null) {
				dtDutyLeader.setValue(ps.empId);
				dtDutyLeaderName.setValue(ps.empName);
			}
		});
	var dtDutyLeader = new Ext.form.Hidden({
		id : 'dtDutyLeader',
		name : 'model.dtDutyLeader'
	})
	
	// 责任领导（大唐陕西）
	var dtDutyLeaderName = new Ext.form.TextField({
		fieldLabel : "责任领导",
//		allowBlank : false,
		id : 'dtDutyLeaderName',
		name : 'dtDutyLeaderName',
		editable : false,
		disabled : true,
		readOnly : true 
	})
	dtDutyLeaderName.onClick(function() {
			var ps = empSelect();
			if (ps != null) {
				dtDutyLeader.setValue(ps.empId);
				dtDutyLeaderName.setValue(ps.empName);
			}
		});
	var dtDutyLeader = new Ext.form.Hidden({
		id : 'dtDutyLeader',
		name : 'model.dtDutyLeader'
	})
	// 管理责任人（大唐陕西）
	var dtManagerName = new Ext.form.TextField({
		fieldLabel : "管理责任人",
//		allowBlank : false,
		id : 'dtManagerName',
		name : 'dtManagerName',
		editable : false,
		disabled : true,
		readOnly : true 
	})
	dtManagerName.onClick(function() {
			var ps = empSelect();
			if (ps != null) {
				dtManager.setValue(ps.empId);
				dtManagerName.setValue(ps.empName);
			}
		});
	var dtManager = new Ext.form.Hidden({
		id : 'dtManager',
		name : 'model.dtManager'
	})
	// 技术责任人（大唐陕西）
	var dtTechnologyName = new Ext.form.TextField({
		fieldLabel : "技术责任人",
//		allowBlank : false,
		id : 'dtTechnologyName',
		name : 'dtTechnologyName',
		editable : false,
		disabled : true,
		readOnly : true
	})
	dtTechnologyName.onClick(function() {
			var ps = empSelect();
			if (ps != null) {
				dtTechnologyBy.setValue(ps.empId);
				dtTechnologyName.setValue(ps.empName);
			}
		});
	var dtTechnologyBy = new Ext.form.Hidden({
		id : 'dtTechnologyBy',
		name : 'model.dtTechnologyBy'
	})
	// 监督责任人（大唐陕西）
	var dtSuperviseName = new Ext.form.TextField({
		fieldLabel : "责任领导",
//		allowBlank : false,
		id : 'dtSuperviseName',
		name : 'dtSuperviseName',
		editable : false,
		disabled : true,
		readOnly : true
	})
	dtSuperviseName.onClick(function() {
			var ps = empSelect();
			if (ps != null) {
				dtSuperviseBy.setValue(ps.empId);
				dtSuperviseName.setValue(ps.empName);
			}
		});
	var dtSuperviseBy = new Ext.form.Hidden({
		id : 'dtSuperviseBy',
		name : 'model.dtSuperviseBy'
	})
	
	//修改人
	var entryBy = new Ext.form.Hidden({
		id : 'entryBy',
		name : 'model.entryBy'
	})
	var entryName = new Ext.form.TextField({
		id : 'entryName',
		name : 'entryName',
		fieldLabel : '修改人',
		readOnly : true
	})
	//修改部门
	var entryDept = new Ext.form.Hidden({
		id : 'entryDept',
		name : 'model.entryDept'
	})
	var entryDeptName = new Ext.form.TextField({
		id : 'entryDeptName',
		name : 'entryDeptName',
		fieldLabel : '修改部门',
		readOnly : true
	})
	// 修改时间
	var entryDateString = new Ext.form.TextField({
		fieldLabel : '修改时间',
		id : 'entryDateString',
		name : 'entryDateString',
		readOnly : true,
		value : getDate()
	})
	var memo = new Ext.form.TextArea({
		id : 'memo',
		name : 'model.memo',
		fieldLabel : '备注',
		readOnly : false,		
		width : 390,
		height : 50
	});
	
	// 发电企业标签
	var label1 = new Ext.form.Label({
				text : '发电企业',
				border : false,
				width : 110
			})
	// 大唐陕西标签
	var label2 = new Ext.form.Label({
				text : '大唐陕西',
				border : false,
				width : 110
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
			labelWidth : 70,
			items : [measureCode,measureName]
		}, {
			border : false,
			layout : 'form',
			columnWidth : .5,
			labelWidth : 70,
			items : [specialCode]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 0.5,
			labelWidth : 70,
			items : [entryBy,entryName]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 0.5,
			labelWidth : 70,
			items : [label1,fdDutyLeaderName,fdDutyLeader,fdManagerName,fdManager,
				fdTechnologyName,fdTechnologyBy,fdSuperviseName,fdSuperviseBy]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 0.5,
			labelWidth : 70,
			items : [label2,dtDutyLeaderName,dtDutyLeader,dtManagerName,dtManager,
				dtTechnologyName,dtTechnologyBy,dtSuperviseName,dtSuperviseBy]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 0.5,
			labelWidth : 70,
			items : [entryDept,entryDeptName]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 0.5,
			labelWidth : 70,
			items : [entryDateString]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 1,
			labelWidth : 70,
			items : [memo]
		}]
	});
	var layout = new Ext.Viewport({
		layout : "border",
		border : false,
		items : [{
			title : '详细信息',
			region : "center",
			split : true,
			collapsible : false,
			titleCollapse : false,
			margins : '1',
//			tbar : [{text : '  '}],
			layout : 'border',
			items : [{
				region : "center",
				split : true,
				collapsible : false,
				titleCollapse : true,
				margins : '1',
				// layoutConfig : {
				// animate : true
				// },
				layout : 'fit',
				items : [form]
			}, {
				region : "south",
				title : '明细列表',
				layoutConfig : {
					animate : true
				},
				height : 240,
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
	function treeClick(node, e) {
		// 提示修改是否保存
		e.stopEvent();
		currentNode = node;
		node.toggle();
		if (currentNode.id != 0) {
			var temp = currentNode.id.length;
			loadForm(currentNode);

			if (temp == 6) {
				ds.load({
					params : {
						id : currentNode.id
					}
				})				
			} else {
				ds.removeAll();
			}
		} else {
			form.getForm().reset();
			getWorkCode();
			ds.removeAll();
		}
		
	};
	// 根据当前节点填充form
	function loadForm(o) {
		var ob = new Object();
		ob.measureCode = o.id;
		ob.measureName = o.text;
		// 专业 责任领导（发电企业） 管理责任人（发电企业） 技术责任人（发电企业） 监督责任人（发电企业） 
		// 责任领导（大唐陕西）  管理责任人（大唐陕西) 技术责任人（大唐陕西） 监督责任人（大唐陕西） 
		var strC = o.attributes.code;
		var arrC = strC.split(",")
		var arr1C = arrC[0].split("-");
		ob.specialCode = arr1C[0];
		ob.specialName = arr1C[1];
		
		var arr2C = arrC[1].split("-");
		ob.fdDutyLeader = arr2C[0];
		ob.fdDutyLeaderName = arr2C[1];
		
		var arr3C = arrC[2].split("-");
		ob.fdManager = arr3C[0];
		ob.fdManagerName = arr3C[1];
		
		var arr4C = arrC[3].split("-");
		ob.fdTechnologyBy = arr4C[0];
		ob.fdTechnologyName = arr4C[1];
		
		var arr5C = arrC[4].split("-");
		ob.fdSuperviseBy = arr5C[0];
		ob.fdSuperviseName = arr5C[1];
		
		var arr6C = arrC[5].split("-");
		ob.dtDutyLeader = arr6C[0];
		ob.dtDutyLeaderName = arr6C[1];
		
		var arr7C = arrC[6].split("-");
		ob.dtManager = arr7C[0];
		ob.dtManagerName = arr7C[1];
		
		var arr8C = arrC[7].split("-");
		ob.dtTechnologyBy = arr8C[0];
		ob.dtTechnologyName = arr8C[1];
		
		var arr9C = arrC[8].split("-");
		ob.dtSuperviseBy = arr9C[0];
		ob.dtSuperviseName = arr9C[1];
		
		// 修改人，修改部门，修改时间
		var str = o.attributes.description;
		var arr = str.split(",")
		var arr1 = arr[0].split("-");
		ob.entryBy = arr1[0];
		ob.entryName = arr1[1];
		
		var arr2 = arr[1].split("-");
		ob.entryDept = arr2[0];
		ob.entryDeptDepName = arr2[1];
		
//		var arr3 = arr[2].split("-");
		ob.entryDateString = arr[2];

		ob.memo = o.attributes.href;
		
		measureCode.setValue(ob.measureCode);
		measureName.setValue(ob.measureName.substring(
				ob.measureCode.length + 1, ob.measureName.length));
		memo.setValue(ob.memo);

		specialCode.setValue(ob.specialName)
		
		fdDutyLeader.setValue(ob.fdDutyLeader);
		fdDutyLeaderName.setValue(ob.fdDutyLeaderName);
		fdManager.setValue(ob.fdManager);
		fdManagerName.setValue(ob.fdManagerName);
		fdTechnologyBy.setValue(ob.fdTechnologyBy);
		fdTechnologyName.setValue(ob.fdTechnologyName);
		fdSuperviseBy.setValue(ob.fdSuperviseBy);
		fdSuperviseName.setValue(ob.fdSuperviseName);
		
		dtDutyLeader.setValue(ob.dtDutyLeader);
		dtDutyLeaderName.setValue(ob.dtDutyLeaderName);
		dtManager.setValue(ob.dtManager);
		dtManagerName.setValue(ob.dtManagerName);
		dtTechnologyBy.setValue(ob.dtTechnologyBy);
		dtTechnologyName.setValue(ob.dtTechnologyName);
		dtSuperviseBy.setValue(ob.dtSuperviseBy);
		dtSuperviseName.setValue(ob.dtSuperviseName);

		
		entryBy.setValue(ob.entryBy);
		entryName.setValue(ob.entryName);
		entryDept.setValue(ob.entryDept);
		entryDeptName.setValue(ob.entryDeptDepName);
		entryDateString.setValue(ob.entryDateString)
	}
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
	
	function empSelect() {
		var args = new Object();
		var person = window
				.showModalDialog(
						'../empSelect/empSelect.jsp',
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
	
	
})