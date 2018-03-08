package power.ejb.productiontec.relayProtection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PtJdbhJSybgjl entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_JDBH_J_SYBGJL", schema = "POWER")
public class PtJdbhJSybgjl implements java.io.Serializable {

	// Fields

	private Long jdsybgjgId;
	private Long jdsybgId;
	private Long sydId;
	private Long regulatorId;
	private Double result;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJdbhJSybgjl() {
	}

	/** minimal constructor */
	public PtJdbhJSybgjl(Long jdsybgjgId) {
		this.jdsybgjgId = jdsybgjgId;
	}

	/** full constructor */
	public PtJdbhJSybgjl(Long jdsybgjgId, Long jdsybgId, Long sydId,
			Long regulatorId, Double result, String enterpriseCode) {
		this.jdsybgjgId = jdsybgjgId;
		this.jdsybgId = jdsybgId;
		this.sydId = sydId;
		this.regulatorId = regulatorId;
		this.result = result;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "JDSYBGJG_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getJdsybgjgId() {
		return this.jdsybgjgId;
	}

	public void setJdsybgjgId(Long jdsybgjgId) {
		this.jdsybgjgId = jdsybgjgId;
	}

	@Column(name = "JDSYBG_ID", precision = 10, scale = 0)
	public Long getJdsybgId() {
		return this.jdsybgId;
	}

	public void setJdsybgId(Long jdsybgId) {
		this.jdsybgId = jdsybgId;
	}

	@Column(name = "SYD_ID", precision = 10, scale = 0)
	public Long getSydId() {
		return this.sydId;
	}

	public void setSydId(Long sydId) {
		this.sydId = sydId;
	}

	@Column(name = "REGULATOR_ID", precision = 10, scale = 0)
	public Long getRegulatorId() {
		return this.regulatorId;
	}

	public void setRegulatorId(Long regulatorId) {
		this.regulatorId = regulatorId;
	}

	@Column(name = "RESULT", precision = 10)
	public Double getResult() {
		return this.result;
	}

	public void setResult(Double result) {
		this.result = result;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}