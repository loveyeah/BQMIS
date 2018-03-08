
// 获取时间
function getDate() {
    var d, s, t;
    d = new Date();
    s = d.getFullYear().toString(10) + "-";
    t = d.getMonth() + 1;
    s += (t > 9 ? "" : "0") + t + "-";
    t = d.getDate();
    s += (t > 9 ? "" : "0") + t + " ";
    t = d.getHours();
    s += (t > 9 ? "" : "0") + t + ":";
    t = d.getMinutes();
    s += (t > 9 ? "" : "0") + t + ":";
    t = d.getSeconds();
    s += (t > 9 ? "" : "0") + t;
    return s;
}
Ext.onReady(function() {
	var workticketNo=getParameter("workticketNo");
//	   var returnBackDate = new Ext.form.TextField({
//        id : 'returnBackDate',
//        name : 'oldApprovedFinishDate',
//        style : 'cursor:pointer',
//        anchor : "80%",
//        value : getDate(),
//        
//        listeners : {
//            focus : function() {
//                WdatePicker({
//                	autoPickDate:true,
//                    startDate : '%y-%M-%d 00:00:00',
//                    dateFmt : 'yyyy-MM-dd HH:mm:ss',
//                    alwaysUseStartDate : true 
//                });
//            }
//        }
//    })
    
    var chargeByCode=new Ext.form.TextField({
       id:'chargeByCode',
       name:'chargeByCode'
    });
    
      var permitByCode=new Ext.form.TextField({
       id:'permitByCode',
       name:'permitByCode'
    });
 
	
	  var chargeByName = new Ext.form.ComboBox({
		name : 'chargeByName',
	//	xtype : 'combo',
		id : 'chargeByName',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName:'oldChargeByName',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '合肥电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(emp) != "undefined") {
				Ext.getCmp('chargeByName').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('chargeByName'), emp.workerName);
				chargeByCode.setValue(emp.workerCode);
				grid.getSelectionModel().getSelected()
									.set("oldChargeBy",
											chargeByCode.getValue());
			}
		}
	});
	
	 var permitByName = new Ext.form.ComboBox({
		name : 'permitByName',
		hiddenName:'newChargeByName',
	//	xtype : 'combo',
		id : 'permitByName',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '合肥电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(emp) != "undefined") {
				Ext.getCmp('permitByName').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('permitByName'), emp.workerName);
						permitByCode.setValue(emp.workerCode);
						
						grid.getSelectionModel().getSelected()
									.set("newChargeBy",
											permitByCode.getValue());
			}
		}
	});
	
	
		// 定义状态
	var stateData = new Ext.data.SimpleStore({
				data : [[5,'交回'], [6,'恢复']],
				fields : ['value', 'name']
			});
	// 定义状态
	var stateComboBox = new Ext.form.ComboBox({
				id : "stateCob",
				store : stateData,
				displayField : "name",
				valueField : "value",
				mode : 'local',
				triggerAction : 'all',
				//hiddenName:'stateComboBox',
				readOnly : true,
				value:'7',
				width : 140
			});
	
	
var MyRecord = Ext.data.Record.create([
	{name : 'id'},
    {name : 'oldApprovedFinishDate'},
    {name : 'oldChargeBy'},
    {name : 'newChargeBy'},
    {name : 'changeStatus'},
    {name : 'oldChargeByName'},
    {name : 'newChargeByName'}
	]);

	var DataProxy = new Ext.data.HttpProxy(

			{
				url:'bqworkticket/findReturnBackList.action'
			}

	);

	var TheReader = new Ext.data.JsonReader({
		//root : "list"

	}, MyRecord);

	var  store = new Ext.data.Store({
		 
		proxy : DataProxy,

		reader : TheReader

	});
	    store.load({
		params : {
			workticketNo : workticketNo
		}
	});
        store.on('beforeload', function() {
		Ext.apply(this.baseParams, {
         workticketNo : workticketNo
		});
	});                        

var sm = new Ext.grid.CheckboxSelectionModel();

	var grid = new Ext.grid.EditorGridPanel({
		store : store,
		columns : [sm,
		new Ext.grid.RowNumberer({
		header : '序号',
		width : 40,
		height:100,
		align : 'center'
	}),{
			
			header : "ID",
			width : 75,
			sortable : true,
			dataIndex : 'id',
			hidden:true
		},{
			header : "交回/恢复时间",
			width : 150,
			sortable : true,
			dataIndex : 'oldApprovedFinishDate',
				renderer : function(value) {
			if (!value)
				return '';
			if (value instanceof Date)
				return renderDate(value);
			var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
			var reTime = /\d{2}:\d{2}:\d{2}/gi;
			var strDate = value.match(reDate);
			var strTime = value.match(reTime);
			if (!strDate)
				return "";
			strTime = strTime ? strTime : '00:00:00';
			return strDate + " " + strTime;
		},
		editor : new Ext.form.TextField({
			readOnly : true,
			id : "temp",
			listeners : {
				focus : function() {
					WdatePicker({
						startDate : '%y-%M-%d 00:00:00',
						dateFmt : 'yyyy-MM-dd HH:mm:ss',
						alwaysUseStartDate : true
						,
						onpicked : function() {
							if (Ext.get("temp").dom.value < new Date()
									.dateFormat('Y-m-d 00:00:00')) {
								Ext.Msg.alert(Constants.ERROR, "完成时间不能小于当前时间！");
								return false;
							}
							grid.getSelectionModel().getSelected()
									.set("oldApprovedFinishDate",
											Ext.get("temp").dom.value);
						}
					});
				}
			}
		})
		},{
			header : "工作许可人编码",
			width : 200,
			sortable : true,
			dataIndex : 'newChargeBy',
			editor : permitByCode,
			align : 'left',
			hidden:true
		},{
			header : "工作负责人编码",
			width : 200,
			sortable : true,
			dataIndex : 'oldChargeBy',
			editor :chargeByCode,
			align : 'left',
			hidden:true
		},{
			header : "工作许可人",
			width : 180,
			sortable : true,
			dataIndex : 'newChargeByName',
			editor : permitByName,
			align : 'left'
		},{
			header : "工作负责人",
			width : 180,
			sortable : true,
			dataIndex : 'oldChargeByName',
			editor :chargeByName,
			align : 'left'
		},{
			header : "状态",
			width : 80,
			sortable : true,
			dataIndex : 'changeStatus',
			editor :stateComboBox,
			align : 'left',
					renderer : function changeIt(val) {
					if(val=="5") return "交回";
					if(val=="6") return "恢复";
			}
		}
		],
		clicksToEdit : 1,
		sm : sm,
		tbar:['交回/恢复  ',
			{ text:'增加',
			iconCls:'add',
			handler:addRecord},
			{text:'保存',
			iconCls:'update',
			handler:saveRecord
			},{
				text:'删除',
				iconCls:'delete',
				handler:deleteRecord
			},
			{
				text:'刷新',
				iconCls:'query',
				handler:function(){store.reload();}
			}
		]
			//分页
//		bbar : new Ext.PagingToolbar({
//			pageSize : 18,
//			store : store,
//			displayInfo : true,
//			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
//			emptyMsg : "没有记录"
//		})
	});
	
	function addRecord()
	{
		
	var	mycount = grid.getStore().getCount();
	var m;
	if(mycount%2==0)
	{
	            m = new MyRecord({
	           	id:"",
				oldApprovedFinishDate:getDate(),
				newChargeBy:"",
				oldChargeBy:"",
				newChargeByName:"",
				oldChargeByName:"",
				changeStatus:"5"
				});
	}
	else
	{
		 m = new MyRecord({
	           	id:"",
				oldApprovedFinishDate:getDate(),
				newChargeBy:"",
				oldChargeBy:"",
				newChargeByName:"",
				oldChargeByName:"",
				changeStatus:"6"
				});
	}
		mycount = grid.getStore().getCount();
				grid.stopEditing();
		store.insert(mycount, m);
		grid.startEditing(mycount + 1, 0);
		
	}
	
	function saveRecord()
	{
	    var str = "[";
		var record = grid.getStore().getModifiedRecords();
		if (record.length > 0) {
			for (var i = 0; i < record.length; i++) {
				var data = record[i];
				str = str + "{'id':'" + data.get("id")
				        +"','oldApprovedFinishDate':'"+data.get("oldApprovedFinishDate")
						+ "','newChargeBy':'" + data.get("newChargeBy")
						+"','oldChargeBy':'" + data.get("oldChargeBy")
						+"','changeStatus':'" + data.get("changeStatus")
						+"','workticketNo':'"+workticketNo
						+ "'},"
			}
			if (str.length > 1) {
				str = str.substring(0, str.length - 1);
			}
			str = str + "]";
            //  alert(str);
			Ext.Ajax.request({
				url : 'bqworkticket/saveReturnBackInfo.action',
				params : {
					data : str
				},
				method : 'post',
				waitMsg : '正在加载数据...',
				success : function(result, request) {
					var json = eval('(' + result.responseText + ')');
					Ext.Msg.alert("注意", json.msg);
					if(json.msg=="保存成功！")
					{
					store.reload();
					store.rejectChanges();
					}
					
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('错误', '操作失败!');
				}
			});
		} else {
			alert("没有对数据进行任何修改！");
		}
	}
	
	function deleteRecord()
	{
		
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {

			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.id) {
					ids.push(member.id); 
				} else {
					
					store.remove(store.getAt(i));
				}
			}
			if(ids=="")
			{
				Ext.Msg.alert("提示", "请选择要删除的记录！");
				return;
			}
			
			Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(
					buttonobj) {

				if (buttonobj == "yes") {

					Ext.lib.Ajax.request('POST',
							'bqworkticket/deleteReturnBackInfo.action', {
								success : function(action) {
									Ext.Msg.alert("提示", "删除成功！")
								
						         	store.reload();
								},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
							}, 'ids=' + ids);
				}
			});
		}

	}
	
		new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		layout : "fit",
		items : [grid]
	});
	
})