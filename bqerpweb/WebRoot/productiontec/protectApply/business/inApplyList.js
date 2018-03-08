//Ext.ns("ProductApply.InOrOutMaint");
//ProductApply.InOrOutMaint = function(_type) { 
Ext.onReady(function() {
	var type="I";
	function getMin() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		t = d.getHours();
		s += (t > 9 ? "" : "0") + t + ":";
		t = d.getMinutes();
		s += (t > 9 ? "" : "0") + t ;
		

		return s;
	};
	
	var sm = new Ext.grid.CheckboxSelectionModel();
	var rn = new Ext.grid.RowNumberer({
		header : '序号',
		width : 60,
		align : 'center'
	})

	var colModel = new Ext.grid.ColumnModel({
		columns : [sm, rn,{
			dataIndex : 'applyCode',
			header : '投入单编号',
			width : 150,
			renderer : function(value,metadata,record){
				if(value != null){
					return record.get('applyCode');
				}
			}
		}, {
			dataIndex : "protectionType",
			header : '投退保护类型',
			width :300,
			renderer : function(value, metadata, record) {
				if (value == '1')
				//update by sychen 20100824
				   return "设备继电保护及安全自动装置、调动自动化投入申请单";
//					return "其它继电保护及安全自动装置、调度自动化设备投、退申请单";
				else if (value == '2')
					return "设备继电保护及安全自动装置、调动自动化退出申请单";
				else if (value == '3')
					return "热控保护投入申请单";
				else if (value == "4")
					return "热控保护退出申请单";
			}
		},{
			dataIndex : "blockId",
			header : '机组',
			width : 150,
			renderer : function(v){
				if(v==1)
				return '300MW机组';
				else if(v==4)
				return '125MW机组';
			}
		}, {
			dataIndex : "applyDep",
			header : '申请部门',
			width : 150,
			renderer:function(value, metadata, record) {
								if (value != null)
									return record.get('applyDepName')}
			
		}, {
			dataIndex : "applyBy",
			align : 'left',
			header : '申请人',
			width : 150,
			renderer : function(v, metadata,record) {
				if (v != null)
					return record.get('applyByName')
			}
		}, {
			dataIndex : "applyTime",
			header : '申请时间',
			width : 150
		}, {
			dataIndex : "protectionName",
			header : '保护名称',
			width : 150
		}],
		defaultSortable : false
	});

	var OUTRecord = Ext.data.Record.create([{
		name : 'applyId',
		mapping : 0
	}, {
		name : 'applyCode',
		mapping : 1
	}, {
		name : 'inOut',
		mapping : 2
	}, {
		name : 'protectionType',
		mapping : 3
	}, {
		name : 'applyDep',
		mapping : 4
	},{
		name : 'applyDepName',
		mapping : 5
	}, {
		name : 'applyBy',
		mapping : 6
	}, {
		name : 'applyByName',
		mapping : 7
	}, {
		name : 'applyTime',
		mapping :8
	}, {
		name : 'protectionName',
		mapping : 9
	}, {
		name : 'protectionReason',
		mapping :10
	}, {
		name : 'measures',
		mapping : 11
	}, {
		name : 'workflowNo',
		mapping : 12
	}, {
		name : 'status',
		mapping : 13
	}, {
		name : 'executeTime',
		mapping : 14
	}, {
		name : 'memo',
		mapping : 15
	},{
		name : 'blockId',
		mapping : 16
	}]);
	
	
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : "productionrec/getProtectApply.action"
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list'
		}, OUTRecord)
	});
	
	
	var bbar = new Ext.PagingToolbar({
		setPagePosition : (600, 700),
		pageSize : 18,
		store : ds,
		displayInfo : true,
		displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
		emptyMsg : "没有记录"
		
	})
	 
	var btnUpdate = new Ext.Button({
		text : '修改',
		iconCls : 'update',
		handler : updateProtect
	});
	var btnDelete = new Ext.Button({
		text : '删除',
		iconCls : 'delete',
		handler : deleteProtect
	});
	 
	var btnUpcommit = new Ext.Button({
		text : '上报',
		iconCls : 'upcommit',
		handler : reportProtect
	});
	var btnCancel = new Ext.Button({
		text : '取消',
		iconCls : 'cancer',
		handler : calcel
	});
	var tbar = new Ext.Toolbar({
		height : 25,
		items : [new Ext.Button({
			text : '新增',
			iconCls : 'add',
			handler : function addProtect() {
				method = "add";
				getApplyInNo();
				win.show();
				Protectform.getForm().reset();
				protectType.focus();
			}
		}), btnUpdate, btnDelete, btnUpcommit]
	});

	function reportProtect() {
		if (ds.getCount() == 0) {
			Ext.Msg.alert("提示", "无上报数据");
			return false;
		}
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		
		if(selected.length>1||selected.length==0)
		{
			Ext.Msg.alert("提示", "请选择一条数据上报！");
			return;
		}
	
		var url = "reportSign.jsp";
		
//		var Id = ds.getAt(0).get("applyId");
		Id=selected[0].get("applyId")
		var workFlowNo = ds.getAt(0).get("workFlowNo");
		var type=selected[0].get("protectionType");
		var args = new Object();
		args.entryId = workFlowNo;
		args.type =type;
		args.applyId = Id;
		args.title="投退保护数据上报";
		args.flowCode = "bqCastBackProtectApprove";
		
	    
		var obj = window.showModalDialog(url, args,
				'status:no;dialogWidth=770px;dialogHeight=550px');
				ds.reload();
				
		if (obj) {
			ds.reload();
		}
	}
	
	function updateProtect() {
		method = "update";
		var rec = grid.getSelectionModel().getSelections();
		if (rec.length != 1) {
			Ext.Msg.alert('提示信息', "请选择一行！");
			return false;
		} else {
			win.show();
			fillFormValue(rec[0]);
			//Ext.get('applyCode').setDisable = true;
			// useDomain.el.dom.readOnly = true;
		}

	}
	function fillFormValue(rec) {
		Protectform.setTitle("保护申请单修改");
		
	   	applyCode.setValue(rec.get("applyCode"));
		applyDepName.setValue(rec.get("applyDepName"));
		applyByName.setValue(rec.get("applyByName"));
		protectType.setValue(rec.get("protectionType"));
		protecName.setValue(rec.get("protectionName"));
		stateComboBox.setValue(rec.get("blockId"));
		reason.setValue(rec.get("protectionReason"));
		
		measures.setValue(rec.get("measures"));
		memo.setValue(rec.get("memo"));

	}
	function deleteProtect() {
		var records = grid.selModel.getSelections();
		var ids = [];
		if (records.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {

			for (var i = 0; i < records.length; i += 1) {
				var member = records[i];
				if (member.get("applyId")) {
					ids.push(member.get("applyId"));
					ids.join(",");
				} else {
					ds.remove(ds.getAt(i));
				}
			}
			Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(buttonobj) {
				if (buttonobj == "yes") {
					Ext.lib.Ajax.request('POST',
							"productionrec/deleteProtect.action", {
								success : function(action) {
									Ext.Msg.alert("提示", "删除成功！")
									ds.reload();
								},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
							}, 'ids=' + ids);
				}
			});
		}
	}
	function calcel() {
		win.hide();
		Protectform.getForm().reset();

	}
 
	function saveProtect() {
		var record = grid.getSelectionModel().getSelected();
		var accidentId;
		if (typeof(record) == "undefined") {

		} else {
			applyId = record.get("applyId");
		}
		if (method == "add") {
			myurl = "productionrec/addProtectApply.action"
		} else {
			myurl = "productionrec/updateProtectApply.action?applyId="
					+ applyId
		}
		Protectform.getForm().submit({
			url :  myurl,
			method : 'post',
			params:{
				type:type
				
			},
			waitMsg : '正在保存数据...',
			
			success : function(form, action) {
				var o = eval('(' + action.response.responseText + ')');

				ds.reload();
				win.hide();
				Protectform.getForm().reset();
				grid.getView().refresh();
				Ext.Msg.alert('提示', "操作成功");
				win.hide();
				type="I";
			},
			failure : function(form, action) {
				Ext.Msg.alert('错误', "操作失败 ");
			}
		});

	}

	var protectType = new Ext.form.ComboBox({
		name : 'protectionType',
		hiddenName : 'entity.protectionType',
		store : new Ext.data.SimpleStore({
			fields : ['name', 'value'],
			data : [['设备继电保护及安全自动装置、调动自动化投入申请单','1'],
					['设备继电保护及安全自动装置、调动自动化退出申请单', '2'],
//			        ['1:380V电压等级及以上设备继电保护及安全自动装置投、退申请单', '1'],
					['热控保护投入申请单', '3'],
					['热控保护退出申请单', '4']]
		}),
		value : "1",
		fieldLabel : '投退保护类型',
		triggerAction : 'all',
		readOnly : true,
		valueField : 'value',
		displayField : 'name',
		mode : 'local',
		width : 150,
		anchor : '95%',
		listeners : {
			"select" : function() {
				if (protectType.getValue()=="1"||protectType.getValue()=="3") {
					type="I";
				} else {
					type="O";
				};
				getApplyInNo();
			}
		}
	})

	var applyID = new Ext.form.Hidden({
		id : "contentId",
		fieldLabel : 'ID',
		readOnly : true,
		value : '',
		name : 'entity.applyId'
	});	
	var applyDept = new Ext.form.Hidden({
		fieldLabel : 'name',
		readOnly : true,
		value : '',
		name : 'entity.applyDep'
	});	
	//增加投退保护单编号，自动生成
	//继电保护：继电+年份+月份+流水号+投/退
    //热控保护：热控+年份+月份+流水号+投/退
	
	var applyCode = new Ext.form.TextField({
//		id:'applyCode',
		name : 'entity.applyCode',
		xtype : 'textfield',
		fieldLabel : '申请单编号',
		readOnly : true,
		anchor : '95%'
	});
	var stateData = new Ext.data.SimpleStore({
				data : [[1, '300MW机组'], [4,'125MW机组']],
				fields : ['value', 'name']
			});
	// 定义状态
	var stateComboBox = new Ext.form.ComboBox({
//				id : "blockId",
				fieldLabel:'机组',
				name:'stateComboBox',
				store : stateData,
				displayField : "name",
				valueField : "value",
				mode : 'local',
				triggerAction : 'all',
				hiddenName:'entity.blockId',
				readOnly : true,
				value:'',
		        anchor : '95%'
//				width : 120
			}); 

	var applyDepName = new Ext.form.TextField({
		name : 'entity.applyDep',
		xtype : 'textfield',
		fieldLabel : '申请部门',
		readOnly : true,
		/*allowBlank : false,*/
		anchor : '95%'
	});
	var applyBy = new Ext.form.Hidden({
		fieldLabel : 'name',
		readOnly : true,
		value : '',
		name : 'entity.applyBy'
	});	

	var applyByName = new Ext.form.TextField({
		name : 'entity.applyByName',
		xtype : 'textfield',
		fieldLabel : '申请人',
		readOnly : true,
		/*allowBlank : false,*/
		anchor : '95%'
	});
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号，赋给全局变量
//							fillBy.setValue(result.workerCode);
							applyByName.setValue(result.workerName);
							applyDepName.setValue(result.deptName);
						
							

						}
					}
				});
	}
	 getWorkCode();
	//自动生成投退单编号
	function getApplyInNo(){
		//传递的参数：投退保护专业、类型
		var special = protectType.getValue() ;
		//var type = 1;//1表示投入
		//alert(ttType);
		Ext.lib.Ajax.request('POST','productionrec/getApplyNo.action?special=' + special + '&&type=' + ttType,
		{
			success : function(action){
				applyCode.setValue(action.responseText);
			}
		});
	}
	//getApplyInNo();
	var applyTime = new Ext.form.TextField({
		name : 'entity.applyTime',
		xtype : 'textfield',
		fieldLabel : '申请时间',
		readOnly : true,
		/*allowBlank : false,*/
		anchor : '95%'
	});
    applyTime.setValue(getMin());
    /*var time=getMin();
	var time1=time.substring(0,4)+'-'+time.substring(5,7)+'-'+time.substring(8,10)+'-'+time.substring(11,13)+'-'+time.substring(14,16);
	*/
	var domainDesc = new Ext.form.TextField({
		name : 'entity.domainDesc',
		xtype : 'textfield',
		fieldLabel : '使用域名称',
		readOnly : false,
		anchor : '100%'
	});

	var protecName = new Ext.form.TextArea({
		name : 'entity.protectionName',
		fieldLabel : '保护名称',
		readOnly : false,
		anchor : '95%'
	});
	var reason = new Ext.form.TextArea({
		name : 'entity.protectionReason',
		fieldLabel : '退出原因',
		readOnly : false,
		anchor : '95%'
	});
	var measures = new Ext.form.TextArea({
		name : 'entity.measures',
		fieldLabel : '安全,技术措施',
		readOnly : false,
		anchor : '95%'
	});
	var memo = new Ext.form.TextArea({
		name : 'entity.memo',
		fieldLabel : '备注',
		readOnly : false,
		anchor : '95%'
	});
	memo
			.setValue("本申请单一式4份，由执行人打印、回填。执行完毕，由申请人送申请部门、执行部门、值长、设备部各1份，分别保留存档并建立台帐，存档不少于3年.");

	var fiedset = new Ext.form.FieldSet({
//		title : '保护申请单填写',
		layout : 'form',
		autoHeight : true,
		labelWidth : 90,
		labelAlign : 'right',
		frame : true,
		items : [protectType,applyCode,applyID, applyDept, applyDepName,stateComboBox,applyBy,applyByName, applyTime,
				protecName, reason, measures, memo]

	});

	var Protectform = new Ext.FormPanel({
		title : '保护申请单填写',
		frame : true,
		autoWidth : true,
		autoHeight : true,
		align : 'center',
		layout : 'fit',
		labelAlign : 'left',
		items : [fiedset],
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : saveProtect
		}, btnCancel]
	});
	var win = new Ext.Window({
		width : 550,
		height : 550,
		modal : true,
		closeAction : 'hide',
		items : [Protectform]
	});

	var grid = new Ext.grid.GridPanel({
		ds : ds,
		cm : colModel,
		sm : sm,
		bbar : bbar,
		tbar : tbar,
		border : false,
		autoWidth : true,
		fitToFrame : true
	});
	var inOut=null;
	ds.baseParams = {
		//inOut : type,
		approve : "",
		status : "'0','9'"
	}
			            ds.load({
		params : {
			start : 0,
			limit : 18
		}
	});
//	return {
//		grid:grid
//	}
	var view = new Ext.Viewport({
		layout : 'fit',
		items : [grid]
	});
});