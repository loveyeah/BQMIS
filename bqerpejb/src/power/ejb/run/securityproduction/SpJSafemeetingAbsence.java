package power.ejb.run.securityproduction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SpJSafemeetingAbsence entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "SP_J_SAFEMEETING_ABSENCE", schema = "POWER")
public class SpJSafemeetingAbsence implements java.io.Serializable {

	// Fields

	private Long absenceId;
	private Long meetingId;
	private String workerCode;
	private String depCode;
	private String reason;
	private String makeupRecord;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public SpJSafemeetingAbsence() {
	}

	/** minimal constructor */
	public SpJSafemeetingAbsence(Long absenceId) {
		this.absenceId = absenceId;
	}

	/** full constructor */
	public SpJSafemeetingAbsence(Long absenceId, Long meetingId,
			String workerCode, String depCode, String reason,
			String makeupRecord, String enterpriseCode) {
		this.absenceId = absenceId;
		this.meetingId = meetingId;
		this.workerCode = workerCode;
		this.depCode = depCode;
		this.reason = reason;
		this.makeupRecord = makeupRecord;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "ABSENCE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getAbsenceId() {
		return this.absenceId;
	}

	public void setAbsenceId(Long absenceId) {
		this.absenceId = absenceId;
	}

	@Column(name = "MEETING_ID", precision = 10, scale = 0)
	public Long getMeetingId() {
		return this.meetingId;
	}

	public void setMeetingId(Long meetingId) {
		this.meetingId = meetingId;
	}

	@Column(name = "WORKER_CODE", length = 16)
	public String getWorkerCode() {
		return this.workerCode;
	}

	public void setWorkerCode(String workerCode) {
		this.workerCode = workerCode;
	}

	@Column(name = "DEP_CODE", length = 20)
	public String getDepCode() {
		return this.depCode;
	}

	public void setDepCode(String depCode) {
		this.depCode = depCode;
	}

	@Column(name = "REASON", length = 200)
	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Column(name = "MAKEUP_RECORD", length = 400)
	public String getMakeupRecord() {
		return this.makeupRecord;
	}

	public void setMakeupRecord(String makeupRecord) {
		this.makeupRecord = makeupRecord;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}