Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var MyRecord = Ext.data.Record.create([{
				name : 'messageId'
			}, {
				name : 'userCode'
			}, {
				name : 'userPsw'
			}, {
				name : 'msgPerson'
			}, {
				name : 'msgName'
			}, {
				name : 'msgContent'
			}, {
				name : 'isSend'
			}, {
				name : 'entryBy'
			}, {
				name : 'entryName'
			}, {
				name : 'entryDate'
			}]);

	var DataProxy = new Ext.data.HttpProxy({
				url : 'com/getHrJMessageList.action'
			});

	var TheReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, MyRecord);

	var store = new Ext.data.Store({
				proxy : DataProxy,
				reader : TheReader
			});


	var sm = new Ext.grid.CheckboxSelectionModel();

	function query() {
			store.load({
						params : {
							start : 0,
							limit : 18
						}
					})
	}


	var ids = new Array();
	function deleteSms(){
		var sm = grid.getSelectionModel();
		var selections = sm.getSelections();
		if (selections.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
				for (var i = 0; i < selections.length; i += 1) {
				var member = selections[i];
				if (member.get("messageId") != null) {
					ids.push(member.get("messageId"));
				}
			}
			Ext.Msg.confirm('提示', '是否确定删除该短信通知记录？', function(response) {
				if (response == 'yes') {
					Ext.Ajax.request({
								method : 'post',
								url : 'com/deleteMessage.action',
								params : {
									ids : ids.join(",")
								},
								success : function(action) {
									Ext.Msg.alert("提示", "删除成功！");
									store.reload();
								},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
							});
				}
			});
		}
	
	}
	
	function updateSms(){
	
		var member = grid.getSelectionModel().getSelected();
		var selections = grid.getSelectionModel().getSelections();	
		if (member) {
			if(selections.length>1){
			    Ext.Msg.alert("提示", "请先选择一行记录进行编辑!");
			    return;
			  }
			else {
						tabPanel.setActiveTab(0);
						messageRegister.setDetail(member);
						messageRegister.setMessageId(member.get("messageId"));
					}
			
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	
	}
	
	var sendIds = new Array();
	var sendPersons=new Array();
	var sendContents=new Array();
	
	function sendSms(){
	var member = grid.getSelectionModel().getSelected();
	var selections = grid.getSelectionModel().getSelections();
	if (member) {
		 	for (var i = 0; i < selections.length; i += 1) {
				var member = selections[i];
				if (member.get("isSend") == 'Y') {
					Ext.Msg.alert("提示", "请选择未发送的行进行发送!");
					return;
				}
				else{
					sendIds.push(member.get("messageId"));
					sendPersons.push(member.get("msgPerson"));
					sendContents.push(member.get("msgContent"));
				}
			}
			 Ext.Msg.confirm('提示', '是否确定发送该短信通知记录？', function(response) {
				if (response == 'yes') {
					Ext.Ajax.request({
								method : 'post',
								url : 'com/sendMessage.action',
								params : {
									ids : sendIds.join(";"),
									sendPersons:sendPersons.join(";"),
									sendContents:sendContents.join(";")
								},
								success : function(action) {
									Ext.Msg.alert("提示", "短信通知发送成功！");
									store.reload();
								},
								failure : function() {
									Ext.Msg.alert('错误', '发送时出现未知错误.');
								}
							});
				}
			});
		} else {
			Ext.Msg.alert("提示", "请先选择要发送的行!");
		}
	}
	
	var grid = new Ext.grid.GridPanel({
		        id:'div_grid',
				region : "center",
				layout : 'fit',
				store : store,
				columns : [sm, new Ext.grid.RowNumberer({
									header : '行号',
									width : 35,
									align : 'left'
								}), {
							header : "ID",
							width : 35,
							sortable : true,
							dataIndex : 'messageId',
							hidden : true
						}, {
							header : "通知人员",
							width : 150,
							allowBlank : false,
							sortable : true,
							dataIndex : 'msgName'
						}, {
							header : "通知内容",
							width : 150,
							allowBlank : false,
							sortable : true,
							dataIndex : 'msgContent'
						}, {
							header : "填写人",
							width : 150,
							allowBlank : false,
							sortable : true,
							dataIndex : 'entryName'
						}, {
							header : "填写时间",
							width : 150,
							allowBlank : false,
							sortable : true,
							dataIndex : 'entryDate'
						}],
				sm : sm,
				autoSizeColumns : true,
				viewConfig : {
					forceFit : true
				},
				tbar : [ {
							text : "查询",
							iconCls : 'query',
							handler : query
						},{
							text : "增加",
							iconCls : 'add',
							handler : function() {
								tabPanel.setActiveTab(0);
								messageRegister.clearForm();
							}
						},{
							text : "删除",
							iconCls : 'delete',
							handler : deleteSms
						},{
							text : "修改",
							iconCls : 'update',
							handler : updateSms
						},{
							text : "发送",
							iconCls : 'send',
							handler : sendSms
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
	
grid.on('rowdblclick', updateSms);

	var messageRegister = new message.smsFill();

	var tabPanel = new Ext.TabPanel({
		        id:'div_tabs',
				renderTo : document.body,
				activeTab : 0,
				tabPosition : 'bottom',
				plain : true,
				defaults : {
					autoScroll : true
				},
				frame : false,
				border : false,
				items : [{
							id : 'tab1',
							layout : 'fit',
							title : '短信发送填写',
							items : [messageRegister.form]
						}, {
							id : 'tab2',
							layout : 'fit',
							title : '短信发送列表',
							items : [grid]
						}]
			});

	
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
							items : [tabPanel]
						}]
			});
			
query() 
	});
