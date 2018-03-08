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
							likestr : carNoQuery.getValue()
							
							
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
									tbar : ['根据牌照号码模糊查询',carNoQuery,'-',{
							id : 'que',
							text : "查询",
							iconCls : 'query',
							handler : function() {
								
								query();
							}
							},{
												text : "新增",
												iconCls : 'add',
												minWidth : 70,
								handler : addRecord
							}, '-', {
								text : "修改",
								iconCls : 'update',
								minWidth : 70,
								handler : updateRecord
							}, '-', {
								text : "删除",
								iconCls : 'delete',
								minWidth : 70,
								handler : deleteRecord
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
		grid.on("rowdblclick",updateRecord);

		var carId = new Ext.form.Hidden({
					id : "carId",
					fieldLabel : '车辆ID',
					readOnly : true,
					name : 'car.carId'
				});

		var carNo = new Ext.form.TextField({
					id : "carNo",
					fieldLabel : '牌照号码',
					allowBlank : false,
					width : 120,
					name : 'car.carNo'
				});

		var belongTo = new Ext.form.TextField({
					id : "belongTo",
					fieldLabel : '车辆产权归属单位',
					allowBlank : false,
					width : 120,
					name : 'car.belongTo'
				});

		var rightCode = new Ext.form.TextField({
					id : "rightCode",
					fieldLabel : '产权证书编号',
					allowBlank : true,
					width : 120,
					name : 'car.rightCode'
				});

		var factoryType = new Ext.form.TextField({
					id : "factoryType",
					fieldLabel : '厂牌型号',
					allowBlank : false,
					width : 120,
					name : 'car.factoryType'
				});
		
		var carType = new Ext.form.TextField({
					id : "carType",
					fieldLabel : '车辆类型',
					width : 120,
					name : 'car.carType'
				});		
		var seeSize = new Ext.form.TextField({
					id : "seeSize",
					fieldLabel : '外廓尺寸',
					width : 120,
					name : 'car.seeSize'
				});
	
		var carColor = new Ext.form.TextField({
					id : "carColor",
					fieldLabel : '车身颜色',
					width : 120,
					name : 'car.carColor'
				});			
				
		var fuelType = new Ext.form.TextField({
					id : "fuelType",
					fieldLabel : '燃油种类',
					width : 120,
					name : 'car.fuelType'
				});			
		
		var tireType = new Ext.form.TextField({
					id : "tireType",
					fieldLabel : '轮胎规格',
					width : 120,
					name : 'car.tireType'
				});			
		
	   var wheelbase = new Ext.form.TextField({
					id : "wheelbase",
					fieldLabel : '轴距',
					width : 120,
					name : 'car.wheelbase'
				});			
		var passergerCapacity = new Ext.form.NumberField({
					id : "passergerCapacity",
					fieldLabel : '核定载客',
					width : 120,
					name : 'car.passergerCapacity',
					allowNegative:false,
					allowDecimals:false
				});			
			
		var inOut = new Ext.form.ComboBox({
					fieldLabel : '国产/进口',
					store : new Ext.data.SimpleStore({
								fields : ['value', 'text'],
								data : [['I', '国产'], ['O', '进口']]
							}),
					id : 'inOut',
					name : 'inOut',
					valueField : "value",
					displayField : "text",
					mode : 'local',
					typeAhead : true,
					forceSelection : true,
					hiddenName : 'car.inOut',
					editable : false,
					triggerAction : 'all',
					selectOnFocus : true,
					width : 120
				});
				
	   var outFactoryDate = new Ext.form.TextField({
					id : "outFactoryDate",
					fieldLabel : '出厂日期',
					allowBlank : false,
					width : 120,
					name : 'car.outFactoryDate',
				    value : getDate(),
				    listeners : {
					 focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : true
								});
					 }
				 }
				});	
					
				
	   var firstRegisterDate = new Ext.form.TextField({
					id : "firstRegisterDate",
					fieldLabel : '初次登记日期',
					allowBlank : false,
					width : 120,
					name : 'car.firstRegisterDate',
				    value : getDate(),
				    listeners : {
					 focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : true
								});
					 }
				 }
				});			
		var engineCode = new Ext.form.TextField({
					id : "engineCode",
					fieldLabel : '发动机号',
					width : 120,
					name : 'car.engineCode'
				});			
		
		var discernCode = new Ext.form.TextField({
					id : "discernCode",
					fieldLabel : '识别代码/车架号',
					width : 120,
					name : 'car.discernCode'
				});			
		
	   var supplier = new Ext.form.TextField({
					id : "supplier",
					fieldLabel : '生产厂家',
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
    // 图片
    var tfPhoto = new Ext.form.TextField({
        id : "photo",
        name : 'photo',
        inputType : 'file',
        hideLabel : true,
        labelSeparator : '',
        height : 20,
        initEvents : function(){
        	Ext.form.TextField.prototype.initEvents.apply(this, arguments);
        	var keydown = function(e){
	            e.stopEvent();
	        };
	        this.el.on("keydown", keydown, this);
        }
    });	
   
		var addpanel = new Ext.FormPanel({
						frame : true,
						labelAlign : 'center',
						labelWidth : 105,
						closeAction : 'hide',
						fileUpload : true,
						title : '增加/修改车辆档案信息',
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
                                        items : [imagePhoto,tfPhoto]
									}]

						}]
					});

			// --------图片显示----------------------------
		addpanel.on('render', function(f) {
			addpanel.form.findField('photo').on('render',
					function() {
			Ext.get('photo').on('change', function(field, newValue, oldValue) {
				var url = Ext.get('photo').dom.value;
				var image = Ext.get('imagePhoto').dom;

				if (Ext.isIE7) {
					image.src = Ext.BLANK_IMAGE_URL;// 覆盖原来的图片
					image.filters
							.item("DXImageTransform.Microsoft.AlphaImageLoader").src = url;
				} else {
					image.src = url;
				}
			});
		  });
		})				
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
						text : '保存',
						iconCls : 'save',
						handler : function() {
							
							var myurl = "";
							if (carId.getValue() == "") {
								myurl = "security/saveSpCcarfile.action";
							} else {
								myurl = "security/updateSpCcarfile.action";
							}
                               
							addpanel.getForm().submit({
								method : 'POST',
								url : myurl,
								params:{
								  'filePath' : tfPhoto.getValue()
								},
								success : function(form, action) {
									if (action.result.existFlag == true) {
										Ext.Msg.alert("提示", "该牌照号码已存在 ,请重新输入!");
									} else {
										var o = eval("("
												+ action.response.responseText
												+ ")");
										Ext.Msg.alert("注意", o.msg);
										if (o.msg.indexOf("成功") != -1) {
			                                tfPhoto.setValue(null);
											store.reload();
											win.hide();
										}
									}
								},
								faliue : function() {
									Ext.Msg.alert('错误', '出现未知错误.');
								}
							});
						}
					}, {
						text : '取消',
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
		function addRecord() {
			addpanel.getForm().reset();
			win.show();
			addpanel.setTitle("增加车辆档案信息");
			Ext.get('imagePhoto').dom.src = '';
		}

		function updateRecord() {
			if (grid.selModel.hasSelection()) {
				var records = grid.selModel.getSelections();
				var recordslen = records.length;
				if (recordslen > 1) {
					Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
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
					addpanel.setTitle("修改车辆档案信息");
				}
			} else {
				Ext.Msg.alert("提示", "请先选择要编辑的行!");
			}
		}

		function deleteRecord() {
			var records = grid.selModel.getSelections();
			var carIds = [];
			if (records.length == 0) {
				Ext.Msg.alert("提示", "请选择要删除的记录！");
			} else {

				for (var i = 0; i < records.length; i += 1) {
					var member = records[i];
					if (member.get("carId")) {
						carIds.push(member.get("carId"));
					} else {
						store.remove(store.getAt(i));
					}
				}
				Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(buttonobj) {
							if (buttonobj == "yes") {
								Ext.lib.Ajax.request('POST',
										'security/deleteSpCcarfile.action', {
											success : function(action) {
												Ext.Msg.alert("提示", "删除成功！");
												store.reload();
											},
											failure : function() {
												Ext.Msg.alert('错误',
														'删除时出现未知错误.');
											}
										}, 'carIds=' + carIds);
							}
						});
			}
		}
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});
});