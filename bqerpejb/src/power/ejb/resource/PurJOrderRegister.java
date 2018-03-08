/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.resource;

import java.util.Date;

/**
 * 采购单登记 自定义Bean
 * @author huyou
 *
 */
public class PurJOrderRegister implements java.io.Serializable{

	/**
	 * serialID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 采购订单明细表.流水号
	 */
	private Long orderDetailsId = null;

	/**
	 * 物料主文件.规格型号
	 */
	private String specNo = null;

	/**
	 * 采购订单明细表.采购数量
	 */
	private Double purQty = null;

	/**
	 * 物料主文件.是否免检
	 */
	private String qaControlFlag = null;

	/**
	 * 物资需求计划明细表.申请单编号/需求计划表头流水号
	 */
	private Long requirementHeadId = null;

	/**
	 * 采购订单表.备注
	 */
	private String orderMemo = null;

	/**
	 * 采购人员
	 */
	private String buyer = null;

	/**
	 * 采购订单明细表.交期
	 */
	private Date orderDetailsDueDate = null;

	/**
	 * 合同编号
	 */
	private String contractNo = null;

	/**
	 * 物资需求计划明细表.流水号
	 */
	private Long requirementDetailId = null;

	/**
	 * 采购订单表.税率
	 */
	private Double orderTaxRate = null;

	/**
	 * 订单编号
	 */
	private String purNo = null;

	/**
	 * 币别ID
	 */
	private Long currencyId = null;

	/**
	 * 采购订单表.流水号
	 */
	private Long orderId = null;

	/**
	 * 采购订单明细表.已收数量
	 */
	private Double rcvQty = null;

	/**
	 * 是否为需求单的采购单明细
	 */
	private Double purOrderDetailsId = null;

	/**
	 * 物料主文件.材质/参数
	 */
	private String parameter = null;

	/**
	 * 汇率.汇率
	 */
	private Double rate = null;

	/**
	 * 采购订单表.交期
	 */
	private Date orderDueDate = null;

	/**
	 * 物料主文件.编码
	 */
	private Long materialId = null;

	/**
	 * 采购订单明细表.备注
	 */
	private String orderDetailsMemo = null;

	/**
	 * 采购订单与需求计划关联表.从计划分拆或合并的数量
	 */
	private Double needQty = null;

	/**
	 * 采购订单与需求计划关联表．流水号
	 */
	private Long planOrderId = null;

	/**
	 * 订单状态
	 */
	private String purStatus = null;

	/**
	 * 物料主文件.最大库存量
	 */
	private Double maxStock = null;

	/**
	 * 物资需求计划明细表.已生成采购的数量
	 */
	private Double planPurQty = null;

	/**
	 * 物料主文件.名称
	 */
	private String materialName = null;

	/**
	 * 询价明细表.报价
	 */
	private Double quotedPrice = null;

	/**
	 * 采购员维护表.人员名称
	 */
	private String buyerName = null;

	/**
	 * 供应商表.供应商全称
	 */
	private String supplyName = null;

	/**
	 * 物资需求计划明细表.核准数量
	 */
	private Double approvedQty = null;

	/**
	 * 采购订单明细表.税率
	 */
	private Double orderDetailsTaxRate = null;
	
	/**
	 * 物料主文件.采购计量单位
	 */
	private Long purUmId = null;
	
	/**
	 * 物料主文件.采购计量单位add by drdu 091127
	 */
	private String stockUmName = null;
	/**
	 * 物料主文件.编码
	 */
	private String materialNo = null;
	
	/**
	 * 物资需求计划明细表.上次修改日期
	 */
	private String planModifyDate = null;

	/**
	 * 采购订单表.上次修改日期
	 */
	private String orderModifyDate = null;

	/**
	 * 采购订单与需求计划关联表.上次修改日期
	 */
	private String planRelateModifyDate = null;

	/**
	 * 采购订单明细表.上次修改日期
	 */
	private String orderDetailsModifyDate = null;
	/**
	 * 供应商Id
	 */
	private Long supplier = null;

	/**
	 * 供应商编号
	 */
	private String supplierNo = null;
	
	/*
	 * 最后修改人
	 * @author yiliu
	 */
	private String lastModifiedBy;
	
	/*
	 * 最后修改人姓名
	 * @author yiliu
	 */
	private String lastModifiedByName;
	
	/*
	 * 工程项目
	 * @author yiliu
	 */
	private String  project;
	
	/*
	 * 采购单明细表.暂收数量
	 * @author yiliu
	 */
	private Double insQty = null;
	
	/*
	 * 采购单明细表.单价
	 * @author yiliu
	 */
	private Double unitPrice = null;
	/*
	 * 币种表.名称
	 * @author yiliu
	 */
	private String currencyName = null;
	
	/**
	 * 发票号
	 * @author bjxu 091209
	 */
	private String invoiceNo =null;
	
	/**
	 * 申报部门
	 */
	private String sbDeptName;
	
	private String sbMemo;
	
	public String getSbMemo() {
		return sbMemo;
	}

	public void setSbMemo(String sbMemo) {
		this.sbMemo = sbMemo;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getInsQty() {
		return insQty;
	}

	public void setInsQty(Double insQty) {
		this.insQty = insQty;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	/**
	 * 取得供应商Id
	 *
	 * @return 供应商Id
	 */
	public Long getSupplier() {
		return supplier;
	}

	/**
	 * 设置供应商Id
	 *
	 * @param argSupplier 供应商Id
	 */
	public void setSupplier(Long argSupplier) {
		supplier = argSupplier;
	}

	/**
	 * 取得供应商编号
	 *
	 * @return 供应商编号
	 */
	public String getSupplierNo() {
		return supplierNo;
	}

	/**
	 * 设置供应商编号
	 *
	 * @param argSupplierNo 供应商编号
	 */
	public void setSupplierNo(String argSupplierNo) {
		supplierNo = argSupplierNo;
	}


	/**
	 * 取得物资需求计划明细表.上次修改日期
	 *
	 * @return 物资需求计划明细表.上次修改日期
	 */
	public String getPlanModifyDate() {
		return planModifyDate;
	}

	/**
	 * 设置物资需求计划明细表.上次修改日期
	 *
	 * @param argPlanModifyDate 物资需求计划明细表.上次修改日期
	 */
	public void setPlanModifyDate(String argPlanModifyDate) {
		planModifyDate = argPlanModifyDate;
	}

	/**
	 * 取得采购订单表.上次修改日期
	 *
	 * @return 采购订单表.上次修改日期
	 */
	public String getOrderModifyDate() {
		return orderModifyDate;
	}

	/**
	 * 设置采购订单表.上次修改日期
	 *
	 * @param argOrderModifyDate 采购订单表.上次修改日期
	 */
	public void setOrderModifyDate(String argOrderModifyDate) {
		orderModifyDate = argOrderModifyDate;
	}

	/**
	 * 取得采购订单与需求计划关联表.上次修改日期
	 *
	 * @return 采购订单与需求计划关联表.上次修改日期
	 */
	public String getPlanRelateModifyDate() {
		return planRelateModifyDate;
	}

	/**
	 * 设置采购订单与需求计划关联表.上次修改日期
	 *
	 * @param argPlanRelateModifyDate 采购订单与需求计划关联表.上次修改日期
	 */
	public void setPlanRelateModifyDate(String argPlanRelateModifyDate) {
		planRelateModifyDate = argPlanRelateModifyDate;
	}

	/**
	 * 取得采购订单明细表.上次修改日期
	 *
	 * @return 采购订单明细表.上次修改日期
	 */
	public String getOrderDetailsModifyDate() {
		return orderDetailsModifyDate;
	}

	/**
	 * 设置采购订单明细表.上次修改日期
	 *
	 * @param argOrderDetailsModifyDate 采购订单明细表.上次修改日期
	 */
	public void setOrderDetailsModifyDate(String argOrderDetailsModifyDate) {
		orderDetailsModifyDate = argOrderDetailsModifyDate;
	}

	/**
	 * 取得物料主文件.编码
	 *
	 * @return 物料主文件.编码
	 */
	public String getMaterialNo() {
		return materialNo;
	}

	/**
	 * 设置物料主文件.编码
	 *
	 * @param argMaterialNo 物料主文件.编码
	 */
	public void setMaterialNo(String argMaterialNo) {
		materialNo = argMaterialNo;
	}
	
	/**
	 * 取得物料主文件.采购计量单位
	 *
	 * @return 物料主文件.采购计量单位
	 */
	public Long getPurUmId() {
		return purUmId;
	}

	/**
	 * 设置物料主文件.采购计量单位
	 *
	 * @param argPurUmId 物料主文件.采购计量单位
	 */
	public void setPurUmId(Long argPurUmId) {
		purUmId = argPurUmId;
	}

	/**
	 * 取得采购订单明细表.流水号
	 *
	 * @return 采购订单明细表.流水号
	 */
	public Long getOrderDetailsId() {
		return orderDetailsId;
	}

	/**
	 * 设置采购订单明细表.流水号
	 *
	 * @param argOrderDetailsId 采购订单明细表.流水号
	 */
	public void setOrderDetailsId(Long argOrderDetailsId) {
		orderDetailsId = argOrderDetailsId;
	}

	/**
	 * 取得物料主文件.规格型号
	 *
	 * @return 物料主文件.规格型号
	 */
	public String getSpecNo() {
		return specNo;
	}

	/**
	 * 设置物料主文件.规格型号
	 *
	 * @param argSpecNo 物料主文件.规格型号
	 */
	public void setSpecNo(String argSpecNo) {
		specNo = argSpecNo;
	}

	/**
	 * 取得采购订单明细表.采购数量
	 *
	 * @return 采购订单明细表.采购数量
	 */
	public Double getPurQty() {
		return purQty;
	}

	/**
	 * 设置采购订单明细表.采购数量
	 *
	 * @param argPurQty 采购订单明细表.采购数量
	 */
	public void setPurQty(Double argPurQty) {
		purQty = argPurQty;
	}

	/**
	 * 取得物料主文件.是否免检
	 *
	 * @return 物料主文件.是否免检
	 */
	public String getQaControlFlag() {
		return qaControlFlag;
	}

	/**
	 * 设置物料主文件.是否免检
	 *
	 * @param argQaControlFlag 物料主文件.是否免检
	 */
	public void setQaControlFlag(String argQaControlFlag) {
		qaControlFlag = argQaControlFlag;
	}

	/**
	 * 取得物资需求计划明细表.申请单编号/需求计划表头流水号
	 *
	 * @return 物资需求计划明细表.申请单编号/需求计划表头流水号
	 */
	public Long getRequirementHeadId() {
		return requirementHeadId;
	}

	/**
	 * 设置物资需求计划明细表.申请单编号/需求计划表头流水号
	 *
	 * @param argRequirementHeadId 物资需求计划明细表.申请单编号/需求计划表头流水号
	 */
	public void setRequirementHeadId(Long argRequirementHeadId) {
		requirementHeadId = argRequirementHeadId;
	}

	/**
	 * 取得采购订单表.备注
	 *
	 * @return 采购订单表.备注
	 */
	public String getOrderMemo() {
		return orderMemo;
	}

	/**
	 * 设置采购订单表.备注
	 *
	 * @param argOrderMemo 采购订单表.备注
	 */
	public void setOrderMemo(String argOrderMemo) {
		orderMemo = argOrderMemo;
	}

	/**
	 * 取得采购人员
	 *
	 * @return 采购人员
	 */
	public String getBuyer() {
		return buyer;
	}

	/**
	 * 设置采购人员
	 *
	 * @param argBuyer 采购人员
	 */
	public void setBuyer(String argBuyer) {
		buyer = argBuyer;
	}

	/**
	 * 取得采购订单明细表.交期
	 *
	 * @return 采购订单明细表.交期
	 */
	public Date getOrderDetailsDueDate() {
		return orderDetailsDueDate;
	}

	/**
	 * 设置采购订单明细表.交期
	 *
	 * @param argOrderDetailsDueDate 采购订单明细表.交期
	 */
	public void setOrderDetailsDueDate(Date argOrderDetailsDueDate) {
		orderDetailsDueDate = argOrderDetailsDueDate;
	}

	/**
	 * 取得合同编号
	 *
	 * @return 合同编号
	 */
	public String getContractNo() {
		return contractNo;
	}

	/**
	 * 设置合同编号
	 *
	 * @param argContractNo 合同编号
	 */
	public void setContractNo(String argContractNo) {
		contractNo = argContractNo;
	}

	/**
	 * 取得物资需求计划明细表.流水号
	 *
	 * @return 物资需求计划明细表.流水号
	 */
	public Long getRequirementDetailId() {
		return requirementDetailId;
	}

	/**
	 * 设置物资需求计划明细表.流水号
	 *
	 * @param argRequirementDetailId 物资需求计划明细表.流水号
	 */
	public void setRequirementDetailId(Long argRequirementDetailId) {
		requirementDetailId = argRequirementDetailId;
	}

	/**
	 * 取得采购订单表.税率
	 *
	 * @return 采购订单表.税率
	 */
	public Double getOrderTaxRate() {
		return orderTaxRate;
	}

	/**
	 * 设置采购订单表.税率
	 *
	 * @param argOrderTaxRate 采购订单表.税率
	 */
	public void setOrderTaxRate(Double argOrderTaxRate) {
		orderTaxRate = argOrderTaxRate;
	}

	/**
	 * 取得订单编号
	 *
	 * @return 订单编号
	 */
	public String getPurNo() {
		return purNo;
	}

	/**
	 * 设置订单编号
	 *
	 * @param argPurNo 订单编号
	 */
	public void setPurNo(String argPurNo) {
		purNo = argPurNo;
	}

	/**
	 * 取得币别ID
	 *
	 * @return 币别ID
	 */
	public Long getCurrencyId() {
		return currencyId;
	}

	/**
	 * 设置币别ID
	 *
	 * @param argCurrencyId 币别ID
	 */
	public void setCurrencyId(Long argCurrencyId) {
		currencyId = argCurrencyId;
	}

	/**
	 * 取得采购订单表.流水号
	 *
	 * @return 采购订单表.流水号
	 */
	public Long getOrderId() {
		return orderId;
	}

	/**
	 * 设置采购订单表.流水号
	 *
	 * @param argOrderId 采购订单表.流水号
	 */
	public void setOrderId(Long argOrderId) {
		orderId = argOrderId;
	}

	/**
	 * 取得采购订单明细表.已收数量
	 *
	 * @return 采购订单明细表.已收数量
	 */
	public Double getRcvQty() {
		return rcvQty;
	}

	/**
	 * 设置采购订单明细表.已收数量
	 *
	 * @param argRcvQty 采购订单明细表.已收数量
	 */
	public void setRcvQty(Double argRcvQty) {
		rcvQty = argRcvQty;
	}

	/**
	 * 取得是否为需求单的采购单明细
	 *
	 * @return 是否为需求单的采购单明细
	 */
	public Double getPurOrderDetailsId() {
		return purOrderDetailsId;
	}

	/**
	 * 设置是否为需求单的采购单明细
	 *
	 * @param argPurOrderDetailsId 是否为需求单的采购单明细
	 */
	public void setPurOrderDetailsId(Double argPurOrderDetailsId) {
		purOrderDetailsId = argPurOrderDetailsId;
	}

	/**
	 * 取得物料主文件.材质/参数
	 *
	 * @return 物料主文件.材质/参数
	 */
	public String getParameter() {
		return parameter;
	}

	/**
	 * 设置物料主文件.材质/参数
	 *
	 * @param argParameter 物料主文件.材质/参数
	 */
	public void setParameter(String argParameter) {
		parameter = argParameter;
	}

	/**
	 * 取得汇率.汇率
	 *
	 * @return 汇率.汇率
	 */
	public Double getRate() {
		return rate;
	}

	/**
	 * 设置汇率.汇率
	 *
	 * @param argRate 汇率.汇率
	 */
	public void setRate(Double argRate) {
		rate = argRate;
	}

	/**
	 * 取得采购订单表.交期
	 *
	 * @return 采购订单表.交期
	 */
	public Date getOrderDueDate() {
		return orderDueDate;
	}

	/**
	 * 设置采购订单表.交期
	 *
	 * @param argOrderDueDate 采购订单表.交期
	 */
	public void setOrderDueDate(Date argOrderDueDate) {
		orderDueDate = argOrderDueDate;
	}

	/**
	 * 取得物料主文件.编码
	 *
	 * @return 物料主文件.编码
	 */
	public Long getMaterialId() {
		return materialId;
	}

	/**
	 * 设置物料主文件.编码
	 *
	 * @param argMaterialId 物料主文件.编码
	 */
	public void setMaterialId(Long argMaterialId) {
		materialId = argMaterialId;
	}

	/**
	 * 取得采购订单明细表.备注
	 *
	 * @return 采购订单明细表.备注
	 */
	public String getOrderDetailsMemo() {
		return orderDetailsMemo;
	}

	/**
	 * 设置采购订单明细表.备注
	 *
	 * @param argOrderDetailsMemo 采购订单明细表.备注
	 */
	public void setOrderDetailsMemo(String argOrderDetailsMemo) {
		orderDetailsMemo = argOrderDetailsMemo;
	}

	/**
	 * 取得采购订单与需求计划关联表.从计划分拆或合并的数量
	 *
	 * @return 采购订单与需求计划关联表.从计划分拆或合并的数量
	 */
	public Double getNeedQty() {
		return needQty;
	}

	/**
	 * 设置采购订单与需求计划关联表.从计划分拆或合并的数量
	 *
	 * @param argNeedQty 采购订单与需求计划关联表.从计划分拆或合并的数量
	 */
	public void setNeedQty(Double argNeedQty) {
		needQty = argNeedQty;
	}

	/**
	 * 取得采购订单与需求计划关联表．流水号
	 *
	 * @return 采购订单与需求计划关联表．流水号
	 */
	public Long getPlanOrderId() {
		return planOrderId;
	}

	/**
	 * 设置采购订单与需求计划关联表．流水号
	 *
	 * @param argPlanOrderId 采购订单与需求计划关联表．流水号
	 */
	public void setPlanOrderId(Long argPlanOrderId) {
		planOrderId = argPlanOrderId;
	}

	/**
	 * 取得订单状态
	 *
	 * @return 订单状态
	 */
	public String getPurStatus() {
		return purStatus;
	}

	/**
	 * 设置订单状态
	 *
	 * @param argPurStatus 订单状态
	 */
	public void setPurStatus(String argPurStatus) {
		purStatus = argPurStatus;
	}

	/**
	 * 取得物料主文件.最大库存量
	 *
	 * @return 物料主文件.最大库存量
	 */
	public Double getMaxStock() {
		return maxStock;
	}

	/**
	 * 设置物料主文件.最大库存量
	 *
	 * @param argMaxStock 物料主文件.最大库存量
	 */
	public void setMaxStock(Double argMaxStock) {
		maxStock = argMaxStock;
	}

	/**
	 * 取得物资需求计划明细表.已生成采购的数量
	 *
	 * @return 物资需求计划明细表.已生成采购的数量
	 */
	public Double getPlanPurQty() {
		return planPurQty;
	}

	/**
	 * 设置物资需求计划明细表.已生成采购的数量
	 *
	 * @param argPlanPurQty 物资需求计划明细表.已生成采购的数量
	 */
	public void setPlanPurQty(Double argPlanPurQty) {
		planPurQty = argPlanPurQty;
	}

	/**
	 * 取得物料主文件.名称
	 *
	 * @return 物料主文件.名称
	 */
	public String getMaterialName() {
		return materialName;
	}

	/**
	 * 设置物料主文件.名称
	 *
	 * @param argMaterialName 物料主文件.名称
	 */
	public void setMaterialName(String argMaterialName) {
		materialName = argMaterialName;
	}

	/**
	 * 取得询价明细表.报价
	 *
	 * @return 询价明细表.报价
	 */
	public Double getQuotedPrice() {
		return quotedPrice;
	}

	/**
	 * 设置询价明细表.报价
	 *
	 * @param argQuotedPrice 询价明细表.报价
	 */
	public void setQuotedPrice(Double argQuotedPrice) {
		quotedPrice = argQuotedPrice;
	}

	/**
	 * 取得采购员维护表.人员名称
	 *
	 * @return 采购员维护表.人员名称
	 */
	public String getBuyerName() {
		return buyerName;
	}

	/**
	 * 设置采购员维护表.人员名称
	 *
	 * @param argBuyerName 采购员维护表.人员名称
	 */
	public void setBuyerName(String argBuyerName) {
		buyerName = argBuyerName;
	}

	/**
	 * 取得供应商表.供应商全称
	 *
	 * @return 供应商表.供应商全称
	 */
	public String getSupplyName() {
		return supplyName;
	}

	/**
	 * 设置供应商表.供应商全称
	 *
	 * @param argSupplyName 供应商表.供应商全称
	 */
	public void setSupplyName(String argSupplyName) {
		supplyName = argSupplyName;
	}

	/**
	 * 取得物资需求计划明细表.核准数量
	 *
	 * @return 物资需求计划明细表.核准数量
	 */
	public Double getApprovedQty() {
		return approvedQty;
	}

	/**
	 * 设置物资需求计划明细表.核准数量
	 *
	 * @param argApprovedQty 物资需求计划明细表.核准数量
	 */
	public void setApprovedQty(Double argApprovedQty) {
		approvedQty = argApprovedQty;
	}

	/**
	 * 取得采购订单明细表.税率
	 *
	 * @return 采购订单明细表.税率
	 */
	public Double getOrderDetailsTaxRate() {
		return orderDetailsTaxRate;
	}

	/**
	 * 设置采购订单明细表.税率
	 *
	 * @param argOrderDetailsTaxRate 采购订单明细表.税率
	 */
	public void setOrderDetailsTaxRate(Double argOrderDetailsTaxRate) {
		orderDetailsTaxRate = argOrderDetailsTaxRate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public String getLastModifiedByName() {
		return lastModifiedByName;
	}

	public void setLastModifiedByName(String lastModifiedByName) {
		this.lastModifiedByName = lastModifiedByName;
	}
	public String getStockUmName() {
		return stockUmName;
	}

	public void setStockUmName(String stockUmName) {
		this.stockUmName = stockUmName;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getSbDeptName() {
		return sbDeptName;
	}

	public void setSbDeptName(String sbDeptName) {
		this.sbDeptName = sbDeptName;
	}

	
}
