package power.ejb.commodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SysCParameters entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_C_PARAMETERS")
public class SysCParameters implements java.io.Serializable {

	// Fields

	private String parmNo;
	private String parmName;
	private String parmValue;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public SysCParameters() {
	}

	/** full constructor */
	public SysCParameters(String parmNo, String parmName, String parmValue,
			String enterpriseCode) {
		this.parmNo = parmNo;
		this.parmName = parmName;
		this.parmValue = parmValue;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "PARM_NO", unique = true, nullable = false, length = 12)
	public String getParmNo() {
		return this.parmNo;
	}

	public void setParmNo(String parmNo) {
		this.parmNo = parmNo;
	}

	@Column(name = "PARM_NAME", nullable = false, length = 100)
	public String getParmName() {
		return this.parmName;
	}

	public void setParmName(String parmName) {
		this.parmName = parmName;
	}

	@Column(name = "PARM_VALUE", nullable = false, length = 100)
	public String getParmValue() {
		return this.parmValue;
	}

	public void setParmValue(String parmValue) {
		this.parmValue = parmValue;
	}

	@Column(name = "ENTERPRISE_CODE", nullable = false, length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}