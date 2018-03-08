package power.ejb.resource.form;

import java.util.Date;

@SuppressWarnings("serial")
public class MrpPlanRequireDetailQueryInfo implements java.io.Serializable{

	//询价明细流水号
	private Long inquireDetailId;
	//汇总ID
	private Long gatherId;
	//需求明细ID
	private String requirementDetailIds;
	//供应商ID
	private Long inquireSupplier;
	//物料ID
	private Long materialId;
	//询价单号
	private String billNo;
	//供应商名称
	private String supplyName;
	//数量
	private Double inquireQty;
	//单价
	private Double unitPrice;
	//总价
	private Double totalPrice;
	//质保期
	private String qualityTime;
	//供货周期
	private String offerCycle;
	//备注
	private String memo;
	//修改人名称
	private String modifyByName;
	//修改日期
	private String modifyDate;
	//是否选择的供货商
	private String isSelectSupplier;
	//物料名称
	private String materialName;
	//规格型号
	private String specNo;
	
	private String effectStartDate; //报价有效开始日期
	private String  effectEndDate; //报价有效结束日期
	//add by bjxu 091104 采购员
	private String buyer;
	
	// add by ywliu 091111
    /** 
     * 分配计划时间
     */
    private String gatherTime;
   
 //  建议供应商 add by bjxu 091214
    private String supplier;
    
 // 建议生产厂家 add by bjxu 091214
	private String Factory;
	
	// 需求计划备注 add by liuyi 20100406
	private String sbMemo;
	// 申报部门 add by liuyi 20100406
	private String sbDeptName;
	
	// 附件 add by liuyi 20100409 
	private String filePath;
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getSbMemo() {
		return sbMemo;
	}
	public void setSbMemo(String sbMemo) {
		this.sbMemo = sbMemo;
	}
	public String getSbDeptName() {
		return sbDeptName;
	}
	public void setSbDeptName(String sbDeptName) {
		this.sbDeptName = sbDeptName;
	}
	/**
	 * @return the gatherTime
	 */
	public String getGatherTime() {
		return gatherTime;
	}
	/**
	 * @param gatherTime the gatherTime to set
	 */
	public void setGatherTime(String gatherTime) {
		this.gatherTime = gatherTime;
	}
	public Long getInquireDetailId() {
		return inquireDetailId;
	}
	public void setInquireDetailId(Long inquireDetailId) {
		this.inquireDetailId = inquireDetailId;
	}
	public Long getGatherId() {
		return gatherId;
	}
	public void setGatherId(Long gatherId) {
		this.gatherId = gatherId;
	}
	public String getRequirementDetailIds() {
		return requirementDetailIds;
	}
	public void setRequirementDetailIds(String requirementDetailIds) {
		this.requirementDetailIds = requirementDetailIds;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public Long getInquireSupplier() {
		return inquireSupplier;
	}
	public void setInquireSupplier(Long inquireSupplier) {
		this.inquireSupplier = inquireSupplier;
	}
	public Double getInquireQty() {
		return inquireQty;
	}
	public void setInquireQty(Double inquireQty) {
		this.inquireQty = inquireQty;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getQualityTime() {
		return qualityTime;
	}
	public void setQualityTime(String qualityTime) {
		this.qualityTime = qualityTime;
	}
	public String getOfferCycle() {
		return offerCycle;
	}
	public void setOfferCycle(String offerCycle) {
		this.offerCycle = offerCycle;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getModifyByName() {
		return modifyByName;
	}
	public void setModifyByName(String modifyByName) {
		this.modifyByName = modifyByName;
	}
	public String getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getIsSelectSupplier() {
		return isSelectSupplier;
	}
	public void setIsSelectSupplier(String isSelectSupplier) {
		this.isSelectSupplier = isSelectSupplier;
	}
	public Long getMaterialId() {
		return materialId;
	}
	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public String getSpecNo() {
		return specNo;
	}
	public void setSpecNo(String specNo) {
		this.specNo = specNo;
	}
	public String getSupplyName() {
		return supplyName;
	}
	public void setSupplyName(String supplyName) {
		this.supplyName = supplyName;
	}
	public String getEffectStartDate() {
		return effectStartDate;
	}
	public void setEffectStartDate(String effectStartDate) {
		this.effectStartDate = effectStartDate;
	}
	public String getEffectEndDate() {
		return effectEndDate;
	}
	public void setEffectEndDate(String effectEndDate) {
		this.effectEndDate = effectEndDate;
	}
	public String getBuyer() {
		return buyer;
	}
	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public String getFactory() {
		return Factory;
	}
	public void setFactory(String factory) {
		Factory = factory;
	}
	
}
