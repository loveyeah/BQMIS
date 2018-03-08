package power.ejb.run.runlog.shift;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunJShiftRecord entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_J_SHIFT_RECORD", schema = "POWER")
public class RunJShiftRecord implements java.io.Serializable {

	// Fields

	private Long shiftRecordId;
	private Long runLogId;
	private String mainItemCode;
	private String recordContent;
	private String filePath;
	private String recordBy;
	private Date recordTime;
	private String reviewNo;
	private String reviewType;
	private String checkBy;
	private String checkMemo;
	private Date checkTime;
	private String isCompletion;
	private Long notCompletionId;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public RunJShiftRecord() {
	}

	/** minimal constructor */
	public RunJShiftRecord(Long shiftRecordId) {
		this.shiftRecordId = shiftRecordId;
	}

	/** full constructor */
	public RunJShiftRecord(Long shiftRecordId, Long runLogId,
			String mainItemCode, String recordContent, String filePath,
			String recordBy, Date recordTime, String reviewNo,
			String reviewType, String checkBy, String checkMemo,
			Date checkTime, String isCompletion, String isUse, Long notCompletionId,
			String enterpriseCode) {
		this.shiftRecordId = shiftRecordId;
		this.runLogId = runLogId;
		this.mainItemCode = mainItemCode;
		this.recordContent = recordContent;
		this.filePath = filePath;
		this.recordBy = recordBy;
		this.recordTime = recordTime;
		this.reviewNo = reviewNo;
		this.reviewType = reviewType;
		this.checkBy = checkBy;
		this.checkMemo = checkMemo;
		this.checkTime = checkTime;
		this.isCompletion = isCompletion;
		this.notCompletionId = notCompletionId;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}
	
	// Property accessors
	@Id
	@Column(name = "SHIFT_RECORD_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getShiftRecordId() {
		return this.shiftRecordId;
	}

	public void setShiftRecordId(Long shiftRecordId) {
		this.shiftRecordId = shiftRecordId;
	}

	@Column(name = "RUN_LOG_ID", precision = 10, scale = 0)
	public Long getRunLogId() {
		return this.runLogId;
	}

	public void setRunLogId(Long runLogId) {
		this.runLogId = runLogId;
	}

	@Column(name = "MAIN_ITEM_CODE", length = 10)
	public String getMainItemCode() {
		return this.mainItemCode;
	}

	public void setMainItemCode(String mainItemCode) {
		this.mainItemCode = mainItemCode;
	}

	@Column(name = "RECORD_CONTENT", length = 600)
	public String getRecordContent() {
		return this.recordContent;
	}

	public void setRecordContent(String recordContent) {
		this.recordContent = recordContent;
	}

	@Column(name = "FILE_PATH", length = 250)
	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Column(name = "RECORD_BY", length = 15)
	public String getRecordBy() {
		return this.recordBy;
	}

	public void setRecordBy(String recordBy) {
		this.recordBy = recordBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RECORD_TIME", length = 7)
	public Date getRecordTime() {
		return this.recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}

	@Column(name = "REVIEW_NO", length = 20)
	public String getReviewNo() {
		return this.reviewNo;
	}

	public void setReviewNo(String reviewNo) {
		this.reviewNo = reviewNo;
	}

	@Column(name = "REVIEW_TYPE", length = 10)
	public String getReviewType() {
		return this.reviewType;
	}

	public void setReviewType(String reviewType) {
		this.reviewType = reviewType;
	}

	@Column(name = "CHECK_BY", length = 15)
	public String getCheckBy() {
		return this.checkBy;
	}

	public void setCheckBy(String checkBy) {
		this.checkBy = checkBy;
	}

	@Column(name = "CHECK_MEMO", length = 500)
	public String getCheckMemo() {
		return this.checkMemo;
	}

	public void setCheckMemo(String checkMemo) {
		this.checkMemo = checkMemo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CHECK_TIME", length = 7)
	public Date getCheckTime() {
		return this.checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	@Column(name = "IS_COMPLETION", length = 1)
	public String getIsCompletion() {
		return this.isCompletion;
	}

	public void setIsCompletion(String isCompletion) {
		this.isCompletion = isCompletion;
	}
	
	@Column(name = "NOT_COMPLETION_ID", precision = 10, scale = 0)
	public Long getNotCompletionId() {
		return this.notCompletionId;
	}

	public void setNotCompletionId(Long notCompletionId) {
		this.notCompletionId = notCompletionId;
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