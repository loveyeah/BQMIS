/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.resource.form;

import java.util.Date;

import power.ejb.resource.InvCTransaction;

/**
 * 物料盘点调整bean
 * @author huangweijie
 */
public class BookCheckInfo implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    /** 物料盘点明细表 */
    /** 物料盘点明细流水号 */
    private Long bookDetailId;
    /** 物料盘点明细仓库号 */
    private String whsNo;
    /** 物料盘点明细库位号 */
    private String locationNo;
    /** 物料盘点明细批号 */
    private String lotNo;
    /** 物料盘点明细账面数量 */
    private Double bookQty;
    
    /** 物料盘点主表 */
    /** 物料盘点流水号 */
    private Long bookId;
    
    /** 物料主文件 */
    /** 物料流水号 */
    private Long materialId;
    /** 物料编码 */
    private String materialNo;
    /** 物料名称 */
    private String materialName;
    /** 物料规格型号 */
    private String specNo;
    /** 物料存货计量单位名称 */
    private String stockUmName;
    
    /** 仓库主文件 */
    /** 仓库名称 */
    private String whsName;
    /** 库位主文件 */
    /** 库位名称 */
    private String locationName;
    /** 事务影响 */
    private InvCTransaction trans;
    
    /** 修改人 */
    private String modifyBy;
    /** 实盘数量 */
    private Double physicalQty;
    /** 盈亏数量 */
    private Double balance;
    /** 差异原因 */
    private String reason;
    /** 上次修改时间 */
    private String modifyDate;
    /**
     * @return the bookDetailId
     */
    public Long getBookDetailId() {
        return bookDetailId;
    }
    /**
     * @param bookDetailId the bookDetailId to set
     */
    public void setBookDetailId(Long bookDetailId) {
        this.bookDetailId = bookDetailId;
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
     * @return the locationNo
     */
    public String getLocationNo() {
        return locationNo;
    }
    /**
     * @param locationNo the locationNo to set
     */
    public void setLocationNo(String locationNo) {
        this.locationNo = locationNo;
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
     * @return the bookQty
     */
    public Double getBookQty() {
        return bookQty;
    }
    /**
     * @param bookQty the bookQty to set
     */
    public void setBookQty(Double bookQty) {
        this.bookQty = bookQty;
    }
    /**
     * @return the bookId
     */
    public Long getBookId() {
        return bookId;
    }
    /**
     * @param bookId the bookId to set
     */
    public void setBookId(Long bookId) {
        this.bookId = bookId;
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
     * @return the trans
     */
    public InvCTransaction getTrans() {
        return trans;
    }
    /**
     * @param trans the trans to set
     */
    public void setTrans(InvCTransaction trans) {
        this.trans = trans;
    }
    /**
     * @return the modifyBy
     */
    public String getModifyBy() {
        return modifyBy;
    }
    /**
     * @param modifyBy the modifyBy to set
     */
    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }
    /**
     * @return the physicalQty
     */
    public Double getPhysicalQty() {
        return physicalQty;
    }
    /**
     * @param physicalQty the physicalQty to set
     */
    public void setPhysicalQty(Double physicalQty) {
        this.physicalQty = physicalQty;
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
     * @return the balance
     */
    public Double getBalance() {
        return balance;
    }
    /**
     * @param balance the balance to set
     */
    public void setBalance(Double balance) {
        this.balance = balance;
    }
	/**
	 * @return the modifyDate
	 */
	public String getModifyDate() {
		return modifyDate;
	}
	/**
	 * @param modifyDate the modifyDate to set
	 */
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
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
