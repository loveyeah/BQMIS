/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.resource;

/**
 * 库存再订货报表List详细信息
 * @author zhujie 
 */
public class WareHouseListBean {

	/** 物资编码 */
    private String materialNo="";
    /** 物资名称 */
    private String materialName="";
    /** 规格型号 */
    private String specNo="";
    /** 单位 */
    private String stockUmID="";
    /** 当前库存 */
    private String nowStock="";
    /** 建议再订货数量 */
    private String adviceBuyNumber="";
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
	 * @return the adviceBuyNumber
	 */
	public String getAdviceBuyNumber() {
		return adviceBuyNumber;
	}
	/**
	 * @param adviceBuyNumber the adviceBuyNumber to set
	 */
	public void setAdviceBuyNumber(String adviceBuyNumber) {
		this.adviceBuyNumber = adviceBuyNumber;
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
