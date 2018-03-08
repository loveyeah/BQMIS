Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

/**
	时间控件
**/

	function ChangeDateToString(DateIn){
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";
		
		//初始化时间
		Year = DateIn.getYear();
		Month = DateIn.getMonth() + 1;
		Day = DateIn.getDate();
		CurrentDate = Year = "-";
		if(Month >=10){
			CurrentDate = CurrentDate + Month + "-";
		}else {
			CurrentDate = CurrentDate + "0" + "-";
		}
		if(Day >=10){
			CurrentDate = CurrentDate + Day;
		}else{
			CurrentDate = CurrentDate + "0" + Day;
		}
		return CurrentDate;
	}

	var date = new Date();
	var startdate = date.add(Date.DAY, -2);
	var currentY = ChangeDateToString(startdate); 

	var dateY = new Ext.form.TextField({
				 fieldLabel : "活动日期 ",
				readOnly : true,
				anchor : '90%',
				name : 'dateY',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy',
									alwaysUseStartDate : true
									
								});
						this.blur();
					}
				},
				value : new Date().getFullYear()
			}); 
		var date = new Ext.form.TextField({
				fieldLabel : "活动日期",
				readOnly : true,
				anchor : '90%',
				name : 'hdDate',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : true
									
								});
						this.blur();
					}
				}
			});
	
	var jdzyId = parent.jdzyId;
	//选择人员窗口
	function selectPersonWin() {
		var args = {
			selectModel : 'signal',
			notIn : "",
			rootNode : {
				id : '-1',
				text : '大唐灞桥热电厂'
			}
		}
		var person	 = window
				.showModalDialog(
						'../../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(person) != "undefined") {
			
				emceeName.setValue(person.workerName);
				emceeMan.setValue(person.workerCode);
			
		}
	}
	// 系统当前时间
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		s = s.substr(0,10);
		return s;
	}
	// 从session取登录人编码姓名部门相关信息
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号，赋给全局变量
							dependEntryCode.setValue(result.workerCode);
							dependEntry.setValue(result.workerName);
							//alert(result.workerCode);
							//alert(result.workerName);
						}
					}
				});
	}
	var bview;
	// 活动主题
	var topic = new Ext.form.TextField({
				fieldLabel : "活动主题",
				name : 'model.mainTopic',
				anchor : '95%'
	});
	
	//季度下拉列表
	var smartDate = new Ext.form.ComboBox({
		store : [['1', '一季度'], ['2', '二季度'], ['3', '三季度'], ['4', '四季度']],
		value : '三季度',
		//id : 'smartDate',
		name : 'smartDate',
		valueField : "value",
		displayField : "text",
		fieldLabel : "季度",
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
	
	//季度下拉列表
	var smartDate1 = new Ext.form.ComboBox({
		store : [['1', '一季度'], ['2', '二季度'], ['3', '三季度'], ['4', '四季度']],
		value : '三季度',
		//id : 'toQuarterBox',
		name : 'smartDate1',
		valueField : "value",
		displayField : "text",
		fieldLabel : "季度",
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
	
	var dependYear = new Ext.form.TextField({
				fieldLabel : "年份",
				readOnly : true,
				anchor : '90%',
				name : 'dependYear',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy',
									alwaysUseStartDate : true							
								});
						this.blur();
					}
				},
				value : new Date().getFullYear()
			});
	//主持人
	var emceeName = new Ext.form.ComboBox({
				id : 'emceeName',
				name : 'emceeName',
				fieldLabel : "主持人",
				mode : 'local',
				readOnly : true,
				typeAhead : true,
				hiddent:true,
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				//allowBlank : false,
				anchor : '90%',
				onTriggerClick : function() {
					selectPersonWin();
				}
		});
	//主持人编码
	var emceeMan = new Ext.form.Hidden({
				name : "model.emceeMan"
	})
	//地点
	var place = new Ext.form.TextField({
				fieldLabel : "地点",
				name : 'model.place',
				anchor : '95%'
	});
	//参加人员
	var joinMan = new Ext.form.TextArea({
				fieldLabel : "参加人员",
				name : 'model.joinMan',
				anchor : '95%'
			})
	// 附件名称
	var annex = {
		id : "annex",
		xtype : 'fileuploadfield',
		isFormField:true,
		name : "annex",
		fieldLabel : '附件上传',
	//	fileUpload : true,
		height : 21,
		anchor : "95%",
		buttonCfg : {
			text : '浏览...',
			iconCls : 'upload-icon'
		}
	}

	// 查看
	var btnView = new Ext.Button({
				id : 'btnView',
				text : '查看',
				handler : function() {
					window.open(bview);
				}
			});
	btnView.setVisible(false);
	// 备注
	var memo = new Ext.form.TextArea({
				fieldLabel : "备注",
				name : 'dependMemo',
				anchor : '95%'
			})
	// 填报人姓名
	var dependEntry = new Ext.form.TextField({
				fieldLabel : "填报人",
				readOnly : true,
				name : 'dependEntry',
				anchor : '90%'

			})
	// 填报人编码
	var dependEntryCode = new Ext.form.Hidden({
				name : 'dependEntryCode'
			})
	// 填报时间
	var fillDate = new Ext.form.TextField({
				fieldLabel : "填报时间",
				readOnly : true,
				name : 'fillDate',
				value : getDate(),
				anchor : '90%'

			})
	//监督专业编码
	var zyId = new Ext.form.Hidden({
				name : "model.jdzyId"
	})
	// 主键
	var jdhdId = new Ext.form.Hidden({
				name : 'model.jdhdId'

			})
	function checkInput()
	{
		if(topic.getValue()==""){
			 Ext.Msg.alert("提示","请输入活动主题！");
			 return false;
		}
		if(date.getValue() == ""){
			Ext.Msg.alert("提示","请输入日期！");
			return false;
		}
		if(emceeName.getValue() == ""){
			Ext.Msg.alert("提示","请输入主持人！");
			return false;
		}
		return true;
	}
	
	//可靠性报表ID --主键
	var dependId = new Ext.form.Hidden({
		name : 'dependId'
	});
	
	//可靠性报表名
	var dependName = new Ext.form.TextField({
		fieldLabel : '报表名称',
		name : 'dependName',
		ahchor : '90%'
	});
	
	//可靠性年报年份
	var date = new Ext.form.TextField({
				fieldLabel : "月份",
				readOnly : true,
				anchor : '90%',
				name : 'dependDate',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy',
									alwaysUseStartDate : true
									
								});
						this.blur();
					}
				},
				value : new Date().getFullYear()
			});
	
	// 填报人姓名
	var fillName = new Ext.form.TextField({
				fieldLabel : "填报人",
				readOnly : true,
				name : 'dependEntry',
				anchor : '90%'

			})
	
	
	// 弹窗的表单对象
	var blockForm = new Ext.form.FormPanel({
				labelAlign : 'right',
				frame : true,
				labelWidth : 80,
				style : 'padding:10px,0px,0px,5px',
				layout : 'column',
				closeAction : 'hide',
				fileUpload : true,
				items : [{//年份
							border : false,
							layout : 'form',
							columnWidth : .5,
							items : [dependYear]
						},{//季度
							border : false,
							layout : 'form',
							columnWidth : .5,
							items : [smartDate1]
						}, {//可靠性名称
							columnWidth : 1,
							layout : 'form',
							border : false,
							items : [dependName]
						}, {
							columnWidth : .5,
							layout : 'form',
							border : false,
							hidden :true,
							items : [emceeName]
						}, {
							border : false,
							layout : 'form',
							hidden :true,
							columnWidth : 1,
							items : [place]
						}, {
							border : false,
							layout : 'form',
							hidden :true,
							columnWidth : 1,
							items : [joinMan]
						}, {//附件
							columnWidth : 0.8,
							border : false,
							layout : 'form',
							items : [annex]
						},{//浏览
							columnWidth : 0.2,
							border : false,
							layout : 'form',
							items : [btnView]
						}, {//备注
							border : false,
							layout : 'form',
							columnWidth : 1,
							items : [memo]
						}, {//填报人
							columnWidth : .5,
							layout : 'form',
							border : false,
							items : [dependEntry]
						}, {//填报时间
							columnWidth : .5,
							layout : 'form',
							border : false,
							items : [fillDate]
						}, {//填报人工号
							border : false,
							layout : 'form',
							columnWidth : 1,
							items : [dependEntryCode,dependId]
						}]

			});

	//弹出窗体
	var win = new Ext.Window({
		width : 500,
		height : 330,
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
				if (dependId.getValue() == "") {
					myurl="productionrec/addDependYearReport.action";
				} else {
					myurl="productionrec/updateDependYearReport.action";
				}
				
				//if(!checkInput()) return;
				blockForm.getForm().submit({
					method : 'POST',
					url : myurl,
					params:{	
					      filePath : Ext.get("annex").dom.value,
					      timeType : 'S'
					      //jdzyId : jdzyId,
					      //hdDate : date.getValue()
					},
					success : function(form, action) {
						//var o = eval("(" + action.response.responseText + ")");
						//Ext.Msg.alert("注意", o.msg);
						//if(o.msg.indexOf("成功")!=-1)
						//{
							queryRecord();
						    //win.hide(); 
						    //bview="";
						//}
					},
					faliue : function() {
						//Ext.Msg.alert('错误', '出现未知错误.');
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

	// 刷新按钮
	var westbtnref = new Ext.Button({
				text : '刷新',
				iconCls : 'reflesh',
				handler : function() {
					topicName.setValue("");
					queryRecord();
				}
			});

	var westsm = new Ext.grid.CheckboxSelectionModel();
	// 左边列表中的数据
	var datalist = new Ext.data.Record.create([

	{
				name : 'dependId'
			}, {
				name : 'dependName'
			}, {
				name : 'dependPath'
			}, {
				name : 'dependDate'
			}, {
				name : 'dependType'
			}, {
				name : 'dependYear'
			}, {
				name : 'dependTimeType'
			}, {
				name : 'dependEntry'
			}, {
				name : 'dependMemo'
			},{//新增上报人姓名
				name : 'dependEntryName'
			}]);

	var westgrids = new Ext.data.JsonStore({
				url : 'productionrec/findDependOnlyYear.action',//需修改 findDependOnlyYear
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});
	//模糊查询条件
	var topicName = new Ext.form.TextField({
		id : "topicName",
		readOnly : false,
		width : 200,
		emptyText : "文件名"
	})
	//月份
	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
				ds : westgrids,
				columns : [westsm, new Ext.grid.RowNumberer(),  {
							header : "报表名称",
							width : 100,
							//align : "center",
							sortable : true,
							dataIndex : 'dependName'
						},{
							header : "报表内容",
							width : 75,
							sortable : true,
							dataIndex : 'dependPath',
							renderer:function(v){
								if(v !=null && v !='')
								{ 
									var s =  '<a href="#" onclick="window.open(\''+v+'\');return  false;">[查看]</a>';
									return s;
								}
							} 
			
		                  },{
							header : "填报人",
							width : 100,
							//align : "center",
							sortable : true,
							dataIndex : 'dependEntryName'
						},{
							header : "填报时间",
							width : 100,
							//align : "center",
							sortable : true,
							dataIndex : 'dependDate'
						}],
				viewConfig : {
			                 forceFit : true
		           },
				tbar : ["年份：",dateY,"季度：",smartDate,{
			              id : 'btnQuery',
					       text : "查询",
						   iconCls : 'query',
					       handler : queryRecord
		                }, {
							xtype : "tbseparator"
						}, westbtnAdd, {
							xtype : "tbseparator"
						}, westbtnedit, {
							xtype : "tbseparator"
						}, westbtndel, {
							xtype : "tbseparator"
						}, westbtnref],
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
			dependYear : document.getElementById("dateY").value,
			dependSession : document.getElementById("dateY").value + document.getElementById("smartDate").value
			//topicName : topicName.getValue()
		}
		westgrids.load({
			params : {
				start : 0,
				limit : 18				
			}
		});
	}
	function addRecord()
	{  
		blockForm.getForm().reset(); 
		getWorkCode();
		win.show();  
		btnView.setVisible(false);
		win.setTitle("增加活动纪要信息");
		//alert("dff");
	}
	function updateRecord()
	{
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
		        if(record.get("dependName")!=null&&record.get("dependName")!="")
		        {
		              bview=record.get("dependPath");
		              btnView.setVisible(true);
		              Ext.get("annex").dom.value = bview.replace('/power/upload_dir/productionrec/','');
		              alert(bview);
		        }
		        else
		        {
		        	  btnView.setVisible(false);
		        }
				win.setTitle("修改活动纪要信息");
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
				if (member.get("dependId")) {
					ids.push(member.get("dependId")); 
				} else {
					
					westgrids.remove(westgrids.getAt(i));
				}
			}
		
			Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(
					buttonobj) {

				if (buttonobj == "yes") {

					Ext.lib.Ajax.request('POST',
							'productionrec/deleteDependYearReport.action', {
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