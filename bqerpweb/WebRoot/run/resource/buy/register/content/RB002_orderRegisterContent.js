// 采购单登记Constants
Constants.RB002_E_001 = "存在其他用户的记录，不能操作。请确认。"
Constants.RB002_E_003 = "当前采购订单的采购人员为不是当前用户，不能执行删除操作。请确认。"
Constants.RB002_E_004 = "当前采购订单已经发送了，不能操作。请确认。"
Constants.RB002_E_005 = "请在汇率维护画面增加当前汇率。"
Constants.RB002_I_001 = "保存成功，采购单号：{0}。"
Constants.RB002_I_002 = "当前有了新的汇率。"
Constants.RB002_I_003 = "当前没有最新的汇率。"

Ext.QuickTips.init();
//--------add by fyyang 091109
/**
	 * 金钱格式化
	 */

		function moneyFormat(v) {
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
					if (r.data["orderDetailsDueDate"] ==null||r.data["orderDetailsDueDate"]=="undefined") {
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
Ext.onReady(function(){
	// add by liuyi 091109 标记统计行
//	var flagCount = false;
    // 页面跳转时使用
    var register = parent.Ext.getCmp('tabPanel').register;
     if(parent.first)
	{
		var tabPanel=parent.Ext.getCmp('tabPanel');
		tabPanel.setActiveTab('tabTable');
		tabPanel.setActiveTab('tabContent');
		parent.first=false;
	}
    // 加载采购单记录的监听器
    register.editOrderHandler = initPage;
    
	// 分页时每页显示记录条数
	var PAGE_SIZE = Constants.PAGE_SIZE;
    
	// =======       对象定义开始       ==============
	
// ↓↓********************grid插件，用来显示一行checkbox********************↓↓ //
	Ext.grid.CheckColumn = function(config) {
	    Ext.apply(this, config);
	    if (!this.id) {
	        this.id = Ext.id();
	    }
	    this.renderer = this.renderer.createDelegate(this);
	};
	
	Ext.grid.CheckColumn.prototype = {
	    init : function(grid) {
	        this.grid = grid;
	        this.grid.on('render', function() {
		            var view = this.grid.getView();
		            view.mainBody.on('mousedown', this.onMouseDown, this);
		        }, this);
	    },
	
	    onMouseDown : function(e, t) {
	    	if (!orderRegister.checkCurrentUser()) {
	    		e.stopEvent();
	    		return false;
	    	}
	        if (t.className && t.className.indexOf('x-grid3-cc-' + this.id) != -1) {
	            e.stopEvent();
	            var index = this.grid.getView().findRowIndex(t);
	            var record = this.grid.store.getAt(index);
	            if (record.data[this.dataIndex] == Constants.CHECKED_VALUE) {
	                record.set(this.dataIndex, Constants.UNCHECKED_VALUE);
	            } else {
	                record.set(this.dataIndex, Constants.CHECKED_VALUE);
	            }
	        }
	    },
	
	    renderer : function(v, p, record) {
	        p.css += ' x-grid3-check-col-td';
	        if (!orderRegister.checkCurrentUser()) {
	    		p.css += ' x-item-disabled';
	    	}
	        return '<div class="x-grid3-check-col' + (v == 'Y' ? '-on' : '')
	                + ' x-grid3-cc-' + this.id + '">&#160;</div>';
	    }
	}
	
    // 复制对象
    Array.prototype.clone = function() {
    	var newArr = new Array();
    	for (var i = 0; i < this.length; i++) {
    		if (this[i] instanceof Array) {
	    		newArr.push(this[i].clone());
    		} else {
    			var obj = {};
    			Ext.apply(obj, this[i]);
	    		newArr.push(obj);
    		}
    	}
    	return newArr;
    }
    
    // 加载对象
    Object.prototype.loadData = function(argData, argJustNeed) {
    	for (var prop in this) {
    		// 如果是数组或是函数
    		if (typeof this[prop] == 'function') {
				continue;
			}
			if (argJustNeed && !isNull(this[prop])) {
				continue;
			}
			if (argData.hasOwnProperty(prop)) {
				if (argData[prop] instanceof Array) {
					this[prop] = argData[prop].clone();
				} else {
					this[prop] = argData[prop];
				}
				
			}
    	}
    }
    
	// 相等性比较
    Object.prototype.equalsData = function(argData) {
    	if (!argData) {
    		return false;
    	}
    	
    	var result = true;
    	var orderDataOne = this;
    	for (var prop in orderDataOne) {
    		// 如果是数组或是函数
    		if (typeof orderDataOne[prop] == 'function'
    			||	orderDataOne[prop] instanceof Array) {
				continue;
			}
			if (isNull(orderDataOne[prop]) && isNull(argData[prop])) {
				continue;
			}
    		if (orderDataOne[prop] != argData[prop]) {
//    			alert(prop + ': left=' + orderDataOne[prop] + ',right=' + argData[prop]);
    			result = false;
    			break;
    		}
    	}
    	return result;
	}

    function OrderData() {
    	// 采购订单表.流水号
    	this.orderId = null;
    	// 订单编号
    	this.purNo = null;
    	// 订单状态
    	this.purStatus = null;
    	//发票号 
    	this.invoiceNo = null;
    	// 合同编号
    	this.contractNo = null;
    	// 供应商编号
    	this.supplier = null;
    	// 采购人员
    	this.buyer = null;
    	// 采购人员名称
    	this.buyerName = null;
    	// 采购订单表.备注
    	this.orderMemo = null;
    	// 交期
    	this.orderDueDate = null;
    	// 税率
    	this.orderTaxRate = 0;
    	// 币种.Id
    	this.currencyId = null;
    	// 汇率
    	this.rate = 0;
    	// 采购订单表.上次修改日期
    	this.orderModifyDate = null;
    }
    
    function PlanOrderData() {
    	// 采购订单与需求计划关联表．流水号
    	this.planOrderId = null;
    	// 物资需求计划明细表.流水号
    	this.requirementDetailId = null;
    	// 物资需求计划明细表.申请单编号
    	this.requirementHeadId = null;
    	// 采购订单明细表.流水号
    	this.orderDetailsId = null;
    	// 物资需求计划明细表.核准数量
    	this.approvedQty = 0;
    	// 物资需求计划明细表.已生成采购的数量
    	this.planPurQty = 0;
    	// 物料ID
    	this.materialId = null;
    	// 物料编码
    	this.materialNo = null;
    	// 物料名称
    	this.materialName = null;
    	// 规格型号
    	this.specNo = null;
    	// 材质/参数
    	this.parameter = null;
    	// 是否免检
    	this.qaControlFlag = null;
    	// 报价
    	this.quotedPrice = null;
    	// 待采购数
		this.needQty = 0;
		
		// 交期
    	this.orderDetailsDueDate = null;
    	// 税率
    	this.orderDetailsTaxRate = 0;
    	// 备注
    	this.orderDetailsMemo = null;
		// 采购订单明细表.已收数量
		this.rcvQty = 0;
		// 物料主文件.最大库存量
    	this.maxStock = 0;
		// 物料主文件.采购计量单位
    	this.purUmId = null;
		// 是否是其它采购员的需求物资
		this.isOtherPlan = false;
		// 采购订单与需求计划关联表.上次修改日期
    	this.planRelateModifyDate = null;
		// 物资需求计划明细表.上次修改日期
    	this.planModifyDate = null;
    	//--------add by fyyang 090814------增加供应商
    	this.supplier=null;
    	this.supplyName=null;
    	this.supplierNo=null;
    	//------add end ---------------------------------
    	// add by liuyi 091127 增加物料单位
    	this.stockUmName = null;
    	// 申报部门 add by liuyi 20100406
    	this.sbDeptName = null; 
    	// 申报时的备注 add by liuyi 20100406 
    	this.sbMemo = null;
    }
    
    function OrderDetailsData() {
    	   	//--------add by fyyang 090814------增加供应商
    	this.supplier=null;
    	this.supplyName=null;
    	this.supplierNo=null;
    	//------add end ---------------------------------
    	// 采购订单明细表.流水号
    	this.orderDetailsId = null;
    	// 采购数量
    	this.purQty = null;
    	// 已收数量
    	this.rcvQty = 0;
    	// 交期
    	this.orderDetailsDueDate = null;
    	// 税率
    	this.orderDetailsTaxRate = 0;
    	// 备注
    	this.orderDetailsMemo = null;
    	// 物料ID
    	this.materialId = null;
    	// 物料编码
    	this.materialNo = null;
    	// 物料名称
    	this.materialName = null;
    	// 规格型号
    	this.specNo = null;
    	// 材质/参数
    	this.parameter = null;
    	// 是否免检
    	this.qaControlFlag = null;
    	// 报价
    	this.quotedPrice = null;
    	// 是否为需求单的采购单明细
    	this.purOrderDetailsId = null;
    	// 是否为需求单的采购单明细标志
    	this.isPlanDetail = false;
    	// 物料主文件.最大库存量
    	this.maxStock = 0;
    	// 物料主文件.采购计量单位
    	this.purUmId = null;
    	
    	// add by liuyi 091127 增加物料单位
    	this.stockUmName = null;
    	// 申报部门 add by liuyi 20100406
    	this.sbDeptName = null; 
    	// 申报时的备注 add by liuyi 20100406 
    	this.sbMemo = null;
		// 币种.Id
    	this.currencyId = null;
    	// 汇率
    	this.rate = null;
    	// 采购订单明细表.上次修改日期
    	this.orderDetailsModifyDate = null;
    	// 自动生成的ID
    	this.autoDetailId = 0;
    	
    	// 增加的操作
    	this.actions = [];
    	
    	if (typeof OrderDetailsData._intialized == 'undefined') {
            OrderDetailsData._intialized = true;
			
            // 合并
            OrderDetailsData.prototype.merge = function(argPlanData) {
            	if (this.actions.length < 1) {
            		// 初始化采购订单明细表.采购数量
            		this.purQty = 0;
            	}
            	this.actions.push(argPlanData);
    			
            	this.purQty = (Number(this.purQty) * 1000 + Number(argPlanData.needQty) * 1000)/1000;
            	this.purQty = Number(this.purQty).toFixed(2);
            }
            
            // 复制对象
            OrderDetailsData.prototype.clone = function() {
            	var newDetail = {};
            	Ext.apply(newDetail, this);
            	if (this.actions.length > 0) {
	            	newDetail.actions = [].concat(this.actions.clone());
            	}
            	
            	return newDetail;
            }
    	}
    }
    
    function OrderMediator(argPlanOrderDetails, argPlanOrder) {
    	// 采购票明细
    	this.orderDetailsData = argPlanOrderDetails;
    	// 需求计划物资
    	this.planOrderDatas = argPlanOrder;
    	
    	if (typeof OrderMediator._intialized == 'undefined') {
            OrderMediator._intialized = true;
            
            // 右移 ->>
            OrderMediator.prototype.rightMove = function(argPlan) {
            	var planIndex = this.findByPlan(argPlan);
        		argPlan = this.planOrderDatas[planIndex];
        		
            	var detailIndex = this.findDetailByMaterialId(argPlan.materialId);
            	var detail = null;
            	if (detailIndex > -1) {
            		detail = this.orderDetailsData[detailIndex];
					
            		// 如果没有设置,则加载
            		detail.loadData(argPlan, true);
            		// 数字字段特别处理
            		if (!detail.rcvQty && argPlan.rcvQty) {
            			detail.rcvQty = argPlan.rcvQty;
            		}
            		if (!detail.quotedPrice && argPlan.quotedPrice) {
            			detail.quotedPrice = argPlan.quotedPrice;
            		}
            		if (!detail.orderDetailsTaxRate && argPlan.orderDetailsTaxRate) {
            			detail.orderDetailsTaxRate = argPlan.orderDetailsTaxRate;
            		}
            		if (!detail.maxStock && argPlan.maxStock) {
            			detail.maxStock = argPlan.maxStock;
            		}
            		
            		detail.merge(argPlan);
            	} else {
            		// 通过物料ID得到采购单明细
            		var index = orderRegister.findDetailByMaterialId(
            			orderRegister.origialRegister.orderDetailsData['planed'], argPlan.materialId);
            		
        			var detail = {};
        			if (index > -1) {
        				Ext.apply(detail, orderRegister.origialRegister.orderDetailsData['planed'][index]);
        				detail.actions = [];
        			} else {
	            		detail = new OrderDetailsData();
	            		detail.loadData(argPlan);
	            		// 是否为需求单的采购单明细
	            		detail.isPlanDetail = true;
	            		
	            		// 交期
	            		if (!detail.orderDetailsDueDate) {
	            			detail.orderDetailsDueDate = renderDate(dfdueDate.getValue());
	            		}
	            		// 税率
	            		if (!detail.orderDetailsTaxRate) {
	            			detail.orderDetailsTaxRate = nfTaxRate.getValue();
	            		}
	            		// 币别
	            		if (!detail.currencyId) {
	            			detail.currencyId = cbxCurrencyId.getValue();
	            		}
	            		// 汇率
	            		if (!detail.rate) {
	            			detail.rate = nfExchageRate.getValue();
	            		}
        			}
            		detail.merge(argPlan);
            		this.orderDetailsData.push(detail);
            	}
            	
            	this.planOrderDatas.splice(planIndex, 1);
            }
            
            // 左移 <<-
            OrderMediator.prototype.leftMove = function(argMaterialId) {
            	var detailIndex = this.findDetailByMaterialId(argMaterialId);
            	if (detailIndex > -1) {
            		var detail = this.orderDetailsData[detailIndex];
					
            		var plans = detail.actions;
            		for (var i = 0; i< plans.length; i++) {
            			if (!plans[i].requirementHeadId) {
            				for (var j=0; j< this.planOrderDatas.length; j++) {
			            		var planData = this.planOrderDatas[j];
			            		if (planData.requirementHeadId
			            			&& planData.requirementDetailId == plans[i].requirementDetailId) {
			            			plans[i].requirementHeadId = planData.requirementHeadId;
			            			break;
			            		}
			            	}
            			}
            			this.planOrderDatas.push(plans[i]);
            		}
            		
            		this.orderDetailsData.splice(detailIndex, 1);
            	}
            }
            
            // 拆分
            OrderMediator.prototype.disassemble = function(argPlan, argCnt) {
            	var planIndex = this.findByPlan(argPlan);
            	argPlan = this.planOrderDatas[planIndex];
            	
            	if (!argCnt || argPlan.needQty == argCnt) {
            		return;
            	}
            	
            	var planOne = {}, planTwo = {};
            	Ext.apply(planOne, argPlan);
            	Ext.apply(planTwo, argPlan);
            	planOne.needQty = (argPlan.needQty * 1000 - argCnt * 1000) / 1000;
            	planOne.needQty = Number(planOne.needQty).toFixed(2);
            	planTwo.needQty = argCnt;
            	
            	this.planOrderDatas.splice(planIndex, 1, planOne, planTwo);
            }
            
            // 通过物料ID得到采购单明细的位置
            OrderMediator.prototype.findDetailByMaterialId = function(argmaterialId) {
            	var result = -1;
            	for (var i=0; i< this.orderDetailsData.length; i++) {
            		if (this.orderDetailsData[i].materialId == argmaterialId) {
            			result = i;
            			break;
            		}
            	}
            	return result;
            }
            
            // 通过需求计划物资得到它的位置
            OrderMediator.prototype.findByPlan = function(argPlan) {
            	var result = -1;
            	var planData = null;
            	for (var i=0; i< this.planOrderDatas.length; i++) {
            		planData = this.planOrderDatas[i];
            		if (planData.requirementDetailId == argPlan.requirementDetailId
            			&& planData.needQty == argPlan.needQty) {
            			result = i;
            			break;
            		}
            	}
            	return result;
            }
            
    	}
    }
    
    function OrderRegister() {
    	// 采购单部分是否有输入
    	this.isOrderChanged = false;
    	// 编辑模式
    	this.isEdit = true;
    	// 当前登录用户
    	this.currentUser = null;
    	// 是否已加载需求计划物资
    	this.planLoaded = false;
    	// 是否已加载其它采购员的需求计划物资
    	this.isOtherPlanLoaded = false;
    	// 是否显示所有的需求计划物资
    	this.showAllChecked = false;
    	// 初始化的步骤
    	this.initStep = 0;
    	// 是否初始化
    	this.isIntialized = false;
    	
    	// 是否上报
    	this.isReported = false;
    	
    	// 采购票
    	this.orderData = null;
    	// 采购票明细
    	this.orderDetailsData = {};
    	// 采购票明细(计划)
    	this.orderDetailsData['planed'] = [];
    	// 采购票明细(非计划)
    	this.orderDetailsData['noplaned'] = [];
    	// 需求计划物资
    	this.planOrderDatas = [];
    	// 其它采购员的需求计划物资
    	this.otherPlanOrderDatas = [];
    	// 处理对象
    	this.mediator = null;
    	// 回滚数据
    	this.rollbackData = {};
    	// 初始化的数据
    	this.origialRegister = null;
    	
    	if (typeof OrderRegister._intialized == 'undefined') {
            OrderRegister._intialized = true;
            
            // 初始化
            OrderRegister.prototype.init = function(argNum) {
            	if (this.isIntialized) return;
            	
            	if (!this.origialRegister) {
	            	// 设置初始值
	            	this.origialRegister = new OrderRegister();
            	}
            	
            	if (argNum == 1) {
            		// 初始化采购单明细
		        	
		        	if (this.orderData.purStatus == '1' || this.orderData.purStatus == '2') {
		        		// 已上报
		        		this.isReported = true;
		        	}
            		
            		if (!this.checkCurrentUser() || this.isReported) {
            			buttonStatus.setOtherUser();
            		} else if (this.isEdit) {
            			buttonStatus.setEdit();
	            		
		        		// 如果汇率已经变化并且画面可以编辑
		        		if (nfExchageRate.getValue() != this.orderData.rate) {
		        			var msg = Constants.RB002_I_002;
		        			if (!nfExchageRate.getValue()) {
		        				msg = Constants.RB002_I_003;
		        			}
		        			Ext.Msg.alert(Constants.NOTICE, msg);
		        		}
            		} else {
            			buttonStatus.setNew();
            		}
            		
	            	this.origialRegister.orderData = new OrderData();
	            	this.origialRegister.orderData.loadData(this.orderData);
	            	
		        	// 采购票明细(计划)
			    	Array.prototype.push.apply(this.origialRegister.orderDetailsData['planed'], this.orderDetailsData['planed'].clone());
			    	var tempOrigialPlanData = this.origialRegister.orderDetailsData['planed'];
			    	for (var i = tempOrigialPlanData.length-1; i>=0; i--) {
			    		if (tempOrigialPlanData[i].actions.length > 0) {
			    			var tempArr = tempOrigialPlanData[i].actions;
			    			tempOrigialPlanData[i].actions = [].concat(tempArr.clone());
			    		}
			    	}
			    	
			    	// 采购票明细(非计划)
			    	Array.prototype.push.apply(this.origialRegister.orderDetailsData['noplaned'], this.orderDetailsData['noplaned'].clone());
            	} else if (argNum == 2) {
            		// 初始化需求计划物资
            		
			    	// 需求计划物资
			    	Array.prototype.push.apply(this.origialRegister.planOrderDatas, this.planOrderDatas.clone());
	            	this.mediator = new OrderMediator(this.orderDetailsData.planed, this.planOrderDatas);
            	} else {
            		this.initStep = 0;
            		this.isIntialized = true;
            	}
            }
            
            // 得到采购单明细数组
            OrderRegister.prototype.getDetailsData = function(argReloadGrid) {
            	var result = [];
            	if (argReloadGrid) {
	            	// 重新读取Grid中的值
	            	this.refreshGridData();
            	}
            	
            	Array.prototype.push.apply(result, this.orderDetailsData['planed']);
            	Array.prototype.push.apply(result, this.orderDetailsData['noplaned']);
            	
            	return result;
            }
            
            // 检查用户是否有输入
            OrderRegister.prototype.checkChanged = function() {
            	if (!this.orderData) return false;
            	if (Ext.getCmp('btnOrderCreate').disabled) return false;
            	
            	// 重新读取Form和Grid中的值
            	this.refreshOrderForm();
            	this.refreshGridData();
            	
            	if (!this.orderData.equalsData(this.origialRegister.orderData)) {
            		this.isOrderChanged = true;
            		return true;
            	}
            	this.isOrderChanged = false;
				
            	var detail = null;
            	var tempOrigials = this.origialRegister.orderDetailsData['planed'];
            	var detailLen = this.orderDetailsData['planed'].length;
            	if (detailLen != tempOrigials.length) {
            		return true;
            	}
            	
            	var origialDetail = null;
            	for (var i = 0; i<detailLen; i++) {
            		detail = this.orderDetailsData['planed'][i];
            		
            		var index = this.findDetailByMaterialId(tempOrigials, detail.materialId);
            		if (index < 0) {
            			return true;
            		}
            		origialDetail = tempOrigials[index];
            		
            		if (!detail.equalsData(origialDetail)) {
            			return true;
            		}
            	}
            	
            	tempOrigials = this.origialRegister.orderDetailsData['noplaned'];
            	detailLen = this.orderDetailsData['noplaned'].length;
            	if (detailLen != tempOrigials.length) {
            		return true;
            	}
            	
            	for (var i = 0; i< detailLen; i++) {
            		detail = this.orderDetailsData['noplaned'][i];
            		
            		var index = this.findDetailById(tempOrigials, detail.orderDetailsId);
            		if (index < 0) {
            			return true;
            		}
            		origialDetail = tempOrigials[index];
            		
            		if (!detail.equalsData(origialDetail)) {
            			return true;
            		}
            	}
            	
            	return false;
            }
            
            // 检查采购员是否是当前用户
            OrderRegister.prototype.checkCurrentUser = function() {
            	if (!this.currentUser) {
            		return false;
            	}
            	return this.currentUser.code == this.orderData.buyer && !this.isReported;
            }
            
            // 右移 ->>
            OrderRegister.prototype.rightMove = function(argPlan) {
            	this.mediator.rightMove(argPlan);
            }
            
            // 左移 <<-
            OrderRegister.prototype.leftMove = function(argmaterialId) {
            	this.mediator.leftMove(argmaterialId);
            }
            
            // 拆分
            OrderRegister.prototype.disassemble = function(argPlan, argCnt) {
            	this.mediator.disassemble(argPlan, argCnt);
            }
            
            // 确定
            OrderRegister.prototype.submit = function() {
            	// 重新加载Grid
            	girdReload(false);
            }
            
            // 添加采购单明细
            OrderRegister.prototype.addDetail = function(argDetail, argPlanFlag) {
            	if (!this._autoDetailId) {
            		this._autoDetailId = 0;
            	}
            	this._autoDetailId ++;
            	
            	argDetail.autoDetailId = this._autoDetailId;
            	if (argPlanFlag) {
            		this.orderDetailsData['planed'].push(argDetail);
            	} else {
            		this.orderDetailsData['noplaned'].push(argDetail);
            	}
            }
            
            // 删除采购单明细
            OrderRegister.prototype.deleteDetail = function(argDetail) {
            	var tempDetails = this.orderDetailsData['noplaned'];
            	var index = -1;
            	
            	// 是否为需求单的采购单明细
            	if (argDetail.isPlanDetail) {
            		if (!this.mediator) {
            			this.mediator = new OrderMediator(this.orderDetailsData.planed, this.planOrderDatas);
            		}
            		this.leftMove(argDetail.materialId);
            	} else {
            		if (argDetail.orderDetailsId) {
        				index = this.findDetailById(tempDetails, argDetail.orderDetailsId);
        			} else {
        				index = this.findDetailByAutoId(tempDetails, argDetail.autoDetailId);
        			}
	            	
	            	tempDetails.splice(index, 1);
            	}
            }
            
            // 清空所有数据
            OrderRegister.prototype.clear = function() {
            	// 采购票明细(计划)
		    	this.orderDetailsData['planed'].length = 0;
		    	// 采购票明细(非计划)
		    	this.orderDetailsData['noplaned'].length = 0;
		    	// 需求计划物资
		    	this.planOrderDatas.length = 0;
		    	// 其它采购员的需求计划物资
    			this.otherPlanOrderDatas.length = 0;
		    	
		    	var lastCurrentUser = this.currentUser;
            	this.loadData(new OrderRegister());
            	this.currentUser = lastCurrentUser;
            	return this;
            }
            
            // 增加其它采购员的需求计划物资
            OrderRegister.prototype.addOtherPlan = function(argOtherPlan) {
            	var planData = null;
            	for (var i=0; i< this.planOrderDatas.length; i++) {
            		planData = this.planOrderDatas[i];
            		if (planData.requirementDetailId == argOtherPlan.requirementDetailId) {
            			return;
            		}
            	}
            	
            	var plan = new PlanOrderData();
        		plan.loadData(argOtherPlan);
        		plan.isOtherPlan = true;
        		// 待采购数
        		plan.needQty = (Number(plan.approvedQty)*1000 - Number(plan.planPurQty)*1000)/1000;
        		plan.needQty = Number(plan.needQty).toFixed(2);
        		this.otherPlanOrderDatas.push(plan);
            }
            
            // 清空其它采购员的需求计划物资
            OrderRegister.prototype.clearOtherPlans = function() {
            	var cnt = 0;
            	for (var i = this.planOrderDatas.length - 1; i>=0; i--) {
            		if (this.planOrderDatas[i].isOtherPlan) {
            			this.planOrderDatas.splice(i, 1);
            			cnt++;
            		}
            	}
            	return cnt;
            }
            
            // 通过物料ID得到采购单明细的位置
            OrderRegister.prototype.findDetailByMaterialId = function(argDetails, argmaterialId) {
            	var result = -1;
            	for (var i=0; i< argDetails.length; i++) {
            		if (argDetails[i].materialId == argmaterialId) {
            			result = i;
            			break;
            		}
            	}
            	return result;
            }
            
            // 设置更新位置
            OrderRegister.prototype.setEndPoint = function() {
            	if (!this.rollbackData.plans) {
            		this.rollbackData.plans = [];
            		this.rollbackData.details = [];
            	}
            	
            	this.rollbackData.showAllChecked = this.showAllChecked;
            	this.rollbackData.details.length = 0;
            	// 重新读取Grid中的值
            	this.refreshGridData();
            	// 复制对象
            	Array.prototype.push.apply(this.rollbackData.details, this.orderDetailsData['planed'].clone());
            	
            	this.rollbackData.plans.length = 0;
            	// 复制对象
            	Array.prototype.push.apply(this.rollbackData.plans, this.planOrderDatas.clone());
            }
            
            // 回滚
            OrderRegister.prototype.rollback = function() {
            	this.showAllChecked = this.rollbackData.showAllChecked;
            	this.orderDetailsData['planed'].length = 0;
            	// 复制对象
            	Array.prototype.push.apply(this.orderDetailsData['planed'], this.rollbackData.details.clone());
            	this.rollbackData.details.length = 0;
            	
            	this.planOrderDatas.length = 0;
            	// 复制对象
            	Array.prototype.push.apply(this.planOrderDatas, this.rollbackData.plans.clone());
            	this.rollbackData.plans.length = 0;
            }
            
            // 更新请求参数
            OrderRegister.prototype.checkParams = function() {
            	// 采购员
            	this.orderData.buyer = tfBuyer.getValue();
            	if (tfBuyer.getValue() == Constants.AUTO_CREATE) {
            		this.orderData.buyer = null;
            	}
            	// 供应商编号
            	this.orderData.supplier = hideSupplierId.getValue();
            }
            
            // 通过采购单明细ID得到采购单明细的位置
            OrderRegister.prototype.findDetailById = function(argDetails, argOrderDetailsId) {
            	var result = -1;
            	for (var i=0; i< argDetails.length; i++) {
            		if (argDetails[i].orderDetailsId == argOrderDetailsId) {
            			result = i;
            			break;
            		}
            	}
            	return result;
            }
            
            // 通过采购单明细客户端采番的ID得到采购单明细的位置
            OrderRegister.prototype.findDetailByAutoId = function(argDetails, argAutoDetailId) {
            	var result = -1;
            	for (var i=0; i< argDetails.length; i++) {
            		if (argDetails[i].autoDetailId == argAutoDetailId) {
            			result = i;
            			break;
            		}
            	}
            	return result;
            }
            
            // 重新读取Form中的值
            OrderRegister.prototype.refreshOrderForm = function() {
            	// ===========     重新读取Form中的值      =========
            	// 供应商编号
            	this.orderData.supplier = hideSupplierId.getValue();
            	// 交期
            	this.orderData.orderDueDate = renderDate(Ext.get('orderDueDate').dom.value);
            	// 币别.Id
            	this.orderData.currencyId = cbxCurrencyId.getValue();
            	//发票号 
        		  this.orderData.invoiceNo = ttinvoiceNo.getValue();
            	// 合同号
            	this.orderData.contractNo = tfContractNo.getValue();
            	// 税率
            	this.orderData.orderTaxRate = getNumber(nfTaxRate.getValue());
            	// 汇率
            	this.orderData.rate = getNumber(nfExchageRate.getValue());
            	
            	// 备注
            	this.orderData.orderMemo = tfMemo.getValue();
            }
            
            // 重新读取Grid中的值
            OrderRegister.prototype.refreshGridData = function() {
            	// ===========     重新读取Grid中的值      =========
            	var detail = null;
            	var origialDetail = null;
            	var tempDetailsData = [];
            	var index = -1;
            	
            	for (var i = queryStore.getCount() - 1; i >= 0; i--) {
            		detail = queryStore.getAt(i).data;
            		
			    	// 更新所有明细部分的汇率
					detail.rate = nfExchageRate.getValue();
    				
            		// 是否为需求单的采购单明细
            		if (!detail.isPlanDetail) {
            			tempDetailsData = this.orderDetailsData['noplaned'];
            			
            			if (detail.orderDetailsId) {
            				index = this.findDetailById(tempDetailsData, detail.orderDetailsId);
            			} else {
            				index = this.findDetailByAutoId(tempDetailsData, detail.autoDetailId);
            			}
            		} else {
            			tempDetailsData = this.orderDetailsData['planed'];
            			index = this.findDetailByMaterialId(tempDetailsData, detail.materialId);
            		}
        			
            		if (index < 0) {
            			continue;
            		}
        			tempDetailsData[index].loadData(detail);
        			// 交期
        			var detailDueDate = tempDetailsData[index].orderDetailsDueDate;
        			if (detailDueDate) {
        				tempDetailsData[index].orderDetailsDueDate = renderDate(detailDueDate);
        			}
            	}
            }
            
            // 得到画面变更的所有数据
            OrderRegister.prototype.getSaveData = function() {
            	// 采购单
            	var orderData = this.orderData;
            	if (!this.isOrderChanged) {
            		// 如果没有变更
            		orderData = {};
            		Ext.apply(orderData, this.orderData);
            		orderData.orderId = null;
            	}
            	
            	var saveData = {
            		// 采购单
            		order : orderData,
            		// 采购单明细
            		orderDetails : {
            			// 需求单
            			plan : this._getAllEditDetailDatas(true),
            			// 行政单
            			unplan : this._getAllEditDetailDatas(false)
            		},
            		// 得到应保存的所有需求物资数据
            		planDatas : this._getAllEditPlanDatas()
            	};
            	
            	return saveData;
            }
            
            // 得到画面删除的所有数据
            OrderRegister.prototype.getDeleteData = function() {
            	var deleteRegister = new OrderRegister();
            	
            	// 复制所有数据
            	deleteRegister.orderData = this.origialRegister.orderData;
            	// 采购票明细(计划)
		    	Array.prototype.push.apply(deleteRegister.orderDetailsData['planed'],
		    		this.origialRegister.orderDetailsData['planed'].clone());
		    	// 采购票明细(非计划)
		    	Array.prototype.push.apply(deleteRegister.orderDetailsData['noplaned'],
		    		this.origialRegister.orderDetailsData['noplaned'].clone());

		    	// 需求计划物资
		    	Array.prototype.push.apply(deleteRegister.planOrderDatas,
		    		this.origialRegister.planOrderDatas.clone());
            	
            	// 删除所有明细数据
	    		var detailDatas = deleteRegister.getDetailsData(false);
            	for (var i = detailDatas.length - 1; i>=0; i--) {
            		// 删除明细
            		deleteRegister.deleteDetail(detailDatas[i]);
            	}
            	
            	// 设置采购单标志
            	deleteRegister.isOrderChanged = true;
            	
            	deleteRegister.origialRegister = this.origialRegister;
            	var deleteData = deleteRegister.getSaveData();
            	
            	return deleteData;
            }
            
            // 得到应保存的所有采购单明细数据
            OrderRegister.prototype._getAllEditDetailDatas = function(argPlanFlag) {
            	var tempDetailsData = this.orderDetailsData['noplaned'];
            	var tempOrigialData = this.origialRegister.orderDetailsData['noplaned'];
            	if (argPlanFlag) {
            		tempDetailsData = this.orderDetailsData['planed'];
            		tempOrigialData = this.origialRegister.orderDetailsData['planed'];
            	}
            	
            	var result = {'add':[],'update':[],'delete':[]};
            	
            	var detail = null;
            	var addFlag = tempOrigialData.length < 1;
            	var updateFlag = true;
            	for (var i = tempDetailsData.length - 1; i>=0; i--) {
            		
            		detail = tempDetailsData[i];
            		var j = tempOrigialData.length - 1;
            		for (; j>=0; j--) {
            			// 采购订单明细表.流水号
            			if (!detail.orderDetailsId) {
            				addFlag = true;
            				updateFlag = false;
            				break;
            			}
            			if (detail.orderDetailsId == tempOrigialData[j].orderDetailsId) {
            				updateFlag = !detail.equalsData(tempOrigialData[j]);
            				break;
            			}
            		}
        			
            		if (addFlag || updateFlag) {
            			// 复制一份
        				var newDetail = detail.clone();
        				detail = newDetail;
        				
        				if (argPlanFlag) {
        					// 合并所有需求物资数据
        					var tempPlans = this.mergePlanDatas(detail.actions);
        					var tempOrigialPlans = [];
        					if (j>=0) {
        						tempOrigialPlans = this.mergePlanDatas(tempOrigialData[j].actions);
        					}
        					
        					// 得到应保存的所有采购订单与需求计划关联数据
        					detail.planRelated = this._getAllPlanRelatedDatas(tempPlans, tempOrigialPlans, addFlag);
        				}
        				detail.actions = [];
            		}
            		
        			if (addFlag) {
    					result.add.push(detail);
    				} else if (updateFlag) {
    					result.update.push(detail);
    				}
        			
            		addFlag = tempOrigialData.length < 1;
	            	updateFlag = true;
            	}
            	
            	var deleteFlag = true;
            	for (var j = tempOrigialData.length - 1; j>=0; j--) {
            		detail = tempOrigialData[j];
            		for (var i = tempDetailsData.length - 1; i>=0; i--) {
            			// 采购订单明细表.流水号
            			if (tempDetailsData[i].orderDetailsId == detail.orderDetailsId) {
            				deleteFlag = false;
            				break;
            			}
            		}
            		
            		if (deleteFlag) {
            			// 复制一份
        				var newDetail = detail.clone();
        				detail = newDetail;
        				
        				if (argPlanFlag) {
        					// 得到应删除的所有采购订单与需求计划关联数据
        					detail.planRelated = {
        						'add': [],
        						'update': [],
        						'delete': [].concat(detail.actions.clone())
    						};
        				}
        				detail.actions = [];
        				
            			result['delete'].push(detail);
            		}
            		deleteFlag = true;
            	}
            	
            	return result;
            }
            
            // 得到应保存的所有需求物资数据
            OrderRegister.prototype._getAllEditPlanDatas = function() {
            	var result = {'add':[],'update':[],'delete':[]};
            	
            	// 合并所有需求物资数据
            	var tempPlanDatas = this.mergePlanDatas(this.planOrderDatas);
            	var tempOrigial = this.origialRegister.planOrderDatas;
            	
            	var plan = null;
            	var addFlag = true;
            	var updateFlag = true;
            	for (var i = tempPlanDatas.length - 1; i>=0; i--) {
            		plan = tempPlanDatas[i];
            		for (var j = tempOrigial.length - 1; j>=0; j--) {
            			// 物资需求计划明细表.流水号
            			if (plan.requirementDetailId == tempOrigial[j].requirementDetailId) {
            				addFlag = false;
            				
            				// 物资需求计划明细表.待采购数
        					updateFlag = plan.needQty != tempOrigial[j].needQty;
        					break;
            			}
            		}
            		
            		if (addFlag || updateFlag) {
            			// 复制一份
            			var newPlan = {};
            			Ext.apply(newPlan, plan);
            			plan = newPlan;
            			
	            		if (addFlag) {
	    					result.add.push(plan);
	    				} else if (updateFlag) {
	    					result.update.push(plan);
	    				}
            		}
    				
        			addFlag = true;
        			updateFlag = true;
            	}
            	
            	var deleteFlag = true;
            	for (var j = tempOrigial.length - 1; j>=0; j--) {
            		plan = tempOrigial[j];
            		
            		for (var i = tempPlanDatas.length - 1; i>=0; i--) {
            			// 需求物资明细表.流水号
            			if (tempPlanDatas[i].requirementDetailId == plan.requirementDetailId) {
            				deleteFlag = false;
            				break;
            			}
            		}
            		
            		if (deleteFlag) {
            			// 复制一份
            			var newDetail = {};
            			Ext.apply(newDetail, plan);
            			plan = newDetail;
            			
            			result['delete'].push(plan);
            		}
            		deleteFlag = true;
            	}
            	
            	return result;
            }
            
            // 得到应保存的所有采购订单与需求计划关联数据
            OrderRegister.prototype._getAllPlanRelatedDatas = function(argPlanDatas, argOrigialPlanDatas, argAddFlag) {
            	var result = {'add':[],'update':[],'delete':[]};
            	
            	var tempPlanDatas = argPlanDatas;
            	var tempOrigial = argOrigialPlanDatas;
            	if (argAddFlag) {
            		result.add = [].concat(tempPlanDatas);
            		return result;
            	}
            	
            	var plan = null;
            	var addFlag = true;
            	var updateFlag = true;
            	for (var i = tempPlanDatas.length - 1; i>=0; i--) {
            		plan = tempPlanDatas[i];
            		for (var j = tempOrigial.length - 1; j>=0; j--) {
            			// 物资需求计划明细表.流水号  物资需求计划明细表.待采购数
            			if (plan.requirementDetailId == tempOrigial[j].requirementDetailId) {
            				addFlag = false;
            				updateFlag = plan.needQty != tempOrigial[j].needQty;
            				break;
            			}
            		}
            		
            		if (addFlag || updateFlag) {
            			// 复制一份
            			var newPlan = {};
            			Ext.apply(newPlan, plan);
            			plan = newPlan;
            			
	            		if (addFlag) {
	    					result.add.push(plan);
	    				} else if (updateFlag) {
	    					result.update.push(plan);
	    				}
            		}
    				
        			addFlag = true; 	
        			updateFlag = true;
            	}
            	
            	var deleteFlag = true;
            	for (var j = tempOrigial.length - 1; j>=0; j--) {
            		plan = tempOrigial[j];
            		for (var i = tempPlanDatas.length - 1; i>=0; i--) {
            			// 采购订单与需求计划关联表.流水号
            			if (tempPlanDatas[i].planOrderId == plan.planOrderId) {
            				deleteFlag = false;
            				break;
            			}
            		}
            		
            		if (deleteFlag) {
            			// 复制一份
            			var newPlan = {};
            			Ext.apply(newPlan, plan);
            			plan = newPlan;
            			
            			result['delete'].push(plan);
            		}
            		deleteFlag = true;
            	}
            	
            	return result;
            }
            
            // 合并所有需求物资数据
            OrderRegister.prototype.mergePlanDatas = function(argPlanDatas) {
            	var result = [];
            	var plan = null;
            	var mergeFlag = false;
            	for (var i = argPlanDatas.length - 1; i>=0; i--) {
            		plan = argPlanDatas[i];
            		for (var j = result.length - 1; j>=0; j--) {
            			// 物资需求计划明细表.流水号
            			if (plan.requirementDetailId == result[j].requirementDetailId) {
            				result[j].needQty += plan.needQty;
            				// 设置采购订单与需求计划关联表.流水号
            				if (plan.planOrderId && !result[j].planOrderId) {
            					result[j].planOrderId = plan.planOrderId;
            					// 设置采购订单与需求计划关联表.修改时间
            					result[j].planRelateModifyDate = plan.planRelateModifyDate;
            				}
            				
            				mergeFlag = true;
            				break;
            			}
            		}
            		
            		if (!mergeFlag) {
	            		var newPlan = {};
	            		Ext.apply(newPlan, plan);
	            		result[result.length] = newPlan;
            		}
            		mergeFlag = false;
            	}
            	
            	return result;
            }
    	}
    }
    
    function ButtonStatus() {
    	this.status = '';
    	
    	if (typeof ButtonStatus._initialized == 'undefined') {
    		ButtonStatus._initialized = true;
    		
    		// 初始化
    		ButtonStatus.prototype.setInit = function() {
    			this.status = 'init';
    			
    			Ext.getCmp('btnOrderPrint').setDisabled(true);
    			Ext.getCmp('btnOrderCreate').setDisabled(true);
    			Ext.getCmp('btnOrderDelete').setDisabled(true);
    			Ext.getCmp('btnOrderReport').setDisabled(true);
    			Ext.getCmp('btnDetailCreate').setDisabled(true);
    			Ext.getCmp('btnDetailAdd').setDisabled(true);
    			Ext.getCmp('btnDetailDelete').setDisabled(true);
    		}
    		
    		// 新增时
    		ButtonStatus.prototype.setNew = function() {
    			this.status = 'new';
    			
    			Ext.getCmp('btnOrderPrint').setDisabled(false);
    			Ext.getCmp('btnOrderCreate').setDisabled(false);
    			Ext.getCmp('btnOrderDelete').setDisabled(true);
    			Ext.getCmp('btnOrderReport').setDisabled(false);
    			Ext.getCmp('btnDetailCreate').setDisabled(false);
    			Ext.getCmp('btnDetailAdd').setDisabled(false);
    			Ext.getCmp('btnDetailDelete').setDisabled(false);
    			
    			tfContractNo.setDisabled(false);
    			tfSupplierNo.setDisabled(false);
    			dfdueDate.setDisabled(false);
    			nfTaxRate.setDisabled(false);
    			cbxCurrencyId.setDisabled(false);
    			tfMemo.setDisabled(false);
    			tfSupplierName.setDisabled(false);
    		}
    		
    		// 编辑时
    		ButtonStatus.prototype.setEdit = function() {
    			this.status = 'edit';
    			
    			this.setNew();
    			Ext.getCmp('btnOrderDelete').setDisabled(false);
    			tfSupplierNo.setDisabled(true);
    		}
    		
    		// 其它采购员
    		ButtonStatus.prototype.setOtherUser = function() {
    			this.status = 'otherUser';
    			
    			this.setInit();
    			tfContractNo.setDisabled(true);
    			tfSupplierNo.setDisabled(true);
    			dfdueDate.setDisabled(true);
    			nfTaxRate.setDisabled(true);
    			cbxCurrencyId.setDisabled(true);
    			tfMemo.setDisabled(true);
    			tfSupplierName.setDisabled(true);
    		}
    	}
    }
    
    // 全局变量
    var orderRegister = new OrderRegister();
    var buttonStatus = new ButtonStatus();
	// =======       对象定义结束       ==============
	
    // ********** 主画面******* //
    var wd = 180;
    
 	// head工具栏
    var headTbar = new Ext.Toolbar({
        items:[
             {
	            id : 'btnOrderPrint',
	            text : "打印",
	            iconCls : 'print',
	            handler : function() {
	            	printOrder();
	            }
            }, '-',{
	            id : 'btnOrderCreate',
	            text : '保存',
	            iconCls : Constants.CLS_SAVE,
	            handler : createPO
            }, '-',{
	            id : 'btnOrderAdd',
	            text : '新增',
	            iconCls : Constants.CLS_ADD,
	            handler : addOrder
            }, '-',{
	            id : 'btnOrderDelete',
	            text : Constants.BTN_DELETE,
	            iconCls : Constants.CLS_DELETE,
	            handler : deleteOrder
            }, '-', {
	            id : 'btnOrderReport',
	            text : "发送",
	            iconCls : Constants.CLS_REPOET,
	            handler : function() {
					if (orderRegister.checkChanged()) {
			    		Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_W_002, function(buttonobj) {
			                if (buttonobj == "yes") {
			                	// 生成PO
			                	createPO('report');
			                }
			    		});
			    	} else {    		
				    	// 如果画面中没有明细
				    	if (orderRegister.getDetailsData(false).length < 1) {
				    		Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_E_018);
				    		return false;
				    	}
						
			    		// 上报计划单
	    				reportOrder();
			    	}
	            }
            }]
    });
    
    // 采购单号
    var tfPurNo = new Ext.form.TextField({
        id : 'purNo',
        name : 'order.purNo',
        fieldLabel : "采购单号",
        value : Constants.AUTO_CREATE,
        disabled : true,
        maxLength : 20,
        width : wd
    });
    
    // 采购员名称
    var tfBuyerName = new Ext.form.TextField({
        id : 'buyerName',
        fieldLabel : "采购员",
        disabled : true,
        maxLength : 16,
        width : wd
    });
    
    // 采购员
    var tfBuyer = new Ext.form.Hidden({
        id : 'buyer',
        name : 'order.buyer'
    });
    
    // 供应商Id
    var hideSupplierId = new Ext.form.Hidden({
        id : 'supplier',
        name : 'order.supplier'
    });
    
    // 供应商编号
    var tfSupplierNo = new Ext.form.TextField({
        id : 'supplierNo',
        fieldLabel : "供应商编号<font color='red'>*</font>",
        allowBlank : false,
        width : wd,
        tabIndex : 2,
        readOnly : true
    });
    
    // 单击事件
    tfSupplierNo.onClick(selectSupplier);
    
    // 交期
    var dfdueDate = new Ext.form.TextField({
        style : 'cursor:pointer',
        id : "orderDueDate",
        name : 'order.orderDueDate',
        fieldLabel : "预计到货时间<font color='red'>*</font>",
        readOnly : true,
        allowBlank : false,
        tabIndex : 4,
        width : wd,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    alwaysUseStartDate : false,
                    dateFmt : 'yyyy-MM-dd',
                    onpicked : function(){
                        dfdueDate.clearInvalid();
                    },
                    onclearing:function(){
                    	dfdueDate.markInvalid();
                    }
                });
            }
        }
    });
    
    // 币别
    var storeCurrencyId = new Ext.data.JsonStore({
        url : 'resource/getRegisterCurrencyList.action',
        root : 'list',
        fields : ['currencyId', 'currencyName']
    });
    if (register.orderData) {
    	
    	storeCurrencyId.on('load', function() {
    		// 设置币别
    		
    		cbxCurrencyId.setValue(register.orderData.currencyId);
    	});
    }
    else
    {
    	
    	//add by fyyang 090514
    	storeCurrencyId.on('load', function() {
    		// 默认为人民币
    		
    		cbxCurrencyId.setValue(2);
    		//
    			// 头部的汇率做变更
			Ext.Ajax.request({
	            method : Constants.POST,
	            url : 'resource/getRegisterExchangeRate.action',
	            params : {
	            	currencyId : cbxCurrencyId.getValue()
	        	},
	            success : function(result, request) {
	        		nfExchageRate.setValue(result.responseText);
	        		
	        		for (var i = queryStore.getCount() - 1; i>=0; i--) {
		    			// 明细部分的币别
		    			queryStore.getAt(i).data.currencyId = cbxCurrencyId.getValue();
		    			// 明细部分的汇率
		    			queryStore.getAt(i).data.rate = nfExchageRate.getValue();
					}
	            }
			});
    	});
    }
    storeCurrencyId.load();
    var cbxCurrencyId = new Ext.form.ComboBox({
        id : 'currencyId',
        fieldLabel : "币别<font color='red'>*</font>",
        triggerAction : 'all',
        store : storeCurrencyId,
        displayField : "currencyName",
        valueField : "currencyId",
        hiddenName : 'order.currencyId',
        mode : 'local',
        readOnly : true,
        allowBlank : false,
        tabIndex : 6,
        width : wd,
        listeners : {
            select : selecteCurrency
        }
    });
    
    //发票号  add by bjxu  091209
	var ttinvoiceNo = new Ext.form.TextField({
		id : "invoiceNo",
		height : 22,
		fieldLabel : '发票号',
		readOnly : false,
		width : wd,
		name : 'order.invoiceNo',
		maxLength : 200
	});
    
    // 合同号
    var tfContractNo = new Ext.form.TextField({
        id : 'contractNo',
        name : 'order.contractNo',
        fieldLabel : "合同号",
        maxLength : 30,
        codeField : 'yes',
        tabIndex : 1,
        width : wd,
        style: {
        	'ime-mode' : 'disabled'
        }
    });
    
    // 供应商名称
    var tfSupplierName = new Ext.form.TextField({
        id : 'supplyName',
        fieldLabel : "供应商名称",
        readOnly : true,
        maxLength : 100,
        tabIndex : 3,
        width : wd
    });
    
    // 税率
    var nfTaxRate = new Powererp.form.NumField({
        id : 'orderTaxRate',
        name : 'order.orderTaxRate',
        style : 'text-align:right',
        fieldLabel : "税率<font color='red'>*</font>",
        allowNegative : false,
        allowBlank : false,
        tabIndex : 5,
       	value : 0.17,
        decimalPrecision : 4,
        maxValue : 9999999999999.9999,
        allowNegative : false,
        padding : 4,
        width : wd
    });
    
    // 汇率
    var nfExchageRate = new Powererp.form.NumField({
        id : 'rate',
        name : 'order.rate',
        fieldLabel : "汇率",
        disabled : true,
        decimalPrecision : 4,
        maxValue : 9999999999999.9999,
        allowNegative : false,
        padding : 4,
        style : "text-align:right",
        width : wd
    });
    
    // 备注
    var tfMemo = new Ext.form.TextArea({
        id : 'orderMemo',
        name : 'order.orderMemo',
        fieldLabel : "备注",
        tabIndex : 7,
        height : 50,
        maxLength : 128,
        width : wd + 311
    });

    var baseInfoField = new Ext.form.FieldSet({
        border : false,
        labelAlign : 'right',
        labelWidth : 90,
        layout : 'form',
        width : 650,
        defaults : {
        	autoScroll : true
        },
    	autoHeight : true,
        style : {
            "padding-top" : "5px",
            "padding-left" : "5px",
            'border' : 0
        },
        items : [
//        	{
//                    layout : 'form',
//                    border : false,
//                    items : [tfPurNo]
//                }, 
                	{
		    		layout : 'column',
		    		autoHeight : true,
		    		border : false,
		    		items : [{
		    			columnWidth : 0.495,
						layout : 'form',
						border : false,
						items : [tfPurNo,tfBuyerName, tfBuyer, hideSupplierId, tfSupplierNo, dfdueDate, cbxCurrencyId]
		    			
		    		}, {
		    			columnWidth : 0.495,
						layout : 'form',
						border : false,
						items : [ttinvoiceNo,tfContractNo, tfSupplierName, nfTaxRate, nfExchageRate]
		    		}]
	    		}, {
	    			layout : 'form',
	    			border : false,
	    			autoHeight : true,
                    items : [tfMemo]
    	}]
    });
    
    var formPanel = new Ext.form.FormPanel({
    	layout : 'form',
    	frame : false,
    	border : false,
    	height : 225,
    	items : [baseInfoField]
    });
    var panel = new Ext.Panel({
    	region : 'north',
    	border : false,
    	layout : 'fit',
    	tbar : headTbar,
        height : 228,
    	items : [formPanel]
    });
    
    // grid工具栏
    var gridTbar = new Ext.Toolbar({
        items:[{
	            id : 'btnDetailCreate',
	            text : "按计划生成",
	            iconCls : Constants.CLS_LIST,
	            handler : createByPlan
            }, '-', {
	            id : 'btnDetailAdd',
	            text : '新增明细',
	            iconCls : Constants.CLS_ADD,
	            handler : addOrderDetail
            }, '-',{
	            id : 'btnDetailDelete',
	            text : '删除明细',
	            iconCls : Constants.CLS_DELETE,
	            handler : deleteOrderDetail
            }]
    });

	//定义选择列
	var sm = new Ext.grid.RowSelectionModel({
		singleSelect : true
	});

    // grid中的数据Record
    var gridRecord = new Ext.data.Record.create([{
            // 项次号
            name : 'orderDetailsId'
        }, {
            // 物料ID
            name : 'materialId'
        }, {
            // 物料编码
            name : 'materialNo'
        }, {
            // 物料名称
            name : 'materialName'
        },{
            // 规格型号
            name : 'specNo'
        },{
            // 材质/参数
            name : 'parameter'
        }, {
            // 采购数量
            name : 'purQty'
         }, {
            // 已收数量
            name : 'rcvQty'
        }, {
            // 单价
            name : 'quotedPrice'
        }, {
            // 交期
            name : 'orderDetailsDueDate'
        },{
            // 税率
            name : 'orderDetailsTaxRate'
        },{
            // 小计金额
            name : ''
        },{
            // 税额
            name : ''
        },{
            // 是否免检
            name : 'qaControlFlag'
        }, {
            // 备注
            name : 'orderDetailsMemo'
        }, {
            // 是否为需求单的采购单明细
            name : 'isPlanDetail'
        }, {
            // 自动生成的ID
            name : 'autoDetailId'
        }, {
            // 计量单位
            name : 'purUmId'
        }, {
            // 最大库存量
            name : 'maxStock'
        }, {
            // 汇率
            name : 'rate'
    }
    // add by liuyi 091127 计量单位名称
   	,{
   		name : 'stockUmName'
   	},{
   		name : 'sbDeptName' // add by liuyi 20100406 申报部门
   	},{
   		name : 'sbMemo' // add by liuyi 20100406 申报时的备注
   	}
    ]);
    
    // 是否免检 显示为Checkbox
    var ckcQaControlFlag = new Ext.grid.CheckColumn({
        header : "是否免检",
        dataIndex : 'qaControlFlag',
        width : 60
    });
    
	// Grid列定义
    var columnModel = new Ext.grid.ColumnModel([
    	new Ext.grid.RowNumberer({
			header : '行号',
			width : 35
		}), {
            header : "项次号",
            sortable: true,
            hidden : true,
            width : 60,
            align : 'right',
            dataIndex : 'orderDetailsId'
        }, {
            header : "物料编码<font color='red'>*</font>",
            sortable: true,
            width : 80,
            css:CSS_GRID_INPUT_COL,
            dataIndex : 'materialNo'
            //modify by drdu 090730
//            editor: new Ext.form.TextField({
//            	id : 'editorMaterialNo',
//            	allowBlank: false,
//            	readOnly : true,
//               	listeners : {
//                    focus : selectMaterial
//                }
//            })
        }, {
            header : "物料名称",
            sortable: true,
            width : 100,
            dataIndex : 'materialName'
        },{
            header : "规格型号",
            sortable: true,
            width : 60,
            dataIndex : 'specNo'
        },{
            header : "材质/参数",
            sortable: true,
            width : 60,
            dataIndex : 'parameter'
        },
        // add by liuyi 091127 
        	{
            header : "单位",
            sortable: true,
            width : 60,
            dataIndex : 'stockUmName'
        },
        	{
            header : "采购数量<font color='red'>*</font>",
            sortable: true,
            width : 60,
            align : 'right',
            dataIndex : 'purQty',
            renderer : moneyFormat,
            css:CSS_GRID_INPUT_COL,
            editor: new Ext.form.NumberField({
            	allowNegative : false,
            	maxValue : 9999999.9999,
            	allowBlank: false,
            	decimalPrecision : 4,
            	listeners : {
               		'blur': function() {
               			var record = gridOrder.getSelectionModel().getSelected();
               			var totalMoney = (10000 * Number(record.data.quotedPrice) * this.value)/10000;
               			
           				record.set('totalMoney', totalMoney);
           				record.set('totalRate', (10000 * Number(record.data.orderDetailsTaxRate) * totalMoney)/10000);
               		}
            	}
            })
        }, {
            header : "已收数量",
            sortable: true,
            width : 60,
            renderer : moneyFormat,
            align : 'right',
            dataIndex : 'rcvQty'
        }, {
            header : "单价<font color='red'>*</font>",
				sortable : true,
				width : 60,
				align : 'right',
//				renderer : function(v) {
//					if (v != null && v != 'undefined') {
//						var totalMoney = 0;
//						var record = gridOrder.getSelectionModel().getSelected();
//						if(record != null && record.data != null && record.data.purQty != null)
//						{totalMoney = (10000 * Number(record.data.purQty) * Number(v).toFixed(2))/ 10000;
//						record.set('totalMoney', totalMoney);}
//					}
//					return renderMoney(v)
//				},
				dataIndex : 'quotedPrice',
				css : CSS_GRID_INPUT_COL,
				editor : new Ext.form.TriggerField({
					id : 'trigglerPrice',
					maxLength : 22,
					allowBlank : false,
					style : {
						'text-align' : 'right',
						'ime-mode' : 'disabled'
					},
		        onTriggerClick : function() {
		        	if (!orderRegister.checkCurrentUser()) {
			    		return false;	
			    	}
		        	// 单击‘单价’单元格
		    		
		    		var arg = {};
		    		var record = gridOrder.getSelectionModel().getSelected();
		    		arg.materialId = record.data.materialId;
		    		arg.supplier = hideSupplierId.getValue();
					var price = window.showModalDialog(
						'RB002_price.jsp',
						arg, 'dialogWidth=400px;dialogHeight=270px;status=no');
					
					if (typeof price == 'object') {
						var quotedPrice = Number(price.quotedPrice).toFixed(2);
						record.set('quotedPrice', quotedPrice);
						Ext.get('trigglerPrice').dom.value = quotedPrice;
					}
		        },
		        initEvents : function(){
		        	Ext.form.TriggerField.prototype.initEvents.call(this);
		        	var keyPress = function(e){
			            var k = e.getKey();
			            if(!Ext.isIE && (e.isSpecialKey() || k == e.BACKSPACE || k == e.DELETE)){
			                return;
			            }
			            var c = e.getCharCode();
			            var allowed = '0123456789.';
			            if(allowed.indexOf(String.fromCharCode(c)) === -1){
			                e.stopEvent();
			            }
			            
			            var tempNum = this.getRawValue() + '' + String.fromCharCode(c);
			            if (!checkNumLen(tempNum, 13, 4)) {
			            	e.stopEvent();
			            }
			        };
			        this.el.on("keypress", keyPress, this);
		        }
		    })
        }, {
            header : "预计到货时间<font color='red'>*</font>",
            sortable: true,
            width : 100,
            dataIndex : 'orderDetailsDueDate',
            renderer : function(value) {
            	if (!value) {
            		return '';
            	}
            	if (value instanceof Date) {
            		return value.dateFormat('Y-m-d');
            	}
            	return renderDate(value);
            },	
            css:CSS_GRID_INPUT_COL,
            editor : new Ext.form.TextField({
		        style : 'cursor:pointer',
		        id : "temp",
		        readOnly : true,
		        allowBlank: false,
		        listeners : {
		            focus : function() {
		                WdatePicker({
		                    startDate : '%y-%M-%d',
		                    alwaysUseStartDate : false,
		                    dateFmt : 'yyyy-MM-dd',
		                    onpicked : function() {
								gridOrder.getSelectionModel().getSelected().set(
								    "orderDetailsDueDate", Ext.get("temp").dom.value);
		                    },
		                    onclearing : function() {
		                    	gridOrder.getSelectionModel().getSelected().set(
								    "orderDetailsDueDate", null);
		                    }
		                });
		            }
		        }
		    })
        },{
            header : "税率<font color='red'>*</font>",
            sortable: true,
            width : 60,
            align : 'right',
            renderer : renderMoney,
            dataIndex : 'orderDetailsTaxRate',
            css:CSS_GRID_INPUT_COL,
            editor: new Ext.form.NumberField({
            	allowBlank: false,
            	allowNegative : false,
            	maxValue : 9999999999999.9999,
            	decimalPrecision : 4,
            	listeners : {
               		'blur': function() {
               			var record = gridOrder.getSelectionModel().getSelected();
               			var totalMoney = record.data.totalMoney;
               			if (totalMoney) {
               				record.set('totalRate', (10000 * Number(totalMoney) * this.value)/10000);
               			}
               		}
            	}
            })
        },{
            header : "小计金额",
            sortable: true,
            dataIndex : 'totalMoney',
            width : 60,
            align : 'right',
            renderer : function(value, params, record) {
            	if (record.data.materialNo != null && record.data.materialNo != ''
            	&& record.data.materialNo != 'undefined') {
						var v = (10000 * Number(record.data.purQty) * Number(record.data.quotedPrice))
								/ 10000;
						return renderMoney(v);
					}
					else
					{
						return renderMoney(value)
					}
					
            }
        }, {
            header : "税额",
            sortable: true,
            dataIndex : 'totalRate',
            width : 60,
            align : 'right',
            renderer : function(value, params, record) {
            	if(record.data.materialNo==null||record.data.materialNo=="undefined")
            	{
            		//modify by fyyang 090723 合计行
            		return value;
            	}
            	else
            	{
            	// modify by ywliu 2009/7/3  修改税额计算
            	var v = (10000 * (Number(record.data.purQty) * Number(record.data.quotedPrice)/(1+Number(record.data.orderDetailsTaxRate)))
            		* Number(record.data.orderDetailsTaxRate))/10000;
        		return renderMoney(v);
            	}
            }
        }, ckcQaControlFlag
        , {
            header : "备注",
            sortable: true,
            width : 250,
            dataIndex : 'orderDetailsMemo',
            css:CSS_GRID_INPUT_COL,
            editor : new Ext.form.TextArea({
            		maxLength : 250,
					listeners:{
					    "render" : function() {
					    	if (!orderRegister.checkCurrentUser()) {
					    		return false;
					    	}
					        this.el.on("dblclick", function(){ 
					        	var record = gridOrder.getSelectionModel().getSelected();
                              	gridOrder.stopEditing();
                              	taShowMemo.setValue(record.get("orderDetailsMemo"));
                              	win.show();
					        })
					    }
					}
				})
        }, {
        	header : '是否为需求单的采购单明细',
            hidden : true,
            dataIndex : 'isPlanDetail'
        }, {
        	header : '自动生成的ID',
            hidden : true,
            dataIndex : 'autoDetailId'
        }, {
        	header : '物料ID',
            hidden : true,
            dataIndex : 'materialId'
        }, {
        	header : '计量单位',
            hidden : true,
            dataIndex : 'purUmId'
        }, {
        	header : '最大库存量',
            hidden : true,
            dataIndex : 'maxStock'
        }, {
        	header : '汇率',
            hidden : true,
            dataIndex : 'rate'
    }, {
        	header : '需求备注',
            dataIndex : 'sbMemo'
    }, {
        	header : '申报部门',
            dataIndex : 'sbDeptName'
    }]);
    
    // grid的store
    var queryStore = new Ext.data.JsonStore({
        url : 'resource/getRegisterOrderDetailsByNo.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : gridRecord
    });
    queryStore.setDefaultSort('orderDetailsId', 'asc');
    queryStore.on('load', function() {
    	var disabledFlag = this.getCount() < 1;
		
    	if (orderRegister.checkCurrentUser()) {
			Ext.getCmp('btnDetailDelete').setDisabled(disabledFlag);
    	}
    	
    	//---------add by fyyang 090723 增加统计行--
    	var totalpurQty = 0; // 采购数
		var totalrcvQty = 0; // 已收数
		var totalquotedPrice = 0; // 单价
		var totaltotalMoney=0;//小计金额
		var mytotalRate=0;  //税额
		var myflag=false;
				for (var j = 0; j < queryStore.getCount(); j++) {
					var temp = queryStore.getAt(j);
					if (temp.get("purQty") != null) {
						totalpurQty = parseFloat(totalpurQty)
								+ parseFloat(temp.get("purQty"));
					}
					if (temp.get("rcvQty") != null) {
						totalrcvQty = parseFloat(totalrcvQty)
								+ parseFloat(temp.get("rcvQty"));
								myflag=true;
					}
					// modified by liuyi 091110 单价不用合计，小计金额合计
//					if (temp.get("quotedPrice") != null) {
//						totalquotedPrice = parseFloat(totalquotedPrice)
//								+ parseFloat(temp.get("quotedPrice"));
//					}
					if (temp.get("purQty") != null && temp.get("quotedPrice") != null) {
						totaltotalMoney = parseFloat(totaltotalMoney)
								+ parseFloat(temp.get("purQty") * parseFloat(temp.get("quotedPrice")));
					}
						var rate = (10000 * (Number(temp.get("purQty")) * Number(temp.get("quotedPrice"))/(1+Number(temp.get("orderDetailsTaxRate"))))
            		* Number(temp.get("orderDetailsTaxRate")))/10000;
            		
						mytotalRate =  parseFloat(mytotalRate)+rate;
					
				}
				
		var mydata=new gridRecord({
		purQty :totalpurQty.toFixed(4),
//		rcvQty: (myflag ? true :totalrcvQty.toFixed(2),null),
		rcvQty: (myflag == true ?totalrcvQty.toFixed(2):null),
//		quotedPrice :totalquotedPrice.toFixed(2),
		totalMoney : totaltotalMoney.toFixed(2),
		totalRate:mytotalRate.toFixed(2)
		});
		
//		flagCount = true;
		 queryStore.add(mydata);
		
    	//----------------------------------------
	});
	
    // 页面的Grid主体
    var gridOrder = new Ext.grid.EditorGridPanel({
    	region : 'center',
        store : queryStore,
        cm : columnModel,
        sm : sm,
        tbar : gridTbar,
     //  plugins : [ckcQaControlFlag],
        frame : false,
        border : false,
        enableColumnMove : false,
        clicksToEdit:1
    });
    gridOrder.on('beforeedit', function(obj) {
    	if (!orderRegister.checkCurrentUser()) {
    		return false;
    	}
    	// 是否为需求单的采购单明细
    	if (obj.record.get('isPlanDetail')) {
    		if (obj.field == 'materialNo') {
    			return false;
    		}
    		if (obj.field == 'purQty') {
    			return false;
    		}
    	}
    	//-----------add by fyyang 合计行-------------------
    	if(obj.field == 'materialNo'||obj.field == 'purQty'||obj.field == 'quotedPrice'
    	||obj.field=="orderDetailsDueDate"||obj.field=="orderDetailsTaxRate"
    	||obj.field=="orderDetailsMemo")
    	{
    		if(obj.record.get("orderDetailsDueDate")==null||obj.record.get("orderDetailsDueDate")=="undefined")
    		{
    			return false;
    		}
    	}
    	//-------------------------------------------------
    	return true;
    });
    
    //---------add by fyyang 090723---合计行计算-----------
     gridOrder.on('afteredit', function(obj) {
     	if(obj.field == 'materialNo'||obj.field == 'purQty'||obj.field == 'quotedPrice'
    	||obj.field=="orderDetailsTaxRate")
    	{
    		if(obj.record.get("orderDetailsDueDate")!=null&&obj.record.get("orderDetailsDueDate")!="undefined")
    		{
		    	var totalpurQty = 0; // 采购数
				
				var totalquotedPrice = 0; // 单价
				var totaltotalMoney=0;//小计金额
				var mytotalRate=0;  //税额
				for (var j = 0; j < queryStore.getCount()-1; j++) {
					var temp = queryStore.getAt(j);
					if (temp.get("purQty") != null) {
						totalpurQty = parseFloat(totalpurQty)
								+ parseFloat(temp.get("purQty"));
					}
					// modified by liuyi 091110 单价不用合计，小计金额合计
//					if (temp.get("quotedPrice") != null) {
//						totalquotedPrice = parseFloat(totalquotedPrice)
//								+ parseFloat(temp.get("quotedPrice"));
//					}
					if (temp.get("purQty") != null && temp.get("quotedPrice") != null) {
						totaltotalMoney = parseFloat(totaltotalMoney)
								+ parseFloat(temp.get("purQty") * parseFloat(temp.get("quotedPrice")));
					}
						var rate = (10000 * (Number(temp.get("purQty")) * Number(temp.get("quotedPrice"))/(1+Number(temp.get("orderDetailsTaxRate"))))
            		* Number(temp.get("orderDetailsTaxRate")))/10000;
            		
						mytotalRate =  parseFloat(mytotalRate)+rate;
					
				}
				
				queryStore.getAt(queryStore.getCount() - 1).set('purQty',
												totalpurQty.toFixed(4));
//				queryStore.getAt(queryStore.getCount() - 1).set('quotedPrice',
//												totalquotedPrice.toFixed(2));
				queryStore.getAt(queryStore.getCount() - 1).set('totalRate',
												mytotalRate.toFixed(2));
				queryStore.getAt(queryStore.getCount() - 1).set('totalMoney',
												totaltotalMoney.toFixed(2));
    		}
    	}
     });
    //--------------------------------------------
    
    // 主框架
    new Ext.Viewport({   
        layout : "border",
        autoHeight : true,
        defaults : {
        	autoScroll : true
        },
        items : [panel, gridOrder]
    });
    
    // 备注
    var taShowMemo = new Ext.form.TextArea({
		id : "taShowMemo",
		maxLength : 250,
    	width : 180
    });
    
    // 弹出画面
	var win = new Ext.Window({
		height : 170,
		width : 350,
		layout : 'fit',
		resizable : false,
		modal  : true,
		closeAction : 'hide',
		items : [taShowMemo],
		buttonAlign : "center",
		title : '详细信息录入窗口',
		buttons : [{
			text : Constants.BTN_SAVE,
			iconCls : Constants.CLS_SAVE,
			handler : function() {
				var record = gridOrder.getSelectionModel().getSelected();
				record.set("orderDetailsMemo", taShowMemo.getValue());
				win.hide();
			}
		}, {
			text : Constants.BTN_CANCEL,
			iconCls : Constants.CLS_CANCEL,
			handler : function() {
				win.hide();
			}
		}]
	});
	win.on('show', function() {
		taShowMemo.focus(true, 100);
	});
    // ↑↑********** 主画面*******↑↑//

    // ↓↓*********处理***********↓↓//
    function renderDate(value) {
    	if (value instanceof Date) {
    		return value.dateFormat('Y-m-d');
    	}
    	value = value ? value.match(/\d{4}-\d{2}-\d{2}/gi) : '';
    	return value ? value[0] : '';
    }
    
    function renderModifyDate(value) {
    	if (!value) return "";
    	if (value instanceof Date) {
    		return value.dateFormat('Y-m-d H:i:s');
    	}
        var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
        var reTime = /\d{2}:\d{2}:\d{2}/gi;
        var strDate = value.match(reDate);
        var strTime = value.match(reTime);
        if (!strDate) return "";
        strTime = strTime ? strTime[0] : '00:00:00';
        return strDate[0] + " " + strTime;
    }
    
    // textField显示时间比较方法
    function compareDate(argDate1, argDate2){
    	var dateStr1 = argDate1;
    	var dateStr2 = argDate2;
    	if (argDate1 instanceof Date) {
    		dateStr1 = argDate1.dateFormat('Y-m-d');
    	}
    	if (argDate2 instanceof Date) {
    		dateStr2 = argDate2.dateFormat('Y-m-d');
    	}
        
        return dateStr1 >= dateStr2;
    }
    
    function isNull(value) {
    	return !value && value != '0';	
    }
    
    function getNumber(argValue) {
    	if (!argValue) {
    		return 0;
    	}
    	return Number(argValue);
    }
    
    // 检查数字的最大长度,包括小数和小数点
    function checkNumLen(argNum, argIntLen, argPointLen) {
    	var num = getNumber(argNum);
    	if (isNaN(num)) {
    		return false;
    	}
    	if (!checkPointLen(argNum, argPointLen)) {
    		return false;
    	}
    	
    	return String(num.toFixed(argPointLen)).length <= argIntLen + argPointLen + 1;
    }
    
    function checkPointLen(argNum, argPonitLen) {
    	var s = String(argNum);
    	var index = s.lastIndexOf('.');
    	if (index > -1 && index < s.length) {
    		var str = s.substring(index + 1);
    		return str.length <= argPonitLen;
    	}
    	return true;
    }
    
    function renderMoney(v) {
    	return renderNumber(v, 2);// modify by ywliu 2009/7/3 修改计算金额现在2位小数
    }
    
    function renderNumber(v, argDecimal) {
		if (v) {
			if (typeof argDecimal != 'number') {
				argDecimal = 2;
			}
			v = Number(v).toFixed(argDecimal);
			var t = '';
			v = String(v);
			while ((t = v.replace(/^(-?[0-9]+)([0-9]{3}.*)$/, '$1,$2')) !== v)
				v = t;
			
			return v;
		} else
			return '';
	}
    
    // 在供应商查询Tab画面选择（共通控件）
    function selectSupplier() {
    	this.sValue = this.getValue();
    	if (Ext.get('supplierNo').dom.disabled) {
    		return;
    	}
		var supply = window.showModalDialog(
				'../../../../../comm/jsp/supplierQuery/supplierQuery.jsp',
				window, 'dialogWidth=800px;dialogHeight=600px;status=no');
		if (typeof(supply) != "undefined" && this.sValue != supply.supplier) {
			hideSupplierId.setValue(supply.supplierId);
			tfSupplierNo.setValue(supply.supplier);
			tfSupplierName.setValue(supply.supplyName);
			
			for (var i = queryStore.getCount() - 1; i>=0; i--) {
				// 明细部分的单价清空
				queryStore.getAt(i).data.quotedPrice = null;
			}
			
			// 重新加载Grid
			girdReload(true);
			
			// 供应商与汇率无关
//			var currencyId = cbxCurrencyId.getValue();
//			if (currencyId) {
//				// 头部的汇率做变更
//				Ext.Ajax.request({
//		            method : Constants.POST,
//		            url : 'resource/getRegisterExchangeRate.action',	
//		            success : function(result, request) {
//	            		nfExchageRate.setValue(result.responseText);
//				        
//						for (var i = queryStore.getCount() - 1; i>=0; i--) {
//							// 明细部分的单价清空
//							queryStore.getAt(i).data.quotedPrice = null;
//							// 明细部分的汇率
//							queryStore.getAt(i).data.rate = nfExchageRate.getValue();
//						}
//						
//						// 重新加载Grid
//						girdReload(true);
//		            },
//		            params : {
//		            	currencyId : currencyId,
//		            	supplier : hideSupplierId.getValue()
//		        	}
//				});
//			} else {
//				nfExchageRate.setValue('');
//				
//				for (var i = queryStore.getCount() - 1; i>=0; i--) {
//					// 明细部分的单价清空
//					queryStore.getAt(i).data.quotedPrice = null;
//					// 明细部分的汇率
//					queryStore.getAt(i).data.rate = 0;
//				}
//				
//				// 重新加载Grid
//				girdReload(true);
//			}
		}
	}
    
	// ‘币别’内容发生变更
	function selecteCurrency() {
		if (!this.sValue) {
			this.sValue = this.startValue;
		}
		
		if (this.sValue != this.getValue()) {
			// 供应商与汇率无关
//			var supplier = hideSupplierId.getValue();
//			if (!supplier) {
//				nfExchageRate.setValue('');
//				
//				for (var i = queryStore.getCount() - 1; i>=0; i--) {
//	    			// 明细部分的币别
//	    			queryStore.getAt(i).data.currencyId = null;
//	    			// 明细部分的汇率
//	    			queryStore.getAt(i).data.rate = 0;
//				}
//			} else {
//				// 头部的汇率做变更
//				Ext.Ajax.request({
//		            method : Constants.POST,
//		            url : 'resource/getRegisterExchangeRate.action',
//		            success : function(result, request) {
//		        		nfExchageRate.setValue(result.responseText);
//		        		
//		        		for (var i = queryStore.getCount() - 1; i>=0; i--) {
//			    			// 明细部分的币别
//			    			queryStore.getAt(i).data.currencyId = cbxCurrencyId.getValue();
//			    			// 明细部分的汇率
//			    			queryStore.getAt(i).data.rate = nfExchageRate.getValue();
//						}
//		            },
//		            params : {
//		            	currencyId : cbxCurrencyId.getValue(),
//		            	supplier : supplier
//		        	}
//				});
//			}
			// 头部的汇率做变更
			Ext.Ajax.request({
	            method : Constants.POST,
	            url : 'resource/getRegisterExchangeRate.action',
	            params : {
	            	currencyId : cbxCurrencyId.getValue()
	        	},
	            success : function(result, request) {
	        		nfExchageRate.setValue(result.responseText);
	        		
	        		for (var i = queryStore.getCount() - 1; i>=0; i--) {
		    			// 明细部分的币别
		    			queryStore.getAt(i).data.currencyId = cbxCurrencyId.getValue();
		    			// 明细部分的汇率
		    			queryStore.getAt(i).data.rate = nfExchageRate.getValue();
					}
	            }
			});
		}
		
		this.sValue = this.getValue();
	}
	
    // 选择物料信息 modify by drdu 090730
//    function selectMaterial() {
//        var material = window.showModalDialog('../../../plan/register/selectMaterials.jsp', window,
//        	'dialogWidth=800px;dialogHeight=550px;status=no');
//        if (typeof(material) != "undefined") {
//            var record = gridOrder.getSelectionModel().getSelected();
//            
//            if (record.data.materialId != material.materialId) {
//	            // 清空相应单价
//	            record.set("quotedPrice", null);
//            }
//          
//            // 物料ID
//            record.set("materialId", material.materialId);
//            // 物料编码
//            record.set("materialNo", material.materialNo);
//            // 物料名称
//            record.set("materialName", material.materialName);
//            // 规格型号
//            record.set("specNo", material.specNo);
//            // 材质/参数
//            record.set("parameter", material.parameter);
//            // 是否免检
//            record.set("qaControlFlag", material.qaControlFlag);
//            // 计量单位
//            record.set("purUmId", material.purUmId);
//            // 最大库存量
//            record.set("maxStock", material.maxStock);
//        }
//        
//    	this.blur();
//    }
    
	// 上报计划单
	function reportOrder(argUpdate, argOrderId) {
	//--------------update by sychen 20100331--------------//
		Ext.Msg.confirm(Constants.SYS_REMIND_MSG, "是否已经打印？", function(buttonobj) {

            if (buttonobj == "yes") {
            	
		Ext.Msg.confirm(Constants.SYS_REMIND_MSG, "确认要发送吗？", function(buttonobj) {
        	if (!argOrderId) {
        		argOrderId = orderRegister.orderData.orderId;
        	}
            if (buttonobj == "yes") {
				// 上报计划单
		    	Ext.Ajax.request({
		            method : Constants.POST,
		            url : 'resource/reportRegisterOrder.action',
		            success : function(result, request) {
		            	orderRegister.isReported = true;
				        
		            	// 刷新采购单列表
		            	register.refreshList(argOrderId);
		            	Ext.Msg.alert(Constants.SYS_REMIND_MSG, "发送成功！");
		            },
		            params : {
		            	orderId : argOrderId
		        	}
		    	});
            } else if (argUpdate) {
            	// 刷新采购单列表
            	register.refreshList(argOrderId);
            }
		});
		//------------------------------------------//
            } else if (argUpdate) {
            	// 刷新采购单列表
            	register.refreshList(argOrderId);
            }
		});
	
	}
    
	// 打印计划单
	function printOrder() {
		if (orderRegister.checkChanged()) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_010);
		} else {
			// 如果画面中没有明细
	    	if (orderRegister.getDetailsData(false).length < 1) {
	    		Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_E_018);
	    		return false;
	    	}
	    	
			window.open("/power/"+"report/webfile/purchase.jsp?purNo=" + orderRegister.orderData.purNo+"&clientName="+tfSupplierName.getValue());
		}
	}
	
    // 初始化订单
    function initOrder() {
    	
		// 清空所有数据
		orderRegister.clear();
    	orderRegister.isEdit = false;
    	
    	formPanel.getForm().reset();
    	cbxCurrencyId.sValue = '';
    	//add by fyyang 090618--新增时默认为人民币
    	// 默认为人民币
    		cbxCurrencyId.setValue(2);
    		//
    			// 头部的汇率做变更
			Ext.Ajax.request({
	            method : Constants.POST,
	            url : 'resource/getRegisterExchangeRate.action',
	            params : {
	            	currencyId : cbxCurrencyId.getValue()
	        	},
	            success : function(result, request) {
	        		nfExchageRate.setValue(result.responseText);
	        		
	        		for (var i = queryStore.getCount() - 1; i>=0; i--) {
		    			// 明细部分的币别
		    			queryStore.getAt(i).data.currencyId = cbxCurrencyId.getValue();
		    			// 明细部分的汇率
		    			queryStore.getAt(i).data.rate = nfExchageRate.getValue();
					}
	            }
			});
			//----------------------------------------
    	if (orderRegister.currentUser) {
    		// 采购员设置为登陆用户
    		orderRegister.orderData = new OrderData();
    		orderRegister.orderData.buyer = orderRegister.currentUser.code;
    		orderRegister.orderData.buyerName = orderRegister.currentUser.name;
    		tfBuyer.setValue(orderRegister.currentUser.code);
    		tfBuyerName.setValue(orderRegister.currentUser.name);
			
			// 准备初始化对象
			startInited(1);
			
			// 重新加载Grid
			girdReload(false);
    	} else {
	    	// 取得当前用户
	    	Ext.Ajax.request({
	            method : Constants.POST,
	            url : 'resource/getCurrentWorker.action',
	            success : function(result, request) {
	            	if (result.responseText) {
	            		orderRegister.currentUser = eval('(' + result.responseText + ')');
	    				// 采购员设置为登陆用户
	            		orderRegister.orderData = new OrderData();
	            		orderRegister.orderData.buyer = orderRegister.currentUser.code;
	            		orderRegister.orderData.buyerName = orderRegister.currentUser.name;
	            		tfBuyer.setValue(orderRegister.currentUser.code);
    					tfBuyerName.setValue(orderRegister.currentUser.name);
	            		
						// 准备初始化对象
						startInited(1);
				    	
				    	// 重新加载Grid
				    	girdReload(false);
	            	}
	            }
	    	});
    	}
    }
    
    // 增加订单
    function addOrder() {
    	if (!orderRegister.checkChanged()) {
    		// 初始化订单
    		initOrder();
    	} else {
    		Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_004, function(buttonobj) {
                if (buttonobj == "yes") {
                	// 初始化订单
    				initOrder();
                }
    		});
    	}
    }
    
    // 删除订单
    function deleteOrder() {
    	if (!orderRegister.checkCurrentUser()) {
    		// 当前采购订单的采购人员为不是当前用户
    		Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.RB002_E_003);
    		return;
    	}
    	if (orderRegister.isReport) {
    		// 当前采购订单已经上报了
    		Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.RB002_E_004);
    		return;
    	}
    	
    	Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_002, function(buttonobj) {
	        if (buttonobj == "yes") {
	        	// 删除采购单
		    	Ext.Ajax.request({
		            method : Constants.POST,
		            url : 'resource/saveRegisterOrderData.action',
		            success : function(result, request) {
		            	if (result.responseText) {
		                	var msg = eval('(' + result.responseText + ')');
		                	if (msg.msg == 'U') {
		                		// 排他
		                		Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_E_015);
		                		return;
		                	}
		            	}
	                	Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_005);
	                	
	                	// 刷新采购单列表
	                	register.refreshList();
	                	// 初始化订单
    					initOrder();
		        	 },
		            failure : function() {
		                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
		            },
		            params : {
		            	saveInfo : Ext.encode(orderRegister.getDeleteData()),
		            	isDelete : true
		        	}
		        });
	        }
		});
    }
    //add by fyyang 091119-----------start----------------------
    	/**
	 * 检测物料明细是否有重复
	 */
	function isMaterialRepeat() {
		var msg = "";
		for (var i = 0; i < queryStore.getCount() - 1; i++) {
			for (var j = i + 1; j < queryStore.getCount() - 1; j++) {
				if (queryStore.getAt(i).get('materialId') == queryStore
						.getAt(j).get('materialId')
						&& queryStore.getAt(i).get('materialId') != ""
						&& queryStore.getAt(j).get('materialId') != "") {
					msg = Constants.RP002_E_002 + "<br />";
				}
			}
		}
		return msg;
	}
    //-------------------------------end------------------------
    
    // 生成PO check
    function checkCreatePO() {
    	var msg = '';
    	
    	// 如果汇率为空
    	if (!isNull(cbxCurrencyId.getValue()) && !nfExchageRate.getValue()) {
    		Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.RB002_E_005);
    		return false;
    	}
    	// 如果画面中没有明细
    	if (orderRegister.getDetailsData(false).length < 1) {
    		Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_E_018);
    		return false;
    	}
    	
    	// 头部的项目
    	if (isNull(hideSupplierId.getValue())) {
    		msg += String.format(Constants.COM_E_002, '供应商编号') + '<br/>';
    	}
    	if (!Ext.get('orderDueDate').dom.value) {
    		msg += String.format(Constants.COM_E_002, '预计到货时间') + '<br/>';
    	} else if (!compareDate(dfdueDate.getValue(), new Date())) {
    		msg += String.format(Constants.COM_E_004, '预计到货时间') + '<br/>';
    	}
    	if (isNull(nfTaxRate.getValue())) {
    		msg += String.format(Constants.COM_E_002, '税率') + '<br/>';
    	}
    	if (!cbxCurrencyId.getValue()) {
    		msg += String.format(Constants.COM_E_003, '人民币') + '<br/>';
    	}
    	
    	// 明细部分的项目
    	var detail = null;
    	var detailDueDateNull = false;
    	var detailPurQtyNull = false;
    	var detailMaterialNoNull = false;
    	var detailDueDateCheck = false;
    	var detailTaxRateNull = false;
    	var detailQuotedPriceNull = false;
    	//for (var i = queryStore.getCount() - 1; i >= 0; i--) { modify by fyyang 去掉最后的明显行
    	for (var i = queryStore.getCount() - 2; i >= 0; i--) {
    		detail = queryStore.getAt(i).data;
			
			if (!detail.orderDetailsDueDate) {
				detailDueDateNull = true;
			} else if (!compareDate(detail.orderDetailsDueDate, new Date())) {
	    		detailDueDateCheck = true;
	    	}
			if (isNull(detail.orderDetailsTaxRate)) {
				detailTaxRateNull = true;
			}
	
			if (detail.purQty==null||detail.purQty==0) {
				detailPurQtyNull = true;
			}
		
			if (isNull(detail.materialNo)) {
				detailMaterialNoNull = true;
			}
			if (isNull(detail.quotedPrice)) {
				detailQuotedPriceNull = true;
			}
			
			if (detailDueDateNull && detailDueDateCheck && detailPurQtyNull
				&& detailMaterialNoNull && detailTaxRateNull && detailQuotedPriceNull) {
				break;
			}
    	}
    	
    	if (msg.length > 0) {
    		msg = '头部的项目:<br/>' + msg;
    	}
    	var inValidFlag = false;
    	if (detailMaterialNoNull) {
    		if (!inValidFlag) {
    			inValidFlag = true;
    			if (msg.length > 0) {
	    			msg += '<br/>';
    			}
    			msg += '明细部分的项目:<br/>';
    		}
    		msg += String.format(Constants.COM_E_002, '物料编码') + '<br/>';
    	}
    	if (detailPurQtyNull) {
    		if (!inValidFlag) {
    			inValidFlag = true;
    			if (msg.length > 0) {
	    			msg += '<br/>';
    			}
    			msg += '明细部分的项目:<br/>';
    		}
    		msg += String.format(Constants.COM_E_002, '采购数量') + '<br/>';
    	}
    	if (detailDueDateNull) {
    		if (!inValidFlag) {
    			inValidFlag = true;
    			if (msg.length > 0) {
	    			msg += '<br/>';
    			}
    			msg += '明细部分的项目:<br/>';
    		}
    		msg += String.format(Constants.COM_E_002, '预计到货时间') + '<br/>';
    	}
    	if (detailDueDateCheck) {
    		if (!inValidFlag) {
    			inValidFlag = true;
    			if (msg.length > 0) {
	    			msg += '<br/>';
    			}
    			msg += '明细部分的项目:<br/>';
    		}
    		msg += String.format(Constants.COM_E_004, '预计到货时间') + '<br/>';
    	}
    	if (detailTaxRateNull) {
    		if (!inValidFlag) {
    			inValidFlag = true;
    			if (msg.length > 0) {
	    			msg += '<br/>';
    			}
    			msg += '明细部分的项目:<br/>';
    		}
    		msg += String.format(Constants.COM_E_002, '税率') + '<br/>';
    	}
    	if (detailQuotedPriceNull) {
    		if (!inValidFlag) {
    			inValidFlag = true;
    			if (msg.length > 0) {
	    			msg += '<br/>';
    			}
    			msg += '明细部分的项目:<br/>';
    		}
    		msg += String.format(Constants.COM_E_002, '单价') + '<br/>';
    	}
    	//add by fyyang 091119
    	msg += isMaterialRepeat();
    	
    	if (msg.length > 0) {
    		Ext.Msg.alert(Constants.SYS_REMIND_MSG, msg);
    		return false;
    	}
    	
    	if (!formPanel.getForm().isValid()) {
    		return false;
    	}
    	if (!checkNumLen(nfTaxRate.getValue(), 13, 4)) {
    		return false;
    	}
    	if (!checkNumLen(nfExchageRate.getValue(), 13, 4)) {
    		return false;
    	}
    	
    	return true;
    }
    
    // 更新画面上所有的数据
    function updateAllDatas(argCmd) {
    	if (orderRegister.isReported) {
    		Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.RB002_E_004);
    		return;
    	}
    	var saveInfo = Ext.encode(orderRegister.getSaveData());
    	// 保存采购单
    	Ext.Ajax.request({
            method : Constants.POST,
            url : 'resource/saveRegisterOrderData.action',
            success : function(result, request) {
            	if (result.responseText) {
                	var msg = eval('(' + result.responseText + ')');
                	if (msg.msg == 'U') {
                		// 排他
                		Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_E_015);
                		return;
                	}
            	}
                
            	var orderId = '';
            	if (argCmd == 'report') {
            		if (orderRegister.isEdit) {
	            		// 上报计划单
	            		reportOrder(true);
            		} else {
	            		// 上报计划单
	            		reportOrder(true, msg.orderId);
            		}
            		return;
            	} else if (orderRegister.isEdit) {
            		orderId = orderRegister.orderData.orderId;
		    		Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_004);
		    	} else {
		    		orderId = msg.orderId;
	        		Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(Constants.RB002_I_001, msg.purNo));
		    	}
        		
            	// 刷新采购单列表
            	register.refreshList(orderId);
        	 },
            failure : function() {
                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
            },
            params : {
            	saveInfo : saveInfo,
            	isEdit : orderRegister.isEdit
        	}
        });
    }
    
    function createPOTemp(argCmd) {
    	// 对必须输入的项目的确认
		if (!checkCreatePO()) {
			return;
		}
		
		var materialIds = [];
		var detailDatas = orderRegister.getDetailsData(true);
		for (var i = detailDatas.length - 1; i>=0; i--) {
			if (detailDatas[i].materialId) {
				materialIds.push(detailDatas[i].materialId);
			}
		}
		
		if (materialIds.length < 1) {
	    	// 更新画面上所有的数据
	    	updateAllDatas(argCmd);
		} else {
			// 得到物料的当前库存
			Ext.Ajax.request({
	            method : Constants.POST,
	            url : 'resource/getRegisterCurrentMatCounts.action',
	            success : function(result, request) {
	            	if (result.responseText) {
	                	var records = eval('(' + result.responseText + ')');
	                	records = [].concat(records);
	                	var msg = '';
	                	var detail = null;
	                	for (var i = records.length - 1; i>=0; i--) {
	                		for (var j = detailDatas.length - 1; j>=0; j--) {
	                			if (records[i].key == detailDatas[j].materialId) {
	                				detail = detailDatas[j];
	                				break;
	                			}
	                		}
	                		
	                		if (!detail.maxStock) {
	                			// 当物料主文件.最大库存 = 0或者空时，不做以下check
	                			continue;
	                		}
	                		// 若采购数量+当前库存 > 物料主文件.最大库存
	                		var invalidFlag = detail.purQty + records[i].value > detail.maxStock;
	                		if (invalidFlag) {
	                			msg += String.format(Constants.COM_W_001, detail.materialName, '采购') + '<br/>';
	                		}
	                	}
	                	
	                	if (msg.length > 0) {
	                		Ext.Msg.confirm(Constants.SYS_REMIND_MSG, msg, function(buttonobj1) {
						        if (buttonobj1 == "yes") {
							    	// 更新画面上所有的数据
							    	updateAllDatas(argCmd);
						        }
	                		});
			    			return;
	                	}
	            	}
	            	
			        // 更新画面上所有的数据
			    	updateAllDatas(argCmd);
	            },
	            params : {
	            	materialIds : materialIds.join(',')
	        	}
			});
		}
    }
    
    // 生成PO
    function createPO(argCmd) {
    	if (!orderRegister.checkChanged()) {
    		Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_006);
    		return;
    	}
    	
    	if (argCmd == 'report') {
    		createPOTemp(argCmd);
    		return;
    	}
    	Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_001, function(buttonobj) {
	        if (buttonobj == "yes") {
				createPOTemp();
	        }
    	});
		
    }
    
    // 增加订单明细
    function addOrderDetail() {

		if (!checkCreateByPlan()) {
			return;
		}
		//add by drdu 090730
		var material = window.showModalDialog(
				'../../../plan/register/selectMaterials.jsp', window,
				'dialogWidth=800px;dialogHeight=550px;status=no');
		if (typeof(material) != "undefined") {
			
			for (var i = 0; i < material.length; i++) {//add by drdu 090730

				var newDetailValue = new OrderDetailsData();
				// 交期: 头部的交期
				newDetailValue.orderDetailsDueDate = Ext.get('orderDueDate').dom.value;
				// 税率: 头部的税率
				newDetailValue.orderDetailsTaxRate = nfTaxRate.getValue();
				// 币别: 头部的币别
				newDetailValue.currencyId = cbxCurrencyId.getValue();
				// 汇率: 头部的汇率
				newDetailValue.rate = nfExchageRate.getValue();

				orderRegister.addDetail(newDetailValue);

				var tempDetail = new gridRecord(newDetailValue);
				//-==============add by drdu 090730======================
				tempDetail.set('materialName', material[i].data.materialName);
				tempDetail.set('materialNo', material[i].data.materialNo);
				tempDetail.set('materialId', material[i].data.materialId);
				tempDetail.set('specNo', material[i].data.specNo);
				tempDetail.set('parameter', material[i].data.parameter);
				tempDetail.set('qaControlFlag', material[i].data.qaControlFlag);
				tempDetail.set('purUmId', material[i].data.purUmId);
				tempDetail.set('maxStock', material[i].data.maxStock);
				// add by liuyi 091127 计量单位名称
				tempDetail.set('stockUmName', material[i].data.stockUmName);
				//======================================================
				gridOrder.stopEditing();
				var cnt = queryStore.getCount() - 1; // var cnt =
														// queryStore.getCount();
				queryStore.insert(cnt, tempDetail);
				gridOrder.getView().refresh();
				gridOrder.startEditing(cnt, 1);

				// 设置删除订单明细按钮可用
				Ext.getCmp('btnDetailDelete').setDisabled(false);
			}
		}
	}
	
    // 删除订单明细
    function deleteOrderDetail() {
    	var records = gridOrder.getSelectionModel().getSelections();
    
    	if (!records || records.length < 1) {
    		// 如果没有选择显示提示信息
    		Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
    		return;
    	}
    	
    	var record = records[0].data;
    	//------add by fyyang 090723 合计行不能删
    	if(record.orderDetailsDueDate==null||record.orderDetailsDueDate=="undefined")
    	{
    	  Ext.Msg.alert("提示","合计行不能删除！");	
    	  return ;
    	}
    	//------------------------------------------
    	orderRegister.deleteDetail(record);
    	// 重新加载Grid
    	girdReload(true);
    }
    
    // 按计划生成明细check
    function checkCreateByPlan() {
    	var msg = '';
    	if (!Ext.get('orderDueDate').dom.value) {
    		msg += String.format(Constants.COM_E_002, '预计到货时间') + '<br/>';
    	} else if (!compareDate(dfdueDate.getValue(), new Date())) {
    		msg += String.format(Constants.COM_E_004, '预计到货时间') + '<br/>';
    	}
    	if (isNull(nfTaxRate.getValue())) {
    		msg += String.format(Constants.COM_E_002, '税率') + '<br/>';
    	}
    	
    	if (msg.length > 0) {
    		Ext.Msg.alert(Constants.SYS_REMIND_MSG, msg);
    		return false;
    	}
    	return true;
    }
    
    // 按计划生成明细
    function createByPlan() {
    	if (orderRegister.isEdit && !register.orderData) {
    		return;
    	}
    	if (!checkCreateByPlan()) {
    		return;
    	}
    	
    	Ext.getCmp('btnDetailCreate').setDisabled(true);
		if (orderRegister.planLoaded) {
			// 弹出计划单拆分/合并对话框
    		popupMeasure();
		} else {			
	    	// 取得需求计划明细数据
	    	Ext.Ajax.request({
	            method : Constants.POST,
	            url : 'resource/getRegisterPlans.action',
	            success : function(result, request) {
	            	orderRegister.planLoaded = true;
	            	if (result.responseText) {
	                	var records = eval('(' + result.responseText + ')');
	                	records = [].concat(records);
	            		
	                	var planOrderDatas = orderRegister.planOrderDatas;
	                	var record = null;
	                	var plan = null;
	                	for (var intCnt = 0; intCnt < records.length; intCnt++) {
	                		record = records[intCnt];
	                		// 采购订单与需求计划关联表.申请单项次号
	                		if (!record.requirementDetailId) {
	                			continue;
	                		}
	                		
	                		plan = new PlanOrderData();
	                		plan.loadData(record);
	                		// 待采购数
	                		plan.needQty = (Number(plan.approvedQty)*1000 - Number(plan.planPurQty)*1000)/1000;
	                		// 上次修改时间
    						plan.planModifyDate = renderModifyDate(plan.planModifyDate);
	                		planOrderDatas.push(plan);
	                	}
	            	}
	            	
					// 准备初始化对象
					startInited(2);
	            },
	            failure : function() {
	                Ext.Msg.alert(Constants.ERROR, '加载数据失败!');
	            },
	            params : {
	            	buyer : tfBuyer.getValue(),
	            	supplier : hideSupplierId.getValue()
	        	}
	        });
		}
    }
    
    // 重新加载Grid
    function girdReload(argReloadFlag) {
    	var detailDatas = orderRegister.getDetailsData(argReloadFlag);
    	queryStore.proxy = new Ext.data.MemoryProxy({
    		list : detailDatas,
    		totalCount: detailDatas.length
    	});
    	gridOrder.getView().refresh();
    	
    	queryStore.reload();
    }
    
    function startInited(argNum) {
		// 开始初始化
		orderRegister.init(argNum);
		
    	orderRegister.initStep += argNum;
    	if ((orderRegister.initStep > 2 && orderRegister.initStep % 2 > 0)) {
    		orderRegister.init(orderRegister.initStep);
    		
    		// 弹出计划单拆分/合并对话框
    		popupMeasure();
    	}
    }
    
    // 弹出计划单拆分/合并对话框
    function popupMeasure() {
    	// 设置更新位置
    	orderRegister.setEndPoint();
    	
		var args = {};
		args.orderRegister = orderRegister;
		var object = window.showModalDialog('RB002_measure.jsp',
	            args, 'dialogWidth=1000px;dialogHeight=600px;center=yes;help=no;resizable=no;status=no;');
	    Ext.getCmp('btnDetailCreate').setDisabled(false);
	    if (typeof object == 'object') {
//	    	alert(Ext.util.JSON.encode(orderRegister.orderDetailsData['planed'][0]))
	    	//add by fyyang 090814--将供应商带过来
	    	if(orderRegister.orderDetailsData['planed'].length>0)
	    	{
	    			var selectSupplier=orderRegister.orderDetailsData['planed'][0].supplier;
	    			if(selectSupplier!=null&&selectSupplier!="")
	    			{
	    		var selectSupplyName=orderRegister.orderDetailsData['planed'][0].supplyName;
	    	
	    		var selectSupplierNo=orderRegister.orderDetailsData['planed'][0].supplierNo;
	         
	    		hideSupplierId.setValue(selectSupplier);
		    	tfSupplierNo.setValue(selectSupplierNo);
			tfSupplierName.setValue(selectSupplyName);
	    			}
	    	}
	    	//-------add end ---------------------
		    // 更新Grid
		    orderRegister.submit();
		    // 清空其它采购员的需求计划物资
		    orderRegister.clearOtherPlans();
	    } else {
	    	// 回滚
	    	
	    	orderRegister.rollback();
	    }
    }
    
    // 初始化画面
    function initPage(argOrderData, argRefresh) { 
    	if (!argOrderData) {  
    		// 初始化订单
    		initOrder();
    		return;
    	}
    	if (argRefresh || !orderRegister.checkChanged()) { 
    		doInitPage(argOrderData);
    	} else {   
    		Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_004, function(buttonobj) {
                if (buttonobj == "yes") {
                	doInitPage(argOrderData);
                }
    		});
    	}
    }
	    
    // 初始化画面
    function doInitPage(argOrderData) {
    	// 设置采购票Form中的值
    	formPanel.getForm().reset();
    	cbxCurrencyId.sValue = '';
    	formPanel.getForm().loadRecord({data: argOrderData});
    	var rendedDate = renderDate(argOrderData.orderDueDate);
    	dfdueDate.setValue(rendedDate);
    	
    	// 设置币别
    	var rC = cbxCurrencyId.findRecord(cbxCurrencyId.valueField, argOrderData.currencyId);
    	if (rC) {
    		cbxCurrencyId.setValue(argOrderData.currencyId);
    	} else {
    		Ext.Ajax.request({
	            method : Constants.POST,
	            url : 'resource/getRegisterCurrencyNameById.action',
	            params : {
	            	currencyId : argOrderData.currencyId
	        	},
	            success : function(result, request) {
	            	// 设置已经没有使用的币别
	            	Ext.form.ComboBox.superclass.setValue.call(cbxCurrencyId, result.responseText);
	            	cbxCurrencyId.value = argOrderData.currencyId;
	            	if (cbxCurrencyId.hiddenField) {
	            		cbxCurrencyId.hiddenField.value = argOrderData.currencyId;	
	            	}
	            }
			});
    	}
    	
		// 设置头部的汇率
		Ext.Ajax.request({
            method : Constants.POST,
            url : 'resource/getRegisterExchangeRate.action',
            params : {
            	currencyId : argOrderData.currencyId
        	},
            success : function(result, request) {
//            	if (result.responseText) {
        			// 如果有了最新的汇率
        			nfExchageRate.setValue(result.responseText);
//            	}
            }
		});
		
    	// 取得采购票明细数据
    	Ext.Ajax.request({
            method : Constants.POST,
            url : 'resource/getRegisterOrderDetailsByNo.action',
            success : function(result, request) {  
            	// 清空所有数据
	    		orderRegister.clear();
		    	// 采购票
		    	orderRegister.orderData = new OrderData();
		    	orderRegister.orderData.loadData(argOrderData);
		    	// 上次修改时间
		    	orderRegister.orderData.orderModifyDate = renderModifyDate(argOrderData.orderModifyDate);
		    	// 采购订单表.交期
		    	orderRegister.orderData.orderDueDate = rendedDate;
				// 采购票明细(计划)
				var planedDetails = orderRegister.orderDetailsData['planed'];
				// 采购票明细(非计划)
				var noplanedDetails = orderRegister.orderDetailsData['noplaned'];
				var detail = null;
				var record = null;
		    	
            	if (result.responseText) {
            		var records = eval('(' + result.responseText + ')');
            		records = [].concat(records.list);
					
					for (var intCnt = 0; intCnt < records.length; intCnt++) {
						record = records[intCnt];
						detail = new OrderDetailsData();
						detail.loadData(record);
						// 采购订单表.交期
		    			detail.orderDetailsDueDate = renderDate(detail.orderDetailsDueDate);
						// 上次修改时间
    					detail.orderDetailsModifyDate = renderModifyDate(detail.orderDetailsModifyDate);
    					
						// 是否为需求单的采购单明细
						if (detail.purOrderDetailsId) {
							detail.isPlanDetail = true;
							planedDetails.push(detail);
						} else {
							noplanedDetails.push(detail);
						}
					}
            	}
				
		    	// 取得维护的数据
		    	Ext.Ajax.request({
		            method : Constants.POST,
		            url : 'resource/getRegisterMeasureByPurNo.action',
		            success : function(result, request) {
		            	if (result.responseText) {
		                	records = eval('(' + result.responseText + ')');
		                	
		                	record = null;
		                	detail = null;
		                	var plan = null;
		                	if (!(records instanceof Array)) {
		                		records = [records];
		                	}
		                	for (var intCnt = 0; intCnt < records.length; intCnt++) {
		                		record = records[intCnt];
		                		// 采购订单与需求计划关联表.申请单项次号
		                		if (!record.requirementDetailId) {
		                			continue;
		                		}
		                		for (var i = 0; i < planedDetails.length; i++) {
		                			if (planedDetails[i].materialId == record.materialId) {
		                				detail = planedDetails[i];
		                				break;
		                			}
		                		}
		                		plan = new PlanOrderData();
		                		plan.loadData(record);
		                		// 上次修改时间
		    					plan.planRelateModifyDate = renderModifyDate(plan.planRelateModifyDate);
		                		// 上次修改时间
		    					plan.planModifyDate = renderModifyDate(plan.planModifyDate);
		                		// 合并对象
		                		detail.merge(plan);
		                	}
		            	}
		            	
				    	// 取得当前用户
				    	Ext.Ajax.request({
				            method : Constants.POST,
				            url : 'resource/getCurrentWorker.action',
				            success : function(result, request) {
				            	if (result.responseText) {
				            		orderRegister.currentUser = eval('(' + result.responseText + ')');
				            	}
						        
								// 准备初始化对象
								startInited(1);
								
								var detailDats = orderRegister.getDetailsData(false);
				            	queryStore.proxy = new Ext.data.MemoryProxy({
				            		list : detailDats,
				            		totalCount: detailDats.length
				            	});
				            	queryStore.load();
				            }
				    	});
		            },
		            failure : function() {
		                Ext.Msg.alert(Constants.ERROR, '加载数据失败!');
		            },
		            params : {
		            	purNo : tfPurNo.getValue(),
		            	supplier : hideSupplierId.getValue()
		        	}
		        });
	        },
            failure : function() {
                Ext.Msg.alert(Constants.ERROR, '加载数据失败!');
            },
            params : {
            	purNo : tfPurNo.getValue(),
            	supplier : hideSupplierId.getValue()
        	}
    	});
    }
    
    
    // 初始化画面
    buttonStatus.setInit();
    initPage(register.orderData, false);
    // ↑↑*********处理***********↑↑//
});
