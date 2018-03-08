package power.ejb.productiontec.chemistry;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtHxjdJRlsbjcqk entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PT_HXJD_J_RLSBJCQK")
public class PtHxjdJRlsbjcqk implements java.io.Serializable {

	// Fields

	private Long rlsbjcId;
	private String deviceCode;
	private Long testType;
	private Date startDate;
	private Date endDate;
	private String examineBy;
	private String chargeBy;
	private String content;
	private String fillBy;
	private String depCode;
	private Date fillDate;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtHxjdJRlsbjcqk() {
	}

	/** minimal constructor */
	public PtHxjdJRlsbjcqk(Long rlsbjcId) {
		this.rlsbjcId = rlsbjcId;
	}

	/** full constructor */
	public PtHxjdJRlsbjcqk(Long rlsbjcId, String deviceCode, Long testType,
			Date startDate, Date endDate, String examineBy, String chargeBy,
			String content, String fillBy, String depCode, Date fillDate,
			String enterpriseCode) {
		this.rlsbjcId = rlsbjcId;
		this.deviceCode = deviceCode;
		this.testType = testType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.examineBy = examineBy;
		this.chargeBy = chargeBy;
		this.content = content;
		this.fillBy = fillBy;
		this.depCode = depCode;
		this.fillDate = fillDate;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "RLSBJC_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getRlsbjcId() {
		return this.rlsbjcId;
	}

	public void setRlsbjcId(Long rlsbjcId) {
		this.rlsbjcId = rlsbjcId;
	}

	@Column(name = "DEVICE_CODE", length = 2)
	public String getDeviceCode() {
		return this.deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	@Column(name = "TEST_TYPE", precision = 1, scale = 0)
	public Long getTestType() {
		return this.testType;
	}

	public void setTestType(Long testType) {
		this.testType = testType;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE", length = 7)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_DATE", length = 7)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "EXAMINE_BY", length = 100)
	public String getExamineBy() {
		return this.examineBy;
	}

	public void setExamineBy(String examineBy) {
		this.examineBy = examineBy;
	}

	@Column(name = "CHARGE_BY", length = 16)
	public String getChargeBy() {
		return this.chargeBy;
	}

	public void setChargeBy(String chargeBy) {
		this.chargeBy = chargeBy;
	}

	@Column(name = "CONTENT", length = 1000)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "FILL_BY", length = 16)
	public String getFillBy() {
		return this.fillBy;
	}

	public void setFillBy(String fillBy) {
		this.fillBy = fillBy;
	}

	@Column(name = "DEP_CODE", length = 20)
	public String getDepCode() {
		return this.depCode;
	}

	public void setDepCode(String depCode) {
		this.depCode = depCode;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FILL_DATE", length = 7)
	public Date getFillDate() {
		return this.fillDate;
	}

	public void setFillDate(Date fillDate) {
		this.fillDate = fillDate;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}