Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t;
		return s;
	};
	var carNoQuery = new Ext.form.TextField({
					id : "mquery",
					fieldLabel : '模糊查询',
					allowBlank : true,
					width : 120
			
				});
  
	//------部门-------------------
	 var deptRec = new Ext.data.Record.create([{
    	name  : 'deptCode',
    	mapping : 0
    },{
    	name : 'deptName',
    	mapping : 1
    }])
   
    var deptStore = new Ext.data.Store({
    	proxy : new Ext.data.HttpProxy({
    		url : 'security/getDeptListForCarOrDriver.action'
    	}),
    	reader : new Ext.data.JsonReader({},deptRec)
    })
  
    deptStore.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							flag : "car"
						})
			});
    deptStore.load();
    var dept = new Ext.form.ComboBox({
				id : 'dept',
				name : 'dept',
				store : deptStore,
				displayField : 'deptName',
				valueField : 'deptCode',
				mode : 'local',
				editable : true,
				triggerAction : 'all',
				allowBlank : false,
				anchor : "95%",
				selectOnFocus : true,
				typeAhead : true,
				forceSelection : true
			})	
	//----------------------------			
				
		var record = Ext.data.Record.create([{
					name : 'carId',
					mapping : 0
				}, {
					name : 'carNo',
					mapping : 1
				}, {
					name : 'belongTo',
					mapping : 2
				}, {
					name : 'rightCode',
					mapping : 3
				}, {
					name : 'factoryType',
					mapping : 4
				}, {
					name : 'carType',
					mapping : 5
				}, {
					name : 'seeSize',
					mapping : 6
				}, {
					name : 'carColor',
					mapping : 7
				}, {
					name : 'fuelType',
					mapping : 8
				}, {
					name : 'tireType',
					mapping : 9
				}, {
					name : 'wheelbase',
					mapping : 10
				}, {
					name : 'passergerCapacity',
					mapping : 11
				}, {
					name : 'inOut',
					mapping : 12
				}, {
					name : 'outFactoryDate',
					mapping : 13
				}, {
					name : 'firstRegisterDate',
					mapping : 14
				}, {
					name : 'engineCode',
					mapping : 15
				}, {
					name : 'discernCode',
					mapping : 16
				}, {
					name : 'supplier',
					mapping : 17
				}, {
					name : 'lastModifiedBy',
					mapping : 18
				}, {
					name : 'lastModifiedName',
					mapping : 19
				}, {
					name : 'lastModifiedTime',
					mapping : 20
				}, {
					name : 'deptCode',
					mapping : 21
				}, {
					name : 'deptName',
					mapping : 22
				}]);

		var dataProxy = new Ext.data.HttpProxy({
					url : 'security/getSpCcarfileList.action'
				});

		var theReader = new Ext.data.JsonReader({
					root : "list",
					totalProperty : "totalCount"
				}, record);

		var store = new Ext.data.Store({
					proxy : dataProxy,
					reader : theReader
				});
		store.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							likestr : carNoQuery.getValue(),
							dept:dept.getValue(),
							flag:"query"
						})
			});
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		})
		var sm = new Ext.grid.CheckboxSelectionModel();

		var grid = new Ext.grid.GridPanel({
					region : "center",
					layout : 'fit',
					store : store,
					columns : [sm, new Ext.grid.RowNumberer({
										header : '行号',
										width : 35,
										align : 'left'
									}), {
								header : "车辆ID",
								width : 75,
								sortable : true,
								dataIndex : 'carId',
								hidden : true
							}, {
								header : "牌照号码",
								width : 200,
								sortable : true,
								dataIndex : 'carNo'
							}, {
								header : "车辆产权归属单位",
								width : 200,
								sortable : true,
								dataIndex : 'belongTo'
							}, {
								header : "产权证书编号",
								width : 200,
								sortable : true,
								dataIndex : 'rightCode'
							}, {
								header : "厂牌型号",
								width : 200,
								sortable : true,
								dataIndex : 'factoryType'
							}, {
								header : "车辆类型",
								width : 200,
								sortable : true,
								dataIndex : 'carType'
							}, {
								header : "外廓尺寸",
								width : 200,
								sortable : true,
								dataIndex : 'seeSize'
							}, {
								header : "车身颜色",
								width : 200,
								sortable : true,
								dataIndex : 'carColor'
							},  {
								header : "燃油种类",
								width : 200,
								sortable : true,
								dataIndex : 'fuelType'
							}, {
								header : "轮胎规格",
								width : 200,
								sortable : true,
								dataIndex : 'tireType'
							}, {
								header : "轴距",
								width : 200,
								sortable : true,
								dataIndex : 'wheelbase'
							}, {
								header : "核定载客",
								width : 200,
								sortable : true,
								dataIndex : 'passergerCapacity'
							}, {
								header : "国产/进口",
								width : 200,
								sortable : true,
								dataIndex : 'inOut',
								renderer : function(v) {
									if (v == 'I') {
										return "国产";
									}
									if (v == 'O') {
										return "进口";
									}
								}
							}, {
								header : "出厂日期",
								width : 200,
								sortable : true,
								dataIndex : 'outFactoryDate'
							}, {
								header : "初次登记日期",
								width : 200,
								sortable : true,
								dataIndex : 'firstRegisterDate'
							}, {
								header : "发动机号",
								width : 200,
								sortable : true,
								dataIndex : 'engineCode'
							}, {
								header : "识别代码/车架号",
								width : 200,
								sortable : true,
								dataIndex : 'discernCode'
							}, {
								header : "生产厂家",
								width : 200,
								sortable : true,
								dataIndex : 'supplier'
							}],
					sm : sm,
					autoSizeColumns : true,
					viewConfig : {
						forceFit : true
									},
									tbar : ['牌照',carNoQuery,'-','部门',dept,'-',{
							id : 'que',
							text : "查询",
							iconCls : 'query',
							handler : function() {
							 query();
							}
							}, '-', {
								text : "詳細查看",
								iconCls : 'detail',
								minWidth : 70,
								handler : detailRecord
							}],
					// 分页
					bbar : new Ext.PagingToolbar({
								pageSize : 18,
								store : store,
								displayInfo : true,
								displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
								emptyMsg : "没有记录"
							})
				});
		grid.on("rowdblclick",detailRecord);

		var carId = new Ext.form.Hidden({
					id : "carId",
					fieldLabel : '车辆ID',
					readOnly : true,
					name : 'car.carId'
				});

		var carNo = new Ext.form.TextField({
					id : "carNo",
					fieldLabel : '牌照号码',
					readOnly : true,
					width : 120,
					name : 'car.carNo'
				});

		var belongTo = new Ext.form.TextField({
					id : "belongTo",
					fieldLabel : '车辆产权归属单位',
					readOnly : true,
					width : 120,
					name : 'car.belongTo'
				});

		var rightCode = new Ext.form.TextField({
					id : "rightCode",
					fieldLabel : '产权证书编号',
					readOnly : true,
					width : 120,
					name : 'car.rightCode'
				});

		var factoryType = new Ext.form.TextField({
					id : "factoryType",
					fieldLabel : '厂牌型号',
					readOnly : true,
					width : 120,
					name : 'car.factoryType'
				});
		
		var carType = new Ext.form.TextField({
					id : "carType",
					fieldLabel : '车辆类型',
					readOnly : true,
					width : 120,
					name : 'car.carType'
				});		
		var seeSize = new Ext.form.TextField({
					id : "seeSize",
					fieldLabel : '外廓尺寸',
					readOnly : true,
					width : 120,
					name : 'car.seeSize'
				});
	
		var carColor = new Ext.form.TextField({
					id : "carColor",
					fieldLabel : '车身颜色',
					readOnly : true,
					width : 120,
					name : 'car.carColor'
				});			
				
		var fuelType = new Ext.form.TextField({
					id : "fuelType",
					fieldLabel : '燃油种类',
					readOnly : true,
					width : 120,
					name : 'car.fuelType'
				});			
		
		var tireType = new Ext.form.TextField({
					id : "tireType",
					fieldLabel : '轮胎规格',
					readOnly : true,
					width : 120,
					name : 'car.tireType'
				});			
		
	   var wheelbase = new Ext.form.TextField({
					id : "wheelbase",
					fieldLabel : '轴距',
					readOnly : true,
					width : 120,
					name : 'car.wheelbase'
				});			
		var passergerCapacity = new Ext.form.NumberField({
					id : "passergerCapacity",
					fieldLabel : '核定载客',
					width : 120,
					name : 'car.passergerCapacity',
					readOnly : true
				});			
			
		var inOut = new Ext.form.TextField({
			        id : "inOut",
					fieldLabel : '国产/进口',
					width : 120,
					name : 'car.inOut',
					readOnly : true
				});
				
	   var outFactoryDate = new Ext.form.TextField({
					id : "outFactoryDate",
					fieldLabel : '出厂日期',
					readOnly : true,
					width : 120,
					name : 'car.outFactoryDate'
				});	
					
				
	   var firstRegisterDate = new Ext.form.TextField({
					id : "firstRegisterDate",
					fieldLabel : '初次登记日期',
					readOnly : true,
					width : 120,
					name : 'car.firstRegisterDate'
				});			
		var engineCode = new Ext.form.TextField({
					id : "engineCode",
					fieldLabel : '发动机号',
					readOnly : true,
					width : 120,
					name : 'car.engineCode'
				});			
		
		var discernCode = new Ext.form.TextField({
					id : "discernCode",
					fieldLabel : '识别代码/车架号',
					readOnly : true,
					width : 120,
					name : 'car.discernCode'
				});			
		
	   var supplier = new Ext.form.TextField({
					id : "supplier",
					fieldLabel : '生产厂家',
					readOnly : true,
					width : 120,
					name : 'car.supplier'
				});	
	
	 // 选择图片
    var imagePhoto = new Ext.form.TextField({
        id : "imagePhoto",
        labelSeparator : '',
        height : 180,
        width:210,
		hideLabel : true,
        src : '/power/comm/images/UnknowBody.jpg',
        style : 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);',
        inputType : 'image' 
    });
  
   
		var addpanel = new Ext.FormPanel({
						frame : true,
						labelAlign : 'center',
						labelWidth : 105,
						closeAction : 'hide',
						fileUpload : true,
						title : '车辆档案详细信息',
						items : [{
							layout : 'column',
							items : [{// 第一列
								columnWidth : 0.34,
								layout : 'form',
								items : [carId,carNo, rightCode,carType , seeSize,tireType
										, passergerCapacity,outFactoryDate ,engineCode ,
										supplier ]
							}, {	// 第二列
										columnWidth : 0.38,
										layout : 'form',
										items : [belongTo, factoryType,
												carColor, fuelType,wheelbase
												, inOut,firstRegisterDate
												,discernCode]
									}, {
										columnWidth : 0.28,
										rowspan : 9,
										layout : 'form',
                                        items : [imagePhoto]
									}]

						}]
					});
		
		var win = new Ext.Window({
					width : 750,
					height : 400,
					buttonAlign : "center",
					items : [addpanel],
					layout : 'fit',
					closeAction : 'hide',
					resizable : false,
					modal : true,
					buttons : [{
						text : '关闭',
						iconCls : 'cancer',
						handler : function() {
							win.hide();
						}
					}]

				});
  function query()
  {
  	store.reload();
  }
		function detailRecord() {
			if (grid.selModel.hasSelection()) {
				var records = grid.selModel.getSelections();
				var recordslen = records.length;
				if (recordslen > 1) {
					Ext.Msg.alert("系统提示信息", "请选择其中一项进行查看！");
				} else {
					win.show();
					var record = grid.getSelectionModel()
							.getSelected();
					addpanel.getForm().reset();
					addpanel.getForm().loadRecord(record)
					// 加载图片
             Ext.get('imagePhoto').dom.src = "security/showphoto.action?Id="
            + carId.getValue() + "&time=" + new Date().getTime()
            + "&type=" + "C";
//					}
					addpanel.setTitle("车辆档案详细信息");
				}
			} else {
				Ext.Msg.alert("提示", "请先选择要查看的行!");
			}
		}

	
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});
});