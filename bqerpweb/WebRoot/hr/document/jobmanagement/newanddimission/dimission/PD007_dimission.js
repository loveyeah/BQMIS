// 画面：档案管理/招聘离职管理/员工离职登记
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
    // 重复标记
    Constants.DATE_REPEAT = "R";
    // 存档
    ONLYSAVE_0 = "0";
    // 保存
    ONLYSAVE_1 = "1";
    // 新增
    ADDFLAG_1 = "1";
    // 修改
    ADDFLAG_0 = "0";
    var lastModDate = "";
    var empLastModDate = "";
    var onlySaveFlag = "";
    var addFlag = "";
    // 修改时：流水号，新增时：离职人员id
    var empidOrDimissionid = "";
    // 系统当前日期
    var sysDate = new Date();
    sysDate = sysDate.format('Y');
    // 年度选择
    var txtYear = new Ext.form.TextField({
        id : 'txtYear',
        name : 'year',
        style : 'cursor:pointer',
        width : 80,
        value : sysDate,
        readOnly : true
    });
    txtYear.onClick(function() {
                WdatePicker({
                    startDate : '%y',
                    dateFmt : 'yyyy',
                    alwaysUseStartDate : false,
                    isShowClear : true,
                    isShowToday : false,
                    onpicked : function() {
                        txtYear.blur();
                    }
                });
            })
    // 部门选择
    var txtDept = new Ext.form.TextField({
        fieldLabel : '部门',
        width : 100,
        valueField : 'id',
        hiddenName : 'dept',
        maxLength : 100,
        anchor : '100%',
        readOnly : true
    });
    txtDept.onClick( function() {
                var args = {
                    selectModel : 'single',
                    rootNode : {
                        id : '0',
                        text : '合肥电厂'
                    },
                    onlyLeaf : false
                };
                this.blur();
                var dept = window.showModalDialog('../../../../../comm/jsp/hr/dept/dept.jsp', args,
                'dialogWidth:' + Constants.WIDTH_COM_DEPT +'px;dialogHeight:'
                + Constants.HEIGHT_COM_DEPT + 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
                if (typeof(dept) != "undefined") {
                    txtDept.setValue(dept.names);
                    hiddenMrDept.setValue(dept.ids);
                }
            })
    // 隐藏域部门ID
    var hiddenMrDept = new Ext.form.Hidden({
        hiddenName : 'dept'
    })
    // 离职类别选择
    var drpType = new Ext.form.CmbHRBussiness({
        fieldLabel : '离厂类别',
        type : "离职类别",
        width : 100,
        allowBlank : true,
        id : 'drpType',
        hiddenName : 'type'
    })
    
    // 通知单号 add by ywliu 20100618
    var txtAdvicenoteNo = new Ext.form.TextField({
        id : 'txtAdvicenoteNo',
        name : 'advicenoteNo',
        width : 80
    });

    // 查询按钮
    var queryBtn = new Ext.Button({
        id : 'query',
        text : Constants.BTN_QUERY,
        iconCls : Constants.CLS_QUERY,
        handler : queryRecord
    });
  

    // 导出按钮 add by ywliu 20100618
    var exportBtn = new Ext.Button({
        text : Constants.BTN_EXPORT,
        iconCls : Constants.CLS_EXPORT,
        handler : exportHandler
    });
    var headTbar = new Ext.Toolbar({
        region : 'north',
        border : false,
        height : 25,
        items : ["年度:", txtYear, "-", "部门:", txtDept, "-", "离厂类别:", drpType, "-", "通知单号:", txtAdvicenoteNo, "-", queryBtn, 
         exportBtn]
    });

    // grid中的数据 员工工号,员工姓名,员工类别,原工作部门,原工作岗位,
    // 离职类别,离职日期,离职原因,离职后去向,是否存档,备注,(离职人员ID)
    var runGridList = new Ext.data.Record.create([{
            name : "empCode"
        }, {
            name : "empName"
        }, {
            name : "empTypeName"
        }, {
            name : "oldDepName"
        }, {
            name : "oldStationName"
        }, {
            name : "outTypeName"
        }, {
            name : "disMissionDate"
        }, {
            name : "disMissionReason"
        }, {
            name : "whither"
        }, {
            name : "ifSave"
        }, {
            name : "memo"
        }, {
            name : "outTypeId"
        }, {
            name : "lastModifiedDate"
        }, {
            name : "empLastModifiedDate"
        }, {
            // 流水号
            name : "dimissionid"
        }, {
            // 离职人员ID
            name : "empId"
        }, {
            // 员工工号 add by drdu 20100506
            name : "newEmpCode"
        }, {
            // 通知单号 add by ywliu 20100617
            name : "advicenoteNo"
        }, {
            // 止薪日期 add by ywliu 20100617
            name : "stopsalaryDate"
        }]);

    // grid中的store
    var runGridStore = new Ext.data.Store({
        proxy : new Ext.data.HttpProxy({
            url : 'hr/getDismissionInfoList.action'
        }),
        reader : new Ext.data.JsonReader({
            root : 'list',
            totalProperty : 'totalCount'
        }, runGridList)
    });
//    runGridStore.setDefaultSort('disMissionDate', 'ASC');
    runGridStore.baseParams = {
        year : txtYear.getValue(),
        deptId : hiddenMrDept.getValue(),
        typeId : drpType.getValue(),
        advicenoteNo : txtAdvicenoteNo.getValue() // add by ywliu 20100618
        ,flag:'deptFlag'//add by sychen 20100716
    };
    // 初始化时,显示数据
    runGridStore.load({
        params : {
            start : 0,
            limit : Constants.PAGE_SIZE
        }
    });
    // 是否是查询操作
    var queryFlag = false;
    runGridStore.on("load", function() {
        if (runGridStore.getCount() == 0) {
            // 没有检索到任何信息(仅在查询处理后显示)
            if (queryFlag) {
                Ext.Msg.alert(Constants.REMIND, Constants.COM_I_003);
            }
        }
        queryFlag = false;
    });
    // add by ywliu 20100617
    var sm = new Ext.grid.CheckboxSelectionModel();

    // 运行执行的Grid主体 (员工工号,员工姓名,员工类别,原工作部门,原工作岗位,
    // 离职类别,离职日期,离职原因,离职后去向,是否存档,备注)
    var runGrid = new Ext.grid.GridPanel({
        store : runGridStore,
        region : 'center',
        columns : [sm,
            // 自动生成行号
            new Ext.grid.RowNumberer({
                header : '行号',
                width : 35
            })/*, {
                header : '员工工号',
                width : 100,
                align : 'left',
                sortable : true,
                hidden : true,
                dataIndex : 'empCode'
            }*/, {
                header : '工号',
                width : 80,
                align : 'left',
                sortable : true,
                dataIndex : 'newEmpCode'
            }, {
                header : '姓名',
                width : 80,
                align : 'left',
                sortable : true,
                dataIndex : 'empName'
            }/*, {
                header : '员工类别',
                width : 100,
                align : 'left',
                sortable : true,
                dataIndex : 'empTypeName'
            }*/, {
                header : '职务(岗位)',
                width : 100,
                align : 'left',
                sortable : true,
                dataIndex : 'oldStationName'
            }, {
                header : '工作部门',
                width : 150,
                align : 'left',
                sortable : true,
                dataIndex : 'oldDepName'
            }, {
                header : '离厂日期',
                width : 100,
                align : 'left',
                sortable : true,
                dataIndex : 'disMissionDate'
            }, {
                header : '止薪日期',
                width : 100,
                align : 'left',
                sortable : true,
                dataIndex : 'stopsalaryDate'
            }, {
                header : '备注',
                width : 200,
                align : 'left',
                sortable : true,
                dataIndex : 'memo'
            }, {
                header : '通知单号',
                width : 80,
                align : 'left',
                sortable : true,
                dataIndex : 'advicenoteNo'
                //add by sychen 20100717
                ,
                renderer:function(value){
                	if(value==null||value=="")
                	{
                		return "";
                	}
                	else
                	{
                	var strYear=new Date().getYear();
                	return "人离字("+strYear+")第"+value+"号";
                	}
                }
            }, {
                header : '离厂类别',
                width : 100,
                align : 'left',
                sortable : true,
                dataIndex : 'outTypeName'
            }/*, {
                header : '离职原因',
                width : 100,
                align : 'left',
                sortable : true,
                dataIndex : 'disMissionReason'
            }, {
                header : '离职后去向',
                width : 100,
                align : 'left',
                sortable : true,
                dataIndex : 'whither'
            }, {
                header : '是否存档',
                width : 100,
                align : 'left',
                sortable : true,
                dataIndex : 'ifSave',
                renderer : function(value) {
                    if (value == "1") {
                        value = "是";
                    } else if (value == "0") {
                        value = "否";
                    } else
                        value = "";
                    return value;
                }
            }*/, {
                header : '流水号',
                dataIndex : 'dimissionid',
                hidden : true
            }, {
                header : '离职人员ID',
                dataIndex : 'empId',
                hidden : true
            }, {
                header : '离职类别id',
                dataIndex : 'outTypeId',
                hidden : true
            }, {
                header : '人员上次修改日期',
                dataIndex : 'empLastModifiedDate',
                hidden : true
            }, {
                header : '上次修改日期',
                dataIndex : 'lastModifiedDate',
                hidden : true
            }],
        viewConfig : {
            forceFit : false
        },
      //  tbar : headTbar,
        // 分页
        bbar : new Ext.PagingToolbar({
            pageSize : Constants.PAGE_SIZE,
            store : runGridStore,
            displayInfo : true,
            displayMsg : Constants.DISPLAY_MSG,
            emptyMsg : Constants.EMPTY_MSG
        }),
        sm : sm,
        frame : false,
        border : false,
        enableColumnHide : true,
        enableColumnMove : false
    });
   
    
    
     var titleLabel= new Ext.form.FieldSet({
        anchor : '100%',
        border : false,
        region:'north',
        height:35,
        style : "padding-top:8px;padding-bottom:0px;border:0px;margin:0px;text-align:center;",
        items : [new Ext.form.Label({
            text:'离职员工花名册',
            style:'font-size:22px;'
        })]
     });
       var fullPanel = new Ext.Panel({
        tbar : headTbar,
        layout:'border',
        border:false,
        items:[titleLabel,runGrid]
    })
    // 设定布局器及面板
    new Ext.Viewport({
        enableTabScroll : true,
        layout : "fit",
        border:false,
        items : [fullPanel]
    });


//    // 设定布局器及面板
//    new Ext.Viewport({
//        enableTabScroll : true,
//        layout : "border",
//        items : [{
//                xtype : "panel",
//                region : 'center',
//                layout : 'fit',
//                border : false,
//                items : [runGrid]
//            }]
//    });

  

    /**
     * 查询处理
     */
    function queryRecord() {
        queryFlag = true;
        runGridStore.baseParams = {
            year : txtYear.getValue(),
            deptId : hiddenMrDept.getValue(),
            typeId : drpType.getValue(),
            advicenoteNo : txtAdvicenoteNo.getValue() // add by ywliu 20100618
            ,flag:'deptFlag'//add by sychen 20100716
        };
        runGridStore.load({
            params : {
                start : 0,
                limit : Constants.PAGE_SIZE
            }
        });
    }
   
    

    


    /**
     * 导出处理 add by ywliu 20100618
     */
    function exportHandler() {
        Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_007, function(buttonobj) {
            if (runGrid.selModel.hasSelection()) {
            	var records = runGrid.selModel.getSelections();
	            var recordslen = records.length;
	            if (recordslen > 1) {
					var date = new Date();
	            	var strDate = date.format('y')+"年"+date.format('m')+"月"+date.format('d')+"日";
	            	var recs = runGrid.getSelectionModel().getSelections();
					var html = ['<table border=1><tr><th colspan=13>大唐陕西发电有限公司灞桥热电厂'+date.getYear()+"年"+'职工离厂通知单</th>'];
					html.push('<tr><th rowspan=2>姓名</th><th rowspan=2 colspan=2>职务（岗位）</th><th rowspan=2 colspan=2>工作部门</th><th colspan=3>离厂日期</th><th colspan=3>止薪日期</th><th rowspan=2 colspan=2>备注</th></tr>');
					html.push('<tr><th>年</th><th>月</th><th>日</th><th>年</th><th>月</th><th>日</th></tr>');
					for(var i = 0; i<recs.length; i++){
						var re = recs[i];
						var dateStr = re.get('disMissionDate')
						var stpslryStr = re.get('stopsalaryDate')
						html.push('<tr><td>'+re.get('empName')+'</td><td colspan=2>'+re.get('oldStationName')+'</td>' +
								'<td colspan=2>'+re.get('oldDepName')+'</td><td>'+dateStr.substring(0,4)+'</td>' +
								'<td>'+dateStr.substring(5,7)+'</td><td>'+dateStr.substring(8)+'</td>' +
										'<td>'+stpslryStr.substring(0,4)+'</td><td>'+stpslryStr.substring(5,7)+'</td>' +
												'<td>'+stpslryStr.substring(8)+'</td><td colspan=2>'+re.get('memo')+'</td></tr>');
					}						
					for(var j = 0; j<3; j++){
						html.push('<tr><td></td><td colspan=2></td><td colspan=2></td><td></td><td></td><td></td><td></td><td></td>' +
											'<td></td><td colspan=2></td></tr>')
					}	
					html.push('<tr border=0><th colspan=3 align="left">厂长:</th><th colspan=5 align="left">人力资源部主任:</th><th colspan=5 align="left">制单:</th></tr>');
					html.push('</table>');
					html = html.join(''); // 最后生成的HTML表格
					tableToExcel(html);
	            } else {
	            	var date = new Date();
	          
	            	var strDate = date.format('y')+"年"+date.format('m')+"月"+date.format('d')+"日";
	            		
	            	var recs = runGrid.getSelectionModel().getSelections();
					var html = ['<table border=1><tr><th colspan=13>大唐陕西发电有限公司灞桥热电厂职工离厂通知单</th>'];
					html.push('<tr></tr>');
					html.push('<tr><th colspan=3 align="center">查照</th><th colspan=5 align="center">'+strDate+'</th><th colspan=5 align="center">人离字（'+ date.getYear()+'）第'+recs[0].get('advicenoteNo')+'号</th></tr>');
					html.push('<tr><th rowspan=2>姓名</th><th rowspan=2 colspan=2>职务（岗位）</th><th rowspan=2 colspan=2>工作部门</th><th colspan=3>离厂日期</th><th colspan=3>止薪日期</th><th rowspan=2 colspan=2>备注</th></tr>');
					html.push('<tr><th>年</th><th>月</th><th>日</th><th>年</th><th>月</th><th>日</th></tr>');
					var re = recs[0];
					var dateStr = re.get('disMissionDate')
					var stpslryStr = re.get('stopsalaryDate')
					html.push('<tr><td>'+re.get('empName')+'</td><td colspan=2>'+re.get('oldStationName')+'</td>' +
							'<td colspan=2>'+re.get('oldDepName')+'</td><td>'+dateStr.substring(0,4)+'</td>' +
							'<td>'+dateStr.substring(5,7)+'</td><td>'+dateStr.substring(8)+'</td>' +
									'<td>'+stpslryStr.substring(0,4)+'</td><td>'+stpslryStr.substring(5,7)+'</td>' +
											'<td>'+stpslryStr.substring(8)+'</td><td colspan=2>'+re.get('memo')+'</td></tr>');
					for(var j = 0; j<3; j++){
						html.push('<tr><td></td><td colspan=2></td><td colspan=2></td><td></td><td></td><td></td><td></td><td></td>' +
											'<td></td><td colspan=2></td></tr>')
					}	
					html.push('<tr border=0><th colspan=3 align="left">厂长:</th><th colspan=5 align="left">人力资源部主任:</th><th colspan=5 align="left">制单:</th></tr>');
					html.push('</table>');
					html = html.join(''); // 最后生成的HTML表格
					tableToExcel(html);
	            }	
				}else{
					Ext.Msg.alert('提示','请选择要导出的数据！')
				}
        });
    }
    
    /**
     * 将HTML转化为Excel文档
     */
    function tableToExcel(tableHTML){
		window.clipboardData.setData("Text",tableHTML);
		try{
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExApp.visible = true;
		}catch(e){
			if(e.number != -2146827859){
				Ext.Msg.alert('提示','您的电脑没有安装Microsoft Excel软件!')
			}
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}
});