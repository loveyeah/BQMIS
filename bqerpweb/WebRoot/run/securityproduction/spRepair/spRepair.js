// isQuery  是否为选择 true:是  
// type 类别
SpRepair = function(isQuery,type){
	
	var label1 = new Ext.form.Label({
		text : '检验时间：'
	})
	var queryStartTime = new Ext.form.TextField({
		id : 'startTime',
		readOnly : true,
		width : 80,
		listeners : {
			focus : function(){
				WdatePicker({
					startDate : '%y-%m-%d',
					dateFmt : 'yyyy-MM-dd'
				})
			}
		}
	})
	
	var label2 = new Ext.form.Label({
		text : '至：'
	})
	var queryEndTime = new Ext.form.TextField({
		id : 'endTime',
		readOnly : true,
		width : 80,
		listeners : {
			focus : function(){
				WdatePicker({
					startDate : '%y-%m-%d',
					dateFmt : 'yyyy-MM-dd'
				})
			}
		}
	})
	var label3 = new Ext.form.Label({
		text : '编号：'
	})
	var queryCode = new Ext.form.TextField({
		id : 'queryCode'
	})
	
	
	var queryBtu = new Ext.Button({
		id : 'queryBtu',
		text : '查询',
		iconCls : 'query',
		handler : queryFun
	})
	function queryFun() {
		repairStore.load({
					params : {
						start : 0,
						limit : 18
					}
				})
	}
	var addBtu = new Ext.Button({
		id : 'addBtu',
		text : '新增',
		iconCls : 'add',
		handler : addFun
	})
	function addFun(){
		editWin.setTitle('新增电气安全用具检修记录')
		editWin.show();
		formPanel.getForm().reset()
	}
	var updateBtu = new Ext.Button({
		id : 'updateBtu',
		text  : '修改',
		iconCls  : 'update',
		handler : updateFun
	})
	function updateFun(){
		if(sm.hasSelection()){
			if(sm.getSelections().length > 1)
				Ext.Msg.alert('提示','请选择其中一条数据!');
			else
			{
				editWin.setTitle('修改电气安全用具检修记录')
				editWin.show();
				formPanel.getForm().loadRecord(sm.getSelected())
				tool.setValue(sm.getSelected().get('toolId'),sm.getSelected().get('toolName'));
				toolType.setValue('电气安全用具')
				person.setValue(sm.getSelected().get('repairBy'),sm.getSelected().get('repairByName'))
//				formPanel.getForm().setValues(sm.getSelected().data)
			}
		}else
			Ext.Msg.alert('提示','请先选择要修改的数据！')
	}
	var saveBtu = new Ext.Button({
		id : 'saveBtu',
		text : '保存',
		iconCls : 'save',
		handler : saveFun
	})
	function saveFun(){
		if(formPanel.getForm().isValid()){
			if(belongDep.getValue() == null || belongDep.getValue() == ''){
				Ext.Msg.alert('提示','请选择一具体部门！');
				return;
			}
			Ext.Msg.confirm("提示",'确认要保存数据吗？',function(buttonId){
				if(buttonId == 'yes'){
					formPanel.getForm().submit({
						url : 'security/saveRepairEntity.action',
						method : 'post',
						success : function(form,action){
							if(action && action.response && action.response.responseText)
							{
								var res = Ext.decode(action.response.responseText)
								Ext.Msg.alert('提示',res.msg)
							}
							editWin.hide();
							queryFun();
						},
						failure : function(form,action){
							Ext.Msg.alert('提示','保存出现异常！')
						}
					})
				}
			})
		}
	}
	var cancelBtu = new Ext.Button({
		id  : "cancelBtu",
		text : '取消',
		iconCls : 'cancer',
		handler : cancelFun
	})
	function cancelFun(){
		formPanel.getForm().reset()
		editWin.hide()
	}
	var deleteBtu = new Ext.Button({
		id : 'deleteBtu',
		text : '删除',
		iconCls : 'delete',
		handler : deleteFun
	})
	function deleteFun() {
		if (sm.hasSelection()) {
			Ext.Msg.confirm('提示', '确认要删除数据吗？', function(buttonId) {
				if (buttonId == 'yes') {
					var ids = new Array();
					var selects = sm.getSelections();
					for (var i = 0; i < selects.length; i++) {
						ids.push(selects[i].get('repairId'))
					}
					if (ids.length > 0) {
						Ext.Ajax.request({
									url : 'security/deleteRepairEntity.action',
									method : 'post',
									params : {
										ids : ids.join(",")
									},
									success : function(response, options) {
										if (response && response.responseText) {
											var res = Ext
													.decode(response.responseText)
											Ext.Msg.alert('提示', res.msg);
											queryFun()
										}
									},
									failure : function(response, options) {
										Ext.Msg.alert('提示', '删除数据出现异常！')
									}
								})
					}
				}
			})

		} else
			Ext.Msg.alert('提示', '请先选择要删除的数据！')
	}
	var exportBtu = new Ext.Button({
		id : 'exportBtu',
		text : '导出',
		iconCls : 'export',
		handler : exportFun
	})
		// 导出
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
	function myExport() {
		if (repairStoreCopy.getTotalCount() == 0) {
			Ext.Msg.alert('提示', '无数据可导出！')
		} else {
			var html = ['<table border=1><tr><th>编号</th><th>名称</th><th>规格型号</th><th>类别</th><th>所属部门</th><th>检验结果</th><th>检验开始时间</th><th>检验结束时间</th>'
					+ '<th>检验人</th><th>检验部门</th><th>下次检验时间</th><th>备注</th></tr>'];
			for (var i = 0; i < repairStoreCopy.getTotalCount(); i++) {
				var rc = repairStoreCopy.getAt(i);
				var typeName = rc.get('toolType') == null ? '' : rc
						.get('toolType');
				if (typeName) {
					if (typeName == 1)
						typeName = '电气安全用具';
					else if (typeName == 2)
						typeName = '电动工具';
					else if (typeName == 3)
						typeName = '安全带清册';
				}
				html.push('<tr><td>' + (rc.get('toolCode') == null ? "" : rc.get('toolCode')) + '</td>' + '<td>'
						+(rc.get('toolName') == null ? "" : rc.get('toolName')) + '</td>' + '<td>'
						+(rc.get('toolModel') == null ? "" : rc.get('toolModel')) + '</td>'
						+ '<td>' + typeName+ '</td>' 
						+ '<td>' + (rc.get('belongDepName') == null ? "" : rc.get('belongDepName')) + '</td>'
						+ '<td>' +(rc.get('repairResult') == null ? "" : rc.get('repairResult'))  + '</td>' + '<td>'
						+(rc.get('repairBegin') == null ? "" : rc.get('repairBegin')) + '</td>' + '<td>'
						+ (rc.get('repairEnd') == null ? "" : rc.get('repairEnd')) + '</td>' + '<td>'
						+(rc.get('repairByName') == null ? "" : rc.get('repairByName'))  + '</td>' + '<td>'
						+ (rc.get('repairDepName') == null ? "" : rc.get('repairDepName')) + '</td>' + '<td>'
						+(rc.get('nextTime') == null ? "" : rc.get('nextTime')) + '</td>' + '<td>'
						+(rc.get('memo') == null ? "" : rc.get('memo')) + '</td>' + '</tr>')

			}
			html.push('</table>');
			html = html.join(''); // 最后生成的HTML表格
			tableToExcel(html);
		}

	}
	
	function exportFun(){
		if(repairStoreCopy.getTotalCount() == 0){
			repairStoreCopy.load()
		}else{
			repairStoreCopy.reload();
		}
	}
	
	var tbar = new Ext.Toolbar({
		items : [label1,queryStartTime,label2,queryEndTime,label3,queryCode,queryBtu,addBtu,updateBtu,deleteBtu,exportBtu]
	})
	var RepairRecord = new Ext.data.Record.create([{
		name : 'repairId',
		mapping : 0
	},{
		name : 'toolId',
		mapping :1
	},{
		name : 'belongDep',
		mapping :2
	},{
		name : 'belongDepName',
		mapping :3
	},{
		name : 'repairResult',
		mapping :4
	},{
		name : 'repairBegin',
		mapping :5
	},{
		name : 'repairEnd',
		mapping :6
	},{
		name : 'repairBy',
		mapping :7
	},{
		name : 'repairByName',
		mapping :8
	},{
		name : 'repairDep',
		mapping :9
	},{
		name : 'repairDepName',
		mapping :10
	},{
		name : 'nextTime',
		mapping :11
	},{
		name : 'memo',
		mapping :12
	},{
		name : 'toolCode',
		mapping :13
	},{
		name : 'toolName',
		mapping :14
	},{
		name : 'toolType',
		mapping :15
	},{
		name : 'toolModel',
		mapping :16
	},{
		name : 'fillBy',
		mapping :17
	},{
		name : 'fillByName',
		mapping :18
	}])
	
	var repairStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'security/findToolsRepairObject.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list',
			totalProperty : 'totalCount'
		},RepairRecord)
		
	})
	repairStore.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							beginTime : queryStartTime.getValue(),
							endTime : queryEndTime.getValue(),
							toolCode : queryCode.getValue(),
							toolType : type,
							isMaint : (isQuery != null && isQuery) ? null : "1" 
						})
			})
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	})
	
	var cm = new Ext.grid.ColumnModel([sm,new Ext.grid.RowNumberer({
		header : '行号',
		width : 35
	}),{
		header : '编号',
		dataIndex : 'toolCode'
	},{
		header : '名称',
		dataIndex : 'toolName'
	},{
		header : '规格型号',
		dataIndex : 'toolModel'
	},{
		header : '类别',
		dataIndex : 'toolType',
		renderer : function(v){
			if(v == 1)
				return '电气安全用具';
			else if(v == 2)
				return '电动工具';
			else if(v == 3)
				return '安全带清册';
			else	
				return '';
		}
	},{
		header : '所属部门',
		dataIndex : 'belongDepName'
	},{
		header : '检验结果',
		dataIndex : 'repairResult'
	},{
		header : '检验开始时间',
		dataIndex : 'repairBegin'
	},{
		header : '检验结束时间',
		dataIndex : 'repairEnd'
	},{
		header : '检验人',
		dataIndex : 'repairByName'
	},{
		header : '检验部门',
		dataIndex : 'repairDepName'
	},{
		header : '下次检验时间',
		dataIndex : 'nextTime'
	},{
		header : '备注',
		dataIndex : 'memo'
	}])

	var bbar = new Ext.PagingToolbar({
						pageSize : 18,
						store : repairStore,
						displayInfo : true,
						displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
						emptyMsg : "没有记录"
					})
	var grid = new Ext.grid.GridPanel({
		id  : 'grid',
		frame : 'true',
		border : false,
		layout : 'fit',
		autoScroll : true,
		sm : sm,
		store : repairStore,
		cm : cm,
		tbar : tbar,
		bbar : bbar
	})
	
	
	// 检修记录id
	var repairId = new Ext.form.Hidden({
		id : 'repairId',
		name : 'repair.repairId'
	})
	// 工具ID
	var tool = new Tool({
				fieldLabel : '电气安全用具',
				hiddenName : 'repair.toolId',
				anchor : '90%',
				allowBlank : false
			}, true, 1, true)
	tool.init()
	tool.confirmBtu.on('click',function(){
		var result = tool.confirmFun();
		if(result){
			{				
				toolCode.setValue(result.get('toolCode'))
				toolModel.setValue(result.get('toolModel'))
			}
		}
	})
//	tool.combo
	//编号
	var toolCode = new Ext.form.TextField({
		id  : 'toolCode',
		name : 'toolCode',
		fieldLabel : '编号',
		disabled : true,
		anchor : '90%'
	})

	var toolType = new Ext.form.TextField({
		id : 'toolType',
		fieldLabel : '类别',
		anchor : '90%',
		value : '电气安全用具',
		disabled : true,
		readOnly : true
	})
	//规格型号
	var toolModel = new Ext.form.TextField({
		id : 'toolModel',
		name : 'toolModel',
		fieldLabel : '规格型号',
		disabled : true,
		anchor : '90%'
	})
	
	// 所属部门
	var belongDep = new Ext.form.Hidden({
		id : 'belongDep',
		name : 'repair.belongDep'
	})
	var dept = new Power.dept(null, false, {
				fieldLabel : '所属部门',
				hiddenName : 'belongDepName',
				anchor : '90%',
				allowBlank : false
			});
	dept.btnConfrim.on('click', function() {
				var deptRes = dept.getValue();
				if (deptRes.code != null) {
					belongDep.setValue(deptRes.code)
				} else {
					belongDep.setValue(null);
				}
			})
	// 检验结果
	var repairResult = new Ext.form.TextArea({
		id : 'repairResult',
		name : 'repair.repairResult',
		fieldLabel : '检验结果',
		anchor : '90%',
		height : 75,
		allowBlank : false
	})
	// 检修开始时间
	var repairBegin = new Ext.form.TextField({
		id : 'repairBegin',
		name : 'repair.repairBegin',
		fieldLabel : '检修开始时间',
		style : 'cursor:pointer',
		anchor : '90%',
		listeners : {
			focus : function(){
				WdatePicker({
					startDate : '%y-%m-%d',
					dateFmt : 'yyyy-MM-dd'
				})
			}
		}
	})
	// 检修结束时间
	var repairEnd = new Ext.form.TextField({
		id : 'repairEnd',
		name : 'repair.repairEnd',
		fieldLabel : '检修结束时间',
		style : 'cursor:pointer',
		anchor : '90%',
		listeners : {
			focus : function(){
				WdatePicker({
					startDate : '%y-%m-%d',
					dateFmt : 'yyyy-MM-dd'
				})
			}
		}
	})
	// 检验人  
	var person = new Power.person({
				fieldLabel : '检验人',
				hiddenName : 'repair.repairBy',
				anchor : '90%'
			}, {selectModel:'single'})
	person.btnConfirm.on('click',function(){
		var personRes = person.chooseWorker();
		if(personRes){
			repairDep.setValue(personRes.get("deptCode"));
			repairDepName.setValue(personRes.get("deptName"))
		}else{
			repairDep.setValue(null);
			repairDepName.setValue(null)
		}
	})
	//person.combo
	//检验部门
	var repairDep = new Ext.form.Hidden({
		id :'repairDep',
		name : 'repair.repairDep'
	})
	var repairDepName = new Ext.form.TextField({
		id : 'repairDepName',
		disabled : true,
		fieldLabel : '检验部门',
		anchor : '90%'
	})
	// 下次检验时间
	var nextTime = new Ext.form.TextField({
		id : 'nextTime',
		name : 'repair.nextTime',
		fieldLabel : '下次检验时间',
		style : 'cursor:pointer',
		anchor : '90%',
		listeners : {
			focus : function(){
				WdatePicker({
					startDate : '%y-%m-%d',
					dateFmt : 'yyyy-MM-dd'
				})
			}
		}
	})
	//备注
	var memo = new Ext.form.TextArea({
		id : 'memo',
		name : 'repair.memo',
		fieldLabel : '备注',
		anchor : '90%',
		height : 75
	})
	
	var formPanel = new Ext.form.FormPanel({
		id : 'formPanel',
		frame : true,
		border : false,
		layout : 'column',
		buttons : [saveBtu,cancelBtu],
		buttonAlign : 'center',
		labelAlign : 'right',
		labelWidth : 80,
		defaults : {
			layout : 'form',
			frame : false,
			border : false
		},
		items : [{
			columnWidth : 0.5,
			items : [repairId,tool.combo,toolType,dept.combo]
		},{
			columnWidth : 0.5,
			items : [toolCode,toolModel,belongDep]
		},{
			columnWidth : 1,
			items : [repairResult]
		},{
			columnWidth : 0.5,
			items : [repairBegin,person.combo,repairDep,nextTime]
		},{
			columnWidth : 0.5,
			items : [repairEnd,repairDepName]
		},{
			columnWidth : 1,
			items : [memo]
		}]
	})
	
	var editWin = new Ext.Window({
		id : 'editWin',
		width : 500,
		height : 400,
		items : [formPanel],
		modal : true,
		closeAction  : 'hide'
	})
	
	
	var repairStoreCopy = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'security/findToolsRepairObject.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list',
			totalProperty : 'totalCount'
		},RepairRecord)
		
	})
	repairStoreCopy.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							beginTime : queryStartTime.getValue(),
							endTime : queryEndTime.getValue(),
							toolCode : queryCode.getValue(),
							toolType : type,
							isMaint : (isQuery != null && isQuery) ? null : "1"
						})
			})
	repairStoreCopy.on('load',myExport)
	
	this.grid = grid;
	this.init = function(){
		if(isQuery != null && isQuery){
			addBtu.setVisible(false)
			updateBtu.setVisible(false)
			deleteBtu.setVisible(false)
			grid.purgeListeners()
		}else{
			exportBtu.setVisible(false)
			grid.on('rowdblclick',updateFun)
		}
		queryFun()
	};
}