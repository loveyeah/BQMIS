package power.ejb.resource;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PurCBuyer entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PUR_C_BUYER")
public class PurCBuyer implements java.io.Serializable {

	// Fields

	private Long id;
	private String buyer;
	private String buyerName;
	private String materialOrClassNo;
	private String isMaterialClass;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public PurCBuyer() {
	}

	/** full constructor */
	public PurCBuyer(Long id, String buyer, String buyerName,
			String materialOrClassNo, String isMaterialClass,
			String lastModifiedBy, Date lastModifiedDate,
			String enterpriseCode, String isUse) {
		this.id = id;
		this.buyer = buyer;
		this.buyerName = buyerName;
		this.materialOrClassNo = materialOrClassNo;
		this.isMaterialClass = isMaterialClass;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "BUYER", nullable = false, length = 16)
	public String getBuyer() {
		return this.buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	@Column(name = "BUYER_NAME", nullable = false, length = 30)
	public String getBuyerName() {
		return this.buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	@Column(name = "MATERIAL_OR_CLASS_NO", nullable = false, length = 30)
	public String getMaterialOrClassNo() {
		return this.materialOrClassNo;
	}

	public void setMaterialOrClassNo(String materialOrClassNo) {
		this.materialOrClassNo = materialOrClassNo;
	}

	@Column(name = "IS_MATERIAL_CLASS", nullable = false, length = 1)
	public String getIsMaterialClass() {
		return this.isMaterialClass;
	}

	public void setIsMaterialClass(String isMaterialClass) {
		this.isMaterialClass = isMaterialClass;
	}

	@Column(name = "LAST_MODIFIED_BY", nullable = false, length = 16)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIED_DATE", nullable = false, length = 7)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Column(name = "ENTERPRISE_CODE", nullable = false, length = 10)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USE", nullable = false, length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

}