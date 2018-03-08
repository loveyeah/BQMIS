// Ext.namespace('Power.dept');
// Power.dept = function(config) {
// var rootNode = (config && config.rootNode) ? config.rootNode : {
// id : '0',
// text : '灞桥电厂'
// };
// var rootNodeObj = new Ext.tree.AsyncTreeNode(rootNode);
// var deptTree = new Ext.tree.TreePanel({
// loader : new Ext.tree.TreeLoader({
// dataUrl : 'comm/getDeptsByPid.action'
// }),
// root : rootNodeObj,
// autoWidth : true,
// autoScroll : true,
// animate : true,
// enableDD : false,
// border : false,
// rootVisible : true,
// containerScroll : true
// });
// var btnConfrim = new Ext.Button({
// text:'确定',
// id:'btnConfrim',
// iconCls:'confirm',
// handler:function(){
// win.hide();
// }
// });
// var win = new Ext.Window({
// closeAction : 'hide',
// width:600,
// height:450,
// title : '部门选择',
// modal : true,
// layout : 'border',
// items : [{
// region : 'center',
// layout : 'fit',
// split:true,
// items : [deptTree]
// }],
// buttons:[btnConfrim,{
// text:'取消',
// iconCls:'cancer',
// handler:function(){
// win.hide();
// }
// }]
// });
// win.on("render",function(){
// rootNodeObj.expand();
// rootNodeObj.select();
// });
// return {
// treeRoot:rootNodeObj,
// tree : deptTree,
// win:win,
// btnConfrim:btnConfrim,
// getValue:function(){
// var node = deptTree.getSelectionModel().getSelectedNode();
// return node;
// }
// }
// };

var deptNode ;
Ext.namespace('Power.person');
Power.person = function(cbConfig, config) {
	var selectModel = (config && config.selectModel)
			? config.selectModel
			: 'multiple';
	var notIn = (config && config.notIn) ? arg.notIn : "";
	var dept = Power.dept(config);
	var deptTree = dept.tree;
	deptTree.on('click', function(node, e) {
				e.stopEvent();
				deptNode = node;
				ds.load({
							params : {
								start : 0,
								limit : bbar.pageSize
							}
						});
			});
	var btnConfirm = new Ext.Button({
				text : '确定',
				id : 'btnConfirm',
				iconCls : 'confirm',
				xtype : 'button',
				handler : function() {
					chooseWorker();
				}
			});
	//modified by kzhang 20100902
	var nameText = new Ext.form.TextField({
			name : 'queryKey',
			width : '100pt',
			//tabIndex:1,
			xtype : 'textfield',
			listeners : {
				specialkey : function(field, e) {
					if (e.getKey() == Ext.EventObject.ENTER
							&& this.getValue().length > 0) {
						ds.load({
									params : {
										start : 0,
										limit : bbar.pageSize
									}
								});
					}
				}
			}
	});
	var toolbar = new Ext.Toolbar({
				items : [{
							text : '工号/姓名',
							xtype : 'label'
						},nameText
						, {
							text : '查询',
							iconCls : 'query',
							xtype : 'button',
							handler : function() {
								ds.load({
											params : {
												start : 0,
												limit : bbar.pageSize
											}
										});
							}
						}, btnConfirm]
			});

	function chooseWorker() {
		// 单选
		if (selectModel != "multiple") {
			var record = grid.getSelectionModel().getSelected();
			if (typeof(record) != "object") {
				// modified by liuyi 20100421
				// Ext.Msg.alert('提示', '请选择员工记录');
				// return false;
				win.hide()
				return null;
			}
			setValue(record.get("workerCode"), record.get("workerName"));
			win.hide();
			return record;
		}
		// 多选
		else {
			var selectNodes = grid.getSelectionModel().getSelections();
			if (selectNodes.length == 0) {
				Ext.Msg.alert('提示', '请选择员工记录');
				return false;
			}
			var ros = new Array();
			var workerCodes = new Array();
			var workerNames = new Array();
			var deptIds = new Array();
			var deptCodes = new Array();
			var deptNames = new Array();
			for (var i = 0; i < selectNodes.length; i++) {
				var record = selectNodes[i].data;
				workerCodes.push(record.workerCode);
				workerNames.push(record.workerName);
				deptIds.push(record.deptId);
				deptCodes.push(record.deptCode)
				deptNames.push(record.deptName)
				ros.push(record);
			}
			setValue(workerCodes.join(","), workerNames.join(","));
			win.hide();
			return ros;
		}
	}

	var User = Ext.data.Record.create([{
				name : 'workerCode'
			}, {
				name : 'workerName'
			}, {
				name : 'deptId'
			}, {
				name : 'deptCode'
			}, {
				name : 'deptName'
			}, {
				name : 'empId'
			}]);
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'comm/getWorkerByDept.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'totalCount',
							root : 'list'
						}, User)
			});
	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : !(selectModel == "multiple")
			});
	ds.on('beforeload', function() {
					if (deptNode) {
					Ext.apply(this.baseParams, {
								deptId : deptNode.id,
								queryKey : Ext.get("queryKey") ? Ext
										.get("queryKey").dom.value : '',
								notInWorkerCodes : notIn
							});
				}else{
					Ext.apply(this.baseParams, {
								queryKey : Ext.get("queryKey") ? Ext
										.get("queryKey").dom.value : '',
								notInWorkerCodes : notIn
							});
				}
			});
	var cm = new Ext.grid.ColumnModel([sm, {
				header : '工号',
				dataIndex : 'workerCode',
				align : 'left',
				width : 80
			}, {
				header : '姓名',
				dataIndex : 'workerName',
				style : 'display="none"',
				align : 'left',
				width : 100
			}, {
				header : '部门',
				dataIndex : 'deptName',
				align : 'left',
				width : 150
			}]);
	cm.defaultSortable = true;
	var bbar = new Ext.PagingToolbar({
				pageSize : 20,
				store : ds,
				displayInfo : true
			});
	var grid = new Ext.grid.EditorGridPanel({
				ds : ds,
				cm : cm,
				sm : sm,
				bbar : bbar,
				tbar : toolbar,
				autoWidth : true,
				autoScroll : true,
				fitToFrame : true,
				border : false
			});
	grid.enableColumnHide = false;
	if (!(selectModel == "multiple")) {
		grid.on("rowdblclick", function() {
					// modified by liuyi 20100421
					// chooseWorker();
					Ext.get('btnConfirm').dom.click();
				});
	}
	var win = new Ext.Window({
				closeAction : 'hide',
				width : 600,
				height : 450,
				title : '人员选择',
				modal : true,
				layout : 'border',
				items : [{
							region : 'center',
							layout : 'fit',
							split : true,
							items : [grid]
						}, {
							region : 'west',
							width : 200,
							layout : 'fit',
							items : [deptTree]
						}]
			});
	win.on("render", function() {
				dept.treeRoot.expand();
				dept.treeRoot.select();
				ds.load({
							params : {
								start : 0,
								limit : bbar.pageSize
							}
						});
			});
	var cbStore = new Ext.data.Store({
				reader : new Ext.data.JsonReader({}, User)
			});
	var combo = new Ext.form.ComboBox({
				fieldLabel : '员工选择',
				store : cbStore,
				mode : 'local',
				hiddenName : 'person',
				name : 'person',
				width : 180,
				valueField : 'workerCode',
				displayField : 'workerName',
				editable : true,
				triggerAction : 'all',
				forceSelection : true,
				readOnly : true,
				onTriggerClick : function() {
					if (!this.disabled) {
						win.show();
						nameText.focus(true, true); 
					}
				}
			});
	Ext.apply(combo, cbConfig);
	function setValue(code, name) {
		var d1 = new User({
					workerCode : code,
					workerName : name
				});
		cbStore.removeAll();
		cbStore.add(d1);
		combo.setValue(code);
	}
	function getValue() {
		if (cbStore.getCount() > 0) {
			var p = cbStore.getAt(0);
			return p.data;
		} else {
			return null;
		}
	}
	return {
		btnConfirm : btnConfirm,
		deptRoot : dept.treeRoot,
		win : win,
		combo : combo,
		getSelectedPersonCode : function() {
			var rec = grid.getSelectionModel().getSelected();
			if (rec) {
				return rec.get("workerCode");
			} else {
				return null;
			}
		},
		setValue : setValue,
		getValue : getValue,
		chooseWorker : chooseWorker
	}
}