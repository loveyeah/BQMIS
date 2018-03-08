package power.ejb.manage.plan.trainplan;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * BpJTrainingSum entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "BP_J_TRAINING_SUM")
public class BpJTrainingSum implements java.io.Serializable {

	// Fields

	private Long trainingMainId;
	private Long approvalId;
	private Date trainingMonth;
	private String trainingDep;
	private Long trainingTypeId;
	private Long trainingNumber;
	private Long finishNumber;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpJTrainingSum() {
	}

	/** minimal constructor */
	public BpJTrainingSum(Long trainingMainId, Long approvalId,
			Date trainingMonth, String trainingDep, Long trainingTypeId) {
		this.trainingMainId = trainingMainId;
		this.approvalId = approvalId;
		this.trainingMonth = trainingMonth;
		this.trainingDep = trainingDep;
		this.trainingTypeId = trainingTypeId;
	}

	/** full constructor */
	public BpJTrainingSum(Long trainingMainId, Long approvalId,
			Date trainingMonth, String trainingDep, Long trainingTypeId,
			Long trainingNumber, Long finishNumber, String isUse,
			String enterpriseCode) {
		this.trainingMainId = trainingMainId;
		this.approvalId = approvalId;
		this.trainingMonth = trainingMonth;
		this.trainingDep = trainingDep;
		this.trainingTypeId = trainingTypeId;
		this.trainingNumber = trainingNumber;
		this.finishNumber = finishNumber;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "TRAINING_MAIN_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getTrainingMainId() {
		return this.trainingMainId;
	}

	public void setTrainingMainId(Long trainingMainId) {
		this.trainingMainId = trainingMainId;
	}

	@Column(name = "APPROVAL_ID", precision = 10, scale = 0)
	public Long getApprovalId() {
		return this.approvalId;
	}

	public void setApprovalId(Long approvalId) {
		this.approvalId = approvalId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "TRAINING_MONTH", nullable = false, length = 7)
	public Date getTrainingMonth() {
		return this.trainingMonth;
	}

	public void setTrainingMonth(Date trainingMonth) {
		this.trainingMonth = trainingMonth;
	}

	@Column(name = "TRAINING_DEP", nullable = false, length = 20)
	public String getTrainingDep() {
		return this.trainingDep;
	}

	public void setTrainingDep(String trainingDep) {
		this.trainingDep = trainingDep;
	}

	@Column(name = "TRAINING_TYPE_ID", nullable = false, precision = 10, scale = 0)
	public Long getTrainingTypeId() {
		return this.trainingTypeId;
	}

	public void setTrainingTypeId(Long trainingTypeId) {
		this.trainingTypeId = trainingTypeId;
	}

	@Column(name = "TRAINING_NUMBER", precision = 5, scale = 0)
	public Long getTrainingNumber() {
		return this.trainingNumber;
	}

	public void setTrainingNumber(Long trainingNumber) {
		this.trainingNumber = trainingNumber;
	}

	@Column(name = "FINISH_NUMBER", precision = 5, scale = 0)
	public Long getFinishNumber() {
		return this.finishNumber;
	}

	public void setFinishNumber(Long finishNumber) {
		this.finishNumber = finishNumber;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}