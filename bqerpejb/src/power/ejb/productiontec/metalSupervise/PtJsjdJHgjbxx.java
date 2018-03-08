package power.ejb.productiontec.metalSupervise;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtJsjdJHgjbxx entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PT_JSJD_J_HGJBXX")
public class PtJsjdJHgjbxx implements java.io.Serializable {

	// Fields

	private Long weldId;
	private String workerCode;
	private String weldCode;
	private Date weldWorkDate;
	private Long weldAge;
	private String enterpriseCode;

	// 焊工等级 add by drdu 091106
	private String weldLevel;
	// Constructors

	/** default constructor */
	public PtJsjdJHgjbxx() {
	}

	/** minimal constructor */
	public PtJsjdJHgjbxx(Long weldId) {
		this.weldId = weldId;
	}

	/** full constructor */
	public PtJsjdJHgjbxx(Long weldId, String workerCode, String weldCode,
			Date weldWorkDate, Long weldAge, String enterpriseCode) {
		this.weldId = weldId;
		this.workerCode = workerCode;
		this.weldCode = weldCode;
		this.weldWorkDate = weldWorkDate;
		this.weldAge = weldAge;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "WELD_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getWeldId() {
		return this.weldId;
	}

	public void setWeldId(Long weldId) {
		this.weldId = weldId;
	}

	@Column(name = "WORKER_CODE", length = 16)
	public String getWorkerCode() {
		return this.workerCode;
	}

	public void setWorkerCode(String workerCode) {
		this.workerCode = workerCode;
	}

	@Column(name = "WELD_CODE", length = 10)
	public String getWeldCode() {
		return this.weldCode;
	}

	public void setWeldCode(String weldCode) {
		this.weldCode = weldCode;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "WELD_WORK_DATE", length = 7)
	public Date getWeldWorkDate() {
		return this.weldWorkDate;
	}

	public void setWeldWorkDate(Date weldWorkDate) {
		this.weldWorkDate = weldWorkDate;
	}

	@Column(name = "WELD_AGE", precision = 4, scale = 0)
	public Long getWeldAge() {
		return this.weldAge;
	}

	public void setWeldAge(Long weldAge) {
		this.weldAge = weldAge;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "WELD_LEVEL", length = 30)
	public String getWeldLevel() {
		return weldLevel;
	}

	public void setWeldLevel(String weldLevel) {
		this.weldLevel = weldLevel;
	}

}