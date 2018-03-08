Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	var jdzyId = parent.jdzyId;
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
							fillBy.setValue(result.workerCode);
							fillName.setValue(result.workerName);

						}
					}
				});
	}
	
	var bview;
	// 活动主题
	var topic = new Ext.form.TextField({
				fieldLabel : "活动主题",
				name : 'model.mainTopic',
				readOnly : true,
				anchor : '95%'
	});
	// 活动日期
	var date = new Ext.form.TextField({
				fieldLabel : "活动日期",
				readOnly : true,
				anchor : '90%',
				name : 'hdDate'
			});
	//主持人
	var emceeName = new Ext.form.ComboBox({
				id : 'emceeName',
				name : 'emceeName',
				fieldLabel : "主持人",
				mode : 'local',
				readOnly : true,
				typeAhead : true,
				forceSelection : true,
				editable : false,
				disabled : true,
				triggerAction : 'all',
				selectOnFocus : true,
				//allowBlank : false,
				anchor : '90%'
		});
	//主持人编码
	var emceeMan = new Ext.form.Hidden({
				name : "model.emceeMan"
	})
	//地点
	var place = new Ext.form.TextField({
				fieldLabel : "地点",
				name : 'model.place',
				readOnly : true,
				anchor : '95%'
	});
	//参加人员
	var joinMan = new Ext.form.TextArea({
				fieldLabel : "参加人员",
				name : 'model.joinMan',
				readOnly : true,
				anchor : '95%'
			})
	// 附件名称
	var annex = {
		id : "annex",
		xtype : 'textfield',
		isFormField:true,
		name : "annex",
		fieldLabel : '活动内容',
	//	fileUpload : true,
		height : 22,
		readOnly : true,
		anchor : "95%"
		//buttonCfg : {
		//	text : '浏览...',
		//	iconCls : 'upload-icon'
		//}
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
				name : 'model.memo',
				readOnly : true,
				anchor : '95%'
			})
	// 填报人姓名
	var fillName = new Ext.form.TextField({
				fieldLabel : "填报人",
				readOnly : true,
				name : 'fillName',
				anchor : '90%'

			})
	// 填报人编码
	var fillBy = new Ext.form.Hidden({
				name : 'model.fillBy'

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
							items : [topic]
						}, {
							columnWidth : .5,
							layout : 'form',
							border : false,
							items : [date]
						}, {
							columnWidth : .5,
							layout : 'form',
							border : false,
							items : [emceeName]
						}, {
							border : false,
							layout : 'form',
							columnWidth : 1,
							items : [place]
						}, {
							border : false,
							layout : 'form',
							columnWidth : 1,
							items : [joinMan]
						}, {
							columnWidth : 0.8,
							border : false,
							layout : 'form',
							items : [annex]
						},{
							columnWidth : 0.2,
							border : false,
							layout : 'form',
							items : [btnView]
						}, {
							border : false,
							layout : 'form',
							columnWidth : 1,
							items : [memo]
						}, {
							columnWidth : .5,
							layout : 'form',
							border : false,
							items : [fillName]
						}, {
							columnWidth : .5,
							layout : 'form',
							border : false,
							items : [fillDate]
						}, {
							border : false,
							layout : 'form',
							columnWidth : 1,
							items : [fillBy,emceeMan,jdhdId,zyId]
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
			text : '取消',
			iconCls:'cancer',
			handler : function() { 
				win.hide();
			}
		}]

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
				name : 'model.jdhdId'
			}, {
				name : 'model.jdzyId'
			}, {
				name : 'model.mainTopic'
			}, {
				name : 'model.hdDate'
			}, {
				name : 'model.emceeMan'
			}, {
				name : 'model.joinMan'
			}, {
				name : 'model.place'
			}, {
				name : 'model.content'
			}, {
				name : 'model.memo'
			}, {
				name : 'model.fillBy'
			}, {
				name : 'model.fillDate'
			},{
				name : 'fillDate'
			}, {
				name : 'hdDate'
			}, {
				name : 'fillName'
			}, {
				name : 'emceeName'
			}]);

	var westgrids = new Ext.data.JsonStore({
				url : 'productionrec/findPtJJdhdjlList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});
	//模糊查询条件
	var topicName = new Ext.form.TextField({
		id : "topicName",
		readOnly : false,
		width : 200,
		emptyText : "（活动主题）"
	})
	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
				ds : westgrids,
				columns : [westsm, new Ext.grid.RowNumberer(),  {
							header : "活动主题",
							width : 100,
							//align : "center",
							sortable : true,
							dataIndex : 'model.mainTopic'
						},{
							header : "活动内容",
							width : 75,
							sortable : true,
							dataIndex : 'model.content',
							renderer:function(v){
								if(v !=null && v !='')
								{ 
									var s =  '<a href="#" onclick="window.open(\''+v+'\');return  false;">[查看]</a>';
									return s;
								}
							} 
			
		                  }],
				viewConfig : {
			                 forceFit : true
		           },
				tbar : ["模糊查询：",topicName,{
			              id : 'btnQuery',
					       text : "查询",
						   iconCls : 'query',
					       handler : queryRecord
		                }, {
							xtype : "tbseparator"
						}, {
			               text : "查看详细信息",
			               iconCls : 'list',
			              handler : function() {updateRecord()}
		                 }],
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
			jdzyId : jdzyId,
				topicName : topicName.getValue()
		}
		westgrids.load({
			params : {			
				start : 0,
				limit : 18				
			}
		});
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
		        if(record.get("model.content")!=null&&record.get("model.content")!="")
		        {
		              bview=record.get("model.content");
		              btnView.setVisible(true);
		              Ext.get("annex").dom.value = bview.replace('/power/upload_dir/productionrec/','');
		        }
		        else
		        {
		        	  btnView.setVisible(false);
		        }
				win.setTitle("活动纪要信息");
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
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