Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	// 系统当前时间
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";

		return s;
	}

	// 从session取登录人编码姓名部门相关信息
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号，赋给全局变量
							fillBy.setValue(result.workerCode);
							fillName.setValue(result.workerName);

						}
					}
				});
	}
	//监督专业ID
	
	
	//成员监督网职务
	var netDuty = new Ext.form.ComboBox({
		fieldLabel : "监督网职务",
		store : [['1', '组长'], ['2', '副组长'], ['3', '专职'], ['4', '成员']],
		//id : 'toQuarterBox',
		name : 'toQuarterBoxName',
		valueField : "value",
		displayField : "text",
		//hideLabel : true,
		mode : 'local',
		readOnly : true,
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'model.netDuty',
		editable : false,
		disabled : true,
		triggerAction : 'all',
		width : 160,
		selectOnFocus : true
	});
	// 成员姓名
	var workerName = new Ext.form.TextField({
		fieldLabel : "成员姓名",
		readOnly : true,
		name : 'workerName',
		width : 160
	})
	//成员所在部门名称
	var deptName = new Ext.form.TextField({
		id : "deptName",
		fieldLabel : '所在部门',
		name : 'deptName',
		readOnly : true,
		//allowBlank : false,
		width : 160
	})
	// 成员编码
	var workerCode = new Ext.form.Hidden({
		name : 'model.workerCode'

			})
	// 主键
	var jdwcyId = new Ext.form.Hidden({
		name : 'model.jdwcyId'
	})
	// 弹窗的表单对象
	var blockForm = new Ext.form.FormPanel({
				labelAlign : 'right',
				frame : true,
				labelWidth : 80,
				style : 'padding:10px,5px,0px,5px',
				//layout : 'column',
				closeAction : 'hide',
				fileUpload : true,
				items : [workerName,deptName,netDuty,workerCode,jdwcyId]

			});

	//弹出窗体
	var win = new Ext.Window({
		width : 300,
		height : 150,
		buttonAlign : "center",
		items : [blockForm],
		layout : 'fit',
		closeAction : 'hide',
		resizable : false,
		modal : true,
		buttons : [ {
			text : '取消',
			iconCls:'cancer',
			handler : function() { 
				win.hide();
			}
		}]

	});
	//显示所在监督网
	var jdzyName = new Ext.form.TextField({
		id : 'jdzyName',
		readOnly : true,
		//anchor : '90%'
		width : 120
		
	})
	
	// 刷新按钮
	var westbtnref = new Ext.Button({
		text : '刷新',
		iconCls : 'reflesh',
		handler : function() {
		queryRecord();
		}
	});

	var westsm = new Ext.grid.CheckboxSelectionModel();
	// 左边列表中的数据
	var datalist = new Ext.data.Record.create([

	{
				name : 'model.jdwcyId'
			}, {
				name : 'model.workerCode'
			}, {
				name : 'model.jdzyId'
			}, {
				name : 'model.netDuty'
			}, {
				name : 'workerName'
			}, {
				name : 'deptName'
			}]);

	var westgrids = new Ext.data.JsonStore({
				url : 'productionrec/findPtJJdwcyList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});

	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
				ds : westgrids,
				columns : [westsm, new Ext.grid.RowNumberer(), {
							header : "姓名",
							width : 40,
							//align : "center",
							sortable : true,
							dataIndex : 'workerName'
						}, {
							header : "所在部门",
							width : 40,
							//align : "center",
							sortable : false,
							dataIndex : 'deptName'
						}, {
							header : "监督网职务",
							width : 40,
							//align : "center",
							sortable : true,
							dataIndex : 'model.netDuty',
							renderer : function(v){
								if (v == 1){
									return "组长";
								}
								if (v == 2){
									return "副组长";
								}
								if (v == 3){
									return "专职";
								}
								if (v == 4){
									return "成员";
								}
							}
						}],
				viewConfig : {
			                 forceFit : true
		           },
				tbar : [jdzyName,{
							xtype : "tbseparator"
						}, westbtnref,{
							xtype : "tbseparator"
						},{
			               text : "查看详细信息",
			               iconCls : 'list',
			              handler : function() {updateRecord()}
		                 }],
				sm : westsm,
				frame : true,
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : westgrids,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						}),
				border : true,
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});

	// westgrid 的事件
	westgrid.on("rowdblclick", updateRecord);
	function queryRecord()
	{
		westgrids.baseParams = {
			jdzyId : jdId
		}	
		westgrids.load({
			params : {				
				start : 0,
				limit : 18				
			}
		});
		Ext.get("jdzyName").dom.value = name;
	}
	function updateRecord()
	{
			if (westgrid.selModel.hasSelection()) {
		
			var records = westgrid.getSelectionModel().getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				  win.show(); 
				var record = westgrid.getSelectionModel().getSelected();
		        blockForm.getForm().reset();
		        blockForm.form.loadRecord(record);
				win.setTitle("查看监督网成员信息");
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要查看的记录!");
		}
	}
	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							region : 'center',
							layout : 'fit',
							border : false,
							margins : '1 0 1 1',
							collapsible : true,
							items : [westgrid]

						}]
			});
			queryRecord();
});