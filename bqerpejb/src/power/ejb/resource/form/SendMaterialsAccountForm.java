package power.ejb.resource.form;
@SuppressWarnings("serial")
public class SendMaterialsAccountForm implements java.io.Serializable{
	
	
	//物料id
	private Long materialId;
	//物料编码
	private String materialNo;
	//物料名称
	private String materialName;
	//规格型号
	private String specNo;
	// 数量
	private Double qty;
	// 单价
	private Double  stdCost;
	// 本期初始
	private Double openBalance;
	// 当前结余
	private Double thisQty;
	// 金额
	private Double price;
	public Long getMaterialId() {
		return materialId;
	}
	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}
	public String getMaterialNo() {
		return materialNo;
	}
	public void setMaterialNo(String materialNo) {
		this.materialNo = materialNo;
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
	public Double getQty() {
		return qty;
	}
	public void setQty(Double qty) {
		this.qty = qty;
	}
	public Double getStdCost() {
		return stdCost;
	}
	public void setStdCost(Double stdCost) {
		this.stdCost = stdCost;
	}
	public Double getOpenBalance() {
		return openBalance;
	}
	public void setOpenBalance(Double openBalance) {
		this.openBalance = openBalance;
	}
	public Double getThisQty() {
		return thisQty;
	}
	public void setThisQty(Double thisQty) {
		this.thisQty = thisQty;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}

}