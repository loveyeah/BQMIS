package power.ejb.productiontec.powertest;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtJYqybjyjl entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PT_J_YQYBJYJL")
public class PtJYqybjyjl implements java.io.Serializable {

	// Fields

	private Long jyjlId;
	private Long regulatorId;
	private Date planCheckDate;
	private Date checkDate;
	private Date nextCheckDate;
	private Double checkTemperature;
	private Double checkWet;
	private String rusult;
	private String checkMan;
	private String checkDepCode;
	private String testMan;
	private String memo;
	private Long jdzyId;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJYqybjyjl() {
	}

	/** minimal constructor */
	public PtJYqybjyjl(Long jyjlId) {
		this.jyjlId = jyjlId;
	}

	/** full constructor */
	public PtJYqybjyjl(Long jyjlId, Long regulatorId, Date planCheckDate,
			Date checkDate, Date nextCheckDate, Double checkTemperature,
			Double checkWet, String rusult, String checkMan,
			String checkDepCode, String testMan, String memo, Long jdzyId,
			String enterpriseCode) {
		this.jyjlId = jyjlId;
		this.regulatorId = regulatorId;
		this.planCheckDate = planCheckDate;
		this.checkDate = checkDate;
		this.nextCheckDate = nextCheckDate;
		this.checkTemperature = checkTemperature;
		this.checkWet = checkWet;
		this.rusult = rusult;
		this.checkMan = checkMan;
		this.checkDepCode = checkDepCode;
		this.testMan = testMan;
		this.memo = memo;
		this.jdzyId = jdzyId;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "JYJL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getJyjlId() {
		return this.jyjlId;
	}

	public void setJyjlId(Long jyjlId) {
		this.jyjlId = jyjlId;
	}

	@Column(name = "REGULATOR_ID", precision = 10, scale = 0)
	public Long getRegulatorId() {
		return this.regulatorId;
	}

	public void setRegulatorId(Long regulatorId) {
		this.regulatorId = regulatorId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "PLAN_CHECK_DATE", length = 7)
	public Date getPlanCheckDate() {
		return this.planCheckDate;
	}

	public void setPlanCheckDate(Date planCheckDate) {
		this.planCheckDate = planCheckDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CHECK_DATE", length = 7)
	public Date getCheckDate() {
		return this.checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "NEXT_CHECK_DATE", length = 7)
	public Date getNextCheckDate() {
		return this.nextCheckDate;
	}

	public void setNextCheckDate(Date nextCheckDate) {
		this.nextCheckDate = nextCheckDate;
	}

	@Column(name = "CHECK_TEMPERATURE", precision = 10, scale = 4)
	public Double getCheckTemperature() {
		return this.checkTemperature;
	}

	public void setCheckTemperature(Double checkTemperature) {
		this.checkTemperature = checkTemperature;
	}

	@Column(name = "CHECK_WET", precision = 10, scale = 4)
	public Double getCheckWet() {
		return this.checkWet;
	}

	public void setCheckWet(Double checkWet) {
		this.checkWet = checkWet;
	}

	@Column(name = "RUSULT", length = 256)
	public String getRusult() {
		return this.rusult;
	}

	public void setRusult(String rusult) {
		this.rusult = rusult;
	}

	@Column(name = "CHECK_MAN", length = 16)
	public String getCheckMan() {
		return this.checkMan;
	}

	public void setCheckMan(String checkMan) {
		this.checkMan = checkMan;
	}

	@Column(name = "CHECK_DEP_CODE", length = 20)
	public String getCheckDepCode() {
		return this.checkDepCode;
	}

	public void setCheckDepCode(String checkDepCode) {
		this.checkDepCode = checkDepCode;
	}

	@Column(name = "TEST_MAN", length = 16)
	public String getTestMan() {
		return this.testMan;
	}

	public void setTestMan(String testMan) {
		this.testMan = testMan;
	}

	@Column(name = "MEMO", length = 256)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "JDZY_ID", precision = 2, scale = 0)
	public Long getJdzyId() {
		return this.jdzyId;
	}

	public void setJdzyId(Long jdzyId) {
		this.jdzyId = jdzyId;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}