package power.ejb.resource.form;

import java.util.Date;

public class MRPGatherDetailInfo {
	// 流水号
	private String materialId;
	// 物料编码
	private String materialNo;
	// 物料名称
	private String materialName;
	// 物料类别（名称）
	private String className;
	// 规格型号
	private String specNo;
	// 材质/参数
	private String parameter;
	// 存货计量单位STOCK_UM_ID
	private String stockUmName;
	// 最大货存
	private Double maxStock;
	// 核实数量
	private Double approvedQty;
	// 已收数量
	private Double issQty;
	// 是否免检
	private String qaControlFlag;
	// 需求计划明细ID
	private String requirementDetailId;
	// 汇总id
	private String gatherId;

	// 询价状态
	private String isEnquire;

	// add by fyyang 090703
	/**
	 * 申请人
	 */
	private String applyByName;
	/**
	 * 申请部门
	 */
	private String applyDeptName;

	/**
	 * 申请理由
	 */
	private String applyReason;

	/**
	 * 计划来源ID
	 */
	private String planOriginalId;

	/**
	 * 申请日期
	 */
	private String mrDate;

	/**
	 * add by fyyang 090807 作废原因
	 */
	private String cancelReason;

	/**
	 * add by ywliu 091030 需求日期
	 */
	private String dueDate;

	/**
	 * add by ywliu 091030 建议供应商
	 */
	private String supplier;

	/**
	 * add by ywliu 091030 备注信息
	 */
	private String memo;

	/**
	 * add by ywliu 091030 填写人
	 */
	private String entryBy;

	/**
	 * buyer add by bjxu091104 采购员
	 */
	private String buyer;

	// add by liuyi 091109
	/**
	 * 是否退回
	 */
	private String isReturn;
	/**
	 * 退回原因
	 */
	private String returnReason;

	// add by ywliu 091111
	/**
	 * 分配计划时间
	 */
	private String gatherTime;

	// 建议生产厂家 add by bjxu 091214
	private String factory;
	
	//估计单价 add by ltong 100427
	private double estimatedPrice;

	/**
	 * @return the estimatedPrice
	 */
	public double getEstimatedPrice() {
		return estimatedPrice;
	}

	/**
	 * @param estimatedPrice the estimatedPrice to set
	 */
	public void setEstimatedPrice(double estimatedPrice) {
		this.estimatedPrice = estimatedPrice;
	}

	public String getIsReturn() {
		return isReturn;
	}

	public void setIsReturn(String isReturn) {
		this.isReturn = isReturn;
	}

	public String getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public String getMaterialId() {
		return materialId;
	}

	/**
	 * @param materialId
	 *            the materialId to set
	 */
	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}

	/**
	 * @return the materialNo
	 */
	public String getMaterialNo() {
		return materialNo;
	}

	/**
	 * @param materialNo
	 *            the materialNo to set
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
	 * @param materialName
	 *            the materialName to set
	 */
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className
	 *            the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return the specNo
	 */
	public String getSpecNo() {
		return specNo;
	}

	/**
	 * @param specNo
	 *            the specNo to set
	 */
	public void setSpecNo(String specNo) {
		this.specNo = specNo;
	}

	/**
	 * @return the parameter
	 */
	public String getParameter() {
		return parameter;
	}

	/**
	 * @param parameter
	 *            the parameter to set
	 */
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	/**
	 * @return the stockUmName
	 */
	public String getStockUmName() {
		return stockUmName;
	}

	/**
	 * @param stockUmName
	 *            the stockUmName to set
	 */
	public void setStockUmName(String stockUmName) {
		this.stockUmName = stockUmName;
	}

	/**
	 * @return the maxStock
	 */
	public Double getMaxStock() {
		return maxStock;
	}

	/**
	 * @param maxStock
	 *            the maxStock to set
	 */
	public void setMaxStock(Double maxStock) {
		this.maxStock = maxStock;
	}

	/**
	 * @return the approvedQty
	 */
	public Double getApprovedQty() {
		return approvedQty;
	}

	/**
	 * @param approvedQty
	 *            the approvedQty to set
	 */
	public void setApprovedQty(Double approvedQty) {
		this.approvedQty = approvedQty;
	}

	/**
	 * @return the issQty
	 */
	public Double getIssQty() {
		return issQty;
	}

	/**
	 * @param issQty
	 *            the issQty to set
	 */
	public void setIssQty(Double issQty) {
		this.issQty = issQty;
	}

	/**
	 * @return the qaControlFlag
	 */
	public String getQaControlFlag() {
		return qaControlFlag;
	}

	/**
	 * @param qaControlFlag
	 *            the qaControlFlag to set
	 */
	public void setQaControlFlag(String qaControlFlag) {
		this.qaControlFlag = qaControlFlag;
	}

	/**
	 * @return the requirementDetailId
	 */
	public String getRequirementDetailId() {
		return requirementDetailId;
	}

	/**
	 * @param requirementDetailId
	 *            the requirementDetailId to set
	 */
	public void setRequirementDetailId(String requirementDetailId) {
		this.requirementDetailId = requirementDetailId;
	}

	public String getGatherId() {
		return gatherId;
	}

	public void setGatherId(String gatherId) {
		this.gatherId = gatherId;
	}

	public String getIsEnquire() {
		return isEnquire;
	}

	public void setIsEnquire(String isEnquire) {
		this.isEnquire = isEnquire;
	}

	public String getApplyByName() {
		return applyByName;
	}

	public void setApplyByName(String applyByName) {
		this.applyByName = applyByName;
	}

	public String getApplyDeptName() {
		return applyDeptName;
	}

	public void setApplyDeptName(String applyDeptName) {
		this.applyDeptName = applyDeptName;
	}

	public String getApplyReason() {
		return applyReason;
	}

	public void setApplyReason(String applyReason) {
		this.applyReason = applyReason;
	}

	/**
	 * @return the planOriginalId
	 */
	public String getPlanOriginalId() {
		return planOriginalId;
	}

	/**
	 * @param planOriginalId
	 *            the planOriginalId to set
	 */
	public void setPlanOriginalId(String planOriginalId) {
		this.planOriginalId = planOriginalId;
	}

	/**
	 * @return the mrDate
	 */
	public String getMrDate() {
		return mrDate;
	}

	/**
	 * @param mrDate
	 *            the mrDate to set
	 */
	public void setMrDate(String mrDate) {
		this.mrDate = mrDate;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	/**
	 * @return the dueDate
	 */
	public String getDueDate() {
		return dueDate;
	}

	/**
	 * @param dueDate
	 *            the dueDate to set
	 */
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * @return the supplier
	 */
	public String getSupplier() {
		return supplier;
	}

	/**
	 * @param supplier
	 *            the supplier to set
	 */
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @param memo
	 *            the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * @return the entryBy
	 */
	public String getEntryBy() {
		return entryBy;
	}

	/**
	 * @param entryBy
	 *            the entryBy to set
	 */
	public void setEntryBy(String entryBy) {
		this.entryBy = entryBy;
	}

	/**
	 * @return the gatherTime
	 */
	public String getGatherTime() {
		return gatherTime;
	}

	/**
	 * @param gatherTime
	 *            the gatherTime to set
	 */
	public void setGatherTime(String gatherTime) {
		this.gatherTime = gatherTime;
	}

	public String getFactory() {
		return factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}



}
