Ext.onReady(function() {
	var MyRecord = Ext.data.Record.create([{
		name : 'deptName'
	},
	{name : 'registerTime'
	},
	{name:'deptRegisterId'}
	])
	var grid;
	var recordValue;
	var yearValue
	var sesonValue;
	var view;
	var ds;
	
	
	function getYear() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10);
		return s;
	}
		
  
	

	
	

	function query() {
		//QuarterTime.setValue(QuarterTime.getValue());
		
/*		if (dateTime == null || dateTime == '') {
			Ext.Msg.alert("提示", "时间必填");
			return;
		}*/
			//发送AJAX请求
    	//alert(Ext.getCmp("unitCode").getValue());
	    Ext.Ajax.request({
			url : 'hr/getAllRegisterInfo.action',
			params : {
				year :Ext.getCmp("findingMonth").getValue(),
				season:sesonValue
			},
			method : 'post',
			waitMsg : '正在加载数据...',
			success : function(result, request) {
				var json = eval('(' + result.responseText + ')');
				createGrid(json,Ext.getCmp("findingMonth").getValue(),sesonValue);
			},
			failure : function(result, request) {
				Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
				Ext.Msg.hide();
			}
		});
		//Ext.Msg.wait("正在查询数据!请等待...");
	//	query();
	}
	

					
			
	
	createGrid(null);
	
	function createGrid(json,myYear,mySeason){
	    if(json==null){
	    		var findingYear = new Ext.form.TextField({
				style : 'cursor:pointer',
				id : 'findingMonth',
				columnWidth : 0.5,
				readOnly : true,
				anchor : "70%",
				fieldLabel : '月份',
				name : 'month',
				value : getYear(),
				listeners : {
					focus : function() {
						WdatePicker({
							startDate : '%y',
							dateFmt : 'yyyy',
							alwaysUseStartDate : true
						});
						this.blur();
					}
				}
			});  
			
		
		     QuarterTime =new Ext.form.ComboBox({
								fieldLabel : '年度',
								id : 'unitCode',
								name : 'retrunValue',
								//allowBlank : true,
								//style : "border-bottom:solid 2px",
								triggerAction : 'all',
								//editable : false,
								store : new Ext.data.SimpleStore({
															fields : ["retrunValue","displayText"],
															data : [['1', '第一季度'], ['2', '第二季度'], ['3', '第三季度'], ['4', '第四季度']]
														}),
								valueField : 'retrunValue',
								displayField : 'displayText',
								mode : 'local',
								anchor : '85%',
								listeners:{
									select :function(){
										//alert(QuarterTime.getValue());
										//leftPanel.set("registerTime", QuarterTime.getValue());
										sesonValue=this.value;
									}
								}});
						var btnQuery = new Ext.Button({
							text : '查询',
							iconCls : 'query',
							handler : query
						});
							var btnSave = new Ext.Button({
							text : '保存',
							iconCls : 'save',
							handler : saveData
						});
			
			    	var tbar = new Ext.Toolbar({
								id : 'tbar',
								height : 25,
								items : ['发放年度：', findingYear, '-', '发放时间：', QuarterTime, ' ',
										btnQuery,'-',btnSave]
			        });
	    	 new Ext.Viewport({
					layout : 'fit',
					items : [tbar]
				})
				
				
				
				
				
				
	    }else{
	    	var findingYear = new Ext.form.TextField({
				style : 'cursor:pointer',
				id : 'findingMonth',
				columnWidth : 0.5,
				readOnly : true,
				anchor : "70%",
				fieldLabel : '月份',
				name : 'month',
				value : myYear,
				listeners : {
					focus : function() {
						WdatePicker({
							startDate : '%y',
							dateFmt : 'yyyy',
							alwaysUseStartDate : true
						});
						this.blur();
					}
				}
			});  
			
		
		      var QuarterTime =new Ext.form.ComboBox({
								fieldLabel : '年度',
								id : 'unitCode',
								name : 'retrunValue',
								allowBlank : true,
								style : "border-bottom:solid 2px",
								triggerAction : 'all',
								editable : false,
								store : new Ext.data.SimpleStore({
															fields : ["retrunValue","displayText"],
															data : [['1', '第一季度'], ['2', '第二季度'], ['3', '第三季度'], ['4', '第四季度']]
														}),
								valueField : 'retrunValue',
								value:mySeason,
								displayField : 'displayText',
								mode : 'local',
								anchor : '85%',
								listeners:{
									select :function(){
										sesonValue=this.value;
									}
								}});
						var btnQuery = new Ext.Button({
							text : '查询',
							iconCls : 'query',
							handler : query
						});
							var btnSave = new Ext.Button({
							text : '保存',
							iconCls : 'save',
							handler : saveData
						});
			
			    	var tbar = new Ext.Toolbar({
								id : 'tbar',
								height : 25,
								items : ['发放年度：', findingYear, '-', '发放时间：', QuarterTime, ' ',
										btnQuery,'-',btnSave]
			        });
	    	
		    var cm = new Ext.grid.ColumnModel({
				columns : json.columModle,
				defaultSortable : false,
				rows : json.rows
			});
			//cm.setRenderer(0, changeColor);
			var ds = new Ext.data.JsonStore({
				data : json.data,
				fields : json.fieldsNames
			})	;
			view=	new Ext.Viewport({
					layout : 'fit',
					items : [{
						id:'grid',
						xtype : 'editorgrid',
						store : ds,
						colModel : cm,
						enableColumnMove : false,
						viewConfig : {
							forceFit : false
						},
						plugins : [new Ext.ux.plugins.GroupHeaderGrid()],
						tbar : tbar
					}]
				})
	    }
		
	}
	function saveData() {
			var grid=view.findById("grid");
	        grid.stopEditing();
	        var modifyRec = grid.getStore().getModifiedRecords();
	        if (modifyRec.length > 0 ) {
				if (!confirm("确定要保存修改吗?"))
					return;
	 var ds=grid.getStore();
	        //--------modify by fyyang 100105---------------------------
	        var mod = new Array();
			
		for (var i = 0; i <= ds.getTotalCount() - 1; i++) {
			//var laborRegisterId = ds.getAt(i).get('loborRegisterId');
		
			var deptCode = ds.getAt(i).get('deptCode');
			// 种类
			var laborMaterialId = new Array();
			// 值
			var materialNum = new Array();
			for (var j = 4; j < grid.getColumnModel().getColumnCount(); j++) {
				var dataIndex = grid.getColumnModel().getDataIndex(j);
				laborMaterialId.push(dataIndex);
				materialNum.push(grid.getStore().getAt(i).get(dataIndex));
			}
			obj = {
				deptCode : deptCode,
				laborMaterialId : laborMaterialId,
				materialNum : materialNum
			};
			mod.push(obj);
		}
	        //-----------------------------------------------------------------
	        
	     
	        
	
		Ext.Ajax.request({
			url : 'hr/saveLaborRegisterInfo.action',
			method : 'post',
			params : {
                addOrUpdateRecords:Ext.util.JSON.encode(mod),
                year :Ext.getCmp("findingMonth").getValue(),
				season:sesonValue
			},
			
			success : function(result, request) {
				Ext.Msg.alert('提示信息', "操作成功!");
				//grid.reload();
	          query();
	          //alert(addOrUpdateRecords);
				
			},
			failure : function(result, request) {
				Ext.Msg.alert('提示信息', '操作失败')
			}
		})
		
		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	
	}
	



/*	var view = new Ext.Viewport({
		layout : 'fit',
		margins : '0 0 0 0',
		collapsible : true,
		split : true,
		border : false,
		items : [new Ext.Panel({
			id : 'panel',
			border : false,
			tbar : tbar,
			items : [grid 
				//html : '<div id="gridDiv"></div>'
			]
		})]
	});*/
	/*Ext.get('gridDiv').setWidth(Ext.get('leftPanel').getWidth());
	Ext.get('gridDiv').setHeight(Ext.get('leftPanel').getHeight() - 25);*/
});
