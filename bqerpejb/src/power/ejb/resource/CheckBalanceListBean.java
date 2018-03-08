/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.resource;

/**
 * 盘点损益表List详细信息
 * @author zhujie 
 */
public class CheckBalanceListBean {

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
    private String bookQuantity="";
    /** 实盘数量 */
    private String physicalQuantity="";
    /** 盈亏数量 */
    private String earnQuantity="";
    /** 账面金额 */
    private String bookMoney="";
    /** 盈亏金额 */
    private String earnMoney="";
    /** 差异原因 */
    private String reason="";
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
	 * @return the bookQuantity
	 */
	public String getBookQuantity() {
		return bookQuantity;
	}
	/**
	 * @param bookQuantity the bookQuantity to set
	 */
	public void setBookQuantity(String bookQuantity) {
		this.bookQuantity = bookQuantity;
	}
	/**
	 * @return the physicalQuantity
	 */
	public String getPhysicalQuantity() {
		return physicalQuantity;
	}
	/**
	 * @param physicalQuantity the physicalQuantity to set
	 */
	public void setPhysicalQuantity(String physicalQuantity) {
		this.physicalQuantity = physicalQuantity;
	}
	/**
	 * @return the earnQuantity
	 */
	public String getEarnQuantity() {
		return earnQuantity;
	}
	/**
	 * @param earnQuantity the earnQuantity to set
	 */
	public void setEarnQuantity(String earnQuantity) {
		this.earnQuantity = earnQuantity;
	}
	/**
	 * @return the bookMoney
	 */
	public String getBookMoney() {
		return bookMoney;
	}
	/**
	 * @param bookMoney the bookMoney to set
	 */
	public void setBookMoney(String bookMoney) {
		this.bookMoney = bookMoney;
	}
	/**
	 * @return the earnMoney
	 */
	public String getEarnMoney() {
		return earnMoney;
	}
	/**
	 * @param earnMoney the earnMoney to set
	 */
	public void setEarnMoney(String earnMoney) {
		this.earnMoney = earnMoney;
	}
	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
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
