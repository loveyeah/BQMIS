package power.ejb.run.runlog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RunCEarthtar entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_C_EARTHTAR",  uniqueConstraints = {})
public class RunCEarthtar implements java.io.Serializable {

	// Fields

	private Long earthId;
	private String earthName;
	private String memo;
	private String isUse;
	private Long displayNo;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public RunCEarthtar() {
	}

	/** minimal constructor */
	public RunCEarthtar(Long earthId) {
		this.earthId = earthId;
	}

	/** full constructor */
	public RunCEarthtar(Long earthId, String earthName, String memo,
			String isUse, Long displayNo, String enterpriseCode) {
		this.earthId = earthId;
		this.earthName = earthName;
		this.memo = memo;
		this.isUse = isUse;
		this.displayNo = displayNo;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "EARTH_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getEarthId() {
		return this.earthId;
	}

	public void setEarthId(Long earthId) {
		this.earthId = earthId;
	}

	@Column(name = "EARTH_NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 40)
	public String getEarthName() {
		return this.earthName;
	}

	public void setEarthName(String earthName) {
		this.earthName = earthName;
	}

	@Column(name = "MEMO", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "IS_USE", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "DISPLAY_NO", unique = false, nullable = true, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getDisplayNo() {
		return this.displayNo;
	}

	public void setDisplayNo(Long displayNo) {
		this.displayNo = displayNo;
	}

	@Column(name = "ENTERPRISE_CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}