/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.resource;

/**
 * 物料需求计划登记明细List
 * @author zhujie 
 */
public class MaterialRequestReportDetailListBean {

	/** 项次号 */
	private String orderDetailsId = "";
	/** 物资编码 */
    private String materialNo="";
    /** 物资名称 */
    private String materialName="";
    /** 规格型号 */
    private String specNo="";
    /** 请求数量 */
    private String requestQuantity="";
    /** 核准数量 */
    private String checkQuantity="";
    /** 已领数量 */
    private String gotQuantity="";
    /** 估计单价 */
    private String estimatedPrice="";
    /** 估计金额 */
    private String estimatedMoney="";
    /** 采购数量 */
    private String purchaseQuantity="";
    /** 单位 */
    private String stockUmName="";
    /** 需求日期 */
    private String dueDate="";
    /** 物料材质 */
    private String materialPramater="";
    /** 物料图号 */
    private String materialMapNo="";
    /** 仓库号 */
    private String wareHouseNo="";
    /** 质量等级 */
    private String qualityLevel="";
    /** 当前库存 */
    private String nowStock="";
    /** 暂收数量 */
    private String instanceGotQuantity="";
    /** 费用来源 */
    private String moneyFrom="";
    /** 用途 */
    private String useFor="";
    /** 备注 */
    private String meno="";
    /** 行数计数 */
    private Integer cntRow;
    /** 合计金额 */
    private String totalMoney="";
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
	 * @return the requestQuantity
	 */
	public String getRequestQuantity() {
		return requestQuantity;
	}
	/**
	 * @param requestQuantity the requestQuantity to set
	 */
	public void setRequestQuantity(String requestQuantity) {
		this.requestQuantity = requestQuantity;
	}
	/**
	 * @return the checkQuantity
	 */
	public String getCheckQuantity() {
		return checkQuantity;
	}
	/**
	 * @param checkQuantity the checkQuantity to set
	 */
	public void setCheckQuantity(String checkQuantity) {
		this.checkQuantity = checkQuantity;
	}
	/**
	 * @return the gotQuantity
	 */
	public String getGotQuantity() {
		return gotQuantity;
	}
	/**
	 * @param gotQuantity the gotQuantity to set
	 */
	public void setGotQuantity(String gotQuantity) {
		this.gotQuantity = gotQuantity;
	}
	/**
	 * @return the estimatedPrice
	 */
	public String getEstimatedPrice() {
		return estimatedPrice;
	}
	/**
	 * @param estimatedPrice the estimatedPrice to set
	 */
	public void setEstimatedPrice(String estimatedPrice) {
		this.estimatedPrice = estimatedPrice;
	}
	/**
	 * @return the estimatedMoney
	 */
	public String getEstimatedMoney() {
		return estimatedMoney;
	}
	/**
	 * @param estimatedMoney the estimatedMoney to set
	 */
	public void setEstimatedMoney(String estimatedMoney) {
		this.estimatedMoney = estimatedMoney;
	}
	/**
	 * @return the purchaseQuantity
	 */
	public String getPurchaseQuantity() {
		return purchaseQuantity;
	}
	/**
	 * @param purchaseQuantity the purchaseQuantity to set
	 */
	public void setPurchaseQuantity(String purchaseQuantity) {
		this.purchaseQuantity = purchaseQuantity;
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
	 * @return the materialPramater
	 */
	public String getMaterialPramater() {
		return materialPramater;
	}
	/**
	 * @param materialPramater the materialPramater to set
	 */
	public void setMaterialPramater(String materialPramater) {
		this.materialPramater = materialPramater;
	}
	/**
	 * @return the materialMapNo
	 */
	public String getMaterialMapNo() {
		return materialMapNo;
	}
	/**
	 * @param materialMapNo the materialMapNo to set
	 */
	public void setMaterialMapNo(String materialMapNo) {
		this.materialMapNo = materialMapNo;
	}
	/**
	 * @return the wareHouseNo
	 */
	public String getWareHouseNo() {
		return wareHouseNo;
	}
	/**
	 * @param wareHouseNo the wareHouseNo to set
	 */
	public void setWareHouseNo(String wareHouseNo) {
		this.wareHouseNo = wareHouseNo;
	}
	/**
	 * @return the qualityLevel
	 */
	public String getQualityLevel() {
		return qualityLevel;
	}
	/**
	 * @param qualityLevel the qualityLevel to set
	 */
	public void setQualityLevel(String qualityLevel) {
		this.qualityLevel = qualityLevel;
	}
	/**
	 * @return the nowStock
	 */
	public String getNowStock() {
		return nowStock;
	}
	/**
	 * @param nowStock the nowStock to set
	 */
	public void setNowStock(String nowStock) {
		this.nowStock = nowStock;
	}
	/**
	 * @return the instanceGotQuantity
	 */
	public String getInstanceGotQuantity() {
		return instanceGotQuantity;
	}
	/**
	 * @param instanceGotQuantity the instanceGotQuantity to set
	 */
	public void setInstanceGotQuantity(String instanceGotQuantity) {
		this.instanceGotQuantity = instanceGotQuantity;
	}
	/**
	 * @return the moneyFrom
	 */
	public String getMoneyFrom() {
		return moneyFrom;
	}
	/**
	 * @param moneyFrom the moneyFrom to set
	 */
	public void setMoneyFrom(String moneyFrom) {
		this.moneyFrom = moneyFrom;
	}
	/**
	 * @return the useFor
	 */
	public String getUseFor() {
		return useFor;
	}
	/**
	 * @param useFor the useFor to set
	 */
	public void setUseFor(String useFor) {
		this.useFor = useFor;
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
	 * @return the totalMoney
	 */
	public String getTotalMoney() {
		return totalMoney;
	}
	/**
	 * @param totalMoney the totalMoney to set
	 */
	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}
    
}
