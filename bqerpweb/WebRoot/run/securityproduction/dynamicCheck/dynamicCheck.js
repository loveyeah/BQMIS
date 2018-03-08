Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

//	//-------------------------------------------
    // 附件
    var tfAppend = new Ext.form.TextField({
        id : "xlsFile",
        name : 'xlsFile',
        inputType : 'file',
        width : 200
    });

    // 导入按钮
    var btnInport = new Ext.Toolbar.Button({
        text : '导入',
        handler : uploadQuestFile,
        iconCls : 'upLoad'
    });
    
        // 上传附件
    function uploadQuestFile() {
      var  filePath = tfAppend.getValue();
        // 文件路径为空的情况
        if (filePath == "") {
            Ext.Msg.alert("提示","请选择文件！");
            return;
        } else {
            // 取得后缀名并小写
            var suffix = filePath.substring(filePath.length - 3, filePath.length);
            if (suffix.toLowerCase() != 'xls')
                Ext.Msg.alert("提示", "导入的文件格式必须是Excel格式");
            else {
            	Ext.Msg.wait("正在导入,请等待....");
                headForm.getForm().submit({
                    method : 'POST',
                    url : 'security/importMatterInfo.action',
                    success : function(form, action) {
                        var o = eval("(" + action.response.responseText + ")");
                        Ext.Msg.alert("提示",o.msg);
                          if(o.msg=="导入成功！")
                          {
                          	
                          		queryRecord();
                          		Ext.Msg.hide();
                          }
                      
                    },
                    failture : function() {
                      //  Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                    }
                })
            }
        }
    }
	//-------------------------------------------
	
	var quarterDate = new Ext.form.TextField({
				id : 'strquarterDate',
				name : 'quarterDate',
				fieldLabel : "年份",
				style : 'cursor:pointer',
				cls : 'Wdate',
				width : 90,
				value : new Date().getFullYear(),
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y',
									dateFmt : 'yyyy',
									alwaysUseStartDate : true,
									isShowClear : false
								});
					}
				}
			});

	var quarterBox = new Ext.form.ComboBox({
				fieldLabel : '季度',
				store : [['1', '春查'], ['2', '秋查'], ['3', '安评'],
						['4', '重大危险源'],['5', '技术监控']],
				id : 'quarterBox',
				name : 'quarterBoxName',
				valueField : "value",
				displayField : "text",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'quarterBoxName',
				editable : false,
				triggerAction : 'all',
				width : 85,
				selectOnFocus : true,
				value : 1
			});

	// 定义grid
	var MyRecord = Ext.data.Record.create([{
				name : 'mainId',
				mapping : 0
			}, {
				name : 'year',
				mapping : 1
			}, {
				name : 'season',
				mapping : 2
			}, {
				name : 'detailId',
				mapping : 3
			}, {
				name : 'existQuestion',
				mapping : 4
			}, {
				name : 'wholeStep',
				mapping : 5
			}, {
				name : 'avoidStep',
				mapping : 6
			}, {
				name : 'planDate',
				mapping : 7
			}, {
				name : 'actualDate',
				mapping : 8
			}, {
				name : 'dutyDeptCode',
				mapping : 9
			}, {
				name : 'dutyDeptName',
				mapping : 10
			}, {
				name : 'dutyBy',
				mapping : 11
			}, {
				name : 'dutyName',
				mapping : 12
			}, {
				name : 'superDeptCode',
				mapping : 13
			}, {
				name : 'superDeptName',
				mapping : 14
			}, {
				name : 'superBy',
				mapping : 15
			}, {
				name : 'superName',
				mapping : 16
			}, {
				name : 'noReason',
				mapping : 17
			}, {
				name : 'issueProerty',
				mapping : 18
			}]);

	var dataProxy = new Ext.data.HttpProxy(

	{
				url : 'security/findDynamicCheckList.action'
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

	var sm = new Ext.grid.CheckboxSelectionModel();

	var grid = new Ext.grid.GridPanel({
				region : "center",
				layout : 'fit',
				store : store,

				columns : [sm, new Ext.grid.RowNumberer({
									header : '序号',
									width : 50
								}), {

							header : "dID",
							width : 75,
							sortable : true,
							dataIndex : 'detailId',
							hidden : true
						}, {
							header : "ID",
							width : 75,
							sortable : true,
							hidden : true,
							dataIndex : 'mainId'

						}, {
							header : "存在问题",
							width : 250,
							sortable : true,
							dataIndex : 'existQuestion'
						},

						{
							header : "整改计划",
							width : 250,
							sortable : true,
							dataIndex : 'wholeStep'
						},{
							header : "计划完成时间",
							width : 75,
							sortable : true,
							dataIndex : 'planDate'
						}, {
							header : "整改责任人",
							width : 75,
							sortable : true,
							dataIndex : 'dutyName'
						}, {
							header : "整改责任部门",
							width : 75,
							sortable : true,
							dataIndex : 'dutyDeptName'
						}, {
							header : "整改监督人",
							width : 75,
							sortable : true,
							dataIndex : 'superName'
						},{
							header : "整改监督部门",
							width : 75,
							sortable : true,
							dataIndex : 'superDeptName'
						}, {
							header : "问题性质",
							width : 75,
							sortable : true,
							dataIndex : 'issueProerty',
							renderer : function(v) {
								if (v == '1') {
									return "一般性质";
								}
								else  if (v == '2') {
									return "重大性质";
								}// update by ltong
								else
								{
									return v;
								}
			}
						}, {
							header : "未整改原因",
							width : 75,
							sortable : true,
							hidden : true,
							dataIndex : 'noReason'
						}, {
							header : "整改前防范措施",
							width : 75,
							sortable : true,
							dataIndex : 'avoidStep'
						}],
				sm : sm,
				autoSizeColumns : true,
				viewConfig : {
					forceFit : true
				},
				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : store,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						})
			});
			
			
			
			 var headTbar = new Ext.Toolbar({
        region : 'north',
        border : false,
        items : ['年度：', quarterDate,"-", '检查类别：', quarterBox, '-', {
							text : "查询",
							iconCls : 'query',
							handler : queryRecord
						}, '-', {
							text : "新增",
							iconCls : 'add',
							handler : addRecord
						}, '-', {
							text : "修改",
							iconCls : 'update',
							handler : updateRecord
						}, '-', {
							text : "删除",
							iconCls : 'delete',
							handler : deleteRecord
						},'-',tfAppend,'-',btnInport]
    });
			

    
      var headForm = new Ext.form.FormPanel({
        region : 'north',
        id : 'center-panel',
        height : 28,
        frame : false,
        fileUpload : true,
        labelWidth : 70,
        labelAlign : 'right',
        layout : 'form',
        items : [headTbar]
    });
	grid.on("rowdblclick", updateRecord);
	// ---------------------------------------


			
			    var layout = new Ext.Viewport({
        layout : 'border',
        border : false,
        enableTabScroll : true,
        border : false,
        items : [{
                region : 'north',
                layout : 'fit',
                border : false,
                margins : '0 0 0 0',
                height : 25,
                items : [headForm]
            }, {
                title : "",
                region : 'center',
                layout : 'fit',
                border : false,
                margins : '0 0 0 0',
                split : true,
                collapsible : false,
                items : [grid]
            }]
    });

	// -------------------
	var wd = 240;
	var detailId = new Ext.form.Hidden({
				id : "detailId",
				fieldLabel : 'detailID',
				width : wd,
				readOnly : true,
				value : '',
				name : 'detailModel.detailId'
			});

	var mainId = new Ext.form.Hidden({
				id : "mainId",
				fieldLabel : 'ID',
				anchor : '90%',
				readOnly : true,
				value : '',
				name : 'mainModel.mainId'
			});

	var year = new Ext.form.Hidden({
				id : "year",
				fieldLabel : 'ID',
				width : wd,
				readOnly : true,
				value : '',
				name : 'mainModel.year'
			});

	var season = new Ext.form.Hidden({
				id : "season",
				fieldLabel : 'ID',
				width : wd,
				readOnly : true,
				value : '',
				name : 'mainModel.season'
			});

	var existQuestion = new Ext.form.TextArea({
				id : "existQuestion",
				fieldLabel : '存在问题',
				anchor : '94.5%',
				heigth : 80,
				maxLength : 100,
				name : 'detailModel.existQuestion'
			});

	var wholeStep = new Ext.form.TextArea({
				id : "wholeStep",
				fieldLabel : '整改措施',
				anchor : '94.5%',
				heigth : 80,
				maxLength : 100,
				name : 'detailModel.wholeStep'
			});

	var avoidStep = new Ext.form.TextArea({
				id : "avoidStep",
				fieldLabel : '整改前防范措施',
				anchor : '94.5%',
				heigth : 80,
				maxLength : 100,
				name : 'detailModel.avoidStep'
			});

	var planDate = new Ext.form.DateField({
				id : 'planDate',
				name : 'detailModel.planDate',
				fieldLabel : '计划完成时间',
				anchor : '90%',
				altFormats : 'Y-m-d',
				format : 'Y-m-d',
				readOnly : true,
				value : new Date()
			});

	/**
	 * 人员选择画面处理
	 */
	function selectPersonWin(flag) {
		var args = {
			selectModel : 'signal',
			notIn : "",
			rootNode : {
				id : '-1',
				text : '合肥电厂'
			}
		}
		var person = window
				.showModalDialog(
						'../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(person) != "undefined") {
			Ext.lib.Ajax.request('POST', 'security/getdeptcode.action', {
						success : function(action) {
							var result = eval("(" + action.responseText + ")");
							var arr = new Array()
							arr = result.split(",");
							if (result) {
								var DeptCode = arr[0];
								var DeptName = arr[1];
								if (flag == "1") {
									dutyName.setValue(person.workerName);
									hdnDutyBy.setValue(person.workerCode);
									dutyDeptName.setValue(DeptName);
									hdndutyDept.setValue(DeptCode);

								}
								if (flag == "2") {
									superName.setValue(person.workerName);
									hdnSuperBy.setValue(person.workerCode);
									superDeptName.setValue(DeptName);
									hdnSuperDept.setValue(DeptCode);

								}
							}
						}
					}, 'deptId=' + person.deptId);
			// update by ltong 20100429
		}
	}

	// 整改责任人
	var dutyName = new Ext.form.TriggerField({
				isFormField : true,
				anchor : '90%',
				name : 'dutyName',
				fieldLabel : "整改责任人",
				readOnly : true,
				onTriggerClick : function() {
					selectPersonWin(1);
				}
			});
	// 整改责任人隐藏域
	var hdnDutyBy = new Ext.form.Hidden({
				id : "dutyBy",
				isFormField : true,
				name : "detailModel.dutyBy"
			});

	// 整改责任部门
	var dutyDeptName = new Ext.form.TextField({
				name : 'dutyDeptName',
				anchor : '90%',
				fieldLabel : "整改责任部门",
				readOnly : true
			});
	// 整改责任部门
	var hdndutyDept = new Ext.form.Hidden({
				id : "dutyDeptCode",
				name : "detailModel.dutyDeptCode"
			});

	// 整改监督人
	var superName = new Ext.form.TriggerField({
				isFormField : true,
				anchor : '90%',
				name : 'superName',
				fieldLabel : "整改监督人",
				readOnly : true,
				onTriggerClick : function() {
					selectPersonWin(2);
				}
			});
	// 整改监督人隐藏域
	var hdnSuperBy = new Ext.form.Hidden({
				id : "superBy",
				isFormField : true,
				name : "detailModel.superBy"
			});

	// 整改监督部门
	var superDeptName = new Ext.form.TextField({
				name : 'superDeptName',
				anchor : '90%',
				fieldLabel : "整改监督部门",
				readOnly : true
			});
	// 整改监督部门
	var hdnSuperDept = new Ext.form.Hidden({
				id : "superDeptCode",
				name : "detailModel.superDeptCode"
			});

	// var noReason =new Ext.form.TextArea( {
	// id : "noReason",
	// fieldLabel : '未整改原因',
	// anchor : '90%',
	// heigth:80,
	// maxLength : 100,
	// name : 'detailModel.noReason'
	// });

	var issueProerty = new Ext.form.ComboBox({
				id : "issueProerty",
				fieldLabel : '问题性质',
				store : [['1', '一般性质'], ['2', '重大性质']],
				anchor : '90%',
				name : 'detailModel.issueProerty',
				valueField : "value",
				displayField : "text",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'detailModel.issueProerty',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				value : 1
			});// update by ltong

	var myaddpanel = new Ext.FormPanel({
				frame : true,
				labelAlign : 'center',
				labelWidth : 100,
				closeAction : 'hide',
				layout : "form",
				title : '增加/修改动态检查信息',
				items : [{
							layout : "column",
							border : false,
							items : [{
										columnWidth : 0.2,
										layout : "form",
										border : false,
										items : [detailId]
									}, {
										columnWidth : 0.2,
										layout : "form",
										border : false,
										items : [year]
									}, {
										columnWidth : 0.2,
										layout : "form",
										border : false,
										items : [mainId]
									}, {
										columnWidth : 0.2,
										layout : "form",
										border : false,
										items : [season]
									}]
						}, {
							layout : "column",
							border : false,
							items : [{
										columnWidth : 0.5,
										layout : "form",
										border : false,
										items : [planDate]
									}, {
										columnWidth : 0.5,
										layout : "form",
										border : false,
										items : [issueProerty]
									}]
						}, {
							layout : "column",
							border : false,
							items : [{
										columnWidth : 0.5,
										layout : "form",
										border : false,
										items : [dutyName, hdnDutyBy]
									}, {
										columnWidth : 0.5,
										layout : "form",
										border : false,
										items : [dutyDeptName, hdndutyDept]
									}]
						}, {
							layout : "column",
							border : false,
							items : [{
										columnWidth : 0.5,
										layout : "form",
										border : false,
										items : [superName, hdnSuperBy]
									}, {
										columnWidth : 0.5,
										layout : "form",
										border : false,
										items : [superDeptName, hdnSuperDept]
									}]
						}, {
							layout : "column",
							border : false,
							items : [{
										columnWidth : 1,
										layout : "form",
										border : false,
										items : [existQuestion]
									}]
						}, {
							layout : "column",
							border : false,
							items : [{
										columnWidth : 1,
										layout : "form",
										border : false,
										items : [wholeStep]
									}]
						}, {
							layout : "column",
							border : false,
							items : [{
										columnWidth : 1,
										layout : "form",
										border : false,
										items : [avoidStep]
									}]
						}

				]
			});

	function checkInput() {
		var msgArr = new Array();
		if (existQuestion.getValue() == "") {
			msgArr.push("‘存在问题’")
		}
		if (dutyName.getValue() == "") {
			msgArr.push("‘整改责任人’")
		}
		if (msgArr.length == 0) {
			return true;
		} else {
			Ext.Msg.alert("提示", "请填写：" + msgArr.join(','));
			return false;
		}
	}

	var win = new Ext.Window({
				width : 600,
				height : 400,
				buttonAlign : "center",
				items : [myaddpanel],
				layout : 'fit',
				closeAction : 'hide',
				resizable : false,
				modal : true,
				buttons : [{
					text : '保存',
					iconCls : 'save',
					handler : function() {
						var myurl = "";
						if (detailId.getValue() == "") {
							myurl = "security/saveDynamicCheckInfo.action";
						} else {
							myurl = "security/updateDynamicCheckInfo.action?flag=1";
						}

						if (!checkInput())
							return;
						myaddpanel.getForm().submit({
							method : 'POST',
							url : myurl,
							success : function(form, action) {
								var o = eval("(" + action.response.responseText
										+ ")");
								Ext.Msg.alert("注意", o.msg);
								if (o.msg.indexOf("成功") != -1) {
									queryRecord();
									win.hide();
								}
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
			year : quarterDate.getValue(),
			season : quarterBox.getValue()

		};
		store.load({
					params : {
						start : 0,
						limit : 18
					}
				});
	}

	function addRecord() {
		myaddpanel.getForm().reset();
		win.show();
		myaddpanel.setTitle("增加动态检查信息");
		if (store.getTotalCount() > 0) {
			var obj = store.getAt(0);
			mainId.setValue(obj.get("mainId"));
			year.setValue(obj.get("year"));
			season.setValue(obj.get("season"));

		} else {
			year.setValue(quarterDate.getValue());
			season.setValue(quarterBox.getValue());
		}

	}

	function updateRecord() {
		if (grid.selModel.hasSelection()) {

			var records = grid.getSelectionModel().getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑!");
			} else {
				win.show();
				var record = grid.getSelectionModel().getSelected();
				myaddpanel.getForm().reset();
				myaddpanel.form.loadRecord(record);
				myaddpanel.setTitle("修改动态检查信息");
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}

	function deleteRecord() {
		var records = grid.selModel.getSelections();
		var ids = [];

		if (records.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录!");
		} else {

			for (var i = 0; i < records.length; i += 1) {
				var member = records[i];
				if (member.get("detailId")) {
					ids.push(member.get("detailId"));

				} else {

					store.remove(store.getAt(i));
				}
			}

			Ext.Msg.confirm("删除", "确定要删除所选的记录?", function(buttonobj) {

						if (buttonobj == "yes") {

							Ext.lib.Ajax.request('POST',
									'security/deleteDynamicCheckInfo.action', {
										success : function(action) {
											Ext.Msg.alert("提示", "删除数据成功！")

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

	queryRecord();

});