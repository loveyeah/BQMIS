Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
    Ext.QuickTips.init();
    var empIdUsing = null;
    
    
    // 新增按钮
    var btnAdd = new Ext.Button({
        text : Constants.BTN_ADD,
        iconCls : Constants.CLS_ADD,
        handler : onAdd
    });
    
    // 修改按钮
    var btnModify = new Ext.Button({
        text : Constants.BTN_UPDATE,
        iconCls : Constants.CLS_UPDATE,
        handler : onModify
    });
    
    // 删除按钮
    var btnDelete = new Ext.Button({
        text : Constants.BTN_DELETE,
        iconCls : Constants.CLS_DELETE,
        handler : onDelete
    });
    
    var tfAppend = new Ext.form.TextField({
		name : 'xlsFile',
		inputType : 'file',
		width : 200
	})
	
	

    //模板下载 by ghzhou 2010-07-02
	//----------begin----------
	function downloadMoudle(){
		window.open("../downloadMoudle/学习经历-模板.xls")
	}
	
	var btnDownload = new Ext.Toolbar.Button({
		id : 'btnDownload',
		text : '模板下载',
		handler : downloadMoudle,
		iconCls : 'export'
	});
	//------------end-----------
	
    // 导入按钮
	var btnInport = new Ext.Toolbar.Button({
		id : 'workResume_inport',
		text : '导入',
		handler : uploadQuestFile,
		iconCls : 'upLoad'
	});
	// 上传附件
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
					url : 'hr/importPersonnelFilesInfo.action',
					params : {
						type : 'studyResume'
					},
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("提示", o.msg);
						studyStore.reload();
					},
					failture : function() {
						Ext.Msg.alert(Constants.ERROR, "导入失败！");
					}
				})
			}
		}
	}
    
    // 打印按钮
//    var btnPrint = new Ext.Button({
//        text : '打印员工履历表',
//        iconCls : Constants.CLS_PRINT,
//        handler : onPrint
//    });
//    
    
    var studyRecord = Ext.data.Record.create([{
            // 入学时间
            name : 'enrollmentDate'
        }, {
            // 毕业时间
            name : 'graduateDate'
        }, {
            // 学校编码Id
            name : 'schoolCodeId'
        }, {
            // 学校名称
            name : 'schoolName'
        }, {
            // 学习专业Id
            name : 'specialtyCodeId'
        }, {
            // 学习专业
            name : 'specialtyName'
        }, {
            // 学号
            name : 'studyCode'
        }, {
            // 学历Id
            name : 'educationId'
        }, {
            // 学历
            name : 'educationName'
        }, {
            // 学位Id
            name : 'degreeId'
        }, {
            // 学位
            name : 'degreeName'
        }, {
            // 语种编码Id
            name : 'languageCodeId'
        }, {
            // 语种
            name : 'languageName'
        }, {
            // 学习类别编码Id
            name : 'studyTypeCodeId'
        }, {
            // 学习类别
            name : 'studyType'
        }, {
            // 学制(年)
            name : 'studyLimit'
        }, {
            // 是否毕业
            name : 'ifGraduate'
        }, {
            // 培训费用
            name : 'studyMoney'
        }, {
            // 教育结果
            name : 'educationResult'
        }, {
            // 证书号码
            name : 'certificateCode'
        }, {
            // 是否原始学历
            name : 'ifOriginality'
        }, {
            // 是否最高学历
            name : 'ifHightestXl'
        }, {
            // 备注
            name : 'memo'
        }, {
            // 学历教育Id
            name : 'educationid'
        }, {
            // 上次修改日期
            name : 'lastModifiedDate'
    },{
    	// 专业类别
    	name : 'category'
    },{
       //毕业院校名称 add by sychen 20100709
    	name:'graduateSchool'
    },{
       //所学专业名称 add by sychen 20100709
    	name:'specialty'
    },{
       //人员编码 add by sychen 20100713
    	name:'newEmpCode'
    }]);
    var sm = new Ext.grid.CheckboxSelectionModel({
    	singleSelect : false
    })
    // grid列模式
    var studyCM = new Ext.grid.ColumnModel([sm,
        new Ext.grid.RowNumberer({
            header : '行号',
            width : 35
        }), {
            header : '入学时间',
            width : 80,
            dataIndex : 'enrollmentDate'
        }, {
            header : '毕业时间',
            width : 80,
            dataIndex : 'graduateDate'
        }, {
            header : '毕业院校',
            width : 100,
//            dataIndex : 'schoolName' // update by sychen 20100709
            dataIndex : 'graduateSchool'
        }, {
            header : '所学专业',
            width : 80,
//            dataIndex : 'specialtyName'  // update by sychen 20100709
            dataIndex : 'specialty'
        }, {
            header : '学号',
            width : 80,
            dataIndex : 'studyCode'
        }, {
            header : '学历',
            width : 50,
            dataIndex : 'educationName'
        }, {
            header : '学位',
            width : 50,
            dataIndex : 'degreeName'
        }, {
            header : '语种',
            width : 40,
            dataIndex : 'languageName'
        }, {
            header : '学习方式',
            width : 60,
            dataIndex : 'studyType'
        }, {
            header : '专业类别',
            width : 60,
            dataIndex : 'category'
        }, {
            header : '学制(年)',
            width : 60,
            dataIndex : 'studyLimit'
        }, {
            header : '是否毕业',
            width : 60,
            renderer : renderYesNo,
            dataIndex : 'ifGraduate'
        }, {
            header : '培训费用',
            width : 80,
            align : 'right',
            renderer : renderNumber,
            dataIndex : 'studyMoney'
        }, {
            header : '教育状态',
            width : 80,
            renderer : renderEduResult,
            dataIndex : 'educationResult'
        }, {
            header : '毕业证编号',
            width : 80,
            dataIndex : 'certificateCode'
        }, {
            header : '是否原始学历',
            width : 80,
            renderer : renderYesNo,
            dataIndex : 'ifOriginality'
        }, {
            header : '是否最高学历',
            width : 80,
            renderer : renderYesNo,
            dataIndex : 'ifHightestXl'
        }, {
            header : '备注',
            width : 150,
            dataIndex : 'memo'
    }]);
    studyCM.defaultSortable = true;
    
    
    // 数据源
    var studyStore = new Ext.data.JsonStore({
        url : 'hr/getEmpStudyResumeInfo.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : studyRecord
    });
    
    // 分页工具栏
    var pagebar = new Ext.PagingToolbar({
        pageSize : Constants.PAGE_SIZE,
        store : studyStore,
        displayInfo : true,
        displayMsg : Constants.DISPLAY_MSG,
        emptyMsg : Constants.EMPTY_MSG
    })
    var tbar = new Ext.Toolbar({
    	items : [btnAdd, btnModify, btnDelete,tfAppend,btnInport,btnDownload/*, '->', btnPrint*/
    	  // add by sychen 20100712
    	                  ,{
							text : "导出",
							id : 'btnreport',
							iconCls : 'export',
							handler :function(){
			Ext.Ajax.request({
				url : 'hr/getEmpStudyResumeInfo.action',
				params : {
				empId : empIdUsing,
				start : 0,
                limit : Constants.PAGE_SIZE
			},
				method : 'post',
				success : function(response,options){
				var res = Ext.decode(response.responseText).list;
				if(studyStore.getTotalCount()==0)
				{
					 Ext.Msg.alert('提示', '无数据进行导出！');
			         return;
				}else
				{
			  if(res){
				Ext.Msg.confirm('提示', '确认要导出数据？', function(id) {
			    if (id == 'yes') {
				var tableHeader = "<table border=1><tr><th>人员编码</th><th>学历 </th><th>毕业院校</th><th>所学专业</th>" +
						                    "<th>专业类别</th><th>学习方式</th><th>入学时间</th><th>毕业时间</th><th>教育状态</th>" +
						                    "<th>毕业证编号</th><th>学位</th> </tr>";
				var html = [tableHeader];
					for (var i = 0; i <res.length; i++) {
					var rec = res[i];
   	        	    html.push('<tr><td align=left >' + (rec.newEmpCode==null?"":rec.newEmpCode) +'</td>' +
   	        	                          '<td align=left>'+( rec.educationName==null?"":rec.educationName)+'</td>' +
   	        	                          '<td align=left>'+ (rec.graduateSchool==null?"":rec.graduateSchool )+ '</td>' +
   	        	                          '<td align=left>'+ (rec.specialty==null?"":rec.specialty)+ '</td>' +
   	        	                          '<td align=left>'+( rec.category==null?"":rec.category)+  '</td>' +
   	        	                          '<td align=left>'+( rec.studyType==null?"":rec.studyType)+'</td>' +
   	        	                          '<td align=left>'+ (rec.enrollmentDate==null?"":rec.enrollmentDate )+ '</td>' +
   	        	                          '<td align=left>'+ (rec.graduateDate==null?"":rec.graduateDate)+ '</td>' +
   	        	                          '<td align=left>'+( rec.educationResult==null?"":renderEduResult(rec.educationResult))+  '</td>' +
   	        	                          '<td align=left>'+( rec.certificateCode==null?"":rec.certificateCode)+'</td>' +
   	        	                          '<td align=left>'+( rec.degreeName==null?"":rec.degreeName)+ '</td></tr>')
				}
				html.push('</table>');
				html = html.join(''); // 最后生成的HTML表格
				tableToExcel(html);
			}
		})
			  }else
						{
						      Ext.Msg.alert('提示', '无数据进行导出！');
			                  return;
						}
					
				}},
				failure : function(response,options){
					
							Ext.Msg.alert('提示',"导出失败！");
					}
			})
		  }
		}
    // add by sychen 20100712  end
    	
    	]
    })
    
           function tableToExcel(tableHTML) {
		window.clipboardData.setData("Text", tableHTML);
		try {
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExWSh.Columns("A").ColumnWidth  = 13;
			ExWSh.Columns("B").ColumnWidth  = 13;
			ExWSh.Columns("C").ColumnWidth  = 13;
			ExWSh.Columns("D").ColumnWidth  = 13;
			ExWSh.Columns("E").ColumnWidth  = 13;
			ExWSh.Columns("F").ColumnWidth  = 13;
			ExWSh.Columns("G").ColumnWidth  = 13;
			ExWSh.Columns("H").ColumnWidth  = 13;
			ExWSh.Columns("I").ColumnWidth  = 13;
			ExWSh.Columns("J").ColumnWidth  = 13;
			ExWSh.Columns("K").ColumnWidth  = 13;
			ExApp.DisplayAlerts = false;
			ExApp.visible = true;
		} catch (e) {
			if (e.number != -2146827859)
				alert("您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}
    var headForm = new Ext.form.FormPanel({
		region : 'north',
		id : 'center-panel',
		height : 28,
		frame : false,
		fileUpload : true,
		layout : 'form',
		items : [tbar]
	});
    // 学习简历Grid
    var studyGrid = new Ext.grid.GridPanel({
        store : studyStore,
        sm : sm,
        cm : studyCM,
        // 分页
        bbar : pagebar,
        tbar : headForm,
        border : false,
        enableColumnMove : false
    });
    // 双击处理
    studyGrid.on('rowdblclick', onModify);
    
    // 设定布局器及面板
    var layout = new Ext.Viewport({
        layout : "fit",
        items : [studyGrid]
    });
    
    
    // ==============    定义弹出画面    ===========
    Ext.form.TextField.prototype.width = 105;
    Ext.form.CmbHRBussiness.prototype.width = 105;
    Ext.form.CmbHRCode.prototype.width = 105;
    var twoWd = 294;
    
    // 员工姓名
    var tfChsName = new Ext.form.TextField({
        id : 'chsName',
        fieldLabel : "员工姓名",
        readOnly : true
    });
    
    // 学校名称
    //update by sychen 20100709
        var tfGraduateSchool = new Ext.form.TextField({
        id : 'graduateSchool',
        name : 'studyResume.graduateSchool',
        fieldLabel : "毕业院校<font color='red'>*</font>",
        allowBlank : false,
        width : twoWd
    });
    
//    var cbSchoolCodeId = new Ext.form.CmbHRBussiness({
//        id : "schoolCodeId",
//        hiddenName : 'studyResume.schoolCodeId',
//        fieldLabel : "毕业院校<font color='red'>*</font>",
//        allowBlank : false,
//        selectOnFocus : true,
//        type : '学校',
//        width : twoWd
//    });
    
    // 入学时间
    var tfEnrollmentDate = new Ext.form.TextField({
        id : 'enrollmentDate',
        name : 'studyResume.enrollmentDate',
        style : 'cursor:pointer',
        fieldLabel : "入学时间<font color='red'>*</font>",
        allowBlank : false,
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    isShowClear : false,
                    onpicked : function(){
                        tfEnrollmentDate.clearInvalid();
                    },
                    onclearing:function(){
                        tfEnrollmentDate.markInvalid();
                    }
                });
            }
        }
    });
    
    // 学习专业
    //update by sychen 20100709
    var tfSpecialty = new Ext.form.TextField({
        id : 'specialty',
        name : 'studyResume.specialty',
        fieldLabel : "所学专业",
        allowBlank : true
    });
//    var cbSpecialtyCodeId = new Ext.form.CmbHRBussiness({
//        id : "specialtyCodeId",
//        hiddenName : 'studyResume.specialtyCodeId',
//        fieldLabel : '所学专业',
//        selectOnFocus : true,
//        type : '学习专业'
//    });
    
    // 毕业时间
    var tfGraduateDate = new Ext.form.TextField({
        id : 'graduateDate',
        name : 'studyResume.graduateDate',
        style : 'cursor:pointer',
        fieldLabel : "毕业时间<font color='red'>*</font>",
        allowBlank : false,
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    isShowClear : false,
                    onpicked : function(){
                        tfGraduateDate.clearInvalid();
                    },
                    onclearing:function(){
                        tfGraduateDate.markInvalid();
                    }
                });
            }
        }
    });
    
    // 学历
    var cbEducationId = new Ext.form.CmbHRBussiness({
        id : "educationId",
        hiddenName : 'studyResume.educationId',
        fieldLabel : "学历",
        selectOnFocus : true,
        type : '学历'
    });
    
    // 学位
    var cbDegreeId = new Ext.form.CmbHRBussiness({
        id : "degreeId",
        hiddenName : 'studyResume.degreeId',
        fieldLabel : "学位",
        selectOnFocus : true,
        type : '学位'
    });
    
    // 语种
    var cbLanguageCodeId = new Ext.form.CmbHRBussiness({
        id : "languageCodeId",
        hiddenName : 'studyResume.languageCodeId',
        fieldLabel : '语种',
        selectOnFocus : true,
        type : '语种'
    });
    
    // 学习类别
    var cbStudyTypeCodeId = new Ext.form.CmbHRBussiness({
        id : "studyTypeCodeId",
        hiddenName : 'studyResume.studyTypeCodeId',
        fieldLabel : '学习方式',
        selectOnFocus : true,
        type : '学习类别'
    });
    
     // 专业类别
    var tfCategory = new Ext.form.TextField({
        id : "category",
        name : 'studyResume.category',
        fieldLabel : '专业类别'
    });
    
    // 学制（年）
    var cbStudyLimit = new Ext.form.TextField({
        id : "studyLimit",
        name : 'studyResume.studyLimit',
        fieldLabel : '学制（年）',
        maxLength : 8
    });
    
    // 学号
    var tfStudyCode = new Ext.form.TextField({
        id : 'studyCode',
        name : 'studyResume.studyCode',
        fieldLabel : "学号",
        maxLength : 10,
        codeField : 'yes',
        style: {
            'ime-mode' : 'disabled'
        }
    });
    
    // 教育结果
    var cbEducationResult = new Ext.form.CmbHRCode({
        id : "educationResult",
        hiddenName : 'studyResume.educationResult',
        fieldLabel : '教育状态',
        selectOnFocus : true,
        type : '教育结果'
    });
    
    // 培训费用
    var cbStudyMoney = new Powererp.form.NumField({
        id : "studyMoney",
        name : 'studyResume.studyMoney',
        fieldLabel : '培训费用',
        style : 'text-align:right',
        maxLength: 16,
        allowNegative : false,
        decimalPrecision : 4,
        padding : 4
    });
    
    // 是否毕业
    var raIfGraduate = {
        id : 'ifGraduate',
        layout : 'column',
        isFormField : true,
        fieldLabel : '是否毕业',
        border : false,
        items : [{
            columnWidth : .4,
            border : false,
            items : new Ext.form.Radio({
                id : 'ifGraduate1',
                boxLabel : '是',
                name : 'studyResume.ifGraduate',
                inputValue : '1',
                checked : true
            })
        }, {

            columnWidth : .4,
            items : new Ext.form.Radio({
                id : 'ifGraduate2',
                boxLabel : '否',
                name : 'studyResume.ifGraduate',
                inputValue : '0'
            })
        }]
    };
    
    // 证书号码
    var tfCertificateCode = new Ext.form.TextField({
        id : 'certificateCode',
        name : 'studyResume.certificateCode',
        fieldLabel : "毕业证编号",
        maxLength : 20,
        width : twoWd,
        codeField : 'yes',
        style: {
            'ime-mode' : 'disabled'
        }
    });
    
    // 原始学历
    var raIfOriginality = {
        id : 'ifOriginality',
        layout : 'column',
        isFormField : true,
        fieldLabel : '原始学历',
        border : false,
        items : [{
            columnWidth : .4,
            border : false,
            items : new Ext.form.Radio({
                id : 'ifOriginality1',
                boxLabel : '是',
                name : 'studyResume.ifOriginality',
                inputValue : '1',
                checked : true
            })
        }, {

            columnWidth : .4,
            items : new Ext.form.Radio({
                id : 'ifOriginality2',
                boxLabel : '否',
                name : 'studyResume.ifOriginality',
                inputValue : '0'
            })
        }]
    };
    
    // 最高学历
    var raIfHightestXl = {
        id : 'ifHightestXl',
        layout : 'column',
        isFormField : true,
        fieldLabel : '最高学历',
        border : false,
        items : [{
            columnWidth : .4,
            border : false,
            items : new Ext.form.Radio({
                id : 'ifHightestXl1',
                boxLabel : '是',
                name : 'studyResume.ifHightestXl',
                inputValue : '1',
                checked : true
            })
        }, {

            columnWidth : .4,
            items : new Ext.form.Radio({
                id : 'ifHightestXl2',
                boxLabel : '否',
                name : 'studyResume.ifHightestXl',
                inputValue : '0'
            })
        }]
    };
    
    // 备注
    var tfMemo = new Ext.form.TextArea({
        id : 'memo',
        name : 'studyResume.memo',
        fieldLabel : "备注",
        maxLength : 128,
        width : twoWd - 2
    });
    
    // 学习简历ID
    var hideStudyResumeId = new Ext.form.Hidden({
        id : 'educationid',
        name : 'studyResume.educationid'
    });
    
    // 上次修改时间
    var hideLastModifiedDate = new Ext.form.Hidden({
        id : 'lastModifiedDate',
        name : 'studyResume.lastModifiedDate'
    });
    
    // 保存按钮
    var btnSave = new Ext.Button({
        text : Constants.BTN_SAVE,
        iconCls : Constants.CLS_SAVE,
        handler : onSave
    });
    
    // 取消按钮
    var btnCancel = new Ext.Button({
        text : Constants.BTN_CANCEL,
        iconCls : Constants.CLS_CANCEL,
        handler : function() {
            Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_005, function(button, text) {
                if (button == "yes") {
                    win.hide();
                }
            });
        }
    });
    
    var panel1 = new Ext.Panel({
        layout : "form",
        items : [
            {
                layout : "column",
                items : [{
                        columnWidth : 0.33,
                        layout : "form",
                        items : [tfChsName]
                    }, {
                        columnWidth : 0.67,
                        layout : "form",
                        items : [tfGraduateSchool]
//                        items : [cbSchoolCodeId]
                    }]
            }, {
                layout : "column",
                items : [{
                        columnWidth : 0.33,
                        layout : "form",
                        items : [tfEnrollmentDate]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        items : [tfSpecialty]
//                        items : [cbSpecialtyCodeId]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        items : [tfCategory]
                    }]
            }]
    });
    
    var panel2 = new Ext.Panel({
        layout : "form",
        items : [
            {
                layout : "column",
                items : [{
                        columnWidth : 0.33,
                        layout : "form",
                        items : [tfGraduateDate]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        items : [cbEducationId]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        items : [cbDegreeId]
                    }]
            }, {
                layout : "column",
                items : [{
                        columnWidth : 0.33,
                        layout : "form",
                        items : [cbLanguageCodeId]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        items : [cbStudyTypeCodeId]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        items : [cbStudyLimit]
                    }]
            }, {
                layout : "column",
                items : [{
                        columnWidth : 0.33,
                        layout : "form",
                        items : [tfStudyCode]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        items : [cbEducationResult]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        items : [cbStudyMoney]
                    }]
            }]
    });
    
    var panel3 = new Ext.Panel({
        layout : "form",
        items : [
            {
                layout : "column",
                items : [{
                        columnWidth : 0.33,
                        layout : "form",
                        items : [raIfGraduate]
                    }, {
                        columnWidth : 0.67,
                        layout : "form",
                        items : [tfCertificateCode]
                    }]
            }, {
                layout : "column",
                items : [{
                        columnWidth : 0.33,
                        layout : "form",
                        items : [raIfOriginality, raIfHightestXl]
                    }, {
                        columnWidth : 0.67,
                        layout : "form",
                        items : [tfMemo]
                    }]
            }]
    });
    
    var formPanel = new Ext.FormPanel({
        labelAlign : 'right',
        labelWidth : 70,
        frame : true,
        border : false,
        items : [panel1, panel2, panel3, 
            hideStudyResumeId, hideLastModifiedDate
        ]
    });
    
    // 定义弹出窗体
    var win = new Ext.Window({
        modal : true,
        resizable : false,
        width : 600,
        height : 300,
        layout : 'fit',
        buttonAlign : "center",
        closeAction : 'hide',
        items : [formPanel],
        buttons : [btnSave, btnCancel]
    });
    
    var pageFields = ['schoolCodeId',
        	'enrollmentDate',
        	'specialtyCodeId',
        	'graduateDate',
        	'educationId',
        	'degreeId',
        	'languageCodeId',
        	'studyTypeCodeId',
        	'studyLimit',
        	'studyCode',
        	'educationResult',
        	'studyMoney',
        	'certificateCode',
        	'ifGraduate',
        	'ifOriginality',
        	'ifHightestXl',
        	'memo'];
    // 检查画面是否变更
    function checkPageChanged() {
    	var isAdd = !hideStudyResumeId.getValue();
    	var record = isAdd ? {} : studyGrid.getSelectionModel().getSelected().data;
    	
    	var pageDatas = formPanel.getForm().getValues();
    	var origialV, currentV, prop;
    	for (var i = 0; i < pageFields.length; i++) {
    		prop = pageFields[i];
    		origialV = isAdd ? '' : record[prop];
    		currentV = pageDatas[prop] || pageDatas['studyResume.' + prop];
    		if (origialV === 'undefined'
                || origialV === 'null'
                || origialV == null) {
                origialV = "";
            }
            if (!origialV && (
            	prop === 'ifGraduate'
                	|| prop === 'ifOriginality'
                	|| prop === 'ifHightestXl')) {
        		origialV = "1";
        	}
    		if (currentV === 'undefined'
                || currentV === 'null'
                || currentV == null) {
                currentV = "";
            }
            // 培训费用
            if (prop === 'studyMoney') {
            	// 删除逗号
            	currentV = String(currentV).replace(/,/g, '');
            	if (currentV.length > 0) {
            		currentV = Number(currentV);
            	}
        	}
                    
    		if (origialV != currentV) {
    			return true;
    		}
    	}
    	return false;
    }
    
    function renderYesNo(value) {
        if (value == '1') {
            return '是';
        } else if (value == '0') {
            return '否';
        }
        return '';
    }
    
    function renderNumber(v, argDecimal) {
		if (v) {
			if (typeof argDecimal != 'number') {
				argDecimal = 4;
			}
			v = Number(v).toFixed(argDecimal);
			var t = '';
			v = String(v);
			while ((t = v.replace(/^(-?[0-9]+)([0-9]{3}.*)$/, '$1,$2')) !== v)
				v = t;
			
			return v;
		} else
			return '';
	}
	
    // 教育结果
    function renderEduResult(value) {
        if (value == '1') {
            return '毕业';
        } else if (value == '2') {
            return '结业';
        } else if (value == '3') {
            return '肆业';
        } else if (value == '4') {
            return '其他';
        }
        return '';
    }
    
    // 显示时间比较方法
    function compareDate(argDate1, argDate2) {
        return argDate1.getTime() < argDate2.getTime();
    }
    
    // textField显示时间比较方法
    function compareDateStr(argDateStr1, argDateStr2){
        var date1 = Date.parseDate(argDateStr1, 'Y-m-d');
        var date2 = Date.parseDate(argDateStr2, 'Y-m-d');
        return compareDate(date1, date2);
    }
    
    // 加载员工学习简历信息 
    function loadEmpStudyInfo(empId) {
    	// 隐藏弹出画面
        studyStore.baseParams = {
            empId : empId
        };
        
        studyStore.load({
            params:{
                start : 0,
                limit : Constants.PAGE_SIZE
            }
        });
        
    }
    
    // 重新加载Grid
    function reloadGrid(options) {
        studyStore.reload(options);
    }
    
    // 新增按钮处理
    function onAdd() {
        formPanel.getForm().reset();
        // 新增学习简历
        win.setTitle('新增学习简历');
        win.show();
        win.center();
    }
    
    // 修改按钮处理
    function onModify() {
        if (btnModify.hidden) {
            return;
        }
        if (!studyGrid.getSelectionModel().hasSelection()) {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_E_016);
            return;
        }
        if (studyGrid.getSelectionModel().getSelections().length > 1) {
            Ext.Msg.alert(Constants.REMIND, "请选择其中一条!");
            return;
        }
        
        formPanel.getForm().reset();
        
        // 修改学习简历
        win.setTitle('修改学习简历');
        win.show();
        win.center();
        
        // 获得选中的记录
        var record = studyGrid.getSelectionModel().getSelected();
        formPanel.getForm().loadRecord(record);
        // 学校名称
        tfGraduateSchool.setValue(record.get('graduateSchool'), true);
//        cbSchoolCodeId.setValue(record.get('schoolCodeId'), true);
        // 学习专业
        tfSpecialty.setValue(record.get('specialty'), true);
//        cbSpecialtyCodeId.setValue(record.get('specialtyCodeId'), true);
        // 学历
        cbEducationId.setValue(record.get('educationId'), true);
        // 学位
        cbDegreeId.setValue(record.get('degreeId'), true);
        // 语种
        cbLanguageCodeId.setValue(record.get('languageCodeId'), true);
        // 学习类别
        cbStudyTypeCodeId.setValue(record.get('studyTypeCodeId'), true);
        // 教育结果
        cbEducationResult.setValue(record.get('educationResult'), true);
        
        // 是否毕业
        if (record.get('ifGraduate') == '0') {
            Ext.getCmp('ifGraduate2').setValue(true);
        }
        // 是否原始学历
        if (record.get('ifOriginality') == '0') {
            Ext.getCmp('ifOriginality2').setValue(true);
        }
        // 是否最高学历
        if (record.get('ifHightestXl') == '0') {
            Ext.getCmp('ifHightestXl2').setValue(true);
        }
    }
    
    // 删除按钮处理
    function onDelete() {
        if (!studyGrid.getSelectionModel().hasSelection()) {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_E_016);
            return;
        }
        
        Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_002,
            function(buttonobj) {
                if (buttonobj == "yes") {
                    // 获得选中的记录
                    var records = studyGrid.getSelectionModel().getSelections();
                    var ids = new Array();
                    for(var i = 0; i < records.length; i++){
                    	ids.push(records[i].get("educationid"))
                    }
                    // 删除数据
                    Ext.Ajax.request({
                        method : Constants.POST,
                        url : 'hr/deleteMutilEmpWorkresumeInfo.action',
                        params : {
                        	ids : ids.join(","),
                        	type : 'studyResume'
                            },
                        success : function(result, request) {
                            var o = eval('(' + result.responseText + ')');
                            
                            // 排他异常
                            if(o.msg == "U") {
                                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_015);
                                return;
                            }
                            // 数据库异常
                            if(o.msg == "SQL") {
                                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                return;
                            }
                            
                            // 重新加载Grid
                            reloadGrid({
					            params:{
					                start : 0,
					                limit : Constants.PAGE_SIZE
					            }
					        });
                            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_005);
                        },
                        failure : function() {
                            Ext.Msg.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
                        }
                    });
                }
            }
        );    
    }
    
    // 打印按钮处理
//    function onPrint() {
////        employee.print();
//    	alert('打印')
//    }
    
    // Check处理
    function checkFields() {
        var msg = '';
        if (!tfGraduateSchool.getValue() && tfGraduateSchool.getValue() != '0') {
//        if (!cbSchoolCodeId.getValue() && cbSchoolCodeId.getValue() != '0') {
            msg += String.format(Constants.COM_E_003, "毕业院校") + '<br/>';
        }
        if (tfEnrollmentDate.getValue() === '') {
            msg += String.format(Constants.COM_E_002, "入学时间") + '<br/>';
        }
        if (tfGraduateDate.getValue() === '') {
            msg += String.format(Constants.COM_E_002, "毕业时间") + '<br/>';
        }
        
        if (msg.length > 0) {
            Ext.Msg.alert(Constants.ERROR, msg.replace(/<br\/>$/, ''));
            return false;
        }
        return true;
    }
    
    // 关联Check处理
    function checkRefFields() {
    	var msg = '';
    	if (tfEnrollmentDate.getValue() !== '') {
            if (!compareDateStr(tfEnrollmentDate.getValue(), tfGraduateDate.getValue())) {
                msg += String.format(Constants.COM_E_006, "入学时间", "毕业时间") + '<br/>';
            }
        }
        
        if (msg.length > 0) {
            Ext.Msg.alert(Constants.ERROR, msg.replace(/<br\/>$/, ''));
            return false;
        }
        return true;
    }
    
    // 提交前设置Combo的值，保证其以Number类型传回服务端
    function setComboValue() {
        // 学校名称
        if (!tfGraduateSchool.getValue()) {
            tfGraduateSchool.setValue('');
        }
//        if (!cbSchoolCodeId.getValue()) {
//            cbSchoolCodeId.setValue('');
//        }
        // 学习专业
        if (!tfSpecialty.getValue()) {
            tfSpecialty.setValue('');
        }
//        if (!cbSpecialtyCodeId.getValue()) {
//            cbSpecialtyCodeId.setValue('');
//        }
        // 学历
        if (!cbEducationId.getValue()) {
            cbEducationId.setValue('');
        }
        // 学位
        if (!cbDegreeId.getValue()) {
            cbDegreeId.setValue('');
        }
        // 语种
        if (!cbLanguageCodeId.getValue()) {
            cbLanguageCodeId.setValue('');
        }
        // 学习类别
        if (!cbStudyTypeCodeId.getValue()) {
            cbStudyTypeCodeId.setValue('');
        }
        // 教育结果
        if (!cbEducationResult.getValue()) {
            cbEducationResult.setValue('');
        }
    }
    
    // 保存按钮处理
    function onSave() {
        if (!checkFields()) {
            return;
        }
       
        Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_001,
            function(buttonobj) {
                if (buttonobj == "yes") {
                	if (!checkRefFields()) {
			            return;
			        }
			        
                    var isAddFlag = !hideStudyResumeId.getValue();
                    // 提交前设置Combo的值
                    setComboValue();
                    
                    // 保存数据
                    formPanel.getForm().submit({
                        method : Constants.POST,
                        url : 'hr/saveEmpStudyResumeInfo.action',
                        params : {
                        	// 待处理
                            'emp.empId' : empIdUsing,
                            'isAdd' : isAddFlag
                            },
                        success : function(form, action) {
                            var o = eval('(' + action.response.responseText + ')');
                            
                            // 排他异常
                            if(o.msg == "U") {
                                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_015);
                                return;
                            }
                            // 数据库异常
                            if(o.msg == "SQL") {
                                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                return;
                            }
                            
                            // 重新加载Grid
                            reloadGrid();
                            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004, function() {
                                win.hide();
                            });
                        }
                    });
                }
            }
        );
    }
    
    // 员工姓名不可用
    tfChsName.setDisabled(true);
    
    if(parent.empParam){
    	empIdUsing = parent.empParam.data.empId;
    	tfChsName.setValue(parent.empParam.data.chsName)
    	 loadEmpStudyInfo(empIdUsing);
    	 headForm.setDisabled(false);
    }else{
    	headForm.setDisabled(true);
    }
});
