/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.resource;

/**
 * 采购表List详细信息
 * @author zhujie 
 */
public class PurchaseListBean {

	/** 项次号 */
	private String orderDetailsId = "";
	/** 物资编码 */
    private String materialNo="";
    /** 物资名称 */
    private String materialName="";
    /** 规格型号 */
    private String specNo="";
    /** 参数/材质 */
    private String paramater="";
    /** 采购数量 */
    private String purchaseQuatity="";
    /** 已收数量 */
    private String achieveQuantity="";
    /** 单价 */
    private String quotedPrice="";
    /** 交期 */
    private String dueDate="";
    /** 税率 */
    private String taxRate="";
    /** 小计金额 */
    private String littleCulMoney="";
    /** 税额 */
    private String taxMoney="";
    /** 是否免检 */
    private String whetherNoCheck="";
    /** 备注 */
    private String meno="";
    /** 行数计数 */
    private Integer cntRow;
    
    /**
     * 采购人编码 add by liuyi 091029
     */
    private String buyerBy;
    
    /**
     * 采购人名 add by liuyi 091029
     */
    private String buyerName;
    
    /**
     * 采购时间 add by liuyi 091029
     */
    private String buyTime;
    
    /**
     * 申报部门编码 add by liuyi 20100406
     */
    private String deptCode;
    
    /**
     * 申报部门名称 add by liuyi 20100406
     */
    private String deptName;
    
  
	public String getBuyTime() {
		return buyTime;
	}
	public void setBuyTime(String buyTime) {
		this.buyTime = buyTime;
	}
	
	/**
	 * @return the orderDetailsId
	 */
	public String getOrderDetailsId() {
		return orderDetailsId;
	}
	/**
	 * @param orderDetailsId the orderDetailsId to set
	 */
	public void setOrderDetailsId(String orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
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
	 * @return the paramater
	 */
	public String getParamater() {
		return paramater;
	}
	/**
	 * @param paramater the paramater to set
	 */
	public void setParamater(String paramater) {
		this.paramater = paramater;
	}
	/**
	 * @return the purchaseQuatity
	 */
	public String getPurchaseQuatity() {
		return purchaseQuatity;
	}
	/**
	 * @param purchaseQuatity the purchaseQuatity to set
	 */
	public void setPurchaseQuatity(String purchaseQuatity) {
		this.purchaseQuatity = purchaseQuatity;
	}
	/**
	 * @return the achieveQuantity
	 */
	public String getAchieveQuantity() {
		return achieveQuantity;
	}
	/**
	 * @param achieveQuantity the achieveQuantity to set
	 */
	public void setAchieveQuantity(String achieveQuantity) {
		this.achieveQuantity = achieveQuantity;
	}
	/**
	 * @return the quotedPrice
	 */
	public String getQuotedPrice() {
		return quotedPrice;
	}
	/**
	 * @param quotedPrice the quotedPrice to set
	 */
	public void setQuotedPrice(String quotedPrice) {
		this.quotedPrice = quotedPrice;
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
	 * @return the littleCulMoney
	 */
	public String getLittleCulMoney() {
		return littleCulMoney;
	}
	/**
	 * @param littleCulMoney the littleCulMoney to set
	 */
	public void setLittleCulMoney(String littleCulMoney) {
		this.littleCulMoney = littleCulMoney;
	}
	/**
	 * @return the taxMoney
	 */
	public String getTaxMoney() {
		return taxMoney;
	}
	/**
	 * @param taxMoney the taxMoney to set
	 */
	public void setTaxMoney(String taxMoney) {
		this.taxMoney = taxMoney;
	}
	/**
	 * @return the whetherNoCheck
	 */
	public String getWhetherNoCheck() {
		return whetherNoCheck;
	}
	/**
	 * @param whetherNoCheck the whetherNoCheck to set
	 */
	public void setWhetherNoCheck(String whetherNoCheck) {
		this.whetherNoCheck = whetherNoCheck;
	}
	/**
	 * @return the meno
	 */
	public String getMeno() {
		return meno;
	}
	/**
	 * @param meno the meno to set
	 */
	public void setMeno(String meno) {
		this.meno = meno;
	}
	/**
	 * @return the cntRow
	 */
	public Integer getCntRow() {
		return cntRow;
	}
	/**
	 * @param cntRow the cntRow to set
	 */
	public void setCntRow(Integer cntRow) {
		this.cntRow = cntRow;
	}
	public String getBuyerBy() {
		return buyerBy;
	}
	public void setBuyerBy(String buyerBy) {
		this.buyerBy = buyerBy;
	}
	public String getBuyerName() {
		return buyerName;
	}
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
}
