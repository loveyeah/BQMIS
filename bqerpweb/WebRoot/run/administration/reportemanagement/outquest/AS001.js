// 内部签报申请
// author:sufeiyu

Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.QuickTips.init();

Ext.onReady(function() {
	var strMethod;
	var today;

	// 签报申请--------------------------------------------------
	// 定义选择列
	var smQuest = new Ext.grid.RowSelectionModel({
				singleSelect : true
			});

	// 数据源--------------------------------
	var recordQuest = Ext.data.Record.create([{
				name : 'id'
			}, {
				name : 'applyId'
			}, {
				name : 'applyMan'
			}, {
				name : 'applyManName'
			}, {
				name : 'applyManDeptName'
			}, {
				name : 'dcmStatus'
			}, {
				name : 'applyTopic'
			}, {
				name : 'applyText'
			}, {
				name : 'applyDate'
			}, {
				name : 'reportId'
			}, {
				name : 'checkedMan'
			}, {
				name : 'updateTime'
			}, {
				name : 'appType'
			}]);

	// 定义获取数据源
	var proxyQuest = new Ext.data.HttpProxy({
				url : 'administration/getOutQuest.action'
			});

	// 定义格式化数据
	var readerQuest = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, recordQuest);

	// 定义封装缓存数据的对象
	var storeQuest = new Ext.data.Store({
				// 访问的对象
				proxy : proxyQuest,
				// 处理数据的对象
				reader : readerQuest
			});

	// --gridpanel显示格式定义-----开始-------------------

	// 增加按钮
	var btnAdd = new Ext.Button({
				text : Constants.BTN_ADD,
				iconCls : Constants.CLS_ADD,
				handler : addQuest
			});

	// 修改按钮		
	var btnUpdate = new Ext.Button({
				text : Constants.BTN_UPDATE,
				iconCls : Constants.CLS_UPDATE,
				handler : updateQuest
			});

	// 删除按钮		
	var btnDelete = new Ext.Button({
				text : Constants.BTN_DELETE,
				iconCls : Constants.CLS_DELETE,
				handler : deleteQuest
			});

	// 上报按钮		
	var btnReport = new Ext.Button({
				text : Constants.BTN_REPOET,
				iconCls : Constants.CLS_REPOET,
				handler : reportQuest
			});

	// 初始化数据		
	storeQuest.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			});

	var cmQuest = new Ext.grid.ColumnModel([
			// 自动行号
			new Ext.grid.RowNumberer({
						header : "行号",
						width : 31
					}), {
				header : "序号",
				hidden : true,
				dataIndex : 'id'
			}, {
				header : "申请单号",
				hidden : true,
				dataIndex : 'applyId'
			}, {
				header : "申报人",
				hidden : true,
				dataIndex : 'applyMan'
			}, {
				header : "签报编号",
				hidden : true,
				dataIndex : 'reportId'
			}, {
				header : "单据状态",
				hidden : true,
				dataIndex : 'dcmStatus'
			}, {
				header : "呈报内容",
				hidden : true,
				dataIndex : 'applyText'
			}, {
				header : "核稿人",
				hidden : true,
				dataIndex : 'checkedMan'
			}, {
				header : "修改时间",
				hidden : true,
				dataIndex : 'updateTime'
			}, {
				header : "签报种类",
				hidden : true,
				dataIndex : 'appType'
			}, {
				header : "申报人",
				sortable : true,
				dataIndex : 'applyManName'
			}, {
				header : "呈报部门",
				sortable : true,
				dataIndex : 'applyManDeptName'
			}, {
				header : "呈报主题",
				sortable : true,
				dataIndex : 'applyTopic'
			}, {
				header : "呈报日期",
				sortable : true,
				dataIndex : 'applyDate'
			}])

	// 头部工具栏					
	var tbarQuest = new Ext.Toolbar([btnAdd, btnUpdate, btnDelete, btnReport])

	// 分页
	var bbarQuest = new Ext.PagingToolbar({
				pageSize : Constants.PAGE_SIZE,
				store : storeQuest,
				displayInfo : true,
				displayMsg : MessageConstants.DISPLAY_MSG,
				emptyMsg : MessageConstants.EMPTY_MSG
			})

	var gridQuest = new Ext.grid.GridPanel({
				layout : 'fit',
				store : storeQuest,
				cm : cmQuest,
				sm : smQuest,
				autoScroll : true,
				enableColumnMove : false,
				autoSizeColumns : true,
				viewConfig : {
					forceFit : false
				},
				// 头部工具栏
				tbar : tbarQuest,

				// 分页
				bbar : bbarQuest
			});
	// --gridpanel显示格式定义-----结束--------------------

	// 注册双击事件
	gridQuest.on("rowdblclick", updateQuest);

	//-------------------弹出窗口-------------------------
	// -------------------签报申请详细开始--------------------------------	
	// 数据源--------------------------------
	var recordApplyId = Ext.data.Record.create([{
				name : 'strWorkerCode'
			}, {
				name : 'strWorkerCodeName'
			}, {
				name : 'deptName'
			}, {
				name : 'strRes'
			}, {
				name : 'applyId'
			}]);

	// 定义获取数据源
	var proxyApplyId = new Ext.data.HttpProxy({
				url : 'administration/getNewApplyId.action'
			});

	// 定义格式化数据
	var readerApplyId = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, recordApplyId);

	// 定义封装缓存数据的对象
	var storeApplyId = new Ext.data.Store({
				// 访问的对象
				proxy : proxyApplyId,
				// 处理数据的对象
				reader : readerApplyId
			});

	// 序号
	var hdId = {
	     id : 'id',
	     name : 'quest.id',
	     xtype : 'hidden',
	     readOnly : true,
		 hidden : true,
	     value :　'0'
	}		
			
	// 编号
	var txtaApplyId = new Ext.form.TextField({
				id : 'strRes',
				name : 'quest.reportId',
				fieldLabel : '编号',
				//height : 18,
				anchor : '100%',
				readOnly : true,
				isFormField : true,
				width : 120
			})
			
	// 编号
	var hidApplyId = {
				id : 'applyId',
				name : 'quest.applyId',
				hidden : true,
				readOnly : true,
		        xtype : 'hidden',
		        value : '0'
			}		

	// 第一行
	var fldSetFirstLineQuest = new Ext.form.FieldSet({
				border : false,
				layout : 'column',
				style : "padding-top:0px;padding-bottom:0px;border:0px",
				//height : 21,
				anchor : '100%',
				items : [{
							columnWidth : 0.5,
							border : false,
							layout : 'form',
							items : [txtaApplyId,hidApplyId]
						}]
			})

	// 申报人code
	var hdApplyMan = {
		id : 'applyMan',
		readOnly : true,
		xtype : 'hidden',
		hidden : true,
		value : '0',
		name : 'quest.applyMan'
	}
	
	// 申报人code
	var hdUpdateTime = {
		id : 'updateTime',
		readOnly : true,
		xtype : 'hidden',
		hidden : true,
		value : '0'
	}

	// 申报人
	var txtApplyManName = new Ext.form.TextField({
				id : 'applyManName',
				name : 'quest.applyManName',
				fieldLabel : '申报人',
				//height : 18,
				anchor : '100%',
				readOnly : true,
				isFormField : true,
				width : 120
			})

	// 呈报部门
	var txtApplyManDeptName = new Ext.form.TextField({
				id : 'applyManDeptName',
				name : 'quest.applyManDeptName',
				fieldLabel : '呈报部门',
				//height : 18,
				anchor : '100%',
				readOnly : true,
				isFormField : true,
				width : 120
			})

	// 数据源初期化操作
	storeApplyId.on('load', function() {
		        Ext.get('applyId').dom.value = storeApplyId.getAt(0).get('applyId');
//				txtaApplyId.setValue(storeApplyId.getAt(0).get('strRes'));
				txtApplyManName.setValue(storeApplyId.getAt(0)
						.get('strWorkerCodeName'));
				txtApplyManDeptName.setValue(storeApplyId.getAt(0)
						.get('deptName'));
			})

	// 第二行
	var fldSetSecondLineQuest = new Ext.form.FieldSet({
				border : false,
				layout : 'column',
				style : "padding-top:0px;padding-bottom:0px;border:0px",
				//height : 20,
				anchor : '100%',
				items : [{
							columnWidth : 0.5,
							border : false,
							layout : 'form',
							items : [txtApplyManName]
						}, {
							columnWidth : 0.5,
							border : false,
							layout : 'form',
							items : [txtApplyManDeptName]
						}]
			})

	// 呈报日期
	var txtApplyDate = new Ext.form.TextField({
				id : 'applyDate',
				name : 'quest.applyDate',
				fieldLabel : '呈报日期',
				readOnly : true,
				width : 120,
				anchor : '100%'
			});

	// 核稿人
	var txtCheckedMan = new Ext.form.TextField({
				id : 'checkedMan',
				name : 'quest.checkedMan',
				fieldLabel : '核稿人',
				//height : 18,
				anchor : '100%',
				isFormField : true,
				width : 120,
				maxLength : 10
			})

	// 第三行
	var fldSetThirdLineQuest = new Ext.form.FieldSet({
				border : false,
				layout : 'column',
				style : "padding-bottom:0px;border:0px",
				//height : 20,
				anchor : '100%',
				items : [{
							columnWidth : 0.5,
							border : false,
							layout : 'form',
							items : [txtApplyDate]
						}, {
							columnWidth : 0.5,
							border : false,
							layout : 'form',
							items : [txtCheckedMan]
						}]
			})

	// 呈报主题
	var txtApplyTopic = new Ext.form.TextField({
				id : 'applyTopic',
				name : 'quest.applyTopic',
				fieldLabel : '主题',
				//height : 18,
				anchor : '100%',
				isFormField : true,
				width : 337,
				maxLength : 100
			})

	// 第四行
	var fldSetFouthLineQuest = new Ext.form.FieldSet({
				border : false,
				layout : 'form',
				style : "padding-bottom:0px;border:0px",
				//height : 20,
				anchor : '100%',
				items : [txtApplyTopic]
			})

	// 呈报内容
	var txaApplyText = new Ext.form.TextArea({
				fieldLabel : '内容',
				id : 'applyText',
				name : 'quest.applyText',
				height : 33,
				isFormField : true,
				anchor : '99.5%',
				width : 335,
				maxLength : 2000
			});

	// 第五行
	var fldSetFifthLineQuest = new Ext.form.FieldSet({
				border : false,
				layout : 'form',
				style : "padding-bottom:0px;border:0px",
				height : 40,
				anchor : '100%',
				items : [txaApplyText]
			})

	// fieldSet
	var fldSetDetail = new Ext.form.FieldSet({
				title : '内部签报申请详细',
				frame : true,
				labelAlign : 'right',
				height : 216,
				labelWidth : 55,
				style : "padding-top:0px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
				items : [fldSetFirstLineQuest, fldSetSecondLineQuest,
						fldSetThirdLineQuest, fldSetFouthLineQuest,
						fldSetFifthLineQuest]
			})
			
	// formpanel
	var formpanelDetail = new Ext.form.FormPanel({
		layout : 'fit',
	    frame : false,
	    style : "padding-top:0px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
	    width : 460,
	    height : 222,
	    border : false,
		items : [fldSetDetail, hdApplyMan, hdId, hdUpdateTime]	
	})
	// -------------------签报申请详细结束--------------------------------		

	// -------------------签报批复后抄送人--------------------------------	
	// 抄送人下拉列表
	var drpReaderMan = new Ext.form.CmbCCUser({
	    id : 'drpMan' 
	});
	
	// 数据源--------------------------------
	var recordReaderManName = Ext.data.Record.create([{
				name : 'strWorkerCodeName'
			}]);

	drpReaderMan.on('select', function() {
        // xsTan 修改开始 2009-1-22 对应抄送人员DRP显示Bug
             var strSelectValue = drpReaderMan.getValue();
             var intIndex = drpReaderMan.store.find('workerCode', strSelectValue);
	         var rec = gridQuestReader.getSelectionModel().getSelected();
             var recReadMan = drpReaderMan.store.getAt(intIndex);
	         rec.data.readManReader = strSelectValue;
             rec.data.readManNameReader = recReadMan == undefined
						? ""
						: recReadMan.get("name");
       // xsTan 修改结束 2009-1-22      
	         gridQuestReader.getView().refresh();
	    })

	// 定义选择列
	var smQuestReader = new Ext.grid.RowSelectionModel({
				singleSelect : true
			});

	// 数据源--------------------------------
	var recordQuestReader = Ext.data.Record.create([{
				name : 'idReader'
			}, {
				name : 'applyIdReader'
			}, {
				name : 'readManReader'
			}, {
				name : 'updateTimeReader'
			}, {
				name : 'readManNameReader'
			}]);

	// 定义获取数据源
	var proxyQuestReader = new Ext.data.HttpProxy({
				url : 'administration/getQuestReader.action'
			});

	// 定义格式化数据
	var readerQuestReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, recordQuestReader);

	// 定义封装缓存数据的对象
	var storeQuestReader = new Ext.data.Store({
				// 访问的对象
				proxy : proxyQuestReader,
				// 处理数据的对象
				reader : readerQuestReader
			});

	// --gridpanel显示格式定义-----开始-------------------

	// 新增明细按钮
	var btnAddDetail = new Ext.Button({
				text : "新增明细",
				handler : addQuestReader
			});

	// 取消明细按钮		
	var btnCancelDetail = new Ext.Button({
				text : "取消明细",
				handler : cancelQuestReader
			});

	// 删除明细按钮		
	var btnDeleteDetail = new Ext.Button({
				text : "删除明细",
				handler : deleteQuestReader
			});

	var cmQuestReader = new Ext.grid.ColumnModel([
			// 自动行号
			new Ext.grid.RowNumberer({
						header : "行号",
						width : 31
					}), {
				header : "序号",
				hidden : true,
				dataIndex : 'idReader'
			}, {
				header : "申请单号",
				hidden : true,
				dataIndex : 'applyIdReader'
			}, {
				header : "抄送人员code",
				hidden : true,
				dataIndex : 'readManReader'
			}, {
				id : 'readerManName',
				header : "抄送人员",
				sortable : true,
				editor : drpReaderMan,
        // xsTan 修改开始 2009-1-22
//              renderer :  getNameByCode ,
        // xsTan 修改结束 2009-1-22
				dataIndex : 'readManNameReader'
			}, {
				header : "修改时间",
				hidden : true,
				dataIndex : 'updateTimeReader'
			}])

	// 头部工具栏					
	var tbarQuestReader = new Ext.Toolbar([btnAddDetail, btnDeleteDetail,
			btnCancelDetail])

    var gridQuestReader = new Ext.grid.EditorGridPanel({
		store : storeQuestReader,
		cm : cmQuestReader,
		sm : smQuestReader,
//		anchor : '100%',
		border : false,
//		fitToPanel : true,
		style : "padding-bottom:0px",
		autoScroll : true,
		clicksToEdit : 1,
		enableColumnMove : false,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : false
		},
		// 头部工具栏
		tbar : tbarQuestReader

	});
	// --gridpanel显示格式定义-----结束--------------------

	// fieldSet
	var fldSetReader = new Ext.form.FieldSet({
				title : '签报批复后抄送人',
				layout : 'fit',
				frame : true,
				height : 141,
				width : 439,
				style : "padding-bottom:0px",
				items : [gridQuestReader]
			})
			
	// formpanel
	var formpanelReader = new Ext.form.FormPanel({
		layout : 'fit',
	    frame : false,
	    width : 460,
//	    height : 135,
	    style : "border:0px",
		items : [fldSetReader]
	})			
	// -------------------签报批复后抄送人结束-------------------------			

	// -------------------内容相关附件--------------------------------			
	// 定义选择列
	var smQuestFile = new Ext.grid.RowSelectionModel({
				singleSelect : true
			});

	// 数据源--------------------------------
	var recordQuestFile = Ext.data.Record.create([{
				name : 'idFile'
			}, {
				name : 'applyIdFile'
			}, {
				name : 'fileTypeFile'
			}, {
				name : 'fileKindFile'
			}, {
				name : 'fileNameFile'
			}, {
				name : 'fileTextFile'
			}, {
				name : 'updateTimeFile'
			}]);

	// 定义获取数据源
	var proxyQuestFile = new Ext.data.HttpProxy({
				url : 'administration/getOutQuestFile.action'
			});

	// 定义格式化数据
	var recordQuestFile = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, recordQuestFile);

	// 定义封装缓存数据的对象
	var storeQuestFile = new Ext.data.Store({
				// 访问的对象
				proxy : proxyQuestFile,
				// 处理数据的对象
				reader : recordQuestFile
			});

	// --gridpanel显示格式定义-----开始-------------------
	// 附件
    var tfAppend = new Ext.form.TextField({
        id : "fileUpload",
        inputType : 'file',
        buttonText: '参照...',
        name : 'fileUpload',
        fieldLabel : "附件",
        width : 200
    });

	// 上传附件按钮
	var btnUploadFile = new Ext.Button({
				text : "上传附件",
				iconCls : Constants.CLS_UPLOAD,
				align : 'center',
				handler : uploadQuestFile
			});

   var cmQuestFile = new Ext.grid.ColumnModel([
			// 自动行号
			new Ext.grid.RowNumberer({
						header : "行号",
						width : 31
					}), {
				header : "序号",
				hidden : true,
				dataIndex : 'idFile'
			}, {
				header : "申请单号",
				hidden : true,
				dataIndex : 'applyIdFile'
			}, {
				header : "文件类型",
				hidden : true,
				dataIndex : 'fileTypeFile'
			}, {
				header : "附件类型",
				hidden : true,
				dataIndex : 'fileKindFile'
			}, {
				header : "文件内容",
				hidden : true,
				dataIndex : 'fileTextFile'
			}, {
				header : "文件名",
				sortable : true,
				dataIndex : 'fileNameFile',
				renderer : showFile
			}, {
				header : "删除附件",
				sortable : true,
				width : 65,
				dataIndex : 'fileNameFile',
				renderer : dele
			}, {
				header : "修改时间",
				hidden : true,
				dataIndex : 'updateTimeFile'
			}])
			
	var testpanel = new Ext.form.FormPanel({
		layout : 'column',
	    frame : true,
	    style : "border:0px",
		fileUpload : true,
		items : [tfAppend,btnUploadFile]
	})
	
		// 头部工具栏					
	var tbarQuestFile = new Ext.Toolbar(testpanel)

	
	var gridQuestFile = new Ext.grid.GridPanel({
				store : storeQuestFile,
				cm : cmQuestFile,
				sm : smQuestFile,
				style : "padding-bottom:0px",
				autoScroll : true,
				enableColumnMove : false,
				autoSizeColumns : true,
				viewConfig : {
					forceFit : false
				},
				// 头部工具栏
				tbar : tbarQuestFile
	})
    // --gridpanel显示格式定义-----结束--------------------

	// fieldSet
	var fldSetFile = new Ext.form.FieldSet({
				title : '内容相关附件',
				layout : 'fit',
				height : 155,
				width : 439,
				frame : true,
				style : "padding-bottom:0px",
				items : [gridQuestFile]
			})
			
	// formpanel
	var formpanelFile = new Ext.form.FormPanel({
		layout : 'fit',
	    frame : false,
//	    height : 130,
	    width : 460,
	    style : "border:0px",
		fileUpload : true,
		items : [fldSetFile]
	})		
	// -------------------内容相关附件结束-------------------------					

	// Panel
	var formpanelQuest = new Ext.Panel({
		        frame : true,
                border : false,
				labelAlign : 'right',
				width : 473,
				labelWidth : 65,
				style : "padding-bottom:0px;border:0px",
				items : [formpanelDetail, formpanelReader, formpanelFile]
			})
	

	// 保存按钮
	var btnConfirmQuest = new Ext.Button({
				text : Constants.BTN_SAVE,
				iconCls : Constants.CLS_SAVE,
				buttonAlign : 'center',
				handler : confirmQuest
			})

	// 取消按钮
	var btnCancelQuest = new Ext.Button({
				text : Constants.BTN_CANCEL,
				iconCls : Constants.CLS_CANCEL,
				buttonAlign : 'center',
				handler : cancelQuestDetail
			})

	// 弹出窗口
	var winQuest = new Ext.Window({
				width : 505,
				height : 320,
				modal : true,
				autoScroll : true,
                buttonAlign : 'center',
				items : [formpanelQuest],
				layout : 'form',
				closeAction : 'hide',
				resizable : false,
				buttons : [btnConfirmQuest, btnCancelQuest]
			});

	// **********主画面********** //
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "fit",
				items : [gridQuest]
			});

	// **********画面事件处理********** //	
	// 增加				
	function addQuest() {

		strMethod = "add";
		initWin();
		winQuest.setTitle("新增内部签报申请");
		dateInit();
		txtApplyDate.setValue(today);
		storeApplyId.load();
		drpReaderMan.store.load();
		winQuest.show();
		formpanelQuest.focus();
		// 清除附件内容
		var domAppend = tfAppend.el.dom;
		var parent = domAppend.parentNode;

		// 保存
		var domForSave = domAppend.cloneNode();
		// 移除附件控件
		parent.removeChild(domAppend);
		// 再追加控件
		parent.appendChild(domForSave);
		// 应用该控件
		tfAppend.applyToMarkup(domForSave);
	}

	// 修改
	function updateQuest() {
		var rec = gridQuest.getSelectionModel().getSelected();
		if (rec) {
			strMethod = "update";
			initWin();
			// 申请明细初始化
			hdId.value = rec.data.id;
			hdUpdateTime.value = rec.data.updateTime;
			txtaApplyId.setValue(rec.data.reportId);
//			Ext.get('applyId').dom.value = rec.data.applyId;
			hdApplyMan.value = rec.data.applyMan;
			txtApplyManName.setValue(rec.data.applyManName);
			txtApplyManDeptName.setValue(rec.data.applyManDeptName);
			txtApplyDate.setValue(rec.data.applyDate);
			txtCheckedMan.setValue(rec.data.checkedMan);
			txtApplyTopic.setValue(rec.data.applyTopic);
			txaApplyText.setValue(rec.data.applyText);
			// 抄送人初始化数据
			storeQuestReader.baseParams = {
				strApplyId : rec.data.applyId
			}
			storeQuestReader.load({
						params : {
							start : 0,
							limit : Constants.PAGE_SIZE
						}
					});
			drpReaderMan.store.load();
			// 申请附件初始化数据
			storeQuestFile.baseParams = {
				strApplyId : rec.data.applyId
			}
			storeQuestFile.load({
						params : {
							start : 0,
							limit : Constants.PAGE_SIZE
						}
					});
			winQuest.setTitle("修改内部签报申请");
			winQuest.show();
			formpanelQuest.focus();
			Ext.get('applyId').dom.value = rec.data.applyId;
			// 清除附件内容
			var domAppend = tfAppend.el.dom;
			var parent = domAppend.parentNode;

			// 保存
			var domForSave = domAppend.cloneNode();
			// 移除附件控件
			parent.removeChild(domAppend);
			// 再追加控件
			parent.appendChild(domForSave);
			// 应用该控件
			tfAppend.applyToMarkup(domForSave);
		} else {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
		}
	}

	// 删除
	function deleteQuest() {
		var rec = gridQuest.getSelectionModel().getSelected();
		if (rec) {
			var lngId = rec.get('id');
			var strApplyId = rec.get('applyId');
			var strUpdateTime = rec.get('updateTime');
			Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, MessageConstants.COM_C_002,
					function(buttonobj) {
						if (buttonobj == 'yes') {
							// 刪除
							Ext.Ajax.request({
								method : Constants.POST,
								url : 'administration/deleteQuost.action',
								params : {
									lngId : lngId,
									strApplyId : strApplyId,
									strUpdateTime : strUpdateTime
								},
								success : function(result, request) {
									var o = eval("(" + result.responseText
											+ ")");
									// 排他
									if (o.msg == Constants.DATA_USING) {
										Ext.Msg
												.alert(
														MessageConstants.SYS_ERROR_MSG,
														MessageConstants.COM_I_002,
														function() {
															storeQuest.load({
																params : {
																	start : 0,
																	limit : Constants.PAGE_SIZE
																}
															});
															gridQuest.getView()
																	.refresh();
														});
										return;
									}
									// Sql错误
									if (o.msg == Constants.SQL_FAILURE) {
										Ext.Msg.alert(Constants.ERROR,
												MessageConstants.COM_E_014);
										return;
									}
									// IO错误
									if (o.msg == Constants.IO_FAILURE) {
										Ext.Msg.alert(Constants.ERROR,
												MessageConstants.COM_E_022);
										return;
									}
									// 数据格式化错误
									if (o.msg == Constants.DATE_FAILURE) {
										Ext.Msg.alert(Constants.ERROR,
												MessageConstants.COM_E_023);
										return;
									} else {
										Ext.Msg
												.alert(
														MessageConstants.SYS_REMIND_MSG,
														MessageConstants.COM_I_005,
														function() {
															storeQuest.load({
																params : {
																	start : 0,
																	limit : Constants.PAGE_SIZE
																}
															});
															gridQuest.getView()
																	.refresh();
														});
									}
								},
								failure : function() {
									Ext.Msg.alert(Constants.ERROR,
											MessageConstants.DEL_ERROR);
								}
							});
						}
					});
		} else {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
		}
	}

	// 上报
	function reportQuest() {
		var rec = gridQuest.getSelectionModel().getSelected();
		if (rec) {
			var lngId = rec.get('id');
			var strApplyId = rec.get('applyId');
			var strUpdateTime = rec.get('updateTime');
			Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, MessageConstants.COM_C_006,
					function(buttonobj) {
						if (buttonobj == 'yes') {
							// 上报
							Ext.Ajax.request({
								method : Constants.POST,
								url : 'administration/reportQuost.action',
								params : {
									lngId : lngId,
									strApplyId : strApplyId,
									strUpdateTime : strUpdateTime
								},
								success : function(result, request) {
									var o = eval("(" + result.responseText
											+ ")");
									// 排他
									if (o.msg == Constants.DATA_USING) {
										Ext.Msg
												.alert(
														MessageConstants.SYS_ERROR_MSG,
														MessageConstants.COM_I_002);
										return;
									}
									// Sql错误
									if (o.msg == Constants.SQL_FAILURE) {
										Ext.Msg.alert(Constants.ERROR,
												MessageConstants.COM_E_014);
										return;
									}
									// IO错误
									if (o.msg == Constants.IO_FAILURE) {
										Ext.Msg.alert(Constants.ERROR,
												MessageConstants.COM_E_022);
										return;
									}
									// 数据格式化错误
									if (o.msg == Constants.DATE_FAILURE) {
										Ext.Msg.alert(Constants.ERROR,
												MessageConstants.COM_E_023);
										return;
									} else {
										Ext.Msg
												.alert(
														MessageConstants.SYS_REMIND_MSG,
														MessageConstants.COM_I_007,
														function() {
															storeQuest.load({
																params : {
																	start : 0,
																	limit : Constants.PAGE_SIZE
																}
															});
															gridQuest.getView()
																	.refresh();
														});
									}
								},
								failure : function() {
									Ext.Msg.alert(Constants.ERROR,
											MessageConstants.DEL_ERROR);
								}
							});
						}
					});
		} else {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
		}
	}

	// 保存				
	function confirmQuest() {
		for (i = 0; i < storeQuestReader.getCount(); i++) {
			for (j = i + 1; j < storeQuestReader.getCount(); j++) {
				if (storeQuestReader.getAt(i).data.readManReader == storeQuestReader
						.getAt(j).data.readManReader) {
					Ext.Msg
							.alert(Constants.ERROR,
									MessageConstants.AS002_E_001);
					return;
				}
			}
		}
		Ext.Msg.confirm(Constants.BTN_CONFIRM, MessageConstants.COM_C_001,
				function(buttonobj) {
					if (buttonobj == 'yes') {
						// 保存操作开始
						var strAdd = new Array();
						var strUpdate = new Array();
						for (i = 0; i < storeQuestReader.getCount(); i++) {
							if (storeQuestReader.getAt(i).data.readManNameReader != "") {
								// for (j = i + 1; j <
								// storeQuestReader.getCount(); j++) {
								// if
								// (storeQuestReader.getAt(i).data.readManReader
								// == storeQuestReader
								// .getAt(j).data.readManReader) {
								// Ext.Msg.alert(Constants.ERROR,
								// MessageConstants.AS002_E_001);
								// return;
								// }
								// }
								// 序列化数据
								if (storeQuestReader.getAt(i).data.idReader == "") {
									strAdd.push(storeQuestReader.getAt(i).data);
								} else {
									strUpdate
											.push(storeQuestReader.getAt(i).data);
								}
							}
						}

						Ext.get('id').dom.value = hdId.value;
						Ext.get('applyMan').dom.value = hdApplyMan.value;
						// 提交表单
						formpanelDetail.getForm().submit({
							params : {
								// 新加的记录
								strAdd : Ext.util.JSON.encode(strAdd),
								// 修改记录
								strUpdate : Ext.util.JSON.encode(strUpdate),
								// 删除的
								strDelete : Ext.util.JSON.encode(strDel),
								// 更新时间
								strUpdateTime : hdUpdateTime.value,
								strApplyId : Ext.get('applyId').dom.value,
								strRes : txtaApplyId.getValue()
							},
							method : Constants.POST,
							url : 'administration/saveQuost.action',
							success : function(form, action) {
								var o = eval("(" + action.response.responseText
										+ ")");
								// 排他
								if (o.msg == Constants.DATA_USING) {
									Ext.Msg.alert(
											MessageConstants.SYS_ERROR_MSG,
											MessageConstants.COM_I_002);
									return;
								}
								// Sql错误
								if (o.msg == Constants.SQL_FAILURE) {
									Ext.Msg.alert(Constants.ERROR,
											MessageConstants.COM_E_014);
									return;
								}
								// IO错误
								if (o.msg == Constants.IO_FAILURE) {
									Ext.Msg.alert(Constants.ERROR,
											MessageConstants.COM_E_022);
									return;
								}
								// 数据格式化错误
								if (o.msg == Constants.DATE_FAILURE) {
									Ext.Msg.alert(Constants.ERROR,
											MessageConstants.COM_E_023);
									return;
								} else {
									// 成功
									Ext.Msg.alert(
											MessageConstants.SYS_REMIND_MSG,
											MessageConstants.COM_I_004,
											function() {
												storeQuest.load({
													params : {
														start : 0,
														limit : Constants.PAGE_SIZE
													}
												});
												gridQuest.getView().refresh();
												winQuest.hide();
											});
								}
							},
							failure : function() {
							}
						})
					}
				})
	}

	// 取消
	function cancelQuestDetail() {
		Ext.Msg.confirm(Constants.BTN_CONFIRM, MessageConstants.COM_C_005,
				function(buttonobj) {
					if (buttonobj == 'yes') {
						winQuest.hide();
					}
					// 清除附件内容
					var domAppend = tfAppend.el.dom;
					var parent = domAppend.parentNode;

					// 保存
					var domForSave = domAppend.cloneNode();
					// 移除附件控件
					parent.removeChild(domAppend);
					// 再追加控件
					parent.appendChild(domForSave);
					// 应用该控件
					tfAppend.applyToMarkup(domForSave);
				})
	}

	// 增加抄送人明细
	function addQuestReader() {
		var add = new recordQuestReader({
					idReader : "",
					readManReader : "",
        // xsTan 追加开始 2009-1-22 对应抄送人员DRP显示Bug
                    readManNameReader : ""
        // xsTan 追加结束 2009-1-22 对应抄送人员DRP显示Bug
				});
		storeQuestReader.insert(storeQuestReader.getCount(), add);
		gridQuestReader.getView().refresh();
	}

	// 取消抄送人明细				
	function cancelQuestReader() {
		Ext.Msg.confirm(Constants.BTN_CONFIRM, MessageConstants.COM_C_005,
				function(buttonobj) {
					if (buttonobj == 'yes') {
						// 删除抄送人记录清零
		                strDel = [];
						// 抄送人初始化数据
						storeQuestReader.baseParams = {
							strApplyId : Ext.get('applyId').dom.value
						}
						storeQuestReader.load({
									params : {
										start : 0,
										limit : Constants.PAGE_SIZE
									}
								});
					}
				})}

	var strDel = [];
	// 删除抄送人明细				
	function deleteQuestReader() {
		var rec = gridQuestReader.getSelectionModel().getSelected();
		if (rec) {
			Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, MessageConstants.COM_C_002,
					function(buttonobj) {
						if (buttonobj == 'yes') {
							// 如果是新增的数据
							if (rec.data.idReader == "") {
							} else {
								strDel.push(rec.data);
							}
							
							storeQuestReader.remove(rec);
//							storeQuestReader.commitChanges();
							gridQuestReader.getView().refresh();
									
                        }
					})
		} else {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
		}
	}
	// 上传附件				
	function uploadQuestFile() {
		if (strMethod == "add") {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.AS002_C_001);
		} else {
			if (!checkFilePath(tfAppend.getValue())) {
				Ext.Msg.alert(Constants.ERROR, MessageConstants.COM_E_024);
				return;
		    }
			// 提交表单
			formpanelFile.getForm().submit({
				method : Constants.POST,
				params : {
					strApplyId : Ext.get('applyId').dom.value
				},
				url : 'administration/uploadQuestFile.action',
				success : function(form, action) {
					var o = eval("(" +action.response.responseText + ")");
					// 排他
					if (o.msg == Constants.DATA_USING) {
						Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
								MessageConstants.COM_I_002);
						return;
					}
					// Sql错误
					if (o.msg == Constants.SQL_FAILURE) {
						Ext.Msg.alert(Constants.ERROR,
								MessageConstants.COM_E_014);
						return;
					}
					// 文件不存在
					if (o.msg == Constants.FILE_NOT_EXIST) {
						Ext.Msg.alert(Constants.ERROR,
								MessageConstants.COM_E_024);
						return;
					}
					// IO错误
					if (o.msg == Constants.IO_FAILURE) {
						Ext.Msg.alert(Constants.ERROR,
								MessageConstants.COM_E_022);
						return;
					}
					// 数据格式化错误
					if (o.msg == Constants.DATE_FAILURE) {
						Ext.Msg.alert(Constants.ERROR,
								MessageConstants.COM_E_023);
						return;
					} else {
						Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
								MessageConstants.UPLOAD_SUCCESS, function() {
									// 申请附件初始化数据
									storeQuestFile.baseParams = {
										strApplyId : Ext.get('applyId').dom.value
									}
									storeQuestFile.load({
												params : {
													start : 0,
													limit : Constants.PAGE_SIZE
												}
											});
									gridQuestFile.getView().refresh();
								})}
				},
				failure : function() {
					
				}
			});
		}
	}

	// 删除附件
	deleteQuestFile = function() {
		Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
				MessageConstants.COM_C_002, function(buttonobj) {
					if (buttonobj == 'yes') {
						var rec = gridQuestFile.getSelectionModel()
								.getSelected();

						// Db中的数据更新
						var strUpdateTime = rec.data.updateTimeFile;
						// 刪除
						Ext.Ajax.request({
							method : Constants.POST,
							url : 'administration/deleteQuestFile.action',
							params : {
								lngFileId : rec.data.idFile,
								strUpdateTime : strUpdateTime
							},
							success : function(result, request) {
								var o = eval("(" + result.responseText + ")");
								// 排他
								if (o.msg == Constants.DATA_USING) {
									Ext.Msg.alert(
											MessageConstants.SYS_ERROR_MSG,
											MessageConstants.COM_I_002);
								}
								// Sql错误
								if (o.msg == Constants.SQL_FAILURE) {
									Ext.Msg.alert(Constants.ERROR,
											MessageConstants.COM_E_014);
									return;
								}
								// IO错误
								if (o.msg == Constants.IO_FAILURE) {
									Ext.Msg.alert(Constants.ERROR,
											MessageConstants.COM_E_022);
									return;
								}
								// 数据格式化错误
								if (o.msg == Constants.DATE_FAILURE) {
									Ext.Msg.alert(Constants.ERROR,
											MessageConstants.COM_E_023);
									return;
								} else {
									// 成功
									Ext.Msg.alert(
											MessageConstants.SYS_REMIND_MSG,
											MessageConstants.COM_I_005,
											function() {
												// 申请附件初始化数据
												storeQuestFile.baseParams = {
													strApplyId : Ext
															.get('applyId').dom.value
												}
												storeQuestFile.load({
													params : {
														start : 0,
														limit : Constants.PAGE_SIZE
													}
												});
											});
								}
							},
							failure : function() {
								Ext.Msg.alert(Constants.ERROR,
										Constants.DEL_ERROR);
							}
						});
					}
				})
	} 
	
	
	
	function dele(value) {
		if(value != "") {
    		return "<span style='padding-left:19px'></span><a href='#'  onclick= 'deleteQuestFile();return false;'><img src='comm/ext/tool/dialog_close_btn.gif'></a>";
    	} else {
    		return "";
    	}
	}
	
	// 下载的实现
	 /**
     * 附件名
     */
    function showFile(value, cellmeta, record) {
    	if(value != "") {
    		var idFile = record.get("idFile");
    		var download = 'download("' + idFile + '");return false;';
    		return "<a href='#' onclick='"+ download+ "'>" + value + "</a>";
    	} else {
    		return "";
    	}
    }
    
    /**
	 * 下载文件
	 */
	 download = function(idFile) {
		document.all.blankFrame.src = "administration/downloadOutQueryFile.action?id=" + idFile;
	}
	
	// 日期初值
	function dateInit() {
		today = new Date();
		today = dateFormat(today);
	}

	function dateFormat(value) {
		var year;
		var month;
		var day;
		var hour;
		var minute;
		minute = value.getMinutes();
		if (minute < 10) {
			minute = '0' + minute;
		}
		hour = value.getHours();
		if (hour < 10) {
			hour = '0' + hour;
		}
		day = value.getDate();
		if (day < 10) {
			day = '0' + day;
		}
		month = value.getMonth() + 1;
		if (month < 10) {
			month = '0' + month;
		}
		year = value.getYear();
		// 修正日期格式的Bug 开始
		value = year + "-" + month + "-" + day;
		// 修正日期格式的Bug 结束
		return value;
	}

        // xsTan 删除开始 2009-1-22 
    // 通过抄送人员code得到名字
//    function getNameByCode(value) {
//}
        // xsTan 删除结束 2009-1-22
	// 弹出画面初期化
	function initWin() {
		// 申请明细初始化
		hdId.value = "";
		hdUpdateTime.value = "";
		txtaApplyId.setValue("");
//		Ext.get('applyId').dom.value = "";
		txtApplyManName.setValue("");
		txtApplyManDeptName.setValue("");
		txtApplyDate.setValue("");
		txtCheckedMan.setValue("");
		txtApplyTopic.setValue("");
		txaApplyText.setValue("");
		
		// 抄送人初始化数据
		storeQuestReader.removeAll();

		// 申请附件初始化数据
		storeQuestFile.removeAll();

		// 删除抄送人记录清零
		strDel = [];
		
	}    
})