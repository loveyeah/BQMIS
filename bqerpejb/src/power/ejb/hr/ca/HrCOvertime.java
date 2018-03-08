package power.ejb.hr.ca;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCOvertime entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_OVERTIME")
public class HrCOvertime implements java.io.Serializable {

	// Fields

	private Long overtimeTypeId;
	private String overtimeTypeCode;
	private String overtimeType;
	private String ifOvertimeFee;
	private String overtimeMark;
	private String lastModifiyBy;
	private Date lastModifiyDate;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrCOvertime() {
	}

	/** minimal constructor */
	public HrCOvertime(Long overtimeTypeId) {
		this.overtimeTypeId = overtimeTypeId;
	}

	/** full constructor */
	public HrCOvertime(Long overtimeTypeId, String overtimeTypeCode,
			String overtimeType, String ifOvertimeFee, String overtimeMark,
			String lastModifiyBy, Date lastModifiyDate, String isUse,
			String enterpriseCode) {
		this.overtimeTypeId = overtimeTypeId;
		this.overtimeTypeCode = overtimeTypeCode;
		this.overtimeType = overtimeType;
		this.ifOvertimeFee = ifOvertimeFee;
		this.overtimeMark = overtimeMark;
		this.lastModifiyBy = lastModifiyBy;
		this.lastModifiyDate = lastModifiyDate;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "OVERTIME_TYPE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getOvertimeTypeId() {
		return this.overtimeTypeId;
	}

	public void setOvertimeTypeId(Long overtimeTypeId) {
		this.overtimeTypeId = overtimeTypeId;
	}

	@Column(name = "OVERTIME_TYPE_CODE", length = 2)
	public String getOvertimeTypeCode() {
		return this.overtimeTypeCode;
	}

	public void setOvertimeTypeCode(String overtimeTypeCode) {
		this.overtimeTypeCode = overtimeTypeCode;
	}

	@Column(name = "OVERTIME_TYPE", length = 20)
	public String getOvertimeType() {
		return this.overtimeType;
	}

	public void setOvertimeType(String overtimeType) {
		this.overtimeType = overtimeType;
	}

	@Column(name = "IF_OVERTIME_FEE", length = 1)
	public String getIfOvertimeFee() {
		return this.ifOvertimeFee;
	}

	public void setIfOvertimeFee(String ifOvertimeFee) {
		this.ifOvertimeFee = ifOvertimeFee;
	}

	@Column(name = "OVERTIME_MARK", length = 10)
	public String getOvertimeMark() {
		return this.overtimeMark;
	}

	public void setOvertimeMark(String overtimeMark) {
		this.overtimeMark = overtimeMark;
	}

	@Column(name = "LAST_MODIFIY_BY", length = 16)
	public String getLastModifiyBy() {
		return this.lastModifiyBy;
	}

	public void setLastModifiyBy(String lastModifiyBy) {
		this.lastModifiyBy = lastModifiyBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIY_DATE", length = 7)
	public Date getLastModifiyDate() {
		return this.lastModifiyDate;
	}

	public void setLastModifiyDate(Date lastModifiyDate) {
		this.lastModifiyDate = lastModifiyDate;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 10)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}