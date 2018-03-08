Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrefntSessionEmployee.action', {
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result.workerCode) {
					// 设定工作负责人为登录人
					entryBy.setValue(result.workerCode);
					entryName.setValue(result.workerName);
					entryDept.setValue(result.deptCode);
					entryDeptName.setValue(result.deptName)
				}
			}
		});
	}
	getWorkCode()
	// 系统当前时间
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t;
		return s;
	}
	var ids = new Array();
	var rootNode = new Ext.tree.AsyncTreeNode({
		text : '25项反事故措施条例表',
		id : '0',
		isRoot : true
	});
	var currentNode = rootNode;
	var treePanel = new Ext.tree.TreePanel({
		region : 'center',
		animate : true,
	//	autoHeight : true,
		autoScroll : true,
		root : rootNode,
		border : false,
		rootVisible : true,
		loader : new Ext.tree.TreeLoader({
			dataUrl : "security/findByParentCode.action"
		})
	});
	treePanel.setRootNode(rootNode);
	treePanel.on("click", treeClick);
	rootNode.select();
	rootNode.expand();
	
	
	//-----add by fyyang 090920--------------------
		var btnExport = new Ext.Button({
		id : 'btnExport',
		text : '导  出',
		iconCls : 'export',
		minWidth : 70,
		handler : function() {
			myExport();
		}
	});
	var measureCode = new Ext.form.Hidden({
		        id:'measureCode',
				name : 'model.measureCode'
			})
	var fdDutyLeaderpeople= new Ext.form.Hidden({
		        id:'fdDutyLeader',
				name : 'model.fdDutyLeader'
			})
	var measureName = new Ext.form.TextField({
		fieldLabel :"应完成工作",
		id : 'measureName',
		name : 'model.measureName',
		width : 125,
	    readOnly :false
	})
	var fdDutyLeaderName = new Ext.form.TextField({
		fieldLabel :"责任领导",
		id : 'fdDutyLeaderName',
//		name : 'fdDutyLeaderName',
		width : 125,
		readOnly : true,
		listeners : {
			focus : function() {
				var args = {
					selectModel : 'multiple',
					rootNode : {
						id : '0',
						text : '合肥电厂'
					},
					onlyLeaf : false
				};
				this.blur();
				var person = window
						.showModalDialog(
								'../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
								args,
								'dialogWidth:'
										+ Constants.WIDTH_COM_EMPLOYEE
										+ 'px;dialogHeight:'
										+ Constants.HEIGHT_COM_EMPLOYEE
										+ 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
				if (typeof(person) != "undefined") {
					
					fdDutyLeaderName.setValue(person.names);
					fdDutyLeaderpeople.setValue(person.codes);
				
				
			}
		}
		}
	});
	var fdManager = new Ext.form.Hidden({
		         id:'fdManager',
				name : 'model.fdManager'
			})
	var fdManagerName = new Ext.form.TextField({
		fieldLabel :"管理责任人",
		id : 'fdManagerName',
//		name : 'fdManagerName',
		width : 125,
		readOnly : true,
		listeners : {
			focus : function() {
				var args = {
					selectModel : 'single',
					rootNode : {
						id : '0',
						text : '合肥电厂'
					},
					onlyLeaf : false
				};
				this.blur();
				var person = window
						.showModalDialog(
								'../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
								args,
								'dialogWidth:'
										+ Constants.WIDTH_COM_EMPLOYEE
										+ 'px;dialogHeight:'
										+ Constants.HEIGHT_COM_EMPLOYEE
										+ 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
				if (typeof(person) != "undefined") {
					fdManagerName.setValue(person.workerName);
					fdManager.setValue(person.workerCode);
				
				
			}
		}
		}
	});
	var fdTechnologyBy = new Ext.form.Hidden({
		          id:'fdTechnologyBy',
				name : 'model.fdTechnologyBy'
			})
	var fdTechnologyByName = new Ext.form.TextField({
		fieldLabel :"技术责任人",
		id : 'fdTechnologyName',
//		name : 'fdTechnologyByName',
		width : 125,
		readOnly : true,
		listeners : {
			focus : function() {
				var args = {
					selectModel : 'multiple',
					rootNode : {
						id : '0',
						text : '合肥电厂'
					},
					onlyLeaf : false
				};
				this.blur();
				var person = window
						.showModalDialog(
								'../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
								args,
								'dialogWidth:'
										+ Constants.WIDTH_COM_EMPLOYEE
										+ 'px;dialogHeight:'
										+ Constants.HEIGHT_COM_EMPLOYEE
										+ 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
				if (typeof(person) != "undefined") {
					fdTechnologyByName.setValue(person.names);
					fdTechnologyBy.setValue(person.codes);
				
				
			}
		}
		}
	});
	var fdSuperviseBy = new Ext.form.Hidden({
	         id:'fdSuperviseBy',
			name : 'model.fdSuperviseBy'
			})
	var fdSuperviseByName = new Ext.form.TextField({
		fieldLabel :"监督责任人",
		id : 'fdSuperviseName',
		name : 'fdSuperviseName',
		width : 125,
		readOnly : true,
		listeners : {
			focus : function() {
				var args = {
					selectModel : 'single',
					rootNode : {
						id : '0',
						text : '合肥电厂'
					},
					onlyLeaf : false
				};
				this.blur();
				var person = window
						.showModalDialog(
								'../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
								args,
								'dialogWidth:'
										+ Constants.WIDTH_COM_EMPLOYEE
										+ 'px;dialogHeight:'
										+ Constants.HEIGHT_COM_EMPLOYEE
										+ 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
				if (typeof(person) != "undefined") {
					fdSuperviseByName.setValue(person.workerName);
					fdSuperviseBy.setValue(person.workerCode);
				
				
			}
		}
		}
	});

	var gridForm = new Ext.FormPanel({
//		id : 'safe-form',
		frame : true,
		autoWidth : true,
		autoHeight : true,
		align : 'center',
		labelAlign : 'left',
		items : [measureCode,measureName,fdDutyLeaderpeople,fdDutyLeaderName,fdManager,fdManagerName,fdTechnologyBy,
                 fdTechnologyByName,fdSuperviseBy,fdSuperviseByName],
			buttons : [{
			id : 'save',
			text : "保存",
			hidden:false,
			iconCls : 'save',
			handler : function() {
				if (gridForm.getForm().isValid()) {
					var url = "";
					if (measureCode.getValue() == ""
							|| measureCode.getValue() == null) {
						url = "security/addMeasure.action?id ="
								+ currentNode.id;
					} else {
						url = "security/updateMeasure.action";
					}
					
					gridForm.getForm().submit({
						method : 'post',
						url : url,
						success : function(form, action) {
							// 更新树结构
							/*var parentNode;

							if (currentNode.id.length == 6) {
								parentNode = currentNode.parentNode;
							} else if(currentNode.id.length == 1){
								parentNode = currentNode;								
							}
							else {
								parentNode = currentNode.parentNode;
							}
							parentNode.reload();
							var cldNodes = parentNode.childNodes;
							var len = cldNodes ? cldNodes.length : 0;
							var result = null;
							for (var i = 0; i < len; i++) {
								if (measureCode.getValue() == cldNodes[i].id) {
									result = cldNodes[i];
									break;
								}
							}*/
							/*if (result)
								currentNode = result;*/
							Ext.MessageBox.alert('提示', '操作成功');
							antiDs.reload();
							win.hide();
							
						},
						failure : function(form, action) {
							Ext.MessageBox.alert('提示', '操作失败');
						}

					})
				}
			}
		}, {
				text : '取消',
				iconCls:'cancer',
				handler : function() {
					win.hide(); 
				}
			}]
		
	});
		var win = new Ext.Window({
		title:'修改详细信息',
		width : 400,
		height : 200,
		modal:true,
		closeAction : 'hide',
		items : [gridForm]
	});
	function  updateRecord()
	{
		
		var rec = antiGrid.getSelectionModel().getSelections();
		    if(rec.length!=1){
			    Ext.Msg.alert("请选择一行！");
			    return false;
		    }
		    else{
							win.show();
							var rec = antiGrid.getSelectionModel().getSelected();
						
							measureCode.setValue(rec.get("measureCode"))	;	
							measureName.setValue(rec.get("measureName"));
							fdDutyLeaderpeople.setValue(rec.get("fdDutyLeader"));
							fdDutyLeaderName.setValue(rec.get("fdDutyLeaderName"));
							fdManager.setValue(rec.get("fdManager"));
							fdManagerName.setValue(rec.get("fdManagerName"))	;	
						
							fdTechnologyBy.setValue(rec.get("fdTechnologyBy"));
							fdTechnologyByName.setValue(rec.get("fdTechnologyName"));
							fdSuperviseBy.setValue(rec.get("fdSuperviseBy"))	;	
							fdSuperviseByName.setValue(rec.get("fdSuperviseName"));
						
					
							
						} 
					}
	var btnUpdate=new Ext.Button({
		id : 'btnupdate',
		text : '修改',
		iconCls : 'update',
		minWidth : 70,
		handler : function() {
			updateRecord();
		}
	});
	
	
//	antiGrid.on('rowdblclick',updateRecord) ;alert();
	function myExport() {
		//Ext.Ajax.request({
			//url : 'security/getSafeMeasureListForExcel.action',
			//success : function(response) {
				//var json = eval('(' + response.responseText+ ')');
				
				//var list=json.data;
				//var arrayObj = new Array([list.length]);
				//for(var i=0;i<list.length;i++)
				//{
					//arrayObj[i]=list[i].tb;
					
				//}
			
				//tableToExcel(arrayObj);
			//},
			//failure : function(response) {
				//Ext.Msg.alert('信息', '失败');
			//}
		//});
		window.open("http://10.10.100.25:8080/power//run/securityproduction/SafeMeasure/灞桥热电厂25项责任落实分解表安环部.xls");
		
		
//		var html = ['<table border=1><tr><th  colspan=11>二十五项反措动责任落实分解表</th></tr>'];
//		html.push('<tr><th colspan=11 align="left">1. 防止火灾事故执行检查表</th></tr>')
//		html.push('<tr><th colspan=11 align="left">填写单位：</th></tr>')
//		html.push(	'<tr><th rowspan=3>项目</th><th rowspan=3>序号</th><th rowspan=3>应完成的工作</th><th colspan=8>责任落实情况</th></tr>');
//		html.push(	'<tr><th colspan=4>发电企业</th><th colspan=4>大唐陕西发电公司</th></tr>');
//		html.push(	'<tr><th>责任领导</th><th>管理责任人</th><th>技术责任人</th><th>监督责任人</th><th>责任领导</th><th>管理责任人</th><th>技术责任人</th><th>监督责任人</th></tr>');
//	    	
//				
//				for (var i = 0; i <3; i += 1) {
//					
//					html.push('<tr><td>aaa</td><td>'
//							+ 'bbb</td><td>ccc'
//							+ '</td><td>'
//							+  'ddd</td><td>' 
//							+ 'eee</td><td>fff</td><td>'
//							+ 'ggg</td><td>' 
//							+ 'iii</td><td>' 
//							+ 'jjj</td><td>' 
//							+ 'kkk</td><td>' 
//							+ 'hhh</td></tr>');
//				}
//				html.push('</table>');
//				html = html.join(''); // 最后生成的HTML表格
//				
//				var arrayObj = new Array([25]);
//				arrayObj[0]=html;
//				arrayObj[1]="<table><tr><td>dstetw</td><td>2222</td></tr></table>";
//				for(i=2;i<25;i++)
//				{
//					arrayObj[i]=arrayObj[0];
//				}
//				alert(arrayObj.length);
//			tableToExcel(arrayObj);
	}
		// 导出
	function tableToExcel(tableHTML) {
	   var num=tableHTML.length;
		window.clipboardData.setData("Text", tableHTML[0]);
		try {
			var ExApp = new ActiveXObject("Excel.Application");
		    ExApp.sheetsinnewworkbook=num;
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExApp.DisplayAlerts = false;
			ExApp.visible = true;
			ExWBk.worksheets(1).Paste;
			for(var i=1;i<num;i++)
			{
	         window.clipboardData.setData("Text", tableHTML[i]);
	          ExWBk.worksheets(i+1).Paste;
			}
			
		} catch (e) {
			if (e.number != -2146827859)
				alert("您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
	}
	//---------------------------------------------------
	// 反事故措施
	var tbar1 = new Ext.Toolbar({
		items : [{
			id : 'btnAdd',
			text : "增加",
			iconCls : 'add',
			hidden:true,
			handler : function() {
				form.getForm().reset();
				getWorkCode();
				ds.removeAll();
			}
		}, {
			id : 'btnDelete',
			text : "删除",
			iconCls : 'delete',
			hidden:true,
			disabled : true,
			handler : function() {
				if (currentNode.childNodes.length > 0) {
					Ext.Msg.alert("提示", "请先删除子节点！")
				} else {
					Ext.Msg.confirm("删除", "是否确定删除该记录？", function(buttonobj) {
						if (buttonobj == "yes") {
							Ext.lib.Ajax.request('POST',
									'security/deleteMeasure.action', {
										success : function(action) {
											form.getForm().reset();
											ds.removeAll();
											var nd = treePanel
													.getNodeById(currentNode.parentNode.id);
											var path = nd.getPath();
											treePanel.getRootNode().reload();
											treePanel.expandPath(path, false);
											if(currentNode.id.length == 2)
												currentNode = rootNode;
											else	
												currentNode = currentNode.parentNode;
											currentNode.select()											
											Ext.Msg.alert("提示", "删除成功！")
										},
										failure : function() {
											Ext.Msg.alert('错误', '删除时出现未知错误.');
										}
									}, 'id=' + measureCode.getValue());
						}
					})
				};

			}
		}/*,  {
			id : 'btnSave',
			text : "保存",
			hidden:true,
			iconCls : 'save',
			handler : function() {
				if (form.getForm().isValid()) {
					var url = "";
					if (measureCode.getValue() == ""
							|| measureCode.getValue() == null) {
						url = "security/addMeasure.action?id ="
								+ currentNode.id;
					} else {
						url = "security/updateMeasure.action";
					}
					form.getForm().submit({
						method : 'post',
						url : url,
						success : function(form, action) {
							// 更新树结构
							var parentNode;

							if (currentNode.id.length == 6) {
								parentNode = currentNode.parentNode;
							} else if(currentNode.id.length == 1){
								parentNode = currentNode;								
							}
							else {
								parentNode = currentNode.parentNode;
							}
							parentNode.reload();
							var cldNodes = parentNode.childNodes;
							var len = cldNodes ? cldNodes.length : 0;
							var result = null;
							for (var i = 0; i < len; i++) {
								if (measureCode.getValue() == cldNodes[i].id) {
									result = cldNodes[i];
									break;
								}
							}
							if (result)
								currentNode = result;
							Ext.MessageBox.alert('提示', '操作成功');
						},
						failure : function(form, action) {
							Ext.MessageBox.alert('提示', '操作失败');
						}

					})
				}
			}
		}*/
		,btnExport,'-',btnUpdate]
	});	
	// 反事故措施详细
	var tbar2 = new Ext.Toolbar({
		items : [{
			id : 'add',
			text : "增加",
			iconCls : 'add',
			disabled : true,
			handler : function() {
				var count = ds.getCount();
				var currentIndex = count;
				var o = new obj2({
					'detailsId' : '',
					'measureCode' : measureCode.getValue(),
					'content' : ''
				});
				grid.stopEditing();
				ds.insert(currentIndex, o);
				sm.selectRow(currentIndex);
				grid.startEditing(currentIndex, 2);
			}
		}, '-', {
			id : 'delete',
			text : "删除",
			disabled : true,
			iconCls : 'delete',
			handler : function() {
				grid.stopEditing();
				var selected = grid.getSelectionModel().getSelections();
				if (selected.length == 0) {
					Ext.Msg.alert("提示", "请选择要删除的记录！");
				} else {
					for (var i = 0; i < selected.length; i += 1) {
						var member = selected[i];
						if (member.get("detailsId") != null
								&& member.get("detailsId") != "") {
							ids.push(member.get("detailsId"));
						}
						grid.getStore().remove(member);
						grid.getStore().getModifiedRecords().remove(member);
					}
					if (ids.length > 0) {
						Ext.Msg.confirm("删除", "是否确定删除记录？", function(buttonobj) {
							if (buttonobj == "yes") {
								Ext.Ajax.request({
									url : 'security/deleteDetail.action',
									method : 'post',
									params : {
										ids : ids.join()
									},
									success : function(result, request) {
										ids = [];
										// Ext.Msg.alert('提示', '操作成功');
									},
									failure : function(result, request) {
										Ext.Msg.alert('提示', '操作失败');
									}
								})
							}
						})
					}
				}

			}
		}/*, '-', {
			id : 'save',
			text : "保存",
			disabled : true,
			iconCls : 'save',
			handler : saveDetail
		}*/]
	});
	var contentText = new Ext.form.TextArea({
		id : "contentText",
		maxLength : 100,
		width : 180
	});
	/*var win = new Ext.Window({
		height : 170,
		width : 350,
		layout : 'fit',
		resizable : false,
		closeAction : 'hide',
		modal : true,
		items : [contentText],
		buttonAlign : "center",
		title : '修改详细信息',
		buttons : [{
			text : "保存",
			iconCls : 'save',
			handler : function() {
				// if (contentText.getValue().length <= 100) {
				var record = grid.getSelectionModel().getSelected()
				record.set("content", contentText.getValue());
				win.hide();
				// }
			}
		}, {
			text : "取消",
			iconCls : 'canse',
			handler : function() {
				win.hide();
			}
		}]
	});*/

	// 反事故措施详细
	var obj2 = Ext.data.Record.create([{
		name : 'detailsId'
	}, {
		name : 'measureCode'
	}, {
		name : 'content'
	}]);

	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) {
			}
		}
	});

	var cm = new Ext.grid.ColumnModel([sm, new Ext.grid.RowNumberer({
		header : '行号',
		width : 35,
		hidden : true,
		sortable : false,
		align : 'left'
	}), {
		header : '内容',
		dataIndex : 'content',
		sortable : false,
		renderer : function(v) {
			return "<div style='white-space:normal;'>" + v + "</div>";
		},
		width : 400,
		editor : new Ext.form.TextField({
			listeners : {
				"render" : function() {
					this.el.on("dblclick", function() {
						var record = grid.getSelectionModel().getSelected();
						var value = record.get('content');
						contentText.setValue(value);
						win.show();
					})
				}
			}
		})
	}

	]);
		
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'security/findByMeasureCode.action'
		}),
		reader : new Ext.data.JsonReader({
				}, obj2)
	});
	var grid = new Ext.grid.EditorGridPanel({
		store : ds,
		cm : cm,
		//sm : sm,
		tbar : tbar2,
		viewConfig : {
			forceFit : true
		},
		autoScroll : true,
		frame : false,
		border : false,
		clicksToEdit : 1
	});
	/**
	*将动态分解表改为grid形式显示
	*start
	**/
	
	//定义antiSm
	var antiSm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(antiSm,row,rec){
			}
		}
	});
	//定义antiGrid的数据源
	
	var antiRecordCreate = new Ext.data.Record.create([
	{
		name : 'measureCode'
	}, {
		name : 'PMeasureName'
	}, {
		name : 'measureName'
	},{
		name : 'fdDutyLeader'
	}, {
		name : 'fdManager'
	}, {
		name : 'fdTechnologyBy'
	},  {
		name : 'fdSuperviseBy'
	}, {
		name : 'fdDutyLeaderName'
	},{
		name : 'fdManagerName'
	},{
		name : 'fdTechnologyName'
	},{
		name : 'fdSuperviseName'
	}]);
	
	var antiDs = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'security/getMeasurListByCode.action'
		}),
		reader : new Ext.data.JsonReader({},antiRecordCreate)//reader : Ext.data.DataReader 处理数据对象的DataReader会返回一个Ext.data.Record对象的数组。其中的id属性会是一个缓冲了的键。	
	});
	
	//定义antiCm
	var antiCm = new Ext.grid.ColumnModel([
		antiSm,
		new Ext.grid.RowNumberer({
			header : '行号',
			width : 35,
			hidden : true,
			sortable : false,
			align : 'left'
		}),{
			header : '检查项目',
			dataIndex : 'PMeasureName',
			sortable : false,
			renderer : function(v){
				return "<div style='white-space:normal'>" + v + "</div>";
			},
			width : 100
			//editor : new Ext.form.TextField({
				//listeners : {
					//"render" : function(){
						//this.el.on("dblclick",function(){
							//var record = antiGrid.getSelectionModel().getSelected();
							//var value = record.get('pMeasureName');
							//contentText.setValue(value);
							//win.show();
						//})
					//}
				//}
			//})
		},{
			header : '应完成的工作',
			dataIndex : 'measureName',
			sortable : false,
			renderer : function(v){
				return "<div style='white-space:normal'>" + v + "</div>";
			},
			width : 250
			//editor : new Ext.form.TextField({
				//listeners : {
					//"render" : function(){
						//this.el.on("dblclick",function(){
							//var record = antiGrid.getSelectionModel().getSelected();
							//var value = record.get('measureName');
							//contentText.setValue(value);
							//win.show();
						//})
					//}
				//}
			//})
		},{
			header : '责任领导',
			dataIndex : 'fdDutyLeaderName',
			sortable : false,
			renderer : function(v){
				return "<div style='white-space:normal'>" + v + "</div>";
			},
			width : 50
			//editor : new Ext.form.TextField({
				//listeners : {
					//"render" : function(){
						//this.el.on("dblclick",function(){
							//var record = antiGrid.getSelectionModel().getSelected();
							//var value = record.get('fdDutyLeader');
							//contentText.setValue(value);
							//win.show();
						//})
					//}
				//}
			//})
		},{
			header : '管理责任人',
			dataIndex : 'fdManagerName',
			sortable : false,
			renderer : function(v){
				return "<div style='white-space:normal'>" + v + "</div>";
			},
			width : 50
			//editor : new Ext.form.TextField({
				//listeners : {
					//"render" : function(){
						//this.el.on("dblclick",function(){
							//var record = antiGrid.getSelectionModel().getSelected();
							//var value = record.get('fdManager');
							//contentText.setValue(value);
							//win.show();
						//})
					//}
				//}
			//})
		},{
			header : '技术责任人',
			dataIndex : 'fdTechnologyName',
			sortable : false,
			renderer : function(v){
				return "<div style='white-space:normal'>" + v + "</div>";
			},
			width : 50
			//editor : new Ext.form.TextField({
				//listeners : {
					//"render" : function(){
						//this.el.on("dblclick",function(){
							//var record = antiGrid.getSelectionModel().getSelected();
							//var value = record.get('fdTechnologyBy');
							//contentText.setValue(value);
							//win.show();
						//})
					//}
				//}
			//})
		},{
			header : '监督责任人',
			dataIndex : 'fdSuperviseName',
			sortable : false,
			renderer : function(v){
				return "<div style='white-space:normal'>" + v + "</div>";
			},
			width : 100
			//editor : new Ext.form.TextField({
				//listeners : {
					//"render" : function(){
						//this.el.on("dblclick",function(){
							//var record = antiGrid.getSelectionModel().getSelected();
							//var value = record.get('fdSuperviseBy');
							//contentText.setValue(value);
							//win.show();
						//})
					//}
				//}
			//})
		}
	]);
	
	var antiGrid = new Ext.grid.EditorGridPanel({
		store : antiDs,
		cm : antiCm,
		sm : antiSm,
		tbar : tbar1,
		viewConfig : {
			forceFit : true
		},
		autoScroll : true,
		frame : false,
		border : false,
		clicksToEdit : 1
	});
	
	/**
	*将动态分解表改为grid形式显示
	*end
	**/
	/*//措施编号
	var measureCode = new Ext.form.Hidden({
		id : 'measureCode',
		name : 'model.measureCode'
	})
	//措施名称
	var measureName = new Ext.form.TextArea({
		id : 'measureName',
		name : 'model.measureName',
		fieldLabel : '措施名称',
//		allowBlank : false,
		readOnly : false,
		width : 390,
		height : 40
	});
	
	// 专业
	var storeRepairSpecail = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'workticket/getDetailRepairSpecialityType.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, [{
			name : 'specialityCode',
			mapping : 'specialityCode'
		}, {
			name : 'specialityName',
			mapping : 'specialityName'
		}])
	});
	storeRepairSpecail.load();
	var specialCode = new Ext.form.ComboBox({
		id : 'specialCode',
		fieldLabel : "专业",
		store : storeRepairSpecail,
		displayField : "specialityName",
		valueField : "specialityCode",
		hiddenName : 'model.specialCode',
		mode : 'remote',
		triggerAction : 'all',
		value : '',
		readOnly : true,
		disabled : true,
		width : 125
	})
//	// 专业编码
//	var specialCode = new Ext.form.Hidden({
//		id : 'specialCode',
//		name : 'model.specialCode'
//	})
//	//专业编码 名
//	var specialName = new Ext.form.TextField({
//		id : 'specialName',
//		name : 'specialName',
//		fieldLabel : '专业编码',
////		allowBlank : false,
//		readOnly : true
//	});
	// 责任领导（发电企业）
	var fdDutyLeaderName = new Ext.form.TextField({
		fieldLabel : "责任领导",
//		allowBlank : false,
		id : 'fdDutyLeaderName',
		name : 'fdDutyLeaderName',
		editable : false,
		disabled : true,
		readOnly : true
	})
	fdDutyLeaderName.onClick(function() {
			var ps = personSelect();
			if (ps != null) {
				fdDutyLeader.setValue(ps.workerCode);
				fdDutyLeaderName.setValue(ps.workerName);
			}
		});
	var fdDutyLeader = new Ext.form.Hidden({
		id : 'fdDutyLeader',
		name : 'model.fdDutyLeader',
		value : ''
	})
	
	//  管理责任人（发电企业）
	var fdManagerName = new Ext.form.TextField({
		fieldLabel : "管理责任人",
//		allowBlank : false,
		id : 'fdManagerName',
		name : 'fdManagerName',
		editable : false,
		disabled : true,
		readOnly : true
	})
	fdManagerName.onClick(function() {
			var ps = personSelect();
			if (ps != null) {
				fdManager.setValue(ps.workerCode);
				fdManagerName.setValue(ps.workerName);
			}
		})
	var fdManager = new Ext.form.Hidden({
		id : 'fdManager',
		name : 'model.fdManager'
	})
	
	// 技术责任人（发电企业）
	var fdTechnologyName = new Ext.form.TextField({
		fieldLabel : "技术责任人",
//		allowBlank : false,
		id : 'fdTechnologyName',
		name : 'fdTechnologyName',
		editable : false,
		disabled : true,
		readOnly : true
	})
	fdTechnologyName.onClick(function() {
			var ps = personSelect();
			if (ps != null) {
				fdTechnologyBy.setValue(ps.workerCode);
				fdTechnologyName.setValue(ps.workerName);
			}
		});
	var fdTechnologyBy = new Ext.form.Hidden({
		id : 'fdTechnologyBy',
		name : 'model.fdTechnologyBy'
	})
	
	// 监督责任人（发电企业）
	var fdSuperviseName = new Ext.form.TextField({
		fieldLabel : "责任领导",
//		allowBlank : false,
		id : 'fdSuperviseName',
		name : 'fdSuperviseName',
		editable : false,
		disabled : true,
		readOnly : true 
	})
	fdSuperviseName.onClick(function() {
			var ps = personSelect();
			if (ps != null) {
				fdSuperviseBy.setValue(ps.workerCode);
				fdSuperviseName.setValue(ps.workerName);
			}
		});
	var fdSuperviseBy = new Ext.form.Hidden({
		id : 'fdSuperviseBy',
		name : 'model.fdSuperviseBy'
	})
	
	// 责任领导（大唐陕西）
	var dtDutyLeaderName = new Ext.form.TextField({
		fieldLabel : "责任领导",
//		allowBlank : false,
		id : 'dtDutyLeaderName',
		name : 'dtDutyLeaderName',
		editable : false,
		disabled : true,*/
	/*	readOnly : true
	})*/
	/*dtDutyLeaderName.onClick(function() {
			var ps = empSelect();
			if (ps != null) {
				dtDutyLeader.setValue(ps.empId);
				dtDutyLeaderName.setValue(ps.empName);
			}
		});
	var dtDutyLeader = new Ext.form.Hidden({
		id : 'dtDutyLeader',
		name : 'model.dtDutyLeader'
	})
	
	// 责任领导（大唐陕西）
	var dtDutyLeaderName = new Ext.form.TextField({
		fieldLabel : "责任领导",
//		allowBlank : false,
		id : 'dtDutyLeaderName',
		name : 'dtDutyLeaderName',
		editable : false,
		disabled : true,
		readOnly : true 
	})
	dtDutyLeaderName.onClick(function() {
			var ps = empSelect();
			if (ps != null) {
				dtDutyLeader.setValue(ps.empId);
				dtDutyLeaderName.setValue(ps.empName);
			}
		});
	var dtDutyLeader = new Ext.form.Hidden({
		id : 'dtDutyLeader',
		name : 'model.dtDutyLeader'
	})
	// 管理责任人（大唐陕西）
	var dtManagerName = new Ext.form.TextField({
		fieldLabel : "管理责任人",
//		allowBlank : false,
		id : 'dtManagerName',
		name : 'dtManagerName',
		editable : false,
		disabled : true,
		readOnly : true 
	})
	dtManagerName.onClick(function() {
			var ps = empSelect();
			if (ps != null) {
				dtManager.setValue(ps.empId);
				dtManagerName.setValue(ps.empName);
			}
		});
	var dtManager = new Ext.form.Hidden({
		id : 'dtManager',
		name : 'model.dtManager'
	})
	// 技术责任人（大唐陕西）
	var dtTechnologyName = new Ext.form.TextField({
		fieldLabel : "技术责任人",
//		allowBlank : false,
		id : 'dtTechnologyName',
		name : 'dtTechnologyName',
		editable : false,
		disabled : true,
		readOnly : true
	})
	dtTechnologyName.onClick(function() {
			var ps = empSelect();
			if (ps != null) {
				dtTechnologyBy.setValue(ps.empId);
				dtTechnologyName.setValue(ps.empName);
			}
		});
	var dtTechnologyBy = new Ext.form.Hidden({
		id : 'dtTechnologyBy',
		name : 'model.dtTechnologyBy'
	})
	// 监督责任人（大唐陕西）
	var dtSuperviseName = new Ext.form.TextField({
		fieldLabel : "责任领导",
//		allowBlank : false,
		id : 'dtSuperviseName',
		name : 'dtSuperviseName',
		editable : false,
		disabled : true,
		readOnly : true
	})
	dtSuperviseName.onClick(function() {
			var ps = empSelect();
			if (ps != null) {
				dtSuperviseBy.setValue(ps.empId);
				dtSuperviseName.setValue(ps.empName);
			}
		});
	var dtSuperviseBy = new Ext.form.Hidden({
		id : 'dtSuperviseBy',
		name : 'model.dtSuperviseBy'
	})*/
	
	//修改人
	var entryBy = new Ext.form.Hidden({
		id : 'entryBy',
		name : 'model.entryBy'
	})
	var entryName = new Ext.form.TextField({
		id : 'entryName',
		name : 'entryName',
		fieldLabel : '修改人',
		readOnly : true
	})
	//修改部门
	var entryDept = new Ext.form.Hidden({
		id : 'entryDept',
		name : 'model.entryDept'
	})
	var entryDeptName = new Ext.form.TextField({
		id : 'entryDeptName',
		name : 'entryDeptName',
		fieldLabel : '修改部门',
		readOnly : true
	})
	// 修改时间
	var entryDateString = new Ext.form.TextField({
		fieldLabel : '修改时间',
		id : 'entryDateString',
		name : 'entryDateString',
		readOnly : true,
		value : getDate()
	})
	var memo = new Ext.form.TextArea({
		id : 'memo',
		name : 'model.memo',
		fieldLabel : '备注',
		readOnly : false,		
		width : 390,
		height : 50
	});
	
	// 发电企业标签
	var label1 = new Ext.form.Label({
				text : '大唐灞桥热电厂',
				border : false,
				width : 110
			})
	// 大唐陕西标签
	var label2 = new Ext.form.Label({
				text : '大唐陕西分公司',
				border : false,
				width : 110
			})
	
	var layout = new Ext.Viewport({
		layout : "border",
		border : false,
		items : [{
			title : '详细信息',
			region : "center",
			split : true,
			collapsible : false,
			titleCollapse : false,
			margins : '1',
			//tbar : tbar1,
			layout : 'border',
			items : [{
				region : "center",
				split : true,
				collapsible : false,
				titleCollapse : true,
				margins : '1',
				// layoutConfig : {
				// animate : true
				// },
				layout : 'fit',
				items : [antiGrid]
			}
			, {
				region : "south",
				title : '明细列表',
				layoutConfig : {
					animate : true
				},
				hidden:true,
				height : 240,
				border : false,
				layout : 'fit',
				items : [grid]
			}
			]
		}, {
			region : 'west',
			split : true,
			collapsible : true,
			titleCollapse : true,
			margins : '1',
			width : 220,
			layoutConfig : {
				animate : true
			},
			layout : 'fit',
			items : [treePanel]
		}]
	});
	function treeClick(node, e) {
		// 提示修改是否保存
		e.stopEvent();
		currentNode = node;
		node.toggle();
		if (currentNode.id != 0) {
			var temp = currentNode.id.length;
			//loadForm(currentNode);

			if (temp == 2) {
				antiDs.load({
					params : {
						id : currentNode.id
					}
				})
				/*Ext.getCmp("btnAdd").setDisabled(false);
				Ext.getCmp("btnDelete").setDisabled(false);
				Ext.getCmp("delete").setDisabled(false);
				//Ext.getCmp("add").setDisabled(false);
				Ext.getCmp("save").setDisabled(false);*/
			} else {
				/*if (currentNode.childNodes.length > 0) {
					Ext.getCmp("btnDelete").setDisabled(true);
				} else {
					Ext.getCmp("btnDelete").setDisabled(false);
				}
				Ext.getCmp("btnAdd").setDisabled(false);
				Ext.getCmp("delete").setDisabled(true);
				Ext.getCmp("add").setDisabled(true);
				Ext.getCmp("save").setDisabled(true);
				
				specialCode.setDisabled(true);
				fdDutyLeaderName.setDisabled(true);
				fdManagerName.setDisabled(true);
				fdTechnologyName.setDisabled(true);
				fdSuperviseName.setDisabled(true);
				
				dtDutyLeaderName.setDisabled(true);
				dtManagerName.setDisabled(true);
				dtTechnologyName.setDisabled(true);
				dtSuperviseName.setDisabled(true);
				
				ds.removeAll();*/
			}
		} else {
//			form.getForm().reset();
			getWorkCode();
//			ds.removeAll();
//			Ext.getCmp("btnAdd").setDisabled(false);
//			Ext.getCmp("btnDelete").setDisabled(true);
//			Ext.getCmp("add").setDisabled(true);
//			Ext.getCmp("delete").setDisabled(true);
//			Ext.getCmp("save").setDisabled(true);
//			
//			specialCode.setDisabled(true);
//			fdDutyLeaderName.setDisabled(true);
//			fdManagerName.setDisabled(true);
//			fdTechnologyName.setDisabled(true);
//			fdSuperviseName.setDisabled(true);

//			dtDutyLeaderName.setDisabled(true);
//			dtManagerName.setDisabled(true);
//			dtTechnologyName.setDisabled(true);
//			dtSuperviseName.setDisabled(true);
		}
//		saveDetail();
	};
	// 根据当前节点填充form
	function loadForm(o) {
		/*var ob = new Object();
		ob.measureCode = o.id;
//		ob.measureName = o.text;
		// 专业 责任领导（发电企业） 管理责任人（发电企业） 技术责任人（发电企业） 监督责任人（发电企业） 
		// 责任领导（大唐陕西）  管理责任人（大唐陕西) 技术责任人（大唐陕西） 监督责任人（大唐陕西） 
		var strC = o.attributes.code;
		var arrC = strC.split(",")
		var arr1C = arrC[0].split("-");
		ob.specialCode = arr1C[0];
		ob.specialName = arr1C[1];
		
		var arr2C = arrC[1].split("-");
		ob.fdDutyLeader = arr2C[0];
		ob.fdDutyLeaderName = arr2C[1];
		
		var arr3C = arrC[2].split("-");
		ob.fdManager = arr3C[0];
		ob.fdManagerName = arr3C[1];
		
		var arr4C = arrC[3].split("-");
		ob.fdTechnologyBy = arr4C[0];
		ob.fdTechnologyName = arr4C[1];
		
		var arr5C = arrC[4].split("-");
		ob.fdSuperviseBy = arr5C[0];
		ob.fdSuperviseName = arr5C[1];
		
		var arr6C = arrC[5].split("-");
		ob.dtDutyLeader = arr6C[0];
		ob.dtDutyLeaderName = arr6C[1];
		
		var arr7C = arrC[6].split("-");
		ob.dtManager = arr7C[0];
		ob.dtManagerName = arr7C[1];
		
		var arr8C = arrC[7].split("-");
		ob.dtTechnologyBy = arr8C[0];
		ob.dtTechnologyName = arr8C[1];
		
		var arr9C = arrC[8].split("-");
		ob.dtSuperviseBy = arr9C[0];
		ob.dtSuperviseName = arr9C[1];
		
		// 修改人，修改部门，修改时间
		var str = o.attributes.description;
		var arr = str.split(",")
		var arr1 = arr[0].split("-");
		ob.entryBy = arr1[0];
		ob.entryName = arr1[1];
		
		var arr2 = arr[1].split("-");
		ob.entryDept = arr2[0];
		ob.entryDeptDepName = arr2[1];
		
//		var arr3 = arr[2].split("-");
		ob.entryDateString = arr[2];

		ob.memo = o.attributes.href;
		
		measureCode.setValue(ob.measureC*/
//		ode);
//		measureName.setValue(ob.measureName.substring(
//				ob.measureCode.length + 1, ob.measureName.length));
//		memo.setValue(ob.memo);
		
		
//		specialCode.setValue(ob.specialName)
		/*if(ob.specialCode != 'null')
			specialCode.setValue(ob.specialCode)
		else */
			/*specialCode.setValue(null)
		
		fdDutyLeader.setValue(ob.fdDutyLeader);
		fdDutyLeaderName.setValue(ob.fdDutyLeaderName);
		fdManager.setValue(ob.fdManager);
		fdManagerName.setValue(ob.fdManagerName);
		fdTechnologyBy.setValue(ob.fdTechnologyBy);
		fdTechnologyName.setValue(ob.fdTechnologyName);
		fdSuperviseBy.setValue(ob.fdSuperviseBy);
		fdSuperviseName.setValue(ob.fdSuperviseName);
		
		dtDutyLeader.setValue(ob.dtDutyLeader);
		dtDutyLeaderName.setValue(ob.dtDutyLeaderName);
		dtManager.setValue(ob.dtManager);
		dtManagerName.setValue(ob.dtManagerName);
		dtTechnologyBy.setValue(ob.dtTechnologyBy);
		dtTechnologyName.setValue(ob.dtTechnologyName);
		dtSuperviseBy.setValue(ob.dtSuperviseBy);
		dtSuperviseName.setValue(ob.dtSuperviseName);

		
		entryBy.setValue(ob.entryBy);
		entryName.setValue(ob.entryName);
		entryDept.setValue(ob.entryDept);
		entryDeptName.setValue(ob.entryDeptDepName);
		entryDateString.setValue(ob.entryDateString)*/
	}
	function personSelect() {
		var args = {
			selectModel : 'signal',
			rootNode : {
				id : '-1',
				text : '灞桥电厂'
			}
		}
		var person = window
				.showModalDialog(
						'../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(person) != "undefined") {
			return person;
		} else {
			return null;
		}
	};
	
	function empSelect() {
		var args = new Object();
		var person = window
				.showModalDialog(
						'../empSelect/empSelect.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(person) != "undefined") {
			return person;
		} else {
			return null;
		}
	};
	function deptSelect() {
		var args = {
			selectModel : 'single',
			rootNode : {
				id : '0',
				text : '灞桥电厂'
			},
			onlyLeaf : false
		};
		var dept = window
				.showModalDialog(
						'../../../comm/jsp/hr/dept/dept.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(dept) != "undefined") {
			return dept;
		} else {
			return null;
		}
	};
	function saveDetail() {
		grid.stopEditing();
		var modifyRec = grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			Ext.Msg.confirm('提示信息', '是否确定修改？', function(button, text) {
				if (button == 'yes') {
					var updateData = new Array();
					var addData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						if (modifyRec[i].get("detailsId") == ""
								|| modifyRec[i].get("detailsId") == null) {
							addData.push(modifyRec[i].data);
						} else {
							updateData.push(modifyRec[i].data);
						}
					}
					Ext.Ajax.request({
						url : 'security/saveDetailMeasure.action',
						method : 'post',
						params : {
							addStr : Ext.util.JSON.encode(addData),
							updateStr : Ext.util.JSON.encode(updateData),
							deleteStr : ids.join(","),
							id : currentNode.id
						},
						success : function(result, request) {
							Ext.Msg.alert('提示', '操作成功');
							ds.rejectChanges();
							ids = [];
							ds.reload();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('提示信息', '操作失败！')
						}
					})
				}
			});
		}
	}
	
	
})                                                                                                                                                                                                                                