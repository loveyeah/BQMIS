package power.ejb.manage.client;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ConCClientsImportance entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CON_C_CLIENTS_IMPORTANCE")
public class ConCClientsImportance implements java.io.Serializable {

	// Fields

	private Long importanceId;
	private String importanceName;
	private String memo;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public ConCClientsImportance() {
	}

	/** minimal constructor */
	public ConCClientsImportance(Long importanceId) {
		this.importanceId = importanceId;
	}

	/** full constructor */
	public ConCClientsImportance(Long importanceId, String importanceName,
			String memo, String enterpriseCode) {
		this.importanceId = importanceId;
		this.importanceName = importanceName;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "IMPORTANCE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getImportanceId() {
		return this.importanceId;
	}

	public void setImportanceId(Long importanceId) {
		this.importanceId = importanceId;
	}

	@Column(name = "IMPORTANCE_NAME", length = 80)
	public String getImportanceName() {
		return this.importanceName;
	}

	public void setImportanceName(String importanceName) {
		this.importanceName = importanceName;
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