package power.ejb.manage.contract.form;

public class MaterialDetailsForm implements java.io.Serializable {
	private String purNo;
	private Long materialId;
	private Double unitPrice;
	private Double purqty;
	private String  memo;
	private String materialName;
	private String specModel;
	private Double total;
	public String getPurNo() {
		return purNo;
	}
	public void setPurNo(String purNo) {
		this.purNo = purNo;
	}
	public Long getMaterialId() {
		return materialId;
	}
	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Double getPurqty() {
		return purqty;
	}
	public void setPurqty(Double purqty) {
		this.purqty = purqty;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public String getSpecModel() {
		return specModel;
	}
	public void setSpecModel(String specModel) {
		this.specModel = specModel;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
}
