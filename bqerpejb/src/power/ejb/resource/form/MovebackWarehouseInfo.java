package power.ejb.resource.form;

import java.io.Serializable;

/**
 * 退库管理bean
 * @author liugonglei
 */
public class MovebackWarehouseInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	/**领料单明细表*/
	/** 领料单明细ID*/
	private String issueDetailsId;
	/** 需求计划明细ID*/
	private String requirementDetailId;
	/** 领料单ID	  */
	private String issueHeadId;
	/** 物料ID    */
	private String materialId;
	/** 实际数量   */
	private String actIssuedCount;
	/** 操作日期   */
	private Long lastModifiedDate;
	/** 领料单表头  */
	/** 申请领料人  */
	private String  receiptBy;
	/** 领料单编号  */
	private String issueNo;
	/** 领用部门   */
	private String receiptDept;
	/** 费用归口部门*/
	private String  feeByDep;
	
	/** 物料主文件 */
	/** 编码      */
	private String  materialNo;
	/** 名称      */
	private String  materialName;
	/** 存货计量单位*/
	private String  stockUmId;
	/** 存货计量单位名称*/
	private String  stockUmName;
	
	/** 事务历史表  */
	/** 批号       */
	private String lotNo;
	/** 操作仓库   */
	private String fromWhsId;
	/** 操作库位   */
	private String fromLocationId;
	/** 仓库主文件  */
	/** 仓库名称   */
	private String whsName;
	/** 仓库主文件  */
	/** 库位名称   */
	private String locationName;
	/**
	 * @return the issueDetailsId
	 */
	public String getIssueDetailsId() {
		return issueDetailsId;
	}
	/**
	 * @param issueDetailsId the issueDetailsId to set
	 */
	public void setIssueDetailsId(String issueDetailsId) {
		this.issueDetailsId = issueDetailsId;
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
	/**
	 * @return the materialId
	 */
	public String getMaterialId() {
		return materialId;
	}
	/**
	 * @param materialID the materialId to set
	 */
	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}
	/**
	 * @return the actIssuedCount
	 */
	public String getActIssuedCount() {
		return actIssuedCount;
	}
	/**
	 * @param actIssuedCount the actIssuedCount to set
	 */
	public void setActIssuedCount(String actIssuedCount) {
		this.actIssuedCount = actIssuedCount;
	}
	/**
	 * @return the receiptBy
	 */
	public String getReceiptBy() {
		return receiptBy;
	}
	/**
	 * @param receiptBy the receiptBy to set
	 */
	public void setReceiptBy(String receiptBy) {
		this.receiptBy = receiptBy;
	}
	/**
	 * @return the receiveLine
	 */
	public String getIssueNo() {
		return issueNo;
	}
	/**
	 * @param receiveLine the receiveLine to set
	 */
	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}
	/**
	 * @return the receiveDept
	 */
	public String getReceiptDept() {
		return receiptDept;
	}
	/**
	 * @param receiveDept the receiveDept to set
	 */
	public void setReceiptDept(String receiptDept) {
		this.receiptDept = receiptDept;
	}
	/**
	 * @return the feeByDep
	 */
	public String getFeeByDep() {
		return feeByDep;
	}
	/**
	 * @param feeByDep the feeByDep to set
	 */
	public void setFeeByDep(String feeByDep) {
		this.feeByDep = feeByDep;
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
	 * @return the stockUmId
	 */
	public String getStockUmId() {
		return stockUmId;
	}
	/**
	 * @param stockUmId the stockUmId to set
	 */
	public void setStockUmId(String stockUmId) {
		this.stockUmId = stockUmId;
	}
	/**
	 * @return the lotNo
	 */
	public String getLotNo() {
		return lotNo;
	}
	/**
	 * @param lotNo the lotNo to set
	 */
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
	/**
	 * @return the fromWhsId
	 */
	public String getFromWhsId() {
		return fromWhsId;
	}
	/**
	 * @param fromWhsId the fromWhsId to set
	 */
	public void setFromWhsId(String fromWhsId) {
		this.fromWhsId = fromWhsId;
	}
	/**
	 * @return the fromLocationId
	 */
	public String getFromLocationId() {
		return fromLocationId;
	}
	/**
	 * @param fromLocationId the fromLocationId to set
	 */
	public void setFromLocationId(String fromLocationId) {
		this.fromLocationId = fromLocationId;
	}
	/**
	 * @return the whsName
	 */
	public String getWhsName() {
		return whsName;
	}
	/**
	 * @param whsName the whsName to set
	 */
	public void setWhsName(String whsName) {
		this.whsName = whsName;
	}
	/**
	 * @return the locationName
	 */
	public String getLocationName() {
		return locationName;
	}
	/**
	 * @param locationName the locationName to set
	 */
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	/**
	 * @return the serialVersionUID
	 */
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	/**
	 * @return the issueHeadId
	 */
	public String getIssueHeadId() {
		return issueHeadId;
	}
	/**
	 * @param issueHeadId the issueHeadId to set
	 */
	public void setIssueHeadId(String issueHeadId) {
		this.issueHeadId = issueHeadId;
	}
	/**
	 * @return the lastModifiedDate
	 */
	public Long getLastModifiedDate() {
		return lastModifiedDate;
	}
	/**
	 * @param lastModifiedDate the lastModifiedDate to set
	 */
	public void setLastModifiedDate(Long lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	/**
	 * @return the stockUmName
	 */
	public String getStockUmName() {
		return stockUmName;
	}
	/**
	 * @param stockUmName the stockUmName to set
	 */
	public void setStockUmName(String stockUmName) {
		this.stockUmName = stockUmName;
	}
}