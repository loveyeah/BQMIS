/**
 * 员工调动查询
 * @author 黄维杰
 * @since 2009-02-13
 */
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
    Ext.QuickTips.init();

    // 总tabpanel
    var tabPanel = new Ext.TabPanel({
        renderTo : document.body,
        activeTab : 0,
        tabPosition : 'bottom',
        id : "tabPanel",
        plain : true,
        defaults : {
            autoScroll : true
        },
        frame : false,
        border : false,
        items : [/*{  //  这三个页面整合为一个查询页面  modify by  wpzhu 20100629
            id : 'tabBanZu',
            title : '班组调动查询',
            html : "<iframe name='tabBanZu' src='hr/employee/empMoveQuery/banzu/PE007_banzu.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        },*/ {
            id : 'tabEmpMove',
            title : '员工调动查询',
            html : "<iframe name='tabEmpMove' src='hr/employee/empmovedeclare/PE007_empMove.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        }/*, {
            id : 'tabEmpBorrow',
            title : '员工借调查询',
            html : "<iframe name='tabEmpBorrow' src='hr/employee/empMoveQuery/empBorrow/PE007_empBorrow.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        }*/]
    });

    // 设定布局器及面板
    var layout = new Ext.Viewport({
        layout : "border",
        border : false,
        items : [{
            region : 'center',
            layout : 'fit',
            border : false,
            split : true,
            collapsible : false,
            items : [tabPanel]
        }]
    });
    function EmpMoveQuery() {
    	// 部门名称
    	this.name = "";
    	// 部门ID
    	this.id = "";
    	
    	// 选择部门
		EmpMoveQuery.prototype.deptSelect =
		    function deptSelect() {
		    	var args = {
		    		selectModel : 'single',
		    		rootNode : {
		    			id : '0',
		    			text : Constants.POWER_NAME
		    		}
		    	};
		    	// 调用画面
				var object = window.showModalDialog(
								'../../../comm/jsp/hr/dept/dept.jsp',
								args,
								'dialogWidth=' + Constants.WIDTH_COM_DEPT + 'px;dialogHeight=' + Constants.HEIGHT_COM_DEPT + 'px;center=yes;help=no;resizable=no;status=no;');
				// 根据返回值设置画面的值
				if (object) {
					if (typeof(object.names) != "undefined") {
						this.name = object.names;
					}
					if (typeof(object.ids) != "undefined") {
						this.id = object.ids;
					}
				}
		    }
    	
        // 弹出窗口，查看备注
        EmpMoveQuery.prototype.showWin =
            function showWin(value) {
            // 备注
            var taShowMemo = new Ext.form.TextArea({
                id : "taShowMemo",
                maxLength : 127,
                width : 180,
                value : value,
        		disabled : true
            });
            
            // 弹出画面
            var win = new Ext.Window({
                height : 170,
                width : 350,
                layout : 'fit',
                resizable : false,
                modal  : true,
                closeAction : 'hide',
                items : [taShowMemo],
                buttonAlign : "center",
                title : '详细信息查看窗口',
                buttons : [{
                    text : Constants.BTN_CLOSE,
                    iconCls : Constants.CLS_CANCEL,
                    handler : function() {
                        win.hide();
                    }
                }]
            });
            win.x=undefined;
            win.y=undefined;
            win.show();
            win.on('show', function() {
                taShowMemo.focus(true, 100);
            });
            win.on('hide', function() {
                taShowMemo.setValue("");
            });
        }
    }
    var empMoveQuery = new EmpMoveQuery();
    Ext.getCmp('tabPanel').empMoveQuery = empMoveQuery;
});
