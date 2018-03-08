Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();

	function getParameter(psName) {
		var url = self.location;
		var result = "";
		var str = self.location.search.substring(0);
		if (str.indexOf(psName) != -1
				&& (str.substr(str.indexOf(psName) - 1, 1) == "?" || str
						.substr(str.indexOf(psName) - 1, 1) == "&")) {
			if (str.substring(str.indexOf(psName), str.length).indexOf("&") != -1) {
				var Test = str.substring(str.indexOf(psName), str.length);
				result = Test.substr(Test.indexOf(psName), Test.indexOf("&")
								- Test.indexOf(psName));
				result = result.substring(result.indexOf("=") + 1,
						result.length);
			} else {
				result = str.substring(str.indexOf(psName), str.length);
				result = result.substring(result.indexOf("=") + 1,
						result.length);
			}
		}
		return result;
	}
	
	var woCode = getParameter("woCode");
	
	var foWoCode =  getParameter("woCode");
	
// 系统当前时间
	var enddate = new Date();
	// 系统当前时间前七天
	var startdate = new Date();
//	var currentCode = parent.document.getElementById("workerCode").value;
//	var methods = "send";
	startdate.setDate(enddate.getDate() - 7);
	function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";
		Year = DateIn.getYear();
		Month = DateIn.getMonth() + 1;
		Day = DateIn.getDate();
		CurrentDate = Year + "-";
		if (Month >= 10) {
			CurrentDate = CurrentDate + Month + "-";
		} else {
			CurrentDate = CurrentDate + "0" + Month + "-";
		}
		if (Day >= 10) {
			CurrentDate = CurrentDate + Day;
		} else {
			CurrentDate = CurrentDate + "0" + Day;
		}
		return CurrentDate;
	};
	
	var MyRecord = new Ext.data.Record.create([{
		name : 'equJWo.woId'
	}, {
		name : 'equJWo.woCodeShow'
	},{
		name : 'equJWo.woCode'
	}, {
		name : 'equJWo.faWoCode'
	}, {
		name : 'equJWo.workorderContent'
	}, {
		name : 'equJWo.workorderStatus'
	}, {
		name : 'equJWo.workorderType'
	}, {
		name : 'equJWo.professionCode'
	}, {
		name : 'equJWo.crewId'
	}, {
		name : 'equJWo.maintDep'
	}, {
		name : 'equJWo.repairModel'
	}, {
		name : 'equJWo.repairmethodCode'
	}, {
		name : 'equJWo.filepackageCode'
	}, {
		name : 'equJWo.relationFilepackageMemo'
	}, {
		name : 'equJWo.ifWorkticket'
	}, {
		name : 'equJWo.equCode'
	}, {
		name : 'equJWo.kksCode'
	}, {
		name : 'equJWo.equName'
	}, {
		name : 'equJWo.equPostionCode'
	}, {
		name : 'equJWo.remark'
	}, {
		name : 'equJWo.ifOutside'
	}, {
		name : 'equJWo.ifFireticket'
	}, {
		name : 'equJWo.ifMaterials'
	}, {
		name : 'equJWo.ifReport'
	}, {
		name : 'equJWo.ifContact'
	}, {
		name : 'equJWo.ifConform'
	}, {
		name : 'equJWo.ifRemove'
	}, {
		name : 'equJWo.ifCrane'
	}, {
		name : 'equJWo.ifFalsework'
	}, {
		name : 'equJWo.ifSafety'
	}, {
		name : 'equJWo.projectNum'
	}, {
		name : 'equJWo.requireStarttime'
	}, {
		name : 'equJWo.requireEndtime'
	}, {
		name : 'equJWo.planStarttime'
	}, {
		name : 'equJWo.planEndtime'
	}, {
		name : 'equJWo.factStarttime'
	}, {
		name : 'equJWo.factEndtime'
	}, {
		name : 'equJWo.repairDepartment'
	}, {
		name : 'equJWo.workChargeCode'
	}, {
		name : 'equJWo.professionHeader'
	}, {
		name : 'equJWo.requireManCode'
	}, {
		name : 'equJWo.requireTime'
	}, {
		name : 'equJWo.checkManCode'
	}, {
		name : 'equJWo.checkTime'
	}, {
		name : 'equJWo.checkReportid'
	}, {
		name : 'equJWo.checkResultid'
	}, {
		name : 'equJWo.checkReasonid'
	}, {
		name : 'equJWo.requireWotime'
	}, {
		name : 'equJWo.requireWofee'
	}, {
		name : 'equJWo.workFlowNo'
	}, {
		name : 'equJWo.wfState'
	}, {
		name : 'equJWo.assembly'
	}, {
		name : 'equJWo.planWotime'
	}, {
		name : 'equJWo.planWofee'
	}, {
		name : 'equJWo.factWotime'
	}, {
		name : 'equJWo.factWofee'
	}, {
		name : 'equJWo.enterprisecode'
	}, {
		name : 'equJWo.ifUse'
	},{
		name : 'wokerName'
	},{
		name : 'deptName'
	},{
		name: 'workChargeName'
	},{
		name : 'equName'
	},{
		name : 'professionName'
	}]);
	
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'workbill/findByFaWoCode.action',
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list'
		}, MyRecord)
	});
	ds.load({
		params : {
			start : 0,
			limit : 18,
			starttime : ChangeDateToString(startdate),
			endtime : ChangeDateToString(enddate),
			woCode : woCode
		}
	});
//	alert(ChangeDateToString(startdate))
	ds.on('beforeload', function() {
			if(Ext.get("relateBill").dom.checked){
			var	ifWorkticket = "Y"
			}
			if(Ext.get("relateMateriel").dom.checked){
			var	ifMaterials = "Y"
			}
		Ext.apply(this.baseParams, {
						starttime : ChangeDateToString(applyfromDate.getValue()),
						endtime : ChangeDateToString(applytoDate.getValue()),
						ifWorkticket : ifWorkticket,
						ifMaterials : ifMaterials,
						workorderStatus : workorderStatus.getValue(),
//						workorderType : Ext.get("workorderType").dom.value,
						workorderType :workorderType.getValue(),
//						professionCode : Ext.get("professionCode").dom.value,
						professionCode : professionCode.getValue(),
//						repairDepartment :  Ext.get("repairDepartment").dom.value,
						repairDepartment :  repairDepartment.getValue(),
						argFuzzy :Ext.getCmp("query").value,
						woCode : woCode
					});
	});
	
	//查询条件
	//时间条件
	var applyfromDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : 'requireStarttime',
		id : 'applyfromDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		checked : true,
		fieldLabel : "申请时间",
		allowBlank : false,
		readOnly : true,
		value : startdate,	
		emptyText : '请选择',
		width : 90
	});
		var applytoDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : 'requireEndtime',
		value : enddate,
		id : 'applytoDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		fieldLabel : "至",
		allowBlank : false,
		readOnly : true,
		emptyText : '请选择',
		width : 90
});

		//状态
	var workorderStatus = new Ext.form.ComboBox({
		id : "workorderStatus",
		name : "workorderStatus",
		hiddenName : "workStatus",
		fieldLabel : "状态",
		emptyText : '请选择...',
		store :  [['0', '开始工作'], ['1', '工作结束'], ['2', '审核完结'],
						['3', '审核退回'], [null, '全部']],
//		displayField : "workorderStatus",
//		valueField : "workorderStatus",
//		allowBlank : false,
		readOnly : true,
		triggerAction : 'all',
		width : 120
	});

		//类型
	
// 工单类型

	var workorderType = new Ext.form.ComboBox({
		id : "workorderType",
		hiddenName : "workorderTypeName",
		name  : "workorderType",
		fieldLabel : "工单类型",
		emptyText : '请选择...',
		store : [['0', '预防性维护工单'], ['1', '消缺工单'],['2','委外工单'],['3','大修工单'],
			    ['4','小修工单'],[null,'全部']],
		displayField : "workorderType",
		valueField : "workorderType",
		allowBlank : false,
		triggerAction : 'all',
		readOnly : true,
		width : 90
	});
	
	// 专业
	var url = "timework/useprolist.action";
	var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", url, false);
	conn.send(null);
	var search_data4 = eval('(' + conn.responseText + ')');
		//专业
	var professionCode = new Ext.form.ComboBox({
				id : "professionCode",
				name : "professionCode",
				hiddenName : "profession",
				fieldLabel : "专业",
				triggerAction : 'all',
				store : new Ext.data.SimpleStore({
							fields : ['name', 'id'],
							data : search_data4
						}),
				displayField : "name",
				valueField : "id",
				emptyText : '请选择...',
				mode : 'local',
				readOnly : true,
				width : 90
			});
	
		//检修部门
		var repairDeptStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'equfailure/repairDept.action',
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, [{
			name : 'deptCode'
//			mapping : 'deptCode'
		}, {
			name : 'deptName'
//			mapping : 'deptName'
		}])
	});
	repairDeptStore.load();
		var repairDepartment = new Ext.form.ComboBox({
		id : 'repairDepartment',
		store : repairDeptStore,
		fieldLabel : "检修部门",
		valueField : "deptCode",
		displayField : "deptName",
		mode : 'local',
		hiddenName : 'repairDepart',
		triggerAction : 'all',
		blankText : '请选择...',
		emptyText : '请选择...',
		readOnly : true,
		width : 120
	});

	
	//模糊查询
	var query = {
		id : "query",
		xtype : "textfield",
		fieldLabel : '模糊查询',
		name : 'query',
//		readOnly : true,
		emptyText : '根据工单编码、KKS编码、申请人',
		value : ' ',
		anchor : "90%"
//		width : 300
	}
	 // 关联工作票
    var relateBill = new Ext.form.Checkbox({
        id : 'relateBill',
        boxLabel : "关联工作票",
        name : 'relateBill',
        width : 180,
        value : "rb",
        inputValue : 'rb',
//         checked : true,
        hideLabel : true
    })
    
    	 // 关联领料单
    var relateMateriel= new Ext.form.Checkbox({
        id : 'relateMateriel',
        boxLabel : "关联领料单",
        value : "rm",
        inputValue : 'rm',
//        checked : true,
		name : 'relateMateriel',
        width : 180,
        hideLabel : true
    })
    
	var fildset = new Ext.form.FieldSet({
		title : '查询条件',
		height : '100%',
//		collapsible : true,
//		border : false,
		layout : 'form',
		items : [{	
			layout : 'column',
			border : false,
			items : [{
				columnWidth : 0.3,
				labelWidth : 70,
				border : false,
				layout : 'form',
				items : [applyfromDate]
			},{
				columnWidth : 0.3,
				labelWidth : 50,
				border : false,
				layout : 'form',
				items : [applytoDate]
			},{
				columnWidth : 0.4,
				border : false,
				layout : 'form',
				items : [workorderStatus]
			}]
		},{	
			layout :'column',
			border : false,
			items : [{
				columnWidth : 0.3,
				labelWidth : 70,
				border : false,
				layout : 'form',
				items : [workorderType]
			},{
				columnWidth : 0.3,
				labelWidth : 50,
				border : false,
				layout : 'form',
				items : [professionCode]
			},{
				columnWidth : 0.4,
				labelWidth : 100,
				border : false,
				layout : 'form',
				align : 'center',
				items : [repairDepartment]
			}]
		},{
			layout :'column',
			border : false,
			items : [{
				columnWidth : 0.5,
				labelWidth : 70,
				border : false,
				layout : 'form',
				items : [query]
			},{
				columnWidth : 0.25,
				border : false,
				layout : 'form',
				items : [relateBill]
				},{
				columnWidth : 0.25,
				border : false,
				layout : 'form',
				items : [relateMateriel]
				}]
		}]
	})
	
//	alert(Ext.get("relateBill").dom.value)
	var btnquerry = new Ext.Button({
		id : "querry",
		text : "查询",
		iconCls : 'query',
		handler : function() {
//			alert(deptCode)
				ds.reload(
				);
				
			}
	})
	var fildset1 = new Ext.form.FieldSet({
		title : '',
		height : 100,
//		collapsible : true,
		border : false,
		layout : 'form',
		items : [{
			layout :'column',
			border : false,
			items : [{
				columnWidth : 1,
				border : false,
				layout : 'form'
			}]
		},{	
			layout : 'column',
			border : false,
			items : [{
				columnWidth : 1,
				border : false,
				layout : 'form',
				items : [btnquerry]
			}]
		},{	
			layout :'column',
			border : false,
			items : [{
				columnWidth : 1,
				border : false,
				layout : 'form'
			}]
			
		},{
			layout :'column',
			border : false,
			items : [{
				columnWidth : 1,
				border : false,
				layout : 'form'
			}]
		
		}]
	})
	
	var mypanl = new Ext.Panel({
		bodyStyle : "padding:10px 0px 0px 0px",
		 region : "center",
		 layout : 'form',
		 autoheight : true,
		autoScroll : true,
		containerScroll : true,
		items : [{
			layout : 'column',
			border : false,
			items : [{
				border : false,
				labelHeight : 100,
				columnWidth : 0.9,
				align : 'center',
				layout : 'form',
				anchor : '100%',
				items : [fildset]
			}, {
				border : false,
				labelHeight : 100,
				columnWidth : 0.1,
				align : 'center',
				layout : 'form',
				anchor : '100%',
				items : [fildset1]
			}]
		}]
	
	
	})	
	/* 设置每一行的选择框 */
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			rowselect : function(sm, row, rec) {
			}
		}
	});

	var cm = new Ext.grid.ColumnModel([sm, new Ext.grid.RowNumberer({
		header : '序号',
		width : 35
	}),{
		header : "woId",
		dataIndex : "equJWo.woId",
		hidden : true
	}
	, {
		header : '工单编码',
		dataIndex : 'equJWo.woCodeShow',
		width : 70
	}, {
		header : '工单类型',
		dataIndex : 'equJWo.workorderType',
		width : 70,
		renderer : function change(v){
			if(v == "0"){
					return "预防性维护工单";
				}else if(v == "1"){
					return "消缺工单";
				}else if(v == "2"){
					return "委外工单";
				}else if(v == "3"){
					return "大修工单";
				}else if(v == "4"){
					return "小修工单";
				}else{
					return "状态异常";
				}
		    }
	}, {
		header : '当前状态',
		dataIndex : 'equJWo.workorderStatus',
		width : 70,
		renderer : function changeIt(val) {
		if(val=="0") return "开始工作";
		if(val=="1") return "工作完成";
			}
	}, {
		header : '工单内容',
		dataIndex : 'equJWo.workorderContent',
		width : 70
	}, {
		header : '设备编码',
		dataIndex : 'equJWo.kksCode',
		width : 70
	}, {
		header : '设备名称',
		dataIndex : 'equName',
		width : 70
	}, {
		header : '工作负责人',
		dataIndex : 'workChargeName',
		width : 70
	}, {
	hidden : true,
	dataIndex : "equJWo.requireManCode"
	},{
		header : '申请人',
		dataIndex : 'wokerName',
		width : 60
	}, {
		header : '检修部门',
		dataIndex : 'deptName',
		width : 70
	}, {
		header : '检修专业',
		dataIndex : 'professionName',
		width : 70
	}]);
	
	/* 设置分页的工具条 */
	var bbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : ds,
		displayInfo : true,
		displayMsg : '显示第 {0} 条到 {1} 条记录，一共 {2} 条',
		emptyMsg : "没有记录"
	})
	

	/* 创建表格 */
	var grid = new Ext.grid.GridPanel({
		id : 'grid',
		ds : ds,
		cm : cm,
		bbar :bbar
		
	});
	
	grid.on('rowdblclick', function(grid, rowIndex, e) {
		var rec = grid.getSelectionModel().getSelected();
		if (rec) {
			var obj=new Object();
			obj.woCode=rec.get('equJWo.woCode');
			obj.woCodeShow=rec.get('equJWo.woCodeShow');
			window.returnValue = obj;
			window.close();
		}
	});
	var viewport = new Ext.Viewport({
		layout : 'border',
//		border : false,
		enableTabScroll : true,
		items : [{
//			layout : 'fit',
			border : false,
			region : "north",
			height : 150,
			split : true,
			margins : '0 0 0 0',
			items : [mypanl]
		},{
			layout : 'fit',
			region : "center",
			border : false,
			collapsible : true,
			split : true,
			margins : '0 0 0 0',
			items : [grid]
		}]
	});
});