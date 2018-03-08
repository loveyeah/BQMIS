Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
	/**
	 * 数字格式化
	 */
	function numRenderer(v) {
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
	// 把数量用逗号分隔
	function divide(value) {
		var svalue = value + "";
		var decimal = "";
		// 如果有小数
		if (svalue.indexOf(".") > 0) {
			decimal = svalue.substring(svalue.indexOf("."), svalue.length);
		}
		tempV = svalue.substring(0, svalue.length - decimal.length);
		svalue = "";
		while (Div(tempV, 1000) > 0) {
			var temp = Div(tempV, 1000);
			var oddment = tempV - temp * 1000;
			var soddment = "";
			tempV = Div(tempV, 1000);
			soddment += (0 == Div(oddment, 100)) ? "0" : Div(oddment, 100);
			oddment -= Div(oddment, 100) * 100;
			soddment += (0 == Div(oddment, 10)) ? "0" : Div(oddment, 10);
			oddment -= Div(oddment, 10) * 10;
			soddment += (0 == Div(oddment, 1)) ? "0" : Div(oddment, 1);
			oddment -= Div(oddment, 1) * 1;
			svalue = soddment + "," + svalue;
		}
		svalue = tempV + "," + svalue;
		svalue = svalue.substring(0, svalue.length - 1);
		svalue += decimal;
		return svalue;
	}
	// 整除
	function Div(exp1, exp2) {
		var n1 = Math.round(exp1); //四舍五入
		var n2 = Math.round(exp2); //四舍五入
		var rslt = n1 / n2; //除
		if (rslt >= 0) {
			rslt = Math.floor(rslt); //返回值为小于等于其数值参数的最大整数值。
		} else {
			rslt = Math.ceil(rslt); //返回值为大于等于其数字参数的最小整数。
		}
		return rslt;
	}

	// 物料名称
	var txtMaterialName = new Ext.form.TextField({
		fieldLabel : '物料名称',
		width : 180,
		readOnly : false,
		id : "materialName",
		name : "materialName"
	});
	var txtIssueNo = new Ext.form.TextField({
		ieldLabel : '领料单号',
		width : 180,
		readOnly : false,
		id : "orderNo",
		name : "orderNo"
	});
	// grid工具栏
	var gridTbar = new Ext.Toolbar({
		border : false,
		items : ["领料单号:", txtIssueNo,
				"&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp" + "物料名称:", txtMaterialName,
				'-', {
					text : "查询",
					iconCls : 'query',
					handler : gridFresh
				}, '-', {
					text : "出库修改",
					iconCls : 'update',
					handler : updateQty
				}]
	});
	// 物料异动grid
	var queryStore = new Ext.data.JsonStore({
		url : 'resource/findCancelIssueList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : [
				// 流水号
				{
					name : 'transHisId'
				},
				// 单据号
				{
					name : 'orderNo'
				},
				// 项次号
				{
					name : 'sequenceNo'
				},
				// 事务名称
				{
					name : 'transName'
				},
				// 物料编码
				{
					name : 'materialNo'
				},
				// 物料名称            
				{
					name : 'materialName'
				},
				// 规格型号           
				{
					name : 'specNo'
				},
				// 材质/参数           
				{
					name : 'parameter'
				},
				// 异动数量           
				{
					name : 'transQty'
				},
				// 单位           
				{
					name : 'transUmId'
				},
				// 操作人          
				{
					name : 'operatedBy'
				},
				// 操作时间          
				{
					name : 'operatedDate'
				},
				// 操作仓库            
				{
					name : 'whsName'
				},
				// 操作库位            
				{
					name : 'locationName'
				},
				// 调入仓库            
				{
					name : 'whsNameTwo'
				},
				//调入库位                
				{
					name : 'locationNameTwo'
				},
				// 批号           
				{
					name : 'lotNo'
				},
				// 备注            
				{
					name : 'memo'
				}]
	});
	// 载入数据
	queryStore.load({
		params : {
			start : 0,
			limit : Constants.PAGE_SIZE
		}
	});

	// grid
	var transPanel = new Ext.grid.GridPanel({
		region : "center",
		enableColumnMove : false,
		enableColumnHide : true,
		frame : false,
		border : false,
		store : queryStore,
		// 单选
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
		columns : [new Ext.grid.RowNumberer({
			header : "行号",
			width : 35
		}),
				//流水号
				{
					header : "流水号",
					width : 80,
					sortable : true,
					hidden : true,
					dataIndex : 'transHisId'
				},
				//单据号
				{
					header : "单据号",
					width : 55,
					sortable : true,
					dataIndex : 'orderNo'
				},
				//项次号
				{
					header : "项次号",
					width : 55,
					align : 'right',
					sortable : true,
					hidden : true,
					dataIndex : 'sequenceNo'
				},
				//事务名称
				{
					header : "事务名称",
					width : 80,
					sortable : true,
					dataIndex : 'transName'
				},
				//物料编码        
				{
					header : "物料编码",
					width : 120,
					sortable : true,
					dataIndex : 'materialNo'
				},
				//物料名称
				{
					header : "物料名称",
					width : 100,
					sortable : true,
					dataIndex : 'materialName'
				},
				//规格型号
				{
					header : "规格型号",
					width : 80,
					sortable : true,
					dataIndex : 'specNo'
				},
				//材质/参数
				{
					header : "材质/参数",
					width : 80,
					sortable : true,
					dataIndex : 'parameter'
				},
				//异动数量
				{
					header : "出库数量",
					align : 'right',
					width : 80,
					sortable : true,
					dataIndex : 'transQty',
					renderer : numRenderer
				},
				//  单位
				{
					header : "单位",
					width : 100,
					sortable : true,
					dataIndex : 'transUmId'
				},
				//操作人
				{
					header : "操作人",
					width : 100,
					sortable : true,
					dataIndex : 'operatedBy'
				},
				//操作时间
				{
					header : "操作时间",
					width : 100,
					sortable : true,
					dataIndex : 'operatedDate'
				},
				//操作仓库
				{
					header : "操作仓库",
					width : 150,
					sortable : true,
					dataIndex : 'whsName'
				},
				//操作库位
				{
					header : "操作库位",
					width : 100,
					sortable : true,
					dataIndex : 'locationName'
				},
				//调入仓库
				{
					header : "调入仓库",
					width : 100,
					sortable : true,
					hidden : true,
					dataIndex : 'whsNameTwo'
				},
				//调入库位
				{
					header : "调入库位",
					width : 100,
					sortable : true,
					hidden : true,
					dataIndex : 'locationNameTwo'
				},
				//批号
				{
					header : "批号",
					width : 40,
					sortable : true,
					dataIndex : 'lotNo'
				},
				//备注
				{
					header : "备注",
					width : 200,
					sortable : true,
					dataIndex : 'memo'
				}

		],
		tbar : gridTbar,
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : Constants.PAGE_SIZE,
			store : queryStore,
			displayInfo : true,
			displayMsg : Constants.DISPLAY_MSG,
			emptyMsg : Constants.EMPTY_MSG
		})
	});
	
	transPanel.on("rowdblclick",updateQty);
	var transHisId = new Ext.form.Hidden({
		id : "transHisId",
		name : 'transHisId',
		anchor : "85%"
	});
	var issueNo = new Ext.form.TextField({
		id : "orderNo",
		fieldLabel : '领料单号',
		name : 'orderNo',
		readOnly : true,
		anchor : "85%"
	});
	var materialName=new Ext.form.TextField({
		id : "materialName",
		fieldLabel : '物料名称',
		readOnly : true,
		name : 'materialName',
		anchor : "85%"
	});

	var materialNo=new Ext.form.TextField({
		id : "materialNo",
		fieldLabel : '物料编码',
		name : 'materialNo',
		readOnly : true,
		anchor : "85%"
	});
	
	var specNo=new Ext.form.TextField({
		id : "specNo",
		fieldLabel : '规格型号',
		name : 'specNo',
		readOnly : true,
		anchor : "85%"
	});

	var issueQty = new Ext.form.NumberField({
				id : "transQty",
				fieldLabel : '修改数量',
				name : 'transQty',
				anchor : "85%",
				maxValue : 99999999999.9999,
				minValue : 0,
				decimalPrecision : 4
			});

	var myaddpanel = new Ext.FormPanel({
		title : '修改出库数量',
		height : '100%',
		layout : 'form',
		frame : true,
		labelAlign : 'center',
		items : [transHisId,issueNo,materialNo,materialName,specNo,issueQty]
		
	});
	var win = new Ext.Window({
		width : 400,
		height : 250,
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
				
				var record = transPanel.getSelectionModel().getSelected();
				var actCount=parseInt( record.get("transQty"));
				var num=parseInt(issueQty.getValue());
				
				if(num>actCount)
				{
				  	Ext.Msg.alert("提示","数量不能超过出库数量！")
				  	return ;
				}
				
				var myurl = "resource/updateIssueQty.action";
				
				myaddpanel.getForm().submit({
					method : 'POST',
					url : myurl,
					params : {
						qty : issueQty.getValue(),
						tHisId : transHisId.getValue()
					},
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("提示", o.msg);
						if(o.msg.indexOf("成功")!=-1)
						{
						queryStore.reload();
						win.hide();
						window.returnValue=true;
						}
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

	// 显示区域
	var layout = new Ext.Viewport({
		layout : "border",
		border : false,
		defaults : {
			autoScroll : true
		},
		items : [transPanel]

	});
	//  ↓↓*******************************处理****************************************
	// 查询处理函数
	function gridFresh() {
		
		queryStore.baseParams = {
			materialId : txtMaterialName.getValue(),
			issueNo : txtIssueNo.getValue()
		};
		queryStore.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			}
		});
	}

	function updateQty() {
		if (transPanel.selModel.hasSelection()) {
			var records = transPanel.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				var record = transPanel.getSelectionModel().getSelected();
				win.show();
				myaddpanel.getForm().loadRecord(record);
				myaddpanel.setTitle("修改出库数量");
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}
});