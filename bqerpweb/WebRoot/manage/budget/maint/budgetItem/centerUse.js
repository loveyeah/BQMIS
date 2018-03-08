Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	 var args = window.dialogArguments;
	 var itemIds = args.itemId;
	 
	
	 
	var centerRecord = Ext.data.Record.create([{
		        name : 'useId',
				mapping:0

			},
			{
				name : 'depName',
				mapping:1

			},{   
				name : 'depCode',
				mapping:2

			},{   
				name : 'centerId',
				mapping:3

			},{   
				name : 'isUse',
				mapping:4

			},{   
				name : 'itemId',
				mapping:5

			}]);

	 var centerProxy = new Ext.data.HttpProxy({
				url : 'managebudget/queryCenterUse.action'
			});

	var centerReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, centerRecord);
	var centerStore = new Ext.data.Store({
				proxy : centerProxy,
				reader : centerReader
			});
/*	centerStore.load({
					params : {
						start : 0,
						limit : 18
					}
				});*/
		// 查询
	function queryRecord() {
	
		// modified by liuyi 20100601
		centerStore.baseParams = {
			itemId : itemIds
		}
		centerStore.load({
					params : {
						start : 0,
						limit : 18//,
//						fuzzytext : fuzzy.getValue()
					}
				});
	}			
	queryRecord();
	 var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false
			});
			
				
			var centerButtons = new Ext.Toolbar({
				items : [{
							id : 'btnAdd',
							iconCls : 'add',
							text : "新增",
							minWidth : 70,
							//disabled : true,
							handler : addcenterTheme
						}, '-', {
							id : 'btnDelete',
							iconCls : 'delete',
							minWidth : 70,
							//disabled : true,
							text : "删除",
							handler : deletecenterTheme
						}, '-', {
							id : 'btnSave',
							iconCls : 'save',
							text : "保存",
							minWidth : 70,
							handler : savecenterTheme
						}]});	
       var gridCenter=  new Ext.grid.EditorGridPanel({
				layout : 'fit',
				sm : sm,
				
				enableColumnMove : false,
				store : centerStore,
				width:'30%',
				columns : [sm, new Ext.grid.RowNumberer({
									header : "序号",
									width : 35
								}),{
								  header:"id",
								  sortable:true,
								  dataIndex:'useId',
								  hidden:true
								},{
								  header:"指标Id",
								  sortable:true,
								  dataIndex:'itemId',
								  hidden:true
								},
							      {
								  header:"部门id",
								  sortable:true,
								  dataIndex:'centerId',
								  hidden:true
								},{
								  header:"是否使用",
								  sortable:true,
								  dataIndex:'isUse',
								  hidden:true
								}, {
								  header:"部门",
								  width:150,
							      align : "left",
							      sortable : false,
							      css : CSS_GRID_INPUT_COL,
								  dataIndex:'depName',
								  css : CSS_GRID_INPUT_COL
//								  ,editor : new Ext.form.TextField({
//								  style : 'cursor:pointer',
//										allowBlank : false
//							})
								},{
							header : "部门编码",
							sortable : true,
							dataIndex : 'depCode',
							width : 200,
							css : CSS_GRID_INPUT_COL
//							,editor : new Ext.form.TextField({
//								style : 'cursor:pointer',
//								allowDecimals :false,
//								allowBlank : true
//							})
						}],
		tbar : centerButtons,
//		clicksToEdit : 1,		
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : centerStore,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
		,autoSizeColumns : true
			});
	
	gridCenter.on('celldblclick', choseEdit);
			function choseEdit(grid, rowIndex, columnIndex, e) {
				if (rowIndex <= grid.getStore().getCount() - 1) {
					var record = grid.getStore().getAt(rowIndex);
					var fieldName = grid.getColumnModel()
							.getDataIndex(columnIndex);
					if (fieldName == "depName") { 
						  var args = {
                  			  selectModel : 'single',
                  			  flag:'yes',
                   			  rootNode : {
                     	      id : '0'
                    },
                    onlyLeaf : false
                };
						var rec = window
								.showModalDialog(
										'../../../../comm/jsp/hr/dept/dept.jsp',
										args,
										'dialogWidth:500px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
						if (typeof(rec) != "undefined") {
							record.set('depCode', rec.codes);
							record.set('depName', rec.names);
							//record.set('depName', rec.itemName);
							record.set('centerId', rec.ids);
						}
					}

				}
			}

			
	function addcenterTheme() {
				var count = centerStore.getCount();
				var currentIndex = count;
				var o = new centerRecord({
							'depName' : '',
							'depCode' : ''
						});
				gridCenter.stopEditing();
				centerStore.insert(currentIndex, o);
			    sm.selectRow(currentIndex);
				gridCenter.startEditing(currentIndex, 1);
/*
		var count = contentStore.getCount();
		var currentIndex = count;
		var o = new centerRecord({
						'content' : ''
					});
		gridCenter.stopEditing();
		contentStore.insert(currentIndex, o);
		gridCenter.startEditing(currentIndex, 1);
		gridCenter.getView().refresh();*/
	};
	
	
		// 删除记录
			var ids = new Array();
			function deletecenterTheme() {
				gridCenter.stopEditing();
				var sm = gridCenter.getSelectionModel();
				var selected = sm.getSelections();
				if (selected.length == 0) {
					Ext.Msg.alert("提示", "请选择要删除的记录！");
				} else {
					for (var i = 0; i < selected.length; i += 1) {
						var member = selected[i];
						if (member.get("useId") != null) {
							ids.push(member.get("useId"));
						}
						gridCenter.getStore().remove(member);
						gridCenter.getStore().getModifiedRecords().remove(member);
					}
				}
			}

	
	function savecenterTheme() {
		gridCenter.stopEditing();
		var modifyRec = gridCenter.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
	    Ext.Msg.confirm('提示信息', '确定要保存修改吗？', function(button) {
				if (button == 'yes') {
					var addData = new Array();
					var updateData = new Array();
					//var dciIds = '';
					for (var i = 0; i < modifyRec.length; i++) {
						if (modifyRec[i].get('depName') == null
								|| modifyRec[i].get('depName') == "") {
							Ext.Msg.alert('提示信息', '部门名称不可为空，请选择！')
							return;
						}
						if (modifyRec[i].get('isUse') == 'Y') {
							updateData.push(modifyRec[i].data);
						} else {
							addData.push(modifyRec[i].data);
						}
					}
//					itemIds = itemIds.substring(0,itemIds.length - 1);
//					dciIds = dciIds.substring(0,dciIds.length - 1);
					Ext.Ajax.request({
								url : 'managebudget/saveCenterUse.action',
								method : 'post',
								params : {
									isAdd : Ext.util.JSON.encode(addData),
									isUpdate : Ext.util.JSON.encode(updateData),
									isDelete : ids.join(","),
									itemId:itemIds
								},
								success : function(result, request) {
									var res = eval('('+result.responseText+')');
								   if (res.existFlag == true) {
							        Ext.Msg.alert("提示", "该部门名称已存在 ,请重新输入!");
							        centerStore.rejectChanges();
							        centerStore.reload();
									}else{
									Ext.Msg.alert('提示信息', '数据保存修改成功！')
									centerStore.rejectChanges();
									ids = [];
									centerStore.reload();
									}
								},
								failure : function(result, request) {
									Ext.Msg.alert("提示信息", "数据保存修改失败！");
									centerStore.rejectChanges();
									ids = [];
									centerStore.reload();
								}
							})
				}
			})

		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}
    	var rewritePanel = new Ext.Viewport({
				layout : 'border',
				border:false,
				items : [{
					region : 'center',
							split : true,
							layout : 'fit',
							collapsible : true,
							border : false,
							autoScroll : true,
					
	        /*    region : 'center',
			    height : 400,
			    width: 500,*/
			    items : [gridCenter]}],
			    defaults : {
                autoScroll : true
        }
	}); 
  });