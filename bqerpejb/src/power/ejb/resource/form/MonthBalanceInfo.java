/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.resource.form;

/**
 * 库存月结 bean
 * @author huangweijie
 */
public class MonthBalanceInfo implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    
    /** 批号 */
    private String lotNo;
    /** 仓库号 */
    private String whsNo;
    /** 库位号 */
    private String locationNo;
    /** 物料流水号 */
    private Long materialId;
    /** 本期接收 */
    private Double receipt;
    /** 本期调整 */
    private Double adjust;
    /** 本期出库 */
    private Double issue;
    /** 物料数量 */
    private Double quantity;
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
     * @return the receipt
     */
    public Double getReceipt() {
        return receipt;
    }
    /**
     * @param receipt the receipt to set
     */
    public void setReceipt(Double receipt) {
        this.receipt = receipt;
    }
    /**
     * @return the adjust
     */
    public Double getAdjust() {
        return adjust;
    }
    /**
     * @param adjust the adjust to set
     */
    public void setAdjust(Double adjust) {
        this.adjust = adjust;
    }
    /**
     * @return the issue
     */
    public Double getIssue() {
        return issue;
    }
    /**
     * @param issue the issue to set
     */
    public void setIssue(Double issue) {
        this.issue = issue;
    }
    /**
     * @return the quantity
     */
    public Double getQuantity() {
        return quantity;
    }
    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
}
