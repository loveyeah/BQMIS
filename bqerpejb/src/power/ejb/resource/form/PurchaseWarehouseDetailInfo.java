package power.ejb.resource.form;

public class PurchaseWarehouseDetailInfo {
	/** 到货登记/验收表.流水号 */
	private Long id;
	/** 到货登记/验收表.单据编号 */
	private String arrivalNo;
	/** 到货登记/验收明细表.备注 */
	private String memo;
	/** 到货登记/验收明细表.批次 */
	private String lotCode;
	/** 到货登记/验收明细表.已收数量 */
	private Double rcvQty;
	/** 到货登记/验收明细表.操作人 */
	private String operateBy;
	/** 物料主文件.流水号 */
	private Long materialId;
	/** 物料主文件.编码 */
	private String materialNo;
	/** 物料主文件.名称 */
	private String materialName;
	/** 物料主文件.是否批控制 */
	private String isLot;
	/** 物料主文件.物料分类 */
	private Long materialClassId;
	/** 物料主文件.计价方式 */
	private String costMethod;
	/** 物料主文件.标准成本 */
	private Double stdCost;
	/** 物料主文件.缺省仓库编码 */
	private String defaultWhsNo;
	/** 物料主文件.缺省库位编码 */
	private String defaultLocationNo;
	/** 物料主文件.规格型号 */
	private String specNo;
	/** 物料主文件.存货计量单位 */
	private Long stockUmId;
	/** 物料主文件.计划价格 */
	private Double frozenCost;
	/** 采购订单明细表.流水号 */
	private Long purOrderDetailsId;
	/** 采购订单明细表.采购数量 */
	private Double purQty;
	/** 采购订单明细表.已收数 */
	private Double purOrderDetailsRcvQty;
	/** 待入库数 */
	private Double waitQty;
	/** 本次入库数 */
	private Double thisQty;
	/** 采购订单明细表.单价 */
	private Double unitPrice;
	/** 采购订单明细表.交期 */
	private String dueDate;
	/** 采购订单明细表.税率 */
	private String taxRate;
	/** 采购订单明细表.币别 */
	private String currencyType;
	/** 采购订单明细表.生产厂家 */
	private String factory;
	/** 保管员 */
	private String saveName;
	/** 仓库编码 */
	private String whsNo;
	/** 仓位编码 */
	private String locationNo;
	/** 备注 */	
	private String gridMemo;
	/** 暂收数量 */
	private Double insqty;
	/** 到货登记/验收明细表.操作人 */
	private String operateByName;
	/** 物料主文件.存货计量单位 */
	private String stockUmIdName;
	/** 到货登记/验收明细表.流水号 */
	private Long arrivalDetailID;
	/** 到货登记/验收明细表.已入库数量 */
	private Double recQty;
	/** 到货登记/验收明细表.上次修改时间 */
	private String arrivalDetailModifiedDate;
	/** 采购订单明细表.上次修改时间 */
	private String orderDetailModifiedDate;
	/** 物料主文件.上次修改时间 */
	private String materialModifiedDate;
	/** 可以入库数 */
	private Double canQty;
	//----add 090422
	/** 到货登记/验收明细表.物料状态*/
	private String itemStatus;
	//add 090506 
	/** 供应商表供应商编号 */
	private String supplier;
	/** 供应商表供应商全称 */
	private String supplyName;
	/** 币别ID*/
	private Long currencyId;
	/** 采购人员*/
	private String buyerName;
	/** 合同编号*/
	private String contractNo; 
	/** 发票编号*/
	private String invoiceNo; 
	/** 金额*/
	private Double estimatedSum;
	/** 仓库名称*/
	private String whsName;
	// add ywliu 090512
	private String className;
	/** 到货登记/验收表订单编号 */
	private String purNo;
	/** 填写日期 */
	private String entryDate;
	/** 入库日期 */
	private String arrivalDate;
	/** 红单总数量 add by fyyang 090804 */
	private Double sumRedQty;
	/** 需求计划单明细 add by ywliu 091029 */
	private String requirementDetailId;
	/** 单价 add by liuyi 091126*/
	private Double unitCost;
	/** 金额 add by liuyi 091126*/
	private Double accoutPrice;
	//add by fyyang 091218 for report
	/** 单价 */
	private String strUnitPrice;
	/**
	 * 进项税额
	 */
	private String strInputTax;
	/**
	 * 总额
	 */
	private String strTotalPrice;
	/**
	 * 价税总额
	 */
	private String strTotalTax;
	
	/**
	 * 到货单关联 add by ywliu 20100129
	 */
	private String relationArrivalNo;
	
	/**
	 * 采购单编号 add by liuyi 20100406
	 */
	private String cGPurNo;
	/**
	 * 需求备注 add by liuyi 20100406
	 */
	private String sbMemo;
	
	/**
	 * 申报部门 add by liuyi 20100406
	 */
	private String sbDeptName;
	
	/** 税额  add by drdu 20100408 */
	private Double taxCount;
	
	/**  需求计划来源 add by liuyi 20100430 */
	private String planOriginalId;

	/**
	 * @return the planOriginalId
	 */
	public String getPlanOriginalId() {
		return planOriginalId;
	}
	/**
	 * @param planOriginalId the planOriginalId to set
	 */
	public void setPlanOriginalId(String planOriginalId) {
		this.planOriginalId = planOriginalId;
	}
	public Double getUnitCost() {
		return unitCost;
	}
	public void setUnitCost(Double unitCost) {
		this.unitCost = unitCost;
	}
	public Double getAccoutPrice() {
		return accoutPrice;
	}
	public void setAccoutPrice(Double accoutPrice) {
		this.accoutPrice = accoutPrice;
	}
	/**
	 * @return the arrivalDate
	 */
	public String getArrivalDate() {
		return arrivalDate;
	}
	/**
	 * @param arrivalDate the arrivalDate to set
	 */
	public void setArrivalDate(String arrivalDate) {
		this.arrivalDate = arrivalDate;
	}
	/**
	 * @return the entryDate
	 */
	public String getEntryDate() {
		return entryDate;
	}
	/**
	 * @param entryDate the entryDate to set
	 */
	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}
	/**
	 * @return the purNo
	 */
	public String getPurNo() {
		return purNo;
	}
	/**
	 * @param purNo the purNo to set
	 */
	public void setPurNo(String purNo) {
		this.purNo = purNo;
	}
	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	public String getArrivalDetailModifiedDate() {
		return arrivalDetailModifiedDate;
	}
	/**
	 * @param arrivalDetailModifiedDate the arrivalDetailModifiedDate to set
	 */
	public void setArrivalDetailModifiedDate(String arrivalDetailModifiedDate) {
		this.arrivalDetailModifiedDate = arrivalDetailModifiedDate;
	}
	/**
	 * @return the orderDetailModifiedDate
	 */
	public String getOrderDetailModifiedDate() {
		return orderDetailModifiedDate;
	}
	/**
	 * @param orderDetailModifiedDate the orderDetailModifiedDate to set
	 */
	public void setOrderDetailModifiedDate(String orderDetailModifiedDate) {
		this.orderDetailModifiedDate = orderDetailModifiedDate;
	}
	/**
	 * @return the materialModifiedDate
	 */
	public String getMaterialModifiedDate() {
		return materialModifiedDate;
	}
	/**
	 * @param materialModifiedDate the materialModifiedDate to set
	 */
	public void setMaterialModifiedDate(String materialModifiedDate) {
		this.materialModifiedDate = materialModifiedDate;
	}
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the arrivalNo
	 */
	public String getArrivalNo() {
		return arrivalNo;
	}
	/**
	 * @param arrivalNo the arrivalNo to set
	 */
	public void setArrivalNo(String arrivalNo) {
		this.arrivalNo = arrivalNo;
	}
	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}
	/**
	 * @param memo the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}
	/**
	 * @return the lotCode
	 */
	public String getLotCode() {
		return lotCode;
	}
	/**
	 * @param lotCode the lotCode to set
	 */
	public void setLotCode(String lotCode) {
		this.lotCode = lotCode;
	}
	/**
	 * @return the rcvQty
	 */
	public Double getRcvQty() {
		return rcvQty;
	}
	/**
	 * @param rcvQty the rcvQty to set
	 */
	public void setRcvQty(Double rcvQty) {
		this.rcvQty = rcvQty;
	}
	/**
	 * @return the operateBy
	 */
	public String getOperateBy() {
		return operateBy;
	}
	/**
	 * @param operateBy the operateBy to set
	 */
	public void setOperateBy(String operateBy) {
		this.operateBy = operateBy;
	}
	/**
	 * @return the materialId
	 */
	public Long getMaterialId() {
		return materialId;
	}
	/**
	 * @param materialId the materialId to set
	 */
	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}
	/**
	 * @return the materialNo
	 */
	public String getMaterialNo() {
		return materialNo;
	}
	/**
	 * @param materialNo the materialNo to set
	 */
	public void setMaterialNo(String materialNo) {
		this.materialNo = materialNo;
	}
	/**
	 * @return the materialName
	 */
	public String getMaterialName() {
		return materialName;
	}
	/**
	 * @param materialName the materialName to set
	 */
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	/**
	 * @return the isLot
	 */
	public String getIsLot() {
		return isLot;
	}
	/**
	 * @param isLot the isLot to set
	 */
	public void setIsLot(String isLot) {
		this.isLot = isLot;
	}
	/**
	 * @return the materialClassId
	 */
	public Long getMaterialClassId() {
		return materialClassId;
	}
	/**
	 * @param materialClassId the materialClassId to set
	 */
	public void setMaterialClassId(Long materialClassId) {
		this.materialClassId = materialClassId;
	}
	/**
	 * @return the costMethod
	 */
	public String getCostMethod() {
		return costMethod;
	}
	/**
	 * @param costMethod the costMethod to set
	 */
	public void setCostMethod(String costMethod) {
		this.costMethod = costMethod;
	}
	/**
	 * @return the stdCost
	 */
	public Double getStdCost() {
		return stdCost;
	}
	/**
	 * @param stdCost the stdCost to set
	 */
	public void setStdCost(Double stdCost) {
		this.stdCost = stdCost;
	}
	/**
	 * @return the defaultWhsNo
	 */
	public String getDefaultWhsNo() {
		return defaultWhsNo;
	}
	/**
	 * @param defaultWhsNo the defaultWhsNo to set
	 */
	public void setDefaultWhsNo(String defaultWhsNo) {
		this.defaultWhsNo = defaultWhsNo;
	}
	/**
	 * @return the defaultLocationNo
	 */
	public String getDefaultLocationNo() {
		return defaultLocationNo;
	}
	/**
	 * @param defaultLocationNo the defaultLocationNo to set
	 */
	public void setDefaultLocationNo(String defaultLocationNo) {
		this.defaultLocationNo = defaultLocationNo;
	}
	/**
	 * @return the specNo
	 */
	public String getSpecNo() {
		return specNo;
	}
	/**
	 * @param specNo the specNo to set
	 */
	public void setSpecNo(String specNo) {
		this.specNo = specNo;
	}
	/**
	 * @return the stockUmId
	 */
	public Long getStockUmId() {
		return stockUmId;
	}
	/**
	 * @param stockUmId the stockUmId to set
	 */
	public void setStockUmId(Long stockUmId) {
		this.stockUmId = stockUmId;
	}
	/**
	 * @return the frozenCost
	 */
	public Double getFrozenCost() {
		return frozenCost;
	}
	/**
	 * @param frozenCost the frozenCost to set
	 */
	public void setFrozenCost(Double frozenCost) {
		this.frozenCost = frozenCost;
	}
	/**
	 * @return the purOrderDetailsId
	 */
	public Long getPurOrderDetailsId() {
		return purOrderDetailsId;
	}
	/**
	 * @param purOrderDetailsId the purOrderDetailsId to set
	 */
	public void setPurOrderDetailsId(Long purOrderDetailsId) {
		this.purOrderDetailsId = purOrderDetailsId;
	}
	/**
	 * @return the purQty
	 */
	public Double getPurQty() {
		return purQty;
	}
	/**
	 * @param purQty the purQty to set
	 */
	public void setPurQty(Double purQty) {
		this.purQty = purQty;
	}
	/**
	 * @return the purOrderDetailsRcvQty
	 */
	public Double getPurOrderDetailsRcvQty() {
		return purOrderDetailsRcvQty;
	}
	/**
	 * @param purOrderDetailsRcvQty the purOrderDetailsRcvQty to set
	 */
	public void setPurOrderDetailsRcvQty(Double purOrderDetailsRcvQty) {
		this.purOrderDetailsRcvQty = purOrderDetailsRcvQty;
	}
	/**
	 * @return the waitQty
	 */
	public Double getWaitQty() {
		return waitQty;
	}
	/**
	 * @param waitQty the waitQty to set
	 */
	public void setWaitQty(Double waitQty) {
		this.waitQty = waitQty;
	}
	/**
	 * @return the thisQty
	 */
	public Double getThisQty() {
		return thisQty;
	}
	/**
	 * @param thisQty the thisQty to set
	 */
	public void setThisQty(Double thisQty) {
		this.thisQty = thisQty;
	}
	/**
	 * @return the unitPrice
	 */
	public Double getUnitPrice() {
		return unitPrice;
	}
	/**
	 * @param unitPrice the unitPrice to set
	 */
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	/**
	 * @return the dueDate
	 */
	public String getDueDate() {
		return dueDate;
	}
	/**
	 * @param dueDate the dueDate to set
	 */
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	/**
	 * @return the taxRate
	 */
	public String getTaxRate() {
		return taxRate;
	}
	/**
	 * @param taxRate the taxRate to set
	 */
	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}
	/**
	 * @return the currencyType
	 */
	public String getCurrencyType() {
		return currencyType;
	}
	/**
	 * @param currencyType the currencyType to set
	 */
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	/**
	 * @return the factory
	 */
	public String getFactory() {
		return factory;
	}
	/**
	 * @param factory the factory to set
	 */
	public void setFactory(String factory) {
		this.factory = factory;
	}
	/**
	 * @return the saveName
	 */
	public String getSaveName() {
		return saveName;
	}
	/**
	 * @param saveName the saveName to set
	 */
	public void setSaveName(String saveName) {
		this.saveName = saveName;
	}
	/**
	 * @return the whsNo
	 */
	public String getWhsNo() {
		return whsNo;
	}
	/**
	 * @param whsNo the whsNo to set
	 */
	public void setWhsNo(String whsNo) {
		this.whsNo = whsNo;
	}
	/**
	 * @return the locationNo
	 */
	public String getLocationNo() {
		return locationNo;
	}
	/**
	 * @param locationNo the locationNo to set
	 */
	public void setLocationNo(String locationNo) {
		this.locationNo = locationNo;
	}
	/**
	 * @return the gridMemo
	 */
	public String getGridMemo() {
		return gridMemo;
	}
	/**
	 * @param gridMemo the gridMemo to set
	 */
	public void setGridMemo(String gridMemo) {
		this.gridMemo = gridMemo;
	}
	/**
	 * @return the insqty
	 */
	public Double getInsqty() {
		return insqty;
	}
	/**
	 * @param insqty the insqty to set
	 */
	public void setInsqty(Double insqty) {
		this.insqty = insqty;
	}
	/**
	 * @return the operateByName
	 */
	public String getOperateByName() {
		return operateByName;
	}
	/**
	 * @param operateByName the operateByName to set
	 */
	public void setOperateByName(String operateByName) {
		this.operateByName = operateByName;
	}
	/**
	 * @return the stockUmIdName
	 */
	public String getStockUmIdName() {
		return stockUmIdName;
	}
	/**
	 * @param stockUmIdName the stockUmIdName to set
	 */
	public void setStockUmIdName(String stockUmIdName) {
		this.stockUmIdName = stockUmIdName;
	}
	/**
	 * @return the arrivalDetailID
	 */
	public Long getArrivalDetailID() {
		return arrivalDetailID;
	}
	/**
	 * @param arrivalDetailID the arrivalDetailID to set
	 */
	public void setArrivalDetailID(Long arrivalDetailID) {
		this.arrivalDetailID = arrivalDetailID;
	}
	/**
	 * @return the recQty
	 */
	public Double getRecQty() {
		return recQty;
	}
	/**
	 * @param recQty the recQty to set
	 */
	public void setRecQty(Double recQty) {
		this.recQty = recQty;
	}
	/**
	 * @return the canQty
	 */
	public Double getCanQty() {
		return canQty;
	}
	/**
	 * @param canQty the canQty to set
	 */
	public void setCanQty(Double canQty) {
		this.canQty = canQty;
	}
	public String getItemStatus() {
		return itemStatus;
	}
	public void setItemStatus(String itemStatus) {
		this.itemStatus = itemStatus;
	}
	public String getBuyerName() {
		return buyerName;
	}
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	public String getSupplyName() {
		return supplyName;
	}
	public void setSupplyName(String supplyName) {
		this.supplyName = supplyName;
	}
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public Long getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(Long currencyId) {
		this.currencyId = currencyId;
	}
	public String getContractNo() {
		return contractNo;
	}
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public Double getEstimatedSum() {
		return estimatedSum;
	}
	public void setEstimatedSum(Double estimatedSum) {
		this.estimatedSum = estimatedSum;
	}
	public String getWhsName() {
		return whsName;
	}
	public void setWhsName(String whsName) {
		this.whsName = whsName;
	}
	public Double getSumRedQty() {
		return sumRedQty;
	}
	public void setSumRedQty(Double sumRedQty) {
		this.sumRedQty = sumRedQty;
	}
	/**
	 * @return the requirementDetailId
	 */
	public String getRequirementDetailId() {
		return requirementDetailId;
	}
	/**
	 * @param requirementDetailId the requirementDetailId to set
	 */
	public void setRequirementDetailId(String requirementDetailId) {
		this.requirementDetailId = requirementDetailId;
	}
	public String getStrUnitPrice() {
		return strUnitPrice;
	}
	public void setStrUnitPrice(String strUnitPrice) {
		this.strUnitPrice = strUnitPrice;
	}
	public String getStrInputTax() {
		return strInputTax;
	}
	public void setStrInputTax(String strInputTax) {
		this.strInputTax = strInputTax;
	}
	public String getStrTotalPrice() {
		return strTotalPrice;
	}
	public void setStrTotalPrice(String strTotalPrice) {
		this.strTotalPrice = strTotalPrice;
	}
	public String getStrTotalTax() {
		return strTotalTax;
	}
	public void setStrTotalTax(String strTotalTax) {
		this.strTotalTax = strTotalTax;
	}
	/**
	 * @return the relationArrivalNo
	 */
	public String getRelationArrivalNo() {
		return relationArrivalNo;
	}
	/**
	 * @param relationArrivalNo the relationArrivalNo to set
	 */
	public void setRelationArrivalNo(String relationArrivalNo) {
		this.relationArrivalNo = relationArrivalNo;
	}
	public String getCGPurNo() {
		return cGPurNo;
	}
	public void setCGPurNo(String purNo) {
		cGPurNo = purNo;
	}
	public String getSbMemo() {
		return sbMemo;
	}
	public void setSbMemo(String sbMemo) {
		this.sbMemo = sbMemo;
	}
	public String getSbDeptName() {
		return sbDeptName;
	}
	public void setSbDeptName(String sbDeptName) {
		this.sbDeptName = sbDeptName;
	}
	public Double getTaxCount() {
		return taxCount;
	}
	public void setTaxCount(Double taxCount) {
		this.taxCount = taxCount;
	}
	


	
}
