Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	function getMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString() + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		return s;
	}
	var quarterDate = new Ext.form.TextField({
				id : 'quarterDate',
				name : '_quarterDate',
				fieldLabel : "年份",
				style : 'cursor:pointer',
				cls : 'Wdate',
				width : 90,
				value : getMonth(),
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M',
									dateFmt : 'yyyy-MM',
									alwaysUseStartDate : true,
									isShowClear : false
								});
					}
				}
			});

	var quarterBox = new Ext.form.ComboBox({
				fieldLabel : '问题类别',
				store : [['1', '春查'], ['2', '秋查'], ['3', '安评'], ['4', '重大危险源'],
						['5', '技术监控'], ['6', '25项反措']],
				id : 'quarterBox',
				name : 'quarterBoxName',
				valueField : "value",
				displayField : "text",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'quarterBoxName',
				editable : false,
				triggerAction : 'all',
				width : 85,
				selectOnFocus : true,
				value : 1
			});

	// 定义grid
	var MyRecord = Ext.data.Record.create([{
				name : 'dutyDeptCode',
				mapping : 0
			}, {
				name : 'deptName',
				mapping : 1
			}, {
				name : 'total',
				mapping : 2
			}, {
				name : 'finish',
				mapping : 3
			}, {
				name : 'noFinish',
				mapping : 4
			}, {
				name : 'finishFrank',
				mapping : 5
			}]);

	var dataProxy = new Ext.data.HttpProxy(

	{
				url : 'security/findMatterGather.action'
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

	var sm = new Ext.grid.CheckboxSelectionModel();

	var grid = new Ext.grid.GridPanel({
				region : "center",
				layout : 'fit',
				store : store,

				columns : [sm, new Ext.grid.RowNumberer({
									header : '序号',
									width : 50
								}), {
							header : "ID",
							width : 75,
							sortable : true,
							hidden : true,
							dataIndex : 'dutyDeptCode'

						}, {
							header : "部门",
							width : 75,
							sortable : true,
							dataIndex : 'deptName'
						},

						{
							header : "问题总数",
							width : 75,
							sortable : true,
							dataIndex : 'total'
						}, {
							header : "完成项目",
							width : 75,
							sortable : true,
							dataIndex : 'finish'
						}, {
							header : "逾期未完",
							width : 75,
							sortable : true,
							dataIndex : 'noFinish'
						}, {
							header : "完成率",
							width : 75,
							sortable : true,
							dataIndex : 'finishFrank'
						}],
				sm : sm,
				autoSizeColumns : true,
				viewConfig : {
					forceFit : true
				},
				tbar : ['计划完成时间：', quarterDate, "-", '问题类别：', quarterBox, '-',
						{
							text : "查询",
							iconCls : 'query',
							handler : queryRecord
						}],
				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : store,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						})
			});

	grid.on("rowdblclick", queryRecordlist);
	// ---------------------------------------

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				// layout : "fit",
				items : [grid]
			});

	// -------------------

	// 查询
	function queryRecord() {

		store.baseParams = {
			monthDate : quarterDate.getValue(),
			problemKind : quarterBox.getValue()

		};
		store.load({
					params : {
						start : 0,
						limit : 18
					}
				});
	}
	function queryRecordlist() {
		var record = grid.getSelectionModel().getSelected();
		var args = {
			selectModel : 'signal',
			notIn : "",
			rootNode : {
				monthDate : quarterDate.getValue(),
				problemKind : quarterBox.getValue(),
				deptCode : record.get('dutyDeptCode')
			}
		}
		var person = window
				.showModalDialog(
						'matterGatherQuery.jsp',
						args,
						'dialogWidth:1000px;dialogHeight:500px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
	}
	queryRecord();

});