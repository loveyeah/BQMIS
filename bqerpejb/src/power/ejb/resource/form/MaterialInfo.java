package power.ejb.resource.form;

public class MaterialInfo implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	// 物料编码  
	private String materialNo;
	// 物料名称  
    private String materialName;
	// 物料类别（名称）
	private String className;
	// 规格型号
    private String specNo;
    // 材质/参数
    private String parameter;
    // 存货计量单位STOCK_UM_ID
    private String stockUmName;
    // 缺省仓库名称
	private String whsName;
	// 缺省库位名称
	private String locationName; 
	// 流水号
	private String materialId;
	//物料类别ID add by drdu 090624
	private String materialClassId;
	
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
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getSpecNo() {
		return specNo;
	}
	public void setSpecNo(String specNo) {
		this.specNo = specNo;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public String getStockUmName() {
		return stockUmName;
	}
	public void setStockUmName(String stockUmName) {
		this.stockUmName = stockUmName;
	}
	public String getWhsName() {
		return whsName;
	}
	public void setWhsName(String whsName) {
		this.whsName = whsName;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getMaterialId() {
		return materialId;
	}
	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}
	public String getMaterialClassId() {
		return materialClassId;
	}
	public void setMaterialClassId(String materialClassId) {
		this.materialClassId = materialClassId;
	}
	

}
