package power.ejb.hr.ca;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCWorkshift entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_WORKSHIFT")
public class HrCWorkshift implements java.io.Serializable {

	// Fields

	private Long workShiftId;
	private String workShiftCode;
	private String workShift;
	private Double workShitFee;
	private String workShiftMark;
	private String lastModifiyBy;
	private Date lastModifiyDate;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrCWorkshift() {
	}

	/** minimal constructor */
	public HrCWorkshift(Long workShiftId) {
		this.workShiftId = workShiftId;
	}

	/** full constructor */
	public HrCWorkshift(Long workShiftId, String workShiftCode,
			String workShift, Double workShitFee, String workShiftMark,
			String lastModifiyBy, Date lastModifiyDate, String isUse,
			String enterpriseCode) {
		this.workShiftId = workShiftId;
		this.workShiftCode = workShiftCode;
		this.workShift = workShift;
		this.workShitFee = workShitFee;
		this.workShiftMark = workShiftMark;
		this.lastModifiyBy = lastModifiyBy;
		this.lastModifiyDate = lastModifiyDate;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "WORK_SHIFT_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getWorkShiftId() {
		return this.workShiftId;
	}

	public void setWorkShiftId(Long workShiftId) {
		this.workShiftId = workShiftId;
	}

	@Column(name = "WORK_SHIFT_CODE", length = 2)
	public String getWorkShiftCode() {
		return this.workShiftCode;
	}

	public void setWorkShiftCode(String workShiftCode) {
		this.workShiftCode = workShiftCode;
	}

	@Column(name = "WORK_SHIFT", length = 30)
	public String getWorkShift() {
		return this.workShift;
	}

	public void setWorkShift(String workShift) {
		this.workShift = workShift;
	}

	@Column(name = "WORK_SHIT_FEE", precision = 15, scale = 4)
	public Double getWorkShitFee() {
		return this.workShitFee;
	}

	public void setWorkShitFee(Double workShitFee) {
		this.workShitFee = workShitFee;
	}

	@Column(name = "WORK_SHIFT_MARK", length = 10)
	public String getWorkShiftMark() {
		return this.workShiftMark;
	}

	public void setWorkShiftMark(String workShiftMark) {
		this.workShiftMark = workShiftMark;
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