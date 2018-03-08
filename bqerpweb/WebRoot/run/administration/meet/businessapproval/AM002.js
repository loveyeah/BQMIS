Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
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
    // 系统当天日期
    var sd = new Date();
    var ed = new Date();
    // 系统当天前15天的日期
    sd.setDate(sd.getDate() - 15);
    sd = sd.format('Y-m-d');
    // 系统当天后15天的日期
    ed.setDate(ed.getDate()); 
    ed = ed.format('Y-m-d');
    // 定义查询起始时间
	var startDate = new Ext.form.TextField({
		width:75,
        name : 'startDate',
        style : 'cursor:pointer',
        value : sd,
        readOnly:true,
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
 	//定义查询结束时间
    var endDate = new Ext.form.TextField({
    	width:75,
        id : 'endDate',
        name : 'endDate',
        style : 'cursor:pointer',
        value : ed,
        readOnly:true,
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
	var txtMrDept = new Ext.form.TextField({
				fieldLabel : '部门选择',
				width : 90,
				valueField : 'id',
				hiddenName : 'mrDept',
				maxLength : 100,
				anchor : '100%',
				readOnly : true,
				listeners : {
				focus : function() {
					var args = {selectModel:'single',rootNode:{id:'0',text:'灞桥热电厂'},onlyLeaf:false};
					this.blur();
			         var dept = window.showModalDialog('../../../../comm/jsp/hr/dept/dept.jsp',
			                args, 'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			        if (typeof(dept) != "undefined") {
			            txtMrDept.setValue(dept.names);
			            hiddenMrDept.setValue(dept.codes);
			            member.setValue("");
			            member.store.load({
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
        name : 'depCode'
    })		
	    
    // 人员选择
    var member = new Ext.form.CmbWorkerByDept({
    	width : 70,
    	id:"member",
    	labelAlign : 'right',
    	name:"member"
    });
    
	// 是否超支
	var cmbIsOver = new Ext.form.CmbIsFlg({
		id : 'isOver',
		labelAlign : 'right',
		width : 50,
		emptyText : '',
		name : 'overSpend'
	});
	// 单据状态
	var cmbStatus = new Ext.form.CmbReportStatus({
		id : 'dcmStatus',
		labelAlign : 'right',
		width : 70,
		emptyText : '',
		name : 'dcmStatus'
	});
    // 查询按钮
    var btnQuery = new Ext.Button({
        text : Constants.BTN_QUERY,
        iconCls : Constants.CLS_QUERY,
        handler : queryMaterial
    });	
    // 打印按钮
    var btnPrint = new Ext.Button({
        text : Constants.BTN_PRINT_APPLY,
        iconCls : Constants.CLS_PRINT,
        handler : printMaterialType
    });	
	var btnExport = new Ext.Button({
		text : Constants.BTN_EXPORT,
		disabled:true,
		iconCls:Constants.CLS_EXPORT,
		handler : exportOut
	});
	
	// 数据源--------------------------------
	var MyRecord = Ext.data.Record.create([{
				/** 会议审批单号 */
				name : 'meetId'
			}, {
				/**会议申请单号隐藏*/
				name:'meetHidId'
			},{
				/** 姓名 */	
				name : 'name'
			}, {
				/** 部门名称 */
				name : 'depName'
			}, {
				/** 会议名称 */	
				name : 'meetName'
			}, {
				/** 会议开始时间 */
				name : 'startMeetDate'
			}, {
				/** 会议结束时间 */	
				name : 'endMeetDate'
			}, {
				/** 费用名称 */
				name : 'payName'
			}, {
				/** 费用预算 */
				name : 'payBudget'
			}, {
				/** 实际费用 */	
				name : 'payReal'
			}, {
				/** 差额 */
				name:'balance'
			},{
				/** 预计费用汇总 */
				name : 'budpayInall'
			}, {
				/** 实际费用汇总 */
				name : 'realpayInall'
			}, {
				/** 会议其他要求 */
				name : 'meetOther'
			}, {
				/** 单据状态 **/
				name : 'dcmStatus'
		}]);
	// 定义获取数据源
	var dataProxy = new Ext.data.HttpProxy({
		url : 'administration/getMeetApproveList.action'
	});

	// 定义格式化数据
	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);
	// 定义封装缓存数据的对象
	var queryStore = new Ext.data.Store({
		// 访问的对象
		proxy : dataProxy,
		// 处理数据的对象
		reader : theReader,
		listeners : {
				loadexception : function(ds, records, o) {
				var o = eval("(" + o.responseText + ")");
				var succ = o.msg;
				if (succ == Constants.SQL_FAILURE) {
					Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
							MessageConstants.COM_E_014);
				} else if (succ == Constants.DATE_FAILURE){
					Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
							MessageConstants.COM_E_023);
				}
			}
		}
		
	});
	
	// 定义选择列
	var sm = new Ext.grid.RowSelectionModel({
		singleSelect : true
	});
	var headTbar = new Ext.Toolbar({
		region : 'north',
    	border:false,
        height:25,
        items:['会议开始日期:', '从', startDate, '到',endDate,"-" ,'申请部门:',txtMrDept, "-" ,'申请人:',member,"-" ,'是否超支:',cmbIsOver,"-" ,'上报状态:',cmbStatus]
    });
    var gridTbar = new Ext.Toolbar({
    	border:false,
        height:25,
        items:[btnQuery,btnPrint, btnExport]
    });
	var recordGrid = new Ext.grid.GridPanel({
		region : "center",
		store : queryStore,
		columns : [
				// 自动行号
				new Ext.grid.RowNumberer({
							header : "行号",
							width : 31
						}), {
					header : "会议审批单号",
					sortable : false,
					dataIndex : 'meetId'
				}, {
					header : "申请人",
					sortable : false,
					dataIndex : 'name'
				}, {
					header : "申请部门",
					sortable : false,
					dataIndex : 'depName'
				}, {
					header : "会议名称",
					sortable : false,
					dataIndex : 'meetName'
				}, {
					header : "会议开始时间",
					sortable : false,
					dataIndex : 'startMeetDate'
				}, {

					header : "会议结束时间",
					sortable : false,
					dataIndex : 'endMeetDate'
				}, {
					header : "费用名称",
					sortable : false,
					dataIndex : 'payName'
				}, {
					header : "预算费用",
					sortable : false,
					align : 'right',
					dataIndex : 'payBudget',
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
                                    total=store.getAt(i).get('payBudget')*1;
                                    totalSum += total;
                                }
                                return numberFormat(totalSum);
                            	}
                            }
                        },{
                    header:"实际费用",   	
					sortable : false,
					align : 'right',
					dataIndex : 'payReal',
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
                                    total=store.getAt(i).get('payReal')*1;
                                    totalSum += total;
                                }
                                return numberFormat(totalSum);
                            	}
                            }
				}, {
					header : "费用预算汇总",
					sortable : false,
					align : 'right',
					dataIndex : 'budpayInall',
					renderer:function(value, cellmeta, record, rowIndex,
                            columnIndex, store){ 
                            if(record.get("meetId") == null && record.get("countType") != 'total')
                            	return "";
                            // 如果不是最后一行
                            if (rowIndex < store.getCount() - 1) {
                                return numberFormat(value);                     
                            // 如果是最后一行   
                            } else {
                                totalSum = 0;
                                var total="";
                                // 对该列除最后一个单元格以为求和
                                for (var i = 0; i < store.getCount() - 1; i++) {
                                    total=store.getAt(i).get('budpayInall')*1;
                                    totalSum += total;
                                }
                                return numberFormat(totalSum);
                            	}
                            }
					
				}, {
					header : "实际费用汇总",
					sortable : false,
					align : 'right',
					dataIndex : 'realpayInall',
					renderer:function(value, cellmeta, record, rowIndex,
                            columnIndex, store){ 
                            if(record.get("meetId") == null && record.get("countType") != 'total')
                            	return "";
                            // 如果不是最后一行
                            if (rowIndex < store.getCount() - 1) {
                                return numberFormat(value);                     
                            // 如果是最后一行   
                            } else {
                                totalSum = 0;
                                var total="";
                                // 对该列除最后一个单元格以为求和
                                for (var i = 0; i < store.getCount() - 1; i++) {
                                    total=store.getAt(i).get('realpayInall')*1;
                                    totalSum += total;
                                }
                                return numberFormat(totalSum);
                            	}
                            }
				}, {
					
					header : "差额",
					width:85,
					sortable : false,
					align : 'right',
					dataIndex : 'balance',
					renderer:function(value, cellmeta, record, rowIndex,
                            columnIndex, store){  
                            	if(record.get("meetId") == null &&　record.get("countType") != 'total')
                            	return "";
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
					header : "会议其他要求",
					sortable : false,
					dataIndex : 'meetOther'
				}, {
					header : "上报状态",
					sortable : false,
					dataIndex : 'dcmStatus'
					
				}
				],
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
	
//	// 双击进入登记tab
    recordGrid.on("rowdblclick",printMaterialType); 
    
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
        return  v + "元";
	}
	
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
        // 停止原来编辑
        recordGrid.stopEditing();
        // 插入统计行
        queryStore.insert(count, record);
        recordGrid.getView().refresh();   
        totalCount = queryStore.getCount() - 1;
    }
        /**
     * 查询按钮按下时
     */
    function queryMaterial() 
    {
    	if(checkDate()) 
    	{
	    	queryStore.load({
		        params : {
		        	startDate:startDate.getValue(),
		        	endDate:endDate.getValue(),
		        	depCode:hiddenMrDept.getValue(),
		        	member:member.getValue(),
		        	overSpend:cmbIsOver.getValue(),
		        	dcmStatus:cmbStatus.getValue()
		    	},
		    	callback: function()　{
	                if(queryStore.getCount() > 0)
	                    {
	                    	addLine();
	                        // 设置为可用
	                        btnExport.setDisabled(false);
	                    }else 
	                    {   
	                    	Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, MessageConstants.COM_I_003);
	                        // 如果检索数据为空，设置导出按钮不可用
	                        btnExport.setDisabled(true);
	                        
	                    }
	            }
			})
    	}
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
     *  打印按钮按下时
     */
	function printMaterialType() 
	{
		var records = recordGrid.selModel.getSelections();
		var record = recordGrid.getSelectionModel().getSelected();
		var recordslen = records.length;
        if(recordslen < 1)
        {
            // 如果选择记录小于1，弹出提示：”请选择一行“
                Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
            MessageConstants.COM_I_001);
        }else {
        	if(record.get("countType") != 'total')
        	{
        	var meetId = record.get("meetHidId");
        	//调用会议审批单报表（参数：画面选中行的会议申请单号）
            // xsTan 追加开始 2009-2-3 打印接口追加
            window.open("/power/report/webfile/administration/ARF04.jsp?applyNo=" +meetId);
            // xsTan 追加结束 2009-2-3
        	}
        }
	}
	
		/**
	 * 导出按钮按下时 
	 */
	function exportOut()
	{
		// 弹出窗口
		Ext.Msg.confirm(Constants.NOTICE_CONFIRM,MessageConstants.COM_C_007,
            function(buttonobj) {
            	// 如果选是的话，调用共同导出方法
                if (buttonobj == "yes") {
                	document.all.blankFrame.src = "administration/exportBusinessApprovalFile.action";
                }
            })
	}
	
	// **********主画面********** //
	new Ext.Viewport({
		enableTabScroll : true,
		layout : 'border',
		items : [headTbar,recordGrid]
	});

})