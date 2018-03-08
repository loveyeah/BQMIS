/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.resource.form;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 物料询价管理bean
 * 
 * @author sufeiyu
 */
@SuppressWarnings("serial")
public class PurJQuotationInfo implements java.io.Serializable {

	// Fields

	private Long quotationId;
	private Long materialId;
	private Long unitId;
	private String materialNo;
	private String materialName;
	private String specNo;
	private String parameter;
	private Long supplier;
	private String supplyName;
	private Double  quotedQty;
	private Double  quotedPrice;
	private String currencyName;
	private String effectiveDate;
	private String discontinueDate;
	private String memo;
	private Long quotationCurrency;
	private Long lastModifiedDate;
	private String clientCode;
	private String unitName;
	
	// Constructors

	/** default constructor */
	public PurJQuotationInfo() {
	}
	


	public PurJQuotationInfo(Long quotationId, Long materialId, Long unitId,
			String materialNo, String materialName, String specNo,
			String parameter, Long supplier, String supplyName,
			Double quotedQty, Double quotedPrice, String currencyName,
			String effectiveDate, String discontinueDate, String memo,
			Long quotationCurrency, Long lastModifiedDate, String clientCode,
			String unitName) {
		super();
		this.quotationId = quotationId;
		this.materialId = materialId;
		this.unitId = unitId;
		this.materialNo = materialNo;
		this.materialName = materialName;
		this.specNo = specNo;
		this.parameter = parameter;
		this.supplier = supplier;
		this.supplyName = supplyName;
		this.quotedQty = quotedQty;
		this.quotedPrice = quotedPrice;
		this.currencyName = currencyName;
		this.effectiveDate = effectiveDate;
		this.discontinueDate = discontinueDate;
		this.memo = memo;
		this.quotationCurrency = quotationCurrency;
		this.lastModifiedDate = lastModifiedDate;
		this.clientCode = clientCode;
		this.unitName = unitName;
	}



	/**
	 * @return the quotationId
	 */
	public Long getQuotationId() {
		return quotationId;
	}

	/**
	 * @param quotationId the quotationId to set
	 */
	public void setQuotationId(Long quotationId) {
		this.quotationId = quotationId;
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
	 * @return the supplier
	 */
	public Long getSupplier() {
		return supplier;
	}

	/**
	 * @param supplier the supplier to set
	 */
	public void setSupplier(Long supplier) {
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
	 * @return the quotedQty
	 */
	public Double getQuotedQty() {
		return quotedQty;
	}

	/**
	 * @param quotedQty the quotedQty to set
	 */
	public void setQuotedQty(Double quotedQty) {
		this.quotedQty = quotedQty;
	}

	/**
	 * @return the quotedPrice
	 */
	public Double getQuotedPrice() {
		return quotedPrice;
	}

	/**
	 * @param quotedPrice the quotedPrice to set
	 */
	public void setQuotedPrice(Double quotedPrice) {
		this.quotedPrice = quotedPrice;
	}

	/**
	 * @return the currencyName
	 */
	public String getCurrencyName() {
		return currencyName;
	}

	/**
	 * @param currencyName the currencyName to set
	 */
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	/**
	 * @return the effectiveDate
	 */
	public String getEffectiveDate() {
		return effectiveDate;
	}

	/**
	 * @param effectiveDate the effectiveDate to set
	 */
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	/**
	 * @return the discontinueDate
	 */
	public String getDiscontinueDate() {
		return discontinueDate;
	}

	/**
	 * @param discontinueDate the discontinueDate to set
	 */
	public void setDiscontinueDate(String discontinueDate) {
		this.discontinueDate = discontinueDate;
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
	 * @return the unitId
	 */
	public Long getUnitId() {
		return unitId;
	}

	/**
	 * @param unitId the unitId to set
	 */
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	/**
	 * @return the quotationCurrency
	 */
	public Long getQuotationCurrency() {
		return quotationCurrency;
	}

	/**
	 * @param quotationCurrency the quotationCurrency to set
	 */
	public void setQuotationCurrency(Long quotationCurrency) {
		this.quotationCurrency = quotationCurrency;
	}

	/**
	 * @return the lastModifiedDate
	 */
	public Long getlastModifiedDate() {
		return lastModifiedDate;
	}

	/**
	 * @param lastModifiedDate the lastModifiedDate to set
	 */
	@Temporal(TemporalType.TIMESTAMP)
	public void setlastModifiedDate(Long lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	/**
	 * @return the lastModifiedDate
	 */
	public Long getLastModifiedDate() {
		return lastModifiedDate;
	}

	/**
	 * @param lastModifiedDate the lastModifiedDate to set
	 */
	public void setLastModifiedDate(Long lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	/**
	 * @return the clientCode
	 */
	public String getclientCode() {
		return clientCode;
	}

	/**
	 * @param clientCode the clientCode to set
	 */
	public void setclientCode(String clientCode) {
		this.clientCode = clientCode;
	}



	/**
	 * @return the unitName
	 */
	public String getUnitName() {
		return unitName;
	}



	/**
	 * @param unitName the unitName to set
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
}