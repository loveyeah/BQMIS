package power.ejb.equ.base;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * EquCLocation entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EQU_C_LOCATION")
public class EquCLocation implements java.io.Serializable {

	// Fields

	private Long locationId;
	private String locationCode;
	private String locationDesc;
	private String PLocationCode;
	private String locationType;
	private Long classStructureId;
	private String gisparam1;
	private String gisparam2;
	private String gisparam3;
	private String changeBy;
	private Date changeDate;
	private String memo;
	private String disabled;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public EquCLocation() {
	}

	/** minimal constructor */
	public EquCLocation(Long locationId) {
		this.locationId = locationId;
	}

	/** full constructor */
	public EquCLocation(Long locationId, String locationCode,
			String locationDesc, String PLocationCode, String locationType,
			Long classStructureId, String gisparam1, String gisparam2,
			String gisparam3, String changeBy, Date changeDate, String memo,
			String disabled, String enterpriseCode, String isUse) {
		this.locationId = locationId;
		this.locationCode = locationCode;
		this.locationDesc = locationDesc;
		this.PLocationCode = PLocationCode;
		this.locationType = locationType;
		this.classStructureId = classStructureId;
		this.gisparam1 = gisparam1;
		this.gisparam2 = gisparam2;
		this.gisparam3 = gisparam3;
		this.changeBy = changeBy;
		this.changeDate = changeDate;
		this.memo = memo;
		this.disabled = disabled;
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

	@Column(name = "LOCATION_CODE", length = 30)
	public String getLocationCode() {
		return this.locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	@Column(name = "LOCATION_DESC", length = 100)
	public String getLocationDesc() {
		return this.locationDesc;
	}

	public void setLocationDesc(String locationDesc) {
		this.locationDesc = locationDesc;
	}

	@Column(name = "P_LOCATION_CODE", length = 30)
	public String getPLocationCode() {
		return this.PLocationCode;
	}

	public void setPLocationCode(String PLocationCode) {
		this.PLocationCode = PLocationCode;
	}

	@Column(name = "LOCATION_TYPE", length = 15)
	public String getLocationType() {
		return this.locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	@Column(name = "CLASS_STRUCTURE_ID", precision = 10, scale = 0)
	public Long getClassStructureId() {
		return this.classStructureId;
	}

	public void setClassStructureId(Long classStructureId) {
		this.classStructureId = classStructureId;
	}

	@Column(name = "GISPARAM1", length = 1)
	public String getGisparam1() {
		return this.gisparam1;
	}

	public void setGisparam1(String gisparam1) {
		this.gisparam1 = gisparam1;
	}

	@Column(name = "GISPARAM2", length = 1)
	public String getGisparam2() {
		return this.gisparam2;
	}

	public void setGisparam2(String gisparam2) {
		this.gisparam2 = gisparam2;
	}

	@Column(name = "GISPARAM3", length = 1)
	public String getGisparam3() {
		return this.gisparam3;
	}

	public void setGisparam3(String gisparam3) {
		this.gisparam3 = gisparam3;
	}

	@Column(name = "CHANGE_BY", length = 30)
	public String getChangeBy() {
		return this.changeBy;
	}

	public void setChangeBy(String changeBy) {
		this.changeBy = changeBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CHANGE_DATE", length = 7)
	public Date getChangeDate() {
		return this.changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	@Column(name = "MEMO", length = 200)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "DISABLED", length = 1)
	public String getDisabled() {
		return this.disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
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