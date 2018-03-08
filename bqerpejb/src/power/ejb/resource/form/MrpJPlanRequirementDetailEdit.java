/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.resource.form;

import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 物料需求计划登记登记明细Bean
 * 
 * @author zhouxu
 * 
 */
@Stateless
public class MrpJPlanRequirementDetailEdit {//TODO 还有设备表的字段没有加上
    private static final long serialVersionUID = 1L;
    private Long requirementDetailId;
    private Long materialId;
    private String materialName;
    private String equCode;
    private String materSize;
    private String parameter;
    private String stockUmName;
    private Double appliedQty;
    private Double estimatedPrice;
    private Double estimatedSum;
    private Double left;
    private Double maxStock;
    private String usage;
    private String factory;
    private String needDate;
    private String supplier;
    private String memo;
    private String itemId;//modify by ywliu  2009/7/6
  
    
    private Long planOriginalId;
    private Date lastModifiedDate;
    private Double issQty;//add by wpzhu 100408
    private Double  insQty;

    // add by liuyi 091026 物料编码
    private String materialCode;
    
    private String classNo;//物料分类 add by ltong 20100505
    
    /**
	 * @return the classNo
	 */
	public String getClassNo() {
		return classNo;
	}


	/**
	 * @param classNo the classNo to set
	 */
	public void setClassNo(String classNo) {
		this.classNo = classNo;
	}


	public MrpJPlanRequirementDetailEdit() {

    }


    public MrpJPlanRequirementDetailEdit(Long requirementDetailId, Long materialId, String materialName,
            String equCode, String materSize, String parameter, String stockUmName, Double appliedQty,
            Double estimatedPrice, Double estimatedSum, Double left, Double maxStock, String usage, String factory,
            String needDate, String supplier, String memo, String itemId, Long planOriginalId, Date lastModifiedDate,Double issQty,Double insQty) {
        this.requirementDetailId = requirementDetailId;
        this.materialId = materialId;
        this.materialName = materialName;
        this.equCode = equCode;
        this.materSize = materSize;
        this.parameter = parameter;
        this.stockUmName = stockUmName;
        this.appliedQty = appliedQty;
        this.estimatedPrice = estimatedPrice;
        this.estimatedSum = estimatedSum;
        this.left = left;
        this.maxStock = maxStock;
        this.usage = usage;
        this.factory = factory;
        this.needDate = needDate;
        this.supplier = supplier;
        this.memo = memo;
        this.itemId = itemId;
        this.planOriginalId = planOriginalId;
        this.lastModifiedDate = lastModifiedDate;
        this.insQty=insQty;
        this.issQty=issQty;
    }


    /**
     * @return the requirementDetailId
     */
    public Long getRequirementDetailId() {
        return requirementDetailId;
    }

    /**
     * @param requirementDetailId the requirementDetailId to set
     */
    public void setRequirementDetailId(Long requirementDetailId) {
        this.requirementDetailId = requirementDetailId;
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
     * @return the equCode
     */
    public String getEquCode() {
        return equCode;
    }

    /**
     * @param equCode the equCode to set
     */
    public void setEquCode(String equCode) {
        this.equCode = equCode;
    }

    /**
     * @return the materSize
     */
    public String getMaterSize() {
        return materSize;
    }

    /**
     * @param materSize the materSize to set
     */
    public void setMaterSize(String materSize) {
        this.materSize = materSize;
    }

    /**
     * @return the parameter
     */
    public String getParameter() {
        return parameter;
    }

    /**
     * @param parameter the parameter to set
     */
    public void setParameter(String parameter) {
        this.parameter = parameter;
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
     * @return the appliedQty
     */
    public Double getAppliedQty() {
        return appliedQty;
    }

    /**
     * @param appliedQty the appliedQty to set
     */
    public void setAppliedQty(Double appliedQty) {
        this.appliedQty = appliedQty;
    }

    /**
     * @return the estimatedPrice
     */
    public Double getEstimatedPrice() {
        return estimatedPrice;
    }

    /**
     * @param estimatedPrice the estimatedPrice to set
     */
    public void setEstimatedPrice(Double estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }

    /**
     * @return the estimatedSum
     */
    public Double getEstimatedSum() {
        return estimatedSum;
    }

    /**
     * @param estimatedSum the estimatedSum to set
     */
    public void setEstimatedSum(Double estimatedSum) {
        this.estimatedSum = estimatedSum;
    }

    /**
     * @return the left
     */
    public Double getLeft() {
        return left;
    }

    /**
     * @param left the left to set
     */
    public void setLeft(Double left) {
        this.left = left;
    }

    /**
     * @return the maxStock
     */
    public Double getMaxStock() {
        return maxStock;
    }

    /**
     * @param maxStock the maxStock to set
     */
    public void setMaxStock(Double maxStock) {
        this.maxStock = maxStock;
    }

    /**
     * @return the usage
     */
    public String getUsage() {
        return usage;
    }

    /**
     * @param usage the usage to set
     */
    public void setUsage(String usage) {
        this.usage = usage;
    }

    /**
     * @return the factory
     */
    public String getFactory() {
        return factory;
    }

    /**
     * @param factory the factory to set
     */
    public void setFactory(String factory) {
        this.factory = factory;
    }

    /**
     * @return the needDate
     */
    public String getNeedDate() {
        return needDate;
    }

    /**
     * @param needDate the needDate to set
     */
    public void setNeedDate(String needDate) {
        this.needDate = needDate;
    }

    /**
     * @return the supplier
     */
    public String getSupplier() {
        return supplier;
    }

    /**
     * @param supplier the supplier to set
     */
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    /**
     * @return the memo
     */
    public String getMemo() {
        return memo;
    }

    /**
     * @param memo the memo to set
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * @return the itemId
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * @param itemId the itemId to set
     */
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    /**
     * @return the planOriginalId
     */
    public Long getPlanOriginalId() {
        return planOriginalId;
    }

    /**
     * @param planOriginalId the planOriginalId to set
     */
    public void setPlanOriginalId(Long planOriginalId) {
        this.planOriginalId = planOriginalId;
    }


    /**
     * @return the lastModifiedDate
     */
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }


    /**
     * @param lastModifiedDate the lastModifiedDate to set
     */
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }


	public String getMaterialCode() {
		return materialCode;
	}


	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}


	public Double getIssQty() {
		return issQty;
	}


	public void setIssQty(Double issQty) {
		this.issQty = issQty;
	}


	public Double getInsQty() {
		return insQty;
	}


	public void setInsQty(Double insQty) {
		this.insQty = insQty;
	}


	


}
