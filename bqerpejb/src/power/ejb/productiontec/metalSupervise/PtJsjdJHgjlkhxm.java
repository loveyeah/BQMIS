package power.ejb.productiontec.metalSupervise;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PtJsjdJHgjlkhxm entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PT_JSJD_J_HGJLKHXM")
public class PtJsjdJHgjlkhxm implements java.io.Serializable {

	// Fields

	private Long hgjnkhxmId;
	private Long hgjnkhId;
	private String examName;
	private String material;
	private String sizes;
	private String method;
	private String results;
	private String allowWork;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJsjdJHgjlkhxm() {
	}

	/** minimal constructor */
	public PtJsjdJHgjlkhxm(Long hgjnkhxmId) {
		this.hgjnkhxmId = hgjnkhxmId;
	}

	/** full constructor */
	public PtJsjdJHgjlkhxm(Long hgjnkhxmId, Long hgjnkhId, String examName,
			String material, String sizes, String method, String results,
			String allowWork, String enterpriseCode) {
		this.hgjnkhxmId = hgjnkhxmId;
		this.hgjnkhId = hgjnkhId;
		this.examName = examName;
		this.material = material;
		this.sizes = sizes;
		this.method = method;
		this.results = results;
		this.allowWork = allowWork;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "HGJNKHXM_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getHgjnkhxmId() {
		return this.hgjnkhxmId;
	}

	public void setHgjnkhxmId(Long hgjnkhxmId) {
		this.hgjnkhxmId = hgjnkhxmId;
	}

	@Column(name = "HGJNKH_ID", precision = 10, scale = 0)
	public Long getHgjnkhId() {
		return this.hgjnkhId;
	}

	public void setHgjnkhId(Long hgjnkhId) {
		this.hgjnkhId = hgjnkhId;
	}

	@Column(name = "EXAM_NAME", length = 50)
	public String getExamName() {
		return this.examName;
	}

	public void setExamName(String examName) {
		this.examName = examName;
	}

	@Column(name = "MATERIAL", length = 20)
	public String getMaterial() {
		return this.material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	@Column(name = "SIZES", length = 20)
	public String getSizes() {
		return this.sizes;
	}

	public void setSizes(String sizes) {
		this.sizes = sizes;
	}

	@Column(name = "METHOD", length = 40)
	public String getMethod() {
		return this.method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@Column(name = "RESULTS", length = 10)
	public String getResults() {
		return this.results;
	}

	public void setResults(String results) {
		this.results = results;
	}

	@Column(name = "ALLOW_WORK", length = 50)
	public String getAllowWork() {
		return this.allowWork;
	}

	public void setAllowWork(String allowWork) {
		this.allowWork = allowWork;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}