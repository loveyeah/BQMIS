package power.ejb.resource;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * InvCTempMaterial entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "INV_C_TEMP_MATERIAL")
public class InvCTempMaterial implements java.io.Serializable {

	// Fields

	private Long tempId;
	private String materialName;
	private String specNo;
	private String parameter;
	private Long stockUmId;
	private String defaultWhsNo;
	private String defaultLocationNo;
	private Long maertialClassId;
	private String factory;
	private Double actPrice;
	private Date checkDate;
	private String checkBy;
	private String telNo;
	private String memo;
	private Long statusId;
	private String materialNo;
	private String approveBy;
	private Date approveDate;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String enterpriseCode;
	private String isUse;
	private String backReason;

	// Constructors

	/** default constructor */
	public InvCTempMaterial() {
	}

	/** minimal constructor */
	public InvCTempMaterial(Long tempId) {
		this.tempId = tempId;
	}

	/** full constructor */
	public InvCTempMaterial(Long tempId, String materialName, String specNo,
			String parameter, Long stockUmId, String defaultWhsNo,
			String defaultLocationNo, Long maertialClassId, String factory,
			Double actPrice, Date checkDate, String checkBy, String telNo,
			String memo, Long statusId, String materialNo, String approveBy,
			Date approveDate, String lastModifiedBy, Date lastModifiedDate,
			String enterpriseCode, String isUse) {
		this.tempId = tempId;
		this.materialName = materialName;
		this.specNo = specNo;
		this.parameter = parameter;
		this.stockUmId = stockUmId;
		this.defaultWhsNo = defaultWhsNo;
		this.defaultLocationNo = defaultLocationNo;
		this.maertialClassId = maertialClassId;
		this.factory = factory;
		this.actPrice = actPrice;
		this.checkDate = checkDate;
		this.checkBy = checkBy;
		this.telNo = telNo;
		this.memo = memo;
		this.statusId = statusId;
		this.materialNo = materialNo;
		this.approveBy = approveBy;
		this.approveDate = approveDate;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "TEMP_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getTempId() {
		return this.tempId;
	}

	public void setTempId(Long tempId) {
		this.tempId = tempId;
	}

	@Column(name = "MATERIAL_NAME", length = 100)
	public String getMaterialName() {
		return this.materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	@Column(name = "SPEC_NO", length = 50)
	public String getSpecNo() {
		return this.specNo;
	}

	public void setSpecNo(String specNo) {
		this.specNo = specNo;
	}

	@Column(name = "PARAMETER", length = 100)
	public String getParameter() {
		return this.parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	@Column(name = "STOCK_UM_ID", precision = 10, scale = 0)
	public Long getStockUmId() {
		return this.stockUmId;
	}

	public void setStockUmId(Long stockUmId) {
		this.stockUmId = stockUmId;
	}

	@Column(name = "DEFAULT_WHS_NO", length = 10)
	public String getDefaultWhsNo() {
		return this.defaultWhsNo;
	}

	public void setDefaultWhsNo(String defaultWhsNo) {
		this.defaultWhsNo = defaultWhsNo;
	}

	@Column(name = "DEFAULT_LOCATION_NO", length = 10)
	public String getDefaultLocationNo() {
		return this.defaultLocationNo;
	}

	public void setDefaultLocationNo(String defaultLocationNo) {
		this.defaultLocationNo = defaultLocationNo;
	}

	@Column(name = "MAERTIAL_CLASS_ID", precision = 10, scale = 0)
	public Long getMaertialClassId() {
		return this.maertialClassId;
	}

	public void setMaertialClassId(Long maertialClassId) {
		this.maertialClassId = maertialClassId;
	}

	@Column(name = "FACTORY", length = 100)
	public String getFactory() {
		return this.factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	@Column(name = "ACT_PRICE", precision = 18, scale = 5)
	public Double getActPrice() {
		return this.actPrice;
	}

	public void setActPrice(Double actPrice) {
		this.actPrice = actPrice;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CHECK_DATE", length = 7)
	public Date getCheckDate() {
		return this.checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	@Column(name = "CHECK_BY", length = 30)
	public String getCheckBy() {
		return this.checkBy;
	}

	public void setCheckBy(String checkBy) {
		this.checkBy = checkBy;
	}

	@Column(name = "TEL_NO", length = 30)
	public String getTelNo() {
		return this.telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	@Column(name = "MEMO", length = 200)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "STATUS_ID", precision = 10, scale = 0)
	public Long getStatusId() {
		return this.statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	@Column(name = "MATERIAL_NO", length = 30)
	public String getMaterialNo() {
		return this.materialNo;
	}

	public void setMaterialNo(String materialNo) {
		this.materialNo = materialNo;
	}

	@Column(name = "APPROVE_BY", length = 30)
	public String getApproveBy() {
		return this.approveBy;
	}

	public void setApproveBy(String approveBy) {
		this.approveBy = approveBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APPROVE_DATE", length = 7)
	public Date getApproveDate() {
		return this.approveDate;
	}

	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}

	@Column(name = "LAST_MODIFIED_BY", length = 30)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIED_DATE", length = 7)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
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

	@Column(name = "BACK_REASON", length = 200)
	public String getBackReason() {
		return backReason;
	}

	public void setBackReason(String backReason) {
		this.backReason = backReason;
	}

}