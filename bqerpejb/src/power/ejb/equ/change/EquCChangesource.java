package power.ejb.equ.change;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EquCChangesource entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EQU_C_CHANGESOURCE")
public class EquCChangesource implements java.io.Serializable {

	// Fields

	private Long sourceId;
	private String sourceCode;
	private String sourceName;
	private String memo;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public EquCChangesource() {
	}

	/** minimal constructor */
	public EquCChangesource(Long sourceId) {
		this.sourceId = sourceId;
	}

	/** full constructor */
	public EquCChangesource(Long sourceId, String sourceCode,
			String sourceName, String memo, String enterpriseCode, String isUse) {
		this.sourceId = sourceId;
		this.sourceCode = sourceCode;
		this.sourceName = sourceName;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "SOURCE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getSourceId() {
		return this.sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	@Column(name = "SOURCE_CODE", length = 10)
	public String getSourceCode() {
		return this.sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	@Column(name = "SOURCE_NAME", length = 60)
	public String getSourceName() {
		return this.sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	@Column(name = "MEMO", length = 1000)
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

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

}