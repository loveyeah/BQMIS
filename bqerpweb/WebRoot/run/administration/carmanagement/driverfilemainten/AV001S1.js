Ext.onReady(function() {

	// 导出按钮
	var btnExport = new Ext.Button({
				id : "export",
				text : Constants.BTN_EXPORT,
				iconCls : Constants.CLS_EXPORT,
				disabled : true,
				handler : exportIt
			});
	// 查询按钮
	var btnQuery = new Ext.Button({
				id : "query2",
				text : Constants.BTN_QUERY,
				iconCls : Constants.CLS_QUERY,
				handler : queryIt2
			});
	// 所在部门名称
	var txtDepName2 = new Ext.form.TextField({
				id : "txtDepName2",
				name : "txtDepName2",
				readOnly : true,
				width : 120
			});
	txtDepName2.onClick(selectDep2);
	// 姓名
    var drpName = new Ext.form.CmbWorkerByDept({
				id : "strName",
				name : "strName",
				fieldLabel : "姓名",
				maxLength : 15,
				width : 120
			});
	// 驾照类型
	var drpLicence2 = new Ext.form.ComboBox({
				id : "drpLicence2",
				name : "drpLicence2",
				fieldLabel : "驾照类型",
				width : 150,
				readOnly : true,
				triggerAction : 'all',
				mode : 'local',
				displayField : 'text',
				valueField : 'value',
				store : new Ext.data.JsonStore({
							fields : ['value', 'text'],
							data : [{
										value : '',
										text : ''
									}, {
										value : 'A',
										text : 'A'
									}, {
										value : 'B',
										text : 'B'
									}, {
										value : 'C',
										text : 'C'
									}]
						})
			});
	// 部门编码
	var hdnDepCode = new Ext.form.Hidden({
				value : ""
			});
	// 顶部工具栏
	var tbar2 = new Ext.Toolbar({
				items : ["所在部门:", txtDepName2, "-", "姓名:", drpName, "-",
						"驾照类型:", drpLicence2, "-", btnQuery, btnExport,hdnDepCode]
			});

	// 导出按钮处理函数
	function exportIt() {
		Ext.MessageBox.confirm(MessageConstants.SYS_REMIND_MSG,
				MessageConstants.COM_C_007, function(button, text) {
					if(button=="yes"){
						document.all.blankFrame.src = "administration/exportDriverFile.action";
					}
				})
	}
	// 选择部门处理函数
	function selectDep2(){
		var args = {selectModel:'single',rootNode:{id:'0',text:'合肥电厂'},onlyLeaf:true};
		var object = window.showModalDialog(
						'../../../../comm/jsp/hr/dept/dept.jsp',
						args,
						'dialogWidth=800px;dialogHeight=520px;center=yes;help=no;resizable=no;status=no;');
		// 根据返回值设置画面的值
		if (object) {
			drpName.store.removeAll();
			drpName.setValue("");
			if (typeof(object.names) != "undefined") {
				txtDepName2.setValue(object.names);
			}
			if (typeof(object.codes) != "undefined") {
				hdnDepCode.setValue(object.codes);
				drpName.store.load({
				    params : {
				    	strDeptCode : object.codes
				    }
				});
			}
		}
	}
	// 查询按钮处理函数
	function queryIt2() {
		store2.baseParams.strDepCode = hdnDepCode.getValue();
		store2.baseParams.strWorkerCode = drpName.getValue();
		store2.baseParams.strLicence = drpLicence2.getValue();
		Ext.Ajax.request({
					url : "administration/driverFileQuery.action",
					method : "post",
					params : {
						start : 0,
						limit : 18,
						strDepCode : hdnDepCode.getValue(),
						strWorkerCode : drpName.getValue(),
						strLicence : drpLicence2.getValue()
					},
					success : function(result, request) {
						var gridData = eval('(' + result.responseText + ')');
						if((result.msg != null) && (result.msg == Constants.SQL_FAILURE)){
								Ext.Msg.alert(Constants.NOTICE,MessageConstants.COM_E_014);
								return;
						}
						if((result.msg != null) && (result.msg == Constants.DATE_FAILURE)){
								Ext.Msg.alert(Constants.NOTICE,MessageConstants.COM_E_023);
								return;
						}
						store2.loadData(gridData);
					},
					failure : function(result, request) {
						Ext.Msg.alert(Constants.NOTICE,
								MessageConstants.UNKNOWN_ERR);
					}
				});
	}
	function showSex(value){
		if(value == "M"){
			return "男";
		}
		if(value == "W"){
			return "女";
		}
		return "";
	}
	// grid选择模式设为单行选择模式
    var sm2 = new Ext.grid.RowSelectionModel({
	       singleSelect : true
	});
	// grid中的列
	var cm2 = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
						header : "行号",
						width : 35
					}), {
				header : "ID",
				width : 75,
				sortable : true,
				dataIndex : 'id',
				hidden : true
			}, {
				header : "姓名",
				dataIndex : "name"
			}, {
				header : "性别",
				width : 75,
				sortable : true,
				dataIndex : 'sex',
				renderer : showSex
			}, {
				header : "年龄",
				width : 75,
				sortable : true,
				dataIndex : 'ages'
			}, {
				header : "所在部门",
				dataIndex : "depName",
				align : "left"
			}, {
				header : "驾照类型",
				dataIndex : "licence",
				align : "left"
			}, {
				header : "驾照号码",
				width : 75,
				sortable : true,
				dataIndex : 'licenceNo'
			}, {
				header : "办照时间",
				dataIndex : "licenceDate",
				align : "left"
			}, {
				header : "年检时间",
				dataIndex : "checkDate",
				align : "left"
			}, {
				header : "手机号码",
				width : 75,
				sortable : true,
				dataIndex : 'mobileNo'
			}, {
				header : "家庭电话",
				width : 75,
				sortable : true,
				dataIndex : 'telNo'
			}, {
				header : "家庭住址",
				width : 75,
				sortable : true,
				dataIndex : 'homeAddr'
			}, {
				header : "通讯地址",
				width : 75,
				sortable : true,
				dataIndex : 'comAddr'
			}]);
	cm2.defaultSortable = true;
	// grid中的数据
	var recordDriver = Ext.data.Record.create([{
				name : "id"
			}, {
				name : "name"
			}, {
				name : "sex"
			}, {
				name : "ages"
			}, {
				name : "depName"
			}, {
				name : "licence"
			}, {
				name : "licenceNo"
			}, {
				name : "licenceDate"
			}, {
				name : "checkDate"
			}, {
				name : "mobileNo"
			}, {
				name : "telNo"
			}, {
				name : "homeAddr"
			}, {
				name : "comAddr"
			}]);
	// grid中的store
	var store2 = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "administration/driverFileQuery.action"
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, recordDriver)
			});
	store2.baseParams = ({
		strDepCode : hdnDepCode.getValue(),
		strWorkerCode : drpName.getValue(),
		strLicence : drpLicence2.getValue()
	});
	// 注册store的load事件
	store2.on("load", function() {
				if (this.getTotalCount() > 0) {
					btnExport.setDisabled(false);
				} else {
					Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
							MessageConstants.COM_I_003);
					btnExport.setDisabled(true);
				}
			});
	// 底部工具栏
	var bbar2 = new Ext.PagingToolbar({
				pageSize : 18,
				store : store2,
				displayInfo : true,
				displayMsg : MessageConstants.DISPLAY_MSG,
				emptyMsg : MessageConstants.EMPTY_MSG
			})
	// grid主体
	var grid2 = new Ext.grid.GridPanel({
				autoScroll : true,
				region : "center",
				layout : "fit",
				colModel : cm2,
				sm : sm2,
				tbar : tbar2,
				bbar : bbar2,
				enableColumnMove : false,
				store : store2,
				autoSizeColumns : true,
				autoSizeHeaders : false
			});
	// 设定布局器及面板
	var layout2 = new Ext.Viewport({
				layout : "border",
				border : false,
				autoScroll : true,
				enableTabScroll : true,
				items : [grid2]
			});
})