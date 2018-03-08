package power.ejb.run.protectinoutapply;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunJProtectinoutApprove entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_J_PROTECTINOUT_APPROVE")
public class RunJProtectinoutApprove implements java.io.Serializable {

	// Fields

	private Long approveId;
	private String protectNo;
	private String approveBy;
	private Date approveDate;
	private String approveText;
	private Long approveStatus;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public RunJProtectinoutApprove() {
	}

	/** minimal constructor */
	public RunJProtectinoutApprove(Long approveId) {
		this.approveId = approveId;
	}

	/** full constructor */
	public RunJProtectinoutApprove(Long approveId, String protectNo,
			String approveBy, Date approveDate, String approveText,
			Long approveStatus, String enterpriseCode) {
		this.approveId = approveId;
		this.protectNo = protectNo;
		this.approveBy = approveBy;
		this.approveDate = approveDate;
		this.approveText = approveText;
		this.approveStatus = approveStatus;
		this.enterpriseCode = enterpriseCode;
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

	@Column(name = "PROTECT_NO", length = 15)
	public String getProtectNo() {
		return this.protectNo;
	}

	public void setProtectNo(String protectNo) {
		this.protectNo = protectNo;
	}

	@Column(name = "APPROVE_BY", length = 30)
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

	@Column(name = "APPROVE_TEXT", length = 300)
	public String getApproveText() {
		return this.approveText;
	}

	public void setApproveText(String approveText) {
		this.approveText = approveText;
	}

	@Column(name = "APPROVE_STATUS", precision = 10, scale = 0)
	public Long getApproveStatus() {
		return this.approveStatus;
	}

	public void setApproveStatus(Long approveStatus) {
		this.approveStatus = approveStatus;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}