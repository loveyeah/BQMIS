package power.ejb.resource.form;

import java.util.Date;

public class SupplierQueryInfo implements java.io.Serializable {

	/** 合作伙伴档案表 */
	/** 合作伙伴ID */
	private Long supplierId;
	/** 合作伙伴编号 */
	private String supplier;
	/** 合作伙伴名称 */
	private String supplyName;
	/** 责任人 */
	private String principal;
	/** 注册资金 */
	private String registeredCapital;
	/** 合作伙伴电话 */
	private String companyTel;
	/** 是否可用 */
	private String isUse;

	/** 合作伙伴性质 */
	/** 合作伙伴性质名称 */
	private String companyTypeDesc;

	/** 合作伙伴资质登记表 */
	/** 资质登记ID */
	private Long id;
	/** 合作伙伴ID */
	private Long clientId;
	/** 资料齐备否 */
	private String ifQb;
	/** 资质证书编号 */
	private String qualificationCode;
	/** 资质证书名称 */
	private String qualificationName;
	/** 证书有效日期 */
	private Date effectiveFromDate;
	/** 证书失效日期 */
	private Date effectiveToDate;
	/** 最后修改时间 */
	private String lastModifyDate;

	/** 采购订单明细表 */
	/** 物料ID */
	private Long materialId;

	/** 物料主文件表 */
	/** 名称 */
	private String materialName;
	/** 规格型号 */
	private String specNo;
	/** 材质/参数 */
	private String parameter;

	/** 物料分类表 */
	/** 名称 */
	private String className;

	/** 资质材料维护表 */
	/** 资质材料 */
	private String aptitudeName;
	/** 资质材料ID */
	private String aptitudeId;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the effectiveFromDate
	 */
	public Date getEffectiveFromDate() {
		return effectiveFromDate;
	}

	/**
	 * @param effectiveFromDate
	 *            the effectiveFromDate to set
	 */
	public void setEffectiveFromDate(Date effectiveFromDate) {
		this.effectiveFromDate = effectiveFromDate;
	}

	/**
	 * @return the effectiveToDate
	 */
	public Date getEffectiveToDate() {
		return effectiveToDate;
	}

	/**
	 * @param effectiveToDate
	 *            the effectiveToDate to set
	 */
	public void setEffectiveToDate(Date effectiveToDate) {
		this.effectiveToDate = effectiveToDate;
	}

	/**
	 * @return the qualificationName
	 */
	public String getQualificationName() {
		return qualificationName;
	}

	/**
	 * @param qualificationName
	 *            the qualificationName to set
	 */
	public void setQualificationName(String qualificationName) {
		this.qualificationName = qualificationName;
	}

	/**
	 * @return the supplier
	 */
	public String getSupplier() {
		return supplier;
	}

	/**
	 * @param supplier
	 *            the supplier to set
	 */
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	/**
	 * @return the supplyName
	 */
	public String getSupplyName() {
		return supplyName;
	}

	/**
	 * @param supplyName
	 *            the supplyName to set
	 */
	public void setSupplyName(String supplyName) {
		this.supplyName = supplyName;
	}

	/**
	 * @return the companyTypeDesc
	 */
	public String getCompanyTypeDesc() {
		return companyTypeDesc;
	}

	/**
	 * @param companyTypeDesc
	 *            the companyTypeDesc to set
	 */
	public void setCompanyTypeDesc(String companyTypeDesc) {
		this.companyTypeDesc = companyTypeDesc;
	}

	/**
	 * @return the principal
	 */
	public String getPrincipal() {
		return principal;
	}

	/**
	 * @param principal
	 *            the principal to set
	 */
	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	/**
	 * @return the registeredCapital
	 */
	public String getRegisteredCapital() {
		return registeredCapital;
	}

	/**
	 * @param registeredCapital
	 *            the registeredCapital to set
	 */
	public void setRegisteredCapital(String registeredCapital) {
		this.registeredCapital = registeredCapital;
	}

	/**
	 * @return the companyTel
	 */
	public String getCompanyTel() {
		return companyTel;
	}

	/**
	 * @param companyTel
	 *            the companyTel to set
	 */
	public void setCompanyTel(String companyTel) {
		this.companyTel = companyTel;
	}

	/**
	 * @return the materialId
	 */
	public Long getMaterialId() {
		return materialId;
	}

	/**
	 * @param materialId
	 *            the materialId to set
	 */
	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}

	/**
	 * @return the materialName
	 */
	public String getMaterialName() {
		return materialName;
	}

	/**
	 * @param materialName
	 *            the materialName to set
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
	 * @param specNo
	 *            the specNo to set
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
	 * @param parameter
	 *            the parameter to set
	 */
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className
	 *            the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return the supplierId
	 */
	public Long getSupplierId() {
		return supplierId;
	}

	/**
	 * @param supplierId
	 *            the supplierId to set
	 */
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	/**
	 * @return the qualificationCode
	 */
	public String getQualificationCode() {
		return qualificationCode;
	}

	/**
	 * @param qualificationCode
	 *            the qualificationCode to set
	 */
	public void setQualificationCode(String qualificationCode) {
		this.qualificationCode = qualificationCode;
	}

	/**
	 * @return the isUse
	 */
	public String getIsUse() {
		return isUse;
	}

	/**
	 * @param isUse the isUse to set
	 */
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	/**
	 * @return the clientId
	 */
	public Long getClientId() {
		return clientId;
	}

	/**
	 * @param clientId the clientId to set
	 */
	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	/**
	 * @return the aptitudeName
	 */
	public String getAptitudeName() {
		return aptitudeName;
	}

	/**
	 * @param aptitudeName the aptitudeName to set
	 */
	public void setAptitudeName(String aptitudeName) {
		this.aptitudeName = aptitudeName;
	}

	/**
	 * @return the ifQb
	 */
	public String getIfQb() {
		return ifQb;
	}

	/**
	 * @param ifQb the ifQb to set
	 */
	public void setIfQb(String ifQb) {
		this.ifQb = ifQb;
	}

	/**
	 * @return the aptitudeId
	 */
	public String getAptitudeId() {
		return aptitudeId;
	}

	/**
	 * @param aptitudeId the aptitudeId to set
	 */
	public void setAptitudeId(String aptitudeId) {
		this.aptitudeId = aptitudeId;
	}

	/**
	 * @return the lastModifyDate
	 */
	public String getLastModifyDate() {
		return lastModifyDate;
	}

	/**
	 * @param lastModifyDate the lastModifyDate to set
	 */
	public void setLastModifyDate(String lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}


}
