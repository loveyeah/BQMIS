Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function(){
	
	// 当前选中行号
	var selectedRow = -1;
	// 保存采购员Grid内的数据
	var gridObjects = new Array();
	// 物料为空数量
	var emptyCount = new Array();
	// 新增计划员数量
	var planerCount = 0;
	
	// 新增按钮
	var addBtn = new Ext.Button({
		text : '新增',
		iconCls : Constants.CLS_ADD,
		handler : addRecord
	})
	
	// 删除按钮
	var delBtn = new Ext.Button({
		text : Constants.BTN_DELETE,
		iconCls : Constants.CLS_DELETE,
		handler : deleteRecord
	})
	
	// 保存按钮
	var saveBtn = new Ext.Button({
		text : Constants.BTN_SAVE,
		iconCls : Constants.CLS_SAVE,
		handler : saveRecords
	})
	
	// 计划员数据
	var planerData = Ext.data.Record.create([
        // 人员编码
        {name: 'planer'},
        // 计划员姓名
        {name: 'planerName'},
        // 手机号
        {name: 'mobileNo'},
        // 固定电话
        {name: 'telephone'},
        // 传真
        {name: 'fax'},
        // 标识
        // Y: 从数据库中取出
        // N: 运行时增加
        {name: 'flag'}
    ]);

     var planerStore = new Ext.data.JsonStore({
        url : 'resource/getPlanerList.action',
        root : 'list',
        sortInfo :{field: "planer", direction: "ASC"},
        fields : planerData
  	});
  	
    //载入数据
    planerStore.load({
    	callback : function() {
    		gridObjects = new Array();
    		emptyCount = new Array();
    	}
    });
    
    // 选择列
	var sm = new Ext.grid.RowSelectionModel({singleSelect : true});
	// 人员信息选择
	var txtPlaner = new Ext.form.TriggerField({
		allowBlank : false,
		maxLength : 30,
		readOnly : true
	});
	// 从弹出窗口选择人员
	txtPlaner.onTriggerClick = popupSelect;
	
	// 计划员Grid
	var planerGrid = new Ext.grid.EditorGridPanel({
		region : 'center',
		layout : 'fit',
		height : 200,
		width : 800,
		isFormField : false,
		// 单击修改
        clicksToEdit : 1,
        store : planerStore,
        sm : sm,
        columns : [new Ext.grid.RowNumberer({header:"行号",width : 35}),
        	{   header : "人员编码<font color='red'>*</font>",
                width : 30,
                sortable : true,
                defaultSortable : true,
            	css:CSS_GRID_INPUT_COL,
                editor : txtPlaner,
                dataIndex : 'planer'
            }, {
                header : '计划员姓名',
                width : 40,
                sortable : true,
                dataIndex : 'planerName'
            }, {
                header : '手机号',
                width : 30,
                sortable : true,
                dataIndex : 'mobileNo'
            }, {
                header : '固定电话',
                width : 30,
                sortable : true,
                dataIndex : 'telephone'
            }, {
                header : '传真',
                width : 30,
                sortable : true,
                dataIndex : 'fax'
            }],
        viewConfig : {
            forceFit : true
        },
        tbar : [addBtn, '-', delBtn, '-', saveBtn],
       autoSizeColumns : true,
       frame : false,
       autoScroll : true,
       border : false,
       enableColumnHide : true,
       enableColumnMove : false
    });
    
 	// beforeedit 事件
    planerGrid.on('beforeedit', planerBeforeEdit);

    // 单击一行事件
    planerGrid.on('rowclick', planerGridClick);
    
	// 物料类别树
	var Tree = Ext.tree;
	var materialTypeTree = new Tree.TreePanel({
		region : 'center',
		autoScroll : true,
		height : 180,
		width : 150,
		border : false,
		rootVisible : true,
		root : root,
		animate : true,
		enableDD : true,
		border : false,
		containerScroll : true,
		loader : new Tree.TreeLoader({
			dataUrl : 'resource/getMaterialClassList.action'
		})
	});    
	var root = new Tree.AsyncTreeNode({
		text : '物料',
		draggable : false,
		id : '-1'
	});
	materialTypeTree.setRootNode(root);
	root.expand();
	root.select();
	currentNode = root;
	// 树的节点单击事件
	materialTypeTree.on('click', treeClick);
	
	// 待选项数据
	var waitSelectData = Ext.data.Record.create([
            // 编码
            {name: 'no'},
            // 种类
            // Y: 物料种类
            // N: 物料
            {name: 'kind'},
            // 名称
            {name: 'name'}]);
	
    var waitSelectStore = new Ext.data.JsonStore({
        url : 'resource/getWaitSelectMaterialList.action',
        root : 'list',
        sortInfo :{field: "no", direction: "ASC"},
        fields : waitSelectData
  	});
	
	// 待选项Grid
	var waitSelectGrid = new Ext.grid.GridPanel({
        store : waitSelectStore,
        height : 180,
        width : 700,
        autoScroll : true,
        sm : new Ext.grid.RowSelectionModel({singleSelect : false}),
        columns : [{
        		width : 700,
                sortable : true,
                header : '待选项',
                dataIndex : 'name'
            }],
       frame : false,
       border : false,
       enableColumnHide : true,
       enableColumnMove : false
    });
    
    // 已选项数据
	var selectedData = Ext.data.Record.create([
            // 编码
            {name: 'no'},
            // 种类
            // Y: 物料种类
            // N: 物料
            {name: 'kind'},
            // 名称
            {name: 'name'}]);
            
    var selectedStore = new Ext.data.JsonStore({
        url : 'resource/getPlanerSelectedMaterialList.action',
        root : 'list',
        sortInfo :{field: "no", direction: "ASC"},
        fields : selectedData
  	});
	
	// 已选项Grid
	var selectedGrid = new Ext.grid.GridPanel({
        store : selectedStore,
        height : 180,
        width : 700,
        autoScroll : true,
        sm : new Ext.grid.RowSelectionModel({singleSelect : false}),
        columns : [{
                width : 700,
                sortable : true,
                header : "已选项<font color='red'>*</font>",
                dataIndex : 'name'
            }],
       frame : false,
       border : false,
       enableColumnHide : true,
       enableColumnMove : false
    });
    
    // 移除事件
	selectedStore.on('remove', changeHandler);
	// 增加事件
	selectedStore.on('add', changeHandler);
	
    // ">>"按钮
	var toRightBtn = new Ext.Button({
		text : Constants.BTN_TO_RIGHT,
		handler : toRight
	})
	
	// "<<"按钮
	var toLeftBtn = new Ext.Button({
		text : Constants.BTN_TO_LEFT,
		handler : toLeft
	})
	
	// [按物料类别]单选按钮
	var materialTypeRadio = new Ext.form.Radio({
		boxLabel : '按物料类别',
		checked : true,
		name : 'type',
		inputValue : 'Y'
	});
	
	// [按物料]单选按钮
	var materialRadio = new Ext.form.Radio({
		boxLabel : '按物料',
		name : 'type',
		inputValue : 'N'
	});
	
	// 设置布局及面板
	new Ext.Viewport({
		layout : 'border',
		auotHeight : true,
		items : [{
			region : 'center',
			layout : 'fit',
			height : 200,
			items : [planerGrid]
		}, {
			region : 'south',
			border : false,
			height : 230,
			items : [{
					xtype : 'panel',
					style : "padding-top:10;",
					border : false,
					layout : 'column',
					height : 200,
					items : [{
						columnWidth : 0.3,
						border : false,
						items : [{
							xtype : 'panel',
							border : true,
							height : 180,
							layout : 'fit',
							atuoscroll : true,
							items : [materialTypeTree]
					}]}, {
						columnWidth : 0.02,
						border : false
					}, {
						columnWidth : 0.3,
						border : true,
						layout : 'fit',
						height : 180,
						items : [waitSelectGrid]
					}, {
						columnWidth : 0.08,
						border : false,
						xtype : 'panel',
						layout : 'column',
						items : [{
							columnWidth : 0.2,
							border : false
						}, {
							xtype : 'panel',
							border : false,
							columnWidth : 0.7,
							style : "padding-top:50;",
							items : [toRightBtn, {
								xtype : 'panel',
								border : false,
								height : 10
							}, toLeftBtn]
						}, {
							columnWidth : 0.1,
							border : false
						}]
					}, {
						columnWidth : 0.3,
						border : true,
						layout : 'fit',
						height : 180,
						items : [selectedGrid]
					}]
				}, {
					xtype : 'panel',
					border : false,
					style : 'font-size:13;',
					layout : 'column',
					items : [{
						columnWidth : .15,
						border : false,
						items : [materialTypeRadio]
					}, {
						columnWidth : .15,
						border : false,
						items : materialRadio
					}]
				}]
			}]
	})
	
	// ↓↓*************** 处理 ************ //
	
	/**
	 * 单击树，更新待选项
	 */
	function treeClick(node, e) {
		e.stopEvent();
		if(node.id != -1) {
			// 按物料类别时
			if(materialTypeRadio.checked) {
				// 将节点增加到待选项Grid中
				getNodeToGrid(node);
			// 按物料时
			} else {
				if(node.leaf) {
					// 将节点增加到待选项Grid中
					getNodeToGrid(node);
				} else {
					// 查询所有对应的物料
				    var jsonStore = new Ext.data.JsonStore({
				        url : 'resource/getWaitSelectMaterialList.action',
				        root : 'list',
				        sortInfo :{field: "no", direction: "ASC"},
				        fields : waitSelectData
				  	});
			
					jsonStore.load({
						params : {
							// 当前节点的id
							node : node.id
						},
						callback : function() {
							waitSelectStore.removeAll();
							if(jsonStore.getCount() > 0) {
								// 将查询结果赋给待选项Grid
								for(var i = 0; i < jsonStore.getCount(); i++) {
									var count = jsonStore.getCount();
									var record = jsonStore.getAt(i);
									if(!checkItems(record)) {
										var waitCount = waitSelectStore.getCount();
										waitSelectStore.insert(waitCount,record)
								        waitSelectStore.commitChanges();
									}
								}
							}
							waitSelectGrid.getView().refresh();
						}
					});
				}
			}
		}
	}
	
	/**
	 * 将选中的节点移到待选项中
	 */
	function getNodeToGrid(node) {
		var record = new waitSelectData({
            // 名称
            name : node.text,
            // 编码
            no : node.id,
            // 种类
            kind : node.attributes.kind
    	});
        // 停止原来编辑
        waitSelectGrid.stopEditing();
        // 插入新数据
        waitSelectStore.removeAll();
        // 在已选项中存在的数据则不移进待选项中
        if(!checkItems(record)) {
        	waitSelectStore.insert(0,record)
	        waitSelectGrid.getView().refresh();
        }
	}

	/**
     * beforeedit事件处理函数
     */
	function planerBeforeEdit(e) {
		// 获取store
        var store = e.grid.getStore();
        // 获取当前记录
        var record = store.getAt(e.row); 
        // 编辑列的字段名
        var fieldName = e.grid.getColumnModel().getDataIndex(e.column);
        if("planer" == fieldName) {
          // db中原有记录的人员编码不可编辑
          if(record.get('flag') == "Y"){
            e.cancel = true;
            return;
          }
        }
	}
	
	/**
	 * 单击一行，更新已选项
	 */
	function planerGridClick() {
		if (planerGrid.selModel.hasSelection()) {
			// 移除已选项的所有数据
			selectedStore.removeAll();
			var records = planerGrid.selModel.getSelections();
			// 当前选择行
			selectedRow = planerStore.indexOf(records[0]);
			// 如果此行对应的已选项已经更新过
			if(gridObjects[selectedRow] != "undefined" &&
				gridObjects[selectedRow] != null ) {
				// 用gridObjects中保存的数据初始化已选项
				var names = gridObjects[selectedRow].names;
				var nos = gridObjects[selectedRow].nos;
				var kinds = gridObjects[selectedRow].kinds;
				// 初始化
				for(var i = 0; i < names.length; i++) {
					if(names[i] != " ") {
						var count = selectedStore.getCount();
						// 新记录
				        var record = new planerData({
							// 名称
				        	name : names[i],
				        	// 编码
				        	no : nos[i],
				        	// 种类
				            kind : kinds[i]
				        });
						selectedStore.insert(count, record);
					}
				}
			// 如果没有更新过此行,则再次查询
			} else {
				selectedStore.load({
					params : {
						planer : records[0].data.planer
					}
				});
			}
			// 移除待选项所有数据
			waitSelectStore.removeAll();
			waitSelectGrid.getView().refresh();
		}
	}
	
	/**
	 * 添加一行数据
	 */
	function addRecord() {
		// 新记录
        var record = new planerData({
			// 人员编码
        	planer : '',
        	// 计划员姓名
        	planerName : '',
        	// 手机号
        	mobileNo : '',
        	// 固定电话
        	telephone : '',
        	// 传真
        	fax : '',
        	// 标识
        	flag : 'N'
        });
        // 移除已选项所有数据
        selectedStore.removeAll();
        // 原数据个数
        var count = planerStore.getCount();
        // 停止原来编辑
        planerGrid.stopEditing();
        // 插入新数据
        planerStore.insert(count,record);
        emptyCount[emptyCount.length] = count;
        // 新增数量加1
        planerCount += 1;
        // 选中新增加行
        sm.selectLastRow(true);
        var records = planerGrid.selModel.getSelections();
		selectedRow = planerStore.indexOf(records[0]);
        planerGrid.getView().refresh();
	}
	
	/**
	 * 删除选中的一行
	 */
	function deleteRecord() {
		var smodel = planerGrid.getSelectionModel();
		var selected = smodel.getSelections();
		// 如果没有选中行
		if (selected.length < 1) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
		} else {
			// 弹出信息确认是否删除选中行
			Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_002, function(
				buttonobj) {
				var record = selected[0];
				if (buttonobj == "yes") {
					var member = record.data;
					// 如果此行数据是从数据库中取出的
					if(member.flag == "Y") {
						Ext.lib.Ajax.request(Constants.POST,
							'resource/deletePlanerByPlaner.action', {
							success : function(action) {
								// 画面上对应数据删除
								afterDelete(record);
								Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_005)
							},
							failure : function() {
								Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_E_014);
							}
						}, 'planer=' + member.planer);
					// 如果此行数据是运行时增加的
					} else {
						// 画面上对应数据删除
						afterDelete(record);
					}
				}
			})
		}
	}
	
	/**
	 * 画面上对应数据删除
	 */
	function afterDelete(record) {
		// 判断删除一行后,应该选择哪一行
		if(sm.hasNext()) {
			sm.selectNext(true);
		} else if(sm.hasPrevious()) {
			sm.selectPrevious(true);
		}
		var index = planerStore.indexOf(record);
		// 从Grid中删除对应的数据
		planerStore.remove(record);
		// 从gridObjects中删除对应的数据
		gridObjects.splice(selectedRow, 1);
		var result = findExist(selectedRow);
		if(result != -1) {
			emptyCount.splice(result, 1);
		}
		for(var i = 0; i < emptyCount.length; i++) {
			if(emptyCount[i] > index) {
				emptyCount[i]--;
			}
		}
		// 如果是新增的，新增数量减1
		if(record.data.flag == "N") {
			planerCount -= 1;
		}
		// 触发Grid单击事件
		if(planerStore.getCount() > 0) {
			planerGridClick();
		}
		waitSelectStore.removeAll();
		selectedStore.removeAll();
		planerGrid.getView().refresh();
	}
	
	/**
	 * 保存Grid内的数据
	 */
	function saveRecords() {
		// 把gridObjects转化为字符串
		var strString = arrayToString();
		// 如果画面数据没有修改
		if((strString.length == 0) && (planerCount < 1)) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_006);
		} else {
			// 弹出信息确认是否保存
			Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_001, function(
				buttonobj) {
				// 确认保存
				if (buttonobj == "yes") {
					// 检查画面数据是否合法
					if(checkGrid() && checkSelected()) {
						Ext.lib.Ajax.request(Constants.POST,
								'resource/savePlanerRecords.action', {
									success : function(action) {
										Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_004);
										refresh();
									},
									failure : function() {
										Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_E_014);
										refresh();
									}
								}, 'string=' + strString);
					}
				}
			})
		}
	}
	
	/**
	 * 保存后刷新画面数据
	 */
	function refresh() {
		// 画面数据刷新
		waitSelectStore.removeAll();
		waitSelectGrid.getView().refresh();
		selectedStore.removeAll();
		selectedGrid.getView().refresh();
		planerStore.load({
			callback : function() {
				gridObjects = new Array();
				emptyCount = new Array();
				planerGrid.getView().refresh();
			}}
		);
		root.reload();
		materialRadio.reset();
		materialTypeRadio.reset();
		planerCount = 0;
		selectedRow = -1;
	}
	
	/**
	 * 把gridObjects转化为字符串
	 */
	function arrayToString() {
		var strString = "";
		// 已经改变的行
		for(var i = 0; i < gridObjects.length; i++) {
			var obj = gridObjects[i];
			if(obj != "undefined" && obj != null) {
				// 人员编码
				strString += obj.planer;
				strString += ",";
				// 人员姓名
				strString += obj.planerName;
				strString += ",";
				// 标识
				strString += obj.flag;
				strString += ",";
				// 对应的物料名称
				if(obj.names != "undefined" && obj.names != null) {
					strString += obj.names.join(":");
					strString += ",";
				}
				// 对应的物料编码
				if(obj.nos != "undefined" && obj.nos != null) {
					strString += obj.nos.join(":");
					strString += ",";
				}
				// 对应的物料分类
				if(obj.kinds != "undefined" && obj.kinds != null) {
					strString += obj.kinds.join(":");
					strString += ",";
				}
				if(strString.charAt(strString.length -1) == ",") {
					strString = strString.substring(0, strString.length - 1);
				}
				strString += ";";
			}
			
		}
		if(strString.charAt(strString.length -1) == ";") {
			strString = strString.substring(0, strString.length - 1);
		}
		return strString;
	}
	
	/**
	 * 检查Grid内数据是否合法
	 */
	function checkGrid() {
		var count = planerStore.getCount();
		if(checkPlanerEmpty() && checkPlanerRepeat()) {
			return true;
		} else {
			return false;
		}
		return true;
	}
	
	/**
	 * 检查计划员对应的已选物料是否为空
	 */
	function checkSelected() {
		if(emptyCount.length > 0) {
			var msg = "";
			for(var i = 0; i < emptyCount.length; i++) {
				var record = planerStore.getAt(emptyCount[i]);
				if(msg != "") {
					msg += '<br/>' + String.format(Constants.COM_E_017, '人员编码',record.get("planer"),'已选项列表');
				} else {
					msg += String.format(Constants.COM_E_017, '人员编码',record.get("planer"),'已选项列表');
				}
			}
			if(msg != "") {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, msg);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 检查Grid内数据是否为空
	 */
	function checkPlanerEmpty() {
		var count = planerStore.getCount();
		for(var i = 0; i < count; i++) {
			var planer = planerStore.getAt(i).get("planer");
			if(planer == "" || planer == null) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(Constants.COM_E_003, '人员编码'))
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 检查Grid内数据是否重复
	 */
	function checkPlanerRepeat() {
		var count = planerStore.getCount();
		for(var i = 0; i < count; i++) {
			var record = planerStore.getAt(i);
			for(var j = i + 1; j < count; j++) {
				var recordCompare = planerStore.getAt(j);
				if(record.get("planer") == recordCompare.get("planer")) {
					Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.RI002_E_003)
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 从待选项向已选项中加入数据
	 */
	function toRight() {
        // 停止待选项编辑
        waitSelectGrid.stopEditing();
		var waitSelectRecord = waitSelectGrid.selModel.getSelections();
		var planerSelected = planerGrid.selModel.getSelections();
		// 如果没有选中行
		if (planerSelected.length < 1) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.RI009_E_004);
		} else {
			if (waitSelectRecord.length < 1) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.RI002_E_001);
			} else {
				for(var i = 0; i < waitSelectRecord.length; i++) {
					var member = waitSelectRecord[i];
					// 已选项数据个数
			        var selectedCount = selectedStore.getCount();
			        // 停止已选项编辑
			        selectedGrid.stopEditing();
			        // 向已选项中插入新数据
			        selectedStore.insert(selectedCount,member);
			        // 移除待选项对应数据
			        waitSelectStore.remove(member)
				}
				// 刷新
				selectedGrid.getView().refresh();
				waitSelectGrid.getView().refresh();
			}
		}
	}
	
	/**
	 * 从已选项向待选项中加入数据
	 */
	function toLeft() {
		// 停止待选项编辑
        selectedGrid.stopEditing();
		var selectedRecord = selectedGrid.selModel.getSelections();
		if (selectedRecord.length < 1) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.RI002_E_002);
		} else {
			for(var i = 0; i < selectedRecord.length; i++) {
				var member = selectedRecord[i];
				// 待选项数据个数
		        var waitSelectCount = waitSelectStore.getCount();
		        // 停止待选项编辑
		        waitSelectGrid.stopEditing();
		        // 向待选项中插入新数据
		        waitSelectStore.insert(waitSelectCount,member);
		        // 移除已选项对应数据
		        selectedStore.remove(member)
			}
			// 刷新
			selectedGrid.getView().refresh();
			waitSelectGrid.getView().refresh();
		}
	}
	
	/**
	 * 已选项中数据发生改变时处理
	 */
	function changeHandler() {
		// 判断是否有选中行
		if (planerGrid.selModel.hasSelection()) {
			var count = selectedStore.getCount();
			// 如果已选项中数据为空
			if(count == 0) {
				// 如果对应的行是从数据库中取出的，则要从数据库中删除此行
				if(planerStore.getAt(selectedRow).get("flag") == "Y") {
					var obj = new Object();
					obj.names = new Array(" ");
					obj.nos = new Array(" ");
					obj.kinds = new Array(" ");
					obj.planer = planerStore.getAt(selectedRow).get("planer");
					obj.planerName = planerStore.getAt(selectedRow).get("planerName");
					obj.flag = planerStore.getAt(selectedRow).get("flag");
					gridObjects[selectedRow] = obj;
				} else {
					// 如果此行是运行时增加的，直接删除
					gridObjects[selectedRow] = null;
				}
				emptyCount[emptyCount.length] = selectedRow;
			// 如果数据不为空
			} else {
				var names = new Array();
				var nos = new Array();
				var kinds = new Array();
				for(var i = 0; i < count; i++) {
					// 物料名称
					names[i] = selectedStore.getAt(i).get("name");
					// 物料编码
					nos[i] = selectedStore.getAt(i).get("no");
					// 物料种类
					kinds[i] = selectedStore.getAt(i).get("kind");
				}
				// 保存数据
				var obj = new Object();
				obj.names = names;
				obj.nos = nos;
				obj.kinds = kinds;
				obj.planer = planerStore.getAt(selectedRow).get("planer");
				obj.planerName = planerStore.getAt(selectedRow).get("planerName");
				obj.flag = planerStore.getAt(selectedRow).get("flag");
				gridObjects[selectedRow] = obj;
				var result = findExist(selectedRow);
				if(result != -1) {
					emptyCount.splice(result, 1);
				}
			}
		}
	}
	
	/**
	 * 检查数据是否已经存在于已选项中
	 */
	function checkItems(record) {
		if(selectedStore.getCount() > 0) {
			var no = record.get("no");
			var count = selectedStore.getCount();
			for(var i = 0; i < count; i++) {
				var recordSel = selectedStore.getAt(i);
				var noSel = recordSel.get("no");
				if(no == noSel ) {
					return true;
				}
			}
			return false;
		}
	}
	
	/**
	 * 查找值是否在数组内
	 */
	function findExist(value) {
		for(var i = 0; i < emptyCount.length; i++) {
			if(emptyCount[i] == value) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 从弹出窗口中选择人员
	 */
	function popupSelect() {
		var args = new Object();
		args.selectModel = "single";
		args.rootNode = {
			id : '-1',
			text : '合肥电厂'
		};
		var object = window.showModalDialog(
						'../../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
						args,
						'dialogWidth=800px;dialogHeight=520px;center=yes;help=no;resizable=no;status=no;');
		if (object) {
			var record = planerStore.getAt(selectedRow);
			if (typeof(object.workerCode) != "undefined") {
				Ext.Ajax.request({
					url : 'resource/getPersonInfoPlaner.action',
					method : Constants.POST,
					params : {
						// 人员编码
						planer : object.workerCode
					},
					// 成功
					success : function(result, request) {
						result = eval("(" + result.responseText + ")");
						record.set("planer", object.workerCode);
						if(typeof(gridObjects[selectedRow]) != "undefined") {
							gridObjects[selectedRow].planer = object.workerCode;
						}
						record.set("planerName", object.workerName);
						if(typeof(gridObjects[selectedRow]) != "undefined") {
							gridObjects[selectedRow].planerName = object.workerName;
						}
						if(result != null) {
							record.set("mobileNo", result.mobilePhoneNo);
							record.set("telephone", result.immobilePhoneNo);
							record.set("fax", result.electrographNo);
						} else {
							record.set("mobileNo", "");
							record.set("telephone", "");
							record.set("fax", "");
						}
					},
					// 失败
					failure : function() {
						Ext.Msg.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
					}
				});

			}
			planerGrid.getView().refresh();
		}
	}
	
	// ↑↑*************** 处理 ************ //
});