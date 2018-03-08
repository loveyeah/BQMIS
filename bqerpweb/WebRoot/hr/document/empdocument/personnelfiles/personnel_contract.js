Ext.ns("Personnel")
Personnel.Workcontract = function(config){

    var empIdUsing = null;
    var  newEmpCode=null; //add by sychen 20100713
//    var employee = parent.Ext.getCmp('tabPanel').employee;
    
    var tfAppend = new Ext.form.TextField({
		name : 'xlsFile',
		inputType : 'file',
		width : 200
	})
	//----------begin----------
	function downloadMoudle(){
		window.open("./downloadMoudle/劳动合同-模板.xls")
	}
	
	var btnDownload = new Ext.Toolbar.Button({
		id : 'btnDownload',
		text : '模板下载',
		handler : downloadMoudle,
		iconCls : 'export'
	});
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
						type : 'workcontract'
					},
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("提示", o.msg);
						contractInfoStore.reload();
					},
					failture : function() {
						Ext.Msg.alert(Constants.ERROR, "导入失败！");
					}
				})
			}
		}
	}
    
    var contractRecord = new Ext.data.Record.create([{
            name : 'workcontractid'
        }, {
            name : 'empId'
        }, {
            name : 'empName'
        }, {
            name : 'deptId'
        }, {
            name : 'deptName'
        }, {
            name : 'stationId'
        }, {
            name : 'stationName'
        }, {
            name : 'wrokContractNo'
        }, {
            name : 'fristDepId'
        }, {
            name : 'fristDepName'
        }, {
            name : 'fristAddrest'
        }, {
            name : 'contractTermId'
        }, {
            name : 'workSignDate'
        }, {
            name : 'startDate'
        }, {
            name : 'endDate'
        }, {
            name : 'ifExecute'
        }, {
            name : 'contractContinueMark'
        }, {
            name : 'memo'
        }, {
            name : 'insertby'
        }, {
            name : 'insertdate'
        }, {
            name : 'enterpriseCode'
        }, {
            name : 'isUse'
        }, {
            name : 'lastModifiedBy'
        }, {
            name : 'lastModifiedDate'
        }, {
        	// add by ywliu 20100611
            name : 'owner'
        }, {
        	// add by ywliu 20100611
            name : 'signedInstitutions'
        }, {
        	// add by ywliu 20100611
            name : 'contractPeriod'
        }, {
        	// add by ywliu 20100611
            name : 'laborType'
        }, {
        	// add by ywliu 20100611
            name : 'contractType'
        }, {
        	// add by sychen 20100714
            name : 'contractTerminatedCode'
        }]);
        
    var sm = new Ext.grid.CheckboxSelectionModel({
                singleSelect : false
            })
    // grid列模式
    var contractCM = new Ext.grid.ColumnModel([sm,
        new Ext.grid.RowNumberer({
            header : '行号',
            width : 35
        }), {
            header : '状态',
            width : 120,
            dataIndex : 'ifExecute',
            renderer : function(val) {
            	if(val == 0) {
            		return '无效';
            	} else if(val == 1) {
            		return '有效';
            	}
            }
        }, {
            header : '人员编码',
            width : 120,
            dataIndex : 'empId'
        }, {
            header : '甲方',
            width : 80,
            dataIndex : 'owner'
        }, {
            header : '签订机构',
            width : 100,
            dataIndex : 'signedInstitutions'
        }, {
            header : '合同编号',
            width : 100,
            dataIndex : 'wrokContractNo'
        }, {
            header : '签订时间',
            hidden : true,
            width : 80,
            dataIndex : 'workSignDate',
            renderer : renderDate
        }, {
            header : '合同期限(年)',
            width : 60,
            hidden : true,
            dataIndex : 'contractPeriod'
        }, {
            header : '合同期限(月)',
            width : 200,
            dataIndex : 'contractPeriod'
    	}, {
            header : '用工形式',
            width : 200,
            dataIndex : 'laborType',
            renderer : function(val) {
            	if(val == 1) {
            		return '临时工';
            	} else if(val == 2) {
            		return '培训工';
            	} else if(val == 3) {
            		return '合同工';
            	} else if(val == 4) {
            		return '劳务派遣';
            	} else if(val == 5) {
            		return '其它';
            	}
            }
    	}, {
            header : '合同类别',
            width : 200,
            dataIndex : 'contractType',
            renderer : function(val) {
            	if(val == 1) {
            		return '固定期限合同';
            	} else if(val == 2) {
            		return '无固定期限合同';
            	} else if(val == 3) {
            		return '完成一定工作期限合同';
            	}
            }
    	}]);
    contractCM.defaultSortable = true;
    
    // 所有合同信息和人员信息的json阅读器
    var contractInfoReader = new Ext.data.JsonReader({
        root : "list",
        totalProperty : "totalCount"
    }, contractRecord)
    
    var contractInfoStore = new Ext.data.Store({
    	url : 'hr/getContractByEmpId.action',
        reader : contractInfoReader
    });
    
    var tbar = new Ext.Toolbar({
    	items : [tfAppend,btnInport,btnDownload
    	 // add by sychen 20100713
    	                  ,{
							text : "导出",
							id : 'btnreport',
							iconCls : 'export',
							handler :function(){
			Ext.Ajax.request({
				url : 'hr/getContractByEmpId.action',
				params : {
				empId : empIdUsing,
				start : 0,
                limit : Constants.PAGE_SIZE
			},
				method : 'post',
				success : function(response,options){
				var res = Ext.decode(response.responseText).list;
				var ifExecute=null;
				var  laborType=null;
				var contractType=null;
				if(contractInfoStore.getTotalCount()==0)
				{
					 Ext.Msg.alert('提示', '无数据进行导出！');
			         return;
				}else
				{
			  if(res){
				Ext.Msg.confirm('提示', '确认要导出数据？', function(id) {
			    if (id == 'yes') {
				var tableHeader = "<table border=1><tr><th>状态</th><th>人员编码</th><th>甲方</th><th>签订机构</th><th>合同编号" +
						"</th><th>签订时间</th><th>合同期限（年）</th><th>合同期限（月）</th><th>用工形式</th><th>合同类别</th>" +
						"<th>合同解除文号</th><th>合同生效日期</th><th>合同终止日期</th></tr>";
				var html = [tableHeader];
					for (var i = 0; i <res.length; i++) {
					var rec = res[i];
					
					if(rec.ifExecute== 0) {
            		ifExecute= '无效';
            	} else if(rec.ifExecute== 1) {
            		ifExecute= '有效';
            	}
            	else
            	   ifExecute='';
            	   
            	   if(rec.laborType == 1) {
            		laborType= '临时工';
            	} else if(rec.laborType == 2) {
            		laborType= '培训工';
            	} else if(rec.laborType == 3) {
            		laborType= '合同工';
            	} else if(rec.laborType == 4) {
            		laborType= '劳务派遣';
            	} else if(rec.laborType == 5) {
            		laborType= '其它';
            	}else
            	   laborType='';
            	   
            	if(rec.contractType == 1) {
            		contractType= '固定期限合同';
            	} else if(rec.contractType == 2) {
            		contractType= '无固定期限合同';
            	} else if(rec.contractType == 3) {
            		contractType= '完成一定工作期限合同';
            	}else 
            	    contractType= '';
            	    
   	        	    html.push('<tr><td align=left >'+ifExecute+'</td>'+
   	        	                          '<td align=left>'+( newEmpCode==null?"":newEmpCode)+'</td>' +
   	        	                          '<td align=left>'+ (rec.owner==null?"":rec.owner)+ '</td>' +
   	        	                            '<td align=left>'+ (rec.signedInstitutions==null?"":rec.signedInstitutions)+ '</td>' +
   	        	                          '<td align=left>'+( rec.wrokContractNo==null?"":rec.wrokContractNo)+  '</td>' +
   	        	                          '<td align=left>'+( rec.workSignDate==null?"":rec.workSignDate)+'</td>' +
   	        	                          '<td align=left>'+ (rec.workSignDate==null?"":rec.workSignDate)+ '</td>' +
   	        	                            '<td align=left>'+ (rec.contractPeriod==null?"":rec.contractPeriod)+ '</td>' +
   	        	                          '<td align=left>'+laborType+  '</td>' +
   	        	                          '<td align=left>'+contractType+'</td>' +
   	        	                          '<td align=left>'+ (rec.contractTerminatedCode==null?"":rec.contractTerminatedCode)+ '</td>' +
   	        	                            '<td align=left>'+ (rec.startDate==null?"":rec.startDate)+ '</td>' +
   	        	                          '<td align=left>'+( rec.endDate==null?"":rec.endDate)+ '</td></tr>')
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
    // add by sychen 20100713  end
    	]
    })
    
       // add by sychen 20100713
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
			ExWSh.Columns("L").ColumnWidth  = 13;
			ExWSh.Columns("M").ColumnWidth  = 13;
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
    // 工作简历Grid
    var contractGrid = new Ext.grid.GridPanel({
    	id : 'contractGrid',
        store : contractInfoStore,
        sm : sm,
        cm : contractCM,
        // 分页
        bbar : new Ext.PagingToolbar({
                pageSize : Constants.PAGE_SIZE,
                store : contractInfoStore,
                displayInfo : true,
                displayMsg : Constants.DISPLAY_MSG,
                emptyMsg : Constants.EMPTY_MSG
            }),
        tbar : headForm,
        border : false,
        enableColumnMove : false
    });
    
    var contractPanel = new Ext.Panel({
    	id : 'contractPanel',
    	title : "劳动合同",
    	frame : true,
    	border : false,
    	layout : 'fit',
    	items : [contractGrid]
    })
    
    // 加载员工合同信息 
	function loadContractInfo(empParam) {
		if(!empParam || empParam.data.empId == null ){
			Ext.Msg.alert('提示','人员id不存在，出现异常!');
			contractGrid.getTopToolbar().setDisabled(true);
			return;
		}
		contractGrid.getTopToolbar().setDisabled(false);
		empIdUsing = empParam.data.empId;
		newEmpCode=empParam.data.newEmpCode;
        contractInfoStore.baseParams = {
            empId : empIdUsing
        };
        
        contractInfoStore.load({
            params:{
                start : 0,
                limit : Constants.PAGE_SIZE
            }
        });
        
    }
    
    // 重新加载Grid
    function reloadGrid(options) {
        contractInfoStore.reload(options);
    }
    
    function renderDate(value) {
        if (value instanceof Date) {
            return value.dateFormat('Y-m-d');
        }
        value = value ? value.match(/\d{4}-\d{2}-\d{2}/gi) : '';
        return value ? value[0] : '';
    }
    
	this.contractPanel = contractPanel;
	this.loadContractInfo = loadContractInfo;
	this.contractGrid = contractGrid;
	
}
