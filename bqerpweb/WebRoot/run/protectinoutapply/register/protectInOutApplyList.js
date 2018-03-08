Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif'; 

var protectApply=new ProtectApply();

Ext.onReady(function() {

	// 系统当天日期
	var sdate = new Date();
	var edate = new Date();
	// 系统当天前30天的日期
	sdate.setDate(sdate.getDate() - 30);
	// 系统当天后30天的日期
	edate.setDate(edate.getDate() + 30);
	
	var fromDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : 'startDate',
		id : 'fromDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		checked : true,
		fieldLabel : "申请日期",
		allowBlank : false,
		readOnly : true,
		value : sdate,
		emptyText : '请选择',
		anchor : '100%'
	});
	var toDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : 'endDate',
		value : edate,
		id : 'toDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		fieldLabel : "至",
		allowBlank : false,
		readOnly : true,
		emptyText : '请选择',
		anchor : '80%'
	});
	
	
	// 定义状态
	var stateComboBox = new Ext.form.ComboBox({
		id : "stateCob",
		store : protectApply.reportStatus,
		displayField : "name",
		valueField : "value",
		mode : 'local',
		triggerAction : 'all',
		hiddenName : 'stateComboBox',
		readOnly : true,
		value : '',
		width : 120
	}); 
	
	//所属部门
    var topDeptRootNode =  new Ext.tree.AsyncTreeNode({
		id : '-1',
		text : '所有'
	})
    var comboDepChoose = new Ext.ux.ComboBoxTree({
                labelwidth : 50,
                fieldLabel : '部门',
                id : 'deptId',
                displayField : 'text',
                width : 170,
                valueField : 'id',
                hiddenName : 'power.applyDept',
                blankText : '请选择',
                value : topDeptRootNode,
                emptyText : '请选择', 
                readOnly : true,
                tree : {
                    xtype : 'treepanel',
                    loader : new Ext.tree.TreeLoader({
                                dataUrl : 'comm/getDeptsByPid.action'
                            }),
                    root :  new Ext.tree.AsyncTreeNode({
                                id : '-1',
                                text : '所有'
                            })
                }
                //selectNodeModel : 'exceptRoot',
                ,selectNodeModel : 'all'
              
            });
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'power.applyId'
	}, {
		name : 'power.protectNo'
	}, {
		name : 'power.applyDept'
	}, {
		name : 'applyDeptName'
	}, {
		name : 'power.applyBy'
	}, {
		name : 'applyName'
	}, {
		name : 'applyDate'
	}, {
		name : 'power.equCode'
	}, {
		name : 'equName'
	}, {
		name : 'power.protectName'
	}, {
		name : 'power.protectReason'
	}, {
		name : 'power.equEffect'
	}, {
		name : 'power.outSafety'
	}, {
		name : 'power.memo'
	}, {
		name : 'applyStartDate'
	}, {
		name : 'applyEndDate'
	}, {
		name : 'power.statusId'
	}, {
		name : 'statusName'
	}, {
		name : 'power.workFlowNo'
	}, {
		name : 'power.isIn'
	}, {
		name : 'power.isSelect'
	}, {
		name : 'power.relativeNo'
	}]);

	var dataProxy = new Ext.data.HttpProxy(
			{
				url : 'protect/findRegisterList.action'
			}
	);

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);

	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
//分页
		var fuzzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});

	var sm = new Ext.grid.CheckboxSelectionModel();
	var grid = new Ext.grid.GridPanel({
		region : "center",
		store : store,
		columns : [sm, new Ext.grid.RowNumberer({
			header : '序号',
			width : 35,
			align : 'left'
		}), {

			header : "ID",
			width : 100,
			sortable : true,
			dataIndex : 'power.applyId',
			hidden : true
		}, {

			header : "状态ID",
			width : 100,
			sortable : true,
			dataIndex : 'power.statusId',
			hidden : true
		}, {

			header : "申请单号",
			width : 80,
			sortable : true,
			dataIndex : 'power.protectNo'
		}, {

			header : "状态",
			width : 70,
			sortable : true,
			dataIndex : 'power.statusId',
			renderer:function(value)
			{
				
				return protectApply.getStatusName(value);
			}
				},

				{
					header : "申请人编码",
					width : 100,
					sortable : true,
					dataIndex : 'power.applyBy',
					hidden : true
				},

				{
					header : "申请人",
					width : 85,
					sortable : true,
					dataIndex : 'applyName'
				}, {
					header : "申请部门编码",
					width : 100,
					sortable : true,
					dataIndex : 'power.applyDept',
					hidden : true
				}, {
					header : "申请部门",
					width : 70,
					sortable : true,
					dataIndex : 'applyDeptName'
				}, {
					header : "申请时间",
					width : 110,
					sortable : true,
					dataIndex : 'applyDate'
				}, {
					header : "设备名称",
					width : 110,
					sortable : true,
					dataIndex : 'equName'
				}, {
					header : "保护名称",
					width : 110,
					sortable : true,
					dataIndex : 'power.protectName'
				}, {
					header : "投退原因",
					width : 150,
					sortable : true,
					renderer : function(v) {
						return v.replace(/\r/g, "<br/>");
					},
					dataIndex : 'power.protectReason'
				}, {
					header : "对系统或设备的影响",
					width : 150,
					sortable : true,
					renderer : function(v) {
						return v.replace(/\r/g, "<br/>");
					},
					dataIndex : 'power.equEffect'
				}, {
					header : "保护退出时安措",
					width : 150,
					sortable : true,
					renderer : function(v) {
						return v.replace(/\r/g, "<br/>");
					},
					dataIndex : 'power.outSafety'
				}, {
					header : "申请开工时间",
					width : 110,
					sortable : true,
					dataIndex : 'applyStartDate'
				}, {
					header : "申请完工时间",
					width : 110,
					sortable : true,
					dataIndex : 'applyEndDate'
				}, {
					header : "是否投入",
					width : 110,
					sortable : true,
					dataIndex : 'power.isIn',
					renderer : function(v){
						if(v=="Y")
							return "投入";
						else if(v=="N")
							return "退出";
					}
				},{
					header : "备注",
					width : 150,
					sortable : true,
					dataIndex : 'power.memo'
				}],
		sm : sm,
		tbar : ['时间范围：',fromDate,'~',toDate,"&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp部门:", comboDepChoose,"&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp状态:", stateComboBox, {
			text : "查询",
			iconCls : 'query',
			handler : queryRecord
		}, '-', {
			text : "增加",
			iconCls : 'add',
			handler : function() {
				parent.currentRecord=null;
//				parent.document.all.iframe1.src = "run/protectinoutapply/register/protectInOutApplyBase.jsp";
				parent.Ext.getCmp("maintab").setActiveTab(0);
			}

		}, '-', {
			text : "修改",
			iconCls : 'update',
			handler : updateRecord

		}, '-', {
			text : "删除",
			iconCls : 'delete',
			handler : deleteRecord

		}, '-', {
			text : "上报",
			iconCls : 'upcommit',
			handler : reportBtn

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

	grid.on('rowdblclick',updateRecord);
	function updateRecord()
	{		
		if (grid.selModel.hasSelection()) {		
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				var member = records[0];
				parent.currentRecord = member;
				if(member.get("power.isIn")!=null && member.get("power.isIn")!=""){
					if(member.get("power.isIn")=="Y"){
						parent.Ext.getCmp("maintab").setActiveTab(1);
					}else if(member.get("power.isIn")=="N"){
						parent.Ext.getCmp("maintab").setActiveTab(0);
					}
				}
				
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}
	
	function deleteRecord()
	{
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		var nos=[];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("power.applyId")) {
					ids.push(member.get("power.applyId"));
					nos.push(member.get("power.protectNo"));
				} else {
					store.remove(store.getAt(i));
				}
			}
			Ext.Msg.confirm("删除", "是否确定删除申请单'" + nos + "'？", function(
					buttonobj) {
				if (buttonobj == "yes") {
					Ext.lib.Ajax.request('POST',
							'protect/deleteProtectApply.action', {
								success : function(action) {
									Ext.Msg.alert("提示", "删除成功！");
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
     function queryRecord() {
     		var ftime = Ext.get('fromDate').dom.value;
			var ttime = Ext.get('toDate').dom.value;
			if (ftime > ttime) {
				Ext.Msg.alert('提示', '选择后一日期应比前一日期大！');
				return false;
			}
		store.load({
			params : {
				start : 0,
				limit : 18,
				status :stateComboBox.getValue(),
				startDate : Ext.get('fromDate').dom.value,
				endDate : Ext.get('toDate').dom.value,
				applyDept:comboDepChoose.value=='-1'?'':comboDepChoose.value
			
			}
		});
	}
	
	// 上报处理
	function reportBtn() {
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var protectNo;
		var isReport;
		if (selected.length == 0) {
			Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
		} else {
			var menber = selected[0];
			protectNo = menber.get('power.protectNo');
			isReport = menber.get('power.statusId');
			if (isReport !="1"
					&& isReport != "2") {
				Ext.Msg.alert("系统提示信息", "只有未上报和已退回的票允许上报");
			} else {
				
				var args=new Object();
				args.busiNo=protectNo;
		//		args.flowCode="bqStopSendElec";
				args.entryId=menber.get('power.workFlowNo');
				
				args.title="保护投退申请单上报";
				  var danger = window.showModalDialog('protectApplyReportSign.jsp',
                args, 'dialogWidth=700px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');
               queryRecord();
			}
		}
	}
//---------------------------------------

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});
	
	queryRecord();

});