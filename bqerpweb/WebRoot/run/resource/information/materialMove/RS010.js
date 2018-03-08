Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Constants.RI003_E_001 = "起始时间不能大于结束时间。";
Ext.onReady(function() {
    Ext.QuickTips.init();  
    // 把数量用逗号分隔
    function divide(value) {
        var svalue = value + "";
        var decimal = "";
        // 如果有小数
        if (svalue.indexOf(".") > 0) {
            decimal = svalue.substring(svalue.indexOf("."), svalue.length);
        }
        tempV = svalue.substring(0, svalue.length - decimal.length);
        svalue = "";
        while (Div(tempV, 1000) > 0) {
            var temp = Div(tempV, 1000);
            var oddment = tempV - temp*1000;
            var soddment = "";
            tempV = Div(tempV, 1000);
            soddment += (0 == Div(oddment,100)) ? "0" : Div(oddment,100);
            oddment -= Div(oddment,100)*100;
            soddment += (0 == Div(oddment,10)) ? "0" : Div(oddment,10);
            oddment -= Div(oddment,10)*10;
            soddment += (0 == Div(oddment,1)) ? "0" : Div(oddment,1);
            oddment -= Div(oddment,1)*1;
            svalue = soddment + "," + svalue;
        }
        svalue = tempV + "," + svalue;
        svalue = svalue.substring(0,svalue.length - 1);
        svalue += decimal;
        return svalue;
    }
    // 整除
    function Div(exp1, exp2)
    {
        var n1 = Math.round(exp1); //四舍五入
        var n2 = Math.round(exp2); //四舍五入
        
        var rslt = n1 / n2; //除
        
        if (rslt >= 0)
        {
            rslt = Math.floor(rslt); //返回值为小于等于其数值参数的最大整数值。
        }
        else
        {
            rslt = Math.ceil(rslt); //返回值为大于等于其数字参数的最小整数。
        }
        
        return rslt;
    }
     // 计划开始时间
    var planStartDate = new Ext.form.TextField({
        id : 'planStartDate',
        name : 'workticketBaseInfo.planStartDate',
        style : 'cursor:pointer',
        width:145,
        listeners : {
            focus : function() {
                WdatePicker({
                    dateFmt : 'yyyy-MM-dd'
                });
            }
        }
    })
    // 计划结束时间
    var planEndDate = new Ext.form.TextField({
        id : 'planEndDate',
        name : 'workticketBaseInfo.planEndDate',
        style : 'cursor:pointer',
        width:145,
        listeners : {
            focus : function() {
                var pkr = WdatePicker({
                    dateFmt : 'yyyy-MM-dd'
                });
            }
        }
    })

     // 物料编码
//    var txtMaterialNo = new Ext.form.TextField({
//        fieldLabel : '物料编码' ,
//        isFormField : true,
//        readOnly : true,
//        id : "materialNo",
//        name : "materialNo",
//        width: 180,
//        listeners : {
//            focus : selectMaterial
//        }
//    });
    // 物料名称
    var txtMaterialName = new Ext.form.TextField({
        fieldLabel : '物料名称' ,
        width : 180,
        readOnly : false,
        id : "materialName",
        name :"materialName"
    });
     // 替代物料流水号
//    var hdnAltMatId = new Ext.form.Hidden({
//        id: "materialId",
//        name: "materialId"
//    });
    
    // 事务类型检索
    var transStore = new Ext.data.JsonStore({
                root : 'list',
                url : "resource/getTransListForCo.action",
                fields : ['transName', 'transCode']
            })
    transStore.load();
    // 事务类型
    var transTypeCo = new Ext.form.ComboBox({
        id : "transType",
        name : 'transType',
        allowBlank : true,
        triggerAction : 'all',
        store : transStore,
        width : 180,
        displayField : "transName",
        valueField : "transCode",
        mode : 'local',
        readOnly : true
    });
   transStore.on('load', function(e, records, o) {
				transTypeCo.setValue(records[0].data.transCode);
			});
    // head工具栏
    var headerTbar = new Ext.Toolbar({
    	region : 'north',
    	border : false,
        items : ["事务类型:",transTypeCo,'&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp操作时间:',planStartDate,"--",planEndDate,"&nbsp"]
    });
     // grid工具栏
    var gridTbar = new Ext.Toolbar({
    	border : false,
        items : [
        //"物料编码:",txtMaterialNo,"&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp" +
        		"物料名称:",txtMaterialName,'-',
            {
                text : "查询",
                iconCls : 'query',
                handler : searchBtn
            }]
    });
    // 物料异动grid
    var queryStore = new Ext.data.JsonStore({
        url : 'resource/getTransMoveList.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : [
            // 流水号
            {name:'transHisId'},
            // 单据号
            {name: 'orderNo'}, 
            // 项次号
            {name: 'sequenceNo'},
            // 事务名称
            {name: 'transName'},
            // 物料编码
            {name: 'materialNo'},
            // 物料名称            
            {name: 'materialName'},
            // 规格型号           
            {name: 'specNo'},
            // 材质/参数           
            {name: 'parameter'},
            // 异动数量           
            {name: 'transQty'},
            // 单位           
            {name: 'transUmId'},
            // 操作人          
            {name: 'operatedBy'},
            // 操作时间          
            {name: 'operatedDate'},
            // 操作仓库            
            {name: 'whsName'},
            // 操作库位            
            {name: 'locationName'},
            // 调入仓库            
            {name: 'whsNameTwo'},
            //调入库位                
            {name: 'locationNameTwo'}, 
            // 批号           
            {name: 'lotNo'},
            // 备注            
            {name: 'memo'}
            ]
        });
    // 载入数据
    queryStore.load({params:{start: 0, limit : Constants.PAGE_SIZE}} );

    // grid
    var transPanel = new Ext.grid.GridPanel({
        region : "center",
        enableColumnMove : false,
        enableColumnHide : true,
        frame : false,
        border : false,
        store : queryStore,
        // 单选
        sm : new Ext.grid.RowSelectionModel({singleSelect : true}),
        columns : [
            new Ext.grid.RowNumberer({header:"行号",width : 35}),
              //流水号
            {   header : "流水号",
                width : 80,
                sortable : true,
                dataIndex : 'transHisId'            
            },
              //单据号
            {   header : "单据号",
                width : 55,
                sortable : true,
                dataIndex : 'orderNo'            
            },
              //项次号
            {   header : "项次号",
                width : 55,
                align : 'right',
                sortable : true,
                dataIndex : 'sequenceNo'            
            },
              //事务名称
            {   header : "事务名称",
                   width : 150,
                sortable : true,
                dataIndex : 'transName'            
            },
              //物料编码        
            {   header : "物料编码",
                width : 80,
                sortable : true,
                dataIndex : 'materialNo'            
            },
              //物料名称
            {   header : "物料名称",
                width : 100,
                sortable : true,
                dataIndex : 'materialName'            
            },
              //规格型号
            {   header : "规格型号",
                width : 80,
                sortable : true,
                dataIndex : 'specNo'            
            },
              //材质/参数
            {   header : "材质/参数",
                width : 80,
                sortable : true,
                dataIndex : 'parameter'            
            },
              //异动数量
            {   header : "异动数量",
                align : 'right',
                width : 80,
                sortable : true,
                dataIndex : 'transQty',
                renderer : function(value) {    
                               if (("" == value || null == value) && !("0" == value)) {
                                   return "";
                               } else {
                                   return divide(value);
                           }
                }
            },
            //  单位
            {   header : "单位",
                width : 100,
                sortable : true,
                dataIndex : 'transUmId'            
            },
              //操作人
            {   header : "操作人",
                width : 100,
                sortable : true,
                dataIndex : 'operatedBy'            
            },
              //操作时间
            {   header : "操作时间",
                width : 100,
                sortable : true,
                dataIndex : 'operatedDate'            
            },
              //操作仓库
            {   header : "操作仓库",
                width : 100,
                sortable : true,
                dataIndex : 'whsName'            
            },
              //操作库位
            {   header : "操作库位",
                width : 100,
                sortable : true,
                dataIndex : 'locationName'            
            },
              //调入仓库
            {   header : "调入仓库",
                width : 100,
                sortable : true,
                dataIndex : 'whsNameTwo'            
            },
              //调入库位
            {   header : "调入库位",
                width : 100,
                sortable : true,
                dataIndex : 'locationNameTwo'            
            },
              //批号
            {   header : "批号",
                width : 40,
                sortable : true,
                dataIndex : 'lotNo'            
            },
              //备注
            {   header : "备注",
                width : 200,
                sortable : true,
                dataIndex : 'memo'            
            }

        ],
         tbar : gridTbar,
        // 分页
        bbar : new Ext.PagingToolbar({
            pageSize : Constants.PAGE_SIZE,
            store : queryStore,
            displayInfo : true,
            displayMsg : Constants.DISPLAY_MSG,
            emptyMsg : Constants.EMPTY_MSG
        })
    });

    // 显示区域
    var layout = new Ext.Viewport({
        layout : "border",
        border : false,
        defaults : {
                    autoScroll : true
                },
        items : [headerTbar,transPanel]   
               
    });
    //  ↓↓*******************************处理****************************************
    // 查询处理函数
    function searchBtn() {
        if(!(planStartDate.getValue() == null||""==planStartDate.getValue() )&& !(planEndDate.getValue() == null||""==planEndDate.getValue())) {
        if (planStartDate.getValue() > planEndDate.getValue() ) {
            Ext.Msg.alert(Constants.SYS_REMIND_MSG,Constants.RI003_E_001 );
            return;
        } else {
            // 刷新数据
            gridFresh();

        }
    } else {
    	 // 刷新数据
         gridFresh();

    }
    }
    // 刷新Grid中的数据
    function gridFresh() {
        queryStore.baseParams = {
            // 开始时间
            sdate : planStartDate.getValue(),
            // 结束时间
            edate : planEndDate.getValue(),
            // 物料id    
        //    materialId : hdnAltMatId.getValue(),
            materialId : txtMaterialName.getValue(),
            // 事务类型
            transType : transTypeCo.getValue()
        };
        queryStore.load({
                    params : {
                        start : 0,
                        limit : Constants.PAGE_SIZE
                    }
                });
    }
     /**
     * 选择物料信息表单信息:form
     */
//    function selectMaterial() {
//        var mate = window.showModalDialog('../../plan/RP001.jsp', window,'dialogWidth=800px;dialogHeight=550px;status=no');
//        if (typeof(mate) != "undefined") {
//            // 设置物料编码
//            txtMaterialNo.setValue(mate.materialNo);
//            // 设置物料名称
//            txtMaterialName.setValue(mate.materialName);
//            // 设置物料id
//            hdnAltMatId.setValue(mate.materialId);
//        }
//        Ext.get("materialName").focus();
//    }    
 });