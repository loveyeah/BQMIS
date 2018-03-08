/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.resource;

import java.util.List;

/**
 * 采购表
 * @author zhujie 
 */
public class PurchaseBean {

	/** 制单人 */
    private String createMan="";
    /** 当前日期 */
    private String nowDate="";
    /** 采购员 */
    private String buyer="";
    /** 合同号 */
    private String contractNo="";
    /** 供应商编号 */
    private String supplier="";
    /** 供应商名称 */
    private String clientName="";
    /** 币别 */
    private String currencyType="";
    /** 交期 */
    private String dueDate="";
    /** 税率 */
    private String taxRate="";
    /** 汇率 */
    private String exchangeRate="";
    /** 备注 */
    private String meno="";
    /** 采购表数据 */
    private List<PurchaseListBean> purchaseList;
	/**
	 * @return the createMan
	 */
	public String getCreateMan() {
		return createMan;
	}
	/**
	 * @param createMan the createMan to set
	 */
	public void setCreateMan(String createMan) {
		this.createMan = createMan;
	}
	/**
	 * @return the nowDate
	 */
	public String getNowDate() {
		return nowDate;
	}
	/**
	 * @param nowDate the nowDate to set
	 */
	public void setNowDate(String nowDate) {
		this.nowDate = nowDate;
	}
	/**
	 * @return the buyer
	 */
	public String getBuyer() {
		return buyer;
	}
	/**
	 * @param buyer the buyer to set
	 */
	public void setBuyer(String buyer) {
		this.buyer = buyer;
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
	 * @return the clientName
	 */
	public String getClientName() {
		return clientName;
	}
	/**
	 * @param clientName the clientName to set
	 */
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	/**
	 * @return the currencyType
	 */
	public String getCurrencyType() {
		return currencyType;
	}
	/**
	 * @param currencyType the currencyType to set
	 */
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	/**
	 * @return the dueDate
	 */
	public String getDueDate() {
		return dueDate;
	}
	/**
	 * @param dueDate the dueDate to set
	 */
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	/**
	 * @return the taxRate
	 */
	public String getTaxRate() {
		return taxRate;
	}
	/**
	 * @param taxRate the taxRate to set
	 */
	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}
	/**
	 * @return the exchangeRate
	 */
	public String getExchangeRate() {
		return exchangeRate;
	}
	/**
	 * @param exchangeRate the exchangeRate to set
	 */
	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
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
	 * @return the purchaseList
	 */
	public List<PurchaseListBean> getPurchaseList() {
		return purchaseList;
	}
	/**
	 * @param purchaseList the purchaseList to set
	 */
	public void setPurchaseList(List<PurchaseListBean> purchaseList) {
		this.purchaseList = purchaseList;
	}
}
