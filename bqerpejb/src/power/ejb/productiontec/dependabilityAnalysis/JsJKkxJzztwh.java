package power.ejb.productiontec.dependabilityAnalysis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * JsJKkxJzztwh entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "JS_J_KKX_JZZTWH")
public class JsJKkxJzztwh implements java.io.Serializable {

	// Fields

	private Long jzztId;
	private String jzztCode;
	private String jzztName;
	private String daddyCode;
	private String jzztDescription;
	private Long displayNo;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public JsJKkxJzztwh() {
	}

	/** minimal constructor */
	public JsJKkxJzztwh(Long jzztId) {
		this.jzztId = jzztId;
	}

	/** full constructor */
	public JsJKkxJzztwh(Long jzztId, String jzztCode, String jzztName,
			String daddyCode, String jzztDescription, Long displayNo,
			String enterpriseCode) {
		this.jzztId = jzztId;
		this.jzztCode = jzztCode;
		this.jzztName = jzztName;
		this.daddyCode = daddyCode;
		this.jzztDescription = jzztDescription;
		this.displayNo = displayNo;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "JZZT_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getJzztId() {
		return this.jzztId;
	}

	public void setJzztId(Long jzztId) {
		this.jzztId = jzztId;
	}

	@Column(name = "JZZT_CODE", length = 20)
	public String getJzztCode() {
		return this.jzztCode;
	}

	public void setJzztCode(String jzztCode) {
		this.jzztCode = jzztCode;
	}

	@Column(name = "JZZT_NAME", length = 50)
	public String getJzztName() {
		return this.jzztName;
	}

	public void setJzztName(String jzztName) {
		this.jzztName = jzztName;
	}

	@Column(name = "DADDY_CODE", length = 20)
	public String getDaddyCode() {
		return this.daddyCode;
	}

	public void setDaddyCode(String daddyCode) {
		this.daddyCode = daddyCode;
	}

	@Column(name = "JZZT_DESCRIPTION", length = 200)
	public String getJzztDescription() {
		return this.jzztDescription;
	}

	public void setJzztDescription(String jzztDescription) {
		this.jzztDescription = jzztDescription;
	}

	@Column(name = "DISPLAY_NO", precision = 4, scale = 0)
	public Long getDisplayNo() {
		return this.displayNo;
	}

	public void setDisplayNo(Long displayNo) {
		this.displayNo = displayNo;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}