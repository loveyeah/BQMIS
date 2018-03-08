package power.ejb.manage.contract.form;

import java.util.Date;

public class PaymentPlanForm implements java.io.Serializable{
	//付款计划ID
	private Long paymentId;
	//合同ID
	private Long conId;
	//付款说明
	private String paymentMoment;
	//付款金额
	private Double payPrice;
	//付款日期
	private String payDate;
	//备注
	private String memo;
	//修改人工号
	private String lastModifiedBy;
	//修改时间
	private String lastModifiedDate;
	//企业编码
	private String enterpriseCode;
	//是否有效
	private String isUse;
	//付款状态
	private String payStatu;
	//付款比例
	private String payRate;
	//币别
	private String currencyName;
	//修改人名字
	private String lastModifyName;
	//付款总金额
	private Double actAmount;
	//add bjxu
	//经办人名
	private String  operateName;
	//经办人编码
	private String operateBy;
	
	public String getLastModifyName() {
		return lastModifyName;
	}
	public void setLastModifyName(String lastModifyName) {
		this.lastModifyName = lastModifyName;
	}
	public Long getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}
	public Long getConId() {
		return conId;
	}
	public void setConId(Long conId) {
		this.conId = conId;
	}
	public String getPaymentMoment() {
		return paymentMoment;
	}
	public void setPaymentMoment(String paymentMoment) {
		this.paymentMoment = paymentMoment;
	}
	public Double getPayPrice() {
		return payPrice;
	}
	public void setPayPrice(Double payPrice) {
		this.payPrice = payPrice;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
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
	public String getPayStatu() {
		return payStatu;
	}
	public void setPayStatu(String payStatu) {
		this.payStatu = payStatu;
	}
	public String getPayRate() {
		return payRate;
	}
	public void setPayRate(String payRate) {
		this.payRate = payRate;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	public Double getActAmount() {
		return actAmount;
	}
	public void setActAmount(Double actAmount) {
		this.actAmount = actAmount;
	}
	public String getOperateName() {
		return operateName;
	}
	public void setOperateName(String operateName) {
		this.operateName = operateName;
	}
	public String getOperateBy() {
		return operateBy;
	}
	public void setOperateBy(String operateBy) {
		this.operateBy = operateBy;
	}
}
