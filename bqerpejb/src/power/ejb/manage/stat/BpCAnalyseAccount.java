package power.ejb.manage.stat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BpCAnalyseAccount entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_C_ANALYSE_ACCOUNT")
public class BpCAnalyseAccount implements java.io.Serializable {

	// Fields

	private Long accountCode;
	private String accountName;
	private String accountType;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpCAnalyseAccount() {
	}

	/** minimal constructor */
	public BpCAnalyseAccount(Long accountCode, String accountName,
			String enterpriseCode) {
		this.accountCode = accountCode;
		this.accountName = accountName;
		this.enterpriseCode = enterpriseCode;
	}

	/** full constructor */
	public BpCAnalyseAccount(Long accountCode, String accountName,
			String accountType, String enterpriseCode) {
		this.accountCode = accountCode;
		this.accountName = accountName;
		this.accountType = accountType;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "ACCOUNT_CODE", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getAccountCode() {
		return this.accountCode;
	}

	public void setAccountCode(Long accountCode) {
		this.accountCode = accountCode;
	}

	@Column(name = "ACCOUNT_NAME", nullable = false, length = 100)
	public String getAccountName() {
		return this.accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	@Column(name = "ACCOUNT_TYPE", length = 1)
	public String getAccountType() {
		return this.accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	@Column(name = "ENTERPRISE_CODE", nullable = false, length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}