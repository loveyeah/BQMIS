package power.ejb.productiontec.report;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtJJdybDetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_J_JDYB_DETAIL", schema = "POWER")
public class PtJJdybDetail implements java.io.Serializable {

	// Fields

	private Long jdybDetailId;
	private Long jdybId;
	private Date time;
	private String address;
	private String equSummary;
	private String equName;
	private String equType;
	private Long rightNum;
	private Long wrongNum;
	private String errorReason;
	private String memo;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJJdybDetail() {
	}

	/** minimal constructor */
	public PtJJdybDetail(Long jdybDetailId) {
		this.jdybDetailId = jdybDetailId;
	}

	/** full constructor */
	public PtJJdybDetail(Long jdybDetailId, Long jdybId, Date time,
			String address, String equSummary, String equName, String equType,
			Long rightNum, Long wrongNum, String errorReason, String memo,
			String enterpriseCode) {
		this.jdybDetailId = jdybDetailId;
		this.jdybId = jdybId;
		this.time = time;
		this.address = address;
		this.equSummary = equSummary;
		this.equName = equName;
		this.equType = equType;
		this.rightNum = rightNum;
		this.wrongNum = wrongNum;
		this.errorReason = errorReason;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "JDYB_DETAIL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getJdybDetailId() {
		return this.jdybDetailId;
	}

	public void setJdybDetailId(Long jdybDetailId) {
		this.jdybDetailId = jdybDetailId;
	}

	@Column(name = "JDYB_ID", precision = 10, scale = 0)
	public Long getJdybId() {
		return this.jdybId;
	}

	public void setJdybId(Long jdybId) {
		this.jdybId = jdybId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "TIME", length = 7)
	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Column(name = "ADDRESS", length = 100)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "EQU_SUMMARY", length = 200)
	public String getEquSummary() {
		return this.equSummary;
	}

	public void setEquSummary(String equSummary) {
		this.equSummary = equSummary;
	}

	@Column(name = "EQU_NAME", length = 200)
	public String getEquName() {
		return this.equName;
	}

	public void setEquName(String equName) {
		this.equName = equName;
	}

	@Column(name = "EQU_TYPE", length = 100)
	public String getEquType() {
		return this.equType;
	}

	public void setEquType(String equType) {
		this.equType = equType;
	}

	@Column(name = "RIGHT_NUM", precision = 10, scale = 0)
	public Long getRightNum() {
		return this.rightNum;
	}

	public void setRightNum(Long rightNum) {
		this.rightNum = rightNum;
	}

	@Column(name = "WRONG_NUM", precision = 10, scale = 0)
	public Long getWrongNum() {
		return this.wrongNum;
	}

	public void setWrongNum(Long wrongNum) {
		this.wrongNum = wrongNum;
	}

	@Column(name = "ERROR_REASON", length = 500)
	public String getErrorReason() {
		return this.errorReason;
	}

	public void setErrorReason(String errorReason) {
		this.errorReason = errorReason;
	}

	@Column(name = "MEMO", length = 500)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}