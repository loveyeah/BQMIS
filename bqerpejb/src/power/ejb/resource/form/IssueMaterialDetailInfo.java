/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.resource.form;
/**
 * 出库管理，物料详细bean
 * @author qzhang
 */
@SuppressWarnings("serial")
public class IssueMaterialDetailInfo implements java.io.Serializable{
	/** 领料单明细.流水号*/
	private Long issueDetailsId;
	/** 物料id*/
	private Long materialId;
	/** 物料编码*/
	private String materialNo;
	/**物料名称*/
	private String materialName;
	/** 存货计量单位*/
	private Long stockUmId;
	/** 单位名称*/
	private String unitName;
	/** 需求数量*/
	private Double demandNum;
	/** 实际数量*/
	private Double actIssuedCount;
	/** 待发货数量*/
	private Double waitNum;
	/** 费用来源id*/
	//private Long itemId;
	// TODO 费用来源名称
	/** 领料单明细.需求计划明细ID*/
	private Long requirementDetailId;
	/** 批号*/
	private String lotNo;
	/** 仓库*/
	private String whsNo;
	/** 库位*/
	private String locationNo;
	/** 本次发货数量*/
	private Double thisNum;
	/** 上次修改时间*/
	private String lastModifiedDate;

	/** add by drdu  20090512*/
	
	/** 领料单号*/
	private String issueNo;
	/** 规格型号*/
	private String specNo;
	/** 单价*/
	private Double unitPrice;
	/** 总金额=单价*总数量*/
	private Double actPrice;
	/** 费用归口部门*/
	private String freeDeptName;
	/** 费用归口专业*/
	private String freeSpecialName;
	/** 出库人员*/
	private String lastModifyBy;
	/** 估计申请数量*/
	private Long appliedQty;
	/** 估计金额*/
	private Double estimatedPrice;
	/** 估计总金额 =估计申请数量*估计金额 */
	private Double actEstimatePrice;
	/** 备注*/
	private String memo;
	
	/**
	 * 费用来源编码
	 */
	private String itemCode;
	/**
	 * 红单处理单号
	 */
	private String refIssueNo;
	/** */
	
	/**
	 * 领料人 add by fyyang 091218
	 */
	private String getPerson;
	
	/**税额 add by drdu 20100408
	 */
	private Double taxCount;
	/**事务历史表ID add by drdu 20100408
	 */
	private Long transHisId;
	/**
	 * 获取
	 * @return lastModifiedDate
	 */
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	/**
	 * 设置
	 * @param lastModifiedDate
	 */
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}


	/**
	 * 获取单位名称
	 * @return unitName 单位名称
	 */
	public String getUnitName() {
		return unitName;
	}
	/**
	 * 设置单位名称
	 * @param unitName 单位名称
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	/**
	 * 获取  物料id
	 * @return materialId  物料id
	 */
	public Long getMaterialId() {
		return materialId;
	}
	/**
	 * 设置 物料id
	 * @param materialId  物料id
	 */
	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}

	/**
	 * 获取领料单明细.流水号
	 * @return issueDetailsId 领料单明细.流水号
	 */
	public Long getIssueDetailsId() {
		return issueDetailsId;
	}
	/**
	 * 设置领料单明细.流水号
	 * @param issueDetailsId 领料单明细.流水号
	 */
	public void setIssueDetailsId(Long issueDetailsId) {
		this.issueDetailsId = issueDetailsId;
	}
	/**
	 * 获取实际数量
	 * @return actIssuedCount 实际数量
	 */
	public Double getActIssuedCount() {
		return actIssuedCount;
	}
	/**
	 * 设置实际数量
	 * @param actIssuedCount 实际数量
	 */
	public void setActIssuedCount(Double actIssuedCount) {
		this.actIssuedCount = actIssuedCount;
	}
	/**
	 * 获取物料编码
	 * @return materialNo 物料编码
	 */
	public String getMaterialNo() {
		return materialNo;
	}
	/**
	 * 设置物料编码
	 * @param materialNo 物料编码
	 */
	public void setMaterialNo(String materialNo) {
		this.materialNo = materialNo;
	}
	/**
	 * 获取存货计量单位
	 * @return stockUmId 存货计量单位
	 */
	public Long getStockUmId() {
		return stockUmId;
	}
	/**
	 * 设置存货计量单位
	 * @param stockUmId 存货计量单位
	 */
	public void setStockUmId(Long stockUmId) {
		this.stockUmId = stockUmId;
	}
	/**
	 * 获取需求数量
	 * @return demandNum 需求数量
	 */
	public Double getDemandNum() {
		return demandNum;
	}
	/**
	 * 设置需求数量
	 * @param demandNum 需求数量
	 */
	public void setDemandNum(Double demandNum) {
		this.demandNum = demandNum;
	}

	/**
	 * 获取待发货数量
	 * @return waitNum 待发货数量
	 */
	public Double getWaitNum() {
		return waitNum;
	}
	/**
	 * 设置待发货数量
	 * @param waitNum 待发货数量
	 */
	public void setWaitNum(Double waitNum) {
		this.waitNum = waitNum;
	}

	/**
	 * 获取
	 * @return requirementDetailId
	 */
	public Long getRequirementDetailId() {
		return requirementDetailId;
	}
	/**
	 * 设置
	 * @param requirementDetailId
	 */
	public void setRequirementDetailId(Long requirementDetailId) {
		this.requirementDetailId = requirementDetailId;
	}
	/**
	 * 获取物料名称
	 * @return materialName 物料名称
	 */
	public String getMaterialName() {
		return materialName;
	}
	/**
	 * 设置物料名称
	 * @param materialName 物料名称
	 */
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	/**
	 * 获取批号
	 * @return lotNo 批号
	 */
	public String getLotNo() {
		return lotNo;
	}
	/**
	 * 设置批号
	 * @param lotNo 批号
	 */
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
	/**
	 * 获取仓库
	 * @return whsNo 仓库
	 */
	public String getWhsNo() {
		return whsNo;
	}
	/**
	 * 设置仓库
	 * @param whsNo 仓库
	 */
	public void setWhsNo(String whsNo) {
		this.whsNo = whsNo;
	}
	/**
	 * 获取库位
	 * @return locationNo 库位
	 */
	public String getLocationNo() {
		return locationNo;
	}
	/**
	 * 设置库位
	 * @param locationNo 库位
	 */
	public void setLocationNo(String locationNo) {
		this.locationNo = locationNo;
	}
	/**
	 * 获取本次发货数量
	 * @return thisNum本次发货数量
	 */
	public Double getThisNum() {
		return thisNum;
	}
	/**
	 * 设置本次发货数量
	 * @param thisNum 本次发货数量
	 */
	public void setThisNum(Double thisNum) {
		this.thisNum = thisNum;
	}
	
//	/**
//	 * 获取
//	 * @return itemId
//	 */
//	public Long getItemId() {
//		return itemId;
//	}
//	/**
//	 * 设置
//	 * @param itemId
//	 */
//	public void setItemId(Long itemId) {
//		this.itemId = itemId;
//	}
	public String getIssueNo() {
		return issueNo;
	}
	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}
	public String getSpecNo() {
		return specNo;
	}
	public void setSpecNo(String specNo) {
		this.specNo = specNo;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Double getActPrice() {
		return actPrice;
	}
	public void setActPrice(Double actPrice) {
		this.actPrice = actPrice;
	}
	public String getFreeDeptName() {
		return freeDeptName;
	}
	public void setFreeDeptName(String freeDeptName) {
		this.freeDeptName = freeDeptName;
	}
	public String getFreeSpecialName() {
		return freeSpecialName;
	}
	public void setFreeSpecialName(String freeSpecialName) {
		this.freeSpecialName = freeSpecialName;
	}
	public String getLastModifyBy() {
		return lastModifyBy;
	}
	public void setLastModifyBy(String lastModifyBy) {
		this.lastModifyBy = lastModifyBy;
	}
	public Long getAppliedQty() {
		return appliedQty;
	}
	public void setAppliedQty(Long appliedQty) {
		this.appliedQty = appliedQty;
	}
	public Double getEstimatedPrice() {
		return estimatedPrice;
	}
	public void setEstimatedPrice(Double estimatedPrice) {
		this.estimatedPrice = estimatedPrice;
	}
	public Double getActEstimatePrice() {
		return actEstimatePrice;
	}
	public void setActEstimatePrice(Double actEstimatePrice) {
		this.actEstimatePrice = actEstimatePrice;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getRefIssueNo() {
		return refIssueNo;
	}
	public void setRefIssueNo(String refIssueNo) {
		this.refIssueNo = refIssueNo;
	}
	public String getGetPerson() {
		return getPerson;
	}
	public void setGetPerson(String getPerson) {
		this.getPerson = getPerson;
	}
	public Double getTaxCount() {
		return taxCount;
	}
	public void setTaxCount(Double taxCount) {
		this.taxCount = taxCount;
	}
	public Long getTransHisId() {
		return transHisId;
	}
	public void setTransHisId(Long transHisId) {
		this.transHisId = transHisId;
	}

}
