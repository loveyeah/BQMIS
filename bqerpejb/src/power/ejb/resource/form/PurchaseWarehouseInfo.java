package power.ejb.resource.form;

import java.util.Date;

public class PurchaseWarehouseInfo {
	/**  到货登记/验收表流水号  */
	private	Long id;
	/** 到货登记/验收表单据编号 */
	private String arrivalNo;
	/** 到货登记/验收表订单编号 */
	private String purNo;
	/** 到货登记/验收表合同编号 */
	private String contractNo;
	/** 到货登记/验收表操作日期 */
	private String operateDate;
	/** 供应商表供应商编号 */
	private String supplier;
	/** 供应商表供应商全称 */
	private String supplyName;
	/** 到货登记/验收表备注 */
	private String memo;
	/** 登陆者名字 */
	private String loginName;
	/** 到货登记/验收表操作日期 */
	private String operateDate2;
	
	//add By ywliu 090511 
	/** 采购员 */
	private String buyerName;
	/** 填写日期 */
	private String entryDate;
	/** 入库日期 */
	private String arrivalDate;
	/** 发票号 */
	private String invoiceNo;
	/** 仓库号 */
	private String whsNo;
	/** 仓库名称 */
	private String whsName;
	
	/** 仓库对应的所有物资 add by liuyi 20100430 仓库以 , 分割，此为 ;分割 */
	private String allMetailIds;
	/** 仓库对应的 需求计划为固定资产类的物资 add by liuyi 20100430  */
	private String gdMetailIds;
	
	/** 蓝单(红单)数量 用于判断是蓝单还是红单 */
	private String rcvQty;
	/** 关联到货单号 */
	private String relationArrivalNo;
	
	/**
	 * @return the buyerName
	 */
	public String getBuyerName() {
		return buyerName;
	}
	/**
	 * @param buyerName the buyerName to set
	 */
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	/**
	 * @return the entryDate
	 */
	public String getEntryDate() {
		return entryDate;
	}
	/**
	 * @param entryDate the entryDate to set
	 */
	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}
	/**
	 * @return the arrivalDate
	 */
	public String getArrivalDate() {
		return arrivalDate;
	}
	/**
	 * @param arrivalDate the arrivalDate to set
	 */
	public void setArrivalDate(String arrivalDate) {
		this.arrivalDate = arrivalDate;
	}
	/**
	 * @return the invoiceNo
	 */
	public String getInvoiceNo() {
		return invoiceNo;
	}
	/**
	 * @param invoiceNo the invoiceNo to set
	 */
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the arrivalNo
	 */
	public String getArrivalNo() {
		return arrivalNo;
	}
	/**
	 * @param arrivalNo the arrivalNo to set
	 */
	public void setArrivalNo(String arrivalNo) {
		this.arrivalNo = arrivalNo;
	}
	/**
	 * @return the purNo
	 */
	public String getPurNo() {
		return purNo;
	}
	/**
	 * @param purNo the purNo to set
	 */
	public void setPurNo(String purNo) {
		this.purNo = purNo;
	}
	/**
	 * @return the contractNo
	 */
	public String getContractNo() {
		return contractNo;
	}
	/**
	 * @param contractNo the contractNo to set
	 */
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	/**
	 * @return the operateDate
	 */
	public String getOperateDate() {
		return operateDate;
	}
	/**
	 * @param operateDate the operateDate to set
	 */
	public void setOperateDate(String operateDate) {
		this.operateDate = operateDate;
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
	 * @return the supplyName
	 */
	public String getSupplyName() {
		return supplyName;
	}
	/**
	 * @param supplyName the supplyName to set
	 */
	public void setSupplyName(String supplyName) {
		this.supplyName = supplyName;
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
	 * @return the loginName
	 */
	public String getLoginName() {
		return loginName;
	}
	/**
	 * @param loginName the loginName to set
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	/**
	 * @return the operateDate2
	 */
	public String getOperateDate2() {
		return operateDate2;
	}
	/**
	 * @param operateDate2 the operateDate2 to set
	 */
	public void setOperateDate2(String operateDate2) {
		this.operateDate2 = operateDate2;
	}
	public String getWhsNo() {
		return whsNo;
	}
	public void setWhsNo(String whsNo) {
		this.whsNo = whsNo;
	}
	public String getWhsName() {
		return whsName;
	}
	public void setWhsName(String whsName) {
		this.whsName = whsName;
	}
	/**
	 * @return the rcvQty
	 */
	public String getRcvQty() {
		return rcvQty;
	}
	/**
	 * @param rcvQty the rcvQty to set
	 */
	public void setRcvQty(String rcvQty) {
		this.rcvQty = rcvQty;
	}
	/**
	 * @return the relationArrivalNo
	 */
	public String getRelationArrivalNo() {
		return relationArrivalNo;
	}
	/**
	 * @param relationArrivalNo the relationArrivalNo to set
	 */
	public void setRelationArrivalNo(String relationArrivalNo) {
		this.relationArrivalNo = relationArrivalNo;
	}
	/**
	 * @return the allMetailIds
	 */
	public String getAllMetailIds() {
		return allMetailIds;
	}
	/**
	 * @param allMetailIds the allMetailIds to set
	 */
	public void setAllMetailIds(String allMetailIds) {
		this.allMetailIds = allMetailIds;
	}
	/**
	 * @return the gdMetailIds
	 */
	public String getGdMetailIds() {
		return gdMetailIds;
	}
	/**
	 * @param gdMetailIds the gdMetailIds to set
	 */
	public void setGdMetailIds(String gdMetailIds) {
		this.gdMetailIds = gdMetailIds;
	}

}
