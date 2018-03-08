package power.ejb.manage.client;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ConJAppraise entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CON_J_APPRAISE")
public class ConJAppraise implements java.io.Serializable {

	// Fields

	private Long appraisalId;
	private Long cliendId;
	private Long intervalId;
	private Double totalScore;
	private String gatherFlag;
	private String appraiseBy;
	private Date appraiseDate;
	private String appraisalResult;
	private String gatherBy;
	private Date gatherDate;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public ConJAppraise() {
	}

	/** minimal constructor */
	public ConJAppraise(Long appraisalId) {
		this.appraisalId = appraisalId;
	}

	/** full constructor */
	public ConJAppraise(Long appraisalId, Long cliendId, Long intervalId,
			Double totalScore, String gatherFlag, String appraiseBy,
			Date appraiseDate, String appraisalResult, String gatherBy,
			Date gatherDate, String enterpriseCode) {
		this.appraisalId = appraisalId;
		this.cliendId = cliendId;
		this.intervalId = intervalId;
		this.totalScore = totalScore;
		this.gatherFlag = gatherFlag;
		this.appraiseBy = appraiseBy;
		this.appraiseDate = appraiseDate;
		this.appraisalResult = appraisalResult;
		this.gatherBy = gatherBy;
		this.gatherDate = gatherDate;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "APPRAISAL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getAppraisalId() {
		return this.appraisalId;
	}

	public void setAppraisalId(Long appraisalId) {
		this.appraisalId = appraisalId;
	}

	@Column(name = "CLIEND_ID", precision = 10, scale = 0)
	public Long getCliendId() {
		return this.cliendId;
	}

	public void setCliendId(Long cliendId) {
		this.cliendId = cliendId;
	}

	@Column(name = "INTERVAL_ID", precision = 10, scale = 0)
	public Long getIntervalId() {
		return this.intervalId;
	}

	public void setIntervalId(Long intervalId) {
		this.intervalId = intervalId;
	}

	@Column(name = "TOTAL_SCORE", precision = 4, scale = 1)
	public Double getTotalScore() {
		return this.totalScore;
	}

	public void setTotalScore(Double totalScore) {
		this.totalScore = totalScore;
	}

	@Column(name = "GATHER_FLAG", length = 1)
	public String getGatherFlag() {
		return this.gatherFlag;
	}

	public void setGatherFlag(String gatherFlag) {
		this.gatherFlag = gatherFlag;
	}

	@Column(name = "APPRAISE_BY", length = 16)
	public String getAppraiseBy() {
		return this.appraiseBy;
	}

	public void setAppraiseBy(String appraiseBy) {
		this.appraiseBy = appraiseBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APPRAISE_DATE", length = 7)
	public Date getAppraiseDate() {
		return this.appraiseDate;
	}

	public void setAppraiseDate(Date appraiseDate) {
		this.appraiseDate = appraiseDate;
	}

	@Column(name = "APPRAISAL_RESULT", length = 256)
	public String getAppraisalResult() {
		return this.appraisalResult;
	}

	public void setAppraisalResult(String appraisalResult) {
		this.appraisalResult = appraisalResult;
	}

	@Column(name = "GATHER_BY", length = 16)
	public String getGatherBy() {
		return this.gatherBy;
	}

	public void setGatherBy(String gatherBy) {
		this.gatherBy = gatherBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "GATHER_DATE", length = 7)
	public Date getGatherDate() {
		return this.gatherDate;
	}

	public void setGatherDate(Date gatherDate) {
		this.gatherDate = gatherDate;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}