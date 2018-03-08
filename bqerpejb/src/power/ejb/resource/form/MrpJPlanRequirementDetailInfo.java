/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.resource.form;

import javax.ejb.Stateless;
import javax.persistence.Entity;

/**
 * 物料需求计划登记查询明细Bean
 * 
 * @author zhouxu
 * 
 */
@Stateless 




public class MrpJPlanRequirementDetailInfo implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Long requirementDetailId;
    private String materialNo;
    private String materialName;
    private String materSize;
    private Double appliedQty;
    private Double approvedQty;
    private Double issQty;
    private Double estimatedPrice;
    private Double estimatedSum;
    private Double purQty;
    private String stockUmName;
    private String usage;
    private String memo;
    private String needDate;
    private String parameter;
    private String docNo;
    private String whsName;
    private String qualityClass;
    private Double left;
    private Double tempNum;
    private String itemId;//modify by ywliu  2009/7/6
    private String cancelReason; //作废原因 add by fyyang 090807
    private String useFlag; //使用标志 Y----使用 C----作废
    
    private String mrDate; // add by liuyi 091029 申请时间
    private String mrNo; // add by liuyi 091029 计划单号
    private String status; // add by jling 状态
    // add by liuyi 091104 开始
    /** 采购项次号 */
	private String orderDetailsId = "";
	/** 采购单号*/
	private String purNo;
	 /** 采购数量 */
    private String purchaseQuatity="";
    /** 交期 */
    private String dueDate="";  
    /** 采购人编码  */
    private String buyerBy;   
    /** 采购人名  */
    private String buyerName;   
    /** 采购时间  */
    private String buyTime;

    /** 已入库数量 */
	private String recQty = "";
	/** 已到货数 */
	private String theQty = "";
	
	/**　实际发货数量　*/
	private String actIssuedCount="";
	// add by liuyi 091104 结束
	
	 //add by fyyang 091105
    private String applyByName; //申请人
    private String applyDeptName; //申请单位
    private String applyReason;   //申请原因
    private String planOriginalId;//计划种类
    private String factory;//生产厂家  add by fyyang  091118
    private String supplier;//建议供应商
    
    //add by drdu 091106
    private String wfNo;//需求计划工作流编号
    private String modifyMemo;//核准数量修改信息 add by fyyang 100112
    private Double unitPirce; //采购单价 add by fyyang 20100318
    
    public String getActIssuedCount() {
		return actIssuedCount;
	}



	public void setActIssuedCount(String actIssuedCount) {
		this.actIssuedCount = actIssuedCount;
	}



	public String getOrderDetailsId() {
		return orderDetailsId;
	}



	public void setOrderDetailsId(String orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}



	public String getPurNo() {
		return purNo;
	}



	public void setPurNo(String purNo) {
		this.purNo = purNo;
	}



	public String getPurchaseQuatity() {
		return purchaseQuatity;
	}



	public void setPurchaseQuatity(String purchaseQuatity) {
		this.purchaseQuatity = purchaseQuatity;
	}



	public String getDueDate() {
		return dueDate;
	}



	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
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



	public String getBuyTime() {
		return buyTime;
	}



	public void setBuyTime(String buyTime) {
		this.buyTime = buyTime;
	}



	public String getMrNo() {
		return mrNo;
	}



	public void setMrNo(String mrNo) {
		this.mrNo = mrNo;
	}



	public String getMrDate() {
		return mrDate;
	}



	public void setMrDate(String mrDate) {
		this.mrDate = mrDate;
	}



	public MrpJPlanRequirementDetailInfo() {
        
    }



    public MrpJPlanRequirementDetailInfo(Long requirementDetailId, String materialNo, String materialName,
            String materSize, Double appliedQty, Double approvedQty, Double issQty, Double estimatedPrice,
            Double estimatedSum, Double purQty, String stockUmName, String usage, String memo, String needDate,
            String parameter, String docNo, String whsName, String qualityClass, Double left, Double tempNum,
            String itemId) {
        this.requirementDetailId = requirementDetailId;
        this.materialNo = materialNo;
        this.materialName = materialName;
        this.materSize = materSize;
        this.appliedQty = appliedQty;
        this.approvedQty = approvedQty;
        this.issQty = issQty;
        this.estimatedPrice = estimatedPrice;
        this.estimatedSum = estimatedSum;
        this.purQty = purQty;
        this.stockUmName = stockUmName;
        this.usage = usage;
        this.memo = memo;
        this.needDate = needDate;
        this.parameter = parameter;
        this.docNo = docNo;
        this.whsName = whsName;
        this.qualityClass = qualityClass;
        this.left = left;
        this.tempNum = tempNum;
        this.itemId = itemId;
    }



    /**
     * @return the requirementDetailId
     */
    public Long getRequirementDetailId() {
        return requirementDetailId;
    }

    /**
     * @param requirementDetailId
     *            the requirementDetailId to set
     */
    public void setRequirementDetailId(Long requirementDetailId) {
        this.requirementDetailId = requirementDetailId;
    }

    /**
     * @return the materialNo
     */
    public String getMaterialNo() {
        return materialNo;
    }

    /**
     * @param materialNo
     *            the materialNo to set
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
     * @param materialName
     *            the materialName to set
     */
    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    /**
     * @return the materSize
     */
    public String getMaterSize() {
        return materSize;
    }

    /**
     * @param materSize
     *            the materSize to set
     */
    public void setMaterSize(String materSize) {
        this.materSize = materSize;
    }

    /**
     * @return the appliedQty
     */
    public Double getAppliedQty() {
        return appliedQty;
    }

    /**
     * @param appliedQty
     *            the appliedQty to set
     */
    public void setAppliedQty(Double appliedQty) {
        this.appliedQty = appliedQty;
    }

    /**
     * @return the approvedQty
     */
    public Double getApprovedQty() {
        return approvedQty;
    }

    /**
     * @param approvedQty
     *            the approvedQty to set
     */
    public void setApprovedQty(Double approvedQty) {
        this.approvedQty = approvedQty;
    }

    /**
     * @return the issQty
     */
    public Double getIssQty() {
        return issQty;
    }

    /**
     * @param issQty
     *            the issQty to set
     */
    public void setIssQty(Double issQty) {
        this.issQty = issQty;
    }

    /**
     * @return the estimatedPrice
     */
    public Double getEstimatedPrice() {
        return estimatedPrice;
    }

    /**
     * @param estimatedPrice
     *            the estimatedPrice to set
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
     * @param estimatedSum
     *            the estimatedSum to set
     */
    public void setEstimatedSum(Double estimatedSum) {
        this.estimatedSum = estimatedSum;
    }

    /**
     * @return the purQty
     */
    public Double getPurQty() {
        return purQty;
    }

    /**
     * @param purQty
     *            the purQty to set
     */
    public void setPurQty(Double purQty) {
        this.purQty = purQty;
    }

    /**
     * @return the stockUmName
     */
    public String getStockUmName() {
        return stockUmName;
    }

    /**
     * @param stockUmName
     *            the stockUmName to set
     */
    public void setStockUmName(String stockUmName) {
        this.stockUmName = stockUmName;
    }

    /**
     * @return the usage
     */
    public String getUsage() {
        return usage;
    }

    /**
     * @param usage
     *            the usage to set
     */
    public void setUsage(String usage) {
        this.usage = usage;
    }

    /**
     * @return the memo
     */
    public String getMemo() {
        return memo;
    }

    /**
     * @param memo
     *            the memo to set
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * @return the needDate
     */
    public String getNeedDate() {
        return needDate;
    }

    /**
     * @param needDate
     *            the needDate to set
     */
    public void setNeedDate(String needDate) {
        this.needDate = needDate;
    }

    /**
     * @return the parameter
     */
    public String getParameter() {
        return parameter;
    }

    /**
     * @param parameter
     *            the parameter to set
     */
    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    /**
     * @return the docNo
     */
    public String getDocNo() {
        return docNo;
    }

    /**
     * @param docNo
     *            the docNo to set
     */
    public void setDocNo(String docNo) {
        this.docNo = docNo;
    }

    /**
     * @return the whsName
     */
    public String getWhsName() {
        return whsName;
    }

    /**
     * @param whsName
     *            the whsName to set
     */
    public void setWhsName(String whsName) {
        this.whsName = whsName;
    }

    /**
     * @return the qualityClass
     */
    public String getQualityClass() {
        return qualityClass;
    }

    /**
     * @param qualityClass
     *            the qualityClass to set
     */
    public void setQualityClass(String qualityClass) {
        this.qualityClass = qualityClass;
    }

    /**
     * @return the left
     */
    public Double getLeft() {
        return left;
    }

    /**
     * @param left
     *            the left to set
     */
    public void setLeft(Double left) {
        this.left = left;
    }

    /**
     * @return the tempNum
     */
    public Double getTempNum() {
        return tempNum;
    }

    /**
     * @param tempNum
     *            the tempNum to set
     */
    public void setTempNum(Double tempNum) {
        this.tempNum = tempNum;
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



	public String getCancelReason() {
		return cancelReason;
	}



	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}



	public String getUseFlag() {
		return useFlag;
	}



	public void setUseFlag(String useFlag) {
		this.useFlag = useFlag;
	}



	public String getRecQty() {
		return recQty;
	}



	public void setRecQty(String recQty) {
		this.recQty = recQty;
	}



	public String getTheQty() {
		return theQty;
	}



	public void setTheQty(String theQty) {
		this.theQty = theQty;
	}



	public String getApplyByName() {
		return applyByName;
	}



	public void setApplyByName(String applyByName) {
		this.applyByName = applyByName;
	}



	public String getApplyDeptName() {
		return applyDeptName;
	}



	public void setApplyDeptName(String applyDeptName) {
		this.applyDeptName = applyDeptName;
	}



	public String getApplyReason() {
		return applyReason;
	}



	public void setApplyReason(String applyReason) {
		this.applyReason = applyReason;
	}



	public String getPlanOriginalId() {
		return planOriginalId;
	}



	public void setPlanOriginalId(String planOriginalId) {
		this.planOriginalId = planOriginalId;
	}



	public String getWfNo() {
		return wfNo;
	}



	public void setWfNo(String wfNo) {
		this.wfNo = wfNo;
	}



	public String getFactory() {
		return factory;
	}



	public void setFactory(String factory) {
		this.factory = factory;
	}



	public String getSupplier() {
		return supplier;
	}



	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}



	public String getModifyMemo() {
		return modifyMemo;
	}



	public void setModifyMemo(String modifyMemo) {
		this.modifyMemo = modifyMemo;
	}



	public Double getUnitPirce() {
		return unitPirce;
	}



	public void setUnitPirce(Double unitPirce) {
		this.unitPirce = unitPirce;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}





}
