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
	var nameQuery = new Ext.form.TextField({
					id : "namequery",
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
							flag : "driver"
						})
			});
    deptStore.load();
    var deptQuery = new Ext.form.ComboBox({
				id : 'deptQuery',
				name : 'deptQuery',
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
					name : 'driverId',
					mapping : 0
				}, {
					name : 'driverName',
					mapping : 1
				}, {
					name : 'sex',
					mapping : 2
				}, {
					name : 'nativePlaceId',
					mapping : 3
				},  {
					name : 'nativePlace',
					mapping : 4
				}, {
					name : 'brithday',
					mapping : 5
				}, {
					name : 'deptCode',
					mapping : 6
				}, {
					name : 'deptName',
					mapping : 7
				}, {
					name : 'workTime',
					mapping : 8
				}, {
					name : 'homePhone',
					mapping : 9
				}, {
					name : 'politicsId',
					mapping : 10
				}, {
					name : 'politics',
					mapping : 11
				}, {
					name : 'joinInTime',
					mapping : 12
				}, {
					name : 'mobilePhone',
					mapping : 13
				}, {
					name : 'driveCode',
					mapping : 14
				}, {
					name : 'allowDriverType',
					mapping : 15
				}, {
					name : 'memo',
					mapping : 16
				}]);

		var dataProxy = new Ext.data.HttpProxy({
					url : 'security/getSpCDriverList.action'
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
							likestr : nameQuery.getValue(),
							deptQuery:deptQuery.getValue(),
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
								header : "司机ID",
								width : 75,
								sortable : true,
								dataIndex : 'driverId',
								hidden : true
							}, {
								header : "司机姓名",
								width : 200,
								sortable : true,
								dataIndex : 'driverName'
							}, {
								header : "性别",
								width : 200,
								sortable : true,
								dataIndex : 'sex',
								renderer : function(v) {
									if (v == 'F') {
										return "女";
									}
									if (v == 'M') {
										return "男";
									}
								}
							}, {
								header : "籍贯",
								width : 200,
								sortable : true,
								dataIndex : 'nativePlace'
							}, {
								header : "出生日期",
								width : 200,
								sortable : true,
								dataIndex : 'brithday'
							}, {
								header : "部门",
								width : 200,
								sortable : true,
								dataIndex : 'deptName'
							}, {
								header : "工作时间",
								width : 200,
								sortable : true,
								dataIndex : 'workTime'
							}, {
								header : "住宅电话",
								width : 200,
								sortable : true,
								dataIndex : 'homePhone'
							},  {
								header : "政治面貌",
								width : 200,
								sortable : true,
								dataIndex : 'politics'
							}, {
								header : "加入时间",
								width : 200,
								sortable : true,
								dataIndex : 'joinInTime'
							}, {
								header : "手机号码",
								width : 200,
								sortable : true,
								dataIndex : 'mobilePhone'
							}, {
								header : "驾驶证号",
								width : 200,
								sortable : true,
								dataIndex : 'driveCode'
							}, {
								header : "准驾车型",
								width : 200,
								sortable : true,
								dataIndex : 'allowDriverType'
							}, {
								header : "备注",
								width : 200,
								sortable : true,
								dataIndex : 'memo'
							}],
					sm : sm,
					autoSizeColumns : true,
					viewConfig : {
						forceFit : true
					},
					tbar : ['司机姓名',nameQuery,'-','部门',deptQuery,'-',{
								text : "查询",
								iconCls : 'query',
								minWidth : 70,
								handler : query
							}, '-', {
								text : "详细查看",
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

		var driverId = new Ext.form.Hidden({
					id : "driverId",
					fieldLabel : '司机ID',
					readOnly : true,
					name : 'driver.driverId'
				});

		var driverName = new Ext.form.TextField({
					id : "driverName",
					fieldLabel : '司机姓名',
					readOnly : true,
					width : 125,
					name : 'driver.driverName'
				});

			
			var sex = new Ext.form.ComboBox({
					fieldLabel : '性别',
					store : new Ext.data.SimpleStore({
								fields : ['value', 'text'],
								data : [['F', '女'], ['M', '男']]
							}),
					id : 'sex',
					name : 'sex',
					valueField : "value",
					displayField : "text",
					mode : 'local',
					typeAhead : true,
					forceSelection : true,
					readOnly : true,
					hiddenName : 'driver.sex',
					editable : false,
					triggerAction : 'query',
					selectOnFocus : true,
					width : 125
				});		
		
// 籍贯:
 var nativeRec = new Ext.data.Record.create([{
    	name  : 'value',
    	mapping : 0
    },{
    	name : 'text',
    	mapping : 1
    }])
    
    var nativestore = new Ext.data.Store({
    	proxy : new Ext.data.HttpProxy({
    		url : 'security/getNativePlaceData.action'
    	}),
    	reader : new Ext.data.ArrayReader({},nativeRec)
    })
    nativestore.load();
    
    var nativePlaceId = new Ext.form.ComboBox({
    	id : 'nativePlaceId',
    	hiddenName : 'driver.nativePlaceId',
    	name : 'nativePlaceId',
    	store : nativestore,
    	displayField : 'text',
    	valueField : 'value',
    	mode : 'local',
    	editable : false,
		readOnly : true,
    	fieldLabel : '籍贯',
    	triggerAction : 'query',
    	width : 125
    })	

		
	// 出身日期
     var brithday = new Ext.form.TextField({
				id : 'brithday',
				fieldLabel : '出生日期',
				readOnly : true,
				width : 125,
				name : 'driver.brithday'
			})	
		
	// 部门
	 var dept = new Ext.form.TextField({
				id : 'dept',
				fieldLabel : '部门',
				readOnly : true,
				width : 125,
				name : 'deptName'
			})	
	
    var workTime = new Ext.form.TextField({
				id : 'workTime',
				fieldLabel : '工作时间',
				readOnly : true,
				width : 125,
				name : 'driver.workTime'
			})
	
	var homePhone = new Ext.form.TextField({
				id : "homePhone",
				fieldLabel : '住宅电话',
				readOnly : true,
				width : 125,
				name : 'driver.homePhone'
			});

// 政治面貌
	 var politicsRec = new Ext.data.Record.create([{
    	name  : 'value',
    	mapping : 0
    },{
    	name : 'text',
    	mapping : 1
    }])
    
    var politicsstore = new Ext.data.Store({
    	proxy : new Ext.data.HttpProxy({
    		url : 'security/getPoliticsData.action'
    	}),
    	reader : new Ext.data.ArrayReader({},politicsRec)
    })
    politicsstore.load();
    
    var politicsId = new Ext.form.ComboBox({
    	id : 'politicsId',
    	hiddenName : 'driver.politicsId',
    	name : 'politicsId',
    	store : politicsstore,
    	displayField : 'text',
    	valueField : 'value',
    	mode : 'local',
    	editable : false,
		readOnly : true,
    	fieldLabel : '政治面貌',
    	triggerAction : 'query',
    	width : 125
    })				
	
	var joinInTime = new Ext.form.TextField({
				id : "joinInTime",
				fieldLabel : '加入时间',
				readOnly : true,
				width : 125,
				name : 'driver.joinInTime'
			});
	
	var mobilePhone = new Ext.form.NumberField({
					id : "mobilePhone",
					fieldLabel : '手机号码',
				    readOnly : true,
					width : 125,
					name : 'driver.mobilePhone'
				});		
	var driveCode = new Ext.form.TextField({
					id : "driveCode",
					fieldLabel : '驾驶证号',
					readOnly : true,
					width : 125,
					name : 'driver.driveCode'
				});
		
		var allowDriverType = new Ext.form.TextField({
					id : "allowDriverType",
					fieldLabel : '准驾车型', 
					readOnly : true,
					width : 125,
					name : 'driver.allowDriverType'
				});		
		var memo = new Ext.form.TextField({
					id : "memo",
					fieldLabel : '备注',
					readOnly : true,
					width : 125,
					name : 'driver.memo'
				});		
	
	
	 // 选择图片
    var imagePhoto = new Ext.form.TextField({
        id : "imagePhoto",
        labelSeparator : '',
		hideLabel : true,
        height : 180,
        width:200,
        src : '/power/comm/images/UnknowBody.jpg',
        style : 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);',
        inputType : 'image'
    });
  
   
		var addpanel = new Ext.FormPanel({
						frame : true,
						labelAlign : 'center',
						labelWidth : 60,
						closeAction : 'hide',
						fileUpload : true,
						title : '司机档案详细信息',
						items : [{
							layout : 'column',
							items : [{// 第一列
								columnWidth : 0.35,
								layout : 'form',
								items : [driverId,driverName, nativePlaceId,dept,homePhone
										, joinInTime,driveCode,memo]
							}, {	// 第二列
										columnWidth : 0.35,
										layout : 'form',
										items : [sex, brithday,
												workTime, politicsId,mobilePhone
												, allowDriverType]
									}, {
										columnWidth : 0.3,
										rowspan : 9,
										layout : 'form',
                                        items : [imagePhoto]
									}]

						}]
					});

		
		var win = new Ext.Window({
					width : 650,
					height : 380,
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
					
					addpanel.getForm().loadRecord(record);
					dept.setValue(records[0].get("deptName"));
					
			// 加载图片
             Ext.get('imagePhoto').dom.src = "security/showphoto.action?Id="
            + driverId.getValue() + "&time=" + new Date().getTime()
            + "&type=" + "D";

					addpanel.setTitle("司机档案详细信息");
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