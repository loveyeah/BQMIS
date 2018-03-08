/**
 * 工作票区域维护
 * @author 黄维杰
 * @version 1.0
 */
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function(){
   // 空字符串常量
   var BLANK_STRING = "";
	
   //*****弹出窗口*******//
   var txtLocationIdPop = new Ext.form.TextField({
   		fieldLabel:"ID",
   		id:"locationId",
   		readOnly:true, 
   		name:"locationId",
   		value:Constants.AUTO_CREATE,
   		width: 180
   });
   
    // 所属系统检索	
	var blockListStore = new Ext.data.JsonStore({
		url:"workticket/getBelongSystem.action",
		root : 'list',				
		fields:[{name:"blockCode"},{name:"blockName"}]
	});
	// 读取所属系统store
	blockListStore.load();
 
	// 所属系统下拉框
	var comboBlockCodePop = new Ext.form.ComboBox({
   		fieldLabel:"所属系统",
   		id:"blockCodePop",
   		name:"blockCodePop",
   		store:blockListStore,
   		width: 180,
   		hiddenName:"hidBlockCodePop",
   		displayField:"blockName",
   		valueField:"blockCode",
   		mode : 'local', 
   		triggerAction : 'all',
   		emptyText : '请选择所属系统...',
        blankText : '所属系统',
   		allowBlank : false,
   		readOnly : true
	});
   
	//区域名称文本框
	var txtLocationNamePop = new Ext.form.TextField({
   		fieldLabel:"区域名称<font color='red'>*</font>",
   		id:"locationNamePop",
   		allowBlank:false,
   		name:"locationNamePop",
   		width: 180,
        maxLength : 100
	});
   
	//排序号文本框
	var txtOrderBy = new Ext.form.NumberField({
   		fieldLabel:"显示顺序",
   		id:"orderBy",
   		name:"orderBy",
   		width: 180,
        maxLength : 10
	});
	
	var addPanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'right',
		items : [txtLocationIdPop,comboBlockCodePop,txtLocationNamePop,txtOrderBy]
	});
	
	var win = new Ext.Window({
		width : 350,
		height : 200,
   		modal : true,
		title : '工作票区域维护',
		buttonAlign : "center",
		items : [addPanel],
		layout : 'fit',
		closeAction : 'hide',
		resizable : false,
		buttons : [{
			text : Constants.BTN_SAVE,
			id:"add",
			iconCls : Constants.CLS_SAVE,
			handler : function() {
				if (txtLocationNamePop.isValid()) {
					var myurl="";
					// 添加记录Action
					if (Ext.get("locationId").dom.value == Constants.AUTO_CREATE) {
						myurl="workticket/addLocation.action";
					// 修改记录Action
					} else {
							myurl="workticket/updateLocation.action";
					}
					addPanel.getForm().submit({
						method : Constants.POST,
						url : myurl,
						success : function(form, action) {						
							var o = eval("(" + action.response.responseText + ")");
							Ext.Msg.alert(Constants.NOTICE, o.msg);
							// 重新读取store的最新内容
							searchStore.load({
		                       	params : {
		                           	blockCode : comboBlockCode.value,
		                           	start : 0,
									limit : Constants.PAGE_SIZE
		                        }});
							win.hide(); 
						},
						failure : function() {
							Ext.Msg.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
						}
					});
				} else {
					Ext.Msg.alert(Constants.ERROR, '请填写正确信息后再保存');
				}
			}}, 
			{text : Constants.BTN_CANCEL,
				id:"close",
				iconCls:Constants.CLS_CANCEL,
				// 窗口隐藏
				handler : function() {
					win.hide();
			}}]
	});

	//********** 主画面*******//
		
	// 所属系统检索	
	var blockListStore = new Ext.data.JsonStore({
		url:"workticket/getBelongSystem.action",
		root : 'list',				
		fields:[{name:"blockCode"},{name:"blockName"}]		
	});
	// 读取所属系统store
	blockListStore.load();
	
	// 所属系统下拉框
	var comboBlockCode = new Ext.form.ComboBox({
   		fieldLabel:"所属系统",
   		id:"blockCode",
   		name:"blockCode",
   		store:blockListStore,
   		hiddenName:"hidBlockCode",
   		displayField:"blockName",
   		valueField:"blockCode",
   		mode : 'local', 
   		triggerAction : 'all',
   		emptyText : '请选择所属系统...',
        blankText : '所属系统',
   		allowBlank : false,
   		readOnly : true
	});

	// 区域名称文本框
	var txtLocationName = new Ext.form.TextField({
   		fieldLabel:"区域名称",
   		id:"locationName",
   		name:"locationName"
	});
	
    // 查询按钮
    var searchBtn = new Ext.Button({
                id : 'search',
                text : Constants.BTN_QUERY,
                iconCls : Constants.CLS_QUERY
            });
    // 增加按钮
    var addBtn = new Ext.Button({
                id : 'add',
                text : Constants.BTN_ADD,
                iconCls : Constants.CLS_ADD,
                handler : function() {
                }
            });
    // 修改按钮
    var editBtn = new Ext.Button({
                id : 'edit',
                text : Constants.BTN_UPDATE,
                iconCls : Constants.CLS_UPDATE,
                handler : updateRecord
            });
    // 删除按钮
    var deleteBtn = new Ext.Button({
                id : 'delete',
                text : Constants.BTN_DELETE,
                iconCls : Constants.CLS_DELETE,
                handler : function() {
                }
            });
	// 选择列
	var sm2 = new Ext.grid.CheckboxSelectionModel();
  
    // grid中的数据bean
	var rungridlist = new Ext.data.Record.create([  {
				name : 'locationId'
			}, {
				name : 'locationName'
			}, {
				name : 'blockCode'
			}, {
				name : 'orderBy'
			},{
				name : 'modifyBy'
			},{
				name : 'modifyDate'
			}]);
    
    // grid的store
    var searchStore = new Ext.data.JsonStore({
				url : 'workticket/getLocationList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : rungridlist
			});
	searchStore.setDefaultSort('orderBy', 'ASC');
	// 格式化时间输出格式
	function renderDate(value) {
		if (!value) return "";
		//格式化输出时间为"yyyy-mm-dd"
		var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
	    var strDate = value.match(reDate);
	    if (!strDate) return "";
	    return strDate;
	}

	// 分页工具栏
	var pagebar = new Ext.PagingToolbar({
					pageSize : Constants.PAGE_SIZE,
					store : searchStore,
					displayInfo : true,
					displayMsg : Constants.DISPLAY_MSG,
					emptyMsg : Constants.EMPTY_MSG
				})
	// 页面的Grid主体
    var rungrid = new Ext.grid.GridPanel({
                store : searchStore,
                columns : [ sm2, {
                            header : "区域名称",
                            width : 200,
                            sortable : true,
                            dataIndex : 'locationName'
                        }, {
                            header : "填写日期",
                            width : 60,
                            sortable : true,
                            dataIndex : 'modifyDate',
                            renderer : renderDate
                        }, {
                            header : "填写人",
                            width : 60,
                            sortable : true,
                            hidden : true,
                            dataIndex : 'modifyBy'
                        }],
                viewConfig : {
                    forceFit : true
                },
                tbar : ['所属系统：', comboBlockCode, {
                            xtype : "tbseparator"
                        },'区域名称：', txtLocationName,searchBtn, addBtn, editBtn, deleteBtn],
		        //分页
				bbar : pagebar,
                sm : sm2,
                frame : false,
                border : false,
                enableColumnHide : true,
                enableColumnMove : false,
                autoExpandColumn:2
            });
    // 设置grid的某条记录的双击事件
    rungrid.on("rowdblclick", updateRecord);
	// 设定布局器及面板
    var layout = new Ext.Viewport({
                layout : "border",
                border : false,
                items : [{
                            title : "",
                            region : 'center',
                            layout : 'fit',
                            border : false,
                            margins : '0 0 0 0',
                            split : true,
                            collapsible : false,
                            items : [rungrid]
                        }]
            });
    	
    // 通过store的装载初始化所属系统下拉框的默认选项为store的第一项
	blockListStore.on("load", function(e, records, o) {
		comboBlockCode.setValue(records[0].data.blockCode);
		searchStore.baseParams = {
    				// 设置所属系统
					blockCode : comboBlockCode.value,
					// 设置区域名称
					locationName : txtLocationName.getValue()
				};
		searchStore.load({
			params : {
				// 可选：初始化grid的页面数据
//				blockCode : records[0].data.blockCode,
				start : 0,
				limit : Constants.PAGE_SIZE
			}
    	});
	});
	//***************************处理**************************//
    // 增加处理
    addBtn.handler = function(){
    	addPanel.getForm().reset();
    	win.show();
    	// 获得所属系统，设置弹出窗口的默认选项
    	comboBlockCodePop.setValue(comboBlockCode.value);
    	comboBlockCodePop.enable();
		
    }
    // 编辑处理
    function updateRecord()
	{
		if (rungrid.selModel.hasSelection()) {
			var records = rungrid.selModel.getSelections();
			var recordslen = records.length;
			// 提示选择了多项记录
			if (recordslen > 1) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.SELECT_COMPLEX_MSG);
			} else {
				// 取得选中的记录
				var record = rungrid.getSelectionModel().getSelected();
				win.show();
				addPanel.getForm().loadRecord(record);
				// 设置区域Id
				txtLocationIdPop.setValue(record.get("locationId"));
				// 设置所属系统
				comboBlockCodePop.setValue(record.get("blockCode"));
				// 编辑时所属系统为只读
				comboBlockCodePop.disable();
				// 设置区域名称
				txtLocationNamePop.setValue(record.get("locationName"));
				// 设置排序号
				txtOrderBy.setValue(record.get("orderBy"));
			}
		} else {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.SELECT_NULL_UPDATE_MSG);
		}
	}
    // 查询处理
    searchBtn.handler = search;
    function search() {
    			var value = comboBlockCode.value;
    			if(value == undefined || value==""){
    				Ext.MessageBox.alert(Constants.NOTICE,"请选择一个所属系统");
    				return;
    			}
    			searchStore.baseParams = {
    				// 设置所属系统
					blockCode : comboBlockCode.value,
					// 设置区域名称
					locationName : txtLocationName.getValue()
				};
				searchStore.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});
    }
    // 删除处理
    deleteBtn.handler = function(){
    	var sm = rungrid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		var locationName = [];
		// 未选择记录
		if (selected.length == 0) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.SELECT_NULL_DEL_MSG);
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var record = selected[i];
				if (record.get("locationId")) {
					ids.push(record.get("locationId"));
					locationName.push(record.get("locationName"));
					
				} else {
					store.remove(store.getAt(i));
				}
			}
			// 用户确认是否删除所选记录
			Ext.Msg.confirm(Constants.BTN_DELETE, Constants.DelMsg, function(
					buttonobj) {
					if (buttonobj == "yes") {
						Ext.lib.Ajax.request('POST',
							'workticket/deleteLocation.action', {
								success : function(action) {
									Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.DEL_SUCCESS)
                                   		searchStore.load({
											params : {
												blockCode : comboBlockCode.value ,
												fuzzy : BLANK_STRING,	
												start : 0,
												limit : Constants.PAGE_SIZE
											}
    									});						         	
								},
								failure : function() {
									Ext.Msg.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
								}
							}, 'ids=' + ids);
					}
			});		
		}
    }   
});