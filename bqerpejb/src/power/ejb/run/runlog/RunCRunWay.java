package power.ejb.run.runlog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RunCRunWay entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_C_RUN_WAY", schema = "POWER")
public class RunCRunWay implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long runKeyId;
	private String runWayCode;
	private String runWayName;
	private Long diaplayNo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public RunCRunWay() {
	}

	/** minimal constructor */
	public RunCRunWay(Long runKeyId) {
		this.runKeyId = runKeyId;
	}

	/** full constructor */
	public RunCRunWay(Long runKeyId, String runWayCode, String runWayName,
			Long diaplayNo, String isUse, String enterpriseCode) {
		this.runKeyId = runKeyId;
		this.runWayCode = runWayCode;
		this.runWayName = runWayName;
		this.diaplayNo = diaplayNo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "RUN_KEY_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getRunKeyId() {
		return this.runKeyId;
	}

	public void setRunKeyId(Long runKeyId) {
		this.runKeyId = runKeyId;
	}

	@Column(name = "RUN_WAY_CODE", length = 10)
	public String getRunWayCode() {
		return this.runWayCode;
	}

	public void setRunWayCode(String runWayCode) {
		this.runWayCode = runWayCode;
	}

	@Column(name = "RUN_WAY_NAME", length = 20)
	public String getRunWayName() {
		return this.runWayName;
	}

	public void setRunWayName(String runWayName) {
		this.runWayName = runWayName;
	}

	@Column(name = "DIAPLAY_NO", precision = 10, scale = 0)
	public Long getDiaplayNo() {
		return this.diaplayNo;
	}

	public void setDiaplayNo(Long diaplayNo) {
		this.diaplayNo = diaplayNo;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}