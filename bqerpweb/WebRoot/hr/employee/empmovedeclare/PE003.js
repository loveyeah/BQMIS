Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.form.Label.prototype.setText = function(argText) {
	this.el.dom.innerHTML = argText;
}
Ext.onReady(function() {
	Ext.QuickTips.init();
	var FLAG_SUB_ADD = '0';
	var FLAG_SUB_MODIFY = '1';
	var flag;
	var flagSub;
	var oldBanzu=null;
	var newBanzu=null;
	
		function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t;
		return s;
	}
	
	
	
	// 薪酬变动登记store
	var storeSalaryChange = new Ext.data.JsonStore({
				root : 'list',
				url : "hr/getSalaryAdjustInfo.action",
				fields : ['empId', 'empCode', 'bfdeptId', 'adjustType',
						'bfDeptName', 'bfStationId', 'bfStationName',
						'bfStationLevel', 'bfStationLevelName',
						'stationChangeType', 'oldCheckStationGrade',
						'newCheckStationGrade', 'oldStationGrade',
						'newStationGrade', 'oldSalaryGrade', 'newSalaryGrade',
						'doDate', 'reason', 'memo', 'lastModifiedDate']
			})
	storeSalaryChange.on('load', function() {
				if (storeSalaryChange.getCount() > 0) {
					textSrBfWage.setValue(storeSalaryChange.getAt(0).get('oldSalaryGrade'));
		            textSrAfWage.setValue(storeSalaryChange.getAt(0).get('newSalaryGrade'));
		            CmbAStationLevel.setValue(storeSalaryChange.getAt(0)
				.get('newCheckStationGrade'));
					
				} else {
					
					textSrBfWage.setValue(hiddenSalaryLevel.getValue());
				}

			})


	// 系统当天日期
	var dd = new Date();
	var qx = new Date();
	dd = dd.format('Y-m-d');
	qx = qx.format('Y-m-d');

	// 定义查询起始时间
	var startDate = new Ext.form.TextField({
				width : 90,
				name : 'startDate',
				style : 'cursor:pointer',
				readOnly : true,
				value : new Date(new Date().getYear() - 1,0,1).format('Y-m-d'),
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : false,
									isShowClear : true
								});
					}
				}
			});
	// 定义查询结束时间
	var endDate = new Ext.form.TextField({
				width : 90,
				id : 'endDate',
				name : 'endDate',
				style : 'cursor:pointer',
				readOnly : true,
				value : new Date().format('Y-m-d'),
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : false,
									isShowClear : true
								});
					}
				}
			});
			
	// 定义查询登记时间
	var insertDate = new Ext.form.TextField({
				width : 90,
				id : 'insertDate',
				name : 'insertDate',
				style : 'cursor:pointer',
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : false,
									isShowClear : true
								});
					}
				}
			});

	// 单据状态
	var cmbStatus = new Ext.form.CmbHRCode({
				id : "cmbStatus",
				type : "单据状态",
				width : 70,
				// value : '0',
				value : '',
				hidden : true,
				name : 'dcmStatus'
			});

	// 调动前部门
	var txtBeforeDept = new Ext.form.TextField({
		fieldLabel : '部门选择',
		width : 110,
		valueField : 'id',
		hiddenName : 'mrDept',
		maxLength : 100,
		anchor : '100%',
		readOnly : true,
		listeners : {
			focus : function() {
				var args = {
					selectModel : 'single',
					rootNode : {
						id : '0',
						text : '灞桥电厂'
					},
					onlyLeaf : false
				};
				this.blur();
				var dept = window
						.showModalDialog(
								'../../../comm/jsp/hr/dept/dept.jsp',
								args,
								'dialogWidth:'
										+ Constants.WIDTH_COM_DEPT
										+ 'px;dialogHeight:'
										+ Constants.HEIGHT_COM_DEPT
										+ 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
				if (typeof(dept) != "undefined") {
					txtBeforeDept.setValue(dept.names);
					hiddenBeforeDept.setValue(dept.ids);
					hiddenBeforeDeptIsBanzu.setValue(dept.isBanzus);
				}
			}
		}
	});
	// 隐藏域调动前部门ID
	var hiddenBeforeDept = new Ext.form.Hidden({
				name : 'depCode'
			})
	// 隐藏域调动前部门是否班组
	var hiddenBeforeDeptIsBanzu = new Ext.form.Hidden({
				id : 'hiddenBeforeDeptIsBanzu'
			})

	
	// 调动后部门
	var txtAfterDept = new Ext.form.TextField({
		fieldLabel : '部门选择',
		width : 110,
		valueField : 'id',
		hiddenName : 'mrDept',
		maxLength : 100,
		anchor : '100%',
		readOnly : true,
		listeners : {
			focus : function() {
				var args = {
					selectModel : 'single',
					rootNode : {
						id : '0',
						text : '灞桥电厂'
					},
					onlyLeaf : false
				};
				this.blur();
				var dept = window
						.showModalDialog(
								'../../../comm/jsp/hr/dept/dept.jsp',
								args,
								'dialogWidth:'
										+ Constants.WIDTH_COM_DEPT
										+ 'px;dialogHeight:'
										+ Constants.HEIGHT_COM_DEPT
										+ 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
				if (typeof(dept) != "undefined") {
					txtAfterDept.setValue(dept.names);
					hiddenAfterDept.setValue(dept.ids);
					hiddenAfterDeptIsBanzu.setValue(dept.isBanzus)
				}
			}
		}
	});
	// 隐藏域调动后部门ID
	var hiddenAfterDept = new Ext.form.Hidden({
				name : 'depCode'
			})
	// 隐藏域调动后部门是否班组
	var hiddenAfterDeptIsBanzu = new Ext.form.Hidden({
				id : 'hiddenAfterDeptIsBanzu'
			})

	
	var queryText = new Ext.form.TextField({
		id : 'queryText'
	})
	// 查询按钮
	var btnQuery = new Ext.Button({
				text : Constants.BTN_QUERY,
				iconCls : Constants.CLS_QUERY,
				handler : queryEmpMoveData
			});
	// 新增按钮
	var btnAdd = new Ext.Button({
				text : Constants.BTN_ADD,
				iconCls : Constants.CLS_ADD,
				handler : addEmpMoveData
			});
	/**
	 * 点击新增按钮时
	 */
	function addEmpMoveData() {
		flag = FLAG_SUB_ADD;
		flagSub = "";
		win.x = undefined;
		win.y = undefined;
		win.show();
		// 设置子窗口标题
		win.setTitle("新增员工调动单");
		// 调动不可用
		CmbStationType.setDisabled(false);

		textName.setDisabled(false);
		// 显示增加合作伙伴性质信息窗口
		headForm.getForm().reset();
	
		textRequisition.setDisabled(false);
		textADeptName.setDisabled(false);
		CmbAStation.setDisabled(false);
		storeAStation.removeAll();
		
		storeBStation.removeAll();
		beforeBanZuStore.removeAll();
		afterBanZuStore.removeAll();
		
		textRemoveDate.setDisabled(false);
		textReason.setDisabled(false);
		textMemo.setDisabled(false);
		btnNSave.setDisabled(false);
		btnNCancel.setDisabled(false);
		getRequisitionNo();
		labelTextName.setText('员工姓名<font color ="red">*</font>:');
	}

	/**
	 * 查询按钮按下时
	 */
	function queryEmpMoveData() {
			loadData(true);
	}

	// 修改按钮
	var btnModify = new Ext.Button({
				text : Constants.BTN_UPDATE,
				iconCls : Constants.CLS_UPDATE,
				handler : modifyData
			});

	/**
	 * 点击修改按钮时
	 */
	function modifyData() {
		headForm.getForm().reset();
		
		flagSub = "2";
		storeSubMove.getAt(0).data['newCheckStationGrade'] = "";
		storeAStation.removeAll();
		
		flag = FLAG_SUB_MODIFY;
		if (recordGrid.selModel.hasSelection()) {
			var record = recordGrid.getSelectionModel().getSelected();
			textName.setDisabled(true);
			hiddenStationRemoveId.setValue(record.get('stationRemoveId'));
			// 调动类型  是否为班组 1是，其余否
			if (record.get('stationMoveTypeId') == 1)
				hiddenADeptIsBanzu.setValue(0)
			else
				hiddenADeptIsBanzu.setValue(1);
			// 显示主窗口
			win.x = undefined;
			win.y = undefined;
			win.show();
			
			// 加载该行记录
			headForm.getForm().loadRecord(record);
			// 设置子窗口标题
			win.setTitle("修改员工调动单");
			labelTextName.setText('员工姓名:');
			storeSubMove.getAt(0).data['bDeptCode'] = record.get('bfdeptId');
			storeSubMove.getAt(0).data['bDeptName'] = record.get('bfDeptName');
			
			textBDeptName.setValue(record.get('bfDeptName'));
           
			storeSubMove.getAt(0).data['bStationCode'] = record
					.get('bfStationId');
			storeSubMove.getAt(0).data['bStationName'] = record
					.get('bfStationName');
			
			textBStationName.setValue(record.get('bfStationId'));
			textBStationName.setRawValue(record.get('bfStationName'));

			storeSubMove.getAt(0).data['bStationLevel'] = record
					.get('bfStationLevel');
			storeSubMove.getAt(0).data['bStationLevelName'] = record
					.get('bfStationLevelName');
			textBStationLevelName.setValue(record.get('bfStationLevel'));
            hiddenBdeptId.setValue(record.get('bfdeptId'));
			hiddenADeptId.setValue(record.get('afDeptId'));
		
			txtBeforeBanZu.setValue(record.get('beforeBanZu'));
			txtAfterBanZu.setValue(record.get('afterBanZu'));
			beforeBanZuStore.load({
								params : {
									deptId : record.get('bfdeptId')
								}
							});
			
			afterBanZuStore.load({
								params : {
									deptId : record.get('afDeptId')
								}
			              });
			
			var bfpteId=null;
			var afDeptId=null;
			if(record.get('stationMoveTypeId')=='0'){
			  bfpteId=record.get('beforeBanZuId');
			  afDeptId=record.get('afterBanZuId');
			}
			else{
			  bfpteId=record.get('bfdeptId');
			  afDeptId=record.get('afDeptId');
			}				
			storeBStation.load({
				params : {
					deptId : bfpteId
				},
				callback : function() {
					var flag = "";
					for (i = 0; i < storeBStation.getCount(); i++) {
						if (storeBStation.getAt(i).get('stationId') == record
								.get('bfStationId')) {
							flag = "1";
							break;
						}
					}
					if (flag == "") {
						textBStationName.setValue("");
					
					} else {
						textBStationName.setValue(record.get('bfStationId'));
					
					}

				}
			})		
			
				if(CmbStationType.getValue()=='0'){
					    txtBeforeBanZu.setDisabled(false);
						txtAfterBanZu.setDisabled(false);
					} 
				else  if (CmbStationType.getValue() == '1') {
						txtBeforeBanZu.setDisabled(true);
						txtAfterBanZu.setDisabled(true);
					}
				else if (CmbStationType.getValue() == '2') {
						txtBeforeBanZu.setDisabled(true);
						txtAfterBanZu.setDisabled(true);
					}
			
			
			storeAStation.load({
				params : {
					deptId : afDeptId
				},
				callback : function() {
					var flag = "";
					for (i = 0; i < storeAStation.getCount(); i++) {
						if (storeAStation.getAt(i).get('stationId') == record
								.get('afStationId')) {
							flag = "1";
							break;
						}
					}
					if (flag == "") {
						CmbAStation.setValue("");
					
					} else {
						CmbAStation.setValue(record.get('afStationId'));
					
					}

				}
			})

			printDate.setValue(record.get('printDate'))
			hiddenStatus.setValue(record.get('dcmState'));
			hiddenCheckStationGrade.setValue(record.get('checkStationGrade'));
			hiddenStationGrade.setValue(record.get('stationGrade'));
			hiddenSalaryLevel.setValue(record.get('salaryLevel'));
			hiddenLastModifiedDate.setValue(record.get('lastModifiedDate'));

			if (record.get('dcmState') == '1' || record.get('dcmState') == '2') {
				setEmpMoveStatus(true);
			} else {
				setEmpMoveStatus(false);
				
			}

			// 调动不可用
  CmbStationType.setDisabled(false);
		} else {
			Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
		}
	}

	function setEmpMoveStatus(flag) {
		
		textRequisition.setDisabled(flag);
		textADeptName.setDisabled(flag);
		CmbAStation.setDisabled(flag);
		textRemoveDate.setDisabled(flag);
		textReason.setDisabled(flag);
		textMemo.setDisabled(flag);
		btnNSave.setDisabled(flag);
	}

	// 删除按钮
	var btnDelete = new Ext.Button({
				text : Constants.BTN_DELETE,
				iconCls : Constants.CLS_DELETE,
				handler : delRecord
			});

	// 上报按钮
	var btnReport = new Ext.Button({
				text : Constants.BTN_REPOET,
				iconCls : Constants.CLS_REPOET,
				hidden : true,
				handler : reportData
			});
	// 数据源--------------------------------
	var MyRecord = Ext.data.Record.create([{
				/** 员工姓名 */
				name : 'chsName'
			}, {
				/** 人员ID */
				name : 'empId'
			}, {
				/** 岗位调动单ID */
				name : 'stationRemoveId'
			}, {
				/** 岗位调动类别ID */
				name : 'stationMoveTypeId'
			}, {
				/** 岗位调动类别 */
				name : 'stationMoveType'
			}, {
				/** 调动日期 */
				name : 'removeDate'
			}, {
				/** 起薪日期 */
				name : 'doDate2'
			}, {
				/** 调动通知单号 */
				name : 'requisition'
			}, {
				name : 'bfdeptId'
			}, {
				/** 调动前部门 */
				name : 'bfDeptName'
			}, {
				/** 调动后部门 */
				name : 'afDeptName'
			}, {
				name : 'afDeptId'
			}, {
				name : 'bfStationId'
			}, {
				/** 调动前岗位 */
				name : 'bfStationName'
			}, {
				/** 调动后岗位 */
				name : 'afStationName'
			}, {
				name : 'afStationId'
			}, {
				name : 'bfStationLevel'
			}, {
				/** 调动前岗位级别 */
				name : 'bfStationLevelName'
			}, {
				/** 调动后岗位级别 */
				name : 'afStationLevelName'
			}, {
				name : 'afStationLevel'
			}, {
				/** 执行岗级 */
				name : 'checkStationGrade'
			}, {
				/** 标准岗级 */
				name : 'stationGrade'
			}, {
				/** 薪级 */
				name : 'salaryLevel'
			}, {
				/** 单据状态 */
				name : 'dcmState'
			}, {
				/** 原因 */
				name : 'reason'
			}, {
				/** 备注 */
				name : 'memo'
			}, {
				name : 'lastModifiedDate'
			},{
				// 登记时间
				name : 'insertdate'
			},{
				// 调动前后薪级
				name : 'oldSalaryLevel'
			},{
				// 调动前后薪级
				name : 'newSalaryLevel'
			},{
				// 调动前后薪点
				name : 'oldSalaryPoint'
			},{
				// 调动前后薪点
				name : 'newSalaryPoint'
			},{
				// 调动前后岗位级别
				name : 'oldStationGrade'
			},{
				// 调动前后岗位级别
				name : 'newStationGrade'
			},{
				//调动前班组
				name : 'beforeBanZu'
			},{
				//调动后班组
				name : 'afterBanZu'
			},{
				//打印日期 
				name : 'printDate'
			},{
				//调动前班组ID
				name : 'beforeBanZuId'
			},{
				//调动后班组ID 
				name : 'afterBanZuId'
			}]);
	// 定义获取数据源
	var dataProxy = new Ext.data.HttpProxy({
				url : 'hr/getEmpMoveDeclareInfo.action'
			});

	// 定义格式化数据
	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount",
				sortInfo : {
					field : "stationRemoveId",
					direction : "ASC"
				}
			}, MyRecord);
	// 定义封装缓存数据的对象
	var queryStore = new Ext.data.Store({
				// 访问的对象
				proxy : dataProxy,
				// 处理数据的对象
				reader : theReader
			});



	// 选择类型
	var moveTypeStore = new Ext.data.SimpleStore({
		         data : [['', '全部'], ['0', '部门内'],['1', '部门间'], ['2','内部借调']], 
				fields : ['id', 'text']
			})
	var moveType = new Ext.form.ComboBox({
				id : 'moveType',
				store : moveTypeStore,
				displayField : 'text',
				valueField : 'id',
				mode : 'local',
				width : 70,
				triggerAction : 'all',
				readOnly : true,
				value : ''
			})
	var queryNo = new Ext.form.TextField({
				id : 'queryNo'
			})
	
	var removeIds = new Array();		
	var printBtu = new Ext.Button({
		id : 'printBtu',
		iconCls : 'print',
		text : '打印调动通知单',
		handler : function() {
			// 如果选择了数据的话，先弹出提示
			if (recordGrid.selModel.hasSelection()) {
				if(recordGrid.selModel.getSelections().length > 1){
				
					var selections = recordGrid.getSelectionModel().getSelections();
						for (var i = 1; i < selections.length; i += 1) {
				         var member = selections[i];
				         if((selections[0].get('requisition')!=member.get('requisition'))||
				             (selections[0].get('stationMoveTypeId')!=member.get('stationMoveTypeId'))){
					       Ext.Msg.alert('提示','通知单号不一致，请修改一致后再打印!');
					       return;
				         }
			      }
				}
				var selections = recordGrid.getSelectionModel().getSelections();
				for (var i = 0; i < selections.length; i += 1) {
			    var member = selections[i];
				  if (member.get("stationRemoveId") != null) {
					 removeIds.push(member.get("stationRemoveId"));
				  }
				}
		
				var record = recordGrid.getSelectionModel().getSelected();
				Ext.Msg.confirm(Constants.CONFIRM, '确认要打印吗？', function(
						buttonobj) {
					// 如果选择是
					if (buttonobj == "yes") {
						var movetype = record.get('stationMoveTypeId');
						var printDate = record.get('printDate');
						var stationRemoveIds = ","+removeIds.join(",")+",";
						
						// 部门内
						if (movetype == 0) {
							var url="/power/hr/deptInMoveOrder.action?requisitionNo="+record.get('requisition')+"" +
									"&removeType="+ 0+"&printDate="+ record.get('printDate')+"&stationRemoveIds="+stationRemoveIds+"";
							window.open(url);

						}
						// 部门间
						else if(movetype == 1){
							
							var url="/power/hr/deptBetMoveOrder.action?requisitionNo="+record.get('requisition')+"" +
									"&removeType="+1+"&printDate="+ record.get('printDate')+"&stationRemoveIds="+stationRemoveIds+"";
							window.open(url);
						
						}
						// 内部借调
						else {
							
							
							var url="/power/hr/neiBuJieDiaoOrder.action?requisitionNo="+record.get('requisition')+"" +
									"&removeType="+2+"&printDate="+ record.get('printDate')+"&stationRemoveIds="+stationRemoveIds+"";
							window.open(url);
						}
						
						
						removeIds=[];
					}
				});

			} else {
				// 如果没有选择数据，弹出错误提示框
				Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
			}

		}
	})
	
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
	function myExport() {
		if (recordGrid.selModel.hasSelection()) {
			var recs = recordGrid.getSelectionModel().getSelections();
			var html = ['<table border=1><tr><th rowspan=2>姓名</th><th colspan=4>未调动以前</th><th colspan=4>调动以后</th><th colspan=3>调动日期</th><th rowspan=2>备注</th></tr>'];
				html.push('<tr><th>职务</th><th>岗级</th><th>薪点</th><th>部门</th><th>职务</th><th>岗级</th><th>薪点</th><th>部门</th><th>年</th><th>月</th><th>日</th></tr>')
				
				for(var j = 0; j<recs.length; j++){
					var re = recs[j];
					var dateStr = re.get('removeDate')
					html.push('<tr><td>'+re.get('chsName')+'</td><td>'+re.get('bfStationName')+'</td>' +
							'<td>'+re.get('bfStationLevelName')+'</td><td>'+re.get('oldSalaryPoint')+'</td>' +
							'<td>'+re.get('bfDeptName')+'</td><td>'+re.get('afStationName')+'</td>' +
									'<td>'+re.get('afStationLevelName')+'</td><td>'+re.get('newSalaryPoint')+'</td>' +
											'<td>'+re.get('afDeptName')+'</td><td>'+dateStr.substring(0,4)+'</td>' +
													'<td>'+dateStr.substring(5,7)+'</td><td>'+dateStr.substring(8)+'</td>' +
															'<td>'+re.get('memo')+'</td></tr>')
				}

				
				html.push('</table>');
				html = html.join(''); // 最后生成的HTML表格
				// alert(html);
				tableToExcel(html);
				}else{
					Ext.Msg.alert('提示','请选择要导出的数据！')
				}
		
	}
	var exprotBtu = new Ext.Button({
		text : '导出',
		iconCls : 'export',
		handler : myExport
	})
	var headTbar = new Ext.Toolbar({
				region : 'north',
				border : false,
				height : 25,
				items : ['调动日期:', startDate, '~', endDate,'-','登记时间:',insertDate, '-', '调动类型：',
						moveType, cmbStatus, "-", '调动前部门:', txtBeforeDept,
						'调动后部门:', txtAfterDept, '-', '通知单编号:', queryNo]
			});
	var gridTbar = new Ext.Toolbar({
				border : false,
				height : 25,
				items : ['员工姓名',queryText,'-',btnQuery, btnAdd, btnModify, btnDelete, btnReport,
						'-', printBtu,exprotBtu]
			});
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	})
	var recordGrid = new Ext.grid.GridPanel({
				region : "center",
//				renderTo : 'mygrid',
				store : queryStore,
				sm : sm,
				columns : [sm,
						// 自动行号
						new Ext.grid.RowNumberer({
									header : "行号",
									width : 31
								}), {
							header : "员工姓名",
							sortable : true,
							dataIndex : 'chsName'
						}, {
							header : '员工ID',
							hidden : true,
							dataIndex : 'empId'

						}, {
							header : "调动日期",
							sortable : true,
							dataIndex : 'removeDate'
						}, {
							header : "起薪日期",
							sortable : true,
							hidden : true,
							dataIndex : 'doDate2'
						}, {
							header : "调动通知单号",
							sortable : true,
							dataIndex : 'requisition',
							width:130,
							renderer:function(value, cellmeta, record, rowIndex, columnIndex, store) 
							{
								if(value==null||value=="")
								{
								  return "";
								}
								else
								{
								var typeId=record.get("stationMoveTypeId");
								var nowYear=new Date().getYear();
								if(typeId==0)
								{
									//部门内
									return "调字("+nowYear+")第"+value+"号";
								}else if(typeId==1)
								{
								 //部门间	
									return "人内调字("+nowYear+")第"+value+"号";
								}else if(typeId==2)
								{
								 //内部借调	
										return "借调字("+nowYear+")第"+value+"号";
								}
								else
								{
								  return value;	
								}
								}
							}
						}, {
							header : "调动前岗位",
							sortable : true,
							dataIndex : 'bfStationName'
						}, {
							header : "调动前岗位级别",
							sortable : true,
							dataIndex : 'bfStationLevelName'
						}, {
							header : "调动前薪级别",
							sortable : true,
							// 待处理
							hidden : true,
							dataIndex : 'oldSalaryLevel'
						}, {
							header : "调动前薪点",
							sortable : true,
							// 待处理
							hidden : false,
							dataIndex : 'oldSalaryPoint'
						}, {
							header : "调动前部门",
							sortable : true,
							dataIndex : 'bfDeptName'
						}, {
							header : "调动前班组",
							sortable : true,
							dataIndex : 'beforeBanZu'
						},{
							header : "调动后岗位",
							sortable : true,
							dataIndex : 'afStationName'
						}, {
							header : "调动后岗位级别",
							sortable : true,
							dataIndex : 'afStationLevelName'
						}, {
							header : "调动后薪点",
							sortable : true,
							dataIndex : 'newSalaryPoint'
						}, {

							header : "调动后部门",
							sortable : true,
							dataIndex : 'afDeptName'
						}, {
							header : "调动后班组",
							sortable : true,
							dataIndex : 'afterBanZu'
						}, {
							header : "单据状态",
							sortable : true,
							hidden : true,
							dataIndex : 'dcmState',
							renderer : function(value) {
								if (value == "0")
									return '未上报';
								else if (value == "1")
									return '已上报'
								else if (value == "2")
									return '已终结'
								else if (value == "3")
									return '已退回'
							}
						}, {
							header : "原因",
							sortable : true,
							hidden : true,
							dataIndex : 'reason'
						}, {
							header : "备注",
							sortable : true,
							dataIndex : 'memo'
						}, {
							header : "登记时间",
							sortable : true,
							dataIndex : 'insertdate',
							renderer : function(v) {
						if (v != null && v.length > 10) {
							return v.substr(0, 10);
						} else {
							return v;
						}
					}
						}, {
							header : "打印时间",
							sortable : true,
							renderer : function(v) {
						if (v != null && v.length > 10) {
							return v.substr(0, 10);
						} else {
							return v;
						}
					},
							dataIndex : 'printDate'
						}],
				sm : sm,
				autoScroll : true,
				enableColumnMove : false,
				autoSizeColumns : true,
				viewConfig : {
					forceFit : false
				},
				// 头部工具栏
				tbar : gridTbar,
				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : Constants.PAGE_SIZE,
							store : queryStore,
							displayInfo : true,
							displayMsg : Constants.DISPLAY_MSG,
							emptyMsg : Constants.EMPTY_MSG
						})
			});

	// // 双击进入登记tab
	
	queryStore.on('beforeload', function() {
				this.baseParams = {
					startDate : startDate.getValue(),
					endDate : endDate.getValue(),
					beforeDeptCode : hiddenBeforeDept.getValue(),
					afterDeptCode : hiddenAfterDept.getValue(),
					dcmStatus : cmbStatus.getValue(),
					moveType : moveType.getValue(),
					queryNo : queryNo.getValue(),
					insertDate : insertDate.getValue(),
					queryText : queryText.getValue(),
					deptFlag:'deptFlag'
				};
			})
	queryStore.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE,
					flag : 0
				}
			});
	// 部门岗位级别store
	var storeDeptStation = new Ext.data.JsonStore({
				root : 'list',
				url : "hr/getDeptStationLevelInfo.action",
				fields : ['empId', 'empCode', 'bfdeptId', 'bfDeptName',
						'bfStationId', 'bfStationName', 'bfStationLevel',
						'bfStationLevelName', 'checkStationGrade',
						'stationGrade', 'salaryLevel','oldSalaryPoint']
			})
	storeDeptStation.on('load', function() {
		if (storeDeptStation.getCount() > 0) {
			storeSubMove.getAt(0).data['bStationCode'] = storeDeptStation
					.getAt(0).get('bfStationId');
			storeSubMove.getAt(0).data['bStationName'] = storeDeptStation
					.getAt(0).get('bfStationName');
			textBStationName.setValue(storeDeptStation.getAt(0)
					.get('bfStationId'));
			storeSubMove.getAt(0).data['bStationLevel'] = storeDeptStation
					.getAt(0).get('bfStationLevel');
			storeSubMove.getAt(0).data['bStationLevelName'] = storeDeptStation
					.getAt(0).get('bfStationLevelName');
			textBStationLevelName.setValue(storeDeptStation.getAt(0)
					.get('bfStationLevel'));
			oldSalaryPoint.setValue(storeDeptStation.getAt(0)
					.get('oldSalaryPoint'))	

			textSrBfWage.setValue(storeDeptStation.getAt(0).get('salaryLevel'));

		}
	})

	var hiddenStationRemoveId = new Ext.form.Hidden({
				id : 'hiddenStationRemoveId'
			})
	// 执行岗级
	var hiddenCheckStationGrade = new Ext.form.Hidden({
				id : 'checkStationGrade'
			})
	// 标准岗级
	var hiddenStationGrade = new Ext.form.Hidden({
				id : 'stationGrade'
			})
	// 薪级
	var hiddenSalaryLevel = new Ext.form.Hidden({
				id : 'salaryLevel'
			})
	var hiddenLastModifiedDate = new Ext.form.Hidden({
				id : 'lastModifiedDate'
			})
	var hiddenLastModifiedDateSub = new Ext.form.Hidden({
				id : 'hiddenLastModifiedDateSub'
			})
	// 员工调动单子页面store
	var storeSubMove = new Ext.data.JsonStore({
				fields : ['workerCode', 'workerName', 'bDeptCode', 'bDeptName',
						'aDeptCode', 'aDeptName', 'bStationCode',
						'bStationName', 'bStationLevel', 'bStationLevelName',
						'adjustType', 'stationChangeType',
						'oldCheckStationGrade', 'newCheckStationGrade',
						'oldStationGrade', 'newStationGrade', 'oldSalaryGrade',
						'newSalaryGrade', 'doDate', 'reason', 'momo']
			})
	storeSubMove.insert(0, new Ext.data.Record({
						'workerCode' : '',
						'workerName' : '',
						'bDeptCode' : '',
						'bDeptName' : '',
						'aDeptCode' : '',
						'aDeptName' : '',
						'bStationCode' : '',
						'bStationName' : '',
						'bStationLevel' : '',
						'bStationLevelName' : '',
						'adjustType' : '',
						'stationChangeType' : '',
						'oldCheckStationGrade' : '',
						'newCheckStationGrade' : '',
						'oldStationGrade' : '',
						'newStationGrade' : '',
						'oldSalaryGrade' : '',
						'newSalaryGrade' : '',
						'doDate' : '',
						'reason' : '',
						'momo' : ''
					}))
	var labelTextName = new Ext.form.Label({
				text : '员工姓名:',
				height : 15,
				labelAlign : 'right',
				width : 62,
				style : {
					'fontSize' : 12,
					'padding-top' : '10%'

				}
			})
	// 员工姓名
	var textName = new Ext.form.TextField({
		hideLabel : true,
		id : 'chsName',
		valueField : 'id',
		hiddenName : 'mrName',
		width : 125,
		readOnly : true,
		listeners : {
			focus : function() {
				var args = {
					selectModel : 'single',
					rootNode : {
						id : '0',
						text : '合肥电厂'
					},
					onlyLeaf : false
				};
				this.blur();
				var person = window
						.showModalDialog(
								'../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
								args,
								'dialogWidth:'
										+ Constants.WIDTH_COM_EMPLOYEE
										+ 'px;dialogHeight:'
										+ Constants.HEIGHT_COM_EMPLOYEE
										+ 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
				if (typeof(person) != "undefined") {
					textName.setValue(person.workerName);
					hiddenWorkerName.setValue(person.workerCode);
					hiddenWorkerId.setValue(person.empId);
					storeSubMove.getAt(0).data['bDeptCode'] = person.deptId;
					storeSubMove.getAt(0).data['bDeptName'] = person.deptName;
			          Ext.getCmp('bfdeptId').setValue(person.deptId);
						Ext.form.ComboBox.superclass.setValue.call(Ext
								.getCmp('bfdeptId'), person.deptName);;
					
                     hiddenBdeptId.setValue(person.deptId);
                     
               beforeBanZuStore.load({
								params : {
									deptId : person.deptId
								}
							});
				if(person.isClass=="1"){  
					txtBeforeBanZu.setValue(person.deptName);
					oldBanzu=person.deptId;
					}
				else{
				    txtBeforeBanZu.setValue("");
				}	
				
				storeBStation.load({
								params : {
									deptId : person.deptId
								}
							});
			
							
				storeSubMove.getAt(0).data['bStationCode'] = person.stationId;
			storeSubMove.getAt(0).data['bStationName'] =person.stationName;

								textBStationName.setValue(person.stationId);
			                  textBStationName.setRawValue(person.stationName);
			                 
				}
			}
		}
	});
	// 隐藏域code
	var hiddenWorkerName = new Ext.form.Hidden({
				name : 'workCode'
			})
	// 隐藏域ID
	var hiddenWorkerId = new Ext.form.Hidden({
				name : 'workId'
			})

	// 调动通知单号
	var textRequisition = new Ext.form.NumberField({
				id : "requisition",
				maxLength : 20,
				//readOnly:true,
				fieldLabel : '调动通知单号'
			});
	// 调动前部门
	var textBDeptName = new Ext.form.ComboBox({
				fieldLabel : '调动前部门',
				width : 125,
				allowBlank :false,
				store : storeSubMove,
				id: "bfdeptId",
				name : "bfdeptId",
				displayField : "bfDeptName",
				valueField : "bfdeptId",
			  listeners : {
			focus : function() {
				var args = {
					selectModel : 'single',
					rootNode : {
						id : '0',
						text : '灞桥电厂'
					},
					onlyLeaf : false
				};
				this.blur();
				var dept = window
						.showModalDialog(
								'../../../comm/jsp/hr/dept/dept.jsp',
								args,
								'dialogWidth:'
										+ Constants.WIDTH_COM_DEPT
										+ 'px;dialogHeight:'
										+ Constants.HEIGHT_COM_DEPT
										+ 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
				if (typeof(dept) != "undefined") {
					textBDeptName.setValue(dept.names);
					hiddenBdeptId.setValue(dept.ids);
					hiddenBdeptIsBanzu.setValue(dept.isBanzus);
			        
					txtBeforeBanZu.setValue("");
					beforeBanZuStore.load({
								params : {
									deptId : dept.ids
								}
							})
				   
					storeBStation.load({
								params : {
									deptId : dept.ids
								}
							})		
				}
			}
		}
			
			});
			
   
	// 调动后部门
	var textADeptName = new Ext.form.TextField({
		fieldLabel : '调动后部门<font color ="red">*</font>',
		valueField : 'id',
		hiddenName : 'mrDept',
		id : 'afDeptName',
		maxLength : 100,
		readOnly : true,
		listeners : {
			focus : function() {
				var args = {
					selectModel : 'single',
					rootNode : {
						id : '0',
						text : '灞桥电厂'
					},
					onlyLeaf : false
				};
				this.blur();
				var dept = window
						.showModalDialog(
								'../../../comm/jsp/hr/dept/dept.jsp',
								args,
								'dialogWidth:'
										+ Constants.WIDTH_COM_DEPT
										+ 'px;dialogHeight:'
										+ Constants.HEIGHT_COM_DEPT
										+ 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
				if (typeof(dept) != "undefined") {
					textADeptName.setValue(dept.names);
					hiddenADeptId.setValue(dept.ids);
					hiddenADeptIsBanzu.setValue(dept.isBanzus);
					CmbAStation.setValue("");
					CmbAStationLevel.setValue("");
					storeAStation.load({
								params : {
									deptId : dept.ids
								}
							});
					
				   txtAfterBanZu.setValue("");
					afterBanZuStore.load({
								params : {
									deptId : dept.ids
								}
							})
				}
			}
		}
	});
	// 隐藏域调动后部门ID
	var hiddenADeptId = new Ext.form.Hidden({
				name : 'afDeptId'
			})
	var hiddenADeptIsBanzu = new Ext.form.Hidden({
				id : 'hiddenADeptIsBanzu'
			})
	var hiddenStatus = new Ext.form.Hidden({
				name : 'hiddenStatus'
			})
	
    var hiddenBdeptId=new Ext.form.Hidden({
				name : 'bfdeptId'
			})
    var hiddenBdeptIsBanzu=new Ext.form.Hidden({
				id : 'hiddenBdeptIsBanzu'
			})

	var storeMove = new Ext.data.JsonStore({
				root : 'list',
				url : "hr/getStationMoveTypeList.action",
				fields : ['stationMoveTypeId', 'stationMoveType']
			})

	var aStationTypeRecord = Ext.data.Record.create([{
				// 岗位级别id
				name : 'stationMoveTypeId'
			}, {
				// 岗位级别名称
				name : 'stationMoveType'
			}]);
	storeMove.on('load', function() {
		if (storeMove.getCount() > 0) {
			CmbStationType
					.setValue(storeMove.getAt(0).get('stationMoveTypeId'))
		}
		})
	// 调动类别
	var CmbStationType = new Ext.form.ComboBox({
		fieldLabel : '调动类别<font color ="red">*</font>',
		id : "stationMoveTypeId",
		width : 125,
		store : storeMove,
		name : "stationMoveTypeId",
		displayField : "stationMoveType",
		valueField : "stationMoveTypeId",
		mode : 'local',
		triggerAction : 'all',
		readOnly : true,
		listeners : {
					select : function() {
					if(CmbStationType.getValue()=='0'){
					    txtBeforeBanZu.setDisabled(false);
						txtAfterBanZu.setDisabled(false);
					} 
					else  if (CmbStationType.getValue() == '1') {
						txtBeforeBanZu.setDisabled(true);
						txtAfterBanZu.setDisabled(true);
					}
				    else if (CmbStationType.getValue() == '2') {
						txtBeforeBanZu.setDisabled(true);
						txtAfterBanZu.setDisabled(true);
					}
				  }
				}
		});
		
	storeMove.load();

			
	var beforeBanZuStore = new Ext.data.JsonStore({
				root : 'list',
				url : 'hr/getBeforeBanZuList.action',
				fields : ['deptId', 'deptCode', 'deptName']
			})
	var contractTermData = Ext.data.Record.create([{
				name : 'deptId'
			}, {
				name : 'deptCode'
			}, {
				name : 'deptName'
			}]);
					
	beforeBanZuStore.on('load', function() {
				var record = new contractTermData({
							deptId : "",
							deptCode : "",
							deptName : ""
						})
				beforeBanZuStore.insert(0, record);

			})
			//调动前班组-
	var txtBeforeBanZu = new Ext.form.ComboBox({
				fieldLabel : '调动前班组',
				width : 125,
				store : beforeBanZuStore,
				allowBlank:true,
				id:'beforeDeptId',
				name : "beforeDeptId",
				displayField : "deptName",
				valueField : "deptId",
				mode : 'local',
				triggerAction : 'all',
				readOnly : true,
				listeners : {
					select : function() {
					oldBanzu=txtBeforeBanZu.getValue()
					if(CmbStationType.getValue()=='0'){
						storeBStation.load({
								params : {
									deptId : txtBeforeBanZu.getValue()
								}
							});
					}
				  }
				}
			});
			
					
	var afterBanZuStore = new Ext.data.JsonStore({
				root : 'list',
				url : 'hr/getBeforeBanZuList.action',
				fields : ['deptId', 'deptCode', 'deptName']
			})
	var contractTermData = Ext.data.Record.create([{
				name : 'deptId'
			}, {
				name : 'deptCode'
			}, {
				name : 'deptName'
			}]);
	afterBanZuStore.on('load', function() {
				var record = new contractTermData({
							deptId : "",
							deptCode : "",
							deptName : ""
						})
				afterBanZuStore.insert(0, record);

			})
			
	// 调动后班组
	var txtAfterBanZu = new Ext.form.ComboBox({
				fieldLabel : '调动后班组',
				width : 125,
				store : afterBanZuStore,
				id : "afterDeptId",
				name : "afterDeptId",
				displayField : "deptName",
				valueField : "deptId",
				mode : 'local',
				triggerAction : 'all',
				readOnly : true,
				listeners : {
					select : function() {
						newBanzu=txtAfterBanZu.getValue()
					if(CmbStationType.getValue()=='0'){
						storeAStation.load({
								params : {
									deptId : txtAfterBanZu.getValue()
								}
							});
					}
				  }
				}
			});		
			
	
	// 调动前岗位store
	var storeBStation = new Ext.data.JsonStore({
				root : 'list',
				url : "hr/linkDeptStation.action",
				fields : ['stationId', 'stationCode', 'stationName']
			})
	var contractTermData2 = Ext.data.Record.create([{
				// 岗位id
				name : 'stationId'
			}, {
				// 岗位Code
				name : 'stationCode'
			}, {
				// 岗位名称
				name : 'stationName'
			}]);
	storeBStation.on('load', function() {
				var record = new contractTermData2({
							stationId : "",
							stationCode : "",
							stationName : ""
						})
				storeBStation.insert(0, record);

			})
			//调动前岗位----------
	var textBStationName = new Ext.form.ComboBox({
				fieldLabel : '调动前岗位<font color ="red">*</font>',
				width : 125,
				store : storeBStation,
				allowBlank:false,
				id:'stationId',
				name : "stationId",
				displayField : "stationName",
				valueField : "stationId",
				mode : 'local',
				triggerAction : 'all',
				readOnly : true,
				listeners : {
					select : function() {
						if (textBStationName.getValue() != "") {
							textBStationLevelName.setValue("");
						
						} else if (textBStationName.getValue() == "") {
							textBStationLevelName.setValue("");
						}

					}
				}
			});
			
	// 调动后岗位store
	var storeAStation = new Ext.data.JsonStore({
				root : 'list',
				url : "hr/linkDeptStation.action",
				fields : ['stationId', 'stationCode', 'stationName']
			})
	var contractTermData = Ext.data.Record.create([{
				// 岗位id
				name : 'stationId'
			}, {
				// 岗位Code
				name : 'stationCode'
			}, {
				// 岗位名称
				name : 'stationName'
			}]);
	storeAStation.on('load', function() {
				var record = new contractTermData({
							stationId : "",
							stationCode : "",
							stationName : ""
						})
				storeAStation.insert(0, record);
				if (storeAStation.getCount() > 1) {
					CmbAStation.setValue(storeAStation.getAt(1)
							.get('stationId'));
				
				} else
					CmbAStation.setValue(storeAStation.getAt(0)
							.get('stationId'));

			})		
		
			
	// 调动后岗位
	var CmbAStation = new Ext.form.ComboBox({
				fieldLabel : '调动后岗位<font color ="red">*</font>',
				width : 125,
				store : storeAStation,
				name : "stationId",
				displayField : "stationName",
				valueField : "stationId",
				mode : 'local',
				triggerAction : 'all',
				readOnly : true,
				listeners : {
					select : function() {
						if (CmbAStation.getValue() != "") {
							CmbAStationLevel.setValue("");
						
						} else if (CmbAStation.getValue() == "") {
							CmbAStationLevel.setValue("");
						}
					}
				}
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
	var textBStationLevelName = new Ext.form.ComboBox({
				fieldLabel : "调动前岗位级别<font color='red'>*</font>",
				name : 'bStationLevel',
				id : 'oldStationGrade',
				store : stationLestore,
				valueField : "id",
				displayField : "text",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				anchor : '98%',
				blankText : '请选择',
				emptyText : '请选择',
				selectOnFocus : true,
				allowBlank : false
			});
	var CmbAStationLevel = new Ext.form.ComboBox({
				fieldLabel : "调动后岗位级别<font color='red'>*</font>",
				name : 'textAfSrExecute',
				id : 'newStationGrade',
				store : stationLestore,
				valueField : "id",
				displayField : "text",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				anchor : '98%',
				blankText : '请选择',
				emptyText : '请选择',
				selectOnFocus : true,
				allowBlank : false
			});

	// 调动日期
	var textRemoveDate = new Ext.form.TextField({
				fieldLabel : '调动日期<font color ="red">*</font>',
				id : 'removeDate',
				style : 'cursor:pointer',
				value : dd,
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : false,
									isShowClear : false
								});
					}
				}
			});

	// 原因
	var textReason = new Ext.form.Hidden({
				id : "reason",
				width : 359,
				height : 40,
				maxLength : 200,
				fieldLabel : '原因'
			});
	// 备注
	var textMemo = new Ext.form.TextArea({
				id : "memo",
				width : 359,
				height : 40,
				maxLength : 127,
				fieldLabel : '备注'	
			});

	
	var printDate = new Ext.form.TextField({
		id : 'printDate',
		name : 'printDate',
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
				
	// 保存按钮
	var btnNSave = new Ext.Button({
				text : Constants.BTN_SAVE,
				iconCls : Constants.CLS_SAVE,
				handler : saveData
			});


	/**
	 * 点击保存数据时
	 */
	function saveData() {
		var message = "";
		var messageLength = "";
		var deng = "";

		if (textADeptName.getValue() == "")
			message += String.format(Constants.COM_E_003, "调动后部门") + "</br>";
		if (CmbAStation.getValue() == "")
			message += String.format(Constants.COM_E_003, "调动后岗位") + "</br>";
		if(CmbAStationLevel.getValue() == "")
			message += String.format(Constants.COM_E_003, "调动后岗位级别") + "</br>";
		if(textSrAfWage.getValue() == "")
			message += String.format(Constants.COM_E_003, "调动后薪级") + "</br>";
		if (textRemoveDate.getValue() == "")
			message += String.format(Constants.COM_E_003, "调动日期") + "</br>";

		if (textRequisition.getValue().toString().length > 20)
			messageLength += "1";
		if (textReason.getValue().toString().length > 200)
			messageLength += "1";
		if (textMemo.getValue().toString().length > 127)
			messageLength += "1";
		if (FLAG_SUB_ADD == flag) {
			if (textName.getValue() == "")
				message = String.format(Constants.COM_E_003, "员工姓名") + "</br>"
						+ message;
		}
		if (textBDeptName.getValue() != hiddenADeptId.getValue()) {
			deng = "0";
		} else if (textBStationName.getValue() != CmbAStation.getValue())
			deng = "0"
		else
			deng = "1";
		if (message != "")
			Ext.Msg.alert(Constants.ERROR, message);
		else {
			if (FLAG_SUB_MODIFY == flag) {
				Ext.Msg.confirm(Constants.NOTICE_CONFIRM, Constants.COM_C_001,
						function(buttonobj) {
							// 如果选择是
							if (buttonobj == "yes") {
								if (messageLength != "") {
								} else if (deng == "1") {
									Ext.Msg.alert(Constants.ERROR,
											Constants.PE003_E_002);
								} else {
									 var rec = recordGrid.getSelectionModel().getSelected();
									   oldBanzu=rec.get('beforeBanZuId');
									   newBanzu=rec.get('afterBanZuId');
									Ext.Ajax.request({
										method : Constants.POST,
										url : 'hr/updateStationRemoveInfo.action',
										params : {
											// 员工调动id
											stationRemoveId : hiddenStationRemoveId
													.getValue(),
											// 调动通知单号
											requisitionNo : textRequisition
													.getValue(),
											// 部门id
											deptId : hiddenADeptId.getValue(),
											// 调动前部门Id-----------------
											bdeptId:hiddenBdeptId.getValue(),    
											// 岗位id
											stationId : CmbAStation.getValue(),
											//调动前岗位-----------------
											bstationId:textBStationName .getValue(),
											// 调动后岗位级别
											staionLevel : CmbAStationLevel
													.getValue(),
											// 调动类别
											stationType : CmbStationType
													.getValue(),
											// 调动日期
											removeDate : textRemoveDate
													.getValue(),
											// 原因
											reason : textReason.getValue(),
											// 备注
											memo : textMemo.getValue(),
											adjustType : storeSubMove.getAt(0).data['adjustType'],
											stationChangeType : storeSubMove
													.getAt(0).data['stationChangeType'],
											oldCheckStationGrade : storeSubMove
													.getAt(0).data['oldCheckStationGrade'],
											newCheckStationGrade : storeSubMove
													.getAt(0).data['newCheckStationGrade'],
											// 调动前岗位级别
											oldStationGrade :textBStationLevelName.getValue(), 

											// 调动后岗位级别
											newStationGrade :CmbAStationLevel
													.getValue(), 

											// 调动前薪级
											oldSalaryGrade : textSrBfWage.getValue(),

											// 调动后薪级
											newSalaryGrade : textSrAfWage.getValue(),

											// 调动前薪点
											oldSalaryPoint : oldSalaryPoint.getValue(),
											// 调动后薪点
											newSalaryPoint : newSalaryPoint.getValue(),
											doDate : storeSubMove.getAt(0).data['doDate'],
											subReason : storeSubMove.getAt(0).data['reason'],
											momo : storeSubMove.getAt(0).data['momo'],
											lastModifiedDate : hiddenLastModifiedDate
													.getValue(),
											lastModifiedDateSub : hiddenLastModifiedDateSub
													.getValue()
													//调动前部门
													//调动前岗位
													//调动前岗位级别
											,oldBanzu:oldBanzu
											,newBanzu:newBanzu
											,printDate:printDate.getValue()
										},
										success : function(result, request) {
											var o = eval("("
													+ result.responseText + ")");
											var succ = o.msg;
											// 如果更新失败，弹出提示
											if (succ == Constants.SQL_FAILURE) {
												Ext.Msg.alert(Constants.ERROR,
														Constants.COM_E_014);
											} else if (succ == Constants.DATA_USING) {
												Ext.Msg.alert(Constants.REMIND,
														Constants.COM_E_015);
											} else
												Ext.Msg.alert(Constants.REMIND,
														Constants.COM_I_004,
														function() {
															// 重新加载数据
															loadData(false);
															win.hide();
														})
										}
									});
								} 

							}
						})
			} else if (FLAG_SUB_ADD == flag) {
				Ext.Msg.confirm(Constants.NOTICE_CONFIRM, Constants.COM_C_001,
						function(buttonobj) {
							// 如果选择是
							if (buttonobj == "yes") {
								if (messageLength != "") {
								} else if (deng == "1") {
									Ext.Msg.alert(Constants.ERROR,
											Constants.PE003_E_002);
								} else{
									
									Ext.Ajax.request({
										method : Constants.POST,
										url : 'hr/addStationRemoveInfo.action',
										params : {
											empId : hiddenWorkerId.getValue(),
											requisitionNo : textRequisition
													.getValue(),
											deptId : hiddenADeptId.getValue(),
											beforeDeptCode : hiddenBdeptId.getValue(),
											stationId : CmbAStation.getValue(),
											bfStationId : textBStationName
													.getValue(),
											staionLevel : CmbAStationLevel
													.getValue(),
											bfStaionLevel : textBStationLevelName
													.getValue(),
											removeDate : textRemoveDate
													.getValue(),
											reason : textReason.getValue(),
											memo : textMemo.getValue(),
											adjustType : storeSubMove.getAt(0).data['adjustType'],
											stationChangeType : storeSubMove
													.getAt(0).data['stationChangeType'],
											oldCheckStationGrade : storeSubMove
													.getAt(0).data['oldCheckStationGrade'],
											newCheckStationGrade : storeSubMove
													.getAt(0).data['newCheckStationGrade'],
											// 调动前岗位级别
											oldStationGrade :textBStationLevelName.getValue(), 

											// 调动后岗位级别
											newStationGrade :CmbAStationLevel
													.getValue(), 

											// 调动前薪级
											oldSalaryGrade : textSrBfWage.getValue(),

											// 调动后薪级
											newSalaryGrade : textSrAfWage.getValue(),

											// 调动前薪点
											oldSalaryPoint : oldSalaryPoint.getValue(),
											// 调动后薪点
											newSalaryPoint : newSalaryPoint.getValue(),	
											doDate : storeSubMove.getAt(0).data['doDate'],
											subReason : storeSubMove.getAt(0).data['reason'],
											momo : storeSubMove.getAt(0).data['momo'],
											moveTypeId : CmbStationType
													.getValue()
											,oldBanzu:oldBanzu
											,newBanzu:newBanzu
											,printDate:printDate.getValue()
										},
										success : function(result, request) {
											var o = eval("("
													+ result.responseText + ")");
											var succ = o.msg;
											// 如果更新失败，弹出提示
											if (succ == Constants.SQL_FAILURE) {
												Ext.Msg.alert(Constants.ERROR,
														Constants.COM_E_014);
											} else
												Ext.Msg.alert(Constants.REMIND,
														Constants.COM_I_004,
														function() {
															// 重新加载数据
															loadData(false);
															win.hide();
														})
										}
									});
								}
							}
						})
			}
		}
	}

	
	/*
	 * 把store里的数据设置到弹出窗口中
	 */
	function setDataToWin() {
	
		
		textSrBfWage.setValue(storeSubMove.getAt(0).data['oldSalaryGrade']);
		textSrAfWage.setValue(storeSubMove.getAt(0).data['newSalaryGrade']);
		
	}
	// 取消按钮
	var btnNCancel = new Ext.Button({
				text : Constants.BTN_CANCEL,
				iconCls : Constants.CLS_CANCEL,
				handler : cancelWin
			});

	/**
	 * 点击取消按钮时
	 */
	function cancelWin() {
		Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_005, function(
						buttonobj) {
					// 如果选择是
					if (buttonobj == "yes") {
						// 隐藏弹出窗口
						win.hide();
					}
				})
	}
	

	// 薪级store
	var salaryLevelStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'empInfoManage.action?method=getSalaryLevelStore'
						}),
				reader : new Ext.data.JsonReader({}, [{
									name : 'id'
								}, {
									name : 'text'
								}])
			});
	salaryLevelStore.load();
	// 变更前薪级
	var textSrBfWage = new Ext.form.ComboBox({
				fieldLabel : '调动前薪级',
				name : 'textSrBfWage',
				id : 'oldSalaryLevel',
				store : salaryLevelStore,
				valueField : "id",
				displayField : "text",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				editable : false,  
				triggerAction : 'all',
				anchor : '98%',
				selectOnFocus : true
//				disabled : true,
//				hideTrigger : true
			});

	// 变更后薪级
	var textSrAfWage = new Ext.form.ComboBox({
				fieldLabel : "调动后薪级<font color='red'>*</font>",
				name : 'textSrAfWage',
				id : 'newSalaryLevel',
				store : salaryLevelStore,
				valueField : "id",
				displayField : "text",
				mode : 'local',
				editable : false,
				triggerAction : 'all',
				anchor : '98%',
				blankText : '请选择',
				emptyText : '请选择',
				allowBlank : false
			});
	//调动前薪点	
	var oldSalaryPoint = new Ext.form.NumberField({
		id : 'oldSalaryPoint',
		name : 'oldSalaryPoint',
		fieldLabel : "调动前薪点"
	})//调动后薪点	
	var newSalaryPoint = new Ext.form.NumberField({
		id : 'newSalaryPoint',
		name : 'newSalaryPoint',
		fieldLabel : "调动后薪点"
	})
	var headForm = new Ext.form.FormPanel({
		border : false,
		labelAlign : 'right',
		frame : true,
		layout : 'form',
		items : [{

			layout : "column",
			border : false,
			height : 5,
			items : [hiddenCheckStationGrade, hiddenLastModifiedDateSub,
					hiddenStationGrade, hiddenSalaryLevel,
					hiddenStationRemoveId, hiddenLastModifiedDate]
		}, {
			layout : "column",
			border : false,
			labelAlign : "right",
			items : [{
						columnWidth : 0.075,
						layout : "form",
						border : false
					}, {
						columnWidth : 0.155,
						layout : "form",
						border : false,
						height : 30,
						labelAlign : "right",
						items : [labelTextName]
					}, {
						columnWidth : 0.26,
						layout : "form",
						border : false,
						height : 30,
						labelAlign : "right",
						items : [textName]
					}, {
						columnWidth : 0.013,
						layout : "form",
						border : false
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						height : 30,
						labelAlign : "right",
						items : [CmbStationType]
					}]
		}, {
			layout : "column",
			border : false,
			items : [{
						columnWidth : 0.5,
						layout : "form",
						border : false,
						height : 30,
						labelAlign : "right",
						items : [textBDeptName]
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						height : 30,
						labelAlign : "right",
						items : [textADeptName]
					}]
		},{
			layout : "column",
			border : false,
			items : [{
						columnWidth : 0.5,
						layout : "form",
						border : false,
						height : 30,
						labelAlign : "right",
						items : [txtBeforeBanZu]
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						height : 30,
						labelAlign : "right",
						items : [txtAfterBanZu]
					}]
		},{
			layout : "column",
			border : false,
			items : [{
						columnWidth : 0.5,
						layout : "form",
						border : false,
						height : 30,
						labelAlign : "right",
						items : [textBStationName, hiddenWorkerId, hiddenStatus]
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						height : 30,
						labelAlign : "right",
						items : [CmbAStation]
					}]
		},{
			layout : "column",
			border : false,
			items : [{
						columnWidth : 0.5,
						layout : "form",
						border : false,
						height : 30,
						labelAlign : "right",
						items : [textBStationLevelName]
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						height : 30,
						labelAlign : "right",
						items : [CmbAStationLevel]
					}]
		},  {
			layout : "column",
			border : false,
			items : [{
						columnWidth : 0.5,
						layout : "form",
						border : false,
						height : 30,
						labelAlign : "right",
						items : [textSrBfWage]
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						height : 30,
						labelAlign : "right",
						items : [textSrAfWage]
					}]
		},  {
			layout : "column",
			border : false,
			items : [{
						columnWidth : 0.5,
						layout : "form",
						border : false,
						height : 30,
						labelAlign : "right",
						items : [oldSalaryPoint]
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						height : 30,
						labelAlign : "right",
						items : [newSalaryPoint]
					}]
		}, {
			layout : "column",
			border : false,
			items : [{
						columnWidth : 0.5,
						layout : "form",
						border : false,
						height : 30,
						labelAlign : "right",
						items : [textRemoveDate]
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						height : 30,
						labelAlign : "right",
						items : [textRequisition]
					}]
		},{
			layout : "column",
			border : false,
			items : [{
						columnWidth : 0.5,
						layout : "form",
						border : false,
						height : 30,
						labelAlign : "right",
						items : [printDate]
					}]
		},{
			layout : "form",
			border : false,
			items : [textReason]
		}, {
			layout : "form",
			border : false,
			items : [textMemo]
		}]
	});

	// 弹出画面
	var win = new Ext.Window({
				height : 380,
				width : 500,
				modal : true,
				resizable : false,
				closeAction : 'hide',
				items : [headForm],
				buttonAlign : "center",
				title : '',
				buttons : [btnNSave,  btnNCancel]
			});

	/**
	 * 把薪酬变动单的数据设置到store里
	 */
	function setSubData() {
		
				
		storeSubMove.getAt(0).data['oldSalaryGrade'] = textSrBfWage.getValue();
		storeSubMove.getAt(0).data['newSalaryGrade'] = textSrAfWage.getValue();
		
	}


	// 显示区域
	var layout = new Ext.Viewport({
				layout : 'border',
				autoHeight : true,
				items : [headTbar, recordGrid]
			});

	function loadData(flag) {
		queryStore.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE,
						flag : 1
					},
					callback : function() {
						if (flag) {
							if (queryStore.getCount() <= 0) {
								Ext.Msg.alert(Constants.REMIND,
										Constants.COM_I_003);
							}
						}
					}
				})
	}

	/**
	 * 点击删除按钮时
	 */
	function delRecord() {
		// 如果选择了数据的话，先弹出提示
		if (recordGrid.selModel.hasSelection()) {
			
			if(recordGrid.selModel.getSelections().length > 1){
				Ext.Msg.alert('提示','请单条删除数据!');
				return;
			}
			
			var record = recordGrid.getSelectionModel().getSelected();
			if (record.get('dcmState') == '1') {
				Ext.Msg.alert(Constants.ERROR, String.format(
								Constants.COM_E_032, "已上报", "删除"));
			} else if (record.get('dcmState') == '2') {
				Ext.Msg.alert(Constants.ERROR, String.format(
								Constants.COM_E_032, "已终结", "删除"));
			} else {
				Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_002,
						function(buttonobj) {
							// 如果选择是
							if (buttonobj == "yes") {
								Ext.Ajax.request({
									method : Constants.POST,
									url : 'hr/deleteEmpMoveDeclareInfo.action',
									params : {
										stationRemoveId : record
												.get('stationRemoveId'),
										lastModifiedDate : record
												.get('lastModifiedDate')
									},
									success : function(result, request) {
										if (result.responseText) {
											var o = eval("("
													+ result.responseText + ")");
											var suc = o.msg;
											// 如果成功，弹出删除成功
											if (suc == Constants.SQL_FAILURE) {
												Ext.Msg.alert(Constants.ERROR,
														Constants.COM_E_014);
											} else if (suc == Constants.DATA_USING) {
												Ext.Msg.alert(Constants.ERROR,
														Constants.COM_E_015);
											} else if (suc == Constants.DEL_SUCCESS) {
												Ext.Msg.alert(Constants.REMIND,
														Constants.COM_I_005)
												// 成功,则重新加载grid里的数据
												// 重新加载数据
												loadData(false);
											} else {
												Ext.Msg.alert(Constants.ERROR,
														Constants.DEL_ERROR)
											}
										}
									}
								});
							}
						});
			}
		} else {
			// 如果没有选择数据，弹出错误提示框
			Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
		}
	}

	/**
	 * 点击上报时
	 */
	function reportData() {
		// 如果选择了数据的话，先弹出提示
		if (recordGrid.selModel.hasSelection()) {
			var record = recordGrid.getSelectionModel().getSelected();
			if (record.get('dcmState') == '1') {
				Ext.Msg.alert(Constants.ERROR, String.format(
								Constants.COM_E_032, "已上报", "上报"));
			} else if (record.get('dcmState') == '2') {
				Ext.Msg.alert(Constants.ERROR, String.format(
								Constants.COM_E_032, "已终结", "上报"));
			} else {
				Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_006,
						function(buttonobj) {
							// 如果选择是
							if (buttonobj == "yes") {
								Ext.Ajax.request({
									method : Constants.POST,
									url : 'hr/reportEmpMoveDeclareInfo.action',
									params : {
										stationRemoveId : record
												.get('stationRemoveId'),
										lastModifiedDate : record
												.get('lastModifiedDate')
									},
									success : function(result, request) {
										if (result.responseText) {
											var o = eval("("
													+ result.responseText + ")");
											var suc = o.msg;
											if (suc == Constants.SQL_FAILURE) {
												Ext.Msg.alert(Constants.ERROR,
														Constants.COM_E_014);
											} else if (suc == Constants.DATA_USING) {
												Ext.Msg.alert(Constants.ERROR,
														Constants.COM_E_015);
											} else {
												Ext.Msg.alert(Constants.REMIND,
														Constants.COM_I_007)
												// 成功,则重新加载grid里的数据
												// 重新加载数据
												loadData(false);
											}
										}
									}
								});
							}
						});

			}
		} else {
			// 如果没有选择数据，弹出错误提示框
			Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
		}
	}
	/**
	 * check字段长度
	 */
	function check(str) {
		// check是否是双字节的
		var reg = /[^\x00-\xff]/
		var s = 0;
		var ts;
		for (i = 0; i < str.length; i++) {
			// 取字符串中的一个字符
			ts = str.substring(i, i + 1);
			if (reg.test(ts)) {
				// 如果字符是双字节的，则长度加2
				s = s + 2;
			} else {
				// 如果是单字节的，则字段加1
				s = s + 1;
			}
		}
		return s;
	}
	/**
	 * 只取年月日 YYYY-MM-DD
	 */
	function renderDate(value) {
		if (!value)
			return "";

		var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
		var reTime = /\d{2}:\d{2}:\d{2}/gi;

		var strDate = value.match(reDate);
		var strTime = value.match(reTime);
		if (!strDate)
			return "";
		return strTime ? strDate : strDate;
	}

	/**
	 * 只取年月日 YYYY-MM-DD
	 */
	function renderDateDT(value) {
		if (!value)
			return "";

		var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
		var reTime = /\d{2}:\d{2}:\d{2}/gi;

		var strDate = value.match(reDate);
		var strTime = value.match(reTime);
		if (!strDate)
			return "";
		strTime = strTime ? strTime : '00:00:00';
		return strDate + " " + strTime;
	}

	if (hiddenBtn == "2") {
	 // 隐藏
		btnModify.setVisible(false);
		btnAdd.setVisible(false);
		btnDelete.setVisible(false);
	

	} else if (hiddenBtn == "1") {
		recordGrid.on("rowdblclick", modifyData);
	}
	
	
	function getRequisitionNo()
	{
		
		  var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", 'hr/getMaxRequisitionNo.action', false);
	conn.send(null);
	if (conn.status == "200") {
		var result = conn.responseText;
		
		
		textRequisition.setValue(result);
	}
		
	  		
	}
	
})