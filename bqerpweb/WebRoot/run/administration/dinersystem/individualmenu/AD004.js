// 菜谱类型维护
// author:zhaomingjian
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();

Ext.onReady(function() {
	// zhaomingjian合计行追加
	Ext.override(Ext.grid.GridView, {
				// 重写doRender方法
				doRender : function(cs, rs, ds, startRow, colCount, stripe) {
					var ts = this.templates, ct = ts.cell, rt = ts.row, last = colCount
							- 1;
					var tstyle = 'width:' + this.getTotalWidth() + ';';
					// buffers
					var buf = [], cb, c, p = {}, rp = {
						tstyle : tstyle
					}, r;
					for (var j = 0, len = rs.length; j < len; j++) {
						r = rs[j];
						cb = [];
						var rowIndex = (j + startRow);
						for (var i = 0; i < colCount; i++) {
							c = cs[i];
							p.id = c.id;
							p.css = i == 0 ? 'x-grid3-cell-first ' : (i == last
									? 'x-grid3-cell-last '
									: '');
							p.attr = p.cellAttr = "";
							// 如果该行是统计行并且改列是第一列
							if (r.data["countType"] == "total" && i == 0) {
								p.value = "合计";
							} else {
								p.value = c.renderer(r.data[c.name], p, r,
										rowIndex, i, ds);
							}
							p.style = c.style;
							if (p.value == undefined || p.value === "")
								p.value = "&#160;";
							if (r.dirty
									&& typeof r.modified[c.name] !== 'undefined') {
								p.css += ' x-grid3-dirty-cell';
							}
							cb[cb.length] = ct.apply(p);
						}
						var alt = [];
						if (stripe && ((rowIndex + 1) % 2 == 0)) {
							alt[0] = "x-grid3-row-alt";
						}
						if (r.dirty) {
							alt[1] = " x-grid3-dirty-row";
						}
						rp.cols = colCount;
						if (this.getRowClass) {
							alt[2] = this.getRowClass(r, rowIndex, rp, ds);
						}
						rp.alt = alt.join(" ");
						rp.cells = cb.join("");
						buf[buf.length] = rt.apply(rp);
					}
					return buf.join("");
				}
			});
	// 添加与修改共通方法参数
	var strMethod;
	var strFlag;
	var strMId;
	var strFlag;
	// 新增按钮
	var btnAdd = new Ext.Button({
				id : 'add',
				text : Constants.BTN_ADD,
				iconCls : Constants.CLS_ADD,
				enableToggle : true,
				handler : addRecord
			});

	// 修改按钮
	var btnUpdate = new Ext.Button({
				id : 'update',
				text : Constants.BTN_UPDATE,
				iconCls : Constants.CLS_UPDATE,
				handler : updateRecord
			});
	// 删除按钮
	var btnDelete = new Ext.Button({
				id : 'delete',
				text : Constants.BTN_DELETE,
				iconCls : Constants.CLS_DELETE,
				handler : deleteRecord
			});
	// grid所需的类定义
	var MyGridRecord = new Ext.data.Record.create([{
				name : 'MId'
			}, {
				name : 'menuDate'
			}, {
				name : 'menuType'
			}, {
				name : 'userName'
			}, {
				name : 'depName'
			}, {
				name : 'insertDate'
			}, {
				name : 'menuInfo'
			}, {
				name : 'stationName'
			}, {
				name : 'strUpdateTime'
			}, {
                name : 'place'
            }]);
	// grid中的store定义
	var gridStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'administration/getIndividualMenuListInfo.action'
						}),
				reader : new Ext.data.JsonReader({
							root : 'list',
							totalProperty : 'totalCount'
						}, MyGridRecord)

			});
	// 初始化时显示所有数据
	gridStore.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			});
			// xstan 删除开始 2009-02-09 修改，删除按钮控制取消 
//	// 判断store是否有数据
//	gridStore.on('load', function() {
//				if (gridStore.getTotalCount() <= 0) {
//					btnUpdate.disable(true);
//					btnDelete.disable(true);
//				} else {
//					btnUpdate.enable(true);
//					btnDelete.enable(true);
//				}
//			});
			// xstan 删除开始 2009-02-09
	// 定义底部工具栏
	var btmToolbar = new Ext.PagingToolbar({
				pageSize : Constants.PAGE_SIZE,
				store : gridStore,
				displayInfo : true,
				displayMsg : MessageConstants.DISPLAY_MSG,
				emptyMsg : MessageConstants.EMPTY_MSG
			});
	// 定义ColumnModel
	var gridCM = new Ext.grid.ColumnModel([
			// 行号
			new Ext.grid.RowNumberer({
						header : '行号',
						width : 35
					}), {
				header : '订单号',
				hidden : true,
				dataIndex : 'MId'
			}, {
				// 订餐日期
				header : '订餐日期',
				align : 'left',
				sortable : true,
				width : 120,
				dataIndex : 'menuDate'
			}, {
				// 订餐类别
				header : '订餐类别',
				align : 'left',
				sortable : true,
				width : 80,
				dataIndex : 'menuType'
			}, {
				hidden : true,
				header : '姓名',
				dataIndex : 'userName'
			}, {
				hidden : true,
				header : '部门名称',
				dataIndex : 'depName'
			}, {
				hidden : true,
				header : '填单日期',
				dataIndex : 'insertDate'
			}, {
				hidden : true,
				header : '订单状态',
				dataIndex : 'menuInfo'
			}, {
				hidden : true,
				header : '工作类别',
				dataIndex : 'workKind'
			}, {
				hidden : true,
				dataIndex : 'strUpdateTime'
			}]);
	// 对Grid进行初始化
	var mainGrid = new Ext.grid.GridPanel({
				store : gridStore,
				cm : gridCM,
				tbar : [btnAdd, btnUpdate, btnDelete],
				bbar : btmToolbar,
				frame : false,
				border : false,
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : true
						}),
				enableColumnHide : false,
				enableColumnMove : false
			});
	// 注册双击事件
	mainGrid.on("rowdblclick", updateRecord);
	// 设定布局器及面板
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "fit",
				items : [mainGrid]
			});
	// ******************弹出窗口布局器及界面定义*********************
	// *****************菜谱信息一览 **********************
	// 系统当天日期
	var dteFrom = new Date();
	dteFrom.setDate(dteFrom.getDate());
	dteFrom = dteFrom.format('Y-m-d');

	// 第一行第一******************订餐日期
	var txtmenuDate = new Ext.form.TextField({
				format : 'Y-m-d',
				id : 'menuDate',
				fieldLabel : '订餐日期',
				name : 'menuDate',
				readOnly : true,
				clearCls : 'allow-float',
//				checked : true,
				style : 'cursor:pointer',
				isFormField : true,
				anchor : '98%',
				listeners : {
					focus : function() {
						if (strMethod == 'add') {
							WdatePicker({
										isShowClear : false,
										startDate : '%y-%M-%d',
										dateFmt : 'yyyy-MM-dd'
									});
						}
					}
				}

			});

	// 第一行第二************订餐类别
	var drpMenuType = new Ext.form.CmbMealType({
				fieldLabel : '订餐类别',
				id : 'menuType',
				// readOnly:true,
				name : 'menuType',
				emptyText : '',
				mode : 'local',
				isFormField : true,
				anchor : '100%'
			});
	// 根据时间取消
	drpMenuType.on('beforequery', function(obj) {
				if (strMethod == 'update')
					obj.cancel = true;
			});
	drpMenuType.setValue('1');
	// 第一行
	var fldSetFirstLineQuest = new Ext.form.FieldSet({
				border : false,
				layout : 'column',
				style : "padding-top:0px;padding-bottom:0px;border:0px",
				// height : 21,
				anchor : '100%',
				items : [{
							columnWidth : 0.5,
							border : false,
							layout : 'form',
							items : [txtmenuDate]
						}, {
							columnWidth : 0.5,
							border : false,
							layout : 'form',
							items : [drpMenuType]
						}]
			})

	// 第二行第一*************订餐人
	var insertBy = new Ext.form.TextField({
				fieldLabel : "订餐人",
				id : "insertByMan",
				name : "insertByMan",
				readOnly : true,
				maxLength : 50,
				anchor : '98%'
			});
	// 第二行第二********************所属部门
	var deptBy = new Ext.form.TextField({
				fieldLabel : "所属部门",
				id : "deptBy",
				name : "deptBy",
				readOnly : true,
				maxLength : 60,
				anchor : '100%'
			});
	// 第二行
	var fldSetSecondLineQuest = new Ext.form.FieldSet({
				border : false,
				layout : 'column',
				style : "padding-top:0px;padding-bottom:0px;border:0px",
				// height : 20,
				anchor : '100%',
				items : [{
							columnWidth : 0.5,
							border : false,
							layout : 'form',
							items : [insertBy]
						}, {
							columnWidth : 0.5,
							border : false,
							layout : 'form',
							items : [deptBy]
						}]
			})

	// 第三行第一个******************填单日期
	var insertDate = new Ext.form.TextField({
				fieldLabel : "填单日期",
				id : "insertDate",
				name : "insertDate",
				allowBlank : false,
				maxLength : 50,
				readOnly : true,
				anchor : '98%'
			});
	var dteFrom = new Date();
	dteFrom.setDate(dteFrom.getDate());
	dteFrom = dteFrom.format('Y-m-d');
	insertDate.setValue(dteFrom);

	// 第三行第二个**************************单据状态
	var menuInfo = new Ext.form.TextField({
				fieldLabel : "订单状态",
				id : "menuInfo",
				name : "menuInfo",
				allowBlank : false,
				readOnly : true,
				align : 'center',
				value : '填写',
				maxLength : 50,
				anchor : '100%'
			});
	// 第三行
	var fldSetThirdLineQuest = new Ext.form.FieldSet({
				border : false,
				layout : 'column',
				style : "padding-bottom:0px;border:0px",
				// height : 20,
				anchor : '100%',
				items : [{
							columnWidth : 0.5,
							border : false,
							layout : 'form',
							items : [insertDate]
						}, {
							columnWidth : 0.5,
							border : false,
							layout : 'form',
							items : [menuInfo]
						}]
			})

	// 第四行********************就餐地点
	var drpAddress = new Ext.form.ComboBox({
				fieldLabel : '就餐地点',
				width : 180,
				allowBlank : true,
				anchor : '49%',
				id : 'address',
				name : 'address',
				emptyText : '',
				forceSelection : true,
				triggerAction : 'all',
				mode : 'local',
				readOnly : true,
				displayField : 'text',
				valueField : 'value',
				store : new Ext.data.JsonStore({
							fields : ['value', 'text'],
							data : [{
										value : CodeConstants.MEAL_PLACE_1,
										text : '餐厅'
									}, {
										value : CodeConstants.MEAL_PLACE_2,
										text : '工作地'
									}]
						})
			});
	// 设置就餐地点默认值***************餐厅
	drpAddress.setValue(CodeConstants.MEAL_PLACE_1);
	// 第四行
	var fldSetFouthLineQuest = new Ext.form.FieldSet({
				border : false,
				layout : 'form',
				style : "padding-bottom:0px;border:0px",
				// height : 20,
				anchor : '100%',
				items : [drpAddress]
			})

	// fieldSet
	var fldSetDetail = new Ext.form.FieldSet({
		title : '订餐信息',
		frame : true,
		labelAlign : 'right',
		height : 176,
		width : 450,
		labelWidth : 55,
		style : "padding-top:10px;margin-right:0px;margin-left:0px;padding-bottom:0px;margin-bottom:0px",
		items : [fldSetFirstLineQuest, fldSetSecondLineQuest,
				fldSetThirdLineQuest, fldSetFouthLineQuest]
	});
	/** *********订餐信息框架结束********** */
	/** ********第二个框架开始*********** */
	// 第四行第一个按钮
	var btnMenu = new Ext.Button({
				text : '点菜',
				handler : function() {
					subWindow.show();
					subWindow.setTitle('菜谱选择');
					
					// 设置菜谱选择画面的订餐类别的初始数据
					var strType = drpMenuType.value;

					txtSubMenuType.setValue(drpMenuType.el.dom.value);
					// 菜谱选择画面日期初始值 
					txtsubMenuDate.setValue(Ext.get('menuDate').dom.value);
					var strDate = Ext.get('menuDate').dom.value;
					
					// 加载菜谱选择画面数据
					menuChooseGridStore.baseParams={
					         strDate : strDate,
							 strType : strType
					};
					menuChooseGridStore.load({
								params : {
									start : 0,
									limit : Constants.PAGE_SIZE
								}
							});
				}
			});
	// 第四行第二个按钮
	var btnDeleteInfo = new Ext.Button({
				text : '删除点菜明细',
				handler : function() {
					var choosedRecord = subGrid.selModel.getSelected();
					// 如果选中，则进行删除操作
					if (choosedRecord) {
						// 如果是合计行，则不能被删除
						// flag为z则为合计行记录
						if (choosedRecord.get('flag') != 'e') {
							// flag为0 则为更新的数据记录
							// flag为1则为插入的数据记录
							if (choosedRecord.get('flag') == '0') {
								// flag为2则为删除
								var record = choosedRecord.copy();
								record.set('flag', '2');
								subCopyGridStore.add(record);
							}
							subGridStore.remove(choosedRecord);
							// 如果子gridstore为空
							if (subGridStore.getCount() == 1) {
                                subGridStore.removeAll();
								// 禁用保存与发送按钮
								btnSave.disable();
								btnSend.disable();
								// 禁用删除明细
								btnDeleteInfo.disable();
								btnCancleInfo.enable();
							}else{
								totalSum = 0;
							var sum = "";
							totalPrice = 0;
							var price = "";
							for (i = 0; i < subGridStore.getCount() - 1; i++) {
								sum = subGridStore.getAt(i).get('menuAmount')
										* 1;
								totalSum += sum;
								price = subGridStore.getAt(i).get('menuTotal')
										* 1;
								totalPrice += price;
							}
							subGridStore.getAt(subGridStore.getCount() - 1)
									.set('menuAmount', totalSum);
							subGridStore.getAt(subGridStore.getCount() - 1)
									.set('menuTotal', totalPrice);
							subGrid.getView().refresh();
							}
							
						}
					} else {
						Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
								MessageConstants.COM_I_001);
					}
				}
			});
	var btnCancleInfo = new Ext.Button({
				text : '取消点菜明细',
				handler : cancelMenuDetail
			});
	// 第四行面板

	// 个人订餐画面subgrid所需的类定义
	var MySubGridRecord = new Ext.data.Record.create([{
				name : 'menuCode'
			}, {
				name : 'MId'
			}, {
				name : 'menuName'
			}, {
				name : 'menuTypeName'
			}, {
				name : 'menuAmount'
			}, {
				name : 'menuPrice'
			}, {
				name : 'menuTotal'
			}, {
				name : 'memo'
			}, {
				name : 'flag'
			}, {
				name : 'id'
			}, {
				name : 'strUpdateTime'
			}]);
	// 个人订餐画面grid中的store定义
	var subGridStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'administration/getIndividualSubMenuInfo.action'
						}),
				reader : new Ext.data.JsonReader({
							root : 'list',
							totalProperty : 'totalCount'
						}, MySubGridRecord)

			});
	// 删除时要用到的gridstore
	var subCopyGridStore = new Ext.data.Store(MySubGridRecord);
	// 编辑控件
	var amount = new Ext.form.NumberField({
				id : 'amount',
				maxValue : 9999999999,
				minValue : 0,
				decimalPrecision : 0
			});
	// 编辑控件
	var memo1 = new Ext.form.TextField({
				id : 'memo1'
			});
	// 截取备注信息
	memo1.on('blur', function() {
				if (memo1.getValue().length * 1 >= 127) {
					memo1.setValue(memo1.getValue().substring(0, 127));
				}
			});

	memo1.on('change', function() {
				btnMenu.enable();
				window.buttons[0].enable();
				window.buttons[1].enable();
			});
     // 备注赵明建追加
	var txtNote = new Ext.form.TextField({
				id : 'note',
				maxLength : 100,
				listeners : {
					"render" : function() {
						this.el.on("dblclick", cellClickHandler);
					}
				}
			});
	// 子个人订餐画面信息
	var subGridCM = new Ext.grid.ColumnModel([
			// 自动生成行号
			new Ext.grid.RowNumberer({
						header : '行号',
						width : 35
					}), {
				hidden : true,
				dataIndex : 'MId'
			}, {
				header : '菜谱名称',
				align : 'left',
				width : 68,
				dataIndex : 'menuName'
			}, {
				header : '菜谱类别',
				align : 'left',
				width : 70,
				dataIndex : 'menuTypeName'
			}, {
				header : '份数',
				align : 'right',
				width : 45,
				sortable : false,
				dataIndex : 'menuAmount',
				editor : amount,
				renderer : function(value, cellmeta, record, rowIndex,
						columnIndex, store) {
					// 如果不是最后一行
					if (rowIndex < store.getCount() - 1) {
						var totalSum = 0;
						// 对该列除最后一个单元格以为求和
						for (var i = 0; i < store.getCount() - 1; i++) {
							totalSum += store.getAt(i).get('menuAmount')
									* 1;
						}
						if(store.getAt(store.getCount() - 1)
									.get('flag') == 'e')
							 store.getAt(store.getCount() - 1).set('menuAmount',totalSum);
						return numberWithSub(value);
						// 如果是最后一行
					} else {
						var totalSum = 0;
						// 对该列除最后一个单元格以为求和
						for (var i = 0; i < store.getCount() - 1; i++) {
							totalSum += store.getAt(i).get('menuAmount')
									* 1;
						}
						return numberWithSub(totalSum);
					}
				}
			}, {
				header : '单价',
				align : 'right',
				width : 58,
				sortable : true,
				dataIndex : 'menuPrice',
				renderer : function(value, cellmeta, record, rowIndex,
						columnIndex, store) {
					// 如果不是最后一行
					if (rowIndex < store.getCount() - 1) {
						return numberFormat(value);
					} else {
						return null;
					}
				}
			}, {
				header : '合计',
				width : 58,
				align : 'right',
				sortable : false,
				dataIndex : 'menuTotal',
				renderer : function(value, cellmeta, record, rowIndex,
						columnIndex, store) {
					// 如果不是最后一行
					if (rowIndex < store.getCount() - 1) {
						
						value = store.getAt(rowIndex).get('menuAmount')* store.getAt(rowIndex).get('menuPrice');
						record.set('menuTotal', value);
						totalSum = 0;
						var total = "";
						// 对该列除最后一个单元格以为求和
						for (var i = 0; i < store.getCount() - 1; i++) {
							total = store.getAt(i).get('menuTotal') * 1;
							if (total) {
								totalSum += total;
							}
						}
						if(store.getAt(store.getCount() - 1)
									.get('flag') == 'e')
							store.getAt(store.getCount()-1).set('menuTotal', totalSum);
						return numberFormat(value);
						// 如果是最后一行
					} else {
						totalSum = 0;
						var total = "";
						// 对该列除最后一个单元格以为求和
						for (var i = 0; i < store.getCount() - 1; i++) {
							total = store.getAt(i).get('menuTotal') * 1;
							if (total) {
								totalSum += total;
							}
						}
						return numberFormat(totalSum);
					}
				}

			}, {
				header : '备注',
				width : 100,
				align : 'left',
				sortable : true,
				editor :txtNote,
				dataIndex : 'memo'
			}, {
				hidden : true,
				dataIndex : id
			}]);
	// 第五行第一个子面板***********个人订餐画面
	var subGrid = new Ext.grid.EditorGridPanel({
		store : subGridStore,
		height : 200,
		width : 450,
		style : "margin-left:0px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		cm : subGridCM,
		frame : true,
		border : true,
		clicksToEdit : 1,
		tbar : [btnMenu, btnDeleteInfo, btnCancleInfo],
		sm : new Ext.grid.RowSelectionModel({
					singleSelect : true
				}),
		enableColumnHide : false,
		enableColumnMove : false
	});
	//添加事件


    // 备注-弹出窗口
    var memoText = new Ext.form.TextArea({
                id : "memoText",
                maxLength : 127,
                width : 180
            });
	// 弹出画面
    var win = new Ext.Window({
                height : 170,
                width : 350,
                layout : 'fit',
                resizable : false,
                modal : true,
                closeAction : 'hide',
                items : [memoText],
                buttonAlign : "center",
                title : '详细信息录入窗口',
                buttons : [{
                    text : Constants.BTN_CONFIRM,
                    iconCls : Constants.CLS_OK,
                    handler : function() {
                                        var rec = subGrid
                                                .getSelectionModel()
                                                .getSelections();
                                        if (Ext.get("memoText").dom.value.length <= 127
                                                && memoText.isValid()) {
                                            rec[0].set('memo',Ext.get("memoText").dom.value);
                                            win.hide();
                                        }
                                    

                    }
                }, {
                    text : Constants.BTN_CANCEL,
                    iconCls : Constants.CLS_CANCEL,
                    handler : function() {
                        win.hide();
                    }
                }]
            });

	//双击编辑控件函数
	function cellClickHandler(){
		var record = subGrid.selModel.getSelected();
		win.show();
		memoText.setValue(record.get("memo"));
	}
    
	// 禁用合计行
	subGrid.on("beforeedit", function(obj) {
				if (obj.row == subGridStore.getCount() - 1) {
					obj.cancel = true;
				}
			});

	// 第五行面板
	var fifthLine = new Ext.Panel({
		border : false,
		layout : "form",
		width : 460,
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		items : [{
					columnWidth : 0.99,
					layout : "form",
					border : false,
					items : [subGrid]
				}]
	});

	// 定义弹出窗体中的addform
	var formPanel = new Ext.form.FormPanel({
		labelAlign : 'right',
		height : 320,
		frame : true,
		border : true,
		width : 460,
		autoScroll : true,
		style : "padding-top:0px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		items : [fldSetDetail, fifthLine]
	});
	// 定义弹出窗口
	// 保存按钮
	var btnSave = new Ext.Button({
				text : Constants.BTN_SAVE,
				iconCls : Constants.CLS_SAVE,
				handler : function() {
					strFlag = 'add';
					confirmRecord();
				}
			});
	// 发送按钮
	var btnSend = new Ext.Button({
				text : Constants.BTN_SEND,
				iconCls : Constants.CLS_SEND,
				handler : function() {
					strFlag = 'send';
					confirmRecord();
				}
			});
	// 第一子画面取消按钮
	var btnCancle = new Ext.Button({
				text : Constants.BTN_CANCEL,
				id : 'cancle1',
				iconCls : Constants.CLS_CANCEL,
				handler : function() {
					Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
							MessageConstants.COM_C_005, function(buttonobj) {
								if (buttonobj == 'yes') {
									window.hide();
								}
							});
				}
			});
	var window = new Ext.Window({
				width : 503,
				height : 320,
				title : "增添/修改",
				layout : 'fit',
				autoScroll : true,
				closeAction : 'hide',
				modal : true,
				buttonAlign : "center",
				resizable : false,
				items : [formPanel],
				buttons : [btnSave, btnSend, btnCancle]
			});

	// ***************************菜谱选择画面定义 ************************
	var dteFrom1 = new Date();
	dteFrom1.setDate(dteFrom1.getDate());
	dteFrom1 = dteFrom1.format('Y-m-d');
	// 第一行第一列日期
	var txtsubMenuDate = new Ext.form.TextField({
				format : 'Y-m-d',
				id : 'subMenuDate',
				fieldLabel : '订餐日期',
				name : 'subMenuDate',
				clearCls : 'allow-float',
				checked : true,
				width : 80,
				value : dteFrom1,
				readOnly : true,
				anchor : '100%'
			});

	// 第一行第二列
	var txtSubMenuType = new Ext.form.TextField({
				fieldLabel : "订餐类别",
				id : "subMenuType",
				readOnly : true,
				name : "subMenuType",
				allowBlank : false,
				width : 80,
				anchor : '100%'
			});

	// 第一行第三列 选择按钮
	var btnChoose = new Ext.Button({
				text : '选择',
				iconCls : Constants.CLS_OK,
				align : 'right',
				handler : function() {
					var choosedRecord = sm.getSelections();
					// 如果选择行小于0
					if (choosedRecord.length <= 0) {
						Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
								MessageConstants.COM_I_001);
					} else {
						// 如果原来已经存在record数据,则删除最后一条record
						if (subGridStore.getCount() * 1 != 0) {
							subGridStore.remove(subGridStore.getAt(subGridStore
									.getCount()
									* 1 - 1));
						}
						for (var i = 0; i < choosedRecord.length; i++) {
							// 把菜谱选则画面的数据添加到个人订餐画面中
							// 判断是否该选择的菜谱已经在个人订餐中存在，如果存在的话那么只要相加份数，而如果不存在那么就要增加这样一条记录
							var k = getNumber(subGridStore, choosedRecord[i]);
							// k=-1情况则是插入数据,否则则更新数据并且k是该相同记录的编号
							if (k == -1) {
								// 申明MySubGridRecord
								var record = new MySubGridRecord({
											menuCode : ''
										});
								// 添加菜谱编码
								record.set('menuCode', choosedRecord[i]
												.get('menuCode'));
								// 添加菜谱名称
								record.set('menuName', choosedRecord[i]
												.get('menuName'));
								// 添加菜谱类型名称
								record.set('menuTypeName', choosedRecord[i]
												.get('menuTypeName'));
								// 添加份数
								record.set('menuAmount', '1');
								// 添加单价
								record.set('menuPrice', choosedRecord[i]
												.get('menuPrice'));
								// 添加合计
								record.set('menuTotal', choosedRecord[i]
												.get('menuPrice'));
								// 添加标志
								record.set('flag', '1');
								//
								record.set('MId', "");
								// 添加注释
								record.set('memo', '');

								subGridStore.insert(
										subGridStore.getCount() * 1, record);
							} else {
								// 在更新的情况下
								subGridStore.getAt(k).set(
										'menuAmount',
										subGridStore.getAt(k).get('menuAmount')
												* 1 + 1);
								subGridStore.getAt(k).set(
										'menuTotal',
										subGridStore.getAt(k).get('menuTotal')
												* 1
												+ choosedRecord[i]
														.get('menuPrice') * 1);
							}
						}
						// 重新计算数据
						totalSum = 0;
						var sum = "";
						totalPrice = 0;
						var price = "";
						for (i = 0; i < subGridStore.getCount(); i++) {
							sum = subGridStore.getAt(i).get('menuAmount') * 1;
							totalSum += sum;
							price = subGridStore.getAt(i).get('menuTotal') * 1;
							totalPrice += price;
						}
						var record = new MySubGridRecord({
									countType : "total"
								});
						record.set('menuAmount', totalSum);
						record.set('menuTotal', totalPrice);
						record.set('flag', 'e');
						subGridStore
								.insert(subGridStore.getCount() * 1, record);

						subWindow.hide();
						subGrid.getView().refresh();
					}
				}
			});
	// 第一行第四列
	var btnsubCancel = new Ext.Button({
				text : Constants.BTN_CANCEL,
				iconCls : Constants.CLS_CANCEL,
				handler : function() {
					Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
							MessageConstants.COM_C_005, function(buttonobj) {
								if (buttonobj == 'yes') {
									subWindow.hide();
								}
							});

				}
			});
	// 第一行面板

	// 第二行第一个子面板***********菜谱选择画面
	// 菜谱选择画面所需的类定义
	var MyMenuChooseRecord = new Ext.data.Record.create([{
				name : 'id'
			}, {
				name : 'menuCode'
			}, {
				name : 'menuName'
			}, {
				name : 'menuTypeName'
			}, {
				name : 'menuPrice'
			}, {
				name : 'memo'
			}]);
	// 菜谱选择订餐画面grid中的store定义
	var menuChooseGridStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'administration/getIndividualMenuChooseInfo.action'
						}),
				reader : new Ext.data.JsonReader({
							root : 'list',
							totalProperty : 'totalCount'
						}, MyMenuChooseRecord)

			});
	// 菜谱选择sm
	var sm = new Ext.grid.CheckboxSelectionModel({
				id : 'select',
				align : 'right',
				header : '选择',
				sortable : true,
				width : 34,
				onMouseDown : function(e, t) {
					if (e.button === 0) {														// left-click
						e.stopEvent();
						var row = e.getTarget('.x-grid3-row');
						if (row) {
							var rowIndex = row.rowIndex;
							var isSelected = this.isSelected(rowIndex);
                            if (isSelected) {
                                this.deselectRow(rowIndex);
                            } else if (!isSelected || this.getCount() > 1) {
                                this.selectRow(rowIndex, true);
                                  var view = this.grid.getView();
                                view.focusRow(rowIndex);
                            }
						}
					}
				}
			});
	// 菜谱选择画面信息
	var menuChooseGridCM = new Ext.grid.ColumnModel([
			// 自动生成行号
			new Ext.grid.RowNumberer({
						header : '行号',
						width : 35
					}), {
				hidden : true,
				dataIndex : 'mId'
			}, {
				hidden : true,
				dataIndex : 'menuCode'
			}, {
				header : '菜谱名称',
				align : 'left',
				width : 60,
				sortable : true,
				dataIndex : 'menuName'
			}, {
				header : '菜谱类别',
				align : 'left',
				width : 60,
				sortable : true,
				dataIndex : 'menuTypeName'
			}, {
				header : '单价',
				width : 60,
				align : 'right',
				sortable : true,
				dataIndex : 'menuPrice',
				renderer : function(value, cellmeta, record, rowIndex,
						columnIndex, store) {
					// 如果不是最后一行
					return numberFormat(value);
				}
			}, {
				header : '备注',
				width : 100,
				align : 'left',
				sortable : true,
				dataIndex : 'memo'
			}, sm]);
	//加载事件
	menuChooseGridStore.on('load',function(){
	     menuChooseGrid.getView().refresh();
	});

	// 菜谱选择画面的分页
	var btmChooseToolbar = new Ext.PagingToolbar({
				pageSize : Constants.PAGE_SIZE,
				store : menuChooseGridStore,
				displayInfo : true,
				displayMsg : MessageConstants.DISPLAY_MSG,
				emptyMsg : MessageConstants.EMPTY_MSG
			});
	// 第二行第一个***********菜谱选择画面

	//定义菜谱选择窗口的gridpanel
	var menuChooseGrid = new Ext.grid.GridPanel({
		store : menuChooseGridStore,
		height : 288,
		width : 490,
		style : "padding-top:0px;padding-left:0px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		cm : menuChooseGridCM,
		frame : false,
		border : false,
		sm : sm,
		tbar : ['日期:', txtsubMenuDate, '-', '订餐类别:', txtSubMenuType, '-',
				btnChoose, btnsubCancel],
		bbar : btmChooseToolbar,
		enableColumnHide : true,
		enableColumnMove : false
	});
	    // 备注-弹出窗口
    var memoTextByChoose = new Ext.form.TextArea({
                id : "memoTextByChoose",
                maxLength : 127,
                readOnly :true,
                width : 180
            });
	// 弹出画面
    var winByChoose = new Ext.Window({
                height : 170,
                width : 350,
                layout : 'fit',
                resizable : false,
                modal : true,
                closeAction : 'hide',
                items : [memoTextByChoose],
                buttonAlign : "center",
                title : '详细信息查看窗口',
                buttons : [{
                    text : Constants.BTN_CLOSE,
                    iconCls : Constants.CLS_CANCEL,
                    handler : function() {
                        winByChoose.hide();
                    }
                }]
            });

     menuChooseGrid.on('celldblclick',cellDbClick);
    // 单元格双击处理函数
	function cellDbClick(grid, rowIndex, columnIndex, e){
		     if(columnIndex == 6){
		      memoTextByChoose.setValue(menuChooseGridStore.getAt(rowIndex).get("memo"));
			  winByChoose.show();
	         }
	         
	}
	// 第2行面板
	var menuChooseSecondLine = new Ext.Panel({
		border : false,
		layout : "form",
		style : "padding-top:0px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
					columnWidth : 0.99,
					layout : "form",
					border : false,
					items : [menuChooseGrid]
				}]
	});

	// 菜谱选择弹出窗体中form
	var subFormPanel = new Ext.form.FormPanel({
		labelAlign : 'right',
		height : 310,
		border : true,
		style : "padding-top:0px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		items : [menuChooseSecondLine]
	});
	// 菜谱选择弹出窗口
	var subWindow = new Ext.Window({
				width : 503,
				height : 320,
				title : "添加/修改",
				layout : 'fit',
				closeAction : 'hide',
				modal : true,
				buttonAlign : "center",
				resizable : false,
				items : [subFormPanel]
			});
	// *******************事件响应处理******方法定义**********************************
	// 加载事件
	subGridStore.on('load', function(store, index) {
				// 清空
				totalSum = 0;
				var sum = "";
				totalPrice = 0;
				var price = "";
				for (i = 0; i < subGridStore.getCount(); i++) {
					sum = subGridStore.getAt(i).get('menuAmount') * 1;
					totalSum += sum;
					price = subGridStore.getAt(i).get('menuTotal') * 1;
					totalPrice += price;
				}
                 
				var record = new MySubGridRecord({
							countType : "total"
						});
				// 设置合计标志
				record.set('flag', 'e');
				record.set('menuAmount', totalSum);
				record.set('menuTotal', totalPrice);
				// 删除掉所有copy的值
				subCopyGridStore.removeAll();
                if(subGridStore.getCount()>0)
				    subGridStore.add(record);
				    if(menuInfo.getValue() !='填写'){
				       	btnMenu.disable();
				       	btnDeleteInfo.disable();
				       	btnCancleInfo.disable();
				       	btnSave.disable();
				       	btnSend.disable();
				    }
				subGrid.getView().refresh();
			});
	// 初始化个人订餐画面时根据调用方法设置按钮
	subGridStore.on('add', function(store, index) {
				if (subGridStore.getCount() > 0) {
					btnDeleteInfo.enable();
					btnMenu.enable();
					btnCancleInfo.enable();
					window.buttons[0].enable();
					window.buttons[1].enable();
					// 增加记录进行加减

				} else {
					btnDeleteInfo.disable();
					btnMenu.enable();
					btnCancleInfo.disable();
					window.buttons[0].disable();
					window.buttons[1].disable();
				}
				subGrid.getView().refresh();
			});
	// 移除数据时触发事件

	// 双击个人订餐行触发事件
	// subGrid.on('rowdblclick',updateRecord);
			
			Ext.Ajax.request({
					method : Constants.POST,
					url : 'administration/getMenuUserNameInfo.action',
					success : function(result, request) {
						var o = eval("(" + result.responseText + ")");
						if (o.msg == 'U') {
							Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
									MessageConstants.COM_I_002);
						} else {
							insertBy.setValue(o.user);
							deptBy.setValue(o.dept);
						}
					},
					failure : function() {
						Ext.Msg.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
					}
				});
			
	/**
	 * 增加函数
	 */
   
	function addRecord() {
		strMethod = "add";
		// 设置strMId
		strMId = '';
		// 设置按钮,部门按钮有效
		drpMenuType.enable();
		btnDeleteInfo.disable();
		// 添加指针
		txtmenuDate.addClass('cursor:pointer');
		btnCancleInfo.disable();
		btnMenu.enable();
		window.buttons[0].disable();
		window.buttons[1].disable();
		// 默认初始订餐类别
		txtSubMenuType.setValue('1');
		txtmenuDate.reset();
        
		subGridStore.removeAll();

		
		formPanel.getForm().reset();
		window.setTitle("新增订餐");
		window.show();
		var dteInit = getCurrentDate();
		txtmenuDate.setValue(dteInit);
	}
	/**
	 * 修改函数
	 */
	function updateRecord() {
		// 选择行
		var choosedRecord = mainGrid.getSelectionModel().getSelected();
		strMethod = 'update';

		// 判断行选择
		if (choosedRecord) {

			strMId = choosedRecord.get('MId');
			// 如果子gridstore中有值，则把按钮部分有效
			// 订餐日期禁用
			// 订餐类型
			btnDeleteInfo.disable();
			btnMenu.enable();

			btnCancleInfo.disable();
			window.buttons[0].disable();
			window.buttons[1].disable();

			// 加载用户名与部门名
			formPanel.getForm().reset();
            // TODO 设置画面的滚动条位置
			Ext.Ajax.request({
						method : Constants.POST,
						url : 'administration/getMenuUserNameInfo.action',
						success : function(result, request) {
							var o = eval("(" + result.responseText + ")");
							if (o.msg == 'DATA') {
								Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
										MessageConstants.COM_I_003);
							} else {
								insertBy.setValue(o.user);
								deptBy.setValue(o.dept);
							}
						},
						failure : function() {
							Ext.Msg.alert(Constants.ERROR,
									Constants.UNKNOWN_ERR);
						}
					});
			// 加载个人订餐画面信息
			// 设置点菜一览的日期按钮

			var mId = choosedRecord.get('MId');
			var menuType = choosedRecord.get('menuType');
			var menuDate = choosedRecord.get('menuDate');
			txtmenuDate.setValue(menuDate);
			// 判断
			if (menuType == '早餐') {
				drpMenuType.setValue('1');
			} else if (menuType == '中餐') {
				drpMenuType.setValue('2');
			} else if (menuType == '晚餐') {
				drpMenuType.setValue('3');
			} else {
				drpMenuType.setValue('4');
			}
			subGridStore.removeAll();
			subGridStore.load({
						params : {
							mId : mId,
							start : 0,
							limit : Constants.PAGE_SIZE
						}
					});
			// 循环store取得其值
		     
            var strMenuInfo = choosedRecord.get('menuInfo');
            if(strMenuInfo =='1'){
              menuInfo.setValue('填写');
			}else if(strMenuInfo == '2'){
			  menuInfo.setValue('审核');
			}else if(strMenuInfo == '3'){
				menuInfo.setValue('已发送');
			}
            //添加地址
            var strPlace = choosedRecord.get('place');
            drpAddress.setValue(strPlace);
            //设置窗口标题
            window.setTitle("修改订餐");
			window.show();
		} else {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
		}
	}

	/**
	 * 删除函数
	 */
	function deleteRecord() {
		var choosedRecord = mainGrid.getSelectionModel().getSelected();
		if (choosedRecord) {

			var id = choosedRecord.get('MId');
			// 更新时间
			var updateTime = choosedRecord.get('strUpdateTime');

			Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
					MessageConstants.COM_C_002, function(buttonobj) {
						if (buttonobj == 'yes') {
							// 删除操作
							Ext.Ajax.request({
								method : Constants.POST,
								url : 'administration/deleteIndividualMenuInfo.action',
								params : {
									updateTime : updateTime,
									id : id
								},
								success : function(result, request) {
									var result = eval("(" + result.responseText
											+ ")");
									if (result.msg == Constants.DATA_USING) {
										// 排他处理
										Ext.Msg.alert(
												MessageConstants.SYS_ERROR_MSG,
												MessageConstants.COM_I_002);
										return;
									} else if (result.msg == Constants.SQL_FAILURE) {
										Ext.Msg.alert(
												MessageConstants.SYS_ERROR_MSG,
												MessageConstants.COM_E_014);
										return;
									} else if (result.msg == Constants.DATE_FAILURE) {
										Ext.Msg.alert(
												MessageConstants.SYS_ERROR_MSG,
												MessageConstants.COM_E_023);
										return;
									} else {
										Ext.Msg
												.alert(
														MessageConstants.SYS_REMIND_MSG,
														MessageConstants.COM_I_005,
														function() {
															gridStore.load({
																params : {
																	start : 0,
																	limit : Constants.PAGE_SIZE
																}
															});
															mainGrid.getView()
																	.refresh();
														});
									}
								},
								failure : function() {
									Ext.Msg.alert(
											MessageConstants.SYS_ERROR_MSG,
											MessageConstants.UNKNOWN_ERR);
								}
							});
						}
					});

		} else {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
		}
	}
	/**
	 * 按下保存按钮动作
	 */
	function confirmRecord() {
		// 判断是否重复

		if (checkTime()) {
			var strDate = Ext.get('menuDate').dom.value;
			var strType = drpMenuType.getValue();
			if (strMethod == 'add') {
				Ext.Ajax.request({
					method : Constants.POST,
					url : 'administration/checkDataRepeat.action',
					params : {
						strDate : strDate,
						strType : strType
					},
					success : function(result, request) {
						var o = eval("(" + result.responseText + ")");
						if (o.msg == 'U') {
							if (strType == '1') {
								Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
										String.format(
												MessageConstants.AD004_E_001,
												strDate, '早餐'));
							} else if (strType == '2') {
								Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
										String.format(
												MessageConstants.AD004_E_001,
												strDate, '中餐'));
							} else if (strType == '3') {
								Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
										String.format(
												MessageConstants.AD004_E_001,
												strDate, '晚餐'));
							} else {
								Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
										String.format(
												MessageConstants.AD004_E_001,
												strDate, '宵夜'));
							}
							return;
						} else {
							if (strFlag == 'add') {
								Ext.Msg.confirm(
										MessageConstants.SYS_CONFIRM_MSG,
										MessageConstants.COM_C_001,
										pressYesButton);
							} else {
								Ext.Msg
										.confirm(
												MessageConstants.SYS_CONFIRM_MSG,
												'&nbsp&nbsp&nbsp确认要发送吗？&nbsp&nbsp&nbsp',
												pressYesButton);
							}

						}
					},
					failure : function() {
						Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
								MessageConstants.COM_E_014);
					}
				});
			} else {
				if (strFlag == 'add') {
					Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
							MessageConstants.COM_C_001, pressYesButton);
				} else {
					Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
							'&nbsp&nbsp&nbsp确认要发送吗？&nbsp&nbsp&nbsp',
							pressYesButton);
				}

			}
		} else {
			return;
		}
	}
	/**
	 * 按下YES按钮动作
	 */
	function pressYesButton(buttonobj) {
		// 判断用户所按按钮值
		if (buttonobj == 'yes') {
			// action
			var myurl = 'administration/saveUserMenuAndUserSubInfo.action';
			// 查询日期
			var strDate = Ext.get('menuDate').dom.value;
			// 订餐地址
			var strPlace = drpAddress.getValue();
			// 订餐类别
			var strType = drpMenuType.getValue();
			// 填单日期
			var insertDate = Ext.get('insertDate').dom.value;
			// 在新增情况下
			if (strMethod == 'add') {
				// 插入用户点菜表
				var strUpdate = [];
				// grid的批量数据的取得
				for (i = 0; i < subGridStore.getCount() - 1; i++) {
					// 序列化数据
					strUpdate.push(subGridStore.getAt(i).data);
				}

				// 传送数据
				Ext.Ajax.request({
					method : Constants.POST,
					url : myurl,
					params : {
						strFlag : strFlag,
						strPlace : strPlace,
						strDate : strDate,
						strType : strType,
						insertDate : insertDate,
						strUpdate : Ext.util.JSON.encode(strUpdate),
						strMethod : strMethod
					},
					success : function(result, request) {
						var result = eval("(" + result.responseText + ")");
						if (result.msg == Constants.SQL_FAILURE) {
							Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
									MessageConstants.COM_E_014);
							return;
						} else if (result.msg == Constants.DATE_FAILURE) {
							Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
									MessageConstants.COM_E_023);
							return;
						} else {
							// 成功
							if (strFlag == 'add') {
								Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
										MessageConstants.COM_I_004, function() {
											// 重新载入
											gridStore.load({
														params : {
															start : 0,
															limit : Constants.PAGE_SIZE
														}
													});
											// 页面初始化
											mainGrid.getView().refresh();
											window.hide();
										});
							} else {
								Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
										MessageConstants.REPORT_SUCCESS,
										function() {
											// 重新载入
											gridStore.load({
														params : {
															start : 0,
															limit : Constants.PAGE_SIZE
														}
													});
											// 页面初始化
											mainGrid.getView().refresh();
											window.hide();
										});
							}

						}
					},
					failure : function() {
						Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
								MessageConstants.UNKNOWN_ERR);
					}
				});
			}
			// 在修改已存在情况下
			if (strMethod == 'update') {
				// 第二个request
				var strUpdate = [];
                // 更新时间
				var choosedRecord = mainGrid.getSelectionModel().getSelected();
				var strUpdateTime = choosedRecord.get('strUpdateTime');
				for (i = 0; i < subGridStore.getCount() - 1; i++) {
					// 序列化数据
					strUpdate.push(subGridStore.getAt(i).data);
				}
				// 要删除的记录序列化
				for (j = 0; j < subCopyGridStore.getCount(); j++) {
					strUpdate.push(subCopyGridStore.getAt(j).data);
				}
				// 传送数据
				Ext.Ajax.request({
					method : Constants.POST,
					url : myurl,
					params : {
                        strUpdateTime : strUpdateTime,
						strMId : strMId,
						strFlag : strFlag,
						strPlace : strPlace,
						strDate : strDate,
						strType : strType,
						insertDate : insertDate,
						strUpdate : Ext.util.JSON.encode(strUpdate),
						strMethod : strMethod
					},
					success : function(result, request) {
						var result = eval("(" + result.responseText + ")");
						if (result.msg == Constants.DATA_USING) {
							// 排他处理
							Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
									MessageConstants.COM_I_002);
							return;
						} else if (result.msg == Constants.SQL_FAILURE) {
							Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
									MessageConstants.COM_E_014);
							return;
						} else if (result.msg == Constants.DATE_FAILURE) {
							Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
									MessageConstants.COM_E_023);
							return;
						} else {
							// 成功
							if (strFlag == 'add') {
								Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
										MessageConstants.COM_I_004, function() {
											// 重新载入
											gridStore.load({
														params : {
															start : 0,
															limit : Constants.PAGE_SIZE
														}
													});
											// 页面初始化
											mainGrid.getView().refresh();
											window.hide();
										});
							} else {
								Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
										MessageConstants.REPORT_SUCCESS,
										function() {
											// 重新载入
											gridStore.load({
														params : {
															start : 0,
															limit : Constants.PAGE_SIZE
														}
													});
											// 页面初始化
											mainGrid.getView().refresh();
											window.hide();
										});
							}
						}
					},
					failure : function() {
						Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
								MessageConstants.UNKNOWN_ERR);
					}
				});
			}
		}
	}
	// 如果菜谱编码相同返回行号，否则返回-1
	function getNumber(store, record) {
		for (var i = 0; i < store.getCount(); i++) {

			if (store.getAt(i).get('menuCode') == record.get('menuCode')) {
				return i;
			}
		}
		return -1;
	}
	/**
	 * 大数字中间用','分隔 小数点后3位
	 */
	function numberFormat(v,argDecimal) {
		if (v) {
            if (typeof argDecimal != 'number') {
                argDecimal = 2;
            }
            v = Number(v).toFixed(argDecimal);
            var t = '';
            v = String(v);
            while ((t = v.replace(/^(-?[0-9]+)([0-9]{3}.*)$/, '$1,$2')) !== v)
                v = t;
            
            return v+"元";
        } else
            return "0.00元";
	}

	/**
	 * 份数追加逗号
	 */
	function numberWithSub(value) {
		value = value * 1;
		value = String(value);
		// 整数部分
		var whole = value;
		var r = /(\d+)(\d{3})/;
		while (r.test(whole)) {
			whole = whole.replace(r, '$1' + ',' + '$2');
		}
		return whole;
	}
	// 时间check
	function checkTime() {
		var endDate = txtmenuDate.getValue();
		var startDate = new Date().format('Y-m-d');
		if (startDate != "" && endDate != "") {
			var res = compareDateStr(startDate, endDate);
			if (res) {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
								MessageConstants.COM_E_004, "订餐日期"));
				return false;
			}
		}
		return true;
	}
	/**
	 * textField显示时间比较方法
	 */
	function compareDate(argDate1, argDate2) {
		return argDate1.getTime() > argDate2.getTime();
	}
	/**
	 * textField显示时间比较方法
	 */
	function compareDateStr(argDateStr1, argDateStr2) {
		var date1 = Date.parseDate(argDateStr1, 'Y-m-d');
		var date2 = Date.parseDate(argDateStr2, 'Y-m-d');
		return compareDate(date1, date2);
	}
	// 用餐类别的选中值
	var strMenuType = drpMenuType.getValue();
	drpMenuType.on('select', selectMenuType);
	/**
	 * 用餐类别DRP的点击事件
	 */
	function selectMenuType() {
		if (strMenuType == drpMenuType.getValue()) {
			return;
		} else {
			if (subGridStore.getCount() > 0) {
				Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, String
								.format(MessageConstants.AD004_C_001),
						handleChangeMenuType);
			} else {
				strMenuType = drpMenuType.getValue();
				return;
			}
		}
	}
	/**
	 * 用餐类别DRP改变时的处理
	 */
	function handleChangeMenuType(obj) {
		if (obj == 'no') {
			drpMenuType.setValue(strMenuType);
			return;
		} else {
			strMenuType = drpMenuType.getValue();
			cancelMenuDetail();
			subGrid.getView().refresh();
		}
	}
	/**
	 * 取消明细处理
	 */
	function cancelMenuDetail() {

		// 如果strMId非空
		if (strMId != '') {
			subGridStore.load({
						params : {
							mId : strMId
						}
					});
		} else {
			subGridStore.removeAll();
		}
		if (subGridStore.getCount() == 0) {
			// 禁用保存与发送按钮
			btnSave.disable();
			btnSend.disable();
			// 禁用删除明细
			btnDeleteInfo.disable();
			btnCancleInfo.disable();
		}
	}
	/**
	 * 获取当前时间
	 */
	function getCurrentDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t;
		return s;
	}
});
