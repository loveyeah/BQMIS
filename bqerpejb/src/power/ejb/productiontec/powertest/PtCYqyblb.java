package power.ejb.productiontec.powertest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PtCYqyblb entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PT_C_YQYBLB")
public class PtCYqyblb implements java.io.Serializable {

	// Fields

	private Long yqyblbId;
	private String yqyblbName;
	private Long jdzyId;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtCYqyblb() {
	}

	/** minimal constructor */
	public PtCYqyblb(Long yqyblbId, Long jdzyId) {
		this.yqyblbId = yqyblbId;
		this.jdzyId = jdzyId;
	}

	/** full constructor */
	public PtCYqyblb(Long yqyblbId, String yqyblbName, Long jdzyId,
			String enterpriseCode) {
		this.yqyblbId = yqyblbId;
		this.yqyblbName = yqyblbName;
		this.jdzyId = jdzyId;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "YQYBLB_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getYqyblbId() {
		return this.yqyblbId;
	}

	public void setYqyblbId(Long yqyblbId) {
		this.yqyblbId = yqyblbId;
	}

	@Column(name = "YQYBLB_NAME", length = 50)
	public String getYqyblbName() {
		return this.yqyblbName;
	}

	public void setYqyblbName(String yqyblbName) {
		this.yqyblbName = yqyblbName;
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