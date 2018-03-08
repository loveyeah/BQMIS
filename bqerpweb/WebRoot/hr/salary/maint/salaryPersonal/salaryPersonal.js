Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
  function getcurrentYear() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) 
		return s;
	}
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'salaryPersonalId',
		mapping : 0
	}, {
		name : 'empId',
		mapping : 1
	}, {
		name : 'deptId',
		mapping : 2
	}, {
		name : 'workingFrom',
		mapping : 3
	}, {
		name : 'joinFrom',
		mapping : 4
	}, {
		name : 'runningAge',
		mapping : 5
	}, {
		name : 'remainSalary',
		mapping : 6
	}, {
		name : 'pointSalaryAdjust',
		mapping : 7
	}, {
		name : 'memo',
		mapping : 8
	}, {
		name : 'newEmpCode',
		mapping : 9
	}, {
		name : 'empName',
		mapping : 10
	}, {
		name : 'deptCode',
		mapping : 11
	}, {
		name : 'deptName',
		mapping : 12
	}, {
		name : 'monthAward',
		mapping : 13
	},{
		name : 'stationName',
		mapping : 14
	}, {
		name : 'proportion',
		mapping : 15
	},{
		name : 'salaryPoint',
		mapping : 16
	},{
		name : 'stationId',
		mapping : 17
	},{
		name : 'lastJoinRuntime',
		mapping : 18
	},{
		name : 'runAgeFlag',
		mapping : 19
	}]);
	var dataProxy = new Ext.data.HttpProxy({
				url : 'com/findSalaryPersonalList.action'
			});

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);
	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
	//分页
	store.load({
		params : {
			start : 0,
			limit : 18
		}
	});
	
	var fuzzy = new Ext.form.TextField({
		id : 'fuzzy',
       anchor : "75%"
	});
	var tfAppend = new Ext.form.TextField({
		name : 'xlsFile',
		inputType : 'file',
		width : 200
	})
	var btnInport = new Ext.Toolbar.Button({
		id : 'saraly_inport',
		text : '导入',
		handler : uploadQuestFile,
		iconCls : 'upLoad'
	});
	   //add by ypan 20100728
		var queydeptId = new Ext.ux.ComboBoxTree({
			fieldLabel : '所属部门',
			id : 'deptId',
			displayField : 'text',
			valueField : 'id',
			hiddenName : 'empinfo.deptId',
			blankText : '请选择',
			emptyText : '请选择', 
			resizable:true,
			width : 250,
			// value:{id:'0',text:'合肥电厂',attributes:{description:'deptName'}},
			tree : {
				xtype : 'treepanel',
				autoScroll : false,
				loader : new Ext.tree.TreeLoader({
					dataUrl : 'empInfoManage.action?method=getDep&flag=roleQuery'
				}),
				root : new Ext.tree.AsyncTreeNode({
					id : '0',
					//name : '灞桥热电厂',
					text : '灞桥热电厂'
				})
			},
			selectNodeModel : 'all'
		})
		//modify by ypan 20100728
	var headerTbar = new Ext.Toolbar({
				items : [fuzzy,'部门:',queydeptId,{
			text : "查询",
			iconCls : 'query',
			handler : queryRecord
		}, {
			text : "员工更新",
			iconCls : 'add',
			handler : empChangeRecord
		}, {
			text : "修改",
			iconCls : 'update',
			handler : updateRecord
		}, {
			text : "运龄调整",
			iconCls : 'add',
			handler : runAgeChangeRecord
		},tfAppend,btnInport,{
			text : "导出",
			iconCls : Constants.CLS_EXPORT,
			handler : exportRecord
		}]
			});
			
	function exportRecord() {
		Ext.Ajax.request({
			url : 'com/findSalaryPersonalList.action?fuzzy='+ fuzzy.getValue(),
			success : function(response) {
				var json = eval('(' + response.responseText.trim() + ')');
				var records = json.list;
				var html = ['<table>'];
				  html.push("<tr><th>员工工号</th><th>员工姓名</th><th>所属部门</th><th>参加工作时间</th><th>入厂时间</th><th>初始运龄</th>" );
				  html.push("<th>保留工资</th><th>薪点工资调整</th><th>月奖系数</th><th>岗位</th><th>岗位量化比例</th><th>薪点</th><th>最近加入运行时间</th>");
				  html.push("<th>运龄是否使用</th><th>备注</th>");
				  html.push("</tr>");
				for (var i = 0; i < records.length; i += 1) {
					var rc = records[i];
					var name19=rc[19]=='1'?'是':'否';
		         html.push("<tr><td>"+rc[9]+"&nbsp</td><td>"+rc[10]+"</td><td>"+rc[12]+"</td><td>"+(rc[3]==null?' ':rc[3])+"</td><td>"+(rc[4]==null?' ':rc[4])+"</td><td>"+(rc[5]==null?' ':rc[5])+"</td>");
				 html.push("<td>"+(rc[6]==null?' ':rc[6])+"</td><td>"+(rc[7]==null?' ':rc[7])+"</td><td>"+(rc[13]==null?' ':rc[13])+"</td><td>"+(rc[14]==null?' ':rc[14])+"</td><td>"+(rc[15]==null?' ':rc[15])+"</td><td>"+(rc[16]==null?' ':rc[16])+"</td><td>"+(rc[18]==null?' ':rc[18])+"</td>");
				 html.push("<td>"+name19+"</td><td>"+(rc[8]==null?' ':rc[8])+"</td>");
				 html.push("</tr>");
				}
			   html.push('</table>');
				html = html.join(''); // 最后生成的HTML表格
				
				tableToExcel(html);
			},
			failure : function(response) {
				Ext.Msg.alert('信息', '失败');
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
			ExWSh.Columns("A").ColumnWidth  = 15;
			ExWSh.Columns("B").ColumnWidth  = 15;
			ExWSh.Columns("C").ColumnWidth  = 20;
			ExWSh.Columns("D:E").ColumnWidth  = 10;
			ExWSh.Columns("J").ColumnWidth  = 12;
			ExWSh.Columns("M").ColumnWidth  = 12;
			ExWSh.Columns("O").ColumnWidth  = 20;
			
//			ExWSh.Cells.NumberFormatLocal = "@";
			ExApp.visible = true;
		}catch(e){
			if(e.number != -2146827859){
				Ext.Msg.alert('提示','您的电脑没有安装Microsoft Excel软件!')
			}
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}			
			
			
	
	var headForm = new Ext.form.FormPanel({
//		region : 'north',
		height : 28,
		frame : false,
		fileUpload : true,
		layout : 'border',
		items : [{
							bodyStyle : "padding: 1,1,1,0",
							region : "center",
							border : true,
							frame : false,
							layout : 'form',
							items : [headerTbar]
						}]
		
	});
	var sm = new Ext.grid.CheckboxSelectionModel({
	//	singleSelect : true
	});
	var grid = new Ext.grid.GridPanel({
		region : "center",
		
		store : store,
		columns : [sm, new Ext.grid.RowNumberer({
			header : '序号',
			width : 35,
			align : 'left'
		}),{
			header : "ID",
			sortable : true,
			hidden : true,
			align : 'left',
			dataIndex : 'salaryPersonalId'
		}, {
			header : "员工编号",
			width : 100,
			sortable : true,
			align : 'left',
			dataIndex : 'newEmpCode'
		}, {
			header : "员工名称",
			width : 130,
			sortable : true,
			align : 'left',
			dataIndex : 'empName'
		}, {
			header : "所属部门",
			width : 120,
			sortable : true,
			align : 'left',
			dataIndex : 'deptName'
		}, {
			header : "参加工作时间",
			width : 120,
			sortable : true,
			align : 'left',
			dataIndex : 'workingFrom'
		}, {
			header : "入厂时间",
			width : 120,
			sortable : true,
			align : 'left',
			dataIndex : 'joinFrom'
		}, {
			header : " 初始运龄",
			width : 120,
			sortable : true,
			align : 'left',
			dataIndex : 'runningAge'
		}, {
			header : "保留工资(元)",
			width : 130,
			sortable : true,
			align : 'left',
			dataIndex : 'remainSalary'
		}, {
			header : "薪点工资调整",
			width : 130,
			sortable : true,
			align : 'left',
			dataIndex : 'pointSalaryAdjust'
		}, {
			header : "月奖系数",
			width : 130,
			sortable : true,
			align : 'left',
			dataIndex : 'monthAward'
		},{//add by wpzhu 
			header : "岗位",
			width : 130,
			sortable : true,
			align : 'left',
			dataIndex : 'stationName'
		},{ 
			header : "岗位量化比例",
			width : 130,
			sortable : true,
			align : 'left',
			dataIndex : 'proportion'
		}, { 
			header : "薪点",
			width : 130,
			sortable : true,
			align : 'left',
			dataIndex : 'salaryPoint'
		},{ 
			header : "最近加入运行时间",
			width : 130,
			sortable : true,
			align : 'left',
			dataIndex : 'lastJoinRuntime'
		},{ 
			header : "运龄是否使用",
			width : 130,
			sortable : true,
			align : 'left',
			dataIndex : 'runAgeFlag',
			renderer:function(v)
			{
				if(v=="1")
				{
				return "是";
				}
				else if(v=="2")
				{
				return "否";
				}
			}
		},{
			header : "备注",
			width : 130,
			sortable : true,
			align : 'left',
			dataIndex : 'memo'
		}],
		sm : sm,
		tbar :headForm /*[fuzzy, {
			text : "查询",
			iconCls : 'query',
			handler : queryRecord
		}, {
			text : "员工更新",
			iconCls : 'add',
			handler : empChangeRecord
		}, {
			text : "修改",
			iconCls : 'update',
			handler : updateRecord
		}, {
			text : "运龄调整",
			iconCls : 'add',
			handler : runAgeChangeRecord
		},tfAppend,btnInport]*/,
		//分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});
  
	grid.on("rowdblclick", updateRecord);
	
	// ---------------------------------------
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});
	// -------------------
	//定义FORM
	 var salaryPersonalId = new Ext.form.TextField({
		id : "salaryPersonalId",
		fieldLabel : 'ID',
		readOnly : true,
		hidden : true,
		name : 'personal.salaryPersonalId',
		anchor : "85%"
	});
	var empCode = new Ext.form.TextField({
		id : 'newEmpCode',
		fieldLabel : "员工工号",
		readOnly : true,
		name : 'personal.newEmpCode',
		anchor : "85%"
	});
	
	var empName = new Ext.form.TextField({
		id : 'empName',
		fieldLabel : "员工名称",
		readOnly : true,
		name : 'personal.empName',
		anchor : "85%"
	});
	
	var deptName = new Ext.form.TextField({
		id : 'deptName',
		fieldLabel : "部门",
		readOnly : true,
		name : 'deptName',
		anchor : "85%"
	});
	var deptId = new Ext.form.Hidden({
		id : 'personal.deptId'
	});

	var workingFrom = new Ext.form.DateField({
		id : 'workingFrom',
		fieldLabel : '参加工作时间',
		name : 'personal.workingFrom',
		format : 'Y-m-d',
		anchor : "85%",
		readOnly : true
	});
		
	var joinFrom = new Ext.form.DateField({
		id : 'joinFrom',
		fieldLabel : '入厂时间',
		name : 'personal.joinFrom',
		format : 'Y-m-d',
		anchor : "85%",
		readOnly : true
	});
	
	var runningAge = new Ext.form.NumberField({
		id : 'runningAge',
		fieldLabel : "初始运龄",
		allowBlank:false,
		name : 'personal.runningAge',
		anchor : "85%"
	});
	
	var remainSalary = new Ext.form.NumberField({
		id : 'remainSalary',
		fieldLabel : '保留工资',
		name : 'personal.remainSalary',
		anchor : "85%"
	});
	
	var pointSalaryAdjust = new Ext.form.NumberField({
		id : 'pointSalaryAdjust',
		fieldLabel : '薪点工资调整',
		name : 'personal.pointSalaryAdjust',
		anchor : "85%"
	});
	
	var monthAward = new Ext.form.NumberField({
		id : 'monthAward',
		fieldLabel : '月奖系数',
		name : 'personal.monthAward',
		anchor : "85%"
	});
	//  薪点  add by wpzhu 20100707
	var runageFlag = new Ext.form.ComboBox({
				readOnly : true,
				id:'runageFlag',
				name : 'runageFlag',
				hiddenName : 'personal.runAgeFlag',
				mode : 'local',
				width : 100,
				value : "1",
				fieldLabel : '运龄是否使用',
				triggerAction : 'all',
				listeners : {
					"select" : function() {
					
				
					}
				},
				store : new Ext.data.SimpleStore({
							fields : ['name', 'value'],
							data : [['是', '1'], ['否', '2']]
						}),
				valueField : 'value',
				displayField : 'name',
				anchor : "85%"
				
			})
	/*function getRuntime() {
		var c=0;
		if (runageFlag.getValue() == "1") {
			var year = getcurrentYear();
			var runage = runningAge.getValue();
			var lastRuntime = lastJoinRuntime.getValue();
			var runYear = lastRuntime.substr(0, 4);
			if(runage!=0&&runage!=null&&runage!="")
			{
				c=parseInt(runage )
			}
			else{
				c=0;
			}
			var a = c+ (parseInt(year) - parseInt(runYear)) + 1;
			runningAge.setValue(a);
		} else if (runageFlag.getValue() == "2") {

			var year = getcurrentYear();
			var lastRuntime = lastJoinRuntime.getValue();
			var runYear = lastRuntime.substr(0, 4);
			var b = parseInt(year )-parseInt( runYear) + 1;

			runningAge.setValue(b);

		}
	}*/
	var lastJoinRuntime = new Ext.form.TextField({
				style : 'cursor:pointer',
				id:'lastJoinRuntime',
				fieldLabel : '最近加入运行时间',
				readOnly : true,
				//modify by ypan 20100728
				allowBlank:true,
				anchor : "80%",
				value : getcurrentYear(),
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									alwaysUseStartDate : false,
									dateFmt : 'yyyy-MM-dd',
									isShowClear : false,
									onpicked : function(v) {
										
										this.blur();
									}
								});
					}
				}
			});

  var salaryPoint = new Ext.form.NumberField({
		id : 'salaryPoint',
		fieldLabel : '薪点',
		name : 'personal.salaryPoint',
		anchor : "85%"
	});
	var nameStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'hr/getStationQuantity.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, [{
				name : 'stationId',
				mapping:0
			}, {
				name : 'stationName',
				mapping:1
			},{
				name : 'proportion',
				mapping:2
			}])
	});

//    var stationId = new Ext.form.Hidden({
//		id : 'personal.quentityId'
//	});

  function getPorprotion() {
  	
		Ext.Ajax.request({
			url : 'hr/getPorprotion.action',
			method : 'post',
			params : {
				stationId : stationName.getValue()
			},
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result.quantifyProportion != null) {
					porprotion.setValue(result.quantifyProportion);

				}
			},
			failure : function(result, request) {
				Ext.MessageBox.alert('提示信息', '操作失败！')
			}
		})
	}
	var stationName = new Ext.form.ComboBox({
				fieldLabel : '岗位',
				store : nameStore,
				id : 'quentityId',
				name : 'stationName',
				valueField : "stationId",
				displayField : "stationName",
				mode : 'remote',
				forceSelection : true,
				hiddenName : 'personal.quentityId',
				editable : false,
				triggerAction : 'all',
				listeners : {
					"select" : function() {
						getPorprotion();

					}
				},

				selectOnFocus : true,
				emptyText : '请选择',
				anchor : '85%'
			});
     nameStore.load();
	var porprotion = new Ext.form.TextField({
		id : 'proportion',
		fieldLabel : "岗位量化比例",
		readOnly : true,
		name : 'porprotion',
		anchor : "85%"
	});
	var memo = new Ext.form.TextArea({
		id : "memo",
		height : 80,
		fieldLabel : '备注',
		name : 'personal.memo',
		anchor : "92.3%"
	});
	
	var myaddpanel = new Ext.FormPanel({
		title : '修改',
		height : '100%',
		layout : 'form',
		frame : true,
		labelAlign : 'center',
		labelWidth : 80,
		items : [{
			border : false,
			hidden : true,
			layout : 'form',
			columnWidth : 1,
			labelWidth : 70,
			items : [salaryPersonalId]
		}, {
			border : false,
			layout : 'column',
			items : [{
				columnWidth : .5,
				layout : 'form',
				border : false,
				labelWidth : 80,
				items : [empCode, deptName,deptId,workingFrom , remainSalary,monthAward,stationName,runageFlag]
			}, {
				columnWidth : .5,
				layout : 'form',
				labelWidth : 80,
				border : false,
				items : [empName, joinFrom, runningAge, pointSalaryAdjust,salaryPoint,porprotion,lastJoinRuntime]
			}]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 1,
			labelWidth : 80,
			items : [memo]
		}]
	});
		
	var win = new Ext.Window({
		width : 550,
		height : 380,
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
//				getRuntime();
				var myurl = "";
				if(Ext.get("personal.quentityId").dom.value==null||Ext.get("personal.quentityId").dom.value=="null")
				{
				Ext.get("personal.quentityId").dom.value="";
				}
				   //add by ypan 20100728
					if(Ext.get("runageFlag").dom.value=="是")
					{
					if( !Ext.get('lastJoinRuntime').dom.value.trim()){
						Ext.Msg.alert('提示','最近加入运行时间不能为空！');
						return;
					}
				}
				myurl = "com/updateSalaryPersonalRecord.action";
				myaddpanel.getForm().submit({
					method : 'POST',
					params : {
								lastJoinRuntime:	lastJoinRuntime.getValue()	
									},
					url : myurl,
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
	//add by wpzhu --------------
 
	
	function uploadQuestFile() {
		var filePath = tfAppend.getValue();
		// 文件路径为空的情况
		if (filePath == "") {
			Ext.Msg.alert("提示", "请选择文件！");
			return;
		} else {
			// 取得后缀名并小写
			var suffix = filePath.substring(filePath.length - 3,
					filePath.length);
			if (suffix.toLowerCase() != 'xls')
				Ext.Msg.alert("提示", "导入的文件格式必须是Excel格式");
			else {
				Ext.Msg.wait("正在导入,请等待....");
				headForm.getForm().submit({
					method : 'POST',
					url : 'hr/importSaralyPersonalInf.action',
					params : {
						type : 'SaralyPersoalInf'
						
					},
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
					  queryRecord();
						Ext.Msg.alert("提示", o.msg);
						
					},
					failture : function() {
						Ext.Msg.alert(Constants.ERROR, "导入失败！");
					}
				})
			}
		}
	}
	// 查询
	function queryRecord() {
		
		store.baseParams = {
			fuzzy : fuzzy.getValue(),
			//add by ypan 20100728
			deptId:queydeptId.getValue()
		}
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}

	function empChangeRecord() {
			Ext.Msg.confirm("提示", "是否确认更新?", function(buttonobj) {
				if (buttonobj == "yes") {
					Ext.Msg.wait("操作进行中...", "请稍候");
					Ext.Ajax.request({
						method : 'post',
						url : 'com/empChangeRecord.action',
						timeout : 180000,
						success : function(action) {
							Ext.Msg.alert("提示", "操作成功！");
							queryRecord();
						},
						failure : function() {
							Ext.MessageBox.alert('提示', '未知错误！')
						}
					});
				}
			})
	}
	
	function updateRecord() {
        if (grid.selModel.hasSelection()) {
            var records = grid.selModel.getSelections();
            var recordslen = records.length;
            if (recordslen > 1) {
                Ext.Msg.alert("提示", "请选择其中一项进行编辑！");
            } else {
                var record = grid.getSelectionModel().getSelected();
				//queryRecord();
				win.show();
                myaddpanel.getForm().loadRecord(record);
                stationName.setRawValue(record.get("stationName"));//add by wpzhu 
                stationName.setValue(record.get("stationId"));
         
                
                myaddpanel.setTitle("修改薪酬个人维护");
					Ext.Ajax.request({
					method : 'post',
					url : 'com/getRunStationList.action',
					params : {
						empCode : empCode.getValue()
					},
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result != null) {
							if(result.model != null)
							{
							if (result.model.stationTypeName != '运行岗位') {
								runningAge.setDisabled(false);
							}else if(result.model.stationTypeName == '运行岗位'){
								runningAge.setDisabled(false);
							}
						}else{
								runningAge.setDisabled(false);
						}
						}
					}
				});
                }
        } else {
           Ext.Msg.alert("提示", "请先选择要编辑的行!");
        }
    }

	function runAgeChangeRecord() {
		var records = grid.selModel.getSelections();
		var ids = [];
		if (records.length == 0) {
			Ext.Msg.alert("提示", "请选择记录！");
		} else {

			for (var i = 0; i < records.length; i += 1) {
				var member = records[i];
				if (member.get("salaryPersonalId")) {
					ids.push(member.get("salaryPersonalId"));
				} else {
					store.remove(store.getAt(i));
				}
			}
			Ext.Msg.confirm("提示", "是否确定将所选员工的运龄+1年？", function(buttonobj) {
				if (buttonobj == "yes") {
					Ext.lib.Ajax.request('POST',
							'com/runAgeChangeRecord.action', {
								success : function(action) {
									Ext.Msg.alert("提示", "操作成功！")
									queryRecord();
								},
								failure : function() {
									Ext.Msg.alert('错误', '操作时出现未知错误.');
								}
							}, 'ids=' + ids);
				}
			});
		}
	}
});