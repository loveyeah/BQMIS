Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	    Ext.QuickTips.init();
	    // 原价格
	    var strOldPrice;

	    // 序号
	    var serialNum = new Ext.form.Hidden({
	         id : "serialNum",
	         name : "adJRoomPrice.id"
	    })

	    // 房间类别 ComboBox
		var drpRoomType = new Ext.form.CmbRoomType({
				fieldLabel : "房间类别<font color='red'>*</font>",
				id : "room",
				displayField : "text",
				valueField : "value",
				anchor : '85%'
			});

		drpRoomType.on('render', initRoomType);
		drpRoomType.on("select", changeHandler);

		// 隐藏的房间类别code
	   var cmbCodeHidden = new Ext.form.Hidden({
	   	  id : "roomTypeCode",
	   	  name : "adJRoomPrice.roomTypeCode"
	   })

	  // 隐藏的房间类别名称
	  var cmbNameHidden = new Ext.form.Hidden({
	   	  id : "roomTypeName",
	   	  name : "adJRoomPrice.roomTypeName"
	   })

	    // 价格
	    var txtPrice = new Ext.form.MoneyField ({
	    	id : "price",
	    	anchor : "85%",
	    	maxLength : 16,
	    	appendChar : '元',
	    	fieldLabel : "价格",
	    	padding : 2,
	    	decimalPrecision : 2
	    });
	    txtPrice.on("change", changePriceHandler);

	    // 价格隐藏
	    var priceHidden = new Ext.form.Hidden({
	    	id : "priceHidden",
	    	name : "adJRoomPrice.price"
	    });

	    // 保存button
	    var btnSave = new Ext.Button({
	      		text : Constants.BTN_SAVE,
        		iconCls : Constants.CLS_SAVE,
                handler : saveHandler
	    });

	    // formPanel
	    var myaddpanel = new Ext.FormPanel({
			region : "center",
			labelWidth : 65,
			labelAlign : 'right',
			buttonAlign : "center",
			height : 100,
            border : false,
			items :[serialNum,drpRoomType,txtPrice,priceHidden,cmbCodeHidden,cmbNameHidden],
			buttons : [btnSave]
		});

		// 左布局
		var panWest = new Ext.Panel({
	        region : 'west',
	        layout : 'fit',
	        width : '30%',
	        items : "",
	        border : false
	    });

	    // 右布局
	    var panEast= new Ext.Panel({
	        region : 'east',
	        layout : 'fit',
	        width : '30%',
	        items : "",
	        border : false
	    });

	    // 上面布局
	    var panTop = new Ext.Panel({
	        region : 'north',
	        layout : 'fit',
	        items : "",
	        height :150,
	        border : false
	    });

	    // 中间布局
	    var panCenter = new Ext.Panel({
	        region : 'center',
	        layout : 'form',
	        items : [myaddpanel],
	        border : false
	    });

	    // 下面布局
	    var panBottom = new Ext.Panel({
	        region : 'south',
	        layout : 'fit',
	        items : "",
	        height :'35%',
	        border : false
	    });

	    // 总布局
	 	var viewPort = new Ext.Viewport({
	        enableTabScroll : true,
	        layout : "border",
	        items : [panTop,panWest, panCenter, panEast,panBottom]
	    });

		// 初始化房间类别
		function initRoomType() {
			drpRoomType.setValue(this.store.getAt(0).data.value);
			changeHandler();
		}
		
		// 改变时重新load价格
		function changeHandler() {
			var strValue = drpRoomType.getValue()
			// 房间类别code
			 cmbCodeHidden.setValue(drpRoomType.getValue());
			 // 房间类别名称
			cmbNameHidden.setValue(Ext.get("room").dom.value);
			if (strValue != null && strValue != "") {
					Ext.Ajax.request({
		                url : 'administration/getRoomPrice.action',
		                method : Constants.POST,
		                params : {
		                    roomTypeCode : strValue
		                },
		                success : function(result, request) {
		                	if (result.responseText != "") {
		                		var myData = eval("(" + result.responseText + ")");
							    if (myData != null && myData != "") {
								  	// 设置价格
								  	if (myData.price != null) {
								  		txtPrice.setValue(myData.price);
								  		strOldPrice = myData.price;
								  		priceHidden.setValue(myData.price);
								  	}
								  	// 设置序号
								     if (myData.id != null) {
								     		serialNum.setValue(myData.id);
								     }
							  }
		                	} else {
							  		txtPrice.setValue("0");
							  		strOldPrice = "0";
								  	priceHidden.setValue("0");
								  	serialNum.setValue("");
		                	}		                   
		                },
		                failure : function() {
		                	Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, MessageConstants.UNKNOWN_ERR);
		                }
		    });
			}
		}
		
		// 价格变化
		function changePriceHandler() {
			var strPriceValue = txtPrice.getValue();
			if (strPriceValue == "" || strPriceValue == null) {
			  	txtPrice.setValue("0");
				priceHidden.setValue(strPriceValue);
			}
			txtPrice.setValue(strPriceValue);
			priceHidden.setValue(strPriceValue);
		}
		
		// 保存操作
		 function saveHandler() {
			if(strOldPrice != txtPrice.getValue()) {
				if(Ext.get("price").dom.value.length <= 20) {
				    Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, MessageConstants.COM_C_001, function(buttonobj) {
						if (buttonobj == "yes") {
			    	       // 提交表单
					        myaddpanel.getForm().submit({
					            method : Constants.POST,
					            waitMsg : Constants.DATA_SAVING,
					            url : 'administration/updateRoomPrice.action',
					            success : function(form, action) {
									var result = eval("(" + action.response.responseText + ")");
									if (result.success == true) {
										// 重新load
					      				changeHandler();
					      				// 保存成功
										Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
													MessageConstants.COM_I_004);
											return;
										}
						            },
						            faliue : function() {
						            }
						      });
						}
				 });
				}
		 } else {
			 Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
			MessageConstants.AM005_E_001);
		 }
		}
})