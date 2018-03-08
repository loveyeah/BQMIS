Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	// 获得年份
	function getDate() {
		var Y;
		Y = new Date();
		Y = Y.getFullYear().toString(10);
		return Y;
	}
	// 获得月份
	function getMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString() + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		return s;
	}

	// 系统当前时间
	function getTime() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		return s;
	}
	
	// 定义部门
	var deptData = new Ext.data.JsonStore({
		root : 'list',
		url : "managebudget/findBudgetDeptList.action",
		fields : ['centerId', 'depName']
	});
	deptData.on('load', function(e, records) {
		var record1=new Ext.data.Record({
    	depName:'所有',
    	centerId:''}
    	);
    	deptData.insert(0,record1);
    	deptComboBox.setValue('');
	});
	deptData.load();
	
		var deptComboBox = new Ext.form.ComboBox({
		id : "deptComboBox",
		store : deptData,
		displayField : "depName",
		valueField : "centerId",
		mode : 'local',
		width : 134,
		triggerAction : 'all',
		readOnly : true
	});

	
		// 年月份选择
	var formatType;
	var yearRadio = new Ext.form.Radio({
		id : 'year',
		name : 'queryWayRadio',
		// blankText : getDate(),
		hideLabel : true,
		boxLabel : '年份'
	});
	var monthRadio = new Ext.form.Radio({
		id : 'month',
		name : 'queryWayRadio',
		hideLabel : true,
		boxLabel : '月份',
		checked : true,
		listeners : {
			check : function() {
				var queryType = getChooseQueryType();
				switch (queryType) {
					case 'year' : {
						formatType = 1;
						time.setValue(getDate());
						break;
					}
					case 'month' : {
						time.setValue(getMonth());
						formatType = 2;
						break;
					}
				}
			}
		}
	});

	var time = new Ext.form.TextField({
		id : 'time',
		allowBlank : true,
		readOnly : true,
		value : getMonth(),
		width : 100,
		listeners : {
			focus : function() {
				var format = '';
				if (formatType == 1)
					format = 'yyyy';
				if (formatType == 2)
					format = 'yyyy-MM';
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : format,
					alwaysUseStartDate : false,
					onclearing : function() {
						planStartDate.markInvalid();
					}
				});
			}
		}
	});
	// 遍历所有的REDIO获得ID
	function getChooseQueryType() {
		var list = document.getElementsByName("queryWayRadio");
		for (var i = 0; i < list.length; i++) {
			if (list[i].checked) {
				return list[i].id;
			}
		}
	}
	
//----------------------------------------------------------------	

	var sm = new Ext.grid.CheckboxSelectionModel();
	// 创建记录类型
	var Myrecord = Ext.data.Record.create([
		 {
		name : 'cjm.happenId'
	}, {
		name : 'cjm.happenValue'
	}, {
		name : 'cjm.workFlowNo'
	}, {
		
		name : 'itemName'
	}, {
		
		name : 'adviceBudget'
	}, {
		
		name : 'fillName'
	}, {
	
		name : 'fillTime'
	},{name:'cjm.happenExplain'}]);
	// 配置数据集
	var store = new Ext.data.Store({
		/* 创建代理对象 */
		proxy : new Ext.data.HttpProxy({
			url : 'managebudget/masterItemApproveQuery.action'
		}),
		/* 创建解析Json格式数据的解析器 */
		reader : new Ext.data.JsonReader({
			root : "list",
			totalProperty : "totalCount"
		}, Myrecord)
	});
    
	function queryRecord() {
		
		store.baseParams = {
			budgetTime :  time.getValue(),
			centerId:deptComboBox.getValue()
		};
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}
	
	
	// 查询
	var query = new Ext.Button({
		id : "btnQuery",
		text : "查询",
		iconCls : "query",
		handler :queryRecord
	})
	
	// 定义grid
	// 事件状态
	var sm_store_item = new Ext.grid.ColumnModel([sm,  {
				header : '预算指标',
				align : 'center',
				width : 110,
				sortable : true,
				dataIndex : 'itemName'
			}, {
				header : '预算值',
				align : 'center',
				width : 110,
				sortable : true,
				dataIndex : 'adviceBudget'
			}, {
				header : '发生值',
				align : 'center',
				width : 110,
				sortable : true,
				dataIndex : 'cjm.happenValue'
			}, {
				header : '报销人',
				align : 'center',
				width : 110,
				sortable : true,
				dataIndex : 'fillName'
			}, {
				header : '报销日期',
				align : 'center',
				width : 110,
				sortable : true,
				dataIndex : 'fillTime'
			},{
			
			header : '报销说明',
				align : 'center',
				width : 110,
				sortable : true,
				dataIndex : 'cjm.happenExplain'
			}]);

	// 底部分页栏
	var bbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : store,
		displayInfo : true,
		displayMsg : "{0} 到 {1} /共 {2} 条",
		emptyMsg : "没有记录",
		beforePageText : '',
		afterPageText : ""
	});
	// 顶部工具栏
	var tbar = new Ext.Toolbar({
		items : [yearRadio, monthRadio, time,'-','归口部门',deptComboBox,'-',query]
	});
	// 可编辑的表格
	var Grid = new Ext.grid.GridPanel({
		sm : sm,
		ds : store,
		cm : sm_store_item,
		autoScroll : true,
		bbar : bbar,
		tbar : tbar,
		border : true,
		clicksToEdit : 1,
		viewConfig : {
		 forceFit : true
		}
	});
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		border : false,
		frame : false,
		items : [{
			layout : 'fit',
			border : false,
			frame : false,
			region : "center",
			items : [Grid]
		}]
	});
	
	
	Grid.on("rowdblclick", report);
	function report()
	{
		//----------------
		var record = Grid.getSelectionModel().getSelected();
		var workFlowNo = record.get('cjm.workFlowNo');
		Ext.Ajax.request({
			url : 'MAINTWorkflow.do?action=getCurrentStepsInfo',
			method : 'POST',
			params : {
				entryId : workFlowNo
			},
			success : function(result, request) {
				var url="";
				var obj = eval("(" + result.responseText + ")");
				var args = new Object();
				if (obj[0].url==null||obj[0].url=="") {
					url="approveSign.jsp";
				} 
				else
				{
					 url = "../../../../../" + obj[0].url; 
				} 
					args.busiNo = record.get('cjm.happenId');
					args.entryId = record.get("cjm.workFlowNo"); 
					var obj = window.showModalDialog(url, args,
					'status:no;dialogWidth=750px;dialogHeight=550px');
					if(obj)
					{
					  store.load();
					}
				
			},
			failure : function(result, request) {
				Ext.Msg.alert("提示","错误");
			}

		});
		//-----------------
		
		
	}
	
	queryRecord();
	
})
