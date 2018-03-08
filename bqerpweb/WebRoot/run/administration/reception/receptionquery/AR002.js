// 接待审批查询
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
	// 接待开始时间
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
    // 接待结束时间
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
	// 部门选择
	var txtDept = new Ext.form.TextField({
		fieldLabel : '部门选择',
		width : 100,
		valueField : 'id',
		hiddenName : 'dept',
		maxLength : 100,
		anchor : '100%',
		readOnly : true,
		listeners : {
		focus : function() {
			var args = {selectModel:'single',rootNode:{id:'0',text:'灞桥热电厂'},onlyLeaf:false};
			this.blur();
	         var dept = window.showModalDialog('../../../../comm/jsp/hr/dept/dept.jsp',
	                args, 'dialogWidth:500px;dialogHeight:320px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
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

	// 人员选择
	var drpWorker = new Ext.form.CmbWorkerByDept({
		width : 70
	})
	// 是否超支
	var drpIsOver = new Ext.form.ComboBox({
		id : 'isOver',
		width : 50,
		name : 'isOver',
		allowBlank : true,
	        forceSelection : true,
	        triggerAction : 'all',
	        mode : 'local',
	        readOnly : true,
	        displayField : 'text',
	        valueField : 'value',
	        store : new Ext.data.JsonStore({
	            fields : ['value', 'text'],
	            data : [{value : '',text : ''
	                },{
	                    value : 'Y',text : '是'
	                },{
	                    value : 'N',text : '否'
	                }]
	         })
	});
	// 单据状态
	var drpStatus = new Ext.form.CmbReportStatus({
		id : 'dcmStatus',
		labelAlign : 'right',
		width : 75,
		emptyText : '',
		name : 'dcmStatus'
	});

	// 数据源--------------------------------
	var MyRecord = Ext.data.Record.create([{
				name : 'applyId'
			}, {
				name : 'applyDeptName'
			}, {
				name : 'applyManName'
			}, {
				name : 'logDate'
			}, {
				name : 'meetDate'
			}, {
				name : 'payoutBz'
			}, {
				name : 'payout'
			}, {
				name : 'balance'
			}, {
				name : 'dcmStatus'
		    }]);

	var queryStore = new Ext.data.JsonStore({
		url : 'administration/getReceptionInfoList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : [{
				name : 'applyId'
			}, {
				name : 'applyDeptName'
			}, {
				name : 'applyManName'
			}, {
				name : 'logDate'
			}, {
				name : 'meetDate'
			}, {
				name : 'payoutBz'
			}, {
				name : 'payout'
			}, {
				name : 'balance'
			}, {
				name : 'dcmStatus'
		    }],
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
	var btnQuery = new Ext.Button({
		text : Constants.BTN_QUERY,
		iconCls : Constants.CLS_QUERY,
		handler : queryRecord
	})

	var btnExport = new Ext.Button({
		text : Constants.BTN_EXPORT,
		iconCls : Constants.CLS_EXPORT,
		disabled : true,
		handler : exportRecord
	})
    var btnPrint = new Ext.Button({
		text : Constants.BTN_PRINT_APPLY,
		iconCls : Constants.CLS_PRINT,
		disabled : false,
		handler : printRecord
	})
	var headTbar = new Ext.Toolbar({
		region : 'north',
		border:false,
		height:25,
		items:["接待日期:", "从", txtStartDate, "到", txtEndDate, "-" , "申请部门:",txtDept, "-" , "申请人:",drpWorker, "-" ,"是否超支:", drpIsOver, "-" ,"上报状态:", drpStatus]
	});
	var gridTbar = new Ext.Toolbar({
		border:false,
		height:25,
		items:[ btnQuery, btnPrint, btnExport]
	});

	queryStore.on("load", function() {
		if (queryStore.getCount() > 0) {
			btnExport.setDisabled(false);
//			btnPrint.setDisabled(false);
			addLine();
		}else {
		    btnExport.setDisabled(true);
//		    btnPrint.setDisabled(true);
		    // 没有检索到任何信息
		    Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_003);
		}
	});

	// 定义选择列
	var sm = new Ext.grid.RowSelectionModel({
		singleSelect : true
	});
	var grid = new Ext.grid.GridPanel({
		region : "center",
		store : queryStore,
		columns : [
		// 自动行号
		new Ext.grid.RowNumberer({
			header : "行号",
			width : 31
		}), {
			header : "审批单号",
			sortable : true,
			dataIndex : 'applyId'
		}, {
			header : "申请部门",
			sortable : true,
			dataIndex : 'applyDeptName',
			width : 220
		}, {
			header : "申请人",
			sortable : true,
			dataIndex : 'applyManName'
		}, {
			header : "填表日期",
			sortable : true,
			dataIndex : 'logDate'
		}, {
			header : "接待日期",
			sortable : true,
			dataIndex : 'meetDate'
		}, {
			header : "标准支出",
			sortable : true,
			align : 'right',
			dataIndex : 'payoutBz',
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
                        total=store.getAt(i).get('payoutBz')*1;
                        totalSum += total;
                    }
                    return numberFormat(totalSum);
                	}
                }
		}, {
			header : "实际支出",
			sortable : true,
			align : 'right',
			dataIndex : 'payout',
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
                        total=store.getAt(i).get('payout')*1;
                        totalSum += total;
                    }
                    return numberFormat(totalSum);
                	}
                }
		}, {
			header : "差额",
			sortable : true,
			align : 'right',
			dataIndex : 'balance',
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
                        total=store.getAt(i).get('balance')*1;
                        totalSum += total;
                    }
                    return numberFormat(totalSum);
                	}
                }
		}, {
			header : "上报状态",
			sortable : true,
			renderer : dcmStatusFormat,
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
	// 注册双击事件
 	grid.on("rowdblclick", function(grid, rowIndex, e) {
				if (rowIndex < queryStore.getCount() - 1) {
					printRecord();
					return true;
				}
				return false;
			});
 	// 禁止选中最后行
	grid.on('rowmousedown', function(grid, rowIndex, e) {
				if (rowIndex < queryStore.getCount() - 1) {
					return true;
				}
				return false;
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
					isOver : drpIsOver.getValue(),
					dcmStatus : drpStatus.getValue()
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
			    document.all.blankFrame.src = "administration/exportReceptionInfoFile.action";
			}
		})
	}
	/**
	 *  打印处理
	 */
    function printRecord() {
        // 是否有被选项
		if (grid.selModel.hasSelection()) {
        	var record = grid.getSelectionModel().getSelected();	
		    var applyId = record.get('applyId');
		    if(applyId != null){
		        	window.open("/power/report/webfile/administration/ARF05.jsp?applyNo="+applyId); 
		    }
		}else {
		    Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
		}
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
    	}else
    	    value = ""
    	return value;
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
})