/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.resource;

/**
 * 物料盘点表打印List
 * @author zhujie 
 */
public class MaterialCheckPrintReportListBean {

	/** 物资编码 */
    private String materialNo="";
    /** 物资名称 */
    private String materialName="";
    /** 规格型号 */
    private String specNo="";
    /** 单位 */
    private String stockUmID="";
    /** 仓库 */
    private String whsName="";
    /** 库位 */
    private String locationName="";
    /** 批号 */
    private String lotNo="";
    /** 账面数量 */
    private String accountQuantity="";
    /** 实盘数量 */
    private String realQuantity="";
    /** 行数计数 */
    private Integer cntRow;
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
	 * @return the stockUmID
	 */
	public String getStockUmID() {
		return stockUmID;
	}
	/**
	 * @param stockUmID the stockUmID to set
	 */
	public void setStockUmID(String stockUmID) {
		this.stockUmID = stockUmID;
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
	 * @return the accountQuantity
	 */
	public String getAccountQuantity() {
		return accountQuantity;
	}
	/**
	 * @param accountQuantity the accountQuantity to set
	 */
	public void setAccountQuantity(String accountQuantity) {
		this.accountQuantity = accountQuantity;
	}
	/**
	 * @return the realQuantity
	 */
	public String getRealQuantity() {
		return realQuantity;
	}
	/**
	 * @param realQuantity the realQuantity to set
	 */
	public void setRealQuantity(String realQuantity) {
		this.realQuantity = realQuantity;
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
}
