/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.resource.form;


/**
 * 领料单登记Bean
 *
 * @author
 * @version 1.0
 */
public class IssueHeadRegisterInfo implements java.io.Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/** 领料单明细ID */
	private Long issueDetailsId;
	/** 申请数量 */
	private Double appliedCount;
	/** 核准数量 */
	private Double approvedCount;
	/** 实际数量 */
	private Double actIssuedCount;
	/** 待发货数量*/
	private Double waitCount;
	/** 成本分摊项目编码 */
	private Long costItemId;
	/** 费用来源ID */
	private String itemId;//modify by ywliu  2009/7/6
	/** 费用来源名称*/
	private String itemName;
	/** 物料需求明细id*/
	private Long requirementDetailId;
	/** 上次修改时间*/
	private String lastModifiedDate;
	// 物料主文件
	/** 流水号 */
	private Long materialId;
	/** 编码 */
	private String materialNo;
	/** 名称 */
	private String materialName;
	/** 规格型号 */
	private String specNo;
	/** 材质/参数 */
	private String parameter;
	/** 存货计量单位 */
	private Long stockUmId;
	/** 存货计量单位名称*/
	private String unitName;
	/** 库存数量*/
	private String stock;//add by ywliu 20091019
	
	private String modifyMemo;//add by fyyang 100112 核准信息
	
	private Double unitPrice;//add by fyyang 20100204 单价
	private Double price;//金额


	/**
	 * @return the stock
	 */
	public String getStock() {
		return stock;//add by ywliu 20091019
	}
	/**
	 * @param stock the stock to set
	 */
	public void setStock(String stock) {
		this.stock = stock;//add by ywliu 20091019
	}
	/**
	 * 获取待发货数量
	 * @return waitCount 待发货数量
	 */
	public Double getWaitCount() {
		return waitCount;
	}
	/**
	 * 设置待发货数量
	 * @param waitCount 待发货数量
	 */
	public void setWaitCount(Double waitCount) {
		this.waitCount = waitCount;
	}
	/**
	 * @return the issueDetailsId
	 */
	public Long getIssueDetailsId() {
		return issueDetailsId;
	}
	/**
	 * @param issueDetailsId the issueDetailsId to set
	 */
	public void setIssueDetailsId(Long issueDetailsId) {
		this.issueDetailsId = issueDetailsId;
	}
	/**
	 * @return the appliedCount
	 */
	public Double getAppliedCount() {
		return appliedCount;
	}
	/**
	 * @param appliedCount the appliedCount to set
	 */
	public void setAppliedCount(Double appliedCount) {
		this.appliedCount = appliedCount;
	}
	/**
	 * @return the approvedCount
	 */
	public Double getApprovedCount() {
		return approvedCount;
	}
	/**
	 * @param approvedCount the approvedCount to set
	 */
	public void setApprovedCount(Double approvedCount) {
		this.approvedCount = approvedCount;
	}
	/**
	 * @return the actIssuedCount
	 */
	public Double getActIssuedCount() {
		return actIssuedCount;
	}
	/**
	 * @param actIssuedCount the actIssuedCount to set
	 */
	public void setActIssuedCount(Double actIssuedCount) {
		this.actIssuedCount = actIssuedCount;
	}
	/**
	 * @return the costItemId
	 */
	public Long getCostItemId() {
		return costItemId;
	}
	/**
	 * @param costItemId the costItemId to set
	 */
	public void setCostItemId(Long costItemId) {
		this.costItemId = costItemId;
	}
	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}
	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
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
	 * @return the parameter
	 */
	public String getParameter() {
		return parameter;
	}
	/**
	 * @param parameter the parameter to set
	 */
	public void setParameter(String parameter) {
		this.parameter = parameter;
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
	 * 获取
	 * @return itemName
	 */
	public String getItemName() {
		return itemName;
	}
	/**
	 * 设置
	 * @param itemName
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	/**
	 * 获取
	 * @return unitName
	 */
	public String getUnitName() {
		return unitName;
	}
	/**
	 * 设置
	 * @param unitName
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
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
	public String getModifyMemo() {
		return modifyMemo;
	}
	public void setModifyMemo(String modifyMemo) {
		this.modifyMemo = modifyMemo;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
}
