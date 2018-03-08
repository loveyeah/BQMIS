package power.ejb.manage.contract.form;

import java.util.Date;

public class BalinvioceForm implements java.io.Serializable{
	
	private Long invoiceNo;
	private Long balanceId;
	private String invoiceCode;
	private String invoiceId;
	private String invoiceDate;
	private String invoiceName;
	private Double invoicePrice;
	private String drawerBy;
	private String operateBy;
	private String memo;
	private String enterpriseCode;
	private String isUse;
	private String drawerName;
	private String operateName;
	public Long getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(Long invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public Long getBalanceId() {
		return balanceId;
	}
	public void setBalanceId(Long balanceId) {
		this.balanceId = balanceId;
	}
	public String getInvoiceCode() {
		return invoiceCode;
	}
	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}
	public String getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getInvoiceName() {
		return invoiceName;
	}
	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}
	public Double getInvoicePrice() {
		return invoicePrice;
	}
	public void setInvoicePrice(Double invoicePrice) {
		this.invoicePrice = invoicePrice;
	}
	public String getDrawerBy() {
		return drawerBy;
	}
	public void setDrawerBy(String drawerBy) {
		this.drawerBy = drawerBy;
	}
	public String getOperateBy() {
		return operateBy;
	}
	public void setOperateBy(String operateBy) {
		this.operateBy = operateBy;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getEnterpriseCode() {
		return enterpriseCode;
	}
	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}
	public String getIsUse() {
		return isUse;
	}
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	public String getDrawerName() {
		return drawerName;
	}
	public void setDrawerName(String drawerName) {
		this.drawerName = drawerName;
	}
	public String getOperateName() {
		return operateName;
	}
	public void setOperateName(String operateName) {
		this.operateName = operateName;
	}
}
