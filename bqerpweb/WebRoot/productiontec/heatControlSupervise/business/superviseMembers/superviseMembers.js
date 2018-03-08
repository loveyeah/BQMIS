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
	//选择所有人员及其所在部门
	function selectRecipientsByWin() {
		var args = {
			selectModel : 'signal',
			notIn : "",
			rootNode : {
				id : '-1',
				text : '合肥电厂'
			}
		}
		var person = window
				.showModalDialog(
						'../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(person) != "undefined") {
			//alert(westgrids.find("model.workerCode",person.workerCode))
            if((westgrids.find("model.workerCode",person.workerCode))!=-1){
                             Ext.Msg.alert("提示","该成员已担任监督网职务,不可再担任其他职务！");
                             return false;
            }else{
			workerName.setValue(person.workerName);
			workerCode.setValue(person.workerCode);
			workId.setValue(person.workerId);
		
		//	alert(workId.getValue());
			if(person.deptName == null || person.deptName == ""){
					Ext.get('deptName').dom.value = "";
			     }else{
			     	Ext.get('deptName').dom.value = person.deptName;
			     }
            }
		}
	}
	
	
	//成员监督网职务
	var netDuty = new Ext.form.ComboBox({
		fieldLabel : "监督网职务",
		store : [['1', '一级'], ['2', '二级'], ['3', '三级']],
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
		triggerAction : 'all',
		width : 160,
		selectOnFocus : true
	});
	// 成员姓名
	var workerName = new Ext.form.TriggerField({
		fieldLabel : "成员姓名",
		readOnly : true,
		displayField : 'text',
		valueField : 'id',
		hiddenName : 'workerName',
		name : 'workerName',
		blankText : '请选择',
		emptyText : '请选择',
		width : 160
	})
	workerName.onTriggerClick = selectRecipientsByWin;
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
			
	//add by fyyang 091030----------------
		var workId=new Ext.form.Hidden({
		name:'workId'
		});	
	//-------------------------------------		
	// 主键
	var jdwcyId = new Ext.form.Hidden({
		name : 'model.jdwcyId'
	})
	function checkInput()
	{
		if(workerName.getValue()==""){
			
			 Ext.Msg.alert("提示","请选择成员！");
			 return false;
		}
		if(netDuty.getValue() == ""){
			Ext.Msg.alert("提示","请选择职务！");
			return false;
		}
		return true;
	}
	// 弹窗的表单对象
	var blockForm = new Ext.form.FormPanel({
				labelAlign : 'right',
				frame : true,
				labelWidth : 80,
				style : 'padding:10px,5px,0px,5px',
				//layout : 'column',
				closeAction : 'hide',
				fileUpload : true,
				items : [workerName,deptName,netDuty,workerCode,jdwcyId,workId]

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
		buttons : [{
			text : '保存',
			iconCls:'save',
			handler : function() {
				var myurl="";
				if (jdwcyId.getValue() == "") {
					myurl="productionrec/addPtJJdwcyInfo.action";
				} else {
					myurl="productionrec/updatePtJJdwcyInfo.action";
				}
				
				if(!checkInput()) return;
				blockForm.getForm().submit({
					method : 'POST',
					url : myurl,
					params:{	
								jdzyId : jdId,
								workId:workId.getValue()
					},
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						//Ext.Msg.alert("注意", o.msg);
						if(o.msg.indexOf("成功")!=-1)
						{
							queryRecord();
						    win.hide(); 
						   // bview="";
						}
					},
					faliue : function() {
						Ext.Msg.alert('错误', '出现未知错误.');
					}
				});
			}
		}, {
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
	// 新建按钮
	var westbtnAdd = new Ext.Button({
		text : '新增',
		iconCls : 'add',
		handler : addRecord
	});


	// 修改按钮
	var westbtnedit = new Ext.Button({
		text : '修改',
		iconCls : 'update',
		handler : updateRecord
	});

	// 删除按钮
	var westbtndel = new Ext.Button({
		text : '删除',
		iconCls : 'delete',
		handler : deleteRecord
	});
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
						},westbtnAdd, {
							xtype : "tbseparator"
						}, westbtnedit, {
							xtype : "tbseparator"
						}, westbtndel,{
							xtype : "tbseparator"
						}, westbtnref],
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
			jdzyId:jdId
		};
		westgrids.load({		
			params : {
				start : 0,
				limit : 18				
			}
		});
		Ext.get("jdzyName").dom.value = name;
	}
	function addRecord()
	{  
		blockForm.getForm().reset(); 
		//getWorkCode();
		win.show();  
		//btnView.setVisible(false);
		win.setTitle("增加监督网成员信息");
		//alert("dff");
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
				win.setTitle("修改监督网成员信息");
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}
	function deleteRecord()
	{
		var records = westgrid.selModel.getSelections();
		var ids = [];
		if (records.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {

			
			for (var i = 0; i < records.length; i += 1) {
				var member = records[i];
				if (member.get("model.jdwcyId")) {
					ids.push(member.get("model.jdwcyId")); 
				} else {
					
					store.remove(store.getAt(i));
				}
			}
		
			Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(
					buttonobj) {

				if (buttonobj == "yes") {

					Ext.lib.Ajax.request('POST',
							'productionrec/deletePtJJdwcyInfo.action', {
								success : function(action) {
									//Ext.Msg.alert("提示", "删除成功！")
                                   		queryRecord();
						         	
								},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
							}, 'ids=' + ids);
				}
			});
			
		
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