package power.ejb.resource;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * InvCLocation entity.
 *
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "INV_C_LOCATION")
public class InvCLocation implements java.io.Serializable {

	// Fields

	private Long locationId;
	private String whsNo;
	private String locationNo;
	private String locationName;
	private String locationDesc;
	private String locationZone;
	private String isAllocatableLocations;
	private String isDefault;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public InvCLocation() {
	}

	/** minimal constructor */
	public InvCLocation(Long locationId, String whsNo, String locationNo,
			String locationName, String lastModifiedBy, Date lastModifiedDate,
			String enterpriseCode, String isUse) {
		this.locationId = locationId;
		this.whsNo = whsNo;
		this.locationNo = locationNo;
		this.locationName = locationName;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	/** full constructor */
	public InvCLocation(Long locationId, String whsNo, String locationNo,
			String locationName, String locationDesc, String locationZone,
			String isAllocatableLocations, String isDefault,
			String lastModifiedBy, Date lastModifiedDate,
			String enterpriseCode, String isUse) {
		this.locationId = locationId;
		this.whsNo = whsNo;
		this.locationNo = locationNo;
		this.locationName = locationName;
		this.locationDesc = locationDesc;
		this.locationZone = locationZone;
		this.isAllocatableLocations = isAllocatableLocations;
		this.isDefault = isDefault;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "LOCATION_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getLocationId() {
		return this.locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	@Column(name = "WHS_NO", nullable = false, length = 10)
	public String getWhsNo() {
		return this.whsNo;
	}

	public void setWhsNo(String whsNo) {
		this.whsNo = whsNo;
	}

	@Column(name = "LOCATION_NO", nullable = false, length = 10)
	public String getLocationNo() {
		return this.locationNo;
	}

	public void setLocationNo(String locationNo) {
		this.locationNo = locationNo;
	}

	@Column(name = "LOCATION_NAME", nullable = false, length = 100)
	public String getLocationName() {
		return this.locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	@Column(name = "LOCATION_DESC", length = 200)
	public String getLocationDesc() {
		return this.locationDesc;
	}

	public void setLocationDesc(String locationDesc) {
		this.locationDesc = locationDesc;
	}

	@Column(name = "LOCATION_ZONE", length = 10)
	public String getLocationZone() {
		return this.locationZone;
	}

	public void setLocationZone(String locationZone) {
		this.locationZone = locationZone;
	}

	@Column(name = "IS_ALLOCATABLE_LOCATIONS", length = 1)
	public String getIsAllocatableLocations() {
		return this.isAllocatableLocations;
	}

	public void setIsAllocatableLocations(String isAllocatableLocations) {
		this.isAllocatableLocations = isAllocatableLocations;
	}

	@Column(name = "IS_DEFAULT", length = 1)
	public String getIsDefault() {
		return this.isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
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