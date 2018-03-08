Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.form.Label.prototype.setText = function(argText) {
    this.el.dom.innerHTML = argText;
}
Ext.onReady(function() {
	var  materialClassCode="";
    // ↓↓*****弹出窗口*******↓↓//
    // 组件默认宽度
    var width = 80;
    // 物料编码
    var lblMaterialId = new Ext.form.Label({
                style : "fontSize:11px",
                id : 'materialId',
                width : width
            });
            
    // 物料名称
    var lblMaterialName = new Ext.form.Label({
                style : "fontSize:11px",
                id : 'materialName',
                width : width
            });
            
    // 图号
    var lblDocNo = new Ext.form.Label({
                style : "fontSize:11px",
                id : 'docNo',
                width : width
            });
    // 返回button
    var btnCancel = new Ext.Button({
                text : "返回",
                handler : function() {
                    // 关闭画面
                    win.hide();
                    // 返回空
                    return;
                }
            });
    // 显示图片
    var txtImage = new Ext.form.TextField({
                id : "materialMap",
                height : 260,
                width : 500,
                name : 'materialMap',
                inputType : 'image',
                anchor : "95%"
            });

    // formpanel
    var accessoryPanel = new Ext.FormPanel({
                tbar : ['物料编码： ', lblMaterialId, '-', '物料名称： ', lblMaterialName,
                        '-', '图号： ', lblDocNo, '-', btnCancel],
                frame : false,
                layout : 'fit',
                items : [txtImage]
            });
            
    var tooltip = null;     
    // 弹出窗口
    var win = new Ext.Window({
                title : '附件查看',
                width : 500,
                height : 320,
                modal : true,
                buttonAlign : "center",
                resizable : false,
                layout : 'fit',
                closeAction : 'hide',
                items : [accessoryPanel],
                
                listeners:{
                    "show":function() {
                        if(tooltip == null){
                            tooltip = new Ext.ToolTip({
                                          target : 'materialName',
                                          html : "&nbsp;",
                                          dismissDelay : 0 , 
                                          showDelay : 200,  
                                          hideDelay : 0 , 
                                          autoHeight : true,  
                                          autowidth : true
                                        });
                          tooltip.on("show", function() {
                              var _ttv = Ext.DomQuery.select(".x-tip-body", this.el.dom)[0];
                              if(win["_materialName"]) {
                                  _ttv.firstChild.nodeValue = win["_materialName"];
                              } else {
                                  _ttv.firstChild.nodeValue = "&nbsp;"
                              }
                          })
                        }
                    }
                }
            });
            
    // ↑↑********弹出窗口*********↑↑//

    // ↓↓********** 主画面*******↓↓//

    // 查询条件
    var txtFuzzy = new Ext.form.TextField({
                id : "fuzzy",
                name : "fuzzy",
                emptyText : '物料名称/物料编码/规格型号/物料分类',
                width : 220
            });
    // 模糊查询
    var btnQuery = new Ext.Button({
                text : "模糊查询",
                handler : function() {
                    materialStore.load({
                                params : {
                                    start : 0,
                                    limit : Constants.PAGE_SIZE
                                }
                            });
                    cleanFun();
                }
            });
    // 确认
    var btnSave = new Ext.Button({
                text : "确认",
                iconCls : Constants.CLS_SAVE,
                handler : function() {
                    // 如果没有选择，弹出提示信息
                    if (!materialGrid.selModel.hasSelection()) {
                        Ext.Msg.alert(Constants.SYS_REMIND_MSG,
                                Constants.COM_I_001);
                        return;
                    }
                    // 返回
                    returnList();
                }
            });
    // 取消
    var btnCancel = new Ext.Button({
        text : "取消",
        iconCls : Constants.CLS_CANCEL,
        handler : function() {      
            var obj = new Object();         
            // 返回值
            obj.materialId = "",
            obj.materialNo = "",
            obj.materialName = "",
            obj.specNo = "",
            obj.parameter ="",
            obj.stockUmId = "",
            obj.factory = "",
            obj.docNo = "",
            obj.className = "",
            obj.locationNo = "",
            obj.maxStock = "",
            obj.qaControlFlag = "",
            window.returnValue = obj;
            // 关闭画面
            window.close();
        }
    });
    // 物料编码
    var lblMaterialNo1 = new Ext.form.Label({
                x : 10,
                y : 5,
                style : "fontSize:13px",
                text : '物料编码:'
            });
    var lblMaterialNo2 = new Ext.form.Label({
                x : 70,
                y : 5,
                style : "fontSize:13px",
                id : 'lblMaterialNo'
            });
    // 物料名称
    var lblMaterialName1 = new Ext.form.Label({
                x : 196,
                y : 5,
                style : "fontSize:13px",
                text : '物料名称:'
            });
    var lblMaterialName2 = new Ext.form.Label({
                x : 256,
                y : 5,
                style : "fontSize:13px",
                id : 'lblMaterialName'
            });
    // 规格型号
    var lblSpecNo1 = new Ext.form.Label({
                x : 445,
                y : 5,
                style : "fontSize:13px",
                text : '规格型号:'
            });
    var lblSpecNo2 = new Ext.form.Label({
                x : 505,
                y : 5,
                style : "fontSize:13px",
                id : 'lblSpecNo:'
            });
    // 第一行
    var secondLine = new Ext.Panel({
        border : false,
        height : 30,
        layout : "absolute",
        style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
        anchor : '100%',
        items : [lblMaterialNo1, lblMaterialNo2, lblMaterialName1,
                lblMaterialName2, lblSpecNo1, lblSpecNo2]
    });
    // 材质/参数
    var lblParameter1 = new Ext.form.Label({
                x : 5,
                y : 12,
                style : "fontSize:13px",
                text : '材质/参数:'
            });
    var lblParameter2 = new Ext.form.Label({
                x : 70,
                y : 12,
                style : "fontSize:13px",
                id : 'lblParameter'
            });
    // 计量单位
    var lblUmId1 = new Ext.form.Label({
                x : 196,
                y : 12,
                style : "fontSize:13px",
                text : '计量单位:'
            });
    var lblUmId2 = new Ext.form.Label({
                x : 256,
                y : 12,
                style : "fontSize:13px",
                id : 'lblUmId'
            });
    // 图号
    var lblDocNo1 = new Ext.form.Label({
                x : 330,
                y : 12,
                style : "fontSize:13px",
                text : '图号:'
            });
    var lblDocNo2 = new Ext.form.Label({
                x : 400,
                y : 12,
                style : "fontSize:13px",
                id : 'lblDocNo'
            });
    // 物料分类
    var lblClassName1 = new Ext.form.Label({
                x : 445,
                y : 12,
                style : "fontSize:13px",
                text : '物料分类:'
            });
    var lblClassName2 = new Ext.form.Label({
                x : 505,
                y : 12,
                style : "fontSize:13px",
                id : 'lblClassName'
            });
    // 第二行
    var thirdLine = new Ext.Panel({
                border : false,
                height : 30,
                layout : "absolute",
                style : "padding-top:0px;padding-bottom:0px;margin-bottom:0px",
                anchor : '100%',
                items : [lblParameter1, lblParameter2, lblUmId1, lblUmId2,
                        lblDocNo1, lblDocNo2, lblClassName1, lblClassName2]
            });
    // 物料信息明细部
    var material = Ext.data.Record.create([
            // 物料ID
            {
        name : 'materialId'
    },
            // 物料编码
            {
                name : 'materialNo'
            },
            // 物料名称
            {
                name : 'materialName'
            },
            // 规格型号
            {
                name : 'specNo'
            },
            // 材质/参数
            {
                name : 'parameter'
            },
            // start jincong 增加单位ID 2008-01-10
            // 单位ID
            {
                name : 'stockUmId'
            },
            // end jincong 增加单位ID 2008-01-10
            // 单位
            {
                name : 'stockUmName'
            },
            // 生产厂家
            {
                name : 'factory'
            },
            // 图号
            {
                name : 'docNo'
            },
            // ・・・（附件选择)
            {
                name : 'accessory'
            },
            // 物料类别名称
            {
                name : 'className'
            },
            // 最大库存量
            {
                name : 'maxStock'
            },
            // 是否免检
            {
                name : 'qaControlFlag'
            },
            // 采购计量单位ID
            {
                name : 'purUmId'
            }]);
    // 物料grid的store
    var materialStore = new Ext.data.JsonStore({
                url : 'resource/getMaterialNameList.action',
                root : 'list',
                totalProperty : 'totalCount',
                fields : material
            });
    // 载入数据
    materialStore.load({
                params : {
                    start : 0,
                    limit : Constants.PAGE_SIZE
                }
            });
    // before load事件,传递查询字符串作为参数
    materialStore.on('beforeload', function() {
                Ext.apply(this.baseParams, {
                            fuzzy : txtFuzzy.getValue(),
                            materialClassCode:materialClassCode
                        });
            });
    // 物料grid
    var materialGrid = new Ext.grid.GridPanel({
        region : "center",
        layout : 'fit',
        autoScroll : true,
        height : 410,
        isFormField : false,
        border : true,
        anchor : "0",
        // 标题不可以移动
        enableColumnMove : false,
        store : materialStore,
        columns : [new Ext.grid.RowNumberer({
                            header : "行号",
                            width : 35
                        }),
                // 物料ID
                {
                    header : "物料ID",
                    width : 100,
                    sortable : false,
                    hidden : true,
                    dataIndex : 'materialId'
                },
                // 物料编码
                {
                    header : "物料编码",
                    width : 100,
                    sortable : false,
                    dataIndex : 'materialNo'
                },
                // 物料名称
                {
                    header : "物料名称",
                    width : 200,
                    sortable : false,
                    dataIndex : 'materialName'
                },
                // 规格型号
                {
                    header : "规格型号",
                    width : 100,
                    sortable : false,
                    dataIndex : 'specNo'
                },
                // 材质/参数
                {
                    header : "材质/参数",
                    width : 100,
                    sortable : false,
                    dataIndex : 'parameter'
                },
                // 单位
                {
                    header : "单位",
                    width : 100,
                    sortable : false,
                    dataIndex : 'stockUmName'
                },
                // 生产厂家
                {
                    header : "生产厂家",
                    width : 100,
                    sortable : false,
                    dataIndex : 'factory'
                },
                // 图号
                {
                    header : "图号",
                    width : 100,
                    sortable : false,
                    dataIndex : 'docNo'
                },
                // ・・・（附件选择)
                {
                    header : " ",
                    width : 35,
                    sortable : false,
                    dataIndex : 'accessory',
                    renderer : creatButton
                },
                // 物料类别名称
                {
                    header : "物料类别名称",
                    width : 100,
                    sortable : false,
                    dataIndex : 'className'
                },
                // 最大库存量
                {
                    header : "最大库存量",
                    width : 100,
                    hidden : true,
                    sortable : false,
                    dataIndex : 'maxStock'
                },
                // 是否免检
                {
                    header : "是否免检",
                    width : 100,
                    hidden : true,
                    sortable : false,
                    dataIndex : 'qaControlFlag'
                }],
      //  autoSizeColumns : true,
        sm : new Ext.grid.RowSelectionModel({
                    singleSelect : true
                }),
        // 分页
        bbar : new Ext.PagingToolbar({
                    pageSize : Constants.PAGE_SIZE,
                    store : materialStore,
                    displayInfo : true,
                    displayMsg : Constants.DISPLAY_MSG,
                    emptyMsg : Constants.EMPTY_MSG
                }),
//        viewConfig : {
//            forceFit : true
//        },
        // grid监听
        listeners : {
            cellclick : function(grid, rowIndex, columnIndex, e) {
                var record = grid.getStore().getAt(rowIndex);
                var fieldName = grid.getColumnModel().getDataIndex(columnIndex);
                if ('accessory' == fieldName) {
                    var _materialName = record.get("materialName") || "";                   
                    win["_materialName"] = _materialName;
                    win.show();
                    
                    lblMaterialId.setText(record.get("materialNo") == null
                            ? ""
                            : Ext.util.Format.substr(record.get("materialNo"),
                                    0, 9));

                    if(_materialName.length > 8) {
                        _materialName = Ext.util.Format.substr(_materialName, 0, 8) + "...";
                    }
                    lblMaterialName.setText(_materialName);
                    lblDocNo
                            .setText(record.get("docNo") == null
                                    ? ""
                                    : Ext.util.Format.substr(record
                                                    .get("docNo"), 0, 9));
                    Ext.get("materialMap").dom.src = "resource/getMaterialMap.action?docNo="
                            + record.get("docNo");
                }
            }
        }
    });
    

    
    // 表单panel
    var formPanel = new Ext.FormPanel({
                tbar : [txtFuzzy, '-', btnQuery, '-', btnSave, '-', btnCancel],
                labelAlign : 'right',
                border : true,
                autoScroll : true,
                items : [{
                    xtype : 'fieldset',
                    id : 'formSet',
                    labelAlign : 'right',
                    labelWidth : 100,
                    title : '物料信息',
                    autoHeight : true,
                    bodyStyle : Ext.isIE
                            ? 'padding:0 0 5px 15px;'
                            : 'padding:10px 15px;',
                    border : true,
                    style : {
                        "margin-left" : "10px",
                        "margin-right" : Ext.isIE6 ? (Ext.isStrict
                                ? "10px"
                                : "13px") : "10px"
                    },
                    items : [secondLine, thirdLine]
                }, materialGrid]
            });

    // ↑↑********** 主画面*******↑↑//

    // ↓↓*******************************处理****************************************
    /**
     * 单击事件
     */
    materialGrid.addListener('rowclick', fieldDataSet);
    function fieldDataSet() {
        if (event.ctrlKey) {

            if (materialGrid.getSelectionModel().hasSelection()) {
                cleanFun();
                // 选择的记录
                var record = materialGrid.getSelectionModel().getSelected();
                // 给label设值
                lblMaterialNo2.setText(record.get("materialNo") == null
                        ? ""
                        : Ext.util.Format.substr(record.get("materialNo"), 0,
                                18));
                lblMaterialName2.setText(record.get("materialName") == null
                        ? ""
                        : Ext.util.Format.substr(record.get("materialName"), 0,
                                23));
                lblSpecNo2.setText(record.get("specNo") == null
                        ? ""
                        : Ext.util.Format.substr(record.get("specNo"), 0, 18));
                lblUmId2.setText(record.get("stockUmName") == null
                        ? ""
                        : record.get("stockUmName"));
                lblParameter2.setText(record.get("parameter") == null
                        ? ""
                        : Ext.util.Format
                                .substr(record.get("parameter"), 0, 18));
                lblDocNo2.setText(record.get("docNo") == null
                        ? ""
                        : Ext.util.Format.substr(record.get("docNo"), 0, 18));
                lblClassName2.setText(record.get("className") == null
                        ? ""
                        : Ext.util.Format
                                .substr(record.get("className"), 0, 18));
            } else {
                cleanFun();
            }

        } else {
            // 选择的记录
            var record = materialGrid.getSelectionModel().getSelected();
            // 给label设值
            lblMaterialNo2.setText(record.get("materialNo") == null
                    ? ""
                    : Ext.util.Format.substr(record.get("materialNo"), 0, 18));
            lblMaterialName2
                    .setText(record.get("materialName") == null
                            ? ""
                            : Ext.util.Format.substr(
                                    record.get("materialName"), 0, 23));
            lblSpecNo2.setText(record.get("specNo") == null
                    ? ""
                    : Ext.util.Format.substr(record.get("specNo"), 0, 18));
            lblUmId2.setText(record.get("stockUmName") == null ? "" : record
                    .get("stockUmName"));
            lblParameter2.setText(record.get("parameter") == null
                    ? ""
                    : Ext.util.Format.substr(record.get("parameter"), 0, 18));
            lblDocNo2.setText(record.get("docNo") == null
                    ? ""
                    : Ext.util.Format.substr(record.get("docNo"), 0, 18));
            lblClassName2.setText(record.get("className") == null
                    ? ""
                    : Ext.util.Format.substr(record.get("className"), 0, 18));
        }
    }
    /**
     * 清空事件
     */
    function cleanFun() {
        // 给label设值
        lblMaterialNo2.setText("");
        lblMaterialName2.setText("");
        lblSpecNo2.setText("");
        lblUmId2.setText("");
        lblParameter2.setText("");
        lblDocNo2.setText("");
        lblClassName2.setText("");
    }
    /**
     * 双击事件
     */
    materialGrid.addListener('rowdblclick', returnList);
    function returnList() {
        // 选择的记录
        var obj = new Object();
        var record = materialGrid.getSelectionModel().getSelected();
        // 返回值
        obj.materialId = record.get("materialId"), obj.materialNo = record
                .get("materialNo"), obj.materialName = record
                .get("materialName"), obj.specNo = record.get("specNo"), obj.parameter = record
                .get("parameter"), obj.stockUmId = record.get("stockUmName"),
                // start jincong 增加单位ID 2008-01-10
                obj.stock = record.get("stockUmId"),
                // end jincong 增加单位ID 2008-01-10
                obj.factory = record
                .get("factory"), obj.docNo = record.get("docNo"), obj.className = record
                .get("className"), obj.locationNo = record.get("locationNo"), obj.maxStock = record
                .get("maxStock"), obj.qaControlFlag = record
                .get("qaControlFlag");
                // 采购计量单位ID
                obj.purUmId = record.get("purUmId");
                window.returnValue = obj;
        // 关闭画面
        window.close();
    }
    /**
     * 弹出窗口
     */
    function creatButton() {
        var str = "<input type='button' value='···' width=20 style='height:20' />";
        return str;
    }
    //-----------add by fyyang 090623--------------------------
    var root =new Ext.tree.AsyncTreeNode({
		text : '物料',
		isRoot : true,
		id : '-1'
		
	});

	var mytree = new Ext.tree.TreePanel({
		renderTo : "treepanel",
		autoHeight:true,
			border:false,
		//height : 900,
		root : root,
		requestMethod : 'GET',
		loader : new Ext.tree.TreeLoader({
			url : "resource/getMaterialClass.action"
		})
	});

	mytree.on("click", clickTree, this);
	mytree.on('beforeload', function(node) {
		mytree.loader.dataUrl = 'resource/getMaterialClass.action';
	}, this);

	function clickTree(node) {
		materialClassCode=node.id;
		
		     materialStore.load({
                                params : {
                                    start : 0,
                                    limit : Constants.PAGE_SIZE
                                }
                            });
                    cleanFun();
		
	};
	root.expand();//展开根节点
    //----------------------------------------------------------------
    
        // 显示区域
    var layout = new Ext.Viewport({
                layout : 'border',
                margins : '0 0 0 0',
                border : true,
                items :  [
                {
			region : 'west',
			split : true,
			width : 160,
			layout : 'fit',
			minSize : 175,
			maxSize : 600,
			margins : '0 0 0 0',
			collapsible : true,
			border : false,
			autoScroll : true,
			items : [mytree]
		},{
		  region : "center",
		layout : 'fit',
		collapsible : true,
		autoScroll : true,
		items:[formPanel]
		}
                ]
            });

});