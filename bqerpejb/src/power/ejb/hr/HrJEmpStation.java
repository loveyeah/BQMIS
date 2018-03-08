package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJEmpStation entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_EMP_STATION")
public class HrJEmpStation implements java.io.Serializable {

	// Fields

	private Long empStationId;
	private Long empId;
	private Long stationId;
	private String isMainStation;
	private String memo;
	private String enterpriseCode;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private Long orderBy;
	private String isUse;

	// Constructors

	/** default constructor */
	public HrJEmpStation() {
	}

	/** minimal constructor */
	public HrJEmpStation(Long empStationId) {
		this.empStationId = empStationId;
	}

	/** full constructor */
	public HrJEmpStation(Long empStationId, Long empId, Long stationId,
			String isMainStation, String memo, String enterpriseCode,
			String lastModifiedBy, Date lastModifiedDate, Long orderBy,
			String isUse) {
		this.empStationId = empStationId;
		this.empId = empId;
		this.stationId = stationId;
		this.isMainStation = isMainStation;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.orderBy = orderBy;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "EMP_STATION_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getEmpStationId() {
		return this.empStationId;
	}

	public void setEmpStationId(Long empStationId) {
		this.empStationId = empStationId;
	}

	@Column(name = "EMP_ID", precision = 10, scale = 0)
	public Long getEmpId() {
		return this.empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	@Column(name = "STATION_ID", precision = 10, scale = 0)
	public Long getStationId() {
		return this.stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	@Column(name = "IS_MAIN_STATION", length = 1)
	public String getIsMainStation() {
		return this.isMainStation;
	}

	public void setIsMainStation(String isMainStation) {
		this.isMainStation = isMainStation;
	}

	@Column(name = "MEMO", length = 500)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

	@Column(name = "ORDER_BY", precision = 10, scale = 0)
	public Long getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

}