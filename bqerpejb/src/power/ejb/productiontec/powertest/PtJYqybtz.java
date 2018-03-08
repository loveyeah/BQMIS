package power.ejb.productiontec.powertest;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtJYqybtz entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PT_J_YQYBTZ")
public class PtJYqybtz implements java.io.Serializable {

	// Fields

	private Long regulatorId;
	private String names;
	private Long yqyblbId;
	private Long yqybdjId;
	private Long yqybjdId;
	private String userRange;
	private String sizes;
	private Long testCycle;
	private String factory;
	private Date outFactoryDate;
	private String outFactoryNo;
	private Date buyDate;
	private Date useDate;
	private String depCode;
	private String charger;
	private String ifUsed;
	private String memo;
	private Date lastCheckDate;
	private Date nextCheckDate;
	private Long jdzyId;
	private String enterpriseCode;
	
	private String grade;//GRADE
	private String precision;//PRECISION
	private String checkDeptCode;//CHECK_DEPT_CODE
	private String mainParam;//MAIN_PARAM
	private String checkResult;//CHECK_RESULT

	// Constructors

	/** default constructor */
	public PtJYqybtz() {
	}

	/** minimal constructor */
	public PtJYqybtz(Long regulatorId) {
		this.regulatorId = regulatorId;
	}

	/** full constructor */
	public PtJYqybtz(Long regulatorId, String names, Long yqyblbId,
			Long yqybdjId, Long yqybjdId, String userRange, String sizes,
			Long testCycle, String factory, Date outFactoryDate,
			String outFactoryNo, Date buyDate, Date useDate, String depCode,
			String charger, String ifUsed, String memo, Date lastCheckDate,
			Date nextCheckDate, Long jdzyId, String enterpriseCode) {
		this.regulatorId = regulatorId;
		this.names = names;
		this.yqyblbId = yqyblbId;
		this.yqybdjId = yqybdjId;
		this.yqybjdId = yqybjdId;
		this.userRange = userRange;
		this.sizes = sizes;
		this.testCycle = testCycle;
		this.factory = factory;
		this.outFactoryDate = outFactoryDate;
		this.outFactoryNo = outFactoryNo;
		this.buyDate = buyDate;
		this.useDate = useDate;
		this.depCode = depCode;
		this.charger = charger;
		this.ifUsed = ifUsed;
		this.memo = memo;
		this.lastCheckDate = lastCheckDate;
		this.nextCheckDate = nextCheckDate;
		this.jdzyId = jdzyId;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "REGULATOR_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getRegulatorId() {
		return this.regulatorId;
	}

	public void setRegulatorId(Long regulatorId) {
		this.regulatorId = regulatorId;
	}

	@Column(name = "NAMES", length = 100)
	public String getNames() {
		return this.names;
	}

	public void setNames(String names) {
		this.names = names;
	}

	@Column(name = "YQYBLB_ID", precision = 10, scale = 0)
	public Long getYqyblbId() {
		return this.yqyblbId;
	}

	public void setYqyblbId(Long yqyblbId) {
		this.yqyblbId = yqyblbId;
	}

	@Column(name = "YQYBDJ_ID", precision = 10, scale = 0)
	public Long getYqybdjId() {
		return this.yqybdjId;
	}

	public void setYqybdjId(Long yqybdjId) {
		this.yqybdjId = yqybdjId;
	}

	@Column(name = "YQYBJD_ID", precision = 10, scale = 0)
	public Long getYqybjdId() {
		return this.yqybjdId;
	}

	public void setYqybjdId(Long yqybjdId) {
		this.yqybjdId = yqybjdId;
	}

	@Column(name = "USER_RANGE", length = 50)
	public String getUserRange() {
		return this.userRange;
	}

	public void setUserRange(String userRange) {
		this.userRange = userRange;
	}

	@Column(name = "SIZES", length = 50)
	public String getSizes() {
		return this.sizes;
	}

	public void setSizes(String sizes) {
		this.sizes = sizes;
	}

	@Column(name = "TEST_CYCLE", precision = 4, scale = 0)
	public Long getTestCycle() {
		return this.testCycle;
	}

	public void setTestCycle(Long testCycle) {
		this.testCycle = testCycle;
	}

	@Column(name = "FACTORY", length = 256)
	public String getFactory() {
		return this.factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "OUT_FACTORY_DATE", length = 7)
	public Date getOutFactoryDate() {
		return this.outFactoryDate;
	}

	public void setOutFactoryDate(Date outFactoryDate) {
		this.outFactoryDate = outFactoryDate;
	}

	@Column(name = "OUT_FACTORY_NO", length = 60)
	public String getOutFactoryNo() {
		return this.outFactoryNo;
	}

	public void setOutFactoryNo(String outFactoryNo) {
		this.outFactoryNo = outFactoryNo;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "BUY_DATE", length = 7)
	public Date getBuyDate() {
		return this.buyDate;
	}

	public void setBuyDate(Date buyDate) {
		this.buyDate = buyDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "USE_DATE", length = 7)
	public Date getUseDate() {
		return this.useDate;
	}

	public void setUseDate(Date useDate) {
		this.useDate = useDate;
	}

	@Column(name = "DEP_CODE", length = 20)
	public String getDepCode() {
		return this.depCode;
	}

	public void setDepCode(String depCode) {
		this.depCode = depCode;
	}

	@Column(name = "CHARGER", length = 16)
	public String getCharger() {
		return this.charger;
	}

	public void setCharger(String charger) {
		this.charger = charger;
	}

	@Column(name = "IF_USED", length = 1)
	public String getIfUsed() {
		return this.ifUsed;
	}

	public void setIfUsed(String ifUsed) {
		this.ifUsed = ifUsed;
	}

	@Column(name = "MEMO", length = 256)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_CHECK_DATE", length = 7)
	public Date getLastCheckDate() {
		return this.lastCheckDate;
	}

	public void setLastCheckDate(Date lastCheckDate) {
		this.lastCheckDate = lastCheckDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "NEXT_CHECK_DATE", length = 7)
	public Date getNextCheckDate() {
		return this.nextCheckDate;
	}

	public void setNextCheckDate(Date nextCheckDate) {
		this.nextCheckDate = nextCheckDate;
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

	@Column(name = "GRADE", length = 50)
	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	@Column(name = "PRECISION", length = 50)
	public String getPrecision() {
		return precision;
	}

	public void setPrecision(String precision) {
		this.precision = precision;
	}

	@Column(name = "CHECK_DEPT_CODE", length = 20)
	public String getCheckDeptCode() {
		return checkDeptCode;
	}

	public void setCheckDeptCode(String checkDeptCode) {
		this.checkDeptCode = checkDeptCode;
	}
	
	@Column(name = "MAIN_PARAM", length = 500)
	public String getMainParam() {
		return mainParam;
	}

	public void setMainParam(String mainParam) {
		this.mainParam = mainParam;
	}

	@Column(name = "CHECK_RESULT", length = 500)
	public String getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

}