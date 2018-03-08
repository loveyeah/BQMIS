package power.ejb.productiontec.powertest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PtCYqybjd entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PT_C_YQYBJD")
public class PtCYqybjd implements java.io.Serializable {

	// Fields

	private Long yqybjdId;
	private String yqybjdName;
	private Long jdzyId;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtCYqybjd() {
	}

	/** minimal constructor */
	public PtCYqybjd(Long yqybjdId, Long jdzyId) {
		this.yqybjdId = yqybjdId;
		this.jdzyId = jdzyId;
	}

	/** full constructor */
	public PtCYqybjd(Long yqybjdId, String yqybjdName, Long jdzyId,
			String enterpriseCode) {
		this.yqybjdId = yqybjdId;
		this.yqybjdName = yqybjdName;
		this.jdzyId = jdzyId;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "YQYBJD_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getYqybjdId() {
		return this.yqybjdId;
	}

	public void setYqybjdId(Long yqybjdId) {
		this.yqybjdId = yqybjdId;
	}

	@Column(name = "YQYBJD_NAME", length = 50)
	public String getYqybjdName() {
		return this.yqybjdName;
	}

	public void setYqybjdName(String yqybjdName) {
		this.yqybjdName = yqybjdName;
	}

	@Column(name = "JDZY_ID", nullable = false, precision = 2, scale = 0)
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