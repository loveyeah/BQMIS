// 菜谱类型维护
// author:zhaomingjian
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();

Ext.onReady(function(){
	         //添加与修改共通方法参数
	         var strMethod;
	         //字符串
	         var strallow = "abcdefghgklmnopqlsrzxvtyuwABCDEFGHGKLMNOPQLSRZXVTYUW1234567890";
             //添加按钮
	         var btnAdd = new Ext.Button({
	                id : 'add',
	                text : '新增',
	                iconCls :Constants.CLS_ADD,
	                handler : addRecord
	             });
	          //修改按钮
	          var btnUpdate = new Ext.Button({
	                id: 'update',
	                text : Constants.BTN_UPDATE,
	                iconCls : Constants.CLS_UPDATE,
	                handler : updateRecord
	          });
	          //删除按钮
	          var btnDelete = new Ext.Button({
	                id : 'delete',
	                text : Constants.BTN_DELETE,
	                iconCls : Constants.CLS_DELETE,
	                handler : deleteRecord
	          });
	          //grid所需的类定义
	          var MyGridRecord = new Ext.data.Record.create([{
	                 name : 'menutypeCode'
	                 },{
	          	     name : 'menutypeName'
	          	     },{
	          	     name : 'retrieveCode'
	          	     },{
	          	     name : 'id'}
	          	    ]);
	         // grid中的store定义
	          var gridStore = new Ext.data.Store({
	                proxy : new Ext.data.HttpProxy({ 
	                        url : 'administration/getMenuTypeListInfo.action' 
	                        }),
	                reader : new Ext.data.JsonReader({
	                         root :'list',
	                         totalProperty :'totalCount'
	                         },MyGridRecord)
	                
	         });
	         //初始化时显示所有数据
	         gridStore.load({
	                params :{
	                      start : 0 ,
	                      limit : Constants.PAGE_SIZE
	                }
	         });
	         //判断store是否有数据
	         gridStore.on('load',function(){
	                if(gridStore.getTotalCount()<=0){
	                	btnUpdate.disable();
	                	btnDelete.disable();
	                }else{
	                	btnUpdate.enable();
	                	btnDelete.enable();
	                }
	         });
	         //定义底部工具栏
	         var btmToolbar = new Ext.PagingToolbar({
                        pageSize : Constants.PAGE_SIZE,
                        store : gridStore,
                        displayInfo : true,
                        displayMsg : MessageConstants.DISPLAY_MSG,
                        emptyMsg : MessageConstants.EMPTY_MSG
	         });
	         //定义ColumnModel
	         var gridCM = new Ext.grid.ColumnModel([
	                     // 自动生成行号
	                      new Ext.grid.RowNumberer({
	                          header :'行号',
	                          width : 35
	                      }),{
	                         header : '类别编码',
	                         align : 'left',
	                         sortable :true,
	                         width : 80,
	                         dataIndex : 'menutypeCode'
	                      },{
	                      	 header : '类别名称',
	                      	 align :'left',
	                      	 sortable :true,
	                      	 width :120,
	                      	 dataIndex : 'menutypeName'
	                      },{
	                      	 header :'检索码',
	                      	 align :'left',
	                      	 sortable :true,
	                      	 width : 80,
	                      	 dataIndex : 'retrieveCode'
	                      },{
							hidden : true,
							dataIndex : 'id'
						}
	          ]);
	         //对Grid进行初始化 
	         var mainGrid = new Ext.grid.GridPanel({
	                store : gridStore,
	                cm : gridCM,
	                tbar :[btnAdd,btnUpdate,btnDelete],
	                bbar : btmToolbar,
	                frame : false,
				    border : false,
				    sm : new Ext.grid.RowSelectionModel({
							singleSelect : true
						}),
				    enableColumnHide : true,
				    enableColumnMove : false
	         });
	       // 注册双击事件
	       mainGrid.on("rowdblclick", updateRecord);
	       // 设定布局器及面板
	        new Ext.Viewport({
				enableTabScroll : true,
				layout : "fit",
				items : [mainGrid]
			});
			//******************弹出窗口布局器及界面定义*********************
			//******************form中的文本框定义 ***********************
			//添加面板类型名
			var txtAddTypeName = new Ext.form.TextField({
				id : 'addTypeName',
				name :'strAddTypeName',
				isFormField: true,
			    allowBlank : false,
			    maxLength : 25,
				fieldLabel :"类别名称<font color='red'>*</font>"
			}); 
			//添加面板检索码
			var txtAddRetrieveCode = new Ext.form.TextField({
			    id :'addRetrieveCode',
			    name :'strAddRetrieveCode',
			    isFormField: true,
			    style :'IME_MODE:inactive',
			    allowBlank : true,
			    maxLength : 6,
			    onlyLetter : true,
			    fieldLabel :'检索码',
			    style :{
                     'ime-mode' : 'disabled'
                }
			});
			
			
			//修改面板类型编码
			var txtUpdateTypeCode = new Ext.form.TextField({
			   id :'updateTypeCode',
			   name : 'strUpdateTypeCode',
			   isFormField:true,
			   allowBlank:false,
			   maxLength:50,
			   // 修正UT-BUG-AD001-005
			   readOnly : true,
			   fieldLabel:'类别编码'
			});
			//修改面板类别名称
			var txtUpdateTypeName = new Ext.form.TextField({
				id : 'updateTypeName',
				name :'strUpdateTypeName',
				isFormField: true,
			    allowBlank : false,
			    maxLength : 25,
				fieldLabel :"类别名称<font color='red'>*</font>"
			});
			//修改面板检索
			var txtUpdateRetrieveCode = new Ext.form.TextField({
			    id :'updateRetrieveCode',
			    name :'strUpdateRetrieveCode',
			    isFormField: true,
			    allowBlank : true,
			    onlyLetter : true,
			    style :{ 'ime-mode' : 'disabled'},
			    maxLength : 6,
			    fieldLabel :'检索码'
			    
			});
			//禁用修改类型编码框
//			txtUpdateTypeCode.disable();
			//定义弹出窗体中的addform
			var addFormPanel = new Ext.form.FormPanel({
			    labelAlign:'right',
			    height : 170,
			    border : false,
			    style :"padding-top:0px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
			    frame : true,
			    items:[txtAddTypeName,txtAddRetrieveCode]
			});
			//定义弹出窗口
			var addWindow = new Ext.Window({
			    width : 350,
			    height : 140,
			    title : "添加/修改",
			    layout : 'form',
			    closeAction : 'hide',
			    modal : true,
			    buttonAlign : "center",
			    resizable : false,
			    items :[addFormPanel],
			    buttons :[{
			    	text :Constants.BTN_SAVE,
			    	iconCls :Constants.CLS_SAVE,
			    	handler: confirmRecord
			    },{
			        text :Constants.BTN_CANCEL,
			        iconCls : Constants.CLS_CANCEL,
			        handler: function(){
                                Ext.Msg.confirm(
                                        MessageConstants.SYS_CONFIRM_MSG,
                                        MessageConstants.COM_C_005, function(
                                                buttonobj) {
                                            if (buttonobj == 'yes') {
                                                addWindow.hide();
                                            }
                                        })
                            }
			    }]
			 });
			 //定义修改面板
			 var updateFormPanel = new Ext.form.FormPanel({
			      labelAlign:'right',
			      height : 140,
			      border : true,
			     style :"padding-top:0px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
			     frame : true,
			     items:[txtUpdateTypeCode,txtUpdateTypeName,txtUpdateRetrieveCode]
			 });
			 //定义修改window
			 var updateWindow = new Ext.Window({
			    width : 350,
			    height : 170,
			    title : "添加/修改",
			    layout : 'fit',
			    closeAction : 'hide',
			    modal : true,
			    buttonAlign : "center",
			    resizable : false,
			    items :[updateFormPanel],
			    buttons :[{
			    	text :Constants.BTN_SAVE,
			    	iconCls :Constants.CLS_SAVE,
			    	handler: confirmRecord
			    },{
			        text :Constants.BTN_CANCEL,
			        iconCls : Constants.CLS_CANCEL,
			        handler : function() {
								Ext.Msg.confirm(
										MessageConstants.SYS_CONFIRM_MSG,
										MessageConstants.COM_C_005, function(
												buttonobj) {
											if (buttonobj == 'yes') {
												updateWindow.hide();
											}
										})
							}
			    }]});
			 
	      // *******************************处理****************************************
	      /**
	       * 增加函数
	       */
	       function addRecord(){
	       	    strMethod = "add";
	       	    addFormPanel.getForm().reset();
	       	    addWindow.setTitle("新增菜谱类别");
	         	addWindow.show();
	       }
	       /**
	        * 修改函数
	        */
	       function updateRecord(){
	       	   //选择行
	       	   var choosedRecord = mainGrid.getSelectionModel().getSelected();
	       	   //判断行选择
	       	   if(choosedRecord){
	       	      strMethod = "update";
	       	      var menutypeCode = choosedRecord.get('menutypeCode');
	       	      var id = choosedRecord.get('id');
	       	      var menutypeName = choosedRecord.get('menutypeName');
	       	      var retrieveCode = choosedRecord.get('retrieveCode');
	       	      //修改面板上的菜谱类型编码
	       	      updateFormPanel.getForm().reset();
	       	      txtUpdateTypeCode.setValue(menutypeCode);
	       	      txtUpdateTypeName.setValue(menutypeName);
	       	      txtUpdateRetrieveCode.setValue(retrieveCode);
	       	      updateWindow.setTitle("修改菜谱类别");
	         	  updateWindow.show();
	       	   }else{
	       	   	  Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, MessageConstants.COM_I_001);
	       	   }
	       }

	       /**
	        * 删除函数
	        */
	       function deleteRecord(){
	       	  var  choosedRecord = mainGrid.getSelectionModel().getSelected();
	       	  if(choosedRecord){
	       	  	 var id  = choosedRecord.get('id');
	       	  	 Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, MessageConstants.COM_C_002,
	       	  	             function(buttonobj){
	       	  	             	if(buttonobj == 'yes'){
	       	  	             		 //删除操作
	       	  	             		Ext.Ajax.request({
	       	  	             		     method : Constants.POST,
	       	  	             		     url : 'administration/deleteMenuTypeInfo.action',
	       	  	             		     params :{
	       	  	             		     	id: id
	       	  	             		     },
	       	  	             		     success : function(result,request){
                                            var o = eval("(" + result.responseText + ")");
											if(o.msg =='U'){
												 Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
												 MessageConstants.COM_E_015);
											} else {
										         Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
												    MessageConstants.COM_I_005, function() {
                                                       gridStore.load({
                                                         params : {
                                                            start : 0,
                                                            limit : Constants.PAGE_SIZE
                                                          }
                                                       });
                                                    mainGrid.getView().refresh();
                                                 });
									             }
	       	  	             		     },
	       	  	             		     failure : function() {
									            Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
											    MessageConstants.DEL_ERROR,function(){});
								         }
	       	  	             		});
	       	  	             	}
	       	  	             });
	       	  	
	       	  }else{
	       	  	 Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, MessageConstants.COM_I_001);
	       	  }
	       }
	       /**
	        * 按下保存按钮动作
	        */
	       function confirmRecord(){
	       	    if(strMethod == 'add'){
	       	    	if(addCheckform()){
	       	    		Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, MessageConstants.COM_C_001,pressYesButton);	 	   
	       	    	}else{
	       	    		Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, strMsg);
	       	    		return;
	       	    	}
	       	    }
	       	    if(strMethod == 'update'){
	       	    	if(updateCheckform()){
	       	    		Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, MessageConstants.COM_C_001,pressYesButton);	 	  
	       	    	}else{
	       	    		Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, strMsg);
	       	    		return;
	       	    	}
	       	    }
	       }
	       /**
	        * 按下YES按钮动作
	        */
	       function  pressYesButton(buttonobj){
	            //判断用户所按按钮值
	            if(buttonobj == 'yes'){
	            	var myurl = "";
	            	if(strMethod == 'add'){
	            			myurl  ='administration/addMenuTypeInfo.action';
	            			addFormPanel.getForm().submit({
	            				method : Constants.POST,
	            				url : myurl,
	            				success : function(form,action){
	            	                Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,MessageConstants.COM_I_004,
                                        function (){
                                           gridStore.load({
                                              params : {
                                                    start : 0,
                                                    limit : Constants.PAGE_SIZE
                                              }
                                           });
                                           addWindow.hide();
                                           mainGrid.getView().refresh();
                                        });	            					
	            				},
							    failure : function() {
							    	
								}
	            			});
	            		}
	            	if(strMethod == 'update'){
	            		var  choosedRecord = mainGrid.getSelectionModel().getSelected();
	            		
	            			var id  = choosedRecord.get('id');
	            			var menutypeCode = choosedRecord.get('menutypeCode');
                           
	            			myurl =  'administration/updateMenuTypeInfo.action';
	            			updateFormPanel.getForm().submit({
	            			     method : Constants.POST,
	            			     url : myurl,
	            			     params:{
	            			     	id : id
	            			     },
	            			     success : function(form,action){
	            			     	Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,MessageConstants.COM_I_004,
                                        function() {
                                        gridStore.load({
                                              params : {
                                                    start : 0,
                                                    limit : Constants.PAGE_SIZE
                                              }
                                    });
                                    updateWindow.hide();
                                    mainGrid.getView().refresh();});
	            			     },
	            			     failure : function(){
	            			     }
	            			});
	            	}	
	            	}
	            	
	            }
	   
	      /**
	       * add数据进行验证
	       */
	      function addCheckform(){
	      	   strMsg = "";
	      	   //判断类型名字是否为空
	      	   var typeName = Ext.get("addTypeName").dom.value;
	      	   
	      	    if((typeName == null) || (typeName.trim() == ""))
	      	    {
	      	    	strMsg = String.format(MessageConstants.COM_E_002, '类别名称');
	      	    }
	           if (strMsg == "") {
			       return true;
		       } else {
			       return false;
		       }
	      	   
	      }
	      /**
	       * 修改
	       */
	       function updateCheckform(){
	      	   strMsg = "";
	      	   var typeName = Ext.get("updateTypeName").dom.value;
	      	    if((typeName == null) || (typeName.trim() == ""))
	      	    {
	      	    	strMsg = String.format(MessageConstants.COM_E_002, '类别名称');
	      	    }
	           if (strMsg == "") {
			       return true;
		       } else {
			      return false;
		       }
	      }
	     
});