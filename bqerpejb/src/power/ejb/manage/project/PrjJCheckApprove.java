package power.ejb.manage.project;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PrjJCheckApprove entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRJ_J_CHECK_APPROVE", schema = "POWER")
public class PrjJCheckApprove implements java.io.Serializable {

	// Fields

	private Long approveId;
	private Long checkId;
	private String approveBy;
	private Date approveDate;
	private String approveText;
	private Long statusId;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public PrjJCheckApprove() {
	}

	/** minimal constructor */
	public PrjJCheckApprove(Long approveId) {
		this.approveId = approveId;
	}

	/** full constructor */
	public PrjJCheckApprove(Long approveId, Long checkId, String approveBy,
			Date approveDate, String approveText, Long statusId,
			String enterpriseCode, String isUse) {
		this.approveId = approveId;
		this.checkId = checkId;
		this.approveBy = approveBy;
		this.approveDate = approveDate;
		this.approveText = approveText;
		this.statusId = statusId;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "APPROVE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getApproveId() {
		return this.approveId;
	}

	public void setApproveId(Long approveId) {
		this.approveId = approveId;
	}

	@Column(name = "CHECK_ID", precision = 10, scale = 0)
	public Long getCheckId() {
		return this.checkId;
	}

	public void setCheckId(Long checkId) {
		this.checkId = checkId;
	}

	@Column(name = "APPROVE_BY", length = 20)
	public String getApproveBy() {
		return this.approveBy;
	}

	public void setApproveBy(String approveBy) {
		this.approveBy = approveBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APPROVE_DATE", length = 7)
	public Date getApproveDate() {
		return this.approveDate;
	}

	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}

	@Column(name = "APPROVE_TEXT", length = 200)
	public String getApproveText() {
		return this.approveText;
	}

	public void setApproveText(String approveText) {
		this.approveText = approveText;
	}

	@Column(name = "STATUS_ID", precision = 2, scale = 0)
	public Long getStatusId() {
		return this.statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

}