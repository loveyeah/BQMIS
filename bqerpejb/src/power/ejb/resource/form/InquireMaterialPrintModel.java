package power.ejb.resource.form;

public class InquireMaterialPrintModel {
	
	private String materialName;
	private String specNo;
	private String parameter;
	private String factory;
	private String stockUmName;
	private String approvedQty;
	
	private String memo;// add by liuyi 20100406 
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
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
	 * @return the approvedQty
	 */
	public String getApprovedQty() {
		return approvedQty;
	}
	/**
	 * @param approvedQty the approvedQty to set
	 */
	public void setApprovedQty(String approvedQty) {
		this.approvedQty = approvedQty;
	}

}
