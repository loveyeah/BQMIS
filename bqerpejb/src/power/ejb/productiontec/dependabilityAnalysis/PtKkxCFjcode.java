package power.ejb.productiontec.dependabilityAnalysis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PtKkxCFjcode entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_KKX_C_FJCODE")
public class PtKkxCFjcode implements java.io.Serializable {

	// Fields

	private String fjCode;
	private String fjName;
	private Long orderBy;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtKkxCFjcode() {
	}

	/** minimal constructor */
	public PtKkxCFjcode(String fjCode) {
		this.fjCode = fjCode;
	}

	/** full constructor */
	public PtKkxCFjcode(String fjCode, String fjName, Long orderBy,
			String enterpriseCode) {
		this.fjCode = fjCode;
		this.fjName = fjName;
		this.orderBy = orderBy;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "FJ_CODE", unique = true, nullable = false, length = 20)
	public String getFjCode() {
		return this.fjCode;
	}

	public void setFjCode(String fjCode) {
		this.fjCode = fjCode;
	}

	@Column(name = "FJ_NAME", length = 30)
	public String getFjName() {
		return this.fjName;
	}

	public void setFjName(String fjName) {
		this.fjName = fjName;
	}

	@Column(name = "ORDER_BY", precision = 10, scale = 0)
	public Long getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}