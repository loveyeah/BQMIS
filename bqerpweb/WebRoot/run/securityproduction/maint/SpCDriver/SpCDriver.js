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
							likestr : nameQuery.getValue()
							
							
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
					tbar : ['根据司机姓名模糊查询',nameQuery,{
								text : "查询",
								iconCls : 'query',
								minWidth : 70,
								handler : query
							}, '-',{
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

		var driverId = new Ext.form.Hidden({
					id : "driverId",
					fieldLabel : '司机ID',
					readOnly : true,
					name : 'driver.driverId'
				});

		var driverName = new Ext.form.TextField({
					id : "driverName",
					fieldLabel : '司机姓名',
					allowBlank : false,
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
					allowBlank : false,
					hiddenName : 'driver.sex',
					editable : false,
					triggerAction : 'all',
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
		allowBlank : false,
    	fieldLabel : '籍贯',
    	triggerAction : 'all',
    	width : 125
    })	

		
		// 出身日期
		var brithday = {
			id : 'brithday',
			xtype : "datefield",
			fieldLabel : '出生日期',
			name : 'driver.brithday',
			format : 'Y-m-d',
			width : 125,
			readOnly : true
		};
		
	// 部门
	var deptCode = new Ext.form.Hidden({
		id : 'deptCode',
		name : 'driver.deptCode'
	
	})
	var dept = new Power.dept(null, false, {
				fieldLabel : '部门',
				hiddenName : 'deptName',
				anchor : '88%',
				allowBlank : false
			});
	dept.btnConfrim.on('click', function() {
				var deptRes = dept.getValue();
				if (deptRes.code != null) {
					deptCode.setValue(deptRes.code)
				} else {
					dept.combo.setValue(null);
					return;
				}
			})
	
	/*var dept = new Ext.form.TriggerField({
		id : 'dept',
		fieldLabel : '部门',
		allowBlank : false,
		name : 'dept',
		width : 125,
		onTriggerClick : function() {
			var url =  "/power/comm/jsp/hr/dept/dept.jsp";
			var args = {
				selectModel : 'single',
				rootNode : {
					id : "0",
					text : '灞桥热电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:250px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (typeof(emp) != "undefined") {
				deptCode.setValue(emp.codes);
				dept.setValue(emp.names);
			}
		}
	})
*/
	var workTime = {
			id : 'workTime',
			xtype : "datefield",
			fieldLabel : '工作时间',
			name : 'driver.workTime',
			format : 'Y-m-d',
			width : 125,
			readOnly : true
		};		
				
		var homePhone = new Ext.form.TextField({
					id : "homePhone",
					fieldLabel : '住宅电话',
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
		allowBlank : false,
    	fieldLabel : '政治面貌',
    	triggerAction : 'all',
    	width : 125
    })				
	
		
		var joinInTime = {
			id : 'joinInTime',
			xtype : "datefield",
			fieldLabel : '加入时间',
			name : 'driver.joinInTime',
			format : 'Y-m-d',
			width : 125,
			readOnly : true
		};
		
		var mobilePhone = new Ext.form.NumberField({
					id : "mobilePhone",
					fieldLabel : '手机号码',
					allowBlank : false,
					width : 125,
					name : 'driver.mobilePhone',
					allowNegative:false,
					allowDecimals:false
				});		
		var driveCode = new Ext.form.TextField({
					id : "driveCode",
					fieldLabel : '驾驶证号',
					allowBlank : false,
					width : 125,
					name : 'driver.driveCode'
				});
		
		var allowDriverType = new Ext.form.TextField({
					id : "allowDriverType",
					fieldLabel : '准驾车型',
					width : 125,
					name : 'driver.allowDriverType'
				});		
		var memo = new Ext.form.TextField({
					id : "memo",
					fieldLabel : '备注',
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
    // 图片
    var tfPhoto = new Ext.form.TextField({
        id : "photo",
        name : 'photo',
        inputType : 'file',
        labelSeparator : '',
		hideLabel : true,
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
						labelWidth : 60,
						closeAction : 'hide',
						fileUpload : true,
						title : '增加/修改司机档案信息',
						items : [{
							layout : 'column',
							items : [{// 第一列
								columnWidth : 0.35,
								layout : 'form',
								items : [driverId,driverName, nativePlaceId,deptCode,dept.combo,homePhone
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
					width : 650,
					height : 380,
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
							if (driverId.getValue() == "") {
								
								myurl = "security/saveSpCDriver.action";
							} else {
								myurl = "security/updateSpCDriver.action";
							}
							addpanel.getForm().submit({
								method : 'POST',
								url : myurl,
								params:{
								  'filePath' : tfPhoto.getValue()
								},
								success : function(form, action) {
									if (action.result.existFlag == true) {
										Ext.Msg.alert("提示", "该驾驶证号已存在 ,请重新输入!");
									} else {
										var o = eval("("
												+ action.response.responseText
												+ ")");
										Ext.Msg.alert("注意", o.msg);
										if (o.msg.indexOf("成功") != -1) {
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
			addpanel.setTitle("增加司机档案信息");
			Ext.get('imagephoto').dom.src = '/power/comm/images/UnknowBody.jpg';
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
					
					addpanel.getForm().loadRecord(record);
					dept.setValue(records[0].get("deptCode"),records[0].get("deptName"));
					
					// 加载图片
             Ext.get('imagePhoto').dom.src = "security/showphoto.action?Id="
            + driverId.getValue() + "&time=" + new Date().getTime()
            + "&type=" + "D";
//					}
					addpanel.setTitle("修改司机档案信息");
				}
			} else {
				Ext.Msg.alert("提示", "请先选择要编辑的行!");
			}
		}

		function deleteRecord() {
			var records = grid.selModel.getSelections();
			var driverIds = [];
			if (records.length == 0) {
				Ext.Msg.alert("提示", "请选择要删除的记录！");
			} else {

				for (var i = 0; i < records.length; i += 1) {
					var member = records[i];
					if (member.get("driverId")) {
						driverIds.push(member.get("driverId"));
					} else {
						store.remove(store.getAt(i));
					}
				}
				Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(buttonobj) {
							if (buttonobj == "yes") {
								Ext.lib.Ajax.request('POST',
										'security/deleteSpCDriver.action', {
											success : function(action) {
												Ext.Msg.alert("提示", "删除成功！");
												store.reload();
											},
											failure : function() {
												Ext.Msg.alert('错误',
														'删除时出现未知错误.');
											}
										}, 'driverIds=' + driverIds);
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