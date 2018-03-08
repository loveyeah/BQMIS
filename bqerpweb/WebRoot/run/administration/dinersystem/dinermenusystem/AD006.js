Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	// xsTan 追加开始 2009-1-23 合计行追加
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
	// xsTan 追加结束 2009-1-23
	// 系统当天日期
	var dteFrom = new Date();
	dteFrom.setDate(dteFrom.getDate());
	dteFrom = dteFrom.format('Y-m-d');

	// 定义查询日期控件
	var startDate = new Ext.form.TextField({
				id : 'startDate',
				name : 'startDate',
				style : 'cursor:pointer',
				value : dteFrom,
				width : 80,
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
									isShowClear : false,
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd'
								});
					}
				}
			});

	// 用餐类别
	var drpMenuType = new Ext.form.CmbMealType({
				fieldLabel : '订餐类别',
				id : 'menuType',
				name : 'menuType',
				width : 55,
				emptyText : '',
				mode : 'local',
				anchor : '60%'
			});
	drpMenuType.setValue('1');
	// 人员类别
	var drpManType = new Ext.form.ComboBox({
				width : 90,
				allowBlank : true,
				forceSelection : true,
				triggerAction : 'all',
				mode : 'local',
				readOnly : true,
				displayField : 'text',
				valueField : 'value',
				store : new Ext.data.JsonStore({
							fields : ['value', 'text'],
							data : [{
										value : '',
										text : ''
									}, {
										value : CodeConstants.RUN_WORK_NO,
										text : '运行人员'
									}, {
										value : CodeConstants.RUN_WORK_YES,
										text : '非运行人员'
									}]
						})
			});

	// 订餐信息查询record定义
	var recordMy = Ext.data.Record.create([{
				name : 'mId'
			}, {
                // xsTan 删除开始 2009-1-23
//				name : 'menuType'
//			}, {
//				name : 'menuDate'
//			}, {
                // xsTan 删除结束 2009-1-23
				name : 'name'
			}, {
				name : 'deptName'
			}, {
				name : 'insertDate'
			}, {
				name : 'place'
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
			}]);

	// 订餐信息查询代理proxy
	var dataProxy = new Ext.data.HttpProxy({
				url : 'administration/getMenuSystemInfo.action'
			});
	// 订餐信息查询reader
	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, recordMy);
	// 订餐信息查询store
	var storeMy = new Ext.data.Store({
				proxy : dataProxy,
				reader : theReader
			});
	// store加载完数据触发，判断数据是否为空
	storeMy.on("load", function() {
				if (storeMy.getCount() <= 0) {
					   btnPrint.disable();
					   btnExport.disable();
					   btnCount.disable();
                    Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
							MessageConstants.COM_I_003);
				} else {
					btnPrint.enable();
					btnExport.enable();
					btnCount.enable();
                    addLine();
               }
			});
	// xsTan 追加开始 2009-1-23 追加合计
	// 查询结束时插入统计行
	function addLine() {
		// 统计行
		var record = new recordMy({
					countType : "total"
				});
		// 原数据个数
		var count = storeMy.getCount();
		storeMy.insert(count, record);
		grid.getView().refresh();
	};
	// xsTan 追加结束 2009-1-23 追加合计
	// 定义订餐信息查询grid的ColumnModel
	var cmMy = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
						header : "行号",
						width : 35
					}), {
                // xsTan 删除开始 2009-1-23
//				header : "用餐时间",
//				width : 100,
//				dataIndex : 'menuDate'
//			}, {
                // xsTan 删除结束 2009-1-23
				header : "订餐人",
				width : 60,
				dataIndex : 'name'
			}, {
				header : "所属部门",
				width : 100,
				dataIndex : 'deptName'
			}, {
				header : "填单日期",
				width : 70,
				dataIndex : 'insertDate'
			}, {
				header : "就餐地点",
				width : 70,
				dataIndex : 'place'
			}, {
				header : "菜谱名称",
				width : 80,
				dataIndex : 'menuName'
			}, {
				header : "菜谱类别",
				width : 80,
				dataIndex : 'menuTypeName'
			}, {
				header : "份数",
				width : 50,
				align : 'right',
				dataIndex : 'menuAmount',
                renderer : function(value, cellmeta, record, rowIndex,
						columnIndex, store) {
					// 如果不是最后一行
					if (rowIndex < store.getCount() - 1) {
						return numberWithSub(value);
					} else {
						return null;
					}
                }
			}, {
				header : "单价",
				width : 60,
				align : 'right',
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
				header : "合计",
				width : 80,
				align : 'right',
				dataIndex : 'menuTotal',
                // xsTan 追加开始 2003-1-23 追加合计行的值
                renderer : function(value, cellmeta, record, rowIndex,
						columnIndex, store) {
					if (value == "0") {
						return "";
					}
					// 如果不是最后一行
					if (rowIndex < store.getCount() - 1) {
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
               // xsTan 追加结束 2003-1-23、
			}, {
				header : "备注",
				width : 100,
				dataIndex : 'memo'
			}]);
	// 查询按钮
	var btnQuery = new Ext.Button({
				text : Constants.BTN_QUERY,
				id : 'query',
				name : 'query',
				iconCls : Constants.CLS_QUERY,
				handler : queryRecord
			});
	// 打印按钮
	var btnPrint = new Ext.Button({
				text : Constants.BTN_PRINT,
                iconCls : Constants.CLS_PRINT,
				disabled : true,
				handler : printHanler
			});
	// 导出按钮
	var btnExport = new Ext.Button({
				text : Constants.BTN_EXPORT,
				iconCls : Constants.CLS_EXPORT,
				disabled : true,
				name : 'import',
				handler : exportIt
			});
	// 统计按钮
	var btnCount = new Ext.Button({
				text : "统计",
				id : 'count',
				disabled : true,
				name : 'count',
				handler : countRecord
			});
			
	// 备注-弹出窗口
    var memoText = new Ext.form.TextArea({
                id : "memoText",
                maxLength : 127,
                readOnly :true,
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
                title : '详细信息查看窗口',
                buttons : [{
                    text : Constants.BTN_CLOSE,
                    iconCls : Constants.CLS_CANCEL,
                    handler : function() {
                        win.hide();
                    }
                }]
            });

    
    // 单元格双击处理函数
	function cellDbClick(grid, rowIndex, columnIndex, e){
	       if(rowIndex < storeMy.getCount()-1){
	       	if(columnIndex == 10){
		       memoText.setValue(storeMy.getAt(rowIndex).get('memo'));
		       win.show();
		     }
	       }
		    
	
	}
	// 订餐信息查询GridPanel显示格式定义-----开始-------------------
	var grid = new Ext.grid.GridPanel({
				store : storeMy,
				cm : cmMy,
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : true
						}),
				// 顶部工具栏
				tbar : ['订餐日期:', startDate, '-', '订餐类别:', drpMenuType, '-',
						'人员类别:', drpManType, '-', 
						btnQuery, btnPrint, btnExport, btnCount],
				// xsTan 删除开始 2009-1-23
				// 底部工具栏
//				bbar : new Ext.PagingToolbar({
//							pageSize : Constants.PAGE_SIZE,
//							store : storeMy,
//							displayInfo : true,
//							displayMsg : MessageConstants.DISPLAY_MSG,
//							emptyMsg : MessageConstants.EMPTY_MSG
//						}),
				// xsTan 删除结束 2009-1-23
				// 不允许移动列
				enableColumnMove : false,
				frame : false
			});
	// --gridpanel显示格式定义-----结束-------------------
    // xsTan 追加开始 2009-1-23
    // 禁止选中最后行
    grid.on('rowmousedown', function(grid, rowIndex, e) {
                if (rowIndex < storeMy.getCount() - 1) {
                    return true;
                }
                return false;
            });
     grid.on('celldblclick',cellDbClick);        
    // xsTan 追加结束 2009-1-23

	// 页面加载显示数据
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				items : [{
							xtype : "panel",
							region : 'center',
							layout : 'fit',
							border : false,
							items : [grid]
						}]
			});
	// ***************************子画面***********************************
	var recordMySub = new Ext.data.Record.create([{
				name : 'menuName'
			}, {
				name : 'menuTypeName'
			}, {
				name : 'menuAmount'
			}, {
				name : 'menuTotal'
			}]);
	// 订餐信息查询子画面grid中的store定义
	var subGridStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'administration/getMenuSystemSubInfo.action'
						}),
				reader : new Ext.data.JsonReader({
							root : 'list',
							totalProperty : 'totalCount'
						}, recordMySub)

			});
	subGridStore.on("load", function() {
				if (subGridStore.getTotalCount() <= 0) {
					Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
							MessageConstants.COM_I_003);
				} else {
					var record = new recordMySub({
								'menuName' : '合计:'
							});
					total = 0;
					for (i = 0; i < subGridStore.getCount(); i++) {
						total += subGridStore.getAt(i).get('menuTotal') * 1;
					}
					record.set('menuTotal', total);
					subGridStore.add(record);
				}
			});
	// 订餐信息查询columnModel
	var cmMySub = new Ext.grid.ColumnModel([
			// 自动生成行号
			new Ext.grid.RowNumberer({
						header : '行号',
						width : 35
					}), {
				header : '菜谱名称',
				align : 'left',
				width : 140,
				dataIndex : 'menuName'
			}, {
				header : '菜谱类别',
				align : 'left',
				width : 140,
				dataIndex : 'menuTypeName'
			}, {
				header : '份数',
				align : 'right',
				width : 70,
				dataIndex : 'menuAmount'
//                renderer : function(value, cellmeta, record, rowIndex,
//						columnIndex, store) {
//						return numberFormat(value);
//						}
			}, {
				header : '价格合计',
				align : 'right',
				width : 80,
				dataIndex : 'menuTotal',
                renderer : function(value) {
						return numberFormat(value);
                }
			}]);
	//子画面gridpanel
	var subGrid = new Ext.grid.GridPanel({
		store : subGridStore,
		height : 300,
		width : 500,
		style : "padding-top:10px;padding-right:10px;padding-bottom:0px;margin-bottom:0px",
		cm : cmMySub,
		frame : false,
		border : false,
		sm : new Ext.grid.RowSelectionModel({
					singleSelect : true
				}),
		enableColumnHide : true,
		enableColumnMove : false
	});
	//子画面panel
	var subPanel = new Ext.Panel({
		border : false,
		layout : "border",
		items : [{
			        region: 'center',
					columnWidth : 1.00,
					layout : "form",
					border : false,
					items : [subGrid]
				}]
	});
	//子窗口
	var subWindow = new Ext.Window({
				width : 500,
				height : 300,
				title : "汇总统计",
				layout : 'fit',
				closeAction : 'hide',
				modal : true,
				buttonAlign : "center",
				resizable : false,
				items : [subPanel]
			});
	// *********************************函数定义开始处******************************
	// 导出按钮处理函数
	function exportIt() {
        Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, MessageConstants.COM_C_007, function(
                buttonobj) {
            if (buttonobj == "yes") {
                document.all.blankFrame.src = "administration/exportMenuSystemFile.action";
            }
        })
	}
	// 统计函数定义
	function countRecord() {
		var strDate = startDate.getValue();
		var strMenuType = drpMenuType.getValue();
		var strManType = drpManType.getValue();
		subGridStore.baseParams = {
			strDate : strDate,
			strMenuType : strMenuType,
			strManType : strManType

		};
		subGridStore.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});
		subWindow.show();
	}
	// 查询函数
	function queryRecord() {
		// 获取开始时间值

		// 获取单选框值
		var strDate = startDate.getValue();
		var strMenuType = drpMenuType.getValue();
		var strManType = drpManType.getValue();
		storeMy.baseParams = {
			strDate : strDate,
			strMenuType : strMenuType,
			strManType : strManType

		};
		storeMy.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});
	}
    /**
     * 打印
     */
    function printHanler(){
        var menuType = drpMenuType.getValue();
        var manType = drpManType.getValue();
        var menuDate = startDate.getValue();
        window.open("/power/report/webfile/administration/ARF02.jsp?" + "menuType=" + menuType + "&manType=" + manType+ "&menuDate=" + menuDate);
    }
    
    /**
     * 大数字中间用','分隔 小数点后3位
     */
    function numberFormat(value){
        value = value*1;
        value = String(value);
        // 整数部分
        var whole = value;
        // 小数部分
        var sub = ".00";
        // 如果有小数
        if (value.indexOf(".") > 0) {
            whole = value.substring(0, value.indexOf("."));
            sub = value.substring(value.indexOf("."), value.length);
            sub = sub + "00";
            if(sub.length > 3){
                sub = sub.substring(0,3);
            }
        }
        var r = /(\d+)(\d{3})/;
        while (r.test(whole)){
           whole = whole.replace(r, '$1' + ',' + '$2');
        }
        v = whole + sub;
        return v + "元";
    }
    
    /**
     * 份数追加逗号
     */
    function numberWithSub(value) {
        value = value*1;
        value = String(value);
        // 整数部分
        var whole = value;
        var r = /(\d+)(\d{3})/;
        while (r.test(whole)){
           whole = whole.replace(r, '$1' + ',' + '$2');
        }
        return  whole;
    }
});
