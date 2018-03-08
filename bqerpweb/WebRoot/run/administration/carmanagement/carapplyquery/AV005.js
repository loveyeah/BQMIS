// 用车申请查询
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	// 开始结束日期声明
	var startDate = "";
	var endDate = "";
	// 开始结束日期初始化
	var startDate = new Date();
	var endDate = new Date();
	// 系统当天前15天的日期
	startDate.setDate(startDate.getDate() - 15);
	startDate = startDate.format('Y-m-d');
	// 系统当前日期
	endDate.setDate(endDate.getDate());
	endDate = endDate.format('Y-m-d');
	// 用车日期开始时间
	var txtStartDate = new Ext.form.TextField({
		id : 'startDate',
		name : 'startDate',
		style : 'cursor:pointer',
		value : startDate,
		width : 80,
		readOnly : true,
		listeners : {
			focus : function() {
	            WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : false,
					isShowClear : false
				});
			}
		}
	});
    // 用车日期结束时间
	var txtEndDate = new Ext.form.TextField({
		id : 'endDate',
		name : 'endDate',
		style : 'cursor:pointer',
		value : endDate,
		width : 80,
		readOnly : true,
		listeners : {
		    focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : false,
					isShowClear : false
				});
			}
		}
	});
	// 用车部门
	var txtDept = new Ext.form.TextField({
		fieldLabel : '用车部门',
		width : 100,
		id : "dept",
		displayField : 'text',
		valueField : 'id',
		hiddenName : 'dept',
		maxLength : 100,
		readOnly : true,		
		listeners : {
		focus : function() {
			var args = {selectModel:'single',rootNode:{id:'0',text:'合肥电厂'},onlyLeaf:false};
			this.blur();
	         var dept = window.showModalDialog('../../../../comm/jsp/hr/dept/dept.jsp',
	                args, 'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
	        if (typeof(dept) != "undefined") {
	            txtDept.setValue(dept.names);
	            hiddenMrDept.setValue(dept.codes);
	            drpWorker.setValue("");
	            drpWorker.store.load({
	    			params:{
	    				strDeptCode:dept.codes
	    			}
	            })
 			}
			}
		}
	});
	// 隐藏域部门ID
	var hiddenMrDept =new Ext.form.Hidden({
		hiddenName : 'dept'
	})		

	// 申请人
	var drpWorker = new Ext.form.CmbWorkerByDept({
		width : 75
	})
	// 司机
	var drpDriver = new Ext.form.CmbDriver({
		id : 'driver',
		width : 75,
		name : 'driver'
	});
	drpDriver.store.load();
	// 上报状态
	var drpStatus = new Ext.form.CmbReportStatus({
		id : 'dcmStatus',
		labelAlign : 'right',
		width : 65,
		emptyText : '',
		name : 'dcmStatus'
	});

	// 数据源--------------------------------
	var MyRecord = Ext.data.Record.create([
			// 上报状态
			{
				name : 'dcmStatus'
			// 申请人
			}, {
				name : 'applyMan'
			// 用车部门
			}, {
				name : 'dep'
			// 用车时间 
			}, {
				name : 'useDate'
			// 用车人数
			}, {
				name : 'useNum'
			// 是否出省
			}, {
				name : 'ifOut'
			// 路桥费
			}, {
				name : 'lqPay'
			// 油费
			}, {
				name : 'useOil'
			// 行车里程
		    }, {
				name : 'distance'
			// 用车事由
			}, {
				name : 'reason'
			// 发车时间
		    }, {
				name : 'startTime'
			// 收车时间
			}, {
				name : 'endTime'
			// 到达地点
		    }, {
				name : 'aim'
			// 车牌号
			}, {
				name : 'carNo'
			// 司机
		    }, {
				name : 'driver'
			}]);

	var queryStore = new Ext.data.JsonStore({
		url : 'administration/getApplyCarInfo.action',
		root : 'list',
		fields : MyRecord,
		listeners : {
			loadexception : function(ds, records, o) {
				var o = eval("(" + o.responseText + ")");
				var succ = o.msg;
				if (succ == Constants.SQL_FAILURE) {
					Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
							MessageConstants.COM_E_014);
				} else if (succ == Constants.IO_FAILURE) {
					Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
							MessageConstants.COM_E_022);
				} else if (succ == Constants.DATE_FAILURE){
					Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
							MessageConstants.COM_E_023);
				}
			}

		}
	});
    
	// --gridpanel显示格式定义-----开始-------------------
	// 查询按钮
	var btnQuery = new Ext.Button({
		text : Constants.BTN_QUERY,
		iconCls : Constants.CLS_QUERY,
		handler : queryRecord
	});

	// 导出按钮
	var btnExport = new Ext.Button({
		text : Constants.BTN_EXPORT,
		iconCls : Constants.CLS_EXPORT,
		disabled : true,
		handler : exportRecord
	});

	// 查询条件
	var headTbar = new Ext.Toolbar({
		region : 'north',
		border:false,
		height:25,
		items:["用车日期:", "从", txtStartDate, "到", txtEndDate,'-', "用车部门:",
		txtDept, '-',"申请人:",drpWorker,'-',"司机:", drpDriver, '-',"上报状态:", drpStatus]
	});

	// button
	var gridTbar = new Ext.Toolbar({
		border:false,
		height:25,
		items:[ btnQuery, btnExport]
	});

	queryStore.on("load", function() {
		if (queryStore.getCount() > 0) {
			btnExport.setDisabled(false);
			addLine();
		}else {
		    btnExport.setDisabled(true);
		    // 没有检索到任何信息
		    Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_003);
		}
	});

	// 定义选择列
	var sm = new Ext.grid.RowSelectionModel({
		singleSelect : true
	});

	// 用车申请一览
	var grid = new Ext.grid.GridPanel({
		region : "center",
		store : queryStore,
		columns : [
		// 自动行号
		new Ext.grid.RowNumberer({
			header : "行号",
			width : 31
		}), {
			header : "申请人",
			sortable : true,
			dataIndex : 'applyMan'
		}, {
			header : "用车部门",
			sortable : true,
			dataIndex : 'dep'
		}, {
			header : "用车日期",
			sortable : true,
			dataIndex : 'useDate'
		}, {
			header : "用车人数",
			sortable : true,
			align : 'right',
			renderer : function(value, cellmeta, record, rowIndex,
                    columnIndex, store){                            
                // 如果不是最后一行
                if (rowIndex < store.getCount() - 1) {
                    return numberFormatMan(value); 
                }
                },
			dataIndex : 'useNum'
		},  {
			header : "是否出省",
			sortable : true,
			dataIndex : 'ifOut'
		}, {
			header : "路桥费",
			sortable : true,
			align : 'right',
			dataIndex : 'lqPay',
			renderer:function(value, cellmeta, record, rowIndex,
                    columnIndex, store){                            
                // 如果不是最后一行
                if (rowIndex < store.getCount() - 1) {
                    return numberFormat(value);                     
                // 如果是最后一行
                } else {
                    totalSum = 0;
                    var total="";
                    // 对该列除最后一个单元格以为求和
                    for (var i = 0; i < store.getCount() - 1; i++) {
                        total=store.getAt(i).get('lqPay')*1;
                        totalSum += total;
                    }
                    return numberFormat(totalSum);
                	}
                }
		}, {
			header : "油费",
			sortable : true,
			align : 'right',
			dataIndex : 'useOil',
			renderer:function(value, cellmeta, record, rowIndex,
                    columnIndex, store){                            
                // 如果不是最后一行
                if (rowIndex < store.getCount() - 1) {
                    return numberFormat(value);                     
                // 如果是最后一行
                } else {
                    totalSum = 0;
                    var total="";
                    // 对该列除最后一个单元格以为求和
                    for (var i = 0; i < store.getCount() - 1; i++) {
                        total=store.getAt(i).get('useOil')*1;
                        totalSum += total;
                    }
                    return numberFormat(totalSum);
                	}
                }
		}, {
			header : "行车里程",
			sortable : true,
			align : 'right',
			dataIndex : 'distance',
			renderer:function(value, cellmeta, record, rowIndex,
                    columnIndex, store){                            
                // 如果不是最后一行
                if (rowIndex < store.getCount() - 1) {
                    return numberFormatMile(value);                     
                // 如果是最后一行
                } else {
                    totalSum = 0;
                    var total="";
                    // 对该列除最后一个单元格以为求和
                    for (var i = 0; i < store.getCount() - 1; i++) {
                        total=store.getAt(i).get('distance')*1;
                        totalSum += total;
                    }
                    return numberFormatMile(totalSum);
                	}
                }
		}, {
			header : "用车事由",
			sortable : true,
			dataIndex : 'reason'
		}, {
			header : "发车时间",
			sortable : true,
			dataIndex : 'startTime'
		}, {
			header : "收车时间",
			sortable : true,
			dataIndex : 'endTime'
		}, {
			header : "到达地点",
			sortable : true,
			dataIndex : 'aim'
		}, {
			header : "车牌号",
			sortable : true,
			dataIndex : 'carNo'
		}, {
			header : "司机",
			sortable : true,
			dataIndex : 'driver'
		}, {
			header : "上报状态",
			sortable : true,
			dataIndex : 'dcmStatus'
		}],
		sm : sm,
		autoScroll : true,
		enableColumnMove : false,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : false
		},
		// 头部工具栏
		tbar : gridTbar
	});
	// --gridpanel显示格式定义-----结束--------------------
    new Ext.Viewport({
		enableTabScroll : true,
		layout : 'border',
		items : [headTbar,grid]
	});
	
	// 函数处理
	/**
	 * 添加共计行
	 */
	function addLine() {
        // 原数据个数
        var count = queryStore.getCount();
        // 统计行
        var record = new MyRecord({
                    countType : "total"
                });
        // 插入统计行
        queryStore.insert(count, record);
        grid.getView().refresh();
    }
	/**
	 * 时间的有效性检查
	 */
	function checkDate() {
		var strStartDate = Ext.get("startDate").dom.value;
		var strEndDate = Ext.get("endDate").dom.value;
		if(strStartDate != "" && strEndDate != "") {
			var dateStart = Date.parseDate(strStartDate, 'Y-m-d');
			var dateEnd = Date.parseDate(strEndDate, 'Y-m-d');
			if (dateStart.getTime() > dateEnd.getTime()) {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(MessageConstants.COM_E_009,"开始日期","结束日期"));
				return false;
			} else {
				return true;
			}
		}
		return true;
	}
    /**
     * 查询处理
     */
	function queryRecord() {
		if (checkDate()) {
			queryStore.load({
				params : {
					startDate : txtStartDate.getValue(),
					endDate : txtEndDate.getValue(),
					dept : hiddenMrDept.getValue(),
					worker : drpWorker.getValue(),
					driver : drpDriver.getValue(),
					dcmStatus : drpStatus.getValue(),
					start: 0, 
             		limit : Constants.PAGE_SIZE
				}
			});
		}
	}

	/**
	 * 导出处理
	 */
	function exportRecord() {
		Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, MessageConstants.COM_C_007, function(
			    buttonobj) {
			if (buttonobj == "yes") {
			    document.all.blankFrame.src = "administration/exportApplyCarInfoFile.action";
			}
		})
	}

    /**
     * 单据状态初始化
     */
    function dcmStatusFormat(value){
    	if(value == CodeConstants.FROM_STATUS_0){
    		value = "未上报";
    	}else if(value == CodeConstants.FROM_STATUS_1){
    		value = "已上报";
    	}else if(value == CodeConstants.FROM_STATUS_2){
    		value = "已终结";
    	}else if(value == CodeConstants.FROM_STATUS_3){
    		value = "已退回";
    	}
    	return value;
    }
    
    /**
	 * 大数字中间用','分隔 小数点后3位
	 */
	function numberFormat(strValue){
	    strValue = String(strValue);
	    if (strValue == null || strValue == "" || strValue == "null") {
	           strValue = "0";
	    }
        // 整数部分
        var strWhole = strValue;
        // 小数部分
        var strSub = ".00";
        // 如果有小数
		if (strValue.indexOf(".") > 0) {
		    strWhole = strValue.substring(0, strValue.indexOf("."));
			strSub = strValue.substring(strValue.indexOf("."), strValue.length);
			strSub = strSub + "00";
			if(strSub.length > 3){
			    strSub = strSub.substring(0,3);
		    }
		}
        var r = /(\d+)(\d{3})/;
        while (r.test(strWhole)){
           strWhole = strWhole.replace(r, '$1' + ',' + '$2');
        }
        v = strWhole + strSub;
        return  v + "元";
	}

	/**
	 * 大数字中间用','分隔 小数点后3位
	 */
	function numberFormatMile(strValue){
	    strValue = String(strValue);
	    if (strValue == null || strValue == "" || strValue == "null") {
	           strValue = "0";
	    }
        // 整数部分
        var strWhole = strValue;
        // 小数部分
        var strSub = ".00";
        // 如果有小数
		if (strValue.indexOf(".") > 0) {
		    strWhole = strValue.substring(0, strValue.indexOf("."));
			strSub = strValue.substring(strValue.indexOf("."), strValue.length);
			strSub = strSub + "00";
			if(strSub.length > 3){
			    strSub = strSub.substring(0,3);
		    }
		}
        var r = /(\d+)(\d{3})/;
        while (r.test(strWhole)){
           strWhole = strWhole.replace(r, '$1' + ',' + '$2');
        }
        v = strWhole + strSub;
        return  v + "公里";
	}

		/**
	 * 大数字中间用','分隔 小数点后3位
	 */
	function numberFormatMan(strValue){
	    strValue = String(strValue);
	    if (strValue == null || strValue == "" || strValue == "null") {
	           strValue = "0";
	    }
        // 整数部分
        var strWhole = strValue;
//        // 小数部分
//        var strSub = ".00";
//        // 如果有小数
//		if (strValue.indexOf(".") > 0) {
//		    strWhole = strValue.substring(0, strValue.indexOf("."));
//			strSub = strValue.substring(strValue.indexOf("."), strValue.length);
//			strSub = strSub + "00";
//			if(strSub.length > 3){
//			    strSub = strSub.substring(0,3);
//		    }
//		}
        var r = /(\d+)(\d{3})/;
        while (r.test(strWhole)){
           strWhole = strWhole.replace(r, '$1' + ',' + '$2');
        }
//        v = strWhole + strSub;
        return  strWhole + "人";
	}

	/**
	 * 重写doRender方法
	 */
	Ext.override(Ext.grid.GridView, {                
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
                        p.value = "共计";
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
})