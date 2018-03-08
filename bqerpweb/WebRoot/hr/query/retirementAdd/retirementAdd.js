var start=0;
var limit=18;
Ext.onReady(function() {
    var nowDate = new Date();
	// 计算月份数
	function MonthDiff(sDate1, sDate2) {
		// sDate1和sDate2是年-月-日格式
		var arrDate, objDate1, objDate2, intDays;
		arrDate = sDate1.split("-");
		objDate1 = new Date(arrDate[1] + '-' + arrDate[2] + '-' + arrDate[0]);// 转换为月-日-年格式
		arrDate = sDate2.split("-");
		objDate2 = new Date(arrDate[1] + '-' + arrDate[2] + '-' + arrDate[0]);
		intDays = parseInt(Math.abs(objDate1 - objDate2) / 1000 / 60 / 60 / 24); // 把相差的毫秒数转换为天数
		intMonths=parseInt(intDays/30);
		return intMonths;
	}
	
	function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";
		// 初始化时间
		Year = DateIn.getYear();
		Month = DateIn.getMonth() + 1;
		Day = DateIn.getDate();
		CurrentDate = Year + "-";
		if (Month >= 10) {
			CurrentDate = CurrentDate + Month;
		} else {
			CurrentDate = CurrentDate + "0" + Month+ "-";
		}
		if (Day >= 10) {
			CurrentDate = CurrentDate + Day;
		} else {
			CurrentDate = CurrentDate + "0" + Day;
		}
		return CurrentDate;
	}
	var enddate = new Date();
	var startdate = enddate.add(Date.MONTH, 6);
	startdate = startdate.getFirstDateOfMonth();
	var retirementDate = new Ext.form.TextField({
		id : 'retirementDate',
		fieldLabel : '退休日期',
		style : 'cursor:pointer',
		value : ChangeDateToString(startdate),
		readOnly : true,
		width : 85,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M',
					dateFmt : 'yyyy-MM',
					alwaysUseStartDate : true,
					isShowClear : false
				});
				this.blur();
			}
		}
	});
	//部门选择
	var queydeptId = new Ext.ux.ComboBoxTree({
			fieldLabel : '所属部门',
			id : 'deptId',
			displayField : 'text',
			valueField : 'id',
			hiddenName : 'empinfo.deptId',
			blankText : '请选择',
			emptyText : '请选择', 
			resizable:true,
			width : 250,
			// value:{id:'0',text:'合肥电厂',attributes:{description:'deptName'}},
			tree : {
				xtype : 'treepanel',
				autoScroll : false,
				loader : new Ext.tree.TreeLoader({
					dataUrl : 'empInfoManage.action?method=getDep&flag=roleQuery'
				}),
				root : new Ext.tree.AsyncTreeNode({
					id : '0',
					//name : '灞桥热电厂',
					text : '灞桥热电厂'
				})
			},
			selectNodeModel : 'all'
	});
	// -------------------- 定义grid--------------------------
	var MyRecord = Ext.data.Record.create([{
		name : 'empId',
		mapping : 0
	}, {
		name : 'empCode',
		mapping : 1
	}, {
		name : 'chsName',
		mapping : 2
	}, {
		name : 'sex',
		mapping : 3
	}, {
		name : 'deptName',
		mapping : 4
	}, {
		name : 'brithday',
		mapping : 5
	}, {
		name : 'age',
		mapping : 6
	}, {
		name : 'missionDate',
		mapping : 7
	}, {
		name : 'stationName',
		mapping : 8
	}, {
		name : 'retirementDate',
		mapping : 9
	}, {
		name : 'countDown',
		mapping : 10
	}, {
		name : 'workAge',
		mapping : 11
	}, {
		name : 'isSpecialTrades',
		mapping : 12
	}, {
		name : 'isCadres',
		mapping : 13
	}, {
		name : 'standard',
		mapping : 14
	}, {
		name : 'workDate',
		mapping : 15
	},{
		name : 'politicsId'
		
	},{
		name : 'politicsName'
	
	},{
		name : 'stationId',
		mapping : 16
	},{
		name : 'deptId',
		mapping : 17
	},{
		name : 'isRetired',
		mapping : 18
	}]);
   	
	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'com/finRetirementByDeptAndName.action ',
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list'
		}, MyRecord)
	});
		// 计算工龄
	//update by sychen 20100817
		function getWorkAge(argDate) {
		var value = argDate;
		if (!value) {
			return '';
		}
		if (value instanceof Date) {
			value = value.dateFormat('Y-m');
		}
		var now = new Date();
		if ((now.dateFormat('Y-m').substring(5, 7) - Number(value.substring(5,
				7))) > 0) {
			var age = now.dateFormat('Y') - Number(value.substring(0, 4)) + 1;
		}
		else{
		    var age = now.dateFormat('Y') - Number(value.substring(0, 4));
		}
		return age;
	}

function getAge(argDate) {
		var value = argDate;
		if (!value) {
			return '';
		}
		if (value instanceof Date) {
			value = value.dateFormat('Y-m');
		}
		var now = new Date();
		if ((now.dateFormat('Y-m').substring(5, 7) - Number(value.substring(5,
				7))) > 0) {
			var age = now.dateFormat('Y') - Number(value.substring(0, 4)) + 1;
		}
		else{
		    var age = now.dateFormat('Y') - Number(value.substring(0, 4));
		}
		return age;
	}
//	function getWorkAge(argDate) {
//		var value = argDate;
//		if (!value) {
//			return '';
//		}
//		if (value instanceof Date) {
//			value = value.dateFormat('Y-m');
//		}
//		var now = new Date();
//		var age = now.dateFormat('Y') - Number(value.substring(0, 4)) + 1;
//		return age;
//	}
//
//function getAge(argDate) {
//		var value = argDate;
//		if (!value) {
//			return '';
//		}
//		if (value instanceof Date) {
//			value = value.dateFormat('Y-m');
//		}
//		var now = new Date();
//		var age = now.dateFormat('Y') - Number(value.substring(0, 4)) + 1;
//		// age += Math.ceil((now.dateFormat('m') - Number(value.substring(5,
//		// 7))) /12);
//		return age;
//	}
	
	//update by sychen 20100817 end 
	var sm = new Ext.grid.CheckboxSelectionModel({
	   singleSelect : true
	});
	//查询按钮
	var query=new Ext.Button({
		text : "查询",
					iconCls : 'query',
					minWidth : 70,
					handler : queryRecord
	});
	//导出按钮
	var Export=new Ext.Button({
		id : 'btnPrint',
		text : '导 出',
		iconCls : 'export',
		handler:function(){
					var deptId=queydeptId.getValue();
					var empName=nameField.getValue();
					var url = "com/retirementExport.action?deptId="+deptId+"&empName="+empName;
					window.open(url);
		}
		
//		handler : function() {
//			reportexport();
//		}
	});
	//修改按钮
	var update=new Ext.Button({
		id : 'update',
		text : '修改',
		iconCls : 'update',
		handler : function() {
			updateRecord();
		}
	});
	//姓名（模糊查询）
	var nameField = new Ext.form.TextField({
				id : 'nameField',
				//name : 'empinfo.chsName',
//				width : 150,
				anchor : '100%'
			});
//	//部门Code
//	var deptCodeField = new Ext.form.TextField({
//				id : 'deptCode',
//				//name : 'empinfo.chsName',
////				width : 150,
//				anchor : '100%'
//			});
	//
	store.baseParams = {
			deptCode : queydeptId.getValue(),
			empName:nameField.getValue()
		};
	//上部工具栏
	var gridTbar = new Ext.Toolbar({
				items : ["部门:", queydeptId,"姓名：" ,nameField,"-",query,"-",Export,"-",update]
			});	
	//底部分页工具栏		 
	var bbar=new Ext.PagingToolbar({
				pageSize : limit,
				store : store,
				displayInfo : true,
				displayMsg : '第{0}条到第{1}条记录，共 {2} 条',
				emptyMsg : "没有记录"
			});
	//页面主体
	var grid = new Ext.grid.GridPanel({
		region : "center",
		ds : store,
		columns : [
		sm, new Ext.grid.RowNumberer({
			header : '行号',
			width : 35
		}), {
			header : "员工工号",
			width : 80,
			sortable : true,
			dataIndex : 'empCode',
			renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						  //update by sychen 20100817
					        var standard = record.get('standard');
							var a=MonthDiff(record.get('brithday').substring(0,10),ChangeDateToString(nowDate).substring(0,10))*(-1)+standard*12
							if(a<0||a==0){
							 if(v!=null)
								return "<font color='red'>" + v + "</font>";
								else
								return "";
							}
							else if((a>1||a===1)&&a<6){
							    if(v!=null)
							    return "<font color='yellow'>" + v + "</font>";
								else
								return "";
							}	
							else if((a>6||a==6)&&(a<12||a==12)){
							    if(v!=null)
							    return "<font color='green'>" + v + "</font>";
								else
								return "";
							}
							else{
							    if(v!=null)
							    return "<font color='black'>" + v + "</font>";
								else
								return "";
							}
							
//							var standard = record.get('standard');
//							var age = record.get('age');
//							var a = standard-age;
//							alert(MonthDiff(ChangeDateToString(nowDate).substring(0,10),record.get('brithday').substring(0,10)))
//							if (a>0) {
//								if(v!=null)
//								return "<font color='darkorange'>" + v + "</font>";
//								else
//								return "";
//							} else 
//							{
//								if(v!=null)
//								return "<font color='red'>" + v + "</font>";
//								else
//								return"";
//							}
							
							
						}
		}, {
			header : "中文名",
			width : 80,
			sortable : true,
			dataIndex : 'chsName',
			renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) { 
							//update by sychen 20100817
					        var standard = record.get('standard');
							var a=MonthDiff(record.get('brithday').substring(0,10),ChangeDateToString(nowDate).substring(0,10))*(-1)+standard*12
							if(a<0||a==0){
							 if(v!=null)
								return "<font color='red'>" + v + "</font>";
								else
								return "";
							}
							else if((a>1||a===1)&&a<6){
							    if(v!=null)
							    return "<font color='yellow'>" + v + "</font>";
								else
								return "";
							}	
							else if((a>6||a==6)&&(a<12||a==12)){
							    if(v!=null)
							    return "<font color='green'>" + v + "</font>";
								else
								return "";
							}
							else{
							    if(v!=null)
							    return "<font color='black'>" + v + "</font>";
								else
								return "";
							}
//							var standard = record.get('standard');
//							var age = record.get('age');
//							var a = standard-age;
//							if (a>0) {
//								if(v!=null)
//								return "<font color='darkorange'>" + v + "</font>";
//								else
//								return "";
//							} else 
//							{
//								if(v!=null)
//								return "<font color='red'>" + v + "</font>";
//								else
//								return"";
//							}
							
						}
		}, {
			header : "性别",
			width : 45,
			sortable : true,
			dataIndex : 'sex',
			renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
							//update by sychen 20100817
					        var standard = record.get('standard');
							var a=MonthDiff(record.get('brithday').substring(0,10),ChangeDateToString(nowDate).substring(0,10))*(-1)+standard*12
							if(a<0||a==0){
							 if(v!=null)
								return "<font color='red'>" + v + "</font>";
								else
								return "";
							}
							else if((a>1||a===1)&&a<6){
							    if(v!=null)
							    return "<font color='yellow'>" + v + "</font>";
								else
								return "";
							}	
							else if((a>6||a==6)&&(a<12||a==12)){
							    if(v!=null)
							    return "<font color='green'>" + v + "</font>";
								else
								return "";
							}
							else{
							    if(v!=null)
							    return "<font color='black'>" + v + "</font>";
								else
								return "";
							}
//							var standard = record.get('standard');
//							var age = record.get('age');
//							var a = standard-age;
//							if (a>0) {
//								if(v!=null)
//								return "<font color='darkorange'>" + v + "</font>";
//								else
//								return "";
//							} else 
//							{
//								if(v!=null)
//								return "<font color='red'>" + v + "</font>";
//								else
//								return"";
//							}
							
						}
		}, {
			header : "部门",
			width : 90,
			sortable : true,
			dataIndex : 'deptName',
			renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						
							//update by sychen 20100817
					        var standard = record.get('standard');
							var a=MonthDiff(record.get('brithday').substring(0,10),ChangeDateToString(nowDate).substring(0,10))*(-1)+standard*12
							if(a<0||a==0){
							 if(v!=null)
								return "<font color='red'>" + v + "</font>";
								else
								return "";
							}
							else if((a>1||a===1)&&a<6){
							    if(v!=null)
							    return "<font color='yellow'>" + v + "</font>";
								else
								return "";
							}	
							else if((a>6||a==6)&&(a<12||a==12)){
							    if(v!=null)
							    return "<font color='green'>" + v + "</font>";
								else
								return "";
							}
							else{
							    if(v!=null)
							    return "<font color='black'>" + v + "</font>";
								else
								return "";
							}
//							var standard = record.get('standard');
//							var age = record.get('age');
//							var a = standard-age;
//							if (a>0) {
//								if(v!=null)
//								return "<font color='darkorange'>" + v + "</font>";
//								else
//								return "";
//							} else 
//							{
//								if(v!=null)
//								return "<font color='red'>" + v + "</font>";
//								else
//								return"";
//							}
							
						}
		},{
			header : "岗位",
			width : 100,
			sortable : true,
			dataIndex : 'stationName',
			renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
							//update by sychen 20100817
					        var standard = record.get('standard');
							var a=MonthDiff(record.get('brithday').substring(0,10),ChangeDateToString(nowDate).substring(0,10))*(-1)+standard*12
							if(a<0||a==0){
							 if(v!=null)
								return "<font color='red'>" + v + "</font>";
								else
								return "";
							}
							else if((a>1||a===1)&&a<6){
							    if(v!=null)
							    return "<font color='yellow'>" + v + "</font>";
								else
								return "";
							}	
							else if((a>6||a==6)&&(a<12||a==12)){
							    if(v!=null)
							    return "<font color='green'>" + v + "</font>";
								else
								return "";
							}
							else{
							    if(v!=null)
							    return "<font color='black'>" + v + "</font>";
								else
								return "";
							}
//							var standard = record.get('standard');
//							var age = record.get('age');
//							var a = standard-age;
//							if (a>0) {
//								if(v!=null)
//								return "<font color='darkorange'>" + v + "</font>";
//								else
//								return "";
//							} else 
//							{
//								if(v!=null)
//								return "<font color='red'>" + v + "</font>";
//								else
//								return"";
//							}
							
						}
		},{
			header : "年龄",
			width : 45,
			sortable : true,
			dataIndex : 'age',
			renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
							//update by sychen 20100817
					        var standard = record.get('standard');
							var a=MonthDiff(record.get('brithday').substring(0,10),ChangeDateToString(nowDate).substring(0,10))*(-1)+standard*12
							if(a<0||a==0){
							 if(v!=null)
								return "<font color='red'>" + v + "</font>";
								else
								return "";
							}
							else if((a>1||a===1)&&a<6){
							    if(v!=null)
							    return "<font color='yellow'>" + v + "</font>";
								else
								return "";
							}	
							else if((a>6||a==6)&&(a<12||a==12)){
							    if(v!=null)
							    return "<font color='green'>" + v + "</font>";
								else
								return "";
							}
							else{
							    if(v!=null)
							    return "<font color='black'>" + v + "</font>";
								else
								return "";
							}
//							var standard = record.get('standard');
//							var age = record.get('age');
//							var a = standard-age;
//							if (a>0) {
//								if(v!=null)
//								return "<font color='darkorange'>" + v + "</font>";
//								else
//								return "";
//							} else 
//							{
//								if(v!=null)
//								return "<font color='red'>" + v + "</font>";
//								else
//								return"";
//							}
							
						}
		}, {
			header : "工龄",
			width : 45,
			sortable : true,
			dataIndex : 'workAge',
			renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
							//update by sychen 20100817
					        var standard = record.get('standard');
							var a=MonthDiff(record.get('brithday').substring(0,10),ChangeDateToString(nowDate).substring(0,10))*(-1)+standard*12
							if(a<0||a==0){
							 if(v!=null)
								return "<font color='red'>" + v + "</font>";
								else
								return "";
							}
							else if((a>1||a===1)&&a<6){
							    if(v!=null)
							    return "<font color='yellow'>" + v + "</font>";
								else
								return "";
							}	
							else if((a>6||a==6)&&(a<12||a==12)){
							    if(v!=null)
							    return "<font color='green'>" + v + "</font>";
								else
								return "";
							}
							else{
							    if(v!=null)
							    return "<font color='black'>" + v + "</font>";
								else
								return "";
							}
//							var standard = record.get('standard');
//							var age = record.get('age');
//							var a = standard-age;
//							if (a>0) {
//								if(v!=null)
//								return "<font color='darkorange'>" + v + "</font>";
//								else
//								return "";
//							} else 
//							{
//								if(v!=null)
//								return "<font color='red'>" + v + "</font>";
//								else
//								return"";
//							}
							
						}
		}, {
			header : "是否特殊工种",
			width : 90,
			sortable : true,
			dataIndex : 'isSpecialTrades',
			renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
							//update by sychen 20100817
					        var standard = record.get('standard');
							var a=MonthDiff(record.get('brithday').substring(0,10),ChangeDateToString(nowDate).substring(0,10))*(-1)+standard*12
							if(a<0||a==0){
							 if(v!=null)
								return "<font color='red'>" + v + "</font>";
								else
								return "";
							}
							else if((a>1||a===1)&&a<6){
							    if(v!=null)
							    return "<font color='yellow'>" + v + "</font>";
								else
								return "";
							}	
							else if((a>6||a==6)&&(a<12||a==12)){
							    if(v!=null)
							    return "<font color='green'>" + v + "</font>";
								else
								return "";
							}
							else{
							    if(v!=null)
							    return "<font color='black'>" + v + "</font>";
								else
								return "";
							}
//							var standard = record.get('standard');
//							var age = record.get('age');
//							var a = standard-age;
//							if (a>0) {
//								if(v!=null)
//								return "<font color='darkorange'>" + v + "</font>";
//								else
//								return "";
//							} else 
//							{
//								if(v!=null)
//								return "<font color='red'>" + v + "</font>";
//								else
//								return"";
//							}
							
						}
		}, {
			header : "是否干部",
			width : 80,
			sortable : true,
			dataIndex : 'isCadres',
			renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
							//update by sychen 20100817
					        var standard = record.get('standard');
							var a=MonthDiff(record.get('brithday').substring(0,10),ChangeDateToString(nowDate).substring(0,10))*(-1)+standard*12
							if(a<0||a==0){
							 if(v!=null)
								return "<font color='red'>" + v + "</font>";
								else
								return "";
							}
							else if((a>1||a===1)&&a<6){
							    if(v!=null)
							    return "<font color='yellow'>" + v + "</font>";
								else
								return "";
							}	
							else if((a>6||a==6)&&(a<12||a==12)){
							    if(v!=null)
							    return "<font color='green'>" + v + "</font>";
								else
								return "";
							}
							else{
							    if(v!=null)
							    return "<font color='black'>" + v + "</font>";
								else
								return "";
							}
//							var standard = record.get('standard');
//							var age = record.get('age');
//							var a = standard-age;
//							if (a>0) {
//								if(v!=null)
//								return "<font color='darkorange'>" + v + "</font>";
//								else
//								return "";
//							} else 
//							{
//								if(v!=null)
//								return "<font color='red'>" + v + "</font>";
//								else
//								return"";
//							}
							
						}
		}, {
			header : "退休标准",
			width : 100,
			sortable : true,
			dataIndex : 'standard',
			renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
							//update by sychen 20100817
					        var standard = record.get('standard');
							var a=MonthDiff(record.get('brithday').substring(0,10),ChangeDateToString(nowDate).substring(0,10))*(-1)+standard*12
							if(a<0||a==0){
							 if(v!=null)
								return "<font color='red'>" + v + "</font>";
								else
								return "";
							}
							else if((a>1||a===1)&&a<6){
							    if(v!=null)
							    return "<font color='yellow'>" + v + "</font>";
								else
								return "";
							}	
							else if((a>6||a==6)&&(a<12||a==12)){
							    if(v!=null)
							    return "<font color='green'>" + v + "</font>";
								else
								return "";
							}
							else{
							    if(v!=null)
							    return "<font color='black'>" + v + "</font>";
								else
								return "";
							}
//							var standard = record.get('standard');
//							var age = record.get('age');
//							var a = standard-age;
//							if (a>0) {
//								if(v!=null)
//								return "<font color='darkorange'>" + v + "</font>";
//								else
//								return "";
//							} else 
//							{
//								if(v!=null)
//								return "<font color='red'>" + v + "</font>";
//								else
//								return"";
//							}
							
						}
		},{
			header : "出生日期",
			width : 90,
			sortable : true,
			dataIndex : 'brithday',
			renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
							//update by sychen 20100817
					        var standard = record.get('standard');
							var a=MonthDiff(record.get('brithday').substring(0,10),ChangeDateToString(nowDate).substring(0,10))*(-1)+standard*12
							if(a<0||a==0){
							 if(v!=null)
								return "<font color='red'>" + v + "</font>";
								else
								return "";
							}
							else if((a>1||a===1)&&a<6){
							    if(v!=null)
							    return "<font color='yellow'>" + v + "</font>";
								else
								return "";
							}	
							else if((a>6||a==6)&&(a<12||a==12)){
							    if(v!=null)
							    return "<font color='green'>" + v + "</font>";
								else
								return "";
							}
							else{
							    if(v!=null)
							    return "<font color='black'>" + v + "</font>";
								else
								return "";
							}
//							var standard = record.get('standard');
//							var age = record.get('age');
//							var a = standard-age;
//							if (a>0) {
//								if(v!=null)
//								return "<font color='darkorange'>" + v + "</font>";
//								else
//								return "";
//							} else 
//							{
//								if(v!=null)
//								return "<font color='red'>" + v + "</font>";
//								else
//								return"";
//							}
							
						}
		}, {
			header : "参加工作时间",
			width : 90,
			sortable : true,
			dataIndex : 'workDate',
			renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
							//update by sychen 20100817
					        var standard = record.get('standard');
							var a=MonthDiff(record.get('brithday').substring(0,10),ChangeDateToString(nowDate).substring(0,10))*(-1)+standard*12
							if(a<0||a==0){
							 if(v!=null)
								return "<font color='red'>" + v + "</font>";
								else
								return "";
							}
							else if((a>1||a===1)&&a<6){
							    if(v!=null)
							    return "<font color='yellow'>" + v + "</font>";
								else
								return "";
							}	
							else if((a>6||a==6)&&(a<12||a==12)){
							    if(v!=null)
							    return "<font color='green'>" + v + "</font>";
								else
								return "";
							}
							else{
							    if(v!=null)
							    return "<font color='black'>" + v + "</font>";
								else
								return "";
							}
//							var standard = record.get('standard');
//							var age = record.get('age');
//							var a = standard-age;
//							if (a>0) {
//								if(v!=null)
//								return "<font color='darkorange'>" + v + "</font>";
//								else
//								return "";
//							} else 
//							{
//								if(v!=null)
//								return "<font color='red'>" + v + "</font>";
//								else
//								return"";
//							}
							
						}
		},{
			header : "是否退休",
			width : 75,
			sortable : true,
			dataIndex : 'isRetired',
			renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
							//update by sychen 20100817
					        var standard = record.get('standard');
							var a=MonthDiff(record.get('brithday').substring(0,10),ChangeDateToString(nowDate).substring(0,10))*(-1)+standard*12
							if(a<0||a==0){
							 if(v!=null)
								return "<font color='red'>" + v + "</font>";
								else
								return "";
							}
							else if((a>1||a===1)&&a<6){
							    if(v!=null)
							    return "<font color='yellow'>" + v + "</font>";
								else
								return "";
							}	
							else if((a>6||a==6)&&(a<12||a==12)){
							    if(v!=null)
							    return "<font color='green'>" + v + "</font>";
								else
								return "";
							}
							else{
							    if(v!=null)
							    return "<font color='black'>" + v + "</font>";
								else
								return "";
							}
//							var standard = record.get('standard');
//							var age = record.get('age');
//							var a = standard-age;
//							if (a>0) {
//								if(v!=null)
//								return "<font color='darkorange'>" + v + "</font>";
//								else
//								return "";
//							} else 
//							{
//								if(v!=null)
//								return "<font color='red'>" + v + "</font>";
//								else
//								return"";
//							}
							
						}
		}
		, {
			header : "CountDown",
			width : 75,
			sortable : true,
			hidden:true,
			dataIndex : 'countDown'
				
		}
		],
		sm : sm,
		stripeRows : true,
		autoSizeColumns : true,
		//update by sychen 20100629
//		viewConfig : {
//			forceFit : false,
//			getRowClass : function(record, rowIndex, rowParams, store) {
//				if(record.data.countDown <=0 )
//				 return 'row_red';
//				else
//				 return '<font color='green'></font>'//'row_green'
//		}},
		viewConfig : {
			forceFit : true
		},
		tbar : gridTbar,
		// 分页
		bbar : bbar
	});
	//员工工号----------------------------------
	var tfEmpCode = new Ext.form.CodeField({
				id : 'empCode',
				// name : 'emp.empCode',
				name : 'empinfo.empCode',
				fieldLabel : "员工工号",
				readOnly : true,
//				width : 150,
				anchor : '100%',
				maxLength : 20
			});
			
	// 员工姓名
	var tfChsName = new Ext.form.TextField({
				id : 'chsName',
				name : 'empinfo.chsName',
//				width : 150,
				anchor : '100%',
				readOnly:true,
				fieldLabel : "员工姓名"
			});
// 出生日期
	var tfBrithday = new Ext.form.TextField({
				id : 'brithday',
				name : 'empinfo.brithday',
				style : 'cursor:pointer',
//				width : 150,
				anchor : '100%',
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
	
   // 年龄
	var tfAge = new Ext.form.TextField({
				id : 'age',
				fieldLabel : "年龄",
//				width : 150,
				anchor : '100%',
				style : 'text-align: right;',
				readOnly : true
			});
// 参加工作日期
	var tfWorkDate = new Ext.form.TextField({
				id : 'workDate',
				name : 'empinfo.workDate',
				style : 'cursor:pointer',
//				width : 150,
				anchor : '100%',
				fieldLabel : "参加工作日期",
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									onpicked : function() {
										workAge
												.setValue(getWorkAge(this.value));
									},
									onclearing : function() {
										workAge.setValue('');
									}
								});
					}
				}
			});
  var workAge = new Ext.form.TextField({
				id : 'workAge',
				anchor : '100%',
				fieldLabel : "工龄",
				style : 'text-align: right;',
				readOnly : true
			});
// 性别
	var cbSex = new Ext.form.TextField({
				id : 'sex',
				fieldLabel : '性别',
				anchor : '100%',
				selectOnFocus : true
			});
var deptTxt = new Ext.form.TextField({
				id : 'deptName',
				fieldLabel : '所属部门',
				anchor : '100%',
				readOnly : true
			});
	// 所属部门ID
	var hiddenDeptTxt = new Ext.form.Hidden({
				id : 'deptId',
				name : 'empinfo.deptId'
			});
	var stationData = Ext.data.Record.create([{
				name : 'stationName'
			}, {
				name : 'stationId'
			}]);
	var stationStore = new Ext.data.JsonStore({
				url : 'hr/getStationByDeptNewEmployee.action',
				root : 'list',
				fields : stationData
			});
	var tfStationName = new Ext.form.TextField({
				id : 'stationId',
				fieldLabel : '工作岗位',
				anchor : '100%',
				readOnly : true
			});

// 进厂日期
	var tfMissionDate = new Ext.form.TextField({
				id : 'missionDate',
				name : 'empinfo.missionDate',
				style : 'cursor:pointer',
				fieldLabel : "进厂日期",

				anchor : '100%',
				readOnly : true

			});
var isSpecialTrades = new Ext.form.ComboBox({
				fieldLabel : '是否特殊工种',
//				width : 150,
				anchor : '100%',
				store : new Ext.data.SimpleStore({
							fields : ['value', 'text'],
							data : [['Y', '是'], ['N', '否']]
						}),
				id : 'isSpecialTrades',
				name : 'isSpecialTrades',
				valueField : "value",
				displayField : "text",
				value : 'Y',
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'empinfo.isSpecailTrades',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false
			});
	var isRetired = new Ext.form.ComboBox({
				fieldLabel : '是否退休',
//				width : 150,
				anchor : '100%',
				store : new Ext.data.SimpleStore({
							fields : ['value', 'text'],
							data : [[1, '是'], [0, '否']]
						}),
				id : 'isRetired',
				name : 'isRetired',
				valueField : "value",
				displayField : "text",
				value : '0',
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'empinfo.isRetired',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false
			});
	var isCadres = new Ext.form.ComboBox({
				fieldLabel : '是否干部',
//				width : 150,
				anchor : '100%',
				store : new Ext.data.SimpleStore({
							fields : ['value', 'text'],
							data : [['Y', '是'], ['N', '否']]
						}),
				id : 'isCadres',
				name : 'isCadres',
				valueField : "value",
				displayField : "text",
				value : 'Y',
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'empinfo.isCardes',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false
			});
			
			
		var empId = new Ext.form.Hidden({
				id : 'empId',
				name : 'empinfo.empId'
			});
	//------------------------------------
	var myaddpanel = new Ext.FormPanel({
		title : '员工信息修改对话框',
		//height : '175%',
		autoHeight:true,
		layout : 'form',
		frame : true,
		labelAlign : 'center',
		items : [{
			border : false,
			layout : 'column',
			items : [{
				columnWidth : .5,
				layout : 'form',
				border : false,
				labelWidth : 80,
				items : [empId,tfEmpCode, tfBrithday, cbSex, tfWorkDate]
			}, {
				columnWidth : .5,
				layout : 'form',
				labelWidth : 80,
				border : false,
				items : [tfChsName, tfAge, workAge,tfMissionDate]
			}]
		},{
			border : false,
			layout : 'column',
			items : [{
				columnWidth : .5,
				layout : 'form',
				border : false,
				labelWidth : 80,
				items : [hiddenDeptTxt, deptTxt,isSpecialTrades,isRetired]
			}, {
				columnWidth : .5,
				layout : 'form',
				labelWidth : 80,
				border : false,
				items : [tfStationName,isCadres]
			}]
		}]
	});
	

	var win = new Ext.Window({
		width : 550,
		//height : 350,
		autoHeight:true,
		buttonAlign : "center",
		items : [myaddpanel],
		layout : 'fit',
		closeAction : 'hide',
		draggable : true,
		modal : true,
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
				myaddpanel.getForm().submit({
					method : 'POST',
					url : "hr/saveEmpRec.action",
					params : {
						'empId' : empId.getValue()
					},
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("提示", o.msg);
						store.load({
							params : {
								start : start,
								limit : limit
							}
						});
						win.hide();
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
	var deptId="";
	//修改
   function updateRecord() {
		if (grid.selModel.hasSelection()) {
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("提示", "请选择其中一项进行编辑！");
			} else {
				var record = grid.getSelectionModel().getSelected();
				win.show();
				myaddpanel.getForm().loadRecord(record);
				deptId=record.get('deptId');
				stationStore.load({
						params : {
							deptIdForm :deptId
						},
						callback : function() {
							stationStore.insert(0, new stationData({
												stationName : '',
												stationId : ''
											}));
						}
					})
				tfStationName.setValue(record.get('stationId'));
				tfStationName.setRawValue(record.get('stationName'));
//				if(record.get("sex")=="男")
//				{
//					cbSex.setValue("男")
//				}else
//				{
//					cbSex.setValue("女")
//				}
			    if(record.get("isSpecialTrades")=="是")
			    {
			    	isSpecialTrades.setValue("Y");
			    }else
			    {
			    	isSpecialTrades.setValue("N");
			    }
			    if(record.get("isRetired")=="是")
			    {
			    	isRetired.setValue("1");
			    }else
			    {
			    	isRetired.setValue("0");
			    }
			    if(record.get("isCadres")=="是")
			    {
			    	isCadres.setValue("Y");
			    }else
			    {
			    	isCadres.setValue("N");
			    }
				myaddpanel.setTitle("员工信息修改");
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}

	grid.on("rowdblclick",updateRecord );
	
//	grid.on("rowdblclick", showPrintPreview);

	// ------------------------查询-----------------------------------------
	
	function queryRecord() {
		store.baseParams = {
			deptId:queydeptId.getValue(),
			empName : '%'+nameField.getValue()
		};
		store.load({
			params : {
				start : start,
				limit : limit
			}
		});
	}
	//导出
	function reportexport() {
		Ext.Ajax.request({
		url : 'com/retirementExport.action',
		method : 'POST',
		params : {
						deptId : queydeptId.getValue(),
						empName : nameField.getValue()
					},
		success : function(response) {
			var txt=
				Ext.Msg.alert('提示', '导出成功！');
		},
		failure : function(response) {
				Ext.Msg.alert('提示', '导出失败！');
		}
		})
		
		
	}
	function showPrintPreview() {
		var selected = grid.getSelectionModel().getSelections();
		var empId;
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要浏览的记录！");
		} else {
			var menber = selected[0];
			empId = menber.get('empId');
			var url = "/power/report/webfile/hr/employeeRecord.jsp?empId="
					+ empId;
			window.open(url);
		}
	};

	var memoText = "退居二线的标准为男同志53岁，女同志48岁；岗位名称为“副总师”的男同志卫55岁，女同志为50岁；员工以周岁为准，当(员工出生年月+退休标准)<=当前年月 的时候员工信息行用红色标记，当 一个月<= (员工出生年月+退休标准)-当前年月<六个月 时候员工信息用黄色标记，当 六个月<= (员工出生年月+退休标准)-当前年月<=十二个月 时候员工信息用绿色标记，其他员工信息默认用字体黑色显示。";
	var totalMemo = new Ext.form.TextArea({
				id : "totalMemo",
				fieldLabel : '备注',
				allowBlank : true,
				readOnly : true,
				value : memoText,
				width : 980,
				name : 'totalMemo'
			});

	var Form = new Ext.Toolbar({
				items : ["备注:", totalMemo]
			});
	
	new Ext.Viewport({
		layout : "border",
		items : [{
			region : 'center',
			height : '100%',
			border : false,
			layout : 'border',
			items : [grid]
		}, {
		    region : 'south',
			xtype : 'panel',
			border : false,
			height : 75,
			items : [{
				border : false,
				items : [Form]
			}]
		}]
	});
	// 部门选择
	function deptSelect() {
		var args = {
			selectModel : 'single',
			rootNode : {
				id : '0',
				text : '灞桥电厂'
			}
		};
		var object = window.showModalDialog('/power/comm/jsp/hr/dept/dept.jsp',
				args, 'dialogWidth=' + Constants.WIDTH_COM_DEPT
						+ 'px;dialogHeight=' + Constants.HEIGHT_COM_DEPT
						+ 'px;center=yes;help=no;resizable=no;status=no;');
		if (object) {
			tfStationName.clearValue();
			deptTxt.setValue(object.names);
			Ext.get("deptId").dom.value = object.ids;
			stationStore.load({
						params : {
							deptIdForm : object.ids
						},
						callback : function() {
							stationStore.insert(0, new stationData({
												stationName : '',
												stationId : ''
											}));
						}
					})
		}
	}
	queryRecord();
});