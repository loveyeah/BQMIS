Ext.onReady(function() {
	
	// 预算费用合计
    var strSum1;
    // 实际费用合计
	var strSum2;
	Ext.override(Ext.grid.GridView, {
            // 重写doRender方法
            doRender : function(cs, rs, ds, startRow, colCount, stripe){
                var ts = this.templates, ct = ts.cell, rt = ts.row, last = colCount-1;
                var tstyle = 'width:'+this.getTotalWidth()+';';
                // buffers
                var buf = [], cb, c, p = {}, rp = {tstyle: tstyle}, r;
                for(var j = 0, len = rs.length; j < len; j++){
                    r = rs[j]; cb = [];
                    var rowIndex = (j+startRow);
                    for(var i = 0; i < colCount; i++){
                        c = cs[i];
                        p.id = c.id;
                        p.css = i == 0 ? 'x-grid3-cell-first ' : (i == last ? 'x-grid3-cell-last ' : '');
                        p.attr = p.cellAttr = "";
                        // 如果该行是统计行并且改列是第一列
                        if(r.data["countType"] == "total"&&i==0){
                            p.value = "合计";
                        }else{
                            p.value = c.renderer(r.data[c.name], p, r, rowIndex, i, ds);
                        }
                        p.style = c.style;
                        if(p.value == undefined || p.value === "") p.value = "&#160;";
                        if(r.dirty && typeof r.modified[c.name] !== 'undefined'){
                            p.css += ' x-grid3-dirty-cell';
                        }
                        cb[cb.length] = ct.apply(p);
                    }
                    var alt = [];
                    if(stripe && ((rowIndex+1) % 2 == 0)){
                        alt[0] = "x-grid3-row-alt";
                    }
                    if(r.dirty){
                        alt[1] = " x-grid3-dirty-row";
                    }
                    rp.cols = colCount;
                    if(this.getRowClass){
                        alt[2] = this.getRowClass(r, rowIndex, rp, ds);
                    }
                    rp.alt = alt.join(" ");
                    rp.cells = cb.join("");
                    buf[buf.length] =  rt.apply(rp);
                }
                return buf.join("");
            }
        });
	// 导出按钮
	var btnDerive = new Ext.Button({
				id : "derive",
				text : Constants.BTN_EXPORT,
				iconCls : Constants.CLS_EXPORT,
				disabled : true,
				handler : exportIt
			});
	var dteStart = new Date();
	var dteEnd = new Date();
	// 系统当天前15天的日期
	dteStart.setDate(dteStart.getDate() - 30);
	dteStart = dteStart.format('Y-m-d');
	// 系统当前日期
	dteEnd.setDate(dteEnd.getDate());
	dteEnd = dteEnd.format('Y-m-d');
	// 起始时间控件
	var txtStartDate = new Ext.form.TextField({
				id : 'startDate',
				name : 'startDate',
				width : 90,
				style : 'cursor:pointer',
				value : dteStart,
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
	// 截止时间控件
	var txtEndDate = new Ext.form.TextField({
				id : 'endDate',
				name : 'endDate',
				width : 90,
				style : 'cursor:pointer',
				value : dteEnd,
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
	// 经办人
	var drpManager = new Ext.form.CmbDriver({
                width : 80
	});
	drpManager.store.load();
	// 车牌号
	var drpCarNo = new Ext.form.ComboBox({
						readOnly : true,
						displayField : 'carNo',
						valueField : 'carNo',
						mode : 'local',
						allowBlank : true,
						forceSelection : true,
						triggerAction : 'all',
						store : new Ext.data.JsonStore({
									url : 'administration/carRepairCarNoGet.action',
									root : 'list',
									fields : [{
												name : 'carNo'
											}]
								}),
								width : 90
					});
	drpCarNo.store.load();
	// 上报状态
	var drpDcmStatus = new Ext.form.CmbReportStatus({
	              width : 80
	});
	var headTbar = new Ext.Toolbar({
		region : 'north',
		border:false,
		height:25,
		items:["维修日期:", "从", txtStartDate, "到", txtEndDate, "-",
						"经办人:",drpManager,  "-", "车牌号:",drpCarNo,  "-","上报状态:",drpDcmStatus]
	});
	// 顶部工具栏
	var tbar = new Ext.Toolbar({
				items : [{
							id : "query",
							text : Constants.BTN_QUERY,
							iconCls : Constants.CLS_QUERY,
							handler : queryIt
						},{
						    id : "queryList",
							text : "材料清单查询",
							iconCls : Constants.CLS_QUERY,
							handler : queryList
						}, btnDerive]
			});

    /**
	 * 获取单位对应的名字
	 */
	function getUnitName(value){
		for(var i =0;i<unitStore.getCount();i++){
			if(value===unitStore.getAt(i).data.strUnitID){
				return unitStore.getAt(i).data.strUnitName;
			}
		}
		return "";
	}
	/**
	 * 大数字中间用','分隔 小数点后2位
	 */
	function numberFormat(value){
		if(value == null) 
		return "0.00元";
		if(value === "")
		return "";
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
        return  v + "元";
	}
	function numberFormat2(value){
		if(value == null || value == "") 
		return "0.00";
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
        return  v;
	}
	function numberFormat3(value){
		if(value == null || value == "") 
		return "0.00元";
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
        return  v + "元";
	}
	function numberFormat4(value){
		if(value == null || value == "") 
		return "";
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
        return  v;
	}
	// 导出按钮处理函数
	function exportIt() {
		Ext.MessageBox.confirm(MessageConstants.SYS_CONFIRM_MSG,
				MessageConstants.COM_C_007, function(button, text) {
					if(button=="yes"){
						document.all.blankFrame.src = "administration/exportCarRepairInfo.action?strPrePrice="
				                                    + strSum1 + "&&" + "strRealPrice=" + strSum2 + "";
					}
				})
	}
	function showDcmStatus(value){
		if(value=="0"){
			return "未上报";
		}
		if(value=="1"){
			return "已上报";
		}
		if(value=="2"){
			return "已终结";
		}
		if(value=="3"){
			return "已退回";
		}
		return "";
	}
	// 查询按钮处理函数
	function queryIt() {
		if ((txtStartDate.getValue() != "") && (txtEndDate.getValue() != "")) {
			if (txtStartDate.getValue() > txtEndDate.getValue()) {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
								MessageConstants.COM_E_009, "开始日期", "结束日期"));
				return;
			}
		}
		store.baseParams.strStartDate = txtStartDate.getValue();
		store.baseParams.strEndDate = txtEndDate.getValue();
		store.baseParams.strManager = drpManager.getValue();
		store.baseParams.strCarNo = drpCarNo.getValue();
		store.baseParams.strDcmStatus = drpDcmStatus.getValue();
		store.load({
					callback : function(success) {
						// 如果load成功后初始状态，加入统计行
						if (success) {
							if (store.getTotalCount() > 0) {
								addLine();
								btnDerive.setDisabled(false);
							} else {
								Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
										MessageConstants.COM_I_003);
								btnDerive.setDisabled(true);
							}
						}
					}
				})
	}
    
    // 查询结束时插入统计行
    function addLine() {
        // 统计行
        var record = new recordCarRepairInfo({
            countType : "total"
        });
        // 原数据个数
        var count = store.getCount();
        store.insert(count, record);
        grid.getView().refresh();
    };
    // 明细查询按钮处理方法
	function queryList() {
		var selectedRows = grid.getSelectionModel().getSelections();
		if (selectedRows.length < 1) {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
			return;
		}
		var lastSelected = selectedRows[selectedRows.length - 1];
        if(lastSelected.get("haveLise")=="Y"){
        	storeList.baseParams.strWhId = lastSelected.get("hdnWhId");
        	storeList.baseParams.strProCode = lastSelected.get("proCode");
        	storeList.load({
        	     params : {
        	     	start : 0,
        	     	limit : Constants.PAGE_SIZE
        	     },
        	     callback : function(success){
        	     	win.show();
        	     }
        	});
        } else {
        	Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, MessageConstants.AV001_I_001)
        	return;
        }
	}

    // 小数格式化
    function renderNumber(v, argDecimal) {
        if (v) {
            if (typeof argDecimal != 'number') {
                argDecimal = 2;
            }
            v = Number(v).toFixed(argDecimal);
            var t = '';
            v = String(v);
            while ((t = v.replace(/^(-?[0-9]+)([0-9]{3}.*)$/, '$1,$2')) !== v)
                v = t;
            
            return v;
        } else
            return "0.00";
    }
    // grid选择模式设为单行选择模式
    var sm = new Ext.grid.RowSelectionModel({
	       singleSelect : true
	});
	// grid中的列
	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
						header : "行号",
						width : 35
					}), {
				header : "维修申请单号",
				dataIndex : "whId",
				align : "left",
				width : 110
			}, {
				header : "上报状态",
				dataIndex : "dcmStatus",
				align : "left",
				renderer : showDcmStatus
			}, {
				header : "车牌号",
				dataIndex : "carNo",
				align : "left",
				width : 70
			}, {
				header : "车名",
				dataIndex : "carName",
				align : "left",
				width : 150
			}, {
				header : "修理日期",
				dataIndex : "repairDate",
				align : "left"
			}, {
				header : "维修项目",
				dataIndex : "proName",
				align : "left",
				width : 70
			}, {
				header : "预算费用",
				dataIndex : "price",
				align : "right",
				renderer:function(value, cellmeta, record, rowIndex,
                            columnIndex, store){    
                            // 如果不是最后一行
                            if (rowIndex < store.getCount() - 1) {
                                //var total=record.data.repastBz*record.data.repastNum;
                                return numberFormat(value);
                            // 如果是最后一行    
                         } else {
                             var totalSum = 0;
                             var total="";
                             // 对该列除最后一个单元格以为求和
                             for (var i = 0; i < store.getCount() - 1; i++) {
                                 total=store.getAt(i).get('price')*1;
                                 totalSum += total;
                             }
                             strSum1 = renderNumber(totalSum);
                             return numberFormat(totalSum);
                         }
                        }
			}, {
				header : "实际费用",
				dataIndex : "realPrice",
				align : "right",
				renderer:function(value, cellmeta, record, rowIndex,
                            columnIndex, store){    
                            // 如果不是最后一行
                            if (rowIndex < store.getCount() - 1) {
                                //var total=record.data.repastBz*record.data.repastNum;
                                return numberFormat(value);
                            // 如果是最后一行    
                         } else {
                             var totalSum = 0;
                             var total="";
                             // 对该列除最后一个单元格以为求和
                             for (var i = 0; i < store.getCount() - 1; i++) {
                                 total=store.getAt(i).get('realPrice')*1;
                                 totalSum += total;
						}
						strSum2 = renderNumber(totalSum);
						return numberFormat(totalSum);
					}
				}
			}, {
				header : "实际费用合计",
				dataIndex : "realSum",
				align : "right",
				renderer : function(value, cellmeta, record, rowIndex,
						columnIndex, store) {
					// 如果不是最后一行
					if (rowIndex < store.getCount() - 1) {
						return numberFormat(value);
						// 如果是最后一行
					} else {
						return "";
					}
				}
			}, {
				header : "维修单位",
				dataIndex : "cpName",
				align : "left",
				width : 200
			}, {
				header : "维修里程(公里)",
				dataIndex : "driveMile",
				align : "right",
				renderer : function(value, cellmeta, record, rowIndex,
						columnIndex, store) {
					// 如果不是最后一行
					if (rowIndex < store.getCount() - 1) {
						return numberFormat4(value);
						// 如果是最后一行
					} else {
						return "";
					}
				},
				width : 150
			}, {
				header : "经办人",
				dataIndex : "chsName",
				align : "left",
				width : 70
			}, {
				header : "支出事由",
				dataIndex : "reason",
				align : "left",
				width : 70
			}, {
				header : "备注",
				dataIndex : "memo",
				align : "left",
				width : 150
			}]);
	cm.defaultSortable = false;
	// grid中的数据
	var recordCarRepairInfo = Ext.data.Record.create([{
				name : "hdnWhId"
			}, {
				name : "whId"
			}, {
				name : "dcmStatus"
			}, {
				name : "carNo"
			}, {
				name : "carName"
			}, {
				name : "repairDate"
			}, {
				name : "proName"
			}, {
				name : "haveLise"
			}, {
				name : "proCode"
			}, {
				name : "price"
			}, {
				name : "realPrice"
			}, {
				name : "realSum"
			}, {
				name : "cpName"
			}, {
				name : "driveMile"
			}, {
				name : "chsName"
			}, {
				name : "reason"
			}, {
				name : "memo"
			}]);
	// 明细查询grid中的列
	var cmList = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
						header : "行号",
						width : 35
					}), {
				header : "维护单据号",
				dataIndex : "whId",
				align : "left",
				width : 110
			}, {
				header : "项目名称",
				dataIndex : "proName",
				align : "left"
			}, {
				header : "零件名称",
				dataIndex : "partName",
				align : "left",
				width : 70
			}, {
				header : "单位",
				dataIndex : "unit",
				align : "left",
				renderer : getUnitName,
				width : 70
			}, {
				header : "预算数量",
				dataIndex : "preNum",
				align : "right",
				renderer : numberFormat2,
				width : 70
			}, {
				header : "预算单价",
				dataIndex : "preUnitPrice",
				align : "right",
				renderer : numberFormat,
				width : 70
			}, {
				header : "预算金额",
				dataIndex : "prePrice",
				align : "right",
				renderer : function(value, cellmeta, record, rowIndex,
						columnIndex, store) {
					var total = store.getAt(rowIndex).get('preNum')
							* store.getAt(rowIndex).get('preUnitPrice');
					return numberFormat3(total);
				},
				width : 70
			}, {
				header : "实际数量",
				dataIndex : "realNum",
				align : "right",
				renderer : numberFormat2,
				width : 70
			}, {
				header : "实际单价",
				dataIndex : "realUnitPrice",
				align : "right",
				renderer : numberFormat,
				width : 70
			}, {
				header : "实际金额",
				dataIndex : "realPrice",
				align : "right",
				renderer : function(value, cellmeta, record, rowIndex,
						columnIndex, store) {
					var total = store.getAt(rowIndex).get('realNum')
							* store.getAt(rowIndex).get('realUnitPrice');
					return numberFormat3(total);
				},
				width : 70
			}, {
				header : "备注",
				dataIndex : "note",
				align : "left",
				width : 150
			}])
	var recordCarRepairList = Ext.data.Record.create([{
				name : "whId"
			}, {
				name : "proName"
			}, {
				name : "partName"
			}, {
				name : "unit"
			}, {
				name : "preNum"
			}, {
				name : "preUnitPrice"
			}, {
				name : "prePrice"
			}, {
				name : "realNum"
			}, {
				name : "realUnitPrice"
			}, {
				name : "realPrice"
			}, {
				name : "note"
			}]);
	// grid中的store
	var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "administration/carRepairQuery.action"
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, recordCarRepairInfo)
			});
	var storeList = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "administration/carRepairListQuery.action"
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, recordCarRepairList)
			});
	var unitStore = new Ext.data.JsonStore({
				lastQuery : '',
				root : 'list',
				url : "administration/getUnit.action",
				fields : ['strUnitName', 'strUnitID']
			})
	unitStore.load();
	storeList.baseParams = ({
	    strWhId : "",
        strProCode : ""
	});
	store.baseParams = ({
		strStartDate : txtStartDate.getValue(),
		strEndDate : txtEndDate.getValue(),
		strManager : drpManager.getValue(),
		strCarNo : drpCarNo.getValue(),
		strDcmStatus : drpDcmStatus.getValue()
	});
	// 注册store的load事件
	store.on("load", function() {
				if (this.getTotalCount() > 0) {
					btnDerive.setDisabled(false);
				} else {
					Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
							MessageConstants.COM_I_003);
					btnDerive.setDisabled(true);
				}
			});
	// 底部工具栏
	var bbar = new Ext.PagingToolbar({
				pageSize : Constants.PAGE_SIZE,
				store : storeList,
				displayInfo : true,
				displayMsg : MessageConstants.DISPLAY_MSG,
				emptyMsg : MessageConstants.EMPTY_MSG
			})
	// 明细查询grid
	var gridList = new Ext.grid.GridPanel({
		        region : "center",
				layout : "fit",
				autoScroll : true,
				bbar : bbar,
				colModel : cmList,
				enableColumnMove : false,
				store : storeList,
				autoSizeColumns : true,
				autoSizeHeaders : false
			});
	// 显示单元格内容
	var txtArea = new Ext.form.TextArea({
	     readOnly : true
	});
	// 显示窗口
	var win2 = new Ext.Window({
	     title : "详细信息查看窗口",
	     buttonAlign : "center",
	     modal : true,
	     height : 170,
	     width : 350,
	     layout : "fit",
	     closeAction : "hide",
	     resizable : false,
	     items : [txtArea],
	     buttons : [{
					text : Constants.BTN_CLOSE,
					iconCls : Constants.CLS_CANCEL,
					handler : closeListWin
				   }]
	});
	function closeListWin(){
		win2.hide();
	}
	// 注册gridList的单元格双击事件
	gridList.on("celldblclick",cellDbClick);
	// 单元格双击处理函数
	function cellDbClick(grid, rowIndex, columnIndex, e){
		if(columnIndex==11){
			txtArea.setValue(storeList.getAt(rowIndex).get("note"));
			win2.show();
		}
	}
	// 关闭按钮
	var btnClose = ({
				id : "close",
				text : Constants.BTN_CLOSE,
				iconCls : Constants.CLS_CANCEL,
				handler : closeWin
			});
	function closeWin(){
		win.hide();
	}
	// 材料结算清单窗口
	var win = new Ext.Window({
		        title : "材料结算清单",
		        buttonAlign : "center",
				modal : true,
				layout : "border",
				width : 500,
				height : 320,
				closeAction : "hide",
				resizable : false,
				items : [gridList],
				buttons : [btnClose]
			});
	// grid主体
	var grid = new Ext.grid.GridPanel({
				autoScroll : true,
				region : "center",
				layout : "fit",
				colModel : cm,
				sm : sm,
				tbar : tbar,
				enableColumnMove : false,
				store : store,
				autoSizeColumns : true,
				autoSizeHeaders : false
			});
    // 禁止选中最后行
    grid.on('rowmousedown', function(grid, rowIndex, e) {
        if (rowIndex<store.getCount()-1) {
            return true;
        }
        return false;
    });
	grid.on("rowdblclick", gridDb);

	function gridDb(grid, rowIndex, e) {
		if (rowIndex<store.getCount()-1) {
            Ext.get("queryList").dom.click();
        }
		return false;
	}
	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				autoScroll : true,
				enableTabScroll : true,
				items : [grid,headTbar]
			});
})