package power.ejb.run.securityproduction.safesupervise;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SpCCarordriverPhoto entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SP_C_CARORDRIVER_PHOTO", schema = "POWER")
public class SpCCarordriverPhoto implements java.io.Serializable {

	// Fields

	private Long id;
	private String type;
	private Long carDriverId;
	private byte[] photo;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public SpCCarordriverPhoto() {
	}

	/** minimal constructor */
	public SpCCarordriverPhoto(Long id) {
		this.id = id;
	}

	/** full constructor */
	public SpCCarordriverPhoto(Long id, String type, Long carDriverId,
			byte[] photo, String lastModifiedBy, Date lastModifiedDate,
			String isUse, String enterpriseCode) {
		this.id = id;
		this.type = type;
		this.carDriverId = carDriverId;
		this.photo = photo;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
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

	@Column(name = "TYPE", length = 1)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "CAR_DRIVER_ID", precision = 10, scale = 0)
	public Long getCarDriverId() {
		return this.carDriverId;
	}

	public void setCarDriverId(Long carDriverId) {
		this.carDriverId = carDriverId;
	}

	@Column(name = "PHOTO")
	public byte[] getPhoto() {
		return this.photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	@Column(name = "LAST_MODIFIED_BY", length = 20)
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

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}