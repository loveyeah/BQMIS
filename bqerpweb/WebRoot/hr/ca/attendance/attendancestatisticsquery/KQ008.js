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
        items : [{
            id : 'tabEmployee',
            title : '请假统计查询',
            html : "<iframe name='tabEmployee' src='hr/ca/attendance/attendancestatisticsquery/leave/KQ008_leave.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        }, {
            id : 'tabContinue',
            title : '加班统计查询',
            html : "<iframe name='tabContinue' src='hr/ca/attendance/attendancestatisticsquery/workovertime/KQ008_workovertime.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        }, {
            id : 'tabChange',
            title : '运行班统计查询',
            html : "<iframe name='tabChange' src='hr/ca/attendance/attendancestatisticsquery/workshift/KQ008_workshift.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        }, {
            id : 'tabStop',
            title : '出勤统计查询',
            html : "<iframe name='deptonduty' src='hr/ca/attendance/attendancestatisticsquery/deptonduty/KQ008_deptonduty.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        }, {
            id : 'tabDuedate',
            title : '部门请假单查询',
            html : "<iframe name='deptleave' src='hr/ca/attendance/attendancestatisticsquery/deptleave/KQ008_deptleave.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        }]
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
    
     function AttendanceQuery() {
    	// 部门名称
    	this.name = "";
    	// 部门ID
    	this.id = "";
    
		// 选择部门
		AttendanceQuery.prototype.deptSelect =
		    function deptSelect() {
		    	var args = {
		    		selectModel : 'single',
		    		rootNode : {
		    			id : '0',
		    			text : '灞桥热电厂'
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
					if (typeof(object.names) != "undefined") {
						this.name = object.names;
					}
					if (typeof(object.ids) != "undefined") {
						this.id = object.ids;
					}
					return true;
				} else {
					return false;
				}
		    }
    }
    
     // 弹出窗口，查看备注
        AttendanceQuery.prototype.showWin =
            function showWin(value) {
            // 备注
            var taShowMemo = new Ext.form.TextArea({
                id : "taShowMemo",
                maxLength : 250,
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
    
    
    var attendanceQuery = new AttendanceQuery();
    Ext.getCmp('tabPanel').attendanceQuery = attendanceQuery;
    
});
