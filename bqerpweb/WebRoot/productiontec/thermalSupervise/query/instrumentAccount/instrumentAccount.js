Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

			
			// 仪器名称
			var names = new Ext.form.TextField({
						fieldLabel : "名称",
						name : 'model.names',
						anchor : '95%',
						readOnly : true

					});
			// 类别
			var lb_item = Ext.data.Record.create([{
						name : 'yqyblbName'
					}, {
						name : 'yqyblbId'
					}]);
			var lbStore = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : 'productionrec/findLBList.action'
								}),
						reader : new Ext.data.JsonReader({
									totalCount : "totalCount",
									root : "list"
								}, lb_item)
					});
			lbStore.load({
						params : {
							jdzyId : JDZY_ID
						}
					})

			var yqyblbId = new Ext.form.ComboBox({

						hiddenName : "model.yqyblbId",
						fieldLabel : "类别",
						// triggerAction : 'all',
						store : lbStore,
						displayField : "yqyblbName",
						disabled : true,
						valueField : "yqyblbId",
						emptyText : '请选择...',
						mode : 'local',
						readOnly : true,
						
						anchor : "90%"
					});
			// 等级
			var dj_item = Ext.data.Record.create([{
						name : 'yqybdjName'
					}, {
						name : 'yqybdjId'
					}]);
			var djStore = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : 'productionrec/findDJList.action'
								}),
						reader : new Ext.data.JsonReader({
									totalCount : "totalCount",
									root : "list"
								}, dj_item)
					});
			djStore.load({
						params : {
							jdzyId : JDZY_ID
						}
					})

			var yqybdjId = new Ext.form.ComboBox({
						id : "",
						name : "",
						hiddenName : "model.yqybdjId",
						fieldLabel : "等级",
						// triggerAction : 'all',
						store : djStore,
						displayField : "yqybdjName",
						valueField : "yqybdjId",
						emptyText : '请选择...',
						disabled : true,
						mode : 'local',
						readOnly : true,
					
						anchor : "90%"
					});
			// 精度
			var jd_item = Ext.data.Record.create([{
						name : 'yqybjdName'
					}, {
						name : 'yqybjdId'
					}]);
			var jdStore = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : 'productionrec/findJDList.action'
								}),
						reader : new Ext.data.JsonReader({
									totalCount : "totalCount",
									root : "list"
								}, jd_item)
					});
			jdStore.load({
						params : {
							jdzyId : JDZY_ID
						}
					})

			var yqybjdId = new Ext.form.ComboBox({
						id : "",
						name : "",
						hiddenName : "model.yqybjdId",
						fieldLabel : "精度",
						// triggerAction : 'all',
						store : jdStore,
						displayField : "yqybjdName",
						valueField : "yqybjdId",
						emptyText : '请选择...',
						mode : 'local',
						disabled : true,
						readOnly : true,
//						allowBlank : false,
						anchor : "90%"
					});

			// 年度
			var userRange = new Ext.form.TextField({
						fieldLabel : '量程',
						name : 'model.userRange',
						anchor : '90%',
						readOnly : true

					})
			// 型号
			var sizes = new Ext.form.TextField({
						fieldLabel : '型号',
						name : 'model.sizes',
						anchor : '95%',
						readOnly : true

					})
			// 生产厂商
			var factory = new Ext.form.TextField({
						fieldLabel : '生产厂商',
						name : 'model.factory',
						anchor : '95%',
						readOnly : true

					})

			// 出厂编号
			var outFactoryNo = new Ext.form.TextField({
						fieldLabel : '出厂编号',
						name : 'model.outFactoryNo',
						anchor : '90%',
						readOnly : true

					})
			// 出厂编号
			var outFactoryDate = new Ext.form.TextField({
						format : 'Y-m-d',
						fieldLabel : '出厂日期',
						name : 'model.outFactoryDate',
						anchor : '90%',
						readOnly : true
					})
			// 购买日期
			var buyDate = new Ext.form.TextField({
						format : 'Y-m-d',
						fieldLabel : '购买日期',
						name : 'model.buyDate',
						anchor : '90%',
						readOnly : true
					})
			// 投用日期
			var useDate = new Ext.form.TextField({
						format : 'Y-m-d',
						fieldLabel : '投用日期',

						name : 'model.useDate',
						anchor : '90%',
						readOnly : true
					})

			// 备注
			var memo = new Ext.form.TextArea({
						fieldLabel : "备注",
						name : 'model.memo',
						anchor : '95%',
						readOnly : true

					})

			// 负责人名称
			var chargerName = new Ext.form.TextField({
						fieldLabel : "负责人",

						name : 'chargerName',

						emptyText : '请选择...',

						readOnly : true,
//						allowBlank : false,

						anchor : '90%'

					})

			// 负责人编码
			var charger = new Ext.form.Hidden({
						name : 'model.charger'

					})
			// 部门名称
			var depName = new Ext.form.TextField({
						fieldLabel : "部门",

						name : 'depName',

						emptyText : '请选择...',

						readOnly : true,
//						allowBlank : false,

						anchor : '90%'

					})

			// 部门编码
			var depCode = new Ext.form.Hidden({
						name : 'model.depCode'

					})

			// 检验周期
			var testCycle = new Ext.form.NumberField({
						name : "model.testCycle",
						fieldLabel : "检验周期",
						maxLength : '4',
						maxLengthText : "检验周期最大只能4位数字",
						anchor : '90%',
						readOnly : true
					})

			// 使用状态

			// 是
			var ifUsedY = new Ext.form.Radio({
						boxLabel : '是',
						inputValue : 'Y',
						name : 'model.ifUsed',
						fieldLabel : '使用状态',
						checked : true,
						disabled : true
					});

			// 否
			var ifUsedN = new Ext.form.Radio({
						boxLabel : '否',
						inputValue : 'N',
						hideLabel : 'true',
						name : 'model.ifUsed',
						disabled : true

					});

			// 仪器仪表主表id
			var regulatorId = new Ext.form.Hidden({
						name : 'model.regulatorId'

					})
			// 监督专业
			var jdzyId = new Ext.form.Hidden({
						name : 'model.jdzyId',
						value : JDZY_ID

					})

			// 弹窗的表单对象
			var blockForm = new Ext.form.FormPanel({
						labelAlign : 'right',
						frame : true,
						labelWidth : 70,
						layout : 'column',

						items : [{

									bodyStyle : "padding:20px 0px 0px 0px",
									border : false,
									layout : 'form',
									columnWidth : 1,
									items : [names]
								}, {
									columnWidth : .5,
									layout : 'form',
									border : false,
									items : [yqyblbId]
								}, {
									columnWidth : .5,
									layout : 'form',
									border : false,
									items : [yqybdjId]
								}, {
									columnWidth : .5,
									layout : 'form',
									border : false,
									items : [yqybjdId]
								}, {
									columnWidth : .5,
									layout : 'form',
									border : false,
									items : [userRange]
								}, {

									border : false,
									layout : 'form',
									columnWidth : 1,
									items : [sizes]
								}, {

									border : false,
									layout : 'form',
									columnWidth : 1,
									items : [factory]
								}, {
									columnWidth : .5,
									layout : 'form',
									border : false,
									items : [outFactoryDate]
								}, {
									columnWidth : .5,
									layout : 'form',
									border : false,
									items : [outFactoryNo]
								}, {
									columnWidth : .5,
									layout : 'form',
									border : false,
									items : [buyDate]
								}, {
									columnWidth : .5,
									layout : 'form',
									border : false,
									items : [useDate]
								}, {
									columnWidth : .5,
									layout : 'form',
									border : false,
									items : [chargerName]
								}, {
									columnWidth : .5,
									layout : 'form',
									border : false,
									items : [depName]
								}, {
									columnWidth : .5,
									layout : 'form',
									border : false,
									items : [testCycle]
								}, {
									columnWidth : .5,
									layout : 'column',
									border : false,
									items : [{
												columnWidth : .5,
												layout : 'form',
												border : false,
												items : [ifUsedY]
											}, {
												columnWidth : .5,
												layout : 'form',
												border : false,
												items : [ifUsedN]
											}]
								}, {

									border : false,
									layout : 'form',
									columnWidth : 1,
									items : [memo, charger, depCode,
											regulatorId, jdzyId]
								}]

					});

			// 左边的弹出窗体

			var blockAddWindow = new Ext.Window({
						// el : 'window_win',
						title : '',
						layout : 'fit',
						width : 550,
						height : 400,
						modal : true,
						closable : true,
						border : false,
						resizable : false,
						closeAction : 'hide',
						plain : true,
						// 面板中按钮的排列方式
						buttonAlign : 'center',
						items : [blockForm]
					});

			// 修改按钮
			var westbtnedit = new Ext.Button({
						id : 'btnUpdate',
						text : '查看',
						iconCls : 'list',
						handler : function() {
							if (westgrid.selModel.hasSelection()) {

								var records = westgrid.getSelectionModel()
										.getSelections();
								var recordslen = records.length;
								if (recordslen > 1) {
									Ext.Msg.alert("系统提示信息", "请选择其中一项进行查看！");
								} else {
									blockAddWindow.show();
									var record = westgrid.getSelectionModel()
											.getSelected();
									blockForm.getForm().reset();
									blockForm.form.loadRecord(record);

									outFactoryDate.setValue(record
											.get("model.outFactoryDate")
											.substring(0, 10));
									buyDate.setValue(record
											.get("model.buyDate").substring(0,
													10));
									useDate.setValue(record
											.get("model.useDate").substring(0,
													10));
									op = 'edit';
									blockAddWindow.setTitle("仪器仪表信息查看");
								}
							} else {
								Ext.Msg.alert("提示", "请先选择要查看的行!");
							}
						}
					});

			var westsm = new Ext.grid.CheckboxSelectionModel();
			// 左边列表中的数据
			var datalist = new Ext.data.Record.create([

			{
						name : 'model.regulatorId'
					}, {
						name : 'model.names'
					}, {
						name : 'model.yqyblbId'
					}, {
						name : 'model.yqybdjId'
					}, {
						name : 'model.yqybjdId'
					}, {
						name : 'model.userRange'
					}, {
						name : 'model.sizes'
					}, {
						name : 'model.testCycle'
					}, {
						name : 'model.factory'
					}, {
						name : 'model.outFactoryDate'
					}, {
						name : 'model.outFactoryNo'
					}, {
						name : 'model.buyDate'
					}, {
						name : 'model.useDate'
					}, {
						name : 'model.depCode'
					}, {
						name : 'model.charger'
					}, {
						name : 'model.ifUsed'
					}, {
						name : 'model.memo'
					}, {
						name : 'model.lastCheckDate'
					}, {
						name : 'model.nextCheckDate'
					}, {
						name : 'model.jdzyId'
					}, {
						name : 'depName'

					}, {
						name : 'chargerName'
					}, {
						name : 'nameOfSort'
					}]);

			var westgrids = new Ext.data.JsonStore({
						url : 'productionrec/findAccountByNames.action',
						root : 'list',
						totalCount : 'totalCount',
						fields : datalist
					});

			var query = new Ext.form.TextField({
						id : 'argFuzzy',
						fieldLabel : "模糊查询",
						hideLabel : false,
						emptyText : '仪器名称..',
						name : 'argFuzzy',
						width : 150,
						value : ''
					});
			function fuzzyQuery() {
				westgrids.baseParams = {
					fuzzy : query.getValue(),
					jdzyId : jdzyId.getValue()
				}
				westgrids.load({
							params : {
								start : 0,
								limit : 18
							}
						});
			};
			// 查询时Enter
			document.onkeydown = function() {
				if (event.keyCode == 13 && document.getElementById('argFuzzy')) {
					fuzzyQuery();

				}
			}
			var querybtn = new Ext.Button({
						iconCls : 'query',
						text : '查询',
						handler : function() {
							fuzzyQuery();
						}
					})
			// 左边列表
			var westgrid = new Ext.grid.GridPanel({
						ds : westgrids,
						columns : [westsm, new Ext.grid.RowNumberer(), {
									header : "仪表名称",

									// align : "center",
									sortable : true,

									dataIndex : 'model.names'
								}, {
									header : "类别",

									sortable : false,

									dataIndex : 'nameOfSort'
								}],
						tbar : [query, querybtn, westbtnedit],
						sm : westsm,
						viewConfig : {
							forceFit : true
						},
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
			westgrid.on('rowdblclick', function() {
						Ext.get("btnUpdate").dom.click();
					})
			fuzzyQuery();
			// 设定布局器及面板
			var layout = new Ext.Viewport({
						layout : "border",
						border : false,
						items : [{
									title : "",
									region : 'center',
									layout : 'fit',
									border : false,
									margins : '1 0 1 1',
									split : true,
									collapsible : true,

									items : [westgrid]

								}]
					});
		});