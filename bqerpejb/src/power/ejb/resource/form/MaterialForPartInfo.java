package power.ejb.resource.form;

public class MaterialForPartInfo implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private Long materialId;
	private String materialNo;
	private String materialName;
	private String materialClassName;
	private String specNo;
	private String parameter;
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
	 * @return the materialClassName
	 */
	public String getMaterialClassName() {
		return materialClassName;
	}
	/**
	 * @param materialClassName the materialClassName to set
	 */
	public void setMaterialClassName(String materialClassName) {
		this.materialClassName = materialClassName;
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
}
