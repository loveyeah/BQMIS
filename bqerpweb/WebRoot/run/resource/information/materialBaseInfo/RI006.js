Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	
	var  materialClassCode="";
	Ext.QuickTips.init();
	// 允许输入的最大值
    Constants.MAXNUM15_2 = 99999999999.99;
    Constants.MAXNUM18_4 = 9999999999999.9980;
    Constants.MAXNUM15_0 = 99999999999;
    // 控件长度
    Constants.THEWIDTH = 180;
	// ↓↓*******************物料基础资料查询tab**************************************
    // add by ywliu 2009/6/29
    var root =new Ext.tree.AsyncTreeNode({
		text : '物料',
		isRoot : true,
		id : '-1'
		
	});
    var mytree = new Ext.tree.TreePanel({
		renderTo : "treepanel",
		autoHeight : true,
		border : false,
		//height : 900,
		root : root,
		requestMethod : 'GET',
		containerScroll : true,
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
                                    limit : 18
                                }
                            });
		
	};
	root.expand();//展开根节点
    // add by ywliu 2009/6/29

	// 物料grid的store
	var materialStore = new Ext.data.JsonStore({
			    url : 'resource/getMaterialBaseInfoList.action',
			    root : 'list',
			    totalProperty : 'totalCount',
				fields : [
						// 物料编码
						{
					    name : 'materialNo'
				        },
						// 物料名称
						{
							name : 'materialName'
						},
						// 物料类别（名称）
						{
							name : 'className'
						},
						// 规格型号
						{
							name : 'specNo'
						},
						// 材质/参数
						{
							name : 'parameter'
						},
						// 单位
						{
							name : 'stockUmName'
						},
						// 缺省仓库
						{
							name : 'whsName'
						},
						// 缺省库位
						{
							name : 'locationName'
						},
						// 缺省仓库
						{
							name : 'materialId'
						},
						//物料类别ID add by drdu 090624
						{
							name : 'materialClassId'
						}]
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
                            fuzzy : fuzzy.getValue(),
                            materialClassCode:materialClassCode // add by ywliu 2009/6/29
                        });
            });
    // 当没有数据时确认按钮不可用
    //materialStore.on('load',function(store,records){
    //    if(store.getCount() == 0){
    //        Ext.getCmp('btnOk').setDisabled(true);
    //    }else{
    //        Ext.getCmp('btnOk').setDisabled(false);
    //    }
   //});隐藏确认按钮 modify ywliu 090717
            
	// 查询字符串
	var fuzzy = new Ext.form.TextField({
				id : "fuzzy",
				name : "fuzzy",
				emptyText : "物料编码/物料名称/物料类别/规格型号",
				width : 400
			});
	// 物料grid
	var materialGrid = new Ext.grid.GridPanel({
				region : "center",
				layout : 'fit',
				// 标题不可以移动
				enableColumnMove : false,
				// 单选
        		sm : new Ext.grid.RowSelectionModel({singleSelect : true}),
			 	store : materialStore,
			 	sortInfo :{field: "materialNo", direction: "ASC"},
				columns : [
						// 自动生成行号
						new Ext.grid.RowNumberer({
									header : '行号',
									width : 35
								}),
						// 物料编码
						{
							header : "物料编码",
							width : 100,
							sortable : true,
							dataIndex : 'materialNo'
						},
						// 物料名称
						{
							header : "物料名称",
							width : 100,
							sortable : true,
							dataIndex : 'materialName'
						},
						// 物料类别（名称）
						{
							header : "物料类别",
							width : 100,
							sortable : true,
							dataIndex : 'className'
						},
						// 规格型号
						{
							header : "规格型号",
							width : 100,
							sortable : true,
							dataIndex : 'specNo'
						},
						// 材质/参数
						{
							header : "材质/参数",
							width : 100,
							sortable : true,
							dataIndex : 'parameter'
						},
						// 单位
						{
							header : "单位",
							width : 40,
							sortable : true,
							dataIndex : 'stockUmName'
						},
						// 缺省仓库
						{
							header : "缺省仓库",
							width : 150,
							sortable : true,
							dataIndex : 'whsName',
							renderer : getWhsList  // add by drdu 090624
						},
						// 缺省库位
						{
							header : "缺省库位",
							width : 150,
							sortable : true,
							dataIndex : 'locationName'
						},
						{
							header : "流水号",
							hidden : true,
							dataIndex : 'materialId'
						}],
				viewConfig : {
					forceFit : true
				},
				tbar : [fuzzy, "-",{
							text : "模糊查询",
							iconCls : Constants.CLS_QUERY,
							handler : function() {
							    materialStore.load({params:{start: 0, limit : Constants.PAGE_SIZE}} );
							}
						}],//, "-",{text : "确认",iconCls : Constants.CLS_OK,id : "btnOk",handler : function() {
						// 如果没有选择，弹出提示信息if(!materialGrid.selModel.hasSelection()){
                        // Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);return;}                    
                        // 进入登记页面gotoRegisterTab();}} 隐藏确认按钮 modify ywliu 090717
						
				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : Constants.PAGE_SIZE,
							store : materialStore,
							displayInfo : true,
							displayMsg : Constants.DISPLAY_MSG,
							emptyMsg : Constants.EMPTY_MSG
						})
			});
			
	// 双击进入登记tab
    materialGrid.on("rowdblclick", gotoRegisterTab);
    
	// 查询tab
	var queryPanel = new Ext.Panel({
//				layout : 'fit',
		layout : 'border',
				title : '查询',
				items : [ {
			region : 'west',
			split : true,
			width : 190,
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
		items:[materialGrid]
		}]// modify by ywliu 2009/6/29
//				items : [materialGrid]
			});
	// ↑↑*******************物料基础资料查询tab**************************************

	// ↓↓*******************物料基础资料登记tab**************************************
	// 各种单位下拉框数据源
	var  unitData = Ext.data.Record.create([
		{name : 'unitName'},
		{name : 'unitId'}//''retrieveCode',
	]);
	var allUnitStore = new Ext.data.JsonStore({
        url : 'resource/getAllUnitList.action',
        root : 'list',  
        fields : unitData    
    });
    allUnitStore.load({
    	callback : function() {
    		// 新记录
		    var record = new unitData({
		        	unitName : '',
		        	unitId : ''// retrieveCode
		        });
		    allUnitStore.insert(0, record);
		    cbxStockUnit.setValue("");
		    cbxBuyUnit.setValue("");
		    cbxSaleUnit.setValue("");
		    cbxLengthUnit.setValue("");
		    cbxWidthUnit.setValue("");
		    cbxHeightUnit.setValue("");
		    cbxWeightUnit.setValue("");
		    cbxVolumeUnit.setValue("");
    	}
    })
	// 缺省仓库下拉框数据源
//	var whsNameStore = new Ext.data.JsonStore({
//        url : 'resource/getWarehouseNameList.action',
//        root : 'list',  
//        fields : [{name : 'whsName'},
//            {name : 'whsNo'}]      
//    });
//    whsNameStore.load();
    //modify by fyyang 090617
//    whsNameStore.on("load", function(e, records, o) {
//		cbxDefaultWHouse.setValue(records[0].data.whsNo);
//	});
	// 缺省库位下拉框数据源
	var locationNameStore = new Ext.data.JsonStore({
        url : 'resource/getLocationNameList.action',
        root : 'list',  
        fields : [{name : 'locationName'},
            {name : 'locationNo'}]      
    });
    //---add by fyyang--------------------
    locationNameStore.load();
      locationNameStore.on("load", function(e, records, o) {
      	cbxDefaultWHousePostion.setValue("01");
	});
	//--------add end -------------
	// 物料状态下拉框数据源
	var statusNameStore = new Ext.data.JsonStore({
        url : 'resource/getStatusNameList.action',
        root : 'list',  
        fields : [{name : 'statusName'},
            {name : 'materialStatusId'}]      
    });	
    statusNameStore.load();
    statusNameStore.on("load", function(e, records, o) {
		//cbxMStatus.setValue(records[0].data.materialStatusId);
    	cbxMStatus.setValue(1); //modify by fyyang 
	});
	// 物料类型下拉框数据源
    var typeNameStore = new Ext.data.JsonStore({
        url : 'resource/getTypeNameList.action',
        root : 'list',  
        fields : [{name : 'typeName'},
            {name : 'materialTypeId'}]
    });
    typeNameStore.load();
    typeNameStore.on("load", function(e, records, o) {
		//cbxMType.setValue(records[0].data.materialTypeId);
    	cbxMType.setValue(2);
	});	
	// 物料分类下拉框数据源
    var classNameStore = new Ext.data.JsonStore({
        url : 'resource/getClassNameList.action',
        root : 'list',  
        fields : [{name : 'className'},
            {name : 'maertialClassId'}]
    });
    classNameStore.load();

	// 增加按钮
	var btnAdd = new Ext.Button({
		        tabIndex : 1,
				text : '新增',
				iconCls : Constants.CLS_ADD,
				handler : btnAddHandler
			});	
	// 删除按钮
	var btnDelete = new Ext.Button({
		        tabIndex : 2,
				text : Constants.BTN_DELETE,
				iconCls : Constants.CLS_DELETE,
				disabled : true,
				handler : deleteMaterial
			});
	// 保存按钮
	var btnSave = new Ext.Button({
		        tabIndex : 3,
				text : Constants.BTN_SAVE,
				iconCls : Constants.CLS_SAVE,
				handler : btnSaveHandler
			});
    // 流水号materialId
    var hdnMaterialId = {
		id : "materialId",
		xtype : "hidden",
		name : "materialBeen.materialId"
	}
	var lblMNo = new Ext.form.Label({
				x : 65,//71
				y : 10,
				width : 90,
				html : "编码"
			})
	var txtMNo = new Ext.form.TextField({
				x : 108,
				y : 5,
				tabIndex : 4,
				width : Constants.THEWIDTH,//180,
				style :{
                    'ime-mode' : 'disabled'
                },
                codeField : "yes",
//                maskRe : /^[A-Za-z0-9]+$/,
//                validator : Powererp.util.validateCode,
				maxLength : 30,
				isFormField : true,
			//	allowBlank : false,
				readOnly:true,
				id : "materialNo",
				name : "materialBeen.materialNo"
			})
	var lblMName = new Ext.form.Label({
				x : 517,
				y : 10,
				width : 140,
				html : "名称<font color='red'>*</font>:"
			})
	var txtMName = new Ext.form.TextField({
				x : 560,
				y : 5,
				tabIndex : 5,
				width : Constants.THEWIDTH,
				maxLength : 50,
				isFormField : true,
				allowBlank : false,
				id : "materialName",
				name : "materialBeen.materialName"
			})
	var lblDescription = new Ext.form.Label({
				x : 71,
				y : 37,
				width : 90,
				text : '描述:'				
			})
	var txaDescription = new Ext.form.TextArea({
				x : 108,
				y : 32,
				tabIndex : 6,
				width : 630,
				maxLength : 100,//200
				height : 60,
				isFormField : true,
				id : "materialDesc",
				name : "materialBeen.materialDesc"
			})
	var lblIsBat = new Ext.form.Label({
				x : 35,
				y : 98,
				width : 90,
				text : '是否批控制:'
			})
	var chkIsBat = new Ext.form.Checkbox({
				x : 108,
				y : 97,
				tabIndex : 7,
              //  id : 'isLot',
                isFormField:true,
                name : "materialBeen.isLot",
                listeners : {
            		check : function(){
            			if(this.getValue()){
            				cbxPriceMethod.setValue("2");
            			}else{
            				if(cbxPriceMethod.getValue() == "2")
            				    cbxPriceMethod.setValue("");
            			}
            		}
                },
                inputValue : Constants.CHECKED_VALUE
			})
	var lblMClassification = new Ext.form.Label({
				x : 41,
				y : 129,
				width : 90,
				html : "物料分类<font color='red'>*</font>:"
			})

	var cbxMClassificationHid = new Ext.form.Hidden({
    	hidden : false,
		id : "cbxMClassificationHid",
		name : 'maertialClassIdStr'
	});

	// 物料分类
    var cbxMClassification = new Ext.ux.ComboBoxTree({
               // fieldLabel : '物料类别',
    	        x : 108,
    	        y : 124,
    	        tabIndex : 8,
    	        id : 'cbxMClassification',
                allowBlank : false,
                width : Constants.THEWIDTH,
                displayField : 'text',
                valueField : 'id',
                blankText : '',
                emptyText : '',
                readOnly : true,
                style : "border-bottom:solid 2px",
                listeners : {
                        blur : function(){
	                        this.tree.root.collapse(true,false);
	                    }
                    },
                tree : //materialTypeTree,
                	{
                    xtype : 'treepanel',
                    // 虚拟节点,不能显示
                    rootVisible : false,

                    loader : new Ext.tree.TreeLoader({
                                dataUrl : 'resource/getMaterialClass.action'
                            }),
                    root : new Ext.tree.AsyncTreeNode({
                                id : '-1',
                                name : '合肥电厂',
                                text : '合肥电厂'
                            }),
                    listeners : {
                        click : treeClick
                    }
                },
                selectNodeModel : 'all'
            });
    
	var lblDefaultWHouse = new Ext.form.Label({
				x : 499,
				y : 129,
				width : 140,
				html : "缺省仓库<font color='red'>*</font>:"
			})
     // 缺省仓库 modify by drdu 090619
	var cbxDefaultWHouse = new Ext.form.TextField({
		id : 'defaultWhsNo',
		x : 560,
		y : 124,
		tabIndex : 9,
		width : Constants.THEWIDTH,
		maxLength : 50,
		readOnly : true,
		name : 'defaultWhsNo'

	});
	var cboDelayStoreHid = new Ext.form.Hidden({
		hidden : false,
		id : "cboDelayStoreHid",
		name : 'materialBeen.defaultWhsNo'
	});
	
	//根据物资类别ID取仓库名称 add by drdu 090624
	function getWhs() {
		Ext.Ajax.request({
			method : 'post',
			url : 'resource/getParentCode.action',
			params : {
				classId : cbxMClassificationHid.getValue()
			},
			method : 'post',
			success : function(result, request) {
				var obj = result.responseText;
				var mytext = obj.substring(1, obj.length - 1);

				var arr = mytext.split(',');

				Ext.get("defaultWhsNo").dom.value = arr[1];

				cboDelayStoreHid.setValue(arr[0]);
			}
		});

	}
//	var cbxDefaultWHouse = new Ext.form.ComboBox({
//				x : 560,
//				y : 124,
//				tabIndex : 9,
//				width : Constants.THEWIDTH,
//				id : 'cbxDefaultWHouse',
//				isFormField : true,
//				allowBlank : false,
//				style : "border-bottom:solid 2px",
//                triggerAction : 'all',
//                store : whsNameStore,
//				blankText : '',
//                emptyText : '',
//                displayField : 'whsName',
//                valueField : 'whsNo',
//                mode : 'local',
//                readOnly : true,
//                hiddenName : "materialBeen.defaultWhsNo"
////                listeners : {
////                    select : defaultWHouseSelected  //modify by fyyang 090617
////                }
//			})
	var lblMType = new Ext.form.Label({
				x : 47,
				y : 156,
				width : 90,
				text : '物料类型:'
			})
	var cbxMType = new Ext.form.ComboBox({
				x : 108,
				y : 151,
				tabIndex : 10,
				width : Constants.THEWIDTH,
				id : 'cbxMType',
				allowBlank : true,
				style : "border-bottom:solid 2px",
                triggerAction : 'all',
                store : typeNameStore,
				blankText : '',
                emptyText : '',
                displayField : 'typeName',
                valueField : 'materialTypeId',
                mode : 'local',
                readOnly : true,
                hiddenName : "materialTypeIdStr"
			})
	var lblDefaultWHousePostion = new Ext.form.Label({
				x : 499,
				y : 156,
				width : 140,
				text : '缺省库位:'
			})
	var cbxDefaultWHousePostion = new Ext.form.ComboBox({
				x : 560,
				y : 151,
				tabIndex : 11,
				width : Constants.THEWIDTH,
				id : 'cbxDefaultWHousePostion',
				allowBlank : true,
				style : "border-bottom:solid 2px",
                triggerAction : 'all',
                store : locationNameStore,
				blankText : '',
                emptyText : '',
                displayField : 'locationName',
                valueField : 'locationNo',
                mode : 'local',
                readOnly : true,
             //   disabled : true,
                hiddenName : "materialBeen.defaultLocationNo"
			})
	var lblMStatus = new Ext.form.Label({
				x : 47,
				y : 183,
				width : 90,
				text : '物料状态:'
			})
	var cbxMStatus = new Ext.form.ComboBox({
				x : 108,
				y : 178,
				tabIndex : 12,
				width : Constants.THEWIDTH,
				id : 'cbxMStatus',
				allowBlank : true,
				style : "border-bottom:solid 2px",
                triggerAction : 'all',
                store : statusNameStore,
				blankText : '',
                emptyText : '',
                displayField : 'statusName',
                valueField : 'materialStatusId',
                mode : 'local',
                readOnly : true,
                hiddenName : "materialStatusIdStr"
			})
	var lblABCClassification = new Ext.form.Label({
				x : 503,
				y : 183,
				width : 140,
				text : 'ABC分类:'
			})
    var ABCStore = new Ext.data.SimpleStore({
				data : [['', ''], ['A', 'A'], ['B', 'B'], ['C', 'C']],
				fields : ['value', 'text']
			})
	var costMethodStore = new Ext.data.SimpleStore({
			    fields : ['value', 'text'],
                data : [['', ''], ['1', '移动加权平均'], ['2', '先进先出'],
                            ['3', '计划价格']]
	        })	
	var cbxABCClassification = new Ext.form.ComboBox({
				x : 560,
				y : 178,
				tabIndex : 13,
				width : Constants.THEWIDTH,
				store : ABCStore,
	            valueField : 'value',
                displayField : 'text',
				id : 'cbxABCClassification',
				allowBlank : true,
				style : "border-bottom:solid 2px",
				blankText : '',
                emptyText : '',
				triggerAction : 'all',
                mode : 'local',
                readOnly : true,
				hiddenName : 'materialBeen.abcType'
			})
	cbxABCClassification.setValue("");
	var lblIsLeft = new Ext.form.Label({
				x : 47,
				y : 6,
				width : 90,
				text : '是否预留:'
			})
	var chkIsLeft = new Ext.form.Checkbox({
				x : 108,
				y : 5,
				tabIndex : 14,
		    //  id : 'isReserved',
                isFormField:true,
                name : "materialBeen.isReserved",
                inputValue : Constants.CHECKED_VALUE
			})
	var lblIsGoods = new Ext.form.Label({
				x : 197,
				y : 6,
				width : 140,
				text : '是否消费品:'
			})
	var chkIsGoods = new Ext.form.Checkbox({
				x : 270,
				y : 5,
				tabIndex : 15,
		    //  id : 'isCommodity',
                isFormField:true,
                name : "materialBeen.isCommodity",
                inputValue : Constants.CHECKED_VALUE
			})
	var lblPlainPrice = new Ext.form.Label({
				x : 47,
				y : 37,
				width : 90,
				text : '计划价格:'
			})

    var txtPlainPrice = new Powererp.form.NumField({
        x : 108,
        y : 32,
        tabIndex : 16,
        allowNegative : false,
        width : Constants.THEWIDTH,
        maxValue : Constants.MAXNUM18_4,
        minValue : 0,
        id : 'frozenCost',
        name : 'frozenCost',
        hiddenName : 'materialBeen.frozenCost',
        decimalPrecision : 4,
        padding : 4
    })
    
	var lblStockUnit = new Ext.form.Label({
				x : 469,
				y : 37,
				width : 140,
				html : "存货计量单位<font color='red'>*</font>:"
			})
	var cbxStockUnit = new Ext.form.ComboBox({
				x : 560,
				y : 32,
				tabIndex : 17,
				width : Constants.THEWIDTH,
				maxLength : 10,
				//isFormField : true,
				//readOnly : true,
				id : 'cbxStockUnit',
                allowBlank : false,
                style : "border-bottom:solid 2px",
				triggerAction : 'all',
				store : allUnitStore,
				blankText : '',
                emptyText : '',
	            valueField : 'unitId',
                displayField : 'unitName',
                mode : 'local',
				hiddenName : 'stockUmIdStr',
				listeners : {
	            	render : function() {
	            	    this.clearInvalid();
	            	}
	            }				
			})
	
	var lblPriceMethod = new Ext.form.Label({
				x : 47,
				y : 64,
				width : 90,
				text : '计价方式:'
			})
	var cbxPriceMethod = new Ext.form.ComboBox({
				x : 108,
				y : 59,
				tabIndex : 18,
				width : Constants.THEWIDTH,
				maxLength : 10,
				store : costMethodStore,
				blankText : '',
                emptyText : '',
	            valueField : 'value',
                displayField : 'text',
				id : 'cbxPriceMethod',
				allowBlank : true,
				style : "border-bottom:solid 2px",
				triggerAction : 'all',
                mode : 'local',
                readOnly : true,
                listeners : {
            		select : function(){
            			if(this.getValue() == "2"){
            				chkIsBat.setValue(true);
            			}else{
            				chkIsBat.setValue(false);
            			}
            		}
                },
				hiddenName : 'materialBeen.costMethod'
			})
	cbxPriceMethod.setValue("");
	var lblConversionRate1 = new Ext.form.Label({
				x : 412,
				y : 64,
				width : 140,
				text : '采购/存货计量单位转换比:'
			})

    var txtConversionRate1 = new Powererp.form.NumField({
    	x : 560,
		y : 59,
		tabIndex : 19,
        allowNegative : false,
        width : Constants.THEWIDTH,
        maxValue : Constants.MAXNUM15_2,
        minValue : 0,
        value : 1.00,
        id : 'purStockUm',
        name : 'purStockUm',
        hiddenName : 'materialBeen.purStockUm',
        decimalPrecision : 2,
        padding : 2,
        listeners : {
            blur : function() {
                if(!this.getValue()){
                	this.setValue(1);
                }
            }
        }
    })
    
	var lblFOBPrice = new Ext.form.Label({
				x : 51,
				y : 91,
				width : 90,
				text : 'FOB价格:'
			})

    var txtFOBPrice = new Powererp.form.NumField({
    	x : 108,
		y : 86,
		tabIndex : 20,
        allowNegative : false,
        width : Constants.THEWIDTH,
        maxValue : Constants.MAXNUM18_4,
        minValue : 0,
        id : 'fobPrice',
        name : 'fobPrice',
        hiddenName : 'materialBeen.fobPrice',
        decimalPrecision : 4,
        padding : 4
    })
    
	var lblBuyUnit = new Ext.form.Label({
				x : 469,
				y : 91,
				width : 140,
				html: "采购计量单位<font color='red'>*</font>:"
			})
	var cbxBuyUnit = new Ext.form.ComboBox({
				x : 560,
				y : 86,
				tabIndex : 21,
				width : Constants.THEWIDTH,
				maxLength : 10,				
				isFormField : true,
				//readOnly : true,
				id : 'cbxBuyUnit',
                allowBlank : false,
                style : "border-bottom:solid 2px",
				triggerAction : 'all',
				store : allUnitStore,
				blankText : '',
                emptyText : '',
	            valueField : 'unitId',
                displayField : 'unitName',
                mode : 'local',
				hiddenName : 'purUmIdStr',
				listeners : {
	            	render : function() {
	            	   this.clearInvalid();
	            	}
	            }
			})
	var lblTax = new Ext.form.Label({
				x : 71,
				y : 118,
				width : 90,
				text : '税率:'
			})

    var txtTax = new Powererp.form.NumField({
    	x : 108,
		y : 113,
		tabIndex : 22,
        allowNegative : false,
        width : Constants.THEWIDTH,
        maxValue : 99999999999.9999,
        minValue : 0,
        id : 'taxRate',
        name : 'taxRate',
        hiddenName : 'materialBeen.taxRate',
        decimalPrecision : 4,
        padding : 4
    })
    
	var lblConversionRate2 = new Ext.form.Label({
				x : 412,
				y : 118,
				width : 140,
				text : '销售/存货计量单位转换比:'
			})

    var txtConversionRate2 = new Powererp.form.NumField({
    	x : 560,
		y : 113,
		tabIndex : 23,
        allowNegative : false,
        width : Constants.THEWIDTH,
        maxValue : Constants.MAXNUM15_2,
        minValue : 0,
        value : 1.00,
        id : 'saleStockUm',
        name : 'saleStockUm',
        hiddenName : 'materialBeen.saleStockUm',
        decimalPrecision : 2,
        padding : 2
    })
    
	var lblDays = new Ext.form.Label({
				x : 35,
				y : 145,
				width : 90,
				text : '提前期天数:'
			})

    var txtDays = new Powererp.form.NumField({
    	allowDecimals : false,
        x : 108,
		y : 140,
		tabIndex : 24,
        allowNegative : false,
        width : Constants.THEWIDTH,
        maxValue : Constants.MAXNUM15_0,
        minValue : 0,
        id : 'leadDays',
        name : 'leadDays',
        hiddenName : 'materialBeen.leadDays'
    })
    
	var lblSaleUnit = new Ext.form.Label({
				x : 475,
				y : 145,
				width : 140,
				text : '销售计量单位:'
			})
	var cbxSaleUnit = new Ext.form.ComboBox({
				x : 560,
				y : 140,
				tabIndex : 25,
				width : Constants.THEWIDTH,
				maxLength : 10,				
				isFormField : true,
				readOnly : true,
				id : 'cbxSaleUnit',
                allowBlank : true,
                style : "border-bottom:solid 2px",
				triggerAction : 'all',
				store : allUnitStore,
				blankText : '',
                emptyText : '',
	            valueField : 'unitId',
                displayField : 'unitName',
                mode : 'local',
				hiddenName : 'saleUmIdStr'
			})
	var lblMainSupply = new Ext.form.Label({
				x : 35,
				y : 172,
				width : 90,
				text : '主要供应商:'
			})
	var txtMainSupply = new Ext.form.TextField({
				x : 108,
				y : 167,
				tabIndex : 26,
				width : Constants.THEWIDTH,
				maxLength : 50,				
				isFormField : true,
				readOnly : true,
				id : 'txtMainSupply'
			})
	// 主要供应商流水号
    var hdnPrimarySupplierId = {
		id : "primarySupplier",
		xtype : "hidden",
		name : "primarySupplierIdStr"
	}
	// 注册单击事件
	txtMainSupply.onClick(addPrimarySupplier);
	var lblMaxStock = new Ext.form.Label({
				x : 487,
				y : 172,
				width : 140,
				text : '最大库存量:'
			})

    var txtMaxStock = new Powererp.form.NumField({
    	x : 560,
		y : 167,
		tabIndex : 27,
        allowNegative : false,
        width : Constants.THEWIDTH,
        maxValue : Constants.MAXNUM15_2,
        minValue : 0,
        id : 'maxStock',
        name : 'maxStock',
        hiddenName : 'materialBeen.maxStock',
        decimalPrecision : 2,
        padding : 2
    })
    
	var lblNextSupply = new Ext.form.Label({
				x : 35,
				y : 199,
				width : 90,
				text : '次要供应商:'
			})
	var txtNextSupply = new Ext.form.TextField({
				x : 108,
				y : 194,
				tabIndex : 28,
				width : Constants.THEWIDTH,
				maxLength : 50,
				isFormField : true,
				id : 'txtNextSupply'
			})
	// 次要供应商流水号
    var hdnSecondarySupplierId = {
		id : "secondarySupplier",
		xtype : "hidden",
		name : "secondarySupplierIdStr"
	}
	// 注册单击事件
	txtNextSupply.onClick(addSecondarySupplier);
	var lblMinStock = new Ext.form.Label({
				x : 487,
				y : 199,
				width : 140,
				text : '最小库存量:'
			})

    var txtMinStock = new Powererp.form.NumField({
    	x : 560,
		y : 194,
		tabIndex : 29,
        allowNegative : false,
        width : Constants.THEWIDTH,
        maxValue : Constants.MAXNUM15_2,
        minValue : 0,
        id : 'minStock',
        name : 'minStock',
        hiddenName : 'materialBeen.minStock',
        decimalPrecision : 2,
        padding : 2
    })
    
	var lblLength = new Ext.form.Label({
				x : 71,
				y : 6,
				width : 90,
				text : '长度:'
			})

    var txtLength = new Powererp.form.NumField({
    	x : 108,
		y : 5,
		tabIndex : 30,
        allowNegative : false,
        width : Constants.THEWIDTH,
        maxValue : Constants.MAXNUM15_2,
        minValue : 0,
        id : 'len',
        name : 'len',
        hiddenName : 'materialBeen.len',
        decimalPrecision : 2,
        padding : 2
    })
    
	var lblLengthUnit = new Ext.form.Label({
				x : 475,
				y : 6,
				width : 140,
				text : '长度计量单位:'
			})
	var cbxLengthUnit = new Ext.form.ComboBox({
				x : 560,
				y : 5,
				tabIndex : 31,
				width : Constants.THEWIDTH,
				maxLength : 10,
				isFormField : true,
				id : "cbxLengthUnit",
                allowBlank : true,
                style : "border-bottom:solid 2px",
                readOnly : true,
				triggerAction : 'all',
				store : allUnitStore,
				blankText : '',
                emptyText : '',
	            valueField : 'unitId',
                displayField : 'unitName',
                mode : 'local',
				hiddenName : 'lengthUmIdStr'
			})
	var lblWidth = new Ext.form.Label({
				x : 71,
				y : 37,
				width : 90,
				text : '宽度:'
			})

    var txtWidth = new Powererp.form.NumField({
        x : 108,
        y : 32,
        tabIndex : 32,
        allowNegative : false,
        width : Constants.THEWIDTH,
        maxValue : Constants.MAXNUM15_2,
        minValue : 0,
        id : 'width',
        name : 'width',
        hiddenName : 'materialBeen.width',
        decimalPrecision : 2,
        padding : 2
    })
    
	var lblWidthUnit = new Ext.form.Label({
				x : 475,
				y : 37,
				width : 140,
				text : '宽度计量单位:'
			})
	var cbxWidthUnit = new Ext.form.ComboBox({
				x : 560,
				y : 32,
				tabIndex : 33,
				width : Constants.THEWIDTH,
				maxLength : 10,
				isFormField : true,
				id : "cbxWidthUnit",
                allowBlank : true,
                style : "border-bottom:solid 2px",
                readOnly : true,
				triggerAction : 'all',
				store : allUnitStore,
				blankText : '',
                emptyText : '',
	            valueField : 'unitId',
                displayField : 'unitName',
                mode : 'local',
				hiddenName : 'widthUmIdStr'
			})
	var lblHeight = new Ext.form.Label({
				x : 71,
				y : 64,
				width : 90,
				text : '高度:'
			})

    var txtHeight = new Powererp.form.NumField({
    	x : 108,
		y : 59,
		tabIndex : 34,
        allowNegative : false,
        width : Constants.THEWIDTH,
        maxValue : Constants.MAXNUM15_2,
        minValue : 0,
        id : 'heigh',
        name : 'heigh',
        hiddenName : 'materialBeen.heigh',
        decimalPrecision : 2,
        padding : 2
    })
    
	var lblHeightUnit = new Ext.form.Label({
				x : 475,
				y : 64,
				width : 140,
				text : '高度计量单位:'
			})
	var cbxHeightUnit = new Ext.form.ComboBox({
				x : 560,
				y : 59,
				tabIndex : 35,
				width : Constants.THEWIDTH,
				maxLength : 10,
				isFormField : true,
				id : "cbxHeightUnit",
                allowBlank : true,
                style : "border-bottom:solid 2px",
                readOnly : true,
				triggerAction : 'all',
				store : allUnitStore,
				blankText : '',
                emptyText : '',
	            valueField : 'unitId',
                displayField : 'unitName',
                mode : 'local',
				hiddenName : 'heighUmIdStr'
			})
	var lblWeight = new Ext.form.Label({
				x : 47,
				y : 91,
				width : 90,
				text : '单位重量:'
			})

    var txtWeight = new Powererp.form.NumField({
    	x : 108,
		y : 86,
		tabIndex : 36,
        allowNegative : false,
        width : Constants.THEWIDTH,
        maxValue : Constants.MAXNUM15_2,
        minValue : 0,
        id : 'weight',
        name : 'weight',
        hiddenName : 'materialBeen.weight',
        decimalPrecision : 2,
        padding : 2
    })
    
	var lblWeightUnit = new Ext.form.Label({
				x : 475,
				y : 91,
				width : 140,
				text : '重量计量单位:'
			})
	var cbxWeightUnit = new Ext.form.ComboBox({
				x : 560,
				y : 86,
				tabIndex : 37,
				width : Constants.THEWIDTH,
				maxLength : 10,
				isFormField : true,
				id : "cbxWeightUnit",
                allowBlank : true,
                style : "border-bottom:solid 2px",
                readOnly : true,
				triggerAction : 'all',
				store : allUnitStore,
				blankText : '',
                emptyText : '',
	            valueField : 'unitId',
                displayField : 'unitName',
                mode : 'local',
				hiddenName : 'wightUmIdStr'
			})
	var lblVolume = new Ext.form.Label({
				x : 47,
				y : 118,
				width : 90,
				text : '单位体积:'
			})

    var txtVolume = new Powererp.form.NumField({
    	x : 108,
		y : 113,
		tabIndex : 38,
        allowNegative : false,
        width : Constants.THEWIDTH,
        maxValue : Constants.MAXNUM15_2,
        minValue : 0,
        id : 'volumUnit',
        name : 'volumUnit',
        hiddenName : 'materialBeen.volumUnit',
        decimalPrecision : 2,
        padding : 2
    })
    
	var lblVolumeUnit = new Ext.form.Label({
				x : 475,
				y : 118,
				width : 140,
				text : '体积计量单位:'
			})
	var cbxVolumeUnit = new Ext.form.ComboBox({
				x : 560,
				y : 113,
				tabIndex : 39,
				width : Constants.THEWIDTH,
				height : 100,
				maxLength : 10,
				isFormField : true,
				id : "cbxVolumeUnit",
                allowBlank : true,
                style : "border-bottom:solid 2px",
                readOnly : true,
				triggerAction : 'all',
				store : allUnitStore,
				blankText : '',
                emptyText : '',
	            valueField : 'unitId',
                displayField : 'unitName',
                mode : 'local',
				hiddenName : 'volumUmIdStr'
			})
	var lblIsDangrous = new Ext.form.Label({
				x : 35,
				y : 141,
				width : 90,
				text : '是否危险品:'
			})
	var chkIsDangrous = new Ext.form.Checkbox({
				x : 108,
				y : 140,
				tabIndex : 40,
		    //  id : 'isDanger',
                isFormField:true,
                name : "materialBeen.isDanger",
                inputValue : Constants.CHECKED_VALUE
			})
	var lblIsExemption = new Ext.form.Label({
				x : 209,
				y : 141,
				width : 140,
				text : '是否免检:'
			})
	var chkIsExemption = new Ext.form.Checkbox({
				x : 270,
				y : 140,
				tabIndex : 41,
		    //  id : 'qaControlFlag',
                isFormField:true,
                checked :true,
                name : "materialBeen.qaControlFlag",
                inputValue : Constants.CHECKED_VALUE
			})
	var lblType = new Ext.form.Label({
				id : 'lblType',
				x : 47,
				y : 172,
				width : 90,
				html : '规格型号<font color="red">*</font>:'
			})
	var txtType = new Ext.form.TextField({
				x : 108,
				y : 167,
				tabIndex : 42,
				width : Constants.THEWIDTH,
				maxLength : 100,
				allowBanlk : false,
				isFormField : true,
				id : "specNo",
				name : "materialBeen.specNo"
			})
	var lblFireD = new Ext.form.Label({
				x : 499,
				y : 172,
				width : 140,
				text : '燃点摄氏:'
			})

    var txtFireD = new Powererp.form.NumField({
    	x : 560,
		y : 167,
		tabIndex : 43,
        allowNegative : false,
        width : Constants.THEWIDTH,
        maxValue : Constants.MAXNUM15_2,
        minValue : 0,
        id : 'centigrade',
        name : 'centigrade',
        hiddenName : 'materialBeen.centigrade',
        decimalPrecision : 2,
        padding : 2
    })
    
	var lblDocNo = new Ext.form.Label({
				x : 59,
				y : 199,
				width : 90,
				text : '文档号:'
			})
	var txtDocNo = new Ext.form.TextField({
				x : 108,
				y : 194,
				tabIndex : 44,
				width : Constants.THEWIDTH,
				maxLength : 25,
				isFormField : true,
				id : "docNo",
				name : "materialBeen.docNo"
			})
	var lblQualityLevel = new Ext.form.Label({
				x : 499,
				y : 199,
				width : 140,
				text : '质量等级:'
			})
	var txtQualityLevel = new Ext.form.TextField({
				x : 560,
				y : 194,
				tabIndex : 45,
				width : Constants.THEWIDTH,
				maxLength : 5,
				isFormField : true,
				id : "qualityClass",
				name : "materialBeen.qualityClass"
			})
	var lblMAndParm = new Ext.form.Label({
				x : 44,
				y : 226,
				width : 90,
				text : '材质/参数:'
			})
	var txtMAndParm = new Ext.form.TextField({
				x : 108,
				y : 221,
				tabIndex : 46,
				width : Constants.THEWIDTH,
				maxLength : 50,
				isFormField : true,
				id : "parameter",
				name : "materialBeen.parameter"
			})
	var lblStoreTime = new Ext.form.Label({
				x : 499,
				y : 226,
				width : 140,
				text : '储存年限:'
			})

    var txtStoreTime = new Powererp.form.NumField({
    	allowDecimals : false,
        x : 560,
		y : 221,
		tabIndex : 47,
        allowNegative : false,
        width : Constants.THEWIDTH,
        maxValue : Constants.MAXNUM15_0,
        minValue : 0,
        value : 0,
        id : 'stockYears',
        name : 'stockYears',
        hiddenName : 'materialBeen.stockYears'
    })
    
	var lblMadeBy = new Ext.form.Label({
				x : 47,
				y : 253,
				width : 90,
				text : '生产厂家:'
			})
	var txtMadeBy = new Ext.form.TextField({
				x : 108,
				y : 248,
				tabIndex : 48,
				width : Constants.THEWIDTH,
				maxLength : 50,
				isFormField : true,
				id : "factory",
				name : "materialBeen.factory"
			})
	var lblWaitDays = new Ext.form.Label({
				x : 499,
				y : 253,
				width : 140,
				text : '待验天数:'
			})
    var txtWaitDays = new Powererp.form.NumField({
    	allowDecimals : false,
        x : 560,
        y : 248,
        tabIndex : 49,
        allowNegative : false,
        width : Constants.THEWIDTH,
        maxValue : Constants.MAXNUM15_0,
        minValue : 0,
        id : 'checkDays',
        name : 'checkDays',
        hiddenName : 'materialBeen.checkDays',
        decimalPrecision : 0,
        padding : 0
    })
    
    // 定义/描述
	var infoSet1 = new Ext.form.FieldSet({
				x : '0',
				y : 5,
				title : '定义/描述',
				border : true,
				layout : 'absolute',
				height : 230,
				width : 775,
				items : [hdnMaterialId,lblMNo, txtMNo, lblMName, txtMName, lblDescription,
						txaDescription, lblIsBat, chkIsBat, lblMClassification,
						cbxMClassification, cbxMClassificationHid, lblDefaultWHouse,cbxDefaultWHouse,cboDelayStoreHid,
						lblMType, cbxMType, lblDefaultWHousePostion,
						cbxDefaultWHousePostion, lblMStatus, cbxMStatus,
						lblABCClassification, cbxABCClassification]
			});

	// 需求/采购
	var infoSet2 = new Ext.form.FieldSet({
				x : '0',
				y : 255,
				title : '需求/采购',
				border : true,
				layout : 'absolute',
				height : 250,
				width : 775,
				//anchor : '95%',
				items : [lblIsLeft, chkIsLeft, lblIsGoods, chkIsGoods,
						lblPlainPrice, txtPlainPrice, lblStockUnit,
						cbxStockUnit, lblPriceMethod, cbxPriceMethod,
						lblConversionRate1, txtConversionRate1, lblFOBPrice,
						txtFOBPrice, lblBuyUnit, cbxBuyUnit, lblTax, txtTax,
						lblConversionRate2, txtConversionRate2, lblDays,
						txtDays, lblSaleUnit, cbxSaleUnit, lblMainSupply,
						txtMainSupply, hdnPrimarySupplierId, lblMaxStock, txtMaxStock, lblNextSupply,
						txtNextSupply, hdnSecondarySupplierId, lblMinStock, txtMinStock]
			});

	// 规格/质量
	var infoSet3 = new Ext.form.FieldSet({
				x : '0',
				y : 525,
				title : '规格/质量',
				border : true,
				layout : 'absolute',
				height : 310,
				width : 775,
				//anchor : '95%',//100
				items : [lblLength, txtLength, lblLengthUnit, cbxLengthUnit,
						lblWidth, txtWidth, lblWidthUnit, cbxWidthUnit,
						lblHeight, txtHeight, lblHeightUnit, cbxHeightUnit,
						lblWeight, txtWeight, lblWeightUnit, cbxWeightUnit,
						lblVolume, txtVolume, lblVolumeUnit, cbxVolumeUnit,
						lblIsDangrous, chkIsDangrous, lblIsExemption,
						chkIsExemption, lblType, txtType, lblFireD, txtFireD,
						lblDocNo, txtDocNo, lblQualityLevel, txtQualityLevel,
						lblMAndParm, txtMAndParm, lblStoreTime, txtStoreTime,
						lblMadeBy, txtMadeBy, lblWaitDays, txtWaitDays]
			});

	// 表单panel
	var formRegister = new Ext.form.FormPanel({
				labelAlign : 'right',
				border : true,
				autoScroll : true,
				frame : true,
				layout : 'absolute',
				items : [infoSet1, infoSet2, infoSet3]
			});

	// 登记tab
	var registerPanel = new Ext.Panel({
		        tbar : [btnAdd, '-', btnDelete, '-', btnSave],
				layout : 'fit',
				title : '登记',
				height : 800,
				frame : false,
				border : false,
				items : [formRegister]
			});
	// ↑↑*******************物料基础资料登记tab**************************************
	// tabPanel
	var tabPanel = new Ext.TabPanel({
		        layoutOnTabChange:true,//
				activeTab : 0,
				tabPosition : 'bottom',
				items : [queryPanel, registerPanel]

			});

	// 显示区域
	var layout = new Ext.Viewport({
				layout : 'fit',
				margins : '0 0 0 0',
				region : 'center',
				border : true,
				items : [tabPanel]
			});
	// ↓↓*******************************处理****************************************
    // 保存登记画面初始数据
	var originalData = null
    
    
    /**
     * 双击查询grid记录，进入登记tab
     */
    function gotoRegisterTab(){
        // 选择的记录
        var record = materialGrid.getSelectionModel().getSelected(); 
        // 转到登记tab
        tabPanel.setActiveTab(1);
        // 删除按钮可用
		btnDelete.setDisabled(false);
		// 判断是否有未保存数据
		var isNewRecord = false;//isFormChange();
		if (isNewRecord) {
			// 有未保存数据
			// 是否放弃已修改的信息(否：处理终了)
			Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_004,
					function(button) {
						if (button == "yes") {
						    // 放弃
							// 给登记tab的form设值(根据id从后台检索)
			                setQueryValue(record.get('materialId'));
						}
					});
		} else {
		    // 没有未保存数据
			// 给登记tab的form设值(根据id从后台检索)
			setQueryValue(record.get('materialId'));
		}
    }
    
    /**
     * 给登记tab的form设值(根据id从后台检索)并保存下当前数据用来判断数据是否被修改
     * 删除按钮可用
     * @param theId 查询页面的流水号
     */
    function setQueryValue(theId) {
    	// 初始化
    	initRegister();
        // 根据id从后台检索物料基础资料信息
		var url = "resource/getMaterialById.action?materialId="	+ theId;
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("POST", url, false);
		conn.send(null);
		var mydata = eval('('+conn.responseText+')');
		
		//↓↓************ComboBox***************
		// 物料分类
		var index = -1;
		index = classNameStore.find("maertialClassId",mydata.maertialClassId);
		if(index > -1){
			// 通过id取出name，然后设值
			var textName = classNameStore.getAt(index).get('className');
			var obj = new Object();
			obj.text = textName;
			obj.id = "";
			cbxMClassification.setValue(obj);
			// 设定隐藏流水号的值
			cbxMClassificationHid.setValue(mydata.maertialClassId);
		}
		// 物料类型
		if(typeNameStore.find("materialTypeId",mydata.materialTypeId) > -1){
			cbxMType.setValue(mydata.materialTypeId);
	    }
		// 物料状态
	    if(statusNameStore.find("materialStatusId", mydata.materialStatusId) > -1){
		    cbxMStatus.setValue(mydata.materialStatusId);
		}
		// 计价方式
		if(costMethodStore.find("value", mydata.costMethod) > -1){
			cbxPriceMethod.setValue(mydata.costMethod);
		}
		// ABC分类
		if(ABCStore.find("value", mydata.abcType) > -1){
			cbxABCClassification.setValue(mydata.abcType);
		}
		// 计量单位（8个） stockUmIdStr purUmIdStr saleUmIdStr 
		// lengthUmIdStr widthUmIdStr heighUmIdStr wightUmIdStr volumUmIdStr;
		if(allUnitStore.find("unitId", mydata.stockUmId) > -1){
			cbxStockUnit.setValue(mydata.stockUmId);
		}
		if(allUnitStore.find("unitId", mydata.purUmId) > -1){
			cbxBuyUnit.setValue(mydata.purUmId);
		}
		if(allUnitStore.find("unitId", mydata.saleUmId) > -1){
			cbxSaleUnit.setValue(mydata.saleUmId);
		}
		if(allUnitStore.find("unitId", mydata.lengthUmId) > -1){
			cbxLengthUnit.setValue(mydata.lengthUmId);
		}
		if(allUnitStore.find("unitId", mydata.widthUmId) > -1){
			cbxWidthUnit.setValue(mydata.widthUmId);
		}
		if(allUnitStore.find("unitId", mydata.heighUmId) > -1){
			cbxHeightUnit.setValue(mydata.heighUmId);
		}
		if(allUnitStore.find("unitId", mydata.wightUmId) > -1){
			cbxWeightUnit.setValue(mydata.wightUmId);
		}
		if(allUnitStore.find("unitId", mydata.volumUmId) > -1){
			cbxVolumeUnit.setValue(mydata.volumUmId);
		}
		//↑↑************ComboBox***************
		
		// 主次供应商
		if(mydata.primarySupplier != null){
			var primarySupplierName = getSupplyNameById(mydata.primarySupplier);
			Ext.get("primarySupplier").dom.value = mydata.primarySupplier
			txtMainSupply.setValue(primarySupplierName);
			if(mydata.secondarySupplier != null){
				var secondarySupplierName = getSupplyNameById(mydata.secondarySupplier);
				Ext.get("secondarySupplier").dom.value = mydata.secondarySupplier
				txtNextSupply.setValue(secondarySupplierName);
			}
        }

		//↓↓************Checkbox***************
		// 是否批控制
		if(mydata.isLot == Constants.CHECKED_VALUE){
			chkIsBat.setValue(true);
		}
		// 是否预留
		if(mydata.isReserved == Constants.CHECKED_VALUE){        
			chkIsLeft.setValue(true);
		}
		// 是否消费品
		if(mydata.isCommodity == Constants.CHECKED_VALUE){        
			chkIsGoods.setValue(true);
		}
		// 是否危险品
		if(mydata.isDanger == Constants.CHECKED_VALUE){        
			chkIsDangrous.setValue(true);
		}
		// 是否免检
		if(mydata.qaControlFlag == Constants.CHECKED_VALUE){        
			chkIsExemption.setValue(true);
		}
		//↑↑************Checkbox***************

		//-------modify by fyyang 
		// 库位disabled设置
		//cbxDefaultWHousePostion.setDisabled(true);
		
		// 缺省仓库 modify by drdu 090624
//		if(whsNameStore.find("whsNo", mydata.defaultWhsNo) > -1){
//		    cbxDefaultWHouse.setValue(mydata.defaultWhsNo);
		     // 加载库位store
		    locationNameStore.load({
//				params : {
//					whsNo : ""  //mydata.defaultWhsNo
//				},
				callback : function() {
		            cbxDefaultWHousePostion.setValue("");
					// 缺省库位 联动
					if(locationNameStore.find("locationNo",mydata.defaultLocationNo) > -1){
					    cbxDefaultWHousePostion.setValue(mydata.defaultLocationNo);
					  //  cbxDefaultWHousePostion.setDisabled(false);
					}
					// form 加载数据
				 	formRegister.getForm().loadRecord({
				 	    data : mydata
				 	});
					// 保存原始信息（用来判断数据是否被修改
					originalData = formRegister.getForm().getValues();
				}
			});
//		}else{
			// form 加载数据
			formRegister.getForm().loadRecord({
				data : mydata
			});
			// 保存原始信息（用来判断数据是否被修改
			originalData = formRegister.getForm().getValues();
		//}
		// 删除按钮可用
		btnDelete.setDisabled(false);
		
		getWhs(); //add by drdu 090624
    }
    /**
     * 增加物料基础资料  初始化
     */
    function btnAddHandler() {
		if (isFormChange()) {
		    // 放弃已修改的内容吗
			Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_004,
					function(button) {
						if (button == "yes") {
							initRegister();
						}
					});
		} else {
			// 画面初始化操作
			initRegister();
		}
    }
    /**
     * 删除物料基础资料
     */
    function deleteMaterial() {
		Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_002,
				function(button) {
					if (button == "yes") {
						Ext.Ajax.request({
									url : 'resource/deleteMaterialBaseInfo.action',
									method : 'POST',
									params : {
										materialId : originalData['materialBeen.materialId']//Ext.get("hdnMaterialId").dom.value
									},
									success : function(result, request) {
										var o = eval("(" + result.responseText + ")");
										
									    // 初始化登记tab 清空from
										if(o.flag=="1")
										{
										initRegister();
										Ext.Msg.alert(Constants.SYS_REMIND_MSG,
												Constants.COM_I_005)
									    // 刷新查询tab
										materialStore.load({
				                            params : {
					                            start : 0,
					                            limit : Constants.PAGE_SIZE
				                            }
			                            });
										}
										else
										{
											Ext.Msg.alert("提示",
												"此物资已经在使用，不能删除！")
										}
												
									},
									failure : function() {
										Ext.Msg.alert(Constants.SYS_REMIND_MSG,
												Constants.COM_E_014)
									}
								})
					}
				});
    }
    /**
     * 保存按钮事件
     */
    function btnSaveHandler() {
//    	// 判断画面是否修改
//    	if(!isFormChange()){
//    	    Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_006);
//    	    return;
//    	}
        // 是否要保存
		Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_001,
				function(buttonobj) {
					if (buttonobj == "yes") {
						if (!isformCanSave()){
							return;
						}
						// 画面控件是否红线
					    if (!formRegister.getForm().isValid()) {
							return;
						}
						// 保存
						saveMaterialInfo();
					}
				});
    }
    /**
     * 保存物料基础资料信息
     */
    function saveMaterialInfo() {
        // 表单提交
        formRegister.getForm().submit({
            url : 'resource/saveMaterialInfo.action',
            method : Constants.POST,
            success : function(form ,action){
            	var result = eval("(" + action.response.responseText +")");
                if(result.success){
                	if(result.flag == 1){
                		// 编码重复
                	    Ext.Msg.alert(Constants.SYS_REMIND_MSG, 
                	            String.format(Constants.COM_E_007, "编码"));
						return;
                	}
                    // 保存成功
                    Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_004);
                    // 刷新页面 显示保存后的信息 删除按钮可用
                    var id = result.theId;
                    setQueryValue(id);
		            // 刷新查询tab
					materialStore.load({
				        params : {
					        start : 0,
					        limit : Constants.PAGE_SIZE
				        }
			        });
            	}else{
            	    // 保存失败
            		Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_E_014);
            	}
            },
            failure : function(form ,action){
            	Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_E_014);
            }
        });
    }
    /**
     * 保存之前的check
     */
    function isformCanSave() {
        var nullMsg = "";
        // 清除编码、名称输入后的首尾空格
        txtMNo.setValue(txtMNo.getValue().trim());
        txtMName.setValue(txtMName.getValue().trim());
        // 编码、名称不可以为空
        //modify by fyyang 090521 编码不从前台输入，在后台按编码规则生成
//        if(!txtMNo.getValue()) {
//            nullMsg = nullMsg + String.format(Constants.COM_E_002,"编码") + "<br/>";
//        }
        if(Ext.get("materialBeen.defaultWhsNo").dom.value==null||Ext.get("materialBeen.defaultWhsNo").dom.value=="")
        {
          	 nullMsg = nullMsg + String.format(Constants.COM_E_002,"缺省仓库") + "<br/>";
        }
        if((!txtMName.getValue())){
            nullMsg = nullMsg + String.format(Constants.COM_E_002,"名称") + "<br/>";
        }
        // 物料分类必须选择
        var classId = cbxMClassificationHid.getValue();
        if(classId == null || classId == ""){
            nullMsg = nullMsg + String.format(Constants.COM_E_003,"物料分类") + "<br/>";
            // 加红线
            cbxMClassification.markInvalid();
        }
        // 存货计量单位必须选择
        var stockUnit = cbxStockUnit.getValue();
        if(stockUnit == null || stockUnit == ""){
            nullMsg = nullMsg + String.format(Constants.COM_E_003,"存货计量单位") + "<br/>";
            // 加红线
            cbxStockUnit.markInvalid();
        }
        // 采购计量单位必须选择
        var buyUnit = cbxBuyUnit.getValue();
        if(buyUnit == null || buyUnit == ""){
            nullMsg = nullMsg + String.format(Constants.COM_E_003,"采购计量单位") + "<br/>";
            // 加红线
            cbxBuyUnit.markInvalid();
        } 
        
        //add by bjxu 规格型号必须选择
		var lblTypeTemp = txtType.getValue();
		if(lblTypeTemp == null || lblTypeTemp == ""){
			nullMsg = nullMsg + String.format(Constants.COM_E_002,"规格型号");
            // 加红线
            txtType.markInvalid();
        } 
        if(nullMsg != "") {
            Ext.Msg.alert(Constants.SYS_REMIND_MSG,nullMsg);
            return false;
        }
        // 最大库存量应大于最小库存量
        var max = txtMaxStock.getValue();
        var min = txtMinStock.getValue();
        if(min > max && max != ""){
            Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.RI006_E_002);
            return false;
        }
        // 次供应商与主供应商重复时，报错：RI006_E_001
        var primary = txtMainSupply.getValue();
		var second = txtNextSupply.getValue();
		if(primary == second && second != ""){
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.RI006_E_001);
		    return false;
		}
		// 采购/存货计量单位转换比为空时默认为1
		if(!txtConversionRate1.getValue()){
			txtConversionRate1.setValue(1);
		}
        return true;
    }
	/**
	 * 判断表单数据是否改变
	 */
	function isFormChange() {
		// 获取现在的表单值
		var checkForm = formRegister.getForm().getValues();
		// 循环判断
		for (var prop in originalData) {
			if (checkForm[prop] != originalData[prop]) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 登记画面初始化
	 */
	function initRegister() {
		// 删除按钮不可用
		btnDelete.setDisabled(true);
		// 设置库位不可用
		//cbxDefaultWHousePostion.setDisabled(true);
		// 登记画面表单清空
		formRegister.getForm().reset();
	    // 重新保存初始值
		originalData = formRegister.getForm().getValues();
		// 去除库位store
	//	locationNameStore.removeAll();
	}
	/**
	 * 选择主供应商
	 */
	function addPrimarySupplier(){
		txtMainSupply.blur();
		var url="../../../../comm/jsp/supplierQuery/supplierQuery.jsp";    
	    var selectedMember = window.showModalDialog(
			 	url,
			 	null,
			 	'dialogWidth=800px;dialogHeight=550px;center=yes;help=no;resizable=no;status=no;');
	    if (selectedMember != null) {
	    	var selectedId = selectedMember.supplierId;
	    	var selectedName = selectedMember.supplyName;
	    	if(selectedId != ""){
	    		Ext.get("primarySupplier").dom.value = selectedId
			    txtMainSupply.setValue(selectedName);
	    	}else{
	    		Ext.get("primarySupplier").dom.value = null;
	    		txtMainSupply.setValue("");
	    	    Ext.get("secondarySupplier").dom.value = null;
	    		txtNextSupply.setValue("");
	    	}
	    }
	}
	/**
	 * 选择次供应商
	 */
	function addSecondarySupplier(){
		txtNextSupply.blur();
		var primary = txtMainSupply.getValue();
		if(primary == ""){
		    Ext.get("secondarySupplier").dom.value = null;
		    txtNextSupply.setValue("");
		}else{
			var url="../../../../comm/jsp/supplierQuery/supplierQuery.jsp";    
	        var selectedMember = window.showModalDialog(
			 	    url,
			 	    null,
			 	    'dialogWidth=800px;dialogHeight=550px;center=yes;help=no;resizable=no;status=no;');
	        if (selectedMember != null) {
		        var selectedId = selectedMember.supplierId;
	    	    var selectedName = selectedMember.supplyName;
		    	if(selectedId != ""){
			        Ext.get("secondarySupplier").dom.value = selectedId;
			        txtNextSupply.setValue(selectedName);
		    	}else{
		    	    Ext.get("secondarySupplier").dom.value = null;
		    		txtNextSupply.setValue("");
		    	}
		    }
		}
	}
    /**
     *  默认仓库选择  库位都默认为id1
     */
//    function defaultWHouseSelected() {
//    	var temp = formRegister.getForm().getValues();
//    	// 仓库No
//    	var defaultWhsNo = temp['materialBeen.defaultWhsNo'];
//    	if(defaultWhsNo == ""){
//    		// 去除库位store
//		    locationNameStore.removeAll();
//		    cbxDefaultWHousePostion.setValue("");
//		    cbxDefaultWHousePostion.setDisabled(true);
//		    return;
//    	}
//        locationNameStore.load({
//				params : {
//					whsNo : defaultWhsNo
//				}
//			});
//		locationNameStore.on("load", function(e, records, o) {
//		    cbxDefaultWHousePostion.setValue(records[0].data.locationNo);
//		    cbxDefaultWHousePostion.setDisabled(false);
//	    });
//    }
	/**
	 * 点击物料分类树时
	 */
	function treeClick(node, e) {
        cbxMClassificationHid.setValue(node.attributes.code);
        getWhs() // add by drdu 090624
    }
	/**
	 * 根据id从后台检索物料基础资料信息
	 */
	function getSupplyNameById(theId){
		var url = "clients/findClientInfoById.action?supplyId="	+ theId;
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("POST", url, false);
		conn.send(null);
		var mydata = eval('('+conn.responseText+')');
		return mydata.clientName;
	}
	/**
	 * 列表显示时，获取缺省仓库名称 add by drdu 090624
	 */
	function getWhsList(value, cellmeta, record, rowIndex, columnIndex, store) {
		if (record.data["materialClassId"] != null
				&& record.data["materialClassId"] != "") {

			var url = "resource/getParentCode.action?classId="
					+ record.data["materialClassId"];
			var conn = Ext.lib.Ajax.getConnectionObject().conn;
			conn.open("POST", url, false);
			conn.send(null);
			var obj = conn.responseText;
			var mytext = obj.substring(1, obj.length - 1);

			var arr = mytext.split(',');
			return arr[1];
		} else {
			return value;
		}

	}
});