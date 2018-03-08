package power.ejb.productiontec.powertest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PtCYqybdj entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PT_C_YQYBDJ")
public class PtCYqybdj implements java.io.Serializable {

	// Fields

	private Long yqybdjId;
	private String yqybdjName;
	private Long jdzyId;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtCYqybdj() {
	}

	/** minimal constructor */
	public PtCYqybdj(Long yqybdjId, Long jdzyId) {
		this.yqybdjId = yqybdjId;
		this.jdzyId = jdzyId;
	}

	/** full constructor */
	public PtCYqybdj(Long yqybdjId, String yqybdjName, Long jdzyId,
			String enterpriseCode) {
		this.yqybdjId = yqybdjId;
		this.yqybdjName = yqybdjName;
		this.jdzyId = jdzyId;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "YQYBDJ_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getYqybdjId() {
		return this.yqybdjId;
	}

	public void setYqybdjId(Long yqybdjId) {
		this.yqybdjId = yqybdjId;
	}

	@Column(name = "YQYBDJ_NAME", length = 50)
	public String getYqybdjName() {
		return this.yqybdjName;
	}

	public void setYqybdjName(String yqybdjName) {
		this.yqybdjName = yqybdjName;
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