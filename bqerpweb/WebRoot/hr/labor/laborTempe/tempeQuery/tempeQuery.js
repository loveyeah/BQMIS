Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	
	function getYear() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) ;
		
		return s;
	}

		// 系统当前时间
	function getToDate() {
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
							workercode.setValue(result.workerCode);
							editName.setValue(result.workerName);
							editDate.setValue(editTime.getValue());
							feeResources.setValue("劳保费");
							query();
						}
					}
				});
	}
	
	
	var con_item = Ext.data.Record.create([{
				name : 'tempeDetailId',
				mapping : 0
			}, {
				name : 'tempeId',
				mapping : 1
			}, {
				name : 'deptName',
				mapping : 2
			}, {
				name : 'factNum',
				mapping : 3
			}, {
				name : 'highTempeNum',
				mapping : 4
			}, {
				name : 'highTempeStandard',
				mapping : 5
			}, {
				name : 'highAmount',
				mapping : 6
			}, {
				name : 'midTempeNum',
				mapping : 7
			}, {
				name : 'midTempeStandard',
				mapping : 8
			}, {
				name : 'midAmount',
				mapping : 9
			}, {
				name : 'lowTempeNum',
				mapping : 10
			}, {
				name : 'lowTempeStandard',
				mapping : 11
			}, {
				name : 'lowAmount',
				mapping : 12
			},  {
				name : 'sumAmount',
				mapping : 13
			},{
				name : 'memo',
				mapping : 14
			},{
				name : 'tempeMonth',
				mapping : 15
			},{
				name : 'costItem',
				mapping : 16
			},{
				name : 'tempeState',
				mapping : 17
			},{
				name : 'workFlowNo',
				mapping : 18
			}]);

	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'hr/getHrJLaborTempeList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, con_item)
			});
	
	con_ds.on("load", function(){
				var a1 = 0.0,a2=0.0,a3=0.0,b1=0,a4=0.0,a5=0.0,a6=0.0,b2=0,a7=0.0,a8=0.0,a9=0.0,b3=0,a10=0.0,a11=0.0;
			
				con_ds.each(function(rec) {
				if(rec.get('deptName') != "合计"){
				 if (rec.get('factNum') != null)
								a1 += rec.get('factNum');
							if (rec.get('highTempeNum') != null)
								a2 += rec.get('highTempeNum');
							if (rec.get('highTempeStandard') != null){
								a3 += rec.get('highTempeStandard');
							    b1+=1;
							}
							if (rec.get('highAmount') != null)
								a4 += rec.get('highAmount');
							if (rec.get('midTempeNum') != null)
								a5 += rec.get('midTempeNum');
							if (rec.get('midTempeStandard') != null){
								a6 += rec.get('midTempeStandard');
							    b2+=1;
							}
							if (rec.get('midAmount') != null)
								a7 += rec.get('midAmount');
							if (rec.get('lowTempeNum') != null)
								a8 += rec.get('lowTempeNum');
							if (rec.get('lowTempeStandard') != null){
								a9 += rec.get('lowTempeStandard');
							   b3+=1;
							}
							if (rec.get('lowAmount') != null)
								a10 += rec.get('lowAmount');
							if (rec.get('sumAmount') != null)
								a11 += rec.get('sumAmount');
						
				}})
				if(b1==0)
				  a3=0.0;
				else
				   a3/=b1;
				if(b2==0)  
				   a6=0.0;
				else
				   a6/=b2;
				if(a9==0)
				   a9=0.0;
				else
			       a9/=b3;
				var addCount = new con_item({});
				addCount.set('deptName','本页合计')
				addCount.set('factNum',a1);
				addCount.set('highTempeNum',a2);
				addCount.set('highTempeStandard',a3);
				addCount.set('highAmount',a4);
				addCount.set('midTempeNum',a5);
				addCount.set('midTempeStandard',a6);
				addCount.set('midAmount',a7);
				addCount.set('lowTempeNum',a8);
				addCount.set('lowTempeStandard',a9);
				addCount.set('lowAmount',a10);
				addCount.set('sumAmount',a11);
				
				if(con_ds.getAt(con_ds.getCount()-1).get("deptName") == "合计")
				    con_ds.insert(con_ds.getCount()-1,addCount);
				else
				    con_ds.add(addCount);
			});
			
	var con_item_cm = new Ext.grid.ColumnModel([con_sm,
			{
				header : "部门",
				align : 'left',
				dataIndex : 'deptName'
			}, {
				header : '实有人数',
				dataIndex : 'factNum',
				align : 'center'

			}, {
				header : '高温标准人数',
				dataIndex : 'highTempeNum',
				align : 'center'

			}, {
				header : '高温标准',
				dataIndex : 'highTempeStandard',
				align : 'center',
				renderer:function(value){
					if(value==""||value==null) return;
				    else return value.toFixed(2);
				}

			}, {
				header : '高温金额（元）',
				dataIndex : 'highAmount',
				align : 'center'

			}, {
				header : '中温标准人数',
				dataIndex : 'midTempeNum',
				align : 'center'

			}, {
				header : '中温标准',
				dataIndex : 'midTempeStandard',
				align : 'center',
				renderer:function(value){
					if(value==""||value==null) return;
				    else return value.toFixed(2);
				}

			}, {
				header : '中温金额（元）',
				dataIndex : 'midAmount',
				align : 'center'

			}, {
				header : '低温标准人数',
				dataIndex : 'lowTempeNum',
				align : 'center'

			}, {
				header : '低温标准',
				dataIndex : 'lowTempeStandard',
				align : 'center',
				renderer:function(value){
					if(value==""||value==null) return;
				    else return value.toFixed(2);
				}

			}, {
				header : '低温金额（元）',
				dataIndex : 'lowAmount',
				align : 'center'

			}, {
				header : '总额（元）',
				dataIndex : 'sumAmount',
				align : 'center'

			}, {
				header : '备注',
				dataIndex : 'memo',
				width : 100,
				align : 'left'
			}]);
	con_item_cm.defaultSortable = true;
	var workercode = new Ext.form.Hidden({
				id : 'workercode',
				name : 'workercode'
			})
	var editName = new Ext.form.TextField({
				id : 'editName',
				readOnly : true
			})
	var editDate = new Ext.form.TextField({
				id : 'editDate',
				readOnly : true
			})
	
	var feeResources= new Ext.form.TextField({
				id : 'feeResources',
				name : 'feeResources',
				readOnly : true
			})
			
	var gridbbar = new Ext.Toolbar({
				items : [ '制表人：', editName, '制表时间：',editDate, '费用来源：',feeResources]
			})
			
	var editTime = new Ext.form.TextField({
				name : 'editTime',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '',
									alwaysUseStartDate : true,
									dateFmt : "yyyy-MM-dd"
								});
						this.blur();
					}
				}
			});		
	editTime.setValue(getToDate());	
			
	//月份
	var tempeMonth = new Ext.form.TextField({
				name : 'tempeMonth',
				fieldLabel : '月份',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '',
									alwaysUseStartDate : true,
									dateFmt : "yyyy",
									isShowClear : false
								});
						this.blur();
					}
				}

			});
	tempeMonth.setValue(getYear());

	function query() {
		con_ds.baseParams = {
			tempeMonth : tempeMonth.getValue(),
			flag:"query"
		}
			con_ds.load({
						params : {
							start : 0,
							limit : 18
						}
		})
	}

var tbar = new Ext.Toolbar({
				items : ["年份:", tempeMonth, '-', {
							text : '查询',
							iconCls : 'query',
							handler : query
						}, '-', {
							text : "审批查询",
							id : 'btnapprove',
							iconCls : 'approve',
							handler : approveQuery
						}]
			});
	
	var grid = new Ext.grid.EditorGridPanel({
				sm : con_sm,
				ds : con_ds,
				cm : con_item_cm,
				tbar : tbar,
				border : false,
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : con_ds,
							displayInfo : true,
							displayMsg : "显示第{0}条到{1}条，共{2}条",
							emptyMsg : "没有记录",
							beforePageText : '',
							afterPageText : ""
						})
			});
			
   function approveQuery() {
     	if (!con_sm.hasSelection() || con_sm.getSelections().length > 1)
			Ext.Msg.alert('提示', '请选择一条数据进行查看！');
		else {
			var rec = con_sm.getSelected();
			var workflowType = "bqHrJLaborTempeApprove";
			var entryId = rec.get('workFlowNo');
			var url;
			if (entryId == null || entryId == "")
				url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
						+ workflowType;
			else
				url = url = "/power/workflow/manager/show/show.jsp?entryId="
						+ entryId;
			window.open(url);

		}
   }


	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [{
							layout : 'fit',
							border : false,
							frame : false,
							region : "center",
							items : [grid]
						},{
							region : 'south',
							layout : 'fit',
							height : 25,
							items : [gridbbar]
						}]
			});

	getWorkCode();
})
