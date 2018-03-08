package power.ejb.productiontec.chemistry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PtHxjdCHxzxybwh entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_HXJD_C_HXZXYBWH")
public class PtHxjdCHxzxybwh implements java.io.Serializable {

	// Fields

	private Long meterId;
	private String meterName;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtHxjdCHxzxybwh() {
	}

	/** minimal constructor */
	public PtHxjdCHxzxybwh(Long meterId) {
		this.meterId = meterId;
	}

	/** full constructor */
	public PtHxjdCHxzxybwh(Long meterId, String meterName, String enterpriseCode) {
		this.meterId = meterId;
		this.meterName = meterName;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "METER_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getMeterId() {
		return this.meterId;
	}

	public void setMeterId(Long meterId) {
		this.meterId = meterId;
	}

	@Column(name = "METER_NAME", length = 50)
	public String getMeterName() {
		return this.meterName;
	}

	public void setMeterName(String meterName) {
		this.meterName = meterName;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}