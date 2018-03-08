Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
	// bug edit by fangjihu
	var isFirstLoad = true;
	// bug edit by fangjihu
	// 是否正在删除更新行
	IS_DELETE = '';
	// 是否进行更新操作
	DEF_METHOD = '';
	// 方法
	strMethod = '';
	// 状态
	EMP_STATUS_FLAG = false;
	// 删除状态
	DEF_FLAG_DELETE = '0';
	// 修改状态
	DEF_FLAG_UPDATE = '1';
	// 新增状态
	DEF_FLAG_ADD = '2';
	// 上报与退回状态
	UPLOAD_AND_RETURN_FLAG = '';
	// 比较记录
	CompareRecord = Ext.data.Record.create([{
				name : 'wrokContractNo'
			}, {
				name : 'signatureDate'
			}, {
				name : 'startDate'
			}, {
				name : 'endDate'
			}, {
				name : 'cooperateUnitSub'
			}, {
				name : 'contractContent'
			}, {
				name : 'note'
			}]);
	recordDatas = ['wrokContractNo', 'signatureDate', 'startDate', 'endDate',
			'cooperateUnitSub', 'contractContent', 'note'];
	// 比较数据
	record1 = new CompareRecord({
				wrokContractNo : ''
			}, {
				signatureDate : ''
			}, {
				startDate : ''
			}, {
				endDate : ''
			}, {
				cooperateUnitSub : ''
			}, {
				contractContent : ''
			}, {
				note : ''
			});
	record2 = new CompareRecord({
				wrokContractNo : ''
			}, {
				signatureDate : ''
			}, {
				startDate : ''
			}, {
				endDate : ''
			}, {
				cooperateUnitSub : ''
			}, {
				contractContent : ''
			}, {
				note : ''
			});
	// 初始话record1和record2的值
	for (i = 0; i < recordDatas.length; i++) {
		record1.set(recordDatas[i], '');
		record2.set(recordDatas[i], '');
	}
	// render函数
	showWindow = function(id, mark) {
		var args = {
			mode : 'read',
			contractId : id,
			fileOriger : mark
		};
		window.x = undefined;
		window.y = undefined;
		var person = window
				.showModalDialog(
						'../../common/PC002.jsp',
						args,
						'dialogWidth=500px;dialogHeight=320px;directories=yes;help=no;center=yes;status=no;resizable=no;scrollbars=yes;');
	}
	// 签字日期
	var tfSignatureDateFrom = new Ext.form.TextField({
				id : 'signatureDateFrom',
				name : 'borrowContract.signatureDateFrom',
				style : 'cursor:pointer',
				readOnly : true,
				width : 75,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : false
								});
					}
				}
			});
	// 签字结束日期
	var tfSignatureDateTo = new Ext.form.TextField({
				id : 'signatureDateTo',
				name : 'borrowContract.signatureDateTo',
				style : 'cursor:pointer',
				readOnly : true,
				width : 75,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd'
								});
					}
				}
			});
	// 协作单位
	var drpCooperateUnit = new Ext.form.ComboBox({
				width : 85,
				allowBlank : true,
				forceSelection : true,
				triggerAction : 'all',
				mode : 'local',
				readOnly : true,
				displayField : 'cooperateUnit',
				valueField : 'cooperateUnitId',
				initComponent : function() {
					this.store = new Ext.data.JsonStore({
								url : 'hr/getCooperateUnitIDAndName.action',
								root : 'list',
								fields : ['cooperateUnitId', 'cooperateUnit']
							});
					this.store.load();
				}
			});
	// 调动类型
	var transferType = new Ext.form.ComboBox({
				width : 85,
				forceSelection : true,
				triggerAction : 'all',
				mode : 'local',
				readOnly : true,
				displayField : 'transferName',
				valueField : 'transferType',
				store : [['',''],['1', '外借'], ['2', '外调']]
			});

	// 单据状态
	var drpDcmStatus = new Ext.form.ComboBox({
				width : 85,
				allowBlank : true,
				forceSelection : true,
				triggerAction : 'all',
				mode : 'local',
				readOnly : true,
				displayField : 'text',
				valueField : 'value',
				store : new Ext.data.JsonStore({
							fields : ['text', 'value'],
							data : [{
										value : '',
										text : ''
									}, {
										value : '0',
										text : '未上报'
									}, {
										value : '1',
										text : '已上报'
									}, {
										value : '2',
										text : '已终结'
									}, {
										value : '3',
										text : '已退回'
									}]
						})
			});
	// 初始化单据状态初始值
	drpDcmStatus.setValue('0');

	// 【劳务派遣合同查询画面】-查询
	var btnQuery1 = new Ext.Button({
				id : 'query1',
				name : 'main.query1',
				text : Constants.BTN_QUERY,
				iconCls : Constants.CLS_QUERY,
				handler : query1
			});
	// 【劳务派遣合同查询画面】-新增
	var btnAdd1 = new Ext.Button({
				id : 'add1',
				name : 'main.add1',
				text : Constants.BTN_ADD,
				iconCls : Constants.CLS_ADD,
				handler : add1
			});
	// 【劳务派遣合同查询画面】-修改
	var btnUpdate1 = new Ext.Button({
				id : 'update1',
				name : 'main.update1',
				text : Constants.BTN_UPDATE,
				iconCls : Constants.CLS_UPDATE,
				handler : update1
			});
	// 【劳务派遣合同查询画面】-删除
	var btnDelete1 = new Ext.Button({
				id : 'delete1',
				name : 'main.delete1',
				text : Constants.BTN_DELETE,
				iconCls : Constants.CLS_DELETE,
				handler : delete1
			});
	// 【劳务派遣合同查询画面】-上报
	var btnRepoet1 = new Ext.Button({
				id : 'repoet1',
				name : 'main.repoet1',
				text : Constants.BTN_REPOET,
				iconCls : Constants.CLS_REPOET,
				handler : repoet1
			});
	// 【劳务派遣合同查询画面】- record定义
	var LaborBorrowRecord = Ext.data.Record.create([{
				name : 'borrowContractId'
			}, {
				name : 'wrokContractNo'
			}, {
				name : 'signatureDate'
			}, {
				name : 'cooperateUnitId'
			}, {
				name : 'cooperateUnit'
			}, {
				name : 'startDate'
			}, {
				name : 'endDate'
			}, {
				name : 'contractContent'
			}, {
				name : 'dcmStatus'
			}, {
				name : 'note'
			}, {
				name : 'lastModifiedBy'
			}, {
				name : 'lastModifiedDate'
			}, {
				name : 'workContractId'
			},{
				name : 'transferType'
			}]);
	// 【劳务派遣合同查询画面】-数据源
	var laborBorrowStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'hr/getLaborBorrowRegisterInfo.action'
						}),
				reader : new Ext.data.JsonReader({
							root : "list",
							totalProperty : "totalCount"
						}, LaborBorrowRecord)
			});
	// 【劳务派遣合同查询画面】-数据源参数

	// 【劳务派遣合同查询画面】-columnmodel
	var laborBorrowCM = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
						header : '行号',
						width : 35
					}), {
				header : '合同编码',
				width : 60,
				sortable : true,
				dataIndex : 'wrokContractNo'
			}, {
				header : '签字日期',
				width : 75,
				sortable : true,
				dataIndex : 'signatureDate'
			}, {
				header : '协作单位',
				width : 70,
				sortable : true,
				dataIndex : 'cooperateUnit'
			}, {
				header : '开始日期',
				width : 75,
				sortable : true,
				dataIndex : 'startDate'
			}, {
				header : '结束日期',
				width : 75,
				sortable : true,
				dataIndex : 'endDate'
			}, {
				header : '劳务内容',
				width : 100,
				sortable : true,
				dataIndex : 'contractContent'
			}, {
				header : '附件',
				align : 'left',
				width : 60,
				dataIndex : 'workContractId',
				renderer : function(v, cellmeta, record) {
					if (v != null && v != '') {
						var workcontractid = record.get("borrowContractId");
						var fileOriger = '4';
						var showWindow = 'showWindow("' + workcontractid
								+ '","' + fileOriger + '");return false;';
						return "<a href='#' onclick='" + showWindow
								+ "'>查看附件</a>";
					} else {
						return "—"
					}
				}
			},/*
				 * { header : '单据状态', width : 60, sortable : true, dataIndex :
				 * 'dcmStatus' },
				 */{
				header : '备注',
				width : 100,
				sortable : true,
				dataIndex : 'note'
			},{
			   header : '调动类型',
				width : 100,
				sortable : true,
				dataIndex : 'transferType',
				renderer : function(v){
					if(v == "1")
					return "外借"
					if(v == "2")
					return "外调"
				}
			}]);
	// 【劳务派遣合同查询画面】-工具栏头部
	var headTbar = new Ext.Toolbar({
				region : 'north',
				border : false,
				height : 25,
				items : ['签字日期:', tfSignatureDateFrom, '~', tfSignatureDateTo,
						'-', '协作单位:', drpCooperateUnit,'-','调动类型',transferType, /*
														 * '单据状态:',
														 * drpDcmStatus,
														 */'-', btnQuery1, btnAdd1, btnUpdate1, btnDelete1/*
																			 * ,
																			 * btnRepoet1
																			 */]
			});

	// 【劳务派遣合同查询画面】-GridPanel
	var laborBorrowGridPanel = new Ext.grid.GridPanel({
				store : laborBorrowStore,
				cm : laborBorrowCM,
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : true
						}),
				tbar : headTbar, // 底部工具栏
				bbar : new Ext.PagingToolbar({
							pageSize : Constants.PAGE_SIZE,
							store : laborBorrowStore,
							displayInfo : true,
							displayMsg : MessageConstants.DISPLAY_MSG,
							emptyMsg : MessageConstants.EMPTY_MSG
						}),
				enableColumnMove : false,
				frame : false,
				viewConfig : {
					forceFit : false
				}
			});

	// 【劳务派遣合同查询画面】
	var tabQuery = new Ext.Panel({
				title : '查询',
				layout : 'fit',
				border : false,
				items : [laborBorrowGridPanel]
			});
	tabQuery.on('activate', function() {
		isFirstLoad = true;
		var start = (laborBorrowGridPanel.getBottomToolbar().getPageData().activePage
				* 1 - 1)
				* Constants.PAGE_SIZE;
		laborBorrowStore.baseParams = {
			strSignatureDateFrom : tfSignatureDateFrom.getValue(),
			strSignatureDateTo : tfSignatureDateTo.getValue(),
			strCooperateUnitId : drpCooperateUnit.value,
			strDcmStatus : drpDcmStatus.value
		};
		laborBorrowStore.load({
					params : {
						start : start,
						limit : Constants.PAGE_SIZE
					}
				});
	});
	// ************劳务派遣合同登记画面登记************
	// 【劳务派遣合同登记画面】保存按钮
	var btnSave2 = new Ext.Button({
				id : 'save',
				name : 'window.save',
				text : Constants.BTN_SAVE,
				iconCls : Constants.CLS_SAVE,
				handler : save2
			});
	// 工具栏代替为第一块面板位置
	// 【劳务派遣合同登记画面】-合同编码
	var tfWrokContractNo = new Ext.form.TextField({
				id : 'wrokContractNo',
				name : 'borrowContract.wrokContractNo',
				fieldLabel : "合同编码<font color='red'>*</font>",
				width : 101,
				allowBlank : false,
				maxLength : 6,
				codeField : 'yes',
				style : {
					'ime-mode' : 'disabled'
				}
			});
	// 合同编码-事件
	tfWrokContractNo.on('blur', function() {
				record2.set('wrokContractNo', tfWrokContractNo.getValue());
			});
	// 【劳务派遣合同登记画面】-签字日期
	var tfSignatureDate = new Ext.form.TextField({
				id : 'signatureDate',
				name : 'borrowContract.signatureDate',
				fieldLabel : "签字日期<font color='red'>*</font>",
				allowBlank : false,
				readOnly : true,
				width : 100,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									isShowClear : false,
									onpicked : function() {
										tfSignatureDate.clearInvalid();
										record2.set('signatureDate',
												tfSignatureDate.getValue());
									},
									onclearing : function() {
										tfSignatureDate.markInvalid();
									}
								});
					}
				}
			});
	// 【劳务派遣合同登记画面】-第一行面板
	var borrowConctractSecondPnl = new Ext.Panel({
				border : false,
				layout : 'form',
				style : 'padding-top:10px;padding-left:2px',
				items : [{
							border : false,
							layout : "column",
							items : [{
										columnWidth : 0.30,
										border : false,
										layout : "form",
										items : [tfWrokContractNo]
									}, {
										columnWidth : 0.70,
										border : false,
										layout : 'form',
										items : [tfSignatureDate]
									}]
						}]
			});
	// 【劳务派遣合同登记画面】-开始日期
	var tfStartDate = new Ext.form.TextField({
				id : 'startDate',
				name : 'borrowContract.startDate',
				fieldLabel : "开始日期<font color='red'>*</font>",
				allowBlank : false,
				readOnly : true,
				width : 101,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									isShowClear : false,
									onpicked : function() {
										tfStartDate.clearInvalid();
										record2.set('startDate', tfStartDate
														.getValue());
									},
									onclearing : function() {
										tfStartDate.markInvalid();
									}
								});
					}
				}
			});
	// 【劳务派遣合同登记画面】-结束日期
	var tfEndDate = new Ext.form.TextField({
				id : 'endDate',
				name : 'borrowContract.endDate',
				fieldLabel : "结束日期<font color='red'>*</font>",
				allowBlank : false,
				readOnly : true,
				width : 100,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									isShowClear : false,
									onpicked : function() {
										tfEndDate.clearInvalid();
										record2.set('endDate', tfEndDate
														.getValue());
									},
									onclearing : function() {
										tfEndDate.markInvalid();
									}
								});
					}
				}
			});
	// 【劳务派遣合同登记画面】-第二行面板
	var borrowConctractThirdPnl = new Ext.Panel({
				border : false,
				layout : 'form',
				style : 'padding-top:0px;padding-left:2px',
				items : [{
							border : false,
							layout : "column",
							items : [{
										columnWidth : 0.30,
										border : false,
										layout : "form",
										items : [tfStartDate]
									}, {
										columnWidth : 0.70,
										border : false,
										layout : "form",
										items : [tfEndDate]
									}]
						}]
			});
	// 【劳务派遣合同登记画面】-协作单位
	var drpCooperateUnitSub = new Ext.form.ComboBox({
				id : 'cooperateUnitSub',
				name : 'borrowContract.cooperateUnitSub',
				fieldLabel : "协作单位<font color='red'>*</font>",
				width : 101,
				allowBlank : false,
				forceSelection : true,
				triggerAction : 'all',
				mode : 'local',
				readOnly : true,
				displayField : 'cooperateUnit',
				valueField : 'cooperateUnitId',
				initComponent : function() {
					this.store = new Ext.data.JsonStore({
								url : 'hr/getCooperateUnitIDAndName.action',
								root : 'list',
								fields : ['cooperateUnitId', 'cooperateUnit']
							});
					this.store.load();
				}
			});
	// 调动类型
	var drpTransferType = new Ext.form.ComboBox({
				id : 'drpTransferType',
				name : 'borrowContract.transferType',
				fieldLabel : "协作单位<font color='red'>*</font>",
				width : 101,
				allowBlank : false,
				forceSelection : true,
				triggerAction : 'all',
				mode : 'local',
				readOnly : true,
				fieldLabel : "调动类型<font color='red'>*</font>",
				displayField : 'transferName',
				valueField : 'transferType',
				store : [['1', '外借'], ['2', '外调']]
			});
	// 协作单位-失去焦点事件
	drpCooperateUnitSub.on('blur', function() {
				record2.set('cooperateUnitSub', drpCooperateUnitSub.getValue());
			});
	// 【劳务派遣合同登记画面】-设置协作单位事件
	drpCooperateUnitSub.on('change', function() {
				hiddenCooperateUnitId.setValue(drpCooperateUnitSub.getValue());
			});
	// 【劳务派遣合同登记画面】-劳务派遣合同ID
	var hiddenBorrowContractId = new Ext.form.Hidden({
				id : 'borrowContractId',
				value : null
			});
	// 【劳务派遣合同登记画面】-协作单位ID
	var hiddenCooperateUnitId = new Ext.form.Hidden({
				id : 'cooperateUnitId',
				value : null
			});
	// 【劳务派遣合同登记画面】-单据状态
	var hiddenDsmStatus = new Ext.form.Hidden({
				id : 'dsmStatus',
				value : null
			});
	// 【劳务派遣合同登记画面】-更新时间
	var hiddenUpdateTime = new Ext.form.Hidden({
				id : 'updateTime',
				value : null
			});
	// 【劳务派遣合同登记画面】-第三行面板
	var borrowConctractFourthPnl = new Ext.Panel({
				border : false,
				layout : 'form',
				style : 'padding-top:0px;padding-left:2px',
				items : [{
					border : false,
					layout : "column",
					items : [{
						columnWidth : 0.3,
						border : false,
						layout : "form",
						items : [drpCooperateUnitSub, hiddenBorrowContractId,
								hiddenCooperateUnitId, hiddenDsmStatus,
								hiddenUpdateTime]
					}, {
						columnWidth : 0.7,
						border : false,
						layout : "form",
						items : [drpTransferType]

					}]
				}]
			});
	// 【劳务派遣合同登记画面】-劳务内容
	var taContractContent = new Ext.form.TextArea({
				id : 'contractContent',
				name : 'borrowContract.contractContent',
				width : 319,
				height : 35,
				maxLength : 128,
				fieldLabel : '劳务内容'
			});
	// 劳务内容-失去焦点事件
	taContractContent.on('blur', function() {
				record2.set('contractContent', taContractContent.getValue());
			});
	// 【劳务派遣合同登记画面】-第四个面板
	var borrowConctractFifththPnl = new Ext.Panel({
				border : false,
				layout : 'form',
				height : 40,
				style : 'padding-top:0px;padding-left:2px',
				items : [{
							border : false,
							layout : "column",
							items : [{
										columnWidth : 1.00,
										border : false,
										layout : "form",
										items : [taContractContent]
									}]
						}]
			});
	// 【劳务派遣合同登记画面】-备注
	var taNote = new Ext.form.TextArea({
				id : 'note',
				name : 'borrowContract.note',
				width : 319,
				height : 35,
				maxLength : 128,
				fieldLabel : '备注'
			});
	// 备注-失去焦点事件
	taNote.on('blur', function() {
				record2.set('note', taNote.getValue());
			});
	// 【劳务派遣合同登记画面】-第五个面板
	var borrowConctractSixthPnl = new Ext.Panel({
				border : false,
				layout : 'form',
				height : 40,
				style : 'padding-top:0px;padding-left:2px',
				items : [{
							border : false,
							layout : "column",
							items : [{
										columnWidth : 1.00,
										border : false,
										layout : "form",
										items : [taNote]
									}]
						}]
			});
	// 【劳务派遣合同登记画面】-第一个formpanel
	var formpanel1 = new Ext.form.FormPanel({
				labelAlign : 'right',
				border : false,
				width : 740,
				autoScroll : true,
				frame : false,
				items : [borrowConctractSecondPnl, borrowConctractThirdPnl,
						borrowConctractFourthPnl, borrowConctractFifththPnl,
						borrowConctractSixthPnl]

			});

	// ********************--*************检索合同附件信息

	// 【检索合同附件】-数据对象
	var storeQuestFile = new Ext.data.JsonStore({
				url : 'hr/getContractAppendFileDatas.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : [
						// 流水号
						'fileId',
						// 文件名
						'fileName',
						// 更新日时
						'lastModifiyDate']
			});
	storeQuestFile.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							workcontractid : hiddenBorrowContractId.getValue(),
							fileOriger : "4"

						});
			})

	// 【检索合同附件】-下载文件
	downloadFile = function(id) {
		document.all.blankFrame.src = "hr/downloadContractAppendFile.action?fileId="
				+ id;
	}
	// 【检索合同附件】-gridPanel
	var gridQuestFile = new AppendFilePanel({
				// 显示工具栏
				// 是否可编辑
				width : 320,
				height : 100,
				readOnly : false,
				// store
				store : storeQuestFile,
				// 下载方法名字
				downloadFuncName : 'downloadFile',
				// 上传文件url
				uploadUrl : 'hr/uploadContractAppendFile.action',
				// 上传文件时参数
				uploadParams : {
					workcontractid : hiddenBorrowContractId.getValue(),
					fileOriger : '4'
				},
				// 删除url
				deleteUrl : 'hr/deleteContractAppendFile.action',
				// 工具栏按钮
				showlabel : true
			});

	// 【检索合同附件】- load参数
	gridQuestFile.on('beforeupload', function() {
				Ext.apply(this.uploadParams, {
							workcontractid : hiddenBorrowContractId.getValue(),
							fileOriger : "4"

						});
			});
	// 【检索合同附件】
	var borrowConctractSeventhPnl = new Ext.Panel({
				border : false,
				layout : 'form',
				width : 450,
				height : 112,
				style : 'padding-top:5px;padding-left:110px;margin-buttom:0px',
				items : [{
							border : false,
							height : 120,
							layout : "column",
							items : [{
										columnWidth : 1.00,
										border : false,
										height : 100,
										autoScroll : true,
										layout : "form",
										items : [gridQuestFile]
									}]
						}]
			});
	// ***********************【检索派遣人员】*************
	// 【劳务派遣合同登记画面员工外借】-新增
	var btnAdd3 = new Ext.Button({
				id : 'add3',
				name : 'employeeBorrow.add3',
				text : Constants.BTN_ADD,
				iconCls : Constants.CLS_ADD,
				handler : add2
			});
	// 【劳务派遣合同登记画面员工外借】-修改
	var btnUpdate3 = new Ext.Button({
				id : 'update3',
				name : 'employeeBorrow.update3',
				text : Constants.BTN_UPDATE,
				iconCls : Constants.CLS_UPDATE,
				handler : update2
			});
	// 【劳务派遣合同登记画面员工外借】-删除
	var btnDelete3 = new Ext.Button({
				id : 'delete3',
				name : 'employeeBorrow.delete3',
				text : Constants.BTN_DELETE,
				iconCls : Constants.CLS_DELETE,
				handler : delete2
			});
	// 【劳务派遣合同登记画面员工外借】-record定义
	var EmployeeBorrowRecord = Ext.data.Record.create([{
				name : 'empCode'
			}, {
				name : 'chsName'
			}, {
				name : 'deptName'
			}, {
				name : 'stationName'
			}, {
				name : 'employeeBorrowId'
			}, {
				name : 'startDate'
			}, {
				name : 'endDate'
			}, {
				name : 'stopPayDate'
			}, {
				name : 'startPayDate'
			}, {
				name : 'ifBack'
			}, {
				name : 'memo'
			}, {
				name : 'empId'
			}, {
				name : 'deptId'
			}, {
				name : 'stationId'
			}, {
				name : 'updateTime'
			}, {
				name : 'flag'
			}]);
	// 【劳务派遣合同登记画面员工外借】-数据源
	var employeeBorrowStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'hr/getBorrowEmployeeInfo.action'
						}),
				reader : new Ext.data.JsonReader({
							root : "list",
							totalProperty : "totalCount"
						}, EmployeeBorrowRecord)
			});
	// 【劳务派遣合同登记画面员工外借】副本-数据源
	var employeeBorrowStoreX = new Ext.data.Store(EmployeeBorrowRecord);
	// 【劳务派遣合同登记画面员工外借】-copy
	var copyEmployeeBorrowStore = new Ext.data.Store(EmployeeBorrowRecord);
	// 【劳务派遣合同登记画面员工外借】-columnmodel
	var employeeBorrowCM = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
						header : '行号',
						width : 35
					}), {
				header : '员工工号',
				width : 60,
				sortable : true,
				dataIndex : 'empCode'
			}, {
				header : '员工姓名',
				width : 60,
				sortable : true,
				dataIndex : 'chsName'
			}, {
				header : '所属部门',
				width : 60,
				sortable : true,
				dataIndex : 'deptName'
			}, {
				header : '所在岗位',
				width : 60,
				sortable : true,
				dataIndex : 'stationName'
			}, {
				header : '开始日期',
				width : 75,
				sortable : true,
				dataIndex : 'startDate'
			}, {
				header : '结束日期',
				width : 75,
				sortable : true,
				dataIndex : 'endDate'
			}, {
				header : '停薪日期',
				width : 80,
				sortable : true,
				dataIndex : 'stopPayDate'
			}, {
				header : '起薪日期',
				width : 75,
				sortable : true,
				dataIndex : 'startPayDate'
			}, {
				header : '是否已回',
				width : 60,
				sortable : true,
				dataIndex : 'ifBack'
			}, {
				header : '备注',
				width : 80,
				sortable : true,
				dataIndex : 'memo'
			}]);
	// 【劳务派遣合同登记画面员工外借】-GridPanel
	var employeeBorrowgridPanel = new Ext.grid.GridPanel({
				store : employeeBorrowStore,
				cm : employeeBorrowCM,
				width : 720,
				style : 'padding-left:0px;padding-top:0px',
				height : 180,
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : true
						}),
				tbar : [btnAdd3, btnUpdate3, btnDelete3],
				enableColumnMove : false,
				autoExpandColumn : 3,
				frame : false,
				border : true,
				viewConfig : {
					forceFit : true
				}
			});
	// 【劳务派遣合同登记画面员工外借】-第八个面板
	var borrowConctractEightthPnl = new Ext.Panel({
				border : true,
				layout : 'form',
				width : 720,
				style : 'padding-top:0px',
				items : [{
							border : false,
							height : 180,
							layout : "column",
							items : [{
										columnWidth : 1.00,
										border : false,
										height : 180,
										layout : "form",
										items : [employeeBorrowgridPanel]
									}]
						}]
			});
	// 【劳务派遣合同登记画面员工外借】
	var contractBorrowFormPanel = new Ext.Panel({
				labelAlign : 'right',
				border : false,
				autoScroll : true,
				frame : false,
				items : [formpanel1, borrowConctractSeventhPnl,
						borrowConctractEightthPnl]

			});

	// ***********************主画面********************* //
	// 【劳务登记】
	var tabRegister = new Ext.Panel({
				tbar : [btnSave2],
				title : '登记',
				layout : 'fit',
				border : false,
				items : [contractBorrowFormPanel]
			});
	tabRegister.on('activate', function() {
				if (!judgeDataIsExisted()) {
					gridQuestFile.disable();
				}
			});
	// 【劳务登记查询】tabpanel
	var tabpanel = new Ext.TabPanel({
				activeTab : 0,
				layoutOnTabChange : true,
				tabPosition : 'bottom',
				border : false,
				autoScroll : true,
				items : [tabQuery, tabRegister]
			});
	// 【 劳务派遣合同登记画面】-viewport
	var laborBorrowViewport = new Ext.Viewport({
				enableTabScroll : true,
				layout : 'fit',

				items : [tabpanel]
			});

	// ********************新增/修改劳务派遣人员*********************
	// 【劳务派遣人员】-员工姓名
	var tfChsName = new Ext.form.TextField({
				id : 'chsName',
				name : 'emp.chsName',
				fieldLabel : "员工姓名<font color='red'>*</font>",
				allowBlank : false,
				readOnly : true,
				width : 100
			});
	// 【劳务派遣人员】-员工工号
	var tfEmpCode = new Ext.form.TextField({
				id : 'empCode',
				name : 'emp.empCode',
				width : 100,
				maxLength : 10,
				disabled : true,
				codeField : 'yes',
				style : {
					'ime-mode' : 'disabled'
				},
				fieldLabel : '员工工号'
			});
	// 【劳务派遣人员】- 人员ID
	var hEmpId = new Ext.form.Hidden({
				id : 'empId',
				name : 'station.empId'
			});
	// 【劳务派遣人员】-第一面板
	var employeeBorrowFirstPnl = new Ext.Panel({
				border : false,
				layout : 'form',
				style : 'padding-top:12px',
				items : [{
							border : false,
							layout : "column",
							items : [{
										columnWidth : 0.45,
										border : false,
										layout : "form",
										items : [tfChsName]
									}, {
										columnWidth : 0.55,
										border : false,
										layout : "form",
										items : [tfEmpCode]
									}]
						}]
			});
	// 【劳务派遣人员】-所属部门
	var tfDeptName = new Ext.form.TextField({
				id : 'deptName',
				name : 'dept.deptName',
				fieldLabel : "所属部门",
				disabled : true,
				width : 313,
				maxLength : 50
			});
	// 【劳务派遣人员】-所属部门ID
	var hDeptId = new Ext.form.Hidden({
				id : 'deptId',
				name : 'station.deptId'
			});
	// 【劳务派遣人员】-第二行面板
	var employeeBorrowSecondPnl = new Ext.Panel({
				border : false,
				layout : 'form',
				style : 'padding-top:5px',
				items : [{
							border : false,
							layout : "column",
							items : [{
										columnWidth : 1.00,
										border : false,
										layout : "form",
										items : [tfDeptName]
									}]
						}]
			});
	// 【劳务派遣人员】-所在岗位
	var tfStationName = new Ext.form.TextField({
				id : 'stationName',
				name : 'station.stationName',
				fieldLabel : '所在岗位',
				disabled : true,
				author : '100%',
				maxLength : 30,
				width : 100
			});
	// 【劳务派遣人员】-所在岗位ID
	var hStationId = new Ext.form.Hidden({
				id : 'stationId',
				name : 'station.stationId'
			});
	// 【劳务派遣人员】-radio
	var radioY = new Ext.form.Radio({
				id : 'ifBackY',
				boxLabel : '是',
				name : 'flag',
				inputValue : '1'
			});
	var radioN = new Ext.form.Radio({
				id : 'radioGroup',
				boxLabel : '否',
				name : 'flag',
				inputValue : '0',
				checked : true
			});
	// 【劳务派遣人员】-是否已回
	var rdIfBack = {
		id : 'ifBack',
		layout : 'Column',
		name : 'employeeBorrow.ifBack',
		isFormField : true,
		fieldLabel : "是否已回<font color='red'>*</font>",
		border : false,
		items : [{
					columnWidth : .4,
					border : false,
					items : radioY
				}, {
					columnWidth : .5,
					border : false,
					items : radioN
				}]
	};
	// 【劳务派遣人员】-第三行面板
	var employeeBorrowThirdPnl = new Ext.Panel({
				border : false,
				layout : 'form',
				style : 'padding-top:5px',
				items : [{
							border : false,
							layout : "column",
							items : [{
										columnWidth : 0.45,
										border : false,
										layout : "form",
										items : [tfStationName]
									}, {
										columnWidth : 0.55,
										border : false,
										layout : "form",
										items : [rdIfBack]
									}]
						}]
			});
	// 【劳务派遣人员】-开始日期
	var tfSubStartDate = new Ext.form.TextField({
				id : 'subStartDate',
				name : 'employeeBorrow.subStartDate',
				fieldLabel : "开始日期<font color='red'>*</font>",
				width : 100,
				readOnly : true,
				allowBlank : false,
				listeners : {
					focus : function() {
						WdatePicker({
									isShowClear : false,
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									onpicked : function() {
										tfSubStartDate.clearInvalid();
									},
									onclearing : function() {
										tfSubStartDate.markInvalid();
									}
								});
					}
				}
			});
	// 【劳务派遣人员】-结束日期
	var tfSubEndDate = new Ext.form.TextField({
				id : 'subEndDate',
				name : 'employeeBorrow.subEndDate',
				fieldLabel : "结束日期<font color='red'>*</font>",
				width : 100,
				readOnly : true,
				allowBlank : false,
				listeners : {
					focus : function() {
						WdatePicker({
									isShowClear : false,
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									onpicked : function() {
										tfSubEndDate.clearInvalid();
									},
									onclearing : function() {
										tfSubEndDate.markInvalid();
									}
								});
					}
				}
			});

	// 【劳务派遣人员】-第四行面板
	var employeeBorrowFourthPnl = new Ext.Panel({
				border : false,
				layout : 'form',
				style : 'padding-top:5px',
				items : [{
							border : false,
							layout : "column",
							items : [{
										columnWidth : 0.45,
										border : false,
										layout : "form",
										items : [tfSubStartDate]
									}, {
										columnWidth : 0.55,
										border : false,
										layout : 'form',
										items : [tfSubEndDate]
									}]
						}]
			});
	// 【劳务派遣人员】-停薪日期
	var tfSubStopPayDate = new Ext.form.TextField({
				id : 'subStopPayDate',
				name : 'employeeBorrow.subStopPayDate',
				fieldLabel : "停薪日期",
				width : 100,
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									onpicked : function() {
										tfSubStopPayDate.clearInvalid();
									},
									onclearing : function() {
										tfSubStopPayDate.markInvalid();
									}
								});
					}
				}
			});
	// 【劳务派遣人员】-起薪日期
	var tfSubStartPayDate = new Ext.form.TextField({
				id : 'subStartPayDate',
				name : 'employeeBorrow.subStartPayDate',
				fieldLabel : "起薪日期",
				width : 100,
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({

									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									onpicked : function() {
										tfSubStartPayDate.clearInvalid();
									},
									onclearing : function() {
										tfSubStartPayDate.markInvalid();
									}
								});
					}
				}
			});
	// 【劳务派遣人员】-第五个面板
	var employeeBorrowFifththPnl = new Ext.Panel({
				border : false,
				layout : 'form',
				style : 'padding-top:5px',
				items : [{
							border : false,
							layout : "column",
							items : [{
										columnWidth : 0.45,
										border : false,
										layout : "form",
										items : [tfSubStopPayDate]
									}, {
										columnWidth : 0.55,
										border : false,
										layout : "form",
										items : [tfSubStartPayDate]
									}]
						}]
			});
	// 【劳务派遣人员】-备注
	var taMemo = new Ext.form.TextArea({
				id : 'memo',
				name : 'employeeBorrow.memo',
				width : 310,
				maxLength : 128,
				fieldLabel : '备&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp注'
			});
	// 【劳务派遣人员】-第六个面板
	var employeeBorrowSixthPnl = new Ext.Panel({
				border : false,
				layout : 'form',
				style : 'padding-top:5px',
				items : [{
							border : false,
							layout : "column",
							items : [{
										columnWidth : 1.00,
										border : false,
										layout : "form",
										items : [taMemo]
									}]
						}]
			});
	// 【劳务派遣人员】-formpanel
	var employeeBorrowFormPanel = new Ext.form.FormPanel({
				labelAlign : 'right',
				height : 280,
				border : false,
				frame : true,
				items : [employeeBorrowFirstPnl, employeeBorrowSecondPnl,
						employeeBorrowThirdPnl, employeeBorrowFourthPnl,
						employeeBorrowFifththPnl, employeeBorrowSixthPnl]

			});
	// 【劳务派遣人员】-弹出子窗口
	var employeeBorrowWindow = new Ext.Window({
				title : '新增/修改派遣人员',
				width : 500,
				buttonAlign : 'center',
				height : 320,
				closeAction : 'hide',
				autoScroll : false,
				modal : true,
				resizable : false,
				items : [employeeBorrowFormPanel],
				buttons : [{
					id : 'employeeBorrowOk',
					text : Constants.BTN_CONFIRM,
					iconCls : Constants.CLS_OK,
					handler : function() {
						// 检查合法
						var Msg = [];
						var showMsg = '';
						if (tfChsName.getValue() == '') {
							Msg.push("员工姓名");
						}
						if (tfSubStartDate.getValue() == '') {
							Msg.push("开始日期");
						}
						if (tfSubEndDate.getValue() == '') {
							Msg.push("结束日期");
						}

						for (i = 0; i < Msg.length; i++) {
							showMsg = showMsg
									+ String.format(MessageConstants.COM_E_003,
											Msg[i]) + '<br/>';
						}
						if (Msg.length > 0) {
							showMsg = showMsg.substring(0, showMsg.length - 1);
							Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
									showMsg);
							return;
						}
						if (!checkDate((Ext.get('subStartDate').dom.value),
								(Ext.get('subEndDate').dom.value))) {
							Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
									String.format(MessageConstants.COM_E_009,
											"开始日期", "结束日期"));
							return;
						}
						if (Ext.get('subStopPayDate').dom.value != ''
								&& Ext.get('subStartPayDate').dom.value != '') {
							if (!checkDate(
									(Ext.get('subStopPayDate').dom.value), (Ext
											.get('subStartPayDate').dom.value))) {
								Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
										String.format(
												MessageConstants.COM_E_009,
												"停薪日期", "起薪日期"));
								return;
							}
						}
						if (!employeeBorrowFormPanel.getForm().isValid()) {
							return;
						}
						// 新增情况
						if (EMP_STATUS_FLAG) {
							var record = new EmployeeBorrowRecord({
										flag : DEF_FLAG_ADD
									});

							record.set('flag', DEF_FLAG_ADD);
							// 员工empCode
							if (Ext.get('empCode') != null)
								record.set('empCode',
										Ext.get('empCode').dom.value);
							else {
								record.set('empCode', '');
							}
							// 员工Id
							if (hEmpId.getValue() != null)
								record.set('empId', hEmpId.getValue());
							else {
								record.set('empId', '');
							}
							// 员工姓名
							if (Ext.get('chsName') != null)
								record.set('chsName',
										Ext.get('chsName').dom.value);
							else {
								record.set('chsName', '');
							}
							// 部门ID
							if (hDeptId.getValue() != null) {
								record.set('deptId', hDeptId.getValue());
							} else {
								record.set('deptId', '');
							}

							// 部门名称
							if (Ext.get('deptName') != null)
								record.set('deptName',
										Ext.get('deptName').dom.value);
							else {
								record.set('deptName', '');
							}
							// 岗位ID
							if (hStationId.getValue() != null)
								record.set('stationId', hStationId.getValue());
							else {
								record.set('stationId', '');
							}
							// 是否返回
							record.set('ifBack', '否');
							// 岗位名称
							if (tfStationName.getValue() != null)
								record.set('stationName', tfStationName
												.getValue());
							else {
								record.set('stationId', '');
							}
							// 备注
							if (Ext.get('memo') != null)
								record.set('memo', Ext.get('memo').dom.value);
							else {
								record.set('memo', '');
							}
							// 开始日期
							if (Ext.get('subStartDate') != null)
								record.set('startDate',
										Ext.get('subStartDate').dom.value);
							else {
								record.set('startDate', '');
							}
							// 结束日期
							if (Ext.get('subEndDate') != null)
								record.set('endDate',
										Ext.get('subEndDate').dom.value);
							else {
								record.set('endDate', '');
							}
							// 停薪日期
							if (Ext.get('subStopPayDate') != null)
								record.set('stopPayDate',
										Ext.get('subStopPayDate').dom.value);
							else {
								record.set('stopPayDate', '');
							}
							// 起薪日期
							if (Ext.get('subStartPayDate') != null)
								record.set('startPayDate',
										Ext.get('subStartPayDate').dom.value);
							else {
								record.set('startPayDate', '');
							}

							// 添加record
							employeeBorrowStore.add(record);
							// 更新情况
						} else {

							var chooseRecord = employeeBorrowgridPanel.selModel
									.getSelected();
							var num = employeeBorrowStore.indexOf(chooseRecord);
							// 开始日期
							employeeBorrowStore.getAt(num).set('startDate',
									Ext.get('subStartDate').dom.value);
							// 结束日期
							employeeBorrowStore.getAt(num).set('endDate',
									Ext.get('subEndDate').dom.value);
							// 停薪日期
							employeeBorrowStore.getAt(num).set('stopPayDate',
									Ext.get('subStopPayDate').dom.value);
							// 结束日期
							employeeBorrowStore.getAt(num).set('startPayDate',
									Ext.get('subStartPayDate').dom.value);
							// 备注
							employeeBorrowStore.getAt(num).set('memo',
									Ext.get('memo').dom.value);
							// 是否返回
							if (Ext.getCmp("radioGroup").getGroupValue() == '1') {
								employeeBorrowStore.getAt(num).set('ifBack',
										'是');
							} else {
								employeeBorrowStore.getAt(num).set('ifBack',
										'否');
							}
						}
						employeeBorrowgridPanel.getView().refresh();
						employeeBorrowFormPanel.getForm().reset();
						employeeBorrowWindow.hide();

					}
				}, {
					id : 'employeeBorrowCancel',
					text : Constants.BTN_CANCEL,
					iconCls : Constants.CLS_CANCEL,
					handler : function() {
						Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
								MessageConstants.COM_C_005,
								function(buttonobj) {
									if (buttonobj == 'yes') {
										employeeBorrowFormPanel.getForm()
												.reset();
										employeeBorrowWindow.hide();
									}
								});
					}
				}]
			});
	// 【劳务派遣合同查询画面】-查询处理
	function query1() {
		
		var strSignatureDateFrom = tfSignatureDateFrom.getValue();
		var strSignatureDateTo = tfSignatureDateTo.getValue();
		if ((strSignatureDateFrom != "") && (strSignatureDateTo != "")) {
			var dateFrom = Date.parseDate(strSignatureDateFrom, 'Y-m-d');
			var dateTo = Date.parseDate(strSignatureDateTo, 'Y-m-d');
			if (dateFrom.getTime() > dateTo.getTime()) {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
								MessageConstants.COM_E_009, "开始日期", "结束日期"));
				return;
			}
		}
		isFirstLoad = false;
		// 查询画面store
		laborBorrowStore.baseParams = {
			strSignatureDateFrom : strSignatureDateFrom,
			strSignatureDateTo : strSignatureDateTo,
			strCooperateUnitId : drpCooperateUnit.value,
			strDcmStatus : drpDcmStatus.value,
			strTransferType : transferType.getValue()
		};
		laborBorrowStore.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});
	}
	// 【劳务派遣合同查询画面】-新增处理
	function add1() {
		// 激活登记面板
		tabpanel.activate(tabRegister);

		if (dataIsChanged(employeeBorrowStore, employeeBorrowStoreX)) {
			Ext.Msg.confirm(Constants.NOTICE_CONFIRM, Constants.COM_C_004,
					function(buttonobj) {
						if (buttonobj == "yes") {
							// 只有在数据清空才能证明是在新增

							UPLOAD_AND_RETURN_FLAG = '';
							hiddenBorrowContractId.setValue(null);
							hiddenUpdateTime.setValue(null);
							strMethod = DEF_FLAG_ADD;
							for (i = 0; i < recordDatas.length; i++) {
								record1.set(recordDatas[i], '');
								record2.set(recordDatas[i], '');
							}
							formpanel1.getForm().reset();
							// 清空
							employeeBorrowStore.removeAll();
							employeeBorrowStoreX.removeAll();
							storeQuestFile.removeAll();
							// 启用一些列控件
							tfWrokContractNo.enable();
							tfSignatureDate.enable();
							tfStartDate.enable();
							tfEndDate.enable();
							drpCooperateUnitSub.enable();
							taContractContent.enable();
							taNote.enable();
							btnAdd3.enable();
							btnDelete3.enable();
							// 清空store
							storeQuestFile.removeAll();
							// 清空附件工具栏
							gridQuestFile.clearAppendFileValue();
							// 禁用上传附件控件
							gridQuestFile.disable();
						} else {
							// 否则返回
							return;
						}
					});
			// 没有数据变化
		} else {
			for (i = 0; i < recordDatas.length; i++) {
				record1.set(recordDatas[i], '');
				record2.set(recordDatas[i], '');
			}
			UPLOAD_AND_RETURN_FLAG = '';
			hiddenBorrowContractId.setValue(null);
			hiddenUpdateTime.setValue(null);
			storeQuestFile.removeAll();
			formpanel1.getForm().reset();
			// gridQuestFile.getView().refresh();

			// 启用一些列控件
			tfWrokContractNo.enable();
			tfSignatureDate.enable();
			tfStartDate.enable();
			tfEndDate.enable();
			drpCooperateUnitSub.enable();
			taContractContent.enable();
			taNote.enable();
			btnAdd3.enable();
			btnDelete3.enable();
			gridQuestFile.clearAppendFileValue();
			employeeBorrowStore.removeAll();
			employeeBorrowStoreX.removeAll();
			gridQuestFile.disable();
			strMethod = DEF_FLAG_ADD;
		}

	}
	// 【劳务派遣合同查询画面】-修改函数
	function update1() {
		// 修改情况下
		var chooseRecord = laborBorrowGridPanel.selModel.getSelected();
		if (chooseRecord) {
			tabpanel.activate(tabRegister);
			// 如果数据变更的话
			if (dataIsChanged(employeeBorrowStore, employeeBorrowStoreX)) {
				Ext.Msg.confirm(Constants.NOTICE_CONFIRM, Constants.COM_C_004,
						function(buttonobj) {
							if (buttonobj == "yes") {
								// 做更新操作
								strMethod = DEF_FLAG_UPDATE;
								update1_do(chooseRecord);

								// 激活登记面板
								var dcmStatus = chooseRecord.get('dcmStatus');
								UPLOAD_AND_RETURN_FLAG = dcmStatus;
								if ((dcmStatus == '已上报')
										|| (dcmStatus == '已终结')) {
									tfWrokContractNo.disable();
									tfSignatureDate.disable();
									tfStartDate.disable();
									tfEndDate.disable();
									drpCooperateUnitSub.disable();
									drpTransferType.disabled();
									taContractContent.disable();
									taNote.disable();
									btnAdd3.disable();
									btnDelete3.disable();
									gridQuestFile.clearAppendFileValue();
									// 禁用合同面板
									gridQuestFile.disable();
								} else {
									tfWrokContractNo.enable();
									tfSignatureDate.enable();
									tfStartDate.enable();
									tfEndDate.enable();
									drpCooperateUnitSub.enable();
									drpTransferType.enable();
									taContractContent.enable();
									taNote.enable();
									btnAdd3.enable();
									btnDelete3.enable();
									// 激活合同面板
									gridQuestFile.clearAppendFileValue();
									gridQuestFile.enable();
								}
							} else {
								return;
							}
						});
				// 数据不存在
			} else {
				strMethod = DEF_FLAG_UPDATE;
				// 更新操作
				update1_do(chooseRecord);
				var dcmStatus = chooseRecord.get('dcmStatus');
				UPLOAD_AND_RETURN_FLAG = dcmStatus;
				if ((dcmStatus == '已上报') || (dcmStatus == '已终结')) {

					tfWrokContractNo.disable();
					tfSignatureDate.disable();
					tfStartDate.disable();
					tfEndDate.disable();
					drpCooperateUnitSub.disable();
					taContractContent.disable();
					taNote.disable();
					btnAdd3.disable();
					btnDelete3.disable();
					gridQuestFile.clearAppendFileValue();
					// 禁用合同面板
					gridQuestFile.disable();
				} else {

					tfWrokContractNo.enable();
					tfSignatureDate.enable();
					tfStartDate.enable();
					tfEndDate.enable();
					drpCooperateUnitSub.enable();
					taContractContent.enable();
					taNote.enable();
					btnAdd3.enable();
					btnDelete3.enable();
					gridQuestFile.clearAppendFileValue();
					// 激活合同面板
					gridQuestFile.enable();
				}

				// 激活登记面板

			}
		} else {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
		}

	}
	// 【劳务派遣合同查询画面】--删除函数
	function delete1() {
		var chooseRecord = laborBorrowGridPanel.selModel.getSelected();
		if (chooseRecord) {
			if ((chooseRecord.get('dcmStatus') == '已上报')
					|| (chooseRecord.get('dcmStatus') == '已终结')) {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
								Constants.COM_E_032, chooseRecord
										.get('dcmStatus'), '删除'));
				return;
			}
			Ext.Msg.confirm(Constants.NOTICE_CONFIRM, Constants.COM_C_002,
					function(buttonobj) {
						if (buttonobj == 'yes') {
							delete1_do(chooseRecord);
						} else {
							return;
						}
					});
			// 没有选中要弹出警告窗口
		} else {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
		}
	}
	// 【劳务派遣合同查询画面】--上报函数
	function repoet1() {
		var chooseRecord = laborBorrowGridPanel.selModel.getSelected();

		if (chooseRecord) {
			if ((chooseRecord.get('dcmStatus') == '已上报')
					|| (chooseRecord.get('dcmStatus') == '已终结')) {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
								Constants.COM_E_032, chooseRecord
										.get('dcmStatus'), '上报'));
				return;
			}
			Ext.Msg.confirm(Constants.NOTICE_CONFIRM, Constants.COM_C_006,
					function(buttonobj) {
						if (buttonobj == 'yes') {

							repoet1_do(chooseRecord);
						} else {
							return;
						}
					});
		} else {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
		}
	}
	// 【劳务派遣合同登记】-保存函数
	function save2() {
		// 检查合法性
		if (!((UPLOAD_AND_RETURN_FLAG == '已上报') || (UPLOAD_AND_RETURN_FLAG == '已终结'))) {
			var Msg = [];
			var showMsg = '';
			if (tfWrokContractNo.getValue() == '') {
				Msg.push("合同编码");
			}
			if (tfSignatureDate.getValue() == '') {
				Msg.push("签字日期");
			}
			if (tfStartDate.getValue() == '') {
				Msg.push("开始日期");
			}
			if (tfEndDate.getValue() == '') {
				Msg.push("结束日期");
			}
			if (drpCooperateUnitSub.getValue() == '') {
				Msg.push("协作单位");
			}
			if (drpTransferType.getValue() == '') {
				Msg.push("调动类型");
			}
			for (i = 0; i < Msg.length; i++) {
				showMsg = showMsg
						+ String.format(MessageConstants.COM_E_003, Msg[i])
						+ '<br/>';
			}
			if (Msg.length > 0) {
				showMsg = showMsg.substring(0, showMsg.length - 1);
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, showMsg);
				return;
			}

		}
		// 弹出确认子画面
		Ext.Msg.confirm(Constants.NOTICE_CONFIRM, Constants.COM_C_001,
				function(buttonobj) {
					if (buttonobj == 'yes') {
						if (!checkDate((Ext.get('signatureDate').dom.value),
								(Ext.get('startDate').dom.value))) {
							Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
									String.format(MessageConstants.COM_E_009,
											"签字日期", "开始日期"));
							return;
						}

						if (!checkDate((Ext.get('startDate').dom.value), (Ext
										.get('endDate').dom.value))) {
							Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
									String.format(MessageConstants.COM_E_009,
											"开始日期", "结束日期"));
							return;
						}
						// 检查员工工号重复性
						if (!formpanel1.getForm().isValid()) {
							return;
						}
						save2_do();
					} else {
						return;
					}
				});
	}
	// 【劳务派遣人员】-新增函数
	function add2() {
		employeeBorrowWindow.setTitle('新增劳务派遣人员');
		EMP_STATUS_FLAG = true;
		// 禁用人员选择
		// 把值清空
		employeeBorrowWindow.x = undefined;
		employeeBorrowWindow.y = undefined;
		Ext.getCmp('radioGroup').setValue(true);
		Ext.getCmp('ifBackY').setValue(false);
		radioY.disable();
		radioN.disable();
		employeeBorrowWindow.x = undefined;
		employeeBorrowWindow.y = undefined;
		employeeBorrowWindow.show();
		employeeBorrowFormPanel.getForm().reset();

		tfChsName.enable();
	}
	// 【劳务派遣人员】-修改函数
	function update2() {
		var chooseRecord = employeeBorrowgridPanel.getSelectionModel()
				.getSelected();
		if (chooseRecord) {
			employeeBorrowWindow.setTitle('修改劳务派遣人员');
			employeeBorrowWindow.x = undefined;
			employeeBorrowWindow.y = undefined;
			employeeBorrowWindow.show();
			// 重新reset
			employeeBorrowFormPanel.getForm().reset();
			EMP_STATUS_FLAG = false;
			// 激活
			if (chooseRecord.get('flag') == DEF_FLAG_ADD) {
				radioY.disable();
				radioN.disable();
			} else {
				radioY.enable();
				radioN.enable();
			}

			// 设置员工姓名
			tfChsName.setValue(chooseRecord.get('chsName'));
			tfChsName.disable(false);

			// 设置员工工号
			tfEmpCode.setValue(chooseRecord.get('empCode'));
			// 设置部门名
			tfDeptName.setValue(chooseRecord.get('deptName'));
			// 是否已回
			if (chooseRecord.get('ifBack') == '否') {
				Ext.getCmp('radioGroup').setValue(true);
			} else {
				Ext.getCmp('radioGroup').setValue(false);
				Ext.getCmp('ifBackY').setValue(true);
			}
			// 设置岗位
			tfStationName.setValue(chooseRecord.get('stationName'));
			// 起薪日期
			tfSubStartPayDate.setValue(chooseRecord.get('startPayDate'));
			// 停薪日期
			tfSubStopPayDate.setValue(chooseRecord.get('stopPayDate'));
			// 结束日期
			tfSubEndDate.setValue(chooseRecord.get('endDate'));
			// 开始日期
			tfSubStartDate.setValue(chooseRecord.get('startDate'));
			// 备注
			taMemo.setValue(chooseRecord.get('memo'));

		} else {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
			return;
		}

	}
	// 【劳务派遣人员】-删除函数
	function delete2() {
		var chooseRecord = employeeBorrowgridPanel.getSelectionModel()
				.getSelected();
		if (chooseRecord) {
			if (chooseRecord.get('flag') == DEF_FLAG_UPDATE) {
				var record = chooseRecord.copy();
				record.set('flag', DEF_FLAG_DELETE);
				copyEmployeeBorrowStore.add(record);
			}
			employeeBorrowStore.remove(chooseRecord);
			employeeBorrowgridPanel.getView().refresh();
		} else {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
			return;
		}
	}
	// 判断数据是否被更改
	function judgeDataIsExisted() {
		var datas = ['wrokContractNo', 'signatureDate', 'startDate', 'endDate',
				'cooperateUnitSub', 'cooperateUnitId', 'dsmStatus',
				'contractContent', 'note'];
		for (i = 0; i < datas.length; i++) {
			if ((Ext.get(datas[i]) != null)
					&& (Ext.get(datas[i]).dom.value != '')) {
				return true;
			}
		}
		// 判断员工外借登记是否有数据
		if (employeeBorrowStore.getCount() > 0) {
			return true;
		}
		// 判断合同附件是否有数据
		if (storeQuestFile.getCount() > 0) {
			return true;
		}
		return false;
	}
	// 登记画面数据是否更改
	function dataIsChanged(store, storeX) {
		for (i = 0; i < recordDatas.length; i++) {
			if (record1.get(recordDatas[i]).trim() != record2
					.get(recordDatas[i]).trim()) {
				return true;
			}
		}
		if (store.getCount() != storeX.getCount()) {
			return true;
		}
		var datas = ['empCode', 'chsName', 'deptName', 'employeeBorrowId',
				'startDate', 'endDate', 'stopPayDate', 'startPayDate',
				'ifBack', 'empId', 'deptId', 'stationId', 'updateTime', 'flag'];
		for (i = 0; i < store.getCount(); i++) {
			for (j = 0; j < datas.length; j++) {
				if (store.getAt(i).get(datas[j]) != storeX.getAt(i)
						.get(datas[j])) {
					return true;
				}
			}
		}
		return false;
	}
	// 【劳务派遣合同查询画面】-登记画面的操作
	function update1_do(record) {

		// 正在更新的劳务派遣合同ID
		formpanel1.getForm().reset();
		IS_DELETE = record.get('borrowContractId');
		// 合同编码
		tfWrokContractNo.setValue(record.get('wrokContractNo'));
		// 签字日期
		tfSignatureDate.setValue(record.get('signatureDate'));
		// 开始日期日期
		for (i = 1; i < drpCooperateUnit.store.getCount(); i++) {
			if (record.get('cooperateUnitId') == drpCooperateUnit.store
					.getAt(i).get('cooperateUnitId')) {
				// 协作单位
				drpCooperateUnitSub.setValue(record.get('cooperateUnitId'),
						true);
			}
		}
		tfStartDate.setValue(record.get('startDate'));
		// 结束日期
		tfEndDate.setValue(record.get('endDate'));
		//调动类型
		drpTransferType.setValue(record.get('transferType'));
		// 劳务内容
		taContractContent.setValue(record.get('contractContent'));
		// 备注
		taNote.setValue(record.get('note'));
		// 初始话比较数据值班
		for (i = 0; i < recordDatas.length; i++) {
			record1.set(recordDatas[i], Ext.get(recordDatas[i]).dom.value);
			record2.set(recordDatas[i], Ext.get(recordDatas[i]).dom.value);
		}
		// 劳务派遣合同ID
		hiddenBorrowContractId.setValue(record.get('borrowContractId'));
		// 协作单位ID
		hiddenCooperateUnitId.setValue(record.get('cooperateUnitId'));
		// 单据状态
		hiddenDsmStatus.setValue(record.get('dcmStatus'));
		// 更新时间
		hiddenUpdateTime.setValue(record.get('lastModifiedDate'));
		// 劳务派遣合同登记画面合同附件-数据源初始化处理
		storeQuestFile.on('beforeload', function() {
					Ext.apply(this.baseParams, {
								workcontractid : hiddenBorrowContractId
										.getValue(),
								fileOriger : "4"

							});
				})
		storeQuestFile.load();
		// 劳务派遣合同登记画面员工外借-数据源
		employeeBorrowStore.baseParams = {
			// 外借合同ID
			strWorkConstractId : record.get('borrowContractId')
		};
		employeeBorrowStore.load();
	}
	// 【劳务派遣合同查询画面】-删除按钮进行的操作
	function delete1_do(record) {
		var id = record.get('borrowContractId');
		var strUpdateTime = record.get('lastModifiedDate');
		// 删除操作
		Ext.Ajax.request({
					method : Constants.POST,
					url : 'hr/deleteBorrowContract.action',
					params : {
						strBorrowContractId : id,
						strUpdateTime : strUpdateTime
					},
					success : function(result, request) {
						var result = ("(" + result.responseText + ")");
						if (result.msg == Constants.DATA_USING) {
							// 排他处理
							Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
									MessageConstants.COM_I_002);
							return;
						} else if (result.msg == Constants.SQL_FAILURE) {
							Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
									MessageConstants.COM_E_014);
							return;
						} else if (result.msg == Constants.DATE_FAILURE) {
							Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
									MessageConstants.COM_E_023);
							return;
						} else {
							isFirstLoad = true;
							laborBorrowStore.baseParams = {
								strDcmStatus : '0'
							};
							laborBorrowStore.load({
										params : {
											start : 0,
											limit : Constants.PAGE_SIZE
										}
									});
							laborBorrowGridPanel.getView().refresh();
							// 如果删除内容在登记面板出现的话，那么则要进行清空处理
							if (id == IS_DELETE) {
								formpanel1.getForm().reset();
								// 更新时间
								hiddenUpdateTime.setValue(null);
								// 合同附件清空
								storeQuestFile.removeAll();
								// 派遣人员清空
								employeeBorrowStore.removeAll();
							}
							Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
									MessageConstants.COM_I_005);
						}
					},
					failure : function() {
						Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
								MessageConstants.UNKNOWN_ERR);
					}

				});
	}
	// 【劳务派遣合同查询画面】-上报函数操作
	function repoet1_do(record) {
		var id = record.get('borrowContractId');
		var strUpdateTime = record.get('lastModifiedDate');
		Ext.Ajax.request({
					method : Constants.POST,
					url : 'hr/repoetBorrowContract.action',
					params : {
						strBorrowContractId : id,
						strUpdateTime : strUpdateTime
					},
					success : function(result, request) {
						var result = ("(" + result.responseText + ")");
						if (result.msg == Constants.DATA_USING) {
							Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
									MessageConstants.COM_I_002);
							return;
						} else if (result.msg == Constants.SQL_FAILURE) {
							Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
									MessageConstants.COM_E_014);
							return;
						} else if (result.msg == Constants.DATE_FAILURE) {
							Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
									MessageConstants.COM_E_023);
							return;
						} else {
							isFirstLoad = true;

							Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
									MessageConstants.REPORT_SUCCESS,
									function() {
									});

							laborBorrowStore.baseParams = {
								strDcmStatus : '0'
							};
							laborBorrowStore.load({
										params : {
											start : 0,
											limit : Constants.PAGE_SIZE
										}
									});
							laborBorrowGridPanel.getView().refresh();

						}
					},
					failure : function() {
						Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
								MessageConstants.UNKNOWN_ERR);
					}
				});
	}
	// 【劳务派遣合同登记画面】-保存按钮操作
	function save2_do() {

		// 合同编号
		var wrokContractNo = Ext.get('wrokContractNo').dom.value;
		// 协作单位ID
		var strCooperateUnitId = hiddenCooperateUnitId.getValue();
		// 调动类型
		var strTransferType = drpTransferType.getValue();

		// 签字日期
		var signatureDate = Ext.get('signatureDate').dom.value;
		// 开始日期
		var startDate = Ext.get('startDate').dom.value;
		// 结束日期
		var endDate = Ext.get('endDate').dom.value;
		// 派遣合同ID
		var borrowContractId = Ext.get('borrowContractId').dom.value;
		if (hiddenBorrowContractId.getValue() != '') {
			strMethod = DEF_FLAG_UPDATE;
		} else {
			strMethod = DEF_FLAG_ADD;
		}

		// 单据状态
		var dsmStatus = '';
		if (Ext.get('dsmStatus') != null) {
			dsmStatus = Ext.get('dsmStatus').dom.value;
		}
		// 更新时间
		var updateTime = hiddenUpdateTime.getValue();
		// 合同内容
		var contractContent = '';
		if (Ext.get('contractContent') != null) {
			contractContent = Ext.get('contractContent').dom.value;
		}
		// 备注
		var note = '';
		if (Ext.get('note') != null) {
			note = Ext.get('note').dom.value;
		}
		// 重复则返回
		if (!checkStore(employeeBorrowStore)) {
			return;
		}
		// 装载数据
		var jsonData = [];
		for (i = 0; i < employeeBorrowStore.getCount(); i++) {
			jsonData.push(employeeBorrowStore.getAt(i).data);
		}
		for (j = 0; j < copyEmployeeBorrowStore.getCount(); j++) {
			jsonData.push(copyEmployeeBorrowStore.getAt(j).data);
		}
		// 发送数据去后台
		Ext.Ajax.request({
			method : Constants.POST,
			url : 'hr/doSave2OfRegister.action',
			params : {
				strMethod : strMethod,
				strBorrowContractId : borrowContractId,
				strWrokContractNo : wrokContractNo,
				strSignatureDate : signatureDate,
				strStartDate : startDate,
				strEndDate : endDate,
				strCooperateUnitId : strCooperateUnitId,
				strTransferType : strTransferType,
				strDcmStatus : dsmStatus,
				strContractContent : contractContent,
				strNote : note,
				strUpdateTime : updateTime,
				strJsonData : Ext.util.JSON.encode(jsonData)
			},
			success : function(result, request) {
				var result = eval("(" + result.responseText + ")");
				if (result.msg == Constants.SQL_FAILURE) {
					Ext.Msg.alert(Constants.ERROR, MessageConstants.COM_E_014);
					alert('后台抛员工重复异常')
					return;
				} else if (result.msg == Constants.DATA_USING) {
					Ext.Msg.alert(Constants.ERROR, Constants.COM_E_015);
					return;
				} else if (result.msg == Constants.DATE_FAILURE) {
					Ext.Msg.alert(Constants.ERROR, MessageConstants.COM_E_023);
					return;
				} else if (result.msg == 'EMPRP') {
					Ext.Msg.alert(Constants.ERROR, String.format(
									Constants.PE002_I_001, result.empId));

				} else {
					laborBorrowStore.baseParams = {
						strSignatureDateFrom : tfSignatureDateFrom.getValue(),
						strSignatureDateTo : tfSignatureDateTo.getValue(),
						strCooperateUnitId : drpCooperateUnit.value,
						strDcmStatus : drpDcmStatus.value
					};
					// 重新载入
					laborBorrowStore.load({
								params : {
									start : 0,
									limit : Constants.PAGE_SIZE
								}
							});
					// 判断ID
					var borrowContractId = result.ContractId;
					// 合同ID为空
					if (borrowContractId != null && borrowContractId != "null") {
						hiddenBorrowContractId.setValue(borrowContractId);
					}
					hiddenUpdateTime.setValue(result.updateTime);
					// 排他时间
					employeeBorrowStore.baseParams = {
						// 修改
						strWorkConstractId : hiddenBorrowContractId.getValue()
					};
					isFirstLoad = true;
					employeeBorrowStore.reload();
					// 激活附件控件
					gridQuestFile.enable();
					for (i = 0; i < recordDatas.length; i++) {
						record2
								.set(recordDatas[i], record1
												.get(recordDatas[i]));
					}
					// 成功
					Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
							MessageConstants.COM_I_004);
					copyEmployeeBorrowStore.removeAll();
					return;
				}
			},
			failure : function() {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
						MessageConstants.UNKNOWN_ERR);
			}
		});

		employeeBorrowgridPanel.getView().refresh();
		// 页面初始化
		laborBorrowGridPanel.getView().refresh();

	}
	// 【共通画面】-双击姓名编辑函数
	function selectName2() {
		if (EMP_STATUS_FLAG) {
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : Constants.POWER_NAME
				}
			};
			var object = window
					.showModalDialog(
							'../../../comm/jsp/hr/workerByDept/workerByDept2.jsp',
							args,
							'dialogWidth:550px;dialogHeight:320px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			// 根据返回值设置画面的值
			if (object) {
				// 员工姓名
				tfChsName.setValue(object.workerName);
				// 员工工号
				tfEmpCode.setValue(object.workerCode);
				// 人员Id
				hEmpId.setValue(object.empId);
				// 设置员工部门
				tfDeptName.setValue(object.deptName);
				// 部门ID
				hDeptId.setValue(object.deptId);
				// 岗位名称
				hStationId.setValue(object.stationId);
				tfStationName.setValue(object.stationName);

			}

		}
	}
	// 时间比较
	function checkDate(startDate, endDate) {
		if ((startDate != "") && (endDate != "")) {
			var date1 = Date.parseDate(startDate, 'Y-m-d');
			var date2 = Date.parseDate(endDate, 'Y-m-d');
			if (date1.getTime() > date2.getTime()) {
				return false;
			} else {
				return true;
			}
		}
	}
	// 附件删除按钮
	function dele(value) {
		if (value != "") {
			return "<span style='padding-left:19px'></span><a href='#'  onclick= 'deleteFile();return false;'><img src='comm/ext/tool/dialog_close_btn.gif'></a>";
		} else {
			return "";
		}
	}
	// 附件名

	// 下载文件
	download = function(borrowContractId) {
		document.all.blankFrame.src = "hr/downloadContractAppendFile.action?fileId="
				+ borrowContractId;
	}
	// ***************定义事件**********************
	laborBorrowGridPanel.on('rowdblclick', update1);
	// 双击事件
	employeeBorrowgridPanel.on('rowdblclick', update2);
	// 双击姓名txt
	tfChsName.onClick(selectName2);
	// 下载事件
	employeeBorrowStore.on('load', function() {
				employeeBorrowStoreX.removeAll();
				for (i = 0; i < employeeBorrowStore.getCount(); i++) {
					var record = employeeBorrowStore.getAt(i).copy();
					employeeBorrowStoreX.add(record);
				}

			});
	// 查询数据
	laborBorrowStore.on('load', function() {
				if (!isFirstLoad) {
					if (laborBorrowStore.getCount() <= 0) {
						Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
								MessageConstants.COM_I_003);
					}
				}

			});
	// 检查员工工号重复
	function checkStore(store) {
		for (i = 0; i < store.getCount(); i++) {
			for (j = i + 1; j < store.getCount(); j++) {
				// 如果有员工工号重复则
				if (store.getAt(i).get('empId') == store.getAt(j).get('empId')) {
					Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
							Constants.PE002_I_002);
					return false;
				}
			}
		}
		return true;
	}
});
