package power.ejb.productiontec.powertest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PtCYqybjycs entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PT_C_YQYBJYCS")
public class PtCYqybjycs implements java.io.Serializable {

	// Fields

	private Long parameterId;
	private Long yqyblbId;
	private String parameterNames;
	private String memo;
	private Long jdzyId;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtCYqybjycs() {
	}

	/** minimal constructor */
	public PtCYqybjycs(Long parameterId, Long yqyblbId) {
		this.parameterId = parameterId;
		this.yqyblbId = yqyblbId;
	}

	/** full constructor */
	public PtCYqybjycs(Long parameterId, Long yqyblbId, String parameterNames,
			String memo, Long jdzyId, String enterpriseCode) {
		this.parameterId = parameterId;
		this.yqyblbId = yqyblbId;
		this.parameterNames = parameterNames;
		this.memo = memo;
		this.jdzyId = jdzyId;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "PARAMETER_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getParameterId() {
		return this.parameterId;
	}

	public void setParameterId(Long parameterId) {
		this.parameterId = parameterId;
	}

	@Column(name = "YQYBLB_ID", nullable = false, precision = 10, scale = 0)
	public Long getYqyblbId() {
		return this.yqyblbId;
	}

	public void setYqyblbId(Long yqyblbId) {
		this.yqyblbId = yqyblbId;
	}

	@Column(name = "PARAMETER_NAMES", length = 30)
	public String getParameterNames() {
		return this.parameterNames;
	}

	public void setParameterNames(String parameterNames) {
		this.parameterNames = parameterNames;
	}

	@Column(name = "MEMO", length = 100)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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