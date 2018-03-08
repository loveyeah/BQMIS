package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCStationmovetype entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_STATIONMOVETYPE")
public class HrCStationmovetype implements java.io.Serializable {

    // Fields

    private Long stationMoveTypeId;
    private String stationMoveType;
    private String enterpriseCode;
    private String isUse;
    private String lastModifiedBy;
    private Date lastModifiedDate;

    // Constructors

    /** default constructor */
    public HrCStationmovetype() {
    }

    /** minimal constructor */
    public HrCStationmovetype(Long stationMoveTypeId) {
	this.stationMoveTypeId = stationMoveTypeId;
    }

    /** full constructor */
    public HrCStationmovetype(Long stationMoveTypeId, String stationMoveType,
	    String enterpriseCode, String isUse, String lastModifiedBy,
	    Date lastModifiedDate) {
	this.stationMoveTypeId = stationMoveTypeId;
	this.stationMoveType = stationMoveType;
	this.enterpriseCode = enterpriseCode;
	this.isUse = isUse;
	this.lastModifiedBy = lastModifiedBy;
	this.lastModifiedDate = lastModifiedDate;
    }

    // Property accessors
    @Id
    @Column(name = "STATION_MOVE_TYPE_ID", unique = true, nullable = false, precision = 10, scale = 0)
    public Long getStationMoveTypeId() {
	return this.stationMoveTypeId;
    }

    public void setStationMoveTypeId(Long stationMoveTypeId) {
	this.stationMoveTypeId = stationMoveTypeId;
    }

    @Column(name = "STATION_MOVE_TYPE", length = 40)
    public String getStationMoveType() {
	return this.stationMoveType;
    }

    public void setStationMoveType(String stationMoveType) {
	this.stationMoveType = stationMoveType;
    }

    @Column(name = "ENTERPRISE_CODE", length = 10)
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