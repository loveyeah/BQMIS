package power.ejb.productiontec.insulation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PtJyjdJYqybtzlh entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_JYJD_J_YQYBTZLH")
public class PtJyjdJYqybtzlh implements java.io.Serializable {

	// Fields

	private Long regulatorId;
	private String names;
	private String regulatorNo;
	private String factory;
	private String sizes;
	private String userRange;
	private Long testCycle;
	private String memo;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJyjdJYqybtzlh() {
	}

	/** minimal constructor */
	public PtJyjdJYqybtzlh(Long regulatorId) {
		this.regulatorId = regulatorId;
	}

	/** full constructor */
	public PtJyjdJYqybtzlh(Long regulatorId, String names, String regulatorNo,
			String factory, String sizes, String userRange, Long testCycle,
			String memo, String enterpriseCode) {
		this.regulatorId = regulatorId;
		this.names = names;
		this.regulatorNo = regulatorNo;
		this.factory = factory;
		this.sizes = sizes;
		this.userRange = userRange;
		this.testCycle = testCycle;
		this.memo = memo;
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

	@Column(name = "REGULATOR_NO", length = 50)
	public String getRegulatorNo() {
		return this.regulatorNo;
	}

	public void setRegulatorNo(String regulatorNo) {
		this.regulatorNo = regulatorNo;
	}

	@Column(name = "FACTORY", length = 100)
	public String getFactory() {
		return this.factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	@Column(name = "SIZES", length = 50)
	public String getSizes() {
		return this.sizes;
	}

	public void setSizes(String sizes) {
		this.sizes = sizes;
	}

	@Column(name = "USER_RANGE", length = 50)
	public String getUserRange() {
		return this.userRange;
	}

	public void setUserRange(String userRange) {
		this.userRange = userRange;
	}

	@Column(name = "TEST_CYCLE", precision = 22, scale = 0)
	public Long getTestCycle() {
		return this.testCycle;
	}

	public void setTestCycle(Long testCycle) {
		this.testCycle = testCycle;
	}

	@Column(name = "MEMO", length = 256)
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