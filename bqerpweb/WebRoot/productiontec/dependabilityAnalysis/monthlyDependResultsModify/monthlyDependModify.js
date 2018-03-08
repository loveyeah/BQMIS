Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	function numberFormat(v) {
		if (v == null || v == "") {
			return "0.0000";
		}
		v = (Math.round((v - 0) * 10000)) / 10000;
		v = (v == Math.floor(v))
				? v + ".0000"
				: ((v * 10 == Math.floor(v * 10))
						? v + "000"
						: ((v * 100 == Math.floor(v * 100)) ? v + "00" : ((v
								* 1000 == Math.floor(v * 1000)) ? v + "0" : v)));
		v = String(v);
		var ps = v.split('.');
		var whole = ps[0];
		var sub = ps[1] ? '.' + ps[1] : '.0000';
		var r = /(\d+)(\d{3})/;
		while (r.test(whole)) {
			whole = whole.replace(r, '$1' + ',' + '$2');
		}
		v = whole + sub;
		if (v.charAt(0) == '-') {
			return '-' + v.substr(1);
		}
		return v;
	}
	
	var storeChargeBySystem = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'workticket/getDetailEquList.action'
						}),
				reader : new Ext.data.JsonReader({
							root : 'list'
						}, [{
									name : 'blockCode',
									mapping : 'blockCode'
								}, {
									name : 'blockName',
									mapping : 'blockName'
								}])
			});
	storeChargeBySystem.load();

	/* 下面的grid */

	var stat_reclist = Ext.data.Record.create([{
				name : 'kkxybId'
			}, {
				name : 'month'
			}, {
				name : 'blockCode'
			}, {
				name : 'fdl'
			}, {
				name : 'uth'
			}, {
				name : 'ph'
			}, {
				name : 'undh'
			}, {
				name : 'eundh'
			}, {
				name : 'sh'
			}, {
				name : 'rh'
			}, {
				name : 'pot'
			}, {
				name : 'poh'
			}, {
				name : 'uot'
			}, {
				name : 'uoh'
			}, {
				name : 'fot'
			}, {
				name : 'foh'
			}, {
				name : 'for1'
			}, {
				name : 'eaf'
			}, {
				name : 'exr'
			}, {
				name : 'pof'
			}, {
				name : 'uof'
			}, {
				name : 'fof'
			}, {
				name : 'af'
			}, {
				name : 'sf'
			}, {
				name : 'udf'
			}, {
				name : 'utf'
			}, {
				name : 'uor'
			}, {
				name : 'foor'
			}, {
				name : 'mttpo'
			}, {
				name : 'mttuo'
			}, {
				name : 'cah'
			}, {
				name : 'mtbf'
			}])

	var stat_Store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'productionrec/findAllBlockMonthly.action'
						}),
				reader : new Ext.data.JsonReader({
							root : 'list'
						}, stat_reclist)
			})

	var stat_cm1 = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
				header : '行号',
				width : 40
			}),{
				header : '月份',
				dataIndex : 'month',
				renderer : function(value){
					if(value != null && value != "")
					{
						return value.toString().substring(0,7);
					}
				}
			}, {
				header : '机组',
				renderer : function(v) {
					var recIndex = storeChargeBySystem.find("blockCode", v);
					return storeChargeBySystem.getAt(recIndex).get("blockName");
				},
				dataIndex : 'blockCode'
			}, {
				header : '发电量',
				align : 'right',
				renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {						
						return  numberFormat(value)
						},
				dataIndex : 'fdl'
			}, {
				header : '利用小时UTH',
				align : 'right',
				css : CSS_GRID_INPUT_COL,
				editor : new Ext.form.NumberField({
					maxValue : 9999999999.9999,
					minValue : 0,
					allowBlank : true,
					allowNegative : false,
					allowDecimals : true,
					decimalPrecision : 4
				}),
				renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {						
						return  numberFormat(value)
						},
				dataIndex : 'uth'
			}, {
				header : '统计期间小时PH',
				align : 'right',
				css : CSS_GRID_INPUT_COL,
				editor : new Ext.form.NumberField({
					maxValue : 9999999999.9999,
					minValue : 0,
					allowBlank : true,
					allowNegative : false,
					allowDecimals : true,
					decimalPrecision : 4
				}),
				renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
						return numberFormat(value)
						},
				dataIndex : 'ph'
			}, {
				header : '降出力小时UNDH',
				align : 'right',
				css : CSS_GRID_INPUT_COL,
				editor : new Ext.form.NumberField({
					maxValue : 9999999999.9999,
					minValue : 0,
					allowBlank : true,
					allowNegative : false,
					allowDecimals : true,
					decimalPrecision : 4
				}),
				renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
						return numberFormat(value)
						},
				dataIndex : 'undh'
			},
				
			{
				header : ' 降出力等效小时EUNDH',
				align : 'right',
				css : CSS_GRID_INPUT_COL,
				editor : new Ext.form.NumberField({
					maxValue : 9999999999.9999,
					minValue : 0,
					allowBlank : true,
					allowNegative : false,
					allowDecimals : true,
					decimalPrecision : 4
				}),
				renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
						return numberFormat(value)
						},
				dataIndex : 'eundh'
			},{
				header : '运行小时SH',
				align : 'right',
				css : CSS_GRID_INPUT_COL,
				editor : new Ext.form.NumberField({
					maxValue : 9999999999.9999,
					minValue : 0,
					allowBlank : true,
					allowNegative : false,
					allowDecimals : true,
					decimalPrecision : 4
				}),
				renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
						return numberFormat(value)
						},
				dataIndex : 'sh'
			}, {
				header : '备用小时RH',
				align : 'right',
				css : CSS_GRID_INPUT_COL,
				editor : new Ext.form.NumberField({
					maxValue : 9999999999.9999,
					minValue : 0,
					allowBlank : true,
					allowNegative : false,
					allowDecimals : true,
					decimalPrecision : 4
				}),
				renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
						return numberFormat(value)
						},
				dataIndex : 'rh'
			}, {
				header : '计划停运次数POT',
				align : 'right',
				css : CSS_GRID_INPUT_COL,
				editor : new Ext.form.NumberField({
					maxValue : 9999999999.9999,
					minValue : 0,
					allowBlank : true,
					allowNegative : false,
					allowDecimals : true,
					decimalPrecision : 4
				}),
				renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
						return numberFormat(value)
						},
				dataIndex : 'pot'
			}, {
				header : '计划停运小时POH',
				align : 'right',
				css : CSS_GRID_INPUT_COL,
				editor : new Ext.form.NumberField({
					maxValue : 9999999999.9999,
					minValue : 0,
					allowBlank : true,
					allowNegative : false,
					allowDecimals : true,
					decimalPrecision : 4
				}),
				renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
						return numberFormat(value)
						},
				dataIndex : 'poh'
			}

	]);
	var stat_cm2 = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(),
				{
				header : '非计划停运次数UOT',
				align : 'right',
				css : CSS_GRID_INPUT_COL,
				editor : new Ext.form.NumberField({
					maxValue : 9999999999.9999,
					minValue : 0,
					allowBlank : true,
					allowNegative : false,
					allowDecimals : true,
					decimalPrecision : 4
				}),
				renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
						return numberFormat(value)
						},
				dataIndex : 'uot'
			},{
				header : '非计划停运小时UOH',
				align : 'right',
				css : CSS_GRID_INPUT_COL,
				editor : new Ext.form.NumberField({
					maxValue : 9999999999.9999,
					minValue : 0,
					allowBlank : true,
					allowNegative : false,
					allowDecimals : true,
					decimalPrecision : 4
				}),
				renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
						return numberFormat(value)
						},
				dataIndex : 'uoh'
			}, {
				header : '强迫停运次数FOT',
				align : 'right',
				css : CSS_GRID_INPUT_COL,
				editor : new Ext.form.NumberField({
					maxValue : 9999999999.9999,
					minValue : 0,
					allowBlank : true,
					allowNegative : false,
					allowDecimals : true,
					decimalPrecision : 4
				}),
				renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
						return numberFormat(value)
						},
				dataIndex : 'fot'
			}, {
				header : '强迫停运小时FOH',
				align : 'right',
				css : CSS_GRID_INPUT_COL,
				editor : new Ext.form.NumberField({
					maxValue : 9999999999.9999,
					minValue : 0,
					allowBlank : true,
					allowNegative : false,
					allowDecimals : true,
					decimalPrecision : 4
				}),
				renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
						return numberFormat(value)
						},
				dataIndex : 'foh'
			}, {
				header : '强迫停运率',
				align : 'right',
				css : CSS_GRID_INPUT_COL,
				editor : new Ext.form.NumberField({
					maxValue : 9999999999.9999,
					minValue : 0,
					allowBlank : true,
					allowNegative : false,
					allowDecimals : true,
					decimalPrecision : 4
				}),
				renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
						return numberFormat(value)
						},
				dataIndex : 'for1'
			}, {
				header : '等效可用系数EAF',
				align : 'right',
				css : CSS_GRID_INPUT_COL,
				editor : new Ext.form.NumberField({
					maxValue : 9999999999.9999,
					minValue : 0,
					allowBlank : true,
					allowNegative : false,
					allowDecimals : true,
					decimalPrecision : 4
				}),
				renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
						return numberFormat(value)
						},
				dataIndex : 'eaf'
			}, {
				header : '暴露率EXR',
				align : 'right',
				css : CSS_GRID_INPUT_COL,
				editor : new Ext.form.NumberField({
					maxValue : 9999999999.9999,
					minValue : 0,
					allowBlank : true,
					allowNegative : false,
					allowDecimals : true,
					decimalPrecision : 4
				}),
				renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
						return numberFormat(value)
						},
				dataIndex : 'exr'
			}, {
				header : '计划停运系数POF',
				align : 'right',
				css : CSS_GRID_INPUT_COL,
				editor : new Ext.form.NumberField({
					maxValue : 9999999999.9999,
					minValue : 0,
					allowBlank : true,
					allowNegative : false,
					allowDecimals : true,
					decimalPrecision : 4
				}),
				renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
						return numberFormat(value)
						},
				dataIndex : 'pof'
			}, {
				header : '非计划停运系数UOF',
				align : 'right',
				css : CSS_GRID_INPUT_COL,
				editor : new Ext.form.NumberField({
					maxValue : 9999999999.9999,
					minValue : 0,
					allowBlank : true,
					allowNegative : false,
					allowDecimals : true,
					decimalPrecision : 4
				}),
				renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
						return numberFormat(value)
						},
				dataIndex : 'uof'
			}, {
				header : '强迫停运系数FOF',
				align : 'right',
				css : CSS_GRID_INPUT_COL,
				editor : new Ext.form.NumberField({
					maxValue : 9999999999.9999,
					minValue : 0,
					allowBlank : true,
					allowNegative : false,
					allowDecimals : true,
					decimalPrecision : 4
				}),
				renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
						return numberFormat(value)
						},
				dataIndex : 'fof'
			}

	]);
	var stat_cm3 = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
				header : '可用系数AF',
				align : 'right',
				css : CSS_GRID_INPUT_COL,
				editor : new Ext.form.NumberField({
					maxValue : 9999999999.9999,
					minValue : 0,
					allowBlank : true,
					allowNegative : false,
					allowDecimals : true,
					decimalPrecision : 4
				}),
				renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
						return numberFormat(value)
						},
				dataIndex : 'af'
			}, {
				header : '运行系数SF',
				align : 'right',
				css : CSS_GRID_INPUT_COL,
				editor : new Ext.form.NumberField({
					maxValue : 9999999999.9999,
					minValue : 0,
					allowBlank : true,
					allowNegative : false,
					allowDecimals : true,
					decimalPrecision : 4
				}),
				renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
						return numberFormat(value)
						},
				dataIndex : 'sf'
			}, {
				header : '降低出力系数UDF',
				align : 'right',
				css : CSS_GRID_INPUT_COL,
				editor : new Ext.form.NumberField({
					maxValue : 9999999999.9999,
					minValue : 0,
					allowBlank : true,
					allowNegative : false,
					allowDecimals : true,
					decimalPrecision : 4
				}),
				renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
						return numberFormat(value)
						},
				dataIndex : 'udf'
			}, {
				header : '利用系数UTF',
				align : 'right',
				css : CSS_GRID_INPUT_COL,
				editor : new Ext.form.NumberField({
					maxValue : 9999999999.9999,
					minValue : 0,
					allowBlank : true,
					allowNegative : false,
					allowDecimals : true,
					decimalPrecision : 4
				}),
				renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
						return numberFormat(value)
						},
				dataIndex : 'utf'
			}, {
				header : '非计划停运率UOR',
				align : 'right',
				css : CSS_GRID_INPUT_COL,
				editor : new Ext.form.NumberField({
					maxValue : 9999999999.9999,
					minValue : 0,
					allowBlank : true,
					allowNegative : false,
					allowDecimals : true,
					decimalPrecision : 4
				}),
				renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
						return numberFormat(value)
						},
				dataIndex : 'uor'
			}, {
				header : '强迫停运发生率FOOR',
				align : 'right',
				css : CSS_GRID_INPUT_COL,
				editor : new Ext.form.NumberField({
					maxValue : 9999999999.9999,
					minValue : 0,
					allowBlank : true,
					allowNegative : false,
					allowDecimals : true,
					decimalPrecision : 4
				}),
				renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
						return numberFormat(value)
						},
				dataIndex : 'foor'
			}, {
				header : '平均计划停运间隔时间MTTPO',
				align : 'right',
				css : CSS_GRID_INPUT_COL,
				editor : new Ext.form.NumberField({
					maxValue : 9999999999.9999,
					minValue : 0,
					allowBlank : true,
					allowNegative : false,
					allowDecimals : true,
					decimalPrecision : 4
				}),
				renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
						return numberFormat(value)
						},
				dataIndex : 'mttpo'
			}, {
				header : '平均非计划停运间隔时间MTTUO',
				align : 'right',
				css : CSS_GRID_INPUT_COL,
				editor : new Ext.form.NumberField({
					maxValue : 9999999999.9999,
					minValue : 0,
					allowBlank : true,
					allowNegative : false,
					allowDecimals : true,
					decimalPrecision : 4
				}),
				renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
						return numberFormat(value)
						},
				dataIndex : 'mttuo'
			}, {
				header : '平均连续可用小时CAH',
				align : 'right',
				css : CSS_GRID_INPUT_COL,
				editor : new Ext.form.NumberField({
					maxValue : 9999999999.9999,
					minValue : 0,
					allowBlank : true,
					allowNegative : false,
					allowDecimals : true,
					decimalPrecision : 4
				}),
				renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
						return numberFormat(value)
						},
				dataIndex : 'cah'
			}, {
				header : '平均无故障可用小时MTBF',
				align : 'right',
				css : CSS_GRID_INPUT_COL,
				editor : new Ext.form.NumberField({
					maxValue : 9999999999.9999,
					minValue : 0,
					allowBlank : true,
					allowNegative : false,
					allowDecimals : true,
					decimalPrecision : 4
				}),
				renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
						return numberFormat(value)
						},
				dataIndex : 'mtbf'
			}

	]);
			
	var queryField = new Ext.form.TextField({
				id : 'argFuzzy',
				fieldLabel : "月份",
				name : 'argFuzzy',
				width : 100,
				value : getDate(),
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '',
									alwaysUseStartDate : true,
									dateFmt : "yyyy-MM"

								});
						this.blur();
					}

				}
			});
	var queryBtn = new Ext.Button({
				text : '查询',
				width : 80,
				iconCls : 'query',
				handler : fuzzyQuery
			})
	var modifyBtn = new Ext.Button({
				text : '保存',
				width : 70,
				iconCls : 'save',
				handler : modify
			})

	var stat_Grid1 = new Ext.grid.EditorGridPanel({
			tbar : [{text : '月份:'},queryField,queryBtn,modifyBtn],
				ds : stat_Store,
				height : 300,
				title : "机组主要指标表",
				viewConfig : {
			                 forceFit : true
		           },
				cm : stat_cm1
			})
	var stat_Grid2 = new Ext.grid.EditorGridPanel({

				height : 300,
				ds : stat_Store,
				title : "",
				viewConfig : {
			                 forceFit : true
		           },
				cm : stat_cm2
			})
	var stat_Grid3 = new Ext.grid.EditorGridPanel({

				height : 300,
				ds : stat_Store,
				title : "",
				viewConfig : {
			                 forceFit : true
		           },
				cm : stat_cm3
			})

	/* 以上为下面的grid */

	/* 查询控件 */

	// 系统当前时间
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;

		return s;
	}
	
	function fuzzyQuery() {
		stat_Store.baseParams = {
			month : queryField.getValue()
		}
		stat_Store.load({
			params : {
						start : 0,
						limit : 18
					}
		})

	};

	function modify() {
		if(stat_Store.getCount() == 0)
		{
			Ext.Msg.alert("提示信息","没有数据进行保存！")
			return ;
		}
		stat_Grid1.stopEditing();
		stat_Grid2.stopEditing();
		stat_Grid3.stopEditing();
		var modifiedRec = stat_Store.getModifiedRecords();
		if(modifiedRec.length > 0){
			Ext.Msg.confirm("提示","确认保存表格中的数据？",function(button,text){
				if(button == 'yes')
				{
					Ext.Msg.wait('正在保存数据，请等待...');
					var modifyRecords = new Array();
					for(var i = 0;i <= modifiedRec.length - 1; i++)
					{
						modifyRecords.push(modifiedRec[i].data);
					}
					Ext.Ajax.request({
						url : 'productionrec/monthlyResultsModify.action',
						mothod : 'post',
						params : {
							modifyRecords : Ext.util.JSON.encode(modifyRecords)
						},
						success : function(result,request){
							Ext.Msg.alert('提示信息','数据保存成功！');
							stat_Store.rejectChanges();
							stat_Store.reload();
						},
						failure : function(result,request){
							Ext.Msg.alert('提示信息','数据操纵失败！')
							stat_Store.rejectChanges();
							stat_Store.reload();
						}
					})
				}
			})
	}
	else 
	{
		Ext.Msg.alert('提示信息','您没有做任何修改！');
	}
	}

	var panel = new Ext.Panel({
		width : Ext.getBody().getViewSize().width,
		autoHeight : true,
		autoScroll : true,
		border : false,
		split : true,
		items : [
		stat_Grid1, 
		stat_Grid2, stat_Grid3
		]

	})
	fuzzyQuery()
	panel.render("treepanel");

});