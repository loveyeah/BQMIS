package power.ejb.productiontec.powertest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PtJYqybjycsz entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PT_J_YQYBJYCSZ")
public class PtJYqybjycsz implements java.io.Serializable {

	// Fields

	private Long jycszId;
	private Long jyjlId;
	private Long parameterId;
	private String parameterValue;
	private Long jdzyId;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJYqybjycsz() {
	}

	/** minimal constructor */
	public PtJYqybjycsz(Long jycszId) {
		this.jycszId = jycszId;
	}

	/** full constructor */
	public PtJYqybjycsz(Long jycszId, Long jyjlId, Long parameterId,
			String parameterValue, Long jdzyId, String enterpriseCode) {
		this.jycszId = jycszId;
		this.jyjlId = jyjlId;
		this.parameterId = parameterId;
		this.parameterValue = parameterValue;
		this.jdzyId = jdzyId;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "JYCSZ_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getJycszId() {
		return this.jycszId;
	}

	public void setJycszId(Long jycszId) {
		this.jycszId = jycszId;
	}

	@Column(name = "JYJL_ID", precision = 10, scale = 0)
	public Long getJyjlId() {
		return this.jyjlId;
	}

	public void setJyjlId(Long jyjlId) {
		this.jyjlId = jyjlId;
	}

	@Column(name = "PARAMETER_ID", precision = 10, scale = 0)
	public Long getParameterId() {
		return this.parameterId;
	}

	public void setParameterId(Long parameterId) {
		this.parameterId = parameterId;
	}

	@Column(name = "PARAMETER_VALUE", length = 10)
	public String getParameterValue() {
		return this.parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	@Column(name = "JDZY_ID", precision = 2, scale = 0)
	public Long getJdzyId() {
		return this.jdzyId;
	}

	public void setJdzyId(Long jdzyId) {
		this.jdzyId = jdzyId;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}