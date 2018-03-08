package power.ejb.hr.labor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * HrCLaborMaterial entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_LABOR_MATERIAL")
public class HrCLaborMaterial implements java.io.Serializable {

	// Fields

	private Long laborMaterialId;
	private String laborMaterialName;
	private Long materialCode;
	private String materialName;
	private Long laborClassId;
	private Long unitId;
	private String isSend;
	private String receiveKind;
	private Long orderBy;
	private String searchesCode;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public HrCLaborMaterial() {
	}

	/** minimal constructor */
	public HrCLaborMaterial(Long laborMaterialId) {
		this.laborMaterialId = laborMaterialId;
	}

	/** full constructor */
	public HrCLaborMaterial(Long laborMaterialId, String laborMaterialName,
			Long materialCode, String materialName, Long laborClassId,
			Long unitId, String isSend, String receiveKind, Long orderBy,
			String searchesCode, String enterpriseCode, String isUse) {
		this.laborMaterialId = laborMaterialId;
		this.laborMaterialName = laborMaterialName;
		this.materialCode = materialCode;
		this.materialName = materialName;
		this.laborClassId = laborClassId;
		this.unitId = unitId;
		this.isSend = isSend;
		this.receiveKind = receiveKind;
		this.orderBy = orderBy;
		this.searchesCode = searchesCode;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "LABOR_MATERIAL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getLaborMaterialId() {
		return this.laborMaterialId;
	}

	public void setLaborMaterialId(Long laborMaterialId) {
		this.laborMaterialId = laborMaterialId;
	}

	@Column(name = "LABOR_MATERIAL_NAME", length = 50)
	public String getLaborMaterialName() {
		return this.laborMaterialName;
	}

	public void setLaborMaterialName(String laborMaterialName) {
		this.laborMaterialName = laborMaterialName;
	}

	@Column(name = "MATERIAL_CODE", precision = 10, scale = 0)
	public Long getMaterialCode() {
		return this.materialCode;
	}

	public void setMaterialCode(Long materialCode) {
		this.materialCode = materialCode;
	}

	@Column(name = "MATERIAL_NAME", length = 50)
	public String getMaterialName() {
		return this.materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	@Column(name = "LABOR_CLASS_ID", precision = 10, scale = 0)
	public Long getLaborClassId() {
		return this.laborClassId;
	}

	public void setLaborClassId(Long laborClassId) {
		this.laborClassId = laborClassId;
	}

	@Column(name = "UNIT_ID", precision = 10, scale = 0)
	public Long getUnitId() {
		return this.unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	@Column(name = "IS_SEND", length = 1)
	public String getIsSend() {
		return this.isSend;
	}

	public void setIsSend(String isSend) {
		this.isSend = isSend;
	}

	@Column(name = "RECEIVE_KIND", length = 1)
	public String getReceiveKind() {
		return this.receiveKind;
	}

	public void setReceiveKind(String receiveKind) {
		this.receiveKind = receiveKind;
	}

	@Column(name = "ORDER_BY", precision = 10, scale = 0)
	public Long getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}

	@Column(name = "SEARCHES_CODE", length = 20)
	public String getSearchesCode() {
		return this.searchesCode;
	}

	public void setSearchesCode(String searchesCode) {
		this.searchesCode = searchesCode;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

}