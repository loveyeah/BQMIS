package power.ejb.equ.failure;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EquCFailureType entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EQU_C_FAILURE_TYPE")
public class EquCFailureType implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 912527062396792471L;
	private Long id;
	private String failuretypeCode;
	private String failuretypeName;
	private String failurePri;
	private String failuretypeDesc;
	private String needCaclOvertime;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public EquCFailureType() {
	}

	/** minimal constructor */
	public EquCFailureType(Long id) {
		this.id = id;
	}

	/** full constructor */
	public EquCFailureType(Long id, String failuretypeCode,
			String failuretypeName, String failurePri, String failuretypeDesc,
			String needCaclOvertime, String isUse, String enterpriseCode) {
		this.id = id;
		this.failuretypeCode = failuretypeCode;
		this.failuretypeName = failuretypeName;
		this.failurePri = failurePri;
		this.failuretypeDesc = failuretypeDesc;
		this.needCaclOvertime = needCaclOvertime;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "FAILURETYPE_CODE", length = 20)
	public String getFailuretypeCode() {
		return this.failuretypeCode;
	}

	public void setFailuretypeCode(String failuretypeCode) {
		this.failuretypeCode = failuretypeCode;
	}

	@Column(name = "FAILURETYPE_NAME", length = 20)
	public String getFailuretypeName() {
		return this.failuretypeName;
	}

	public void setFailuretypeName(String failuretypeName) {
		this.failuretypeName = failuretypeName;
	}

	@Column(name = "FAILURE_PRI", length = 20)
	public String getFailurePri() {
		return this.failurePri;
	}

	public void setFailurePri(String failurePri) {
		this.failurePri = failurePri;
	}

	@Column(name = "FAILURETYPE_DESC", length = 400)
	public String getFailuretypeDesc() {
		return this.failuretypeDesc;
	}

	public void setFailuretypeDesc(String failuretypeDesc) {
		this.failuretypeDesc = failuretypeDesc;
	}

	@Column(name = "NEED_CACL_OVERTIME", length = 1)
	public String getNeedCaclOvertime() {
		return this.needCaclOvertime;
	}

	public void setNeedCaclOvertime(String needCaclOvertime) {
		this.needCaclOvertime = needCaclOvertime;
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