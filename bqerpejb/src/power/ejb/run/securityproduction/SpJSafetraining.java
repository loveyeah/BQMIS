package power.ejb.run.securityproduction;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SpJSafetraining entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SP_J_SAFETRAINING", schema = "POWER")
public class SpJSafetraining implements java.io.Serializable {

	// Fields

	private Long trainingId;
	private String depCode;
	private String trainingSubject;
	private Date trainingTime;
	private String trainingSpeaker;
	private String completion;
	private String content;
	private String memo;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public SpJSafetraining() {
	}

	/** minimal constructor */
	public SpJSafetraining(Long trainingId) {
		this.trainingId = trainingId;
	}

	/** full constructor */
	public SpJSafetraining(Long trainingId, String depCode,
			String trainingSubject, Date trainingTime, String trainingSpeaker,
			String completion, String content, String memo,
			String enterpriseCode) {
		this.trainingId = trainingId;
		this.depCode = depCode;
		this.trainingSubject = trainingSubject;
		this.trainingTime = trainingTime;
		this.trainingSpeaker = trainingSpeaker;
		this.completion = completion;
		this.content = content;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "TRAINING_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getTrainingId() {
		return this.trainingId;
	}

	public void setTrainingId(Long trainingId) {
		this.trainingId = trainingId;
	}

	@Column(name = "DEP_CODE", length = 20)
	public String getDepCode() {
		return this.depCode;
	}

	public void setDepCode(String depCode) {
		this.depCode = depCode;
	}

	@Column(name = "TRAINING_SUBJECT", length = 100)
	public String getTrainingSubject() {
		return this.trainingSubject;
	}

	public void setTrainingSubject(String trainingSubject) {
		this.trainingSubject = trainingSubject;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "TRAINING_TIME", length = 7)
	public Date getTrainingTime() {
		return this.trainingTime;
	}

	public void setTrainingTime(Date trainingTime) {
		this.trainingTime = trainingTime;
	}

	@Column(name = "TRAINING_SPEAKER", length = 16)
	public String getTrainingSpeaker() {
		return this.trainingSpeaker;
	}

	public void setTrainingSpeaker(String trainingSpeaker) {
		this.trainingSpeaker = trainingSpeaker;
	}

	@Column(name = "COMPLETION", length = 20)
	public String getCompletion() {
		return this.completion;
	}

	public void setCompletion(String completion) {
		this.completion = completion;
	}

	@Column(name = "CONTENT", length = 2000)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "MEMO", length = 500)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}