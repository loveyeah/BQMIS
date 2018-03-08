Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
//----------------add-----------------------
Ext.override(Ext.grid.GridView, {
		doRender : function(cs, rs, ds, startRow, colCount, stripe) {
			var ts = this.templates, ct = ts.cell, rt = ts.row, last = colCount
					- 1;
			var tstyle = 'width:' + this.getTotalWidth() + ';';
			// buffers
			var buf = [], cb, c, p = {}, rp = {
				tstyle : tstyle
			}, r;
			for (var j = 0, len = rs.length; j < len; j++) {
				r = rs[j];
				cb = [];
				var rowIndex = (j + startRow);
				for (var i = 0; i < colCount; i++) {
					c = cs[i];
					p.id = c.id;
					p.css = i == 0 ? 'x-grid3-cell-first ' : (i == last
							? 'x-grid3-cell-last '
							: '');
					p.attr = p.cellAttr = "";
					p.value = c.renderer(r.data[c.name], p, r, rowIndex, i, ds);
					// 判断是否是统计行
					if (r.data["issueDetailsId"] ==null||r.data["issueDetailsId"]=="undefined") {
						// 替换掉其中的背景颜色
						p.style = c.style
								.replace(/background\s*:\s*[^;]*;/, '');
					} else {
						// 引用原样式
						p.style = c.style;
					};
					if (p.value == undefined || p.value === "")
						p.value = "&#160;";
					if (r.dirty && typeof r.modified[c.name] !== 'undefined') {
						p.css += ' x-grid3-dirty-cell';
					}
					cb[cb.length] = ct.apply(p);
				}
				var alt = [];
				if (stripe && ((rowIndex + 1) % 2 == 0)) {
					alt[0] = "x-grid3-row-alt";
				}
				if (r.dirty) {
					alt[1] = " x-grid3-dirty-row";
				}
				rp.cols = colCount;
				if (this.getRowClass) {
					alt[2] = this.getRowClass(r, rowIndex, rp, ds);
				}
				rp.alt = alt.join(" ");
				rp.cells = cb.join("");
				buf[buf.length] = rt.apply(rp);
			}
			return buf.join("");
		}
	});
//------------------------------------------
// 获取时间
function getSetDate() {
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

Ext.form.Label.prototype.setText = function(argText) {
	this.el.dom.innerHTML = argText;
}
Ext.form.Radio.prototype.url = "";
var nrs = "";
function removeAppointNextRoles() {
	if (confirm("确定要清除指定下一步人吗？")) {
		nrs = "";
		Ext.get("showAppointNextRoles").dom.innerHTML = "";
	}
}

var entryId = getParameter("entryId");
var issueHeadId = getParameter("issueHeadId");
var orginalId = getParameter("orginalId");
var eventIdentify = "";
var jsonArgs = "";
var deptType = "";
var mrNo = getParameter("mrNo");//add by sychen 20100511
var receiptDep = getParameter("receiptDep");//add by sychen 20100511
var flag = getParameter("flag");//add by sychen 20100511


Ext.onReady(function() {
	var workerCode = '';
//	function initReceiptBy() {
//		// 画面初始值设置
//		Ext.lib.Ajax.request('POST', 'resource/getInitUserCode.action', {
//			success : function(action) {
//				var result = eval("(" + action.responseText + ")");
//				if (result) {
//					workerCode = result.workerCode;
//					
//				}
//
//			}
//		});
//	}
	var actionId = "";
	// 设置响应时间
	var timeSet = new Ext.form.Checkbox({
		name : 'reponseTime',
		boxLabel : "设置响应时间",
		listeners : {
			check : function(box, checked) {
				if (checked) {
					// 如果checkbox选中,显示时间选择textfield
					Ext.getCmp('datePicker').setVisible(true);
					Ext.getCmp('approveField').doLayout();
				} else {
					Ext.getCmp('datePicker').setVisible(false);
					Ext.getCmp('approveField').doLayout();
				}
			}
		}
	});

	// 时间选择
	var timeDisplay = new Ext.form.TextField({
		id : 'datePicker',
		hidden : true,
		style : 'cursor:pointer',
		value : getSetDate(),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-01 00:00:00',
					dateFmt : 'yyyy-MM-dd HH:mm:ss',
					alwaysUseStartDate : true
				});
			}
		}
	});

	// 图形展示
	var btnPictureDisplay = new Ext.Button({
		text : "图形展示",
		handler : function() {
			var url =application_base_path + "workflow/manager/show/show.jsp?entryId="
					+ entryId;
			window.open(url);
		}
	});

	// 查看审批记录
	var btnRecord = new Ext.Button({
		text : "查看审批记录",
		handler : function() {
			var url =application_base_path + "workflow/manager/approveInfo/approveInfo.jsp?entryId="
					+ entryId;
			window
					.showModalDialog(
							url,
							null,
							"dialogWidth:650px;dialogHeight:400px;;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;");
		}
	});
	// 显示下步角色
	var btnNextDisplay = new Ext.Button({
		text : "显示下步角色",
		handler : getNextSteps
	});

	// 标题label
	var lblApprove = new Ext.form.Label({
		width : '100%',
		id : 'lblApprove',
		text : '相关领导审批',
		height : 10,
		style : "color:blue;line-height:100px;padding-left:175px;font-size:30px"
	});

	// 显示指定下一步角色
	var showAppointNextRoles = new Ext.form.Label({
		border : false,
		id : 'showAppointNextRoles',
		style : "color:red;font-size:12px"
	});

	// 审批方式label
	var lblApproveStyle = new Ext.form.Label({
		text : " 审批方式：",
		style : 'font-size:12px',
		border : false
	});

	// 单选按钮组
	var radioSet = new Ext.Panel({
		height : 30,
		border : false,
		layout : "column"
	});

	var stylePanel = new Ext.Panel({
		height : 65,
		border : false,
		style : "padding-top:20;border-width:1px 0 0 0;",
		items : [lblApproveStyle, radioSet]
	});
	function gettype() {
		//modify by fyyang 090618
//		if (orginalId == 4 || orginalId == 10)// 新厂生产类和新厂固定资产
//		{
//			jsonArgs = "{'isNew':true}";
//			getCurrentSteps();
//		} else if (orginalId == 5 || orginalId == 11||orginalId==3)// 老厂生产和老厂固定资产
//		{
//			jsonArgs = "{'isNew':false}";
//			getCurrentSteps();
//		} else
		if (orginalId == 5)// 行政
		{
			Ext.lib.Ajax.request('POST', 'resource/getFeeDeptType.action', {
				success : function(action) {
					var deptType = action.responseText;
					if (deptType == 'FD') {
						jsonArgs = "{'isFD':true,'isJX':false,'isSY':false}";
					} else if (deptType == 'JX') {
						jsonArgs = "{'isFD':false,'isJX':true,'isSY':false}";
					} else {
						jsonArgs = "{'isFD':false,'isJX':false,'isSY':true}";
					}
					getCurrentSteps();
				}
			}, 'issueHeadId=' + issueHeadId);
		}
		else
		{
		getCurrentSteps();	
		}
		// jsonArgs = "{'isNew':true}";
		// Ext.lib.Ajax.request('POST', 'resource/getIssueOrginalId.action', {
		// success : function(action) {
		// var orginalId = action.responseText;
		// else {
		// jsonArgs = "{'isNew':true}";
		// getCurrentSteps();
		// }
		// }
		// }, 'issueHeadId=' + issueHeadId);
	}
	// 获取审批方式
	function getCurrentSteps() {
		Ext.Ajax.request({
			url : "MAINTWorkflow.do?action=getCurrentSteps",
			method : 'post',
			params : {
				entryId : entryId,
				workerCode : workerCode,
				jsonArgs : jsonArgs
			},
			success : function(result, request) {
				var radio = eval('(' + result.responseText + ')');
				radioAddHandler(radio[0].actions);
				lblApprove.setText(radio[0].stepName);
			},
			failure : function() {

			}
		});

	};

	function getNextSteps() {
		var args = new Object();
		args.actionId = actionId;
		args.entryId = entryId;
		var url =application_base_path + "workflow/manager/appointNextRole/appointNextRole.jsp";
		var ro = window
				.showModalDialog(
						url,
						args,
						"dialogWidth:400px;dialogHeight:350px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;");
		if (typeof(ro) != "undefined") {
			if (ro) {
				nrs = ro.nrs;
				showAppointNextRoles
						.setText("<span style=\"cursor:hand;color:blue\"  onclick=\"removeAppointNextRoles();\">[取消指定]</span>  您指定下一步角色为：[ "
								+ ro.nrsname + " ]");
			}
		}
	};

	/**
	 * 审批方式radio button生成
	 */

	function radioAddHandler(radio) {

		// radio = radio ||
		// [{actionId:1,actionName:'上报'},{actionId:2,actionName:'紧急上报'}];
		if (radio instanceof Array) {
			if (!radioSet.items) {
				radioSet.items = new Ext.util.MixedCollection();
			}
			if (radio.length > 0) {
				var _radio = new Ext.form.Radio({
					boxLabel : "<font size='2.6px'>" + radio[0].actionName
							+ "</font>",
					id : 'approve-radio' + radio[0].actionId,
					name : 'rb-approve',
					inputValue : radio[0].actionId,
					url : radio[0].url,
					eventIdentify : radio[0].changeBusiStateTo,
					listeners : {
						'check' : function() {
							var _actionId = Ext.ComponentMgr
									.get('approve-radio' + radio[0].actionId)
									.getGroupValue();
							var obj = Ext.getCmp('approve-radio' + _actionId);
							actionId = _actionId;
							eventIdentify = obj.eventIdentify;
							//remark.setValue("同意!");
						}
					},
					checked : true
				});
				radioSet.items.add(_radio);
				// 添加隐藏域，为了使radio正常显示
				radioSet.items.add(new Ext.form.Hidden());

				for (var i = 1; i < radio.length; i++) {
					// 添加隐藏域，为了使radio正常显示
					radioSet.items.add(new Ext.form.Hidden());
					var _radio = new Ext.form.Radio({
						boxLabel : "<font size='2.6px'>" + radio[i].actionName
								+ "</font>",
						id : 'approve-radio' + radio[i].actionId,
						name : 'rb-approve',
						url : radio[i].url,
						eventIdentify : radio[i].changeBusiStateTo,
						inputValue : radio[i].actionId,
						// addBy liuyingwen 2009-7-21
						listeners: {check: function(radio,checked) {
				                if (checked) {
						           // remark.setValue("退回!");
					            } 
			                }
			            }
					});

					radioSet.items.add(_radio);
					// 添加隐藏域，为了使radio正常显示
					radioSet.items.add(new Ext.form.Hidden());
				}
			}

		}
		radioSet.doLayout();
	}
	
	 //----------费用来源-------add by fyyang 20100325------------------------
    	function choseItem() {
  // add by sychen 20100511
        var args = new Object();
        args.mrDept=receiptDep;
        args.flag=flag;
    // add end --------------//
		var item = window
				.showModalDialog(
						'../../../../comm/jsp/item/budget/budget.jsp',
//						null,//update by sychen 20100511
						args,
						'dialogWidth:560px;dialogHeight:440px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(item) != "undefined") {
			txtMrItem.setValue(item.itemName);
			txtMrItemH.setValue(item.itemCode);
		}
	}

	// 费用来源
	var txtMrItem = new Ext.form.TriggerField({
		fieldLabel : '费用来源<font color ="red">*</font>',
		displayField : 'text',
		valueField : 'id',
		blankText : '请选择',
		emptyText : '请选择',
		anchor : '50%',
		name : "itemName",
		value : getParameter("itemName"),
		allowBlank : false,
		readOnly : true,
		hidden : false,
		height : 40,
		hideLabel : false
	});
	
	txtMrItem.onTriggerClick = choseItem;
	//add by sychen 2010------//
	if(mrNo=="null")
	 txtMrItem.hideTrigger=true;
	else
	 txtMrItem.hideTrigger=false;
	//add end --------------------//
	var txtMrItemH = new Ext.form.Hidden({
		name : 'mr.itemCode',
		value : getParameter("itemCode")
	})
    //--------------------------------------------------

	// 备注
	var remark = new Ext.form.TextArea({
		id : "remark",
		width : '99%',
		autoScroll : true,
		border : false,
		height : 110,
		allowBlank:false
		//value : '同意！'
	})
	// 备注label
	var lblRemarks = new Ext.form.Label({
		text : " 审批意见：",
		style : 'font-size:12px'
	});
	var remarkSet = new Ext.Panel({
		//autoHeight : true,
		height : 120,
		border : false,
	  border : false,
        style : "padding-top:10;border-width:1px 0 0 0;",
		items : [
		     {
					columnWidth : 0.5,
					layout : "form",
					border : false,
				
					items : [txtMrItem]
				},
		lblRemarks, remark]
	});

	var approveField = new Ext.Panel({
		id : "approveField",
		autoWidth : true,
		height : 70,
		style : "padding-top:20;padding-bottom:20;border-width:1px 0 0 0;",
		border : false,
		layout : "column",
		anchor : '100%',
		items : [
				// 设置响应时间checkbox
				{
					columnWidth : 0.185,
					id : "tiemcheck",
					layout : "form",
					hideLabels : true,
					border : false,
					items : [timeSet]
				},
				// 时间选择
				{
					columnWidth : 0.265,
					hideLabels : true,
					layout : "form",
					border : false,
					items : [timeDisplay]
				}, {
					columnWidth : 0.02,
					border : false
				},
				// 图形显示按钮
				{
					columnWidth : 0.145,
					layout : "form",
					hideLabels : true,
					border : false,
					items : [btnPictureDisplay]
				},
				// 查看审批记录按钮
				{
					columnWidth : 0.19,
					layout : "form",
					hideLabels : true,
					border : false,
					items : [btnRecord]
				},
				// 下一步按钮
				{
					columnWidth : 0.19,
					layout : "form",
					hideLabels : true,
					border : false,
					items : [btnNextDisplay]
				}]

	});
	// ↓↓****************员工验证窗口****************
	// 工号
	var workCode = new Ext.form.TextField({
		id : "workCode",
		//value : '999999',
		fieldLabel : '工号<font color ="red">*</font>',
		allowBlank : false,
		width : 120
	});

	// 密码
	var workPwd = new Ext.form.TextField({
		id : "workPwd",
		fieldLabel : '密码<font color ="red">*</font>',
		allowBlank : false,
		inputType : "password",
		width : 120
	});
	// 弹出窗口panel
	var workerPanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'right',
		height : 120,
		items : [workCode, workPwd]
	});

	// 弹出窗口
	var validateWin = new Ext.Window({
		width : 300,
		height : 140,
		modal : true,
		title : "请输入工号和密码",
		buttonAlign : "center",
		resizable : false,
		items : [workerPanel],
		buttons : [{
			text : '确定',
			handler : function() {
				// 工号确认
				Ext.lib.Ajax.request('POST',
						'comm/workticketApproveCheckUser.action', {
							success : function(action) {
								var result = eval(action.responseText);
								// 如果验证成功，进行签字操作
								if (result) {
									var o = new Object();
									o.actionId = actionId;
									o.opinion = Ext.get("remark").dom.value;
									o.workerCode = workCode.getValue();
									o.nrs = nrs;
									o.eventIdentify = eventIdentify;
									o.itemCode=txtMrItemH.getValue();
									validateWin.hide();
									window.returnValue = o;
									window.close();
								} else {
									Ext.Msg.alert("提示", "密码错误");
								}
							}
						}, "workerCode=" + workCode.getValue() + "&loginPwd="
								+ workPwd.getValue());
			}
		}, {
			text : '取消',
			handler : function() {
				validateWin.hide();
			}
		}],
        listeners : {
            show : function(com) {
                // 取得默认工号
               workCode.setValue(workerCode);
            }
        },
		closeAction : 'hide'
	});
	// ↑↑****************员工验证窗口****************

	// 审批签字
	var approvePanel = new Ext.form.FormPanel({
		title : '审批签字',
		border : false,
		labelAlign : 'left',
		layout : "column",

		items : [{
			columnWidth : 0.1,
			border : false
		}, {
			columnWidth : 0.8,
			border : true,
			layout : 'form',
			buttonAlign : 'center',
			items : [lblApprove, approveField, showAppointNextRoles,
					stylePanel, remarkSet],
			buttons : [{
				text : '确定',
				handler : function() {
					if(remark.getValue()=="")
				      {
                           Ext.Msg.alert("提示","请填写审批意见！");
                           return;
				      }
					//modify by fyyang 091028
					var record = detailGrid.getStore().getModifiedRecords();
					//alert(record.length);
					if (record.length > 1) {
						Ext.Msg.confirm("提示", "物料详细信息已修改尚未保存，是否不进行保存继续签字审批？",
								function(buttonobj) {
									if (buttonobj == "yes") {
										validateWin.show();
									}
								});
					} else {
						validateWin.show();
					}
					
				}
			}, {
				text : '取消',
				handler : function() {
					//viewport.remove(tabpanel);
					window.close();
				}
			}]
		}, {
			columnWidth : 0.1,
			border : false

		}]
	});

	// tabPanel定义
	// var tabpanel = new Ext.TabPanel({
	// activeTab : 0,
	// layoutOnTabChange : true,
	// items : [approvePanel]
	// });
	var detailRecord = Ext.data.Record.create([
			// 领料单明细id
			{
				name : 'issueDetailsId'
			},
			// 上次修改日时
			{
				name : "lastModifiedDate"
			},
			// 物料id
			{
				name : "materialId"
			},
			// 物料编码，
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
			// 存货计量单位id
			{
				name : 'stockUmId'
			},
			// 存货计量单位名称
			{
				name : "unitName"
			},
			// 申请数量
			{
				name : 'appliedCount'
			},
			// 核准数量
			{
				name : 'approvedCount'
			},
			// 物资需求明细id
			{
				name : "requirementDetailId"
			},
			// 实际数量
			{
				name : 'actIssuedCount'
			},
			// 待发货数量
			{
				name : 'waitCount'
			},
			// 成本分摊项目编码
			{
				name : 'costItemId'
			},
			// 费用来源id
			{
				name : "itemId"
			},
			// 费用来源名称
			{
				name : 'itemName'
			},
			// 是否是新规的记录
			{
				name : "isNew"
			},
			// 是否是删除的记录
			{
				name : "isDeleted"
			},
			// 库存数量
			{
				name : "stock" // add by ywliu 20091019
			}]);
	var detailStore = new Ext.data.JsonStore({
		url : 'resource/getIssueHeadRegisterDetailList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : detailRecord
	});
	detailStore.on({
		'beforeload' : function() {
			Ext.apply(this.baseParams, {
				issueHeadId : issueHeadId,
				flag : "1",
				requimentHeadId : ""
			});
		}
	});
	detailStore.load({
		params : {
			start : 0,
			limit : Constants.PAGE_SIZE
		}
	});
	//---add by fyyang 090723---加合计行------
	detailStore.on("load",function(){
	
		var totalappliedCount = 0; // 申请数量
		var totalapprovedCount = 0; // 核准数量
		var totalactIssuedCount = 0; // 已发货数量
		var totalwaitCount=0;//待发货数量
				
				for (var j = 0; j < detailStore.getCount(); j++) {
					var temp = detailStore.getAt(j);
					if (temp.get("appliedCount") != null) {
						totalappliedCount = parseFloat(totalappliedCount)
								+ parseFloat(temp.get("appliedCount"));
					}
					if (temp.get("approvedCount") != null) {
						totalapprovedCount = parseFloat(totalapprovedCount)
								+ parseFloat(temp.get("approvedCount"));
					}
					if (temp.get("actIssuedCount") != null) {
						totalactIssuedCount = parseFloat(totalactIssuedCount)
								+ parseFloat(temp.get("actIssuedCount"));
					}
					if (temp.get("waitCount") != null) {
						totalwaitCount = parseFloat(totalwaitCount)
								+ parseFloat(temp.get("waitCount"));
					}
				}
		var mydata=new detailRecord({
		appliedCount :totalappliedCount,
		approvedCount: totalapprovedCount,
		actIssuedCount :totalactIssuedCount,
		waitCount:totalwaitCount
		});
		if(detailStore.getCount()>0)
		{
		 	detailStore.add(mydata);
		}
		
	})
	
	//--------------------------------------
	function getDetailRecords() {
		var results = [];
		for (var i = 0; i < detailStore.getCount()-1; i++) {
			var record = detailStore.getAt(i);
			results.push(record.data);
		}
		return results;
	}
	var detailGrid = new Ext.grid.EditorGridPanel({
		height : 220,
		region : "center",
		style : "padding-top:0px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		border : false,
		clicksToEdit : 1,
		autoScroll : true,
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
		store : detailStore,
			//add------------------
				listeners : {
				'beforeedit' : function(e) { 
					if (e.field == "approvedCount")
						{ var column = e.record.get('issueDetailsId'); 
						  if(column==null||column=="undefined")
						  {
						  	return false;
						  }
						}
				},
				'afteredit':function(e)
				{
					if (e.field == "approvedCount")
						{ var column = e.record.get('issueDetailsId'); 
							  if(column!=null&&column!="undefined")
							  {
							  	var totalapprovedCount=0;
							  		for (var j = 0; j < detailStore.getCount()-1; j++) {
											var temp = detailStore.getAt(j);
											if (temp.get("approvedCount") != null) {
												totalapprovedCount = parseFloat(totalapprovedCount)
														+ parseFloat(temp.get("approvedCount"));
											}
					              }
					              detailStore.getAt(detailStore.getCount() - 1).set('approvedCount',
												totalapprovedCount);
							  }
						}
				}
					
				},
				//addend--------------------
		columns : [new Ext.grid.RowNumberer({
			header : "行号",
			width : 35
		}),
				// 到货单号
				{
					header : "项次号",
					sortable : true,
					width : 75,
					dataIndex : 'issueDetailsId'
				},
				// 供应商
				{
					header : "物料编码",
					sortable : true,
					width : 75,
					dataIndex : 'materialNo'
				},
				// 物料名称
				{
					header : "物料名称",
					sortable : true,
					width : 75,
					dataIndex : 'materialName'
				},
				// 规格型号
				{
					header : "规格型号",
					sortable : true,
					width : 75,
					dataIndex : 'specNo'
				},
				// 材质/参数
				{
					header : "材质/参数",
					sortable : true,
					width : 75,
					dataIndex : 'parameter'
				},
				// 单位
				{
					header : "单位",
					sortable : true,
					width : 75,
					dataIndex : 'unitName'
				},
				// 申请数量
				{
					header : "申请数量",
					sortable : true,
					width : 75,
					align : "right",
					renderer : numRenderer,
					dataIndex : 'appliedCount'
				},
				// 核准数量
				{
					header : "核准数量<font color='red'>*</font>",
					sortable : true,
					align : "right",
					renderer : numRenderer,
					width : 75,
					css : CSS_GRID_INPUT_COL,
					editor : new Ext.form.NumberField({
								maxValue : 999999999999999.99,
								minValue : 0,
								decimalPrecision : 4
							}),
					dataIndex : 'approvedCount'
				},
				// 已发货数量
				{
					header : "已发货数量",
					sortable : true,
					align : "right",
					renderer : numRenderer,
					width : 75,
					dataIndex : 'actIssuedCount'
				},
				// 待发货数量
				{
					header : "待发货数量",
					sortable : true,
					align : "right",
					renderer : numRenderer,
					width : 75,
					dataIndex : 'waitCount'
				},
				// 成本分摊项目
				{
					header : "成本分摊项目",
					sortable : true,
					width : 100,
					hidden : true,
					renderer : function(val) {
						if (val == "1") {
							return "成本分摊项目1";
						} else if (val == "2") {
							return "成本分摊项目2";
						} else if (val == "3") {
							return "成本分摊项目3";
						} else if (val == "4") {
							return "成本分摊项目4";
						} else {
							return "";
						}
					},
					dataIndex : 'costItemId'
				},
				// TODO 费用来源
				{
					header : "费用来源",
					sortable : true,
					width : 75,
					renderer : function(val) {
						if (val == "zzfy") {
							return "生产成本";
						} else if (val == "lwcb") {
							return "劳务成本";
						}else {
							return "";
						}
					},
					dataIndex : 'itemId'
				},
				// TODO 当前库存
				{
					header : "当前库存",
					sortable : true,
					width : 75,
					renderer : function(v){
						if(v != null)
							return numRenderer(v);
					},
					dataIndex : 'stock'// add by ywliu 20091019
				}],

		enableColumnMove : false,
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : Constants.PAGE_SIZE,
			store : detailStore,
			displayInfo : true,
			displayMsg : Constants.DISPLAY_MSG,
			emptyMsg : Constants.EMPTY_MSG
		}),
		tbar : new Ext.Toolbar({
			region : "north",
			height : 25,
			items : ['请核实数量', '-', {
				text : "保存",
				iconCls : 'save',
				handler : function() {
					Ext.Ajax.request({
						url : 'resource/saveDetailIssue.action',
						method : 'post',
						params : {
							updateDetails : Ext.util.JSON
									.encode(getDetailRecords())
						},
						success : function(result, request) {
							detailStore.reload();
							Ext.Msg.alert('提示信息','操作成功！');
						},
						failure : function(restlt, request) {
						}
					})

				}
			}, "-", {
				text : "取消",
				iconCls : 'cancer',
				handler : function() {
					detailStore.reload();
				}
			}]
		})
	});
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
//		if (value === "" || value == null) {
//			return value
//		}
//		value = String(value);
//		// 整数部分
//		var whole = value;
//		// 小数部分
//		var sub = ".00";
//		// 如果有小数
//		if (value.indexOf(".") > 0) {
//			whole = value.substring(0, value.indexOf("."));
//			sub = value.substring(value.indexOf("."), value.length);
//			sub = sub + "00";
//			if (sub.length > 3) {
//				sub = sub.substring(0, 3);
//			}
//		}
//		var r = /(\d+)(\d{3})/;
//		while (r.test(whole)) {
//			whole = whole.replace(r, '$1' + ',' + '$2');
//		}
//		v = whole + sub;
//		return v;
	}
	var detail_panel = new Ext.Panel({
		region : "center",
		layout : 'fit',
		title : '详细信息',
		items : [detailGrid]
	})
	var tabPanel = new Ext.TabPanel({
		activeTab : 0,
		layoutOnTabChange : true,
		border : false,
		items : [approvePanel, detail_panel]
	})
	var viewport = new Ext.Viewport({
		layout : "fit",
		items : [approvePanel]// add by ywliu 20091111
	});
	gettype();
	
	 function getWorkCode()
    {
     Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
                    success : function(action) {
                    	 var result = eval("(" + action.responseText + ")");
                        if (result.workerCode) {
                            // 设定默认工号
                        	workerCode=result.workerCode;
                          
                        }
                    }
                });
    }
    getWorkCode();
});