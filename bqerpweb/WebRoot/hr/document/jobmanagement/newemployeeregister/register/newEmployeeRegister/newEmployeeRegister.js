Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();

	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t;
//		t = d.getHours();
//		s += (t > 9 ? "" : "0") + t + ":";
//		t = d.getMinutes();
//		s += (t > 9 ? "" : "0") + t + ":";
//		t = d.getSeconds();
//		s += (t > 9 ? "" : "0") + t;
		return s;
	}

	function getYear() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10);
		return s;
	}

	var year = new Ext.form.TextField({
		style : 'cursor:pointer',
		name : 'year',
		fieldLabel : '年度',
		readOnly : true,
		width : 70,
		value : getYear(),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y',
					alwaysUseStartDate : false,
					dateFmt : 'yyyy',
					isShowClear : false,
					onpicked : function(v) {
						store.reload();
						this.blur();
					}
				});
			}
		}
	});

	var advicenoteNoFuzzy = new Ext.form.TextField({
		id : 'advicenoteNoFuzzy',
		name : '通知单号',
		anchor : "40%"
	});

	var deptHidden = new Ext.form.Hidden({
		id : 'newDeptid'
	});
	var dept = new Ext.form.ComboBox({
		id : 'dept',
		fieldLabel : '部门',
		value : "请选择...",
		mode : 'remote',
		editable : false,
		readOnly : true,
		anchor : "40%",
		onTriggerClick : function() {
			var args = {
				selectModel : 'single',
				rootNode : {
					id : "0",
					text : '灞桥热电厂'
				}
			}
			var url = "/power/comm/jsp/hr/dept/dept.jsp";
			var rvo = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:250px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (typeof(rvo) != "undefined") {
				deptHidden.setValue(rvo.ids);
				dept.setValue(rvo.names);
			}
		}
	});

	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'newEmpid',
		mapping : 0
	}, {
		name : 'empId',
		mapping : 1
	}, {
		name : 'empCode',
		mapping : 2
	}, {
		name : 'empName',
		mapping : 3
	}, {
		name : 'advicenoteNo',
		mapping : 4
	}, {
		name : 'newDeptid',
		mapping : 5
	}, {
		name : 'newDeptName',
		mapping : 6
	}, {
		name : 'checkStationGrade',
		mapping : 7
	}, {
		name : 'stationId',
		mapping : 8
	}, {
		name : 'stationName',
		mapping : 9
	}, {
		name : 'salaryPoint',
		mapping : 10
	}, {
		name : 'missionDate',
		mapping : 11
	}, {
		name : 'startsalaryDate',
		mapping : 12
	}, {
		name : 'memo',
		mapping : 13
	},{
		name : 'checkStationGradeName',
		mapping : 14
	}
	//add by sychen 20100717
	,{
		name : 'registerDate',
		mapping : 15
	}
	//add by sychen 20100721
	,{
		name : 'empType',
		mapping : 16
	}
	//add by sychen 20100721
	,{
		name : 'empTime',
		mapping : 17
	}
	//add by sychen 20100721
	,{
		name : 'printDate',
		mapping : 18
	}]);
	var dataProxy = new Ext.data.HttpProxy({
		url : 'hr/findNewEmployeeList.action'
	});

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);
	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
	// 分页
	store.load({
		params : {
			start : 0,
			limit : 18
		}
	});
	
	var printBtn = new Ext.Button({
        text : '打印',
        iconCls : Constants.CLS_PRINT,
        disabled : true,
        handler : printRecord
    });

	var sm = new Ext.grid.CheckboxSelectionModel({
			// singleSelect : true
	});
	var grid = new Ext.grid.GridPanel({
		region : "center",
		store : store,
		columns : [sm, new Ext.grid.RowNumberer({
			header : '行号',
			width : 35
		}), {
			header : '工号',
			dataIndex : 'empCode'
		}, {
			header : '姓名',
			dataIndex : 'empName'
		}, {
			header : '通知单号',
			width : 130,
			dataIndex : 'advicenoteNo',
			renderer:function(value){
				
				return "人新进("+year.getValue()+")第"+value+"号";
			}
		}, {
			header : '岗位',
			width : 100,
			dataIndex : 'stationName'
		}, {
			header : '岗级',
			width : 100,
			dataIndex : 'checkStationGradeName'
		}, {
			header : '薪点',
			dataIndex : 'salaryPoint'
		}, {
			header : '部门',
			dataIndex : 'newDeptName'
		}, {
			header : '到厂日期',
			dataIndex : 'missionDate'
		}, {
			header : '起薪日期',
			dataIndex : 'startsalaryDate'
		}, {
			header : '备注',
			dataIndex : 'memo'
		}
		// add by sychen 20100717
		, {
			header : '登记时间',
			dataIndex : 'registerDate'
		}
		// add by sychen 20100717
		, {
			header : '类别',
			dataIndex : 'empType'
		}
		// add by sychen 20100717
		, {
			header : '时间',
			dataIndex : 'empTime'
		}
		// add by sychen 20100717
		, {
			header : '打印日期',
			dataIndex : 'printDate'
		}],
		sm : sm,
		tbar : ["年度:", year, "部门:", dept, deptHidden, "通知单号:", advicenoteNoFuzzy, {
			text : "查询",
			iconCls : 'query',
			handler : queryRecord
		}, {
			text : "新增",
			iconCls : 'add',
			handler : addRecord
		}, {
			text : "修改",
			iconCls : 'update',
			handler : updateRecord
		}, {
			text : "删除",
			iconCls : 'delete',
			handler : deleteRecord
		},printBtn, {
			text : "导出",
			iconCls : Constants.CLS_EXPORT,
			handler : exportRecord
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

	grid.on("rowdblclick", updateRecord);

	grid.on("rowclick", function(){
    	printBtn.setDisabled(false);
    });
	// ---------------------------------------
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});
	// -------------------
	// 定义FORM
	var newEmpid = new Ext.form.Hidden({
		id : "newEmpid",
		fieldLabel : 'ID',
		name : 'newEmp.newEmpid',
		anchor : "85%"
	});

	var newEmpCodeTxt = new Ext.form.TextField({
		id : 'newEmpCodeTxt',// 新工号
		disabled : true,
		fieldLabel : '员工工号',
		anchor : "85%"
	});

	var empIdHidden = new Ext.form.Hidden({
		id : 'empId',
		name : 'newEmp.empId'
	});

	// 员工姓名数据
	var empNameData = Ext.data.Record.create([{
		// 员工姓名
		name : 'chsName'
	}, {
		// 员工编码
		name : 'empCode'
	}, {
		// 员工ID
		name : 'empId'
	}, {
		// 上次修改时间
		name : 'lastModifiyDate'
	}, {
		name : 'newEmpCode'
	}]);

	// 员工姓名Store
	var empNameStore = new Ext.data.JsonStore({
		url : 'hr/getEmpInfoNewEmployee.action',
		root : 'list',
		fields : empNameData
	});
	empNameStore.load({
		callback : function() {
			empNameStore.insert(0, new empNameData({
				chsName : '',
				empCode : '',
				empId : '',
				lastModifiyDate : ''
			}));
			empNameCbo.collapse();
		}
	});
	// 员工姓名下拉框
	var empNameCbo = new Ext.form.ComboBox({
		id : 'chsName',
		fieldLabel : '员工姓名<font color="red">*</font>',
		allowBlank : false,
		triggerAction : 'all',
		store : empNameStore,
		anchor : "85%",
		displayField : 'chsName',
		valueField : 'empCode',
		mode : 'local',
		readOnly : true,
		listeners : {
			select : function(cmb, record, index) {
				//员工工号（新)
				newEmpCodeTxt.setValue(record.get("newEmpCode"));
				// 设置人员ID
				empIdHidden.setValue(record.get("empId"));
			}
		}
	});

	var deptTxt = new Ext.form.TextField({
		id : 'deptName',
		fieldLabel : '所属部门<font color="red">*</font>',
		anchor : "85%",
		allowBlank : false,
		readOnly : true
	});

	// 所属部门ID
	var hiddenDeptTxt = {
		id : 'deptId',
		name : 'newEmp.newDeptid',
		xtype : 'hidden',
		value : '',
		readOnly : true,
		hidden : true
	};
	deptTxt.onClick(deptSelect);

	var stationData = Ext.data.Record.create([{
		name : 'stationName'
	}, {
		name : 'stationId'
	}]);
	var stationStore = new Ext.data.JsonStore({
		url : 'hr/getStationByDeptNewEmployee.action',
		root : 'list',
		fields : stationData
	});
	var stationCbo = new Ext.form.ComboBox({
		id : 'stationId',
		hiddenName : 'newEmp.stationId',
		fieldLabel : '工作岗位',
		anchor : "85%",
		triggerAction : 'all',
		store : stationStore,
		displayField : 'stationName',
		valueField : 'stationId',
		mode : 'local',
		readOnly : true
	});

	// 进厂日期
	var missionDateTxt = new Ext.form.TextField({
		id : 'missionDate',
		name : 'newEmp.missionDate',
		anchor : "85%",
		fieldLabel : '进厂日期<font color="red">*</font>',
		style : 'cursor:pointer',
		readOnly : true,
		allowBlank : false
	});
	missionDateTxt.onClick(function() {
		WdatePicker({
			startDate : '%y-%M-%d',
			dateFmt : 'yyyy-MM-dd',
			isShowClear : false,
			onpicked : function() {
				missionDateTxt.clearInvalid();
			},
			onclearing : function() {
				missionDateTxt.markInvalid();
				missionDateTxt.setValue("");
			}
		});
	});

	var advicenoteNoTxt = new Ext.form.NumberField({
		id : 'advicenoteNo',
		name : 'newEmp.advicenoteNo',
		fieldLabel : '通知单号<font color="red">*</font>',
		allowBlank : false,
		anchor : "85%"
	});

	var stationLestore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'empInfoManage.action?method=getStationlevel'
		}),
		reader : new Ext.data.JsonReader({}, [{
			name : 'id'
		}, {
			name : 'text'
		}])
	});
	stationLestore.load();
	var tfCheckStationGrade = {
		fieldLabel : '执行岗级',
		name : 'checkStationGrade',
		xtype : 'combo',
		id : 'checkStationGrade',
		store : stationLestore,
		valueField : "id",
		displayField : "text",
		mode : 'remote',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'newEmp.checkStationGrade',
		editable : false,
		triggerAction : 'all',
		blankText : '请选择',
		emptyText : '请选择',
		selectOnFocus : true,
		anchor : "85%"
	};

	var salaryPoint = new Ext.form.NumberField({
		id : 'salaryPoint',
		name : 'newEmp.salaryPoint',
		fieldLabel : "薪点",
		anchor : "85%"
	})

	var startsalaryDate = new Ext.form.TextField({
		id : 'startsalaryDate',
		name : 'newEmp.startsalaryDate',
		anchor : "85%",
		fieldLabel : '起薪日期',
		style : 'cursor:pointer',
		readOnly : true
	});
	startsalaryDate.onClick(function() {
		WdatePicker({
			startDate : '%y-%M-%d',
			dateFmt : 'yyyy-MM-dd',
			isShowClear : false,
			onpicked : function() {
				startsalaryDate.clearInvalid();
			},
			onclearing : function() {
				startsalaryDate.markInvalid();
				startsalaryDate.setValue("");
			}
		});
	});

		//add by sychen 20100721
		var empType = new Ext.form.ComboBox({
			    id : 'empType',
				name : 'empType',
				hiddenName : 'newEmp.empType',
				store : new Ext.data.SimpleStore({
							fields : ['name', 'value'],
							data : [['试用期', '1'], ['见习期', '2'], ['实习期', '3']]
						}),
				fieldLabel : '类别',
				triggerAction : 'all',
				readOnly : false,
				valueField : 'value',
				displayField : 'name',
				mode : 'local',
				anchor : "85%"
			})
			
			var empTime = new Ext.form.ComboBox({
			    id : 'empTime',
				name : 'empTime',
				hiddenName : 'newEmp.empTime',
				store : new Ext.data.SimpleStore({
							fields : ['name', 'value'],
							data : [['3个月', '1'], ['6个月', '2'], ['12个月', '3']]
						}),
				fieldLabel : '时间',
				triggerAction : 'all',
				readOnly : false,
				valueField : 'value',
				displayField : 'name',
				mode : 'local',
				anchor : "85%"
			})
	
	var printDate = new Ext.form.TextField({
		id : 'printDate',
		name : 'newEmp.printDate',
		anchor : "85%",
		fieldLabel : '打印日期',
		style : 'cursor:pointer',
		readOnly : true,
		value: getDate() 
	});
	printDate.onClick(function() {
		WdatePicker({
			startDate : '%y-%M-%d',
			dateFmt : 'yyyy-MM-dd',
			isShowClear : false,
			onpicked : function() {
				printDate.clearInvalid();
			},
			onclearing : function() {
				printDate.markInvalid();
				printDate.setValue("");
			}
		});
	});
	//add by sychen 20100721 end
	
	var memo = new Ext.form.TextArea({
		id : "memo",
		height : 60,
		fieldLabel : '备注',
		name : 'newEmp.memo',
		anchor : "92.3%"
	});


	var myaddpanel = new Ext.FormPanel({
		title : '新进员工增加/修改',
		height : '100%',
		layout : 'form',
		frame : true,
		labelAlign : 'center',
		items : [{
			border : false,
			layout : 'column',
			items : [{
				columnWidth : .5,
				layout : 'form',
				border : false,
				labelWidth : 70,
				items : [newEmpid, newEmpCodeTxt, empIdHidden, empNameCbo,
						hiddenDeptTxt, deptTxt, stationCbo, empType,empTime]
			}, {
				columnWidth : .5,
				layout : 'form',
				labelWidth : 70,
				border : false,
				items : [advicenoteNoTxt, tfCheckStationGrade, salaryPoint,
						startsalaryDate,missionDateTxt,printDate]
			}]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 1,
			labelWidth : 70,
			items : [memo]
		}]
	});
	var win = new Ext.Window({
		width : 550,
		height : 350,
		buttonAlign : "center",
		items : [myaddpanel],
		layout : 'fit',
		closeAction : 'hide',
		draggable : true,
		modal : true,
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
				var isAddFlag = !newEmpid.getValue();
				myaddpanel.getForm().submit({
					method : 'POST',
					url : "hr/editNewEmployeeRecord.action",
					params : {
						'isAdd' : isAddFlag,
						'empId' : empIdHidden.getValue(),
						'empType':Ext.get('empType').dom.value,
						'empTime':Ext.get('empTime').dom.value
					},
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("提示", o.msg);
						store.load({
							params : {
								start : 0,
								limit : 18
							}
						});
						win.hide();
					},
					faliue : function() {
						Ext.Msg.alert('错误', '出现未知错误.');
					}
				});
			}
		}, {
			text : '取消',
			iconCls : 'cancer',
			handler : function() {
				win.hide();
			}
		}]
	});

	// 查询
	function queryRecord() {
		store.baseParams = {
			year : year.getValue(),
			advicenoteNo : advicenoteNoFuzzy.getValue(),
			dept : deptHidden.getValue()
		}
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}

	function addRecord() {
		myaddpanel.getForm().reset();
		win.setPosition(100, 50);
		Ext.Ajax.request({
			url : 'hr/getAdvicenoteNo.action',
			method : 'post',
			success : function(result, request) {
				var json = result.responseText;
				// 将json字符串转换成对象
				var o = eval("(" + json + ")");
				advicenoteNoTxt.setValue(o.message);
			}
		});
		win.show();
		myaddpanel.setTitle("增加新进员工登记");
	}
	
	function updateRecord() {
		if (grid.selModel.hasSelection()) {
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("提示", "请选择其中一项进行编辑！");
			} else {
				var record = grid.getSelectionModel().getSelected();
				win.show();
				myaddpanel.getForm().loadRecord(record);
				Ext.get('newEmpCodeTxt').dom.value = record.get('empCode');
				Ext.get('chsName').dom.value = record.get('empName');
				Ext.get('deptName').dom.value = record.get('newDeptName');
				Ext.get('deptId').dom.value = record.get('newDeptid');
				Ext.get('stationId').dom.value = record.get('stationId');
				Ext.get('stationId').dom.value = record.get('stationName');
				myaddpanel.setTitle("修改新进员工登记");
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}

	function deleteRecord() {
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.newEmpid) {
					ids.push(member.newEmpid);
				} else {
					store.remove(store.getAt(i));
				}
			}
			Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(buttonobj) {
				if (buttonobj == "yes") {
					Ext.lib.Ajax.request('POST',
							'hr/deleteNewEmployeeRecord.action', {
								success : function(action) {
									Ext.Msg.alert("提示", "删除成功！")
									store.load({
										params : {
											start : 0,
											limit : 18
										}
									});
								},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
							}, 'ids=' + ids);
				}
			});
		}

	}

	var empids = new Array();	
	function printRecord() {
		if (grid.selModel.hasSelection()) {
				if(grid.selModel.getSelections().length > 1){
				//update by sychen 20100724
					var selections = grid.getSelectionModel().getSelections();
						for (var i = 1; i < selections.length; i += 1) {
				         var member = selections[i];
				         if(selections[0].get('advicenoteNo')!=member.get('advicenoteNo')){
					       Ext.Msg.alert('提示','通知单号不一致，请修改一致后再打印!');
					       return;
				         }
			      }
//					Ext.Msg.alert('提示','请选择其中一条数据!');
//					return;
				}
				var selections = grid.getSelectionModel().getSelections();
				for (var i = 0; i < selections.length; i += 1) {
			    var member = selections[i];
				  if (member.get("newEmpid") != null) {
					 empids.push(member.get("newEmpid"));
				  }
				}
				var record = grid.getSelectionModel().getSelected();
				Ext.Msg.confirm(Constants.CONFIRM, '确认要打印吗？', function(
						buttonobj) {
					// 如果选择是
					if (buttonobj == "yes") {
						var empId = record.get('empId');
						var advicenoteNo = record.get('advicenoteNo');
						var printDate = record.get('printDate');
						var newEmpids = ","+empids.join(",")+",";//add by sychen 20100724
						var url="/power/hr/newWorkerOrder.action?advicenoteNo="+record.get('advicenoteNo')+"" +
									"&printDate="+record.get('printDate')+"&newEmpids="+newEmpids+"";
							window.open(url);
						
						
	
//							window.open("/powerrpt/report/webfile/hr/newEmployeeReport.jsp?empId="+empId+"&advicenoteNo="+advicenoteNo+"&printDate="+printDate+"&newEmpids="+newEmpids);
					}
				});
			} else {
				// 如果没有选择数据，弹出错误提示框
				Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
			}
	}
	
	function exportRecord() {
		 Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_007, function(buttonobj) {
            if (grid.selModel.hasSelection()) {
            	var records = grid.selModel.getSelections();
	            var recordslen = records.length;
	            if (recordslen >= 1) {
					var date = new Date();
	            	var strDate = date.format('y')+"年"+date.format('m')+"月"+date.format('d')+"日";
	            	var recs = grid.getSelectionModel().getSelections();
					var html = ['<table border=1><tr><th colspan=15>大唐陕西发电有限公司灞桥热电厂'+date.getYear()+"年"+'新进员工通知单</th>'];
				if(recordslen==1)
					{
						//add by fyyang 20100702
					html.push('<tr></tr>');
					html.push('<tr><th colspan=3 align="center">查照</th><th colspan=7 align="center">'+strDate+'</th><th colspan=5 align="center">人新进（'+ date.getYear()+'）第'+recs[0].get('advicenoteNo')+'号</th></tr>');
					}
					html.push('<tr><th rowspan=2>姓名</th><th rowspan=2 colspan=2>职务（岗位）</th><th rowspan=2>岗级</th><th rowspan=2>薪点</th><th rowspan=2 colspan=2>工作部门</th><th colspan=3>到厂日期</th><th colspan=3>起薪日期</th><th rowspan=2 colspan=2>备注</th></tr>');
					html.push('<tr><th>年</th><th>月</th><th>日</th><th>年</th><th>月</th><th>日</th></tr>');
					for(var i = 0; i<recs.length; i++){
						var re = recs[i];
						var dateStr = re.get('missionDate')
						var stpslryStr = re.get('startsalaryDate')
						html.push('<tr><td>'+re.get('empName')+'</td><td colspan=2>'+re.get('stationName')+'</td><td>'+re.get('checkStationGradeName')+'</td><td>'+re.get('salaryPoint')+'</td>' +
								'<td colspan=2>'+re.get('newDeptName')+'</td><td>'+dateStr.substring(0,4)+'</td>' +
								'<td>'+dateStr.substring(5,7)+'</td><td>'+dateStr.substring(8)+'</td>' +
										'<td>'+stpslryStr.substring(0,4)+'</td><td>'+stpslryStr.substring(5,7)+'</td>' +
												'<td>'+stpslryStr.substring(8)+'</td><td colspan=2>'+re.get('memo')+'</td></tr>');
					}						
					for(var j = 0; j<3; j++){
						html.push('<tr><td></td><td colspan=2></td><td></td><td ></td><td colspan=2></td><td></td><td></td><td></td><td></td><td></td>' +
											'<td></td><td colspan=2></td></tr>')
					}	
					html.push('<tr border=0><th colspan=3 align="left">厂长:</th><th colspan=5 align="left">人力资源部主任:</th><th colspan=5 align="left">制单:</th></tr>');
					html.push('</table>');
					html = html.join(''); // 最后生成的HTML表格
					tableToExcel(html);
	            } 
				}else{
					Ext.Msg.alert('提示','请选择要导出的数据！')
				}
        });
	}
	  /**
     * 将HTML转化为Excel文档
     */
    function tableToExcel(tableHTML){
		window.clipboardData.setData("Text",tableHTML);
		try{
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExApp.visible = true;
		}catch(e){
			if(e.number != -2146827859){
				Ext.Msg.alert('提示','您的电脑没有安装Microsoft Excel软件!')
			}
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}
	function deptSelect() {
		var args = {
			selectModel : 'single',
			rootNode : {
				id : '0',
				text : '灞桥电厂'
			}
		};
		var object = window.showModalDialog('/power/comm/jsp/hr/dept/dept.jsp',
				args, 'dialogWidth=' + Constants.WIDTH_COM_DEPT
						+ 'px;dialogHeight=' + Constants.HEIGHT_COM_DEPT
						+ 'px;center=yes;help=no;resizable=no;status=no;');
		if (object) {
			stationCbo.clearValue();
			deptTxt.setValue(object.names);
			Ext.get("deptId").dom.value = object.ids;
			stationStore.load({
				params : {
					deptIdForm : object.ids
				},
				callback : function() {
					stationStore.insert(0, new stationData({
						stationName : '',
						stationId : ''
					}));
				}
			})
		}
	}

});
