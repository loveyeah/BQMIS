Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var runlogId = getParameter("runlogId");

	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'shiftParmId'
	}, {
		name : 'runlogParmId'
	}, {
		name : 'parmName'
	}, {
		name : 'itemNumberValue'
	}, {
		name : 'unitName'
	}]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url : 'runlog/findShifParmList.action'
			}

	);

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"

	}, MyRecord);

	var store = new Ext.data.Store({

		proxy : dataProxy,

		reader : theReader

	});

	store.load({
		params : {
			runlogId : runlogId

		}
	});

	var sm = new Ext.grid.CheckboxSelectionModel();
	var nm = new Ext.grid.RowNumberer();

	var grid = new Ext.grid.EditorGridPanel({
		border : false,
		autoScroll : true,
		width : 200,
		fitToFrame : true,
		store : store,
		columns : [nm, {
			header : "ID",
			width : 10,
			sortable : true,
			dataIndex : 'shiftParmId',
			hidden : true
		}, {
			header : "参数ID",
			width : 10,
			sortable : true,
			dataIndex : 'runlogParmId',
			hidden : true
		}, {
			header : "参数名称",
			width : 200,
			sortable : true,
			dataIndex : 'parmName'
		}, {
			header : "参数值",
			width : 100,
			sortable : true,
			dataIndex : 'itemNumberValue',
			editor : new Ext.form.NumberField({
				allowBlank : true,
				align : 'left'
			}),
			align : 'left'
		}, {
			header : "计量单位",
			width : 100,
			sortable : true,
			dataIndex : 'unitName'
		}],
		clicksToEdit : 2,
		// autoSizeColumns : true,
		// viewConfig : {
		// forceFit : true
		// },
		tbar : [{
			text : '保存',
			iconCls : 'save',
			handler : saveParm
		}, '-', {
			text : '刷新',
			iconCls : 'reflesh',
			handler : function() {
				store.load({
					params : {
						runlogId : runlogId
					}
				});
			}
		}]

			// title : '运行参数维护',

	});

	function saveParm() {
		var str = "[";
		var record = grid.getStore().getModifiedRecords();
		if (record.length > 0) {
			for (var i = 0; i < record.length; i++) {
				var data = record[i];
				str = str + "{'shiftParmId':" + data.get("shiftParmId")
						+ ",'itemNumberValue':'" + data.get("itemNumberValue")
						+ "'},"
			}
			if (str.length > 1) {
				str = str.substring(0, str.length - 1);
			}
			str = str + "]";

			Ext.Ajax.request({
				url : 'runlog/addShifParm.action',
				params : {
					data : str
				},
				method : 'post',
				waitMsg : '正在加载数据...',
				success : function(result, request) {
					var json = eval('(' + result.responseText + ')');
					Ext.Msg.alert("注意", json.msg);
					store.reload();
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('错误', '操作失败!');
				}
			});
		} else {
			Ext.Msg.alert("提示", "没有对数据进行任何修改！");
		}

	}

	// ---------------------------------------

	var view = new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		// split : true,
		items : [grid]
	});

});