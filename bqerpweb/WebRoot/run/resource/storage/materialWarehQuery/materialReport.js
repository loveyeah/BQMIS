// 物料盘点表打印
// author:chenshoujiang
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
    Ext.QuickTips.init();   
            
//    // 待盘仓库 DataStore
//    var dsDelayStore = new Ext.data.JsonStore({
//        root : 'list',
//        url : "resource/getWarehouseList.action",
//        fields : ['whsNo', 'whsName']
//    });
//    
//    // 待盘仓库
//    dsDelayStore.load();
//    dsDelayStore.on('load', function() {
//        if(dsDelayStore.getTotalCount() > 0) {
//            var recordLocation = dsDelayStore.getAt(0);
//            dsDelayStore.remove(recordLocation);
//            var record = new Ext.data.Record({
//                    whsNo:"",
//                    whsName:"&nbsp"
//                })
//            dsDelayStore.insert(0, record);
//            }
//    })
    
    // 待盘仓库组合框
//    var cboDelayStore = new Ext.form.ComboBox({
//        fieldLabel : "仓库名称",
//        name : "delayStore",
//        width : 150,
//        store : dsDelayStore,
//        displayField : "whsName",
//        valueField : "whsNo",
//        mode : 'local',
//        triggerAction : 'all',
//        readOnly : true,
//        listeners : {
//            select : function() {
//                // 如果待盘仓库数据为空，则待盘库位也为空
//                if(cboDelayStore.getRawValue() == "&nbsp")
//                {
//                   
//                    
//                }
//                else if(cboDelayStore.getRawValue() != "&nbsp"){
//                    hiddenDelayStore.setValue(Ext.get('delayStore').dom.value);
//                }
//            }
//        }
//    });
    
    var cboDelayStore= new Ext.ux.ComboBoxTree({
		fieldLabel : '物料类别',
		allowBlank : true,
		width : 200,
		id : "materialClassId",
		anchor : '90%',
		displayField : 'text',
		valueField : 'id',
		hiddenName : 'materialClass',
		blankText : '请选择',
		emptyText : '请选择',
		// readOnly : true,
		tree : {
			xtype : 'treepanel',
			// 虚拟节点,不能显示
			rootVisible : false,

			loader : new Ext.tree.TreeLoader({
				dataUrl : 'resource/getMaterialClassList.action'
			}),
			root : new Ext.tree.AsyncTreeNode({
				id : '-1',
				name : '合肥电厂',
				text : '合肥电厂'
			}),
			listeners : { // add by ywliu 20091116
				click : function(node, e) {
					hiddenDelayStore.setValue(node.attributes.id);
					
				}
			}
		},
		selectNodeModel : 'all'
	});

    // 待盘仓库隐藏域
    var hiddenDelayStore = new Ext.form.Hidden({
        id : "hiddenDelayStore",
        value:""
    });

    
 //--------add by fyyang 090810----月份
    /**
	 * 月份Field
	 */
	var nowdate = new Date();
	var date1 = nowdate.toLocaleDateString();
	var exdate;
	if (date1.substring(6, 7) == '月') {
		exdate = date1.substring(0, 4) + '-0' + date1.substring(5, 6);
	} else {
		exdate = date1.substring(0, 4) + "-" + date1.substring(5, 7);
	}
	var enddate = exdate;
	var monthDate = new Ext.form.TextField({
		name : 'monthDate',
		value : exdate,
		id : 'monthDate',
		fieldLabel : "月份",
		style : 'cursor:pointer',
		cls : 'Wdate',
		width : 90,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M',
					dateFmt : 'yyyy-MM',
					alwaysUseStartDate : true
				});
			}
		}
	});
 //-----------------------------------   
    
   
    
    // 打印盘点表按钮
    var btnPrint = new Ext.Button({
        text : "打印",
        iconCls : 'print',
        disabled:false,
        handler : printMaterialType
    }); 
    
     var exportbtn = new Ext.Button({
				text : "导出",
				iconCls : 'export',
				handler : exportbtn
			})
       
    // head工具栏
    var headTbar = new Ext.Toolbar({
        region : 'north',
//      border:false,
        items : ['月份：',monthDate,'&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp物料类别:',cboDelayStore, 
         '&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp',btnPrint,exportbtn]
    });
    
    // 显示区域
    var layout = new Ext.Viewport({
        layout : 'fit',
//        margins : '0 0 0 0',
//        border : false,
        autoHeight : true,
        items : [headTbar]        
    });

    function exportbtn() {
    	
    	if(cboDelayStore.getValue()==null||cboDelayStore.getValue()=="")
    	{
    	  Ext.Msg.alert("提示","请选择物料类别！");	
    	  return ;
    	}
		var strMonth=monthDate.getValue().substring(0,4)+monthDate.getValue().substring(5,7);

		
	
		url = "/powerrpt/frameset?__report=hfmis/receiveIssuesDetailsWh.rptdesign";
		url += "&__action=print&whsName="+cboDelayStore.getRawValue()+"&whsNo="+hiddenDelayStore.getValue()+"&month=" + strMonth +"&__format=xls";
		window.open(url);
	}
    
    
    /**
     * 物料盘点打印按钮按下时
     */
    function printMaterialType() 
    {
    	if(cboDelayStore.getValue()==null||cboDelayStore.getValue()=="")
    	{
    	  Ext.Msg.alert("提示","请选择物料类别！");	
    	  return ;
    	}
    	var strMonth=monthDate.getValue().substring(0,4)+monthDate.getValue().substring(5,7);
//        strReportAdds="/powerrpt/report/webfile/receiveIssuesDetails.jsp?whsName="+hiddenDelayStore.getValue()+"&whsNo="+cboDelayStore.getValue()+"&month="+strMonth;
    	strReportAdds="/powerrpt/report/webfile/receiveIssuesDetailsWh.jsp?"
    	strReportAdds+="whsName="+encodeURIComponent(cboDelayStore.getRawValue())+"&whsNo="+encodeURIComponent(hiddenDelayStore.getValue())+"&month="+encodeURIComponent(strMonth);
		window.open(strReportAdds);
    }
   
    
   
})