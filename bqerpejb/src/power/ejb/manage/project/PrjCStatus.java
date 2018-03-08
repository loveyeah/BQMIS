package power.ejb.manage.project;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PrjCStatus entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PRJ_C_STATUS")
public class PrjCStatus implements java.io.Serializable {
      
	// Fields

	private Long prjStatusId;
	private String prjStatusName;
	private String prjStatusType;

	// Constructors

	/** default constructor */
	public PrjCStatus() {
	}

	/** full constructor */
	public PrjCStatus(Long prjStatusId, String prjStatusName) {
		this.prjStatusId = prjStatusId;
		this.prjStatusName = prjStatusName;
	}

	// Property accessors
	@Id
	@Column(name = "PRJ_STATUS_ID", unique = true, nullable = false, precision = 2, scale = 0)
	public Long getPrjStatusId() {
		return this.prjStatusId;
	}

	public void setPrjStatusId(Long prjStatusId) {
		this.prjStatusId = prjStatusId;
	}

	@Column(name = "PRJ_STATUS_NAME", nullable = false, length = 50)
	public String getPrjStatusName() {
		return this.prjStatusName;
	}

	public void setPrjStatusName(String prjStatusName) {
		this.prjStatusName = prjStatusName;
	}

	@Column(name = "PRJ_STATUS_TYPE", nullable = false, length = 2)
	public String getPrjStatusType() {
		return prjStatusType;
	}

	public void setPrjStatusType(String prjStatusType) {
		this.prjStatusType = prjStatusType;
	}

}