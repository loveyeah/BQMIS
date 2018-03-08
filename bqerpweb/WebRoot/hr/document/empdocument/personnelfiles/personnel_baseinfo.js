Ext.namespace("Personnel")
Personnel.BaseInfo = function(config) {
	Ext.form.TextField.prototype.anchor = '95%'
	var empIdUsing = null;
	// ************基础信息 开始********************************************
	var modiBtu = new Ext.Toolbar.Button({
		id : 'modiBtu',
		iconCls : 'update',
		text : '修改',
		handler : function() {

			// alert('modified')
			setEnable(true);
			// 修改按钮
			modiBtu.setDisabled(true);
		}
	})
	var saveBtu = new Ext.Toolbar.Button({
		id : 'saveBtu',
		iconCls : 'save',
		text : '保存',
		handler : onSave
	})
	// 打印按钮
//    var btnPrint = new Ext.Button({
//        text : '打印员工履历表',
//        iconCls : Constants.CLS_PRINT,
//        handler : function(){
//        	if(empIdUsing)
//        	 	window.open("/power/report/webfile/hr/employeeRecord.jsp?empId=" + empIdUsing);
//        	 else{
//        	 	Ext.Msg.alert('提示','请先选择人员!')
//        	 }
//        }
//    });
	var tfAppend = new Ext.form.TextField({
		id : 'xlsFile',
		name : 'xlsFile',
		inputType : 'file',
		width : 200
	})
	
	//模板下载 by ghzhou 2010-07-02
	//----------begin----------
	function downloadMoudle(){
		window.open("./downloadMoudle/人员基本信息导入模板.xls")
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
		id : 'btuInport',
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
						type : 'baseInfo'
					},
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("提示", o.msg);
						// if(o.msg=="导入成功！")
						// {
						//                          	
						// // queryRecord();
						// Ext.Msg.hide();
						// }

					},
					failture : function() {
						Ext.Msg.alert(Constants.ERROR, "导入失败！");
					}
				})
			}
		}
	}
	function tableToExcel(tableHTML) {
		window.clipboardData.setData("Text", tableHTML);
		try {
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExApp.DisplayAlerts = false;
			ExApp.visible = true;
		} catch (e) {
			if (e.number != -2146827859)
				alert("您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}
	var isPhoto="";
	function getPhoto()
	{
		 Ext.Ajax.request({
			method : Constants.POST,
			url :  "hr/getPhotoInfo.action?empId="
            + empIdUsing + "&time=" + new Date().getTime(),
			params : {
//				empId : empId
			},
			success : function(result, request) {
				var record = eval('(' + result.responseText + ')');
				if(record!=""&&record.msg=="成功")
				{
				isPhoto="有";
				}else
				{
					isPhoto="无";
				}
				
			},
			failure : function() {
				
			}
		}); 
	}
     
	var exportBtu = new Ext.Toolbar.Button({
		id : 'exportBtu',
		text : '导出',
		iconCls : 'export',
		handler : function() {
			Ext.Ajax.request({
				url : 'hr/getEmpMaintBaseInfo.action',
				params : {
				empId : empIdUsing
			},
				method : 'post',
				success : function(response,options){
				var res = Ext.decode(response.responseText)
				if(empIdUsing==null||empIdUsing=="")
				{
					 Ext.Msg.alert('提示', '无数据进行导出！');
			                  return;
				}else
				{
			  if(res){
				Ext.Msg.confirm('提示', '确认要导出数据？', function(id) {
			    if (id == 'yes') {
				var tableHeader = "<table border=1><tr><th colspan = 11>人字档案信息基本信息导出</th></tr>";
				var html = [tableHeader];
				html
						.push("<tr><th>姓名</th><th>人员编码 </th><th>内部编号</th><th>部门名称</th><th>岗位名称</th><th>出生日期</th>" +
								"<th>年龄 </th><th>性别</th><th>民族</th><th >员工身份</th><th >是否有照片</th><th >政治面貌</th>" +
								"<th>是否主业 </th><th>籍贯</th><th>自定义编码</th><th >户籍地址</th><th >户口</th><th >婚姻状况</th>" +
								"<th>国籍 </th><th>血型</th><th>现有文化程度</th><th >本人成分</th><th >入党时间</th><th >入党转正时间</th></tr>")
					for (var i = 0; i <1; i++) {
					var rec = res;
   		html.push('<tr><td align=left >' + (rec.chsName==null?"":rec.chsName) + '</td><td align=left>'
							+( rec.newEmpCode==null?"":rec.newEmpCode)+ '&nbsp;</td><td align=right>'
							+(( rec.newEmpCode==null?"":rec.newEmpCode).substr(6,6))+ '&nbsp;</td><td align=left>'
							+ (rec.deptName==null?"":rec.deptName )+ '</td><td align=left>'
							+ (rec.stationName==null?"":rec.stationName)+ '</td><td align=left>'
							+( rec.brithday==null?"":rec.brithday )+ '</td><td align=left>'
							+( rec.brithday==null?"": getage(rec.brithday) ) + '</td><td align=left>'
							+ (rec.sex==null?"":getSex(rec.sex)) + '</td><td align=left>'
							+ (rec.nationName==null?"":rec.nationName ) + '</td><td align=left>' 
							+ (rec.workName==null?"": rec.workName )+ '</td><td align=left>' 	
							+ (isPhoto)+ '</td><td align=left>'
							+ (rec.politicsName==null?"":rec.politicsName) + '</td><td align=left>'
							+ (rec.isMainWork==null?"":getisMain(rec.isMainWork))+ '</td><td align=left>'
							+ (rec.nativePlaceName==null?"":rec.nativePlaceName) + '</td><td align=left>'
							+ (rec.empCode==null?"":rec.empCode) + '</td><td align=left>' 
							+ (rec.familyAddress==null?"":rec.familyAddress) + '</td><td align=left>' 
							
							+ (rec.household==null?"":getHouse(rec.household))+ '</td><td align=left>'
							+ (rec.isWedded==null?"":getisWed(rec.isWedded) ) + '</td><td align=left>'
							+ (rec.country==null?"":rec.country)+ '</td><td align=left>'
							+ (rec.bloodType==null?"":rec.bloodType) + '</td><td align=left>'
							+ (rec.educationName==null?"":rec.educationName) + '</td><td align=left>' 
							+ (rec.component==null?"":getCompent(rec.component) )+ '</td><td align=left>' 
							+ (rec.intoPartDate==null?"":rec.intoPartDate ) + '</td><td align=left>' 
							+( rec.partPositiveDate==null?"":rec.partPositiveDate)+ '</td></tr>')
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
	})
	function getage(v) {
		var value = v;
		if (!value) {
			return '';
		}
		if (value instanceof Date) {
			value = value.dateFormat('Y-m');
		}

		var now = new Date();
		var age = now.dateFormat('Y') - Number(value.substring(0, 4))+1;
		return age;
	}
	function getSex(v)
	{
		if(v=="M")
		{
			return "男";
		}else  if(v=="W")
		{
			return "女";
		}
	}
	
	function getisWed(v)
	{
			 if(v=="0")
    {
     return "已婚";
    }
     else if(v=="1")
     {
     return "未婚";
     }else if(v=="2")
     {
     	return "离异";
     }else if(v=="3")
     {
      return "丧偶";
     }
		
	}
  function getisMain(v)
  { if(v=="Y")
  {
     return "是";
  }
     else if(v=="N")
     {
     return "否";
     }else if(v=="")
     {
     	return "";
     }
  }
  function getHouse(v)
  { 
    if(v=="1")
    {
     return "农业";
    }
     else if(v=="2")
     {
     return "非农";
     }else if(v=="")
     {
     	return "";
     }
  }
 function  getCompent(v)
  {
  		
  		 if(v=="1")
    {
     return "工人";
    }
     else if(v=="2")
     {
     return "农民";
     }else if(v=="3")
     {
     	return "知识分子";
     }else if(v=="4")
     {
      return "其它";
     }else if(v=="")
     {
     	return "";
     }
  }
	
	
	var headTbar = new Ext.Toolbar({
		region : 'north',
		border : false,
		items : [modiBtu, '-', saveBtu, '-'/*, btnPrint*/,'-',tfAppend, '-', btnInport, '-',
				exportBtu,'-',btnDownload]
	});
	var headForm = new Ext.form.FormPanel({
		region : 'north',
		id : 'center-panel',
		height : 28,
		frame : false,
		fileUpload : true,
		// labelWidth : 70,
		// labelAlign : 'right',
		layout : 'form',
		items : [headTbar]
	});

	// 员工id
	var tfEmpId = new Ext.form.Hidden({
		id : 'empId',
		name : 'emp.empId'
	})

	// 员工姓名
	var tfChsName = new Ext.form.TextField({
		id : 'chsName',
		name : 'emp.chsName',
		fieldLabel : "姓名",
		readOnly : false
	});

	// 社保卡号
//	var tfSocialInsuranceId = new Ext.form.CodeField({
//		id : 'socialInsuranceId',
//		name : 'emp.socialInsuranceId',
//		fieldLabel : "社保卡号",
//		maxLength : 50
//	});
	// 出生日期
	var tfBrithday = new Ext.form.TextField({
		id : 'brithday',
		name : 'emp.brithday',
		style : 'cursor:pointer',
		fieldLabel : "出生日期",
		readOnly : true,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd',
					onpicked : function() {
						tfAge.setValue(getAge(this.value));
					},
					onclearing : function() {
						tfAge.setValue('');
					}
				});
			}
		}
	});

	// 性别
	var cbSex = new Ext.form.CmbHRCode({
		id : "sex",
		hiddenName : 'emp.sex',
		fieldLabel : '性别',
		selectOnFocus : true,
		type : '性别'
	});
	// 参加工作日期
//	var tfWorkDate = new Ext.form.TextField({
//		id : 'workDate',
//		name : 'emp.workDate',
//		style : 'cursor:pointer',
//		fieldLabel : "参加工作日期",
//		readOnly : true,
//		listeners : {
//			focus : function() {
//				WdatePicker({
//					startDate : '%y-%M-%d',
//					dateFmt : 'yyyy-MM-dd'
//				});
//			}
//		}
//	});
	// 是否主业
	var isMainWorkStore = new Ext.data.SimpleStore({
		data : [['', ''], ['Y', '是'], ['N', '否']],
		fields : ['value', 'text']
	});
	var isMainWorkCombo = new Ext.form.ComboBox({
		id : 'isMainWork',
		hiddenName : 'emp.isMainWork',
		mode : 'local',
		store : isMainWorkStore,
		displayField : 'text',
		valueField : 'value',
		fieldLabel : '是否主业',
		readOnly : true,
		triggerAction : 'all'
	})
	// 技术职称
//	var cbTechnologyTitlesId = new Ext.form.CmbHRBussiness({
//		id : "technologyTitlesId",
//		hiddenName : 'emp.technologyTitlesId',
//		fieldLabel : '职称',
//		selectOnFocus : true,
//		type : '技术职称'
//	});
	// 技能等级
//	var jnGrade = new Ext.form.TextField({
//		id : 'jnGrade',
//		fieldLabel : '技能等级',
//		name : 'emp.jnGrade'
//	})
	// 政治面貌
	var cbPoliticsId = new Ext.form.CmbHRBussiness({
		id : "politicsId",
		hiddenName : 'emp.politicsId',
		fieldLabel : '政治面貌',
		selectOnFocus : true,
		type : '政治面貌'
	});
	// 入党转正时间PART_POSITIVE_DATE
	var partPositiveDate = new Ext.form.TextField({
		id : 'partPositiveDate',
		name : 'emp.partPositiveDate',
		fieldLabel : '入党转正时间',
		style : 'cursor:pointer',
		readOnly : true,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd'
				});
			}
		}
	})

	// 员工工号
	var tfEmpCode = new Ext.form.CodeField({
		// id : 'empCode',
		id : 'newEmpCode',
		// name : 'emp.empCode',
		name : 'emp.newEmpCode',
		fieldLabel : "人员编码",
		maxLength : 20
	});
	// 身份证号
//	var tfIdentityCard = new Ext.form.CodeField({
//		id : 'identityCard',
//		name : 'emp.identityCard',
//		fieldLabel : "身份证号",
//		maxLength : 18
//	});

	// 年龄
	var tfAge = new Ext.form.TextField({
		id : 'age',
		fieldLabel : "年龄",
		style : 'text-align: right;',
		readOnly : true,
		width : 180 - 30
	});
	var lblAge = new Ext.form.Label({
		id : 'lblAge',
		text : '周岁'
	});

	// 民族
	var cbNationCodeId = new Ext.form.CmbHRBussiness({
		id : "nationCodeId",
		hiddenName : 'emp.nationCodeId',
		fieldLabel : '民族',
		selectOnFocus : true,
		type : '民族'
	});
	// 进厂日期
//	var tfMissionDate = new Ext.form.TextField({
//		id : 'missionDate',
//		name : 'emp.missionDate',
//		style : 'cursor:pointer',
//		fieldLabel : "进厂日期",
//		readOnly : true,
//		listeners : {
//			focus : function() {
//				WdatePicker({
//					startDate : '%y-%M-%d',
//					dateFmt : 'yyyy-MM-dd'
//				});
//			}
//		}
//	});

	// 当前学历
	var cbEducationId = new Ext.form.CmbHRBussiness({
		id : "educationId",
		hiddenName : 'emp.educationId',
		fieldLabel : '现有文化程度',
		selectOnFocus : true,
		type : '学历'
	});
	// 职称等级
	// 技术等级
//	var cbTechnologyGradeId = new Ext.form.CmbHRBussiness({
//		id : "technologyGradeId",
//		hiddenName : 'emp.technologyGradeId',
//		fieldLabel : '职称等级',
//		selectOnFocus : true,
//		type : '技术等级'
//	});
	// 婚否状况
	var cbIsWedded = new Ext.form.CmbHRCode({
		id : "isWedded",
		hiddenName : 'emp.isWedded',
		fieldLabel : '婚姻状况',
		selectOnFocus : true,
		type : '婚否状况'
	});
	// 入党时间INTO_PART_DATE
	var intoPartDate = new Ext.form.TextField({
		id : 'intoPartDate',
		name : 'emp.intoPartDate',
		fieldLabel : '入党时间',
		style : 'cursor:pointer',
		readOnly : true,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd'
				});
			}
		}
	})

	// 上次修改时间
	var hideLastModifiedDate = new Ext.form.Hidden({
		id : 'lastModifiedDate',
		name : 'emp.lastModifiedDate'
	});
	
	
	//************************************************************************************
	// 员工岗位信息
    function EmployeeExtraData() {
        // 职工岗位ID
        this.empStationId = "";
        // 岗位ID
        this.stationId = "";
        // 人员ID
        this.empId = "";
        // 岗位名称
        this.stationName = "";
        // 是否主岗位
        this.isMainStation = "";
        // 备注
        this.memo = "";
        // 职工岗位上次修改时间
        this.lastModifiedDate = "";
        // DB操作flag
        this.operateFlag = "";
    }
	// 初始化员工岗位信息
    var empExtraData = null;
    // 员工岗位上次的信息
    var empLastExtraData = null;
	// 部门Id
    var hideDeptId = new Ext.form.Hidden({
        id : 'deptId'
        // add by liuyi 091221 部门允许修改
        ,name : 'emp.deptId'
    });
    
    // 所属部门
    var tfDeptName = new Ext.form.TextField({
        id : 'deptName',
        name : 'emp.deptName',
        fieldLabel : "所属部门",
        readOnly : true,
        listeners : {
            render : function() {
                Ext.form.TextField.prototype.render.apply(this, arguments);
                
                this.el.on('dblclick', function(){
                	var flagDept = hideDeptId.getValue();
//                	alert(flagDept)
                	var selectedDept = deptSelect();
                	if(selectedDept){
                		tfDeptName.setValue(selectedDept.names)
                		hideDeptId.setValue(selectedDept.ids)
                	}
                	
                	if(flagDept != hideDeptId.getValue())
                	{
                		tfStationId.setValue(null);
                		tfStationName.setValue(null);
                	}
//                	alert(tfDeptName.getValue())
//                	alert(hideDeptId.getValue())
                });
            }
        }
    });
   
    // 工作岗位Id
    var tfStationId = new Ext.form.Hidden({
        id : 'stationId',
        name : 'emp.stationId'
    });
    
    // 工作岗位
    var tfStationName = new Ext.form.TextField({
        id : 'stationName',
        style : 'cursor:pointer',
        fieldLabel : "工作岗位",
        readOnly : true,
        listeners : {
            render : function() {
                Ext.form.TextField.prototype.render.apply(this, arguments);
                
                this.el.on('click', StationNameFocus);
            }
        }
    });
    // 选择图片
    var imagePhoto = new Ext.form.TextField({
        id : "imagePhoto",
        labelSeparator : '',
        height : 153,
        style : 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);',
        inputType : 'image'
    });
    // 图片
    var tfPhoto = new Ext.form.TextField({
        id : "photo",
        name : 'photo',
        inputType : 'file',
        labelSeparator : '',
        height : 20,
        initEvents : function(){
        	Ext.form.TextField.prototype.initEvents.apply(this, arguments);
        	var keydown = function(e){
	            e.stopEvent();
	        };
	        this.el.on("keydown", keydown, this);
        }
    });
    // 图片路径变更
    tfPhoto.on('render',function(){
    	  Ext.get('photo').on('change', function(field, newValue, oldValue) {
            var url = Ext.get('photo').dom.value;
            var image = Ext.get('imagePhoto').dom;
            
            if(Ext.isIE7){
                 image.src = Ext.BLANK_IMAGE_URL;//覆盖原来的图片   
                 image.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src= url; 
            } else {
                 image.src = url;
            }
        }
    );
    })
  
    
    // 修改照片
    var btnModifyPhto = new Ext.Button({
        text : '修改照片',
        style : 'margin-left:150px;',
        handler : function() {
            var photoDom = Ext.getDom('photo');
            var oldValue = photoDom.value;
            photoDom.click();
            var newValue = photoDom.value;
            if (oldValue != newValue) {
                tfPhoto.fireEvent('change', tfPhoto, newValue, oldValue);
            }
        }
    });
    // 员工身份
    var cbWorkId = new Ext.form.CmbHRBussiness({
        id : "workId",
        hiddenName : 'emp.workId',
        fieldLabel : '员工身份',
        selectOnFocus : true,
        type : '员工身份'
    });
     // 籍贯
//    var cbNativePlaceId = new Ext.form.CmbHRBussiness({
//        id : "nativePlaceId",
//        hiddenName : 'emp.nativePlaceId',
//        fieldLabel : '籍贯',
//        selectOnFocus : true,
//        type : '籍贯'
//    });
    
     var txtNativePlaceName = new Ext.form.TextField({
        id : "nativePlaceName",
        name : 'emp.nativePlaceName',
        fieldLabel : '籍贯',
        selectOnFocus : true
    });
   
    // 员工工号  老
	var tfEmpCodeBySelf = new Ext.form.CodeField({
		 id : 'empCode',
		 name : 'emp.empCode',
		fieldLabel : "自定义编码",
		maxLength : 20
	});
	
	
	// 户口
	var householdStore = new Ext.data.SimpleStore({
		data : [['', ''], ['1', '农业'], ['2', '非农']],
		fields : ['value', 'text']
	});
	
	var householdCombo = new Ext.form.ComboBox({
		id : 'household',
		hiddenName : 'emp.household',
		mode : 'local',
		store : householdStore,
		displayField : 'text',
		valueField : 'value',
		readOnly : true,
		triggerAction : 'all',
		fieldLabel : '户口'
	})
	// 家庭住址
    var tfFamilyAddress = new Ext.form.TextField({
        id : 'familyAddress',
        name : 'emp.familyAddress',
        fieldLabel : "家庭住址",
        maxLength : 50,
        anchor : '90%'
    });
    // 血型  BLOOD_TYPE 
	var bloodTypeStore = new Ext.data.SimpleStore({
		data : [['', ''], ['A', 'A'], ['B', 'B'],['AB', 'AB'],['O', 'O']],
		fields : ['value', 'text']
	});
	
	var bloodTypeCombo = new Ext.form.ComboBox({
		id : 'bloodType',
		hiddenName : 'emp.bloodType',
		mode : 'local',
		store : bloodTypeStore,
		displayField : 'text',
		valueField : 'value',
		readOnly : true,
		triggerAction : 'all',
		fieldLabel : '血型'
	})
	// 国籍  COUNTRY
    var tfCountry = new Ext.form.TextField({
        id : 'country',
        name : 'emp.country',
        fieldLabel : "国籍",
        maxLength : 50
    });
    
    // 本人成分 COMPONENT 
	var componentStore = new Ext.data.SimpleStore({
		data : [['', ''], ['1', '工人'], ['2', '农民'],['3', '知识分子'],['4', '其它']],
		fields : ['value', 'text']
	});
	
	var componentCombo = new Ext.form.ComboBox({
		id : 'component',
		hiddenName : 'emp.component',
		mode : 'local',
		store : componentStore,
		displayField : 'text',
		valueField : 'value',
		readOnly : true,
		triggerAction : 'all',
		fieldLabel : '本人成分'
	})
    function deptSelect() {
		    	var args = {
		    		selectModel : 'single',
		    		rootNode : {
		    			id : '0',
		    			text : '灞桥电厂'
		    		}
		    	};
		    	// 调用画面
				var object = window.showModalDialog(
								'../../../../comm/jsp/hr/dept/dept.jsp',
								args,
								'dialogWidth='  + Constants.WIDTH_COM_DEPT +
								'px;dialogHeight=' + Constants.HEIGHT_COM_DEPT +
								'px;center=yes;help=no;resizable=no;status=no;');
				// 根据返回值设置画面的值
				if (object) {
					return object;
				} 
		    }
		 // 工作岗位
    function StationNameFocus() {
        var args = {};
        if (empLastExtraData) {
        	args = {objects: empLastExtraData};
        } else {
	        // 部门ID
	        args.deptId = hideDeptId.getValue();
	        // 人员ID
	        args.empId = empIdUsing;
	        // 第一次
	        args.first = true;
        }
        var object = window.showModalDialog('../empstationmaintain/PD003.jsp',
                args, 'dialogWidth=700px;dialogHeight=520px;center=yes;help=no;resizable=no;status=no;');
        
        if (object) {
        	var lastEmpExtraData = empExtraData;
            empExtraData = [];
            
        	var station = null;
        	var mainStation = null;
        	var updateCount = 0;
        	var lastStationId = tfStationId.getValue();
        	for (var i = 0; i < object.length; i++) {
        		station = object[i];
    			if (!station.existFlag) {
    				continue;
    			}
        		if (station.isLeft !== 'false' && station.existFlag !== 'D') {
    				continue;
    			}
    			if (station.isLeft === 'false' && station.mainStation === 'Y') {
    				mainStation = station;
    			} else {
    				station.mainStation = 'N';
    			}
    			
    			// 职工岗位对象
    			var objEmpSta = new EmployeeExtraData();
    			empExtraData.push(objEmpSta);
    			// 职工岗位ID
		        objEmpSta.empStationId = station.empStationId;
		        // 岗位ID
		        objEmpSta.stationId = station.stationId;
		        // 人员ID
	        	objEmpSta.empId = empIdUsing;
		        // 岗位名称
		        objEmpSta.stationName = station.stationName;
		        // 是否主岗位
		        objEmpSta.isMainStation = station.mainStation;
		        // 备注
		        objEmpSta.memo = station.memo;
		        // 职工岗位上次修改时间
		        objEmpSta.lastModifiedDate = station.lastModifyDate;
		        // DB操作flag
		        objEmpSta.operateFlag = station.existFlag;
		        
		        if (objEmpSta.operateFlag) {
	        		updateCount++;
	        	}
        	}
			
        	if (updateCount < 1) {
        		empExtraData = lastEmpExtraData;
        		return false;	
        	}
            // 设置职工岗位
            empLastExtraData = object;
            if (mainStation) {
            	// 如果主岗位已修改
		        tfStationId.setValue(mainStation.stationId);
	            tfStationName.setValue(mainStation.stationName);
            }
        }
        return false;
    }
   
	//************************************************************************************
	
	
	var baseInfoForm = new Ext.form.FormPanel({
		id : 'baseInfoForm',
		layout : 'column',
		frame : true,
		border : false,
		fileUpload : true,
		labelAlign : 'left',
		labelWidth : 80,
		defaults : {
			layout : 'form'
		},
		items : [{
			columnWidth : .33,
			items : [tfEmpId, tfChsName,hideDeptId,tfDeptName,  tfBrithday,cbSex
			,cbWorkId,isMainWorkCombo,tfEmpCodeBySelf]
		}, {
			columnWidth : .33,
			items : [tfEmpCode,tfStationId,tfStationName,  tfAge,cbNationCodeId,
			cbPoliticsId,txtNativePlaceName,cbIsWedded]
		}
		, {
                  columnWidth : .33,
                    border : false,
                    items : [imagePhoto, tfPhoto]
                },{
			columnWidth : .33,
			items : [ householdCombo]
		},{
			columnWidth : .6,
			items : [ tfFamilyAddress]
		},{
			columnWidth : .33,
			items : [ bloodTypeCombo,componentCombo]
		},{
			columnWidth : .33,
			items : [cbEducationId,intoPartDate]
		},{
			columnWidth : .33,
			items : [ tfCountry,partPositiveDate,hideLastModifiedDate]
		}
//		,
//		
//			{
//			columnWidth : .33,
//			items : [ 
//			tfWorkDate, 
//			cbTechnologyTitlesId,
//					jnGrade,
//					
//					tfSocialInsuranceId,
//					tfMissionDate, 
//					cbTechnologyGradeId,
//					  tfIdentityCard
//					  ]
//		}
		]
	});

	// 计算年龄
	//update  by sychen 20100709 根据出生日期或身份证计算年龄
	function getAge(argDate,argCardId) {
		var value = argDate;
		var cardId=argCardId;
		var age="";
		if (!value) {
			if(!cardId){
			   return '';
			}
			else{
			 var now = new Date();
			 if(cardId.length==18){//身份证号码为18位的
			   age = now.dateFormat('Y') - Number(cardId.substring(6, 10)) + 1;
			 }
			 if(cardId.length==15){//身份证号码为15位的
			 	var valuet=now.dateFormat('Y-m')
			 	if (Number(valuet.substring(2, 4)) < Number(cardId
							.substring(6, 8))) {
						var birthday = ((Number(valuet.substring(0, 2)) - 1)
								* 100 + Number(cardId.substring(6, 8)));
						age = now.dateFormat('Y')- birthday + 1;
					}
			 }
			}
		}
		else {
			if (value instanceof Date) {
				value = value.dateFormat('Y-m');
			}

			var now = new Date();
			age = now.dateFormat('Y') - Number(value.substring(0, 4)) + 1;
		}
		return age;
	}
	
//	function getAge(argDate) {
//		var value = argDate;
//		if (!value) {
//			return '';
//		}
//		if (value instanceof Date) {
//			value = value.dateFormat('Y-m');
//		}
//
//		var now = new Date();
//		var age = now.dateFormat('Y') - Number(value.substring(0, 4))+1;
//		return age;
//	}
	//update by sychen 20100709 end----------------------------------------//
	
	var infoForm = new Ext.Panel({
		id : 'infoForm',
		layout : 'border',
		title : '基本信息',
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
			region : 'center',
			layout : 'fit',
			border : false,
			margins : '0 0 0 0',
			split : true,
			collapsible : false,
			items : [baseInfoForm]
		}]
	})
	// ************基础信息 结束********************************************

	// 加载员工基本信息
	function loadEmp(empId) {
		empIdUsing = empId;
		setEnable(false);
		if (empId == null || empId == '') {
			clearAllFields();
			return;
		}

		// 加载数据
		Ext.Ajax.request({
			method : Constants.POST,
			url : 'hr/getEmpMaintBaseInfo.action',
			params : {
				empId : empId
			},
			success : function(result, request) {
				var record = eval('(' + result.responseText + ')');
				// 数据库异常
				if (record && typeof record.msg != 'undefined'
						&& record.msg == "SQL") {
					Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
					return;
				}

				if (!record) {
					// 清除所有项目
					clearAllFields();
					return;
				}
				// 设置画面控件的值
				setPageDate(record);
				// 设置员工名字
				// employee.changeName(record.chsName);
			},
			failure : function() {
				Ext.Msg.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
			}
		});
		
		// 加载图片
        Ext.get('imagePhoto').dom.src = "hr/getEmpPhotoInfo.action?empId="
            + empId + "&time=" + new Date().getTime();
            
         
          getPhoto();
		 
        
	}
  
  
  
	// 设置Form是否可以编辑
	function setEnable(argFlag) {
		baseInfoForm.getForm().items.each(function(f) {
			var xtype = f.getXType();
			if (f.el.dom
					&& (xtype == 'textfield' || xtype == 'CodeField'
							|| xtype == 'numfield' || xtype == 'textarea'
							|| xtype == 'radio'
							|| xtype == 'NaturalNumberField'
							|| xtype == 'CmbHRBussiness'
							|| xtype == 'CmbHRCode' || xtype == 'combo')) {
				f.setDisabled(!argFlag);
			}
		});

		 // 修改照片按钮
        btnModifyPhto.setDisabled(!argFlag);
		// 修改按钮
		modiBtu.setDisabled(!argFlag);
		// 保存按钮
		saveBtu.setDisabled(!argFlag);

		// 员工工号
		tfEmpCode.setDisabled(true);
		// 人员姓名
		// tfChsName.setDisabled(true);
		// 年龄
		tfAge.setDisabled(true);
	}
	// 设置画面控件的值
	function setPageDate(record) {
		if (!record.modifiedDate) {
//			 alert(Ext.util.JSON.encode(record))
			if (record.empState == 3) {
				Ext.Msg.alert('提示信息', '该员工已离职！');
			}
			// 清除所有项目
			clearAllFields();
			baseInfoForm.getForm().setValues(record);
			// 设置年龄
//			tfAge.setValue(getAge(record.brithday));//update by sychen 20100709
			tfAge.setValue(getAge(record.brithday,record.payCardId));
			// 性别
			cbSex.setValue(record['sex'], true);
			// 民族
			cbNationCodeId.setValue(record['nationCodeId'], true);
			// 政治面貌
			cbPoliticsId.setValue(record['politicsId'], true);
			// 婚否状况
			cbIsWedded.setValue(record['isWedded'], true);
			 // 员工身份
            cbWorkId.setValue(record['workId'], true);
			// 技术等级
//			cbTechnologyGradeId.setValue(record['technologyGradeId'], true);
			// 技术职称
//			cbTechnologyTitlesId.setValue(record['technologyTitlesId'], true);
			// 当前学历
			cbEducationId.setValue(record['educationId'], true);
		} else {
			// 设置修改时间
			hideLastModifiedDate.setValue(record.modifiedDate);

		}

		setEnable(false);
		// 修改按钮
		modiBtu.setDisabled(false);
		 // 初始化员工岗位信息
        empExtraData = null;
        empLastExtraData = null;
	}
	// 清除所有项目
	function clearAllFields() {
		baseInfoForm.getForm().reset();
		// 清除附件内容
        var domAppend = tfPhoto.el.dom;
        var parent = domAppend.parentNode;
        // 保存
        var domForSave = domAppend.cloneNode();
        // 移除附件控件
        parent.removeChild(domAppend);
        // 再追加控件
        parent.appendChild(domForSave);
        // 应用该控件
        tfPhoto.applyToMarkup(domForSave);

        // 清除图片内容
        domAppend = imagePhoto.el.dom;
        parent = domAppend.parentNode;
        // 保存
        domForSave = domAppend.cloneNode();
        // 移除附件控件
        parent.removeChild(domAppend);
        // 再追加控件
        parent.appendChild(domForSave);
        // 应用该控件
        imagePhoto.applyToMarkup(domForSave);
    	
        // 初始化员工岗位信息
        empExtraData = null;
        empLastExtraData = null;
	}
	// 提交前设置Combo的值，保证其以Number类型传回服务端
	function setComboValue() {
		// 性别
		if (!cbSex.getValue()) {
			cbSex.setValue('');
		}

		// 民族
		if (!cbNationCodeId.getValue()) {
			cbNationCodeId.setValue('');
		}
		// 政治面貌
		if (!cbPoliticsId.getValue()) {
			cbPoliticsId.setValue('');
		}
		// 婚否状况
		if (!cbIsWedded.getValue()) {
			cbIsWedded.setValue('');
		}

		// 技术等级
//		if (!cbTechnologyGradeId.getValue()) {
//			cbTechnologyGradeId.setValue('');
//		}

		// 技术职称
//		if (!cbTechnologyTitlesId.getValue()) {
//			cbTechnologyTitlesId.setValue('');
//		}
		// 当前学历
		if (!cbEducationId.getValue()) {
			cbEducationId.setValue('');
		}

		// 是否主业
		if (!isMainWorkCombo.getValue()) {
			isMainWorkCombo.setValue('')
		}
		// 员工身份
		if (!cbWorkId.getValue()) {
			cbWorkId.setValue('')
		}
		//籍贯
		if (!txtNativePlaceName.getValue()) {
			txtNativePlaceName.setValue('')
		}
		//  
		if (!householdCombo.getValue()) {
			householdCombo.setValue('')
		}
		// 血型
		if (!bloodTypeCombo.getValue()) {
			bloodTypeCombo.setValue('')
		}
		// 本人成分
		if(!componentCombo.getValue()){
			componentCombo.setValue('')
		}

	}
	// 保存按钮处理
	function onSave() {

		
		if (tfEmpCode.getValue() == null || tfEmpCode.getValue() == '') {
			Ext.Msg.alert('提示', '工号不可为空！');
			return;
		}
		Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_001, function(
				buttonobj) {
			if (buttonobj == "yes") {

				// 提交前设置Combo的值
				setComboValue();
				// 保存数据
				baseInfoForm.getForm().submit({
					method : Constants.POST,
					url : 'hr/savePersonnelFileInfo.action',
					params : {
						'filePath' : tfPhoto.getValue()
					},
					success : function(form, action) {
						var o = eval('(' + action.response.responseText + ')');
						Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004);
						// 设置画面控件的值
						setPageDate(o);
					},failure : function(form,action){
						Ext.Msg.alert('提示','数据保存失败!');
					}
				});
			}
		});
	}

	if(config && config.empId){
		loadEmp(config.empId)
	}
	
	this.setEnable = setEnable;
	this.loadEmp = loadEmp;
	this.baseInfoForm = baseInfoForm;
	this.modiBtu = modiBtu;
	this.saveBtu = saveBtu;
	this.tfAppend = tfAppend;
	this.btnInport = btnInport;
	this.exportBtu = exportBtu;
	this.infoForm = infoForm;
	
	
}