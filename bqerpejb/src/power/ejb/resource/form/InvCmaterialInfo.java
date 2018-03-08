package power.ejb.resource.form;

import java.util.Date;

/**
 * 替代物料bean
 * @author wujiao
*/
public class InvCmaterialInfo implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	/** 替代物料.流水号*/
	private Long alternateMaterialId;
	/** 替代物料.替代物料id*/
	private Long alterMaterialId;
	/** 替代物料.相对替换数量*/
	private Double qty;
	/** 替代物料.优先级*/
	private Long priority;
	/** 替代物料.有效开始日期*/
	private String effectiveDate;
	/** 替代物料.有效截止日期*/
	private String discontinueDate;
	/** 物料主文件.编码*/
	private String materialNo;
	/** 物料主文件.名称*/
	private String materialName;
	/**
	 * @return the alternateMaterialId
	 */
	public Long getAlternateMaterialId() {
		return alternateMaterialId;
	}
	/**
	 * @param alternateMaterialId the alternateMaterialId to set
	 */
	public void setAlternateMaterialId(Long alternateMaterialId) {
		this.alternateMaterialId = alternateMaterialId;
	}
	/**
	 * @return the alterMaterialId
	 */
	public Long getAlterMaterialId() {
		return alterMaterialId;
	}
	/**
	 * @param alterMaterialId the alterMaterialId to set
	 */
	public void setAlterMaterialId(Long alterMaterialId) {
		this.alterMaterialId = alterMaterialId;
	}
	/**
	 * @return the qty
	 */
	public Double getQty() {
		return qty;
	}
	/**
	 * @param qty the qty to set
	 */
	public void setQty(Double qty) {
		this.qty = qty;
	}
	/**
	 * @return the priority
	 */
	public Long getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(Long priority) {
		this.priority = priority;
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
}
	
