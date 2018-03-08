package power.ejb.run.securityproduction;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SpJMonthSafeMeeting entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "SP_J_MONTH_SAFE_MEETING")
public class SpJMonthSafeMeeting implements java.io.Serializable {

	// Fields

	private Long meetingId;
	private String depCode;
	private Date meetingDate;
	private String meetingAddress;
	private String moderator;
	private String recordBy;
	private String content;
	private String memo;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public SpJMonthSafeMeeting() {
	}

	/** minimal constructor */
	public SpJMonthSafeMeeting(Long meetingId) {
		this.meetingId = meetingId;
	}

	/** full constructor */
	public SpJMonthSafeMeeting(Long meetingId, String depCode,
			Date meetingDate, String meetingAddress, String moderator,
			String recordBy, String content, String memo, String enterpriseCode) {
		this.meetingId = meetingId;
		this.depCode = depCode;
		this.meetingDate = meetingDate;
		this.meetingAddress = meetingAddress;
		this.moderator = moderator;
		this.recordBy = recordBy;
		this.content = content;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "MEETING_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getMeetingId() {
		return this.meetingId;
	}

	public void setMeetingId(Long meetingId) {
		this.meetingId = meetingId;
	}

	@Column(name = "DEP_CODE", length = 20)
	public String getDepCode() {
		return this.depCode;
	}

	public void setDepCode(String depCode) {
		this.depCode = depCode;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "MEETING_DATE", length = 7)
	public Date getMeetingDate() {
		return this.meetingDate;
	}

	public void setMeetingDate(Date meetingDate) {
		this.meetingDate = meetingDate;
	}

	@Column(name = "MEETING_ADDRESS", length = 200)
	public String getMeetingAddress() {
		return this.meetingAddress;
	}

	public void setMeetingAddress(String meetingAddress) {
		this.meetingAddress = meetingAddress;
	}

	@Column(name = "MODERATOR", length = 16)
	public String getModerator() {
		return this.moderator;
	}

	public void setModerator(String moderator) {
		this.moderator = moderator;
	}

	@Column(name = "RECORD_BY", length = 16)
	public String getRecordBy() {
		return this.recordBy;
	}

	public void setRecordBy(String recordBy) {
		this.recordBy = recordBy;
	}

	@Column(name = "CONTENT", length = 2000)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "MEMO", length = 1000)
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