package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCNativePlace entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_NATIVE_PLACE")
public class HrCNativePlace implements java.io.Serializable {

	// Fields

	private Long nativePlaceId;
	private String nativePlaceName;
	private String isUse;
	private String retrieveCode;
	private Long orderBy;
	private String enterpriseCode;
	private String lastModifiedBy;
	private Date lastModifiedDate;

	// Constructors

	/** default constructor */
	public HrCNativePlace() {
	}

	/** minimal constructor */
	public HrCNativePlace(Long nativePlaceId) {
		this.nativePlaceId = nativePlaceId;
	}

	/** full constructor */
	public HrCNativePlace(Long nativePlaceId, String nativePlaceName,
			String isUse, String retrieveCode, Long orderBy,
			String enterpriseCode, String lastModifiedBy, Date lastModifiedDate) {
		this.nativePlaceId = nativePlaceId;
		this.nativePlaceName = nativePlaceName;
		this.isUse = isUse;
		this.retrieveCode = retrieveCode;
		this.orderBy = orderBy;
		this.enterpriseCode = enterpriseCode;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
	}

	// Property accessors
	@Id
	@Column(name = "NATIVE_PLACE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getNativePlaceId() {
		return this.nativePlaceId;
	}

	public void setNativePlaceId(Long nativePlaceId) {
		this.nativePlaceId = nativePlaceId;
	}

	@Column(name = "NATIVE_PLACE_NAME", length = 100)
	public String getNativePlaceName() {
		return this.nativePlaceName;
	}

	public void setNativePlaceName(String nativePlaceName) {
		this.nativePlaceName = nativePlaceName;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "RETRIEVE_CODE", length = 20)
	public String getRetrieveCode() {
		return this.retrieveCode;
	}

	public void setRetrieveCode(String retrieveCode) {
		this.retrieveCode = retrieveCode;
	}

	@Column(name = "ORDER_BY", precision = 10, scale = 0)
	public Long getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}

	@Column(name = "ENTERPRISE_CODE", length = 10)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "LAST_MODIFIED_BY", length = 16)
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

}