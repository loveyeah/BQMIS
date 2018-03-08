/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.resource;

/**
 * 到货登记表明细
 * @author zhujie 
 */
public class ReceiveGoodsListBean {

	/** 物资编码 */
    private String materialNo="";
    /** 物资名称 */
    private String materialName="";
    /** 规格型号 */
    private String specNo="";
    /** 单位 */
    private String stockUmID="";
    /** 采购数 */
    private String purcharseQuantity="";
    /** 已收数 */
    private String achieveQuantity="";
    /** 待收数 */
    private String prepareQuantity="";
    /** 到货数 */
    private String theQuantity="";
    /** 批号 */
    private String batchNumber="";
    /** 备忘 */
    private String meno="";
    
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
	 * @return the purcharseQuantity
	 */
	public String getPurcharseQuantity() {
		return purcharseQuantity;
	}
	/**
	 * @param purcharseQuantity the purcharseQuantity to set
	 */
	public void setPurcharseQuantity(String purcharseQuantity) {
		this.purcharseQuantity = purcharseQuantity;
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
	 * @return the prepareQuantity
	 */
	public String getPrepareQuantity() {
		return prepareQuantity;
	}
	/**
	 * @param prepareQuantity the prepareQuantity to set
	 */
	public void setPrepareQuantity(String prepareQuantity) {
		this.prepareQuantity = prepareQuantity;
	}
	/**
	 * @return the theQuantity
	 */
	public String getTheQuantity() {
		return theQuantity;
	}
	/**
	 * @param theQuantity the theQuantity to set
	 */
	public void setTheQuantity(String theQuantity) {
		this.theQuantity = theQuantity;
	}
	/**
	 * @return the batchNumber
	 */
	public String getBatchNumber() {
		return batchNumber;
	}
	/**
	 * @param batchNumber the batchNumber to set
	 */
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
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
}
