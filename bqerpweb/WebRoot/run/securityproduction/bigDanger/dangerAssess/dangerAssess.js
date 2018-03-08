Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	
	// 系统当前时间
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";

		return s;
	}
	

	// 从session取登录人编码姓名部门相关信息
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号，赋给全局变量
							fillByCode.setValue(result.workerCode);
							entryBy.setValue(result.workerCode);

						}
					}
				});
	}
	var bview;
		var btnView = new Ext.Button({
				id : 'btnView',
				text : '查看',
				handler : function() {
					window.open(bview);
				}
			});
	// 标题
	
	function checkInput()
	{
		if(fileSort.getValue() == ""){
			Ext.Msg.alert("提示","请选择类别！");
			return false;
		}
		if(name.getValue() == ""){
			Ext.Msg.alert("提示","请输入名称！");
			return false;
		}
		
		
		return true;
	}
	
	var fileSort = new Ext.form.ComboBox({
		store : [['0', '评估标准'], ['1', '评估报告']],
		value : '',
		//id : 'smartDate',
		name : 'model.assessSort',
		valueField : "value",
		displayField : "text",
		fieldLabel : "类别",
		//hideLabel : true,
		mode : 'local',
		readOnly : true,
		anchor : '90%',
		typeAhead : true,
		forceSelection : true,
		//hiddenName : 'model.smartDate',
		editable : false,
		triggerAction : 'all',
		disabled : false,
		//width : 65,
		selectOnFocus : true
	});
	
	// 附件名称
//	var annex = {
//		id : "annex",
//		xtype : 'fileuploadfield',
//		isFormField:true,
//		name : "annex",
//		fieldLabel : '选择附件：',
//		fileUpload : true,
//		height : 21,
//		anchor : "80%",
//		buttonCfg : {
//			text : '浏览...',
//			iconCls : 'upload-icon'
//		}
//	}
	
	var annex = new Ext.form.TextField({
		id : "annex",
		inputType : "file",
		fieldLabel : '选择附件',
		anchor : '90%',
		name : 'annex' 
	})
	annex.on('focus',function(){
		var temp = (annex.getValue()).split('\\');
		var file = temp[temp.length-1];
		var filename = (file.split('.'))[0];
		name.setValue(filename);
		
	})
	
	var name = new Ext.form.TextField({
				fieldLabel : "名称",
				name : 'model.fileName',
				anchor : '90%'

			})
	
	var assessid = new Ext.form.TextField({
				fieldLabel : "id：",
				name : 'model.Assess_Id',
				anchor : '90%'

			})
	
			
	var entryBy = new Ext.form.TextField({
				fieldLabel : "上传人",
				readOnly : true,
				name : 'model.entryBy',
				anchor : '80%'

			})
	// 填报人编码
	var fillByCode = new Ext.form.Hidden({
				name : 'model.fillBy'

			})
	// 填报时间
	var entryDate = new Ext.form.TextField({
				fieldLabel : "填报时间",
				readOnly : true,
				name : 'model.entryDate',
				value : getDate(),
				anchor : '80%'

			})
			
			
	// 弹窗的表单对象
	var blockForm = new Ext.form.FormPanel({
				labelAlign : 'right',
				frame : true,
				labelWidth : 80,
				style : 'padding:10px,0px,0px,5px',
				layout : 'column',
				closeAction : 'hide',
				//title : "增加、修改月报信息",
				fileUpload : true,
				items : [{

							border : false,
							layout : 'form',
							columnWidth : 1,
							items : [fileSort]
						}, {
							columnWidth : 1,
							layout : 'form',
							border : false,
							items : [annex]
						}, {
							columnWidth : 1,
							border : false,
							layout : 'form',
							items : [name]
						}, {
							columnWidth : .5,
							layout : 'form',
							border : false,
							items : [entryBy]
						}, {
							columnWidth : .5,
							layout : 'form',
							border : false,
							items : [entryDate]
						}]

			});

	//弹出窗体
	var win = new Ext.Window({
		width : 500,
		height : 180,
		buttonAlign : "center",
		items : [blockForm],
		layout : 'fit',
		closeAction : 'hide',
		resizable : false,
		modal : true,
		buttons : [{
			text : '保存',
			iconCls:'save',
			handler : function() {
				var myurl="";
				if (method=="update") {
					myurl="security/updateDangerInfo.action";
				} 
				if(method=="add"){
					myurl="security/addDangerInfo.action";
				}
				
				if(!checkInput()) return;
				blockForm.getForm().submit({
					method : 'POST',
					url : myurl,
					params:{	
						  assesId:assessid.getValue(),
					      filePath : Ext.get("annex").dom.value,
					      reportType : reType,
					      timeType : tiType
					},
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						//Ext.Msg.alert("注意", o.msg);
						if(o.msg.indexOf("成功")!=-1)
						{
							queryRecord();
						    win.hide(); 
						    bview="";
						}
					},
					faliue : function() {
						Ext.Msg.alert('错误', '出现未知错误.');
					}
				});
			}
		}, {
			text : '取消',
			iconCls:'cancer',
			handler : function() { 
				win.hide();
			}
		}]

	});
  //名称关键字
	var fuzzy = new Ext.form.TextField({
		name : 'name',
		xtype : 'textfield'
	});
	
	//类别下拉框
	
	var sort = new Ext.form.ComboBox({
		store : [['', '全部'],['0', '评估标准'], ['1', '评估报告']],
		value : '',
		//id : 'smartDate',
		name : 'sort',
		valueField : "value",
		displayField : "text",
		fieldLabel : "类别",
		//hideLabel : true,
		mode : 'local',
		readOnly : true,
		anchor : '90%',
		typeAhead : true,
		forceSelection : true,
		//hiddenName : 'model.smartDate',
		editable : false,
		triggerAction : 'all',
		disabled : false,
		//width : 65,
		selectOnFocus : true
	});
	// 新建按钮
	var westbtnAdd = new Ext.Button({
				text : '新增',
				iconCls : 'add',
				handler : addRecord
			});


	// 修改按钮
	var westbtnedit = new Ext.Button({
				text : '修改',
				iconCls : 'update',
				handler : updateRecord
			});

	// 删除按钮
	var westbtndel = new Ext.Button({
		text : '删除',
		iconCls : 'delete',
		handler : deleteRecord
	});

	// 查询按钮
	var westbtnref = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : function() {
					queryRecord();
				}
			});

	var westsm = new Ext.grid.CheckboxSelectionModel();
	// 左边列表中的数据
	
			
				var datalist = new Ext.data.Record.create([

	{			id:'assessId',
				name : 'assessId'
			}, {
				name : 'fileName'
			},{
				name:'assessSort'
			},{
				name : 'annex'
			}, {
				name : 'entryBy'
			}, {
				name : 'entryDate'
			}, {
				name : 'isUse'
			}, {
				name : 'enterpriseCode'
			}]);

	var westgrids = new Ext.data.JsonStore({
				url : 'security/findDangerList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});

	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
				ds : westgrids,
				columns : [westsm, new Ext.grid.RowNumberer(), {
							header : "名称",
							width : 75,
							//align : "center",
							sortable : true,
							dataIndex : 'fileName'
						}, {
							header : "类别",
							width : 75,
							//align : "center",
							sortable : false,
							dataIndex : 'assessSort',
							renderer:function(v){
								if(v=='0')
								return "评估标准";
								if(v=='1')
								return "评估报告";
							} 
						}, {
							header : "查看",
							width : 75,
							sortable : true,
							dataIndex : 'annex',
							renderer:function(v){
								if(v !=null && v !='')
								{ 
									var s =  '<a href="#" onclick="window.open(\''+v+'\');return  false;">[查看]</a>';
									return s;
								}
							} 
						},{
							header : "上传人",
							width : 75,
							sortable : true,
							dataIndex : 'entryBy'
							
			
		                  },{
							header : "上传日期",
							width : 75,
							sortable : true,
							dataIndex : 'entryDate',
								renderer:function(v){
								if(v !=null && v !='')
								{ 
									return (v.replace('T',' ').substring(0,10));
								}
							}
		                  }],
				viewConfig : {
			                 forceFit : true
		           },
				tbar : ['名称：',fuzzy,'类别：',sort,westbtnref, {
							xtype : "tbseparator"
						},westbtnAdd , {
							xtype : "tbseparator"
						},westbtnedit, {
							xtype : "tbseparator"
						}, westbtndel],
				sm : westsm,
				frame : true,
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : westgrids,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						}),
				border : true,
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});

	// westgrid 的事件
	westgrid.on("rowdblclick", updateRecord);
	function queryRecord()
	{
		
		westgrids.baseParams = {
				name:fuzzy.getValue(),
				sort:sort.getValue(),
			    reportType : reType,
				timeType : tiType
		}
		westgrids.load({
			params : {
				name:fuzzy.getValue(),
				sort:sort.getValue(),
				start : 0,
				limit : 18				
			}
		});
	}
	function addRecord()
	{  
		method="add";
		blockForm.getForm().reset(); 
		annex.setValue("");
		getWorkCode();
		win.show();  
		btnView.setVisible(false);
		win.setTitle("增加"+reType+"信息");
		//alert("dff");
	}
	function updateRecord()
	{		
			method="update";
			if (westgrid.selModel.hasSelection()) {
		
			var records = westgrid.getSelectionModel().getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				  win.show(); 
				var record = westgrid.getSelectionModel().getSelected();
		        blockForm.getForm().reset();
		        blockForm.form.loadRecord(record);
		        assessid.setValue(record.get("assessId"));
		        if(record.get("fileName")!=null&&record.get("fileName")!="")
		        {
		              bview=record.get("fileName");
		              btnView.setVisible(true);
		              Ext.get("annex").dom.value = record.get("annex");
		        }
		        else
		        {
		        	  btnView.setVisible(false);
		        }
		        
		        fileSort.setValue(record.get("assessSort"));
		        name.setValue(record.get("fileName"));
		        entryBy.setValue(record.get("entryBy"));
				win.setTitle("修改"+reType+"信息");
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}
	function deleteRecord()
	{
		var records = westgrid.selModel.getSelections();
		var ids = [];
		if (records.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {

			
			for (var i = 0; i < records.length; i += 1) {
				var member = records[i];
				if (member.get("assessId")) {
					ids.push(member.get("assessId")); 
				} else {
					
					westgrids.remove(westgrids.getAt(i));
				}
			}
		
			Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(
					buttonobj) {

				if (buttonobj == "yes") {

					Ext.lib.Ajax.request('POST',
							'security/deleteDangerInfo.action', {
								success : function(action) {
									//Ext.Msg.alert("提示", "删除成功！")
                                   		queryRecord();
						         	
								},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
							}, 'ids=' + ids);
				}
			});
			
		
		}

	}
	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							region : 'center',
							layout : 'fit',
							border : false,
							margins : '1 0 1 1',
							collapsible : true,
							items : [westgrid]

						}]
			});
			queryRecord();
});