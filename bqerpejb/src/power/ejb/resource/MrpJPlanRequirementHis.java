package power.ejb.resource;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * MrpJPlanRequirementHis entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "MRP_J_PLAN_REQUIREMENT_HIS")
public class MrpJPlanRequirementHis implements java.io.Serializable {

	// Fields

	private Long approveId;
	private String mrNo;
	private String approveBy;
	private Date approveDate;
	private String approveText;
	private String approveStatus;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public MrpJPlanRequirementHis() {
	}

	/** minimal constructor */
	public MrpJPlanRequirementHis(Long approveId) {
		this.approveId = approveId;
	}

	/** full constructor */
	public MrpJPlanRequirementHis(Long approveId, String mrNo,
			String approveBy, Date approveDate, String approveText,
			String approveStatus, String enterpriseCode) {
		this.approveId = approveId;
		this.mrNo = mrNo;
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

	@Column(name = "MR_NO", length = 20)
	public String getMrNo() {
		return this.mrNo;
	}

	public void setMrNo(String mrNo) {
		this.mrNo = mrNo;
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

	@Column(name = "APPROVE_STATUS",length = 1)
	public String getApproveStatus() {
		return this.approveStatus;
	}

	public void setApproveStatus(String approveStatus) {
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