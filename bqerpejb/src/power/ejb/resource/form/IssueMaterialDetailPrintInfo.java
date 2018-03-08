package power.ejb.resource.form;

public class IssueMaterialDetailPrintInfo {
	
	/** 物料编码*/
	private String materialNo;
	/**物料名称*/
	private String materialName;
	/** 单位名称*/
	private String unitName;
	/** 需求数量*/
	private Double demandNum;
	/** 实际数量*/
	private String actIssuedCount;
	/** 仓库*/
	private String whsNo;
	/** 领料单号*/
	private String issueNo;
	/** 规格型号*/
	private String specNo;
	/** 单价*/
	private Double unitPrice;
	/** 出库人员*/
	private String lastModifyBy;
	/** 领用人*/
	private String receiveMan;
	/** 领用部门*/
	private String receiveDept;
	/** 税率*/
	private String taxRate;
	/** 工程名称*/
	private String projectName;
	/** 领料单类别*/
	private String issueType;
	/** 领料单类别*/
	private String materialKeeper;
	
	/** 单价 String add by liuyi 091130*/
	private String unitPriceString;
	/** 金额 String add by liuyi 091130*/
	private String totalPrice;
	
	/**费用来源编码 add by liuyi 20100316 */
	private String itemCode;
	/**费用来源名称 add by liuyi 20100317 */
	private String itemName;
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getUnitPriceString() {
		return unitPriceString;
	}
	public void setUnitPriceString(String unitPriceString) {
		this.unitPriceString = unitPriceString;
	}
	public String getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
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
	 * @return the unitName
	 */
	public String getUnitName() {
		return unitName;
	}
	/**
	 * @param unitName the unitName to set
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	/**
	 * @return the demandNum
	 */
	public Double getDemandNum() {
		return demandNum;
	}
	/**
	 * @param demandNum the demandNum to set
	 */
	public void setDemandNum(Double demandNum) {
		this.demandNum = demandNum;
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
	 * @return the issueNo
	 */
	public String getIssueNo() {
		return issueNo;
	}
	/**
	 * @param issueNo the issueNo to set
	 */
	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
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
	 * @return the lastModifyBy
	 */
	public String getLastModifyBy() {
		return lastModifyBy;
	}
	/**
	 * @param lastModifyBy the lastModifyBy to set
	 */
	public void setLastModifyBy(String lastModifyBy) {
		this.lastModifyBy = lastModifyBy;
	}
	/**
	 * @return the receiveMan
	 */
	public String getReceiveMan() {
		return receiveMan;
	}
	/**
	 * @param receiveMan the receiveMan to set
	 */
	public void setReceiveMan(String receiveMan) {
		this.receiveMan = receiveMan;
	}
	/**
	 * @return the receiveDept
	 */
	public String getReceiveDept() {
		return receiveDept;
	}
	/**
	 * @param receiveDept the receiveDept to set
	 */
	public void setReceiveDept(String receiveDept) {
		this.receiveDept = receiveDept;
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
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}
	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	/**
	 * @return the issueType
	 */
	public String getIssueType() {
		return issueType;
	}
	/**
	 * @param issueType the issueType to set
	 */
	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}
	/**
	 * @return the materialKeeper
	 */
	public String getMaterialKeeper() {
		return materialKeeper;
	}
	/**
	 * @param materialKeeper the materialKeeper to set
	 */
	public void setMaterialKeeper(String materialKeeper) {
		this.materialKeeper = materialKeeper;
	}

}
